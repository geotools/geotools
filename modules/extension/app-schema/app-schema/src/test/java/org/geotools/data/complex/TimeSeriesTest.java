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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.DataAccessFinder;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.feature.Attribute;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.ComplexType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.appschema.filter.FilterFactoryImplNamespaceAware;
import org.geotools.data.DataUtilities;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator;
import org.geotools.data.complex.config.AppSchemaDataAccessDTO;
import org.geotools.data.complex.config.AppSchemaFeatureTypeRegistry;
import org.geotools.data.complex.config.XMLConfigDigester;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.data.complex.util.EmfComplexFeatureReader;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.gml3.GML;
import org.geotools.test.AppSchemaTestSupport;
import org.geotools.xlink.XLINK;
import org.geotools.xsd.SchemaIndex;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * @author Rob Atkinson
 * @version $Id$
 * @since 2.4
 */
public class TimeSeriesTest extends AppSchemaTestSupport {
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(TimeSeriesTest.class);

    private static final String AWNS = "http://www.water.gov.au/awdip";

    private static final String CVNS = "http://www.opengis.net/cv/0.2.1";

    private static final String SANS = "http://www.opengis.net/sampling/1.0";

    private static final String OMNS = "http://www.opengis.net/om/1.0";

    private static final String SWENS = "http://www.opengis.net/swe/1.0.1";

    private static final String GMLNS = "http://www.opengis.net/gml";

    // private static final String GEONS =
    // "http://www.seegrid.csiro.au/xml/geometry";

    final String schemaBase = "/test-data/";

    EmfComplexFeatureReader reader;

    /** */
    @Before
    public void setUp() throws Exception {
        reader = EmfComplexFeatureReader.newInstance();

        // Logging.GEOTOOLS.forceMonolineConsoleOutput(Level.FINEST);
    }

    /** @param location schema location path discoverable through getClass().getResource() */
    private SchemaIndex loadSchema(URL location) throws IOException {
        URL catalogLocation = getClass().getResource(schemaBase + "observations.oasis.xml");
        reader.setResolver(catalogLocation);
        return reader.parse(location);
    }

