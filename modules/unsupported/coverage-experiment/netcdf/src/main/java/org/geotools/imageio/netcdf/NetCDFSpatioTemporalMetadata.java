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

import it.geosolutions.imageio.ndplugin.BaseImageMetadata;
import it.geosolutions.imageio.plugins.netcdf.NetCDFImageMetadata;
import it.geosolutions.imageio.plugins.netcdf.NetCDFUtilities;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.metadata.IIOMetadata;

import org.geotools.imageio.SpatioTemporalImageReader;
import org.geotools.imageio.metadata.AbstractCoordinateReferenceSystem;
import org.geotools.imageio.metadata.Axis;
import org.geotools.imageio.metadata.Band;
import org.geotools.imageio.metadata.BoundedBy;
import org.geotools.imageio.metadata.CoordinateReferenceSystem;
import org.geotools.imageio.metadata.Identification;
import org.geotools.imageio.metadata.RectifiedGrid;
import org.geotools.imageio.metadata.SpatioTemporalMetadata;
import org.geotools.imageio.metadata.SpatioTemporalMetadataFormat;
import org.geotools.imageio.metadata.TemporalCRS;
import org.geotools.imageio.metadata.VerticalCRS;
import org.geotools.imageio.metadata.AbstractCoordinateReferenceSystem.Datum;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPosition;
import org.opengis.temporal.TemporalObject;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import ucar.ma2.Range;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.Variable;
import ucar.nc2.constants.AxisType;
import ucar.nc2.constants.CF;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.CoordinateAxis1D;
import ucar.nc2.dataset.CoordinateSystem;
import ucar.nc2.dataset.VariableDS;

/**
 * Class involved in spatioTemporal metadata settings for NetCDF data sources.
 * 
 * @author Alessio Fabiani, GeoSolutions
 * @author Daniele Romagnoli, GeoSolutions
 */
public class NetCDFSpatioTemporalMetadata extends SpatioTemporalMetadata {

    protected final static Logger LOGGER = Logger
            .getLogger("org.geotools.imageio.netcdf");

    public NetCDFSpatioTemporalMetadata(SpatioTemporalImageReader reader,
            int imageIndex) {
        super(reader, imageIndex);
    }

    /**
     * The mapping between UCAR axis type and ISO axis directions.
     */
    private static final Map<AxisType, String> DIRECTIONS = new HashMap<AxisType, String>(16);

    private static final Map<AxisType, String> OPPOSITES = new HashMap<AxisType, String>(16);
    static {
        add(AxisType.Time, "future", "past");
        add(AxisType.GeoX, "east", "west");
        add(AxisType.GeoY, "north", "south");
        add(AxisType.GeoZ, "up", "down");
        add(AxisType.Lat, "north", "south");
        add(AxisType.Lon, "east", "west");
        add(AxisType.Height, "up", "down");
        add(AxisType.Pressure, "up", "down");
    }

    /**
     * Adds a mapping between UCAR type and ISO direction.
     */
    private static void add(final AxisType type, final String direction,
            final String opposite) {
        if (DIRECTIONS.put(type, direction) != null) {
            throw new IllegalArgumentException(String.valueOf(type));
        }

        if (OPPOSITES.put(type, opposite) != null) {
            throw new IllegalArgumentException(String.valueOf(type));
        }
    }

    protected void setCoordinateReferenceSystemElement(
            SpatioTemporalImageReader reader) {
        checkReader(reader);
        Variable variable = ((NetCDFSpatioTemporalImageReader) reader)
                .getVariable(getImageIndex());
        if (variable != null && variable instanceof VariableDS) {
            final List<CoordinateSystem> systems = ((VariableDS) variable)
                    .getCoordinateSystems();
            if (!systems.isEmpty()) {
                addCoordinateReferenceSystem(systems.get(0));
            }
        }
    }

    private void checkReader(SpatioTemporalImageReader reader) {
        if (reader == null)
            throw new IllegalArgumentException(
                    "null spatio-temporal reader provided");
        else if (!(reader instanceof NetCDFSpatioTemporalImageReader)) {
            throw new IllegalArgumentException(
                    "Invalid spatio-temporal reader provided");
        }
    }

