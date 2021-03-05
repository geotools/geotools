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
import org.geotools.xml.schema.Choice;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;

/** @author dzwiers */
public class ChoiceGT implements Choice {
    private String id;
    private int min;
    private int max;
    private ElementGrouping[] children;

    private ChoiceGT() {
        // do nothing
    }

    /** Creates a new ChoiceGT object. */
    public ChoiceGT(String id, int min, int max, ElementGrouping[] children) {
        this.id = id;
        this.min = min;
        this.max = max;
        this.children = children;
    }

    public ChoiceGT(ElementGrouping[] children) {
        this.min = 1;
        this.max = 1;
        this.children = children;
    }

    /** @see org.geotools.xml.schema.Choice#getId() */
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

    /** @see org.geotools.xml.schema.Choice#getChildren() */
    @Override
    public ElementGrouping[] getChildren() {
        return children;
    }

    /** @see org.geotools.xml.schema.ElementGrouping#getGrouping() */
    @Override
    public int getGrouping() {
        return CHOICE;
    }

    /** @see org.geotools.xml.schema.ElementGrouping#findChildElement(java.lang.String) */
    @Override
    public Element findChildElement(String name) {
        if (children == null) {
            return null;
        }

        for (ElementGrouping child : children) {
            Element e = child.findChildElement(name);

            if (e != null) {
                return e;
            }
        }

        return null;
    }

    @Override
    public Element findChildElement(String localName, URI namespaceURI) {
        if (children == null) {
            return null;
        }

        for (ElementGrouping child : children) {
            Element e = child.findChildElement(localName, namespaceURI);

            if (e != null) {
                return e;
            }
        }

        return null;
    }
}
