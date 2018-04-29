/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.opengis.feature.Attribute;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;

/**
 * Special node iterator for {@link Attribute}.
 *
 * @author Justin Deoliveira (The Open Planning Project)
 * @author Gabriel Roldan (Axios Engineering)
 * @source $URL$
 */
public class AttributeNodeIterator implements NodeIterator {

    /** The feature node pointer */
    AttributeNodePointer pointer;

    /** The feature. */
    ComplexAttribute feature;

    List<Property> children;

    /** current position */
    int position;

    public AttributeNodeIterator(AttributeNodePointer pointer) {
        this.pointer = pointer;
        feature = (ComplexAttribute) pointer.getImmediateAttribute();
        children = new ArrayList<Property>(feature.getValue());
        position = 1;
    }

    public AttributeNodeIterator(AttributeNodePointer pointer, Name name) {
        this.pointer = pointer;
        feature = (ComplexAttribute) pointer.getImmediateAttribute();

        AttributeDescriptor descriptor = feature.getDescriptor();
        Name attName = descriptor == null ? feature.getType().getName() : descriptor.getName();
        if (attName.equals(name)) {
            children = Collections.<Property>singletonList(feature);
        } else {
            children = new ArrayList<Property>(feature.getProperties(name));
        }

        position = children.size() > 0 ? 1 : 0;
    }

    public int getPosition() {
        return position;
    }

    public boolean setPosition(int position) {
        this.position = position;
        return position <= children.size();
    }

    public NodePointer getNodePointer() {
        Attribute attribute = (Attribute) children.get(position - 1);
        Name name = attribute.getDescriptor().getName();
        QName qname = new QName(name.getNamespaceURI(), name.getLocalPart());
        return new AttributeNodePointer(pointer, attribute, qname);
    }
}
