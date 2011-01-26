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
package org.geotools.data.shapefile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import junit.framework.AssertionFailedError;

import org.geotools.TestData;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/shapefile/src/test/java/org/geotools/data/shapefile/ShapefileReadWriteTest.java $
 * @version $Id$
 * @author Ian Schneider
 */
public class ShapefileReadWriteTest extends TestCaseSupport {
    final String[] files = { "shapes/statepop.shp", "shapes/polygontest.shp",
            "shapes/pointtest.shp", "shapes/holeTouchEdge.shp",
            "shapes/stream.shp", "shapes/chinese_poly.shp" };

    /** Creates a new instance of ShapefileReadWriteTest */
    public ShapefileReadWriteTest(String name) throws IOException {
        super(name);
    }
    
    public void testReadWriteStatePop() throws Exception {
        test("shapes/statepop.shp");
    }
    
    public void testReadWritePolygonTest() throws Exception {
        test("shapes/polygontest.shp");
    }
    
    public void testReadWritePointTest() throws Exception {
        test("shapes/pointtest.shp");
    }
    
    public void testReadWriteHoleTouchEdge() throws Exception {
        test("shapes/holeTouchEdge.shp");
    }
    
    public void testReadWriteChinese() throws Exception {
        test("shapes/chinese_poly.shp", Charset.forName("GB18030"));
    }
    
    public void testReadDanishPoint() throws Exception {
        test("shapes/danish_point.shp");
    }
    
    

//    public void testAll() {
//        StringBuffer errors = new StringBuffer();
//        Exception bad = null;
//        for (int i = 0, ii = files.length; i < ii; i++) {
//            try {
//                
//            } catch (Exception e) {
//                System.out.println("File failed:" + files[i] + " " + e);
//                e.printStackTrace();
//                errors.append("\nFile " + files[i] + " : " + e.getMessage());
//                bad = e;
//            }
//        }
//        if (errors.length() > 0) {
//            fail(errors.toString(), bad);
//        }
//    }

    boolean readStarted = false;

    Exception exception = null;

    public void testConcurrentReadWrite() throws Exception {
        System.gc();
        System.runFinalization(); // If some streams are still open, it may
        // help to close them.
        final File file = getTempFile();
        Runnable reader = new Runnable() {
            public void run() {
                int cutoff = 0;
                FileInputStream fr = null;
                try {
                    fr = new FileInputStream(file);
                    try {
                        fr.read();
                    } catch (IOException e1) {
                        exception = e1;
                        return;
                    }
                    // if (verbose) {
                    // System.out.println("locked");
                    // }
                    readStarted = true;
                    while (cutoff < 10) {
                        synchronized (this) {
                            try {
                                try {
                                    fr.read();
                                } catch (IOException e) {
                                    exception = e;
                                    return;
                                }
                                wait(500);
                                cutoff++;
                            } catch (InterruptedException e) {
                                cutoff = 10;
                            }
                        }
                    }
                } catch (FileNotFoundException e) {
                    assertTrue(false);
                } finally {
                    if (fr != null) {
                        try {
                            fr.close();
                        } catch (IOException e) {
                            exception = e;
                            return;
                        }
                    }
                }
            }
        };
        Thread readThread = new Thread(reader);
        readThread.start();
        while (!readStarted) {
            if (exception != null) {
                throw exception;
            }
            Thread.sleep(100);
        }
        test(files[0]);
    }

    private static void fail(String message, Throwable cause) {
        AssertionFailedError fail = new AssertionFailedError(message);
        fail.initCause(cause);
        throw fail;
    }
    
    private void test(String f) throws Exception {
        test(f, null);
    }

