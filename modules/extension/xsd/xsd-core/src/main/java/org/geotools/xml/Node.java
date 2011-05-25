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
package org.geotools.xml;

import java.util.List;


/**
 * Represents a value in the parse tree. A node has a corresponds to a
 * particular instance component of a document (element or attribute). Each node
 * contains a parsed value, as well as a reference to the instance.
 *
 * @author Justin Deoliveira,Refractions Research Inc.,jdeolive@refractions.net
 *
 *
 *
 * @source $URL$
 */
public interface Node {
    /**
     * @return The component instance.
     */
    InstanceComponent getComponent();

    /**
     * @return The parsed value of the instance component.
     */
    Object getValue();

    /**
     * Sets the value of the node.
     *
     * @param value The new parse value.
     */
    void setValue(Object value);

    /**
     * Determines if the node has a child with the specified name.
     *
     * @param name The name of a child node.
     *
     * @return <code>true</code> if a child node exists with the name, otehrwise <code>false</code>.
     */
    boolean hasChild(String name);

    /**
     * Determines if the node has a child whose value is of the specified class.
     *
     * @param clazz The class of the child node value.
     *
     * @return <code>true</code> if a child node exists with the class, otherwise <code>false</code>.
     */
    boolean hasChild(Class clazz);

    /**
     * Returns all nodes corresponding child elements.
     *
     * @return A list containing objects of type Node.
     */
    List getChildren();

    /**
     * Returns all nodes corresponding child elements with the specified name.
     * This method returns an empty list if it finds no nodes corresponding to
     * the specified name.
     *
     * @param name The name of a child element.
     *
     * @return A list containing objects of type Node.
     */
    List getChildren(String name);

    /**
     * Returns all nodes corresponding to child elements with the parsed values
     * that are instances of <code>clazz</code>
     *
     * @param clazz The class of parsed child values.
     *
     * @return A list containing objects of type Node, such that node#getValue()
     * is an instance of <code>clazz<code>, or an empty list.
     */
    List getChildren(Class clazz);

    /**
     * Returns a node corresponding to a child element. This method returns the
     * first such node it finds and no order is guaranteed, it is provided for
     * convenience. This method returns null if it finds no such child node
     * matching the specified name.
     *
     * @param name The name of a child element.
     *
     * @return The first node that matches a child element with the specified
     * name.
     */
    Node getChild(String name);

    /**
     * Returns a node corresponding to a child element which has a parsed value
     * which is an instance of <code>clazz</code>. This method returns the
     * first such node it finds and no order is guarenteed, it is providedd
     * for convenience. This method returns <code>null</codee> if it finds no
     * such child mathing the above criteria.
     *
     * @param clazz The class of the parsed value of a child element.
     *
     * @return The first node found, or null.
     */
    Node getChild(Class clazz);

    /**
     * Helper method for access to child's parsed contents.
     * <p>
     * Should be in the range of getChildren().size()
     * <p>
     * Simple helper method for the contents of getChildren:
     * <code>
     * return ((Node)getChildren.get( index )).getValue();
     * </code>
     * </p>
     * @param index
     * @return the value of the child at the given index
     */
    Object getChildValue(int index);

    /**
     * Helper method for access to child's parsed contents by element name.
     * <p>
     * In the event that the node has multiple children mathing <code>name</name>
     * the first encountered is returned, there is no guarantee of order. For a
     * list of all values matching name use {@link #getChildValues(String)}.
     * </p>
     *
     * @param name The name of the child which parsed content is to be retrived.
     *
     * @return the value of the child with the given name, or <code>null</code>
     * if the child does not exist.
     */
    Object getChildValue(String name);

    /**
     * Helper method for access to child's parsed contents by element name.returning
     * a <tt>defaultValue</tt> when no such value is present
     * <p>
     * In the event that the node has multiple children mathing <code>name</name>
     * the first encountered is returned, there is no guarantee of order. For a
     * list of all values matching name use {@link #getChildValues(String)}.
     * </p>
     *
     * @param name The name of the child which parsed content is to be retrived.
     * @param defaultValue A defaultValue to return, if no such child found.
     *
     * @return the value of the child with the given name, or
     * <code>defaultValue</code> if the child does not exist.
     */
    Object getChildValue(String name, Object defaultValue);

    /**
     * Helper method for access to child's parsed contents by class.
     * <p>
     * In the event that the node has multiple children which are instances of
     * <code>clazz</code>, the first is returned, there is no guarantee of
     * order. For a list of all values which are instances of <code>clazz</code>
     * use {@link #getChildValues(Class)}.
     * </p>
     *
     * @param clazz The class of parsed child value.
     *
     * @return the value of the child which is an instance of <code>clazz</code>,
     * or <code>null</code> if no such child exists.
     */
    Object getChildValue(Class clazz);

