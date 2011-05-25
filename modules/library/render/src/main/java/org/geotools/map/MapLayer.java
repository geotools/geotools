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

import java.util.Collection;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.memory.CollectionSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.event.MapLayerListener;
import org.geotools.styling.Style;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.operation.TransformException;

/**
 * MapLayer is a clean wrapper around the Layer class used for rendering; primarily used for user
 * interface code.
 * <p>
 * Please note that not all Layer implementations support Query and/or Style.
 * 
 * @author Cameron Shorter
 * @author Martin Desruisseaux
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/library/render/src/main/java/org/geotools
 *         /map/MapLayer.java $
 * @version $Id$
 */
public class MapLayer {
    /**
     * This is the internal delegate
     */
    protected Layer internal;

    /**
     * Wrap up a layer in a DefaultMapLayer; generally used to ensure layer is shown to the world as
     * a feature source even if it happens to be a WMS or something.
     */
    public MapLayer(Layer layer) {
        this.internal = layer;
    }

    /**
     * Creates a new instance of DefaultMapLayer
     * 
     * @param featureSource
     *            the data source for this layer
     * @param style
     *            the style used to represent this layer
     * @param title
     *            the layer title
     * 
     * @throws NullPointerException
     *             DOCUMENT ME!
     */
    @SuppressWarnings("unchecked")
    public MapLayer(FeatureSource featureSource, Style style, String title) {
        internal = new FeatureLayer(featureSource, style, title);
    }

    public MapLayer(CollectionSource source, Style style, String title) {
        throw new UnsupportedOperationException(
                "Raw Collections not supported; please use FeatureCollection");
    }

    /**
     * Creates a new instance of DefaultMapLayer
     * 
     * @param featureSource
     *            the data source for this layer
     * @param style
     *            the style used to represent this layer
     */
    @SuppressWarnings("unchecked")
    public MapLayer(FeatureSource featureSource, Style style) {
        internal = new FeatureLayer(featureSource, style);
    }

    /**
     * Creates a new instance of DefaultMapLayer using a non-emtpy feature collection as a parameter
     * 
     * @param collection
     *            the source feature collection
     * @param style
     *            the style used to represent this layer
     * @param title
     *            Title of map layer
     */
    public MapLayer(FeatureCollection collection, Style style, String title) {
        internal = new FeatureLayer(collection, style, title);
    }

    @SuppressWarnings("unchecked")
    public MapLayer(Collection collection, Style style, String title) {
        if (collection instanceof FeatureCollection) {
            internal = new FeatureLayer((FeatureCollection) collection, style, title);
        } else {
            throw new UnsupportedOperationException(
                    "Raw collections not supported; please use FeatureCollection");
        }
    }

    /**
     * Creates a new instance of DefaultMapLayer using a non-emtpy feature collection as a parameter
     * 
     * @param collection
     *            the source feature collection
     * @param style
     *            the style used to represent this layer
     */
    public MapLayer(FeatureCollection collection, Style style) {
        internal = new FeatureLayer((FeatureCollection) collection, style);
    }

    @SuppressWarnings("unchecked")
    public MapLayer(Collection collection, Style style) {
        if (collection instanceof FeatureCollection) {
            internal = new FeatureLayer((FeatureCollection) collection, style);
        } else {
            throw new UnsupportedOperationException(
                    "Raw collections not supported; please use FeatureCollection");
        }
    }

    /**
     * * Add a new layer and trigger a {@link LayerListEvent}.
     * 
     * @param coverage
     *            The new layer that has been added.
     * @param style
     * @throws SchemaException
     * @throws FactoryRegistryException
     * @throws TransformException
     */
    public MapLayer(GridCoverage coverage, Style style) throws TransformException,
            FactoryRegistryException, SchemaException {
        internal = new GridCoverageLayer((GridCoverage2D) coverage, style);
    }

