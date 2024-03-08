/*
 * Units of Measurement Reference Implementation
 * Copyright (c) 2005-2023, Jean-Marie Dautelle, Werner Keil, Otavio Santana.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *    and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of JSR-385, Indriya nor the names of their contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.geotools.measure;

import tech.units.indriya.AbstractUnit;
import tech.units.indriya.format.AbstractUnitFormat;
import tech.units.indriya.function.AddConverter;
import tech.units.indriya.function.MultiplyConverter;
import tech.units.indriya.function.RationalNumber;
import tech.units.indriya.unit.Units;
import tech.units.indriya.unit.*;

import javax.measure.*;
import javax.measure.format.MeasurementParseException;
import javax.measure.format.UnitFormat;
import java.io.IOException;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static javax.measure.MetricPrefix.MICRO;
import static tech.units.indriya.format.FormatConstants.MIDDLE_DOT;

/**
 * <p>
 * This class implements the {@link UnitFormat} interface for formatting and parsing {@link Unit units}.
 * </p>
 *
 * <p>
 * For all SI units, the <b>24 SI prefixes</b> used to form decimal multiples and sub-multiples are recognized. As well as the <b>8 binary prefixes</b>.<br>
 * {@link Units} are directly recognized. For example:<br>
 * <code>
 *        UnitFormat format = SimpleUnitFormat.getInstance();<br>
 *        format.parse("m°C").equals(MetricPrefix.MILLI(Units.CELSIUS));<br>
 *        format.parse("kW").equals(MetricPrefix.KILO(Units.WATT));<br>
 *        format.parse("ft").equals(Units.METRE.multiply(0.3048))</code>
 * </p>
 *
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @author <a href="mailto:werner@units.tech">Werner Keil</a>
 * @author Eric Russell
 * @author Andi Huber
 * @version 2.10, June 6, 2023
 * @since 1.0
 */
public abstract class SimpleUnitFormat extends AbstractUnitFormat {
    /**
     *
     */
    // private static final long serialVersionUID = 4149424034841739785L;#

    /**
     * Flavor of this format
     *
     * @author Werner
     *
     */
    public static enum Flavor {
        Default, ASCII
    }

    private static final String MU = "\u03bc";

    /**
     * Holds the standard unit format.
     */
    private static final DefaultFormat DEFAULT = new DefaultFormat().init();

    /**
     * Holds the ASCIIFormat flavor.
     */
    private static final ASCIIFormat ASCII = new ASCIIFormat().init();


    /**
     * Returns the globally shared unit format instance (used by {@link AbstractUnit#parse(CharSequence) AbstractUnit.parse()} and
     * {@link AbstractUnit#toString() AbstractUnit.toString()}).
     *
     * @return the default unit format.
     */
    public static SimpleUnitFormat getInstance() {
        return getInstance(Flavor.Default);
    }

    /**
     * Returns the {@link SimpleUnitFormat} in the desired {@link Flavor}
     *
     * @return the instance for the given {@link Flavor}.
     */
    public static SimpleUnitFormat getInstance(Flavor flavor) {
        switch (flavor) {
        case ASCII:
            return SimpleUnitFormat.ASCII;
        default:
            return DEFAULT;
        }
    }

    /**
     * Similar to {@link #getInstance()}, but returns a new, non-shared unit format instance,
     * instead of a shared singleton instance.
     *
     * @return a new instance of the default unit format.
     * @see #getInstance()
     * @since 2.7
     */
    public static SimpleUnitFormat getNewInstance() {
        return getNewInstance(Flavor.Default);
    }

    /**
     * Similar to {@link #getInstance(Flavor)}, but returns a new {@link SimpleUnitFormat} instance in the desired
     * {@link Flavor}, instead of a shared singleton instance.
     *
     * @return a new instance for the given {@link Flavor}.
     * @see #getInstance(Flavor)
     * @since 2.7 
     */
    public static SimpleUnitFormat getNewInstance(Flavor flavor) {
        switch (flavor) {
        case ASCII:
            return new ASCIIFormat().init();
        default:
            return new DefaultFormat().init();
        }
    }

    /**
     * Base constructor.
     */
    protected SimpleUnitFormat() {
    }

    /**
     * Formats the specified unit.
     *
     * @param unit
     *          the unit to format.
     * @param appendable
     *          the appendable destination.
     * @throws IOException
     *           if an error occurs.
     */
    public abstract Appendable format(Unit<?> unit, Appendable appendable) throws IOException;

    /**
     * Parses a sequence of character to produce a unit or a rational product of unit.
     *
     * @param csq
     *          the <code>CharSequence</code> to parse.
     * @param pos
     *          an object holding the parsing index and error position.
     * @return an {@link Unit} parsed from the character sequence.
     * @throws IllegalArgumentException
     *           if the character sequence contains an illegal syntax.
     */
    @SuppressWarnings("rawtypes")
    public abstract Unit<? extends Quantity> parseProductUnit(CharSequence csq, ParsePosition pos) throws MeasurementParseException;

