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
package org.geotools.graph.io.standard;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;

import junit.framework.TestCase;

import org.geotools.graph.GraphTestUtil;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.basic.BasicGraphBuilder;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;

public class BasicGraphSerializerTest extends TestCase {

  private GraphBuilder m_builder;
  private GraphBuilder m_rebuilder;
  private SerializedReaderWriter m_serializer;
   
  public BasicGraphSerializerTest(String name) {
    super(name);  
  } 
  
  protected void setUp() throws Exception {
    super.setUp();
    
    m_builder = createBuilder();
    m_rebuilder = createRebuilder();
    m_serializer = new SerializedReaderWriter();
    m_serializer.setProperty(SerializedReaderWriter.BUILDER, rebuilder());
  }
  
  /**
   * Create a simple graph with no bifurcations and serialize, then deserialize 
   * <BR>
   * <BR>
   * Expected: 1. before and after graph should have same structure.
   *
   */
  public void test_0() throws Exception{
    final int nnodes = 100;
    GraphTestUtil.buildNoBifurcations(builder(), nnodes);    
    File victim = File.createTempFile( "graph", null );
    victim.deleteOnExit();
    
    m_serializer.setProperty(SerializedReaderWriter.FILENAME, victim.getAbsolutePath() );
      
      m_serializer.write(builder().getGraph());
      
      Graph before = builder().getGraph();
      Graph after = m_serializer.read();
      
      //ensure same number of nodes and edges
      assertTrue(before.getNodes().size() == after.getNodes().size());
      assertTrue(before.getEdges().size() == after.getEdges().size());
      
      //ensure same graph structure
      GraphVisitor visitor = new GraphVisitor() {
        public int visit(Graphable component) {
          Edge e = (Edge)component;
          
          assertTrue(e.getNodeA().getID() == e.getID());
          assertTrue(e.getNodeB().getID() == e.getID()+1);
          
          return(0);
        }
      };
      after.visitEdges(visitor);
      
      visitor = new GraphVisitor() {
        public int visit(Graphable component) {
          Node n = (Node)component;
          
          if (n.getDegree() == 1) {
            assertTrue(n.getID() == 0 || n.getID() == nnodes-1);  
          }
          else {
            assertTrue(n.getDegree() == 2);
            
            Edge e0 = (Edge)n.getEdges().get(0);
            Edge e1 = (Edge)n.getEdges().get(1);
            
            assertTrue(
              (e0.getID() == n.getID()-1 && e1.getID() == n.getID()) ||
              (e1.getID() == n.getID()-1 && e0.getID() == n.getID())
            );
            
          }
          
          return(0);
        }
      };
      after.visitNodes(visitor);
  }
  
