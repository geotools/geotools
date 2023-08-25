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

import org.geotools.api.metadata.citation.OnLineResource;
import org.geotools.api.style.GraphicalSymbol;
import org.geotools.styling.ExternalGraphicImpl;
import org.geotools.styling.GraphicImpl;
import org.geotools.styling.MarkImpl;

/** Encodes a {@link GraphicalSymbol} as YSLD. */
public class SymbolsEncoder extends YsldEncodeHandler<GraphicalSymbol> {

    public SymbolsEncoder(GraphicImpl g) {
        super(g.graphicalSymbols().iterator());
    }

    @Override
    protected void encode(GraphicalSymbol symbol) {
        if (symbol instanceof MarkImpl) {
            push("mark");
            encode((MarkImpl) symbol);
        } else if (symbol instanceof ExternalGraphicImpl) {
            push("external");
            encode((ExternalGraphicImpl) symbol);
        }
    }

    SymbolsEncoder encode(MarkImpl mark) {
        putName("shape", mark.getWellKnownName());
        inline(new StrokeEncoder(mark.getStroke()));
        inline(new FillEncoder(mark.getFill()));
        // encode("stroke", new StrokeEncoder(mark.getStroke()));
        // encode("fill", mark.getFill());
        // url:
        // inline:
        return this;
    }

    SymbolsEncoder encode(ExternalGraphicImpl eg) {
        OnLineResource r = eg.getOnlineResource();
        if (r != null) {
            put("url", r.getLinkage().toString());
        }

        put("format", eg.getFormat());
        return this;
    }
}
