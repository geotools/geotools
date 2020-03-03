/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.geotools.data.complex.AttributeMapping;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.NestedAttributeMapping;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator.ComplexNameImpl;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.data.complex.util.XPathUtil.Step;
import org.geotools.data.complex.util.XPathUtil.StepList;
import org.geotools.data.joining.JoiningNestedAttributeMapping;
import org.geotools.filter.visitor.DefaultExpressionVisitor;
import org.geotools.util.logging.Logging;
import org.geotools.xlink.XLINK;
import org.opengis.feature.Feature;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Expression visitor that uses the attribute and mapping information provided by a {@link
 * FeatureTypeMapping} object to determine which nested feature types / attributes must be traversed
 * to reach the attribute identified by the provided {@link PropertyName} expression.
 *
 * <p>The provided {@link FeatureTypeMapping} object is regarded as the root mapping against which
 * the expression is evaluated.
 *
 * <p>The nested attribute mappings are returned as a list of {@link FeatureChainLink} objects; the
 * first one in the list always refers to the root mapping.
 *
 * @author Stefano Costa, GeoSolutions
 */
public class FeatureChainedAttributeVisitor extends DefaultExpressionVisitor {

    private static final Logger LOGGER = Logging.getLogger(FeatureChainedAttributeVisitor.class);

    private FeatureTypeMapping rootMapping;

    private List<FeatureChainedAttributeDescriptor> attributes;

    private boolean conditionalMappingFound;
    private boolean unboundedNestedElementFound = false;

    public FeatureChainedAttributeVisitor(FeatureTypeMapping root) {
        if (root == null) {
            throw new NullPointerException("root mapping is null");
        }
        this.attributes = new ArrayList<>();
        this.rootMapping = root;
        this.conditionalMappingFound = false;
    }

    @Override
    public Object visit(PropertyName expression, Object data) {
        if (expression == null) {
            throw new NullPointerException("expression is null");
        }
        Feature feature = null;
        if (data != null && !(data instanceof Feature)) {
            feature = (Feature) data;
        }

        // reset outcome of the visit
        attributes = new ArrayList<>();

        try {
            walkXPath(expression.getPropertyName(), feature);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Exception occurred splitting XPath expression into mapping steps", e);
        }

        return getFeatureChainedAttributes();
    }

    void walkXPath(String xpath, Feature feature) throws IOException {
        FeatureTypeMapping currentType = rootMapping;
        StepList currentXPath =
                XPath.steps(rootMapping.getTargetFeature(), xpath, rootMapping.getNamespaces());
        FeatureChainedAttributeDescriptor attrDescr = new FeatureChainedAttributeDescriptor();
        walkXPathRecursive(currentXPath, currentType, attrDescr, feature);
    }

