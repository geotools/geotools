/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;

import org.geotools.geopkg.wps.GeoPackageProcessRequest;
import org.geotools.xsd.Binding;
import org.junit.Test;

public class Geopkgtype_featuresBindingTest extends GPKGTestSupport {
    @Test
    public void testType() {
        assertEquals(
                GeoPackageProcessRequest.Layer.class,
                binding(GPKG.geopkgtype_features).getType());
    }

    @Test
    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(GPKG.geopkgtype_features).getExecutionMode());
    }
}
