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
package org.geotools.filter;

import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Assert;
import org.junit.Test;

public class AttributeExpressionTest {

    @Test
    public void testFeature() {
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();

        typeBuilder.setName("test");
        typeBuilder.setNamespaceURI("http://www.geotools.org/test");
        typeBuilder.add("foo", Integer.class);
        typeBuilder.add("bar", Double.class);

        SimpleFeatureType type = typeBuilder.buildFeatureType();

        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        builder.add(Integer.valueOf(1));
        builder.add(Double.valueOf(2.0));

        SimpleFeature feature = builder.buildFeature("fid");

        AttributeExpressionImpl ex = new AttributeExpressionImpl("foo");
        Assert.assertEquals(Integer.valueOf(1), ex.evaluate(feature));

        ex = new AttributeExpressionImpl("@id");
        Assert.assertEquals("fid", ex.evaluate(feature));
    }
}
