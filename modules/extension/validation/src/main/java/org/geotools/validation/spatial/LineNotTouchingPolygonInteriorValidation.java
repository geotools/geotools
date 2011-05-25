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
 * PolygonNotOverlappingLineValidation purpose.
 * 
 * <p>
 * Checks that the line is not touching the interior of the polygon.
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 *
 * @source $URL$
 * @version $Id$
 */
public class LineNotTouchingPolygonInteriorValidation
    extends LinePolygonAbstractValidation {
    /**
     * PolygonNotOverlappingLineValidation constructor.
     * 
     * <p>
     * Description
     * </p>
     */
    public LineNotTouchingPolygonInteriorValidation() {
        super();
    }

    /**
     * Check that the line is not touching the interior of the polygon.
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
        if(fsLine == null)
        	return true;
        SimpleFeatureCollection fcLine = fsLine.getFeatures();
        SimpleFeatureIterator fLine = fcLine.features();
        
        SimpleFeatureSource fsPoly = (SimpleFeatureSource) layers.get(getRestrictedPolygonTypeRef());
        if(fsPoly == null)
        	return true;
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
        						Polygon p = (Polygon)polyGeom;
        						for(int i=0;i<p.getNumInteriorRing();i++){
        							if(!p.getInteriorRingN(i).touches(lineGeom)){
        								results.error(poly,"Polygon interior touches the specified Line.");
        							}
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
}
