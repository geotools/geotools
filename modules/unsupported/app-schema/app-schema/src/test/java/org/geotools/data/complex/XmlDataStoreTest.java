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

import java.awt.RenderingHints.Key;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.geotools.data.DataAccess;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.LockingManager;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ResourceInfo;
import org.geotools.data.ServiceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator;
import org.geotools.data.complex.config.AppSchemaDataAccessDTO;
import org.geotools.data.complex.config.XMLConfigDigester;
import org.geotools.data.complex.xml.XmlFeatureCollection;
import org.geotools.data.complex.xml.XmlFeatureSource;
import org.geotools.data.complex.xml.XmlResponse;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.DataFeatureCollection;
import org.geotools.feature.ComplexAttributeImpl;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.feature.Types;
import org.geotools.feature.simple.SimpleFeatureTypeImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.filter.FilterFactoryImplNamespaceAware;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml3.GMLSchema;
import org.geotools.gml3.bindings.GML3EncodingUtils;
import org.geotools.util.Converters;
import org.geotools.xs.XSSchema;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.opengis.feature.Attribute;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
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
 * @source $URL: http://svn.geotools.org/trunk/modules/library/main/src/test/java/org/geotools/data/
 *         DataAccessFinderTest.java $
 */
public class XmlDataStoreTest extends TestCase {

    private static final String MOCK_DS_PARAM_KEY = "DATA_FILE_DIRECTORY_PARAM_KEY";

    private static final SAXBuilder sax = new SAXBuilder();

    static final XmlDataStore MOCK_DATASTORE = new XmlDataStore();
    
    private static final String schemaBase = "/test-data/";

    private static final String GSMLNS = "http://www.cgi-iugs.org/xml/GeoSciML/2";

    private static final String GMLNS = "http://www.opengis.net/gml";

    private final int MAX_FEATURES = 5;

    private DataAccess mappingDataStore;

    private Name typeName;

    /**
     * Filter factory instance
     */
    static FilterFactory ff;

    protected void setUp() throws Exception {
        super.setUp();
        setFilterFactory();
        buildXmlBackedDataAccess();
        typeName = Types.typeName(GSMLNS, "GeologicUnit");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        DataAccessRegistry.unregisterAll();
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
        
        List<Integer> ls = new ArrayList<Integer>();
        ls.add(1);
        MOCK_DATASTORE.setValidElements(ls);

        FeatureCollection features = getFeatures(MAX_FEATURES, inputFilter);
        int size = size(features);

        assertEquals(ls.size(), size);
        Query query = MOCK_DATASTORE.getQuery();

        assertEquals(MAX_FEATURES, query.getMaxFeatures());
        String translatedFilter = query.getFilter().toString();
        assertTrue(getExpectedFilter().equals(translatedFilter) ||
                getReversedExpectedFilter().equals(translatedFilter));
    }

    public void testFeatureCounting() throws Exception {
        Filter inputFilter = ff.equals(ff.property("gml:name"), ff
                .literal("Unit Name1233811724109 UC1233811724109 description name"));

        List<Integer> ls = new ArrayList<Integer>();
        ls.add(1);
        MOCK_DATASTORE.setValidElements(ls);

        FeatureCollection features = getFeatures(MAX_FEATURES, inputFilter);
        int size = size(features);

        assertEquals(ls.size(), size);
    }

    public void testNoElementsReturned() throws Exception {
        final Filter filter = ff.equals(ff.property("gml:name"), ff
                .literal("Unit Name1233811724109 UC1233811724109 description name"));

        List<Integer> ls = new ArrayList<Integer>();
        // Empty list--no valid elements returned in the response
        MOCK_DATASTORE.setValidElements(ls);

        FeatureCollection features = getFeatures(MAX_FEATURES, filter);
        int size = size(features);
        assertEquals(0, size);

        List<Feature> results = new ArrayList<Feature>();
        FeatureIterator it = features.features();
        for (; it.hasNext();) {
            results.add((Feature) it.next());
        }
        it.close();
        assertEquals(ls.size(), results.size());
    }

