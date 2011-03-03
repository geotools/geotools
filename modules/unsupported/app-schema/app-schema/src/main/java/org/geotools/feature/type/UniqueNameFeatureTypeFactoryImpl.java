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

import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.util.InternationalString;

/**
 * A specialisation of {@link FeatureTypeFactoryImpl} that returns {@link UniqueNameFeatureTypeImpl}
 * instead of {@link FeatureTypeImpl} to avoid equality tests on types with cyclic definitions.
 * <p>
 * 
 * Users of this factory must not use it to create multiple FeatureType instances with the same name
 * unless they represent the same type, because other parts of the implementation will assume they
 * are equal, and if they are not, Bad Things Will Happen.
 * 
 * @author Ben Caradoc-Davies, CSIRO Earth Science and Resource Engineering
 * @see GEOT-3354
 */
public class UniqueNameFeatureTypeFactoryImpl extends FeatureTypeFactoryImpl {

    /**
     * Override superclass to return {@link UniqueNameFeatureTypeImpl} instead of
     * {@link FeatureTypeImpl}.
     * 
     * @see org.geotools.feature.type.FeatureTypeFactoryImpl#createFeatureType(org.opengis.feature.type.Name,
     *      java.util.Collection, org.opengis.feature.type.GeometryDescriptor, boolean,
     *      java.util.List, org.opengis.feature.type.AttributeType,
     *      org.opengis.util.InternationalString)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public FeatureType createFeatureType(Name name, Collection schema,
            GeometryDescriptor defaultGeometry, boolean isAbstract, List restrictions,
            AttributeType superType, InternationalString description) {
        return new UniqueNameFeatureTypeImpl(name, schema, defaultGeometry, isAbstract,
                restrictions, superType, description);
    }

}
