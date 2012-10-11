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

import java.io.IOException;

import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureEvent;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.event.MapLayerEvent;
import org.geotools.referencing.CRS;
import org.geotools.styling.Style;
import org.geotools.util.Utilities;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Layer responsible for rendering vector information provided by a FeatureSource.
 * <p>
 * The FeatureLayer combines:
 * <ul>
 * <li>data: FeatureSource</li>
 * <li>style: Style</li>
 * </ul>
 * Please note that a StyleLayerDescriptor (defined by SLD) document is usually used to describe the
 * rendering requirements for an entire Map; while a Style (defined by SE) is focused on a single
 * layer of content
 * @since 2.7
 * @version 8.0
 *
 * @source $URL$
 */
public class FeatureLayer extends StyleLayer {

    /** FeatureSource offering content for display */
    protected FeatureSource<? extends FeatureType, ? extends Feature> featureSource;

    /** Query use to limit content of featureSource */
    protected Query query;

    /** Listener to forward feature source events as layer events */
    protected FeatureListener sourceListener;

    /**
     * Flag to force ignoring all text symbolizers. 
     */
    private boolean hideLabels;

    /**
     * Label opacity if specified.
     */
    private Float labelOpacity;

    /**
     * Creates a new instance of FeatureLayer
     * 
     * @param featureSource
     *            the data source for this layer
     * @param style
     *            the style used to represent this layer
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public FeatureLayer(FeatureSource featureSource, Style style) {
        super(style);
        this.featureSource = featureSource;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public FeatureLayer(FeatureSource featureSource, Style style, String title) {
        super(style,title);
        this.featureSource = featureSource;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public FeatureLayer(FeatureCollection collection, Style style) {
        super(style);
        this.featureSource = DataUtilities.source(collection);
        this.style = style;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public FeatureLayer(FeatureCollection collection, Style style, String title) {
        super( style, title );
        this.featureSource = DataUtilities.source(collection);
    }

    /**
     * Used to connect/disconnect a FeatureListener if any map layer listeners are registered.
     */
    protected synchronized void connectDataListener(boolean listen) {
        if (sourceListener == null) {
            sourceListener = new FeatureListener() {
                public void changed(FeatureEvent featureEvent) {
                    fireMapLayerListenerLayerChanged(MapLayerEvent.DATA_CHANGED);
                }
            };
        }
        if (listen) {
            featureSource.addFeatureListener(sourceListener);
        } else {
            featureSource.removeFeatureListener(sourceListener);
        }
    }

    @Override
    public void dispose() {
        preDispose();
        if (featureSource != null) {
            if (sourceListener != null) {
                featureSource.removeFeatureListener(sourceListener);
            }
            featureSource = null;
        }
        style = null;
        query = null;
        super.dispose();
    }

    /**
     * Get the feature source for this layer.
     * 
     * @return feature source for the contents of this layer
     */
    @Override
    public FeatureSource<?,?> getFeatureSource() {
        return featureSource;
    }

    /**
     * Get the feature source for this layer.
     * 
     * @return SimpleFeatureSource for this layer, or null if not available
     */
    public SimpleFeatureSource getSimpleFeatureSource() {
        if (featureSource instanceof SimpleFeatureSource) {
            return (SimpleFeatureSource) featureSource;
        }
        return null; // not available
    }

