package org.geotools.map;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.event.MapBoundsEvent;
import org.geotools.map.event.MapBoundsListener;
import org.geotools.map.event.MapBoundsEvent.Type;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

/**
 * Represents the area of a map to be displayed.
 * <p>
 * A viewport is used to stage information for map rendering; while the viewport provides support
 * for bounds and coordinate reference system out of the box it is expected that the user data
 * support is used to record additional information such as elevation and time as required for
 * rendering.
 * 
 * @author Jody
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

    /**
     * Creates a new view port. The viewport bounds, in both screen and world coordinates,
     * will be empty rectangles and a default coordinate reference system (WGS84) will
     * be set.
     */
    public MapViewport(){
        setEmptyBounds();
    }

    /**
     * Creates a new view port with the specified display area in world coordinates.
     * If {@code bounds} is {@code null} any current bounds are cleared and 
     * {@link #isEmpty()} will return {@code true}.
     * 
     * @param bounds display area in world coordinates
     */
    public MapViewport(ReferencedEnvelope bounds){
        if (bounds == null) { 
            setEmptyBounds();
        } else {
            // copy the input bounds defensively
            this.bounds = new ReferencedEnvelope(bounds);
            setTransforms();
        }
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
     * Checks if the view port bounds are empty (undefined).
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
    public ReferencedEnvelope getBounds() {
        return new ReferencedEnvelope(bounds);
    }

    /**
     * Sets the display area in world coordinates. If {@code bounds} is not
     * {@code null} or empty, and the current screen area is not empty,
     * the viewport's coordinate transforms will be recalculated.
     * 
     * @param bounds the new bounds (may be {@code null}
     */
    public void setBounds(ReferencedEnvelope bounds) {
        ReferencedEnvelope old = this.bounds;
        if (bounds == null) {
            this.bounds = new ReferencedEnvelope(this.bounds.getCoordinateReferenceSystem());
            setDefaultTransforms();
            
        } else {
            // defensive copy
            this.bounds = new ReferencedEnvelope(bounds);
            setTransforms();
        }
        fireMapBoundsListenerMapBoundsChanged(Type.BOUNDS, old, this.bounds);
    }

    /**
     * Screen area to render into when drawing.
     * @return screen area to render into when drawing.
     */
    public Rectangle getScreenArea() {
        return screenArea;
    }

    /**
     * Sets the display area in screen (window, image) coordinates. If {@code screenArea}
     * is not {@code null} or empty, and the current world bounds are not empty,
     * the viewport's coordinate transforms will be recalculated.
     * 
     * @param screenArea display area in screen coordinates (may be {@code null})
     */
    public void setScreenArea(Rectangle screenArea) {
        if (screenArea == null) {
            this.screenArea = new Rectangle();
            setDefaultTransforms();
            
        } else {
            // defensive copy
            this.screenArea = new Rectangle(screenArea);
            setTransforms();
        }
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
    public CoordinateReferenceSystem getCoordianteReferenceSystem() {
        return bounds == null ? null : bounds.getCoordinateReferenceSystem();
    }

    /**
     * Set the <code>CoordinateReferenceSystem</code> for this map's internal viewport.
     * 
     * @param crs
     * @throws FactoryException
     * @throws TransformException
     */
    public void setCoordinateReferenceSystem(CoordinateReferenceSystem crs) {
        if( bounds == null ){
            bounds = new ReferencedEnvelope(crs);
        }
        else if (bounds.getCoordinateReferenceSystem() != crs) {
            if (bounds != null) {
                try {
                    ReferencedEnvelope old = bounds;
                    bounds = bounds.transform(crs, true);
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
     * Gets the current screen to world coordinate transform. If 
     * the display area is empty the identity transform is returned.
     * 
     * @return a copy of the current screen to world transform
     */
    public AffineTransform getScreenToWorld() {
        return new AffineTransform(screenToWorld);
    }

    /**
     * Gets the current world to screen coordinate transform. If 
     * the display area is empty the identity transform is returned.
     * 
     * @return a copy of the current world to screen transform
     */
    public AffineTransform getWorldToScreen() {
        return new AffineTransform(worldToScreen);
    }

    /**
     * Sets the screen and world bounds to empty rectangles and the 
     * coordinate reference system to WGS84.
     */
    private void setEmptyBounds() {
        bounds = new ReferencedEnvelope(DefaultGeographicCRS.WGS84);
        screenArea = new Rectangle();
        setDefaultTransforms();
    }

    /**
     * Calculates the affine transforms used to convert between screen
     * and world coordinates. Transforms are calculated such that the
     * current world bounds are centred in the screen area.
     */
    private void setTransforms() {
        if (!(bounds.isEmpty() || screenArea.isEmpty())) {
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
        }
    }

    /**
     * Sets the transforms to the default identity transforms.
     */
    private void setDefaultTransforms() {
        screenToWorld = new AffineTransform();
        worldToScreen = new AffineTransform();
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
