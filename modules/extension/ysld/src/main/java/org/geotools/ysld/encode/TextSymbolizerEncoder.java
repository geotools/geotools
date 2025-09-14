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

import org.geotools.api.style.Halo;
import org.geotools.api.style.LabelPlacement;
import org.geotools.api.style.LinePlacement;
import org.geotools.api.style.PointPlacement;
import org.geotools.api.style.TextSymbolizer;

/** Encodes a {@link TextSymbolizer} as YSLD. */
public class TextSymbolizerEncoder extends SymbolizerEncoder<TextSymbolizer> {

    TextSymbolizerEncoder(TextSymbolizer text) {
        super(text);
    }

    @Override
    protected void encode(TextSymbolizer text) {
        put("label", text.getLabel());
        put("priority", text.getPriority());
        inline(new FillEncoder(text.getFill()));
        inline(new HaloEncoder(text.getHalo()));
        inline(new FontEncoder(text.getFont()));
        inline(new PlacementEncoder(text.getLabelPlacement()));
        if (text instanceof TextSymbolizer) {
            inline(new GraphicEncoder(text.getGraphic(), false));
        }
        super.encode(text);
    }

    static class PlacementEncoder extends YsldEncodeHandler<LabelPlacement> {
        PlacementEncoder(LabelPlacement placement) {
            super(placement);
        }

        @Override
        protected void encode(LabelPlacement placement) {
            // push("placement");
            if (placement instanceof LinePlacement linePlacement) {
                put("placement", "line");
                put("offset", linePlacement.getPerpendicularOffset());
            } else if (placement instanceof PointPlacement pp) {
                put("placement", "point");

                inline(new AnchorPointEncoder(pp.getAnchorPoint()));
                inline(new DisplacementEncoder(pp.getDisplacement()));
                put("rotation", nullIf(pp.getRotation(), 0));
            }
        }
    }

    static class HaloEncoder extends YsldEncodeHandler<Halo> {
        public HaloEncoder(Halo halo) {
            super(halo);
        }

        @Override
        protected void encode(Halo h) {
            push("halo");
            inline(new FillEncoder(h.getFill()));
            put("radius", h.getRadius());
        }
    }
}
