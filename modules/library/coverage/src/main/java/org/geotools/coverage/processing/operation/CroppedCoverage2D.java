/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.coverage.processing.operation;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.ROIShape;
import javax.media.jai.operator.MosaicDescriptor;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.processing.CannotCropException;
import org.geotools.coverage.processing.OperationJAI;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.resources.coverage.FeatureUtilities;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.image.ImageUtilities;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.parameter.InvalidParameterTypeException;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.InternationalString;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * This class is responsible for applying a crop operation to a source coverage
 * with a specified envelope.
 * 
 * @author Simone Giannecchini, GeoSolutions
 * 
 */
final class CroppedCoverage2D extends GridCoverage2D {
	/**
	 * Serial number for interoperability with different versions.
	 */
	private static final long serialVersionUID = -501742139906901754L;

	private final static PrecisionModel pm;

	private final static GeometryFactory gf;

        public static final double EPS = 1E-3;
	static {
		// getting default hints
		final Hints defaultHints = GeoTools.getDefaultHints();

		// check if someone asked us to use a specific precision model
		final Object o = defaultHints.get(Hints.JTS_PRECISION_MODEL);
		if (o != null)
			pm = (PrecisionModel) o;
		else {
			pm = new PrecisionModel();
		}
		gf = new GeometryFactory(pm, 0);

	}

	/**
	 * Convenience constructor for a {@link CroppedCoverage2D}.
	 * 
	 * @param name
	 *            for this {@link GridCoverage2D}.
	 * @param sourceRaster
	 *            is the raster that will be the back-end for this
	 *            {@link GridCoverage2D}.
	 * @param croppedGeometry
	 *            is the {@link GridGeometry2D} for this new
	 *            {@link CroppedCoverage2D}.
	 * @param source
	 *            is the original {@link GridCoverage2D}.
	 * @param actionTaken
	 *            it is used to do the necessary postprocessing for supporting
	 *            paletted images.
	 * @param rasterSpaceROI
	 *            in case we used the JAI's mosaic with a ROI this
	 *            {@link java.awt.Polygon} will hold the used roi.
	 * @param hints
         *          An optional set of hints, or {@code null} if none.
	 */
	private CroppedCoverage2D(InternationalString name,
			PlanarImage sourceRaster, GridGeometry2D croppedGeometry,
			GridCoverage2D source, int actionTaken,
			java.awt.Polygon rasterSpaceROI, Hints hints) {
		super(name.toString(), sourceRaster, croppedGeometry,
				(GridSampleDimension[]) (actionTaken == 1 ? null : source
						.getSampleDimensions().clone()),
				new GridCoverage[] { source },
				rasterSpaceROI != null ? Collections.singletonMap("GC_ROI",
						rasterSpaceROI) : null,hints);
	}

