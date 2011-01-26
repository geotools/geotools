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

public class OptDirectedNodeTest extends TestCase {
  private OptDirectedNode m_node;
  private OptDirectedNode m_inNode1;
  private OptDirectedNode m_inNode2;
  private OptDirectedNode m_outNode1;
  private OptDirectedNode m_outNode2;
  
  private OptDirectedEdge m_inEdge1;
  private OptDirectedEdge m_inEdge2;
  private OptDirectedEdge m_outEdge1;
  private OptDirectedEdge m_outEdge2;
  
  private OptDirectedEdge m_loop;
  
  public OptDirectedNodeTest(String name) {
    super(name);
  }
  
  protected void setUp() throws Exception {
    super.setUp();
    
    m_node = new OptDirectedNode(); 
//    m_node.setInDegree(2);
//    m_node.setOutDegree(2);
    
    m_inNode1 = new OptDirectedNode(); 
//    m_inNode1.setInDegree(0);
//    m_inNode1.setOutDegree(1);
    
    m_inNode2 = new OptDirectedNode();
//    m_inNode2.setInDegree(0);
//    m_inNode2.setOutDegree(1);
    
    m_outNode1 = new OptDirectedNode();
//    m_outNode1.setInDegree(1);
//    m_outNode1.setOutDegree(0);
    
    m_outNode2 = new OptDirectedNode();
//    m_outNode2.setInDegree(1);
//    m_outNode2.setOutDegree(0);
    
    m_inEdge1 = new OptDirectedEdge(m_inNode1, m_node);
    m_inEdge2 = new OptDirectedEdge(m_inNode2, m_node);
    m_outEdge1 = new OptDirectedEdge(m_node, m_outNode1);
    m_outEdge2 = new OptDirectedEdge(m_node, m_outNode2);
    
    //dont add yet
    m_loop = new OptDirectedEdge(m_node, m_node);
  }
  
  public void test_addIn() {
    assertTrue(m_node.getInEdges().size() == 0);
    
    //single in edge
    m_node.setInDegree(1);
    m_node.addIn(m_inEdge1);
    
    assertTrue(m_node.getInEdges().contains(m_inEdge1));
    assertTrue(m_node.getInEdges().size() == 1);
    
    //multiple in edges, same edge
    m_node.setInDegree(2);
    m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge1);
    
    assertTrue(m_node.getInEdges().size() == 2);
    
