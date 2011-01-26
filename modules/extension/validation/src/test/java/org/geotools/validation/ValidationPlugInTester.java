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
package org.geotools.validation;

import java.util.HashMap;

import org.geotools.data.DataTestCase;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.validation.spatial.LineNoSelfIntersectValidation;
import org.geotools.validation.spatial.LineNoSelfOverlappingValidation;
import org.geotools.validation.spatial.LinesNotIntersectValidation;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * ValidationPlugInTester purpose.
 * <p>
 * Description of ValidationPlugInTester ...
 * <p>
 * Capabilities:
 * <ul>
 * </li></li>
 * </ul>
 * Example Use:
 * <pre><code>
 * ValidationPlugInTester x = new ValidationPlugInTester(...);
 * </code></pre>
 * 
 * @author bowens, Refractions Research, Inc.
 * @author $Author: sploreg $ (last modification)
 * @source $URL$
 * @version $Id$
 */
public class ValidationPlugInTester extends DataTestCase {

	MemoryDataStore store;
	ValidationProcessor processor;
	
	/**
	 * ValidationPlugInTester constructor.
	 * <p>
	 * Description
	 * </p>
	 * @param arg0
	 */
	public ValidationPlugInTester(String arg0) {
		super(arg0);
		
	}
	
	/**
	 * Construct data store for use.
	 * 
	 * @see junit.framework.TestCase#setUp()
	 * 
	 * @throws Exception
	 */
	protected void setUp() throws Exception {
		super.setUp();
		store = new MemoryDataStore();
		store.addFeatures( roadFeatures );
		store.addFeatures( riverFeatures );
		processor = new ValidationProcessor();
	}

	/**
	 * Override tearDown.
	 *
	 * @see junit.framework.TestCase#tearDown()
	 * 
	 * @throws Exception
	 */
	protected void tearDown() throws Exception {
		store = null;
		super.tearDown();
		
	}
		
		
	public void testLineNoSelfIntersectFV_CorrectData()
	{
		// the visitor
		RoadNetworkValidationResults roadValidationResults = new RoadNetworkValidationResults();
			 
		LineNoSelfIntersectValidation valid 
					= new LineNoSelfIntersectValidation();
        valid.setName("RoadSelfIntersect");
        valid.setDescription("Tests to see if a road intersects itself, which is bad!");
        valid.setTypeRef( "road:road" );
		
		try {
			processor.addValidation(valid);
		} catch (Exception e) {
			assertTrue(false);
		}
		
		try {
			processor.runFeatureTests("road", DataUtilities.collection(this.roadFeatures), roadValidationResults);
		} catch (Exception e1) {
			assertTrue(false);
		}
		
		System.out.println("NoLineSelfIntersect - correct");
		String[] messages = roadValidationResults.getFailedMessages();
		for (int i=0; i<messages.length; i++){
			System.out.println(messages[i]);
        }
		assertTrue(roadValidationResults.getFailedMessages().length == 0);
		
	}
	
	public void testLineNoSelfIntersectFV_IncorrectData()
	{
		// the visitor
		RoadNetworkValidationResults roadValidationResults = new RoadNetworkValidationResults();
		 
		/*LineNoSelfIntersectValidation selfIntersectValidatorRoads 
					= new LineNoSelfIntersectValidation("RoadSelfIntersect", 
							"Tests to see if a road intersects itself, which is bad!", 
							new String[] {"road"});*/
	
		try {
			//processor.addValidation(selfIntersectValidatorRoads);
		} catch (Exception e) {
			assertTrue(false);
		}
	
		// produce a broken road (newRoad)
		try {
			this.newRoad = SimpleFeatureBuilder.build(this.roadType, new Object[] {
				new Integer(2), line(new int[] { 3, 6, 3, 8, 5, 8, 5, 7, 2, 7}), "r4"
			}, "road.rd4");
		} catch (IllegalAttributeException e) {}
		
		try {
             FeatureReader<SimpleFeatureType, SimpleFeature> reader = DataUtilities.reader(new SimpleFeature[] {this.newRoad});
            String typeName = reader.getFeatureType().getTypeName();            
			SimpleFeatureCollection collection = DataUtilities.collection(new SimpleFeature[] {this.newRoad});
            processor.runFeatureTests("road", collection, roadValidationResults);
			}
		catch (Exception e1) {
			assertTrue(false);
		}
	
		System.out.println("NoLineSelfIntersect - incorrect");
		String[] messages = roadValidationResults.getFailedMessages();
		for (int i=0; i<messages.length; i++){
			System.out.println(messages[i]);
        }
		assertTrue(roadValidationResults.getFailedMessages().length > 0);
	
	}
	
	
	public void testLineNoSelfOverlapFV_CorrectData()
	{
		// the visitor
		RoadNetworkValidationResults roadValidationResults = new RoadNetworkValidationResults();
		 
		LineNoSelfOverlappingValidation selfOverlappingValidatorRoads 
					= new LineNoSelfOverlappingValidation();
        selfOverlappingValidatorRoads.setName("RoadSelfIntersect");
        selfOverlappingValidatorRoads.setDescription("Tests to see if a road intersects itself, which is bad!");
        selfOverlappingValidatorRoads.setTypeRef("road");
	
		try {
			processor.addValidation(selfOverlappingValidatorRoads);
		} catch (Exception e) {
			assertTrue(false);
		}
	
		try {
			processor.runFeatureTests( "id", DataUtilities.collection(this.roadFeatures), roadValidationResults);
		} catch (Exception e1) {
			assertTrue(false);
		}
		
		System.out.println("NoLineSelfOverlap - correct");
		String[] messages = roadValidationResults.getFailedMessages();
		for (int i=0; i<messages.length; i++)
			System.out.println(messages[i]);
		assertTrue(roadValidationResults.getFailedMessages().length == 0);
	
	}
	
