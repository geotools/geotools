/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.wkt;

import java.io.BufferedReader;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Length;
import javax.measure.quantity.Quantity;
import static java.util.Collections.singletonMap;

import org.opengis.metadata.citation.Citation;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.NoSuchIdentifierException;

// While start import is usually a deprecated practice, we use such a large amount
// of interfaces in those packages that it we choose to exceptionnaly use * here.
import org.opengis.referencing.cs.*;
import org.opengis.referencing.crs.*;
import org.opengis.referencing.datum.*;
import org.opengis.referencing.operation.*;

import org.geotools.factory.Hints;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.datum.BursaWolfParameters;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.referencing.datum.DefaultPrimeMeridian;
import org.geotools.referencing.datum.DefaultVerticalDatum;
import org.geotools.referencing.cs.AbstractCS;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotools.referencing.cs.DirectionAlongMeridian;
import org.geotools.referencing.factory.ReferencingFactoryContainer;
import org.geotools.referencing.operation.DefiningConversion;
import org.geotools.resources.Arguments;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * Parser for
 * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
 * Known Text</cite> (WKT)</A>. This parser can parse {@linkplain MathTransform math transform}
 * objects as well, which is part of the WKT's {@code FITTED_CS} element.
 *
 * @since 2.0
 *
 * @source $URL$
 * @version $Id$
 * @author Remi Eve
 * @author Martin Desruisseaux (IRD)
 *
 * @see <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html">Well Know Text specification</A>
 * @see <A HREF="http://gdal.velocet.ca/~warmerda/wktproblems.html">OGC WKT Coordinate System Issues</A>
 */
public class Parser extends MathTransformParser {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = -144097689843465085L;

    /**
     * {@code true} in order to allows the non-standard Oracle syntax. Oracle put the Bursa-Wolf
     * parameters straight into the {@code DATUM} elements, without enclosing them in a
     * {@code TOWGS84} element.
     */
    private static final boolean ALLOW_ORACLE_SYNTAX = true;

    /**
     * The mapping between WKT element name and the object class to be created.
     * Will be created by {@link #getTypeMap} only when first needed. Keys must
     * be upper case.
     */
    private static Map<String,Class<?>> TYPES;

    /**
     * The factory to use for creating {@linkplain Datum datum}.
     */
    protected final DatumFactory datumFactory;

    /**
     * The factory to use for creating {@linkplain CoordinateSystem coordinate systems}.
     */
    protected final CSFactory csFactory;

    /**
     * The factory to use for creating {@linkplain CoordinateReferenceSystem
     * coordinate reference systems}.
     */
    protected final CRSFactory crsFactory;

    /**
     * The list of {@linkplain AxisDirection axis directions} from their name.
     */
    private final Map<String,AxisDirection> directions;

    /**
     * Constructs a parser using the default set of symbols and factories.
     */
    public Parser() {
        this(Symbols.DEFAULT);
    }

    /**
     * Constructs a parser for the specified set of symbols using default factories.
     *
     * @param symbols The symbols for parsing and formatting numbers.
     *
     * @todo Pass hints in argument.
     */
    public Parser(final Symbols symbols) {
        this(symbols,
             ReferencingFactoryFinder.getDatumFactory        (null),
             ReferencingFactoryFinder.getCSFactory           (null),
             ReferencingFactoryFinder.getCRSFactory          (null),
             ReferencingFactoryFinder.getMathTransformFactory(null));
    }

    /**
     * Constructs a parser for the specified set of symbols using the specified set of factories.
     *
     * @param symbols   The symbols for parsing and formatting numbers.
     * @param factories The factories to use.
     */
    public Parser(final Symbols symbols, final ReferencingFactoryContainer factories) {
        this(symbols,
             factories.getDatumFactory(),
             factories.getCSFactory(),
             factories.getCRSFactory(),
             factories.getMathTransformFactory());
    }

    /**
     * Constructs a parser for the specified set of symbols using the specified factories.
     *
     * @param symbols      The symbols for parsing and formatting numbers.
     * @param datumFactory The factory to use for creating {@linkplain Datum datum}.
     * @param csFactory    The factory to use for creating {@linkplain CoordinateSystem
     *                     coordinate systems}.
     * @param crsFactory   The factory to use for creating {@linkplain CoordinateReferenceSystem
     *                     coordinate reference systems}.
     * @param mtFactory    The factory to use for creating {@linkplain MathTransform
     *                     math transform} objects.
     */
    public Parser(final Symbols                symbols,
                  final DatumFactory      datumFactory,
                  final CSFactory            csFactory,
                  final CRSFactory          crsFactory,
                  final MathTransformFactory mtFactory)
    {
        super(symbols, mtFactory);
        this.datumFactory = datumFactory;
        this. csFactory   =    csFactory;
        this.crsFactory   =   crsFactory;
        final AxisDirection[] values = AxisDirection.values();
        directions = new HashMap<String,AxisDirection>(
                (int) Math.ceil((values.length + 1) / 0.75f), 0.75f);
        for (int i=0; i<values.length; i++) {
            directions.put(values[i].name().trim().toUpperCase(), values[i]);
        }
    }

    /**
     * Parses a coordinate reference system element.
     *
     * @param  text The text to be parsed.
     * @return The coordinate reference system.
     * @throws ParseException if the string can't be parsed.
     */
    public CoordinateReferenceSystem parseCoordinateReferenceSystem(final String text)
            throws ParseException
    {
        final Element element = getTree(text, new ParsePosition(0));
        final CoordinateReferenceSystem crs = parseCoordinateReferenceSystem(element);
        element.close();
        return crs;
    }

