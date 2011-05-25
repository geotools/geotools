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
package org.geotools.image.io.text;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import java.util.Map.Entry;
import javax.measure.unit.SI;
import javax.measure.unit.NonSI;

import javax.imageio.IIOException;
import javax.media.jai.DeferredProperty;
import javax.media.jai.PropertySource;


import org.geotools.io.TableWriter;
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.referencing.factory.ReferencingFactoryContainer;
import org.geotools.coverage.io.AmbiguousMetadataException;
import org.geotools.coverage.io.MetadataReader;
import org.geotools.coverage.io.MetadataException;
import org.geotools.image.io.metadata.GeographicMetadata;
import org.geotools.image.io.metadata.GeographicMetadataFormat;
import org.geotools.resources.OptionalDependencies;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.datum.Datum;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.operation.MathTransformFactory;
import org.opengis.referencing.operation.OperationMethod;
import org.opengis.referencing.operation.Projection;


/**
 * Helper class for creating OpenGIS's object from a set of metadata. Metadata are
 * <cite>key-value</cite> pairs, for example {@code "Units=meters"}. There is a wide
 * variety of ways to contruct OpenGIS's objects from <cite>key-value</cite> pairs, and
 * supporting them is not always straightforward. The {@code MetadataReader} class
 * tries to make the work easier. It defines a set of format-neutral keys (i.e. keys not
 * related to any specific file format). Before parsing a file, the mapping between
 * format-neutral keys and "real" keys used in a particuler file format <strong>must</strong>
 * be specified. This mapping is constructed with calls to {@link #addAlias}. For example,
 * one may want to parse the following informations:
 *
 * <blockquote><pre>
 * XMinimum           = 217904.31
 * YMaximum           = 5663495.1
 * XResolution        = 1000.0000
 * YResolution        = 1000.0000
 * Unit               = meters
 * Projection         = Mercator_1SP
 * Central meridian   = -15.2167
 * Latitude of origin =  28.0667
 * False easting      = 0.00000000
 * False northing     = 0.00000000
 * Ellipsoid          = Clarke 1866
 * Datum              = Clarke 1866
 * </pre></blockquote>
 *
 * Before to be used for parsing such informations, a {@code MetadataReader} object
 * must be setup using the following code:
 *
 * <blockquote><pre>
 * addAlias({@link #X_MINIMUM},    "XMinimum");
 * addAlias({@link #Y_MAXIMUM},    "YMaximum");
 * addAlias({@link #X_RESOLUTION}, "XResolution");
 * addAlias({@link #Y_RESOLUTION}, "YResolution");
 * // etc...
 * </pre></blockquote>
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Cédric Briançon
 *
 * @since 2.5
 */
public abstract class TextMetadataParser {
    /**
     * The geogrpahic metadata to consider.
     */
    protected GeographicMetadata metadata;

    /**
     * Key for the {@linkplain CoordinateReferenceSystem coordinate reference system}.
     * The {@link MetadataReader#getCoordinateReferenceSystem} method looks for this
     * metadata.
     *
     * @see #UNIT
     * @see #DATUM
     * @see #PROJECTION
     */
    public static final Key COORDINATE_REFERENCE_SYSTEM =
            new Key("coordinate_reference_system");

    /**
     * Key for the {@linkplain CoordinateReferenceSystem coordinate reference system} type.
     *
     * @see #UNIT
     * @see #DATUM
     * @see #PROJECTION
     */
    public static final Key COORDINATE_REFERENCE_SYSTEM_TYPE =
            new Key("coordinate_reference_system_type");

    
    /**
     * Key for the {@linkplain CoordinateSystem coordinate system}.
     * The {@link MetadataReader#getCoordinateSystem} method looks for this
     * metadata.
     *
     * @see #UNIT
     * @see #DATUM
     * @see #PROJECTION
     */
    public static final Key COORDINATE_SYSTEM = new Key("coordinate_system");

    /**
     * Key for the {@linkplain CoordinateSystem coordinate system} type.
     *
     * @see #UNIT
     * @see #DATUM
     * @see #PROJECTION
     */
    public static final Key COORDINATE_SYSTEM_TYPE = new Key("coordinate_system_type");

    /**
     * Key for the {@linkplain CoordinateSystemAxis coordinate system axis} units.
     * The {@link MetadataReader#getUnit} method looks for this metadata. The following
     * heuristic rule may be applied in order to infer the CRS from the units:
     * <p>
     * <ul>
     *   <li>If the unit is compatible with {@linkplain NonSI#DEGREE_ANGLE degrees},
     *       then a {@linkplain GeographicCRS geographic CRS} is assumed.</li>
     *   <li>Otherwise, if this unit is compatible with {@linkplain SI#METER metres},
     *       then a {@linkplain ProjectedCRS projected CRS} is assumed.</li>
     * </ul>
     *
     * @see #ELLIPSOID
     * @see #DATUM
     * @see #PROJECTION
     * @see #COORDINATE_REFERENCE_SYSTEM
     */
    public static final Key UNIT = new Key("Unit");

