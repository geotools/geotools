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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.NamedLayer;
import org.geotools.api.style.RemoteOWS;
import org.geotools.api.style.Rule;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyledLayerDescriptor;
import org.geotools.api.style.UserLayer;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;

/**
 * {@link YsldParseHandler} for the root of a {@link YamlObject}. This is the "entrypoint" {@link
 * YsldParseHandler} for parsing Ysld into GeoTools-style objects. The resulting sld is accessible
 * via the {{@link #sld()} method.
 */
public class RootParser extends YsldParseHandler {

    StyledLayerDescriptor sld;

    Style style;

    public RootParser() {
        this(Collections.emptyList());
    }

    public RootParser(List<ZoomContextFinder> zCtxtFinders) {
        super(new Factory());
        this.zCtxtFinders = new ArrayList<>(zCtxtFinders.size() + 1);
        this.zCtxtFinders.addAll(zCtxtFinders);
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        sld = factory.style.createStyledLayerDescriptor();

        YamlMap root = obj.map();

        if (root.has("grid")) {
            context.setDocHint(ZoomContext.HINT_ID, getZoomContext(root.map("grid")));
        }

        // sld prefix for sld properties
        if (root.has("sld-name")) {
            sld.setName(root.str("sld-name"));
        }
        if (root.has("sld-title")) {
            sld.setTitle(root.str("sld-title"));
        }
        if (root.has("sld-abstract")) {
            sld.setAbstract(root.str("sld-abstract"));
        }

        style = factory.style.createStyle();

        if (root.has("user-name") || root.has("user-remote") || root.has("user-service")) {
            // user prefix for user layer properties
            UserLayer layer = factory.style.createUserLayer();
            sld.layers().add(layer);
            layer.userStyles().add(style);

            if (root.has("user-name")) {
                layer.setName(root.str("user-name"));
            }
            if (root.has("user-remote")) {
                RemoteOWS remote =
                        factory.style.createRemoteOWS(
                                root.strOr("user-service", "WMS"), root.str("user-remote"));
                layer.setRemoteOWS(remote);
            }
        } else {
            // assume named layer
            NamedLayer layer = factory.style.createNamedLayer();
            sld.layers().add(layer);
            layer.styles().add(style);

            // layer prefix for named layer properties
            if (root.has("layer-name")) {
                layer.setName(root.str("layer-name"));
            }
        }

        // no prefix for style properties
        style.setName(root.str("name"));
        if (root.has("title")) {
            style.getDescription().setTitle(root.str("title"));
        }
        if (root.has("abstract")) {
            style.getDescription().setAbstract(root.str("abstract"));
        }

        if (root.has("feature-styles")) {
            context.push("feature-styles", new FeatureStyleParser(style, factory));
        } else if (root.has("rules")) {
            context.push("rules", new RuleParser(newFeatureTypeStyle(), factory));
        } else if (root.has("symbolizers")) {
            context.push("symbolizers", new SymbolizersParser(newRule(), factory));
        } else if (root.has("point")
                || root.has("line")
                || root.has("polygon")
                || root.has("text")
                || root.has("raster")) {
            context.push(new SymbolizersParser(newRule(), factory));
        }
    }

    final List<ZoomContextFinder> zCtxtFinders;

    @SuppressWarnings("unchecked")
    protected ZoomContext getZoomContext(YamlMap map) {
        ZoomContext result = null;
        if (map.has("name")) {
            result = Util.getNamedZoomContext(map.str("name"), zCtxtFinders);
        }

        if (result == null && map.has("scales")) {
            final List<?> raw = map.seq("scales").raw();
            final List<Double> scaleDenoms = new ArrayList<>(raw.size());
            for (Number s : (List<Number>) raw) {
                scaleDenoms.add(s.doubleValue());
            }
            final int initialLevel = map.intOr("initial-level", 0);

            result = new ListZoomContext(scaleDenoms, initialLevel);
        }

        if (result == null && map.has("initial-scale")) {
            final double initialScale = map.doub("initial-scale");
            final double ratio = map.doubOr("ratio", 2d);
            final int initialLevel = map.intOr("initial-level", 0);

            result = new RatioZoomContext(initialLevel, initialScale, ratio);
        }

        if (result == null) {
            throw new IllegalArgumentException();
        }

        return result;
    }

    public FeatureTypeStyle newFeatureTypeStyle() {
        FeatureTypeStyle fts = factory.style.createFeatureTypeStyle();
        style.featureTypeStyles().add(fts);
        return fts;
    }

    public Rule newRule() {
        Rule rule = factory.style.createRule();
        newFeatureTypeStyle().rules().add(rule);
        return rule;
    }

    public StyledLayerDescriptor sld() {
        return sld;
    }
}
