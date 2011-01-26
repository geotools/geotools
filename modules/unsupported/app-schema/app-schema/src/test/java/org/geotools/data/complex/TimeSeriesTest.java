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

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator;
import org.geotools.data.complex.config.AppSchemaDataAccessDTO;
import org.geotools.data.complex.config.EmfAppSchemaReader;
import org.geotools.data.complex.config.FeatureTypeRegistry;
import org.geotools.data.complex.config.XMLConfigDigester;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.feature.Types;
import org.geotools.filter.FilterFactoryImplNamespaceAware;
import org.geotools.gml3.GML;
import org.geotools.xlink.XLINK;
import org.geotools.xml.SchemaIndex;
import org.opengis.feature.Attribute;
import org.opengis.feature.Feature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * DOCUMENT ME!
 * 
 * @author Rob Atkinson
 * @version $Id$
 * @source $URL$
 * @since 2.4
 */
public class TimeSeriesTest extends TestCase {
    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(TimeSeriesTest.class.getPackage().getName());

    private static final String AWNS = "http://www.water.gov.au/awdip";

    private static final String CVNS = "http://www.opengis.net/cv/0.2.1";

    private static final String SANS = "http://www.opengis.net/sampling/1.0";

    private static final String OMNS = "http://www.opengis.net/om/1.0";

    private static final String SWENS = "http://www.opengis.net/swe/1.0.1";

    private static final String GMLNS = "http://www.opengis.net/gml";

    // private static final String GEONS =
    // "http://www.seegrid.csiro.au/xml/geometry";

    final String schemaBase = "/test-data/";

    EmfAppSchemaReader reader;

    private FeatureSource<FeatureType, Feature> source;

    /**
     * DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    protected void setUp() throws Exception {
        super.setUp();
        reader = EmfAppSchemaReader.newInstance();

        // Logging.GEOTOOLS.forceMonolineConsoleOutput(Level.FINEST);
    }

    /**
     * DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * DOCUMENT ME!
     * 
     * @param location
     *            schema location path discoverable through getClass().getResource()
     * @return 
     * 
     * @throws IOException
     *             DOCUMENT ME!
     */
    private SchemaIndex loadSchema(URL location) throws IOException {
        URL catalogLocation = getClass().getResource(schemaBase + "observations.oasis.xml");
        reader.setResolver(catalogLocation);
        return reader.parse(location);
    }

