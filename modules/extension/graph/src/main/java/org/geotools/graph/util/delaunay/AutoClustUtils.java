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
import java.util.logging.Logger;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Node;
import org.geotools.graph.structure.basic.BasicGraph;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 *
 * @author jfc173
 *
 *
 * @source $URL$
 */
public class AutoClustUtils {
    
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.graph");
    
    /** Creates a new instance of AutoClustUtils */
    public AutoClustUtils() {
    }
    
    public static Vector findConnectedComponents(final Collection nodes, final Collection edges){
        Vector components = new Vector();
        Vector nodesVisited = new Vector();
        
        Iterator nodesIt = nodes.iterator();
        while (nodesIt.hasNext()){            
            Node next = (Node) nodesIt.next();
            if (!(nodesVisited.contains(next))){
                Vector componentNodes = new Vector();
                Vector componentEdges = new Vector();
                expandComponent(next, edges, componentNodes, componentEdges);
                nodesVisited.addAll(componentNodes);
                Graph component = new BasicGraph(componentNodes, componentEdges);
                components.add(component);
            }
        }
        
        return components;
    }
    
    private static void expandComponent(final Node node, final Collection edges, final Collection componentNodes, final Collection componentEdges){
        if (componentNodes.contains(node)){
            //base case.  I've already expanded on node, so no need to repeat the process.
//            LOGGER.fine("I've already expanded from " + node);
        } else {
            componentNodes.add(node);
//            LOGGER.finer("Adding " + node + " to component");
            Vector adjacentEdges = findAdjacentEdges(node, edges);  //yes, I know node.getEdges() should do this, but this method could be out of data by the time I use this in AutoClust
            adjacentEdges.trimToSize();
            componentEdges.addAll(adjacentEdges);  
//            LOGGER.finer("Adding " + adjacentEdges + " to component");
            
            Iterator aeIt = adjacentEdges.iterator();
            while (aeIt.hasNext()){
                Edge next = (Edge) aeIt.next();
//                LOGGER.finer("looking at edge " + next);
                Node additionalNode = next.getOtherNode(node);
//                LOGGER.finer("its other node is " + additionalNode);
                if (additionalNode == null){
                    throw new RuntimeException("I tried to get the other node of this edge " + next + " but it doesn't have " + node);
                }
                expandComponent(additionalNode, edges, componentNodes, componentEdges);
            }
            adjacentEdges.clear();
        }
    }
    
    public static Vector findAdjacentEdges(final Node node, final Collection edges){
        Vector ret = new Vector();
        Iterator it = edges.iterator();
        while (it.hasNext()){
            Edge next = (Edge) it.next();
            if ((next.getNodeA().equals(node)) || (next.getNodeB().equals(node))){
                ret.add(next);
            }
        }
        return ret;
    }
    
    public static DelaunayNode[] featureCollectionToNodeArray(SimpleFeatureCollection fc){
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
    
    public static boolean arrayContains(DelaunayNode node, DelaunayNode[] nodes, int index){
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
    
}
