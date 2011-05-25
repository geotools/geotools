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
 */
package org.geotools.referencing.operation.transform;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URL;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.StringTokenizer;
import java.util.prefs.Preferences;

import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.Transformation;
import org.opengis.referencing.operation.TransformException;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.Parameter;
import org.geotools.parameter.ParameterGroup;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.operation.MathTransformProvider;
import org.geotools.referencing.operation.builder.LocalizationGrid;
import org.geotools.resources.Arguments;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;


/**
 * Transform backed by the North American Datum Conversion grid.
 * The North American Datum Conversion (NADCON) Transform (EPSG code 9613) is a
 * two dimentional datum shift method, created by the National Geodetic Survey
 * (NGS), that uses interpolated values from two grid shift files. This
 * method is used to transform NAD27 (EPSG code 4267) datum coordinates (and
 * some others) to NAD83 (EPSG code 4267) within the United States. There are
 * two set of grid shift files: NADCON and High Accuracy Reference Networks
 * (HARN).  NADCON shfts from NAD27 (and some others) to NAD83 while HARN
 * shifts from the NADCON NAD83 to an improved NAD83. Both sets of grid shift
 * files may be downloaded from
 * <a href="http://www.ngs.noaa.gov/PC_PROD/NADCON/">www.ngs.noaa.gov/PC_PROD/NADCON/</a>.
 * <p>
 *
 * Some of the NADCON grids, their areas of use, and source datums are shown
 * in the following table.
 * <p>
 *
 * <table>
 *   <tr><th>Shift File Name</td><th>Area</td><th>Source Datum</td><th>Accuracy at 67% confidence (m)</td></tr>
 *   <tr><td>CONUS</td><td>Conterminous U S (lower 48 states)</td><td>NAD27</td><td>0.15</td></tr>
 *   <tr><td>ALASKA</td><td>Alaska, incl. Aleutian Islands</td><td>NAD27</td><td>0.5</td></tr>
 *   <tr><td>HAWAII</td><td>Hawaiian Islands</td><td>Old Hawaiian (4135)</td><td>0.2</td></tr>
 *   <tr><td>STLRNC</td><td>St. Lawrence Is., AK</td><td>St. Lawrence Island (4136)</td><td>--</td></tr>
 *   <tr><td>STPAUL </td><td>St. Paul Is., AK</td><td>St. Paul Island (4137)</td><td>--</td></tr>
 *   <tr><td>STGEORGE</td><td>St. George Is., AK</td><td>St. George Island (4138)</td><td>--</td></tr>
 *   <tr><td>PRVI</td><td>Puerto Rico and the Virgin Islands</td><td>Puerto Rico (4139)</td><td>0.05</td></tr>
 * </table>
 * <p>
 *
 * Grid shift files come in two formats: binary and text. The files from the NGS are
 * binary and have {@code .las} (latitude shift) and {@code .los} (longitude shift)
 * extentions. Text grids may be created with the <cite>NGS nadgrd</cite> program and have
 * {@code .laa} (latitude shift) and {@code .loa} (longitude shift) file extentions.
 * Both types of  files may be used here.
 * <p>
 *
 * The grid names to use for transforming are parameters of this
 * {@link MathTransform}.  This parameter may be the full name and path to the grids
 * or just the name of the grids if the default location of the grids was set
 * as a preference.  This preference may be set with the main method of this
 * class.
 * <p>
 *
 * Transformations here have been tested to be within 0.00001 seconds of
 * values given by the <cite>NGS ndcon210</cite> program for NADCON grids. American Samoa
 * and HARN shifts have not yet been tested.  <strong>References:</strong>
 *
 * <ul>
 *   <li><a href="http://www.ngs.noaa.gov/PC_PROD/NADCON/Readme.htm">NADCONreadme</a></li>
 *   <li>American Samoa Grids for NADCON - Samoa_Readme.txt</li>
 *   <li><a href="http://www.ngs.noaa.gov/PUBS_LIB/NGS50.pdf">NADCON - The
 *       Application of Minimum-Curvature-Derived  Surfaces in the Transformation of
 *       Positional Data From the North American  Datum of 1927 to the North
 *       American Datum of 1983</a> - NOAA TM.</li>
 *   <li>{@code ndcon210.for} - NGS fortran source code for NADCON conversions. See the
 *       following subroutines: TRANSF, TO83, FGRID, INTRP, COEFF and SURF</li>
 *   <li>{@code nadgrd.for} - NGS fortran source code to export/import binary and text grid
 *       formats</li>
 *   <li>EPSG Geodesy Parameters database version 6.5</li>
 * </ul>
 *
 * @see <a href="http://www.ngs.noaa.gov/TOOLS/Nadcon/Nadcon.html"> NADCON -
 *      North American Datum Conversion Utility</a>
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Rueben Schulz
 *
 * @todo the transform code does not deal with the case where grids cross +- 180 degrees.
 */
