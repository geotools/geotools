package org.geotools.data.transform;

import static org.junit.Assert.*;

import java.awt.RenderingHints.Key;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ResourceInfo;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.Hints;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

import com.vividsolutions.jts.geom.Geometry;

public class TransformFeatureSourceTest extends AbstractTransformTest {

    @Test
    public void testInfo() throws Exception {
        SimpleFeatureSource transformed = transformWithSelection();
        ResourceInfo info = transformed.getInfo();
        assertEquals(transformed.getBounds(), info.getBounds());
        SimpleFeatureType schema = transformed.getSchema();
        assertEquals(schema.getCoordinateReferenceSystem(), info.getCRS());
        assertEquals(schema.getTypeName(), info.getName());
        assertEquals(schema.getTypeName(), info.getTitle());
        assertEquals(new URI(schema.getName().getNamespaceURI()), info.getSchema());
    }

    @Test
    public void testQueryCapabilities() throws Exception {
        SimpleFeatureSource transformed = transformWithSelection();
        QueryCapabilities caps = transformed.getQueryCapabilities();
        assertTrue(caps.isOffsetSupported());
        assertFalse(caps.isVersionSupported());
        QueryCapabilities originalCaps = STATES.getQueryCapabilities();
        assertEquals(originalCaps.isJoiningSupported(), caps.isJoiningSupported());
        assertEquals(originalCaps.isReliableFIDSupported(), caps.isReliableFIDSupported());
        assertEquals(originalCaps.isUseProvidedFIDSupported(), caps.isUseProvidedFIDSupported());
    }

    @Test
    public void testSupportedHints() throws Exception {
        SimpleFeatureSource transformed = transformWithSelection();
        Set<Key> hints = transformed.getSupportedHints();
        assertTrue(hints.contains(Hints.FEATURE_DETACHED));
    }

    @Test
    public void testSchemaSelect() throws Exception {
        SimpleFeatureSource transformed = transformWithSelection();
        SimpleFeatureType schema = transformed.getSchema();
        SimpleFeatureType original = STATES.getSchema();
        assertEquals(schema.getName(), transformed.getName());
        assertEquals("states_mini", schema.getTypeName());
        assertEquals(original.getName().getNamespaceURI(), schema.getName().getNamespaceURI());
        assertEquals(3, schema.getAttributeDescriptors().size());
        assertEquals(original.getDescriptor("the_geom"), schema.getDescriptor("the_geom"));
        assertEquals(original.getDescriptor("state_name"), schema.getDescriptor("state_name"));
        assertEquals(original.getDescriptor("persons"), schema.getDescriptor("persons"));
    }

    @Test
    public void testBoundsWithSelect() throws Exception {
        SimpleFeatureSource transformed = transformWithSelection();
        ReferencedEnvelope re = transformed.getBounds();
        ReferencedEnvelope ae = STATES.getBounds();
        assertEquals(re, ae);
    }

    @Test
    public void testBoundsWithSelectNoGeom() throws Exception {
        SimpleFeatureSource transformed = transformWithSelection();
        ReferencedEnvelope re = transformed.getBounds(new Query("states_mini", Filter.INCLUDE,
                new String[] { "state_name" }));
        assertNull(re);
    }

    @Test
    public void testCountWithSelect() throws Exception {
        SimpleFeatureSource transformed = transformWithSelection();
        int actual = transformed.getCount(Query.ALL);
        int expected = STATES.getCount(Query.ALL);
        assertEquals(expected, actual);

        actual = STATES.getCount(new Query("states", CQL.toFilter("state_name = 'Illinois'")));
        expected = transformed.getCount(new Query("states_mini", CQL
                .toFilter("state_name = 'Illinois'")));

        assertEquals(expected, actual);
    }

