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
package org.geotools.swt;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.event.MapBoundsEvent;
import org.geotools.map.event.MapBoundsListener;
import org.geotools.map.event.MapLayerEvent;
import org.geotools.map.event.MapLayerListEvent;
import org.geotools.map.event.MapLayerListListener;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.label.LabelCacheImpl;
import org.geotools.renderer.lite.LabelCache;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.swt.event.MapMouseListener;
import org.geotools.swt.event.MapPaneEvent;
import org.geotools.swt.event.MapPaneListener;
import org.geotools.swt.tool.CursorTool;
import org.geotools.swt.tool.MapToolManager;
import org.geotools.swt.utils.CursorManager;
import org.geotools.swt.utils.Messages;
import org.geotools.swt.utils.Utils;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * A map display pane that works with a GTRenderer and
 * MapContext to display features. It supports the use of tool classes
 * to implement, for example, mouse-controlled zooming and panning.
 * <p>
 * Rendering is performed on a background thread and is managed by
 * the {@linkplain RenderingExecutor} class.
 * <p>
 * Adapted from original code by Ian Turton.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 * @author Michael Bedward
 * @author Ian Turton
 *
 *
 *
 * @source $URL$
 */
public class SwtMapPane extends Canvas implements Listener, MapLayerListListener, MapBoundsListener {
    private static final PaletteData PALETTE_DATA = new PaletteData(0xFF0000, 0xFF00, 0xFF);

    /** RGB value to use as transparent color */
    private static final int TRANSPARENT_COLOR = 0x123456;
    /**
     * Default delay (milliseconds) before the map will be redrawn when resizing
     * the pane. This is to avoid flickering while drag-resizing.
     */
    public static final int DEFAULT_RESIZING_PAINT_DELAY = 500; // delay in milliseconds

    private int resizingPaintDelay;
    private boolean acceptRepaintRequests;

    /**
     * This field is used to cache the full extent of the combined map
     * layers.
     */
    private ReferencedEnvelope fullExtent;

    private MapContent content;
    private GTRenderer renderer;
    private LabelCache labelCache;
    private MapToolManager toolManager;
    private MapLayerComposite layerTable;
    private Set<MapPaneListener> listeners = new HashSet<MapPaneListener>();
    private AffineTransform worldToScreen;
    private AffineTransform screenToWorld;
    private Rectangle curPaintArea;
    private BufferedImage baseImage;
    private Point imageOrigin;
    private boolean redrawBaseImage;
    private boolean clearLabelCache;

    private boolean toolCanDraw;
    private boolean toolCanMove;

    /**
     * swt image used to draw
     */
    private Image swtImage;
    private GC gc;
    private boolean mouseDown = false;
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private boolean isDragging = false;

    private Image overlayImage;

    private ReferencedEnvelope overlayEnvelope;

    private boolean overlayDoXor;

    private int alpha = 255;

    private Color white;
    private Color yellow;

    private Color cursorToolColor;
    private int cursorToolLineWidth;
    private int cursorToolLineStyle;

    /**
     * Constructor - creates an instance of JMapPane with no map
     * context or renderer initially
     */
    public SwtMapPane( Composite parent, int style ) {
        this(parent, style, null, null);
    }

    /**
     * Constructor - creates an instance of JMapPane with the given
     * renderer and map context.
     *
     * @param renderer a renderer object
     * @param content an instance of MapContext
     */
    public SwtMapPane( Composite parent, int style, GTRenderer renderer, MapContent content ) {
        super(parent, style);
        white = getDisplay().getSystemColor(SWT.COLOR_WHITE);
        yellow = getDisplay().getSystemColor(SWT.COLOR_YELLOW);

        addListener(SWT.Paint, this);
        addListener(SWT.MouseDown, this);
        addListener(SWT.MouseUp, this);

        imageOrigin = new Point(0, 0);

        acceptRepaintRequests = true;
        redrawBaseImage = true;
        clearLabelCache = false;

        setRenderer(renderer);
        setMapContent(content);

        toolManager = new MapToolManager(this);

        this.addMouseListener(toolManager);
        this.addMouseMoveListener(toolManager);
        this.addMouseWheelListener(toolManager);

        /*
         * Listen for mouse entered events to (re-)set the
         * current tool cursor, otherwise the cursor seems to
         * default to the standard cursor sometimes (at least
         * on OSX)
         */
        this.addMouseMoveListener(new MouseMoveListener(){
            public void mouseMove( MouseEvent event ) {
                if (mouseDown) {
                    endX = event.x;
                    endY = event.y;
                    isDragging = true;
                    if (!isDisposed())
                        redraw();
                }
            }
        });

        addControlListener(new ControlAdapter(){
            public void controlResized( ControlEvent e ) {
                curPaintArea = getVisibleRect();
                doSetDisplayArea(SwtMapPane.this.content.getViewport().getBounds());
            }
        });

        cursorToolColor = getDisplay().getSystemColor(SWT.COLOR_YELLOW);
        cursorToolLineWidth = 2;
        cursorToolLineStyle = SWT.LINE_SOLID;
    }

