/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.netcdf;


import it.geosolutions.imageio.plugins.netcdf.NetCDFUtilities;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;

import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPosition;
import org.opengis.temporal.TemporalObject;

import ucar.ma2.InvalidRangeException;
import ucar.ma2.Range;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.Variable;
import ucar.nc2.constants.AxisType;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.CoordinateAxis1D;
import ucar.nc2.dataset.CoordinateSystem;

class NetCDFSliceUtilities {

    private final static Logger LOGGER = Logger
            .getLogger("org.geotools.imageio.netcdf.util");

    /**
     * Gets the name, as the "description", "title" or "standard name" attribute
     * if possible, or as the variable name otherwise.
     */
    public static String getName(final Variable variable) {
        String name = variable.getDescription();
        if (name == null || (name = name.trim()).length() == 0) {
            name = variable.getName();
        }
        return name;
    }

    public static double[] getEnvelope(CoordinateSystem cs) {
        // TODO: Handle 3D GEO CoordinateReferenceSystem
        double[] envelope = null;
        if (cs != null) {
            /*
             * Adds the axis in reverse order, because the NetCDF image reader
             * put the last dimensions in the rendered image. Typical NetCDF
             * convention is to put axis in the (time, depth, latitude,
             * longitude) order, which typically maps to (longitude, latitude,
             * depth, time) order in our referencing framework.
             */
            final List<CoordinateAxis> axes = cs.getCoordinateAxes();
            envelope = new double[] { Double.NaN, Double.NaN, Double.NaN,
                    Double.NaN };
            for (int i = axes.size(); --i >= 0;) {
                final CoordinateAxis axis = axes.get(i);

                // final String name = NetCDFSliceUtilities.getName(axis);
                final AxisType type = axis.getAxisType();
                // final String units = axis.getUnitsString();

                /*
                 * Gets the axis direction, taking in account the possible
                 * reversal or vertical axis. Note that geographic and projected
                 * CoordinateReferenceSystem have the same directions. We can
                 * distinguish them either using the ISO
                 * CoordinateReferenceSystem type ("geographic" or "projected"),
                 * the ISO CS type ("ellipsoidal" or "cartesian") or the units
                 * ("degrees" or "m").
                 */

                /*
                 * If the axis is not numeric, we can't process any further. If
                 * it is, then adds the coordinate and index ranges.
                 */
                if (axis.isNumeric() && axis instanceof CoordinateAxis1D
                        && !AxisType.Time.equals(type)) {
                    final CoordinateAxis1D axis1D = (CoordinateAxis1D) axis;
                    final int length = axis1D.getDimension(0).getLength();
                    if (length > 2 && axis1D.isRegular()) {
                        final double increment = axis1D.getIncrement();
                        final double start = axis1D.getStart();
                        final double end = start + increment * (length - 1); // Inclusive

                        if (AxisType.Lon.equals(type)
                                || AxisType.GeoX.equals(type)) {
                            if (increment > 0) {
                                envelope[0] = start;
                                envelope[2] = end;
                            } else {
                                envelope[0] = end;
                                envelope[2] = start;
                            }
                        }

                        if (AxisType.Lat.equals(type)
                                || AxisType.GeoY.equals(type)) {
                            if (increment > 0) {
                                envelope[1] = start;
                                envelope[3] = end;
                            } else {
                                envelope[1] = end;
                                envelope[3] = start;
                            }
                        }
                    } else {

                        final double[] values = axis1D.getCoordValues();
                        final double val0 = values[0];
                        final double valN = values[values.length - 1];

                        if (AxisType.Lon.equals(type)
                                || AxisType.GeoX.equals(type)) {
                            // if (CoordinateAxis.POSITIVE_DOWN
                            // .equalsIgnoreCase(axis.getPositive())) {
                            // envelope[1] = values[0];
                            // envelope[3] = values[values.length - 1];
                            // } else {
                            envelope[0] = val0;
                            envelope[2] = valN;
                            // }
                        }

                        if (AxisType.Lat.equals(type)
                                || AxisType.GeoY.equals(type)) {
                            // if (CoordinateAxis.POSITIVE_DOWN
                            // .equalsIgnoreCase(axis.getPositive())) {
                            // envelope[0] = values[0];
                            // envelope[2] = values[values.length - 1];
                            // } else {
                            envelope[1] = val0;
                            envelope[3] = valN;
                            // }
                        }
                    }
                }
            }
            for (int i = 0; i < envelope.length; i++)
                if (Double.isNaN(envelope[i])) {
                    envelope = null;
                    break;
                }

        }
        return envelope;
    }

