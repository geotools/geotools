/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.netcdf.utilities;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.measure.Unit;
import javax.measure.format.UnitFormat;
import org.geotools.imageio.Identification;
import org.geotools.metadata.sql.MetadataException;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotools.referencing.factory.ReferencingFactoryContainer;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPosition;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.factory.GeoTools;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.TemporalCRS;
import org.opengis.referencing.crs.VerticalCRS;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.cs.TimeCS;
import org.opengis.referencing.cs.VerticalCS;
import org.opengis.referencing.datum.TemporalDatum;
import org.opengis.referencing.datum.VerticalDatum;
import org.opengis.referencing.datum.VerticalDatumType;
import org.opengis.temporal.Position;
import si.uom.NonSI;
import si.uom.SI;
import tec.uom.se.format.SimpleUnitFormat;
import ucar.nc2.Attribute;
import ucar.nc2.constants.AxisType;
import ucar.nc2.constants.CF;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.VariableDS;

/** Utility class to build {@link CoordinateReferenceSystem} objects. */
public class NetCDFCRSUtilities {

    private static final java.util.logging.Logger LOGGER =
            Logger.getLogger(NetCDFCRSUtilities.class.toString());

    public static final ReferencingFactoryContainer FACTORY_CONTAINER =
            ReferencingFactoryContainer.instance(GeoTools.getDefaultHints());

    static final PrecisionModel PRECISION_MODEL = new PrecisionModel(PrecisionModel.FLOATING);

    public static final GeometryFactory GEOM_FACTORY = new GeometryFactory(PRECISION_MODEL);

    /**
     * Set of commonly used symbols for "seconds".
     *
     * @todo Needs a more general way to set unit symbols once the Unit API is completed.
     */
    private static final String[] DAYS = {"day", "dd", "days since"};

    /**
     * Set of commonly used symbols for "degrees".
     *
     * @todo Needs a more general way to set unit symbols once the Unit API is completed.
     */
    private static final String[] DEGREES = {"degree", "degrees", "deg", "°"};

    /**
     * Set of commonly used symbols for "seconds".
     *
     * @todo Needs a more general way to set unit symbols once the Unit API is completed.
     */
    private static final String[] HOURS = {"hour", "hh", "hours since"};

    /**
     * Set of commonly used symbols for "metres".
     *
     * @todo Needs a more general way to set unit symbols once the Unit API is completed.
     */
    private static final String[] METERS = {"meter", "meters", "metre", "metres", "m"};

    /**
     * Set of commonly used symbols for "seconds".
     *
     * @todo Needs a more general way to set unit symbols once the Unit API is completed.
     */
    private static final String[] MINUTES = {"minute", "min", "minutes since"};

    /**
     * Set of commonly used symbols for "seconds".
     *
     * @todo Needs a more general way to set unit symbols once the Unit API is completed.
     */
    private static final String[] SECONDS = {"second", "sec", "seconds since"};

    public static final Set<String> VERTICAL_AXIS_NAMES = new HashSet<String>();
    /** The mapping between UCAR axis type and ISO axis directions. */
    private static final Map<AxisType, String> DIRECTIONS = new HashMap<AxisType, String>(16);

    private static final Map<AxisType, String> OPPOSITES = new HashMap<AxisType, String>(16);

    /**
     * this flag states if an automatic conversion from km to m should happen with axis/coordinates
     */
    public static final String CONVERT_AXIS_KM_KEY =
            "org.geotools.coverage.io.netcdf.convertAxis.km";

    private static final boolean CONVERT_AXIS_KM;

    static {
        add(AxisType.Time, "future", "past");
        add(AxisType.RunTime, "future", "past");
        add(AxisType.GeoX, "east", "west");
        add(AxisType.GeoY, "north", "south");
        add(AxisType.GeoZ, "up", "down");
        add(AxisType.Lat, "north", "south");
        add(AxisType.Lon, "east", "west");
        add(AxisType.Height, "up", "down");
        add(AxisType.Pressure, "up", "down");
        VERTICAL_AXIS_NAMES.add("elevation");
        VERTICAL_AXIS_NAMES.add("height");
        VERTICAL_AXIS_NAMES.add("z");
        VERTICAL_AXIS_NAMES.add("depth");
        VERTICAL_AXIS_NAMES.add("pressure");

        // Default is false, resulting into no automatic conversion anymore
        CONVERT_AXIS_KM = Boolean.parseBoolean(System.getProperty(CONVERT_AXIS_KM_KEY, "false"));
    }

    /** The object to use for parsing and formatting units. */
    private static final UnitFormat UNIT_FORMAT = SimpleUnitFormat.getInstance();

