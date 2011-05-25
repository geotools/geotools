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

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.geotools.data.DataUtilities;
import org.geotools.data.wfs.v1_0_0.Action.InsertAction;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.CompareFilter;
import org.geotools.filter.FidFilter;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.FilterType;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.PropertyIsEqualTo;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * Testing the ability of WFS to turn transaction state into a series of actions
 * for encoding into a Transaction request.
 * <p>
 * This test focuses on ability to combine a bunch of update actions for the same
 * feature into a single action.
 * 
 * @author Jesse
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/wfs-ng/src/test/java/org/geotools/data/wfs/v1_0_0/ProcessEditsBeforeCommitTest.java $
 */
public class ProcessEditsBeforeCommitTest extends TestCase {

	private static final PropertyIsEqualTo CUSTOM_FILTERALL;
	private static FilterFactory filterFac=FilterFactoryFinder.createFilterFactory();
	static{
		CUSTOM_FILTERALL=filterFac.equals( 
			filterFac.createLiteralExpression(1), filterFac.createLiteralExpression(2) 
		);
	}
	private WFSTransactionState state;
	private SimpleFeatureType featureType;
	private String typename;
	private String GEOM_ATT="geom";
	private String NAME_ATT="name";

	protected void setUp() throws Exception {
		super.setUp();
		state=new WFSTransactionState(null);
		typename="Type";
		featureType=DataUtilities.createType(typename, GEOM_ATT+":Point,"+NAME_ATT+":String");
	}
	
	private SimpleFeature createFeature(int x, int y, String name) throws IllegalAttributeException {
		GeometryFactory fac=new GeometryFactory();
		Object p = fac.createPoint(new Coordinate(x,y));
		
		return SimpleFeatureBuilder.build( featureType, new Object[]{p,name}, null );
	}

	public void testBasicInsertFIDFilterUpdateCollapse() throws Exception {
		SimpleFeature createFeature = createFeature(0,0,NAME_ATT);
		state.addAction(featureType.getTypeName(), new Action.InsertAction(createFeature));
		Map properties=new HashMap(); 
		properties.put(NAME_ATT, "newName");
		state.addAction(featureType.getTypeName(), new Action.UpdateAction(typename, 
				filterFac.createFidFilter(createFeature.getID()),
				properties));
		
		state.combineActions();
		Action.InsertAction action=(InsertAction) state.getActions(featureType.getTypeName()).get(0);
		assertEquals(1, state.getActions(featureType.getTypeName()).size());
		assertEquals("newName", action.getFeature().getAttribute(NAME_ATT));
		assertTrue(((Point)createFeature.getAttribute(GEOM_ATT)).equals((Point)action.getFeature().getAttribute(GEOM_ATT)));
	}
	

	public void testMultiInsertUpdateCollapse() throws Exception {
		SimpleFeature createFeature = createFeature(0,0,NAME_ATT);
		state.addAction(featureType.getTypeName(), new Action.InsertAction(createFeature));
		state.addAction(featureType.getTypeName(), new Action.InsertAction(createFeature(10,10,NAME_ATT)));
		Map properties=new HashMap(); 
		properties.put(NAME_ATT, "newName");
		state.addAction(featureType.getTypeName(), new Action.UpdateAction(typename, 
				filterFac.createFidFilter(createFeature.getID()),
				properties));
		
		state.combineActions();
		Action.InsertAction action=(InsertAction) state.getActions(featureType.getTypeName()).get(0);
		assertTrue(state.getActions(featureType.getTypeName()).get(1) instanceof InsertAction);
		assertEquals(2, state.getActions(featureType.getTypeName()).size());
		assertEquals("newName", action.getFeature().getAttribute(NAME_ATT));
		assertTrue(((Point)createFeature.getAttribute(GEOM_ATT)).equals((Point)action.getFeature().getAttribute(GEOM_ATT)));
	}
	

