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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Node;
import org.geotools.graph.structure.basic.BasicGraph;

/**
 *
 *    This class implements the AUTOCLUST algorithm of Estivill-Castro and Lee (2002)
 *    "Argument free clustering for large spatial point-data sets via boundary extraction
 *    from Delaunay Diagram" in Computers, Environment and Urban Systems, 26:315-334.
 *
 * @author jfc173
 *
 * @source $URL$
 */
public class AutoClust {        
    
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.graph");
    
    /** Creates a new instance of AutoClust */
    public AutoClust() {
    }
    
    public static Graph runAutoClust(Graph d){
        //this is assuming d is a delaunay triangulation.  If it isn't, results are unspecified.
        //Such a diagram can be obtained through org.geotools.graph.util.delaunay.DelaunayTriangulator.
            
        HashMap map = new HashMap();
        Collection nodes = d.getNodes();
        Collection edges = d.getEdges();
        showGraph(nodes, edges, 0);
        Iterator nodeIt = nodes.iterator();
        double[] localDevs = new double[nodes.size()];
        int index = 0;
        while (nodeIt.hasNext()){
            DelaunayNode next = (DelaunayNode) nodeIt.next();
            AutoClustData acd = new AutoClustData();
            List localEdges = AutoClustUtils.findAdjacentEdges(next, edges);
            double totalLength = 0;
            Iterator edgeIt = localEdges.iterator();
            while (edgeIt.hasNext()){
                DelaunayEdge nextEdge = (DelaunayEdge) edgeIt.next();
                totalLength = totalLength + nextEdge.getEuclideanDistance();
            }
            double meanLength = totalLength/localEdges.size();
            double sumOfSquaredDiffs = 0;
            Iterator anotherEdgeIt = localEdges.iterator();            
            while (anotherEdgeIt.hasNext()){                
                DelaunayEdge nextEdge = (DelaunayEdge) anotherEdgeIt.next();
                sumOfSquaredDiffs = sumOfSquaredDiffs + Math.pow((nextEdge.getEuclideanDistance() - meanLength), 2);                
            }
            double variance = sumOfSquaredDiffs/(localEdges.size());
            double stDev = Math.sqrt(variance);
            localDevs[index] = stDev;
            index++;            
            acd.setLocalMean(meanLength);
            acd.setLocalStDev(stDev);
            map.put(next, acd);
        }
        double total = 0;
        for (int i = 0; i < localDevs.length; i++){
            total = total + localDevs[i];
        }
        double meanStDev = total/localDevs.length;
        
        //these three are for coloring the graph in the poster, not for algorithmic purposes
        Vector allShortEdges = new Vector();
        Vector allLongEdges = new Vector();
        Vector allOtherEdges = new Vector();
        
        Iterator anotherNodeIt = nodes.iterator();
        while (anotherNodeIt.hasNext()){
            DelaunayNode next = (DelaunayNode) anotherNodeIt.next();
            List localEdges = AutoClustUtils.findAdjacentEdges(next, edges);
            AutoClustData acd = (AutoClustData) map.get(next);
            Iterator edgeIt = localEdges.iterator();
            Vector shortEdges = new Vector();
            Vector longEdges = new Vector();
            Vector otherEdges = new Vector();
            LOGGER.fine("local mean is " + acd.getLocalMean());
            LOGGER.fine("mean st dev is " + meanStDev);
            while (edgeIt.hasNext()){
                DelaunayEdge nextEdge = (DelaunayEdge) edgeIt.next();
                double length = nextEdge.getEuclideanDistance();
                if (length < acd.getLocalMean() - meanStDev){
                    shortEdges.add(nextEdge);
                    LOGGER.finer(nextEdge + ": length " + nextEdge.getEuclideanDistance() + " is short");
                } else if (length > acd.getLocalMean() + meanStDev){
                    longEdges.add(nextEdge);
                    LOGGER.finer(nextEdge + ": length " + nextEdge.getEuclideanDistance() + " is long");
                } else {
                    otherEdges.add(nextEdge);
                    LOGGER.finer(nextEdge + ": length " + nextEdge.getEuclideanDistance() + " is medium");
                }
            }
            acd.setShortEdges(shortEdges);
            acd.setLongEdges(longEdges);
            acd.setOtherEdges(otherEdges);            
                     
            allLongEdges.addAll(longEdges);
            allShortEdges.addAll(shortEdges);
            allOtherEdges.addAll(otherEdges);
        }
        
        //for the poster
        Graph gp = new BasicGraph(nodes, edges);
        javax.swing.JFrame frame = new javax.swing.JFrame();
        GraphViewer viewer = new GraphViewer();
        viewer.setLongEdges(allLongEdges);
        viewer.setShortEdges(allShortEdges);
        viewer.setOtherEdges(allOtherEdges);
        viewer.setColorEdges(true);
        viewer.setGraph(gp);
        frame.getContentPane().add(viewer);
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setSize(new java.awt.Dimension(800, 800));
        frame.setTitle("Assigned edge categories");
        frame.setVisible(true); 
        
        //Phase I
        Iterator nodeIt3 = nodes.iterator();
        while (nodeIt3.hasNext()){
            DelaunayNode next = (DelaunayNode) nodeIt3.next();
            AutoClustData acd = (AutoClustData) map.get(next);
            List shortEdges = acd.getShortEdges();
            List longEdges = acd.getLongEdges();
            edges.removeAll(shortEdges);
            LOGGER.finer("removed " + shortEdges);
            edges.removeAll(longEdges);
            LOGGER.finer("removed " + longEdges);
        }
        
        LOGGER.fine("End of phase one and ");
        LOGGER.fine("Nodes are " + nodes);
        LOGGER.fine("Edges are " + edges);                
        showGraph(nodes, edges, 1);        
        Vector connectedComponents = AutoClustUtils.findConnectedComponents(nodes, edges);
        
        //Phase II
        Iterator nodeIt4 = nodes.iterator();
        while (nodeIt4.hasNext()){
            DelaunayNode next = (DelaunayNode) nodeIt4.next();
            AutoClustData acd = (AutoClustData) map.get(next);
            List shortEdges = acd.getShortEdges();
            if (!(shortEdges.isEmpty())){
                Vector shortlyConnectedComponents = new Vector();
                Iterator shortIt = shortEdges.iterator();
                while (shortIt.hasNext()){
                    Edge nextEdge = (Edge) shortIt.next();
                    Node other = nextEdge.getOtherNode(next);
                    Graph g = getMyComponent(other, connectedComponents);
                    if (!(shortlyConnectedComponents.contains(g))){
                        shortlyConnectedComponents.add(g);
                    }
                }
                Graph cv = null;
                if (shortlyConnectedComponents.size() > 1){
                    Iterator sccIt = shortlyConnectedComponents.iterator();
                    int maxSize = 0;
                    while (sccIt.hasNext()){
                        Graph nextGraph = (Graph) sccIt.next();
                        int size = nextGraph.getNodes().size();
                        if (size > maxSize){
                            maxSize = size;
                            cv = nextGraph;
                        }
                    }
                } else {
                    cv = (Graph) shortlyConnectedComponents.get(0);
                }
                Iterator shortIt2 = shortEdges.iterator();
                while (shortIt2.hasNext()){
                    Edge nextEdge = (Edge) shortIt2.next();
                    Node other = nextEdge.getOtherNode(next);
                    if (cv.equals(getMyComponent(other, shortlyConnectedComponents))){
                        edges.add(nextEdge);
                    }
                }
            } //end if shortEdges isn't empty
            Graph gr = getMyComponent(next, connectedComponents);
            if (gr.getNodes().size() == 1){
                Vector shortlyConnectedComponents = new Vector();
                Iterator shortIt = shortEdges.iterator();
                while (shortIt.hasNext()){
                    Edge nextEdge = (Edge) shortIt.next();
                    Node other = nextEdge.getOtherNode(next);
                    Graph g = getMyComponent(other, connectedComponents);
                    if (!(shortlyConnectedComponents.contains(g))){
                        shortlyConnectedComponents.add(g);
                    }
                }
                if (shortlyConnectedComponents.size() == 1){
                    edges.addAll(shortEdges);
                }
            }
        } //end nodeIt4 while loop.
        
        LOGGER.fine("End of phase two and ");
        LOGGER.fine("Nodes are " + nodes);
        LOGGER.fine("Edges are " + edges);   
        showGraph(nodes, edges, 2);
        connectedComponents = AutoClustUtils.findConnectedComponents(nodes, edges);
        
        //Phase III
        Iterator nodeIt5 = nodes.iterator();
        while (nodeIt5.hasNext()){
            DelaunayNode next = (DelaunayNode) nodeIt5.next();
            Vector edgesWithinTwo = new Vector();
            List adjacentEdges = AutoClustUtils.findAdjacentEdges(next, edges); //yes, next.getEdges() could work, but there's no guarantee that next's edge list is current anymore
            edgesWithinTwo.addAll(adjacentEdges);
            Iterator adjacentIt = adjacentEdges.iterator();
            while (adjacentIt.hasNext()){
                Edge nextEdge = (Edge) adjacentIt.next();
                Node other = nextEdge.getOtherNode(next);
                List adjacentToOther = AutoClustUtils.findAdjacentEdges(other, edges);  //yes, other.getEdges() could work, but there's no guarantee that other's edge list is current anymore
                Iterator atoIt = adjacentToOther.iterator();
                while (atoIt.hasNext()){
                    Edge nextEdge2 = (Edge) atoIt.next();
                    if (!(edgesWithinTwo.contains(nextEdge2))){
                        edgesWithinTwo.add(nextEdge2);
                    }
                }
            }
            double totalLength = 0;
            Iterator ewtIt = edgesWithinTwo.iterator();
            while (ewtIt.hasNext()){
                totalLength = totalLength + ((DelaunayEdge) ewtIt.next()).getEuclideanDistance();
            }
            double local2Mean = totalLength/edgesWithinTwo.size();
            
            Iterator ewtIt2 = edgesWithinTwo.iterator();
            while (ewtIt2.hasNext()){
                DelaunayEdge dEdge = (DelaunayEdge) ewtIt2.next();
                if (dEdge.getEuclideanDistance() > (local2Mean + meanStDev)){
                    edges.remove(dEdge);
                }
            }                        
        } //end nodeIt5 loop
        
        LOGGER.fine("End of phase three and ");
        LOGGER.fine("Nodes are " + nodes);
        LOGGER.fine("Edges are " + edges);    
        showGraph(nodes, edges, 3);
        connectedComponents = AutoClustUtils.findConnectedComponents(nodes, edges);
        
        return new BasicGraph(nodes, edges);
    }
    
    private static Graph getMyComponent(Node node, Vector components){
        Iterator it = components.iterator();
        Graph ret = null;
        boolean found = false;
        while ((it.hasNext()) && (!(found))){
            Graph next = (Graph) it.next();
            if (next.getNodes().contains(node)){
                found = true;
                ret = next;
            }
        }
        if (ret == null){
            throw new RuntimeException("Couldn't find the graph component containing node: " + node);
        }
        return ret;
    }
    
    private static void showGraph(Collection nodes, Collection edges, int phase){
        Graph g = new BasicGraph(nodes, edges);
        javax.swing.JFrame frame = new javax.swing.JFrame();
        GraphViewer viewer = new GraphViewer();
        viewer.setGraph(g);
        frame.getContentPane().add(viewer);
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setSize(new java.awt.Dimension(800, 800));
        frame.setTitle("Phase " + phase);
        frame.setVisible(true); 
    }
    
}
