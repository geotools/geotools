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
package org.geotools.graph.util.delaunay;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Node;
import org.geotools.graph.structure.line.XYNode;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 *
 * @author jfc173
 *
 *
 * @source $URL$
 */
public class Triangle {
    
    Edge edge1, edge2, edge3;
    Node node1, node2, node3;
    GeometryFactory fact = new GeometryFactory();    
    
    public final static int OUTSIDE = 0;
    public final static int INSIDE = 1;
    public final static int ON_EDGE = 2;
    private final static double TOLERANCE = 0.0001;    
    
    /** Creates a new instance of Triangle */
    public Triangle(Edge e1, Edge e2, Edge e3) {
        Node n1a = e1.getNodeA();
        Node n1b = e1.getNodeB();
        Node n2a = e2.getNodeA();
        Node n2b = e2.getNodeB();
        Node n3a = e3.getNodeA();
        Node n3b = e3.getNodeB();
        
        Node temp1, temp2, temp3;
        if (n1a.equals(n2a)){
            temp1 = n1a; //==n2a
            temp2 = n1b;
            temp3 = n2b;
            if (n3a.equals(temp2)){
                if (n3b.equals(temp3)){
                    assign(e1, e2, e3, temp1, temp2, temp3);
                } else {
                    whinge(e1, e2, e3);
                }
            } else if (n3a.equals(temp3)){
                if (n3b.equals(temp2)){
                    assign(e1, e2, e3, temp1, temp2, temp3);
                } else {
                    whinge(e1, e2, e3);
                }
            } else {
                whinge(e1, e2, e3);
            }
        } else if (n1a.equals(n2b)){
            temp1 = n1a; //==n2b
            temp2 = n1b;
            temp3 = n2a;
            if (n3a.equals(temp2)){
                if (n3b.equals(temp3)){
                    assign(e1, e2, e3, temp1, temp2, temp3);
                } else {
                    whinge(e1, e2, e3);
                }
            } else if (n3a.equals(temp3)){
                if (n3b.equals(temp2)){
                    assign(e1, e2, e3, temp1, temp2, temp3);
                } else {
                    whinge(e1, e2, e3);
                }
            } else {
                whinge(e1, e2, e3);
            }            
        } else if (n1b.equals(n2a)){
            temp1 = n1a;
            temp2 = n1b; //==n2a
            temp3 = n2b;
            if (n3a.equals(temp1)){
                if (n3b.equals(temp3)){
                    assign(e1, e2, e3, temp1, temp2, temp3);
                } else {
                    whinge(e1, e2, e3);
                }
            } else if (n3a.equals(temp3)){
                if (n3b.equals(temp1)){
                    assign(e1, e2, e3, temp1, temp2, temp3);
                } else {
                    whinge(e1, e2, e3);
                }
            } else {
                whinge(e1, e2, e3);
            }             
        } else if (n1b.equals(n2b)){
            temp1 = n1a;
            temp2 = n1b; //==n2b
            temp3 = n2a;
            if (n3a.equals(temp1)){
                if (n3b.equals(temp3)){
                    assign(e1, e2, e3, temp1, temp2, temp3);
                } else {
                    whinge(e1, e2, e3);
                }
            } else if (n3a.equals(temp3)){
                if (n3b.equals(temp1)){
                    assign(e1, e2, e3, temp1, temp2, temp3);
                } else {
                    whinge(e1, e2, e3);
                }
            } else {
                whinge(e1, e2, e3);
            }               
        } else {
            whinge(e1, e2, e3);
        }
        
    }
    
    private void assign(Edge e1, Edge e2, Edge e3, Node n1, Node n2, Node n3){
        edge1 = e1;
        edge2 = e2;
        edge3 = e3;
        node1 = n1;
        node2 = n2;
        node3 = n3;          
    }
    
    private void whinge(Edge e1, Edge e2, Edge e3){
        throw new RuntimeException("You didn't give me a proper triangle.  " + e1 + ", " + e2 + ", " + e3);
    }
    
