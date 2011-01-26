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

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.memory.CollectionSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.event.MapLayerListEvent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.feature.type.FeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A kinder gentler implementation of {@linkplain org.geotools.map.MapContext} that produces defaults
 * as needed.
 * <p>
 * This implementation produces defaults as needed for:
 * <ul>
 * <li>Map Bounds are generated from the first layer if needed</li>
 * <li>Default Styles are generated if needed for display</li>
 * </ul>
 * 
 * @author Andrea Aime
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/library/render/src/main/java/org/geotools
 *         /map/DefaultMapContext.java $
 */
public class DefaultMapContext extends MapContext {

    /**
     * Creates a default empty map context. The coordinate reference system for the map context
     * should be set explicitly, or implicitly via {@code addLayer} prior to using the context.
     */
    public DefaultMapContext() {
        super((CoordinateReferenceSystem) null);
    }

    /**
     * Creates a default empty map context
     * 
     * @param crs
     *            the coordindate reference system to be used with this context (may be null and set
     *            later)
     */
    public DefaultMapContext(final CoordinateReferenceSystem crs) {
        super(null, null, null, null, null, crs);
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
    public DefaultMapContext(MapLayer[] layers) {
        super(layers, DefaultGeographicCRS.WGS84);
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
    public DefaultMapContext(MapLayer[] layers, final CoordinateReferenceSystem crs) {
        super(layers, null, null, null, null, crs);
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
    public DefaultMapContext(MapLayer[] layers, String title, String contextAbstract,
            String contactInformation, String[] keywords) {
        super(layers, title, contextAbstract, contactInformation, keywords, null);
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
    public DefaultMapContext(MapLayer[] layers, String title, String contextAbstract,
            String contactInformation, String[] keywords, final CoordinateReferenceSystem crs) {
        super(layers, title, contextAbstract, contactInformation, keywords, crs);
    }

    /**
     * Add a new layer if not already present and trigger a {@linkplain MapLayerListEvent}. If a
     * coordinate reference system has not been set for the context an attempt is made to retrieve
     * one from the new layer and use that as the context's CRS.
     * 
     * @param index
     *            the position at which to insert the layer in the list of layers held by this
     *            context
     * 
     * @param layer
     *            the map layer to add
     * 
     * @return true if the layer was added; false otherwise (layer was already present)
     */
    @SuppressWarnings("deprecation")
    public boolean addLayer(int index, MapLayer mapLayer) {
        // checkCRS(mapLayer);
        Layer layer = mapLayer.toLayer();
        layers().add(index, layer);
        return true;
    }

    /**
     * Add a new layer, if not already present, to the end of the list of layers held by this
     * context and trigger a {@linkplain MapLayerListEvent} If a coordinate reference system has not
     * been set for the context an attempt is made to retrieve one from the new layer and use that
     * as the context's CRS.
     * 
     * @param layer
     *            the map layer to add
     * 
     * @return true if the layer was added; false otherwise (layer was already present)
     */
    @SuppressWarnings("deprecation")
    public boolean addLayer(MapLayer mapLayer) {
        //checkCRS(mapLayer);
        layers().add(mapLayer.toLayer());
        return true;
    }

    /**
     * Add the given feature source as a new layer to the end of the list of layers held by this
     * context and trigger a {@linkplain MapLayerListEvent}. This is a convenience method equivalent
     * to {@linkplain #addLayer}(new DefaultMapLayer(featureSource, style).
     * <p>
     * If a coordinate reference system has not been set for the context an attempt is made to
     * retrieve one from the new layer and use that as the context's CRS.
     * <p>
     * If {@code style} is null, a default style is created using
     * {@linkplain SLD#createSimpleStyle(org.opengis.feature.simple.SimpleFeatureType)}.
     * 
     * @param featureSource
     *            the source of the features for the new layer
     * 
     * @param style
     *            a Style object to be used in rendering this layer.
     */
    public void addLayer(FeatureSource featureSource, Style style) {
        //checkCRS(featureSource);
        Style layerStyle = checkStyle(style, featureSource.getSchema());
        addLayer(new DefaultMapLayer(featureSource, layerStyle, ""));
    }

    /**
     * Add the given collection source as a new layer to the end of the list of layers held by this
     * context and trigger a {@linkplain MapLayerListEvent}. This is a convenience method equivalent
     * to {@linkplain #addLayer}(new DefaultMapLayer(source, style).
     * <p>
     * If a coordinate reference system has not been set for the context an attempt is made to
     * retrieve one from the new layer and use that as the context's CRS.
     * 
     * @param source
     *            the source of the features for the new layer
     * @param style
     *            a Style object to be used in rendering this layer
     */
    public void addLayer(CollectionSource source, Style style) {
        throw new UnsupportedOperationException("FeatureSource required");
    }

    /**
     * Add a grid coverage as a new layer to the end of the list of layers held by this context.
     * <p>
     * If a coordinate reference system has not been set for the context an attempt is made to
     * retrieve one from the grid coverage and use that as the context's CRS.
     * 
     * @param gc
     *            the grid coverage
     * @param style
     *            a Style to be used when rendering the new layer
     */
    public void addLayer(GridCoverage gc, Style style) {
        if (style == null) {
            throw new IllegalArgumentException("style cannot be null");
        }
        // checkCRS(gc.getCoordinateReferenceSystem());
        if (gc instanceof GridCoverage2D) {
            Layer layer = new GridCoverageLayer((GridCoverage2D) gc, style);
            layers().add(layer);
        } else {
            throw new UnsupportedOperationException("GridCoverage2D required");
        }
    }

    /**
     * Add a grid coverage data to be supplied by the given reader as a new layer to the end of the
     * list of layers held by this context.
     * <p>
     * If a coordinate reference system has not been set for the context an attempt is made to
     * retrieve one from the reader and use that as the context's CRS.
     * 
     * @param reader
     *            the grid coverage reader
     * @param style
     *            a Style to be used when rendering the new layer
     */
    public void addLayer(AbstractGridCoverage2DReader reader, Style style) {
        if (style == null) {
            throw new IllegalArgumentException("Style cannot be null");
        }
        // checkCRS(reader.getCrs());
        Layer layer = new GridReaderLayer(reader, style);
        layers().add(layer);
    }

    /**
     * Add the given feature collection as a new layer to the end of the list of layers held by this
     * context and trigger a {@linkplain MapLayerListEvent}. This is a convenience method equivalent
     * to {@linkplain #addLayer}(new DefaultMapLayer(collection, style).
     * 
     * @param collection
     *            the collection of features for the new layer
     * @param style
     *            a Style object to be used in rendering this layer
     */
    public void addLayer(FeatureCollection featureCollection, Style style) {
        FeatureType schema = featureCollection.getSchema();
        Style layerStyle = checkStyle(style, schema);
        // checkCRS(schema.getCoordinateReferenceSystem()); // Jody: added for consistency
        Layer layer = new FeatureLayer(featureCollection, layerStyle);
        this.layers().add(layer);
    }

    /**
     * Add the given collection as a new layer to the end of the list of layers held by this context
     * and trigger a {@linkplain MapLayerListEvent}. This is a convenience method equivalent to
     * {@linkplain #addLayer}(new DefaultMapLayer(collection, style).
     * 
     * @param collection
     *            the collection of features for the new layer
     * @param style
     *            a Style object to be used in rendering this layer
     */
    @SuppressWarnings("unchecked")
    public void addLayer(Collection collection, Style style) {
        if (collection instanceof FeatureCollection) {
            FeatureCollection featureCollection = (FeatureCollection) collection;
            addLayer( featureCollection, style);
        } else {
            throw new IllegalArgumentException("FeatureCollection required");
        }
    }

    /**
     * If a CRS has not been defined for this context, attempt to get one from this map layer and
     * set it as the context's CRS. Invoked by addLayer methods.
     * 
     * @param layer
     *            a map layer being added to the context
     */
//    @SuppressWarnings("deprecation")
//    private void checkCRS(MapLayer layer) {
//        FeatureSource<? extends FeatureType, ? extends Feature> featureSource = layer
//                .getFeatureSource();
//        if (featureSource != null) {
//            checkCRS(featureSource);
//        } else {
//            CollectionSource source = layer.getSource();
//            if (source != null) {
//                checkCRS(source.getCRS());
//            }
//        }
//    }

    /**
     * If a CRS has not been defined for this context, attempt to get one from this featureSource
     * and set it as the context's CRS. Invoked by addLayer.
     * 
     * @param featureSource
     *            a feature source being added in a new layer
     */
//    private void checkCRS(FeatureSource<? extends FeatureType, ? extends Feature> featureSource) {
//        if (featureSource != null) {
//            checkCRS(featureSource.getSchema().getCoordinateReferenceSystem());
//        }
//    }

    /**
     * Sets the viewport CRS if needed.
     * <p>
     * This amounts to the viewport taking its default CRS from the first provided layer.
     */
//    private void checkCRS(CoordinateReferenceSystem crs) {
//        if( viewport != null && viewport.getCoordianteReferenceSystem() != null ){
//            if (crs != null) {
//                getViewport().setCoordinateReferenceSystem(crs);
//            }
//        }
//    }

    /**
     * Helper for some addLayer methods that take a Style argument. Checks if the style is null and,
     * if so, attepts to create a default Style.
     * 
     * @param style
     *            style argument that was passed to addLayer
     * @param featureType
     *            feature type for which a default style be will made if required
     * 
     * @return the input Style object if not {@code null}, or a Style instance for a default style
     */
    private Style checkStyle(Style style, FeatureType featureType) {
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
     * Remove the given layer from this context, if present, and trigger a
     * {@linkplain MapLayerListEvent}
     * 
     * @param layer
     *            the layer to be removed
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
     * Remove the layer at the given position in the list of layers held by this context. The
     * position must be valid or an IndexOutOfBoundsException will result. CAlling this method
     * triggers a {@linkplain MapLayerListEvent}.
     * 
     * @param index
     *            the position of the layer in this context's list of layers
     * 
     * @return the layer that was removed
     */
    public MapLayer removeLayer(int index) {
        Layer removed = layers().remove(index);
        return new DefaultMapLayer(removed);
    }

    /**
     * Add an array of new layers to this context and trigger a {@link MapLayerListEvent}.
     * 
     * @param layers
     *            the new layers that are to be added.
     * 
     * @return the number of new layers actually added (will be less than the length of the layers
     *         array if some layers were already present)
     */
    @SuppressWarnings("deprecation")
    public int addLayers(MapLayer[] array) {
        if ((array == null) || (array.length == 0)) {
            return 0;
        }
        List<Layer> layersToAdd = toLayerList(array);
        int count = layers().addAllAbsent(layersToAdd);
        return count;
    }

    /**
     * Remove an array of layers, if present, and trigger a {@link MapLayerListEvent}.
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
     * Return this model's list of layers. If no layers are present, then an empty array is
     * returned.
     * 
     * @return This model's list of layers.
     */
    @SuppressWarnings("deprecation")
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
    @SuppressWarnings("deprecation")
    public MapLayer getLayer(int index) throws IndexOutOfBoundsException {
        Layer layer = layers().get(index);
        return new DefaultMapLayer(layer);
    }

    /**
     * Gets the current area of interest. If no area of interest is set, the default is to fall back
     * on the layer bounds
     * 
     * @return Current area of interest
     * 
     */
    public ReferencedEnvelope getAreaOfInterest() {
        return getBounds();        
    }

    /**
     * Remove all of the map layers from this context. This triggers a MapLayerListEvent.
     * 
     */
    public void clearLayerList() {
        layers().clear();
    }
    
    @Override
    public synchronized MapViewport getViewport() {
        if(viewport == null) {
            viewport = new MapViewport();
            try {
                ReferencedEnvelope layerBounds = getLayerBounds();
                if(layerBounds != null) {
                    viewport.setBounds( layerBounds );
                    viewport.setCoordinateReferenceSystem(layerBounds.getCoordinateReferenceSystem());
                }
            } catch (IOException e) {
            }
        }
        return viewport;
    }
    
}
