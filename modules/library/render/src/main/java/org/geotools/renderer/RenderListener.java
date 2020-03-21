/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.map.Layer;
import org.opengis.feature.simple.SimpleFeature;

/**
 * A RenderListener is notified each time a feature is rendered and each time an error occurs during
 * rendering. Therefore <b>VERY LITTLE WORK</b> should be done in the listener!!!
 *
 * @author jeichar
 */
public interface RenderListener {

    /**
     * Reports that a specific feature has been rendered. The same feature might be reported
     * multiple times, if
     */
    public void featureRenderer(SimpleFeature feature);

    /**
     * Reports a rendering error. The rendering is not normally stopped on it, a listener that wants
     * to stop it can call {@link GTRenderer#stopRendering()}
     */
    public void errorOccurred(Exception e);

    /** Event issued when the layer begins rendering. */
    default void layerStart(Layer layer) {
        // does nothing
    }

    /** Event issued when the layer completed rendering. May not be issued. */
    default void layerEnd(Layer layer) {
        // does nothing
    }

    /** Event issued when labelling starts. May not be issued if there are no labels to paint. */
    default void labellingStart() {
        // does nothing
    }

    /** Event issued when labelling ends. May not be issued. */
    default void labellingEnd() {
        // does nothing
    }

    /** Event issued when rendering ends. Always issued. */
    default void renderingComplete() {
        // does nothing
    }
}
