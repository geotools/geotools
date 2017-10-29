/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.ext.LexicalHandler;

/**
 * @author ian
 *
 */
public class CDATAHandler extends DefaultHandler2 implements LexicalHandler {

    private ParserHandler handler;

    /**
     * Set the parser so we can inform it of cdata events 
     */
    public CDATAHandler(ParserHandler handler) {
        this.handler = handler;
    }
   
    @Override
    public void startCDATA() throws SAXException {
        if(handler!=null) {
            handler.setCDATA(true);
        }
    }

    @Override
    public void endCDATA() throws SAXException {
        if(handler!=null) {
            handler.setCDATA(false);
        }
        }

   
}
