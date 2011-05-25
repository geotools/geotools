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
package org.geotools.graph.traverse.basic;

import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.traverse.GraphIterator;
import org.geotools.graph.traverse.GraphWalker;

/**
 * 
 * TODO: DOCUMENT ME!
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 * @source $URL$
 */
public class StagedGraphTraversal extends BasicGraphTraversal {

  private int m_stage;
  
  public StagedGraphTraversal(
    Graph graph, GraphWalker walker, GraphIterator iterator
  ) {
    super(graph, walker, iterator);
    m_stage = 0;
  }
  
  public void init() {
    //initialize the nodes of the graph by setting counts to 0
    getGraph().visitNodes(
      new GraphVisitor() {
        public int visit(Graphable component) {
          component.setCount(0);
          return(0);
        }
      }
    );
  }

  public boolean isVisited(Graphable g) {
    return(g.getCount() == m_stage);
  }
  
  public void setVisited(Graphable g, boolean visited) {
    g.setCount(visited ? m_stage : -1);
  }

}
