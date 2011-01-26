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
package org.geotools.graph.structure.opt;

import java.util.Iterator;

import junit.framework.TestCase;

public class OptDirectedEdgeTest extends TestCase {
 
  private OptDirectedNode m_inNode;
  private OptDirectedNode m_outNode;
  private OptDirectedNode m_otherInNode;
  private OptDirectedNode m_otherOutNode;
  
  private OptDirectedEdge m_edge;
  
  private OptDirectedEdge m_inEdge;
  private OptDirectedEdge m_outEdge;
  
  private OptDirectedEdge m_same;
  private OptDirectedEdge m_same2;
  private OptDirectedEdge m_opp;
  private OptDirectedEdge m_opp2;
  private OptDirectedEdge m_inloop;
  private OptDirectedEdge m_outloop;
  private OptDirectedEdge m_inoutEdge;
  private OptDirectedEdge m_outinEdge;
  
  public OptDirectedEdgeTest(String name) {
    super(name);  
  } 
  
  protected void setUp() throws Exception {
    m_inNode = new OptDirectedNode();
    m_inNode.setInDegree(1);
    m_inNode.setOutDegree(1);
    
    m_outNode = new OptDirectedNode();
    m_outNode.setInDegree(1);
    m_outNode.setOutDegree(1);
    
    m_edge = new OptDirectedEdge(m_inNode, m_outNode);
    m_inNode.addOut(m_edge);
    m_outNode.addIn(m_edge);
    
    m_otherInNode = new OptDirectedNode();
    m_otherInNode.setInDegree(0);
    m_otherInNode.setOutDegree(1);
    
    m_otherOutNode = new OptDirectedNode();
    m_otherOutNode.setInDegree(1);
    m_otherOutNode.setOutDegree(0);
    
    m_inEdge = new OptDirectedEdge(m_otherInNode, m_inNode);
    m_otherInNode.addOut(m_inEdge);
    m_inNode.addIn(m_inEdge);
    
    m_outEdge = new OptDirectedEdge(m_outNode, m_otherOutNode);
    m_outNode.addOut(m_outEdge);
    m_otherOutNode.addIn(m_outEdge);
    
    m_same = new OptDirectedEdge(m_inNode, m_outNode);
    m_same2 = new OptDirectedEdge(m_inNode, m_outNode);
    
    m_opp = new OptDirectedEdge(m_outNode, m_inNode);
    m_opp2 = new OptDirectedEdge(m_outNode, m_inNode);
    
    m_inloop = new OptDirectedEdge(m_inNode, m_inNode);
    m_outloop = new OptDirectedEdge(m_outNode, m_outNode);
    
    m_inoutEdge = new OptDirectedEdge(m_inNode, m_otherInNode);
    m_outinEdge = new OptDirectedEdge(m_otherOutNode, m_outNode);
  }
  
  protected void addSame() {
    try {
      setUp();  
    }
    catch(Exception e) {
      e.printStackTrace(); 
    }  
    
    m_inNode.setOutDegree(2);
    m_inNode.addOut(m_edge);
    m_inNode.addOut(m_same);
    
    m_outNode.setInDegree(2);
    m_outNode.addIn(m_edge);
    m_outNode.addIn(m_same);
  }

  protected void addSame2() {
    try {
      setUp();  
    }
    catch(Exception e) {
      e.printStackTrace(); 
    }  
    
    m_inNode.setOutDegree(3);
    m_inNode.addOut(m_edge);
    m_inNode.addOut(m_same);
    m_inNode.addOut(m_same2);
    
    m_outNode.setInDegree(3);
    m_outNode.addIn(m_edge);
    m_outNode.addIn(m_same);
    m_outNode.addIn(m_same2);
  }
  
  protected void addOpp() {
    try {
      setUp();  
    }
    catch(Exception e) {
      e.printStackTrace(); 
    }  
    
    m_inNode.setInDegree(2);
    m_inNode.addIn(m_inEdge);
    m_inNode.addIn(m_opp);
    
    m_outNode.setOutDegree(2);
    m_outNode.addOut(m_outEdge);
    m_outNode.addOut(m_opp);
  }