    /**
     * Parses a sequence of character to produce a single unit.
     *
     * @param csq
     *          the <code>CharSequence</code> to parse.
     * @param pos
     *          an object holding the parsing index and error position.
     * @return an {@link Unit} parsed from the character sequence.
     * @throws IllegalArgumentException
     *           if the character sequence does not contain a valid unit identifier.
     */
    @SuppressWarnings("rawtypes")
    public abstract Unit<? extends Quantity> parseSingleUnit(CharSequence csq, ParsePosition pos) throws MeasurementParseException;

    /**
     * Attaches a system-wide label to the specified unit. For example: <code>SimpleUnitFormat.getInstance().label(DAY.multiply(365), "year");
     * SimpleUnitFormat.getInstance().label(METER.multiply(0.3048), "ft");</code> If the specified label is already associated to an unit the previous
     * association is discarded or ignored.
     * <p>
     * If you set a different label without calling {@link #removeLabel(Unit)}), {@link #removeAlias(Unit, String)}), using the old label, or {@link #removeAliases(Unit)}) on the given unit, the old label is overwritten for <b>labeling/<b> purposes, but it remains like an <b>alias</b> (it still works for parsing). 
     * </p>
     * @param unit
     *          the unit being labeled.
     * @param label
     *          the new label for this unit.
     * @throws IllegalArgumentException
     *           if the label is not a {@link SimpleUnitFormat#isValidIdentifier(String)} valid identifier.
     */
    public abstract void label(Unit<?> unit, String label);

	/**
	 * Removes the system-wide label (added by {@link #label(Unit, String)}) and all system-wide aliases (added by {@link #alias(Unit, String)}) for this unit.
	 *
	 * @param unit
	 *          the unit for which label shall be removed.
	 */
	public abstract void removeLabel(Unit<?> unit);

	/**
     * Attaches a system-wide alias to this unit. Multiple aliases may be attached to the same unit. Aliases are used during parsing to recognize
     * different variants of the same unit. For example: <code> SimpleUnitFormat.getInstance().alias(METER.multiply(0.3048), "foot");
     * SimpleUnitFormat.getInstance().alias(METER.multiply(0.3048), "feet"); SimpleUnitFormat.getInstance().alias(METER, "meter");
     * SimpleUnitFormat.getInstance().alias(METER, "metre"); </code> If the specified label is already associated to an unit the previous association is
     * discarded or ignored.
     *
     * @param unit
     *          the unit being aliased.
     * @param alias
     *          the alias attached to this unit.
     * @throws IllegalArgumentException
     *           if the label is not a {@link SimpleUnitFormat#isValidIdentifier(String)} valid identifier.
     */
    public abstract void alias(Unit<?> unit, String alias);

	/**
	 * Removes the given system-wide alias (added by {@link #alias(Unit, String)}) for this unit and keeps the label (added by {@link #label(Unit, String)})
	 *
	 * @param unit
	 *          the unit for which alias shall be removed.
	 *          
	 * @param alias
	 *          the alias to be removed.          
	 */
	public abstract void removeAlias(Unit<?> unit, String alias);
    
	/**
	 * Removes all system-wide aliases (added by {@link #alias(Unit, String)}) for this unit and keeps the label (added by {@link #label(Unit, String)})
	 *
	 * @param unit
	 *          the unit for which aliases shall be removed.
	 */
	public abstract void removeAliases(Unit<?> unit);

    /**
     * Indicates if the specified name can be used as unit identifier.
     *
     * @param name
     *          the identifier to be tested.
     * @return <code>true</code> if the name specified can be used as label or alias for this format;<code>false</code> otherwise.
     */
    protected abstract boolean isValidIdentifier(String name);

    /**
     * Formats an unit and appends the resulting text to a given string buffer (implements <code>java.text.Format</code>).
     *
     * @param unit
     *          the unit to format.
     * @param toAppendTo
     *          where the text is to be appended
     * @param pos
     *          the field position (not used).
     * @return <code>toAppendTo</code>
     */
    public final StringBuffer format(Object unit, final StringBuffer toAppendTo, FieldPosition pos) {
        try {
            final Object dest = toAppendTo;
            if (dest instanceof Appendable) {
                format((Unit<?>) unit, (Appendable) dest);
            } else { // When retroweaver is used to produce 1.4 binaries. TODO is this still relevant?
                format((Unit<?>) unit, new Appendable() {
                    public Appendable append(char arg0) throws IOException {
                        toAppendTo.append(arg0);
                        return null;
                    }
                    public Appendable append(CharSequence arg0) throws IOException {
                        toAppendTo.append(arg0);
                        return null;
                    }
                    public Appendable append(CharSequence arg0, int arg1, int arg2) throws IOException {
                        toAppendTo.append(arg0.subSequence(arg1, arg2));
                        return null;
                    }
                });
            }
            return toAppendTo;
        } catch (IOException e) {
            throw new MeasurementError(e); // Should never happen.
        }
    }

