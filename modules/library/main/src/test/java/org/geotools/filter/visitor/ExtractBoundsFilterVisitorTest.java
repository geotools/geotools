package org.geotools.filter.visitor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.temporal.TemporalFilterTestSupport;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;

/** @source $URL$ */
public class ExtractBoundsFilterVisitorTest extends TemporalFilterTestSupport {

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    ExtractBoundsFilterVisitor visitor = new ExtractBoundsFilterVisitor();

    Envelope infinity = visitor.infinity();

    @Test
    public void testInclude() {
        Envelope env = (Envelope) Filter.INCLUDE.accept(visitor, null);
        assertEquals(infinity, env);
    }

    @Test
    public void testExclude() {
        Envelope env = (Envelope) Filter.EXCLUDE.accept(visitor, null);
        assertTrue(env.isNull());
    }

    @Test
    public void testNonSpatial() {
        Filter f = ff.less(ff.property("att"), ff.literal(10));
        Envelope env = (Envelope) f.accept(visitor, null);
        assertEquals(infinity, env);
    }

    @Test
    public void testBbox() {
        Filter f = ff.bbox("geom", -10, -10, 10, 10, null);
        Envelope env = (Envelope) f.accept(visitor, null);
        assertEquals(new Envelope(-10, 10, -10, 10), env);
    }

    @Test
    public void testAnd() {
        Filter f =
                ff.and(
                        ff.bbox("geom", -10, -10, 10, 10, null),
                        ff.equals(ff.property("att"), ff.literal("10")));
        Envelope env = (Envelope) f.accept(visitor, null);
        assertEquals(new Envelope(-10, 10, -10, 10), env);
    }

    @Test
    public void testOr() {
        Filter f =
                ff.or(
                        ff.bbox("geom", -10, -10, 10, 10, null),
                        ff.equals(ff.property("att"), ff.literal("10")));
        Envelope env = (Envelope) f.accept(visitor, null);
        assertEquals(infinity, env);
    }

    @Test
    public void testTouches() {
        Coordinate[] coords = new Coordinate[] {new Coordinate(0, 0), new Coordinate(10, 10)};
        LineString lineString = new GeometryFactory().createLineString(coords);
        Filter filter = ff.touches(ff.property("name"), ff.literal(lineString));
        Envelope env = (Envelope) filter.accept(visitor, null);
        assertEquals(new Envelope(0, 10, 0, 10), env);
    }

    @Test
    public void testBeyond() {
        Coordinate[] coords = new Coordinate[] {new Coordinate(0, 0), new Coordinate(10, 10)};
        LineString lineString = new GeometryFactory().createLineString(coords);
        Filter filter = ff.beyond(ff.property("name"), ff.literal(lineString), 100, "m");
        Envelope env = (Envelope) filter.accept(visitor, null);
        assertEquals(infinity, env);
    }

    @Test
    public void testNotBeyond() {
        Coordinate[] coords = new Coordinate[] {new Coordinate(0, 0), new Coordinate(10, 10)};
        LineString lineString = new GeometryFactory().createLineString(coords);
        Filter filter = ff.beyond(ff.property("name"), ff.literal(lineString), 100, "m");
        Envelope env = (Envelope) filter.accept(visitor, null);
        // the thing is not so smart to assess that not(beyond) -> within, but we have to make
        // sure that at least the returned envelope contains the real one
        assertEquals(infinity, env);
    }

    @Test
    public void testNull() {
        Filter filter = ff.isNull(ff.property("name"));
        Envelope env = (Envelope) filter.accept(visitor, null);
        assertEquals(infinity, env);
    }

    @Test
    public void testDWithin() {
        Point geom = new GeometryFactory().createPoint(new Coordinate(0, 0));
        Filter filter = ff.dwithin(ff.property("name"), ff.literal(geom), 100, "metre");
        Envelope env = (Envelope) filter.accept(visitor, null);

        assertEquals(new Envelope(-100, 100, -100, 100), env);
    }

    @Test
    public void testAndDWithin() {
        Point geom = new GeometryFactory().createPoint(new Coordinate(0, 0));
        Filter filter = ff.dwithin(ff.property("geom"), ff.literal(geom), 100, "metre");
        filter = ff.and(filter, ff.bbox(ff.property("geom"), 50, 50, 150, 150, null));
        Envelope env = (Envelope) filter.accept(visitor, null);

        assertEquals(new Envelope(50, 100, 50, 100), env);
    }