	/**
	 * Applies the band select operation to a grid coverage.
	 * 
	 * @param parameters
	 *            List of name value pairs for the parameters.
	 * @param sourceCoverage
	 *            is the source {@link GridCoverage2D} that we want to crop.
	 * @param hints
	 *            A set of rendering hints, or {@code null} if none.
	 * @param sourceGridToWorldTransform
	 *            is the 2d grid-to-world transform for the source coverage.
	 * @param scaleFactor
	 *            for the grid-to-world transform.
	 * @return The result as a grid coverage.
	 */
	static GridCoverage2D create(
			final ParameterValueGroup parameters,
			final Hints hints, 
			final GridCoverage2D sourceCoverage,
			final AffineTransform sourceGridToWorldTransform, 
			final double scaleFactor) {

		// /////////////////////////////////////////////////////////////////////
		//
		// Getting the source coverage and its child geolocation objects
		//
		// /////////////////////////////////////////////////////////////////////
		final RenderedImage sourceImage = sourceCoverage.getRenderedImage();
		final Envelope2D sourceEnvelope = sourceCoverage.getEnvelope2D();
		final GridGeometry2D sourceGridGeometry = ((GridGeometry2D) sourceCoverage.getGridGeometry());
		final GridEnvelope2D sourceGridRange = sourceGridGeometry.getGridRange2D();


		// /////////////////////////////////////////////////////////////////////
		//
		// Now we try to understand if we have a simple scale and translate or a
		// more elaborated grid-to-world transformation n which case a simple
		// crop could not be enough, but we may need a more elaborated chain of
		// operation in order to do a good job. As an instance if we
        // have a rotation which is not multiple of PI/2 we have to use
        // the mosaic with a ROI
		//
		// /////////////////////////////////////////////////////////////////////
	    final boolean isSimpleTransform = CoverageUtilities.isSimpleGridToWorldTransform(sourceGridToWorldTransform,EPS);

		// /////////////////////////////////////////////////////////////////////
		//
		// Managing Hints, especially for output coverage's layout purposes
		//
		// /////////////////////////////////////////////////////////////////////
		final RenderingHints targetHints = prepareHints(hints, sourceImage);


		// /////////////////////////////////////////////////////////////////////
		//
		// Do we need to explode the Palette to RGB(A)?
		//
		// /////////////////////////////////////////////////////////////////////
		int actionTaken = 0;

		// //
		//
		// Layout
		//
		// //
		ImageLayout layout = initLayout(sourceImage, targetHints);
		targetHints.put(JAI.KEY_IMAGE_LAYOUT, layout);

		// /////////////////////////////////////////////////////////////////////
		//
		// prepare the processor to use for this operation
		//
		// /////////////////////////////////////////////////////////////////////
		final JAI processor = OperationJAI.getJAI(targetHints);
		final boolean useProvidedProcessor = !processor.equals(JAI.getDefaultInstance());

		try {
			// /////////////////////////////////////////////////////////////////////
			//
			// Get the crop envelope and do your thing!
			//
			// /////////////////////////////////////////////////////////////////////
			final GeneralEnvelope cropEnvelope = (GeneralEnvelope) parameters.parameter("Envelope").getValue();

			// //
			//
			// Do we actually need to crop?
			//
			// If the intersection envelope is empty or if the intersection
			// envelope is (almost) the same of the original envelope we just
			// return (with different return values).
			//
			// //
			if (cropEnvelope.isEmpty())
				throw new CannotCropException(Errors.format(ErrorKeys.CANT_CROP));
			if (cropEnvelope.equals(sourceEnvelope, scaleFactor / 2.0, false))
				return sourceCoverage;

			// //
			//
			// Build the new range by keeping into
			// account translation of grid geometry constructor for respecting
			// OGC PIXEL-IS-CENTER ImageDatum assumption.
			//
			// //
			final AffineTransform sourceWorldToGridTransform = sourceGridToWorldTransform.createInverse();
			
			// //
			//
            // finalRasterArea will hold the smallest rectangular integer raster area that contains the floating point raster
			// area which we obtain when applying the world-to-grid transform to the cropEnvelope. Note that we need to intersect 
			// such an area with the area covered by the source coverage in order to be sure we do not try to crop outside the 
			// bounds of the source raster.
			//
			// //
            final Rectangle2D finalRasterAreaDouble = XAffineTransform.transform(sourceWorldToGridTransform, cropEnvelope.toRectangle2D(),null);
            final Rectangle finalRasterArea = finalRasterAreaDouble.getBounds();
            
            // intersection with the original range in order to not try to crop outside the image bounds
            Rectangle.intersect(finalRasterArea, sourceGridRange, finalRasterArea);
            if(finalRasterArea.isEmpty())
            	throw new CannotCropException(Errors.format(ErrorKeys.CANT_CROP));
            

            // ////////////////////////////////////////////////////////////////////
            //
            //
            // It is worth to point out that doing a crop the G2W transform
            // should not change while the envelope might change as
            // a consequence of the rounding of the underlying image datum
            // which uses integer factors or in case the G2W is very
            // complex. Note that we will always strive to 
            // conserve the original grid-to-world transform.
            //
            // ////////////////////////////////////////////////////////////////////

            // we do not have to crop in this case (should not really happen at
            // this time)
            if (finalRasterArea.equals(sourceGridRange) && isSimpleTransform)
                    return sourceCoverage;

            // ////////////////////////////////////////////////////////////////////
            //
            // if I get here I have something to crop
            // using the world-to-grid transform for going from envelope to the
            // new grid range.
            //
            // ////////////////////////////////////////////////////////////////////
            final double minX = finalRasterArea.getMinX();
            final double minY = finalRasterArea.getMinY();
            final double width = finalRasterArea.getWidth();
            final double height =finalRasterArea.getHeight();
		

			// /////////////////////////////////////////////////////////////////////
			//
			// Check if we need to use mosaic or crop
			//
			// /////////////////////////////////////////////////////////////////////
			final PlanarImage croppedImage;
			final ParameterBlock pbj = new ParameterBlock();
			pbj.addSource(sourceImage);
			java.awt.Polygon rasterSpaceROI=null;
            String operatioName=null;
            if (!isSimpleTransform) {

				// /////////////////////////////////////////////////////////////////////
				//
				// We don't have a simple scale and translate transform, JAI
				// crop MAY NOT suffice. Let's decide whether or not we'll use
				// the Mosaic.
				//
				// /////////////////////////////////////////////////////////////////////
				// //
				//
				// Convert the crop envelope into a polygon and the use the
				// world-to-grid transform to get a ROI for the source coverage.
				//
				// //
				final Rectangle2D rect = cropEnvelope.toRectangle2D();
				final Coordinate[] coord = new Coordinate[] {
						new Coordinate(rect.getMinX(), rect.getMinY()),
						new Coordinate(rect.getMinX(), rect.getMaxY()),
						new Coordinate(rect.getMaxX(), rect.getMaxY()),
						new Coordinate(rect.getMaxX(), rect.getMinY()),
						new Coordinate(rect.getMinX(), rect.getMinY()) };
				final LinearRing ring = gf.createLinearRing(coord);
				final Polygon modelSpaceROI = new Polygon(ring, null, gf);

				// check that we have the same thing here
				assert modelSpaceROI.getEnvelopeInternal().equals(new ReferencedEnvelope(rect, cropEnvelope.getCoordinateReferenceSystem()));
				// //
				//
				// Now convert this polygon back into a shape for the source
				// raster space.
				//
				// //
				final List<Point2D> points = new ArrayList<Point2D>(5);
				rasterSpaceROI = FeatureUtilities.convertPolygonToPointArray(modelSpaceROI, ProjectiveTransform.create(sourceWorldToGridTransform), points);
				if(rasterSpaceROI==null||rasterSpaceROI.getBounds().isEmpty())
		            if(finalRasterArea.isEmpty())
		            	throw new CannotCropException(Errors.format(ErrorKeys.CANT_CROP));
				final boolean doMosaic = decideJAIOperation(parameters,rasterSpaceROI.getBounds2D(), points);
				if (doMosaic) {
				        assert isSimpleTransform==false;
					// prepare the params for the mosaic
					final ROIShape roi = new ROIShape(rasterSpaceROI);
					pbj.add(MosaicDescriptor.MOSAIC_TYPE_OVERLAY);
					pbj.add(null);
					pbj.add(new ROI[] { roi });
					pbj.add(null);
 					pbj.add(CoverageUtilities.getBackgroundValues(sourceCoverage));
	
 					//prepare the fina layout
					final Rectangle bounds = roi.getBounds2D().getBounds();
					Rectangle.intersect(bounds, sourceGridRange, bounds);	
					if(bounds.isEmpty())
						throw new CannotCropException(Errors.format(ErrorKeys.CANT_CROP));

					// we do not have to crop in this case (should not really happen at
                    // this time)
                    if (bounds.getBounds().equals(sourceGridRange) && isSimpleTransform)
                            return sourceCoverage;


        	        // nice trick, we use the layout to do the actual crop
                    final Rectangle boundsInt=bounds.getBounds();
					layout.setMinX(boundsInt.x);
					layout.setWidth(boundsInt.width );
					layout.setMinY(boundsInt.y);
					layout.setHeight( boundsInt.height);
					operatioName = "Mosaic";
				} 

			}
            //do we still have to set the operation name? If so that means we have to go for crop.
            if(operatioName==null) {
                // executing the crop
                pbj.add((float) minX);
                pbj.add((float) minY);
                pbj.add((float) width);
                pbj.add((float) height);
                operatioName = "Crop";
            }
            // /////////////////////////////////////////////////////////////////////
            //
            // Apply operation
            //
            // /////////////////////////////////////////////////////////////////////
            if (!useProvidedProcessor)
                    croppedImage = JAI.create(operatioName, pbj, targetHints);
            else
                    croppedImage = processor.createNS(operatioName, pbj,targetHints);
            
          
		    //conserve the input grid to world transformation
		    return new CroppedCoverage2D(
		            sourceCoverage.getName(),
		            croppedImage, 
		            new GridGeometry2D(
		                    new GridEnvelope2D(croppedImage.getBounds()), 
		                    sourceGridGeometry.getGridToCRS2D(PixelOrientation.CENTER),
		                    sourceCoverage.getCoordinateReferenceSystem()),
		            sourceCoverage, 
		            actionTaken, 
		            rasterSpaceROI,
		            hints);

		} catch (TransformException e) {
			throw new CannotCropException(Errors.format(ErrorKeys.CANT_CROP), e);
		} catch (NoninvertibleTransformException e) {
			throw new CannotCropException(Errors.format(ErrorKeys.CANT_CROP), e);
		}

	}

