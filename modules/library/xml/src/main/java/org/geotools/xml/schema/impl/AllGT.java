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
import org.geotools.xml.schema.All;
import org.geotools.xml.schema.Element;

public class AllGT implements All {

    private Element[] elements;
    private String id;
    private int max;
    private int min;

    public AllGT(Element[] elements) {
        this.elements = elements;
        max = min = 1;
    }

    public AllGT(String id, Element[] elements, int min, int max) {
        this.id = id;
        this.elements = elements;
        this.min = min;
        this.max = max;
    }
    /* (non-Javadoc)
     * @see org.geotools.xml.schema.All#getElements()
     */
    @Override
    public Element[] getElements() {
        return elements;
    }

    /* (non-Javadoc)
     * @see org.geotools.xml.schema.All#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see org.geotools.xml.schema.ElementGrouping#getMaxOccurs()
     */
    @Override
    public int getMaxOccurs() {
        return max;
    }

    /* (non-Javadoc)
     * @see org.geotools.xml.schema.ElementGrouping#getMinOccurs()
     */
    @Override
    public int getMinOccurs() {
        return min;
    }

    /* (non-Javadoc)
     * @see org.geotools.xml.schema.ElementGrouping#getGrouping()
     */
    @Override
    public int getGrouping() {
        return ALL;
    }

    /* (non-Javadoc)
     * @see org.geotools.xml.schema.ElementGrouping#findChildElement(java.lang.String)
     */
    @Override
    public Element findChildElement(String name) {
        if (elements != null) {
            for (Element element : elements) {
                Element e = element.findChildElement(name);

                if (e != null) {
                    return e;
                }
            }
        }

        return null;
    }

    @Override
    public Element findChildElement(String localName, URI namespaceURI) {
        if (elements != null) {
            for (Element element : elements) {
                Element e = element.findChildElement(localName, namespaceURI);

                if (e != null) {
                    return e;
                }
            }
        }

        return null;
    }
}
