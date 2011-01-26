/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.oracle;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCSpatialFiltersTest;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.spatial.BBOX;

public class OracleSpatialFiltersTest extends JDBCSpatialFiltersTest {

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new OracleDataStoreAPITestSetup(new OracleTestSetup());
    }

    public void testLooseBboxFilter() throws Exception {
        ((OracleDialect) dataStore.getSQLDialect()).setLooseBBOXEnabled(true);
        
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        // should match only "r2"
        BBOX bbox = ff.bbox(aname("geom"), 2, 3.5, 4, 4.5, "EPSG:4326");
        FeatureCollection features = dataStore.getFeatureSource(tname("road")).getFeatures(bbox);
        checkSingleResult(features, "r2");
    }
}
