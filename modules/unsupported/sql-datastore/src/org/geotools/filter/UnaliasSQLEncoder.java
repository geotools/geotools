/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
 */
package org.geotools.filter;

import java.io.Writer;
import java.util.Map;

import org.geotools.data.jdbc.FilterToSQL;
import org.opengis.filter.expression.PropertyName;

public class UnaliasSQLEncoder extends FilterToSQL {

    private Map aliases;

    public UnaliasSQLEncoder(Map aliases) {
        super();
        this.aliases = aliases;
    }

    public UnaliasSQLEncoder() {
        aliases = null;
    }

    public UnaliasSQLEncoder(Map aliases, Writer out) throws SQLEncoderException {
        super(out);
        this.aliases = aliases;
    }

    public void setAliases(Map aliases) {
        this.aliases = aliases;
    }

    /**
     * Writes the SQL for the attribute Expression.
     * 
     * @param expression
     *            the attribute to turn to SQL.
     * 
     * @throws RuntimeException
     *             for io exception with writer
     */
    public Object visit(PropertyName expression, Object extraData) throws RuntimeException {
        if (this.aliases == null) {
            super.visit(expression, extraData);
            return null;
        }
        String alias = expression.getPropertyName();
        String sqlExpression = (String) this.aliases.get(alias);
        if (sqlExpression == null) {
            throw new IllegalStateException("Unkown sql expression for attribute " + alias);
        }
        try {
            out.write(escapeName(sqlExpression));
        } catch (java.io.IOException ioe) {
            throw new RuntimeException("IO problems writing attribute exp", ioe);
        }
        return extraData;
    }

}
