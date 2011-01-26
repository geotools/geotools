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
package org.geotools.xml.impl.jxpath;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.NodePointerFactory;
import java.util.Locale;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;


/**
 * A node factory which creates special node pointers featurs.
 * <p>
 * The following types are supported:
 * <ul>
 *         <li>{@link org.geotools.feature.Feature}
 *         <li>{@link org.geotools.feature.FeatureType}
 * </ul>
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public class FeatureNodeFactory implements NodePointerFactory {
    public int getOrder() {
        return 0;
    }

    public NodePointer createNodePointer(QName name, Object object, Locale locale) {
        if (object instanceof SimpleFeature) {
            return new FeaturePointer(null, (SimpleFeature) object, name);
        }

        if (object instanceof SimpleFeatureType) {
            return new FeatureTypePointer(null, (SimpleFeatureType) object, name);
        }

        return null;
    }

    public NodePointer createNodePointer(NodePointer parent, QName name, Object object) {
        if (object instanceof SimpleFeature) {
            return new FeaturePointer(parent, (SimpleFeature) object, name);
        }

        if (object instanceof SimpleFeatureType) {
            return new FeatureTypePointer(null, (SimpleFeatureType) object, name);
        }

        return null;
    }
}
