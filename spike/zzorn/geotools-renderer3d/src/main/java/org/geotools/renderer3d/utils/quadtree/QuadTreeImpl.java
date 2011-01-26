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
import org.geotools.renderer3d.utils.ParameterChecker;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * @author Hans Häggström
 */
public class QuadTreeImpl<N>
        implements QuadTree<N>
{

    //======================================================================
    // Private Fields

    private final double myStartRadius;

    private final NodeDataFactory<N> myNodeDataFactory;

    private final List<QuadTreeListener<N>> myListeners = new ArrayList<QuadTreeListener<N>>( 3 );

    private QuadTreeNode<N> myRootNode;

    //======================================================================
    // Private Constants

    private static final NodeDataFactory NULL_NODE_DATA_FACTORY = new NodeDataFactory()
    {

        public Object createNodeDataObject( final QuadTreeNode node )
        {
            return null;
        }

        public Object reuseNodeDataObject( final QuadTreeNode node, final Object nodeData )
        {
            return null;
        }

        public void onDataObjectUnused( final Object nodeData )
        {

        }
    };

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // Constructors

    /**
     * @param startRadius
     * @param nodeDataFactory a factory for creating data objects for nodes.  May be null (in that case all node data objects will be null by default).
     */
    public QuadTreeImpl( final double startRadius,
                         NodeDataFactory<N> nodeDataFactory )
    {
        ParameterChecker.checkPositiveNonZeroNormalNumber( startRadius, "startRadius" );

        myStartRadius = startRadius;


        if ( nodeDataFactory == null )
        {
            myNodeDataFactory = NULL_NODE_DATA_FACTORY;
        }
        else
        {
            myNodeDataFactory = nodeDataFactory;
        }
    }

    //----------------------------------------------------------------------
    // QuadTree Implementation

    public QuadTreeNode<N> getRootNode()
    {
        buildRootNodeIfNeeded( 0, 0 );

        return myRootNode;
    }


    public void setRootNode( QuadTreeNode<N> newRootNode )
    {
        ParameterChecker.checkNotNull( newRootNode, "newRootNode" );

        myRootNode = newRootNode;

        for ( QuadTreeListener<N> listener : myListeners )
        {
            listener.onRootChanged( myRootNode );
        }
    }


    public NodeDataFactory<N> getNodeDataFactory()
    {
        return myNodeDataFactory;
    }


    public void addQuadTreeListener( QuadTreeListener<N> addedQuadTreeListener )
    {
        ParameterChecker.checkNotNull( addedQuadTreeListener, "addedQuadTreeListener" );
        ParameterChecker.checkNotAlreadyContained( addedQuadTreeListener, myListeners, "myListeners" );

        myListeners.add( addedQuadTreeListener );
    }


    public void removeQuadTreeListener( QuadTreeListener<N> removedQuadTreeListener )
    {
        ParameterChecker.checkNotNull( removedQuadTreeListener, "removedQuadTreeListener" );
        ParameterChecker.checkContained( removedQuadTreeListener, myListeners, "myListeners" );

        myListeners.remove( removedQuadTreeListener );
    }


    private final LinkedList<QuadTreeNode<N>> myQuadTreeNodePool = new LinkedList<QuadTreeNode<N>>();

    public QuadTreeNode<N> createQuadTreeNode( final BoundingRectangle bounds,
                                               final QuadTreeNode<N> parentNode )
    {
        final QuadTreeNode<N> quadTreeNode;
        if ( myQuadTreeNodePool.isEmpty() )
        {
            quadTreeNode = new QuadTreeNodeImpl<N>( this, bounds, parentNode );
        }
        else
        {
            quadTreeNode = myQuadTreeNodePool.removeLast();
            quadTreeNode.attach( bounds, parentNode );
        }

        return quadTreeNode;
    }

    public void initnodedata( final QuadTreeNode<N> quadTreeNode )
    {
        final N nodeData;
        if ( quadTreeNode.hasNodeData() )
        {
            nodeData = myNodeDataFactory.reuseNodeDataObject( quadTreeNode, quadTreeNode.getNodeData() );
        }
        else
        {
            nodeData = myNodeDataFactory.createNodeDataObject( quadTreeNode );
        }

        quadTreeNode.setNodeData( nodeData );
    }

    public void releaseQuadTreeNode( final QuadTreeNode<N> node )
    {
        final N nodeData = node.getNodeData();

        myNodeDataFactory.onDataObjectUnused( nodeData );

        node.detach();

        myQuadTreeNodePool.addLast( node );
    }

    //======================================================================
    // Private Methods

    private void buildRootNodeIfNeeded( final double startCenterX, final double startCenterY )
    {
        if ( myRootNode == null )
        {
            myRootNode = new QuadTreeNodeImpl<N>( this, startCenterX, startCenterY, myStartRadius );
        }
    }

}
