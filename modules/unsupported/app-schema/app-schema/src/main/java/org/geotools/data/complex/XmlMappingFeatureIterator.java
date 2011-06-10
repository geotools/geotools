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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.jxpath.JXPathException;
import org.geotools.data.Query;
import org.geotools.data.complex.PathAttributeList.Pair;
import org.geotools.data.complex.filter.XPath.StepList;
import org.geotools.data.complex.xml.XmlFeatureCollection;
import org.geotools.data.complex.xml.XmlResponse;
import org.geotools.feature.AttributeBuilder;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.util.Converters;
import org.geotools.util.XmlXpathUtilites;
import org.opengis.feature.Attribute;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.expression.Expression;
import org.xml.sax.Attributes;

/**
 * An implementation of AbstractMappingFeatureIterator to handle XML datasources.
 * 
 * @author Russell Petty (GeoScience Victoria)
 * @version $Id$
 *
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main
 *         /java/org/geotools/data/complex/AppSchemaDataAccess.java $
 */
public class XmlMappingFeatureIterator extends DataAccessMappingFeatureIterator {

    /**
     * Constants for manipulating XPath Expressions
     */
    private static final String XPATH_SEPARATOR = "/";

    private static final String XPATH_LEFT_INDEX_BRACKET = "[";

    private static final String XPATH_RIGHT_INDEX_BRACKET = "]";

    protected XmlResponse xmlResponse;

    private AttributeCreateOrderList attOrderedTypeList = null;
    
    private int count = 0;

    private int indexCounter = 1;

    private String idXpath;
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
        super(store, mapping, query, false);

        idXpath = mapping.getFeatureIdExpression().equals(Expression.NIL) ? "@id" : mapping
				.getFeatureIdExpression().toString();
        
        if (xmlResponse == null) {
            this.xmlResponse = ((XmlFeatureCollection) sourceFeatures).xmlResponse();
        }

