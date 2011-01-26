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
 * Creates Geometry line objects for use by the ShapeRenderer.
 *
 * @author jeichar
 *
 * @since 2.1.x
 * @source $URL$
 */
public class MultiLineHandler implements ShapeHandler {
    static final int TOP = 0x8;
    static final int BOTTOM = 0x4;
    static final int RIGHT = 0x2;
    static final int LEFT = 0x1;
    private ShapeType type;
    private Envelope bbox;
    private MathTransform mt;
    ScreenMap screenMap;
    private Point2D span;

    /**
     * Create new instance
     *
     * @param type the type of shape.
     * @param env the area that is visible. If shape is not in area then skip.
     * @param mt the transform to go from data to the envelope (and that should
     *        be used to transform the shape coords)
     * @param hasOpacity
     * @param screenSize 
     *
     * @throws TransformException
     */
    public MultiLineHandler(ShapeType type, Envelope env, MathTransform mt,
        boolean hasOpacity, Rectangle screenSize) throws TransformException {
        if (mt == null) {
            throw new NullPointerException();
        }
        this.type = type;
        this.bbox = env;
        this.mt = mt;
        screenMap = GeometryHandlerUtilities.calculateScreenSize(screenSize,
                hasOpacity);
        span = GeometryHandlerUtilities.calculateSpan(mt,0,0);

    }

    /**
     * @see org.geotools.data.shapefile.shp.ShapeHandler#getShapeType()
     */
    public ShapeType getShapeType() {
        return type;
    }

