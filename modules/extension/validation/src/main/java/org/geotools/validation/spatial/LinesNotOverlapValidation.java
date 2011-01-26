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


/**
 * LinesNotOverlapValidation purpose.
 * 
 * <p>
 * Ensures Lines do not overlap.
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @source $URL$
 * @version $Id$
 */
public class LinesNotOverlapValidation extends LineLineAbstractValidation {
    /**
     * LinesNotOverlapValidation constructor.
     * 
     * <p>
     * Description
     * </p>
     */
    public LinesNotOverlapValidation() {
        super();

        // TODO Auto-generated constructor stub
    }

    /**
     * Ensure Lines do not overlap.
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
        SimpleFeatureSource lineSource1 = (SimpleFeatureSource) layers.get(getLineTypeRef());
        SimpleFeatureSource lineSource2 = (SimpleFeatureSource) layers.get(getRestrictedLineTypeRef());

        Object[] lines1 = lineSource1.getFeatures().toArray();
        Object[] lines2 = lineSource2.getFeatures().toArray();

        if (!envelope.contains(lineSource1.getBounds())) {
            results.error((SimpleFeature) lines1[0],
                "Point Feature Source is not contained within the Envelope provided.");

            return false;
        }

        if (!envelope.contains(lineSource2.getBounds())) {
            results.error((SimpleFeature) lines2[0],
                "Line Feature Source is not contained within the Envelope provided.");

            return false;
        }

        boolean r = true;

        for (int i = 0; i < lines2.length; i++) {
            SimpleFeature tmp = (SimpleFeature) lines2[i];
            Geometry gt = (Geometry) tmp.getDefaultGeometry();

            for (int j = 0; j < lines1.length; j++) {
                SimpleFeature tmp2 = (SimpleFeature) lines1[j];
                Geometry gt2 = (Geometry) tmp2.getDefaultGeometry();

                if (gt.overlaps(gt2)) {
                    results.error(tmp,
                        "Overlaps with another line specified. Id="
                        + tmp2.getID());
                    r = false;
                }
            }
        }

        return r;
    }
}
