/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2016, Open Source Geospatial Foundation (OSGeo)
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

import it.geosolutions.imageio.stream.AccessibleStream;
import it.geosolutions.imageio.stream.input.URIImageInputStream;
import it.geosolutions.imageio.utilities.ImageIOUtilities;
import org.geotools.data.DataUtilities;
import org.geotools.gce.imagemosaic.ImageMosaicFormat;
import org.geotools.imageio.netcdf.cv.CoordinateHandlerFinder;
import org.geotools.imageio.netcdf.cv.CoordinateHandlerSpi;
import org.geotools.referencing.operation.projection.MapProjection;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import ucar.ma2.*;
import ucar.nc2.*;
import ucar.nc2.constants.AxisType;
import ucar.nc2.dataset.CoordinateAxis1D;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.dataset.NetcdfDataset.Enhance;
import ucar.nc2.dataset.VariableDS;
import ucar.nc2.jni.netcdf.Nc4Iosp;

import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Set of NetCDF utility methods.
 * 
 * @author Alessio Fabiani, GeoSolutions SAS
 * @author Daniele Romagnoli, GeoSolutions SAS
 * @author Simone Giannecchini, GeoSolutions SAS
 */
public class NetCDFUtilities {

    public static final boolean CHECK_COORDINATE_PLUGINS; 

    public static final String CHECK_COORDINATE_PLUGINS_KEY = "netcdf.coordinates.enablePlugins";

    public static final String NETCDF4_MIMETYPE = "application/x-netcdf4";

    public static final String NETCDF3_MIMETYPE = "application/x-netcdf";

    public static final String NETCDF = "NetCDF";

    public static final String NETCDF4 = "NetCDF4";

    public static final String NETCDF_4C = "NetCDF-4C";

    public static final String NETCDF_3 = "NetCDF-3";

    public static final String STANDARD_PARALLEL_1 = MapProjection.AbstractProvider.STANDARD_PARALLEL_1
    .getName().getCode();

    public static final String STANDARD_PARALLEL_2 = MapProjection.AbstractProvider.STANDARD_PARALLEL_2
    .getName().getCode();

    public static final String CENTRAL_MERIDIAN = MapProjection.AbstractProvider.CENTRAL_MERIDIAN
    .getName().getCode();

    public static final String LATITUDE_OF_ORIGIN = MapProjection.AbstractProvider.LATITUDE_OF_ORIGIN
    .getName().getCode();

    public static final String SCALE_FACTOR = MapProjection.AbstractProvider.SCALE_FACTOR.getName()
    .getCode();

    public static final String FALSE_EASTING = MapProjection.AbstractProvider.FALSE_EASTING
    .getName().getCode();

    public static final String FALSE_NORTHING = MapProjection.AbstractProvider.FALSE_NORTHING
    .getName().getCode();

    public static final String SEMI_MINOR = MapProjection.AbstractProvider.SEMI_MINOR
    .getName().getCode();

    public static final String SEMI_MAJOR = MapProjection.AbstractProvider.SEMI_MAJOR
    .getName().getCode();

    public static final String INVERSE_FLATTENING = "inverse_flattening";

    public static final String UNKNOWN = "unknown";

    public static final double DEFAULT_EARTH_RADIUS = 6371229.0d; 

    private NetCDFUtilities() {

    }

    /** The LOGGER for this class. */
    private static final Logger LOGGER = Logger.getLogger(NetCDFUtilities.class.toString());

    /** Set containing all the definition of the UNSUPPORTED DIMENSIONS to set as vertical ones*/
    private static final Set<String> UNSUPPORTED_DIMENSIONS;

    /** Set containing all the dimensions to be ignored */
    private static final Set<String> IGNORED_DIMENSIONS;
    
    /** Boolean indicating if GRIB library is available*/
    private static boolean IS_GRIB_AVAILABLE;

    /** Boolean indicating if NC4 C Library is available */
    private static boolean IS_NC4_LIBRARY_AVAILABLE;

    public static final String EXTERNAL_DATA_DIR;

    private static final String NETCDF_DATA_DIR = "NETCDF_DATA_DIR";

    public static final String FILL_VALUE = "_FillValue";

    public static final String MISSING_VALUE = "missing_value";

    public final static String LOWER_LEFT_LONGITUDE = "lower_left_longitude";

    public final static String LOWER_LEFT_LATITUDE = "lower_left_latitude";

    public final static String UPPER_RIGHT_LONGITUDE = "upper_right_longitude";

    public final static String UPPER_RIGHT_LATITUDE = "upper_right_latitude";

    public static final String COORDSYS = "latLonCoordSys";

    public final static String Y = "y";

    public final static String Y_COORD_PROJ = "y coordinate of projection";

    public final static String Y_PROJ_COORD = "projection_y_coordinate";

    public final static String X = "x";

    public final static String X_COORD_PROJ = "x coordinate of projection";

    public final static String X_PROJ_COORD = "projection_x_coordinate";
    
    public final static String LATITUDE = "latitude";