    /**
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

        boolean bboxdecimate = (geomBBox.getWidth() <= span.getX())
            && (geomBBox.getHeight() <= span.getY());
        int numParts = buffer.getInt();
        int numPoints = buffer.getInt(); // total number of points

        int[] partOffsets = new int[numParts];

        for (int i = 0; i < numParts; i++) {
            partOffsets[i] = buffer.getInt();
        }

        double[][] coords = new double[numParts][];
        double[][] transformed = new double[numParts][];

        int finish;
        int start = 0;
        int length = 0;

        // if bbox is less than a pixel then decimate the geometry.  But orientation must
        // remain the same so geometry data must be parsed.
        if (bboxdecimate) {
            coords = new double[1][];
            coords[0] = new double[4];
            transformed = new double[1][];
            transformed[0] = new double[4];
            coords[0][0] = buffer.getDouble();
            coords[0][1] = buffer.getDouble();
            buffer.position((buffer.position() + ((numPoints - 2) * 16)));
            coords[0][2] = buffer.getDouble();
            coords[0][3] = buffer.getDouble();

            if (!bbox.contains(coords[0][0], coords[0][1])
                    && !bbox.contains(coords[0][2], coords[0][3])) {
                return null;
            }

            try {
                mt.transform(coords[0], 0, transformed[0], 0, 1);
                
                if (screenMap.checkAndSet((int) transformed[0][0], (int) transformed[0][1])) {
                    return null;
                }

                mt.transform(coords[0], 2, transformed[0], 2, 1);

                double minx = (int) transformed[0][0];
                double miny = (int) transformed[0][1];
                double maxx = minx + 1;
                double maxy = transformed[0][3];
                transformed[0][0] = minx;
                transformed[0][1] = miny;
                transformed[0][2] = maxx;
                transformed[0][3] = maxy;
            } catch (Exception e) {
                ShapefileRenderer.LOGGER.severe(
                    "could not transform coordinates "
                    + e.getLocalizedMessage());
                transformed[0] = coords[0];
            }
        } else {
            boolean intersection = false;
            int partsInBBox = 0;

            for (int part = 0; part < numParts; part++) {
                intersection = false;
                start = partOffsets[part];

                if (part == (numParts - 1)) {
                    finish = numPoints;
                } else {
                    finish = partOffsets[part + 1];
                }

                length = finish - start;
                coords[part] = new double[length * 2];

                int readDoubles = 0;
                int currentDoubles = 0;
                int totalDoubles = length * 2;

                while (currentDoubles < totalDoubles) {
                    coords[part][readDoubles] = buffer.getDouble();
                    readDoubles++;
                    currentDoubles++;
                    coords[part][readDoubles] = buffer.getDouble();
                    readDoubles++;
                    currentDoubles++;
                    intersection = bboxIntersectSegment(intersection,
                            coords[part], readDoubles);

                    if ((readDoubles > 3)
                            && (currentDoubles < (totalDoubles - 1))) {
                        if ((Math.abs(coords[part][readDoubles - 4]
                                    - coords[part][readDoubles - 2]) <= span
                                .getX())
                                && (Math.abs(coords[part][readDoubles - 3]
                                    - coords[part][readDoubles - 1]) <= span
                                .getY())) {
                            readDoubles -= 2;
                        } 
                    }
                }

                if (!intersection) {
                    continue;
                }

                if (!mt.isIdentity()) {
                    try {
                        transformed[partsInBBox] = new double[readDoubles];
                        GeometryHandlerUtilities.transform(type, mt,
                            coords[part], transformed[partsInBBox], readDoubles / 2);
                    } catch (Exception e) {
                        ShapefileRenderer.LOGGER.severe(
                            "could not transform coordinates "
                            + e.getLocalizedMessage());
                        transformed[partsInBBox] = coords[part];
                    }
                } else {
                    transformed[partsInBBox] = new double[readDoubles];
                    System.arraycopy(coords[part], 0, transformed[partsInBBox],
                        0, readDoubles / 2);
                }

                partsInBBox++;
            }

            if (partsInBBox == 0) {
                return null;
            }

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

    public boolean bboxIntersectSegment(boolean intersection, double[] coords,
        int index) {
        if (intersection) {
            return true;
        }

        if (bbox.contains(coords[index - 2], coords[index - 1])) {
            return true;
        }

        if (index < 4) {
            return false;
        }

        return intersect(coords[index - 4],
                    coords[index - 3], coords[index - 2],
                    coords[index - 1], bbox.getMinX(), bbox.getMinY(),
                    bbox.getMaxX(), bbox.getMaxY());
    }

    /**
     * Uses Cohen Sutherland line clipping algorithm to determine intersection.
     * See  Computer Graphics: Principles and Practice Foley, van Dam,
     * Feiner, Hughes
     *
     * @param x0 DOCUMENT ME!
     * @param y0 DOCUMENT ME!
     * @param x1 DOCUMENT ME!
     * @param y1 DOCUMENT ME!
     * @param xmin DOCUMENT ME!
     * @param ymin DOCUMENT ME!
     * @param xmax DOCUMENT ME!
     * @param ymax DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean intersect(double x0, double y0, double x1, double y1,
        double xmin, double ymin, double xmax, double ymax) {
        boolean accept = false;
        boolean done = false;
        int outcode0 = compOutCode(x0, y0, xmin, xmax, ymin, ymax);
        int outcode1 = compOutCode(x1, y1, xmin, xmax, ymin, ymax);

        do {
            if ((outcode0 == 0) || (outcode1 == 0)) { // trivial accept and exit
                accept = done = true;
            } else if ((outcode0 & outcode1) != 0) { // logical and is true so trivial reject and exit
                done = true;
            } else {
                // failed both tests so calculate the line segment to clip:
                //from an otside poin to the intersectin with clip edge.
                double x;

                // failed both tests so calculate the line segment to clip:
                //from an otside poin to the intersectin with clip edge.
                double y;

                //at least one endpoint is outside the clip rectangle so pick it.
                int outcodeOut = (outcode0 > 0) ? outcode0 : outcode1;

                //Now find intersection point 
                if ((outcodeOut & TOP) > 0) {
                    x = x0 + (((x1 - x0) * (ymax - y0)) / (y1 - y0));
                    y = ymax;
                } else if ((outcodeOut & BOTTOM) > 0) {
                    x = x0 + (((x1 - x0) * (ymin - y0)) / (y1 - y0));
                    y = ymin;
                } else if ((outcodeOut & RIGHT) > 0) {
                    y = y0 + (((y1 - y0) * (xmax - x0)) / (x1 - x0));
                    x = xmax;
                } else {
                    y = y0 + (((y1 - y0) * (xmin - x0)) / (x1 - x0));
                    x = xmin;
                }

                if (outcodeOut == outcode0) {
                    x0 = x;
                    y0 = y;
                    outcode0 = compOutCode(x0, y0, xmin, xmax, ymin, ymax);
                } else {
                    x1 = x;
                    y1 = y;
                    outcode1 = compOutCode(x1, y1, xmin, xmax, ymin, ymax);
                }
            }
        } while (done == false);

        return accept;
    }

    private static int compOutCode(double x, double y, double xmin, double xmax,
        double ymin, double ymax) {
        int outcode = 0;

        if (y > ymax) {
            outcode |= TOP;
        }

        if (y < ymin) {
            outcode |= BOTTOM;
        }

        if (x > xmax) {
            outcode |= RIGHT;
        }

        if (x < xmin) {
            outcode |= LEFT;
        }

        return outcode;
    }

    //	/**
    //	 * @return
    //	 */
    //	private SimpleGeometry decimateBasedOnEnvelope(Envelope geomBBox) {
    //		if (geomBBox.getWidth() <= span.getX() && geomBBox.getHeight() <= span.getY()) {
    //			double[][] coords = new double[1][];
    //			coords[0] = new double[] { geomBBox.getMinX(), geomBBox.getMinY() };
    //			double[][] transformed = new double[1][];
    //			transformed[0] = new double[4];
    //			try {
    //				mt.transform(coords[0], 0, transformed[0], 0, 1);
    //			} catch (Exception e) {
    //				ShapefileRenderer.LOGGER.severe("could not transform coordinates "
    //						+ e.getLocalizedMessage());
    //				transformed = coords;
    //			}
    //			transformed[0][2]=transformed[0][0];
    //			transformed[0][3]=transformed[0][1];
    //			return new SimpleGeometry(type, transformed, geomBBox);
    //		}
    //		return null;
    //	}

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
