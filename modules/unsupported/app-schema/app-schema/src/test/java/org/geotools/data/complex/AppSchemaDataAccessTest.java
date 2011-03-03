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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator;
import org.geotools.data.complex.config.AppSchemaDataAccessDTO;
import org.geotools.data.complex.config.XMLConfigDigester;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.feature.Types;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.UniqueNameFeatureTypeFactoryImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.xml.sax.helpers.NamespaceSupport;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;

/**
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 * @source $URL$
 * @since 2.4
 */
public class AppSchemaDataAccessTest extends TestCase {

    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(AppSchemaDataAccessTest.class.getPackage().getName());

    Name targetName;

    FeatureType targetType;

    private AppSchemaDataAccess dataStore;

    FeatureTypeMapping mapping;

    protected void setUp() throws Exception {
        super.setUp();
        MemoryDataStore ds = createWaterSampleTestFeatures();
        targetType = TestData.createComplexWaterSampleType();
        FeatureTypeFactory tf = new UniqueNameFeatureTypeFactoryImpl();
        AttributeDescriptor targetFeature = tf.createAttributeDescriptor(targetType, targetType
                .getName(), 0, Integer.MAX_VALUE, true, null);
        targetName = targetFeature.getName();
        List mappings = TestData.createMappingsColumnsAndValues(targetFeature);

        Name sourceName = TestData.WATERSAMPLE_TYPENAME;
        SimpleFeatureSource source = ds.getFeatureSource(sourceName);

        // empty nssupport as the sample types have no namespace defined
        NamespaceSupport namespaces = new NamespaceSupport();
        mapping = new FeatureTypeMapping(source, targetFeature, mappings, namespaces);

        dataStore = new AppSchemaDataAccess(Collections.singleton(mapping));

    }

    protected void tearDown() throws Exception {
        super.tearDown();
        DataAccessRegistry.unregisterAll();
    }

    /*
     * Test method for 'org.geotools.data.complex.ComplexDataStore.getTypeNames()'
     */
    public void testGetTypeNames() throws IOException {
        Name[] typeNames = dataStore.getTypeNames();
        assertNotNull(typeNames);
        assertEquals(1, typeNames.length);
        assertEquals(targetName, typeNames[0]);

        // DataAccess interface:
        List names = dataStore.getNames();
        assertNotNull(names);
        assertEquals(1, names.size());
        assertEquals(targetName, names.get(0));
    }

    /*
     * Test method for 'org.geotools.data.complex.ComplexDataStore.getSchema(String)'
     */
    public void testTargetType() throws IOException {
        assertEquals(targetType, dataStore.getSchema(targetName));
    }

    public void testGetBounds() throws IOException {
        final String namespaceUri = "http://online.socialchange.net.au";
        final String localName = "RoadSegment";
        final Name typeName = Types.typeName(namespaceUri, localName);

        URL configUrl = getClass().getResource("/test-data/roadsegments.xml");

        AppSchemaDataAccessDTO config = new XMLConfigDigester().parse(configUrl);

        Set<FeatureTypeMapping> mappings = AppSchemaDataAccessConfigurator.buildMappings(config);

        dataStore = new AppSchemaDataAccess(mappings);
        FeatureSource<FeatureType, Feature> source = dataStore.getFeatureSource(typeName);

        FeatureTypeMapping mapping = (FeatureTypeMapping) mappings.iterator().next();

        FeatureSource<?,?> mappedSource = mapping.getSource();
        Envelope expected = getBounds(mappedSource);
        Envelope actual = getBounds(source);

        assertEquals(expected, actual);

    }

