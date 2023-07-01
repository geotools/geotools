/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.visitor;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Polygon;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.spatial.BBOX;

public class PropertyNameResolvingFilterVisitorTest {

    FilterFactory factory;
    SimpleFeatureType featureType;

    @Before
    public void setUp() throws Exception {

        factory = CommonFactoryFinder.getFilterFactory(null);
        SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
        b.setName("test");
        b.add("name", String.class);
        b.add("geom", Polygon.class, DefaultGeographicCRS.WGS84);
        b.setDefaultGeometry("geom");
        featureType = b.buildFeatureType();
    }

    @Test
    public void testResolvePropertyName() {
        PropertyIsEqualTo f =
                factory.equal(factory.property("gml:name"), factory.literal("foo"), true);
        Assert.assertEquals("gml:name", f.getExpression1().toString());

        f = (PropertyIsEqualTo) f.accept(new PropertyNameResolvingVisitor(featureType), null);

        Assert.assertEquals("name", f.getExpression1().toString());
    }

    @Test
    public void testResolveEmptyName() {
        // We use a geometry filter here to test that expression1 does not get filled with the
        // default geometry
        BBOX f = factory.bbox("", 0.0, 0.0, 1.0, 1.0, DefaultGeographicCRS.WGS84.toString());
        Assert.assertEquals("", f.getExpression1().toString());

        f = (BBOX) f.accept(new PropertyNameResolvingVisitor(featureType), null);

        Assert.assertEquals("", f.getExpression1().toString());
    }
}
