/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.Name;

/**
 * Iterates over a single attribute of a feature type.
 * 
 * @author Niels Charlier (Curtin University of Technology)
 * 
 *
 *
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main
 *         /java/org/geotools/feature/xpath/SingleFeatureTypeAttributeIterator.java $
 * 
 */

public class SingleFeatureTypeAttributeIterator implements NodeIterator {

    /**
     * The feature type node pointer
     */
    protected NodePointer pointer;

    protected Name name;

    protected ComplexType featureType;

    /**
     * Creates the iteartor.
     * 
     * @param pointer
     *            The pointer to the feature.
     * @param index
     *            The index of the property to iterate over
     */
    public SingleFeatureTypeAttributeIterator(NodePointer pointer, ComplexType featureType,
            Name name) {
        this.pointer = pointer;
        this.name = name;
        this.featureType = featureType;
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
        return new FeatureTypeAttributePointer(pointer, featureType, name);
    }
}
