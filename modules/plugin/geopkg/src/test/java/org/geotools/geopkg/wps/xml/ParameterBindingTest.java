/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg.wps.xml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import org.geotools.geopkg.wps.GeoPackageProcessRequest;
import org.geotools.xsd.Binding;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class ParameterBindingTest extends GPKGTestSupport {
    @Test
    public void testType() {
        assertEquals(
                GeoPackageProcessRequest.Parameter.class, binding(GPKG.parametertype).getType());
    }

    @Test
    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(GPKG.parametertype).getExecutionMode());
    }

    @Test
    public void testParse() throws Exception {
        buildDocument(
                "<param name=\"env\" xmlns=\"http://www.opengis.net/gpkg\">date:2010-10-01</param>");
        Object result = parse(GPKG.parametertype);
        assertThat(result, CoreMatchers.instanceOf(GeoPackageProcessRequest.Parameter.class));
        GeoPackageProcessRequest.Parameter parameter = (GeoPackageProcessRequest.Parameter) result;
        assertEquals("env", parameter.getName());
        assertEquals("date:2010-10-01", parameter.getValue());
    }
}
