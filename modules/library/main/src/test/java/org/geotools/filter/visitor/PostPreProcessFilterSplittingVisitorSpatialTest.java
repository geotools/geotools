/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.visitor;

import org.geotools.api.filter.Filter;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.Beyond;
import org.geotools.api.filter.spatial.Contains;
import org.geotools.api.filter.spatial.Crosses;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.api.filter.spatial.Disjoint;
import org.geotools.api.filter.spatial.Equals;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.filter.spatial.Overlaps;
import org.geotools.api.filter.spatial.Touches;
import org.geotools.api.filter.spatial.Within;
import org.geotools.filter.FilterCapabilities;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

public class PostPreProcessFilterSplittingVisitorSpatialTest
        extends AbstractPostPreProcessFilterSplittingVisitorTests {

    Geometry geom = new GeometryFactory().createPoint();

    @Test
    public void testBBOX() throws Exception {
        // JE:  this test fails but I am not sure if it is a bug or expected behaviour
        // I wrote this test so it may be correct but it maybe wrong.  Someone that knows should
        // look at this.
        Filter f = ff.bbox(geomAtt, 10, 10, 20, 20, "");
        runTest(f, new FilterCapabilities(BBOX.class), geomAtt);
    }

    @Test
    public void testBEYOND() throws Exception {
        Filter f = ff.beyond(geomAtt, geom, 10, "");
        runTest(f, new FilterCapabilities(Beyond.class), geomAtt);
    }

    @Test
    public void testCONTAINS() throws Exception {
        Filter f = ff.contains(geomAtt, geom);
        runTest(f, new FilterCapabilities(Contains.class), geomAtt);
    }

    @Test
    public void testCROSSES() throws Exception {
        Filter f = ff.crosses(geomAtt, geom);
        runTest(f, new FilterCapabilities(Crosses.class), geomAtt);
    }

    @Test
    public void testDISJOINT() throws Exception {
        Filter f = ff.disjoint(geomAtt, geom);
        runTest(f, new FilterCapabilities(Disjoint.class), geomAtt);
    }

    @Test
    public void testDWITHIN() throws Exception {
        Filter f = ff.dwithin(geomAtt, geom, 10, "");
        runTest(f, new FilterCapabilities(DWithin.class), geomAtt);
    }

    @Test
    public void testEQUALS() throws Exception {
        Filter f = ff.equals(geomAtt, geom);
        runTest(f, new FilterCapabilities(Equals.class), geomAtt);
    }

    @Test
    public void testINTERSECTS() throws Exception {
        Filter f = ff.intersects(geomAtt, geom);
        runTest(f, new FilterCapabilities(Intersects.class), geomAtt);
    }

    @Test
    public void testOVERLAPS() throws Exception {
        Filter f = ff.overlaps(geomAtt, geom);
        runTest(f, new FilterCapabilities(Overlaps.class), geomAtt);
    }

    @Test
    public void testTOUCHES() throws Exception {
        Filter f = ff.touches(geomAtt, geom);
        runTest(f, new FilterCapabilities(Touches.class), geomAtt);
    }

    @Test
    public void testWITHIN() throws Exception {
        Filter f = ff.within(geomAtt, geom);
        runTest(f, new FilterCapabilities(Within.class), geomAtt);
    }
}
