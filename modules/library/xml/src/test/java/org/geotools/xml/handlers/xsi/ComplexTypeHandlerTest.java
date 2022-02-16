/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.xml.handlers.xsi;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/** Tests for {@link ComplexTypeHandler}. */
public class ComplexTypeHandlerTest {

    private static final String NAMESPACE_URI = "http://example.org/test";

    private ComplexTypeHandler handler = new ComplexTypeHandler();
    private AttributesImpl attributes = new AttributesImpl();

    /** Test abstracT true */
    @Test
    public void abstracT() throws SAXException {
        attributes.addAttribute("", "name", "name", "string", "Generic");
        attributes.addAttribute("", "abstract", "abstract", "boolean", "cat");
        try {
            handler.startElement(NAMESPACE_URI, "name", attributes);
            Assert.fail("Schrodinger: I am not a cat");
        } catch (SAXException e) {
            Assert.assertEquals(
                    "Schema element declaration supports 'abstract' \"true\" or \"false\" only (abstract=\"cat\")",
                    e.getMessage());
        }
    }

    /** Test abstracT true */
    @Test
    public void mixed() throws SAXException {
        attributes.addAttribute("", "name", "name", "string", "Assorted");
        attributes.addAttribute("", "mixed", "mixed", "boolean", "true");

        handler.startElement(NAMESPACE_URI, "name", attributes);
        Assert.assertEquals("Assorted", handler.getName());
    }
}