    /**
     * Parses the text from a string to produce an object (implements <code>java.text.Format</code>).
     *
     * @param source
     *          the string source, part of which should be parsed.
     * @param pos
     *          the cursor position.
     * @return the corresponding unit or <code>null</code> if the string cannot be parsed.
     */
    public final Unit<?> parseObject(String source, ParsePosition pos) throws MeasurementParseException {
        return parseProductUnit(source, pos);
    }

    /**
     * This class represents an exponent with both a power (numerator) and a root (denominator).
     */
    private static class Exponent {
        public final int pow;
        public final int root;

        public Exponent(int pow, int root) {
            this.pow = pow;
            this.root = root;
        }
    }

    /**
     * This class represents the default (Unicode) format.
     * internal class, please extend either SimpleUnitFormat or AbstractUnitFormat
     */
    static class DefaultFormat extends SimpleUnitFormat {

        // Initializes the standard unit databases.

        static final Unit<?>[] METRIC_UNITS = { Units.AMPERE, Units.BECQUEREL, Units.CANDELA, Units.COULOMB, Units.FARAD, Units.GRAY, Units.HENRY,
                Units.HERTZ, Units.JOULE, Units.KATAL, Units.KELVIN, Units.LUMEN, Units.LUX, Units.METRE, Units.MOLE, Units.NEWTON, Units.OHM, Units.PASCAL,
                Units.RADIAN, Units.SECOND, Units.SIEMENS, Units.SIEVERT, Units.STERADIAN, Units.TESLA, Units.VOLT, Units.WATT, Units.WEBER };

        static final String[] METRIC_PREFIX_SYMBOLS =
                Stream.of(MetricPrefix.values())
                .map(Prefix::getSymbol)
                .collect(Collectors.toList())
                .toArray(new String[] {});

        // TODO try to consolidate those
        static final UnitConverter[] METRIC_PREFIX_CONVERTERS =
                Stream.of(MetricPrefix.values())
                .map(MultiplyConverter::ofPrefix)
                .collect(Collectors.toList())
                .toArray(new UnitConverter[] {});

        static final String[] BINARY_PREFIX_SYMBOLS =
                Stream.of(BinaryPrefix.values())
                .map(Prefix::getSymbol)
                .collect(Collectors.toList())
                .toArray(new String[] {});

        static final UnitConverter[] BINARY_PREFIX_CONVERTERS =
                Stream.of(BinaryPrefix.values())
                .map(MultiplyConverter::ofPrefix)
                .collect(Collectors.toList())
                .toArray(new UnitConverter[] {});

        /**
         * Holds the unique symbols collection (base units or alternate units).
         */
        private final Map<String, Unit<?>> symbolToUnit = new HashMap<>();

        private static enum Token { EOF, IDENTIFIER, OPEN_PAREN, CLOSE_PAREN, EXPONENT, MULTIPLY, DIVIDE,
            PLUS, INTEGER, FLOAT };


        DefaultFormat() {

            // Hack, somehow µg is not found.
            symbolToUnit.put(MetricPrefix.MICRO.getSymbol() + "g", MICRO(Units.GRAM));
            symbolToUnit.put("μg", MICRO(Units.GRAM));
            symbolToUnit.put(MU + "g", MICRO(Units.GRAM));
        }

        private DefaultFormat init() {

            for (int i = 0; i < METRIC_UNITS.length; i++) {
                Unit<?> si = METRIC_UNITS[i];
                String symbol = (si instanceof BaseUnit) ? ((BaseUnit<?>) si).getSymbol() : ((AlternateUnit<?>) si).getSymbol();
                label(si, symbol);
                for (int j = 0; j < METRIC_PREFIX_SYMBOLS.length; j++) {
                    Unit<?> u = si.prefix(MetricPrefix.values()[j]);
                    label(u, METRIC_PREFIX_SYMBOLS[j] + symbol);
                    if ( "µ".equals(METRIC_PREFIX_SYMBOLS[j]) ) {
                        label(u, MU + symbol);
                    }
                } // TODO what about BINARY_PREFIX here?
            }

            // -- GRAM/KILOGRAM

            label(Units.GRAM, "g");
            for(MetricPrefix prefix : MetricPrefix.values()) {
                switch (prefix) {
                case KILO:
                    label(Units.KILOGRAM, "kg");
                    break;
                case MICRO:
                    label(Units.GRAM.prefix(prefix), prefix.getSymbol()+"g");
                    break;
                default:
                    label(Units.GRAM.prefix(prefix), prefix.getSymbol()+"g");
                    break;
                }
            }

            label(MICRO(Units.GRAM), MetricPrefix.MICRO.getSymbol() + "g");

            // Alias and ASCIIFormat for Ohm
            alias(Units.OHM, "Ohm");
            for (int i = 0; i < METRIC_PREFIX_SYMBOLS.length; i++) {
                alias(Units.OHM.prefix(MetricPrefix.values()[i]), METRIC_PREFIX_SYMBOLS[i] + "Ohm");
            }

            // Special case for DEGREE_CELSIUS.
            label(Units.CELSIUS, "℃");
            alias(Units.CELSIUS, "°C");
            for (int i = 0; i < METRIC_PREFIX_SYMBOLS.length; i++) {
                label(Units.CELSIUS.prefix(MetricPrefix.values()[i]), METRIC_PREFIX_SYMBOLS[i] + "℃");
                alias(Units.CELSIUS.prefix(MetricPrefix.values()[i]), METRIC_PREFIX_SYMBOLS[i] + "°C");
            }

            label(Units.PERCENT, "%");
            label(Units.METRE, "m");
            label(Units.SECOND, "s");
            label(Units.MINUTE, "min");
            label(Units.HOUR, "h");
            label(Units.DAY, "day");
            alias(Units.DAY, "d");
            label(Units.WEEK, "week");
            label(Units.YEAR, "year");
            alias(Units.YEAR, "days365");
            label(Units.MONTH, "mo");
            alias(Units.MONTH, "mon");
            alias(Units.MONTH, "month");
            label(Units.KILOMETRE_PER_HOUR, "km/h");
            label(Units.CUBIC_METRE, "\u33A5");

            // -- LITRE

            label(Units.LITRE, "l");
            for(Prefix prefix : MetricPrefix.values()) {
                label(Units.LITRE.prefix(prefix), prefix.getSymbol()+"l");
            }
            label(Units.NEWTON, "N");
            label(Units.RADIAN, "rad");

            label(AbstractUnit.ONE, "one");

            alias(Units.SQUARE_METRE, "m2");
            alias(Units.CUBIC_METRE, "m3");

            return this;
        }


