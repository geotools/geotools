/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2009, Open Source Geospatial Foundation (OSGeo)
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.complex.PathAttributeList.Pair;
import org.geotools.data.complex.filter.XPath.Step;
import org.geotools.data.complex.filter.XPath.StepList;
import org.geotools.data.complex.xml.XmlFeatureCollection;
import org.geotools.data.complex.xml.XmlFeatureSource;
import org.geotools.data.complex.xml.XmlResponse;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.AttributeBuilder;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.util.XmlXpathUtilites;
import org.jdom.Element;
import org.opengis.feature.Attribute;
import org.opengis.feature.Feature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;
import org.opengis.filter.expression.Expression;
import org.xml.sax.Attributes;

/**
 * An implementation of AbstractMappingFeatureIterator to handle XML datasources.
 * 
 * @author Russell Petty, GSV
 * @version $Id$
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main
 *         /java/org/geotools/data/complex/AppSchemaDataAccess.java $
 */
public class XmlMappingFeatureIterator extends AbstractMappingFeatureIterator {

    /**
     * Constants for manipulating XPath Expressions
     */
    private static final String XPATH_SEPARATOR = "/";

    private static final String XPATH_LEFT_INDEX_BRACKET = "[";

    private static final String XPATH_RIGHT_INDEX_BRACKET = "]";

    protected XmlResponse xmlResponse;

    private List<Element> sources;

    @SuppressWarnings("unchecked")
    private FeatureSource mappedSource;

    private SimpleFeatureCollection sourceFeatures;

    private AttributeCreateOrderList attOrderedTypeList = null;

    private List<TreeAttributeMapping> setterAttributes = new ArrayList<TreeAttributeMapping>();

    private TreeAttributeMapping rootAttribute;

    private int count = 0;

    private int indexCounter = 1;

    private String idXpath;

    private static Query cachedQuery = null;

    protected static XmlResponse cachedXmlResponse = null;

    /**
     * 
     * @param store
     * @param mapping
     *            place holder for the target type, the surrogate FeatureSource and the mappings
     *            between them.
     * @param query
     *            the query over the target feature type, that is to be unpacked to its equivalent
     *            over the surrogate feature type.
     * @throws IOException
     */
    public XmlMappingFeatureIterator(AppSchemaDataAccess store, FeatureTypeMapping mapping,
            Query query) throws IOException {
        super(store, mapping, query);

        idXpath = mapping.getFeatureIdExpression().equals(Expression.NIL) ? "@id" : mapping
				.getFeatureIdExpression().toString();

        List<Integer> ls = xmlResponse.getValidFeatureIndex();
        count = ls.size();
    }

    protected Iterator<Feature> getSourceFeatureIterator() {
        return null;
    }

    protected boolean isSourceFeatureIteratorNull() {
        return xmlResponse == null;
    }

    protected void setSourceFeatureIterator(Iterator<Element> xmlSourceFeatureIterator) {
    }

    protected void initialiseSourceFeatures(FeatureTypeMapping mapping, Query query)
            throws IOException {
        mappedSource = mapping.getSource();

        // check if the previous query was the same as this one. If not retrieve the new data.
        // Otherwise use the same data. We want to do this once only--Geoserver does a count
        // operation, before getting the data. This results in two identical queries. We are simply
        // trying to save on the second query here. We don't want to cache beyond this.
        if (cachedQuery == null || !cachedQuery.equals(query)) {
            sourceFeatures = (SimpleFeatureCollection) mappedSource.getFeatures(query);

            XmlFeatureSource xmlFeatureSource = (XmlFeatureSource) mappedSource;
            xmlFeatureSource.setNamespaces(mapping.getNamespaces());
            xmlFeatureSource.setItemXpath(mapping.getItemXpath());

            this.xmlResponse = ((XmlFeatureCollection) sourceFeatures).xmlResponse();
            XmlMappingFeatureIterator.cachedQuery = query;
            XmlMappingFeatureIterator.cachedXmlResponse = xmlResponse;
        } else {
            this.xmlResponse = cachedXmlResponse;
            XmlMappingFeatureIterator.cachedXmlResponse = null;
            XmlMappingFeatureIterator.cachedQuery = null;
        }
    }

    protected String extractIdForFeature() {
        return XmlXpathUtilites.getSingleXPathValue(mapping.getNamespaces(),
                createIndexedItemXpathString() + XPATH_SEPARATOR + idXpath, xmlResponse.getDoc());
    }

    private String createIndexedItemXpathString() {
        return mapping.itemXpath + XPATH_LEFT_INDEX_BRACKET
                + xmlResponse.getValidFeatureIndex().get(indexCounter - 1)
                + XPATH_RIGHT_INDEX_BRACKET;
    }

    protected String extractIdForAttribute(final Expression idExpression, Object sourceInstance) {
        String value = (String) idExpression.evaluate(sourceInstance, String.class);
        return value;
    }

