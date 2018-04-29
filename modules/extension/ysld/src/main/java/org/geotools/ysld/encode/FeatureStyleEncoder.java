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

import java.util.logging.Logger;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Style;
import org.geotools.util.logging.Logging;

/**
 * Encodes a {@link FeatureTypeStyle} as YSLD. Delegates to {@link TransformEncoder} and {@link
 * RuleEncoder}.
 */
public class FeatureStyleEncoder extends YsldEncodeHandler<FeatureTypeStyle> {

    static Logger LOG = Logging.getLogger(FeatureStyleEncoder.class);

    public FeatureStyleEncoder(Style style) {
        super(style.featureTypeStyles().iterator());
    }

    @Override
    protected void encode(FeatureTypeStyle featureStyle) {
        put("name", featureStyle.getName());
        put("title", featureStyle.getTitle());
        put("abstract", featureStyle.getAbstract());
        if (featureStyle.getTransformation() != null) {
            push("transform").inline(new TransformEncoder(featureStyle.getTransformation()));
            pop();
        }
        put("rules", new RuleEncoder(featureStyle));
        vendorOptions(featureStyle.getOptions());
    }
}
