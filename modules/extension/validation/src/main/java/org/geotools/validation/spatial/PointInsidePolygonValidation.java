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
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;


/**
 * PointInsidePolygonValidation purpose.
 * 
 * <p>
 * Ensures a Point is inside the Polygon (not on boundary).
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @source $URL$
 * @version $Id$
 */
public class PointInsidePolygonValidation extends PointPolygonAbstractValidation {
    /**
     * PointInsidePolygonValidation constructor.
     * 
     * <p>
     * Description
     * </p>
     */
    public PointInsidePolygonValidation() {
        super();
    }

    /**
     * Ensure Point is inside the Polygon (not on boundary).
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
        SimpleFeatureSource pointSource = (SimpleFeatureSource) layers.get(getPointTypeRef());
        SimpleFeatureSource polySource = (SimpleFeatureSource) layers.get(getRestrictedPolygonTypeRef());

        Object[] polys = polySource.getFeatures().toArray();
        Object[] points = pointSource.getFeatures().toArray();

        if (!envelope.contains(polySource.getBounds())) {
            results.error((SimpleFeature) polys[0],
                "Point Feature Source is not contained within the Envelope provided.");

            return false;
        }

        if (!envelope.contains(pointSource.getBounds())) {
            results.error((SimpleFeature) points[0],
                "Line Feature Source is not contained within the Envelope provided.");

            return false;
        }

        for (int i = 0; i < points.length; i++) {
            SimpleFeature tmp = (SimpleFeature) points[i];
            Geometry gt = (Geometry) tmp.getDefaultGeometry();

            if (gt instanceof Polygon) {
            	Polygon ls = (Polygon) gt;

                boolean r = false;
                for (int j = 0; j < polys.length && !r; j++) {
                    SimpleFeature tmp2 = (SimpleFeature) polys[j];
                    Geometry gt2 = (Geometry) tmp2.getDefaultGeometry();

                    if (gt2 instanceof Point) {
                    	Point pt = (Point) gt2;
                        if(!pt.within(ls)){
                        	r = true;
                        }
                    }
                }
                if(!r){
                    results.error(tmp, "Polygon does not contained one of the specified points.");
                	return false;
                }
            }
        }

        return true;
    }
}
