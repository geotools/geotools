/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureSource;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator;
import org.geotools.data.complex.config.AppSchemaDataAccessDTO;
import org.geotools.data.complex.config.NonFeatureTypeProxy;
import org.geotools.data.complex.config.SourceDataStore;
import org.geotools.data.complex.config.TypeMapping;
import org.geotools.feature.Types;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

/**
 * This tests AppSchemaDataAccessRegistry class. When an appschema data access is created, it would
 * be registered in the registry. Once it's in the registry, its feature type mapping and feature
 * source (simple or mapped) would be accessible globally.
 * 
 * @author Rini Angreani, Curtin Universtiy of Technology
 * 
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/test
 *         /java/org/geotools/data/complex/AppSchemaDataAccessRegistryTest.java $
 */
public class AppSchemaDataAccessRegistryTest {

    public static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.data.complex");

    private static final String GSMLNS = "urn:cgi:xmlns:CGI:GeoSciML:2.0";

    private static final Name MAPPED_FEATURE = Types.typeName(GSMLNS, "MappedFeature");

    private static final Name GEOLOGIC_UNIT = Types.typeName("myGeologicUnit");

    private static final Name COMPOSITION_PART = Types.typeName(GSMLNS, "CompositionPart");

    private static final Name CGI_TERM_VALUE = Types.typeName(GSMLNS, "CGI_TermValue");

    private static final Name CONTROLLED_CONCEPT = Types.typeName(GSMLNS, "ControlledConcept");

    private static final String schemaBase = "/test-data/";

    /**
     * Geological unit data access
     */
    private static AppSchemaDataAccess guDataAccess;

    /**
     * Compositional part data access
     */
    private static AppSchemaDataAccess cpDataAccess;

    /**
     * Mapped feature data access
     */
    private static AppSchemaDataAccess mfDataAccess;

    /**
     * CGI Term Value data access
     */
    private static AppSchemaDataAccess cgiDataAccess;

    /**
     * Controlled Concept data access
     */
    private static AppSchemaDataAccess ccDataAccess;

    /**
     * App-schema FeatureTypeMapping extract from a mapping file. This one has a mappingName to
     * identify itself.
     */
    private static TypeMapping dtoMappingName;

    /**
     * App-schema FeatureTypeMapping extract from a mapping file.This one has no mappingName,
     * therefore targetElement is used to identify itself.
     */
    private static TypeMapping dtoNoMappingName;

    /**
     * App-schema mapping file extract.
     */
    private static AppSchemaDataAccessDTO config;

    /**
     * Test registering and unregistering all data accesses works.
     * 
     * @throws Exception
     */
    @Test
    public void testRegisterAndUnregisterDataAccess() throws Exception {
        loadDataAccesses();
        /**
         * Check that data access are registered
         */
        this.checkRegisteredDataAccess(mfDataAccess, MAPPED_FEATURE, false);
        this.checkRegisteredDataAccess(guDataAccess, GEOLOGIC_UNIT, false);
        this.checkRegisteredDataAccess(cpDataAccess, COMPOSITION_PART, true);
        this.checkRegisteredDataAccess(cgiDataAccess, CGI_TERM_VALUE, true);
        this.checkRegisteredDataAccess(ccDataAccess, CONTROLLED_CONCEPT, true);

        /**
         * Now unregister, and see if they're successful
         */
        unregister(mfDataAccess, MAPPED_FEATURE);
        unregister(guDataAccess, GEOLOGIC_UNIT);
        unregister(cpDataAccess, COMPOSITION_PART);
        unregister(cgiDataAccess, CGI_TERM_VALUE);
        unregister(ccDataAccess, CONTROLLED_CONCEPT);
    }

