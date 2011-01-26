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
 * Thrown when an unexpected parameter was found in a
 * {@linkplain ParameterDescriptorGroup parameter group}.
 *
 * @version <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverage specification 1.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 */
@UML(identifier="GC_InvalidParameterName", specification=ISO_19111)
public class InvalidParameterNameException extends IllegalArgumentException {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -8473266898408204803L;

    /**
     * The invalid parameter name.
     */
    private final String parameterName;

    /**
     * Creates an exception with the specified message and parameter name.
     *
     * @param  message The detail message. The detail message is saved for
     *         later retrieval by the {@link #getMessage()} method.
     * @param parameterName The invalid parameter name.
     */
    public InvalidParameterNameException(String message, String parameterName) {
        super(message);
        this.parameterName = parameterName;
    }

    /**
     * Returns the invalid parameter name.
     *
     * @return The name of the invalid parameter.
     */
    public String getParameterName() {
        return parameterName;
    }
}
