/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.expression;

import java.util.Map;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimpleFeaturePropertyAccessorFactoryTest {

    SimpleFeaturePropertyAccessorFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new SimpleFeaturePropertyAccessorFactory();
    }

    @Test
    public void test() {

        // make sure features are supported
        Assert.assertNotNull(factory.createPropertyAccessor(SimpleFeature.class, "xpath", null, null));
        Assert.assertNotNull(factory.createPropertyAccessor(SimpleFeatureType.class, "xpath", null, null));
        Assert.assertNull(factory.createPropertyAccessor(Map.class, "xpath", null, null));

        // make sure only simple xpath
        Assert.assertNull(factory.createPropertyAccessor(SimpleFeature.class, "@xpath", null, null));
        Assert.assertNull(factory.createPropertyAccessor(SimpleFeatureType.class, "@xpath", null, null));

        Assert.assertNull(factory.createPropertyAccessor(SimpleFeature.class, "/xpath", null, null));
        Assert.assertNull(factory.createPropertyAccessor(SimpleFeatureType.class, "/xpath", null, null));

        Assert.assertNull(factory.createPropertyAccessor(SimpleFeature.class, "*[0]", null, null));
        Assert.assertNull(factory.createPropertyAccessor(SimpleFeatureType.class, "*[0]", null, null));

        Assert.assertNull(factory.createPropertyAccessor(SimpleFeature.class, "===", null, null));
        Assert.assertNull(factory.createPropertyAccessor(SimpleFeature.class, "34x?<>", null, null));
    }
}
