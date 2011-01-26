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
package org.geotools.xml.schema;

import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import org.geotools.xml.PrintHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <p>
 * This is a convinience interface to help speed up the code, allowing for any
 * type definition to handled in a consistent manner, independant of whether
 * it's nested or not.
 * </p>
 * 
 * @see SimpleType
 * @see ComplexType
 * @author dzwiers www.refractions.net
 * @source $URL$
 */
public interface Type {

    /**
     * <p>
     * This method is intended to receive the child elements in the form of
     * ElementValues (@see ElementValue). Recall that this is a pairing
     * containing a reference to the type and the actual value (do not call
     * .getValue on the types). This should return the real value (interpreted
     * value) for this element based on it's children. Remember, this is a XML
     * is a tree structure, so if you don't include some children, they are gone
     * forever (so be careful when ignoring data).
     * </p>
     * <p>
     * If the element had #CData (Nested Child Text), this will appear in the first slot, with a null Element.  
     * </p>
     * <p>
     * A SAXNotSupportedException should be thrown when the child's
     * [inherited, default implementation of
     * getValue(ElementValue[],Attributes)] method should be used.
     * </p>
     * 
     * @throws OperationNotSupportedException
     * 
     * @see ElementValue
     * 
     * @return Object
     */
    public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) throws SAXException, OperationNotSupportedException;

    /**
     * <p>
     * This returns the XML Schema declaration name of this type (both complex
     * and simple types have names ...)
     * </p>
     * 
     */
    public String getName();

    /**
     * <p>
     * This is used for validation of an XML document, and represents the
     * targetNamespace of that this type resides in.
     * </p>
     * 
     */
    public URI getNamespace();

    /**
     * <p>
     * This will return the intended Java Class for this element type. For
     * generic complex types this will be an object array. SimpleTypes will
     * match as they are parsed, and custom extensions will also return the
     * appropriate class value.
     * </p>
     * 
     */
    public Class getInstanceType();


    /**
     * Can I write this object out as element specified?
     * 
     * @param element The element which may be used to represent the Object. This is included to allow for child definitions to include addition information where appropriate. 
     * @param value An Object which may or may not be encodeable by this type. The value may also be null.
     * 
     * @return True when the encode method can interpret the given element/value pair into valid xml.
     * 
     * @see Type#encode(Element, Object, Writer, Map)
     */
    public boolean canEncode(Element element, Object value, Map hints);
    
    /**
     * Encode value as element on the provided output.
     * <p>
     * This is encoding because the PrintHandler does not have to go back to a stream.
     * </p>
     * @param element The original element declaration to which we should encode. 
     * @param value The Object to encode.
     * @param output This is where the output should be written to.
     * @param hints For providing additional context information to specific schemas.
     * @throws IOException When there is an error with the Writer.
     * @throws OperationNotSupportedException When this type cannot be encoded ... and wasn't checked first.
     */
    public void encode(Element element, Object value, PrintHandler output, Map hints) 
    	throws IOException, OperationNotSupportedException;


    /**
     * Convinience method used to search this type's children for the
     * requested element by localName.
     *
     * @param name the element's localName to search for.
     *
     */
    public Element findChildElement(String name);
}
