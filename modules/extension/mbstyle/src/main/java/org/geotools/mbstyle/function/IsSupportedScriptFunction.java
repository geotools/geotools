/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.function;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.awt.Font;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.filter.FunctionImpl;
import org.geotools.filter.capability.FunctionNameImpl;

/** Implements the Mapbox "is-supported-script" expression: true when the server fonts can display every character. */
public class IsSupportedScriptFunction extends FunctionImpl {

    public static final FunctionName NAME = new FunctionNameImpl(
            "isSupportedScript", parameter("supported", Boolean.class), parameter("text", String.class));

    // Java2D shapes complex and right-to-left scripts, so only glyph coverage limits a label, unlike in Mapbox GL;
    // the logical font chains all the fonts installed in the operating system, and it is immutable
    private static final Font FALLBACK_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);

    public IsSupportedScriptFunction() {
        this.functionName = NAME;
    }

    @Override
    public Object evaluate(Object object) {
        if (getParameters().isEmpty()) {
            throw new IllegalArgumentException("isSupportedScript requires exactly 1 argument");
        }
        String text = getParameters().get(0).evaluate(object, String.class);
        if (text == null || text.isEmpty()) {
            return Boolean.TRUE;
        }
        return FALLBACK_FONT.canDisplayUpTo(text) == -1;
    }
}
