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
package org.geotools.graph.build.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Node;
import org.geotools.graph.structure.basic.BasicEdge;
import org.geotools.graph.structure.basic.BasicGraph;
import org.geotools.graph.structure.basic.BasicNode;

/**
 * Basic implementation of GraphBuilder. This implementation of builder creates the graph when the builder is created.
 * The underlying graph implementation makes copies of the references to the node and edge collections, not copies of
 * the underlying collections themselves. In this way as nodes and edges are added to the builder, it is reflected in
 * the built graph.
 *
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 */
public class BasicGraphBuilder implements GraphBuilder {

    /** graph being built * */
    private Graph m_graph;

    /** nodes of graph being built * */
    private Set<Node> m_nodes;

    /** edges of graph being built * */
    private Set<Edge> m_edges;

    /** Constructs a new empty graph builder. */
    public BasicGraphBuilder() {
        m_nodes = new HashSet<>();
        m_edges = new HashSet<>();
        m_graph = buildGraph();
    }

    /** @see GraphBuilder#buildNode() */
    @Override
    public Node buildNode() {
        return new BasicNode();
    }

    /** @see GraphBuilder#buildEdge(Node, Node) */
    @Override
    public Edge buildEdge(Node nodeA, Node nodeB) {
        return new BasicEdge(nodeA, nodeB);
    }

    /** @see GraphBuilder#addNode(Node) */
    @Override
    public void addNode(Node node) {
        m_nodes.add(node);
    }

    /**
     * Checks for loops in which case it only added the edge to the adjacency list of one of the nodes (both of its
     * nodes are the same node).
     *
     * @see GraphBuilder#addEdge(Edge)
     */
    @Override
    public void addEdge(Edge edge) {
        edge.getNodeA().add(edge);

        // if the edge is a loop edge, which is legal, only add to node once
        if (!edge.getNodeA().equals(edge.getNodeB())) edge.getNodeB().add(edge);
        m_edges.add(edge);
    }

    /** @see GraphBuilder#removeNode(Node) */
    @Override
    public void removeNode(Node node) {
        // prevents concurrent modification
        ArrayList toRemove = new ArrayList<>(node.getEdges());
        removeEdges(toRemove);
        m_nodes.remove(node);
    }

    /** @see GraphBuilder#removeNodes(Collection) */
    @Override
    public void removeNodes(Collection nodes) {
        for (Object node : nodes) {
            Node n = (Node) node;
            removeNode(n);
        }
    }

    /** @see GraphBuilder#removeEdge(Edge) */
    @Override
    public void removeEdge(Edge edge) {
        edge.getNodeA().remove(edge);
        edge.getNodeB().remove(edge);
        m_edges.remove(edge);
    }

    /** @see GraphBuilder#removeEdges(Collection) */
    @Override
    public void removeEdges(Collection edges) {
        for (Object edge : edges) {
            Edge e = (Edge) edge;
            removeEdge(e);
        }
    }

    /** @see GraphBuilder#getGraph() */
    @Override
    public Graph getGraph() {
        return m_graph;
    }

    /** @see GraphBuilder#clone(boolean) */
    @Override
    public Object clone(boolean deep) throws Exception {
        GraphBuilder builder = getClass().getDeclaredConstructor().newInstance();
        if (deep) builder.importGraph(getGraph());

        return builder;
    }

    /** @see GraphBuilder#importGraph(Graph) */
    @Override
    public void importGraph(Graph g) {
        m_nodes = new HashSet<>(g.getNodes());
        m_edges = new HashSet<>(g.getEdges());
        m_graph = buildGraph();
    }

    /**
     * Returns the nodes belonging to the graph being built.
     *
     * @return A collection of nodes.
     */
    public Collection<Node> getNodes() {
        return m_nodes;
    }

    /**
     * Returns the edges belonging to the graph being built.
     *
     * @return A collection of edges.
     */
    public Collection<Edge> getEdges() {
        return m_edges;
    }

    /**
     * Creates the underlying graph object.
     *
     * @return A Graph object.
     */
    protected Graph buildGraph() {
        return new BasicGraph(m_nodes, m_edges);
    }
}
