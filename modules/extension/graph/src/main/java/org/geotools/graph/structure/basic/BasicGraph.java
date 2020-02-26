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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;

/**
 * Basic implemenation of Graph.
 *
 * @see Graph
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 */
public class BasicGraph implements Graph, Serializable {

    /** Nodes belonging to the graph */
    private transient Collection<Node> m_nodes;

    /** Edges belonging to the graph */
    private transient Collection<Edge> m_edges;

    /**
     * Constructs an empty graph with edge and node collections uninitialized. Mainly for
     * serializability purposes.
     */
    public BasicGraph() {}

    /**
     * Constructs a graph from a collection of nodes and a collection of edges. The relationships
     * between the nodes (edges) are already assumed to be formed. Only the references to the node
     * and edge collections are copied, not the underlying collections themselves.
     *
     * @param nodes Collection of nodes to be contained by the graph.
     * @param edges Collection of edges to be contained by the graph.
     */
    public BasicGraph(Collection<Node> nodes, Collection<Edge> edges) {
        m_nodes = nodes;
        m_edges = edges;
    }

    /**
     * Sets the node collection of the graph.
     *
     * @param nodes Collection of Node objects.
     */
    public void setNodes(Collection<Node> nodes) {
        m_nodes = nodes;
    }

    /** @see Graph#getNodes() */
    @Override
    public Collection<Node> getNodes() {
        return (m_nodes);
    }

    /**
     * Sets the edge collection for the graph.
     *
     * @param edges Collection of Edge objects.
     */
    public void setEdges(Collection<Edge> edges) {
        m_edges = edges;
    }

    /** @see Graph#getEdges() */
    @Override
    public Collection<Edge> getEdges() {
        return (m_edges);
    }

    /** @see Graph#queryNodes(GraphVisitor) */
    @Override
    public List<Node> queryNodes(GraphVisitor visitor) {
        return (List<Node>) (query(getNodes(), visitor));
    }

    /** @see Graph#queryEdges(GraphVisitor) */
    @Override
    public List<Edge> queryEdges(GraphVisitor visitor) {
        return (List<Edge>) (query(getEdges(), visitor));
    }

    /** @see Graph#visitNodes(GraphVisitor) */
    @Override
    public void visitNodes(GraphVisitor visitor) {
        visit(m_nodes, visitor);
    }

    /** @see Graph#visitEdges(GraphVisitor) */
    @Override
    public void visitEdges(GraphVisitor visitor) {
        visit(m_edges, visitor);
    }

    /**
     * @see Graph#getNodesOfDegree(int)
     * @see Node#getDegree()
     */
    @Override
    public List<Node> getNodesOfDegree(int n) {
        final int degree = n;

        return (queryNodes(
                new GraphVisitor() {
                    @Override
                    public int visit(Graphable component) {
                        if (((Node) component).getDegree() == degree) return (PASS_AND_CONTINUE);
                        return (FAIL_QUERY);
                    }
                }));
    }

    /** @see Graph#getVisitedNodes(boolean) */
    @SuppressWarnings("unchecked")
    @Override
    public List<Node> getVisitedNodes(boolean visited) {
        return (List<Node>) (getVisited(getNodes(), visited));
    }

    /** @see Graph#getVisitedEdges(boolean) */
    @SuppressWarnings("unchecked")
    @Override
    public List<Edge> getVisitedEdges(boolean visited) {
        return (List<Edge>) (getVisited(getEdges(), visited));
    }

    /**
     * Initializes the nodes in the graph by setting all visited flags to false and all visited
     * counts to zero.
     *
     * @see BasicGraphable#isVisited()
     * @see BasicGraphable#getCount()
     */
    public void initNodes() {
        for (Iterator<?> itr = m_nodes.iterator(); itr.hasNext(); ) {
            Node node = (Node) itr.next();
            node.setVisited(false);
            node.setCount(0);
        }
    }

    /**
     * Initializes the edges in the graph by setting all visited flags to false and all visited
     * counts to zero.
     *
     * @see BasicGraphable#isVisited()
     * @see BasicGraphable#getCount()
     */
    public void initEdges() {
        for (Iterator<?> itr = m_edges.iterator(); itr.hasNext(); ) {
            Edge edge = (Edge) itr.next();
            edge.setVisited(false);
            edge.setCount(0);
        }
    }

    /**
     * Returns the string representation of the graph which is just the string representation of the
     * edge and node collections.
     *
     * @return String represtentaton of graph.
     */
    @Override
    public String toString() {
        return ("V=" + m_nodes.toString() + "\n" + "E=" + m_edges.toString());
    }
    /*
     * Internal query method.
     */
    private List<? extends Graphable> query(
            Collection<? extends Graphable> components, GraphVisitor visitor) {
        ArrayList<Graphable> result = new ArrayList<>();

        for (Iterator<? extends Graphable> itr = components.iterator(); itr.hasNext(); ) {
            Graphable component = itr.next();

            switch (visitor.visit(component)) {
                case PASS_AND_CONTINUE:
                    result.add(component);
                    continue;

                case PASS_AND_STOP:
                    result.add(component);
                    return (result);

                case FAIL_QUERY:
                    continue;
            }
        }

        return (result);
    }

    /*
     * Internal visit method
     */
    private void visit(Collection<?> components, GraphVisitor visitor) {
        for (Iterator<?> itr = components.iterator(); itr.hasNext(); ) {
            visitor.visit((Graphable) itr.next());
        }
    }

    /*
     * Internal getVisited method.
     */
    private List<? extends Graphable> getVisited(
            Collection<? extends Graphable> components, boolean visited) {
        final boolean isVisited = visited;
        return (query(
                components,
                new GraphVisitor() {
                    @Override
                    public int visit(Graphable component) {
                        if (component.isVisited() == isVisited) return (PASS_AND_CONTINUE);
                        return (FAIL_QUERY);
                    }
                }));
    }
}
