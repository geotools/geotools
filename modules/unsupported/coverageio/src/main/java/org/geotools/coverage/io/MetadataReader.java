/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Collections;
import java.util.Map;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;

import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.image.io.metadata.Axis;
import org.geotools.image.io.metadata.GeographicMetadata;
import org.geotools.image.io.metadata.GeographicMetadataFormat;
import org.geotools.image.io.metadata.Identification;
import org.geotools.image.io.metadata.ImageGeometry;
import org.geotools.image.io.metadata.ImageReferencing;
import org.geotools.image.io.metadata.Parameter;
import org.geotools.image.io.text.TextMetadataParser;
import org.geotools.io.TableWriter;
import org.geotools.metadata.iso.extent.GeographicBoundingBoxImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotools.referencing.cs.DefaultEllipsoidalCS;
import org.geotools.referencing.datum.DefaultEllipsoid;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.referencing.datum.DefaultPrimeMeridian;
import org.geotools.referencing.factory.ReferencingFactoryContainer;
import org.geotools.referencing.operation.DefiningConversion;
import org.geotools.resources.OptionalDependencies;
import org.geotools.util.NumberRange;

import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.geometry.Envelope;
import org.opengis.metadata.extent.GeographicBoundingBox;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchIdentifierException;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CartesianCS;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.cs.CSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.cs.EllipsoidalCS;
import org.opengis.referencing.datum.Datum;
import org.opengis.referencing.datum.DatumFactory;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.datum.GeodeticDatum;
import org.opengis.referencing.datum.PrimeMeridian;
import org.opengis.referencing.operation.Conversion;
import org.opengis.referencing.operation.MathTransformFactory;
import org.opengis.referencing.operation.TransformException;


/**
 * Helper class for creating OpenGIS's object from a set of metadata.
 * <p>Metadata are already stored into a {@linkplain GeographicMetadata geographic metadata}
 *    object, which provides a representation of these metadata as a tree, trying to respect
 *    the <a href="http://www.opengeospatial.org/standards/gmljp2">GML in JPEG 2000</a>
 *    standard.</p>
 * <p>It provides a set of {@code getXXX()} methods for constructing various objects from
 *    those information. For example, the {@link #getCoordinateReferenceSystem} method
 *    constructs a {@link CoordinateReferenceSystem} object using available information.</p>
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Cédric Briançon
 *
 * @since 2.2
 */
public class MetadataReader {
    /**
     * Set of commonly used symbols for "metres".
     *
     * @todo Needs a more general way to set unit symbols once the Unit API is completed.
     */
    private static final String[] METRES = {
        "meter", "meters", "metre", "metres", "m"
    };

    /**
     * Set of commonly used symbols for "degrees".
     *
     * @todo Needs a more general way to set unit symbols once the Unit API is completed.
     */
    private static final String[] DEGREES = {
        "degree", "degrees", "deg", "°"
    };

    /**
     * Set of {@linkplain DefaultEllipsoid ellipsoids} already defined.
     */
    private static final DefaultEllipsoid[] ELLIPSOIDS = new DefaultEllipsoid[] {
        DefaultEllipsoid.CLARKE_1866, DefaultEllipsoid.GRS80, DefaultEllipsoid.INTERNATIONAL_1924,
        DefaultEllipsoid.SPHERE, DefaultEllipsoid.WGS84
    };

    /**
     * The factories to use for constructing ellipsoids, projections, coordinate reference
     * systems...
     */
    private final ReferencingFactoryContainer factories;

    /**
     * The geographic metadata to consider. It should be filled with the
     * {@link #setGeographicMetadata} method, before to called to any getter of this
     * class, since the constructor does not allow to fix its value.
     */
    private GeographicMetadata metadata;

    /**
     * The object to use for parsing and formatting units.
     */
    private UnitFormat unitFormat;

    /**
     * Constructs a new {@code MetadataReader} using default factories and geographic
     * metadata. Do not forget to call {@link #setGeographicMetadata(GeographicMetadata)}
     * in order to fix the metadata value.
     */
    public MetadataReader() {
        factories = ReferencingFactoryContainer.instance(null);
    }

