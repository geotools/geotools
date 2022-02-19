package org.geotools.filter.spatial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Collections;
import java.util.List;
import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureTypes;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Intersects;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

@SuppressWarnings("PMD.SimplifiableTestAssertion") // using equals with tolerance for bboxes
public class ReprojectingFilterVisitorTest {

    SimpleFeatureType ft;
    FilterFactory2 ff;
    ReprojectingFilterVisitor reprojector;

    @Before
    public void setUp() throws Exception {
        // this is the only thing that actually forces CRS object to give up
        // its configuration, necessary when tests are run by Maven, one JVM for all
        // the tests in this module
        Hints.putSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
        GeoTools.fireConfigurationChanged();
        ft =
                DataUtilities.createType(
                        "testType", "geom:Point:srid=4326,line:LineString,name:String,id:int");
        ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
        reprojector = new ReprojectingFilterVisitor(ff, ft);
    }

    /** Make sure it does not break with non spatial filters */
    @Test
    public void testNoProjection() {
        Filter idFilter = ff.id(Collections.singleton(ff.featureId("testType:1")));
        Filter clone = (Filter) idFilter.accept(reprojector, null);
        Assert.assertNotSame(idFilter, clone);
        assertEquals(idFilter, clone);
    }

    @Test
    public void testBboxNoReprojection() {
        // no reprojection needed in fact
        Filter bbox = ff.bbox(ff.property("geom"), 10, 10, 20, 20, "EPSG:4326");
        Filter clone = (Filter) bbox.accept(reprojector, null);
        Assert.assertNotSame(bbox, clone);
        assertEquals(bbox, clone);
    }

    @Test
    public void testBboxReproject() throws FactoryException {
        // see if coordinates gets flipped, urn forces lat/lon interpretation
        BBOX bbox =
                ff.bbox(ff.property("geom"), 10, 15, 20, 25, "urn:x-ogc:def:crs:EPSG:6.11.2:4326");
        Filter clone = (Filter) bbox.accept(reprojector, null);
        Assert.assertNotSame(bbox, clone);
        BBOX clonedBbox = (BBOX) clone;
        assertEquals(bbox.getExpression1(), clonedBbox.getExpression1());
        Assert.assertTrue(
                JTS.equals(
                        new ReferencedEnvelope(15, 25, 10, 20, CRS.decode("EPSG:4326", false)),
                        clonedBbox.getBounds(),
                        1e-6));
    }

    @Test
    public void testBboxReprojectNoNativeAuthority() throws Exception {
        // like WGS84, but no authority
        String wkt =
                "GEOGCS[\"WGS 84\", DATUM[\"World Geodetic System 1984\", SPHEROID[\"WGS 84\", 6378137.0, 298.257223563]], PRIMEM[\"Greenwich\", 0.0], UNIT[\"degree\", 0.017453292519943295], AXIS[\"Geodetic longitude\", EAST], AXIS[\"Geodetic latitude\", NORTH]]";
        CoordinateReferenceSystem crs = CRS.parseWKT(wkt);
        SimpleFeatureType newFt = FeatureTypes.transform(ft, crs);
        reprojector = new ReprojectingFilterVisitor(ff, newFt);

        BBOX bbox =
                ff.bbox(ff.property("geom"), 10, 15, 20, 25, "urn:x-ogc:def:crs:EPSG:6.11.2:4326");
        Filter clone = (Filter) bbox.accept(reprojector, null);
        Assert.assertNotSame(bbox, clone);
        BBOX clonedBbox = (BBOX) clone;
        assertEquals(bbox.getExpression1(), clonedBbox.getExpression1());
        Assert.assertTrue(
                JTS.equals(
                        new ReferencedEnvelope(15, 25, 10, 20, CRS.decode("EPSG:4326", false)),
                        clonedBbox.getBounds(),
                        1e-6));
    }

