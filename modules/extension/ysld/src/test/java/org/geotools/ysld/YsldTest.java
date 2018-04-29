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

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import org.geotools.ysld.Ysld.YsldInput;
import org.junit.Test;

public class YsldTest {

    @Test
    public void readerTest() throws IOException {
        InputStream inputStream = YsldTest.class.getResourceAsStream("point.yml");
        YsldInput reader = Ysld.reader(inputStream);
        reader.close();
        try {
            inputStream.read();
            fail("inputStream should be closed");
        } catch (IOException e) {
            // expect IOException reading from a closed reader
        }
    }
}
