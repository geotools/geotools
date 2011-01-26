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
import java.util.logging.Logger;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.memory.CollectionSource;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.event.MapBoundsEvent;
import org.geotools.map.event.MapLayerEvent;
import org.geotools.map.event.MapLayerListEvent;
import org.geotools.map.event.MapLayerListener;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.resources.coverage.FeatureUtilities;
import org.geotools.styling.Style;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Envelope;
import org.geotools.styling.SLD;

/**
 * The default implementation of the {@linkplain org.geotools.map.MapContext}
 * interface
 * 
 * @author Andrea Aime
 * @source $URL$
 */
public class DefaultMapContext implements MapContext {

    /** The logger for the map module. */
    static public final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.map");

    /** List of MapLayers */
    List<MapLayer> layerList = new ArrayList<MapLayer>();

    /** Current area of interest */
    ReferencedEnvelope areaOfInterest;

    /** Utility field used by event firing mechanism. */
    protected javax.swing.event.EventListenerList listenerList = null;
    protected MapLayerListener layerListener = new MapLayerListener() {

        public void layerChanged(MapLayerEvent event) {
            fireAsListEvent(event);
        }

        public void layerShown(MapLayerEvent event) {
            fireAsListEvent(event);
        }

        public void layerHidden(MapLayerEvent event) {
            fireAsListEvent(event);
        }

        public void layerSelected(MapLayerEvent event) {
            fireAsListEvent(event);
        }

        public void layerDeselected(MapLayerEvent event) {
            fireAsListEvent(event);
        }

        private void fireAsListEvent(MapLayerEvent event) {
            MapLayer layer = (MapLayer) event.getSource();
            int position = layerList.indexOf(layer);
            fireMapLayerListListenerLayerChanged(new MapLayerListEvent(
                    DefaultMapContext.this, layer, position, event));
        }
    };
    /** Holds value of property abstracts. */
    protected String abstracts;
    /** Utility field used by bound properties. */
    protected java.beans.PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(
            this);
    /** Holds value of property contactInformation. */
    protected String contactInformation;
    /** Holds value of property keywords. */
    protected String[] keywords;
    /** Holds value of property title. */
    protected String title;

    /**
     * Creates a default empty map context. The coordinate reference system
     * for the map context should be set explicitly, or implicitly via
     * {@code addLayer} prior to using the context.
     */
    public DefaultMapContext() {
        this((CoordinateReferenceSystem) null);
    }

    /**
     * Creates a default empty map context
     *
     * @param crs the coordindate reference system to be used with this
     * context (may be null and set later)
     */
    public DefaultMapContext(final CoordinateReferenceSystem crs) {
        this(null, null, null, null, null, crs);
    }

    /**
     * Creates a map context with the provided layers.
     * <p>
     * Note, the coordinate reference system for the context will be
     * set from that of the first layer with an available CRS.
     *
     * @param layers an array of MapLayer objects (may be empty or null)
     * to be added to this context
     */
    public DefaultMapContext(MapLayer[] layers) {
        this(layers, DefaultGeographicCRS.WGS84);
    }

    /**
     * Creates a map context with the provided layers and coordinate
     * reference system
     *
     * @param layers an array of MapLayer objects (may be empty or null)
     * to be added to this context

     * @param crs the coordindate reference system to be used with this
     * context (may be null and set later)
     */
    public DefaultMapContext(MapLayer[] layers, final CoordinateReferenceSystem crs) {
        this(layers, null, null, null, null, crs);
    }

    /**
     * Creates a map context
     * <p>
     * Note, the coordinate reference system for the context will be
     * set from that of the first layer with an available CRS.
     *
     * @param layers an array of MapLayer objects (may be empty or null)
     * to be added to this context
     *
     * @param title a title for this context (e.g. might be used by client-code
     * that is displaying the context's layers); may be null or an empty string
     *
     * @param contextAbstract a short description of the context and its
     * contents; may be null or an empty string
     *
     * @param contactInformation can be used, for example, to record the
     * creators or custodians of the data that are, or will be, held by this context;
     * may be null or an empty string
     *
     * @param keywords an optional array of key words pertaining to the
     * data that are, or will be, held by this context;
     * may be null or a zero-length String array
     *
     */
    public DefaultMapContext(MapLayer[] layers, String title,
            String contextAbstract, String contactInformation, String[] keywords) {
        this(layers, title, contextAbstract, contactInformation, keywords, null);
    }