  protected void addOpp2() {
    try {
      setUp();  
    }
    catch(Exception e) {
      e.printStackTrace(); 
    }  
    
    m_inNode.setInDegree(3);
    m_inNode.addIn(m_inEdge);
    m_inNode.addIn(m_opp);
    m_inNode.addIn(m_opp2);
    
    m_outNode.setOutDegree(3);
    m_outNode.addOut(m_outEdge);
    m_outNode.addOut(m_opp);
    m_outNode.addOut(m_opp2);
  }
  
  protected void addLoops() {
    try {
      setUp();  
    }
    catch(Exception e) {
      e.printStackTrace(); 
    }  
    
    m_inNode.setInDegree(2);
    m_inNode.addIn(m_inEdge);
    m_inNode.addIn(m_inloop);
    
    m_inNode.setOutDegree(2);
    m_inNode.addOut(m_edge);
    m_inNode.addOut(m_inloop);
    
    m_outNode.setInDegree(2);
    m_outNode.addIn(m_edge);
    m_outNode.addIn(m_outloop);
    
    m_outNode.setOutDegree(2);
    m_outNode.addOut(m_outEdge);
    m_outNode.addOut(m_outloop);
  }
  
  protected void addInOutEdges() {
    try {
      setUp();  
    }
    catch(Exception e) {
      e.printStackTrace(); 
    }  
    
    m_inNode.setOutDegree(2);
    m_inNode.addOut(m_edge);
    m_inNode.addOut(m_inoutEdge);
    
    m_otherInNode.setInDegree(1);
    m_otherInNode.addIn(m_inoutEdge);
    
    m_outNode.setInDegree(2);
    m_outNode.addIn(m_edge);
    m_outNode.addIn(m_outinEdge);
    
    m_otherOutNode.setOutDegree(1);
    m_otherInNode.addOut(m_outinEdge);
    
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
    assertTrue(null == m_edge.getOtherNode(new OptDirectedNode()));
  }

  public void test_reverse() {
    assertTrue(m_edge.getInNode().getOutEdges().contains(m_edge));
    assertTrue(m_edge.getOutNode().getInEdges().contains(m_edge));
    
    try {
      m_edge.reverse();
      assertTrue(false);
    }
    catch(UnsupportedOperationException e) {
      assertTrue(true);  
    }
    
  }  
  
