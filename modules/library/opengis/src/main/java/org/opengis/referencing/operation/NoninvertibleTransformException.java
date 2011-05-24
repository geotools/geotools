/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.referencing.operation;


/**
 * Thrown when {@link MathTransform#inverse} is
 * invoked but the transform can't be inverted.
 *
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 *
 * @see org.opengis.referencing.operation.CoordinateOperationFactory
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/referencing/operation/NoninvertibleTransformException.java $
 */
public class NoninvertibleTransformException extends TransformException {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 9184806660368158575L;

    /**
     * Construct an exception with no detail message.
     */
    public NoninvertibleTransformException() {
    }

    /**
     * Construct an exception with the specified detail message.
     *
     * @param  message The detail message. The detail message is saved
     *         for later retrieval by the {@link #getMessage()} method.
     */
    public NoninvertibleTransformException(String message) {
        super(message);
    }

    /**
     * Construct an exception with the specified detail message and cause. The cause
     * is typically an other {@link java.lang.geom.NoninvertibleTransformException}
     * emitted by Java2D.
     *
     * @param  message The detail message. The detail message is saved
     *         for later retrieval by the {@link #getMessage()} method.
     * @param  cause The cause for this exception. The cause is saved
     *         for later retrieval by the {@link #getCause()} method.
     */
    public NoninvertibleTransformException(String message, Throwable cause) {
        super(message, cause);
    }
}
