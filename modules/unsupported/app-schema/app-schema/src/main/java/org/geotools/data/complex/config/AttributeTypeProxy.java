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
import java.util.List;
import java.util.Map;

import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.util.InternationalString;

/**
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 * @source $URL$
 * @since 2.4
 */
class AttributeTypeProxy implements AttributeType {

    private Name typeName;

    private Map registry;

    private AttributeType subject;

    public AttributeTypeProxy(Name typeName, Map registry) {
        this.typeName = typeName;
        this.registry = registry;
    }

    public AttributeType getSubject() {
        if (subject == null) {
            subject = (AttributeType) registry.get(typeName);
            if (subject == null) {
                throw new IllegalStateException("Subject type not loaded yet");
            }
        }
        return subject;
    }

    public Class getBinding() {
        return getSubject().getBinding();
    }

    public Collection getOperations() {
        return null;
    }

    public List<Filter> getRestrictions() {
        return getSubject().getRestrictions();
    }

    public AttributeType getSuper() {
        return getSubject().getSuper();
    }

    public boolean isAbstract() {
        return getSubject().isAbstract();
    }

    public boolean isIdentified() {
        return getSubject().isIdentified();
    }

    public InternationalString getDescription() {
        return getSubject().getDescription();
    }

    public Name getName() {
        return typeName;
    }

    public Map<Object, Object> getUserData() {
        return getSubject().getUserData();
    }

    public boolean equals(Object o) {
        AttributeType subject = getSubject();
        return subject.equals(o);
    }

    public int hashCode() {
        AttributeType subject = getSubject();
        return subject.hashCode();
    }
}
