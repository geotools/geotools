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

/** @author dzwiers */
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

    /** Creates a new GroupGT object. */
    public GroupGT(String id, String name, URI namespace, ElementGrouping child, int min, int max) {
        this.id = id;
        this.name = name;
        this.namespace = namespace;
        this.child = child;
        this.min = min;
        this.max = max;
    }

    /** @see org.geotools.xml.schema.Group#getChild() */
    @Override
    public ElementGrouping getChild() {
        return child;
    }

    /** @see org.geotools.xml.schema.Group#getId() */
    @Override
    public String getId() {
        return id;
    }

    /** @see org.geotools.xml.schema.ElementGrouping#getMaxOccurs() */
    @Override
    public int getMaxOccurs() {
        return max;
    }

    /** @see org.geotools.xml.schema.ElementGrouping#getMinOccurs() */
    @Override
    public int getMinOccurs() {
        return min;
    }

    /** @see org.geotools.xml.schema.Group#getName() */
    @Override
    public String getName() {
        return name;
    }

    /** @see org.geotools.xml.schema.Group#getNamespace() */
    @Override
    public URI getNamespace() {
        return namespace;
    }

    /** @see org.geotools.xml.schema.ElementGrouping#getGrouping() */
    @Override
    public int getGrouping() {
        return GROUP;
    }

    /** @see org.geotools.xml.schema.ElementGrouping#findChildElement(java.lang.String) */
    @Override
    public Element findChildElement(String name1) {
        return child == null ? null : child.findChildElement(name1);
    }

    @Override
    public Element findChildElement(String localName, URI namespaceURI) {
        return child == null ? null : child.findChildElement(localName, namespaceURI);
    }
}
