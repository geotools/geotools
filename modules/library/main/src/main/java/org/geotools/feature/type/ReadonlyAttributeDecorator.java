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
package org.geotools.feature.type;

import java.util.Map;

import org.opengis.feature.Attribute;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;
import org.opengis.filter.identity.Identifier;

/**
 * Readonly wrapper around the provided Attribute.
 * <p>
 * This class is used by Types in order to protect provided attributes from modification
 * during evaluation.
 *
 * @source $URL$
 */
public final class ReadonlyAttributeDecorator implements Attribute {
    private final Attribute delegate;

    public ReadonlyAttributeDecorator(Attribute delegate) {
        this.delegate = delegate;
    }

    public AttributeType getType() {
        return delegate.getType();
    }

    public Identifier getIdentifier() {
        return delegate.getIdentifier();
    }

    public Object getValue() {
        return delegate.getValue();
    }

    public void setValue(Object newValue) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Modification is not supported");
    }

    public AttributeDescriptor getDescriptor() {
        return delegate.getDescriptor();
    }

    public Name getName() {
        return delegate.getName();
    }

    public Map<Object, Object> getUserData() {
        return delegate.getUserData();
    }

    public boolean isNillable() {
        return delegate.isNillable();
    }
    public void validate() {
        delegate.validate();
    }
}
