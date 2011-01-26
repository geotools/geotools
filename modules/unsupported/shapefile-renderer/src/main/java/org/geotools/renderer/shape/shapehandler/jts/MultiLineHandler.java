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
package org.geotools.renderer.shape.shapehandler.jts;

import java.awt.Rectangle;

import org.geotools.data.shapefile.shp.ShapeType;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;


/**
 * Creates Geometry line objects for use by the ShapeRenderer.
 *
 * @author jeichar
 *
 * @since 2.1.x
 * @source $URL$
 */
public class MultiLineHandler extends org.geotools.renderer.shape.shapehandler.simple.MultiLineHandler {
    private static final GeometryFactory factory=new GeometryFactory(new LiteCoordinateSequenceFactory());
    
    public MultiLineHandler(ShapeType type, Envelope env,
            MathTransform mt, boolean hasOpacity, Rectangle screenSize) throws TransformException {
        super(type, env, mt, hasOpacity, screenSize);
    }
    
    protected Object createGeometry(ShapeType type, Envelope geomBBox, double[][] transformed) {
        
        LineString[] ls=new LineString[transformed.length];
        for (int i = 0; i < transformed.length; i++) {
            ls[i]=factory.createLineString(new LiteCoordinateSequence(transformed[i]));
        }
        
        return factory.createMultiLineString(ls);
    }
}
