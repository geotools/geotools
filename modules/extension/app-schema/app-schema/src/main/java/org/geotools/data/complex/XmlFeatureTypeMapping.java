/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.geotools.data.FeatureSource;
import org.geotools.data.complex.PathAttributeList.Pair;
import org.geotools.data.complex.util.XPathUtil.Step;
import org.geotools.data.complex.util.XPathUtil.StepList;
import org.geotools.data.complex.xml.XmlFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.LiteralExpressionImpl;
import org.opengis.feature.Attribute;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.PropertyName;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * @author Russell Petty (GeoScience Victoria)
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 */
public class XmlFeatureTypeMapping extends FeatureTypeMapping {

    /** Constants for manipulating XPath Expressions */
    private static final String XPATH_SEPARATOR = "/";

    private static final String XPATH_PROPERTY_SEPARATOR = "/@";

    private static final String XPATH_LEFT_INDEX_BRACKET = "[";

    private static final String XPATH_RIGHT_INDEX_BRACKET = "]";

    private static final String AS_XPATH_FUNCTION = "asXpath";

    /** Output xpath to input xpath map */
    private Map<String, Expression> mapping = new HashMap<String, Expression>();

    /** List of labelled AttributeMappings */
    private AttributeCreateOrderList attOrderedTypeList = null;

    /** Label to AttributeMapping map */
    private Map<String, AttributeMapping> indexAttributeList;

    AttributeMapping rootAttribute;

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    /** Attributes that don't have their own label, therefore are children of another node. */
    List<AttributeMapping> setterAttributes = new ArrayList<AttributeMapping>();

    PathAttributeList elements;

    protected String itemXpath;

    /** No parameters constructor for use by the digester configuration engine as a JavaBean */
    public XmlFeatureTypeMapping() {
        super(null, null, new LinkedList<AttributeMapping>(), new NamespaceSupport());
    }

    public XmlFeatureTypeMapping(
            FeatureSource source,
            AttributeDescriptor target,
            List<AttributeMapping> mappings,
            NamespaceSupport namespaces,
            String itemXpath) {
        super(source, target, mappings, namespaces);
        this.itemXpath = itemXpath;
        elements = new PathAttributeList();
        ((XmlFeatureSource) source).setItemXpath(itemXpath);
        try {
            populateFeatureData();
        } catch (IOException ex) {
            throw new RuntimeException(
                    "Error occured when trying to create attribute mappings", ex);
        }
    }

    public List<Expression> getExpressionsIgnoreIndex(final StepList targetPath) {
        List<Expression> mappings = new ArrayList<Expression>();
        String path = targetPath.toString();

        Collection<String> c = mapping.keySet();

        // obtain an Iterator for Collection
        Iterator<String> itr = c.iterator();

        // iterate through HashMap values iterator
        while (itr.hasNext()) {
            String listPath = itr.next();
            String unindexedListPath = removeIndexFromPath(listPath);
            if (path.equals(unindexedListPath)) {
                mappings.add(mapping.get(listPath));
            }
        }

        if (mappings.isEmpty()) {
            // look in the setter attributes
            Iterator<AttributeMapping> leafAtts = setterAttributes.iterator();
            while (leafAtts.hasNext()) {
                AttributeMapping att = leafAtts.next();
                String listPath = att.getTargetXPath().toString();
                String unindexedListPath = removeIndexFromPath(listPath);
                if (path.equals(unindexedListPath)) {
                    mappings.add(att.getSourceExpression());
                }
            }
        }

        return mappings;
    }

    private String removeIndexFromPath(String inputPath) {
        String tempPath = inputPath;
        while (tempPath.contains(XPATH_LEFT_INDEX_BRACKET)) {
            int leftIndex = tempPath.indexOf(XPATH_LEFT_INDEX_BRACKET);
            int rightIndex = tempPath.indexOf(XPATH_RIGHT_INDEX_BRACKET, leftIndex);
            String leftTempPath = tempPath.substring(0, leftIndex);
            String rightTempPath = tempPath.substring(rightIndex + 1);
            tempPath = leftTempPath + rightTempPath;
        }

        return tempPath;
    }

