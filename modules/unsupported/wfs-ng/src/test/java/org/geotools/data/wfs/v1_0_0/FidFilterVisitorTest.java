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
package org.geotools.data.wfs.v1_0_0;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.IllegalFilterException;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;
import org.opengis.filter.Or;
import org.opengis.filter.identity.FeatureId;

/**
 * FidFilterVisitor is used to fix up feature Ids that have
 * been modifed (or actually assigned) after a transaction response.
 * 
 * @author Jesse
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/wfs-ng/src/test/java/org/geotools/data/wfs/v1_0_0/FidFilterVisitorTest.java $
 */
public class FidFilterVisitorTest extends TestCase {

	private FidFilterVisitor visitor;
	private FilterFactory2 ff=CommonFactoryFinder.getFilterFactory2(null);
	private Map<String,String> state;
	
	protected void setUp() throws Exception {
		state=new HashMap<String,String>();
		state.put("new1","final1");
		state.put("new2","final2");
		state.put("new3","final3");
		
		visitor=new FidFilterVisitor(state); 
	}

   /**
    * This is really the only valid thing we have to take care of.
    */
    public void testVisitIdFilter() throws Exception {
        Set<FeatureId> fidSet = new HashSet<FeatureId>();
        fidSet.add( ff.featureId("new1"));
        fidSet.add( ff.featureId("new2"));
        Id before = ff.id( fidSet );
        
        Id after = (Id) before.accept( visitor, null );
        assertSame( after, after );
        assertTrue( after.getIDs().contains("final1") );
        assertTrue( after.getIDs().contains("final2") );        
    }
    /** Check to make sure others ids are not harmed */
    public void testVisitIdFilter2() throws Exception {
        Set<FeatureId> fidSet = new HashSet<FeatureId>();
        fidSet.add( ff.featureId("new1"));
        fidSet.add( ff.featureId("other"));
        Id before = ff.id( fidSet );
        
        Id after = (Id) before.accept( visitor, null );
        assertSame( after, after );
        assertTrue( after.getIDs().contains("final1") );
        assertTrue( after.getIDs().contains("other") );        
    }
    /** Check to make sure others ids are not harmed */
    public void testVisitIdFilter3() throws Exception {
        Set<FeatureId> fidSet = new HashSet<FeatureId>();
        fidSet.add( ff.featureId("new1"));
        fidSet.add( ff.featureId("new2"));
        fidSet.add( ff.featureId("new3"));        
        fidSet.add( ff.featureId("other"));
        Id before = ff.id( fidSet );
        
        Id after = (Id) before.accept( visitor, null );
        assertSame( after, after );
        assertTrue( after.getIDs().contains("final1") );
        assertTrue( after.getIDs().contains("final2") );
        assertTrue( after.getIDs().contains("final3") );
        assertTrue( after.getIDs().contains("other") );        
    }
    
	/**
	 * Test method for {@link org.geotools.wfs.v_1_0_0.data.FidFilterVisitor#visit(org.geotools.filter.LogicFilter)}.
	 */
	public void testVisitLogicFilterOR() throws Exception {
	    Set<FeatureId> fidSet1 = Collections.singleton(ff.featureId("new1"));
	    Set<FeatureId> fidSet2 = Collections.singleton(ff.featureId("new2"));
        
	    Or before = ff.or( ff.id( fidSet1 ), ff.id( fidSet2 ) );
	    
	    Or after = (Or) before.accept( visitor, null );
		assertSame( after, after );
	    assertTrue( ((Id)after.getChildren().get(0)).getIDs().contains("final1") );
	    assertTrue( ((Id)after.getChildren().get(1)).getIDs().contains("final2") );
	}
	
	/**
	 * Test method for {@link org.geotools.wfs.v_1_0_0.data.FidFilterVisitor#visit(org.geotools.filter.FidFilter)}.
	 */
	public void testVisitLogicFilterNOT() {
        Set<FeatureId> fidSet1 = Collections.singleton(ff.featureId("new1"));
        Set<FeatureId> fidSet2 = Collections.singleton(ff.featureId("new2"));
        
        And before = ff.and( ff.id( fidSet1 ), ff.id( fidSet2 ) );
        
        And after = (And) before.accept( visitor, null );
        assertSame( after, after );
        assertTrue( ((Id)after.getChildren().get(0)).getIDs().contains("final1") );
        assertTrue( ((Id)after.getChildren().get(1)).getIDs().contains("final2") );
    }
	/**
	 * Lets ensure that others kinds of filters are not harmed 
	 * @throws IllegalFilterException
	 */
	public void testVisitBetweenFilter() {
	    Filter before = ff.between( ff.literal("1"), ff.literal("1"), ff.literal("1"));
		Filter after = (Filter) before.accept(visitor, null );
		assertEquals( before, after );
		assertNotSame( before, after);
	}

	public void testVisitCompareFilter() {
        Filter before = ff.less(ff.literal("1"), ff.literal("1") );
        Filter after = (Filter) before.accept(visitor, null );
        assertEquals( before, after );
        assertNotSame( before, after );
	}

}
