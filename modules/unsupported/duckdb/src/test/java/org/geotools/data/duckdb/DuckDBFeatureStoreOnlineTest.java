/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import static org.junit.Assert.assertTrue;

import java.awt.geom.AffineTransform;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.sort.SortOrder;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.util.ScreenMap;
import org.geotools.jdbc.JDBCTestSupport;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.util.factory.Hints;
import org.junit.Test;

public class DuckDBFeatureStoreOnlineTest extends JDBCTestSupport {

    @Override
    protected DuckDBTestSetup createTestSetup() {
        return new DuckDBTestSetup();
    }

    @Test
    public void testScreenMapSkip() throws Exception {
        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("ft1"));
        assertTrue(fs.getSupportedHints().contains(Hints.SCREENMAP));

        Query q = new Query(tname("ft1"));
        q.setPropertyNames("stringProperty", "geometry");
        FilterFactory ff = dataStore.getFilterFactory();
        q.setSortBy(ff.sort("doubleProperty", SortOrder.ASCENDING));

        ScreenMap screenMap =
                new ScreenMap(-1, -1, 10, 10, new AffineTransform2D(AffineTransform.getScaleInstance(0.5, 0.5)));
        screenMap.setSpans(1, 1);
        q.setHints(new Hints(Hints.SCREENMAP, screenMap));

        try (SimpleFeatureIterator it = fs.getFeatures(q).features()) {
            while (it.hasNext()) {
                it.next();
            }
        }

        assertTrue(screenMap.get(0, 0));
    }
}
