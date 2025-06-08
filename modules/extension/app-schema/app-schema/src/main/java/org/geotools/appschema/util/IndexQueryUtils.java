/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.appschema.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.geotools.api.data.Query;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.data.DataUtilities;
import org.geotools.data.complex.AttributeMapping;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.NestedAttributeMapping;
import org.geotools.data.complex.filter.XPath;
import org.geotools.data.complex.util.XPathUtil.StepList;
import org.geotools.factory.CommonFactoryFinder;

/** @author Fernando Miño - Geosolutions */
public final class IndexQueryUtils {

    private IndexQueryUtils() {}

    /**
     * Extracts List of Sort attributes names from Query
     *
     * @return List of Sort attributes names
     */
    public static List<String> getAttributesOnSort(Query query) {
        List<String> result = new ArrayList<>();
        if (query.getSortBy() == null) return result;
        for (int i = 0; i < query.getSortBy().length; i++) {
            result.add(query.getSortBy()[i].getPropertyName().getPropertyName());
        }
        return result;
    }

    /**
     * Extracts List of attributes names from Filter
     *
     * @return List of attributes names
     */
    public static List<String> getAttributesOnFilter(Filter filter) {
        String[] attrs = DataUtilities.attributeNames(filter);
        return new ArrayList<>(Arrays.asList(attrs));
    }

    /** Checks if Expression is empty or Null */
    public static boolean isExpressionEmpty(Expression expression) {
        if (expression == null || Expression.NIL.equals(expression)) return true;
        return false;
    }

    /** Checks if property name is equals to source/identifier expression in attribute mapping */
    public static boolean equalsProperty(AttributeMapping mapping, String propertyName) {
        return equalsPropertyExpression(mapping.getSourceExpression(), propertyName)
                || equalsPropertyExpression(mapping.getIdentifierExpression(), propertyName);
    }

    /** Compare if mapping-xpath == attMapping */
    public static boolean equalsXpath(FeatureTypeMapping mapping, AttributeMapping attMapping, String xpath) {
        StepList simplifiedSteps = XPath.steps(mapping.getTargetFeature(), xpath, mapping.getNamespaces());
        return Objects.equals(attMapping.getTargetXPath(), simplifiedSteps);
    }

    /** Compare if expression == propertyName */
    public static boolean equalsPropertyExpression(Expression expression, String propertyName) {
        if (IndexQueryUtils.isExpressionEmpty(expression)) return false;
        String[] name = DataUtilities.attributeNames(expression);
        if (name.length != 1) return false;
        return Objects.equals(name[0], propertyName);
    }

    /**
     * Checks if all unrolled properties are indexed in mapping
     *
     * @return //
     */
    //    public static boolean checkAllUnrolledPropertiesIndexed(
    //            List<String> properties, FeatureTypeMapping mapping) {
    //        return !properties.stream().anyMatch(p -> mapping.getIndexAttributeNameUnrolled(p) ==
    // null);
    //    }

    /** Checks if all properties are indexed in mapping */
    public static boolean checkAllPropertiesIndexed(List<String> properties, FeatureTypeMapping mapping) {
        return !properties.stream().anyMatch(p -> mapping.getIndexAttributeName(p) == null);
    }

    /**
     * Builds an OR operator comparing Identifier with ids list
     *
     * @return Or Filter
     */
    public static Filter buildIdInExpressionOr(List<String> ids, FeatureTypeMapping mapping) {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        List<Filter> idFilters = new ArrayList<>();
        ids.forEach(idStr -> {
            idFilters.add(
                    ff.equals(ff.property(mapping.getTargetFeature().getName().getLocalPart()), ff.literal(idStr)));
        });

        return ff.or(idFilters);
    }

    /**
     * Builds a mapping->identifier IN (ids...) like function/clause
     *
     * @return Filter IN function
     */
    public static Filter buildIdInExpressionFunction(List<String> ids, FeatureTypeMapping mapping) {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        List<Expression> idExpressions = new ArrayList<>();
        String rootXpath = XPath.rootElementSteps(mapping.getTargetFeature(), mapping.getNamespaces())
                .toString();
        // add id field name
        idExpressions.add(ff.property(rootXpath));
        // add values
        ids.forEach(idStr -> {
            idExpressions.add(ff.literal(idStr));
        });
        // create Filter = id IN (val1, val2, .... valn)
        return ff.equals(ff.function("in", idExpressions.toArray(new Expression[] {})), ff.literal(true));
    }

    /**
     * Builds a mapping->identifier IN (ids...) like function/clause
     *
     * @return Filter IN function
     */
    public static Filter buildIdInExpression(List<String> ids, FeatureTypeMapping mapping) {
        return buildIdInExpressionFunction(ids, mapping);
    }

    public static AttributeMapping getIndexedAttribute(FeatureTypeMapping mapping, String xpath) {
        AttributeMapping atm = mapping.getAttributeMapping(xpath);
        if (atm != null && StringUtils.isNotEmpty(atm.getIndexField())) {
            return atm;
        }

        // XPathUtil
        // Search on Nested Attributes
        //        StepList rootStepList =
        //                XPath.rootElementSteps(mapping.getTargetFeature(),
        // mapping.getNamespaces());
        StepList stepList = XPath.steps(mapping.getTargetFeature(), xpath, mapping.getNamespaces());
        String relXpath = stepList.toString();
        for (NestedAttributeMapping nm : mapping.getNestedMappings()) {
            String nestedXpath = nm.getTargetXPath().toString();
            if (relXpath.startsWith(nestedXpath)) {
                String subXpath = relXpath.substring(nestedXpath.length() + 1, relXpath.length());
                FeatureTypeMapping ft = null;
                // nm.getNestedFeatureType(null);
                try {
                    ft = nm.getFeatureTypeMapping(null);
                } catch (IOException e) {
                    Logger.getLogger(IndexQueryUtils.class.getName()).log(Level.FINE, null, e);
                }
                if (ft != null && StringUtils.isNotBlank(subXpath)) {
                    AttributeMapping at = getIndexedAttribute(ft, subXpath);
                    if (at != null && StringUtils.isNotBlank(at.getIndexField())) return at;
                }
            }
        }
        // nothing found
        return null;
    }
}
