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


/**
 * A ShapeHandler that reads PointHandler objects from a file. It returns a
 * SimpleGeometry and decimates all points that map to the same screen location.
 * 
 * @author jeichar
 * @since 2.1.x
 * @source $URL:
 *         http://svn.geotools.org/geotools/branches/2.2.x/ext/shaperenderer/src/org/geotools/renderer/shape/PointHandler.java $
 */
public class PointHandler extends org.geotools.renderer.shape.shapehandler.simple.PointHandler{
    private static final GeometryFactory factory=new GeometryFactory(new LiteCoordinateSequenceFactory());
    
    public PointHandler(ShapeType type, Envelope env, Rectangle screenSize, 
            MathTransform mt, boolean hasOpacity) throws TransformException {
        super(type, env, screenSize, mt, hasOpacity);
    }

    protected Object createGeometry(ShapeType type, Envelope geomBBox, double[][] transformed) {
        return factory.createPoint(new LiteCoordinateSequence(transformed[0]));
    }
    
}
