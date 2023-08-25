/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.coverage;

import org.geotools.api.geometry.Position;

/**
 * Thrown when a {@link Coverage#evaluate(Position, java.util.Set) evaluate} method is invoked for a
 * location outside the domain of the coverage.
 *
 * @version <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverage specification 1.0</A>
 * @author Martin Desruisseaux (IRD)
 * @author Alexander Petkov
 * @since GeoAPI 1.0
 * @see Coverage#evaluate(Position, byte[])
 * @see Coverage#evaluate(Position, double[])
 */
public class PointOutsideCoverageException extends CannotEvaluateException {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = -8718412090539227101L;

    /** Represents a direct position which is outside the domain of the coverage. */
    private Position offendingLocation;

    /** Creates an exception with no message. */
    public PointOutsideCoverageException() {
        super();
    }

    /**
     * Creates an exception with the specified message.
     *
     * @param message The detail message. The detail message is saved for later retrieval by the
     *     {@link #getMessage()} method.
     */
    public PointOutsideCoverageException(String message) {
        super(message);
    }

    /**
     * Returns the {@linkplain Position direct position} which is outside the domain of the
     * {@linkplain #getCoverage coverage}.
     *
     * @return The position outside the coverage, or {@code null} if unknown.
     * @since GeoAPI 2.2
     */
    public Position getOffendingLocation() {
        return offendingLocation;
    }

    /**
     * Sets the {@linkplain Position direct position} which is outside the domain of the {@linkplain
     * #getCoverage coverage}.
     *
     * @param location The position outside the coverage, or {@code null} if unknown.
     * @since GeoAPI 2.2
     */
    public void setOffendingLocation(final Position location) {
        this.offendingLocation = location;
    }
}
