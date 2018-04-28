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

import java.util.Collections;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayerDescriptor;

/**
 * Encodes a {@link StyledLayerDescriptor} as YSLD. Handles top-level elements such a name and
 * title, and delegates to {@link FeatureStyleEncoder} for the content.
 */
public class RootEncoder extends YsldEncodeHandler<StyledLayerDescriptor> {

    RootEncoder(StyledLayerDescriptor sld) {
        super(Collections.singleton(sld).iterator());
    }

    @Override
    protected void encode(StyledLayerDescriptor sld) {
        Style style = SLD.defaultStyle(sld);
        if (style != null) {
            put("name", style.getName());
            put("title", style.getTitle());
            put("abstract", style.getAbstract());
            put("feature-styles", new FeatureStyleEncoder(style));
        }
    }
}
