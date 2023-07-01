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

import java.util.Collections;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.GeometryAttributeImpl;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.GeometryDescriptorImpl;
import org.geotools.feature.type.GeometryTypeImpl;
import org.geotools.filter.expression.PropertyAccessor;
import org.geotools.filter.expression.PropertyAccessorFactory;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.factory.Hints;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.util.GeometricShapeFactory;
import org.geotools.api.feature.GeometryAttribute;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.feature.type.GeometryType;
import org.geotools.api.filter.FilterFactory2;
import org.geotools.api.referencing.FactoryException;

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

    @Test
    public void evaluateOnGeometryAttribute() throws FactoryException {
        Envelope env = new Envelope(0.5, 2, 0.5, 2);
        FastBBOX fastBBOX =
                new FastBBOX(filterFactory.property("geometryAttribute"), env, filterFactory);
        assertTrue(fastBBOX.evaluate(circle));
    }

    /**
     * An object carrying data which will be accessed through a custom {@link
     * MockPropertyAccessorFactory}
     */
    public static class MockDataObject {
        Geometry geometry;

        GeometryAttribute geometryAttribute;

        public MockDataObject(Geometry g) {
            this.geometry = g;
            GeometryType type =
                    new GeometryTypeImpl(
                            new NameImpl("GEOMETRY"),
                            Geometry.class,
                            DefaultGeographicCRS.WGS84,
                            false,
                            false,
                            Collections.emptyList(),
                            null,
                            null);
            GeometryDescriptor desc =
                    new GeometryDescriptorImpl(type, new NameImpl("geometry"), 0, 1, false, null);
            this.geometryAttribute = new GeometryAttributeImpl(g, desc, null);
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
                        @SuppressWarnings("unchecked")
                        T result = (T) ((MockDataObject) object).geometry;
                        return result;
                    } else if ("geometryAttribute".equals(xpath)) {
                        @SuppressWarnings("unchecked")
                        T result = (T) ((MockDataObject) object).geometryAttribute;
                        return result;
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
