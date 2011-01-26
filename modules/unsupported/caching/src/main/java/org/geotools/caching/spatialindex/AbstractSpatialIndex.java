/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.caching.spatialindex;

import java.util.Stack;


/** This is a base class for implementing spatial indexes.
 * It provides common routines useful for every type of indexes.
 *
 * @author Christophe Rousson, SoC 2007, CRG-ULAVAL
 *
 *
 * @source $URL$
 */
public abstract class AbstractSpatialIndex implements SpatialIndex {
    public static final int ContainmentQuery = 1;
    public static final int IntersectionQuery = 2;

    /**
     * The node at the root of index.
     * All others nodes should be direct or indirect children of this one.
     */
    protected NodeIdentifier root;
    protected Node rootNode = null;
    protected Storage store;

    /**
     * Indexes can be n-dimensional, but queries and data should be consistent with regards to dimensions.
     * This is the dimension of data shapes in the index, and should be considered final.
     * (It is not because it makes things easier to initialize index from a serialized form).
     */
    protected int dimension;
    protected Region infiniteRegion;

    protected SpatialIndexStatistics stats = new SpatialIndexStatistics();

    
    public Storage getStorage(){
        return this.store;
    }

    public void intersectionQuery(Shape query, Visitor v) {
        if (query.getDimension() != dimension) {
            throw new IllegalArgumentException(
                "intersectionQuery: Shape has the wrong number of dimensions.");
        }

        rangeQuery(IntersectionQuery, query, v);
    }

    public void containmentQuery(Shape query, Visitor v) {
        if (query.getDimension() != dimension) {
            throw new IllegalArgumentException(
                "containmentQuery: Shape has the wrong number of dimensions.");
        }

        rangeQuery(ContainmentQuery, query, v);
    }

    public void pointLocationQuery(Point query, Visitor v) {
        if (query.getDimension() != dimension) {
            throw new IllegalArgumentException(
                "pointLocationQuery: Shape has the wrong number of dimensions.");
        }

        /*Region r = null;
           if (query instanceof Point) {
               r = new Region((Point) query, (Point) query);
           } else if (query instanceof Region) {
               r = (Region) query;
           } else {
               throw new IllegalArgumentException(
                   "pointLocationQuery: Shape can be Point or Region only.");
           }*/
        Region r = new Region(query, query);

        rangeQuery(IntersectionQuery, r, v);
    }

    /** Common algorithm used by both intersection and containment queries.
     *
     * @param type
     * @param query
     * @param v
     *
     */
    protected void rangeQuery(int type, Shape query, Visitor v) {
        NodePointer current = null;

        Stack<NodePointer> notYetVisitedNodes = new Stack<NodePointer>();
        Stack<NodePointer> visitedNodes = new Stack<NodePointer>();

        if (query.intersects(this.root.getShape())) {
            current = new NodePointer(readNode(this.root));
            notYetVisitedNodes.push(current);
        }
        
        while (!notYetVisitedNodes.isEmpty() || !visitedNodes.isEmpty()) {
            if (!notYetVisitedNodes.isEmpty()) {
                current = notYetVisitedNodes.pop();
                v.visitNode(current.node);

                if (v.isDataVisitor()) { // skip if visitor does nothing with data
                                         // visitData check for actual containment or intersection
                    visitData(current.node, v, query, type);
                }
            } else {
                current = visitedNodes.pop();
            }

            while (current.hasNext()) {
                NodeIdentifier child = current.next();

                if (query.intersects(child.getShape())) {
                    Node n = readNode(child);
                    NodePointer np = new NodePointer(n);
                    notYetVisitedNodes.push(np);
                    visitedNodes.push(current);
                    break;
                }
            }
        }
    }

    /** Visit data associated with a node using given visitor.
     * At this stage, we only know that node's MBR intersects query.
     * This method is reponsible for iterating over node's data, if any,
     * and for checking if data is actually part of the query result.
     * Then it uses the visitor's visit() method on the selected data.
     *
     * @param node to visit
     * @param visitor for callback
     * @param query
     * @param type of query, either containement or intersection (@see AbstractSpatialIndex)
     */
    protected abstract void visitData(Node n, Visitor v, Shape query, int type);

    public void nearestNeighborQuery(int k, Shape query, Visitor v, NearestNeighborComparator nnc) {
        // TODO Auto-generated method stub
    }

    public void nearestNeighborQuery(int k, Shape query, Visitor v) {
        // TODO Auto-generated method stub
    } 
    
    /**
     * Inserts data into the spatial index.
     * 
     * <p>Items with the same "data" and  "shape" 
     * will be considered equal and only one copy of them will be added to the
     * cache.
     * </p>
     * 
     * @param data to insert
     * @param shape associated with data
     * @param id the id of the data
     * 
     * 
     */
    public void insertData(Object data, Shape shape) {
        if (shape.getDimension() != dimension) {
            throw new IllegalArgumentException(
                "insertData: Shape has the wrong number of dimensions.");
        }

        if (this.root.getShape().contains(shape)) {
            insertData(this.root, data, shape);
        } else {
            insertDataOutOfBounds(data, shape);
        }
    }

    /** Insert new data into target node. Node may delegate to child nodes, if required.
     * Implementation note : it is assumed arguments verify :
     * <code>node.getShape().contains(shape)</code>
     * So this must be checked before calling this method.
     *
     * @param node where to insert data
     * @param data
     * @param shape of data
     * @param id of data
     */
    protected abstract void insertData(NodeIdentifier n, Object data, Shape shape);

    /** Insert new data with shape not contained in the current index.
     * Some indexes may require to recreate the root or the index,
     * depending on the type of index ...
     *
     * @param data
     * @param shape
     * @param id
     */
    protected abstract void insertDataOutOfBounds(Object data, Shape shape);

    public Statistics getStatistics() {
        return stats;
    }

    protected Node readNode(NodeIdentifier id) {
        Node ret;
        if ((rootNode != null) && (id.equals(this.root))) {
            return rootNode;
        }

        ret = id.getNode();
        if (ret != null){
            return ret;
        }
        ret = store.get(id);
        stats.stats_reads++;

        id.setNode(ret);
        return ret;
    }

    protected void writeNode(Node node) {
        if (node.getIdentifier().equals(this.root)) {
            this.rootNode = node;
            return;
        }
        store.put(node);
        stats.stats_writes++;
    }

    protected void deleteNode(NodeIdentifier id) {
        store.remove(id);
    }

    public synchronized void flush() {
        if (this.rootNode != null) {
            store.put(this.rootNode);
        }
        store.flush();
    }

    class NodePointer {
        Node node;
        int nextidx = 0;

        NodePointer(Node n) {
            this.node = n;
        }

        boolean hasNext() {
            return (nextidx < node.getChildrenCount());
        }

        NodeIdentifier next() {
            if (hasNext()) {
                NodeIdentifier next = node.getChildIdentifier(nextidx);
                nextidx++;

                return next;
            } else {
                return null;
            }
        }
    }

    
}
