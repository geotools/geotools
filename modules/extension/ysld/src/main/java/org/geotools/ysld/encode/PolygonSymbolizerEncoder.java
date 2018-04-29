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

import org.geotools.styling.PolygonSymbolizer;

/** Encodes a {@link PolygonSymbolizer} as YSLD. */
public class PolygonSymbolizerEncoder extends SymbolizerEncoder<PolygonSymbolizer> {
    public PolygonSymbolizerEncoder(PolygonSymbolizer sym) {
        super(sym);
    }

    @Override
    protected void encode(PolygonSymbolizer sym) {
        inline(new StrokeEncoder(sym.getStroke()));
        inline(new FillEncoder(sym.getFill()));
        put("offset", sym.getPerpendicularOffset());
        inline(new DisplacementEncoder(sym.getDisplacement()));

        super.encode(sym);
    }
}