        List<Integer> ls = xmlResponse.getValidFeatureIndex();
        count = ls.size();
    }

    public XmlMappingFeatureIterator(AppSchemaDataAccess store, FeatureTypeMapping mapping,
            Query query, String xpath, String value) throws IOException {
        super(store, mapping, query, false);

        idXpath = mapping.getFeatureIdExpression().equals(Expression.NIL) ? "@id" : mapping
                                .getFeatureIdExpression().toString();

        if (xmlResponse == null) {
            this.xmlResponse = ((XmlFeatureCollection) sourceFeatures).xmlResponse(xpath, value);
        }

        List<Integer> ls = xmlResponse.getValidFeatureIndex();
        count = ls.size();
    }

    protected Iterator<SimpleFeature> getSourceFeatureIterator() {
        return null;
    }

    protected boolean isSourceFeatureIteratorNull() {
        return xmlResponse == null;
    }

    protected String extractIdForFeature() {
        try {
            return XmlXpathUtilites.getSingleXPathValue(mapping.getNamespaces(),
                createIndexedItemXpathString() + XPATH_SEPARATOR + idXpath, xmlResponse.getDoc());
        } catch (RuntimeException e) {
            if (e.getCause() instanceof JXPathException) {
                // only log info since id is not always compulsory
                LOGGER.info("Feature id is not mapped for: " + mapping.getTargetFeature().getName());                
            } else {
                throw e;
            }            
        }
        return null;
    }

    private String createIndexedItemXpathString() {
        String rootXpath = ((XmlFeatureTypeMapping)mapping).itemXpath + XPATH_LEFT_INDEX_BRACKET
                + xmlResponse.getValidFeatureIndex().get(indexCounter - 1)
                + XPATH_RIGHT_INDEX_BRACKET;
        
        if (((XmlFeatureTypeMapping) mapping).rootAttribute.getInstanceXpath() != null) {
            rootXpath += XPATH_SEPARATOR
                    + ((XmlFeatureTypeMapping) mapping).rootAttribute.getInstanceXpath();
        }
        
        return rootXpath;
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
        setAttributeValues(elements, target);
        indexCounter++;
        return target;
    }

    private void setAttributeValues(PathAttributeList elements, Feature target) throws IOException {
        for (AttributeMapping attMapping : ((XmlFeatureTypeMapping)mapping).setterAttributes) {           
            List<Pair> ls = elements.get(attMapping.getParentLabel());
            if (ls != null) {
                final Expression sourceExpression = attMapping.getSourceExpression();
                
                for (int i = 0; i < ls.size(); i++) {
                    Pair parentPair = ls.get(i);
                    Attribute setterTarget = parentPair.getAttribute();

                    List<String> values = getValue(parentPair.getXpath(), sourceExpression,
                            setterTarget);
                    for (int j = 0; j < values.size(); j++) {
                        String value = values.get(j);
                        if (LOGGER.isLoggable(Level.FINER)) {
                            LOGGER.finer("setting target=" + setterTarget.getName()
                                    + ", targetXpath=" + attMapping.getTargetXPath() + ", value="
                                    + value);
                        }
                        
                        // find setter target in the xpath and set the new index
                        StepList xpath = attMapping.getTargetXPath().clone();
                        if (ls.size() > 1) {
                            xpath.get(0).setIndex(i + 1);
                        }
                                    
                        Attribute att = setAttributeValue(target, null, attMapping, value, xpath, null);
                        setClientProperties(att, parentPair.getXpath(), attMapping.getClientProperties());
                    }
                }
            }
        }
    }
    
    @Override
    protected void setClientProperties(final Attribute target, final Object xpathPrefix,
            final Map<Name, Expression> clientProperties) {
        if (target == null) {
            return;
        }

        // NC - first calculate target attributes
        final Map<Name, Object> targetAttributes = new HashMap<Name, Object>();
        if (target.getUserData().containsValue(Attributes.class)) {
            targetAttributes.putAll((Map<? extends Name, ? extends Object>) target.getUserData()
                    .get(Attributes.class));
        }        
        for (Map.Entry<Name, Expression> entry : clientProperties.entrySet()) {
            Name propName = entry.getKey();
            Object propExpr = entry.getValue();
            Object propValue;
            if (propExpr instanceof Expression) {
                propValue = getValue(String.valueOf(xpathPrefix), (Expression)propExpr, target);
            } else {
                propValue = propExpr;
            }
            if (propValue != null) {
                if (propValue instanceof Collection) {
                    if (!((Collection)propValue).isEmpty()) {                
                        propValue = ((Collection)propValue).iterator().next();
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
        String rootPrefix = createIndexedItemXpathString();
        elements.put(((XmlFeatureTypeMapping)mapping).rootAttribute.getLabel(), rootPrefix, target);
        
        setClientProperties(target, rootPrefix, ((XmlFeatureTypeMapping)mapping).rootAttribute.getClientProperties());

        // iterator returns the attribute mappings starting from the root of the tree.
        // parents are always returned before children elements.
        Iterator<AttributeMapping> it = attOrderedTypeList.iterator();
        while (it.hasNext()) {
            AttributeMapping attMapping = it.next();
            final Expression sourceExpression = attMapping.getIdentifierExpression();

            List<Pair> ls = elements.get(attMapping.getParentLabel());
            if (ls != null) {
                for (int i = 0; i < ls.size(); i++) {
                    Pair parentAttribute = ls.get(i);
                    String countXpath = parentAttribute.getXpath();
                    // if instance path not set, then only count the root node
                    if (attMapping.getInstanceXpath() != null) {
                        countXpath += XPATH_SEPARATOR + attMapping.getInstanceXpath();                        
                    }
                    int count = XmlXpathUtilites.countXPathNodes(mapping.getNamespaces(),
                            countXpath, xmlResponse.getDoc());
                    
                    createSubFeaturesAndAddToAttributeList(elements, attMapping, sourceExpression,
                            parentAttribute, count, countXpath);
                }
            }
        }
        return elements;
    }

    private void createSubFeaturesAndAddToAttributeList(PathAttributeList elements,
            AttributeMapping attMapping, final Expression sourceExpression,
            Pair parentAttribute, int count, String countXpath) throws IOException {

        for (int j = 1; j <= count; j++) {
            final String bracketIndex = bracketedIndex(j);
            final String idPath = setFeatureXpath(attMapping, sourceExpression, parentAttribute,
                    bracketIndex);
            List<String> featureIdList = getValue(idPath);
            String featureId = null;
            if (!featureIdList.isEmpty()) {
                featureId = featureIdList.get(0);
            }
            StepList sl = attMapping.getTargetXPath().clone();
            setPathIndex(j, sl);
//            Attribute subFeature = setAttributeValue(parentAttribute.getAttribute(), featureId, null, attMapping, null, sl);
            Attribute subFeature = xpathAttributeBuilder.set(parentAttribute.getAttribute(), sl,
                    null, featureId, attMapping.getTargetNodeInstance(), false, attMapping
                            .getSourceExpression());               
            String xpath = countXpath + bracketIndex;
            setClientProperties(subFeature, xpath, attMapping.getClientProperties());
            elements.put(attMapping.getLabel(), xpath, subFeature);
        }
    }

    private String setFeatureXpath(AttributeMapping attMapping,
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
        if (j > 1) {
            sl.get(0).setIndex(j);
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

    protected List<String> getValue(Expression expression, String xpathPrefix, Attribute target) {
        return getValue(xpathPrefix, expression, target);
    }

    private List<String> getValue(String xpathPrefix, Expression node, Attribute target) {
        final String EMPTY_STRING = "";
        String expressionValue = node.toString();
        boolean isUnsetNode = Expression.NIL.equals(node) || expressionValue.equals("''");

        if (isUnsetNode || expressionValue.startsWith("'") || node instanceof LiteralExpressionImpl) {
            String editedValue = EMPTY_STRING;
            List<String> ls = new ArrayList<String>(1);
            if (!isUnsetNode) {
                editedValue = expressionValue.replace("'", "");
            }
            ls.add(editedValue);
            return ls;
        } else if (node instanceof FunctionExpressionImpl) {
            // special handling for functions
            List<String> ls = new ArrayList<String>(1);
            Object value = node.evaluate(target);
            if (value != null) {
                ls.add(Converters.convert(value, String.class));
            }
            return ls;
        } else {
            expressionValue = xpathPrefix + XPATH_SEPARATOR + expressionValue;
        }
        return getValue(expressionValue);
    }

    protected List<String> getValue(String expressionValue) {

        return XmlXpathUtilites.getXPathValues(mapping.getNamespaces(), expressionValue,
                xmlResponse.getDoc());
    }

    protected void closeSourceFeatures() {
        if (sourceFeatures != null) {
            xmlResponse = null;
            sourceFeatures = null;
        }
    }

    private void initialiseAttributeLists(List<AttributeMapping> mappings) {
        
        attOrderedTypeList = new AttributeCreateOrderList(
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
        
        if (featureCounter >= maxFeatures) {
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

    protected Feature computeNext() throws IOException {
        if (!isHasNextCalled()) {
            // hasNext needs to be called to set nextSrcFeature
            if (!hasNext()) {
                return null;
            }
        }
        setHasNextCalled(false);
        if (isNextSourceFeatureNull()) {
            throw new UnsupportedOperationException("No more features produced!");
        }

        String id = extractIdForFeature();
        return populateFeatureData(id);
    }
}
