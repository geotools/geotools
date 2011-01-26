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
import org.geotools.renderer3d.utils.BoundingRectangleImpl;
import org.geotools.renderer3d.utils.ParameterChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans Häggström
 */
public final class QuadTreeNodeImpl<N>
        implements QuadTreeNode<N>
{

    //======================================================================
    // Private Fields

    private final QuadTree<N> myQuadTree;
    private final List<NodeListener<N>> myNodeListeners = new ArrayList<NodeListener<N>>( 3 );

    private QuadTreeNode<N> myParent = null;
    private BoundingRectangle myBoundingRectangle;
    private QuadTreeNode<N>[] myChildren = null;
    private N myNodeData = null;
    private boolean myExpanded = false;
    private boolean myAttached = true;

    //======================================================================
    // Private Constants

    private static final int NUMBER_OF_SUBNODES = 4;
    private static final BoundingRectangleImpl ORIGO_BOUNDING_RECTANGLE = new BoundingRectangleImpl( 0, 0, 10 );

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // Constructors

    public QuadTreeNodeImpl( final QuadTree<N> quadTree,
                             BoundingRectangle boundingRectangle )
    {
        this( quadTree, boundingRectangle, null );
    }


    public QuadTreeNodeImpl( final QuadTree<N> quadTree,
                             final BoundingRectangle boundingRectangle,
                             final QuadTreeNode<N> parentNode )
    {
        ParameterChecker.checkNotNull( quadTree, "quadTree" );

        setBounds( boundingRectangle );

        myQuadTree = quadTree;
        myParent = parentNode;
    }


    public QuadTreeNodeImpl( QuadTree<N> quadTree, double centerX, double centerY, double radius )
    {
        this( quadTree,
              centerX - radius,
              centerY - radius,
              centerX + radius,
              centerY + radius );

        ParameterChecker.checkPositiveNonZeroNormalNumber( radius, "radius" );
        ParameterChecker.checkNormalNumber( centerX, "centerX" );
        ParameterChecker.checkNormalNumber( centerY, "centerY" );
    }


    public QuadTreeNodeImpl( final QuadTree<N> quadTree,
                             final double x1,
                             final double y1,
                             final double x2,
                             final double y2 )
    {
        this( quadTree, new BoundingRectangleImpl( x1, y1, x2, y2 ) );
    }

    //----------------------------------------------------------------------
    // QuadTreeNode Implementation

    public BoundingRectangle getBounds()
    {
        checkNodeIsAttached();

        return myBoundingRectangle;
    }


    public QuadTreeNode<N> getRootNode()
    {
        checkNodeIsAttached();

        if ( myParent != null )
        {
            return myParent.getRootNode();
        }
        else
        {
            return this;
        }
    }

    public boolean hasNodeData()
    {
        return myNodeData != null;
    }

    public N getNodeData()
    {
        checkNodeIsAttached();

        // Lazy creation
        if ( myNodeData == null )
        {
            myNodeData = myQuadTree.getNodeDataFactory().createNodeDataObject( this );
        }

        return myNodeData;
    }


    public void setNodeData( final N nodeData )
    {
        checkNodeIsAttached();

        myNodeData = nodeData;
    }


    public boolean visitChildren( final NodeVisitor<N> visitor )
    {
        checkNodeIsAttached();

        if ( myChildren != null )
        {
            for ( QuadTreeNode<N> child : myChildren )
            {
                if ( !visitor.visitNode( child ) )
                {
                    return false;
                }
            }
        }

        return true;
    }


    public boolean visitDecendants( final NodeVisitor<N> visitor )
    {
        checkNodeIsAttached();

        if ( myChildren != null )
        {
            for ( QuadTreeNode<N> child : myChildren )
            {
                if ( !child.visitSelfAndDecendants( visitor ) )
                {
                    return false;
                }
            }
        }

        return true;
    }


    public boolean visitSelfAndDecendants( final NodeVisitor<N> visitor )
    {
        checkNodeIsAttached();

        if ( visitor.visitNode( this ) )
        {
            return visitDecendants( visitor );
        }
        else
        {
            return false;
        }
    }


    public int getNumberOfChildren()
    {
        checkNodeIsAttached();

        return NUMBER_OF_SUBNODES;
    }


    public QuadTreeNode<N> getChild( final int index )
    {
        checkNodeIsAttached();

        if ( myChildren == null )
        {
            return null;
        }
        else
        {
            return myChildren[ index ];
        }
    }


    public void setExpanded( final boolean expanded )
    {
        checkNodeIsAttached();

        if ( expanded )
        {
            expand();
        }
        else
        {
            collapse();
        }
    }


    public boolean isExpanded()
    {
        checkNodeIsAttached();

        return myExpanded;
    }


    public boolean covers( final double x, final double y, final double radius )
    {
        checkNodeIsAttached();

        return getBounds().isInside( x, y, radius );
    }


    public void grow( double x, double y )
    {
        checkNodeIsAttached();

        if ( isRootNode() )
        {
            // Calcualte which direction the new parent should expand into
            final int parentSubsector = myBoundingRectangle.getSubsectorAt( x, y );

            // Create a new parent
            final BoundingRectangle parentBounds = myBoundingRectangle.createParentBoundingRectangle( parentSubsector );
            final QuadTreeNode<N> parentNode = myQuadTree.createQuadTreeNode( parentBounds, null );
            myQuadTree.initnodedata( parentNode );

            // Add this node as a child of the parent node (in the opposite corner of where we expanded)
            final int childSubquadrant = myBoundingRectangle.getOppositeSubquadrant( parentSubsector );
            parentNode.expandWithChild( childSubquadrant, this );

            myParent = parentNode;

            // Notify model that we have a new root node
            myQuadTree.setRootNode( parentNode );

        }
        else
        {
            getRootNode().grow( x, y );
        }
    }


    public void growToInclude( double x, double y, final double radius )
    {
        checkNodeIsAttached();

        while ( !getRootNode().covers( x, y, radius ) )
        {
            grow( x, y );
        }
    }


    public boolean isRootNode()
    {
        checkNodeIsAttached();

        return myParent == null;
    }


    public void addNodeListener( NodeListener<N> addedNodeListener )

    {
        checkNodeIsAttached();

        ParameterChecker.checkNotNull( addedNodeListener, "addedNodeListener" );
        ParameterChecker.checkNotAlreadyContained( addedNodeListener, myNodeListeners, "myNodeListeners" );

        myNodeListeners.add( addedNodeListener );
    }


    public void removeNodeListener(
            NodeListener<N> removedNodeListener )

    {
        checkNodeIsAttached();

        ParameterChecker.checkNotNull( removedNodeListener, "removedNodeListener" );
        ParameterChecker.checkContained( removedNodeListener, myNodeListeners, "myNodeListeners" );

        myNodeListeners.remove( removedNodeListener );
    }


    public void detach()
    {
        checkNodeIsAttached();

        myParent = null;
        myAttached = false;
        myChildren = null;
        myBoundingRectangle = ORIGO_BOUNDING_RECTANGLE;
    }


    public void attach( final BoundingRectangle bounds, QuadTreeNode<N> parent )
    {
        if ( myAttached )
        {
            throw new IllegalStateException( "A node that was already attached to '" + myParent + "' was attempted to be attached to '" + parent + "' " );
        }

        myParent = parent;
        myAttached = true;

        setBounds( bounds );
    }


    public boolean isAttached()
    {
        return myAttached;
    }


    public void expandWithChild( final int childSubquadrant, final QuadTreeNode<N> childNode )
    {
        checkNodeIsAttached();

        if ( !myExpanded )
        {
            myExpanded = true;

            // Create the child array if needed
            if ( myChildren == null )
            {
                //noinspection unchecked
                myChildren = new QuadTreeNode[NUMBER_OF_SUBNODES];
            }

            // Create child nodes
            for ( int i = 0; i < NUMBER_OF_SUBNODES; i++ )
            {
                if ( myChildren[ i ] != null )
                {
                    throw new IllegalStateException( "A newly expanded node should not have any children" );
                }

                if ( i == childSubquadrant )
                {
                    myChildren[ i ] = childNode;
                }
                else
                {
                    final BoundingRectangle rectangle = myBoundingRectangle.createSubquadrantBoundingRectangle( i );
                    myChildren[ i ] = myQuadTree.createQuadTreeNode( rectangle, this );
                }
            }

            // Init node data for the other children
            for ( int i = 0; i < myChildren.length; i++ )
            {
                if ( i != childSubquadrant )
                {
                    myQuadTree.initnodedata( myChildren[ i ] );
                }
            }

            for ( NodeListener<N> nodeListener : myNodeListeners )
            {
                nodeListener.onExpanded( this );
            }
        }
    }


    public QuadTreeNode<N> getParent()
    {
        return myParent;
    }


    public int getIndexOfChild( final QuadTreeNode<N> childNode )
    {
        for ( int i = 0; i < myChildren.length; i++ )
        {
            if ( childNode == myChildren[ i ] )
            {
                return i;
            }
        }

        return -1;
    }

    //======================================================================
    // Private Methods

    private void setBounds( final BoundingRectangle boundingRectangle )
    {
        checkNodeIsAttached();

        ParameterChecker.checkNotNull( boundingRectangle, "boundingRectangle" );
        if ( boundingRectangle.isEmpty() )
        {
            throw new IllegalArgumentException( "The bounding rectangle '" + boundingRectangle +
                                                "' specified for the QuadTreeNode should not be empty." );
        }

        myBoundingRectangle = boundingRectangle;
    }


    private void checkNodeIsAttached()
    {
        if ( !isAttached() )
        {
            throw new IllegalStateException( "Can not do any operations to a detached node." );
        }
    }


    private void expand()
    {
        checkNodeIsAttached();

        expandWithChild( -1, null );
    }


    private void collapse()
    {
        checkNodeIsAttached();

        if ( myExpanded )
        {
            myExpanded = false;

            if ( myChildren != null )
            {
                // Delete all child nodes
                for ( int i = 0; i < NUMBER_OF_SUBNODES; i++ )
                {
                    final QuadTreeNode<N> child = myChildren[ i ];
                    if ( child != null )
                    {
                        child.setExpanded( false );

                        myQuadTree.releaseQuadTreeNode( child );
                    }
                    myChildren[ i ] = null;
                }
            }

            myChildren = null;

            for ( NodeListener<N> nodeListener : myNodeListeners )
            {
                nodeListener.onCollapsed( this );
            }
        }
    }

}