    @Test
    public void testDisjoint() {
        Coordinate[] coords = new Coordinate[] {new Coordinate(0, 0), new Coordinate(10, 10)};
        LineString lineString = new GeometryFactory().createLineString(coords);
        Filter filter = ff.disjoint(ff.property("name"), ff.literal(lineString));
        Envelope env = (Envelope) filter.accept(visitor, null);

        assertEquals(infinity, env);
    }

    @Test
    public void testAndDisjoint() {
        Coordinate[] coords = new Coordinate[] {new Coordinate(0, 0), new Coordinate(10, 10)};
        LineString lineString = new GeometryFactory().createLineString(coords);
        Filter filter = ff.disjoint(ff.property("name"), ff.literal(lineString));
        filter = ff.and(filter, ff.bbox(ff.property("geom"), 50, 50, 150, 150, null));
        Envelope env = (Envelope) filter.accept(visitor, null);

        assertEquals(new Envelope(50, 150, 50, 150), env);
    }

    @Test
    public void testAndTemporalBetween() throws Exception {
        final Instant start = instant("2016-01-01T00:00:00.000-0500");
        final Instant end = instant("2106-01-02T00:00:00.000-0500");
        final Filter f =
                ff.and(
                        ff.bbox("geom", -10, -10, 10, 10, null),
                        ff.between(ff.literal("someDate"), ff.literal(start), ff.literal(end)));
        final Envelope env = (Envelope) f.accept(visitor, null);
        assertEquals(new Envelope(-10, 10, -10, 10), env);
    }

    @Test
    public void testAndTemporalAfter() throws Exception {
        final Instant start = instant("2016-01-01T00:00:00.000-0500");
        final Filter f =
                ff.and(
                        ff.bbox("geom", -10, -10, 10, 10, null),
                        ff.after(ff.literal("someDate"), ff.literal(start)));
        final Envelope env = (Envelope) f.accept(visitor, null);
        assertEquals(new Envelope(-10, 10, -10, 10), env);
    }

    @Test
    public void testAndTemporalBefore() throws Exception {
        final Instant start = instant("2016-01-01T00:00:00.000-0500");
        final Filter f =
                ff.and(
                        ff.bbox("geom", -10, -10, 10, 10, null),
                        ff.before(ff.literal("someDate"), ff.literal(start)));
        final Envelope env = (Envelope) f.accept(visitor, null);
        assertEquals(new Envelope(-10, 10, -10, 10), env);
    }

    @Test
    public void testAndTemporalBegins() throws Exception {
        final Instant start = instant("2016-01-01T00:00:00.000-0500");
        final Filter f =
                ff.and(
                        ff.bbox("geom", -10, -10, 10, 10, null),
                        ff.begins(ff.literal("someDate"), ff.literal(start)));
        final Envelope env = (Envelope) f.accept(visitor, null);
        assertEquals(new Envelope(-10, 10, -10, 10), env);
    }

    @Test
    public void testAndTemporalBegunBy() throws Exception {
        final Instant start = instant("2016-01-01T00:00:00.000-0500");
        final Filter f =
                ff.and(
                        ff.bbox("geom", -10, -10, 10, 10, null),
                        ff.begunBy(ff.literal("someDate"), ff.literal(start)));
        final Envelope env = (Envelope) f.accept(visitor, null);
        assertEquals(new Envelope(-10, 10, -10, 10), env);
    }

    @Test
    public void testAndTemporalDuring() throws Exception {
        final Period p = period("2016-01-01T00:00:00.000-0500", "2106-01-02T00:00:00.000-0500");
        final Filter f =
                ff.and(
                        ff.bbox("geom", -10, -10, 10, 10, null),
                        ff.during(ff.literal("someDate"), ff.literal(p)));
        final Envelope env = (Envelope) f.accept(visitor, null);
        assertEquals(new Envelope(-10, 10, -10, 10), env);
    }

    @Test
    public void testAndTemporalEndedBy() throws Exception {
        final Instant start = instant("2016-01-01T00:00:00.000-0500");
        final Filter f =
                ff.and(
                        ff.bbox("geom", -10, -10, 10, 10, null),
                        ff.endedBy(ff.literal("someDate"), ff.literal(start)));
        final Envelope env = (Envelope) f.accept(visitor, null);
        assertEquals(new Envelope(-10, 10, -10, 10), env);
    }

