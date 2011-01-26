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
package org.geotools.data.shapefile.indexed;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geotools.data.DefaultQuery;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.spatial.BBOX;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class FidQueryTest extends FIDTestCase {
    public  FidQueryTest(  ) throws IOException {
        super("FidQueryTest");
    }

    private IndexedShapefileDataStore ds;

    private static final FilterFactory2 fac = CommonFactoryFinder
            .getFilterFactory2(null);
    Map<String, SimpleFeature> fids = new HashMap<String, SimpleFeature>();

    SimpleFeatureStore featureStore;

    private int numFeatures;

    protected void setUp() throws Exception {

        super.setUp();

        URL url = backshp.toURI().toURL();
        ds = new IndexedShapefileDataStore(url, null, false, true,
                IndexType.QIX);
        numFeatures = 0;
        featureStore = (SimpleFeatureStore) ds.getFeatureSource();
        {
            SimpleFeatureIterator features = featureStore.getFeatures().features();
            try {
                while (features.hasNext()) {
                    numFeatures++;
                    SimpleFeature feature = features.next();
                    fids.put(feature.getID(), feature);
                }
            } finally {
                if (features != null)
                    features.close();
            }
            assertEquals(numFeatures, fids.size());
        }

    }
    
    @Override
    protected void tearDown() throws Exception {
    	ds.dispose();
    	super.tearDown();
    }

    public void testGetByFID() throws Exception {
        assertFidsMatch();
    }
    public void testAddFeature() throws Exception {
        SimpleFeature feature = fids.values().iterator().next();
        SimpleFeatureType schema = ds.getSchema();

        SimpleFeatureBuilder build = new SimpleFeatureBuilder(schema);
        GeometryFactory gf = new GeometryFactory();
        build.add(gf.createPoint((new Coordinate(0, 0))));
        build.add(new Long(0));
        build.add(new Long(0));
        build.add("Hey");
        SimpleFeature newFeature = build.buildFeature(null);
        SimpleFeatureCollection collection = FeatureCollections.newCollection();
        collection.add(newFeature);

        List<FeatureId> newFids = featureStore.addFeatures(collection);
        assertEquals(1, newFids.size());
        // this.assertFidsMatch();

        DefaultQuery query = new DefaultQuery(schema.getTypeName());        
        FeatureId id = newFids.iterator().next();
        String fid = id.getID();
        
        Filter filter = fac.id(Collections.singleton(id));
        query.setFilter(filter);
        SimpleFeatureIterator features = featureStore.getFeatures(query).features();
        try {
            feature = features.next();
            for (int i = 0; i < schema.getAttributeCount(); i++) {
                Object value = feature.getAttribute(i);
                Object newValue = newFeature.getAttribute(i);

                if (value instanceof Geometry) {
                    assertTrue(((Geometry) newValue).equals((Geometry) value));
                } else {
                    assertEquals(newValue, value);
                }
            }
            assertFalse(features.hasNext());
        } finally {
            if (features != null)
                features.close();
        }
    }
 
    public void testModifyFeature() throws Exception {
        SimpleFeature feature = this.fids.values().iterator().next();
        int newId = 237594123;

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

        Id createFidFilter = ff.id(Collections.singleton(ff.featureId(feature
                .getID())));

        SimpleFeatureType schema = feature.getFeatureType();
        featureStore.modifyFeatures(schema.getDescriptor("ID"), new Integer(
                newId), createFidFilter);

        SimpleFeatureIterator features = featureStore.getFeatures(createFidFilter)
                .features();
        try {
            assertFalse(feature.equals(features.next()));
        } finally {
            if (features != null) {
                features.close();
            }
        }
        feature.setAttribute("ID", new Integer(newId));
        this.assertFidsMatch();
    }

    public void testDeleteFeature() throws Exception {
        SimpleFeatureIterator features = featureStore.getFeatures().features();
        SimpleFeature feature;
        try {
            feature = features.next();
        } finally {
            if (features != null)
                features.close();
        }
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Id createFidFilter = ff.id(Collections.singleton(ff.featureId(feature
                .getID())));

        featureStore.removeFeatures(createFidFilter);
        fids.remove(feature.getID());

        assertEquals(fids.size(), featureStore.getCount(Query.ALL));

        features = featureStore.getFeatures(createFidFilter).features();
        try {
            assertFalse(features.hasNext());
        } finally {
            if (features != null)
                features.close();
        }

        this.assertFidsMatch();

    }
/*
    public void testFIDBBoxQuery() throws Exception {
        SimpleFeatureIterator features = featureStore.getFeatures().features();
        SimpleFeature feature;
        try {
            feature = features.next();
            feature = features.next();
            feature = features.next();
        } finally {
            if (features != null)
                features.close();
        }
        // FilterFactory factory = FilterFactoryFinder.createFilterFactory();
        // BBoxExpression bb =
        // factory.createBBoxExpression(feature.getBounds());
        //
        // GeometryFilter bboxFilter =
        // factory.createGeometryFilter(FilterType.GEOMETRY_INTERSECTS);
        // bboxFilter.addRightGeometry(bb);
        //
        // String geom = ds.getSchema().getDefaultGeometry().getLocalName();
        //
        // bboxFilter.addLeftGeometry(factory.createAttributeExpression(geom));

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        BBOX bbox = ff.bbox(ff.property(""), feature.getBounds());

        features = featureStore.getFeatures(bbox).features();

        try {
            while (features.hasNext()) {
                SimpleFeature newFeature = features.next();
                assertEquals(newFeature, fids.get(newFeature.getID()));
            }
        } finally {
            if (features != null)
                features.close();
        }
    }
*/
    private void assertFidsMatch() throws IOException {
        // long start = System.currentTimeMillis();
        DefaultQuery query = new DefaultQuery(featureStore.getSchema()
                .getTypeName());

        int i = 0;

        for (Iterator iter = fids.entrySet().iterator(); iter.hasNext();) {
            i++;
            Map.Entry entry = (Map.Entry) iter.next();
            String fid = (String) entry.getKey();
            FeatureId id = fac.featureId(fid);
            Filter filter = fac.id(Collections.singleton(id));
            query.setFilter(filter);
            SimpleFeatureIterator features = featureStore.getFeatures(query)
                    .features();
            try {
                SimpleFeature feature = features.next();
                assertFalse(features.hasNext());
                assertEquals(i + "th feature", entry.getValue(), feature);
            } finally {
                if (features != null)
                    features.close();
            }

        }
    }

}
