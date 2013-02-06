/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml.handlers;

import java.net.URI;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.geotools.xml.XMLElementHandler;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.impl.ElementGT;
import org.xml.sax.SAXException;

/**
 * @author Mauro Bartolomeoli - mauro.bartolomeoli@geo-solutions.it
 *
 */
public class ElementHandlerFactoryTest extends TestCase {
    private static final Logger logger = Logger
            .getLogger(ElementHandlerFactoryTest.class.getName());

    public void testCreateElementHandlerIgnoresUnknownTypes() {
        ElementHandlerFactory factory = new ElementHandlerFactory(logger);
        Element el = new ElementGT("test", "test",
                URI.create("http://www.geotools.org"), /* Type */ null, 0, 0, false, null,
                false);
        try {
            XMLElementHandler elementHandler = factory.createElementHandler(el);
            assertNotNull(elementHandler);
            assertTrue(elementHandler instanceof IgnoreHandler);
        } catch (SAXException e) {
            fail("Failure in createElementHandler: " + e.getLocalizedMessage());
        }
    }
}
