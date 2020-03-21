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
import org.geotools.graph.structure.DirectedEdge;
import org.geotools.graph.structure.DirectedNode;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Node;

/**
 * Optimized implementation of DirectedNode. The following optimizations reduce space and increase
 * performance. <br>
 *
 * <UL>
 *   <LI>In and Out edge adjacency list stored as arrays of exact size.
 *   <LI>Support from removing edges is removed
 *   <LI>The related component iterators iterates over the underlying edge arrays of the node
 *       instread of newly created collections.
 * </UL>
 *
 * Using an optimized directed node requires that the size of the in and out edge adjacency lists be
 * known before its creation.
 *
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 * @see DirectedNode
 */
public class OptDirectedNode extends OptGraphable implements DirectedNode {

    /** in edge adjacency list * */
    private transient DirectedEdge[] m_in;

    /** out edge adjacency list * */
    private transient DirectedEdge[] m_out;

    /**
     * Constructs a new OptDirectedNode. This constructor does not create the edge adjacency arrays
     * for the node.
     */
    public OptDirectedNode() {
        this(0, 0);
    }

    /**
     * Constructs a new OptDirectedNode.
     *
     * @param indegree Number of in adjacenct edges to the node.
     * @param outdegree Number of out adjacent edges to the node.
     */
    public OptDirectedNode(int indegree, int outdegree) {
        super();
        m_in = new DirectedEdge[indegree];
        m_out = new DirectedEdge[outdegree];
    }

    /** Not supported. */
    @Override
    public void add(Edge e) {
        throw new UnsupportedOperationException(getClass().getName() + "#add(Edge)");
    }

    /** @see DirectedNode#addIn(DirectedEdge) */
    @Override
    public void addIn(DirectedEdge e) {
        for (int i = 0; i < m_in.length; i++) {
            if (m_in[i] == null) {
                m_in[i] = e;
                return;
            }
        }
    }

    /** @see DirectedNode#addOut(DirectedEdge) */
    @Override
    public void addOut(DirectedEdge e) {
        for (int i = 0; i < m_out.length; i++) {
            if (m_out[i] == null) {
                m_out[i] = e;
                return;
            }
        }
    }

    /** Unsupported Operation. */
    @Override
    public void remove(Edge e) {
        throw new UnsupportedOperationException(getClass().getName() + "#remove(Edge)");
    }

    /** Unsupported Operation. */
    @Override
    public void removeIn(DirectedEdge e) {
        throw new UnsupportedOperationException(getClass().getName() + "#removeIn(DirectedEdge)");
    }

    /** Unsupported Operation. */
    @Override
    public void removeOut(DirectedEdge e) {
        throw new UnsupportedOperationException(getClass().getName() + "#removeOut(DirectedEdge)");
    }

    /** @see Node#getEdge(Node) */
    @Override
    public Edge getEdge(Node other) {
        Edge e = getInEdge((DirectedNode) other);
        if (e == null) e = getOutEdge((DirectedNode) other);
        return (e);
    }

    /** @see DirectedNode#getInEdge(DirectedNode) */
    @Override
    public Edge getInEdge(DirectedNode other) {
        for (int i = 0; i < m_in.length; i++) {
            if (m_in[i].getInNode().equals(other)) return (m_in[i]);
        }
        return (null);
    }

    /** @see DirectedNode#getOutEdge(DirectedNode) */
    @Override
    public Edge getOutEdge(DirectedNode other) {
        for (int i = 0; i < m_out.length; i++) {
            if (m_out[i].getOutNode().equals(other)) return (m_out[i]);
        }

        return (null);
    }

    /** @see Node#getEdges(Node) */
    @Override
    public List<Edge> getEdges(Node other) {
        List<Edge> l = getInEdges((DirectedNode) other);
        l.addAll(getOutEdges((DirectedNode) other));

        return (l);
    }

    /** @see DirectedNode#getInEdges(DirectedNode) */
    @Override
    public List<Edge> getInEdges(DirectedNode other) {
        ArrayList<Edge> edges = new ArrayList<>();

        for (int i = 0; i < m_in.length; i++) {
            if (m_in[i].getInNode().equals(other)) edges.add(m_in[i]);
        }

        return (edges);
    }

    /** @see DirectedNode#getOutEdges(DirectedNode) */
    @Override
    public List<Edge> getOutEdges(DirectedNode other) {
        ArrayList<Edge> edges = new ArrayList<>();

        for (int i = 0; i < m_out.length; i++) {
            if (m_out[i].getOutNode().equals(other)) edges.add(m_out[i]);
        }

        return (edges);
    }

