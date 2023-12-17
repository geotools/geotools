/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, David Zwiers
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
package org.geotools.data.wfs.online.v1_0;

import static org.geotools.data.wfs.WFSTestData.GEOS_STATES_11;

import java.util.Collections;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.data.wfs.online.AbstractWfsDataStoreOnlineTest;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

public class GeoServerOnlineTest extends AbstractWfsDataStoreOnlineTest {

    public static final String SERVER_URL =
            "http://localhost:8080/geoserver/wfs?service=WFS&request=GetCapabilities&version=1.0.0"; // $NON-NLS-1$

    public GeoServerOnlineTest() {
        super(
                SERVER_URL,
                GEOS_STATES_11,
                "the_geom",
                MultiPolygon.class,
                -1,
                ff.id(Collections.singleton(ff.featureId("states.1"))),
                createSpatialFilter(),
                WFSDataStoreFactory.AXIS_ORDER_EAST_NORTH);
    }

    public static Filter createSpatialFilter() {
        GeometryFactory gf = new GeometryFactory();
        Coordinate[] coordinates = {
            new Coordinate(-107, 39),
            new Coordinate(-107, 38),
            new Coordinate(-104, 38),
            new Coordinate(-104, 39),
            new Coordinate(-107, 39)
        };
        LinearRing shell = gf.createLinearRing(coordinates);
        Polygon polygon = gf.createPolygon(shell, null);
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        return ff.intersects(ff.property("the_geom"), ff.literal(polygon));
    }

    @Test
    @Override
    public void testDataStoreSupportsPlainBBOXInterface() throws Exception {
        super.testDataStoreSupportsPlainBBOXInterface();
    }
}
