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
package org.geotools.renderer3d.utils.canvas3d;

import com.jme.renderer.Camera;
import com.jme.renderer.Renderer;
import com.jme.scene.Spatial;
import com.jme.scene.state.CullState;
import com.jme.system.DisplaySystem;
import com.jmex.awt.JMECanvas;
import com.jmex.awt.SimpleCanvasImpl;
import org.geotools.renderer3d.navigationgestures.*;
import org.geotools.renderer3d.utils.CursorChangerImpl;
import org.geotools.renderer3d.utils.FpsCounter;
import org.geotools.renderer3d.utils.ParameterChecker;

import javax.swing.*;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * A 3D Canvas, showing a 3D object in an AWT Canvas component.
 * <p/>
 * Allows registering Gestures, that can be used to navigate the 3D view (already has default gestures registered).
 * <p/>
 * Also allows adding FrameListeners, that are called after each rendering frame in the swing thread.
 * IDEA: Also call them (a different method) in the opengl thread, before rendering.
 *
 * @author Hans Häggström
 */
public final class Canvas3D
{

    //======================================================================
    // Private Fields

    private final Set<NavigationGesture> myNavigationGestures = new HashSet<NavigationGesture>();
    private final CursorChangerImpl myCursorChanger = new CursorChangerImpl();
    private final FpsCounter myFpsCounter = new FpsCounter();
    private final Set<FrameListener> myFrameListeners = new HashSet<FrameListener>();

    private final CameraAccessor myCameraAccessor = new CameraAccessor()
    {

        public Camera getCamera()
        {
            return Canvas3D.this.getCamera();
        }

    };

    private Spatial my3DNode = null;
    private Component myView3D = null;
    private Canvas myCanvas = null;
    private MyCanvasRenderer myCanvasRenderer = null;
    private float myViewDistance;

    //======================================================================
    // Private Constants

    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;

    private static final int CANVAS_REPAINT_INTERVAL_MS = 10;
    private static final int DEFAULT_VIEW_DISTANCE = 100000;

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // Constructors

    /**
     * Creates a new empty 3D canvas.
     * <p/>
     * Use set3DNode to set a 3D object to show.
     */
    public Canvas3D()
    {
        this( null );
    }


    /**
     * Creates a new 3D canvas, showing the specified 3D node.
     *
     * @param a3dNode the 3D node to show on this canvas, or null not to show any node.
     */
    public Canvas3D( final Spatial a3dNode )
    {
        this( a3dNode, DEFAULT_VIEW_DISTANCE );
    }


    public Canvas3D( final Spatial a3DNode, final float viewDistance )
    {
        my3DNode = a3DNode;

        setViewDistance( viewDistance );

        // Add default navigation gestures
        addNavigationGesture( new PanGesture() );
        addNavigationGesture( new RotateGesture() );
        addNavigationGesture( new MoveGesture() );
    }

    //----------------------------------------------------------------------
    // Other Public Methods

    /**
     * TODO: CHECK: Are the units meters?
     *
     * @return distance in screen units to the far clipping plane - 3D geometry beyond this distance is not shown.
     */
    public float getViewDistance()
    {
        return myViewDistance;
    }


    /**
     * TODO: CHECK: Are the units meters?
     *
     * @param viewDistance distance in screen units to the far clipping plane - 3D geometry beyond this distance is not shown.
     */
    public void setViewDistance( final float viewDistance )
    {
        ParameterChecker.checkPositiveNonZeroNormalNumber( viewDistance, "viewDistance" );

        myViewDistance = viewDistance;
    }


    /**
     * @return the 3D node currently shown, or null if none shown.
     */
    public Spatial get3DNode()
    {
        return my3DNode;
    }


    /**
     * @return the camera associated wit this 3D canvas, if it has been created, otherwise null.
     */
    public Camera getCamera()
    {
        if ( myCanvasRenderer != null )
        {
            return myCanvasRenderer.getCamera();
        }
        else
        {
            return null;
        }
    }


    /**
     * Adds the specified FrameListener.  The listener is called after each frame is rendered in the swing thread.
     *
     * @param addedFrameListener should not be null or already added.
     */
    public void addFrameListener( FrameListener addedFrameListener )
    {
        ParameterChecker.checkNotNull( addedFrameListener, "addedFrameListener" );
        ParameterChecker.checkNotAlreadyContained( addedFrameListener,
                                                   myFrameListeners,
                                                   "myFrameListeners" );

        myFrameListeners.add( addedFrameListener );
    }


    /**
     * Removes the specified FrameListener.
     *
     * @param removedFrameListener should not be null.
     *
     * @return true if the listener was found and removed, false if it was not found.
     */
    public boolean removeFrameListener( FrameListener removedFrameListener )
    {
        ParameterChecker.checkNotNull( removedFrameListener, "removedFrameListener" );

        return myFrameListeners.remove( removedFrameListener );
    }


