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
import java.util.Map;

import junit.framework.TestCase;

import org.geotools.graph.GraphTestUtil;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.basic.BasicGraphBuilder;
import org.geotools.graph.path.DijkstraShortestPathFinder;
import org.geotools.graph.path.Path;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Node;
import org.geotools.graph.traverse.standard.DijkstraIterator;
import static org.junit.Assert.*;

/**
 * 
 *
 * @source $URL$
 */
public class DijkstraShortestPathFinderTest extends TestCase {  

  private GraphBuilder m_builder;
  
  public DijkstraShortestPathFinderTest(String name) {
    super(name);  
  }
  
  protected void setUp() throws Exception {
    super.setUp();
    
    m_builder = createBuilder();
  }
  
  /**
   * Create a graph with no bifurcations and calculate path from beginning
   * to end. <BR>
   * <BR>
   * Expected: 1. Path should contain every node in graph in order.
   */
  public void test_0() {
    int nnodes = 100;
    Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), nnodes);
    DijkstraShortestPathFinder pfinder = new DijkstraShortestPathFinder(
      builder().getGraph(), ends[0], costFunction()
    );
    
    pfinder.calculate();
    Path p = pfinder.getPath(ends[1]);
    
    int count = 99;
    for (Iterator itr = p.iterator(); itr.hasNext();) {
      Node n = (Node)itr.next();
      assertTrue(n.getID() == count--);
    }
  }
  
  /**
   * Create a circular graph and calculate a path from beginning to end. <BR>
   * <BR>
   * Expected: 1. Path should just contain end nodes.
   *
   */
  public void test_1() {
    int nnodes = 100;
    Node[] ends = GraphTestUtil.buildCircular(builder(), nnodes);
    
    DijkstraShortestPathFinder pfinder = new DijkstraShortestPathFinder(
      builder().getGraph(), ends[0], costFunction()
    );
    
    pfinder.calculate();
    Path p = pfinder.getPath(ends[1]);
    
    assertTrue(p.size() == 2);
    assertTrue(p.get(0) == ends[1]);
    assertTrue(p.get(1) == ends[0]);
  }
  
  /**
   * Create a balanced binary tree and calculate a path from root to a leaf. <BR>
   * <BR>
   * Expected 1. Path should be links from leaf to root. 
   */
  public void test_2() {
    int k = 4;
    Object[] obj = GraphTestUtil.buildPerfectBinaryTree(builder(), k);
    Node root = (Node)obj[0];
    Map id2node = (Map)obj[1];
    
    DijkstraShortestPathFinder pfinder = new DijkstraShortestPathFinder(
      builder().getGraph(), root, costFunction()
    );
    pfinder.calculate();
    
    for (Iterator itr = builder().getGraph().getNodes().iterator(); itr.hasNext();) {
      Node node = (Node)itr.next();
      String id = node.getObject().toString();
      
      if (id2node.get(id + ".0") == null) {
        Path p = pfinder.getPath(node);
        assertTrue(p.size() == k+1);  
     
        for (Iterator pitr = p.iterator(); pitr.hasNext();) {
          Node n = (Node)pitr.next();
          assertTrue(n.getObject().toString().equals(id));
          if (id.length() > 2) id = id.substring(0, id.length()-2);
        }    
      }  
    }
  }
  
  /**
   * This tests the functionality of the NodeWeighter with equal node cost.
   * 
   * The test creates a graph with 4 nodes that consists of a single bifurcation.
   * <BR><BR>
   * 
   * The shortest path is via the bifurcation (4 nodes), this is tested in the first stage. 
   * In the second stage the bifurcation path is forbidden by increasing the node cost for the bifurcation node 
   * if the outgoing edge is towards the bifurcation (new path consists of 99 nodes).
   * 
   * <BR><BR>
   * Expected: <BR>
   * 1. The node cost should be 0 for the source, 1 for the bifurcation and 3 for the two leafs.
   */
  public void test_3() {
	  	double[] expected = { 0.0, 1.0, 3.0, 3.0 };
	    int nnodes = 4;
	    
	    // Creating a single bifurcation graph.
	    Object[] singleBif = GraphTestUtil.buildSingleBifurcation(builder(), nnodes, 1);
	    
	    // Getting the source.
	    Node source = (Node)builder().getGraph().getNodes().iterator().next();

        // Creating a path finder and calculates the paths.
        DijkstraShortestPathFinder pf = new DijkstraShortestPathFinder(builder().getGraph(), 
        															   source, 
        															   costFunction(), 
        															   tcostFunction());

        pf.calculate();

        // Extracting the actual node costs.
        ArrayList<Double> gotArray = new ArrayList<Double>(nnodes);
        for (Iterator it = builder().getGraph().getNodes().iterator() ; it.hasNext();)
        {

        	Node d = (Node) it.next();

            Path path = pf.getPath(d);

            gotArray.add(pf.getCost(d));

        }
        
        double[] actual = new double[gotArray.size()];
        for (int i = 0 ; i < actual.length ; i++)
        {
        	actual[i] = gotArray.get(i);
        }
        
        // Testing if the actual node cost is the same as the expected.
        assertArrayEquals(expected, actual, 0.1);
	  }
  
  /**
   * This tests the functionality of the NodeWeighter that forbids turning by increasing the node cost.
   * 
   * The test creates a graph with 100 nodes that consists of a single bifurcation and 
   * the bifurcation is connected to the end of the graph.
   * <BR><BR>
   * 
   * The shortest path is via the bifurcation (4 nodes), this is tested in the first stage. 
   * In the second stage the bifurcation path is forbidden by increasing the node cost for the bifurcation node 
   * if the outgoing edge is towards the bifurcation (new path consists of 99 nodes).
   * 
   * <BR>
   * Expected: <BR>
   * 1. Path should contain 4 nodes when the NodeWeighter is not used.<BR>
   * 2. Path should contain 99 nodes when the NodeWeighter is used. 
   */
  public void test_4()
  {
	  int nnodes = 100;
	  int bifurcationId = 1;
	  
	  // Creating a single bifurcation graph and connects the bifurcation to the end of the graph.
	  Object[] singleBif = GraphTestUtil.buildSingleBifurcation(builder(), nnodes, bifurcationId);
	  Node source = (Node)singleBif[0];
	  Node destination = (Node) singleBif[1];
	  Node bifurcation = (Node) singleBif[2];
	  Edge splitEdge = null;
	  
	  for (Iterator it = bifurcation.getEdges().iterator() ; it.hasNext();)
	  {
		  Edge edge = (Edge) it.next();
		  Node otherNode = edge.getOtherNode(bifurcation);
		  
		  if (otherNode != source && otherNode.getEdges().size() == 1)
		  {
			  splitEdge = edge;
			  builder().addEdge(builder().buildEdge(otherNode, destination));
		  }
	  }
	  
	  final Edge finalSplitEdge = splitEdge;
	  
	  // Testing the path finder without the NodeWeighter
	  DijkstraShortestPathFinder pfinder = new DijkstraShortestPathFinder(builder().getGraph(), source, costFunction());
	  
	  pfinder.calculate();
	  
	  Path path = pfinder.getPath(destination);
	  
	  assertEquals("Shortest path without turning restriction", 4, path.size());
	  
	  
	  // Creating a NodeWeighter that forbids the use of the bifurcation.
	  DijkstraIterator.NodeWeighter nodeWeighted = new DijkstraIterator.NodeWeighter() 
	  {
		@Override
		public double getWeight(Node n, Edge e1, Edge e2) 
		{
			if (n == bifurcation && e1 == source.getEdge(bifurcation) && e2 == finalSplitEdge)
			{
				return Double.MAX_VALUE;
			}
			else
			{
				return 0;
			}
		}
	  };

	  // Testing the path finder with the NodeWeighter
	  pfinder = new DijkstraShortestPathFinder(builder().getGraph(), source, costFunction(), nodeWeighted);
	  
	  pfinder.calculate();
	  
	  path = pfinder.getPath(destination);
	  
	  assertEquals("Shortest path without turning restriction", 99, path.size());
  }

  protected DijkstraIterator.EdgeWeighter costFunction() {
    return(
      new DijkstraIterator.EdgeWeighter() {
        public double getWeight(Edge e) {
          return 1;
        }
      }
    );
  }
  
  protected DijkstraIterator.NodeWeighter tcostFunction() {
      return (new DijkstraIterator.NodeWeighter() {
          public double getWeight(Node n, Edge e1, Edge e2) {
              return 1.0;
          }
      });
  }
  
  protected GraphBuilder createBuilder() {
    return(new BasicGraphBuilder());  
  }
  
  protected GraphBuilder builder() {
    return(m_builder);  
  }

}
