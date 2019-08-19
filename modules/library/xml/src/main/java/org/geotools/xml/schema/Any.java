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
package org.geotools.xml.schema;

import java.net.URI;

/**
 * Instances of this interface are intended to represent the 'any' construct in an XML Schema.
 *
 * @author dzwiers www.refractions.net
 */
public interface Any extends ElementGrouping {

    public static final URI ALL = null;
    /** Returns the element declaration's id for this schema element. */
    public String getId();

    /** @see org.geotools.xml.xsi.ElementGrouping#getMaxOccurs() */
    public int getMaxOccurs();

    /** @see org.geotools.xml.xsi.ElementGrouping#getMinOccurs() */
    public int getMinOccurs();

    /**
     * Returns the namespace attribute of the 'any' contruct that an instance of this interface is
     * representing within an XML Schema.
     */
    public URI getNamespace();
}
