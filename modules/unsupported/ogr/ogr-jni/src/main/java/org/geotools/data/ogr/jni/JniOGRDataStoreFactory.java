/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.ogr.jni;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.ogr.OGR;
import org.geotools.data.ogr.OGRDataStoreFactory;
import org.geotools.util.logging.Logging;

public class JniOGRDataStoreFactory extends OGRDataStoreFactory {

    private static final Logger LOGGER = Logging.getLogger(JniOGRDataStoreFactory.class);

    @Override
    protected OGR createOGR() {
        return new JniOGR();
    }

    @Override
    protected boolean doIsAvailable() throws Throwable {
        try {
            // GDAL version >= 2.3.0
            System.loadLibrary("gdalalljni");
        } catch (UnsatisfiedLinkError e) {
            LOGGER.log(
                    Level.FINE,
                    "Error initializing GDAL/OGR library from \"gdalalljni\". "
                            + "Falling back to \"gdaljni\"",
                    e);
            System.loadLibrary("gdaljni");
        }
        return true;
    }
}
