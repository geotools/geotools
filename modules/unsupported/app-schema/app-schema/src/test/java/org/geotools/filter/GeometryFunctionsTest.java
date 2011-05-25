/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.Types;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.filter.expression.ToDirectPositionFunction;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml3.GMLSchema;
import org.geotools.referencing.CRS;
import org.geotools.xs.XSSchema;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.PropertyName;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;

/**
 * This is to test functions that convert numeric values to geometry types. This is required when
 * the data store doesn't have geometry columns, but geometry types are needed.
 * 
 * @author Rini Angreani, Curtin University of Technology
 *
 *
 * @source $URL$
 */
public class GeometryFunctionsTest {

    public static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.data.complex");

    private static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    private static SimpleFeature feature;

    private static PropertyName pointOne;

    private static PropertyName pointTwo;

    @BeforeClass
    public static void setUpOnce() {
        List<AttributeDescriptor> schema = new ArrayList<AttributeDescriptor>();
        schema.add(new AttributeDescriptorImpl(XSSchema.DOUBLE_TYPE, Types.typeName("pointOne"), 0,
                1, false, null));
        schema.add(new AttributeDescriptorImpl(XSSchema.DOUBLE_TYPE, Types.typeName("pointTwo"), 0,
                1, false, null));

        SimpleFeatureType type = new SimpleFeatureTypeImpl(Types.typeName("GeometryContainer"),
                schema, null, false, null, GMLSchema.ABSTRACTFEATURETYPE_TYPE, null);
        feature = SimpleFeatureBuilder.build(type, new Object[] { 5.0, 2.5 }, null);
        pointOne = ff.property("pointOne");
        pointTwo = ff.property("pointTwo");
    }

    @Test
    /**
     * Test toDirectPosition function
     */
    public void testToDirectPosition() throws Exception {
        // 2 points with SRS
        Function function = ff.function("toDirectPosition", ToDirectPositionFunction.SRS_NAME, ff
                .literal("EPSG:4326"), pointOne, pointTwo);
        Object value = function.evaluate(feature);
        assertTrue(value instanceof DirectPosition);
        DirectPosition pos = (DirectPosition) value;
        assertEquals(CRS.toSRS(pos.getCoordinateReferenceSystem()), "EPSG:4326");
        assertEquals(pos.getDimension(), 2);
        assertEquals(pos.getOrdinate(0), 5.0, 0);
        assertEquals(pos.getOrdinate(1), 2.5, 0);

        // 1 point, no SRS
        function = ff.function("toDirectPosition", pointOne);
        value = function.evaluate(feature);
        assertTrue(value instanceof DirectPosition);
        pos = (DirectPosition) value;
        assertNull(pos.getCoordinateReferenceSystem());
        assertEquals(pos.getDimension(), 1);
        assertEquals(pos.getOrdinate(0), 5.0, 0);
        // invalid CRS
        try {
            function = ff.function("toDirectPosition", ToDirectPositionFunction.SRS_NAME, ff
                    .literal("1"), pointOne, pointTwo);
            function.evaluate(feature);
            fail("Shouldn't get this far with invalid SRS name: '1'");
        } catch (Throwable e) {
            LOGGER.info("Testing exception: " + e.toString());
        }
        // too many of points
        try {
            function = ff.function("toDirectPosition", pointOne, pointTwo, pointOne);
            function.evaluate(feature);
            fail("Shouldn't get this far with too many parameters: " + pointOne.toString() + ", "
                    + pointTwo.toString() + ", " + pointOne.toString());
        } catch (Throwable e) {
            LOGGER.info("Testing exception: " + e.toString());
        }
        // no points
        try {
            function = ff.function("toDirectPosition", ToDirectPositionFunction.SRS_NAME, ff
                    .literal("EPSG:WGS84"));
            function.evaluate(feature);
            fail("Shouldn't get this far with too many parameters: " + pointOne.toString() + ", "
                    + pointTwo.toString() + ", " + pointOne.toString());
        } catch (Throwable e) {
            LOGGER.info("Testing exception: " + e.toString());
        }
    }

