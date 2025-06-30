/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.proj;

import java.lang.reflect.Array;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Locale;
import javax.measure.Unit;
import org.apache.commons.lang3.StringUtils;
import org.geotools.api.metadata.Identifier;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.IdentifiedObject;
import org.geotools.api.referencing.datum.Datum;
import org.geotools.api.referencing.datum.Ellipsoid;
import org.geotools.api.referencing.datum.PrimeMeridian;
import org.geotools.api.util.GenericName;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.operation.DefaultOperationMethod;

/**
 * A Formatter that formats {@link PROJFormattable} objects as PROJ strings. Supported PROJ formattable are normally
 * {@link IdentifiedObject} as well.
 *
 * <p>Call toPROJ(PROJFormattable) to get the associated proj String.
 */
public class PROJFormatter {

    public static final String NO_KEYWORD = "";

    private StringBuffer buffer;

    private static final FieldPosition FP_ZERO = new FieldPosition(0);

    private static final PROJAliases PROJ_ALIASES = new PROJAliases();

    private static final PROJRefiner PROJ_REFINER = new PROJRefiner();

    private boolean projectedCRS = false;

    private boolean datumProvided = false;

    private boolean ellipsoidProvided = false;

    private boolean primeMeridianProvided = false;

    /** The object to use for formatting numbers. */
    private NumberFormat numberFormat;

    /** Creates a new instance of the PROJFormatter. */
    public PROJFormatter() {
        numberFormat = NumberFormat.getNumberInstance(Locale.US);
        numberFormat.setGroupingUsed(false);
        numberFormat.setMinimumFractionDigits(1);
        numberFormat.setMaximumFractionDigits(17);
        buffer = new StringBuffer();
    }

    public boolean isProjectedCRS() {
        return projectedCRS;
    }

    public boolean isDatumProvided() {
        return datumProvided;
    }

    public boolean isEllipsoidProvided() {
        return ellipsoidProvided;
    }

    public boolean isPrimeMeridianProvided() {
        return primeMeridianProvided;
    }

    public void append(final PROJFormattable formattable) {
        int base = buffer.length();
        final IdentifiedObject info = formattable instanceof IdentifiedObject ? (IdentifiedObject) formattable : null;
        if (info != null) {
            // Getting the name of a formattable object will also check the type
            // of IdentifiedObject (i.e. a datum, a primemeridian, an ellipsoid)
            // and prepare the formatting accordingly
            buffer.append(getName(info)).append(" ");
        }

        // The formatter may append additional elements while formatting a formattable
        // and it may return a keyword for that formattable. If that's the case
        // it will be prepended to the current formatting
        String keyword = formattable.formatPROJ(this);
        if (!StringUtils.isEmpty(keyword)) {
            buffer.insert(base, keyword);
        }
    }

    /**
     * Appends a PROJ string representation of the given parameter to the internal buffer.
     *
     * <p>This method is part of a PROJFormatter that converts {@link GeneralParameterValue} objects (such as parameters
     * for projection operations) into PROJ format strings. It handles both individual parameters and groups of
     * parameters.
     *
     * <p>If the parameter is a {@link ParameterValueGroup}, the method recursively processes each contained
     * {@link GeneralParameterValue}. For each {@link ParameterValue}, it appends a formatted key-value pair to the
     * buffer, with the key based on the parameter descriptor's name and the value based on the parameter's value,
     * converted to a double if possible.
     *
     * @param parameter the {@link GeneralParameterValue} to format and append. This may be an individual
     *     {@link ParameterValue} or a group of parameters in a {@link ParameterValueGroup}.
     * @throws ClassCastException if {@code parameter} is not an instance of {@link ParameterValueGroup} or
     *     {@link ParameterValue}, as only these types are supported.
     */
    public void append(final GeneralParameterValue parameter) {
        if (parameter instanceof ParameterValueGroup) {
            for (final GeneralParameterValue param : ((ParameterValueGroup) parameter).values()) {
                append(param);
            }
        }
        if (parameter instanceof ParameterValue) {
            final ParameterValue<?> param = (ParameterValue) parameter;
            final ParameterDescriptor<?> descriptor = param.getDescriptor();
            final Unit<?> valueUnit = descriptor.getUnit();
            String name = getName(descriptor);
            if (name != null && !name.isBlank()) {
                if (valueUnit != null) {
                    double value;
                    try {
                        value = param.doubleValue();
                    } catch (IllegalStateException exception) {
                        // May happen if a parameter is mandatory (e.g. "semi-major")
                        // but no value has been set for this parameter.
                        value = Double.NaN;
                    }
                    append(name, value);
                } else {
                    buffer.append("+" + name);
                    buffer.append("=");
                    appendObject(param.getValue());
                    buffer.append(" ");
                }
            }
        }
    }