    /**
     * Parses a coordinate reference system element.
     *
     * @param  parent The parent element.
     * @return The next element as a {@link CoordinateReferenceSystem} object.
     * @throws ParseException if the next element can't be parsed.
     */
    private CoordinateReferenceSystem parseCoordinateReferenceSystem(final Element element)
            throws ParseException
    {
        final Object key = element.peek();
        if (key instanceof Element) {
            final String keyword = ((Element) key).keyword.trim().toUpperCase(symbols.locale);
            CoordinateReferenceSystem r = null;
            try {
                if (   "GEOGCS".equals(keyword)) return r=parseGeoGCS  (element);
                if (   "PROJCS".equals(keyword)) return r=parseProjCS  (element);
                if (   "GEOCCS".equals(keyword)) return r=parseGeoCCS  (element);
                if (  "VERT_CS".equals(keyword)) return r=parseVertCS  (element);
                if ( "LOCAL_CS".equals(keyword)) return r=parseLocalCS (element);
                if ( "COMPD_CS".equals(keyword)) return r=parseCompdCS (element);
                if ("FITTED_CS".equals(keyword)) return r=parseFittedCS(element);
            } finally {
                // Work around for simulating post-conditions in Java.
                assert isValid(r, keyword) : element;
            }
        }
        throw element.parseFailed(null, Errors.format(ErrorKeys.UNKNOW_TYPE_$1, key));
    }

    /**
     * Parses the next element in the specified <cite>Well Know Text</cite> (WKT) tree.
     *
     * @param  element The element to be parsed.
     * @return The object.
     * @throws ParseException if the element can't be parsed.
     *
     * @todo All sequences of <code>if ("FOO".equals(keyword))</code> in this method
     *       and other methods of this class and subclasses, could be optimized with
     *       a {@code switch} statement.
     */
    @Override
    protected Object parse(final Element element) throws ParseException {
        final Object key = element.peek();
        if (key instanceof Element) {
            final String keyword = ((Element) key).keyword.trim().toUpperCase(symbols.locale);
            Object r = null;
            try {
                if (       "AXIS".equals(keyword)) return r=parseAxis      (element, SI.METER, true);
                if (     "PRIMEM".equals(keyword)) return r=parsePrimem    (element, NonSI.DEGREE_ANGLE);
                if (    "TOWGS84".equals(keyword)) return r=parseToWGS84   (element);
                if (   "SPHEROID".equals(keyword)) return r=parseSpheroid  (element);
                if ( "VERT_DATUM".equals(keyword)) return r=parseVertDatum (element);
                if ("LOCAL_DATUM".equals(keyword)) return r=parseLocalDatum(element);
                if (      "DATUM".equals(keyword)) return r=parseDatum     (element, DefaultPrimeMeridian.GREENWICH);
                r = parseMathTransform(element, false);
                if (r != null) {
                    return r;
                }
            } finally {
                // Work around for simulating post-conditions in Java.
                assert isValid(r, keyword) : element;
            }
        }
        return parseCoordinateReferenceSystem(element);
    }

    /**
     * Checks if the parsed object is of the expected type. This is also a way to check
     * the consistency of the {@link #TYPES} map.
     */
    private static boolean isValid(final Object parsed, final String keyword) {
        if (parsed == null) {
            // Required in order to avoid AssertionError in place of ParseException.
            return true;
        }
        final Class type = getClassOf(keyword);
        return type!=null && type.isInstance(parsed);
    }

    /**
     * Returns the properties to be given to the parsed object. This method is invoked
     * automatically by the parser for the {@linkplain Element#isRoot root element} only.
     * This method expect on input the properties parsed from the {@code AUTHORITY} element,
     * and returns on output the properties to give to the object to be created. The default
     * implementation returns the {@code properties} map unchanged. Subclasses may override
     * this method in order to add or change properties.
     * <p>
     * <strong>Example:</strong> if a subclass want to add automatically an authority code when
     * no {@code AUTHORITY} element was explicitly set in the WKT, then it may test for the
     * {@link IdentifiedObject#IDENTIFIERS_KEY} key and add automatically an entry if this
     * key was missing.
     *
     * @param  properties The properties parsed from the WKT file. Entries can be added, removed
     *         or modified directly in this map.
     * @return The properties to be given to the parsed object. This is usually {@code properties}
     *         (maybe after modifications), but could also be a new map.
     *
     * @since 2.3
     */
    protected Map<String,Object> alterProperties(final Map<String,Object> properties) {
        return properties;
    }

