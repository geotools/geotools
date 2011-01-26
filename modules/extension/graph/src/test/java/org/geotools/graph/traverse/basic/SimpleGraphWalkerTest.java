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
package org.geotools.graph.traverse.basic;

import junit.framework.TestCase;

import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.geotools.graph.structure.basic.BasicNode;
import org.geotools.graph.traverse.GraphTraversal;

public class SimpleGraphWalkerTest extends TestCase {
  private boolean m_visited;
  
  public SimpleGraphWalkerTest(String name) {
    super(name);
  }
  
  public void test_visit() {
    m_visited = false;
    
    GraphVisitor visitor = new GraphVisitor() {
      public int visit(Graphable component) {
        m_visited = true;
        return(GraphTraversal.CONTINUE);  
      }
    }; 
    
    Node n = new BasicNode();
    n.setVisited(false);
    
    SimpleGraphWalker walker = new SimpleGraphWalker(visitor);
    
    assertTrue(walker.visit(n, null) == GraphTraversal.CONTINUE);
    assertTrue(m_visited);
  }
}
