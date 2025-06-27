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
package org.geotools.graph.structure.basic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.geotools.graph.structure.DirectedEdge;
import org.geotools.graph.structure.DirectedNode;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Node;

/**
 * Basic implementation of DirectedNode.
 *
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 */
public class BasicDirectedNode extends BasicGraphable implements DirectedNode {

    /** In adjacency list * */
    private transient ArrayList<DirectedEdge> m_in;

    /** Out adjacecy list * */
    private transient ArrayList<DirectedEdge> m_out;

    /** Constructs a new BasicDirectedNode. */
    public BasicDirectedNode() {
        super();
        m_in = new ArrayList<>();
        m_out = new ArrayList<>();
    }

    /**
     * Unsupported operation. Directed nodes classify edges as <B>in</B> and <B>out</B>. addIn(Edge) and addOut(Edge)
     * should be used instead of this method.
     *
     * @see DirectedNode#addIn(DirectedEdge)
     * @see DirectedNode#addOut(DirectedEdge)
     */
    @Override
    public void add(Edge e) {
        throw new UnsupportedOperationException("add(Edge)");
    }

    /**
     * Adds an edge to the <B>in</B> adjacency list of the node which is an underlying List implementation. No checking
     * is done on the edge (duplication, looping...), it is simply added to the list. It is also assumed that the edge
     * being added has the node as its out node.
     *
     * @see DirectedNode#addIn(DirectedEdge)
     * @see DirectedEdge#getOutNode()
     */
    @Override
    public void addIn(DirectedEdge e) {
        m_in.add(e);
    }

    /**
     * Adds an edge to the <B>ou</B> adjacency list of the node which is an underlying List implementation. No checking
     * is done on the edge (duplication, looping...), it is simply added to the list. It is also assumed that the edge
     * being added has the node as its in node.
     *
     * @see DirectedNode#addOut(DirectedEdge)
     * @see DirectedEdge#getInNode()
     */
    @Override
    public void addOut(DirectedEdge e) {
        m_out.add(e);
    }

    /**
     * Removes the edge from both the in and out adjacency lists.
     *
     * @see Node#remove(Edge)
     */
    @Override
    public void remove(Edge e) {
        m_in.remove(e);
        m_out.remove(e);
    }

    /** @see DirectedNode#removeIn(DirectedEdge) */
    @Override
    public void removeIn(DirectedEdge e) {
        m_in.remove(e);
    }

    /** @see DirectedNode#removeOut(DirectedEdge) */
    @Override
    public void removeOut(DirectedEdge e) {
        m_out.remove(e);
    }

    /**
     * First searches for an in edge with an out node == this, and in node == other. If none is found an edge with out
     * node == other, and in node == this is searched for.
     *
     * @see Node#remove(Edge)
     */
    @Override
    public Edge getEdge(Node other) {
        Edge e = getInEdge((DirectedNode) other);
        if (e != null) return e;
        return getOutEdge((DirectedNode) other);
    }

    /** @see DirectedNode#getInEdge(DirectedNode) */
    @Override
    public Edge getInEdge(DirectedNode other) {
        // must explicitly check that the edge has node other, and one node this,
        // just checking other is not good enough because of loops
        for (DirectedEdge edge : m_in) {
            if (edge.getInNode().equals(other) && edge.getOutNode().equals(this)) return edge;
        }
        return null;
    }

    /** @see DirectedNode#getOutEdge(DirectedNode) */
    @Override
    public Edge getOutEdge(DirectedNode other) {
        // must explicitly check that the edge has node other, and one node this,
        // just checking other is not good enough because of loops
        for (DirectedEdge edge : m_out) {
            if (edge.getOutNode().equals(other) && edge.getInNode().equals(this)) return edge;
        }
        return null;
    }

