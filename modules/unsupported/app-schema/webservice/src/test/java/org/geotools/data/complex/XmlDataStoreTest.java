package org.geotools.data.complex;

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator;
import org.geotools.data.complex.config.AppSchemaDataAccessDTO;
import org.geotools.data.complex.config.XMLConfigDigester;
import org.geotools.data.complex.xml.XmlResponse;
import org.geotools.data.ws.WSDataStoreFactory;
import org.geotools.data.ws.WS_DataStore;
import org.geotools.data.ws.protocol.ws.WSProtocol;
import org.geotools.feature.ComplexAttributeImpl;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.feature.Types;
import org.geotools.filter.FilterFactoryImplNamespaceAware;
import org.geotools.gml3.bindings.GML3EncodingUtils;
import org.geotools.util.Converters;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.opengis.feature.Attribute;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * 
 * @author Russell Petty
 * @version $Id$
 *
 *
 *
 * @source $URL$
 *         DataAccessFinderTest.java $
 */
public class XmlDataStoreTest extends TestCase {

    private static final String MOCK_DS_PARAM_KEY = "DATA_FILE_DIRECTORY_PARAM_KEY";

    private static final SAXBuilder sax = new SAXBuilder();
    
    private static final String schemaBase = "/test-data/";

    private static final String CGINS = "urn:cgi:xmlns:CGI:Utilities:1.0";

    private final int MAX_FEATURES = 1;

    private AppSchemaDataAccess mappingDataStore;

    private Name typeName;

    /**
     * Filter factory instance
     */
    static FilterFactory ff;

    protected void setUp() throws Exception {
        super.setUp();
        setFilterFactory();
        buildXmlBackedDataAccess();
        typeName = Types.typeName(FeatureChainingTest.GSMLNS, "GeologicUnit");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        DataAccessRegistry.unregisterAndDisposeAll();
    }

    public void testDataStoreCreated() throws Exception {
        assertNotNull(mappingDataStore);

        FeatureType mappedFeatureType = mappingDataStore.getSchema(typeName);
        assertNotNull(mappedFeatureType);
    }

    public void testFilterTranslation() throws Exception {
        //tests that the translation of the filter from GeoSciML to the xml datasorce works.
        Filter inputFilter = ff.equals(ff.property("gml:name"), ff
                .literal("Unit Name1233811724109 UC1233811724109 description name"));

        FeatureCollection features = getFeatures(Query.DEFAULT_MAX, inputFilter);
        assertEquals(1, DataUtilities.count(features));
        
        // check that it returns the right feature
        FeatureIterator iterator = features.features();
        Feature f = iterator.next();
        assertEquals(f.getIdentifier().toString(), "lithostratigraphic.unit.1679161041155866313");        
        iterator.close();     
    }

    public void testFeatureCounting() throws Exception {
        Filter inputFilter = ff.like(ff.property("gml:name"), "*description name*");
        
        FeatureCollection features = getFeatures(Query.DEFAULT_MAX, inputFilter);
        assertEquals(3, DataUtilities.count(features));
        
        // check feature ids
        FeatureIterator iterator = features.features();
        Feature f = iterator.next();
        assertEquals(f.getIdentifier().toString(), "lithostratigraphic.unit.1679161021439131319");
        assertTrue(iterator.hasNext());
        f = iterator.next();            
        assertEquals(f.getIdentifier().toString(), "lithostratigraphic.unit.1679161041155866313");  
        assertTrue(iterator.hasNext());
        f = iterator.next();      
        assertEquals(f.getIdentifier().toString(), "lithostratigraphic.unit.1679161021439938381");
        
        iterator.close();        
        
        // now with maxFeatures = 1, it should only return the first one
        features = getFeatures(MAX_FEATURES, inputFilter);
        assertEquals(MAX_FEATURES, DataUtilities.count(features));
        
        iterator = features.features();
        f = iterator.next();
        assertEquals(f.getIdentifier().toString(), "lithostratigraphic.unit.1679161021439131319");
        
        iterator.close();    
    }

