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
package org.geotools.validation.relate;

import java.util.HashSet;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.filter.IllegalFilterException;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.validation.ValidationResults;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.geometry.BoundingBox;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;


/**
 * CrossesIntegrity<br>
 * @author bowens, ptozer<br>
 * Created Apr 27, 2004<br>
 *
 * @source $URL$
 * @version <br>
 * 
 * <b>Puropse:</b><br>
 * <p>
 * Tests to see if a Geometry crosses another Geometry.
 * 
 * <b>Description:</b><br>
 * <p>
 * If only one layer is provided, the geometries of that layer are compared with each other.
 * If two layers are provided, then the geometries are compared across the layers.
 * </p>
 * 
 * <b>Usage:</b><br>
 * <p>
 * 		CrossesIntegrity cross = new CrossesIntegrity();
 *		cross.setExpected(false);
 *		cross.setGeomTypeRefA("my:line");
 *		
 *		Map map = new HashMap();
 *		try
 *		{
 *			map.put("my:line", mds.getFeatureSource("line"));
 *		} catch (IOException e1)
 *		{
 *			e1.printStackTrace();
 *		}
 *		
 *		try
 *		{
 *			assertFalse(cross.validate(map, lineBounds, vr));
 *		} catch (Exception e)
 *		{
 *			e.printStackTrace();
 *		}
 * </p>
 */
public class CrossesIntegrity extends RelationIntegrity 
{
	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.validation");
	private static HashSet usedIDs;
	
	/**
	 * CrossesIntegrity Constructor
	 * 
	 */
	public CrossesIntegrity()
	{
		super();
		usedIDs = new HashSet();	//TODO: remove me later, memory inefficient
	}
	
	
	/* (non-Javadoc)
	 * @see org.geotools.validation.IntegrityValidation#validate(java.util.Map, com.vividsolutions.jts.geom.Envelope, org.geotools.validation.ValidationResults)
	 */
	public boolean validate(Map layers, ReferencedEnvelope envelope,
			ValidationResults results) throws Exception 
	{
		LOGGER.finer("Starting test "+getName()+" ("+getClass().getName()+")" );
		String typeRef1 = getGeomTypeRefA();
		LOGGER.finer( typeRef1 +": looking up SimpleFeatureSource " );    	
		SimpleFeatureSource geomSource1 = (SimpleFeatureSource) layers.get( typeRef1 );
		LOGGER.finer( typeRef1 +": found "+ geomSource1.getSchema().getTypeName() );
		
		String typeRef2 = getGeomTypeRefB();
		if (typeRef2 == EMPTY || typeRef1.equals(typeRef2))
			return validateSingleLayer(geomSource1, isExpected(), results, envelope);
		else
		{
			LOGGER.finer( typeRef2 +": looking up SimpleFeatureSource " );        
			SimpleFeatureSource geomSource2 = (SimpleFeatureSource) layers.get( typeRef2 );
			LOGGER.finer( typeRef2 +": found "+ geomSource2.getSchema().getTypeName() );
			return validateMultipleLayers(geomSource1, geomSource2, isExpected(), results, envelope);
		}	
	
	}


	/**
	 * <b>validateMultipleLayers Purpose:</b> <br>
	 * <p>
	 * This validation tests for a geometry crosses another geometry.
     * Uses JTS' Geometry.crosses(Geometry) method.
     * The DE-9IM intersection matrix for crosses is
   	 * T*T****** (for a point and a curve, a point and an area or a line and an area)
   	 * 0******** (for two curves)
	 * </p>
	 * 
	 * <b>Description:</b><br>
	 * <p>
	 * The function filters the FeatureSources using the given bounding box.
	 * It creates iterators over both filtered FeatureSources. It calls overlaps() and contains()using the
	 * geometries in the SimpleFeatureSource layers. Tests the results of the method call against
	 * the given expected results. Returns true if the returned results and the expected results 
	 * are true, false otherwise.
	 * 
	 * </p>
	 * 
	 * Author: bowens<br>
	 * Created on: Apr 27, 2004<br>
	 * @param featureSourceA - the SimpleFeatureSource to pull the original geometries from. This geometry is the one that is tested for overlaping with the other
	 * @param featureSourceB - the SimpleFeatureSource to pull the other geometries from - these geometries will be those that may overlap the first geometry
	 * @param expected - boolean value representing the user's expected outcome of the test
	 * @param results - ValidationResults
	 * @param bBox - Envelope - the bounding box within which to perform the overlaps() and contains()
	 * @return boolean result of the test
	 * @throws Exception - IOException if iterators improperly closed
	 */
	private boolean validateMultipleLayers(	SimpleFeatureSource featureSourceA, 
											SimpleFeatureSource featureSourceB, 
											boolean expected, 
											ValidationResults results, 
											Envelope bBox) 
	throws Exception
	{
		boolean success = true;
		
		FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
		Filter filter = null;

		//JD: fix this!!
		//filter = (Filter) ff.createBBoxExpression(bBox);

		SimpleFeatureCollection featureResultsA = featureSourceA.getFeatures(filter);
        SimpleFeatureCollection featureResultsB = featureSourceB.getFeatures(filter);
		
		SimpleFeatureIterator fr1 = null;
        SimpleFeatureIterator fr2 = null;
		try 
		{
			fr1 = featureResultsA.features();

			if (fr1 == null)
				return success;
						
			while (fr1.hasNext())
			{
				SimpleFeature f1 = fr1.next();
				Geometry g1 = (Geometry)f1.getDefaultGeometry();
				fr2 = featureResultsB.features();
				
				while (fr2 != null && fr2.hasNext())
				{
					SimpleFeature f2 = fr2.next();
					Geometry g2 = (Geometry)f2.getDefaultGeometry();
					//System.out.println("Do the two overlap?->" + g1.overlaps(g2));
					//System.out.println("Does the one contain the other?->" + g1.contains(g2));
					if(g1.overlaps(g2) != expected || g1.contains(g2) != expected)
					{
						results.error( f1, ((Geometry)f1.getDefaultGeometry()).getGeometryType()+" "+getGeomTypeRefA()+" overlapped "+getGeomTypeRefB()+"("+f2.getID()+"), Result was not "+expected );
						success = false;
					}
				}		
			}
		}finally
		{
			fr1.close();
			if (fr2 != null)
				fr2.close();		
		}
				
		return success;
	}

