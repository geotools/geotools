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

/**
 * <p>
 * Instances of this interface are intended to represent the 'all' construct in
 * an XML Schema.
 * </p>
 *
 * @author dzwiers www.refractions.net
 *
 * @see Element
 * @source $URL$
 */
public interface All extends ElementGrouping {
    /**
     * <p>
     * The list of elements represented within this 'all' declaration. We
     * should not that as per the Schema definition of the 'all' declaration,
     * the return order in the array should not have an effect on the instance
     * document.
     * </p>
     *
     */
    public Element[] getElements();

    /**
     * <p>
     * Returns the element declaration's id for this schema element.
     * </p>
     *
     */
    public String getId();

    /**
     * @see org.geotools.xml.xsi.ElementGrouping#getMaxOccurs()
     */
    public int getMaxOccurs();

    /**
     * @see org.geotools.xml.xsi.ElementGrouping#getMinOccurs()
     */
    public int getMinOccurs();
}
