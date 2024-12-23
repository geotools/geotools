/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.text.cql_2.conformance;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.geootols.filter.text.cql_2.CQL2;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.api.data.Query;
import org.geotools.api.filter.Filter;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.http.HTTPResponse;
import org.geotools.http.SimpleHttpClient;
import org.geotools.jdbc.JDBCFeatureSource;
import org.junit.Test;

/**
 * Base class for tests issued from the Abstract Test Suite (ATS). See <a
 * href="https://docs.ogc.org/is/21-065r2/21-065r2.html#ats">https://docs.ogc.org/is/21-065r2/21-065r2.html#ats</a> for
 * the context.
 *
 * <p>This class will download the official Natural Earth dataset and store it into the default temporary directory.
 */
public abstract class ATSOnlineTest {

    private static String NE_DATA_URL =
            "https://github.com/opengeospatial/ogcapi-features/raw/refs/heads/master/cql2/standard/data/ne110m4cql2.gpkg";

    protected final File neGpkg =
            Path.of(System.getProperty("java.io.tmpdir"), "ne.gpkg").toFile();

    protected final String dataset;
    protected final Filter filter;
    protected final int expectedFeatures;

    protected ATSOnlineTest(String dataset, String criteria, int expectedFeatures) throws CQLException {
        this.dataset = dataset;
        this.filter = criteriaToFilter(criteria);
        this.expectedFeatures = expectedFeatures;
    }

    protected Filter criteriaToFilter(String criteria) throws CQLException {
        return CQL2.toFilter(criteria);
    }

    protected ATSOnlineTest(String dataset, Filter filter, int expectedFeatures) {
        this.dataset = dataset;
        this.filter = filter;
        this.expectedFeatures = expectedFeatures;
    }

    private void downloadNaturalEarthData() throws IOException {
        if (neGpkg.exists()) {
            return;
        }
        neGpkg.createNewFile();
        SimpleHttpClient client = new SimpleHttpClient();
        HTTPResponse r = client.get(new URL(NE_DATA_URL));
        FileUtils.copyInputStreamToFile(r.getResponseStream(), neGpkg);
    }

    protected DataStore naturalEarthData() throws IOException {
        downloadNaturalEarthData();
        Map params = new HashMap();
        params.put("dbtype", "geopkg");
        params.put("database", neGpkg.getAbsolutePath());
        params.put("read-only", true);

        DataStore datastore = DataStoreFinder.getDataStore(params);

        return datastore;
    }

    protected int featuresReturned(DataStore ds) throws CQLException, IOException {
        Query q = new Query(this.dataset);
        q.getHints().put(JDBCFeatureSource.FILTER_THREE_WAY_LOGIC, Boolean.TRUE);
        q.setFilter(filter);
        return ds.getFeatureSource(this.dataset).getFeatures(q).size();
    }

    @Test
    public void testConformance() throws CQLException, IOException {
        DataStore ds = naturalEarthData();
        int feat = featuresReturned(ds);
        ds.dispose();

        assertEquals(this.filter.toString(), this.expectedFeatures, feat);
    }
}