	/**
	 * <b>validateSingleLayer Purpose:</b> <br>
	 * <p>
	 * This validation tests for a geometry crosses another geometry.
     * Uses JTS' Geometry.crosses(Geometry) method.
     * The DE-9IM intersection matrix for crosses is
   	 * T*T****** (for a point and a curve, a point and an area or a line and an area)
   	 * 0******** (for two curves)
	 * </p>
	 * 
	 * <b>Description:</b><br>
	 * <p>
	 * The function filters the SimpleFeatureSource using the given bounding box.
	 * It creates iterators over the filtered FeatureSource. It calls overlaps() and contains() using the
	 * geometries in the SimpleFeatureSource layer. Tests the results of the method calls against
	 * the given expected results. Returns true if the returned results and the expected results 
	 * are true, false otherwise.
	 * 
	 * </p>	 * 
	 * Author: bowens<br>
	 * Created on: Apr 27, 2004<br>
	 * @param featureSourceA - the SimpleFeatureSource to pull the original geometries from. This geometry is the one that is tested for overlapping itself
	 * @param expected - boolean value representing the user's expected outcome of the test
	 * @param results - ValidationResults
	 * @param bBox - Envelope - the bounding box within which to perform the overlaps() and contains()
	 * @return boolean result of the test
	 * @throws Exception - IOException if iterators improperly closed
	 */
	private boolean validateSingleLayer(SimpleFeatureSource featureSourceA, 
										boolean expected, 
										ValidationResults results, 
										ReferencedEnvelope bBox) 
	throws Exception
	{
		boolean success = true;
		
		SimpleFeatureType ft = featureSourceA.getSchema();
		
		Filter filter = filterBBox(bBox, ft);

		//FeatureResults featureResults = featureSourceA.getFeatures(filter);
		SimpleFeatureCollection featureResults = featureSourceA.getFeatures();
		
		SimpleFeatureIterator fr1 = null;
        SimpleFeatureIterator fr2 = null;
		try 
		{
			fr1 = featureResults.features();

			if (fr1 == null)
				return success;
		
			while (fr1.hasNext())
			{
				//System.out.println("Single layer Outer loop count: " + loopCt1);
				SimpleFeature f1 = fr1.next();
//				System.out.println("overlapFilter " + overlapsFilter.contains(f1));
//				System.out.println("containsFilter " + containsFilter.contains(f1));
				//System.out.println("Filter " + filter.contains(f1));
//				System.out.println("f1 = " + f1.getDefaultGeometry().getEnvelope());
//				System.out.println("env1 = " + bBox);
				
				Geometry g1 = (Geometry)f1.getDefaultGeometry();
				Filter filter2 = filterBBox(ReferencedEnvelope.reference(g1.getEnvelope().getEnvelopeInternal()), ft);

				//FeatureResults featureResults2 = featureSourceA.getFeatures(filter2);
				SimpleFeatureCollection featureResults2 = featureSourceA.getFeatures();
				fr2 = featureResults2.features();	
				while (fr2 != null && fr2.hasNext())
				{			
					SimpleFeature f2 = fr2.next();
					//System.out.println("Filter2 " + filter2.contains(f2));
					Geometry g2 = (Geometry)f2.getDefaultGeometry();
					//System.out.println("Do the two overlap?->" + g1.overlaps(g2));
					//System.out.println("Does the one contain the other?->" + g1.contains(g2));
					if (!usedIDs.contains(f2.getID()))
					{
						
						if (!f1.getID().equals(f2.getID()))	// if they are the same feature, move onto the next one
						{
							if(g1.crosses(g2) != expected)
							{
								//results.error( f1, f1.getDefaultGeometry().getGeometryType()+" "+getGeomTypeRefA()+"("+f1.getID()+")"+" crossed "+getGeomTypeRefA()+"("+f2.getID()+"), Result was not "+expected );
								results.error( f1, getGeomTypeRefA()+"("+f1.getID()+")"+" crossed "+getGeomTypeRefA()+"("+f2.getID()+")");
								System.out.println(((Geometry)f1.getDefaultGeometry()).getGeometryType()+" "+getGeomTypeRefA()+"("+f1.getID()+")"+" crossed "+getGeomTypeRefA()+"("+f2.getID()+"), Result was not "+expected);
								success = false;
							}
						}
					}
				}
				usedIDs.add(f1.getID());
			}
		}finally
		{
			fr1.close();
			if (fr2 != null)
				fr2.close();			
		}
		
		return success;
	}
	
	
	
	private Filter filterBBox(ReferencedEnvelope bBox, SimpleFeatureType ft)
		throws FactoryRegistryException, IllegalFilterException
	{
		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
		Disjoint filter = ff.disjoint(ff.property(ft.getGeometryDescriptor().getLocalName()), 
		        ff.literal(JTS.toGeometry((BoundingBox) bBox)));
		return filter;
	}
}
