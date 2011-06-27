/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.geotools.data.complex.ComplexFeatureConstants;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.AttributeBuilder;
import org.geotools.feature.AttributeImpl;
import org.geotools.feature.NameImpl;
import org.geotools.feature.Types;
import org.geotools.feature.ValidatingFeatureFactoryImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.GeometryTypeImpl;
import org.geotools.feature.type.UniqueNameFeatureTypeFactoryImpl;
import org.geotools.util.CheckedArrayList;
import org.geotools.xs.XSSchema;
import org.opengis.feature.Attribute;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.FeatureFactory;
import org.opengis.feature.Property;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.Cloneable;
import org.xml.sax.helpers.NamespaceSupport;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Utility class to evaluate XPath expressions against an Attribute instance, which may be any
 * Attribute, whether it is simple, complex, a feature, etc.
 * <p>
 * At the difference of the Filter subsystem, which works against Attribute contents (for example to
 * evaluate a comparison filter), the XPath subsystem, for which this class is the single entry
 * point, works against Attribute instances. That is, the result of an XPath expression, if a single
 * value, is an Attribtue, not the attribute content, or a List of Attributes, for instance.
 * </p>
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @author Rini Angreani, Curtin University of Technology
 * @version $Id$
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main
 *         /java/org/geotools/data/complex/filter/XPath.java $
 * @since 2.4
 */
public class XPath {

    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(XPath.class
            .getPackage().getName());

    private FilterFactory FF;

    private FeatureFactory featureFactory;
    
    private CoordinateReferenceSystem crs;

    /**
     * Used to create specific attribute descriptors for
     * {@link #set(Attribute, String, Object, String, AttributeType)} when the actual attribute
     * instance is of a derived type of the corresponding one declared in the feature type.
     */
    private FeatureTypeFactory descriptorFactory;

    public XPath() {
        this.FF = CommonFactoryFinder.getFilterFactory(null);
        this.featureFactory = new ValidatingFeatureFactoryImpl();
        this.descriptorFactory = new UniqueNameFeatureTypeFactoryImpl();
    }

    public XPath(FilterFactory ff, FeatureFactory featureFactory) {
        setFilterFactory(ff);
        setFeatureFactory(featureFactory);
        // this.descriptorFactory = new TypeFactoryImpl();
    }

    public void setFilterFactory(FilterFactory ff) {
        this.FF = ff;
    }
    
    public void setCRS(CoordinateReferenceSystem crs) {
        this.crs = crs;
    }

    public void setFeatureFactory(FeatureFactory featureFactory) {
        this.featureFactory = featureFactory;
    }

    public static class StepList extends CheckedArrayList<Step> {
        private static final long serialVersionUID = -5612786286175355862L;

        private StepList() {
            super(XPath.Step.class);
        }

        public StepList(StepList steps) {
            super(XPath.Step.class);
            addAll(steps);
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            for (Iterator<Step> it = iterator(); it.hasNext();) {
                Step s = (Step) it.next();
                sb.append(s.toString());
                if (it.hasNext()) {
                    sb.append("/");
                }
            }
            return sb.toString();
        }

        public StepList subList(int fromIndex, int toIndex) {
            if (fromIndex < 0)
                throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
            if (toIndex > size())
                throw new IndexOutOfBoundsException("toIndex = " + toIndex);
            if (fromIndex > toIndex)
                throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex("
                        + toIndex + ")");
            StepList subList = new StepList();
            for (int i = fromIndex; i < toIndex; i++) {
                subList.add(this.get(i));
            }
            return subList;
        }

        public StepList clone() {
            StepList copy = new StepList();
            for (Step step : this) {
                copy.add((Step) step.clone());
            }
            return copy;
        }