    /** Adds a mapping between UCAR type and ISO direction. */
    private static void add(final AxisType type, final String direction, final String opposite) {
        if (DIRECTIONS.put(type, direction) != null) {
            throw new IllegalArgumentException(String.valueOf(type));
        }

        if (OPPOSITES.put(type, opposite) != null) {
            throw new IllegalArgumentException(String.valueOf(type));
        }
    }

    static String[] getUnitDirection(CoordinateAxis axis) {
        AxisType type = axis.getAxisType();
        String units = axis.getUnitsString();
        /*
         * Gets the axis direction, taking in account the possible reversal or
         * vertical axis. Note that geographic and projected
         * CoordinateReferenceSystem have the same directions. We can
         * distinguish them either using the ISO CoordinateReferenceSystem type
         * ("geographic" or "projected"), the ISO CS type ("ellipsoidal" or
         * "cartesian") or the units ("degrees" or "m").
         */
        String direction = DIRECTIONS.get(type);
        if (direction != null) {
            if (CF.POSITIVE_DOWN.equalsIgnoreCase(axis.getPositive())) {
                direction = OPPOSITES.get(type);
            }
            final int offset = units.lastIndexOf('_');
            if (offset >= 0) {
                final String unitsDirection = units.substring(offset + 1).trim();
                final String opposite = OPPOSITES.get(type);
                if (unitsDirection.equalsIgnoreCase(opposite)) {
                    // TODO WARNING: INCONSISTENT AXIS ORIENTATION
                    direction = opposite;
                }
                if (unitsDirection.equalsIgnoreCase(direction)) {
                    units = units.substring(0, offset).trim();
                }
            }
        }
        return new String[] {units, direction};
    }

    /** Get the {@link AxisDirection} object related to the specified direction */
    static AxisDirection getDirection(final String direction) {
        return AxisDirection.valueOf(direction);
    }