    @Test
    public void testFeaturesWithSelect() throws Exception {
        SimpleFeatureSource transformed = transformWithSelection();

        // some checks on the feature collection with the ALL query
        SimpleFeatureCollection fc = transformed.getFeatures();
        assertEquals(transformed.getSchema(), fc.getSchema());
        assertEquals(transformed.getCount(Query.ALL), fc.size());
        assertEquals(transformed.getBounds(), fc.getBounds());

        // and now with a specific one, here the property feature source will return null values
        Query q = new Query("states_mini", CQL.toFilter("state_name = 'Delaware'"));
        fc = transformed.getFeatures(q);
        assertEquals(transformed.getSchema(), fc.getSchema());
        assertEquals(1, fc.size());
        assertEquals(DELAWARE_BOUNDS, fc.getBounds());

        // and now read for good
        SimpleFeatureIterator fi = null;
        try {
            fi = fc.features();
            assertTrue(fi.hasNext());
            SimpleFeature f = fi.next();
            assertEquals(f.getFeatureType(), transformed.getSchema());
            assertNotNull(f.getDefaultGeometry());
            assertEquals(DELAWARE_BOUNDS, f.getBounds());
            assertEquals("Delaware", f.getAttribute("state_name"));
            assertEquals(666168d, f.getAttribute("persons"));
        } finally {
            if (fi != null) {
                fi.close();
            }
        }
    }

    @Test
    public void testSortFeaturesWithSelect() throws Exception {
        SimpleFeatureSource transformed = transformWithSelection();

        SortBy[] sortBy = new SortBy[] { FF.sort("state_name", SortOrder.DESCENDING) };

        // check we can sort
        assertTrue(transformed.getQueryCapabilities().supportsSorting(sortBy));

        // and now with a specific one, here the property feature source will return null values
        Query q = new Query("states_mini");
        q.setSortBy(sortBy);
        q.setMaxFeatures(2);
        SimpleFeatureCollection fc = transformed.getFeatures(q);
        assertEquals(transformed.getSchema(), fc.getSchema());
        assertEquals(2, fc.size());

        // and now read for good
        SimpleFeatureIterator fi = null;
        List<String> names = new ArrayList<String>();
        try {
            fi = fc.features();
            while (fi.hasNext()) {
                SimpleFeature f = fi.next();
                names.add((String) f.getAttribute("state_name"));
            }
        } finally {
            if (fi != null) {
                fi.close();
            }
        }

        assertEquals(2, names.size());
        assertEquals("West Virginia", names.get(0));
        assertEquals("Virginia", names.get(1));
    }

    @Test
    public void testSchemaRename() throws Exception {
        SimpleFeatureSource transformed = transformWithRename();
        SimpleFeatureType schema = transformed.getSchema();
        SimpleFeatureType original = STATES.getSchema();
        assertEquals("usa", schema.getTypeName());
        assertEquals(original.getName().getNamespaceURI(), schema.getName().getNamespaceURI());
        assertEquals(3, schema.getAttributeDescriptors().size());
        assertSimilarDescriptor(original.getDescriptor("the_geom"), schema.getDescriptor("geom"));
        assertSimilarDescriptor(original.getDescriptor("state_name"), schema.getDescriptor("name"));
        assertSimilarDescriptor(original.getDescriptor("persons"), schema.getDescriptor("people"));
    }

    @Test
    public void testBoundsWithRename() throws Exception {
        SimpleFeatureSource transformed = transformWithRename();
        ReferencedEnvelope re = transformed.getBounds();
        ReferencedEnvelope ae = STATES.getBounds();
        assertEquals(re, ae);
    }

    @Test
    public void testCountWithRename() throws Exception {
        SimpleFeatureSource transformed = transformWithSelection();
        int actual = transformed.getCount(Query.ALL);
        int expected = STATES.getCount(Query.ALL);
        assertEquals(expected, actual);

        actual = STATES.getCount(new Query("states", CQL.toFilter("state_name = 'Illinois'")));
        expected = transformed.getCount(new Query("usa", CQL.toFilter("name = 'Illinois'")));

        assertEquals(expected, actual);
    }

