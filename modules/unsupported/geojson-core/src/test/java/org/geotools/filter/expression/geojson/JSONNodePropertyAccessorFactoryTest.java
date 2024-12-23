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
package org.geotools.filter.expression.geojson;

import org.geotools.api.feature.simple.SimpleFeature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JSONNodePropertyAccessorFactoryTest {

    JSONNodePropertyAccessorFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new JSONNodePropertyAccessorFactory();
    }

    @Test
    public void test() {
        Assert.assertNotNull(factory.createPropertyAccessor(SimpleFeature.class, "xpath", null, null));
        Assert.assertNull(factory.createPropertyAccessor(SimpleFeature.class, null, null, null));
    }
}
