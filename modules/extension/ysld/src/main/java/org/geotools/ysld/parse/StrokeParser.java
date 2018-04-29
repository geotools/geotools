/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld.parse;

import org.geotools.styling.Graphic;
import org.geotools.styling.Stroke;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;

/**
 * Handles parsing Ysld "stroke-*" (e.g., "stroke-color", "stroke-width", ... ) properties into a
 * {@link Stroke} object.
 */
public abstract class StrokeParser extends YsldParseHandler {
    Stroke stroke;

    protected StrokeParser(Factory factory) {
        super(factory);
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        YamlMap map = obj.map();

        if (map.has("stroke-color")) {
            stroke().setColor(Util.color(map.get("stroke-color"), factory));
        }
        if (map.has("stroke-width")) {
            stroke().setWidth(Util.expression(map.str("stroke-width"), factory));
        }
        if (map.has("stroke-opacity")) {
            stroke().setOpacity(Util.expression(map.str("stroke-opacity"), factory));
        }
        if (map.has("stroke-linejoin")) {
            stroke().setLineJoin(Util.expression(map.str("stroke-linejoin"), factory));
        }
        if (map.has("stroke-linecap")) {
            stroke().setLineCap(Util.expression(map.str("stroke-linecap"), factory));
        }
        if (map.has("stroke-dasharray")) {
            stroke().setDashArray(Util.floatArray(map.str("stroke-dasharray")));
        }
        if (map.has("stroke-dashoffset")) {
            stroke().setDashOffset(Util.expression(map.str("stroke-dashoffset"), factory));
        }

        context.push(
                "stroke-graphic-fill",
                new GraphicParser(factory) {
                    @Override
                    protected void graphic(Graphic g) {
                        stroke().setGraphicFill(g);
                    }
                });
        context.push(
                "stroke-graphic",
                new GraphicParser(factory) {
                    @Override
                    protected void graphic(Graphic g) {
                        stroke().setGraphicStroke(g);
                    }
                });
    }

    Stroke stroke() {
        if (stroke == null) {
            stroke = factory.style.createStroke(null, null);
            stroke(stroke);
        }
        return stroke;
    }

    protected abstract void stroke(Stroke stroke);
}