    /** @see Node#getEdges() */
    @Override
    public List<DirectedEdge> getEdges() {
        ArrayList<DirectedEdge> edges = new ArrayList<>();
        for (int i = 0; i < m_in.length; i++) edges.add(m_in[i]);
        for (int i = 0; i < m_out.length; i++) edges.add(m_out[i]);

        return (edges);
    }

    /**
     * Returns the in adjacency edge array of the node.
     *
     * @return An array of in edges for the node.
     */
    public DirectedEdge[] getInEdgeArray() {
        return (m_in);
    }

    /** @see DirectedNode#getInEdges() */
    @Override
    public List<DirectedEdge> getInEdges() {
        ArrayList<DirectedEdge> edges = new ArrayList<>();

        for (int i = 0; i < m_in.length; i++) {
            edges.add(m_in[i]);
        }
        return (edges);
    }

    /**
     * Returns the out adjacency edge array of the node.
     *
     * @return An array of out edges for the node.
     */
    public DirectedEdge[] getOutEdgeArray() {
        return (m_out);
    }

    /** @see DirectedNode#getOutEdges() */
    @Override
    public List<DirectedEdge> getOutEdges() {
        ArrayList<DirectedEdge> edges = new ArrayList<>();

        for (int i = 0; i < m_out.length; i++) {
            edges.add(m_out[i]);
        }
        return (edges);
    }

    /** @see Node#getDegree() */
    @Override
    public int getDegree() {
        return (m_in.length + m_out.length);
    }

    /**
     * Sets the in degree of the node. This method builds the in edge adjacency list of the node.
     *
     * @param indegree The in degree / size of in edge array of the node.
     */
    public void setInDegree(int indegree) {
        m_in = new DirectedEdge[indegree];
    }

    /** @see DirectedNode#getInDegree() */
    @Override
    public int getInDegree() {
        return (m_in.length);
    }

    /**
     * Sets the out degree of the node. This method builds the out edge adjacency list of the node.
     *
     * @param outdegree The out degree / size of out edge array of the node.
     */
    public void setOutDegree(int outdegree) {
        m_out = new DirectedEdge[outdegree];
    }

    /** @see DirectedNode#getOutDegree() */
    @Override
    public int getOutDegree() {
        return (m_out.length);
    }

    /**
     * This iterator iterates over the underlying edge arrays of the node.
     *
     * @see org.geotools.graph.structure.Graphable#getRelated()
     */
    @Override
    public Iterator<Node> getRelated() {
        return (new RelatedIterator(RelatedIterator.BOTH));
    }

    /**
     * This iterator iterates over the underlying in edge array of the node.
     *
     * @see org.geotools.graph.structure.DirectedGraphable#getInRelated()
     */
    @Override
    public Iterator<Node> getInRelated() {
        return (new RelatedIterator(RelatedIterator.IN));
    }

    /** This iterator iterates over the underlying out edge array of the node. */
    @Override
    public Iterator<Node> getOutRelated() {
        return (new RelatedIterator(RelatedIterator.OUT));
    }

    /**
     * Overrides the default deserialization operation. Since edge adjacency lists of Nodes are not
     * written out upon serialization, they must be recreated upon deserialization.
     *
     * @param in Object input stream containing serialized objects.
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {

        in.defaultReadObject();

        // read the degree of the node
        setInDegree(in.readInt());
        setOutDegree(in.readInt());
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

        // write the degree of the node to the output stream
        out.writeInt(getInDegree());
        out.writeInt(getOutDegree());
    }

    /**
     * Iterator used to iterate over related nodes.
     *
     * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
     */
    public class RelatedIterator implements Iterator<Node> {

        /** in iteration mode * */
        public static final int IN = 0;

        /** out iteration mode * */
        public static final int OUT = 1;

        /** both iteration mode * */
        public static final int BOTH = 2;

        /** iteration mode * */
        private int m_mode;

        /** iteration index * */
        private int m_index;

        /**
         * Constructs a new iterator.
         *
         * @param mode Iteration mode.
         */
        public RelatedIterator(int mode) {
            m_mode = mode;
            m_index = 0;
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
            switch (m_mode) {
                case IN:
                    return (m_index < m_in.length);

                case OUT:
                    return (m_index < m_out.length);

                case BOTH:
                    return (m_index < m_in.length + m_out.length);
            }
            return (false);
        }

        /** Returns the next related node. */
        @Override
        public Node next() {

            switch (m_mode) {
                case IN:
                    return (m_in[m_index++].getInNode());

                case OUT:
                    return (m_out[m_index++].getOutNode());

                case BOTH:
                    return (m_index < m_in.length
                            ? m_in[m_index++].getInNode()
                            : m_out[m_index++ - m_in.length].getOutNode());
            }
            return (null);
        }
    }
}