        /**
         * Compares this StepList with another for equivalence regardless of the indexes of each
         * Step.
         * 
         * @param propertyName
         * @return <code>true</code> if this step list has the same location paths than
         *         <code>propertyName</code> ignoring the indexes in each step. <code>false</code>
         *         otherwise.
         */
        public boolean equalsIgnoreIndex(final StepList propertyName) {
            if (propertyName == null) {
                return false;
            }
            if (propertyName == this) {
                return true;
            }
            if (size() != propertyName.size()) {
                return false;
            }
            Iterator mine = iterator();
            Iterator him = propertyName.iterator();
            Step myStep;
            Step hisStep;
            while (mine.hasNext()) {
                myStep = (Step) mine.next();
                hisStep = (Step) him.next();
                if (myStep.isIndexed()) {
                    if (!myStep.equals(hisStep)) {
                        return false;
                    }
                } else {
                    if (!myStep.equalsIgnoreIndex(hisStep)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    /**
     * 
     * @author gabriel
     * 
     */
    public static class Step implements Cloneable {
        private int index;

        private QName attributeName;

        private boolean isXmlAttribute;

        private boolean isIndexed;

        /**
         * Creates a "property" xpath step (i.e. isXmlAttribute() == false).
         * 
         * @param name
         * @param index
         */
        public Step(final QName name, final int index) {
            this(name, index, false, false);
        }

        /**
         * Creates an xpath step for the given qualified name and index; and the given flag to
         * indicate if it it an "attribute" or "property" step.
         * 
         * @param name
         *            the qualified name of the step (name should include prefix to be reflected in
         *            toString())
         * @param index
         *            the index (indexing starts at 1 for Xpath) of the step
         * @param isXmlAttribute
         *            whether the step referers to an "attribute" or a "property" (like for
         *            attributes and elements in xml)
         * @throws NullPointerException
         *             if <code>name==null</code>
         * @throws IllegalArgumentException
         *             if <code>index &lt; 1</code>
         */
        public Step(final QName name, final int index, boolean isXmlAttribute) {
            this(name, index, isXmlAttribute, false);
        }

        /**
         * Creates an xpath step for the given qualified name and index; and the given flag to
         * indicate if it it an "attribute" or "property" step.
         * 
         * @param name
         *            the qualified name of the step (name should include prefix to be reflected in
         *            toString())
         * @param index
         *            the index (indexing starts at 1 for Xpath) of the step
         * @param isXmlAttribute
         *            whether the step referers to an "attribute" or a "property" (like for
         *            attributes and elements in xml)
         * @param isIndexed
         *            whether or not the index is to be shown in the string representation even if
         *            index = 1
         * @throws NullPointerException
         *             if <code>name==null</code>
         * @throws IllegalArgumentException
         *             if <code>index &lt; 1</code>
         */
        public Step(final QName name, final int index, boolean isXmlAttribute, boolean isIndexed) {
            if (name == null) {
                throw new NullPointerException("name");
            }
            if (index < 1) {
                throw new IllegalArgumentException("index shall be >= 1");
            }
            this.attributeName = name;
            this.index = index;
            this.isXmlAttribute = isXmlAttribute;
            this.isIndexed = isIndexed;
        }

        /**
         * Compares this Step with another for equivalence ignoring the steps indexes.
         * 
         * @param hisStep
         * @return
         */
        public boolean equalsIgnoreIndex(Step other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            return attributeName.equals(other.attributeName)
                    && isXmlAttribute == other.isXmlAttribute;
        }

        public int getIndex() {
            return index;
        }

        public boolean isIndexed() {
            return isIndexed;
        }

        public QName getName() {
            return attributeName;
        }

        public String toString() {
            StringBuffer sb = new StringBuffer(isXmlAttribute ? "@" : "");
            if (XMLConstants.DEFAULT_NS_PREFIX != attributeName.getPrefix()) {
                sb.append(attributeName.getPrefix()).append(':');
            }
            sb.append(attributeName.getLocalPart());
            if (isIndexed) {
                // we want to print index = 1 as well if specified
                // so filtering on the first index doesn't return all
                // e.g. gml:name[1] doesn't get translated to
                // gml:name i.e. all gml:name instances
                sb.append("[").append(index).append("]");
            }
            return sb.toString();
        }

        public boolean equals(Object o) {
            if (!(o instanceof Step)) {
                return false;
            }
            Step s = (Step) o;
            return attributeName.equals(s.attributeName) && index == s.index
                    && isXmlAttribute == s.isXmlAttribute;
        }

        public int hashCode() {
            return 17 * attributeName.hashCode() + 37 * index;
        }

        public Object clone() {
            return new Step(this.attributeName, this.index, this.isXmlAttribute, this.isIndexed);
        }

        /**
         * Flag that indicates that this single step refers to an "attribute" rather than a
         * "property".
         * <p>
         * I.e. it was created from the last step of an expression like
         * <code>foo/bar@attribute</code>.
         * </p>
         * 
         * @return
         */
        public boolean isXmlAttribute() {
            return isXmlAttribute;
        }
    }

    /**
     * Returns the list of stepts in <code>xpathExpression</code> by cleaning it up removing
     * unnecessary elements.
     * <p>
     * </p>
     * 
     * @param root
     *            non null descriptor of the root attribute, generally the Feature descriptor. Used
     *            to ignore the first step in xpathExpression if the expression's first step is
     *            named as rootName.
     * 
     * @param xpathExpression
     * @return
     * @throws IllegalArgumentException
     *             if <code>xpathExpression</code> has no steps or it isn't a valid XPath expression
     *             against <code>type</code>.
     */
    public static StepList steps(final AttributeDescriptor root, final String xpathExpression,
            final NamespaceSupport namespaces) throws IllegalArgumentException {

        if (root == null) {
            throw new NullPointerException("root");
        }

        if (xpathExpression == null) {
            throw new NullPointerException("xpathExpression");
        }

        String expression = xpathExpression.trim();

        if ("".equals(expression)) {
            throw new IllegalArgumentException("expression is empty");
        }

        StepList steps = new StepList();

        if ("/".equals(expression)) {
            expression = root.getName().getLocalPart();
        }

        if (expression.startsWith("/")) {
            expression = expression.substring(1);
        }

        final String[] partialSteps = expression.split("[/]");

        if (partialSteps.length == 0) {
            throw new IllegalArgumentException("no steps provided");
        }

        int startIndex = 0;

        for (int i = startIndex; i < partialSteps.length; i++) {

            String step = partialSteps[i];
            if ("..".equals(step)) {
                steps.remove(steps.size() - 1);
            } else if (".".equals(step)) {
                continue;
            } else {
                int index = 1;
                boolean isXmlAttribute = false;
                boolean isIndexed = false;
                String stepName = step;
                if (step.indexOf('[') != -1) {
                    int start = step.indexOf('[');
                    int end = step.indexOf(']');
                    stepName = step.substring(0, start);
                    Scanner scanner = new Scanner(step.substring(start + 1, end));
                    if (scanner.hasNextInt()) {
                        index = scanner.nextInt();
                        isIndexed = true;
                    }
                }
                if (step.charAt(0) == '@') {
                    isXmlAttribute = true;
                    stepName = stepName.substring(1);
                }
                QName qName = deglose(stepName, root, namespaces, isXmlAttribute);
                steps.add(new Step(qName, index, isXmlAttribute, isIndexed));
            }
            //            
            // if (step.indexOf('[') != -1) {
            // int start = step.indexOf('[');
            // int end = step.indexOf(']');
            // String stepName = step.substring(0, start);
            // int stepIndex = Integer.parseInt(step.substring(start + 1, end));
            // QName qName = deglose(stepName, root, namespaces);
            // steps.add(new Step(qName, stepIndex));
            // } else if ("..".equals(step)) {
            // steps.remove(steps.size() - 1);
            // } else if (".".equals(step)) {
            // continue;
            // } else {
            // QName qName = deglose(step, root, namespaces);
            // steps.add(new Step(qName, 1));
            // }
        }

        // XPath simplification phase: if the xpath expression contains more
        // nodes
        // than the root node itself, and the root node is present, remove the
        // root
        // node as it is redundant
        if (root != null && steps.size() > 1) {
            Step step = (Step) steps.get(0);
            Name rootName = root.getName();
            QName stepName = step.getName();
            if (Types.equals(rootName, stepName)) {
                LOGGER.fine("removing root name from xpath " + steps + " as it is redundant");
                steps.remove(0);
            }
        }

        return steps;
    }

    private static QName deglose(final String prefixedName, final AttributeDescriptor root,
            final NamespaceSupport namespaces, boolean isXmlAttribute) {
        if (prefixedName == null) {
            throw new NullPointerException("prefixedName");
        }

        QName name = null;

        final String prefix;
        final String namespaceUri;
        final String localName;

        int prefixIdx = prefixedName.indexOf(':');

        if (prefixIdx == -1) {
            localName = prefixedName;
            final Name rootName = root.getName();
            // don't use default namespace for client properties (xml attribute), and FEATURE_LINK
            final String defaultNamespace = (isXmlAttribute
                    || localName.equals(ComplexFeatureConstants.FEATURE_CHAINING_LINK_NAME
                            .getLocalPart()) || rootName.getNamespaceURI() == null) ? XMLConstants.NULL_NS_URI
                    : rootName.getNamespaceURI();
            namespaceUri = defaultNamespace;
            if (XMLConstants.NULL_NS_URI.equals(defaultNamespace)) {
                prefix = XMLConstants.DEFAULT_NS_PREFIX;
            } else {
                if (!localName.equals(rootName.getLocalPart())) {
                    LOGGER.warning("Using root's namespace " + defaultNamespace
                            + " for step named '" + localName + "', as no prefix was stated");
                }
                prefix = namespaces.getPrefix(defaultNamespace);

                if (prefix == null) {
                    throw new IllegalStateException("Default namespace is not mapped to a prefix: "
                            + defaultNamespace);
                }
            }
        } else {
            prefix = prefixedName.substring(0, prefixIdx);
            localName = prefixedName.substring(prefixIdx + 1);
            namespaceUri = namespaces.getURI(prefix);
        }

        name = new QName(namespaceUri, localName, prefix);

        return name;
    }

    /**
     * Sets the value of the attribute of <code>att</code> addressed by <code>xpath</code> and of
     * type <code>targetNodeType</code> to be <code>value</code> with id <code>id</code>.
     * 
     * @param att
     *            the root attribute for which to set the child attribute value
     * @param xpath
     *            the xpath expression that addresses the <code>att</code> child whose value is to
     *            be set
     * @param value
     *            the value of the attribute addressed by <code>xpath</code>
     * @param id
     *            the identifier of the attribute addressed by <code>xpath</code>, might be
     *            <code>null</code>
     * @param targetNodeType
     *            the expected type of the attribute addressed by <code>xpath</code>, or
     *            <code>null</code> if unknown
     * @param isXlinkRef
     *            true if the attribute would only contain xlink:href client property
     * @return
     */
    public Attribute set(final Attribute att, final StepList xpath, Object value, String id,
            AttributeType targetNodeType, boolean isXlinkRef, Expression sourceExpression) {
        return set(att, xpath, value, id, targetNodeType, isXlinkRef, null, sourceExpression);
    }

    public Attribute set(final Attribute att, final StepList xpath, Object value, String id,
            AttributeType targetNodeType, boolean isXlinkRef, AttributeDescriptor targetDescriptor,
            Expression sourceExpression) {
        if (XPath.LOGGER.isLoggable(Level.CONFIG)) {
            XPath.LOGGER.entering("XPath", "set", new Object[] { att, xpath, value, id,
                    targetNodeType });
        }

        final StepList steps = new StepList(xpath);

        // if (steps.size() < 2) {
        // throw new IllegalArgumentException("parent not yet built for " +
        // xpath);
        // }

        Attribute parent = att;
        Name rootName = null;
        if (parent.getDescriptor() != null) {
            rootName = parent.getDescriptor().getName();
            Step rootStep = (Step) steps.get(0);
            QName stepName = rootStep.getName();
            if (stepName.getLocalPart().equals(rootName.getLocalPart())) {
                if (XMLConstants.NULL_NS_URI.equals(stepName.getNamespaceURI())
                        || stepName.getNamespaceURI().equals(rootName.getNamespaceURI())) {
                    // first step is the self reference to att, so skip it
                    steps.remove(0);
                }
            }
        }

        Iterator stepsIterator = steps.iterator();

        for (; stepsIterator.hasNext();) {
            final XPath.Step currStep = (Step) stepsIterator.next();
            AttributeDescriptor currStepDescriptor = null;
            final boolean isLastStep = !stepsIterator.hasNext();
            final QName stepName = currStep.getName();
            final Name attributeName = Types.toName(stepName);

            final AttributeType _parentType = parent.getType();
            if (_parentType.equals(XSSchema.ANYTYPE_TYPE) && targetDescriptor != null) {
                // this needs to be passed on if casting anyType to something else, since it won't
                // exist in the schema
                currStepDescriptor = targetDescriptor;
            } else {
                ComplexType parentType = (ComplexType) _parentType;

                if (!isLastStep || targetNodeType == null) {
                    if (null == attributeName.getNamespaceURI()) {
                        currStepDescriptor = (AttributeDescriptor) Types.descriptor(parentType,
                                attributeName.getLocalPart());
                    } else {
                        currStepDescriptor = (AttributeDescriptor) Types.descriptor(parentType,
                                attributeName);
                    }

                    if (currStepDescriptor == null) {
                        // need to take the non easy way, may be the instance has a
                        // value for this step with a different name, of a derived
                        // type of the one declared in the parent type
                        String prefixedStepName = currStep.toString();
                        PropertyName name = FF.property(prefixedStepName);
                        Attribute child = (Attribute) name.evaluate(parent);
                        if (child != null) {
                            currStepDescriptor = child.getDescriptor();
                        }
                    }
                } else {
                    AttributeDescriptor actualDescriptor;
                    if (null == attributeName.getNamespaceURI()) {
                        actualDescriptor = (AttributeDescriptor) Types.descriptor(parentType,
                                attributeName.getLocalPart(), targetNodeType);
                    } else {
                        actualDescriptor = (AttributeDescriptor) Types.descriptor(parentType,
                                attributeName, targetNodeType);
                    }

                    if (actualDescriptor != null) {
                        int minOccurs = actualDescriptor.getMinOccurs();
                        int maxOccurs = actualDescriptor.getMaxOccurs();
                        boolean nillable = actualDescriptor.isNillable();
                        if (actualDescriptor instanceof GeometryDescriptor) {
                            // important to maintain CRS information encoding
                            if (Geometry.class.isAssignableFrom(targetNodeType.getBinding())) {
                                if (!(targetNodeType instanceof GeometryType)) {
                                    targetNodeType = new GeometryTypeImpl(targetNodeType.getName(),
                                            targetNodeType.getBinding(), crs != null ? crs
                                                    : ((GeometryDescriptor) actualDescriptor)
                                                            .getCoordinateReferenceSystem(),
                                            targetNodeType.isIdentified(), targetNodeType
                                                    .isAbstract(),
                                            targetNodeType.getRestrictions(), targetNodeType
                                                    .getSuper(), targetNodeType.getDescription());
                                }
                                currStepDescriptor = descriptorFactory.createGeometryDescriptor(
                                        (GeometryType) targetNodeType, attributeName, minOccurs,
                                        maxOccurs, nillable, null);
                            } else {
                                throw new IllegalArgumentException("Can't set targetNodeType: "
                                        + targetNodeType.toString() + " for attribute mapping: "
                                        + attributeName + " as it is not a Geometry type!");
                            }
                        } else {
                            currStepDescriptor = descriptorFactory.createAttributeDescriptor(
                                    targetNodeType, attributeName, minOccurs, maxOccurs, nillable,
                                    null);
                        }
                    }
                }

                if (currStepDescriptor == null) {
                    StringBuffer parentAtts = new StringBuffer();
                    Collection properties = parentType.getDescriptors();
                    for (Iterator it = properties.iterator(); it.hasNext();) {
                        PropertyDescriptor desc = (PropertyDescriptor) it.next();
                        Name name = desc.getName();
                        parentAtts.append(name.getNamespaceURI());
                        parentAtts.append("#");
                        parentAtts.append(name.getLocalPart());
                        if (it.hasNext()) {
                            parentAtts.append(", ");
                        }
                    }
                    throw new IllegalArgumentException(currStep
                            + " is not a valid location path for type " + parentType.getName()
                            + ". " + currStep + " ns: " + currStep.getName().getNamespaceURI()
                            + ", " + parentType.getName().getLocalPart() + " properties: "
                            + parentAtts);
                }
            }

            if (isLastStep) {
                // reached the leaf
                if (currStepDescriptor == null) {
                    throw new IllegalArgumentException(currStep
                            + " is not a valid location path for type " + _parentType.getName());
                }
                if (value == null && !currStepDescriptor.isNillable() && sourceExpression != null
                        && !sourceExpression.equals(Expression.NIL)) {
                    if (currStepDescriptor.getMinOccurs() == 0) {
                        return null;
                    }
                }
                int index = currStep.isIndexed ? currStep.getIndex() : -1;
                Attribute attribute = setValue(currStepDescriptor, id, value, index, parent,
                        targetNodeType, isXlinkRef);
                return attribute;
            } else {
                // parent = appendComplexProperty(parent, currStep,
                // currStepDescriptor);
                int index = currStep.isIndexed ? currStep.getIndex() : -1;
                parent = setValue(currStepDescriptor, null, new ArrayList<Property>(), index,
                        parent, null, isXlinkRef);
            }
        }
        throw new IllegalStateException();
    }

    private Attribute setValue(final AttributeDescriptor descriptor, final String id,
            final Object value, final int index, final Attribute parent,
            final AttributeType targetNodeType, boolean isXlinkRef) {
        // adapt value to context
        Object convertedValue = convertValue(descriptor, value);
        Attribute leafAttribute = null;
        final Name attributeName = descriptor.getName();        
        if (!isXlinkRef) {
            // skip this process if the attribute would only contain xlink:ref
            // that is chained, because it won't contain any values, and we
            // want to create a new empty leaf attribute
            if (parent instanceof ComplexAttribute) {
                Object currStepValue = ((ComplexAttribute) parent).getProperties(attributeName);
                if (currStepValue instanceof Collection) {
                    List<Attribute> values = new ArrayList((Collection) currStepValue);
                    if (!values.isEmpty()) {
                        if (isEmpty(convertedValue)) {
                            // when attribute is empty, it is probably just a parent of a leaf
                            // attribute
                            // it could already exist from another attribute mapping for a different
                            // leaf
                            // e.g. 2 different attribute mappings:
                            // sa:relatedObservation/om:Observation/om:parameter[2]/swe:Time/swe:uom
                            // sa:relatedObservation/om:Observation/om:parameter[2]/swe:Time/swe:value
                            // and this could be processing om:parameter[2] the second time for
                            // swe:value
                            // so we need to find it if it already exists
                            if (index > -1) {
                                // get the attribute of specified index
                                for (Attribute stepValue : values) {
                                    if (stepValue.getUserData().containsKey(
                                            ComplexFeatureConstants.MAPPED_ATTRIBUTE_INDEX)) {
                                        if (index == Integer.parseInt(
                                                String.valueOf(stepValue.getUserData().get(
                                                    ComplexFeatureConstants.MAPPED_ATTRIBUTE_INDEX)))) {
                                            return stepValue;
                                        }
                                    }
                                }
                            } else {
                                // get the last existing node
                                return values.get(values.size() - 1);
                            }
                        } else {
                            for (Attribute stepValue : values) {
                                // eliminate duplicates in case the values come from denormalized
                                // view..
                                boolean sameIndex = true;
                                if (index > -1) {
                                    if (stepValue.getUserData().containsKey(
                                            ComplexFeatureConstants.MAPPED_ATTRIBUTE_INDEX)) {
                                        sameIndex = (index == Integer.parseInt(
                                            String.valueOf(stepValue.getUserData().get(
                                                ComplexFeatureConstants.MAPPED_ATTRIBUTE_INDEX))));
                                    }
                                }
                                if (sameIndex && stepValue.getValue().equals(convertedValue)) {
                                    return stepValue;
                                }
                            }
                        }
                    }
                } else if (currStepValue instanceof Attribute) {
                    leafAttribute = (Attribute) currStepValue;
                } else if (currStepValue != null) {
                    throw new IllegalStateException("Unknown addressed object. Xpath:"
                            + attributeName + ", addressed: " + currStepValue.getClass().getName()
                            + " [" + currStepValue.toString() + "]");
                }
            }
        }
        if (leafAttribute == null) {
            AttributeBuilder builder = new AttributeBuilder(featureFactory);
            if (crs != null) {
                builder.setCRS(crs);
            }
            builder.setDescriptor(parent.getDescriptor());
            // check for mapped type override
            builder.setType(parent.getType());

            if (targetNodeType != null) {
                if (parent.getType().equals(XSSchema.ANYTYPE_TYPE)) {
                    // special handling for casting any type since there's no attributes in its
                    // schema
                    leafAttribute = builder.addAnyTypeValue(convertedValue, targetNodeType,
                            descriptor, id);
                } else {
                    leafAttribute = builder.add(id, convertedValue, attributeName, targetNodeType);
                }
            } else if (value == null && descriptor.getType().equals(XSSchema.ANYTYPE_TYPE)) {
                // casting anyType as a complex attribute so we can set xlink:href
                leafAttribute = builder.addComplexAnyTypeAttribute(convertedValue, descriptor, id);
            } else {
                leafAttribute = builder.add(id, convertedValue, attributeName);
            }
            if (index > -1) {
                // set attribute index if specified so it can be retrieved later for grouping
                leafAttribute.getUserData().put(ComplexFeatureConstants.MAPPED_ATTRIBUTE_INDEX,
                        index);
            }
            List newValue = new ArrayList();
            newValue.addAll((Collection) parent.getValue());
            newValue.add(leafAttribute);
            parent.setValue(newValue);
        }

        if (!isEmpty(convertedValue)) {
            leafAttribute.setValue(convertedValue);
        }
        return leafAttribute;
    }

    private boolean isEmpty(Object convertedValue) {
        if (convertedValue == null) {
            return true;
        } else if (convertedValue instanceof Collection && ((Collection) convertedValue).isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return value converted into a type suitable for this descriptor.
     * 
     * @param descriptor
     * @param value
     * @return
     */
    @SuppressWarnings("serial")
    private Object convertValue(final AttributeDescriptor descriptor, final Object value) {
        final AttributeType type = descriptor.getType();
        Class<?> binding = type.getBinding();

        if (type instanceof ComplexType && binding == Collection.class) {
            if (!(value instanceof Collection) && isSimpleContentType(type)) {
                ArrayList<Property> list = new ArrayList<Property>();
                if (value == null && !descriptor.isNillable()) {
                    return list;
                }
                list.add(buildSimpleContent(type, value));
                return list;
            }
        }
        if (binding == String.class && value instanceof Collection) {
            // if it's a single value in a collection, strip the square brackets
            String collectionString = value.toString();
            return collectionString.substring(1, collectionString.length() - 1);
        }
        return FF.literal(value).evaluate(value, binding);
    }

    /**
     * Return true if the type is either a simple type or has a simple type as its supertype. In
     * particular, complex types with simple content will return true.
     * 
     * @param type
     * @return
     */
    static boolean isSimpleContentType(AttributeType type) {
        if (type == XSSchema.ANYSIMPLETYPE_TYPE) {
            // should never happen as this type is abstract
            throw new RuntimeException("Unexpected simple type");
        }
        AttributeType superType = type.getSuper();
        if (superType == XSSchema.ANYSIMPLETYPE_TYPE) {
            return true;
        } else if (superType == null) {
            return false;
        } else {
            return isSimpleContentType(superType);
        }
    }

    /**
     * Get base (non-collection) type of simple content.
     * 
     * @param type
     * @return
     */
    static AttributeType getSimpleContentType(AttributeType type) {
        Class<?> binding = type.getBinding();
        if (binding == Collection.class) {
            return getSimpleContentType(type.getSuper());
        } else {
            return type;
        }
    }

    /**
     * Create a fake property for simple content of a complex type.
     * 
     * @param type
     * @param value
     * @return
     */
    Attribute buildSimpleContent(AttributeType type, Object value) {
        AttributeType simpleContentType = getSimpleContentType(type);
        Object convertedValue = FF.literal(value).evaluate(value,
                getSimpleContentType(type).getBinding());
        Name name = new NameImpl(null, "simpleContent");
        AttributeDescriptor descriptor = new AttributeDescriptorImpl(simpleContentType, name, 1, 1,
                true, (Object) null);
        return new AttributeImpl(convertedValue, descriptor, null);
    }

    public boolean isComplexType(final StepList attrXPath, final AttributeDescriptor featureType) {
        PropertyName attExp = FF.property(attrXPath.toString());
        Object type = attExp.evaluate(featureType);
        if (type == null) {
            type = attExp.evaluate(featureType);
            throw new IllegalArgumentException("path not found: " + attrXPath);
        }

        AttributeDescriptor node = (AttributeDescriptor) type;
        return node.getType() instanceof ComplexType;
    }

}
