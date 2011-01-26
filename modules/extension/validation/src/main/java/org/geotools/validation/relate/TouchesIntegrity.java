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

import java.util.Map;
import java.util.logging.Logger;

import org.geotools.data.FeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.validation.ValidationResults;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
/**
 * @author Pati
 *
 * <b>Puropse:</b><br>
 * <p>
 * Tests to see if a Geometry touches another Geometry.
 * 
 * <b>Description:</b><br>
 * <p>
 * If only one Geometry is given, then this test checks to see if it 
 * touches to itself.
 * </p>
 * 
 * <b>Usage:</b><br>
 * <p>
 * 
 * </p>
 * @source $URL$
 */
public class TouchesIntegrity extends RelationIntegrity {
	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.validation");
	
	
	/**
	 * OverlapsIntegrity Constructor
	 * 
	 */
	public TouchesIntegrity()
	{
		super();
	}
	
	
	/* (non-Javadoc)
	 * @see org.geotools.validation.IntegrityValidation#validate(java.util.Map, com.vividsolutions.jts.geom.Envelope, org.geotools.validation.ValidationResults)
	 */
	public boolean validate(Map layers, Envelope envelope,
			ValidationResults results) throws Exception 
	{
		LOGGER.finer("Starting test "+getName()+" ("+getClass().getName()+")" );
		String typeRef1 = getGeomTypeRefA();
		LOGGER.finer( typeRef1 +": looking up FeatureSource<SimpleFeatureType, SimpleFeature> " );    	
		FeatureSource<SimpleFeatureType, SimpleFeature> geomSource1 = (FeatureSource<SimpleFeatureType, SimpleFeature>) layers.get( typeRef1 );
		LOGGER.finer( typeRef1 +": found "+ geomSource1.getSchema().getTypeName() );
		
		String typeRef2 = getGeomTypeRefB();
		if (typeRef2 == EMPTY || typeRef1.equals(typeRef2))
			return validateSingleLayer(geomSource1, isExpected(), results, envelope);
		else
		{
			LOGGER.finer( typeRef2 +": looking up FeatureSource<SimpleFeatureType, SimpleFeature> " );        
			FeatureSource<SimpleFeatureType, SimpleFeature> geomSource2 = (FeatureSource<SimpleFeatureType, SimpleFeature>) layers.get( typeRef2 );
			LOGGER.finer( typeRef2 +": found "+ geomSource2.getSchema().getTypeName() );
			return validateMultipleLayers(geomSource1, geomSource2, isExpected(), results, envelope);
		}	
	
	}


	/**
	 * <b>validateMultipleLayers Purpose:</b> <br>
	 * <p>
	 * This validation tests for a geometry touches another geometry. 
	 * Uses JTS' Geometry.touches(Geometry) method.
	 * Returns true if the DE-9IM intersection matrix for the two Geometrys is FT*******, F**T***** or F***T****.
	 * </p>
	 * 
	 * <b>Description:</b><br>
	 * <p>
	 * The function filters the FeatureSources using the given bounding box.
	 * It creates iterators over both filtered FeatureSources. It calls touches() using the
	 * geometries in the FeatureSource<SimpleFeatureType, SimpleFeature> layers. Tests the results of the method call against
	 * the given expected results. Returns true if the returned results and the expected results 
	 * are true, false otherwise.
	 * 
	 * </p>
	 * 
	 * Author: bowens<br>
	 * Created on: Apr 27, 2004<br>
	 * @param featureSourceA - the FeatureSource<SimpleFeatureType, SimpleFeature> to pull the original geometries from. 
	 * @param featureSourceB - the FeatureSource<SimpleFeatureType, SimpleFeature> to pull the other geometries from.
	 * @param expected - boolean value representing the user's expected outcome of the test
	 * @param results - ValidationResults
	 * @param bBox - Envelope - the bounding box within which to perform the touches()
	 * @return boolean result of the test
	 * @throws Exception - IOException if iterators improperly closed

	 */
	private boolean validateMultipleLayers(	FeatureSource<SimpleFeatureType, SimpleFeature> featureSourceA, 
											FeatureSource<SimpleFeatureType, SimpleFeature> featureSourceB, 
											boolean expected, 
											ValidationResults results, 
											Envelope bBox) 
	throws Exception
	{
		boolean success = true;
		
		Filter filter = null;

		//JD: fix this!!
		//filter = (Filter) ff.createBBoxExpression(bBox);

		FeatureCollection<SimpleFeatureType, SimpleFeature> collectionA = featureSourceA.getFeatures(filter);
		FeatureCollection<SimpleFeatureType, SimpleFeature> collectionB = featureSourceB.getFeatures(filter);
		
		FeatureIterator<SimpleFeature> fr1 = null;
		FeatureIterator<SimpleFeature> fr2 = null;
		try 
		{
			fr1 = collectionA.features();
			if (fr1 == null)
				return false;
						
			while (fr1.hasNext())
			{
				SimpleFeature f1 = fr1.next();
				Geometry g1 = (Geometry) f1.getDefaultGeometry();
				fr2 = collectionB.features();
				try {
    				while (fr2 != null && fr2.hasNext())
    				{
    					SimpleFeature f2 = fr2.next();
    					Geometry g2 = (Geometry) f2.getDefaultGeometry();
    					if(g1.touches(g2) != expected )
    					{
    						results.error( f1, ((Geometry) f1.getDefaultGeometry()).getGeometryType()+" "+getGeomTypeRefA()+" touches "+getGeomTypeRefB()+"("+f2.getID()+"), Result was not "+expected );
    						success = false;
    					}
    				}
                }
                finally {
                    collectionB.close( fr2 );
                }
			}
		}finally
		{
            collectionA.close( fr1 );            
		}
				
		return success;
	}



