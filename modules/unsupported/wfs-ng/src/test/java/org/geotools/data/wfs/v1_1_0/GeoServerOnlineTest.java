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
package org.geotools.data.wfs.v1_1_0;

import static org.geotools.data.wfs.v1_1_0.DataTestSupport.GEOS_STATES;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collections;

import org.geotools.data.DefaultQuery;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

public class GeoServerOnlineTest extends AbstractWfsDataStoreOnlineTest {

    public static final String SERVER_URL = "http://sigma.openplans.org:8080/geoserver/wfs?service=WFS&request=GetCapabilities&version=1.1.0"; //$NON-NLS-1$

    public GeoServerOnlineTest() {
        super(SERVER_URL, GEOS_STATES, "the_geom", MultiPolygon.class, 49, ff.id(Collections
                .singleton(ff.featureId("states.1"))));
    }

    @Test
    public void testFeatureSourceGetFeaturesFilter() throws IOException {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }

        SimpleFeatureSource featureSource;
        featureSource = wfs.getFeatureSource(testType.FEATURETYPENAME);
        assertNotNull(featureSource);

        DefaultQuery query = new DefaultQuery(testType.FEATURETYPENAME);

        GeometryFactory gf = new GeometryFactory();
        //GEOT-2283: use lat/lon coordinate order, this is a wfs 1.1 instance
        Coordinate[] coordinates = { new Coordinate(39, -107), new Coordinate(38, -107),
                new Coordinate(38, -104), new Coordinate(39, -104), new Coordinate(39, -107) };
        LinearRing shell = gf.createLinearRing(coordinates);
        Polygon polygon = gf.createPolygon(shell, null);
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Filter filter = ff.intersects(ff.property(defaultGeometryName), ff.literal(polygon));
        // System.out.println(filter);
        query.setFilter(filter);

        SimpleFeatureCollection features;
        features = featureSource.getFeatures(query);
        assertNotNull(features);

        SimpleFeatureType schema = features.getSchema();
        assertNotNull(schema);

        SimpleFeatureIterator iterator = features.features();
        assertNotNull(iterator);
        try {
            assertTrue(iterator.hasNext());
            SimpleFeature next = iterator.next();
            assertNotNull(next);
            assertNotNull(next.getDefaultGeometry());
            assertFalse(iterator.hasNext());
        } finally {
            iterator.close();
        }
    }

}
