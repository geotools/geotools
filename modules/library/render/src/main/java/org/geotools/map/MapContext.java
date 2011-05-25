/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.event.MapBoundsEvent;
import org.geotools.map.event.MapBoundsListener;
import org.geotools.map.event.MapLayerListListener;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.feature.type.FeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Extension of MapContent to cover the requirements of the OGC Map Context specifications.
 * <p>
 * The following OGC specifications (or working drafts) are the inspiration for this class:
 * <ul>
 * <li>OGC Web Map Context Document
 * <li>OGC Open Web Services Context Document
 * </ul>
 * At this time the primary difference is the provision of contact details, map abstract and
 * keywords.
 * 
 * @author Cameron Shorter
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/library/render/src/main/java/org/geotools
 *         /map/MapContext.java $
 * @version $Id$
 */
public class MapContext extends MapContent {
    
    /**
     * Creates a default empty map context. The coordinate reference system for the map context
     * should be set explicitly, or implicitly via {@code addLayer} prior to using the context.
     */
    public MapContext() {
        this((CoordinateReferenceSystem) null);
    }

    /**
     * Creates a default empty map context
     * 
     * @param crs
     *            the coordindate reference system to be used with this context (may be null and set
     *            later)
     */
    public MapContext(final CoordinateReferenceSystem crs) {
        this(null, null, null, null, null, crs);
    }

    /**
     * Creates a map context with the provided layers.
     * <p>
     * Note, the coordinate reference system for the context will be set from that of the first
     * layer with an available CRS.
     * 
     * @param layers
     *            an array of MapLayer objects (may be empty or null) to be added to this context
     */
    public MapContext(MapLayer[] layers) {
        this(layers, DefaultGeographicCRS.WGS84);
    }

    /**
     * Creates a map context with the provided layers and coordinate reference system
     * 
     * @param layers
     *            an array of MapLayer objects (may be empty or null) to be added to this context
     * 
     * @param crs
     *            the coordindate reference system to be used with this context (may be null and set
     *            later)
     */
    public MapContext(MapLayer[] layers, final CoordinateReferenceSystem crs) {
        this(layers, null, null, null, null, crs);
    }

    /**
     * Creates a map context
     * <p>
     * Note, the coordinate reference system for the context will be set from that of the first
     * layer with an available CRS.
     * 
     * @param layers
     *            an array of MapLayer objects (may be empty or null) to be added to this context
     * 
     * @param title
     *            a title for this context (e.g. might be used by client-code that is displaying the
     *            context's layers); may be null or an empty string
     * 
     * @param contextAbstract
     *            a short description of the context and its contents; may be null or an empty
     *            string
     * 
     * @param contactInformation
     *            can be used, for example, to record the creators or custodians of the data that
     *            are, or will be, held by this context; may be null or an empty string
     * 
     * @param keywords
     *            an optional array of key words pertaining to the data that are, or will be, held
     *            by this context; may be null or a zero-length String array
     * 
     */
    public MapContext(MapLayer[] layers, String title, String contextAbstract,
            String contactInformation, String[] keywords) {
        this(layers, title, contextAbstract, contactInformation, keywords, null);
    }

    /**
     * Creates a new map context
     * 
     * @param layers
     *            an array of MapLayer objects (may be empty or null) to be added to this context
     * 
     * @param title
     *            a title for this context (e.g. might be used by client-code that is displaying the
     *            context's layers); may be null or an empty string
     * 
     * @param contextAbstract
     *            a short description of the context and its contents; may be null or an empty
     *            string
     * 
     * @param contactInformation
     *            can be used, for example, to record the creators or custodians of the data that
     *            are, or will be, held by this context; may be null or an empty string
     * 
     * @param keywords
     *            an optional array of key words pertaining to the data that are, or will be, held
     *            by this context; may be null or a zero-length String array
     * 
     * @param crs
     *            the coordindate reference system to be used with this context (may be null and set
     *            later)
     */
    public MapContext(MapLayer[] layers, String title, String contextAbstract,
            String contactInformation, String[] keywords, final CoordinateReferenceSystem crs) {
        super(layers, title, contextAbstract, contactInformation, keywords, crs);
    }
    
