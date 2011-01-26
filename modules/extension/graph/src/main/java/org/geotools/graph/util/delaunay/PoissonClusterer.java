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

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.geotools.filter.Expression;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.basic.BasicGraph;
import org.opengis.feature.simple.SimpleFeature;

/**
 *
 * @author jfc173
 *
 * @source $URL$
 */
public class PoissonClusterer {
    
    private static double threshold = 1.0E-10;
    
    /** Creates a new instance of PoissonClusterer */
    public PoissonClusterer() {
    }        
    
    public static Graph findClusters(Graph incoming, Expression base, Expression target, double meanRate, int distance){
        Collection nodes = incoming.getNodes();
        Iterator nodeIt = nodes.iterator();
        Vector clusterNodes = new Vector();
        Vector clusterEdges = new Vector();
        System.out.println("x, y, actual, expected, probability");
        while (nodeIt.hasNext()){
            DelaunayNode next = (DelaunayNode) nodeIt.next();
            SimpleFeature nextFeature = next.getFeature();
            
            Object baseObj = base.getValue(nextFeature);
            if (!(baseObj instanceof Number)){
                throw new RuntimeException("Expression " + base + " must evaluate to a number on feature " + nextFeature);
            }
            Object targetObj = target.getValue(nextFeature);
            if (!(targetObj instanceof Number)){
                throw new RuntimeException("Expression " + target + " must evaluate to a number on feature " + nextFeature);
            } 
            double totalBase = ((Number) baseObj).doubleValue();
            double totalTarget = ((Number) targetObj).doubleValue();                              
                   
            Collection newEdges = new Vector();
            Vector newNodes = new Vector();
            newNodes.add(next);
            
            if (distance == 1){
            
                newEdges = next.getEdges();
//                System.out.println("this node has " + newEdges.size() + " edges");
                Iterator edgeIt = newEdges.iterator();
                Vector removals = new Vector();
                while (edgeIt.hasNext()){
                    DelaunayEdge nextEdge = (DelaunayEdge) edgeIt.next();
                    if (nextEdge.getEuclideanDistance() > 30){
                        removals.add(nextEdge);
                    } else {
                        DelaunayNode neighbor = (DelaunayNode) nextEdge.getOtherNode(next);
                        if (neighbor == null){
                            throw new RuntimeException("We have a problem.  " + next + " and " + neighbor + " should be neighbors via " + nextEdge + ", but aren't.");
                        }
                        SimpleFeature neighborFeature = neighbor.getFeature();
                        newNodes.add(neighbor);

                        Object neighborsBaseObj = base.getValue(nextFeature);
                        if (!(baseObj instanceof Number)){
                            throw new RuntimeException("Expression " + base + " must evaluate to a number on feature " + neighborFeature);
                        }
                        Object neighborsTargetObj = target.getValue(nextFeature);
                        if (!(targetObj instanceof Number)){
                            throw new RuntimeException("Expression " + target + " must evaluate to a number on feature " + neighborFeature);
                        } 
                        totalBase = totalBase + ((Number) baseObj).doubleValue();
                        totalTarget = totalTarget + ((Number) targetObj).doubleValue(); 
                    }
                }
                newEdges.removeAll(removals);
            } else {
                for (int i = 0; i <= distance; i++){
                    Iterator nodeIt2 = newNodes.iterator();  
                    Vector nodesToAdd = new Vector();
                    Vector edgesToAdd = new Vector();
                    while (nodeIt2.hasNext()){
                        DelaunayNode next2 = (DelaunayNode) nodeIt2.next();
//                        System.out.println("expanding from " + next2);
                        Collection edges = next2.getEdges();
//                        System.out.println("its edges are " + edges);
                        newEdges.addAll(edges);
                        Iterator another = edges.iterator();
                        while (another.hasNext()){
                            DelaunayEdge nextEdge = (DelaunayEdge) another.next();
                            DelaunayNode farNode = (DelaunayNode) nextEdge.getOtherNode(next2);
//                            System.out.println("checking " + farNode + " in " + newNodes);
                            if (!(newNodes.contains(farNode))){
                                nodesToAdd.add(farNode);
                                edgesToAdd.add(nextEdge);
//                                System.out.println("adding node " + farNode + " and edge " + nextEdge);
                            }
                        }
                    }
                    newNodes.addAll(nodesToAdd);
                    newEdges.addAll(edgesToAdd);
                }
                
//                System.out.println("I've got " + newNodes + " and ");
//                System.out.println(newEdges);
                
                totalBase = totalTarget = 0;
                Iterator newNodeIt = newNodes.iterator();
                while (newNodeIt.hasNext()){
                    DelaunayNode nextNode = (DelaunayNode) newNodeIt.next();
                    SimpleFeature nextFeature2 = nextNode.getFeature();
                    Object neighborsBaseObj = base.getValue(nextFeature2);
                    if (!(baseObj instanceof Number)){
                        throw new RuntimeException("Expression " + base + " must evaluate to a number on feature " + nextFeature2);
                    }
                    Object neighborsTargetObj = target.getValue(nextFeature2);
                    if (!(targetObj instanceof Number)){
                        throw new RuntimeException("Expression " + target + " must evaluate to a number on feature " + nextFeature2);
                    } 
                    totalBase = totalBase + ((Number) baseObj).doubleValue();
                    totalTarget = totalTarget + ((Number) targetObj).doubleValue();                     
                }
            }
            double expectedTarget = meanRate * totalBase;

            double top = ((Math.pow(Math.E,(0 - expectedTarget)) * (Math.pow(expectedTarget, totalTarget))));
            double bottom = fact((int) Math.round(totalTarget));
            double poissonProb = top/bottom;
//            System.out.println("testing " + newNodes);
//            System.out.println("testing " + newEdges);
            System.out.println(next.getCoordinate().x + ", " + next.getCoordinate().y + ", " + totalTarget + ", " + expectedTarget + ", " + poissonProb);
            
            if (poissonProb < threshold){
                clusterNodes.addAll(newNodes);
                clusterEdges.addAll(newEdges);
            }            
        }  //end while (nodeIt.hasNext())
        
        return new BasicGraph(clusterNodes, clusterEdges);
        
    }
    
    private static double iterFact(int i, int f){
        if ((i == 0) || (i == 1)){
            return f;
        } else {
            return iterFact(i-1, i*f);
        }
    }

    public static double fact(int i){
        return iterFact(i, 1);
    }     
    
}
