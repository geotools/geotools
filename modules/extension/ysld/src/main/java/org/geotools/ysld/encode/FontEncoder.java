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

import org.geotools.styling.Font;

/** Encodes an {@link Font} as YSLD. */
public class FontEncoder extends YsldEncodeHandler<Font> {
    FontEncoder(Font font) {
        super(font);
    }

    @Override
    protected void encode(Font font) {
        putName("font-family", font.getFontFamily());
        put("font-size", font.getSize());
        putName("font-style", font.getStyle());
        putName("font-weight", font.getWeight());
    }
}
