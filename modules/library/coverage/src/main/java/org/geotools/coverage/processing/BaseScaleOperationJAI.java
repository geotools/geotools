/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.processing;

import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.util.Map;

import javax.media.jai.BorderExtender;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.OperationDescriptor;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.ViewType;
import org.geotools.metadata.iso.spatial.PixelTranslation;
import org.geotools.referencing.operation.LinearTransform;
import org.geotools.referencing.operation.transform.ConcatenatedTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.resources.image.ImageUtilities;
import org.opengis.coverage.processing.OperationNotFoundException;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.util.InternationalString;

/**
 * Base class for providing capabilities to scale {@link GridCoverage2D} objects
 * using JAI scale operations.
 * 
 * <p>
 * This class tries to handles all the problems related to scaling index-color
 * images in order to avoid strange results in the smoothest possible way by
 * performing color expansions under the hood as needed. It may also apply some
 * optimizations in case we were dealing with non-geo view of coverage.
 * 
 * @author Simone Giannecchini, GeoSolutions.
 * @source $URL$
 * @since 2.5
 */
public class BaseScaleOperationJAI extends OperationJAI {

	/**
	 * Serial number for interoperability with different versions.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for {@link BaseScaleOperationJAI}.
	 * 
	 * @param operation name of the {@link JAI} operation we wrap.
	 * @throws OperationNotFoundException
	 */
	public BaseScaleOperationJAI(String operation)
			throws OperationNotFoundException {
		super(operation);
	}

	/**
	 * Constructor for {@link BaseScaleOperationJAI}.
	 * 
	 * @param operation {@link OperationDescriptor} of the {@link JAI} operation we wrap.
	 */
	public BaseScaleOperationJAI(OperationDescriptor operation) {
		super(operation);
	}

	/**
	 * Constructor for {@link BaseScaleOperationJAI}.
	 * @param operation {@link OperationDescriptor} of the {@link JAI} operation we wrap.
	 * @param descriptor
	 */
	public BaseScaleOperationJAI(OperationDescriptor operation,
			ParameterDescriptorGroup descriptor) {
		super(operation, descriptor);
	}