    /**
     * Constructor which adds a new layer and trigger a {@link LayerListEvent}.
     * 
     * @param reader
     *            a reader with the new layer that will be added.
     * @param style
     * @param title
     * @param params
     *            GeneralParameterValue[] that describe how the {@link AbstractGridCoverage2DReader}
     *            shall read the images
     * 
     * @throws SchemaException
     * @throws FactoryRegistryException
     * @throws TransformException
     */
    public MapLayer(AbstractGridCoverage2DReader reader, Style style, String title,
            GeneralParameterValue[] params) throws TransformException, FactoryRegistryException,
            SchemaException {
        internal = new GridReaderLayer(reader, style, title, params);
    }

    /**
     * Constructor which adds a new layer and trigger a {@link LayerListEvent}.
     * 
     * @param reader
     *            a reader with the new layer that will be added.
     * @param style
     * @param title
     * 
     * @throws SchemaException
     * @throws FactoryRegistryException
     * @throws TransformException
     */
    public MapLayer(AbstractGridCoverage2DReader reader, Style style, String title) {
        internal = new GridReaderLayer(reader, style, title);
    }

    /**
     * Constructor which adds a new layer and triggers a {@link LayerListEvent}.
     * 
     * @param reader
     *            a reader with the new layer that will be added
     * @param style
     * 
     * @throws SchemaException
     * @throws FactoryRegistryException
     * @throws TransformException
     */
    public MapLayer(AbstractGridCoverage2DReader reader, Style style) {
        internal = new GridReaderLayer(reader, style);
    }

    /**
     * * Add a new layer and trigger a {@link LayerListEvent}.
     * 
     * @param coverage
     *            The new layer that has been added.
     * @param style
     * @param title
     * @throws SchemaException
     * @throws FactoryRegistryException
     * @throws TransformException
     */
    public MapLayer(GridCoverage coverage, Style style, String title) throws TransformException,
            FactoryRegistryException, SchemaException {
        internal = new GridCoverageLayer((GridCoverage2D) coverage, style, title);
    }

    /**
     * Access to raw layer object used for rendering.
     * 
     * @return Layer used for rendering
     */
    public Layer toLayer() {
        return internal;
    }

    /**
     * Get the feature collection for this layer; if available.
     * 
     * @return The features for this layer, null if not available.
     */
    @SuppressWarnings("unchecked")
    public FeatureSource getFeatureSource() {
        if (internal instanceof FeatureLayer) {
            FeatureLayer layer = (FeatureLayer) internal;
            return layer.getFeatureSource();
        }
        else {
            FeatureSource source = (FeatureSource) internal.getUserData().get("source");
            if( source == null ){
                if (internal instanceof GridCoverageLayer) {
                    GridCoverageLayer layer = (GridCoverageLayer) internal;
                    SimpleFeatureCollection featureCollection = layer.toFeatureCollection();
                    source = DataUtilities.source(featureCollection);
                    layer.getUserData().put("source", source ); 
                }
                if (internal instanceof GridReaderLayer) {
                    GridReaderLayer layer = (GridReaderLayer) internal;
                    SimpleFeatureCollection featureCollection = layer.toFeatureCollection();
                    source = DataUtilities.source(featureCollection);
                    layer.getUserData().put("source", source ); 
                }
            }
            return source;
        }

    }

    /**
     * Get the data source for this layer.
     * 
     * @return Data source for this layer, null if not yet set or if {@link FeatureSource} is used
     */
    public CollectionSource getSource() {
        return null; // no longer supported
    }

    /**
     * Get the style for this layer. If style has not been set, then null is returned.
     * 
     * @return The style (SLD).
     */
    public Style getStyle(){
        if (internal instanceof FeatureLayer) {
            FeatureLayer layer = (FeatureLayer) internal;
            return layer.getStyle();
        }
        if (internal instanceof GridCoverageLayer) {
            GridCoverageLayer layer = (GridCoverageLayer) internal;
            return layer.getStyle();
        }
        if (internal instanceof GridReaderLayer) {
            GridReaderLayer layer = (GridReaderLayer) internal;
            return layer.getStyle();
        }
        return null;
    }