    /**
     * Finds the attribute mappings for the given source expression.
     *
     * @return list of matching attribute mappings
     */
    public List<AttributeMapping> getAttributeMappingsByExpression(
            final Expression sourceExpression) {
        AttributeMapping attMapping;
        List<AttributeMapping> mappings = Collections.emptyList();
        for (Iterator<AttributeMapping> it = attributeMappings.iterator(); it.hasNext(); ) {
            attMapping = (AttributeMapping) it.next();
            if (sourceExpression.equals(attMapping.getSourceExpression())) {
                if (mappings.size() == 0) {
                    mappings = new ArrayList<AttributeMapping>(2);
                }
                mappings.add(attMapping);
            }
        }
        return mappings;
    }

    /**
     * Finds an attribute mapping by label.
     *
     * @param label The attribute mapping label.
     * @return Attribute mapping that matches the label, or null.
     */
    public AttributeMapping getAttributeMappingByLabel(String label) {
        AttributeMapping attMapping;
        for (Iterator<AttributeMapping> it = attributeMappings.iterator(); it.hasNext(); ) {
            attMapping = (AttributeMapping) it.next();
            if (label.equals(attMapping.getLabel())) {
                return attMapping;
            }
        }
        return null;
    }

    /**
     * Finds the attribute mapping for the target expression <code>exactPath</code>
     *
     * @param exactPath the xpath expression on the target schema to find the mapping for
     * @return the attribute mapping that match 1:1 with <code>exactPath</code> or <code>null</code>
     *     if
     */
    public AttributeMapping getStringMapping(final StepList exactPath) {
        AttributeMapping attMapping;
        for (Iterator<AttributeMapping> it = attributeMappings.iterator(); it.hasNext(); ) {
            attMapping = (AttributeMapping) it.next();
            if (exactPath.equals(attMapping.getTargetXPath())) {
                return attMapping;
            }
        }
        return null;
    }

    public void populateFeatureData() throws IOException {

        List<AttributeMapping> attMap = getAttributeMappings();
        if (attOrderedTypeList == null) {
            initialiseAttributeLists(attMap);
        }
        // create required elements
        String xpath =
                rootAttribute.getInstanceXpath() == null
                        ? itemXpath
                        : itemXpath + XPATH_SEPARATOR + rootAttribute.getInstanceXpath();

        elements.put(rootAttribute.getLabel(), xpath, null);
        Expression idExpression = rootAttribute.getIdentifierExpression();
        if (!idExpression.equals(Expression.NIL)) {
            Expression id;
            if (!(idExpression instanceof Function) && rootAttribute.getInstanceXpath() != null) {
                id = ff.property(rootAttribute.getInstanceXpath() + XPATH_SEPARATOR + idExpression);
            } else {
                id = idExpression;
            }
            mapping.put("@gml:id", id);
        }

        // iterator returns the attribute mappings starting from the root of the tree.
        // parents are always returned before children elements.
        Iterator<AttributeMapping> it = attOrderedTypeList.iterator();
        addComplexAttributes(elements, it);
        addSetterAttributes(elements);

        removeAllRelativePaths();
    }

    private void addComplexAttributes(PathAttributeList elements, Iterator<AttributeMapping> it) {
        while (it.hasNext()) {
            AttributeMapping attMapping = it.next();
            final Expression sourceExpression = attMapping.getIdentifierExpression();

            List<Pair> ls = elements.get(attMapping.getParentLabel());
            if (ls != null) {
                for (int i = 0; i < ls.size(); i++) {
                    Pair parentAttribute = ls.get(i);
                    String instancePath = attMapping.getInstanceXpath();
                    int count = 1;
                    String countXpath = parentAttribute.getXpath();
                    // if instance path not set, then element exists, with one instance
                    if (instancePath != null) {
                        countXpath = countXpath + XPATH_SEPARATOR + instancePath;
                    }

                    for (int j = 0; j < count; j++) {
                        final String bracketIndex = "";
                        String xpath;
                        if (instancePath == null) {
                            xpath =
                                    parentAttribute.getXpath()
                                            + XPATH_SEPARATOR
                                            + sourceExpression.toString();
                        } else {
                            xpath =
                                    parentAttribute.getXpath()
                                            + XPATH_SEPARATOR
                                            + instancePath
                                            + bracketIndex
                                            + XPATH_SEPARATOR
                                            + sourceExpression.toString();
                        }
                        String label = getFullQueryPath(attMapping);

                        mapping.put(
                                label + XPATH_PROPERTY_SEPARATOR + "gml:id", ff.property(xpath));

                        StepList sl = attMapping.getTargetXPath();
                        setPathIndex(j, sl);
                        Attribute subFeature = null;
                        elements.put(attMapping.getLabel(), countXpath + bracketIndex, subFeature);
                    }
                }
            }
        }
    }