    /**
     * @return the number of frames rendered per second,
     *         or a negative value if the canvas has not yet been rendered.
     */
    public double getFramesPerSecond()
    {
        return myCanvasRenderer.getFramesPerSecond();
    }


    /**
     * @return number of seconds between the previous frame and the frame before that,
     *         or a negative value if the canvas has not yet been rendered.
     */
    public double getSecondsBetweenFrames()
    {
        return myCanvasRenderer.getSecondsBetweenFrames();
    }


    /**
     * @param a3dNode the 3d node to show in this 3D canvas.
     */
    public void set3DNode( final Spatial a3dNode )
    {
        my3DNode = a3dNode;

        if ( myCanvasRenderer != null )
        {
            myCanvasRenderer.setCanvasRootNode( a3dNode );
        }
    }


    /**
     * @return an AWT component containing a view of the 3D node.
     */
    public Component get3DView()
    {
        if ( myView3D == null )
        {
            myView3D = createView3D();
        }

        return myView3D;
    }


    /**
     * @param addedNavigationGesture some gesture that can be used to control the camera.
     */
    public void addNavigationGesture( NavigationGesture addedNavigationGesture )
    {
        ParameterChecker.checkNotNull( addedNavigationGesture, "addedNavigationGesture" );
        ParameterChecker.checkNotAlreadyContained( addedNavigationGesture,
                                                   myNavigationGestures,
                                                   "myNavigationGestures" );

        myNavigationGestures.add( addedNavigationGesture );

        registerNavigationGestureListener( addedNavigationGesture );
    }


    /**
     * @param removedNavigationGesture gesture to remove.
     */
    public void removeNavigationGesture( NavigationGesture removedNavigationGesture )
    {
        ParameterChecker.checkNotNull( removedNavigationGesture, "removedNavigationGesture" );
        ParameterChecker.checkContained( removedNavigationGesture,
                                         myNavigationGestures,
                                         "myNavigationGestures" );

        myNavigationGestures.remove( removedNavigationGesture );

        unRegisterNavigationGestureListener( removedNavigationGesture );
    }


    /**
     * Removes all registered navigation gestures, including the builtin ones.
     */
    public void removeAllNavigationGestures()
    {
        myNavigationGestures.clear();

        for ( NavigationGesture navigationGesture : myNavigationGestures )
        {
            unRegisterNavigationGestureListener( navigationGesture );
        }
    }

    //======================================================================
    // Private Methods

    private void registerNavigationGestureListener( final NavigationGesture navigationGesture )
    {
        if ( myCanvas != null )
        {
            navigationGesture.init( myCanvas, myCursorChanger, myCameraAccessor );
        }
    }


    private void unRegisterNavigationGestureListener( final NavigationGesture navigationGesture )
    {
        if ( myCanvas != null )
        {
            navigationGesture.deInit();
        }
    }


    private Component createView3D()
    {
        final int width = DEFAULT_WIDTH;
        final int height = DEFAULT_HEIGHT;

        // Create the 3D canvas
        myCanvas = DisplaySystem.getDisplaySystem( "lwjgl" ).createCanvas( width, height );
        myCanvas.setMinimumSize( new Dimension( 0, 0 ) ); // Make sure it is shrinkable
        myCursorChanger.setComponent( myCanvas );
        final JMECanvas jmeCanvas = ( (JMECanvas) myCanvas );

        // Set the renderer that renders the canvas contents
        myCanvasRenderer = new MyCanvasRenderer( width, height, my3DNode, myCanvas );
        jmeCanvas.setImplementor( myCanvasRenderer );

        // Add navigation gesture listeners to the created 3D canvas
        for ( NavigationGesture navigationGesture : myNavigationGestures )
        {
            registerNavigationGestureListener( navigationGesture );
        }

        // We need to repaint the component to see the updates, so we create a repaint calling thread
        final Thread repaintThread = new Thread( new MyRepainter( myCanvas ) );
        repaintThread.setDaemon( true ); // Do not keep the JVM alive if only the repaint thread is left running
        repaintThread.start();

        return myCanvas;
    }

    //======================================================================
    // Inner Classes

    /**
     * A thread for repainting a swing canvas regularily to make it update the 3D view.
     */
    private static final class MyRepainter
            implements Runnable
    {

        //======================================================================
        // Private Fields

        private final Canvas myCanvas;

        //======================================================================
        // Public Methods

        //----------------------------------------------------------------------
        // Constructors

        public MyRepainter( final Canvas canvas )
        {
            myCanvas = canvas;
        }

        //----------------------------------------------------------------------
        // Runnable Implementation

        public void run()
        {
            while ( true )
            {
                myCanvas.repaint();

                // TODO: Instead of sleeping a fixed amount, we could try to sleep some amount to maintain some maximum FPS.
                try
                {
                    Thread.sleep( CANVAS_REPAINT_INTERVAL_MS );
                }
                catch ( InterruptedException e )
                {
                    // Ignore
                }
            }
        }

    }

