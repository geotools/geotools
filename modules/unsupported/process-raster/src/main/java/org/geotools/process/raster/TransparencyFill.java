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
package org.geotools.process.raster;

import java.io.Serial;
import org.eclipse.imagen.ImageN;
import org.geotools.coverage.processing.OperationJAI;
import org.geotools.image.jai.Registry;
import org.geotools.processing.jai.TransparencyFillDescriptor;
import org.geotools.processing.jai.TransparencyFillRIF;

public class TransparencyFill extends OperationJAI {
    /** Serial number for interoperability with different versions. */
    @Serial
    private static final long serialVersionUID = 3368109980974496342L;

    static {
        Registry.registerRIF(
                ImageN.getDefaultInstance(),
                new TransparencyFillDescriptor(),
                new TransparencyFillRIF(),
                "org.eclipse.imagen.media");
    }

    public TransparencyFill() {
        super("TransparencyFill");
    }

    @Override
    public String getName() {
        return "TransparencyFill";
    }
}
