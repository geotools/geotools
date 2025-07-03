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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.DataAccessFinder;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.feature.Attribute;
import org.geotools.api.feature.ComplexAttribute;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.ComplexType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.appschema.filter.FilterFactoryImplNamespaceAware;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator;
import org.geotools.data.complex.config.AppSchemaDataAccessDTO;
import org.geotools.data.complex.config.AppSchemaFeatureTypeRegistry;
import org.geotools.data.complex.config.XMLConfigDigester;
import org.geotools.data.complex.feature.type.ComplexFeatureTypeImpl;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.data.complex.util.EmfComplexFeatureReader;
import org.geotools.data.complex.util.XPathUtil.StepList;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.test.AppSchemaTestSupport;
import org.geotools.xlink.XLINK;
import org.geotools.xsd.SchemaIndex;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * @author Gabriel Roldan (Axios Engineering)
 * @version $Id$
 * @since 2.4
 */
public class BoreholeTest extends AppSchemaTestSupport {
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(BoreholeTest.class);

    private static final String XMMLNS = "http://www.opengis.net/xmml";

    private static final String SANS = "http://www.seegrid.csiro.au/xml/sampling";

    private static final String OMNS = "http://www.opengis.net/om";

    private static final String SWENS = "http://www.opengis.net/swe/0.0";

    private static final String GMLNS = "http://www.opengis.net/gml";

    private static final String GEONS = "http://www.seegrid.csiro.au/xml/geometry";

    private static final String schemaBase = "/test-data/";

    final Name typeName = new NameImpl(XMMLNS, "Borehole");

    private static EmfComplexFeatureReader reader;

    private FeatureSource source;

    private static DataAccess<FeatureType, Feature> mappingDataStore;

    @BeforeClass
    public static void oneTimeSetUp() throws IOException {
        // System.out.println("beforeclass");
        final Map<String, Serializable> dsParams = new HashMap<>();
        final URL url = BoreholeTest.class.getResource(schemaBase + "BoreholeTest_properties.xml");
        dsParams.put("dbtype", "app-schema");
        dsParams.put("url", url.toExternalForm());

        mappingDataStore = DataAccessFinder.getDataStore(dsParams);
        assertNotNull(mappingDataStore);

        reader = EmfComplexFeatureReader.newInstance();
    }

    /** @param location schema location path discoverable through getClass().getResource() */
    private SchemaIndex loadSchema(String location) throws IOException {
        // load needed GML types directly from the gml schemas
        URL schemaLocation = getClass().getResource(location);
        assertNotNull(location, schemaLocation);
        return reader.parse(schemaLocation);
    }