    /**
     * Parses an <strong>optional</strong> "AUTHORITY" element.
     * This element has the following pattern:
     *
     * <blockquote><code>
     * AUTHORITY["&lt;name&gt;", "&lt;code&gt;"]
     * </code></blockquote>
     * or even
     * <blockquote><code>
     * AUTHORITY["&lt;name&gt;", &lt;code&gt;]
     * </code></blockquote>
     *
     * @param  parent The parent element.
     * @param  name The name of the parent object being parsed.
     * @return A properties map with the parent name and the optional autority code.
     * @throws ParseException if the "AUTHORITY" can't be parsed.
     */
    private Map<String,Object> parseAuthority(final Element parent, final String name)
            throws ParseException
    {
        final boolean  isRoot = parent.isRoot();
        final Element element = parent.pullOptionalElement("AUTHORITY");
        Map<String,Object> properties;
        if (element == null) {
            if (isRoot) {
                properties = new HashMap<String,Object>(4);
                properties.put(IdentifiedObject.NAME_KEY, name);
            } else {
                properties = singletonMap(IdentifiedObject.NAME_KEY, (Object) name);
            }
        } else {
            final String auth = element.pullString("name");
            // the code can be annotation marked but could be a number to
            String code = element.pullOptionalString("code");
            if (code == null) {
            	int codeNumber = element.pullInteger("code");
            	code = String.valueOf(codeNumber);
            }
            element.close();
            final Citation authority = Citations.fromName(auth);
            properties = new HashMap<String,Object>(4);
            properties.put(IdentifiedObject.       NAME_KEY, new NamedIdentifier(authority, name));
            properties.put(IdentifiedObject.IDENTIFIERS_KEY, new NamedIdentifier(authority, code));
        }
        if (isRoot) {
            properties = alterProperties(properties);
        }
        return properties;
    }

    /**
     * Parses an "UNIT" element.
     * This element has the following pattern:
     *
     * <blockquote><code>
     * UNIT["<name>", <conversion factor> {,<authority>}]
     * </code></blockquote>
     *
     * @param  parent The parent element.
     * @param  unit The contextual unit. Usually {@link SI#METRE} or {@link SI#RADIAN}.
     * @return The "UNIT" element as an {@link Unit} object.
     * @throws ParseException if the "UNIT" can't be parsed.
     *
     * @todo Authority code is currently ignored. We may consider to create a subclass of
     *       {@link Unit} which implements {@link IdentifiedObject} in a future version.
     */
    private <T extends Quantity> Unit<T> parseUnit(final Element parent, final Unit<T> unit)
            throws ParseException
    {
        final Element element = parent.pullElement("UNIT");
        final String     name = element.pullString("name");
        final double   factor = element.pullDouble("factor");
        final Map<String,?> properties = parseAuthority(element, name);
        element.close();
        return (factor != 1) ? unit.times(factor) : unit;
    }

    /**
     * Parses an "AXIS" element.
     * This element has the following pattern:
     *
     * <blockquote><code>
     * AXIS["<name>", NORTH | SOUTH | EAST | WEST | UP | DOWN | OTHER]
     * </code></blockquote>
     *
     * Note: there is no AUTHORITY element for AXIS element in OGC specification. However, we
     *       accept it anyway in order to make the parser more tolerant to non-100% compliant
     *       WKT. Note that AXIS is really the only element without such AUTHORITY clause and
     *       the EPSG database provides authority code for all axis.
     *
     * @param  parent The parent element.
     * @param  unit The contextual unit. Usually {@link NonSI#DEGREE_ANGLE} or {@link SI#METRE}.
     * @param  required {@code true} if the axis is mandatory,
     *         or {@code false} if it is optional.
     * @return The "AXIS" element as a {@link CoordinateSystemAxis} object, or {@code null}
     *         if the axis was not required and there is no axis object.
     * @throws ParseException if the "AXIS" element can't be parsed.
     */
    private CoordinateSystemAxis parseAxis(final Element parent,
                                           final Unit<?> unit,
                                           final boolean required)
            throws ParseException
    {
        final Element element;
        if (required) {
            element = parent.pullElement("AXIS");
        } else {
            element = parent.pullOptionalElement("AXIS");
            if (element == null) {
                return null;
            }
        }
        final String  name        = element.pullString     ("name");
        final Element orientation = element.pullOptionalVoidElement();
        final AxisDirection direction;
        if(orientation != null) {
            direction = directions.get(orientation.keyword.trim().toUpperCase());
        } else {
            String directionName = element.pullString("orientation");
            direction = DirectionAlongMeridian.parse(directionName).getDirection();
        }
        final Map<String,?> properties = parseAuthority(element, name); // See javadoc
        element.close();
        
        if (direction == null) {
            throw element.parseFailed(null, Errors.format(ErrorKeys.UNKNOW_TYPE_$1, orientation));
        }
        try {
            return createAxis(properties, name, direction, unit);
        } catch (FactoryException exception) {
            throw element.parseFailed(exception, null);
        }
    }

    /**
     * Creates an axis. If the name matches one of pre-defined axis, the pre-defined one
     * will be returned. This replacement help to get more success when comparing a CS
     * built from WKT against a CS built from one of Geotools's constants.
     *
     * @param  properties Name and other properties to give to the new object.
     *         If {@code null}, the abbreviation will be used as the axis name.
     * @param  abbreviation The coordinate axis abbreviation.
     * @param  direction The axis direction.
     * @param  unit The coordinate axis unit.
     * @throws FactoryException if the axis can't be created.
     */
    private CoordinateSystemAxis createAxis(Map<String,?> properties,
                                      final String        abbreviation,
                                      final AxisDirection direction,
                                      final Unit<?>       unit)
            throws FactoryException
    {
        final CoordinateSystemAxis candidate =
                DefaultCoordinateSystemAxis.getPredefined(abbreviation, direction);
        if (candidate != null && unit.equals(candidate.getUnit())) {
            return candidate;
        }
        if (properties == null) {
            properties = singletonMap(IdentifiedObject.NAME_KEY, abbreviation);
        }
        return csFactory.createCoordinateSystemAxis(properties, abbreviation, direction, unit);
    }

