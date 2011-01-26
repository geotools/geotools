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

public class BasicDirectedNodeTest extends TestCase {

  private BasicDirectedNode m_node;
  private BasicDirectedNode m_inNode1;
  private BasicDirectedNode m_inNode2;
  private BasicDirectedNode m_outNode1;
  private BasicDirectedNode m_outNode2;
  
  private BasicDirectedEdge m_inEdge1;
  private BasicDirectedEdge m_inEdge2;
  private BasicDirectedEdge m_outEdge1;
  private BasicDirectedEdge m_outEdge2;
  
  private BasicDirectedEdge m_loop;
  
  public BasicDirectedNodeTest(String name) {
    super(name);
  }
  
  protected void setUp() throws Exception {
    super.setUp();
    
    m_node = new BasicDirectedNode();
    m_inNode1 = new BasicDirectedNode();
    m_inNode2 = new BasicDirectedNode();
    m_outNode1 = new BasicDirectedNode();
    m_outNode2 = new BasicDirectedNode();
    
    m_inEdge1 = new BasicDirectedEdge(m_inNode1, m_node);
    m_inEdge2 = new BasicDirectedEdge(m_inNode2, m_node);
    m_outEdge1 = new BasicDirectedEdge(m_node, m_outNode1);
    m_outEdge2 = new BasicDirectedEdge(m_node, m_outNode2);
    
    m_loop = new BasicDirectedEdge(m_node, m_node);
  }
  
  public void test_addIn() {
    assertTrue(m_node.getInEdges().size() == 0);
    
    //single in edge
    m_node.addIn(m_inEdge1);
    assertTrue(m_node.getInEdges().contains(m_inEdge1));
    assertTrue(m_node.getInEdges().size() == 1);
    
    //multiple in edges, same edge
    m_node.addIn(m_inEdge1);
    assertTrue(m_node.getInEdges().size() == 2);
    m_node.removeIn(m_inEdge1);
     
    //multiple in edges, different
    m_node.addIn(m_inEdge2);
    assertTrue(m_node.getInEdges().contains(m_inEdge1));
    assertTrue(m_node.getInEdges().contains(m_inEdge2));
    assertTrue(m_node.getInEdges().size() == 2);
  }
  
  public void test_addOut() {
  	assertTrue(m_node.getOutEdges().size() == 0);
  	
  	//single out edge
    m_node.addOut(m_outEdge1);
    assertTrue(m_node.getOutEdges().contains(m_outEdge1));
    assertTrue(m_node.getOutEdges().size() == 1);
    
    //multiple out edges, same edge
    m_node.addOut(m_outEdge1);
    assertTrue(m_node.getOutEdges().size() == 2);
    m_node.removeOut(m_outEdge1);
     
    //multiple out edges, different
    m_node.addOut(m_outEdge2);
    assertTrue(m_node.getOutEdges().contains(m_outEdge1));
    assertTrue(m_node.getOutEdges().contains(m_outEdge2));
    assertTrue(m_node.getOutEdges().size() == 2);
  }
  
  public void test_remove() {
    m_node.addIn(m_inEdge1);
    m_node.addOut(m_outEdge1);
    
    m_node.remove(m_inEdge1);
    m_node.remove(m_outEdge1);
    
    assertTrue(m_node.getInEdges().size() == 0);
    assertTrue(m_node.getOutEdges().size() == 0);	
  }
  
  public void test_removeIn() {
    //single edge
    m_node.addIn(m_inEdge1);
    m_node.removeIn(m_inEdge1);
    assertTrue(m_node.getInEdges().isEmpty());
    
    //multiple edges same
    m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge1);
    assertTrue(m_node.getInEdges().size() == 2);
    
    m_node.removeIn(m_inEdge1);
    assertTrue(m_node.getInEdges().size() == 1);
    
    m_node.removeIn(m_inEdge1);
    assertTrue(m_node.getInEdges().size() == 0);
    
