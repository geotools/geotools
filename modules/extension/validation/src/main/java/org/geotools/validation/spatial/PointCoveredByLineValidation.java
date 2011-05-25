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

import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.validation.ValidationResults;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;


/**
 * PointCoveredByLineValidation purpose.
 * 
 * <p>
 * Checks to ensure the Point is covered by the Line.
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 *
 * @source $URL$
 * @version $Id$
 */
public class PointCoveredByLineValidation extends PointLineAbstractValidation {
    /**
     * PointCoveredByLineValidation constructor.
     * 
     * <p>
     * Super
     * </p>
     */
    public PointCoveredByLineValidation() {
        super();
    }

    /**
     * Ensure Point is covered by the Line.
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
        SimpleFeatureSource lineSource = (SimpleFeatureSource) layers.get(getRestrictedLineTypeRef());
        SimpleFeatureSource pointSource = (SimpleFeatureSource) layers.get(getPointTypeRef());

        Object[] points = pointSource.getFeatures().toArray();
        Object[] lines = lineSource.getFeatures().toArray();

        if (!envelope.contains(pointSource.getBounds())) {
            results.error((SimpleFeature) points[0],
                "Point Feature Source is not contained within the Envelope provided.");

            return false;
        }

        if (!envelope.contains(lineSource.getBounds())) {
            results.error((SimpleFeature) lines[0],
                "Line Feature Source is not contained within the Envelope provided.");

            return false;
        }

        for (int i = 0; i < lines.length; i++) {
            SimpleFeature tmp = (SimpleFeature) lines[i];
            Geometry gt = (Geometry) tmp.getDefaultGeometry();

            if (gt instanceof LineString) {
                LineString ls = (LineString) gt;

                boolean r = false;
                for (int j = 0; j < points.length && !r; j++) {
                    SimpleFeature tmp2 = (SimpleFeature) points[j];
                    Geometry gt2 = (Geometry) tmp2.getDefaultGeometry();

                    if (gt2 instanceof Point) {
                        Point pt = (Point) gt2;
                        if(!ls.contains(pt)){
                        	r = true;
                        }
                    }
                }
                if(!r){
                    results.error(tmp, "Line does not contained one of the specified points.");
                	return false;
                }
            }
        }

        return true;
    }
}