    /**
     * Key for the coordinate reference system's {@linkplain Datum datum}.
     * The {@link MetadataReader#getDatum} method looks for this metadata.
     *
     * @see #UNIT
     * @see #ELLIPSOID
     * @see #PROJECTION
     * @see #COORDINATE_REFERENCE_SYSTEM
     */
    public static final Key DATUM = new Key("Datum");
    
    public static final Key DATUM_TYPE = new Key("Datum_type");

    /**
     * Key for the coordinate reference system {@linkplain Ellipsoid ellipsoid}.
     * The {@link MetadataReader#getEllipsoid} method looks for this metadata.
     *
     * @see #UNIT
     * @see #DATUM
     * @see #PROJECTION
     * @see #COORDINATE_REFERENCE_SYSTEM
     */
    public static final Key ELLIPSOID = new Key("Ellipsoid");

    /**
     * The unit of the {@linkplain Ellipsoid ellipsoid}.
     */
    public static final Key ELLIPSOID_UNIT = new Key("ellipsoid_unit");

    /**
     * Key for the {@linkplain OperationMethod operation method}. The
     * {@link MetadataReader#getProjection} method looks for this metadata. The operation
     * method name determines the {@linkplain MathTransformFactory#getDefaultParameters
     * math transform implementation and its list of parameters}. This name is the
     * projection <cite>classification</cite>.
     * <p>
     * If this metadata is not defined, then the operation name is inferred from the
     * {@linkplain #PROJECTION projection name}.
     *
     * @see #PROJECTION
     * @see #COORDINATE_REFERENCE_SYSTEM
     */
    public static final Key OPERATION_METHOD = new Key("OperationMethod");

    /**
     * Key for the {@linkplain Projection projection}. The
     * {@link MetadataReader#getProjection} method looks for this metadata. If the
     * metadata is not defined, then the projection name is assumed the same than the
     * {@linkplain #OPERATION_METHOD operation method} name.
     *
     * @see #SEMI_MAJOR
     * @see #SEMI_MINOR
     * @see #LATITUDE_OF_ORIGIN
     * @see #CENTRAL_MERIDIAN
     * @see #FALSE_EASTING
     * @see #FALSE_NORTHING
     */
    public static final Key PROJECTION = new Key("Projection");

    /**
     * Key for the {@code "prime_meridian"} name parameter.
     */
    public static final Key PRIME_MERIDIAN = new Key("prime_meridian");

    /**
     * Key for the {@code "greenwich_longitude"} parameter.
     */
    public static final Key GREENWICH_LONGITUDE = new Key("greenwich_longitude");

    /**
     * Key for the {@code "semi_major"} ellipsoid parameter. There is no specific method
     * for this key. However, this key may be queried indirectly by
     * {@link MetadataReader#getEllipsoid}.
     *
     * @see #SEMI_MINOR
     * @see #INVERSE_FLATTENING
     * @see #LATITUDE_OF_ORIGIN
     * @see #CENTRAL_MERIDIAN
     * @see #FALSE_EASTING
     * @see #FALSE_NORTHING
     * @see #PROJECTION
     */
    public static final Key SEMI_MAJOR = new Key("semi_major");

    /**
     * Key for the {@code "semi_minor"} ellipsoid parameter. There is no specific method
     * for this key. However, this key may be queried indirectly by
     * {@link MetadataReader#getEllipsoid}.
     *
     * @see #INVERSE_FLATTENING
     * @see #SEMI_MAJOR
     * @see #LATITUDE_OF_ORIGIN
     * @see #CENTRAL_MERIDIAN
     * @see #FALSE_EASTING
     * @see #FALSE_NORTHING
     * @see #PROJECTION
     */
    public static final Key SEMI_MINOR = new Key("semi_minor");

    /**
     * Key for the {@code "inverse_flattening"} ellipsoid parameter. There is no specific
     * method for this key. However, this key may be queried indirectly by
     * {@link MetadataReader#getEllipsoid}.
     *
     * @see #SEMI_MINOR
     * @see #SEMI_MAJOR
     * @see #LATITUDE_OF_ORIGIN
     * @see #CENTRAL_MERIDIAN
     * @see #FALSE_EASTING
     * @see #FALSE_NORTHING
     * @see #PROJECTION
     */
    public static final Key INVERSE_FLATTENING = new Key("inverse_flattening");

    /**
     * Key for the {@code "latitude_of_origin"} projection parameter. There is no specific
     * method for this key. However, this key may be queried indirectly by
     * {@link MetadataReader#getProjection}.
     *
     * @see #SEMI_MAJOR
     * @see #SEMI_MINOR
     * @see #CENTRAL_MERIDIAN
     * @see #FALSE_EASTING
     * @see #FALSE_NORTHING
     * @see #PROJECTION
     */
    public static final Key LATITUDE_OF_ORIGIN = new Key("latitude_of_origin");

    /**
     * Key for the {@code "central_meridian"} projection parameter. There is no specific
     * method for this key. However, this key may be queried indirectly by
     * {@link MetadataReader#getProjection}.
     *
     * @see #SEMI_MAJOR
     * @see #SEMI_MINOR
     * @see #LATITUDE_OF_ORIGIN
     * @see #FALSE_EASTING
     * @see #FALSE_NORTHING
     * @see #PROJECTION
     */
    public static final Key CENTRAL_MERIDIAN = new Key("central_meridian");

