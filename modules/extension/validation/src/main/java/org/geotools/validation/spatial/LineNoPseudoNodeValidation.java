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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.LineString;


/**
 * PointCoveredByLineValidation purpose.
 * 
 * <p>
 * Checks to ensure the Line does not have a psuedo-node.
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @source $URL$
 * @version $Id$
 */
public class LineNoPseudoNodeValidation extends LineAbstractValidation {
	
	private int degreesAllowable;
    /**
     * PointCoveredByLineValidation constructor.
     * 
     * <p>
     * Super
     * </p>
     */
    public LineNoPseudoNodeValidation() {
        super();
    }

    /**
     * Ensure Line does not have a psuedo-node.
     * 
     * <p></p>
     *
     * @param layers a HashMap of key="TypeName" value="FeatureSource"
     * @param envelope The bounding box of modified features
     * @param results Storage for the error and warning messages
     *
     * @return True if no features intersect. If they do then the validation
     *         failed.
     *
     * @throws Exception DOCUMENT ME!
     *
     * @see org.geotools.validation.IntegrityValidation#validate(java.util.Map,
     *      com.vividsolutions.jts.geom.Envelope,
     *      org.geotools.validation.ValidationResults)
     */
    public boolean validate(Map layers, Envelope envelope,
        ValidationResults results) throws Exception {

    	boolean r = true;
    	
        SimpleFeatureSource fsLine = (SimpleFeatureSource) layers.get(getLineTypeRef());
        SimpleFeatureCollection fcLine = fsLine.getFeatures();
        SimpleFeatureIterator fLine = fcLine.features();
                
        while(fLine.hasNext()){
        	SimpleFeature line = fLine.next();
        	Geometry lineGeom = (Geometry) line.getDefaultGeometry();
        	if(envelope.contains(lineGeom.getEnvelopeInternal())){
        		// 	check for valid comparison
        		if(LineString.class.isAssignableFrom(lineGeom.getClass())){
        			Coordinate[] c = lineGeom.getCoordinates();
        			int i=0;
        			while(i+2<c.length){
        				LineSegment ls1 = new LineSegment(c[i],c[i+1]);
        				LineSegment ls2 = new LineSegment(c[i+1],c[i+2]);
        				double a1 = ls1.angle();
        				double a2 = ls2.angle();
        				if(!((a1-degreesAllowable)<a1 && (a1+degreesAllowable)>a2)){
        					results.error(line,"Atleast one node was too close to the other the perpendicular line between the node's two neighbours.");
							i = c.length;
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
	 * Access degreesAllowable property.
	 * 
	 * @return Returns the degreesAllowable.
	 */
	public int getDegreesAllowable() {
		return degreesAllowable;
	}
	/**
	 * Set degreesAllowable to degreesAllowable.
	 *
	 * @param degreesAllowable The degreesAllowable to set.
	 */
	public void setDegreesAllowable(int degreesAllowable) {
		this.degreesAllowable = degreesAllowable;
	}
}