    /**
     * Tests if the schema-to-FM parsing code developed for complex datastore configuration loading can parse the
     * GeoSciML types
     */
    @Test
    public void testParseSchema() throws Exception {
        SchemaIndex schemaIndex;
        try {
            String schemaLocation = schemaBase + "commonSchemas_new/awdip.xsd";
            URL location = getClass().getResource(schemaLocation);
            assertNotNull(location);
            schemaIndex = loadSchema(location);
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            throw e;
        }

        final AppSchemaFeatureTypeRegistry typeRegistry = new AppSchemaFeatureTypeRegistry();
        try {
            typeRegistry.addSchemas(schemaIndex);

            final Name typeName = Types.typeName(AWNS, "SiteSinglePhenomTimeSeriesType");
            final ComplexType testType = (ComplexType) typeRegistry.getAttributeType(typeName);

            assertNotNull(testType);
            assertTrue(testType instanceof FeatureType);

            AttributeType superType = testType.getSuper();
            assertNotNull(superType);

            Name superTypeName = Types.typeName(AWNS, "SamplingSitePurposeType");
            assertEquals(superTypeName, superType.getName());
            // assertTrue(superType instanceof FeatureType);

            // ensure all needed types were parsed and aren't just empty proxies
            Map<Name, Name> samplingProperties = new HashMap<>();

            // from gml:AbstractFeatureType
            samplingProperties.put(name(GMLNS, "metaDataProperty"), typeName(GMLNS, "MetaDataPropertyType"));
            samplingProperties.put(name(GMLNS, "description"), typeName(GMLNS, "StringOrRefType"));
            samplingProperties.put(name(GMLNS, "name"), typeName(GMLNS, "CodeType"));
            samplingProperties.put(name(GMLNS, "boundedBy"), typeName(GMLNS, "BoundingShapeType"));
            samplingProperties.put(name(GMLNS, "location"), typeName(GMLNS, "LocationPropertyType"));

            // aw:SamplingSiteType
            samplingProperties.put(name(AWNS, "samplingRegimeType"), Types.toTypeName(GML.CodeType));
            samplingProperties.put(name(AWNS, "waterBodyType"), Types.toTypeName(GML.CodeType));
            samplingProperties.put(name(AWNS, "accessTypeCode"), Types.toTypeName(GML.CodeType));

            // sa:SamplingPointType
            samplingProperties.put(name(SANS, "position"), typeName(GMLNS, "PointPropertyType"));

            // sa:SamplingFeatureType
            samplingProperties.put(name(SANS, "relatedObservation"), typeName(OMNS, "ObservationPropertyType"));
            samplingProperties.put(
                    name(SANS, "relatedSamplingFeature"), typeName(SANS, "SamplingFeatureRelationPropertyType"));
            samplingProperties.put(name(SANS, "sampledFeature"), typeName(GMLNS, "FeaturePropertyType"));
            samplingProperties.put(name(SANS, "surveyDetails"), typeName(SANS, "SurveyProcedurePropertyType"));

            // sa:SiteSinglePhenomTimeSeriesType
            samplingProperties.put(
                    name(AWNS, "relatedObservation"), typeName(AWNS, "PhenomenonTimeSeriesPropertyType"));

            assertPropertyNamesAndTypeNames(testType, samplingProperties);

            AttributeDescriptor relatedObservation =
                    (AttributeDescriptor) Types.descriptor(testType, name(AWNS, "relatedObservation"));
            Map<Name, Name> relatedObsProps = new HashMap<>();
            relatedObsProps.put(name(AWNS, "PhenomenonTimeSeries"), typeName(AWNS, "PhenomenonTimeSeriesType"));
            ComplexType phenomenonTimeSeriesPropertyType = (ComplexType) relatedObservation.getType();

            assertPropertyNamesAndTypeNames(phenomenonTimeSeriesPropertyType, relatedObsProps);

            AttributeDescriptor phenomenonTimeSeries = (AttributeDescriptor)
                    Types.descriptor(phenomenonTimeSeriesPropertyType, name(AWNS, "PhenomenonTimeSeries"));
            ComplexType phenomenonTimeSeriesType = (ComplexType) phenomenonTimeSeries.getType();
            Map<Name, Name> phenomenonTimeSeriesProps = new HashMap<>();
            // from
            // aw:WaterObservationType/om:TimeSeriesObsType/om:AbstractObservationType
            // phenomenonTimeSeriesProps.put(name(OMNS, "procedure"), typeName(OMNS,
            // "ObservationProcedurePropertyType"));
            // phenomenonTimeSeriesProps.put(name(OMNS, "countParameter"),
            // typeName(SWENS,
            // "TypedCountType"));
            // phenomenonTimeSeriesProps.put(name(OMNS, "measureParameter"),
            // typeName(SWENS,
            // "TypedMeasureType"));
            // phenomenonTimeSeriesProps.put(name(OMNS, "termParameter"),
            // typeName(SWENS,
            // "TypedCategoryType"));
            // phenomenonTimeSeriesProps.put(name(OMNS, "observedProperty"),
            // typeName(SWENS,
            // "PhenomenonPropertyType"));
            //
            // from PhenomenonTimeSeriesType
            phenomenonTimeSeriesProps.put(
                    name(AWNS, "result"), typeName(CVNS, "CompactDiscreteTimeCoveragePropertyType"));

            assertPropertyNamesAndTypeNames(phenomenonTimeSeriesType, phenomenonTimeSeriesProps);

            AttributeDescriptor observedProperty =
                    (AttributeDescriptor) Types.descriptor(phenomenonTimeSeriesType, name(OMNS, "observedProperty"));

            ComplexType phenomenonPropertyType = (ComplexType) observedProperty.getType();

            assertPropertyNamesAndTypeNames(
                    phenomenonPropertyType,
                    Collections.singletonMap(name(SWENS, "Phenomenon"), typeName(SWENS, "PhenomenonType")));

            AttributeDescriptor phenomenon =
                    (AttributeDescriptor) Types.descriptor(phenomenonPropertyType, name(SWENS, "Phenomenon"));
            ComplexType phenomenonType = (ComplexType) phenomenon.getType();
            assertNotNull(phenomenonType.getSuper());
            assertEquals(
                    typeName(GMLNS, "DefinitionType"), phenomenonType.getSuper().getName());

            Map<Name, Name> phenomenonProps = new HashMap<>();
            // from gml:DefinitionType
            phenomenonProps.put(name(GMLNS, "metaDataProperty"), null);
            phenomenonProps.put(name(GMLNS, "description"), null);
            phenomenonProps.put(name(GMLNS, "name"), null);

            assertPropertyNamesAndTypeNames(phenomenonType, phenomenonProps);
        } finally {
            typeRegistry.disposeSchemaIndexes();
        }
    }

