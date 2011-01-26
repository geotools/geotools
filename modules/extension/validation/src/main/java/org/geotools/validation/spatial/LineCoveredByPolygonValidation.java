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
package org.geotools.validation.spatial;

import java.util.Map;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.validation.ValidationResults;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;


/**
 * Ensure a LineString is contained by Polygon.
 * 
 * <p>
 * This is an integrity test in which we ensure that every LineString in a
 * FeatureType is contained by a Polygon in a second FeatureType. This needs
 * to be done as an Integrity Test to account for both the LineString and
 * Polygon FeatureTypes being changed in the same Transaction. Further more
 * the test will need to be run if either (or both) FeatureTypes are changed:
 * a new line may be created outside of a polygon, or a polygon may be deleted
 * leaving a line uncovered. If we a bit smarter about these relationship we
 * could run this Validation Test only on LineString insert/modify or Polygon
 * modify/delete but life is too short, and there is always another release.
 * </p>
 * 
 * <p>
 * To do this with any sense of efficiency we will need to take an initial run
 * through the Polygon SimpleFeatureSource to build an Index of FeatureID by
 * BoundingBox. We can use this to selectively query the Polygon FeatureSource
 * as we work through the LineString content.
 * </p>
 * 
 * <p>
 * TODO: David Zweirs Read This! Talk to Justin or any of the JUMP experts who
 * have experence in implementing JTS indexes. You can use the "layers" Map to
 * store the generated index, or make up an API to do so as part of the
 * ValidationProcessor. At the very least your index will last for the current
 * validation "run" - which is all that can be expected. What really needs to
 * be done is punt the index generation off to the database/DataStore anything
 * else won't scale (don't you love GIS problems).
 * </p>
 *
 * @author Jody Garnett, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @source $URL$
 * @version $Id$
 */
public class LineCoveredByPolygonValidation
    extends LinePolygonAbstractValidation {

    /**
     * No argument constructor, required by the Java Bean Specification.
     */
    public LineCoveredByPolygonValidation() {
    }

    /**
     * Check that lineTypeRef is convered by polygonTypeRef.
     * 
     * <p>
     * Detailed description...
     * </p>
     *
     * @param layers Map of SimpleFeatureSource by "dataStoreID:typeName"
     * @param envelope The bounding box that encloses the unvalidated data
     * @param results Used to coallate results information
     *
     * @return <code>true</code> if all the features pass this test.
     *
     * @throws Exception DOCUMENT ME!
     */
    public boolean validate(Map layers, Envelope envelope,
        ValidationResults results) throws Exception {

    	boolean r = true;
    	
        SimpleFeatureSource fsLine = (SimpleFeatureSource) layers.get(getLineTypeRef());
        
        SimpleFeatureCollection fcLine = fsLine.getFeatures();
        SimpleFeatureIterator fLine = fcLine.features();
        
        SimpleFeatureSource fsPoly = (SimpleFeatureSource) layers.get(getRestrictedPolygonTypeRef());
        
        SimpleFeatureCollection fcPoly = fsPoly.getFeatures();
                
        while(fLine.hasNext()){
        	SimpleFeature line = fLine.next();
        	SimpleFeatureIterator fPoly = fcPoly.features();
        	Geometry lineGeom = (Geometry) line.getDefaultGeometry();
        	if(envelope.contains(lineGeom.getEnvelopeInternal())){
        		// 	check for valid comparison
        		if(LineString.class.isAssignableFrom(lineGeom.getClass())){
        			while(fPoly.hasNext()){
        				SimpleFeature poly = fPoly.next();
        				Geometry polyGeom = (Geometry) poly.getDefaultGeometry(); 
        				if(envelope.contains(polyGeom.getEnvelopeInternal())){
        					if(Polygon.class.isAssignableFrom(polyGeom.getClass())){
        						if(!polyGeom.contains(lineGeom)){
        							results.error(poly,"Polygon does not contain the specified Line.");
        							r = false;
        						}
                    		// do next.
        					}else{
        						fcPoly.remove(poly);
        						results.warning(poly,"Invalid type: this feature is not a derivative of a Polygon");
        					}
        				}else{
        					fcPoly.remove(poly);
        				}
        			}
        		}else{
        			results.warning(line,"Invalid type: this feature is not a derivative of a LineString");
        		}
        	}
        }
        return r;
    }

    /**
     * The priority level used to schedule this Validation.
     *
     * @return PRORITY_SIMPLE
     *
     * @see org.geotools.validation.Validation#getPriority()
     */
    public int getPriority() {
        return PRIORITY_SIMPLE;
    }
}
