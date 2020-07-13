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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.prefs.Preferences;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Errors;
import org.geotools.metadata.i18n.Vocabulary;
import org.geotools.metadata.i18n.VocabularyKeys;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.Parameter;
import org.geotools.parameter.ParameterGroup;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.factory.gridshift.GridShiftLocator;
import org.geotools.referencing.factory.gridshift.NADCONGridShiftFactory;
import org.geotools.referencing.factory.gridshift.NADConGridShift;
import org.geotools.referencing.operation.MathTransformProvider;
import org.geotools.util.Arguments;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchIdentifierException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;
import org.opengis.referencing.operation.Transformation;

/**
 * Transform backed by the North American Datum Conversion grid. The North American Datum Conversion
 * (NADCON) Transform (EPSG code 9613) is a two dimentional datum shift method, created by the
 * National Geodetic Survey (NGS), that uses interpolated values from two grid shift files. This
 * method is used to transform NAD27 (EPSG code 4267) datum coordinates (and some others) to NAD83
 * (EPSG code 4267) within the United States. There are two set of grid shift files: NADCON and High
 * Accuracy Reference Networks (HARN). NADCON shfts from NAD27 (and some others) to NAD83 while HARN
 * shifts from the NADCON NAD83 to an improved NAD83. Both sets of grid shift files may be
 * downloaded from <a
 * href="http://www.ngs.noaa.gov/PC_PROD/NADCON/">www.ngs.noaa.gov/PC_PROD/NADCON/</a>.
 *
 * <p>Some of the NADCON grids, their areas of use, and source datums are shown in the following
 * table.
 *
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
 *
 * <p>Grid shift files come in two formats: binary and text. The files from the NGS are binary and
 * have {@code .las} (latitude shift) and {@code .los} (longitude shift) extentions. Text grids may
 * be created with the <cite>NGS nadgrd</cite> program and have {@code .laa} (latitude shift) and
 * {@code .loa} (longitude shift) file extentions. Both types of files may be used here.
 *
 * <p>The grid names to use for transforming are parameters of this {@link MathTransform}. This
 * parameter may be the full name and path to the grids or just the name of the grids if the default
 * location of the grids was set as a preference. This preference may be set with the main method of
 * this class.
 *
 * <p>Transformations here have been tested to be within 0.00001 seconds of values given by the
 * <cite>NGS ndcon210</cite> program for NADCON grids. American Samoa and HARN shifts have not yet
 * been tested. <strong>References:</strong>
 *
 * <ul>
 *   <li><a href="http://www.ngs.noaa.gov/PC_PROD/NADCON/Readme.htm">NADCONreadme</a>
 *   <li>American Samoa Grids for NADCON - Samoa_Readme.txt
 *   <li><a href="http://www.ngs.noaa.gov/PUBS_LIB/NGS50.pdf">NADCON - The Application of
 *       Minimum-Curvature-Derived Surfaces in the Transformation of Positional Data From the North
 *       American Datum of 1927 to the North American Datum of 1983</a> - NOAA TM.
 *   <li>{@code ndcon210.for} - NGS fortran source code for NADCON conversions. See the following
 *       subroutines: TRANSF, TO83, FGRID, INTRP, COEFF and SURF
 *   <li>{@code nadgrd.for} - NGS fortran source code to export/import binary and text grid formats
 *   <li>EPSG Geodesy Parameters database version 6.5
 * </ul>
 *
 * @see <a href="http://www.ngs.noaa.gov/TOOLS/Nadcon/Nadcon.html">NADCON - North American Datum
 *     Conversion Utility</a>
 * @since 2.1
 * @version $Id$
 * @author Rueben Schulz
 * @todo the transform code does not deal with the case where grids cross +- 180 degrees.
 */