    public void testFeaturesCreatedCorrectly() throws Exception {
        final Name GeologicUnitName = new NameImpl(GSMLNS, "GeologicUnit");
        final Name GeologicUnitType = new NameImpl(GSMLNS, "GeologicUnitType");
        final Filter filter = ff.equals(ff.property("gml:name"), ff
                .literal("Unit Name1233811724109 UC1233811724109 description name"));

        XmlDataStore ds = (XmlDataStore) MOCK_DATASTORE;
        ds.setFileName("./src/test/resources/test-data/xmlDataStoreResponse.xml");
        String s = Filter.INCLUDE.toString();
        List<Integer> ls = new ArrayList<Integer>();
        ls.add(1);
        ls.add(2);
        MOCK_DATASTORE.setValidElements(ls);

        List<Feature> results = new ArrayList<Feature>();

        FeatureCollection features = getFeatures(MAX_FEATURES, filter);
        FeatureIterator it = features.features();
        for (; it.hasNext();) {
            results.add((Feature) it.next());
        }
        it.close();
        assertEquals(ls.size(), results.size());

        // ***************************************************************************************
        // Check that all features returned are GUs and have the correct id.
        // ***************************************************************************************
        final String[] ids = new String[] { "1679161021439131319", "1679161041155866313" };
        for (int i = 0; i < ls.size(); i++) {
            Feature f = results.get(i);
            assertEquals(ids[i], f.getIdentifier().getID());
            assertEquals(GeologicUnitName, f.getName());
            assertEquals(GeologicUnitType, f.getDescriptor().getType().getName());
        }

        Feature feature = results.get(0);

        // ***************************************************************************************
        // Test an attribute that occurs once, set via xpaths. Also has no userData
        // ***************************************************************************************
        List<Attribute> attDescList = getAttributesForProperty(feature, "description");
        assertEquals(1, attDescList.size());
        Attribute at = attDescList.get(0);
        assertEquals("Test description 1", getValueForAttribute(at));
        assertEquals(0, at.getUserData().size());

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

        // ***************************************************************************************
        // Test an attribute with constant value (as opposed to an xpath)
        // ***************************************************************************************
        List<Attribute> attPurposeList = getAttributesForProperty(feature, "purpose");
        assertEquals(1, attPurposeList.size());

        at = attPurposeList.get(0);
        assertEquals("CONSTANT", getValueForAttribute(at));
        assertEquals(0, at.getUserData().size());

        // ***************************************************************************************
        // Test an attribute with null value (ie unset) but User data is an xpath
        // ***************************************************************************************
        List<Attribute> attGUTypeList = getAttributesForProperty(feature, "rank");
        assertEquals(1, attGUTypeList.size());

        at = attGUTypeList.get(0);
        assertEquals("", getValueForAttribute(at));
        assertEquals(1, at.getUserData().size());
        assertEquals("urn:cgi:classifier:GSV:LithostratigraphicUnitRank:formation",
                getUserDataForAttribute(at, new NameImpl("http://www.w3.org/1999/xlink", "href")));

        // ***************************************************************************************
        // Test that a nested complex type is created correctly
        // ***************************************************************************************
        List<Attribute> attObMethList = getAttributesForProperty(feature, "observationMethod");
        assertEquals(1, attObMethList.size());
        at = attObMethList.get(0);

        assertEquals(null, at.getIdentifier());
        assertEquals(new NameImpl(GSMLNS, "observationMethod"), at.getName());
        assertEquals(new NameImpl(GSMLNS, "CGI_TermValuePropertyType"), at.getDescriptor()
                .getType().getName());

        ComplexAttribute ob = getNestedComplexValueForAttribute(at);
        assertEquals(null, ob.getIdentifier());
        assertEquals(new NameImpl(GSMLNS, "CGI_TermValue"), ob.getName());
        assertEquals(new NameImpl(GSMLNS, "CGI_TermValueType"), ob.getDescriptor().getType()
                .getName());

        ComplexAttribute ob2 = getNestedComplexValueForAttribute(ob);
        assertEquals(null, ob2.getIdentifier());
        assertEquals(new NameImpl(GSMLNS, "value"), ob2.getName());
        assertEquals(new NameImpl(GSMLNS, "ScopedNameType"), ob2
                .getDescriptor().getType().getName());
        assertEquals("CONSTANT", getValueForAttribute(ob2));
        assertEquals("gsv:NameSpace", getUserDataForAttribute(ob2, new NameImpl("codeSpace")));
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


    public static class MockXmlDataStoreFactory implements DataStoreFactorySpi {
        public boolean isAvailable() {
            return true;
        }

        public boolean canProcess(Map<String, Serializable> params) {
            return params.get(MOCK_DS_PARAM_KEY) != null;
        }

        public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
            XmlDataStore ds = (XmlDataStore) MOCK_DATASTORE;
            ds.setFileName((String) params.get(MOCK_DS_PARAM_KEY));
            return ds;
        }

        public DataStore createNewDataStore(Map params) throws IOException {
            return null;
        }

        public String getDescription() {
            return null;
        }

        public String getDisplayName() {
            return null;
        }

        public org.geotools.data.DataAccessFactory.Param[] getParametersInfo() {
            return null;
        }

        public Map<Key, ?> getImplementationHints() {
            return null;
        }
    }

    public static final class XmlDataStore implements DataStore {

        private String fileName;

        private List<Integer> validElements;

        private Query query;

        public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(Query query,
                Transaction transaction) throws IOException {
            return null;
        }

        public SimpleFeatureSource getFeatureSource(String typeName)
                throws IOException {
            return null;
        }

        public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName,
                Filter filter, Transaction transaction) throws IOException {
            return null;
        }