        /**
         * Holds the name to unit mapping.
         */
        protected final Map<String, Unit<?>> nameToUnit = new HashMap<>();

        /**
         * Holds the unit to name mapping.
         */
        protected final Map<Unit<?>, String> unitToName = new HashMap<>();

        @Override
        public String toString() {
            return "SimpleUnitFormat";
        }

        @Override
        public void label(Unit<?> unit, String label) {
            if (!isValidIdentifier(label))
                throw new IllegalArgumentException("Label: " + label + " is not a valid identifier.");
            synchronized (this) {
                nameToUnit.put(label, unit);
                unitToName.put(unit, label);
            }
        }

        @Override
        public void alias(Unit<?> unit, String alias) {
            if (!isValidIdentifier(alias))
                throw new IllegalArgumentException("Alias: " + alias + " is not a valid identifier.");
            synchronized (this) {
                nameToUnit.put(alias, unit);
            }
        }

        @Override
		public void removeAlias(Unit<?> unit, String alias) {
			nameToUnit.remove(alias);
		}
        
		@Override
		public void removeAliases(Unit<?> unit) {
			final String alias = unitToName.get(unit);
			nameToUnit.entrySet().removeIf(e -> e.getValue().equals(unit) && !e.getKey().equals(alias));
		}

		@Override
		public void removeLabel(Unit<?> unit) {
			unitToName.remove(unit);
			nameToUnit.entrySet().removeIf(e -> e.getValue().equals(unit));
		}

        @Override
        protected boolean isValidIdentifier(String name) {
            if ((name == null) || (name.length() == 0))
                return false;
            return isUnitIdentifierPart(name.charAt(0));
        }

        protected static boolean isUnitIdentifierPart(char ch) {
            return Character.isLetter(ch)
                    || (!Character.isWhitespace(ch) && !Character.isDigit(ch) && (ch != MIDDLE_DOT) && (ch != '*') && (ch != '/') && (ch != '(') && (ch != ')')
                            && (ch != '[') && (ch != ']') && (ch != '\u00b9') && (ch != '\u00b2') && (ch != '\u00b3') && (ch != '^') && (ch != '+') && (ch != '-'));
        }

