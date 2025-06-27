/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.graph.util.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.geotools.graph.structure.basic.BasicGraph;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.GraphWalker;
import org.geotools.graph.traverse.basic.BasicGraphTraversal;
import org.geotools.graph.traverse.standard.DepthFirstIterator;

/**
 * Creates a collection of connected graphs from a single graph. A connected graph in which for every two pair of nodes,
 * there is a path between them.
 *
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 */
// this is mixing Graph and List<Serializable> in the same containers, refuse to untangle it
@SuppressWarnings("unchecked")
public class GraphPartitioner implements GraphWalker {

    /** graph to be partitioned into connected components * */
    private Graph m_graph;

    /** Partitions of graph * */
    private ArrayList<Serializable> m_partitions;

    /** current partition * */
    private ArrayList<Serializable> m_partition;

    /** visited counter * */
    private int m_nvisited;

    /**
     * Constructs a new partitioner for a graph.
     *
     * @param graph Graph to be partitioned.
     */
    public GraphPartitioner(Graph graph) {
        m_graph = graph;
        m_partitions = new ArrayList<>();
    }

    /**
     * Performs the partition.
     *
     * @return True if the partition was successful, otherwise false.
     */
    public boolean partition() {
        // strategy is to perform a depth first search from a node, every node is
        // reaches is connected therefore in the same partition
        // when traversal ends, start from a new source, repeat until no more
        // sources

        try {
            m_nvisited = m_graph.getNodes().size();

            DepthFirstIterator iterator = new DepthFirstIterator();
            BasicGraphTraversal traversal = new BasicGraphTraversal(m_graph, this, iterator);

            Iterator<?> sources = m_graph.getNodes().iterator();

            traversal.init();
            m_partition = new ArrayList<>();

            while (m_nvisited > 0) {

                // find a node that hasn't been visited and set as source of traversal
                Node source = null;
                while (sources.hasNext()) {
                    source = (Node) sources.next();
                    if (!source.isVisited()) break;
                }

                // if we could not find a source, return false
                if (source == null || source.isVisited()) return false;

                iterator.setSource(source);
                traversal.traverse();
            }

            // create the individual graphs
            HashSet<Node> nodes = null;
            HashSet<Edge> edges = null;
            ArrayList<Serializable> graphs = new ArrayList<>();

            for (Serializable mPartition : m_partitions) {
                m_partition = (ArrayList<Serializable>) mPartition;
                if (m_partition.isEmpty()) continue;

                nodes = new HashSet<>();
                edges = new HashSet<>();
                for (Serializable serializable : m_partition) {
                    Node node = (Node) serializable;
                    nodes.add(node);
                    edges.addAll(node.getEdges());
                }

                graphs.add(new BasicGraph(nodes, edges));
            }

            m_partitions = graphs;

            return true;
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
        return false;
    }

    /**
     * Returns the partitions of the graph.
     *
     * @return A collection of Graph objects.
     * @see Graph
     */
    public ArrayList<Serializable> getPartitions() {
        return m_partitions;
    }

    /**
     * Adds the element to the current partition.
     *
     * @see GraphWalker#visit(Graphable, GraphTraversal)
     */
    @Override
    public int visit(Graphable element, GraphTraversal traversal) {
        // add element to current set
        m_nvisited--;
        m_partition.add((Serializable) element);
        return GraphTraversal.CONTINUE;
    }

    /**
     * Adds the current partition to the completed set of partitions and creates a new partition.
     *
     * @see GraphWalker#finish()
     */
    @Override
    public void finish() {
        // create a new set
        m_partitions.add(m_partition);
        m_partition = new ArrayList<>();
    }
}