    public void testNoElementsReturned() throws Exception {
        final Filter filter = ff.equals(ff.property("gml:name"), ff
                .literal("invalid name"));        

        FeatureCollection features = getFeatures(MAX_FEATURES, filter);
        
        assertEquals(0, DataUtilities.count(features));

        List<Feature> results = new ArrayList<Feature>();
        FeatureIterator it = features.features();
        for (; it.hasNext();) {
            results.add((Feature) it.next());
        }
        it.close();
        assertEquals(0, results.size());
    }

    public void testFeaturesCreatedCorrectly() throws Exception {
        final Name GeologicUnitName = new NameImpl(FeatureChainingTest.GSMLNS, "GeologicUnit");
        final Name GeologicUnitType = new NameImpl(FeatureChainingTest.GSMLNS, "GeologicUnitType");

        List<Feature> results = new ArrayList<Feature>();

        FeatureCollection features = getFeatures(Query.DEFAULT_MAX, Filter.INCLUDE);
        FeatureIterator it = features.features();
        for (; it.hasNext();) {
            results.add((Feature) it.next());
        }
        it.close();
        
        final int EXPECTED_SIZE = 3;        
        assertEquals(EXPECTED_SIZE, results.size());

        // ***************************************************************************************
        // Check that all features returned are GUs and have the correct id.
        // ***************************************************************************************
        final String[] ids = new String[] { "lithostratigraphic.unit.1679161021439131319",
                "lithostratigraphic.unit.1679161041155866313",
                "lithostratigraphic.unit.1679161021439938381" };
        
        for (int i = 0; i < EXPECTED_SIZE; i++) {
            Feature f = results.get(i);
            assertEquals(ids[i], f.getIdentifier().getID());
            assertEquals(GeologicUnitName, f.getName());
            assertEquals(GeologicUnitType, f.getDescriptor().getType().getName());
            
            // ***************************************************************************************
            // Test an attribute with constant value (as opposed to an xpath)
            // ***************************************************************************************
            List<Attribute> attPurposeList = getAttributesForProperty(f, "purpose");
            assertEquals(1, attPurposeList.size());

            Attribute at = attPurposeList.get(0);
            assertEquals("CONSTANT", getValueForAttribute(at));
            assertFalse(at.getUserData().containsKey(Attributes.class));
            
            // ***************************************************************************************
            // Test an attribute with null value (ie unset) but User data is an xpath
            // ***************************************************************************************
            List<Attribute> attGUTypeList = getAttributesForProperty(f, "rank");
            assertEquals(1, attGUTypeList.size());

            at = attGUTypeList.get(0);
            assertEquals("", getValueForAttribute(at));
            assertEquals(1, ((Map<Object, Object>) at.getUserData().get(Attributes.class)).size());
            assertEquals("urn:cgi:classifier:GSV:LithostratigraphicUnitRank:formation",
                    getUserDataForAttribute(at, new NameImpl("http://www.w3.org/1999/xlink", "href")));
            
            // ***************************************************************************************
            // Test that a nested complex type is created correctly
            // ***************************************************************************************
            List<Attribute> attObMethList = getAttributesForProperty(f, "observationMethod");
            assertEquals(1, attObMethList.size());
            at = attObMethList.get(0);

            assertEquals(null, at.getIdentifier());
            assertEquals(new NameImpl(FeatureChainingTest.GSMLNS, "observationMethod"), at.getName());
            assertEquals(new NameImpl(FeatureChainingTest.GSMLNS, "CGI_TermValuePropertyType"), at.getDescriptor()
                    .getType().getName());

            ComplexAttribute ob = getNestedComplexValueForAttribute(at);
            assertEquals(null, ob.getIdentifier());
            assertEquals(new NameImpl(FeatureChainingTest.GSMLNS, "CGI_TermValue"), ob.getName());
            assertEquals(new NameImpl(FeatureChainingTest.GSMLNS, "CGI_TermValueType"), ob.getDescriptor().getType()
                    .getName());

            ComplexAttribute ob2 = getNestedComplexValueForAttribute(ob);
            assertEquals(null, ob2.getIdentifier());
            assertEquals(new NameImpl(FeatureChainingTest.GSMLNS, "value"), ob2.getName());
            assertEquals(new NameImpl(CGINS, "CodeWithAuthorityType"), ob2
                    .getDescriptor().getType().getName());
            assertEquals("CONSTANT", getValueForAttribute(ob2));
            assertEquals("gsv:NameSpace", getUserDataForAttribute(ob2, new NameImpl("codeSpace")));
        }

        Feature feature = results.get(0);

        // ***************************************************************************************
        // Test an attribute that occurs once, set via xpaths. Also has no userData
        // ***************************************************************************************
        List<Attribute> attDescList = getAttributesForProperty(feature, "description");
        assertEquals(1, attDescList.size());
        Attribute at = attDescList.get(0);
        assertEquals("Test description 1", getValueForAttribute(at));
        assertFalse(at.getUserData().containsKey(Attributes.class));

        // ***************************************************************************************
        // Test an attribute with two values. Also has userData, set with constants instead of
        // xpaths
        // ***************************************************************************************
        List<Attribute> attNameList = getAttributesForProperty(feature, "name");
        assertEquals(2, attNameList.size());

        // First one has a value and a single attribute--codeSpace
        at = attNameList.get(0);
        assertEquals("Unit Name1248396531312 UC1248396531312 description name",
                getValueForAttribute(at));
        assertEquals(1, ((Map) at.getUserData().get(Attributes.class)).size());
        assertEquals("gsv:NameSpace", getUserDataForAttribute(at, new NameImpl("codeSpace")));

        // Second one has a value and a single attribute--codeSpace
        at = attNameList.get(1);
        assertEquals("urn:cgi:feature:GSV:1679161021439131319", getValueForAttribute(at));
        assertTrue(at.getUserData() != null && ((Map) at.getUserData().get(Attributes.class)).size() == 1);
        assertEquals("gsv:NameSpace", getUserDataForAttribute(at, new NameImpl("codeSpace")));
        
        // second feature
        feature = results.get(1);
        attDescList = getAttributesForProperty(feature, "description");
        assertEquals(1, attDescList.size());
        at = attDescList.get(0);
        assertEquals("Test description 1", getValueForAttribute(at));
        assertFalse(at.getUserData().containsKey(Attributes.class));

        attNameList = getAttributesForProperty(feature, "name");
        assertEquals(2, attNameList.size());
        
        at = attNameList.get(0);
        assertEquals("Unit Name1233811724109 UC1233811724109 description name",
                getValueForAttribute(at));
        assertEquals(1, ((Map) at.getUserData().get(Attributes.class)).size());
        assertEquals("gsv:NameSpace", getUserDataForAttribute(at, new NameImpl("codeSpace")));

        at = attNameList.get(1);
        assertEquals("urn:cgi:feature:GSV:1679161041155866313", getValueForAttribute(at));
        assertTrue(at.getUserData() != null && ((Map) at.getUserData().get(Attributes.class)).size() == 1);
        assertEquals("gsv:NameSpace", getUserDataForAttribute(at, new NameImpl("codeSpace")));
                
        // third feature
        feature = results.get(2);
        attDescList = getAttributesForProperty(feature, "description");
        assertEquals(1, attDescList.size());
        at = attDescList.get(0);
        assertEquals("Test description 2", getValueForAttribute(at));
        assertFalse(at.getUserData().containsKey(Attributes.class));

        attNameList = getAttributesForProperty(feature, "name");
        assertEquals(2, attNameList.size());
        
        at = attNameList.get(0);
        assertEquals("Unit Name1248396020281 UC1248396020281 description name 2",
                getValueForAttribute(at));
        assertEquals(1, ((Map) at.getUserData().get(Attributes.class)).size());
        assertEquals("gsv:NameSpace", getUserDataForAttribute(at, new NameImpl("codeSpace")));

        at = attNameList.get(1);
        assertEquals("urn:cgi:feature:GSV:1679161021439938381", getValueForAttribute(at));
        assertTrue(at.getUserData() != null && ((Map) at.getUserData().get(Attributes.class)).size() == 1);
        assertEquals("gsv:NameSpace", getUserDataForAttribute(at, new NameImpl("codeSpace")));
    }

