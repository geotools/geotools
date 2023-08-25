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

import org.geotools.styling.FillImpl;

/** Encodes a {@link FillImpl} as YSLD. */
public class FillEncoder extends YsldEncodeHandler<FillImpl> {

    public FillEncoder(FillImpl fill) {
        super(fill);
    }

    @Override
    protected void encode(FillImpl fill) {
        putColor("fill-color", fill.getColor());
        put("fill-opacity", nullIf(fill.getOpacity(), 1d));
        if (fill.getGraphicFill() != null) {
            push("fill-graphic").inline(new GraphicEncoder(fill.getGraphicFill()));
        }
    }
}