        public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName,
                Transaction transaction) throws IOException {
            return null;
        }

        public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(
                String typeName, Transaction transaction) throws IOException {
            return null;
        }

        public LockingManager getLockingManager() {
            return null;
        }

        public SimpleFeatureType getSchema(String typeName) throws IOException {
            return null;
        }

        public String[] getTypeNames() throws IOException {
            return null;
        }

        public void updateSchema(String typeName, SimpleFeatureType featureType) throws IOException {
        }

        public void createSchema(SimpleFeatureType featureType) throws IOException {
        }

        public void dispose() {
        }

        public SimpleFeatureSource getFeatureSource(Name typeName)
                throws IOException {
            return new XmlTestFeatureSource(this);
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public void setValidElements(List<Integer> validElements) {
            this.validElements = validElements;
        }

        public Query getQuery() {
            return query;
        }

        public int getCount(Query query) {
            return -1;
        }

        public ServiceInfo getInfo() {
            return null;
        }

        public List<Name> getNames() throws IOException {
            return null;
        }

        public SimpleFeatureType getSchema(Name name) throws IOException {
            return null;
        }

        public void updateSchema(Name typeName, SimpleFeatureType featureType) throws IOException {
        }

        public XmlResponse getXmlReader(Query query, final Transaction transaction)
                throws IOException {
            this.query = query;
            Document doc = null;
            try {
                File outFile = new File(fileName);
                DataInputStream dos = new DataInputStream(new FileInputStream(outFile));
                doc = sax.build(dos);
            } catch (JDOMException e1) {
                throw new RuntimeException("error reading xml from file ", e1);
            }

            return new XmlResponse(doc, validElements);
        }
    };

    public static final class XmlTestFeatureSource implements XmlFeatureSource {

        private XmlDataStore dataStore;

        public XmlTestFeatureSource(final DataStore dataStore) throws IOException {
            this.dataStore = (XmlDataStore) dataStore;
        }

        public Name getName() {
            return new NameImpl(GSMLNS, "GeologicUnit");
        }

        public DataStore getDataStore() {
            return dataStore;
        }

        public SimpleFeatureType getSchema() {
            List<AttributeDescriptor> schema = new ArrayList<AttributeDescriptor>();
            schema.add(new AttributeDescriptorImpl(XSSchema.DOUBLE_TYPE, Types.typeName("pointOne"), 0,
                    1, false, null));
            schema.add(new AttributeDescriptorImpl(XSSchema.DOUBLE_TYPE, Types.typeName("pointTwo"), 0,
                    1, false, null));
            return new SimpleFeatureTypeImpl(Types.typeName("GeometryContainer"),
                    schema, null, false, null, GMLSchema.ABSTRACTFEATURETYPE_TYPE, null);
        }

        public void setNamespaces(org.xml.sax.helpers.NamespaceSupport namespaces) {
        }

        public void setItemXpath(String inputAttributeXpathPrefix) {
        }

        public ResourceInfo getInfo() {
            return null;
        }

        public void addFeatureListener(FeatureListener listener) {
        }

        public void removeFeatureListener(FeatureListener listener) {
        }

        public ReferencedEnvelope getBounds() throws IOException {
            return getInfo().getBounds();
        }

        public ReferencedEnvelope getBounds(Query query) throws IOException {
            return null;
        }

        public int getCount(Query query) throws IOException {
            Query namedQuery = namedQuery(query);
            return dataStore.getCount(namedQuery);
        }

        public XmlTestFeatureCollection getFeatures(Filter filter) throws IOException {
            return getFeatures(new DefaultQuery("GeologicUnit", filter));
        }

        public XmlTestFeatureCollection getFeatures() throws IOException {
            return getFeatures(new DefaultQuery("GeologicUnit"));
        }

        public XmlTestFeatureCollection getFeatures(final Query query) throws IOException {
            return new XmlTestFeatureCollection(dataStore, namedQuery(query));
        }

        public Set getSupportedHints() {
            return Collections.EMPTY_SET;
        }

        private Query namedQuery(final Query query) {
            return new DefaultQuery(query);
        }

        public QueryCapabilities getQueryCapabilities() {
            return null;
        }
    }

    public static final class XmlTestFeatureCollection extends DataFeatureCollection implements
            XmlFeatureCollection {

        private Query query;

        private XmlDataStore dataStore;

        private XmlResponse xmlResponse;

        public XmlTestFeatureCollection(XmlDataStore dataStore, Query query) throws IOException {
            this.dataStore = dataStore;
            this.query = query;
        }

        @Override
        public ReferencedEnvelope getBounds() {
            throw new UnsupportedOperationException("No bounds for WS!");
        }

        @Override
        public int getCount() throws IOException {
            return dataStore.getCount(query);
        }

        public XmlResponse xmlResponse() {
            try {
                xmlResponse = dataStore.getXmlReader(query, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return xmlResponse;
        }
    }

    private DefaultQuery namedQuery(Filter filter, int count) throws Exception {
        return new DefaultQuery("GeologicUnit", new URI(GSMLNS), filter, count, new String[] {},
                "test");
    }

    /**
     * Set filter factory with name spaces
     */
    private void setFilterFactory() {
        NamespaceSupport namespaces = new NamespaceSupport();
        namespaces.declarePrefix("gsml", GSMLNS);
        namespaces.declarePrefix("gml", GMLNS);
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

    private int size(FeatureCollection<FeatureType, Feature> features) {
        int size = 0;
        for (Iterator i = features.iterator(); i.hasNext(); i.next()) {
            size++;
        }
        return size;
    }
}
