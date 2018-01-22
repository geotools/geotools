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
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;

public class EpaVicDataStoreIT {

  public static String TYPENAME1 = "measurement";

  private EpaVicDatastore dataStore;

  private Query q;

  @Before
  public void setUp() throws Exception {
    q = new Query("measurement",
        ECQL.toFilter("MonitorId='PM10' AND TimeBasisId='24HR_AV' "
            + "AND FromDate='2009-02-07T00:00:00' AND ToDate='2009-02-07T00:00:00'"));
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetCount() throws Exception {

    EpaVicDatastore ds = EpaVicDataStoreFactoryTest
        .createDefaultEPAServerTestDataStore();
    ContentFeatureSource featureSource = ds.getFeatureSource("measurement");
    int count = featureSource.getCount(q);

    assertTrue(count > 0);

    SimpleFeatureIterator it = featureSource.getFeatures(q).features();

    assertTrue(it.hasNext());
    SimpleFeature feat = it.next();

    String attribute = (String) feat
        .getAttribute(MeasurementFields.SITE_LIST_NAME.getFieldName());

    assertEquals("EAST", attribute);
  }

  @Test
  public void testGetSites() throws Exception {

    EpaVicDatastore ds = EpaVicDataStoreFactoryTest
        .createDefaultEPAServerTestDataStore();
    Sites sites = ds.retrieveSitesJSON();
    assertEquals(29, sites.getSites().size());
  }
}