    public int relate(XYNode n){
        int ret;
        if ((!(node1 instanceof XYNode)) ||
            (!(node2 instanceof XYNode)) ||
            (!(node3 instanceof XYNode))){
            throw new RuntimeException("I can't perform a relate function on a non-spatial triangle");
        }
        LinearRing lr = fact.createLinearRing(new Coordinate[]{((XYNode)node1).getCoordinate(), ((XYNode)node2).getCoordinate(), ((XYNode)node3).getCoordinate(), ((XYNode)node1).getCoordinate()});
        Polygon poly = fact.createPolygon(lr, null);
        Point nPoint = fact.createPoint(n.getCoordinate());
        
        Line2D.Double line12 = new Line2D.Double(((XYNode)node1).getCoordinate().x, ((XYNode)node1).getCoordinate().y, ((XYNode)node2).getCoordinate().x, ((XYNode)node2).getCoordinate().y);
        Line2D.Double line13 = new Line2D.Double(((XYNode)node1).getCoordinate().x, ((XYNode)node1).getCoordinate().y, ((XYNode)node3).getCoordinate().x, ((XYNode)node3).getCoordinate().y);
        Line2D.Double line23 = new Line2D.Double(((XYNode)node2).getCoordinate().x, ((XYNode)node2).getCoordinate().y, ((XYNode)node3).getCoordinate().x, ((XYNode)node3).getCoordinate().y);
        Point2D.Double point2D = new Point2D.Double(n.getCoordinate().x, n.getCoordinate().y);
        if ((line12.ptSegDist(point2D) <= TOLERANCE) ||
            (line13.ptSegDist(point2D) <= TOLERANCE) ||
            (line23.ptSegDist(point2D) <= TOLERANCE)){
            ret = ON_EDGE;
        } else if (poly.contains(nPoint)){
            ret = INSIDE;
        } else {
            ret = OUTSIDE;
        }
        return ret;
    }            
    
    public Edge getBoundaryEdge(XYNode n){
        if (!(relate(n) == ON_EDGE)){
            throw new RuntimeException("Can't get the boundary edge for a point that isn't on an edge.");
        }
        Point2D.Double point = new Point2D.Double(n.getCoordinate().x, n.getCoordinate().y);
        Line2D.Double line1 = new Line2D.Double(((XYNode) edge1.getNodeA()).getCoordinate().x, ((XYNode) edge1.getNodeA()).getCoordinate().y,
                                                ((XYNode) edge1.getNodeB()).getCoordinate().x, ((XYNode) edge1.getNodeB()).getCoordinate().y);
        Line2D.Double line2 = new Line2D.Double(((XYNode) edge2.getNodeA()).getCoordinate().x, ((XYNode) edge2.getNodeA()).getCoordinate().y,
                                                ((XYNode) edge2.getNodeB()).getCoordinate().x, ((XYNode) edge2.getNodeB()).getCoordinate().y);
        Line2D.Double line3 = new Line2D.Double(((XYNode) edge3.getNodeA()).getCoordinate().x, ((XYNode) edge3.getNodeA()).getCoordinate().y,
                                                ((XYNode) edge3.getNodeB()).getCoordinate().x, ((XYNode) edge3.getNodeB()).getCoordinate().y);   
        Edge ret = null;
        if (line1.ptSegDist(point) <= TOLERANCE){
            ret = edge1;
        } else if (line2.ptSegDist(point) <= TOLERANCE){
            ret = edge2;
        } else if (line3.ptSegDist(point) <= TOLERANCE){
            ret = edge3;
        } else {
            throw new RuntimeException("So...  node " + n + " is on an edge of " + this.toString() + " but isn't on any of its edges: " + edge1 + ", " + edge2 + ", or " + edge3);
        }
        return ret;
    }
    
