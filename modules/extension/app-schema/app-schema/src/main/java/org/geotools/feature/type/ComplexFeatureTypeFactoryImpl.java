/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2011, Open Source Geospatial Foundation (OSGeo)
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
 * Feature type factory to produce complex feature type that can be used in feature chaining. The
 * specific complex feature type will have an additional system field called "FEATURE_LINK" that can
 * be used to link the feature type to its parent, i.e. allow the type to be nested.
 * 
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering) 
 *
 *
 *
 *
 * @source $URL$
 */
public class ComplexFeatureTypeFactoryImpl extends UniqueNameFeatureTypeFactoryImpl {

    @Override
    public FeatureType createFeatureType(Name name, Collection schema,
            GeometryDescriptor defaultGeometry, boolean isAbstract, List restrictions,
            AttributeType superType, InternationalString description) {

        return new ComplexFeatureTypeImpl(name, schema, defaultGeometry, isAbstract, restrictions,
                superType, description);
    }
}
