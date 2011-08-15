/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2011, Open Source Geospatial Foundation (OSGeo)
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
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
import org.geotools.map.MapContent;
import org.geotools.map.Layer;
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
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A map display pane that works with a renderer ({@code StreamingRenderer} by default)
 * to display map layers contained in a {@code MapContent} instance. It supports the 
 * use of tool classes to implement, for example, mouse-controlled zooming and panning.
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
 * @source $URL$
 * @version $Id$
 */
public class JMapPane extends JPanel implements MapPane, MapLayerListListener, MapBoundsListener,
        RenderingExecutorListener {

    /**
     * Default delay (milliseconds) before the map will be redrawn when resizing
     * the pane. This is to avoid flickering while drag-resizing.
     */
    public static final int DEFAULT_RESIZING_PAINT_DELAY = 500;  // delay in milliseconds

    /**
     * Default background color (white).
     */
    public static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;

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

    /*
     * This field is used to cache the full extent of the combined map
     * layers.
     */
    private ReferencedEnvelope fullExtent;
    
    private final MapToolManager toolManager;
    private final MouseDragBox dragBox;
    
    private MapContent mapContent;
    private GTRenderer renderer;
    private LabelCache labelCache;
    private RenderingExecutor renderingExecutor;
    private Set<MapPaneListener> listeners = new HashSet<MapPaneListener>();

    private BufferedImage baseImage;
    private Graphics2D baseImageGraphics;
    private Point imageOrigin;
    
    private AtomicBoolean baseImageMoved;
    private AtomicBoolean clearLabelCache;

    /**
     * Creates a new map pane. 
     */
    public JMapPane() {
        this(null, null);
    }

    /**
     * Creates a new map pane with the given renderer and map content.
     * Either or both of {@code renderer} and {@code content} may be
     * {@code null} when the {@link #setRenderer(GTRenderer)} and 
     * {@link #setMapContent(MapContent)} methods are to be called 
     * subsequently.
     *
     * @param renderer the renderer to use for drawing layers
     * @param content the {@code MapContent} instance containing layers to 
     *     display
     */
    public JMapPane(GTRenderer renderer, MapContent content) {
        this(renderer, content, null);
    }
    
    /**
     * Creates a new map pane with the given renderer and map content.
     * Either or both of {@code renderer} and {@code content} may be
     * {@code null} when the {@link #setRenderer(GTRenderer)} and 
     * {@link #setMapContent(MapContent)} methods are to be called 
     * subsequently. If {@code executor} is {@code null}, a default
     * rendering executor (an instance of {@linkplain DefaultRenderingExecutor})
     * will be set.
     *
     * @param renderer the renderer to use for drawing layers
     * @param content the {@code MapContent} instance containing layers to 
     *     display
     * @param executor the rendering executor
     */
    public JMapPane(GTRenderer renderer, MapContent content, RenderingExecutor executor) {
        imageOrigin = new Point(0, 0);
        setBackground(DEFAULT_BACKGROUND_COLOR);

        acceptRepaintRequests = true;
        baseImageMoved = new AtomicBoolean();
        clearLabelCache = new AtomicBoolean();

        /*
         * An interval is set for delayed painting to avoid
         * flickering when the user is drag-resizing the parent
         * container of this map pane.
         *
         * Using a ComponentListener doesn't work because, unlike
         * a JFrame, the pane receives a stream of events during
         * drag-resizing.
         */
        resizingPaintDelay = DEFAULT_RESIZING_PAINT_DELAY;

        doSetRenderer(renderer);
        doSetMapContent(content);
        doSetRenderingExecutor(executor);

        toolManager = new MapToolManager(this);

        dragBox = new MouseDragBox(this);
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
            @Override
            public void hierarchyChanged(HierarchyEvent he) {
                if ((he.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
                    if (isShowing()) {
                        onShownOrResized();
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
            @Override
            public void run() {
                setForNewSize();
            }
        }, resizingPaintDelay, TimeUnit.MILLISECONDS);
    }
    
    private void setForNewSize() {
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
            
            publishEvent(new MapPaneEvent(this, 
                    MapPaneEvent.Type.DISPLAY_AREA_CHANGED,
                    getDisplayArea()));

            acceptRepaintRequests = true;
            drawBaseImage(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
    public void addMouseListener(MapMouseListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }

        toolManager.addMouseListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMouseListener(MapMouseListener listener) {
        if (listener != null) {
            toolManager.removeMouseListener(listener);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMapPaneListener(MapPaneListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }

        listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMapPaneListener(MapPaneListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }
    
    /**
     * Sets the renderer to be used by this map pane.
     *
     * @param renderer the renderer to use
     */
    public void setRenderer(GTRenderer renderer) {
        doSetRenderer(renderer);
    }

    /**
     * Gets the renderer used by this map pane.
     *
     * @return the renderer
     */
    public GTRenderer getRenderer() {
        return renderer;
    }

    private void doSetRenderer(GTRenderer newRenderer) {
        if (newRenderer != null) {
            Map<Object, Object> hints = newRenderer.getRendererHints();
            if (hints == null) {
                hints = new HashMap<Object, Object>();
            }
            
            if (newRenderer instanceof StreamingRenderer) {
                if (hints.containsKey(StreamingRenderer.LABEL_CACHE_KEY)) {
                    labelCache = (LabelCache) hints.get(StreamingRenderer.LABEL_CACHE_KEY);
                } else {
                    labelCache = new LabelCacheImpl();
                    hints.put(StreamingRenderer.LABEL_CACHE_KEY, labelCache);
                }
            }
            
            newRenderer.setRendererHints(hints);

            if (mapContent != null) {
                newRenderer.setMapContent(mapContent);
            }
        }
        
        renderer = newRenderer;
    }
    
    /**
     * Sets the rendering executor. If {@code executor} is {@code null},
     * the default {@linkplain DefaultRenderingExecutor} will be set.
     * 
     * @param newExecutor the rendering executor
     */
    public void setRenderingExecutor(RenderingExecutor executor) {
        doSetRenderingExecutor(executor);
    }
    
    /**
     * Gets the rendering executor.
     * 
     * @return the rendering executor
     */
    public RenderingExecutor getRenderingExecutor() {
        return renderingExecutor;
    }
    
    private void doSetRenderingExecutor(RenderingExecutor newExecutor) {
        if (newExecutor == null) {
            newExecutor = new DefaultRenderingExecutor();
        }
        
        if (renderingExecutor != null) {
            renderingExecutor.shutdown();
        }
        
        renderingExecutor = newExecutor;
    }    

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMapContent(MapContent content) {
        doSetMapContent(content);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public MapContent getMapContent() {
        return mapContent;
    }

    private void doSetMapContent(MapContent newMapContent) {
        if (mapContent != newMapContent) {

            if (mapContent != null) {
                mapContent.removeMapLayerListListener(this);
                for( Layer layer : mapContent.layers() ){
                    if( layer instanceof ComponentListener){
                        removeComponentListener( (ComponentListener) layer );
                    }
                }
            }

            mapContent = newMapContent;
            mapContent.getViewport().setMatchingAspectRatio(true);

            if (mapContent != null) {
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
                    doSetDisplayArea(fullExtent);
                }
            }

            if (renderer != null) {
                renderer.setMapContent(mapContent);
            }
            
            MapPaneEvent event = new MapPaneEvent(
                    this, MapPaneEvent.Type.NEW_MAPCONTENT, mapContent);
            publishEvent(event);
            
            drawBaseImage(false);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ReferencedEnvelope getDisplayArea() {
        if (mapContent != null) {
            return mapContent.getViewport().getBounds();
        } else if (pendingDisplayArea != null) {
            return new ReferencedEnvelope(pendingDisplayArea);
        } else {
            return new ReferencedEnvelope();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDisplayArea(Envelope envelope) {
        if (envelope == null) {
            throw new IllegalArgumentException("envelope must not be null");
        }

        doSetDisplayArea(envelope);
        if (mapContent != null) {
            clearLabelCache.set(true);
            drawBaseImage(false);
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
        if (mapContent != null) {
            if (equalsFullExtent(envelope)) {
                mapContent.getViewport().setBounds(fullExtent);

            } else {
                CoordinateReferenceSystem crs = envelope.getCoordinateReferenceSystem();
                if (crs == null) {
                    // assume that it is the current CRS
                    crs = mapContent.getCoordinateReferenceSystem();
                }

                ReferencedEnvelope refEnv = new ReferencedEnvelope(
                        envelope.getMinimum(0), envelope.getMaximum(0),
                        envelope.getMinimum(1), envelope.getMaximum(1),
                        crs);

                mapContent.getViewport().setBounds(refEnv);
            }
            
        } else {
            pendingDisplayArea = new ReferencedEnvelope(envelope);
        }

        // Publish the resulting display area with the event
        publishEvent( new MapPaneEvent(this, 
                MapPaneEvent.Type.DISPLAY_AREA_CHANGED,
                getDisplayArea()) );
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
        if (envelope == null || mapContent == null) {
            return false;
        }
        if (fullExtent == null && !setFullExtent()) {
            return false;
        }

        final double TOL = 1.0e-6d * Math.min(fullExtent.getWidth(), fullExtent.getHeight());

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
     * {@inheritDoc}
     */
    @Override
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
    
    @Override
    public void setIgnoreRepaint(boolean ignoreRepaint) {
        super.setIgnoreRepaint(ignoreRepaint);
        acceptRepaintRequests = !ignoreRepaint;
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
     * {@inheritDoc}
     */
    @Override
    public AffineTransform getScreenToWorldTransform() {
        if (mapContent != null) {
            return mapContent.getViewport().getScreenToWorld();
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AffineTransform getWorldToScreenTransform() {
        if (mapContent != null) {
            return mapContent.getViewport().getWorldToScreen();
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
            @Override
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
        if (mapContent != null
                && !mapContent.getViewport().isEmpty()
                && acceptRepaintRequests) {
            
            Rectangle r = getVisibleRect();
            if (baseImage == null || createNewImage) {
                baseImage = GraphicsEnvironment.getLocalGraphicsEnvironment().
                        getDefaultScreenDevice().getDefaultConfiguration().
                        createCompatibleImage(r.width, r.height, Transparency.TRANSLUCENT);

                if (baseImageGraphics != null) {
                    baseImageGraphics.dispose();
                }

                baseImageGraphics = baseImage.createGraphics();
                clearLabelCache.set(true);
                
            } else {
                baseImageGraphics.setBackground(getBackground());
                baseImageGraphics.clearRect(0, 0, r.width, r.height);
            }

            if (renderer != null && mapContent != null && !mapContent.layers().isEmpty()) {
                renderingExecutor.submit(mapContent, renderer, baseImageGraphics, this);
            }
        }
    }

    /**
     * {@inheritDoc}
     * Publishes a {@linkplain MapPaneEvent} of type
     * {@code MapPaneEvent.Type.RENDERING_STARTED} to listeners.
     */
    @Override
    public void onRenderingStarted(RenderingExecutorEvent ev) {
        publishEvent(new MapPaneEvent(this, MapPaneEvent.Type.RENDERING_STARTED));
    }
    

    /**
     * {@inheritDoc}
     * Publishes a {@linkplain MapPaneEvent} of type
     * {@code MapPaneEvent.Type.RENDERING_STOPPED} to listeners.
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
     * {@inheritDoc}
     * Publishes a {@linkplain MapPaneEvent} of type
     * {@code MapPaneEvent.Type.RENDERING_STOPPED} to listeners.
     */
    @Override
    public void onRenderingFailed(RenderingExecutorEvent ev) {
        publishEvent(new MapPaneEvent(this, MapPaneEvent.Type.RENDERING_STOPPED));
    }

    /**
     * Called after the base image has been dragged. Sets the new map area and
     * transforms
     */
    protected void afterImageMoved() {
        int dx = imageOrigin.x;
        int dy = imageOrigin.y;
        DirectPosition2D newPos = new DirectPosition2D(dx, dy);
        mapContent.getViewport().getScreenToWorld().transform(newPos, newPos);

        ReferencedEnvelope env = new ReferencedEnvelope(mapContent.getViewport().getBounds());
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
    @Override
    public void layerAdded(MapLayerListEvent event) {
        Layer layer = event.getElement();
        
        if( layer instanceof ComponentListener ){
            addComponentListener( (ComponentListener) layer );
        }

        boolean atFullExtent = equalsFullExtent(getDisplayArea());
        setFullExtent();
        if (mapContent.layers().size() == 1 || atFullExtent) {
            reset();
        }

        drawBaseImage(false);
        repaint();
    }

    /**
     * Called when a map layer has been removed
     */
    @Override
    public void layerRemoved(MapLayerListEvent event) {
        Layer layer = event.getElement();

        if( layer instanceof ComponentListener ){
            addComponentListener( (ComponentListener) layer );
        }

        if (mapContent.layers().isEmpty()) {
            fullExtent = null;
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
    @Override
    public void layerChanged(MapLayerListEvent event) {
        int reason = event.getMapLayerEvent().getReason();

        if (reason == MapLayerEvent.DATA_CHANGED) {
            setFullExtent();
        }

        if (reason != MapLayerEvent.SELECTION_CHANGED) {
            clearLabelCache.set(true);
            drawBaseImage(false);
        }

        repaint();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layerMoved(MapLayerListEvent event) {
        drawBaseImage(false);
        repaint();
    }

    /**
     * Called by the map context when its bounds have changed. Used
     * here to watch for a changed CRS, in which case the map is
     * redisplayed at (new) full extent.
     */
    @Override
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
     * 
     * @return {@code true} if full extent was set successfully
     */
    private boolean setFullExtent() {
        if (mapContent != null && !mapContent.layers().isEmpty()) {
            try {
                fullExtent = mapContent.getMaxBounds();

                /*
                 * Guard against degenerate envelopes (e.g. empty
                 * map layer or single point feature)
                 */
                if (fullExtent == null ) {
                    // set arbitrary bounds centred on 0,0
                    fullExtent = new ReferencedEnvelope(-1, 1, -1, 1, mapContent.getCoordinateReferenceSystem());

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

                    fullExtent = new ReferencedEnvelope(xmin, xmax, ymin, ymax, mapContent.getCoordinateReferenceSystem());
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
     * Publish a MapPaneEvent to registered listeners
     *
     * @param ev the event to publish
     * @see MapPaneListener
     */
    private void publishEvent(MapPaneEvent ev) {
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

}
