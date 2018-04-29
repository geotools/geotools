/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A helper class for escaping object name patterns in calls to {@link java.sql.DatabaseMetaData}.
 *
 * @see org.geotools.jdbc.JDBCDataStore#escapeNamePattern(java.sql.DatabaseMetaData, String)
 */
class NamePatternEscaping {
    private final String escape;
    private final Pattern replacementPattern;
    private final String replacement;

    public NamePatternEscaping(String escape) {
        this.escape = escape == null ? "" : escape;
        String quotedEscape = Pattern.quote(this.escape);
        replacementPattern = Pattern.compile("(" + quotedEscape + "|[_%])");
        replacement = Matcher.quoteReplacement(this.escape) + "$1";
    }

    public String escape(String name) {
        if (needsEscaping(name)) {
            return replacementPattern.matcher(name).replaceAll(replacement);
        } else {
            return name;
        }
    }

    private boolean needsEscaping(String name) {
        if (name == null) {
            return false;
        }
        if (escape.isEmpty()) {
            return false;
        }
        return name.indexOf('_') != -1 || name.indexOf('%') != -1 || name.contains(escape);
    }
}
