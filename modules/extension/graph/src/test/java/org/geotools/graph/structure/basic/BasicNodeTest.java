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

import java.util.Iterator;

import junit.framework.TestCase;

import org.geotools.graph.structure.Edge;

public class BasicNodeTest extends TestCase {

  private BasicNode m_node;
  private BasicNode m_otherNode;
  private Edge m_edge;
  private Edge m_otherEdge;
  private Edge m_loop;
  
  public BasicNodeTest(String name) {
    super(name);	
  }
  
  protected void setUp() throws Exception {
    super.setUp();
    m_node = new BasicNode();
    m_otherNode = new BasicNode();
    
    m_edge = new BasicEdge(m_node,m_otherNode);
    m_otherEdge = new BasicEdge(m_node, m_otherNode);
    m_loop = new BasicEdge(m_node, m_node);
  }
  
  public void test_add() {
    //test addition of single edge
    m_node.add(m_edge);
    assertTrue(m_node.getEdges().contains(m_edge));	
    
    //test addition of multiple edges, same edge 
    m_node.add(m_edge);
    assertTrue(m_node.getEdges().size() == 2);
    
    //test addition of multiple edges, different
    Edge other = new BasicEdge(m_node, m_otherNode);
    m_node.add(other);
    assertTrue(m_node.getEdges().size() == 3);
    assertTrue(m_node.getEdges().contains(m_edge));
    assertTrue(m_node.getEdges().contains(other));
  }
	
	public void test_remove() {
    m_node.add(m_edge);
    assertTrue(m_node.getEdges().contains(m_edge));		
   
    //test removal of a single edge 
    m_node.remove(m_edge);
    assertTrue(m_node.getEdges().size() == 0);
    
    //test removal of multiple edges, same 
    m_node.add(m_edge);
    m_node.add(m_edge);
    assertTrue(m_node.getEdges().size() == 2);
    
    m_node.remove(m_edge);
    assertTrue(m_node.getEdges().size() == 1);
    
    m_node.remove(m_edge);
    assertTrue(m_node.getEdges().size() == 0);
    
    //test removal of multiple edges, different
    m_node.add(m_edge);
    m_node.add(m_otherEdge);
    
    m_node.remove(m_edge);
    assertTrue(m_node.getEdges().size() == 1);
    
    m_node.remove(m_otherEdge);
    assertTrue(m_node.getEdges().size() == 0);
	}
	
	public void test_getDegree() {
		//intially degree should be zero
    assertTrue(m_node.getDegree() == 0);
    
    //add single edge making degree 1
    m_node.add(m_edge);
    assertTrue(m_node.getDegree() == 1);
    
    //add the same edge, should be degree 2
    m_node.add(m_edge);
    assertTrue(m_node.getDegree() == 2);
    
    //add different edge, should be degree 3
    m_node.add(m_otherEdge);
    assertTrue(m_node.getDegree() == 3);		
	
	  m_node.getEdges().clear();
	  
	  //add a loop edge, degree should be two, but size will be one
	  m_node.add(m_loop);
	  assertTrue(m_node.getEdges().size() == 1);
	  assertTrue(m_node.getDegree() == 2);
	}
	
	public void test_getEdge() {
	  m_node.add(m_edge);
	  assertSame(m_edge, m_node.getEdge(m_otherNode));  	
	
	  //add another edge that has the same other node, since the underlying
	  // structure is a list, first one should be returned
	  m_node.add(m_otherEdge);
	  assertSame(m_edge, m_node.getEdge(m_otherNode));
	  
	  //remove first edge
	  m_node.remove(m_edge);
	     
	  //the call should now return other edge
	  assertSame(m_otherEdge, m_node.getEdge(m_otherNode));
	}
  
  public void test_getEdges() {
    m_node.add(m_edge);
    assertTrue(m_node.getEdges(m_otherNode).contains(m_edge));
    
    //add the same edge
    m_node.add(m_edge);
    assertTrue(m_node.getEdges(m_otherNode).size() == 2);
    
    //add a different edge
    m_node.add(m_otherEdge);
    assertTrue(m_node.getEdges(m_otherNode).contains(m_edge));
    assertTrue(m_node.getEdges(m_otherNode).contains(m_otherEdge));
  }
  
  public void test_getRelated() {
    // no edges should be empty
    assertTrue(!m_node.getRelated().hasNext());
    
    //single edge
    m_node.add(m_edge);
    Iterator itr = m_node.getRelated();
    assertSame(itr.next(), m_otherNode);
    assertTrue(!itr.hasNext());
    
    //multiple edges, same, same node should be returned twice
    m_node.add(m_edge);
    itr = m_node.getRelated();
    assertSame(itr.next(), m_otherNode);
    assertSame(itr.next(), m_otherNode);
    assertTrue(!itr.hasNext());
    
  }
}
