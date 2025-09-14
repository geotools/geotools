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

import org.geotools.api.style.LineSymbolizer;
import org.geotools.api.style.PointSymbolizer;
import org.geotools.api.style.PolygonSymbolizer;
import org.geotools.api.style.RasterSymbolizer;
import org.geotools.api.style.Rule;
import org.geotools.api.style.Symbolizer;
import org.geotools.api.style.TextSymbolizer;

/**
 * Encodes an iterator of {@link Symbolizer} as YSLD, delegating to {@link PointSymblolizerEncoder},
 * {@link PolygonSymbolizerEncoder}, {@link LineSymbolizerEncoder}, {@link TextSymbolizerEncoder}, or
 * {@link RasterSymbolizerEncoder} as appropriate.
 */
public class SymbolizersEncoder extends YsldEncodeHandler<Symbolizer> {

    public SymbolizersEncoder(Rule rule) {
        super(rule.symbolizers().iterator());
    }

    @Override
    protected void encode(Symbolizer sym) {
        if (sym instanceof PointSymbolizer symbolizer4) {
            push("point").inline(new PointSymblolizerEncoder(symbolizer4));
        } else if (sym instanceof LineSymbolizer symbolizer3) {
            push("line").inline(new LineSymbolizerEncoder(symbolizer3));
        } else if (sym instanceof PolygonSymbolizer symbolizer2) {
            push("polygon").inline(new PolygonSymbolizerEncoder(symbolizer2));
        } else if (sym instanceof TextSymbolizer symbolizer1) {
            push("text").inline(new TextSymbolizerEncoder(symbolizer1));
        } else if (sym instanceof RasterSymbolizer symbolizer) {
            push("raster").inline(new RasterSymbolizerEncoder(symbolizer));
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
