/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.metadata.iso.extent;

import org.opengis.metadata.Identifier;
import org.opengis.metadata.extent.GeographicDescription;


/**
 * Description of the geographic area using identifiers.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class GeographicDescriptionImpl extends GeographicExtentImpl
        implements GeographicDescription
{
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 7250161161099782176L;

    /**
     * The identifier used to represent a geographic area.
     */
    private Identifier geographicIdentifier;

    /**
     * Constructs an initially empty geographic description.
     */
    public GeographicDescriptionImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public GeographicDescriptionImpl(final GeographicDescription source) {
        super(source);
    }

    /**
     * Creates a geographic description initialized to the specified value.
     */
     public GeographicDescriptionImpl(final Identifier geographicIdentifier) {
         setGeographicIdentifier(geographicIdentifier);
     }

    /**
     * Returns the identifier used to represent a geographic area.
     */
    public Identifier getGeographicIdentifier() {
        return geographicIdentifier;
    }

    /**
     * Set the identifier used to represent a geographic area.
     */
    public synchronized void setGeographicIdentifier(final Identifier newValue) {
        checkWritePermission();
        geographicIdentifier = newValue;
    }
}