    @Test
    /**
     * Test toPoint function
     */
    public void testToPoint() throws NoSuchAuthorityCodeException, FactoryException {
        // 2 points with SRS name and gml:id
        Function function = ff.function("toPoint", ToDirectPositionFunction.SRS_NAME, ff
                .literal("EPSG:4283"), pointOne, pointTwo, ff.literal("1"));
        Object value = function.evaluate(feature);
        assertTrue(value instanceof Point);
        Point pt = (Point) value;
        assertEquals(pt.getDimension(), 0);
        assertEquals(pt.getCoordinate().x, 5.0, 0);
        assertEquals(pt.getCoordinate().y, 2.5, 0);
        Map<Object, Object> userData = (Map<Object, Object>) pt.getUserData();
        assertEquals(userData.get("gml:id"), "1");
        assertEquals(userData.get(CoordinateReferenceSystem.class), CRS.decode("EPSG:4283"));

        // 2 points with no SRS name
        function = ff.function("toPoint", pointOne, pointTwo);
        value = function.evaluate(feature);
        assertTrue(value instanceof Point);
        pt = (Point) value;
        assertEquals(pt.getDimension(), 0);
        assertEquals(pt.getCoordinate().x, 5.0, 0);
        assertEquals(pt.getCoordinate().y, 2.5, 0);
        assertNull(pt.getUserData());
        // 1 point
        function = ff.function("toPoint", pointOne);
        try {
            value = function.evaluate(feature);
            fail("Shouldn't get this far with not enough parameters :" + pointOne.toString());
        } catch (Throwable e) {
            LOGGER.info("Testing exception: " + e.toString());
        }
        // 3 points
        function = ff.function("toPoint", ToDirectPositionFunction.SRS_NAME, ff.literal("1"),
                pointOne, pointTwo, pointOne);
        try {
            function.evaluate(feature);
            fail("Shouldn't get this far with too many parameters: " + pointOne.toString() + ", "
                    + pointTwo.toString() + ", " + pointOne.toString());
        } catch (Throwable e) {
            LOGGER.info("Testing exception: " + e.toString());
        }
    }

    @Test
    /**
     * Test toEnvelope function
     */
    public void testToEnvelope() {
        // Option 1 (1D Envelope) : <OCQL>ToEnvelope(minx,maxx)</OCQL>
        Function function = ff.function("toEnvelope", pointOne, pointTwo);
        Object value = function.evaluate(feature);
        assertTrue(value instanceof Envelope);
        Envelope env = (Envelope) value;
        assertEquals(env.getMinX(), env.getMaxX(), 0);
        assertEquals(env.getMinX(), 5.0, 0);
        assertEquals(env.getMinY(), env.getMaxY(), 0);
        assertEquals(env.getMinY(), 2.5, 0);
        // Option 2 (1D Envelope with crsname): <OCQL>ToEnvelope(minx,maxx,crsname)</OCQL>
        function = ff.function("toEnvelope", pointOne, pointTwo, ff.literal("EPSG:4283"));
        value = function.evaluate(feature);
        assertTrue(value instanceof ReferencedEnvelope);
        ReferencedEnvelope refEnv = (ReferencedEnvelope) value;
        assertEquals(refEnv.getMinX(), refEnv.getMaxX(), 0);
        assertEquals(refEnv.getMinX(), 5.0, 0);
        assertEquals(refEnv.getMinY(), refEnv.getMaxY(), 0);
        assertEquals(refEnv.getMinY(), 2.5, 0);
        assertEquals(CRS.toSRS(refEnv.getCoordinateReferenceSystem()), "EPSG:4283");
        // Option 3 (2D Envelope) : <OCQL>ToEnvelope(minx,maxx,miny,maxy)</OCQL>
        function = ff.function("toEnvelope", pointTwo, pointOne, pointTwo, pointOne);
        value = function.evaluate(feature);
        assertTrue(value instanceof Envelope);
        env = (Envelope) value;
        assertEquals(env.getMinX(), 2.5, 0);
        assertEquals(env.getMaxX(), 5.0, 0);
        assertEquals(env.getMinY(), 2.5, 0);
        assertEquals(env.getMaxY(), 5.0, 0);
        assertEquals(CRS.toSRS(refEnv.getCoordinateReferenceSystem()), "EPSG:4283");
        // Option 4 (2D Envelope with crsname): <OCQL>ToEnvelope(minx,maxx,miny,maxy,crsname)</OCQL>
        function = ff.function("toEnvelope", pointTwo, pointOne, pointTwo, pointOne, ff
                .literal("EPSG:4283"));
        value = function.evaluate(feature);
        assertTrue(value instanceof ReferencedEnvelope);
        refEnv = (ReferencedEnvelope) value;
        assertEquals(refEnv.getMinX(), 2.5, 0);
        assertEquals(refEnv.getMaxX(), 5.0, 0);
        assertEquals(refEnv.getMinY(), 2.5, 0);
        assertEquals(refEnv.getMaxY(), 5.0, 0);
        assertEquals(CRS.toSRS(refEnv.getCoordinateReferenceSystem()), "EPSG:4283");
    }
}
