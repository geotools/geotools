/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swt.tool;

import java.lang.ref.WeakReference;
import java.util.logging.Logger;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * Abstract base class for helper classes used by {@code InfoTool} to query {@code MapLayers}.
 *
 * <p>The primary reason for having this class is to avoid loading grid coverage classes unless they
 * are really needed, and thus avoid the need for users to have JAI in the classpath when working
 * with vector data.
 *
 * <p>The type parameter <code>&lt;T&gt;</code> defines the return type of the {@linkplain #getInfo}
 * method.
 *
 * @see InfoTool
 * @author Michael Bedward
 * @since 2.6
 */
public abstract class InfoToolHelper<T> {
    private static final Logger LOGGER = Logger.getLogger(VectorLayerHelper.class.getName());

    private final WeakReference<MapContent> contentRef;
    private CoordinateReferenceSystem dataCRS;
    private boolean transformRequired;
    private boolean transformFailed;
    private MathTransform transform;

    /**
     * Protected constructor.
     *
     * @param content the map context
     * @param dataCRS the coordinate reference system of the feature data that will be queried by
     *     this helper
     */
    protected InfoToolHelper(MapContent content, CoordinateReferenceSystem dataCRS) {
        this.contentRef = new WeakReference<MapContent>(content);
        setCRS(dataCRS);
    }

    /**
     * Get feature data at the given position.
     *
     * @param pos the location to query
     * @param params additional parameters as optionally defined by the sub-class
     * @return data of type {@code T} as defined by the sub-class
     * @see #isValid()
     */
    public abstract T getInfo(DirectPosition2D pos, Object... params) throws Exception;

    /**
     * Query if this helper has a reference to a {@code MapContext} and {@code MapLayer}.
     *
     * <p>Helpers only hold a {@linkplain WeakReference} to the map context and layer with which
     * they are working to avoid blocking garbage collection when layers are discarded. If this
     * method returns {@code false} the helper should be re-created.
     *
     * <pre><code>
     * //
     * // Example using a VectorLayerHelper...
     * //
     * VectorLayerHelper helper = ...
     *
     * if (helper != null && helper.isValid()) {
     *     FeatureCollection coll = helper.getInfo(queryLocation, ...);
     *     // do something useful with results
     *
     * } else {
     *     // (Re-)create the helper
     *     // Note: example only; this obviously depends on your use case
     *     helper = new VectorLayerHelper(context, layer);
     * }
     * </code></pre>
     *
     */
    public abstract boolean isValid();

    /**
     * Get the {@code MapContent} associated with this helper. The helper maintains only a
     * {@linkplain WeakReference} to the context.
     *
     * @return the map context or null if it is no longer current.
     */
    public MapContent getMapContent() {
        return contentRef != null ? contentRef.get() : null;
    }

    /**
     * Check if queries with this helper involve transforming between coordinate systems.
     *
     * @return true if coordinte transformation is required; false otherwise
     */
    protected boolean isTransformRequired() {
        return transformRequired;
    }

    /**
     * Get the {@code MathTransform} to reproject data from the coordinate system of the {@code
     * MapContext's} to that of the {@code MapLayer}.
     *
     * @return the transform or {@code null} if either the layer's coordinate system is the same as
     *     that of the map context, or either has a {@code null} CRS.
     */
    public MathTransform getTransform() {
        if (transform == null && !transformFailed && dataCRS != null) {
            MapContent content = getMapContent();
            if (content == null) {
                throw new IllegalStateException("map context should not be null");
            }

            CoordinateReferenceSystem contextCRS = content.getCoordinateReferenceSystem();
            try {
                transform = CRS.findMathTransform(contextCRS, dataCRS, true);
            } catch (Exception ex) {
                LOGGER.warning("Can't transform map context to map layer CRS");
                transformFailed = true;
            }
        }

        return transform;
    }

    /**
     * Set the coordinate reference system that pertains to the feature data that will be queried by
     * this helper.
     *
     * @param crs data coordinate reference system
     */
    protected void setCRS(CoordinateReferenceSystem crs) {
        this.dataCRS = crs;

        MapContent content = getMapContent();
        if (content == null) {
            throw new IllegalStateException("map context should not be null");
        }

        final CoordinateReferenceSystem contextCRS = content.getCoordinateReferenceSystem();
        transformRequired = false;
        if (contextCRS != null && crs != null && !CRS.equalsIgnoreMetadata(contextCRS, dataCRS)) {
            transformRequired = true;
        }
    }
}
