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
package org.geotools.data.wfs.online.v1_1;

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
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.identity.FeatureId;

/** @source $URL$ */
public class GeoServerOnlineTest extends AbstractWfsDataStoreOnlineTest {

    public static final String SERVER_URL =
            "http://localhost:8080/geoserver/wfs?service=WFS&request=GetCapabilities&version=1.1.0"; // $NON-NLS-1$

    public GeoServerOnlineTest() {
        super(
                SERVER_URL,
                GEOS_STATES_11,
                "the_geom",
                MultiPolygon.class,
                49,
                ff.id(Collections.singleton(ff.featureId("states.1"))),
                createSpatialFilter(),
                WFSDataStoreFactory.AXIS_ORDER_NORTH_EAST);
    }

    public static Filter createSpatialFilter() {
        GeometryFactory gf = new GeometryFactory();
        Coordinate[] coordinates = {
            new Coordinate(39, -107),
            new Coordinate(38, -107),
            new Coordinate(38, -104),
            new Coordinate(39, -104),
            new Coordinate(39, -107)
        };
        LinearRing shell = gf.createLinearRing(coordinates);
        Polygon polygon = gf.createPolygon(shell, null);
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        return ff.intersects(ff.property("the_geom"), ff.literal(polygon));
    }

    /**
     * Tests case where filter is makes use of 2 different attributes but Query object only requests
     * 1 of the two attributes. This is a fix for a bug that has occurred.
     */
    @Test
    public void testFeatureReaderWithQueryFilter() throws Exception {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }

        Filter filter = ff.equals(ff.property("NAME"), ff.literal("E 58th St"));

        Query query = new Query("tiger_tiger_roads", filter);
        FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                wfs.getFeatureReader(query, new DefaultTransaction());
        int expected = 0;
        while (reader.hasNext()) {
            expected++;
            reader.next();
        }
        query = new Query("tiger_tiger_roads", filter, 100, new String[] {"CFCC"}, "");
        reader = wfs.getFeatureReader(query, new DefaultTransaction());
        int count = 0;
        while (reader.hasNext()) {
            count++;
            reader.next();
        }

        assertEquals(expected, count);
    }

    public static final String ATTRIBUTE_TO_EDIT = "STATE_FIPS";

    public static final String NEW_EDIT_VALUE = "newN";

    private static final int EPSG_CODE = 4326;

    /**
     * Writing test that only engages against a remote geoserver.
     *
     * <p>Makes reference to the standard featureTypes that geoserver ships with. NOTE: Ignoring
     * this test for now because it edits topp:states and GeoServer doesn't return the correct
     * Feature IDs on transactions against shapefiles
     */
    @Test
    @Ignore
    public void testWrite() throws Exception {

        SimpleFeatureType ft = wfs.getSchema(testType.FEATURETYPENAME);
        SimpleFeatureSource fs = wfs.getFeatureSource(testType.FEATURETYPENAME);

        Id startingFeatures = createFidFilter(fs);
        FilterFactory2 filterFac =
                CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
        try {
            GeometryFactory gf = new GeometryFactory();
            MultiPolygon mp =
                    gf.createMultiPolygon(
                            new Polygon[] {
                                gf.createPolygon(
                                        gf.createLinearRing(
                                                new Coordinate[] {
                                                    new Coordinate(-88.071564, 37.51099),
                                                    new Coordinate(-88.467644, 37.400757),
                                                    new Coordinate(-90.638329, 42.509361),
                                                    new Coordinate(-89.834618, 42.50346),
                                                    new Coordinate(-88.071564, 37.51099)
                                                }),
                                        new LinearRing[] {})
                            });
            mp.setUserData("http://www.opengis.net/gml/srs/epsg.xml#" + EPSG_CODE);

            PropertyName geometryAttributeExpression =
                    filterFac.property(ft.getGeometryDescriptor().getLocalName());
            PropertyIsNull geomNullCheck = filterFac.isNull(geometryAttributeExpression);
            Query query =
                    new Query(
                            testType.FEATURETYPENAME,
                            filterFac.not(geomNullCheck),
                            1,
                            Query.ALL_NAMES,
                            null);
            SimpleFeatureIterator inStore = fs.getFeatures(query).features();

            SimpleFeature f, f2;
            try {
                SimpleFeature feature = inStore.next();

                SimpleFeature copy = SimpleFeatureBuilder.deep(feature);
                SimpleFeature copy2 = SimpleFeatureBuilder.deep(feature);

                f = SimpleFeatureBuilder.build(ft, copy.getAttributes(), null);
                f2 = SimpleFeatureBuilder.build(ft, copy2.getAttributes(), null);
                assertFalse("Max Feature failed", inStore.hasNext());
            } finally {
                inStore.close();
            }

            org.geotools.util.logging.Logging.getLogger("org.geotools.data.wfs")
                    .setLevel(Level.FINE);
            SimpleFeatureCollection inserts = DataUtilities.collection(new SimpleFeature[] {f, f2});
            Id fp = WFSOnlineTestSupport.doInsert(wfs, ft, inserts);

            // / okay now count ...
            FeatureReader<SimpleFeatureType, SimpleFeature> count =
                    wfs.getFeatureReader(new Query(ft.getTypeName()), Transaction.AUTO_COMMIT);
            int i = 0;
            while (count.hasNext() && i < 3) {
                f = count.next();
                i++;
            }
            count.close();
            WFSOnlineTestSupport.doDelete(wfs, ft, fp);
            WFSOnlineTestSupport.doUpdate(wfs, ft, ATTRIBUTE_TO_EDIT, NEW_EDIT_VALUE);
        } finally {
            try {
                ((SimpleFeatureStore) fs).removeFeatures(filterFac.not(startingFeatures));
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private Id createFidFilter(SimpleFeatureSource fs) throws IOException {
        SimpleFeatureIterator iter = fs.getFeatures().features();
        FilterFactory2 ffac = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
        Set fids = new HashSet();
        try {
            while (iter.hasNext()) {
                String id = iter.next().getID();
                FeatureId fid = ffac.featureId(id);
                fids.add(fid);
            }
            Id filter = ffac.id(fids);
            return filter;
        } finally {
            iter.close();
        }
    }
}
