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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.data.wfs.online.AbstractWfsDataStoreOnlineTest;
import org.geotools.data.wfs.online.WFSOnlineTestSupport;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.identity.FeatureId;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * 
 * 
 * @source $URL$
 */
public class GeoServerOnlineTest extends AbstractWfsDataStoreOnlineTest {

    public static final String SERVER_URL = "http://localhost:9090/geoserver/wfs?service=WFS&request=GetCapabilities&version=2.0.0";

    public GeoServerOnlineTest() {
        super(SERVER_URL, GEOS_STATES_11, "the_geom", MultiPolygon.class, -1, ff.id(Collections
                .singleton(ff.featureId("states.1"))), createSpatialFilter(), WFSDataStoreFactory.AXIS_ORDER_EAST_NORTH);
    }
    
    public static Filter createSpatialFilter() {
        GeometryFactory gf = new GeometryFactory(); 
        Coordinate[] coordinates = { new Coordinate(-107, 39), new Coordinate(-107, 38),
                new Coordinate(-104, 38), new Coordinate(-104, 39), new Coordinate(-107, 39) };
        LinearRing shell = gf.createLinearRing(coordinates);
        Polygon polygon = gf.createPolygon(shell, null);
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        return ff.intersects(ff.property("the_geom"), ff.literal(polygon));
    }   
    
    @Override
    public void testDataStoreHandlesAxisFlipping() {
        //disabled, not implemented for 2.0.0
    }

}