    private void addSetterAttributes(PathAttributeList elements) {
        for (AttributeMapping attMapping : setterAttributes) {
            List<Pair> ls = elements.get(attMapping.getParentLabel());
            if (ls != null) {
                for (int i = 0; i < ls.size(); i++) {
                    Pair parentPair = ls.get(i);
                    final Expression sourceExpression = attMapping.getSourceExpression();
                    String prefix = parentPair.getXpath();

                    Expression usedXpath = getValue(prefix, sourceExpression, attMapping);

                    String label = getFullQueryPath(attMapping);
                    mapping.put(label, usedXpath);
                    if (usedXpath instanceof PropertyName) {
                        // if current source expression is an inputAttribute
                        // work out the client properties value from the inputAttribute
                        // xpath, which would already have the parent prefix in it
                        addClientProperties(attMapping, usedXpath.toString(), label);
                    } else {
                        // if source expression is a constant or function
                        // just add the parent prefix to work out client properties values
                        addClientProperties(attMapping, prefix, label);
                    }
                }
            }
        }
    }

    private void addClientProperties(AttributeMapping attMapping, String prefix, String label) {
        Map<Name, Expression> clientProperties = attMapping.getClientProperties();
        if (clientProperties.size() != 0) {

            for (Map.Entry<Name, Expression> entry : clientProperties.entrySet()) {
                Name propName = entry.getKey();
                Expression propExpr = entry.getValue();
                Expression xPath = getValue(prefix, propExpr, attMapping);
                mapping.put(
                        label + XPATH_PROPERTY_SEPARATOR + getPropertyNameXpath(propName), xPath);
            }
        }
    }

    private String getPropertyNameXpath(Name propName) {
        String xpath;
        String namespaceUri = propName.getNamespaceURI();
        if (namespaceUri != null) {
            String namespace = namespaces.getPrefix(namespaceUri);
            xpath = namespace + propName.getSeparator() + propName.getLocalPart();
        } else {
            xpath = propName.getLocalPart();
        }
        return xpath;
    }

    private void setPathIndex(int j, StepList sl) {
        if (j > 0) {
            Step st = sl.get(sl.size() - 1);
            Step st2 = new Step(st.getName(), j + 1, st.isXmlAttribute());
            sl.remove(sl.size() - 1);
            sl.add(st2);
        }
    }

    protected Expression getValue(String xpathPrefix, Expression node, AttributeMapping mapping) {
        Expression value = null;
        if (node instanceof Function) {
            // function
            Function func = (Function) node;
            Expression exp = getAsXpathExpression(func, mapping);
            if (exp != null) {
                // this function must be an AsXpath
                // return the extracted expression
                value = exp;
            } else {
                // not an asXpath, but might have it in the param somewhere
                // the param would already be turned into the extracted expression
                // return the function
                value = func;
            }
        } else if (node instanceof LiteralExpressionImpl) {
            // constant
            value = node;
        } else {
            // input attribute
            String expressionValue = node.toString();
            if (xpathPrefix.length() > 0) {
                value = ff.property(xpathPrefix + XPATH_SEPARATOR + expressionValue);
            } else {
                value = node;
            }
        }
        return value;
    }

