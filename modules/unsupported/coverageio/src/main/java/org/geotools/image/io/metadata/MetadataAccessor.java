/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io.metadata;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.geotools.resources.Classes;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.OptionalDependencies;
import org.geotools.util.UnsupportedImplementationException;


/**
 * Base class for {@linkplain GeographicMetadata geographic metadata} parsers. This class
 * provides convenience methods for encoding and decoding metadata information. A metadata
 * root {@linkplain Node node} is specified at construction time, together with a path to
 * the {@linkplain Element element} of interest. Example of valid paths:
 * <p>
 * <ul>
 *   <li>{@code "CoordinateReferenceSystem/Datum"}</li>
 *   <li>{@code "CoordinateReferenceSystem/CoordinateSystem"}</li>
 *   <li>{@code "GridGeometry/Envelope"}</li>
 * </ul>
 * <p>
 * In addition, some elements contains an arbitrary amount of childs. The path to child
 * elements can also be specified to the constructor. Examples (note that the constructor
 * expects paths relative to the parent; we show absolute paths below for completness):
 * <p>
 * <ul>
 *   <li>{@code "CoordinateReferenceSystem/CoordinateSystem/Axis"}</li>
 *   <li>{@code "GridGeometry/Envelope/CoordinateValues"}</li>
 *   <li>{@code "SampleDimensions/SampleDimension"}</li>
 * </ul>
 *
 * The {@code get} and {@code set} methods defined in this class will operate on the
 * <cite>selected</cite> {@linkplain Element element}, which may be either the one
 * specified at construction time, or one of its childs. The element can be selected
 * by {@link #selectParent} (the default) or {@link #selectChild}.
 * <p>
 * The example below creates an accessor for a node called {@code "CoordinateSystem"}
 * which is expected to have childs called {@code "Axis"}:
 *
 * <blockquote><pre>
 * MetadataAccessor accessor = new MetadataAccessor(metadata,
 *         "CoordinateReferenceSystem/CoordinateSystem", "Axis");
 *
 * accessor.selectParent();
 * String csName = accessor.getAttributeAsString("name");
 *
 * accessor.selectChild(0);
 * String firstAxisName = accessor.getAttributeAsString("name");
 * </pre></blockquote>
 *
 * @since 2.4
 * @version $Id$
 *
 * @source $URL$
 * @author Martin Desruisseaux
 * @author Cédric Briançon
 */
public class MetadataAccessor {
    /**
     * The separator between names in a node path.
     */
    private static final char SEPARATOR = '/';

    /**
     * The owner of this accessor.
     */
    final GeographicMetadata metadata;

    /**
     * The parent of child {@linkplain Element elements}.
     */
    private final Node parent;

    /**
     * The {@linkplain #childs} path. This is the {@code childPath} parameter
     * given to the constructor.
     */
    private final String childPath;

    /**
     * The list of child elements. May be empty but never null.
     */
    private final List<Node> childs;

    /**
     * The current element, or {@code null} if not yet selected.
     *
     * @see #selectChild
     * @see #currentElement()
     */
    private transient Element current;

    /**
     * {@code true} if warnings are enabled.
     */
    private transient boolean warningsEnabled = true;

    /**
     * Creates an accessor with the same parent and childs than the specified one. The two
     * accessors will share the same {@linkplain Node metadata nodes} (including the list
     * of childs), so change in one accessor will be immediately reflected in the other
     * accessor. However each accessor can {@linkplain #selectChild select their child}
     * independently.
     * <p>
     * The main purpose of this constructor is to create many views over the same list
     * of childs, where each view {@linkplain #selectChild select} a different child.
     */
    protected MetadataAccessor(final MetadataAccessor clone) {
        metadata  = clone.metadata;
        parent    = clone.parent;
        childPath = clone.childPath;
        childs    = clone.childs;
    }