    @Test
    public void testAndTemporalEnds() throws Exception {
        final Instant start = instant("2016-01-01T00:00:00.000-0500");
        final Filter f =
                ff.and(
                        ff.bbox("geom", -10, -10, 10, 10, null),
                        ff.ends(ff.literal("someDate"), ff.literal(start)));
        final Envelope env = (Envelope) f.accept(visitor, null);
        assertEquals(new Envelope(-10, 10, -10, 10), env);
    }

    @Test
    public void testAndTemporalMeets() throws Exception {
        final Period p = period("2016-01-01T00:00:00.000-0500", "2106-01-02T00:00:00.000-0500");
        final Filter f =
                ff.and(
                        ff.bbox("geom", -10, -10, 10, 10, null),
                        ff.meets(ff.literal("someDate"), ff.literal(p)));
        final Envelope env = (Envelope) f.accept(visitor, null);
        assertEquals(new Envelope(-10, 10, -10, 10), env);
    }

    @Test
    public void testAndTemporalMetBy() throws Exception {
        final Period p = period("2016-01-01T00:00:00.000-0500", "2106-01-02T00:00:00.000-0500");
        final Filter f =
                ff.and(
                        ff.bbox("geom", -10, -10, 10, 10, null),
                        ff.metBy(ff.literal("someDate"), ff.literal(p)));
        final Envelope env = (Envelope) f.accept(visitor, null);
        assertEquals(new Envelope(-10, 10, -10, 10), env);
    }

    @Test
    public void testAndTemporalOverlappedBy() throws Exception {
        final Period p = period("2016-01-01T00:00:00.000-0500", "2106-01-02T00:00:00.000-0500");
        final Filter f =
                ff.and(
                        ff.bbox("geom", -10, -10, 10, 10, null),
                        ff.overlappedBy(ff.literal("someDate"), ff.literal(p)));
        final Envelope env = (Envelope) f.accept(visitor, null);
        assertEquals(new Envelope(-10, 10, -10, 10), env);
    }

    @Test
    public void testAndTemporalTContains() throws Exception {
        final Period p = period("2016-01-01T00:00:00.000-0500", "2106-01-02T00:00:00.000-0500");
        final Filter f =
                ff.and(
                        ff.bbox("geom", -10, -10, 10, 10, null),
                        ff.tcontains(ff.literal("someDate"), ff.literal(p)));
        final Envelope env = (Envelope) f.accept(visitor, null);
        assertEquals(new Envelope(-10, 10, -10, 10), env);
    }

    @Test
    public void testAndTemporalTEquals() throws Exception {
        final Period p = period("2016-01-01T00:00:00.000-0500", "2106-01-02T00:00:00.000-0500");
        final Filter f =
                ff.and(
                        ff.bbox("geom", -10, -10, 10, 10, null),
                        ff.tequals(ff.literal("someDate"), ff.literal(p)));
        final Envelope env = (Envelope) f.accept(visitor, null);
        assertEquals(new Envelope(-10, 10, -10, 10), env);
    }

    @Test
    public void testAndTemporalTOverlaps() throws Exception {
        final Period p = period("2016-01-01T00:00:00.000-0500", "2106-01-02T00:00:00.000-0500");
        final Filter f =
                ff.and(
                        ff.bbox("geom", -10, -10, 10, 10, null),
                        ff.toverlaps(ff.literal("someDate"), ff.literal(p)));
        final Envelope env = (Envelope) f.accept(visitor, null);
        assertEquals(new Envelope(-10, 10, -10, 10), env);
    }

    @Test
    public void testAndIsNull() throws Exception {
        final Filter f =
                ff.and(ff.bbox("geom", -10, -10, 10, 10, null), ff.isNull(ff.literal("someDate")));
        final Envelope env = (Envelope) f.accept(visitor, null);
        assertEquals(new Envelope(-10, 10, -10, 10), env);
    }

    @Test
    public void testAndIsNil() throws Exception {
        final Filter f =
                ff.and(
                        ff.bbox("geom", -10, -10, 10, 10, null),
                        ff.isNil(ff.literal("someDate"), null));
        final Envelope env = (Envelope) f.accept(visitor, null);
        assertEquals(new Envelope(-10, 10, -10, 10), env);
    }
}
