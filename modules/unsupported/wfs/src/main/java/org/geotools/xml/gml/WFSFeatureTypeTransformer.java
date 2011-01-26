/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml.gml;

import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A sad hack class until the new Feature Model comes around. 
 * 
 * @see ChoiceAttributeType
 * @author Jesse
 * @since 1.1.0
 *
 * @source $URL$
 */
public class WFSFeatureTypeTransformer {

    public static SimpleFeatureType transform( SimpleFeatureType schema, CoordinateReferenceSystem crs ) throws SchemaException {
        SimpleFeatureTypeBuilder build = new SimpleFeatureTypeBuilder();
        build.setName( schema.getName() );
        //build.setDefaultGeometry( schema.getDefaultGeometry().getLocalName() );
        
        GeometryDescriptor defaultGeometryType = null;
        for( int i = 0; i < schema.getAttributeCount(); i++ ) {
            AttributeDescriptor attributeType = schema.getDescriptor(i);
            if( attributeType instanceof ChoiceGeometryType ){
                defaultGeometryType = handleChoiceGeometryAttribute(schema, crs, build, defaultGeometryType, attributeType);
            }
            else if (attributeType instanceof GeometryDescriptor) {
                defaultGeometryType = handleGeometryAttribute(schema, crs, build, defaultGeometryType, attributeType);
            }
            else {
                build.add(attributeType);
            }
        }
        //Only try to set default geometry when there actually is a geometry type
        if(defaultGeometryType!=null){
           build.setDefaultGeometry(defaultGeometryType.getLocalName());  
        }
        return build.buildFeatureType();
    }

    private static GeometryDescriptor handleGeometryAttribute( SimpleFeatureType schema, CoordinateReferenceSystem crs, SimpleFeatureTypeBuilder factory, GeometryDescriptor defaultGeometryType, AttributeDescriptor attributeType ) {
        GeometryDescriptor geometryType = (GeometryDescriptor) attributeType;
        GeometryDescriptor geometry;

        AttributeTypeBuilder builder = new AttributeTypeBuilder();
        builder.setName( geometryType.getLocalName()  );
        builder.setBinding( geometryType.getType().getBinding() );
        builder.setNillable( geometryType.isNillable() );
        
        //builder.setDefaultValue(defaultValue);
        builder.setCRS( crs );
        
        geometry = builder.buildDescriptor( geometryType.getLocalName(), builder.buildGeometryType() );
        
//        geometry = (GeometryDescriptor) AttributeTypeFactory.newAttributeType(
//                geometryType.getLocalName(), geometryType.getBinding(), geometryType.isNillable(),
//                0, geometryType.createDefaultValue(), crs);

        if (defaultGeometryType == null || geometryType == schema.getGeometryDescriptor()) {
            defaultGeometryType = geometry;
        }
        factory.add(geometry);
        return defaultGeometryType;
    }

    private static GeometryDescriptor handleChoiceGeometryAttribute( SimpleFeatureType schema, CoordinateReferenceSystem crs, SimpleFeatureTypeBuilder factory, GeometryDescriptor defaultGeometryType, AttributeDescriptor attributeType ) {
        ChoiceGeometryTypeImpl geometryType = (ChoiceGeometryTypeImpl) attributeType;
        ChoiceGeometryTypeImpl geometry;

        geometry = new ChoiceGeometryTypeImpl(
            geometryType.getName(), geometryType.getChoices(), geometryType.getBinding(), geometryType.isNillable(),
            geometryType.getMinOccurs(), geometryType.getMaxOccurs(), geometryType.createDefaultValue(), crs, geometryType.getRestrictions());

        if (defaultGeometryType == null || geometryType == schema.getGeometryDescriptor()) {
            defaultGeometryType = geometry;
        }
        factory.add(geometry);
        return defaultGeometryType;
    }

}
