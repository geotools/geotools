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
package org.geotools.metadata.iso.quality;

import org.opengis.metadata.quality.AccuracyOfATimeMeasurement;


/**
 * Correctness of the temporal references of an item (reporting of error in time measurement).
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class AccuracyOfATimeMeasurementImpl extends TemporalAccuracyImpl
        implements AccuracyOfATimeMeasurement
{
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -7934234071852119486L;

    /**
     * Constructs an initially empty accuracy of a time measurement.
     */
    public AccuracyOfATimeMeasurementImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public AccuracyOfATimeMeasurementImpl(final AccuracyOfATimeMeasurement source) {
        super(source);
    }
}
