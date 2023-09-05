/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import org.geotools.api.filter.BinaryComparisonOperator;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.FilterVisitor;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.BinarySpatialOperator;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;

/**
 * A fast envelope vs envelope bbox used in rendering operations. To be removed one we have an
 * official concept of "loose bbox" in the API
 *
 * @author aaime
 */
class FastBBOX implements BBOX, BinarySpatialOperator, BinaryComparisonOperator {

    PropertyName property;
    Envelope envelope;
    FilterFactory factory;

    public FastBBOX(PropertyName propertyName, Envelope env, FilterFactory factory) {
        this.property = propertyName;
        this.envelope = env;
        this.factory = factory;
    }

    public PropertyName getProperty() {
        return property;
    }

    public Envelope getEnvelope() {
        return envelope;
    }

    @Override
    public BoundingBox getBounds() {
        return ReferencedEnvelope.reference(envelope);
    }

    @Override
    public Expression getExpression1() {
        return property;
    }

    @Override
    public Expression getExpression2() {
        return factory.literal(envelope);
    }

    @Override
    public Object accept(FilterVisitor visitor, Object context) {
        Object result = visitor.visit(this, context);
        if (!(result instanceof BBOX)) return result;

        BBOX clone = (BBOX) result;
        if (clone.getExpression1().equals(getExpression1())
                && clone.getExpression2().equals(getExpression2()))
            return new FastBBOX(property, envelope, factory);

        return result;
    }

    @Override
    public boolean evaluate(Object feature) {
        if (feature == null) return false;

        Geometry other = property.evaluate(feature, Geometry.class);
        if (other == null) return false;

        return other.getEnvelopeInternal().intersects(envelope);
    }

    // THIS GARGABE IS HERE TO ALLOW OLD DATASTORES NOT USING PROPER OGC FILTERS TO WORK
    // WILL BE REMOVED WHEN THERE IS NOTHING LEFT USING THEM

    @Override
    public boolean isMatchingCase() {
        return false;
    }

    @Override
    public MatchAction getMatchAction() {
        return MatchAction.ANY;
    }

    @Override
    public String toString() {
        return "FastBBOX [property=" + property + ", envelope=" + envelope + "]";
    }
}
