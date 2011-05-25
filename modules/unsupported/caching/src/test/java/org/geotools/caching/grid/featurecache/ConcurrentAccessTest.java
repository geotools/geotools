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
package org.geotools.caching.grid.featurecache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

import org.geotools.caching.grid.spatialindex.store.BufferedDiskStorage;
import org.geotools.caching.grid.spatialindex.store.DiskStorage;
import org.geotools.caching.util.CacheUtil;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.spatial.BBOXImpl;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Envelope;


/** Multithreaded test for concurrent access to GridFeatureCache.
 * If variable <code>hardest</code> is set to true, test will use different settings
 * and run more threads at the same time, to increase concurrency, so the test
 * is harder to pass.
 * 
 * @author Christophe Rousson <christophe.rousson@gmail.com>, Google SoC 2007 
 *
 *
 *
 * @source $URL$
 */
public class ConcurrentAccessTest extends TestCase {
    boolean hardest = false;
    GridFeatureCache grid;
    FeatureCollection dataset;
    List<Filter> filterset1;
    List<Filter> filterset2;
    double[] windows = new double[] { 0.2, 0.1, 0.05 };

    @Override
    protected void setUp() {
        try {
            MemoryDataStore ds = new MemoryDataStore();
            int numdata;

            if (hardest) {
                numdata = 1000;
            } else {
                numdata = 100;
            }

            dataset = DataUtilities.createUnitsquareDataSet(numdata, 1025);
            ds.createSchema((SimpleFeatureType)dataset.getSchema());
            ds.addFeatures(dataset);

            if (hardest) {
            	Properties pset = new Properties();
                pset.setProperty(BufferedDiskStorage.BUFFER_SIZE_PROPERTY, "10");
                pset.setProperty(DiskStorage.DATA_FILE_PROPERTY, "cache.tmp");
                pset.setProperty(DiskStorage.INDEX_FILE_PROPERTY, "cache.idx");
                pset.setProperty(DiskStorage.PAGE_SIZE_PROPERTY, "1000");
                grid = new GridFeatureCache(ds.getFeatureSource(ds.getTypeNames()[0]), 100, 500,
                        DiskStorage.createInstance(pset));
            } else {
                grid = new GridFeatureCache(ds.getFeatureSource(ds.getTypeNames()[0]), 100, 500,
                        BufferedDiskStorage.createInstance());
            }

            File filtersrc = new File("filters.data");

            if (filtersrc.exists()) {
                loadFilters(filtersrc);
            } else {
                createAndSaveFilters(filtersrc);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void createAndSaveFilters(File f) throws Exception {
        filterset1 = DataUtilities.createUnitsquareFilterSet(50, windows);
        filterset2 = DataUtilities.createUnitsquareFilterSet(50, windows);

        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        dumpFilterSet(oos, filterset1);
        dumpFilterSet(oos, filterset2);
        oos.close();
        fos.close();
    }

    void loadFilters(File f) throws Exception {
        System.out.println("Loading existing filters.");

        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fis);
        int n = ois.readInt();
        filterset1 = new ArrayList<Filter>();

        for (int i = 0; i < n; i++) {
            Envelope e = (Envelope) ois.readObject();
            filterset1.add(DataUtilities.convert(e));
        }

        n = ois.readInt();
        filterset2 = new ArrayList<Filter>();

        for (int i = 0; i < n; i++) {
            Envelope e = (Envelope) ois.readObject();
            filterset2.add(DataUtilities.convert(e));
        }

        ois.close();
        fis.close();
    }

    void dumpFilterSet(ObjectOutputStream oos, List<Filter> filterset)
        throws Exception {
        oos.writeInt(filterset.size());

        for (Iterator<Filter> it = filterset.iterator(); it.hasNext();) {
            Envelope e = CacheUtil.extractEnvelope((BBOXImpl) it.next());
            oos.writeObject(e);
        }
    }

    protected static Test suite() {
        return new TestSuite(ConcurrentAccessTest.class);
    }

    public void testConcurrentAccess() throws Throwable {
        TestRunnable client1 = new CacheClient(filterset1);
        TestRunnable client2 = new CacheClient(filterset2);
        TestRunnable client3 = new CacheClient(filterset1);
        TestRunnable[] trs = new TestRunnable[] { client1, client2, client3 };
        MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);
        mttr.runTestRunnables();
    }

    public void testConcurrentAccessAndClear() throws Throwable {
        int nthreads;

        if (hardest) {
            nthreads = 8;
        } else {
            nthreads = 5;
        }

        TestRunnable[] trs = new TestRunnable[nthreads];
        trs[0] = new CacheClient(filterset1);

        trs[1] = new CacheClient(filterset2);
        trs[2] = new CacheClient(filterset1);
        trs[3] = new LazyCacheCleaner(50, 5);
        trs[4] = new LazyCacheCleaner(100, 2);
        if (hardest) {
        	trs[5] = new CacheClient(filterset2);
        	trs[6] = new CacheClient(filterset1);
        	trs[7] = new CacheClient(filterset2);
        }
        MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);
        mttr.runTestRunnables();
    }

    private class CacheClient extends TestRunnable {
        List<Filter> filterset;

        CacheClient(List<Filter> filterset) {
            this.filterset = filterset;
        }

        @Override
        public void runTest() throws Throwable {
            int count = 0;

            for (Iterator<Filter> it = filterset.iterator(); it.hasNext();) {
                Filter next = it.next();
                count++;

                //				System.out.println(Thread.currentThread().getName() + " : filter " + count);
                int control = dataset.subCollection(next).size();
                FeatureCollection fc = grid.getFeatures(next);

                if (control != fc.size()) {
                    System.out.println(Thread.currentThread().isInterrupted());
                }

                //System.out.println("Expected : " + control + ", got :" + fc.size());
                assertEquals(control, fc.size());
            }
        }
    }

    private class LazyCacheCleaner extends TestRunnable {
        Random rand = new Random();
        int maxdelay = 100;
        int repeat = 1;

        LazyCacheCleaner() {
        }

        LazyCacheCleaner(int maxdelay, int repeat) {
            this.maxdelay = maxdelay;
            this.repeat = repeat;
        }

        @Override
        public void runTest() throws Throwable {
            int count = 0;

            while (count < repeat) {
                Thread.sleep(rand.nextInt(maxdelay));

                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }

                //                System.out.println(Thread.currentThread().getName() + " : cleared cache");
                grid.clear();
                count++;
            }
        }
    }
}
