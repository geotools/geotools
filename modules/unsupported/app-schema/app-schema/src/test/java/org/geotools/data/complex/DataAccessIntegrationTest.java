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

import java.awt.RenderingHints.Key;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFactory;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ResourceInfo;
import org.geotools.data.ServiceInfo;
import org.geotools.data.complex.config.EmfAppSchemaReader;
import org.geotools.data.complex.config.FeatureTypeRegistry;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.AttributeImpl;
import org.geotools.feature.CollectionListener;
import org.geotools.feature.ComplexAttributeImpl;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureImpl;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.feature.Types;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.FeatureTypeImpl;
import org.geotools.filter.FilterFactoryImplNamespaceAware;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml3.GMLSchema;
import org.geotools.xml.SchemaIndex;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.sort.SortBy;
import org.opengis.util.ProgressListener;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * This is to test integration of a non-app-schema data access with an app-schema data access. An
 * app-schema data access can chain features from a non-app-schema data access.
 * 
 * @author Rini Angreani, Curtin University of Technology
 * 
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/test
 *         /java/org/geotools/data/complex/DataAccessIntegrationTest.java $
 */
public class DataAccessIntegrationTest {

    protected static final String GSMLNS = "urn:cgi:xmlns:CGI:GeoSciML:2.0";

    protected static final String GMLNS = "http://www.opengis.net/gml";

    protected static final Name MAPPED_FEATURE = Types.typeName(GSMLNS, "MappedFeature");

    private static final Name GEOLOGIC_UNIT_TYPE = Types.typeName(GSMLNS, "GeologicUnitType");

    protected static final Name GEOLOGIC_UNIT = Types.typeName(GSMLNS, "GeologicUnit");

    private static final Name COMPOSITION_PART_TYPE = Types.typeName(GSMLNS, "CompositionPartType");

    private static final Name COMPOSITION_PART = Types.typeName(GSMLNS, "CompositionPart");

    protected static final String schemaBase = "/test-data/";

    /**
     * App schema config reader
     */
    protected static EmfAppSchemaReader reader;

    protected static FeatureTypeRegistry typeRegistry;

    /**
     * Mapped Feature data access in GSML form
     */
    protected static DataAccess<FeatureType, Feature> mfDataAccess;

    /**
     * GSML:geologicUnit feature source coming from the mapped data access
     */
    protected static FeatureSource<FeatureType, Feature> guFeatureSource;

    /**
     * Collection of MO:earthResource complex features
     */
    protected static ArrayList<Feature> inputFeatures;

    /**
     * Collection of GSML:compositionPart complex features
     */
    protected static ArrayList<Feature> cpFeatures;

    /**
     * Collection of GSML:mappedFeature complex features
     */
    protected static ArrayList<Feature> mfFeatures;

    /**
     * Filter factory instance
     */
    protected static FilterFactory ff;

    /**
     * Create the input data access containing complex features of MO form.
     */
    @BeforeClass
    public static void setUp() throws Exception {
        setFilterFactory();
        loadGeologicUnitDataAccess();
        // Load CGI Term Value data access
        URL url = DataAccessIntegrationTest.class.getResource(schemaBase + "CGITermValue.xml");
        assertNotNull(url);
        Map<String, Serializable> dsParams = new HashMap<String, Serializable>();
        dsParams.put("dbtype", "app-schema");
        dsParams.put("url", url.toExternalForm());
        DataAccess<FeatureType, Feature> cgiDataAccess = DataAccessFinder.getDataStore(dsParams);
        assertNotNull(cgiDataAccess);
        // Load composition part data access
        url = DataAccessIntegrationTest.class.getResource(schemaBase + "CompositionPart.xml");
        assertNotNull(url);        
        dsParams.put("url", url.toExternalForm());
        DataAccessFinder.getDataStore(dsParams);
        // Load Mapped Feature data access
        loadDataAccess("MappedFeaturePropertyfile.xml");
        
    }

