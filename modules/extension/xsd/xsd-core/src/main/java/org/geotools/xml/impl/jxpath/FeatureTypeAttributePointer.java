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
import org.opengis.feature.simple.SimpleFeatureType;


/**
 * Pointer to a single attribute of a feature type.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public class FeatureTypeAttributePointer extends NodePointer {
    /**
     * the feature type
     */
    SimpleFeatureType featureType;

    /**
     * The parent pointer
     */
    FeatureTypePointer parent;

    /**
     * the indedx of hte property being pointed at
     */
    int index;

    /**
     * Creates the pointer.
     *
     * @param parent The parent pointer, pointer at the feature type.
     * @param index The index of hte property to point to
     */
    public FeatureTypeAttributePointer(FeatureTypePointer parent, int index) {
        super(parent);
        this.index = index;
        this.featureType = (SimpleFeatureType) parent.getImmediateNode();
    }

    /**
     * Return <code>true</code>.
     */
    public boolean isLeaf() {
        return true;
    }

    /**
     * Return <code>false</code>.
     */
    public boolean isCollection() {
        return false;
    }

    /**
     * Return <code>1</code>
     */
    public int getLength() {
        return 1;
    }

    /**
     * Returns the qname with prefix as <code>null</code>, and local part the name of the
     * feature attribute.
     */
    public QName getName() {
        return new QName(null, featureType.getDescriptor(index).getLocalName());
    }

    public Object getBaseValue() {
        return featureType;
    }

    public Object getImmediateNode() {
        return featureType.getDescriptor(index);
    }

    public void setValue(Object value) {
        throw new UnsupportedOperationException("Feautre types are immutable");
    }

    /**
     * Always return <code>0</code>, can never have child pointers.
     */
    public int compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {
        return 0;
    }
}
