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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.geotools.data.FeatureSource;
import org.geotools.data.complex.PathAttributeList.Pair;
import org.geotools.data.complex.filter.XPath.Step;
import org.geotools.data.complex.filter.XPath.StepList;
import org.geotools.filter.LiteralExpressionImpl;
import org.opengis.feature.Attribute;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.expression.Expression;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * @author Russell Petty, GSV Victoria
 * @version $Id$
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main
 *         /java/org/geotools/data/complex/FeatureTypeMapping.java $
 */
public class XmlFeatureTypeMapping extends FeatureTypeMapping {

    /**
     * Constants for manipulating XPath Expressions
     */
    private static final String XPATH_SEPARATOR = "/";

    private static final String XPATH_PROPERTY_SEPARATOR = "/@";

    private static final String XPATH_LEFT_INDEX_BRACKET = "[";

    private static final String XPATH_RIGHT_INDEX_BRACKET = "]";

    private Map<String, String> mapping = new HashMap<String, String>();

    private AttributeCreateOrderList attOrderedTypeList = null;

    private Map<String, TreeAttributeMapping> indexAttributeList;

    private TreeAttributeMapping rootAttribute;

    private int index = 1;

    private List<TreeAttributeMapping> setterAttributes = new ArrayList<TreeAttributeMapping>();

    /**
     * No parameters constructor for use by the digester configuration engine as a JavaBean
     */
    public XmlFeatureTypeMapping() {
        super(null, null, new LinkedList<AttributeMapping>(), new NamespaceSupport());
    }

    public XmlFeatureTypeMapping(FeatureSource source, AttributeDescriptor target,
            List<AttributeMapping> mappings, NamespaceSupport namespaces) {
        this(source, target, mappings, namespaces, null);
    }

    public XmlFeatureTypeMapping(FeatureSource source, AttributeDescriptor target,
            List<AttributeMapping> mappings, NamespaceSupport namespaces, String itemXpath) {
        super(source, target, mappings, namespaces, itemXpath);
        try {
            populateFeatureData();
        } catch (IOException ex) {
            throw new RuntimeException("Error occured when trying to create attribute mappings", ex);
        }
    }