    /**
     * Test that asking for a nonexistent type causes an excception to be thrown with the correct
     * number of type names in the detail message.
     * 
     * @throws Exception
     */
    @Test
    public void testThrowDataSourceException() throws Exception {
        Name typeName = Types.typeName(GSMLNS, "DoesNotExist");
        boolean handledException = false;
        try {
            AppSchemaDataAccessRegistry.getMappingByElement(typeName);
        } catch (DataSourceException e) {
            LOGGER.info(e.toString());
            handledException = true;
            assertTrue(e.getMessage().startsWith("Feature type " + typeName + " not found"));
        }
        assertTrue("Expected a DataSourceException to have been thrown and handled",
                handledException);
    }

    /**
     * Load all data accesses
     * 
     * @throws Exception
     */
    public static void loadDataAccesses() throws Exception {
        /**
         * Load Mapped Feature data access
         */
        Map dsParams = new HashMap();
        URL url = AppSchemaDataAccessRegistryTest.class.getResource(schemaBase
                + "MappedFeaturePropertyfile.xml");
        assertNotNull(url);
        dsParams.put("dbtype", "app-schema");
        dsParams.put("url", url.toExternalForm());
        mfDataAccess = (AppSchemaDataAccess) DataAccessFinder.getDataStore(dsParams);
        assertNotNull(mfDataAccess);

        /**
         * Load Geological Unit data access
         */
        url = AppSchemaDataAccessRegistryTest.class.getResource(schemaBase + "GeologicUnit.xml");
        assertNotNull(url);
        dsParams.put("url", url.toExternalForm());
        guDataAccess = (AppSchemaDataAccess) DataAccessFinder.getDataStore(dsParams);
        assertNotNull(guDataAccess);

        /**
         * Find Compositional Part data access
         */
        cpDataAccess = (AppSchemaDataAccess) DataAccessRegistry.getDataAccess(COMPOSITION_PART);
        assertNotNull(cpDataAccess);

        /**
         * Find CGI Term Value data access
         */
        cgiDataAccess = (AppSchemaDataAccess) DataAccessRegistry.getDataAccess(CGI_TERM_VALUE);
        assertNotNull(cgiDataAccess);

        /**
         * Find ControlledConcept data access
         */
        ccDataAccess = (AppSchemaDataAccess) DataAccessRegistry.getDataAccess(CONTROLLED_CONCEPT);
        assertNotNull(ccDataAccess);
    }

