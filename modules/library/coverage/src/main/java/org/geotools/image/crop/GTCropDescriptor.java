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
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.jai.JAI;
import javax.media.jai.OperationDescriptorImpl;
import javax.media.jai.OperationRegistry;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.registry.RenderedRegistryMode;

import org.geotools.image.jai.Registry;
import org.geotools.util.logging.Logging;

/**
 * Describes the "GTCrop" operation which performs a crop on an image, like the standard JAI Crop,
 * but does so respecting the tile scheduler and tile cache specified in the rendering hints
 * 
 * @author Andrea Aime
 * @since 2.7.2
 */
public class GTCropDescriptor extends OperationDescriptorImpl {
    
    static final Logger LOGGER = Logging.getLogger(GTCropDescriptor.class);

    private static final long serialVersionUID = -2995031215260355215L;

    static final int X_ARG = 0;

    static final int Y_ARG = 1;

    static final int WIDTH_ARG = 2;

    static final int HEIGHT_ARG = 3;

    private static final String[] paramNames = { "x", "y", "width", "height", };

    private static final Class[] paramClasses = { Float.class, Float.class, Float.class,
            Float.class, };

    private static final Object[] paramDefaults = { Float.valueOf(0), Float.valueOf(0),
            NO_PARAMETER_DEFAULT, NO_PARAMETER_DEFAULT };

    public GTCropDescriptor() {
        super(new String[][] { { "GlobalName", "GTCrop" }, { "LocalName", "GTCrop" },
                { "Vendor", "org.geotools" },
                { "Description", "Crops the image to the specified bounds" },
                { "DocURL", "http://www.geotools.org" }, { "Version", "1.0.0" },

                { "arg0Desc", paramNames[0] + " (Integer, default = 0) min image X" },

                { "arg1Desc", paramNames[1] + " (Integer, default = 0) min image Y" },

                { "arg2Desc", paramNames[2] + " (Integer) image width" },

                { "arg3Desc", paramNames[3] + " (Integer) image height" }, },
                new String[] { RenderedRegistryMode.MODE_NAME }, // supported modes
                1, // number of sources
                paramNames, paramClasses, paramDefaults, null);
    }
    
    /**
     * Manually registers the operation in the registry in case it's not already there
     */
    public static void register() {
        try {
            final OperationRegistry opr = JAI.getDefaultInstance().getOperationRegistry();
            if(opr.getDescriptor(RenderedRegistryMode.MODE_NAME, "GTCrop") == null) {
                Registry.registerRIF(JAI.getDefaultInstance(), new GTCropDescriptor(),
                        new GTCropCRIF(), Registry.GEOTOOLS_PRODUCT);
            }
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, e.getLocalizedMessage());
            }
        }
    }
    
    /**
     * Performs cropping to a specified bounding box.
     *
     * @param source <code>RenderedImage</code> source 0.
     * @param x The x origin of the cropping operation.
     * @param y The y origin of the cropping operation.
     * @param width The width of the cropping operation.
     * @param height The height of the cropping operation.
     * @param hints The <code>RenderingHints</code> to use, may be null
     */
    public static RenderedOp create(RenderedImage source0,
                                    Float x,
                                    Float y,
                                    Float width,
                                    Float height,
                                    RenderingHints hints)  {
        ParameterBlockJAI pb =
            new ParameterBlockJAI("GTCrop",
                                  RenderedRegistryMode.MODE_NAME);

        pb.setSource("source0", source0);

        pb.setParameter("x", x);
        pb.setParameter("y", y);
        pb.setParameter("width", width);
        pb.setParameter("height", height);

        return JAI.create("GTCrop", pb, hints);
    }

}
