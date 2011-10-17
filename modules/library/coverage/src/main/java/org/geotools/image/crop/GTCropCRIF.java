/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2010, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.image.crop;

import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.media.jai.JAI;

/**
 * The image factory for the GTCrop operator.
 * 
 * @author Andrea Aime
 * @since 2.7.2
 *
 * @source $URL$
 */
public class GTCropCRIF implements RenderedImageFactory {

    public GTCropCRIF() {
    }

    /**
     * Creates a new instance of {@link GTCropOpImage} in the rendered layer.
     * 
     * @param paramBlock
     *            parameter block with parameters minx, miny, width height
     * 
     * @param renderHints
     *            optional rendering hints which may be used to pass down a tile scheduler and tile
     *            cache
     */
    public RenderedImage create(ParameterBlock paramBlock, RenderingHints renderingHints) {
        RenderedImage image = (RenderedImage) paramBlock.getSource(0);
        float x = paramBlock.getFloatParameter(GTCropDescriptor.X_ARG);
        float y = paramBlock.getFloatParameter(GTCropDescriptor.Y_ARG);
        float width = paramBlock.getFloatParameter(GTCropDescriptor.WIDTH_ARG);
        float height = paramBlock.getFloatParameter(GTCropDescriptor.HEIGHT_ARG);

        // only leave tile cache and tile scheduler (we can't instantiate directly RenderingHints
        // as it won't allow for a null tile cache, even if the rest of JAI handles that peachy
        Map<Key, Object> tmp = new HashMap<RenderingHints.Key, Object>();
        for (Object key : renderingHints.keySet()) {
            if (key == JAI.KEY_TILE_CACHE || key == JAI.KEY_TILE_SCHEDULER) {
                tmp.put((Key) key, renderingHints.get(key));
            }
        }
        RenderingHints local = new RenderingHints(tmp);
        

        return new GTCropOpImage(image, x, y, width, height, local);
    }
}
