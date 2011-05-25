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

/**
 * <p>
 * DOCUMENT ME!
 * </p>
 *
 * @author dzwiers
 *
 * @source $URL$
 */
public class ChoiceGT implements Choice {
    private String id;
    private int min;
    private int max;
    private ElementGrouping[] children;

    private ChoiceGT() {
        // do nothing
    }

    /**
     * Creates a new ChoiceGT object.
     *
     * @param id DOCUMENT ME!
     * @param min DOCUMENT ME!
     * @param max DOCUMENT ME!
     * @param children DOCUMENT ME!
     */
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

    /**
     * @see org.geotools.xml.schema.Choice#getId()
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
     * @see org.geotools.xml.schema.Choice#getChildren()
     */
    public ElementGrouping[] getChildren() {
        return children;
    }

    /**
     * @see org.geotools.xml.schema.ElementGrouping#getGrouping()
     */
    public int getGrouping() {
        return CHOICE;
    }

    /**
     * @see org.geotools.xml.schema.ElementGrouping#findChildElement(java.lang.String)
     */
    public Element findChildElement(String name) {
        if (children == null) {
            return null;
        }

        for (int i = 0; i < children.length; i++) {
            Element e = children[i].findChildElement(name);

            if (e != null) {
                return e;
            }
        }

        return null;
    }

	public Element findChildElement(String localName, URI namespaceURI) {
        if (children == null) {
            return null;
        }

        for (int i = 0; i < children.length; i++) {
            Element e = children[i].findChildElement(localName, namespaceURI);

            if (e != null) {
                return e;
            }
        }

        return null;
	}
}
