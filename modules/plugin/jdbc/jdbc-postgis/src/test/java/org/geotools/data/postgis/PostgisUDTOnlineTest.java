/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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

import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.Arrays;
import java.util.Date;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.postgis.PostGISDialect.XDate;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.jdbc.JDBCUDTOnlineTest;
import org.geotools.jdbc.JDBCUDTTestSetup;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;

/**
 * 
 *
 * @source $URL$
 */
public class PostgisUDTOnlineTest extends JDBCUDTOnlineTest {

    @Override
    protected JDBCUDTTestSetup createTestSetup() {
        return new PostgisUDTTestSetup();
    }

    public void testBigDates() throws Exception {
        FeatureWriter w = dataStore.getFeatureWriterAppend(tname("bigdates"), Transaction.AUTO_COMMIT);
        w.hasNext();
        
        SimpleFeature f = (SimpleFeature) w.next();
        f.setAttribute(aname("d"), new java.util.Date());
        
        w.write();
        w.close();
        
        SimpleFeatureCollection features = dataStore.getFeatureSource(tname("bigdates")).getFeatures();
        SimpleFeatureIterator fi = features.features();
        while (fi.hasNext()) {
            SimpleFeature n = fi.next();
            System.out.println(n.getAttribute("d").getClass());
        }
        
        fi.close();
        
        // build query to grab the dimension values
        final Query dimQuery = new Query( tname("bigdates") );
        dimQuery.setPropertyNames(Arrays.asList("d"));
        features = dataStore.getFeatureSource(tname("bigdates")).getFeatures(dimQuery);
        
        UniqueVisitor v = new UniqueVisitor("d");
        features.accepts(v, null);
        System.out.println(v.getUnique());
        
        SimpleFeatureType schema = dataStore.getSchema(tname("bigdates"));
        SimpleFeatureType newFeatureType = FeatureTypes.newFeatureType(schema.getAttributeDescriptors().toArray(new AttributeDescriptor[0]), "bigdates2");
        dataStore.createSchema(newFeatureType);
        schema = dataStore.getSchema(tname("bigdates2"));
        assertEquals(XDate.class, schema.getType("d").getBinding());
        
        // make sure the string query works
        FilterFactory ff = dataStore.getFilterFactory();
        // @todo revisit - not sure if string query will ever get passed in this formatting...
//        PropertyIsEqualTo eq = ff.equals(ff.property("d"), ff.literal(new Date().toString()));
//        features = dataStore.getFeatureSource(tname("bigdates")).getFeatures(eq);
//        System.out.println(features.size());
        
    }
    
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
        assertEquals(Timestamp.class, type.getDescriptor(aname("ut11")).getType().getBinding());
        assertEquals(Timestamp.class, type.getDescriptor(aname("ut12")).getType().getBinding());
        assertEquals(UUID.class, type.getDescriptor(aname("ut13")).getType().getBinding());
    }
    
    public void testRead() throws Exception {
        SimpleFeatureType type = dataStore.getSchema(tname("udt"));
            
        SimpleFeatureCollection features = dataStore.getFeatureSource(tname("udt")).getFeatures();
        SimpleFeatureIterator fi = null;
        try {
            fi = features.features();
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
            assertEquals("2004-10-31 16:30:00.0", item.getAttribute(aname("ut11")).toString());
            assertEquals("2004-10-30 17:30:00.0", item.getAttribute(aname("ut12")).toString());
            assertEquals("00000000-0000-0000-0000-000000000000", item.getAttribute(aname("ut13")).toString());

            assertFalse(fi.hasNext());
        } finally { 
            fi.close();
        }
        
    }

}
