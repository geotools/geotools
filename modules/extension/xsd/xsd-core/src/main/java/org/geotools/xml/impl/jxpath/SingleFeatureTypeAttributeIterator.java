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
 * Iterates over a single attribute of a feature type.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class SingleFeatureTypeAttributeIterator implements NodeIterator {
    /**
     * The feature type node pointer
     */
    FeatureTypePointer pointer;

    /**
     * The feature type
     */
    SimpleFeatureType featureType;

    /**
     * indedx of property
     */
    int index;

    /**
     * Creates the iteartor.
     *
     * @param pointer The pointer to the feature.
     * @param index The index of the property to iterate over
     */
    public SingleFeatureTypeAttributeIterator(FeatureTypePointer pointer, int index) {
        this.pointer = pointer;
        this.index = index;
        featureType = (SimpleFeatureType) pointer.getImmediateNode();
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
        return position < 2;
    }

    /**
     * Return a pointer to the property at the set index.
     */
    public NodePointer getNodePointer() {
        return new FeatureTypeAttributePointer(pointer, index);
    }
}
