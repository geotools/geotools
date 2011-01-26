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
package org.geotools.graph.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.geotools.graph.GraphTestUtil;
import org.geotools.graph.build.GraphGenerator;
import org.geotools.graph.build.basic.BasicGraphGenerator;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.geotools.graph.util.graph.GraphFuser;

public class GraphFuserTest extends TestCase {
  
  private GraphGenerator m_gen;
  
  public GraphFuserTest(String name) {
    super(name);  
  }
  
  protected void setUp() throws Exception {
    super.setUp();
    
    m_gen = createGenerator();  
  }
  
  /**
   * Create a no bifurcation graph and fuse it. <BR>
   * <BR>
   * Expected: 1. Resulting graph should only have 2 nodes and 1 edge. <BR>
   * <BR>
   *  O---O---O-...-O---O---O  ==FUSER=>  O-------...-------O  
   *
   */
  public void test_0() {
    final int nnodes = 100;
    Node[] ends = GraphTestUtil.buildNoBifurcations(generator(), nnodes);
    GraphGenerator fused = createGenerator();
    
    GraphFuser fuser = new GraphFuser(
      generator().getGraph(), generator().getGraphBuilder(), createEdgeMerger()
    );
    assertTrue(fuser.fuse());
    
    assertTrue(generator().getGraph().getNodes().size() == 2);
    assertTrue(generator().getGraph().getEdges().size() == 1);
    
    GraphVisitor visitor = new GraphVisitor() {
      public int visit(Graphable component) {
        String id = (String)component.getObject();
        assertTrue(id.equals("0") || id.equals(String.valueOf(nnodes-1)));
        return(0);
      }
    };
    generator().getGraph().visitNodes(visitor);
    
    visitor = new GraphVisitor() {
      public int visit(Graphable component) {
        Edge e = (Edge)component;
        String id0 = (String)e.getNodeA().getObject();
        String id1 = (String)e.getNodeB().getObject();
        
        assertTrue(
          (id0.equals("0") && id1.equals(String.valueOf(nnodes-1)))||
          (id0.equals(String.valueOf(nnodes-1)) && id1.equals("0"))
        );
           
        return(0);
      }
    };
    generator().getGraph().visitEdges(visitor);
  }
  
  /**
   * Build a no bifurcation graph and then add a cycle containing two adjacent
   * nodes and fuse.  <BR>
   * <BR>
   * Expected: 1. The graph should have 4 nodes and 4 edges. <BR>
   * <BR>
   *              ___                                ___
   *             /   \                              /   \
   *  O---O-...-O    O-...-O---O ==FUSER=> O--...--O    O--...--O
   *             \___/                              \___/
   *
   */
  public void test_1() {
    final int nnodes = 100;
    final int cyc = 50;
    
    Node[] ends = GraphTestUtil.buildNoBifurcations(generator(), nnodes);
    generator().add(new Object[]{String.valueOf(cyc), String.valueOf(cyc+1)});
    GraphGenerator fused = createGenerator();
    
    GraphFuser fuser = new GraphFuser(
      generator().getGraph(), generator().getGraphBuilder(), createEdgeMerger()
    );
    assertTrue(fuser.fuse());
    
    assertTrue(generator().getGraph().getNodes().size() == 4);
    assertTrue(generator().getGraph().getEdges().size() == 4);
    
    GraphVisitor visitor = new GraphVisitor() {
      public int visit(Graphable component) {
        String id = (String)component.getObject();
        assertTrue(
          (id.equals("0") || id.equals(String.valueOf(nnodes-1))) 
       || (id.equals(String.valueOf(cyc)) || id.equals(String.valueOf(cyc+1)))
        );
        return(0);
      }
    };
    generator().getGraph().visitNodes(visitor);
    
    visitor = new GraphVisitor() {
      public int visit(Graphable component) {
        Edge e = (Edge)component;
        String id0 = (String)e.getNodeA().getObject();
        String id1 = (String)e.getNodeB().getObject();
        
        assertTrue(
          (id0.equals("0") && id1.equals(String.valueOf(cyc)))|| 
          (id0.equals(String.valueOf(cyc)) && id1.equals("0"))||
          (id0.equals(String.valueOf(cyc)) && id1.equals(String.valueOf(cyc+1)))||
          (id0.equals(String.valueOf(cyc+1)) && id1.equals(String.valueOf(cyc)))||
          (id0.equals(String.valueOf(cyc+1)) && id1.equals(String.valueOf(nnodes-1)))||
          (id0.equals(String.valueOf(nnodes-1)) && id1.equals(String.valueOf(cyc+1)))
        );
        
        return(0);
      }
    };
    generator().getGraph().visitEdges(visitor);
  }
  
