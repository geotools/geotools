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

import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Symbolizer;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;

/** Handles parsing a Ysld "line" symbolizer property into a {@link Symbolizer} object. */
public class LineParser extends SymbolizerParser<LineSymbolizer> {

    protected LineParser(Rule rule, Factory factory) {
        super(rule, factory.style.createLineSymbolizer(), factory);
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        super.handle(obj, context);

        YamlMap map = obj.map();
        context.push(
                new StrokeParser(factory) {
                    @Override
                    protected void stroke(Stroke stroke) {
                        sym.setStroke(stroke);
                    }
                });
        if (map.has("offset")) {
            sym.setPerpendicularOffset(Util.expression(map.str("offset"), factory));
        }
    }
}