    /**
     * Creates a new map context
     *
     * @param layers an array of MapLayer objects (may be empty or null)
     * to be added to this context
     *
     * @param title a title for this context (e.g. might be used by client-code
     * that is displaying the context's layers); may be null or an empty string
     *
     * @param contextAbstract a short description of the context and its
     * contents; may be null or an empty string
     *
     * @param contactInformation can be used, for example, to record the
     * creators or custodians of the data that are, or will be, held by this context;
     * may be null or an empty string
     *
     * @param keywords an optional array of key words pertaining to the
     * data that are, or will be, held by this context;
     * may be null or a zero-length String array
     *
     * @param crs the coordindate reference system to be used with this
     * context (may be null and set later)
     */
    public DefaultMapContext(MapLayer[] layers, String title,
            String contextAbstract, String contactInformation,
            String[] keywords, final CoordinateReferenceSystem crs) {

        setTitle(title);
        setAbstract(contextAbstract);
        setContactInformation(contactInformation);
        setKeywords(keywords);

        this.areaOfInterest = new ReferencedEnvelope(crs);

        addLayers(layers);
    }

    /**
     * Add a new layer if not already present and trigger a {@linkplain MapLayerListEvent}.
     * If a coordinate reference system has not been set for the context an attempt is
     * made to retrieve one from the new layer and use that as the context's CRS.
     *
     * @param index the position at which to insert the layer in the list of layers
     * held by this context
     *
     * @param layer the map layer to add
     *
     * @return true if the layer was added; false otherwise (layer was already present)
     */
    public boolean addLayer(int index, MapLayer layer) {
        if (layerList.contains(layer)) {
            return false;
        }

        checkCRS(layer);

        layerList.add(index, layer);
        layer.addMapLayerListener(layerListener);

        fireMapLayerListListenerLayerAdded(new MapLayerListEvent(this, layer,
                index));

        return true;
    }

    /**
     * Add a new layer, if not already present, to the end of the list of layers held
     * by this context and trigger a {@linkplain MapLayerListEvent}
     * If a coordinate reference system has not been set for the context an attempt is
     * made to retrieve one from the new layer and use that as the context's CRS.
     *
     * @param layer the map layer to add
     *
     * @return true if the layer was added; false otherwise (layer was already present)
     */
    public boolean addLayer(MapLayer layer) {
        if (layerList.contains(layer)) {
            return false;
        }

        checkCRS(layer);
        
        layerList.add(layer);
        layer.addMapLayerListener(layerListener);

        fireMapLayerListListenerLayerAdded(new MapLayerListEvent(this, layer,
                indexOf(layer)));

        return true;
    }

    /**
     * Add the given feature source as a new layer to the end of the list of layers held
     * by this context and trigger a {@linkplain MapLayerListEvent}.
     * This is a convenience method equivalent to
     * {@linkplain #addLayer}(new DefaultMapLayer(featureSource, style).
     * <p>
     * If a coordinate reference system has not been set for the context an attempt is
     * made to retrieve one from the new layer and use that as the context's CRS.
     * <p>
     * If {@code style} is null, a default style is created using
     * {@linkplain SLD#createSimpleStyle(org.opengis.feature.simple.SimpleFeatureType)}.
     *
     * @param featureSource the source of the features for the new layer
     *
     * @param style a Style object to be used in rendering this layer. 
     */
    public void addLayer(FeatureSource<SimpleFeatureType, SimpleFeature> featureSource, Style style) {

        checkCRS(featureSource);
        Style layerStyle = checkStyle(style, featureSource.getSchema());
        addLayer(new DefaultMapLayer(featureSource, layerStyle, ""));
    }

    /**
     * Add the given collection source as a new layer to the end of the list of layers held
     * by this context and trigger a {@linkplain MapLayerListEvent}.
     * This is a convenience method equivalent to
     * {@linkplain #addLayer}(new DefaultMapLayer(source, style).
     * <p>
     * If a coordinate reference system has not been set for the context an attempt is
     * made to retrieve one from the new layer and use that as the context's CRS.
     *
     * @param source the source of the features for the new layer
     * @param style a Style object to be used in rendering this layer
     */
    public void addLayer(CollectionSource source, Style style) {
        // JG: for later when feature source extends source
//        if( source instanceof FeatureSource){
//            addLayer( (FeatureSource<SimpleFeatureType, SimpleFeature>) source, style);
//        }

        this.addLayer(new DefaultMapLayer(source, style, ""));
    }