        // Returns the name for the specified unit or null if product unit.
        protected String nameFor(Unit<?> unit) {
            // Searches label database.
            String label = unitToName.get(unit);
            if (label != null)
                return label;
            if (unit instanceof BaseUnit)
                return ((BaseUnit<?>) unit).getSymbol();
            if (unit instanceof AlternateUnit)
                return ((AlternateUnit<?>) unit).getSymbol();
            if (unit instanceof TransformedUnit) {
                TransformedUnit<?> tfmUnit = (TransformedUnit<?>) unit;
                if (tfmUnit.getSymbol() != null) {
                    return tfmUnit.getSymbol();
                }
                Unit<?> baseUnit = tfmUnit.getParentUnit();
                UnitConverter cvtr = tfmUnit.getConverter(); // tfmUnit.getSystemConverter();
                StringBuilder result = new StringBuilder();
                String baseUnitName = baseUnit.toString();
                String prefix = prefixFor(cvtr);
                if ((baseUnitName.indexOf(MIDDLE_DOT) >= 0) || (baseUnitName.indexOf('*') >= 0) || (baseUnitName.indexOf('/') >= 0)) {
                    // We could use parentheses whenever baseUnits is an
                    // instanceof ProductUnit, but most ProductUnits have
                    // aliases,
                    // so we'd end up with a lot of unnecessary parentheses.
                    result.append('(');
                    result.append(baseUnitName);
                    result.append(')');
                } else {
                    result.append(baseUnitName);
                }
                if (prefix != null) {
                    result.insert(0, prefix);
                } else {
                    if (cvtr instanceof AddConverter) {
                        result.append('+');
                        result.append(((AddConverter) cvtr).getOffset());
                    } else if (cvtr instanceof MultiplyConverter) {
                        Number scaleFactor = ((MultiplyConverter) cvtr).getFactor();
                        if(scaleFactor instanceof RationalNumber) {

                            RationalNumber rational = (RationalNumber)scaleFactor;
                            RationalNumber reciprocal = rational.reciprocal();
                            if(reciprocal.isInteger()) {
                                result.append('/');
                                result.append(reciprocal.toString()); // renders as integer
                            } else {
                                result.append('*');
                                result.append(scaleFactor);
                            }

                        } else {
                            result.append('*');
                            result.append(scaleFactor);
                        }

                    } else { // Other converters.
                        return "[" + baseUnit + "?]";
                    }
                }
                return result.toString();
            }
            if (unit instanceof AnnotatedUnit<?>) {
                AnnotatedUnit<?> annotatedUnit = (AnnotatedUnit<?>) unit;
                final StringBuilder annotable = new StringBuilder(nameFor(annotatedUnit.getActualUnit()));
                if (annotatedUnit.getAnnotation() != null) {
                    annotable.append('{'); // TODO maybe also configure this one similar to mix delimiter
                    annotable.append(annotatedUnit.getAnnotation());
                    annotable.append('}');
                }
                return annotable.toString();
            }
            return null; // Product unit.
        }

        // Returns the prefix for the specified unit converter.
        protected String prefixFor(UnitConverter converter) {
            for (int i = 0; i < METRIC_PREFIX_CONVERTERS.length; i++) {
                if (METRIC_PREFIX_CONVERTERS[i].equals(converter)) {
                    return METRIC_PREFIX_SYMBOLS[i];
                }
            }
            for (int j = 0; j < BINARY_PREFIX_CONVERTERS.length; j++) {
                if (BINARY_PREFIX_CONVERTERS[j].equals(converter)) {
                    return BINARY_PREFIX_SYMBOLS[j];
                }
            }
            return null; // TODO or return blank?
        }

        // Returns the unit for the specified name.
        protected Unit<?> unitFor(String name) {
            Unit<?> unit = nameToUnit.get(name);
            if (unit != null) {
                return unit;
            } else {
                unit = symbolToUnit.get(name);
            }
            return unit;
        }

