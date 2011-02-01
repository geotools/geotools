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

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureImpl;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.Types;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.gml3.bindings.GML3EncodingUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.xml.sax.Attributes;

import com.vividsolutions.jts.util.Stopwatch;

/**
 * This is the tests for feature chaining; nesting complex attributes (feature and non-feature)
 * inside another complex attribute.
 * 
 * @author Rini Angreani, Curtin University of Technology
 *
 * @source $URL$
 */
public class FeatureChainingTest {
    static final String GSMLNS = "urn:cgi:xmlns:CGI:GeoSciML:2.0";

    static final String GMLNS = "http://www.opengis.net/gml";

    static final Name MAPPED_FEATURE_TYPE = Types.typeName(GSMLNS, "MappedFeatureType");

    static final Name MAPPED_FEATURE = Types.typeName(GSMLNS, "MappedFeature");

    static final Name GEOLOGIC_UNIT_TYPE = Types.typeName(GSMLNS, "GeologicUnitType");

    static final Name GEOLOGIC_UNIT = Types.typeName(GSMLNS, "GeologicUnit");
    
    static final Name GEOLOGIC_UNIT_NAME = Types.typeName("myGeologicUnit");

    static final Name COMPOSITION_PART_TYPE = Types.typeName(GSMLNS, "CompositionPartType");

    static final Name COMPOSITION_PART = Types.typeName(GSMLNS, "CompositionPart");

    static final Name CGI_TERM_VALUE = Types.typeName(GSMLNS, "CGI_TermValue");

    static final Name CGI_TERM_VALUE_TYPE = Types.typeName(GSMLNS, "CGI_TermValueType");

    static final Name CONTROLLED_CONCEPT = Types.typeName(GSMLNS, "ControlledConcept");

    static FilterFactory ff = new FilterFactoryImpl(null);

    /**
     * Map of geological unit values to mapped feature objects based on
     * mappedFeaturePropertyFile.properties
     */
    final static Map<String, String> mfToGuMap = new HashMap<String, String>() {
        {
            put("mf1", "gu.25699");
            put("mf2", "gu.25678");
            put("mf3", "gu.25678");
            put("mf4", "gu.25682");
        }
    };

    /**
     * Map of compositional part values to geological unit objects based on geologicUnit.properties
     */
    final static Map<String, String> guToCpMap = new HashMap<String, String>() {
        {
            put("gu.25699", "cp.167775491936278899");
            put("gu.25678", "cp.167775491936278844;cp.167775491936278856");
            put("gu.25682", "cp.167775491936278812");
        }
    };

    /**
     * Map of exposure colour values to geological unit objects based on geologicUnit.properties
     */
    final static Map<String, String> guToExposureColorMap = new HashMap<String, String>() {
        {
            put("gu.25699", "Blue");
            put("gu.25678", "Yellow;Blue");
            put("gu.25682", "Red");
        }
    };

    /**
     * Map of out crop character values to geological unit objects based on geologicUnit.properties
     */
    static Map<String, String> guToOutcropCharacterMap = new HashMap<String, String>() {
        {
            put("gu.25699", "x");
            put("gu.25678", "x;y");
            put("gu.25682", "z");

        }
    };

    private static final String schemaBase = "/test-data/";

    private static FeatureSource<FeatureType, Feature> mfSource;

    /**
     * Generated mapped features
     */
    private static FeatureCollection<FeatureType, Feature> mfFeatures;

    /**
     * Generated geological unit features
     */
    private static FeatureCollection<FeatureType, Feature> guFeatures;

    /**
     * Generated compositional part fake "features"
     */
    private static FeatureCollection<FeatureType, Feature> cpFeatures;