    /**
     * Creates an accessor for the {@linkplain Element element} at the given path. Paths are
     * separated by the {@code '/'} character. See {@linkplain MetadataAccessor class javadoc}
     * for path examples.
     *
     * @param  metadata   The metadata node.
     * @param  parentPath The path to the {@linkplain Node node} of interest, or {@code null}
     *                    if the {@code metadata} root node is directly the node of interest.
     * @param  childPath  The path (relative to {@code parentPath}) to the child
     *                    {@linkplain Element elements}, or {@code null} if none.
     */
    @SuppressWarnings("fallthrough")
    protected MetadataAccessor(final GeographicMetadata metadata, final String parentPath, final String childPath) {
        this.metadata = metadata;
        final Node root = metadata.getRootNode();
        /*
         * Fetchs the parent node and ensure that we got a singleton. If there is more nodes than
         * expected, log a warning and pickup the first one. If there is no node, create a new one.
         */
        final List<Node> childs = new ArrayList<Node>(4);
        if (parentPath != null) {
            listChilds(root, parentPath, 0, childs, true);
            final int count = childs.size();
            switch (count) {
                default: {
                    warning("<init>", ErrorKeys.TOO_MANY_OCCURENCES_$2,
                            new Object[] {parentPath, count});
                    // Fall through for picking the first node.
                }
                case 1: {
                    parent = childs.get(0);
                    childs.clear();
                    break;
                }
                case 0: {
                    parent = appendChild(root, parentPath);
                    break;
                }
            }
        } else {
            parent = root;
        }
        /*
         * Computes a full path to children. Searching from 'metadata' root node using 'path'
         * should be identical to searching from 'parent' node using 'childPath', except in
         * case of badly formed metadata where the parent node appears more than once.
         */
        this.childPath = childPath;
        if (childPath != null) {
            final String path;
            if (parentPath != null) {
                path = parentPath + SEPARATOR + childPath;
            } else {
                path = childPath;
            }
            listChilds(root, path, 0, childs, false);
            this.childs = childs;
        } else {
            this.childs = Collections.emptyList();
        }
        if (parent instanceof Element) {
            current = (Element) parent;
        }
    }

    /**
     * Adds to the {@link #childs} list the child nodes at the given {@code path}.
     * This method is for constructor implementation only and invokes itself recursively.
     *
     * @param  parent The parent metadata node.
     * @param  path   The path to the nodes or elements to insert into the list.
     * @param  base   The offset in {@code path} for the next element name.
     * @param  childs The list where to insert the nodes or elements.
     * @param  includeNodes {@code true} of adding nodes as well as elements.
     */
    private static void listChilds(final Node parent, final String path, final int base,
                                   final List<Node> childs, final boolean includeNodes)
    {
        final int upper = path.indexOf(SEPARATOR, base);
        final String name = ((upper >= 0) ? path.substring(base, upper)
                                          : path.substring(base)).trim();
        final NodeList list = parent.getChildNodes();
        final int length = list.getLength();
        for (int i=0; i<length; i++) {
            final Node candidate = list.item(i);
            if (name.equals(candidate.getNodeName())) {
                if (upper >= 0) {
                    listChilds(candidate, path, upper+1, childs, includeNodes);
                } else if (includeNodes || (candidate instanceof Element)) {
                    // For the very last node, we may require an element.
                    childs.add(candidate);
                }
            }
        }
    }

    /**
     * Appends a child to the given parent.
     *
     * @param parent   The parent to add a child to.
     * @param path     The path of the child to add.
     * @return element The new child.
     */
    private static Node appendChild(Node parent, final String path) {
        int lower = 0;
search: for (int upper; (upper = path.indexOf(SEPARATOR, lower)) >= 0; lower=upper+1) {
            final String name = path.substring(lower, upper).trim();
            final NodeList list = parent.getChildNodes();
            final int length = list.getLength();
            for (int i=length; --i>=0;) {
                final Node candidate = list.item(i);
                if (name.equals(candidate.getNodeName())) {
                    parent = candidate;
                    continue search;
                }
            }
            parent = parent.appendChild(new IIOMetadataNode(name.intern()));
        }
        final String name = path.substring(lower).trim().intern();
        return parent.appendChild(new IIOMetadataNode(name));
    }

    /**
     * Returns the number of child {@linkplain Element elements}.
     * This is the upper value (exclusive) for {@link #selectChild}.
     *
     * @return The child {@linkplain Element elements} count.
     *
     * @see #selectChild
     * @see #appendChild
     */
    protected int childCount() {
        return childs.size();
    }

    /**
     * Adds a new child {@linkplain Element element} at the path given at construction time.
     * The {@linkplain #childCount child count} will be increased by 1.
     * <p>
     * The new child is <strong>not</strong> automatically selected. In order to select this
     * new child, the {@link #selectChild} method must be invoked explicitly.
     *
     * @return The index of the new child element.
     *
     * @see #childCount
     * @see #selectChild
     */
    protected int appendChild() {
        final int size = childs.size();
        final Node child = appendChild(parent, childPath);
        if (child instanceof Element) {
            childs.add((Element) child);
            return size;
        } else {
            throw new UnsupportedImplementationException(child.getClass());
        }
    }