    /** Returns the combination of both the in and out adjacency lists. */
    @Override
    public List<DirectedEdge> getEdges() {
        ArrayList<DirectedEdge> edges = new ArrayList<>();
        edges.addAll(m_in);
        edges.addAll(m_out);
        return edges;
    }

    /** @see DirectedNode#getInEdges() */
    @Override
    public List<DirectedEdge> getInEdges() {
        return m_in;
    }

    /** @see DirectedNode#getOutEdges() */
    @Override
    public List<DirectedEdge> getOutEdges() {
        return m_out;
    }

    /**
     * A combination of the results of getInEdges(Node) and getOutEdges(Node).
     *
     * @see Node#getEdges(Node)
     * @see DirectedNode#getInEdges(DirectedNode)
     * @see DirectedNode#getOutEdges(DirectedNode)
     */
    @Override
    public List<DirectedEdge> getEdges(Node other) {
        List<DirectedEdge> edges = getInEdges((DirectedNode) other);
        edges.addAll(getOutEdges((DirectedNode) other));
        return edges;
    }

    /** @see DirectedNode#getInEdges(DirectedNode) */
    @Override
    public List<DirectedEdge> getInEdges(DirectedNode other) {
        ArrayList<DirectedEdge> edges = new ArrayList<>();
        for (DirectedEdge edge : m_in) {
            if (edge.getInNode().equals(other)) edges.add(edge);
        }
        return edges;
    }

    /** @see DirectedNode#getOutEdges(DirectedNode) */
    @Override
    public List<DirectedEdge> getOutEdges(DirectedNode other) {
        ArrayList<DirectedEdge> edges = new ArrayList<>();
        for (DirectedEdge edge : m_out) {
            if (edge.getOutNode().equals(other)) edges.add(edge);
        }
        return edges;
    }

    /**
     * Returns sum of sizes of in and out adjacency lists.
     *
     * @see Node#getDegree()
     */
    @Override
    public int getDegree() {
        return m_in.size() + m_out.size();
    }

    /** @see DirectedNode#getInDegree() */
    @Override
    public int getInDegree() {
        return m_in.size();
    }

    /** @see DirectedNode#getOutDegree() */
    @Override
    public int getOutDegree() {
        return m_out.size();
    }

    /**
     * Returns an iterator over all out nodes of out edges and in nodes of in edges.
     *
     * @see org.geotools.graph.structure.Graphable#getRelated()
     */
    @Override
    public Iterator<DirectedNode> getRelated() {
        ArrayList<DirectedNode> related = new ArrayList<>(m_out.size() + m_in.size());
        for (DirectedEdge e : m_in) {
            related.add(e.getInNode());
        }

        for (DirectedEdge e : m_out) {
            related.add(e.getOutNode());
        }
        return related.iterator();
    }

    /**
     * Returns all in nodes of in edges.
     *
     * @see org.geotools.graph.structure.DirectedGraphable#getInRelated()
     */
    @Override
    public Iterator<DirectedNode> getInRelated() {
        ArrayList<DirectedNode> related = new ArrayList<>(m_in.size());
        for (DirectedEdge e : m_in) {
            related.add(e.getInNode());
        }

        return related.iterator();
    }

    /**
     * Returns all out nodes of out edges.
     *
     * @see org.geotools.graph.structure.DirectedGraphable#getOutRelated()
     */
    @Override
    public Iterator<DirectedNode> getOutRelated() {
        ArrayList<DirectedNode> related = new ArrayList<>(m_out.size());
        for (DirectedEdge e : m_out) {
            related.add(e.getOutNode());
        }
        return related.iterator();
    }

    /**
     * Overides the default deserialization operation. The edge adjacency lists of a BasicDirectedNode is not written
     * out when the node is serialized so they must be recreated upon deserialization.
     *
     * @param in Object input stream containing serialized object.
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {

        in.defaultReadObject();

        // recreate edge adjacency lists
        m_in = new ArrayList<>();
        m_out = new ArrayList<>();
    }
}
