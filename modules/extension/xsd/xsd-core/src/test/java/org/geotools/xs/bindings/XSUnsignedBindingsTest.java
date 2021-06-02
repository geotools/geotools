/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xs.bindings;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import org.geotools.util.URLs;
import org.geotools.xs.TestSchema;
import org.geotools.xs.XSConfiguration;
import org.geotools.xsd.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class XSUnsignedBindingsTest {

    Parser p;

    @Before
    public void setUp() throws Exception {
        XSConfiguration xs = new XSConfiguration();
        p = new Parser(xs);
    }

    @Test
    public void testParseUnsignedInt() throws Exception {
        Long l = (Long) p.parse(new ByteArrayInputStream(doc("unsignedInt", "12").getBytes(UTF_8)));
        Assert.assertEquals(12l, l.longValue());
    }

    @Test
    public void testParseUnsignedByte() throws Exception {
        Short s =
                (Short)
                        p.parse(
                                new ByteArrayInputStream(
                                        doc("unsignedByte", "12").getBytes(UTF_8)));
        Assert.assertEquals(12, s.shortValue());
    }

    @Test
    public void testParseUnsignedShort() throws Exception {
        Integer i =
                (Integer)
                        p.parse(
                                new ByteArrayInputStream(
                                        doc("unsignedShort", "12").getBytes(UTF_8)));
        Assert.assertEquals(12, i.longValue());
    }

    @Test
    public void testParseUnsignedLong() throws Exception {
        BigDecimal l =
                (BigDecimal)
                        p.parse(
                                new ByteArrayInputStream(
                                        doc("unsignedLong", "12").getBytes(UTF_8)));
        Assert.assertEquals(12l, l.longValue());
    }

    String doc(String elName, String elValue) throws IOException {
        File sampleXsd = File.createTempFile("sample", "xsd", new File("target"));
        copy(TestSchema.class.getResourceAsStream("sample.xsd"), sampleXsd);

        return String.format(
                "<my:%s xmlns:my='http://localhost/xob/test' "
                        + "schemaLocation='http://localhost/xob/test %s'>%s</my:%s>",
                elName, URLs.fileToUrl(sampleXsd), elValue, elName);
    }

    void copy(InputStream in, File to) throws IOException {
        try (FileOutputStream fout = new FileOutputStream(to)) {
            byte[] buffer = new byte[1024];
            int n = 0;

            while ((n = in.read(buffer)) > 0) {
                fout.write(buffer, 0, n);
            }
            fout.flush();
        }
    }
}
