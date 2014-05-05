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
package org.geotools.renderer.lite;

import org.geotools.filter.IllegalFilterException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.geometry.BoundingBox;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.TopologyException;

/**
 * A fast envelope vs envelope bbox used in rendering operations.
 * To be removed one we have an official concept of "loose bbox" in the API
 * @author aaime
 *
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

    public double getMaxX() {
        return envelope.getMaxX();
    }

    public double getMaxY() {
        return envelope.getMaxY();
    }

    public double getMinX() {
        return envelope.getMinX();
    }

    public double getMinY() {
        return envelope.getMinY();
    }
    
    public PropertyName getProperty() {
        return property;
    }

    public String getPropertyName() {
        return property.getPropertyName();
    }

    public String getSRS() {
        return null;
    }
    
    public Envelope getEnvelope() {
        return envelope;
    }
    
    public BoundingBox getBounds() {
        return ReferencedEnvelope.reference(envelope);
    }

    public Expression getExpression1() {
        return property;
    }

    public Expression getExpression2() {
        Coordinate[] coords = new Coordinate[5];
        coords[0] = new Coordinate(envelope.getMinX(), envelope.getMinY());
        coords[1] = new Coordinate(envelope.getMinX(), envelope.getMaxY());
        coords[2] = new Coordinate(envelope.getMaxX(), envelope.getMaxY());
        coords[3] = new Coordinate(envelope.getMaxX(), envelope.getMinY());
        coords[4] = new Coordinate(envelope.getMinX(), envelope.getMinY());

        LinearRing ring = null;

        GeometryFactory gfac = new GeometryFactory();
        try {
            ring = gfac.createLinearRing(coords);
        } catch (TopologyException tex) {
            throw new IllegalFilterException(tex.toString());
        }

        Polygon polygon = gfac.createPolygon(ring, null);
        if (envelope instanceof ReferencedEnvelope) {
            ReferencedEnvelope refEnv = (ReferencedEnvelope) envelope;
            polygon.setUserData(refEnv.getCoordinateReferenceSystem());
        }
        
        return factory.literal(polygon);
    }

    public Object accept(FilterVisitor visitor, Object context) {
        Object result = visitor.visit(this, context);
        if(!(result instanceof BBOX))
            return result;
        
        BBOX clone = (BBOX) result;
        if(clone.getExpression1().equals(getExpression1()) && clone.getExpression2().equals(getExpression2())) 
            return new FastBBOX(property, envelope, factory);
        
        return result;
    }

    public boolean evaluate(Object feature) {
        SimpleFeature sf = (SimpleFeature) feature;
        if(feature == null)
            return false;
        
        Geometry other = (Geometry) property.evaluate(sf);
        if(other == null)
            return false;
        
        return other.getEnvelopeInternal().intersects(envelope); 
    }
    
    // THIS GARGABE IS HERE TO ALLOW OLD DATASTORES NOT USING PROPER OGC FILTERS TO WORK
    // WILL BE REMOVED WHEN THERE IS NOTHING LEFT USING THEM

    public boolean isMatchingCase() {
        return false;
    }

    public MatchAction getMatchAction() {
        return MatchAction.ANY;
    }

    @Override
    public String toString() {
        return "FastBBOX [property=" + property + ", envelope=" + envelope + "]";
    }
    
    

}
