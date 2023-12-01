/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import static org.geotools.referencing.crs.DefaultEngineeringCRS.CARTESIAN_2D;
import static org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class NorthFixTest {

    static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();
    public static final Literal ANGLE_LT = FF.literal(10);
    public static final Literal WGS84_LT = FF.literal(WGS84);
    static final GeometryFactory GF = new GeometryFactory();
    static final Point POINT = GF.createPoint();
    public static final Literal POINT_LT = FF.literal(POINT);
    public static final String NORTH_FIX = NorthFix.FUNCTION_NAME;

    /**
     * The tolerance for comparing angles reported by NorthFix. There is a balance here between
     * accurancy and the distance that NorthFix can use to determine the angle.
     */
    private static final double EPS = 1;

    /** Test the SPI registration is working */
    @Test
    public void testLookup() throws Exception {

        Function function = FF.function(NORTH_FIX, WGS84_LT, POINT_LT);
        assertThat(function, instanceOf(NorthFix.class));
    }

    @Test
    public void testGeographic() throws Exception {
        Function function = FF.function(NORTH_FIX, WGS84_LT, POINT_LT);
        assertThat((Double) function.evaluate(null), equalTo(0d));
        assertFalse(NorthFix.fixRequired(WGS84));
    }

    @Test
    public void testGeographicAngle() throws Exception {
        Function function = FF.function(NORTH_FIX, WGS84_LT, POINT_LT, ANGLE_LT);
        assertThat((Double) function.evaluate(null), equalTo(10d));
        assertFalse(NorthFix.fixRequired(WGS84));
    }

    @Test
    public void testCompound() throws Exception {
        // build a compound programmatically (future proofing)
        CoordinateReferenceSystem compoundCRS = getCompoundCRS();

        Function function = FF.function(NORTH_FIX, FF.literal(compoundCRS), POINT_LT);
        assertThat((Double) function.evaluate(null), equalTo(0d));
        assertFalse(NorthFix.fixRequired(compoundCRS));
    }

    private static CoordinateReferenceSystem getCompoundCRS() throws FactoryException {
        CRSFactory crsFactory = ReferencingFactoryFinder.getCRSFactory(null);
        CoordinateReferenceSystem sourceHorizontalCRS = CRS.decode("EPSG:4326");
        CoordinateReferenceSystem sourceVerticalCRS = CRS.decode("EPSG:5783");
        Map<String, Object> properties = new HashMap<>();
        properties.put(
                IdentifiedObject.NAME_KEY,
                new NamedIdentifier(Citations.fromName("TEST"), "Compound 4326+5783"));
        CoordinateReferenceSystem[] elements = {sourceHorizontalCRS, sourceVerticalCRS};
        CoordinateReferenceSystem compoundCRS = crsFactory.createCompoundCRS(properties, elements);
        return compoundCRS;
    }

    @Test
    public void testEngineering() throws Exception {
        Function function = FF.function(NORTH_FIX, FF.literal(CARTESIAN_2D), POINT_LT);
        assertThat((Double) function.evaluate(null), equalTo(0d));
        assertFalse(NorthFix.fixRequired(CARTESIAN_2D));
    }

    @Test
    public void testPolar() throws Exception {
        CoordinateReferenceSystem polarCRS = CRS.decode("EPSG:3976");
        assertTrue(NorthFix.fixRequired(polarCRS));

        // a test point on the greenwich meridian, no correction needed
        assertEquals(0, getNorthFix(polarCRS, getPoint(0, 10000)), EPS);
        // a test point at 90 longitude
        assertEquals(90, getNorthFix(polarCRS, getPoint(10000, 0)), EPS);
        // a test point at 180 longitude (dateline)
        assertEquals(180, getNorthFix(polarCRS, getPoint(0, -10000)), EPS);
        // a test point at -90 longitude
        assertEquals(270, getNorthFix(polarCRS, getPoint(-10000, 0)), EPS);
        // now going around at diagonal angles instead
        assertEquals(45, getNorthFix(polarCRS, getPoint(10000, 10000)), EPS);
        assertEquals(135, getNorthFix(polarCRS, getPoint(10000, -10000)), EPS);
        assertEquals(225, getNorthFix(polarCRS, getPoint(-10000, -10000)), EPS);
        assertEquals(315, getNorthFix(polarCRS, getPoint(-10000, 10000)), EPS);
    }

    /**
     * Real world use case, with source and target CRS, visually tested. Source CRS provided as a
     * direct function parameter.
     */
    @Test
    public void testPolarExplicitSourceCRS() throws Exception {
        CoordinateReferenceSystem polarCRS = CRS.decode("EPSG:3976");

        Point pt = getPoint(-102, -73);
        Function function =
                FF.function(
                        NORTH_FIX, FF.literal(polarCRS), FF.literal(pt), FF.literal(195), WGS84_LT);
        assertEquals(93, function.evaluate(null, Double.class), EPS);
    }

    /**
     * Real world use case, with source and target CRS, visually tested. Source CRS coming from the
     * geometry user data.
     */
    @Test
    public void testPolarMetadataSourceCRS() throws Exception {
        CoordinateReferenceSystem polarCRS = CRS.decode("EPSG:3976");

        Point pt = getPoint(-102, -73);
        pt.setUserData(WGS84);
        Function function =
                FF.function(NORTH_FIX, FF.literal(polarCRS), FF.literal(pt), FF.literal(195));
        assertEquals(93, function.evaluate(null, Double.class), EPS);
    }

    /**
     * Real world use case, with source and target CRS, visually tested. Source CRS coming from the
     * geometry user data.
     */
    @Test
    public void testPolarSridCRS() throws Exception {
        CoordinateReferenceSystem polarCRS = CRS.decode("EPSG:3976");

        Point pt = getPoint(-102, -73);
        pt.setSRID(4326);
        Function function =
                FF.function(NORTH_FIX, FF.literal(polarCRS), FF.literal(pt), FF.literal(195));
        assertEquals(93, function.evaluate(null, Double.class), EPS);
    }

    /**
     * Real world use case, with source and target CRS, visually tested. Source CRS coming from the
     * feature schema.
     */
    @Test
    public void testPolarFeatureCRS() throws Exception {
        CoordinateReferenceSystem polarCRS = CRS.decode("EPSG:3976");

        SimpleFeature feature = buildWGS84Feature();

        Function function =
                FF.function(NORTH_FIX, FF.literal(polarCRS), FF.property("geom"), FF.literal(195));
        assertEquals(93, function.evaluate(feature, Double.class), EPS);
    }

    private static SimpleFeature buildWGS84Feature() {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.add("geom", Point.class, WGS84);
        tb.setName("points");
        SimpleFeatureType ft = tb.buildFeatureType();
        SimpleFeature feature = DataUtilities.createFeature(ft, "POINT(-102 -73)");
        return feature;
    }

    private static Point getPoint(double x, double y) {
        return GF.createPoint(new Coordinate(x, y));
    }

    private static Double getNorthFix(CoordinateReferenceSystem crs, Point point) {
        Function function = FF.function(NORTH_FIX, FF.literal(crs), FF.literal(point));
        return (Double) function.evaluate(null);
    }

    @Test
    public void testSimplifyNoConversion() throws Exception {
        // no conversion needed, just use the angle directly
        PropertyName angle = FF.property("myAngle");
        NorthFix function = (NorthFix) FF.function(NORTH_FIX, WGS84_LT, POINT_LT, angle);
        Expression simplified = function.simplify(FF, null);
        assertSame(angle, simplified);
    }

    @Test
    public void testSimplifyNoAngle() throws Exception {
        // no conversion needed, no angle either -> new static literal
        NorthFix function = (NorthFix) FF.function(NORTH_FIX, WGS84_LT, POINT_LT);
        Expression simplified = function.simplify(FF, null);
        assertEquals(FF.literal(0d), simplified);
    }

    @Test
    public void testSimplifyCRSConversions() throws Exception {
        // function that could use conversion to static CRS objects (rather than strings)
        NorthFix function =
                (NorthFix)
                        FF.function(
                                NORTH_FIX,
                                FF.literal("EPSG:3976"),
                                POINT_LT,
                                ANGLE_LT,
                                FF.literal("CRS:84"));
        NorthFix simplified = (NorthFix) function.simplify(FF, null);

        Expression targetCRSParameter = simplified.getParameters().get(0);
        assertThat(targetCRSParameter, instanceOf(Literal.class));
        assertEquals(CRS.decode("EPSG:3976"), ((Literal) targetCRSParameter).getValue());

        Expression sourceCRSParameter = simplified.getParameters().get(3);
        assertThat(sourceCRSParameter, instanceOf(Literal.class));
        assertEquals(CRS.decode("CRS:84"), ((Literal) sourceCRSParameter).getValue());
    }

    @Test
    public void testSimplifyFromFeatureType() throws Exception {
        SimpleFeature feature = buildWGS84Feature();
        PropertyName angle = FF.property("myAngle");
        // the source CRS can be looked up dynamically from the feature type, the simplification
        // should make that lookup static
        NorthFix function =
                (NorthFix)
                        FF.function(NORTH_FIX, FF.literal("EPSG:3976"), FF.property("geom"), angle);
        assertEquals(3, function.getParameters().size());
        NorthFix simplified = (NorthFix) function.simplify(FF, feature.getFeatureType());
        assertEquals(4, simplified.getParameters().size());
        assertEquals(WGS84_LT, simplified.getParameters().get(3));
    }
}
