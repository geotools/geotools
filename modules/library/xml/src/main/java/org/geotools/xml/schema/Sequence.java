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
 * This interface is intended to represent a Sequence in an XML Schema. This
 * shildren of this sequence are ElementGroupings which may involve Element
 * declarations, Choices, Groups ... or even another Sequence. We recommend
 * flattening child Sequences with the parent, creating a semantically
 * equivalent sequence in it's place.
 * </p>
 *
 * @author dzwiers www.refractions.net
 *
 * @see ElementGrouping
 *
 * @source $URL$
 */
public interface Sequence extends ElementGrouping {
    /**
     * <p>
     * This method returns an ORDERED list of children. The children in the
     * list may be singular elements, sequences, choices, ... , or groups.
     * </p>
     *
     */
    public ElementGrouping[] getChildren();

    /**
     * <p>
     * The Schema ID for this sequence definition.
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
