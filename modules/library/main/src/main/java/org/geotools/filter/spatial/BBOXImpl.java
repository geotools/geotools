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

import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.TopologyException;
import com.vividsolutions.jts.geom.prep.PreparedGeometry;

/**
 * 
 *
 * @source $URL$
 */
public class BBOXImpl extends AbstractPreparedGeometryFilter implements BBOX {

    double minx, miny, maxx, maxy;

    String srs;

    public BBOXImpl(Expression e1, Expression e2) {
        super(e1, e2);
        if (e1 != null)
            setExpression1(e1);
        if (e2 != null)
            setExpression2(e2);
    }

    public BBOXImpl(Expression name, double minx, double miny, double maxx, double maxy, String srs) {
        this(name,
             new LiteralExpressionImpl(boundingPolygon(new Envelope(minx, maxx, miny, maxy)))
             );
        this.srs = srs;
    }

    public BBOXImpl(Expression e1, Expression e2, MatchAction matchAction) {
        super(e1, e2, matchAction);
        if (e1 != null)
            setExpression1(e1);
        if (e2 != null)
            setExpression2(e2);
    }

    public BBOXImpl(Expression name, double minx, double miny, double maxx, double maxy,
            String srs, MatchAction matchAction) {
        this(name,
             new LiteralExpressionImpl( boundingPolygon(buildEnvelope(minx, maxx, miny, maxy, srs))),
             matchAction);
        this.srs = srs;
    }

    public String getPropertyName() {
        // BBOX filters can be also created setting the expressions directly, and some
        // old code sets the property name the other way around, try to handle this silliness
        if (getExpression1() instanceof PropertyName) {
            PropertyName propertyName = (PropertyName) getExpression1();
            return propertyName.getPropertyName();
        } else if (getExpression2() instanceof PropertyName) {
            PropertyName propertyName = (PropertyName) getExpression2();
            return propertyName.getPropertyName();
        } else {
            return null;
        }
    }

    public void setPropertyName(String propertyName) {
        setExpression1(new AttributeExpressionImpl(propertyName));
    }

    public String getSRS() {
        return srs;
    }

    /**
     * @deprecated use the constructor or setExpression2
     */
    public void setSRS(String srs) {
        this.srs = srs;
        updateExpression2();
    }

    public double getMinX() {
        return minx;
    }

    /**
     * @deprecated use the constructor or setExpression2
     */
    public void setMinX(double minx) {
        this.minx = minx;
        updateExpression2();
    }

    public double getMinY() {
        return miny;
    }

    /**
     * @deprecated use the constructor or setExpression2
     */
    public void setMinY(double miny) {
        this.miny = miny;
        updateExpression2();
    }

    public double getMaxX() {
        return maxx;
    }

    /**
     * @deprecated use the constructor or setExpression2
     */
    public void setMaxX(double maxx) {
        this.maxx = maxx;
        updateExpression2();
    }

    public double getMaxY() {
        return maxy;
    }

    /**
     * @deprecated use the constructor or setExpression2
     */
    public void setMaxY(double maxy) {
        this.maxy = maxy;
        updateExpression2();
    }

    private void updateExpression2() {
        // this is temporary until set...XY are removed
        Literal expression = 
                new LiteralExpressionImpl( boundingPolygon(buildEnvelope(minx, maxx, miny, maxy, srs)));
        super.setExpression2(expression);
    }

    @Override
    public boolean evaluateInternal(Geometry left, Geometry right) {
        switch (literals) {
        case BOTH:
            return cacheValue;
        case RIGHT: {
            return preppedEvaluate(rightPreppedGeom, left);
        }
        case LEFT: {
            return preppedEvaluate(leftPreppedGeom, right);
        }
        default: {
            return basicEvaluate(left, right);
        }
        }
    }

    protected boolean basicEvaluate(Geometry left, Geometry right) {
        Envelope envLeft = left.getEnvelopeInternal();
        Envelope envRight = right.getEnvelopeInternal();

        if (envRight.intersects(envLeft)) {
            return left.intersects(right);
        } else {
            return false;
        }

        // Note that this is a pretty permissive logic
        // if the type has somehow been mis-set (can't happen externally)
        // then true is returned in all cases
    }

    private boolean preppedEvaluate(PreparedGeometry prepped, Geometry other) {
        Envelope envLeft = prepped.getGeometry().getEnvelopeInternal();
        Envelope envRight = other.getEnvelopeInternal();

        if(envRight.intersects(envLeft)) {
            return prepped.intersects(other);
        } else {
            return false;
        }

        // Note that this is a pretty permissive logic
        // if the type has somehow been mis-set (can't happen externally)
        // then true is returned in all cases
    }

