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

import org.geotools.styling.Graphic;

/** Encodes a {@link Graphic} as YSLD. */
public class GraphicEncoder extends YsldEncodeHandler<Graphic> {

    final boolean flatten;

    GraphicEncoder(Graphic g) {
        this(g, true);
    }

    GraphicEncoder(Graphic g, Boolean flatten) {
        super(g);
        this.flatten = flatten;
    }

    @Override
    protected void encode(Graphic g) {
        if (!flatten) push("graphic");

        inline(new AnchorPointEncoder(g.getAnchorPoint()));
        inline(new DisplacementEncoder(g.getDisplacement()));
        put("gap", nullIf(g.getGap(), 0d), nullIf(g.getInitialGap(), 0d));
        put("opacity", nullIf(g.getOpacity(), 1));
        put("size", g.getSize());
        put("rotation", nullIf(g.getRotation(), 0d));
        put("symbols", new SymbolsEncoder(g));
    }
}