    public Node getThirdNode(Edge e){
        if (e.getNodeA().equals(node1)){            
            if (e.getNodeB().equals(node2)){
                return node3;
            } else if (e.getNodeB().equals(node3)){
                return node2;
            } else {
                throw new RuntimeException("Edge e must be in this triangle for Triangle.getThirdNode to work!");
            }
        } else if (e.getNodeA().equals(node2)){
            if(e.getNodeB().equals(node1)){
                return node3;
            } else if (e.getNodeB().equals(node3)){
                return node1;
            } else {
                throw new RuntimeException("Edge e must be in this triangle for Triangle.getThirdNode to work!");               
            }
        } else if (e.getNodeA().equals(node3)){
            if (e.getNodeB().equals(node2)){
                return node1;
            } else if (e.getNodeB().equals(node1)){
                return node2;
            } else {
                throw new RuntimeException("Edge e must be in this triangle for Triangle.getThirdNode to work!");
            }
        } else {
            throw new RuntimeException("Edge " + e + " must be in this triangle " + this.toString() + " for Triangle.getThirdNode to work!");
        }
    }
    
    public Edge getOppositeEdge(Node n){
        if ((edge1.getNodeA().equals(n) || (edge1.getNodeB().equals(n)))){
            if ((edge2.getNodeA().equals(n) || (edge2.getNodeB().equals(n)))){
                return edge3;
            } else if ((edge3.getNodeA().equals(n) || (edge3.getNodeB().equals(n)))){
                return edge2;
            } else {
                throw new RuntimeException("Node n must be in this triangle for Triangle.getOppositeEdge to work!");
            }
        } else if ((edge2.getNodeA().equals(n) || (edge2.getNodeB().equals(n)))){
            if ((edge3.getNodeA().equals(n) || (edge3.getNodeB().equals(n)))){
                return edge1;
            } else {
                throw new RuntimeException("Node n must be in this triangle for Triangle.getOppositeEdge to work!");
            }
        } else {
            throw new RuntimeException("Node n must be in this triangle for Triangle.getOppositeEdge to work!");
        }
    }
    
    public Edge getSharedEdge(Triangle t){
        Edge[] tEdges = t.getEdges();
        Edge shared = null;
        for (int i = 0; i < 3; i++){
            if (tEdges[i].equals(edge1)){
                shared = edge1;
            } else if (tEdges[i].equals(edge2)){
                shared = edge2;
            } else if (tEdges[i].equals(edge3)){
                shared = edge3;
            }
        }
        return shared;            
    }
    
    public Edge[] getEdges(){
        return new Edge[]{edge1, edge2, edge3};
    }
    
    public Node[] getNodes(){
        return new Node[]{node1, node2, node3};
    }
    
    public double getArea(){
        if ((node1 instanceof XYNode) ||
            (node2 instanceof XYNode) ||
            (node3 instanceof XYNode)){
            double x1 = ((XYNode) node1).getCoordinate().x;
            double y1 = ((XYNode) node1).getCoordinate().y;
            double x2 = ((XYNode) node2).getCoordinate().x;
            double y2 = ((XYNode) node2).getCoordinate().y;
            double x3 = ((XYNode) node3).getCoordinate().x;
            double y3 = ((XYNode) node3).getCoordinate().y;
            
            double length1_2 = Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
            double length1_3 = Math.sqrt((x1-x3)*(x1-x3) + (y1-y3)*(y1-y3));
            double length2_3 = Math.sqrt((x2-x3)*(x2-x3) + (y2-y3)*(y2-y3));
            double s = (length1_2 + length1_3 + length2_3)/2;
            
            return Math.sqrt((s) * (s - length1_2) * (s - length1_3) * (s - length2_3));                        
        } else {
            throw new RuntimeException("I can't calculate the area if the triangle doesn't have XY coordinates.");
        }
    }
    
    public boolean containsEdge(Edge e){
        return ((edge1.equals(e)) || (edge2.equals(e)) || (edge3.equals(e)));
    }
    
    public boolean equals(Object o){
        boolean ret;
        if (o instanceof Triangle){
            ret = ((this.containsEdge(((Triangle) o).getEdges()[0])) &&
                   (this.containsEdge(((Triangle) o).getEdges()[1])) &&
                   (this.containsEdge(((Triangle) o).getEdges()[2])));
        } else {
            ret = false;
        }
        return ret;
    }
    
    public String toString(){
        return ("{" + node1.toString() + ", " + node2.toString() + ", " + node3.toString() + "}");
    }
    
}
