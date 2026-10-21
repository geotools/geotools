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
package org.geotools.mbstyle.expression;

import java.util.ArrayList;
import java.util.List;
import org.geotools.api.filter.expression.Expression;
import org.geotools.mbstyle.parse.MBFormatException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Mapbox "format" expression: builds display text from alternating (input, options) sections.
 *
 * <pre>{@code ["format", input_1, options_1, ..., input_n, options_n]}</pre>
 *
 * <p>Only the text content is honored: the section option objects (per-section "text-font", "font-scale", "text-color")
 * are dropped and the inputs are concatenated into a single string, so the label renders in one font. Full fidelity
 * needs per-run fonts, which SLD text symbolizers cannot express today.
 */
public class MBFormat extends MBExpression {

    public MBFormat(JSONArray json) {
        super(json);
    }

    /** Returns the concatenation of the section inputs, ignoring the section options. */
    public Expression format() {
        List<Expression> parts = new ArrayList<>();
        for (int i = 1; i < json.size(); i++) {
            // section options: single-font only, dropped
            if (json.get(i) instanceof JSONObject) {
                continue;
            }
            // literals stay as written: a "\n" separator must not collapse, and {token} is not resolved
            parts.add(parse.string(json, i));
        }
        if (parts.size() == 1) {
            return parts.get(0);
        }
        return ff.function("Concatenate", parts.toArray(new Expression[0]));
    }

    @Override
    public Expression getExpression() throws MBFormatException {
        if ("format".equals(name)) {
            return format();
        }
        throw new MBFormatException(name + " is an unsupported format expression");
    }
}
