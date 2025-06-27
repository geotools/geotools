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
package org.geotools.graph.build.line;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineSegment;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.Point;

/**
 * Builds a graph representing a line network in which edges in the network are represented by LineString geometries.
 * This implementation is a wrapper around a LineGraphGenerator which sets underlying edge objects to be LineString
 * objects, and underlying Node objects to be Point objects. While generating the graph, the generator uses the visited
 * flag of created components to determine when to create underlying objects. For this reason it is not recommended to
 * modify the visited flag of any graph components.
 *
 * @see org.locationtech.jts.geom.LineString
 * @see org.locationtech.jts.geom.Point
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 * @author Anders Bakkevold, Bouvet AS, bakkedev@gmail.com
 */
public class LineStringGraphGenerator extends BasicLineGraphGenerator {

    private static GeometryFactory gf = new GeometryFactory();

    public LineStringGraphGenerator(double tolerance) {
        super(tolerance);
    }

    public LineStringGraphGenerator() {}

    @Override
    public Graphable add(Object obj) {
        LineString ls = null;
        if (obj instanceof MultiLineString) {
            ls = (LineString) ((MultiLineString) obj).getGeometryN(0);
        } else {
            ls = (LineString) obj;
        }

        // parent class expects a line segment
        Edge e = (Edge) super.add(new LineSegment(ls.getCoordinateN(0), ls.getCoordinateN(ls.getNumPoints() - 1)));
        // check if the LineSegment has been changed
        if (useTolerance()) {
            LineSegment lineSegment = (LineSegment) e.getObject();
            Coordinate[] coordinates = ls.getCoordinates();
            List<Coordinate> coordinateList = Arrays.asList(coordinates);
            // list from asList does not support add(index,object), must make an arraylist
            List<Coordinate> nCoordinateList = new ArrayList<>(coordinateList);
            if (!ls.getCoordinateN(0).equals(lineSegment.p0)) {
                nCoordinateList.add(0, lineSegment.p0);
            } else if (!ls.getCoordinateN(ls.getNumPoints() - 1).equals(lineSegment.p1)) {
                nCoordinateList.add(lineSegment.p1);
            }
            Coordinate[] newCoordinates = nCoordinateList.toArray(new Coordinate[nCoordinateList.size()]);
            ls = gf.createLineString(newCoordinates);
        }
        // over write object to be the linestring
        e.setObject(ls);
        return e;
    }

    @Override
    protected LineSegment alterLine(LineSegment line, Node n1, Node n2) {
        Point c1added = (Point) n1.getObject();
        Point c2added = (Point) n2.getObject();
        if (!c1added.getCoordinate().equals(line.p0) || c2added.getCoordinate().equals(line.p1)) {
            line = new LineSegment(c1added.getCoordinate(), c2added.getCoordinate());
        }
        return line;
    }

    @Override
    public Graphable remove(Object obj) {
        LineString ls = (LineString) obj;

        // parent ecpexts a line segment
        return super.remove(new LineSegment(ls.getCoordinateN(0), ls.getCoordinateN(ls.getNumPoints() - 1)));
    }

    @Override
    public Graphable get(Object obj) {
        LineString ls = (LineString) obj;

        // parent ecpexts a line segment
        return super.get(new LineSegment(ls.getCoordinateN(0), ls.getCoordinateN(ls.getNumPoints() - 1)));
    }

    @Override
    protected void setObject(Node n, Object obj) {
        // set underlying object to be point instead of coordinate
        Coordinate c = (Coordinate) obj;
        n.setObject(gf.createPoint(c));
    }
}
