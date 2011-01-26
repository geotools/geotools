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

public class BasicDirectedEdgeTest extends TestCase {

  private BasicDirectedNode m_inNode;
  private BasicDirectedNode m_outNode;
  private BasicDirectedNode m_otherInNode;
  private BasicDirectedNode m_otherOutNode;
  
  private BasicDirectedEdge m_edge;
  
  private BasicDirectedEdge m_inEdge;
  private BasicDirectedEdge m_outEdge;
  
  private BasicDirectedEdge m_same;
  private BasicDirectedEdge m_same2;
  private BasicDirectedEdge m_opp;
  private BasicDirectedEdge m_opp2;
  private BasicDirectedEdge m_inloop;
  private BasicDirectedEdge m_outloop;
  private BasicDirectedEdge m_inoutEdge;
  private BasicDirectedEdge m_outinEdge;
  
  public BasicDirectedEdgeTest(String name) {
    super(name);  
  } 
	
  protected void setUp() throws Exception {
    m_inNode = new BasicDirectedNode();
    m_outNode = new BasicDirectedNode();
    m_edge = new BasicDirectedEdge(m_inNode, m_outNode);
    m_inNode.addOut(m_edge);
    m_outNode.addIn(m_edge);
    
    m_otherInNode = new BasicDirectedNode();
    m_otherOutNode = new BasicDirectedNode();
    
    m_inEdge = new BasicDirectedEdge(m_otherInNode, m_inNode);
    m_otherInNode.addOut(m_inEdge);
    m_inNode.addIn(m_inEdge);
    
    m_outEdge = new BasicDirectedEdge(m_outNode, m_otherOutNode);
    m_outNode.addOut(m_outEdge);
    m_otherOutNode.addIn(m_outEdge);
    
    m_same = new BasicDirectedEdge(m_inNode, m_outNode);
    m_same2 = new BasicDirectedEdge(m_inNode, m_outNode);
    
    m_opp = new BasicDirectedEdge(m_outNode, m_inNode);
    m_opp2 = new BasicDirectedEdge(m_outNode, m_inNode);
    
    m_inloop = new BasicDirectedEdge(m_inNode, m_inNode);
    m_outloop = new BasicDirectedEdge(m_outNode, m_outNode);
    
    m_inoutEdge = new BasicDirectedEdge(m_inNode, m_otherInNode);
    m_outinEdge = new BasicDirectedEdge(m_otherOutNode, m_outNode);
  }
  
  public void test_getInNode() {
    assertTrue(m_inNode == m_edge.getNodeA());
    assertTrue(m_inNode == m_edge.getInNode());	
  }
  
  public void test_getOutNode() {
  	assertTrue(m_outNode == m_edge.getNodeB());
    assertTrue(m_outNode == m_edge.getOutNode());
  }
  
  public void test_getOtherNode() {
    assertTrue(m_inNode == m_edge.getOtherNode(m_outNode));
    assertTrue(m_outNode == m_edge.getOtherNode(m_inNode));	
    assertTrue(null == m_edge.getOtherNode(new BasicDirectedNode()));
  }

  public void test_reverse() {
    assertTrue(m_edge.getInNode().getOutEdges().contains(m_edge));
    assertTrue(m_edge.getOutNode().getInEdges().contains(m_edge));
    
    m_edge.reverse();
    
    assertTrue(m_inNode == m_edge.getOutNode());
    assertTrue(m_outNode == m_edge.getInNode());	
    
    assertTrue(m_edge.getInNode().getOutEdges().contains(m_edge));
    assertTrue(m_edge.getOutNode().getInEdges().contains(m_edge));
  }  
  
