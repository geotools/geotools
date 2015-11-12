/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.factory;

import java.util.AbstractSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * A set of <code>Object</code>s with pairwise orderings between them.
 * The <code>iterator</code> method provides the elements in topologically sorted order.
 * Elements participating in a cycle are not returned.
 *
 * Unlike the <code>SortedSet</code> and <code>SortedMap</code> interfaces, which require their 
 * elements to implement the <code>Comparable</code> interface, this class receives ordering
 * information via its <code>setOrdering</code> and <code>unsetPreference</code> methods.
 * This difference is due to the fact that the relevant ordering between elements is unlikely to
 * be inherent in the elements themselves; rather, it is set dynamically according to application
 * policy. For example, in a service provider registry situation, an application might allow the
 * user to set a preference order for service provider objects supplied by a trusted vendor over 
 * those supplied by another.
 * 
 * @since 15.0
 */
class PartiallyOrderedSet<E> extends AbstractSet<E> {

    // The topological sort (roughly) follows the algorithm described in
    // Horowitz and Sahni, _Fundamentals of Data Structures_ (1976), p. 315.

    // Maps Objects to DigraphNodes that contain them
    private final Map<E, DigraphNode> poNodes = new HashMap<>();

    // The set of Objects
    private final Set<E> nodes = poNodes.keySet();

    /**
     * A node in a directed graph. In addition to an arbitrary <code>Object</code> containing
     * user data associated with the node, each node maintains a <code>Set</code>s of nodes
     * which are pointed to by the current node (available from <code>getOutNodes</code>).
     * The in-degree of the node (that is, number of nodes that point to the current node)
     * may be queried.
     */
    private class DigraphNode implements Cloneable {

        /** The data associated with this node. */
        private final E data;

        /** A <code>Set</code> of neighboring nodes pointed to by this node. */
        private final Set<DigraphNode> outNodes = new HashSet<>();

        /** The in-degree of the node. */
        private int inDegree = 0;

        /** A <code>Set</code> of neighboring nodes that point to this node. */
        private final Set<DigraphNode> inNodes = new HashSet<>();

        /**
         * Constructs a new <code>DigraphNode</code>.
         * @param data The data associated with this node
         */
        public DigraphNode(E data) {
            this.data = data;
        }

        /** 
         * Returns the <code>Object</code> referenced by this node.
         */
        public E getData() {
            return data;
        }

        /**
         * Returns an <code>Iterator</code> containing the nodes pointed to by this node.
         */
        public Iterator<DigraphNode> getOutNodes() {
            return outNodes.iterator();
        }

        /**
         * Adds a directed edge to the graph. The outNodes list of this node is updated
         * and the in-degree of the other node is incremented.
         *
         * @param node a <code>DigraphNode</code>.
         *
         * @return <code>true</code> if the node was not previously the target of an edge.
         */
        public boolean addEdge(DigraphNode node) {
            if (outNodes.contains(node)) {
                return false;
            }

            outNodes.add(node);
            node.inNodes.add(this);
            node.incrementInDegree();
            return true;
        }

        /**
         * Returns <code>true</code> if an edge exists between this node and the given node.
         *
         * @param node a <code>DigraphNode</code>.
         *
         * @return <code>true</code> if the node is the target of an edge.
         */
        public boolean hasEdge(DigraphNode node) {
            return outNodes.contains(node);
        }

        /**
         * Removes a directed edge from the graph. The outNodes list of this node is updated
         * and the in-degree of the other node is decremented.
         *
         * @return <code>true</code> if the node was previously the target of an edge.
         */
        public boolean removeEdge(DigraphNode node) {
            if (!outNodes.contains(node)) {
                return false;
            }

            outNodes.remove(node);
            node.inNodes.remove(this);
            node.decrementInDegree();
            return true;
        }

        /**
         * Removes this node from the graph, updating neighboring nodes appropriately.
         */
        public void dispose() {
            Object[] inNodesArray = inNodes.toArray();
            for(int i=0; i<inNodesArray.length; i++) {
                ((DigraphNode) inNodesArray[i]).removeEdge(this);
            }

            Object[] outNodesArray = outNodes.toArray();
            for(int i=0; i<outNodesArray.length; i++) {
                removeEdge(((DigraphNode) outNodesArray[i]));
            }
        }

