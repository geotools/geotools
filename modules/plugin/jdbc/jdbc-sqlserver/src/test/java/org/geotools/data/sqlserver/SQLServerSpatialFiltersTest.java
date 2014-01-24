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
package org.geotools.data.sqlserver;

import java.io.IOException;

import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCSpatialFiltersTest;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.DWithin;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * 
 *
 * @source $URL$
 */
public class SQLServerSpatialFiltersTest extends JDBCSpatialFiltersTest {

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new SQLServerSpatialFiltersTestSetup();
    }

    public void testPointDistance() throws IOException, ParseException {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        Geometry point = new WKTReader().read("POINT(180000 0)");
        DWithin filter = ff.dwithin(ff.property(aname("geom")), ff.literal(point), 15000, "m");
        
        ContentFeatureCollection fc = dataStore.getFeatureSource(tname("ppoint")).getFeatures(filter);
        assertEquals(1, fc.size());
    }
}
