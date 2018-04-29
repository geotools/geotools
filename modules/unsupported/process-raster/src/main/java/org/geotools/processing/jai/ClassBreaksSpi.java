/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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

public class ClassBreaksSpi implements OperationRegistrySpi {

    /** The name of the product to which these operations belong. */
    private String productName = "org.jaitools.media.jai";

    /** Default constructor. */
    public ClassBreaksSpi() {}

    public void updateRegistry(OperationRegistry registry) {
        ClassBreaksDescriptor op = new ClassBreaksDescriptor();
        registry.registerDescriptor(op);

        String descName = op.getName();

        RenderedImageFactory rif = new ClassBreaksRIF();
        registry.registerFactory(RenderedRegistryMode.MODE_NAME, descName, productName, rif);
    }
}