    /**
     * Add a grid coverage as a new layer to the end of the list of layers held by
     * this context.
     * <p>
     * If a coordinate reference system has not been set for the context an attempt is
     * made to retrieve one from the grid coverage and use that as the context's CRS.
     *
     * @param gc the grid coverage
     * @param style a Style to be used when rendering the new layer
     */
    public void addLayer(GridCoverage gc, Style style) {
        if (style == null) {
            throw new IllegalArgumentException("style cannot be null");
        }

        checkCRS(gc.getCoordinateReferenceSystem());

        try {
            this.addLayer(FeatureUtilities.wrapGridCoverage((GridCoverage2D) gc), style);
        } catch (TransformException e) {
            DefaultMapContext.LOGGER.log(Level.WARNING, "Could not use gc", e);
        } catch (FactoryRegistryException e) {
            DefaultMapContext.LOGGER.log(Level.WARNING, "Could not use gc", e);
        } catch (SchemaException e) {
            DefaultMapContext.LOGGER.log(Level.WARNING, "Could not use gc", e);
        }
    }

    /**
     * Add a grid coverage data to be supplied by the given reader as a new layer
     * to the end of the list of layers held by this context.
     * <p>
     * If a coordinate reference system has not been set for the context an attempt is
     * made to retrieve one from the reader and use that as the context's CRS.
     *
     * @param reader the grid coverage reader
     * @param style a Style to be used when rendering the new layer
     */
    public void addLayer(AbstractGridCoverage2DReader reader, Style style) {
        if (style == null) {
            throw new IllegalArgumentException("style cannot be null");
        }

        checkCRS( reader.getCrs() );

        try {
            this.addLayer(FeatureUtilities.wrapGridCoverageReader(reader, null), style);
        } catch (TransformException e) {
            DefaultMapContext.LOGGER.log(Level.WARNING, "Could not use gc", e);
        } catch (FactoryRegistryException e) {
            DefaultMapContext.LOGGER.log(Level.WARNING, "Could not use gc", e);
        } catch (SchemaException e) {
            DefaultMapContext.LOGGER.log(Level.WARNING, "Could not use gc", e);
        }
    }

    /**
     * Add the given feature collection as a new layer to the end of the list of layers held
     * by this context and trigger a {@linkplain MapLayerListEvent}.
     * This is a convenience method equivalent to
     * {@linkplain #addLayer}(new DefaultMapLayer(collection, style).
     *
     * @param collection the collection of features for the new layer
     * @param style a Style object to be used in rendering this layer
     */
    public void addLayer(FeatureCollection<SimpleFeatureType, SimpleFeature> collection, Style style) {
        Style layerStyle = checkStyle(style, collection.getSchema());
        this.addLayer(new DefaultMapLayer(collection, layerStyle, ""));
    }

    /**
     * Add the given collection as a new layer to the end of the list of layers held
     * by this context and trigger a {@linkplain MapLayerListEvent}.
     * This is a convenience method equivalent to
     * {@linkplain #addLayer}(new DefaultMapLayer(collection, style).
     *
     * @param collection the collection of features for the new layer
     * @param style a Style object to be used in rendering this layer
     */
    public void addLayer(Collection collection, Style style) {
        if (collection instanceof FeatureCollection) {
            this.addLayer(new DefaultMapLayer((FeatureCollection<SimpleFeatureType, SimpleFeature>) collection, style, ""));
            return;
        }
        this.addLayer(new DefaultMapLayer(collection, style, ""));
    }


    /**
     * If a CRS has not been defined for this context, attempt to
     * get one from this map layer and set it as the context's CRS.
     * Invoked by addLayer methods.
     *
     * @param layer a map layer being added to the context
     */
    private void checkCRS(MapLayer layer) {
        FeatureSource<? extends FeatureType, ? extends Feature> featureSource = layer.getFeatureSource();
        if (featureSource != null) {
            checkCRS(featureSource);
        } else {
            CollectionSource source = layer.getSource();
            if (source != null) {
                checkCRS( source.getCRS() );
            }
        }
    }

    /**
     * If a CRS has not been defined for this context, attempt to
     * get one from this featureSource and set it as the context's CRS.
     * Invoked by addLayer.
     *
     * @param featureSource a feature source being added in a new layer
     */
    private void checkCRS(FeatureSource<? extends FeatureType, ? extends Feature> featureSource) {
        if (featureSource != null) {
            checkCRS( featureSource.getSchema().getCoordinateReferenceSystem() );
        }
    }

    /**
     * If a CRS has not been defined for this context yet use
     * the one provided if it is not null. We do this rather than
     * call setCoordinateReferenceSystem because we don't want to
     * trigger a change event being published to listeners.
     */
    private void checkCRS(CoordinateReferenceSystem crs) {
        if (crs != null) {
            if (areaOfInterest.getCoordinateReferenceSystem() == null) {
                this.areaOfInterest = new ReferencedEnvelope(areaOfInterest, crs);
            }
        }
    }

