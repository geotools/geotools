/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.caching.featurecache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.geotools.caching.CacheOversizedException;
import org.geotools.caching.util.CacheUtil;
import org.geotools.caching.util.Generator;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.spatial.BBOXImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;


public abstract class AbstractFeatureCacheTest extends TestCase {
    protected static SimpleFeatureCollection dataset;
    protected static int numdata = 100;
    protected static List<Filter> filterset;
    public final static Filter unitsquarefilter;
    public final static Envelope unitsquare;
    protected static int numfilters = 100;

    static {
        Generator gen = new Generator(1, 1); // seed 1029 for a set registering outside of unitsquare
        dataset = new DefaultFeatureCollection("Test", Generator.type);

        for (int i = 0; i < numdata; i++) {
            SimpleFeature f = gen.createFeature(i);
            dataset.add(f);
        }
        
        filterset = new ArrayList<Filter>(numfilters);

        for (int i = 0; i < numfilters; i++) {
            Coordinate point = Generator.pickRandomPoint(new Coordinate(0.5, 0.5), 0.5, 0.5);
            Filter f = Generator.createBboxFilter(point, 0.2, 0.2);
            filterset.add(f);
        }

        unitsquarefilter = Generator.createBboxFilter(new Coordinate(0.5, 0.5), 1, 1);
        unitsquare = CacheUtil.extractEnvelope((BBOXImpl) unitsquarefilter);
    }

    protected MemoryDataStore ds;
    protected AbstractFeatureCache cache;
    
    protected void setUp() {
        try {
            ds = new MemoryDataStore();
            FeatureType ft = dataset.getSchema();
            SimpleFeatureType sft = (SimpleFeatureType)ft;
            ds.createSchema(sft);
            ds.addFeatures(dataset);
            cache = createInstance(1000);
        } catch (FeatureCacheException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract AbstractFeatureCache createInstance(int capacity)
        throws FeatureCacheException, IOException;

    public void testExtractEnvelope() {
        BBOXImpl filter = (BBOXImpl) Generator.createBboxFilter(new Coordinate(0.1, 0.9), 0.2, 0.3);
        Envelope e = CacheUtil.extractEnvelope(filter);
        Envelope c = new Envelope(0, 0.2, 0.75, 1.05);
        assertEquals(c, e);
    }

    public void testConvert() {
    }

    

    public abstract void testPut() throws CacheOversizedException;

    public void testPeek() throws CacheOversizedException {
        cache.put(dataset);

        FeatureCollection fc = cache.peek(unitsquare);

        //        assertEquals(0, fc.size());
        List<Envelope> matches = cache.match(unitsquare);

        for (Iterator<Envelope> it = matches.iterator(); it.hasNext();) {
            cache.register(it.next());
        }

        cache.put(dataset);
        fc = cache.peek(unitsquare);
        assertEquals(dataset.size(), fc.size());
    }

    public void testGet() throws IOException {
        FeatureCollection fc = cache.get(unitsquare);
        int size = fc.size();
        assertEquals(dataset.size(), size);
       
        List<Envelope> matches = cache.match(unitsquare);
        assertTrue(matches.isEmpty());
    }

    public void testOversized() {
        FeatureCollection fc = null;
        boolean pass = false;

        try {
            cache = createInstance(10);
            fc = cache.get(unitsquare); // should not cause CacheOversizedException
            assertEquals(dataset.size(), fc.size());

            List<Envelope> matches = cache.match(unitsquare);
            assertFalse(matches.isEmpty()); // but cache should not have registered oversized query
            pass = true;
            cache.put(dataset); // must cause CacheOversizedException
        } catch (CacheOversizedException e) {
            //e.printStackTrace();
            assertTrue("CacheOversizedException raised where unexpected", pass);

            return;
        } catch (FeatureCacheException e) {
            fail(e.toString());
        } catch (IOException e) {
            fail(e.toString());
        }

        fail("did not get expected CacheOversizedException");
    }

    public void testGetFeatures() throws IOException {
        FeatureCollection fc = cache.getFeatures(unitsquarefilter);
        assertEquals(dataset.size(), fc.size());

        List<Envelope> matches = cache.match(unitsquare);
        assertTrue(matches.isEmpty());
    }

    public void testClear() throws IOException {
        Envelope e = CacheUtil.extractEnvelope((BBOXImpl) filterset.get(0));
        
        
        int size = cache.get(e).size();
        int presize = cache.match(e).size();
        
        cache.clear();

        assertEquals(size, cache.get(e).size());
        assertEquals(presize, cache.match(e).size());
    }

    public void testGetBounds() throws IOException {
        ReferencedEnvelope env = cache.getBounds();
        
        //dataset.getBounds() returns an envelope with a null crs; wee need
        //to add the crs to ensure assertequals returns true
        ReferencedEnvelope bnds = new ReferencedEnvelope(dataset.getBounds(), dataset.getSchema().getCoordinateReferenceSystem());
        
        //assertEquals(dataset.getBounds(), env);
        assertEquals(bnds, env);
    }

    public abstract void testEviction() throws IOException, FeatureCacheException;

//    class PrintingVisitor implements Visitor {
//        public boolean isDataVisitor() {
//            return true;
//        }
//
//        public void visitData(Data d) {
//            System.out.println("    " + d.getData());
//        }
//
//        public void visitNode(Node n) {
//            System.out.println(n + " is " + n.getIdentifier().isValid() + " and has "
//                + n.getDataCount() + " data :");
//        }
//    }
//
//    public class CountingVisitor implements Visitor {
//        int data = 0;
//        int nodes = 0;
//
//        public boolean isDataVisitor() {
//            return true;
//        }
//
//        public void visitData(Data d) {
//            data++;
//        }
//
//        public void visitNode(Node n) {
//            nodes++;
//        }
//    }
}
