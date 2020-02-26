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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Node;

/**
 * Optimized implementation of Node. The following optimizations reduce space and improve
 * performance.<br>
 *
 * <UL>
 *   <LI>Edge adjacency list stored as array of predetermined size.
 *   <LI>Removing support for removing edges from the nodes ajdacency list.
 *   <LI>The related component iterator iterates over the underlying edge array of the node instread
 *       of a newly created collection.
 * </UL>
 *
 * Using an optimized node requires that the degree of the node be known before the node is built.
 *
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 * @see Node
 */
public class OptNode extends OptGraphable implements Node {

    /** edge adjacency list * */
    private Edge[] m_edges;

    /**
     * Constructs a new OptimizedNode. This constructor does not build the adjacency array for the
     * node.
     */
    public OptNode() {
        this(0);
    }

    /**
     * Constructs a new Optimized Node with a known degree.
     *
     * @param degree The degree of the node.
     */
    public OptNode(int degree) {
        super();
        m_edges = new Edge[degree];
    }

    /** @see Node#add(Edge) */
    @Override
    public void add(Edge e) {
        for (int i = 0; i < m_edges.length; i++) {
            if (m_edges[i] == null) {
                m_edges[i] = e;
                return;
            }
        }
    }

    /**
     * Not supported.
     *
     * @see Node#remove(Edge)
     */
    @Override
    public void remove(Edge e) {
        throw new UnsupportedOperationException(getClass().getName() + "#remove(Edge)");
    }

    /** @see Node#getEdge(Node) */
    @Override
    public Edge getEdge(Node other) {
        for (int i = 0; i < m_edges.length; i++) {
            if (m_edges[i].getNodeA().equals(this) && m_edges[i].getNodeB().equals(other)
                    || m_edges[i].getNodeB().equals(this) && m_edges[i].getNodeA().equals(other))
                return (m_edges[i]);
        }
        return (null);
    }

    /** @see Node#getEdges(Node) */
    @Override
    public List<Edge> getEdges(Node other) {
        ArrayList<Edge> edges = new ArrayList<>();
        for (int i = 0; i < m_edges.length; i++) {
            if (m_edges[i].getNodeA().equals(this) && m_edges[i].getNodeB().equals(other)
                    || m_edges[i].getNodeB().equals(this) && m_edges[i].getNodeA().equals(other))
                edges.add(m_edges[i]);
        }
        return (edges);
    }

    /**
     * Returns the edge adjacency list of the node as an array.
     *
     * @return An array containing edges adjacent to the node.
     */
    public Edge[] getEdgeArray() {
        return (m_edges);
    }

    /** @see Node#getEdges() */
    @Override
    public List<Edge> getEdges() {
        ArrayList<Edge> edges = new ArrayList<>();

        for (int i = 0; i < m_edges.length; i++) {
            edges.add(m_edges[i]);
        }

        return (edges);
    }

    /**
     * Sets the degree of the node. This method build the edge adjacency array for the node.
     *
     * @param degree The degree of the node / size of edge adjacency array.
     */
    public void setDegree(int degree) {
        m_edges = new Edge[degree];
    }

    /** @see Node#getDegree() */
    @Override
    public int getDegree() {
        return (m_edges.length);
    }

    /**
     * This iterator iterates over the underlying edge array of the node.
     *
     * @see org.geotools.graph.structure.Graphable#getRelated()
     */
    @Override
    public Iterator<Node> getRelated() {
        return (new RelatedIterator(this));
    }

    /**
     * Overrides the default deserialization operation. Since edge adjacency lists of Nodes are not
     * written out upon serialization, they must be recreated upon deserialization.
     *
     * @param in Object input stream containing serialized objects.
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {

        in.defaultReadObject();

        // read degree from object stream and recreate edge list
        setDegree(in.readInt());
    }

    /**
     * Overrides the default serialization operation. Since edge adjacency lists of Nodes are not
     * written out upon serialization, all the information needed to recreate them must be written
     * to the object stream as well. Since the edge list is not written out, and the node does not
     * store its degree explicitly, it must be written to the output stream.
     *
     * @param out Object output stream containing serialized objects.
     */
    private void writeObject(ObjectOutputStream out) throws IOException {

        out.defaultWriteObject();

        // store degree in object stream
        out.writeInt(getDegree());
    }

    /**
     * An iterator used to iterate over related nodes.
     *
     * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
     */
    public class RelatedIterator implements Iterator<Node> {

        /** index of iterator * */
        private byte m_index = 0;

        private OptNode m_node;

        public RelatedIterator(OptNode node) {
            m_node = node;
        }

        /** Not supported. */
        @Override
        public void remove() {
            throw new UnsupportedOperationException(getClass().getName() + "#remove()");
        }

        /**
         * Determines if there are any more related nodes to return.
         *
         * @see Iterator#hasNext()
         */
        @Override
        public boolean hasNext() {
            return (m_index < m_edges.length);
        }

        /**
         * Returns the next related node.
         *
         * @see Iterator#next()
         */
        @Override
        public Node next() {
            Edge e = m_edges[m_index++];
            return (e.getNodeA().equals(m_node) ? e.getNodeB() : e.getNodeA());
        }
    }
}
