/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.jai;

// J2SE dependencies
import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;

// JAI dependencies
import javax.media.jai.CRIFImpl;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;


/**
 * The factory for the {@link Hysteresis} operation.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Lionel Flahaut (2ie Technologie, IRD)
 */
public class HysteresisCRIF extends CRIFImpl {
    /**
     * Constructs a default factory.
     */
    public HysteresisCRIF() {
    }

    /**
     * Creates a {@link RenderedImage} for the results of an imaging
     * operation for a given {@link ParameterBlock} and {@link RenderingHints}.
     */
    public RenderedImage create(final ParameterBlock param,
                                final RenderingHints hints)
    {
        final RenderedImage image = (RenderedImage)param.getSource(0);
        final ImageLayout  layout = (ImageLayout)hints.get(JAI.KEY_IMAGE_LAYOUT);
        final double      low = param.getDoubleParameter(0);
        final double     high = param.getDoubleParameter(1);
        final double padValue = param.getDoubleParameter(2);
        return new Hysteresis(image, layout, hints, low, high, padValue);
    }
}	
