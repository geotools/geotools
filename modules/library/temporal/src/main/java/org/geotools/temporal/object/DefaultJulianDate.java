/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.temporal.object;

import org.opengis.temporal.IndeterminateValue;
import org.opengis.temporal.JulianDate;
import org.opengis.temporal.TemporalReferenceSystem;

/**
 * The Julian day numbering system is a temporal coordinate system that has its origin at noon
 * on 1 January 4713 BCE in the Julian proleptic calendar. The Julian day number is an integer
 * value; the Julian date is a decimal value that allows greater resolution.
 *
 * @author Mehdi Sidhoum (Geomatys)
 *
 * @source $URL$
 */
public class DefaultJulianDate extends DefaultTemporalCoordinate implements JulianDate {

    /**
     * Creates a new instance of JulianDate.
     * @param frame
     * @param indeterminatePosition
     * @param coordinateValue
     */
    public DefaultJulianDate(TemporalReferenceSystem frame, IndeterminateValue indeterminatePosition, Number coordinateValue) {
        super(frame, indeterminatePosition, coordinateValue);
    }
}
