/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
 *   
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.referencing.operation;

// OpenGIS dependencies
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.referencing.operation.CoordinateOperation;          // For javadoc
import org.opengis.referencing.operation.CoordinateOperationFactory;   // For javadoc
import org.opengis.referencing.operation.OperationNotFoundException;   // For javadoc


/**
 * Thrown when a transformation can't be performed because no path from
 * {@linkplain CoordinateOperation#getSourceCRS source CRS} to
 * {@linkplain CoordinateOperation#getTargetCRS target CRS} has been found.
 * This exception usually wraps an {@link OperationNotFoundException} thrown
 * by an {@linkplain CoordinateOperationFactory coordinate operation factory}.
 * This exception is sometime used in order to collapse a
 *
 * <blockquote><pre>
 * throws FactoryException, TransformException
 * </pre></blockquote>
 *
 * clause (in method signature) into a single
 *
 * <blockquote><pre>
 * throws TransformException
 * </pre></blockquote>
 *
 * clause, i.e. in order to hide the factory step into a more general transformation processus
 * from the API point of view.
 *
 * @since 2.2
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class TransformPathNotFoundException extends TransformException {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 5072333160296464925L;

    /**
     * Constructs an exception with no detail message.
     */
    public TransformPathNotFoundException() {
    }

    /**
     * Constructs an exception with the specified detail message.
     *
     * @param  cause The cause for this exception. The cause is saved
     *         for later retrieval by the {@link #getCause()} method.
     */
    public TransformPathNotFoundException(FactoryException cause) {
        super(cause.getLocalizedMessage(), cause);
    }

    /**
     * Constructs an exception with the specified detail message.
     *
     * @param  message The detail message. The detail message is saved
     *         for later retrieval by the {@link #getMessage()} method.
     */
    public TransformPathNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs an exception with the specified detail message and cause.
     *
     * @param  message The detail message. The detail message is saved
     *         for later retrieval by the {@link #getMessage()} method.
     * @param  cause The cause for this exception. The cause is saved
     *         for later retrieval by the {@link #getCause()} method.
     */
    public TransformPathNotFoundException(String message, FactoryException cause) {
        super(message, cause);
    }
}
