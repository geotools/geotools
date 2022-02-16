/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2011, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.data.util.FeatureStreams.toFeatureStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
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
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

/**
 * @author Rob Atkinson
 * @since 2.4
 */
public class GeoSciMLTest extends AppSchemaTestSupport {
    private static final String GSMLNS = "http://www.cgi-iugs.org/xml/GeoSciML/2";

    private static final String schemaBase = "/test-data/";

    private static EmfComplexFeatureReader reader;

    private static DataAccess<FeatureType, Feature> mappingDataStore;

    @BeforeClass
    public static void oneTimeSetUp() throws IOException {
        final Map<String, Serializable> dsParams = new HashMap<>();
        final URL url = GeoSciMLTest.class.getResource(schemaBase + "mappedPolygons.xml");
        dsParams.put("dbtype", "app-schema");
        dsParams.put("url", url.toExternalForm());
        mappingDataStore = DataAccessFinder.getDataStore(dsParams);

        reader = EmfComplexFeatureReader.newInstance();
        // Logging.GEOTOOLS.forceMonolineConsoleOutput(Level.FINEST);
    }

    /** @param location schema location path discoverable through getClass().getResource() */
    private SchemaIndex loadSchema(String location) throws IOException {
        URL catalogLocation = getClass().getResource(schemaBase + "mappedPolygons.oasis.xml");
        reader.setResolver(catalogLocation);
        return reader.parse(new URL(location));
    }

    /**
     * Tests if the schema-to-FM parsing code developed for complex datastore configuration loading
     * can parse the GeoSciML types
     */
    @Test
    public void testParseSchema() throws Exception {
        SchemaIndex schemaIndex;
        try {
            // loadSchema(schemaBase + "commonSchemas_new/GeoSciML/Gsml.xsd");
            // use the absolute URL and let the Oasis Catalog resolve it to the local FS
            schemaIndex = loadSchema("http://schemas.opengis.net/GeoSciML/Gsml.xsd");
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            throw e;
        }

        AppSchemaFeatureTypeRegistry typeRegistry = new AppSchemaFeatureTypeRegistry();
        try {
            typeRegistry.addSchemas(schemaIndex);

            Name typeName = Types.typeName(GSMLNS, "MappedFeatureType");
            ComplexType mf = (ComplexType) typeRegistry.getAttributeType(typeName);
            assertNotNull(mf);
            assertTrue(mf instanceof FeatureType);

            typeName = Types.typeName("http://www.opengis.net/sampling/1.0", "SamplingFeatureType");
            mf = (ComplexType) typeRegistry.getAttributeType(typeName);
            assertNotNull(mf);
            assertTrue(mf instanceof FeatureType);
        } finally {
            typeRegistry.disposeSchemaIndexes();
        }
        /*
         * AttributeType superType = mf.getSuper(); assertNotNull(superType); Name superTypeName =
         * Types.typeName(SANS, "ProfileType"); assertEquals(superTypeName, superType.getName());
         * assertTrue(superType instanceof FeatureType); // ensure all needed types were parsed and
         * aren't just empty proxies Collection properties = mf.getProperties(); assertEquals(16,
         * properties.size()); Map expectedNamesAndTypes = new HashMap(); // from
         * gml:AbstractFeatureType expectedNamesAndTypes.put(name(GMLNS, "metaDataProperty"),
         * typeName(GMLNS, "MetaDataPropertyType")); expectedNamesAndTypes.put(name(GMLNS,
         * "description"), typeName(GMLNS, "StringOrRefType"));
         * expectedNamesAndTypes.put(name(GMLNS, "name"), typeName(GMLNS, "CodeType"));
         * expectedNamesAndTypes.put(name(GMLNS, "boundedBy"), typeName(GMLNS,
         * "BoundingShapeType")); expectedNamesAndTypes.put(name(GMLNS, "location"), typeName(GMLNS,
         * "LocationPropertyType")); // from sa:ProfileType expectedNamesAndTypes.put(name(SANS,
         * "begin"), typeName(GMLNS, "PointPropertyType")); expectedNamesAndTypes.put(name(SANS,
         * "end"), typeName(GMLNS, "PointPropertyType")); expectedNamesAndTypes.put(name(SANS,
         * "length"), typeName(SWENS, "RelativeMeasureType")); expectedNamesAndTypes.put(name(SANS,
         * "shape"), typeName(GEONS, "Shape1DPropertyType")); // sa:SamplingFeatureType
         * expectedNamesAndTypes.put(name(SANS, "member"), typeName(SANS,
         * "SamplingFeaturePropertyType")); expectedNamesAndTypes.put(name(SANS, "surveyDetails"),
         * typeName(SANS, "SurveyProcedurePropertyType")); expectedNamesAndTypes.put(name(SANS,
         * "associatedSpecimen"), typeName(SANS, "SpecimenPropertyType"));
         * expectedNamesAndTypes.put(name(SANS, "relatedObservation"), typeName(OMNS,
         * "AbstractObservationPropertyType")); // from xmml:mfType
         * expectedNamesAndTypes.put(name(XMMLNS, "drillMethod"), typeName(XMMLNS, "drillCode"));
         * expectedNamesAndTypes.put(name(XMMLNS, "collarDiameter"), typeName(GMLNS,
         * "MeasureType")); expectedNamesAndTypes.put(name(XMMLNS, "log"), typeName(XMMLNS,
         * "LogPropertyType"));
         *
         * for (Iterator it = expectedNamesAndTypes.entrySet().iterator(); it.hasNext();) {
         * Map.Entry entry = (Entry) it.next(); Name dName = (Name) entry.getKey(); Name tName =
         * (Name) entry.getValue();
         *
         * AttributeDescriptor d = (AttributeDescriptor) Types.descriptor(mf, dName);
         * assertNotNull("Descriptor not found: " + dName, d); AttributeType type; try { type =
         * d.getType(); } catch (Exception e) { LOGGER.log(Level.SEVERE, "type not parsed for " +
         * ((AttributeDescriptor) d).getName(), e); throw e; } assertNotNull(type);
         * assertNotNull(type.getName()); assertNotNull(type.getBinding()); if (tName != null) {
         * assertEquals(tName, type.getName()); } }
         *
         * Name tcl = Types.typeName(SWENS, "TypedCategoryListType"); AttributeType
         * typedCategoryListType = (AttributeType) typeRegistry.get(tcl);
         * assertNotNull(typedCategoryListType); assertFalse(typedCategoryListType instanceof
         * ComplexType);
         */
    }

