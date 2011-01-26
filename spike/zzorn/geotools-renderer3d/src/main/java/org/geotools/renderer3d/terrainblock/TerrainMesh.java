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
package org.geotools.renderer3d.terrainblock;

import com.jme.bounding.BoundingBox;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.TriMesh;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.TextureKey;
import com.jme.util.TextureManager;
import com.jme.util.geom.BufferUtils;
import com.jmex.awt.swingui.ImageGraphics;
import org.geotools.renderer3d.utils.*;

import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.Callable;

/**
 * A rectangular terrain elevation mesh.
 *
 * @author Hans Häggström
 */
public final class TerrainMesh
        extends TriMesh
{

    //======================================================================
    // Private Fields

    private static int theTerrainMeshCounter = 0;

    private final double myZ;
    private final double mySkirtSize;
    private final FloatBuffer myVertexes;
    private final FloatBuffer myColors;
    private final FloatBuffer myTextureCoordinates;
    private final FloatBuffer myNormals;
    private final IntBuffer myIndices;
    private final int myNumberOfVertices;
    private final int myNumberOfCells;
    private final int myNumberOfIndices;
    private final int mySizeY_cells;
    private final int mySizeX_cells;
    private final int mySizeY_vertices;
    private final int mySizeX_vertices;

    private double myX1;
    private double myY1;
    private double myX2;
    private double myY2;

    private TextureState myTextureState;
    private Texture myTexture;
    private ImageGraphics myTextureGraphics;

    private TextureState myPlaceholderTextureState = null;

    private final Object myTextureStateLock = new Object();

    private boolean myPlaceholderTextureInUse = false;

    //======================================================================
    // Private Constants

    private static final double SKIRT_SIZE_FACTOR = 0.1;

    private static final BufferedImage PLACEHOLDER_PICTURE = ImageUtils.createPlaceholderPicture( 128, 128 );
    private static final float DEFAULT_ANISO_LEVEL = 1.0f;
    private static final int DEFAULT_TEXTURE_IMAGE_FORMAT = com.jme.image.Image.GUESS_FORMAT_NO_S3TC;
    private static final BoundingRectangleImpl WHOLE_TEXTURE_AREA = new BoundingRectangleImpl( 0, 0, 1, 1 );
    private static final Texture PLACEHOLDER_TEXTURE = TextureManager.loadTexture( PLACEHOLDER_PICTURE,
                                                                                   Texture.MM_LINEAR_LINEAR,
                                                                                   Texture.FM_LINEAR,
                                                                                   1,
                                                                                   Image.GUESS_FORMAT_NO_S3TC,
                                                                                   false );

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // Constructors

    /**
     * @param sizeXCells Number of grid cells along the X side.
     * @param sizeYCells Number of grid cells along the Y side.
     * @param x1         first world coordinate.
     * @param y1         first world coordinate.
     * @param x2         second world coordinate.  Should be larger than the first.
     * @param y2         second world coordinate.  Should be larger than the first.
     * @param z          the default height level.
     */
    public TerrainMesh( final int sizeXCells,
                        final int sizeYCells,
                        final double x1,
                        final double y1,
                        final double x2,
                        final double y2,
                        final double z )
    {
        // JME seems to need an unique identifier for each node.  NOTE: Not thread safe.
        super( "TerrainMesh_" + theTerrainMeshCounter++ );

        // Check parameters
        ParameterChecker.checkPositiveNonZeroInteger( sizeXCells, "sizeXCells" );
        ParameterChecker.checkPositiveNonZeroInteger( sizeYCells, "sizeYCells" );
        ParameterChecker.checkNormalNumber( z, "z" );

        // Assign fields from parameters
        // Cells are the rectangular areas between four normal surface vertices.  Does not include the rectangles in the downturned skirt.
        // Vertices are the vertex points making up the grid corners of the mesh.  Also includes the vertices used to make the downturned skirt.
        mySizeX_cells = sizeXCells;
        mySizeY_cells = sizeYCells;
        mySizeX_vertices = mySizeX_cells + 1 + 2;
        mySizeY_vertices = mySizeY_cells + 1 + 2;
        myZ = z;

        mySkirtSize = calculateSkirtSize();

        // Calculate sizes
        myNumberOfVertices = mySizeX_vertices * mySizeY_vertices;
        myNumberOfCells = mySizeX_cells * mySizeY_cells;
        myNumberOfIndices = ( mySizeX_vertices - 1 ) * ( mySizeY_vertices - 1 ) * 6;

        // Create databuffers
        myVertexes = BufferUtils.createVector3Buffer( myNumberOfVertices );
        myColors = BufferUtils.createColorBuffer( myNumberOfVertices );
        myTextureCoordinates = BufferUtils.createVector2Buffer( myNumberOfVertices );
        myNormals = BufferUtils.createVector3Buffer( myNumberOfVertices );
        myIndices = BufferUtils.createIntBuffer( myNumberOfIndices );

        // Stich together the vertices into triangles
        initializeIndices();

        updateBounds( x1, y1, x2, y2 );


    }

    //----------------------------------------------------------------------
    // Other Public Methods

    /**
     * Updates the positon and covered area of the terrain mesh.
     * <p/>
     * Called from the constructor, as well as when a TerrainMesh is re-used.
     *
     * @param x1 first world coordinate.
     * @param y1 first world coordinate.
     * @param x2 second world coordinate.  Should be larger than the first.
     * @param y2 second world coordinate.  Should be larger than the first.
     */
    public void updateBounds( final double x1,
                              final double y1,
                              final double x2,
                              final double y2 )
    {
        ParameterChecker.checkNormalNumber( x1, "x1" );
        ParameterChecker.checkNormalNumber( y1, "y1" );
        ParameterChecker.checkNormalNumber( x2, "x2" );
        ParameterChecker.checkNormalNumber( y2, "y2" );
        ParameterChecker.checkLargerThan( x2, "x2", x1, "x1" );
        ParameterChecker.checkLargerThan( y2, "y2", y1, "y1" );

        myX1 = x1;
        myY1 = y1;
        myX2 = x2;
        myY2 = y2;

        // Put vertices in a grid formation in the correct place in the world
        initializeVetices();

        // Initialize the TriMesh
        setVertexBuffer( 0, myVertexes );
        setColorBuffer( 0, myColors );
        setTextureBuffer( 0, myTextureCoordinates );
        setNormalBuffer( 0, myNormals );
        setIndexBuffer( 0, myIndices );

        // Update bounding box
        setModelBound( new BoundingBox() );
        updateModelBound();
    }


    /**
     * Creates a texture from the specified image and applies it to this Terrainmesh.
     *
     * @param textureImage the image to create a texture from.  If null, a placeholder texture is created.
     */
    public void setTextureImage( final BufferedImage textureImage )
    {
        GameTaskQueueManager.getManager().getQueue( GameTaskQueue.UPDATE ).enqueue( new Callable<Object>()
        {

            public Object call() throws Exception
            {
                final Renderer renderer = DisplaySystem.getDisplaySystem().getRenderer();
                initTexture( textureImage, renderer );
                return null;
            }

        } );
    }


    /**
     * @return the texture that this TerrainMesh is currently using, or null if it is using a placeholder texture.
     */
    public Texture getTexture()
    {
        return myTexture;
    }


    public void setPlaceholderTexture( final Texture texture, final BoundingRectangle textureArea )
    {
        synchronized ( myTextureStateLock )
        {
            myPlaceholderTextureInUse = true;

            if ( myTextureState != null )
            {
                // Update texture indexes
                setTextureCoordinates( textureArea );

                // Update the geometry
                updateGeometricState( 0, true );

                // Use placeholder if no texture specified
                Texture textureToUse = texture;
                if ( textureToUse == null )
                {
                    textureToUse = PLACEHOLDER_TEXTURE;
                }

                // Set the texture
                myTextureState.setTexture( textureToUse );

                updateRenderState();
            }
        }
    }

    public boolean isPlaceholderTextureInUse()
    {
        return myPlaceholderTextureInUse;
    }

    //======================================================================
    // Private Methods

    private double calculateSkirtSize()
    {
        final double cellSizeX = ( myX2 - myX1 ) / mySizeX_cells;
        final double cellSizeY = ( myY2 - myY1 ) / mySizeY_cells;
        return Math.max( cellSizeX, cellSizeY ) * SKIRT_SIZE_FACTOR;
    }


    private void initializeVetices()
    {
        for ( int y = 0; y < mySizeY_vertices; y++ )
        {
            for ( int x = 0; x < mySizeX_vertices; x++ )
            {
                initializeVertex( x, y );
            }
        }
    }


    private void initializeVertex( final int x, final int y )
    {
        final int index = calculateMeshIndex( x, y );

        // Calculate position
        final float xPos = (float) MathUtils.interpolateClamp( x, 1, mySizeX_vertices - 2, myX1, myX2 );
        final float yPos = (float) MathUtils.interpolateClamp( y, 1, mySizeY_vertices - 2, myY1, myY2 );
        float zPos = (float) myZ;

        // Fold the edges down to form a skirt, to avoid one pixel cracks between terrain blocks caused by rounding errors.
        if ( isEdge( x, y ) )
        {
            zPos -= mySkirtSize;
        }

        // Stretch the texture across the terrain mesh, and have downturned edges have the same color as the edge
        final float textureXPos = (float) MathUtils.interpolateClamp( x, 1, mySizeX_vertices - 2, 0, 1 );
        final float textureYPos = (float) MathUtils.interpolateClamp( y, 1, mySizeY_vertices - 2, 1, 0 );

        final Vector3f position = new Vector3f( xPos, yPos, zPos );
        final Vector3f normal = new Vector3f( 0, 0, 1 );
        final ColorRGBA color = new ColorRGBA( 1.0f, 1.0f, 1.0f, 1.0f );
        final Vector2f textureCoordinate = new Vector2f( textureXPos, textureYPos );

        BufferUtils.setInBuffer( position, myVertexes, index );
        BufferUtils.setInBuffer( normal, myNormals, index );
        BufferUtils.setInBuffer( color, myColors, index );
        BufferUtils.setInBuffer( textureCoordinate, myTextureCoordinates, index );
    }


    private boolean isEdge( final int x, final int y )
    {
        return x == 0 ||
               y == 0 ||
               x == mySizeX_vertices - 1 ||
               y == mySizeY_vertices - 1;
    }


    private int calculateMeshIndex( final int x, final int y )
    {
        return x + y * mySizeX_vertices;
    }


    private void initializeIndices()
    {
        // OPTIMIZE: Use triangle strips or fans to get more efficient results!

        // Create indices indicating the connections
        int index = 0;
        for ( int y = 0; y < mySizeY_vertices - 1; y++ )
        {
            for ( int x = 0; x < mySizeX_vertices - 1; x++ )
            {
                final int topLeft = x + y * mySizeX_vertices;
                final int topRight = ( x + 1 ) + y * mySizeX_vertices;
                final int bottomLeft = x + ( y + 1 ) * mySizeX_vertices;
                final int bottomRight = ( x + 1 ) + ( y + 1 ) * mySizeX_vertices;

                myIndices.put( index++, topLeft );
                myIndices.put( index++, bottomRight );
                myIndices.put( index++, topRight );
                myIndices.put( index++, bottomRight );
                myIndices.put( index++, topLeft );
                myIndices.put( index++, bottomLeft );
            }
        }
    }


    private void initTexture( BufferedImage textureImage, final Renderer renderer )
    {
        synchronized ( myTextureStateLock )
        {
            // The texture can be null e.g. if we ran out of memory
            if ( textureImage == null )
            {
                textureImage = PLACEHOLDER_PICTURE;
            }

            // Remove any placeholder render state
            if ( myPlaceholderTextureInUse )
            {
/*
            clearRenderState( RenderState.RS_TEXTURE );
*/
/*
            myPlaceholderTextureState.setEnabled( false );
            myPlaceholderTextureState.setTexture( null );
            myPlaceholderTextureState = null;
*/
                setTextureCoordinates( WHOLE_TEXTURE_AREA );
/*
*/
            }

            if ( myTextureGraphics == null )
            {
                // First time initializations:

                // Create JME Image Renderer
                myTextureGraphics = ImageGraphics.createInstance( textureImage.getWidth( null ),
                                                                  textureImage.getHeight( null ),
                                                                  0 );
                myTextureGraphics.drawImage( textureImage, 0, 0, null );
                myTextureGraphics.update();

                // Create texture
                myTexture = TextureManager.loadTexture( null,
                                                        createTextureKey( textureImage.hashCode() ),
                                                        myTextureGraphics.getImage() );

                // Make sure this texture is not cached, as we will be updating it when the TerrainMesh is re-used
                TextureManager.releaseTexture( myTexture );

                // Clamp texture at edges (no wrapping)
                myTexture.setWrap( Texture.WM_ECLAMP_S_ECLAMP_T );
                myTexture.setMipmapState( Texture.MM_LINEAR_LINEAR );

                createTextureRenderState( renderer, myTexture );

                if ( myPlaceholderTextureInUse )
                {
                    myTextureState.setTexture( myTexture, 0 );
                }
            }
            else
            {
                // Release the previously reserved textures, so that they don't take up space on the 3D card
                // NOTE: Maybe this also forces JME to re-upload the changed texture?
                if ( !myPlaceholderTextureInUse )
                {
                    myTextureState.deleteAll( true );
                }
                else
                {
                    myTextureState.setTexture( myTexture, 0 );
                    myTextureState.deleteAll( true );
                }

                // Update the JME Image used by the texture
                myTextureGraphics.drawImage( textureImage, 0, 0, null );
                myTextureGraphics.update();
                myTextureGraphics.update( myTexture );

                // Make sure this texture is not cached, as we will be updating it when the TerrainMesh is re-used
                TextureManager.releaseTexture( myTexture );

                // Smoother look at low viewing angles
                myTexture.setMipmapState( Texture.MM_LINEAR_LINEAR );
            }

            myPlaceholderTextureInUse = false;
        }
    }


    private void createTextureRenderState( final Renderer renderer, final Texture texture )
    {
        myTextureState = renderer.createTextureState();
        myTextureState.setEnabled( true );
        myTextureState.setTexture( texture, 0 );
        setRenderState( myTextureState );
        updateRenderState();
    }


    private TextureKey createTextureKey( final int imageHashcode )
    {
        final TextureKey tkey = new TextureKey( null, Texture.MM_LINEAR, Texture.FM_LINEAR,
                                                DEFAULT_ANISO_LEVEL, false, DEFAULT_TEXTURE_IMAGE_FORMAT );
        tkey.setFileType( "" + imageHashcode );
        return tkey;
    }


    private void setTextureCoordinates( BoundingRectangle boundingRectangle )
    {
        if ( boundingRectangle == null )
        {
            boundingRectangle = WHOLE_TEXTURE_AREA;
        }

        for ( int y = 0; y < mySizeY_vertices; y++ )
        {
            for ( int x = 0; x < mySizeX_vertices; x++ )
            {
                final int index = calculateMeshIndex( x, y );

                final float textureXPos = (float) MathUtils.interpolateClamp( x,
                                                                              1,
                                                                              mySizeX_vertices - 2,
                                                                              boundingRectangle.getX1(),
                                                                              boundingRectangle.getX2() );
                final float textureYPos = (float) MathUtils.interpolateClamp( y,
                                                                              1,
                                                                              mySizeY_vertices - 2,
                                                                              boundingRectangle.getY2(),
                                                                              boundingRectangle.getY1() );

                final Vector2f textureCoordinate = new Vector2f( textureXPos, textureYPos );

                BufferUtils.setInBuffer( textureCoordinate, myTextureCoordinates, index );
            }
        }

        setTextureBuffer( 0, myTextureCoordinates );
    }

}
