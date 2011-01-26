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

import org.eclipse.xsd.XSDSchemaContent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.xml.InstanceComponent;
import org.geotools.xml.Node;


public class DocumentHandlerImpl extends HandlerImpl implements DocumentHandler {
    /** factory used to create a handler for the root element **/
    HandlerFactory factory;

    /** root node of the parse tree */
    Node tree;

    //ElementHandler handler;

    /** the parser */
    ParserHandler parser;

    public DocumentHandlerImpl(HandlerFactory factory, ParserHandler parser) {
        this.factory = factory;
        this.parser = parser;
    }

    public XSDSchemaContent getSchemaContent() {
        return null;
    }

    public InstanceComponent getComponent() {
        return null;
    }

    public Object getValue() {
        //jsut return the root of the parse tree's value
        if (tree != null) {
            return tree.getValue();
        }

        //    	//just return the root handler value
        //        if (handler != null) {
        //            return handler.getValue();
        //        }
        return null;
    }

    public Node getParseNode() {
        return tree;
    }

    public Handler createChildHandler(QName qName) {
        return factory.createElementHandler(qName, this, parser);
    }

    //    public List getChildHandlers() {
    //    	if ( handler == null ) {
    //    		return Collections.EMPTY_LIST;
    //    	}
    //    	
    //    	ArrayList list = new ArrayList();
    //    	list.add( handler );
    //    	
    //    	return list;
    //    }
    public void startChildHandler(Handler child) {
        this.tree = child.getParseNode();

        //this.handler = (ElementHandler) child;
    }

    public void endChildHandler(Handler child) {
        //this.handler = null;
    }

    public Handler getParentHandler() {
        //always null, this is the root handler
        return null;
    }

    //    public ElementHandler getDocumentElementHandler() {
    //        return handler;
    //    }
    
    public void startDocument() {
    }
    
    public void endDocument() {
    }
}