    /**
     * Adding CoordinateReferenceSystem nodes to this
     * {@link SpatioTemporalMetadata} instance.
     * 
     * @param cs
     *                the available {@link CoordinateSystem} netcdf object
     *                obtained from the current netCDF variable.
     */
    private void addCoordinateReferenceSystem(CoordinateSystem cs) {
        String crsType, csType;
        // TODO: fix this to handle Vertical instead of Geographic3D
        if (cs == null)
            throw new IllegalArgumentException(
                    "Provided CoordinateSystem is null");

        if (cs.isLatLon()) {
            crsType = cs.hasVerticalAxis() ? SpatioTemporalMetadataFormat.GEOGRAPHIC_3D
                    : SpatioTemporalMetadataFormat.GEOGRAPHIC;
            csType = SpatioTemporalMetadataFormat.ELLIPSOIDAL;
        } else if (cs.isGeoXY()) {
            crsType = cs.hasVerticalAxis() ? SpatioTemporalMetadataFormat.PROJECTED_3D
                    : SpatioTemporalMetadataFormat.PROJECTED;
            csType = SpatioTemporalMetadataFormat.CARTESIAN;
        } else {
            crsType = null;
            csType = null;
        }

        // ////
        //
        // Setting up the AbstractCoordinateReferenceSystem
        //
        // ////
        CoordinateReferenceSystem crs = getCRS(crsType);
        if (crsType == SpatioTemporalMetadataFormat.GEOGRAPHIC
                || crsType == SpatioTemporalMetadataFormat.GEOGRAPHIC_3D) {
            crs.setIdentification(new Identification("WGS 84", null, null,"EPSG:4326"));
            crs.setCoordinateSystem(new Identification("WGS 84", null, null, null));

            // //
            // Datum and Ellipsoid
            // //
            crs.setDatum(Datum.GEODETIC_DATUM, new Identification("WGS_1984", "World Geodetic System 1984", null, "EPSG:6326"));
            crs.addPrimeMeridian("0.0", new Identification("Greenwich", null, null, "EPSG:8901"));
            crs.addEllipsoid("6378137.0", null, "298.257223563", "meter", new Identification("WGS 84", null, null, "EPSG:7030"));
        } else if (crsType == SpatioTemporalMetadataFormat.PROJECTED
                || crsType == SpatioTemporalMetadataFormat.PROJECTED_3D) {
            // TODO Handle this case ... we need an example of netCDF projected
            // CoordinateReferenceSystem
        }

        /*
         * Adds the axis in reverse order, because the NetCDF image reader put
         * the last dimensions in the rendered image. Typical NetCDF convention
         * is to put axis in the (time, depth, latitude, longitude) order, which
         * typically maps to (longitude, latitude, depth, time) order in
         * GeoTools referencing framework.
         */
        final List<CoordinateAxis> axes = cs.getCoordinateAxes();
        for (int i = axes.size(); --i >= cs.getRankDomain() - 2;) {
            addCoordinateAxis(crs, axes.get(i));
        }

        // ////
        //
        // VerticalCRS
        //
        // ////
        if (cs.getRankDomain() > 2 && cs.hasVerticalAxis()) {
            setHasVerticalCRS(true);
            VerticalCRS vCRS = getVerticalCRS();
            vCRS.setDatum(new Identification("Mean Sea Level", null, null,
                    "EPSG:5100"));

            if (cs.getElevationAxis() != null || cs.getAzimuthAxis() != null
                    || cs.getZaxis() != null)
                vCRS.addVerticalDatumType("geoidal");
            else if (cs.getHeightAxis() != null) {
                CoordinateAxis axis = cs.getHeightAxis();
                if (!axis.getName().equalsIgnoreCase("height")) {
                    vCRS.addVerticalDatumType("depth");
                    vCRS.setIdentification(new Identification(
                            "mean sea level depth", null, null, "EPSG:5715"));
                } else {
                    vCRS.addVerticalDatumType("geoidal");
                    vCRS.setIdentification(new Identification(
                            "mean sea level height", null, null, "EPSG:5714"));
                }
            } else if (cs.getPressureAxis() != null)
                vCRS.addVerticalDatumType("barometric");
            else
                vCRS.addVerticalDatumType("other_surface");

            addCoordinateAxis(vCRS, axes.get(cs.getRankDomain() - NetCDFUtilities.Z_DIMENSION));
        }

        // ////
        //
        // TemporalCRS
        //
        // ////
        if (cs.getRankDomain() > 2 && cs.hasTimeAxis()) {
            setHasTemporalCRS(true);
            TemporalCRS tCRS = getTemporalCRS();
            tCRS.setDatum(new Identification("ISO8601", null, null, null));
            addCoordinateAxis(tCRS, axes.get(0));
        }
    }

