/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import org.geotools.feature.type.Types;
import org.geotools.util.CheckedArrayList;
import org.geotools.util.SuppressFBWarnings;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.util.Cloneable;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Utility class to evaluate XPath expressions against an Attribute instance, which may be any
 * Attribute, whether it is simple, complex, a feature, etc.
 *
 * <p>At the difference of the Filter subsystem, which works against Attribute contents (for example
 * to evaluate a comparison filter), the XPath subsystem, for which this class is the single entry
 * point, works against Attribute instances. That is, the result of an XPath expression, if a single
 * value, is an Attribtue, not the attribute content, or a List of Attributes, for instance.
 *
 * @author Gabriel Roldan (Axios Engineering)
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 * @version $Id$
 */
public class XPathUtil {

    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(XPathUtil.class);

    public static class StepList extends CheckedArrayList<Step> {
        private static final long serialVersionUID = -5612786286175355862L;

        private StepList() {
            super(XPathUtil.Step.class);
        }

        public StepList(StepList steps) {
            super(XPathUtil.Step.class);
            addAll(steps);
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            for (Iterator<Step> it = iterator(); it.hasNext(); ) {
                Step s = (Step) it.next();
                sb.append(s.toString());
                if (it.hasNext()) {
                    sb.append("/");
                }
            }
            return sb.toString();
        }

        public boolean containsPredicate() {
            for (int i = 0; i < size(); i++) {
                if (get(i).getPredicate() != null) {
                    return true;
                }
            }

            return false;
        }

        public boolean startsWith(StepList other) {
            if (other.size() > this.size()) {
                return false;
            }
            for (int i = 0; i < other.size(); i++) {
                Step thisStep = this.get(i);
                Step otherStep = other.get(i);
                if (thisStep.isIndexed && otherStep.isIndexed) {
                    return thisStep.equals(otherStep);
                } else {
                    return thisStep.equalsIgnoreIndex(otherStep);
                }
            }
            return true;
        }

        public StepList subList(int fromIndex, int toIndex) {
            if (fromIndex < 0) throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
            if (toIndex > size()) throw new IndexOutOfBoundsException("toIndex = " + toIndex);
            if (fromIndex > toIndex)
                throw new IllegalArgumentException(
                        "fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
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
         * @return <code>true</code> if this step list has the same location paths than <code>
         *     propertyName</code> ignoring the indexes in each step. <code>false</code> otherwise.
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
                if (!myStep.equalsIgnoreIndex(hisStep)) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Find the first steps matching the xpath within this list, and set an index to it.
         *
         * @param index the new index for the matching steps
         * @param xpath the xpath to be searched
         */
        public void setIndex(int index, String xpath, String separator) {
            if (this.toString().contains(xpath)) {
                for (int i = 0; i < size() - 1; i++) {
                    String firstString = get(i).toString();
                    if (xpath.equals(firstString)) {
                        get(i).setIndex(index);
                        return;
                    }
                    if (xpath.startsWith(firstString)) {
                        StringBuffer buf = new StringBuffer(firstString);
                        buf.append(separator);
                        for (int j = i + 1; j < size() - 1; j++) {
                            buf.append(get(j).toString());
                            if (buf.toString().equals(xpath)) {
                                get(j).setIndex(index);
                                return;
                            }
                            buf.append(separator);
                        }
                    }
                }
            }
        }
    }

    /** @author Gabriel Roldan */
    public static class Step implements Cloneable {
        private int index;

        private String predicate = null;

        private QName attributeName;

        private boolean isXmlAttribute;

        private boolean isIndexed;

        /** Creates a "property" xpath step (i.e. isXmlAttribute() == false). */
        public Step(final QName name, final int index) {
            this(name, index, false, false);
        }

        /**
         * Creates an xpath step for the given qualified name and index; and the given flag to
         * indicate if it it an "attribute" or "property" step.
         *
         * @param name the qualified name of the step (name should include prefix to be reflected in
         *     toString())
         * @param index the index (indexing starts at 1 for Xpath) of the step
         * @param isXmlAttribute whether the step referers to an "attribute" or a "property" (like
         *     for attributes and elements in xml)
         * @throws NullPointerException if <code>name==null</code>
         * @throws IllegalArgumentException if <code>index &lt; 1</code>
         */
        public Step(final QName name, final int index, boolean isXmlAttribute) {
            this(name, index, isXmlAttribute, false);
        }

        /**
         * Creates an xpath step for the given qualified name and index; and the given flag to
         * indicate if it it an "attribute" or "property" step.
         *
         * @param name the qualified name of the step (name should include prefix to be reflected in
         *     toString())
         * @param index the index (indexing starts at 1 for Xpath) of the step
         * @param isXmlAttribute whether the step referers to an "attribute" or a "property" (like
         *     for attributes and elements in xml)
         * @param isIndexed whether or not the index is to be shown in the string representation
         *     even if index = 1
         * @throws NullPointerException if <code>name==null</code>
         * @throws IllegalArgumentException if <code>index &lt; 1</code>
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

        public Step(final QName name, boolean isXmlAttribute, final String predicate) {
            if (name == null) {
                throw new NullPointerException("name");
            }
            this.attributeName = name;
            this.index = 1;
            this.isIndexed = false;
            this.isXmlAttribute = isXmlAttribute;
            this.predicate = predicate;
        }

        /** Compares this Step with another for equivalence ignoring the steps indexes. */
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

        public String getPredicate() {
            return predicate;
        }

        public boolean isIndexed() {
            return isIndexed;
        }

        public QName getName() {
            return attributeName;
        }

        @SuppressFBWarnings("ES_COMPARING_STRINGS_WITH_EQ")
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
            } else if (predicate != null) {
                sb.append("[").append(predicate).append("]");
            }
            return sb.toString();
        }

