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
package org.geotools.renderer3d;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.util.LoggingSystem;
import org.geotools.map.MapContext;
import org.geotools.renderer3d.navigationgestures.NavigationGesture;
import org.geotools.renderer3d.provider.texture.MapTextureRenderer;
import org.geotools.renderer3d.provider.texture.impl.TextureProvider;
import org.geotools.renderer3d.provider.texture.impl.TextureProviderImpl;
import org.geotools.renderer3d.terrainblock.TerrainBlock;
import org.geotools.renderer3d.terrainblock.TerrainBlockFactory;
import org.geotools.renderer3d.utils.ParameterChecker;
import org.geotools.renderer3d.utils.canvas3d.Canvas3D;
import org.geotools.renderer3d.utils.canvas3d.FrameListener;
import org.geotools.renderer3d.utils.quadtree.QuadTree;
import org.geotools.renderer3d.utils.quadtree.QuadTreeImpl;
import org.geotools.renderer3d.utils.quadtree.QuadTreeListener;
import org.geotools.renderer3d.utils.quadtree.QuadTreeNode;

import java.awt.Color;
import java.awt.Component;
import java.util.logging.Level;

/**
 * TODO: Keeps track of all the terrain blocks, calculate the size/distance ratio for them when the camera moves.
 * The size is the length of a side of a terrain block in meters.
 * The distance is the distance from the camera to its center point (on the ground elevation surface) in meters.
 * (NOTE: All values and thresholds can be stored as squares to avoid a square root computation)
 * <ul>
 * <li>If some expanded terrain block is too far away and too small (size/distance under some threshold), collapse it
 * <li>If some collapsed terrain block is too close and too big (size/distance over some threshold), and it is above the minimum terrain block size, expand it
 * <li>If the size/distance ratio for the root node is under some threshold, create a parent, in the direction of the camera
 * </ul>
 * <p/>
 * TODO: Add expand, collapse, and extendRoot methods to the terrain blocks, and a keepExpanded flag to the QuadTreeNode.
 * TODO: Maybe add a generic data object that can be associated with quad tree nodes also, and a way to visit those for
 * a quad tree node and all its sub nodes.  That would allow the camera checks.  There could also be a clear subtree
 * method that removes the data objects from a subtree.
 *
 * @author Hans Häggström
 */
