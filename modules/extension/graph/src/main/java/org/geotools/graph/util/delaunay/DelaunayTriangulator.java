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

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Node;
import org.geotools.graph.structure.basic.BasicGraph;
import org.geotools.math.Line;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 *
 * @author jfc173
 *
 *
 * @source $URL$
 */
public class DelaunayTriangulator {
   
    public DelaunayNode temp1, temp2, temp3;
    private DelaunayNode[] nodes;
    private Vector triangleList;
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.graph");
    
    /** Creates a new instance of delaunayTriangulator */
    public DelaunayTriangulator() {
    }
    
    public void setNodeArray(DelaunayNode[] nodeArray){
        nodes = nodeArray;
    }
    
    public DelaunayNode[] getNodeArray(){
        return nodes;
    }
    
    public void setFeatureCollection(SimpleFeatureCollection data){
        nodes = featuresToNodes(data);
    }
    
    public Vector getTriangles(){
        return triangleList;
    }
    
    public DelaunayNode[] featuresToNodes(SimpleFeatureCollection fc){
        SimpleFeatureIterator iter = fc.features();
        int size = fc.size();
        DelaunayNode[] nodes = new DelaunayNode[size];
        int index = 0;
        while (iter.hasNext()){
            SimpleFeature next = iter.next();
            Geometry geom = (Geometry) next.getDefaultGeometry();
            Point centroid;
            if (geom instanceof Point){
                centroid = (Point) geom;
            } else {
                centroid = geom.getCentroid();
            }
            DelaunayNode node = new DelaunayNode();   
            node.setCoordinate(centroid.getCoordinate());  
            node.setFeature(next);
            if (!(arrayContains(node, nodes, index))){
                nodes[index] = node;
                index++;                
            }                  
        }
        
        DelaunayNode[] trimmed = new DelaunayNode[index];
        for (int i = 0; i < index; i++){
            trimmed[i] = nodes[i];
        }

        return trimmed;
    }
    
    private static boolean arrayContains(DelaunayNode node, DelaunayNode[] nodes, int index){
        boolean ret = false;
        boolean done = false;
        int i = 0;
        while (!(done)){
            if (i < index){
                done = ret = (nodes[i].equals(node));
                i++;
            } else {
                done = true;
            }
        }
        return ret;
    }
    
    public Graph getTriangulation(){
        //this algorithm is from "Computational Geometry: Algorithms and Applications" by M. de Berg et al., 
        //written in 1997 and printed by Springer-Verlag (New York).  Pseudocode from section 9.3 (pp. 190-194).
        //A few additional checks for degenerate cases were needed.  They're commented below.        
               
        //find the initial bounding triangle and supplement the nodes with its corners        
        DelaunayNode[] tempNodes = new DelaunayNode[nodes.length+3];
        double max = 0;
        for (int i = 0; i < nodes.length; i++){
            tempNodes[i] = nodes[i];
            max = Math.max(max, Math.abs(nodes[i].getCoordinate().x));
            max = Math.max(max, Math.abs(nodes[i].getCoordinate().y));
        }
        tempNodes[nodes.length] = new DelaunayNode();
        tempNodes[nodes.length].setCoordinate(new Coordinate(0, 3*max));
        tempNodes[nodes.length+1] = new DelaunayNode();
        tempNodes[nodes.length+1].setCoordinate(new Coordinate(3*max, 0));       
        tempNodes[nodes.length+2] = new DelaunayNode();
        tempNodes[nodes.length+2].setCoordinate(new Coordinate(-3*max, -3*max));        
        
        temp1 = tempNodes[nodes.length];
        temp2 = tempNodes[nodes.length+1];
        temp3 = tempNodes[nodes.length+2];
        
        //initialize triangulation to the bounding triangle
        triangleList = new Vector();
        DelaunayEdge e1 = new DelaunayEdge(tempNodes[nodes.length], tempNodes[nodes.length+1]);
        DelaunayEdge e2 = new DelaunayEdge(tempNodes[nodes.length], tempNodes[nodes.length+2]);
        DelaunayEdge e3 = new DelaunayEdge(tempNodes[nodes.length+1], tempNodes[nodes.length+2]); 
        Triangle first = new Triangle(e1, e2, e3);
        e1.setFaceA(first);
        e2.setFaceA(first);
        e3.setFaceA(first);
        
        DelaunayNode U1 = new DelaunayNode();
        U1.setCoordinate(new Coordinate(Double.POSITIVE_INFINITY, 0));
        DelaunayNode U2 = new DelaunayNode();
        U2.setCoordinate(new Coordinate(0, Double.POSITIVE_INFINITY));
        DelaunayNode U3 = new DelaunayNode();
        U3.setCoordinate(new Coordinate(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));    
        Triangle UNBOUNDED = new Triangle(new DelaunayEdge(U1, U2),  
                                          new DelaunayEdge(U1, U3), 
                                          new DelaunayEdge(U2, U3));               
        
        e1.setFaceB(UNBOUNDED);
        e2.setFaceB(UNBOUNDED);
        e3.setFaceB(UNBOUNDED);
        
        triangleList.add(first);                
        
        //add nodes one at a time.
        for (int i = 0; i < nodes.length; i++){
            System.out.println("triangulating node " + i);
            triangleList = insertNode(tempNodes[i], triangleList);
        }

        Graph g = triangleListToGraph(triangleList);
                
        return g;  
    }
    