    private void test(String f, Charset charset) throws Exception {
        copyShapefiles(f); // Work on File rather than URL from JAR.
        ShapefileDataStore s = null;
        if(charset == null) {
            s = new ShapefileDataStore(TestData.url(TestCaseSupport.class, f));
        } else {
            s = new ShapefileDataStore(TestData.url(TestCaseSupport.class, f), false, charset);
        }
        String typeName = s.getTypeNames()[0];
        FeatureSource<SimpleFeatureType, SimpleFeature> source = s.getFeatureSource(typeName);
        SimpleFeatureType type = source.getSchema();
        FeatureCollection<SimpleFeatureType, SimpleFeature> one = source.getFeatures();
        File tmp = getTempFile();

        ShapefileDataStoreFactory maker = new ShapefileDataStoreFactory();
        test(type, one, tmp, maker, true, charset);

        File tmp2 = getTempFile(); // TODO consider reuse tmp results in
        // failure
        test(type, one, tmp2, maker, false, charset);
    }

    private void test(SimpleFeatureType type, FeatureCollection<SimpleFeatureType, SimpleFeature> original,
            File tmp, ShapefileDataStoreFactory maker, boolean memorymapped, Charset charset)
            throws IOException, MalformedURLException, Exception {

        ShapefileDataStore shapefile;
        String typeName;
        Map<String,Serializable> params = new HashMap<String,Serializable>();
        params.put(ShapefileDataStoreFactory.URLP.key, tmp.toURI().toURL());
        params.put(ShapefileDataStoreFactory.MEMORY_MAPPED.key, memorymapped);
        shapefile = (ShapefileDataStore) maker.createDataStore(params);
        if(charset != null)
            shapefile.setStringCharset(charset);

        shapefile.createSchema(type);

        FeatureStore<SimpleFeatureType, SimpleFeature> store = (FeatureStore<SimpleFeatureType, SimpleFeature>) shapefile.getFeatureSource(type
                .getTypeName());

        store.addFeatures(original);

        FeatureCollection<SimpleFeatureType, SimpleFeature> copy = store.getFeatures();
        compare(original, copy);

        if (true) {
            // review open
            ShapefileDataStore review;
            if(charset == null) 
                review = new ShapefileDataStore(tmp.toURI().toURL(), tmp.toURI(), memorymapped);
            else
                review = new ShapefileDataStore(tmp.toURI().toURL(), tmp.toURI(), memorymapped, charset);
            typeName = review.getTypeNames()[0];
            FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = review.getFeatureSource(typeName);
            FeatureCollection<SimpleFeatureType, SimpleFeature> again = featureSource.getFeatures();

            compare(copy, again);
            compare(original, again);
        }
    }

    static void compare(FeatureCollection<SimpleFeatureType, SimpleFeature> one, FeatureCollection<SimpleFeatureType, SimpleFeature> two)
            throws Exception {

        if (one.size() != two.size()) {
            throw new Exception("Number of Features unequal : " + one.size()
                    + " != " + two.size());
        }

        FeatureIterator<SimpleFeature> iterator1 = one.features();
        FeatureIterator<SimpleFeature> iterator2 = two.features();

        while (iterator1.hasNext()) {
            SimpleFeature f1 = iterator1.next();
            SimpleFeature f2 = iterator2.next();
            compare(f1, f2);
        }
        iterator1.close();
        iterator2.close();
    }

    static void compare(SimpleFeature f1, SimpleFeature f2) throws Exception {

        if (f1.getAttributeCount() != f2.getAttributeCount()) {
            throw new Exception("Unequal number of attributes");
        }

        for (int i = 0; i < f1.getAttributeCount(); i++) {
            Object att1 = f1.getAttribute(i);
            Object att2 = f2.getAttribute(i);
            if (att1 instanceof Geometry && att2 instanceof Geometry) {
                Geometry g1 = ((Geometry) att1);
                Geometry g2 = ((Geometry) att2);
                g1.normalize();
                g2.normalize();
                if (!g1.equalsExact(g2)) {
                    throw new Exception("Different geometries (" + i + "):\n"
                            + g1 + "\n" + g2);
                }
            } else {
                if (!att1.equals(att2)) {
                    throw new Exception("Different attribute (" + i + "): ["
                            + att1 + "] - [" + att2 + "]");
                }
            }
        }

    }

    public static final void main(String[] args) throws Exception {
        // verbose = true;
        junit.textui.TestRunner.run(suite(ShapefileReadWriteTest.class));
    }
}
