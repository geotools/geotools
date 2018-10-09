/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2016, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, David Zwiers
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
package org.geotools.data.wfs.online;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.identity.FeatureId;

/** */
public class WFSOnlineTestSupport {

    private WFSOnlineTestSupport() {}

    public static WFSDataStore getDataStore(URL server, Boolean post) throws IOException {

        Map<String, Serializable> m = new HashMap<String, Serializable>();
        m.put(WFSDataStoreFactory.URL.key, server);
        m.put(WFSDataStoreFactory.PROTOCOL.key, false);
        m.put(WFSDataStoreFactory.GML_COMPATIBLE_TYPENAMES.key, true);
        m.put(WFSDataStoreFactory.TIMEOUT.key, new Integer(10000)); // not debug
        m.put(WFSDataStoreFactory.TIMEOUT.key, new Integer(1000000)); // for debug

        if (post != null) {
            m.put(WFSDataStoreFactory.PROTOCOL.key, Boolean.valueOf(post));
        }

        return new WFSDataStoreFactory().createDataStore(m);
    }

    public static void doFeatureType(DataStore wfs, String typeName) throws Exception {

        assertNotNull("No featureTypes", wfs.getTypeNames());

        // post
        SimpleFeatureType ft = wfs.getSchema(typeName);
        assertNotNull("DescribeFeatureType for " + typeName + " resulted in null", ft);

        GeometryDescriptor geometryDescriptor = ft.getGeometryDescriptor();
        List<AttributeDescriptor> attributeDescriptors = ft.getAttributeDescriptors();
        int attributeCount = ft.getAttributeCount();

        assertNotNull("CRS missing ", geometryDescriptor.getCoordinateReferenceSystem());

        assertTrue(
                "POST "
                        + typeName
                        + " must have 1 geom and atleast 1 other attribute -- fair assumption",
                geometryDescriptor != null && attributeDescriptors != null && attributeCount > 0);
    }

    public static void doFeatureReader(DataStore wfs, String typeName) throws Exception {
        assertNotNull("No featureTypes", wfs.getTypeNames());

        Query query = new Query(typeName);
        query.setMaxFeatures(5);

        FeatureReader<SimpleFeatureType, SimpleFeature> reader;

        reader = wfs.getFeatureReader(query, Transaction.AUTO_COMMIT);
        try {
            assertNotNull("FeatureType was null", reader);
            assertTrue("must have 1 feature -- fair assumption", reader.hasNext());

            SimpleFeature next = reader.next();
            assertNotNull(next);
        } finally {
            reader.close();
        }
    }

    public static void doFeatureReaderWithQuery(DataStore wfs, String typeName) throws Exception {
        SimpleFeatureType ft = wfs.getSchema(typeName);
        // take atleast attributeType 3 to avoid the undeclared one .. inherited optional attrs

        String[] props;
        props = new String[] {ft.getGeometryDescriptor().getLocalName()};

        Query query = new Query(ft.getTypeName());
        query.setPropertyNames(props);
        String fid = null;
        // get
        FeatureReader<SimpleFeatureType, SimpleFeature> fr =
                wfs.getFeatureReader(query, Transaction.AUTO_COMMIT);
        try {
            assertNotNull("FeatureType was null", ft);

            SimpleFeatureType featureType = fr.getFeatureType();
            if (ft.getAttributeCount() > 1) {
                assertEquals(
                        "Query must restrict feature type to only having 1 AttributeType",
                        1,
                        featureType.getAttributeCount());
            }
            assertTrue(
                    "must have 1 feature -- fair assumption", fr.hasNext() && featureType != null);
            SimpleFeature feature = fr.next();
            featureType = feature.getFeatureType();
            if (ft.getAttributeCount() > 1) {
                assertEquals(
                        "Query must restrict feature type to only having 1 AttributeType",
                        1,
                        featureType.getAttributeCount());
            }
            assertNotNull("must have 1 feature ", feature);
            fid = feature.getID();
            int j = 0;
            while (fr.hasNext()) {
                fr.next();
                j++;
            }
            // System.out.println(j + " Features");
        } finally {
            fr.close();
        }

        // test fid filter
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        query.setFilter(ff.id(ff.featureId(fid)));

        fr = wfs.getFeatureReader(query, Transaction.AUTO_COMMIT);
        try {
            assertNotNull("FeatureType was null", ft);
            int j = 0;
            while (fr.hasNext()) {
                assertEquals(fid, fr.next().getID());
                j++;
            }
            assertEquals(1, j);
        } finally {
            fr.close();
        }
    }

    public static void doFeatureReaderWithBBox(
            DataStore wfs, String typeName, ReferencedEnvelope bbox) throws Exception {
        SimpleFeatureType featureType = wfs.getSchema(typeName);

        // take atleast attributeType 3 to avoid the undeclared one .. inherited optional attrs
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Query query = new Query(featureType.getTypeName());
        PropertyName theGeom = ff.property(featureType.getGeometryDescriptor().getName());
        Filter filter = ff.bbox(theGeom, bbox);

        query.setFilter(filter);
        FeatureReader<SimpleFeatureType, SimpleFeature> fr =
                wfs.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertNotNull("GET " + typeName + " FeatureType was null", featureType);

        assertTrue("GET " + typeName + " has next?", fr.hasNext());
        assertNotNull("GET " + typeName + " has feature type", fr.getFeatureType());

        SimpleFeature feature = fr.next();

        assertNotNull("GET " + typeName + " has non null feature", feature);

        int j = 0;
        while (fr.hasNext()) {
            fr.next();
            j++;
        }
        // System.out.println("bbox selected " + j + " Features");
        fr.close();
    }