    /**
     * Helper for some addLayer methods that tkae a Style argument.
     * Checks if the style is null and, if so, attepts to create a default Style.

     * @param style style argument that was passed to addLayer
     * @param featureType feature type for which a default style be will made if
     *        required
     *
     * @return the input Style object if not {@code null}, or a Style instance
     *         for a default style
     */
    private Style checkStyle(Style style, SimpleFeatureType featureType) {
        if (style != null) {
            return style;
        }

        Style defaultStyle = SLD.createSimpleStyle(featureType);
        if (defaultStyle == null) {
            throw new IllegalStateException("Failed to creaate a default style for the layer");
        }

        return defaultStyle;
    }

    /**
     * Remove the given layer from this context, if present, and
     * trigger a {@linkplain MapLayerListEvent}
     *
     * @param layer the layer to be removed
     *
     * @return true if the layer was present; false otherwise
     */
    public boolean removeLayer(MapLayer layer) {
        int index = indexOf(layer);
        // getLayerBounds();
        if (index == -1) {
            return false;
        } else {
            removeLayer(index);

            return true;
        }
    }

    /**
     * Remove the layer at the given position in the list of
     * layers held by this context. The position must be valid or
     * an IndexOutOfBoundsException will result. CAlling this method
     * triggers a {@linkplain MapLayerListEvent}.
     *
     * @param index the position of the layer in this context's list of layers
     *
     * @return the layer that was removed
     */
    public MapLayer removeLayer(int index) {
        MapLayer layer = layerList.remove(index);
        // getLayerBounds();
        layer.removeMapLayerListener(layerListener);

        fireMapLayerListListenerLayerRemoved(new MapLayerListEvent(this, layer,
                index));

        return layer;
    }

    /**
     * Add an array of new layers to this context and trigger a {@link MapLayerListEvent}.
     *
     * @param layers the new layers that are to be added.
     *
     * @return the number of new layers actually added (will be less than the
     * length of the layers array if some layers were already present)
     */
    public int addLayers(MapLayer[] layers) {
        if ((layers == null) || (layers.length == 0)) {
            return 0;
        }

        int layerAdded = 0;
        MapLayer lastLayer = null;
        final int length = layers.length;
        for (int i = 0; i < length; i++) {
            if (!layerList.contains(layers[i])) {
                checkCRS(layers[i]);
                layerList.add(layers[i]);
                layerAdded++;
                lastLayer = layers[i];
                layers[i].addMapLayerListener(layerListener);
            }
        }

        if (layerAdded > 0) {
            int fromIndex = layerList.size() - layerAdded;
            int toIndex = layerList.size() - 1;

            if (layerAdded == 1) {
                fireMapLayerListListenerLayerAdded(new MapLayerListEvent(this,
                        lastLayer, fromIndex, toIndex));
            } else {
                fireMapLayerListListenerLayerAdded(new MapLayerListEvent(this,
                        null, fromIndex, toIndex));
            }
        }

        // getLayerBounds();
        return layerAdded;
    }

    /**
     * Remove an array of layers, if present, and trigger a {@link MapLayerListEvent}.
     *
     * @param layers
     *            The layers that are to be removed.
     */
    public void removeLayers(MapLayer[] layers) {
        if ((layers == null) || (layers.length == 0) || (layerList.size() == 0)) {
            return;
        }

        // compute minimum and maximum index changed
        int fromIndex = layerList.size();
        int toIndex = 0;
        int layersRemoved = 0;
        int length = layers.length;
        for (int i = 0; i < length; i++) {
            int index = layerList.indexOf(layers[i]);

            if (index == -1) {
                continue;
            }

            layersRemoved++;

            if (index < fromIndex) {
                fromIndex = index;
            }

            if (index > toIndex) {
                toIndex = index;
            }
        }
        if (layersRemoved == 0) {
            return;
        }

        // remove layerslength=layers.length;
        for (int i = 0; i < layersRemoved; i++) {
            if (layerList.remove(layers[i])) {
                layers[i].removeMapLayerListener(layerListener);
            }
        }

        // getLayerBounds();
        // fire event
        fireMapLayerListListenerLayerRemoved(new MapLayerListEvent(this, null,
                fromIndex, toIndex));
    }

