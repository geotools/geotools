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
package org.geotools.filter.visitor;

import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.expression.PropertyName;

/**
 * Resolves all property name references in a filter against a particular feature type. 
 * <p>
 * This visitor is used to handle property accesses such as "gml:name", "//foo" etc..  Each 
 * such reference is resolved against the feature type and replaced with the actual name of
 * the attribute, ie "gml:name" => "name", "//foo" => "foo".
 * </p>
 * 
 * @author Justin Deoliveira, OpenGEO
 * @since 2.6
 *
 *
 * @source $URL$
 */
public class PropertyNameResolvingVisitor extends DuplicatingFilterVisitor {

    /** the feature type */
    SimpleFeatureType featureType;

    public PropertyNameResolvingVisitor( SimpleFeatureType featureType ) {
        if ( featureType == null ) {
            throw new NullPointerException( "featureType" );
        }
        
        this.featureType = featureType;
    }
    
    public Object visit(PropertyName expression, Object extraData) {
        AttributeDescriptor att = expression.evaluate( featureType, null);
        if ( att != null ) {
            return getFactory(extraData).property( att.getLocalName() );
        }
        return super.visit(expression, extraData);
    }
}
