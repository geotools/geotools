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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.apache.commons.jxpath.JXPathException;
import org.geotools.appschema.feature.AppSchemaAttributeBuilder;
import org.geotools.appschema.util.XmlXpathUtilites;
import org.geotools.data.Query;
import org.geotools.data.complex.PathAttributeList.Pair;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.data.complex.util.ComplexFeatureConstants;
import org.geotools.data.complex.util.XPathUtil.*;
import org.geotools.data.complex.xml.*;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.type.ComplexTypeImpl;
import org.geotools.filter.LiteralExpressionImpl;
import org.opengis.feature.Attribute;
import org.opengis.feature.Feature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.xml.sax.Attributes;

/**
 * An implementation of AbstractMappingFeatureIterator to handle XML datasources.
 *
 * @author Russell Petty (GeoScience Victoria)
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 */
public class XmlMappingFeatureIterator extends DataAccessMappingFeatureIterator {

    /** Constants for manipulating XPath Expressions */
    public static final String XPATH_SEPARATOR = "/";

    private static final String XPATH_LEFT_INDEX_BRACKET = "[";

    private static final String XPATH_RIGHT_INDEX_BRACKET = "]";

    protected XmlResponse xmlResponse;

    private AttributeCreateOrderList attOrderedTypeList = null;

    private int count = 0;

    private int indexCounter = 1;

    private String idXpath;
    /**
     * @param mapping place holder for the target type, the surrogate FeatureSource and the mappings
     *     between them.
     * @param query the query over the target feature type, that is to be unpacked to its equivalent
     *     over the surrogate feature type.
     */
    public XmlMappingFeatureIterator(
            AppSchemaDataAccess store, FeatureTypeMapping mapping, Query query) throws IOException {
        super(store, mapping, query, null, false);

        setIdXPath();

        if (xmlResponse == null) {
            this.xmlResponse = ((XmlFeatureCollection) sourceFeatures).xmlResponse();
        }

        List<Integer> ls = xmlResponse.getValidFeatureIndex();
        count = ls.size();
    }

    public XmlMappingFeatureIterator(
            AppSchemaDataAccess store,
            FeatureTypeMapping mapping,
            Query query,
            String xpath,
            String value)
            throws IOException {
        super(store, mapping, query, null, false);

        setIdXPath();

        if (xmlResponse == null) {
            this.xmlResponse = ((XmlFeatureCollection) sourceFeatures).xmlResponse(xpath, value);
        }

        List<Integer> ls = xmlResponse.getValidFeatureIndex();
        count = ls.size();
    }

    private void setIdXPath() {
        idXpath =
                mapping.getFeatureIdExpression().equals(Expression.NIL)
                        ? "@id"
                        : mapping.getFeatureIdExpression().toString();
    }

    protected FeatureIterator<? extends Feature> getSourceFeatureIterator() {
        return null;
    }

    protected boolean isSourceFeatureIteratorNull() {
        return xmlResponse == null;
    }

    @Override
    protected String extractIdForAttribute(final Expression idExpression, Object sourceInstance) {
        try {
            if (idExpression instanceof Function) {
                // special handling for functions
                XmlXpathFilterData data =
                        new XmlXpathFilterData(
                                namespaces,
                                xmlResponse.getDoc(),
                                -1,
                                XmlMappingFeatureIterator.createIndexedItemXpathString(
                                        (XmlFeatureTypeMapping) mapping,
                                        xmlResponse,
                                        indexCounter));
                Object value = idExpression.evaluate(data);
                return (value == null ? "" : value.toString());
            } else {
                return XmlXpathUtilites.getSingleXPathValue(
                        mapping.getNamespaces(),
                        createIndexedItemXpathString(
                                        (XmlFeatureTypeMapping) mapping, xmlResponse, indexCounter)
                                + XPATH_SEPARATOR
                                + idXpath,
                        xmlResponse.getDoc());
            }
        } catch (RuntimeException e) {
            if (e.getCause() instanceof JXPathException) {
                // only log info since id is not always compulsory
                LOGGER.info(
                        "Feature id is not mapped for: " + mapping.getTargetFeature().getName());
            } else {
                throw e;
            }
        }
        return null;
    }

    public static String createIndexedItemXpathString(
            XmlFeatureTypeMapping mapping, XmlResponse xmlResponse, int indexCounter) {
        String rootXpath =
                mapping.itemXpath
                        + XPATH_LEFT_INDEX_BRACKET
                        + xmlResponse.getValidFeatureIndex().get(indexCounter - 1)
                        + XPATH_RIGHT_INDEX_BRACKET;

        if (mapping.rootAttribute.getInstanceXpath() != null) {
            rootXpath += XPATH_SEPARATOR + mapping.rootAttribute.getInstanceXpath();
        }

        return rootXpath;
    }