        /** Returns the in-degree of this node. */
        public int getInDegree() {
            return inDegree;
        }

        /** Increments the in-degree of this node. */
        private void incrementInDegree() {
            ++inDegree;
        }

        /** Decrements the in-degree of this node. */
        private void decrementInDegree() {
            --inDegree;
        }
    }

    private class PartialOrderIterator implements Iterator<E> {

        private final LinkedList<DigraphNode> zeroList = new LinkedList<>();
        private final Map<DigraphNode, Integer> inDegrees = new HashMap<>();

        public PartialOrderIterator(Iterator<DigraphNode> iter) {
            // Initialize scratch in-degree values, zero list
            while (iter.hasNext()) {
                DigraphNode node = iter.next();
                int inDegree = node.getInDegree();
                inDegrees.put(node, new Integer(inDegree));

                // Add nodes with zero in-degree to the zero list
                if (inDegree == 0) {
                    zeroList.add(node);
                }
            }
        }

        @Override
        public boolean hasNext() {
            return !zeroList.isEmpty();
        }

        @Override
        public E next() {
            DigraphNode first = zeroList.removeFirst();

            // For each out node of the output node, decrement its in-degree
            Iterator<DigraphNode> outNodes = first.getOutNodes();
            while (outNodes.hasNext()) {
                DigraphNode node = outNodes.next();
                int inDegree = inDegrees.get(node).intValue() - 1;
                inDegrees.put(node, new Integer(inDegree));

                // If the in-degree has fallen to 0, place the node on the list
                if (inDegree == 0) {
                    zeroList.add(node);
                }
            }

            return first.getData();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    /**
     * Constructs a <code>PartiallyOrderedSet</code>.
     */
    public PartiallyOrderedSet() {}

    @Override
    public int size() {
        return nodes.size();
    }

    @Override
    public boolean contains(Object o) {
        return nodes.contains(o);
    }

    /**
     * Returns an iterator over the elements contained in this collection, with an ordering 
     * that respects the orderings set by the <code>setOrdering</code> method.
     */
    @Override
    public Iterator<E> iterator() {
        return new PartialOrderIterator(poNodes.values().iterator());
    }

    /**
     * Adds an <code>Object</code> to this <code>PartiallyOrderedSet</code>.
     */
    @Override
    public boolean add(E o) {
        if (nodes.contains(o)) {
            return false;
        }

        poNodes.put(o, new DigraphNode(o));
        return true;
    }

    /**
     * Removes an <code>Object</code> from this
     * <code>PartiallyOrderedSet</code>.
     */
    @Override
    public boolean remove(Object o) {
        DigraphNode node = poNodes.get(o);
        if (node == null) {
            return false;
        }

        poNodes.remove(o);
        node.dispose();
        return true;
    }

    @Override
    public void clear() {
        poNodes.clear();
    }

    /**
     * Sets an ordering between two nodes. When an iterator is requested, the first node will
     * appear earlier in the sequence than the second node.  If a prior ordering existed between
     * the nodes in the opposite order, it is removed.
     *
     * @return <code>true</code> if no prior ordering existed between the nodes,
     * <code>false</code>otherwise.
     */
    public boolean setOrdering(Object first, Object second) {
        DigraphNode firstPONode = poNodes.get(first);
        DigraphNode secondPONode = poNodes.get(second);

        secondPONode.removeEdge(firstPONode);
        return firstPONode.addEdge(secondPONode);
    }

    /**
     * Removes any ordering between two nodes.
     *
     * @return true if a prior prefence existed between the nodes.
     */
    public boolean unsetOrdering(Object first, Object second) {
        DigraphNode firstPONode = poNodes.get(first);
        DigraphNode secondPONode = poNodes.get(second);

        return firstPONode.removeEdge(secondPONode) || secondPONode.removeEdge(firstPONode);
    }

    /**
     * Returns <code>true</code> if an ordering exists between two nodes.
     */
    public boolean hasOrdering(Object preferred, Object other) {
        return poNodes.get(preferred).hasEdge(poNodes.get(other));
    }
}
