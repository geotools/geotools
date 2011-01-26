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
package org.geotools.graph.build.line;

import org.geotools.graph.build.basic.BasicDirectedGraphBuilder;
import org.geotools.graph.structure.Node;
import org.geotools.graph.structure.line.BasicDirectedXYNode;

/**
 * An implementation of GraphBuilder extended from BasicDirectedGraphBuilder 
 * used to build graphs representing directed line networks.  
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 * @source $URL$
 */
public class BasicDirectedLineGraphBuilder extends BasicDirectedGraphBuilder {
 
  /**
   * Returns a node of type BasicDirectedXYNode.
   * 
   * @see BasicDirectedXYNode
   * @see org.geotools.graph.build.GraphBuilder#buildNode()
   */
  public Node buildNode() {
    return(new BasicDirectedXYNode());
  }
 
}
