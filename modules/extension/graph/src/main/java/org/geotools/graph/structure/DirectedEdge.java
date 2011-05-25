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

/**
 * Represents an edge in a directed graph. A directed edge catagorizes its 
 * nodes as originating (in node), and terminating (out node). The edge is an 
 * <B>out edge</B> of its <B>in node</B> and an <B>in edge</B> to its <B>out 
 * node</B>.
 * 
 * @see DirectedGraph
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 * 
 *
 * @source $URL$
 */
public interface DirectedEdge extends Edge, DirectedGraphable {
  
  /**
   * Returns the originating (in) node of the edge.
   * 
   * @return The directed node at the source of the  edge.
   */
  public DirectedNode getInNode();
  
  /**
   * Returns the terminating (out) node of the edge.
   * 
   * @return The directed node at the destination of the edge. 
   */
  public DirectedNode getOutNode();
  
}
