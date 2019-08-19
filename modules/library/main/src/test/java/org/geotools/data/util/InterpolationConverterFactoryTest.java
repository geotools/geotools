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
package org.geotools.data.util;

import java.util.Set;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationBicubic;
import javax.media.jai.InterpolationBicubic2;
import javax.media.jai.InterpolationBilinear;
import javax.media.jai.InterpolationNearest;
import org.geotools.util.ConverterFactory;
import org.geotools.util.Converters;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link InterpolationConverterFactory} machinery.
 *
 * @author Simone Giannecchini, GeoSolutions
 */
public class InterpolationConverterFactoryTest extends Assert {

    /** INTERPOLATION_CLASS */
    private static final Class<Interpolation> INTERPOLATION_CLASS = Interpolation.class;

    @Test
    public void testInterpolationConverterFactory() {

        // make sure the class is registered and assigned
        Set<ConverterFactory> set =
                Converters.getConverterFactories(String.class, INTERPOLATION_CLASS);
        assertNotNull(set);
        assertFalse(set.isEmpty());
        assertEquals(set.size(), 1);
        assertSame(set.iterator().next().getClass(), InterpolationConverterFactory.class);

        //
        assertNull(new InterpolationConverterFactory().createConverter(null, null, null));
        assertNull(new InterpolationConverterFactory().createConverter(String.class, null, null));
        assertNull(
                new InterpolationConverterFactory()
                        .createConverter(String.class, Double.class, null));
    }

    @Test
    public void testInterpolationNearest() {

        // make sure the class is registered and assigned
        Interpolation result = Converters.convert("InterpolationNearest", INTERPOLATION_CLASS);
        assertNotNull(result);
        assertSame(result.getClass(), InterpolationNearest.class);

        // test case sensiteveness
        assertNull(Converters.convert("Interpolationnearest", INTERPOLATION_CLASS));
        assertNull(Converters.convert("interpolationnearest", INTERPOLATION_CLASS));
        assertNull(Converters.convert("interpolatioNnearest", INTERPOLATION_CLASS));
    }

    @Test
    public void testInterpolationBilinear() {

        // make sure the class is registered and assigned
        Interpolation result = Converters.convert("InterpolationBilinear(2)", INTERPOLATION_CLASS);
        assertNotNull(result);
        assertSame(result.getClass(), InterpolationBilinear.class);
        assertSame(result.getSubsampleBitsH(), 2);
        assertSame(result.getSubsampleBitsV(), 2);

        result = Converters.convert("InterpolationBilinear(4)", INTERPOLATION_CLASS);
        assertNotNull(result);
        assertSame(result.getClass(), InterpolationBilinear.class);
        assertSame(result.getSubsampleBitsH(), 4);
        assertSame(result.getSubsampleBitsV(), 4);

        result = Converters.convert("InterpolationBilinear", INTERPOLATION_CLASS);
        assertNotNull(result);
        assertSame(result.getClass(), InterpolationBilinear.class);
        assertSame(result.getSubsampleBitsH(), 8);
        assertSame(result.getSubsampleBitsV(), 8);

        // test case sensiteveness
        assertNull(Converters.convert("interpolationBilinear", INTERPOLATION_CLASS));
        assertNull(Converters.convert("Interpolationbilinear", INTERPOLATION_CLASS));
        assertNull(Converters.convert("interpolationbilinear", INTERPOLATION_CLASS));
    }

    @Test
    public void testInterpolationBicubic() {

        // make sure the class is registered and assigned
        Interpolation result = Converters.convert("InterpolationBicubic(2)", INTERPOLATION_CLASS);
        assertNotNull(result);
        assertSame(result.getClass(), InterpolationBicubic.class);
        assertSame(result.getSubsampleBitsH(), 2);
        assertSame(result.getSubsampleBitsV(), 2);

        result = Converters.convert("InterpolationBicubic(4)", INTERPOLATION_CLASS);
        assertNotNull(result);
        assertSame(result.getClass(), InterpolationBicubic.class);
        assertSame(result.getSubsampleBitsH(), 4);
        assertSame(result.getSubsampleBitsV(), 4);

        result = Converters.convert("InterpolationBicubic", INTERPOLATION_CLASS);
        assertNull(result);

        // test case sensiteveness
        assertNull(Converters.convert("interpolationBicubic(2)", INTERPOLATION_CLASS));
        assertNull(Converters.convert("Interpolationbicubic(2)", INTERPOLATION_CLASS));
        assertNull(Converters.convert("interpolationbicubic(2)", INTERPOLATION_CLASS));
    }

    @Test
    public void testInterpolationBicubic2() {

        // make sure the class is registered and assigned
        Interpolation result = Converters.convert("InterpolationBicubic2(2)", INTERPOLATION_CLASS);
        assertNotNull(result);
        assertSame(result.getClass(), InterpolationBicubic2.class);
        assertSame(result.getSubsampleBitsH(), 2);
        assertSame(result.getSubsampleBitsV(), 2);

        result = Converters.convert("InterpolationBicubic2(4)", INTERPOLATION_CLASS);
        assertNotNull(result);
        assertSame(result.getClass(), InterpolationBicubic2.class);
        assertSame(result.getSubsampleBitsH(), 2);
        assertSame(result.getSubsampleBitsV(), 2);

        // missing subsamplebits
        result = Converters.convert("InterpolationBicubic2", INTERPOLATION_CLASS);
        assertNull(result);

        // test case sensiteveness
        assertNull(Converters.convert("interpolationBicubic2(2)", INTERPOLATION_CLASS));
        assertNull(Converters.convert("Interpolationbicubic2(2)", INTERPOLATION_CLASS));
        assertNull(Converters.convert("interpolationbicubic2(2)", INTERPOLATION_CLASS));
    }
}
