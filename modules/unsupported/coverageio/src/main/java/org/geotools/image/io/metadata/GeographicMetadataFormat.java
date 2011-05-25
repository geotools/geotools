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
package org.geotools.image.io.metadata;

import java.awt.image.RenderedImage;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadataFormatImpl;

import org.geotools.resources.UnmodifiableArrayList;
import org.opengis.coverage.SampleDimension;
import org.opengis.geometry.Envelope;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.crs.CompoundCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.datum.Datum;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.datum.PrimeMeridian;
import org.opengis.referencing.operation.Projection;


/**
 * Describes the structure of {@linkplain GeographicMetadata geographic metadata}.
 * The following formatting rules apply:
 * <p>
 * <ul>
 *   <li>Numbers must be formatted as in the {@linkplain Locale#US US locale}, i.e.
 *       as {@link Integer#toString(int)} or {@link Double#toString(double)}.</li>
 *   <li>Dates must be formatted with the {@code "yyyy-MM-dd HH:mm:ss"}
 *       {@linkplain SimpleDateFormat pattern} in UTC {@linkplain TimeZone timezone}.</li>
 * </ul>
 * <p>
 * This format tries to match approximatively the
 * <a href="http://www.opengeospatial.org/standards/gmljp2">GML in JPEG 2000</a> standard.
 * See the {@linkplain org.geotools.image.io.metadata package javadoc} for a list of departures
 * from the standard.
 *
 * @since 2.4
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 * @author Cédric Briançon
 */
public class GeographicMetadataFormat extends IIOMetadataFormatImpl {
    /**
     * The metadata format name.
     */
    public static final String FORMAT_NAME = "geotools_coverage_1.0";

    /**
     * The maximum number of dimension allowed for the image coordinate system. Images
     * must be at least two-dimensional. Some plugins consider the set of bands as the
     * third dimension (for example slices at different depths). An additional "1 pixel
     * large" temporal dimension is sometime used for storing the image timestamp.
     */
    private static final int MAXIMUM_DIMENSIONS = 4;

    /**
     * The maximum number of bands allowed. This is a somewhat arbitrary value since
     * there is no reason (except memory or disk space constraints) to restrict the
     * number of bands in the image stream. The number of bands actually read is
     * usually much smaller.
     */
    private static final int MAXIMUM_BANDS = Short.MAX_VALUE;

    /**
     * The maximum number of parameters for the projection.
     */
    private static final int MAXIMUM_PARAMETERS = 10;

    /**
     * The geographic {@linkplain CoordinateReferenceSystem coordinate reference system}
     * type. This is often used together with the {@linkplain #ELLIPSOIDAL ellipsoidal}
     * coordinate system type.
     */
    public static final String GEOGRAPHIC = "geographic";

    /**
     * The geographic {@linkplain CoordinateReferenceSystem coordinate reference system}
     * type with a vertical axis. This is often used together with a three-dimensional
     * {@linkplain #ELLIPSOIDAL ellipsoidal} coordinate system type.
     * <p>
     * If the coordinate reference system has no vertical axis, or has additional axis
     * of other kind than vertical (for example only a temporal axis), then the type
     * should be the plain {@value #GEOGRAPHIC}. This is because such CRS are usually
     * constructed as {@linkplain CompoundCRS compound CRS} rather than a CRS with a
     * three-dimensional coordinate system.
     * <p>
     * To be strict, a 3D CRS should be allowed only if the vertical axis is of the kind
     * "height above the ellipsoid" (as opposed to "height above the geoid" for example),
     * otherwise we have a compound CRS. But many datafile don't make this distinction.
     */
    public static final String GEOGRAPHIC_3D = "geographic3D";

    /**
     * The projected {@linkplain CoordinateReferenceSystem coordinate reference system}
     * type. This is often used together with the {@linkplain #CARTESIAN cartesian}
     * coordinate system type.
     */
    public static final String PROJECTED = "projected";

