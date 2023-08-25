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

import org.geotools.api.style.Symbolizer;
import org.geotools.styling.*;
import org.geotools.styling.LineSymbolizerImpl;

/**
 * Encodes an iterator of {@link Symbolizer} as YSLD, delegating to {@link PointSymblolizerEncoder},
 * {@link PolygonSymbolizerEncoder}, {@link LineSymbolizerEncoder}, {@link TextSymbolizerEncoder},
 * or {@link RasterSymbolizerEncoder} as appropriate.
 */
public class SymbolizersEncoder extends YsldEncodeHandler<Symbolizer> {

    public SymbolizersEncoder(RuleImpl rule) {
        super(rule.symbolizers().iterator());
    }

    @Override
    protected void encode(Symbolizer sym) {
        if (sym instanceof PointSymbolizerImpl) {
            push("point").inline(new PointSymblolizerEncoder((PointSymbolizerImpl) sym));
        } else if (sym instanceof LineSymbolizerImpl) {
            push("line").inline(new LineSymbolizerEncoder((LineSymbolizerImpl) sym));
        } else if (sym instanceof PolygonSymbolizerImpl) {
            push("polygon").inline(new PolygonSymbolizerEncoder((PolygonSymbolizerImpl) sym));
        } else if (sym instanceof TextSymbolizerImpl) {
            push("text").inline(new TextSymbolizerEncoder((TextSymbolizerImpl) sym));
        } else if (sym instanceof RasterSymbolizerImpl) {
            push("raster").inline(new RasterSymbolizerEncoder((RasterSymbolizerImpl) sym));
        }
    }

    SymbolizersEncoder encode(LineSymbolizerImpl sym) {
        return this;
    }

    SymbolizersEncoder encode(PolygonSymbolizerImpl sym) {
        return this;
    }

    SymbolizersEncoder encode(RasterSymbolizerImpl sym) {
        return this;
    }
}
