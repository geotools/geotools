/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex.feature.type;

import java.util.Collection;
import java.util.Map;
import org.opengis.feature.Property;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;

/**
 * @author Gabriel Roldan (Axios Engineering)
 * @since 2.4
 */
public class ComplexTypeProxy extends AttributeTypeProxy implements ComplexType {

    public ComplexTypeProxy(Name typeName, Map registry) {
        super(typeName, registry);
    }

    @Override
    public PropertyDescriptor getDescriptor(Name name) {
        return ((ComplexType) getSubject()).getDescriptor(name);
    }

    @Override
    public PropertyDescriptor getDescriptor(String name) {
        return ((ComplexType) getSubject()).getDescriptor(name);
    }

    @Override
    public Collection<PropertyDescriptor> getDescriptors() {
        return ((ComplexType) getSubject()).getDescriptors();
    }

    @Override
    public boolean isInline() {
        return ((ComplexType) getSubject()).isInline();
    }

    @Override
    public Class<Collection<Property>> getBinding() {
        // TODO: dodgy, during tests subject is evaluated to a GeometryTypeImpl
        // too, casting to a ComplexType results in tests failures
        // in GeoServer app-schema tests, SimpleAttributeFeatureChainWfsTest
        @SuppressWarnings("unchecked")
        Class<Collection<Property>> result =
                (Class<Collection<Property>>) getSubject().getBinding();
        return result;
    }
}
