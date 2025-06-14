/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008 Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.feature;

import java.util.Map;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.Name;

/**
 * Indicates a validation check has failed; the provided descriptor and value are available via this exception.
 *
 * @author Jody Garnett (Refractions Research, Inc.)
 * @since GeoAPI 2.2
 */
public class IllegalAttributeException extends IllegalArgumentException {
    private static final long serialVersionUID = 3373066465585246605L;

    private static final AttributeDescriptor NULL_ATTRIBUTE_DESCRIPTOR = new NullAttributeDescriptor();

    /** AttributeDescriptor being used to validate against. */
    private final AttributeDescriptor descriptor;

    /** Object that failed validation. */
    private final Object value;

    public IllegalAttributeException(String message) {
        this(NULL_ATTRIBUTE_DESCRIPTOR, null, message);
    }

    public IllegalAttributeException(AttributeDescriptor descriptor, Object value) {
        super();
        this.descriptor = descriptor;
        this.value = value;
    }

    public IllegalAttributeException(AttributeDescriptor descriptor, Object value, String message) {
        super(message);
        this.descriptor = descriptor;
        this.value = value;
    }

    public IllegalAttributeException(AttributeDescriptor descriptor, Object value, String message, Throwable t) {
        super(message, t);
        this.descriptor = descriptor;
        this.value = value;
    }

    public IllegalAttributeException(AttributeDescriptor descriptor, Object value, Throwable t) {
        super(t);
        this.descriptor = descriptor;
        this.value = value;
    }

    /**
     * AttribtueDescriptor being checked against.
     *
     * @return AttributeDescriptor being checked.
     */
    public AttributeDescriptor getDescriptor() {
        return descriptor;
    }

    /**
     * Attribute value that failed validation.
     *
     * @return Attribute value
     */
    public Object getValue() {
        return value;
    }

    @Override
    public String getMessage() {
        String s = getClass().getName();
        String message = getLocalizedMessage();

        StringBuilder buf = new StringBuilder();
        buf.append(s);
        if (message != null) {
            buf.append(":");
            buf.append(message);
        }
        if (descriptor != null) {
            buf.append(":");
            buf.append(descriptor.getName());
        }
        buf.append(" value:");
        buf.append(value);

        return buf.toString();
    }

    /** A descriptor for an attribute that does not exist. An ugly, ugly workaround for GEOT-2111/GEO-156. */
    private static class NullAttributeDescriptor implements AttributeDescriptor {

        @Override
        public int getMaxOccurs() {
            return 0;
        }

        @Override
        public int getMinOccurs() {
            return 0;
        }

        @Override
        public Name getName() {
            return null;
        }

        @Override
        public Map<Object, Object> getUserData() {
            return null;
        }

        @Override
        public boolean isNillable() {
            return false;
        }

        @Override
        public Object getDefaultValue() {
            return null;
        }

        @Override
        public String getLocalName() {
            return null;
        }

        @Override
        public AttributeType getType() {
            return null;
        }
    }
}
