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
package org.geotools.imageio.unidata.cv;

import java.io.IOException;
import java.text.ParseException;
import java.util.AbstractList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;

import org.geotools.imageio.unidata.utilities.UnidataCRSUtilities;
import org.geotools.imageio.unidata.utilities.UnidataTimeUtilities;
import org.geotools.imageio.unidata.utilities.UnidataUtilities;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.TemporalCRS;

import ucar.nc2.Attribute;
import ucar.nc2.constants.AxisType;
import ucar.nc2.dataset.CoordinateAxis1D;

/**
 * @author User
 *TODO caching
 */
class TimeCoordinateVariable extends CoordinateVariable<Date> {
    /** The LOGGER for this class. */
    private static final Logger LOGGER = Logger.getLogger(TimeCoordinateVariable.class.toString());

    private static class TimeBuilder {
    
        String units;
    
        String origin;
    
        Date epoch;
    
        CoordinateAxis1D axis1D;
        
        int baseTimeUnits;
        
        public TimeBuilder(CoordinateAxis1D axis) {
            axis1D = axis;
            units = axis.getUnitsString();       
            /*
             * Gets the axis origin. In the particular case of time axis, units are typically written in the form "days since 1990-01-01
             * 00:00:00". We extract the part before "since" as the units and the part after "since" as the date.
             */
            origin = null;
            final String[] unitsParts = units.split("(?i)\\s+since\\s+");
            if (unitsParts.length == 2) {
                units = unitsParts[0].trim();
                origin = unitsParts[1].trim();
            } else {
                final Attribute attribute = axis.findAttribute("time_origin");
                if (attribute != null) {
                    origin = attribute.getStringValue();
                }
            }

            baseTimeUnits  = UnidataTimeUtilities.getTimeUnits(units, null);     
            if (baseTimeUnits == -1) {
            	throw new IllegalArgumentException("Couldn't determine time units from unit string '" + units + "'");
            }
            if (origin != null) {
                origin = UnidataTimeUtilities.trimFractionalPart(origin);
                // add 0 digits if absent
                origin = UnidataTimeUtilities.checkDateDigits(origin);
    
                try {
                    epoch = (Date) UnidataUtilities.getAxisFormat(AxisType.Time, origin)
                            .parseObject(origin);
                } catch (ParseException e) {
                    LOGGER.warning("Error while parsing time Axis. Skip setting the TemporalExtent from coordinateAxis");
                }
            }
        }
    
        public Date buildTime(int timeIndex) {
            if (epoch != null) {
                final Calendar cal = new GregorianCalendar();
                cal.setTime(epoch);
                cal.setTimeZone(UnidataTimeUtilities.UTC_TIMEZONE);
                final double coordValue = axis1D.getCoordValue(timeIndex);
                long vi = (long) Math.floor(coordValue);
                double vd = coordValue - vi;
                UnidataTimeUtilities.addTimeUnit(cal, baseTimeUnits, vi);
                if (vd != 0.0) {
                	UnidataTimeUtilities.addTimeUnit(cal, UnidataTimeUtilities.getTimeUnits(units, vd),
                			UnidataTimeUtilities.getTimeSubUnitsValue(units, vd));
                }
                return cal.getTime();
            }
            return null;
    
        }
    
        public int getNumTimes() {
            return axis1D.getShape(0);
        }
    }

    private final TimeBuilder timeBuilder;
    private TemporalCRS temporalCRS;

    /**
     * @param binding
     * @param coordinateAxis
     */
    public TimeCoordinateVariable(CoordinateAxis1D coordinateAxis) {
        super(Date.class, coordinateAxis);
        this.timeBuilder= new TimeBuilder(coordinateAxis);
    }

    @Override
    public Date getMinimum() throws IOException {
        return timeBuilder.buildTime(0);
    }

    @Override
    public Date getMaximum() throws IOException {
        return timeBuilder.buildTime(timeBuilder.getNumTimes()-1);
    }

    @Override
    public Date read(int index) throws IndexOutOfBoundsException {
        return timeBuilder.buildTime(index);
    }

    @Override
    public List<Date> read() throws IndexOutOfBoundsException {
        return new AbstractList<Date>() {

            @Override
            public Date get(int index) {
                return timeBuilder.buildTime(index);
            }

            @Override
            public int size() {
                return timeBuilder.getNumTimes();
            }
        };        
    }

    @Override
    public boolean isNumeric() {
        return false;
    }

    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        if(temporalCRS==null){
         synchronized (this) {
             this.temporalCRS= UnidataCRSUtilities.buildTemporalCrs(coordinateAxis);
         }   
        }
        return temporalCRS;
    }

}
