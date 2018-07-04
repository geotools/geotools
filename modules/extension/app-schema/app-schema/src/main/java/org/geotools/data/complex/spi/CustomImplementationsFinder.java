/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.complex.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.NestedAttributeMapping;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator;
import org.geotools.data.complex.filter.XPathUtil;
import org.opengis.feature.type.Name;
import org.opengis.filter.expression.Expression;
import org.xml.sax.helpers.NamespaceSupport;

/** Helper class used to find implementations for extensions points. */
public final class CustomImplementationsFinder {

    private static List<CustomMappingFactory> mappingsFactories = new ArrayList<>();
    private static List<CustomAttributeExpressionFactory> attributesFactories = new ArrayList<>();

    static {
        mappingsFactories = initFactories(CustomMappingFactory.class);
        attributesFactories = initFactories(CustomAttributeExpressionFactory.class);
    }

    private CustomImplementationsFinder() {}

    private static <T> List<T> initFactories(Class<T> type) {
        ServiceLoader<T> loader = ServiceLoader.load(type);
        loader.reload();
        List<T> factories = new ArrayList<>();
        for (T aLoader : loader) {
            factories.add(aLoader);
        }
        return factories;
    }

    public static NestedAttributeMapping find(
            AppSchemaDataAccessConfigurator configuration,
            Expression idExpression,
            Expression parentExpression,
            XPathUtil.StepList targetXPath,
            boolean isMultiValued,
            Map<Name, Expression> clientProperties,
            Expression sourceElement,
            XPathUtil.StepList sourcePath,
            NamespaceSupport namespaces) {
        for (CustomMappingFactory factory : mappingsFactories) {
            NestedAttributeMapping mapping =
                    factory.createNestedAttributeMapping(
                            configuration,
                            idExpression,
                            parentExpression,
                            targetXPath,
                            isMultiValued,
                            clientProperties,
                            sourceElement,
                            sourcePath,
                            namespaces);
            if (mapping != null) {
                return mapping;
            }
        }
        return null;
    }

    public static Expression find(
            FeatureTypeMapping mappings,
            XPathUtil.StepList xpath,
            NestedAttributeMapping nestedMapping) {
        for (CustomAttributeExpressionFactory factory : attributesFactories) {
            Expression attributeExpression =
                    factory.createNestedAttributeExpression(mappings, xpath, nestedMapping);
            if (attributeExpression != null) {
                return attributeExpression;
            }
        }
        return null;
    }
}