    /**
     * The projected {@linkplain CoordinateReferenceSystem coordinate reference system}
     * type with a vertical axis. This is often used together with a three-dimensional
     * {@linkplain #CARTESIAN cartesian} coordinate system type.
     * <p>
     * If the coordinate reference system has no vertical axis, or has additional axis
     * of other kind than vertical (for example only a temporal axis), then the type
     * should be the plain {@value #PROJECTED}. This is because such CRS are usually
     * constructed as {@linkplain CompoundCRS compound CRS} rather than a CRS with a
     * three-dimensional coordinate system.
     * <p>
     * To be strict, a 3D CRS should be allowed only if the vertical axis is of the kind
     * "height above the ellipsoid" (as opposed to "height above the geoid" for example),
     * otherwise we have a compound CRS. But many datafile don't make this distinction.
     */
    public static final String PROJECTED_3D = "projected3D";

    /**
     * The ellipsoidal {@linkplain CoordinateSystem coordinate system} type.
     */
    public static final String ELLIPSOIDAL = "ellipsoidal";

    /**
     * The cartesian {@linkplain CoordinateSystem coordinate system} type.
     */
    public static final String CARTESIAN = "cartesian";

    /**
     * The geophysics {@linkplain SampleDimension sample dimension} type.
     * Pixels in the {@linkplain RenderedImage rendered image} produced by the image
     * reader contain directly geophysics values like temperature or elevation.
     * Sample type is typically {@code float} or {@code double} and missing value, if
     * any, <strong>must</strong> be one of {@linkplain Float#isNaN NaN values}.
     */
    public static final String GEOPHYSICS = "geophysics";

    /**
     * The packed {@linkplain SampleDimension sample dimension} type.
     * Pixels in the {@linkplain RenderedImage rendered image} produced by the image
     * reader contain packed data, typically as {@code byte} or {@code short}
     * integer type. Conversions to geophysics values are performed by the application
     * of a scale and offset. Some special values are typically used for missing values.
     */
    public static final String PACKED = "packed";

    /**
     * The engineering {@linkplain Datum datum} type.
     */
    public static final String ENGINEERING = "engineering";

    /**
     * The geodetic {@linkplain Datum datum} type.
     */
    public static final String GEODETIC = "geodetic";

    /**
     * The image {@linkplain Datum datum} type.
     */
    public static final String IMAGE = "image";

    /**
     * The temporal {@linkplain Datum datum} type.
     */
    public static final String TEMPORAL = "temporal";

    /**
     * The vertical {@linkplain Datum datum} type.
     */
    public static final String VERTICAL = "vertical";

    /**
     * Enumeration of valid coordinate reference system types.
     */
    static final List<String> CRS_TYPES = UnmodifiableArrayList.wrap(new String[] {
        GEOGRAPHIC, PROJECTED
    });

    /**
     * Enumeration of valid coordinate system types.
     */
    static final List<String> CS_TYPES = UnmodifiableArrayList.wrap(new String[] {
        ELLIPSOIDAL, CARTESIAN
    });

    /**
     * Enumeration of valid datum types.
     */
    static final List<String> DATUM_TYPES = UnmodifiableArrayList.wrap(new String[] {
        ENGINEERING, GEODETIC, IMAGE, TEMPORAL, VERTICAL
    });

    /**
     * Enumeration of valid axis directions. We do not declare {@link String} constants
     * for them since they are already available as {@linkplain AxisDirection
     * axis direction} code list.
     */
    static final List<String> DIRECTIONS = UnmodifiableArrayList.wrap(new String[] {
        "north", "east", "south", "west", "up", "down"
    });

    /**
     * Enumeration of valid pixel orientation. We do not declare {@link String} constants
     * for them since they are already available as {@linkplain PixelOrientation pixel orientation} code list.
     */
    static final List<String> PIXEL_ORIENTATIONS = UnmodifiableArrayList.wrap(new String[] {
        "center", "lower left", "lower right", "upper right", "upper left"
    });

    /**
     * Enumeration of valid sample dimention types.
     */
    static final List<String> SAMPLE_TYPES = UnmodifiableArrayList.wrap(new String[] {
        GEOPHYSICS, PACKED
    });

    /**
     * The default instance. Will be created only when first needed.
     *
     * @see #getInstance
     */
    private static GeographicMetadataFormat DEFAULT;

    /**
     * Creates a default metadata format.
     */
    private GeographicMetadataFormat() {
        this(FORMAT_NAME, MAXIMUM_DIMENSIONS, MAXIMUM_BANDS);
    }