    public final static String LAT = "lat";

    public final static String LONGITUDE = "longitude";

    public final static String LON = "lon";

    public final static String GRID_LATITUDE = "grid_latitude";

    public final static String RLAT = "rlat";

    public final static String GRID_LONGITUDE = "grid_longitude";

    public final static String RLON = "rlon";

    public final static String DEPTH = "depth";

    public final static String ZETA = "z";

    public static final String BOUNDS = "bounds";

    private static final String BNDS = "bnds";

    public final static String HEIGHT = "height";

    public final static String TIME = "time";

    public static final String POSITIVE = "positive";

    public static final String UNITS = "units";

    public static final String NAME = "name";

    public static final String LONG_NAME = "long_name";

    public static final String ELEVATION_DIM = ImageMosaicFormat.ELEVATION.getName().toString();

    public static final String TIME_DIM = ImageMosaicFormat.TIME.getName().toString();

    public final static String STANDARD_NAME = "standard_name";

    public final static String DESCRIPTION = "description";

    public final static String M = "m";

    public final static String BOUNDS_SUFFIX = "_bnds";

    public final static String LON_UNITS = "degrees_east";

    public final static String LAT_UNITS = "degrees_north";

    public final static String RLATLON_UNITS = "degrees";

    public final static String NO_COORDS = "NoCoords";

    public final static String TIME_ORIGIN = "seconds since 1970-01-01 00:00:00 UTC";

    public final static long START_TIME;

    public final static String BOUNDARY_DIMENSION = "nv";

    public final static TimeZone UTC;

    public final static String GRID_MAPPING = "grid_mapping";

    public final static String GRID_MAPPING_NAME = "grid_mapping_name";

    public final static String COORDINATE_AXIS_TYPE = "_CoordinateAxisType";

    public final static String CONVENTIONS = "Conventions";

    public final static String COORD_SYS_BUILDER = "_CoordSysBuilder";

    public final static String COORD_SYS_BUILDER_CONVENTION = "ucar.nc2.dataset.conv.CF1Convention";

    public final static String COORDINATE_TRANSFORM_TYPE = "_CoordinateTransformType";

    public final static String COORDINATES = "coordinates";

    // They are recognized from GDAL
    public final static String SPATIAL_REF = "spatial_ref";

    public final static String GEO_TRANSFORM = "GeoTransform";

    public final static String UNIQUE_TIME_ATTRIBUTE = "uniqueTimeAttribute";

    final static Set<String> EXCLUDED_ATTRIBUTES = new HashSet<String>();

    /**
     * Global attribute for coordinate coverageDescriptorsCache.
     * 
     * @author Simone Giannecchini, GeoSolutions S.A.S.
     * 
     */
    public static enum Axis {
        X, Y, Z, T;

    }

    public static enum CheckType {
        NONE, UNSET, NOSCALARS, ONLYGEOGRIDS
    }

    /**
     * The dimension <strong>relative to the rank</strong> in {@link #variable} to use as image width. The actual dimension is
     * {@code variable.getRank() - X_DIMENSION}. Is hard-coded because the loop in the {@code read} method expects this order.
     */
    public static final int X_DIMENSION = 1;

    /**
     * The dimension <strong>relative to the rank</strong> in {@link #variable}
     * to use as image height. The actual dimension is
     * {@code variable.getRank() - Y_DIMENSION}. Is hard-coded because the loop
     * in the {@code read} method expects this order.
     */
    public static final int Y_DIMENSION = 2;

    /**
     * The default dimension <strong>relative to the rank</strong> in
     * {@link #variable} to use as Z dimension. The actual dimension is
     * {@code variable.getRank() - Z_DIMENSION}.
     * <p>
     */
    public static final int Z_DIMENSION = 3;

    /**
     * The data type to accept in images. Used for automatic detection of which
     * coverageDescriptorsCache to assign to images.
     */
    public static final Set<DataType> VALID_TYPES = new HashSet<DataType>(12);

    public static final String NC4_ERROR_MESSAGE = "Native NetCDF C library is not available. "
            + "Unable to handle NetCDF4 files on input/output."
            + "\nPlease make sure to add the paht of the Native NetCDF C libraries to the "
            + "PATH environment variable\n if you want to support NetCDF4-Classic files";

    static {
        String property = System.getProperty(CHECK_COORDINATE_PLUGINS_KEY);
        CHECK_COORDINATE_PLUGINS = Boolean.getBoolean(CHECK_COORDINATE_PLUGINS_KEY);
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("Value of Check Coordinate Plugins:" + property);
            LOGGER.info("Should check for coordinate handler plugins:" 
                + CHECK_COORDINATE_PLUGINS);
        }

        IGNORED_DIMENSIONS = initializeIgnoreSet();

        // Setting the LINUX Epoch as start time
        final GregorianCalendar calendar = new GregorianCalendar(1970, 00, 01, 00, 00, 00);
        UTC = TimeZone.getTimeZone("UTC");
        calendar.setTimeZone(UTC);
        START_TIME = calendar.getTimeInMillis();

