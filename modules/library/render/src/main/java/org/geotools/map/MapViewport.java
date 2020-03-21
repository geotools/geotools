/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.map;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapBoundsEvent.Type;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Represents the area of a map to be displayed, expressed in world coordinates and (optionally)
 * screen (window, image) coordinates. A viewport is used to stage information for map rendering.
 * While the viewport provides support for bounds and coordinate reference system out of the box it
 * is expected that the user data support in {@code MapContent} will be used to record additional
 * information such as elevation and time as required for rendering.
 *
 * <p>When both world and screen bounds are defined, the viewport calculates {@code
 * AffineTransforms} to convert the coordinates of one bounds to those of the other. It can also
 * optionally adjust the world bounds to maintain an identical aspect ratio with the screen bounds.
 * Note however that aspect ratio adjustment should not be enabled when the viewport is used with a
 * service such as WMS which mandates that specified screen and world bounds must be honoured
 * exactly, regardless of the resulting aspect ratio differences.
 *
 * <p>The {@code AffineTransforms} can be retrieved with the methods {@linkplain
 * #getScreenToWorld()} and {@linkplain #getWorldToScreen()}. The following rules apply to the
 * return values of these methods:
 *
 * <ul>
 *   <li>If screen area is not defined, {@code null} is returned.
 *   <li>If screen area only is defined, the identity transform is returned.
 *   <li>If both screen area and world extent are defined, calculated transforms are returned.
 * </ul>
 *
 * @author Jody Garnett
 * @author Michael Bedward
 * @since 2.7
 */
public class MapViewport {

    /** The logger for the map module. */
    protected static final Logger LOGGER = Logging.getLogger(MapViewport.class);

    /*
     * Flags whether this viewport can be changed
     */
    private final AtomicBoolean editable;

    /*
     * The current display area expressed in window coordinates
     * (e.g. the visible rectangle of a JMapPane). The area can
     * include slack space beyond the edges of the map layers.
     */
    private Rectangle screenArea;
    /*
     * The current dispay area in world coordinates. The area can
     * include slack space beyond the edges of the map layers.
     */
    private ReferencedEnvelope bounds;

    /*
     * Transform to convert screen (window, image) coordinates to corresponding
     * world coordinates.
     */
    private AffineTransform screenToWorld;

    /*
     * Transform to convert world coordinates to corresponding screen (window,
     * image) coordinates.
     */
    private AffineTransform worldToScreen;
    private CopyOnWriteArrayList<MapBoundsListener> boundsListeners;
    private boolean matchingAspectRatio;
    private boolean hasCenteringTransforms;
    /**
     * Determine if the map retains its scale (false) or its bounds (true) when the MapPane is
     * resized.
     */
    private boolean fixedBoundsOnResize = false;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Creates a new view port. Screen area and world bounds will be empty, and aspect ratio
     * matching will be disabled.
     */
    public MapViewport() {
        this(false);
    }

    /**
     * Creates a new view port. Screen area and world bounds will be empty.
     *
     * @param matchAspectRatio whether to enable aspect ratio matching
     */
    public MapViewport(boolean matchAspectRatio) {
        this(null, matchAspectRatio);
    }

    /**
     * Creates a new view port with the specified display area in world coordinates. The input
     * envelope is copied so subsequent changes to it will not affect the viewport.
     *
     * <p>The initial screen area will be empty and aspect ratio matching will be disabled.
     *
     * @param bounds display area in world coordinates (may be {@code null})
     */
    public MapViewport(ReferencedEnvelope bounds) {
        this(bounds, false);
    }

    /**
     * Creates a new viewport with the specified world bounds. The input envelope is copied so
     * subsequent changes to it will not affect the viewport.
     *
     * <p>The initial screen area will be empty.
     *
     * @param bounds display area in world coordinates (may be {@code null})
     * @param matchAspectRatio whether to enable aspect ratio matching
     */
    public MapViewport(ReferencedEnvelope bounds, boolean matchAspectRatio) {
        this.editable = new AtomicBoolean(true);
        this.screenArea = new Rectangle();
        this.hasCenteringTransforms = false;
        this.matchingAspectRatio = matchAspectRatio;
        copyBounds(bounds);
        setTransforms(true);
    }

    /**
     * Creates a new viewport based on an existing instance. The world bounds, screen area and
     * aspect ratio matching setting of {@code sourceViewport} are copied.
     *
     * <p><strong>Note:</strong> The new viewport will be editable even if {@code sourceViewport} is
     * not editable.
     *
     * @param sourceViewport the viewport to copy
     * @throws IllegalArgumentException if {@code viewport} is {@code null}
     */
    public MapViewport(MapViewport sourceViewport) {
        this.editable = new AtomicBoolean(true);
        this.matchingAspectRatio = sourceViewport.matchingAspectRatio;
        copyBounds(sourceViewport.bounds);
        doSetScreenArea(sourceViewport.screenArea);
        setTransforms(true);
    }

    /**
     * Tests whether this viewport's attributes can be changed. Viewports are editable by default. A
     * non-editable viewport will not allow the value of any of its attributes to be changed and
     * will issue a log message (fine level) on any attempt to do so.
     *
     * @return {@code true} if this viewport is editable
     */
    public boolean isEditable() {
        return editable.get();
    }

    /**
     * Sets whether the value of this viewport's attributes can be changed. Viewports are editable
     * by default.
     *
     * @param editable {@code true} to allow changes
     */
    public void setEditable(boolean editable) {
        this.editable.set(editable);
    }

    /**
     * Sets whether to adjust input world bounds to match the aspect ratio of the screen area.
     *
     * @param enabled whether to enable aspect ratio adjustment
     */
    public void setMatchingAspectRatio(boolean enabled) {
        lock.writeLock().lock();
        try {
            if (checkEditable("setMatchingAspectRatio")) {
                if (enabled != matchingAspectRatio) {
                    matchingAspectRatio = enabled;
                    setTransforms(true);
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Queries whether input worlds bounds will be adjusted to match the aspect ratio of the screen
     * area.
     *
     * @return {@code true} if enabled
     */
    public boolean isMatchingAspectRatio() {
        lock.readLock().lock();
        try {
            return matchingAspectRatio;
        } finally {
            lock.readLock().unlock();
        }
    }

    /** Used by client application to track the bounds of this viewport. */
    public void addMapBoundsListener(MapBoundsListener listener) {
        lock.writeLock().lock();
        try {
            if (boundsListeners == null) {
                boundsListeners = new CopyOnWriteArrayList<MapBoundsListener>();
            }
            if (!boundsListeners.contains(listener)) {
                boundsListeners.add(listener);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeMapBoundsListener(MapBoundsListener listener) {
        if (boundsListeners != null) {
            boundsListeners.remove(listener);
        }
    }

    /**
     * Checks if the view port bounds are empty (undefined). This will be {@code true} if either or
     * both of the world bounds and screen bounds are empty.
     *
     * @return {@code true} if empty
     */
    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return screenArea.isEmpty() || bounds.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Gets the display area in world coordinates.
     *
     * <p>Note Well: this only covers spatial extent; you may wish to use the user data map to
     * record the current viewport time or elevation.
     *
     * @return a copy of the current bounds
     */
    public ReferencedEnvelope getBounds() {
        lock.readLock().lock();
        try {
            return new ReferencedEnvelope(bounds);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Sets the display area in world coordinates.
     *
     * <p>If {@code bounds} is {@code null} or empty, default identity coordinate transforms will be
     * set.
     *
     * <p>If {@code bounds} is not empty, and aspect ratio matching is enabled, the coordinate
     * transforms will be calculated to centre the requested bounds in the current screen area (if
     * defined), after which the world bounds will be adjusted (enlarged) as required to match the
     * screen area's aspect ratio.
     *
     * <p>A {@code MapBoundsEvent} will be fired to inform listeners of the change from old to new
     * bounds. Note that when aspect ratio matching is enabled, the new bounds carried by the event
     * will be the viewport's adjusted bounds, not the originally requested bounds.
     *
     * @param requestedBounds the requested bounds (may be {@code null})
     */
    public void setBounds(ReferencedEnvelope requestedBounds) {
        lock.writeLock().lock();
        try {
            if (checkEditable("setBounds")) {
                ReferencedEnvelope old = bounds;
                copyBounds(requestedBounds);
                setTransforms(true);
                fireMapBoundsListenerMapBoundsChanged(Type.BOUNDS, old, bounds);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void copyBounds(ReferencedEnvelope newBounds) {
        if (newBounds == null || newBounds.isEmpty()) {
            this.bounds = new ReferencedEnvelope();
        } else {
            this.bounds = new ReferencedEnvelope(newBounds);
        }
    }

    /**
     * Gets a copy of the current screen area.
     *
     * @return screen area to render into when drawing.
     */
    public Rectangle getScreenArea() {
        lock.readLock().lock();
        try {
            return new Rectangle(screenArea);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Sets the display area in screen (window, image) coordinates.
     *
     * @param screenArea display area in screen coordinates (may be {@code null})
     */
    public void setScreenArea(Rectangle screenArea) {
        lock.writeLock().lock();
        try {
            if (checkEditable("setScreenArea")) {
                doSetScreenArea(screenArea);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void doSetScreenArea(Rectangle screenArea) {
        if (screenArea == null || screenArea.isEmpty()) {
            this.screenArea = new Rectangle();
        } else {
            this.screenArea = new Rectangle(screenArea);
        }
        if (fixedBoundsOnResize) {
            setTransforms(true);
        } else {
            setTransforms(false);
        }
    }

    /**
     * The coordinate reference system used for rendering the map. If not yet set, {@code null} is
     * returned.
     *
     * <p>The coordinate reference system used for rendering is often considered to be the "world"
     * coordinate reference system; this is distinct from the coordinate reference system used for
     * each layer (which is often data dependent).
     *
     * @return coordinate reference system used for rendering the map (may be {@code null}).
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        lock.readLock().lock();
        try {
            return bounds.getCoordinateReferenceSystem();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Set the {@code CoordinateReferenceSystem} for the viewport. If {@code crs} is null, the
     * existing reference system will be discarded.
     *
     * @param crs the new coordinate reference system, or {@code null} for no reference system
     */
    public void setCoordinateReferenceSystem(CoordinateReferenceSystem crs) {
        lock.writeLock().lock();
        try {
            if (checkEditable("setCoordinateReferenceSystem")) {
                if (crs == null) {
                    bounds = new ReferencedEnvelope(bounds, null);

                } else if (!CRS.equalsIgnoreMetadata(crs, bounds.getCoordinateReferenceSystem())) {
                    if (bounds.isEmpty()) {
                        bounds = new ReferencedEnvelope(crs);

                    } else {
                        try {
                            ReferencedEnvelope old = bounds;
                            bounds = bounds.transform(crs, true);
                            setTransforms(true);

                            fireMapBoundsListenerMapBoundsChanged(
                                    MapBoundsEvent.Type.CRS, old, bounds);

                        } catch (Exception e) {
                            LOGGER.log(Level.FINE, "Difficulty transforming to {0}", crs);
                        }
                    }
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /** Notifies MapBoundsListeners about a change to the bounds or crs. */
    protected void fireMapBoundsListenerMapBoundsChanged(
            Type type, ReferencedEnvelope oldBounds, ReferencedEnvelope newBounds) {

        if (boundsListeners == null) {
            return;
        }
        if (newBounds == bounds) {
            // issue a copy to the boundsListeners for safety
            newBounds = new ReferencedEnvelope(bounds);
        }
        MapBoundsEvent event = new MapBoundsEvent(this, type, oldBounds, newBounds);
        for (MapBoundsListener boundsListener : boundsListeners) {
            try {
                boundsListener.mapBoundsChanged(event);
            } catch (Throwable t) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.logp(
                            Level.FINE,
                            boundsListener.getClass().getName(),
                            "mapBoundsChanged",
                            t.getLocalizedMessage(),
                            t);
                }
            }
        }
    }

    /**
     * Gets the current screen to world coordinate transform.
     *
     * @return a copy of the current screen to world transform or {@code null} if the transform is
     *     not set
     */
    public AffineTransform getScreenToWorld() {
        lock.readLock().lock();
        try {
            return screenToWorld == null ? null : new AffineTransform(screenToWorld);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Gets the current world to screen coordinate transform.
     *
     * @return a copy of the current world to screen transform or {@code null} if the transform is
     *     not set
     */
    public AffineTransform getWorldToScreen() {
        lock.readLock().lock();
        try {
            return worldToScreen == null ? null : new AffineTransform(worldToScreen);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Sets the affine transforms used to convert between screen and world coordinates.
     *
     * <p>If screen area is undefined, the transforms are set to {@code null}.
     *
     * <p>If screen area is defined but not world bounds, the transforms are set to identity.
     *
     * <p>When both screen area and world bounds are defined, the transforms are set as follows. If
     * aspect ratio matching is enabled, the transforms transforms are calculated to centre the
     * world bounds in the screen area, after which the bounds will be adjusted if necessary to have
     * the same aspect ratio as the screen area. If aspect ratio matching is not enabled, basic
     * transforms are calculated without centering or bounds adjustment.
     *
     * @param newBounds indicates whether world bounds have just been changed
     */
    private void setTransforms(boolean newBounds) {
        if (screenArea.isEmpty()) {
            screenToWorld = worldToScreen = null;
            hasCenteringTransforms = false;

        } else if (bounds.isEmpty()) {
            screenToWorld = new AffineTransform();
            worldToScreen = new AffineTransform();
            hasCenteringTransforms = false;

        } else if (matchingAspectRatio) {
            if (newBounds || !hasCenteringTransforms) {
                calculateCenteringTransforms();
            }
            bounds = calculateActualBounds();

        } else {
            calculateSimpleTransforms(bounds);
            hasCenteringTransforms = false;
        }
    }

    /**
     * Calculates transforms suitable for aspect ratio matching. The world bounds will be centred in
     * the screen area.
     */
    private void calculateCenteringTransforms() {
        double xscale = screenArea.getWidth() / bounds.getWidth();
        double yscale = screenArea.getHeight() / bounds.getHeight();

        double scale = Math.min(xscale, yscale);

        double xoff = bounds.getMedian(0) * scale - screenArea.getCenterX();
        double yoff = bounds.getMedian(1) * scale + screenArea.getCenterY();

        worldToScreen = new AffineTransform(scale, 0, 0, -scale, -xoff, yoff);
        try {
            screenToWorld = worldToScreen.createInverse();

        } catch (NoninvertibleTransformException ex) {
            throw new RuntimeException("Unable to create coordinate transforms.", ex);
        }

        hasCenteringTransforms = true;
    }

    /**
     * Determine if the map retains its scale (false) or its bounds (true) when the MapPane is
     * resized.
     */
    public boolean isFixedBoundsOnResize() {
        return fixedBoundsOnResize;
    }
    /**
     * Determine if the map retains its scale (false) or its bounds (true) when the MapPane is
     * resized.
     *
     * @param fixedBoundsOnResize - if true retain bounds on resize otherwise retain scale.
     */
    public void setFixedBoundsOnResize(boolean fixedBoundsOnResize) {
        this.fixedBoundsOnResize = fixedBoundsOnResize;
    }

    /**
     * Calculates transforms suitable for no aspect ratio matching.
     *
     * @param requestedBounds requested display area in world coordinates
     */
    private void calculateSimpleTransforms(ReferencedEnvelope requestedBounds) {
        double xscale = screenArea.getWidth() / requestedBounds.getWidth();
        double yscale = screenArea.getHeight() / requestedBounds.getHeight();

        worldToScreen =
                new AffineTransform(
                        xscale,
                        0,
                        0,
                        -yscale,
                        -xscale * requestedBounds.getMinX(),
                        yscale * requestedBounds.getMaxY());
        try {
            screenToWorld = worldToScreen.createInverse();

        } catch (NoninvertibleTransformException ex) {
            throw new RuntimeException("Unable to create coordinate transforms.", ex);
        }
    }

    /** Calculates the world bounds of the current screen area. */
    private ReferencedEnvelope calculateActualBounds() {
        Point2D p0 = new Point2D.Double(screenArea.getMinX(), screenArea.getMinY());
        Point2D p1 = new Point2D.Double(screenArea.getMaxX(), screenArea.getMaxY());
        screenToWorld.transform(p0, p0);
        screenToWorld.transform(p1, p1);

        return new ReferencedEnvelope(
                Math.min(p0.getX(), p1.getX()),
                Math.max(p0.getX(), p1.getX()),
                Math.min(p0.getY(), p1.getY()),
                Math.max(p0.getY(), p1.getY()),
                bounds.getCoordinateReferenceSystem());
    }

    /**
     * Helper for setter methods which checks that this viewport is editable and issues a log
     * message if not.
     */
    private boolean checkEditable(String methodName) {
        final boolean state = editable.get();
        if (!state) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(
                        Level.FINE,
                        "Ignored call to {0} because viewport is not editable",
                        methodName);
            }
        }

        return state;
    }
}
