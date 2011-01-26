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
package org.geotools.feature;

import org.opengis.feature.Association;
import org.opengis.feature.Attribute;
import org.opengis.feature.type.AssociationDescriptor;
import org.opengis.feature.type.AssociationType;
import org.opengis.feature.type.AttributeType;

public class AssociationImpl extends PropertyImpl implements Association {

    protected AssociationImpl(Attribute value, AssociationDescriptor descriptor) {
        super(value, descriptor);
    }

    public AttributeType getRelatedType() {
        return getType().getRelatedType();
    }

    public AssociationDescriptor getDescriptor() {
        return (AssociationDescriptor) super.getDescriptor();
    }
    
    public AssociationType getType() {
        return (AssociationType) super.getType();
    }
    
    public Attribute getValue() {
        return (Attribute) super.getValue();
    }
}
