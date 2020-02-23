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
package org.geotools.xsd;

import java.util.List;
import javax.xml.namespace.QName;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDParticle;
import org.picocontainer.MutablePicoContainer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A strategy for parsing elements in an instance document which are of complex type.
 *
 * <p>Complex types contain child elements, and attributes. A complex strategy has the ability to
 *
 * <p>
 *
 * @author Justin Deoliveira,Refractions Research Inc.,jdeolive@refractions.net
 */
public interface ComplexBinding extends Binding {
    /**
     * Callback for the binding to initialize itself.
     *
     * <p>This method is called when the leading edge of the associated element is reached, before
     * any children have been processed.
     *
     * @param instance The element being parsed.
     * @param node The node in the parse tree representing the element being parsed. It is important
     *     to note that at the time this method is called the node contains no child element nodes,
     *     only child attribute nodes.
     * @param context The container to be used as context for child binding.
     */
    void initialize(ElementInstance instance, Node node, MutablePicoContainer context);

    /**
     * Initializes the context for a child element.
     *
     * <p>This method is called on the leading edge of a child element. It is used to create context
     * for the binding of a child element. It is important to note that each time this method is
     * called, the <param>node</param> parse tree will contain different, ie child nodes for those
     * previous elements parsed.
     *
     * @param childInstance The child element instance
     * @param node The parse node for the parent element.
     * @param context the context in which the child element will be parsed.
     */
    void initializeChildContext(
            ElementInstance childInstance, Node node, MutablePicoContainer context);

    /**
     * Parses a complex element from an instance document into an object representation.
     *
     * <p>This method is called when the trailing edge of the associated element is reached.
     *
     * @param instance The element being parsed.
     * @param node The node in the parse tree representing the element being parsed.
     * @param value The result of the parse from another strategy in the type hierarchy. Could be
     *     null if this is the first strategy being executed.
     * @return The parsed object, or null if the component could not be parsed.
     * @throws Exception Strategy objects should not attempt to handle any exceptions.
     */
    Object parse(ElementInstance instance, Node node, Object value) throws Exception;

    /**
     * Performs the encoding of the object into its xml representation.
     *
     * <p>Complex objects are encoded as elements in a document. The <param>value</param> parameter
     * is the encoded element, created by the parent binding. For the first binding in the execution
     * chain this is just an empty element ( no children or attributes ). The binding has the choice
     * to return <param>value</param> or to create a new element to return.
     *
     * <p>This method may choose to create child elements and attributes for the element. Or as an
     * alternative return the object values for these contructs in {@link #getProperty(Object,
     * QName)}.
     *
     * @param object The object being encoded.
     * @param document The document containing the encoded element.
     * @param value The object as encoded by the parent binding.
     * @return The element for the objcet being encoded, or <code>null</code>
     */
    Element encode(Object object, Document document, Element value) throws Exception;

    /**
     * Returns a property of a particular object which corresponds to the specified name.
     *
     * <p>This method should just return null in the event that the object being encoded is an leaf
     * in its object model.
     *
     * <p>For multi-values properties ( maxOccurs > 0 ), this method may return an instance of
     * {@link java.util.Collection}, {@link java.util.Iterator}, or an array.
     *
     * @param object The object being encoded.
     * @param name The name of the property to obtain.
     * @return The value of the property, or <code>null</code>.
     */
    Object getProperty(Object object, QName name) throws Exception;

    /**
     * Returns a list of properties of the specified object.
     *
     * <p>The return list contains a set of tuples (two element object array) which represent the
     * properties of the object. The second value is an object which respresents the value. The
     * first value of the tuple can one of two things:
     *
     * <ol>
     *   <li>A {@link QName} identifying an element. This name will be used to locate the schema
     *       element for the property.
     *   <li>A {@link XSDParticle} representing the element itself.
     * </ol>
     *
     * <p>This method should only be implemented in the case where the encoder can not determine
     * what the properties of the object are from the schema.
     *
     * <p>An example would be an object which corresponds to an element in the schema which has a
     * the type <code>xs:anyType</code>. Since the content of this type can be anything the schema
     * has no way to determine what the properties are. So in this case this method must specify the
     * properties manually as a set of name, object tuples.
     *
     * <p>In the case of a multi-valued property, this method must return a tuple for each instance
     * of the property, and not a list, iterator, or array containing all of the instances.
     *
     * @param object the object being encoded.
     * @return A list of the properties for the object.
     */
    List /*Object[QName,Object]*/ getProperties(Object object, XSDElementDeclaration element)
            throws Exception;
}
