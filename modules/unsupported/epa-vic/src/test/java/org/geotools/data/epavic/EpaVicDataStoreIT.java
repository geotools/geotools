/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.epavic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.geotools.data.Query;
import org.geotools.data.epavic.schema.MeasurementFields;
import org.geotools.data.epavic.schema.Sites;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.filter.text.ecql.ECQL;
import org.junit.After;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;

public class EpaVicDataStoreIT {

    public static String TYPENAME1 = "measurement";

    private EpaVicDatastore dataStore;

    @After
    public void tearDown() throws Exception {}

    @Test
    public void testGetMeasurementBBOX() throws Exception {

        Query q =
                new Query(
                        "measurement",
                        ECQL.toFilter(
                                "BBOX(geometry, -43, 96, -9, 160) AND MonitorId='CO' AND TimeBaseId='24HR_AV' "
                                        + "AND DateTimeRecorded BETWEEN '2018-03-21T10:00:00' AND '2019-03-23T10:00:00'"));
        EpaVicDatastore ds = EpaVicDataStoreFactoryTest.createDefaultEPAServerTestDataStore();
        ContentFeatureSource featureSource = ds.getFeatureSource("measurement");
        int count = featureSource.getCount(q);

        assertTrue(count > 3000);

        SimpleFeatureIterator it = featureSource.getFeatures(q).features();

        assertTrue(it.hasNext());
        SimpleFeature feat = it.next();
        assertEquals(
                "EAST",
                (String) feat.getAttribute(MeasurementFields.SITE_LIST_NAME.getFieldName()));
        assertEquals(
                "Beta attenuation monitoring",
                (String) feat.getAttribute(MeasurementFields.EQUIPMENT_TYPE.getFieldName()));

        int count2 = 1;
        while (it.hasNext()) {
            feat = it.next();
            count2++;
        }

        assertEquals(count, count2);
    }

    @Test
    public void testGetSites() throws Exception {

        EpaVicDatastore ds = EpaVicDataStoreFactoryTest.createDefaultEPAServerTestDataStore();
        Sites sites = ds.retrieveSitesJSON();
        assertTrue(sites.getSites().size() > 30);
    }
}
