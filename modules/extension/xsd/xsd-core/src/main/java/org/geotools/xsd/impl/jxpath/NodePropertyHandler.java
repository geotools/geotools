/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xsd.impl.jxpath;

import java.util.List;
import org.apache.commons.jxpath.DynamicPropertyHandler;
import org.geotools.xsd.Node;

public class NodePropertyHandler implements DynamicPropertyHandler {
    @Override
    public Object getProperty(Object object, String property) {
        Node node = (Node) object;

        return node.getChildren(property);
    }

    @Override
    public String[] getPropertyNames(Object object) {
        Node node = (Node) object;
        List children = node.getChildren();

        if (children == null || children.isEmpty()) {
            return new String[] {};
        }

        String[] propertyNames = new String[children.size()];

        for (int i = 0; i < children.size(); i++) {
            Node child = (Node) children.get(i);
            propertyNames[i] = child.getComponent().getName();
        }

        return propertyNames;
    }

    @Override
    public void setProperty(Object object, String property, Object value) {
        throw new UnsupportedOperationException();
    }
}
