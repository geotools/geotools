/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2018, Open Source Geospatial Foundation (OSGeo)
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import junit.framework.TestCase;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPeriod;
import org.geotools.temporal.object.DefaultPosition;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.BegunBy;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;

/**
 * Unit test for FilterCapabilities.
 *
 * @author Chris Holmes, TOPP
 */
public class CapabilitiesTest extends TestCase {
    public void testCapablities() {
        Capabilities capabilities = new Capabilities();
        capabilities.addType(Beyond.class); // add to SpatialCapabilities
        capabilities.addType(After.class); // add to TemporalCapabilities
        capabilities.addType(PropertyIsEqualTo.class); // add to ScalarCapabilities
        capabilities.addName("NullCheck"); // will enable PropertyIsNull use
        capabilities.addName("Mul"); // will enable hasSimpleArithmatic
        capabilities.addName("random"); // a function returning a random number
        capabilities.addName("Length", 1); // single argument function
        capabilities.addName("toDegrees", "radians"); // single argument function
        capabilities.addName("length", "expression");

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Filter filter = ff.between(ff.literal(0), ff.property("x"), ff.literal(2));
        assertFalse("supports", capabilities.supports(filter));

        filter = ff.equals(ff.property("x"), ff.literal(2));
        assertTrue("supports", capabilities.supports(filter));

        filter = ff.after(ff.property("x"), ff.literal("1970-01-01 00:00:00"));
        assertTrue("supports", capabilities.supports(filter));

        assertTrue("fullySupports", capabilities.fullySupports(filter));

        Capabilities capabilities2 = new Capabilities();

        capabilities2.addAll(capabilities);
        capabilities2.addType(And.class);

        assertTrue(capabilities2.getContents().getScalarCapabilities().hasLogicalOperators());
        assertFalse(capabilities.getContents().getScalarCapabilities().hasLogicalOperators());
    }

    public void testCapablities_PropertyIsLessThanOrEqualTo() {
        Capabilities capabilities = new Capabilities();
        capabilities.addAll(Capabilities.SIMPLE_COMPARISONS);

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Filter filter = ff.lessOrEqual(ff.property("x"), ff.literal(2));
        assertTrue("supports", capabilities.supports(filter));
    }

    public void testCapabilities_BegunBy() throws ParseException {
        Capabilities capabilities = new Capabilities();
        capabilities.addType(BegunBy.class);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        Date date2 = dateFormat.parse("1970-07-19T07:08:09.101Z");
        Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
        Period period = new DefaultPeriod(temporalInstant, temporalInstant2);
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        BegunBy filter = ff.begunBy(ff.literal(period), ff.property("dateAttr"));
        assertTrue("supports", capabilities.supports(filter));

        assertTrue("fullySupports", capabilities.fullySupports(filter));
    }
}