    public Graph triangleListToGraph(Vector tList){
        //turn what I've got into a proper GeoTools2 Graph!        
        //But don't include the three temporary nodes and all incident edges.
        Vector edgeList = new Vector();
        Vector nodeList = new Vector();
        Iterator triangleIterator = tList.iterator();
        while(triangleIterator.hasNext()){
            Triangle next = (Triangle) triangleIterator.next();
            Edge[] edges = next.getEdges();
            for (int i = 0; i < 3; i++){
                if (!(((DelaunayEdge) edges[i]).hasEndPoint(temp1) ||      //this test ensures that we don't
                      ((DelaunayEdge) edges[i]).hasEndPoint(temp2) ||      //add to the edge list any edges referring                        
                      ((DelaunayEdge) edges[i]).hasEndPoint(temp3))){      //to the temporary nodes
                    if (!(edgeList.contains(edges[i]))){
                        edgeList.add(edges[i]);
                        edges[i].getNodeA().add(edges[i]);
                        edges[i].getNodeB().add(edges[i]);
                        if (!(nodeList.contains(edges[i].getNodeA()))){
                            nodeList.add(edges[i].getNodeA());
                        }
                        if (!(nodeList.contains(edges[i].getNodeB()))){
                            nodeList.add(edges[i].getNodeB());
                        }                        
                    }
                }
            }
        }
        
        return new BasicGraph(nodeList, edgeList);        
    }
    
