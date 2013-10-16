/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import java.awt.image.RenderedImage;

import javax.media.jai.ROI;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.MosaicDescriptor;

import org.geotools.image.ImageWorker;


/**
 * Handles the way footprints should be treated
 */
public enum FootprintBehavior {
    
    
    None(false),
    Cut(true),
    Transparent(true) {
        @Override
        public RenderedImage postProcessMosaic(RenderedImage mosaic, ROI overallROI) {
            
            // force the current image in RGB or Gray
            final ImageWorker imageWorker = new ImageWorker(mosaic);
            imageWorker.forceColorSpaceRGB().forceComponentColorModel();
            RenderedImage componentImage = imageWorker.getRenderedImage();

            // do we already have a alpha band in the input image?
            if (componentImage.getColorModel().hasAlpha()) {
                ImageWorker iwAlpha = new ImageWorker(componentImage);
                RenderedImage alpha = iwAlpha.retainLastBand().getRenderedImage();
                RenderedOp maskedAlpha = MosaicDescriptor.create(new RenderedImage[] {alpha}, MosaicDescriptor.MOSAIC_TYPE_OVERLAY, null, new ROI[] {overallROI}, null, null, null);
                
                imageWorker.retainBands(componentImage.getColorModel().getNumColorComponents());
                imageWorker.addBand(maskedAlpha, false);
            } else {
                // turn the roi into a single band image and add it
                final RenderedImage alpha = new ImageWorker(overallROI.getAsImage())
                    .forceComponentColorModel().retainFirstBand().getRenderedImage();
                imageWorker.addBand(alpha, false);
            }

            RenderedImage result = imageWorker.getRenderedImage();
            return result;
        }
        
        @Override
        public RenderedImage postProcessBlankResponse(RenderedImage finalImage) {
            // force the current image in RGB or Gray
            final ImageWorker iw = new ImageWorker(finalImage);
            iw.forceColorSpaceRGB().forceComponentColorModel();
            RenderedImage blank = iw.getRenderedImage();
            if(!blank.getColorModel().hasAlpha()) {
                RenderedImage alpha = new ImageWorker(blank).retainFirstBand().getRenderedImage();
                iw.addBand(alpha, false);
            }
            
            RenderedImage result = iw.getRenderedImage();
            return result;
        }
        
    };
    
    
    private boolean handleFootprints;
    
    FootprintBehavior(boolean handleFootprints) {
        this.handleFootprints = handleFootprints;
    }
    
    public boolean handleFootprints() {
        return handleFootprints;
    }
    
    /**
     * Retrieves the default {@link FootprintBehavior}.
     * 
     * @return the default {@link FootprintBehavior}.
     */
    public static FootprintBehavior getDefault(){
        return None;
    }
    
    /**
     * Retrieves the possible values as Strings.
     * 
     * @return an arrays of {@link String} that contains the representation of each value.
     */
    public static String[] valuesAsStrings() {
        final FootprintBehavior[] values = FootprintBehavior.values();
        final String[] valuesS = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            valuesS[i] = values[i].toString();
        }
        return null;
    }
    
    /**
     * Applies post processing to the result mosaic, eventually making certain areas transparent
     * @param mosaic
     * @param overallROI
     * @return
     */
    public RenderedImage postProcessMosaic(RenderedImage mosaic, ROI overallROI) {
        return mosaic;
    }

    /**
     * Post processes a blank image response, eventually making it transparent
     * @param finalImage
     * @return
     */
    public RenderedImage postProcessBlankResponse(RenderedImage finalImage) {
        return finalImage;
    }

}
