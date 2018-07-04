/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.wfs.internal.v2_0.storedquery;

import java.util.Collections;
import java.util.Map;
import org.geotools.data.wfs.internal.FeatureTypeInfo;
import org.geotools.filter.visitor.ExtractBoundsFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Envelope;
import org.opengis.filter.Filter;

/**
 * The property expression evaluation context for stored query parameter mappings. To see how these
 * are mapped as properties in the expression, @see {@link ParameterCQLExpressionFilterFactoryImpl}.
 *
 * @author Sampo Savolainen (Spatineo)
 */
public class ParameterMappingContext {
    // Set in constructor
    private final Filter filter;
    private final Map<String, String> viewParams;
    private final FeatureTypeInfo featureTypeInfo;

    // Cached
    private Envelope bbox;

    public ParameterMappingContext(
            Filter filter, Map<String, String> viewParams, FeatureTypeInfo featureTypeInfo) {
        this.filter = filter;
        if (viewParams == null) {
            viewParams = Collections.emptyMap();
        }
        this.viewParams = viewParams;
        this.featureTypeInfo = featureTypeInfo;
    }

    public Filter getFilter() {
        return filter;
    }

    public Map<String, String> getViewParams() {
        return viewParams;
    }

    public FeatureTypeInfo getFeatureTypeInfo() {
        return featureTypeInfo;
    }

    public Envelope getBBOX() {
        if (bbox != null) return bbox;

        bbox = new ReferencedEnvelope();
        bbox = (Envelope) filter.accept(ExtractBoundsFilterVisitor.BOUNDS_VISITOR, bbox);

        return bbox;
    }
}