    /**
     * Create Geologic Unit complex features from the simple features and complex type.
     * 
     * @param fCollection
     * @param geologicUnitType
     * @return
     * @throws IOException
     */
    private static ArrayList<Feature> getInputFeatures(
            SimpleFeatureCollection fCollection,
            FeatureType geologicUnitType) throws IOException {
        ArrayList<Feature> features = new ArrayList<Feature>();

        AttributeDescriptor featureDesc = new AttributeDescriptorImpl(geologicUnitType,
                GEOLOGIC_UNIT, 0, -1, false, null);
        // gml:description
        AttributeDescriptor descriptionDescriptor = (AttributeDescriptor) geologicUnitType
                .getDescriptor(Types.typeName(GMLNS, "description"));
        // gml:name
        AttributeDescriptor nameDescriptor = (AttributeDescriptor) GMLSchema.ABSTRACTGMLTYPE_TYPE
                .getDescriptor(Types.typeName(GMLNS, "name"));
        // for simple string properties
        Name name = new NameImpl(null, "simpleContent");
        AttributeType simpleContentType = typeRegistry.getAttributeType(Types.typeName(
                "http://www.w3.org/2001/XMLSchema", "string"));
        AttributeDescriptor stringDescriptor = new AttributeDescriptorImpl(simpleContentType, name,
                1, 1, true, (Object) null);
        SimpleFeatureIterator simpleFeatures = fCollection.features();
        // keep track of the last feature, so we can group them as 1 if they're the same
        Feature lastFeature = null;
        while (simpleFeatures.hasNext()) {
            SimpleFeature next = simpleFeatures.next();
            String propertyName;
            ArrayList<Property> value = new ArrayList<Property>();
            Collection<Property> properties = new ArrayList<Property>();
            if (lastFeature != null && lastFeature.getIdentifier().equals(next.getIdentifier())) {
                // ATTENTION: so far only NAME has different values in the properties file,
                // if you edit the properties file involving other values, you have to add
                // the code here accordingly.
                Collection<Property> values = lastFeature.getProperties(Types.typeName(GMLNS,
                        "name"));
                Iterator<Property> iterator = values.iterator();
                Object existingValue = ((Collection<Property>) iterator.next().getValue())
                        .iterator().next().getValue();
                Object nameValue = next.getProperty("NAME").getValue();
                if (!existingValue.equals(nameValue)) {
                    value.add(new AttributeImpl(nameValue, stringDescriptor, null));
                    properties.addAll(lastFeature.getProperties());
                    properties.add(new ComplexAttributeImpl(value, nameDescriptor, null));
                    lastFeature = new FeatureImpl(properties, lastFeature.getDescriptor(),
                            lastFeature.getIdentifier());
                }
                existingValue = ((Collection<Property>) iterator.next().getValue()).iterator()
                        .next().getValue();
                nameValue = next.getProperty("ABBREVIATION").getValue();
                if (!existingValue.equals(nameValue)) {
                    value.add(new AttributeImpl(nameValue, stringDescriptor, null));
                    properties.addAll(lastFeature.getProperties());
                    properties.add(new ComplexAttributeImpl(value, nameDescriptor, null));
                    lastFeature = new FeatureImpl(properties, lastFeature.getDescriptor(),
                            lastFeature.getIdentifier());
                }
            } else {
                // description
                propertyName = "TEXTDESCRIPTION";
                value.add(new AttributeImpl(next.getProperty(propertyName).getValue(),
                        stringDescriptor, null));
                properties.add(new ComplexAttributeImpl(value, descriptionDescriptor, null));

                // name 1
                propertyName = "NAME";
                value = new ArrayList<Property>();
                value.add(new AttributeImpl(next.getProperty(propertyName).getValue(),
                        stringDescriptor, null));
                ComplexAttributeImpl name1 = new ComplexAttributeImpl(value, nameDescriptor, null);
                properties.add(name1);

                // name 2
                value = new ArrayList<Property>();
                propertyName = "ABBREVIATION";
                value.add(new AttributeImpl(next.getProperty(propertyName).getValue(),
                        stringDescriptor, null));
                properties.add(new ComplexAttributeImpl(value, nameDescriptor, null));

                // composition part
                ComplexType cpType = (ComplexType) typeRegistry
                        .getAttributeType(COMPOSITION_PART_TYPE);

                ArrayList<Property> compositionParts = new ArrayList<Property>();
                compositionParts.add(name1);

                value = new ArrayList<Property>();
                value.add(new ComplexAttributeImpl(compositionParts, cpType, null));
                properties.add(new ComplexAttributeImpl(value,
                        (AttributeDescriptor) geologicUnitType.getDescriptor(Types.typeName(GSMLNS,
                                "composition")), null));

                // feature chaining link
                properties.add(new AttributeImpl(next.getID(),
                        (AttributeDescriptor) ComplexFeatureConstants.FEATURE_CHAINING_LINK, null));

                lastFeature = new FeatureImpl(properties, featureDesc, next.getIdentifier());
                features.add(lastFeature);
            }
        }
        simpleFeatures.close();

        return features;
    }

