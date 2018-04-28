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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.opengis.wfs20.ParameterExpressionType;
import net.opengis.wfs20.ParameterType;
import net.opengis.wfs20.StoredQueryDescriptionType;
import net.opengis.wfs20.Wfs20Factory;
import org.geotools.data.wfs.internal.FeatureTypeInfo;
import org.opengis.filter.Filter;

/**
 * Builds parameters to pass to the stored query. It selects a value for each parameter in the
 * stored query. The value is primarily the one passed in as a view parameter and secondarily the
 * value of the mapping configured for the feature type.
 *
 * @author Sampo Savolainen (Spatineo)
 */
public class ParameterTypeFactory {
    private final StoredQueryConfiguration config;
    private final StoredQueryDescriptionType desc;
    private final FeatureTypeInfo featureTypeInfo;

    public ParameterTypeFactory(
            StoredQueryConfiguration config,
            StoredQueryDescriptionType desc,
            FeatureTypeInfo featureTypeInfo) {
        this.config = config;
        this.desc = desc;
        this.featureTypeInfo = featureTypeInfo;
    }

    public ParameterMapping getStoredQueryParameterMapping(String parameterName) {
        ParameterMapping ret = null;

        if (this.config == null || this.config.getStoredQueryParameterMappings() == null) {
            return ret;
        }

        for (ParameterMapping sqpm : this.config.getStoredQueryParameterMappings()) {
            if (sqpm.getParameterName().equals(parameterName)) {
                ret = sqpm;
                break;
            }
        }

        return ret;
    }

    public List<ParameterType> buildStoredQueryParameters(
            Map<String, String> viewParams, Filter filter) {
        final Wfs20Factory factory = Wfs20Factory.eINSTANCE;

        if (filter == null) {
            filter = Filter.INCLUDE;
        }

        ParameterMappingContext mappingContext =
                new ParameterMappingContext(filter, viewParams, featureTypeInfo);

        List<ParameterType> ret = new ArrayList<ParameterType>();

        for (ParameterExpressionType parameter : desc.getParameter()) {
            String value = null;

            ParameterMapping mapping = getStoredQueryParameterMapping(parameter.getName());

            // If mapping is forced, use that value over what the user provides
            if (mapping != null && mapping.isForcible()) {
                value = evaluateMapping(mappingContext, mapping);

            } else {
                // Use the one provided by the user
                if (viewParams != null) {
                    value = viewParams.get(parameter.getName());
                }

                // If no value given by the user, execute mapping
                if (value == null && mapping != null) {
                    value = evaluateMapping(mappingContext, mapping);
                }
            }

            // If there is a value, add a parameter to the query
            if (value != null) {
                ParameterType tmp = factory.createParameterType();
                tmp.setName(parameter.getName());
                tmp.setValue(value);
                ret.add(tmp);
            }
        }

        return ret;
    }

    protected String evaluateMapping(
            ParameterMappingContext mappingContext, ParameterMapping mapping) {
        String value;

        if (mapping instanceof ParameterMappingDefaultValue) {
            value = ((ParameterMappingDefaultValue) mapping).getDefaultValue();
        } else if (mapping instanceof ParameterMappingExpressionValue) {
            value = ((ParameterMappingExpressionValue) mapping).evaluate(mappingContext);
        } else if (mapping instanceof ParameterMappingBlockValue) {
            value = null;
        } else {
            throw new IllegalArgumentException(
                    "Unknown StoredQueryParameterMapping: " + mapping.getClass());
        }
        return value;
    }
}
