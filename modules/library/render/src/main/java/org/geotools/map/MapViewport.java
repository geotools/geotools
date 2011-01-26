package org.geotools.map;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.event.MapBoundsEvent;
import org.geotools.map.event.MapBoundsListener;
import org.geotools.map.event.MapBoundsEvent.Type;
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
 */
public class MapViewport {
    /** The logger for the map module. */
    static protected final Logger LOGGER = Logging.getLogger("org.geotools.map");

    private Rectangle screenArea;
    
    private ReferencedEnvelope bounds;

    private CopyOnWriteArrayList<MapBoundsListener> boundsListeners;

    /**
     * Used by client application to track the bounds of this viewport.
     * 
     * @param listener
     */
    public void addMapBoundsListener(MapBoundsListener listener) {
        if (boundsListeners == null) {
            boundsListeners = new CopyOnWriteArrayList<MapBoundsListener>();
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
     * The extent of the map to render (this is the main attribute of the map viewport).
     * <p>
     * Note Well: The bounds should match your screen aspect ratio (or the map will appear
     * squashed). Please note this only covers spatial extent; you may wish to use the user data map
     * to record the current viewport time or elevation.
     */
    public ReferencedEnvelope getBounds() {
        return bounds;
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
                    fireMapBoundsListenerMapBoundsChanged(MapBoundsEvent.Type.BOUNDS, old, bounds);
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

    public void setBounds(ReferencedEnvelope bounds) {
        ReferencedEnvelope old = this.bounds;
        this.bounds = bounds;
        fireMapBoundsListenerMapBoundsChanged(Type.BOUNDS, old, bounds);
    }

    /**
     * Screen area to render into when drawing.
     * @return screen area to render into when drawing.
     */
    public Rectangle getScreenArea() {
        return screenArea;
    }
    
    /**
     * Screen area to render into when drawing.
     * @param screenArea
     */
    public void setScreenArea(Rectangle screenArea) {
        // we could consider updating the bounds to have the correct aspect ratio
        // matching this screen area?
        this.screenArea = screenArea;
    }
    
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