    /**
     * Tests if the schema-to-FM parsing code developed for complex datastore configuration loading
     * can parse the GeoSciML types
     * 
     * @throws Exception
     */
    public void testParseSchema() throws Exception {
        SchemaIndex schemaIndex;
        try {
            String schemaLocation = schemaBase + "commonSchemas_new/awdip.xsd";
            URL location = getClass().getResource(schemaLocation);
            assertNotNull(location);
            schemaIndex = loadSchema(location);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        final FeatureTypeRegistry typeRegistry = new FeatureTypeRegistry();
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
        Map samplingProperties = new HashMap();

        // from gml:AbstractFeatureType
        samplingProperties.put(name(GMLNS, "metaDataProperty"), typeName(GMLNS,
                "MetaDataPropertyType"));
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
        samplingProperties.put(name(SANS, "relatedObservation"), typeName(OMNS,
                "ObservationPropertyType"));
        samplingProperties.put(name(SANS, "relatedSamplingFeature"), typeName(SANS,
                "SamplingFeatureRelationPropertyType"));
        samplingProperties
                .put(name(SANS, "sampledFeature"), typeName(GMLNS, "FeaturePropertyType"));
        samplingProperties.put(name(SANS, "surveyDetails"), typeName(SANS,
                "SurveyProcedurePropertyType"));

        // sa:SiteSinglePhenomTimeSeriesType
        samplingProperties.put(name(AWNS, "relatedObservation"), typeName(AWNS,
                "PhenomenonTimeSeriesPropertyType"));

        assertPropertyNamesAndTypeNames(testType, samplingProperties);

        AttributeDescriptor relatedObservation = (AttributeDescriptor) Types.descriptor(testType,
                name(AWNS, "relatedObservation"));
        Map relatedObsProps = new HashMap();
        relatedObsProps.put(name(AWNS, "PhenomenonTimeSeries"), typeName(AWNS,
                "PhenomenonTimeSeriesType"));
        ComplexType phenomenonTimeSeriesPropertyType = (ComplexType) relatedObservation.getType();

        assertPropertyNamesAndTypeNames(phenomenonTimeSeriesPropertyType, relatedObsProps);

        AttributeDescriptor phenomenonTimeSeries = (AttributeDescriptor) Types.descriptor(
                phenomenonTimeSeriesPropertyType, name(AWNS, "PhenomenonTimeSeries"));
        ComplexType phenomenonTimeSeriesType = (ComplexType) phenomenonTimeSeries.getType();
        Map phenomenonTimeSeriesProps = new HashMap();
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
        phenomenonTimeSeriesProps.put(name(AWNS, "result"), typeName(CVNS,
                "CompactDiscreteTimeCoveragePropertyType"));

        assertPropertyNamesAndTypeNames(phenomenonTimeSeriesType, phenomenonTimeSeriesProps);

        AttributeDescriptor observedProperty = (AttributeDescriptor) Types.descriptor(
                phenomenonTimeSeriesType, name(OMNS, "observedProperty"));

        ComplexType phenomenonPropertyType = (ComplexType) observedProperty.getType();

        assertPropertyNamesAndTypeNames(phenomenonPropertyType, Collections.singletonMap(name(
                SWENS, "Phenomenon"), typeName(SWENS, "PhenomenonType")));

        AttributeDescriptor phenomenon = (AttributeDescriptor) Types.descriptor(
                phenomenonPropertyType, name(SWENS, "Phenomenon"));
        ComplexType phenomenonType = (ComplexType) phenomenon.getType();
        assertNotNull(phenomenonType.getSuper());
        assertEquals(typeName(GMLNS, "DefinitionType"), phenomenonType.getSuper().getName());

        Map phenomenonProps = new HashMap();
        // from gml:DefinitionType
        phenomenonProps.put(name(GMLNS, "metaDataProperty"), null);
        phenomenonProps.put(name(GMLNS, "description"), null);
        phenomenonProps.put(name(GMLNS, "name"), null);

        assertPropertyNamesAndTypeNames(phenomenonType, phenomenonProps);
    }

    private void assertPropertyNamesAndTypeNames(ComplexType parentType,
            Map expectedPropertiesAndTypes) throws Exception {

        for (Iterator it = expectedPropertiesAndTypes.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Entry) it.next();
            Name dName = (Name) entry.getKey();
            Name expectedDescriptorTypeName = (Name) entry.getValue();

            AttributeDescriptor d = (AttributeDescriptor) Types.descriptor(parentType, dName);
            assertNotNull("Descriptor " + dName + " not found for type " + parentType.getName(), d);
            AttributeType type;
            try {
                type = (AttributeType) d.getType();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "type not parsed for "
                        + ((AttributeDescriptor) d).getName(), e);
                throw e;
            }
            assertNotNull(type);
            Name actualTypeName = type.getName();
            assertNotNull(actualTypeName);
            assertNotNull(type.getBinding());
            if (expectedDescriptorTypeName != null) {
                assertEquals("type mismatch for property " + dName, expectedDescriptorTypeName,
                        actualTypeName);
            }
        }
    }

    private Name typeName(String ns, String localName) {
        return Types.typeName(ns, localName);
    }

    private Name name(String ns, String localName) {
        return Types.typeName(ns, localName);
    }

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
        assertEquals("aw:SiteSinglePhenomTimeSeries", attMapping.getTargetXPath().toString());

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
        assertTrue("client property " + clientPropName + " not found", clientProperties
                .containsKey(clientPropName));
        clientPropName = name(XLINK.NAMESPACE, "href");
        assertTrue("client property " + clientPropName + " not found", clientProperties
                .containsKey(clientPropName));

        // now test the use of specific subtype overriding a general node type
        attMapping = (AttributeMapping) attributeMappings.get(5);
        assertNotNull(attMapping);
        String expected = "aw:relatedObservation/aw:PhenomenonTimeSeries/om:observedProperty/swe:Phenomenon/gml:name";
        String actual = attMapping.getTargetXPath().toString();
        assertEquals(expected, actual);
    }

    public void testDataStore() throws Exception {
        DataAccess<FeatureType, Feature> mappingDataStore;
        final Name typeName = new NameImpl(AWNS, "SiteSinglePhenomTimeSeries");
        {
            final Map dsParams = new HashMap();

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
        FeatureCollection features;
        // make a getFeatures request with a nested properties filter.
        //
        // was 96, but now 3 as mapped features are grouped by id
        final int EXPECTED_MAPPED_FEATURE_COUNT = 3;
        // 96 data rows from property file
        final int EXPECTED_SIMPLE_FEATURE_COUNT = 96;
        {
            features = fSource.getFeatures();

            int resultCount = getCount(features);
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

        final String phenomNamePath = "aw:relatedObservation/aw:PhenomenonTimeSeries/om:observedProperty/swe:Phenomenon/gml:name";
        FeatureIterator it = features.features();
        for (; it.hasNext();) {
            feature = (Feature) it.next();
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
                final String elementPath = "aw:relatedObservation/aw:PhenomenonTimeSeries/om:result/cv:CompactDiscreteTimeCoverage";
                PropertyName elementName = ffac.property(elementPath);
                Object timeCovVal = elementName.evaluate(feature);
                assertNotNull(elementPath, timeCovVal);
                assertTrue(timeCovVal instanceof Feature);
                final List elements = (List) ((Feature) timeCovVal).getValue();
                assertEquals(1, elements.size());
            }

        }
        it.close();

        count = 0;
        Iterator<Feature> simpleIterator = ((AbstractMappingFeatureIterator) features.features()).getSourceFeatureIterator();
        for (; simpleIterator.hasNext();) {
            feature = (Feature) simpleIterator.next();
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
