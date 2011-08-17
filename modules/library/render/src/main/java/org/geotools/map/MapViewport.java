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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.event.MapBoundsEvent;
import org.geotools.map.event.MapBoundsListener;
import org.geotools.map.event.MapBoundsEvent.Type;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Represents the area of a map to be displayed, expressed in world coordinates and (optionally)
 * screen (window, image) coordinates. A viewport is used to stage information for map rendering.
 * While the viewport provides support for bounds and coordinate reference system out of the box
 * it is expected that the user data support in {@code MapContent} will be used to record 
 * additional information such as elevation and time as required for rendering.
 * <p>
 * When both world and screen bounds are defined, the viewport calculates {@code AffineTransforms}
 * to convert the coordinates of one bounds to those of the other. It can also optionally adjust
 * the world bounds to maintain an identical aspect ratio with the screen bounds. Note however
 * that aspect ratio adjustment should not be enabled when the viewport is used with a service
 * such as WMS which mandates that specified screen and world bounds must be honoured exactly,
 * regardless of the resulting aspect ratio differences.
 * <p>
 * The {@code AffineTransforms} can be retrieved with the methods 
 * {@linkplain #getScreenToWorld()} and {@linkplain #getWorldToScreen()}.
 * The following rules apply to the return values of these methods:
 * <ul>
 * <li>
 * If screen area is not defined, {@code null} is returned.
 * </li>
 * <li>
 * If screen area only is defined, the identity transform is returned.
 * </li>
 * <li>
 * If both screen area and world extent are defined, calculated transforms are returned.
 * </li>
 * </ul>
 * 
 * @author Jody Garnett
 * @author Michael Bedward
 * @since 2.7
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/library/render/src/main/java/org/geotools/map/MapViewport.java $
 */
public class MapViewport {
    /** The logger for the map module. */
    static protected final Logger LOGGER = Logging.getLogger("org.geotools.map");

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
     * Creates a new view port. The viewport bounds, in both screen and world coordinates,
     * will be empty rectangles, a default coordinate reference system (WGS84) will
     * be set, and aspect ratio matching will not be enabled.
     */
    public MapViewport(){
        this(null);
    }
    
    /**
     * Creates a new view port with aspect ratio matching enabled or disabled according
     * to {@code matchAspectRatio}. The viewport bounds, in both screen and world coordinates,
     * will be empty rectangles and a default coordinate reference system (WGS84) will
     * be set.
     * 
     * @param matchAspectRatio whether to enable aspect ratio matching
     */
    public MapViewport(boolean matchAspectRatio) {
        this(null, matchAspectRatio);
    }

    /**
     * Creates a new view port with the specified display area in world coordinates.
     * The input envelope is copied so subsequent changes to it will not affect the
     * viewport.
     * <p>
     * The initial screen area will be empty and aspect ratio matching will not
     * be enabled.
     * 
     * @param bounds display area in world coordinates (may be {@code null})
     */
    public MapViewport(ReferencedEnvelope bounds){
        this(bounds, false);
    }
    
    /**
     * Creates a new view port  with the specified display area in world coordinates.
     * The input envelope is copied so subsequent changes to it will not affect the
     * viewport.
     * <p>
     * The initial screen area will be empty and aspect ratio matching will be enabled
     * or disabled according to {@code matchAspectRatio}.
     * 
     * @param bounds display area in world coordinates (may be {@code null})
     * @param matchAspectRatio whether to enable aspect ratio matching
     */
    public MapViewport(ReferencedEnvelope bounds, boolean matchAspectRatio) {
        this.screenArea = new Rectangle();
        this.hasCenteringTransforms = false;
        this.matchingAspectRatio = matchAspectRatio;
        
        if (bounds == null || bounds.isEmpty()) { 
            this.bounds = new ReferencedEnvelope(DefaultGeographicCRS.WGS84);
            
        } else {
            this.bounds = new ReferencedEnvelope(bounds);
        }
        
        setTransforms(true);
    }
    
    /**
     * Sets whether to adjust input world bounds to match the aspect
     * ratio of the screen area.
     * 
     * @param enabled whether to enable aspect ratio adjustment
     */
    public synchronized void setMatchingAspectRatio(boolean enabled) {
        if (enabled != matchingAspectRatio) {
            matchingAspectRatio = enabled;
            setTransforms(true);
        }
    }
    
    /**
     * Queries whether input worlds bounds will be adjusted to match the
     * aspect ratio of the screen area.
     * 
     * @return {@code true} if enabled
     */
    public boolean isMatchingAspectRatio() {
        return matchingAspectRatio;
    }

    /**
     * Used by client application to track the bounds of this viewport.
     * 
     * @param listener
     */
    public void addMapBoundsListener(MapBoundsListener listener) {
        if (boundsListeners == null) {
            synchronized ( this ){
                boundsListeners = new CopyOnWriteArrayList<MapBoundsListener>();
            }
        }
        if (!boundsListeners.contains(listener)) {
            boundsListeners.add(listener);
        }
    }

    public void removeMapBoundsListener(MapBoundsListener listener) {
        if (boundsListeners != null) {
            boundsListeners.remove(listener);
        }
    }

    /**
     * Checks if the view port bounds are empty (undefined). This will be
     * {@code true} if either or both of the world bounds and screen bounds
     * are empty.
     * 
     * @return {@code true} if empty
     */
    public boolean isEmpty() {
        return screenArea.isEmpty() || bounds.isEmpty();
    }
    
    /**
     * Gets the display area in world coordinates.
     * <p>
     * Note Well: this only covers spatial extent; you may wish to use the user data map
     * to record the current viewport time or elevation.
     * 
     * @return a copy of the current bounds
     */
    public synchronized ReferencedEnvelope getBounds() {
        return new ReferencedEnvelope(bounds);
    }
    
    /**
     * Sets the display area in world coordinates. 
     * <p>
     * If {@code bounds} is {@code null} or empty, default identity coordinate
     * transforms will be set. The viewport's existing coordinate reference system
     * will be preserved.
     * <p>
     * If {@code bounds} is not empty, and aspect ratio matching is enabled,
     * the coordinate transforms will be calculated to centre the requested bounds
     * in the current screen area (if defined), after which the world bounds will
     * be adjusted (enlarged) as required to match the screen area's aspect ratio.
     * 
     * @param requestedBounds the requested bounds (may be {@code null})
     */
    public synchronized void setBounds(ReferencedEnvelope requestedBounds) {
        ReferencedEnvelope old = this.bounds;
        if (requestedBounds == null || requestedBounds.isEmpty()) {
            bounds = new ReferencedEnvelope(this.bounds.getCoordinateReferenceSystem());
        } else {
            bounds = new ReferencedEnvelope(requestedBounds);
        }
        
        setTransforms(true);
        
        // Note the bounds communicated by the event are the actual world bounds
        // rather than the user-requested bounds (unless empty)
        fireMapBoundsListenerMapBoundsChanged(Type.BOUNDS, old, this.bounds);
    }

    /**
     * Gets a copy of the current screen area.
     * 
     * @return screen area to render into when drawing.
     */
    public synchronized Rectangle getScreenArea() {
        return new Rectangle(screenArea);
    }

    /**
     * Sets the display area in screen (window, image) coordinates.
     * 
     * @param screenArea display area in screen coordinates (may be {@code null})
     */
    public synchronized void setScreenArea(Rectangle screenArea) {
        if (screenArea == null || screenArea.isEmpty()) {
            this.screenArea = new Rectangle();
        } else {
            this.screenArea = new Rectangle(screenArea);
        }
        
        setTransforms(false);
    }
    
    /**
     * The coordinate reference system used for rendering the map.
     * <p>
     * The coordinate reference system used for rendering is often considered to be the "world"
     * coordinate reference system; this is distinct from the coordinate reference system used for
     * each layer (which is often data dependent).
     * </p>
     * 
     * @return coordinate reference system used for rendering the map.
     */
    public synchronized CoordinateReferenceSystem getCoordianteReferenceSystem() {
        return bounds == null ? null : bounds.getCoordinateReferenceSystem();
    }

    /**
     * Set the <code>CoordinateReferenceSystem</code> for this map's internal viewport.
     * 
     * @param crs
     * @throws FactoryException
     * @throws TransformException
     */
    public synchronized void setCoordinateReferenceSystem(CoordinateReferenceSystem crs) {
        if (bounds.getCoordinateReferenceSystem() != crs) {
            if (bounds.isEmpty()) {
                bounds = new ReferencedEnvelope(crs);
            } else {
                try {
                    ReferencedEnvelope old = bounds;
                    bounds = bounds.transform(crs, true);
                    setTransforms(true);
                    
                    fireMapBoundsListenerMapBoundsChanged(MapBoundsEvent.Type.CRS, old, bounds);
                    
                } catch (Exception e) {
                    LOGGER.log(Level.FINE, "Difficulty transforming to {0}", crs);
                }
            }
        }
    }

    /**
     * Notifies MapBoundsListeners about a change to the bounds or crs.
     * 
     * @param event
     *            The event to be fired
     */
    protected void fireMapBoundsListenerMapBoundsChanged(Type type, ReferencedEnvelope oldBounds,
            ReferencedEnvelope newBounds) {

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
                    LOGGER.logp(Level.FINE, boundsListener.getClass().getName(),
                            "mapBoundsChanged", t.getLocalizedMessage(), t);
                }
            }
        }
    }

    /**
     * Gets the current screen to world coordinate transform.
     * 
     * @return a copy of the current screen to world transform or
     *     {@code null} if the transform is not set
     */
    public synchronized AffineTransform getScreenToWorld() {
        return screenToWorld == null ? null : new AffineTransform(screenToWorld);
    }

    /**
     * Gets the current world to screen coordinate transform.
     * 
     * @return a copy of the current world to screen transform or
     *     {@code null} if the transform is not set
     */
    public synchronized AffineTransform getWorldToScreen() {
        return worldToScreen == null ? null : new AffineTransform(worldToScreen);
    }

    /**
     * Sets the affine transforms used to convert between screen
     * and world coordinates. 
     * <p>
     * If screen area is undefined, the transforms are set to {@code null}.
     * <p>
     * If screen area is defined but not world bounds, the transforms are set 
     * to identity. 
     * <p>
     * When both screen area and world bounds are defined, the transforms are
     * set as follows. If aspect ratio matching is enabled, the transforms
     * transforms are calculated to centre the world bounds in the
     * screen area, after which the bounds will be adjusted if necessary to have
     * the same aspect ratio as the screen area. If aspect ratio matching is not
     * enabled, basic transforms are calculated without centering or bounds 
     * adjustment.
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
     * Calculates transforms suitable for aspect ratio matching. The
     * world bounds will be centred in the screen area.
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
     * Calculates transforms suitable for no aspect ratio matching.
     * 
     * @param requestedBounds requested display area in world coordinates
     */
    private void calculateSimpleTransforms(ReferencedEnvelope requestedBounds) {
        double xscale = screenArea.getWidth() / requestedBounds.getWidth();
        double yscale = screenArea.getHeight() / requestedBounds.getHeight();
        double scale = Math.min(xscale, yscale);
        worldToScreen = new AffineTransform(scale, 0, 0, -scale, 
                -requestedBounds.getMinX(), requestedBounds.getMaxY());
        try {
            screenToWorld = worldToScreen.createInverse();

        } catch (NoninvertibleTransformException ex) {
            throw new RuntimeException("Unable to create coordinate transforms.", ex);
        }
    }

    /**
     * Calculates the world bounds of the current screen area.
     */
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
     * @todo MB: Not sure if this method should be used.
     * 
     * @param transform 
     */
    public void transform(AffineTransform transform) {
        ReferencedEnvelope old = this.bounds;

        double[] coords = new double[4];
        coords[0] = bounds.getMinX();
        coords[1] = bounds.getMinY();
        coords[2] = bounds.getMaxX();
        coords[3] = bounds.getMaxY();

        transform.transform(coords, 0, coords, 0, 2);

        this.bounds = new ReferencedEnvelope(coords[0], coords[2], coords[1], coords[3], bounds
                .getCoordinateReferenceSystem());

        fireMapBoundsListenerMapBoundsChanged(MapBoundsEvent.Type.BOUNDS, old, bounds);
    }

}
