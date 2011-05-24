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

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * Thrown when a {@linkplain CoordinateOperation coordinate operation} is not found.
 * It may be because there is no known path between source and target
 * {@linkplain CoordinateReferenceSystem coordinate reference systems},
 * or because the requested operation is not available in the environment.
 *
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/referencing/operation/OperationNotFoundException.java $
 */
public class OperationNotFoundException extends FactoryException {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -382625493416204214L;

    /**
     * Construct an exception with no detail message.
     */
    public OperationNotFoundException() {
    }

    /**
     * Construct an exception with the specified detail message.
     *
     * @param message The details message.
     */
    public OperationNotFoundException(final String message) {
        super(message);
    }

    /**
     * Construct an exception with the specified detail message and cause.
     *
     * @param message The details message.
     * @param cause The cause for this exception.
     */
    public OperationNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
