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

import java.util.Map;

import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;


/**
 * Indicates client class has attempted to create an invalid feature.
 * @source $URL$
 * @deprecated Please use org.opengis.feature.IllegalAttributeException
 */
public class IllegalAttributeException extends org.opengis.feature.IllegalAttributeException {
    
    private static final AttributeDescriptor NULL_ATTRIBUTE_DESCRIPTOR = new NullAttributeDescriptor();
    
    /**
     * Constructor with message argument.
     *
     * @param message Reason for the exception being thrown
     */
    public IllegalAttributeException(String message) {
        super(NULL_ATTRIBUTE_DESCRIPTOR,null,message);
    }

    /**
     * Constructor that makes the message given the expected and invalid.
     *
     * @param expected the expected AttributeType.
     * @param invalid the attribute that does not validate against expected.
     */
    public IllegalAttributeException(AttributeDescriptor expected, Object invalid) {
        super(expected, invalid );
    }

    /**
     * Constructor that makes the message given the expected and invalid, along
     * with the root cause.
     *
     * @param expected the expected AttributeType.
     * @param invalid the attribute that does not validate against expected.
     * @param cause the root cause of the error.
     */
    public IllegalAttributeException(AttributeDescriptor expected, Object invalid, Throwable cause) {
        super( expected, invalid, cause );
    }
    
    /**
     * A descriptor for an attribute that does not exist. An ugly, ugly workaround for
     * GEOT-2111/GEO-156.
     */
    private static class NullAttributeDescriptor implements AttributeDescriptor {

        public int getMaxOccurs() {
            return 0;
        }

        public int getMinOccurs() {
            return 0;
        }

        public Name getName() {
            return null;
        }

        public Map<Object, Object> getUserData() {
            return null;
        }

        public boolean isNillable() {
            return false;
        }

        public Object getDefaultValue() {
            return null;
        }

        public String getLocalName() {
            return null;
        }

        public AttributeType getType() {
            return null;
        }
        
    }

}
