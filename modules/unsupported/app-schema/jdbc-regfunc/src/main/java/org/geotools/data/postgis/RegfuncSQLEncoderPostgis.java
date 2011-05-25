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

package org.geotools.data.postgis;

import java.io.IOException;
import java.util.Iterator;

import org.geotools.filter.Expression;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.FunctionExpression;
import org.geotools.filter.RegisteredFunction;
import org.geotools.filter.SQLEncoder;
import org.geotools.filter.SQLEncoderPostgis;

/**
 * SQL encoder with support for registered functions (on PostGIS).
 * 
 * <p>
 * 
 * This class adds {@link RegisteredFunction} to the filter capabilities for this encoder, and
 * implements the method required to encode a {@link FunctionExpression} as SQL.
 * 
 * <p>
 * 
 * TODO: everything here should be pulled up into {@link SQLEncoder}.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @version $Id$
 *
 * @source $URL$
 * @since 2.4
 */
public class RegfuncSQLEncoderPostgis extends SQLEncoderPostgis {

    /**
     * Empty constructor
     * 
     * <p>
     * 
     * TODO: rethink empty constructor, as BBOXes _need_ an SRID, must make client set it somehow.
     * Maybe detect when encode is called?
     */
    public RegfuncSQLEncoderPostgis() {
    }

    /**
     * @param looseBbox
     *                Whether the BBOX filter should be strict (using the exact geom), or loose
     *                (using the envelopes)
     * 
     */
    public RegfuncSQLEncoderPostgis(boolean looseBbox) {
        super(looseBbox);
    }

    /**
     * Constructor with srid.
     * 
     * @param srid
     *                spatial reference id to encode geometries with.
     */
    public RegfuncSQLEncoderPostgis(int srid) {
        super(srid);
    }

    /**
     * Extends super by adding support for registered functions to the filter capabilities.
     * 
     * @see org.geotools.filter.SQLEncoderPostgis#createFilterCapabilities()
     */
    protected FilterCapabilities createFilterCapabilities() {
        FilterCapabilities filterCapabilities = super.createFilterCapabilities();
        filterCapabilities.addType(RegisteredFunction.class);
        return filterCapabilities;
    }

    /**
     * Writes SQL for a function expression.
     * 
     * @see org.geotools.filter.SQLEncoder#visit(org.geotools.filter.FunctionExpression)
     */
    // @Override
    public void visit(FunctionExpression expression) {
        try {
            String name = expression.getName();
            /*
             * SECURITY: protect against SQL injection attacks. SQL identifiers are similar to Java,
             * and testing for Java form should give sufficient protection as quotes and whitespace
             * are excluded.
             */
            if (!name.matches("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*")) {
                throw new RuntimeException("Function expression name \"" + name
                        + "\" is not a valid SQL identifier");
            }
            out.write(escapeName(name) + "(");
            boolean isFirstParameter = true;
            for (Iterator parameterIterator = expression.getParameters().iterator(); parameterIterator
                    .hasNext();) {
                Expression parameter = (Expression) parameterIterator.next();
                if (isFirstParameter) {
                    isFirstParameter = false;
                } else {
                    out.write(", ");
                }
                parameter.accept(this);
            }
            out.write(")");
        } catch (IOException e) {
            throw new RuntimeException("IO problems writing function expression", e);
        }
    }

}