    //multiple edges different
    m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge2);
    
    m_node.removeIn(m_inEdge1);
    assertTrue(m_node.getInEdges().size() == 1);
    assertTrue(m_node.getInEdges().contains(m_inEdge2));
    
    m_node.removeIn(m_inEdge2);
    assertTrue(m_node.getInEdges().size() == 0);
  }
  
  public void test_removeOut() {
  	//single edge
    m_node.addOut(m_outEdge1);
    m_node.removeOut(m_outEdge1);
    assertTrue(m_node.getOutEdges().isEmpty());
    
    //multiple edges same
    m_node.addOut(m_outEdge1);
    m_node.addOut(m_outEdge1);
    assertTrue(m_node.getOutEdges().size() == 2);
    
    m_node.removeOut(m_outEdge1);
    assertTrue(m_node.getOutEdges().size() == 1);
    
    m_node.removeOut(m_outEdge1);
    assertTrue(m_node.getOutEdges().size() == 0);
    
    //multiple edges different
    m_node.addOut(m_outEdge1);
    m_node.addOut(m_outEdge2);
    
    m_node.removeOut(m_outEdge1);
    assertTrue(m_node.getOutEdges().size() == 1);
    assertTrue(m_node.getOutEdges().contains(m_outEdge2));
    
    m_node.removeOut(m_outEdge2);
    assertTrue(m_node.getOutEdges().size() == 0);
  }
  
  public void test_getEdge() {
    m_node.addIn(m_inEdge1);
    m_node.addOut(m_outEdge1);
    
    assertSame(m_node.getEdge(m_inNode1), m_inEdge1);
    assertSame(m_node.getEdge(m_outNode1), m_outEdge1);	
  }
  
  public void test_getInEdge() {
    m_node.addIn(m_inEdge1);
    assertSame(m_node.getInEdge(m_inNode1), m_inEdge1);	
  }

  public void test_getOutEdge() {
  	m_node.addOut(m_outEdge1);
    assertSame(m_node.getOutEdge(m_outNode1), m_outEdge1);
  }
  
  public void test_getEdges_0() {
  	m_node.addIn(m_inEdge1);
  	m_node.addOut(m_outEdge1);
  	
  	assertTrue(m_node.getEdges().size() == 2);
  	assertTrue(m_node.getEdges().contains(m_inEdge1));
  	assertTrue(m_node.getEdges().contains(m_outEdge1));
  }
  
  public void test_getEdges_1() {
    m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge2);
    
    m_node.addOut(m_outEdge1);
    m_node.addOut(m_outEdge2);
    
    assertTrue(m_node.getEdges(m_inNode1).contains(m_inEdge1));	
    assertTrue(m_node.getEdges(m_inNode2).contains(m_inEdge2));
    assertTrue(m_node.getEdges(m_outNode1).contains(m_outEdge1));
    assertTrue(m_node.getEdges(m_outNode2).contains(m_outEdge2));
    
    //add duplicates
    m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge2);
    
    m_node.addOut(m_outEdge1);
    m_node.addOut(m_outEdge2);
    
    assertTrue(m_node.getEdges(m_inNode1).contains(m_inEdge1));	
    assertTrue(m_node.getEdges(m_inNode1).size() == 2);
    assertTrue(m_node.getEdges(m_inNode2).contains(m_inEdge2));
    assertTrue(m_node.getEdges(m_inNode2).size() == 2);
    assertTrue(m_node.getEdges(m_outNode1).contains(m_outEdge1));
    assertTrue(m_node.getEdges(m_outNode1).size() == 2);
    assertTrue(m_node.getEdges(m_outNode2).contains(m_outEdge2));
    assertTrue(m_node.getEdges(m_outNode2).size() == 2);
  }
  
  
  public void test_getInEdges_0() {
    m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge2);
    
  	assertTrue(m_node.getInEdges().contains(m_inEdge1));
  	assertTrue(m_node.getInEdges().contains(m_inEdge2));
  	assertTrue(m_node.getInEdges().size() == 2);
  }
  
  public void test_getInEdges_1() {
    m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge2);
    
    assertTrue(m_node.getInEdges(m_inNode1).contains(m_inEdge1));	
    assertTrue(m_node.getInEdges(m_inNode2).contains(m_inEdge2));
    
    m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge2);
    
    assertTrue(m_node.getInEdges(m_inNode1).contains(m_inEdge1));
    assertTrue(m_node.getInEdges(m_inNode1).size() == 2);
    	
    assertTrue(m_node.getInEdges(m_inNode2).contains(m_inEdge2));
    assertTrue(m_node.getInEdges(m_inNode2).size() == 2);
  }
  
  public void test_getOutEdges_0() {
  	m_node.addOut(m_outEdge1);
    m_node.addOut(m_outEdge2);
    
  	assertTrue(m_node.getOutEdges().contains(m_outEdge1));
  	assertTrue(m_node.getOutEdges().contains(m_outEdge2));
  	assertTrue(m_node.getOutEdges().size() == 2);
  }
  
  public void test_getOutEdges_1() {
  	m_node.addOut(m_outEdge1);
    m_node.addOut(m_outEdge2);
    
    assertTrue(m_node.getOutEdges(m_outNode1).contains(m_outEdge1));	
    assertTrue(m_node.getOutEdges(m_outNode2).contains(m_outEdge2));
    
    m_node.addOut(m_outEdge1);
    m_node.addOut(m_outEdge2);
    
    assertTrue(m_node.getOutEdges(m_outNode1).contains(m_outEdge1));
    assertTrue(m_node.getOutEdges(m_outNode1).size() == 2);
    	
    assertTrue(m_node.getOutEdges(m_outNode2).contains(m_outEdge2));
    assertTrue(m_node.getOutEdges(m_outNode2).size() == 2);
  }
  
  public void test_getDegree() {
    m_node.addIn(m_inEdge1);
    m_node.addOut(m_outEdge2);
    assertTrue(m_node.getDegree() == 2);	
  }
  
  public void test_getInDegree() {
    m_node.addIn(m_inEdge1);
    m_node.addOut(m_outEdge2);
    assertTrue(m_node.getInDegree() == 1);	
  }
  
  public void test_getOutDegree_0() {
  	m_node.addIn(m_inEdge1);
    m_node.addOut(m_outEdge2);
    
    assertTrue(m_node.getOutDegree() == 1);	
  }
  
  
  public void test_getRelated() {
    m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge2);
    
    m_node.addOut(m_outEdge1);
    m_node.addOut(m_outEdge2);
   
    Iterator related = m_node.getRelated(); 
    BasicDirectedNode dn = (BasicDirectedNode)related.next();
    assertTrue(
      dn == m_inNode1 || dn == m_inNode2 || dn == m_outNode1 || dn == m_outNode2
    );
    
    dn = (BasicDirectedNode)related.next();
    assertTrue(
      dn == m_inNode1 || dn == m_inNode2 || dn == m_outNode1 || dn == m_outNode2
    );
    
    dn = (BasicDirectedNode)related.next();
    assertTrue(
      dn == m_inNode1 || dn == m_inNode2 || dn == m_outNode1 || dn == m_outNode2
    );
    
    dn = (BasicDirectedNode)related.next();
    assertTrue(
      dn == m_inNode1 || dn == m_inNode2 || dn == m_outNode1 || dn == m_outNode2
    );
    
    assertTrue(!related.hasNext());
    
    //add loop
    m_node.addIn(m_loop);
    m_node.addOut(m_loop);
    
    related = m_node.getRelated(); 
    dn = (BasicDirectedNode)related.next();
    assertTrue(
      dn == m_inNode1 || dn == m_inNode2 || 
      dn == m_outNode1 || dn == m_outNode2 || dn == m_node
    );
    
    dn = (BasicDirectedNode)related.next();
    assertTrue(
      dn == m_inNode1 || dn == m_inNode2 || 
      dn == m_outNode1 || dn == m_outNode2 || dn == m_node
    );
    
    dn = (BasicDirectedNode)related.next();
    assertTrue(
      dn == m_inNode1 || dn == m_inNode2 || 
      dn == m_outNode1 || dn == m_outNode2 || dn == m_node
    );
    
    dn = (BasicDirectedNode)related.next();
    assertTrue(
      dn == m_inNode1 || dn == m_inNode2 || 
      dn == m_outNode1 || dn == m_outNode2 || dn == m_node
    );
    
    dn = (BasicDirectedNode)related.next();
    assertTrue(
      dn == m_inNode1 || dn == m_inNode2 || 
      dn == m_outNode1 || dn == m_outNode2 || dn == m_node
    );
    
    dn = (BasicDirectedNode)related.next();
    assertTrue(
      dn == m_inNode1 || dn == m_inNode2 || 
      dn == m_outNode1 || dn == m_outNode2 || dn == m_node
    );
    
    assertTrue(!related.hasNext());
  }
  
  public void test_getInRelated() {
  	m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge2);
    
    m_node.addOut(m_outEdge1);
    m_node.addOut(m_outEdge2);
   
    Iterator related = m_node.getInRelated(); 
    BasicDirectedNode dn = (BasicDirectedNode)related.next();
    assertTrue(dn == m_inNode1 || dn == m_inNode2);
    
    dn = (BasicDirectedNode)related.next();
    assertTrue(dn == m_inNode1 || dn == m_inNode2);
    assertTrue(!related.hasNext());
    
    //add a loop
    m_node.addIn(m_loop);
    m_node.addOut(m_loop);
    
    related = m_node.getInRelated(); 
    
    dn = (BasicDirectedNode)related.next();
    assertTrue(dn == m_inNode1 || dn == m_inNode2 || dn == m_node);
    
    dn = (BasicDirectedNode)related.next();
    assertTrue(dn == m_inNode1 || dn == m_inNode2 || dn == m_node);
    
    dn = (BasicDirectedNode)related.next();
    assertTrue(dn == m_inNode1 || dn == m_inNode2 || dn == m_node);
    
    assertTrue(!related.hasNext());
    
  }
  
  public void test_getOutRelated() {
  	m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge2);
    
    m_node.addOut(m_outEdge1);
    m_node.addOut(m_outEdge2);
   
    Iterator related = m_node.getOutRelated(); 
    BasicDirectedNode dn = (BasicDirectedNode)related.next();
    assertTrue(dn == m_outNode1 || dn == m_outNode2);
    
    dn = (BasicDirectedNode)related.next();
    assertTrue(dn == m_outNode1 || dn == m_outNode2);
    
    assertTrue(!related.hasNext());
    
    //add a loop
    m_node.addIn(m_loop);
    m_node.addOut(m_loop);
    
    related = m_node.getOutRelated(); 
    
    dn = (BasicDirectedNode)related.next();
    assertTrue(dn == m_outNode1 || dn == m_outNode2 || dn == m_node);
    
    dn = (BasicDirectedNode)related.next();
    assertTrue(dn == m_outNode1 || dn == m_outNode2 || dn == m_node);
    
    dn = (BasicDirectedNode)related.next();
    assertTrue(dn == m_outNode1 || dn == m_outNode2 || dn == m_node);
    
    assertTrue(!related.hasNext());
  }
}