        // //////////////////////////
        // Parsing.
        @SuppressWarnings({ "rawtypes", "unchecked" })
        public Unit<? extends Quantity> parseSingleUnit(CharSequence csq, ParsePosition pos) throws MeasurementParseException {
            int startIndex = pos.getIndex();
            String name = readIdentifier(csq, pos);
            Unit unit = unitFor(name);
            check(unit != null, name + " not recognized", csq, startIndex);
            return unit;
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public Unit<? extends Quantity> parseProductUnit(CharSequence csq, ParsePosition pos) throws MeasurementParseException {
        	Unit result = null;
        	if (csq == null) {
            	throw new MeasurementParseException("Cannot parse null", csq, pos.getIndex());
            } else {
            	result = unitFor(csq.toString());
            	if (result != null)
            		return result;
            }
        	result = AbstractUnit.ONE;
            Token token = nextToken(csq, pos);
            switch (token) {
            case IDENTIFIER:
                result = parseSingleUnit(csq, pos);
                break;
            case OPEN_PAREN:
                pos.setIndex(pos.getIndex() + 1);
                result = parseProductUnit(csq, pos);
                token = nextToken(csq, pos);
                check(token == Token.CLOSE_PAREN, "')' expected", csq, pos.getIndex());
                pos.setIndex(pos.getIndex() + 1);
                break;
            default:
                break;
            }
            token = nextToken(csq, pos);
            while (true) {
                switch (token) {
                case EXPONENT:
                    Exponent e = readExponent(csq, pos);
                    if (e.pow != 1) {
                        result = result.pow(e.pow);
                    }
                    if (e.root != 1) {
                        result = result.root(e.root);
                    }
                    break;
                case MULTIPLY:
                    pos.setIndex(pos.getIndex() + 1);
                    token = nextToken(csq, pos);
                    if (token == Token.INTEGER) {
                        long n = readLong(csq, pos);
                        if (n != 1) {
                            result = result.multiply(n);
                        }
                    } else if (token == Token.FLOAT) {
                        double d = readDouble(csq, pos);
                        if (d != 1.0) {
                            result = result.multiply(d);
                        }
                    } else {
                        result = result.multiply(parseProductUnit(csq, pos));
                    }
                    break;
                case DIVIDE:
                    pos.setIndex(pos.getIndex() + 1);
                    token = nextToken(csq, pos);
                    if (token == Token.INTEGER) {
                        long n = readLong(csq, pos);
                        if (n != 1) {
                            result = result.divide(n);
                        }
                    } else if (token == Token.FLOAT) {
                        double d = readDouble(csq, pos);
                        if (d != 1.0) {
                            result = result.divide(d);
                        }
                    } else {
                        result = result.divide(parseProductUnit(csq, pos));
                    }
                    break;
                case PLUS:
                    pos.setIndex(pos.getIndex() + 1);
                    token = nextToken(csq, pos);
                    if (token == Token.INTEGER) {
                        long n = readLong(csq, pos);
                        if (n != 1) {
                            result = result.shift(n);
                        }
                    } else if (token == Token.FLOAT) {
                        double d = readDouble(csq, pos);
                        if (d != 1.0) {
                            result = result.shift(d);
                        }
                    } else {
                        throw new MeasurementParseException("not a number", csq, pos.getIndex());
                    }
                    break;
                case EOF:
                case CLOSE_PAREN:
                    return result;
                default:
                    throw new MeasurementParseException("unexpected token " + token, csq, pos.getIndex());
                }
                token = nextToken(csq, pos);
            }
        }

        private static Token nextToken(CharSequence csq, ParsePosition pos) {
            final int length = csq.length();
            while (pos.getIndex() < length) {
                char c = csq.charAt(pos.getIndex());
                if (isUnitIdentifierPart(c)) {
                    return Token.IDENTIFIER;
                } else if (c == '(') {
                    return Token.OPEN_PAREN;
                } else if (c == ')') {
                    return Token.CLOSE_PAREN;
                } else if ((c == '^') || (c == '\u00b9') || (c == '\u00b2') || (c == '\u00b3')) {
                    return Token.EXPONENT;
                } else if (c == '*') {
                    if (csq.length() == pos.getIndex() + 1) {
                        throw new MeasurementParseException("unexpected token " + Token.EOF, csq, pos.getIndex()); // return ;
                    }
                    char c2 = csq.charAt(pos.getIndex() + 1);
                    return c2 == '*' ? Token.EXPONENT : Token.MULTIPLY;
                } else if (c == MIDDLE_DOT) {
                    return Token.MULTIPLY;
                } else if (c == '/') {
                    return Token.DIVIDE;
                } else if (c == '+') {
                    return Token.PLUS;
                } else if ((c == '-') || Character.isDigit(c)) {
                    int index = pos.getIndex() + 1;
                    while ((index < length) && (Character.isDigit(c) || (c == '-') || (c == '.') || (c == 'E'))) {
                        c = csq.charAt(index++);
                        if (c == '.') {
                            return Token.FLOAT;
                        }
                    }
                    return Token.INTEGER;
                }
                pos.setIndex(pos.getIndex() + 1);
            }
            return Token.EOF;
        }

        private static void check(boolean expr, String message, CharSequence csq, int index) throws MeasurementParseException {
            if (!expr) {
                throw new MeasurementParseException(message + " (in " + csq + " at index " + index + ")", index);
            }
        }

        private static Exponent readExponent(CharSequence csq, ParsePosition pos) {
            char c = csq.charAt(pos.getIndex());
            if (c == '^') {
                pos.setIndex(pos.getIndex() + 1);
            } else if (c == '*') {
                pos.setIndex(pos.getIndex() + 2);
            }
            final int length = csq.length();
            int pow = 0;
            boolean isPowNegative = false;
            boolean parseRoot = false;

            POWERLOOP: while (pos.getIndex() < length) {
                c = csq.charAt(pos.getIndex());
                switch(c) {
                case '-': isPowNegative = true; break;
                case '\u00b9': pow = pow * 10 + 1; break;
                case '\u00b2': pow = pow * 10 + 2; break;
                case '\u00b3': pow = pow * 10 + 3; break;
                case ':': parseRoot = true; break POWERLOOP;
                default:
                    if (c >= '0' && c <= '9') pow = pow * 10 + (c - '0');
                    else break POWERLOOP;
                }
                pos.setIndex(pos.getIndex() + 1);
            }
            if (pow == 0) pow = 1;

            int root = 0;
            boolean isRootNegative = false;
            if (parseRoot) {
                pos.setIndex(pos.getIndex() + 1);
                ROOTLOOP: while (pos.getIndex() < length) {
                    c = csq.charAt(pos.getIndex());
                    switch(c) {
                    case '-': isRootNegative = true; break;
                    case '\u00b9': root = root * 10 + 1; break;
                    case '\u00b2': root = root * 10 + 2; break;
                    case '\u00b3': root = root * 10 + 3; break;
                    default:
                        if (c >= '0' && c <= '9') root = root * 10 + (c - '0');
                        else break ROOTLOOP;
                    }
                    pos.setIndex(pos.getIndex() + 1);
                }
            }
            if (root == 0) root = 1;

            return new Exponent(isPowNegative ? -pow : pow, isRootNegative ? -root : root);
        }

        private static long readLong(CharSequence csq, ParsePosition pos) {
            final int length = csq.length();
            int result = 0;
            boolean isNegative = false;
            while (pos.getIndex() < length) {
                char c = csq.charAt(pos.getIndex());
                if (c == '-') {
                    isNegative = true;
                } else if ((c >= '0') && (c <= '9')) {
                    result = result * 10 + (c - '0');
                } else {
                    break;
                }
                pos.setIndex(pos.getIndex() + 1);
            }
            return isNegative ? -result : result;
        }

        private static double readDouble(CharSequence csq, ParsePosition pos) {
            final int length = csq.length();
            int start = pos.getIndex();
            int end = start + 1;
            while (end < length) {
                if ("0123456789+-.E".indexOf(csq.charAt(end)) < 0) {
                    break;
                }
                end += 1;
            }
            pos.setIndex(end + 1);
            return Double.parseDouble(csq.subSequence(start, end).toString());
        }

        private static String readIdentifier(CharSequence csq, ParsePosition pos) {
            final int length = csq.length();
            int start = pos.getIndex();
            int i = start;
            while ((++i < length) && isUnitIdentifierPart(csq.charAt(i))) {
            }
            pos.setIndex(i);
            return csq.subSequence(start, i).toString();
        }

        // //////////////////////////
        // Formatting.

        @Override
        public Appendable format(Unit<?> unit, Appendable appendable) throws IOException {
            String name = nameFor(unit);
            if (name != null) {
                return appendable.append(name);
            }
            if (!(unit instanceof ProductUnit)) {
                throw new IllegalArgumentException("Cannot format given Object as a Unit");
            }

            // Product unit.
            ProductUnit<?> productUnit = (ProductUnit<?>) unit;

            // Special case: self-powered product unit
            if (productUnit.getUnitCount() == 1 && productUnit.getUnit(0) instanceof ProductUnit) {
                final ProductUnit<?> powerUnit = (ProductUnit<?>) productUnit.getUnit(0);
                // is the sub-unit known under a given label?
                if (nameFor(powerUnit) == null)
                    // apply the power to the sub-units and format those instead
                    return format(ProductUnit.ofPow(powerUnit, productUnit.getUnitPow(0)), appendable);
            }

            int invNbr = 0;

            // Write positive exponents first.
            boolean start = true;
            for (int i = 0; i < productUnit.getUnitCount(); i++) {
                int pow = productUnit.getUnitPow(i);
                if (pow >= 0) {
                    if (!start) {
                        appendable.append(MIDDLE_DOT); // Separator.
                    }
                    name = nameFor(productUnit.getUnit(i));
                    int root = productUnit.getUnitRoot(i);
                    append(appendable, name, pow, root);
                    start = false;
                } else {
                    invNbr++;
                }
            }

            // Write negative exponents.
            if (invNbr != 0) {
                if (start) {
                    appendable.append('1'); // e.g. 1/s
                }
                appendable.append('/');
                if (invNbr > 1) {
                    appendable.append('(');
                }
                start = true;
                for (int i = 0; i < productUnit.getUnitCount(); i++) {
                    int pow = productUnit.getUnitPow(i);
                    if (pow < 0) {
                        name = nameFor(productUnit.getUnit(i));
                        int root = productUnit.getUnitRoot(i);
                        if (!start) {
                            appendable.append(MIDDLE_DOT); // Separator.
                        }
                        append(appendable, name, -pow, root);
                        start = false;
                    }
                }
                if (invNbr > 1) {
                    appendable.append(')');
                }
            }
            return appendable;
        }

        private static void append(Appendable appendable, CharSequence symbol, int pow, int root) throws IOException {
            appendable.append(symbol);
            if ((pow != 1) || (root != 1)) {
                // Write exponent.
                if ((pow == 2) && (root == 1)) {
                    appendable.append('\u00b2'); // Square
                } else if ((pow == 3) && (root == 1)) {
                    appendable.append('\u00b3'); // Cubic
                } else {
                    // Use general exponent form.
                    appendable.append('^');
                    appendable.append(String.valueOf(pow));
                    if (root != 1) {
                        appendable.append(':');
                        appendable.append(String.valueOf(root));
                    }
                }
            }
        }

        // private static final long serialVersionUID = 1L;

        @Override
        public Unit<?> parse(CharSequence csq) throws MeasurementParseException {
            return parse(csq, 0);
        }

        protected Unit<?> parse(CharSequence csq, int index) throws IllegalArgumentException {
            return parse(csq, new ParsePosition(index));
        }

        @Override
        public Unit<?> parse(CharSequence csq, ParsePosition cursor) throws IllegalArgumentException {
            return parseObject(csq.toString(), cursor);
        }
    }

