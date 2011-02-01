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

package org.geotools.feature.xpath;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.opengis.feature.Attribute;
import org.opengis.feature.type.Name;

/**
 * Special node iterator for {@link Attribute}.
 * 
 * @author Niels Charlier, Curtin University of Technology
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main/java/org/geotools/feature/xpath/XmlAttributeNodeIterator.java $
 */
public class XmlAttributeNodeIterator implements NodeIterator {

    /**
     * The feature node pointer
     */
    AttributeNodePointer pointer;

    /**
     * The feature.
     */
    Attribute feature;
    
    /**
     * The name
     */
    Name name;


    public XmlAttributeNodeIterator(AttributeNodePointer pointer, Name name) {
        this.pointer = pointer;
        this.name = name;
        feature = (Attribute) pointer.getImmediateNode();

    }

    /**
     * Always return 1, only a single property.
     */
    public int getPosition() {
        return 1;
    }

    /**
     * Return true if position == 1.
     */
    public boolean setPosition(int position) {
        return position == 1;
    }

    public NodePointer getNodePointer() {
        return new XmlAttributeNodePointer(pointer, feature, name);
    }

}
