/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 20011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.utils.imageoverviews;

import java.awt.RenderingHints;
import java.awt.image.RenderedImage;

import javax.media.jai.BorderExtender;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.TileCache;
import javax.media.jai.operator.ScaleDescriptor;

import org.geotools.resources.image.ImageUtilities;
/**
 * Utilities methods for the {@link OverviewsEmbedder} class.
 * 
 * @author Simone Giannecchini,GeoSolutions.
 */
class Utils {

    /**
     * This method retiles the original image using a specified tile width and
     * height.
     * 
     * @param Original
     *            image to be tiled or retiled.
     * @param tileWidth
     *            Tile width.
     * @param tileHeight
     *            Tile height.
     * @param tileGrdiOffseX
     * @param tileGrdiOffseY
     * @param interpolation
     *            Interpolation method used.
     * 
     * @return RenderedOp containing the chain to obtain the tiled image.
     */
    static ImageLayout createTiledLayout(
    		final int tileWidth, 
    		final int tileHeight,
    		final int tileGrdiOffseX, 
    		final int tileGrdiOffseY) {
    
    	// //
    	//
    	// creating a new layout for this image
    	// using tiling
    	//
    	// //
    	ImageLayout layout = new ImageLayout();
    
    	// //
    	//
    	// changing parameters related to the tiling
    	//
    	//
    	// //
    	layout.setTileGridXOffset(tileGrdiOffseX);
    	layout.setTileGridYOffset(tileGrdiOffseY);
    	layout.setValid(ImageLayout.TILE_GRID_X_OFFSET_MASK);
    	layout.setValid(ImageLayout.TILE_GRID_Y_OFFSET_MASK);
    	layout.setTileWidth(tileWidth);
    	layout.setTileHeight(tileHeight);
    	layout.setValid(ImageLayout.TILE_HEIGHT_MASK);
    	layout.setValid(ImageLayout.TILE_WIDTH_MASK);
    
    	return layout;
    }

    /**
     * Creating the scale operation using the FilteredSubSample operation with a
     * null filter, which basically does not do any filtering. This is a hint I
     * found on the JAI mailing list, a SUN engineer suggested to use this
     * instead of scale since it uses a integer factor which is easier for the
     * library to handle than a float scale factor like Scale operation is
     * using.
     * 
     * @param src
     *            Source image to be scaled.
     * @param scaleTC 
     * @param factor
     *            Scale factor.
     * @param interpolation
     *            Interpolation used.
     * @param hints
     *            Hints provided to this method.
     * 
     * @return The scaled image.
     */
    static RenderedOp filteredSubsample(RenderedImage src, TileCache scaleTC, int downsampleStep,float[] lowPassFilter ) {
    	final RenderingHints newHints = new RenderingHints(JAI.KEY_TILE_CACHE,scaleTC);
    	newHints.add(ImageUtilities.DONT_REPLACE_INDEX_COLOR_MODEL);
    	
    	// using filtered subsample operator to do a subsampling
    	final ParameterBlockJAI pb = new ParameterBlockJAI("filteredsubsample");
    	pb.addSource(src);
    	pb.setParameter("scaleX", new Integer(downsampleStep));
    	pb.setParameter("scaleY", new Integer(downsampleStep));
    	pb.setParameter("qsFilterArray", lowPassFilter);
    	pb.setParameter("Interpolation", new InterpolationNearest());
    	return JAI.create("filteredsubsample", pb,newHints);
    }

    /**
     * Creating the scale operation using the FilteredSubSample operation with a
     * null filter, which basically does not do any filtering. This is a hint I
     * found on the JAI mailing list, a SUN engineer suggested to use this
     * instead of scale since it uses a integer factor which is easier for the
     * library to handle than a float scale factor like Scale operation is
     * using.
     * 
     * @param src
     *            Source image to be scaled.
     * @param scaleTC 
     * @param factor
     *            Scale factor.
     * @param interpolation
     *            Interpolation used.
     * @param hints
     *            Hints provided to this method.
     * 
     * @return The scaled image.
     */
    static RenderedOp scaleAverage(RenderedImage src, TileCache scaleTC, int downsampleStep, BorderExtender borderExtender) {
    	final RenderingHints newHints = new RenderingHints(JAI.KEY_TILE_CACHE,scaleTC);
    	newHints.add(ImageUtilities.DONT_REPLACE_INDEX_COLOR_MODEL);
    	newHints.add(new RenderingHints(JAI.KEY_BORDER_EXTENDER, borderExtender));
    	// using filtered subsample operator to do a subsampling
    	final ParameterBlockJAI pb = new ParameterBlockJAI("SubsampleAverage");
    	pb.addSource(src);
    	pb.setParameter("scaleX", new Double(1.0 / downsampleStep));
    	pb.setParameter("scaleY", new Double(1.0 / downsampleStep));
    	return JAI.create("SubsampleAverage", pb, newHints);
    }

    /**
    	 * This methods built up a RenderedOp for subsampling an image in order to
    	 * create various previes. I wanted to use the filtered subsample but It was
    	 * giving me problems in the native libraries therefore I am doing a two
    	 * steps downsampling:
    	 * 
    	 * Step 1: low pass filtering.
    	 * 
    	 * Step 2: Subsampling.
    	 * 
    	 * @param src
    	 *            Image to subsample.
    	 * @param scaleTC 
    	 * @param scale
    	 *            Scale factor.
    	 * @param interpolation
    	 *            Interpolation method used.
    	 * @param tileHints
    	 *            Hints provided.
    	 * 
    	 * @return The subsampled RenderedOp.
    	 */
    	static RenderedOp subsample(RenderedOp src, TileCache scaleTC, final Interpolation interpolation, int downsampleStep,BorderExtender borderExtender) {
    		final RenderingHints newHints = new RenderingHints(JAI.KEY_TILE_CACHE,scaleTC);
    		newHints.add(ImageUtilities.DONT_REPLACE_INDEX_COLOR_MODEL);
    		newHints.add(new RenderingHints(JAI.KEY_BORDER_EXTENDER, borderExtender));
    		
    		return ScaleDescriptor.create(src, 
    		        Float.valueOf(1.0f/downsampleStep), 
    		        Float.valueOf(1.0f/downsampleStep), 
    		        Float.valueOf(0.0f), 
    		        Float.valueOf(0.0f), 
    		        interpolation, 
    		        newHints);
    		
    //		// using filtered subsample operator to do a subsampling
    //		final ParameterBlockJAI pb = new ParameterBlockJAI("filteredsubsample");
    //		pb.addSource(src);
    //		pb.setParameter("scaleX", Integer.valueOf(downsampleStep));
    //		pb.setParameter("scaleY", Integer.valueOf(downsampleStep));
    //		pb.setParameter("qsFilterArray", new float[] { 1.0f });
    //		pb.setParameter("Interpolation", interpolation);
    //		// remember to add the hint to avoid replacement of the original
    //		// IndexColorModel
    //		// in future versions we might want to make this parametrix XXX TODO
    //		// @task
    //		return JAI.create("filteredsubsample", pb, newHints);
    	}

}