    /**
     * Key for the {@code "false_easting"} projection parameter. There is no specific
     * method for this key. However, this key may be queried indirectly by
     * {@link MetadataReader#getProjection}.
     *
     * @see #SEMI_MAJOR
     * @see #SEMI_MINOR
     * @see #LATITUDE_OF_ORIGIN
     * @see #CENTRAL_MERIDIAN
     * @see #FALSE_NORTHING
     * @see #PROJECTION
     */
    public static final Key FALSE_EASTING = new Key("false_easting");

    /**
     * Key for the {@code "false_northing"} projection parameter. There is no specific
     * method for this key. However, this key may be queried indirectly by
     * {@link MetadataReader#getProjection}.
     *
     * @see #SEMI_MAJOR
     * @see #SEMI_MINOR
     * @see #LATITUDE_OF_ORIGIN
     * @see #CENTRAL_MERIDIAN
     * @see #FALSE_EASTING
     * @see #PROJECTION
     */
    public static final Key FALSE_NORTHING = new Key("false_northing");

    /**
     * Key for the minimal <var>x</var> value (western limit).
     * This is usually the longitude coordinate of the <em>upper left</em> corner.
     * The {@link MetadataReader#getEnvelope} method looks for this metadata in order
     * to set the {@linkplain Envelope#getMinimum minimal coordinate} for dimension
     * <strong>0</strong>.
     *
     * @see #X_MAXIMUM
     * @see #Y_MINIMUM
     * @see #Y_MAXIMUM
     * @see #X_RESOLUTION
     * @see #Y_RESOLUTION
     */
    public static final Key X_MINIMUM = new Key("XMinimum");

    /**
     * Key for the minimal <var>y</var> value (southern limit).
     * This is usually the latitude coordinate of the <em>bottom right</em> corner.
     * The {@link MetadataReader#getEnvelope} method looks for this metadata. in order
     * to set the {@linkplain Envelope#getMinimum minimal coordinate} for dimension
     * <strong>1</strong>.
     *
     * @see #X_MINIMUM
     * @see #X_MAXIMUM
     * @see #Y_MAXIMUM
     * @see #X_RESOLUTION
     * @see #Y_RESOLUTION
     */
    public static final Key Y_MINIMUM = new Key("YMinimum");

    /**
     * Key for the minimal <var>z</var> value. This is usually the minimal altitude.
     * The {@link MetadataReader#getEnvelope} method looks for this metadata in order
     * to set the {@linkplain Envelope#getMinimum minimal coordinate} for dimension
     * <strong>2</strong>.
     *
     * @see #Z_MAXIMUM
     * @see #Z_RESOLUTION
     * @see #DEPTH
     */
    public static final Key Z_MINIMUM = new Key("ZMinimum");

    /**
     * Key for the maximal <var>x</var> value (eastern limit).
     * This is usually the longitude coordinate of the <em>bottom right</em> corner.
     * The {@link MetadataReader#getEnvelope} method looks for this metadata in order
     * to set the {@linkplain Envelope#getMaximum maximal coordinate} for dimension
     * <strong>0</strong>.
     *
     * @see #X_MINIMUM
     * @see #Y_MINIMUM
     * @see #Y_MAXIMUM
     * @see #X_RESOLUTION
     * @see #Y_RESOLUTION
     */
    public static final Key X_MAXIMUM = new Key("XMaximum");

    /**
     * Key for the maximal <var>y</var> value (northern limit).
     * This is usually the latitude coordinate of the <em>upper left</em> corner.
     * The {@link MetadataReader#getEnvelope} method looks for this metadata in order
     * to set the {@linkplain Envelope#getMaximum maximal coordinate} for dimension
     * <strong>1</strong>.
     *
     * @see #X_MINIMUM
     * @see #X_MAXIMUM
     * @see #Y_MINIMUM
     * @see #X_RESOLUTION
     * @see #Y_RESOLUTION
     */
    public static final Key Y_MAXIMUM = new Key("YMaximum");

    /**
     * Key for the maximal <var>z</var> value. This is usually the maximal altitude.
     * The {@link MetadataReader#getEnvelope} method looks for this metadata in order
     * to set the {@linkplain Envelope#getMaximum maximal coordinate} for dimension
     * <strong>2</strong>.
     *
     * @see #Z_MINIMUM
     * @see #Z_RESOLUTION
     * @see #DEPTH
     */
    public static final Key Z_MAXIMUM = new Key("ZMaximum");

    /**
     * Key for the resolution among the <var>x</var> axis. The
     * {@link MetadataReader#getEnvelope} method looks for this metadata in order
     * to infer the coordinates for dimension <strong>0</strong>.
     *
     * @see #X_MINIMUM
     * @see #X_MAXIMUM
     * @see #Y_MINIMUM
     * @see #Y_MAXIMUM
     * @see #Y_RESOLUTION
     */
    public static final Key X_RESOLUTION = new Key("XResolution");