    private void walkXPathRecursive(
            StepList currentXPath,
            FeatureTypeMapping currentType,
            FeatureChainedAttributeDescriptor attrDescr,
            Feature feature)
            throws IOException {
        List<NestedAttributeMapping> currentAttributes = currentType.getNestedMappings();
        boolean searchIsOver = true;

        checkUnboundedElement(currentXPath, currentType);

        for (NestedAttributeMapping nestedAttr : currentAttributes) {
            StepList targetXPath = nestedAttr.getTargetXPath();

            if (startsWith(currentXPath, targetXPath)) {
                if (nestedAttr.isConditional() && feature == null) {
                    logConditionalMappingFound(currentType, targetXPath);
                    // quit the search
                    return;
                } else {
                    FeatureTypeMapping nestedType = nestedAttr.getFeatureTypeMapping(feature);
                    if (nestedType != null) {
                        AttributeType nestedPropertyType = nestedType.getTargetFeature().getType();
                        QName nestedTypeQName = getFeatureTypeQName(nestedType);
                        Step nestedTypeStep = new Step(nestedTypeQName, 1);
                        StepList nestedTypeXPath = targetXPath.clone();
                        nestedTypeXPath.add(nestedTypeStep);

                        boolean xpathContainsNestedType = startsWith(currentXPath, nestedTypeXPath);
                        boolean hasSimpleContent = Types.isSimpleContentType(nestedPropertyType);

                        // if this is feature chaining for simple content, the name of the nested
                        // type
                        // may not be present in the XPath, as it was already specified as the
                        // container
                        // property (e.g. see mappings doing chaining for gml:name)
                        if (xpathContainsNestedType || hasSimpleContent) {
                            LOGGER.finer("Nested feature type found: " + nestedTypeQName);
                            FeatureChainedAttributeDescriptor copy = attrDescr.shallowCopy();
                            copy.addLink(new FeatureChainLink(currentType, nestedAttr));

                            // new root mapping to search
                            FeatureTypeMapping newType = nestedType;

                            // new xpath
                            StepList newXPath = currentXPath.clone();
                            int startIdx =
                                    (xpathContainsNestedType)
                                            ? nestedTypeXPath.size()
                                            : currentXPath.size();
                            newXPath = newXPath.subList(startIdx, currentXPath.size());

                            // if nested type has simple content, XPath expression may point
                            // directly
                            // to the type, and not to one of its attributes (which, BTW, can only
                            // be client properties, or it wouldn't be simple content)
                            if (newXPath.isEmpty() && hasSimpleContent) {
                                newXPath.add(nestedTypeStep);
                            }

                            // recursive call
                            walkXPathRecursive(newXPath, newType, copy, feature);
                            // I'm not done yet
                            searchIsOver = false;
                        }
                    } else {
                        logNestedFeatureTypeNotFound(currentType, targetXPath);
                    }
                }
            }
        }

        // add last attribute mapping, which is a direct child of the last nested feature found
        if (searchIsOver && currentXPath != null && !currentXPath.isEmpty()) {
            StepList lastAttrPath = currentXPath;
            List<Expression> lastAttrExpressions = currentType.findMappingsFor(lastAttrPath, false);
            if (lastAttrExpressions != null && lastAttrExpressions.size() > 0) {
                attrDescr.setAttributePath(lastAttrPath);

                // check whether this is a case of feature chaining by reference
                if (isClientProperty(lastAttrPath) && isXlinkHref(lastAttrPath)) {
                    StepList parentAttrPath = lastAttrPath.subList(0, lastAttrPath.size() - 1);
                    AttributeMapping parentAttr = currentType.getAttributeMapping(parentAttrPath);
                    if (parentAttr != null && parentAttr instanceof NestedAttributeMapping) {
                        // yes, it's feature chaining by reference: add another step to the chain
                        NestedAttributeMapping nestedAttr = (NestedAttributeMapping) parentAttr;
                        attrDescr.addLink(new FeatureChainLink(currentType, nestedAttr));
                        // add last step
                        if (nestedAttr.isConditional() && feature == null) {
                            logConditionalMappingFound(currentType, nestedAttr.getTargetXPath());
                            // abort search
                            return;
                        } else {
                            FeatureTypeMapping nestedType =
                                    nestedAttr.getFeatureTypeMapping(feature);
                            if (nestedType != null) {
                                FeatureChainLink lastLink = new FeatureChainLink(nestedType, true);
                                attrDescr.addLink(lastLink);
                                // search was successful, add attribute to collection
                                attributes.add(attrDescr);
                            } else {
                                logNestedFeatureTypeNotFound(
                                        currentType, nestedAttr.getTargetXPath());
                            }
                        }
                    }
                } else {
                    attrDescr.addLink(new FeatureChainLink(currentType));
                    // search was successful, add attribute to collection
                    attributes.add(attrDescr);
                }
            }
        }
    }

    private void checkUnboundedElement(StepList currentXPath, FeatureTypeMapping currentType) {
        for (AttributeMapping attributeMapping : currentType.getAttributeMappings()) {
            for (Entry<Name, Expression> entry :
                    attributeMapping.getClientProperties().entrySet()) {
                if (entry.getKey() instanceof ComplexNameImpl) {
                    final ComplexNameImpl complexQname = (ComplexNameImpl) entry.getKey();
                    final StepList unboundedElementStepList =
                            attributeMapping.getTargetXPath().clone();
                    Step step =
                            new Step(
                                    new QName(
                                            complexQname.getNamespaceURI(),
                                            complexQname.getLocalPart(),
                                            currentType
                                                    .getNamespaces()
                                                    .getPrefix(complexQname.getNamespaceURI())),
                                    unboundedElementStepList.size() + 1);
                    unboundedElementStepList.add(step);
                    if (currentXPath.equalsIgnoreIndex(unboundedElementStepList)) {
                        this.unboundedNestedElementFound = true;
                    }
                }
            }
        }
    }

