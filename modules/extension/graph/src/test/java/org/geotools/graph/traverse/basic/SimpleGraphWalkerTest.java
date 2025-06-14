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

import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Node;
import org.geotools.graph.structure.basic.BasicNode;
import org.geotools.graph.traverse.GraphTraversal;
import org.junit.Assert;
import org.junit.Test;

public class SimpleGraphWalkerTest {
    private boolean m_visited;

    @Test
    public void test_visit() {
        m_visited = false;

        GraphVisitor visitor = component -> {
            m_visited = true;
            return GraphTraversal.CONTINUE;
        };

        Node n = new BasicNode();
        n.setVisited(false);

        SimpleGraphWalker walker = new SimpleGraphWalker(visitor);

        Assert.assertEquals(walker.visit(n, null), GraphTraversal.CONTINUE);
        Assert.assertTrue(m_visited);
    }
}
