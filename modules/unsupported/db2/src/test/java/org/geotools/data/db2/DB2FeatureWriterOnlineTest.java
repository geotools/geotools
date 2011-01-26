/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    (C) Copyright IBM Corporation, 2005. All rights reserved.
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
 *
 */
package org.geotools.data.db2;

import java.io.IOException;

import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;


/**
 * Exercise DB2FeatureWriter.
 *
 * @author David Adler - IBM Corporation
 * @source $URL$
 */
public class DB2FeatureWriterOnlineTest extends AbstractDB2OnlineTestCase {
    private DB2DataStore dataStore = null;

    /**
     * Get a DB2DataStore that we will use for all the tests.
     *
     * @throws Exception
     */
    public void connect() throws Exception {
        super.connect();
        dataStore = getDataStore();
    }

    public void testRemove() throws IOException {
        try {
            Transaction trans = null;
            trans = new DefaultTransaction("trans1");

            //			fs.setTransaction(trans);
            FeatureWriter<SimpleFeatureType, SimpleFeature> fw = this.dataStore.getFeatureWriter("Roads", trans);

            if (fw.hasNext()) {
                SimpleFeature f = fw.next();
                System.out.println(f);
                fw.remove();
            }
            fw.close();
            trans.commit();
            trans.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void testUpdateRoads() throws IOException {
        try {
            DB2FeatureStore fs = (DB2FeatureStore) dataStore.getFeatureSource(
                    "Roads");
            Transaction trans = null;
            trans = new DefaultTransaction("trans1");

            //			fs.setTransaction(trans);
            FeatureWriter<SimpleFeatureType, SimpleFeature> fw = this.dataStore.getFeatureWriter("Roads", trans);

            if (fw.hasNext()) {
                SimpleFeature f = fw.next();
                System.out.println(f);
                Object a0 = f.getAttribute(0);
                String name = (String) f.getAttribute(1);
                f.setAttribute(1, name.trim() + "1");
                Object a2 = f.getAttribute(2);
                f.setAttribute(2,Double.valueOf("1.5"));

                Geometry a3 = (Geometry) f.getAttribute(3);
        		WKTReader wktReader = new WKTReader();
        		LineString line3 =
        			(LineString) wktReader.read("LINESTRING (599000.0 1162200.0, 599226.0 1162227.0)");
        		f.setAttribute(3,line3);
                System.out.println(f);
                fw.write();
            }
            fw.close();
            trans.commit();
            trans.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void testUpdatePlaces() throws IOException {
        try {
            DB2FeatureStore fs = (DB2FeatureStore) dataStore.getFeatureSource(
                    "Places");
            Transaction trans = null;
            trans = new DefaultTransaction("trans1");
            FeatureWriter<SimpleFeatureType, SimpleFeature> fw = this.dataStore.getFeatureWriter("Places", trans);

            if (fw.hasNext()) {
                SimpleFeature f = fw.next();
                System.out.println(f);

                String name = (String) f.getAttribute(0);
                f.setAttribute(0, name.trim() + "1");

                Geometry a1 = (Geometry) f.getAttribute(1);
        		WKTReader wktReader = new WKTReader();
        		Polygon polygon =
        			(Polygon) wktReader.read("POLYGON ((-74.099595 42.019401, -74.100484 42.01992, -74.101161 42.020315, -74.099595 42.019401))");
        		f.setAttribute(1,polygon);
                System.out.println(f);
                fw.write();
            }
            fw.close();
            trans.commit();
            trans.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void testAppend() throws IOException {
        try {

            FeatureWriter<SimpleFeatureType, SimpleFeature> fw = this.dataStore.getFeatureWriterAppend("Roads",
                    Transaction.AUTO_COMMIT);
            boolean hasNext = fw.hasNext();
            SimpleFeature f = fw.next();
            f.setAttribute(0,"100");
            f.setAttribute(1, "name" + "1");
            Object a2 = f.getAttribute(2);
            f.setAttribute(2,Double.valueOf("1.5"));

            Geometry a3 = (Geometry) f.getAttribute(3);
    		WKTReader wktReader = new WKTReader();
    		LineString line3 =
    			(LineString) wktReader.read("LINESTRING (599000.0 1162200.0, 599226.0 1162227.0)");
    		f.setAttribute(3,line3); 
    		f.toString();
            System.out.println(f);
            fw.write();   
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void testAppendPlaces() throws IOException {
        try {

            FeatureWriter<SimpleFeatureType, SimpleFeature> fw = this.dataStore.getFeatureWriterAppend("Places",
                    Transaction.AUTO_COMMIT);
            boolean hasNext = fw.hasNext();
            SimpleFeature f = fw.next();
            System.out.println(f);
            f.setAttribute(0, "name" + "1");
            Geometry a1 = (Geometry) f.getAttribute(1);
    		WKTReader wktReader = new WKTReader();
    		Polygon polygon =
    			(Polygon) wktReader.read("POLYGON ((-74.099595 42.019401, -74.100484 42.01992, -74.101161 42.020315, -74.099595 42.019401))");
    		f.setAttribute(1,polygon); 
    		f.toString();
            System.out.println(f);
            fw.write();      
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
