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
 * <p>
 * This interface is intended to represent an XML Schema AttributeGroup. In
 * many cases AttributeGroups may be optimized within complexTypes to remove
 * the level of indirection. AttributeGroups remain in the interface set as
 * there are publicly defined (externally visible) AttributeGroups defined in
 * XML Schemas.
 * </p>
 *
 * @author dzwiers www.refractions.net
 * @source $URL$
 */
public interface AttributeGroup {
    /**
     * <p>
     * Represents the Namespace attribute of an AnyAttribute child occuring
     * within this attributeGroup.
     * </p>
     *
     */
    public String getAnyAttributeNameSpace();

    /**
     * <p>
     * The list of attribute declared within this attributeGroup. Although we
     * encourage the order of this list to follow the order the attributes
     * were declared in, there is no such requirement.
     * </p>
     *
     */
    public Attribute[] getAttributes();

    /**
     * <p>
     * The Schema ID for this attributeGroup definition.
     * </p>
     *
     */
    public String getId();

    /**
     * <p>
     * The name of this Attribute Group declaration within the XML Schema
     * </p>
     *
     */
    public String getName();

    /**
     * DOCUMENT ME!
     *
     */
    public URI getNamespace();
}
