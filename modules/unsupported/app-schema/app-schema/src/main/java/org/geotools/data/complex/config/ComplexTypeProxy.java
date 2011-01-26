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

package org.geotools.data.complex.config;

import java.util.Collection;
import java.util.Map;

import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;

/**
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 * @source $URL:
 *         http://svn.geotools.org/trunk/modules/unsupported/community-schemas/community-schema-ds/src/main/java/org/geotools/data/complex/config/ComplexTypeProxy.java $
 * @since 2.4
 */
class ComplexTypeProxy extends AttributeTypeProxy implements ComplexType {

    public ComplexTypeProxy(Name typeName, Map registry) {
        super(typeName, registry);
    }

    public PropertyDescriptor getDescriptor(Name name) {
        return ((ComplexType) getSubject()).getDescriptor(name);
    }

    public PropertyDescriptor getDescriptor(String name) {
        return ((ComplexType) getSubject()).getDescriptor(name);
    }

    public Collection<PropertyDescriptor> getDescriptors() {
        return ((ComplexType) getSubject()).getDescriptors();
    }

    public boolean isInline() {
        return ((ComplexType) getSubject()).isInline();
    }

}
