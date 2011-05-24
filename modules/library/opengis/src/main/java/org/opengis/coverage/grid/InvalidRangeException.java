/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.coverage.grid;

import org.opengis.annotation.UML;
import static org.opengis.annotation.Specification.*;


/**
 * Thrown when a {@linkplain GridRange grid range} is out of
 * {@linkplain GridCoverage grid coverage} bounds.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/coverage/grid/InvalidRangeException.java $
 * @version <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverage specification 1.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 */
@UML(identifier="GC_InvalidRange", specification=OGC_01004)
public class InvalidRangeException extends IllegalArgumentException {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 3165512862939920847L;

    /**
     * Creates an exception with no message.
     */
    public InvalidRangeException() {
        super();
    }

    /**
     * Creates an exception with the specified message.
     *
     * @param  message The detail message. The detail message is saved for
     *         later retrieval by the {@link #getMessage()} method.
     */
    public InvalidRangeException(String message) {
        super(message);
    }
}