	/**
	 * <b>validateSingleLayer Purpose:</b> <br>
	 * <p>
	 * This validation tests for a geometry that touches on itself. 
	 * Uses JTS' Geometry.touches(Geometry) method.
	 * Returns true if the DE-9IM intersection matrix for the two Geometrys is FT*******, F**T***** or F***T****.
	 * </p>
	 * 
	 * <b>Description:</b><br>
	 * <p>
	 * The function filters the FeatureSource<SimpleFeatureType, SimpleFeature> using the given bounding box.
	 * It creates iterators over the filtered FeatureSource. It calls touches() using the
	 * geometries in the FeatureSource<SimpleFeatureType, SimpleFeature> layer. Tests the results of the method call against
	 * the given expected results. Returns true if the returned results and the expected results 
	 * are true, false otherwise.
	 * 
	 * </p>	
	 * 
	 * Author: bowens<br>
	 * Created on: Apr 27, 2004<br>
	 * @param featureSourceA - the FeatureSource<SimpleFeatureType, SimpleFeature> to pull the original geometries from. 
	 * @param expected - boolean value representing the user's expected outcome of the test
	 * @param results - ValidationResults
	 * @param bBox - Envelope - the bounding box within which to perform the touches()
	 * @return boolean result of the test
	 * @throws Exception - IOException if iterators improperly closed
	 */
	private boolean validateSingleLayer(FeatureSource<SimpleFeatureType, SimpleFeature> featureSourceA, 
										boolean expected, 
										ValidationResults results, 
										Envelope bBox) 
	throws Exception
	{
		boolean success = true;
		
		FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
		Filter filter = null;

		//JD: fix this !!
		//filter = (Filter) ff.createBBoxExpression(bBox);

		FeatureCollection<SimpleFeatureType, SimpleFeature> collection = featureSourceA.getFeatures(filter);
		
		FeatureIterator<SimpleFeature> fr1 = null;
		FeatureIterator<SimpleFeature> fr2 = null;
		try 
		{
			fr1 = collection.features();

			if (fr1 == null)
				return false;
					
			while (fr1.hasNext())
			{
				SimpleFeature f1 = fr1.next();
				Geometry g1 = (Geometry) f1.getDefaultGeometry();
				fr2 = collection.features();
				try {
    				while (fr2 != null && fr2.hasNext())
    				{
    					SimpleFeature f2 = fr2.next();
    					Geometry g2 = (Geometry) f2.getDefaultGeometry();
    					if (!f1.getID().equals(f2.getID()))	// if they are the same feature, move onto the next one
    					{
    						if(g1.touches(g2) != expected )
    						{
    							results.error( f1, ((Geometry) f1.getDefaultGeometry()).getGeometryType()+" "+getGeomTypeRefA()+" touches "+getGeomTypeRefA()+"("+f2.getID()+"), Result was not "+expected );
    							success = false;
    						}
    					}
    				}
                }
                finally {
                    collection.close( fr2 );
                }
			}
		}
        finally {
            collection.close( fr1 );
		}
		
		return success;
	}	
}