    /**
     * 
     * 
     * @param crs
     * @param coordinateAxis
     */
    private void addCoordinateAxis(AbstractCoordinateReferenceSystem crs, CoordinateAxis axis) {
        final String name = axis.getName();
        final AxisType type = axis.getAxisType();
        String units = axis.getUnitsString();
        Date epoch = null;

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
        /*
         * Gets the axis origin. In the particular case of time axis, units are
         * typically written in the form "days since 1990-01-01 00:00:00". We
         * extract the part before "since" as the units and the part after
         * "since" as the date.
         */
        Axis netCDFaxis = crs.addAxis(new Identification(name), direction, units, null);

        if (AxisType.Time.equals(type)) {
            String origin = null;
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
            if (origin != null) {
                origin = NetCDFUtilities.trimFractionalPart(origin);
                // add 0 digits if absent
                origin = NetCDFSliceUtilities.checkDateDigits(origin);

                try {
                    epoch = (Date) NetCDFUtilities.getAxisFormat(type, origin).parseObject(origin);
                    if (crs instanceof TemporalCRS) {
                        GregorianCalendar cal = new GregorianCalendar();
                        cal.setTime(epoch);
                        DefaultInstant instant = new DefaultInstant(new DefaultPosition(cal.getTime()));
                        final String originDate = instant.getPosition().getDateTime().toString();
                        // TODO: Check this toString method
                        ((TemporalCRS) crs).addOrigin(originDate);
                    }
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                    // TODO: Change the handle this exception
                }
            }
        }

        /*
         * If the axis is not numeric, we can't process any further. If it is,
         * then adds the coordinate and index ranges.
         */
        if (!axis.isNumeric()) {
            return;
        }
        if (axis instanceof CoordinateAxis1D) {
            final CoordinateAxis1D axis1D = (CoordinateAxis1D) axis;
            final int length = axis1D.getDimension(0).getLength();
            AxisType axisType = axis1D.getAxisType();
            final boolean isZ = axisType == AxisType.Height
                    || axisType == AxisType.GeoZ
                    || axisType == AxisType.Pressure;
            if (length > 2 && axis1D.isRegular()) {
                // Reminder: pixel orientation is "center", maximum value is
                // inclusive.
                final double increment = axis1D.getIncrement();
                double start = axis1D.getStart();
                double end = start + increment * (length - 1); // Inclusive
                if (netCDFaxis != null) {
                    if (start > end && !isZ) {
                        double temp = start;
                        start = end;
                        end = temp;
                    }
                    netCDFaxis.setMinimumValue(String.valueOf(start));
                    netCDFaxis.setMaximumValue(String.valueOf(end));
                    // netCDFaxis.setRangeMeaning("exact");
                }
            } else {
                final double[] values = axis1D.getCoordValues();
                if (netCDFaxis != null) {
                    double start = values[0];
                    double end = values[values.length - 1];
                    if (start > end && !isZ) {
                        double temp = start;
                        start = end;
                        end = temp;
                    }
                    netCDFaxis.setMinimumValue(String.valueOf(start));
                    netCDFaxis.setMaximumValue(String.valueOf(end));
                    // netCDFaxis.setRangeMeaning("exact");
                }
            }
        }
    }

