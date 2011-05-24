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
 * Indicates that an operation is not allowed on a {@linkplain Geometry geometry} object
 * because it is unmodifiable. Note that unmodifiable geometries are not necessarily immutable;
 * they are just not allowed to be modified through the {@code setFoo(...)} method that
 * raised this exception. Whatever an unmodifiable geometry is immutable or not is
 * implementation dependent.
 *
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/geometry/UnmodifiableGeometryException.java $
 */
public class UnmodifiableGeometryException extends UnsupportedOperationException {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 8679047625299612669L;

    /**
     * Creates an exception with no message.
     */
    public UnmodifiableGeometryException() {
        super();
    }

    /**
     * Creates an exception with the specified message.
     *
     * @param  message The detail message. The detail message is saved for
     *         later retrieval by the {@link #getMessage()} method.
     */
    public UnmodifiableGeometryException(final String message) {
        super(message);
    }
}
