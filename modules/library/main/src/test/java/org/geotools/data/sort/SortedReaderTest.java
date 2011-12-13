package org.geotools.data.sort;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Date;
import java.util.NoSuchElementException;

import org.geotools.data.simple.DelegateSimpleFeatureReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * 
 *
 * @source $URL$
 */
public class SortedReaderTest {

    SimpleFeatureReader fr;

    FilterFactory ff;

    SortBy[] peopleAsc;

    SortBy[] peopleDesc;

    SortBy[] fidAsc;

    SimpleFeatureType schema;

    SimpleFeatureCollection fc;

    private SortBy[] dateAsc;

    @Before
    public void setup() throws IOException {
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();

        typeBuilder.setName("test");
        typeBuilder.setNamespaceURI("test");
        typeBuilder.setCRS(DefaultGeographicCRS.WGS84);
        typeBuilder.add("defaultGeom", Point.class, DefaultGeographicCRS.WGS84);
        typeBuilder.add("PERSONS", Integer.class);
        typeBuilder.add("byte", Byte.class);
        typeBuilder.add("short", Short.class);
        typeBuilder.add("long", Long.class);
        typeBuilder.add("float", Float.class);
        typeBuilder.add("double", Double.class);
        typeBuilder.add("date", Date.class);
        typeBuilder.add("sql_date", java.sql.Date.class);
        typeBuilder.add("sql_time", java.sql.Time.class);
        typeBuilder.add("sql_timestamp", java.sql.Timestamp.class);
        typeBuilder.add("otherGeom", LineString.class);
        typeBuilder.setDefaultGeometry("defaultGeom");

        schema = (SimpleFeatureType) typeBuilder.buildFeatureType();

        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(schema);

        GeometryFactory gf = new GeometryFactory();
        fc = new DefaultFeatureCollection("test", schema);

        double x = -140;
        double y = 45;
        final int features = 500;
        for (int i = 0; i < features; i++) {
            Point point = gf.createPoint(new Coordinate(x + i, y + i));
            point.setUserData(DefaultGeographicCRS.WGS84);

            builder.add(point);
            builder.add(new Integer(i));
            builder.add(new Byte((byte) i));
            builder.add(new Short((short) i));
            builder.add(new Long(i));
            builder.add(new Float(i));
            builder.add(new Double(i));
            builder.add(new Date());
            builder.add(new java.sql.Date(System.currentTimeMillis()));
            builder.add(new java.sql.Time(System.currentTimeMillis()));
            builder.add(new java.sql.Timestamp(System.currentTimeMillis()));

            LineString line = gf.createLineString(new Coordinate[] { new Coordinate(x + i, y + i),
                    new Coordinate(x + i + 1, y + i + 1) });
            line.setUserData(DefaultGeographicCRS.WGS84);
            builder.add(line);

            fc.add(builder.buildFeature(i + ""));
        }

        // add a feature with a null geometry
        builder.add(null);
        builder.add(new Integer(-1));
        builder.add(null);
        fc.add(builder.buildFeature((features + 1) + ""));

        fr = new DelegateSimpleFeatureReader(schema, fc.features());

        ff = CommonFactoryFinder.getFilterFactory(null);
        peopleAsc = new SortBy[] { ff.sort("PERSONS", SortOrder.ASCENDING) };
        peopleDesc = new SortBy[] { ff.sort("PERSONS", SortOrder.DESCENDING) };
        dateAsc = new SortBy[] { ff.sort("date", SortOrder.ASCENDING) };
        fidAsc = new SortBy[] { SortBy.NATURAL_ORDER };
    }

    @After
    public void tearDown() throws IOException {
        fr.close();
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
    public void testFileSortDate() throws IOException {
        // make it so that we are not going to hit the disk
        SimpleFeatureReader sr = null;
        try {
            sr = new SortedFeatureReader(fr, dateAsc, 100);
            assertSortedOnDateAsc(sr);
        } finally {
            if (sr != null) {
                sr.close();
            }
        }
    }

    @Test
    public void testFileSortPeople() throws IOException {
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
            int curr = (Integer) f.getAttribute("PERSONS");
            if (prev > 0) {
                assertTrue(curr >= prev);
            }
            prev = curr;
        }
    }

    private void assertSortedOnDateAsc(SimpleFeatureReader fr) throws IllegalArgumentException,
            NoSuchElementException, IOException {
        Date prev = null;
        while (fr.hasNext()) {
            SimpleFeature f = fr.next();
            Date curr = (Date) f.getAttribute("date");
            if (prev != null) {
                assertTrue(prev.compareTo(curr) <= 0);
            }
            prev = curr;
        }
    }

    private void assertSortedOnPeopleAsc(SimpleFeatureIterator fi) throws IllegalArgumentException,
            NoSuchElementException, IOException {
        double prev = -1;
        while (fi.hasNext()) {
            SimpleFeature f = fi.next();
            int curr = (Integer) f.getAttribute("PERSONS");
            if (prev > 0) {
                assertTrue(curr >= prev);
            }
            prev = curr;
        }
    }

}