    /**
     * Add a new layer if not already present and trigger a {@link LayerListEvent}.
     * 
     * @param layer
     *            the layer to be inserted
     * 
     * @return true if the layer has been added, false otherwise
     */
    public boolean addLayer(MapLayer mapLayer) {
        layers().add(mapLayer.toLayer());
        return true;
    }

    /**
     * Add a new layer in the specified position and trigger a {@link LayerListEvent}. Layer won't
     * be added if it's already in the list.
     * 
     * @param index
     *            index at which the layer will be inserted
     * @param layer
     *            the layer to be inserted
     * 
     * @return true if the layer has been added, false otherwise
     */
    public boolean addLayer(int index, MapLayer mapLayer) {
        Layer layer = mapLayer.toLayer();
        layers().add(index, layer);
        return true;
    }

    /**
     * Add a new layer and trigger a {@link LayerListEvent}.
     * 
     * @param featureSource
     *            a SimpleFeatureSource with the new layer that will be added.
     */
    public void addLayer(FeatureSource featureSource, Style style) {
        addLayer(new DefaultMapLayer(featureSource, style, ""));
    }

    /**
     * Add a new layer and trigger a {@link LayerListEvent}.
     * 
     * @param collection
     *            a SimpleFeatureCollection with the new layer that will be added.
     */
    public void addLayer(FeatureCollection featureCollection, Style style) {
        Layer layer = new FeatureLayer(featureCollection, style);
        this.layers().add(layer);
    }

    /**
     * Add a new layer and trigger a {@link LayerListEvent}.
     * 
     * @param collection
     *            Collection with the new layer that will be added.
     */
    public void addLayer(Collection collection, Style style) {
        if (collection instanceof FeatureCollection) {
            FeatureCollection featureCollection = (FeatureCollection) collection;
            addLayer(featureCollection, style);
        } else {
            throw new IllegalArgumentException("FeatureCollection required");
        }
    }

    /**
     * Add a new layer and trigger a {@link LayerListEvent}
     * 
     * @param gridCoverage
     *            a GridCoverage with the new layer that will be added.
     * 
     */
    public void addLayer(GridCoverage gridCoverage, Style style) {
        if (style == null) {
            throw new IllegalArgumentException("style cannot be null");
        }
        if (gridCoverage instanceof GridCoverage2D) {
            Layer layer = new GridCoverageLayer((GridCoverage2D) gridCoverage, style);
            layers().add(layer);
        } else {
            throw new UnsupportedOperationException("GridCoverage2D required");
        }
    }

    /**
     * Add a new layer and trigger a {@link LayerListEvent}
     * 
     * @param gridCoverage
     *            an AbstractGridCoverage2DReader with the new layer that will be added.
     * 
     */
    public void addLayer(AbstractGridCoverage2DReader reader, Style style) {
        if (style == null) {
            throw new IllegalArgumentException("style cannot be null");
        }
        Layer layer = new GridReaderLayer(reader, style);
        layers().add(layer);
    }

    /**
     * Remove a layer, if present, and trigger a {@link LayerListEvent}.
     * 
     * @param layer
     *            a MapLayer that will be added.
     * 
     * @return true if the layer has been removed
     */
    public boolean removeLayer(MapLayer layer) {
        int index = indexOf(layer);
        if (index == -1) {
            return false;
        } else {
            removeLayer(index);
            return true;
        }
    }

    /**
     * Remove a layer and trigger a {@link LayerListEvent}.
     * 
     * @param index
     *            The index of the layer that it's going to be removed
     * 
     * @return the layer removed, if any
     */
    public MapLayer removeLayer(int index) {
        Layer removed = layers().remove(index);
        return new DefaultMapLayer(removed);
    }

    /**
     * Add an array of new layers and trigger a {@link LayerListEvent}.
     * 
     * @param layers
     *            The new layers that are to be added.
     * 
     * @return the number of layers actually added to the MapContext
     */
    public int addLayers(MapLayer[] array) {
        if ((array == null) || (array.length == 0)) {
            return 0;
        }
        List<Layer> layersToAdd = toLayerList(array);
        int count = layers().addAllAbsent(layersToAdd);
        return count;
    }

