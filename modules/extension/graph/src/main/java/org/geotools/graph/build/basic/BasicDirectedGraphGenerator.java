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

/**
 * An implementation of GraphGenerator used to generate directed graphs.
 * Graphs are generated as follows:<BR>
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
 * @source $URL$
 */
public class BasicDirectedGraphGenerator extends BasicGraphGenerator {
 
  /**
   * Constructs a new generator.
   */
  public BasicDirectedGraphGenerator() {
    super();
    setGraphBuilder(new BasicDirectedGraphBuilder());  
  } 
}
