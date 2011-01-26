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

import java.awt.geom.Point2D;
import java.nio.ByteBuffer;

import org.geotools.data.shapefile.shp.ShapeHandler;
import org.geotools.data.shapefile.shp.ShapeType;
import org.geotools.renderer.shape.GeometryHandlerUtilities;
import org.geotools.renderer.shape.ShapefileRenderer;
import org.geotools.renderer.shape.SimpleGeometry;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Envelope;

/**
 * A ShapeHandler that reads PointHandler objects from a file.  It returns a SimpleGeometry and decimates all points that
 * map to the same screen location.
 * 
 * @author jeichar
 * @since 2.1.x
 * @source $URL$
 */
public class PolygonHandler implements ShapeHandler {

	private ShapeType type;

	private Envelope bbox;

	double spanx, spany;

	private MathTransform mt;

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
	public PolygonHandler(ShapeType type, Envelope env, MathTransform mt, boolean hasOpacity)
			throws TransformException {
                if (mt == null) {
                    throw new NullPointerException();
                }
		this.type = type;
		this.bbox = env;
		this.mt = mt;
        	Point2D span = GeometryHandlerUtilities.calculateSpan(mt,0,0);
        	this.spanx = span.getX() ;
        	this.spany = span.getY() ;
	}

	/**
	 * @see org.geotools.data.shapefile.shp.ShapeHandler#getShapeType()
	 */
	public ShapeType getShapeType() {
		return type;
	}

	public Object read(ByteBuffer buffer, ShapeType type, boolean flatGeometry) {
		if (type == ShapeType.NULL) {
			return null;
		}

		Envelope geomBBox = GeometryHandlerUtilities.readBounds(buffer);

		if (!bbox.intersects(geomBBox)) {
			return null;
		}

		boolean bboxdecimate = geomBBox.getWidth() <= spanx
				&& geomBBox.getHeight() <= spany;
		int numParts = buffer.getInt();
		int numPoints = buffer.getInt(); // total number of points

		int[] partOffsets = new int[numParts];

		// points = new Coordinate[numPoints];
		for (int i = 0; i < numParts; i++) {
			partOffsets[i] = buffer.getInt();
		}
		double[][] coords = new double[numParts][];
		double[][] transformed = new double[numParts][];
		// if needed in future otherwise all references to a z are commented
		// out.
		// if( dimensions==3 )
		// z=new double[numParts][];

		int finish, start = 0;
		int length = 0;
		if (bboxdecimate) {
			coords = new double[1][];
			coords[0] = new double[4];
			transformed = new double[1][];
			transformed[0] = new double[8];
			coords[0][0] = buffer.getDouble();
			coords[0][1] = buffer.getDouble();
			try {
				mt.transform(coords[0], 0, transformed[0], 0, 1);
			} catch (Exception e) {
				ShapefileRenderer.LOGGER.severe("could not transform coordinates "
						+ e.getLocalizedMessage());
				transformed[0] = coords[0];
			}
			transformed[0][2] = transformed[0][0];
			transformed[0][3] = transformed[0][1] + 0.1;
			transformed[0][4] = transformed[0][0] + 0.1;
			transformed[0][5] = transformed[0][1] + 0.1;
			transformed[0][6] = transformed[0][0];
			transformed[0][7] = transformed[0][1];
		} else {
			Envelope partEnvelope = new Envelope();
			int partsInBBox = 0;

			for (int part = 0; part < numParts; part++) {
				start = partOffsets[part];
				partEnvelope.init();

				if (part == (numParts - 1)) {
					finish = numPoints;
				} else {
					finish = partOffsets[part + 1];
				}

				length = finish - start;
				int totalDoubles = length * 2;
				coords[part] = new double[totalDoubles];
				int readDoubles = 0;
				int currentDoubles = 0;
				for (; currentDoubles < totalDoubles;) {
					try {
						coords[part][readDoubles] = buffer.getDouble();
						readDoubles++;
						currentDoubles++;
						coords[part][readDoubles] = buffer.getDouble();
						readDoubles++;
						currentDoubles++;
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (collapsePoints(coords, part, totalDoubles, readDoubles,
							currentDoubles)) {
						readDoubles -= 2;
					} else {
						partEnvelope.expandToInclude(
								coords[part][readDoubles - 2],
								coords[part][readDoubles - 1]);
					}
				}
				if (!partEnvelope.intersects(bbox)) {
					continue;
				}

                if( readDoubles<8 )
                    transformed[partsInBBox] = new double[8];                    
                else
                    transformed[partsInBBox] = new double[readDoubles];

				if (!mt.isIdentity()) {
					try {
						GeometryHandlerUtilities.transform(type, mt, coords[part], transformed[partsInBBox], readDoubles / 2);
//						mt.transform(coords[part], 0, transformed[partsInBBox],
//								0, readDoubles / 2);
					} catch (Exception e) {
						ShapefileRenderer.LOGGER
								.severe("could not transform coordinates "
										+ e.getLocalizedMessage());
						transformed[partsInBBox] = coords[part];
					}
				} else {
					System.arraycopy(coords[part], 0, transformed[partsInBBox],
							0, readDoubles / 2);
				}
				if( readDoubles<8 ){
				    for(int i=readDoubles; i<transformed[partsInBBox].length;i++){
				        transformed[partsInBBox][i]=transformed[partsInBBox][i-2];
                    }
                }
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
	 * Return true if the current point and the last point should be collapsed
	 * into a single point. The first and last point must be the same so if the
	 * current doubles is the 1-2 second double or if one of the last two the it
	 * will return false. Otherwise it will return true if the distance in the y
	 * axis is less than the distance of one pixel... similar comparison is done
	 * along the x-axis.
	 * 
	 * @return true if the current point and the last point should be collapsed
	 *         into a single point.
	 */
	private boolean collapsePoints(double[][] coords, int part,
			int totalDoubles, int readDoubles, int currentDoubles) {
		return currentDoubles > 3
				&& currentDoubles < totalDoubles - 1
				&& Math.abs(coords[part][readDoubles - 4]
						- coords[part][readDoubles - 2]) <= spanx
				&& Math.abs(coords[part][readDoubles - 3]
						- coords[part][readDoubles - 1]) <= spany;
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
