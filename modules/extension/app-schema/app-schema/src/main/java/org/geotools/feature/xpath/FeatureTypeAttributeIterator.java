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

import java.util.List;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.geotools.feature.Types;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.PropertyDescriptor;

/**
 * A special iterator for iterating over the attributes of a feature type.
 * 
 * @author Niels Charlier (Curtin University of Technology)
 * 
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main/java/org/geotools/feature/xpath/FeatureTypeAttributeIterator.java $
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main
 *         /java/org/geotools/feature/xpath/FeatureTypeAttributeIterator.java $
 * 
 */
public class FeatureTypeAttributeIterator implements NodeIterator {
    /**
     * The feature type node pointer
     */
    protected NodePointer pointer;

    /**
     * The feature type
     */
    protected ComplexType featureType;

    protected List<PropertyDescriptor> children;

    /**
     * current position
     */
    protected int position;

    public FeatureTypeAttributeIterator(NodePointer pointer, ComplexType featureType) {
        this.pointer = pointer;
        this.featureType = featureType;
        
        //get list of descriptors from types and all supertypes
        children = Types.descriptors(featureType);
        
        position = 1;
    }

    public int getPosition() {
        return position;
    }

    public boolean setPosition(int position) {
        this.position = position;

        return position <= children.size();
    }

    public NodePointer getNodePointer() {
        return new FeatureTypeAttributePointer(pointer, featureType, children.get(position).getName());
    }
}
