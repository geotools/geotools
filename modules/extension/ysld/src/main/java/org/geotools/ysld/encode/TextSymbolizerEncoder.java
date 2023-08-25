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

import org.geotools.api.style.LabelPlacement;
import org.geotools.styling.HaloImpl;
import org.geotools.styling.LinePlacementImpl;
import org.geotools.styling.PointPlacementImpl;
import org.geotools.styling.TextSymbolizerImpl;

/** Encodes a {@link TextSymbolizerImpl} as YSLD. */
public class TextSymbolizerEncoder extends SymbolizerEncoder<TextSymbolizerImpl> {

    TextSymbolizerEncoder(TextSymbolizerImpl text) {
        super(text);
    }

    @Override
    protected void encode(TextSymbolizerImpl text) {
        put("label", text.getLabel());
        put("priority", text.getPriority());
        inline(new FillEncoder(text.getFill()));
        inline(new HaloEncoder(text.getHalo()));
        inline(new FontEncoder(text.getFont()));
        inline(new PlacementEncoder(text.getLabelPlacement()));

        inline(new GraphicEncoder(((TextSymbolizerImpl) text).getGraphic(), false));

        super.encode(text);
    }

    static class PlacementEncoder extends YsldEncodeHandler<LabelPlacement> {
        PlacementEncoder(LabelPlacement placement) {
            super(placement);
        }

        @Override
        protected void encode(LabelPlacement placement) {
            // push("placement");
            if (placement instanceof LinePlacementImpl) {
                put("placement", "line");
                put("offset", ((LinePlacementImpl) placement).getPerpendicularOffset());
            } else if (placement instanceof PointPlacementImpl) {
                PointPlacementImpl pp = (PointPlacementImpl) placement;
                put("placement", "point");

                inline(new AnchorPointEncoder(pp.getAnchorPoint()));
                inline(new DisplacementEncoder(pp.getDisplacement()));
                put("rotation", nullIf(pp.getRotation(), 0));
            }
        }
    }

    static class HaloEncoder extends YsldEncodeHandler<HaloImpl> {
        public HaloEncoder(HaloImpl halo) {
            super(halo);
        }

        @Override
        protected void encode(HaloImpl h) {
            push("halo");
            inline(new FillEncoder(h.getFill()));
            put("radius", h.getRadius());
        }
    }
}