    /**
     * This class represents the ASCII format.
     */
    private static final class ASCIIFormat extends DefaultFormat {

        private ASCIIFormat() {
            super();
        }

        private ASCIIFormat init() {

            // ASCII
            for (int i = 0; i < METRIC_UNITS.length; i++) {
                Unit<?> si = METRIC_UNITS[i];
                String symbol = (si instanceof BaseUnit) ? ((BaseUnit<?>) si).getSymbol() : ((AlternateUnit<?>) si).getSymbol();
                if (isAllASCII(symbol))
                    label(si, symbol);
                for (int j = 0; j < METRIC_PREFIX_SYMBOLS.length; j++) {
                    Unit<?> u = si.prefix(MetricPrefix.values()[j]);
                    if ( "µ".equals(METRIC_PREFIX_SYMBOLS[j]) ) {
                        label(u, "micro" + asciiSymbol(symbol));
                    }
                } // TODO what about BINARY_PREFIX here?
            }

            // -- GRAM/KILOGRAM

            label(Units.GRAM, "g");
            for(MetricPrefix prefix : MetricPrefix.values()) {
                switch (prefix) {
                case KILO:
                    label(Units.KILOGRAM, "kg");
                    break;
                case MICRO:
                    label(MICRO(Units.GRAM), "microg"); // instead of 'µg' -> 'microg'
                    break;
                default:
                    label(Units.GRAM.prefix(prefix), prefix.getSymbol()+"g");
                    break;
                }
            }

            // Alias and ASCIIFormat for Ohm
            label(Units.OHM, "Ohm");
            for (int i = 0; i < METRIC_PREFIX_SYMBOLS.length; i++) {
                label(Units.OHM.prefix(MetricPrefix.values()[i]), asciiPrefix(METRIC_PREFIX_SYMBOLS[i]) + "Ohm");
            }

            // Special case for DEGREE_CELSIUS.
            label(Units.CELSIUS, "Celsius");
            for (int i = 0; i < METRIC_PREFIX_SYMBOLS.length; i++) {
                label(Units.CELSIUS.prefix(MetricPrefix.values()[i]), asciiPrefix(METRIC_PREFIX_SYMBOLS[i]) + "Celsius");
            }
            alias(Units.CELSIUS, "Cel");

            label(Units.METRE, "m");
            label(Units.SECOND, "s");
            label(Units.KILOMETRE_PER_HOUR, "km/h");
            alias(Units.SQUARE_METRE, "m2");
            alias(Units.CUBIC_METRE, "m3");

            // -- LITRE

            label(Units.LITRE, "l");
            for(Prefix prefix : MetricPrefix.values()) {
                if(prefix==MICRO) {
                    label(MICRO(Units.LITRE), "microL"); // instead of 'µL' -> 'microL'
                } else {
                    label(Units.LITRE.prefix(prefix), prefix.getSymbol()+"L");
                }
            }
            label(Units.NEWTON, "N");
            label(Units.RADIAN, "rad");

            label(AbstractUnit.ONE, "one");

            return this;
        }


