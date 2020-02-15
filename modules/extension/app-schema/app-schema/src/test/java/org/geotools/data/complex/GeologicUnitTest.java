/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2015, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator;
import org.geotools.data.complex.config.AppSchemaDataAccessDTO;
import org.geotools.data.complex.config.AppSchemaFeatureTypeRegistry;
import org.geotools.data.complex.config.XMLConfigDigester;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.data.complex.util.EmfComplexFeatureReader;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.test.AppSchemaTestSupport;
import org.geotools.xsd.SchemaIndex;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

/**
 * This is to ensure we have a working GeologicUnit configuration test.
 *
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 */
public class GeologicUnitTest extends AppSchemaTestSupport {

    private static final String GSMLNS = "http://www.cgi-iugs.org/xml/GeoSciML/2";

    private static final String schemaBase = "/test-data/";

    private static EmfComplexFeatureReader reader;

    /**
     * Set up the reader
     *
     * @throws Exception If any exception occurs
     */
    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        reader = EmfComplexFeatureReader.newInstance();
    }

    @After
    public void cleanUpDataAccessRegistry() {
        DataAccessRegistry.unregisterAndDisposeAll();
    }

    /**
     * Load schema
     *
     * @param location schema location path that can be found through getClass().getResource()
     */
    private SchemaIndex loadSchema(final String location) throws IOException {
        final URL catalogLocation = getClass().getResource(schemaBase + "mappedPolygons.oasis.xml");
        reader.setResolver(catalogLocation);
        return reader.parse(new URL(location));
    }

    /**
     * Tests if the schema-to-FM parsing code developed for complex data store configuration loading
     * can parse the GeoSciML types
     */
    @Test
    public void testParseSchema() throws Exception {
        SchemaIndex schemaIndex = loadSchema("http://schemas.opengis.net/GeoSciML/Gsml.xsd");

        AppSchemaFeatureTypeRegistry typeRegistry = new AppSchemaFeatureTypeRegistry();
        try {
            typeRegistry.addSchemas(schemaIndex);

            Name typeName = Types.typeName(GSMLNS, "GeologicUnitType");
            ComplexType mf = (ComplexType) typeRegistry.getAttributeType(typeName);
            assertNotNull(mf);
            assertTrue(mf instanceof FeatureType);

            AttributeType superType = mf.getSuper();
            assertNotNull(superType);
            Name superTypeName = Types.typeName(GSMLNS, "GeologicFeatureType");
            assertEquals(superTypeName, superType.getName());
            assertTrue(superType instanceof FeatureType);
        } finally {
            typeRegistry.disposeSchemaIndexes();
        }
    }

    /** Test that mappings are loaded OK. */
    @Test
    public void testLoadMappingsConfig() throws Exception {
        XMLConfigDigester reader = new XMLConfigDigester();
        final URL url = getClass().getResource(schemaBase + "GeologicUnit.xml");

        AppSchemaDataAccessDTO config = reader.parse(url);

        Set mappings = AppSchemaDataAccessConfigurator.buildMappings(config);

        assertNotNull(mappings);
        assertEquals(1, mappings.size());
    }

    /**
     * Tests that a {@link FeatureSource} can be obtained for all names returned by {@link
     * AppSchemaDataAccess#getNames()}.
     */
    @Test
    public void testGetNamesAndFeatureSources() throws Exception {
        /*
         * Initiate data accesses and make sure they have the mappings
         */
        final Map<String, Serializable> dsParams = new HashMap<String, Serializable>();
        URL url = getClass().getResource(schemaBase + "GeologicUnit.xml");
        assertNotNull(url);
        dsParams.put("dbtype", "app-schema");
        dsParams.put("url", url.toExternalForm());

        DataAccess<?, ?> guDataStore = DataAccessFinder.getDataStore(dsParams);
        assertNotNull(guDataStore);

        for (Name name : guDataStore.getNames()) {
            FeatureSource<?, ?> fs = guDataStore.getFeatureSource(name);
            assertNotNull(fs);
        }
    }

    /** Test that geologic unit features are returned correctly. */
    @Test
    public void testGetFeatures() throws Exception {
        /*
         * Initiate data accesses and make sure they have the mappings
         */
        final Map dsParams = new HashMap();
        URL url = getClass().getResource(schemaBase + "GeologicUnit.xml");
        assertNotNull(url);
        dsParams.put("dbtype", "app-schema");
        dsParams.put("url", url.toExternalForm());

        DataAccess guDataStore = DataAccessFinder.getDataStore(dsParams);
        assertNotNull(guDataStore);
        FeatureType geologicUnitType = guDataStore.getSchema(FeatureChainingTest.GEOLOGIC_UNIT);
        assertNotNull(geologicUnitType);

        url = getClass().getResource(schemaBase + "MappedFeaturePropertyfile.xml");
        assertNotNull(url);

        dsParams.put("dbtype", "app-schema");
        dsParams.put("url", url.toExternalForm());
        DataAccess mfDataAccess = DataAccessFinder.getDataStore(dsParams);
        assertNotNull(mfDataAccess);

        FeatureSource guSource =
                (FeatureSource) guDataStore.getFeatureSource(FeatureChainingTest.GEOLOGIC_UNIT);

        FeatureCollection guFeatures = (FeatureCollection) guSource.getFeatures();
        assertEquals(3, size(guFeatures));

        FeatureSource cpSource =
                DataAccessRegistry.getFeatureSource(FeatureChainingTest.COMPOSITION_PART);
        FeatureCollection cpFeatures = (FeatureCollection) cpSource.getFeatures();
        assertEquals(4, size(cpFeatures));

        FeatureSource cgiSource =
                DataAccessRegistry.getFeatureSource(FeatureChainingTest.CGI_TERM_VALUE);
        FeatureCollection cgiFeatures = (FeatureCollection) cgiSource.getFeatures();
        assertEquals(6, size(cgiFeatures));
    }

    private int size(FeatureCollection<FeatureType, Feature> features) {
        int size = 0;
        FeatureIterator<Feature> i = features.features();
        try {
            for (; i.hasNext(); i.next()) {
                size++;
            }
        } finally {
            i.close();
        }
        return size;
    }
}
