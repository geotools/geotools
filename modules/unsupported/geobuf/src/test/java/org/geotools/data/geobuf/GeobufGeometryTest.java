/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geobuf;

import static org.junit.Assert.assertEquals;

import java.io.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;

public class GeobufGeometryTest {

    @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();

    protected void encodeDecode(String geometryWkt) throws Exception {
        File file = temporaryFolder.newFile("geom.pbf");
        WKTReader wkt = new WKTReader();
        GeobufGeometry geobufGeometry = new GeobufGeometry();
        OutputStream out = new FileOutputStream(file);
        geobufGeometry.encode(wkt.read(geometryWkt), out);
        out.close();
        InputStream inputStream = new FileInputStream(file);
        Geometry g = geobufGeometry.decode(inputStream);
        inputStream.close();
        file.delete();
        assertEquals(geometryWkt, g.toText());
    }

    @Test
    public void encodeDecodePoint() throws Exception {
        encodeDecode("POINT (12.3 56.78)");
        encodeDecode("POINT (-122.381635 47.116273)");
    }
}