    public List<String> getStringMappingsIgnoreIndex(final StepList targetPath) {
        List<String> mappings = new ArrayList<String>();
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
            Iterator<TreeAttributeMapping> leafAtts = setterAttributes.iterator();
            while (leafAtts.hasNext()) {
                TreeAttributeMapping att = leafAtts.next();
                String listPath = att.getTargetXPath().toString();
                String unindexedListPath = removeIndexFromPath(listPath);
                if (path.equals(unindexedListPath)) {
                    mappings.add(att.getSourceExpression().toString());
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
     * @param sourceExpression
     * @return list of matching attribute mappings
     */
    public List<AttributeMapping> getAttributeMappingsByExpression(final Expression sourceExpression) {
        AttributeMapping attMapping;
        List<AttributeMapping> mappings = Collections.emptyList();
        for (Iterator<AttributeMapping> it = attributeMappings.iterator(); it.hasNext();) {
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
     * Finds the attribute mapping for the target expression <code>exactPath</code>
     * 
     * @param exactPath
     *            the xpath expression on the target schema to find the mapping for
     * @return the attribute mapping that match 1:1 with <code>exactPath</code> or <code>null</code>
     *         if
     */
    public String getStringMapping(final StepList exactPath) {
        AttributeMapping attMapping;
        for (Iterator<AttributeMapping> it = attributeMappings.iterator(); it.hasNext();) {
            attMapping = (AttributeMapping) it.next();
            if (exactPath.equals(attMapping.getTargetXPath())) {
                return "";
            }
        }
        return null;
    }

    public void populateFeatureData() throws IOException {

        List<AttributeMapping> attMap = getAttributeMappings();
        if (attOrderedTypeList == null) {
            initialiseAttributeLists(attMap);
        }
        PathAttributeList elements = new PathAttributeList();
        elements.put(rootAttribute.getLabel(), itemXpath, null);
        String id = rootAttribute.getInstanceXpath() + XPATH_SEPARATOR
                + rootAttribute.getIdentifierExpression();
        mapping.put("@gml:id", id);
        mapping.put("@gsml:id", id);
        // create required elements

        // iterator returns the attribute mappings starting from the root of the tree.
        // parents are always returned before children elements.
        Iterator<TreeAttributeMapping> it = attOrderedTypeList.iterator();
        addComplexAttributes(elements, it);
        addSetterAttributes(elements);
        
        index++;
        removeAllRelativePaths();
    }

    private void addComplexAttributes(PathAttributeList elements, Iterator<TreeAttributeMapping> it) {
        while (it.hasNext()) {
            TreeAttributeMapping attMapping = it.next();
            final Expression sourceExpression = attMapping.getIdentifierExpression();

            List<Pair> ls = elements.get(attMapping.getParentLabel());
            if (ls != null) {
                for (int i = 0; i < ls.size(); i++) {
                    Pair parentAttribute = ls.get(i);
                    String instancePath = attMapping.getInstanceXpath();
                    int count = 1;
                    String countXpath = null;
                    // if instance path not set, then element exists, with one instance
                    if (instancePath != null) {
                        countXpath = parentAttribute.getXpath() + XPATH_SEPARATOR
                                + attMapping.getInstanceXpath();
                    }

                    for (int j = 0; j < count; j++) {
                        final String bracketIndex = "";
                        String xpath = null;
                        if (instancePath == null) {
                            xpath = parentAttribute.getXpath() + XPATH_SEPARATOR
                                    + sourceExpression.toString();
                        } else {
                            xpath = parentAttribute.getXpath() + XPATH_SEPARATOR
                                    + attMapping.getInstanceXpath() + bracketIndex
                                    + XPATH_SEPARATOR + sourceExpression.toString();
                        }
                        String label = getFullQueryPath(attMapping, attMapping
                                .getTargetQueryString());
                        String idSuffix = XPATH_PROPERTY_SEPARATOR + "gml:id";
                        mapping.put(label + idSuffix, xpath);

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
        for (TreeAttributeMapping attMapping : setterAttributes) {
            List<Pair> ls = elements.get(attMapping.getParentLabel());
            if (ls != null) {
                for (int i = 0; i < ls.size(); i++) {
                    Pair parentPair = ls.get(i);
                    final Expression sourceExpression = attMapping.getSourceExpression();

                    StringBuffer usedXpath = (StringBuffer) getValue(parentPair.getXpath(),
                            sourceExpression);
                    if (usedXpath != null) {
                        String label = getFullQueryPath(attMapping, attMapping.getTargetXPath()
                                .toString());
                        mapping.put(label, usedXpath.toString());
                        addClientProperties(attMapping, usedXpath, label);
                    } else {
                        usedXpath = new StringBuffer(parentPair.getXpath());
                        String label = getFullQueryPath(attMapping, attMapping.getTargetXPath()
                                .toString());
                        addClientProperties(attMapping, usedXpath, label);
                    }

                }
            }

        }
    }

    private void addClientProperties(TreeAttributeMapping attMapping, StringBuffer usedXpath,
            String label) {
        Map<Name, Expression> clientProperties = attMapping.getClientProperties();
        if (clientProperties.size() != 0) {

            for (Map.Entry<Name, Expression> entry : clientProperties.entrySet()) {
                Name propName = entry.getKey();
                Expression propExpr = entry.getValue();
                Object xPath = getValue(usedXpath.toString(), propExpr);
                if (xPath != null) {                                    
                    mapping.put(label + XPATH_PROPERTY_SEPARATOR  + getPropertyNameXpath(propName), 
                            xPath.toString());                                    
                }
            }
        }
    }

    private String getPropertyNameXpath(Name propName) {
        String xpath;
        String namespaceUri = propName.getNamespaceURI();                                    
        if (namespaceUri != null) {
            String namespace = namespaces.getPrefix(namespaceUri);
            xpath = namespace + propName.getSeparator()
                        + propName.getLocalPart();
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

    protected Object getValue(String xpathPrefix, Expression node) {
        StringBuffer usedXpath = new StringBuffer();
        String expressionValue = node.toString();
        if (expressionValue.startsWith("'") || node instanceof LiteralExpressionImpl) {
            usedXpath.append(xpathPrefix);
            return null;
        } else {
            expressionValue = xpathPrefix + XPATH_SEPARATOR + expressionValue;
        }
        usedXpath.append(expressionValue);
        return usedXpath;
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
        indexAttributeList = new HashMap<String, TreeAttributeMapping>();
        indexAttributeList.put(rootAttribute.getLabel(), rootAttribute);

        for (AttributeMapping attMapping : mappings) {
            if (attMapping.isTreeAttribute()) {
                TreeAttributeMapping treeAttMapping = (TreeAttributeMapping) attMapping;
                if (treeAttMapping.getLabel() == null) {
                    setterAttributes.add(treeAttMapping);
                } else if (treeAttMapping.getParentLabel() != null) {
                    attOrderedTypeList.put(treeAttMapping);
                    indexAttributeList.put(treeAttMapping.getLabel(), treeAttMapping);
                }
            }
        }
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
            Object propValue = null; // getValue(propExpr, source);
            if (propValue != null) {
                List<String> ls = (List<String>) propValue;
                if (ls.size() != 0) {
                    propValue = ls.get(0);
                } else {
                    propValue = "";
                }
            }
            targetAttributes.put(propName, propValue);
        }
    }

    private String getFullQueryPath(TreeAttributeMapping attMapping, String initialString) {
        StringBuffer name = new StringBuffer(initialString);
        TreeAttributeMapping tam = attMapping;
        while (tam.getParentLabel() != null) {
            tam = indexAttributeList.get(tam.getParentLabel());
            if (!rootAttribute.equals(tam)) {
                name.insert(0, tam.getTargetQueryString() + XPATH_SEPARATOR);
            }
        }
        return name.toString();
    }

    private void removeAllRelativePaths() {

        Collection<String> c = mapping.keySet();
        Iterator<String> itr = c.iterator();

        while (itr.hasNext()) {
            String key = itr.next();
            String xPath = mapping.get(key);
            String xPath2 = removeRelativePaths(xPath);
            if (!xPath.equals(xPath2)) {
                mapping.put(key, xPath2);
            }
        }
    }

    private String removeRelativePaths(String xPath) {
        String xPathTemp = xPath;
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
        return xPathTemp;
    }
}