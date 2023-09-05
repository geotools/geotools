/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.coverage.grid;

/**
 * Thrown when an attempt is made to write in a non-editable grid.
 *
 * @version <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverage specification 1.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 * @see GridCoverage#isDataEditable
 */
public class GridNotEditableException extends IllegalStateException {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = 612186655921122650L;

    /** Creates an exception with no message. */
    public GridNotEditableException() {
        super();
    }

    /**
     * Creates an exception with the specified message.
     *
     * @param message The detail message. The detail message is saved for later retrieval by the
     *     {@link #getMessage()} method.
     */
    public GridNotEditableException(String message) {
        super(message);
    }
}
