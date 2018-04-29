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
package org.geotools.data.mongodb.complex;

import java.util.Map;
import org.geotools.data.complex.NestedAttributeMapping;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator;
import org.geotools.data.complex.filter.XPathUtil;
import org.geotools.data.complex.spi.CustomMappingFactory;
import org.opengis.feature.type.Name;
import org.opengis.filter.expression.Expression;
import org.xml.sax.helpers.NamespaceSupport;

/** Custom nested attributes mappings builder for MongoDB. */
public class MongoNestedMappingFactory implements CustomMappingFactory {

    @Override
    public NestedAttributeMapping createNestedAttributeMapping(
            AppSchemaDataAccessConfigurator configuration,
            Expression idExpression,
            Expression parentExpression,
            XPathUtil.StepList targetXPath,
            boolean isMultiValued,
            Map<Name, Expression> clientProperties,
            Expression sourceElement,
            XPathUtil.StepList sourcePath,
            NamespaceSupport namespaces) {
        try {
            if (parentExpression instanceof CollectionLinkFunction) {
                return new MongoNestedMapping(
                        idExpression,
                        parentExpression,
                        targetXPath,
                        isMultiValued,
                        clientProperties,
                        sourceElement,
                        sourcePath,
                        namespaces);
            }
            // not a MongoDB mapping
            return null;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
