/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2015, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex.feature.type;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDContentTypeCategory;
import org.eclipse.xsd.XSDDerivationMethod;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.util.logging.Logging;
import org.geotools.xs.XSSchema;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.feature.type.PropertyType;

/**
 * This is a set of utility methods used when <b>implementing</b> types.
 *
 * <p>This set of classes captures the all important how does it work questions, particularly with
 * respect to super types.
 *
 * @author Jody Garnett (Refractions Research)
 * @author Justin Deoliveira (The Open Planning Project)
 */
public class Types extends org.geotools.feature.type.Types {

    private static final Logger LOGGER = Logging.getLogger(Types.class);

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
        if (type instanceof ComplexTypeProxy) {
            if (((ComplexTypeProxy) type).getSubject() instanceof GeometryType) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the type is either <code>xs:anyType</code> or is derived from <code>
     * xs:anyType</code> by extension and has mixed content.
     *
     * <p>Example:
     *
     * <pre>
     *  &lt;complexType name="TestType"&gt;
     *    &lt;complexContent&gt;
     *      &lt;extension base="anyType"&gt;
     *        &lt;attribute name="attr1" type="string" /&gt;
     *      &lt;/extension&gt;
     *    &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     * </pre>
     */
    public static boolean canHaveTextContent(PropertyType type) {
        if (type == XSSchema.ANYTYPE_TYPE) {
            return true;
        }
        PropertyType superType = type.getSuper();
        if (superType == XSSchema.ANYTYPE_TYPE) {
            // type was derived from xs:anyType: check derivation mode and content type category
            Map<Object, Object> userData = type.getUserData();
            if (userData != null && userData.get(XSDTypeDefinition.class) != null) {
                XSDTypeDefinition typeDef =
                        (XSDTypeDefinition) userData.get(XSDTypeDefinition.class);
                if (typeDef instanceof XSDComplexTypeDefinition) {
                    XSDComplexTypeDefinition complexTypeDef = (XSDComplexTypeDefinition) typeDef;
                    XSDContentTypeCategory category = complexTypeDef.getContentTypeCategory();
                    XSDDerivationMethod derivMethod = complexTypeDef.getDerivationMethod();

                    boolean hasMixedContent = XSDContentTypeCategory.MIXED_LITERAL.equals(category);
                    boolean isExtension = XSDDerivationMethod.EXTENSION_LITERAL.equals(derivMethod);
                    return isExtension && hasMixedContent;
                }
            } else {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("No XSDTypeDefinition found for type " + type.getName());
                }
            }
        }
        return false;
    }
}
