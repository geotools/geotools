/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2011, Open Source Geospatial Foundation (OSGeo)
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.Query;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.ComplexType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.FeatureTypeFactory;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.data.DataUtilities;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator;
import org.geotools.data.complex.config.AppSchemaDataAccessDTO;
import org.geotools.data.complex.config.XMLConfigDigester;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.data.complex.feature.type.UniqueNameFeatureTypeFactoryImpl;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.AppSchemaTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Point;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * @author Gabriel Roldan (Axios Engineering)
 * @version $Id$
 * @since 2.4
 */
public class AppSchemaDataAccessTest extends AppSchemaTestSupport {

    Name targetName;

    FeatureType targetType;

    private AppSchemaDataAccess dataStore;

    FeatureTypeMapping mapping;

    @Before
    public void setUp() throws Exception {
        MemoryDataStore ds = createWaterSampleTestFeatures();
        targetType = TestData.createComplexWaterSampleType();
        FeatureTypeFactory tf = new UniqueNameFeatureTypeFactoryImpl();
        AttributeDescriptor targetFeature =
                tf.createAttributeDescriptor(
                        targetType, targetType.getName(), 0, Integer.MAX_VALUE, true, null);
        targetName = targetFeature.getName();
        List<AttributeMapping> mappings = TestData.createMappingsColumnsAndValues(targetFeature);

        Name sourceName = TestData.WATERSAMPLE_TYPENAME;
        FeatureSource<SimpleFeatureType, SimpleFeature> source = ds.getFeatureSource(sourceName);

        // empty nssupport as the sample types have no namespace defined
        NamespaceSupport namespaces = new NamespaceSupport();
        mapping = new FeatureTypeMapping(source, targetFeature, mappings, namespaces);

        dataStore = new AppSchemaDataAccess(Collections.singleton(mapping));
    }

    @After
    public void tearDown() throws Exception {
        DataAccessRegistry.unregisterAndDisposeAll();
    }

    /*
     * Test method for 'org.geotools.data.complex.ComplexDataStore.getTypeNames()'
     */
    @Test
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
    @Test
    public void testTargetType() throws IOException {
        assertEquals(targetType, dataStore.getSchema(targetName));
    }

    @Test
    public void testGetBounds() throws IOException {
        final String namespaceUri = "http://online.socialchange.net.au";
        final String localName = "RoadSegment";
        final Name typeName = Types.typeName(namespaceUri, localName);

        URL configUrl = getClass().getResource("/test-data/roadsegments.xml");

        AppSchemaDataAccessDTO config = new XMLConfigDigester().parse(configUrl);

        Set<FeatureTypeMapping> mappings = AppSchemaDataAccessConfigurator.buildMappings(config);

        dataStore = new AppSchemaDataAccess(mappings);
        FeatureSource<FeatureType, Feature> source = dataStore.getFeatureSource(typeName);

        FeatureTypeMapping mapping = mappings.iterator().next();

        FeatureSource<?, ?> mappedSource = mapping.getSource();
        Envelope expected = getBounds(mappedSource);
        Envelope actual = getBounds(source);

        assertEquals(expected, actual);
    }

