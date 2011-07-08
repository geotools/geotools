package org.geotools.map;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.event.MapLayerEvent;
import org.geotools.map.event.MapLayerListener;
import org.geotools.util.logging.Logging;

/**
 * A Layer to be rendered.
 * <p>
 * Layers usually represent a single dataset; and arranged into a z-order by a Map for display.
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/render/src/main/java/org/geotools/map/Layer.java $
 */
public abstract class Layer {

    /** The logger for the map module. */
    static protected final Logger LOGGER = Logging.getLogger("org.geotools.map");

    /**
     * Human readable title for the layer.
     */
    protected String title;

    /**
     * Flag to mark the layer as visible when being rendered
     */
    protected boolean visible = true;

    /**
     * Map of application supplied information.
     */
    protected Map<String, Object> userData;

    /**
     * Listeners to be notified when layer contents change.
     */
    protected CopyOnWriteArrayList<MapLayerListener> listenerList;

    /**
     * Notifies all registered listeners about the event.
     * 
     * @param event
     *            The event to be fired
     */
    protected void fireMapLayerListenerLayerChanged(int eventType) {
        if (listenerList == null) {
            return;
        }
        MapLayerEvent event = new MapLayerEvent(this, eventType);
        for (MapLayerListener mapListener : listenerList) {
            try {
                mapListener.layerChanged(event);
            } catch (Throwable t) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.log(Level.FINER, "Layer Event failure:" + t, t);
                }
            }
        }
    }

    /**
     * Notifies all registered listeners about the event.
     * 
     * @param event
     *            The event to be fired
     */
    protected void fireMapLayerListenerLayerShown() {
        if (listenerList == null) {
            return;
        }
        MapLayerEvent event = new MapLayerEvent(this, MapLayerEvent.VISIBILITY_CHANGED);
        for (MapLayerListener mapListener : listenerList) {
            try {
                mapListener.layerShown(event);
            } catch (Throwable t) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.log(Level.FINER, "Layer Event failure:" + t, t);
                }
            }
        }
    }

    /**
     * Notifies all registered listeners about the event.
     * 
     * @param event
     *            The event to be fired
     */
    protected void fireMapLayerListenerLayerHidden() {
        if (listenerList == null) {
            return;
        }
        MapLayerEvent event = new MapLayerEvent(this, MapLayerEvent.VISIBILITY_CHANGED);
        for (MapLayerListener mapListener : listenerList) {
            try {
                mapListener.layerHidden(event);
            } catch (Throwable t) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.log(Level.FINER, "Layer Event failure:" + t, t);
                }
            }
        }
    }

    /**
     * Notifies all registered listeners about the selection event.
     * 
     * @param event
     *            The event to be fired
     */
    protected void fireMapLayerListenerLayerSelected() {
        if (listenerList == null) {
            return;
        }
        MapLayerEvent event = new MapLayerEvent(this, MapLayerEvent.SELECTION_CHANGED);
        for (MapLayerListener mapListener : listenerList) {
            try {
                mapListener.layerSelected(event);
            } catch (Throwable t) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.log(Level.FINER, "Layer Event failure:" + t, t);
                }
            }
        }
    }

    /**
     * Notifies all registered listeners about the deselection event.
     * 
     * @param event
     *            The event to be fired
     */
    protected void fireMapLayerListenerLayerDeselected() {
        if (listenerList == null) {
            return;
        }
        MapLayerEvent event = new MapLayerEvent(this, MapLayerEvent.SELECTION_CHANGED);
        for (MapLayerListener mapListener : listenerList) {
            try {
                mapListener.layerDeselected(event);
            } catch (Throwable t) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.log(Level.FINER, "Layer Event failure:" + t, t);
                }
            }
        }
    }

    /**
     * Layer creation; please use a concrete subclass to work with specific content.
     * <p>
     * Note you should dispose() a layer after use.
     */
    protected Layer() {
    }

    @Override
    protected void finalize() throws Throwable {
        if (listenerList != null) {
            LOGGER.severe("Layer dispose not called; possible memory leak");
            dispose();
        }
        super.finalize();
    }

    /**
     * Allows a Layer to clean up any listeners, or internal caches or resources it has added during
     * use.
     */
    public void dispose() {
        if (listenerList != null) {
            listenerList.clear();
            listenerList = null;
        }
    }

    /**
     * Get the title of this layer. If title has not been defined then an empty string is returned.
     * 
     * @return The title of this layer.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of this layer. A {@link LayerEvent} is fired if the new title is different from
     * the previous one.
     * 
     * @param title
     *            The title of this layer.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Determine whether this layer is visible on a map pane or whether the layer is hidden.
     * 
     * @return <code>true</code> if the layer is visible, or <code>false</code> if the layer is
     *         hidden.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Specify whether this layer is visible on a map pane or whether the layer is hidden. A
     * {@link LayerEvent} is fired if the visibility changed.
     * 
     * @param visible
     *            Show the layer if <code>true</code>, or hide the layer if <code>false</code>
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Application supplied information; may be used as a scatch pad for information such selection.
     * <p>
     * Example:
     * 
     * <pre>
     * layer.getUserData().put(&quot;selectable&quot;, true)
     * </pre>
     * 
     * @return Application parameters
     */
    public synchronized java.util.Map<String, Object> getUserData() {
        if (userData == null) {
            userData = new HashMap<String, Object>();
        }
        return this.userData;
    }

    /**
     * The bounds of the Layer content (if known). The bounds can be used to determine if any of the
     * layers content is "on screen" when rendering the map; however often it is expensive to
     * calculate a layers bounds up front so we are allowing this value to be optional.
     * <p>
     * The returned bounds are a ReferencedEnvelope using the same CoordinateReferenceSystem as the
     * layers contents.
     * 
     * @return layer bounds, null if unknown
     */
    public abstract ReferencedEnvelope getBounds();

    /**
     * The listener is notified when the layer information changes; or when the contents of the
     * layer changes. This is used by interactive map displays to detect when information is
     * modified and needs to be redrawn.
     * 
     * @param listener
     *            The listener to add to the listener list.
     */
    public synchronized void addMapLayerListener(MapLayerListener listener) {
        if (listenerList == null) {
            listenerList = new CopyOnWriteArrayList<MapLayerListener>();
        }
        boolean added = listenerList.addIfAbsent(listener);
        if (added && listenerList.size() == 1) {
            // when first listener added we can start listening to data
            connectDataListener(true);
        }
    }

    /**
     * Removes a listener from the listener list for this layer.
     * 
     * @param listener
     *            The listener to remove from the listener list.
     */
    public synchronized void removeMapLayerListener(MapLayerListener listener) {
        if (listenerList != null) {
            listenerList.remove(listener);
            if (listenerList.isEmpty()) {
                connectDataListener(false);
            }
        }
    }

    /**
     * Called in an interactive environment where a Layer is expected to listen to the data source
     * on behalf of the application.
     * <p>
     * This method is called as needed by addMapListener and removeMapListener to allow subclasses
     * to connect any data listeners required in order to issue fireMapLayerChanged events.
     * 
     * @param listen
     *            true to connect, false to disconnect
     */
    protected void connectDataListener(boolean listen) {
    }

    //
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(getClass().getName());
        buf.append("[");
        if (title != null && title.length() != 0) {
            buf.append(getTitle());
        }
        if (visible) {
            buf.append(", VISIBLE");
        } else {
            buf.append(", HIDDEN");
        }
        buf.append("]");
        return buf.toString();
    }
}