    private String getValueForAttribute(Attribute sv) {
        if (sv instanceof ComplexAttribute) {
            Object value = GML3EncodingUtils.getSimpleContent((ComplexAttribute) sv);
            if (value != null) {
                return Converters.convert(value, String.class);
            }
        }
        String value = null;
        Object ob = sv.getValue();
        if (ob instanceof String) {
            value = (String) ob;
        } else {
            List values = (List) ob;
            if (values != null && values.size() > 0) {
                value = values.get(0).toString();
            }
        }
        return value;
    }

    private ComplexAttributeImpl getNestedComplexValueForAttribute(Attribute sv) {
        Object value = null;
        Object ob = sv.getValue();
        List values = (List) ob;
        if (values != null && values.size() > 0) {
            value = values.get(0);
        }

        return (ComplexAttributeImpl) value;
    }

    private String getUserDataForAttribute(Attribute sv, Name key) {
        String value = null;
        Map<Object, Object> userData = (Map<Object, Object>) sv.getUserData();
        if (userData.containsKey(Attributes.class)) {
            Map map = (Map) userData.get(Attributes.class);
            value = (String) map.get(key);
        }
        return value;
    }

    private List<Attribute> getAttributesForProperty(Feature feature, String propertyName) {
        List<Attribute> returnValues = new ArrayList<Attribute>();
        Collection cs = feature.getProperties(propertyName);
        Iterator it = cs.iterator();
        while (it.hasNext()) {
            Attribute sv = (Attribute) it.next();
            returnValues.add(sv);
        }
        return returnValues;
    }


