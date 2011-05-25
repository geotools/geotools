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
package org.geotools.image.io.netcdf;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.imageio.ImageReader;

import org.opengis.referencing.cs.AxisDirection;

import ucar.nc2.Variable;
import ucar.nc2.Attribute;
import ucar.nc2.dataset.AxisType;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.CoordinateAxis1D;
import ucar.nc2.dataset.CoordinateSystem;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.dataset.VariableDS;

import org.geotools.image.io.metadata.Axis;
import org.geotools.image.io.metadata.ImageGeometry;
import org.geotools.image.io.metadata.ImageReferencing;
import org.geotools.image.io.metadata.MetadataAccessor;
import org.geotools.image.io.metadata.GeographicMetadata;
import org.geotools.image.io.metadata.GeographicMetadataFormat;
import org.geotools.util.logging.LoggedFormat;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * Metadata from NetCDF file. This implementation assumes that the NetCDF file follows the
 * <A HREF="http://www.cfconventions.org/">CF Metadata conventions</A>.
 * <p>
 * <b>Limitation:</b>
 * Current implementation retains only the first {@linkplain CoordinateSystem coordinate system}
 * found in the NetCDF file or for a given variable. The {@link org.geotools.coverage.io} package
 * would not know what to do with the extra coordinate systems anyway.
 *
 * @since 2.4
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class NetcdfMetadata extends GeographicMetadata {
    /**
     * Forces usage of UCAR libraries in some places where we use our own code instead.
     * This may result in rounding errors and absence of information regarding fill values,
     * but is useful for checking if we are doing the right thing compared to the UCAR way.
     */
    private static final boolean USE_UCAR_LIB = false;

    /**
     * The mapping between UCAR axis type and ISO axis directions.
     */
    private static final Map<AxisType,AxisDirection> DIRECTIONS = new HashMap<AxisType,AxisDirection>(16);
    static {
        add(AxisType.Time,     AxisDirection.FUTURE);
        add(AxisType.GeoX,     AxisDirection.EAST);
        add(AxisType.GeoY,     AxisDirection.NORTH);
        add(AxisType.GeoZ,     AxisDirection.UP);
        add(AxisType.Lat,      AxisDirection.NORTH);
        add(AxisType.Lon,      AxisDirection.EAST);
        add(AxisType.Height,   AxisDirection.UP);
        add(AxisType.Pressure, AxisDirection.UP);
    }

    /**
     * Adds a mapping between UCAR type and ISO direction.
     */
    private static void add(final AxisType type, final AxisDirection direction) {
        if (DIRECTIONS.put(type, direction) != null) {
            throw new IllegalArgumentException(String.valueOf(type));
        }
    }

    /**
     * Creates metadata from the specified file. This constructor is typically invoked
     * for creating {@linkplain NetcdfReader#getStreamMetadata stream metadata}. Note that
     * {@link ucar.nc2.dataset.CoordSysBuilder#addCoordinateSystems} should have been invoked
     * (if needed) before this constructor.
     */
    public NetcdfMetadata(final ImageReader reader, final NetcdfDataset file) {
        super(reader);
        final List<CoordinateSystem> systems = file.getCoordinateSystems();
        if (!systems.isEmpty()) {
            addCoordinateSystem(systems.get(0));
        }
    }

    /**
     * Creates metadata from the specified file. This constructor is typically invoked
     * for creating {@linkplain NetcdfReader#getImageMetadata image metadata}. Note that
     * {@link ucar.nc2.dataset.CoordSysBuilder#addCoordinateSystems} should have been invoked
     * (if needed) before this constructor.
     */
    public NetcdfMetadata(final ImageReader reader, final VariableDS variable) {
        super(reader);
        final List<CoordinateSystem> systems = variable.getCoordinateSystems();
        if (!systems.isEmpty()) {
            addCoordinateSystem(systems.get(0));
        }
        setSampleType(GeographicMetadataFormat.PACKED);
        addSampleDimension(variable);
    }

    /**
     * Adds the specified coordinate system. Current implementation can adds at most one
     * coordinate system, but this limitation may be revisited in a future Geotools version.
     *
     * @param cs The coordinate system to add.
     */
    public void addCoordinateSystem(final CoordinateSystem cs) {
        String crsType, csType;
        if (cs.isLatLon()) {
            crsType = cs.hasVerticalAxis() ? GeographicMetadataFormat.GEOGRAPHIC_3D
                                           : GeographicMetadataFormat.GEOGRAPHIC;
            csType  = GeographicMetadataFormat.ELLIPSOIDAL;
        } else if (cs.isGeoXY()) {
            crsType = cs.hasVerticalAxis() ? GeographicMetadataFormat.PROJECTED_3D
                                           : GeographicMetadataFormat.PROJECTED;
            csType  = GeographicMetadataFormat.CARTESIAN;
        } else {
            crsType = null;
            csType  = null;
        }
        final ImageReferencing referencing = getReferencing();
        referencing.setCoordinateReferenceSystem(null, crsType);
        referencing.setCoordinateSystem(cs.getName(), csType);
        final ImageGeometry geometry = getGeometry();
        geometry.setPixelOrientation("center");
        /*
         * Adds the axis in reverse order, because the NetCDF image reader put the last
         * dimensions in the rendered image. Typical NetCDF convention is to put axis in
         * the (time, depth, latitude, longitude) order, which typically maps to
         * (longitude, latitude, depth, time) order in Geotools referencing framework.
         */
        final List<CoordinateAxis> axis = cs.getCoordinateAxes();
        for (int i=axis.size(); --i>=0;) {
            addCoordinateAxis(axis.get(i));
        }
    }

    /**
     * Gets the name, as the "description", "title" or "standard name"
     * attribute if possible, or as the variable name otherwise.
     */
    private static String getName(final Variable variable) {
        String name = variable.getDescription();
        if (name == null || (name=name.trim()).length() == 0) {
            name = variable.getName();
        }
        return name;
    }

    /**
     * Adds the specified coordinate axis. This method is invoked recursively
     * by {@link #addCoordinateSystem}.
     *
     * @param axis The axis to add.
     */
    public void addCoordinateAxis(final CoordinateAxis axis) {
        final String name = getName(axis);
        final AxisType type = axis.getAxisType();
        String units = axis.getUnitsString();
        /*
         * Gets the axis direction, taking in account the possible reversal or vertical axis.
         * Note that geographic and projected CRS have the same directions. We can distinguish
         * them either using the ISO CRS type ("geographic" or "projected"), the ISO CS type
         * ("ellipsoidal" or "cartesian") or the units ("degrees" or "m").
         */
        String direction = null;
        AxisDirection directionCode = DIRECTIONS.get(type);
        if (directionCode != null) {
            if (CoordinateAxis.POSITIVE_DOWN.equalsIgnoreCase(axis.getPositive())) {
                directionCode = directionCode.opposite();
            }
            direction = directionCode.name();
            final int offset = units.lastIndexOf('_');
            if (offset >= 0) {
                final String unitsDirection = units.substring(offset + 1).trim();
                final String opposite = directionCode.opposite().name();
                if (unitsDirection.equalsIgnoreCase(opposite)) {
                    warning("addCoordinateAxis", ErrorKeys.INCONSISTENT_AXIS_ORIENTATION_$2,
                            new String[] {name, direction});
                    direction = opposite;
                }
                if (unitsDirection.equalsIgnoreCase(direction)) {
                    units = units.substring(0, offset).trim();
                }
            }
        }
        /*
         * Gets the axis origin. In the particular case of time axis, units are typically
         * written in the form "days since 1990-01-01 00:00:00". We extract the part before
         * "since" as the units and the part after "since" as the date.
         */
        final Axis axisNode = getReferencing().addAxis(name, direction, units);
        if (AxisType.Time.equals(type)) {
            String origin = null;
            final String[] unitsParts = units.split("(?i)\\s+since\\s+");
            if (unitsParts.length == 2) {
                units  = unitsParts[0].trim();
                origin = unitsParts[1].trim();
            } else {
                final Attribute attribute = axis.findAttribute("time_origin");
                if (attribute != null) {
                    origin = attribute.getStringValue();
                }
            }
            Date epoch = null;
            if (origin != null) {
                origin = MetadataAccessor.trimFractionalPart(origin);
                epoch = parse(type, origin, Date.class, "addCoordinateAxis");
            }
            axisNode.setTimeOrigin(epoch);
            axisNode.setUnits(units);
        }
        /*
         * If the axis is not numeric, we can't process any further.
         * If it is, then adds the coordinate and index ranges.
         */
        if (!axis.isNumeric()) {
            return;
        }
        if (axis instanceof CoordinateAxis1D) {
            final CoordinateAxis1D axis1D = (CoordinateAxis1D) axis;
            final ImageGeometry geometry = getGeometry();
            final double[] values = axis1D.getCoordValues();
            geometry.addOrdinates(0, values);
        }
    }

    /**
     * Adds sample dimension information for the specified variable.
     *
     * @param variable The variable to add as a sample dimension.
     */
    public void addSampleDimension(final VariableDS variable) {
        final VariableMetadata m;
        if (USE_UCAR_LIB) {
            m = new VariableMetadata(variable);
        } else {
            m = new VariableMetadata(variable, forcePacking("valid_range"));
        }
        m.copyTo(addBand(getName(variable)));
    }

    /**
     * Parses the given string as a value along the specified axis.
     *
     * @param  type     The type of the axis.
     * @param  value    The value along that axis.
     * @param  expected The expected type.
     * @return The value after parsing.
     */
    private <T> T parse(final AxisType type, String value, final Class<T> expected, final String caller) {
        final LoggedFormat<T> format = createLoggedFormat(getAxisFormat(type, value), expected);
        format.setLogger("org.geotools.image.io.netcdf");
        format.setCaller(NetcdfMetadata.class, caller);
        return format.parse(value);
    }

    /**
     * Returns a format to use for parsing values along the specified axis type. This method
     * is invoked when parsing the date part of axis units like "<cite>days since 1990-01-01
     * 00:00:00</cite>". Subclasses should override this method if the date part is formatted
     * in a different way. The default implementation returns the following formats:
     * <p>
     * <ul>
     *   <li>For {@linkplain AxisType#Time time axis}, a {@link DateFormat} using the
     *       {@code "yyyy-MM-dd HH:mm:ss"} pattern in UTC {@linkplain TimeZone timezone}.</li>
     *   <li>For all other kind of axis, a {@link NumberFormat}.</li>
     * </ul>
     * <p>
     * The {@linkplain Locale#CANADA Canada locale} is used by default for most formats because
     * it is relatively close to ISO (for example regarding days and months order in dates) while
     * using the English symbols.
     *
     * @param  type The type of the axis.
     * @param  prototype An example of the values to be parsed. Implementations may parse this
     *         prototype when the axis type alone is not suffisient. For example the {@linkplain
     *         AxisType#Time time axis type} should uses the {@code "yyyy-MM-dd"} date pattern,
     *         but some files do not follow this convention and use the default local instead.
     * @return The format for parsing values along the axis.
     */
    protected Format getAxisFormat(final AxisType type, final String prototype) {
        if (!type.equals(AxisType.Time)) {
            return NumberFormat.getNumberInstance(Locale.CANADA);
        }
        char dateSeparator = '-';   // The separator used in ISO format.
        boolean yearLast   = false; // Year is first in ISO pattern.
        boolean namedMonth = false; // Months are numbers in the ISO pattern.
        if (prototype != null) {
            /*
             * Performs a quick check on the prototype content. If the prototype seems to use a
             * different date separator than the ISO one, we will adjust the pattern accordingly.
             * Also checks if the year seems to appears last rather than first, and if the month
             * seems to be written using letters rather than digits.
             */
            int field = 1;
            int digitCount = 0;
            final int length = prototype.length();
            for (int i=0; i<length; i++) {
                final char c = prototype.charAt(i);
                if (Character.isWhitespace(c)) {
                    break; // Checks only the dates, ignore the hours.
                }
                if (Character.isDigit(c)) {
                    digitCount++;
                    continue; // Digits are legal in all cases.
                }
                if (field == 2 && Character.isLetter(c)) {
                    namedMonth = true;
                    continue; // Letters are legal for month only.
                }
                if (field == 1) {
                    dateSeparator = c;
                }
                digitCount = 0;
                field++;
            }
            if (digitCount >= 4) {
                yearLast = true;
            }
        }
        String pattern;
        if (yearLast) {
            pattern = namedMonth ? "dd-MMM-yyyy" : "dd-MM-yyyy";
        } else {
            pattern = namedMonth ? "yyyy-MMM-dd" : "yyyy-MM-dd";
        }
        pattern = pattern.replace('-', dateSeparator);
        pattern += " HH:mm:ss";
        final DateFormat format = new SimpleDateFormat(pattern, Locale.CANADA);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }

    /**
     * Returns {@code true} if an attribute (usually the <cite>valid range</cite>) should be
     * converted from unpacked to packed units. The <A HREF="http://www.cfconventions.org/">CF
     * Metadata conventions</A> states that valid ranges should be in packed units, but not
     * every NetCDF files follow this advice in practice. The UCAR NetCDF library applies the
     * following heuristic rules (quoting from {@link ucar.nc2.dataset.EnhanceScaleMissing}):
     *
     * <blockquote>
     * If {@code valid_range} is the same type as {@code scale_factor} (actually the wider of
     * {@code scale_factor} and {@code add_offset}) and this is wider than the external data,
     * then it will be interpreted as being in the units of the internal (unpacked) data.
     * Otherwise it is in the units of the external (packed) data.
     * <blockquote>
     *
     * However some NetCDF files stores unpacked ranges using the same type than packed data.
     * The above cited heuristic rule can not resolve those cases.
     * <p>
     * If this method returns {@code true}, then the attribute is assumed in unpacked units no
     * matter what the CF convention and the heuristic rules said. If this method returns
     * {@code false}, then UCAR's heuristic rules applies.
     * <p>
     * The default implementation returns {@code false} in all cases.
     *
     * @param  attribute The attribute (usually {@code "valid_range"}).
     * @return {@code true} if the attribute should be converted from unpacked to packed units
     *         regardless CF convention and UCAR's heuristic rules.
     *
     * @see ucar.nc2.dataset.EnhanceScaleMissing
     */
    protected boolean forcePacking(final String attribute) {
        return false;
    }

    /**
     * Convenience method for logging a warning.
     */
    private void warning(final String method, final int key, final Object value) {
        final LogRecord record = Errors.getResources(getLocale()).
                getLogRecord(Level.WARNING, key, value);
        record.setSourceClassName(NetcdfMetadata.class.getName());
        record.setSourceMethodName(method);
        warningOccurred(record);
    }
}
