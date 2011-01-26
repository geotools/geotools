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

import javax.xml.namespace.QName;

import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDSchemaContent;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.InstanceComponent;
import org.geotools.xml.Node;
import org.geotools.xml.ParserDelegate;
import org.picocontainer.MutablePicoContainer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class DelegatingHandler implements DocumentHandler, ElementHandler {

    ParserDelegate delegate;
    Handler parent;
    QName elementName;
    NodeImpl parseTree;
    
    DelegatingHandler( ParserDelegate delegate, QName elementName, Handler parent) {
        this.delegate = delegate;
        this.parent = parent;
        this.elementName = elementName;
        
        //create a parse tree
        XSDElementDeclaration e = XSDFactory.eINSTANCE.createXSDElementDeclaration();
        e.setTargetNamespace( elementName.getNamespaceURI() );
        e.setName( elementName.getLocalPart() );
        
        ElementImpl instance = new ElementImpl( e );
        instance.setName( elementName.getLocalPart() );
        instance.setNamespace( elementName.getNamespaceURI() );
        
        parseTree = new NodeImpl( instance );
    }
    
    public void setContext(MutablePicoContainer context) {
    }
    
    public MutablePicoContainer getContext() {
        return null;
    }
    
    
    public XSDElementDeclaration getElementDeclaration() {
        return ((ElementInstance)parseTree.getComponent()).getElementDeclaration();
    }
    
    public Handler getParentHandler() {
        return parent;
    }
    
    public Handler createChildHandler(QName name) {
        return new DelegatingHandler( delegate, name, this );
    }

    public void startChildHandler(Handler child) {
    }
    
    public void endChildHandler(Handler child) {
    }

    public InstanceComponent getComponent() {
        return null;
    }

    public Node getParseNode() {
        return parseTree;
    }

    public XSDSchemaContent getSchemaContent() {
        return null;
    }
    
    public void startDocument() throws SAXException {
        delegate.startDocument();
    }
    
    public void endDocument() throws SAXException {
        delegate.endDocument();
    }

    public void startElement(QName name, Attributes attributes)
        throws SAXException {
        
        if ( !( parent instanceof DelegatingHandler ) ) {
            parent.startChildHandler( this );
        }
        
        delegate.startElement(name.getNamespaceURI(), name.getLocalPart(), 
            qname(name) , attributes);
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        delegate.characters( ch, start, length );
    }

    public void endElement(QName name) throws SAXException {
        delegate.endElement( name.getNamespaceURI(), name.getLocalPart(), qname( name ) );
    }
    
    String qname( QName name ) {
        return name.getNamespaceURI() != null ? name.getPrefix() + ":" + name.getLocalPart() : name.getLocalPart();
    }
}
