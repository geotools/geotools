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
package org.geotools.coverage.grid.io.footprint;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.InputStreamInStream;
import com.vividsolutions.jts.io.WKBReader;

public class WKBLoaderSPI implements FootprintLoaderSpi {

    @Override
    public FootprintLoader createLoader() {
        return new WKBLoader();
    }

    /**
     * Loads WKB files
     */
    public class WKBLoader implements FootprintLoader {

        WKBReader reader = new WKBReader();

        @Override
        public Geometry loadFootprint(String pathNoExtension) throws Exception {
            File file = new File(pathNoExtension + ".wkb");
            if (file.exists()) {
                FileInputStream is = null;
                try {
                    is = new FileInputStream(file);
                    return reader.read(new InputStreamInStream(is));
                } finally {
                    IOUtils.closeQuietly(is);
                }
            }

            return null;
        }
    }
}
