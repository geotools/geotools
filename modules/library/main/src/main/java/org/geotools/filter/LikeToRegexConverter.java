/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;
import org.opengis.filter.PropertyIsLike;

/**
 * Helper class that takes a LikeFilter and generates the equivalent Java Pattern syntax
 *
 * @author Andrea Aime - GeoSolutions
 * @since 13.x
 */
public class LikeToRegexConverter {

    static final Logger LOGGER = Logging.getLogger(String.class);

    String pattern;

    public LikeToRegexConverter(PropertyIsLike like) {
        String pattern = like.getLiteral();
        String wildcardMulti = like.getWildCard();
        String wildcardSingle = like.getSingleChar();
        String escape = like.getEscape();

        // The following things happen for both wildcards:
        // (1) If a user-defined wildcard exists, replace with Java wildcard
        // (2) If a user-defined escape exists, Java wildcard + user-escape
        // Then, test for matching pattern and return result.
        char esc = escape.isEmpty() ? Character.MIN_VALUE : escape.charAt(0);
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer("wildcard " + wildcardMulti + " single " + wildcardSingle);
            LOGGER.finer("escape " + escape + " esc " + esc + " esc == \\ " + (esc == '\\'));
        }

        String escapedWildcardMulti = fixSpecials(wildcardMulti, escape);
        String escapedWildcardSingle = fixSpecials(wildcardSingle, escape);

        // escape any special chars which are not our wildcards
        StringBuffer tmp = new StringBuffer("");

        boolean escapedMode = false;

        for (int i = 0; i < pattern.length(); i++) {
            char chr = pattern.charAt(i);
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("tmp = " + tmp + " looking at " + chr);
            }

            if (!escape.isEmpty() && pattern.regionMatches(false, i, escape, 0, escape.length())) {
                // skip the escape string
                LOGGER.finer("escape ");
                escapedMode = true;

                i += escape.length();
                chr = pattern.charAt(i);
            }

            if (pattern.regionMatches(
                    false, i, wildcardMulti, 0, wildcardMulti.length())) { // replace
                // with
                // java
                // wildcard
                LOGGER.finer("multi wildcard");

                if (escapedMode) {
                    LOGGER.finer("escaped ");
                    tmp.append(escapedWildcardMulti);
                } else {
                    tmp.append(".*");
                }

                i += (wildcardMulti.length() - 1);
                escapedMode = false;

                continue;
            }

            if (pattern.regionMatches(false, i, wildcardSingle, 0, wildcardSingle.length())) {
                // replace with java single wild card
                LOGGER.finer("single wildcard");

                if (escapedMode) {
                    LOGGER.finer("escaped ");
                    tmp.append(escapedWildcardSingle);
                } else {
                    // From the OpenGIS filter encoding spec,
                    // "the single singleChar character matches exactly one character"
                    tmp.append(".{1}");
                }

                i += (wildcardSingle.length() - 1);
                escapedMode = false;

                continue;
            }

            if (isSpecial(chr) && !escape.isEmpty()) {
                LOGGER.finer("special");
                tmp.append(escape + chr);
                escapedMode = false;

                continue;
            }

            tmp.append(chr);
            escapedMode = false;
        }

        pattern = tmp.toString();
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer("final pattern " + pattern);
        }

        this.pattern = pattern;
    }

    /**
     * Convenience method to determine if a character is special to the regex system.
     *
     * @param chr the character to test
     * @return is the character a special character.
     */
    private boolean isSpecial(final char chr) {
        return ((chr == '.')
                || (chr == '?')
                || (chr == '*')
                || (chr == '^')
                || (chr == '$')
                || (chr == '+')
                || (chr == '[')
                || (chr == ']')
                || (chr == '(')
                || (chr == ')')
                || (chr == '|')
                || (chr == '\\')
                || (chr == '&'));
    }

    /**
     * Convenience method to escape any character that is special to the regex system.
     *
     * @param inString the string to fix
     * @param escape the escape charatter
     * @return the fixed string
     */
    private String fixSpecials(final String inString, final String escape) {
        StringBuffer tmp = new StringBuffer("");

        for (int i = 0; i < inString.length(); i++) {
            char chr = inString.charAt(i);

            if (isSpecial(chr)) {
                tmp.append(escape + chr);
            } else {
                tmp.append(chr);
            }
        }

        return tmp.toString();
    }

    /** Returns the equivalent Java pattern */
    public String getPattern() {
        return pattern;
    }
}
