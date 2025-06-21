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
package org.geotools.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.GraphGenerator;
import org.geotools.graph.build.opt.OptDirectedGraphBuilder;
import org.geotools.graph.build.opt.OptGraphBuilder;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.geotools.graph.structure.opt.OptDirectedNode;
import org.geotools.graph.structure.opt.OptNode;

public class GraphTestUtil {

    /**
     * Builds a graph with no bifurcations made up of a specified number of nodes. Nodes are numbered from 0 to (# of
     * nodes - 1).<br>
     * <br>
     * O----O----O--...--O----O----O
     *
     * @param builder Builder to use to construct graph.
     * @param nnodes Number of nodes in graph.
     * @return 2 element object array containing references to the end points of the graph. (The nodes of degree 1 at
     *     the end of the graph.
     */
    public static Node[] buildNoBifurcations(GraphBuilder builder, int nnodes) {
        Node n1 = builder.buildNode();
        builder.addNode(n1);
        n1.setID(0);

        Node n2 = null;
        Edge e = null;
        Node first = n1;

        for (int i = 1; i < nnodes; i++) {
            n2 = builder.buildNode();
            builder.addNode(n2);
            n2.setID(i);

            e = builder.buildEdge(n1, n2);
            builder.addEdge(e);
            e.setID(i - 1);

            n1 = n2;
        }

        return new Node[] {first, n1};
    }

    public static Node[] buildNoBifurcations(GraphGenerator gen, int nnodes) {
        String[] nodes = new String[nnodes];
        Node[] ends = new Node[2];

        for (int i = 0; i < nnodes; i++) {
            nodes[i] = String.valueOf(i);
            if (i > 0) {
                Edge e = (Edge) gen.add(new String[] {nodes[i - 1], nodes[i]});
                if (i == 1) ends[0] = e.getNodeA();
                if (i == nnodes - 1) ends[1] = e.getNodeB();
            }
        }

        return ends;
    }

    public static Object[] buildNoBifurcations(OptGraphBuilder builder, int nnodes) {
        // use maps for id since optimized graphable doesn't use id's
        Map<Node, Integer> node2id = new HashMap<>();
        Map<Edge, Integer> edge2id = new HashMap<>();

        OptNode n1 = (OptNode) builder.buildNode();
        n1.setDegree(1);

        builder.addNode(n1);
        node2id.put(n1, Integer.valueOf(0));

        OptNode n2 = null;
        Edge e = null;
        OptNode first = n1;

        for (int i = 1; i < nnodes; i++) {
            n2 = (OptNode) builder.buildNode();
            if (i < nnodes - 1) n2.setDegree(2);
            else n2.setDegree(1);

            builder.addNode(n2);
            node2id.put(n2, Integer.valueOf(i));

            e = builder.buildEdge(n1, n2);
            builder.addEdge(e);
            edge2id.put(e, Integer.valueOf(i - 1));

            n1 = n2;
        }

        return new Object[] {first, n1, node2id, edge2id};
    }

    public static Object[] buildNoBifurcations(OptDirectedGraphBuilder builder, int nnodes) {
        // use maps for id since optimized graphable doesn't use id's
        Map<Node, Integer> node2id = new HashMap<>();
        Map<Edge, Integer> edge2id = new HashMap<>();

        OptDirectedNode n1 = (OptDirectedNode) builder.buildNode();
        n1.setInDegree(0);
        n1.setOutDegree(1);

        builder.addNode(n1);
        node2id.put(n1, Integer.valueOf(0));

        OptDirectedNode n2 = null;
        Edge e = null;
        OptDirectedNode first = n1;

        for (int i = 1; i < nnodes; i++) {
            n2 = (OptDirectedNode) builder.buildNode();
            if (i < nnodes - 1) {
                n2.setInDegree(1);
                n2.setOutDegree(1);
            } else {
                n2.setInDegree(1);
                n2.setOutDegree(0);
            }

            builder.addNode(n2);
            node2id.put(n2, Integer.valueOf(i));

            e = builder.buildEdge(n1, n2);
            builder.addEdge(e);
            edge2id.put(e, Integer.valueOf(i - 1));

            n1 = n2;
        }

        return new Object[] {first, n1, node2id, edge2id};
    }

    public static Node[] buildSingleBifurcation(final GraphBuilder builder, int nnodes, final int bifurcation) {
        Node[] ends = buildNoBifurcations(builder, nnodes - 1);
        final Node n = builder.buildNode();
        final List<Graphable> bif = new ArrayList<>();

        builder.getGraph().visitNodes(component -> {
            if (component.getID() == bifurcation) {
                bif.add(component);
            }

            return 0;
        });

        Edge e = builder.buildEdge(n, (Node) bif.get(0));
        builder.addNode(n);
        builder.addEdge(e);

        Node[] bifends = {ends[0], ends[1], (Node) bif.get(0)};
        return bifends;
    }

