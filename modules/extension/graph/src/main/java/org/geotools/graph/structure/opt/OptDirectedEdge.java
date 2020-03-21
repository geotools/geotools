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
import org.geotools.graph.structure.DirectedEdge;
import org.geotools.graph.structure.DirectedGraphable;
import org.geotools.graph.structure.DirectedNode;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;

/**
 * Optimized implementation of DirectedEdge.
 *
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 * @see DirectedEdge
 */
public class OptDirectedEdge extends OptGraphable implements DirectedEdge {

    /** in node * */
    private OptDirectedNode m_in;

    /** out node * */
    private OptDirectedNode m_out;

    /**
     * Constructs a new OptDirectedEdge.
     *
     * @param in Optimized in node.
     * @param out Optimized out node.
     */
    public OptDirectedEdge(OptDirectedNode in, OptDirectedNode out) {
        m_in = in;
        m_out = out;
    }

    /** @see DirectedEdge#getInNode() */
    @Override
    public DirectedNode getInNode() {
        return (m_in);
    }

    /** @see DirectedEdge#getOutNode() */
    @Override
    public DirectedNode getOutNode() {
        return (m_out);
    }

    /** @see Edge#getNodeA() */
    @Override
    public Node getNodeA() {
        return (m_in);
    }

    /** @see Edge#getNodeB() */
    @Override
    public Node getNodeB() {
        return (m_out);
    }

    /** @see Edge#getOtherNode(Node) */
    @Override
    public Node getOtherNode(Node node) {
        return (node == m_in ? m_out : node == m_out ? m_in : null);
    }

    /** Unsupported Operation. */
    @Override
    public void reverse() {
        throw new UnsupportedOperationException(getClass().getName() + "#reverse()");
    }

    /** @see Edge#compareNodes(Edge) */
    @Override
    public int compareNodes(Edge other) {
        if (m_in.equals(other.getNodeA()) && m_out.equals(other.getNodeB()))
            return (Edge.EQUAL_NODE_ORIENTATION);

        if (m_in.equals(other.getNodeB()) && m_out.equals(other.getNodeA()))
            return (Edge.OPPOSITE_NODE_ORIENTATION);

        return (Edge.UNEQUAL_NODE_ORIENTATION);
    }

    /** @see Graphable#getRelated() */
    @Override
    public Iterator<? extends Graphable> getRelated() {
        ArrayList<Edge> related = new ArrayList<>(m_in.getDegree() + m_out.getDegree() - 2);

        Edge[] edges = m_in.getInEdgeArray();
        for (int i = 0; i < edges.length; i++) {
            related.add(edges[i]);
        }

        edges = m_in.getOutEdgeArray();
        for (int i = 0; i < edges.length; i++) {
            Edge e = edges[i];
            if (!e.equals(this) && !(e.getNodeA().equals(e.getNodeB()))) related.add(edges[i]);
        }

        edges = m_out.getInEdgeArray();
        for (int i = 0; i < edges.length; i++) {
            Edge e = edges[i];

            switch (compareNodes(e)) {
                case Edge.EQUAL_NODE_ORIENTATION:
                case Edge.OPPOSITE_NODE_ORIENTATION:
                    continue; // already added

                case Edge.UNEQUAL_NODE_ORIENTATION:
                    related.add(e);
            }
        }

        edges = m_out.getOutEdgeArray();
        for (int i = 0; i < edges.length; i++) {
            Edge e = edges[i];

            switch (compareNodes(edges[i])) {
                case Edge.EQUAL_NODE_ORIENTATION:
                case Edge.OPPOSITE_NODE_ORIENTATION:
                    continue; // already added

                case Edge.UNEQUAL_NODE_ORIENTATION:
                    if (!e.getNodeA().equals(e.getNodeB())) related.add(e);
            }
        }

        return (related.iterator());
    }

    /** @see DirectedGraphable#getInRelated() */
    @Override
    public Iterator<? extends Graphable> getInRelated() {
        return (new RelatedIterator(RelatedIterator.IN));
    }

    /** @see DirectedGraphable#getOutRelated() */
    @Override
    public Iterator<? extends Graphable> getOutRelated() {
        return (new RelatedIterator(RelatedIterator.OUT));
    }

    /**
     * Iterator used to iterate over adjacent edges.
     *
     * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
     */
    public class RelatedIterator implements Iterator<Graphable> {
        /** in mode * */
        public static final int IN = 0;

        /** out mode * */
        public static final int OUT = 1;

        /** both mode * */
        public static final int BOTH = 2;

        /** iteration mode * */
        private int m_mode;

        /** iteration index * */
        private int m_index;

        /** number of edges to iterate over * */
        private int m_n;

        /**
         * Constructs a new iterator.
         *
         * @param mode Iteration mode.
         */
        public RelatedIterator(int mode) {
            m_mode = mode;
            m_index = 0;

            switch (m_mode) {
                case IN:
                    m_n = m_in.getInDegree();
                    break;

                case OUT:
                    m_n = m_out.getOutDegree();
                    break;

                default:
                    m_n = 0;
            }
        }

        /** Unsupported Operation. */
        @Override
        public void remove() {
            throw new UnsupportedOperationException(getClass().getName() + "#remove()");
        }

        /**
         * Determines if there are any more related edges to return.
         *
         * @see Iterator#hasNext()
         */
        @Override
        public boolean hasNext() {
            return (m_index < m_n);
        }

        /**
         * Returns the next related edge.
         *
         * @see Iterator#next()
         */
        @Override
        public Graphable next() {
            switch (m_mode) {
                case IN:
                    return (m_in.getInEdgeArray()[m_index++]);
                case OUT:
                    return (m_out.getOutEdgeArray()[m_index++]);
            }

            return (null);
        }
    }
}
