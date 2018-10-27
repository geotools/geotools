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
package org.geotools.geopkg;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.sql.SQLException;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.util.ScreenMap;
import org.geotools.jdbc.JDBCFeatureStoreOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.util.factory.Hints;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

public class GeoPkgFeatureStoreOnlineTest extends JDBCFeatureStoreOnlineTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new GeoPkgTestSetup();
    }

    @Override
    public void testAddNullAttributes() throws IOException {
        // JD: as far as I can tell you can't have null geometries
        // in GeoPkg...
    }

    @Override
    public void testModifyNullAttributes() throws IOException {}

    @Override
    public void testAddInTransaction() throws IOException {
        // does not work, see GEOT-2832
    }

    @Override
    public void testExternalConnection() throws IOException, SQLException {
        // SQLite locking does not allow one connection to write while another one reads on the
        // same table
    }

    public void testScreenMapSkip() throws Exception {
        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("ft1"));
        assertTrue(fs.getSupportedHints().contains(Hints.SCREENMAP));

        Query q = new Query(tname("ft1"));
        q.setPropertyNames(new String[] {"stringProperty", "geometry"});
        FilterFactory ff = dataStore.getFilterFactory();
        q.setSortBy(new SortBy[] {ff.sort("doubleProperty", SortOrder.ASCENDING)});

        // setup the screenmap so that we have a feature that gets skipped due to screenmap
        // but the next one is not
        ScreenMap screenMap =
                new ScreenMap(
                        -1,
                        -1,
                        10,
                        10,
                        new AffineTransform2D(AffineTransform.getScaleInstance(0.5, 0.5)));
        screenMap.setSpans(1, 1);
        Hints hints = new Hints(Hints.SCREENMAP, screenMap);
        q.setHints(hints);

        try (SimpleFeatureIterator it = fs.getFeatures(q).features()) {
            while (it.hasNext()) {
                // System.out.println(it.next());
                it.next();
            }
        }

        // check the screenmap has been updated
        assertTrue(screenMap.get(0, 0));
    }
}