        public boolean equals(Object o) {
            if (!(o instanceof Step)) {
                return false;
            }
            Step s = (Step) o;
            return attributeName.equals(s.attributeName)
                    && index == s.index
                    && isXmlAttribute == s.isXmlAttribute
                    && Objects.equals(predicate, s.predicate);
        }

        public int hashCode() {
            return 17 * attributeName.hashCode() + 37 * index;
        }

        public Step clone() {
            return predicate == null
                    ? new Step(this.attributeName, this.index, this.isXmlAttribute, this.isIndexed)
                    : new Step(this.attributeName, this.isXmlAttribute, this.predicate);
        }

        /**
         * Flag that indicates that this single step refers to an "attribute" rather than a
         * "property".
         *
         * <p>I.e. it was created from the last step of an expression like <code>foo/bar@attribute
         * </code>.
         */
        public boolean isXmlAttribute() {
            return isXmlAttribute;
        }

        public void setIndex(int index) {
            this.index = index;
            isIndexed = true;
        }
    }

    /**
     * Split X-path string in to string steps, ignoring / characters inside []
     *
     * @param s x-path string
     * @return list of string steps
     */
    private static List<String> splitPath(String s) {
        ArrayList<String> parts = new ArrayList<String>();

        StringBuffer b = new StringBuffer();
        int insideIndex = 0;
        for (int pos = 0; pos < s.length(); pos++) {
            if (s.charAt(pos) == '/' && insideIndex == 0) {
                parts.add(b.toString());
                b = new StringBuffer();
            } else {
                if (s.charAt(pos) == '[') {
                    insideIndex++;
                } else if (s.charAt(pos) == ']') {
                    insideIndex--;
                }
                b.append(s.charAt(pos));
            }
        }
        parts.add(b.toString());
        return parts;
    }

    /**
     * Returns the list of steps in an x-path expression that represents the root element.
     *
     * @param rootElement non null descriptor of the root attribute, generally the Feature
     *     descriptor.
     * @param namespaces namespace support for generating qnames from namespaces.
     * @return A list of unique of steps in an xpath expression.
     * @throws IllegalArgumentException if <code>root</code> is undefined.
     */
    public static StepList rootElementSteps(
            final AttributeDescriptor rootElement, final NamespaceSupport namespaces)
            throws IllegalArgumentException {

        if (rootElement == null) {
            throw new NullPointerException("root");
        }
        StepList steps = new StepList();
        QName qName = Types.toQName(rootElement.getName(), namespaces);
        steps.add(new Step(qName, 1, false, false));
        return steps;
    }