        @Override
        protected String nameFor(Unit<?> unit) {
            // First search if specific ASCII name should be used.
            String name = unitToName.get(unit);
            if (name != null)
                return name;
            // Else returns default name.
            return DEFAULT.nameFor(unit);
        }

        @Override
        protected Unit<?> unitFor(String name) {
            // First search if specific ASCII name.
            Unit<?> unit = nameToUnit.get(name);
            if (unit != null)
                return unit;
            // Else returns default mapping.
            return DEFAULT.unitFor(name);
        }

        @Override
        public String toString() {
            return "SimpleUnitFormat - ASCII";
        }

        @Override
        public Appendable format(Unit<?> unit, Appendable appendable) throws IOException {
            String name = nameFor(unit);
            if (name != null)
                return appendable.append(name);
            if (!(unit instanceof ProductUnit))
                throw new IllegalArgumentException("Cannot format given Object as a Unit");

            ProductUnit<?> productUnit = (ProductUnit<?>) unit;
            for (int i = 0; i < productUnit.getUnitCount(); i++) {
                if (i != 0) {
                    appendable.append('*'); // Separator.
                }
                name = nameFor(productUnit.getUnit(i));
                int pow = productUnit.getUnitPow(i);
                int root = productUnit.getUnitRoot(i);
                appendable.append(name);
                if ((pow != 1) || (root != 1)) {
                    // Use general exponent form.
                    appendable.append('^');
                    appendable.append(String.valueOf(pow));
                    if (root != 1) {
                        appendable.append(':');
                        appendable.append(String.valueOf(root));
                    }
                }
            }
            return appendable;
        }

        @Override
        protected boolean isValidIdentifier(String name) {
            if ((name == null) || (name.length() == 0))
                return false;
            // label must not begin with a digit or mathematical operator
            return isUnitIdentifierPart(name.charAt(0)) && isAllASCII(name);
            /*
             * for (int i = 0; i < name.length(); i++) { if
             * (!isAsciiCharacter(name.charAt(i))) return false; } return true;
             */
        }
    }

    private static String asciiPrefix(String prefix) {
        return "µ".equals(prefix) ? "micro" : prefix;
    }

    private static String asciiSymbol(String s) {
        return "Ω".equals(s) ? "Ohm" : s;
    }

    /** to check if a string only contains US-ASCII characters */
    private static boolean isAllASCII(String input) {
        boolean isASCII = true;
        for (int i = 0; i < input.length(); i++) {
            int c = input.charAt(i);
            if (c > 0x7F) {
                isASCII = false;
                break;
            }
        }
        return isASCII;
    }

}