    public Vector insertNode(DelaunayNode newNode, Vector tList){
        //find triangle containing node or if node is on an edge, the two triangles bordering that edge.
        //this finding-the-triangle section can be given better efficiency using the method on pp. 192-193 of book mentioned above.
        Iterator triangleIterator = tList.iterator();
        Triangle contains = null;
        Triangle borderA = null;
        Triangle borderB = null;  //Note: assuming it's on the border of two triangles rather than at the intersection of 3 or more.
        boolean notDone = true;        
        while ((triangleIterator.hasNext()) && (notDone)){
            Triangle next = (Triangle) triangleIterator.next();
            int relation = next.relate(newNode);
            switch (relation){
                case Triangle.INSIDE:
//                    System.out.println(newNode + " is inside " + next);
                    contains = next;
                    notDone = false;
                    break;
                
                case Triangle.ON_EDGE:
                    borderA = next;
                    borderB = ((DelaunayEdge) next.getBoundaryEdge(newNode)).getOtherFace(next);
//                    System.out.println(newNode + " is on the border between " + borderA + " and " + borderB);
                    break;
                
                case Triangle.OUTSIDE:
                    notDone = true;
                    break;
                
                default:
                    throw new RuntimeException("So the point isn't inside, outside, or on the edge of this triangle?!");
            } //end switch            
        }
        
        //Found the triangle(s).  Now do something with them!
        if (contains != null){
            //create three new triangles by adding edges from node to the vertices of contains
            Node[] triangleNodes = contains.getNodes();
            Edge[] triangleEdges = contains.getEdges();
            
            DelaunayEdge newEdgeP_0 = new DelaunayEdge(newNode, (DelaunayNode) triangleNodes[0]);
            DelaunayEdge newEdgeP_1 = new DelaunayEdge(newNode, (DelaunayNode) triangleNodes[1]);
            DelaunayEdge newEdgeP_2 = new DelaunayEdge(newNode, (DelaunayNode) triangleNodes[2]);
            
            DelaunayEdge oldEdge0_1 = null;
            DelaunayEdge oldEdge0_2 = null;
            DelaunayEdge oldEdge1_2 = null;
            for (int i = 0; i < 3; i++){
                if (((DelaunayEdge) triangleEdges[i]).hasEndPoint((DelaunayNode) triangleNodes[0])){
                    if (((DelaunayEdge) triangleEdges[i]).hasEndPoint((DelaunayNode) triangleNodes[1])){
                        oldEdge0_1 = (DelaunayEdge) triangleEdges[i];
                    } else {
                        oldEdge0_2 = (DelaunayEdge) triangleEdges[i];
                    }
                } else {
                    oldEdge1_2 = (DelaunayEdge) triangleEdges[i];
                }
            }
            
            Triangle newTriangleP_0_1 = new Triangle(newEdgeP_0, newEdgeP_1, oldEdge0_1);
            Triangle newTriangleP_0_2 = new Triangle(newEdgeP_0, newEdgeP_2, oldEdge0_2);
            Triangle newTriangleP_1_2 = new Triangle(newEdgeP_1, newEdgeP_2, oldEdge1_2);
            
            Triangle farSide0_1 = oldEdge0_1.getOtherFace(contains);
            Triangle farSide0_2 = oldEdge0_2.getOtherFace(contains);
            Triangle farSide1_2 = oldEdge1_2.getOtherFace(contains);
            
            oldEdge0_1.setOtherFace(newTriangleP_0_1, farSide0_1);
            oldEdge0_2.setOtherFace(newTriangleP_0_2, farSide0_2);
            oldEdge1_2.setOtherFace(newTriangleP_1_2, farSide1_2);
            
            newEdgeP_0.setFaceA(newTriangleP_0_1);
            newEdgeP_0.setFaceB(newTriangleP_0_2);
            newEdgeP_1.setFaceA(newTriangleP_0_1);
            newEdgeP_1.setFaceB(newTriangleP_1_2);
            newEdgeP_2.setFaceA(newTriangleP_0_2);
            newEdgeP_2.setFaceB(newTriangleP_1_2);
            
            tList.remove(contains);
            tList.add(newTriangleP_0_1);
            tList.add(newTriangleP_0_2);
            tList.add(newTriangleP_1_2);
            LOGGER.finer("was inside " + contains);
            LOGGER.finer("triangle List now is " + tList);
//            System.out.println("triangle List now is " + tList);
            //Make any necessary adjustments to other triangles.
            legalizeEdge(newTriangleP_0_1, oldEdge0_1, newNode, tList);
            legalizeEdge(newTriangleP_0_2, oldEdge0_2, newNode, tList);
            legalizeEdge(newTriangleP_1_2, oldEdge1_2, newNode, tList);
            LOGGER.finer("after legalization, triangle list now is: " + triangleList); 
//            System.out.println("after legalization, triangle list now is: " + triangleList);
            
        } else if ((borderA != null) && (borderB != null)){
            //check to see that borderA and borderB share an edge.  If not, whinge.
            DelaunayEdge shared = (DelaunayEdge) borderA.getSharedEdge(borderB);
            if (shared == null){
                throw new RuntimeException("The two bordering triangles for a border case apparently don't share an edge(!)");
            }
            
            //create four new triangles by adding edges from node to the vertices of borderA and borderB
            DelaunayNode shared1 = (DelaunayNode) shared.getNodeA();
            DelaunayNode shared2 = (DelaunayNode) shared.getNodeB();
            DelaunayNode onlyInA = (DelaunayNode) borderA.getThirdNode(shared);
            DelaunayNode onlyInB = (DelaunayNode) borderB.getThirdNode(shared);
            
            DelaunayEdge newEdgeP_1 = new DelaunayEdge(newNode, shared1);
            DelaunayEdge newEdgeP_2 = new DelaunayEdge(newNode, shared2);
            DelaunayEdge newEdgeP_A = new DelaunayEdge(newNode, onlyInA);
            DelaunayEdge newEdgeP_B = new DelaunayEdge(newNode, onlyInB);
            
            DelaunayEdge oldEdgeA_1 = (DelaunayEdge) borderA.getOppositeEdge(shared2);
            DelaunayEdge oldEdgeA_2 = (DelaunayEdge) borderA.getOppositeEdge(shared1);
            DelaunayEdge oldEdgeB_1 = (DelaunayEdge) borderB.getOppositeEdge(shared2);           
            DelaunayEdge oldEdgeB_2 = (DelaunayEdge) borderB.getOppositeEdge(shared1);
            
            Triangle farSideA_1 = oldEdgeA_1.getOtherFace(borderA);
            Triangle farSideA_2 = oldEdgeA_2.getOtherFace(borderA);
            Triangle farSideB_1 = oldEdgeB_1.getOtherFace(borderB);
            Triangle farSideB_2 = oldEdgeB_2.getOtherFace(borderB);
            
            Triangle newTriangleP_A_1 = new Triangle(newEdgeP_A, newEdgeP_1, oldEdgeA_1);
            Triangle newTriangleP_A_2 = new Triangle(newEdgeP_A, newEdgeP_2, oldEdgeA_2);
            Triangle newTriangleP_B_1 = new Triangle(newEdgeP_B, newEdgeP_1, oldEdgeB_1);
            Triangle newTriangleP_B_2 = new Triangle(newEdgeP_B, newEdgeP_2, oldEdgeB_2);
            
            newEdgeP_A.setFaceA(newTriangleP_A_1);
            newEdgeP_A.setFaceB(newTriangleP_A_2);
            newEdgeP_B.setFaceA(newTriangleP_B_1);
            newEdgeP_B.setFaceB(newTriangleP_B_2);
            newEdgeP_1.setFaceA(newTriangleP_A_1);
            newEdgeP_1.setFaceB(newTriangleP_B_1);
            newEdgeP_2.setFaceA(newTriangleP_A_2);
            newEdgeP_2.setFaceB(newTriangleP_B_2);
            
            oldEdgeA_1.setOtherFace(newTriangleP_A_1, farSideA_1);
            oldEdgeA_2.setOtherFace(newTriangleP_A_2, farSideA_2);
            oldEdgeB_1.setOtherFace(newTriangleP_B_1, farSideB_1);
            oldEdgeB_2.setOtherFace(newTriangleP_B_2, farSideB_2);
            
            shared.disconnect();
            
            tList.remove(borderA);
            tList.remove(borderB);
            tList.add(newTriangleP_A_1);
            tList.add(newTriangleP_A_2);
            tList.add(newTriangleP_B_1);
            tList.add(newTriangleP_B_2);
            LOGGER.finer("bordered " + borderA + " and " + borderB);
            LOGGER.finer("triangle list now is " + tList);
            
            legalizeEdge(newTriangleP_A_1, oldEdgeA_1, newNode, tList);
            legalizeEdge(newTriangleP_A_2, oldEdgeA_2, newNode, tList);
            legalizeEdge(newTriangleP_B_1, oldEdgeB_1, newNode, tList);
            legalizeEdge(newTriangleP_B_2, oldEdgeB_2, newNode, tList);
            LOGGER.finer("after legalization, triangle list now is: " + triangleList); 
        } else {
            throw new RuntimeException("What the?  It isn't in any triangle or on any borders?");
        }  
        return tList;      
    }
 
