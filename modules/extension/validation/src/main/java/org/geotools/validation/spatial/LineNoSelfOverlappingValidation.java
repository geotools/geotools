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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.validation.DefaultFeatureValidation;
import org.geotools.validation.ValidationResults;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;


/**
* Ensure the defaultGeometry does not overlap (only works for LineString).
*
* <p>
* Tests to see if a LineString overlaps itself. It does this by breaking up
* the LineString into two point segments then intersects them all. If a
* segment has both of its points on another segment, then they overlap. This
* is not true in all cases and this method has to be rewritten. If a segment
* spans two segments, this method will say that they do not overlap when
* clearly they do.
* </p>
*
* @author bowens, Refractions Research, Inc.
* @author $Author: jive $ (last modification)
 *
 * @source $URL$
* @version $Id$
*/
public class LineNoSelfOverlappingValidation extends DefaultFeatureValidation {
   /** The logger for the validation module. */
   private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
           "org.geotools.validation");

   /**
    * LineNoSelfOverlappingFeatureValidation constructor.
    *
    * <p>
    * Description
    * </p>
    */
   public LineNoSelfOverlappingValidation() {
   		System.out.println("***************** LineNoSelfOverlappingValidation *************");
   }

   /**
    * Override getPriority.
    *
    * <p>
    * Sets the priority level of this validation.
    * </p>
    *
    * @return A made up priority for this validation.
    *
    * @see org.geotools.validation.Validation#getPriority()
    */
   public int getPriority() {
       return PRIORITY_COMPLEX;
   }


   /**
    * Tests to see if a LineString overlaps itself.
    *
    * <p>
    * It does this by breaking up the LineString into two point segments then
    * intersects them all. If a segment has both of its points on another
    * segment, then they overlap. This is not true in all cases and this
    * method has to be rewritten. If a segment spans two segments, this
    * method will say that they do not overlap when clearly they do.
    * </p>
    *
    * @param feature The Feature to be validated
    * @param type The FeatureTypeInfo of the feature
    * @param results The storage for error messages.
    *
    * @return True if the feature does not overlap itself.
    *
    * @see org.geotools.validation.FeatureValidation#validate(org.geotools.feature.Feature,
    *      org.geotools.feature.FeatureTypeInfo,
    *      org.geotools.validation.ValidationResults)
    */
   public boolean validate(SimpleFeature feature, SimpleFeatureType type,
       ValidationResults results) {
       //BUG: refer to comments above.
       LOGGER.setLevel(Level.ALL);
  
       LineString line = null;
       try {
           line = getDefaultLineString( feature );
       }
       catch( ClassCastException unLine ){            
           results.error(feature,"Geometry is required to be a LineString");
           System.out.println( feature.getID()+"  name: "+getName() );
           System.out.println( feature.getID()+"   ref: "+getTypeRef() );
           System.out.println( feature.getID()+"   ref: "+getTypeRefs() );            
       }
       if (line == null) {
           // Ignore null geometry (user can check with nullZero )
           return true;
       }
       if (line.getNumPoints() < 2) {
           results.warning(feature,"LineString contains too few points");            
           return false;
       }
       GeometryFactory gf = new GeometryFactory();

       int numPoints = line.getNumPoints();

       // break up the LineString into line segments
       LineString[] segments = new LineString[numPoints - 1];

       for (int i = 0; i < (numPoints - 1); i++) {
           Coordinate[] coords = new Coordinate[] {
                   line.getCoordinateN(i), line.getCoordinateN(i + 1)
               };
           segments[i] = gf.createLineString(coords);
       }
       
       // overlap all of the line segments with each other
       for (int i = 0; i < segments.length; i++) // for each line segment
       {
           for (int j = 0; j < segments.length; j++) // test with every other line segment
           {
           		if ((i != j)) // if they aren't the same segment
           		{
					if (segments[i].relate(segments[j],"1********")) // changed to relate - bowens
					{
	                    results.error(feature, "LineString overlapped itself.");
	                    return false;
	                }
           		}
           	}
       }
       
       //LOGGER.log(Level.FINEST, getName() + "(" + feature.getID() + ") passed");
       return true;
   }
     /*
    * touchesSegment
    * iterates all segments and returns true as soon as the point is found to intersect with the linestring
    * */
   private boolean touchesSegment(LineString[] segments, Point p1) {
       for(int i = 0; i < segments.length; i++ ) {
           //using intersects will handle a point that is not  an end point on the line segment
           if(p1.intersects(segments[i])) {
               return true;
           }
       }
       return false;
   }
}

