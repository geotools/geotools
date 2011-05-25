/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.map.event.MapBoundsEvent;
import org.geotools.map.event.MapBoundsListener;
import org.geotools.map.event.MapLayerEvent;
import org.geotools.map.event.MapLayerListEvent;
import org.geotools.map.event.MapLayerListListener;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.label.LabelCacheImpl;
import org.geotools.renderer.lite.LabelCache;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.swing.event.MapMouseListener;
import org.geotools.swing.event.MapPaneEvent;
import org.geotools.swing.event.MapPaneListener;
import org.geotools.swing.tool.CursorTool;
import org.geotools.swing.tool.MapToolManager;
import org.opengis.geometry.Envelope;

/**
 * A map display pane that works with a GTRenderer and
 * MapContext to display features. It supports the use of tool classes
 * to implement, for example, mouse-controlled zooming and panning.
 * <p>
 * Rendering is performed on a background thread and is managed by
 * the {@linkplain RenderingExecutor} class.
 *
 * @see JMapFrame
 * @see MapPaneListener
 * @see CursorTool
 *
 * @author Michael Bedward
 * @author Ian Turton
 * @since 2.6
 *
 * @source $URL$
 * @version $Id$
 */
public class JMapPane extends JPanel implements MapLayerListListener, MapBoundsListener {

    private static final ResourceBundle stringRes = ResourceBundle.getBundle("org/geotools/swing/Text");

    /**
     * Default delay (milliseconds) before the map will be redrawn when resizing
     * the pane. This is to avoid flickering while drag-resizing.
     */
    public static final int DEFAULT_RESIZING_PAINT_DELAY = 500;  // delay in milliseconds

    private ScheduledExecutorService paneTaskExecutor = Executors.newSingleThreadScheduledExecutor();
    private Future<?> resizedFuture;
    private Future<?> imageMovedFuture;

    private int resizingPaintDelay;
    private boolean acceptRepaintRequests;

    /*
     * If the user sets the display area before the pane is shown on
     * screen we store the requested envelope with this field and refer
     * to it when the pane is shown.
     */
    private ReferencedEnvelope pendingDisplayArea;

    /* Current display area */
    private ReferencedEnvelope currentDisplayArea;

    /*
     * This field is used to cache the full extent of the combined map
     * layers.
     */
    private ReferencedEnvelope fullExtent;

    /**
     * Encapsulates XOR box drawing logic used with mouse dragging
     */
    private class DragBox extends MouseInputAdapter {

        private Point startPos;
        private Rectangle rect;
        private boolean dragged;
        private boolean enabled;

        DragBox() {
            rect = new Rectangle();
            dragged = false;
            enabled = false;
        }

        void setEnabled(boolean state) {
            enabled = state;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            startPos = new Point(e.getPoint());
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (enabled) {
                Graphics2D g2D = (Graphics2D) JMapPane.this.getGraphics();
                g2D.setColor(Color.WHITE);
                g2D.setXORMode(Color.RED);
                if (dragged) {
                    g2D.drawRect(rect.x, rect.y, rect.width, rect.height);
                }

                rect.setFrameFromDiagonal(startPos, e.getPoint());
                g2D.drawRect(rect.x, rect.y, rect.width, rect.height);

                dragged = true;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (dragged) {
                Graphics2D g2D = (Graphics2D) JMapPane.this.getGraphics();
                g2D.setColor(Color.WHITE);
                g2D.setXORMode(Color.RED);
                g2D.drawRect(rect.x, rect.y, rect.width, rect.height);
                dragged = false;
            }
        }
    }

    private DragBox dragBox;
    private MapContext context;
    private GTRenderer renderer;
    private LabelCache labelCache;
    private RenderingExecutor renderingExecutor;
    private MapToolManager toolManager;
    private MapLayerTable layerTable;
    private Set<MapPaneListener> listeners = new HashSet<MapPaneListener>();
    private AffineTransform worldToScreen;
    private AffineTransform screenToWorld;
    private Rectangle currentPaintArea;
    private BufferedImage baseImage;
    private Graphics2D baseImageGraphics;
    private Point imageOrigin;

