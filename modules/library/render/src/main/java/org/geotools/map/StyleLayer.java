/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.map.event.MapLayerEvent;
import org.geotools.styling.Style;

/**
 * Layer responsible for rendering under control of a user supplied Style object.
 * <p>
 * The StyleLayer is expected to be subclassed; and is responsible for:
 * <ul>
 * <li>style: Style</li>
 * </ul>
 * Please note that a StyleLayerDescriptor (defined by SLD) document is usually used to describe the
 * rendering requirements for an entire Map; while a Style (defined by SE) is focused on a single
 * layer of content
 * @since 8.0
 * @version 8.0
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/library/render/src/main/java/org/geotools/map/StyleLayer.java $
 */
public abstract class StyleLayer extends Layer {
    /** Style used for rendering */
    protected Style style;

    /**
     * Creates a new instance of StyleLayer
     * 
     * @param style
     *            the style used to control drawing of this layer
     */
    public StyleLayer(Style style) {
        this.style = style;
    }

    public StyleLayer(Style style, String title) {
        this.style = style;
        setTitle(title);
    }

    @Override
    public void dispose() {
        // We assume that preDispose has been called by 
        // the sub-class
        
        style = null;
        super.dispose();
    }

    /**
     * Get the style for this layer.
     * <p>
     * If style has not been set, then null is returned.
     * 
     * @return The style (SLD).
     */
    public Style getStyle() {
        return style;
    }

    /**
     * Sets the style for this layer.
     * 
     * @param style
     *            The new style
     */
    public void setStyle(Style style) {
        if (style == null) {
            throw new NullPointerException("Style is required");
        }
        this.style = style;
        fireMapLayerListenerLayerChanged(MapLayerEvent.STYLE_CHANGED);
    }

}
