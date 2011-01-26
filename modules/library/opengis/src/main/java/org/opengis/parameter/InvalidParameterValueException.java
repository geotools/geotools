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

import org.opengis.annotation.UML;
import static org.opengis.annotation.Specification.*;


/**
 * Thrown when an invalid value was given to a {@linkplain ParameterValue parameter}.
 *
 * @version <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverage specification 1.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 *
 * @see ParameterValue#setValue(int)
 * @see ParameterValue#setValue(double)
 * @see ParameterValue#setValue(Object)
 */
@UML(identifier="GC_InvalidParameterValue", specification=ISO_19111)
public class InvalidParameterValueException extends IllegalArgumentException {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 3814037056147642789L;

    /**
     * The parameter name.
     */
    private final String parameterName;

    /**
     * The invalid parameter value.
     */
    private final Object value;

    /**
     * Creates an exception with the specified invalid value.
     *
     * @param message The detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     * @param parameterName The parameter name.
     * @param value The invalid parameter value.
     */
    public InvalidParameterValueException(String message, String parameterName, Object value) {
        super(message);
        this.parameterName = parameterName;
        this.value = value;
    }

    /**
     * Creates an exception with the specified invalid value as a floating point.
     *
     * @param  message The detail message. The detail message is saved for
     *         later retrieval by the {@link #getMessage()} method.
     * @param  parameterName The parameter name.
     * @param  value The invalid parameter value.
     */
    public InvalidParameterValueException(String message, String parameterName, double value) {
        this(message, parameterName, Double.valueOf(value));
    }

    /**
     * Creates an exception with the specified invalid value as an integer.
     *
     * @param  message The detail message. The detail message is saved for
     *         later retrieval by the {@link #getMessage()} method.
     * @param  parameterName The parameter name.
     * @param  value The invalid parameter value.
     */
    public InvalidParameterValueException(String message, String parameterName, int value) {
        this(message, parameterName, Integer.valueOf(value));
    }

    /**
     * Returns the parameter name.
     *
     * @return The parameter name.
     */
    public String getParameterName() {
        return parameterName;
    }

    /**
     * Returns the invalid parameter value.
     *
     * @return The invalid parameter value.
     */
    public Object getValue() {
        return value;
    }
}
