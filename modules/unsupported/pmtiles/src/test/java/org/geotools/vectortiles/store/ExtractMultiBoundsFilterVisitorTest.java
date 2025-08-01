/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vectortiles.store;

import static org.geotools.vectortiles.store.ExtractMultiBoundsFilterVisitor.getBounds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.List;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;

public class ExtractMultiBoundsFilterVisitorTest {
    private static final String SRS = "EPSG:404000";

    private static final CoordinateReferenceSystem crs;

    static {
        try {
            crs = CRS.decode(SRS);
        } catch (FactoryException e) {
            throw new IllegalStateException();
        }
    }

    FilterFactory FF = CommonFactoryFinder.getFilterFactory();

    private ReferencedEnvelope envelope(double x1, double x2, double y1, double y2) {
        return new ReferencedEnvelope(x1, x2, y1, y2, crs);
    }

    @Test
    public void testBoundsSimple() throws FactoryException {
        BBOX bbox = FF.bbox(FF.property(""), -10, -10, 10, 10, SRS);
        List<ReferencedEnvelope> bounds = getBounds(bbox, crs);
        assertThat(bounds, contains(envelope(-10, 10, -10, 10)));
    }

    @Test
    public void testBoundsSeparateOr() throws FactoryException {
        BBOX bbox1 = FF.bbox(FF.property(""), -10, -10, 10, 10, SRS);
        BBOX bbox2 = FF.bbox(FF.property(""), 50, -10, 60, 10, SRS);
        Filter filter = FF.or(bbox1, bbox2);
        List<ReferencedEnvelope> bounds = getBounds(filter, crs);
        assertThat(bounds, contains(envelope(-10, 10, -10, 10), envelope(50, 60, -10, 10)));
    }

    @Test
    public void testBoundsSeparateAnd() throws FactoryException {
        BBOX bbox1 = FF.bbox(FF.property(""), -10, -10, 10, 10, SRS);
        BBOX bbox2 = FF.bbox(FF.property(""), 50, -10, 60, 10, SRS);
        // and-ing disjoint, should be empty
        Filter filter = FF.and(bbox1, bbox2);
        List<ReferencedEnvelope> bounds = getBounds(filter, crs);
        assertThat(bounds, contains(new Envelope()));
    }

    @Test
    public void testBoundsSeparateOrIntersect() throws FactoryException {
        BBOX bbox1 = FF.bbox(FF.property(""), -10, -10, 10, 10, SRS);
        BBOX bbox2 = FF.bbox(FF.property(""), 50, -10, 70, 10, SRS);
        BBOX bbox3 = FF.bbox(FF.property(""), 0, -10, 60, 10, SRS);
        // two separate or-ed bounds, and-ed with one that overlaps them partially both
        Filter filter = FF.and(bbox3, FF.or(bbox1, bbox2));
        List<ReferencedEnvelope> bounds = getBounds(filter, crs);
        assertThat(bounds, Matchers.containsInAnyOrder(envelope(0, 10, -10, 10), envelope(50, 60, -10, 10)));
    }

    /**
     * Same as {@link #testBoundsSeparateOrIntersect()}, but changing the order of the operands in a way that the result
     * should be the same
     */
    @Test
    public void testBoundsSeparateOrIntersectFlipped() throws FactoryException {
        BBOX bbox1 = FF.bbox(FF.property(""), -10, -10, 10, 10, SRS);
        BBOX bbox2 = FF.bbox(FF.property(""), 50, -10, 70, 10, SRS);
        BBOX bbox3 = FF.bbox(FF.property(""), 0, -10, 60, 10, SRS);
        // two separate or-ed bounds, and-ed with one that overlaps them partially both
        Filter filter = FF.and(FF.or(bbox2, bbox1), bbox3);
        List<ReferencedEnvelope> bounds = getBounds(filter, crs);
        assertThat(bounds, Matchers.containsInAnyOrder(envelope(0, 10, -10, 10), envelope(50, 60, -10, 10)));
    }

    @Test
    public void testBoundsMultiOrAnd() throws FactoryException {
        BBOX bbox1 = FF.bbox(FF.property(""), -10, -10, 10, 10, SRS);
        BBOX bbox2 = FF.bbox(FF.property(""), 50, -10, 70, 10, SRS);
        BBOX bbox3 = FF.bbox(FF.property(""), 0, -5, 5, 5, SRS);
        BBOX bbox4 = FF.bbox(FF.property(""), 45, 5, 55, 15, SRS);
        Filter filter = FF.and(FF.or(bbox2, bbox1), FF.or(bbox3, bbox4));
        List<ReferencedEnvelope> bounds = getBounds(filter, crs);
        assertThat(bounds, Matchers.containsInAnyOrder(envelope(0, 5, -5, 5), envelope(50, 55, 5, 10)));
    }

    @Test
    public void testOrWithNonSpatialFilter() throws FactoryException {
        BBOX spatialFilter = FF.bbox(FF.property(""), -10, -10, 10, 10, SRS);
        Filter in = FF.or(
                FF.equal(FF.function("in", FF.property("kind"), FF.literal("commercial")), FF.literal(true), false),
                FF.equal(FF.function("in", FF.property("kind"), FF.literal("commercial")), FF.literal(true), false));

        Filter filter = FF.or(spatialFilter, in);
        List<ReferencedEnvelope> bounds = getBounds(filter, crs);
        assertThat(bounds, Matchers.containsInAnyOrder(envelope(-10, 10, -10, 10)));

        filter = FF.or(in, spatialFilter);
        bounds = getBounds(filter, crs);
        assertThat(bounds, Matchers.contains(envelope(-10, 10, -10, 10)));
    }

    @Test
    public void testAndWithNonSpatialFilter() throws FactoryException {
        BBOX spatialFilter = FF.bbox(FF.property(""), -10, -10, 10, 10, SRS);
        Filter in = FF.or(
                FF.equal(FF.function("in", FF.property("kind"), FF.literal("commercial")), FF.literal(true), false),
                FF.equal(FF.function("in", FF.property("kind"), FF.literal("commercial")), FF.literal(true), false));

        Filter filter = FF.and(spatialFilter, in);
        List<ReferencedEnvelope> bounds = getBounds(filter, crs);
        assertThat(bounds, Matchers.containsInAnyOrder(envelope(-10, 10, -10, 10)));

        filter = FF.and(in, spatialFilter);
        bounds = getBounds(filter, crs);
        assertThat(bounds, Matchers.contains(envelope(-10, 10, -10, 10)));
    }

    @Test
    public void testComplexFilter() throws CQLException {
        Filter filter = ECQL.toFilter(
                """
                BBOX(the_geom, -10, -10, 10, 10)
                AND
                (
                  kind IN ('pier', 'breakwater', 'groyne')
                  OR
                  (
                  	(geometryType('the_geom') = 'Polygon' OR geometryType('the_geom') = 'MultiPolygon')
                  	AND
                  	kind IN ('pier', 'breakwater', 'groyne')
                  )
                )
                """);

        List<ReferencedEnvelope> bounds = getBounds(filter, crs);
        assertThat(bounds, Matchers.containsInAnyOrder(envelope(-10, 10, -10, 10)));
    }
}
