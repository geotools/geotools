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
package org.geotools.mbtiles;

import static org.junit.Assert.assertThat;

import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.BBOX;
import org.opengis.referencing.FactoryException;

public class ExtractMultiBoundsFilterVisitorTest {
    private static final String CRS = "EPSG:404000";
    FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();

    @Test
    public void testBoundsSimple() throws FactoryException {
        BBOX bbox = FF.bbox(FF.property(""), -10, -10, 10, 10, CRS);
        List<Envelope> bounds = ExtractMultiBoundsFilterVisitor.getBounds(bbox);
        assertThat(bounds, Matchers.contains(new Envelope(-10, 10, -10, 10)));
    }

    @Test
    public void testBoundsSeparateOr() throws FactoryException {
        BBOX bbox1 = FF.bbox(FF.property(""), -10, -10, 10, 10, CRS);
        BBOX bbox2 = FF.bbox(FF.property(""), 50, -10, 60, 10, CRS);
        List<Envelope> bounds = ExtractMultiBoundsFilterVisitor.getBounds(FF.or(bbox1, bbox2));
        assertThat(
                bounds,
                Matchers.contains(new Envelope(-10, 10, -10, 10), new Envelope(50, 60, -10, 10)));
    }

    @Test
    public void testBoundsSeparateAnd() throws FactoryException {
        BBOX bbox1 = FF.bbox(FF.property(""), -10, -10, 10, 10, CRS);
        BBOX bbox2 = FF.bbox(FF.property(""), 50, -10, 60, 10, CRS);
        // and-ing disjoint, should be empty
        List<Envelope> bounds = ExtractMultiBoundsFilterVisitor.getBounds(FF.and(bbox1, bbox2));
        assertThat(bounds, Matchers.contains(new Envelope()));
    }

    @Test
    public void testBoundsSeparateOrIntersect() throws FactoryException {
        BBOX bbox1 = FF.bbox(FF.property(""), -10, -10, 10, 10, CRS);
        BBOX bbox2 = FF.bbox(FF.property(""), 50, -10, 70, 10, CRS);
        BBOX bbox3 = FF.bbox(FF.property(""), 0, -10, 60, 10, CRS);
        // two separate or-ed bounds, and-ed with one that overlaps them partially both
        List<Envelope> bounds =
                ExtractMultiBoundsFilterVisitor.getBounds(FF.and(bbox3, FF.or(bbox1, bbox2)));
        assertThat(
                bounds,
                Matchers.containsInAnyOrder(
                        new Envelope(0, 10, -10, 10), new Envelope(50, 60, -10, 10)));
    }

    /**
     * Same as {@link #testBoundsSeparateOrIntersect()}, but changing the order of the operands in a
     * way that the result should be the same
     */
    @Test
    public void testBoundsSeparateOrIntersectFlipped() throws FactoryException {
        BBOX bbox1 = FF.bbox(FF.property(""), -10, -10, 10, 10, CRS);
        BBOX bbox2 = FF.bbox(FF.property(""), 50, -10, 70, 10, CRS);
        BBOX bbox3 = FF.bbox(FF.property(""), 0, -10, 60, 10, CRS);
        // two separate or-ed bounds, and-ed with one that overlaps them partially both
        List<Envelope> bounds =
                ExtractMultiBoundsFilterVisitor.getBounds(FF.and(FF.or(bbox2, bbox1), bbox3));
        assertThat(
                bounds,
                Matchers.containsInAnyOrder(
                        new Envelope(0, 10, -10, 10), new Envelope(50, 60, -10, 10)));
    }

    @Test
    public void testBoundsMultiOrAnd() throws FactoryException {
        BBOX bbox1 = FF.bbox(FF.property(""), -10, -10, 10, 10, CRS);
        BBOX bbox2 = FF.bbox(FF.property(""), 50, -10, 70, 10, CRS);
        BBOX bbox3 = FF.bbox(FF.property(""), 0, -5, 5, 5, CRS);
        BBOX bbox4 = FF.bbox(FF.property(""), 45, 5, 55, 15, CRS);
        List<Envelope> bounds =
                ExtractMultiBoundsFilterVisitor.getBounds(
                        FF.and(FF.or(bbox2, bbox1), FF.or(bbox3, bbox4)));
        assertThat(
                bounds,
                Matchers.containsInAnyOrder(
                        new Envelope(0, 5, -5, 5), new Envelope(50, 55, 5, 10)));
    }
}