    protected Feature populateFeatureData(String id) throws IOException {

        final AttributeDescriptor targetNode = mapping.getTargetFeature();
        AttributeBuilder builder = new AttributeBuilder(attf);
        builder.setDescriptor(targetNode);
        Feature target = (Feature) builder.build(id);

        if (attOrderedTypeList == null) {
            initialiseAttributeLists(mapping.getAttributeMappings());
        }

        // create required elements
        PathAttributeList elements = populateAttributeList(target);
        setAttributeValues(elements);
        indexCounter++;
        return target;
    }

    private void setAttributeValues(PathAttributeList elements) {
        for (TreeAttributeMapping attMapping : setterAttributes) {
            final Expression sourceExpression = attMapping.getSourceExpression();

            List<Pair> ls = elements.get(attMapping.getParentLabel());
            if (ls != null) {
                for (int i = 0; i < ls.size(); i++) {
                    Pair parentPair = ls.get(i);
                    Attribute setterTarget = parentPair.getAttribute();

                    StringBuffer usedXpath = new StringBuffer();
                    List<String> values = getValue(parentPair.getXpath(), sourceExpression,
                            usedXpath);
                    setValues(attMapping, setterTarget, usedXpath, values);
                }
            }
        }
    }

    private void setValues(TreeAttributeMapping attMapping, Attribute setterTarget,
            StringBuffer usedXpath, List<String> values) {
        for (int j = 0; j < values.size(); j++) {
            String value = values.get(j);

            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("setting target=" + setterTarget.getName() + ", targetXpath="
                        + attMapping.getTargetXPath() + ", value=" + value);
            }
            Attribute subFeature = xpathAttributeBuilder.set(setterTarget, attMapping
                    .getTargetXPath(), value, null, null, false, attMapping.getSourceExpression());
            setClientProperties(subFeature, j == 0 ? usedXpath : usedXpath
                    .append(bracketedIndex(j)), attMapping.getClientProperties());
        }
    }

    private String bracketedIndex(int j) {
        return XPATH_LEFT_INDEX_BRACKET + Integer.toString(j + 1) + XPATH_RIGHT_INDEX_BRACKET;
    }

    private PathAttributeList populateAttributeList(Feature target) {
        PathAttributeList elements = new PathAttributeList();
        elements.put(rootAttribute.getLabel(), createIndexedItemXpathString(), target);

        // iterator returns the attribute mappings starting from the root of the tree.
        // parents are always returned before children elements.
        Iterator<TreeAttributeMapping> it = attOrderedTypeList.iterator();
        while (it.hasNext()) {
            TreeAttributeMapping attMapping = it.next();
            final Expression sourceExpression = attMapping.getIdentifierExpression();

            List<Pair> ls = elements.get(attMapping.getParentLabel());
            if (ls != null) {
                for (int i = 0; i < ls.size(); i++) {
                    Pair parentAttribute = ls.get(i);
                    int count = 1;
                    String countXpath = null;
                    // if instance path not set, then element exists, with one instance
                    if (attMapping.getInstanceXpath() != null) {
                        countXpath = parentAttribute.getXpath() + XPATH_SEPARATOR
                                + attMapping.getInstanceXpath();
                        count = XmlXpathUtilites.countXPathNodes(mapping.getNamespaces(),
                                countXpath, xmlResponse.getDoc());
                    }
                    createSubFeaturesAndAddToAttributeList(elements, attMapping, sourceExpression,
                            parentAttribute, count, countXpath);
                }
            }
        }
        return elements;
    }

    private void createSubFeaturesAndAddToAttributeList(PathAttributeList elements,
            TreeAttributeMapping attMapping, final Expression sourceExpression,
            Pair parentAttribute, int count, String countXpath) {

        for (int j = 0; j < count; j++) {
            final String bracketIndex = bracketedIndex(j);
            final String xpath = setFeatureXpath(attMapping, sourceExpression, parentAttribute,
                    bracketIndex);
            List<String> featureIdList = getValue(xpath);
            String featureId = null;
            if (!featureIdList.isEmpty()) {
                featureId = featureIdList.get(0);
            }
            StepList sl = attMapping.getTargetXPath();
            setPathIndex(j, sl);
            Attribute subFeature = xpathAttributeBuilder.set(parentAttribute.getAttribute(), sl,
                    null, featureId, attMapping.getTargetNodeInstance(), false, attMapping
                            .getSourceExpression());
            elements.put(attMapping.getLabel(), countXpath + bracketIndex, subFeature);
        }
    }

    private String setFeatureXpath(TreeAttributeMapping attMapping,
            final Expression sourceExpression, Pair parentAttribute, final String bracketIndex) {
        String xpath;

        if (attMapping.getInstanceXpath() == null) {
            xpath = parentAttribute.getXpath() + XPATH_SEPARATOR + sourceExpression.toString();
        } else {
            xpath = parentAttribute.getXpath() + XPATH_SEPARATOR + attMapping.getInstanceXpath()
                    + bracketIndex + XPATH_SEPARATOR + sourceExpression.toString();
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

    protected List<String> getValue(Expression expression, Object data) {
        return getValue(((StringBuffer) data).toString(), expression, new StringBuffer());
    }

    protected List<String> getValue(String xpathPrefix, Expression node, StringBuffer usedXpath) {
        final String EMPTY_STRING = "";
        String expressionValue = node.toString();
        boolean isUnsetNode = Expression.NIL.equals(node) || expressionValue.equals("''");

        if (isUnsetNode || expressionValue.startsWith("'") || node instanceof LiteralExpressionImpl) {
            usedXpath.append(xpathPrefix);
            String editedValue = EMPTY_STRING;
            List<String> ls = new ArrayList<String>(1);
            if (!isUnsetNode) {
                editedValue = expressionValue.replace("'", "");
            }
            ls.add(editedValue);
            return ls;
        } else {
            expressionValue = xpathPrefix + XPATH_SEPARATOR + expressionValue;
        }
        usedXpath.append(expressionValue);
        return getValue(expressionValue);
    }

    protected List<String> getValue(String expressionValue) {

        return XmlXpathUtilites.getXPathValues(mapping.getNamespaces(), expressionValue,
                xmlResponse.getDoc());
    }

    protected void setAttributeValueFromSources(Feature target, AttributeMapping attMapping)
            throws IOException {
        for (Element source : sources) {
            setAttributeValue(target, source, attMapping);
        }
    }

    protected void closeSourceFeatures() {
        if (sourceFeatures != null) {
            xmlResponse = null;
            sourceFeatures = null;
        }
    }

    private void initialiseAttributeLists(List<AttributeMapping> mappings) {

        for (AttributeMapping attMapping : mappings) {
            if (attMapping.isTreeAttribute()) {
                TreeAttributeMapping treeAttMapping = (TreeAttributeMapping) attMapping;
                if (treeAttMapping.getLabel() != null && treeAttMapping.getParentLabel() == null
                        && attMapping.getTargetNodeInstance() == null) {

                    rootAttribute = treeAttMapping;
                    break;
                }
            }
        }

        attOrderedTypeList = new AttributeCreateOrderList(rootAttribute.getLabel());

        for (AttributeMapping attMapping : mappings) {
            if (attMapping.isTreeAttribute()) {
                TreeAttributeMapping treeAttMapping = (TreeAttributeMapping) attMapping;
                if (treeAttMapping.getLabel() == null) {
                    setterAttributes.add(treeAttMapping);
                } else if (treeAttMapping.getParentLabel() != null) {
                    attOrderedTypeList.put(treeAttMapping);
                }
            }
        }
    }

    protected void setAttributeValue(Feature target, final Feature source, StepList xpath, String id)
            throws IOException {

        xpathAttributeBuilder.set(target, xpath, source, id, source.getType(), false, null);
    }

    /**
     * Sets the values of grouping attributes.
     * 
     * @param sourceFeature
     * @param groupingMappings
     * @param targetFeature
     * 
     * @return Feature. Target feature sets with simple attributes
     */
    protected void setAttributeValue(Feature target, final Object source,
            final AttributeMapping attMapping) throws IOException {

        final Expression sourceExpression = attMapping.getSourceExpression();
        Object value = getValue(sourceExpression, source);

        String id = null;
        if (Expression.NIL != attMapping.getIdentifierExpression()) {
            id = extractIdForAttribute(attMapping.getIdentifierExpression(), source);
        }

        final AttributeType targetNodeType = attMapping.getTargetNodeInstance();
        final StepList xpath = attMapping.getTargetXPath();

        Attribute instance = xpathAttributeBuilder.set(target, xpath, value, id, targetNodeType,
                attMapping.isMultiValued(), null, attMapping.getSourceExpression());
        Map<Name, Expression> clientPropsMappings = attMapping.getClientProperties();
        setClientProperties(instance, source, clientPropsMappings);
    }

    protected void setClientProperties(final Attribute target, final Object source,
            final Map<Name, Expression> clientProperties) {
        if (clientProperties.size() == 0) {
            return;
        }
        final Map<Name, Object> targetAttributes = new HashMap<Name, Object>();
        for (Map.Entry<Name, Expression> entry : clientProperties.entrySet()) {
            Name propName = entry.getKey();
            Expression propExpr = entry.getValue();
            String propValue = "";

            List<String> ls = getValue(propExpr, source);
            if (!ls.isEmpty()) {
                propValue = ls.get(0);
            }

            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("setting target=" + target.getName() + ", property Name=" + propName
                        + ", value=" + propValue);
            }
            targetAttributes.put(propName, propValue);
        }
        // FIXME should set a child Property
        target.getUserData().put(Attributes.class, targetAttributes);
    }
}
