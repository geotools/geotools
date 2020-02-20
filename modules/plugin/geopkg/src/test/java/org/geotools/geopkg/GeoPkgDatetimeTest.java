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

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.util.NullProgressListener;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.test.TestData;
import org.geotools.util.URLs;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

public class GeoPkgDatetimeTest {
    private DataStore gpkg;

    /** @throws java.lang.Exception */
    @Before
    public void setUp() throws Exception {
        URL url = TestData.url(this.getClass(), "datetime_date.gpkg");
        // open the geopkg
        HashMap<String, Object> params = new HashMap<>();
        params.put(GeoPkgDataStoreFactory.DBTYPE.key, GeoPkgDataStoreFactory.DBTYPE.sample);
        params.put(GeoPkgDataStoreFactory.DATABASE.key, URLs.urlToFile(url));

        gpkg = DataStoreFinder.getDataStore(params);
        assertNotNull(gpkg);
    }

    /**
     * Test for GEOS-9392 - recognition of DATETIME and DATE.
     *
     * @throws IOException
     */
    @Test
    public void testGetContents() throws IOException {
        String[] typeNamesArr = gpkg.getTypeNames();
        assertNotNull("no types found", typeNamesArr);
        List<String> typeNames = Arrays.asList(typeNamesArr);
        assertFalse("no types found", typeNames.isEmpty());
        String name = typeNamesArr[0];
        SimpleFeatureSource fs = gpkg.getFeatureSource(name);
        assertNotNull(fs);

        SimpleFeatureType schema = fs.getSchema();
        assertEquals(Timestamp.class, schema.getDescriptor("datetime").getType().getBinding());
        assertEquals(Date.class, schema.getDescriptor("date").getType().getBinding());

        SimpleFeatureCollection features = fs.getFeatures();
        try (SimpleFeatureIterator itr = features.features()) {
            while (itr.hasNext()) {
                SimpleFeature f = itr.next();
                assertNotNull(f);

                Timestamp datetime = (Timestamp) f.getAttribute("datetime");
                Date date = (Date) f.getAttribute("date");
            }
        }
    }

    /**
     * Test avoidance of aggregate UniqueVisitor optimization (as they do not respect dialect type
     * mapping for Date and Timestamp at this time.
     *
     * @throws IOException
     */
    @Test
    public void testUnique() throws IOException {
        UniqueVisitor highlander = new UniqueVisitor("date");
        SimpleFeatureSource fs = gpkg.getFeatureSource(gpkg.getTypeNames()[0]);

        SimpleFeatureCollection features = fs.getFeatures();
        features.accepts(highlander, new NullProgressListener());

        Set<Date> uniqueSet = highlander.getUnique();
        assertEquals(uniqueSet.size(), features.size());
    }

    /**
     * Test avoidance of aggregate between filter (as this needed some help to respect date and
     * timestamp)
     *
     * @throws IOException
     */
    @Test
    public void testBetween() throws IOException, CQLException {
        Filter between = ECQL.toFilter("date BETWEEN '2020-02-20' AND '2020-02-22'");

        SimpleFeatureSource fs = gpkg.getFeatureSource(gpkg.getTypeNames()[0]);
        SimpleFeatureCollection features = fs.getFeatures(between);
        assertEquals(3, features.size());
    }
}
