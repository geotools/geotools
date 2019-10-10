/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.visitor;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.spatial.BBOX;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Visit the BBOX filter elements and make sure they are valid.
 *
 * <p>Any BBOX filter using a literal geometry will be changed to be a literal envelope based on the
 * geometry internal envelope. If a max bounding box has been provided it will be used to clip this
 * request envelope.
 *
 * <p>
 *
 * @author Jody
 */
public class FixBBOXFilterVisitor extends DuplicatingFilterVisitor {

    // private static final Logger logger = Logging.getLogger(FixBBOXFilterVisitor.class);

    /**
     * Represents a hard limit; requests outside of this bound are assumed to be invalid for the WFS
     * resulting in an exception being thrown.
     */
    ReferencedEnvelope maxbbox;

    /**
     * Visitor used to "clean up" any BBOX expressions.
     *
     * @param maxbbox Max bounding box used to clip any BBox expressions to ensure they are vaild
     */
    public FixBBOXFilterVisitor(ReferencedEnvelope maxbbox) {
        this.maxbbox = maxbbox;
    }

    @Override
    public Object visit(BBOX filter, Object extraData) {
        Envelope bbox = null;
        Expression leftGeometry = filter.getExpression1();
        Expression rightGeometry = filter.getExpression2();
        boolean clipped = false;

        Literal le = null;
        if (leftGeometry != null && leftGeometry instanceof Literal) {
            le = (Literal) leftGeometry;
        } else if (rightGeometry != null && rightGeometry instanceof Literal) {
            le = (Literal) rightGeometry;
        }
        CoordinateReferenceSystem crs = null;
        if (le != null && le.getValue() != null && le.getValue() instanceof Geometry) {
            Geometry geometry = (Geometry) le.getValue();
            bbox = geometry.getEnvelopeInternal();
            crs = (CoordinateReferenceSystem) geometry.getUserData();
        } else if (le != null
                && le.getValue() != null
                && le.getValue() instanceof ReferencedEnvelope) {
            bbox = (Envelope) le.getValue();
            crs = ((ReferencedEnvelope) le.getValue()).getCoordinateReferenceSystem();
        }
        if (bbox == null) {
            // huh? no literal? Allow super class to sort it out ...
            return super.visit(filter, extraData); // allow super class to make a direct copy
        }

        double minx = bbox.getMinX();
        double miny = bbox.getMinY();
        double maxx = bbox.getMaxX();
        double maxy = bbox.getMaxY();
        if (maxbbox != null) {
            if (minx < maxbbox.getMinX()) {
                minx = maxbbox.getMinX();
                clipped = true;
            }
            if (maxx > maxbbox.getMaxX()) {
                maxx = maxbbox.getMaxX();
                clipped = true;
            }
            if (miny < maxbbox.getMinY()) {
                miny = maxbbox.getMinY();
                clipped = true;
            }
            if (maxy > maxbbox.getMaxY()) {
                maxy = maxbbox.getMaxY();
                clipped = true;
            }
        }
        if (clipped) {
            // the bbox was clipped!
            FilterFactory2 ff = getFactory(extraData);
            return ff.bbox(
                    filter.getExpression1(),
                    ff.literal(new ReferencedEnvelope(minx, maxx, miny, maxy, crs)));
        } else {
            return super.visit(filter, extraData); // allow super class to make a direct copy
        }
    }
}