    /**
     * Return this model's list of layers. If no layers are present, then an
     * empty array is returned.
     *
     * @return This model's list of layers.
     */
    public MapLayer[] getLayers() {
        MapLayer[] layers = new MapLayer[layerList.size()];

        return layerList.toArray(new MapLayer[0]);
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
        return layerList.get(index);
    }

    /**
     * @see org.geotools.map.MapContext#indexOf(org.geotools.map.MapLayer)
     */
    public int indexOf(MapLayer layer) {
        return layerList.indexOf(layer);
    }

    /**
     * Returns an iterator over the layers in this context in proper sequence.
     *
     * @return an iterator over the layers in this context in proper sequence.
     */
    public Iterator iterator() {
        return layerList.iterator();
    }

    /**
     * Get the bounding box of all the layers in this MapContext. If all the
     * layers cannot determine the bounding box in the speed required for each
     * layer, then null is returned. The bounds will be expressed in the
     * MapContext coordinate system.
     *
     * @return The bounding box of the features or null if unknown and too
     *         expensive for the method to calculate. TODO: when coordinate
     *         system information will be added reproject the bounds according
     *         to the current coordinate system
     *
     * @throws IOException
     *             DOCUMENT ME!
     *
     */
    public ReferencedEnvelope getLayerBounds() throws IOException {
        ReferencedEnvelope result = null;
        CoordinateReferenceSystem crs = areaOfInterest.getCoordinateReferenceSystem();

        final int length = layerList.size();
        MapLayer layer;
        FeatureSource<SimpleFeatureType, SimpleFeature> fs;
        ReferencedEnvelope env;
        CoordinateReferenceSystem sourceCrs;

        for (int i = 0; i < length; i++) {
            layer = layerList.get(i);
            /*fs = layer.getFeatureSource();
            sourceCrs = fs.getSchema().getDefaultGeometry()
            .getCoordinateSystem();
            env = new ReferencedEnvelope(fs.getBounds(), sourceCrs);*/

            env = layer.getBounds();
            if (env == null) {
                continue;
            } else {
                try {
                    sourceCrs = env.getCoordinateReferenceSystem();
                    if ((sourceCrs != null) && crs != null && !CRS.equalsIgnoreMetadata(sourceCrs, crs)) {
                        env = env.transform(crs, true);
                    }
                    if( sourceCrs == null && crs != null ){
                        LOGGER.log(
                                Level.SEVERE,
                                "It was not possible to get a projected bounds estimate");
                    }

                } catch (FactoryException e) {
                    LOGGER.log(
                            Level.SEVERE,
                            "Data source and map context coordinate system differ, yet it was not possible to get a projected bounds estimate...",
                            e);
                } catch (TransformException e) {
                    LOGGER.log(
                            Level.SEVERE,
                            "Data source and map context coordinate system differ, yet it was not possible to get a projected bounds estimate...",
                            e);
                }

                if (result == null) {
                    result = env;
                } else {
                    result.expandToInclude(env);
                }
            }
        }

        return result;
    }

    /**
     * Gets the current area of interest. If no area of interest is set, the
     * default is to fall back on the layer bounds
     *
     * @return Current area of interest
     *
     */
    public ReferencedEnvelope getAreaOfInterest() {
        if (areaOfInterest.isEmpty()) {
            try {
                final Envelope e = getLayerBounds();
                if (e != null) {
                    areaOfInterest = new ReferencedEnvelope(e, getCoordinateReferenceSystem());
                } else {
                    return null;
                }
            } catch (IOException e) {
                LOGGER.log(
                        Level.SEVERE,
                        "Can't get layer bounds, and area of interest is not set",
                        e);

                return null;
            }
        }

        return new ReferencedEnvelope(this.areaOfInterest);
    }

    /**
     * Get the current coordinate system of this context
     *
     * @return the coordinate system (may be null)
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return areaOfInterest.getCoordinateReferenceSystem();
    }

    /**
     * Transform the current area of interest for this context using the provided transform.
     * This may be useful for zooming and panning processes.
     *
     * @param transform  The transform to change area of interest.
     */
    public void transform(AffineTransform transform) {
        ReferencedEnvelope oldAreaOfInterest = this.areaOfInterest;

        double[] coords = new double[4];
        coords[0] = areaOfInterest.getMinX();
        coords[1] = areaOfInterest.getMinY();
        coords[2] = areaOfInterest.getMaxX();
        coords[3] = areaOfInterest.getMaxY();
        
        transform.transform(coords, 0, coords, 0, 2);

        this.areaOfInterest = new ReferencedEnvelope(coords[0], coords[2],
                coords[1], coords[3], areaOfInterest.getCoordinateReferenceSystem());

        fireMapBoundsListenerMapBoundsChanged(new MapBoundsEvent(this,
                MapBoundsEvent.AREA_OF_INTEREST_MASK,
                oldAreaOfInterest, new ReferencedEnvelope(this.areaOfInterest)));
    }