    public static double getVerticalValue(final NetCDFSpatioTemporalImageReader geoNetCDFReader, Variable variable, Range range,
            final int imageIndex, final CoordinateSystem cs) {
        double ve = Double.NaN;
        if (cs != null && cs.hasVerticalAxis()) {
            final int zIndex = NetCDFUtilities.getZIndex(variable, range,
                    imageIndex);
            final int rank = variable.getRank();

            final Dimension verticalDimension = variable.getDimension(rank
                    - NetCDFUtilities.Z_DIMENSION);
            final Variable axis = geoNetCDFReader.getCoordinate(verticalDimension.getName());
            if ((axis != null) && axis.isCoordinateVariable()){
                final AxisType type = ((CoordinateAxis)axis).getAxisType();
                if (!AxisType.GeoZ.equals(type)
                        && !AxisType.Pressure.equals(type)
                        && !AxisType.Height.equals(type))
                    return ve;

                // String units = axis.getUnitsString();
                if (((CoordinateAxis)axis).isNumeric() && axis instanceof CoordinateAxis1D) {
                    final CoordinateAxis1D axis1D = (CoordinateAxis1D) axis;
                    final double[] values = axis1D.getCoordValues();
                    if (values != null && values.length > zIndex) {
                        ve = values[zIndex];
                    }
                }
            }
        }
        return ve;
    }

    /**
     * 
     * @param geoNetCDFReader 
     * @param imageIndex
     * @throws InvalidRangeException
     * @throws IOException
     * @throws ParseException
     */
    public static TemporalObject getTimeValue(final NetCDFSpatioTemporalImageReader geoNetCDFReader, Variable variable, Range range,
            final int imageIndex, final CoordinateSystem cs) {
        TemporalObject varTime = null;

        if (cs != null && cs.hasTimeAxis()) {
            int timeIndex = NetCDFUtilities.getTIndex(variable, range, imageIndex);
            final int rank = variable.getRank();
            final Dimension temporalDimension = variable.getDimension(rank
                            - ((cs.hasVerticalAxis() ? NetCDFUtilities.Z_DIMENSION : 2) + 1));
            final Variable axis = geoNetCDFReader.getCoordinate(temporalDimension.getName());
            if ((axis != null) && axis.isCoordinateVariable()){
            
//            for (Variable axis : coordVars) {
                final AxisType type = ((CoordinateAxis)axis).getAxisType();
                if (!AxisType.Time.equals(type))
                    return varTime;

                String units = axis.getUnitsString();
                Date epoch = null, start_time = null, end_time = null;

                /*
                 * Gets the axis origin. In the particular case of time axis,
                 * units are typically written in the form "days since
                 * 1990-01-01 00:00:00". We extract the part before "since" as
                 * the units and the part after "since" as the date.
                 */
                String origin = null;
                final String[] unitsParts = units.split("(?i)\\s+since\\s+");
                if (unitsParts.length == 2) {
                    units = unitsParts[0].trim();
                    origin = unitsParts[1].trim();
                } else {
                    final Attribute attribute = axis
                            .findAttribute("time_origin");
                    if (attribute != null) {
                        origin = attribute.getStringValue();
                    }
                }
                if (origin != null) {
                    origin = NetCDFUtilities.trimFractionalPart(origin);
                    // add 0 digits if absent
                    origin = checkDateDigits(origin);
                    
                    try {
                        epoch = (Date) NetCDFUtilities.getAxisFormat(type, origin).parseObject(
                                origin);
                    } catch (ParseException e) {
                        LOGGER
                                .warning("Error while parsing time Axis. Skip setting the TemporalExtent from coordinateAxis");
                    }
                }

                if (((CoordinateAxis)axis).isNumeric() && epoch != null
                        && axis instanceof CoordinateAxis1D) {
                    Calendar cal = null;
                    final CoordinateAxis1D axis1D = (CoordinateAxis1D) axis;
                    final double[] values = axis1D.getCoordValues();
                    cal = new GregorianCalendar();
                    cal.setTime(epoch);

                    int vi = (int) Math.floor(values[timeIndex]);
                    double vd = values[timeIndex] - vi;
                    cal.add(getTimeUnits(units, null), vi);
                    if (vd != 0.0)
                        cal.add(getTimeUnits(units, vd), getTimeSubUnitsValue(units, vd));
                    start_time = cal.getTime();

                    // if (values.length > 1 && values[0] != values[1]) {
                    // cal.setTime(epoch);
                    // cal.add(getTimeUnits(units), (int) values[1]
                    // + timeIndex);
                    // end_time = cal.getTime();
                    // }
                    // if (start_time != null && end_time != null
                    // && start_time.before(end_time)) {
                    // varTime = new DefaultPeriod(new DefaultInstant(
                    // new DefaultPosition(start_time)),
                    // new DefaultInstant(
                    // new DefaultPosition(end_time)));
                    // } else
                    if (start_time != null) {
                        varTime = new DefaultInstant(new DefaultPosition(start_time));
                    }

                }

            }
        }

        return varTime;
    }

