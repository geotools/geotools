/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.v1_0_0.demo;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.geotools.data.DataStore;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * <p> 
 * An example use of the WFS data store
 * </p>
 * @author dzwiers Refractions Research
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/wfs-ng/src/test/java/org/geotools/data/wfs/v1_0_0/demo/WFSDemo.java $
 */
public class WFSDemo {
    public static void main(String[] args){
        System.out.println("WFS Demo");
        try{
//            URL url = new URL("http://www2.dmsolutions.ca/cgi-bin/mswfs_gmap?version=1.0.0&request=getcapabilities&service=wfs");
        	URL url = new URL("http://www.refractions.net:8080/geoserver/wfs?REQUEST=GetCapabilities");
        
            Map m = new HashMap();
            m.put(WFSDataStoreFactory.URL.key,url);
            m.put(WFSDataStoreFactory.TIMEOUT.key,new Integer(10000));
            m.put(WFSDataStoreFactory.PROTOCOL.key,Boolean.FALSE);

            DataStore wfs = (new WFSDataStoreFactory()).createNewDataStore(m);
            Query query = new DefaultQuery(wfs.getTypeNames()[1]);
             FeatureReader<SimpleFeatureType, SimpleFeature> ft = wfs.getFeatureReader(query,Transaction.AUTO_COMMIT);
            int count = 0;
            while(ft.hasNext())
                if(ft.next()!=null)
                    count++;
            System.out.println("Found "+count+" features");
        }catch(IOException e){
            e.printStackTrace();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        } catch (IllegalAttributeException e) {
            e.printStackTrace();
        }
    }
}
