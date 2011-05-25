/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Collection;
import java.util.List;

import org.geotools.gml2.FeatureTypeCache;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.util.InternationalString;

/**
 * A specialisation of {@link FeatureTypeImpl} that avoids equality tests on feature types with
 * cyclic definitions by considering features types to be equal if and only if their names are
 * equal.
 * 
 * <p>
 * 
 * Users of this class must not create multiple instances with the same name unless they represent
 * the same type, because other parts of the implementation will assume they are equal, and if they
 * are not, Bad Things Will Happen.
 * 
 * <p>
 * 
 * It should be noted that app-schema does not support the multiple definition XSD types with the
 * same name. This restriction allows multiple XSD elements and thus WFS feature types (with
 * different names) to have the same XSD type, because the XSD type can be recognised by name even
 * if it has a cyclic definition. This simplified equality testing allows {@link FeatureTypeCache}
 * to handle these types, despite their cyclic definition preventing a full recursive implementation
 * of {@link #equals(Object)}equals(). Unit test coverage is in GeoServer app-schema-test
 * DuplicateTypeTest.
 * 
 * @author Ben Caradoc-Davies, CSIRO Earth Science and Resource Engineering
 * @see GEOT-3354
 *
 * @source $URL$
 */
public class UniqueNameFeatureTypeImpl extends FeatureTypeImpl {

    public UniqueNameFeatureTypeImpl(Name name, Collection<PropertyDescriptor> schema,
            GeometryDescriptor defaultGeometry, boolean isAbstract, List<Filter> restrictions,
            AttributeType superType, InternationalString description) {
        super(name, schema, defaultGeometry, isAbstract, restrictions, superType, description);
    }

    /**
     * Delegates to type name {@link Name#hashCode()}.
     * 
     * @see org.geotools.feature.type.FeatureTypeImpl#hashCode()
     */
    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    /**
     * Delegates to the type name {@link Name#equals(Object)}.
     * 
     * @see org.geotools.feature.type.FeatureTypeImpl#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof UniqueNameFeatureTypeImpl)) {
            return false;
        } else {
            return getName().equals(((FeatureType) other).getName());
        }
    }

}