    public static class MockXmlDataStoreFactory extends WSDataStoreFactory {
        public boolean isAvailable() {
            return true;
        }

        @Override
        public boolean canProcess(final Map params) {
            return params.get(MOCK_DS_PARAM_KEY) != null;
        }

        @Override
        public XmlDataStore createDataStore(Map params) throws IOException {
            XmlDataStore ds = null;
            Map<String, Object> wsParams = new HashMap<String, Object>();
            wsParams.put("WSDataStoreFactory:GET_CONNECTION_URL",
                    "http://d00109:8080/xaware/XADocSoapServlet");
            wsParams.put("WSDataStoreFactory:TIMEOUT", new Integer(30000));
            wsParams.put("WSDataStoreFactory:TEMPLATE_DIRECTORY", getClass()
                    .getResource(schemaBase));
            wsParams.put("WSDataStoreFactory:TEMPLATE_NAME", "request.ftl");
            wsParams.put("WSDataStoreFactory:CAPABILITIES_FILE_LOCATION", getClass().getResource(
                    schemaBase + "ws_capabilities_equals_removed.xml"));

            org.geotools.data.ws.XmlDataStore wsStore = super.createDataStore(wsParams);
            ds = new XmlDataStore(((WS_DataStore) wsStore).getProtocol());
            // additional parameter because we don't actually have a web service, but pretend
            // the output has been written in an xml file
            ds.setFileName((String) params.get(MOCK_DS_PARAM_KEY));

            return ds;
        }
    }

    public static final class XmlDataStore extends WS_DataStore {

        public XmlDataStore(WSProtocol protocol) {
            super(protocol);
        }

