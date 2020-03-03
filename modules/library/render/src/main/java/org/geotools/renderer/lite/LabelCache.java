/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.List;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.styling.TextSymbolizer;
import org.geotools.util.NumberRange;
import org.opengis.feature.Feature;

/**
 * Used to cache labels prior to their being drawn on the screen.
 *
 * <p>Implementations exisit with different trade offs about what labels can fit where.
 *
 * @author jeichar
 * @since 2.2
 */
public interface LabelCache {
    /** Called by renderer to indicate that the rendering process is starting. */
    void start();

    /**
     * Called by renderer to indication the start of rendering a layer. Will add the layer to the
     * set of active layers.
     *
     * @param layerId an id for the layer
     */
    void startLayer(String layerId);

    /**
     * Puts a Label in the cache.
     *
     * @param layerId id indicating the layer the feature is part of
     * @param symbolizer The symbolizer containing the style information
     * @param feature the feature that has the information required for the symbolizer to calculate
     *     the required render information.
     * @param shape the shape to be labeled. This is in screen coordinates.
     * @param scaleRange the scaleRange that the symbolizer is legal
     */
    void put(
            String layerId,
            TextSymbolizer symbolizer,
            Feature feature,
            LiteShape2 shape,
            NumberRange<Double> scaleRange);

    /**
     * Reserve the provided geometry prior to sorting out where labels can go.
     *
     * <p>This facility is used to reserve an area so that labels do not end up overlapping on
     * screen constructs like scalebars or north arrows etc...
     *
     * @param geometry The Area of the screen to reserve (in screen coordinates)
     */
    void put(Rectangle2D geometry);

    /**
     * Called to indicate that a layer is done rendering. The method may draw labels if appropriate
     * for the labeling algorithm
     *
     * @param graphics the graphics to draw on.
     * @param displayArea The size of the display area
     * @param layerId an id for the layer
     */
    void endLayer(String layerId, Graphics2D graphics, Rectangle displayArea);

    /**
     * Called to indicate that the map is done rendering. The method may draw labels if appropriate
     * for the labeling algorithm
     *
     * @param graphics the graphics to draw on.
     * @param displayArea The size of the display area.
     */
    void end(Graphics2D graphics, Rectangle displayArea);

    /** Tells the cache to stop labelling. */
    void stop();

    /** Clears the cache completely */
    public void clear();

    /**
     * Clears the cache of all information relating to the layer identified.
     *
     * @param layerId id of the layer
     */
    public void clear(String layerId);

    /**
     * Leaves the label information in the cache but ignores it when calculating what labels are
     * drawn.
     *
     * @param layerId id of the layer to disable.
     */
    public void disableLayer(String layerId);

    /**
     * Enable a layer after being disabled. If startLayer is called this does not need to be called
     * as start layer implicitely activates the layer.
     *
     * @param layerId layer to activate.
     */
    public void enableLayer(String layerId);

    /**
     * Return a list with all the values in priority order. Both grouped and non-grouped
     *
     * @return list with all values in priority order
     */
    public List orderedLabels();
}
