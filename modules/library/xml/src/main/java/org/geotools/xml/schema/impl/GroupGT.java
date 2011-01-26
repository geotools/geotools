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

import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;
import org.geotools.xml.schema.Group;


/**
 * <p>
 * DOCUMENT ME!
 * </p>
 *
 * @author dzwiers
 * @source $URL$
 */
public class GroupGT implements Group {
    private ElementGrouping child;
    private String id;
    private String name;
    private URI namespace;
    private int min;
    private int max;

    private GroupGT() {
        // do nothing
    }

    /**
     * Creates a new GroupGT object.
     *
     * @param id DOCUMENT ME!
     * @param name DOCUMENT ME!
     * @param namespace DOCUMENT ME!
     * @param child DOCUMENT ME!
     * @param min DOCUMENT ME!
     * @param max DOCUMENT ME!
     */
    public GroupGT(String id, String name, URI namespace,
        ElementGrouping child, int min, int max) {
        this.id = id;
        this.name = name;
        name.toCharArray();
        this.namespace = namespace;
        this.child = child;
        this.min = min;
        this.max = max;
    }

    /**
     * @see org.geotools.xml.schema.Group#getChild()
     */
    public ElementGrouping getChild() {
        return child;
    }

    /**
     * @see org.geotools.xml.schema.Group#getId()
     */
    public String getId() {
        return id;
    }

    /**
     * @see org.geotools.xml.schema.ElementGrouping#getMaxOccurs()
     */
    public int getMaxOccurs() {
        return max;
    }

    /**
     * @see org.geotools.xml.schema.ElementGrouping#getMinOccurs()
     */
    public int getMinOccurs() {
        return min;
    }

    /**
     * @see org.geotools.xml.schema.Group#getName()
     */
    public String getName() {
        return name;
    }

    /**
     * @see org.geotools.xml.schema.Group#getNamespace()
     */
    public URI getNamespace() {
        return namespace;
    }

    /**
     * @see org.geotools.xml.schema.ElementGrouping#getGrouping()
     */
    public int getGrouping() {
        return GROUP;
    }

    /**
     * @see org.geotools.xml.schema.ElementGrouping#findChildElement(java.lang.String)
     */
    public Element findChildElement(String name1) {
        return (child == null) ? null : child.findChildElement(name1);
    }

	public Element findChildElement(String localName, URI namespaceURI) {
        return (child == null) ? null : child.findChildElement(localName, namespaceURI);
	}
}
