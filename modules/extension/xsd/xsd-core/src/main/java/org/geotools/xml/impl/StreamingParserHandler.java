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

import org.xml.sax.SAXException;
import org.geotools.xml.Configuration;


public class StreamingParserHandler extends ParserHandler {
    /** stream buffer **/
    Buffer buffer;

    public StreamingParserHandler(Configuration config) {
        super(config);

        buffer = new Buffer();
    }

    protected void endElementInternal(ElementHandler handler) {
        super.endElementInternal(handler);

        if (stream(handler)) {
            //throw value into buffer
            buffer.put(handler.getParseNode().getValue());

            //remove this node from parse tree
            if (handler.getParentHandler() instanceof ElementHandler) {
                ElementHandler parent = (ElementHandler) handler
                    .getParentHandler();
                ((NodeImpl) parent.getParseNode()).removeChild(handler.getParseNode());

                //parent.endChildHandler(handler);
            }
        }
    }

    protected boolean stream(ElementHandler handler) {
        return false;
    }

    public void endDocument() throws SAXException {
        super.endDocument();
        buffer.close();
    }

    public Buffer getBuffer() {
        return buffer;
    }
}