	public void testMultiFIDFilterUpdateCollapse() throws Exception {
	    SimpleFeature createFeature = createFeature(0,0,NAME_ATT);
		state.addAction(featureType.getTypeName(), new Action.InsertAction(createFeature));
		Map properties=new HashMap(); 
		properties.put(NAME_ATT, "newName");
		state.addAction(featureType.getTypeName(), new Action.UpdateAction(typename, 
				filterFac.createFidFilter(createFeature.getID()),
				properties));
		properties.put(NAME_ATT, "secondUpdate");
		state.addAction(featureType.getTypeName(), new Action.UpdateAction(typename, 
				filterFac.createFidFilter(createFeature.getID()),
				properties));
		
		state.combineActions();
		Action.InsertAction action=(InsertAction) state.getActions(featureType.getTypeName()).get(0);
		assertEquals(1, state.getActions(featureType.getTypeName()).size());
		assertEquals("secondUpdate", action.getFeature().getAttribute(NAME_ATT));
		assertTrue(((Point)createFeature.getAttribute(GEOM_ATT)).equals((Point)action.getFeature().getAttribute(GEOM_ATT)));
	}


	public void testFidAndNonFidFilterUpdates() throws Exception {
	    SimpleFeature createFeature = createFeature(0,0,NAME_ATT);
		Map updateProperties=new HashMap(); 
		updateProperties.put(NAME_ATT, "noMatch");
		state.addAction(featureType.getTypeName(), new Action.UpdateAction(typename, Filter.INCLUDE, updateProperties));
		state.addAction(featureType.getTypeName(), new Action.InsertAction(createFeature));
		updateProperties.put(NAME_ATT, "newName");
		state.addAction(featureType.getTypeName(), new Action.UpdateAction(typename, 
				filterFac.createFidFilter(createFeature.getID()),
				updateProperties));
		updateProperties.put(NAME_ATT, "secondUpdate");
		CompareFilter matchFilter=filterFac.createCompareFilter(FilterType.COMPARE_EQUALS);
		matchFilter.addLeftValue(filterFac.createAttributeExpression("name"));
		matchFilter.addRightValue(filterFac.createLiteralExpression("newName"));
		state.addAction(featureType.getTypeName(), new Action.UpdateAction(typename, matchFilter, updateProperties));

		updateProperties.put(NAME_ATT, "secondNoMatch");
		state.addAction(featureType.getTypeName(), new Action.UpdateAction(typename, CUSTOM_FILTERALL, updateProperties));
		
		state.combineActions();
		Action.InsertAction action=(InsertAction) state.getActions(featureType.getTypeName()).get(3);
		assertEquals(4, state.getActions(featureType.getTypeName()).size());
		assertEquals("secondUpdate", action.getFeature().getAttribute(NAME_ATT));
		assertTrue(((Point)createFeature.getAttribute(GEOM_ATT)).equals((Point)action.getFeature().getAttribute(GEOM_ATT)));
	}
	
	public void testDeleteAndInsert() throws Exception {
	    SimpleFeature createFeature = createFeature(0,0,NAME_ATT);
		Map updateProperties=new HashMap(); 
		updateProperties.put(NAME_ATT, "noMatch");
		state.addAction(featureType.getTypeName(), new Action.UpdateAction(typename, Filter.INCLUDE, updateProperties));
		state.addAction(featureType.getTypeName(), new Action.InsertAction(createFeature));
		updateProperties.put(NAME_ATT, "newName");
		state.addAction(featureType.getTypeName(), new Action.UpdateAction(typename, 
				filterFac.createFidFilter(createFeature.getID()),
				updateProperties));
		updateProperties.put(NAME_ATT, "secondUpdate");
		CompareFilter matchFilter=filterFac.createCompareFilter(FilterType.COMPARE_EQUALS);
		matchFilter.addLeftValue(filterFac.createAttributeExpression("name"));
		matchFilter.addRightValue(filterFac.createLiteralExpression("newName"));
		state.addAction(featureType.getTypeName(), new Action.UpdateAction(typename, matchFilter, updateProperties));

		updateProperties.put(NAME_ATT, "secondNoMatch");
		state.addAction(featureType.getTypeName(), new Action.UpdateAction(typename, CUSTOM_FILTERALL, updateProperties));
		
		state.combineActions();
		Action.InsertAction action=(InsertAction) state.getActions(featureType.getTypeName()).get(3);
		assertEquals(4, state.getActions(featureType.getTypeName()).size());
		assertEquals("secondUpdate", action.getFeature().getAttribute(NAME_ATT));
		assertTrue(((Point)createFeature.getAttribute(GEOM_ATT)).equals((Point)action.getFeature().getAttribute(GEOM_ATT)));
	}
	
