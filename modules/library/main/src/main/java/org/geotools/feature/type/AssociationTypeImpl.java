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
package org.geotools.feature.type;

import java.util.List;
import org.geotools.api.feature.type.AssociationType;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.util.InternationalString;
import org.geotools.util.Utilities;

public class AssociationTypeImpl extends PropertyTypeImpl implements AssociationType {

    protected final AttributeType relatedType;

    public AssociationTypeImpl(
            Name name,
            AttributeType referenceType,
            boolean isAbstract,
            List<Filter> restrictions,
            AssociationType superType,
            InternationalString description) {
        super(name, referenceType.getBinding(), isAbstract, restrictions, superType, description);
        this.relatedType = referenceType;
    }

    @Override
    public AttributeType getRelatedType() {
        return relatedType;
    }

    @Override
    public AssociationType getSuper() {
        return (AssociationType) super.getSuper();
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ relatedType.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof AssociationTypeImpl)) {
            return false;
        }

        AssociationType ass /*(tee hee)*/ = (AssociationType) other;

        return super.equals(ass) && Utilities.equals(relatedType, ass.getRelatedType());
    }

    @Override
    public String toString() {
        return new StringBuffer(super.toString())
                .append("; relatedType=[")
                .append(relatedType)
                .append("]")
                .toString();
    }
}
