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
package org.geotools.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.filter.expression.PropertyAccessor;
import org.geotools.filter.expression.PropertyAccessorFactory;
import org.geotools.filter.expression.PropertyAccessors;
import org.geotools.util.Converters;
import org.geotools.util.factory.Hints;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Defines a complex filter (could also be called logical filter). This filter holds one or more filters together and
 * relates them logically in an internally defined manner.
 *
 * @author Rob Hranac, TOPP
 * @version $Id$
 */
public class AttributeExpressionImpl extends DefaultExpression implements PropertyName {

    /** The logger for the default core module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(AttributeExpressionImpl.class);

    /** Holds all sub filters of this filter. */
    protected String attPath;

    /** Used to validate attribute references to ensure they match the provided schema */
    protected SimpleFeatureType schema = null;

    /** NamespaceSupport used to defining the prefix information for the xpath expression */
    public NamespaceSupport namespaceSupport;

    /**
     * Configures whether evaluate should return null if it cannot find a working property accessor, rather than
     * throwing an exception (default behaviour).
     */
    protected boolean lenient = true;

    /** Hints passed to the property accessor gathering up additional context information used during evaluation. */
    private Hints hints;

    // accessor caching, scanning the registry every time is really very expensive
    private volatile PropertyAccessor lastAccessor;

    /**
     * Constructor with the schema for this attribute.
     *
     * @param schema The schema for this attribute.
     */
    protected AttributeExpressionImpl(SimpleFeatureType schema) {
        this.schema = schema;
    }

    /**
     * Constructor with schema and path to the attribute.
     *
     * @param xpath the String xpath to the attribute.
     */
    public AttributeExpressionImpl(String xpath) {
        this.attPath = xpath;
        this.schema = null;
        this.namespaceSupport = null;
        this.hints = null;
    }

    /**
     * Constructor with full attribute name.
     *
     * @param name Attribute Name.
     */
    public AttributeExpressionImpl(Name name) {
        attPath = name.getLocalPart();
        schema = null;
        if (name.getNamespaceURI() != null) {
            namespaceSupport = new NamespaceSupport();
            namespaceSupport.declarePrefix("", name.getNamespaceURI());
        } else {
            namespaceSupport = null;
        }
    }

    /**
     * Constructor with schema and path to the attribute.
     *
     * @param xpath the String xpath to the attribute.
     * @param namespaceContext Defining the prefix information for the xpath expression
     */
    public AttributeExpressionImpl(String xpath, NamespaceSupport namespaceContext) {
        attPath = xpath;
        schema = null;
        this.namespaceSupport = namespaceContext;
    }

    /**
     * @param xpath xpath expression
     * @param hints Hints passed to the property accessor during evaulation
     */
    public AttributeExpressionImpl(String xpath, Hints hints) {
        attPath = xpath;
        schema = null;
        this.namespaceSupport = null;
        this.hints = hints;
    }

    @Override
    public NamespaceSupport getNamespaceContext() {
        return namespaceSupport;
    }

    /**
     * Constructor with schema and path to the attribute.
     *
     * @param schema The initial (required) sub filter.
     * @param attPath the xpath to the attribute.
     * @throws IllegalFilterException If the attribute path is not in the schema.
     */
    protected AttributeExpressionImpl(SimpleFeatureType schema, String attPath) throws IllegalFilterException {
        this.schema = schema;
        setPropertyName(attPath);
    }

    /**
     * Gets the path to the attribute to be evaluated by this expression.
     *
     * <p>{@link org.geotools.api.filter.expression.PropertyName#getPropertyName()}
     */
    @Override
    public String getPropertyName() {
        return attPath;
    }

    public void setPropertyName(String attPath) {
        LOGGER.entering("ExpressionAttribute", "setAttributePath", attPath);
        if (LOGGER.isLoggable(Level.FINEST)) LOGGER.finest("schema: " + schema + "\n\nattribute: " + attPath);

        if (schema != null) {
            if (schema.getDescriptor(attPath) != null) {
                this.attPath = attPath;
            } else {

                throw new IllegalFilterException(
                        "Attribute: " + attPath + " is not in stated schema " + schema.getTypeName() + ".");
            }
        } else {
            this.attPath = attPath;
        }
    }

    /**
     * Gets the value of this property from the passed object.
     *
     * @param obj Object from which we need to extract a property value.
     */
    @Override
    public Object evaluate(Object obj) {
        return evaluate(obj, null);
    }

