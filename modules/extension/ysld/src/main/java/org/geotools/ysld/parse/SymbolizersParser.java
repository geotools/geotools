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

import org.geotools.styling.Rule;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.geotools.ysld.YamlSeq;

/**
 * Handles parsing a Ysld "symbolizers" property into {@link Symbolizer} objects, delegating to
 * {@link SymbolizerParser} and its subclasses.
 */
public class SymbolizersParser extends YsldParseHandler {

    Rule rule;

    public SymbolizersParser(Rule rule, Factory factory) {
        super(factory);
        this.rule = rule;
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        YamlSeq seq = obj instanceof YamlMap ? YamlSeq.from(obj) : obj.seq();

        for (YamlObject o : seq) {
            YamlMap sym = o.map();

            if (sym.has("point")) {
                context.push(sym, "point", new PointParser(rule, factory));
            } else if (sym.has("line")) {
                context.push(sym, "line", new LineParser(rule, factory));
            } else if (sym.has("polygon")) {
                context.push(sym, "polygon", new PolygonParser(rule, factory));
            } else if (sym.has("text")) {
                context.push(sym, "text", new TextParser(rule, factory));
            } else if (sym.has("raster")) {
                context.push(sym, "raster", new RasterParser(rule, factory));
            }
        }
    }
}