    /**
     * Remove an array of layers and trigger a {@link LayerListEvent}.
     * 
     * @param layers
     *            The layers that are to be removed.
     */
    public void removeLayers(MapLayer[] array) {
        if ((array == null) || (array.length == 0) || layers().isEmpty()) {
            return;
        }
        List<Layer> layersToRemove = toLayerList(array);
        layers().removeAll(layersToRemove);
    }

    /**
     * Clears the whole layer list. Will fire a LayerListChangedEvent
     */
    public void clearLayerList() {
        layers().clear();
    }

    /**
     * Return this model's list of layers. If no layers are present, then an empty array is
     * returned.
     * 
     * @return This model's list of layers.
     */
    public MapLayer[] getLayers() {
        MapLayer[] array = new MapLayer[layers().size()];
        int index = 0;
        for (Iterator<Layer> iter = layers().iterator(); iter.hasNext(); index++) {
            Layer layer = iter.next();
            array[index] = new DefaultMapLayer(layer);
        }
        return array;
    }

    /**
     * Return the requested layer.
     * 
     * @param index
     *            index of layer to return.
     * 
     * @return the layer at the specified position
     * 
     * @throws IndexOutOfBoundsException
     *             if the index is out of range
     */
    public MapLayer getLayer(int index) throws IndexOutOfBoundsException {
        Layer layer = layers().get(index);
        return new DefaultMapLayer(layer);
    }

    /**
     * Moves a layer from a position to another. Will fire a MapLayerListEvent
     * 
     * @param sourcePosition
     *            the layer current position
     * @param destPosition
     *            the layer new position
     */
    public void moveLayer(int sourcePosition, int destPosition) {
        super.moveLayer(sourcePosition, destPosition);
    }

    /**
     * Returns an iterator over the layers in this context in proper sequence.
     * 
     * @return an iterator over the layers in this context in proper sequence.
     */
    public Iterator<MapLayer> iterator() {
        final Iterator<Layer> iter = layers().iterator();
        return new Iterator<MapLayer>() {
            public void remove() {
                iter.remove();
            }

            public MapLayer next() {
                Layer layer = iter.next();
                if (layer == null) {
                    return null;
                }
                return new DefaultMapLayer(layer);
            }

            public boolean hasNext() {
                return iter.hasNext();
            }
        };
    }

    /**
     * Returns the index of the first occurrence of the specified layer, or -1 if this list does not
     * contain this element.
     * 
     * @param layer
     *            the MapLayer to search for
     * 
     * @return index of mapLayer or -1 if not found
     */
    public int indexOf(MapLayer mapLayer) {
        Layer layer = mapLayer.toLayer();
        return layers().indexOf(layer);
    }

    /**
     * Returns the number of layers in this map context
     * 
     * @return the number of layers in this map context
     */
    public int getLayerCount() {
        return layers().size();
    }

