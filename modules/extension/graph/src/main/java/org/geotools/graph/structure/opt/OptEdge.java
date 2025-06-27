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

import java.util.ArrayList;
import java.util.Iterator;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Node;

/**
 * Optimized implementation of Edge.
 *
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 * @see Edge
 */
public class OptEdge extends OptGraphable implements Edge {

    /** a node * */
    private OptNode m_nodeA;

    /** b node * */
    private OptNode m_nodeB;

    /**
     * Constructs a new optimized edge.
     *
     * @param nodeA A node of edge.
     * @param nodeB B node of edge.
     */
    public OptEdge(OptNode nodeA, OptNode nodeB) {
        m_nodeA = nodeA;
        m_nodeB = nodeB;
    }

    /** @see Edge#getNodeA() */
    @Override
    public Node getNodeA() {
        return m_nodeA;
    }

    /** @see Edge#getNodeB() */
    @Override
    public Node getNodeB() {
        return m_nodeB;
    }

    /** @see Edge#getOtherNode(Node) */
    @Override
    public Node getOtherNode(Node node) {
        return m_nodeA.equals(node) ? m_nodeB : m_nodeB.equals(node) ? m_nodeA : null;
    }

    /** @see Edge#reverse() */
    @Override
    public void reverse() {
        OptNode tmp = m_nodeA;
        m_nodeA = m_nodeB;
        m_nodeB = tmp;
    }

    /** @see Edge#compareNodes(Edge) */
    @Override
    public int compareNodes(Edge other) {
        if (m_nodeA.equals(other.getNodeA()) && m_nodeB.equals(other.getNodeB())) return Edge.EQUAL_NODE_ORIENTATION;

        if (m_nodeB.equals(other.getNodeA()) && m_nodeA.equals(other.getNodeB())) return Edge.OPPOSITE_NODE_ORIENTATION;

        return Edge.UNEQUAL_NODE_ORIENTATION;
    }

    /** @see org.geotools.graph.structure.Graphable#getRelated() */
    @Override
    public Iterator<Edge> getRelated() {
        return new RelatedIterator(this);
    }

    public class RelatedIterator implements Iterator<Edge> {

        private Iterator<Edge> m_itr;

        public RelatedIterator(OptEdge edge) {
            ArrayList<Edge> edges = new ArrayList<>(m_nodeA.getDegree()
                    + m_nodeB.getDegree()
                    - 2
                    - m_nodeA.getEdges(m_nodeB).size());

            // add all edges of node A except this edge
            for (int i = 0; i < m_nodeA.getEdgeArray().length; i++) {
                Edge e = m_nodeA.getEdgeArray()[i];
                if (!e.equals(edge)) edges.add(m_nodeA.getEdgeArray()[i]);
            }

            // add only edges from node b that are node shared with node a
            for (int i = 0; i < m_nodeB.getEdgeArray().length; i++) {
                Edge e = m_nodeB.getEdgeArray()[i];
                if (!e.getOtherNode(m_nodeB).equals(m_nodeA)) edges.add(e);
            }

            m_itr = edges.iterator();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException(getClass().getName() + "#remove() not supported.");
        }

        @Override
        public boolean hasNext() {
            return m_itr.hasNext();
        }

        @Override
        public Edge next() {
            return m_itr.next();
        }
    }
}
