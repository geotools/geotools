package org.geotools.data.util;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import org.geotools.referencing.CRS;
import org.geotools.util.ConverterFactory;
import org.geotools.util.Converters;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Test class for the {@link CRSConverterFactory} class.
 *
 * @author Nicola Lagomarsini GeoSolutions S.A.S.
 */
public class CRSConverterFactoryTest {

    /** CRS class */
    private static final Class<CoordinateReferenceSystem> CRS_CLASS =
            CoordinateReferenceSystem.class;

    /** String class */
    private static final Class<String> STRING_CLASS = String.class;

    @Test
    public void testCRSConverterFactory() {

        // make sure the class is registered and assigned
        Set<ConverterFactory> set = Converters.getConverterFactories(STRING_CLASS, CRS_CLASS);
        assertNotNull(set);
        assertFalse(set.isEmpty());
        assertEquals(set.size(), 1);
        assertSame(set.iterator().next().getClass(), CRSConverterFactory.class);

        // make sure the class is registered also for the inverse process
        Set<ConverterFactory> set1 = Converters.getConverterFactories(CRS_CLASS, STRING_CLASS);
        assertNotNull(set1);
        assertFalse(set1.isEmpty());
        assertEquals(set1.size(), 1);
        assertSame(set1.iterator().next().getClass(), CRSConverterFactory.class);

        //
        assertNull(new CRSConverterFactory().createConverter(null, null, null));
        assertNull(new CRSConverterFactory().createConverter(String.class, null, null));
        assertNull(new CRSConverterFactory().createConverter(String.class, Double.class, null));
    }

    @Test
    public void testStringToCRS() {

        // Test by decoding a CRS id
        CoordinateReferenceSystem result = Converters.convert("EPSG:4326", CRS_CLASS);
        assertNotNull(result);
        assertTrue(CRS_CLASS.isAssignableFrom(result.getClass()));

        // Test by parsing a WKT
        String wkt =
                "GEOGCS[\"WGS 84\", "
                        + "DATUM[\"WGS_1984\", "
                        + "SPHEROID[\"WGS 84\",6378137,298.257223563, "
                        + "AUTHORITY[\"EPSG\",\"7030\"]], "
                        + "AUTHORITY[\"EPSG\",\"6326\"]], "
                        + "PRIMEM[\"Greenwich\",0, "
                        + "AUTHORITY[\"EPSG\",\"8901\"]], "
                        + "UNIT[\"degree\",0.01745329251994328, "
                        + "AUTHORITY[\"EPSG\",\"9122\"]], "
                        + "AUTHORITY[\"EPSG\",\"4326\"]]";

        CoordinateReferenceSystem result1 = Converters.convert(wkt, CRS_CLASS);
        assertNotNull(result1);
        assertTrue(CRS_CLASS.isAssignableFrom(result1.getClass()));

        // Check if nothing is converted if no source or a wrong source is present
        assertNull(Converters.convert(null, null));
        assertNull(Converters.convert(wkt.replace("UNIT", "UNITY"), CRS_CLASS));
    }

    @Test
    public void testCRSToString() throws NoSuchAuthorityCodeException, FactoryException {

        // Simple CRS creation
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");

        // make sure the class is registered and assigned
        String result = Converters.convert(crs, STRING_CLASS);
        assertNotNull(result);
        assertSame(result.getClass(), STRING_CLASS);
    }
}
