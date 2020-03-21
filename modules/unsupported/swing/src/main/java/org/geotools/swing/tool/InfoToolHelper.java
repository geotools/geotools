/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.tool;

import java.awt.geom.AffineTransform;
import java.lang.ref.WeakReference;
import java.util.logging.Logger;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.map.Layer;
import org.geotools.map.MapBoundsEvent;
import org.geotools.map.MapBoundsListener;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * Abstract base class for helper classes used by {@linkplain InfoTool} to query features in map
 * layers.
 *
 * @author Michael Bedward
 * @since 2.6
 * @version $URL$
 */
public abstract class InfoToolHelper implements MapBoundsListener {
    private static final Logger LOGGER = Logging.getLogger(InfoToolHelper.class);

    /**
     * String key used for the position element in the {@code Map} passed to {@linkplain #getInfo(
     * org.geotools.util.KVP )}.
     */
    public static final String KEY_POSITION = "pos";

    protected WeakReference<MapContent> contentRef;
    protected WeakReference<Layer> layerRef;

    private boolean transformFailed;
    private MathTransform transform;

    /**
     * CAlled by the helper lookup system when selecting a helper for a given layer.
     *
     * @param layer the layer
     * @return {@code true} is this helper can handle the layer
     * @throws IllegalArgumentException if {@code layer} is {@code null}
     */
    public abstract boolean isSupportedLayer(Layer layer);

    /**
     * Gets layer data at the specified position. If there are no feature data at the position, an
     * empty {@code InfoToolResult} object is returned.
     *
     * @param pos query position
     * @return layer data
     * @throws Exception on error querying the layer
     */
    public abstract InfoToolResult getInfo(DirectPosition2D pos) throws Exception;

    /**
     * Checks if this helper is holding a reference to a {@code MapContent} and a {@code
     * Layer}.Helpers only hold a {@code WeakReference} to both the map content and layer to avoid
     * blocking garbage collection when layers are discarded.
     *
     * @return {@code true} if both map content and layer references are valid
     */
    public boolean isValid() {
        return contentRef != null
                && contentRef.get() != null
                && layerRef != null
                && layerRef.get() != null;
    }

    /**
     * Sets the map content for this helper.
     *
     * @param content the map content
     * @throws IllegalArgumentException if {@code content} is {@code null}
     */
    public void setMapContent(MapContent content) {
        if (content == null) {
            throw new IllegalArgumentException("content must not be null");
        }

        contentRef = new WeakReference<MapContent>(content);
        clearTransform();
    }

    /**
     * Gets the map content associated with this helper.
     *
     * @return the map content
     */
    public MapContent getMapContent() {
        return contentRef != null ? contentRef.get() : null;
    }

    /**
     * Sets the map layer for this helper.
     *
     * @param layer the map layer
     * @throws IllegalArgumentException if {@code layer} is {@code null}
     */
    public void setLayer(Layer layer) {
        if (layer == null) {
            throw new IllegalArgumentException("layer must not be null");
        }

        layerRef = new WeakReference<Layer>(layer);
        clearTransform();
    }

    /** Gets the map layer associated with this helper. */
    public Layer getLayer() {
        return layerRef != null ? layerRef.get() : null;
    }

    /**
     * A method from the {@code MapBoundsListener} interface used to listen for a change to the map
     * content's coordinate reference system.
     */
    @Override
    public void mapBoundsChanged(MapBoundsEvent event) {
        clearTransform();
    }

    /**
     * Gets the {@code MathTransform} used to convert coordinates from the projection being used by
     * the {@code MapContent} to that of the {@code Layer}.
     *
     * @return the transform or {@code null} if the layer's CRS is the same as that of the map
     *     content, or if either has no CRS defined
     */
    protected MathTransform getContentToLayerTransform() {
        if (transform == null && !transformFailed) {
            MapContent content = getMapContent();
            Layer layer = getLayer();

            if (content != null && layer != null) {
                CoordinateReferenceSystem contentCRS = content.getCoordinateReferenceSystem();
                CoordinateReferenceSystem layerCRS =
                        layer.getFeatureSource().getSchema().getCoordinateReferenceSystem();

                if (contentCRS != null && layerCRS != null) {
                    if (CRS.equalsIgnoreMetadata(contentCRS, layerCRS)) {
                        transform = new AffineTransform2D(new AffineTransform());

                    } else {
                        try {
                            transform = CRS.findMathTransform(contentCRS, layerCRS, true);
                        } catch (Exception ex) {
                            LOGGER.warning("Can't transform map content CRS to layer CRS");
                            transformFailed = true;
                        }
                    }
                }

            } else {
                // one or both of content and layer CRS is null
                transform = new AffineTransform2D(new AffineTransform());
            }
        }

        return transform;
    }

    protected boolean isTransformRequired() {
        return !getContentToLayerTransform().isIdentity();
    }

    protected void clearTransform() {
        transform = null;
        transformFailed = false;
    }
}
