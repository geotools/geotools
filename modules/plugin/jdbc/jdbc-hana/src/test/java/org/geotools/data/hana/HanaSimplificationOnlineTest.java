/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2022, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeTrue;

import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.geotools.util.factory.Hints;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;

/** @author Stefan Uhrig, SAP SE */
public class HanaSimplificationOnlineTest extends JDBCTestSupport {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new HanaSimplificationTestSetup(new HanaTestSetupPSPooling());
    }

    @Test
    public void testRoundEarthSimplification() throws Exception {
        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("roundEarth"));

        assumeTrue(fs.getSupportedHints().contains(Hints.GEOMETRY_SIMPLIFICATION));

        Query query = new Query();
        Hints hints = new Hints(Hints.GEOMETRY_SIMPLIFICATION, 0.5);
        query.setHints(hints);

        SimpleFeatureCollection coll = fs.getFeatures(query);
        Geometry geom = null;
        try (SimpleFeatureIterator iter = coll.features()) {
            if (iter.hasNext()) {
                geom = (Geometry) iter.next().getDefaultGeometry();
            }
        }
        assertNotNull(geom);
    }

    @Test
    public void testPlanarSimplification() throws Exception {
        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("planar"));

        assumeTrue(fs.getSupportedHints().contains(Hints.GEOMETRY_SIMPLIFICATION));

        Query query = new Query();
        Hints hints = new Hints(Hints.GEOMETRY_SIMPLIFICATION, 0.5);
        query.setHints(hints);

        SimpleFeatureCollection coll = fs.getFeatures(query);
        Geometry geom = null;
        try (SimpleFeatureIterator iter = coll.features()) {
            if (iter.hasNext()) {
                geom = (Geometry) iter.next().getDefaultGeometry();
            }
        }
        assertEquals(2, geom.getNumPoints());
    }
}
