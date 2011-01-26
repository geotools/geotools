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

public class BasicEdgeTest extends TestCase {

  private BasicNode m_nodeA;
  private BasicNode m_nodeB;
  private BasicNode m_otherNode1;
  private BasicNode m_otherNode2;
  private BasicNode m_otherNode3;
  private BasicNode m_otherNode4;
  
  private BasicEdge m_edge;
  private BasicEdge m_other1;
  private BasicEdge m_other2;
  private BasicEdge m_other3;
  private BasicEdge m_other4;
  
  private BasicEdge m_same;
  private BasicEdge m_same2;
  private BasicEdge m_diff;
  private BasicEdge m_opp;
  private BasicEdge m_loopA;
  private BasicEdge m_loopB;
  
  public BasicEdgeTest(String name) {
    super(name);  
  }
  
  protected void setUp() throws Exception {
    m_nodeA = new BasicNode();
    m_nodeB = new BasicNode();
    m_otherNode1 = new BasicNode();
    m_otherNode2 = new BasicNode();
    m_otherNode3 = new BasicNode();
    m_otherNode4 = new BasicNode();
    
    m_edge = new BasicEdge(m_nodeA, m_nodeB);  
    m_nodeA.add(m_edge);
    m_nodeB.add(m_edge);
    
    m_other1 = new BasicEdge(m_nodeA, m_otherNode1);
    m_nodeA.add(m_other1);
    m_otherNode1.add(m_other1);
    
    m_other2 = new BasicEdge(m_nodeB, m_otherNode2);
    m_nodeB.add(m_other2);
    m_otherNode2.add(m_other2);
    
    m_other3 = new BasicEdge(m_otherNode3, m_nodeA);
    m_otherNode3.add(m_other3);
    m_nodeA.add(m_other3);
    
    m_other4 = new BasicEdge(m_otherNode4, m_nodeB);
    m_otherNode4.add(m_other4);
    m_nodeB.add(m_other4);
    
    //dont add these to the graph yet
    m_same = new BasicEdge(m_nodeA, m_nodeB);
    m_same2 = new BasicEdge(m_nodeA, m_nodeB);
    m_opp = new BasicEdge(m_nodeB, m_nodeA);
    m_loopA = new BasicEdge(m_nodeA, m_nodeA);
    m_loopB = new BasicEdge(m_nodeB, m_nodeB); 
  }
  
  public void test_getNodeA() {
    assertSame(m_edge.getNodeA(), m_nodeA);	
  }
  
  public void test_getNodeB() {
  	assertSame(m_edge.getNodeB(), m_nodeB);
  }
  
  public void test_getOtherNode() {
    assertSame(m_edge.getOtherNode(m_nodeA), m_nodeB);
    assertSame(m_edge.getOtherNode(m_nodeB), m_nodeA);
    assertSame(m_edge.getOtherNode(new BasicNode()), null);
  }
  
  public void test_reverse() {
    assertSame(m_nodeA, m_edge.getNodeA());
    assertSame(m_nodeB, m_edge.getNodeB());
    
    m_edge.reverse();
    
    assertSame(m_nodeA, m_edge.getNodeB());
    assertSame(m_nodeB, m_edge.getNodeA());	
  }
  
  public void test_getRelated() {
  	BasicEdge be;
    Iterator itr;
    
    //nodes share single edge
    itr = m_edge.getRelated();
    assertTrue(itr.hasNext());
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) ||
      be.equals(m_other3) || be.equals(m_other4)
    );
    assertTrue(itr.hasNext());
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) ||
      be.equals(m_other3) || be.equals(m_other4)
    );
    assertTrue(itr.hasNext());
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) ||
      be.equals(m_other3) || be.equals(m_other4)
    );
    assertTrue(itr.hasNext());
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) ||
      be.equals(m_other3) || be.equals(m_other4)
    );
    assertTrue(!itr.hasNext());
  
    //nodes share multiple edges (same direction)
    m_nodeA.add(m_same);
    m_nodeB.add(m_same);
    
    itr = m_edge.getRelated();
    assertTrue(itr.hasNext());
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || 
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_same)
    );
    assertTrue(itr.hasNext());

    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || 
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_same)
    );
    assertTrue(itr.hasNext());
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || 
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_same)
    );
    assertTrue(itr.hasNext());

    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || 
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_same)
    );
    assertTrue(itr.hasNext());
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || 
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_same)
    );
    assertTrue(!itr.hasNext());
    m_nodeA.remove(m_same);
    m_nodeB.remove(m_same);
    
    //nodes share multiple edges (differnt direction)
    m_nodeB.add(m_opp);
    m_nodeA.add(m_opp);
    
    itr = m_edge.getRelated();
    assertTrue(itr.hasNext());
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || 
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_opp)
    );
    assertTrue(itr.hasNext());
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || 
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_opp)
    );
    assertTrue(itr.hasNext());
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || 
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_opp)
    );
    assertTrue(itr.hasNext());
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || 
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_opp)
    );
    assertTrue(itr.hasNext());
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || 
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_opp)
    );
    assertTrue(!itr.hasNext());
    
    m_nodeA.remove(m_opp);
    m_nodeB.remove(m_opp);
    
    //loop on one of nodes
    m_nodeA.add(m_loopA);
    
    itr = m_edge.getRelated();
    assertTrue(itr.hasNext());
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || 
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_loopA)
    );
    assertTrue(itr.hasNext());
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || 
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_loopA)
    );
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || 
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_loopA)
    );
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || 
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_loopA)
    );
    assertTrue(itr.hasNext());
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || 
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_loopA)
    );
    assertTrue(!itr.hasNext());
    
    m_nodeA.remove(m_loopA);
    
    //test loop on other node
    m_nodeB.add(m_loopB);
    
    itr = m_edge.getRelated();
    assertTrue(itr.hasNext());
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || 
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_loopB)
    );
    assertTrue(itr.hasNext());
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || 
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_loopB)
    );
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || 
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_loopB)
    );
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || 
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_loopB)
    );
    assertTrue(itr.hasNext());
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || 
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_loopB)
    );
    assertTrue(!itr.hasNext());
    
    //test loop on both nodes
    m_nodeA.add(m_loopA);
    itr = m_edge.getRelated();
    assertTrue(itr.hasNext());
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || be.equals(m_loopA) ||
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_loopB)
    );
    assertTrue(itr.hasNext());
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || be.equals(m_loopA) ||
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_loopB)
    );
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || be.equals(m_loopA) ||
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_loopB)
    );
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || be.equals(m_loopA) ||
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_loopB)
    );
    assertTrue(itr.hasNext());
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || be.equals(m_loopA) ||
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_loopB)
    );
    assertTrue(itr.hasNext());
    
    be = (BasicEdge)itr.next();
    assertTrue(
      be.equals(m_other1) || be.equals(m_other2) || be.equals(m_loopA) ||
      be.equals(m_other3) || be.equals(m_other4) || be.equals(m_loopB)
    );
    assertTrue(!itr.hasNext());
    
  }
  
  public void test_compareTo() {
    BasicEdge same = new BasicEdge(m_nodeA, m_nodeB);
    BasicEdge opp = new BasicEdge(m_nodeB, m_nodeA);
    
    assertTrue(m_edge.compareNodes(same) == Edge.EQUAL_NODE_ORIENTATION);
    assertTrue(m_edge.compareNodes(opp) == Edge.OPPOSITE_NODE_ORIENTATION);
    assertTrue(m_edge.compareNodes(m_other1) == Edge.UNEQUAL_NODE_ORIENTATION);
  }
}
