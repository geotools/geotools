/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import java.text.MessageFormat;
import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Set of elements having a partial order, established by setting before/after relationship calling
 * {@link #setOrder(Object, Object)}
 *
 * @author Andrea Aime
 */
public class PartiallyOrderedSet<E> extends AbstractSet<E> {

    private final boolean throwOnCycle;
    private final Map<E, DirectedGraphNode> elementsToNodes = new LinkedHashMap<>();

    public PartiallyOrderedSet(boolean throwOnCycle) {
        this.throwOnCycle = throwOnCycle;
    }

    public PartiallyOrderedSet() {
        this(true);
    }

    @Override
    public boolean add(E e) {
        if (elementsToNodes.containsKey(e)) {
            return false;
        } else {
            elementsToNodes.put(e, new DirectedGraphNode(e));
            return true;
        }
    }

    @Override
    public boolean remove(Object o) {
        DirectedGraphNode node = elementsToNodes.remove(o);
        if (node == null) {
            return false;
        } else {
            clearNode(node);
            return true;
        }
    }

    private void clearNode(DirectedGraphNode node) {
        node.outgoings.forEach(target -> target.ingoings.remove(node));
        node.outgoings.clear();
        node.ingoings.forEach(source -> source.outgoings.remove(node));
        node.ingoings.clear();
    }

    // TODO: document that returns true if this establishes new ordering
    public boolean setOrder(E source, E target) {
        DirectedGraphNode sourceNode = elementsToNodes.get(source);
        DirectedGraphNode targetNode = elementsToNodes.get(target);
        if (sourceNode == null) {
            throw new IllegalArgumentException("Could not find source node in the set: " + source);
        }
        if (targetNode == null) {
            throw new IllegalArgumentException("Could not find target node in the set: " + target);
        }
        return createDirectedEdge(sourceNode, targetNode);
    }

    // TODO: document that returns true if this establishes new ordering
    private boolean createDirectedEdge(DirectedGraphNode source, DirectedGraphNode target) {
        removeDirectedEdge(/* source= */ target, /* target= */ source);
        boolean sourceNew = source.outgoings.add(target);
        boolean targetNew = target.ingoings.add(source);
        if (sourceNew != targetNew) {
            String message = MessageFormat.format(
                    "Inconsistent edge encountered from [source: {0}] to [target: {1}]:", source, target);
            throw new IllegalStateException(message);
        }
        return targetNew;
    }

    // TODO: document that returns true if ordering existed
    public boolean clearOrder(E source, E target) {
        DirectedGraphNode sourceNode = elementsToNodes.get(source);
        DirectedGraphNode targetNode = elementsToNodes.get(target);
        if (sourceNode == null) {
            throw new IllegalArgumentException("Could not find source node in the set: " + source);
        }
        if (targetNode == null) {
            throw new IllegalArgumentException("Could not find target node in the set: " + target);
        }
        return removeDirectedEdge(sourceNode, targetNode);
    }

    // TODO: document that returns true if ordering existed
    private boolean removeDirectedEdge(DirectedGraphNode source, DirectedGraphNode target) {
        boolean sourceExisted = source.outgoings.remove(target);
        boolean targetExisted = target.ingoings.remove(source);
        if (sourceExisted != targetExisted) {
            String message = MessageFormat.format(
                    "Inconsistent edge encountered from [source: {0}] to [target: {1}]:", source, target);
            throw new IllegalStateException(message);
        }
        return targetExisted;
    }

    @Override
    public Iterator<E> iterator() {
        return new TopologicalSortIterator();
    }

    @Override
    public int size() {
        return elementsToNodes.size();
    }

    /** A graph node with ingoing and outgoing edges to other nodes */
    class DirectedGraphNode {

        E element;

        Set<DirectedGraphNode> outgoings = new HashSet<>();

        Set<DirectedGraphNode> ingoings = new HashSet<>();

        public DirectedGraphNode(E element) {
            this.element = element;
        }

        public E getValue() {
            return element;
        }

        public int getInDegree() {
            return ingoings.size();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("[");
            sb.append(element);
            if (!outgoings.isEmpty()) {
                sb.append(" -> (");
                for (DirectedGraphNode outgoing : outgoings) {
                    sb.append(outgoing.element).append(",");
                }
                // remove last comma and close parens
                sb.setCharAt(sb.length() - 1, ')');
            }
            sb.append("]");
            return sb.toString();
        }
    }

    /** Simple count down object */
    static final class Countdown {
        int value;

        public Countdown(int value) {
            this.value = value;
        }

        public int decrement() {
            return --value;
        }
    }

    /**
     * An iterator returning elements based on the partial order relationship we have in the directed nodes, starting
     * from the sources and moving towards the sinks
     */
    class TopologicalSortIterator implements Iterator<E> {

        // lists of nodes with zero residual inDegrees (aka sources)
        @SuppressWarnings("JdkObsolete") // LinkedList used for specific graph traversal ordering
        private final LinkedList<DirectedGraphNode> sources = new LinkedList<>();

        private final Map<DirectedGraphNode, Countdown> residualInDegrees = new LinkedHashMap<>();

        public TopologicalSortIterator() {
            for (DirectedGraphNode node : elementsToNodes.values()) {
                int inDegree = node.getInDegree();
                if (inDegree == 0) {
                    sources.add(node);
                } else {
                    residualInDegrees.put(node, new Countdown(inDegree));
                }
            }
            if (sources.isEmpty() && !elementsToNodes.isEmpty()) {
                maybeThrowLoopException();
            }
        }

        private void maybeThrowLoopException() {
            if (throwOnCycle) {
                String message = "Some of the partial order relationship form a loop: " + residualInDegrees.keySet();
                throw new IllegalStateException(message);
            }
        }

        @Override
        public boolean hasNext() {
            if (sources.isEmpty() && !residualInDegrees.isEmpty()) {
                maybeThrowLoopException();
            }
            return !sources.isEmpty();
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            DirectedGraphNode next = sources.removeFirst();

            // lower the residual inDegree of all nodes after this one,
            // creating a new set of sources
            for (DirectedGraphNode out : next.outgoings) {
                Countdown countdown = residualInDegrees.get(out);
                if (countdown.decrement() == 0) {
                    sources.add(out);
                    residualInDegrees.remove(out);
                }
            }

            return next.getValue();
        }
    }
}