    /**
     * Parses a "PRIMEM" element. This element has the following pattern:
     *
     * <blockquote><code>
     * PRIMEM["<name>", <longitude> {,<authority>}]
     * </code></blockquote>
     *
     * @param  parent The parent element.
     * @param  angularUnit The contextual unit.
     * @return The "PRIMEM" element as a {@link PrimeMeridian} object.
     * @throws ParseException if the "PRIMEM" element can't be parsed.
     */
    private PrimeMeridian parsePrimem(final Element parent, final Unit<Angle> angularUnit)
            throws ParseException
    {
        final Element   element = parent.pullElement("PRIMEM");
        final String       name = element.pullString("name");
        final double  longitude = element.pullDouble("longitude");
        final Map<String,?> properties = parseAuthority(element, name);
        element.close();
        try {
            return datumFactory.createPrimeMeridian(properties, longitude, angularUnit);
        } catch (FactoryException exception) {
            throw element.parseFailed(exception, null);
        }
    }

    /**
     * Parses an <strong>optional</strong> "TOWGS84" element.
     * This element has the following pattern:
     *
     * <blockquote><code>
     * TOWGS84[<dx>, <dy>, <dz>, <ex>, <ey>, <ez>, <ppm>]
     * </code></blockquote>
     *
     * @param  parent The parent element.
     * @return The "TOWGS84" element as a {@link BursaWolfParameters} object,
     *         or {@code null} if no "TOWGS84" has been found.
     * @throws ParseException if the "TOWGS84" can't be parsed.
     */
    private static BursaWolfParameters parseToWGS84(final Element parent)
            throws ParseException
    {
        final Element element = parent.pullOptionalElement("TOWGS84");
        if (element == null) {
            return null;
        }
        final BursaWolfParameters info = new BursaWolfParameters(DefaultGeodeticDatum.WGS84);
        info.dx  = element.pullDouble("dx");
        info.dy  = element.pullDouble("dy");
        info.dz  = element.pullDouble("dz");
        if (element.peek() != null) {
            info.ex  = element.pullDouble("ex");
            info.ey  = element.pullDouble("ey");
            info.ez  = element.pullDouble("ez");
            info.ppm = element.pullDouble("ppm");
        }
        element.close();
        return info;
    }

    /**
     * Parses a "SPHEROID" element. This element has the following pattern:
     *
     * <blockquote><code>
     * SPHEROID["<name>", <semi-major axis>, <inverse flattening> {,<authority>}]
     * </code></blockquote>
     *
     * @param  parent The parent element.
     * @return The "SPHEROID" element as an {@link Ellipsoid} object.
     * @throws ParseException if the "SPHEROID" element can't be parsed.
     */
    private Ellipsoid parseSpheroid(final Element parent) throws ParseException {
        Element          element = parent.pullElement("SPHEROID");
        String              name = element.pullString("name");
        double     semiMajorAxis = element.pullDouble("semiMajorAxis");
        double inverseFlattening = element.pullDouble("inverseFlattening");
        Map<String,?> properties = parseAuthority(element, name);
        element.close();
        if (inverseFlattening == 0) {
            // Inverse flattening null is an OGC convention for a sphere.
            inverseFlattening = Double.POSITIVE_INFINITY;
        }
        try {
            return datumFactory.createFlattenedSphere(properties,
                    semiMajorAxis, inverseFlattening, SI.METER);
        } catch (FactoryException exception) {
            throw element.parseFailed(exception, null);
        }
    }

    /**
     * Parses a "PROJECTION" element. This element has the following pattern:
     *
     * <blockquote><code>
     * PROJECTION["<name>" {,<authority>}]
     * </code></blockquote>
     *
     * @param  parent The parent element.
     * @param  ellipsoid The ellipsoid, or {@code null} if none.
     * @param  linearUnit The linear unit of the parent PROJCS element, or {@code null}.
     * @param  angularUnit The angular unit of the parent GEOCS element, or {@code null}.
     * @return The "PROJECTION" element as a {@link ParameterValueGroup} object.
     * @throws ParseException if the "PROJECTION" element can't be parsed.
     */
    private ParameterValueGroup parseProjection(final Element      parent,
                                                final Ellipsoid    ellipsoid,
                                                final Unit<Length> linearUnit,
                                                final Unit<Angle>  angularUnit)
            throws ParseException
    {
        final Element          element = parent.pullElement("PROJECTION");
        final String    classification = element.pullString("name");
        final Map<String,?> properties = parseAuthority(element, classification);
        element.close();
        /*
         * Set the list of parameters.  NOTE: Parameters are defined in
         * the parent Element (usually a "PROJCS" element), not in this
         * "PROJECTION" element.
         *
         * We will set the semi-major and semi-minor parameters from the
         * ellipsoid first. If those values were explicitly specified in
         * a "PARAMETER" statement, they will overwrite the values inferred
         * from the ellipsoid.
         */
        final ParameterValueGroup parameters;
        try {
            parameters = mtFactory.getDefaultParameters(classification);
        } catch (NoSuchIdentifierException exception) {
            throw element.parseFailed(exception, null);
        }
        Element param = parent;
        try {
            if (ellipsoid != null) {
                final Unit<Length> axisUnit = ellipsoid.getAxisUnit();
                parameters.parameter("semi_major").setValue(ellipsoid.getSemiMajorAxis(), axisUnit);
                parameters.parameter("semi_minor").setValue(ellipsoid.getSemiMinorAxis(), axisUnit);
            }
            while ((param=parent.pullOptionalElement("PARAMETER")) != null) {
                final String paramName  = param.pullString("name");
                final double paramValue = param.pullDouble("value");
                final ParameterValue<?> parameter = parameters.parameter(paramName);
                final Unit<?> expected = parameter.getDescriptor().getUnit();
                if (expected!=null && !Unit.ONE.equals(expected)) {
                    if (linearUnit!=null && SI.METER.isCompatible(expected)) {
                        parameter.setValue(paramValue, linearUnit);
                        continue;
                    }
                    if (angularUnit!=null && SI.RADIAN.isCompatible(expected)) {
                        parameter.setValue(paramValue, angularUnit);
                        continue;
                    }
                }
                parameter.setValue(paramValue);
            }
        } catch (ParameterNotFoundException exception) {
            throw param.parseFailed(exception, Errors.format(ErrorKeys.UNEXPECTED_PARAMETER_$1,
                                                             exception.getParameterName()));
        }
        return parameters;
    }

