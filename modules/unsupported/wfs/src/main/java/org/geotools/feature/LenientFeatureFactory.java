/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.opengis.feature.Attribute;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

public class LenientFeatureFactory extends AbstractFeatureFactoryImpl {

    @Override
    public Attribute createAttribute(Object value,
            AttributeDescriptor descriptor, String id) {
        return new LenientAttribute( value, descriptor, id );
    }
    public SimpleFeature createSimpleFeature(List<Attribute> properties, SimpleFeatureType type, String id) {
        LenientFeature newFeature = new LenientFeature( properties, type, id );
        
//        List<Object> values = new ArrayList<Object>();
//        for( Attribute attribute : properties ){
//            values.add( attribute.getValue() );
//        }
//        newFeature.setAttributes(values);
//        
        return newFeature;
    }
}
