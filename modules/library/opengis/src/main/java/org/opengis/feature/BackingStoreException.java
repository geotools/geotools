/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.feature;

// J2SE direct dependencies
import java.io.IOException;    // For javadoc
import java.sql.SQLException;  // For javadoc


/**
 * Thrown to indicate that a {@link FeatureCollection} operation could not complete because of a
 * failure in the backing store, or a failure to contact the backing store. At the difference of
 * {@link FeatureStoreException}, this exception is unchecked in order to allows its usage with
 * the Java collection framework. This exception usually have an {@link IOException} or a
 * {@link SQLException} as its {@linkplain #getCause cause}. The cause may also be a
 * {@link org.opengis.referencing.operation.TransformException} if the feature collection
 * performs reprojection on the fly.
 *
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
public class BackingStoreException extends RuntimeException {
    /**
     * Serial version UID allowing cross compiler use of {@code BackingStoreException}.
     */
    private static final long serialVersionUID = 6069054157609700353L;

    /**
     * Constructs a new exception with no detail message.
     */
    public BackingStoreException() {
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message, saved for later retrieval by the {@link #getMessage} method.
     */
    public BackingStoreException(final String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified cause.
     *
     * @param cause the cause, saved for later retrieval by the {@link Throwable#getCause} method.
     */
    public BackingStoreException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message, saved for later retrieval by the {@link #getMessage} method.
     * @param cause the cause, saved for later retrieval by the {@link Throwable#getCause} method.
     */
    public BackingStoreException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