    @Test
    public void testFeaturesWithRename() throws Exception {
        SimpleFeatureSource transformed = transformWithRename();

        // some checks on the feature collection with the ALL query
        SimpleFeatureCollection fc = transformed.getFeatures();
        assertEquals(transformed.getSchema(), fc.getSchema());
        assertEquals(transformed.getCount(Query.ALL), fc.size());
        assertEquals(transformed.getBounds(), fc.getBounds());

        // and now with a specific one, here the property feature source will return null values
        Query q = new Query("usa", CQL.toFilter("name = 'Delaware'"));
        fc = transformed.getFeatures(q);
        assertEquals(transformed.getSchema(), fc.getSchema());
        assertEquals(1, fc.size());
        assertEquals(DELAWARE_BOUNDS, fc.getBounds());

        // and now read for good
        SimpleFeatureIterator fi = null;
        try {
            fi = fc.features();
            assertTrue(fi.hasNext());
            SimpleFeature f = fi.next();
            assertEquals(f.getFeatureType(), transformed.getSchema());
            assertNotNull(f.getDefaultGeometry());
            assertEquals(DELAWARE_BOUNDS, f.getBounds());
            assertEquals("Delaware", f.getAttribute("name"));
            assertEquals(666168d, f.getAttribute("people"));
        } finally {
            if (fi != null) {
                fi.close();
            }
        }
    }

    @Test
    public void testSortFeaturesWithRename() throws Exception {
        SimpleFeatureSource transformed = transformWithRename();

        SortBy[] sortBy = new SortBy[] { FF.sort("name", SortOrder.DESCENDING) };

        // check we can sort
        assertTrue(transformed.getQueryCapabilities().supportsSorting(sortBy));

        // and now with a specific one, here the property feature source will return null values
        Query q = new Query("usa");
        q.setSortBy(sortBy);
        q.setMaxFeatures(2);
        SimpleFeatureCollection fc = transformed.getFeatures(q);
        assertEquals(transformed.getSchema(), fc.getSchema());
        assertEquals(2, fc.size());

        // and now read for good
        SimpleFeatureIterator fi = null;
        List<String> names = new ArrayList<String>();
        try {
            fi = fc.features();
            while (fi.hasNext()) {
                SimpleFeature f = fi.next();
                names.add((String) f.getAttribute("name"));
            }
        } finally {
            if (fi != null) {
                fi.close();
            }
        }

        assertEquals(2, names.size());
        assertEquals("West Virginia", names.get(0));
        assertEquals("Virginia", names.get(1));
    }

    @Test
    public void testSchemaTransform() throws Exception {
        SimpleFeatureSource transformed = transformWithExpressions();
        SimpleFeatureType schema = transformed.getSchema();
        SimpleFeatureType original = STATES.getSchema();
        assertEquals("bstates", schema.getTypeName());
        assertEquals(original.getName().getNamespaceURI(), schema.getName().getNamespaceURI());
        assertEquals(4, schema.getAttributeDescriptors().size());
        GeometryDescriptor geom = (GeometryDescriptor) schema.getDescriptor("geom");
        assertNotNull(geom);
        assertEquals(Geometry.class, geom.getType().getBinding());
        assertEquals("EPSG:4326", CRS.lookupIdentifier(geom.getCoordinateReferenceSystem(), true));
        AttributeDescriptor name = schema.getDescriptor("name");
        assertEquals(String.class, name.getType().getBinding());
        AttributeDescriptor total = schema.getDescriptor("total");
        assertEquals(Double.class, total.getType().getBinding());
        AttributeDescriptor logp = schema.getDescriptor("logp");
        assertEquals(Double.class, logp.getType().getBinding());
    }

    @Test
    public void testBoundsWithTranform() throws Exception {
        SimpleFeatureSource transformed = transformWithExpressions();
        assertNull(transformed.getBounds());
    }

    @Test
    public void testCountWithTransform() throws Exception {
        SimpleFeatureSource transformed = transformWithSelection();
        int actual = transformed.getCount(Query.ALL);
        int expected = STATES.getCount(Query.ALL);
        assertEquals(expected, actual);

        actual = STATES.getCount(new Query("states", CQL.toFilter("state_name = 'Illinois'")));
        expected = transformed.getCount(new Query("bstates", CQL.toFilter("name = 'illinois'")));

        assertEquals(expected, actual);
    }