    /**
     * Find asXpath in a function, which might be the function itself or a parameter of the
     * function, and extract the xpath value including itemXpath and instancePath prefixes.
     *
     * @param func The function
     * @param mapping The attribute mapping
     * @return xpath expression or null
     */
    private Expression getAsXpathExpression(Function func, AttributeMapping mapping) {
        if (func.getName().equals(AS_XPATH_FUNCTION)) {
            // get original filter xpath
            Expression queryXpath = func.getParameters().get(0);
            // get the attribute mapping full xpath and append to the function param
            String prefix;
            String parentLabel = mapping.getParentLabel();
            if (parentLabel == null) {
                // must be root
                prefix = elements.getPath(rootAttribute.getLabel());
            } else {
                prefix = elements.getPath(parentLabel);
                String instancePath = mapping.getInstanceXpath();
                if (instancePath != null) {
                    prefix += XPATH_SEPARATOR + instancePath;
                }
            }
            Expression fullXpath = ff.property(prefix + XPATH_SEPARATOR + queryXpath);
            return fullXpath;
        } else {
            List<Expression> params = func.getParameters();
            for (int i = 0; i < params.size(); i++) {
                Expression param = params.get(i);
                if (param instanceof Function) {
                    Expression expr = getAsXpathExpression((Function) param, mapping);
                    if (expr != null) {
                        // found asXpath and returned an expression
                        // set as the new parameter
                        func.getParameters().remove(i);
                        func.getParameters().add(expr);
                    }
                }
            }
            return null;
        }
    }

    private void initialiseAttributeLists(List<AttributeMapping> mappings) {

        for (AttributeMapping attMapping : mappings) {
            if (attMapping.getLabel() != null
                    && attMapping.getParentLabel() == null
                    && attMapping.getTargetNodeInstance() == null) {

                rootAttribute = attMapping;
                break;
            }
        }

        attOrderedTypeList = new AttributeCreateOrderList(rootAttribute.getLabel());
        indexAttributeList = new HashMap<String, AttributeMapping>();
        indexAttributeList.put(rootAttribute.getLabel(), rootAttribute);

        for (AttributeMapping attMapping : mappings) {
            if (attMapping.getLabel() == null) {
                setterAttributes.add(attMapping);
            } else if (attMapping.getParentLabel() != null) {
                attOrderedTypeList.put(attMapping);
                indexAttributeList.put(attMapping.getLabel(), attMapping);
            }
        }
    }

    private String getFullQueryPath(AttributeMapping attMapping) {
        return attMapping.getTargetXPath().toString();
    }

    private void removeAllRelativePaths() {

        Collection<String> c = mapping.keySet();
        Iterator<String> itr = c.iterator();

        while (itr.hasNext()) {
            String key = itr.next();
            Expression xPath = mapping.get(key);
            Expression xPath2 = removeRelativePaths(xPath);
            if (!xPath.toString().equals(xPath2.toString())) {
                mapping.put(key, xPath2);
            }
        }
    }

    private PropertyName removeRelativePaths(Expression xPath) {
        String xPathTemp = xPath.toString();
        final int NOT_FOUND = -1;
        final String RELATIVE_PATH = "/../";

        int i = xPathTemp.indexOf(RELATIVE_PATH);
        while (i != NOT_FOUND) {
            int slashPos = xPathTemp.lastIndexOf(XPATH_SEPARATOR, i - 1);
            if (slashPos != NOT_FOUND) {
                String left = xPathTemp.substring(0, slashPos + 1);
                String right = xPathTemp.substring(i + RELATIVE_PATH.length());
                xPathTemp = left + right;
            } else {
                break;
            }
            i = xPathTemp.indexOf(RELATIVE_PATH);
        }
        return ff.property(xPathTemp);
    }

    /**
     * Looks up for attribute mappings matching the xpath expression <code>propertyName</code>.
     *
     * <p>If any step in <code>propertyName</code> has index greater than 1, any mapping for the
     * same property applies, regardless of the mapping. For example, if there are mappings for
     * <code>gml:name[1]</code>, <code>gml:name[2]</code> and <code>gml:name[3]</code>, but
     * propertyName is just <code>gml:name</code>, all three mappings apply.
     */
    @Override
    public List<Expression> findMappingsFor(
            final StepList propertyName, boolean includeNestedMappings) {
        List<Expression> expressions = null;

        // get all matching mappings if index is not specified, otherwise
        // get the specified mapping
        if (!propertyName.toString().contains("[")) {
            // collect all the mappings for the given property
            expressions = getExpressionsIgnoreIndex(propertyName);
        } else {
            // get specified mapping if indexed
            expressions = new ArrayList<Expression>(1);
            AttributeMapping mapping = getStringMapping(propertyName);
            if (mapping != null) {
                expressions.add(mapping.getSourceExpression());
            }
        }
        return expressions;
    }
}