    /**
     * Key for the resolution among the <var>y</var> axis. The
     * {@link MetadataReader#getEnvelope} method looks for this metadata in order
     * to infer the coordinates for dimension <strong>1</strong>.
     *
     * @see #X_MINIMUM
     * @see #X_MAXIMUM
     * @see #Y_MINIMUM
     * @see #Y_MAXIMUM
     * @see #X_RESOLUTION
     * @see #WIDTH
     * @see #HEIGHT
     */
    public static final Key Y_RESOLUTION = new Key("YResolution");

    /**
     * Key for the resolution among the <var>z</var> axis. The
     * {@link MetadataReader#getEnvelope} method looks for this metadata in order to
     * infer the coordinates for dimension <strong>2</strong>.
     *
     * @see #Z_MINIMUM
     * @see #Z_MAXIMUM
     * @see #DEPTH
     */
    public static final Key Z_RESOLUTION = new Key("ZResolution");

    /**
     * Key for the direction among the <var>x</var> axis. The
     * {@link MetadataReader#getAxis} method looks for this metadata in order to
     * set its direction.
     */
    public static final Key X_DIRECTION = new Key("XDirection");

    /**
     * Key for the direction among the <var>y</var> axis. The
     * {@link MetadataReader#getAxis} method looks for this metadata in order to
     * set its direction.
     */
    public static final Key Y_DIRECTION = new Key("YDirection");

    /**
     * Key for the direction among the <var>z</var> axis. The
     * {@link MetadataReader#getAxis} method looks for this metadata in order to
     * set its direction.
     */
    public static final Key Z_DIRECTION = new Key("ZDirection");

    /**
     * Key for the image's width in pixels. The {@link MetadataReader#getGridRange}
     * method looks for this metadata in order to infer the
     * {@linkplain GridEnvelope#getSpan grid size} along the dimension <strong>0</strong>.
     *
     * @see #HEIGHT
     * @see #X_RESOLUTION
     * @see #Y_RESOLUTION
     */
    public static final Key WIDTH = new Key("Width");

    /**
     * Key for the image's height in pixels. The {@link MetadataReader#getGridRange}
     * method looks for this metadata in order to infer the
     * {@linkplain GridEnvelope#getSpan grid size} along the dimension <strong>1</strong>.
     *
     * @see #WIDTH
     * @see #X_RESOLUTION
     * @see #Y_RESOLUTION
     */
    public static final Key HEIGHT = new Key("Height");

    /**
     * Key for the image's "depth" in pixels. This metadata may exists for 3D images,
     * but some implementations accept at most 1 pixel depth among the third dimension.
     * The {@link MetadataReader#getGridRange} method looks for this metadata in order
     * to infer the {@linkplain GridEnvelope#getSpan grid size} along the dimension
     * <strong>2</strong>.
     *
     * @see #Z_MINIMUM
     * @see #Z_MAXIMUM
     * @see #Z_RESOLUTION
     */
    public static final Key DEPTH = new Key("Depth");

    /**
     * The source (the file path or the URL) specified during the last call to a
     * {@code load(...)} method.
     *
     * @see #load(File)
     * @see #load(URL)
     * @see #load(BufferedReader)
     */
    private String source;

    /**
     * The symbol to use as a separator. The full version ({@code separator}) will
     * be used for formatting with {@link #listMetadata}, while the trimed version
     * ({@code trimSeparator}) will be used for parsing with {@link #parseLine}.
     *
     * @see #getSeparator
     * @see #setSeparator
     */
    private String separator = " = ", trimSeparator = "=";

    /**
     * The non-localized pattern for formatting numbers (as floating point or as integer)
     * and dates. If {@code null}, then the default pattern is used.
     */
    private String numberPattern, datePattern;

    /**
     * The mapping between keys and alias, or {@code null} if there is no alias.
     * This mapping is used for two purpose:
     * <ul>
     *   <li>If the key is a {@link Key} object, then the value is the set of alias (as
     *       {@code AliasKey} objects) for this key. This set is used by {@code getXXX()}
     *       methods.</li>
     *   <li>If the key is an {@code AliasKey} object, then the value if the set of
     *       {@link Key} which have this alias. This set is used by {@code add(...)}
     *       methods in order to check for ambiguity when adding a new metadata.</li>
     * </ul>
     */
    private Map<String, Key> naming;

    /**
     * The factories to use for constructing ellipsoids, projections, coordinate
     * reference systems...
     */
    private final ReferencingFactoryContainer factories;

    /**
     * The locale to use for formatting messages, or {@code null} for a default locale.
     * This is <strong>not</strong> the local to use for parsing the file. This later
     * locale is specified by {@link #getLocale}.
     */
    private Locale userLocale;

    /**
     * Constructs a new {@code MetadataReader} using default factories.
     */
    public TextMetadataParser() {
        this(ReferencingFactoryContainer.instance(null));
    }

    /**
     * Constructs a new {@code MetadataReader} using the specified factories.
     */
    public TextMetadataParser(final ReferencingFactoryContainer factories) {
        this.factories = factories;
    }