    /**
     * Constructs a new {@code MetadataReader} using the specified factories. Do not
     * forget to call {@link #setGeographicMetadata} in order to
     * fix the metadata value.
     *
     * @param factories The specified factories. Should not be {@code null}.
     */
    public MetadataReader(final ReferencingFactoryContainer factories) {
        this.factories = factories;
    }

    /**
     * Constructs an {@linkplain CoordinateSystemAxis axis} using the information from
     * the {@linkplain GeographicMetadata metadata}, or returns {@code null} if the axis
     * does not exist in the metadata tree.
     *
     * @param dimension The dimension to consider. It should be lower than
     *                  {@link ImageGeometry#getDimension()}
     * @return An axis with information gotten from the
     *         {@linkplain GeographicMetadata metadata}, or {@code null} if the axis
     *         does not exist for the specified dimension.
     * @throws MetadataException if the method {@link CSFactory#createCoordinateSystemAxis}
     *                           has not succeed.
     */
    public synchronized CoordinateSystemAxis getAxis(final int dimension)
            throws MetadataException
    {
        final ImageReferencing referencing = metadata.getReferencing();
        if (metadata.getGeometry().getDimension() < dimension) {
            return null;
        }
        final Axis axis = referencing.getAxis(dimension);
        String axisName = axis.getName();
        AxisDirection direction = AxisDirection.valueOf(axis.getDirection());
        if (axisName == null) {
            final String projectionName = referencing.getProjectionName();
            switch (dimension) {
                case 0: axisName  = (projectionName == null) ? "longitude"          : "x";
                        direction = (direction      == null) ? AxisDirection.EAST   : direction; break;
                case 1: axisName  = (projectionName == null) ? "latitude"           : "y";
                        direction = (direction      == null) ? AxisDirection.NORTH  : direction; break;
                case 2: axisName  = (projectionName == null) ? "depth"              : "z";
                        direction = (direction      == null) ? AxisDirection.UP     : direction; break;
                case 3: axisName  = (projectionName == null) ? "time"               : "t";
                        direction = (direction      == null) ? AxisDirection.FUTURE : direction; break;
            }
        }
        final DefaultCoordinateSystemAxis axisFound =
                DefaultCoordinateSystemAxis.getPredefined(axisName, direction);
        if (axisFound != null) {
            return axisFound;
        }
        /* The current axis defined in the metadata tree is not already known in the Geotools
         * implementation, so one will build it using those information.
         */
        final String unitName = axis.getUnits();
        final Unit<?> unit = getUnit(unitName);
        final Map<String,String> map = Collections.singletonMap("name", axisName);
        try {
            return factories.getCSFactory().createCoordinateSystemAxis(map, axisName, direction, unit);
        } catch (FactoryException e) {
            throw new MetadataException(e.getLocalizedMessage());
        }
    }

    /**
     * Returns the unit which matches with the name given.
     *
     * @param unitName The name of the unit. Should not be {@code null}.
     * @return The unit matching with the specified name.
     * @throws MetadataException if the unit name does not match with the
     *                           {@linkplain #unitFormat unit format}.
     */
    private Unit<?> getUnit(final String unitName) throws MetadataException {
        if (contains(unitName, METRES)) {
            return SI.METER;
        } else if (contains(unitName, DEGREES)) {
            return NonSI.DEGREE_ANGLE;
        } else {
            if (unitFormat == null) {
                unitFormat = UnitFormat.getInstance();
            }
            try {
                return (Unit) unitFormat.parseObject(unitName);
            } catch (ParseException e) {
                throw new MetadataException("Unit not known : " + unitName, e);
            }
        }
    }