    // if someone can tell me how to write this with "? extends Feature" and still have it accepted
    // by features.close, I would like to know
    private ReferencedEnvelope getBounds(FeatureSource source) {
        try {
            ReferencedEnvelope boundingBox = new ReferencedEnvelope(DefaultGeographicCRS.WGS84);
            FeatureCollection features = source.getFeatures();
            try (FeatureIterator iterator = features.features()) {
                while (iterator.hasNext()) {
                    Feature f = iterator.next();
                    boundingBox.include(f.getBounds());
                }
            }
            return boundingBox;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Test method for 'org.geotools.data.complex.ComplexDataStore.getFeatureReader(String)'
     */
    @Test
    public void testGetFeatureReader() throws IOException {
        FeatureSource<FeatureType, Feature> access = dataStore.getFeatureSource(targetName);
        FeatureType type = access.getSchema();
        assertEquals(targetType, type);

        FeatureCollection<FeatureType, Feature> reader = access.getFeatures();
        assertNotNull(reader);

        Feature complexFeature;
        try (FeatureIterator<Feature> features = reader.features()) {
            assertTrue(features.hasNext());

            complexFeature = features.next();
            assertNotNull(complexFeature);
            assertEquals(targetType, complexFeature.getType());
        }

        org.geotools.api.filter.FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

        PropertyName expr = ff.property("measurement[1]");
        Object value = expr.evaluate(complexFeature);
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
    @Test
    public void testGetFeatureSource() throws IOException {
        FeatureSource<FeatureType, Feature> complexSource = dataStore.getFeatureSource(targetName);
        assertNotNull(complexSource);
        assertEquals(targetType, complexSource.getSchema());
    }

    /*
     * Test method for 'org.geotools.data.AbstractDataStore.getFeatureReader(Query, Transaction)'
     */
    @Test
    public void testGetFeatureReaderQuery() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

        PropertyName property = ff.property("sample/measurement[1]/parameter");
        Literal literal = ff.literal("ph");
        Filter filterParameter = ff.equals(property, literal);

        property = ff.property("sample/measurement[1]/value");
        literal = ff.literal(Integer.valueOf(3));
        Filter filterValue = ff.equals(property, literal);

        Filter filter = ff.and(filterParameter, filterValue);

        FeatureSource<FeatureType, Feature> complexSource = dataStore.getFeatureSource(targetName);
        FeatureCollection<FeatureType, Feature> features = complexSource.getFeatures(filter);

        try (FeatureIterator<Feature> reader = features.features()) {
            PropertyIsEqualTo equivalentSourceFilter =
                    ff.equals(ff.property("ph"), ff.literal(Integer.valueOf(3)));
            FeatureCollection<?, ?> collection =
                    mapping.getSource().getFeatures(equivalentSourceFilter);

            int count = 0;
            int expectedCount = collection.size();

            Filter badFilter =
                    ff.greater(
                            ff.property("sample/measurement[1]/value"),
                            ff.literal(Integer.valueOf(3)));

            while (reader.hasNext()) {
                Feature f = reader.next();
                assertNotNull(f);
                assertTrue(filter.evaluate(f));
                assertFalse(badFilter.evaluate(f));
                count++;
            }
            assertEquals(expectedCount, count);
        }
    }

    /**
     * Loads config from an xml config file which uses a property datastore as source of features.
     */
    @Test
    public void testWithConfig() throws Exception {
        final String nsUri = "http://online.socialchange.net.au";
        final String localName = "RoadSegment";
        final Name typeName = new NameImpl(nsUri, localName);

        final URL configUrl = getClass().getResource("/test-data/roadsegments.xml");

        AppSchemaDataAccessDTO config = new XMLConfigDigester().parse(configUrl);

        Set<FeatureTypeMapping> mappings = AppSchemaDataAccessConfigurator.buildMappings(config);

        dataStore = new AppSchemaDataAccess(mappings);
        FeatureSource<FeatureType, Feature> source = dataStore.getFeatureSource(typeName);

        FeatureType type = source.getSchema();

        AttributeDescriptor node =
                (AttributeDescriptor) Types.descriptor(type, Types.typeName(nsUri, "the_geom"));
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
        AttributeDescriptor fromNode =
                (AttributeDescriptor) Types.descriptor(fromToNodes, fromNodeName);
        assertNotNull(fromNode);

        Name toNodeName = Types.typeName(nsUri, "toNode");
        AttributeDescriptor toNode =
                (AttributeDescriptor) Types.descriptor(fromToNodes, toNodeName);
        assertNotNull(fromNode);

        assertEquals(Point.class, fromNode.getType().getBinding());
        assertEquals(Point.class, toNode.getType().getBinding());

        // test to see if the mapping can successfully substitute a valid narrower type
        FeatureCollection<FeatureType, Feature> content = source.getFeatures();
        int count = DataUtilities.count(content);
        final int expectedCount = 5;
        assertEquals("feature count", expectedCount, count);

        // Test DefaultMappingFeatureIterator MaxFeatures support [GEOS-1930]
        final int expectedCount2 = 3;
        Query query = new Query();
        query.setMaxFeatures(expectedCount2);
        FeatureCollection<FeatureType, Feature> content2 = source.getFeatures(query);
        int count2 = DataUtilities.count(content2);
        assertEquals("feature count", expectedCount2, count2);
    }

    /**
     * Creates a MemoryDataStore contaning a simple FeatureType with test data for the "Multiple
     * columns could be mapped to a multi-value property" mapping case.
     *
     * <p>The structure of the "WaterSample" FeatureType is as follows:
     *
     * <table>
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
            fbuilder.add(Integer.valueOf(i));
            fbuilder.add(Integer.valueOf(10 + i));
            fbuilder.add(Float.valueOf(i));

            SimpleFeature f = fbuilder.buildFeature(fid);
            dataStore.addFeature(f);
        }
        return dataStore;
    }
}
