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
import java.awt.Shape;
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
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.processing.CannotCropException;
import org.geotools.coverage.processing.Operation2D;
import org.geotools.coverage.processing.OperationJAI;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.resources.coverage.FeatureUtilities;
import org.geotools.resources.coverage.IntersectUtils;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.opengis.coverage.Coverage;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.geometry.Envelope;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.parameter.InvalidParameterTypeException;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * The crop operation is responsible for selecting geographic subarea of the
 * source coverage. The CoverageCrop operation does not merely wrap the JAI Crop
 * operation but it goes beyond that as far as capabilities.
 *
 * <p>
 * The key point is that the CoverageCrop operation aims to perform a spatial
 * crop, i.e. cropping the underlying raster by providing a spatial
 * {@link Envelope} (if the envelope is not 2D only the 2D part of it will be
 * used). This means that, depending on the grid-to-world transformation
 * existing for the raster we want to crop, the crop area in the raster space
 * might not be a rectangle, hence JAI's crop may not suffice in order to shrink
 * the raster area we would obtain. For this purpose this operation make use of
 * either the JAI's Crop or Mosaic operations depending on the conditions in
 * which we are working.
 *
 *
 * <p>
 * <strong>Meaning of the ROI_OPTIMISATION_TOLERANCE parameter</strong> <br>
 * In general if the grid-to-world transform is a simple scale and translate
 * using JAI's crop should suffice, but when the g2w transform contains
 * rotations or skew then we need something more elaborate since a rectangle in
 * model space may not map to a rectangle in raster space. We would still be
 * able to crop using JAI's crop on this polygon bounds but, depending on how
 * this rectangle is built, we would be highly inefficient. In order to overcome
 * this problems we use a combination of JAI's crop and mosaic since the mosaic
 * can be used to crop a raster using a general ROI instead of a simple
 * rectangle. There is a negative effect though. Crop would not create a new
 * raster but simply forwards requests back to the original one (it basically
 * create a viewport on the source raster) while the mosaic operation creates a
 * new raster. We try to address this trade-off by providing the parameter
 * {@link Crop#ROI_OPTIMISATION_TOLERANCE}, which basically tells this
 * operation "Use the mosaic operation only if the area that we would load with
 * the Mosaic is strictly smaller then (ROI_OPTIMISATION_TOLERANCE)* A' where A'
 * is the area of the polygon resulting from converting the crop area from the
 * model space to the raster space.
 *
 * <p>
 * <strong>ROI</strong><br>
 * By providing a ROI parameter, the coverage can be cropped by any set of
 * polygons, even disjuncted ones. When the ROI is provided, the JAI's Mosaic
 * operation will be used.
 * <br>At least one between <i>Envelope</i> and <i>ROI</i> must be provided.
 * If both of them are provided, the resulting area will be the intersection
 * of them.
 * <br>ROI geometries must be in the same CRS as the source coverage.
 * <br>ROI must be any among a {@link com.vividsolutions.jts.geom.Polygon}, a
 * {@link MultiPolygon}, or a {@link GeometryCollection} of the two.
 * 
 * <p>
 * <strong>NOTE</strong> that in case we will use the Mosaic operation with a
 * ROI, such a ROI will be added as a synthetic property to the resulting
 * coverage. The key for this property will be GC_ROI and the type of the object
 * {@link Polygon}.
 * 
 * @source $URL$
 * @todo make this operation more t,z friendly
 * @version $Id$
 * @author Simone Giannecchini (GeoSolutions)
 * @author Emanuele Tajariol (GeoSolutions)
 * @since 2.3
 *
 * @see javax.media.jai.operator.CropDescriptor
 */
public class Crop extends Operation2D {
	/**
	 * Serial number for cross-version compatibility.
	 */
	private static final long serialVersionUID = 4466072819239413456L;

    public static final double EPS = 1E-3;

	private final static GeometryFactory GFACTORY;

	static {
		// getting default hints
		final Hints defaultHints = GeoTools.getDefaultHints();

		// check if someone asked us to use a specific precision model
		final Object o = defaultHints.get(Hints.JTS_PRECISION_MODEL);
        final PrecisionModel pm;
		if (o != null)
			pm = (PrecisionModel) o;
		else {
			pm = new PrecisionModel();
		}
		GFACTORY = new GeometryFactory(pm, 0);
	}

    public static final String PARAMNAME_ENVELOPE = "Envelope";
    public static final String PARAMNAME_ROI = "ROI";
    public static final String PARAMNAME_ROITOLERANCE = "ROITolerance";

	/**
	 * The parameter descriptor used to pass this operation the envelope to use
	 * when doing the spatial crop.
	 */
	public static final ParameterDescriptor<Envelope> CROP_ENVELOPE = new DefaultParameterDescriptor<Envelope>(
			Citations.GEOTOOLS, PARAMNAME_ENVELOPE,
			Envelope.class, // Value class
			null, // Array of valid values
			null, // Default value
			null, // Minimal value
			null, // Maximal value
			null, // Unit of measure
			true); // Parameter is optional

	/**
	 * The parameter descriptor used to pass this operation the polygons(s) to use
	 * when doing the spatial crop.
     *
     * If set, the intersection of Envelope and ROI must not be empty.
     * The final output area will contain area inside the Envelope AND the ROI.
     *
     * The parameter shall be a Polygon instance, or a GeometryCollection holding Polygons
	 */
	public static final ParameterDescriptor<Geometry> CROP_ROI = new DefaultParameterDescriptor<Geometry>(
			Citations.JAI, PARAMNAME_ROI,
			Geometry.class, // Value class
			null, // Array of valid values
			null, // Default value
			null, // Minimal value
			null, // Maximal value
			null, // Unit of measure
			true); // Parameter is optional

	/**
	 * The parameter descriptor used to tell this operation to optimize the crop
	 * using a Mosaic in where the area of the image we would not load is smaller
	 * than ROI_OPTIMISATION_TOLERANCE*FULL_CROP.
	 */
	public static final ParameterDescriptor<Double> ROI_OPTIMISATION_TOLERANCE = new DefaultParameterDescriptor<Double>(
			Citations.GEOTOOLS, PARAMNAME_ROITOLERANCE,
			Double.class, // Value class
			null, // Array of valid values
			0.6,  // Default value
			0.0,  // Minimal value
			1.0,  // Maximal value
			null, // Unit of measure
			true); // Parameter is optional

    /**
     * Constructs a default {@code "Crop"} operation.
     */
	public Crop() {
		super(new DefaultParameterDescriptorGroup(Citations.GEOTOOLS,
				"CoverageCrop", new ParameterDescriptor[] { SOURCE_0,
						CROP_ENVELOPE, CROP_ROI,
						ROI_OPTIMISATION_TOLERANCE }));

	}

	/**
	 * Applies a crop operation to a coverage.
	 *
	 * @see org.geotools.coverage.processing.AbstractOperation#doOperation(org.opengis.parameter.ParameterValueGroup,
	 *      org.geotools.factory.Hints)
	 */
	@SuppressWarnings("unchecked")
	public Coverage doOperation(ParameterValueGroup parameters, Hints hints) {

        final Geometry cropRoi; // extracted from parameters
        GeneralEnvelope cropEnvelope = null; // extracted from parameters
        final GridCoverage2D source; // extracted from parameters
        final double roiTolerance = parameters.parameter(Crop.PARAMNAME_ROITOLERANCE).doubleValue();

		// /////////////////////////////////////////////////////////////////////
		//
		// Assigning and checking input parameters
		//
		// ///////////////////////////////////////////////////////////////////



		// source coverage
        final ParameterValue sourceParameter = parameters.parameter("Source");
        if (sourceParameter == null
                || !(sourceParameter.getValue() instanceof GridCoverage2D)) {
            throw new CannotCropException(Errors.format(ErrorKeys.NULL_PARAMETER_$2, "Source", GridCoverage2D.class.toString()));
        }
        source = (GridCoverage2D) sourceParameter.getValue();

        // Check Envelope and ROI existence - we need at least one of them
        final ParameterValue envelopeParameter = parameters.parameter(PARAMNAME_ENVELOPE);
        final ParameterValue roiParameter = parameters.parameter(PARAMNAME_ROI);

        if ( (envelopeParameter == null || envelopeParameter.getValue() == null) &&
             (roiParameter == null || roiParameter.getValue() ==null)                )
            throw new CannotCropException(Errors.format(ErrorKeys.NULL_PARAMETER_$2, PARAMNAME_ENVELOPE, GeneralEnvelope.class.toString()));

        Object envelope = envelopeParameter.getValue(); 
        if (envelope != null){
            if (envelope instanceof GeneralEnvelope){
                cropEnvelope = (GeneralEnvelope) envelope;
            } else if (envelope instanceof Envelope){
                cropEnvelope = new GeneralEnvelope((Envelope)envelope);
            } 
        }
        // may be null

        // Check crop ROI
        try {
            cropRoi = IntersectUtils.unrollGeometries((Geometry) roiParameter.getValue()); // may throw if format not correct
        } catch (IllegalArgumentException ex) {
            throw new CannotCropException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, PARAMNAME_ROI, ex.getMessage()), ex);
        }

        // Setting a GeneralEnvelope from ROI if needed
        if (cropRoi != null && cropEnvelope == null)  {
            Envelope e2d = JTS.getEnvelope2D(cropRoi.getEnvelopeInternal(), source.getCoordinateReferenceSystem());
            cropEnvelope = new GeneralEnvelope(e2d);
        }

		// /////////////////////////////////////////////////////////////////////
		//
		// Initialization
		//
		// We take the crop envelope and the source envelope then we check their
		// crs and we also check if they ever overlap.
		//
		// /////////////////////////////////////////////////////////////////////
		// envelope of the source coverage
		final Envelope2D sourceEnvelope = source.getEnvelope2D();
		// crop envelope
		Envelope2D destinationEnvelope = new Envelope2D(cropEnvelope);
		CoordinateReferenceSystem sourceCRS = sourceEnvelope.getCoordinateReferenceSystem();
		CoordinateReferenceSystem destinationCRS = destinationEnvelope.getCoordinateReferenceSystem();
		if (destinationCRS == null) {
			// Do not change the user provided object - clone it first.
			final Envelope2D ge = new Envelope2D(destinationEnvelope);
			destinationCRS = source.getCoordinateReferenceSystem2D();
			ge.setCoordinateReferenceSystem(destinationCRS);
			destinationEnvelope = ge;
		}

		// //
		//
		// Source and destination crs must be equals
		//
		// //
		if (!CRS.equalsIgnoreMetadata(sourceCRS, destinationCRS)) {
			throw new CannotCropException(Errors.format(ErrorKeys.MISMATCHED_ENVELOPE_CRS_$2, sourceCRS.getName().getCode(), destinationCRS.getName().getCode()));
		}

        if(cropRoi != null) {
            // TODO: check ROI SRID
        }

		// //
		//
		// Check the intersection and, if needed, do the crop operation.
		//
		// //
		final GeneralEnvelope intersectionEnvelope = new GeneralEnvelope((Envelope) destinationEnvelope);
		intersectionEnvelope.setCoordinateReferenceSystem(source.getCoordinateReferenceSystem());
		// intersect the envelopes
		intersectionEnvelope.intersect(sourceEnvelope);
		if (intersectionEnvelope.isEmpty())
			throw new CannotCropException(Errors.format(ErrorKeys.CANT_CROP));

        // intersect the ROI with the intersection envelope and throw an error if they do not intersect
        if(cropRoi != null) {
            final Geometry jis = JTS.shapeToGeometry(intersectionEnvelope.toRectangle2D(), cropRoi.getFactory());
            if( ! IntersectUtils.intersects(cropRoi, jis))
                throw new CannotCropException(Errors.format(ErrorKeys.CANT_CROP));
        }

		// //
		//
		// Get the grid-to-world transform by keeping into account translation
		// of grid geometry constructor for respecting OGC PIXEL-IS-CENTER
		// ImageDatum assumption.
		//
		// //
		final AffineTransform sourceCornerGridToWorld = (AffineTransform) ((GridGeometry2D) source.getGridGeometry()).getGridToCRS(PixelInCell.CELL_CORNER);
		
		// //
		//
		// I set the tolerance as half the scale factor of the grid-to-world
		// transform. This should more or less means in most cases "don't bother
		// to crop if the new envelope is as close to the old one that we go
		// deep under pixel size."
		//
		// //
		final double tolerance = XAffineTransform.getScale(sourceCornerGridToWorld);
		if (cropRoi != null || !intersectionEnvelope.equals(sourceEnvelope, tolerance / 2.0, false)) {
            cropEnvelope = intersectionEnvelope.clone();
			return buildResult(
					cropEnvelope, 
					cropRoi, 
					roiTolerance,
                    (hints instanceof Hints) ? (Hints) hints: new Hints(hints),
                    source,
                    sourceCornerGridToWorld);
		} else {
			// //
			//
			// Note that in case we don't crop at all, WE DO NOT UPDATE the
			// envelope. If we did we might end up doing multiple successive
			// crop without actually cropping the image but, still, we would
			// shrink the envelope each time. Just think about having a loop
			// that crops recursively the same coverage specifying each time an
			// envelope whose URC is only a a scale quarter close to the LLC of
			// the old one. We would never crop the raster but we would modify
			// the grid-to-world transform each time.
			//
			// //
			return source;
		}
	}

	/**
	 * Applies the band select operation to a grid coverage.
	 *
	 * @param cropEnvelope the target envelope; always not null
     * @param cropROI the target ROI shape; nullable
     * @param roiTolerance; as read from op's params
     * 
	 * @param sourceCoverage
	 *            is the source {@link GridCoverage2D} that we want to crop.
	 * @param hints
	 *            A set of rendering hints, or {@code null} if none.
	 * @param sourceGridToWorldTransform
	 *            is the 2d grid-to-world transform for the source coverage.
     *
	 * @return The result as a grid coverage.
	 */
	private static GridCoverage2D buildResult(
			final GeneralEnvelope cropEnvelope,
            final Geometry cropROI,
            final double roiTolerance,
			final Hints hints,
			final GridCoverage2D sourceCoverage,
			final AffineTransform sourceGridToWorldTransform) {

		//
		// Getting the source coverage and its child geolocation objects
		//
		final RenderedImage sourceImage = sourceCoverage.getRenderedImage();
		final GridGeometry2D sourceGridGeometry = ((GridGeometry2D) sourceCoverage.getGridGeometry());
		final GridEnvelope2D sourceGridRange = sourceGridGeometry.getGridRange2D();

		//
		// Now we try to understand if we have a simple scale and translate or a
		// more elaborated grid-to-world transformation n which case a simple
		// crop could not be enough, but we may need a more elaborated chain of
		// operation in order to do a good job. As an instance if we
        // have a rotation which is not multiple of PI/2 we have to use
        // the mosaic with a ROI
		//
	    final boolean isSimpleTransform = CoverageUtilities.isSimpleGridToWorldTransform(sourceGridToWorldTransform,EPS);

			// Do we need to explode the Palette to RGB(A)?
		//
		int actionTaken = 0;

		// //
		//
		// Layout
		//
		// //
		final RenderingHints targetHints = new RenderingHints(null);
		if(hints!=null)
			targetHints.add(hints);
		final ImageLayout layout = initLayout(sourceImage, targetHints);
		targetHints.put(JAI.KEY_IMAGE_LAYOUT, layout);

		//
		// prepare the processor to use for this operation
		//
		final JAI processor = OperationJAI.getJAI(targetHints);
		final boolean useProvidedProcessor = !processor.equals(JAI.getDefaultInstance());

		try {

            if (cropROI != null) {
                // replace the cropEnvelope with the envelope of the intersection
                // of the ROI and the cropEnvelope.
                // Remember that envelope(intersection(roi,cropEnvelope)) != intersection(cropEnvelope, envelope(roi))
                final Polygon modelSpaceROI = FeatureUtilities.getPolygon(cropEnvelope, GFACTORY);
                Geometry intersection =  IntersectUtils.intersection(cropROI, modelSpaceROI);
                Envelope2D e2d = JTS.getEnvelope2D(intersection.getEnvelopeInternal(), cropEnvelope.getCoordinateReferenceSystem());
                GeneralEnvelope ge = new GeneralEnvelope((org.opengis.geometry.Envelope)e2d);
                cropEnvelope.setEnvelope(ge);
            }

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

            // //
            //
            // It is worth to point out that doing a crop the G2W transform
            // should not change while the envelope might change as
            // a consequence of the rounding of the underlying image datum
            // which uses integer factors or in case the G2W is very
            // complex. Note that we will always strive to
            // conserve the original grid-to-world transform.
            //
            // //

            // we do not have to crop in this case (should not really happen at
            // this time)
            if (finalRasterArea.equals(sourceGridRange) && isSimpleTransform && cropROI==null)
                    return sourceCoverage;

            // //
            //
            // if I get here I have something to crop
            // using the world-to-grid transform for going from envelope to the
            // new grid range.
            //
            // //
            final double minX = finalRasterArea.getMinX();
            final double minY = finalRasterArea.getMinY();
            final double width = finalRasterArea.getWidth();
            final double height =finalRasterArea.getHeight();

			// //
			//
			// Check if we need to use mosaic or crop
			//
			// //
			final PlanarImage croppedImage;
			final ParameterBlock pbj = new ParameterBlock();
			pbj.addSource(sourceImage);
			java.awt.Polygon rasterSpaceROI=null;
            String operatioName=null;
            if (!isSimpleTransform || cropROI!=null) {
                // /////////////////////////////////////////////////////////////////////
                //
                // We don't have a simple scale and translate transform, JAI
                // crop MAY NOT suffice. Let's decide whether or not we'll use
                // the Mosaic.
                //
                // /////////////////////////////////////////////////////////////////////
                Polygon modelSpaceROI = FeatureUtilities.getPolygon(cropEnvelope, GFACTORY);

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
				final boolean doMosaic = decideJAIOperation(roiTolerance, rasterSpaceROI.getBounds2D(), points);
				if (doMosaic || cropROI != null) {
					// prepare the params for the mosaic
                    final ROI[] roiarr;
                    try {
                        if(cropROI != null) {
                            final Shape cropRoiLS2 = new LiteShape2(cropROI, ProjectiveTransform.create(sourceWorldToGridTransform), null, false);
                            final ROIShape cropRS = new ROIShape(cropRoiLS2);
                            roiarr = new ROI[]{cropRS};
                        } else {
                            final ROIShape roi = new ROIShape(rasterSpaceROI);
                            roiarr = new ROI[]{roi};
                        }
                    } catch (FactoryException ex) {
						throw new CannotCropException(Errors.format(ErrorKeys.CANT_CROP), ex);
                    }
					pbj.add(MosaicDescriptor.MOSAIC_TYPE_OVERLAY);
					pbj.add(null);
					pbj.add(roiarr);
					pbj.add(null);
 					pbj.add(CoverageUtilities.getBackgroundValues(sourceCoverage));

 					//prepare the final layout
					final Rectangle bounds = rasterSpaceROI.getBounds2D().getBounds();
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
            // //
            //
            // Apply operation
            //
            // //
            if (!useProvidedProcessor) {
                croppedImage = JAI.create(operatioName, pbj, targetHints);
            } else {
                croppedImage = processor.createNS(operatioName, pbj, targetHints);
            }

		    //conserve the input grid to world transformation
            return new GridCoverageFactory(hints).create(
            		sourceCoverage.getName(), 
            		croppedImage,
            		new GridGeometry2D(
		                    new GridEnvelope2D(croppedImage.getBounds()),
		                    sourceGridGeometry.getGridToCRS2D(PixelOrientation.CENTER),
		                    sourceCoverage.getCoordinateReferenceSystem()
		            ),
                    (GridSampleDimension[]) (actionTaken == 1 ?null :sourceCoverage.getSampleDimensions().clone()),
                    new GridCoverage[] { sourceCoverage },
                    rasterSpaceROI != null ?Collections.singletonMap("GC_ROI", rasterSpaceROI) :null
            
            );
      
		} catch (TransformException e) {
			throw new CannotCropException(Errors.format(ErrorKeys.CANT_CROP), e);
		} catch (NoninvertibleTransformException e) {
			throw new CannotCropException(Errors.format(ErrorKeys.CANT_CROP), e);
		}

	}

    /**
     * Initialize a layout object using the provided {@link RenderedImage} and the provided {@link Hints}.
     *
     * @param sourceImage {@link RenderedImage} to use for initializing the returned layout.
     * @param hints  {@link Hints} to use for initializing the returned layout.
     * @return an {@link ImageLayout} instance.
     */
    private static ImageLayout initLayout(
    		final RenderedImage sourceImage,
            final RenderingHints hints) {
    	
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
            final double roiTolerance, 
            final Rectangle2D finalGridRange,
            final List<Point2D> points) throws InvalidParameterTypeException,
            ParameterNotFoundException {
        final double cropArea = finalGridRange.getWidth()* finalGridRange.getHeight();
        final double roiArea = Math.abs(FeatureUtilities.area((Point2D[]) points.toArray(new Point2D[] {})));
        final boolean doMosaic = roiTolerance * cropArea > roiArea;
        return doMosaic;
    }

}
