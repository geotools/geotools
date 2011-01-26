/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.coverage;

import org.opengis.geometry.DirectPosition;  // For Javadoc
import org.opengis.annotation.Extension;


/**
 * The base class for exceptions thrown when a quantity can't be evaluated.
 * This exception is usually invoked by a
 * <code>Coverage.{@linkplain Coverage#evaluate(DirectPosition, double[]) evaluate}(&hellip;)</code>
 * method, for example when a point is outside the coverage.
 *
 * @author  Martin Desruisseaux (IRD)
 * @author  Alexander Petkov
 * @since   GeoAPI 1.0
 *
 * @see Coverage#evaluate(DirectPosition, byte[])
 * @see Coverage#evaluate(DirectPosition, double[])
 */
public class CannotEvaluateException extends RuntimeException {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 506793649975583062L;

    /**
     * Represents the coverage for which this exception is thrown. Useful when {@link Coverage}
     * is used on a multilevel, so {@code PointOutsideCoverageException} can provide informative
     * details.
     */
    private Coverage coverage;

    /**
     * Creates an exception with no message.
     */
    public CannotEvaluateException() {
        super();
    }

    /**
     * Creates an exception with the specified message.
     *
     * @param  message The detail message. The detail message is saved for
     *         later retrieval by the {@link #getMessage()} method.
     */
    public CannotEvaluateException(String message) {
        super(message);
    }

    /**
     * Creates an exception with the specified message.
     *
     * @param  message The detail message. The detail message is saved for
     *         later retrieval by the {@link #getMessage()} method.
     * @param  cause The cause for this exception. The cause is saved
     *         for later retrieval by the {@link #getCause()} method.
     */
    public CannotEvaluateException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Returns the coverage for which this exception is thrown. Useful when {@link Coverage}
     * is used on a multilevel, so {@code CannotEvaluateException} can provide informative
     * details.
     *
     * @return The coverage, or {@code null} if unknown.
     *
     * @since GeoAPÏ 2.2
     */
    @Extension
    public Coverage getCoverage() {
        return coverage;
    }

    /**
     * Sets the coverage.
     *
     * @param coverage The coverage, or {@code null} if unknown.
     *
     * @since GeoAPÏ 2.2
     */
    @Extension
    public void setCoverage(final Coverage coverage) {
        this.coverage = coverage;
    }
}