    /**
     * Returns the characters to use as separator between keys and values. Leading
     * and trailing spaces will be keept when formatting with {@link #listMetadata},
     * but will be ignored when parsing with {@link #parseLine}. The default value
     * is <code>"&nbsp;=&nbsp;"</code>.
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * Set the characters to use as separator between keys and values.
     */
    public synchronized void setSeparator(final String separator) {
        this.trimSeparator = separator.trim();
        this.separator     = separator;
    }

    /**
     * Returns the pattern used for parsing and formatting values of the specified type.
     * The type should be either {@code Number.class} or {@code Date.class}.
     * <p>
     * <ul>
     *   <li>if {@code type} is assignable to {@code Number.class}, then this method
     *       returns the number pattern as specified by {@link DecimalFormat}.</li>
     *   <li>Otherwise, if {@code type} is assignable to {@code Date.class}, then
     *       this method returns the date pattern as specified by
     *       {@link SimpleDateFormat}.</li>
     * </ul>
     * <p>
     * In any case, this method returns {@code null} if this object should use the
     * default pattern for the {@linkplain #getLocale data locale}.
     *
     * @param  type The data type ({@code Number.class} or {@code Date.class}).
     * @return The format pattern for the specified data type, or {@code null} for
     *         the default locale-dependent pattern.
     * @throws IllegalArgumentException if {@code type} is not valid.
     */
    public String getFormatPattern(final Class<?> type) {
        if (Date.class.isAssignableFrom(type)) {
            return datePattern;
        }
        if (Number.class.isAssignableFrom(type)) {
            return numberPattern;
        }
        throw new IllegalArgumentException(Errors.format(ErrorKeys.UNKNOW_TYPE_$1, type));
    }

    /**
     * Set the pattern to use for parsing and formatting values of the specified type.
     * The type should be either {@code Number.class} or {@code Date.class}.
     *
     * <ul>
     *   <li>If {@code type} is assignable to <code>{@linkplain java.lang.Number}.class</code>,
     *       then {@code pattern} should be a {@link DecimalFormat} pattern (example:
     *       {@code "#0.###"}).</li>
     *   <li>If {@code type} is assignable to <code>{@linkplain Date}.class</code>,
     *       then {@code pattern} should be a {@link SimpleDateFormat} pattern
     *       (example: {@code "yyyy/MM/dd HH:mm"}).</li>
     * </ul>
     *
     * @param  type The data type ({@code Number.class} or {@code Date.class}).
     * @param  pattern The format pattern for the specified data type, or {@code null}
     *         for the default locale-dependent pattern.
     * @throws IllegalArgumentException if {@code type} is not valid.
     */
    public synchronized void setFormatPattern(final Class<?> type, final String pattern) {
        if (Date.class.isAssignableFrom(type)) {
            datePattern = pattern;
            return;
        }
        if (Number.class.isAssignableFrom(type)) {
            numberPattern = pattern;
            return;
        }
        throw new IllegalArgumentException(Errors.format(ErrorKeys.UNKNOW_TYPE_$1, type));
    }

    /**
     * Clears this metadata set. If the same {@code MetadataReader} object is used
     * for parsing many files, then {@code clear()} should be invoked prior any
     * {@code load(...)} method.
     * Note that {@code clear()} do not remove any alias, so this {@code MetadataReader}
     * can be immediately reused for parsing new files of the same kind.
     */
    public synchronized void clear() {
        source = null;
    }

    /**
     * Reads all metadata from a text file. The default implementation invokes
     * {@link #load(BufferedReader)}. Note that this method do not invokes {@link #clear}
     * prior the loading. Consequently, the loaded metadata will be added to the set of
     * existing metadata.
     *
     * @param  header The file to read until EOF.
     * @throws IOException if an error occurs during loading.
     *
     * @see #clear()
     * @see #load(URL)
     * @see #parseLine
     * @see #getSource
     */
    public synchronized void load(final File header) throws IOException {
        source = header.getPath();
        final BufferedReader in = new BufferedReader(new FileReader(header));
        load(in);
        in.close();
    }

    /**
     * Reads all metadata from an URL. The default implementation invokes
     * {@link #load(BufferedReader)}. Note that this method do not invokes {@link #clear}
     * prior the loading. Consequently, the loaded metadata will be added to the set of
     * existing metadata.
     *
     * @param  header The URL to read until EOF.
     * @throws IOException if an error occurs during loading.
     *
     * @see #clear()
     * @see #load(File)
     * @see #parseLine
     * @see #getSource
     */
    public synchronized void load(final URL header) throws IOException {
        source = header.getPath();
        final BufferedReader in = new BufferedReader(
                new InputStreamReader(header.openStream()));
        load(in);
        in.close();
    }

