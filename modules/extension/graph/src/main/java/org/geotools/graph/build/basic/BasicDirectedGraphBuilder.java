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
package org.geotools.graph.build.basic;

import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.structure.DirectedEdge;
import org.geotools.graph.structure.DirectedNode;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Node;
import org.geotools.graph.structure.basic.BasicDirectedEdge;
import org.geotools.graph.structure.basic.BasicDirectedGraph;
import org.geotools.graph.structure.basic.BasicDirectedNode;

/**
 * An implementation of GraphBuilder used to build directed graphs.
 * 
 * @see org.geotools.graph.structure.DirectedGraph
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 * @source $URL$
 */
public class BasicDirectedGraphBuilder extends BasicGraphBuilder {
 
  /**
   * Builds a directed node.
   * 
   * @see DirectedNode
   * @see GraphBuilder#buildNode()
   */
  public Node buildNode() {
    return(new BasicDirectedNode());
  }
  
  /**
   * Builds a directed edge.
   * 
   * @see DirectedEdge
   * @see GraphBuilder#buildEdge()
   */
  public Edge buildEdge(Node nodeA, Node nodeB) {
    return(new BasicDirectedEdge((DirectedNode)nodeA, (DirectedNode)nodeB));
  }
  
  /**
   * Adds a directed edge to the graph.
   * 
   * @see DirectedEdge
   * @see GraphBuilder#addEdge(Edge)
   */
  public void addEdge(Edge edge) {
    DirectedEdge de = (DirectedEdge)edge;
    de.getInNode().addOut(de);
    de.getOutNode().addIn(de);
    getEdges().add(de);
  }
  
  /**
   * Creates a directed graph object.
   */
  protected Graph buildGraph() {
    return(new BasicDirectedGraph(getNodes(), getEdges()));      
  }
  
}