    /**
     * Change the position of a layer in this context's list of map layers.
     * This triggers a MapLayerList event.
     *
     * @param sourcePosition the layer's current position
     * @param destPosition the new position
     *
     * @throws IndexOutOfBoundsException if either position is less than zero or
     *         not less than the number of layers.
     */
    public void moveLayer(int sourcePosition, int destPosition) {
        if ((sourcePosition < 0) || (sourcePosition >= layerList.size())) {
            throw new IndexOutOfBoundsException("Source position " + sourcePosition + " out of bounds");
        }

        if ((destPosition < 0) || (destPosition >= layerList.size())) {
            throw new IndexOutOfBoundsException("Destination position " + destPosition + " out of bounds");
        }

        MapLayer layer = layerList.remove(sourcePosition);
        layerList.add(destPosition, layer);
        fireMapLayerListListenerLayerMoved(new MapLayerListEvent(this, layer,
                Math.min(sourcePosition, destPosition), Math.max(
                sourcePosition, destPosition)));
    }

    /**
     * Remove all of the map layers from this context.
     * This triggers a MapLayerListEvent.
     *
     */
    public void clearLayerList() {
        final int size = layerList.size();
        for (int i = 0; i < size; i++) {
            MapLayer layer = layerList.get(i);
            layer.removeMapLayerListener(layerListener);
        }

        layerList.clear();
        fireMapLayerListListenerLayerRemoved(new MapLayerListEvent(this, null,
                0, 1));
    }

    /**
     * Returns the number of layers in this map context
     *
     * @return the number of layers in this map context
     */
    public int getLayerCount() {
        return layerList.size();
    }

    /**
     * Getter for property abstracts.
     *
     * @return Value of property abstracts.
     */
    public String getAbstract() {
        return this.abstracts;
    }

    /**
     * Setter for property abstracts.
     *
     * @param abstractValue
     *            New value of property abstracts.
     *
     */
    public void setAbstract(String abstractValue) {
        String oldAbstracts = this.abstracts;
        this.abstracts = (abstractValue == null ? "" : abstractValue);
        propertyChangeSupport.firePropertyChange("abstract", oldAbstracts,
                abstracts);
    }

    /**
     * Getter for property contactInformation.
     *
     * @return Value of property contactInformation.
     */
    public String getContactInformation() {
        return this.contactInformation;
    }

    /**
     * Setter for property contactInformation.
     *
     * @param contactInformation
     *            New value of property contactInformation.
     */
    public void setContactInformation(String contactInformation) {
        String oldContactInformation = this.contactInformation;
        this.contactInformation = (contactInformation == null ? "" : contactInformation);
        propertyChangeSupport.firePropertyChange("contactInformation",
                oldContactInformation, contactInformation);
    }

    /**
     * Getter for property keywords.
     *
     * @return Value of property keywords.
     */
    public String[] getKeywords() {
        if (this.keywords.length == 0) {
            return this.keywords;
        } else {
            String[] copy = new String[keywords.length];
            System.arraycopy(keywords, 0, copy, 0, keywords.length);

            return copy;
        }
    }

    /**
     * Setter for property keywords.
     *
     * @param keywords
     *            New value of property keywords.
     */
    public void setKeywords(String[] keywords) {
        String[] oldKeywords = this.keywords;
        this.keywords = (keywords == null ? new String[0] : keywords);
        propertyChangeSupport.firePropertyChange("keywords", oldKeywords,
                keywords);
    }

    /**
     * Getter for property title.
     *
     * @return Value of property title.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Setter for property title.
     *
     * @param title
     *            New value of property title.
     */
    public void setTitle(String title) {
        String oldTitle = this.title;
        this.title = (title == null ? "" : title);
        propertyChangeSupport.firePropertyChange("title", oldTitle, title);
    }

    // ------------------------------------------------------------------------
    // EVENT LISTENERS AND EVENT FIRING CODE
    // ------------------------------------------------------------------------
    /**
     * Registers MapLayerListListener to receive events.
     *
     * @param listener
     *            The listener to register.
     */
    public synchronized void addMapLayerListListener(
            org.geotools.map.event.MapLayerListListener listener) {
        if (listenerList == null) {
            listenerList = new javax.swing.event.EventListenerList();
        }

        listenerList.add(org.geotools.map.event.MapLayerListListener.class,
                listener);
    }

