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
package org.geotools.ysld.encode;

import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;

/**
 * Encodes an iterator of {@link Symbolizer} as YSLD, delegating to {@link PointSymblolizerEncoder},
 * {@link PolygonSymbolizerEncoder}, {@link LineSymbolizerEncoder}, {@link TextSymbolizerEncoder},
 * or {@link RasterSymbolizerEncoder} as appropriate.
 */
public class SymbolizersEncoder extends YsldEncodeHandler<Symbolizer> {

    public SymbolizersEncoder(Rule rule) {
        super(rule.symbolizers().iterator());
    }

    @Override
    protected void encode(Symbolizer sym) {
        if (sym instanceof PointSymbolizer) {
            push("point").inline(new PointSymblolizerEncoder((PointSymbolizer) sym));
        } else if (sym instanceof LineSymbolizer) {
            push("line").inline(new LineSymbolizerEncoder((LineSymbolizer) sym));
        } else if (sym instanceof PolygonSymbolizer) {
            push("polygon").inline(new PolygonSymbolizerEncoder((PolygonSymbolizer) sym));
        } else if (sym instanceof TextSymbolizer) {
            push("text").inline(new TextSymbolizerEncoder((TextSymbolizer) sym));
        } else if (sym instanceof RasterSymbolizer) {
            push("raster").inline(new RasterSymbolizerEncoder((RasterSymbolizer) sym));
        }
    }

    SymbolizersEncoder encode(LineSymbolizer sym) {
        return this;
    }

    SymbolizersEncoder encode(PolygonSymbolizer sym) {
        return this;
    }

    SymbolizersEncoder encode(RasterSymbolizer sym) {
        return this;
    }
}
