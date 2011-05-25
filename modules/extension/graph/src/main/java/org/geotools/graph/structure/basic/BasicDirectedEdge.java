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
package org.geotools.graph.structure.basic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.geotools.graph.structure.DirectedEdge;
import org.geotools.graph.structure.DirectedNode;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Node;

/**
 * Basic implementation of DirectedEdge.
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 * @source $URL$
 */
public class BasicDirectedEdge extends BasicGraphable implements DirectedEdge {

  /** in node **/
  private DirectedNode m_in;
  
  /** out node **/
  private DirectedNode m_out;
  
  /** Contstructs a new DirectedEdge.
   * 
   * @param in The in node of the edge.
   * @param out The out node of the edge.
   */
  public BasicDirectedEdge(DirectedNode in, DirectedNode out) {
    super();
    m_in = in;
    m_out = out;
  }
  
  /**
   * @see DirectedEdge#getInNode()
   */
  public DirectedNode getInNode() {
    return(m_in);
  }


  /**
   * @see DirectedEdge#getOutNode()
   */
  public DirectedNode getOutNode() {
    return(m_out);
  }

  /**
   * Returns the in node.
   * 
   * @see Edge#getNodeA()
   */
  public Node getNodeA() {
    return(m_in);
  }

  /**
   * Returns the out node.
   * 
   * @see Edge#getNodeB()
   */
  public Node getNodeB() {
    return(m_out);
  }

  /**
   * @see Edge#getOtherNode(Node)
   */
  public Node getOtherNode(Node node) {
    return(m_in.equals(node) ? m_out : m_out.equals(node) ? m_in : null);
  }

  /**
   * Removes the edge from the out list of the in node and from the in list of
   * the out node. Nodes are switched and then the edge is added to the in list
   * of the new out node, and to the out list of the new in node.
   * 
   * @see Edge#reverse()
   * 
   */
  public void reverse() {
    //remove edge from adjacent nodes
    m_in.removeOut(this);
    m_out.removeIn(this);
    
    //swap nodes
    DirectedNode tmp = m_in;
    m_in = m_out;
    m_out = tmp;
   
    //re add nodes
    m_in.addOut(this);
    m_out.addIn(this);
  }

  /**
   * Returns an iterator over all edges incident to both the in and out nodes.
   * 
   * @see org.geotools.graph.structure.Graphable#getRelated()
   */
  public Iterator getRelated() {
    HashSet related = new HashSet();
    
    //add all edges incident to both nodes
    related.addAll(m_in.getEdges());
    related.addAll(m_out.getEdges());
    
    //remove this edge
    related.remove(this);
    
    return(related.iterator());
    
//    
//    ArrayList related = new ArrayList();
//    
//    related.addAll(m_in.getInEdges());
//    
//    //add out edges, look for an opposite edge, it will have already 
//    // been added so dont add it
//    for (Iterator itr = m_out.getOutEdges().iterator(); itr.hasNext();) {
//      DirectedEdge de = (DirectedEdge)itr.next();
//      switch(de.compareNodes(this)) {
//        case OPPOSITE_NODE_ORIENTATION: continue;
//      }
//      related.add(de);	
//    }
//    
//    //look for duplicate edges (same direction) if not equal add
//    // dont add opposite edges
//    // dont add loops
//    for (Iterator itr = m_in.getOutEdges().iterator(); itr.hasNext();) {
//      DirectedEdge de = (DirectedEdge)itr.next();
//      switch(de.compareNodes(this)) {
//      	case EQUAL_NODE_ORIENTATION:
//      	  if (!de.equals(this)) 
//      	    related.add(de);
//      	  continue;
//        case OPPOSITE_NODE_ORIENTATION: 
//          continue;
//        case UNEQUAL_NODE_ORIENTATION: 
//          if (de.getNodeA().equals(de.getNodeB())) continue;
//          related.add(de);
//      }
//    }
//    
//    return(related.iterator());
  }
  
  /**
   * Returns an iterator over the in edges of the in node.
   * 
   * @see org.geotools.graph.structure.DirectedGraphable#getInRelated()
   */
  public Iterator getInRelated() {
    ArrayList in = new ArrayList();
    for (Iterator itr = m_in.getInEdges().iterator(); itr.hasNext();) {
      DirectedEdge de = (DirectedEdge)itr.next();
      //this check has to be because the edge could be a loop in which case
      // it is in related to itself
      if (!de.equals(this)) in.add(de);  
    }
    return(in.iterator());  
  }

  /**
   * Returns an iterator over the out edges of the out node.
   * 
   * @see org.geotools.graph.structure.DirectedGraphable#getOutRelated() 
   */
  public Iterator getOutRelated() {
     ArrayList out = new ArrayList();
    for (Iterator itr = m_out.getOutEdges().iterator(); itr.hasNext();) {
      DirectedEdge de = (DirectedEdge)itr.next();
      //this check has to be because the edge could be a loop in which case
      // it is in related to itself
      if (!de.equals(this)) out.add(de);  
    }
    return(out.iterator());    
  }

  /**
   * @see Edge#compareNodes(Edge)
   */
  public int compareNodes(Edge other) {
    if (other instanceof DirectedEdge) {
      DirectedEdge de = (DirectedEdge)other;
      if (de.getInNode().equals(m_in) && de.getOutNode().equals(m_out)) 
        return(EQUAL_NODE_ORIENTATION);
      if (de.getInNode().equals(m_out) && de.getOutNode().equals(m_in)) 
        return(OPPOSITE_NODE_ORIENTATION);	
    }
    return(UNEQUAL_NODE_ORIENTATION);
  }
}