    @Test
    public void testBboxReprojectUnreferencedProperty() {
        // see if coordinates gets flipped, urn forces lat/lon interpretation
        BBOX bbox =
                ff.bbox(ff.property("line"), 10, 15, 20, 25, "urn:x-ogc:def:crs:EPSG:6.11.2:4326");
        Filter clone = (Filter) bbox.accept(reprojector, null);
        Assert.assertNotSame(bbox, clone);
        assertEquals(bbox, clone);
    }

    @Test
    public void testBboxReprojectUnreferencedBBox() {
        // see if coordinates gets flipped, urn forces lat/lon interpretation
        BBOX bbox = ff.bbox(ff.property("geom"), 10, 15, 20, 25, null);
        Filter clone = (Filter) bbox.accept(reprojector, null);
        Assert.assertNotSame(bbox, clone);
        assertEquals(bbox, clone);
    }

    @Test
    public void checkIntersectsReproject() throws Exception {
        Expression geom = ff.property("geom");
        checkIntersectsReproject(geom);
    }

    @Test
    public void testBoundedByReproject() throws Exception {
        Expression geom = ff.function("boundedBy");
        checkIntersectsReproject(geom);
    }

    public void checkIntersectsReproject(Expression geom) throws FactoryException {
        GeometryFactory gf = new GeometryFactory();
        LineString ls =
                gf.createLineString(
                        new Coordinate[] {new Coordinate(10, 15), new Coordinate(20, 25)});
        ls.setUserData(CRS.decode("urn:x-ogc:def:crs:EPSG:6.11.2:4326"));

        // see if coordinates gets flipped, urn forces lat/lon interpretation

        Intersects original = ff.intersects(geom, ff.literal(ls));
        Filter clone = (Filter) original.accept(reprojector, null);
        Assert.assertNotSame(original, clone);
        Intersects isClone = (Intersects) clone;
        assertEquals(isClone.getExpression1(), original.getExpression1());
        LineString clonedLs = (LineString) ((Literal) isClone.getExpression2()).getValue();
        assertEquals(15d, clonedLs.getCoordinateN(0).x, 0d);
        assertEquals(10d, clonedLs.getCoordinateN(0).y, 0d);
        assertEquals(25d, clonedLs.getCoordinateN(1).x, 0d);
        assertEquals(20d, clonedLs.getCoordinateN(1).y, 0d);
        assertEquals(CRS.decode("EPSG:4326"), clonedLs.getUserData());
    }

    @Test
    public void testIntersectsUnreferencedGeometry() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        LineString ls =
                gf.createLineString(
                        new Coordinate[] {new Coordinate(10, 15), new Coordinate(20, 25)});