    /**
     * Generated controlled concept fake "features"
     */
    private static FeatureCollection<FeatureType, Feature> ccFeatures;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Stopwatch sw = new Stopwatch();
        sw.start();
        loadDataAccesses();
        sw.stop();
        System.out.println("Set up time: " + sw.getTimeString());
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        DataAccessRegistry.unregisterAll();
    }

    /**
     * Test that chaining works
     * 
     * @throws Exception
     */
    @Test
    public void testFeatureChaining() throws Exception {
        FeatureIterator<Feature> mfIterator = mfFeatures.features();

        FeatureIterator<Feature> guIterator = guFeatures.features();

        // Extract all geological unit features into a map by id
        Map<String, Feature> guMap = new HashMap<String, Feature>();
        Feature guFeature;
        while (guIterator.hasNext()) {
            guFeature = (Feature) guIterator.next();
            String guId = guFeature.getIdentifier().getID();
            if (!guMap.containsKey(guId)) {
                guMap.put(guId, guFeature);
            }
        }

        // Extract all compositional part "features" into a map by id
        FeatureIterator<Feature> cpIterator = cpFeatures.features();
        Map<String, Feature> cpMap = new HashMap<String, Feature>();
        Feature cpFeature;
        while (cpIterator.hasNext()) {
            cpFeature = (Feature) cpIterator.next();
            String cpId = cpFeature.getIdentifier().getID();
            if (!cpMap.containsKey(cpId)) {
                cpMap.put(cpId, cpFeature);
            }
        }

        Feature mfFeature;
        Collection<Property> nestedGuFeatures;
        String guId;
        final String NESTED_LINK = "specification";
        Collection<Property> nestedCpFeatures;
        String cpId;
        while (mfIterator.hasNext()) {
            mfFeature = (Feature) mfIterator.next();
            String mfId = mfFeature.getIdentifier().toString();
            String[] guIds = this.mfToGuMap.get(mfId).split(";");

            // make sure we have the right number of nested features
            nestedGuFeatures = (Collection<Property>) mfFeature.getProperties(NESTED_LINK);
            assertEquals(guIds.length, nestedGuFeatures.size());

            ArrayList<String> nestedGuIds = new ArrayList<String>();

            for (Property property : nestedGuFeatures) {
                Object value = property.getValue();
                assertNotNull(value);
                assertTrue(value instanceof Collection);
                assertEquals(1, ((Collection) value).size());

                Feature nestedGuFeature = (Feature) ((Collection) value).iterator().next();
                /**
                 * Test geological unit
                 */
                // make sure each of the nested geologic unit is valid
                guId = nestedGuFeature.getIdentifier().toString();
                assertTrue(guMap.containsKey(guId));

                nestedGuIds.add(guId);

                // make sure the nested geologic unit feature has the right properties
                guFeature = guMap.get(guId.toString());
                Collection<Property> guProperties = guFeature.getProperties();
                assertEquals(nestedGuFeature.getProperties(), guProperties);

                /**
                 * Test compositional part
                 */
                // make sure the right number of nested features are there
                String[] cpIds = this.guToCpMap.get(guId).split(";");
                nestedCpFeatures = (Collection<Property>) guFeature.getProperties("composition");
                assertEquals(cpIds.length, nestedCpFeatures.size());

                ArrayList<String> nestedCpIds = new ArrayList<String>();
                for (Property cpProperty : nestedCpFeatures) {
                    Object cpPropertyValue = cpProperty.getValue();
                    assertNotNull(cpPropertyValue);
                    assertTrue(cpPropertyValue instanceof Collection);
                    assertEquals(1, ((Collection) cpPropertyValue).size());

                    Feature nestedCpFeature = (Feature) ((Collection) cpPropertyValue).iterator()
                            .next();
                    // make sure each of the nested compositional part feature is valid
                    cpId = nestedCpFeature.getIdentifier().toString();
                    assertTrue(cpMap.containsKey(cpId));

                    nestedCpIds.add(cpId);

                    // make sure each of the nested compositional part has the right properties
                    cpFeature = cpMap.get(cpId.toString());
                    Collection<Property> cpProperties = cpFeature.getProperties();
                    assertEquals(nestedCpFeature.getProperties(), cpProperties);

                }
                // make sure all the nested compositional part features are there
                assertTrue(nestedCpIds.containsAll(Arrays.asList(cpIds)));
            }
            // make sure all the nested geological unit features are there
            assertTrue(nestedGuIds.containsAll(Arrays.asList(guIds)));

        }
        mfIterator.close();
        guIterator.close();
        cpIterator.close();
    }

    /**
     * testFeatureChaining() tests one to many relationship, but the many side was on the chaining
     * side ie. geologic unit side (with many composition parts). This is to test that configuring
     * many on the the chained works. We're using composition part -> lithology here.
     * 
     * @throws Exception
     */
    @Test
    public void testManyOnChainedSide() throws Exception {

        final String LITHOLOGY = "lithology";
        // get controlled concept features on their own
        AbstractMappingFeatureIterator iterator = (AbstractMappingFeatureIterator) ccFeatures
                .features();
        int count = 0;
        Map<String, Feature> featureList = new HashMap<String, Feature>();
        try {
            while (iterator.hasNext()) {
                Feature f = iterator.next();
                featureList.put(f.getIdentifier().getID(), f);
                count++;
            }
        } finally {
            iterator.close();
        }
        assertEquals(5, count);

        FeatureIterator<Feature> cpIterator = cpFeatures.features();
        while (cpIterator.hasNext()) {
            Feature cpFeature = (Feature) cpIterator.next();
            Collection<Property> lithologies = cpFeature.getProperties(LITHOLOGY);
            if (cpFeature.getIdentifier().toString().equals("cp.167775491936278812")) {
                // see ControlledConcept.properties file:
                // _=NAME:String,COMPOSITION_ID:String
                // cc.1=name_a|cp.167775491936278812
                // cc.1=name_b|cp.167775491936278812
                // cc.1=name_c|cp.167775491936278812
                // cc.2=name_2|cp.167775491936278812
                assertEquals(2, ((Collection) lithologies).size());
                Collection<String> lithologyIds = new ArrayList<String>();
                for (Property lithologyProperty : lithologies) {
                    Feature nestedFeature = (Feature) ((Collection) lithologyProperty.getValue())
                            .iterator().next();
                    String fId = nestedFeature.getIdentifier().getID();
                    lithologyIds.add(fId);
                    Feature lithology = featureList.get(fId);
                    assertEquals(nestedFeature.getProperties(), lithology.getProperties());
                }
                assertTrue(featureList.keySet().containsAll(lithologyIds));
            } else {
                // lithology is required
                assertEquals(1, lithologies.size());
            }
        }
    }

    /**
     * Test nesting multiple multi valued properties. Both exposure color and outcrop character are
     * multi valued. By making sure that both are nested inside geological unit feature, it's
     * verified that nesting multiple multi valued properties is possible.
     * 
     * @throws Exception
     */
    @Test
    public void testMultipleMultiValuedProperties() throws Exception {
        FeatureIterator<Feature> guIterator = guFeatures.features();

        Feature guFeature;
        final String EXPOSURE_COLOR = "exposureColor";
        final String OUTCROP_CHARACTER = "outcropCharacter";
        while (guIterator.hasNext()) {
            guFeature = (Feature) guIterator.next();
            String guId = guFeature.getIdentifier().toString();
            ArrayList realValues = new ArrayList();

            /**
             * Test exposure color
             */
            Collection<Property> nestedTermValues = (Collection<Property>) guFeature
                    .getProperties(EXPOSURE_COLOR);
            // get exposure color property values from geological unit feature
            for (Property property : nestedTermValues) {
                Object value = property.getValue();
                assertNotNull(value);
                assertTrue(value instanceof Collection);
                assertEquals(1, ((Collection) value).size());

                Feature feature = (Feature) ((Collection) value).iterator().next();
                for (Property nestedProperty : feature.getProperties("value")) {
                    realValues.add(((Property) ((Collection) nestedProperty.getValue()).iterator()
                            .next()).getValue());
                }
            }

            // compares the values from the property file
            String[] values = this.guToExposureColorMap.get(guId).split(";");
            assertEquals(realValues.size(), values.length);
            assertTrue(realValues.containsAll(Arrays.asList(values)));

            /**
             * Test outcrop character
             */
            nestedTermValues = (Collection<Property>) guFeature.getProperties(OUTCROP_CHARACTER);
            realValues.clear();
            // get nested outcrop character values from geological unit feature
            for (Property property : nestedTermValues) {
                Object value = property.getValue();
                assertNotNull(value);
                assertTrue(value instanceof Collection);
                assertEquals(1, ((Collection) value).size());

                Feature feature = (Feature) ((Collection) value).iterator().next();
                for (Property nestedProperty : feature.getProperties("value")) {
                    realValues.add(((Property) ((Collection) nestedProperty.getValue()).iterator()
                            .next()).getValue());
                }
            }
            // compare with values from property file
            values = this.guToOutcropCharacterMap.get(guId).split(";");
            assertEquals(realValues.size(), values.length);
            assertTrue(realValues.containsAll(Arrays.asList(values)));
        }
        guIterator.close();
    }

    /**
     * Test mapping multi-valued simple properties still works.
     * 
     * @throws Exception
     */
    @Test
    public void testMultiValuedSimpleProperties() throws Exception {
        FeatureIterator<Feature> iterator = ccFeatures.features();
        while (iterator.hasNext()) {
            Feature next = iterator.next();
            Collection<Property> names = next.getProperties("name");
            // these are gml:name and gsml:name, so count twice
            if (next.getIdentifier().toString().equals("cc.1")) {
                // see ControlledConcept.properties where id = cc.1
                assertEquals(6, names.size());
            } else {
                // see ControlledConcept.properties where id = cc.2
                assertEquals(2, names.size());
            }
        }
        iterator.close();
    }

    /**
     * Test filtering attributes on nested features.
     * 
     * @throws Exception
     */
    @Test
    public void testFilters() throws Exception {
        // make sure filter query can be made on MappedFeature based on GU properties
        //
        // <ogc:Filter>
        // <ogc:PropertyIsLike>
        // <ogc:PropertyName>
        // gsml:specification/gsml:GeologicUnit/gml:description
        // </ogc:PropertyName>
        // <ogc:Literal>Olivine basalt, tuff, microgabbro, minor sedimentary rocks</ogc:Literal>
        // </ogc:PropertyIsLike>
        // </ogc:Filter>

        Expression property = ff.property("gsml:specification/gsml:GeologicUnit/gml:description");
        Filter filter = ff.like(property,
                "Olivine basalt, tuff, microgabbro, minor sedimentary rocks");
        FeatureCollection<FeatureType, Feature> filteredResults = mfSource.getFeatures(filter);
        assertEquals(3, size(filteredResults));
        FeatureIterator<Feature> iterator = filteredResults.features();
        Feature feature = iterator.next();
        assertEquals("mf1", feature.getIdentifier().toString());
        feature = iterator.next();
        assertEquals("mf2", feature.getIdentifier().toString());
        feature = iterator.next();
        assertEquals("mf3", feature.getIdentifier().toString());
        iterator.close();

        /**
         * Test filtering on multi valued properties
         */
        FeatureSource<FeatureType, Feature> guSource = AppSchemaDataAccessRegistry.getFeatureSource(GEOLOGIC_UNIT_NAME);
        // composition part is a multi valued property
        // we're testing that we can get a geologic unit which has a composition part with a
        // significant proportion value
        property = ff
                .property("gsml:composition/gsml:CompositionPart/gsml:proportion/gsml:CGI_TermValue/gsml:value");
        filter = ff.equals(property, ff.literal("significant"));
        filteredResults = guSource.getFeatures(filter);
        assertEquals(2, size(filteredResults));
        iterator = filteredResults.features();
        feature = iterator.next();
        assertEquals("gu.25678", feature.getIdentifier().toString());
        feature = iterator.next();
        assertEquals("gu.25682", feature.getIdentifier().toString());
        iterator.close();

        /**
         * Test filtering client properties on chained features
         */
        property = ff.property("gsml:specification/gsml:GeologicUnit/gsml:occurrence/@xlink:href");
        filter = ff.like(property, "urn:cgi:feature:MappedFeature:mf1");
        filteredResults = mfSource.getFeatures(filter);
        assertEquals(1, size(filteredResults));
        feature = filteredResults.features().next();
        assertEquals("mf1", feature.getIdentifier().toString());

        /**
         * Test filtering on denormalised view, see GEOT-2927
         */
        property = ff.property("gml:name");
        filter = ff.equals(property, ff.literal("Yaugher Volcanic Group 2"));
        filteredResults = guSource.getFeatures(filter);
        assertEquals(1, size(filteredResults));
        // There are 2 rows for 1 feature that matches this filter:
        // gu.25678=-Py|Yaugher Volcanic Group 1
        // gu.25678=-Py|Yaugher Volcanic Group 2
        // Check that all 3 names are there:
        // - Yaugher Volcanic Group 1, Yaugher Volcanic Group 2 and -Py
        feature = filteredResults.features().next();
        assertEquals("gu.25678", feature.getIdentifier().toString());
        Collection<Property> properties = feature.getProperties(Types.typeName(GMLNS, "name"));
        assertTrue(properties.size() == 3);
        Iterator<Property> propIterator = properties.iterator();
        ComplexAttribute complexAttribute;
        Collection<? extends Property> values;
        // first
        complexAttribute = (ComplexAttribute) propIterator.next();
        values = complexAttribute.getValue();
        assertEquals(1, values.size());
        assertEquals("Yaugher Volcanic Group 1", 
                GML3EncodingUtils.getSimpleContent(complexAttribute));
        // second
        complexAttribute = (ComplexAttribute) propIterator.next();
        values = complexAttribute.getValue();
        assertEquals(1, values.size());
        assertEquals("Yaugher Volcanic Group 2", 
                GML3EncodingUtils.getSimpleContent(complexAttribute));
        // third
        complexAttribute = (ComplexAttribute) propIterator.next();
        values = complexAttribute.getValue();
        assertEquals(1, values.size());
        assertEquals("-Py", 
                GML3EncodingUtils.getSimpleContent(complexAttribute));
        /**
         * Same case as above, but the multi-valued property is feature chained
         */
        property = ff.property("gsml:exposureColor/gsml:CGI_TermValue/gsml:value");
        filter = ff.equals(property, ff.literal("Yellow"));
        filteredResults = guSource.getFeatures(filter);
        assertEquals(1, size(filteredResults));
        feature = filteredResults.features().next();
        // ensure it's the right feature
        assertEquals("gu.25678", feature.getIdentifier().toString());
        properties = feature.getProperties(Types.typeName(GSMLNS, "exposureColor"));
        assertTrue(properties.size() == 2);
        propIterator = properties.iterator();
        values = (Collection) propIterator.next().getValue();
        assertEquals(1, values.size());
        Feature cgiFeature = (Feature) values.iterator().next();
        // and that both gsml:exposureColor values from 2 denormalised view rows are there
        assertEquals("Yellow", cgiFeature.getIdentifier().toString());
        values = (Collection) propIterator.next().getValue();
        assertEquals(1, values.size());
        cgiFeature = (Feature) values.iterator().next();
        assertEquals("Blue", cgiFeature.getIdentifier().toString());
    }

    /**
     * Test nesting features of a complex type with simple content. Previously didn't get encoded.
     * Also making sure that a feature type can have multiple FEATURE_LINK to be referred by
     * different types.
     * 
     * @throws Exception
     */
    @Test
    public void testComplexTypeWithSimpleContent() throws Exception {
        Map dsParams = new HashMap();
        URL url = getClass().getResource(schemaBase + "FirstParentFeature.xml");
        assertNotNull(url);

        dsParams.put("dbtype", "app-schema");
        dsParams.put("url", url.toExternalForm());
        DataAccess<FeatureType, Feature> dataAccess = DataAccessFinder.getDataStore(dsParams);
        assertNotNull(dataAccess);

        // <AttributeMapping>
        // <targetAttribute>FEATURE_LINK[1]</targetAttribute>
        // <sourceExpression>
        // <OCQL>LINK_ONE</OCQL>
        // </sourceExpression>
        // </AttributeMapping>

        Name typeName = Types.typeName("http://example.com", "FirstParentFeature");
        FeatureType featureType = dataAccess.getSchema(typeName);
        assertNotNull(featureType);

        FeatureSource fSource = (FeatureSource) dataAccess.getFeatureSource(typeName);
        FeatureCollection features = (FeatureCollection) fSource.getFeatures();

        assertEquals(5, size(features));

        FeatureIterator iterator = features.features();
        while (iterator.hasNext()) {
            Feature next = iterator.next();
            Collection<Property> children = next.getProperties("nestedFeature");
            if (next.getIdentifier().toString().equals("cc.1")) {
                // _=STRING:String,LINK_ONE:String,LINK_TWO:String
                // sc.1=string_one|cc.1|cc.2
                // sc.2=string_two|cc.1|cc.2
                // sc.3=string_three|NULL|cc.2
                assertEquals(2, children.size());
            } else {
                assertEquals(0, children.size());
            }
            for (Property nestedFeature : children) {
                Object value = nestedFeature.getValue();
                assertNotNull(value);
                value = ((Collection) value).iterator().next();
                assertTrue(value instanceof FeatureImpl);
                Feature feature = (Feature) value;
                assertNotNull(feature.getProperty("someAttribute").getValue());
            }
        }

        // <AttributeMapping>
        // <targetAttribute>FEATURE_LINK[2]</targetAttribute>
        // <sourceExpression>
        // <OCQL>LINK_TWO</OCQL>
        // </sourceExpression>
        // </AttributeMapping>
        dsParams = new HashMap();
        url = getClass().getResource(schemaBase + "SecondParentFeature.xml");
        assertNotNull(url);

        dsParams.put("dbtype", "app-schema");
        dsParams.put("url", url.toExternalForm());
        dataAccess = DataAccessFinder.getDataStore(dsParams);
        assertNotNull(dataAccess);
        typeName = Types.typeName("http://example.com", "SecondParentFeature");
        featureType = dataAccess.getSchema(typeName);
        assertNotNull(featureType);

        fSource = (FeatureSource) dataAccess.getFeatureSource(typeName);
        features = (FeatureCollection) fSource.getFeatures();

        assertEquals(5, size(features));

        iterator = features.features();
        while (iterator.hasNext()) {
            Feature next = iterator.next();
            Collection<Property> children = next.getProperties("nestedFeature");
            if (next.getIdentifier().toString().equals("cc.2")) {
                // _=STRING:String,LINK_ONE:String,LINK_TWO:String
                // sc.1=string_one|cc.1|cc.2
                // sc.2=string_two|cc.1|cc.2
                // sc.3=string_three|NULL|cc.2
                assertEquals(3, children.size());
            } else {
                assertEquals(0, children.size());
            }
            for (Property nestedFeature : children) {
                Object value = nestedFeature.getValue();
                assertNotNull(value);
                value = ((Collection) value).iterator().next();
                assertTrue(value instanceof FeatureImpl);
                Feature feature = (Feature) value;
                assertNotNull(feature.getProperty("someAttribute").getValue());
            }
        }

        dataAccess.dispose();
    }

    /**
     * Test chaining multi-valued by reference (xlink:href). It should result with multiple
     * attributes with no nested attributes, but only client property with xlink:href.
     * 
     * @throws Exception
     */
    @Test
    public void testMultiValuedPropertiesByRef() throws Exception {
        final String MF_PREFIX = "urn:cgi:feature:MappedFeature:";
        final String OCCURENCE = "occurrence";
        final Map<String, String> guToOccurrenceMap = new HashMap<String, String>() {
            {
                put("gu.25699", "mf1");
                put("gu.25678", "mf2;mf3");
                put("gu.25682", "mf4");
            }
        };

        ArrayList<String> processedFeatureIds = new ArrayList<String>();

        FeatureIterator<Feature> guIterator = guFeatures.features();
        while (guIterator.hasNext()) {
            Feature guFeature = (Feature) guIterator.next();
            String guId = guFeature.getIdentifier().toString();
            String[] mfIds = guToOccurrenceMap.get(guId).split(";");
            Collection<Property> properties = guFeature.getProperties(OCCURENCE);

            assertEquals(mfIds.length, properties.size());

            int propertyIndex = 0;
            for (Property property : properties) {
                Object clientProps = property.getUserData().get(Attributes.class);
                assertNotNull(clientProps);
                assertTrue(clientProps instanceof HashMap);
                Object hrefValue = ((Map) clientProps)
                        .get(AbstractMappingFeatureIterator.XLINK_HREF_NAME);

                // ensure the right href:xlink is there
                assertEquals(MF_PREFIX + mfIds[propertyIndex], hrefValue);

                // ensure no attributes would be encoded
                assertTrue(((Collection) property.getValue()).isEmpty());
                propertyIndex++;
            }
            processedFeatureIds.add(guId);
        }

        assertEquals(guToOccurrenceMap.size(), processedFeatureIds.size());
        assertTrue(processedFeatureIds.containsAll(guToOccurrenceMap.keySet()));

        // clean ups
        guIterator.close();
    }

    /**
     * Load all the data accesses.
     * 
     * @return
     * @throws Exception
     */
    private static void loadDataAccesses() throws Exception {
        /**
         * Load mapped feature data access
         */
        Map dsParams = new HashMap();
        URL url = FeatureChainingTest.class.getResource(schemaBase
                + "MappedFeaturePropertyfile.xml");
        assertNotNull(url);

        dsParams.put("dbtype", "app-schema");
        dsParams.put("url", url.toExternalForm());
        DataAccess<FeatureType, Feature> mfDataAccess = DataAccessFinder.getDataStore(dsParams);
        assertNotNull(mfDataAccess);

        FeatureType mappedFeatureType = mfDataAccess.getSchema(MAPPED_FEATURE);
        assertNotNull(mappedFeatureType);

        mfSource = (FeatureSource) mfDataAccess.getFeatureSource(MAPPED_FEATURE);
        mfFeatures = (FeatureCollection) mfSource.getFeatures();

        /**
         * Load geologic unit data access
         */
        url = FeatureChainingTest.class.getResource(schemaBase + "GeologicUnit.xml");
        assertNotNull(url);

        dsParams.put("url", url.toExternalForm());
        DataAccess<FeatureType, Feature> guDataAccess = DataAccessFinder.getDataStore(dsParams);
        assertNotNull(guDataAccess);

        FeatureType guType = guDataAccess.getSchema(GEOLOGIC_UNIT);
        assertNotNull(guType);

        FeatureSource<FeatureType, Feature> guSource = (FeatureSource<FeatureType, Feature>) guDataAccess
                .getFeatureSource(GEOLOGIC_UNIT);
        guFeatures = (FeatureCollection) guSource.getFeatures();

        /**
         * Non-feature types that are included in geologicUnit.xml should be loaded when geologic
         * unit data access is created
         */
        // Composition Part
        cpFeatures = DataAccessRegistry.getFeatureSource(COMPOSITION_PART).getFeatures();
        // CGI TermValue
        FeatureCollection<FeatureType, Feature> cgiFeatures = DataAccessRegistry.getFeatureSource(
                CGI_TERM_VALUE).getFeatures();
        // ControlledConcept
        ccFeatures = DataAccessRegistry.getFeatureSource(CONTROLLED_CONCEPT).getFeatures();
        assertEquals(4, size(mfFeatures));
        assertEquals(3, size(guFeatures));
        assertEquals(4, size(cpFeatures));
        assertEquals(6, size(cgiFeatures));
    }

    private static int size(FeatureCollection<FeatureType, Feature> features) {
        int size = 0;
        for (Iterator i = features.iterator(); i.hasNext(); i.next()) {
            size++;
        }
        return size;
    }
}
