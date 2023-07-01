/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import java.util.Calendar;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Assert;
import org.junit.Test;

public class DateFunctionTest {

    @Test
    public void testDateParse() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal("yyyy");
        Literal year = ff.literal("1975");

        Function f = ff.function("dateParse", new Expression[] {pattern, year});
        Calendar cal = f.evaluate(null, Calendar.class);
        // System.out.println(cal);
        Assert.assertEquals(1975, cal.get(Calendar.YEAR));
    }

    @Test
    public void testDateEncode() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal("yyyy");
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, 2000);
        Literal date = ff.literal(cal.getTime());

        Function f = ff.function("dateFormat", new Expression[] {pattern, date});
        String year = f.evaluate(null, String.class);
        Assert.assertEquals("2000", year);
    }
}
