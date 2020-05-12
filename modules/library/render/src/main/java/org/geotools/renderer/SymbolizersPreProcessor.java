/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer;

import java.util.List;
import org.geotools.map.Layer;
import org.geotools.styling.Style;
import org.geotools.styling.Symbolizer;
import org.opengis.feature.Feature;

/**
 * Extension point interface for handling and transforming the Symbolizer list before sending it to
 * render phase. <br>
 * If no changes are done on the symbolizers list, the implementation should return null or bypass
 * the provided symbolizers list.
 */
public interface SymbolizersPreProcessor {

    /**
     * Declares if this pre-processor is applied to the layer involved.
     *
     * @param layer the layer to be rendered
     * @return true if this pre-processor can be applied to the layer
     */
    boolean appliesTo(Layer layer);

    /** Declares which attributes it might need in order to do its job. */
    List<String> getAttributes(Layer layer);

    /** Declares how much of an extra space is needed to catch the symbolizers it is about to add */
    double getBuffer(Layer layer, Style style);

    /**
     * Performs symbolizers list enhancements like adding, editing or removing symbolizers and
     * return the resulting list.
     *
     * @param feature the feature to be rendered
     * @param layer the layer owning the feature
     * @param symbolizers the symbolizers list to be processed before rendering
     * @return the enhanced {@link Symbolizer} list or null if no changes are applied. It can return
     *     the same symbolizers parameter without changes.
     */
    List<Symbolizer> apply(Feature feature, Layer layer, List<Symbolizer> symbolizers);
}
