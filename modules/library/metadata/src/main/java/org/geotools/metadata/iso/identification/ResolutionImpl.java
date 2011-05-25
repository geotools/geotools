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
package org.geotools.metadata.iso.identification;

import org.opengis.metadata.identification.RepresentativeFraction;
import org.opengis.metadata.identification.Resolution;
import org.geotools.metadata.iso.MetadataEntity;


/**
 * Level of detail expressed as a scale factor or a ground distance.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class ResolutionImpl extends MetadataEntity implements Resolution {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = -4644465057871958482L;

    /**
     * Level of detail expressed as the scale of a comparable hardcopy map or chart.
     * This value should be between 0 and 1.
     * Only one of {@linkplain #getEquivalentScale equivalent scale} and
     * {@linkplain #getDistance ground sample distance} may be provided.
     */
    private RepresentativeFraction equivalentScale;

    /**
     * Ground sample distance.
     * Only one of {@linkplain #getEquivalentScale equivalent scale} and
     * {@linkplain #getDistance ground sample distance} may be provided.
     */
    private Double distance;

    /**
     * Constructs an initially empty Resolution.
     */
    public ResolutionImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public ResolutionImpl(final Resolution source) {
        super(source);
    }

    /**
     * Level of detail expressed as the scale of a comparable hardcopy map or chart.
     * Only one of {@linkplain #getEquivalentScale equivalent scale} and
     * {@linkplain #getDistance ground sample distance} may be provided.
     */
    public RepresentativeFraction getEquivalentScale()  {
        return equivalentScale;
    }

    /**
     * Set the level of detail expressed as the scale of a comparable hardcopy map or chart.
     *
     * @since 2.4
     */
    public synchronized void setEquivalentScale(final RepresentativeFraction newValue) {
        checkWritePermission();
        equivalentScale = newValue;
    }

    /**
     * Ground sample distance.
     * Only one of {@linkplain #getEquivalentScale equivalent scale} and
     * {@linkplain #getDistance ground sample distance} may be provided.
     */
    public Double getDistance() {
        return distance;
    }

    /**
     * Set the ground sample distance.
     */
    public synchronized void setDistance(final Double newValue) {
        checkWritePermission();
        distance = newValue;
    }
}