    @Test
    public void testLoadMappingsConfig() throws Exception {
        XMLConfigDigester reader = new XMLConfigDigester();
        final URL url = getClass().getResource(schemaBase + "mappedPolygons.xml");

        AppSchemaDataAccessDTO config = reader.parse(url);

        Set mappings = AppSchemaDataAccessConfigurator.buildMappings(config);

        assertNotNull(mappings);
        assertEquals(1, mappings.size());
    }

    @Test
    public void testDataStore() throws Exception {
        try {
            final Name typeName = Types.typeName(GSMLNS, "MappedFeature");
            FeatureType boreholeType = mappingDataStore.getSchema(typeName);
            assertNotNull(boreholeType);

            final int EXPECTED_RESULT_COUNT = 2;
            FeatureCollection<?, ?> features =
                    mappingDataStore.getFeatureSource(typeName).getFeatures();

            int resultCount = DataUtilities.count(features);
            assertEquals(EXPECTED_RESULT_COUNT, resultCount);

            int count = 0;
            try (FeatureIterator it = features.features()) {
                while (it.hasNext()) {
                    it.next();
                    count++;
                }
            }

            assertEquals(EXPECTED_RESULT_COUNT, count);
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            throw e;
        }
    }

    /** Test that getting features from a feature source with a query honours the namespace. */
    @Test
    public void testFeatureSourceHonoursQueryNamespace() throws Exception {
        final Name typeName = Types.typeName(GSMLNS, "MappedFeature");
        FeatureSource<FeatureType, Feature> source = mappingDataStore.getFeatureSource(typeName);
        Query query = new Query();
        query.setNamespace(new URI(typeName.getNamespaceURI()));
        query.setTypeName(typeName.getLocalPart());
        FeatureCollection<FeatureType, Feature> features = source.getFeatures(query);
        assertNotNull(features);
        assertEquals(2, DataUtilities.count(features));
    }

    /**
     * Checks that declared namespaces are included on FeatureType's userData Map for the complex
     * features collection.
     */
    @Test
    public void testComplexFeatureNamespaces() throws Exception {
        final Name typeName = Types.typeName(GSMLNS, "MappedFeature");
        FeatureSource<FeatureType, Feature> source = mappingDataStore.getFeatureSource(typeName);
        Query query = new Query();
        query.setNamespace(new URI(typeName.getNamespaceURI()));
        query.setTypeName(typeName.getLocalPart());
        FeatureCollection<FeatureType, Feature> features = source.getFeatures(query);
        assertNotNull(features);
        try (final Stream<Feature> featureStream = toFeatureStream(features)) {
            Optional<Feature> first = featureStream.findFirst();
            @SuppressWarnings("unchecked")
            Optional<Map<String, String>> mapOpt =
                    first.map(Feature::getDescriptor)
                            .map(AttributeDescriptor::getType)
                            .map(AttributeType::getUserData)
                            .map(m -> m.get(Types.DECLARED_NAMESPACES_MAP))
                            .filter(v -> v instanceof Map)
                            .map(x -> (Map<String, String>) x);
            assertTrue(mapOpt.isPresent());
            final Map<String, String> namespacesMap = mapOpt.get();
            assertEquals(3, namespacesMap.keySet().size());
            assertTrue(
                    getExpectedNamespaces().stream()
                            .allMatch(ns -> namespacesMap.containsValue(ns)));
        }
    }

    private List<String> getExpectedNamespaces() {
        return Arrays.asList(
                "http://www.w3.org/XML/1998/namespace",
                "http://www.opengis.net/gml",
                "http://www.cgi-iugs.org/xml/GeoSciML/2");
    }
}