  /**
   * Create a graph with no bifurcations and a cycle between containing three 
   *  nodes and fuse it. <BR>
   * <BR>
   * Expected: 1. The graph should have 4 nodes and 4 edges. <BR>
   * <BR>
   *              _____                                _____
   *             /     \                              /     \
   *  O---O-...-O---O---O-...-O---O ==FUSER=> O--...-O------O-...--O
   *
   */
  public void test_2() {
    final int nnodes = 100;
    final int cyc = 50;
    
    Node[] ends = GraphTestUtil.buildNoBifurcations(generator(), nnodes);
    generator().add(new Object[]{String.valueOf(cyc), String.valueOf(cyc+2)});
    GraphGenerator fused = createGenerator();
    
    GraphFuser fuser = new GraphFuser(
      generator().getGraph(), generator().getGraphBuilder(), createEdgeMerger()
    );
    assertTrue(fuser.fuse());
    
    assertTrue(generator().getGraph().getNodes().size() == 4);
    assertTrue(generator().getGraph().getEdges().size() == 4);
    
    GraphVisitor visitor = new GraphVisitor() {
      public int visit(Graphable component) {
        String id = (String)component.getObject();
        assertTrue(
          (id.equals("0") || id.equals(String.valueOf(nnodes-1))) 
       || (id.equals(String.valueOf(cyc)) || id.equals(String.valueOf(cyc+2)))
        );
        return(0);
      }
    };
    generator().getGraph().visitNodes(visitor);
    
    visitor = new GraphVisitor() {
      public int visit(Graphable component) {
        Edge e = (Edge)component;
        String id0 = (String)e.getNodeA().getObject();
        String id1 = (String)e.getNodeB().getObject();
        
        assertTrue(
          (id0.equals("0") && id1.equals(String.valueOf(cyc)))|| 
          (id0.equals(String.valueOf(cyc)) && id1.equals("0"))||
          (id0.equals(String.valueOf(cyc)) && id1.equals(String.valueOf(cyc+2)))||
          (id0.equals(String.valueOf(cyc+2)) && id1.equals(String.valueOf(cyc)))||
          (id0.equals(String.valueOf(cyc+2)) && id1.equals(String.valueOf(nnodes-1)))||
          (id0.equals(String.valueOf(nnodes-1)) && id1.equals(String.valueOf(cyc+2)))
        );
        
        return(0);
      }
    };
    generator().getGraph().visitEdges(visitor);
  }
   
  /**
   * Create a circular graph and fuse it. <BR>
   * <BR>
   * Expected: 1. The graph should have 1 node and 1 edge, a loop on one node.
   * 
   */
  public void test_3() {
    int nnodes = 100;
    GraphTestUtil.buildCircular(generator(), nnodes);
    
    GraphGenerator fused = createGenerator();
    
    GraphFuser fuser = new GraphFuser(
      generator().getGraph(), generator().getGraphBuilder(), createEdgeMerger()
    );
    assertTrue(fuser.fuse());
    
    assertTrue(generator().getGraph().getNodes().size() == 1);
    assertTrue(generator().getGraph().getEdges().size() == 1);
  }
  
  protected GraphGenerator createGenerator() {
    return(new BasicGraphGenerator());  
  }
  
  protected GraphGenerator generator() {
    return(m_gen);
  }
  
  protected GraphFuser.EdgeMerger createEdgeMerger() {
    return (
      new GraphFuser.EdgeMerger() {
        public Object merge(List edges) {
          ArrayList ends = new ArrayList();
          for (Iterator itr = edges.iterator(); itr.hasNext();) {
            Edge edge = (Edge)itr.next();
            if (
              edge.getNodeA().getDegree()!=2 || edge.getNodeB().getDegree()!=2 
            ) ends.add(edge);  
          }  
          
          Object[] obj = new Object[2];
          
          if (ends.size() == 2) {
            Edge end0 = (Edge)ends.get(0);
            Edge end1 = (Edge)ends.get(1);
          
            obj[0] = end0.getNodeA().getDegree() == 2 
                      ? end0.getNodeB().getObject() : end0.getNodeA()
                          .getObject();
            obj[1] = end1.getNodeA().getDegree() == 2 
                      ? end1.getNodeB().getObject() : end1.getNodeA()
                          .getObject();  
          } 
          else if (ends.size() == 0) {
            obj[0] = ((Edge)edges.get(0)).getNodeA().getObject();
            obj[1] = ((Edge)edges.get(0)).getNodeA().getObject();    
          } 
          else 
            throw new IllegalStateException("Found " + ends.size() + " ends."); 
        
          return(obj);
        }
        
        public void setMergedObject(Edge newEdge, Object merged) {
          newEdge.setObject(merged);
        }

				public void setMergedObject(Edge newEdge, Object merged, List edges) {
	        newEdge.setObject(merged);
				}
      }
    ); 
  }
}
