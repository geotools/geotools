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

import org.geotools.api.style.Symbolizer;
import org.geotools.styling.GraphicImpl;
import org.geotools.styling.PointSymbolizerImpl;
import org.geotools.styling.RuleImpl;
import org.geotools.ysld.YamlObject;

/** Handles parsing a Ysld "point" symbolizer property into a {@link Symbolizer} object. */
public class PointParser extends SymbolizerParser<PointSymbolizerImpl> {

    public PointParser(RuleImpl rule, Factory factory) {
        super(rule, factory.style.createPointSymbolizer(), factory);
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        super.handle(obj, context);
        context.push(new GraphicParser(factory, (GraphicImpl) sym.getGraphic()));
    }
}