    @Override
    protected void setRectifiedGridElement(SpatioTemporalImageReader reader) {
        // TODO: Check the 3rd component settings
        final RectifiedGrid rectifiedGrid = getRectifiedGrid();
        checkReader(reader);
        final NetCDFSpatioTemporalImageReader netCDFReader = ((NetCDFSpatioTemporalImageReader) reader);
        final int imageIndex = getImageIndex();
        final Variable variable = netCDFReader.getVariable(imageIndex);
        final List<CoordinateSystem> systems = ((VariableDS) variable).getCoordinateSystems();
        if (!systems.isEmpty()) {
            int rank = variable.getRank() - (systems.get(0).hasTimeAxis() ? 1 : 0);
            boolean isZregular = true;
            List<Dimension> dimensions = variable.getDimensions();

            for (Dimension dim : dimensions) {
            	final Variable axisVar = netCDFReader.getCoordinate(dim.getName());
                if (axisVar != null && axisVar instanceof CoordinateAxis) {
                    final CoordinateAxis coordAxis = (CoordinateAxis) axisVar;
                    final AxisType axisType = coordAxis.getAxisType();
                    if (AxisType.GeoZ.equals(axisType)
                            || AxisType.Height.equals(axisType)
                            || AxisType.Pressure.equals(axisType)) {
                        if (coordAxis instanceof CoordinateAxis1D) {
                            CoordinateAxis1D axis = (CoordinateAxis1D) coordAxis;
//                            // if (!axis.isRegular()) {
//                            rank--;
//                            isZregular = false;
//                            // break;
//                            // }
                        }
                    }
                }
            }

            int i = rank - 1;
            int[] low = new int[rank];
            int[] high = new int[rank];
            String[] axesNames = new String[rank];
            double[] origin = new double[rank];
            double[][] offsetVectors = new double[rank][rank];

            for (Dimension dim : dimensions) {
            	final Variable axisVar = netCDFReader.getCoordinate(dim.getName());
                if (axisVar != null && axisVar instanceof CoordinateAxis) {
                    final CoordinateAxis coordAxis = (CoordinateAxis) axisVar;
                    final AxisType axisType = coordAxis.getAxisType();
                    if (!AxisType.Time.equals(axisType)) {
                        if (!AxisType.GeoZ.equals(axisType)
                                && !AxisType.Height.equals(axisType)
                                && !AxisType.Pressure.equals(axisType)) {
                            low[i] = 0;
                            high[i] = dim.getLength();
                        } else {
//                            if (isZregular) {
                                Range range = ((NetCDFSpatioTemporalImageReader) reader).getRange(imageIndex);
                                final int zIndex = NetCDFUtilities.getZIndex(variable, range, imageIndex);
                                low[i] = zIndex;
                                high[i] = zIndex + 1;
                                axesNames[i] = getAxisName(coordAxis);
//                            }
                        }
                            
                        if (i < 4 && netCDFReader.getCoordinate(dim.getName()) != null) {
                            if (coordAxis.isNumeric() && coordAxis instanceof CoordinateAxis1D) {
                                final CoordinateAxis1D axis1D = (CoordinateAxis1D) coordAxis;
                                final int length = axis1D.getDimension(0).getLength();
                                if (length > 2 && axis1D.isRegular()) {
                                    axesNames[i] = getAxisName(coordAxis);
                                    // Reminder: pixel orientation is
                                    // "center",
                                    // maximum value is inclusive.
                                    final double increment = axis1D
                                            .getIncrement();
                                    final double start = axis1D.getStart();
                                    final double end = start + increment * (length - 1); // Inclusive
                                    origin[i] = start;
                                    offsetVectors[i][i] = (end - start)/ length;
                                    i--;
                                } else {

                                    final double[] values = axis1D.getCoordValues();

                                    if (values != null) {
                                        final int valuesLength = values.length;
                                        if (valuesLength >= 2) {
                                            if (!Double.isNaN(values[0])
                                                    && !Double.isNaN(values[values.length - 1])) {
                                                origin[i] = values[0];
                                                offsetVectors[i][i] = (values[values.length - 1] - values[0])/ length;
                                                i--;
                                            } else {
                                                if (LOGGER.isLoggable(Level.FINE)) {
                                                    LOGGER.log(Level.FINE,
                                                                    "Axis values contains NaN; finding first valid values");
                                                }
                                                for (int j = 0; j < valuesLength; j++) {
                                                    double v = values[j];
                                                    if (!Double.isNaN(v)) {
                                                        for (int k = valuesLength; k > j; k--) {
                                                            double vv = values[k];
                                                            if (!Double
                                                                    .isNaN(vv)) {
                                                                origin[i] = v;
                                                                offsetVectors[i][i] = (vv - v)
                                                                        / length;
                                                                i--;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        else{
                                            origin[i] = values[0];
                                            offsetVectors[i][i] = 0;                                                    
                                            i--;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            rectifiedGrid.setLow(low);
            rectifiedGrid.setHigh(high);
            rectifiedGrid.setCoordinates(origin);

            for (int ov = 0; ov < offsetVectors.length; ov++) {
                rectifiedGrid.addOffsetVector(offsetVectors[ov]);
            }

            for (String axisName : axesNames) {
                rectifiedGrid.addAxisName(axisName);
            }
        }
    }

    private String getAxisName(CoordinateAxis coordAxis) {
        String axisName = coordAxis.getName();
        if (axisName.equalsIgnoreCase("lon"))
            axisName = "longitude";
        else if (axisName.equalsIgnoreCase("lat"))
            axisName = "latitude";
        return axisName;
    }

    @Override
    protected void setBandsElement(SpatioTemporalImageReader reader) {
        checkReader(reader);
        NetCDFSpatioTemporalImageReader netCDFReader = ((NetCDFSpatioTemporalImageReader) reader);
        final int imageIndex = getImageIndex();
        Band band = addBand();

        try {
            final IIOMetadata metadata = netCDFReader.getImageMetadata(imageIndex);
            if (metadata instanceof BaseImageMetadata) {
                final BaseImageMetadata commonMetadata = (BaseImageMetadata) metadata;
                setBandFromCommonMetadata(band, commonMetadata);
                Node node = commonMetadata.getAsTree(NetCDFImageMetadata.nativeMetadataFormatName);
                node = node.getFirstChild();
                if (node != null) {
                    final NamedNodeMap attributesMap = node.getAttributes();
                    if (attributesMap != null) {
                        Node units = attributesMap
                                .getNamedItem(NetCDFUtilities.DatasetAttribs.UNITS);
                        if (units != null) {
                            String unit = units.getNodeValue();
                            if (unit != null) {
                                band.setUoM(unit);
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.warning("Unable to set sampleDimension metadata");
        }
    }

    @Override
    protected void setBoundedByElement(SpatioTemporalImageReader reader) {
        checkReader(reader);
        final BoundedBy bb = getBoundedBy();
        double[] boundingBox = null;
        TemporalObject time = null;
        double verticalValue = Double.NaN;
        double[] envelope = null;

        final NetCDFSpatioTemporalImageReader geoNetCDFReader = ((NetCDFSpatioTemporalImageReader) reader);
        final int imageIndex = getImageIndex();
        final Range range = geoNetCDFReader.getRange(imageIndex);
        final Variable variable = geoNetCDFReader.getVariable(range);
        final CoordinateSystem cs = getCoordinateSystem(variable);

        if (cs != null) {
            envelope = NetCDFSliceUtilities.getEnvelope(cs);
            time = NetCDFSliceUtilities.getTimeValue(geoNetCDFReader, variable, range, imageIndex, cs);
            verticalValue = NetCDFSliceUtilities.getVerticalValue(geoNetCDFReader, variable,
                    range, imageIndex, cs);
        }
        if (envelope == null) {
            // TODO: Use an alternative way for setting envelope
            throw new IllegalArgumentException("Provided Envelope is null");
        }

        if (time == null) {
            // TODO: Use an alternative way for setting time extent
        }

        if (Double.isNaN(verticalValue)) {
            // TODO: Use an alternative way for setting vertical extent
        }

        // //
        //
        // Looking for a Geographic3D/Projected3D case vs VerticalCRS
        //
        // //
        if (!Double.isNaN(verticalValue)) {
            if (!isHasVerticalCRS()) {
                boundingBox = new double[] { envelope[0], envelope[1],
                        verticalValue, envelope[2], envelope[3], verticalValue };
            } else {
                setVerticalExtentNode(bb, verticalValue);
            }
        }
        if (boundingBox == null)
            boundingBox = envelope;

        setBoundingBoxNode(bb, boundingBox);
        if (isHasTemporalCRS())
            setTimeExtentNode(bb, time);
    }

    private static void setVerticalExtentNode(BoundedBy bb, double verticalValue) {
        if (bb != null && !Double.isNaN(verticalValue)) {
            bb.setVerticalExtent(verticalValue);
        }
    }

    private static void setBoundingBoxNode(BoundedBy bb, double[] boundingBox) {
        if (bb == null)
            throw new IllegalArgumentException("Provided BoundedBy element is null");
        if (boundingBox != null) {
            final int halfSize = boundingBox.length / 2;
            final double[] lc = new double[halfSize];
            final double[] uc = new double[halfSize];

            for (int i = 0; i < halfSize; i++) {
                if (boundingBox[i] > boundingBox[i + halfSize]) {
                    double temp = boundingBox[i + halfSize];
                    boundingBox[i + halfSize] = boundingBox[i];
                    boundingBox[i] = temp;
                }
                lc[i] = boundingBox[i];
                uc[i] = boundingBox[i + halfSize];
            }

            bb.setLowerCorner(lc);
            bb.setUpperCorner(uc);
        } else {
            throw new IllegalArgumentException(
                    "Provided Envelope element is null");
        }
    }

    private static CoordinateSystem getCoordinateSystem(Variable variable) {
        CoordinateSystem cs = null;
        final List<CoordinateSystem> systems = ((VariableDS) variable)
                .getCoordinateSystems();
        if (!systems.isEmpty()) {
            cs = systems.get(0);
        }
        return cs;
    }
}
