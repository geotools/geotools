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

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.opengis.feature.simple.SimpleFeatureType;


/**
 * A special iteartor for iteating over the attributes of a feature type.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public class FeatureTypeAttributeIterator implements NodeIterator {
    /**
     * The feature type node pointer
     */
    FeatureTypePointer pointer;

    /**
     * The feature type
     */
    SimpleFeatureType featureType;

    /**
     * current position
     */
    int position;

    public FeatureTypeAttributeIterator(FeatureTypePointer pointer) {
        this.pointer = pointer;
        featureType = (SimpleFeatureType) pointer.getImmediateNode();
        position = 1;
    }

    public int getPosition() {
        return position;
    }

    public boolean setPosition(int position) {
        this.position = position;

        return position <= featureType.getAttributeCount();
    }

    public NodePointer getNodePointer() {
        return new FeatureTypeAttributePointer(pointer, position - 1);
    }
}
