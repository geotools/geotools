/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.FilterFunction_property;
import org.geotools.filter.visitor.DefaultFilterVisitor;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.VolatileFunction;

/**
 * A simple visitor that extracts every attribute used by a filter or an expression
 *
 * <p>Access to this class is available via:
 *
 * <ul>
 *   <li>DataUtilities.attributeNames( Filter )
 *   <li>DataUtilities.attributeNames( Filter, FeatureType )
 *   <li>DataUtilities.attributeNames( Expression )
 *   <li>DataUtilities.attributeNames( Expression, FeatureType )
 * </ul>
 *
 * The class can also be used to determine if an expression is "static", that is, despite a complex
 * structure does not use attribute or volatile functions, and can be thus replaced by a constant:
 * for this use case refer to the {@link #isConstantExpression()} method
 *
 * @author Andrea Aime - GeoSolutions
 */
public class FilterAttributeExtractor extends DefaultFilterVisitor {

    static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();

    /** Last set visited */
    protected Set<String> attributeNames = new HashSet<String>();

    protected Set<PropertyName> propertyNames = new HashSet<PropertyName>();
    protected boolean usingVolatileFunctions;
    protected boolean usingDynamicProperties;

    /** feature type to evaluate against */
    protected SimpleFeatureType featureType;

    /** Just extract the property names; don't check against a feature type. */
    public FilterAttributeExtractor() {
        this(null);
    }
    /** Use the provided feature type as a sanity check when extracting property names. */
    public FilterAttributeExtractor(SimpleFeatureType featureType) {
        this.featureType = featureType;
    }
    /**
     * Attributes names found (so far).
     *
     * @return an unmofiable set of the attribute names found so far during the visit
     */
    public Set<String> getAttributeNameSet() {
        return Collections.unmodifiableSet(attributeNames);
    }

    /**
     * Lists the PropertyNames found so far; useful when dealing with cpath expressions involving
     * namespace informaiton.
     */
    public Set<PropertyName> getPropertyNameSet() {
        return propertyNames;
    }

    /**
     * Array of attribute names found (so far).
     *
     * @return an array of the attribute names found so far during the visit
     */
    public String[] getAttributeNames() {
        return (String[]) attributeNames.toArray(new String[attributeNames.size()]);
    }

    /** Resets the attributes found so that a new attribute search can be performed */
    public void clear() {
        attributeNames = new HashSet<String>();
        usingVolatileFunctions = false;
    }

    public Object visit(PropertyName expression, Object data) {
        if (data != null && data != attributeNames) {
            attributeNames = (Set<String>) data;
        }
        propertyNames.add(expression);

        if (featureType != null) {
            // evaluate against the feature type instead of using straight name
            // since the path from the property name may be an XPath or a
            // namespace prefixed string
            AttributeDescriptor type = (AttributeDescriptor) expression.evaluate(featureType);
            if (type != null) {
                attributeNames.add(type.getLocalName());
            } else {
                attributeNames.add(expression.getPropertyName());
            }
        } else {
            attributeNames.add(expression.getPropertyName());
        }

        return attributeNames;
    }

    public Object visit(org.opengis.filter.expression.Function expression, Object data) {
        if (expression instanceof VolatileFunction) {
            usingVolatileFunctions = true;
        }
        if (expression instanceof FilterFunction_property) {
            boolean foundLiteral = false;
            // dynamic property usage
            if (expression.getParameters() != null && expression.getParameters().size() > 0) {
                org.opengis.filter.expression.Expression firstParam =
                        expression.getParameters().get(0);

                FilterAttributeExtractor secondary = new FilterAttributeExtractor();
                firstParam.accept(secondary, null);
                if (secondary.isConstantExpression()) {
                    String name = firstParam.evaluate(null, String.class);
                    if (name != null) {
                        attributeNames.add(name);
                        propertyNames.add(FF.property(name));
                        foundLiteral = true;
                    }
                }
            }

            if (!foundLiteral) {
                usingDynamicProperties = true;
            }
        }
        return super.visit(expression, data);
    };

    /**
     * Returns true if the last visited expression is a constant, that is, does not depend on any
     * attribute and does not use any {@link VolatileFunction}
     */
    public boolean isConstantExpression() {
        return !usingVolatileFunctions
                && !usingDynamicProperties
                && getAttributeNameSet().isEmpty();
    }

    /**
     * Returns true if the expression is using dynamic property names, so a static analysis of the
     * expression won't be able to return all the properties in use
     */
    public boolean isUsingDynamincProperties() {
        return usingDynamicProperties;
    }
}
