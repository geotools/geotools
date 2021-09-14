/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana.metadata;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Assert;
import org.junit.Test;

/** @author Stefan Uhrig, SAP SE */
public class UomReaderTest {

    @Test
    public void testUomReading() throws IOException {
        String csv = "meter,linear,1.0\ndegree,angular,0.017453292519943278";
        InputStream is = new ByteArrayInputStream(csv.getBytes(UTF_8));
        UomReader reader = new UomReader(is);

        Uom uom = reader.readNextUom();
        Assert.assertNotNull(uom);
        Assert.assertEquals("meter", uom.getName());
        Assert.assertEquals(Uom.Type.LINEAR, uom.getType());
        Assert.assertEquals(1.0, uom.getFactor(), 0d);

        uom = reader.readNextUom();
        Assert.assertNotNull(uom);
        Assert.assertEquals("degree", uom.getName());
        Assert.assertEquals(Uom.Type.ANGULAR, uom.getType());
        Assert.assertEquals(0.017453292519943278, uom.getFactor(), 0d);

        uom = reader.readNextUom();
        Assert.assertNull(uom);
    }

    @Test
    public void testWrongEntryCount() throws IOException {
        String csv = "meter,linear";
        InputStream is = new ByteArrayInputStream(csv.getBytes(UTF_8));
        UomReader reader = new UomReader(is);
        try {
            reader.readNextUom();
            Assert.fail("Expected RuntimeException");
        } catch (RuntimeException e) {
        }
    }

    @Test
    public void testInvalidType() throws IOException {
        String csv = "meter,lin,1.0";
        InputStream is = new ByteArrayInputStream(csv.getBytes(UTF_8));
        UomReader reader = new UomReader(is);
        try {
            reader.readNextUom();
            Assert.fail("Expected RuntimeException");
        } catch (RuntimeException e) {
        }
    }
}
