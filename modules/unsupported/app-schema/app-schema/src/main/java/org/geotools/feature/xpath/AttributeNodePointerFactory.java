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

import java.util.Locale;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.NodePointerFactory;
import org.opengis.feature.Attribute;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeType;

/**
 * A node factory which creates special node pointers featurs.
 * <p>
 * The following types are supported:
 * <ul>
 * <li>{@link Attribute}
 * <li>{@link AttributeType}
 * </ul>
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * @author Gabriel Roldan, Axios Engineering
 * 
 *
 * @source $URL$
 */
public class AttributeNodePointerFactory implements NodePointerFactory {

    public int getOrder() {
        return 0;
    }

    public NodePointer createNodePointer(QName name, Object object, Locale locale) {
        
        /*
         * Do not handle SimpleFeature, which should be handled by FeatureNodeFactory, registered by
         * XPathPropertyAccessorFactory in gt-xsd-core. See GEOS-3525.
         */
        if (object instanceof Attribute && !(object instanceof SimpleFeature)) {
            return new AttributeNodePointer(null, (Attribute) object, name);
        }

        /*
         * if (object instanceof AttributeType) { return new
         * FeatureTypePointer(null, (AttributeType) object, name); }
         */

        return null;
    }

    public NodePointer createNodePointer(NodePointer parent, QName name, Object object) {

        /*
         * Do not handle SimpleFeature, which should be handled by FeatureNodeFactory, registered by
         * XPathPropertyAccessorFactory in gt-xsd-core. See GEOS-3525.
         */
        if (object instanceof Attribute && !(object instanceof SimpleFeature)) {
            return new AttributeNodePointer(parent, (Attribute) object, name);
        }

        /*
         * if (object instanceof AttributeType) { return new
         * FeatureTypePointer(null, (AttributeType) object, name); }
         */

        return null;
    }

}
