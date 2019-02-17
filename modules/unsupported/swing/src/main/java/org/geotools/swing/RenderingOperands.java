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
package org.geotools.swing;

import java.awt.Graphics2D;
import org.geotools.map.Layer;
import org.geotools.renderer.GTRenderer;

/** @author michael */
public class RenderingOperands {

    private final Layer layer;
    private final Graphics2D graphics;
    private final GTRenderer renderer;

    public RenderingOperands(Layer layer, Graphics2D graphics) {
        this(layer, graphics, null);
    }

    public RenderingOperands(Layer layer, Graphics2D graphics, GTRenderer renderer) {
        this.layer = layer;
        this.graphics = graphics;
        this.renderer = renderer;
    }

    public Layer getLayer() {
        return layer;
    }

    public Graphics2D getGraphics() {
        return graphics;
    }

    public GTRenderer getRenderer() {
        return renderer;
    }
}
