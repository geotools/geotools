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

import static org.geotools.data.complex.PolymorphicChainingTest.ARTIFACT;
import static org.geotools.data.complex.PolymorphicChainingTest.EX_NS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataAccessFinder;
import org.geotools.feature.NameImpl;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.type.Name;

public class HiddenDataAccessDisposalPolymorphicTest extends AbstractHiddenDataAccessDisposalTest {

    static final Name STRING_ATTRIBUTE = new NameImpl(EX_NS, "StringAttribute");

    static final Name GEO_ATTRIBUTE = new NameImpl(EX_NS, "GeoAttribute");

    private static final String schemaBase = "/test-data/";

    AppSchemaDataAccess artifactDataAccess;

    /** Load all the data accesses. */
    @Before
    public void loadDataAccesses() throws Exception {
        /** Load artifact data access using polymorphic mappings */
        Map dsParams = new HashMap();
        URL url = getClass().getResource(schemaBase + "artifact_mapping_recode.xml");
        assertNotNull(url);

        dsParams.put("dbtype", "app-schema");
        dsParams.put("url", url.toExternalForm());
        artifactDataAccess = (AppSchemaDataAccess) DataAccessFinder.getDataStore(dsParams);
        assertNotNull(artifactDataAccess);
        assertNotNull(artifactDataAccess.getSchema(ARTIFACT));
        assertFalse(artifactDataAccess.hidden);

        /** Load geologic unit data access */
        loadGeologicUnit();

        // 2 accessible data accesses + 4 hidden data accesses = 6
        assertEquals(6, DataAccessRegistry.getInstance().registry.size());
    }

    @Test
    public void testAutomaticDisposalDisabledIfPolymorphic() {
        guDataAccess.dispose();

        // no automatic disposal should have occurred, because of
        // polymorphic feature chaining configuration in artifact mapping file
        assertEquals(5, DataAccessRegistry.getInstance().registry.size());
    }
}
