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

import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDAttributeGroupDefinition;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;
import java.util.List;
import javax.xml.namespace.QName;


public interface SchemaIndex {
    /**
     * @return The schema itself.
     */
    XSDSchema[] getSchemas();

    /**
     * Returns the element declaration with the specified qualified name.
     *
     * @param qName the qualified name of the element.
     *
     * @return The element declaration, or null if no such element declaration
     * exists.
     */
    XSDElementDeclaration getElementDeclaration(QName qName);

    /**
     * Returns the attribute declaration with the specified qualified name.
     *
     * @param qName the qualified name of the attribute.
     *
     * @return The attribute declaration, or null if no such attribute
     * declaration exists.
     */
    XSDAttributeDeclaration getAttributeDeclaration(QName qName);

    /**
     * Returns the attribute group definition with the specified qualified name.
     *
     * @param qName the qualified name of the attribute group.
     *
     * @return The attribute group definition, or null if no such attribute
     * group definition exists.
     */
    XSDAttributeGroupDefinition getAttributeGroupDefinition(QName qName);

    /**
     * Returns the complex type definition with the specified qualified name.
     *
     * @param qName qualified name of the complex type.
     *
     * @return The complex type definition, or null if no such complex type
     * definition exists.
     */
    XSDComplexTypeDefinition getComplexTypeDefinition(QName qName);

    /**
     * Returns the simple type definition with the specified qualified name.
     *
     * @param qName qualified name of the simple type.
     *
     * @return The simple type definition, or null if no such simple type
     * definition exists.
     */
    XSDSimpleTypeDefinition getSimpleTypeDefinition(QName qName);

    /**
     * Returns the type definition with the specified qualified name.
     *
     * @param qName qualified name of the type.
     *
     * @return The type definition, or null if no such type definition exists.
     */
    XSDTypeDefinition getTypeDefinition(QName qName);

    /**
     * Returns a child element specified by name of a parent element.
     *
     * @param parent The parent element.
     * @param childName The name of the child.
     *
     * @return The element declaration, or null if no such child exists.
     */
    XSDElementDeclaration getChildElement(XSDElementDeclaration parent, QName childName);

    /**
     * Returns a list of the particles which correspond to child element declarations.
     *
     * @param parent The parent element.
     *
     * @return A list of {@link org.eclipse.xsd.XSDParticle}.
     */
    List getChildElementParticles(XSDElementDeclaration parent);

    /**
     * Returns the attributes of a specified elements.
     *
     * @param element The element.
     *
     * @return The list of attributed definied for the element.
     */
    List getAttributes(XSDElementDeclaration element);
    
    /**
     * Cleans up the index before desctruction.
     */
    void destroy();
}