    /**
     * Create mock app-schema data access config.
     */
    @BeforeClass
    public static void setUp() {
        /**
         * Create mock AppSchemaDataAccessDto to test mappingName
         */
        final String TARGET_ELEMENT_NAME = "gsml:MappedFeature";
        final String MAPPING_NAME = "MAPPING_NAME_ONE";
        final String SOURCE_ID = "MappedFeature";
        final String MAPPING_FILE = "MappedFeaturePropertyfile";

        HashSet mappings = new HashSet();
        Map dsParams = new HashMap();
        URL url = AppSchemaDataAccessRegistryTest.class.getResource(schemaBase);
        assertNotNull(url);
        final SourceDataStore ds = new SourceDataStore();
        ds.setId(SOURCE_ID);
        try {
            dsParams.put("directory", new File(url.toURI()).toURL().toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        ds.setParams(dsParams);
        config = new AppSchemaDataAccessDTO();
        config.setSourceDataStores(new ArrayList() {
            {
                add(ds);
            }
        });
        config.setBaseSchemasUrl(url.toExternalForm());
        config.setNamespaces(new HashMap<String, String>() {
            {
                put("gsml", GSMLNS);
            }
        });
        config.setTargetSchemasUris(new ArrayList<String>() {
            {
                add("http://www.geosciml.org/geosciml/2.0/xsd/geosciml.xsd");
            }
        });
        config.setCatalog("mappedPolygons.oasis.xml");

        /**
         * Create mock TypeMapping objects to be set inside config in the test cases
         */
        dtoMappingName = new TypeMapping();
        dtoMappingName.setMappingName(MAPPING_NAME);
        dtoMappingName.setSourceDataStore(SOURCE_ID);
        dtoMappingName.setSourceTypeName(MAPPING_FILE);
        dtoMappingName.setTargetElementName(TARGET_ELEMENT_NAME);

        dtoNoMappingName = new TypeMapping();
        dtoNoMappingName.setSourceDataStore(SOURCE_ID);
        dtoNoMappingName.setSourceTypeName(MAPPING_FILE);
        dtoNoMappingName.setTargetElementName(TARGET_ELEMENT_NAME);
    }

    /**
     * Clean the registry so it doesn't affect other tests
     */
    @AfterClass
    public static void tearDown() {
        DataAccessRegistry.unregisterAll();
    }

    /**
     * Tests that registry works.
     * 
     * @param dataAccess
     *            The app schema data access to check
     * @param typeName
     *            Feature type
     * @param isNonFeature
     *            true if the type is non feature
     * @throws IOException
     */
    private void checkRegisteredDataAccess(AppSchemaDataAccess dataAccess, Name typeName,
            boolean isNonFeature) throws IOException {
        FeatureTypeMapping mapping = AppSchemaDataAccessRegistry.getMappingByName(typeName);
        assertNotNull(mapping);
        // compare with the supplied data access
        assertEquals(dataAccess.getMappingByName(typeName).equals(mapping), true);
        if (isNonFeature) {
            assertTrue(mapping.getTargetFeature().getType() instanceof NonFeatureTypeProxy);
        }

        // should return a simple feature source
        FeatureSource<FeatureType, Feature> source = AppSchemaDataAccessRegistry
                .getMappingByName(typeName).getSource();
        assertNotNull(source);
        assertEquals(mapping.getSource(), source);

        // should return a mapping feature source
        FeatureSource<FeatureType, Feature> mappedSource = DataAccessRegistry
                .getFeatureSource(typeName);
        assertNotNull(mappedSource);
        // compare with the supplied data access
        assertTrue(mappedSource.getDataStore().equals(dataAccess));
    }

    /**
     * Tests that unregistering data access works
     * 
     * @param dataAccess
     *            The data access
     * @param typeName
     *            The feature type name
     * @throws IOException
     */
    private void unregister(DataAccess dataAccess, Name typeName) throws IOException {
        DataAccessRegistry.unregister(dataAccess);
        boolean notFound = false;
        try {
            FeatureTypeMapping mapping = AppSchemaDataAccessRegistry.getMappingByElement(typeName);
        } catch (DataSourceException e) {
            notFound = true;
            assertTrue(e.getMessage().startsWith("Feature type " + typeName + " not found"));
        }
        if (!notFound) {
            fail("Expecting DataSourceException but didn't occur. Deregistering data access fails.");
        }
        notFound = false;
        try {
            FeatureSource source = AppSchemaDataAccessRegistry.getSimpleFeatureSource(typeName);
        } catch (DataSourceException e) {
            notFound = true;
            assertTrue(e.getMessage().startsWith("Feature type " + typeName + " not found"));
        }
        if (!notFound) {
            fail("Expecting DataSourceException but didn't occur. Deregistering data access fails.");
        }
    }

    /**
     * Fail scenarios for breaking uniqueness of FeatureTypeMapping key (mappingName or
     * targetElement).
     * 
     * @throws IOException
     */
    @Test
    public void testDuplicateKey() throws IOException {
        boolean threwException = false;
        /**
         * Test duplicate mappingName
         */
        HashSet mappings = new HashSet();
        TypeMapping duplicate = new TypeMapping();
        duplicate.setMappingName(dtoMappingName.getMappingName());
        duplicate.setSourceDataStore(dtoMappingName.getSourceDataStore());
        duplicate.setSourceTypeName(dtoMappingName.getSourceTypeName());
        duplicate.setTargetElementName(dtoMappingName.getTargetElementName());
        mappings.add(dtoMappingName);
        mappings.add(duplicate);
        config.setTypeMappings(mappings);
        try {
            AppSchemaDataAccess da = new AppSchemaDataAccess(AppSchemaDataAccessConfigurator
                    .buildMappings(config));
        } catch (DataSourceException e) {
            assertTrue(e
                    .getMessage()
                    .startsWith(
                            "Duplicate mappingName or targetElement across FeatureTypeMapping instances detected."));
            threwException = true;
        }
        assertTrue(threwException);
        threwException = false;
        /**
         * Test when targetElement duplicates a mappingName
         */
        duplicate = new TypeMapping();
        duplicate.setMappingName(dtoNoMappingName.getTargetElementName());
        duplicate.setSourceDataStore(dtoNoMappingName.getSourceDataStore());
        duplicate.setSourceTypeName(dtoNoMappingName.getSourceTypeName());
        duplicate.setTargetElementName(dtoNoMappingName.getTargetElementName());
        mappings.clear();
        mappings.add(duplicate);
        mappings.add(dtoNoMappingName);
        config.setTypeMappings(mappings);
        // make sure the above operation didn't fail
        assertTrue(config.getTypeMappings().containsAll(mappings));
        try {
            AppSchemaDataAccess da = new AppSchemaDataAccess(AppSchemaDataAccessConfigurator
                    .buildMappings(config));
        } catch (DataSourceException e) {
            assertTrue(e
                    .getMessage()
                    .startsWith(
                            "Duplicate mappingName or targetElement across FeatureTypeMapping instances detected."));
            threwException = true;
        }
        assertTrue(threwException);
        threwException = false;
        /**
         * Test duplicate targetElement, when both don't have mappingName
         */
        duplicate = new TypeMapping();
        duplicate.setSourceDataStore(dtoNoMappingName.getSourceDataStore());
        duplicate.setSourceTypeName(dtoNoMappingName.getSourceTypeName());
        duplicate.setTargetElementName(dtoNoMappingName.getTargetElementName());
        mappings.clear();
        mappings.add(duplicate);
        mappings.add(dtoNoMappingName);
        config.setTypeMappings(mappings);
        assertTrue(config.getTypeMappings().containsAll(mappings));
        try {
            AppSchemaDataAccess da = new AppSchemaDataAccess(AppSchemaDataAccessConfigurator
                    .buildMappings(config));
        } catch (DataSourceException e) {
            assertTrue(e
                    .getMessage()
                    .startsWith(
                            "Duplicate mappingName or targetElement across FeatureTypeMapping instances detected."));
            threwException = true;
        }
        assertTrue(threwException);
    }

    /**
     * Success scenarios for keeping uniqueness of FeatureTypeMapping key (mappingName or
     * targetElement).
     * 
     * @throws IOException
     * 
     * @throws IOException
     */
    @Test
    public void testUniqueKey() throws IOException {
        /**
         * When mappingName are present in both mappings, and they're unique
         */
        HashSet mappings = new HashSet();
        TypeMapping duplicate = new TypeMapping();
        duplicate.setMappingName(dtoMappingName.getTargetElementName());
        duplicate.setSourceDataStore(dtoMappingName.getSourceDataStore());
        duplicate.setSourceTypeName(dtoMappingName.getSourceTypeName());
        duplicate.setTargetElementName(dtoMappingName.getTargetElementName());
        mappings.add(dtoMappingName);
        mappings.add(duplicate);
        config.setTypeMappings(mappings);
        AppSchemaDataAccess da = new AppSchemaDataAccess(AppSchemaDataAccessConfigurator
                .buildMappings(config));
        assertNotNull(da);
        da.dispose();
        /**
         * When mappingName is present in one mapping, and it's different from the targetElement of
         * the other.
         */
        mappings.clear();
        mappings.add(dtoMappingName);
        mappings.add(dtoNoMappingName);
        config.setTypeMappings(mappings);
        assertTrue(config.getTypeMappings().containsAll(mappings));
        da = new AppSchemaDataAccess(AppSchemaDataAccessConfigurator.buildMappings(config));
        assertNotNull(da);
        da.dispose();
        // no need to test the scenario if both target elements are unique, as most of the other
        // app-schema test files are already like this.
    }
}