    /**
     * Sets the style for this layer. If a style has not been defined a default one is used.
     * 
     * @param style
     *            The new style
     */
    public void setStyle(Style style){
        if (style == null) {
            throw new NullPointerException("Style required");
        }
        else if (internal instanceof FeatureLayer) {
            FeatureLayer layer = (FeatureLayer) internal;
            layer.setStyle(style);
        }
        else if (internal instanceof GridCoverageLayer) {
            GridCoverageLayer layer = (GridCoverageLayer) internal;
            layer.setStyle(style);
        }
        else if (internal instanceof GridReaderLayer) {
            GridReaderLayer layer = (GridReaderLayer) internal;
            layer.setStyle(style);
        }
        else {
            throw new IllegalStateException("Style not supported by "+internal);
        }
    }

    /**
     * Get the title of this layer. If title has not been defined then an empty string is returned.
     * 
     * @return The title of this layer.
     */
    public String getTitle(){
        return internal.getTitle();
    }

    /**
     * Set the title of this layer. A {@link LayerEvent} is fired if the new title is different from
     * the previous one.
     * 
     * @param title
     *            The title of this layer.
     */
    public void setTitle(String title) {
        if (title == null) {
            throw new NullPointerException("Title required");
        }
        internal.setTitle(title);
    }

    /**
     * Determine whether this layer is visible on a map pane or whether the layer is hidden.
     * 
     * @return <code>true</code> if the layer is visible, or <code>false</code> if the layer is
     *         hidden.
     */
    public boolean isVisible(){
        return internal.isVisible();
    }

    /**
     * Specify whether this layer is visible on a map pane or whether the layer is hidden. A
     * {@link LayerEvent} is fired if the visibility changed.
     * 
     * @param visible
     *            Show the layer if <code>true</code>, or hide the layer if <code>false</code>
     */
    public void setVisible(boolean visible){
        internal.setVisible(visible);
    }

    /**
     * Determine whether this layer is currently selected.
     * 
     * @return <code>true</code> if the layer is selected, or <code>false</code> otherwise
     */
    public boolean isSelected(){
        Boolean selected = (Boolean) internal.getUserData().get("selected");

        return selected == null ? false : selected;
    }

    /**
     * Specify whether this layer is selected. A {@link LayerEvent} iw fired if the selected status
     * is changed.
     * 
     * @param selected
     *            Set the layer as selected if <code>true</code> or as unselected if
     *            <code>false</code>
     */
    public void setSelected(boolean selected){
        Boolean current = (Boolean) internal.getUserData().get("selected");

        if (current != null && current == selected) {
            return;
        }
        internal.getUserData().put("selected", selected);
        if (selected) {
            internal.fireMapLayerListenerLayerSelected();
        } else {
            internal.fireMapLayerListenerLayerDeselected();
        }
    }

    /**
     * Returns the definition query (filter) for this layer. If no definition query has been defined
     * {@link Query.ALL} is returned.
     * @return the definition query established for this layer. If not set, just returns
     *         {@link Query.ALL}, if set, returns a copy of the actual query object to avoid
     *         external modification
     */
    public Query getQuery(){
        if( internal instanceof FeatureLayer ){
            FeatureLayer layer = (FeatureLayer) internal;
            Query query = layer.getQuery();
            if( query == null || query == Query.ALL){
                return Query.ALL;
            }
            else {
                return new Query( query );
            }
        }
        return Query.ALL;
    }