	@Override
	protected GridCoverage2D deriveGridCoverage(GridCoverage2D[] sources, Parameters parameters) {
		
		
		// /////////////////////////////////////////////////////////////////////
		//
		// Getting the input parameters we might need
		//
		// /////////////////////////////////////////////////////////////////////
		int indexOfInterpolationParam;
		try{
			indexOfInterpolationParam=parameters.parameters
				.indexOfParam("Interpolation");
		}
		catch (IllegalArgumentException e) {
			indexOfInterpolationParam=-1;
		}
		
		int indexOfBorderExtenderParam;
		try{
			indexOfBorderExtenderParam=parameters.parameters
				.indexOfParam("BorderExtender");
		}
		catch (IllegalArgumentException e) {
			indexOfBorderExtenderParam=-1;
		}

		final Interpolation interpolation =(Interpolation) (indexOfInterpolationParam==-1?
					new InterpolationNearest():
						parameters.parameters.getObjectParameter("Interpolation")); ;
		final BorderExtender borderExtender= 
			(BorderExtender) (indexOfBorderExtenderParam!=-1? 
					parameters.parameters.getObjectParameter("BorderExtender"):
						BorderExtender.createInstance(BorderExtender.BORDER_COPY));
	
		// /////////////////////////////////////////////////////////////////////
		//
		// Getting the source coverage
		//
		// /////////////////////////////////////////////////////////////////////
		GridCoverage2D sourceCoverage = sources[PRIMARY_SOURCE_INDEX];
		// map to upper left to avoid loosing too much precision due to the fact
		// that the internal grid tow world maps from pixel centre.
		final MathTransform sourceG2W = (sourceCoverage.getGridGeometry())
				.getGridToCRS2D(PixelOrientation.UPPER_LEFT);
		RenderedImage sourceImage = sourceCoverage.getRenderedImage();
	
		// /////////////////////////////////////////////////////////////////////
		//
		// Do we need to explode the Palette to RGB(A)?
		//
		// /////////////////////////////////////////////////////////////////////
		ViewType strategy = CoverageUtilities.preferredViewForOperation(
				sourceCoverage, interpolation, false, parameters.hints);
		switch (strategy) {
		case PHOTOGRAPHIC:
			// //
			//
			// In this case I do not require an explicit color expansion since I
			// can leverage on the fact that the scale operation with latest
			// versions of JAI is one of the operations that perform automatic
			// color expansion.
			//
			// //
			break;
		case GEOPHYSICS:
			// in this case we need to go back the geophysics view of the
			// source coverage
                        // fallthrough same code than PACKED.
		case PACKED:
			// in this case we work on the non geophysics version because it
			// should be faster than working on the geophysics one. We are going
			// to work on a single band indexed image.
			sourceCoverage = sourceCoverage.view(strategy);
			sourceImage = sourceCoverage.getRenderedImage();
			break;
		}
	
		// /////////////////////////////////////////////////////////////////////
		//
		// Managing Hints, especially for output coverage's layout purposes.
		//
		// It is worthwhile to point out that layout hints for minx, miny, width
		// and height are NOT honored by the scale operation. The other
		// ImageLayout hints, like tileWidth and tileHeight, however are
		// honored.
		// /////////////////////////////////////////////////////////////////////
		RenderingHints targetHints = ImageUtilities.getRenderingHints(sourceImage);
		if (targetHints == null) 
			targetHints = new RenderingHints(null);
		if (parameters.hints != null) 
			targetHints.add(parameters.hints);
		ImageLayout layout = (ImageLayout) targetHints.get(JAI.KEY_IMAGE_LAYOUT);
		if (layout != null) {
			layout = (ImageLayout) layout.clone();
		} else {
			layout = new ImageLayout(sourceImage);
			layout.unsetTileLayout();
			// At this point, only the color model and sample model are left
			// valid.
		}
		if ((layout.getValidMask() & (ImageLayout.TILE_WIDTH_MASK
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
		targetHints.put(JAI.KEY_IMAGE_LAYOUT, layout);
		targetHints.put(JAI.KEY_BORDER_EXTENDER,borderExtender);
	
		// it is crucial to correctly manage the Hints to control the
		// replacement of IndexColorModel. It is worth to point out that setting
		// the JAI.KEY_REPLACE_INDEX_COLOR_MODEL hint to true is not enough to
		// force the operators to do an expansion.
		// If we explicitly provide an ImageLayout built with the source image
		// where the CM and the SM are valid. those will be employed overriding
		// a the possibility to expand the color model.
		if (strategy != ViewType.PHOTOGRAPHIC)
			targetHints.add(ImageUtilities.DONT_REPLACE_INDEX_COLOR_MODEL);
		else {
			targetHints.add(ImageUtilities.REPLACE_INDEX_COLOR_MODEL);
			layout.unsetValid(ImageLayout.COLOR_MODEL_MASK);
			layout.unsetValid(ImageLayout.SAMPLE_MODEL_MASK);
		}
	
		// /////////////////////////////////////////////////////////////////////
		//
		// Creating final grid coverage.
		//
		// /////////////////////////////////////////////////////////////////////
		RenderedImage image= createRenderedImage(parameters.parameters,targetHints);
	
	
		
		// /////////////////////////////////////////////////////////////////////
		//
		// Preparing the resulting grid to world transformation
		//
		//
		////
		//
		// This step is crucial for making a leap towards a more robust
		// implementation of the scaling operations and also towards their
		// integration as operation JAI subclasses.
		//
		// What we do here is quite trivial, we take into account the initial
		// Grid to World transform and then we concatenate to it a
		// transformation that takes into account the scaling we just performed.
		//
		// /////////////////////////////////////////////////////////////////////
		//concatenate and remap to pixel centre
		final PixelTranslation translationValue = PixelTranslation.getPixelTranslation(PixelOrientation.LOWER_RIGHT);
		final LinearTransform translation = ProjectiveTransform.create(AffineTransform.getTranslateInstance(translationValue.dx, translationValue.dy));
		final LinearTransform scale = ProjectiveTransform.create(
				AffineTransform.getScaleInstance(
						sourceImage.getWidth()/ (1.0 * image.getWidth()),
						sourceImage.getHeight()/ (1.0 * image.getHeight())
				)
		);
		final MathTransform finalTransform= ConcatenatedTransform.create(translation,ConcatenatedTransform.create(scale,sourceG2W));
		
	
		// /////////////////////////////////////////////////////////////////////
		//
		// Preparing the resulting coverage
		//
		// /////////////////////////////////////////////////////////////////////
	
	    /*
	     * Performs the operation using JAI and construct the new grid coverage.
	     * Uses the coordinate system from the main source coverage in order to
	     * preserve the extra dimensions (if any). The first two dimensions should
	     * be equal to the coordinate system set in the 'parameters' block.
	     */
	    final InternationalString      name = deriveName(sources, 0, parameters);
	    final Map<String,?>       properties = getProperties(image,parameters.crs,name,finalTransform,sources,parameters);
	    final GridCoverage2D result = getFactory(parameters.hints)
	            .create(name,        // The grid coverage name
	                    image,        // The underlying data
	                    parameters.crs,         // The coordinate system (may not be 2D).
	                    finalTransform,       // The grid transform (may not be 2D).
	                    (GridSampleDimension[]) (strategy == ViewType.PHOTOGRAPHIC ? null
	    						: sourceCoverage.getSampleDimensions().clone()),  // The sample dimensions
	                    sources,     // The source grid coverage.
	                    properties); // Properties
	
		
		// now let's see what we need to do in order to clean things up
		if (strategy == ViewType.GEOPHYSICS)
			return result.view(ViewType.PACKED);
		if (strategy == ViewType.PACKED)
			return result.view(ViewType.GEOPHYSICS);
		return result;		
	}

}
