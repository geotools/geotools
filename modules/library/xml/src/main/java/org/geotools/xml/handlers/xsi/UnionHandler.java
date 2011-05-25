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
package org.geotools.xml.handlers.xsi;

import java.util.LinkedList;
import java.util.List;

import org.geotools.xml.XSIElementHandler;
import org.xml.sax.Attributes;


/**
 * UnionHandler purpose.
 * 
 * <p>
 * represents a union element
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 *
 * @source $URL$
 * @version $Id$
 */
public class UnionHandler extends XSIElementHandler {
    /** 'union' */
    public final static String LOCALNAME = "union";
    private String id;
    private String memberTypes;
    private List simpleTypes;

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return LOCALNAME.hashCode() * ((id == null) ? 1 : id.hashCode()) * ((memberTypes == null)
        ? 1 : memberTypes.hashCode()) * ((simpleTypes == null) ? 1
                                                               : simpleTypes
        .hashCode());
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getHandler(java.lang.String,
     *      java.lang.String)
     */
    public XSIElementHandler getHandler(String namespaceURI, String localName){
        if (SchemaHandler.namespaceURI.equalsIgnoreCase(namespaceURI)) {
            // child types
            //
            // simpleType
            if (SimpleTypeHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (simpleTypes == null) {
                    simpleTypes = new LinkedList();
                }

                SimpleTypeHandler sth = new SimpleTypeHandler();
                simpleTypes.add(sth);

                return sth;
            }
        }

        return null;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#startElement(java.lang.String,
     *      java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String namespaceURI, String localName,
        Attributes atts){
        id = atts.getValue("", "id");

        if (id == null) {
            id = atts.getValue(namespaceURI, "id");
        }

        memberTypes = atts.getValue("", "memberTypes");

        if (memberTypes == null) {
            memberTypes = atts.getValue(namespaceURI, "memberTypes");
        }
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getLocalName()
     */
    public String getLocalName() {
        return LOCALNAME;
    }

    /**
     * DOCUMENT ME!
     *
     * @return memberTypes attribute value
     */
    public String getMemberTypes() {
        return memberTypes;
    }

    /**
     * DOCUMENT ME!
     *
     * @return list of simpleTypeHandlers representing the nested simpleTypes
     */
    public List getSimpleTypes() {
        return simpleTypes;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getHandlerType()
     */
    public int getHandlerType() {
        return UNION;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#endElement(java.lang.String,
     *      java.lang.String)
     */
    public void endElement(String namespaceURI, String localName){
        // do nothing
    }
}
