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
package org.geotools.data.hana;

import static org.junit.Assert.assertEquals;

import org.geotools.api.data.Query;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.impl.PackedCoordinateSequenceFactory;

/** @author Stefan Uhrig, SAP SE */
public class HanaSpatialFilterOnViewOnlineTest extends JDBCTestSupport {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new HanaSpatialFilterOnViewTestSetup();
    }

    @Test
    public void testFilterOnView() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        BBOX bbox = ff.bbox(aname("geom"), 0, 0, 4, 4, "EPSG:4326");
        ContentFeatureCollection features =
                dataStore.getFeatureSource(tname("viewoftab")).getFeatures(bbox);
        assertEquals(1, features.size());
    }

    @Test
    public void testCountWithFilterOnView() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        LinearRing shell = gf.createLinearRing(sf.create(new double[] {0, 0, 4, 0, 4, 4, 0, 4, 0, 0}, 2));
        Polygon polygon = gf.createPolygon(shell, null);
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Intersects intersects = ff.intersects(ff.property(aname("geom")), ff.literal(polygon));
        Query q = new Query(aname("geom"), intersects);
        int count = dataStore.getFeatureSource(tname("viewoftab")).getCount(q);
        assertEquals(1, count);
    }
}