    protected Feature populateFeatureData() throws IOException {

        final AttributeDescriptor targetNode = mapping.getTargetFeature();
        AppSchemaAttributeBuilder builder = new AppSchemaAttributeBuilder(attf);
        builder.setDescriptor(targetNode);
        Feature target =
                (Feature)
                        builder.build(
                                extractIdForAttribute(mapping.getFeatureIdExpression(), null));

        if (attOrderedTypeList == null) {
            initialiseAttributeLists(mapping.getAttributeMappings());
        }

        // create required elements
        PathAttributeList elements = populateAttributeList(target);
        setAttributeValues(elements, target);
        indexCounter++;
        return target;
    }

    @SuppressWarnings("unchecked")
    private void setAttributeValues(PathAttributeList elements, Feature target) throws IOException {
        for (AttributeMapping attMapping : ((XmlFeatureTypeMapping) mapping).setterAttributes) {
            String parentLabel = attMapping.getParentLabel();
            List<Pair> ls = elements.get(parentLabel);
            if (ls != null) {
                final Expression sourceExpression = attMapping.getSourceExpression();
                final Expression idExpression = attMapping.getIdentifierExpression();

                for (int i = 0; i < ls.size(); i++) {
                    Pair parentPair = ls.get(i);
                    Attribute setterTarget = parentPair.getAttribute();
                    // find the root attribute mapping from the xpath
                    // get the full xpath query including
                    StepList xpath = attMapping.getTargetXPath().clone();
                    Object value = getValue(parentPair.getXpath(), sourceExpression, target);

                    xpath.get(0).setIndex(i + 1);

                    if (value != null) {
                        if (value instanceof Collection) {
                            Collection<Object> values = (Collection) value;
                            for (Object val : values) {
                                setValues(
                                        val,
                                        setterTarget,
                                        parentPair,
                                        attMapping,
                                        target,
                                        xpath,
                                        idExpression);
                            }
                        } else {
                            setValues(
                                    value,
                                    setterTarget,
                                    parentPair,
                                    attMapping,
                                    target,
                                    xpath,
                                    idExpression);
                        }
                    }
                }
            }
        }
    }

    private void setValues(
            Object value,
            Attribute setterTarget,
            Pair parentPair,
            AttributeMapping attMapping,
            Feature target,
            StepList xpath,
            Expression idExpression)
            throws IOException {
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer(
                    "setting target="
                            + setterTarget.getName()
                            + ", targetXpath="
                            + attMapping.getTargetXPath()
                            + ", value="
                            + value);
        }