    /**
     * Parses a "DATUM" element. This element has the following pattern:
     *
     * <blockquote><code>
     * DATUM["<name>", <spheroid> {,<to wgs84>} {,<authority>}]
     * </code></blockquote>
     *
     * @param  parent The parent element.
     * @param  meridian the prime meridian.
     * @return The "DATUM" element as a {@link GeodeticDatum} object.
     * @throws ParseException if the "DATUM" element can't be parsed.
     */
    private GeodeticDatum parseDatum(final Element parent,
                                     final PrimeMeridian meridian)
            throws ParseException
    {
        Element             element    = parent.pullElement("DATUM");
        String              name       = element.pullString("name");
        Ellipsoid           ellipsoid  = parseSpheroid(element);
        BursaWolfParameters toWGS84    = parseToWGS84(element); // Optional; may be null.
        Map<String,Object>  properties = parseAuthority(element, name);
        if (ALLOW_ORACLE_SYNTAX && (toWGS84 == null) && (element.peek() instanceof Number)) {
            toWGS84     = new BursaWolfParameters(DefaultGeodeticDatum.WGS84);
            toWGS84.dx  = element.pullDouble("dx");
            toWGS84.dy  = element.pullDouble("dy");
            toWGS84.dz  = element.pullDouble("dz");
            toWGS84.ex  = element.pullDouble("ex");
            toWGS84.ey  = element.pullDouble("ey");
            toWGS84.ez  = element.pullDouble("ez");
            toWGS84.ppm = element.pullDouble("ppm");
        }
        element.close();
        if (toWGS84 != null) {
            if (!(properties instanceof HashMap)) {
                properties = new HashMap<String,Object>(properties);
            }
            properties.put(DefaultGeodeticDatum.BURSA_WOLF_KEY, toWGS84);
        }
        try {
            return datumFactory.createGeodeticDatum(properties, ellipsoid, meridian);
        } catch (FactoryException exception) {
            throw element.parseFailed(exception, null);
        }
    }

    /**
     * Parses a "VERT_DATUM" element. This element has the following pattern:
     *
     * <blockquote><code>
     * VERT_DATUM["<name>", <datum type> {,<authority>}]
     * </code></blockquote>
     *
     * @param  parent The parent element.
     * @return The "VERT_DATUM" element as a {@link VerticalDatum} object.
     * @throws ParseException if the "VERT_DATUM" element can't be parsed.
     */
    private VerticalDatum parseVertDatum(final Element parent) throws ParseException {
        final Element element = parent.pullElement("VERT_DATUM");
        final String     name = element.pullString ("name");
        final int       datum = element.pullInteger("datum");
        final Map<String,?> properties = parseAuthority(element, name);
        element.close();
        final VerticalDatumType type = DefaultVerticalDatum.getVerticalDatumTypeFromLegacyCode(datum);
        if (type == null) {
            throw element.parseFailed(null, Errors.format(ErrorKeys.UNKNOW_TYPE_$1, datum));
        }
        try {
            return datumFactory.createVerticalDatum(properties, type);
        } catch (FactoryException exception) {
            throw element.parseFailed(exception, null);
        }
    }

    /**
     * Parses a "LOCAL_DATUM" element. This element has the following pattern:
     *
     * <blockquote><code>
     * LOCAL_DATUM["<name>", <datum type> {,<authority>}]
     * </code></blockquote>
     *
     * @param  parent The parent element.
     * @return The "LOCAL_DATUM" element as an {@link EngineeringDatum} object.
     * @throws ParseException if the "LOCAL_DATUM" element can't be parsed.
     *
     * @todo The vertical datum type is currently ignored.
     */
    private EngineeringDatum parseLocalDatum(final Element parent) throws ParseException {
        final Element element = parent.pullElement("LOCAL_DATUM");
        final String     name = element.pullString ("name");
        final int       datum = element.pullInteger("datum");
        final Map<String,?> properties = parseAuthority(element, name);
        element.close();
        try {
            return datumFactory.createEngineeringDatum(properties);
        } catch (FactoryException exception) {
            throw element.parseFailed(exception, null);
        }
    }