  public void test_getRelated() {
    Iterator itr = m_edge.getRelated(); 
    
    OptDirectedEdge de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_outEdge);
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_outEdge);
    
    assertTrue(!itr.hasNext());
    
    //add an edge that has the same nodes, in the same direction
    addSame();
    
    itr = m_edge.getRelated();  
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_outEdge || de == m_same);
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_outEdge || de == m_same);
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_outEdge || de == m_same);
    
    assertTrue(!itr.hasNext());
    
    //add another edge in the same direction
    addSame2();
    
    itr = m_edge.getRelated();  
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de==m_inEdge || de==m_outEdge || de==m_same || de==m_same2);
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de==m_inEdge || de==m_outEdge || de==m_same || de==m_same2);
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de==m_inEdge || de==m_outEdge || de==m_same || de==m_same2);
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de==m_inEdge || de==m_outEdge || de==m_same || de==m_same2);
    
    assertTrue(!itr.hasNext());
    
    addOpp();
    //add an edge that has the same nodes, opposite direction
    m_edge.getInNode().addIn(m_opp);
    m_edge.getOutNode().addOut(m_opp);
    
    itr = m_edge.getRelated();  
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_outEdge || de == m_opp);
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_outEdge || de == m_opp);
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_outEdge || de == m_opp);
    
    assertTrue(!itr.hasNext());
    
    //add another edge in opposite direction
    addOpp2();
    
    itr = m_edge.getRelated();  
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de==m_inEdge || de==m_outEdge || de==m_opp || de==m_opp2);
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de==m_inEdge || de==m_outEdge || de==m_opp || de==m_opp2);
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de==m_inEdge || de==m_outEdge || de==m_opp || de==m_opp2);
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de==m_inEdge || de==m_outEdge || de==m_opp || de==m_opp2);
    
    assertTrue(!itr.hasNext());
    
    //add loops
    addLoops();
    
    itr = m_edge.getRelated();  
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(
      de == m_inEdge || de == m_outEdge || de == m_inloop || de == m_outloop
    );
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(
      de == m_inEdge || de == m_outEdge || de == m_inloop || de == m_outloop
    );
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(
      de == m_inEdge || de == m_outEdge || de == m_inloop || de == m_outloop
    );
    
    de = (OptDirectedEdge)itr.next(); 
    assertTrue(
      de == m_inEdge || de == m_outEdge || de == m_inloop || de == m_outloop
    );
    
    assertTrue(!itr.hasNext());
    
    //add an incoming edge to the out node and an outgoing edge to the in node
    addInOutEdges();
    
    itr = m_edge.getRelated();  
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(
     de == m_inEdge || de == m_outEdge || de == m_inoutEdge || de == m_outinEdge
    );
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(
     de == m_inEdge || de == m_outEdge || de == m_inoutEdge || de == m_outinEdge
    );
        
    de = (OptDirectedEdge)itr.next();
    assertTrue(
     de == m_inEdge || de == m_outEdge || de == m_inoutEdge || de == m_outinEdge
    );
    
    de = (OptDirectedEdge)itr.next(); 
    assertTrue(
     de == m_inEdge || de == m_outEdge || de == m_inoutEdge || de == m_outinEdge
    );
    
    assertTrue(!itr.hasNext());
    
  }
  
  public void test_getInRelated() {
    Iterator itr = m_edge.getInRelated(); 
    
    OptDirectedEdge de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_inEdge);
    
    assertTrue(!itr.hasNext());
    
    //add same edge (shouldn't show up in iterator this time)
    addSame();
    
    itr = m_edge.getInRelated();  
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_inEdge);
    
    assertTrue(!itr.hasNext());
    
    //add multiple same edges
    addSame2();
    
    itr = m_edge.getInRelated();  
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_inEdge);
    
    assertTrue(!itr.hasNext());
    
    //add opposite edge
    addOpp();
    
    itr = m_edge.getInRelated();  
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_opp);
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_opp);
    
    assertTrue(!itr.hasNext());
    
    //add multiple opposites
    addOpp2();
    
    itr = m_edge.getInRelated();  
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_opp || de == m_opp2);
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_opp || de == m_opp2);
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_opp || de == m_opp2);
    
    assertTrue(!itr.hasNext());
    
    addLoops();
    
    //add loop
    m_edge.getInNode().addIn(m_inloop);
    m_edge.getInNode().addOut(m_inloop);
    
    itr = m_edge.getInRelated();  
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_inloop);
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_inEdge || de == m_inloop);
    
    assertTrue(!itr.hasNext());
  }
  
  public void test_getOutRelated() {
    Iterator itr = m_edge.getOutRelated();  
    
    OptDirectedEdge de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_outEdge);
    
    assertTrue(!itr.hasNext());
    
    //add same edge (shouldn't show up in iterator this time)
    addSame();
    
    itr = m_edge.getOutRelated(); 
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_outEdge);
    
    assertTrue(!itr.hasNext());
    
    //add multiple same edge (shouldn't show up in iterator this time)
    addSame2();
    
    itr = m_edge.getOutRelated(); 
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_outEdge);
    
    assertTrue(!itr.hasNext());
    
    //add opposite edge
    addOpp();
        
    itr = m_edge.getOutRelated(); 
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_outEdge || de == m_opp);
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_outEdge || de == m_opp);
    
    assertTrue(!itr.hasNext());
    
    //add another opposite edge
    addOpp2();
    
    itr = m_edge.getOutRelated(); 
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_outEdge || de == m_opp || de == m_opp2);
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_outEdge || de == m_opp || de == m_opp2);
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_outEdge || de == m_opp || de == m_opp2);
    
    assertTrue(!itr.hasNext());
    
    //add loop
    addLoops();
    
    itr = m_edge.getOutRelated(); 
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_outEdge || de == m_outloop);
    
    de = (OptDirectedEdge)itr.next();
    assertTrue(de == m_outEdge || de == m_outloop);
    
    assertTrue(!itr.hasNext());
  } 
}
