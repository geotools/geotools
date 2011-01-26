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

import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDSchemaContent;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.xml.AttributeInstance;
import org.geotools.xml.ElementInstance;


public class ElementImpl extends InstanceComponentImpl implements ElementInstance {
    /** declaration **/
    XSDElementDeclaration declaration;

    /** attributes **/
    AttributeInstance[] atts;

    public ElementImpl(XSDElementDeclaration declaration) {
        this.declaration = declaration;
    }

    public XSDTypeDefinition getTypeDefinition() {
        return declaration.getTypeDefinition();
    }

    public XSDNamedComponent getDeclaration() {
        return getElementDeclaration();
    }

    public XSDElementDeclaration getElementDeclaration() {
        return declaration;
    }

    public AttributeInstance[] getAttributes() {
        return atts;
    }

    public void setAttributes(AttributeInstance[] atts) {
        this.atts = atts;
    }
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(name);
        buf.append(" ");
        buf.append(namespace);        
        if( atts != null ){
            for( AttributeInstance att : atts ){
                buf.append(" ");
                buf.append( att.getName() );
                buf.append( "=");
                buf.append( att.getText() );
            }
        }
        if( text != null ){
            buf.append("\n\t");
            buf.append( text );
        }
        return buf.toString();
    }
}