    /**
     * Creates a metadata format of the given name. Subclasses should invoke the various
     * {@link #addElement(String,String,int) addElement} or {@link #addAttribute
     * addAttribute} methods for adding new elements compared to the {@linkplain
     * #getInstance default instance}.
     *
     * @param rootName the name of the root element.
     * @param maximumDimensions The maximum number of dimensions allowed for coordinate
     *                          systems.
     * @param maximumBands The maximum number of sample dimensions allowed for images.
     */
    protected GeographicMetadataFormat(final String rootName,
            final int maximumDimensions, final int maximumBands)
    {
        /*
         * The schemas illustrated in the following comments use the this syntax:
         *
         *   +-- element (attribute) : ClassName
         *
         * Legend: (*)  Mandatory element or attribute.
         *         (!)  Element or attribute that is not from the OGC specification.
         *
         * In this tree, the index {@code n} is the number of dimensions.
         */
        super(rootName, CHILD_POLICY_SOME);
        /*
         * root
         * +-- boundedBy : Envelope
         *     +-- lowerCorner  (*)
         *     +-- upperCorner  (*)
         */
        addElement    ("boundedBy",   rootName,       CHILD_POLICY_ALL);
        addElement    ("lowerCorner", "boundedBy",    CHILD_POLICY_EMPTY);
        addElement    ("upperCorner", "boundedBy",    CHILD_POLICY_EMPTY);
        addObjectValue("lowerCorner", Double.TYPE, 1, MAXIMUM_DIMENSIONS);
        addObjectValue("upperCorner", Double.TYPE, 1, MAXIMUM_DIMENSIONS);
        /*
         * root : RectifiedGridCoverage
         * +-- rectifiedGridDomain (dimension, srsName) : RectifiedGrid             (*)
         *     +-- crs (name, type) : CoordinateReferenceSystem                     (!)
         *     |   +-- datum (name, type) : Datum                                   (!)
         *     |   |   +-- ellipsoid (name, unit) : Ellipsoid                       (!)
         *     |   |   |   +-- semiMajorAxis : double                               (!)
         *     |   |   |   +-- secondDefiningParameter                              (!)
         *     |   |   |       +-- semiMinorAxis : double                           (!)
         *     |   |   |       +-- inverseFlattening : double                       (!)
         *     |   |   +-- primeMeridian (name, greenwichLongitude) : PrimeMeridian (!)
         *     |   +-- cs (name, type) : CoordinateSystem                           (!)
         *     |   |   +-- axis[0] (name, direction, units, origin) : Axis          (!)
         *     |   |   +-- ...                                                      (!)
         *     |   |   +-- axis[n-1] (name, direction, units, origin) : Axis        (!)
         *     |   +-- projection (name) : Projection                               (!)
         *     |       +-- parameter[0] (name, value) : ParameterValue              (!)
         *     |       +-- parameter[1] (name, value) : ParameterValue              (!)
         *     |       +-- ...                                                      (!)
         *     +-- limits : GridEnvelope                                            (*)
         *     |   +-- low  : int[]                                                 (*)
         *     |   +-- high : int[]                                                 (*)
         *     +-- origin : Point                                                   (*)
         *     |   +-- coordinates                                                  (*)
         *     +-- cells                                                            (!)
         *     |   +-- offsetVector[0]                                              (!)
         *     |   +-- ...                                                          (!)
         *     |   +-- offsetVector[n-1]                                            (!)
         *     +-- localizationGrid                                                 (!)
         *     |   +-- ordinates[0] : double[]                                      (!)
         *     |   +-- ...                                                          (!)
         *     |   +-- ordinates[n-1] : double[]                                    (!)
         *     +-- pixelOrientation                                                 (!)
         *     +-- rangeSet : File                                                  (*)
         *         +-- rangeParameters
         *         |   +-- TBD
         *         +-- fileName                                                     (*)
         *         +-- fileStructure                                                (*)
         *         +-- fileDate
         *         +-- fileFormat
         *         +-- spatialResolution (uom)
         *         +-- spectrum
         *         +-- bandRange (uom)
         *         +-- bands (type)                                                 (!)
         *         |   +-- band[0] (name, scale, offset, minValue, maxValue, fillValues) : Band
         *         |   +-- band[1] (name, scale, offset, minValue, maxValue, fillValues) : Band
         *         |   +-- ...
         *         +-- mimeType
         *         +-- compression
         */
        addElement    ("rectifiedGridDomain",     rootName,                  CHILD_POLICY_SOME);
        addAttribute  ("rectifiedGridDomain",     "dimension",               DATATYPE_INTEGER, true, null);
        addAttribute  ("rectifiedGridDomain",     "srsName",                 DATATYPE_STRING);

        addElement    ("crs",                     "rectifiedGridDomain",     CHILD_POLICY_SOME);
        addAttribute  ("crs",                     "name",                    DATATYPE_STRING);
        addAttribute  ("crs",                     "type",                    DATATYPE_STRING, false, null, CRS_TYPES);
        addElement    ("datum",                   "crs",                     CHILD_POLICY_SOME);
        addAttribute  ("datum",                   "name",                    DATATYPE_STRING);
        addAttribute  ("datum",                   "type",                    DATATYPE_STRING, false, null, DATUM_TYPES);
        addElement    ("ellipsoid",               "datum",                   CHILD_POLICY_ALL);
        addAttribute  ("ellipsoid",               "name",                    DATATYPE_STRING);
        addAttribute  ("ellipsoid",               "unit",                    DATATYPE_STRING);
        addElement    ("primeMeridian",           "datum",                   CHILD_POLICY_EMPTY);
        addAttribute  ("primeMeridian",           "name",                    DATATYPE_STRING);
        addAttribute  ("primeMeridian",           "greenwichLongitude",      DATATYPE_DOUBLE);
        addElement    ("semiMajorAxis",           "ellipsoid",               CHILD_POLICY_EMPTY);
        addObjectValue("semiMajorAxis",           Double.class);
        addElement    ("secondDefiningParameter", "ellipsoid",               CHILD_POLICY_CHOICE);
        addElement    ("semiMinorAxis",           "secondDefiningParameter", CHILD_POLICY_EMPTY);
        addObjectValue("semiMinorAxis",           Double.class);
        addElement    ("inverseFlattening",       "secondDefiningParameter", CHILD_POLICY_EMPTY);
        addObjectValue("inverseFlattening",       Double.class);
        addElement    ("cs",                      "crs",                     2, maximumDimensions);
        addAttribute  ("cs",                      "name",                    DATATYPE_STRING);
        addAttribute  ("cs",                      "type",                    DATATYPE_STRING, false, null, CS_TYPES);
        addElement    ("axis",                    "cs",                      CHILD_POLICY_EMPTY);
        addAttribute  ("axis",                    "name",                    DATATYPE_STRING);
        addAttribute  ("axis",                    "direction",               DATATYPE_STRING, true,  null, DIRECTIONS);
        addAttribute  ("axis",                    "units",                   DATATYPE_STRING);
        addAttribute  ("axis",                    "origin",                  DATATYPE_STRING);
        addElement    ("projection",              "crs",                     0, MAXIMUM_PARAMETERS);
        addAttribute  ("projection",              "name",                    DATATYPE_STRING);
        addElement    ("parameter",               "projection",              CHILD_POLICY_EMPTY);
        addAttribute  ("parameter",               "name",                    DATATYPE_STRING);
        addAttribute  ("parameter",               "value",                   DATATYPE_DOUBLE);

        addElement    ("limits",                  "rectifiedGridDomain",     CHILD_POLICY_ALL);
        addElement    ("low",                     "limits",                  CHILD_POLICY_EMPTY);
        addElement    ("high",                    "limits",                  CHILD_POLICY_EMPTY);
        addObjectValue("low",                     Integer.class);
        addObjectValue("high",                    Integer.class);
        addElement    ("origin",                  "rectifiedGridDomain",     CHILD_POLICY_ALL);
        addElement    ("coordinates",             "origin",                  CHILD_POLICY_EMPTY);
        addElement    ("cells",                   "rectifiedGridDomain",     1, MAXIMUM_DIMENSIONS);
        addElement    ("offsetVector",            "cells",                   CHILD_POLICY_EMPTY);
        addElement    ("localizationGrid",        "rectifiedGridDomain",     1, MAXIMUM_DIMENSIONS);
        addElement    ("ordinates",               "localizationGrid",        CHILD_POLICY_EMPTY);
        addElement    ("pixelOrientation",        "rectifiedGridDomain",     CHILD_POLICY_EMPTY);
        addObjectValue("pixelOrientation",        String.class,              1, PIXEL_ORIENTATIONS.size());

        addElement    ("rangeSet",                "rectifiedGridDomain",     CHILD_POLICY_SOME  );
        addElement    ("rangeParameters",         "rangeSet",                CHILD_POLICY_EMPTY);
        // todo: handle rangeParameters' children; for the moment it is considered
        //as a leaf of the tree.
        addElement    ("fileName",                "rangeSet",                CHILD_POLICY_EMPTY);
        addElement    ("fileStructure",           "rangeSet",                CHILD_POLICY_EMPTY);
        addElement    ("fileDate",                "rangeSet",                CHILD_POLICY_EMPTY);
        addElement    ("fileFormat",              "rangeSet",                CHILD_POLICY_EMPTY);
        addElement    ("spatialResolution",       "rangeSet",                CHILD_POLICY_EMPTY);
        addAttribute  ("spatialResolution",       "uom",                     DATATYPE_STRING);
        addElement    ("spectrum",                "rangeSet",                CHILD_POLICY_EMPTY);
        addElement    ("bandRange",               "rangeSet",                0, MAXIMUM_BANDS);
        addAttribute  ("bandRange",               "uom",                     DATATYPE_STRING);
        addElement    ("bands",                   "rangeSet",                0, MAXIMUM_BANDS);
        addAttribute  ("bands",                   "type",                    DATATYPE_STRING, false, null, SAMPLE_TYPES);
        addElement    ("band",                    "bands",                   CHILD_POLICY_EMPTY);
        addElement    ("mimeType",                "rangeSet",                CHILD_POLICY_EMPTY);
        addElement    ("compression",             "rangeSet",                CHILD_POLICY_EMPTY);
        /*
         * root : RectifiedGridCoverage
         *   +-- rectifiedGridDomain (dimension, srsName) : RectifiedGrid (*)
         *       +-- rangeSet : File                                      (*)
         *           +-- bands                                            (!)
         *               +-- band[0] (name, scale, offset, minValue, maxValue, fillValues)
         *               +-- band[1] (name, scale, offset, minValue, maxValue, fillValues)
         *               +-- ...
         */
        addAttribute("band", "name",       DATATYPE_STRING);
        addAttribute("band", "scale",      DATATYPE_DOUBLE);
        addAttribute("band", "offset",     DATATYPE_DOUBLE);
        addAttribute("band", "minValue",   DATATYPE_DOUBLE);
        addAttribute("band", "maxValue",   DATATYPE_DOUBLE);
        addAttribute("band", "fillValues", DATATYPE_DOUBLE, false, 0, Short.MAX_VALUE);
        /*
         * Allow users to specify fully-constructed GeoAPI objects.
         */
        addObjectValue("crs",           CoordinateReferenceSystem.class);
        addObjectValue("datum",         Datum.class);
        addObjectValue("ellipsoid",     Ellipsoid.class);
        addObjectValue("cs",            CoordinateSystem.class);
        addObjectValue("axis",          CoordinateSystemAxis.class);
        addObjectValue("projection",    Projection.class);
        addObjectValue("parameter",     ParameterValue.class);
        addObjectValue("boundedBy",     Envelope.class);
        addObjectValue("primeMeridian", PrimeMeridian.class);
    }

    /**
     * Adds an optional attribute of the specified data type.
     */
    private void addAttribute(final String elementName, final String attrName,
                                                        final int dataType)
    {
        addAttribute(elementName, attrName, dataType, false, null);
    }

    /**
     * Adds an optional object value of the specified class.
     */
    private void addObjectValue(final String elementName, final Class<?> classType) {
        addObjectValue(elementName, classType, false, null);
    }

    /**
     * Returns {@code true} if the element (and the subtree below it) is allowed to appear
     * in a metadata document for an image of the given type. The default implementation
     * always returns {@code true}.
     */
    public boolean canNodeAppear(final String elementName,
                                 final ImageTypeSpecifier imageType)
    {
        return true;
    }

    /**
     * Returns the default geographic metadata format instance.
     */
    public static synchronized GeographicMetadataFormat getInstance() {
        if (DEFAULT == null) {
            DEFAULT = new GeographicMetadataFormat();
        }
        return DEFAULT;
    }
}