    /**
     * Removes MapLayerListListener from the list of listeners.
     *
     * @param listener
     *            The listener to remove.
     */
    public synchronized void removeMapLayerListListener(
            org.geotools.map.event.MapLayerListListener listener) {
        if (listenerList == null) {
            return;
        }

        listenerList.remove(org.geotools.map.event.MapLayerListListener.class,
                listener);
    }

    /**
     * Notifies all registered listeners about the event.
     *
     * @param event
     *            The event to be fired
     */
    private void fireMapLayerListListenerLayerAdded(
            org.geotools.map.event.MapLayerListEvent event) {
        if (listenerList == null) {
            return;
        }

        Object[] listeners = listenerList.getListenerList();
        final int length = listeners.length;
        for (int i = length - 2; i >= 0; i -= 2) {
            if (listeners[i] == org.geotools.map.event.MapLayerListListener.class) {
                ((org.geotools.map.event.MapLayerListListener) listeners[i + 1]).layerAdded(event);
            }
        }
    }

    /**
     * Notifies all registered listeners about the event.
     *
     * @param event
     *            The event to be fired
     */
    private void fireMapLayerListListenerLayerRemoved(
            org.geotools.map.event.MapLayerListEvent event) {
        if (listenerList == null) {
            return;
        }

        Object[] listeners = listenerList.getListenerList();
        final int length = listeners.length;
        for (int i = length - 2; i >= 0; i -= 2) {
            if (listeners[i] == org.geotools.map.event.MapLayerListListener.class) {
                ((org.geotools.map.event.MapLayerListListener) listeners[i + 1]).layerRemoved(event);
            }
        }
    }

    /**
     * Notifies all registered listeners about the event.
     *
     * @param event
     *            The event to be fired
     */
    private void fireMapLayerListListenerLayerChanged(
            org.geotools.map.event.MapLayerListEvent event) {
        if (listenerList == null) {
            return;
        }

        Object[] listeners = listenerList.getListenerList();
        final int length = listeners.length;
        for (int i = length - 2; i >= 0; i -= 2) {
            if (listeners[i] == org.geotools.map.event.MapLayerListListener.class) {
                ((org.geotools.map.event.MapLayerListListener) listeners[i + 1]).layerChanged(event);
            }
        }
    }

    /**
     * Notifies all registered listeners about the event.
     *
     * @param event
     *            The event to be fired
     */
    private void fireMapLayerListListenerLayerMoved(
            org.geotools.map.event.MapLayerListEvent event) {
        if (listenerList == null) {
            return;
        }

        Object[] listeners = listenerList.getListenerList();
        final int length = listeners.length;
        for (int i = length - 2; i >= 0; i -= 2) {
            if (listeners[i] == org.geotools.map.event.MapLayerListListener.class) {
                ((org.geotools.map.event.MapLayerListListener) listeners[i + 1]).layerMoved(event);
            }
        }
    }