	public void testInsertAndDelete() throws Exception {
	    SimpleFeature createFeature = createFeature(0,0,NAME_ATT);
		state.addAction(featureType.getTypeName(), new Action.InsertAction(createFeature));
		state.addAction(featureType.getTypeName(), new Action.DeleteAction(typename, 
				filterFac.createFidFilter(createFeature.getID())));
		
		state.combineActions();
		assertEquals(0, state.getActions(featureType.getTypeName()).size());
	}	
	
	public void testUpdatesDeletesAndInsertes() throws Exception {
	    SimpleFeature createFeature = createFeature(0,0,NAME_ATT);
		Map updateProperties=new HashMap(); 
		updateProperties.put(NAME_ATT, "noMatch");
		
		//create delete actions
		Action.DeleteAction deleteNone = new Action.DeleteAction(typename, CUSTOM_FILTERALL);

		FidFilter createFidFilter = filterFac.createFidFilter();
		createFidFilter.addFid(createFeature.getID());
		createFidFilter.addFid("someotherfid");
		Action.DeleteAction deleteInsert1 = new Action.DeleteAction(typename, createFidFilter);

		//create update actions
		Action.UpdateAction updateAll = new Action.UpdateAction(typename, Filter.INCLUDE, updateProperties);

		updateProperties.put(NAME_ATT, "newName");
		Action.UpdateAction updateInsert1 = new Action.UpdateAction(typename, 
				filterFac.createFidFilter(createFeature.getID()),
				updateProperties);

		updateProperties.put(NAME_ATT, "secondUpdate");
		CompareFilter matchFilter=filterFac.createCompareFilter(FilterType.COMPARE_EQUALS);
		matchFilter.addLeftValue(filterFac.createAttributeExpression("name"));
		matchFilter.addRightValue(filterFac.createLiteralExpression("newName"));
		Action.UpdateAction updateManyIncludingInsert1 = new Action.UpdateAction(typename, matchFilter, updateProperties);

		updateProperties.put(NAME_ATT, "secondNoMatch");
		Action.UpdateAction updateNone = new Action.UpdateAction(typename, CUSTOM_FILTERALL, updateProperties);

		// create Insert actions
		Action.InsertAction insertAction = new Action.InsertAction(createFeature);
		Action.InsertAction insertAction2 = new Action.InsertAction(createFeature(10,10,"name"));

		state.addAction(featureType.getTypeName(), updateAll);
		state.addAction(featureType.getTypeName(), insertAction);
		state.addAction(featureType.getTypeName(), updateInsert1);

		state.addAction(featureType.getTypeName(), insertAction2);
		state.addAction(featureType.getTypeName(), deleteInsert1);
		state.addAction(featureType.getTypeName(), deleteNone);
		state.addAction(featureType.getTypeName(), updateManyIncludingInsert1);

		state.addAction(featureType.getTypeName(), updateNone);
		
		state.combineActions();
		assertEquals(6, state.getActions(featureType.getTypeName()).size());
		assertSame(updateAll, state.getActions(featureType.getTypeName()).get(0));
		assertSame(deleteInsert1, state.getActions(featureType.getTypeName()).get(1));
		assertSame(deleteNone, state.getActions(featureType.getTypeName()).get(2));
		assertSame(updateManyIncludingInsert1, state.getActions(featureType.getTypeName()).get(3));
		assertSame(updateNone, state.getActions(featureType.getTypeName()).get(4));
		assertSame(insertAction2, state.getActions(featureType.getTypeName()).get(5));
	}

	
}
