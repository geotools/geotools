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

import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementValue;

/**
 * <p>
 * DOCUMENT ME!
 * </p>
 *
 * @author dzwiers
 *
 * @source $URL$
 */
public class ElementValueGT implements ElementValue {
    private Element element;
    private Object value;

    private ElementValueGT() {
        // do nothing
    }

    /**
     * Creates a new ElementValueGT object.
     *
     * @param element DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public ElementValueGT(Element element, Object value) {
        this.element = element;
        this.value = value;
    }

    /**
     * @see org.geotools.xml.schema.ElementValue#getElement()
     */
    public Element getElement() {
        return element;
    }

    /**
     * @see org.geotools.xml.schema.ElementValue#getValue()
     */
    public Object getValue() {
        return value;
    }
}
