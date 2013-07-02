///*
// *    GeoTools - The Open Source Java GIS Toolkit
// *    http://geotools.org
// *
// *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
// *
// *    This library is free software; you can redistribute it and/or
// *    modify it under the terms of the GNU Lesser General Public
// *    License as published by the Free Software Foundation;
// *    version 2.1 of the License.
// *
// *    This library is distributed in the hope that it will be useful,
// *    but WITHOUT ANY WARRANTY; without even the implied warranty of
// *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// *    Lesser General Public License for more details.
// */
//package org.geotools.imageio.unidata.utilities;
//
//import java.io.IOException;
//import java.text.ParseException;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.List;
//import java.util.Map;
//import java.util.SortedSet;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import javax.measure.unit.Unit;
//
//import org.geotools.coverage.io.util.DateRangeTreeSet;
//import org.geotools.coverage.io.util.DoubleRangeTreeSet;
//import org.geotools.geometry.jts.ReferencedEnvelope;
//import org.geotools.metadata.sql.MetadataException;
//import org.geotools.referencing.datum.DefaultEllipsoid;
//import org.geotools.referencing.datum.DefaultGeodeticDatum;
//import org.geotools.referencing.datum.DefaultPrimeMeridian;
//import org.geotools.resources.UnmodifiableArrayList;
//import org.geotools.util.DateRange;
//import org.geotools.util.NumberRange;
//import org.opengis.metadata.content.Band;
//import org.opengis.referencing.FactoryException;
//import org.opengis.referencing.cs.CSFactory;
//import org.opengis.referencing.cs.CoordinateSystemAxis;
//import org.opengis.referencing.datum.Datum;
//import org.opengis.referencing.datum.DatumFactory;
//import org.opengis.referencing.datum.Ellipsoid;
//import org.opengis.referencing.datum.GeodeticDatum;
//import org.opengis.referencing.datum.PrimeMeridian;
//
//import ucar.ma2.DataType;
//import ucar.nc2.Attribute;
//import ucar.nc2.Variable;
//import ucar.nc2.constants.AxisType;
//import ucar.nc2.dataset.CoordinateAxis;
//import ucar.nc2.dataset.CoordinateAxis1D;
//import ucar.nc2.dataset.CoordinateSystem;
//import ucar.nc2.dataset.NetcdfDataset;
//import ucar.nc2.dataset.VariableDS;
//
//import com.vividsolutions.jts.geom.Geometry;
//
///**
// * @author User
// *
// */
//@Deprecated // methods in this class might die soon
//abstract public class UnusedUtilities {
//
//    private static class KeyValuePair implements Map.Entry<String, String> {
//    
//        public KeyValuePair(final String key, final String value) {
//            this.key = key;
//            this.value = value;
//        }
//    
//        private String key;
//    
//        private String value;
//    
//        public String getKey() {
//            return key;
//        }
//    
//        public String getValue() {
//            return value;
//        }
//    
//        private boolean equal(Object a, Object b) {
//            return a == b || a != null && a.equals(b);
//        }
//    
//        public boolean equals(Object o) {
//            return o instanceof KeyValuePair && equal(((KeyValuePair) o).key, key)
//                    && equal(((KeyValuePair) o).value, value);
//        }
//    
//        private static int hashCode(Object a) {
//            return a == null ? 42 : a.hashCode();
//        }
//    
//        public int hashCode() {
//            return hashCode(key) * 3 + hashCode(value);
//        }
//    
//        public String toString() {
//            return "(" + key + "," + value + ")";
//        }
//    
//        public String setValue(String value) {
//            this.value = value;
//            return value;
//        }
//    }
//
//    @Deprecated // should die soon
//    private static class TimeBuilder {
//        public TimeBuilder(CoordinateAxis1D axis) {
//            axis1D = axis;
//            values = axis1D.getCoordValues();
//            units = axis.getUnitsString();
//            /*
//             * Gets the axis origin. In the particular case of time axis, units are typically written in the form "days since 1990-01-01
//             * 00:00:00". We extract the part before "since" as the units and the part after "since" as the date.
//             */
//            origin = null;
//            final String[] unitsParts = units.split("(?i)\\s+since\\s+");
//            if (unitsParts.length == 2) {
//                units = unitsParts[0].trim();
//                origin = unitsParts[1].trim();
//            } else {
//                final Attribute attribute = axis.findAttribute("time_origin");
//                if (attribute != null) {
//                    origin = attribute.getStringValue();
//                }
//            }
//            if (origin != null) {
//                origin = UnidataTimeUtilities.trimFractionalPart(origin);
//                // add 0 digits if absent
//                origin = UnidataTimeUtilities.checkDateDigits(origin);
//    
//                try {
//                    epoch = (Date) UnidataUtilities.getAxisFormat(AxisType.Time, origin)
//                            .parseObject(origin);
//                } catch (ParseException e) {
//                    LOGGER.warning("Error while parsing time Axis. Skip setting the TemporalExtent from coordinateAxis");
//                }
//            }
//        }
//    
//        String units;
//    
//        String origin;
//    
//        Date epoch;
//    
//        CoordinateAxis1D axis1D;
//    
//        double[] values = null;
//    
//        public static final int JGREG = 15 + 31 * (10 + 12 * 1582);
//    
//        public Date buildTime(int timeIndex) {
//            if (epoch != null) {
//                Calendar cal = new GregorianCalendar();
//                cal.setTime(epoch);
//                int vi = (int) Math.floor(values[timeIndex]);
//                double vd = values[timeIndex] - vi;
//                cal.add(UnidataTimeUtilities.getTimeUnits(units, null), vi);
//                if (vd != 0.0) {
//                    cal.add(UnidataTimeUtilities.getTimeUnits(units, vd), UnidataTimeUtilities.getTimeSubUnitsValue(units, vd));
//                }
//                return cal.getTime();
//            }
//            return null;
//    
//        }
//    
//        public int getNumTimes() {
//            return values.length;
//        }
//    }
//
//    @Deprecated // should die soon
//    public
//    static class AxisValueGetter {
//        
//        CoordinateAxis1D axis1D;
//        
//        double[] values = null;
//        
//        public AxisValueGetter(CoordinateAxis axis) {
//            if (axis.isNumeric() && axis instanceof CoordinateAxis1D) {
//                axis1D = (CoordinateAxis1D) axis;
//                values = axis1D.getCoordValues();
//                Attribute scaleFactor = axis1D.findAttribute("scale_factor");
//                Attribute offset = axis1D.findAttribute("offset");
//                if (scaleFactor != null || offset != null) {
//                    rescaleValues(scaleFactor, offset);
//                }
//    
//            } else {
//                throw new IllegalArgumentException(
//                        "The specified axis doesn't represent a valid zeta Axis");
//            }
//        }
//    
//        private void rescaleValues(Attribute scaleFactor, Attribute offset) {
//            DataType dataType = scaleFactor != null ? scaleFactor.getDataType() : offset.getDataType();
//            if (dataType == DataType.DOUBLE) {
//                double sf = scaleFactor != null ? scaleFactor.getNumericValue().doubleValue() : 1.0d; 
//                double off = offset != null ? offset.getNumericValue().doubleValue() : 0.0d;
//                for (int i = 0 ; i < values.length; i++) {
//                    values[i] = ( sf * values[i]) + off;
//                }    
//            } else if (dataType == DataType.FLOAT) {
//                float sf = scaleFactor != null ? scaleFactor.getNumericValue().floatValue() : 1.0f; 
//                float  off = offset != null ? offset.getNumericValue().floatValue() : 0.0f;
//                for (int i = 0 ; i < values.length; i++) {
//                    values[i] = ( sf * values[i]) + off;
//                }
//            }
//        }
//    
//        public double build(int index) {
//            if (values != null && values.length > index) {
//                return values[index];
//            }
//            return Double.NaN;
//        }
//        
//        public int getNumValues() {
//            return values.length;
//        }
//    }
//
//    public static class ProjAttribs {
//        public final static String PROJECT_TO_IMAGE_AFFINE = "proj_to_image_affine";
//    
//        public final static String PROJECT_ORIGIN_LATITUDE = "proj_origin_latitude";
//    
//        public final static String PROJECT_ORIGIN_LONGITUDE = "proj_origin_longitude";
//    
//        public final static String EARTH_FLATTENING = "earth_flattening";
//    
//        public final static String EQUATORIAL_RADIUS = "equatorial_radius";
//    
//        public final static String STANDARD_PARALLEL_1 = "std_parallel_1";
//    
//        private ProjAttribs() {
//    
//        }
//    }
//
//    public static class DatasetAttribs {
//        public final static String VALID_RANGE = "valid_range";
//    
//        public final static String VALID_MIN = "valid_min";
//    
//        public final static String VALID_MAX = "valid_max";
//    
//        public final static String LONG_NAME = "long_name";
//    
//        public final static String FILL_VALUE = "_FillValue";
//        
//        public final static String MISSING_VALUE = "missing_value";
//    
//        public final static String SCALE_FACTOR = "scale_factor";
//    
//        // public final static String SCALE_FACTOR_ERR = "scale_factor_err";
//    
//        public final static String ADD_OFFSET = "add_offset";
//    
//        // public final static String ADD_OFFSET_ERR = "add_offset_err";
//        public final static String UNITS = "units";
//    
//        private DatasetAttribs() {
//    
//        }
//    }
//
//    private static KeyValuePair getGlobalAttribute(final NetcdfDataset dataset, final int attributeIndex) throws IOException {
//    	KeyValuePair attributePair = null;
//        if (dataset != null) {
//        	final List<Attribute> globalAttributes = dataset.getGlobalAttributes();
//            if (globalAttributes != null && !globalAttributes.isEmpty()) {
//            	final Attribute attribute = (Attribute) globalAttributes.get(attributeIndex);
//                if (attribute != null) {
//                    attributePair = new KeyValuePair(attribute.getFullName(), getAttributesAsString(attribute));
//                }
//            }
//        }
//        return attributePair;
//    }
//
//    private static KeyValuePair getAttribute(final Variable var, final int attributeIndex) {
//    	KeyValuePair attributePair = null;
//    	if (var != null){
//    		final List<Attribute> attributes = var.getAttributes();
//    	    if (attributes != null && !attributes.isEmpty()) {
//    	    	final Attribute attribute = (Attribute) attributes.get(attributeIndex);
//    	        if (attribute != null) {
//    	            attributePair = new KeyValuePair(attribute.getFullName(),getAttributesAsString(attribute));
//    	        }
//    	    }
//    	}
//        return attributePair;
//    }
//
//    /**
//         * Return a global attribute as a {@code String}. The required global
//         * attribute is specified by name
//         * 
//         * @param attributeName
//         *                the name of the required attribute.
//         * @return the value of the required attribute. Returns an empty String in
//         *         case the required attribute is not found.
//         */
//    private static String getGlobalAttributeAsString(final NetcdfDataset dataset, final String attributeName) {
//            String attributeValue = "";
//            if (dataset != null) {
//            	final Attribute attrib = dataset.findGlobalAttribute(attributeName);
//    //        	final List<Attribute> globalAttributes = dataset.getGlobalAttributes();
//    //            if (globalAttributes != null && !globalAttributes.isEmpty()) {
//    //                for (Attribute attrib: globalAttributes){
//                        if (attrib != null && attrib.getFullName().equals(attributeName)) {
//                            attributeValue = getAttributesAsString(attrib);
//    //                        break;
//                        }
//    //                }
//    //            }
//            }
//            return attributeValue;
//        }
//
//    private static String getAttributesAsString(Attribute attr) {
//        return getAttributesAsString(attr, false);
//    }
//
//    /**
//     * Return the value of a NetCDF {@code Attribute} instance as a
//     * {@code String}. The {@code isUnsigned} parameter allow to handle byte
//     * attributes as unsigned, in order to represent values in the range
//     * [0,255].
//     */
//    private static String getAttributesAsString(Attribute attr,
//            final boolean isUnsigned) {
//        String values[] = null;
//        if (attr != null) {
//            final int nValues = attr.getLength();
//            values = new String[nValues];
//            final DataType datatype = attr.getDataType();
//    
//            // TODO: Improve the unsigned management
//            if (datatype == DataType.BYTE) {
//                if (isUnsigned)
//                    for (int i = 0; i < nValues; i++) {
//                        byte val = attr.getNumericValue(i).byteValue();
//                        int myByte = (0x000000FF & ((int) val));
//                        short anUnsignedByte = (short) myByte;
//                        values[i] = Short.toString(anUnsignedByte);
//                    }
//                else {
//                    for (int i = 0; i < nValues; i++) {
//                        byte val = attr.getNumericValue(i).byteValue();
//                        values[i] = Byte.toString(val);
//                    }
//                }
//            } else if (datatype == DataType.SHORT) {
//                for (int i = 0; i < nValues; i++) {
//                    short val = attr.getNumericValue(i).shortValue();
//                    values[i] = Short.toString(val);
//                }
//            } else if (datatype == DataType.INT) {
//                for (int i = 0; i < nValues; i++) {
//                    int val = attr.getNumericValue(i).intValue();
//                    values[i] = Integer.toString(val);
//                }
//            } else if (datatype == DataType.LONG) {
//                for (int i = 0; i < nValues; i++) {
//                    long val = attr.getNumericValue(i).longValue();
//                    values[i] = Long.toString(val);
//                }
//            } else if (datatype == DataType.DOUBLE) {
//                for (int i = 0; i < nValues; i++) {
//                    double val = attr.getNumericValue(i).doubleValue();
//                    values[i] = Double.toString(val);
//                }
//            } else if (datatype == DataType.FLOAT) {
//                for (int i = 0; i < nValues; i++) {
//                    float val = attr.getNumericValue(i).floatValue();
//                    values[i] = Float.toString(val);
//                }
//    
//            } else if (datatype == DataType.STRING) {
//                for (int i = 0; i < nValues; i++) {
//                    values[i] = attr.getStringValue(i);
//                }
//            } else {
//                if (LOGGER.isLoggable(Level.WARNING))
//                    LOGGER.warning("Unhandled Attribute datatype "
//                            + attr.getDataType().getClassType().toString());
//            }
//        }
//        String value = "";
//        if (values != null) {
//            StringBuffer sb = new StringBuffer();
//            int j = 0;
//            for (; j < values.length - 1; j++) {
//                sb.append(values[j]).append(",");
//            }
//            sb.append(values[j]);
//            value = sb.toString();
//        }
//        return value;
//    }
//
//    private static String getAttributesAsString(final Variable var, final String attributeName) {
//        String value = "";
//        if (var != null){
//        	Attribute attribute = var.findAttribute(attributeName);
//        	if (attribute != null)
//        		value = getAttributesAsString(attribute, false); 
//        	
//        }
//    	return value;
//    }
//
//    /** The LOGGER for this class. */
//    private static final Logger LOGGER = Logger.getLogger(UnusedUtilities.class.toString());
//
//    /**
//     * Gets the name, as the "description", "title" or "standard name" attribute
//     * if possible, or as the variable name otherwise.
//     */
//    private static String getName( final Variable variable ) {
//        String name = variable.getDescription();
//        if (name == null || (name = name.trim()).length() == 0) {
//            name = variable.getFullName();
//        }
//        return name;
//    }
//
//    /**
//     * Return the temporal extent related to that coordinateAxis-
//     * @param axis
//     * @return
//     */
//    private static DateRange getTemporalExtent(CoordinateAxis axis) {
//        if (axis == null || !AxisType.Time.equals(axis.getAxisType())) {
//            throw new IllegalArgumentException("The specified axis is not a time axis");
//        }
//        TimeBuilder timeBuilder = new TimeBuilder((CoordinateAxis1D)axis);
//        Date startTime = timeBuilder.buildTime(0);
//        Date endTime = timeBuilder.buildTime(timeBuilder.getNumTimes() - 1);
//        return new DateRange(startTime, endTime);
//    }
//
//    /**
//     * Return the full temporal extent set related to that coordinateAxis-
//     * @param axis
//     * @return
//     */    
//    private static SortedSet<DateRange> getTemporalExtentSet(CoordinateAxis axis) {
//        if (axis == null || !AxisType.Time.equals(axis.getAxisType())) {
//            throw new IllegalArgumentException("The specified axis is not a time axis");
//        }
//    
//        TimeBuilder timeBuilder = new TimeBuilder((CoordinateAxis1D)axis);
//        SortedSet<DateRange> sorted = new DateRangeTreeSet();
//        final int numTimes = timeBuilder.getNumTimes();
//        for (int i = 0; i < numTimes; i++) {
//            Date startTime = timeBuilder.buildTime(i);
//            sorted.add(new DateRange(startTime, startTime));
//        }
//        return sorted;
//    }
//
//    /**
//     * Return the vertical extent related to that coordinateAxis-
//     * @param axis
//     * @return
//     */
//    private static NumberRange<Double> getVerticalExtent(CoordinateAxis zAxis) {
//        AxisType axisType = null;
//        if (zAxis == null
//                || ((axisType = zAxis.getAxisType()) != AxisType.Height
//                        && axisType != AxisType.GeoZ && axisType != AxisType.Pressure)) {
//            throw new IllegalArgumentException("The specified axis is not a vertical axis");
//        }
//        UnusedUtilities.AxisValueGetter builder = new UnusedUtilities.AxisValueGetter(zAxis);
//        Double start = builder.build(0);
//        Double end = builder.build(builder.getNumValues() - 1);
//        return new NumberRange<Double>(Double.class, start, end);
//    }
//
//    /**
//     * Return the full vertical extent set related to that coordinateAxis-
//     * @param axis
//     * @return
//     */  
//    private static SortedSet<NumberRange<Double>> getVerticalExtentSet(CoordinateAxis zAxis) {
//        AxisType axisType = null;
//        if (zAxis == null
//                || ((axisType = zAxis.getAxisType()) != AxisType.Height
//                        && axisType != AxisType.GeoZ && axisType != AxisType.Pressure)) {
//            throw new IllegalArgumentException("The specified axis is not a vertical axis");
//        }
//        UnusedUtilities.AxisValueGetter builder = new UnusedUtilities.AxisValueGetter(zAxis);
//        SortedSet<NumberRange<Double>> sorted = new DoubleRangeTreeSet();
//        final int numZetas = builder.getNumValues();
//        for (int i = 0; i < numZetas; i++) {
//            Double start = builder.build(i);
//            sorted.add(new NumberRange<Double>(Double.class, start, start));
//        }
//        return sorted;
//     }
//
//    public static double[] getEnvelope(ucar.nc2.dataset.CoordinateSystem cs ) {
//        // TODO: Handle 3D GEO CoordinateReferenceSystem
//        double[] envelope = null;
//        if (cs != null) {
//            /*
//             * Adds the axis in reverse order, because the NetCDF image reader
//             * put the last dimensions in the rendered image. Typical NetCDF
//             * convention is to put axis in the (time, depth, latitude,
//             * longitude) order, which typically maps to (longitude, latitude,
//             * depth, time) order in our referencing framework.
//             */
//            final List<CoordinateAxis> axes = cs.getCoordinateAxes();
//            envelope = new double[]{Double.NaN, Double.NaN, Double.NaN, Double.NaN};
//            for( int i = axes.size(); --i >= 0; ) {
//                final CoordinateAxis axis = axes.get(i);
//    
//                // final String name = UnidataSliceUtilities.getName(axis);
//                final AxisType type = axis.getAxisType();
//                // final String units = axis.getUnitsString();
//    
//                /*
//                 * Gets the axis direction, taking in account the possible
//                 * reversal or vertical axis. Note that geographic and projected
//                 * CoordinateReferenceSystem have the same directions. We can
//                 * distinguish them either using the ISO
//                 * CoordinateReferenceSystem type ("geographic" or "projected"),
//                 * the ISO CS type ("ellipsoidal" or "cartesian") or the units
//                 * ("degrees" or "m").
//                 */
//    
//                /*
//                 * If the axis is not numeric, we can't process any further. If
//                 * it is, then adds the coordinate and index ranges.
//                 */
//                if (axis.isNumeric() && axis instanceof CoordinateAxis1D && !AxisType.Time.equals(type)) {
//                    final CoordinateAxis1D axis1D = (CoordinateAxis1D) axis;
//                    final int length = axis1D.getDimension(0).getLength();
//                    if (length > 2 && axis1D.isRegular()) {
//                        final double increment = axis1D.getIncrement();
//                        final double start = axis1D.getStart();
//                        final double end = start + increment * (length - 1); // Inclusive
//    
//                        if (AxisType.Lon.equals(type) || AxisType.GeoX.equals(type)) {
//                            if (increment > 0) {
//                                envelope[0] = start;
//                                envelope[2] = end;
//                            } else {
//                                envelope[0] = end;
//                                envelope[2] = start;
//                            }
//                        }
//    
//                        if (AxisType.Lat.equals(type) || AxisType.GeoY.equals(type)) {
//                            if (increment > 0) {
//                                envelope[1] = start;
//                                envelope[3] = end;
//                            } else {
//                                envelope[1] = end;
//                                envelope[3] = start;
//                            }
//                        }
//                    } else {
//    
//                        final double[] values = axis1D.getCoordValues();
//                        final double val0 = values[0];
//                        final double valN = values[values.length - 1];
//    
//                        if (AxisType.Lon.equals(type) || AxisType.GeoX.equals(type)) {
//                            // if (CoordinateAxis.POSITIVE_DOWN
//                            // .equalsIgnoreCase(axis.getPositive())) {
//                            // envelope[1] = values[0];
//                            // envelope[3] = values[values.length - 1];
//                            // } else {
//                            envelope[0] = val0;
//                            envelope[2] = valN;
//                            // }
//                        }
//    
//                        if (AxisType.Lat.equals(type) || AxisType.GeoY.equals(type)) {
//                            // if (CoordinateAxis.POSITIVE_DOWN
//                            // .equalsIgnoreCase(axis.getPositive())) {
//                            // envelope[0] = values[0];
//                            // envelope[2] = values[values.length - 1];
//                            // } else {
//                            envelope[1] = val0;
//                            envelope[3] = valN;
//                            // }
//                        }
//                    }
//                }
//            }
//            for( int i = 0; i < envelope.length; i++ )
//                if (Double.isNaN(envelope[i])) {
//                    envelope = null;
//                    break;
//                }
//    
//        }
//        return envelope;
//    }
//
//    public static Geometry extractEnvelopeAsGeometry(VariableDS variable) {
//        final List<ucar.nc2.dataset.CoordinateSystem> systems = variable.getCoordinateSystems();
//        ucar.nc2.dataset.CoordinateSystem cs = systems.get(0);
//        double[] wsen = getEnvelope(cs);
//        // Currently we only support Geographic CRS
//        ReferencedEnvelope referencedEnvelope = new ReferencedEnvelope(wsen[0], wsen[2], wsen[1], wsen[3], UnidataCRSUtilities.WGS84);
//        return UnidataCRSUtilities.GEOM_FACTORY.toGeometry(referencedEnvelope);
//    }
//
//    /**
//     * Returns the {@linkplain CoordinateSystem coordinate system}. The default
//     * implementation builds a coordinate system using the
//     * {@linkplain UnidataCRSUtilities#getAxis axes} defined in the metadata.
//     * 
//     * @throws MetadataException
//     *                 if there is less than 2 axes defined in the metadata, or
//     *                 if the creation of the coordinate system failed.
//     * 
//     * @see UnidataCRSUtilities#getAxis
//     * @see CoordinateSystem
//     */
//    public static org.opengis.referencing.cs.CoordinateSystem getCoordinateSystem( String csName, final List<CoordinateAxis> axes ) throws Exception {
//        int dimension = axes.size();
//    
//        // FIXME how to tell when baseCRS would have been null?
//        final String type = UnusedUtilities.ELLIPSOIDAL;
//    
//        final CSFactory factory = UnidataCRSUtilities.FACTORY_CONTAINER.getCSFactory();
//        final Map<String, String> map = Collections.singletonMap("name", csName);
//        if (dimension < 2) {
//            throw new MetadataException("Number of dimension error : " + dimension);
//        }
//        try {
//            if (dimension < 3) {
//                CoordinateAxis axis1 = axes.get(0);
//                String[] unitDirection1 = UnidataCRSUtilities.getUnitDirection(axis1);
//                CoordinateSystemAxis csAxis1 = UnidataCRSUtilities.getAxis(axis1.getName(), UnidataCRSUtilities.getDirection(unitDirection1[1]), unitDirection1[0]);
//                CoordinateAxis axis2 = axes.get(1);
//                String[] unitDirection2 = UnidataCRSUtilities.getUnitDirection(axis2);
//                CoordinateSystemAxis csAxis2 = UnidataCRSUtilities.getAxis(axis2.getName(), UnidataCRSUtilities.getDirection(unitDirection2[1]), unitDirection2[0]);
//                if (type.equalsIgnoreCase(UnusedUtilities.CARTESIAN)) {
//                    return factory.createCartesianCS(map, csAxis1, csAxis2);
//                }
//                if (type.equalsIgnoreCase(UnusedUtilities.ELLIPSOIDAL)) {
//                    return factory.createEllipsoidalCS(map, csAxis1, csAxis2);
//                }
//            } else {
//                CoordinateAxis axis1 = axes.get(0);
//                String[] unitDirection1 = UnidataCRSUtilities.getUnitDirection(axis1);
//                CoordinateSystemAxis csAxis1 = UnidataCRSUtilities.getAxis(axis1.getName(), UnidataCRSUtilities.getDirection(unitDirection1[1]), unitDirection1[0]);
//                CoordinateAxis axis2 = axes.get(1);
//                String[] unitDirection2 = UnidataCRSUtilities.getUnitDirection(axis2);
//                CoordinateSystemAxis csAxis2 = UnidataCRSUtilities.getAxis(axis2.getName(), UnidataCRSUtilities.getDirection(unitDirection2[1]), unitDirection2[0]);
//                CoordinateAxis axis3 = axes.get(1);
//                String[] unitDirection3 = UnidataCRSUtilities.getUnitDirection(axis3);
//                CoordinateSystemAxis csAxis3 = UnidataCRSUtilities.getAxis(axis3.getName(), UnidataCRSUtilities.getDirection(unitDirection3[1]), unitDirection3[0]);
//    
//                if (type.equalsIgnoreCase(UnusedUtilities.CARTESIAN)) {
//                    return factory.createCartesianCS(map, csAxis1, csAxis2, csAxis3);
//                }
//                if (type.equalsIgnoreCase(UnusedUtilities.ELLIPSOIDAL)) {
//                    return factory.createEllipsoidalCS(map, csAxis1, csAxis2, csAxis3);
//                }
//            }
//            /*
//             * Should not happened, since the type value should be contained in
//             * the {@link UnidataGeospatialMetadata#CS_TYPES} list.
//             */
//            throw new Exception("Coordinate system type not known : " + type);
//        } catch (FactoryException e) {
//            throw new Exception(e.getLocalizedMessage());
//        }
//    }
//
//    /**
//     * Returns the ellipsoid. Depending on whether
//     * {@link ImageReferencing#semiMinorAxis} or
//     * {@link ImageReferencing#inverseFlattening} has been defined, the default
//     * implementation will construct an ellispoid using
//     * {@link DatumFactory#createEllipsoid} or
//     * {@link DatumFactory#createFlattenedSphere} respectively.
//     * 
//     * @throws MetadataException
//     *                 if the operation failed to create the
//     *                 {@linkplain Ellipsoid ellipsoid}.
//     * 
//     * @see #getUnit(String)
//     */
//    private static Ellipsoid getEllipsoid( String ellipsoidName, double semiMajorAxis, double inverseFlattening, String ellipsoidUnit,
//            String secondDefiningParameterType ) throws Exception {
//        if (ellipsoidName != null) {
//            for( final DefaultEllipsoid ellipsoid : ELLIPSOIDS ) {
//                if (ellipsoid.nameMatches(ellipsoidName)) {
//                    return ellipsoid;
//                }
//            }
//        } else {
//            throw new Exception("Ellipsoid name not defined.");
//        }
//        // It has a name defined, but it is not present in the list of known
//        // ellipsoids.
//        if (Double.isNaN(semiMajorAxis)) {
//            throw new Exception("Ellipsoid semi major axis not defined.");
//        }
//        if (ellipsoidUnit == null) {
//            throw new Exception("Ellipsoid unit not defined.");
//        }
//        final Unit unit = UnidataCRSUtilities.getUnit(ellipsoidUnit);
//        final Map<String, String> map = Collections.singletonMap("name", ellipsoidName);
//        try {
//            final DatumFactory datumFactory = UnidataCRSUtilities.FACTORY_CONTAINER.getDatumFactory();
//            return (secondDefiningParameterType.equals(UnusedUtilities.MD_DTM_GD_EL_SEMIMINORAXIS)) ? datumFactory
//                    .createEllipsoid(map, semiMajorAxis, semiMajorAxis, unit) : datumFactory.createFlattenedSphere(map,
//                    semiMajorAxis, inverseFlattening, unit);
//        } catch (FactoryException e) {
//            throw new Exception(e.getLocalizedMessage(),e);
//        }
//    }
//
//    /**
//     * Returns the datum. The default implementation performs the following
//     * steps:
//     * <p>
//     * <ul>
//     * <li>Verifies if the datum name contains {@code WGS84}, and returns a
//     * {@link DefaultGeodeticDatum#WGS84} geodetic datum if it is the case.
//     * </li>
//     * <li>Builds a {@linkplain PrimeMeridian prime meridian} using information
//     * stored into the metadata tree. </li>
//     * <li>Returns a {@linkplain DefaultGeodeticDatum geodetic datum} built on
//     * the prime meridian. </li>
//     * </ul>
//     * </p>
//     * @param ellipsoidName 
//     * @param semiMajorAxus 
//     * @param inverseFlattening 
//     * @param ellipsoidUnit 
//     * @param secondDefiningParameter 
//     * 
//     * @throws MetadataException
//     *                 if the datum is not defined, or if the
//     *                 {@link #getEllipsoid} method fails.
//     * 
//     * @todo: The current implementation only returns a
//     *        {@linkplain GeodeticDatum geodetic datum}, other kind of datum
//     *        have to be generated too.
//     * 
//     * @see #getEllipsoid
//     */
//    public static Datum getDatum( String datumName, double greenwichLon, String primeMeridianName, String ellipsoidName,
//            double semiMajorAxus, double inverseFlattening, String ellipsoidUnit, String secondDefiningParameter )
//            throws Exception {
//    
//        if (datumName == null) {
//            throw new Exception("Datum not defined.");
//        }
//        if (datumName.toUpperCase().contains("WGS84")) {
//            return DefaultGeodeticDatum.WGS84;
//        }
//        final PrimeMeridian primeMeridian;
//        /*
//         * By default, if the prime meridian name is not defined, or if it is
//         * defined with {@code Greenwich}, one chooses the {@code Greenwich}
//         * meridian as prime meridian. Otherwise one builds it, using the
//         * {@code greenwichLongitude} parameter.
//         */
//        if ((primeMeridianName == null) || (primeMeridianName != null && primeMeridianName.toLowerCase().contains("greenwich"))) {
//            primeMeridian = DefaultPrimeMeridian.GREENWICH;
//        } else {
//    
//            primeMeridian = (Double.isNaN(greenwichLon)) ? DefaultPrimeMeridian.GREENWICH : new DefaultPrimeMeridian(
//                    primeMeridianName, greenwichLon);
//        }
//    
//        Ellipsoid ellipsoid = getEllipsoid(ellipsoidName, semiMajorAxus, inverseFlattening, ellipsoidUnit,
//                secondDefiningParameter);
//        return new DefaultGeodeticDatum(datumName, ellipsoid, primeMeridian);
//    }
//
//    /**
//     * Set of {@linkplain DefaultEllipsoid ellipsoids} already defined.
//     */
//    private static final DefaultEllipsoid[] ELLIPSOIDS = new DefaultEllipsoid[]{//
//    DefaultEllipsoid.CLARKE_1866, //
//            DefaultEllipsoid.GRS80, //
//            DefaultEllipsoid.INTERNATIONAL_1924, //
//            DefaultEllipsoid.SPHERE, //
//            DefaultEllipsoid.WGS84//
//    };
//    public final static String MD_AX_ABBREVIATION = "axisAbbrev";
//    public final static String MD_AX_DIRECTION = "axisDirection";
//    public final static String MD_AX_MAX = "maximumValue";
//    public final static String MD_AX_MIN = "minimumValue";
//    public final static String MD_AX_RANGEMEANING = "rangeMeaning";
//    public final static String MD_AX_UOM = "axisUoM";
//    // ////
//    //
//    // Axis element
//    //
//    // ////
//    public final static String MD_AXIS = "Axis";
//    public final static String MD_COMM_ALIAS = "alias";
//    public final static String MD_COMM_ATTRIBUTETYPE = "type";
//    public final static String MD_COMM_ATTRIBUTEVALUE = "value";
//    public final static String MD_COMM_IDENTIFIER = "identifier";
//    // ////////////////////////////////////////////////////////////////////////
//    // 
//    // Metadata nodes names
//    //
//    // ////////////////////////////////////////////////////////////////////////
//    
//    // ////
//    //
//    // Common element (deeply used by several elements)
//    //
//    // ////
//    public final static String MD_COMM_NAME = "name";
//    public final static String MD_COMM_REMARKS = "remarks";
//    // ////
//    //
//    // CRSs elements
//    //
//    // ////
//    public final static String MD_COORDINATEREFERENCESYSTEM = "CoordinateReferenceSystem";
//    // ////
//    //
//    // Coordinate System element (deeply used in CRSs)
//    //
//    // ////
//    public final static String MD_COORDINATESYSTEM = "CoordinateSystem";
//    public final static String MD_CRS = "CoordinateReferenceSystem";
//    public final static String MD_CS_AXES = "Axes";
//    // ////
//    //
//    // Datum elements
//    //
//    // ////
//    public final static String MD_DATUM = "Datum";
//    // Datum common elements
//    public final static String MD_DTM_ANCHORPOINT = "anchorPoint";
//    // //
//    //
//    // Engineering Datum
//    //
//    // //
//    public final static String MD_DTM_ENGINEERING = "EngineeringDatum";
//    public final static String MD_DTM_GD_EL_INVERSEFLATTENING = "inverseFlattening";
//    public final static String MD_DTM_GD_EL_SECONDDEFPARAM = "secondDefiningParameter";
//    public final static String MD_DTM_GD_EL_SEMIMAJORAXIS = "semiMajorAxis";
//    public final static String MD_DTM_GD_EL_SEMIMINORAXIS = "semiMinorAxis";
//    public final static String MD_DTM_GD_EL_SPHERE = "sphere";
//    public final static String MD_DTM_GD_EL_UNIT = "unit";
//    // Ellipsoid
//    public final static String MD_DTM_GD_ELLIPSOID = "Ellipsoid";
//    public final static String MD_DTM_GD_PM_GREENWICHLONGITUDE = "greenwichLongitude";
//    // //
//    //
//    // Geodetic Datum
//    //
//    // //
//    public final static String MD_DTM_GEODETIC = "GeodeticDatum";
//    // Prime Meridian
//    public final static String MD_DTM_GEODETIC_PRIMEMERIDIAN = "PrimeMeridian";
//    public final static String MD_DTM_ID_PIXELINCELL = "pixelInCell";
//    // //
//    //
//    // Image Datum
//    //
//    // //
//    public final static String MD_DTM_IMAGE = "ImageDatum";
//    public final static String MD_DTM_REALIZATIONEPOCH = "realizationEpoch";
//    public final static String MD_DTM_TD_ORIGIN = "origin";
//    // //
//    //
//    // Temporal Datum
//    //
//    // //
//    public final static String MD_DTM_TEMPORAL = "TemporalDatum";
//    public final static String MD_DTM_VD_TYPE = "verticalDatumType";
//    // //
//    //
//    // Vertical Datum
//    //
//    // //
//    public final static String MD_DTM_VERTICAL = "VerticalDatum";
//    public final static String MD_RG_LI_RASTERLAYOUT = "RasterLayout";
//    public final static String MD_SCRS_BASE_CRS = "BaseCRS";
//    public final static String MD_SCRS_DBC_FORMULA = "formula";
//    public final static String MD_SCRS_DBC_PARAMETER_VALUE = "parameter";
//    public final static String MD_SCRS_DBC_PARAMETERS = "parameters";
//    public final static String MD_SCRS_DBC_SRC_DIM = "srcDim";
//    public final static String MD_SCRS_DBC_TARGET_DIM = "targetDim";
//    // ////
//    //
//    // definedByConversion element
//    //
//    // ////
//    
//    public final static String MD_SCRS_DEFINED_BY_CONVERSION = "definedByConversion";
//    public final static String MD_SCRS_DERIVED_CRS = "DerivedCRS";
//    public final static String MD_SCRS_PROJECTED_CRS = "ProjectedCRS";
//    public final static String MD_TEMPORALCRS = "TemporalCRS";
//    public final static String MD_VERTICALCRS = "VerticalCRS";
//
//    public static String getCrsType( ucar.nc2.dataset.CoordinateSystem cs ) {
//        String crsType;
//        // TODO: fix this to handle Vertical instead of Geographic3D
//        if (cs.isLatLon()) {
//            crsType = cs.hasVerticalAxis() ? UnusedUtilities.GEOGRAPHIC_3D : UnusedUtilities.GEOGRAPHIC;
//            // csType = UnidataMetadataUtilities.ELLIPSOIDAL;
//        } else if (cs.isGeoXY()) {
//            crsType = cs.hasVerticalAxis() ? UnusedUtilities.PROJECTED_3D : UnusedUtilities.PROJECTED;
//            // csType = UnidataMetadataUtilities.CARTESIAN;
//        } else {
//            throw new RuntimeException("DOCUMENT ME");
//        }
//        return crsType;
//    }
//
//    /**
//     * The cartesian {@linkplain CoordinateSystem coordinate system} type.
//     * 
//     * @see #setCoordinateSystem
//     */
//    public static final String CARTESIAN = "cartesian";
//    /**
//     * Enumeration of valid coordinate reference system types.
//     */
//    static final List<String> CRS_TYPES = UnmodifiableArrayList
//            .wrap(new String[] { UnusedUtilities.GEOGRAPHIC, UnusedUtilities.PROJECTED, UnusedUtilities.DERIVED });
//    /**
//     * Enumeration of valid coordinate system types.
//     */
//    static final List<String> CS_TYPES = UnmodifiableArrayList
//            .wrap(new String[] { UnusedUtilities.ELLIPSOIDAL, CARTESIAN });
//    /**
//     * The derived
//     * {@linkplain AbstractCoordinateReferenceSystem coordinate reference system}
//     * type.
//     * 
//     * @see #setCoordinateReferenceSystem
//     */
//    public static final String DERIVED = "derived";
//    /**
//     * Enumeration of valid axis directions. We do not declare {@link String}
//     * constants for them since they are already available as
//     * {@linkplain org.opengis.referencing.cs.AxisDirection axis direction} code
//     * list.
//     */
//    static final List<String> DIRECTIONS = UnmodifiableArrayList
//            .wrap(new String[] { "north", "east", "south", "west", "up", "down" });
//    /**
//     * The ellipsoidal {@linkplain CoordinateSystem coordinate system} type.
//     * 
//     * @see #setCoordinateSystem
//     */
//    public static final String ELLIPSOIDAL = "ellipsoidal";
//    public static final String FORMAT_NAME = "org_geotools_gce_nplugin_geospatialMetadata_1.0";
//    /**
//     * The geographic
//     * {@linkplain AbstractCoordinateReferenceSystem coordinate reference system}
//     * type. This is often used together with the
//     * {@linkplain #ELLIPSOIDAL ellipsoidal} coordinate system type.
//     * 
//     * @see #setCoordinateReferenceSystem
//     */
//    public static final String GEOGRAPHIC = "geographic";
//    /**
//     * The geographic
//     * {@linkplain AbstractCoordinateReferenceSystem coordinate reference system}
//     * type with a vertical axis. This is often used together with a
//     * three-dimensional {@linkplain #ELLIPSOIDAL ellipsoidal} coordinate system
//     * type.
//     * <p>
//     * If the coordinate reference system has no vertical axis, or has
//     * additional axis of other kind than vertical (for example only a temporal
//     * axis), then the type should be the plain {@value #GEOGRAPHIC}. This is
//     * because such CRS are usually constructed as
//     * {@linkplain org.opengis.referencing.crs.CompoundCRS compound CRS} rather
//     * than a CRS with a three-dimensional coordinate system.
//     * <p>
//     * To be strict, a 3D CRS should be allowed only if the vertical axis is of
//     * the kind "height above the ellipsoid" (as opposed to "height above the
//     * geoid" for example), otherwise we have a compound CRS. But many datafile
//     * don't make this distinction.
//     * 
//     * @see #setCoordinateReferenceSystem
//     */
//    public static final String GEOGRAPHIC_3D = "geographic3D";
//    /**
//     * The geophysics {@linkplain Band sample dimension} type. Pixels
//     * in the {@linkplain java.awt.image.RenderedImage rendered image} produced
//     * by the image reader contain directly geophysics values like temperature
//     * or elevation. Sample type is typically {@code float} or {@code double}
//     * and missing value, if any, <strong>must</strong> be one of
//     * {@linkplain Float#isNaN NaN values}.
//     */
//    public static final String GEOPHYSICS = "geophysics";
//    /**
//     * The packed {@linkplain Band sample dimension} type. Pixels in
//     * the {@linkplain java.awt.image.RenderedImage rendered image} produced by
//     * the image reader contain packed data, typically as {@code byte} or
//     * {@code short} integer type. Conversions to geophysics values are
//     * performed by the application of a scale and offset. Some special values
//     * are typically used for missing values.
//     */
//    public static final String PACKED = "packed";
//    /**
//     * Enumeration of valid pixel orientation. We do not declare {@link String}
//     * constants for them since they are already available as
//     * {@linkplain org.opengis.metadata.spatial.PixelOrientation pixel
//     * orientation} code list.
//     */
//    static final List<String> PIXEL_ORIENTATIONS = UnmodifiableArrayList
//            .wrap(new String[] { "center", "lower left", "lower right",
//                    "upper right", "upper left" });
//    /**
//     * The projected
//     * {@linkplain AbstractCoordinateReferenceSystem coordinate reference system}
//     * type. This is often used together with the
//     * {@linkplain #CARTESIAN cartesian} coordinate system type.
//     * 
//     * @see #setCoordinateReferenceSystem
//     */
//    public static final String PROJECTED = "projected";
//    /**
//     * The projected
//     * {@linkplain AbstractCoordinateReferenceSystem coordinate reference system}
//     * type with a vertical axis. This is often used together with a
//     * three-dimensional {@linkplain #CARTESIAN cartesian} coordinate system
//     * type.
//     * <p>
//     * If the coordinate reference system has no vertical axis, or has
//     * additional axis of other kind than vertical (for example only a temporal
//     * axis), then the type should be the plain {@value #PROJECTED}. This is
//     * because such CRS are usually constructed as
//     * {@linkplain org.opengis.referencing.crs.CompoundCRS compound CRS} rather
//     * than a CRS with a three-dimensional coordinate system.
//     * <p>
//     * To be strict, a 3D CRS should be allowed only if the vertical axis is of
//     * the kind "height above the ellipsoid" (as opposed to "height above the
//     * geoid" for example), otherwise we have a compound CRS. But many datafile
//     * don't make this distinction.
//     * 
//     * @see #setCoordinateReferenceSystem
//     */
//    public static final String PROJECTED_3D = "projected3D";
//    /**
//     * Enumeration of valid sample dimention types.
//     */
//    static final List<String> SAMPLE_TYPES = UnmodifiableArrayList.wrap(new String[] { GEOPHYSICS, PACKED });
//
//}