    /**
     * Check if {@code toSearch} appears in the {@code list} array. Search is case-insensitive. This
     * is a temporary patch (will be removed when the final API for JSR-108: Units specification
     * will be available).
     */
    private static boolean contains(final String toSearch, final String[] list) {
        for (int i = list.length; --i >= 0; ) {
            if (toSearch.toLowerCase().contains(list[i].toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static VerticalCRS buildVerticalCrs(CoordinateAxis zAxis) {
        VerticalCRS verticalCRS = null;
        try {
            if (zAxis != null) {
                String axisName = zAxis.getFullName();
                if (!NetCDFCRSUtilities.VERTICAL_AXIS_NAMES.contains(axisName)) {
                    return null;
                }
                String units = zAxis.getUnitsString();
                AxisType axisType = zAxis.getAxisType();

                String v_crsName = "Unknown";
                String v_datumName = "Unknown";
                String v_datumType = null;
                v_datumName =
                        new Identification("Mean Sea Level", null, null, "EPSG:5100").getName();

                if (axisType == AxisType.RadialAzimuth
                        || axisType == AxisType.GeoZ
                        || axisType == AxisType.RadialElevation) v_datumType = "geoidal";
                else if (axisType == AxisType.Height) {
                    if (!zAxis.getShortName().equalsIgnoreCase("height")) {
                        v_datumType = "depth";
                        v_crsName =
                                new Identification("mean sea level depth", null, null, "EPSG:5715")
                                        .getName();
                    } else {
                        v_datumType = "geoidal";
                        v_crsName =
                                new Identification("mean sea level height", null, null, "EPSG:5714")
                                        .getName();
                    }
                } else if (axisType == AxisType.Pressure) {
                    v_datumType = "barometric";
                } else {
                    v_datumType = "other_surface";
                }

                /*
                 * Gets the axis direction, taking in account the possible reversal or
                 * vertical axis. Note that geographic and projected
                 * CoordinateReferenceSystem have the same directions. We can
                 * distinguish them either using the ISO CoordinateReferenceSystem type
                 * ("geographic" or "projected"), the ISO CS type ("ellipsoidal" or
                 * "cartesian") or the units ("degrees" or "m").
                 */
                String direction = DIRECTIONS.get(axisType);
                if (direction != null) {
                    if (CF.POSITIVE_DOWN.equalsIgnoreCase(zAxis.getPositive())) {
                        direction = OPPOSITES.get(axisType);
                    }
                    final int offset = units.lastIndexOf('_');
                    if (offset >= 0) {
                        final String unitsDirection = units.substring(offset + 1).trim();
                        final String opposite = OPPOSITES.get(axisType);
                        if (unitsDirection.equalsIgnoreCase(opposite)) {
                            // TODO WARNING: INCONSISTENT AXIS ORIENTATION
                            direction = opposite;
                        }
                        if (unitsDirection.equalsIgnoreCase(direction)) {
                            units = units.substring(0, offset).trim();
                        }
                    }
                }
                final Map<String, String> csMap = Collections.singletonMap("name", "vertical_CS");
                VerticalCS verticalCS =
                        NetCDFCRSUtilities.FACTORY_CONTAINER
                                .getCSFactory()
                                .createVerticalCS(
                                        csMap,
                                        getAxis(
                                                zAxis.getShortName(),
                                                getDirection(direction),
                                                units));

                // Creating the Vertical Datum
                final Map<String, String> datumMap = Collections.singletonMap("name", v_datumName);
                final VerticalDatum verticalDatum =
                        NetCDFCRSUtilities.FACTORY_CONTAINER
                                .getDatumFactory()
                                .createVerticalDatum(
                                        datumMap, VerticalDatumType.valueOf(v_datumType));

                final Map<String, String> crsMap = Collections.singletonMap("name", v_crsName);
                verticalCRS =
                        NetCDFCRSUtilities.FACTORY_CONTAINER
                                .getCRSFactory()
                                .createVerticalCRS(crsMap, verticalDatum, verticalCS);
            }
        } catch (FactoryException e) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "Unable to parse vertical CRS", e);
            verticalCRS = null;
        }
        return verticalCRS;
    }

    public static TemporalCRS buildTemporalCrs(CoordinateAxis timeAxis) {
        String t_datumName = new Identification("ISO8601", null, null, null).getName();
        TemporalCRS temporalCRS = null;
        try {
            if (timeAxis != null) {
                AxisType type = timeAxis.getAxisType();
                String units = timeAxis.getUnitsString();

                /*
                 * Gets the axis direction, taking in account the possible reversal or
                 * vertical axis. Note that geographic and projected
                 * CoordinateReferenceSystem have the same directions. We can
                 * distinguish them either using the ISO CoordinateReferenceSystem type
                 * ("geographic" or "projected"), the ISO CS type ("ellipsoidal" or
                 * "cartesian") or the units ("degrees" or "m").
                 */
                String direction = DIRECTIONS.get(type);
                if (direction != null) {
                    if (CF.POSITIVE_DOWN.equalsIgnoreCase(timeAxis.getPositive())) {
                        direction = OPPOSITES.get(type);
                    }
                    final int offset = units.lastIndexOf('_');
                    if (offset >= 0) {
                        final String unitsDirection = units.substring(offset + 1).trim();
                        final String opposite = OPPOSITES.get(type);
                        if (unitsDirection.equalsIgnoreCase(opposite)) {
                            // TODO WARNING: INCONSISTENT AXIS ORIENTATION
                            direction = opposite;
                        }
                        if (unitsDirection.equalsIgnoreCase(direction)) {
                            units = units.substring(0, offset).trim();
                        }
                    }
                }

                Date epoch = null;
                String t_originDate = null;
                if (AxisType.Time.equals(type) || AxisType.RunTime.equals(type)) {
                    String origin = null;
                    final String[] unitsParts = units.split("(?i)\\s+since\\s+");
                    if (unitsParts.length == 2) {
                        units = unitsParts[0].trim();
                        origin = unitsParts[1].trim();
                    } else {
                        final Attribute attribute = timeAxis.findAttribute("time_origin");
                        if (attribute != null) {
                            origin = attribute.getStringValue();
                        }
                    }
                    if (origin != null) {
                        origin = NetCDFTimeUtilities.trimFractionalPart(origin);
                        // add 0 digits if absent
                        origin = NetCDFTimeUtilities.checkDateDigits(origin);

                        try {
                            epoch =
                                    (Date)
                                            NetCDFUtilities.getAxisFormat(type, origin)
                                                    .parseObject(origin);
                            GregorianCalendar cal = new GregorianCalendar();
                            cal.setTime(epoch);
                            DefaultInstant instant =
                                    new DefaultInstant(new DefaultPosition(cal.getTime()));
                            t_originDate = instant.getPosition().getDateTime().toString();
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                            // TODO: Change the handle this exception
                        }
                    }
                }

                String axisName = timeAxis.getShortName();

                String t_csName = "time_CS";
                final Map<String, String> csMap = Collections.singletonMap("name", t_csName);
                final TimeCS timeCS =
                        NetCDFCRSUtilities.FACTORY_CONTAINER
                                .getCSFactory()
                                .createTimeCS(
                                        csMap, getAxis(axisName, getDirection(direction), units));

                // Creating the Temporal Datum
                if (t_datumName == null) {
                    t_datumName = "Unknown";
                }
                final Map<String, String> datumMap = Collections.singletonMap("name", t_datumName);
                final Position timeOrigin =
                        new DefaultPosition(new SimpleInternationalString(t_originDate));
                final TemporalDatum temporalDatum =
                        NetCDFCRSUtilities.FACTORY_CONTAINER
                                .getDatumFactory()
                                .createTemporalDatum(datumMap, timeOrigin.getDate());

                // Finally creating the Temporal CoordinateReferenceSystem
                String crsName = "time_CRS";
                final Map<String, String> crsMap = Collections.singletonMap("name", crsName);
                temporalCRS =
                        NetCDFCRSUtilities.FACTORY_CONTAINER
                                .getCRSFactory()
                                .createTemporalCRS(crsMap, temporalDatum, timeCS);
            }
        } catch (FactoryException e) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "Unable to parse temporal CRS", e);
            temporalCRS = null;
        } catch (ParseException e) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "Unable to parse temporal CRS", e);
            temporalCRS = null;
        }
        return temporalCRS;
    }

    public static ucar.nc2.dataset.CoordinateSystem getCoordinateSystem(VariableDS variableDS) {
        final List<ucar.nc2.dataset.CoordinateSystem> systems = variableDS.getCoordinateSystems();
        if (systems.isEmpty()) {
            throw new RuntimeException(
                    "Coordinate system for Variable "
                            + variableDS.getFullName()
                            + " haven't been found");
        }
        return systems.get(0);
    }

    public static final org.opengis.referencing.crs.CoordinateReferenceSystem WGS84;

    static {
        CoordinateReferenceSystem internalWGS84 = null;
        try {
            internalWGS84 = CRS.decode("EPSG:4326", true);
        } catch (Exception e) {
            internalWGS84 = DefaultGeographicCRS.WGS84;
        }
        WGS84 = internalWGS84;
    }
    /**
     * Build a proper {@link CoordinateSystemAxis} given the set composed of axisName, axisDirection
     * and axis unit of measure.
     *
     * @param axisName the name of the axis to be built.
     * @param direction the {@linkplain AxisDirection direction} of the axis.
     * @param unitName the unit of measure string.
     * @return a proper {@link CoordinateSystemAxis} instance or {@code null} if unable to build it.
     */
    static CoordinateSystemAxis getAxis(
            final String axisName, final AxisDirection direction, final String unitName)
            throws FactoryException {
        if (axisName == null) {
            return null;
        }
        final DefaultCoordinateSystemAxis axisFound =
                DefaultCoordinateSystemAxis.getPredefined(axisName, direction);
        if (axisFound != null) {
            return axisFound;
        }

        /*
         * The current axis defined in the metadata tree is not already known in
         * the Geotools implementation, so one will build it using those
         * information.
         */
        final Unit<?> unit = getUnit(unitName);
        final Map<String, String> map = Collections.singletonMap("name", axisName);
        try {
            return FACTORY_CONTAINER
                    .getCSFactory()
                    .createCoordinateSystemAxis(map, axisName, direction, unit);
        } catch (FactoryException e) {
            throw new FactoryException(e.getLocalizedMessage());
        }
    }

    /**
     * Returns the unit which matches with the name given.
     *
     * @param unitName The name of the unit. Should not be {@code null}.
     * @return The unit matching with the specified name.
     * @throws MetadataException if the unit name does not match with the {@linkplain #UNIT_FORMAT
     *     unit format}.
     */
    static Unit<?> getUnit(final String unitName) throws FactoryException {
        if (contains(unitName, METERS)) {
            return SI.METRE;
        } else if (contains(unitName, DEGREES)) {
            return NonSI.DEGREE_ANGLE;
        } else if (contains(unitName, SECONDS)) {
            return SI.SECOND;
        } else if (contains(unitName, MINUTES)) {
            return SI.MINUTE;
        } else if (contains(unitName, HOURS)) {
            return SI.HOUR;
        } else if (contains(unitName, DAYS)) {
            return SI.DAY;
        } else {
            try {
                return (Unit<?>) UNIT_FORMAT.parse(unitName);
            } catch (UnsupportedOperationException e) {
                throw new FactoryException("Unit not known : " + unitName, e);
            }
        }
    }

    /** Return true if the NetCDF CRS Parsing machinery will convert km coordinates to meter */
    public static boolean isConvertAxisKm() {
        return CONVERT_AXIS_KM;
    }
}
