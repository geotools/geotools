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
package org.geotools;

import java.util.AbstractSet;
import java.util.Collection;
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
class PartiallyOrderedSet<E> extends AbstractSet<E> {
    
    private Map<E, DirectedGraphNode<E>> elementsToNodes = new LinkedHashMap<>();
    
    @Override
    public boolean add(E e) {
        if(elementsToNodes.containsKey(e)) {
            return false;
        } else {
            elementsToNodes.put(e, new DirectedGraphNode<E>(e));
            return true;
        }
    }
    
    @Override
    public boolean remove(Object o) {
        DirectedGraphNode<E> node = elementsToNodes.remove(o);
        if(node == null) {
            return false;
        } else {
            node.clear();
            return true;
        }
    }
    
    public void setOrder(E source, E target) {
        DirectedGraphNode<E> sourceNode = elementsToNodes.get(source);
        DirectedGraphNode<E> targetNode = elementsToNodes.get(target);
        if(sourceNode == null) {
            throw new IllegalArgumentException("Could not find source node in the set: " + source);
        }
        if(targetNode == null) {
            throw new IllegalArgumentException("Could not find target node in the set: " + target);
        }
        sourceNode.addOutgoing(targetNode);
    }
    
    public void clearOrder(E source, E target) {
        DirectedGraphNode<E> sourceNode = elementsToNodes.get(source);
        DirectedGraphNode<E> targetNode = elementsToNodes.get(target);
        if(sourceNode == null) {
            throw new IllegalArgumentException("Could not find source node in the set: " + source);
        }
        if(targetNode == null) {
            throw new IllegalArgumentException("Could not find target node in the set: " + target);
        }
        // clear both directions to be sure
        sourceNode.removeOutgoing(targetNode);
        targetNode.removeOutgoing(sourceNode);
    }

    @Override
    public Iterator<E> iterator() {
        return new TopologicalSortIterator();
    }

    @Override
    public int size() {
        return elementsToNodes.size();
    }

    
    /**
     * A graph node with ingoing and outgoing edges to other nodes
     *  
     * @param <E>
     */
    class DirectedGraphNode<E> {

        E element;
        
        Set<DirectedGraphNode<E>> outgoings = new HashSet<>();
        
        Set<DirectedGraphNode<E>> ingoings = new HashSet<>();

        public DirectedGraphNode(E element) {
            this.element = element;
        }

        /**
         * Clean up all ingoing and outgoing edges
         */
        public void clear() {
            outgoings.clear();
            ingoings.clear();
        }

        public boolean removeOutgoing(DirectedGraphNode<E> targetNode) {
            targetNode.ingoings.remove(this);
            return outgoings.remove(targetNode);
            
        }

        public void addOutgoing(DirectedGraphNode<E> targetNode) {
            // keep the link between two nodes going in a single direction
            targetNode.ingoings.add(this);
            targetNode.outgoings.remove(this);
            outgoings.add(targetNode);
        }

        public Collection<DirectedGraphNode<E>> getOutgoings() {
            return outgoings;
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
            if(outgoings.size() > 0) {
                sb.append(" -> (");
                for (DirectedGraphNode<E> outgoing : outgoings) {
                    sb.append(outgoing.element).append(",");
                }
                // remove last comma and close parens
                sb.setCharAt(sb.length() - 1, ')');
            }
            sb.append("]");
            return sb.toString();
        }
    }
    
    /**
     * Simple count down object
     */
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
     * An iterator returning elements based on the partial order relationship we have
     * in the directed nodes, starting from the sources and moving towards the sinks
     */
    class TopologicalSortIterator implements Iterator<E> {
        
        // lists of nodes with zero residual inDegrees (aka sources)
        LinkedList<DirectedGraphNode<E>> sources = new LinkedList<>();
        Map<DirectedGraphNode<E>, Countdown> residualInDegrees = new LinkedHashMap<>();
        
        
        public TopologicalSortIterator() {
            for (DirectedGraphNode<E> node : elementsToNodes.values()) {
                int inDegree = node.getInDegree();
                if(inDegree == 0) {
                    sources.add(node);
                } else {
                    residualInDegrees.put(node, new Countdown(inDegree));
                }
            }
            if(sources.size() == 0) {
                throwLoopException();
            }
        }

        private void throwLoopException() {
            String message = "Some of the partial order relationship form a loop: " + residualInDegrees.keySet();
            throw new IllegalStateException(message);
        }

        @Override
        public boolean hasNext() {
            if(sources.isEmpty() && !residualInDegrees.isEmpty()) {
                throwLoopException();
            }
            return !sources.isEmpty();
        }

        @Override
        public E next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            DirectedGraphNode<E> next = sources.removeFirst();
            
            // lower the residual inDegree of all nodes after this one,
            // creating a new set of sources
            for (DirectedGraphNode<E> out : next.getOutgoings()) {
                Countdown countdown = residualInDegrees.get(out);
                if(countdown.decrement() == 0) {
                    sources.add(out);
                    residualInDegrees.remove(out);
                }
            }
            
            return next.getValue();
        }
        
    }
}
