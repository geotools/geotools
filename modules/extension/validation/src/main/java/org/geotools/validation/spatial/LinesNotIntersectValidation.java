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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.validation.ValidationResults;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;


/**
 * This validation plugIn checks to see if any features intersect.
 * 
 * <p>
 * If they do then the validation failed.
 * </p>
 *
 * @author Brent Owens, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 *
 * @source $URL$
 * @version $Id$
 */
public class LinesNotIntersectValidation extends LineLineAbstractValidation {
    /**
     * An no argument constructor (for the Java Beans Specification)
     */
    public LinesNotIntersectValidation() {
    }

    /**
     * Ensure Lines do not intersect.
     * 
     * <p>
     * This is supposed to go off and grab the necesary features from the
     * database using the envelope with the typeNames. But it doesn't yet.  It
     * just uses the ones passed in through parameter layers.
     * </p>
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
        ArrayList geoms = new ArrayList(); // FIDs used for lookup to see if any match
        boolean result = true;
        Iterator it = layers.values().iterator();

        while (it.hasNext()) // for each layer
         {
            SimpleFeatureSource featureSource = (SimpleFeatureSource) it.next();
            SimpleFeatureIterator features = featureSource.getFeatures().features();

            try {
                while (features.hasNext()) // for each feature
                 {
                    // check if it intersects any of the previous features
                    SimpleFeature feature = features.next();
                    Geometry geom = (Geometry) feature.getDefaultGeometry();

                    for (int i = 0; i < geoms.size(); i++) // for each existing geometry
                     {
                        // I don't trust this thing to work correctly
                        if (geom.crosses((Geometry) geoms.get(i))) {
                            results.error(feature,
                                "Lines cross when they shouldn't.");
                            result = false;
                        }
                    }

                    geoms.add(geom);
                }
            } finally {
                features.close(); // this is an important line    
            }
        }

        return result;
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
        return PRIORITY_INVOLVED;
    }
}
