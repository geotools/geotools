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

import java.util.HashMap;

import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.GraphGenerator;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;

/**
 * An implementation of GraphGenerator. Graphs are generated as follows:<BR>
 * <UL>
 * <LI>Objects added to the generator are 2 element object arrays.</LI>
 * <LI>The elements of the array represent the objects modelled by the nodes.
 * <LI>The object array itself is the object modelled by the edges.
 * <LI>As each object array is added to the generator:
 *   <UL>
 *   <LI>A node lookup table is queried using the elements of the object array.
 *   <LI>If a node lookup returns null, a new node is created for its respective
 *       object.
 *   <LI>A new edge is created incident to the two looked up nodes.
 *   <LI>The underlying object of the edge is set to the be object array. 
 *   </UL>
 * </UL> 
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 * @source $URL$
 */
public class BasicGraphGenerator implements GraphGenerator {

  /** The underlying builder **/
  private GraphBuilder m_builder;
  
  /** object to node lookup table **/
  private HashMap m_obj2graphable;
  
  /** 
   * Constructs a new generator.
   */
  public BasicGraphGenerator() {
    m_obj2graphable = new HashMap();
    setGraphBuilder(new BasicGraphBuilder());  
  }
  
  /**
   * @see GraphGenerator#add(Object)
   */
  public Graphable add(Object obj) {
    Object[] objs = (Object[])obj;
    
    Node n1, n2;
    
    //look up first node and create if necessary
    if ((n1 = (Node)m_obj2graphable.get(objs[0])) == null) {
      n1 = getGraphBuilder().buildNode();
      n1.setObject(objs[0]);
      
      getGraphBuilder().addNode(n1);
      m_obj2graphable.put(objs[0], n1);
    }
    
    //look up second node and create if necessary
    if ((n2 = (Node)m_obj2graphable.get(objs[1])) == null) {
      n2 = getGraphBuilder().buildNode();
      n2.setObject(objs[1]);
      
      getGraphBuilder().addNode(n2);
      m_obj2graphable.put(objs[1], n2);
    }  
    
    //create edge and set underlying object
    Edge e = getGraphBuilder().buildEdge(n1,n2);
    e.setObject(obj);
    
    getGraphBuilder().addEdge(e);
    
    return(e);  
  }

  /**
   * @see GraphGenerator#get(Object)
   */
  public Graphable get(Object obj) {
    Object[] objs = (Object[])obj;
    Node n1 = (Node)m_obj2graphable.get(objs[0]);
    Node n2 = (Node)m_obj2graphable.get(objs[1]);
    
    return(n1.getEdge(n2));  
  }
  
  /**
   * @see GraphGenerator#remove(Object)
   */
  public Graphable remove(Object obj) {
    Object[] objs = (Object[])obj;
    
    Node n1 = (Node)m_obj2graphable.get(objs[0]);
    Node n2 = (Node)m_obj2graphable.get(objs[1]);
    
    Edge e = n1.getEdge(n2);
    getGraphBuilder().removeEdge(e);
    
    return(e);
  }

  /**
   * @see GraphGenerator#setGraphBuilder(GraphBuilder)
   */
  public void setGraphBuilder(GraphBuilder builder) {
    m_builder = builder;
  }

  /**
   * @see GraphGenerator#getGraphBuilder()
   */
  public GraphBuilder getGraphBuilder() {
    return(m_builder);
  }
  
  /**
   * @see GraphGenerator#getGraph()
   */
  public Graph getGraph() {
    return(m_builder.getGraph());
  }
  
}
