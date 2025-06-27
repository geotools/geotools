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
 * RedefineHandler purpose.
 *
 * <p>represents a 'redefine' element
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 * @version $Id$
 */
public class RedefineHandler extends XSIElementHandler {
    /** 'redefine' */
    public static final String LOCALNAME = "redefine";

    private static int offset = 0;
    private String id;
    private String schemaLocation;
    private List<SimpleTypeHandler> simpleTypes;
    private List<ComplexTypeHandler> complexTypes;
    private List<GroupHandler> groups;
    private List<AttributeGroupHandler> attributeGroups;
    private int hashCodeOffset = getOffset();

    /*
     * helper for hashCode();
     */
    private static int getOffset() {
        return offset++;
    }

    /** @see java.lang.Object#hashCode() */
    @Override
    @SuppressWarnings("PMD.OverrideBothEqualsAndHashcode")
    public int hashCode() {
        return LOCALNAME.hashCode()
                        * (id == null ? 1 : id.hashCode())
                        * (schemaLocation == null ? 1 : schemaLocation.hashCode())
                + hashCodeOffset;
    }

    /** @see org.geotools.xml.XSIElementHandler#getHandler(java.lang.String, java.lang.String) */
    @Override
    public XSIElementHandler getHandler(String namespaceURI, String localName) {
        if (SchemaHandler.namespaceURI.equalsIgnoreCase(namespaceURI)) {
            // child types
            //
            // simpleType
            if (SimpleTypeHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (simpleTypes == null) {
                    simpleTypes = new LinkedList<>();
                }

                SimpleTypeHandler sth = new SimpleTypeHandler();
                simpleTypes.add(sth);

                return sth;
            }

            // complexType
            if (ComplexTypeHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (complexTypes == null) {
                    complexTypes = new LinkedList<>();
                }

                ComplexTypeHandler sth = new ComplexTypeHandler();
                complexTypes.add(sth);

                return sth;
            }

            // group
            if (GroupHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (groups == null) {
                    groups = new LinkedList<>();
                }

                GroupHandler sth = new GroupHandler();
                groups.add(sth);

                return sth;
            }

            // attributeGroup
            if (AttributeGroupHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (attributeGroups == null) {
                    attributeGroups = new LinkedList<>();
                }

                AttributeGroupHandler sth = new AttributeGroupHandler();
                attributeGroups.add(sth);

                return sth;
            }
        }

        return null;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#startElement(java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement(String namespaceURI, String localName, Attributes atts) {
        id = atts.getValue("", "id");

        if (id == null) {
            id = atts.getValue(namespaceURI, "id");
        }

        schemaLocation = atts.getValue("", "schemaLocation");

        if (schemaLocation == null) {
            schemaLocation = atts.getValue(namespaceURI, "schemaLocation");
        }
    }

    /** @see org.geotools.xml.XSIElementHandler#getLocalName() */
    @Override
    public String getLocalName() {
        return LOCALNAME;
    }

    /** Returns a list of AttributeGroupHandlers */
    public List getAttributeGroups() {
        return attributeGroups;
    }

    /** Returns a list of ComplexTypeHandlers */
    public List getComplexTypes() {
        return complexTypes;
    }

    /** Returns a list of GroupHandlers */
    public List getGroups() {
        return groups;
    }

    /** Returns the id attribute */
    public String getId() {
        return id;
    }

    /** Returns the schemaLocation attribute */
    public String getSchemaLocation() {
        return schemaLocation;
    }

    /** Returns a list of SimpleTypeHandlers */
    public List getSimpleTypes() {
        return simpleTypes;
    }

    /** @see org.geotools.xml.XSIElementHandler#getHandlerType() */
    @Override
    public int getHandlerType() {
        return DEFAULT;
    }

    /** @see org.geotools.xml.XSIElementHandler#endElement(java.lang.String, java.lang.String) */
    @Override
    public void endElement(String namespaceURI, String localName) {
        // do nothing
    }
}