    /**
     * Test that mapping geologic unit inside mapped feature type is successful.
     * 
     * @throws IOException
     */
    @Test
    public void testMappings() throws IOException {

        Iterator<Feature> mfIterator = mfFeatures.iterator();

        Iterator<Feature> guIterator = inputFeatures.iterator();

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
        Feature mfFeature;
        Collection<Property> nestedGuFeatures;
        String guId;
        final String NESTED_LINK = "specification";
        while (mfIterator.hasNext()) {
            mfFeature = (Feature) mfIterator.next();
            String mfId = mfFeature.getIdentifier().toString();
            String[] guIds = FeatureChainingTest.mfToGuMap.get(mfId).split(";");

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
            }
            // make sure all the nested geological unit features are there
            assertTrue(nestedGuIds.containsAll(Arrays.asList(guIds)));
        }
    }

    /**
     * Make sure filters are working.
     * 
     * @throws IOException
     */
    @Test
    public void testFilters() throws IOException {

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

        // <ogc:PropertyName>
        // gsml:specification/gsml:GeologicUnit/gml:description
        Expression property = ff.property("gsml:specification/gsml:GeologicUnit/gml:description");
        Filter filter = ff.like(property,
                "Olivine basalt, tuff, microgabbro, minor sedimentary rocks");

        FeatureCollection<FeatureType, Feature> filteredResults = mfDataAccess.getFeatureSource(
                MAPPED_FEATURE).getFeatures(filter);

        assertEquals(3, size(filteredResults));
    }

    private int size(FeatureCollection<FeatureType, Feature> features) {
        int size = 0;
        for (Iterator i = features.iterator(); i.hasNext(); i.next()) {
            size++;
        }
        return size;
    }

    /**
     * Load non-app-schema Geologic Unit data access.
     * 
     * @throws IOException
     */
    private static void loadGeologicUnitDataAccess() throws IOException {
        Map<String, Serializable> erParams = new HashMap<String, Serializable>();
        erParams.put("dbtype", "input-data-access");
        DataAccess<FeatureType, Feature> inputDataAccess = DataAccessFinder.getDataStore(erParams);
        guFeatureSource = inputDataAccess.getFeatureSource(GEOLOGIC_UNIT);
    }

    /**
     * Load all the data accesses.
     * 
     * @param mfMappingFile
     *            Mapped feature mapping file with geologic unit as specification
     * @throws IOException
     */
    protected static void loadDataAccess(String mfMappingFile) throws IOException {
        /**
         * Load mapped feature data access
         */
        Map<String, Serializable> dsParams = new HashMap<String, Serializable>();
        URL url = DataAccessIntegrationTest.class.getResource(schemaBase + mfMappingFile);
        assertNotNull(url);

        dsParams.put("dbtype", "app-schema");
        dsParams.put("url", url.toExternalForm());

        mfDataAccess = DataAccessFinder.getDataStore(dsParams);
        assertNotNull(mfDataAccess);

        FeatureType mappedFeatureType = mfDataAccess.getSchema(MAPPED_FEATURE);
        assertNotNull(mappedFeatureType);
        FeatureSource<FeatureType, Feature> mfSource = mfDataAccess
                .getFeatureSource(MAPPED_FEATURE);
        FeatureCollection<FeatureType, Feature> mfCollection = mfSource.getFeatures();
        FeatureIterator<Feature> mfIterator = mfCollection.features();
        mfFeatures = new ArrayList<Feature>();
        while (mfIterator.hasNext()) {
            mfFeatures.add(mfIterator.next());
        }
        mfIterator.close();
        
        /**
         * Get composition part data access features
         */
        DataAccess<FeatureType, Feature> cpDataAccess = DataAccessRegistry.getDataAccess(COMPOSITION_PART);
        assertNotNull(cpDataAccess);
        FeatureSource<FeatureType, Feature> cpSource = cpDataAccess
                .getFeatureSource(COMPOSITION_PART);
        FeatureCollection<FeatureType, Feature> cpCollection = cpSource.getFeatures();
        FeatureIterator<Feature> cpIterator = cpCollection.features();

        cpFeatures = new ArrayList<Feature>();
        while (cpIterator.hasNext()) {
            cpFeatures.add(cpIterator.next());
        }
        cpIterator.close();
    }

    /**
     * Dispose all the data accesses so that there is no mapping conflicts for other tests
     */
    @AfterClass
    public static void tearDown() {
        DataAccessRegistry.unregisterAll();
    }

    /**
     * This is a test data access factory to create non-app-schema data access as an input for the
     * tests above.
     * 
     * @author ang05a
     */
    public static class InputDataAccessFactory implements DataAccessFactory {
        public InputDataAccessFactory() {
        }

        public boolean canProcess(Map<String, Serializable> params) {
            Object dbType = params.get("dbtype");
            return dbType == null ? false : dbType.equals("input-data-access");
        }

        public DataAccess<? extends FeatureType, ? extends Feature> createDataStore(
                Map<String, Serializable> params) throws IOException {
            URL schemaURL = getClass().getResource(schemaBase);
            File dir;
            try {
                dir = new File(schemaURL.toURI());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            // get geologic unit properties file
            PropertyDataStore dataStore = new PropertyDataStore(dir);
            SimpleFeatureSource simpleFeatureSource = dataStore
                    .getFeatureSource(GEOLOGIC_UNIT);
            SimpleFeatureCollection fCollection = simpleFeatureSource
                    .getFeatures();
            reader = EmfAppSchemaReader.newInstance();
            // set schema URI
            SchemaIndex schemaIndex = reader.parse(new URL(
                    "http://www.geosciml.org/geosciml/2.0/xsd/geosciml.xsd"));
            typeRegistry = new FeatureTypeRegistry();
            typeRegistry.addSchemas(schemaIndex);

            // get simple features
            FeatureType simpleType = (FeatureType) typeRegistry
                    .getAttributeType(GEOLOGIC_UNIT_TYPE);
            inputFeatures = getInputFeatures(fCollection, simpleType);
            // create complex feature type
            FeatureType guSchema = new FeatureTypeImpl(GEOLOGIC_UNIT, simpleType.getDescriptors(),
                    null, true, simpleType.getRestrictions(), GMLSchema.ABSTRACTFEATURETYPE_TYPE,
                    null);
            return new InputDataAccess(inputFeatures, guSchema,                    
                    FeatureChainingTest.GEOLOGIC_UNIT_NAME);
        }

        public String getDescription() {
            return null;
        }

        public String getDisplayName() {
            return null;
        }

        public Param[] getParametersInfo() {
            return null;
        }

        public boolean isAvailable() {
            return true;
        }

        public Map<Key, ?> getImplementationHints() {
            return null;
        }

        /**
         * This is a test non app-schema MO:data access
         * 
         * @author ang05a
         */
        class InputDataAccess implements DataAccess<FeatureType, Feature> {
            private FeatureSource<FeatureType, Feature> fSource;

            private ArrayList<Name> names = new ArrayList<Name>();

            public InputDataAccess(Collection<Feature> features, FeatureType schema, Name fTypeName) {
                InputFeatureCollection fCollection = new InputFeatureCollection(schema, features);
                fSource = new InputFeatureSource(fCollection, this);
                names.add(fTypeName);
                DataAccessRegistry.register(this);
            }

            public void createSchema(FeatureType featureType) throws IOException {
                throw new UnsupportedOperationException();
            }

            public void dispose() {
                this.fSource = null;
                this.names.clear();
                DataAccessRegistry.unregister(this);
            }

            public FeatureSource<FeatureType, Feature> getFeatureSource(Name typeName)
                    throws IOException {
                return fSource;
            }

            public ServiceInfo getInfo() {
                throw new UnsupportedOperationException();
            }

            public List<Name> getNames() throws IOException {
                return names;
            }

            public FeatureType getSchema(Name name) throws IOException {
                return fSource.getFeatures().getSchema();
            }

            public void updateSchema(Name typeName, FeatureType featureType) throws IOException {
                throw new UnsupportedOperationException();
            }
        }

        /**
         * This is a test feature source for non-app-schema complex features.
         * 
         * @author ang05a
         */
        private class InputFeatureSource implements FeatureSource<FeatureType, Feature> {
            private FeatureCollection<FeatureType, Feature> fCollection;

            private DataAccess<FeatureType, Feature> dataAccess;

            public InputFeatureSource(FeatureCollection<FeatureType, Feature> fCollection,
                    DataAccess<FeatureType, Feature> dataAccess) {
                this.fCollection = fCollection;
                this.dataAccess = dataAccess;
            }

            public void addFeatureListener(FeatureListener listener) {
                throw new UnsupportedOperationException();
            }

            public ReferencedEnvelope getBounds() throws IOException {
                throw new UnsupportedOperationException();
            }

            public ReferencedEnvelope getBounds(Query query) throws IOException {
                throw new UnsupportedOperationException();
            }

            public int getCount(Query query) throws IOException {
                return fCollection.size();
            }

            public DataAccess<FeatureType, Feature> getDataStore() {
                return dataAccess;
            }

            public FeatureCollection<FeatureType, Feature> getFeatures(Query query)
                    throws IOException {
                return getFeatures(query.getFilter());
            }

            public FeatureCollection<FeatureType, Feature> getFeatures(Filter filter)
                    throws IOException {
                return fCollection.subCollection(filter);
            }

            public FeatureCollection<FeatureType, Feature> getFeatures() throws IOException {
                return fCollection;
            }

            public ResourceInfo getInfo() {
                throw new UnsupportedOperationException();
            }

            public Name getName() {
                return fCollection.getSchema().getName();
            }

            public QueryCapabilities getQueryCapabilities() {
                throw new UnsupportedOperationException();
            }

            public FeatureType getSchema() {
                return fCollection.getSchema();
            }

            public Set<Key> getSupportedHints() {
                throw new UnsupportedOperationException();
            }

            public void removeFeatureListener(FeatureListener listener) {
                throw new UnsupportedOperationException();
            }
        }

        /**
         * This is a test feature collection of non-app-schema complex features
         * 
         * @author ang05a
         */
        private class InputFeatureCollection implements FeatureCollection<FeatureType, Feature> {
            private ArrayList<Feature> fList = new ArrayList<Feature>();

            private FeatureType schema;

            public InputFeatureCollection(FeatureType schema, Collection<Feature> features) {
                this.schema = schema;
                this.addAll(features);
            }

            public void accepts(FeatureVisitor visitor, ProgressListener progress)
                    throws IOException {
                throw new UnsupportedOperationException();
            }

            public boolean add(Feature obj) {
                return fList.add(obj);
            }

            public boolean addAll(Collection<? extends Feature> collection) {
                return fList.addAll(collection);
            }

            public boolean addAll(
                    FeatureCollection<? extends FeatureType, ? extends Feature> resource) {
                throw new UnsupportedOperationException();
            }

            public void addListener(CollectionListener listener) throws NullPointerException {
                throw new UnsupportedOperationException();
            }

            public void clear() {
                fList.clear();
            }

            public void close(FeatureIterator<Feature> close) {
                close.close();
            }

            public void close(Iterator<Feature> close) {
                ((InputFeatureIterator) close).close();
            }

            public boolean contains(Object o) {
                return fList.contains(o);
            }

            public boolean containsAll(Collection<?> o) {
                return fList.containsAll(o);
            }

            public FeatureIterator<Feature> features() {
                return new InputFeatureIterator(fList);
            }

            public ReferencedEnvelope getBounds() {
                throw new UnsupportedOperationException();
            }

            public String getID() {
                return null;
            }

            public FeatureType getSchema() {
                return schema;
            }

            public boolean isEmpty() {
                return this.fList.isEmpty();
            }

            public Iterator<Feature> iterator() {
                return (Iterator<Feature>) features();
            }

            public void purge() {
                throw new UnsupportedOperationException();
            }

            public boolean remove(Object o) {
                return this.fList.remove(o);
            }

            public boolean removeAll(Collection<?> c) {
                return this.fList.removeAll(c);
            }

            public void removeListener(CollectionListener listener) throws NullPointerException {
                throw new UnsupportedOperationException();
            }

            public boolean retainAll(Collection<?> c) {
                return this.fList.retainAll(c);
            }

            public int size() {
                return this.fList.size();
            }

            public FeatureCollection<FeatureType, Feature> sort(SortBy order) {
                throw new UnsupportedOperationException();
            }

            public FeatureCollection<FeatureType, Feature> subCollection(Filter filter) {
                if (filter == Filter.INCLUDE) {
                    return this;
                }
                FeatureCollection<FeatureType, Feature> fCollection = new InputFeatureCollection(
                        this.schema, new ArrayList<Feature>());

                for (Feature feature : this.fList) {
                    if (filter.evaluate(feature)) {
                        fCollection.add(feature);
                    }
                }
                return fCollection;
            }

            public Object[] toArray() {
                return fList.toArray();
            }

            public <O> O[] toArray(O[] a) {
                return fList.toArray(a);
            }
        }

        /**
         * This is a test feature iterator for non-app-schema complex features
         * 
         * @author ang05a
         */
        private class InputFeatureIterator implements Iterator<Feature>, FeatureIterator<Feature> {
            Iterator<Feature> iterator;

            public InputFeatureIterator(ArrayList<Feature> features) {
                iterator = features.iterator();
            }

            public void close() {
                iterator = null;
            }

            public boolean hasNext() {
                return iterator.hasNext();
            }

            public Feature next() throws NoSuchElementException {
                return iterator.next();
            }

            public void remove() {
                iterator.remove();
            }
        }
    }

    /**
     * Set filter factory with name spaces
     */
    public static void setFilterFactory() {
        NamespaceSupport namespaces = new NamespaceSupport();
        namespaces.declarePrefix("gsml", GSMLNS);
        namespaces.declarePrefix("gml", GMLNS);
        ff = new FilterFactoryImplNamespaceAware(namespaces);
    }
}
