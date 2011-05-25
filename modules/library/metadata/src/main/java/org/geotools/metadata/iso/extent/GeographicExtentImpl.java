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

import org.opengis.metadata.extent.GeographicExtent;
import org.geotools.metadata.iso.MetadataEntity;


/**
 * Base class for geographic area of the dataset.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class GeographicExtentImpl extends MetadataEntity implements GeographicExtent {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -8844015895495563161L;

    /**
     * Indication of whether the bounding polygon encompasses an area covered by the data
     * (<cite>inclusion</cite>) or an area where data is not present (<cite>exclusion</cite>).
     */
    private Boolean inclusion;

    /**
     * Constructs an initially empty geographic extent.
     */
    public GeographicExtentImpl() {
    }

    /**
     * Constructs a geographic extent initialized to the same values than the specified one.
     *
     * @since 2.2
     */
    public GeographicExtentImpl(final GeographicExtent extent) {
        super(extent);
    }

    /**
     * Constructs a geographic extent initialized with the specified inclusion value.
     */
    public GeographicExtentImpl(final boolean inclusion) {
        setInclusion(Boolean.valueOf(inclusion));
    }

    /**
     * Indication of whether the bounding polygon encompasses an area covered by the data
     * (<cite>inclusion</cite>) or an area where data is not present (<cite>exclusion</cite>).
     *
     * @return {@code true} for inclusion, or {@code false} for exclusion.
     */
    public Boolean getInclusion() {
        return inclusion;
    }

    /**
     * Set whether the bounding polygon encompasses an area covered by the data
     * (<cite>inclusion</cite>) or an area where data is not present (<cite>exclusion</cite>).
     */
    public synchronized void setInclusion(final Boolean newValue) {
        checkWritePermission();
        inclusion = newValue;
    }
}