    /**
     * Parses a "LOCAL_CS" element.
     * This element has the following pattern:
     *
     * <blockquote><code>
     * LOCAL_CS["<name>", <local datum>, <unit>, <axis>, {,<axis>}* {,<authority>}]
     * </code></blockquote>
     *
     * @param  parent The parent element.
     * @return The "LOCAL_CS" element as an {@link EngineeringCRS} object.
     * @throws ParseException if the "LOCAL_CS" element can't be parsed.
     *
     * @todo The coordinate system used is always a Geotools implementation, since we don't
     *       know which method to invokes in the {@link CSFactory} (is it a cartesian
     *       coordinate system? a spherical one? etc.).
     */
    private EngineeringCRS parseLocalCS(final Element parent) throws ParseException {
        Element           element = parent.pullElement("LOCAL_CS");
        String               name = element.pullString("name");
        EngineeringDatum    datum = parseLocalDatum(element);
        Unit<Length>   linearUnit = parseUnit(element, SI.METER);
        CoordinateSystemAxis axis = parseAxis(element, linearUnit, true);
        List<CoordinateSystemAxis> list = new ArrayList<CoordinateSystemAxis>();
        do {
            list.add(axis);
            axis = parseAxis(element, linearUnit, false);
        } while (axis != null);
        final Map<String,?> properties = parseAuthority(element, name);
        element.close();
        final CoordinateSystem cs;
        cs = new AbstractCS(singletonMap("name", name),
                list.toArray(new CoordinateSystemAxis[list.size()]));
        try {
            return crsFactory.createEngineeringCRS(properties, datum, cs);
        } catch (FactoryException exception) {
            throw element.parseFailed(exception, null);
        }
    }

    /**
     * Parses a "GEOCCS" element.
     * This element has the following pattern:
     *
     * <blockquote><code>
     * GEOCCS["<name>", <datum>, <prime meridian>,  <linear unit>
     *        {,<axis> ,<axis> ,<axis>} {,<authority>}]
     * </code></blockquote>
     *
     * @param  parent The parent element.
     * @return The "GEOCCS" element as a {@link GeocentricCRS} object.
     * @throws ParseException if the "GEOCCS" element can't be parsed.
     */
    private GeocentricCRS parseGeoCCS(final Element parent) throws ParseException {
        final Element          element = parent.pullElement("GEOCCS");
        final String              name = element.pullString("name");
        final Map<String,?> properties = parseAuthority(element, name);
        final PrimeMeridian   meridian = parsePrimem   (element, NonSI.DEGREE_ANGLE);
        final GeodeticDatum      datum = parseDatum    (element, meridian);
        final Unit<Length>  linearUnit = parseUnit     (element, SI.METER);
        CoordinateSystemAxis axis0, axis1, axis2;
        axis0 = parseAxis(element, linearUnit, false);
        try {
            if (axis0 != null) {
                axis1 = parseAxis(element, linearUnit, true);
                axis2 = parseAxis(element, linearUnit, true);
            } else {
                // Those default values are part of WKT specification.
                axis0 = createAxis(null, "X", AxisDirection.OTHER, linearUnit);
                axis1 = createAxis(null, "Y", AxisDirection.EAST,  linearUnit);
                axis2 = createAxis(null, "Z", AxisDirection.NORTH, linearUnit);
            }
            element.close();
            return crsFactory.createGeocentricCRS(properties, datum,
                    csFactory.createCartesianCS(properties, axis0, axis1, axis2));
        } catch (FactoryException exception) {
            throw element.parseFailed(exception, null);
        }
    }

    /**
     * Parses an <strong>optional</strong> "VERT_CS" element.
     * This element has the following pattern:
     *
     * <blockquote><code>
     * VERT_CS["<name>", <vert datum>, <linear unit>, {<axis>,} {,<authority>}]
     * </code></blockquote>
     *
     * @param  parent The parent element.
     * @return The "VERT_CS" element as a {@link VerticalCRS} object.
     * @throws ParseException if the "VERT_CS" element can't be parsed.
     */
    private VerticalCRS parseVertCS(final Element parent) throws ParseException {
        final Element element = parent.pullElement("VERT_CS");
        if (element == null) {
            return null;
        }
        String               name = element.pullString("name");
        VerticalDatum       datum = parseVertDatum(element);
        Unit<Length>   linearUnit = parseUnit(element, SI.METER);
        CoordinateSystemAxis axis = parseAxis(element, linearUnit, false);
        Map<String,?>  properties = parseAuthority(element, name);
        element.close();
        try {
            if (axis == null) {
                axis = createAxis(null, "Z", AxisDirection.UP, linearUnit);
            }
            return crsFactory.createVerticalCRS(properties, datum,
                    csFactory.createVerticalCS(singletonMap("name", name), axis));
        } catch (FactoryException exception) {
            throw element.parseFailed(exception, null);
        }
    }

    /**
     * Parses a "GEOGCS" element. This element has the following pattern:
     *
     * <blockquote><code>
     * GEOGCS["<name>", <datum>, <prime meridian>, <angular unit>  {,<twin axes>} {,<authority>}]
     * </code></blockquote>
     *
     * @param  parent The parent element.
     * @return The "GEOGCS" element as a {@link GeographicCRS} object.
     * @throws ParseException if the "GEOGCS" element can't be parsed.
     */
    private GeographicCRS parseGeoGCS(final Element parent) throws ParseException {
        Element            element = parent.pullElement("GEOGCS");
        String                name = element.pullString("name");
        Map<String,?>   properties = parseAuthority(element, name);
        Unit<Angle>    angularUnit = parseUnit     (element, SI.RADIAN);
        PrimeMeridian     meridian = parsePrimem   (element, angularUnit);
        GeodeticDatum        datum = parseDatum    (element, meridian);
        CoordinateSystemAxis axis0 = parseAxis     (element, angularUnit, false);
        CoordinateSystemAxis axis1;
        CoordinateSystemAxis axis2 = null;
        try {
            if (axis0 != null) {
                axis1 = parseAxis(element, angularUnit, true);
                if(axis1 != null) {
                    axis2 = parseAxis(element, SI.METER, false);
                } 
            } else {
                // Those default values are part of WKT specification.
                axis0 = createAxis(null, "Lon", AxisDirection.EAST,  angularUnit);
                axis1 = createAxis(null, "Lat", AxisDirection.NORTH, angularUnit);
            }
            element.close();
            EllipsoidalCS ellipsoidalCS;
            if(axis2 != null) {
                ellipsoidalCS = csFactory.createEllipsoidalCS(properties, axis0, axis1, axis2);
            } else {
                ellipsoidalCS = csFactory.createEllipsoidalCS(properties, axis0, axis1);
            }
            return crsFactory.createGeographicCRS(properties, datum,
                    ellipsoidalCS);
        } catch (FactoryException exception) {
            throw element.parseFailed(exception, null);
        }
    }

