/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld;

import java.io.InputStream;

/** Test class for loading SLD data. */
public class YsldTests {
    /**
     * Loads a test SLD.
     *
     * @param dir One of point, line, polygon, raster.
     * @param file The filename.
     * @return The input stream for the SLD file.
     */
    public static InputStream sld(String dir, String file) {
        return YsldTests.class.getResourceAsStream("sld/" + dir + "/" + file);
    }

    public static InputStream ysld(String dir, String file) {
        return YsldTests.class.getResourceAsStream("" + dir + "/" + file);
    }
}