public class NADCONTransform extends AbstractMathTransform
        implements MathTransform2D, Serializable {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = -4707304160205218546L;

    /** The factory that loads the NADCON grids */
    private static NADCONGridShiftFactory FACTORY = new NADCONGridShiftFactory();

    /** Preference node for the grid shift file location. */
    private static final String GRID_LOCATION = "Grid location";

    /** The default value for the grid shift file location. */
    private static final String DEFAULT_GRID_LOCATION = ".";

    /**
     * Difference allowed in iterative computations. This is half the value used in the NGS fortran
     * code (so all tests pass).
     */
    private static final double TOL = 5.0E-10;

    /** Maximum number of iterations for iterative computations. */
    private static final int MAX_ITER = 10;

    /** Conversion factor from seconds to decimal degrees. */
    private static final double SEC_2_DEG = 3600.0;

    /** Latitude grid shift file names. Output in WKT. */
    private final URI latGridName;

    /** Longitude grid shift file names. Output in WKT. */
    private final URI longGridName;

    /**
     * The {@link #gridShift} values as a {@code LocalizationGridTransform2D}. Used for
     * interpolating shift values.
     */
    private MathTransform gridShiftTransform;

    /** The inverse of this transform. Will be created only when needed. */
    private transient MathTransform2D inverse;

    /** The grid driving this transform */
    NADConGridShift grid;

    /**
     * Constructs a {@code NADCONTransform} from the specified grid shift files.
     *
     * @param latGridName path and name (or just name if {@link #GRID_LOCATION} is set) to the
     *     latitude difference file. This will have a {@code .las} or {@code .laa} file extention.
     * @param longGridName path and name (or just name if {@link #GRID_LOCATION} is set) to the
     *     longitude difference file. This will have a {@code .los} or {@code .loa} file extention.
     * @throws ParameterNotFoundException if a math transform parameter cannot be found.
     * @throws FactoryException if there is a problem creating this math transform (ie file
     *     extentions are unknown or there is an error reading the grid files)
     */
    public NADCONTransform(final URI latGridName, final URI longGridName)
            throws ParameterNotFoundException, FactoryException {
        if (latGridName == null) {
            throw new NoSuchIdentifierException("Latitud grid shift file name is null", null);
        }

        if (longGridName == null) {
            throw new NoSuchIdentifierException("Latitud grid shift file name is null", null);
        }

        this.latGridName = latGridName;
        this.longGridName = longGridName;

        URL latGridURL = locateGrid(latGridName);
        URL longGridURL = locateGrid(longGridName);

        this.grid = FACTORY.loadGridShift(latGridURL, longGridURL);
        this.gridShiftTransform = grid.getMathTransform();
    }

    protected URL locateGrid(URI uri) throws FactoryException {
        String grid = uri.toString();
        for (GridShiftLocator locator : ReferencingFactoryFinder.getGridShiftLocators(null)) {
            URL result = locator.locateGrid(grid);
            if (result != null) {
                return result;
            }
        }

        throw new FactoryException("Could not locate grid file " + grid);
    }

    /** Returns the parameter descriptors for this math transform. */
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

        return new ParameterGroup(
                getParameterDescriptors(),
                new GeneralParameterValue[] {lat_diff_file, long_diff_file});
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
     * Transforms a list of coordinate point ordinal values. This method is provided for efficiently
     * transforming many points. The supplied array of ordinal values will contain packed ordinal
     * values. For example, if the source dimension is 3, then the ordinals will be packed in this
     * order: (<var>x<sub>0</sub></var>,<var>y<sub>0</sub></var>,<var>z<sub>0</sub></var>,
     *
     * <p><var>x<sub>1</sub></var>,<var>y<sub>1</sub></var>,<var>z<sub>1</sub></var> ...). All input
     * and output values are in decimal degrees.
     *
     * @param srcPts the array containing the source point coordinates.
     * @param srcOff the offset to the first point to be transformed in the source array.
     * @param dstPts the array into which the transformed point coordinates are returned. May be the
     *     same than {@code srcPts}.
     * @param dstOff the offset to the location of the first transformed point that is stored in the
     *     destination array.
     * @param numPts the number of point objects to be transformed.
     * @throws TransformException if the input point is outside the area covered by this grid.
     */
    public void transform(
            final double[] srcPts, int srcOff, final double[] dstPts, int dstOff, int numPts)
            throws TransformException {
        int step = 0;

        if ((srcPts == dstPts)
                && (srcOff < dstOff)
                && ((srcOff + (numPts * getSourceDimensions())) > dstOff)) {
            step = -getSourceDimensions();
            srcOff -= ((numPts - 1) * step);
            dstOff -= ((numPts - 1) * step);
        }

        while (--numPts >= 0) {
            double x = srcPts[srcOff++];
            double y = srcPts[srcOff++];

            // check bounding box
            if (((x < grid.getMinX()) || (x > grid.getMaxX()))
                    || ((y < grid.getMinY()) || (y > grid.getMaxY()))) {
                throw new TransformException(
                        "Point ("
                                + x
                                + " "
                                + y
                                + ") is not outside of (("
                                + grid.getMinX()
                                + " "
                                + grid.getMinY()
                                + ")("
                                + grid.getMaxX()
                                + " "
                                + grid.getMaxY()
                                + "))");
            }

            // find the grid the point is in (index is 0 based)
            final double xgrid = (x - grid.getMinX()) / grid.getDx();
            final double ygrid = (y - grid.getMinY()) / grid.getDy();
            double[] array = new double[] {xgrid, ygrid};

            // use the LocalizationGridTransform2D transform method (bilineal interpolation)
            // returned shift values are in seconds, longitude shift values are + west
            gridShiftTransform.transform(array, 0, array, 0, 1);

            dstPts[dstOff++] = x - (array[0] / SEC_2_DEG);
            dstPts[dstOff++] = y + (array[1] / SEC_2_DEG);
            srcOff += step;
            dstOff += step;
        }
    }

    /**
     * Transforms nad83 values to nad27. Input and output values are in decimal degrees. This is
     * done by itteratively finding a nad27 value that shifts to the input nad83 value. The input
     * nad83 value is used as the first approximation.
     *
     * @param srcPts the array containing the source point coordinates.
     * @param srcOff the offset to the first point to be transformed in the source array.
     * @param dstPts the array into which the transformed point coordinates are returned. May be the
     *     same than {@code srcPts}.
     * @param dstOff the offset to the location of the first transformed point that is stored in the
     *     destination array.
     * @param numPts the number of point objects to be transformed.
     * @throws TransformException if the input point is outside the area covered by this grid.
     */
    public void inverseTransform(
            final double[] srcPts, int srcOff, final double[] dstPts, int dstOff, int numPts)
            throws TransformException {
        int step = 0;

        if ((srcPts == dstPts)
                && (srcOff < dstOff)
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

            for (int i = MAX_ITER; ; ) {
                double[] array = {xtemp, ytemp};
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

    @Override
    public int hashCode() {
        return grid.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            // Slight optimization
            return true;
        }

        if (super.equals(object)) {
            final NADCONTransform that = (NADCONTransform) object;

            return this.grid.equals(that.grid);
        } else {
            return false;
        }
    }

    /**
     * Used to set the preference for the default grid shift file location. This allows grids
     * parameters to be specified by name only, without the full path. This needs to be done only
     * once, by the user. Path values may be simple file system paths or more complex text
     * representations of a url. A value of "default" resets this preference to its default value.
     *
     * <p>Example:
     *
     * <blockquote>
     *
     * <pre>
     * java org.geotools.referencing.operation.transform.NADCONTransform file:///home/rschulz/GIS/NADCON/data
     * </pre>
     *
     * </blockquote>
     *
     * @param args a single argument for the defualt location of grid shift files
     */
    @SuppressWarnings("PMD.CloseResource")
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
            final String location = prefs.get(GRID_LOCATION, DEFAULT_GRID_LOCATION);
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
            implements MathTransform2D, Serializable {
        /** Serial number for interoperability with different versions. */
        private static final long serialVersionUID = -4707304160205218546L;

        /** Default constructor. */
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
         * @throws TransformException if the input point is outside the area covered by this grid.
         */
        public void transform(
                final double[] source,
                final int srcOffset,
                final double[] dest,
                final int dstOffset,
                final int length)
                throws TransformException {
            NADCONTransform.this.inverseTransform(source, srcOffset, dest, dstOffset, length);
        }

        /** Returns the original transform. */
        @Override
        public MathTransform2D inverse() {
            return (MathTransform2D) super.inverse();
        }

        /** Restore reference to this object after deserialization. */
        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            NADCONTransform.this.inverse = this;
        }
    }

    /**
     * The provider for {@link NADCONTransform}. This provider will construct transforms from
     * {@linkplain org.geotools.referencing.crs.DefaultGeographicCRS geographic} to {@linkplain
     * org.geotools.referencing.crs.DefaultGeographicCRS geographic} coordinate reference systems.
     *
     * @version $Id$
     * @author Rueben Schulz
     */
    public static class Provider extends MathTransformProvider {
        /** Serial number for interoperability with different versions. */
        private static final long serialVersionUID = -4707304160205218546L;

        /**
         * The operation parameter descriptor for the "Latitude_difference_file" parameter value.
         * The default value is "conus.las".
         */
        public static final ParameterDescriptor LAT_DIFF_FILE =
                new DefaultParameterDescriptor("Latitude difference file", URI.class, null, null);

        /**
         * The operation parameter descriptor for the "Longitude_difference_file" parameter value.
         * The default value is "conus.los".
         */
        public static final ParameterDescriptor LONG_DIFF_FILE =
                new DefaultParameterDescriptor("Longitude difference file", URI.class, null, null);

        /** The parameters group. */
        static final ParameterDescriptorGroup PARAMETERS =
                createDescriptorGroup(
                        new NamedIdentifier[] {
                            new NamedIdentifier(Citations.OGC, "NADCON"),
                            new NamedIdentifier(Citations.EPSG, "NADCON"),
                            new NamedIdentifier(Citations.EPSG, "9613"),
                            new NamedIdentifier(
                                    Citations.GEOTOOLS,
                                    Vocabulary.formatInternational(VocabularyKeys.NADCON_TRANSFORM))
                        },
                        new ParameterDescriptor[] {LAT_DIFF_FILE, LONG_DIFF_FILE});

        /** Constructs a provider. */
        public Provider() {
            super(2, 2, PARAMETERS);
        }

        /** Returns the operation type. */
        @Override
        public Class<Transformation> getOperationType() {
            return Transformation.class;
        }

        /**
         * Creates a math transform from the specified group of parameter values.
         *
         * @param values The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         * @throws FactoryException if there is a problem creating this math transform.
         */
        protected MathTransform createMathTransform(final ParameterValueGroup values)
                throws ParameterNotFoundException, FactoryException {
            return new NADCONTransform(
                    (URI) getParameter(LAT_DIFF_FILE, values).getValue(),
                    (URI) getParameter(LONG_DIFF_FILE, values).getValue());
        }
    }
}