    private void logConditionalMappingFound(FeatureTypeMapping containerType, StepList xpath) {
        conditionalMappingFound = true;
        if (LOGGER.isLoggable(Level.FINE)) {
            QName qname = getFeatureTypeQName(containerType);
            String prefixedName = qname.getPrefix() + ":" + qname.getLocalPart();
            LOGGER.fine(
                    "Conditional nested mapping found, but no feature to evaluate "
                            + "against was provided: nested feature type cannot be determined for "
                            + "container type \""
                            + prefixedName
                            + "\" and target attribute \""
                            + xpath);
        }
    }

    public boolean isUnboundedNestedElementFound() {
        return unboundedNestedElementFound;
    }

    private void logNestedFeatureTypeNotFound(FeatureTypeMapping containerType, StepList xpath) {
        if (LOGGER.isLoggable(Level.FINE)) {
            QName qname = getFeatureTypeQName(containerType);
            String prefixedName = qname.getPrefix() + ":" + qname.getLocalPart();
            LOGGER.fine(
                    "Nested type could not be determined for container type \""
                            + prefixedName
                            + "\" and target attribute \""
                            + xpath);
        }
    }

    private QName getFeatureTypeQName(FeatureTypeMapping featureTypeMapping) {
        NamespaceSupport nsSupport = featureTypeMapping.getNamespaces();
        Name featureTypeName = featureTypeMapping.getTargetFeature().getName();
        String uri = featureTypeName.getNamespaceURI();
        String localPart = featureTypeName.getLocalPart();
        String prefix = nsSupport.getPrefix(uri);

        return new QName(uri, localPart, prefix);
    }

    static boolean isClientProperty(StepList steps) {
        if (steps.isEmpty()) {
            return false;
        }
        return steps.get(steps.size() - 1).isXmlAttribute();
    }

    static boolean isXlinkHref(StepList steps) {
        if (steps.isEmpty()) {
            return false;
        }
        // special case for xlink:href by feature chaining
        // must get the value from the nested attribute mapping instead, i.e. from another table
        // if it's to get the values from the local table, it shouldn't be set with feature chaining
        return steps.get(steps.size() - 1).getName().equals(XLINK.HREF);
    }

    static boolean isFid(StepList steps) {
        if (steps.isEmpty()) {
            return false;
        }
        return steps.get(steps.size() - 1).toString().matches("@(\\w+:)?id");
    }

    /**
     * Returns an object describing the sequence of feature chaining links that must be traversed to
     * reach the attribute specified by the visited expression.
     *
     * @return a feature chained attribute descriptor, or <code>null</code> if none was found
     */
    public List<FeatureChainedAttributeDescriptor> getFeatureChainedAttributes() {
        return attributes;
    }

    /**
     * Tells clients whether or not a conditional mapping was found in the feature chain when the
     * latest expression was visited.
     *
     * @return {@code true} if a conditional mapping was found in the feature chain, {@code false}
     *     otherwise
     */
    public boolean conditionalMappingWasFound() {
        return conditionalMappingFound;
    }

    protected boolean startsWith(StepList one, StepList other) {
        return one.startsWith(other);
    }

    /**
     * Descriptor class holding information about a feature chained attribute, i.e. an attribute
     * belonging to a feature type that is linked to a root feature type via feature chaining.
     *
     * <p>In more detail, purpose of this class is to store:
     *
     * <ol>
     *   <li>the sequence of nested attribute mappings describing how from top to bottom in the
     *       feature types chain
     *   <li>the path of the attribute, relative to the last linked feature type in the chain
     *       (except when the last chaining is done by reference, in which case the path refers to
     *       the second last feature type)
     * </ol>
     *
     * @author Stefano Costa, GeoSolutions
     */
    public static class FeatureChainedAttributeDescriptor {

        private List<FeatureChainLink> featureChain;

        private StepList attributePath;

        FeatureChainedAttributeDescriptor() {
            featureChain = new ArrayList<>();
        }

        /**
         * Returns the list of links in the feature types chain.
         *
         * @return a copy of the internal feature chain links list
         */
        public List<FeatureChainLink> getFeatureChain() {
            return new ArrayList<>(featureChain);
        }

