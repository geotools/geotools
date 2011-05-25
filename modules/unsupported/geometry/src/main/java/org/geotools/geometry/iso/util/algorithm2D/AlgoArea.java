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
package org.geotools.geometry.iso.util.algorithm2D;

import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.geotools.geometry.iso.util.elem2D.Edge2D;

/**
 * @author roehrig
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 *
 *
 * @source $URL$
 */
public class AlgoArea {

	/**
	 * returns a list of boundaries, whereas each boundary is a list of
	 * connected Line2D segments (ArrayList&lt;ArrayList&lt;Line2D&gt;&gt;)
	 * 
	 * @param area
	 * @return
	 */
	public static ArrayList<ArrayList<Line2D>> getBoundariesLines(Area area) {
		ArrayList<ArrayList<Line2D>> result = new ArrayList<ArrayList<Line2D>>();
		PathIterator pi = area.getPathIterator(null);
		double coords[] = new double[6];
		while (!pi.isDone()) {
			int type = pi.currentSegment(coords);
			if (type == PathIterator.SEG_MOVETO) {
				ArrayList<Line2D> lines = new ArrayList<Line2D>();
				result.add(lines);
				Point2D p0 = new Point2D.Double(coords[0], coords[1]);
				Point2D pStart = p0;
				pi.next();
				while (!pi.isDone()) {
					type = pi.currentSegment(coords);
					if (type == PathIterator.SEG_CLOSE) {
						/**
						 * according to our definition of ring, the first node
						 * must be equal the last
						 */
						// Jr hier gibt es probleme: mit teresapolygon ist
						// p0==pStart, für HunderMalHundertQuadrat ist p0 der
						// punkt davor. Zwischenlösung, bis das erklärt wird:
						if (!AlgoPoint2D.equalsTol(p0, pStart)) {
							lines.add(new Line2D.Double(p0, pStart));
						}
						pi.next();
						break;
					} else if (type == PathIterator.SEG_LINETO) {
						Point2D p1 = new Point2D.Double(coords[0], coords[1]);
						lines.add(new Line2D.Double(p0, p1));
						p0 = p1;
					} else if (type == PathIterator.SEG_QUADTO) {
						Point2D p1 = new Point2D.Double(coords[0], coords[1]);
						lines.add(new Line2D.Double(p0, p1));
						Point2D p2 = new Point2D.Double(coords[2], coords[3]);
						lines.add(new Line2D.Double(p1, p2));
						p0 = p2;
					} else if (type == PathIterator.SEG_CUBICTO) {
						Point2D p1 = new Point2D.Double(coords[0], coords[1]);
						lines.add(new Line2D.Double(p0, p1));
						Point2D p2 = new Point2D.Double(coords[2], coords[3]);
						lines.add(new Line2D.Double(p1, p2));
						Point2D p3 = new Point2D.Double(coords[4], coords[5]);
						lines.add(new Line2D.Double(p2, p3));
						p0 = p3;
					} else {
						throw new IllegalArgumentException("Bad PathIterator");
					}
					pi.next();
				}
			} else {
				pi.next();
			}
		}
		return result;
	}

	public ArrayList<ArrayList<Point2D>> getBoundariesPoints(Area area) {
		ArrayList<ArrayList<Point2D>> result = new ArrayList<ArrayList<Point2D>>();
		PathIterator pi = area.getPathIterator(null);
		double startCoords[] = new double[6];
		while (!pi.isDone()) {
			int type = pi.currentSegment(startCoords);
			if (type == PathIterator.SEG_MOVETO) {
				ArrayList<Point2D> points = new ArrayList<Point2D>();
				result.add(points);
				points.add(new Point2D.Double(startCoords[0], startCoords[1]));
				pi.next();
				while (!pi.isDone()) {
					double coords[] = new double[6];
					type = pi.currentSegment(coords);
					if (type == PathIterator.SEG_CLOSE) {
						/**
						 * according to our definition of ring, the first node
						 * must be equal the last
						 */
						points.add(new Point2D.Double(startCoords[0],
								startCoords[1]));
						break;
					} else if (type == PathIterator.SEG_LINETO) {
						points.add(new Point2D.Double(coords[0], coords[1]));
					} else if (type == PathIterator.SEG_QUADTO) {
						points.add(new Point2D.Double(coords[0], coords[1]));
						points.add(new Point2D.Double(coords[2], coords[3]));
					} else if (type == PathIterator.SEG_LINETO) {
						points.add(new Point2D.Double(coords[0], coords[1]));
						points.add(new Point2D.Double(coords[2], coords[3]));
						points.add(new Point2D.Double(coords[4], coords[5]));
					} else {
						throw new IllegalArgumentException("Bad PathIterator");
					}
					pi.next();
				}
			} else {
				pi.next();
			}
		}
		return result;
	}

	public static boolean linesOrientation(Collection<Line2D> segments){
		//Algorithm for a lineString-Orientation (clockwise or counterclockwise)
        //is the value of result > 0 the Orientation of the lineString is clockwise
        //is the value of result < 0 the Orientation of the lineString is counterclockwise
		ArrayList<Point2D> points = new ArrayList<Point2D>(segments.size()+1);

		for (Iterator<Line2D> it = segments.iterator(); it.hasNext();) {
			Line2D edge = it.next();
			points.add(((Line2D)it.next()).getP1());
		}		

		return AlgoPoint2D.pointsOrientation(points).booleanValue();
	}

	public static GeneralPath createGeneralPathFromEdges(Collection<Edge2D> edges) {
		
		ArrayList<Point2D> nodes = new ArrayList<Point2D>(edges.size());
		
		for (Iterator it = edges.iterator(); it.hasNext();) {
			Edge2D edge = (Edge2D)it.next();
			nodes.add(edge.getNode1());
		}
		return createGeneralPathFromNodes(nodes);
	}
	/**
	 * @param points
	 * @return
	 */
	public static GeneralPath createGeneralPathFromNodes(Collection<Point2D> points) {
		if (points==null) return null;
		if ( points.isEmpty()) return null;
		Point2D pointsArray[] = new Point2D[points.size()];
		pointsArray = (Point2D[])points.toArray(pointsArray);
		
		int n = pointsArray.length;
		double x[] = new double[n];
		double y[] = new double[n];
		
		GeneralPath path = new GeneralPath(GeneralPath.WIND_NON_ZERO,n);
		
		Point2D p = pointsArray[0];
		path.moveTo((float)p.getX(),(float)p.getY());
		for (int i=1; i < n-1; i++) {
			p = pointsArray[i];
			path.lineTo((float)p.getX(),(float)p.getY());
		}
		if (pointsArray[0].equals(pointsArray[n-1])) {
			path.closePath();
		} else {
			p = pointsArray[n-1];
			path.lineTo((float)p.getX(),(float)p.getY());			
		}
		return path;
	}

}
