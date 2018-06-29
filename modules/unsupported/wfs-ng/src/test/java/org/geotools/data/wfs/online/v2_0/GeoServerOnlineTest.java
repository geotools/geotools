/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.online.v2_0;

import static org.geotools.data.wfs.WFSTestData.GEOS_STATES_11;

import java.util.Collections;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.data.wfs.online.AbstractWfsDataStoreOnlineTest;
import org.geotools.factory.CommonFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

/** @source $URL$ */
public class GeoServerOnlineTest extends AbstractWfsDataStoreOnlineTest {

    public static final String SERVER_URL =
            "http://localhost:9090/geoserver/wfs?service=WFS&request=GetCapabilities&version=2.0.0";

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
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        return ff.intersects(ff.property("the_geom"), ff.literal(polygon));
    }

    @Override
    public void testDataStoreHandlesAxisFlipping() {
        // disabled, not implemented for 2.0.0
    }
}
