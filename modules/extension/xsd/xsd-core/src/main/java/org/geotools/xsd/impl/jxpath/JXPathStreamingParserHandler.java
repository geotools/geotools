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
package org.geotools.xsd.impl.jxpath;

import java.util.Iterator;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathContextFactory;
import org.apache.commons.jxpath.JXPathIntrospector;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Node;
import org.geotools.xsd.impl.DocumentHandler;
import org.geotools.xsd.impl.ElementHandler;
import org.geotools.xsd.impl.NodeImpl;
import org.geotools.xsd.impl.StreamingParserHandler;

public class JXPathStreamingParserHandler extends StreamingParserHandler {
    /** xpath to stream * */
    String xpath;

    public JXPathStreamingParserHandler(Configuration config, String xpath) {
        super(config);

        this.xpath = xpath;
    }

    protected boolean stream(ElementHandler handler) {
        // create an xpath context from the root element
        // TODO: cache the context, should work just the same
        //        JXPathIntrospector.registerDynamicClass(ElementHandlerImpl.class,
        //            ElementHandlerPropertyHandler.class);
        JXPathIntrospector.registerDynamicClass(NodeImpl.class, NodePropertyHandler.class);

        //        ElementHandler rootHandler =
        //        	((DocumentHandler) handlers.firstElement()).getDocumentElementHandler();
        Node root = ((DocumentHandler) handlers.firstElement()).getParseNode();
        JXPathContext jxpContext = JXPathContextFactory.newInstance().newContext(null, root);

        jxpContext.setLenient(true);

        Iterator itr = jxpContext.iterate(xpath);

        while (itr.hasNext()) {
            Object obj = itr.next();

            if (handler.getParseNode().equals(obj)) {
                return true;
            }
        }

        return false;
    }
}