    private void legalizeEdge(Triangle t, DelaunayEdge e, DelaunayNode p, Vector triangleList){
        LOGGER.fine("legalizing " + t + " and " + e.getOtherFace(t));
        if (isIllegal(t, e, p)){
            Triangle otherFace = e.getOtherFace(t);  
            LOGGER.finer("switch internal edge");
//            System.out.println("switch internal edge");
            DelaunayNode fourthCorner = (DelaunayNode) otherFace.getThirdNode(e);
            DelaunayNode eNodeA = (DelaunayNode) e.getNodeA();
            DelaunayNode eNodeB = (DelaunayNode) e.getNodeB();
            //replace e with a new edge from p to fourthCorner
            DelaunayEdge edgeP_4 = new DelaunayEdge(p, fourthCorner);
            DelaunayEdge edgeP_A = (DelaunayEdge) t.getOppositeEdge(eNodeB);
            DelaunayEdge edgeP_B = (DelaunayEdge) t.getOppositeEdge(eNodeA);
            DelaunayEdge edgeA_4 = (DelaunayEdge) otherFace.getOppositeEdge(eNodeB);
            DelaunayEdge edgeB_4 = (DelaunayEdge) otherFace.getOppositeEdge(eNodeA);
            
            Triangle farSideP_A = edgeP_A.getOtherFace(t);
            Triangle farSideP_B = edgeP_B.getOtherFace(t);
            Triangle farSideA_4 = edgeA_4.getOtherFace(otherFace);
            Triangle farSideB_4 = edgeB_4.getOtherFace(otherFace);
            
            Triangle newTriangleP_A_4 = new Triangle(edgeP_A, edgeA_4, edgeP_4);
            Triangle newTriangleP_B_4 = new Triangle(edgeP_B, edgeB_4, edgeP_4);
            
            if (rejectSwap(t, otherFace, newTriangleP_A_4, newTriangleP_B_4)){ //Degenerate case.  Explained in the method rejectSwap
                LOGGER.finer("Rejected swap of " + t + " and " + otherFace);
//                System.out.println("Rejected swap of " + t + " and " + otherFace);
            } else {                         
                edgeP_A.setOtherFace(newTriangleP_A_4, farSideP_A);
                edgeP_B.setOtherFace(newTriangleP_B_4, farSideP_B);
                edgeA_4.setOtherFace(newTriangleP_A_4, farSideA_4);
                edgeB_4.setOtherFace(newTriangleP_B_4, farSideB_4);

                edgeP_4.setFaceA(newTriangleP_A_4);
                edgeP_4.setFaceB(newTriangleP_B_4);
                       
                e.disconnect();
                
                triangleList.remove(t);
                triangleList.remove(otherFace);
                triangleList.add(newTriangleP_A_4);
                triangleList.add(newTriangleP_B_4);
                LOGGER.finer("swapped " + t + " and " + otherFace);
                LOGGER.finer("new triangles are " + newTriangleP_A_4 + " and " + newTriangleP_B_4);
                LOGGER.finer("Triangle list now is: " + triangleList);     
//                System.out.println("swapped " + t + " and " + otherFace);
//                System.out.println("new triangles are " + newTriangleP_A_4 + " and " + newTriangleP_B_4);
//                System.out.println("Triangle list now is: " + triangleList);
                legalizeEdge(newTriangleP_A_4, edgeA_4, p, triangleList);
                legalizeEdge(newTriangleP_B_4, edgeB_4, p, triangleList);  
            }
        }
    }
    