    /**
     * Appends the PROJ string representation of the specified unit to the internal buffer.
     *
     * <p>This method adds the unit symbol to the buffer in a format compatible with PROJ, typically as a `+units=`
     * key-value pair. If the unit's symbol is not available, it uses the unit's string representation instead.
     *
     * @param unit the {@link Unit} to format and append. If {@code unit} is {@code null}, nothing is appended to the
     *     buffer.
     *     <p><b>Example:</b>
     *     <pre>
     * +units=m
     * </pre>
     */
    public void append(final Unit<?> unit) {
        if (unit != null) {
            buffer.append("+units=");
            String symbol = unit.getSymbol();
            if (symbol == null) {
                symbol = unit.toString();
            }
            buffer.append(symbol);
        }
    }

    /**
     * Append the specified value to a string buffer. If the value is an array, then the array elements are appended
     * recursively (i.e. the array may contains sub-array).
     */
    private void appendObject(final Object value) {
        if (value == null) {
            buffer.append("null");
            return;
        }
        if (value.getClass().isArray()) {
            buffer.append('{');
            final int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                if (i != 0) {
                    buffer.append(',').append(' ');
                }
                appendObject(Array.get(value, i));
            }
            buffer.append('}');
            return;
        }
        if (value instanceof Number) {
            format((Number) value);
        } else {
            buffer.append('"').append(value).append('"');
        }
    }

    /**
     * Appends a PROJ-compatible key-value pair to the internal buffer, where the value is a string.
     *
     * <p>This method formats the key and value in the style expected by PROJ. The result format is `+key=value`.
     *
     * @param key the key to be appended, representing the parameter name.
     * @param value the value to be appended, represented as a string.
     *     <p><b>Example:</b>
     *     <pre>
     * +proj=longlat
     * </pre>
     */
    public void append(String key, String value) {
        buffer.append(" +").append(key).append("=").append(value).append(" ");
    }

    /**
     * Appends a PROJ-compatible key-value pair to the internal buffer, where the value is a double.
     *
     * <p>This method formats the key and numeric value in a style compatible with PROJ. The result format is
     * `+key=value`, with the value formatted to the appropriate precision using {@link #format(double)}.
     *
     * @param key the key to be appended, representing the parameter name.
     * @param number the value to be appended, represented as a double.
     *     <p><b>Example:</b>
     *     <pre>
     * +rf=299.1528128
     * </pre>
     */
    public void append(String key, final double number) {
        buffer.append(" +").append(key).append("=");
        format(number);
        buffer.append(" ");
    }

    /**
     * Returns the preferred name for the specified object. If the specified object contains a name from the preferred
     * authority then this name is returned. Otherwise, it will be added to not parseable list
     *
     * @param info The object to looks for a preferred name.
     * @return The preferred name.
     */
    public String getName(final IdentifiedObject info) {
        final Identifier name = info.getName();
        if (Citations.PROJ != name.getAuthority()) {
            final Collection<GenericName> aliases = info.getAlias();
            if (aliases != null && !aliases.isEmpty()) {
                /*
                 * The main name doesn't matches. Search in alias. We will first
                 * check if alias implements Identifier (this is the case of
                 * Geotools implementation). Otherwise, we will look at the
                 * scope in generic name.
                 */
                for (final GenericName alias : aliases) {
                    if (alias instanceof Identifier) {
                        final Identifier candidate = (Identifier) alias;
                        if (Citations.PROJ == candidate.getAuthority()) {
                            if (info instanceof DefaultOperationMethod) {
                                projectedCRS = true;
                            }
                            return candidate.getCode();
                        }
                    }
                }
                // The "null" locale argument is required for getting the unlocalized version.
                final String title = Citations.PROJ.getTitle().toString(null);
                for (final GenericName alias : aliases) {
                    final GenericName scope = alias.scope().name();
                    if (scope != null) {
                        if (title.equalsIgnoreCase(scope.toString())) {
                            if (info instanceof Datum) {
                                datumProvided = true;
                            } else if (info instanceof Ellipsoid) {
                                ellipsoidProvided = true;
                            }
                            return alias.tip().toString();
                        }
                    }
                }
            }
            if (info instanceof Ellipsoid) {
                String projAlias = PROJ_ALIASES.getEllipsoidAlias(info.getName().getCode());
                if (projAlias != null) {
                    ellipsoidProvided = true;
                    return projAlias;
                }
            }
            if (info instanceof PrimeMeridian) {
                String projAlias =
                        PROJ_ALIASES.getPrimeMeridianAlias(info.getName().getCode());
                if (projAlias != null) {
                    primeMeridianProvided = true;
                    return projAlias;
                }
            }
        }
        return "";
    }

    /**
     * Retrieves the identifier for a given {@link IdentifiedObject}.
     *
     * @param info the {@link IdentifiedObject} from which to retrieve an identifier. May be {@code null}, in which case
     *     this method returns {@code null}.
     * @return the {@link Identifier} with the authority {@code Citations.PROJ} if found, or the first identifier in the
     *     collection if no match with {@code Citations.PROJ} is found. Returns {@code null} if {@code info} is
     *     {@code null} or has no identifiers.
     */
    public Identifier getIdentifier(final IdentifiedObject info) {
        Identifier first = null;
        if (info != null) {
            final Collection<? extends Identifier> identifiers = info.getIdentifiers();
            if (identifiers != null) {
                for (final Identifier id : identifiers) {
                    if (Citations.PROJ == id.getAuthority()) {
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
     * Clears the internal state of the formatter to allow for reuse.
     *
     * <p>This method resets the internal buffer and all tracking flags, preparing the formatter for a new PROJ string
     * generation. It can be used to reuse the same formatter instance for multiple formatting operations.
     *
     * <p>This is useful when the formatter instance needs to be reused for different projections or coordinate
     * reference systems without creating a new instance each time.
     */
    public void clear() {
        if (buffer != null) {
            buffer.setLength(0);
        }
        datumProvided = false;
        ellipsoidProvided = false;
        primeMeridianProvided = false;
        projectedCRS = false;
    }

    /** Format an arbitrary number. */
    private void format(final Number number) {
        if (number instanceof Byte || number instanceof Short || number instanceof Integer) {
            format(number.intValue());
        } else {
            format(number.doubleValue());
        }
    }

    /** Formats an integer number. */
    private void format(final int number) {
        final int fraction = numberFormat.getMinimumFractionDigits();
        numberFormat.setMinimumFractionDigits(0);
        numberFormat.format(number, buffer, FP_ZERO);
        numberFormat.setMinimumFractionDigits(fraction);
    }

    /** Formats a floating point number. */
    private void format(final double number) {
        if (number == Math.floor(number)) {
            format((int) number);
        } else {
            numberFormat.format(number, buffer, FP_ZERO);
        }
    }

    /**
     * That's the main method to get the PROJ String out of a {@link PROJFormattable}.
     *
     * @param formattable the identified object to be formatted
     * @return
     */
    public String toPROJ(PROJFormattable formattable) {
        if (formattable instanceof IdentifiedObject) {
            IdentifiedObject identifiedObject = (IdentifiedObject) formattable;
            formattable.formatPROJ(this);
            String refinedString = PROJ_REFINER.refine(
                    buffer.toString(),
                    identifiedObject.getIdentifiers().iterator().next().getCode());
            buffer = new StringBuffer(refinedString);
            return refinedString;
        } else {
            throw new UnsupportedOperationException(
                    "PROJ String is not supported for this type of object: " + formattable);
        }
    }
}
