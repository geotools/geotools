/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana;

import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.spatial.BBOX;

/** @author Stefan Uhrig, SAP SE */
public class HanaSpatialFilterOnViewOnlineTest extends JDBCTestSupport {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new HanaSpatialFilterOnViewTestSetup();
    }

    public void testFilterOnView() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        BBOX bbox = ff.bbox(aname("geom"), 0, 0, 4, 4, "EPSG:4326");
        ContentFeatureCollection features =
                dataStore.getFeatureSource(tname("viewoftab")).getFeatures(bbox);
        assertEquals(1, features.size());
    }
}
