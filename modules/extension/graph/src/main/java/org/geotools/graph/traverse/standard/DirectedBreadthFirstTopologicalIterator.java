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
package org.geotools.graph.traverse.standard;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import org.geotools.graph.structure.DirectedGraphable;
import org.geotools.graph.structure.DirectedNode;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.basic.AbstractGraphIterator;

public class DirectedBreadthFirstTopologicalIterator extends AbstractGraphIterator {

    private Queue<Graphable> m_queue;

    @Override
    public void init(Graph graph, GraphTraversal traversal) {
        // create queue
        m_queue = buildQueue(graph);

        // initialize nodes
        graph.visitNodes(
                new GraphVisitor() {
                    @Override
                    public int visit(Graphable component) {
                        DirectedNode node = (DirectedNode) component;

                        node.setVisited(false);
                        node.setCount(0);

                        if (node.getInDegree() == 0) m_queue.add(node);

                        return (0);
                    }
                });
    }

    @Override
    public Graphable next(GraphTraversal traversal) {
        return (!m_queue.isEmpty() ? (Graphable) m_queue.remove() : null);
    }

    @Override
    public void cont(Graphable current, GraphTraversal traversal) {
        // increment the count of all adjacent nodes by one
        // if the result count equal to the degree, place it into the queue
        DirectedGraphable directed = (DirectedGraphable) current;
        for (Iterator<? extends Graphable> itr = directed.getOutRelated(); itr.hasNext(); ) {
            DirectedNode related = (DirectedNode) itr.next();
            if (!traversal.isVisited(related)) {
                related.setCount(related.getCount() + 1);
                if (related.getInDegree() == related.getCount()) m_queue.add(related);
            }
        }
    }

    @Override
    public void killBranch(Graphable current, GraphTraversal traversal) {
        // do nothing
    }

    protected Queue<Graphable> buildQueue(Graph graph) {
        return (new ArrayDeque<>(graph.getNodes().size()));
    }
}
