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

import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;

/**
 * Handles parsing Ysld "fill-*" properties ("fill-color", "fill-opacity", "fill-graphic") into a
 * {@link Fill} object.
 */
public abstract class FillParser extends YsldParseHandler {
    Fill fill;

    protected FillParser(Factory factory) {
        super(factory);
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        YamlMap map = obj.map();
        if (map.has("fill-color")) {
            fill().setColor(Util.color(map.get("fill-color"), factory));
        }
        if (map.has("fill-opacity")) {
            fill().setOpacity(Util.expression(map.str("fill-opacity"), factory));
        }
        context.push(
                "fill-graphic",
                new GraphicParser(factory) {
                    @Override
                    protected void graphic(Graphic g) {
                        fill().setGraphicFill(g);
                    }
                });
    }

    Fill fill() {
        if (fill == null) {
            fill(fill = factory.style.createFill(null));
        }
        return fill;
    }

    protected abstract void fill(Fill fill);
}
