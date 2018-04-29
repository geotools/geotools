/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014-2105, Open Source Geospatial Foundation (OSGeo)
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.filter.FilterFactoryImpl;
import org.opengis.filter.expression.PropertyName;

/**
 * Stored Query parameters may be configured as CQL expressions. This factory extends the basic CQL
 * language by adding support to string concatenation. The add (+) operand is extended so that if
 * either left hand or right hand value is not a number, the values are concatenated as strings (via
 * toString()).
 *
 * <p>The following properties are supported:
 *
 * <pre>
 *  bboxMinX       Filter envelope bounds
 *  bboxMaxX
 *  bboxMinY
 *  bboxMaxY
 *  defaultSRS	   The defaultSRS of the Feature Type in question
 *  viewparam:name View parameter used in original request ('name' is the name of the parameter)
 * </pre>
 *
 * @author Sampo Savolainen
 */
public class ParameterCQLExpressionFilterFactoryImpl extends FilterFactoryImpl {

    private Map<String, PropertyName> properties;

    public ParameterCQLExpressionFilterFactoryImpl() {
        List<ParameterCQLExpressionPropertyName> tmp =
                new ArrayList<ParameterCQLExpressionPropertyName>();

        tmp.add(
                new ParameterCQLExpressionPropertyName("bboxMinX") {
                    @Override
                    protected Object get(ParameterMappingContext context) {
                        return context.getBBOX().getMinX();
                    }
                });
        tmp.add(
                new ParameterCQLExpressionPropertyName("bboxMaxX") {
                    @Override
                    protected Object get(ParameterMappingContext context) {
                        return context.getBBOX().getMaxX();
                    }
                });
        tmp.add(
                new ParameterCQLExpressionPropertyName("bboxMinY") {
                    @Override
                    protected Object get(ParameterMappingContext context) {
                        return context.getBBOX().getMinY();
                    }
                });
        tmp.add(
                new ParameterCQLExpressionPropertyName("bboxMaxY") {
                    @Override
                    protected Object get(ParameterMappingContext context) {
                        return context.getBBOX().getMaxY();
                    }
                });
        tmp.add(
                new ParameterCQLExpressionPropertyName("defaultSRS") {
                    @Override
                    protected Object get(ParameterMappingContext context) {
                        return context.getFeatureTypeInfo().getDefaultSRS();
                    }
                });

        properties = new HashMap<String, PropertyName>();
        for (ParameterCQLExpressionPropertyName p : tmp) {
            properties.put(p.getPropertyName(), p);
        }
    }

    @Override
    public PropertyName property(String name) {
        PropertyName ret = properties.get(name);

        if (ret == null && name.startsWith("viewparam:")) {
            final String paramName = name.substring(10);
            ret =
                    new ParameterCQLExpressionPropertyName(name) {
                        @Override
                        protected Object get(ParameterMappingContext context) {
                            return context.getViewParams().get(paramName);
                        }
                    };
        }

        if (ret == null) {
            ret = super.property(name);
        }
        return ret;
    }
}