public final class Renderer3DImpl
        implements Renderer3D
{

    //======================================================================
    // Private Fields

    private final Canvas3D myCanvas3D = new Canvas3D();
    private final TerrainBlockFactory myTerrainBlockFactory;
    private final int myTextureSize;
    private final double myVisibilityDistance;

    private final Vector3f myPreviousCameraPosition = new Vector3f();

    private MapContext myMapContext = null;
    private QuadTree<TerrainBlock> myQuadTree;
    private Node myTerrainNode = null;
    private Spatial myRootSpatial;

    //======================================================================
    // Private Constants

    private static final int DEFAULT_TERRAIN_BLOCK_SIZE_IN_GRIDS = 32;
    private static final int DEFAULT_TEXTURE_SIZE = 128;
    private static final double DEFAULT_VISIBILITY_DISTANCE = 100000.0;
    private static final double MINIMUM_NODE_SIZE_M = 50;
    private static final double EXPANSION_THRESHOLD = 2.0;
    private static final double COLLAPSING_THRESHOLD = 1.5;

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // Constructors

    /**
     * Creates a new Renderer3D with 1 km default size for the initial terrain blocks.
     */
    public Renderer3DImpl()
    {
        this( DEFAULT_VISIBILITY_DISTANCE );
    }


    /**
     * Creates a new Renderer3D.
     *
     * @param visibilityDistance_m distance that should be visible, in meters.
     */
    public Renderer3DImpl( final double visibilityDistance_m )
    {
        this( null, visibilityDistance_m );
    }


    /**
     * Creates a new Renderer3D with 1 km default size for the initial terrain blocks.
     *
     * @param mapContextToRender the map context that is used to get the layers to render in the 3D view.
     */
    public Renderer3DImpl( final MapContext mapContextToRender )
    {
        this( mapContextToRender, DEFAULT_VISIBILITY_DISTANCE );
    }


    /**
     * Creates a new Renderer3D.
     *
     * @param mapContextToRender   the map context that is used to get the layers to render in the 3D view.
     * @param visibilityDistance_m distance that should be visible, in meters.
     */
    public Renderer3DImpl( final MapContext mapContextToRender,
                           final double visibilityDistance_m )
    {
        this( mapContextToRender,
              new MapTextureRenderer( mapContextToRender, Color.WHITE ),
              DEFAULT_TERRAIN_BLOCK_SIZE_IN_GRIDS, DEFAULT_TEXTURE_SIZE, visibilityDistance_m );
    }


    /**
     * Creates a new Renderer3D.
     *
     * @param mapContextToRender      the map context that is used to get the layers to render in the 3D view.
     * @param textureRenderer         the renderer that renders chunks of the ground texture on request.
     * @param terrainBlockSizeInGrids number of grid cells along the side of a TerrainBlock.
     * @param textureSize             the size to use for the texture for each terrain block, per side, in pixels.
     * @param visibilityDistance_m    distance that there should be terrain visible in each direction from the camera,
     *                                in display units (TODO: CHECK: meters?).
     */
    public Renderer3DImpl( final MapContext mapContextToRender,
                           final MapTextureRenderer textureRenderer,
                           final int terrainBlockSizeInGrids,
                           final int textureSize, final double visibilityDistance_m )
    {
        ParameterChecker.checkNotNull( mapContextToRender, "mapContextToRender" );
        ParameterChecker.checkPositiveNonZeroInteger( terrainBlockSizeInGrids, "terrainBlockSizeInGrids" );
        ParameterChecker.checkPositiveNonZeroInteger( textureSize, "textureSize" );
        ParameterChecker.checkPositiveNonZeroNormalNumber( visibilityDistance_m, "visibilityDistance_m" );

        myTextureSize = textureSize;
        myVisibilityDistance = visibilityDistance_m;

        myCanvas3D.setViewDistance( (float) myVisibilityDistance );

        final TextureProvider mapTextureProvider = new TextureProviderImpl( textureRenderer,
                                                                            textureSize,
                                                                            Color.WHITE );

/*
        // DEBUG
        final TextureProvider mapTextureProvider = new TextureProviderImpl( new DummyTextureRenderer(),
                                                                            textureSize,
                                                                            Color.GRAY );
*/

        myTerrainBlockFactory = new TerrainBlockFactory( terrainBlockSizeInGrids,
                                                         mapTextureProvider,
                                                         myTextureSize );

        setMapContext( mapContextToRender );

        myQuadTree.addQuadTreeListener( new QuadTreeListener<TerrainBlock>()
        {

            public void onRootChanged( final QuadTreeNode<TerrainBlock> newRoot )
            {
                if ( myTerrainNode != null )
                {
                    update3DModel();
                }
            }

        } );

        // Filter out internal trace info and debug data from JME (for some reason they use INFO level for that)
        LoggingSystem.getLoggingSystem().setLevel( Level.WARNING );

        initExpansionAndCollapsionHandler();
    }

    //----------------------------------------------------------------------
    // Renderer3D Implementation

    public MapContext getMapContext()
    {
        return myMapContext;
    }


    public void setMapContext( final MapContext mapContext )
    {
        if ( myMapContext != mapContext )
        {
            myMapContext = mapContext;

            // Clear the old quadtree and start building a new one, with the data from the new context.
            myQuadTree = new QuadTreeImpl<TerrainBlock>( myVisibilityDistance / 1000, myTerrainBlockFactory );
        }
    }


    public Component get3DView()
    {
        initializeTerrainNodeIfNeeded();

        return myCanvas3D.get3DView();
    }


    public Spatial get3DNode()
    {
        initializeTerrainNodeIfNeeded();

        return myTerrainNode;
    }


    public void addNavigationGesture( final NavigationGesture addedNavigationGesture )
    {
        myCanvas3D.addNavigationGesture( addedNavigationGesture );
    }


    public void removeNavigationGesture( final NavigationGesture removedNavigationGesture )
    {
        myCanvas3D.removeNavigationGesture( removedNavigationGesture );
    }


    public void removeAllNavigationGestures()
    {
        myCanvas3D.removeAllNavigationGestures();
    }


    public void addFrameListener( final FrameListener addedFrameListener )
    {
        myCanvas3D.addFrameListener( addedFrameListener );
    }


    public boolean removeFrameListener( final FrameListener removedFrameListener )
    {
        return myCanvas3D.removeFrameListener( removedFrameListener );
    }

    //======================================================================
    // Private Methods

    private void initExpansionAndCollapsionHandler()
    {
        addFrameListener( new FrameListener()
        {

            public void onFrame( final double secondsSinceLastFrame )
            {
                final Camera camera = myCanvas3D.getCamera();
                if ( camera != null )
                {
                    final Vector3f currentCameraPosition = camera.getLocation();
                    if ( !currentCameraPosition.equals( myPreviousCameraPosition ) )
                    {
                        // Grow quad tree if necessary
                        myQuadTree.getRootNode().growToInclude( currentCameraPosition.x,
                                                                currentCameraPosition.y,
                                                                myVisibilityDistance );

                        // Go through quad tree nodes, recalculate their size/distance ratios,
                        // and expand or collapse them as needed.
                        checkNode( myQuadTree.getRootNode(), currentCameraPosition );

                        myPreviousCameraPosition.set( currentCameraPosition );
                    }
                }
            }

        } );
    }


    private void checkNode( QuadTreeNode<TerrainBlock> node, final Vector3f currentCameraPosition )
    {
        final TerrainBlock terrainBlock = node.getNodeData();

        if ( terrainBlock != null )
        {
            // Calculate size/distance
            final double size = node.getBounds().getSizeAveraged();
            final double squaredSize = size * size;
            final double squaredDistance = terrainBlock.getCenter().distanceSquared( currentCameraPosition );
            final double comparsionFactor = squaredSize / squaredDistance;

            // Expand if needed, and if the node is larger than the minimum node size
            if ( comparsionFactor > EXPANSION_THRESHOLD && size > MINIMUM_NODE_SIZE_M )
            {
                node.setExpanded( true );
            }

            // Collapse if needed
            if ( comparsionFactor < COLLAPSING_THRESHOLD )
            {
                node.setExpanded( false );
            }

            // Recursively call children, if it has them
            final int num = node.getNumberOfChildren();
            for ( int i = 0; i < num; i++ )
            {
                final QuadTreeNode<TerrainBlock> child = node.getChild( i );
                if ( child != null )
                {
                    checkNode( child, currentCameraPosition );
                }
            }
        }
    }


    private void initializeTerrainNodeIfNeeded()
    {
        if ( myTerrainNode == null )
        {
            myTerrainNode = new Node();

            update3DModel();

            myCanvas3D.set3DNode( myTerrainNode );
        }
    }


    private void update3DModel()
    {
        if ( myRootSpatial != null )
        {
            myTerrainNode.detachChild( myRootSpatial );
        }

        myRootSpatial = myQuadTree.getRootNode().getNodeData().getSpatial();

        if ( myRootSpatial != null )
        {
            myTerrainNode.attachChild( myRootSpatial );
        }
    }

}
