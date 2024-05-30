/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.appschema.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class XmlXpathUtilitesTest {

    @Test
    public void testGetXPathValuesWithJavaMethod() {
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                XmlXpathUtilites.getXPathValues(
                                        null, "java.lang.Thread.sleep(30000)", null));
        assertEquals("Error reading xpath java.lang.Thread.sleep(30000)", exception.getMessage());
    }

    @Test
    public void testCountXPathNodesWithJavaMethod() {
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                XmlXpathUtilites.countXPathNodes(
                                        null, "java.lang.Thread.sleep(30000)", null));
        assertEquals("Error reading xpath java.lang.Thread.sleep(30000)", exception.getMessage());
    }

    @Test
    public void testGetSingleXPathValueWithJavaMethod() {
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                XmlXpathUtilites.getSingleXPathValue(
                                        null, "java.lang.Thread.sleep(30000)", null));
        assertEquals("Error reading xpath java.lang.Thread.sleep(30000)", exception.getMessage());
    }
}