    @Test
    public void testFeaturesWithTransform() throws Exception {
        SimpleFeatureSource transformed = transformWithExpressions();

        // some checks on the feature collection with the ALL query
        SimpleFeatureCollection fc = transformed.getFeatures();
        assertEquals(transformed.getSchema(), fc.getSchema());
        assertEquals(transformed.getCount(Query.ALL), fc.size());
        ReferencedEnvelope bufferedStateBounds = new ReferencedEnvelope(-110.04782099895442,
                -74.04752638847438, 34.98970859966714, 43.50933565139621, WGS84);
        assertEquals(bufferedStateBounds, fc.getBounds());

        // and now with a specific one, here the property feature source will return null values
        Query q = new Query("usa", CQL.toFilter("name = 'delaware'"));
        fc = transformed.getFeatures(q);
        assertEquals(transformed.getSchema(), fc.getSchema());
        assertEquals(1, fc.size());
        ReferencedEnvelope bufferedDelawareBounds = new ReferencedEnvelope(-76.78885040499915,
                -74.05085782368926, 37.450389376543036, 40.82235239392104, WGS84);
        assertEquals(bufferedDelawareBounds, fc.getBounds());

        // and now read for good
        SimpleFeatureIterator fi = null;
        try {
            fi = fc.features();
            assertTrue(fi.hasNext());
            SimpleFeature f = fi.next();
            assertEquals(f.getFeatureType(), transformed.getSchema());
            assertNotNull(f.getDefaultGeometry());
            assertEquals(bufferedDelawareBounds, f.getBounds());
            assertEquals("delaware", f.getAttribute("name"));
            assertEquals(666168d, f.getAttribute("total"));
            assertEquals(Math.log(666168), f.getAttribute("logp"));
        } finally {
            if (fi != null) {
                fi.close();
            }
        }
    }

    @Test
    public void testSortFeaturesWithTransform() throws Exception {
        SimpleFeatureSource transformed = transformWithExpressions();

        SortBy[] sortBy = new SortBy[] { FF.sort("total", SortOrder.DESCENDING) };

        // check we can sort
        assertTrue(transformed.getQueryCapabilities().supportsSorting(sortBy));

        // and now with a specific one, here the property feature source will return null values
        Query q = new Query("bstates");
        q.setSortBy(sortBy);
        q.setMaxFeatures(2);
        SimpleFeatureCollection fc = transformed.getFeatures(q);
        assertEquals(transformed.getSchema(), fc.getSchema());
        assertEquals(2, fc.size());

        // and now read for good
        SimpleFeatureIterator fi = null;
        List<Number> totals = new ArrayList<Number>();
        try {
            fi = fc.features();
            while (fi.hasNext()) {
                SimpleFeature f = fi.next();
                totals.add((Number) f.getAttribute("total"));
            }
        } finally {
            if (fi != null) {
                fi.close();
            }
        }

        // grab the two biggest from the original data set
        List<Double> sums = new ArrayList<Double>();
        try {
            fi = STATES.getFeatures().features();
            while (fi.hasNext()) {
                SimpleFeature f = fi.next();
                double male = (Double) f.getAttribute("male");
                double female = (Double) f.getAttribute("female");
                sums.add(male + female);
            }

        } finally {
            if (fi != null) {
                fi.close();
            }
        }
        Collections.sort(sums);

        assertEquals(2, totals.size());
        assertEquals(sums.get(sums.size() - 1), totals.get(0));
        assertEquals(sums.get(sums.size() - 2), totals.get(1));
    }

    void assertSimilarDescriptor(AttributeDescriptor expected, AttributeDescriptor actual) {
        if (actual == null) {
            fail("Actual attribute descriptor is null");
        }

        assertEquals(actual.getDefaultValue(), expected.getDefaultValue());
        assertEquals(actual.getMinOccurs(), expected.getMinOccurs());
        assertEquals(actual.getMaxOccurs(), expected.getMaxOccurs());
        assertEquals(actual.getUserData(), expected.getUserData());

        AttributeType at = actual.getType();
        AttributeType et = expected.getType();
        assertEquals(et.getBinding(), at.getBinding());
        assertEquals(et.getDescription(), et.getDescription());
        assertEquals(et.getRestrictions(), et.getRestrictions());
        assertEquals(et.getSuper(), et.getSuper());
        assertEquals(et.getUserData(), et.getUserData());
    }

}
