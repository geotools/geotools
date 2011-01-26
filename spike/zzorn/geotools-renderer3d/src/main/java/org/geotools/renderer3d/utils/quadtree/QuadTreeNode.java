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
package org.geotools.renderer3d.utils.quadtree;

import org.geotools.renderer3d.utils.BoundingRectangle;

/**
 * A node in the quad tree.
 * <p/>
 * N is the type of a data object associated with each QuadTreeNode.
 * <p/>
 * TODO: Add some listender method that is notified when the node is collapsed / expanded, if it seems like we need that info for terrain blocks.
 *
 * @author Hans Häggström
 */
public interface QuadTreeNode<N>
{

    //======================================================================
    // Public Methods

    /**
     * @return the axis aligned bounding rectangle for this node.
     */
    BoundingRectangle getBounds();

    /**
     * @return the highest parent node.
     */
    QuadTreeNode<N> getRootNode();


    /**
     * @return the data object associated with this quad tree node, or null if none available.
     */
    N getNodeData();

    /**
     * @param nodeData the data object associated with this quad tree node, or null if none.
     */
    void setNodeData( N nodeData );

    /**
     * Visits the immediate children of this node.
     *
     * @return true if the visits completed normally, false if they were interrupted when the visitor returned false
     *         for some  node.
     */
    boolean visitChildren( NodeVisitor<N> visitor );

    /**
     * Visits the children of this node and all their children, and so forth.
     * <p/>
     * The order is a depth first walk through, with the parent visited before the children.
     *
     * @return true if the visits completed normally, false if they were interrupted when the visitor returned false
     *         for some  node.
     */
    boolean visitDecendants( NodeVisitor<N> visitor );

    /**
     * Visits this node and the the children of this node and all their children, and so forth.
     * <p/>
     * The order is a depth first walk through, with the parent visited before the children.
     *
     * @return true if the visits completed normally, false if they were interrupted when the visitor returned false
     *         for some  node.
     */
    boolean visitSelfAndDecendants( NodeVisitor<N> visitor );

    /**
     * @return number of children that this node has.
     *         (For a normal quad tree this is four).
     */
    int getNumberOfChildren();

    /**
     * @param index the index to get the child from.
     *
     * @return the child at the specifided index, or null if there is none.
     */
    QuadTreeNode<N> getChild( int index );

    /**
     * @param expanded if true, child nodes will be created for this node if it doesn't already have them.
     *                 If false, the child nodes will be deleted, if it has them.
     */
    void setExpanded( final boolean expanded );

    /**
     * @return true if this node is expanded (has child nodes), false if not.
     */
    boolean isExpanded();

    /**
     * Removes / releases allocated resources of this node and all its child nodes.
     */
//    void delete();

    /**
     * @return true if this node covers the area defined by the centerpoint and the radius, false otherwise.
     */
    boolean covers( double x, double y, final double radius );

    /**
     * Grows this QuadTree one step in the direction of the specified point.
     */
    void grow( double x, double y );

    /**
     * Grows the QuadTree by adding parent nodes until it covers the area defined by the specified point and radius.
     *
     * @param radius shortest acceptable distance from the position to the edge of the root node area.
     */
    void growToInclude( double x, double y, double radius );

    /**
     * @return true if this QuadTreeNode is a root node of a QuadTree.
     */
    boolean isRootNode();

    /**
     * Adds the specified NodeListener.  The listener is notified when this node is collapsed, expanded, or deleted.
     *
     * @param addedNodeListener should not be null or already added.
     */
    void addNodeListener( NodeListener<N> addedNodeListener );

    /**
     * Removes the specified NodeListener.
     *
     * @param removedNodeListener should not be null, and should be present.
     */
    void removeNodeListener(
            NodeListener<N> removedNodeListener );

    /**
     * Removes this node from its parent, and marks it as detached.  Detached nodes may later be reused by attaching them.
     */
    void detach();

    /**
     * Attaches this node to the specified parent.  Used when re-using detached nodes.
     */
    void attach( final BoundingRectangle bounds, QuadTreeNode<N> parent );

    /**
     * @return true if this node is attached (part of a quadtree), false if it is detached (not currently used).
     */
    boolean isAttached();

    /**
     * Expand this QuadTreeNode, and use the specified child node for the specified subquadrant.
     * <p/>
     * Called by the quad tree code when a new root node is created, should not be called from client code.
     */
    void expandWithChild( final int childSubquadrant, final QuadTreeNode<N> childNode );

    /**
     * @return the parent node of this QuadTreeNode, or null if this node is the root node.
     */
    QuadTreeNode<N> getParent();

    /**
     * @param childNode a child node to get the position of in the parent
     *
     * @return the index of the specified child node, or -1 if the specified node is not a child.
     */
    int getIndexOfChild( final QuadTreeNode<N> childNode );

    /**
     * @return true if this QuadTreeNode has any Node Data attached to it, false if not.
     */
    boolean hasNodeData();
}