    /**
     * A renderer that renders a 3D object in a 3D Canvas.
     */
    private final class MyCanvasRenderer
            extends SimpleCanvasImpl
    {

        //======================================================================
        // Private Fields

        private final Canvas myCanvas;

        private final Runnable myFrameListenerUpdater = new Runnable()
        {

            public void run()
            {
                final double secondsSinceLastFrame = myFpsCounter.getSecondsBetweenFrames();
                for ( FrameListener frameListener : myFrameListeners )
                {
                    frameListener.onFrame( secondsSinceLastFrame );
                }
            }

        };

        private Spatial myCanvasRootNode;
        private boolean myAspectRatioNeedsCorrecting = true;

        //======================================================================
        // Private Constants

        private static final float DEFAULT_FIELD_OF_VIEW_DEGREES = 45;

        //======================================================================
        // Public Methods

        //----------------------------------------------------------------------
        // Constructors

        /**
         * Creates a new renderer that renders the specified spatial in a 3D canvas.
         *
         * @param width          initial size of the canvas.  Should be larger than 0.
         * @param height         initial size of the canvas.  Should be larger than 0.
         * @param canvasRootNode the 3D object to render.
         *                       May be null, in which case nothing is rendered (black area)
         * @param canvas         the canvas we are rendering to.  Needed for listening to resize events.
         */
        public MyCanvasRenderer( final int width,
                                 final int height,
                                 final Spatial canvasRootNode,
                                 final Canvas canvas )
        {
            super( width, height );

            ParameterChecker.checkPositiveNonZeroInteger( width, "width" );
            ParameterChecker.checkPositiveNonZeroInteger( height, "height" );
            ParameterChecker.checkNotNull( canvas, "canvas" );

            myCanvasRootNode = canvasRootNode;
            myCanvas = canvas;

            // When the component is resized, adjust the size of the 3D viewport too.
            myCanvas.addComponentListener( new ComponentAdapter()
            {

                public void componentResized( ComponentEvent ce )
                {
                    resizeCanvas( myCanvas.getWidth(), myCanvas.getHeight() );
                    myAspectRatioNeedsCorrecting = true;
                }

            } );
        }

        //----------------------------------------------------------------------
        // Other Public Methods

        /**
         * @return the number of frames rendered per second,
         *         or a negative value if the canvas has not yet been rendered.
         */
        public double getFramesPerSecond()
        {
            return myFpsCounter.getFramesPerSecond();
        }


        /**
         * @return number of seconds between the previous frame and the frame before that,
         *         or a negative value if the canvas has not yet been rendered.
         */
        public double getSecondsBetweenFrames()
        {
            return myFpsCounter.getSecondsBetweenFrames();
        }


        /**
         * @param canvasRootNode the spatial to render with this CanvasRenderer.
         *                       May be null, in which case nothing is rendered (black area)
         */
        public void setCanvasRootNode( final Spatial canvasRootNode )
        {
            if ( rootNode != null && myCanvasRootNode != null )
            {
                rootNode.detachChild( myCanvasRootNode );
            }

            myCanvasRootNode = canvasRootNode;

            if ( rootNode != null && myCanvasRootNode != null )
            {
                rootNode.attachChild( myCanvasRootNode );
            }
        }


        @Override
        public void simpleSetup()
        {
            // Remove the back faces when rendering
            // REFACTOR: Actually the terrain is backwards at the moment, the camera is 'under' it.  Flip it around at some point.
            final CullState cullState = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
            cullState.setCullMode( CullState.CS_FRONT );
            rootNode.setRenderState( cullState );


            if ( myCanvasRootNode != null )
            {
                rootNode.attachChild( myCanvasRootNode );
            }

            getCamera().setFrustumFar( myViewDistance );
        }


        @Override
        public void simpleUpdate()
        {
            myFpsCounter.onFrame();

            if ( !myFrameListeners.isEmpty() )
            {
                SwingUtilities.invokeLater( myFrameListenerUpdater );
            }
        }


        public void simpleRender()
        {
            // Setup aspect ratio for camera on the first frame (the camera is not created before the rendering starts)
            if ( myAspectRatioNeedsCorrecting )
            {
                correctCameraAspectRatio();

                myAspectRatioNeedsCorrecting = false;
            }
        }

        //======================================================================
        // Private Methods

        /**
         * Sets the aspect ratio of the camera to the aspect ratio of the viewport size.
         */
        private void correctCameraAspectRatio()
        {
            final Renderer renderer = getRenderer();

            if ( renderer != null )
            {
                // Get size on screen
                final float height = renderer.getHeight();
                final float width = renderer.getWidth();

                // Calculate aspect ratio
                float aspectRatio = 1;
                if ( height > 0 )
                {
                    aspectRatio = width / height;
                }

                // Set aspect ratio and field of view to camera
                final Camera camera = getCamera();
                camera.setFrustumPerspective( DEFAULT_FIELD_OF_VIEW_DEGREES,
                                              aspectRatio,
                                              camera.getFrustumNear(),
                                              camera.getFrustumFar() );
            }
        }

    }

}
