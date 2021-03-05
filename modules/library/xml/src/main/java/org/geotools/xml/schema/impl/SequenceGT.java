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
import org.geotools.xml.schema.Sequence;

/** @author dzwiers */
public class SequenceGT implements Sequence {
    private ElementGrouping[] children;
    private String id;
    private int min;
    private int max;

    private SequenceGT() {
        // do nothing
    }

    public SequenceGT(ElementGrouping[] children) {
        this.children = children;
        min = max = 1;
    }

    public SequenceGT(String id, ElementGrouping[] children, int min, int max) {
        this.children = children;
        this.min = min;
        this.max = max;
        this.id = id;
    }

    /** @see org.geotools.xml.schema.Sequence#getChildren() */
    @Override
    public ElementGrouping[] getChildren() {
        return children;
    }

    /** @see org.geotools.xml.schema.Sequence#getId() */
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

    /** @see org.geotools.xml.schema.ElementGrouping#getGrouping() */
    @Override
    public int getGrouping() {
        return SEQUENCE;
    }

    /** @see org.geotools.xml.schema.ElementGrouping#findChildElement(java.lang.String) */
    @Override
    public Element findChildElement(String name) {
        if (children != null) {
            for (ElementGrouping child : children) {
                Element e = child.findChildElement(name);

                if (e != null) {
                    return e;
                }
            }
        }

        return null;
    }

    @Override
    public Element findChildElement(String localName, URI namespaceURI) {
        if (children != null) {
            for (ElementGrouping child : children) {
                Element e = child.findChildElement(localName, namespaceURI);

                if (e != null) {
                    return e;
                }
            }
        }

        return null;
    }
}
