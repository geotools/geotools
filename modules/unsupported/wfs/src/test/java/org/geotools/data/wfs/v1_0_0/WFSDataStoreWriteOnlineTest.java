/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.v1_0_0;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.geotools.data.DataStore;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.filter.IllegalFilterException;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.identity.FeatureId;

/**
 * <p> 
 * Needs two featureTypes on the server, with the same data types ... will add the second set to the first set. 
 * </p>
 * @author dzwiers
 *
 * @source $URL$
 */
public class WFSDataStoreWriteOnlineTest extends TestCase {
    public void testEmpty() throws NoSuchElementException, IOException, IllegalAttributeException, FactoryRegistryException{
//        URL u = new URL("http://localhost:8080/geoserver/wfs");
//        WFSDataStore ds = getDataStore(u);
//        FeatureType ft = ds.getSchema("states");
//        Feature f = ds.getFeatureReader("states").next();
//        doDelete(ds,ft,FilterFactoryFinder.createFilterFactory().createFidFilter(f.getID()));
//        SimpleFeatureCollection fc = DefaultFeatureCollections.newCollection();
//        fc.add(f);
//        doInsert(ds,ft,(new CollectionDataStore(fc)).getFeatureReader("states"));
    }

    
    public WFSDataStoreWriteOnlineTest(){
        Logger.global.setLevel(Level.SEVERE);
    }
    
    public static Id doInsert(DataStore ds,SimpleFeatureType ft,SimpleFeatureCollection insert) throws NoSuchElementException, IOException, IllegalAttributeException{
    	Transaction t = new DefaultTransaction();
    	WFSFeatureStore fs = (WFSFeatureStore)ds.getFeatureSource(ft.getTypeName());
    	fs.setTransaction(t);
    	System.out.println("Insert Read 1");
    	SimpleFeatureIterator fr = fs.getFeatures().features();
    	int count1 = 0;
    	while(fr.hasNext()){
    		count1 ++; fr.next();
    	}
        fr.close();
    	System.out.println("Insert Add Features");
    	List<FeatureId> fids1 = fs.addFeatures(insert);

    	System.out.println("Insert Read 2");
    	fr = fs.getFeatures().features();
    	int count2 = 0;
    	while(fr.hasNext()){
    		count2 ++; fr.next();
    	}
        fr.close();
    	assertEquals(count1+insert.size(), count2);

        FilterFactory fac=CommonFactoryFinder.getFilterFactory(null);
        Set<FeatureId> featureIds = new HashSet<FeatureId>();
        for(FeatureId id : fids1){
            featureIds.add(id);
        }
        Id fidfilter = fac.id(featureIds);
        
        System.out.println("Remove Inserted Features");
        fs.removeFeatures(fidfilter);
        
        System.out.println("Insert Read 3");
        fr = fs.getFeatures().features();
        count2 = 0;
        while(fr.hasNext()){
            count2 ++; fr.next();
        }
        fr.close();
        assertEquals(count1, count2);
        
        System.out.println("Insert Add Features");
        fs.addFeatures(insert);

        System.out.println("Insert Read 2");
        fr = fs.getFeatures().features();
        count2 = 0;
        while(fr.hasNext()){
            count2 ++; fr.next();
        }
        fr.close();
        assertEquals(count1+insert.size(), count2);

        
    	System.out.println("Insert Commit");
    	t.commit();

    	System.out.println("Insert Read 3");
    	fr = fs.getFeatures().features();
    	int count3 = 0;
    	while(fr.hasNext()){
    		count3 ++; fr.next();
    	}
        fr.close();
    	assertEquals(count2,count3);
    	
    	WFSTransactionState ts = (WFSTransactionState)t.getState(ds);
    	String[] fids = ts.getFids(ft.getTypeName());
    	assertNotNull(fids);
    	
        Set ids = new HashSet();
        for(int i=0;i<fids.length;i++){
    		ids.add(fac.featureId(fids[i]));
        }
        Id ff = fac.id(ids);
    	return ff;
    }
    
    public static void doDelete(DataStore ds,SimpleFeatureType ft, Id ff) throws NoSuchElementException, IllegalAttributeException, IOException{
    	assertNotNull("doInsertFailed?",ff);
    	Transaction t = new DefaultTransaction();
    	SimpleFeatureStore fs = (SimpleFeatureStore)ds.getFeatureSource(ft.getTypeName());
    	fs.setTransaction(t);
    	
    	System.out.println("Delete Read 1");
    	SimpleFeatureIterator fr = fs.getFeatures().features();
    	int count1 = 0;
    	while(fr.hasNext()){
    		count1 ++; fr.next();
    	}
        fr.close();

    	System.out.println("Delete Remove "+ff);
    	fs.removeFeatures(ff);

    	System.out.println("Delete Read 2");
    	fr = fs.getFeatures().features();
    	int count2 = 0;
    	while(fr.hasNext()){
    		count2 ++;
    		if(count2<5)
    			System.out.println("# == "+count2+" "+fr.next().getID());
    		else
    			fr.next();
    	}
        fr.close();
    	assertTrue("Read 1 == "+count1+" Read 2 == "+count2,count2<count1);

    	System.out.println("Delete Commit");
    	t.commit();

    	System.out.println("Delete Read 3");
    	fr = fs.getFeatures().features();
    	int count3 = 0;
    	while(fr.hasNext()){
    		count3 ++; fr.next();
    	}
        fr.close();
    	assertTrue(count2==count3);
    }
    
    public static void doUpdate(DataStore ds,SimpleFeatureType ft, String attributeToChange, Object newValue ) throws IllegalFilterException, FactoryRegistryException, NoSuchElementException, IOException, IllegalAttributeException{
    	Transaction t = new DefaultTransaction();
    	SimpleFeatureStore fs = (SimpleFeatureStore)ds.getFeatureSource(ft.getTypeName());
    	fs.setTransaction(t);
    	
    	AttributeDescriptor at = ft.getDescriptor(attributeToChange);
    	assertNotNull("Attribute "+attributeToChange+" does not exist",at);
    	
    	FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);
        Filter f = filterFactory.equals(filterFactory.property(at.getLocalName()), filterFactory
                .literal(newValue));

    	System.out.println("Update Read 1");
    	SimpleFeatureIterator fr = fs.getFeatures(f).features();
    	
    	int count1 = 0;
    	Object oldValue=null;
        if(fr!=null)
    	while(fr.hasNext()){
    		count1 ++; oldValue=fr.next().getAttribute(attributeToChange);
    	}

        fr.close();
    	System.out.println("Update Modify");
    	fs.modifyFeatures(at,newValue,Filter.INCLUDE);

    	System.out.println("Update Read 2");
    	fr = fs.getFeatures(f).features();
    	int count2 = 0;
    	while(fr.hasNext()){
    		count2 ++;
//    		System.out.println(fr.next());
    		fr.next();
    	}
        fr.close();
//System.out.println("Read 1 == "+count1+" Read 2 == "+count2);
    	assertTrue("Read 1 == "+count1+" Read 2 == "+count2,count2>count1);

    	System.out.println("Update Commit");
        try {
            t.commit();

            assertTrue(((WFSTransactionState) t.getState(ds)).getFids(ft.getTypeName()) != null);

            System.out.println("Update Read 3");
            fr = fs.getFeatures(f).features();
            int count3 = 0;
            while( fr.hasNext() ) {
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