    /**
     * Sets a definition query for the layer which acts as a filter for the features that the layer
     * will draw.
     * 
     * <p>
     * A consumer must ensure that this query is used in combination with the bounding box filter
     * generated on each map interaction to limit the number of features returned to those that
     * complains both the definition query and relies inside the area of interest.
     * </p>
     * <p>
     * IMPORTANT: only include attribute names in the query if you want them to be ALWAYS returned.
     * It is desirable to not include attributes at all but let the renderer
     * decide which attributes are actually needed to perform its required operation.
     * </p>
     * 
     * @param query
     *            the full filter for this layer.
     * 
     * @throws NullPointerException
     *             if no query is passed on. If you want to reset a definition query, pass it
     *             {@link Query.ALL} instead of <code>null</code>
     * 
     * @task TODO: test that the query filter is siutable for the layer's <code>FeatureSource</code>
     *       schema
     * 
     * @see org.geotools.map.FeatureLayer#setQuery(org.geotools.data.Query)
     */
    public void setQuery(Query query){
        if (query == null) {
            throw new NullPointerException("must provide a Query. Do you mean Query.ALL?");
        }
        if( internal instanceof FeatureLayer ){
            FeatureLayer layer = (FeatureLayer) internal;
            layer.setQuery( new Query(query));
        }
    }

    /**
     * find out the bounds of the layer
     * 
     * @return - the layer's bounds
     */
    public ReferencedEnvelope getBounds(){
        return internal.getBounds();
    }

    // ------------------------------------------------------------------------
    // EVENT HANDLING CODE
    // ------------------------------------------------------------------------

    /**
     * Registers MapLayerListener to receive events.
     * 
     * @param listener
     *            The listener to register.
     */
    public synchronized void addMapLayerListener(org.geotools.map.event.MapLayerListener listener) {
        internal.addMapLayerListener(listener);
    }

    /**
     * Removes MapLayerListener from the list of listeners.
     * 
     * @param listener
     *            The listener to remove.
     */
    public synchronized void removeMapLayerListener(org.geotools.map.event.MapLayerListener listener) {
        internal.removeMapLayerListener(listener);
    }

    /**
     * Notifies all registered listeners about the event.
     * 
     * @param event
     *            The event to be fired
     */
//    protected void fireMapLayerListenerLayerChanged(org.geotools.map.event.MapLayerEvent event) {
//        internal.fireMapLayerListenerLayerChanged(event.getReason());
//    }

    /**
     * Notifies all registered listeners about the event.
     * 
     * @param event
     *            The event to be fired
     */
//    protected void fireMapLayerListenerLayerShown(org.geotools.map.event.MapLayerEvent event) {
//        internal.fireMapLayerListenerLayerShown();
//    }

    /**
     * Notifies all registered listeners about the event.
     * 
     * @param event
     *            The event to be fired
     */
//    protected void fireMapLayerListenerLayerHidden(org.geotools.map.event.MapLayerEvent event) {
//        internal.fireMapLayerListenerLayerHidden();
//    }

    /**
     * Notifies all registered listeners about the selection event.
     * 
     * @param event
     *            The event to be fired
     */
//    protected void fireMapLayerListenerLayerSelected(org.geotools.map.event.MapLayerEvent event) {
//        internal.fireMapLayerListenerLayerSelected();
//    }

    /**
     * Notifies all registered listeners about the deselection event.
     * 
     * @param event
     *            The event to be fired
     */
//    protected void fireMapLayerListenerLayerDeselected(org.geotools.map.event.MapLayerEvent event) {
//        internal.fireMapLayerListenerLayerDeselected();
//    }
    //
    // Object Contract
    //

    /** Hashcode based on internal Layer */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((internal == null) ? 0 : internal.hashCode());
        return result;
    }

    /** Equals based on internal layer */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MapLayer)) {
            return false;
        }
        MapLayer other = (MapLayer) obj;
        if (internal == null) {
            if (other.internal != null) {
                return false;
            }
        } else if (!internal.equals(other.internal)) {
            return false;
        }
        return true;
    }
    
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("MapLayer:");
        buf.append( internal );
        
        return buf.toString();
    }
}