    private boolean isTemporary(DelaunayNode n){
        return ((n.equals(temp1)) || (n.equals(temp2)) || (n.equals(temp3)));
    }
    
    private boolean isIllegal(Triangle t, DelaunayEdge e, DelaunayNode p){
        DelaunayNode eNodeA = (DelaunayNode) e.getNodeA();
        DelaunayNode eNodeB = (DelaunayNode) e.getNodeB();

        if (isTemporary(eNodeA) && isTemporary(eNodeB)){
            return false;
        }         
        
        DelaunayNode farNode = (DelaunayNode) e.getOtherFace(t).getThirdNode(e);

        DelaunayEdge p_a = ((DelaunayEdge) t.getOppositeEdge(e.getNodeB()));
        DelaunayEdge p_b = ((DelaunayEdge) t.getOppositeEdge(e.getNodeA()));
        DelaunayNode farNodeP_A = (DelaunayNode) p_a.getOtherFace(t).getThirdNode(p_a);
        DelaunayNode farNodeP_B = (DelaunayNode) p_b.getOtherFace(t).getThirdNode(p_b);
        
        if ((farNode.equals(farNodeP_A)) || (farNode.equals(farNodeP_B))){
            //Degenerate case.  There's already a line between p and farnode (p and k in the book) making either
            //p_farnode_A or p_farNode_B a triangle already in the triangulation.  This will eventually manifest
            //itself as trying to create a triangle with two points....  Not a good situation.
            return false;
        }
        
        int numTemporary = 0;
        if (isTemporary(eNodeA)){
            numTemporary++;
        }
        if (isTemporary(eNodeB)){
            numTemporary++;
        }
        if (isTemporary(p)){
            numTemporary++;
        }
        if (isTemporary(farNode)){
            numTemporary++;
        }        
        
        if (numTemporary == 0){
            Ellipse2D.Double circum = constructCircle(p, eNodeA, eNodeB);
            Point2D.Double point = new Point2D.Double(farNode.getCoordinate().x, farNode.getCoordinate().y);
            if (circum.contains(point)){
                LOGGER.finer("Illegal by case 2");
//                System.out.println("Illegal by case 2");
                return true;
            } else {
                return false;
            }            
        } else if (numTemporary == 1){
            if (isTemporary(eNodeA) || isTemporary(eNodeB)){
                LOGGER.finer("Illegal by case 3");
//                System.out.println("Illegal by case 3");
                return true;
            } else {
                return false;
            }
        } else if (numTemporary == 2){
            int i = whichSpecialNode(eNodeA, eNodeB);
            int j = whichSpecialNode(p, farNode);
            if (i<j){  //originally i<j.  i<j for messedUp3.doc, i>j for messedUp1.doc
                return false;
            } else {
                LOGGER.finer("Illegal by case 4");
//                System.out.println("Illegal by case 4");
                return true;
            }
        } else {
            throw new RuntimeException("Problem in DelaunayTriangulator.isIllegal--This shouldn't've happened!");
        }
    }
    