    /**
     * Parses a "PROJCS" element.
     * This element has the following pattern:
     *
     * <blockquote><code>
     * PROJCS["<name>", <geographic cs>, <projection>, {<parameter>,}*,
     *        <linear unit> {,<twin axes>}{,<authority>}]
     * </code></blockquote>
     *
     * @param  parent The parent element.
     * @return The "PROJCS" element as a {@link ProjectedCRS} object.
     * @throws ParseException if the "GEOGCS" element can't be parsed.
     */
    private ProjectedCRS parseProjCS(final Element parent) throws ParseException {
        Element                element = parent.pullElement("PROJCS");
        String                    name = element.pullString("name");
        Map<String,?>       properties = parseAuthority(element, name);
        GeographicCRS           geoCRS = parseGeoGCS(element);
        Ellipsoid            ellipsoid = geoCRS.getDatum().getEllipsoid();
        Unit<Length>        linearUnit = parseUnit(element, SI.METER);
        Unit<Angle>        angularUnit = geoCRS.getCoordinateSystem().getAxis(0).getUnit().asType(Angle.class);
        ParameterValueGroup projection = parseProjection(element, ellipsoid, linearUnit, angularUnit);
        CoordinateSystemAxis     axis0 = parseAxis(element, linearUnit, false);
        CoordinateSystemAxis     axis1;
        try {
            if (axis0 != null) {
                axis1 = parseAxis(element, linearUnit, true);
            } else {
                // Those default values are part of WKT specification.
                axis0 = createAxis(null, "X", AxisDirection.EAST,  linearUnit);
                axis1 = createAxis(null, "Y", AxisDirection.NORTH, linearUnit);
            }
            element.close();
            final Conversion conversion = new DefiningConversion(name, projection);
            return crsFactory.createProjectedCRS(properties, geoCRS, conversion,
                    csFactory.createCartesianCS(properties, axis0, axis1));
        } catch (FactoryException exception) {
            throw element.parseFailed(exception, null);
        }
    }

    /**
     * Parses a "COMPD_CS" element.
     * This element has the following pattern:
     *
     * <blockquote><code>
     * COMPD_CS["<name>", <head cs>, <tail cs> {,<authority>}]
     * </code></blockquote>
     *
     * @param  parent The parent element.
     * @return The "COMPD_CS" element as a {@link CompoundCRS} object.
     * @throws ParseException if the "COMPD_CS" element can't be parsed.
     */
    private CompoundCRS parseCompdCS(final Element parent) throws ParseException {
        final CoordinateReferenceSystem[] CRS = new CoordinateReferenceSystem[2];
        Element       element    = parent.pullElement("COMPD_CS");
        String        name       = element.pullString("name");
        Map<String,?> properties = parseAuthority(element, name);
        CRS[0] = parseCoordinateReferenceSystem(element);
        CRS[1] = parseCoordinateReferenceSystem(element);
        element.close();
        try {
            return crsFactory.createCompoundCRS(properties, CRS);
        } catch (FactoryException exception) {
            throw element.parseFailed(exception, null);
        }
    }

    /**
     * Parses a "FITTED_CS" element.
     * This element has the following pattern:
     *
     * <blockquote><code>
     * FITTED_CS["<name>", <to base>, <base cs>]
     * </code></blockquote>
     *
     * @param  parent The parent element.
     * @return The "FITTED_CS" element as a {@link CompoundCRS} object.
     * @throws ParseException if the "COMPD_CS" element can't be parsed.
     */
    private DerivedCRS parseFittedCS(final Element parent) throws ParseException {
        Element       element    = parent.pullElement("FITTED_CS");
        String        name       = element.pullString("name");
        Map<String,?> properties = parseAuthority(element, name);
        final MathTransform toBase = parseMathTransform(element, true);
        final CoordinateReferenceSystem base = parseCoordinateReferenceSystem(element);
        final OperationMethod method = getOperationMethod();
        element.close();
        /*
         * WKT provides no informations about the underlying CS of a derived CRS.
         * We have to guess some reasonable one with arbitrary units.  We try to
         * construct the one which contains as few information as possible, in
         * order to avoid providing wrong informations.
         */
        final CoordinateSystemAxis[] axis = new CoordinateSystemAxis[toBase.getSourceDimensions()];
        final StringBuilder buffer = new StringBuilder(name);
        buffer.append(" axis ");
        final int start = buffer.length();
        try {
            for (int i=0; i<axis.length; i++) {
                final String number = String.valueOf(i);
                buffer.setLength(start);
                buffer.append(number);
                axis[i] = csFactory.createCoordinateSystemAxis(
                    singletonMap(IdentifiedObject.NAME_KEY, buffer.toString()),
                    number, AxisDirection.OTHER, Unit.ONE);
            }
            final Conversion conversion = new DefiningConversion(
                    singletonMap(IdentifiedObject.NAME_KEY, method.getName().getCode()),
                    method, toBase.inverse());
            final CoordinateSystem cs = new AbstractCS(properties, axis);
            return crsFactory.createDerivedCRS(properties, base, conversion, cs);
        } catch (FactoryException exception) {
            throw element.parseFailed(exception, null);
        } catch (NoninvertibleTransformException exception) {
            throw element.parseFailed(exception, null);
        }
    }