	public void testLineNoSelfOverlapFV_IncorrectData()
	{
		// the visitor
		RoadNetworkValidationResults roadValidationResults = new RoadNetworkValidationResults();
	 
        LineNoSelfOverlappingValidation selfOverlappingValidatorRoads 
        = new LineNoSelfOverlappingValidation();
        selfOverlappingValidatorRoads.setName("RoadSelfIntersect");
        selfOverlappingValidatorRoads.setDescription("Tests to see if a road intersects itself, which is bad!");
        selfOverlappingValidatorRoads.setTypeRef("road");
        

		try {
			processor.addValidation(selfOverlappingValidatorRoads);
		} catch (Exception e) {
			assertTrue(false);
		}

		// produce a broken road (newRoad)
		try {
			this.newRoad = SimpleFeatureBuilder.build(this.roadType, (new Object[] {
				new Integer(2), line(new int[] { 7, 7, 8, 7, 9, 7, 9, 6, 8, 6, 8, 7, 7, 7}), "r4"
			}), "road.rd4");
		} catch (IllegalAttributeException e) {}
	
		try {
			processor.runFeatureTests( "datastoreId", DataUtilities.collection(new SimpleFeature[] {this.newRoad}), roadValidationResults);
			}
		catch (Exception e1) {
			assertTrue(false);
		}

		System.out.println("NoLineSelfOverlap - incorrect");
		String[] messages = roadValidationResults.getFailedMessages();
		for (int i=0; i<messages.length; i++){
			System.out.println(messages[i]);
        }
		assertTrue(roadValidationResults.getFailedMessages().length > 0);

	}
	
	
	public void testLineIsSingleSegmentFV_CorrectData()
	{
		// the visitor
		RoadNetworkValidationResults roadValidationResults = new RoadNetworkValidationResults();
		 
		/*LineMustBeASinglePartValidation singleSegmentLineValidatorRoads 
					= new LineMustBeASinglePartValidation("RoadSelfIntersect", 
							"Tests to see if a road intersects itself, which is bad!", 
							new String[] {"road"});
	
		try {
			processor.addValidation(singleSegmentLineValidatorRoads);
		} catch (Exception e) {
			assertTrue(false);
		}*/
	
		try {
			processor.runFeatureTests("dataStoreId", DataUtilities.collection(new SimpleFeature[] {this.newRoad}), roadValidationResults);
		} catch (Exception e1) {
			assertTrue(false);
		}
		
		System.out.println("LineIsSingleSegment - correct");
		String[] messages = roadValidationResults.getFailedMessages();
		for (int i=0; i<messages.length; i++)
			System.out.println(messages[i]);
		assertTrue(roadValidationResults.getFailedMessages().length == 0);
	
	}
	
