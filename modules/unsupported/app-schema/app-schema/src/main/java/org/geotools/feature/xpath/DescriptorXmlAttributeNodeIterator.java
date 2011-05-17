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
import org.opengis.feature.type.PropertyDescriptor;

/**
 * Special node iterator for {@link Attribute}.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * @author Gabriel Roldan, Axios Engineering
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main/java/org/geotools/feature/xpath/AttributeNodeIterator.java $
 */
public class DescriptorXmlAttributeNodeIterator implements NodeIterator {

    /**
     * The feature node pointer
     */
    FeatureTypeAttributePointer pointer;

    /**
     * The feature.
     */
    PropertyDescriptor descriptor;
    
    /**
     * The name
     */
    Name name;
    
    int position = 0;


    public DescriptorXmlAttributeNodeIterator(FeatureTypeAttributePointer pointer, Name name) {
        this.pointer = pointer;
        this.name = name;
        descriptor = (PropertyDescriptor) pointer.getImmediateNode();

    }

    /**
     * Always return 1, only a single property.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Return true if position == 1.
     */
    public boolean setPosition(int position) {
        this.position = position;
        return position < 2;
    }

    public NodePointer getNodePointer() {
        return new DescriptorXmlAttributeNodePointer(pointer, descriptor, name);
    }

}
