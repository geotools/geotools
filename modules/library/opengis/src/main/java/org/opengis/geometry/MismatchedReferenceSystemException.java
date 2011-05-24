/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry;


/**
 * Indicates that an object cannot be constructed because of a mismatch in the
 * {@linkplain org.opengis.referencing.ReferenceSystem reference systems} of
 * geometric components.
 *
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/geometry/MismatchedReferenceSystemException.java $
 */
public class MismatchedReferenceSystemException extends IllegalArgumentException {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 6222334569692693273L;

    /**
     * Creates an exception with no message.
     */
    public MismatchedReferenceSystemException() {
        super();
    }

    /**
     * Creates an exception with the specified message.
     *
     * @param  message The detail message. The detail message is saved for
     *         later retrieval by the {@link #getMessage()} method.
     */
    public MismatchedReferenceSystemException(final String message) {
        super(message);
    }

    /**
     * Creates an exception with the specified message and cause.
     *
     * @param  message The detail message. The detail message is saved for
     *         later retrieval by the {@link #getMessage()} method.
     * @param  cause The cause.
     */
    public MismatchedReferenceSystemException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
