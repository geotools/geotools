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
 * This interface is intended to represent a Choice in an XML Schema. The
 * children of this choice are ElementGroupings which may involve Element
 * declarations, Sequence, Groups ... or even another Choices. We recommend
 * flattening child Choices with the parent, creating a semantically
 * equivalent choice in it's place.
 * </p>
 *
 * @author dzwiers www.refractions.net
 * @source $URL$
 */
public interface Choice extends ElementGrouping {
    /**
     * <p>
     * The Schema ID for this choice definition.
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

    /**
     * <p>
     * This method returns a list of children which repreensts the options for
     * the element which this choice is representing in an XML Schema.
     * Although the list is semantically a set, we encourage you to add
     * elements to the list as they apear, to allow Schema writers to optimize
     * the search order.
     * </p>
     *
     */
    public ElementGrouping[] getChildren();
}
