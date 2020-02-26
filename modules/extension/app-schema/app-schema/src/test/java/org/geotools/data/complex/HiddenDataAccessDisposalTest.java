/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex;

import static org.geotools.data.complex.FeatureChainingTest.MAPPED_FEATURE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataAccessFinder;
import org.geotools.feature.NameImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.type.Name;

public class HiddenDataAccessDisposalTest extends AbstractHiddenDataAccessDisposalTest {

    static final Name EXPOSURE_COLOR = new NameImpl("exposureColor");

    private static final String schemaBase = "/test-data/";

    AppSchemaDataAccess mfDataAccess;

    /** Load all the data accesses. */
    @Before
    public void loadDataAccesses() throws Exception {
        /** Load mapped feature data access */
        Map dsParams = new HashMap();
        URL url = getClass().getResource(schemaBase + "MappedFeaturePropertyfile.xml");
        assertNotNull(url);

        dsParams.put("dbtype", "app-schema");
        dsParams.put("url", url.toExternalForm());
        mfDataAccess = (AppSchemaDataAccess) DataAccessFinder.getDataStore(dsParams);
        assertNotNull(mfDataAccess);
        assertNotNull(mfDataAccess.getSchema(MAPPED_FEATURE));
        assertFalse(mfDataAccess.hidden);

        /** Load geologic unit data access */
        loadGeologicUnit();

        // 2 accessible data accesses + 3 hidden data accesses = 5
        assertEquals(5, DataAccessRegistry.getInstance().registry.size());
    }

    @Test
    public void testDisposeGeologicUnitDataAccess() {
        guDataAccess.dispose();

        // all hidden data accesses should have been automatically disposed
        assertEquals(1, DataAccessRegistry.getInstance().registry.size());
    }

    @Test
    public void testDisposeMappedFeatureDataAccess() {
        mfDataAccess.dispose();

        // no automatic disposal should have occurred
        assertEquals(4, DataAccessRegistry.getInstance().registry.size());

        guDataAccess.dispose();

        // all hidden data accesses should have been automatically disposed
        assertEquals(0, DataAccessRegistry.getInstance().registry.size());
    }

    @After
    public void cleanUp() {
        DataAccessRegistry.unregisterAndDisposeAll();
        assertEquals(0, DataAccessRegistry.getInstance().registry.size());
    }
}