    /**
     * Returns the class of the specified WKT element. For example this method returns
     * <code>{@linkplain ProjectedCRS}.class</code> for element "{@code PROJCS}".
     *
     * @param  element The WKT element name.
     * @return The GeoAPI class of the specified element, or {@code null} if unknow.
     */
    public static Class<?> getClassOf(String element) {
        if (element == null) {
            return null;
        }
        element = element.trim().toUpperCase(Locale.US);
        final Class<?> type = getTypeMap().get(element);
        assert type == null || type.equals(MathTransform.class)
                || element.equals(getNameOf(type)) : type;
        return type;
    }

    /**
     * Returns the WKT name of the specified object type. For example this method returns
     * "{@code PROJCS}" for type <code>{@linkplain ProjectedCRS}.class</code>.
     *
     * @param type The GeoAPI class of the specified element.
     * @return The WKT element name, or {@code null} if unknow.
     *
     * @since 2.4
     */
    public static String getNameOf(final Class<?> type) {
        if (type != null) {
            for (final Map.Entry<String,Class<?>> entry : getTypeMap().entrySet()) {
                final Class<?> candidate = entry.getValue();
                if (candidate.isAssignableFrom(type)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    /**
     * Returns the type map.
     */
    private static Map<String,Class<?>> getTypeMap() {
        if (TYPES == null) {
            final Map<String,Class<?>> map = new LinkedHashMap<String,Class<?>>(25);
            map.put(        "GEOGCS",        GeographicCRS.class);
            map.put(        "PROJCS",         ProjectedCRS.class);
            map.put(        "GEOCCS",        GeocentricCRS.class);
            map.put(       "VERT_CS",          VerticalCRS.class);
            map.put(      "LOCAL_CS",       EngineeringCRS.class);
            map.put(      "COMPD_CS",          CompoundCRS.class);
            map.put(     "FITTED_CS",           DerivedCRS.class);
            map.put(          "AXIS", CoordinateSystemAxis.class);
            map.put(        "PRIMEM",        PrimeMeridian.class);
            map.put(       "TOWGS84",  BursaWolfParameters.class);
            map.put(      "SPHEROID",            Ellipsoid.class);
            map.put(    "VERT_DATUM",        VerticalDatum.class);
            map.put(   "LOCAL_DATUM",     EngineeringDatum.class);
            map.put(         "DATUM",        GeodeticDatum.class);
            map.put(      "PARAM_MT",        MathTransform.class);
            map.put(     "CONCAT_MT",        MathTransform.class);
            map.put(    "INVERSE_MT",        MathTransform.class);
            map.put("PASSTHROUGH_MT",        MathTransform.class);
            TYPES = map; // Sets the field only once completed, in order to avoid synchronisation.
                         // It is not a big deal in current implementation if two Maps are created.
        }
        return TYPES;
    }

    /**
     * Read WKT strings from the {@linkplain System#in standard input stream} and
     * reformat them to the {@linkplain System#out standard output stream}. The
     * input is read until it reach the end-of-file ({@code [Ctrl-Z]} if
     * reading from the keyboard), or until an unparsable WKT has been hit.
     * Optional arguments are:
     *
     * <TABLE CELLPADDING='0' CELLSPACING='0'>
     *   <TR><TD NOWRAP><CODE>-authority</CODE> <VAR>&lt;name&gt;</VAR></TD>
     *       <TD NOWRAP>&nbsp;The authority to prefer when choosing WKT entities names.</TD></TR>
     *   <TR><TD NOWRAP><CODE>-indentation</CODE> <VAR>&lt;value&gt;</VAR></TD>
     *       <TD NOWRAP>&nbsp;Set the indentation (0 for output on a single line)</TD></TR>
     *   <TR><TD NOWRAP><CODE>-encoding</CODE> <VAR>&lt;code&gt;</VAR></TD>
     *       <TD NOWRAP>&nbsp;Set the character encoding</TD></TR>
     *   <TR><TD NOWRAP><CODE>-locale</CODE> <VAR>&lt;language&gt;</VAR></TD>
     *       <TD NOWRAP>&nbsp;Set the language for the output (e.g. "fr" for French)</TD></TR>
     * </TABLE>
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        final Arguments arguments = new Arguments(args);
        final Integer indentation = arguments.getOptionalInteger(Formattable.INDENTATION);
        final String    authority = arguments.getOptionalString("-authority");
        args = arguments.getRemainingArguments(0);
        if (indentation != null) {
            Formattable.setIndentation(indentation.intValue());
        }
        final BufferedReader in = new BufferedReader(Arguments.getReader(System.in));
        try {
            final Parser parser = new Parser();
            if (authority != null) {
                parser.setAuthority(Citations.fromName(authority));
            }
            parser.reformat(in, arguments.out, arguments.err);
        } catch (Exception exception) {
            exception.printStackTrace(arguments.err);
        }
        // Do not close 'in', since it is the standard input stream.
    }
}
