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

import java.io.ByteArrayInputStream;
import javax.xml.namespace.QName;
import org.geotools.ml.MLConfiguration;
import org.geotools.ml.bindings.ML;
import org.geotools.util.PreventLocalEntityResolver;
import org.junit.Test;

public class StreamingParserTest {

    @Test
    public void testParseXXE() throws Exception {
        String xml =
                "<!DOCTYPE foo [<!ENTITY xxe SYSTEM \"file:///\" >]>"
                        + "<mails><mail><body>&xxe;</body></mail></mails>";
        ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes());
        StreamingParser parser =
                new StreamingParser(new MLConfiguration(), in, new QName(ML.NAMESPACE, "mail"));
        parser.setEntityResolver(PreventLocalEntityResolver.INSTANCE);
        // StreamingParser returns null if the parsing fails
        assertNull(parser.parse());
    }
}
