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

/** Tests for {@link ElementTypeHandler}. */
public class ElementTypeHandlerTest {

    private static final String NAMESPACE_URI = "http://example.org/test";

    private ElementTypeHandler handler = new ElementTypeHandler();

    private AttributesImpl attributes = new AttributesImpl();

    /** Test that a {@code ref} attribute is set as the {@code name}. */
    @Test
    public void ref() throws SAXException {
        attributes.addAttribute("", "ref", "ref", "string", "test:another-element");
        handler.startElement(NAMESPACE_URI, "element", attributes);
        Assert.assertEquals("test:another-element", handler.getName());
    }

    /**
     * Test that the presence of both {@code ref} and {@code name} attributes results in a {@link
     * SAXException} with the expected message.
     */
    @Test
    public void refAndName() {
        attributes.addAttribute("", "ref", "ref", "string", "test:another-element");
        attributes.addAttribute("", "name", "name", "string", "some-name");
        try {
            handler.startElement(NAMESPACE_URI, "element", attributes);
            Assert.fail();
        } catch (SAXException e) {
            Assert.assertEquals(
                    "Schema element declaration cannot have both 'ref' and 'name' "
                            + "attributes (ref=\"test:another-element\", name=\"some-name\")",
                    e.getMessage());
        }
    }

    /**
     * Test that the presence of both {@code ref} and {@code type} attributes results in a {@link
     * SAXException} with the expected message.
     */
    @Test
    public void refAndType() {
        attributes.addAttribute("", "ref", "ref", "string", "test:another-element");
        attributes.addAttribute("", "type", "type", "string", "some-type");
        try {
            handler.startElement(NAMESPACE_URI, "element", attributes);
            Assert.fail();
        } catch (SAXException e) {
            Assert.assertEquals(
                    "Schema element declaration cannot have both 'ref' and 'type' "
                            + "attributes (ref=\"test:another-element\", type=\"some-type\")",
                    e.getMessage());
        }
    }
}
