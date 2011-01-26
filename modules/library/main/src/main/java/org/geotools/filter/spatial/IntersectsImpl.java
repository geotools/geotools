/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.spatial;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.spatial.Intersects;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

public class IntersectsImpl extends AbstractPreparedGeometryFilter implements
        Intersects {

    public IntersectsImpl(org.opengis.filter.FilterFactory factory,
            Expression e1, Expression e2) {
        super(factory, e1, e2);

        // backwards compat with type system
        this.filterType = GEOMETRY_INTERSECTS;
    }

    public boolean evaluate(Object feature) {
        if (feature instanceof SimpleFeature
                && !validate((SimpleFeature) feature)) {
            // we could not obtain a geometry for both left and right hand sides
            // so default to false
            return false;
        }
        Geometry left;
        Geometry right;

        switch (literals) {
        case BOTH:
            return cacheValue;
        case RIGHT: {
            return rightPreppedGeom.intersects(getLeftGeometry(feature));
        }
        case LEFT: {
            return leftPreppedGeom.intersects(getRightGeometry(feature));
        }
        default: {
            left = getLeftGeometry(feature);
            right = getRightGeometry(feature);
            return basicEvaluate(left, right);
        }
        }
    }

    public Object accept(FilterVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    protected final boolean basicEvaluate(Geometry left, Geometry right) {
        Envelope envLeft = left.getEnvelopeInternal();
        Envelope envRight = right.getEnvelopeInternal();
        return envRight.intersects(envLeft) && left.intersects(right);
    }

}