    /**
     * Converts NetCDF time units into opportune Calendar ones.
     * 
     * @param units
     *                {@link String}
     * @param d
     * @return int
     */
    private static int getTimeUnits(String units, Double vd) {
         if ("months".equalsIgnoreCase(units)) {
             if (vd == null || vd == 0.0)
                 // if no day, it is the first day
                 return 1;
             else {
                 //TODO: FIXME
             }
         } else if ("days".equalsIgnoreCase(units)) {
            if (vd == null || vd == 0.0)
                return Calendar.DATE;
            else {
                double hours = vd * 24;
                if (hours - Math.floor(hours) == 0.0)
                    return Calendar.HOUR;

                double minutes = vd * 24 * 60;
                if (minutes - Math.floor(minutes) == 0.0)
                    return Calendar.MINUTE;

                double seconds = vd * 24 * 60 * 60;
                if (seconds - Math.floor(seconds) == 0.0)
                    return Calendar.SECOND;

                return Calendar.MILLISECOND;
            }
        }
        if ("hours".equalsIgnoreCase(units) || "hour".equalsIgnoreCase(units)) {
            if (vd == null || vd == 0.0)
                return Calendar.HOUR;
            else {
                double minutes = vd * 24 * 60;
                if (minutes - Math.floor(minutes) == 0.0)
                    return Calendar.MINUTE;

                double seconds = vd * 24 * 60 * 60;
                if (seconds - Math.floor(seconds) == 0.0)
                    return Calendar.SECOND;

                return Calendar.MILLISECOND;
            }
        }
        if ("minutes".equalsIgnoreCase(units)) {
            if (vd == null || vd == 0.0)
                return Calendar.MINUTE;
            else {
                double seconds = vd * 24 * 60 * 60;
                if (seconds - Math.floor(seconds) == 0.0)
                    return Calendar.SECOND;

                return Calendar.MILLISECOND;
            }
        }
        if ("seconds".equalsIgnoreCase(units)) {
            if (vd == null || vd == 0.0)
                return Calendar.SECOND;
            else {
                return Calendar.MILLISECOND;
            }
        }

        return -1;
    }