    /**
     * Returns the definition query (filter) for this layer. If no definition query has been defined
     * {@link Query.ALL} is returned.
     * 
     * @return Query used to process content prior to display, or Query.ALL to indicate all content
     *         is used
     */
    public Query getQuery() {
        if (query == null) {
            return Query.ALL;
        } else {
            return query;
        }
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
     * It is desirable to not include attributes at all but let the layer user (a renderer?) to
     * decide wich attributes are actually needed to perform its requiered operation.
     * </p>
     * 
     * @param query
     */
    public void setQuery(Query query) {
        this.query = query;
        fireMapLayerListenerLayerChanged(MapLayerEvent.FILTER_CHANGED);
    }

    /**
     * Determines if all text symbolizers configured for a layer should be ignored during
     * rendering.
     * 
     * @return {@code true} if the text symbolizers should be ignored, or {@code false} otherwise.
     */
    public boolean getHideLabels() {
        return hideLabels;
    }
    
    /**
     * Sets wheather all text symbolizers configured for a layer should be ignored during rendering.
     * 
     * This allows to hide all labels for the layer without changing the style.
     * 
     * @param hideLabels {@code true} to ignore all text symbolizers;
     *        {@code false} to restore default behavior.
     */
    public void setHideLabels(boolean hideLabels) {
        if (this.hideLabels != hideLabels) {
            this.hideLabels = hideLabels;
            fireMapLayerListenerLayerChanged(MapLayerEvent.STYLE_CHANGED);
        }
    }

    /**
     * Determines layer's labels opacity as used during rendering.
     *
     * @return opacity value (0.0 for fully transparent, 1.0 for fully opaque) or {@code null} if
     *         no opacity should be applied.
     */
    public Float getLabelOpacity() {
        return labelOpacity;
    }

    /**
     * Sets layer's labels opacity which should be used during rendering.
     *
     * The opacity (if set) is applied to already rendered labels in a single step.
     * It does not change the opacity level of used TextSymbolizers but rather acts as
     * a postprocessing step for already rendered labels.
     *
     * @param labelOpacity opacity value (0.0 for fully transparent labels, 1.0 for fully opaque) or
     *        {@code null} if default rendering should be used. 
     */
    public void setLabelOpacity(Float labelOpacity) {
        Float prevOpacity = this.labelOpacity;
        if (labelOpacity != null) {
            labelOpacity = Math.min(1.0f, Math.max(0.0f, labelOpacity));
        }
        this.labelOpacity = labelOpacity;
        if (!Utilities.equals(prevOpacity, labelOpacity)) {
            fireMapLayerListenerLayerChanged(MapLayerEvent.STYLE_CHANGED);
        }
    }

    @Override
    public ReferencedEnvelope getBounds() {
        try {
            ReferencedEnvelope bounds;
            if(query != null) {
                bounds = featureSource.getBounds(query);
            } else {
                bounds = featureSource.getBounds();
            }
            if( bounds != null ){
                FeatureType schema = featureSource.getSchema();
                CoordinateReferenceSystem schemaCrs = schema.getCoordinateReferenceSystem();
                CoordinateReferenceSystem boundsCrs = bounds.getCoordinateReferenceSystem();
                
                if( boundsCrs == null && schemaCrs != null ){
                    LOGGER.warning("Bounds crs not defined; assuming bounds from schema are correct for "+featureSource );
                    bounds = new ReferencedEnvelope(bounds.getMinX(),bounds.getMaxX(),bounds.getMinY(),bounds.getMaxY(),schemaCrs);
                }
                if( boundsCrs != null && schemaCrs != null && !CRS.equalsIgnoreMetadata(boundsCrs, schemaCrs)){
                    LOGGER.warning("Bounds crs and schema crs are not consistent; forcing the use of the schema crs so they are consistent" );
                    //bounds = bounds.transform(schemaCrs, true );
                    bounds = new ReferencedEnvelope(bounds.getMinX(),bounds.getMaxX(),bounds.getMinY(),bounds.getMaxY(),schemaCrs);
                }
                return bounds;
            }            
        } catch (IOException e) {
            // feature bounds unavailable
        }
        
        CoordinateReferenceSystem crs = featureSource.getSchema().getCoordinateReferenceSystem();
        if (crs != null) {
            // returns the envelope based on the CoordinateReferenceSystem
            Envelope envelope = CRS.getEnvelope(crs);
            if (envelope != null) {
                return new ReferencedEnvelope(envelope); // nice!
            }
            else {
                return new ReferencedEnvelope(crs); // empty bounds
            }
        }
        else {
            return null; // unknown
        }
    }

}
