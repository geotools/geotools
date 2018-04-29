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

import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Style;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.geotools.ysld.YamlSeq;

/** Handles parsing a Ysld "feature-styles" property into a {@link FeatureTypeStyle} object. */
public class FeatureStyleParser extends YsldParseHandler {

    Style style;

    FeatureStyleParser(Style style, Factory factory) {
        super(factory);
        this.style = style;
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        YamlSeq seq = obj.seq();
        for (YamlObject<?> o : seq) {
            YamlMap fs = o.map();

            FeatureTypeStyle featureStyle = factory.style.createFeatureTypeStyle();
            style.featureTypeStyles().add(featureStyle);

            featureStyle.setName(fs.str("name"));
            if (fs.has("title")) {
                featureStyle.getDescription().setTitle(fs.str("title"));
            }
            if (fs.has("abstract")) {
                featureStyle.getDescription().setAbstract(fs.str("abstract"));
            }

            featureStyle.getOptions().putAll(Util.vendorOptions(fs));

            context.push(fs, "transform", new TransformHandler(featureStyle, factory));
            context.push(fs, "rules", new RuleParser(featureStyle, factory));
        }
    }
}
