/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.referencing;


/**
 * Thrown when a {@linkplain org.opengis.referencing.operation.MathTransform math transform}
 * as been requested with an unknow {@linkplain org.opengis.referencing.operation.OperationMethod
 * operation method} identifier.
 *
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 *
 * @see org.opengis.referencing.operation.MathTransformFactory#createParameterizedTransform
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/referencing/NoSuchIdentifierException.java $
 */
public class NoSuchIdentifierException extends FactoryException {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -6846799994429345902L;

    /**
     * The {@linkplain Identifier#getCode identifier code}.
     */
    private final String identifier;

    /**
     * Constructs an exception with the specified detail message and classification name.
     *
     * @param  message The detail message. The detail message is saved
     *         for later retrieval by the {@link #getMessage()} method.
     * @param identifier {@linkplain ReferenceIdentifier#getCode identifier code}.
     */
    public NoSuchIdentifierException(final String message, final String identifier) {
        super(message);
        this.identifier = identifier;
    }

    /**
     * Returns the {@linkplain ReferenceIdentifier#getCode identifier code}.
     *
     * @return The identifier code.
     */
    public String getIdentifierCode() {
        return identifier;
    }
}