    /**
     * Returns the list of stepts in <code>xpathExpression</code> by cleaning it up removing
     * unnecessary elements.
     *
     * <p>
     *
     * @param root non null descriptor of the root attribute, generally the Feature descriptor. Used
     *     to ignore the first step in xpathExpression if the expression's first step is named as
     *     rootName.
     * @throws IllegalArgumentException if <code>xpathExpression</code> has no steps or it isn't a
     *     valid XPath expression against <code>type</code>.
     */
    public static StepList steps(
            final AttributeDescriptor root,
            final String xpathExpression,
            final NamespaceSupport namespaces)
            throws IllegalArgumentException {

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

        final List<String> partialSteps = splitPath(expression);

        if (partialSteps.size() == 0) {
            throw new IllegalArgumentException("no steps provided");
        }

        int startIndex = 0;

        for (int i = startIndex; i < partialSteps.size(); i++) {

            String step = partialSteps.get(i);
            if ("..".equals(step)) {
                steps.remove(steps.size() - 1);
            } else if (".".equals(step)) {
                continue;
            } else {
                int index = 1;
                boolean isXmlAttribute = false;
                boolean isIndexed = false;
                String predicate = null;
                String stepName = step;
                if (step.indexOf('[') != -1) {
                    int start = step.indexOf('[');
                    int end = step.indexOf(']');
                    stepName = step.substring(0, start);
                    String s = step.substring(start + 1, end);
                    try (Scanner scanner = new Scanner(s)) {
                        if (scanner.hasNextInt()) {
                            index = scanner.nextInt();
                            isIndexed = true;
                        } else {
                            predicate = s;
                        }
                    }
                }
                if (step.charAt(0) == '@') {
                    isXmlAttribute = true;
                    stepName = stepName.substring(1);
                }
                QName qName = deglose(stepName, root, namespaces, isXmlAttribute);
                if (predicate == null) {
                    steps.add(new Step(qName, index, isXmlAttribute, isIndexed));
                } else {
                    steps.add(new Step(qName, isXmlAttribute, predicate));
                }
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

    private static QName deglose(
            final String prefixedName,
            final AttributeDescriptor root,
            final NamespaceSupport namespaces,
            boolean isXmlAttribute) {
        if (prefixedName == null) {
            throw new NullPointerException("prefixedName");
        }

        QName name = null;

        String prefix;
        final String namespaceUri;
        final String localName;

        int prefixIdx = prefixedName.indexOf(':');

        if (prefixIdx == -1) {
            localName = prefixedName;
            final Name rootName = root.getName();
            // don't use default namespace for client properties (xml attribute), and FEATURE_LINK
            final String defaultNamespace =
                    (isXmlAttribute
                                    || localName.equals(
                                            ComplexFeatureConstants.FEATURE_CHAINING_LINK_NAME
                                                    .getLocalPart())
                                    || rootName.getNamespaceURI() == null)
                            ? XMLConstants.NULL_NS_URI
                            : namespaces.getURI("") == null
                                    ? rootName.getNamespaceURI()
                                    : namespaces.getURI("");
            namespaceUri = defaultNamespace;
            if (XMLConstants.NULL_NS_URI.equals(defaultNamespace)) {
                prefix = XMLConstants.DEFAULT_NS_PREFIX;
            } else {
                if (!localName.equals(rootName.getLocalPart())) {
                    LOGGER.fine(
                            "Using root's namespace "
                                    + defaultNamespace
                                    + " for step named '"
                                    + localName
                                    + "', as no prefix was stated");
                }
                prefix = namespaces.getPrefix(defaultNamespace);

                if (prefix == null) {
                    // throw new IllegalStateException("Default namespace is not mapped to a prefix:
                    // "
                    //        + defaultNamespace);
                    prefix = "";
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

    public static boolean equals(Name targetNodeName, StepList targetXPath) {
        if (targetXPath.size() == 1) {
            Step rootStep = targetXPath.get(0);
            QName stepName = rootStep.getName();
            if (Types.equals(targetNodeName, stepName)) {
                return true;
            }
        }
        return false;
    }
}
