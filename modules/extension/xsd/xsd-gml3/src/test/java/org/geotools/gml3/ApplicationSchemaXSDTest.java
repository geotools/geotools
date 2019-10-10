/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml3;

import static org.junit.Assert.*;

import org.geotools.xsd.SchemaLocationResolver;
import org.junit.Test;

public class ApplicationSchemaXSDTest {

    /** Test to resolve schema urls within jar files. */
    @Test
    public void testCreateSchemaLocationResolver() {
        ApplicationSchemaXSD instance =
                new ApplicationSchemaXSD(
                        "http://myschema.com", "jar:file:///mytest/schemas.jar!/main.xsd");
        SchemaLocationResolver resolver = instance.createSchemaLocationResolver();
        assertEquals(
                "jar:file:///mytest/schemas.jar!/test.xsd",
                resolver.resolveSchemaLocation(null, "http://myschema.com", "test.xsd"));
    }
}
