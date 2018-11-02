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

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.geotools.data.complex.AttributeMapping;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.NestedAttributeMapping;
import org.geotools.data.complex.filter.XPathUtil;
import org.geotools.data.complex.spi.CustomAttributeExpressionFactory;
import org.geotools.data.mongodb.MongoFeatureStore;
import org.geotools.filter.ConstantExpression;
import org.opengis.filter.expression.Expression;

/** Custom nested attribute expressions builder for MongoDB. */
public class MongoNestedAttributeExpressionFactory implements CustomAttributeExpressionFactory {

    @Override
    public Expression createNestedAttributeExpression(
            FeatureTypeMapping mappings,
            XPathUtil.StepList xpath,
            NestedAttributeMapping nestedMapping) {
        return getSourceExpression(mappings, xpath, nestedMapping);
    }

    private Expression getSourceExpression(
            FeatureTypeMapping mappings,
            XPathUtil.StepList xpath,
            NestedAttributeMapping nestedMapping) {
        if (!(mappings.getSource() instanceof MongoFeatureStore)) {
            // nothing to do here
            return null;
        }
        if (!nestedMapping
                .getTargetXPath()
                .equalsIgnoreIndex(xpath.subList(0, nestedMapping.getTargetXPath().size()))) {
            return Expression.NIL;
        }
        int steps = xpath.size();
        XPathUtil.StepList finalXpath = xpath.subList(nestedMapping.getTargetXPath().size(), steps);
        AttributeMapping attributeMapping = nestedMapping;
        String jsonPath = addPath(attributeMapping, "");
        int end = finalXpath.size();
        int start = 0;
        while (end > start) {
            try {
                SearchResult result = search(finalXpath.subList(start, end), attributeMapping);
                if (!result.found) {
                    break;
                }
                attributeMapping = result.attributeMapping;
                jsonPath = addPath(attributeMapping, jsonPath);
                start += result.index;
            } catch (Exception exception) {
                throw new RuntimeException("Error getting feature type mapping.");
            }
        }
        if (attributeMapping == null) {
            return Expression.NIL;
        }
        Expression sourceExpression = attributeMapping.getSourceExpression();
        if (sourceExpression instanceof JsonSelectFunction) {
            JsonSelectAllFunction jsonSelect = new JsonSelectAllFunction();
            jsonPath = addPath(jsonPath, ((JsonSelectFunction) sourceExpression).getJsonPath());
            List<Expression> parameters =
                    Collections.singletonList(ConstantExpression.constant(jsonPath));
            jsonSelect.setParameters(parameters);
            return jsonSelect;
        }
        return sourceExpression;
    }

    private String addPath(AttributeMapping attribute, String currentPath) {
        if (attribute instanceof MongoNestedMapping) {
            Expression sourceExpression = attribute.getSourceExpression();
            if (sourceExpression instanceof CollectionLinkFunction) {
                String collection = ((CollectionLinkFunction) sourceExpression).getPath();
                return addPath(currentPath, collection);
            }
        }
        return currentPath;
    }

    private String addPath(String currentPath, String newPath) {
        return currentPath == null || currentPath.isEmpty() ? newPath : currentPath + "." + newPath;
    }

    private static final class SearchResult {

        static final SearchResult NOT_FOUND = new SearchResult(false, -1, null);

        final boolean found;
        final int index;
        final AttributeMapping attributeMapping;

        SearchResult(boolean found, int index, AttributeMapping attributeMapping) {
            this.found = found;
            this.index = index;
            this.attributeMapping = attributeMapping;
        }
    }

    private SearchResult search(XPathUtil.StepList xpath, AttributeMapping attributeMapping)
            throws Exception {
        for (int i = xpath.size(); i > 0; i--) {
            AttributeMapping foundAttributeMapping = match(attributeMapping, xpath.subList(0, i));
            if (foundAttributeMapping != null) {
                return new SearchResult(true, i, foundAttributeMapping);
            }
        }
        return SearchResult.NOT_FOUND;
    }

    private AttributeMapping match(AttributeMapping attributeMapping, XPathUtil.StepList xpath)
            throws IOException {
        if (attributeMapping instanceof NestedAttributeMapping) {
            FeatureTypeMapping mappings =
                    ((NestedAttributeMapping) attributeMapping).getFeatureTypeMapping(null);
            List<AttributeMapping> attributesMappings = mappings.getAttributeMappings();
            for (AttributeMapping candidateAttributeMapping : attributesMappings) {
                if (xpath.equalsIgnoreIndex(candidateAttributeMapping.getTargetXPath())) {
                    return candidateAttributeMapping;
                }
            }
        }
        return null;
    }
}
