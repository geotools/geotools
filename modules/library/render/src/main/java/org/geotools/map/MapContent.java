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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Stores the contents of a map for display, including a list of layers, a {@linkplain MapViewport}
 * defining the device and world bounds of display area, and optional user data.
 *
 * <p>Methods are provided to add, remove and reorder layers. Alternatively, the list of layers can
 * be accessed directly with the {@linkplain #layers()}. For example:
 *
 * <pre><code>
 * mapContent.layers().add( newLayer );
 * </code></pre>
 *
 * Operations on the list returned by the {@code layers{}} method are guaranteed to be thread safe,
 * and modifying the list contents will result in {@code MapLayerListEvents} being published.
 *
 * <p>Note: This object is similar to early drafts of the OGC Open Web Service Context
 * specification.
 *
 * @author Jody Garnett
 * @since 2.7
 * @version $Id$
 */
public class MapContent {

    /** The logger for the map module. */
    protected static final Logger LOGGER = Logging.getLogger(MapContent.class);

    static final String UNDISPOSED_MAPCONTENT_ERROR =
            "Call MapContent dispose() to prevent memory leaks";

    /** List of Layers to be rendered */
    private final LayerList layerList;

    /** MapLayerListListeners to be notified in the event of change */
    private CopyOnWriteArrayList<MapLayerListListener> mapListeners;

    /** Map used to hold application specific information */
    private HashMap<String, Object> userData;

    /** Map title */
    private String title;

    /** PropertyListener list used for notifications */
    private CopyOnWriteArrayList<PropertyChangeListener> propertyListeners;

    /**
     * Viewport for map rendering.
     *
     * <p>While the map maintains one viewport internally to better reflect a map context document
     * you are free to maintain a separate viewport; or indeed construct many viewports representing
     * tiles to be rendered.
     */
    protected MapViewport viewport;

    /** Listener used to watch individual layers and report changes to MapLayerListListeners */
    private MapLayerListener layerListener;

    private final ReadWriteLock monitor;

    /** Creates a new map content. */
    public MapContent() {
        layerList = new LayerList();
        monitor = new ReentrantReadWriteLock();
    }

    /** Checks that dispose has been called; producing a warning if needed. */
    @Override
    @SuppressWarnings("deprecation") // finalize is deprecated in Java 9
    protected void finalize() throws Throwable {
        if (this.layerList != null) {
            if (!this.layerList.isEmpty()) {
                LOGGER.severe(UNDISPOSED_MAPCONTENT_ERROR);
            }
        }
        super.finalize();
    }

    /**
     * Clean up any listeners or cached state associated with this MapContent.
     *
     * <p>Please note that open connections (FeatureSources and GridCoverage readers) are the
     * responsibility of your application and are not cleaned up by this method.
     */
    public void dispose() {
        for (Layer layer : layerList) {
            if (layer != null) {
                // Layer.dispose will inform listeners of the impending
                // disposal, then remove listeners from this layer
                layer.preDispose();
                layer.dispose();
            }
        }
        layerList.clear();

        if (this.mapListeners != null) {
            this.mapListeners.clear();
            this.mapListeners = null;
        }

        if (this.layerListener != null) {
            this.layerListener = null;
        }

        if (this.propertyListeners != null) {
            this.propertyListeners.clear();
            this.propertyListeners = null;
        }

        this.title = null;
        if (this.userData != null) {
            // remove property listeners prior to removing userData
            this.userData.clear();
            this.userData = null;
        }
    }

    /**
     * Register interest in receiving a {@link LayerListEvent}. A <code>LayerListEvent</code> is
     * sent if a layer is added or removed, but not if the data within a layer changes.
     *
     * @param listener The object to notify when Layers have changed.
     */
    public void addMapLayerListListener(MapLayerListListener listener) {
        monitor.writeLock().lock();
        try {
            if (mapListeners == null) {
                mapListeners = new CopyOnWriteArrayList<MapLayerListListener>();
            }
            boolean added = mapListeners.addIfAbsent(listener);
            if (added && mapListeners.size() == 1) {
                listenToMapLayers(true);
            }
        } finally {
            monitor.writeLock().unlock();
        }
    }

    /**
     * Listen to the map layers; passing any events on to our own mapListListeners.
     *
     * <p>This method only has an effect if we have any actuall mapListListeners.
     *
     * @param listen True to connect to all the layers and listen to events
     */
    protected void listenToMapLayers(boolean listen) {
        monitor.writeLock().lock();
        try {
            if (mapListeners == null || mapListeners.isEmpty()) {
                return; // not worth listening nobody is interested
            }
            if (layerListener == null) {
                layerListener =
                        new MapLayerListener() {

                            @Override
                            public void layerShown(MapLayerEvent event) {
                                Layer layer = (Layer) event.getSource();
                                int index = layerList.indexOf(layer);
                                fireLayerEvent(layer, index, event);
                            }

                            @Override
                            public void layerSelected(MapLayerEvent event) {
                                Layer layer = (Layer) event.getSource();
                                int index = layerList.indexOf(layer);
                                fireLayerEvent(layer, index, event);
                            }

                            @Override
                            public void layerHidden(MapLayerEvent event) {
                                Layer layer = (Layer) event.getSource();
                                int index = layerList.indexOf(layer);
                                fireLayerEvent(layer, index, event);
                            }

                            @Override
                            public void layerDeselected(MapLayerEvent event) {
                                Layer layer = (Layer) event.getSource();
                                int index = layerList.indexOf(layer);
                                fireLayerEvent(layer, index, event);
                            }

                            @Override
                            public void layerChanged(MapLayerEvent event) {
                                Layer layer = (Layer) event.getSource();
                                int index = layerList.indexOf(layer);
                                fireLayerEvent(layer, index, event);
                            }

                            @Override
                            public void layerPreDispose(MapLayerEvent event) {
                                Layer layer = (Layer) event.getSource();
                                int index = layerList.indexOf(layer);
                                fireLayerEvent(layer, index, event);
                            }
                        };
            }
            if (listen) {
                for (Layer layer : layerList) {
                    layer.addMapLayerListener(layerListener);
                }
            } else {
                for (Layer layer : layerList) {
                    layer.removeMapLayerListener(layerListener);
                }
            }
        } finally {
            monitor.writeLock().unlock();
        }
    }

    /**
     * Remove interest in receiving {@link LayerListEvent}.
     *
     * @param listener The object to stop sending <code>LayerListEvent</code>s.
     */
    public void removeMapLayerListListener(MapLayerListListener listener) {
        monitor.writeLock().lock();
        try {
            if (mapListeners != null) {
                mapListeners.remove(listener);
            }
        } finally {
            monitor.writeLock().unlock();
        }
    }

    /**
     * Add a new layer (if not already present).
     *
     * <p>In an interactive setting this will trigger a {@link LayerListEvent}
     *
     * @return true if the layer was added
     */
    public boolean addLayer(Layer layer) {
        monitor.writeLock().lock();
        try {
            return layerList.addIfAbsent(layer);
        } finally {
            monitor.writeLock().unlock();
        }
    }

    /**
     * Adds all layers from the input collection that are not already present in this map content.
     *
     * @param layers layers to add (may be {@code null} or empty)
     * @return the number of layers added
     */
    public int addLayers(Collection<? extends Layer> layers) {
        monitor.writeLock().lock();
        try {
            if (layers == null || layers.isEmpty()) {
                return 0;
            }

            return layerList.addAllAbsent(layers);

        } finally {
            monitor.writeLock().unlock();
        }
    }

    /**
     * Removes the given layer, if present, and publishes a {@linkplain MapLayerListEvent}.
     *
     * @param layer the layer to be removed
     * @return {@code true} if the layer was removed
     */
    public boolean removeLayer(Layer layer) {
        monitor.writeLock().lock();
        try {
            return layerList.remove(layer);
        } finally {
            monitor.writeLock().unlock();
        }
    }

    /**
     * Moves a layer in the layer list. Will fire a MapLayerListEvent.
     *
     * @param sourcePosition existing position of the layer
     * @param destPosition new position of the layer
     */
    public void moveLayer(int sourcePosition, int destPosition) {
        monitor.writeLock().lock();
        try {
            layerList.move(sourcePosition, destPosition);
        } finally {
            monitor.writeLock().unlock();
        }
    }

    /**
     * Gets the list of layers for this map content. The returned list has the following
     * characteristics:
     *
     * <ul>
     *   <li>It is "live", ie. changes to its contents will be reflected in this map content.
     *   <li>It is thread-safe. Accessing list elements directly or via a {@linkplain
     *       java.util.ListIterator} returns a snapshot view of the list contents (as per Java's
     *       {@linkplain CopyOnWriteArrayList} class).
     *   <li>Adding a layer to the list, or removing a layer from it, results in a {@linkplain
     *       MapLayerListEvent} being published by the map content.
     * </ul>
     *
     * For these reasons, you should always work directly with the list returned by this method and
     * avoid making copies since they will not have the above behaviour.
     *
     * @return a "live" reference to the layer list for this map content
     */
    public List<Layer> layers() {
        monitor.readLock().lock();
        try {
            return layerList;
        } finally {
            monitor.readLock().unlock();
        }
    }

    protected void fireLayerAdded(Layer element, int fromIndex, int toIndex) {
        monitor.readLock().lock();
        try {
            if (mapListeners == null) {
                return;
            }
            MapLayerListEvent event = new MapLayerListEvent(this, element, fromIndex, toIndex);
            for (MapLayerListListener mapLayerListListener : mapListeners) {
                try {
                    mapLayerListListener.layerAdded(event);
                } catch (Throwable t) {
                    if (LOGGER.isLoggable(Level.FINER)) {
                        LOGGER.logp(
                                Level.FINE,
                                mapLayerListListener.getClass().getName(),
                                "layerAdded",
                                t.getLocalizedMessage(),
                                t);
                    }
                }
            }
        } finally {
            monitor.readLock().unlock();
        }
    }

    protected void fireLayerRemoved(Layer element, int fromIndex, int toIndex) {
        monitor.readLock().lock();
        try {
            if (mapListeners == null) {
                return;
            }
            MapLayerListEvent event = new MapLayerListEvent(this, element, fromIndex, toIndex);
            for (MapLayerListListener mapLayerListListener : mapListeners) {
                try {
                    mapLayerListListener.layerRemoved(event);
                } catch (Throwable t) {
                    if (LOGGER.isLoggable(Level.FINER)) {
                        LOGGER.logp(
                                Level.FINE,
                                mapLayerListListener.getClass().getName(),
                                "layerAdded",
                                t.getLocalizedMessage(),
                                t);
                    }
                }
            }
        } finally {
            monitor.readLock().unlock();
        }
    }

    protected void fireLayerMoved(Layer element, int toIndex) {
        monitor.readLock().lock();
        try {
            if (mapListeners == null) {
                return;
            }
            MapLayerListEvent event = new MapLayerListEvent(this, element, toIndex);
            for (MapLayerListListener mapLayerListListener : mapListeners) {
                try {
                    mapLayerListListener.layerMoved(event);
                } catch (Throwable t) {
                    if (LOGGER.isLoggable(Level.FINER)) {
                        LOGGER.logp(
                                Level.FINE,
                                mapLayerListListener.getClass().getName(),
                                "layerMoved",
                                t.getLocalizedMessage(),
                                t);
                    }
                }
            }
        } finally {
            monitor.readLock().unlock();
        }
    }

    protected void fireLayerPreDispose(Layer element, int toIndex) {
        monitor.readLock().lock();
        try {
            if (mapListeners == null) {
                return;
            }
            MapLayerListEvent event = new MapLayerListEvent(this, element, toIndex);
            for (MapLayerListListener mapLayerListListener : mapListeners) {
                try {
                    mapLayerListListener.layerPreDispose(event);
                } catch (Throwable t) {
                    if (LOGGER.isLoggable(Level.FINER)) {
                        LOGGER.logp(
                                Level.FINE,
                                mapLayerListListener.getClass().getName(),
                                "layerMoved",
                                t.getLocalizedMessage(),
                                t);
                    }
                }
            }
        } finally {
            monitor.readLock().unlock();
        }
    }

    protected void fireLayerEvent(Layer element, int index, MapLayerEvent layerEvent) {
        monitor.readLock().lock();
        try {
            if (mapListeners == null) {
                return;
            }
            MapLayerListEvent mapEvent = new MapLayerListEvent(this, element, index, layerEvent);
            for (MapLayerListListener mapLayerListListener : mapListeners) {
                try {
                    switch (layerEvent.getReason()) {
                        case MapLayerEvent.PRE_DISPOSE:
                            mapLayerListListener.layerPreDispose(mapEvent);
                            break;

                        default:
                            mapLayerListListener.layerChanged(mapEvent);
                    }
                } catch (Throwable t) {
                    if (LOGGER.isLoggable(Level.FINER)) {
                        LOGGER.logp(
                                Level.FINE,
                                mapLayerListListener.getClass().getName(),
                                "layerAdded",
                                t.getLocalizedMessage(),
                                t);
                    }
                }
            }
        } finally {
            monitor.readLock().unlock();
        }
    }

    /**
     * Get the bounding box of all the layers in this Map. If all the layers cannot determine the
     * bounding box in the speed required for each layer, then null is returned. The bounds will be
     * expressed in the Map coordinate system.
     *
     * @return The bounding box of the features or null if unknown and too expensive for the method
     *     to calculate.
     * @throws IOException if an IOException occurs while accessing the FeatureSource bounds
     */
    public ReferencedEnvelope getMaxBounds() {
        monitor.readLock().lock();
        try {
            CoordinateReferenceSystem mapCrs = null;
            if (viewport != null) {
                mapCrs = viewport.getCoordinateReferenceSystem();
            }
            ReferencedEnvelope maxBounds = null;

            for (Layer layer : layerList) {
                if (layer == null) {
                    continue;
                }
                try {
                    ReferencedEnvelope layerBounds = layer.getBounds();
                    if (layerBounds == null || layerBounds.isEmpty() || layerBounds.isNull()) {
                        continue;
                    }
                    if (mapCrs == null) {
                        // crs for the map is not defined; let us start with the first CRS we see
                        // then!
                        maxBounds = new ReferencedEnvelope(layerBounds);
                        mapCrs = layerBounds.getCoordinateReferenceSystem();
                        continue;
                    }
                    ReferencedEnvelope normalized;
                    if (CRS.equalsIgnoreMetadata(
                            mapCrs, layerBounds.getCoordinateReferenceSystem())) {
                        normalized = layerBounds;
                    } else {
                        try {
                            normalized = layerBounds.transform(mapCrs, true);
                        } catch (Exception e) {
                            LOGGER.log(Level.FINE, "Unable to transform: {0}", e);
                            continue;
                        }
                    }
                    if (maxBounds == null) {
                        maxBounds = normalized;
                    } else {
                        maxBounds.expandToInclude(normalized);
                    }
                } catch (Throwable eek) {
                    LOGGER.log(Level.WARNING, "Unable to determine bounds of " + layer, eek);
                }
            }
            if (maxBounds == null) {
                maxBounds = new ReferencedEnvelope(mapCrs);
            }

            return maxBounds;

        } finally {
            monitor.readLock().unlock();
        }
    }

    //
    // Viewport Information
    //

    /**
     * Viewport describing the area visible on screen.
     *
     * <p>Applications may create multiple viewports (perhaps to render tiles of content); the
     * viewport recorded here is intended for interactive applications where it is helpful to have a
     * single viewport representing what the user is seeing on screen.
     *
     * <p>With that in mind; if the user has not already supplied a viewport one will be created:
     *
     * <ul>
     *   <li>The viewport will be configured to show the extent of the current layers as provided by
     *       {@link #getMaxBounds()}.
     *   <li>The viewport will have an empty {@link MapViewport#getBounds()} if no layers have been
     *       added yet.
     * </ul>
     *
     * @return MapViewport describing how to draw this map
     */
    public MapViewport getViewport() {
        monitor.readLock().lock();
        try {
            if (viewport == null) {
                viewport = new MapViewport(getMaxBounds());
            }
            return viewport;
        } finally {
            monitor.readLock().unlock();
        }
    }

    /**
     * Sets the viewport for this map content. The {@code viewport} argument may be {@code null}, in
     * which case a subsequent to {@linkplain #getViewport()} will return a new instance with
     * default settings.
     *
     * @param viewport the new viewport
     */
    public void setViewport(MapViewport viewport) {
        monitor.writeLock().lock();
        try {
            this.viewport = viewport;
        } finally {
            monitor.writeLock().unlock();
        }
    }

    /**
     * Register interest in receiving {@link MapBoundsEvent}s.
     *
     * @param listener The object to notify when the area of interest has changed.
     */
    public void addMapBoundsListener(MapBoundsListener listener) {
        monitor.writeLock().lock();
        try {
            getViewport().addMapBoundsListener(listener);
        } finally {
            monitor.writeLock().unlock();
        }
    }

    /**
     * Remove interest in receiving a {@link BoundingBoxEvent}s.
     *
     * @param listener The object to stop sending change events.
     */
    public void removeMapBoundsListener(MapBoundsListener listener) {
        monitor.writeLock().lock();
        try {
            getViewport().removeMapBoundsListener(listener);
        } finally {
            monitor.writeLock().unlock();
        }
    }

    /**
     * The extent of the map currently (sometimes called the map "viewport".
     *
     * <p>Note Well: The bounds should match your screen aspect ratio (or the map will appear
     * squashed). Please note this only covers spatial extent; you may wish to use the user data map
     * to record the current viewport time or elevation.
     */
    ReferencedEnvelope getBounds() {
        monitor.readLock().lock();
        try {
            return getViewport().getBounds();
        } finally {
            monitor.readLock().unlock();
        }
    }

    /**
     * The coordinate reference system used for rendering the map.
     *
     * <p>The coordinate reference system used for rendering is often considered to be the "world"
     * coordinate reference system; this is distinct from the coordinate reference system used for
     * each layer (which is often data dependent).
     *
     * @return coordinate reference system used for rendering the map.
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        monitor.readLock().lock();
        try {
            return getViewport().getCoordinateReferenceSystem();
        } finally {
            monitor.readLock().unlock();
        }
    }

    /** Set the <code>CoordinateReferenceSystem</code> for this map's internal viewport. */
    void setCoordinateReferenceSystem(CoordinateReferenceSystem crs) {
        monitor.writeLock().lock();
        try {
            getViewport().setCoordinateReferenceSystem(crs);
        } finally {
            monitor.writeLock().unlock();
        }
    }

    //
    // Properties
    //
    /**
     * Registers PropertyChangeListener to receive events.
     *
     * @param listener The listener to register.
     */
    public void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
        monitor.writeLock().lock();
        try {
            if (propertyListeners == null) {
                propertyListeners = new CopyOnWriteArrayList<java.beans.PropertyChangeListener>();
            }
            if (!propertyListeners.contains(listener)) {
                propertyListeners.add(listener);
            }

        } finally {
            monitor.writeLock().unlock();
        }
    }

    /**
     * Removes PropertyChangeListener from the list of listeners.
     *
     * @param listener The listener to remove.
     */
    public void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
        monitor.writeLock().lock();
        try {
            if (propertyListeners != null) {
                propertyListeners.remove(listener);
            }

        } finally {
            monitor.writeLock().unlock();
        }
    }

    /**
     * As an example it can be used to record contact details, map abstract, keywords and so forth
     * for an OGC "Open Web Service Context" document.
     *
     * <p>Modifications to the userData will result in a propertyChange event.
     */
    public java.util.Map<String, Object> getUserData() {
        monitor.writeLock().lock();
        try {
            if (userData == null) {
                userData =
                        new HashMap<String, Object>() {
                            private static final long serialVersionUID = 8011733882551971475L;

                            @Override
                            public Object put(String key, Object value) {
                                Object old = super.put(key, value);
                                fireProperty(key, old, value);
                                return old;
                            }

                            @Override
                            public Object remove(Object key) {
                                Object old = super.remove(key);
                                fireProperty((String) key, old, null);
                                return old;
                            }

                            @Override
                            public void putAll(
                                    java.util.Map<? extends String, ? extends Object> m) {
                                super.putAll(m);
                                fireProperty("userData", null, null);
                            }

                            @Override
                            public void clear() {
                                super.clear();
                                fireProperty("userData", null, null);
                            }
                        };
            }
            return this.userData;
        } finally {
            monitor.writeLock().unlock();
        }
    }

    /**
     * Get the title, returns an empty string if it has not been set yet.
     *
     * @return the title, or an empty string if it has not been set.
     */
    public String getTitle() {
        monitor.readLock().lock();
        try {
            return title;
        } finally {
            monitor.readLock().unlock();
        }
    }

    /**
     * Set the title of this context.
     *
     * @param title the title.
     */
    public void setTitle(String title) {
        monitor.writeLock().lock();
        try {
            String old = this.title;
            this.title = title;
            fireProperty("title", old, title);

        } finally {
            monitor.writeLock().unlock();
        }
    }

    protected void fireProperty(String propertyName, Object old, Object value) {
        monitor.readLock().lock();
        try {
            if (propertyListeners == null) {
                return;
            }
            PropertyChangeEvent event = new PropertyChangeEvent(this, "propertyName", old, value);
            for (PropertyChangeListener propertyChangeListener : propertyListeners) {
                try {
                    propertyChangeListener.propertyChange(event);
                } catch (Throwable t) {
                    if (LOGGER.isLoggable(Level.FINER)) {
                        LOGGER.logp(
                                Level.FINE,
                                propertyChangeListener.getClass().getName(),
                                "propertyChange",
                                t.getLocalizedMessage(),
                                t);
                    }
                }
            }

        } finally {
            monitor.readLock().unlock();
        }
    }

    /**
     * Sets the CRS of the viewport, if one exists, based on the first Layer with a non-null CRS.
     * This is called when a new Layer is added to the Layer list. Does nothing if the viewport
     * already has a CRS set or if it has been set as non-editable.
     */
    private void checkViewportCRS() {
        if (viewport != null && getCoordinateReferenceSystem() == null && viewport.isEditable()) {

            for (Layer layer : layerList) {
                ReferencedEnvelope bounds = layer.getBounds();
                if (bounds != null) {
                    CoordinateReferenceSystem crs = bounds.getCoordinateReferenceSystem();
                    if (crs != null) {
                        viewport.setCoordinateReferenceSystem(crs);
                        return;
                    }
                }
            }
        }
    }

    private class LayerList extends CopyOnWriteArrayList<Layer> {

        private static final long serialVersionUID = 8011733882551971475L;

        /**
         * Adds a layer at the specified position in this list. Does nothing if the layer is already
         * present.
         *
         * @param index position for the layer
         * @param element the layer to add
         */
        @Override
        public void add(int index, Layer element) {
            if (!contains(element)) {
                super.add(index, element);
                if (layerListener != null) {
                    element.addMapLayerListener(layerListener);
                }
                checkViewportCRS();
                fireLayerAdded(element, index, index);
            }
        }

        /**
         * Adds a layer if it is not already present. Equivalent to {@linkplain
         * #addIfAbsent(Layer)}.
         *
         * @param element the layer to add
         * @return {@code true} if the layer was added; {@code false} if it was already present in
         *     this list
         */
        @Override
        public boolean add(Layer element) {
            return addIfAbsent(element);
        }

        /**
         * Adds all layers from the input collection that are not already present in this list.
         * Equivalent to {@code addAllAbsent(layers) > 0}.
         *
         * @param layers candidate layers to add
         * @return {@code true} is any layers were added; {@code false} otherwise
         */
        @Override
        public boolean addAll(Collection<? extends Layer> layers) {
            return addAllAbsent(layers) > 0;
        }

        /**
         * Adds all layers from the input collection that are not already present in this list, with
         * the first added layer taking position {@code index}.
         *
         * @param index position of the first added layer in this list
         * @param layers candidate layers to add
         * @return {@code true} if any layers were added; {@code false} otherwise
         */
        @Override
        public boolean addAll(int index, Collection<? extends Layer> layers) {
            boolean added = false;
            int pos = index;

            for (Layer layer : layers) {
                if (!contains(layer)) {
                    add(pos, layer);
                    if (layerListener != null) {
                        layer.addMapLayerListener(layerListener);
                    }
                    added = true;
                    pos++;
                }
            }

            if (added) {
                checkViewportCRS();
                fireLayerAdded(null, index, size() - 1);
            }

            return added;
        }

        /**
         * Adds all layers from the input collection that are not already present in this list.
         *
         * @param layers candidate layers to add
         * @return the number of layers added
         */
        @Override
        public int addAllAbsent(Collection<? extends Layer> layers) {
            int start = size();
            int added = super.addAllAbsent(layers);
            if (added > 0) {
                if (layerListener != null) {
                    for (int i = start; i < size(); i++) {
                        get(i).addMapLayerListener(layerListener);
                    }
                }
                checkViewportCRS();
                fireLayerAdded(null, start, size() - 1);
            }

            return added;
        }

        /**
         * Adds a layer if it is not already present.
         *
         * @param element the layer to add
         * @return {@code true} if the layer was added; {@code false} if it was already present in
         *     this list
         */
        @Override
        public boolean addIfAbsent(Layer element) {
            boolean added = super.addIfAbsent(element);
            if (added) {
                if (layerListener != null) {
                    element.addMapLayerListener(layerListener);
                }
                checkViewportCRS();
                fireLayerAdded(element, size() - 1, size() - 1);
            }
            return added;
        }

        /** Removes all layers from this list and calls their {@code dispose} methods. */
        @Override
        public void clear() {
            for (Layer element : this) {
                if (layerListener != null) {
                    element.removeMapLayerListener(layerListener);
                }
                element.dispose();
            }
            super.clear();
            fireLayerRemoved(null, -1, -1);
        }

        /**
         * Removes the layer at position {@code index} from this list. Note: removing a layer causes
         * its {@code dispose} method to be called, so although a reference to the removed layer is
         * returned by this method it should not be used subsequently.
         *
         * @param index the position of the layer to be removed
         * @return the layer that was removed (will have been disposed)
         */
        @Override
        public Layer remove(int index) {
            Layer removed = super.remove(index);
            fireLayerRemoved(removed, index, index);
            if (layerListener != null) {
                removed.removeMapLayerListener(layerListener);
            }
            removed.dispose();
            return removed;
        }

        /**
         * Removes the specified element, which much be a Layer, from this list if present. This
         * method calls the layer's {@code dispose} method, so any external references to the layer
         * should be discarded.
         *
         * @param element the element to remove
         * @return {@code true} if removed; {@code false} if not present in this list
         */
        @Override
        public boolean remove(Object element) {
            boolean removed = super.remove(element);
            if (removed) {
                fireLayerRemoved((Layer) element, -1, -1);
                if (element instanceof Layer) {
                    Layer layer = (Layer) element;
                    if (layerListener != null) {
                        layer.removeMapLayerListener(layerListener);
                    }
                    layer.dispose();
                }
            }
            return removed;
        }

        /**
         * Removes all layers in the input collection from this list, if present.
         *
         * @param layers the candidate layers to remove
         * @return {@code true} if any layers were removed; {@code false} otherwise
         */
        @Override
        public boolean removeAll(Collection<?> layers) {
            for (Object obj : layers) {
                Layer element = (Layer) obj;
                if (!contains(element)) {
                    continue;
                }
                if (layerListener != null) {
                    element.removeMapLayerListener(layerListener);
                }
                element.dispose();
            }
            boolean removed = super.removeAll(layers);
            fireLayerRemoved(null, 0, size() - 1);
            return removed;
        }

        /**
         * Removes any layers from this list that are not contained in the input collection.
         *
         * @param layers the layers which should not be removed
         * @return {@code true} if any layers were removed; {@code false} otherwise
         */
        @Override
        public boolean retainAll(Collection<?> layers) {
            for (Layer element : this) {
                if (!layers.contains(element)) {
                    if (layerListener != null) {
                        element.removeMapLayerListener(layerListener);
                    }
                    element.dispose();
                }
            }
            boolean removed = super.retainAll(layers);
            if (removed) {
                fireLayerRemoved(null, 0, size() - 1);
            }
            return removed;
        }

        /**
         * Replaces the layer at the given position with another. Equivalent to:
         *
         * <pre><code>
         * remove(index);
         * add(index, element);
         * </code></pre>
         *
         * The same events will be sent to {@link MapLayerListListener} objects as if the above code
         * had been called.
         *
         * @param index position of the layer to be replaced
         * @param element the new layer
         * @return the layer that was replaced
         */
        @Override
        public Layer set(int index, Layer element) {
            /*
             * Note: rather than calling the superclass set method here
             * we call remove followed by add to ensure correct event
             * and listener handling.
             */
            Layer removed = remove(index);
            add(index, element);
            checkViewportCRS();
            return removed;
        }

        /**
         * Moves a layer in this list.
         *
         * @param sourcePosition existing position of the layer
         * @param destPosition new position of the layer
         */
        private void move(int sourcePosition, int destPosition) {
            Layer layer = super.remove(sourcePosition);
            super.add(destPosition, layer);
            fireLayerMoved(layer, destPosition);
        }
    }
}
