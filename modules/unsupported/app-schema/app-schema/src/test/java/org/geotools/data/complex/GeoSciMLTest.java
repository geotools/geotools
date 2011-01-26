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

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator;
import org.geotools.data.complex.config.AppSchemaDataAccessDTO;
import org.geotools.data.complex.config.EmfAppSchemaReader;
import org.geotools.data.complex.config.FeatureTypeRegistry;
import org.geotools.data.complex.config.XMLConfigDigester;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.Types;
import org.geotools.xml.SchemaIndex;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

/**
 * DOCUMENT ME!
 * 
 * @author Rob Atkinson
 * @version $Id$
 * @source $URL:
 *         http://svn.geotools.org/geotools/branches/2.4.x/modules/unsupported/community-schemas
 *         /community-schema-ds/src/test/java/org/geotools/data/complex/BoreholeTest.java $
 * @since 2.4
 */
public class GeoSciMLTest {
    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(GeoSciMLTest.class.getPackage().getName());

    private static final String GSMLNS = "http://www.cgi-iugs.org/xml/GeoSciML/2";

    private static final String GMLNS = "http://www.opengis.net/gml";

    private static final String schemaBase = "/test-data/";

    private static EmfAppSchemaReader reader;

    private FeatureSource source;

    private static DataAccess<FeatureType, Feature> mappingDataStore;

    @BeforeClass
    public static void oneTimeSetUp() throws IOException {
        final Map<String, Serializable> dsParams = new HashMap<String, Serializable>();
        final URL url = GeoSciMLTest.class.getResource(schemaBase + "mappedPolygons.xml");
        dsParams.put("dbtype", "app-schema");
        dsParams.put("url", url.toExternalForm());
        mappingDataStore = DataAccessFinder.getDataStore(dsParams);

        reader = EmfAppSchemaReader.newInstance();
        // Logging.GEOTOOLS.forceMonolineConsoleOutput(Level.FINEST);
    }
    
    @AfterClass
    public static void oneTimeTearDown() {
        DataAccessRegistry.unregisterAll();
    }

    /**
     * 
     * @param location
     *            schema location path discoverable through getClass().getResource()
     * @return 
     */
    private SchemaIndex loadSchema(String location) throws IOException {
        URL catalogLocation = getClass().getResource(schemaBase + "mappedPolygons.oasis.xml");
        reader.setResolver(catalogLocation);
        return reader.parse(new URL(location));
    }

    /**
     * Tests if the schema-to-FM parsing code developed for complex datastore configuration loading
     * can parse the GeoSciML types
     * 
     * @throws Exception
     */
    @Test
    public void testParseSchema() throws Exception {
        SchemaIndex schemaIndex;
        try {
            // loadSchema(schemaBase + "commonSchemas_new/GeoSciML/Gsml.xsd");
            // use the absolute URL and let the Oasis Catalog resolve it to the local FS
            schemaIndex = loadSchema("http://schemas.opengis.net/GeoSciML/Gsml.xsd");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        FeatureTypeRegistry typeRegistry = new FeatureTypeRegistry();
        typeRegistry.addSchemas(schemaIndex);

        Name typeName = Types.typeName(GSMLNS, "MappedFeatureType");
        ComplexType mf = (ComplexType) typeRegistry.getAttributeType(typeName);
        assertNotNull(mf);
        assertTrue(mf instanceof FeatureType);

        typeName = Types.typeName("http://www.opengis.net/sampling/1.0", "SamplingFeatureType");
        mf = (ComplexType) typeRegistry.getAttributeType(typeName);
        assertNotNull(mf);
        assertTrue(mf instanceof FeatureType);
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

            FeatureSource fSource = (FeatureSource) mappingDataStore.getFeatureSource(typeName);

            final int EXPECTED_RESULT_COUNT = 2;

            FeatureCollection features = (FeatureCollection) fSource.getFeatures();

            int resultCount = getCount(features);
            assertEquals(EXPECTED_RESULT_COUNT, resultCount);

            Feature feature;
            int count = 0;
            FeatureIterator it = features.features();
            for (; it.hasNext();) {
                feature = (Feature) it.next();
                count++;
            }
            it.close();

            assertEquals(EXPECTED_RESULT_COUNT, count);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Test that getting features from a feature source with a query honours the namespace.
     * 
     * @throws Exception
     */
    @Test
    public void testFeatureSourceHonoursQueryNamespace() throws Exception {
        final Name typeName = Types.typeName(GSMLNS, "MappedFeature");
        FeatureSource<FeatureType, Feature> source = mappingDataStore.getFeatureSource(typeName);
        DefaultQuery query = new DefaultQuery();
        query.setNamespace(new URI(typeName.getNamespaceURI()));
        query.setTypeName(typeName.getLocalPart());
        FeatureCollection<FeatureType, Feature> features = source.getFeatures(query);
        assertNotNull(features);
        assertEquals(2, size(features));
    }

    private int size(FeatureCollection<FeatureType, Feature> features) {
        int size = 0;
        for (Iterator i = features.iterator(); i.hasNext(); i.next()) {
            size++;
        }
        return size;
    }

    private int getCount(FeatureCollection features) {
        FeatureIterator iterator = features.features();
        int count = 0;
        try {
            while (iterator.hasNext()) {
                iterator.next();
                count++;
            }
        } finally {
            iterator.close();
        }
        return count;
    }
}