        String featureId = getId(idExpression, parentPair, attMapping, "");
        //        Attribute att = xpathAttributeBuilder.set(target,
        //                xpath, value, featureId, attMapping.getTargetNodeInstance(), false,
        // attMapping
        //                        .getSourceExpression());
        Attribute att = setAttributeValue(target, featureId, null, attMapping, value, xpath, null);
        setClientProperties(att, parentPair.getXpath(), attMapping.getClientProperties());
    }

    private void setMappedIndex(Attribute att, int index) {
        att.getUserData().put(ComplexFeatureConstants.MAPPED_ATTRIBUTE_INDEX, index);
    }

    protected void setClientProperties(
            final Attribute target,
            final Object xpathPrefix,
            final Map<Name, Expression> clientProperties) {
        if (target == null) {
            return;
        }

        // NC - first calculate target attributes
        final Map<Name, Object> targetAttributes = new HashMap<Name, Object>();
        if (target.getUserData().containsValue(Attributes.class)) {
            targetAttributes.putAll(
                    (Map<? extends Name, ? extends Object>)
                            target.getUserData().get(Attributes.class));
        }
        for (Map.Entry<Name, Expression> entry : clientProperties.entrySet()) {
            Name propName = entry.getKey();
            Object propExpr = entry.getValue();
            Object propValue;
            if (propExpr instanceof Expression) {
                propValue =
                        getValue(
                                (xpathPrefix == null ? "" : xpathPrefix.toString()),
                                (Expression) propExpr,
                                target);
            } else {
                propValue = propExpr;
            }
            if (propValue != null) {
                if (propValue instanceof Collection) {
                    if (!((Collection) propValue).isEmpty()) {
                        propValue = ((Collection) propValue).iterator().next();
                        targetAttributes.put(propName, propValue);
                    }
                } else {
                    targetAttributes.put(propName, propValue);
                }
            }
        }
        // FIXME should set a child Property.. but be careful for things that
        // are smuggled in there internally and don't exist in the schema, like
        // XSDTypeDefinition, CRS etc.
        if (targetAttributes.size() > 0) {
            target.getUserData().put(Attributes.class, targetAttributes);
        }
        setGeometryUserData(target, targetAttributes);
    }

    private String bracketedIndex(int j) {
        return XPATH_LEFT_INDEX_BRACKET + Integer.toString(j) + XPATH_RIGHT_INDEX_BRACKET;
    }

    private PathAttributeList populateAttributeList(Feature target) throws IOException {
        PathAttributeList elements = new PathAttributeList();
        String rootPrefix =
                createIndexedItemXpathString(
                        (XmlFeatureTypeMapping) mapping, xmlResponse, indexCounter);
        elements.put(
                ((XmlFeatureTypeMapping) mapping).rootAttribute.getLabel(), rootPrefix, target);

        setClientProperties(
                target,
                rootPrefix,
                ((XmlFeatureTypeMapping) mapping).rootAttribute.getClientProperties());

        // iterator returns the attribute mappings starting from the root of the tree.
        // parents are always returned before children elements.
        Iterator<AttributeMapping> it = attOrderedTypeList.iterator();
        while (it.hasNext()) {
            AttributeMapping attMapping = it.next();
            final Expression idExpression = attMapping.getIdentifierExpression();

            List<Pair> ls = elements.get(attMapping.getParentLabel());
            if (ls != null) {
                for (int i = 0; i < ls.size(); i++) {
                    Pair parentAttribute = ls.get(i);
                    String countXpath = parentAttribute.getXpath();
                    // if instance path not set, then only count the root node
                    if (attMapping.getInstanceXpath() != null) {
                        countXpath += XPATH_SEPARATOR + attMapping.getInstanceXpath();
                    }
                    int count =
                            XmlXpathUtilites.countXPathNodes(
                                    mapping.getNamespaces(), countXpath, xmlResponse.getDoc());

                    createSubFeaturesAndAddToAttributeList(
                            elements,
                            attMapping,
                            idExpression,
                            parentAttribute,
                            count,
                            countXpath,
                            target);
                }
            }
        }
        return elements;
    }

    private String getId(
            Expression idExpression,
            Pair parentAttribute,
            AttributeMapping attMapping,
            String bracketIndex) {
        String featureId = null;
        String idPath;
        if (idExpression instanceof Function) {
            // get parent attribute xpath and append this instance xpath + index
            idPath = parentAttribute.getXpath() + XPATH_SEPARATOR;
            if (attMapping.getInstanceXpath() != null) {
                idPath += attMapping.getInstanceXpath() + bracketIndex;
            }

            XmlXpathFilterData data =
                    new XmlXpathFilterData(namespaces, xmlResponse.getDoc(), -1, idPath);
            featureId = idExpression.evaluate(data, String.class);
        } else {
            idPath = setFeatureXpath(attMapping, idExpression, parentAttribute, bracketIndex);
            List<String> featureIdList = getValue(idPath);
            if (!featureIdList.isEmpty()) {
                featureId = featureIdList.get(0);
            }
        }
        return featureId;
    }

    private void createSubFeaturesAndAddToAttributeList(
            PathAttributeList elements,
            AttributeMapping attMapping,
            final Expression idExpression,
            Pair parentAttribute,
            int count,
            String countXpath,
            Feature target)
            throws IOException {

        StepList sl = attMapping.getTargetXPath().clone();
        setPathIndex(parentAttribute.getAttribute(), sl);

        for (int j = 1; j <= count; j++) {
            final String bracketIndex = bracketedIndex(j);
            String featureId = getId(idExpression, parentAttribute, attMapping, bracketIndex);

            setLastElementIndex(parentAttribute.getAttribute(), sl, j);

            Attribute subFeature =
                    xpathAttributeBuilder.set(
                            target,
                            sl,
                            null,
                            featureId,
                            attMapping.getTargetNodeInstance(),
                            false,
                            attMapping.getSourceExpression());
            //            Attribute subFeature = setAttributeValue(parentAttribute.getAttribute(),
            // featureId,
            //                    null, attMapping, null, sl, null);

            String xpath = countXpath + bracketIndex;
            setClientProperties(subFeature, xpath, attMapping.getClientProperties());
            setMappedIndex(subFeature, j);
            elements.put(attMapping.getLabel(), xpath, subFeature);
        }
    }

    /**
     * Find the last element in the given path and set the index.
     *
     * @param parent Parent attribute where the path is going to be set in.
     * @param sl The path to be set.
     * @param index The index for the last element.
     */
    private void setLastElementIndex(Attribute parent, StepList sl, int index) {
        if (!(parent.getType() instanceof ComplexTypeImpl)) {
            // not a complex type, so just set the first index as a simple type
            // can't have another complex type as children anyway
            sl.get(0).setIndex(index);
            return;
        }
        ComplexTypeImpl type = (ComplexTypeImpl) parent.getType();
        // check the last step first and gradually move to the previous one
        int lastIndex = sl.size() - 1;
        Name lastStep = Types.toTypeName(sl.get(lastIndex).getName());
        while (!Types.isElement(type, lastStep)) {
            lastIndex--;
            if (lastIndex < 0) {
                return;
            }
            lastStep = Types.toTypeName(sl.get(lastIndex).getName());
        }
        sl.get(lastIndex).setIndex(index);
    }

    private String setFeatureXpath(
            AttributeMapping attMapping,
            final Expression sourceExpression,
            Pair parentAttribute,
            final String bracketIndex) {
        String xpath;

        if (attMapping.getInstanceXpath() == null) {
            xpath = parentAttribute.getXpath() + XPATH_SEPARATOR + sourceExpression.toString();
        } else {
            xpath =
                    parentAttribute.getXpath()
                            + XPATH_SEPARATOR
                            + attMapping.getInstanceXpath()
                            + bracketIndex
                            + XPATH_SEPARATOR
                            + sourceExpression.toString();
        }
        return xpath;
    }

    private void setPathIndex(Attribute att, StepList sl) {
        Object index = att.getUserData().get(ComplexFeatureConstants.MAPPED_ATTRIBUTE_INDEX);
        if (index != null) {
            int mappedIndex = Integer.parseInt(String.valueOf(index));
            // get prefixed attribute name
            Name attName = att.getName();
            String nsPrefix = namespaces.getPrefix(attName.getNamespaceURI());
            String xpath = nsPrefix + ":" + attName.getLocalPart();
            // set the index of the attribute in the attribute mapping steps
            sl.setIndex(mappedIndex, xpath, XPATH_SEPARATOR);
        }
    }

    protected boolean unprocessedFeatureExists() {
        if (indexCounter <= count) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean sourceFeatureIteratorHasNext() {
        return indexCounter <= count;
    }

    protected boolean isNextSourceFeatureNull() {
        return indexCounter > count;
    }

    private Object getValue(String xpathPrefix, Expression node, Attribute target) {
        final String EMPTY_STRING = "";
        String expressionString = node.toString();
        boolean isUnsetNode = Expression.NIL.equals(node) || expressionString.equals("''");

        if (isUnsetNode
                || expressionString.startsWith("'")
                || node instanceof LiteralExpressionImpl) {
            String editedValue = EMPTY_STRING;
            if (!isUnsetNode) {
                editedValue = expressionString.replace("'", "");
            }
            return editedValue;
        } else {
            if (node instanceof Function) {
                // special handling for functions
                XmlXpathFilterData data =
                        new XmlXpathFilterData(namespaces, xmlResponse.getDoc(), -1, xpathPrefix);
                return node.evaluate(data);
            } else if (xpathPrefix.length() > 0) {
                expressionString = xpathPrefix + XPATH_SEPARATOR + expressionString;
            }
        }
        return getValue(expressionString);
    }

    protected List<String> getValue(String expressionValue) {

        return XmlXpathUtilites.getXPathValues(
                mapping.getNamespaces(), expressionValue, xmlResponse.getDoc());
    }

    protected void closeSourceFeatures() {
        if (sourceFeatures != null) {
            xmlResponse = null;
            sourceFeatures = null;
        }
    }

    private void initialiseAttributeLists(List<AttributeMapping> mappings) {

        attOrderedTypeList =
                new AttributeCreateOrderList(
                        ((XmlFeatureTypeMapping) mapping).rootAttribute.getLabel());

        for (AttributeMapping attMapping : mappings) {
            if (attMapping.equals(((XmlFeatureTypeMapping) mapping).rootAttribute)) {
                // exclude root
                continue;
            }
            if (attMapping.getLabel() != null && attMapping.getParentLabel() != null) {
                attOrderedTypeList.put(attMapping);
            }
        }
    }

    /**
     * Return true if there are more features.
     *
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
        if (isHasNextCalled()) {
            return !isNextSourceFeatureNull();
        }

        boolean exists = false;

        if (featureCounter >= requestMaxFeatures) {
            return false;
        }
        if (isSourceFeatureIteratorNull()) {
            return false;
        }
        // make sure features are unique by mapped id
        exists = unprocessedFeatureExists();

        if (!exists) {
            LOGGER.finest("no more features, produced " + featureCounter);
            close();
        }

        setHasNextCalled(true);

        return exists;
    }

    @Override
    protected Feature computeNext() throws IOException {
        Feature f = populateFeatureData();
        super.cleanEmptyElements(f);
        return f;
    }
}
