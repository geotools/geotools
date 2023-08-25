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

import org.geotools.styling.AnchorPointImpl;
import org.geotools.styling.GraphicImpl;

/** Encodes a {@link GraphicImpl} as YSLD. */
public class GraphicEncoder extends YsldEncodeHandler<GraphicImpl> {

    final boolean flatten;

    GraphicEncoder(GraphicImpl g) {
        this(g, true);
    }

    GraphicEncoder(GraphicImpl g, Boolean flatten) {
        super(g);
        this.flatten = flatten;
    }

    @Override
    protected void encode(GraphicImpl g) {
        if (!flatten) push("graphic");

        inline(new AnchorPointEncoder((AnchorPointImpl) g.getAnchorPoint()));
        inline(new DisplacementEncoder(g.getDisplacement()));
        put("gap", nullIf(g.getGap(), 0d), nullIf(g.getInitialGap(), 0d));
        put("opacity", nullIf(g.getOpacity(), 1));
        put("size", g.getSize());
        put("rotation", nullIf(g.getRotation(), 0d));
        put("symbols", new SymbolsEncoder(g));
    }
}