        private String fileName;

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
        
        @Override
        public XmlResponse getXmlReader(Query query) throws IOException {
            if (Filter.EXCLUDE.equals(query.getFilter())) {
                return null; //empty response
            }

            Query callQuery = new Query(query);

            Filter[] filters = getProtocol().splitFilters(query.getFilter());
            Filter supportedFilter = filters[0];
            Filter postFilter = filters[1];
            callQuery.setFilter(supportedFilter);
            
            Document doc = getXmlResponse();             
            List<Integer> validFeatureIndex = determineValidFeatures(postFilter, doc, query
                    .getMaxFeatures());
            return new XmlResponse(doc, validFeatureIndex);
        }
        
        public XmlResponse getXmlReader(Query query, String xpath, String value) throws IOException {
            Document doc = getXmlResponse(); 
            
            List<Integer> validFeatureIndex = determineValidFeatures(xpath, value, doc, query
                    .getMaxFeatures());
            return new XmlResponse(doc, validFeatureIndex);
        }

        private Document getXmlResponse() throws IOException {
            Document doc = null;
            BufferedInputStream dos = null;
            try {
                File outFile = new File(fileName);
                dos = new BufferedInputStream(new FileInputStream(outFile));
                doc = sax.build(dos);
            } catch (JDOMException e1) {
                throw new RuntimeException("error reading xml from file ", e1);
            } finally {
                if (dos != null) {
                    dos.close();
                }
            }
            return doc;
        }
    };
    
    private Query namedQuery(Filter filter, int count) throws Exception {
        return new Query("GeologicUnit", new URI(FeatureChainingTest.GSMLNS), filter, count,
                new String[] {}, "test");
    }

    /**
     * Set filter factory with name spaces
     */
    private void setFilterFactory() {
        NamespaceSupport namespaces = new NamespaceSupport();
        namespaces.declarePrefix("gsml", FeatureChainingTest.GSMLNS);
        namespaces.declarePrefix("gml", FeatureChainingTest.GMLNS);
        ff = new FilterFactoryImplNamespaceAware(namespaces);
    }

    private void buildXmlBackedDataAccess() throws IOException {
        URL url = getClass().getResource(schemaBase + "xmlDataAccessConfig.xml");
        assertNotNull(url);

        AppSchemaDataAccessDTO config = new XMLConfigDigester().parse(url);

        Set mappings = AppSchemaDataAccessConfigurator.buildMappings(config);
        mappingDataStore = new AppSchemaDataAccess(mappings);

    }

    private String getExpectedFilter() {
        final String prefix = "/soapenv:Envelope/soapenv:Body/qaz:getGeologicalFeaturesByFilterStringResponse/qaz:out/qaz:item/";
        return "[[ "
                + prefix
                + "gss:formattedName = Unit Name1233811724109 UC1233811724109 description name ] OR "
                + "[ "
                + prefix
                + "gss:urn[@domain='GSV'] = Unit Name1233811724109 UC1233811724109 description name ]]";
    }

    private String getReversedExpectedFilter() {
        final String prefix = "/soapenv:Envelope/soapenv:Body/qaz:getGeologicalFeaturesByFilterStringResponse/qaz:out/qaz:item/";
        return "[[ "
                + prefix
                + "gss:urn[@domain='GSV'] = Unit Name1233811724109 UC1233811724109 description name ] OR "
                + "[ "
                + prefix
                + "gss:formattedName = Unit Name1233811724109 UC1233811724109 description name ]]";
    }
    
    private FeatureCollection getFeatures(final int maxFeatures, Filter inputFilter)
            throws Exception {
        FeatureSource fSource = (FeatureSource) mappingDataStore.getFeatureSource(typeName);
        FeatureCollection features = (FeatureCollection) fSource.getFeatures(namedQuery(
                inputFilter, new Integer(maxFeatures)));
        return features;
    }

}
