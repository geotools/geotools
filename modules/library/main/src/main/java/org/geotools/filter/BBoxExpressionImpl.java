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
package org.geotools.filter;


import org.geotools.geometry.jts.ReferencedEnvelope;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.TopologyException;


/**
 * Implements a Bounding Box expression.
 * <p>
 * Please note this is exactly the same as doing:
 * <code>
 * filterFactory.literal( JTS.toGeometry( bounds ) );
 * </code>
 * 
 * @author Ian Turton, CCG
 *
 * @source $URL$
 * @version $Id$
 */
public class BBoxExpressionImpl
    extends org.geotools.filter.LiteralExpressionImpl implements BBoxExpression {
    /** Factory for creating geometries */
    private GeometryFactory gfac = new GeometryFactory();

    /**
     * Creates a new instance of BBoxExpression
     *
     * @throws IllegalFilterException if there are problems.
     */
    protected BBoxExpressionImpl() throws IllegalFilterException {
        this(new Envelope());
    }

    /**
     * Creates a new instance of BBoxExpression, with an initial box.
     *
     * @param env the envelope to set as the box.
     *
     * @throws IllegalFilterException if there are problems.
     */
    protected BBoxExpressionImpl(Envelope env) throws IllegalFilterException {
        expressionType = DefaultExpression.LITERAL_GEOMETRY;
        setBounds(env);
        
    }

    /**
     * Set the bbox for this expression
     *
     * @param env The envelope to set as the bounds.
     *
     * @throws IllegalFilterException If the box can not be created.
     *
     * @task HACK: currently sets the SRID to null, which can cause problems
     *       with JTS when it comes to doing spatial tests
     */
    public final void setBounds(Envelope env) throws IllegalFilterException {
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
        super.setValue(polygon);
    }
}
