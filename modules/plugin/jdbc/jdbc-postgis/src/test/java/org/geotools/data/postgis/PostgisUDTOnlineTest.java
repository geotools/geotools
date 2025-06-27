/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.jdbc.JDBCUDTOnlineTest;
import org.geotools.jdbc.JDBCUDTTestSetup;
import org.junit.Test;

public class PostgisUDTOnlineTest extends JDBCUDTOnlineTest {

    @Override
    protected JDBCUDTTestSetup createTestSetup() {
        return new PostgisUDTTestSetup();
    }

    @Override
    public void testSchema() throws Exception {
        SimpleFeatureType type = dataStore.getSchema(tname("udt"));
        assertNotNull(type);
        assertNotNull(type.getDescriptor(aname("ut")));

        assertEquals(String.class, type.getDescriptor(aname("ut")).getType().getBinding());
        assertEquals(Integer.class, type.getDescriptor(aname("ut2")).getType().getBinding());
        assertEquals(Float.class, type.getDescriptor(aname("ut3")).getType().getBinding());
        assertEquals(Long.class, type.getDescriptor(aname("ut4")).getType().getBinding());
        assertEquals(Boolean.class, type.getDescriptor(aname("ut5")).getType().getBinding());
        assertEquals(Short.class, type.getDescriptor(aname("ut6")).getType().getBinding());
        assertEquals(Float.class, type.getDescriptor(aname("ut7")).getType().getBinding());
        assertEquals(Integer.class, type.getDescriptor(aname("ut8")).getType().getBinding());
        assertEquals(Time.class, type.getDescriptor(aname("ut9")).getType().getBinding());
        assertEquals(Time.class, type.getDescriptor(aname("ut10")).getType().getBinding());
        assertEquals(
                Timestamp.class, type.getDescriptor(aname("ut11")).getType().getBinding());
        assertEquals(
                Timestamp.class, type.getDescriptor(aname("ut12")).getType().getBinding());
        assertEquals(UUID.class, type.getDescriptor(aname("ut13")).getType().getBinding());
    }

    @Override
    public void testRead() throws Exception {
        SimpleFeatureCollection features =
                dataStore.getFeatureSource(tname("udt")).getFeatures();
        try (SimpleFeatureIterator fi = features.features()) {
            assertTrue(fi.hasNext());
            SimpleFeature item = fi.next();
            assertEquals("12ab", item.getAttribute(aname("ut")));
            assertEquals("6", item.getAttribute(aname("ut2")).toString());
            assertEquals("6.6", item.getAttribute(aname("ut3")).toString());
            assertEquals("85748957", item.getAttribute(aname("ut4")).toString());
            assertEquals("true", item.getAttribute(aname("ut5")).toString());
            assertEquals("3", item.getAttribute(aname("ut6")).toString());
            assertEquals("3.3", item.getAttribute(aname("ut7")).toString());
            assertEquals("2", item.getAttribute(aname("ut8")).toString());
            assertEquals("14:30:00", item.getAttribute(aname("ut9")).toString());
            assertEquals(
                    "2004-10-31 16:30:00.0", item.getAttribute(aname("ut11")).toString());
            assertEquals(
                    "2004-10-30 17:30:00.0", item.getAttribute(aname("ut12")).toString());
            assertEquals(
                    "00000000-0000-0000-0000-000000000000",
                    item.getAttribute(aname("ut13")).toString());

            assertFalse(fi.hasNext());
        }
    }

    @Test
    public void testBigDate() throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss G", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        SimpleFeatureType schema = dataStore.getSchema(tname("date_udt"));
        assertTrue(Date.class.isAssignableFrom(schema.getType("bd").getBinding()));

        FeatureSource source = dataStore.getFeatureSource(tname("date_udt"));

        Date d = date(source, "epoch");
        assertEquals("1970-01-01T00:00:00 AD", df.format(d));

        d = date(source, "epoch+1");
        assertEquals("1970-01-01T00:00:01 AD", df.format(d));

        d = date(source, "epoch-1");
        assertEquals("1969-12-31T23:59:59 AD", df.format(d));

        d = date(source, "ce");
        assertEquals("0001-01-01T00:00:00 AD", df.format(d));

        d = date(source, "bc");
        assertEquals("0001-12-31T23:59:59 BC", df.format(d));

        d = date(source, "min");
        assertEquals("292269055-12-02T16:47:04 BC", df.format(d));

        // test round trip
        try (FeatureWriter w = dataStore.getFeatureWriterAppend(tname("date_udt"), Transaction.AUTO_COMMIT)) {
            SimpleFeature f = (SimpleFeature) w.next();
            f.setAttribute(aname("bd"), new Date(-62135769600000L));
            f.setAttribute(aname("name"), "ce2");
            w.write();
        }

        d = date(source, "ce2");
        assertEquals("0001-01-01T00:00:00 AD", df.format(d));

        // test filters
        FilterFactory ff = dataStore.getFilterFactory();
        Filter filter = ff.equals(ff.property(aname("bd")), ff.literal(df.parse("1970-01-01T00:00:00 AD")));

        FeatureCollection features = source.getFeatures(filter);
        try (FeatureIterator it = features.features()) {
            assertTrue(it.hasNext());

            SimpleFeature f = (SimpleFeature) it.next();
            assertEquals("epoch", f.getAttribute("name"));

            assertFalse(it.hasNext());
        }

        filter = ff.greaterOrEqual(ff.property(aname("bd")), ff.literal(df.parse("1970-01-01T00:00:00 AD")));
        features = source.getFeatures(filter);

        Set<String> names = new HashSet<>();
        try (FeatureIterator it = features.features()) {
            while (it.hasNext()) {
                names.add(((SimpleFeature) it.next()).getAttribute("name").toString());
            }
        }

        assertEquals(2, names.size());
        assertTrue(names.contains("epoch"));
        assertTrue(names.contains("epoch+1"));
    }

    Date date(FeatureSource source, String name) throws Exception {
        return (Date) feature(source, name).getAttribute("bd");
    }

    SimpleFeature feature(FeatureSource source, String name) throws IOException {
        FilterFactory ff = dataStore.getFilterFactory();
        FeatureCollection features = source.getFeatures(ff.equals(ff.property(aname("name")), ff.literal(name)));
        try (FeatureIterator it = features.features()) {
            assertTrue("No feature with name: " + name, it.hasNext());

            return (SimpleFeature) it.next();
        }
    }
}
