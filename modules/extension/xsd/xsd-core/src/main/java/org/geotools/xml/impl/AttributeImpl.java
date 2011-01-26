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
package org.geotools.xml.impl;

import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDSchemaContent;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.xml.AttributeInstance;


public class AttributeImpl extends InstanceComponentImpl implements AttributeInstance {
    XSDAttributeDeclaration decl;

    public AttributeImpl(XSDAttributeDeclaration decl) {
        this.decl = decl;
    }

    public XSDTypeDefinition getTypeDefinition() {
        return decl.getTypeDefinition();
    }

    public XSDAttributeDeclaration getAttributeDeclaration() {
        return decl;
    }

    public XSDNamedComponent getDeclaration() {
        return getAttributeDeclaration();
    }
}