    /**
     * Reads all metadata from a stream. The default implementation invokes
     * {@link #parseLine} for each non-empty line found in the stream. Notes:
     * <p>
     * <ul>
     *   <li>This method is not public because it has no way to know how
     *       to set the {@link #getSource source} metadata.</li>
     *   <li>This method is not synchronized. Synchronization, if wanted,
             must be done from the public frontend.</li>
     *   <li>This method do not invokes {@link #clear} prior the loading.</li>
     * </ul>
     *
     * @param in The stream to read until EOF. The stream will not be closed.
     * @throws IOException if an error occurs during loading.
     *
     * @see #clear()
     * @see #load(File)
     * @see #load(URL)
     * @see #parseLine
     */
    protected void load(final BufferedReader in) throws IOException {
        assert Thread.holdsLock(this);
        final Set<String> previousComments = new HashSet<String>();
        final StringBuilder comments = new StringBuilder();
        final String lineSeparator = System.getProperty("line.separator", "\n");
        String line; while ((line=in.readLine())!=null) {
            if (line.trim().length()!=0) {
                if (!parseLine(line)) {
                    if (previousComments.add(line)) {
                        comments.append(line);
                        comments.append(lineSeparator);
                    }
                }
            }
        }
        if (comments.length() != 0) {
            add((String) null, comments.toString());
        }
        putDone();
    }

    /**
     * Parses a line and add the key-value pair to this metadata set. The default
     * implementation takes the substring on the left side of the first occurence
     * of the {@linkplain #getSeparator separator} (usually the '=' character) as
     * the key, and the substring on the right side of the separator as the value.
     * For example, if {@code line} has the following value:
     *
     * <blockquote><pre>
     * Ellipsoid = WGS 1984
     * </pre></blockquote>
     *
     * Then, the default implementation will translate this line in
     * the following call:
     *
     * <blockquote><pre>
     * {@link #add(String,Object) add}("Ellipsoid", "WGS 1984");
     * </pre></blockquote>
     *
     * This method returns {@code true} if it has consumed the line, or {@code false}
     * otherwise.
     * A line is "consumed" if {@code parseLine(...)} has either added the key-value
     * pair (using {@link #add}), or determined that the line must be ignored (for
     * example because {@code parseLine(...)} detected a character announcing a
     * comment line). A "consumed" line will not receive any further treatment. The
     * line is not consumed (i.e. this method returns {@code false}) if
     * {@code parseLine(...)} don't know what to do with it. Non-consumed line will
     * typically go up in a chain of {@code parseLine(...)} methods (if
     * {@code MetadataReader} has been subclassed) until someone consume it.
     *
     * @param  line The line to parse.
     * @return {@code true} if this method has consumed the line.
     * @throws IIOException if the line is badly formatted.
     * @throws AmbiguousMetadataException if a different value was already defined
     *         for the same metadata name.
     *
     * @see #load(File)
     * @see #load(URL)
     * @see #add(String,Object)
     */
    protected boolean parseLine(final String line) throws IIOException {
        final int index = line.indexOf(trimSeparator);
        if (index >= 0) {
            add(line.substring(0, index), line.substring(index+1));
            return true;
        }
        return false;
    }

    /**
     * Add all metadata from the specified image.
     *
     * @param  image The image with metadata to add to this {@code MetadataReader}.
     * @throws AmbiguousMetadataException if a metadata is defined twice.
     *
     * @see #add(GridCoverage)
     * @see #add(PropertySource,String)
     * @see #add(String,Object)
     */
    public synchronized void add(final RenderedImage image)
            throws AmbiguousMetadataException
    {
        if (image instanceof PropertySource) {
            // This version allow the use of deferred properties.
            add((PropertySource) image, null);
        } else {
            final String[] names = image.getPropertyNames();
            if (names != null) {
                for (int i=0; i<names.length; i++) {
                    final String name = names[i];
                    add(name, image.getProperty(name));
                }
            }
        }
    }

    /**
     * Add metadata from the specified property source.
     *
     * @param  properties The properties source.
     * @param  prefix The prefix for properties to add, of {@code null} to add
     *         all properties. If non-null, only properties begining with this prefix
     *         will be added.
     * @throws AmbiguousMetadataException if a metadata is defined twice.
     *
     * @see #add(GridCoverage)
     * @see #add(RenderedImage)
     * @see #add(String,Object)
     */
    public synchronized void add(final PropertySource properties, final String prefix)
            throws AmbiguousMetadataException
    {
        final String[] names = (prefix!=null) ? properties.getPropertyNames(prefix) :
                                                properties.getPropertyNames();
        if (names != null) {
            for (int i=0; i<names.length; i++) {
                final String  name = names[i];
                final Class<?> classe = properties.getPropertyClass(name);
                add(name, new DeferredProperty(properties, name, classe));
            }
        }
    }

    /**
     * Add a metadata for the specified key. Keys are case-insensitive, ignore leading
     * and trailing whitespaces and consider any other whitespace sequences as equal
     * to a single {@code '_'} character.
     *
     * @param  alias The key for the metadata to add. This is usually the name found
     *         in the file to be parsed (this is different from {@link Key} objects,
     *         which are keys in a format neutral way). This key is usually, but not
     *         always, one of the alias defined with {@link #addAlias}.
     * @param  value The value for the metadata to add. If {@code null} or
     *         {@link Image#UndefinedProperty}, then this method do nothing.
     * @throws AmbiguousMetadataException if a different value already exists for the
     *         specified alias, or for an other alias bound to the same {@link Key}.
     *
     * @see #add(GridCoverage)
     * @see #add(RenderedImage)
     * @see #add(PropertySource,String)
     * @see #parseLine
     */
    public synchronized void add(String alias, final Object value)
            throws AmbiguousMetadataException
    {
        final Key aliasAsKey;
        if (alias != null) {
            alias = alias.trim();
            aliasAsKey = new Key(alias);
        }
        else {
            aliasAsKey = null;
        }
        add(aliasAsKey, value);
    }