    /**
     * Set the current cursor tool
     *
     * @param tool the tool to set; null means no active cursor tool
     */
    public void setCursorTool( CursorTool tool ) {
        if (tool == null) {
            toolManager.setNoCursorTool();
            this.setCursor(CursorManager.getInstance().getArrowCursor());
            toolCanDraw = false;
            toolCanMove = false;
        } else {
            this.setCursor(tool.getCursor());
            toolManager.setCursorTool(tool);
            toolCanDraw = tool.canDraw();
            toolCanMove = tool.canMove();
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
    public void addMouseListener( MapMouseListener listener ) {
        if (listener == null) {
            throw new IllegalArgumentException(Messages.getString("arg_null_error")); //$NON-NLS-1$
        }

        toolManager.addMouseListener(listener);
    }

    /**
     * Unregister the {@code MapMouseListener} object.
     *
     * @param listener the listener to remove
     * @throws IllegalArgumentException if listener is null
     */
    public void removeMouseListener( MapMouseListener listener ) {
        if (listener == null) {
            throw new IllegalArgumentException(Messages.getString("arg_null_error")); //$NON-NLS-1$
        }

        toolManager.removeMouseListener(listener);
    }

    /**
     * Register an object that wishes to receive {@code MapPaneEvent}s
     *
     * @param listener an object that implements {@code MapPaneListener}
     * @see MapPaneListener
     */
    public void addMapPaneListener( MapPaneListener listener ) {
        if (listener == null) {
            throw new IllegalArgumentException(Messages.getString("arg_null_error")); //$NON-NLS-1$
        }

        listeners.add(listener);
    }

    /**
     * Register a {@linkplain MapLayerComposite} object to be receive
     * layer change events from this map pane and to control layer
     * ordering, visibility and selection.
     *
     * @param layerTable an instance of MapLayerTable
     *
     * @throws IllegalArgumentException if layerTable is null
     */
    public void setMapLayerTable( MapLayerComposite layerTable ) {
        if (layerTable == null) {
            throw new IllegalArgumentException(Messages.getString("arg_null_error")); //$NON-NLS-1$
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
    public void setRenderer( GTRenderer renderer ) {
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

                if (this.content != null) {
                    renderer.setMapContent(this.content);
                }

            }
        }

        this.renderer = renderer;
    }

    /**
     * Get the map content associated with this map pane
     * @return a live reference to the current map context
     */
    public MapContent getMapContent() {
        return content;
    }

    /**
     * Set the map context for this map pane to display
     * @param content the map context
     */
    public void setMapContent( MapContent content ) {
        if (this.content != content) {

            if (this.content != null) {
                this.content.removeMapLayerListListener(this);
                // for( MapLayer layer : this.context.getLayers() ) {
                // if (layer instanceof ComponentListener) {
                // removeComponentListener((ComponentListener) layer);
                // }
                // }
            }

            this.content = content;

            if (content != null) {
                this.content.addMapLayerListListener(this);
                this.content.addMapBoundsListener(this);

                // set all layers as selected by default for the info tool
                for( Layer layer : content.layers() ) {
                    layer.setSelected(true);
                    // if (layer instanceof ComponentListener) {
                    // addComponentListener((ComponentListener) layer);
                    // }
                }

                setFullExtent();
            }

            if (renderer != null) {
                renderer.setMapContent(this.content);
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
        ReferencedEnvelope aoi = null;

        if (curPaintArea != null && screenToWorld != null) {
            Rectangle2D awtRectangle = Utils.toAwtRectangle(curPaintArea);
            Point2D p0 = new Point2D.Double(awtRectangle.getMinX(), awtRectangle.getMinY());
            Point2D p1 = new Point2D.Double(awtRectangle.getMaxX(), awtRectangle.getMaxY());
            screenToWorld.transform(p0, p0);
            screenToWorld.transform(p1, p1);

            aoi = new ReferencedEnvelope(Math.min(p0.getX(), p1.getX()), Math.max(p0.getX(), p1.getX()), Math.min(p0.getY(),
                    p1.getY()), Math.max(p0.getY(), p1.getY()), content.getCoordinateReferenceSystem());
        }

        return aoi;
    }

    public void setCrs( CoordinateReferenceSystem crs ) {
        try {
            // System.out.println(content.layers().size());
            ReferencedEnvelope rEnv = getDisplayArea();
            // System.out.println(rEnv);

            CoordinateReferenceSystem sourceCRS = rEnv.getCoordinateReferenceSystem();
            CoordinateReferenceSystem targetCRS = crs;

            MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
            com.vividsolutions.jts.geom.Envelope newJtsEnv = JTS.transform(rEnv, transform);

            ReferencedEnvelope newEnvelope = new ReferencedEnvelope(newJtsEnv, targetCRS);
            content.getViewport().setBounds(newEnvelope);
            fullExtent = null;
            doSetDisplayArea(newEnvelope);

            // ReferencedEnvelope displayArea =
            getDisplayArea();
            // System.out.println(displayArea);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the area to display by calling the {@linkplain MapContext#setAreaOfInterest}
     * method of this pane's map context. Does nothing if the MapContext has not been set.
     * If neither the context or the envelope have coordinate reference systems defined
     * this method does nothing.
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
    public void setDisplayArea( Envelope envelope ) {
        if (content != null) {
            if (curPaintArea == null || curPaintArea.isEmpty()) {
                return;
            } else {
                doSetDisplayArea(envelope);
                clearLabelCache = true;
                if (!isDisposed())
                    redraw();
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
    private void doSetDisplayArea( Envelope envelope ) {
        assert (content != null && curPaintArea != null && !curPaintArea.isEmpty());

        if (equalsFullExtent(envelope)) {
            setTransforms(fullExtent, curPaintArea);
        } else {
            setTransforms(envelope, curPaintArea);
        }
        ReferencedEnvelope adjustedEnvelope = getDisplayArea();
        content.getViewport().setBounds(adjustedEnvelope);

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
    private boolean equalsFullExtent( final Envelope envelope ) {
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
        if (fullExtent == null) {
            setFullExtent();
        }
        try {
            fullExtent = new ReferencedEnvelope(CRS.transform(fullExtent, content.getCoordinateReferenceSystem()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDisplayArea(fullExtent);
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
    public void setRepaint( boolean repaint ) {
        acceptRepaintRequests = repaint;
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
    public void setResizeDelay( int delay ) {
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
    public void moveImage( int dx, int dy ) {
        imageOrigin.translate(dx, dy);
        redrawBaseImage = false;
        if (!isDisposed())
            redraw();
    }

    /**
     * Called by the {@linkplain SwtMapPane.RenderingTask} when rendering has been completed
     * Publishes a {@linkplain MapPaneEvent} of type
     * {@code MapPaneEvent.Type.RENDERING_STOPPED} to listeners.
     *
     * @see MapPaneListener#onRenderingStopped(org.geotools.swing.event.MapPaneEvent)
     */
    public void onRenderingCompleted() {
        if (clearLabelCache) {
            labelCache.clear();
        }
        clearLabelCache = false;

        // Graphics2D paneGr = (Graphics2D) this.getGraphics();
        // paneGr.drawImage(baseImage, imageOrigin.x, imageOrigin.y, null);

        // swtImage = new Image(this.getDisplay(), awtToSwt(baseImage, curPaintArea.width,
        // curPaintArea.height));
        // if (gc != null && !gc.isDisposed() && swtImage != null)
        // gc.drawImage(swtImage, imageOrigin.x, imageOrigin.y);

        MapPaneEvent ev = new MapPaneEvent(this, MapPaneEvent.Type.RENDERING_STOPPED);
        publishEvent(ev);
    }

    /**
     * Called by the {@linkplain SwtMapPane.RenderingTask} when rendering was cancelled.
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
     * Called by the {@linkplain SwtMapPane.RenderingTask} when rendering failed.
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
     * when the user pauses during drag-resizing fo the map pane. The base implementation
     * does nothing. It is provided for sub-classes to override if required.
     */
    public void onRenderingRejected() {
        // do nothing
    }

    /**
     * Called after the base image has been dragged. Sets the new map area and
     * transforms
     * @param env the display area (world coordinates) prior to the image being moved
     * @param paintArea the current drawing area (screen units)
     */
    private void afterImageMove() {
        final ReferencedEnvelope env = content.getViewport().getBounds();
        if (env == null)
            return;
        int dx = imageOrigin.x;
        int dy = imageOrigin.y;
        DirectPosition2D newPos = new DirectPosition2D(dx, dy);
        screenToWorld.transform(newPos, newPos);

        env.translate(env.getMinimum(0) - newPos.x, env.getMaximum(1) - newPos.y);
        doSetDisplayArea(env);
        imageOrigin.setLocation(0, 0);
        redrawBaseImage = true;
    }

    /**
     * Called when a new map layer has been added. Sets the layer
     * as selected (for queries) and, if the layer table is being
     * used, adds the new layer to the table.
     */
    public void layerAdded( MapLayerListEvent event ) {
        Layer layer = event.getElement();
        if (layerTable != null) {
            layerTable.onAddLayer(layer);
        }
        layer.setSelected(true);
        redrawBaseImage = true;

        boolean atFullExtent = equalsFullExtent(getDisplayArea());
        boolean firstLayer = content.layers().size() == 1;
        if (firstLayer || atFullExtent) {
            reset();
            if (firstLayer) {
                setCrs(layer.getBounds().getCoordinateReferenceSystem());
                return;
            }
        }
        if (!isDisposed())
            redraw();
    }

    /**
     * Called when a map layer has been removed
     */
    public void layerRemoved( MapLayerListEvent event ) {
        Layer layer = event.getElement();
        if (layerTable != null) {
            layerTable.onRemoveLayer(layer);
        }
        redrawBaseImage = true;

        if (content.layers().size() == 0) {
            clearFields();
        } else {
            setFullExtent();
        }
        if (!isDisposed())
            redraw();
    }

    /**
     * Called when a map layer has changed, e.g. features added
     * to a displayed feature collection
     */
    public void layerChanged( MapLayerListEvent event ) {
        if (layerTable != null) {
            layerTable.repaint(event.getElement());
        }
        redrawBaseImage = true;

        int reason = event.getMapLayerEvent().getReason();

        if (reason == MapLayerEvent.DATA_CHANGED) {
            setFullExtent();
        }

        if (reason != MapLayerEvent.SELECTION_CHANGED) {
            if (!isDisposed())
                redraw();
        }
    }

    /**
     * Called when the bounds of a map layer have changed
     */
    public void layerMoved( MapLayerListEvent event ) {
        redrawBaseImage = true;
        if (!isDisposed())
            redraw();
    }

    /**
     * Called by the map context when its bounds have changed. Used
     * here to watch for a changed CRS, in which case the map is
     * redisplayed at (new) full extent.
     */
    public void mapBoundsChanged( MapBoundsEvent event ) {
        redrawBaseImage = true;
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
        if (content != null && content.layers().size() > 0) {
            try {

                fullExtent = content.getMaxBounds();

                /*
                 * Guard agains degenerate envelopes (e.g. empty
                 * map layer or single point feature)
                 */
                if (fullExtent == null) {
                    // set arbitrary bounds centred on 0,0
                    fullExtent = worldEnvelope();// new ReferencedEnvelope(-1, 1, -1, 1,
                                                 // context.getCoordinateReferenceSystem());

                }
                // else {
                // double w = fullExtent.getWidth();
                // double h = fullExtent.getHeight();
                // double x = fullExtent.getMinimum(0);
                // double y = fullExtent.getMinimum(1);
                //
                // double xmin = x;
                // double xmax = x + w;
                // if (w <= 0.0) {
                // xmin = x - 1.0;
                // xmax = x + 1.0;
                // }
                //
                // double ymin = y;
                // double ymax = y + h;
                // if (h <= 0.0) {
                // ymin = y - 1.0;
                // ymax = y + 1.0;
                // }
                //
                // fullExtent = new ReferencedEnvelope(xmin, xmax, ymin, ymax,
                // context.getCoordinateReferenceSystem());
                // }

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
    private void setTransforms( final Envelope envelope, final Rectangle paintArea ) {
        ReferencedEnvelope refEnv = null;
        if (envelope != null) {
            refEnv = new ReferencedEnvelope(envelope);
        } else {
            refEnv = worldEnvelope();
            // FIXME content.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        }

        java.awt.Rectangle awtPaintArea = Utils.toAwtRectangle(paintArea);
        double xscale = awtPaintArea.getWidth() / refEnv.getWidth();
        double yscale = awtPaintArea.getHeight() / refEnv.getHeight();

        double scale = Math.min(xscale, yscale);

        double xoff = refEnv.getMedian(0) * scale - awtPaintArea.getCenterX();
        double yoff = refEnv.getMedian(1) * scale + awtPaintArea.getCenterY();

        worldToScreen = new AffineTransform(scale, 0, 0, -scale, -xoff, yoff);
        try {
            screenToWorld = worldToScreen.createInverse();

        } catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
        }
    }

    private ReferencedEnvelope worldEnvelope() {
        return new ReferencedEnvelope(-180, 180, -90, 90, DefaultGeographicCRS.WGS84);
    }

    /**
     * Publish a MapPaneEvent to registered listeners
     *
     * @param ev the event to publish
     * @see MapPaneListener
     */
    private void publishEvent( MapPaneEvent ev ) {
        for( MapPaneListener listener : listeners ) {
            switch( ev.getType() ) {
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

    public Rectangle getVisibleRect() {
        return getClientArea();
    }

    /**
     * Define an image that has to be set as overlay.
     * 
     * <p>The image will be scaled to fit into the supplied envelope.
     * 
     * @param overlayImage the image to overlay.
     * @param overlayEnvelope the envelope it has to cover.
     * @param overlayDoXor flag for Xor mode.
     */
    public void setOverlay( Image overlayImage, ReferencedEnvelope overlayEnvelope, boolean overlayDoXor, boolean boundsChanged ) {
        if (this.overlayImage != null)
            this.overlayImage.dispose();
        this.overlayImage = overlayImage;
        this.overlayEnvelope = overlayEnvelope;
        this.overlayDoXor = overlayDoXor;

        if (boundsChanged || swtImage == null) {
            redrawBaseImage = true;
        } else {
            redrawBaseImage = false;
        }
        if (!isDisposed())
            redraw();
    }

    /**
     * Sets the transparency value for the base image (overlays not considered).
     * 
     * @param alpha the transparency value (0 - 255).
     */
    public void setBaseImageAlpha( int alpha ) {
        this.alpha = alpha;
    }

    @SuppressWarnings("deprecation")
    public void handleEvent( Event event ) {

        curPaintArea = getVisibleRect();

        // System.out.println("event: " + event.type);
        if (event.type == SWT.MouseDown) {
            startX = event.x;
            startY = event.y;
            // start mouse activity
            mouseDown = true;
        } else if (event.type == SWT.MouseUp) {
            endX = event.x;
            endY = event.y;

            boolean mouseWasMoved = startX != endX || startY != endY;
            if (toolCanMove && mouseWasMoved) {
                // if the tool is able to move draw the moved image
                afterImageMove();
            }
            // stop mouse activity
            mouseDown = false;
            isDragging = false;
        } else if (event.type == SWT.Paint) {
            // System.out.println("PAINT CALLED (DOESN'T MEAN I'M DRAWING)");

            if (acceptRepaintRequests) {
                gc = event.gc;

                // System.out.println(toolCanDraw + "/" + toolCanMove + "/" + isDragging + "/" +
                // redrawBaseImage);

                /*
                 * if the mouse is dragging and the current tool can
                 * move the map we just draw what we already have
                 * on white background. At the end of the moving 
                 * we will take care of adding the missing pieces.
                 */
                if (toolCanMove && isDragging) {
                    // System.out.println("toolCanMove && isDragging");
                    if (gc != null && !gc.isDisposed() && swtImage != null) {
                        /*
                         * double buffer necessary, since the SWT.NO_BACKGROUND
                         * needed by the canvas to properly draw background, doesn't
                         * clean the parts outside the bounds of the moving panned image,
                         * giving a spilling image effect.
                         */
                        Image tmpImage = new Image(getDisplay(), curPaintArea.width, curPaintArea.height);
                        GC tmpGc = new GC(tmpImage);
                        tmpGc.setBackground(white);
                        tmpGc.fillRectangle(0, 0, curPaintArea.width, curPaintArea.height);
                        tmpGc.drawImage(swtImage, imageOrigin.x, imageOrigin.y);
                        gc.drawImage(tmpImage, 0, 0);
                        tmpImage.dispose();
                    }
                    return;
                }

                /*
                 * if the mouse is dragging and the current tool can
                 * draw a boundingbox while dragging, we draw the box 
                 * keeping the current drawn image
                 */
                if (toolCanDraw && toolManager.getCursorTool().isDrawing() && isDragging) {
                    // System.out.println("draw box: " + startX + "/" + startY + "/" + endX +
                    // "/" + endY);
                    if (swtImage != null) {
                        drawFinalImage(swtImage);
                    }
                    gc.setXORMode(true);

                    org.eclipse.swt.graphics.Color fC = gc.getForeground();
                    gc.setLineStyle(cursorToolLineStyle);
                    gc.setLineWidth(cursorToolLineWidth);
                    gc.setForeground(cursorToolColor);
                    gc.drawRectangle(startX, startY, endX - startX, endY - startY);

                    gc.setForeground(fC);
                    gc.setXORMode(false);
                    return;
                }

                if (!toolCanDraw && !toolCanMove && isDragging) {
                    return;
                }

                if (curPaintArea == null || content == null || renderer == null) {
                    return;
                }

                if (content.layers().size() == 0) {
                    // if no layers available, return only if there are also no overlays

                    gc.setForeground(yellow);
                    gc.fillRectangle(0, 0, curPaintArea.width + 1, curPaintArea.height + 1);
                    if (overlayImage == null)
                        return;
                }

                final ReferencedEnvelope mapAOI = content.getViewport().getBounds();
                if (mapAOI == null) {
                    return;
                }

                if (redrawBaseImage) {
                    MapPaneEvent ev = new MapPaneEvent(this, MapPaneEvent.Type.RENDERING_STARTED);
                    publishEvent(ev);

                    baseImage = new BufferedImage(curPaintArea.width + 1, curPaintArea.height + 1, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = baseImage.createGraphics();
                    g2d.fillRect(0, 0, curPaintArea.width + 1, curPaintArea.height + 1);
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // renderer.setContext(context);
                    java.awt.Rectangle awtRectangle = Utils.toAwtRectangle(curPaintArea);
                    renderer.paint(g2d, awtRectangle, mapAOI, getWorldToScreenTransform());
                    // swtImage.dispose();

                    if (swtImage != null && !swtImage.isDisposed()) {
                        swtImage.dispose();
                        swtImage = null;
                    }
                    // System.out.println("READRAWBASEIMAGE");
                    swtImage = new Image(getDisplay(), awtToSwt(baseImage, curPaintArea.width + 1, curPaintArea.height + 1));
                }

                if (swtImage != null) {
                    drawFinalImage(swtImage);
                }

                MapPaneEvent ev = new MapPaneEvent(this, MapPaneEvent.Type.RENDERING_STOPPED);
                publishEvent(ev);
                clearLabelCache = true;
                onRenderingCompleted();
                redrawBaseImage = false;
            }
        }
    }

    private void drawFinalImage( Image swtImage ) {
        Display display = getDisplay();
        // this is only done if an overlay image exists

        // create a new image
        Image tmpImage = new Image(display, curPaintArea.width, curPaintArea.height);
        GC tmpGc = new GC(tmpImage);
        tmpGc.setBackground(white);
        tmpGc.fillRectangle(0, 0, curPaintArea.width, curPaintArea.height);
        if (swtImage != null) {
            // set the alpha to the new image
            tmpGc.setAlpha(alpha);
            /*
             * draw the background image into it
             * (this means everything but the overlay image) 
             */
            tmpGc.drawImage(swtImage, imageOrigin.x, imageOrigin.y);
            /*
             * set the alpha back to opaque so it doesn't influence the
             * overlay image 
             */
            tmpGc.setAlpha(255);
        }
        if (overlayImage != null) {
            // finally draw the overlay image on top
            doOverlayImage(tmpGc);
        }
        // draw the created new image on the pane
        if (gc != null && !gc.isDisposed())
            gc.drawImage(tmpImage, imageOrigin.x, imageOrigin.y);

        if (tmpImage != null && !tmpImage.isDisposed()) {
            tmpImage.dispose();
            tmpImage = null;
        }
        if (tmpGc != null && !tmpGc.isDisposed()) {
            tmpGc.dispose();
            tmpGc = null;
        }
    }

    @SuppressWarnings("deprecation")
    private void doOverlayImage( GC gc ) {
        Point2D lowerLeft = new Point2D.Double(overlayEnvelope.getMinX(), overlayEnvelope.getMinY());
        worldToScreen.transform(lowerLeft, lowerLeft);

        Point2D upperRight = new Point2D.Double(overlayEnvelope.getMaxX(), overlayEnvelope.getMaxY());
        worldToScreen.transform(upperRight, upperRight);

        Rectangle bounds = overlayImage.getBounds();
        if (overlayDoXor) {
            gc.setXORMode(true);
        }

        gc.drawImage(overlayImage,//
                0,//
                0,//
                bounds.width, //
                bounds.height,//
                (int) lowerLeft.getX(),//
                (int) upperRight.getY(),//
                (int) (upperRight.getX() - lowerLeft.getX()),//
                (int) Math.abs(upperRight.getY() - lowerLeft.getY())//
        );
        if (overlayDoXor) {
            gc.setXORMode(false);
        }
    }

    /**
     * Transform a java2d bufferedimage to a swt image.
     * 
     * @param bufferedImage the image to trasform.
     * @param width the image width.
     * @param height the image height.
     * @return swt image.
     */
    private ImageData awtToSwt( BufferedImage bufferedImage, int width, int height ) {
        final int[] awtPixels = new int[width * height];
        ImageData swtImageData = new ImageData(width, height, 24, PALETTE_DATA);
        swtImageData.transparentPixel = TRANSPARENT_COLOR;
        int step = swtImageData.depth / 8;
        final byte[] data = swtImageData.data;
        bufferedImage.getRGB(0, 0, width, height, awtPixels, 0, width);
        for( int i = 0; i < height; i++ ) {
            int idx = (0 + i) * swtImageData.bytesPerLine + 0 * step;
            for( int j = 0; j < width; j++ ) {
                int rgb = awtPixels[j + i * width];
                for( int k = swtImageData.depth - 8; k >= 0; k -= 8 ) {
                    data[idx++] = (byte) ((rgb >> k) & 0xFF);
                }
            }
        }

        return swtImageData;
    }

    @Override
    public void layerPreDispose( MapLayerListEvent event ) {

    }

    /**
     * Returns the colour which is used to draw the bounding box
     * of the currently active cursor tool. The bounding box is
     * drawn in xor mode.
     *
     * @return the colour used for the tool's bounding box
     */
    public Color getCursorToolColor() {
        return cursorToolColor;
    }

    /**
     * Sets the colour which is used to draw the bounding box
     * of the currently active cursor tool. The bounding box is
     * drawn in xor mode.
     *
     * @param color the colour used for the tool's bounding box
     */
    public void setCursorToolColor( Color color ) {
        this.cursorToolColor = color;
    }

    /**
     * Returns the line width of the bounding box of the currently
     * active cursor tool.
     *
     * @return line width
     */
    public int getCursorToolLineWidth() {
        return cursorToolLineWidth;
    }

    /**
     * Sets the line width of the bounding box of the currently
     * active cursor tool.
     *
     * @param lineWidth line width
     */
    public void setCursorToolLineWidth( int lineWidth ) {
        this.cursorToolLineWidth = lineWidth;
    }

    /**
     * Returns the line style of the bounding box of the currently
     * active cursor tool.
     *
     * @return line style
     */
    public int getCursorToolLineStyle() {
        return cursorToolLineStyle;
    }

    /**
     * Sets the line style of the bounding box of the currently
     * active cursor tool.
     *
     * @param lineStyle line style
     * @see SWT#LINE_SOLID
     * @see SWT#LINE_DASH
     * @see SWT#LINE_DOT
     * @see SWT#LINE_DASHDOT
     * @see SWT#LINE_DASHDOTDOT
     */
    public void setCursorToolLineStyle( int lineStyle ) {
        this.cursorToolLineStyle = lineStyle;
    }

}
