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
package org.geotools.renderer.shape.shapehandler.simple;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import org.geotools.data.shapefile.shp.ShapeHandler;
import org.geotools.data.shapefile.shp.ShapeType;
import org.geotools.renderer.ScreenMap;
import org.geotools.renderer.shape.GeometryHandlerUtilities;
import org.geotools.renderer.shape.ShapefileRenderer;
import org.geotools.renderer.shape.SimpleGeometry;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Envelope;

/**
 * A ShapeHandler that reads MultiPoint objects from a file. It returns a
 * SimpleGeometry and decimates all points that map to the same screen location.
 * 
 * @author jeichar
 * @since 2.1.x
 *
 * @source $URL$
 */
public class MultiPointHandler implements ShapeHandler {
    private ShapeType type;

    private Envelope bbox;

    private MathTransform mt;

    private ScreenMap screenMap;

    private double spanx;

    private double spany;

    Logger LOGGER = ShapefileRenderer.LOGGER;
    /**
     * Create new instance
     * 
     * @param type
     *            the type of shape.
     * @param env
     *            the area that is visible. If shape is not in area then skip.
     * @param mt
     *            the transform to go from data to the envelope (and that should
     *            be used to transform the shape coords)
     * @param hasOpacity
     */
    public MultiPointHandler(ShapeType type, Envelope env,
            Rectangle screenSize, MathTransform mt, boolean hasOpacity)
            throws TransformException {
        if (mt == null) {
            throw new NullPointerException();
        }

        this.type = type;
        this.bbox = env;
        this.mt = mt;

        screenMap = GeometryHandlerUtilities.calculateScreenSize(screenSize,
                hasOpacity);

        Point2D span = GeometryHandlerUtilities.calculateSpan(mt, 0, 0);
        this.spanx = span.getX();
        this.spany = span.getY();
    }

    /**
     * @see org.geotools.data.shapefile.shp.ShapeHandler#getShapeType()
     */
    public ShapeType getShapeType() {
        return type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geotools.data.shapefile.shp.ShapeHandler#read(java.nio.ByteBuffer,
     *      org.geotools.data.shapefile.shp.ShapeType)
     */
    public Object read(ByteBuffer buffer, ShapeType type, boolean flatGeometry) {
        if (type == ShapeType.NULL) {
            return null;
        }

        // read bounding box
        Envelope geomBBox = GeometryHandlerUtilities.readBounds(buffer);

        if (!bbox.intersects(geomBBox)) {
            return null;
        }

        boolean bboxdecimate = geomBBox.getWidth() <= spanx
                && geomBBox.getHeight() <= spany;
        int numParts = buffer.getInt();

        double[][] coords = new double[numParts][];
        double[][] transformed = new double[numParts][];

        // if bbox is less than a pixel then decimate the geometry. But
        // orientation must
        // remain the same so geometry data must be parsed.
        if (bboxdecimate) {
            coords = new double[1][];
            coords[0] = new double[2];
            transformed = new double[1][];
            transformed[0] = new double[2];
            coords[0][0] = buffer.getDouble();
            coords[0][1] = buffer.getDouble();
            try {
                mt.transform(coords[0], 0, transformed[0], 0, 1);
            } catch (Exception e) {
                ShapefileRenderer.LOGGER
                        .severe("could not transform coordinates "
                                + e.getLocalizedMessage());
                transformed[0] = coords[0];
            }
            
            if( screenMap.checkAndSet((int)transformed[0][0], (int)transformed[0][1] ) ){
                LOGGER.finest("Point already rendered" + transformed[0][0] + " "
                        + transformed[0][1]);
                return null;
            }
        } else {

            int partsInBBox = 0;
            for (int part = 0; part < numParts; part++) {
                coords[part] = new double[2];
                coords[part][0] = buffer.getDouble();
                coords[part][1] = buffer.getDouble();

                if (!bbox.contains(coords[part][0], coords[part][1]))
                    continue;

                if (!mt.isIdentity()) {
                    try {
                        transformed[partsInBBox] = new double[2];
                        mt.transform(coords[part], 0, transformed[partsInBBox],
                                0, 1);
                    } catch (Exception e) {
                        ShapefileRenderer.LOGGER
                                .severe("could not transform coordinates "
                                        + e.getLocalizedMessage());
                        transformed[partsInBBox] = coords[part];
                    }
                } else {
                    transformed[partsInBBox] = new double[2];
                    System.arraycopy(coords[part], 0, transformed[partsInBBox],
                            0, 1);
                }
                if (!screenMap.get((int) transformed[partsInBBox][0],
                        (int) transformed[partsInBBox][1]))
                    partsInBBox++;
            }
            if (partsInBBox == 0)
                return null;
            if (partsInBBox != numParts) {
                double[][] tmp = new double[partsInBBox][];
                System.arraycopy(transformed, 0, tmp, 0, partsInBBox);
                transformed = tmp;
            }
        }
        return createGeometry(type, geomBBox, transformed);
    }


    protected Object createGeometry(ShapeType type, Envelope geomBBox, double[][] transformed) {
        return new SimpleGeometry(type, transformed, geomBBox);
    }
    
    /**
     * @see org.geotools.data.shapefile.shp.ShapeHandler#write(java.nio.ByteBuffer,
     *      java.lang.Object)
     */
    public void write(ByteBuffer buffer, Object geometry) {
        // This handler doesnt write
        throw new UnsupportedOperationException(
                "This handler is only for reading");
    }

    /**
     * @see org.geotools.data.shapefile.shp.ShapeHandler#getLength(java.lang.Object)
     */
    public int getLength(Object geometry) {
        // TODO Auto-generated method stub
        return 0;
    }

}
