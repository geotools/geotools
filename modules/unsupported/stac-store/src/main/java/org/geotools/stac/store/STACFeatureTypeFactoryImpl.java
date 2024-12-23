/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.stac.store;

import java.util.List;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.util.InternationalString;
import org.geotools.feature.simple.SimpleFeatureTypeImpl;
import org.geotools.feature.type.FeatureTypeFactoryImpl;

/**
 * Allows creation of a subtype of a STAC item that references a property inside the "assets" top level JSON object (the
 * SimpleFeatureType machinery would have otherwise failed)
 */
class STACFeatureTypeFactoryImpl extends FeatureTypeFactoryImpl {

    @Override
    public SimpleFeatureType createSimpleFeatureType(
            Name name,
            List<AttributeDescriptor> schema,
            GeometryDescriptor defaultGeometry,
            boolean isAbstract,
            List<Filter> restrictions,
            AttributeType superType,
            InternationalString description) {
        return new STACSimpleFeatureTypeImpl(
                name, schema, defaultGeometry, isAbstract, restrictions, superType, description);
    }

    private static class STACSimpleFeatureTypeImpl extends SimpleFeatureTypeImpl {

        public STACSimpleFeatureTypeImpl(
                Name name,
                List<AttributeDescriptor> schema,
                GeometryDescriptor defaultGeometry,
                boolean isAbstract,
                List<Filter> restrictions,
                AttributeType superType,
                InternationalString description) {
            super(name, schema, defaultGeometry, isAbstract, restrictions, superType, description);
        }

        @Override
        public AttributeDescriptor getDescriptor(String name) {
            if (name.startsWith(STACFeatureSource.ASSETS + "/")) return super.getDescriptor(STACFeatureSource.ASSETS);
            return super.getDescriptor(name);
        }
    }
}