    /**
     * Registers PropertyChangeListener to receive events.
     *
     * @param listener
     *            The listener to register.
     */
    public synchronized void addPropertyChangeListener(
            java.beans.PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Removes PropertyChangeListener from the list of listeners.
     *
     * @param listener
     *            The listener to remove.
     */
    public synchronized void removePropertyChangeListener(
            java.beans.PropertyChangeListener listener) {
        if (listenerList == null) {
            return;
        }

        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Registers MapBoundsListener to receive events.
     *
     * @param listener
     *            The listener to register.
     */
    public synchronized void addMapBoundsListener(
            org.geotools.map.event.MapBoundsListener listener) {
        if (listenerList == null) {
            listenerList = new javax.swing.event.EventListenerList();
        }

        listenerList.add(org.geotools.map.event.MapBoundsListener.class,
                listener);
    }

    /**
     * Removes MapBoundsListener from the list of listeners.
     *
     * @param listener
     *            The listener to remove.
     */
    public synchronized void removeMapBoundsListener(
            org.geotools.map.event.MapBoundsListener listener) {
        if (listenerList == null) {
            return;
        }

        listenerList.remove(org.geotools.map.event.MapBoundsListener.class,
                listener);
    }

    /**
     * Notifies all registered listeners about the event.
     *
     * @param event
     *            The event to be fired
     */
    private void fireMapBoundsListenerMapBoundsChanged(
            org.geotools.map.event.MapBoundsEvent event) {
        if (listenerList == null) {
            return;
        }

        Object[] listeners = listenerList.getListenerList();
        final int length = listeners.length;
        for (int i = length - 2; i >= 0; i -= 2) {
            if (listeners[i] == org.geotools.map.event.MapBoundsListener.class) {
                ((org.geotools.map.event.MapBoundsListener) listeners[i + 1]).mapBoundsChanged(event);
            }
        }
    }

    /**
     * Set the area of interest. This triggers a MapBoundsEvent to be
     * published.
     *
     * @param areaOfInterest the new area of interest
     *
     * @throws IllegalArgumentException if the argument is {@code null}
     *
     * @deprecated Use of this method is not safe. Please use
     *             {@linkplain #setAreaOfInterest(Envelope, CoordinateReferenceSystem)}
     *             instead.
     */
    public void setAreaOfInterest(Envelope areaOfInterest)
            throws IllegalArgumentException {

        CoordinateReferenceSystem crs = this.areaOfInterest.getCoordinateReferenceSystem();
        if (crs == null) {
            crs = DefaultGeographicCRS.WGS84;
        }
        ReferencedEnvelope refEnv = new ReferencedEnvelope(areaOfInterest, crs);
        setAreaOfInterest(refEnv);
    }

    /**
     * Set the area of interest. This triggers a MapBoundsEvent to be
     * published.
     *
     * @param areaOfInterest the new area of interest
     * @param coordinateReferenceSystem the CRS for the new area of interest
     *
     * @throws IllegalArgumentException if areaOfInterest is {@code null}
     */
    public void setAreaOfInterest(Envelope areaOfInterest, CoordinateReferenceSystem crs)
            throws IllegalArgumentException {

        if (areaOfInterest == null) {
            throw new IllegalArgumentException("areaOfInterest should not be null");
        }

        setAreaOfInterest(new ReferencedEnvelope(areaOfInterest, crs));
    }

    /**
     * Set the area of interest. This triggers a MapBoundsEvent to be
     * published.
     *
     * @param areaOfInterest the new area of interest
     *
     * @throws IllegalArgumentException if the provided areaOfInterest is {@code null}
     *         or does not have a coordinate reference system
     */
    public void setAreaOfInterest(ReferencedEnvelope areaOfInterest) {
        if (areaOfInterest == null) {
            throw new IllegalArgumentException("areaOfInterest must not be null");
        }

        ReferencedEnvelope oldAreaOfInterest = this.areaOfInterest;
        CoordinateReferenceSystem oldCRS = this.areaOfInterest.getCoordinateReferenceSystem();

        this.areaOfInterest = new ReferencedEnvelope(areaOfInterest);

        int flags = 0;

        if (oldCRS != null) {
            if (!CRS.equalsIgnoreMetadata( this.areaOfInterest.getCoordinateReferenceSystem(), oldCRS )) {
                flags |= MapBoundsEvent.COORDINATE_SYSTEM_MASK;
            }
        } else if (this.areaOfInterest.getCoordinateReferenceSystem() != null) {
            flags |= MapBoundsEvent.COORDINATE_SYSTEM_MASK;
        }

        if (!this.areaOfInterest.boundsEquals2D(oldAreaOfInterest, 1.0e-4d)) {
            flags |= MapBoundsEvent.AREA_OF_INTEREST_MASK;
        }

        fireMapBoundsListenerMapBoundsChanged(
                new MapBoundsEvent(this, flags, oldAreaOfInterest, new ReferencedEnvelope(this.areaOfInterest)));
    }

    /**
     * Set or change the coordinate reference system for this context.
     * This will trigger a MapBoundsEvent to be published to listeners.
     *
     * @throws FactoryException
     * @throws TransformException
     *
     */
    public void setCoordinateReferenceSystem(CoordinateReferenceSystem crs)
            throws TransformException, FactoryException {

        final ReferencedEnvelope oldAreaOfInterest = this.areaOfInterest;
        final CoordinateReferenceSystem oldCRS = this.areaOfInterest.getCoordinateReferenceSystem();

        if (oldCRS != null) {
            if (!CRS.equalsIgnoreMetadata(crs, oldCRS)) {
                this.areaOfInterest = this.areaOfInterest.transform(crs, true);
            }
        } else {
            this.areaOfInterest = new ReferencedEnvelope(areaOfInterest, crs);
        }

        fireMapBoundsListenerMapBoundsChanged(new MapBoundsEvent(this,
                MapBoundsEvent.COORDINATE_SYSTEM_MASK,
                oldAreaOfInterest, new ReferencedEnvelope(this.areaOfInterest)));
    }
}
