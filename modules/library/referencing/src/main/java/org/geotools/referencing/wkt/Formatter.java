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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.referencing.wkt;

import java.lang.reflect.Array;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Locale;

import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Length;

import org.opengis.metadata.Identifier;
import org.opengis.metadata.citation.Citation;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.datum.Datum;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.OperationMethod;
import org.opengis.util.CodeList;
import org.opengis.util.GenericName;
import org.opengis.util.InternationalString;

import org.geotools.math.XMath;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.resources.Arguments;
import org.geotools.util.Utilities;
import org.geotools.resources.X364;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * Format {@link Formattable} objects as
 * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
 * Known Text</cite> (WKT)</A>.
 *
 * A formatter is constructed with a specified set of symbols.
 * The {@linkplain Locale locale} associated with the symbols is used for querying
 * {@linkplain org.opengis.metadata.citation.Citation#getTitle authority titles}.
 *
 * @since 2.0
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html">Well Know Text specification</A>
 * @see <A HREF="http://home.gdal.org/projects/opengis/wktproblems.html">OGC WKT Coordinate System Issues</A>
 */
public class Formatter {
    /**
     * Do not format an {@code "AUTHORITY"} element for instances of those classes.
     *
     * @see #authorityAllowed
     */
    @SuppressWarnings("unchecked")
    private static final Class<? extends IdentifiedObject>[] AUTHORITY_EXCLUDE = new Class[] {
        CoordinateSystemAxis.class
    };

    /**
     * ANSI X3.64 codes for syntax coloring. Used only if syntax coloring
     * has been explicitly enabled.
     */
    private static final String
            NUMBER_COLOR    = X364.YELLOW,  // Floating point numbers only, not integers.
            INTEGER_COLOR   = X364.YELLOW,
            UNIT_COLOR      = X364.YELLOW,
            AXIS_COLOR      = X364.CYAN,
            CODELIST_COLOR  = X364.CYAN,
            PARAMETER_COLOR = X364.GREEN,
            METHOD_COLOR    = X364.GREEN,
            DATUM_COLOR     = X364.GREEN,
            ERROR_COLOR     = X364.BACKGROUND_RED;

    /**
     * The symbols to use for this formatter.
     */
    private final Symbols symbols;

    /**
     * The preferred authority for object or parameter names.
     *
     * @see AbstractParser#getAuthority
     * @see AbstractParser#setAuthority
     */
    private Citation authority = Citations.OGC;

    /**
     * Whatever we allow syntax coloring on ANSI X3.64 compatible terminal.
     *
     * @see AbstractParser#isColorEnabled
     * @see AbstractParser#setColorEnabled
     */
    boolean colorEnabled = false;

    /**
     * The unit for formatting measures, or {@code null} for the "natural" unit of each WKT
     * element.
     */
    private Unit<Length> linearUnit;

    /**
     * The unit for formatting measures, or {@code null} for the "natural" unit of each WKT
     * element. This value is set for example by "GEOGCS", which force its enclosing "PRIMEM" to
     * take the same units than itself.
     */
    private Unit<Angle> angularUnit;
    
    public Citation getAuthority() {
        return authority;
    }

    public void setAuthority(Citation authority) {
        this.authority = authority;
        this.unitFormat = GeoToolsUnitFormat.getInstance(authority);
    }


    /**
     * The object to use for formatting numbers.
     */
    private final NumberFormat numberFormat;

    /**
     * The object to use for formatting units.
     */
    private UnitFormat unitFormat = GeoToolsUnitFormat.getInstance(Citations.EPSG);

    /**
     * Dummy field position.
     */
    private final FieldPosition dummy = new FieldPosition(0);

    /**
     * The buffer in which to format. Consider this field as private final; the only
     * method to set the buffer to a new value is {@link AbstractParser#format}.
     */
    StringBuffer buffer;

    /**
     * The starting point in the buffer. Always 0, except when used by
     * {@link AbstractParser#format}.
     */
    int bufferBase;

    /**
     * The amount of space to use in indentation, or 0 if indentation is disabled.
     */
    final int indentation;

    /**
     * The amount of space to write on the left side of each line. This amount is increased
     * by {@code indentation} every time a {@link Formattable} object is appended in a
     * new indentation level.
     */
    private int margin;

    /**
     * {@code true} if a new line were requested during the execution
     * of {@link #append(Formattable)}. This is used to determine if
     * {@code UNIT} and {@code AUTHORITY} elements should appears
     * on a new line too.
     */
    private boolean lineChanged;

    /**
     * {@code true} if the WKT is invalid. Similar to {@link #unformattable}, except that
     * this field is reset to {@code false} after the invalid part has been processed by
     * {@link #append(Formattable)}. This field is for internal use only.
     */
    private boolean invalidWKT;

    /**
     * Non-null if the WKT is invalid. If non-null, then this field contains the interface class
     * of the problematic part (e.g. {@link org.opengis.referencing.crs.EngineeringCRS}).
     */
    private Class<?> unformattable;

    /**
     * Warning that may be produced during WKT formatting, or {@code null} if none.
     */
    String warning;

    /**
     * Creates a new instance of the formatter with the default symbols.
     */
    public Formatter() {
        this(Symbols.DEFAULT, 0);
    }

    /**
     * Creates a new instance of the formatter. The whole WKT will be formatted
     * on a single line.
     *
     * @param symbols The symbols.
     */
    public Formatter(final Symbols symbols) {
        this(symbols, 0);
    }

    /**
     * Creates a new instance of the formatter with the specified indentation width.
     * The WKT will be formatted on many lines, and the indentation width will have
     * the value specified to this constructor. If the specified indentation is
     * {@link FormattableObject#SINGLE_LINE}, then the whole WKT will be formatted
     * on a single line.
     *
     * @param symbols The symbols.
     * @param indentation The amount of spaces to use in indentation. Typical values are 2 or 4.
     */
    public Formatter(final Symbols symbols, final int indentation) {
        this.symbols     = symbols;
        this.indentation = indentation;
        if (symbols == null) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, "symbols"));
        }
        if (indentation < 0) {
            throw new IllegalArgumentException(Errors.format(
                    ErrorKeys.ILLEGAL_ARGUMENT_$2, "indentation", indentation));
        }
        numberFormat = (NumberFormat) symbols.numberFormat.clone();
        buffer = new StringBuffer();
    }

    /**
     * Constructor for private use by {@link AbstractParser#format} only.
     * This constructor help to share some objects with {@link AbstractParser}.
     */
    Formatter(final Symbols symbols, final NumberFormat numberFormat) {
        this.symbols = symbols;
        indentation  = Formattable.getIndentation();
        this.numberFormat = numberFormat; // No clone needed.
        // Do not set the buffer. It will be set by AbstractParser.format.
    }

    /**
     * Set the colors using the specified ANSI escape. The color is ignored
     * unless syntax coloring has been explicitly enabled. The {@code color}
     * should be a constant from {@link X364}.
     */
    private void setColor(final String color) {
        if (colorEnabled) {
            buffer.append(color);
        }
    }

    /**
     * Reset the colors to the default. This method do nothing
     * unless syntax coloring has been explicitly enabled.
     */
    private void resetColor() {
        if (colorEnabled) {
            buffer.append(X364.DEFAULT);
        }
    }

    /**
     * Returns the color to uses for the name of the specified object.
     */
    private static String getNameColor(final IdentifiedObject object) {
        if (object instanceof Datum) {
            return DATUM_COLOR;
        }
        if (object instanceof OperationMethod) {
            return METHOD_COLOR;
        }
        if (object instanceof CoordinateSystemAxis) {
            return AXIS_COLOR;
        }
        // Note: we can't test for MathTransform here, since it is not an IdentifiedObject.
        //       If we want to provide a color for the MathTransform name, we would need to
        //       do that in 'append(String)' method, but the later is for general string...
        return null;
    }

    /**
     * Add a separator to the buffer, if needed.
     *
     * @param newLine if {@code true}, add a line separator too.
     */
    private void appendSeparator(final boolean newLine) {
        int length = buffer.length();
        char c;
        do {
            if (length == bufferBase) {
                return;
            }
            c = buffer.charAt(--length);
            if (c==symbols.open || c==symbols.openArray) {
                return;
            }
        } while (Character.isWhitespace(c) || c==symbols.space);
        buffer.append(symbols.separator).append(symbols.space);
        if (newLine && indentation != 0) {
            buffer.append(System.getProperty("line.separator", "\n"))
                  .append(Utilities.spaces(margin));
            lineChanged = true;
        }
    }

    /**
     * Append the specified {@code Formattable} object. This method will automatically append
     * the keyword (e.g. <code>"GEOCS"</code>), the name and the authority code, and will invokes
     * <code>formattable.{@linkplain Formattable#formatWKT formatWKT}(this)</code> for completing
     * the inner part of the WKT.
     *
     * @param formattable The formattable object to append to the WKT.
     */
    public void append(final Formattable formattable) {
        /*
         * Formats the opening bracket and the object name (e.g. "NAD27").
         * The WKT entity name (e.g. "PROJCS") will be formatted later.
         * The result of this code portion looks like the following:
         *
         *         <previous text>,
         *           ["NAD27 / Idaho Central"
         */
        appendSeparator(true);
        int base = buffer.length();
        buffer.append(symbols.open);
        final IdentifiedObject info = (formattable instanceof IdentifiedObject)
                                    ? (IdentifiedObject) formattable : null;
        if (info != null) {
            final String c = getNameColor(info);
            if (c != null) {
                setColor(c);
            }
            buffer.append(symbols.quote).append(getName(info)).append(symbols.quote);
            if (c != null) {
                resetColor();
            }
        }
        /*
         * Formats the part after the object name, then insert the WKT element name
         * in front of them. The result of this code portion looks like the following:
         *
         *         <previous text>,
         *           PROJCS["NAD27 / Idaho Central",
         *             GEOGCS[...etc...],
         *             ...etc...
         */
        indent(+1);
        lineChanged = false;
        String keyword = formattable.formatWKT(this);
        if (colorEnabled && invalidWKT) {
            invalidWKT = false;
            buffer.insert(base, ERROR_COLOR + X364.BACKGROUND_DEFAULT);
            base += ERROR_COLOR.length();
        }
        buffer.insert(base, keyword);
        /*
         * Formats the AUTHORITY[<name>,<code>] entity, if there is one. The entity
         * will be on the same line than the enclosing one if no line separator were
         * added (e.g. SPHEROID["Clarke 1866", ..., AUTHORITY["EPSG","7008"]]), or on
         * a new line otherwise. After this block, the result looks like the following:
         *
         *         <previous text>,
         *           PROJCS["NAD27 / Idaho Central",
         *             GEOGCS[...etc...],
         *             ...etc...
         *             AUTHORITY["EPSG","26769"]]
         */
        final Identifier identifier = getIdentifier(info);
        if (identifier!=null && authorityAllowed(info)) {
            final Citation authority = identifier.getAuthority();
            if (authority != null) {
                /*
                 * Since WKT often use abbreviations, search for the shortest
                 * title or alternate title. If one is found, it will be used
                 * as the authority name (e.g. "EPSG").
                 */
                InternationalString inter = authority.getTitle();
                String title = (inter!=null) ? inter.toString(symbols.locale) : null;
                for (final InternationalString alt : authority.getAlternateTitles()) {
                    if (alt != null) {
                        final String candidate = alt.toString(symbols.locale);
                        if (candidate != null) {
                            if (title==null || candidate.length() < title.length()) {
                                title = candidate;
                            }
                        }
                    }
                }
                if (title != null) {
                    appendSeparator(lineChanged);
                    buffer.append("AUTHORITY")
                          .append(symbols.open)
                          .append(symbols.quote)
                          .append(title)
                          .append(symbols.quote);
                    final String code = identifier.getCode();
                    if (code != null) {
                        buffer.append(symbols.separator)
                              .append(symbols.quote)
                              .append(code)
                              .append(symbols.quote);
                    }
                    buffer.append(symbols.close);
                }
            }
        }
        buffer.append(symbols.close);
        lineChanged = true;
        indent(-1);
    }

    /**
     * Append the specified OpenGIS's {@code IdentifiedObject} object.
     *
     * @param info The info object to append to the WKT.
     */
    public void append(final IdentifiedObject info) {
        if (info instanceof Formattable) {
            append((Formattable) info);
        } else {
            append(new Adapter(info));
        }
    }

    /**
     * Append the specified math transform.
     *
     * @param transform The transform object to append to the WKT.
     */
    public void append(final MathTransform transform) {
        if (transform instanceof Formattable) {
            append((Formattable) transform);
        } else {
            append(new Adapter(transform));
        }
    }

    /**
     * Append a code list to the WKT.
     *
     * @param code The code list to format.
     */
    public void append(final CodeList code) {
        if (code != null) {
            appendSeparator(false);
            setColor(CODELIST_COLOR);
            final String name = code.name();
            final boolean needQuotes = (name.indexOf(' ') >= 0);
            if (needQuotes) {
                buffer.append(symbols.quote);
            }
            buffer.append(name);
            if (needQuotes) {
                buffer.append(symbols.quote);
                setInvalidWKT(code.getClass());
            }
            resetColor();
        }
    }

    /**
     * Append a {@linkplain ParameterValue parameter} in WKT form. If the supplied parameter
     * is actually a {@linkplain ParameterValueGroup parameter group}, all parameters will be
     * inlined.
     *
     * @param parameter The parameter to format.
     */
    public void append(final GeneralParameterValue parameter) {
        if (parameter instanceof ParameterValueGroup) {
            for (final GeneralParameterValue param : ((ParameterValueGroup)parameter).values()) {
                append(param);
            }
        }
        if (parameter instanceof ParameterValue) {
            final ParameterValue<?> param = (ParameterValue) parameter;
            final ParameterDescriptor<?> descriptor = param.getDescriptor();
            final Unit<?> valueUnit = descriptor.getUnit();
            Unit<?> unit = valueUnit;
            if (unit!=null && !Unit.ONE.equals(unit)) {
                if (linearUnit!=null && unit.isCompatible(linearUnit)) {
                    unit = linearUnit;
                } else if (angularUnit!=null && unit.isCompatible(angularUnit)) {
                    unit = angularUnit;
                }
            }
            appendSeparator(true);
            final int start = buffer.length();
            buffer.append("PARAMETER");
            final int stop = buffer.length();
            buffer.append(symbols.open);
            setColor(PARAMETER_COLOR);
            buffer.append(symbols.quote).append(getName(descriptor)).append(symbols.quote);
            resetColor();
            buffer.append(symbols.separator).append(symbols.space);
            if (unit != null) {
                double value;
                try {
                    value = param.doubleValue(unit);
                } catch (IllegalStateException exception) {
                    // May happen if a parameter is mandatory (e.g. "semi-major")
                    // but no value has been set for this parameter.
                    if (colorEnabled) {
                        buffer.insert(stop, X364.BACKGROUND_DEFAULT).insert(start, ERROR_COLOR);
                    }
                    warning = exception.getLocalizedMessage();
                    value = Double.NaN;
                }
                if (!unit.equals(valueUnit)) {
                    value = XMath.trimDecimalFractionDigits(value, 4, 9);
                }
                format(value);
            } else {
                appendObject(param.getValue());
            }
            buffer.append(symbols.close);
        }
    }

    /**
     * Append the specified value to a string buffer. If the value is an array, then the
     * array elements are appended recursively (i.e. the array may contains sub-array).
     */
    private void appendObject(final Object value) {
        if (value == null) {
            buffer.append("null");
            return;
        }
        if (value.getClass().isArray()) {
            buffer.append(symbols.openArray);
            final int length = Array.getLength(value);
            for (int i=0; i<length; i++) {
                if (i != 0) {
                    buffer.append(symbols.separator).append(symbols.space);
                }
                appendObject(Array.get(value, i));
            }
            buffer.append(symbols.closeArray);
            return;
        }
        if (value instanceof Number) {
            format((Number) value);
        } else {
            buffer.append(symbols.quote).append(value).append(symbols.quote);
        }
    }

    /**
     * Append an integer number. A comma (or any other element
     * separator) will be written before the number if needed.
     *
     * @param number The integer to format.
     */
    public void append(final int number) {
        appendSeparator(false);
        format(number);
    }

    /**
     * Append a floating point number. A comma (or any other element
     * separator) will be written before the number if needed.
     *
     * @param number The floating point value to format.
     */
    public void append(final double number) {
        appendSeparator(false);
        format(number);
    }

    /**
     * Appends a unit in WKT form. For example, {@code append(SI.KILOMETER)}
     * can append "<code>UNIT["km", 1000]</code>" to the WKT.
     *
     * @param unit The unit to append.
     */
    public void append(final Unit<?> unit) {
        if (unit != null) {
            appendSeparator(lineChanged);
            buffer.append("UNIT").append(symbols.open);
            setColor(UNIT_COLOR);
            buffer.append(symbols.quote);
            unitFormat.format(unit, buffer, dummy);
            buffer.append(symbols.quote);
            resetColor();
            Unit<?> base = null;
            if (SI.METER.isCompatible(unit)) {
                base = SI.METER;
            } else if (SI.SECOND.isCompatible(unit)) {
                base = SI.SECOND;
            } else if (SI.RADIAN.isCompatible(unit)) {
                if (!Unit.ONE.equals(unit)) {
                    base = SI.RADIAN;
                }
            }
            if (base != null) {
                append(unit.getConverterTo(base).convert(1));
            }
            buffer.append(symbols.close);
        }
    }

    /**
     * Append a character string. The string will be written between quotes.
     * A comma (or any other element separator) will be written before the string if needed.
     *
     * @param text The string to format.
     */
    public void append(final String text) {
        appendSeparator(false);
        buffer.append(symbols.quote).append(text).append(symbols.quote);
    }

    /**
     * Format an arbitrary number.
     */
    private void format(final Number number) {
        if (number instanceof Byte  ||
            number instanceof Short ||
            number instanceof Integer)
        {
            format(number.intValue());
        } else {
            format(number.doubleValue());
        }
    }

    /**
     * Formats an integer number.
     */
    private void format(final int number) {
        setColor(INTEGER_COLOR);
        final int fraction = numberFormat.getMinimumFractionDigits();
        numberFormat.setMinimumFractionDigits(0);
        numberFormat.format(number, buffer, dummy);
        numberFormat.setMinimumFractionDigits(fraction);
        resetColor();
    }

    /**
     * Formats a floating point number.
     */
    private void format(final double number) {
        setColor(NUMBER_COLOR);
        numberFormat.format(number, buffer, dummy);
        resetColor();
    }

    /**
     * Increase or reduce the indentation. A value of {@code +1} increase
     * the indentation by the amount of spaces specified at construction time,
     * and a value of {@code +1} reduce it.
     */
    private void indent(final int amount) {
        margin = Math.max(0, margin + indentation*amount);
    }

    /**
     * Tells if an {@code "AUTHORITY"} element is allowed for the specified object.
     */
    private static boolean authorityAllowed(final IdentifiedObject info) {
        for (int i=0; i<AUTHORITY_EXCLUDE.length; i++) {
            if (AUTHORITY_EXCLUDE[i].isInstance(info)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the preferred identifier for the specified object. If the specified
     * object contains an identifier from the preferred authority (usually
     * {@linkplain Citations#OGC Open Geospatial}), then this identifier is
     * returned. Otherwise, the first identifier is returned. If the specified
     * object contains no identifier, then this method returns {@code null}.
     *
     * @param  info The object to looks for a preferred identifier.
     * @return The preferred identifier, or {@code null} if none.
     *
     * @since 2.3
     */
    public Identifier getIdentifier(final IdentifiedObject info) {
        Identifier first = null;
        if (info != null) {
            final Collection<? extends Identifier> identifiers = info.getIdentifiers();
            if (identifiers != null) {
                for (final Identifier id : identifiers) {
                    if (authorityMatches(id.getAuthority())) {
                        return id;
                    }
                    if (first == null) {
                        first = id;
                    }
                }
            }
        }
        return first;
    }

    /**
     * Checks if the specified authority can be recognized as the expected authority.
     * This implementation do not requires an exact matches. A matching title is enough.
     */
    private boolean authorityMatches(final Citation citation) {
        if (authority == citation) {
            return true;
        }
        // The "null" locale argument is required for getting the unlocalized version.
        return (citation != null) &&
               authority.getTitle().toString(null).equalsIgnoreCase(
                citation.getTitle().toString(null));
    }

    /**
     * Returns the preferred name for the specified object. If the specified
     * object contains a name from the preferred authority (usually
     * {@linkplain Citations#OGC Open Geospatial}), then this name is
     * returned. Otherwise, the first name found is returned.
     *
     * @param  info The object to looks for a preferred name.
     * @return The preferred name.
     */
    public String getName(final IdentifiedObject info) {
        final Identifier name = info.getName();
        if (!authorityMatches(name.getAuthority())) {
            final Collection<GenericName> aliases = info.getAlias();
            if (aliases != null) {
                /*
                 * The main name doesn't matches. Search in alias. We will first
                 * check if alias implements Identifier (this is the case of
                 * Geotools implementation). Otherwise, we will look at the
                 * scope in generic name.
                 */
                for (final GenericName alias : aliases) {
                    if (alias instanceof Identifier) {
                        final Identifier candidate = (Identifier) alias;
                        if (authorityMatches(candidate.getAuthority())) {
                            return candidate.getCode();
                        }
                    }
                }
                // The "null" locale argument is required for getting the unlocalized version.
                final String title = authority.getTitle().toString(null);
                for (final GenericName alias : aliases) {
                    final GenericName scope = alias.scope().name();
                    if (scope != null) {
                        if (title.equalsIgnoreCase(scope.toString())) {
                            return alias.tip().toString();
                        }
                    }
                }
            }
        }
        return name.getCode();
    }

    /**
     * The linear unit for formatting measures, or {@code null} for the "natural" unit of each
     * WKT element.
     *
     * @return The unit for measure. Default value is {@code null}.
     */
    public Unit<Length> getLinearUnit() {
        return linearUnit;
    }

    /**
     * Set the unit for formatting linear measures.
     *
     * @param unit The new unit, or {@code null}.
     */
    public void setLinearUnit(final Unit<Length> unit) {
        if (unit!=null && !SI.METER.isCompatible(unit)) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.NON_LINEAR_UNIT_$1, unit));
        }
        linearUnit = unit;
    }

    /**
     * The angular unit for formatting measures, or {@code null} for the "natural" unit of
     * each WKT element. This value is set for example by "GEOGCS", which force its enclosing
     * "PRIMEM" to take the same units than itself.
     *
     * @return The unit for measure. Default value is {@code null}.
     */
    public Unit<Angle> getAngularUnit() {
        return angularUnit;
    }

    /**
     * Set the angular unit for formatting measures.
     *
     * @param unit The new unit, or {@code null}.
     */
    public void setAngularUnit(final Unit<Angle> unit) {
        if (unit!=null && (!SI.RADIAN.isCompatible(unit) || Unit.ONE.equals(unit))) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.NON_ANGULAR_UNIT_$1, unit));
        }
        angularUnit = unit;
    }

    /**
     * Returns {@code true} if the WKT in this formatter is not strictly compliant to the
     * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html">WKT
     * specification</A>. This method returns {@code true} if {@link #setInvalidWKT} has
     * been invoked at least once. The action to take regarding invalid WKT is caller-dependant.
     * For example {@link Formattable#toString} will accepts loose WKT formatting and ignore this
     * flag, while {@link Formattable#toWKT} requires strict WKT formatting and will thrown an
     * exception if this flag is set.
     *
     * @return {@code true} if the WKT is invalid.
     */
    public boolean isInvalidWKT() {
        return unformattable != null || (buffer!=null && buffer.length() == 0);
        /*
         * Note: we really use a "and" condition (not an other "or") for the buffer test because
         *       the buffer is reset to 'null' by AbstractParser after a successfull formatting.
         */
    }

    /**
     * Returns the class declared by the last call to {@link #setInvalidWKT}.
     */
    final Class getUnformattableClass() {
        return unformattable;
    }

    /**
     * Set a flag marking the current WKT as not strictly compliant to the
     * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html">WKT
     * specification</A>. This method is invoked by {@link Formattable#formatWKT} methods when the
     * object to format is more complex than what the WKT specification allows. For example this
     * method is invoked when an {@linkplain org.geotools.referencing.crs.DefaultEngineeringCRS
     * engineering CRS} uses different unit for each axis, An application can tests
     * {@link #isInvalidWKT} later for checking WKT validity.
     *
     * @param unformattable The type of the component that can't be formatted,
     *        for example {@link org.opengis.referencing.crs.EngineeringCRS}.
     *
     * @see UnformattableObjectException#getUnformattableClass
     *
     * @since 2.4
     */
    public void setInvalidWKT(final Class<?> unformattable) {
        this.unformattable = unformattable;
        invalidWKT = true;
    }

    /**
     * Returns the WKT in its current state.
     */
    @Override
    public String toString() {
        return buffer.toString();
    }

    /**
     * Clear this formatter. All properties (including {@linkplain #getLinearUnit unit}
     * and {@linkplain #isInvalidWKT WKT validity flag} are reset to their default value.
     * After this method call, this {@code Formatter} object is ready for formatting
     * a new object.
     */
    public void clear() {
        if (buffer != null) {
            buffer.setLength(0);
        }
        linearUnit    = null;
        angularUnit   = null;
        unformattable = null;
        warning       = null;
        invalidWKT    = false;
        lineChanged   = false;
        margin        = 0;
    }
    

    /**
     * Set the preferred indentation from the command line. This indentation is used by
     * {@link Formattable#toWKT()} when no indentation were explicitly requested. This
     * method can be invoked from the command line using the following syntax:
     *
     * <blockquote>
     * {@code java org.geotools.referencing.wkt.Formatter -indentation=}<var>&lt;preferred
     * indentation&gt;</var>
     * </blockquote>
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        final Arguments arguments = new Arguments(args);
        final int indentation = arguments.getRequiredInteger(Formattable.INDENTATION);
        args = arguments.getRemainingArguments(0);
        Formattable.setIndentation(indentation);
    }
}