    /**
     * 
     */
    private static int getTimeSubUnitsValue(String units, Double vd) {
        if ("days".equalsIgnoreCase(units)) {
            int subUnit = getTimeUnits(units, vd);
            if (subUnit == Calendar.HOUR) {
                double hours = vd * 24;
                return (int) hours;
            }

            if (subUnit == Calendar.MINUTE) {
                double hours = vd * 24 * 60;
                return (int) hours;
            }

            if (subUnit == Calendar.SECOND) {
                double hours = vd * 24 * 60 * 60;
                return (int) hours;
            }

            if (subUnit == Calendar.MILLISECOND) {
                double hours = vd * 24 * 60 * 60 * 1000;
                return (int) hours;
            }

            return 0;
        }

        if ("hours".equalsIgnoreCase(units) || "hour".equalsIgnoreCase(units)) {
            int subUnit = getTimeUnits(units, vd);
            if (subUnit == Calendar.MINUTE) {
                double hours = vd * 24 * 60;
                return (int) hours;
            }

            if (subUnit == Calendar.SECOND) {
                double hours = vd * 24 * 60 * 60;
                return (int) hours;
            }

            if (subUnit == Calendar.MILLISECOND) {
                double hours = vd * 24 * 60 * 60 * 1000;
                return (int) hours;
            }

            return 0;
        }

        if ("minutes".equalsIgnoreCase(units)) {
            int subUnit = getTimeUnits(units, vd);
            if (subUnit == Calendar.SECOND) {
                double hours = vd * 24 * 60 * 60;
                return (int) hours;
            }

            if (subUnit == Calendar.MILLISECOND) {
                double hours = vd * 24 * 60 * 60 * 1000;
                return (int) hours;
            }

            return 0;
        }

        if ("seconds".equalsIgnoreCase(units)) {
            int subUnit = getTimeUnits(units, vd);
            if (subUnit == Calendar.MILLISECOND) {
                double hours = vd * 24 * 60 * 60 * 1000;
                return (int) hours;
            }

            return 0;
        }

        return 0;
    }

    public static int JGREG = 15 + 31 * (10 + 12 * 1582);

    // public static double HALFSECOND = 0.5;

    public static GregorianCalendar fromJulian(double injulian) {
        int jalpha, ja, jb, jc, jd, je, year, month, day;
        // double julian = injulian + HALFSECOND / 86400.0;
        ja = (int) injulian;
        if (ja >= JGREG) {
            jalpha = (int) (((ja - 1867216) - 0.25) / 36524.25);
            ja = ja + 1 + jalpha - jalpha / 4;
        }

        jb = ja + 1524;
        jc = (int) (6680.0 + ((jb - 2439870) - 122.1) / 365.25);
        jd = 365 * jc + jc / 4;
        je = (int) ((jb - jd) / 30.6001);
        day = jb - jd - (int) (30.6001 * je);
        month = je - 1;
        if (month > 12)
            month = month - 12;
        year = jc - 4715;
        if (month > 2)
            year--;
        if (year <= 0)
            year--;

        // Calendar Months are 0 based
        return new GregorianCalendar(year, month - 1, day);
    }

    /**
	 * @param origin
	 */
	public static String checkDateDigits(String origin) {
		String digitsCheckedOrigin = "";
		if (origin.indexOf("-") > 0) {
			String tmp = (origin.indexOf(" ") > 0 ? origin.substring(0, origin.indexOf(" ")) : origin);
			String[] originDateParts = tmp.split("-");
			for (int l=0; l<originDateParts.length; l++) {
				String datePart = originDateParts[l];
				while (datePart.length() % 2 != 0) {
					datePart = "0" + datePart;
				}
				
				digitsCheckedOrigin += datePart;
				digitsCheckedOrigin += (l<(originDateParts.length-1) ? "-" : "");
			}
		}

		if (origin.indexOf(":") > 0) {
			digitsCheckedOrigin += " ";
			String tmp = (origin.indexOf(" ") > 0 ? origin.substring(origin.indexOf(" ")+1) : origin);
			String[] originDateParts = tmp.split(":");
			for (int l=0; l<originDateParts.length; l++) {
				String datePart = originDateParts[l];
				while (datePart.length() % 2 != 0) {
					datePart = "0" + datePart;
				}
				
				digitsCheckedOrigin += datePart;
				digitsCheckedOrigin += (l<(originDateParts.length-1) ? ":" : "");
			}
		}
		
		if (digitsCheckedOrigin.length() > 0)
			return digitsCheckedOrigin;
		
		return origin;
	}
}
