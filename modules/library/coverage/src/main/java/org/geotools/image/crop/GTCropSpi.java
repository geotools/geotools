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

import java.awt.image.renderable.RenderedImageFactory;

import javax.media.jai.OperationDescriptor;
import javax.media.jai.OperationRegistry;
import javax.media.jai.OperationRegistrySpi;
import javax.media.jai.registry.RenderedRegistryMode;

/**
 * OperationRegistrySpi implementation to register the "GTCrop" operation and its associated image
 * factories.
 * 
 * @author Andrea Aime
 * @since 2.7.2
 */
public class GTCropSpi implements OperationRegistrySpi {

    /** The name of the product to which these operations belong. */
    private String productName = "or.geotools.GTCRop";

    /** Default constructor. */
    public GTCropSpi() {
    }

    /**
     * Registers the VectorBinarize operation and its associated image factories across all
     * supported operation modes.
     * 
     * @param registry
     *            The registry with which to register the operations and their factories.
     */
    public void updateRegistry(OperationRegistry registry) {
        OperationDescriptor op = new GTCropDescriptor();
        registry.registerDescriptor(op);
        String descName = op.getName();

        RenderedImageFactory rif = new GTCropCRIF();

        registry.registerFactory(RenderedRegistryMode.MODE_NAME, descName, productName, rif);
    }
}