public class NADCONTransform extends AbstractMathTransform implements MathTransform2D, Serializable {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -4707304160205218546L;

    /**
     * Preference node for the grid shift file location.
     */
    private static final String GRID_LOCATION = "Grid location";

    /**
     * The default value for the grid shift file location.
     */
    private static final String DEFAULT_GRID_LOCATION = ".";

    /**
     * Difference allowed in iterative computations. This is half the value
     * used in the NGS fortran code (so all tests pass).
     */
    private static final double TOL = 5.0E-10;

    /**
     * Maximum number of iterations for iterative computations.
     */
    private static final int MAX_ITER = 10;

    /**
     * Conversion factor from seconds to decimal degrees.
     */
    private static final double SEC_2_DEG = 3600.0;

    /**
     * Latitude grid shift file names. Output in WKT.
     */
    private final String latGridName;

    /**
     * Longitude grid shift file names. Output in WKT.
     */
    private final String longGridName;

    /**
     * The minimum longitude value covered by this grid (decimal degrees)
     */
    private double xmin;

    /**
     * The minimum latitude value covered by this grid (decimal degrees)
     */
    private double ymin;

    /**
     * The maximum longitude value covered by this grid (decimal degrees)
     */
    private double xmax;

    /**
     * The maximum latitude value covered by this grid (decimal degrees)
     */
    private double ymax;

    /**
     * The difference between longitude grid points (decimal degrees)
     */
    private double dx;

    /**
     * The difference between latitude grid points (decimal degrees)
     */
    private double dy;

    /**
     * Longitude and latitude grid shift values. Values are organized from low
     * to high longitude (low x index to high) and low to high latitude (low y
     * index to high).
     */
    private LocalizationGrid gridShift;

    /**
     * The {@link #gridShift} values as a {@code LocalizationGridTransform2D}.
     * Used for interpolating shift values.
     */
    private MathTransform gridShiftTransform;

    /**
     * The inverse of this transform. Will be created only when needed.
     */
    private transient MathTransform2D inverse;

    /**
     * Constructs a {@code NADCONTransform} from the specified grid shift files.
     *
     * @param latGridName path and name (or just name if {@link #GRID_LOCATION}
     *        is set) to the latitude difference file. This will have a {@code .las} or
     *        {@code .laa} file extention.
     * @param longGridName path and name (or just name if {@link #GRID_LOCATION}
     *        is set) to the longitude difference file. This will have a {@code .los}
     *        or {@code .loa} file extention.
     *
     * @throws ParameterNotFoundException if a math transform parameter cannot be found.
     * @throws FactoryException if there is a problem creating this math transform
     *         (ie file extentions are unknown or there is an error reading the
     *          grid files)
     */
    public NADCONTransform(final String latGridName, final String longGridName)
            throws ParameterNotFoundException, FactoryException
    {
        this.latGridName  = latGridName;
        this.longGridName = longGridName;

        //decide if text or binary grid will be used
        try {
            final URL latGridURL  = makeURL(latGridName);
            final URL longGridURL = makeURL(longGridName);

            if ((latGridName.endsWith(".las") && longGridName.endsWith(".los"))
                    || (latGridName.endsWith(".LAS") && longGridName.endsWith(".LOS"))) {
                loadBinaryGrid(latGridURL, longGridURL);
            } else if ((latGridName.endsWith(".laa") && longGridName.endsWith(".loa"))
                    || (latGridName.endsWith(".LAA") && longGridName.endsWith(".LOA"))) {
                loadTextGrid(latGridURL, longGridURL);
            } else {
                throw new FactoryException(Errors.format(ErrorKeys.UNSUPPORTED_FILE_TYPE_$2,
                        latGridName.substring(latGridName.lastIndexOf('.') + 1),
                        longGridName.substring(longGridName.lastIndexOf('.') + 1)));
                // Note: the +1 above hide the dot, but also make sure that the code is
                //       valid even if the path do not contains '.' at all (-1 + 1 == 0).
            }

            gridShiftTransform = gridShift.getMathTransform();
        } catch (IOException exception) {
            final Throwable cause = exception.getCause();
            if (cause instanceof FactoryException) {
                throw (FactoryException) cause;
            }
            throw new FactoryException(exception.getLocalizedMessage(),
                exception);
        }
    }

