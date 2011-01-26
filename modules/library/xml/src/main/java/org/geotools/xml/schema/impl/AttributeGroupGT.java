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
package org.geotools.xml.schema.impl;

import java.net.URI;

import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.AttributeGroup;


/**
 * <p>
 * DOCUMENT ME!
 * </p>
 *
 * @author dzwiers
 * @source $URL$
 */
public class AttributeGroupGT implements AttributeGroup {
    private String anyAttributeNamespace;
    private Attribute[] attributes;
    private String id;
    private String name;
    private URI namespace;

    private AttributeGroupGT() {
        // do nothing
    }

    /**
     * Creates a new AttributeGroupGT object.
     *
     * @param id DOCUMENT ME!
     * @param name DOCUMENT ME!
     * @param namespace DOCUMENT ME!
     * @param attributes DOCUMENT ME!
     * @param anyAttributeNamespace DOCUMENT ME!
     */
    public AttributeGroupGT(String id, String name, URI namespace,
        Attribute[] attributes, String anyAttributeNamespace) {
        this.id = id;
        this.name = name;
        this.namespace = namespace;
        this.attributes = attributes;
        this.anyAttributeNamespace = anyAttributeNamespace;
    }

    /**
     * @see org.geotools.xml.xsi.AttributeGroup#getAnyAttributeNameSpace()
     */
    public String getAnyAttributeNameSpace() {
        return anyAttributeNamespace;
    }

    /**
     * @see org.geotools.xml.xsi.AttributeGroup#getAttributeDescriptors()
     */
    public Attribute[] getAttributes() {
        return attributes;
    }

    /**
     * @see org.geotools.xml.xsi.AttributeGroup#getId()
     */
    public String getId() {
        return id;
    }

    /**
     * @see org.geotools.xml.xsi.AttributeGroup#getLocalName()
     */
    public String getName() {
        return name;
    }

    /**
     * @see org.geotools.xml.xsi.AttributeGroup#getNamespace()
     */
    public URI getNamespace() {
        return namespace;
    }
}
