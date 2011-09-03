package org.geotools.data.aggregate.sort;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.NoSuchElementException;

import org.geotools.TestData;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.DelegateSimpleFeatureReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

public class SortedReaderTest {

    SimpleFeatureReader fr;

    ShapefileDataStore store;

    FilterFactory ff;

    SortBy[] peopleAsc;

    SortBy[] peopleDesc;

    SortBy[] fidAsc;

    SimpleFeatureType schema;

    SimpleFeatureCollection fc;

    @Before
    public void setup() throws IOException {
        URL url = TestData.url("shapes/statepop.shp");
        store = new ShapefileDataStore(url);
        schema = store.getSchema();
        fc = store.getFeatureSource().getFeatures();
        fr = new DelegateSimpleFeatureReader(schema, fc.features());

        ff = CommonFactoryFinder.getFilterFactory(null);
        peopleAsc = new SortBy[] { ff.sort("PERSONS", SortOrder.ASCENDING) };
        peopleDesc = new SortBy[] { ff.sort("PERSONS", SortOrder.DESCENDING) };
        fidAsc = new SortBy[] { SortBy.NATURAL_ORDER };
    }

    @After
    public void tearDown() throws IOException {
        fr.close();
        store.dispose();
    }

    @Test
    public void testCanSort() {
        assertTrue(SortedFeatureReader.canSort(schema, peopleAsc));
        assertTrue(SortedFeatureReader.canSort(schema, peopleDesc));
        assertTrue(SortedFeatureReader.canSort(schema, fidAsc));
    }

    @Test
    public void testMemorySort() throws IOException {
        // make it so that we are not going to hit the disk
        SimpleFeatureReader sr = null;
        try {
            sr = new SortedFeatureReader(fr, peopleAsc, 1000);
            assertSortedOnPeopleAsc(sr);
        } finally {
            if (sr != null) {
                sr.close();
            }
        }
    }
    
    @Test
    public void testFileSortOne() throws IOException {
        // make it so that we are not going to hit the disk, but with a single feature
        SimpleFeatureReader sr = null;
        try {
            sr = new SortedFeatureReader(fr, peopleAsc, 0);
            assertSortedOnPeopleAsc(sr);
        } finally {
            if (sr != null) {
                sr.close();
            }
        }
    }

    @Test
    public void testFileSort() throws IOException {
        // make it so that we are not going to hit the disk
        SimpleFeatureReader sr = null;
        try {
            sr = new SortedFeatureReader(fr, peopleAsc, 5);
            assertSortedOnPeopleAsc(sr);
        } finally {
            if (sr != null) {
                sr.close();
            }
        }
    }

    @Test
    public void testIteratorSortReduce() throws IOException {
        // make it so that we are not going to hit the disk
        SimpleFeatureIterator fi = null;
        try {
            fi = new SortedFeatureIterator(fc.features(), schema, peopleAsc, 1000);
            assertSortedOnPeopleAsc(fi);
        } finally {
            if (fi != null) {
                fi.close();
            }
        }
    }

    @Test
    public void testSortDescending() throws IOException {
        // make it so that we are not going to hit the disk
        SimpleFeatureReader sr = null;
        try {
            sr = new SortedFeatureReader(fr, peopleDesc, 1000);
            double prev = -1;
            while (fr.hasNext()) {
                SimpleFeature f = fr.next();
                double curr = (Double) f.getAttribute("PERSONS");
                if (prev > 0) {
                    assertTrue(curr <= prev);
                }
                prev = curr;
            }
        } finally {
            if (sr != null) {
                sr.close();
            }
        }
    }

    @Test
    public void testSortNatural() throws IOException {
        // make it so that we are not going to hit the disk
        SimpleFeatureReader sr = null;
        try {
            sr = new SortedFeatureReader(fr, fidAsc, 1000);
            String prev = null;
            while (fr.hasNext()) {
                SimpleFeature f = fr.next();
                String id = f.getID();
                if (prev != null) {
                    assertTrue(id.compareTo(prev) >= 0);
                }
                prev = id;
            }
        } finally {
            if (sr != null) {
                sr.close();
            }
        }
    }

    private void assertSortedOnPeopleAsc(SimpleFeatureReader fr) throws IllegalArgumentException,
            NoSuchElementException, IOException {
        double prev = -1;
        while (fr.hasNext()) {
            SimpleFeature f = fr.next();
            double curr = (Double) f.getAttribute("PERSONS");
            if (prev > 0) {
                assertTrue(curr >= prev);
            }
            prev = curr;
        }
    }

    private void assertSortedOnPeopleAsc(SimpleFeatureIterator fi) throws IllegalArgumentException,
            NoSuchElementException, IOException {
        double prev = -1;
        while (fi.hasNext()) {
            SimpleFeature f = fi.next();
            double curr = (Double) f.getAttribute("PERSONS");
            if (prev > 0) {
                assertTrue(curr >= prev);
            }
            prev = curr;
        }
    }

}
