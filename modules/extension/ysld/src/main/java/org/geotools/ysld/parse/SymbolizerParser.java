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
import org.geotools.styling.Symbolizer;
import org.geotools.ysld.UomMapper;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;

/**
 * Base class for handling the parsing of Ysld "symbolizer" properties into {@link Symbolizer}
 * objects.
 *
 * @param <T> The type of {@link Symbolizer} being parsed.
 */
public class SymbolizerParser<T extends Symbolizer> extends YsldParseHandler {

    protected T sym;

    protected SymbolizerParser(Rule rule, T sym, Factory factory) {
        super(factory);
        rule.symbolizers().add(this.sym = sym);
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        YamlMap map = obj.map();
        sym.setName(map.str("name"));
        if (map.has("geometry")) {
            sym.setGeometry(Util.expression(map.str("geometry"), factory));
        }
        UomMapper uomMapper = (UomMapper) context.getDocHint(UomMapper.KEY);
        if (map.has("uom")) {
            sym.setUnitOfMeasure(uomMapper.getUnit(map.str("uom")));
        }
        sym.getOptions().putAll(Util.vendorOptions(map));
    }
}
