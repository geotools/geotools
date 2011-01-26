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

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Checks the datastore can work against unknown columns
 * @author Andrea Aime - OpenGeo
 *
 * @source $URL$
 */
public abstract class JDBCSkipColumnTest extends JDBCTestSupport {

    protected static final String SKIPCOLUMN = "skipcolumn";
    protected static final String ID = "id";
    protected static final String NAME = "name";
    protected static final String GEOM = "geom";
    
    protected SimpleFeatureType schema;

    protected abstract JDBCSkipColumnTestSetup createTestSetup();
    
    @Override
    protected void connect() throws Exception {
        super.connect();
        schema = DataUtilities.createType(dataStore.getNamespaceURI() + "." + SKIPCOLUMN, ID + ":0," + GEOM + ":Point," + NAME + ":String");
    }

    public void testSkippedColumn() throws Exception {
        SimpleFeatureType ft =  dataStore.getSchema(tname(SKIPCOLUMN));
        assertFeatureTypesEqual(schema, ft);
    }
    
    public void testReadFeatures() throws Exception {
    	SimpleFeatureCollection fc = dataStore.getFeatureSource(tname(SKIPCOLUMN)).getFeatures();
        assertEquals(1, fc.size());
        SimpleFeatureIterator fr = fc.features();
        assertTrue(fr.hasNext());
        SimpleFeature f = fr.next();
        assertFalse(fr.hasNext());
        fr.close();
    }
    
    public void testGetBounds() throws Exception {
        ReferencedEnvelope env = dataStore.getFeatureSource(tname(SKIPCOLUMN)).getBounds();
        assertEquals(0.0, env.getMinX());
        assertEquals(0.0, env.getMinY());
        assertEquals(0.0, env.getMaxX());
        assertEquals(0.0, env.getMaxY());
    }
}