    public static Id doInsert(DataStore ds, SimpleFeatureType ft, SimpleFeatureCollection insert)
            throws Exception {
        Transaction t = new DefaultTransaction();
        ContentFeatureStore fs = (ContentFeatureStore) ds.getFeatureSource(ft.getTypeName());
        fs.setTransaction(t);
        // System.out.println("Insert Read 1");
        SimpleFeatureIterator fr = fs.getFeatures().features();
        int count1 = 0;
        while (fr.hasNext()) {
            count1++;
            fr.next();
        }
        fr.close();
        // System.out.println("Insert Add Features");
        List<FeatureId> fids1 = fs.addFeatures(insert);

        // System.out.println("Insert Read 2");
        fr = fs.getFeatures().features();
        int count2 = 0;
        while (fr.hasNext()) {
            count2++;
            fr.next();
        }
        fr.close();
        assertEquals(count1 + insert.size(), count2);

        FilterFactory fac = CommonFactoryFinder.getFilterFactory(null);
        Set<FeatureId> featureIds = new HashSet<FeatureId>();
        for (FeatureId id : fids1) {
            featureIds.add(id);
        }
        Id fidfilter = fac.id(featureIds);

        // System.out.println("Remove Inserted Features");
        fs.removeFeatures(fidfilter);

        // System.out.println("Insert Read 3");
        fr = fs.getFeatures().features();
        count2 = 0;
        while (fr.hasNext()) {
            count2++;
            fr.next();
        }
        fr.close();
        assertEquals(count1, count2);

        // System.out.println("Insert Add Features");
        fs.addFeatures(insert);

        // System.out.println("Insert Read 2");
        fr = fs.getFeatures().features();
        count2 = 0;
        while (fr.hasNext()) {
            count2++;
            fr.next();
        }
        fr.close();
        assertEquals(count1 + insert.size(), count2);

        // System.out.println("Insert Commit");
        t.commit();

        // System.out.println("Insert Read 3");
        fr = fs.getFeatures().features();
        int count3 = 0;
        while (fr.hasNext()) {
            count3++;
            fr.next();
        }
        fr.close();
        assertEquals(count2, count3);

        return fidfilter;
    }

    public static void doDelete(DataStore ds, SimpleFeatureType ft, Id ff) throws Exception {
        assertNotNull("doInsertFailed?", ff);
        Transaction t = new DefaultTransaction();
        SimpleFeatureStore fs = (SimpleFeatureStore) ds.getFeatureSource(ft.getTypeName());
        fs.setTransaction(t);

        // System.out.println("Delete Read 1");
        SimpleFeatureIterator fr = fs.getFeatures().features();
        int count1 = 0;
        while (fr.hasNext()) {
            count1++;
            fr.next();
        }
        fr.close();

        // System.out.println("Delete Remove " + ff);
        fs.removeFeatures(ff);

        // System.out.println("Delete Read 2");
        fr = fs.getFeatures().features();
        int count2 = 0;
        while (fr.hasNext()) {
            count2++;
            fr.next();
        }
        fr.close();
        assertTrue("Read 1 == " + count1 + " Read 2 == " + count2, count2 < count1);

        // System.out.println("Delete Commit");
        t.commit();

        // System.out.println("Delete Read 3");
        fr = fs.getFeatures().features();
        int count3 = 0;
        while (fr.hasNext()) {
            count3++;
            fr.next();
        }
        fr.close();
        assertTrue(count2 == count3);
    }

    public static void doUpdate(
            DataStore ds, SimpleFeatureType ft, String attributeToChange, Object newValue)
            throws Exception {
        Transaction t = new DefaultTransaction();
        SimpleFeatureStore fs = (SimpleFeatureStore) ds.getFeatureSource(ft.getTypeName());
        fs.setTransaction(t);

        AttributeDescriptor at = ft.getDescriptor(attributeToChange);
        assertNotNull("Attribute " + attributeToChange + " does not exist", at);

        FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);
        Filter f =
                filterFactory.equals(
                        filterFactory.property(at.getLocalName()), filterFactory.literal(newValue));

        // System.out.println("Update Read 1");
        SimpleFeatureIterator fr = fs.getFeatures(f).features();

        int count1 = 0;
        Object oldValue = null;
        if (fr != null)
            while (fr.hasNext()) {
                count1++;
                oldValue = fr.next().getAttribute(attributeToChange);
            }

        fr.close();
        // System.out.println("Update Modify");
        fs.modifyFeatures(at, newValue, Filter.INCLUDE);

        // System.out.println("Update Read 2");
        fr = fs.getFeatures(f).features();
        int count2 = 0;
        while (fr.hasNext()) {
            count2++;
            fr.next();
        }
        fr.close();
        assertTrue("Read 1 == " + count1 + " Read 2 == " + count2, count2 > count1);

        // System.out.println("Update Commit");
        try {
            t.commit();

            // System.out.println("Update Read 3");
            fr = fs.getFeatures(f).features();
            int count3 = 0;
            while (fr.hasNext()) {
                count3++;
                fr.next();
            }
            fr.close();
            assertEquals(count2, count3);
        } finally {
            // cleanup
            fs.modifyFeatures(at, oldValue, Filter.INCLUDE);
            t.commit();
        }
    }
}
