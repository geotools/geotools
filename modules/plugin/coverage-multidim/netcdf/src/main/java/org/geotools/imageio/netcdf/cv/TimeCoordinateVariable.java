/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.netcdf.cv;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Logger;
import org.geotools.imageio.netcdf.utilities.NetCDFCRSUtilities;
import org.geotools.imageio.netcdf.utilities.NetCDFTimeUtilities;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import ucar.nc2.Attribute;
import ucar.nc2.constants.AxisType;
import ucar.nc2.dataset.CoordinateAxis;

/**
 * @author Simone Giannecchini, GeoSolutions SAS
 * @author Niels Charlier TODO caching
 */
class TimeCoordinateVariable extends CoordinateVariable<Date> {
    /** The LOGGER for this class. */
    private static final Logger LOGGER = Logger.getLogger(TimeCoordinateVariable.class.toString());

    private String units;

    private int baseTimeUnits;

    private Date epoch;

    /** */
    public TimeCoordinateVariable(CoordinateAxis coordinateAxis) {
        super(Date.class, coordinateAxis);
        units = coordinateAxis.getUnitsString();
        /*
         * Gets the axis origin. In the particular case of time axis, units are typically written in the form "days since 1990-01-01
         * 00:00:00". We extract the part before "since" as the units and the part after "since" as the date.
         */
        String origin = null;
        final String[] unitsParts = units.split("(?i)\\s+since\\s+");
        if (unitsParts.length == 2) {
            units = unitsParts[0].trim();
            origin = unitsParts[1].trim();
        } else {
            final Attribute attribute = coordinateAxis.findAttribute("time_origin");
            if (attribute != null) {
                origin = attribute.getStringValue();
            }
        }

        baseTimeUnits = NetCDFTimeUtilities.getTimeUnits(units, null);
        if (baseTimeUnits == -1) {
            throw new IllegalArgumentException(
                    "Couldn't determine time units from unit string '" + units + "'");
        }
        if (origin != null) {
            origin = NetCDFTimeUtilities.trimFractionalPart(origin);
            // add 0 digits if absent
            origin = NetCDFTimeUtilities.checkDateDigits(origin);

            try {
                epoch =
                        (Date)
                                NetCDFUtilities.getAxisFormat(AxisType.Time, origin)
                                        .parseObject(origin);
            } catch (ParseException e) {
                LOGGER.warning(
                        "Error while parsing time Axis. Skip setting the TemporalExtent from coordinateAxis");
            }
        }
        init();
    }

    @Override
    public boolean isNumeric() {
        return false;
    }

    @Override
    protected synchronized CoordinateReferenceSystem buildCoordinateReferenceSystem() {
        return NetCDFCRSUtilities.buildTemporalCrs(coordinateAxis);
    }

    @Override
    protected Date convertValue(Object o) {
        final Calendar cal = new GregorianCalendar();
        if (epoch != null) {
            cal.setTime(epoch);
        } else {
            cal.setTimeInMillis(0);
        }
        cal.setTimeZone(NetCDFTimeUtilities.UTC_TIMEZONE);
        final double coordValue = ((Number) o).doubleValue();
        long vi = (long) Math.floor(coordValue);
        double vd = coordValue - vi;
        NetCDFTimeUtilities.addTimeUnit(cal, baseTimeUnits, vi);
        if (vd != 0.0) {
            NetCDFTimeUtilities.addTimeUnit(
                    cal,
                    NetCDFTimeUtilities.getTimeUnits(units, vd),
                    NetCDFTimeUtilities.getTimeSubUnitsValue(units, vd));
        }
        return cal.getTime();
    }
}
