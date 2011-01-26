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

import java.util.List;

import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.util.InternationalString;

/**
 * Extension of FeatureTypeFactoryImpl which creates instances of DefaultFeatureType.
 * <p>
 * The point of this class is to maintain backwards compatability internally and
 * it should not be used by any client code. 
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 * @deprecated
 * @since 2.5
 *
 * @source $URL$
 */
public class DefaultFeatureTypeFactory2 extends FeatureTypeFactoryImpl {

    public SimpleFeatureType createSimpleFeatureType(Name name,
            List<AttributeDescriptor> schema,
            GeometryDescriptor defaultGeometry, boolean isAbstract,
            List<Filter> restrictions, AttributeType superType,
            InternationalString description) {
        return new DefaultFeatureType(name, schema, defaultGeometry, isAbstract,
                restrictions, superType, description);
    }
}