    /**
     * Get the bounding box of all the layers in this MapContext. If all the layers cannot determine
     * the bounding box in the speed required for each layer, then null is returned. The bounds will
     * be expressed in the MapContext coordinate system.
     * <p>
     * This implementation is more forgiving then getMaxBounds() as it is willing to consider the
     * bounds of layers that are incomplete and not record a coordinate reference system.
     * 
     * @return The bounding box of the features or null if unknown and too expensive for the method
     *         to calculate.
     * 
     * @throws IOException
     *             if an IOException occurs while accessing the FeatureSource bounds
     * 
     */
    public ReferencedEnvelope getLayerBounds() throws IOException {
        // return getMaxBounds();
        // Jody: I don't like the following implementation because it will allow a mapLayer
        // with null CRS to produce a bounds that is not transformed into the mapCRS
        // (I would prefer to skip these layers as getMaxBounds() does however some test cases
        // depend on this feature)
        ReferencedEnvelope maxBounds = null;
        CoordinateReferenceSystem mapCRS = viewport != null ? viewport.getCoordianteReferenceSystem() : null;

        for (Layer layer : layers()) {
            if (layer == null) {
                continue; // skip empty entry
            }
            ReferencedEnvelope dataBounds = layer.getBounds();
            if (dataBounds == null) {
                continue;
            } else {
                try {
                    CoordinateReferenceSystem dataCrs = dataBounds.getCoordinateReferenceSystem();
                    if ((dataCrs != null) && mapCRS != null
                            && !CRS.equalsIgnoreMetadata(dataCrs, mapCRS)) {
                        dataBounds = dataBounds.transform(mapCRS, true);
                    }
                    if (dataCrs == null && mapCRS != null) {
                        LOGGER.log(Level.SEVERE,
                                "It was not possible to get a projected bounds estimate");
                    }
                } catch (FactoryException e) {
                    LOGGER
                            .log(
                                    Level.SEVERE,
                                    "Data source and map context coordinate system differ, yet it was not possible to get a projected bounds estimate...",
                                    e);
                    continue;
                } catch (TransformException e) {
                    LOGGER
                            .log(
                                    Level.SEVERE,
                                    "Data source and map context coordinate system differ, yet it was not possible to get a projected bounds estimate...",
                                    e);
                    continue;
                }
                if (maxBounds == null) {
                    maxBounds = dataBounds;
                } else {
                    maxBounds.expandToInclude(dataBounds);
                }
                if (mapCRS == null) {
                    mapCRS = dataBounds.getCoordinateReferenceSystem();
                }
            }
        }
        return maxBounds;
    }

    /**
     * Register interest in receiving a {@link LayerListEvent}. A <code>LayerListEvent</code> is
     * sent if a layer is added or removed, but not if the data within a layer changes.
     * 
     * @param listener
     *            The object to notify when Layers have changed.
     */
    public void addMapLayerListListener(MapLayerListListener listener){
        super.addMapLayerListListener(listener);
    }

    /**
     * Remove interest in receiving {@link LayerListEvent}.
     * 
     * @param listener
     *            The object to stop sending <code>LayerListEvent</code>s.
     */
    public void removeMapLayerListListener(MapLayerListListener listener){
        super.removeMapLayerListListener(listener);
    }

    /**
     * Set the area of interest. This triggers a MapBoundsEvent to be published.
     * 
     * @param areaOfInterest
     *            the new area of interest
     * @param coordinateReferenceSystem
     *            the CRS for the new area of interest
     * 
     * @throws IllegalArgumentException
     *             if either argument is {@code null}
     */
    public void setAreaOfInterest(Envelope areaOfInterest, CoordinateReferenceSystem crs)
            throws IllegalArgumentException {
        getViewport().setBounds(new ReferencedEnvelope(areaOfInterest, crs));
    }

    /**
     * Set the area of interest. This triggers a MapBoundsEvent to be published.
     * 
     * @param bounds
     *            the new area of interest
     * 
     * @throws IllegalArgumentException
     *             if the provided areaOfInterest is {@code null} or does not have a coordinate
     *             reference system
     */
    public void setAreaOfInterest(ReferencedEnvelope bounds) throws IllegalArgumentException {
        if (bounds == null) {
            throw new NullPointerException("bounds must not be null");
        }
        getViewport().setBounds(bounds);
    }

    /**
     * Gets the current area of interest provided by {@link #getBounds()}.
     * 
     * @return Current area of interest
     */
    public ReferencedEnvelope getAreaOfInterest() {
        return getBounds();
    }

    /**
     * Get the current coordinate system.
     * 
     * @return the coordinate system of this box.
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem(){
        return super.getCoordinateReferenceSystem();
    }

    /**
     * Transform the current area of interest for this context using the provided transform. This
     * may be useful for zooming and panning processes.
     * 
     * @param transform
     *            The transform to change map viewport
     */
    public void transform(AffineTransform transform) {
        getViewport().transform(transform);
    }

    
    /**
     * Register interest in receiving {@link MapBoundsEvent}s.
     * 
     * @param listener
     *            The object to notify when the area of interest has changed.
     */
    public void addMapBoundsListener(MapBoundsListener listener){
        super.addMapBoundsListener(listener);
    }

