/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.util;


/**
 * Exception that is thrown when an invalid enumeration lookup is performed
 * in the {@link SimpleEnumerationType} class.
 *
 * @author Jesse Crossley (SYS Technologies)
 * @since GeoAPI 1.0
 *
 * @deprecated Not used.
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/util/NoSuchEnumerationException.java $
 */
@Deprecated
public class NoSuchEnumerationException extends Exception {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 5827953825646995065L;

    /**
     * The invalid value.
     */
    private final int value;

    /**
     * Constructs an exception with the given invalid value.
     *
     * @param value The invalid value.
     * @todo Localize the error message.
     */
    public NoSuchEnumerationException(final int value) {
        super("No enumeration exists for the value " + value);
        this.value = value;
    }

    /**
     * Returns the invalid value.
     */
    public int getValue() {
        return value;
    }
}

