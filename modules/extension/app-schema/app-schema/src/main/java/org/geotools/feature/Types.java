/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Map;

import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.data.complex.config.NonFeatureTypeProxy;
import org.geotools.xs.XSSchema;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.feature.type.PropertyType;

/**
 * This is a set of utility methods used when <b>implementing</b> types.
 * <p>
 * This set of classes captures the all important how does it work questions, particularly with
 * respect to super types.
 * </p>
 * 
 * @author Jody Garnett (Refractions Research)
 * @author Justin Deoliveira (The Open Planning Project)
 * 
 *
 *
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main
 *         /java/org/geotools/feature/Types.java $
 */
public class Types extends org.geotools.feature.type.Types {
    
    /**
     * Return true if an attribute from a type is an element.
     * 
     * @param type The type to search in.
     * @param att The attribute name.
     * @return True if the attribute exists in the type and is an element.
     */
    public static boolean isElement(ComplexType type, Name att) {
        PropertyDescriptor descriptor = Types.descriptor(type, att);
        if (descriptor == null) {
            return false;
        }
        Map<Object, Object> userData = descriptor.getUserData();
        if (userData.isEmpty()) {
            return false;
        }
        return userData.get(XSDElementDeclaration.class) != null;
    }
    
    /**
     * Return true if the type is either a simple type or has a simple type as its supertype. In
     * particular, complex types with simple content will return true.
     * 
     * @param type
     * @return
     */
    public static boolean isSimpleContentType(PropertyType type) {
        if (type == XSSchema.ANYSIMPLETYPE_TYPE) {
            // should never happen as this type is abstract
            throw new RuntimeException("Unexpected simple type");
        }
        PropertyType superType = type.getSuper();
        if (superType == XSSchema.ANYSIMPLETYPE_TYPE) {
            return true;
        } else if (superType == null) {
            return false;
        } else {
            return isSimpleContentType(superType);
        }
    }
    
    public static boolean isGeometryType(AttributeType type) {
        if (type instanceof GeometryType) {
            return true;
        }
        if (type instanceof NonFeatureTypeProxy) {
            if (((NonFeatureTypeProxy) type).getSubject() instanceof GeometryType) {
                return true;
            }
        }
        return false;
    }
}
