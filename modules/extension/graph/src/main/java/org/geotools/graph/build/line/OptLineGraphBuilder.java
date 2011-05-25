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

import org.geotools.graph.build.opt.OptGraphBuilder;
import org.geotools.graph.structure.Node;
import org.geotools.graph.structure.line.OptXYNode;

/**
 * An inmplementation extended from OptGraphBuilder used to build optimized
 * components for line networks.
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 * @source $URL$
 */
public class OptLineGraphBuilder extends OptGraphBuilder {

  /**
   * Returns a node of type OptXYNode.
   * 
   * @see OptXYNode
   * @see org.geotools.graph.build.GraphBuilder#buildNode()
   */
  public Node buildNode() {
    return(new OptXYNode());  
  }
 
}
