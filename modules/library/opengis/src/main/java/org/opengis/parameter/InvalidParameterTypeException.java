/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.parameter;


/**
 * Thrown when a parameter can't be cast to the requested type. For example this exception
 * is thrown when {@link ParameterValue#doubleValue} is invoked but the value is not
 * convertible to a {@code double}.
 * <p>
 * <b>Note:</b> This exception is of kind "{@linkplain IllegalStateException illegal state}"
 * rather than "{@linkplain IllegalArgumentException illegal argument}" because it is not
 * caused by a bad argument. It is rather a consequence of invoking the wrong zero-argument
 * method.
 *
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 *
 * @see ParameterValue#intValue
 * @see ParameterValue#doubleValue
 */
public class InvalidParameterTypeException extends IllegalStateException {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 2740762597003093176L;

    /**
     * The invalid parameter name.
     */
    private final String parameterName;

    /**
     * Creates an exception with the specified message and parameter name.
     *
     * @param message The detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     * @param parameterName The parameter name.
     */
    public InvalidParameterTypeException(String message, String parameterName) {
        super(message);
        this.parameterName = parameterName;
    }

    /**
     * Returns the parameter name.
     *
     * @return The parameter name.
     */
    public String getParameterName() {
        return parameterName;
    }
}
