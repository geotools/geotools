/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.text.cql_2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.geootols.filter.text.cql_2.CQL2;
import org.geotools.filter.text.cql2.CQLException;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Polygon;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;
import org.opengis.referencing.FactoryException;

/**
 * Spatial comparsion operators have changed significantly:
 *
 * <ul>
 *   <li>They all have a different name, starting with S_
 *   <li>RELATE and BBOX are gone
 *   <li>ENVELOPE axis order is different
 * </ul>
 */
public class CQL2GeoOperationTest {

    @Test
    public void disjoint() throws CQLException {

        Filter resultFilter = CQL2.toFilter("S_DISJOINT(ATTR1, POINT(1 2))");

        assertTrue("Disjoint was expected", resultFilter instanceof Disjoint);
    }

    @Test
    public void Intersects() throws CQLException {

        Filter resultFilter = CQL2.toFilter("S_INTERSECTS(ATTR1, POINT(1 2))");

        assertTrue("Intersects was expected", resultFilter instanceof Intersects);

        resultFilter = CQL2.toFilter("S_INTERSECTS(GEOLOC, POINT(615358 312185))");

        assertTrue("Intersects was expected", resultFilter instanceof Intersects);
    }

    /** Invalid Geooperation Test */
    @Test(expected = CQLException.class)
    public void invalidGeoOperation() throws CQLException {
        CQL2.toFilter("S_INTERSECT(ATTR1, POINT(1 2))"); // should be "intersects"
    }

    @Test
    public void touches() throws CQLException {
        Filter resultFilter = CQL2.toFilter("S_TOUCHES(ATTR1, POINT(1 2))");
        assertTrue("Touches was expected", resultFilter instanceof Touches);
    }

    @Test
    public void crosses() throws CQLException {
        Filter resultFilter = CQL2.toFilter("S_CROSSES(ATTR1, POINT(1 2))");
        assertTrue("Crosses was expected", resultFilter instanceof Crosses);
    }

    @Test
    public void contains() throws CQLException {
        Filter resultFilter = CQL2.toFilter("S_CONTAINS(ATTR1, POINT(1 2))");
        assertTrue("Contains was expected", resultFilter instanceof Contains);
    }

    @Test
    public void overlaps() throws Exception {
        Filter resultFilter = CQL2.toFilter("S_OVERLAPS(ATTR1, POINT(1 2))");
        assertTrue("Overlaps was expected", resultFilter instanceof Overlaps);
    }

    @Test
    public void equals() throws CQLException {
        Filter resultFilter = CQL2.toFilter("S_EQUALS(ATTR1, POINT(1 2))");
        assertTrue("not an instance of Equals", resultFilter instanceof Equals);
    }

    @Test
    public void within() throws CQLException {
        Filter resultFilter = CQL2.toFilter("S_WITHIN(ATTR1, POLYGON((1 2, 1 10, 5 10, 1 2)) )");
        assertTrue("Within was expected", resultFilter instanceof Within);
    }

    /** RELATE operator is gone in CQL2 */
    @Test(expected = CQLException.class)
    public void relate() throws CQLException {
        CQL2.toFilter(
                "RELATE(the_geom, LINESTRING (-134.921387 58.687767, -135.303391 59"
                        + ".092838), T*****FF*)");
    }

    /** BBOX spatial operator is gone */
    @Test(expected = CQLException.class)
    public void bbox() throws CQLException, FactoryException {
        CQL2.toFilter("BBOX(ATTR1, 10.0,20.0,30.0,40.0)");
    }

    @Test
    public void IntersectsWithTwoLiteral() throws CQLException {
        Filter resultFilter =
                CQL2.toFilter("S_INTERSECTS(POLYGON((1 2, 2 2, 2 3, 1 2)), POINT(1 2))");
        assertTrue("Intersects was expected", resultFilter instanceof Intersects);
    }

    @Test
    public void functionAsFirstArgument() throws CQLException {
        Filter resultFilter = CQL2.toFilter("S_INTERSECTS(centroid(the_geom), POINT(1 2))");

        assertTrue("Intersects was expected", resultFilter instanceof Intersects);
    }

    @Test
    public void functionAsSecondArgument() throws CQLException {

        Filter resultFilter = CQL2.toFilter("S_INTERSECTS(the_geom, buffer(POINT(1 2),10))");
        assertTrue("Intersects was expected", resultFilter instanceof Intersects);

        resultFilter = CQL2.toFilter("S_INTERSECTS(the_geom, buffer(the_geom,10))");
        assertTrue("Intersects was expected", resultFilter instanceof Intersects);
    }

    @Test
    public void functionAsFirstAndSecondArgument() throws CQLException {

        Filter resultFilter =
                CQL2.toFilter("S_INTERSECTS(centroid(the_geom), buffer(POINT(1 2) ,10))");

        assertTrue("Intersects was expected", resultFilter instanceof Intersects);
    }

    /**
     * OGC API defines a separate request parameter for the filter SRS, no need to have EWKT here.
     */
    @Test(expected = CQLException.class)
    public void intersectsWithReferencedGeometry() throws CQLException, FactoryException {
        CQL2.toFilter("S_INTERSECTS(the_geom, SRID=4326;POINT(1 2))");
    }

    @Test
    public void testIntersectsEnvelope() throws CQLException {
        Filter filter = CQL2.toFilter("S_INTERSECTS(geom, ENVELOPE(10, 10, 40, 40))");

        Intersects intersects = (Intersects) filter;
        PropertyName pn = (PropertyName) intersects.getExpression1();
        assertEquals("geom", pn.getPropertyName());

        Polygon polygon = intersects.getExpression2().evaluate(null, Polygon.class);
        assertNotNull(polygon);
        assertTrue(polygon.isRectangle());
        assertEquals(new Envelope(10, 40, 10, 40), polygon.getEnvelopeInternal());
    }
}