    /**
     * Creates a balanced binary tree consisting of a specefied number of levels. Each node created contains a string
     * representing the nodes location in the tree.<br>
     * <br>
     * locstring(root) = "0"; locstring(node) = locstring(parent) + ".0" (if left child); locstring(node) =
     * locstring(parent) + ".1" (if right child);
     *
     * @param builder Builder to construct graph with
     * @param levels Number of levels in the tree.
     * @return A 2 element object array. 0 = root node 1 = map of locstring to node
     */
    public static Object[] buildPerfectBinaryTree(GraphBuilder builder, int levels) {
        HashMap<Object, Node> id2node = new HashMap<>();

        // a balanced binary tree
        Node root = builder.buildNode();
        root.setObject("0");
        id2node.put(root.getObject(), root);

        builder.addNode(root);

        Queue<Graphable> queue = new ArrayDeque<>((int) Math.pow(2, levels + 1));
        queue.add(root);

        // build a complete binary tree
        // number of nodes = 2^(n+1) - 1
        int level = 0;
        while (level < levels) {
            int nnodes = (int) Math.pow(2, level);
            for (int i = 0; i < nnodes; i++) {
                Node n = (Node) queue.remove();

                Node ln = builder.buildNode();
                ln.setObject(n.getObject() + ".0");
                id2node.put(ln.getObject(), ln);

                Node rn = builder.buildNode();
                rn.setObject(n.getObject() + ".1");
                id2node.put(rn.getObject(), rn);

                Edge le = builder.buildEdge(n, ln);
                Edge re = builder.buildEdge(n, rn);

                builder.addNode(ln);
                builder.addNode(rn);
                builder.addEdge(le);
                builder.addEdge(re);

                queue.add(ln);
                queue.add(rn);
            }
            level++;
        }

        return new Object[] {root, id2node};
    }

    public static Object[] buildPerfectBinaryTree(OptGraphBuilder builder, int levels) {
        OptNode root = (OptNode) builder.buildNode();
        root.setDegree(2);
        builder.addNode(root);

        Queue<Graphable> queue = new ArrayDeque<>((int) Math.pow(2, levels + 1));
        queue.add(root);

        // build a complete binary tree
        // number of nodes = 2^(n+1) - 1
        int level = 0;
        while (level < levels) {
            int nnodes = (int) Math.pow(2, level);
            for (int i = 0; i < nnodes; i++) {
                Node n = (Node) queue.remove();

                OptNode ln = (OptNode) builder.buildNode();
                if (level < levels - 1) ln.setDegree(3);
                else ln.setDegree(1);

                OptNode rn = (OptNode) builder.buildNode();
                if (level < levels - 1) rn.setDegree(3);
                else rn.setDegree(1);

                Edge le = builder.buildEdge(n, ln);
                Edge re = builder.buildEdge(n, rn);

                builder.addNode(ln);
                builder.addNode(rn);
                builder.addEdge(le);
                builder.addEdge(re);

                queue.add(ln);
                queue.add(rn);
            }
            level++;
        }

        return new Object[] {root};
    }

    public static Object[] buildPerfectBinaryTree(OptDirectedGraphBuilder builder, int levels) {
        OptDirectedNode root = (OptDirectedNode) builder.buildNode();
        root.setInDegree(0);
        root.setOutDegree(2);
        builder.addNode(root);

        Queue<Graphable> queue = new ArrayDeque<>((int) Math.pow(2, levels + 1));
        queue.add(root);

        // build a complete binary tree
        // number of nodes = 2^(n+1) - 1
        int level = 0;
        while (level < levels) {
            int nnodes = (int) Math.pow(2, level);
            for (int i = 0; i < nnodes; i++) {
                Node n = (Node) queue.remove();

                OptDirectedNode ln = (OptDirectedNode) builder.buildNode();
                if (level < levels - 1) {
                    ln.setInDegree(1);
                    ln.setOutDegree(2);
                } else {
                    ln.setInDegree(1);
                    ln.setOutDegree(0);
                }

                OptDirectedNode rn = (OptDirectedNode) builder.buildNode();
                if (level < levels - 1) {
                    rn.setInDegree(1);
                    rn.setOutDegree(2);
                } else {
                    rn.setInDegree(1);
                    rn.setOutDegree(0);
                }

                Edge le = builder.buildEdge(n, ln);
                Edge re = builder.buildEdge(n, rn);

                builder.addNode(ln);
                builder.addNode(rn);
                builder.addEdge(le);
                builder.addEdge(re);

                queue.add(ln);
                queue.add(rn);
            }
            level++;
        }

        return new Object[] {root};
    }

    public static Node[] buildCircular(GraphBuilder builder, int nnodes) {
        // build a graph with no bifurcations then join the first and last verticies
        Node[] ends = buildNoBifurcations(builder, nnodes);
        Edge e = builder.buildEdge(ends[1], ends[0]);
        builder.addEdge(e);

        return ends;
    }

    public static Node[] buildCircular(GraphGenerator gen, int nnodes) {
        Node[] ends = buildNoBifurcations(gen, nnodes);
        gen.add(new Object[] {"0", String.valueOf(nnodes - 1)});

        return ends;
    }
}