    public Object accept(FilterVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    public void setExpression1(Expression expression) {
        // BBOX filters can be also created setting the expressions directly, and some
        // old code sets the property name the other way around, try to handle this silliness
        updateMinMaxFields(expression);
        super.setExpression1(expression);
    }

    public void setExpression2(Expression expression) {
        // BBOX filters can be also created setting the expressions directly, and some
        // old code sets the property name the other way around, try to handle this silliness
        updateMinMaxFields(expression);
        super.setExpression2(expression);
    }

    private void updateMinMaxFields(Expression expression) {
        if (expression instanceof Literal) {
            Literal bbox = (Literal) expression;
            Object value = bbox.getValue();
            if (value instanceof BoundingBox) {
                BoundingBox env = (BoundingBox) value;
                minx = env.getMinX();
                maxx = env.getMaxX();
                miny = env.getMinY();
                maxy = env.getMaxY();
                srs = CRS.toSRS(env.getCoordinateReferenceSystem());
            } else {
                Envelope env = null;
                if (value instanceof Envelope) {
                    env = (Envelope) value;
                } else if (value instanceof Geometry) {
                    Geometry geom = (Geometry) value;
                    env = geom.getEnvelopeInternal();
                    if (geom.getUserData() != null) {
                        if (geom.getUserData() instanceof String) {
                            srs = (String) geom.getUserData();
                        } else if (geom.getUserData() instanceof CoordinateReferenceSystem) {
                            srs = CRS.toSRS((CoordinateReferenceSystem) geom.getUserData());
                        }
                    }
                } else {
                    env = (Envelope) bbox.evaluate(null, Envelope.class);
                }
                if (env == null)
                    return;
                minx = env.getMinX();
                maxx = env.getMaxX();
                miny = env.getMinY();
                maxy = env.getMaxY();
            }
        }
    }
    /**
     * Generate bounding polygon for provided envelope.
     * 
     * For a ReferenedEnvelope the CoordinateReferenceSystem wil be preserved.
     * 
     * @param env The envelope to set as the bounds.
     *
     * @throws IllegalFilterException If the box can not be created.
     *
     * @task Currently sets the SRID to null, which can cause problems
     *       with JTS when it comes to doing spatial tests
     */
    public static Polygon boundingPolygon( Envelope env ){
        /** Factory for creating geometries */
        GeometryFactory gfac = JTSFactoryFinder.getGeometryFactory();
        
        Coordinate[] coords = new Coordinate[5];
        coords[0] = new Coordinate(env.getMinX(), env.getMinY());
        coords[1] = new Coordinate(env.getMinX(), env.getMaxY());
        coords[2] = new Coordinate(env.getMaxX(), env.getMaxY());
        coords[3] = new Coordinate(env.getMaxX(), env.getMinY());
        coords[4] = new Coordinate(env.getMinX(), env.getMinY());

        LinearRing ring = null;

        try {
            ring = gfac.createLinearRing(coords);
        } catch (TopologyException tex) {
            throw new IllegalFilterException(tex.toString());
        }

        Polygon polygon = gfac.createPolygon(ring, null);
        if (env instanceof ReferencedEnvelope) {
            ReferencedEnvelope refEnv = (ReferencedEnvelope) env;
            polygon.setUserData(refEnv.getCoordinateReferenceSystem());
        }
        
        return polygon;
    }
    private static ReferencedEnvelope buildEnvelope(double minx, double maxx, double miny, double maxy, String srs) {
    	CoordinateReferenceSystem crs = null;
		
		if (srs != null && !("".equals(srs)))
		try {
			try {
					crs = CRS.decode(srs);
			} catch (MismatchedDimensionException e) {
					throw new RuntimeException (e);
			} catch (NoSuchAuthorityCodeException e) {
				CRS.parseWKT(srs);
			} 
		} catch (FactoryException e) {
			
		}
				
		return new ReferencedEnvelope(minx, maxx, miny, maxy, crs);
		
    }
    
    @Override
	public BoundingBox getBounds() {
        Object value = ((Literal) getExpression2()).getValue();        
		if (value instanceof BoundingBox) {
			return (BoundingBox) value;
		}		
		else {		//create one
			return buildEnvelope(minx, maxx, miny, maxy, srs);
		}
	}

}