    /**
     * @param hints
     * @param sourceImage
     * @return
     */
    private static RenderingHints prepareHints(Hints hints,
            final RenderedImage sourceImage) {
        RenderingHints targetHints = ImageUtilities
				.getRenderingHints(sourceImage);
		if (targetHints == null) {
			targetHints = new RenderingHints(null);
		} 
		if (hints != null) {
			targetHints.add(hints);
		}
		
        return targetHints;
    }

    /**
     * Initialize a layout object using the provided {@link RenderedImage} and the provided {@link Hints}.
     * 
     * @param sourceImage {@link RenderedImage} to use for initializing the returned layout.
     * @param hints  {@link Hints} to use for initializing the returned layout.
     * @return an {@link ImageLayout} instance.
     */
    private static ImageLayout initLayout(final RenderedImage sourceImage,
            RenderingHints hints) {
        ImageLayout layout = (ImageLayout) hints.get(JAI.KEY_IMAGE_LAYOUT);
	if (layout != null) {
	    layout = (ImageLayout) layout.clone();
	 } else {
	     layout = new ImageLayout(sourceImage);
	     layout.unsetTileLayout();
	     // At this point, only the color model and sample model are left
	     // valid.
	 }
	// crop will ignore minx, miny width and height
	if ((layout.getValidMask() & 
	        (ImageLayout.TILE_WIDTH_MASK
	                | ImageLayout.TILE_HEIGHT_MASK
				| ImageLayout.TILE_GRID_X_OFFSET_MASK | ImageLayout.TILE_GRID_Y_OFFSET_MASK)) == 0) {
	    layout.setTileGridXOffset(layout.getMinX(sourceImage));
	    layout.setTileGridYOffset(layout.getMinY(sourceImage));
	    final int width = layout.getWidth(sourceImage);
	    final int height = layout.getHeight(sourceImage);
	    if (layout.getTileWidth(sourceImage) > width)
	        layout.setTileWidth(width);
	    if (layout.getTileHeight(sourceImage) > height)
	        layout.setTileHeight(height);
	}
        return layout;
    }

    /**
     * Decides whether we would benefit from using a mosaic instead of a crop
     * @param parameters
     * @param finalGridRange
     * @param points
     * @return
     * @throws InvalidParameterTypeException
     * @throws ParameterNotFoundException
     */
    private static boolean decideJAIOperation(
            final ParameterValueGroup parameters, Rectangle2D finalGridRange,
            final List<Point2D> points) throws InvalidParameterTypeException,
            ParameterNotFoundException {
        final double cropArea = finalGridRange.getWidth()
        		* finalGridRange.getHeight();
        final double roiArea = Math.abs(Crop.area((Point2D[]) points
        		.toArray(new Point2D[] {})));
        final double roiOpt = parameters.parameter("ROITolerance")
        		.doubleValue();
        final boolean doMosaic=roiOpt * cropArea > roiArea;
        return doMosaic;
    }
}
