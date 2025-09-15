/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.referencing;

import java.io.Serial;

/**
 * Thrown when a {@linkplain Factory factory} can't create an instance of the requested object. It may be a failure to
 * create a {@linkplain org.geotools.api.referencing.datum.Datum datum}, a
 * {@linkplain org.geotools.api.referencing.cs.CoordinateSystem coordinate system}, a
 * {@linkplain org.geotools.api.referencing.ReferenceSystem reference system} or a
 * {@linkplain org.geotools.api.referencing.operation.CoordinateOperation coordinate operation}.
 *
 * <p>If the failure is caused by an illegal authority code, then the actual exception should be
 * {@link NoSuchAuthorityCodeException}. Otherwise, if the failure is caused by some error in the underlying database
 * (e.g. {@link java.io.IOException} or {@link java.sql.SQLException}), then this cause should be specified.
 *
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 * @see org.geotools.api.referencing.operation.CoordinateOperationFactory
 */
public class FactoryException extends Exception {
    /** Serial number for interoperability with different versions. */
    @Serial
    private static final long serialVersionUID = -3414250034883898315L;

    /** Construct an exception with no detail message. */
    public FactoryException() {}

    /**
     * Construct an exception with the specified detail message.
     *
     * @param message The detail message. The detail message is saved for later retrieval by the {@link #getMessage()}
     *     method.
     */
    public FactoryException(String message) {
        super(message);
    }

    /**
     * Construct an exception with the specified cause. The detail message is copied from the cause
     * {@linkplain Exception#getLocalizedMessage localized message}.
     *
     * @param cause The cause for this exception. The cause is saved for later retrieval by the {@link #getCause()}
     *     method.
     */
    public FactoryException(Exception cause) {
        super(cause.getLocalizedMessage(), cause);
    }

    /**
     * Construct an exception with the specified detail message and cause. The cause is the exception thrown in the
     * underlying database (e.g. {@link java.io.IOException} or {@link java.sql.SQLException}).
     *
     * @param message The detail message. The detail message is saved for later retrieval by the {@link #getMessage()}
     *     method.
     * @param cause The cause for this exception. The cause is saved for later retrieval by the {@link #getCause()}
     *     method.
     */
    public FactoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