    // if someone can tell me how to write this with "? extends Feature" and still have it accepted
    // by features.close, I would like to know
    @SuppressWarnings("unchecked")
    private ReferencedEnvelope getBounds(FeatureSource source) {
        try {
            ReferencedEnvelope boundingBox = new ReferencedEnvelope(DefaultGeographicCRS.WGS84);
            FeatureCollection features = source.getFeatures();
            FeatureIterator iterator = features.features();
            try {
                while (iterator.hasNext()) {
                    Feature f = iterator.next();
                    boundingBox.include(f.getBounds());
                }
            } finally {
                iterator.close();
            }
            return boundingBox;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Test method for 'org.geotools.data.complex.ComplexDataStore.getFeatureReader(String)'
     */
    public void testGetFeatureReader() throws IOException {
        FeatureSource<FeatureType, Feature> access = dataStore.getFeatureSource(targetName);
        FeatureType type = access.getSchema();
        assertEquals(targetType, type);

        FeatureCollection<FeatureType, Feature> reader = access.getFeatures();
        assertNotNull(reader);

        FeatureIterator<Feature> features = reader.features();
        assertTrue(features.hasNext());

        Feature complexFeature = (Feature) features.next();
        assertNotNull(complexFeature);
        assertEquals(targetType, complexFeature.getType());

        features.close();

        org.opengis.filter.FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        PropertyName expr;
        Object value;

        expr = ff.property("measurement[1]");
        value = expr.evaluate(complexFeature);
        assertNotNull(value);

        expr = ff.property("measurement[1]/parameter");
        value = expr.evaluate(complexFeature);
        assertNotNull(value);

        expr = ff.property("measurement[1]/value");
        value = expr.evaluate(complexFeature);
        assertNotNull(value);

        expr = ff.property("measurement[2]/parameter");
        value = expr.evaluate(complexFeature);
        assertNotNull(value);

        expr = ff.property("measurement[2]/value");
        value = expr.evaluate(complexFeature);
        assertNotNull(value);

        expr = ff.property("measurement[3]/parameter");
        value = expr.evaluate(complexFeature);
        assertNotNull(value);

        expr = ff.property("measurement[3]/value");
        value = expr.evaluate(complexFeature);
        assertNotNull(value);

    }

    /*
     * Test method for 'org.geotools.data.AbstractDataStore.getFeatureSource(String)'
     */
    public void testGetFeatureSource() throws IOException {
        FeatureSource<FeatureType, Feature> complexSource = dataStore.getFeatureSource(targetName);
        assertNotNull(complexSource);
        assertEquals(targetType, complexSource.getSchema());
    }

    /*
     * Test method for 'org.geotools.data.AbstractDataStore.getFeatureReader(Query, Transaction)'
     */
    public void testGetFeatureReaderQuery() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

        PropertyName property = ff.property("sample/measurement[1]/parameter");
        Literal literal = ff.literal("ph");
        Filter filterParameter = ff.equals(property, literal);

        property = ff.property("sample/measurement[1]/value");
        literal = ff.literal(new Integer(3));
        Filter filterValue = ff.equals(property, literal);

        Filter filter = ff.and(filterParameter, filterValue);

        FeatureSource<FeatureType, Feature> complexSource = dataStore.getFeatureSource(targetName);
        FeatureCollection<FeatureType, Feature> features = complexSource.getFeatures(filter);

        FeatureIterator<Feature> reader = features.features();

        PropertyIsEqualTo equivalentSourceFilter = ff.equals(ff.property("ph"), ff
                .literal(new Integer(3)));
        FeatureCollection<?,?> collection = mapping.getSource()
                .getFeatures(equivalentSourceFilter);

        int count = 0;
        int expectedCount = collection.size();

        Filter badFilter = ff.greater(ff.property("sample/measurement[1]/value"), ff
                .literal(new Integer(3)));

        while (reader.hasNext()) {
            Feature f = (Feature) reader.next();
            assertNotNull(f);
            assertTrue(filter.evaluate(f));
            assertFalse(badFilter.evaluate(f));
            count++;
        }
        reader.close();
        assertEquals(expectedCount, count);
    }

    /**
     * Loads config from an xml config file which uses a property datastore as source of features.
     * 
     * @throws IOException
     */
    public void testWithConfig() throws Exception {
        final String nsUri = "http://online.socialchange.net.au";
        final String localName = "RoadSegment";
        final Name typeName = new NameImpl(nsUri, localName);

        final URL configUrl = getClass().getResource("/test-data/roadsegments.xml");

        AppSchemaDataAccessDTO config = new XMLConfigDigester().parse(configUrl);

        Set/* <FeatureTypeMapping> */mappings = AppSchemaDataAccessConfigurator
                .buildMappings(config);

        dataStore = new AppSchemaDataAccess(mappings);
        FeatureSource<FeatureType, Feature> source = dataStore.getFeatureSource(typeName);

        FeatureType type = source.getSchema();

        AttributeDescriptor node;
        node = (AttributeDescriptor) Types.descriptor(type, Types.typeName(nsUri, "the_geom"));
        assertNotNull(node);
        assertEquals("LineStringPropertyType", node.getType().getName().getLocalPart());

        assertNotNull(Types.descriptor(type, Types.typeName(nsUri, "name")));

        Name ftNodeName = Types.typeName(nsUri, "fromToNodes");
        assertNotNull(Types.descriptor(type, ftNodeName));

        AttributeDescriptor descriptor = (AttributeDescriptor) Types.descriptor(type, ftNodeName);

        ComplexType fromToNodes = (ComplexType) descriptor.getType();

        assertFalse(descriptor.isNillable());
        assertTrue(fromToNodes.isIdentified());

        Name fromNodeName = Types.typeName(nsUri, "fromNode");
        AttributeDescriptor fromNode = (AttributeDescriptor) Types.descriptor(fromToNodes,
                fromNodeName);
        assertNotNull(fromNode);

        Name toNodeName = Types.typeName(nsUri, "toNode");
        AttributeDescriptor toNode = (AttributeDescriptor) Types
                .descriptor(fromToNodes, toNodeName);
        assertNotNull(fromNode);

        assertEquals(Point.class, fromNode.getType().getBinding());
        assertEquals(Point.class, toNode.getType().getBinding());

        // test to see if the mapping can successfully substitute a valid narrower type 
        Name subName = Types.typeName(nsUri, "broadTypeEl");
        
        descriptor = (AttributeDescriptor) Types.descriptor(type, subName);

        ComplexType subbedType = (ComplexType) descriptor.getType();

        AttributeDescriptor sub = (AttributeDescriptor) Types
                .descriptor(subbedType, subName);
 //       assertNotNull(sub);

 //       assertEquals(Point.class, fromNode.getType().getBinding());
 

        FeatureCollection<FeatureType, Feature> content = source.getFeatures();
        FeatureIterator<Feature> features = content.features();
        int count = 0;
        final int expectedCount = 5;
        try {
            while (features.hasNext()) {
                Feature f = features.next();
                LOGGER.finest(String.valueOf(f));
                ++count;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            features.close();
        }
        assertEquals("feature count", expectedCount, count);

        // Test DefaultMappingFeatureIterator MaxFeatures support [GEOS-1930]
        final int expectedCount2 = 3;
        DefaultQuery query = new DefaultQuery();
        query.setMaxFeatures(expectedCount2);
        FeatureCollection<FeatureType, Feature> content2 = source.getFeatures(query);
        FeatureIterator<Feature> features2 = content2.features();
        int count2 = 0;
        try {
            while (features2.hasNext()) {
                Feature f = (Feature) features2.next();
                LOGGER.finest(String.valueOf(f));
                ++count2;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            features2.close();
        }
        assertEquals("feature count", expectedCount2, count2);

    }

    /**
     * Creates a MemoryDataStore contaning a simple FeatureType with test data for the "Multiple
     * columns could be mapped to a multi-value property" mapping case.
     * <p>
     * The structure of the "WaterSample" FeatureType is as follows: <table>
     * <tr>
     * <th>watersampleid</th>
     * <th>ph</th>
     * <th>temp</th>
     * <th>turbidity</th>
     * </tr>
     * <tr>
     * <td>watersample.1</td>
     * <td>7</td>
     * <td>21</td>
     * <td>0.6</td>
     * </tr>
     * </table>
     * </p>
     */
    public static MemoryDataStore createWaterSampleTestFeatures() throws Exception {
        MemoryDataStore dataStore = new MemoryDataStore();
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();

        tb.setName(TestData.WATERSAMPLE_TYPENAME.getLocalPart());
        tb.add("watersampleid", String.class);
        tb.add("ph", Integer.class);
        tb.add("temp", Integer.class);
        tb.add("turbidity", Float.class);

        SimpleFeatureType type = tb.buildFeatureType();

        dataStore.createSchema(type);

        final int NUM_FEATURES = 10;

        SimpleFeatureBuilder fbuilder = new SimpleFeatureBuilder(type);
        for (int i = 0; i < NUM_FEATURES; i++) {
            String fid = type.getName().getLocalPart() + "." + i;

            fbuilder.add("watersample." + i);
            fbuilder.add(new Integer(i));
            fbuilder.add(new Integer(10 + i));
            fbuilder.add(new Float(i));

            SimpleFeature f = fbuilder.buildFeature(fid);
            dataStore.addFeature(f);
        }
        return dataStore;
    }
}