    /**
     * Remove interest in receiving a {@link BoundingBoxEvent}s.
     * 
     * @param listener
     *            The object to stop sending change events.
     */
    public void removeMapBoundsListener(MapBoundsListener listener){
        super.removeMapBoundsListener(listener);        
    }

    /**
     * Get the abstract which describes this interface, returns an empty string if this has not been
     * set yet.
     * 
     * @return The Abstract or an empty string if not present
     */
    public String getAbstract(){
        String description = (String) getUserData().get("abstract");
        return description == null ? "" : description;
    }

    /**
     * Set an abstract which describes this context.
     * 
     * @param conAbstract
     *            the Abstract.
     */
    public void setAbstract(final String contextAbstract){
        getUserData().put("abstract", contextAbstract);
    }

    /**
     * Get the contact information associated with this context, returns an empty string if
     * contactInformation has not been set.
     * 
     * @return the ContactInformation or an empty string if not present
     */
    public String getContactInformation(){
        String contact =  (String) getUserData().get("contact");
        return contact == null ? "" : contact;
    }

    /**
     * Set contact information associated with this class.
     * 
     * @param contactInformation
     *            the ContactInformation.
     */
    public void setContactInformation(final String contactInformation){
        getUserData().put("contact", contactInformation);
    }

    /**
     * Set or change the coordinate reference system for this context. This will trigger a
     * MapBoundsEvent to be published to listeners.
     * 
     * @param crs
     * @throws FactoryException
     * @throws TransformException
     */
    public void setCoordinateReferenceSystem(final CoordinateReferenceSystem crs) {
        getViewport().setCoordinateReferenceSystem(crs);
    }

    /**
     * Get an array of keywords associated with this context, returns an empty array if no keywords
     * have been set. The array returned is a copy, changes to the returned array won't influence
     * the MapContextState
     * 
     * @return array of keywords
     */
    public String[] getKeywords(){
        Object obj = getUserData().get("keywords");
        if (obj == null) {
            return new String[0];
        } else if (obj instanceof String) {
            String keywords = (String) obj;
            return keywords.split(",");
        } else if (obj instanceof String[]) {
            String keywords[] = (String[]) obj;
            String[] copy = new String[keywords.length];
            System.arraycopy(keywords, 0, copy, 0, keywords.length);
            return copy;
        } else if (obj instanceof Collection) {
            Collection<String> keywords = (Collection) obj;
            return keywords.toArray(new String[keywords.size()]);
        } else {
            return new String[0];
        }
    }

    /**
     * Set an array of keywords to associate with this context.
     * 
     * @param keywords
     *            the Keywords.
     */
    public void setKeywords(final String[] keywords){
        getUserData().put("keywords", keywords);
    }

    /**
     * Get the title, returns an empty string if it has not been set yet.
     * 
     * @return the title, or an empty string if it has not been set.
     */
    public String getTitle(){
        return super.getTitle();
    }

    /**
     * Set the title of this context.
     * 
     * @param title
     *            the title.
     */
    public void setTitle(final String title){
        super.setTitle(title);
    }

    /**
     * Registers PropertyChangeListener to receive events.
     * 
     * @param listener
     *            The listener to register.
     */
    public void addPropertyChangeListener(java.beans.PropertyChangeListener listener){
        super.addPropertyChangeListener(listener);
    }

    /**
     * Removes PropertyChangeListener from the list of listeners.
     * 
     * @param listener
     *            The listener to remove.
     */
    public void removePropertyChangeListener(java.beans.PropertyChangeListener listener){
        super.removePropertyChangeListener(listener);
    }

    //
    // Utility Methods
    //
    @SuppressWarnings("deprecation")
    protected List<Layer> toLayerList(MapLayer[] array) {
        List<Layer> list = new ArrayList<Layer>();
        for (MapLayer mapLayer : array) {
            Layer layer = mapLayer.toLayer();
            list.add(layer);
        }
        return list;
    }
}
