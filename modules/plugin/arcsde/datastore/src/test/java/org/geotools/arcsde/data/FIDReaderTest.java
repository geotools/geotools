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
 *
 */
package org.geotools.arcsde.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.geotools.arcsde.session.ISession;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.esri.sde.sdk.client.SeLayer;
import com.esri.sde.sdk.client.SeRegistration;
import com.esri.sde.sdk.client.SeTable;

/**
 * @author Gabriel Roldan (TOPP)
 * 
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/plugin/arcsde/datastore/src/test/java/org
 *         /geotools/arcsde/data/FIDReaderTest.java $
 * @version $Id$
 * @since 2.5.x
 */
public class FIDReaderTest {

    private static TestData testData;

    private ISession session;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        testData = new TestData();
        testData.setUp();
        testData.createSimpleTestTables();
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        final boolean cleanTestTable = false;
        final boolean cleanPool = true;
        testData.tearDown(cleanTestTable, cleanPool);
        testData = null;
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        session = testData.getConnectionPool().getSession();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        session.dispose();
    }

    /**
     * Test method for
     * {@link org.geotools.arcsde.data.FIDReader#getFidReader(org.geotools.arcsde.session.ISession, com.esri.sde.sdk.client.SeTable, com.esri.sde.sdk.client.SeLayer, com.esri.sde.sdk.client.SeRegistration)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public void testGetFidReader() throws IOException {
        FIDReader fidReader;
        fidReader = getFidReader("GT_TEST_POINT_ROWID_SDE");
        assertNotNull(fidReader);
        assertTrue(fidReader instanceof FIDReader.SdeManagedFidReader);
        assertEquals(0, fidReader.getColumnIndex());
        assertEquals("ROW_ID", fidReader.getFidColumn());

        fidReader = getFidReader("GT_TEST_POINT_ROWID_USER");
        assertNotNull(fidReader);
        assertTrue(fidReader instanceof FIDReader.UserManagedFidReader);
        assertEquals(0, fidReader.getColumnIndex());
        assertEquals("ROW_ID", fidReader.getFidColumn());

        fidReader = getFidReader("GT_TEST_POINT_ROWID_NONE");
        assertNotNull(fidReader);
        assertTrue(fidReader instanceof FIDReader.ShapeFidReader);
        assertEquals(-1, fidReader.getColumnIndex());
        // use toUpperCase, case may be different depending on the backend rdbms
        assertEquals("GEOM.FID", fidReader.getFidColumn().toUpperCase());
    }

    private FIDReader getFidReader(String tableName) throws IOException {
        FIDReader fidReader;
        String dbName = session.getDatabaseName();
        tableName = ((dbName == null || "".equals(dbName)) ? "" : (dbName + "."))
                + session.getUser() + "." + tableName;
        tableName = tableName.toUpperCase();

        SeTable table = session.getTable(tableName);
        SeLayer layer = session.getLayer(tableName);
        SeRegistration reg = session.createSeRegistration(tableName);
        fidReader = FIDReader.getFidReader(session, table, layer, reg);
        return fidReader;
    }

    /**
     * Test method for {@link org.geotools.arcsde.data.FIDReader#getFidColumn()}.
     */
    @Test
    @Ignore
    public void testGetFidColumn() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.geotools.arcsde.data.FIDReader#setColumnIndex(int)}.
     */
    @Test
    @Ignore
    public void testSetColumnIndex() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.geotools.arcsde.data.FIDReader#getColumnIndex()}.
     */
    @Test
    @Ignore
    public void testGetColumnIndex() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link org.geotools.arcsde.data.FIDReader#readFid(org.geotools.arcsde.data.SdeRow)}.
     */
    @Test
    @Ignore
    public void testReadFid() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link org.geotools.arcsde.data.FIDReader#getPropertiesToFetch(org.opengis.feature.simple.SimpleFeatureType)}
     * .
     */
    @Test
    @Ignore
    public void testGetPropertiesToFetch() {
        fail("Not yet implemented");
    }

}