    /**
     * Helper method for access to child's parsed contents by class, returning a
     * <tt>defaultValue</tt> when no such value is present
     * <p>
     * In the event that the node has multiple children which are instances of
     * <code>clazz</code>, the first is returned, there is no guarantee of
     * order. For a list of all values which are instances of <code>clazz</code>
     * use {@link #getChildValues(Class)}.
     * </p>
     *
     * @param clazz The class of parsed child value.
     * @param defaultValue A defaultValue to return, if no such child found.
     *
     * @return the value of the child which is an instance of <code>clazz</code>,
     * or <code>defaultValue</code> if no such child exists.
     */
    Object getChildValue(Class clazz, Object defaultValue);

    /**
     * Helper method for access to the set of parse child values with the
     * specified name.
     *
     * @param name The name of the child element in which to retreive the
     * parsed value.
     *
     * @return A list of values representing the parsed values of the children,
     * or an empty list of no such values exist.
     */
    List getChildValues(String name);

    /**
     * Helper method for access to the set of parsed child values which are
     * instances of the specified class.
     *
     * @param clazz The class of the child values.
     *
     * @return A list of child values which are instances of <code>class<code>,
     * or an empty list if no such values exist.
     */
    List getChildValues(Class clazz);

    /**
     * Determines if the node has an attribute with the specified name.
     *
     * @param name The name of an attribute
     *
     * @return <code>true</code> if am attribute exists with the name, otehrwise <code>false</code>.
     */
    boolean hasAttribute(String name);

    /**
     * Determines if the node has an attribute whose value is of the specified class.
     *
     * @param clazz The class of the attribute value
     *
     * @return <code>true</code> if an attribute exists with the class, otherwise <code>false</code>.
     */
    boolean hasAttribute(Class clazz);

    /**
     * Returns all nodes corresponding to attributes.
     *
     * @return A list containing objects of type node.
     */
    List getAttributes();

    /**
     * Returns all nodes corresponding to attributes which has a parsed values
     * which are instances of <code>clazz</code>.
     *
     * @param clazz The class of parsed attribute values.
     *
     * @return A list of  attribute nodes whose parsed values are instances of
     * <code>clazz</code>, or an empty list.
     */
    List getAttributes(Class clazz);

    /**
     * Returns the node corresonding to the attribute with the specified name.
     * This method returns null if it finds no such attribute node matching the
     * specified name.
     *
     * @param name The name of the attribute.
     *
     */
    Node getAttribute(String name);

    /**
     * Returns the node corresponding to the attribute which has a parsed value
     * which is an instance of <code>clazz</code>. In the event that the node
     * contains multple attributes matching the above criteria, the first
     * encountered is returned, with no guaratnee of order. For all nodes
     * matching this criteria use {@link #getAttributes(Class)}.
     *
     * @param clazz The class of parsed attribute values.
     *
     * @return The attribute node whose parsed value is an instance of
     * <code>clazz</code>, or <code>null</code> if no such node exists.
     */
    Node getAttribute(Class clazz);

    /**
     * Helper method for access to the parsed value of the attribute with
     * the specified name.
     *
     * @param name The name of the attribute in which to retreive the parsed
     * value from.
     *
     * @return the parsed value of the attribute matching the criteria, or
     * <code>null</code> if no such attribute is found.
     */
    Object getAttributeValue(String name);

    /**
     * Helper method for access to the parsed value of the attribute with
     * the specified name, returning a <code>defaultValue</code> when no such
     * attribute is present
     *
     * @param name The name of the attribute in which to retreive the parsed
     * value from.
     * @param defaultValue A defaultValue to return, if no such attribute
     * found.
     *
     * @return the parsed value of the attribute matching the criteria, or
     * <code>defaultValue</code> if no such attribute is found.
     */
    Object getAttributeValue(String name, Object defaultValue);

    /**
     * Helper method for access to the parsed value of the attribute whose
     * parsed value is an instance of <code>clazz</code>. In the event that the
     * node contains multple attributes matching the above criteria, the first
     * encountered is returned, with no guaratnee of order. For all values
     * matching this criteria use {@link #getAttributeValues(Class)}.
     *
     * @param clazz The class of parsed attribute values.
     *
     * @return the parsed value of the attribute matching the criteria, or
     * <code>null</code> if no such attribute is found.
     */
    Object getAttributeValue(Class clazz);

    /**
     * Helper method for access to the parsed value of the attribute whose
     * parsed value is an instance of <code>clazz</code>, returning a
     * <code>defaultValue</code> when no such attribute is present. In the event
     * that the node contains multple attributes matching the above criteria, the
     * first encountered is returned, with no guaratnee of order. For all values
     * matching this criteria use {@link #getAttributeValues(Class)}.
     *
     * @param clazz The class of parsed attribute values.
     * @param defaultValue A defaultValue to return, if no such attribute
     * found.
     *
     * @return the parsed value of the attribute matching the criteria, or
     * <code>defaultValue</code> if no such attribute is found.
     */
    Object getAttributeValue(Class clazz, Object defaultValue);

    /**
     * Helper method for access ot the parsed values of attribute nodes whose
     * parsed values are instances of <code>clazz</code>.
     *
     * @param clazz The class of parsed attribute values.
     *
     * @return The list of attribute values which are instances of
     * <code>clazz</code>, or an empty list.
     */
    List getAttributeValues(Class clazz);
}