        EXCLUDED_ATTRIBUTES.add(UNITS);
        EXCLUDED_ATTRIBUTES.add(LONG_NAME);
        EXCLUDED_ATTRIBUTES.add(DESCRIPTION);
        EXCLUDED_ATTRIBUTES.add(STANDARD_NAME);

        NetcdfDataset.setDefaultEnhanceMode(EnumSet.of(Enhance.CoordSystems));
        HashSet<String> unsupportedSet = new HashSet<String>();
        unsupportedSet.add("OSEQD");
        UNSUPPORTED_DIMENSIONS = Collections.unmodifiableSet(unsupportedSet);

        VALID_TYPES.add(DataType.BOOLEAN);
        VALID_TYPES.add(DataType.BYTE);
        VALID_TYPES.add(DataType.SHORT);
        VALID_TYPES.add(DataType.INT);
        VALID_TYPES.add(DataType.LONG);
        VALID_TYPES.add(DataType.FLOAT);
        VALID_TYPES.add(DataType.DOUBLE);

        // Didn't extracted to a separate method 
        // since we can't initialize the static fields
        final Object externalDir = System.getProperty(NETCDF_DATA_DIR);
        String finalDir = null;
        if (externalDir != null) {
            String dir = (String) externalDir;
            final File file = new File(dir);
            if (isValidDir(file)) {
                finalDir = dir;
            }
        }
        EXTERNAL_DATA_DIR = finalDir;