    /**
     * Tests if the schema-to-FM parsing code developed for complex datastore configuration loading can parse the
     * GeoSciML types
     */
    @Test
    public void testParseBoreholeSchema() throws Exception {
        /*
         * not found types and elements:
         */

        // load geosciml schema
        SchemaIndex schemaIndex;
        try {
            schemaIndex = loadSchema(schemaBase + "commonSchemas/XMML/1/borehole.xsd");
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            throw e;
        }

        AppSchemaFeatureTypeRegistry typeRegistry = new AppSchemaFeatureTypeRegistry();
        try {
            typeRegistry.addSchemas(schemaIndex);

            Name typeName = Types.typeName(XMMLNS, "BoreholeType");
            ComplexFeatureTypeImpl borehole = (ComplexFeatureTypeImpl) typeRegistry.getAttributeType(typeName);
            assertNotNull(borehole);
            assertTrue(borehole instanceof FeatureType);

            AttributeType superType = borehole.getSuper();
            assertNotNull(superType);
            Name superTypeName = Types.typeName(SANS, "ProfileType");
            assertEquals(superTypeName, superType.getName());
            assertTrue(superType instanceof FeatureType);

            // ensure all needed types were parsed and aren't just empty proxies
            Collection properties = borehole.getTypeDescriptors();
            assertEquals(16, properties.size());
            Map<Name, Name> expectedNamesAndTypes = new HashMap<>();
            // from gml:AbstractFeatureType
            expectedNamesAndTypes.put(name(GMLNS, "metaDataProperty"), typeName(GMLNS, "MetaDataPropertyType"));
            expectedNamesAndTypes.put(name(GMLNS, "description"), typeName(GMLNS, "StringOrRefType"));
            expectedNamesAndTypes.put(name(GMLNS, "name"), typeName(GMLNS, "CodeType"));
            expectedNamesAndTypes.put(name(GMLNS, "boundedBy"), typeName(GMLNS, "BoundingShapeType"));
            expectedNamesAndTypes.put(name(GMLNS, "location"), typeName(GMLNS, "LocationPropertyType"));
            // from sa:ProfileType
            expectedNamesAndTypes.put(name(SANS, "begin"), typeName(GMLNS, "PointPropertyType"));
            expectedNamesAndTypes.put(name(SANS, "end"), typeName(GMLNS, "PointPropertyType"));
            expectedNamesAndTypes.put(name(SANS, "length"), typeName(SWENS, "RelativeMeasureType"));
            expectedNamesAndTypes.put(name(SANS, "shape"), typeName(GEONS, "Shape1DPropertyType"));
            // sa:SamplingFeatureType
            expectedNamesAndTypes.put(name(SANS, "member"), typeName(SANS, "SamplingFeaturePropertyType"));
            expectedNamesAndTypes.put(name(SANS, "surveyDetails"), typeName(SANS, "SurveyProcedurePropertyType"));
            expectedNamesAndTypes.put(name(SANS, "associatedSpecimen"), typeName(SANS, "SpecimenPropertyType"));
            expectedNamesAndTypes.put(
                    name(SANS, "relatedObservation"), typeName(OMNS, "AbstractObservationPropertyType"));
            // from xmml:BoreholeType
            expectedNamesAndTypes.put(name(XMMLNS, "drillMethod"), typeName(XMMLNS, "drillCode"));
            expectedNamesAndTypes.put(name(XMMLNS, "collarDiameter"), typeName(GMLNS, "MeasureType"));
            expectedNamesAndTypes.put(name(XMMLNS, "log"), typeName(XMMLNS, "LogPropertyType"));

            for (Entry<Name, Name> nameNameEntry : expectedNamesAndTypes.entrySet()) {
                Name dName = nameNameEntry.getKey();
                Name tName = nameNameEntry.getValue();

                AttributeDescriptor d = (AttributeDescriptor) Types.descriptor(borehole, dName);
                assertNotNull("Descriptor not found: " + dName, d);
                AttributeType type;
                try {
                    type = d.getType();
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "type not parsed for " + d.getName(), e);
                    throw e;
                }
                assertNotNull(type);
                assertNotNull(type.getName());
                assertNotNull(type.getBinding());
                if (tName != null) {
                    assertEquals(tName, type.getName());
                }
            }

            Name tcl = Types.typeName(SWENS, "TypedCategoryListType");
            AttributeType typedCategoryListType = typeRegistry.getAttributeType(tcl);
            assertNotNull(typedCategoryListType);
            assertTrue(typedCategoryListType instanceof ComplexType);
        } finally {
            typeRegistry.disposeSchemaIndexes();
        }
    }

    private Name typeName(String ns, String localName) {
        return Types.typeName(ns, localName);
    }

    private Name name(String ns, String localName) {
        return new NameImpl(ns, localName);
    }

    @Test
    public void testLoadMappingsConfig() throws Exception {
        XMLConfigDigester reader = new XMLConfigDigester();
        URL url = getClass().getResource(schemaBase + "BoreholeTest_properties.xml");

        AppSchemaDataAccessDTO config = reader.parse(url);

        Set mappings = AppSchemaDataAccessConfigurator.buildMappings(config);

        assertNotNull(mappings);
        assertEquals(1, mappings.size());

        FeatureTypeMapping mapping = (FeatureTypeMapping) mappings.iterator().next();

        AttributeDescriptor targetFeature = mapping.getTargetFeature();
        assertNotNull(targetFeature);
        assertNotNull(targetFeature.getType());
        assertEquals(XMMLNS, targetFeature.getName().getNamespaceURI());
        assertEquals("Borehole", targetFeature.getName().getLocalPart());

        source = mapping.getSource();
        assertNotNull(source);
        FeatureType schema = source.getSchema();
        String typeName = schema.getName().toString();
        assertEquals("test-data:boreholes_denormalized", typeName);

        List attributeMappings = mapping.getAttributeMappings();
        assertEquals(24, attributeMappings.size());

        AttributeMapping attMapping = (AttributeMapping) attributeMappings.get(0);
        assertNotNull(attMapping);
        StepList targetXPath = attMapping.getTargetXPath();
        assertNotNull(targetXPath);
        assertEquals("xmml:Borehole", targetXPath.toString());

        Expression idExpression = attMapping.getIdentifierExpression();
        assertNotNull(idExpression);
        assertTrue(idExpression instanceof Function);
        Function idFunction = (Function) idExpression;
        assertEquals("strConcat", idFunction.getName());
        assertTrue(idFunction.getParameters().get(0) instanceof Literal);
        assertTrue(idFunction.getParameters().get(1) instanceof PropertyName);

        assertEquals(Expression.NIL, attMapping.getSourceExpression());
    }

    @Test
    public void testGetDataStore() throws Exception {
        assertNotNull(mappingDataStore.getSchema(typeName));
    }

    @Test
    public void testDataStore() throws Exception {
        FeatureSource<FeatureType, Feature> fSource = mappingDataStore.getFeatureSource(typeName);

        // make a getFeatures request with a nested properties filter.
        // note that the expected result count is set to 65 since that's the
        // number
        // of results I get from a sql select on min_time_d = 'carnian'
        final int EXPECTED_RESULT_COUNT = 20;

        FeatureCollection<FeatureType, Feature> features = fSource.getFeatures();

        int resultCount = size(features);
        String msg = "be sure difference in result count is not due to different dataset."
                + " Query used should be min_time_d = 'carnian'";
        assertEquals(msg, EXPECTED_RESULT_COUNT, resultCount);

        int count = 0;
        try (FeatureIterator<Feature> it = features.features()) {
            while (it.hasNext()) {
                it.next();
                count++;
            }
            assertEquals(EXPECTED_RESULT_COUNT, count);
        }
    }

    private int size(FeatureCollection<FeatureType, Feature> features) {
        int size = 0;
        try (FeatureIterator<Feature> i = features.features()) {
            for (; i.hasNext(); i.next()) {
                size++;
            }
        }
        return size;
    }

    @Test
    public void testQueryXlinkProperty() throws Exception {
        final FeatureSource<FeatureType, Feature> fSource = mappingDataStore.getFeatureSource(typeName);
        final String queryProperty = "sa:shape/geo:LineByVector/geo:origin/@xlink:href";
        final String queryLiteral = "#bh.176909a.start";

        NamespaceSupport namespaces = new NamespaceSupport();
        namespaces.declarePrefix("sa", SANS);
        namespaces.declarePrefix("geo", GEONS);
        namespaces.declarePrefix("xlink", XLINK.NAMESPACE);

        final FilterFactory ff = new FilterFactoryImplNamespaceAware(namespaces);
        final PropertyName propertyName = ff.property(queryProperty);
        final Literal literal = ff.literal(queryLiteral);

        final Filter filter = ff.equals(propertyName, literal);

        FeatureCollection<FeatureType, Feature> features = fSource.getFeatures(filter);

        // did the query work?
        int resultCount = size(features);
        assertEquals(1, resultCount);

        // the datastore performed the query by unmapping the client property
        // to its corresponding source expression, as defined in the AttributeMapping
        // clientProperties.
        // Now the Filter is able to evaluate the property name?

        // TODO: not sure why AttributePropertyHandler is not catching up at expression
        // evaluation
        // Feature feature = (Feature)features.iterator().next();
        // String obtainedValue = (String) propertyName.evaluate(feature);
        // assertNotNull(obtainedValue);
        // assertEquals(queryLiteral, obtainedValue);
    }

    /** Grab a feature and traverse it in deep as an encoder might do */
    @Test
    public void testTraverseDeep() throws Exception {
        final FeatureSource<FeatureType, Feature> fSource = mappingDataStore.getFeatureSource(typeName);
        NamespaceSupport namespaces = new NamespaceSupport();
        namespaces.declarePrefix("sa", SANS);
        namespaces.declarePrefix("geo", GEONS);
        namespaces.declarePrefix("xlink", XLINK.NAMESPACE);

        FeatureCollection features = fSource.getFeatures();
        Feature f = features.features().next();
        traverse(f);
    }

    private void traverse(Attribute f) {
        Object value = f.getValue();
        if (f instanceof ComplexAttribute) {
            Collection values = (Collection) value;
            for (Object o : values) {
                Attribute att = (Attribute) o;
                assertNotNull(att);
                traverse(att);
            }
        }
    }
}
