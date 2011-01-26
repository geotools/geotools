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
package org.geotools.graph.linegraph;

import junit.framework.TestCase;

import org.geotools.graph.build.line.BasicLineGraphGenerator;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

public class LineGraphGeneratorTest extends TestCase {
  
  private BasicLineGraphGenerator m_gen;
  
  public LineGraphGeneratorTest(String name) {
    super(name);  
  }
  
  protected void setUp() throws Exception {
    super.setUp();

    m_gen = createGenerator();
  }

  /**
   * Build a linear graph by adding a number of line segments that join 
   * at endpoints.
   * <BR> 
   * Expected: 1. Number of edges = number of lines added.
   *           2. Number of nodes = number of lines + 1
   *
   */
  public void test_0() {
    final Coordinate base = new Coordinate(0d,0d);
    final int n = 100;
    for (int i = 1; i <= n; i++) {
      Edge e = (Edge)generator().add(
        new LineSegment(
          new Coordinate(base.x + (i-1), base.y + (i-1)),
          new Coordinate(base.x + i, base.y + i)
        )
      );  
      e.setID(i-1);
      e.getNodeA().setID(i-1);
      e.getNodeB().setID(i);
    }
    
    Graph built = generator().getGraph();
    
    //ensure correct graph structure
    assertTrue(built.getEdges().size() == n);
    assertTrue(built.getNodes().size() == n+1);
    
    GraphVisitor visitor = new GraphVisitor() {
      public int visit(Graphable component) {
        Node node = (Node)component;
        Coordinate c = (Coordinate)node.getObject();
        
        if (node.getDegree() == 1) {
          assertTrue(node.getID() == 0 || node.getID() == n);
        }
        else {
          assertTrue(node.getDegree() == 2);
        }
        
        assertTrue(
          c.x == base.x + node.getID() && c.y == base.y + node.getID() 
        );
        return(0);
      }
    };
    built.visitNodes(visitor);
    
    visitor = new GraphVisitor() {
      public int visit(Graphable component) {
        Edge edge = (Edge)component;
        LineSegment line = (LineSegment)edge.getObject();
        
        assertTrue(line.p1.x == line.p0.x + 1 && line.p1.y == line.p0.y+1);
        
        return(0);
      }
    };
    built.visitEdges(visitor);
    
  }
    
  /**
   * Build a circular graph of line segments that join at endpoints. <BR>
   * <BR>
   * Expected: 1. Number of edges = number of nodes = number of lines.
   */
  public void test_1() {
    final Coordinate base = new Coordinate(0d,0d);
    final int n = 100;
    for (int i = 1; i <= n; i++) {
      Edge e = (Edge)generator().add(
        new LineSegment(
          new Coordinate(base.x + (i-1), base.y + (i-1)),
          new Coordinate(base.x + i, base.y + i)
        )
      );  
      e.setID(i-1);
      e.getNodeA().setID(i-1);
      e.getNodeB().setID(i);
    }
    
    //complete the circle
    generator().add(
      new LineSegment(new Coordinate(base.x + n, base.y + n), base) 
    );
    
    Graph built = generator().getGraph();
    
    assertTrue(built.getEdges().size() == n+1);
    assertTrue(built.getNodes().size() == n+1);
    
    //all nodes should be of degree 2
    assertTrue(
      built.getNodesOfDegree(2).size() == built.getNodes().size()
    );
  }
   
  protected BasicLineGraphGenerator createGenerator() {
    return(new BasicLineGraphGenerator());
  }
  
  protected BasicLineGraphGenerator generator() {
    return(m_gen);  
  }
}
