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
package org.geotools.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.Test;
import org.geotools.api.feature.simple.SimpleFeatureType;

/**
 * Checks the datastore can work against unknown columns
 *
 * @author Andrea Aime - OpenGeo
 */
public abstract class JDBCSkipColumnOnlineTest extends JDBCTestSupport {

    protected static final String SKIPCOLUMN = "skipcolumn";
    protected static final String ID = "id";
    protected static final String NAME = "name";
    protected static final String GEOM = "geom";

    protected SimpleFeatureType schema;

    @Override
    protected abstract JDBCSkipColumnTestSetup createTestSetup();

    @Override
    protected void connect() throws Exception {
        super.connect();
        schema =
                DataUtilities.createType(
                        dataStore.getNamespaceURI() + "." + SKIPCOLUMN,
                        ID + ":0," + GEOM + ":Point," + NAME + ":String");
    }

    @Test
    public void testSkippedColumn() throws Exception {
        SimpleFeatureType ft = dataStore.getSchema(tname(SKIPCOLUMN));
        assertFeatureTypesEqual(schema, ft);
    }

    @Test
    public void testReadFeatures() throws Exception {
        SimpleFeatureCollection fc = dataStore.getFeatureSource(tname(SKIPCOLUMN)).getFeatures();
        assertEquals(1, fc.size());
        try (SimpleFeatureIterator fr = fc.features()) {
            assertTrue(fr.hasNext());
            fr.next();
            assertFalse(fr.hasNext());
        }
    }

    @Test
    public void testGetBounds() throws Exception {
        ReferencedEnvelope env = dataStore.getFeatureSource(tname(SKIPCOLUMN)).getBounds();
        assertEquals(0.0, env.getMinX(), 0.0);
        assertEquals(0.0, env.getMinY(), 0.0);
        assertEquals(0.0, env.getMaxX(), 0.0);
        assertEquals(0.0, env.getMaxY(), 0.0);
    }
}