    /**
     * Implementation of the {@link #add(String, Object)} method. This method is invoked by
     * {@link #add(GridCoverage)}, which iterates through each {@link AliasKey} declared in
     * {@link #naming}.
     */
    private void add(final Key aliasAsKey, Object value)
            throws AmbiguousMetadataException
    {
        assert isValid();
        if (value == null || value == Image.UndefinedProperty) {
            return;
        }
        if (value instanceof CharSequence) {
            final String text = trim(value.toString().trim(), " ");
            if (text.length() == 0) return;
            value = text;
        }
        put(aliasAsKey, value);
    }

    /**
     * Add an alias to a key. After this method has been invoked, calls to
     * <code>{@link #get get}(key)</code> will really looks for metadata named
     * {@code alias}. Alias are mandatory in order to get various {@code getXXX()}
     * methods to work for a particular file format.
     * <p>
     * For example if the file to be parsed uses the names {@code "ULX"} and
     * {@code "ULY"} for the coordinate of the upper left corner, then the
     * {@link #getEnvelope} method will not work unless the following alias are set:
     *
     * <blockquote><pre>
     * addAlias({@linkplain #X_MINIMUM}, "ULX");
     * addAlias({@linkplain #Y_MAXIMUM}, "ULY");
     * </pre></blockquote>
     *
     * An arbitrary number of alias can be set for the same key. For example,
     * <code>addAlias(Y_MAXIMUM,&nbsp;...)</code> could be invoked twice with
     * {@code "ULY"} and {@code "Limit North"} alias. The {@code getXXX()} methods will
     * try alias in the order they were added and use the first value found.
     * <p>
     * The same alias can also be set to more than one key. For example, the following
     * code is legal. It means that pixel are square with the same horizontal and
     * vertical resolution:
     *
     * <blockquote><pre>
     * addAlias({@linkplain #X_RESOLUTION}, "Resolution");
     * addAlias({@linkplain #Y_RESOLUTION}, "Resolution");
     * </pre></blockquote>
     *
     * @param  key The key to add an alias. This key is format neutral.
     * @param  alias The alias to add. This is the name actually used in the file to
     *         be parsed. Alias are case insensitive and ignore multiple whitespace,
     *         like keys. If this alias is already bound to the specified key, then
     *         this method do nothing.
     * @throws AmbiguousMetadataException if the addition of the supplied alias
     *         would introduce an ambiguity in the current set of metadata.
     *         This occurs if the key has already an alias mapping to a different value.
     *
     * @see #getAlias
     * @see #contains
     * @see #get
     */
    public synchronized void addAlias(final Key key, String alias)
            throws AmbiguousMetadataException
    {
        alias = trim(alias.trim(), " ");
        final Key aliasAsKey = new Key(alias);
        if (naming == null) {
            naming = new LinkedHashMap<String,Key>();
        }
        // Add the alias for the specified key. This is the information
        // used by 'get' methods for fetching a metadata from a key.
        Key keyFound = naming.get(aliasAsKey);
        if (keyFound == null) {
            keyFound = new Key(alias);
            naming.put(alias, keyFound);
        }
        assert isValid();
    }