    //multiple in edges, different
    m_node.setInDegree(2);
    m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge2);
    assertTrue(m_node.getInEdges().contains(m_inEdge1));
    assertTrue(m_node.getInEdges().contains(m_inEdge2));
    assertTrue(m_node.getInEdges().size() == 2);
  }
  
  public void test_addOut() {
    assertTrue(m_node.getOutEdges().size() == 0);
    
    //single out edge
    m_node.setOutDegree(1);
    m_node.addOut(m_outEdge1);
    assertTrue(m_node.getOutEdges().contains(m_outEdge1));
    assertTrue(m_node.getOutEdges().size() == 1);
    
    //multiple out edges, same edge
    m_node.setOutDegree(2);
    m_node.addOut(m_outEdge1);
    m_node.addOut(m_outEdge1);
    
    assertTrue(m_node.getOutEdges().size() == 2);
     
    //multiple out edges, different
    m_node.setOutDegree(2);
    m_node.addOut(m_outEdge1);
    m_node.addOut(m_outEdge2);
    assertTrue(m_node.getOutEdges().contains(m_outEdge1));
    assertTrue(m_node.getOutEdges().contains(m_outEdge2));
    assertTrue(m_node.getOutEdges().size() == 2);
  }
  
  public void test_remove() {
    m_node.setInDegree(1);
    m_node.setOutDegree(1);
    
    m_node.addIn(m_inEdge1);
    m_node.addOut(m_outEdge1);
    
    try {
      m_node.remove(m_inEdge1);
      assertTrue(true);
    }
    catch(UnsupportedOperationException uoe) {
      assertTrue(true);
    }
    
    try {
      m_node.remove(m_outEdge1);
      assertTrue(true);
    }
    catch(UnsupportedOperationException uoe) {
      assertTrue(true);
    }
  }
  
  public void test_removeIn() {
    //single edge
    m_node.setInDegree(1);
    m_node.addIn(m_inEdge1);
    
    try {
      m_node.removeIn(m_inEdge1);
      assertTrue(true);
    }
    catch(UnsupportedOperationException uoe) {
      assertTrue(true);
    }
  }
  
  public void test_removeOut() {
    //single edge
    m_node.setOutDegree(1);
    m_node.addOut(m_outEdge1);
    
    try {
      m_node.removeOut(m_outEdge1);
      assertTrue(true);
    }
    catch(UnsupportedOperationException uoe) {
      assertTrue(true);
    }
  }
  
  public void test_getEdge() {
    m_node.setInDegree(1);
    m_node.setOutDegree(1);
    
    m_node.addIn(m_inEdge1);
    m_node.addOut(m_outEdge1);
    
    assertSame(m_node.getEdge(m_inNode1), m_inEdge1);
    assertSame(m_node.getEdge(m_outNode1), m_outEdge1); 
  }
  
  public void test_getInEdge() {
    m_node.setInDegree(1);
    m_node.addIn(m_inEdge1);
    assertSame(m_node.getInEdge(m_inNode1), m_inEdge1); 
  }

  public void test_getOutEdge() {
    m_node.setOutDegree(1);
    m_node.addOut(m_outEdge1);
    assertSame(m_node.getOutEdge(m_outNode1), m_outEdge1);
  }
  
  public void test_getEdges_0() {
    m_node.setInDegree(1);
    m_node.setOutDegree(1);
    
    m_node.addIn(m_inEdge1);
    m_node.addOut(m_outEdge1);
    
    assertTrue(m_node.getEdges().size() == 2);
    assertTrue(m_node.getEdges().contains(m_inEdge1));
    assertTrue(m_node.getEdges().contains(m_outEdge1));
  }
  
  public void test_getEdges_1() {
    m_node.setInDegree(2);
    m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge2);
    
    m_node.setOutDegree(2);
    m_node.addOut(m_outEdge1);
    m_node.addOut(m_outEdge2);
    
    assertTrue(m_node.getEdges(m_inNode1).contains(m_inEdge1)); 
    assertTrue(m_node.getEdges(m_inNode2).contains(m_inEdge2));
    assertTrue(m_node.getEdges(m_outNode1).contains(m_outEdge1));
    assertTrue(m_node.getEdges(m_outNode2).contains(m_outEdge2));
    
    //add duplicates
    m_node.setInDegree(4);
    m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge2);
    m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge2);
    
    m_node.setOutDegree(4);
    m_node.addOut(m_outEdge1);
    m_node.addOut(m_outEdge2);
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
    m_node.setInDegree(2);
    m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge2);
    
    assertTrue(m_node.getInEdges().contains(m_inEdge1));
    assertTrue(m_node.getInEdges().contains(m_inEdge2));
    assertTrue(m_node.getInEdges().size() == 2);
  }
  
  public void test_getInEdges_1() {
    m_node.setInDegree(2);
    m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge2);
    
    assertTrue(m_node.getInEdges(m_inNode1).contains(m_inEdge1)); 
    assertTrue(m_node.getInEdges(m_inNode2).contains(m_inEdge2));
    
    m_node.setInDegree(4);
    m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge2);
    m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge2);
    
    assertTrue(m_node.getInEdges(m_inNode1).contains(m_inEdge1));
    assertTrue(m_node.getInEdges(m_inNode1).size() == 2);
      
    assertTrue(m_node.getInEdges(m_inNode2).contains(m_inEdge2));
    assertTrue(m_node.getInEdges(m_inNode2).size() == 2);
  }
  
  public void test_getOutEdges_0() {
    m_node.setOutDegree(2);
    m_node.addOut(m_outEdge1);
    m_node.addOut(m_outEdge2);
    
    assertTrue(m_node.getOutEdges().contains(m_outEdge1));
    assertTrue(m_node.getOutEdges().contains(m_outEdge2));
    assertTrue(m_node.getOutEdges().size() == 2);
  }
  
  public void test_getOutEdges_1() {
    m_node.setOutDegree(2);
    m_node.addOut(m_outEdge1);
    m_node.addOut(m_outEdge2);
    
    assertTrue(m_node.getOutEdges(m_outNode1).contains(m_outEdge1));  
    assertTrue(m_node.getOutEdges(m_outNode2).contains(m_outEdge2));
    
    m_node.setOutDegree(4);
    m_node.addOut(m_outEdge1);
    m_node.addOut(m_outEdge2);
    m_node.addOut(m_outEdge1);
    m_node.addOut(m_outEdge2);
    
    assertTrue(m_node.getOutEdges(m_outNode1).contains(m_outEdge1));
    assertTrue(m_node.getOutEdges(m_outNode1).size() == 2);
      
    assertTrue(m_node.getOutEdges(m_outNode2).contains(m_outEdge2));
    assertTrue(m_node.getOutEdges(m_outNode2).size() == 2);
  }
  
  public void test_getDegree() {
    m_node.setInDegree(1);
    m_node.setOutDegree(1);
    
    m_node.addIn(m_inEdge1);
    m_node.addOut(m_outEdge2);
    
    assertTrue(m_node.getDegree() == 2);  
  }
  
  public void test_getInDegree() {
    m_node.setInDegree(1);
    m_node.setOutDegree(1);
    
    m_node.addIn(m_inEdge1);
    m_node.addOut(m_outEdge2);
    assertTrue(m_node.getInDegree() == 1);  
  }
  
  public void test_getOutDegree() {
    m_node.setInDegree(1);
    m_node.setOutDegree(1);
    
    m_node.addIn(m_inEdge1);
    m_node.addOut(m_outEdge2);
    
    assertTrue(m_node.getOutDegree() == 1); 
  }
  
  public void test_getRelated() {
    m_node.setInDegree(2);
    m_node.setOutDegree(2);
    
    m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge2);
    
    m_node.addOut(m_outEdge1);
    m_node.addOut(m_outEdge2);
   
    Iterator related = m_node.getRelated(); 
    OptDirectedNode dn = (OptDirectedNode)related.next();
    assertTrue(
      dn == m_inNode1 || dn == m_inNode2 || dn == m_outNode1 || dn == m_outNode2
    );
    
    dn = (OptDirectedNode)related.next();
    assertTrue(
      dn == m_inNode1 || dn == m_inNode2 || dn == m_outNode1 || dn == m_outNode2
    );
    
    dn = (OptDirectedNode)related.next();
    assertTrue(
      dn == m_inNode1 || dn == m_inNode2 || dn == m_outNode1 || dn == m_outNode2
    );
    
    dn = (OptDirectedNode)related.next();
    assertTrue(
      dn == m_inNode1 || dn == m_inNode2 || dn == m_outNode1 || dn == m_outNode2
    );
    
    assertTrue(!related.hasNext());
    
    //add loop
    m_node.setInDegree(3);
    m_node.setOutDegree(3);
    
    m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge2);
    
    m_node.addOut(m_outEdge1);
    m_node.addOut(m_outEdge2);
    
    m_node.addIn(m_loop);
    m_node.addOut(m_loop);
    
    related = m_node.getRelated(); 
    dn = (OptDirectedNode)related.next();
    assertTrue(
      dn == m_inNode1 || dn == m_inNode2 || 
      dn == m_outNode1 || dn == m_outNode2 || dn == m_node
    );
    
    dn = (OptDirectedNode)related.next();
    assertTrue(
      dn == m_inNode1 || dn == m_inNode2 || 
      dn == m_outNode1 || dn == m_outNode2 || dn == m_node
    );
    
    dn = (OptDirectedNode)related.next();
    assertTrue(
      dn == m_inNode1 || dn == m_inNode2 || 
      dn == m_outNode1 || dn == m_outNode2 || dn == m_node
    );
    
    dn = (OptDirectedNode)related.next();
    assertTrue(
      dn == m_inNode1 || dn == m_inNode2 || 
      dn == m_outNode1 || dn == m_outNode2 || dn == m_node
    );
    
    dn = (OptDirectedNode)related.next();
    assertTrue(
      dn == m_inNode1 || dn == m_inNode2 || 
      dn == m_outNode1 || dn == m_outNode2 || dn == m_node
    );
    
    dn = (OptDirectedNode)related.next();
    assertTrue(
      dn == m_inNode1 || dn == m_inNode2 || 
      dn == m_outNode1 || dn == m_outNode2 || dn == m_node
    );
    
    assertTrue(!related.hasNext());
  }
  
  public void test_getInRelated() {
    m_node.setInDegree(2);
    m_node.setOutDegree(2);
    
    m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge2);
    
    m_node.addOut(m_outEdge1);
    m_node.addOut(m_outEdge2);
   
    Iterator related = m_node.getInRelated(); 
    OptDirectedNode dn = (OptDirectedNode)related.next();
    assertTrue(dn == m_inNode1 || dn == m_inNode2);
    
    dn = (OptDirectedNode)related.next();
    assertTrue(dn == m_inNode1 || dn == m_inNode2);
    assertTrue(!related.hasNext());
    
    //add a loop
    m_node.setInDegree(3);
    m_node.setOutDegree(3);
    
    m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge2);
    
    m_node.addOut(m_outEdge1);
    m_node.addOut(m_outEdge2);
    
    m_node.addIn(m_loop);
    m_node.addOut(m_loop);
    
    related = m_node.getInRelated(); 
    
    dn = (OptDirectedNode)related.next();
    assertTrue(dn == m_inNode1 || dn == m_inNode2 || dn == m_node);
    
    dn = (OptDirectedNode)related.next();
    assertTrue(dn == m_inNode1 || dn == m_inNode2 || dn == m_node);
    
    dn = (OptDirectedNode)related.next();
    assertTrue(dn == m_inNode1 || dn == m_inNode2 || dn == m_node);
    
    assertTrue(!related.hasNext());
    
  }
  
  public void test_getOutRelated() {
    m_node.setInDegree(2);
    m_node.setOutDegree(2);
    
    m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge2);
    
    m_node.addOut(m_outEdge1);
    m_node.addOut(m_outEdge2);
   
    Iterator related = m_node.getOutRelated(); 
    OptDirectedNode dn = (OptDirectedNode)related.next();
    assertTrue(dn == m_outNode1 || dn == m_outNode2);
    
    dn = (OptDirectedNode)related.next();
    assertTrue(dn == m_outNode1 || dn == m_outNode2);
    
    assertTrue(!related.hasNext());
    
    //add a loop
    m_node.setInDegree(3);
    m_node.setOutDegree(3);
    
    m_node.addIn(m_inEdge1);
    m_node.addIn(m_inEdge2);
    
    m_node.addOut(m_outEdge1);
    m_node.addOut(m_outEdge2);
    
    m_node.addIn(m_loop);
    m_node.addOut(m_loop);
    
    related = m_node.getOutRelated(); 
    
    dn = (OptDirectedNode)related.next();
    assertTrue(dn == m_outNode1 || dn == m_outNode2 || dn == m_node);
    
    dn = (OptDirectedNode)related.next();
    assertTrue(dn == m_outNode1 || dn == m_outNode2 || dn == m_node);
    
    dn = (OptDirectedNode)related.next();
    assertTrue(dn == m_outNode1 || dn == m_outNode2 || dn == m_node);
    
    assertTrue(!related.hasNext());
  } 
}