  public void test_getRelated() {
    Iterator itr = m_edge.getRelated();	
    
    BasicDirectedEdge de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_outEdge);
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_outEdge);
    
    assertTrue(!itr.hasNext());
    
    //add an edge that has the same nodes, in the same direction
    m_edge.getInNode().addOut(m_same);
    m_edge.getOutNode().addIn(m_same);
    
    itr = m_edge.getRelated();	
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_outEdge || de == m_same);
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_outEdge || de == m_same);
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_outEdge || de == m_same);
    
    assertTrue(!itr.hasNext());
    
    //add another edge in the same direction
    m_edge.getInNode().addOut(m_same2);
    m_edge.getOutNode().addIn(m_same2);
    
    itr = m_edge.getRelated();  
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de==m_inEdge || de==m_outEdge || de==m_same || de==m_same2);
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de==m_inEdge || de==m_outEdge || de==m_same || de==m_same2);
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de==m_inEdge || de==m_outEdge || de==m_same || de==m_same2);
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de==m_inEdge || de==m_outEdge || de==m_same || de==m_same2);
    
    assertTrue(!itr.hasNext());
    
    m_edge.getInNode().removeOut(m_same);
    m_edge.getOutNode().removeIn(m_same);
    
    m_edge.getInNode().removeOut(m_same2);
    m_edge.getOutNode().removeIn(m_same2);
    
    //add an edge that has the same nodes, opposite direction
    m_edge.getInNode().addIn(m_opp);
    m_edge.getOutNode().addOut(m_opp);
    
    itr = m_edge.getRelated();	
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_outEdge || de == m_opp);
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_outEdge || de == m_opp);
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_outEdge || de == m_opp);
    
    assertTrue(!itr.hasNext());
    
    //add another edge in opposite direction
    m_edge.getInNode().addIn(m_opp2);
    m_edge.getOutNode().addOut(m_opp2);
    
    itr = m_edge.getRelated();  
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de==m_inEdge || de==m_outEdge || de==m_opp || de==m_opp2);
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de==m_inEdge || de==m_outEdge || de==m_opp || de==m_opp2);
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de==m_inEdge || de==m_outEdge || de==m_opp || de==m_opp2);
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de==m_inEdge || de==m_outEdge || de==m_opp || de==m_opp2);
    
    assertTrue(!itr.hasNext());
    
    m_edge.getInNode().removeIn(m_opp);
    m_edge.getOutNode().removeOut(m_opp);
    
    m_edge.getInNode().removeIn(m_opp2);
    m_edge.getOutNode().removeOut(m_opp2);
    
    //add loops
    m_edge.getInNode().addIn(m_inloop);
    m_edge.getInNode().addOut(m_inloop);
    
    m_edge.getOutNode().addIn(m_outloop);
    m_edge.getOutNode().addOut(m_outloop);
    
    itr = m_edge.getRelated();	
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(
      de == m_inEdge || de == m_outEdge || de == m_inloop || de == m_outloop
    );
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(
      de == m_inEdge || de == m_outEdge || de == m_inloop || de == m_outloop
    );
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(
      de == m_inEdge || de == m_outEdge || de == m_inloop || de == m_outloop
    );
    
    de = (BasicDirectedEdge)itr.next(); 
    assertTrue(
      de == m_inEdge || de == m_outEdge || de == m_inloop || de == m_outloop
    );
    assertTrue(!itr.hasNext());
    
    m_edge.getInNode().removeIn(m_inloop);
    m_edge.getInNode().removeOut(m_inloop);
    
    m_edge.getOutNode().removeIn(m_outloop);
    m_edge.getOutNode().removeOut(m_outloop);
    
    //add an incoming edge to the out node and an outgoing edge to the in node
    m_edge.getInNode().addOut(m_inoutEdge);
    m_otherInNode.addIn(m_inoutEdge);
    
    m_edge.getOutNode().addIn(m_outinEdge);
    m_otherOutNode.addOut(m_outinEdge);
    
    itr = m_edge.getRelated();  
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(
     de == m_inEdge || de == m_outEdge || de == m_inoutEdge || de == m_outinEdge
    );
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(
     de == m_inEdge || de == m_outEdge || de == m_inoutEdge || de == m_outinEdge
    );
        
    de = (BasicDirectedEdge)itr.next();
    assertTrue(
     de == m_inEdge || de == m_outEdge || de == m_inoutEdge || de == m_outinEdge
    );
    
    de = (BasicDirectedEdge)itr.next(); 
    assertTrue(
     de == m_inEdge || de == m_outEdge || de == m_inoutEdge || de == m_outinEdge
    );
    
    assertTrue(!itr.hasNext());
  }
  
  public void test_getInRelated() {
    Iterator itr = m_edge.getInRelated();	
    
    BasicDirectedEdge de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_inEdge);
    
    assertTrue(!itr.hasNext());
    
    //add same edge (shouldn't show up in iterator this time)
    m_edge.getInNode().addOut(m_same);
    m_edge.getOutNode().addIn(m_same);
    
    itr = m_edge.getInRelated();	
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_inEdge);
    
    assertTrue(!itr.hasNext());
    
    m_edge.getInNode().removeOut(m_same);
    m_edge.getOutNode().removeIn(m_same);
    
    //add opposite edge
    m_edge.getInNode().addIn(m_opp);
    m_edge.getOutNode().addOut(m_opp);
    
    itr = m_edge.getInRelated();	
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_opp);
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_opp);
    
    assertTrue(!itr.hasNext());
    
    //add multiple opposites
    m_edge.getInNode().addIn(m_opp2);
    m_edge.getOutNode().addOut(m_opp2);
    
    itr = m_edge.getInRelated();  
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_opp || de == m_opp2);
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_opp || de == m_opp2);
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_opp || de == m_opp2);
    
    assertTrue(!itr.hasNext());
    
    m_edge.getInNode().removeIn(m_opp);
    m_edge.getOutNode().removeOut(m_opp);
    
    m_edge.getInNode().removeIn(m_opp2);
    m_edge.getOutNode().removeOut(m_opp2);
    
    //add loop
    m_edge.getInNode().addIn(m_inloop);
    m_edge.getInNode().addOut(m_inloop);
    
    itr = m_edge.getInRelated();	
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_inloop);
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_inloop);
    
    assertTrue(!itr.hasNext());
  }
  
  public void test_getOutRelated() {
  	Iterator itr = m_edge.getOutRelated();	
    
    BasicDirectedEdge de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_outEdge);
    
    assertTrue(!itr.hasNext());
    
    //add same edge (shouldn't show up in iterator this time)
    m_edge.getInNode().addOut(m_same);
    m_edge.getOutNode().addIn(m_same);
    
    itr = m_edge.getOutRelated();	
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_outEdge);
    
    assertTrue(!itr.hasNext());
    
    m_edge.getInNode().removeOut(m_same);
    m_edge.getOutNode().removeIn(m_same);
    
    //add opposite edge
    m_edge.getInNode().addIn(m_opp);
    m_edge.getOutNode().addOut(m_opp);
    
    itr = m_edge.getOutRelated();	
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_outEdge || de == m_opp);
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_outEdge || de == m_opp);
    
    assertTrue(!itr.hasNext());
    
    //add another opposite edge
    m_edge.getInNode().addIn(m_opp2);
    m_edge.getOutNode().addOut(m_opp2);
    
    itr = m_edge.getOutRelated(); 
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_outEdge || de == m_opp || de == m_opp2);
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_outEdge || de == m_opp || de == m_opp2);
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_outEdge || de == m_opp || de == m_opp2);
    
    assertTrue(!itr.hasNext());
    
    m_edge.getInNode().removeIn(m_opp);
    m_edge.getOutNode().removeOut(m_opp);
    
    m_edge.getInNode().removeIn(m_opp2);
    m_edge.getOutNode().removeOut(m_opp2);
    
    //add loop
    m_edge.getOutNode().addIn(m_outloop);
    m_edge.getOutNode().addOut(m_outloop);
    
    itr = m_edge.getOutRelated();	
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_outEdge || de == m_outloop);
    
    de = (BasicDirectedEdge)itr.next();
    assertTrue(de == m_outEdge || de == m_outloop);
    
    assertTrue(!itr.hasNext());
  }
}
