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
package org.geotools.styling.css;

import java.util.List;
import org.parboiled.errors.DefaultInvalidInputErrorFormatter;
import org.parboiled.errors.InvalidInputError;
import org.parboiled.errors.ParseError;
import org.parboiled.support.Position;

/**
 * Exception thrown when a CSS parsing error occurs
 *
 * @author Andrea Aime - GeoSolutions
 */
public class CSSParseException extends IllegalArgumentException {
    private static final long serialVersionUID = -2624556764086947780L;

    private volatile List<ParseError> errors;

    public CSSParseException(List<ParseError> errors) {
        super(buildMessage(errors));
        this.errors = errors;
    }

    private static String buildMessage(List<ParseError> errors) {
        if (errors == null || errors.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot build a CSSParseException without a list of errors");
        }

        StringBuilder sb = new StringBuilder();
        for (ParseError pe : errors) {
            Position pos = pe.getInputBuffer().getPosition(pe.getStartIndex());
            String message =
                    pe.getErrorMessage() != null
                            ? pe.getErrorMessage()
                            : pe instanceof InvalidInputError
                                    ? new DefaultInvalidInputErrorFormatter()
                                            .format((InvalidInputError) pe)
                                    : pe.getClass().getSimpleName();
            sb.append(message)
                    .append(" (line ")
                    .append(pos.line)
                    .append(", column ")
                    .append(pos.column)
                    .append(")");
            sb.append('\n');
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    /** The parse errors */
    public List<ParseError> getErrors() {
        return errors;
    }
}
