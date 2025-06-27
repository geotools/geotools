/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage;

import static org.junit.Assert.assertEquals;

import org.geotools.api.util.InternationalString;
import org.geotools.util.SimpleInternationalString;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link GridSampleDimension} implementation. Since {@code GridSampleDimension} rely on {@link CategoryList}
 * for many of its work, many {@code GridSampleDimension} tests are actually {@code CategoryList} tests.
 *
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class SampleDimensionTest {

    static class WrappedGridSampleDimension extends GridSampleDimension {

        protected WrappedGridSampleDimension(GridSampleDimension other) {
            super(other);
        }

        @Override
        public InternationalString getDescription() {
            return new SimpleInternationalString("overridden");
        }
    }
    /** The categories making the sample dimension to test. */
    private static final String[] CATEGORIES = {"No data", "Clouds", "Lands"};

    /**
     * The "no data" values making the sample dimension to test. There is one for each category in {@link #CATEGORIES}.
     */
    private static final int[] NO_DATA = {0, 1, 255};

    /** The minimal value for the geophysics category, inclusive. */
    private static final int minimum = 10;

    /** The maximal value for the geophysics category, exclusive. */
    private static final int maximum = 200;

    /** The scale factor for the sample dimension to test. */
    private static final double scale = 0.1;

    /** The offset value for the sample dimension to test. */
    private static final double offset = 5.0;

    /** The sample dimension to test. */
    private GridSampleDimension test;

    /** Sets up common objects used for all tests. */
    @Before
    public void setUp() {
        assertEquals("setUp", CATEGORIES.length, NO_DATA.length);
        final Category[] categories = new Category[CATEGORIES.length + 1];
        for (int i = 0; i < CATEGORIES.length; i++) {
            categories[i] = new Category(CATEGORIES[i], null, NO_DATA[i]);
        }
        categories[CATEGORIES.length] = new Category("SST", null, minimum, maximum);
        test = new GridSampleDimension("Temperature", categories, scale, offset);
    }

    /** Tests the consistency of the sample dimension. */
    @Test
    public void testSampleDimension() {
        final double[] nodataValues = test.getNoDataValues();
        assertEquals("nodataValues.length", CATEGORIES.length, nodataValues.length);
        for (int i = 0; i < CATEGORIES.length; i++) {
            assertEquals("nodataValues[" + i + ']', NO_DATA[i], nodataValues[i], 0);
        }
        assertEquals("scale", scale, test.getScale(), 0);
        assertEquals("offset", offset, test.getOffset(), 0);
        assertEquals("minimum", 0, test.getMinimumValue(), 0);
        assertEquals("maximum", 255, test.getMaximumValue(), 0);
    }

    /** Tests the {@link GridSampleDimension}'s cloning. */
    @Test
    public void testCloningSampleDimension() {
        GridSampleDimension original = test;
        assertEquals("Temperature", original.getDescription().toString());

        GridSampleDimension wrapped = new WrappedGridSampleDimension(original);
        assertEquals("overridden", wrapped.getDescription().toString());
    }
}