    /**
     * Returns the parameter descriptors for this math transform.
     */
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
    }

    /**
     * Returns the parameter values for this math transform.
     *
     * @return A copy of the parameter values for this math transform.
     */
    @Override
    public ParameterValueGroup getParameterValues() {
        final ParameterValue lat_diff_file = new Parameter(Provider.LAT_DIFF_FILE);
        lat_diff_file.setValue(latGridName);

        final ParameterValue long_diff_file = new Parameter(Provider.LONG_DIFF_FILE);
        long_diff_file.setValue(longGridName);

        return new ParameterGroup(getParameterDescriptors(),
            new GeneralParameterValue[] { lat_diff_file, long_diff_file }
        );
    }

    /**
     * Gets the dimension of input points (always 2).
     *
     * @return the source dimensions.
     */
    public int getSourceDimensions() {
        return 2;
    }

    /**
     * Gets the dimension of output points (always 2).
     *
     * @return the target dimensions.
     */
    public int getTargetDimensions() {
        return 2;
    }

    /**
     * Returns a URL from the string representation. If the string has no
     * path, the default path preferece is added.
     *
     * @param str a string representation of a URL
     * @return a URL created from the string representation
     * @throws MalformedURLException if the URL cannot be created
     */
    private URL makeURL(final String str) throws MalformedURLException {
        //has '/' or '\' or ':', so probably full path to file
        if ((str.indexOf('\\') >= 0) || (str.indexOf('/') >= 0) || (str.indexOf(':') >= 0)) {
            return makeURLfromString(str);
        } else {
            // just a file name , prepend base location
            final Preferences prefs = Preferences.userNodeForPackage(NADCONTransform.class);
            final String baseLocation = prefs.get(GRID_LOCATION, DEFAULT_GRID_LOCATION);
            return makeURLfromString(baseLocation + "/" + str);
        }
    }

    /**
     * Returns a URL based on a string representation. If no protocol is given,
     * it is assumed to be a local file.
     *
     * @param str a string representation of a URL
     * @return a URL created from the string representation
     * @throws MalformedURLException if the URL cannot be created
     */
    private URL makeURLfromString(final String str) throws MalformedURLException {
        try {
            return new URL(str);
        } catch (MalformedURLException e) {
            //try making this with a file protocal
            return new URL("file", "", str);
        }
    }

    /**
     * Reads latitude and longitude binary grid shift file data into {@link
     * grid}.  The file is organized into records, with the first record
     * containing the  header information, followed by the shift data. The
     * header values are: text describing grid (64 bytes), num. columns (int),
     * num. rows (int),  num. z (int), min x (float), delta x (float), min y
     * (float), delta y (float)  and angle (float). Each record is num.
     * columns  4 bytes + 4 byte separator long and the file contains num.
     * rows + 1 (for the header) records. The data records (with the grid
     * shift values) are all floats and have a 4 byte  separator (0's) before
     * the data. Row records are organized from low  y (latitude) to high and
     * columns are orderd from low longitude to high.  Everything is written
     * in low byte order.
     *
     * @param latGridUrl URL to the binary latitude shift file (.las extention).
     * @param longGridUrl URL to the binary longitude shift file (.los extention).
     * @throws IOException if the data files cannot be read.
     * @throws FactoryException if there is an inconsistency in the data
     */
    private void loadBinaryGrid(final URL latGridUrl, final URL longGridUrl)
            throws IOException, FactoryException
    {
        final int HEADER_BYTES = 96;
        final int SEPARATOR_BYTES = 4;
        final int DESCRIPTION_LENGTH = 64;
        ReadableByteChannel latChannel;
        ReadableByteChannel longChannel;
        ByteBuffer latBuffer;
        ByteBuffer longBuffer;

        ////////////////////////
        //setup
        ////////////////////////
        latChannel = getReadChannel(latGridUrl);
        latBuffer = fillBuffer(latChannel, HEADER_BYTES);

        longChannel = getReadChannel(longGridUrl);
        longBuffer = fillBuffer(longChannel, HEADER_BYTES);

        ////////////////////////
        //read header info
        ////////////////////////
        //skip the header description
        latBuffer.position(latBuffer.position() + DESCRIPTION_LENGTH);

        int nc = latBuffer.getInt();
        int nr = latBuffer.getInt();
        int nz = latBuffer.getInt();

        xmin = latBuffer.getFloat();
        dx   = latBuffer.getFloat();
        ymin = latBuffer.getFloat();
        dy   = latBuffer.getFloat();

        float angle = latBuffer.getFloat();
        xmax = xmin + ((nc - 1) * dx);
        ymax = ymin + ((nr - 1) * dy);

        //skip the longitude header description
        longBuffer.position(longBuffer.position() + DESCRIPTION_LENGTH);

        //check that latitude grid header is the same as for latitude grid
        if (       (nc    != longBuffer.getInt())
                || (nr    != longBuffer.getInt())
                || (nz    != longBuffer.getInt())
                || (xmin  != longBuffer.getFloat())
                || (dx    != longBuffer.getFloat())
                || (ymin  != longBuffer.getFloat())
                || (dy    != longBuffer.getFloat())
                || (angle != longBuffer.getFloat())) {
            throw new FactoryException(Errors.format(ErrorKeys.GRID_LOCATIONS_UNEQUAL));
        }

        ////////////////////////
        //read grid shift data into LocalizationGrid
        ////////////////////////
        final int RECORD_LENGTH = (nc * 4) + SEPARATOR_BYTES;
        final int NUM_BYTES_LEFT = ((nr + 1) * RECORD_LENGTH) - HEADER_BYTES;
        final int START_OF_DATA = RECORD_LENGTH - HEADER_BYTES;

        latBuffer = fillBuffer(latChannel, NUM_BYTES_LEFT);
        latBuffer.position(START_OF_DATA); //start of second record (data)

        longBuffer = fillBuffer(longChannel, NUM_BYTES_LEFT);
        longBuffer.position(START_OF_DATA);

        gridShift = new LocalizationGrid(nc, nr);

        int i = 0;
        int j = 0;
        for (i = 0; i < nr; i++) {
            latBuffer.position(latBuffer.position() + SEPARATOR_BYTES); //skip record separator
            longBuffer.position(longBuffer.position() + SEPARATOR_BYTES);

            for (j = 0; j < nc; j++) {
                gridShift.setLocalizationPoint(j, i, longBuffer.getFloat(), latBuffer.getFloat());
            }
        }

        assert i == nr : i;
        assert j == nc : j;
    }

    /**
     * Returns a new bytebuffer, of numBytes length and little endian byte
     * order, filled from the channel.
     *
     * @param channel the channel to fill the buffer from
     * @param numBytes number of bytes to read
     * @return a new bytebuffer filled from the channel
     * @throws IOException if there is a problem reading the channel
     * @throws EOFException if the end of the channel is reached
     */
    private ByteBuffer fillBuffer(ReadableByteChannel channel, int numBytes)
        throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(numBytes);

        if (fill(buf, channel) == -1) {
            throw new EOFException(Errors.format(ErrorKeys.END_OF_DATA_FILE));
        }

        buf.flip();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        return buf;
    }

    /**
     * Fills the bytebuffer from the channel. Code was lifted from
     * ShapefileDataStore.
     *
     * @param buffer bytebuffer to fill from the channel
     * @param channel channel to fill the buffer from
     * @return number of bytes read
     * @throws IOException if there is a problem reading the channel
     */
    private int fill(ByteBuffer buffer, ReadableByteChannel channel)
        throws IOException {
        int r = buffer.remaining();

        // channel reads return -1 when EOF or other error
        // because they a non-blocking reads, 0 is a valid return value!!
        while ((buffer.remaining() > 0) && (r != -1)) {
            r = channel.read(buffer);
        }

        if (r == -1) {
            buffer.limit(buffer.position());
        }

        return r;
    }

    /**
     * Obtain a ReadableByteChannel from the given URL. If the url protocol is
     * file, a FileChannel will be returned. Otherwise a generic channel will
     * be obtained from the urls input stream. Code swiped from
     * ShapefileDataStore.
     *
     * @param url URL to create the channel from
     * @return a new PeadableByteChannel from the input url
     * @throws IOException if there is a problem creating the channel
     */
    private ReadableByteChannel getReadChannel(URL url)
        throws IOException {
        ReadableByteChannel channel = null;

        if (url.getProtocol().equals("file")) {
            File file = new File(url.getFile());

            if (!file.exists() || !file.canRead()) {
                throw new IOException(Errors.format(ErrorKeys.FILE_DOES_NOT_EXIST_$1, file));
            }

            FileInputStream in = new FileInputStream(file);
            channel = in.getChannel();
        } else {
            InputStream in = url.openConnection().getInputStream();
            channel = Channels.newChannel(in);
        }

        return channel;
    }

    /**
     * Reads latitude and longitude text grid shift file data into {@link
     * grid}.    The first two lines of the shift data file contain the
     * header, with the first being a description of the grid. The second line
     * contains 8 values separated by spaces: num. columns, num. rows, num. z,
     * min x, delta x, min y, delta y and angle. Shift data values follow this
     * and are also  separated by spaces. Row records are organized from low y
     * (latitude) to  high and columns are orderd from low longitude to high.
     *
     * @param latGridUrl URL to the text latitude shift file (.laa extention).
     * @param longGridUrl URL to the text longitude shift file (.loa
     *        extention).
     * @throws IOException if the data files cannot be read.
     * @throws FactoryException if there is an inconsistency in the data
     */
    private void loadTextGrid(URL latGridUrl, URL longGridUrl)
        throws IOException, FactoryException {
        String latLine;
        String longLine;
        StringTokenizer latSt;
        StringTokenizer longSt;

        ////////////////////////
        //setup
        ////////////////////////
        InputStreamReader latIsr = new InputStreamReader(latGridUrl.openStream());
        BufferedReader latBr = new BufferedReader(latIsr);

        InputStreamReader longIsr = new InputStreamReader(longGridUrl.openStream());
        BufferedReader longBr = new BufferedReader(longIsr);

        ////////////////////////
        //read header info
        ////////////////////////
        latLine = latBr.readLine(); //skip header description
        latLine = latBr.readLine();
        latSt = new StringTokenizer(latLine, " ");

        if (latSt.countTokens() != 8) {
            throw new FactoryException(Errors.format(ErrorKeys.HEADER_UNEXPECTED_LENGTH_$1,
                                       String.valueOf(latSt.countTokens())));
        }

        int nc = Integer.parseInt(latSt.nextToken());
        int nr = Integer.parseInt(latSt.nextToken());
        int nz = Integer.parseInt(latSt.nextToken());

        xmin = Float.parseFloat(latSt.nextToken());
        dx = Float.parseFloat(latSt.nextToken());
        ymin = Float.parseFloat(latSt.nextToken());
        dy = Float.parseFloat(latSt.nextToken());

        float angle = Float.parseFloat(latSt.nextToken());
        xmax = xmin + ((nc - 1) * dx);
        ymax = ymin + ((nr - 1) * dy);

        //now read long shift grid
        longLine = longBr.readLine(); //skip header description
        longLine = longBr.readLine();
        longSt = new StringTokenizer(longLine, " ");

        if (longSt.countTokens() != 8) {
            throw new FactoryException(Errors.format(ErrorKeys.HEADER_UNEXPECTED_LENGTH_$1,
                                       String.valueOf(longSt.countTokens())));
        }

        //check that latitude grid header is the same as for latitude grid
        if (       (nc    != Integer.parseInt(longSt.nextToken()))
                || (nr    != Integer.parseInt(longSt.nextToken()))
                || (nz    != Integer.parseInt(longSt.nextToken()))
                || (xmin  != Float.parseFloat(longSt.nextToken()))
                || (dx    != Float.parseFloat(longSt.nextToken()))
                || (ymin  != Float.parseFloat(longSt.nextToken()))
                || (dy    != Float.parseFloat(longSt.nextToken()))
                || (angle != Float.parseFloat(longSt.nextToken()))) {
            throw new FactoryException(Errors.format(ErrorKeys.GRID_LOCATIONS_UNEQUAL));
        }

        ////////////////////////
        //read grid shift data into LocalizationGrid
        ////////////////////////
        gridShift = new LocalizationGrid(nc, nr);

        int i = 0;
        int j = 0;
        for (i = 0; i < nr; i++) {
            for (j = 0; j < nc;) {
                latLine = latBr.readLine();
                latSt = new StringTokenizer(latLine, " ");
                longLine = longBr.readLine();
                longSt = new StringTokenizer(longLine, " ");

                while (latSt.hasMoreTokens() && longSt.hasMoreTokens()) {
                    gridShift.setLocalizationPoint(j, i,
                        (double) Float.parseFloat(longSt.nextToken()),
                        (double) Float.parseFloat(latSt.nextToken()));
                    ++j;
                }
            }
        }

        assert i == nr : i;
        assert j == nc : j;
    }

    /**
     * Transforms a list of coordinate point ordinal values. This method is
     * provided for efficiently transforming many points. The supplied array
     * of ordinal values will contain packed ordinal values.  For example, if
     * the source dimension is 3, then the ordinals will be packed in this
     * order:
     * (<var>x<sub>0</sub></var>,<var>y<sub>0</sub></var>,<var>z<sub>0</sub></var>,
     *
     * <var>x<sub>1</sub></var>,<var>y<sub>1</sub></var>,<var>z<sub>1</sub></var>
     * ...).  All input and output values are in decimal degrees.
     *
     * @param srcPts the array containing the source point coordinates.
     * @param srcOff the offset to the first point to be transformed in the
     *        source array.
     * @param dstPts the array into which the transformed point coordinates are
     *        returned. May be the same than {@code srcPts}.
     * @param dstOff the offset to the location of the first transformed point
     *        that is stored in the destination array.
     * @param numPts the number of point objects to be transformed.
     *
     * @throws TransformException if the input point is outside the area
     *         covered by this grid.
     */
    public void transform(final double[] srcPts, int srcOff,
        final double[] dstPts, int dstOff, int numPts)
        throws TransformException {
        int step = 0;

        if ((srcPts == dstPts) && (srcOff < dstOff)
                && ((srcOff + (numPts * getSourceDimensions())) > dstOff)) {
            step = -getSourceDimensions();
            srcOff -= ((numPts - 1) * step);
            dstOff -= ((numPts - 1) * step);
        }

        while (--numPts >= 0) {
            double x = srcPts[srcOff++];
            double y = srcPts[srcOff++];

            //check bounding box
//issue of bbox crossing +- 180 degrees (ie input point of -188 longitude);
//abs(x - xmin) > 0 , rollLongitude() ???
            if (((x < xmin) || (x > xmax)) || ((y < ymin) || (y > ymax))) {
                throw new TransformException(Errors.format(ErrorKeys.POINT_OUTSIDE_GRID));
            }

            //find the grid the point is in (index is 0 based)
            final double xgrid = (x - xmin) / dx;
            final double ygrid = (y - ymin) / dy;
            double[] array = new double[] { xgrid, ygrid };

            //use the LocalizationGridTransform2D transform method (bilineal interpolation)
            //returned shift values are in seconds, longitude shift values are + west
            gridShiftTransform.transform(array, 0, array, 0, 1);

            dstPts[dstOff++] = x - (array[0] / SEC_2_DEG);
            dstPts[dstOff++] = y + (array[1] / SEC_2_DEG);
            srcOff += step;
            dstOff += step;
        }
    }

    /**
     * Transforms nad83 values to nad27. Input and output values are in
     * decimal degrees.  This is done by itteratively finding a nad27 value that
     * shifts to the  input nad83 value. The input nad83 value is used as the
     * first  approximation.
     *
     * @param srcPts the array containing the source point coordinates.
     * @param srcOff the offset to the first point to be transformed in the
     *        source array.
     * @param dstPts the array into which the transformed point coordinates are
     *        returned. May be the same than {@code srcPts}.
     * @param dstOff the offset to the location of the first transformed point
     *        that is stored in the destination array.
     * @param numPts the number of point objects to be transformed.
     *
     * @throws TransformException if the input point is outside the area
     *         covered by this grid.
     */
    public void inverseTransform(final double[] srcPts, int srcOff,
        final double[] dstPts, int dstOff, int numPts)
        throws TransformException {
        int step = 0;

        if ((srcPts == dstPts) && (srcOff < dstOff)
                && ((srcOff + (numPts * getSourceDimensions())) > dstOff)) {
            step = -getSourceDimensions();
            srcOff -= ((numPts - 1) * step);
            dstOff -= ((numPts - 1) * step);
        }

        while (--numPts >= 0) {
            final double x = srcPts[srcOff++];
            final double y = srcPts[srcOff++];
            double xtemp = x;
            double ytemp = y;

            for (int i = MAX_ITER;;) {
                double[] array = { xtemp, ytemp };
                transform(array, 0, array, 0, 1);
                double xdif = array[0] - x;
                double ydif = array[1] - y;

                if (Math.abs(xdif) > TOL) {
                    xtemp = xtemp - xdif;
                }
                if (Math.abs(ydif) > TOL) {
                    ytemp = ytemp - ydif;
                }

                if ((Math.abs(xdif) <= TOL) && (Math.abs(ydif) <= TOL)) {
                    dstPts[dstOff++] = xtemp;
                    dstPts[dstOff++] = ytemp;
                    break;
                }
                if (--i < 0) {
                    throw new TransformException(Errors.format(ErrorKeys.NO_CONVERGENCE));
                }
            }

            srcOff += step;
            dstOff += step;
        }
    }

    /**
     * Returns the inverse of this transform.
     *
     * @return the inverse of this transform
     */
    @Override
    public synchronized MathTransform2D inverse() {
        if (inverse == null) {
            inverse = new Inverse();
        }
        return inverse;
    }

    /**
     * Returns a hash value for this transform. To make this faster it does not
     * check the grid values.
     *
     * @return a hash value for this transform.
     */
    @Override
    public final int hashCode() {
        final long code = Double.doubleToLongBits(xmin)
            + (37 * (Double.doubleToLongBits(ymin)
            + (37 * (Double.doubleToLongBits(xmax)
            + (37 * (Double.doubleToLongBits(ymax)
            + (37 * (Double.doubleToLongBits(dx)
            + (37 * (Double.doubleToLongBits(dy)))))))))));

        return (int) code ^ (int) (code >>> 32);
    }

    /**
     * Compares the specified object with this math transform for equality.
     *
     * @param object the object to compare to
     * @return {@code true} if the objects are equal.
     */
    @Override
    public final boolean equals(final Object object) {
        if (object == this) {
            // Slight optimization
            return true;
        }

        if (super.equals(object)) {
            final NADCONTransform that = (NADCONTransform) object;

            return (Double.doubleToLongBits(this.xmin) == Double.doubleToLongBits(that.xmin))
                && (Double.doubleToLongBits(this.ymin) == Double.doubleToLongBits(that.ymin))
                && (Double.doubleToLongBits(this.xmax) == Double.doubleToLongBits(that.xmax))
                && (Double.doubleToLongBits(this.ymax) == Double.doubleToLongBits(that.ymax))
                && (Double.doubleToLongBits(this.dx)   == Double.doubleToLongBits(that.dx))
                && (Double.doubleToLongBits(this.dy)   == Double.doubleToLongBits(that.dy))
                && (this.gridShiftTransform).equals(that.gridShiftTransform);
        }

        return false;
    }

    /**
     * Used to set the preference for the default grid shift file location.
     * This allows grids parameters to be specified by name only, without the
     * full path. This needs to be done only once, by the user.
     * Path values may be simple file system paths or more complex
     * text representations of a url. A value of "default" resets this
     * preference to its default value.
     * <p>
     *
     * Example:
     * <blockquote>
     * <pre>
     * java org.geotools.referencing.operation.transform.NADCONTransform file:///home/rschulz/GIS/NADCON/data
     * </pre>
     * </blockquote>
     *
     * @param args a single argument for the defualt location of grid shift
     *        files
     */
    public static void main(String[] args) {
        final Arguments arguments = new Arguments(args);
        final PrintWriter out = arguments.out;
        final Preferences prefs = Preferences.userNodeForPackage(NADCONTransform.class);

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("default")) {
                prefs.remove(GRID_LOCATION);
            } else {
                prefs.put(GRID_LOCATION, args[0]);
            }

            return;
        } else {
            final String location = prefs.get(GRID_LOCATION,
                    DEFAULT_GRID_LOCATION);
            out.println(
                "Usage: java org.geotools.referencing.operation.transform.NADCONTransform "
                + "<defalult grid file location (path)>");
            out.print("Grid location: \"");
            out.print(location);
            out.println('"');

            return;
        }
    }

    /**
     * Inverse of a {@link NADCONTransform}.
     *
     * @version $Id$
     * @author Rueben Schulz
     */
    private final class Inverse extends AbstractMathTransform.Inverse
            implements MathTransform2D, Serializable
    {
        /** Serial number for interoperability with different versions. */
        private static final long serialVersionUID = -4707304160205218546L;

        /**
         * Default constructor.
         */
        public Inverse() {
            NADCONTransform.this.super();
        }

        /**
         * Returns the parameter values for this math transform.
         *
         * @return A copy of the parameter values for this math transform.
         */
        @Override
        public ParameterValueGroup getParameterValues() {
            return null;
        }

        /**
         * Inverse transform an array of points.
         *
         * @param source
         * @param srcOffset
         * @param dest
         * @param dstOffset
         * @param length
         *
         * @throws TransformException if the input point is outside the area
         *         covered by this grid.
         */
        public void transform(final double[] source, final int srcOffset,
            final double[] dest, final int dstOffset, final int length)
            throws TransformException {
            NADCONTransform.this.inverseTransform(source, srcOffset, dest,
                dstOffset, length);
        }

        /**
         * Returns the original transform.
         */
        @Override
        public MathTransform2D inverse() {
            return (MathTransform2D) super.inverse();
        }

        /**
         * Restore reference to this object after deserialization.
         *
         * @param in DOCUMENT ME!
         * @throws IOException DOCUMENT ME!
         * @throws ClassNotFoundException DOCUMENT ME!
         */
        private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            NADCONTransform.this.inverse = this;
        }
    }

    /**
     * The provider for {@link NADCONTransform}. This provider will construct
     * transforms from {@linkplain org.geotools.referencing.crs.DefaultGeographicCRS
     * geographic} to {@linkplain org.geotools.referencing.crs.DefaultGeographicCRS
     * geographic} coordinate reference systems.
     *
     * @version $Id$
     * @author Rueben Schulz
     */
    public static class Provider extends MathTransformProvider {
        /** Serial number for interoperability with different versions. */
        private static final long serialVersionUID = -4707304160205218546L;

        /**
         * The operation parameter descriptor for the "Latitude_difference_file"
         * parameter value. The default value is "conus.las".
         */
        public static final ParameterDescriptor LAT_DIFF_FILE = new DefaultParameterDescriptor(
                "Latitude_difference_file", String.class, null, "conus.las");

        /**
         * The operation parameter descriptor for the "Longitude_difference_file"
         * parameter value. The default value is "conus.los".
         */
        public static final ParameterDescriptor LONG_DIFF_FILE = new DefaultParameterDescriptor(
                "Longitude_difference_file", String.class, null, "conus.los");

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.OGC,      "NADCON"),
                new NamedIdentifier(Citations.EPSG,     "NADCON"),
                new NamedIdentifier(Citations.EPSG,     "9613"),
                new NamedIdentifier(Citations.GEOTOOLS, Vocabulary.formatInternational(
                                                        VocabularyKeys.NADCON_TRANSFORM))
            }, new ParameterDescriptor[] {
                LAT_DIFF_FILE,
                LONG_DIFF_FILE
            });

        /**
         * Constructs a provider.
         */
        public Provider() {
            super(2, 2, PARAMETERS);
        }

        /**
         * Returns the operation type.
         */
        @Override
        public Class<Transformation> getOperationType() {
            return Transformation.class;
        }

        /**
         * Creates a math transform from the specified group of parameter
         * values.
         *
         * @param values The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not
         *         found.
         * @throws FactoryException if there is a problem creating this
         *         math transform.
         */
        protected MathTransform createMathTransform(final ParameterValueGroup values)
                throws ParameterNotFoundException, FactoryException
        {
            return new NADCONTransform(
                stringValue(LAT_DIFF_FILE,  values),
                stringValue(LONG_DIFF_FILE, values));
        }
    }
}