    /**
     * Checks if this object is in a valid state. {@link #naming} should
     * contains a key for every values in all {@link Set} objects.
     */
    private boolean isValid() {
        assert Thread.holdsLock(this);
        if (naming != null) {
            for (final Key key : naming.values()) {
                if (!naming.keySet().contains(key.name)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns the specified value as a string.
     *
     * @param  value The value to cast.
     * @param  key The key, for formatting error message if needed.
     * @param  alias The alias, for formatting error message if needed.
     * @return The value as a string.
     * @throws MetadataException if the value can't be cast to a string.
     */
    private String toString(final Object value, final Key key, final String alias)
            throws MetadataException
    {
        if (value == null) {
            return null;
        }
        if (value instanceof CharSequence) {
            return value.toString();
        }
        if (value instanceof IdentifiedObject) {
            return ((IdentifiedObject) value).getName().getCode();
        }
        throw new MetadataException(Errors.getResources(userLocale).getString(
              ErrorKeys.CANT_CONVERT_FROM_TYPE_$1, Classes.getClass(value)), key, alias);
    }

    /**
     * Returns the list of alias for the specified key, or {@code null}
     * if the key has no alias. Alias are the names used in the underlying
     * metadata file, and are format dependent.
     *
     * @param  key The format neutral key.
     * @return The alias for the specified key, or {@code null} if none.
     *
     * @see #addAlias
     */
    public synchronized Set<String> getAlias(final Key key) {
        assert isValid();
        if (naming != null) {
            final Set<Entry<String, Key>> entries = naming.entrySet();
            if (entries != null) {
                final Set<String> alias = new HashSet<String>();
                for (final String aliasKey : alias) {
                    if (key.name.equalsIgnoreCase(aliasKey)) {
                        alias.add(aliasKey);
                    }
                }
                return alias;
            }
        }
        return null;
    }

    /**
     * Returns the source file name or URL. This is the path specified
     * during the last call to a {@code load(...)} method.
     *
     * @return The source file name or URL.
     * @throws MetadataException if this information can't be fetched.
     *
     * @link #load(File)
     * @link #load(URL)
     */
    public String getSource() throws MetadataException {
        return source;
    }

    /**
     * Returns the locale to use when parsing metadata values as numbers, angles or dates.
     * This is <strong>not</strong> the locale used for formatting error messages, if any.
     * The default implementation returns {@link Locale#US}, since it is the format used
     * in most data file.
     *
     * @return The locale to use for parsing metadata values.
     * @throws MetadataException if this information can't be fetched.
     *
     * @see #getAsDouble
     * @see #getAsInt
     * @see #getAsDate
     */
    public Locale getLocale() {
        return Locale.US;
    }

    /**
     * Sets the current {@link Locale} of this {@code MetadataReader}
     * to the given value. A value of {@code null} removes any previous
     * setting, and indicates that the parser should localize as it sees fit.
     * <p>
     * <strong>Note:</strong> this is the locale to use for formatting error messages,
     * not the locale to use for parsing the file. The locale for parsing is specified
     * by {@link #getLocale}.
     */
    final synchronized void setUserLocale(final Locale locale) {
        userLocale = locale;
    }

    protected GeographicMetadata getGeographicMetadata() {
        return metadata;
    }

    protected void setGeographicMetadata(final GeographicMetadata metadata) {
        this.metadata = metadata;
    }

    /**
     * Put the specified value in the right node of the metadata tree. This part is left to
     * subclasses in order to provide different tree structure.
     *
     * @param key   The alias of the key to add.
     * @param value The value to add in the metadata tree.
     */
    protected abstract void put(final Key key, final Object value);

    /**
     * Should be launched after the {@link #put(Key, Object)} method has been done. It will
     * add axes according to the dimension defined, and sets grid range and offset vectors
     * for all dimensions defined.
     */
    protected abstract void putDone();

    /**
     * Returns a string representation of this metadata set. The default implementation
     * write the class name and the envelope in geographic coordinates, as returned by
     * {@link #getGeographicBoundingBox}. Then, it append the list of all metadata as
     * formatted by {@link #listMetadata}.
     */
    @Override
    public String toString() {
        final String lineSeparator = System.getProperty("line.separator", "\n");
        final StringWriter  buffer = new StringWriter();
        if (source != null) {
            buffer.write("[\"");
            buffer.write(source);
            buffer.write("\"]");
        }
        buffer.write(lineSeparator);
        buffer.write('{');
        buffer.write(lineSeparator);
        try {
            final TableWriter table = new TableWriter(buffer, 2);
            table.setMultiLinesCells(true);
            table.nextColumn();
            table.write(OptionalDependencies.toString(
                    OptionalDependencies.xmlToSwing(
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

    /**
     * A key for fetching metadata in a format independent way. For example, the northern
     * limit of an image way be named <code>"Limit North"</code> is some metadata files,
     * and <code>"ULY"</code> (as <cite>Upper Left Y</cite>) in other metadata files. The
     * {@link MetadataReader#Y_MAXIMUM} allows to fetch this metadata without knowledge of
     * the actual name used in the underlying metadata file.
     * <p>
     * Keys are case-insensitive. Furthermore, trailing and leading spaces are ignored.
     * Any succession of one ore more unicode whitespace characters (as of
     * {@link java.lang.Character#isSpaceChar(char)} is understood as equal to a single
     * <code>'_'</code> character. For example, the key <code>"false&nbsp;&nbsp;easting"</code>
     * is considered equals to <code>"false_easting"</code>.
     *
     * @version $Id$
     * @author Martin Desruisseaux (IRD)
     */
    public static class Key implements Serializable {
        /**
         * Serial number for interoperability with different versions.
         */
        private static final long serialVersionUID = -6197070349689520675L;

        /**
         * The original name, as specified by the user.
         */
        private final String name;

        /**
         * The trimed name in lower case. This
         * is the key to use in comparaisons.
         */
        private final String key;

        /**
         * Construct a new key.
         *
         * @param name The key name.
         */
        public Key(String name) {
            name = name.trim();
            this.name = name;
            this.key  = trim(name, "_").toLowerCase();
        }

        /**
         * Returns the name for this key. This is the name supplied to the constructor
         * (i.e. case and whitespaces are preserved).
         */
        @Override
        public String toString() {
            return name;
        }

        /**
         * Returns a hash code value.
         */
        @Override
        public int hashCode() {
            return key.hashCode();
        }

        /**
         * Compare this key with the supplied key for equality. Comparaison is
         * case-insensitive and considere any sequence of whitespaces as a single
         * <code>'_'</code> character, as specified in this class documentation.
         */
        @Override
        public boolean equals(final Object object) {
            return (object!=null) && object.getClass().equals(getClass()) &&
                    key.equals(((Key) object).key);
        }
    }
}
