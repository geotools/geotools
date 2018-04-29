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

import java.awt.image.renderable.RenderedImageFactory;
import javax.media.jai.OperationRegistry;
import javax.media.jai.OperationRegistrySpi;
import javax.media.jai.registry.RenderedRegistryMode;

public class TransparencyFillSpi implements OperationRegistrySpi {

    /** The name of the product to which these operations belong. */
    private String productName = "it.geosolutions.jaiext";

    /** Default constructor. */
    public TransparencyFillSpi() {}

    public void updateRegistry(OperationRegistry registry) {
        TransparencyFillDescriptor op = new TransparencyFillDescriptor();
        registry.registerDescriptor(op);

        String descName = op.getName();

        RenderedImageFactory rif = new TransparencyFillRIF();
        registry.registerFactory(RenderedRegistryMode.MODE_NAME, descName, productName, rif);
    }
}
