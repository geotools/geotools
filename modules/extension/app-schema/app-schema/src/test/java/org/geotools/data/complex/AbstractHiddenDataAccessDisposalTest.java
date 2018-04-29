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

import static org.geotools.data.complex.FeatureChainingTest.CGI_TERM_VALUE;
import static org.geotools.data.complex.FeatureChainingTest.COMPOSITION_PART;
import static org.geotools.data.complex.FeatureChainingTest.CONTROLLED_CONCEPT;
import static org.geotools.data.complex.FeatureChainingTest.GEOLOGIC_UNIT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataAccessFinder;
import org.geotools.feature.NameImpl;
import org.geotools.test.AppSchemaTestSupport;
import org.opengis.feature.type.Name;

public class AbstractHiddenDataAccessDisposalTest extends AppSchemaTestSupport {

    static final Name EXPOSURE_COLOR = new NameImpl("exposureColor");

    static final String schemaBase = "/test-data/";

    AppSchemaDataAccess guDataAccess;

    void loadGeologicUnit() throws Exception {
        /** Load geologic unit data access */
        Map dsParams = new HashMap();
        URL url = getClass().getResource(schemaBase + "GeologicUnit.xml");
        assertNotNull(url);

        dsParams.put("dbtype", "app-schema");
        dsParams.put("url", url.toExternalForm());
        guDataAccess = (AppSchemaDataAccess) DataAccessFinder.getDataStore(dsParams);
        assertNotNull(guDataAccess);
        assertNotNull(guDataAccess.getSchema(GEOLOGIC_UNIT));
        assertFalse(guDataAccess.hidden);

        /**
         * Non-feature types that are included in geologicUnit.xml should be loaded when geologic
         * unit data access is created
         */
        // Composition Part
        AppSchemaDataAccess cpDataAccess =
                (AppSchemaDataAccess) DataAccessRegistry.getDataAccess(COMPOSITION_PART);
        assertNotNull(cpDataAccess);
        assertNotNull(cpDataAccess.getFeatureSource(COMPOSITION_PART));
        assertTrue(cpDataAccess.hidden);
        // CGI TermValue
        AppSchemaDataAccess cgiDataAccess =
                (AppSchemaDataAccess) DataAccessRegistry.getDataAccess(CGI_TERM_VALUE);
        assertNotNull(cgiDataAccess);
        assertNotNull(cgiDataAccess.getFeatureSource(CGI_TERM_VALUE));
        assertTrue(cgiDataAccess.hidden);
        // exposureColor
        AppSchemaDataAccess expDataAccess =
                (AppSchemaDataAccess) DataAccessRegistry.getDataAccess(EXPOSURE_COLOR);
        assertNotNull(expDataAccess);
        assertNotNull(expDataAccess.getFeatureSource(EXPOSURE_COLOR));
        assertTrue(expDataAccess.hidden);
        // ControlledConcept
        AppSchemaDataAccess ccDataAccess =
                (AppSchemaDataAccess) DataAccessRegistry.getDataAccess(CONTROLLED_CONCEPT);
        assertNotNull(ccDataAccess);
        assertNotNull(ccDataAccess.getFeatureSource(CONTROLLED_CONCEPT));
        assertTrue(expDataAccess.hidden);

        // should be the same, since both types are defined in the same mapping file
        assertEquals(cgiDataAccess, expDataAccess);
    }
}
