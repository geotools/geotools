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
 * Thrown when an attempt is made to write in a non-editable grid.
 *
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/coverage/grid/GridNotEditableException.java $
 * @version <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverage specification 1.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 *
 * @see GridCoverage#isDataEditable
 *
 */
@UML(identifier="GC_GridNotEditable", specification=OGC_01004)
public class GridNotEditableException extends IllegalStateException {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 612186655921122650L;

    /**
     * Creates an exception with no message.
     */
    public GridNotEditableException() {
        super();
    }

    /**
     * Creates an exception with the specified message.
     *
     * @param  message The detail message. The detail message is saved for
     *         later retrieval by the {@link #getMessage()} method.
     */
    public GridNotEditableException(String message) {
        super(message);
    }
}
