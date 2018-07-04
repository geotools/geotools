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

import org.geotools.filter.text.ecql.ECQL;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.ysld.Tuple;
import org.opengis.filter.Filter;

/** Encodes a {@link Rule} as YSLD. Delegates to {@link SymbolizersEncoder}. */
public class RuleEncoder extends YsldEncodeHandler<Rule> {

    public RuleEncoder(FeatureTypeStyle featureStyle) {
        super(featureStyle.rules().iterator());
    }

    @Override
    protected void encode(Rule rule) {
        put("name", rule.getName());
        put("title", rule.getTitle());
        put("abstract", rule.getAbstract());
        if (rule.getFilter() != null && rule.getFilter() != Filter.INCLUDE) {
            put(
                    "filter",
                    String.format("${%s}", escapeForEmbededCQL(ECQL.toCQL(rule.getFilter()))));
        }
        if (rule.isElseFilter()) {
            put("else", true);
        }

        Tuple t =
                Tuple.of(
                        toStringOrNull(rule.getMinScaleDenominator(), "min"),
                        toStringOrNull(rule.getMaxScaleDenominator(), "max"));
        if (!t.isNull()) {
            put("scale", t);
        }

        // legend:?
        put("symbolizers", new SymbolizersEncoder(rule));
    }

    String toStringOrNull(double d, String nullKeyword) {
        return d > 0 && !Double.isNaN(d) && !Double.isInfinite(d) ? String.valueOf(d) : nullKeyword;
    }
}
