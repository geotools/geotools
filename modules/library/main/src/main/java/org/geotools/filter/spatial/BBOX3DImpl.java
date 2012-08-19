/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.filter.spatial;

import org.geotools.filter.IllegalFilterException;
import org.geotools.geometry.jts.ReferencedEnvelope3D;
import org.geotools.referencing.CRS;
import org.geotools.util.Converters;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX3D;
import org.opengis.geometry.BoundingBox3D;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.TopologyException;

/**
 * 
 * A 3D BBOX Filter Implementation
 * Supports filtering with BBOXes that have 3D coordinates including a minimum and maximum for the z-axis.
 * 
 * @author Niels Charlier
 *
 */

public class BBOX3DImpl implements BBOX3D {
    
    PropertyName property;
    ReferencedEnvelope3D envelope; 
    FilterFactory factory;
    
    public BBOX3DImpl(PropertyName propertyName, ReferencedEnvelope3D env, FilterFactory factory) {
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
    
    public double getMinZ() {
        return envelope.getMinX();
    }

    public double getMaxZ() {
        return envelope.getMaxZ();
    }
    
    public PropertyName getProperty() {
        return property;
    }

    public String getPropertyName() {
        return property.getPropertyName();
    }

    public String getSRS() {
        return CRS.toSRS( envelope.getCoordinateReferenceSystem() );
    }
    
    public BoundingBox3D getBounds() {
        return envelope;
    }

    public Expression getExpression1() {
        return property;
    }

    public Expression getExpression2() {
    	// in this case, the 3D BBOX falls back to regular 2D bbox behaviour (until there is more support for 3D geometries)
    	// 3DBBOX must be run as a post-filter in order to support the third coordinate.
    	
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
        if (envelope instanceof ReferencedEnvelope3D) {
            ReferencedEnvelope3D refEnv = (ReferencedEnvelope3D) envelope;
            polygon.setUserData(refEnv.getCoordinateReferenceSystem());
        }
        
        return factory.literal(polygon);
    }

    public Object accept(FilterVisitor visitor, Object context) {
        return visitor.visit(this, context);     
    }
    
    public ReferencedEnvelope3D get3DEnvelope(Geometry geom) {
    	Coordinate[] coordinates = geom.getCoordinates();
    	
    	ReferencedEnvelope3D env = new ReferencedEnvelope3D();
    	for (Coordinate coordinate : coordinates) {
    		env.expandToInclude(coordinate);
    	}
    	return env;
    }

    public boolean evaluate(Object feature) {
        
        Geometry other = Converters.convert(property.evaluate(feature), Geometry.class );
        if(other == null)
            return false;
        
        return get3DEnvelope(other).intersects(envelope); 
    }
    
    // THIS GARGABE IS HERE TO ALLOW OLD DATASTORES NOT USING PROPER OGC FILTERS TO WORK
    // WILL BE REMOVED WHEN THERE IS NOTHING LEFT USING THEM

    public boolean isMatchingCase() {
        return false;
    }

    public boolean contains(SimpleFeature feature) {
        return evaluate((Object) feature);
    }

    public boolean evaluate(SimpleFeature feature) {
        return evaluate((Object) feature);
    }

    public MatchAction getMatchAction() {
        return MatchAction.ANY;
    }

    @Override
    public String toString() {
        return "BBOX3D [property=" + property + ", envelope=" + envelope + "]";
    }
    
    

}
