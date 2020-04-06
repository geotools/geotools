/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;

public abstract class JDBCNoPrimaryKeyOnlineTest extends JDBCTestSupport {

    protected static final String LAKE = "lake";
    protected static final String ID = "id";
    protected static final String NAME = "name";
    protected static final String GEOM = "geom";

    protected FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    protected SimpleFeatureType lakeSchema;

    @Override
    protected abstract JDBCNoPrimaryKeyTestSetup createTestSetup();

    @Override
    protected void connect() throws Exception {
        super.connect();
        lakeSchema =
                DataUtilities.createType(
                        dataStore.getNamespaceURI() + "." + LAKE,
                        ID + ":0," + GEOM + ":Polygon," + NAME + ":String");
    }

    public void testSchema() throws Exception {
        SimpleFeatureType ft = dataStore.getSchema(tname(LAKE));
        assertFeatureTypesEqual(lakeSchema, ft);
    }

    public void testReadFeatures() throws Exception {
        SimpleFeatureCollection fc = dataStore.getFeatureSource(tname(LAKE)).getFeatures();
        assertEquals(1, fc.size());
        try (SimpleFeatureIterator fr = fc.features()) {
            assertTrue(fr.hasNext());
            SimpleFeature f = fr.next();
            assertFalse(fr.hasNext());
        }
    }

    public void testGetBounds() throws Exception {
        // GEOT-2067 Make sure it's possible to compute bounds out of a view
        ReferencedEnvelope reference = dataStore.getFeatureSource(tname(LAKE)).getBounds();
        assertEquals(12.0, reference.getMinX());
        assertEquals(16.0, reference.getMaxX());
        assertEquals(4.0, reference.getMinY());
        assertEquals(8.0, reference.getMaxY());
    }

    /**
     * Subclasses may want to override this in case the database has a native way, other than the
     * pk, to identify a row
     */
    public void testReadOnly() throws Exception {
        try {
            dataStore.getFeatureWriter(tname(LAKE), Transaction.AUTO_COMMIT);
            fail("Should not be able to pick a writer without a pk");
        } catch (Exception e) {
            // ok, fine
        }

        assertFalse(dataStore.getFeatureSource(tname(LAKE)) instanceof FeatureStore);
    }
}
