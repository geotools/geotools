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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * A ShapeHandler that reads MultiPoint objects from a file. It returns a
 * SimpleGeometry and decimates all points that map to the same screen location.
 * 
 * @author jeichar
 * @since 2.1.x
 *
 * @source $URL$
 */
public class MultiPointHandler extends org.geotools.renderer.shape.shapehandler.simple.MultiPointHandler{

    private static final GeometryFactory factory=new GeometryFactory(new LiteCoordinateSequenceFactory());
    
    public MultiPointHandler(ShapeType type, Envelope env, Rectangle screenSize, 
            MathTransform mt, boolean hasOpacity) throws TransformException {
        super(type, env, screenSize, mt, hasOpacity);
    }
    
    protected Object createGeometry(ShapeType type, Envelope geomBBox, double[][] transformed) {
        double[] coords = new double[transformed.length * 2];
        for (int i = 0; i < transformed.length; i++) {
            coords[i * 2] = transformed[i][0];
            coords[i * 2 + 1] = transformed[i][1];
        }
        return factory.createMultiPoint(new LiteCoordinateSequence(coords));
    }

    private static class MultiPointCoodinateSequence implements CoordinateSequence{

        final double[][] coords;
        volatile Coordinate[] array;
        public MultiPointCoodinateSequence(double[][] ds) {
            this.coords=ds;
        }

        public Envelope expandEnvelope(Envelope env) {
            for (int i = 0; i < coords.length; i++) {
                env.expandToInclude(coords[i][0], coords[i][1]);
            }
            return env;
        }

        public Object clone() {
            MultiPointCoodinateSequence other;
            try {
                other = (MultiPointCoodinateSequence) super.clone();
                return other;
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }

        public Coordinate getCoordinate(int i) {
            return new Coordinate(coords[i][0], coords[i][1]);
        }

        public void getCoordinate(int index, Coordinate coord) {
            coord.x=coords[index][0];
            coord.y=coords[index][1];
        }

        public Coordinate getCoordinateCopy(int i) {
            return new Coordinate(coords[i][0], coords[i][1]);
        }

        public int getDimension() {
            return 2;
        }

        public double getOrdinate(int index, int ordinateIndex) {
            return coords[index][ordinateIndex];
        }

        public double getX(int index) {
            return coords[index][0];
        }

        public double getY(int index) {
            return coords[index][1];
        }

        public void setOrdinate(int index, int ordinateIndex, double value) {
            throw new UnsupportedOperationException();
        }

        public int size() {
            return coords.length;
        }

        public Coordinate[] toCoordinateArray() {
            if( array==null ){
                synchronized (this) {
                    if( array==null ){
                        array=new Coordinate[size()];
                        for (int i = 0; i < array.length; i++) {
                            array[i]=new Coordinate(coords[i][0], coords[i][1]);
                        }
                    }
                }
            }
            return array;
        }

    }

}
