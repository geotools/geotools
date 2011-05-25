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

import org.geotools.xml.schema.Facet;

/**
 * <p>
 * DOCUMENT ME!
 * </p>
 *
 * @author dzwiers
 *
 * @source $URL$
 */
public class FacetGT implements Facet {
    private int type;
    private String value;

    private FacetGT() {
        // do nothing
    }

    /**
     * Creates a new FacetGT object.
     *
     * @param type DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public FacetGT(int type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * @see org.geotools.xml.schema.Facet#getFacetType()
     */
    public int getFacetType() {
        return type;
    }

    /**
     * @see org.geotools.xml.schema.Facet#getValue()
     */
    public String getValue() {
        return value;
    }
}