    private void assertPropertyNamesAndTypeNames(ComplexType parentType, Map expectedPropertiesAndTypes)
            throws Exception {

        for (Object o : expectedPropertiesAndTypes.entrySet()) {
            Entry entry = (Entry) o;
            Name dName = (Name) entry.getKey();
            Name expectedDescriptorTypeName = (Name) entry.getValue();

            AttributeDescriptor d = (AttributeDescriptor) Types.descriptor(parentType, dName);
            assertNotNull("Descriptor " + dName + " not found for type " + parentType.getName(), d);
            AttributeType type;
            try {
                type = d.getType();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "type not parsed for " + d.getName(), e);
                throw e;
            }
            assertNotNull(type);
            Name actualTypeName = type.getName();
            assertNotNull(actualTypeName);
            assertNotNull(type.getBinding());
            if (expectedDescriptorTypeName != null) {
                assertEquals("type mismatch for property " + dName, expectedDescriptorTypeName, actualTypeName);
            }
        }
    }

    private Name typeName(String ns, String localName) {
        return Types.typeName(ns, localName);
    }

    private Name name(String ns, String localName) {
        return Types.typeName(ns, localName);
    }

    @Test
    public void testLoadMappingsConfig() throws Exception {
        XMLConfigDigester reader = new XMLConfigDigester();
        String configLocation = schemaBase + "TimeSeriesTest_properties.xml";
        URL url = getClass().getResource(configLocation);

        // configLocation =
        // "file:/home/gabriel/svn/geoserver/trunk/configuration/community-schema-timeseries2/TimeSeriesTest_properties.xml";
        // URL url = new URL(configLocation);

        AppSchemaDataAccessDTO config = reader.parse(url);

        Set mappings = AppSchemaDataAccessConfigurator.buildMappings(config);

        assertNotNull(mappings);
        assertEquals(1, mappings.size());

        FeatureTypeMapping mapping = (FeatureTypeMapping) mappings.iterator().next();

        AttributeDescriptor targetFeature = mapping.getTargetFeature();
        assertNotNull(targetFeature);
        assertNotNull(targetFeature.getType());
        assertEquals(AWNS, targetFeature.getName().getNamespaceURI());
        assertEquals("SiteSinglePhenomTimeSeries", targetFeature.getName().getLocalPart());

        List attributeMappings = mapping.getAttributeMappings();
        AttributeMapping attMapping = (AttributeMapping) attributeMappings.get(0);
        assertNotNull(attMapping);
        assertEquals(
                "aw:SiteSinglePhenomTimeSeries", attMapping.getTargetXPath().toString());

        attMapping = (AttributeMapping) attributeMappings.get(1);
        assertNotNull(attMapping);
        assertEquals("gml:name[1]", attMapping.getTargetXPath().toString());

        attMapping = (AttributeMapping) attributeMappings.get(2);
        assertNotNull(attMapping);
        assertEquals("sa:sampledFeature", attMapping.getTargetXPath().toString());
        // this mapping has no source expression, just client properties
        assertSame(Expression.NIL, attMapping.getSourceExpression());
        assertSame(Expression.NIL, attMapping.getIdentifierExpression());
        Map clientProperties = attMapping.getClientProperties();
        assertEquals(2, clientProperties.size());

        Name clientPropName = name(XLINK.NAMESPACE, "title");
        assertTrue("client property " + clientPropName + " not found", clientProperties.containsKey(clientPropName));
        clientPropName = name(XLINK.NAMESPACE, "href");
        assertTrue("client property " + clientPropName + " not found", clientProperties.containsKey(clientPropName));

        // now test the use of specific subtype overriding a general node type
        attMapping = (AttributeMapping) attributeMappings.get(5);
        assertNotNull(attMapping);
        String expected = "aw:relatedObservation/aw:PhenomenonTimeSeries/om:observedProperty/swe:Phenomenon/gml:name";
        String actual = attMapping.getTargetXPath().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testDataStore() throws Exception {
        DataAccess<FeatureType, Feature> mappingDataStore;
        final Name typeName = new NameImpl(AWNS, "SiteSinglePhenomTimeSeries");
        {
            final Map<String, Serializable> dsParams = new HashMap<>();

            String configLocation = schemaBase + "TimeSeriesTest_properties.xml";
            final URL url = getClass().getResource(configLocation);
            // configLocation =
            // "file:/home/gabriel/svn/geoserver/trunk/configuration/community-schema-timeseries2/TimeSeriesTest_properties.xml";
            // URL url = new URL(configLocation);

            dsParams.put("dbtype", "app-schema");
            dsParams.put("url", url.toExternalForm());

            mappingDataStore = DataAccessFinder.getDataStore(dsParams);
        }
        assertNotNull(mappingDataStore);
        FeatureSource<FeatureType, Feature> fSource;
        {
            FeatureType fType = mappingDataStore.getSchema(typeName);
            assertNotNull(fType);
            fSource = mappingDataStore.getFeatureSource(typeName);
        }
        FeatureCollection<FeatureType, Feature> features;
        // make a getFeatures request with a nested properties filter.
        //
        // was 96, but now 3 as mapped features are grouped by id
        final int EXPECTED_MAPPED_FEATURE_COUNT = 3;
        // 96 data rows from property file
        final int EXPECTED_SIMPLE_FEATURE_COUNT = 96;
        {
            features = fSource.getFeatures();

            int resultCount = DataUtilities.count(features);
            String msg = "be sure difference in result count is not due to different dataset.";
            assertEquals(msg, EXPECTED_MAPPED_FEATURE_COUNT, resultCount);
        }

        Feature feature;
        int count = 0;

        FilterFactory ffac;
        {
            NamespaceSupport namespaces = new NamespaceSupport();
            namespaces.declarePrefix("aw", AWNS);
            namespaces.declarePrefix("om", OMNS);
            namespaces.declarePrefix("swe", SWENS);
            namespaces.declarePrefix("gml", GMLNS);
            namespaces.declarePrefix("sa", SANS);
            namespaces.declarePrefix("cv", CVNS);
            // TODO: use commonfactoryfinder or the mechanism choosed
            // to pass namespace context to filter factory
            ffac = new FilterFactoryImplNamespaceAware(namespaces);
        }

        final String phenomNamePath =
                "aw:relatedObservation/aw:PhenomenonTimeSeries/om:observedProperty/swe:Phenomenon/gml:name";
        try (FeatureIterator it = features.features()) {
            while (it.hasNext()) {
                feature = it.next();
                count++;
                {
                    PropertyName gmlName = ffac.property("gml:name");
                    PropertyName phenomName = ffac.property(phenomNamePath);

                    Object nameVal = gmlName.evaluate(feature, String.class);
                    assertNotNull("gml:name evaluated to null", nameVal);

                    Object phenomNameVal = phenomName.evaluate(feature, String.class);
                    assertNotNull(phenomNamePath + " evaluated to null", phenomNameVal);
                }
                {
                    PropertyName sampledFeatureName = ffac.property("sa:sampledFeature");
                    Attribute sampledFeatureVal = (Attribute) sampledFeatureName.evaluate(feature);
                    assertNotNull("sa:sampledFeature evaluated to null", sampledFeatureVal);
                    assertEquals(0, ((Collection) sampledFeatureVal.getValue()).size());
                    Map attributes = (Map) sampledFeatureVal.getUserData().get(Attributes.class);
                    assertNotNull(attributes);
                    Name xlinkTitle = name(XLINK.NAMESPACE, "title");
                    assertTrue(attributes.containsKey(xlinkTitle));
                    assertNotNull(attributes.get(xlinkTitle));

                    Name xlinkHref = name(XLINK.NAMESPACE, "href");
                    assertTrue(attributes.containsKey(xlinkHref));
                    assertNotNull(attributes.get(xlinkHref));
                }

                {
                    final String elementPath =
                            "aw:relatedObservation/aw:PhenomenonTimeSeries/om:result/cv:CompactDiscreteTimeCoverage";
                    PropertyName elementName = ffac.property(elementPath);
                    Object timeCovVal = elementName.evaluate(feature);
                    assertNotNull(elementPath, timeCovVal);
                    assertTrue(timeCovVal instanceof Feature);
                    final List elements = (List) ((Feature) timeCovVal).getValue();
                    assertEquals(1, elements.size());
                }
            }
        }

        count = 0;
        try (FeatureIterator<? extends Feature> simpleIterator =
                ((AbstractMappingFeatureIterator) features.features()).getSourceFeatureIterator()) {
            while (simpleIterator.hasNext()) {
                feature = simpleIterator.next();
                count++;

                if (count == 22) {
                    String compactTimeValuePairName = "result";
                    String geomName = "sample_time_position";

                    Collection compactTimes = feature.getProperties(compactTimeValuePairName);
                    assertNotNull(compactTimes);
                    assertEquals(1, compactTimes.size());

                    Attribute value = (Attribute) compactTimes.iterator().next();
                    assertNotNull(value.getValue());

                    Collection geomProperties = feature.getProperties(geomName);
                    assertNotNull(geomProperties);
                    assertEquals(1, geomProperties.size());

                    Attribute geom = (Attribute) geomProperties.iterator().next();
                    assertNotNull(geom.getValue());

                    Object valueContent = geom.getValue();
                    Date sampleTimePosition = (Date) valueContent;
                    Calendar cal = Calendar.getInstance();
                    // property file dates appear to be parsed as being in UTC
                    cal.setTimeZone(TimeZone.getTimeZone("UTC"));
                    cal.setTime(sampleTimePosition);
                    // see row TS2.22
                    assertEquals(2007, cal.get(Calendar.YEAR));
                    assertEquals(Calendar.JANUARY, cal.get(Calendar.MONTH));
                    assertEquals(21, cal.get(Calendar.DAY_OF_MONTH));
                    // sanity (timezone handling has been bungled one time too many)
                    assertEquals(0, cal.get(Calendar.HOUR_OF_DAY));
                    assertEquals(0, cal.get(Calendar.MINUTE));
                    assertEquals(0, cal.get(Calendar.SECOND));
                }
            }

            mappingDataStore.dispose();

            assertEquals(EXPECTED_SIMPLE_FEATURE_COUNT, count);
        }
    }
}
