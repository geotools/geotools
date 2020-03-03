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
package org.geotools.ysld.parse;

import javax.annotation.Nullable;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Graphic;
import org.geotools.styling.Rule;
import org.geotools.ysld.Tuple;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.geotools.ysld.YamlSeq;

/**
 * Handles parsing a Ysld "rules" property into {@link Rule} objects for a {@link FeatureTypeStyle}.
 */
public class RuleParser extends YsldParseHandler {

    FeatureTypeStyle featureStyle;

    public RuleParser(FeatureTypeStyle featureStyle, Factory factory) {
        super(factory);
        this.featureStyle = featureStyle;
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        YamlSeq seq = obj.seq();
        for (YamlObject o : seq) {
            YamlMap r = o.map();

            Rule rule = factory.style.createRule();
            featureStyle.rules().add(rule);

            rule.setName(r.str("name"));
            if (r.has("title")) {
                rule.getDescription().setTitle(r.str("title"));
            }
            if (r.has("abstract")) {
                rule.getDescription().setAbstract(r.str("abstract"));
            }
            rule.getDescription().setTitle(r.str("title"));
            rule.getDescription().setAbstract(r.str("abstract"));
            context.push(
                    r,
                    "legend",
                    new GraphicParser(factory) {
                        @Override
                        protected void graphic(Graphic g) {
                            rule.setLegend(g);
                        }
                    });

            if (r.has("filter")) {
                try {
                    rule.setFilter(ECQL.toFilter(Util.removeExpressionBrackets(r.str("filter"))));
                } catch (CQLException e) {
                    throw new RuntimeException("Error parsing filter", e);
                }
            }

            rule.setElseFilter(r.boolOr("else", false));

            // Prefer scale over zoom
            @Nullable ScaleRange range = Util.defaultForNull(parseScale(r), parseZoom(r, context));
            if (range != null) {
                range.applyTo(rule);
            }

            context.push(r, "symbolizers", new SymbolizersParser(rule, factory));
        }
    }

    /** @return The parsed scale, or null if the {@link YamlMap} has no "scale" key. */
    @Nullable
    private ScaleRange parseScale(YamlMap r) {
        if (r.has("scale")) {
            Object value = r.get("scale");
            Tuple t = null;
            try {
                t = Tuple.of(2).parse(value);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                        String.format(
                                "Bad scale value: '%s', must be of form [<min>,<max>]", value),
                        e);
            }
            double min = 0;
            double max = Double.POSITIVE_INFINITY;
            if (t.at(0) != null) {
                if (!t.strAt(0).equalsIgnoreCase("min")) {
                    min = Double.parseDouble(t.strAt(0));
                }
            }
            if (t.at(1) != null) {
                if (!t.strAt(1).equalsIgnoreCase("max")) {
                    max = Double.parseDouble(t.strAt(1));
                }
            }
            return new ScaleRange(min, max);
        } else {
            return null;
        }
    }

    private ScaleRange parseZoom(YamlMap r, YamlParseContext context) {
        if (r.has("zoom")) {
            ZoomContext zCtxt = getZoomContext(context);
            Object value = r.get("zoom");
            Tuple t = null;
            try {
                t = Tuple.of(2).parse(value);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                        String.format("Bad zoom value: '%s', must be of form [<min>,<max>]", value),
                        e);
            }
            @Nullable Integer min = null;
            @Nullable Integer max = null;
            if (t.at(0) != null) {
                if (!t.strAt(0).equalsIgnoreCase("min")) {
                    min = Integer.parseInt(t.strAt(0));
                }
            }
            if (t.at(1) != null) {
                if (!t.strAt(1).equalsIgnoreCase("max")) {
                    max = Integer.parseInt(t.strAt(1));
                }
            }
            return zCtxt.getRange(min, max);
        } else {
            return null;
        }
    }

    protected ZoomContext getZoomContext(YamlParseContext context) {
        return Util.forceDefaultForNull(
                (ZoomContext) context.getDocHint(ZoomContext.HINT_ID),
                WellKnownZoomContextFinder.getInstance().get("DEFAULT"));
    }
}