        try {
            Class.forName("ucar.nc2.grib.collection.GribIosp");
            Class.forName("org.geotools.coverage.io.grib.GribUtilities");
            IS_GRIB_AVAILABLE = true;
        } catch (ClassNotFoundException cnf) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("No Grib library found on classpath. GRIB will not be supported");
            }
            IS_GRIB_AVAILABLE = false;
        }

        IS_NC4_LIBRARY_AVAILABLE = Nc4Iosp.isClibraryPresent();
        if (!IS_NC4_LIBRARY_AVAILABLE && LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(NC4_ERROR_MESSAGE);
        }
    }

    static boolean isLatLon(String bandName) {
        return bandName.equalsIgnoreCase(LON) || bandName.equalsIgnoreCase(LAT);
    }

    private static Set<String> initializeIgnoreSet() {
        Set<CoordinateHandlerSpi> handlers = CoordinateHandlerFinder.getAvailableHandlers();
        Iterator<CoordinateHandlerSpi> iterator = handlers.iterator();
        Set<String> ignoredSet = new HashSet<String>();
        while (iterator.hasNext()) {
            CoordinateHandlerSpi handler = iterator.next();
            Set<String> ignored = handler.getIgnoreSet();
            if (ignored != null && !ignored.isEmpty()) {
                ignoredSet.addAll(ignored);
            }
        }
        if (!ignoredSet.isEmpty()) {
            return ignoredSet;
        }
        return new HashSet<>();
    }

    public static boolean isValidDir(File file) {
        String dir = file.getAbsolutePath();
        if (!file.exists()) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("The specified " + NETCDF_DATA_DIR + " property doesn't refer "
                        + "to an existing folder. Please check the path: " + dir);
            }
            return false;
        } else if (!file.isDirectory()) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("The specified " + NETCDF_DATA_DIR + " property doesn't refer "
                        + "to a directory. Please check the path: " + dir);
            }
            return false;
        } else if (!file.canWrite()) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("The specified " + NETCDF_DATA_DIR + " property refers to "
                        + "a directory which can't be written. Please check the path and"
                        + " the permissions for: " + dir);
            }
            return false;
        }
        return true;
    }

    /**
     * Get Z Dimension Lenght for standard CF variables
     * @param var
     * @return
     */
    public static int getZDimensionLength(Variable var) {
        final int rank = var.getRank();
        if (rank > 2) {
            return var.getDimension(rank - Z_DIMENSION).getLength();
        }
        // TODO: Should I avoid use this method in case of 2D Variables?
        return 0;
    }

    public static int getDimensionLength(Variable var, final int dimensionIndex) {
        return var.getDimension(dimensionIndex).getLength();
    }

    /**
     * Returns the data type which most closely represents the "raw" internal
     * data of the variable. This is the value returned by the default
     * implementation of {@link NetcdfImageReader#getRawDataType}.
     * 
     * @param variable
     *                The variable.
     * @return The data type, or {@link DataBuffer#TYPE_UNDEFINED} if unknown.
     * 
     * @see NetcdfImageReader#getRawDataType
     */
    public static int getRawDataType(final VariableIF variable) {
        VariableDS ds = (VariableDS) variable;
        final DataType type = ds.getOriginalDataType();
        return transcodeNetCDFDataType(type,variable.isUnsigned());
    }

    /**
     * Transcode a NetCDF data type into a java2D DataBuffer type.
     * 
     * @param type the {@link DataType} to transcode.
     * @param unsigned if the original data is unsigned or not
     * @return an int representing the correct DataBuffer type.
     */
    public static int transcodeNetCDFDataType(final DataType type, final boolean unsigned) {
        if (DataType.BOOLEAN.equals(type) || DataType.BYTE.equals(type)) {
            return DataBuffer.TYPE_BYTE;
        }
        if (DataType.CHAR.equals(type)) {
            return DataBuffer.TYPE_USHORT;
        }
        if (DataType.SHORT.equals(type)) {
            return unsigned ? DataBuffer.TYPE_USHORT: DataBuffer.TYPE_SHORT;
        }
        if (DataType.INT.equals(type)) {
            return DataBuffer.TYPE_INT;
        }
        if (DataType.FLOAT.equals(type)) {
            return DataBuffer.TYPE_FLOAT;
        }
        if (DataType.LONG.equals(type) || DataType.DOUBLE.equals(type)) {
            return DataBuffer.TYPE_DOUBLE;
        }
        return DataBuffer.TYPE_UNDEFINED;
    }

    /**
     * NetCDF files may contains a wide set of coverageDescriptorsCache. Some of them are unuseful for our purposes. The method returns {@code true}
     * if the specified variable is accepted.
     */
    public static boolean isVariableAccepted( final Variable var, final CheckType checkType ) {
        return isVariableAccepted(var, checkType, null);
    }
    
    /**
     * NetCDF files may contains a wide set of coverageDescriptorsCache. Some of them are unuseful for our purposes. The method returns {@code true}
     * if the specified variable is accepted.
     */
    public static boolean isVariableAccepted( final Variable var, final CheckType checkType, final NetcdfDataset dataset ) {
        if (var instanceof CoordinateAxis1D) {
            return false;
        } else if (checkType == CheckType.NOSCALARS) {
            List<Dimension> dimensions = var.getDimensions();
            if (dimensions.size() < 2) {
                return false;
            }
            DataType dataType = var.getDataType();
            if (dataType == DataType.CHAR) {
                return false;
            }
            return isVariableAccepted(var.getFullName(), CheckType.NONE);
        } else if (checkType == CheckType.ONLYGEOGRIDS) {
            List<Dimension> dimensions = var.getDimensions();
            if (dimensions.size() < 2) {
                return false;
            }
            int twoDimensionalCoordinates = 0;
            for (Dimension dimension : dimensions) {
                String dimName = dimension.getFullName();
                // check the dimension to be defined
                Group group = dimension.getGroup();
                // Simple check if the group is not present. In that case false is returned.
                // This situation could happen with anonymous dimensions inside variables which
                // indicates the bounds of another variable. These kind of variable are not useful
                // for displaying the final raster.
                if (group == null) {
                    return false;
                }
                if (IGNORED_DIMENSIONS.contains(dimName)) {
                    continue;
                }
                Variable dimVariable = group.findVariable(dimName);
                if (dimVariable == null && dataset != null) {
                    //fallback on coordinates attribute for auxiliary coordinates.
                    dimVariable = getAuxiliaryCoordinate(dataset, group, var, dimName);
                }
                if (dimVariable instanceof CoordinateAxis1D) {
                    CoordinateAxis1D axis = (CoordinateAxis1D) dimVariable;
                    AxisType axisType = axis.getAxisType();
                    if (axisType == null) {
                        return false;
                    }
                    switch (axisType) {
                    case GeoX: 
                    case GeoY:
                    case Lat:
                    case Lon:
                        twoDimensionalCoordinates++;
                        break;
                    default:
                        break;
                    }
                }
            }
            if (twoDimensionalCoordinates < 2) {
                // 2D Grid is missing
                return false;
            }

            DataType dataType = var.getDataType();
            if (dataType == DataType.CHAR) {
                return false;
            }
            return isVariableAccepted(var.getFullName(), CheckType.NONE);
        } else {
            return isVariableAccepted(var.getFullName(), checkType);
        }
    }

    private static Variable getAuxiliaryCoordinate(NetcdfDataset dataset, Group group,
            Variable var, String dimName) {
        Variable coordinateVariable = null;
        Attribute attribute = var.findAttribute(NetCDFUtilities.COORDINATES);
        if (attribute != null) {
            String coordinates = attribute.getStringValue();
            String [] coords = coordinates.split(" ");
            for (String coord: coords) {
                Variable coordVar = dataset.findVariable(group, coord);
                List<Dimension> varDimensions = coordVar.getDimensions();
                if (varDimensions != null && varDimensions.size() == 1 && varDimensions.get(0).getFullName().equalsIgnoreCase(dimName)) {
                    coordinateVariable = coordVar;
                    break;
                }
            }
        }
        return coordinateVariable;
    }

    /**
     * NetCDF files may contain a wide set of coverageDescriptorsCache. Some of them are
     * unuseful for our purposes. The method returns {@code true} if the
     * specified variable is accepted.
     */
    public static boolean isVariableAccepted(final String name,
            final CheckType checkType) {
        if (checkType == CheckType.NONE) {
            return true;
        } else if (name.equalsIgnoreCase(LATITUDE)
                || name.equalsIgnoreCase(LONGITUDE)
                || name.equalsIgnoreCase(LON)
                || name.equalsIgnoreCase(LAT)
                || name.equalsIgnoreCase(TIME)
                || name.equalsIgnoreCase(DEPTH)
                || name.equalsIgnoreCase(ZETA)
                || name.equalsIgnoreCase(HEIGHT)
                || name.toLowerCase().contains(COORDSYS.toLowerCase())
                || name.endsWith(BOUNDS)
                || name.endsWith(BNDS)
                || UNSUPPORTED_DIMENSIONS.contains(name)
                ) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns a {@code NetcdfDataset} given an input object
     * 
     * @param input
     *                the input object (usually a {@code File}, a
     *                {@code String} or a {@code FileImageInputStreamExt).
     * @return {@code NetcdfDataset} in case of success.
     * @throws IOException
     *                 if some error occur while opening the dataset.
     * @throws {@link IllegalArgumentException}
     *                 in case the specified input is a directory
     */
    public static NetcdfDataset getDataset(Object input) throws IOException {
        NetcdfDataset dataset = null;
        if (input instanceof File) {
            final File file= (File) input;
            if (!file.isDirectory()) {
                dataset = NetcdfDataset.acquireDataset(file.getPath(), null);
            } else {
                throw new IllegalArgumentException("Error occurred during NetCDF file reading: The input file is a Directory.");
            }
        } else if (input instanceof String) {
            File file = new File((String) input);
            if (!file.isDirectory()) {
                dataset = NetcdfDataset.acquireDataset(file.getPath(), null);
            } else {
                throw new IllegalArgumentException( "Error occurred during NetCDF file reading: The input file is a Directory.");
            }
        } else if (input instanceof URL) {
            final URL tempURL = (URL) input;
            String protocol = tempURL.getProtocol();
            if (protocol.equalsIgnoreCase("file")) {
                File file = ImageIOUtilities.urlToFile(tempURL);
                if (!file.isDirectory()) {
                    dataset = NetcdfDataset.acquireDataset(file.getPath(), null);
                } else {
                    throw new IllegalArgumentException( "Error occurred during NetCDF file reading: The input file is a Directory.");
                }
            } else if (protocol.equalsIgnoreCase("http") || protocol.equalsIgnoreCase("dods")) {
                dataset = NetcdfDataset.acquireDataset(tempURL.toExternalForm(), null);
            }
        } else if (input instanceof URIImageInputStream) {
            final URIImageInputStream uriInStream = (URIImageInputStream) input;
            dataset = NetcdfDataset.acquireDataset(uriInStream.getUri().toString(), null);
        } else if (input instanceof AccessibleStream) {
            final AccessibleStream<?> stream= (AccessibleStream<?>) input;
            if (stream.getBinding().isAssignableFrom(File.class)) {
                final File file = ((AccessibleStream<File>) input).getTarget();
                if (!file.isDirectory()) {
                    dataset = NetcdfDataset.acquireDataset(file.getPath(), null);
                }
            } else {
                throw new IllegalArgumentException("Error occurred during NetCDF file reading: The input file is a Directory.");
            }
        }
        return dataset;
    }

    /**
     * Checks if the input is file based, and if yes, returns the file. 
     * 
     * @param input the input to check.
     * @return the file or <code>null</code> if it is not file based.
     * @throws IOException
     */
    public static File getFile (Object input) throws IOException {
        File guessedFile = null;
        if (input instanceof File) {
            guessedFile = (File) input;
        } else if (input instanceof String) {
            guessedFile = new File((String) input);
        } else if (input instanceof URL) {
            final URL tempURL = (URL) input;
            String protocol = tempURL.getProtocol();
            if (protocol.equalsIgnoreCase("file")) {
                guessedFile = ImageIOUtilities.urlToFile(tempURL);
            }
        } else if (input instanceof URIImageInputStream) {
            final URIImageInputStream uriInStream = (URIImageInputStream) input;
            String uri = uriInStream.getUri().toString();
            guessedFile = new File(uri);
        } else if (input instanceof AccessibleStream) {
            final AccessibleStream<?> stream= (AccessibleStream<?>) input;
            if(stream.getBinding().isAssignableFrom(File.class)){
                guessedFile = ((AccessibleStream<File>) input).getTarget();
            } 
        }
        // check 
        if (guessedFile.exists() && !guessedFile.isDirectory()) {
            return guessedFile;
        }
        return null;
    }

    /**
     * Returns a format to use for parsing values along the specified axis type.
     * This method is invoked when parsing the date part of axis units like "<cite>days
     * since 1990-01-01 00:00:00</cite>". Subclasses should override this
     * method if the date part is formatted in a different way. The default
     * implementation returns the following formats:
     * <p>
     * <ul>
     * <li>For {@linkplain AxisType#Time time axis}, a {@link DateFormat}
     * using the {@code "yyyy-MM-dd HH:mm:ss"} pattern in UTC
     * {@linkplain TimeZone timezone}.</li>
     * <li>For all other kind of axis, a {@link NumberFormat}.</li>
     * </ul>
     * <p>
     * The {@linkplain Locale#CANADA Canada locale} is used by default for most
     * formats because it is relatively close to ISO (for example regarding days
     * and months order in dates) while using the English symbols.
     * 
     * @param type
     *                The type of the axis.
     * @param prototype
     *                An example of the values to be parsed. Implementations may
     *                parse this prototype when the axis type alone is not
     *                sufficient. For example the {@linkplain AxisType#Time time
     *                axis type} should uses the {@code "yyyy-MM-dd"} date
     *                pattern, but some files do not follow this convention and
     *                use the default local instead.
     * @return The format for parsing values along the axis.
     */
    public static Format getAxisFormat(final AxisType type,
            final String prototype) {
        if (!type.equals(AxisType.Time)) {
            return NumberFormat.getNumberInstance(Locale.CANADA);
        }
        char dateSeparator = '-'; // The separator used in ISO format.
        boolean yearLast = false; // Year is first in ISO pattern.
        boolean namedMonth = false; // Months are numbers in the ISO pattern.
        boolean addT = false;
        boolean appendZ = false; 
        int dateLength = 0;
        if (prototype != null) {
            /*
             * Performs a quick check on the prototype content. If the prototype
             * seems to use a different date separator than the ISO one, we will
             * adjust the pattern accordingly. Also checks if the year seems to
             * appears last rather than first, and if the month seems to be
             * written using letters rather than digits.
             */
            int field = 1;
            int digitCount = 0;

            final int length = prototype.length();
            for (int i = 0; i < length; i++) {
                final char c = prototype.charAt(i);
                if (Character.isWhitespace(c)) {
                    break; // Checks only the dates, ignore the hours.
                }
                if (Character.isDigit(c)) {
                    digitCount++;
                    dateLength++;
                    continue; // Digits are legal in all cases.
                }
                if (field == 2 && Character.isLetter(c)) {
                    namedMonth = true;
                    continue; // Letters are legal for month only.
                }
                if (field == 1) {
                    dateSeparator = c;
                    dateLength++;
                }
                if (c=='T')
                	addT = true;
                if (c=='Z' && i==length-1)
                	appendZ = true;
                digitCount = 0;
                field++;
            }
            if (digitCount >= 4) {
                yearLast = true;
            }
        }
        String pattern = null;
        if (yearLast) {
            pattern = namedMonth ? "dd-MMM-yyyy" : "dd-MM-yyyy";
        } else {
            pattern = namedMonth ? "yyyy-MMM-dd" : "yyyy-MM-dd";
            if (dateLength < 10) {
                // case of truncated date
                pattern = pattern.substring(0, dateLength);
            }
        }
        pattern = pattern.replace('-', dateSeparator);
        int lastColon = prototype.lastIndexOf(":"); //$NON-NLS-1$
        if (lastColon != -1) {
            pattern += addT ? "'T'" : " ";
            pattern += prototype != null && lastColon >= 16 ? "HH:mm:ss" : "HH:mm";
        }
        //TODO: Improve me:
        //Handle timeZone
        pattern += appendZ ? "'Z'" : "";
        final DateFormat format = new SimpleDateFormat(pattern, Locale.CANADA);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }

    /**
     * Depending on the type of model/netcdf file, we will check for the
     * presence of some coverageDescriptorsCache rather than some others. The method returns
     * the type of check on which we need to leverage to restrict the set of
     * interesting coverageDescriptorsCache. The method will check for some
     * KEY/FLAGS/ATTRIBUTES within the input dataset in order to define the
     * proper check type to be performed.
     * 
     * @param dataset
     *                the input dataset.
     * @return the proper {@link CheckType} to be performed on the specified
     *         dataset.
     */
    public static CheckType getCheckType(NetcdfDataset dataset) {
        CheckType ct = CheckType.UNSET;
        if (dataset != null) {
            ct = CheckType.ONLYGEOGRIDS;
        }
        return ct;
    }

    /**
     * @param schemaDef
     * @param crs
     * @return
     */
    public static SimpleFeatureType createFeatureType(String schemaName,String schemaDef, CoordinateReferenceSystem crs) {
        SimpleFeatureType indexSchema=null;
        if (schemaDef == null) {
            throw new IllegalArgumentException("Unable to create feature type from null definition!");
        }
        schemaDef = schemaDef.trim();
        // get the schema
        try {
            indexSchema = DataUtilities.createType(schemaName, schemaDef);
            indexSchema = DataUtilities.createSubType(indexSchema, DataUtilities.attributeNames(indexSchema), crs);
        } catch (Throwable e) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            indexSchema = null;
        }
        return indexSchema;
    }

    /**
     * @return true if the GRIB library is available
     */
    public static boolean isGribAvailable() {
        return IS_GRIB_AVAILABLE;
    }

    /**
     * @return true if the C Native NetCDF 4 library is available
     */
    public static boolean isNC4CAvailable() {
        return IS_NC4_LIBRARY_AVAILABLE;
    }

    public static boolean isCheckCoordinatePlugins() {
        return CHECK_COORDINATE_PLUGINS;
    }

    /**
     * @return An unmodifiable Set of Unsupported Dimension names
     */
    public static Set<String> getUnsupportedDimensions() {
        return UNSUPPORTED_DIMENSIONS;
    }

    /** 
     * @return an unmodifiable Set of the Dimensions to be ignored by the 
     * Coordinate parsing machinery
     */
    public static Set<String> getIgnoredDimensions() {
        return Collections.unmodifiableSet(IGNORED_DIMENSIONS);
    }

    /**
     * Adds a dimension to the ignored dimensions set.
     */
    public static void addIgnoredDimension(String dimensionName) {
        IGNORED_DIMENSIONS.add(dimensionName);
    }

    /**
     * Utility method for getting NoData from an input {@link Variable}
     * 
     * @param var Variable instance
     * @return a Number representing NoData
     */
    public static Number getNodata(Variable var) {
        if (var != null) {
            // Getting all the Variable attributes
            List<Attribute> attributes = var.getAttributes();
            String fullName;
            // Searching for FILL_VALUE or MISSING_VALUE attributes
            for (Attribute attribute : attributes) {
                fullName = attribute.getFullName();
                if (fullName.equalsIgnoreCase(FILL_VALUE)
                        || fullName.equalsIgnoreCase(MISSING_VALUE)) {
                    return attribute.getNumericValue();
                }
            }
        }
        return null;
    }

    /** 
     * Return the propery NetCDF dataType for the input datatype class
     * 
     * @param classDataType
     * @return
     */
    public static DataType getNetCDFDataType(String classDataType) {
        if (isATime(classDataType)) {
            return DataType.DOUBLE;
        } else if (classDataType.endsWith("Integer")) {
            return DataType.INT;
        } else if (classDataType.endsWith("Double")) {
            return DataType.DOUBLE;
        } else if (classDataType.endsWith("String")) {
            return DataType.STRING;
        }
        return DataType.STRING;
    }

    /**
     * Transcode a DataBuffer type into a NetCDF DataType .
     * 
     * @param type the beam {@link ProductData} type to transcode.
     * @return an NetCDF DataType type.
     */
    public static DataType transcodeImageDataType(final int dataType) {
        switch (dataType) {
        case DataBuffer.TYPE_BYTE:
            return DataType.BYTE;
        case DataBuffer.TYPE_SHORT:
            return DataType.SHORT;
        case DataBuffer.TYPE_INT:
            return DataType.INT;
        case DataBuffer.TYPE_DOUBLE:
            return DataType.DOUBLE;
        case DataBuffer.TYPE_FLOAT:
            return DataType.FLOAT;
        case DataBuffer.TYPE_UNDEFINED:
        default:
            throw new IllegalArgumentException("Invalid input data type:" + dataType);
        }
    }

    /** 
     * Return true in case that dataType refers to something which need to be handled 
     * as a Time (TimeStamp, Date)
     * @param classDataType
     * @return
     */
    public final static boolean isATime(String classDataType) {
        return (classDataType.endsWith("Timestamp") || classDataType.endsWith("Date"));
    }

    /**
     * Get an Array of proper size and type.
     * 
     * @param dimensions the dimensions
     * @param varDataType the DataType of the required array 
     * @return
     */
    public static Array getArray(int[] dimensions, DataType varDataType) {
        if (dimensions == null)
            throw new IllegalArgumentException("Illegal dimensions");
        final int nDims = dimensions.length;
        switch (nDims) {
        case 6:
            // 6D Arrays
            if (varDataType == DataType.FLOAT) {
                return new ArrayFloat.D6(dimensions[0], dimensions[1],
                        dimensions[2], dimensions[3], dimensions[4], dimensions[5]);
            } else if (varDataType == DataType.DOUBLE) {
                return new ArrayDouble.D6(dimensions[0], dimensions[1],
                        dimensions[2], dimensions[3], dimensions[4], dimensions[5]);
            } else if (varDataType == DataType.BYTE) {
                return new ArrayByte.D6(dimensions[0], dimensions[1],
                        dimensions[2], dimensions[3], dimensions[4], dimensions[5]);
            } else if (varDataType == DataType.SHORT) {
                return new ArrayShort.D6(dimensions[0], dimensions[1],
                        dimensions[2], dimensions[3], dimensions[4], dimensions[5]);
            } else if (varDataType == DataType.INT) {
                return new ArrayInt.D6(dimensions[0], dimensions[1],
                        dimensions[2], dimensions[3], dimensions[4], dimensions[5]);
            } else
                throw new IllegalArgumentException("unsupported Datatype");
        case 5:
            // 5D Arrays
            if (varDataType == DataType.FLOAT) {
                return new ArrayFloat.D5(dimensions[0], dimensions[1],
                        dimensions[2], dimensions[3], dimensions[4]);
            } else if (varDataType == DataType.DOUBLE) {
                return new ArrayDouble.D5(dimensions[0], dimensions[1],
                        dimensions[2], dimensions[3], dimensions[4]);
            } else if (varDataType == DataType.BYTE) {
                return new ArrayByte.D5(dimensions[0], dimensions[1],
                        dimensions[2], dimensions[3], dimensions[4]);
            } else if (varDataType == DataType.SHORT) {
                return new ArrayShort.D5(dimensions[0], dimensions[1],
                        dimensions[2], dimensions[3], dimensions[4]);
            } else if (varDataType == DataType.INT) {
                return new ArrayInt.D5(dimensions[0], dimensions[1],
                        dimensions[2], dimensions[3], dimensions[4]);
            } else
                throw new IllegalArgumentException("unsupported Datatype");
        case 4:
            // 4D Arrays
            if (varDataType == DataType.FLOAT) {
                return new ArrayFloat.D4(dimensions[0], dimensions[1],
                        dimensions[2], dimensions[3]);
            } else if (varDataType == DataType.DOUBLE) {
                return new ArrayDouble.D4(dimensions[0], dimensions[1],
                        dimensions[2], dimensions[3]);
            } else if (varDataType == DataType.BYTE) {
                return new ArrayByte.D4(dimensions[0], dimensions[1],
                        dimensions[2], dimensions[3]);
            } else if (varDataType == DataType.SHORT) {
                return new ArrayShort.D4(dimensions[0], dimensions[1],
                        dimensions[2], dimensions[3]);
            } else if (varDataType == DataType.INT) {
                return new ArrayInt.D4(dimensions[0], dimensions[1],
                        dimensions[2], dimensions[3]);
            } else
                throw new IllegalArgumentException("unsupported Datatype");
        case 3:
            // 3D Arrays
            if (varDataType == DataType.FLOAT) {
                return new ArrayFloat.D3(dimensions[0], dimensions[1],
                        dimensions[2]);
            } else if (varDataType == DataType.DOUBLE) {
                return new ArrayDouble.D3(dimensions[0], dimensions[1],
                        dimensions[2]);
            } else if (varDataType == DataType.BYTE) {
                return new ArrayByte.D3(dimensions[0], dimensions[1],
                        dimensions[2]);
            } else if (varDataType == DataType.SHORT) {
                return new ArrayShort.D3(dimensions[0], dimensions[1],
                        dimensions[2]);
            } else if (varDataType == DataType.INT) {
                return new ArrayInt.D3(dimensions[0], dimensions[1],
                        dimensions[2]);
            } else
                throw new IllegalArgumentException("unsupported Datatype");
        case 2:
            // 2D Arrays
            if (varDataType == DataType.FLOAT) {
                return new ArrayFloat.D2(dimensions[0], dimensions[1]);
            } else if (varDataType == DataType.DOUBLE) {
                return new ArrayDouble.D2(dimensions[0], dimensions[1]);
            } else if (varDataType == DataType.BYTE) {
                return new ArrayByte.D2(dimensions[0], dimensions[1]);
            } else if (varDataType == DataType.SHORT) {
                return new ArrayShort.D2(dimensions[0], dimensions[1]);
            } else if (varDataType == DataType.INT) {
                return new ArrayInt.D2(dimensions[0], dimensions[1]);
            } else
                throw new IllegalArgumentException("unsupported Datatype");
        case 1:
            // 1D Arrays
            if (varDataType == DataType.FLOAT) {
                return new ArrayFloat.D1(dimensions[0]);
            } else if (varDataType == DataType.DOUBLE) {
                return new ArrayDouble.D1(dimensions[0]);
            } else if (varDataType == DataType.BYTE) {
                return new ArrayByte.D1(dimensions[0]);
            } else if (varDataType == DataType.SHORT) {
                return new ArrayShort.D1(dimensions[0]);
            } else if (varDataType == DataType.INT) {
                return new ArrayInt.D1(dimensions[0]);
            } else
                throw new IllegalArgumentException("unsupported Datatype");
        }
        throw new IllegalArgumentException("Unable to create a proper array unsupported Datatype");
    }

    /**
     * Transcode a NetCDF Number into a proper Number instance.
     * 
     * @param type the {@link DataType} to transcode.
     * @return the proper number instance
     */
    public static Number transcodeNumber(final DataType type, Number value) {
        if (DataType.DOUBLE.equals(type)) {
            return Double.valueOf(value.doubleValue());
        } else if (DataType.FLOAT.equals(type)) {
            return Float.valueOf(value.floatValue());
        } else if (DataType.LONG.equals(type)) {
            return Long.valueOf(value.longValue());
        } else if (DataType.INT.equals(type)) {
            return Integer.valueOf(value.intValue());
        } else if (DataType.SHORT.equals(type)) {
            return Short.valueOf(value.shortValue());
        } else if (DataType.BYTE.equals(type)) {
            return Byte.valueOf(value.byteValue());
        }
        throw new IllegalArgumentException("Unsupported type or value: type = " +
        type.toString() + " value = " + value);
    }
}