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

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureEvent;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.memory.CollectionSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.event.MapLayerEvent;
import org.geotools.styling.Style;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Implementation of {@link MapLayer} without restricting the return type of {@link #getFeatureSource()}
 * allows better support of the DataAccess API;
 * 
 * <p>
 * This implementation does not support a collection or grid coverage source.
 * <p>
 * This implementation was almost entirely stolen from that of {@link DefaultMapLayer}.
 * <p>
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @source $URL$
 * @deprecated Please use MapLayer directly; or you are just interested in rendering a subclass of Layer
 */
public class FeatureSourceMapLayer extends MapLayer {

    /**
     * Constructor
     * 
     * @param featureSource
     *            the data source for this layer
     * @param style
     *            the style used to represent this layer
     * @param title
     *            the layer title
     */
    public FeatureSourceMapLayer(
            FeatureSource<? extends FeatureType, ? extends Feature> featureSource, Style style,
            String title) {
        super( featureSource, style, title );
    }

    /**
     * Convenience constructor that sets title to the empty string.
     * 
     * @param featureSource
     *            the data source for this layer
     * @param style
     *            the style used to represent this layer
     */
    public FeatureSourceMapLayer(
            FeatureSource<? extends FeatureType, ? extends Feature> featureSource, Style style) {
        super(featureSource, style, "");
    }

}