    /**
     * Gets the value of this attribute from the passed object.
     *
     * @param obj Object from which to extract attribute value.
     * @param target Target Class
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T evaluate(Object obj, Class<T> target) {
        PropertyAccessor accessor = lastAccessor;

        Object value = false;
        boolean success = false;
        if (accessor != null && accessor.canHandle(obj, attPath, target)) {
            try {
                value = accessor.get(obj, attPath, target);
                success = true;
            } catch (Exception e) {
                // fine, we'll try another accessor
            }
        }

        // made it here, means an accessor needs to be found
        if (!success) {
            if (namespaceSupport != null && hints == null) {
                hints = new Hints(PropertyAccessorFactory.NAMESPACE_CONTEXT, namespaceSupport);
            }
            List<PropertyAccessor> accessors = PropertyAccessors.findPropertyAccessors(obj, attPath, target, hints);
            List<Exception> exceptions = null;
            if (accessors != null) {
                for (PropertyAccessor propertyAccessor : accessors) {
                    accessor = propertyAccessor;
                    try {
                        value = accessor.get(obj, attPath, target);
                        success = true;
                        break;
                    } catch (Exception e) {
                        // fine, we'll try another accessor
                        if (exceptions == null) {
                            exceptions = new ArrayList<>();
                        }
                        exceptions.add(e);
                    }
                }
            }

            if (!success) {
                if (lenient) return null;
                else {
                    IllegalArgumentException exception =
                            new IllegalArgumentException("Could not find working property accessor for attribute ("
                                    + attPath
                                    + ") in object ("
                                    + obj
                                    + ")");
                    if (exceptions != null) {
                        exceptions.forEach(e -> exception.addSuppressed(exception));
                    }
                    throw exception;
                }
            } else {
                lastAccessor = accessor;
            }
        }

        if (target == null) {
            return (T) value;
        }

        return Converters.convert(value, target);
    }

    /**
     * Return this expression as a string.
     *
     * @return String representation of this attribute expression.
     */
    @Override
    public String toString() {
        return attPath;
    }

    /**
     * Compares this filter to the specified object. Returns true if the passed in object is the same as this
     * expression. Checks to make sure the expression types are the same as well as the attribute paths and schemas.
     *
     * @param obj - the object to compare this ExpressionAttribute against.
     * @return true if specified object is equal to this filter; else false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (obj.getClass() == this.getClass()) {
            AttributeExpressionImpl expAttr = (AttributeExpressionImpl) obj;

            boolean isEqual = Filters.getExpressionType(expAttr) == Filters.getExpressionType(this);
            if (LOGGER.isLoggable(Level.FINEST))
                LOGGER.finest("expression type match:"
                        + isEqual
                        + "; in:"
                        + Filters.getExpressionType(expAttr)
                        + "; out:"
                        + Filters.getExpressionType(this));
            isEqual = expAttr.attPath != null
                    ? isEqual && expAttr.attPath.equals(this.attPath)
                    : isEqual && this.attPath == null;
            if (LOGGER.isLoggable(Level.FINEST))
                LOGGER.finest(
                        "attribute match:" + isEqual + "; in:" + expAttr.getPropertyName() + "; out:" + this.attPath);
            isEqual = expAttr.schema != null
                    ? isEqual && expAttr.schema.equals(this.schema)
                    : isEqual && this.schema == null;
            if (LOGGER.isLoggable(Level.FINEST))
                LOGGER.finest("schema match:" + isEqual + "; in:" + expAttr.schema + "; out:" + this.schema);

            return isEqual;
        } else {
            return false;
        }
    }

    /**
     * Override of hashCode method.
     *
     * @return a code to hash this object by.
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + (attPath == null ? 0 : attPath.hashCode());
        result = 37 * result + (schema == null ? 0 : schema.hashCode());
        return result;
    }

    /**
     * Used by FilterVisitors to perform some action on this filter instance. Typicaly used by Filter decoders, but may
     * also be used by any thing which needs infomration from filter structure. Implementations should always call:
     * visitor.visit(this); It is importatant that this is not left to a parent class unless the parents API is
     * identical.
     *
     * @param visitor The visitor which requires access to this filter, the method must call visitor.visit(this);
     */
    @Override
    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    /** Sets lenient property. */
    public void setLenient(boolean lenient) {
        this.lenient = lenient;
    }

    /**
     * Gets lenient property
     *
     * @return lenient
     */
    public boolean isLenient() {
        return lenient;
    }
}
