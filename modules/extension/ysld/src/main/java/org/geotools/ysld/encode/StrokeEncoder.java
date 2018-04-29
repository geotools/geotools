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

import org.geotools.styling.Stroke;

/** Encodes a {@link Stroke} as YSLD. */
public class StrokeEncoder extends YsldEncodeHandler<Stroke> {

    StrokeEncoder(Stroke stroke) {
        super(stroke);
    }

    @Override
    protected void encode(Stroke stroke) {
        putColor("stroke-color", stroke.getColor());
        put("stroke-width", stroke.getWidth());
        put("stroke-opacity", nullIf(stroke.getOpacity(), 1d));
        put("stroke-linejoin", nullIf(stroke.getLineJoin(), "miter"));
        putName("stroke-linecap", nullIf(stroke.getLineCap(), "butt"));
        put("stroke-dasharray", toStringOrNull(stroke.getDashArray()));
        put("stroke-dashoffset", nullIf(stroke.getDashOffset(), 0));

        if (stroke.getGraphicFill() != null) {
            push("stroke-graphic-fill").inline(new GraphicEncoder(stroke.getGraphicFill()));
        }

        if (stroke.getGraphicStroke() != null) {
            push("stroke-graphic").inline(new GraphicEncoder(stroke.getGraphicStroke()));
        }
    }

    String toStringOrNull(float[] arr) {
        if (arr == null || arr.length == 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]).append(" ");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
}
