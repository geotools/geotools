/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.test.TestData;
import org.geotools.util.URLs;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;

/** @author ian */
public class DottedNamesTest {

    private DataStore gpkg;

    /** @throws java.lang.Exception */
    @Before
    public void setUp() throws Exception {
        URL url = TestData.url(this.getClass(), "mosselzaad.gpkg");
        // open the geopkg
        HashMap<String, Object> params = new HashMap<>();
        params.put(GeoPkgDataStoreFactory.DBTYPE.key, GeoPkgDataStoreFactory.DBTYPE.sample);
        params.put(GeoPkgDataStoreFactory.DATABASE.key, URLs.urlToFile(url));

        gpkg = DataStoreFinder.getDataStore(params);
        assertNotNull(gpkg);
    }

    /**
     * Test for GEOT-5852 - SQL exception when querying Geopackage table containing a dot in the
     * name
     */
    @Test
    public void testGetContents() throws IOException, CQLException {
        String[] typeNamesArr = gpkg.getTypeNames();
        assertNotNull("no types found", typeNamesArr);
        List<String> typeNames = Arrays.asList(typeNamesArr);
        assertFalse("no types found", typeNames.isEmpty());
        String name = typeNamesArr[0];
        SimpleFeatureSource fs = gpkg.getFeatureSource(name);
        assertNotNull(fs);

        SimpleFeatureCollection features =
                fs.getFeatures(ECQL.toFilter("BBOX(geom,100000,500000,200000,600000)"));
        try (SimpleFeatureIterator itr = features.features()) {
            while (itr.hasNext()) {
                SimpleFeature f = itr.next();
                assertNotNull(f);
            }
        }
    }
}
