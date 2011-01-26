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
package org.geotools.graph.traverse.standard;

import java.util.Iterator;

import org.geotools.graph.structure.DirectedGraphable;
import org.geotools.graph.structure.DirectedNode;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.basic.AbstractGraphIterator;
import org.geotools.graph.util.FIFOQueue;
import org.geotools.graph.util.Queue;


/**
 * @source $URL$
 */
public class DirectedBreadthFirstTopologicalIterator 
  extends AbstractGraphIterator {

  private Queue m_queue;
  
  public void init(Graph graph, GraphTraversal traversal) {
    //create queue
    m_queue = buildQueue(graph);
    
    //initialize nodes
    graph.visitNodes(
      new GraphVisitor() {
        public int visit(Graphable component) {
          DirectedNode node = (DirectedNode)component;
          
          node.setVisited(false);
          node.setCount(0);
          
          if (node.getInDegree() == 0) m_queue.enq(node);
          
          return(0);  
        }
      } 
    );
  }

  public Graphable next(GraphTraversal traversal) {
    return(!m_queue.isEmpty() ? (Graphable)m_queue.deq() : null); 
  }

  public void cont(Graphable current, GraphTraversal traversal) {
    //increment the count of all adjacent nodes by one
    // if the result count equal to the degree, place it into the queue
    DirectedGraphable directed = (DirectedGraphable)current;
    for (Iterator itr = directed.getOutRelated(); itr.hasNext();) {
      DirectedNode related = (DirectedNode)itr.next();
      if (!traversal.isVisited(related)) {
        related.setCount(related.getCount()+1);  
        if (related.getInDegree() == related.getCount()) m_queue.enq(related);
      }  
    }
  }

  public void killBranch(Graphable current, GraphTraversal traversal) {
    //do nothing
  }
  
  protected Queue buildQueue(Graph graph) {
    return(new FIFOQueue(graph.getNodes().size()));  
  }
}
