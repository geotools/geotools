/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.renderer.lite;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.filter.expression.PropertyAccessor;
import org.geotools.filter.expression.PropertyAccessorFactory;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.util.GeometricShapeFactory;
import org.opengis.filter.FilterFactory2;

/**
 * This tests <a href="https://osgeo-org.atlassian.net/browse/GEOT-5401">[GEOT-5401] FastBBOX should
 * not cast to SimpleFeature</a>, ensuring that FastBBOX does not require a SimpleFeature, but only
 * relies on the (custom) property accessor.
 *
 * @author Jes Wulfsberg Nielsen - NorthTech
 */
public class FastBBOXTest {
    MockDataObject circle;
    FilterFactory2 filterFactory = CommonFactoryFinder.getFilterFactory2();

    @Before
    public void setUp() {
        // Create a mock object with a geometry which is a circle around origin:
        GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
        shapeFactory.setEnvelope(new Envelope(-1, 1, -1, 1));
        circle = new MockDataObject(shapeFactory.createCircle());
    }

    @Test
    public void evaluate_envelopeOverlapsBBOX() throws Exception {
        FastBBOX fastBBOX =
                new FastBBOX(
                        filterFactory.property("geometry"),
                        new Envelope(0.8, 2, 0.8, 2),
                        filterFactory);
        assertTrue(fastBBOX.evaluate(circle));
    }

    @Test
    public void evaluate_envelopeIntersectsGeometry() throws Exception {
        FastBBOX fastBBOX =
                new FastBBOX(
                        filterFactory.property("geometry"),
                        new Envelope(0.5, 2, 0.5, 2),
                        filterFactory);
        assertTrue(fastBBOX.evaluate(circle));
    }

    @Test
    public void evaluate_envelopeDisjoint() throws Exception {
        FastBBOX fastBBOX =
                new FastBBOX(
                        filterFactory.property("geometry"),
                        new Envelope(1.1, 2, 1.1, 2),
                        filterFactory);
        assertFalse(fastBBOX.evaluate(circle));
    }

    /**
     * An object carrying data which will be accessed through a custom {@link
     * MockPropertyAccessorFactory}
     */
    public static class MockDataObject {
        Geometry geometry;

        public MockDataObject(Geometry g) {
            this.geometry = g;
        }
    }

    /**
     * A minimalistic MockPropertyAccessor (and its factory), which only recognizes a single field;
     * namely the "geometry". It is registered through the
     * META-INF/services/org.geotools.filter.expression.PropertyAccessorFactory SPI registration in
     * the test/resources.
     */
    public static class MockPropertyAccessorFactory implements PropertyAccessorFactory {
        @Override
        public PropertyAccessor createPropertyAccessor(
                Class<?> type, String xpath, Class<?> target, Hints hints) {
            if (!MockDataObject.class.equals(type)) {
                return null;
            }
            return new PropertyAccessor() {
                @Override
                public boolean canHandle(Object object, String xpath, Class<?> target) {
                    return object instanceof MockDataObject;
                }

                @Override
                public <T> T get(Object object, String xpath, Class<T> target)
                        throws IllegalArgumentException {
                    if ("geometry".equals(xpath)) {
                        return (T) ((MockDataObject) object).geometry;
                    } else {
                        throw new IllegalArgumentException("Unknown field");
                    }
                }

                @Override
                public <T> void set(Object object, String xpath, T value, Class<T> target)
                        throws IllegalArgumentException {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }
}