        // see if coordinates gets flipped, urn forces lat/lon interpretation
        Intersects original = ff.intersects(ff.property("geom"), ff.literal(ls));
        Filter clone = (Filter) original.accept(reprojector, null);
        Assert.assertNotSame(original, clone);
        assertEquals(original, clone);
    }

    @Test
    public void testIntersectsReferencedGeometry() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        LineString ls =
                gf.createLineString(
                        new Coordinate[] {new Coordinate(10, 15), new Coordinate(20, 25)});
        ls.setUserData(CRS.decode("urn:x-ogc:def:crs:EPSG:6.11.2:4326"));

        // see if coordinates gets flipped, urn forces lat/lon interpretation
        Intersects original = ff.intersects(ff.property("line"), ff.literal(ls));
        Filter clone = (Filter) original.accept(reprojector, null);

        Assert.assertNotSame(original, clone);
        Intersects isClone = (Intersects) clone;
        assertEquals(isClone.getExpression1(), original.getExpression1());
        LineString clonedLs = (LineString) isClone.getExpression2().evaluate(null);
        assertEquals(15d, clonedLs.getCoordinateN(0).x, 0d);
        assertEquals(10d, clonedLs.getCoordinateN(0).y, 0d);
        assertEquals(25d, clonedLs.getCoordinateN(1).x, 0d);
        assertEquals(20d, clonedLs.getCoordinateN(1).y, 0d);
        assertEquals(CRS.decode("EPSG:4326"), clonedLs.getUserData());
    }

    @Test
    public void testPropertyEqualsFirstArgumentNotPropertyName() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        LineString ls =
                gf.createLineString(
                        new Coordinate[] {new Coordinate(10, 15), new Coordinate(20, 25)});
        ls.setUserData(CRS.decode("urn:x-ogc:def:crs:EPSG:6.11.2:4326"));

        // make sure a class cast does not occur, see: http://jira.codehaus.org/browse/GEOS-1860
        Function function = ff.function("geometryType", ff.property("geom"));
        PropertyIsEqualTo original = ff.equals(ff.literal("Point"), function);
        Filter clone = (Filter) original.accept(reprojector, null);
        Assert.assertNotSame(original, clone);
        assertEquals(original, clone);

        // try the opposite, literal and function
        original = ff.equals(function, ff.literal("Point"));
        clone = (Filter) original.accept(reprojector, null);
        Assert.assertNotSame(original, clone);
        assertEquals(original, clone);
    }

    @Test
    public void testIntersectsWithFunction() throws Exception {
        Function function = new GeometryFunction();

        // see if coordinates gets flipped, urn forces lat/lon interpretation
        Intersects original = ff.intersects(ff.property("geom"), function);
        Filter clone = (Filter) original.accept(reprojector, null);
        Assert.assertNotSame(original, clone);
        Intersects isClone = (Intersects) clone;
        assertEquals(isClone.getExpression1(), original.getExpression1());
        LineString clonedLs = (LineString) isClone.getExpression2().evaluate(null);
        assertEquals(15d, clonedLs.getCoordinateN(0).x, 0d);
        assertEquals(10d, clonedLs.getCoordinateN(0).y, 0d);
        assertEquals(25d, clonedLs.getCoordinateN(1).x, 0d);
        assertEquals(20d, clonedLs.getCoordinateN(1).y, 0d);
        assertEquals(CRS.decode("EPSG:4326"), clonedLs.getUserData());
    }

    @Test
    public void testPropertyEqualWithFunction() throws Exception {
        Function function = new GeometryFunction();

        // see if coordinates gets flipped, urn forces lat/lon interpretation
        PropertyIsEqualTo original = ff.equals(ff.property("geom"), function);
        PropertyIsEqualTo clone = (PropertyIsEqualTo) original.accept(reprojector, null);
        Assert.assertNotSame(original, clone);
        assertEquals(clone.getExpression1(), original.getExpression1());
        LineString clonedLs = (LineString) clone.getExpression2().evaluate(null);
        assertEquals(15d, clonedLs.getCoordinateN(0).x, 0d);
        assertEquals(10d, clonedLs.getCoordinateN(0).y, 0d);
        assertEquals(25d, clonedLs.getCoordinateN(1).x, 0d);
        assertEquals(20d, clonedLs.getCoordinateN(1).y, 0d);
        assertEquals(CRS.decode("EPSG:4326"), clonedLs.getUserData());
    }

    @Test
    public void testIntersectsFilterFunctionUnreferencedGeometry() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        LineString ls =
                gf.createLineString(
                        new Coordinate[] {new Coordinate(10, 15), new Coordinate(20, 25)});

        Function intersects = ff.function("intersects", ff.property("geom"), ff.literal(ls));
        Function clone = (Function) intersects.accept(reprojector, null);
        Assert.assertNotSame(intersects, clone);
        assertEquals(clone.getParameters().get(0), intersects.getParameters().get(0));
        assertEquals(clone.getParameters().get(1), intersects.getParameters().get(1));
    }

    @Test
    public void testIntersectsFilterFunctionReferencedGeometry() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        LineString ls =
                gf.createLineString(
                        new Coordinate[] {new Coordinate(10, 15), new Coordinate(20, 25)});
        ls.setUserData(CRS.decode("urn:x-ogc:def:crs:EPSG:6.11.2:4326"));

        Function intersects = ff.function("intersects", ff.property("geom"), ff.literal(ls));
        Function clone = (Function) intersects.accept(reprojector, null);

        Assert.assertNotSame(intersects, clone);
        assertEquals(clone.getParameters().get(0), intersects.getParameters().get(0));
        assertNotEquals(clone.getParameters().get(1), intersects.getParameters().get(1));

        LineString clonedLs = (LineString) ((Literal) clone.getParameters().get(1)).getValue();
        assertEquals(15d, clonedLs.getCoordinateN(0).x, 0d);
        assertEquals(10d, clonedLs.getCoordinateN(0).y, 0d);
        assertEquals(25d, clonedLs.getCoordinateN(1).x, 0d);
        assertEquals(20d, clonedLs.getCoordinateN(1).y, 0d);
        assertEquals(CRS.decode("EPSG:4326"), clonedLs.getUserData());
    }

    /** The provided target CRS (3857) should override the native one (4326). */
    @Test
    public void testBboxReprojectWithTargetCrsProvided() throws FactoryException {
        CoordinateReferenceSystem webMercator = CRS.decode("EPSG:3857");
        ReprojectingFilterVisitor reprojector = new ReprojectingFilterVisitor(ff, ft, webMercator);
        BBOX bbox = ff.bbox(ff.property("geom"), 10, 15, 20, 25, "EPSG:4326");
        Filter clone = (Filter) bbox.accept(reprojector, null);
        Assert.assertNotSame(bbox, clone);
        BBOX clonedBbox = (BBOX) clone;
        assertEquals(bbox.getExpression1(), clonedBbox.getExpression1());
        Assert.assertTrue(
                JTS.equals(
                        new ReferencedEnvelope(
                                1113194.9079327357,
                                2226389.8158654715,
                                1689200.1396078924,
                                2875744.6243522423,
                                webMercator),
                        clonedBbox.getBounds(),
                        1e-6));
    }

    /**
     * The provided target CRS (3857) should not override the native one (4326) since the use
     * property is not a geometry.
     */
    @Test
    public void testTargetCrsProvidedButNoGeometryProperty() throws FactoryException {
        ReprojectingFilterVisitor reprojector =
                new ReprojectingFilterVisitor(ff, ft, CRS.decode("EPSG:3857"));
        BBOX bbox = ff.bbox(ff.property("name"), 10, 15, 20, 25, "EPSG:4326");
        BBOX clone = (BBOX) bbox.accept(reprojector, null);
        Assert.assertNotSame(bbox, clone);
        // check that no reprojection was applied
        assertEquals(bbox.getExpression1(), clone.getExpression1());
        Assert.assertTrue(
                JTS.equals(
                        new ReferencedEnvelope(10, 20, 15, 25, CRS.decode("EPSG:4326")),
                        clone.getBounds(),
                        0.1));
    }

    private final class GeometryFunction implements Function {
        final LineString ls;

        public GeometryFunction() throws Exception {
            GeometryFactory gf = new GeometryFactory();
            ls =
                    gf.createLineString(
                            new Coordinate[] {new Coordinate(10, 15), new Coordinate(20, 25)});
            ls.setUserData(CRS.decode("urn:x-ogc:def:crs:EPSG:6.11.2:4326"));
        }

        @Override
        public String getName() {
            return "function";
        }

        @Override
        public List<Expression> getParameters() {
            return Collections.emptyList();
        }

        @Override
        public Object accept(ExpressionVisitor visitor, Object extraData) {
            return visitor.visit(this, extraData);
        }

        @Override
        public Object evaluate(Object object) {
            return ls;
        }

        @Override
        public <T> T evaluate(Object object, Class<T> context) {
            return context.cast(ls);
        }

        @Override
        public Literal getFallbackValue() {
            return null;
        }

        @Override
        public FunctionName getFunctionName() {
            return new FunctionNameImpl("geometryfunction");
        }
    }
}