    /**
     * Check if {@code toSearch} appears in the {@code list} array.
     * Search is case-insensitive. This is a temporary patch (will be removed
     * when the final API for JSR-108: Units specification will be available).
     */
    private static boolean contains(final String toSearch, final String[] list) {
        for (int i=list.length; --i>=0;) {
            if (toSearch.equalsIgnoreCase(list[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the datum. The default implementation performs the following steps:
     * <p>
     * <ul>
     *   <li>Verifies if the datum name contains {@code WGS84}, and returns a
     *       {@link DefaultGeodeticDatum#WGS84} geodetic datum if it is the case.
     *   </li>
     *   <li>Builds a {@linkplain PrimeMeridian prime meridian} using information
     *       stored into the metadata tree.
     *   </li>
     *   <li>Returns a {@linkplain DefaultGeodeticDatum geodetic datum} built on the
     *       prime meridian.
     *   </li>
     * </ul>
     * </p>
     * @throws MetadataException if the datum is not defined, or if the {@link #getEllipsoid}
     *                           method fails.
     *
     * @todo: The current implementation only returns a
     *        {@linkplain GeodeticDatum geodetic datum}, other kind of datum have
     *        to be generated too.
     *
     * @see #getEllipsoid
     */
    public synchronized Datum getDatum() throws MetadataException {
        final ImageReferencing referencing = metadata.getReferencing();
        final Identification identDatum = referencing.getDatum();
        if (identDatum == null) {
            throw new MetadataException("The datum is not defined.");
        }
        final String name = identDatum.name;
        if (name == null) {
            throw new MetadataException("Datum name not defined.");
        }
        if (name.toUpperCase().contains("WGS84")) {
            return DefaultGeodeticDatum.WGS84;
        }
        final String primeMeridianName = referencing.getPrimeMeridianName();
        final PrimeMeridian primeMeridian;
        /* By default, if the prime meridian name is not defined, or if it is defined with
         * {@code Greenwich}, one chooses the {@code Greenwich} meridian as prime meridian.
         * Otherwise one builds it, using the {@code greenwichLongitude} parameter.
         */
        if ((primeMeridianName == null) ||
            (primeMeridianName != null && primeMeridianName.toLowerCase().contains("greenwich"))) {
            primeMeridian = DefaultPrimeMeridian.GREENWICH;
        } else {
            final double greenwichLon = referencing.getPrimeMeridianGreenwichLongitude();
            primeMeridian = (Double.isNaN(greenwichLon)) ? DefaultPrimeMeridian.GREENWICH :
                new DefaultPrimeMeridian(primeMeridianName, greenwichLon);
        }
        return new DefaultGeodeticDatum(name, getEllipsoid(), primeMeridian);
    }

    /**
     * Returns the ellipsoid. Depending on whether {@link ImageReferencing#semiMinorAxis}
     * or {@link ImageReferencing#inverseFlattening} has been defined, the default
     * implementation will construct an ellispoid using {@link DatumFactory#createEllipsoid}
     * or {@link DatumFactory#createFlattenedSphere} respectively.
     *
     * @throws MetadataException if the operation failed to create the
     *                           {@linkplain Ellipsoid ellipsoid}.
     *
     * @see #getUnit(String)
     */
    public synchronized Ellipsoid getEllipsoid() throws MetadataException {
        final ImageReferencing referencing = metadata.getReferencing();
        final String name = referencing.getEllipsoidName();
        if (name != null) {
            for (final DefaultEllipsoid ellipsoid : ELLIPSOIDS) {
                if (ellipsoid.nameMatches(name)) {
                    return ellipsoid;
                }
            }
        } else {
            throw new MetadataException("Ellipsoid name not defined.");
        }
        // It has a name defined, but it is not present in the list of known ellipsoids.
        final double semiMajorAxis = referencing.getSemiMajorAxis();
        if (Double.isNaN(semiMajorAxis)) {
            throw new MetadataException("Ellipsoid semi major axis not defined.");
        }
        final double semiMinorAxis = referencing.getSemiMinorAxis();
        final String ellipsoidUnit = referencing.getEllipsoidUnit();
        if (ellipsoidUnit == null) {
            throw new MetadataException("Ellipsoid unit not defined.");
        }
        final Unit unit = getUnit(ellipsoidUnit);
        final Map<String,String> map = Collections.singletonMap("name", name);
        try {
            final DatumFactory datumFactory = factories.getDatumFactory();
            return (!Double.isNaN(semiMinorAxis)) ?
                datumFactory.createEllipsoid(map, semiMajorAxis, semiMinorAxis, unit) :
                datumFactory.createFlattenedSphere(
                        map, semiMajorAxis, referencing.getInverseFlattening(), unit);
        } catch (FactoryException e) {
            throw new MetadataException(e.getLocalizedMessage());
        }
    }

    /**
     * Returns the projection. The default implementation performs the following steps:
     * <p>
     * <ul>
     *   <li>Gets a {@linkplain ParameterValueGroup parameter value group} from a
     *       {@link MathTransformFactory}</li>
     *
     *   <li>Gets the metadata values for each parameters in the above step. If a parameter is not
     *       defined in this {@code MetadataReader}, then it will be left to its (projection
     *       dependent) default value. Parameters are projection dependent, but will typically
     *       include
     *
     *           {@code "semi_major"},
     *           {@code "semi_minor"} (or {@code "inverse_flattening"}),
     *           {@code "central_meridian"},
     *           {@code "latitude_of_origin"},
     *           {@code "false_easting"} and
     *           {@code "false_northing"}.
     *
     *       The names actually used in the metadata file to be parsed must be declared as usual,
     *       e.g. <code>{@linkplain TextMetadataParser#addAlias addAlias}
     *                  ({@linkplain TextMetadataParser#SEMI_MAJOR}, ...)</code></li>
     *
     *   <li>Computes and returns a {@linkplain DefiningConversion conversion} using the name of the
     *       projection and the parameter value group previously filled.</li>
     * </ul>
     * </p>
     * @return The projection.
     * @throws MetadataException if the operation failed for some other reason
     *         (for example if a parameter value can't be parsed as a {@code double}).
     *
     * @see TextMetadataParser#SEMI_MAJOR
     * @see TextMetadataParser#SEMI_MINOR
     * @see TextMetadataParser#INVERSE_FLATTENING
     * @see TextMetadataParser#LATITUDE_OF_ORIGIN
     * @see TextMetadataParser#CENTRAL_MERIDIAN
     * @see TextMetadataParser#FALSE_EASTING
     * @see TextMetadataParser#FALSE_NORTHING
     */
    public synchronized Conversion getProjection() throws MetadataException {
        final MathTransformFactory mathTransformFactory = factories.getMathTransformFactory();
        final ImageReferencing referencing = metadata.getReferencing();
        final String projectionName = referencing.getProjectionName();
        if (projectionName == null) {
            throw new MetadataException("Projection name is not defined.");
        }
        final ParameterValueGroup paramValueGroup;
        try {
            paramValueGroup = mathTransformFactory.getDefaultParameters(projectionName);
        } catch (NoSuchIdentifierException e) {
            throw new MetadataException(e.getLocalizedMessage());
        }
        for (final Parameter parameter : referencing.getParameters()) {
            final String name = parameter.getName();
            if (name == null) {
                continue;
            }
            final double value = parameter.getValue();
            if (Double.isNaN(value)) {
                continue;
            }
            try {
                paramValueGroup.parameter(name).setValue(value);
            } catch (ParameterNotFoundException e) {
                // Should not happened. Continue with the next parameter, the current one
                // will be ignored.
                continue;
            }
        }
        return new DefiningConversion(projectionName, paramValueGroup);
    }

    /**
     * Returns the {@linkplain CoordinateReferenceSystem coordinate reference system}.
     * The default implementation builds a coordinate reference system using the
     * {@linkplain #getDatum datum} and the {@linkplain #getCoordinateSystem coordinate system}
     * defined in the metadata.
     *
     * @throws MetadataException if the creation of the coordinate reference system fails.
     *
     * @see #getDatum
     * @see #getCoordinateSystem
     * @see CoordinateReferenceSystem
     */
    public synchronized CoordinateReferenceSystem getCoordinateReferenceSystem()
            throws MetadataException
    {
        final ImageReferencing referencing = metadata.getReferencing();
        String name = referencing.getCoordinateReferenceSystem().name;
        if (name == null) {
            name = "Unknown";
        }
        if (name.contains("WGS84")){
            return (name.contains("3D")) ? DefaultGeographicCRS.WGS84_3D :
                                           DefaultGeographicCRS.WGS84;
        }
        String type = referencing.getCoordinateReferenceSystem().type;
        if (type == null) {
            type = (referencing.getProjectionName() == null) ?
                GeographicMetadataFormat.GEOGRAPHIC : GeographicMetadataFormat.PROJECTED;
        }
        final CRSFactory factory = factories.getCRSFactory();
        final Map<String,String> map = Collections.singletonMap("name", name);
        try {
            if (type.equalsIgnoreCase(GeographicMetadataFormat.GEOGRAPHIC)) {
                return factory.createGeographicCRS(map, (GeodeticDatum)getDatum(),
                        (EllipsoidalCS)getCoordinateSystem());
            } else {
                final GeographicCRS baseCRS = factory.createGeographicCRS(map,
                        (GeodeticDatum) getDatum(), DefaultEllipsoidalCS.GEODETIC_2D);
                return factory.createProjectedCRS(map, baseCRS, getProjection(),
                        (CartesianCS) getCoordinateSystem());
            }
        } catch (FactoryException e) {
            throw new MetadataException(e.getLocalizedMessage());
        }
    }

    /**
     * Returns the {@linkplain CoordinateSystem coordinate system}. The default implementation
     * builds a coordinate system using the {@linkplain #getAxis axes} defined in the
     * metadata.
     *
     * @throws MetadataException if there is less than 2 axes defined in the metadata, or if
     *                           the creation of the coordinate system failed.
     *
     * @see #getAxis
     * @see CoordinateSystem
     */
    public synchronized CoordinateSystem getCoordinateSystem() throws MetadataException {
        final ImageReferencing referencing = metadata.getReferencing();
        Identification cs = referencing.getCoordinateSystem();
        if (cs == null) {
            cs = new Identification("Unknown", null);
        }
        String name = cs.name;
        if (name == null) {
            name = "Unknown";
        }
        String type = cs.type;
        if (type == null) {
            type = (referencing.getProjectionName() == null) ?
                GeographicMetadataFormat.ELLIPSOIDAL :
                GeographicMetadataFormat.CARTESIAN;
        }
        final CSFactory factory = factories.getCSFactory();
        final Map<String,String> map = Collections.singletonMap("name", name);
        final int dimension = metadata.getGeometry().getDimension();
        if (dimension < 2) {
            throw new MetadataException("Number of dimension error : " + dimension);
        }
        try {
            if (type.equalsIgnoreCase(GeographicMetadataFormat.CARTESIAN)) {
                return (dimension < 3) ?
                    factory.createCartesianCS(map, getAxis(0), getAxis(1)) :
                    factory.createCartesianCS(map, getAxis(0), getAxis(1), getAxis(2));
            }
            if (type.equalsIgnoreCase(GeographicMetadataFormat.ELLIPSOIDAL)) {
                return (dimension < 3) ?
                    factory.createEllipsoidalCS(map, getAxis(0), getAxis(1)) :
                    factory.createEllipsoidalCS(map, getAxis(0), getAxis(1), getAxis(2));
            }
            /* Should not happened, since the type value should be contained in the
             * {@link GeographicMetadataFormat#CS_TYPES} list.
             */
            throw new MetadataException("Coordinate system type not known : " + type);
        } catch (FactoryException e) {
            throw new MetadataException(e.getLocalizedMessage());
        }
    }

    /**
     * Returns the envelope. The default implementation constructs an envelope
     * using the values from the {@linkplain GeographicMetadata metadata} tree
     * <ul>
     *   <li>The horizontal limits {@link ImageGeometry#lowerCorner}.</li>
     *   <li>The vertical limits {@link ImageGeometry#upperCorner}.</li>
     * </ul>
     *
     * @throws MetadataException if the dimension specified for the envelope is illegal
     */
    public synchronized Envelope getEnvelope() throws MetadataException {
        final ImageGeometry geometry   = metadata.getGeometry();
        final int dimension            = geometry.getDimension();
        final GeneralEnvelope envelope = new GeneralEnvelope(dimension);
        for (int i=0; i<dimension; i++) {
            final NumberRange<Double> range = geometry.getOrdinateRange(i);
            final double              min   = range   .getMinimum();
            final double              max   = range   .getMaximum();
            try {
                envelope.setRange(i, min, max);
            } catch (IndexOutOfBoundsException e) {
                // Should not occur, but if it comes, throws the exception.
                throw new MetadataException(e.getLocalizedMessage());
            }
        }
        return envelope.clone();
    }

    /**
     * Convenience method returning the envelope in geographic coordinate reference system.
     * Note that the geographic CRS doesn't need to use the 1984 datum, since geographic
     * bounding boxes are approximative.
     *
     * @throws MetadataException if the operation failed. This exception
     *         may contains a {@link TransformException} as its cause.
     *
     * @see #getEnvelope
     */
    public synchronized GeographicBoundingBox getGeographicBoundingBox() throws MetadataException {
        final GeographicBoundingBoxImpl box;
        try {
            box = new GeographicBoundingBoxImpl(getEnvelope());
        } catch (TransformException exception) {
            throw new MetadataException(exception.getLocalizedMessage());
        }
        box.freeze();
        return box;
    }

    /**
     * Returns the current {@linkplain GeographicMetadata geographic metadata}, or
     * {@code null} if not already defined.
     */
    public GeographicMetadata getGeographicMetadata() {
        return metadata;
    }

    /**
     * Sets the current geographic metadata. It should be called before using a getter
     * of this class.
     */
    public void setGeographicMetadata(final GeographicMetadata metadata) {
        this.metadata = metadata;
    }

    /**
     * Returns the grid range. Default implementation fetchs the metadata values
     * for nodes {@link ImageGeometry#low} and {@link ImageGeometry#high}, and
     * transform the resulting strings into a {@linkplain GridEnvelope grid range}
     * object.
     *
     * @throws MissingMetadataException if a required value is missing.
     * @throws MetadataException if the operation failed for some other reason.
     *
     * @see ImageGeometry#getGridRange
     */
    public synchronized GridEnvelope getGridRange() throws MetadataException {
        final ImageGeometry geometry = metadata.getGeometry();
        final int dimension = geometry.getDimension();
        final int[]  lowers = new int[dimension];
        final int[]  uppers = new int[dimension];
        for (int i=0; i<dimension; i++) {
            final NumberRange<Integer> range = geometry.getGridRange(i);
            lowers[i] = range.getMinValue();
            uppers[i] = range.getMaxValue();
            if (!range.isMinIncluded()) lowers[i]++;
            if (!range.isMaxIncluded()) uppers[i]--;
        }
        return new GeneralGridEnvelope(lowers, uppers, true);
    }

    /**
     * Returns a string representation of this metadata set. The default implementation
     * write the class name and the envelope in geographic coordinates, as returned by
     * {@link #getGeographicBoundingBox}. Then, it append the list of all metadata as
     * formatted by {@link GeographicMetadata#getAsTree}.
     */
    @Override
    public String toString() {
        final String lineSeparator = System.getProperty("line.separator", "\n");
        final StringWriter  buffer = new StringWriter();
        buffer.write(lineSeparator);
        try {
            final GeographicBoundingBox box = getGeographicBoundingBox();
            buffer.write(GeographicBoundingBoxImpl.toString(box, "DD°MM'SS\"", null));
            buffer.write(lineSeparator);
        } catch (MetadataException exception) {
            // Ignore.
        }
        buffer.write('{');
        buffer.write(lineSeparator);
        try {
            final TableWriter table = new TableWriter(buffer, 2);
            table.setMultiLinesCells(true);
            table.nextColumn();
            table.write(OptionalDependencies.toString(OptionalDependencies.xmlToSwing(
                    metadata.getAsTree(GeographicMetadataFormat.FORMAT_NAME))));
            table.flush();
        } catch (IOException exception) {
            buffer.write(exception.getLocalizedMessage());
        }
        buffer.write('}');
        buffer.write(lineSeparator);
        return buffer.toString();
    }

    /**
     * Trim a character string. Leading and trailing spaces are removed. Any succession of
     * one ore more unicode whitespace characters (as of {@link Character#isSpaceChar(char)}
     * are replaced by a single <code>'_'</code> character. Example:
     *
     *                       <pre>"This   is a   test"</pre>
     * will be returned as   <pre>"This_is_a_test"</pre>
     *
     * @param  str The string to trim (may be {@code null}).
     * @param  separator The separator to insert in place of succession of whitespaces.
     *         Usually "_" for keys and " " for values.
     * @return The trimed string, or {@code null} if <code>str</code> was null.
     */
    static String trim(String str, final String separator) {
        if (str != null) {
            str = str.trim();
            StringBuilder buffer = null;
    loop:       for (int i=str.length(); --i>=0;) {
                if (Character.isSpaceChar(str.charAt(i))) {
                    final int upper = i;
                    do if (--i < 0) break loop;
                    while (Character.isSpaceChar(str.charAt(i)));
                    if (buffer == null) {
                        buffer = new StringBuilder(str);
                    }
                    buffer.replace(i+1, upper+1, separator);
                }
            }
            if (buffer != null) {
                return buffer.toString();
            }
        }
        return str;
    }
}
