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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.validation.ValidationResults;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.opengis.feature.simple.SimpleFeature;

/**
 * LineIntersectsLineWithNodeValidation purpose.
 *
 * <p>Ensures Line crosses the other Line at a node.
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id$
 */
public class LineIntersectsLineWithNodeValidation extends LineLineAbstractValidation {
    /**
     * LineIntersectsLineWithNodeValidation constructor.
     *
     * <p>Description
     */
    public LineIntersectsLineWithNodeValidation() {
        super();
    }

    /**
     * Ensure Line crosses the other Line at a node.
     *
     * <p>
     *
     * @param layers a HashMap of key="TypeName" value="FeatureSource"
     * @param envelope The bounding box of modified features
     * @param results Storage for the error and warning messages
     * @return True if no features intersect. If they do then the validation failed.
     * @see org.geotools.validation.IntegrityValidation#validate(java.util.Map,
     *     org.locationtech.jts.geom.Envelope, org.geotools.validation.ValidationResults)
     */
    public boolean validate(Map layers, Envelope envelope, ValidationResults results)
            throws Exception {
        boolean r = true;

        SimpleFeatureSource fsLine = (SimpleFeatureSource) layers.get(getLineTypeRef());

        SimpleFeatureCollection fcLine = fsLine.getFeatures();
        try (SimpleFeatureIterator fLine = fcLine.features()) {

            SimpleFeatureSource fsRLine =
                    (SimpleFeatureSource) layers.get(getRestrictedLineTypeRef());

            ListFeatureCollection fcRLine = new ListFeatureCollection(fsRLine.getFeatures());

            while (fLine.hasNext()) {
                SimpleFeature line = fLine.next();
                try (SimpleFeatureIterator fRLine = fcRLine.features()) {
                    Geometry lineGeom = (Geometry) line.getDefaultGeometry();
                    if (envelope.contains(lineGeom.getEnvelopeInternal())) {
                        // 	check for valid comparison
                        if (LineString.class.isAssignableFrom(lineGeom.getClass())) {
                            while (fRLine.hasNext()) {
                                SimpleFeature rLine = fRLine.next();
                                Geometry rLineGeom = (Geometry) rLine.getDefaultGeometry();
                                if (envelope.contains(rLineGeom.getEnvelopeInternal())) {
                                    if (LineString.class.isAssignableFrom(rLineGeom.getClass())) {
                                        if (lineGeom.intersects(rLineGeom)) {
                                            if (!hasPair(
                                                    ((LineString) lineGeom).getCoordinateSequence(),
                                                    ((LineString) rLineGeom)
                                                            .getCoordinateSequence())) {
                                                results.error(
                                                        rLine,
                                                        "Line does not intersect line at node covered by the specified Line.");
                                                r = false;
                                            }
                                        } else {
                                            results.warning(
                                                    rLine, "Does not intersect the LineString");
                                        }
                                        // do next.
                                    } else {
                                        fcRLine.remove(rLine);
                                        results.warning(
                                                rLine,
                                                "Invalid type: this feature is not a derivative of a LineString");
                                    }
                                } else {
                                    fcRLine.remove(rLine);
                                }
                            }
                        } else {
                            results.warning(
                                    line,
                                    "Invalid type: this feature is not a derivative of a LineString");
                        }
                    }
                }
            }
        }
        return r;
    }

    /**
     * hasPair purpose.
     *
     * <p>finds a pair of points, assumes the sequence is sorted smallest to largest.
     */
    private boolean hasPair(CoordinateSequence a1, CoordinateSequence a2) {
        int i = 0;
        CoordinateSequence c;
        c = a1;
        Set m = new HashSet();
        while (i < c.size()) {
            m.add(c.getCoordinate(i));
            i++;
        }
        i = 0;
        c = a2;
        while (i < c.size()) {
            if (!m.add(c.getCoordinate(i))) return true;
            i++;
        }
        return false;
    }
}
