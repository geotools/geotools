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
package org.geotools.caching.firstdraft;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.caching.util.Generator;
import org.geotools.caching.util.SimpleFeatureMarshaller;
import org.opengis.feature.Feature;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;


public class MarshallerXest extends TestCase {
    public static Test suite() {
        return new TestSuite(MarshallerXest.class);
    }

    /** Marshall and unmarshall a DefaultFeature, and test for equality with the result.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws IllegalAttributeException
     */
    public void testMarshall()
        throws IOException, ClassNotFoundException, IllegalAttributeException {
        Generator gen = new Generator(1000, 1000);
        SimpleFeature f = gen.createFeature(0);
        SimpleFeatureMarshaller m = new SimpleFeatureMarshaller();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        m.marshall(f, oos);

        byte[] ba = baos.toByteArray();
        baos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Feature newf = m.unmarshall(ois);
        bais.close();
        assertTrue(f.equals(newf));
    }

    /** Same as testMarshall, but uses complex representation of DefaultFeature.
     * Marshall and unmarshall, and test for equality with the result.
     *
     * @task seems to be a bug in DefaultFeature.equals() or JTS.Geometry.equals()
     *       test is disabled.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws IllegalAttributeException
     */
    public void ztestComplexMarshall()
        throws IOException, ClassNotFoundException, IllegalAttributeException {
        Generator gen = new Generator(1000, 1000);
        SimpleFeature f = gen.createFeature(0);
        SimpleFeatureMarshaller m = new SimpleFeatureMarshaller();
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        m.marshall(f, oos);

        byte[] ba = baos.toByteArray();
        baos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Feature newf = m.unmarshall(ois);
        bais.close();
        //newf = ((DefaultFeature) newf).toComplex() ;
        assertTrue(f.equals(newf));
    }

    /** Disabled test to mesure time to marshall/unmarshall features.
     *  Test results on my PC :
     *   <ul><li>0.8 ms per feature for a marshall/unmarshall cycle
     *       <li>0.2 ms per feature for marshalling only
     *   </ul>
     */
    public void ztestMarshallTime() {
        Generator gen = new Generator(1000, 1000);
        List features = new ArrayList();

        for (int i = 0; i < 10000; i++) {
            SimpleFeature f = gen.createFeature(i);
            features.add(f);
        }

        long start = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            SimpleFeature f = (SimpleFeature) features.get(i);
            SimpleFeatureMarshaller marsh = new SimpleFeatureMarshaller();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            try {
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                marsh.marshall(f, oos);

                byte[] ba = baos.toByteArray();
                ByteArrayInputStream bais = new ByteArrayInputStream(ba);
                ObjectInputStream ois = new ObjectInputStream(bais);
                Feature newf = marsh.unmarshall(ois);

                if (!newf.equals(f)) {
                    throw new RuntimeException("Error at unmarshall");
                }

                if (i == (1000 * (i / 1000))) {
                    System.out.println(i);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                //	TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAttributeException e) {
                //	 TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        long stop = System.currentTimeMillis();
        System.out.println("Elapsed time for 10000 features : " + (stop - start) + " ms.");
    }
}
