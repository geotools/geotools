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
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;

public class WKTLoaderSPI implements FootprintLoaderSpi {

    @Override
    public FootprintLoader createLoader() {
        return new WKTLoader();
    }

    /** Loads WKT files */
    public static class WKTLoader implements FootprintLoader {

        WKTReader reader = new WKTReader();

        @Override
        public Geometry loadFootprint(String pathNoExtension) throws Exception {
            File file = new File(pathNoExtension + ".wkt");
            if (file.exists()) {
                try (FileReader fr = new FileReader(file, StandardCharsets.UTF_8)) {
                    return reader.read(fr);
                }
            }

            return null;
        }

        @Override
        public List<File> getFootprintFiles(String pathNoExtension) {
            File sidecar = new File(pathNoExtension + ".wkt");
            if (sidecar.exists()) {
                return Arrays.asList(sidecar);
            }
            return Collections.emptyList();
        }
    }
}