        /**
         * Adds a new link in the feature types chain.
         *
         * @param chainLink the link to add
         */
        public void addLink(FeatureChainLink chainLink) {
            if (chainLink == null) {
                throw new NullPointerException("chainLink is null");
            }
            featureChain.add(chainLink);
            int size = featureChain.size();
            String alias = (size == 1) ? "chain_root" : "chain_link_" + (size - 1);
            chainLink.setAlias(alias);
            if (size > 1) {
                FeatureChainLink previousStep = featureChain.get(size - 2);
                previousStep.nextStep = chainLink;
                chainLink.previousStep = previousStep;
            }
        }

        /**
         * Gets a link in the feature types chain by its index.
         *
         * @param linkIdx the link index (0-based)
         * @return the feature chain link corresponding to the provided index
         * @throws IllegalArgumentException if <code>linkIdx</code> is negative
         * @throws IndexOutOfBoundsException if <code>linkIdx</code> is >= than the chain size
         */
        public FeatureChainLink getLink(int linkIdx) {
            if (linkIdx < 0) {
                throw new IllegalArgumentException("linkIdx must be > 0");
            }
            if (linkIdx >= featureChain.size()) {
                throw new IndexOutOfBoundsException("linkIdx " + linkIdx + " is not present");
            }
            return featureChain.get(linkIdx);
        }

        /**
         * Gets the first link in the feature types chain.
         *
         * @return the first feature chain link
         * @throws IndexOutOfBoundsException if the feature types chain is empty
         */
        public FeatureChainLink getFirstLink() {
            if (featureChain.size() == 0) {
                throw new IndexOutOfBoundsException("the list is empty");
            }
            return featureChain.get(0);
        }

        /**
         * Gets the last link in the feature types chain.
         *
         * @return the last feature chain link
         * @throws IndexOutOfBoundsException if the feature types chain is empty
         */
        public FeatureChainLink getLastLink() {
            if (featureChain.size() == 0) {
                throw new IndexOutOfBoundsException("the list is empty");
            }
            return featureChain.get(featureChain.size() - 1);
        }

        /**
         * Checks whether all nested attribute mappings are instances of {@link
         * JoiningNestedAttributeMapping}.
         *
         * @return <code>true</code> if all nested attribute mappings in the chain support joining,
         *     <code>false</code> otherwise
         */
        public boolean isJoiningEnabled() {
            boolean joiningEnabled = true;

            for (FeatureChainLink mappingStep : featureChain) {
                joiningEnabled =
                        joiningEnabled
                                && (!mappingStep.hasNestedFeature()
                                        || mappingStep.isJoiningNestedMapping());
            }

            return joiningEnabled;
        }

        /** Removes all elements in the feature types chain. */
        void clearChain() {
            featureChain.clear();
        }

        /**
         * Gets the size of the feature types chain.
         *
         * @return the number of links in the chain
         */
        public int chainSize() {
            return featureChain.size();
        }

        /**
         * Gets the path of the feature chained attribute.
         *
         * @return the attribute path
         */
        public StepList getAttributePath() {
            return attributePath;
        }

        /**
         * Sets the path of the feature chained attribute.
         *
         * @param attributePath the attribute path to set
         */
        public void setAttributePath(StepList attributePath) {
            this.attributePath = attributePath;
        }

        /**
         * Returns the feature type where the mapping configuration of the nested attribute is
         * defined.
         *
         * <p>In practice, this is the last linked feature type in the chain, except when the last
         * chaining is done by reference (via an xlink:href attribute), in which case the second
         * last feature type is returned.
         */
        public FeatureTypeMapping getFeatureTypeOwningAttribute() {
            FeatureChainLink lastLink = getLastLink();
            FeatureTypeMapping featureMapping = lastLink.getFeatureTypeMapping();
            if (lastLink.isChainingByReference()) {
                // last attribute xpath should be resolved against the parent feature
                if (lastLink.previous() != null) {
                    featureMapping = lastLink.previous().getFeatureTypeMapping();
                }
            }
            return featureMapping;
        }

        /**
         * Perform a shallow copy of this {@link FeatureChainedAttributeDescriptor} instance.
         *
         * @return a shallow copy of the instance
         */
        public FeatureChainedAttributeDescriptor shallowCopy() {
            FeatureChainedAttributeDescriptor copy = new FeatureChainedAttributeDescriptor();
            copy.featureChain.addAll(featureChain);
            copy.attributePath = attributePath;
            return copy;
        }
    }

