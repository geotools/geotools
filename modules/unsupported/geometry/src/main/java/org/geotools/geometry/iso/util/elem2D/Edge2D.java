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
package org.geotools.geometry.iso.util.elem2D;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author roehrig
 *     <p>TODO To change the template for this generated type comment go to Window - Preferences -
 *     Java - Code Style - Code Templates
 */
public class Edge2D extends Line2D {

    public static int LEFTSIDE = 0;
    public static int RIGHTSIDE = 1;

    public int id;
    protected Node2D p1;
    protected Node2D p2;
    protected Simplex2D surfaceRight;
    protected Simplex2D surfaceLeft;
    public Object object;

    protected Edge2D(Node2D p1, Node2D p2) {
        this.id = -1;
        this.p1 = p1;
        this.p2 = p2;
        this.surfaceRight = null;
        this.surfaceLeft = null;
        this.object = null;
    }

    protected Edge2D(Node2D p1, Node2D p2, Simplex2D surfaceRight, Simplex2D surfaceLeft) {
        this.id = -1;
        this.p1 = p1;
        this.p2 = p2;
        this.surfaceRight = surfaceRight;
        this.surfaceLeft = surfaceLeft;
        this.object = null;
    }

    protected void setNodes(Node2D p1, Node2D p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    protected void setSimplex(Simplex2D simplex, int SIDE) {
        if (SIDE == RIGHTSIDE) this.surfaceRight = simplex;
        else if (SIDE == LEFTSIDE) this.surfaceLeft = simplex;
    }
    /** @return Returns the surfaceRight. */
    public Simplex2D getSurfaceRight() {
        return surfaceRight;
    }
    /** @return Returns the surfaceLeft. */
    public Simplex2D getSurfaceLeft() {
        return surfaceLeft;
    }

    public int getNumberOfSurfaces() {
        return (surfaceRight == null)
                ? ((surfaceLeft == null) ? 0 : 1)
                : ((surfaceLeft == null) ? 1 : 2);
    }
    /** @return Returns the point0. */
    public boolean hasPoint(Point2D p) {
        return p1 == p || p2 == p;
    }
    /** @return Returns the surfaceLeft. */
    public boolean hasSimplex(Simplex2D s) {
        return surfaceRight == s || surfaceLeft == s;
    }

    /** */
    public void reverse() {
        Node2D p = p1;
        p1 = p2;
        p2 = p;
        Simplex2D s = surfaceRight;
        surfaceRight = surfaceLeft;
        surfaceLeft = s;
    }

    /** */
    public Simplex2D getNeighborSimplex(Simplex2D f) {
        return (surfaceRight == f) ? surfaceLeft : surfaceRight;
    }

    /** @return */
    public Simplex2D getLeftSimplex() {
        return surfaceLeft;
    }

    public Simplex2D getRightSimplex() {
        return surfaceRight;
    }
    /* (non-Javadoc)
     * @see java.awt.geom.Line2D#getX1()
     */
    public double getX1() {
        return this.p1.getX();
    }
    /* (non-Javadoc)
     * @see java.awt.geom.Line2D#getY1()
     */
    public double getY1() {
        return this.p1.getY();
    }
    /* (non-Javadoc)
     * @see java.awt.geom.Line2D#getP1()
     */
    public Point2D getP1() {
        return this.p1;
    }

    public Node2D getNode1() {
        return this.p1;
    }
    /* (non-Javadoc)
     * @see java.awt.geom.Line2D#getX2()
     */
    public double getX2() {
        return this.p2.getX();
    }
    /* (non-Javadoc)
     * @see java.awt.geom.Line2D#getY2()
     */
    public double getY2() {
        return this.p2.getY();
    }
    /* (non-Javadoc)
     * @see java.awt.geom.Line2D#getP2()
     */
    public Point2D getP2() {
        return this.p2;
    }

    public Node2D getNode2() {
        return this.p2;
    }
    /* (non-Javadoc)
     * @see java.awt.geom.Line2D#setLine(double, double, double, double)
     */
    public void setLine(double x1, double y1, double x2, double y2) {
        this.p1.setLocation(x1, y1);
        this.p2.setLocation(x2, y2);
    }
    /* (non-Javadoc)
     * @see java.awt.Shape#getBounds2D()
     */
    public Rectangle2D getBounds2D() {
        double x1 = getX1();
        double y1 = getY1();
        double x2 = getX2();
        double y2 = getY2();
        if (x2 < x1) {
            double tmp = x2;
            x2 = x1;
            x1 = tmp;
        }
        if (y2 < y1) {
            double tmp = y2;
            y2 = y1;
            y1 = tmp;
        }
        return new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1);
    }

    public double getLength() {
        return this.p1.distance(this.p2);
    }

    public double getLengthSq() {
        return this.p1.distanceSq(this.p2);
    }

    /**
     * returns the neighbour edge from this connected to node n. Returns null if the node n does not
     * belong to this edge or if the node n is not connected with exactly two edges
     */
    public Edge2D getNeighbourEdge(Node2D n) {
        if (this.p1 != n && this.p2 != n) return null;
        Edge2D edges[] = n.getEdges();
        if (edges.length != 2) return null;
        return (edges[0] == this) ? edges[1] : edges[0];
    }

    public static ArrayList<LinkedList<Point2D>> getCoordinateSequencesFromEdges(
            ArrayList<Edge2D> edges) {

        ArrayList<LinkedList<Point2D>> result = new ArrayList<LinkedList<Point2D>>();

        while (!edges.isEmpty()) {

            LinkedList<Point2D> coordList = new LinkedList<Point2D>();

            Edge2D e = edges.remove(edges.size() - 1);
            Node2D nBeg = e.getNode1();
            Node2D nEnd = e.getNode2();
            coordList.addFirst(new Point2D.Double(nBeg.x, nBeg.y));
            coordList.addLast(new Point2D.Double(nEnd.x, nEnd.y));

            Edge2D en = e.getNeighbourEdge(nBeg);
            while (en != null && en != e) {
                Node2D n = (en.getNode1() == nBeg) ? en.getNode2() : en.getNode1();
                coordList.addFirst(new Point2D.Double(n.x, n.y));
                nBeg = n;
                Edge2D removeEdge = en;
                en = en.getNeighbourEdge(nBeg);
                edges.remove(removeEdge);
            }

            en = e.getNeighbourEdge(nEnd);
            while (en != null && en != e) {
                Node2D n = (en.getNode1() == nEnd) ? en.getNode2() : en.getNode1();
                coordList.addLast(new Point2D.Double(n.x, n.y));
                nEnd = n;
                Edge2D removeEdge = en;
                en = en.getNeighbourEdge(nEnd);
                edges.remove(removeEdge);
            }

            result.add(coordList);
        }
        return result;
    }
}
