/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.processing.jai;

import com.sun.media.jai.opimage.RIFUtil;
import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;
import javax.media.jai.BorderExtender;
import javax.media.jai.ImageLayout;
import org.geotools.processing.jai.TransparencyFillDescriptor.FillType;

/** @see TransparencyFillOpImage */
public class TransparencyFillRIF implements RenderedImageFactory {

    /** Constructor. */
    public TransparencyFillRIF() {}

    /**
     * Create a new instance of TransparencyFillOpImage in the rendered layer. This method satisfies
     * the implementation of RIF.
     *
     * @param paramBlock The source image and the dilation kernel.
     */
    public RenderedImage create(ParameterBlock paramBlock, RenderingHints renderHints) {
        // Get ImageLayout from renderHints if any.
        ImageLayout layout = RIFUtil.getImageLayoutHint(renderHints);

        // Get BorderExtender from renderHints if any.
        BorderExtender extender = RIFUtil.getBorderExtenderHint(renderHints);
        if (extender == null) {
            extender = BorderExtender.createInstance(BorderExtender.BORDER_COPY);
        }
        FillType type = TransparencyFillDescriptor.FILL_AVERAGE;
        Object param0 = paramBlock.getObjectParameter(0);
        if (param0 != null && param0 instanceof FillType) {
            type = (FillType) param0;
        }
        RenderedImage source = paramBlock.getRenderedSource(0);
        return new TransparencyFillOpImage(source, extender, type, renderHints, layout);
    }
}
