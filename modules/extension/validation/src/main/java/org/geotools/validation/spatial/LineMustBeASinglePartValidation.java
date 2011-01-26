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

import com.vividsolutions.jts.geom.LineString;


/**
 * LineIsSingleSegmentFeatureValidation purpose.
 * 
 * <p>
 * Tests to see if a LineString is made of only one segment, meaning it only
 * has two points. If the LineString has more than two points, the test fails.
 * </p>
 * 
 * <p>
 * This method has been extended to work with MultiLineStrings - this is
 * the most common format that shapefile appears in and as such is forcing
 * our hand.
 * </p>
 * 
 * <p>
 * Example Use:
 * <pre><code>
 * LineIsSingleSegmentFeatureValidation x = new LineIsSingleSegmentFeatureValidation("noSelfIntersectRoads", "Tests to see if a 
 * geometry intersects itself", new String[] {"road"});
 * </code></pre>
 * </p>
 *
 * @author bowens, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @source $URL$
 * @version $Id$
 */
public class LineMustBeASinglePartValidation extends DefaultFeatureValidation {
    /** The logger for the validation module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.validation");

    /**
     * LineIsSingleSegmentFeatureValidation constructor.
     * 
     * <p>
     * Description
     * </p>
     */
    public LineMustBeASinglePartValidation() {
    }

    /**
     * Override getPriority.
     * 
     * <p>
     * Sets the priority level of this validation.
     * </p>
     *
     * @return <code>PRIORITY_SIMPLE</code>
     *
     * @see org.geotools.validation.Validation#getPriority()
     */
    public int getPriority() {
        return PRIORITY_SIMPLE;
    }

    /**
     * Override validate.
     * 
     * <p>
     * Tests to see if a LineString is made of only one segment, meaning it
     * only has two points. If the LineString has more than two points, the
     * test fails.
     * </p>
     *
     * @param feature The Feature to be validated
     * @param type The FeatureTypeInfo of the feature
     * @param results The storage for error messages.
     *
     * @return True if the feature is simple (one segment).
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
        catch( ClassCastException wrongType ){
            results.warning(feature, wrongType.getMessage() );
            return true;
        }
        if( line == null ){
            // Geometry was null - user can check with nullZero
            return true;
        }
        final int NUMBER_OF_POINTS = line.getNumPoints();
        if (NUMBER_OF_POINTS < 2) {
            results.error(feature,
                "LineString contains too few points");
            return false;
        }
        else if (NUMBER_OF_POINTS > 2) {
            // log the error and return
            String message = "LineString is not single part (contains "+(NUMBER_OF_POINTS-1)+" segments)";
            results.error(feature, message);
            LOGGER.log(Level.FINEST, getName() + "(" + feature.getID() + "):" + message);

            return false;
        }
        LOGGER.log(Level.FINEST, getName() + "(" + feature.getID() + ") passed");
        return true;
    }
}
