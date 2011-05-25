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


/**
 * LineNoSelfIntersectFeatureValidation purpose.
 * 
 * <p>
 * Tests to see if a geometry crosses itself. It does not detect if a
 * segment of a LineString doubles back on itself for one segment, then
 * terminates. A different validation is needed to test overlapping. Uses JTS'
 * crosses routine.
 * </p>
 * 
 * <p>
 * Example Use:
 * <pre><code>
 * LineNoSelfIntersectFeatureValidation x = new LineNoSelfIntersectFeatureValidation("noSelfIntersectRoads", "Tests to see if a 
 * geometry intersects itself", new String[] {"road"});
 * </code></pre>
 * </p>
 *
 * @author bowens, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 *
 * @source $URL$
 * @version $Id$
 * - bowens: changed intersects to crosses
 */
public class LineNoSelfIntersectValidation extends DefaultFeatureValidation {
    /** The logger for the validation module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.validation");

    /**
     * LineNoSelfIntersectFeatureValidation constructor.
     * 
     * <p>
     * Description
     * </p>
     */
    public LineNoSelfIntersectValidation() {
    }

    /**
     * Override getPriority.
     * 
     * <p>
     * Sets the priority level of this validation. This is set by the
     * programmer and is a measure of the expense of this plugin
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
     * Override validate.
     * 
     * <p>
     * Tests to see if a geometry intersects itself. It does not detect if a
     * segment of a LineString doubles back on itself for one segment, then
     * terminates. A different validation is needed to test overlapping. Uses
     * JTS' intersect routine.
     * </p>
     *
     * @param feature The Feature to be validated.
     * @param type The FeatureTypeInfo of the feature.
     * @param results The storage for error messages.
     *
     * @return True if the feature does not self intersect.
     *
     * @see org.geotools.validation.FeatureValidation#validate(org.geotools.feature.Feature,
     *      org.geotools.feature.FeatureTypeInfo,
     *      org.geotools.validation.ValidationResults)
     */
    public boolean validate(SimpleFeature feature, SimpleFeatureType type,
        ValidationResults results) {
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

        // intersect all of the line segments with each other
        for (int i = 0; i < segments.length; i++) // for each line segment
         {
            for (int j = 0; j < segments.length; j++) // intersect with every other line segment
             {
                if ((i != j) && ((i - 1) != j) && ((i + 1) != j)) // if they aren't the same segment
                 {
                    if (segments[i].crosses(segments[j])) // changed to crosses - bowens
                     {
                        // log the error and return
                        results.error(feature, "LineString crossed itself");
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