  /**
   * Create a perfect binary tree, serialize it and deserialize it. <BR>
   * <BR>
   * Expected: 1. Same structure before and after.
   *
   */
  public void test_1() {
    final int k = 5;
    Object[] obj = GraphTestUtil.buildPerfectBinaryTree(builder(), k);
    final Node root = (Node)obj[0];
    final Map obj2node = (Map)obj[1];    
    
    try {
      File victim = File.createTempFile( "graph", null );
      victim.deleteOnExit();
      m_serializer.setProperty(SerializedReaderWriter.FILENAME, victim.getAbsolutePath() );
      
      m_serializer.write(builder().getGraph());
      
      Graph before = builder().getGraph();
      Graph after = m_serializer.read();
      
      //ensure same number of nodes and edges
      assertTrue(before.getNodes().size() == after.getNodes().size());
      assertTrue(before.getEdges().size() == after.getEdges().size());
      
      //ensure same structure
      GraphVisitor visitor = new GraphVisitor() {
        public int visit(Graphable component) {
          Node n = (Node)component;
          String id = (String)n.getObject();
          
          assertTrue(obj2node.get(id) != null);
          
          StringTokenizer st = new StringTokenizer(id, ".");
          
          if (st.countTokens() == 1) {
            //root
            assertTrue(n.getDegree() == 2); 
            
            Node n0 = ((Edge)n.getEdges().get(0)).getOtherNode(n);
            Node n1 = ((Edge)n.getEdges().get(1)).getOtherNode(n);
            
            assertTrue(
              n0.getObject().equals("0.0") && n1.getObject().equals("0.1")
           || n0.getObject().equals("0.1") && n1.getObject().equals("0.0")
            );
          }
          else if (st.countTokens() == k+1) {
            //leaf
            assertTrue(n.getDegree() == 1);
            
            Node parent = ((Edge)n.getEdges().get(0)).getOtherNode(n);
            String parentid = (String)parent.getObject();
            
            assertTrue(parentid.equals(id.substring(0, id.length()-2)));   
          } 
          else {
            //internal
            assertTrue(n.getDegree() == 3);
            
            String id0 = ((Edge)n.getEdges().get(0)).getOtherNode(n).getObject()
                           .toString();
            String id1 = ((Edge)n.getEdges().get(1)).getOtherNode(n).getObject()
                           .toString();
            String id2 = ((Edge)n.getEdges().get(2)).getOtherNode(n).getObject()
                           .toString();
            
            String parentid = id.substring(0, id.length()-2);
            
            assertTrue(
             id0.equals(parentid) && id1.equals(id+".0") && id2.equals(id+".1")
          || id0.equals(parentid) && id2.equals(id+".0") && id1.equals(id+".1")
          || id1.equals(parentid) && id0.equals(id+".0") && id2.equals(id+".1")
          || id1.equals(parentid) && id2.equals(id+".0") && id0.equals(id+".1")
          || id2.equals(parentid) && id0.equals(id+".0") && id1.equals(id+".1")
          || id2.equals(parentid) && id1.equals(id+".0") && id0.equals(id+".1")
            );
          }  
          
          return(0);
        }
      };
      after.visitNodes(visitor);
      
    }
    catch(Exception e) {
      e.printStackTrace();
      assertTrue(false);  
    }
  }
  
  /**
   * Create a simple graph and disconnect two nodes (remove all edges) 
   * then serialize and deserialize. <BR>
   * <BR>
   * Exepcted: 1. Same graph structure.
   *
   */
  public void test_2() {
    final int nnodes = 100;
    Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), nnodes);
    
    HashSet toRemove = new HashSet();
    toRemove.addAll(ends[0].getEdges());
    toRemove.addAll(ends[1].getEdges());
    
    //disconnect end nodes
    builder().removeEdges(toRemove);
    
    assertTrue(builder().getGraph().getNodes().size() == nnodes); 
    assertTrue(builder().getGraph().getEdges().size() == nnodes-3);
    
    try {
        File victim = File.createTempFile( "graph", null );
        victim.deleteOnExit();
      m_serializer.setProperty(SerializedReaderWriter.FILENAME, victim.getAbsolutePath());
      
      m_serializer.write(builder().getGraph());
      
      Graph before = builder().getGraph();
      Graph after = m_serializer.read();
      
      //ensure same number of nodes and edges
      assertTrue(before.getNodes().size() == after.getNodes().size());
      assertTrue(before.getEdges().size() == after.getEdges().size());
      
      GraphVisitor visitor = new GraphVisitor() {
        public int visit(Graphable component) {
          Node n = (Node)component;
          if (n.getID() == 0 || n.getID() == nnodes-1) 
            assertTrue(n.getDegree() == 0);
          else if (n.getID() == 1 || n.getID() == nnodes-2)
            assertTrue(n.getDegree() == 1);
          else assertTrue(n.getDegree() == 2); 
           
          return(0); 
        }
      };
      after.visitNodes(visitor);
    }
    catch(Exception e) {
      e.printStackTrace();
      assertTrue(false); 
    }
  }
  
  protected GraphBuilder createBuilder() {
    return(new BasicGraphBuilder());  
  }
  
  protected GraphBuilder createRebuilder() {
    return(new BasicGraphBuilder());  
  }
  
  protected GraphBuilder builder() {
    return(m_builder);  
  }

  protected GraphBuilder rebuilder() {
    return(m_rebuilder);  
  }
  
  protected SerializedReaderWriter serializer() {
    return(m_serializer);  
  }
    
}
