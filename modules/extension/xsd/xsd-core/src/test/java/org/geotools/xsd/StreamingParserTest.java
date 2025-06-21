/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xsd;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import javax.xml.namespace.QName;
import org.geotools.ml.MLConfiguration;
import org.geotools.ml.bindings.ML;
import org.geotools.util.PreventLocalEntityResolver;
import org.junit.Test;

public class StreamingParserTest {

    @Test
    public void testParseXXE() throws Exception {
        String xml = "<!DOCTYPE foo [<!ENTITY xxe SYSTEM \"file:///\" >]>"
                + "<mails><mail><body>&xxe;</body></mail></mails>";
        ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        StreamingParser parser = new StreamingParser(new MLConfiguration(), in, new QName(ML.NAMESPACE, "mail"));
        parser.setEntityResolver(PreventLocalEntityResolver.INSTANCE);
        // StreamingParser returns null if the parsing fails
        assertNull(parser.parse());
    }

    @Test
    public void testParseWithJavaMethod() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("<mails></mails>".getBytes(StandardCharsets.UTF_8));
        StreamingParser parser = new StreamingParser(new MLConfiguration(), in, "java.lang.Thread.sleep(30000)");
        // StreamingParser returns null if the parsing fails
        long start = System.currentTimeMillis();
        assertNull(parser.parse());
        long runtime = System.currentTimeMillis() - start;
        assertTrue("java.lang.Thread.sleep(30000) was executed", runtime < 30000);
    }
}
