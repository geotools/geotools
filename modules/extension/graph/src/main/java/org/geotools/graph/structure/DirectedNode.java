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
package org.geotools.graph.structure;

import java.util.List;

/**
 * Represents a node in a directed graph. A directed node differentiates 
 * between adjacent edges that start at the node and those adjacent edges that 
 * terminate at the node. The former are referred to as "<B>in</B>" edges, and 
 * the latter "<B>out</B>" edges.
 * 
 * @see DirectedGraph
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 * @source $URL$
 */
public interface DirectedNode extends Node, DirectedGraphable {
  
  /**
   * Adds an edge to the <B>in</B> adjacency list of the node. 
   * 
   * @param e A directed edge that terminates at the node.
   * 
   * @see Node#add(Edge)
   */
  public void addIn(DirectedEdge e);
  
  /**
   * Adds an edge to the <B>out</B> adjacency list of the node. 
   * 
   * @param e A directed edge that originates from the node.
   * 
   * @see Node#add(Edge)
   */
  public void addOut(DirectedEdge e);
  
  /**
   * Removes an edge from the <B>in</B> adjacency list of the node. 
   * 
   * @param e A directed edge that terminates at the node.
   * 
   * @see Node#remove(Edge)
   */
  public void removeIn(DirectedEdge e);
  
  /**
   * Removes an edge from the <B>out</B> adjacency list of node. 
   * 
   * @param e A directed edge that originates from the node.
   * 
   * @see Node#remove(Edge)
   */
  public void removeOut(DirectedEdge e);
  
  /**
   * Returns an edge that terminates at the node and originates from a 
   * specified node. <BR>
   * <BR>
   * Note: It is possible for two nodes to share multiple edges between them. In
   * this case, getInEdges(Node other) can be used to obtain a complete list. 
   *
   * @param other The originating node.
   * 
   * @return The first edge found to terminate at the node and originate from 
   * the specefied node. 
   * 
   * @see Node#getEdge(Node)
   */
  public Edge getInEdge(DirectedNode other);
  
  /**
   * Returns all edges that terminate at the node and originate from a 
   * specified node. 
   * 
   * @param other The originating node.
   * 
   * @return All edges found to terminate at the node and originate from the
   * specified node.
   * 
   * @see Node#getEdges(Node)
   */
  public List getInEdges(DirectedNode other);
  
  /**
   * Returns the <B>in</B> adjacency list of the node.
   * 
   * @return A list of edges that terminate at the node.
   * 
   * @see Node#getEdges()
   */
  public List getInEdges();
  
  /**
   * Returns an edge that originates at the node and terminates at a 
   * specified node. <BR>
   * <BR>
   * Note: It is possible for two nodes to share multiple edges between them. In
   * this case, getOutEdges(Node other) can be used to obtain a complete list. 
   *
   * @param other The terminating node.
   * 
   * @return The first edge found to originate at the node and terminate at 
   * the specefied node. 
   * 
   * @see Node#getEdge(Node)
   */
  public Edge getOutEdge(DirectedNode other);
  
  /**
   * Returns all edges that originate at the node and terminate from at 
   * specified node. 
   * 
   * @param other The temimnating node.
   * 
   * @return All edges found to originate at the node and terminate at the
   * specified node.
   *
   * @see Node#getEdges(Node)
   */
  public List getOutEdges(DirectedNode other);
  
  /**
   * Returns the <B>out</B> adjacency list of the node.
   * 
   * @return A list of edges originating at the node.
   * 
   * @see Node#getEdges()
   */
  public List getOutEdges();
  
  /**
   * Returns the <B>in</B> degree of the node.
   * 
   * @return The number of edges that terminate at the node.
   */
  public int getInDegree();
  
  /**
   * Returns the <B>out</B> degree of the node.
   * 
   * @return The number of edges that originate at the node. 
   */
  public int getOutDegree();

}