    /**
     * Selects the {@linkplain Element element} at the given index. Every subsequent calls
     * to {@code get} or {@code set} methods will apply to this selected child element.
     *
     * @param index The index of the element to select.
     * @throws IndexOutOfBoundsException if the specified index is out of bounds.
     *
     * @see #childCount
     * @see #appendChild
     * @see #selectParent
     */
    protected void selectChild(final int index) throws IndexOutOfBoundsException {
        current = (Element) childs.get(index);
    }

    /**
     * Selects the <em>parent</em> of child elements. Every subsequent calls to {@code get}
     * or {@code set} methods will apply to this parent element.
     *
     * @throws NoSuchElementException if there is no parent {@linkplain Element element}.
     *
     * @see #selectChild
     */
    protected void selectParent() throws NoSuchElementException {
        if (parent instanceof Element) {
            current = (Element) parent;
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * Returns the current element.
     *
     * @return The currently selected element.
     * @throws IllegalStateException if there is no selected element.
     *
     * @see #selectChild
     */
    private Element currentElement() throws IllegalStateException {
        if (current == null) {
            throw new IllegalStateException();
        }
        return current;
    }

    /**
     * Returns the {@linkplain IIOMetadataNode#getUserObject user object} associated with the
     * {@linkplain #selectChild selected element}, or {@code null} if none. If no user object
     * is defined for the element, then the {@linkplain Node#getNodeValue node value} is returned
     * as a fallback. This is consistent with {@link #setUserObject} implementation, and allows
     * some parsing of nodes that are not {@link IIOMetadataNode} instances.
     * <p>
     * The {@code getUserObject} methods are the only ones to not parse the value returned by
     * {@link #getAttributeAsString}.
     *
     * @return The user object, or {@code null} if none.
     *
     * @see #getUserObject(Class)
     * @see #setUserObject
     */
    protected Object getUserObject() {
        final Element element = currentElement();
        if (element instanceof IIOMetadataNode) {
            final Object candidate = ((IIOMetadataNode) element).getUserObject();
            if (candidate != null) {
                return candidate;
            }
        }
        /*
         * getNodeValue() returns a String. We use it as a fallback, but in typical
         * IIOMetadataNode usage this value is not used (according its javadoc), so
         * it will often be null.
         */
        return element.getNodeValue();
    }

    /**
     * Returns the user object associated as an instance of the specified class. If the value
     * returned by {@link #getUserObject()} is not of the expected type, then this method will
     * tries to parse it as a string.
     *
     * @param  type The expected class.
     * @return The user object, or {@code null} if none.
     * @throws ClassCastException if the user object can not be casted to the specified type.
     *
     * @see #getUserObject()
     * @see #setUserObject
     */
    protected <T> T getUserObject(Class<? extends T> type) throws ClassCastException {
        type = Classes.primitiveToWrapper(type).asSubclass(type);
        Object value = getUserObject();
        if (value instanceof CharSequence) {
            if (Number.class.isAssignableFrom(type)) {
                value = Classes.valueOf(type, value.toString());
            } else {
                final Class<?> component = Classes.primitiveToWrapper(type.getComponentType());
                if (Double.class.equals(component)) {
                    value = parseSequence(value.toString(), false, false);
                } else if (Integer.class.equals(component)) {
                    value = parseSequence(value.toString(), false, true);
                }
            }
        }
        return type.cast(value);
    }

    /**
     * Sets the {@linkplain IIOMetadataNode#setUserObject user object} associated with the
     * {@linkplain #selectChild selected element}. This is the only {@code set} method that
     * doesn't invoke {@link #setAttributeAsString} with a formatted value.
     * <p>
     * If the specified value is formattable (i.e. is a {@linkplain CharSequence character
     * sequence}, a {@linkplain Number number} or an array of the above), then this method
     * also {@linkplain IIOMetadataNode#setNodeValue sets the node value} as a string. This
     * is mostly a convenience for formatting purpose since {@link IIOMetadataNode} don't
     * use the node value. But it may help some libraries that are not designed to work with
     * with user objects, since they are particular to Image I/O metadata.
     *
     * @param  value The user object, or {@code null} if none.
     * @throws UnsupportedImplementationException if the selected element is not an instance of
     *         {@link IIOMetadataNode}.
     *
     * @see #getUserObject()
     */
    protected void setUserObject(final Object value) throws UnsupportedImplementationException {
        final Element element = currentElement();
        String asText = null;
        if (value != null) {
            final Class<?> type = value.getClass();
            if (isFormattable(type)) {
                asText = value.toString();
            } else if (isFormattable(type.getComponentType())) {
                asText = formatSequence(value);
            }
        }
        if (element instanceof IIOMetadataNode) {
            ((IIOMetadataNode) element).setUserObject(value);
        } else if (value!=null && asText==null) {
            throw new UnsupportedImplementationException(Errors.format(ErrorKeys.ILLEGAL_CLASS_$2,
                    Classes.getClass(element), IIOMetadataNode.class));
        }
        element.setNodeValue(asText);
    }

    /**
     * Returns {@code true} if values of the specified type can be formatted as a
     * text. We allows formatting only for reasonably cheap objects, for example
     * a Number but not a CoordinateReferenceSystem.
     */
    private static boolean isFormattable(final Class<?> type) {
        return CharSequence.class.isAssignableFrom(type) ||
               Number.class.isAssignableFrom(Classes.primitiveToWrapper(type));
    }

    /**
     * Returns an attribute as a string for the {@linkplain #selectChild selected element},
     * or {@code null} if none. This method never returns an empty string.
     * <p>
     * Every {@code get} methods in this class except {@link #getUserObject getUserObject}
     * invoke this method first. Consequently, this method provides a single point for
     * overriding if subclasses want to process the attribute before parsing.
     *
     * @param attribute The attribute to fetch (e.g. {@code "name"}).
     * @return The attribute value (never an empty string), or {@code null} if none.
     */
    protected String getAttributeAsString(final String attribute) {
        String candidate = currentElement().getAttribute(attribute);
        if (candidate != null) {
            candidate = candidate.trim();
            if (candidate.length() == 0) {
                candidate = null;
            }
        }
        return candidate;
    }

    /**
     * Set the attribute to the specified value,
     * or remove the attribute if the value is null.
     * <p>
     * Every {@code set} methods in this class except {@link #setUserObject setUserObject}
     * invoke this method last. Consequently, this method provides a single point for
     * overriding if subclasses want to process the attribute after formatting.
     *
     * @param attribute The attribute name.
     * @param value     The attribute value.
     */
    protected void setAttributeAsString(final String attribute, String value) {
        final Element element = currentElement();
        if (value == null || (value=value.trim()).length() == 0) {
            if (element.hasAttribute(attribute)) {
                element.removeAttribute(attribute);
            }
        } else {
            element.setAttribute(attribute, value);
        }
    }

    /**
     * Set the attribute to the specified enumeration value,
     * or remove the attribute if the value is null.
     *
     * @param attribute The attribute name.
     * @param value     The attribute value.
     * @param enums     The set of allowed values, or {@code null} if unknown.
     */
    final void setAttributeAsEnum(final String attribute, String value, final Collection enums) {
        if (value != null) {
            value = value.replace('_', ' ').trim();
            for (final Iterator it=enums.iterator(); it.hasNext();) {
                final String e = (String) it.next();
                if (value.equalsIgnoreCase(e)) {
                    value = e;
                    break;
                }
            }
        }
        setAttributeAsString(attribute, value);
    }

    /**
     * Returns an attribute as an integer for the {@linkplain #selectChild selected element},
     * or {@code null} if none. If the attribute can't be parsed as an integer, then this method
     * logs a warning and returns {@code null}.
     *
     * @param attribute The attribute to fetch (e.g. {@code "minimum"}).
     * @return The attribute value, or {@code null} if none or unparseable.
     */
    protected Integer getAttributeAsInteger(final String attribute) {
        String value = getAttributeAsString(attribute);
        if (value != null) {
            value = trimFractionalPart(value);
            try {
                return Integer.valueOf(value);
            } catch (NumberFormatException e) {
                warning("getAttributeAsInteger", ErrorKeys.UNPARSABLE_NUMBER_$1, value);
            }
        }
        return null;
    }

    /**
     * Set the attribute to the specified integer value.
     *
     * @param attribute The attribute name.
     * @param value     The attribute value.
     */
    protected void setAttributeAsInteger(final String attribute, final int value) {
        setAttributeAsString(attribute, Integer.toString(value));
    }

    /**
     * Returns an attribute as an array of integers for the {@linkplain #selectChild selected
     * element}, or {@code null} if none. If an element can't be parsed as an integer, then this
     * method logs a warning and returns {@code null}.
     *
     * @param attribute The attribute to fetch (e.g. {@code "minimum"}).
     * @param unique {@code true} if duplicated values should be collapsed into unique values,
     *         or {@code false} for preserving duplicated values.
     * @return The attribute values, or {@code null} if none.
     */
    protected int[] getAttributeAsIntegers(final String attribute, final boolean unique) {
        return (int[]) parseSequence(getAttributeAsString(attribute), unique, true);
    }

    /**
     * Set the attribute to the specified array of values,
     * or remove the attribute if the array is {@code null}.
     *
     * @param attribute The attribute name.
     * @param value     The attribute value.
     */
    protected void setAttributeAsIntegers(final String attribute, final int[] values) {
        setAttributeAsString(attribute, formatSequence(values));
    }

    /**
     * Returns an attribute as a floating point for the {@linkplain #selectChild selected element},
     * or {@code null} if none. If the attribute can't be parsed as a floating point, then this
     * method logs a warning and returns {@code null}.
     *
     * @param attribute The attribute to fetch (e.g. {@code "minimum"}).
     * @return The attribute value, or {@code null} if none or unparseable.
     */
    protected Double getAttributeAsDouble(final String attribute) {
        final String value = getAttributeAsString(attribute);
        if (value != null) try {
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
            warning("getAttributeAsDouble", ErrorKeys.UNPARSABLE_NUMBER_$1, value);
        }
        return null;
    }

    /**
     * Set the attribute to the specified floating point value,
     * or remove the attribute if the value is NaN.
     *
     * @param attribute The attribute name.
     * @param value     The attribute value.
     */
    protected void setAttributeAsDouble(final String attribute, final double value) {
        String text = null;
        if (!Double.isNaN(value) && !Double.isInfinite(value)) {
            text = Double.toString(value);
        }
        setAttributeAsString(attribute, text);
    }

    /**
     * Returns an attribute as an array of floating point for the {@linkplain #selectChild
     * selected element}, or {@code null} if none. If an element can't be parsed as a floating
     * point, then this method logs a warning and returns {@code null}.
     *
     * @param  attribute The attribute to fetch (e.g. {@code "fillValues"}).
     * @param  unique {@code true} if duplicated values should be collapsed into unique values,
     *         or {@code false} for preserving duplicated values.
     * @return The attribute values, or {@code null} if none.
     */
    protected double[] getAttributeAsDoubles(final String attribute, final boolean unique) {
        return (double[]) parseSequence(getAttributeAsString(attribute), unique, false);
    }

    /**
     * Set the attribute to the specified array of values,
     * or remove the attribute if the array is {@code null}.
     *
     * @param attribute The attribute name.
     * @param value     The attribute value.
     */
    protected void setAttributeAsDoubles(final String attribute, final double[] values) {
        setAttributeAsString(attribute, formatSequence(values));
    }

    /**
     * Implementation of {@link #getAttributeAsIntegers} and {@link #getAttributeAsDoubles} methods.
     *
     * @param  sequence The sequence to parse.
     * @param  unique {@code true} if duplicated values should be collapsed into unique values,
     *         or {@code false} for preserving duplicated values.
     * @param  integers {@code true} for parsing as {@code int}, or {@code false} for parsing as
     *         {@code double}.
     * @return The attribute values, or {@code null} if none.
     */
    private Object parseSequence(final String sequence, final boolean unique, final boolean integers) {
        if (sequence == null) {
            return null;
        }
        final Collection<Number> numbers;
        if (unique) {
            numbers = new LinkedHashSet<Number>();
        } else {
            numbers = new ArrayList<Number>();
        }
        final StringTokenizer tokens = new StringTokenizer(sequence);
        while (tokens.hasMoreTokens()) {
            final String token = tokens.nextToken();
            final Number number;
            try {
                if (integers) {
                    number = Integer.valueOf(token);
                } else {
                    number = Double.valueOf(token);
                }
            } catch (NumberFormatException e) {
                warning(integers ? "getAttributeAsIntegers" : "getAttributeAsDoubles", ErrorKeys.UNPARSABLE_NUMBER_$1, token);
                continue;
            }
            numbers.add(number);
        }
        int count = 0;
        final Object values;
        if (integers) {
            values = new int[numbers.size()];
        } else {
            values = new double[numbers.size()];
        }
        for (final Iterator it=numbers.iterator(); it.hasNext();) {
            Array.set(values, count++, it.next());
        }
        assert Array.getLength(values) == count;
        return values;
    }

    /**
     * Formats a sequence for {@link #setAttributeAsIntegers} and {@link #setAttributeAsDoubles} implementations.
     *
     * @param  value The attribute value.
     * @return The formatted sequence.
     */
    private static String formatSequence(final Object values) {
        String text = null;
        if (values != null) {
            final StringBuilder buffer = new StringBuilder();
            final int length = Array.getLength(values);
            for (int i=0; i<length; i++) {
                if (i != 0) {
                    buffer.append(' ');
                }
                buffer.append(Array.get(values, i));
            }
            text = buffer.toString();
        }
        return text;
    }

    /**
     * Returns an attribute as a date for the {@linkplain #selectChild selected element},
     * or {@code null} if none. If the attribute can't be parsed as a date, then this method
     * logs a warning and returns {@code null}.
     *
     * @param attribute The attribute to fetch (e.g. {@code "origin"}).
     * @return The attribute value, or {@code null} if none or unparseable.
     */
    protected Date getAttributeAsDate(final String attribute) {
        String value = getAttributeAsString(attribute);
        if (value != null) {
            value = trimFractionalPart(value);
            return metadata.dateFormat().parse(value);
        }
        return null;
    }

    /**
     * Set the attribute to the specified value, or remove the attribute if the value is null.
     *
     * @param attribute The attribute name.
     * @param value     The attribute value.
     */
    protected void setAttributeAsDate(final String attribute, final Date value) {
        String text = null;
        if (value != null) {
            text = metadata.dateFormat().format(value);
        }
        setAttributeAsString(attribute, text);
    }

    /**
     * Trims the factional part of the given string, provided that it doesn't change the value.
     * More specifically, this method removes the trailing {@code ".0"} characters if any. This
     * method is automatically invoked before to {@linkplain #getAttributeAsInteger parse an integer} or to
     * {@linkplain #getAttributeAsDate parse a date} (for simplifying fractional seconds).
     *
     * @param  value The value to trim.
     * @return The value without the trailing {@code ".0"} part.
     */
    public static String trimFractionalPart(String value) {
        value = value.trim();
        for (int i=value.length(); --i>=0;) {
            switch (value.charAt(i)) {
                case '0': continue;
                case '.': return value.substring(0, i);
                default : return value;
            }
        }
        return value;
    }

    /**
     * Convenience method for logging a warning. Do not allow overriding, because
     * it would not work for warnings emitted by the {@link #getAttributeAsDate} method.
     */
    final void warning(final String method, final int key, final Object value) {
        if (warningsEnabled) {
            final LogRecord record = Errors.getResources(metadata.getLocale()).
                    getLogRecord(Level.WARNING, key, value);
            record.setSourceClassName(MetadataAccessor.class.getName());
            record.setSourceMethodName(method);
            warningOccurred(record);
        }
    }

    /**
     * Invoked when a warning occured. This method is invoked when some inconsistency has
     * been detected in the geographic metadata. The default implementation delegates
     * to {@link GeographicMetadata#warningOccurred}.
     */
    protected void warningOccurred(final LogRecord record) {
        if (warningsEnabled) {
            metadata.warningOccurred(record);
        }
    }

    /**
     * Enables or disables the warnings. Warnings are enabled by default. Subclasses way want
     * to temporarily disable the warnings when failures are expected as the normal behavior.
     * For example a subclass may invokes {@link #getAttributeAsInteger} and fallbacks on {@link #getAttributeAsDouble}
     * if the former failed. In such case, the warnings should be disabled for the integer parsing,
     * but not for the floating point parsing.
     *
     * @param  enabled {@code true} for enabling warnings, or {@code false} for disabling.
     * @return The previous state before this method has been invoked.
     */
    protected boolean setWarningsEnabled(final boolean enabled) {
        final boolean old = warningsEnabled;
        warningsEnabled = enabled;
        return old;
    }

    /**
     * Returns a string representation of metadata, mostly for debugging purpose.
     */
    @Override
    public String toString() {
        return OptionalDependencies.toString(OptionalDependencies.xmlToSwing(parent));
    }
}
