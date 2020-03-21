/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;
import org.geotools.map.MapBoundsEvent;
import org.geotools.map.MapBoundsListener;
import org.geotools.map.MapContent;
import org.geotools.map.MapLayerEvent;
import org.geotools.map.MapLayerListEvent;
import org.geotools.map.MapLayerListListener;
import org.geotools.map.MapViewport;
import org.geotools.renderer.lite.LabelCache;
import org.geotools.swing.event.DefaultMapMouseEventDispatcher;
import org.geotools.swing.event.MapMouseEventDispatcher;
import org.geotools.swing.event.MapMouseListener;
import org.geotools.swing.event.MapPaneEvent;
import org.geotools.swing.event.MapPaneKeyHandler;
import org.geotools.swing.event.MapPaneListener;
import org.geotools.swing.tool.CursorTool;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Base class for Swing map panes. It extends Swing's {@code JPanel} class and handles window sizing
 * and repainting as well as redirecting mouse events. It also provides basic implementations of all
 * interface methods. Sub-classes must implement {@linkplain #drawLayers(boolean)} and override
 * {@linkplain JPanel#paintComponent(java.awt.Graphics)}.
 *
 * @author Michael Bedward
 * @since 8.0
 * @version $Id$
 */
public abstract class AbstractMapPane extends JPanel
        implements MapPane, RenderingExecutorListener, MapLayerListListener, MapBoundsListener {

    /**
     * Default delay (500 milliseconds) before the map will be redrawn when resizing the pane or
     * moving the displayed image. This avoids flickering and redundant rendering.
     */
    public static final int DEFAULT_PAINT_DELAY = 500;

    /** Default background color (white). */
    public static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;

    protected final ScheduledExecutorService paneTaskExecutor;
    protected Future<?> resizedFuture;
    protected int paintDelay;
    protected final AtomicBoolean acceptRepaintRequests;

    /* Fields used for map panning */
    protected final AtomicBoolean baseImageMoved;
    protected Future<?> imageMovedFuture;
    protected final Point imageOrigin;

    protected final Lock drawingLock;
    protected final ReadWriteLock paramsLock;

    protected final Set<MapPaneListener> listeners = new HashSet<MapPaneListener>();
    protected final MouseDragBox dragBox;

    /*
     * If the user sets the display area before the pane is shown on
     * screen we store the requested envelope with this field and refer
     * to it when the pane is shown.
     */
    protected ReferencedEnvelope pendingDisplayArea;

    /*
     * This field is used to cache the full extent of the combined map
     * layers.
     */
    protected ReferencedEnvelope fullExtent;

    protected MapContent mapContent;
    protected RenderingExecutor renderingExecutor;
    protected KeyListener keyHandler;
    protected MapMouseEventDispatcher mouseEventDispatcher;

    protected LabelCache labelCache;
    protected AtomicBoolean clearLabelCache;

    protected CursorTool currentCursorTool;

    public AbstractMapPane(MapContent content, RenderingExecutor executor) {
        setBackground(DEFAULT_BACKGROUND_COLOR);
        setFocusable(true);

        drawingLock = new ReentrantLock();
        paramsLock = new ReentrantReadWriteLock();

        paneTaskExecutor = Executors.newSingleThreadScheduledExecutor();
        paintDelay = DEFAULT_PAINT_DELAY;
        acceptRepaintRequests = new AtomicBoolean(true);
        clearLabelCache = new AtomicBoolean(true);
        baseImageMoved = new AtomicBoolean();
        imageOrigin = new Point(0, 0);

        dragBox = new MouseDragBox(this);
        mouseEventDispatcher = new DefaultMapMouseEventDispatcher(this);

        addMouseListener(dragBox);
        addMouseMotionListener(dragBox);

        addMouseListener(mouseEventDispatcher);
        addMouseMotionListener(mouseEventDispatcher);
        addMouseWheelListener(mouseEventDispatcher);

        /*
         * Listen for mouse entered events to (re-)set the
         * current tool cursor, otherwise the cursor seems to
         * default to the standard cursor sometimes (at least
         * on OSX)
         */
        addMouseListener(
                new MouseInputAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e);
                        if (currentCursorTool != null) {
                            setCursor(currentCursorTool.getCursor());
                        }
                    }
                });

        keyHandler = new MapPaneKeyHandler(this);
        addKeyListener(keyHandler);

        /*
         * Note: we listen for both resizing events (with HierarchyBoundsListener)
         * and showing events (with HierarchyListener). Although showing
         * is often accompanied by resizing this is not reliable in Swing.
         */
        addHierarchyListener(
                new HierarchyListener() {
                    @Override
                    public void hierarchyChanged(HierarchyEvent he) {
                        if ((he.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
                            if (isShowing()) {
                                onShownOrResized();
                            }
                        }
                    }
                });

        addHierarchyBoundsListener(
                new HierarchyBoundsAdapter() {
                    @Override
                    public void ancestorResized(HierarchyEvent he) {
                        if (isShowing()) {
                            onShownOrResized();
                        }
                    }
                });

        doSetMapContent(content);
        doSetRenderingExecutor(executor);
    }

    /** Draws layers into one or more images which will then be displayed by the map pane. */
    protected abstract void drawLayers(boolean recreate);

    /**
     * Gets the rendering executor, creating a default one if necessary.
     *
     * @return the rendering executor
     */
    public RenderingExecutor getRenderingExecutor() {
        if (renderingExecutor == null) {
            doSetRenderingExecutor(new DefaultRenderingExecutor());
        }
        return renderingExecutor;
    }

    /** {@inheritDoc} */
    @Override
    public MapMouseEventDispatcher getMouseEventDispatcher() {
        return mouseEventDispatcher;
    }

    /** {@inheritDoc} */
    @Override
    public void setMouseEventDispatcher(MapMouseEventDispatcher dispatcher) {
        if (mouseEventDispatcher != null) {
            mouseEventDispatcher.removeAllListeners();
        }

        mouseEventDispatcher = dispatcher;
    }

    /**
     * Sets the rendering executor. If {@code executor} is {@code null}, the default {@linkplain
     * DefaultRenderingExecutor} will be set on the next call to {@linkplain
     * #getRenderingExecutor()}.
     *
     * @param executor the rendering executor
     */
    public void setRenderingExecutor(RenderingExecutor executor) {
        doSetRenderingExecutor(executor);
    }

    private void doSetRenderingExecutor(RenderingExecutor newExecutor) {
        if (renderingExecutor != null) {
            renderingExecutor.shutdown();
        }

        renderingExecutor = newExecutor;
    }

    /**
     * Gets the current handler for keyboard actions.
     *
     * @return current handler (may be {@code null})
     */
    public KeyListener getKeyHandler() {
        return keyHandler;
    }

    /**
     * Sets a handler for keyboard actions which control the map pane's display. The default handler
     * is {@linkplain MapPaneKeyHandler} which provides for scrolling and zooming.
     *
     * @param controller the new handler or {@code null} to disable key handling
     */
    public void setKeyHandler(KeyListener controller) {
        if (keyHandler != null) {
            removeKeyListener(keyHandler);
        }

        if (controller != null) {
            addKeyListener(controller);
        }

        keyHandler = controller;
    }

    /**
     * Gets the current paint delay interval in milliseconds. The map pane uses this delay period to
     * avoid flickering and redundant rendering when drag-resizing the pane or panning the map
     * image.
     *
     * @return delay in milliseconds
     */
    public long getPaintDelay() {
        paramsLock.readLock().lock();
        try {
            return paintDelay;

        } finally {
            paramsLock.readLock().unlock();
        }
    }

    /**
     * Sets the current paint delay interval in milliseconds. The map pane uses this delay period to
     * avoid flickering and redundant rendering when drag-resizing the pane or panning the map
     * image.
     *
     * @param delay the delay in milliseconds; if {@code <=} 0 the default delay period will be set
     */
    public void setPaintDelay(int delay) {
        paramsLock.writeLock().lock();
        try {
            if (delay < 0) {
                paintDelay = DEFAULT_PAINT_DELAY;
            } else {
                paintDelay = delay;
            }

        } finally {
            paramsLock.writeLock().unlock();
        }
    }

    /**
     * Specify whether the map pane should defer its normal repainting behaviour.
     *
     * <p>Typical use:
     *
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
     * @param ignoreRepaint if false, paint requests will be handled normally; if true, paint
     *     requests will be deferred.
     * @see #isAcceptingRepaints()
     */
    @Override
    public void setIgnoreRepaint(boolean ignoreRepaint) {
        drawingLock.lock();
        try {
            super.setIgnoreRepaint(ignoreRepaint);
            acceptRepaintRequests.set(!ignoreRepaint);

        } finally {
            drawingLock.unlock();
        }
    }

    /**
     * Query whether the map pane is currently accepting or ignoring repaint requests from other GUI
     * components and the system.
     *
     * @return true if the pane is currently accepting repaint requests; false if it is ignoring
     *     them
     * @see #setRepaint(boolean)
     */
    public boolean isAcceptingRepaints() {
        return acceptRepaintRequests.get();
    }

    protected void onShownOrResized() {
        if (resizedFuture != null && !resizedFuture.isDone()) {
            resizedFuture.cancel(true);
        }

        resizedFuture =
                paneTaskExecutor.schedule(
                        new Runnable() {
                            @Override
                            public void run() {
                                setForNewSize();

                                // Call repaint here rather than within setForNewSize so that
                                // drawingLock will be available in paintComponent
                                repaint();
                            }
                        },
                        paintDelay,
                        TimeUnit.MILLISECONDS);
    }

    protected void setForNewSize() {
        drawingLock.lock();
        try {
            if (mapContent != null) {

                /*
                 * Compare the new pane screen size to the viewport's screen area
                 * and skip further action if the two rectangles are equal. This
                 * check avoid extra rendering requests when redundant resize events
                 * are received (e.g. on mouse button release after drag resizing).
                 */
                if (mapContent.getViewport().getScreenArea().equals(getVisibleRect())) {
                    return;
                }

                mapContent.getViewport().setScreenArea(getVisibleRect());

                if (pendingDisplayArea != null) {
                    doSetDisplayArea(pendingDisplayArea);
                    pendingDisplayArea = null;

                } else if (mapContent.getViewport().getBounds().isEmpty()) {
                    setFullExtent();
                    doSetDisplayArea(fullExtent);
                }

                publishEvent(
                        new MapPaneEvent(
                                this, MapPaneEvent.Type.DISPLAY_AREA_CHANGED, getDisplayArea()));

                acceptRepaintRequests.set(true);
                drawLayers(true);
            }

        } finally {
            drawingLock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void moveImage(int dx, int dy) {
        drawingLock.lock();
        try {
            if (isShowing() && !getVisibleRect().isEmpty()) {
                imageOrigin.translate(dx, dy);
                baseImageMoved.set(true);
                repaint();
                onImageMoved();
            }

        } finally {
            drawingLock.unlock();
        }
    }

    protected void onImageMoved() {
        if (imageMovedFuture != null && !imageMovedFuture.isDone()) {
            imageMovedFuture.cancel(true);
        }

        imageMovedFuture =
                paneTaskExecutor.schedule(
                        new Runnable() {
                            @Override
                            public void run() {
                                afterImageMoved();
                                clearLabelCache.set(true);
                                drawLayers(false);
                            }
                        },
                        paintDelay,
                        TimeUnit.MILLISECONDS);
    }

    /** Called after the base image has been dragged. Sets the new map area and transforms */
    protected void afterImageMoved() {
        paramsLock.writeLock().lock();
        try {
            int dx = imageOrigin.x;
            int dy = imageOrigin.y;
            DirectPosition2D newPos = new DirectPosition2D(dx, dy);
            mapContent.getViewport().getScreenToWorld().transform(newPos, newPos);

            ReferencedEnvelope env = new ReferencedEnvelope(mapContent.getViewport().getBounds());
            env.translate(env.getMinimum(0) - newPos.x, env.getMaximum(1) - newPos.y);
            doSetDisplayArea(env);

            imageOrigin.setLocation(0, 0);
            baseImageMoved.set(false);

        } finally {
            paramsLock.writeLock().unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public MapContent getMapContent() {
        paramsLock.readLock().lock();
        try {
            return mapContent;

        } finally {
            paramsLock.readLock().unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setMapContent(MapContent content) {
        paramsLock.writeLock().lock();
        try {
            doSetMapContent(content);

        } finally {
            paramsLock.writeLock().unlock();
        }
    }

    private void doSetMapContent(MapContent newMapContent) {
        if (mapContent != newMapContent) {

            if (mapContent != null) {
                mapContent.removeMapLayerListListener(this);
                for (Layer layer : mapContent.layers()) {
                    if (layer instanceof ComponentListener) {
                        removeComponentListener((ComponentListener) layer);
                    }
                }
            }

            mapContent = newMapContent;

            if (mapContent != null) {
                MapViewport viewport = mapContent.getViewport();
                viewport.setMatchingAspectRatio(true);
                Rectangle rect = getVisibleRect();
                if (!rect.isEmpty()) {
                    viewport.setScreenArea(rect);
                }

                mapContent.addMapLayerListListener(this);
                mapContent.addMapBoundsListener(this);

                if (!mapContent.layers().isEmpty()) {
                    // set all layers as selected by default for the info tool
                    for (Layer layer : mapContent.layers()) {
                        layer.setSelected(true);

                        if (layer instanceof ComponentListener) {
                            addComponentListener((ComponentListener) layer);
                        }
                    }

                    setFullExtent();
                    doSetDisplayArea(mapContent.getViewport().getBounds());
                }
            }

            MapPaneEvent event =
                    new MapPaneEvent(this, MapPaneEvent.Type.NEW_MAPCONTENT, mapContent);
            publishEvent(event);

            drawLayers(false);
        }
    }

    /** {@inheritDoc} */
    @Override
    public ReferencedEnvelope getDisplayArea() {
        paramsLock.readLock().lock();
        try {
            if (mapContent != null) {
                return mapContent.getViewport().getBounds();
            } else if (pendingDisplayArea != null) {
                return new ReferencedEnvelope(pendingDisplayArea);
            } else {
                return new ReferencedEnvelope();
            }

        } finally {
            paramsLock.readLock().unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setDisplayArea(Envelope envelope) {
        paramsLock.writeLock().lock();
        try {
            if (envelope == null) {
                throw new IllegalArgumentException("envelope must not be null");
            }

            doSetDisplayArea(envelope);
            if (mapContent != null) {
                clearLabelCache.set(true);
                drawLayers(false);
            }

        } finally {
            paramsLock.writeLock().unlock();
        }
    }

    /**
     * Helper method for {@linkplain #setDisplayArea} which is also called by other methods that
     * want to set the display area without provoking repainting of the display
     *
     * @param envelope requested display area
     */
    protected void doSetDisplayArea(Envelope envelope) {
        if (mapContent != null) {
            CoordinateReferenceSystem crs = envelope.getCoordinateReferenceSystem();
            if (crs == null) {
                // assume that it is the current CRS
                crs = mapContent.getCoordinateReferenceSystem();
            }

            ReferencedEnvelope refEnv =
                    new ReferencedEnvelope(
                            envelope.getMinimum(0),
                            envelope.getMaximum(0),
                            envelope.getMinimum(1),
                            envelope.getMaximum(1),
                            crs);

            mapContent.getViewport().setBounds(refEnv);

        } else {
            pendingDisplayArea = new ReferencedEnvelope(envelope);
        }

        // Publish the resulting display area with the event
        publishEvent(
                new MapPaneEvent(this, MapPaneEvent.Type.DISPLAY_AREA_CHANGED, getDisplayArea()));
    }

    /** {@inheritDoc} */
    @Override
    public void reset() {
        paramsLock.writeLock().lock();
        try {
            if (fullExtent != null) {
                setDisplayArea(fullExtent);
            }

        } finally {
            paramsLock.writeLock().unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public AffineTransform getScreenToWorldTransform() {
        paramsLock.readLock().lock();
        try {
            if (mapContent != null) {
                return mapContent.getViewport().getScreenToWorld();
            } else {
                return null;
            }

        } finally {
            paramsLock.readLock().unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public AffineTransform getWorldToScreenTransform() {
        paramsLock.readLock().lock();
        try {
            if (mapContent != null) {
                return mapContent.getViewport().getWorldToScreen();
            } else {
                return null;
            }

        } finally {
            paramsLock.readLock().unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void addMapPaneListener(MapPaneListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }

        listeners.add(listener);
    }

    /** {@inheritDoc} */
    @Override
    public void removeMapPaneListener(MapPaneListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void addMouseListener(MapMouseListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }

        mouseEventDispatcher.addMouseListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public void removeMouseListener(MapMouseListener listener) {
        if (listener != null) {
            mouseEventDispatcher.removeMouseListener(listener);
        }
    }

    /** {@inheritDoc} */
    @Override
    public CursorTool getCursorTool() {
        return currentCursorTool;
    }

    /** {@inheritDoc} */
    @Override
    public void setCursorTool(CursorTool tool) {
        paramsLock.writeLock().lock();
        try {
            if (currentCursorTool != null) {
                mouseEventDispatcher.removeMouseListener(currentCursorTool);
            }

            currentCursorTool = tool;

            if (currentCursorTool == null) {
                setCursor(Cursor.getDefaultCursor());
                dragBox.setEnabled(false);

            } else {
                setCursor(currentCursorTool.getCursor());
                dragBox.setEnabled(currentCursorTool.drawDragBox());
                currentCursorTool.setMapPane(this);
                mouseEventDispatcher.addMouseListener(currentCursorTool);
            }

        } finally {
            paramsLock.writeLock().unlock();
        }
    }

    /**
     * Called when a new map layer has been added. Sets the layer as selected (for queries) and, if
     * the layer table is being used, adds the new layer to the table.
     */
    @Override
    public void layerAdded(MapLayerListEvent event) {
        paramsLock.writeLock().lock();
        try {
            Layer layer = event.getLayer();

            if (layer instanceof ComponentListener) {
                addComponentListener((ComponentListener) layer);
            }

            setFullExtent();
            MapViewport viewport = mapContent.getViewport();
            if (viewport.getBounds().isEmpty()) {
                viewport.setBounds(fullExtent);
            }

        } finally {
            paramsLock.writeLock().unlock();
        }

        drawLayers(false);
        repaint();
    }

    /** Called when a map layer has been removed */
    @Override
    public void layerRemoved(MapLayerListEvent event) {
        paramsLock.writeLock().lock();
        try {
            Layer layer = event.getLayer();

            if (layer instanceof ComponentListener) {
                removeComponentListener((ComponentListener) layer);
            }

            if (mapContent.layers().isEmpty()) {
                fullExtent = null;
            } else {
                setFullExtent();
            }

        } finally {
            paramsLock.writeLock().unlock();
        }

        drawLayers(false);
        repaint();
    }

    /**
     * Called when a map layer has changed, e.g. features added to a displayed feature collection
     */
    @Override
    public void layerChanged(MapLayerListEvent event) {
        paramsLock.writeLock().lock();
        try {
            int reason = event.getMapLayerEvent().getReason();

            if (reason == MapLayerEvent.DATA_CHANGED) {
                setFullExtent();
            }

            if (reason != MapLayerEvent.SELECTION_CHANGED) {
                clearLabelCache.set(true);
                drawLayers(false);
            }

        } finally {
            paramsLock.writeLock().unlock();
        }

        repaint();
    }

    /** {@inheritDoc} */
    @Override
    public void layerMoved(MapLayerListEvent event) {
        drawLayers(false);
        repaint();
    }

    /** {@inheritDoc} */
    @Override
    public void layerPreDispose(MapLayerListEvent event) {
        getRenderingExecutor().cancelAll();
    }

    /**
     * Called by the map content's viewport when its bounds have changed. Used here to watch for a
     * changed CRS, in which case the map is re-displayed at full extent.
     */
    @Override
    public void mapBoundsChanged(MapBoundsEvent event) {
        paramsLock.writeLock().lock();
        try {
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

        } finally {
            paramsLock.writeLock().unlock();
        }
    }

    /**
     * Publish a MapPaneEvent to registered listeners
     *
     * @param ev the event to publish
     * @see MapPaneListener
     */
    protected void publishEvent(MapPaneEvent ev) {
        for (MapPaneListener listener : listeners) {
            switch (ev.getType()) {
                case NEW_MAPCONTENT:
                    listener.onNewMapContent(ev);
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
            }
        }
    }

    /**
     * Determines the full extent of of
     *
     * @return {@code true} if full extent was set successfully
     */
    protected boolean setFullExtent() {
        if (mapContent != null && !mapContent.layers().isEmpty()) {
            try {
                fullExtent = mapContent.getMaxBounds();

                /*
                 * Guard against degenerate envelopes (e.g. empty
                 * map layer or single point feature)
                 */
                if (fullExtent == null) {
                    // set arbitrary bounds centred on 0,0
                    fullExtent =
                            new ReferencedEnvelope(
                                    -1, 1, -1, 1, mapContent.getCoordinateReferenceSystem());

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

                    fullExtent =
                            new ReferencedEnvelope(
                                    xmin,
                                    xmax,
                                    ymin,
                                    ymax,
                                    mapContent.getCoordinateReferenceSystem());
                }

            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        } else {
            fullExtent = null;
        }

        return fullExtent != null;
    }

    /**
     * {@inheritDoc} Publishes a {@linkplain MapPaneEvent} of type {@code
     * MapPaneEvent.Type.RENDERING_STARTED} to listeners.
     */
    @Override
    public void onRenderingStarted(RenderingExecutorEvent ev) {
        publishEvent(new MapPaneEvent(this, MapPaneEvent.Type.RENDERING_STARTED));
    }

    /**
     * {@inheritDoc} Publishes a {@linkplain MapPaneEvent} of type {@code
     * MapPaneEvent.Type.RENDERING_STOPPED} to listeners.
     */
    @Override
    public void onRenderingCompleted(RenderingExecutorEvent event) {
        if (clearLabelCache.get()) {
            labelCache.clear();
        }

        clearLabelCache.set(false);
        repaint();
        publishEvent(new MapPaneEvent(this, MapPaneEvent.Type.RENDERING_STOPPED));
    }

    /**
     * {@inheritDoc} Publishes a {@linkplain MapPaneEvent} of type {@code
     * MapPaneEvent.Type.RENDERING_STOPPED} to listeners.
     */
    @Override
    public void onRenderingFailed(RenderingExecutorEvent ev) {
        publishEvent(new MapPaneEvent(this, MapPaneEvent.Type.RENDERING_STOPPED));
    }
}
