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

import org.geotools.data.shapefile.shp.ShapeType;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;


/**
 * A ShapeHandler that reads PointHandler objects from a file.  It returns a SimpleGeometry and decimates all points that
 * map to the same screen location.
 * 
 * @author jeichar
 * @since 2.1.x
 *
 * @source $URL$
 */
public class PolygonHandler extends org.geotools.renderer.shape.shapehandler.simple.PolygonHandler{
    private static final LinearRing[] HOLES=new LinearRing[0];
    private static final GeometryFactory factory=new GeometryFactory(new LiteCoordinateSequenceFactory());
    public PolygonHandler(ShapeType type, Envelope env, 
            MathTransform mt, boolean hasOpacity) throws TransformException {
        super(type, env, mt, hasOpacity);
    }

    
    protected Object createGeometry(ShapeType type, Envelope geomBBox, double[][] transformed) {
        Polygon[] poly=new Polygon[transformed.length];
        for (int i = 0; i < transformed.length; i++) {
            LinearRing ring = factory.createLinearRing(new LiteCoordinateSequence(transformed[i]));
            poly[i]=factory.createPolygon(ring,HOLES);
        }
        
        return factory.createMultiPolygon(poly);
    }
    
    private static class PolygonCoodinateSequence implements CoordinateSequence{

        final double[] coords;
        volatile Coordinate[] array;
        public PolygonCoodinateSequence(double[] ds) {
            this.coords=ds;
        }

        public Envelope expandEnvelope(Envelope env) {
            for (int i = 0; i < coords.length; i+=2) {
                env.expandToInclude(coords[i], coords[i+1]);
            }
            return env;
        }

        public Coordinate getCoordinate(int i) {
            int offset = i*2;
            return new Coordinate(coords[offset], coords[offset+1]);
        }

        public void getCoordinate(int index, Coordinate coord) {
            int offset = index*2;
            coord.x=coords[offset];
            coord.y=coords[offset+1];
        }

        public Coordinate getCoordinateCopy(int i) {
            return getCoordinate(i);
        }

        public int getDimension() {
            return 2;
        }

        public double getOrdinate(int index, int ordinateIndex) {
            return coords[index*2+ordinateIndex];
        }

        public double getX(int index) {
            return coords[index*2];
        }

        public double getY(int index) {
            return coords[index*2+1];
        }

        public void setOrdinate(int index, int ordinateIndex, double value) {
            throw new UnsupportedOperationException();
        }

        public int size() {
            return coords.length/2;
        }

        public Coordinate[] toCoordinateArray() {
            if( array==null ){
                synchronized (this) {
                    if( array==null ){
                        array=new Coordinate[size()];
                        for (int i = 0; i < array.length*2; i+=2) {
                            array[i/2]=new Coordinate(coords[i], coords[i+1]);
                        }
                    }
                }
            }
            return array;
        }
        public Object clone() {
            PolygonCoodinateSequence other;
            try {
                other = (PolygonCoodinateSequence) super.clone();
                return other;
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }

    }
}