    /**
     * Represents a single link in the "chain" of feature types that need to be linked to go from
     * the root type to a nested attribute.
     *
     * <p>The class is <code>public</code> as its purpose is to convey information to clients, but
     * instantiation and manipulation of its internal state is <code>private</code>.
     *
     * @author Stefano Costa, GeoSolutions
     */
    public static class FeatureChainLink {

        private FeatureTypeMapping featureTypeMapping;

        private NestedAttributeMapping nestedFeatureAttribute;

        private boolean chainingByReference;

        private String alias;

        private FeatureChainLink nextStep;

        private FeatureChainLink previousStep;

        private FeatureChainLink(FeatureTypeMapping featureType) {
            if (featureType == null) {
                throw new NullPointerException("featureType is null");
            }
            this.featureTypeMapping = featureType;
            this.nestedFeatureAttribute = null;
            this.chainingByReference = false;
            this.alias = featureType.getSource().getSchema().getName().getLocalPart();
            this.nextStep = null;
            this.previousStep = null;
        }

        private FeatureChainLink(
                FeatureTypeMapping featureType, NestedAttributeMapping nestedFeatureAttribute) {
            this(featureType);
            if (nestedFeatureAttribute == null) {
                throw new NullPointerException("nestedFeatureAttribute is null");
            }
            this.nestedFeatureAttribute = nestedFeatureAttribute;
        }

        private FeatureChainLink(FeatureTypeMapping featureType, boolean chainingByReference) {
            this(featureType);
            this.chainingByReference = chainingByReference;
        }

        /**
         * Gets the mapping configuration of the linked feature type.
         *
         * @return the linked feature type mapping
         */
        public FeatureTypeMapping getFeatureTypeMapping() {
            return featureTypeMapping;
        }

        /**
         * Gets the mapping configuration of the attribute holding the next nested feature in the
         * chain.
         *
         * @return the nested attribute mapping, or <code>null</code> if there are no more nested
         *     features in the chain
         */
        public NestedAttributeMapping getNestedFeatureAttribute() {
            return nestedFeatureAttribute;
        }

        /**
         * Gets the mapping configuration of the attribute holding the next nested feature in the
         * chain, cast to the specified {@link NestedAttributeMapping} subclass.
         *
         * @see #getNestedFeatureAttribute()
         * @param attributeMappingClass the {@link NestedAttributeMapping} subclass to cast to
         */
        public <T extends NestedAttributeMapping> T getNestedFeatureAttribute(
                Class<T> attributeMappingClass) {
            return attributeMappingClass.cast(nestedFeatureAttribute);
        }

        /**
         * Returns <code>true</code> if this {@link FeatureChainLink} instance represents a
         * chaining-by-reference mapping, i.e. nested feature is not fully encoded inline, only
         * <code>xlink:href</code> attribute is set.
         *
         * @return <code>true</code> if this is chaining by reference, <code>false</code> otherwise
         */
        public boolean isChainingByReference() {
            return chainingByReference;
        }

        /**
         * Returns <code>true</code> if joining support is enabled for the nested attribute mapping.
         *
         * @return <code>true</code> if joining support is enabled for this chain link, <code>false
         *     </code> otherwise
         */
        public boolean isJoiningNestedMapping() {
            return nestedFeatureAttribute != null
                    && nestedFeatureAttribute instanceof JoiningNestedAttributeMapping;
        }

        /**
         * Returns <code>true</code> if this link refers to a nested feature type which in turn
         * contains another nested feature.
         *
         * @return <code>true</code> if there is another nested feature in the chain, <code>false
         *     </code> otherwise
         */
        public boolean hasNestedFeature() {
            return nestedFeatureAttribute != null;
        }

        /**
         * Unique identifier of a link in the chain; mainly useful when SQL encoding the feature
         * chained attribute.
         *
         * @return a unique identifier of the link
         */
        public String getAlias() {
            return alias;
        }

        private void setAlias(String alias) {
            this.alias = alias;
        }

        /**
         * Returns the next link in the chain.
         *
         * @return the next link, or <code>null</code> if none exists
         */
        public FeatureChainLink next() {
            return nextStep;
        }

        /**
         * Returns the previous link in the chain.
         *
         * @return the previous link, or <code>null</code> if none exists
         */
        public FeatureChainLink previous() {
            return previousStep;
        }
    }
}
