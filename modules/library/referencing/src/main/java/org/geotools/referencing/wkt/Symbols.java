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
package org.geotools.referencing.wkt;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;


/**
 * The set of symbols to use for WKT parsing and formatting.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class Symbols {
    /**
     * The default set of symbols.
     */
    public static final Symbols DEFAULT = new Symbols(Locale.US);
    // DONT't invoke the default constructor for this one.

    /**
     * A set of symbols with parameters between square brackets, like {@code [...]}.
     */
    public static final Symbols SQUARE_BRACKETS = DEFAULT;

    /**
     * A set of symbols with parameters between parentheses,
     * like {@code (...)}.
     */
    public static final Symbols CURLY_BRACKETS = new Symbols();
    static {
        CURLY_BRACKETS.open  = '(';
        CURLY_BRACKETS.close = ')';
    }

    /* ----------------------------------------------------------
     * NOTE: Consider all fields below as final.
     *       It is not only in order to make construction easier.
     *       If the informations provided by those fields became
     *       needed outside of this package, then we need to make
     *       them private and declare accessors instead.
     * ---------------------------------------------------------- */

    /**
     * The locale for querying localizable information.
     */
    final Locale locale;

    /**
     * The character used for opening brace.
     * Usually {@code '['}, but {@code '('} is legal as well.
     */
    char open = '[';

    /**
     * The character used for closing brace.
     * Usually {@code ']'}, but {@code ')'} is legal as well.
     */
    char close = ']';

    /**
     * The character used for opening an array or enumeration.
     * Usually {@code '{'}.
     */
    final char openArray = '{';

    /**
     * The character used for closing an array or enumeration.
     * Usually {@code '}'}.
     */
    final char closeArray = '}';

    /**
     * The character used for quote.
     * Usually {@code '"'}.
     */
    final char quote = '"';

    /**
     * The character used as a separator. Usually {@code ','}, but would need
     * to be changed if a non-English locale is used for formatting numbers.
     */
    char separator = ',';

    /**
     * The character used for space.
     * Usually <code>'&nbsp;'</code>, but could be a no-break space too if unicode is allowed.
     */
    final char space = ' ';

    /**
     * List of caracters acceptable as opening bracket. The closing bracket must
     * be the character in the {@code closingBrackets} array at the same index
     * than the opening bracket.
     */
    final char[] openingBrackets = {'[', '('};

    /**
     * List of caracters acceptable as closing bracket.
     */
    final char[] closingBrackets = {']', ')'};

    /**
     * The object to use for parsing and formatting numbers.
     *
     * <STRONG>Note:</STRONG> {@link NumberFormat} object are usually not thread safe.
     * Consequently, each instances of {@link Parser} or {@link Formatter} must use a
     * clone of this object, not this object directly (unless they synchronize on it).
     */
    final NumberFormat numberFormat;

    /**
     * Creates a new instance initialized to the default symbols.
     */
    private Symbols() {
        locale = Locale.US;
        numberFormat = DEFAULT.numberFormat;
    }

    /**
     * Creates a new set of symbols for the specified locale.
     */
    public Symbols(final Locale locale) {
        this.locale = locale;
        numberFormat = NumberFormat.getNumberInstance(locale);
        numberFormat.setGroupingUsed(false);
        numberFormat.setMinimumFractionDigits(1);
        numberFormat.setMaximumFractionDigits(20);
        /*
         * The "maximum fraction digits" seems hight for the precision of floating
         * points (even in double precision), but this is because the above format
         * do not uses the scientific notation. For example 1E-5 is always formatted
         * as 0.00001, and 1E-340 would actually need a maximum fraction digits of
         * 340. For most parameters, such low values should not occurs and may be
         * rounding error for the 0 value. For semi-major and semi-minor axis, we
         * often want to avoid exponential notation as well.
         */
        if (numberFormat instanceof DecimalFormat) {
            final char decimalSeparator = ((DecimalFormat) numberFormat)
                       .getDecimalFormatSymbols().getDecimalSeparator();
            if (decimalSeparator == ',') {
                separator = ';';
            }
        }
    }

    /**
     * Returns {@code true} if the specified WKT contains at least one {@code AXIS[...]} element.
     * This method tries to make a quick checks taking in account a minimal set of WKT syntax rules.
     *
     * @since 2.4
     */
    public boolean containsAxis(final CharSequence wkt) {
        return indexOf(wkt, "AXIS", 0) >= 0;
    }

    /**
     * Returns the index after the specified element in the specified WKT, or -1 if not found.
     * The element must be followed (ignoring spaces) by an opening bracket. If found, this
     * method returns the index of the opening bracket after the element.
     *
     * @param  wkt The WKT to parse.
     * @param  element The element to search. Must contains only uppercase letters.
     * @param  index The index to start the search from.
     */
    private int indexOf(final CharSequence wkt, final String element, int index) {
        assert element.equals(element.trim().toUpperCase(locale)) : element;
        assert element.indexOf(quote) < 0 : element;
        boolean isQuoting = false;
        final int elementLength = element.length();
        final int length = wkt.length();
        if (index < length) {
            char c = wkt.charAt(index);
search:     while (true) {
                // Do not parse any content between quotes.
                if (c == quote) {
                    isQuoting = !isQuoting;
                }
                if (isQuoting || !Character.isJavaIdentifierStart(c)) {
                    if (++index == length) {
                        break search;
                    }
                    c = wkt.charAt(index);
                    continue;
                }
                // Checks if we have a match.
                for (int j=0; j<elementLength; j++) {
                    c = Character.toUpperCase(c);
                    if (c != element.charAt(j)) {
                        // No match. Skip all remaining letters and resume the search.
                        while (Character.isJavaIdentifierPart(c)) {
                            if (++index == length) {
                                break search;
                            }
                            c = wkt.charAt(index);
                        }
                        continue search;
                    }
                    if (++index == length) {
                        break search;
                    }
                    c = wkt.charAt(index);
                }
                // Checks if the next character (ignoring space) is an opening brace.
                while (Character.isWhitespace(c)) {
                    if (++index == length) {
                        break search;
                    }
                    c = wkt.charAt(index);
                }
                for (int i=0; i<openingBrackets.length; i++) {
                    if (c == openingBrackets[i]) {
                        return index;
                    }
                }
            }
        }
        return -1;
    }
}