    private int whichSpecialNode(DelaunayNode a, DelaunayNode b){
        if ((a.equals(temp1)) || (b.equals(temp1))){
            return 1;
        } else if ((a.equals(temp2)) || (b.equals(temp2))){
            return 2;            
        } else if ((a.equals(temp3)) || (b.equals(temp3))){
            return 3;
        } else {
            throw new RuntimeException("I shouldn't be here.  Either node a or node b should be temporary.");
        }
    }
    
    private static Ellipse2D.Double constructCircle(DelaunayNode a, DelaunayNode b, DelaunayNode c){
        //center of this circle is the intersection of the perpendicular bisectors of the triangle
        
        Point2D.Double midPointA_B = new Point2D.Double((a.getCoordinate().x + b.getCoordinate().x)/2,
                                                        (a.getCoordinate().y + b.getCoordinate().y)/2);
        double deltaXA_B = a.getCoordinate().x - midPointA_B.getX();
        double deltaYA_B = a.getCoordinate().y - midPointA_B.getY();

 
        Line bisectorA_B = new Line();
        bisectorA_B.setLine(new Point2D.Double((midPointA_B.getX() + 100*deltaYA_B), 
                                               (midPointA_B.getY() - 100*deltaXA_B)),
                            new Point2D.Double((midPointA_B.getX() - 100*deltaYA_B),
                                               (midPointA_B.getY() + 100*deltaXA_B)));
        
        Point2D.Double midPointA_C = new Point2D.Double((a.getCoordinate().x + c.getCoordinate().x)/2,
                                                        (a.getCoordinate().y + c.getCoordinate().y)/2);
        double deltaXA_C = a.getCoordinate().x - midPointA_C.getX();
        double deltaYA_C = a.getCoordinate().y - midPointA_C.getY();      
        
        Line bisectorA_C = new Line();
        
        Point2D intersection = null;
        int i = 1;
        do{
            bisectorA_C.setLine(new Point2D.Double((midPointA_C.getX() + Math.pow(100, i)*deltaYA_C), 
                                                   (midPointA_C.getY() - Math.pow(100, i)*deltaXA_C)),
                                new Point2D.Double((midPointA_C.getX() - Math.pow(100, i)*deltaYA_C),
                                                   (midPointA_C.getY() + Math.pow(100, i)*deltaXA_C)));        
            intersection = bisectorA_B.intersectionPoint(bisectorA_C);
            i++;
        } while (intersection == null);                
        
        //radius is the distance to the three points (which is hopefully the same!)        
        double radius = intersection.distance(a.getCoordinate().x, a.getCoordinate().y);
                
        //convert from center-radius to the java format
        Ellipse2D.Double circle = new Ellipse2D.Double(intersection.getX()-radius, 
                                                       intersection.getY()-radius, 
                                                       2*radius,
                                                       2*radius);
                
        return circle;  
    }
    
    private boolean rejectSwap(Triangle oldFirst, Triangle oldSecond, Triangle newFirst, Triangle newSecond){
        //I want to reject the swap if the new edge intersects any other edges in the graph, which can happen in
        //the case where A or B (i or j in the book) is one of the bounding triangle points.  This (I think)
        //is equivalent to ensuring that the areas of the new triangles is the same as the areas of the old triangles.
        double oldArea = oldFirst.getArea() + oldSecond.getArea();
        double newArea = newFirst.getArea() + newSecond.getArea();
        double diff = newArea - oldArea;
//        System.out.println("area difference is " + diff);
        double tolerance = 0.000001;
        return (diff > tolerance);   
    }
    
}
