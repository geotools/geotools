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
package org.geotools.renderer.style.markwkt;

import java.awt.Shape;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.LineString;

/**
 * Unit tests for shape mark factory
 *
 * @author Luca Morandini lmorandini@ieee.org
 */
public class ShapeMarkFactoryTest {

    private SimpleFeature feature;
    private Expression exp;
    private FilterFactory ff;

    {
        try {
            ff = CommonFactoryFinder.getFilterFactory(null);
            SimpleFeatureTypeBuilder featureTypeBuilder = new SimpleFeatureTypeBuilder();
            featureTypeBuilder.setName("TestType");
            featureTypeBuilder.add("geom", LineString.class, DefaultGeographicCRS.WGS84);
            SimpleFeatureType featureType = featureTypeBuilder.buildFeatureType();
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
            this.feature = featureBuilder.buildFeature(null);
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
    }

    @Test
    public void testWellKnownName() {
        MeteoMarkFactory smf = new MeteoMarkFactory();
        try {
            this.exp = ff.literal("shape://triangle");
            smf.getShape(null, this.exp, this.feature);
        } catch (Exception e) {
            Assert.fail();
            return;
        }

        Assert.assertTrue(true);
    }

    @Test
    public void testUnknownProtocol() {
        MeteoMarkFactory smf = new MeteoMarkFactory();
        try {
            this.exp = ff.literal("xxx://triangle");
            if (smf.getShape(null, this.exp, this.feature) == null) {
                Assert.assertTrue(true);
                return;
            }
        } catch (Exception e) {
            Assert.fail();
            return;
        }

        Assert.fail();
    }

    @Test
    public void testSouthArrow() {
        MeteoMarkFactory smf = new MeteoMarkFactory();
        try {
            this.exp = ff.literal("extshape://sarrow");
            Shape shape = smf.getShape(null, this.exp, this.feature);
            // Check if the South Arrow is present
            Assert.assertNotNull(shape);
        } catch (Exception e) {
            Assert.fail();
            return;
        }

        Assert.assertTrue(true);
    }
}
