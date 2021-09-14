/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import java.util.Date;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

/**
 * Return the current time as a Date. When evaluated directly, it returns a new value at each
 * invocation. When evaluated as part of a filter against a DataStore, the value might be one, and
 * fixed, during the data access. For example:
 *
 * <ul>
 *   <li>If the datastore uses {@link org.geotools.filter.visitor.SimplifyingFilterVisitor}, the
 *       function will be called once, and replaced with the returned value
 *   <li>If the datastore encodes the function down in a native query language, the value could also
 *       be the same for all processed features. For example, the PostgreSQL <a
 *       href="https://www.postgresql.org/docs/13/functions-datetime.html#FUNCTIONS-DATETIME-CURRENT">now</a>
 *       function always returns the time at the beginning of the current transaction, to give a
 *       consistent notion of current time across it.
 * </ul>
 */
public class NowFunction extends FunctionExpressionImpl {
    public static FunctionName NAME = new FunctionNameImpl("now", Date.class);

    public NowFunction() {
        super(NAME);
    }

    @Override
    public String toString() {
        return "NOW()";
    }

    @Override
    public Object evaluate(Object object) {
        return new Date();
    }
}