	public void testLineIsSingleSegmentFV_IncorrectData()
	{
		// the visitor
		RoadNetworkValidationResults roadValidationResults = new RoadNetworkValidationResults();
	 /*
		LineMustBeASinglePartValidation singleSegmentLineValidatorRoads 
					= new LineMustBeASinglePartValidation("RoadSelfIntersect", 
							"Tests to see if a road intersects itself, which is bad!", 
							new String[] {"road"});

		try {
			processor.addValidation(singleSegmentLineValidatorRoads);
		} catch (Exception e) {
			assertTrue(false);
		}*/
	
		try {
			processor.runFeatureTests("dataStoreId", DataUtilities.collection(this.roadFeatures), roadValidationResults);
			}
		catch (Exception e1) {
			assertTrue(false);
		}

		System.out.println("LineIsSingleSegment - incorrect");
		String[] messages = roadValidationResults.getFailedMessages();
		for (int i=0; i<messages.length; i++){
			System.out.println(messages[i]);
        }
		assertTrue(roadValidationResults.getFailedMessages().length > 0);

	}


	public void testLinesIntersectIV_CorrectData()
	{
		// the visitor
		RoadNetworkValidationResults roadValidationResults = new RoadNetworkValidationResults();
		 
		LinesNotIntersectValidation noIntersectingLinesValidatorAll 
					= new LinesNotIntersectValidation();
        noIntersectingLinesValidatorAll.setName("linesIntersect"); 
        noIntersectingLinesValidatorAll.setDescription("Tests to see if any line geometries cross!"); 
        noIntersectingLinesValidatorAll.setLineTypeRef("road");
        noIntersectingLinesValidatorAll.setLineTypeRef("river");        
	
		try {
			processor.addValidation(noIntersectingLinesValidatorAll);
		} catch (Exception e) {
			assertTrue(false);
		}
		
		
//		Feature[] roads = new Feature[2];
//		roads[0] = roadFeatures[0];
//		roads[1] = newRoad;
//		roadFeatures = null;
//		roadFeatures = new Feature[2];
//		roadFeatures = roads;
//		
//		Feature[] rivers = new Feature[1];
//		rivers[0] = riverFeatures[1];
//		rivers[0] = newRiver;
//		riverFeatures = null;
//		riverFeatures = new Feature[2];
//		riverFeatures = roads;
//		
//		store = new MemoryDataStore();
//		store.addFeatures( roadFeatures );
//		store.addFeatures( riverFeatures );
		
		
		HashMap layers = new HashMap();
		try {
			layers.put("dataStoreId:road", DataUtilities.source(new SimpleFeature[] {newRoad}));
			layers.put("dataStoreId:river", DataUtilities.source(riverFeatures));
		}
		catch (Exception e) {
			assertTrue(false);
		}
	
	
		try {
			processor.runIntegrityTests( null, layers, null, roadValidationResults);
		} catch (Exception e1) {
			assertTrue(false);
		}
		
		System.out.println("NoLinesIntersect - correct");
		String[] messages = roadValidationResults.getFailedMessages();
		for (int i=0; i<messages.length; i++)
			System.out.println(messages[i]);
		assertTrue(roadValidationResults.getFailedMessages().length == 0);
	
	}
	
	public void testLinesIntersectIV_IncorrectData()
	{
		// the visitor
		RoadNetworkValidationResults roadValidationResults = new RoadNetworkValidationResults();
	 
        LinesNotIntersectValidation noIntersectingLinesValidatorAll 
        = new LinesNotIntersectValidation();
        noIntersectingLinesValidatorAll.setName("linesIntersect"); 
        noIntersectingLinesValidatorAll.setDescription("Tests to see if any line geometries cross!"); 
        noIntersectingLinesValidatorAll.setLineTypeRef("road");
        noIntersectingLinesValidatorAll.setLineTypeRef("river");        
        

		try {
			processor.addValidation(noIntersectingLinesValidatorAll);
		} catch (Exception e) {
			assertTrue(false);
		}
	
	
		HashMap layers = new HashMap();
		try {
			layers.put("dataStoreId:road", store.getFeatureSource("road"));					
			layers.put("dataStoreId:river", store.getFeatureSource("river"));			
		}
		catch (Exception e) {
			assertTrue(false);
		}
	
	
		try {
			processor.runIntegrityTests( null, layers, null, roadValidationResults);
		}
		catch (Exception e1) {
			assertTrue(false);
		}

		System.out.println("NoLinesIntersect - incorrect");
		String[] messages = roadValidationResults.getFailedMessages();
		for (int i=0; i<messages.length; i++)
			System.out.println(messages[i]);
		assertTrue(roadValidationResults.getFailedMessages().length > 0);

	}
	
	
}
