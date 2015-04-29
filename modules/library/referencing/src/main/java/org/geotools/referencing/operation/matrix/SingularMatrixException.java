package org.geotools.referencing.operation.matrix;

import java.awt.geom.NoninvertibleTransformException;

/**
 * Matrix is singular, and thus an inverse is not available.
 *
 * This is a Throwable version of !@link java.awt.geom.NoninvertibleTransformException}
 * 
 * @see java.awt.geom.NoninvertibleTransformException
 * @author jody
 *
 */
public class SingularMatrixException extends RuntimeException {
    private static final long serialVersionUID = 7539276472682701858L;

    /** Matrix is singular (often indicating an inverse is not available) */
    public SingularMatrixException(String message) {
        super(message);
    }

    /**
     * Construct using provided message and cause
     * 
     * @param message
     * @param cause
     */
    public SingularMatrixException(String message, Throwable cause) {
        super(message, cause);
    }

    public SingularMatrixException(NoninvertibleTransformException nonInvertable) {
        super(nonInvertable.getMessage(), nonInvertable);
    }
}