    private AtomicBoolean baseImageMoved;
    private AtomicBoolean clearLabelCache;


    /**
     * Constructor - creates an instance of JMapPane with no map
     * context or renderer initially
     */
    public JMapPane() {
        this(null, null);
    }

    /**
     * Constructor - creates an instance of JMapPane with the given
     * renderer and map context.
     *
     * @param renderer a renderer object
     * @param context an instance of MapContext
     */
    public JMapPane(GTRenderer renderer, MapContext context) {
        imageOrigin = new Point(0, 0);

        acceptRepaintRequests = true;
        baseImageMoved = new AtomicBoolean();
        clearLabelCache = new AtomicBoolean();

        /*
         * We use a Timer object to avoid rendering delays and
         * flickering when the user is drag-resizing the parent
         * container of this map pane.
         *
         * Using a ComponentListener doesn't work because, unlike
         * a JFrame, the pane receives a stream of events during
         * drag-resizing.
         */
        resizingPaintDelay = DEFAULT_RESIZING_PAINT_DELAY;

        doSetRenderer(renderer);
        doSetMapContext(context);

        renderingExecutor = new RenderingExecutor(this);

        toolManager = new MapToolManager(this);

        dragBox = new DragBox();
        this.addMouseListener(dragBox);
        this.addMouseMotionListener(dragBox);

        this.addMouseListener(toolManager);
        this.addMouseMotionListener(toolManager);
        this.addMouseWheelListener(toolManager);

        /*
         * Listen for mouse entered events to (re-)set the
         * current tool cursor, otherwise the cursor seems to
         * default to the standard cursor sometimes (at least
         * on OSX)
         */
        this.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                CursorTool tool = toolManager.getCursorTool();
                if (tool != null) {
                    JMapPane.this.setCursor(tool.getCursor());
                }
            }
        });


        /*
         * Note: we listen for both resizing events (with HierarchyBoundsListener) 
         * and showing events (with HierarchyListener). Although showing
         * is often accompanied by resizing this is not reliable in Swing.
         */
        addHierarchyListener(new HierarchyListener() {
            public void hierarchyChanged(HierarchyEvent he) {
                if ((he.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
                    if (isShowing() && baseImage == null) {
                        onShownOrResized();
                        //repaint();
                    }
                }
            }
        });

        addHierarchyBoundsListener(new HierarchyBoundsAdapter() {
            @Override
            public void ancestorResized(HierarchyEvent he) {
                if (isShowing()) {
                    onShownOrResized();
                }
            }
        });
    }

    private void onShownOrResized() {
        if (resizedFuture != null && !resizedFuture.isDone()) {
            resizedFuture.cancel(true);
        }
        
        resizedFuture = paneTaskExecutor.schedule(new Runnable() {
            public void run() {
                setForNewSize();
            }
        }, resizingPaintDelay, TimeUnit.MILLISECONDS);
    }

    private void setForNewSize() {
        currentPaintArea = getVisibleRect();

        // allow a single pixel margin at the right and bottom edges
        currentPaintArea.width -= 1;
        currentPaintArea.height -= 1;

        if (context != null && context.getLayerCount() > 0) {
            if (fullExtent == null) {
                setFullExtent();
            }

            if (pendingDisplayArea != null) {
                doSetDisplayArea(pendingDisplayArea);
                pendingDisplayArea = null;

            } else {
                doSetDisplayArea(fullExtent);
            }

            acceptRepaintRequests = true;
            drawBaseImage(true);

            MapPaneEvent ev = new MapPaneEvent(this, MapPaneEvent.Type.PANE_RESIZED);
            publishEvent(ev);
        }
    }

    /**
     * Set the current cursor tool
     *
     * @param tool the tool to set; null means no active cursor tool
     */
    public void setCursorTool(CursorTool tool) {
        if (tool == null) {
            toolManager.setNoCursorTool();
            this.setCursor(Cursor.getDefaultCursor());
            dragBox.setEnabled(false);

        } else {
            this.setCursor(tool.getCursor());
            toolManager.setCursorTool(tool);
            dragBox.setEnabled(tool.drawDragBox());
        }
    }

    /**
     * Register an object that wishes to receive {@code MapMouseEvent}s
     * such as a {@linkplain org.geotools.swing.StatusBar}
     *
     * @param listener an object that implements {@code MapMouseListener}
     * @throws IllegalArgumentException if listener is null
     * @see MapMouseListener
     */
    public void addMouseListener(MapMouseListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException(stringRes.getString("arg_null_error"));
        }

        toolManager.addMouseListener(listener);
    }

    /**
     * Unregister the {@code MapMouseListener} object.
     *
     * @param listener the listener to remove
     * @throws IllegalArgumentException if listener is null
     */
    public void removeMouseListener(MapMouseListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException(stringRes.getString("arg_null_error"));
        }

        toolManager.removeMouseListener(listener);
    }

    /**
     * Register an object that wishes to receive {@code MapPaneEvent}s
     *
     * @param listener an object that implements {@code MapPaneListener}
     * @see MapPaneListener
     */
    public void addMapPaneListener(MapPaneListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException(stringRes.getString("arg_null_error"));
        }

        listeners.add(listener);
    }

    /**
     * Register a {@linkplain MapLayerTable} object to be receive
     * layer change events from this map pane and to control layer
     * ordering, visibility and selection.
     *
     * @param layerTable an instance of MapLayerTable
     *
     * @throws IllegalArgumentException if layerTable is null
     */
    public void setMapLayerTable(MapLayerTable layerTable) {
        if (layerTable == null) {
            throw new IllegalArgumentException(stringRes.getString("arg_null_error"));
        }

        this.layerTable = layerTable;
    }

    /**
     * Get the renderer being used by this map pane
     *
     * @return live reference to the renderer being used
     */
    public GTRenderer getRenderer() {
        return renderer;
    }

    /**
     * Set the renderer for this map pane.
     *
     * @param renderer the renderer to use
     */
    public void setRenderer(GTRenderer renderer) {
        doSetRenderer(renderer);
    }

    private void doSetRenderer(GTRenderer renderer) {
        if (renderer != null) {
            Map<Object, Object> hints;
            if (renderer instanceof StreamingRenderer) {
                hints = renderer.getRendererHints();
                if (hints == null) {
                    hints = new HashMap<Object, Object>();
                }
                if (hints.containsKey(StreamingRenderer.LABEL_CACHE_KEY)) {
                    labelCache = (LabelCache) hints.get(StreamingRenderer.LABEL_CACHE_KEY);
                } else {
                    labelCache = new LabelCacheImpl();
                    hints.put(StreamingRenderer.LABEL_CACHE_KEY, labelCache);
                }
                renderer.setRendererHints(hints);

                if (this.context != null) {
                    renderer.setContext(this.context);
                }

            }
        }

        this.renderer = renderer;
    }

    /**
     * Get the map context associated with this map pane
     * @return a live reference to the current map context
     */
    public MapContext getMapContext() {
        return context;
    }

    /**
     * Set the map context for this map pane to display
     * @param context the map context
     */
    public void setMapContext(MapContext context) {
        doSetMapContext(context);
    }
    
    private void doSetMapContext(MapContext context) {
        if (this.context != context) {

            if (this.context != null) {
                this.context.removeMapLayerListListener(this);
                for( MapLayer layer : this.context.getLayers() ){
                    if( layer instanceof ComponentListener){
                        removeComponentListener( (ComponentListener) layer );
                    }
                }
            }

            this.context = context;

            if (context != null) {
                this.context.addMapLayerListListener(this);
                this.context.addMapBoundsListener(this);

                // set all layers as selected by default for the info tool
                for (MapLayer layer : context.getLayers()) {
                    layer.setSelected(true);
                    if( layer instanceof ComponentListener){
                        addComponentListener( (ComponentListener) layer );
                    }
                }

                setFullExtent();
            }

            if (renderer != null) {
                renderer.setContext(this.context);
            }

            MapPaneEvent ev = new MapPaneEvent(this, MapPaneEvent.Type.NEW_CONTEXT);
            publishEvent(ev);
        }
    }

    /**
     * Return a (copy of) the currently displayed map area.
     * <p>
     * Note, this will not always be the same as the envelope returned by
     * {@code MapContext.getAreaOfInterest()}. For example, when the
     * map is displayed at the full extent of all layers
     * {@code MapContext.getAreaOfInterest()} will return the union of the
     * layer bounds while this method will return an evnelope that can
     * included extra space beyond the bounds of the layers.
     *
     * @return the display area in world coordinates as a new {@code ReferencedEnvelope}
     */
    public ReferencedEnvelope getDisplayArea() {
        return new ReferencedEnvelope(currentDisplayArea);
    }

    private void calculateDisplayArea() {
        if (currentPaintArea != null && screenToWorld != null) {
            Point2D p0 = new Point2D.Double(currentPaintArea.getMinX(), currentPaintArea.getMinY());
            Point2D p1 = new Point2D.Double(currentPaintArea.getMaxX(), currentPaintArea.getMaxY());
            screenToWorld.transform(p0, p0);
            screenToWorld.transform(p1, p1);

            currentDisplayArea = new ReferencedEnvelope(
                    Math.min(p0.getX(), p1.getX()),
                    Math.max(p0.getX(), p1.getX()),
                    Math.min(p0.getY(), p1.getY()),
                    Math.max(p0.getY(), p1.getY()),
                    context.getCoordinateReferenceSystem());
        }
    }

    /**
     * Sets the area to display. Does nothing if the MapContext has not been set.
     * <p>
     * The map area that ends up being displayed will often be larger than the requested
     * display area. For instance, if the square area is requested, but the map pane's
     * screen area is a rectangle with width greater than height, then the displayed area
     * will be centred on the requested square but include additional area on each side.
     * <p>
     * You can pass any GeoAPI Envelope implementation to this method such as
     * ReferenedEnvelope or Envelope2D.
     * <p>
     * Note: This method does <b>not</b> check that the requested area overlaps
     * the bounds of the current map layers.
     *
     * @param envelope the bounds of the map to display
     *
     * @throws IllegalStateException if a map context is not set
     */
    public void setDisplayArea(Envelope envelope) {
        if (context != null) {
            /*
             * If the pane has not been displayed yet or is zero size then
             * just record the requested display area and defer setting transforms
             * etc.
             */
            if (currentPaintArea == null || currentPaintArea.isEmpty()) {
                pendingDisplayArea = new ReferencedEnvelope(envelope);
            } else {
                doSetDisplayArea(envelope);
                clearLabelCache.set(true);
                drawBaseImage(false);
            }

        } else {
            throw new IllegalStateException("Map context must be set before setting the display area");
        }
    }

    /**
     * Helper method for {@linkplain #setDisplayArea} which is also called by
     * other methods that want to set the display area without provoking
     * repainting of the display
     *
     * @param envelope requested display area
     */
    private void doSetDisplayArea(Envelope envelope) {
        assert (context != null && currentPaintArea != null && !currentPaintArea.isEmpty());

        if (equalsFullExtent(envelope)) {
            setTransforms(fullExtent, currentPaintArea);
        } else {
            setTransforms(envelope, currentPaintArea);
        }

        calculateDisplayArea();
        MapPaneEvent ev = new MapPaneEvent(this, MapPaneEvent.Type.DISPLAY_AREA_CHANGED);
        publishEvent(ev);
    }


    /**
     * Check if the envelope corresponds to full extent. It will probably not
     * equal the full extent envelope because of slack space in the display
     * area, so we check that at least one pair of opposite edges are
     * equal to the full extent envelope, allowing for slack space on the
     * other two sides.
     * <p>
     * Note: this method returns {@code false} if the full extent envelope
     * is wholly within the requested envelope (e.g. user has zoomed out
     * from full extent), only touches one edge, or touches two adjacent edges.
     * In all these cases we assume that the user wants to maintain the slack
     * space in the display.
     * <p>
     * This method is part of the work-around that the map pane needs because
     * of the differences in how raster and vector layers are treated by the
     * renderer classes.
     *
     * @param envelope a pending display envelope to compare to the full extent
     *        envelope
     *
     * @return true if the envelope is coincident with the full extent evenlope
     *         on at least two edges; false otherwise
     *
     * @todo My logic here seems overly complex - I'm sure there must be a simpler
     *       way for the map pane to handle this.
     */
    private boolean equalsFullExtent(final Envelope envelope) {
        if (fullExtent == null || envelope == null) {
            return false;
        }

        final double TOL = 1.0e-6d * (fullExtent.getWidth() + fullExtent.getHeight());

        boolean touch = false;
        if (Math.abs(envelope.getMinimum(0) - fullExtent.getMinimum(0)) < TOL) {
            touch = true;
        }
        if (Math.abs(envelope.getMaximum(0) - fullExtent.getMaximum(0)) < TOL) {
            if (touch) {
                return true;
            }
        }
        if (Math.abs(envelope.getMinimum(1) - fullExtent.getMinimum(1)) < TOL) {
            touch = true;
        }
        if (Math.abs(envelope.getMaximum(1) - fullExtent.getMaximum(1)) < TOL) {
            if (touch) {
                return true;
            }
        }

        return false;
    }

    /**
     * Reset the map area to include the full extent of all
     * layers and redraw the display
     */
    public void reset() {
        if (fullExtent != null) {
            setDisplayArea(fullExtent);
        }
    }

    /**
     * Specify whether the map pane should defer its normal
     * repainting behaviour.
     * <p>
     * Typical use:
     * <pre>{@code
     * myMapPane.setRepaint(false);
     *
     * // do various things that would cause time-consuming
     * // re-paints normally
     *
     * myMapPane.setRepaint(true);
     * myMapPane.repaint();
     * }</pre>
     *
     * @param repaint if true, paint requests will be handled normally;
     * if false, paint requests will be deferred.
     *
     * @see #isAcceptingRepaints()
     */
    public void setRepaint(boolean repaint) {
        acceptRepaintRequests = repaint;

        // we also want to accept / ignore system requests for repainting
        setIgnoreRepaint(!repaint);
    }

    /**
     * Query whether the map pane is currently accepting or ignoring
     * repaint requests from other GUI components and the system.
     *
     * @return true if the pane is currently accepting repaint requests;
     *         false if it is ignoring them
     *
     * @see #setRepaint(boolean)
     */
    public boolean isAcceptingRepaints() {
        return acceptRepaintRequests;
    }

    /**
     * Retrieve the map pane's current base image.
     * <p>
     * The map pane caches the most recent rendering of map layers
     * as an image to avoid time-consuming rendering requests whenever
     * possible. The base image will be re-drawn whenever there is a
     * change to map layer data, style or visibility; and it will be
     * replaced by a new image when the pane is resized.
     * <p>
     * This method returns a <b>live</b> reference to the current
     * base image. Use with caution.
     *
     * @return a live reference to the current base image
     */
    public RenderedImage getBaseImage() {
        return this.baseImage;
    }

    /**
     * Get the length of the delay period between the pane being
     * resized and the next repaint.
     * <p>
     * The map pane imposes a delay between resize events and repainting
     * to avoid flickering of the display during drag-resizing.
     *
     * @return delay in milliseconds
     */
    public int getResizeDelay() {
        return resizingPaintDelay;
    }

    /**
     * Set the length of the delay period between the pane being
     * resized and the next repaint.
     * <p>
     * The map pane imposes a delay between resize events and repainting
     * to avoid flickering of the display during drag-resizing.
     *
     * @param delay the delay in milliseconds; if {@code <} 0 the default delay
     *        period will be set
     */
    public void setResizeDelay(int delay) {
        if (delay < 0) {
            resizingPaintDelay = DEFAULT_RESIZING_PAINT_DELAY;
        } else {
            resizingPaintDelay = delay;
        }
    }

    /**
     * Get a (copy of) the screen to world coordinate transform
     * being used by this map pane.
     *
     * @return a copy of the screen to world coordinate transform
     */
    public AffineTransform getScreenToWorldTransform() {
        if (screenToWorld != null) {
            return new AffineTransform(screenToWorld);
        } else {
            return null;
        }
    }

    /**
     * Get a (copy of) the world to screen coordinate transform
     * being used by this map pane. This method can be
     * used to determine the current drawing scale...
     * <pre>{@code
     * double scale = mapPane.getWorldToScreenTransform().getScaleX();
     * }</pre>
     * @return a copy of the world to screen coordinate transform
     */
    public AffineTransform getWorldToScreenTransform() {
        if (worldToScreen != null) {
            return new AffineTransform(worldToScreen);
        } else {
            return null;
        }
    }

    /**
     * Move the image currently displayed by the map pane from
     * its current origin (x,y) to (x+dx, y+dy). This method
     * allows dragging the map without the overhead of redrawing
     * the features during the drag. For example, it is used by
     * {@link org.geotools.swing.tool.PanTool}.
     *
     * @param dx the x offset in pixels
     * @param dy the y offset in pixels.
     */
    public void moveImage(int dx, int dy) {
        imageOrigin.translate(dx, dy);
        baseImageMoved.set(true);
        repaint();
        onImageMoved();
    }

    private void onImageMoved() {
        if (imageMovedFuture != null && !imageMovedFuture.isDone()) {
            imageMovedFuture.cancel(true);
        }

        imageMovedFuture = paneTaskExecutor.schedule(new Runnable() {
            public void run() {
                afterImageMoved();
                clearLabelCache.set(true);
                drawBaseImage(false);
                repaint();
            }
        }, resizingPaintDelay, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (baseImage != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(baseImage, imageOrigin.x, imageOrigin.y, null);
            return;
        }
    }

    private void drawBaseImage(boolean createNewImage) {
        if (currentPaintArea == null) {
            return;
        }
        
        if (acceptRepaintRequests) {
            if (createNewImage) {
                baseImage = new BufferedImage(
                        currentPaintArea.width + 1, currentPaintArea.height + 1,
                        BufferedImage.TYPE_INT_ARGB);

                if (baseImageGraphics != null) {
                    baseImageGraphics.dispose();
                }
                
                baseImageGraphics = baseImage.createGraphics();
                clearLabelCache.set(true);
            }

            if (renderingExecutor.submit(currentDisplayArea, currentPaintArea, baseImageGraphics)) {
                MapPaneEvent ev = new MapPaneEvent(this, MapPaneEvent.Type.RENDERING_STARTED);
                publishEvent(ev);
                
            } else {
                onRenderingRejected();
            }
        }

        return;
    }

    /**
     * Called by the {@linkplain JMapPane.RenderingTask} when rendering has been completed
     * Publishes a {@linkplain MapPaneEvent} of type
     * {@code MapPaneEvent.Type.RENDERING_STOPPED} to listeners.
     *
     * @see MapPaneListener#onRenderingStopped(org.geotools.swing.event.MapPaneEvent)
     */
    public void onRenderingCompleted() {
        if (clearLabelCache.get()) {
            labelCache.clear();
        }

        clearLabelCache.set(false);
        repaint();

        MapPaneEvent ev = new MapPaneEvent(this, MapPaneEvent.Type.RENDERING_STOPPED);
        publishEvent(ev);
    }

    /**
     * Called by the {@linkplain JMapPane.RenderingTask} when rendering was cancelled.
     * Publishes a {@linkplain MapPaneEvent} of type
     * {@code MapPaneEvent.Type.RENDERING_STOPPED} to listeners.
     *
     * @see MapPaneListener#onRenderingStopped(org.geotools.swing.event.MapPaneEvent)
     */
    public void onRenderingCancelled() {
        MapPaneEvent ev = new MapPaneEvent(this, MapPaneEvent.Type.RENDERING_STOPPED);
        publishEvent(ev);
    }

    /**
     * Called by the {@linkplain JMapPane.RenderingTask} when rendering failed.
     * Publishes a {@linkplain MapPaneEvent} of type
     * {@code MapPaneEvent.Type.RENDERING_STOPPED} to listeners.
     *
     * @see MapPaneListener#onRenderingStopped(org.geotools.swing.event.MapPaneEvent)
     */
    public void onRenderingFailed() {
        MapPaneEvent ev = new MapPaneEvent(this, MapPaneEvent.Type.RENDERING_STOPPED);
        publishEvent(ev);
    }

    /**
     * Called when a rendering request has been rejected. This will be common, such as
     * when the user pauses during drag-resizing of the map pane. The base implementation
     * does nothing. It is provided for sub-classes to override if required.
     */
    public void onRenderingRejected() {
        // do nothing
    }

    /**
     * Called after the base image has been dragged. Sets the new map area and
     * transforms
     */
    protected void afterImageMoved() {
        int dx = imageOrigin.x;
        int dy = imageOrigin.y;
        DirectPosition2D newPos = new DirectPosition2D(dx, dy);
        screenToWorld.transform(newPos, newPos);

        ReferencedEnvelope env = new ReferencedEnvelope(currentDisplayArea);
        env.translate(env.getMinimum(0) - newPos.x, env.getMaximum(1) - newPos.y);
        doSetDisplayArea(env);

        imageOrigin.setLocation(0, 0);
        baseImageMoved.set(false);
    }

    /**
     * Called when a new map layer has been added. Sets the layer
     * as selected (for queries) and, if the layer table is being
     * used, adds the new layer to the table.
     */
    public void layerAdded(MapLayerListEvent event) {
        if (layerTable != null) {
            layerTable.onAddLayer(event.getLayer());
        }
        MapLayer layer = event.getLayer();
        layer.setSelected(true);

        if( layer instanceof ComponentListener ){
            addComponentListener( (ComponentListener) layer );
        }

        boolean atFullExtent = equalsFullExtent(getDisplayArea());
        setFullExtent();
        if (context.getLayerCount() == 1 || atFullExtent) {
            reset();
        }

        drawBaseImage(false);
        repaint();
    }

    /**
     * Called when a map layer has been removed
     */
    public void layerRemoved(MapLayerListEvent event) {
        MapLayer layer = event.getLayer();
        if (layerTable != null) {
            layerTable.onRemoveLayer(layer);
        }

        if( layer instanceof ComponentListener ){
            addComponentListener( (ComponentListener) layer );
        }

        if (context.getLayerCount() == 0) {
            clearFields();
        } else {
            setFullExtent();
        }

        drawBaseImage(false);
        repaint();
    }

    /**
     * Called when a map layer has changed, e.g. features added
     * to a displayed feature collection
     */
    public void layerChanged(MapLayerListEvent event) {
        if (layerTable != null) {
            layerTable.repaint(event.getLayer());
        }

        int reason = event.getMapLayerEvent().getReason();

        if (reason == MapLayerEvent.DATA_CHANGED) {
            setFullExtent();
        }

        if (reason != MapLayerEvent.SELECTION_CHANGED) {
            drawBaseImage(false);
        }

        repaint();
    }

    /**
     * Called when the bounds of a map layer have changed
     */
    public void layerMoved(MapLayerListEvent event) {
        drawBaseImage(false);
        repaint();
    }

    /**
     * Called by the map context when its bounds have changed. Used
     * here to watch for a changed CRS, in which case the map is
     * redisplayed at (new) full extent.
     */
    public void mapBoundsChanged(MapBoundsEvent event) {

        int type = event.getType();
        if ((type & MapBoundsEvent.COORDINATE_SYSTEM_MASK) != 0) {
            /*
             * The coordinate reference system has changed. Set the map
             * to display the full extent of layer bounds to avoid the
             * effect of a shrinking map
             */
            setFullExtent();
            reset();
        }
    }

    /**
     * Gets the full extent of map context's layers. The only reason
     * this method is defined is to avoid having try-catch blocks all
     * through other methods.
     */
    private void setFullExtent() {
        if (context != null && context.getLayerCount() > 0) {
            try {
                fullExtent = context.getLayerBounds();

                /*
                 * Guard agains degenerate envelopes (e.g. empty
                 * map layer or single point feature)
                 */
                if (fullExtent == null ) {
                    // set arbitrary bounds centred on 0,0
                    fullExtent = new ReferencedEnvelope(-1, 1, -1, 1, context.getCoordinateReferenceSystem());

                } else {
                    double w = fullExtent.getWidth();
                    double h = fullExtent.getHeight();
                    double x = fullExtent.getMinimum(0);
                    double y = fullExtent.getMinimum(1);

                    double xmin = x;
                    double xmax = x + w;
                    if (w <= 0.0) {
                        xmin = x - 1.0;
                        xmax = x + 1.0;
                    }

                    double ymin = y;
                    double ymax = y + h;
                    if (h <= 0.0) {
                        ymin = y - 1.0;
                        ymax = y + 1.0;
                    }

                    fullExtent = new ReferencedEnvelope(xmin, xmax, ymin, ymax, context.getCoordinateReferenceSystem());
                }

            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        } else {
            fullExtent = null;
        }
    }

    /**
     * Calculate the affine transforms used to convert between
     * world and pixel coordinates. The calculations here are very
     * basic and assume a cartesian reference system.
     * <p>
     * Tne transform is calculated such that {@code envelope} will
     * be centred in the display
     *
     * @param envelope the current map extent (world coordinates)
     * @param paintArea the current map pane extent (screen units)
     */
    private void setTransforms(final Envelope envelope, final Rectangle paintArea) {
        ReferencedEnvelope refEnv = new ReferencedEnvelope(envelope);

        double xscale = paintArea.getWidth() / refEnv.getWidth();
        double yscale = paintArea.getHeight() / refEnv.getHeight();

        double scale = Math.min(xscale, yscale);

        double xoff = refEnv.getMedian(0) * scale - paintArea.getCenterX();
        double yoff = refEnv.getMedian(1) * scale + paintArea.getCenterY();

        worldToScreen = new AffineTransform(scale, 0, 0, -scale, -xoff, yoff);
        try {
            screenToWorld = worldToScreen.createInverse();

        } catch (NoninvertibleTransformException ex) {
            throw new RuntimeException("Unable to create coordinate transforms.", ex);
        }
    }

    /**
     * Publish a MapPaneEvent to registered listeners
     *
     * @param ev the event to publish
     * @see MapPaneListener
     */
    private void publishEvent(MapPaneEvent ev) {
        for (MapPaneListener listener : listeners) {
            switch (ev.getType()) {
                case NEW_CONTEXT:
                    listener.onNewContext(ev);
                    break;

                case NEW_RENDERER:
                    listener.onNewRenderer(ev);
                    break;

                case PANE_RESIZED:
                    listener.onResized(ev);
                    break;

                case DISPLAY_AREA_CHANGED:
                    listener.onDisplayAreaChanged(ev);
                    break;

                case RENDERING_STARTED:
                    listener.onRenderingStarted(ev);
                    break;

                case RENDERING_STOPPED:
                    listener.onRenderingStopped(ev);
                    break;

                case RENDERING_PROGRESS:
                    listener.onRenderingProgress(ev);
                    break;
            }
        }
    }

    /**
     * This method is called if all layers are removed from the context.
     */
    private void clearFields() {
        fullExtent = null;
        worldToScreen = null;
        screenToWorld = null;
    }

}
