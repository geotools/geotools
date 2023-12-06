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
package org.geotools.data.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Set;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.ConverterFactory;
import org.geotools.util.Converters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;

public class ReferencedEnvelopeConverterFactoryTest {

    ReferencedEnvelopeConverterFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new ReferencedEnvelopeConverterFactory();
    }

    @Test
    public void testReferencedEnvelopeToString() throws Exception {
        String text =
                factory.createConverter(ReferencedEnvelope.class, String.class, null)
                        .convert(
                                new ReferencedEnvelope(
                                        -180, 180, -90, 90, DefaultGeographicCRS.WGS84),
                                String.class);
        Assert.assertNotNull(text);
        Assert.assertEquals(
                "[-180.0 : 180.0, -90.0 : 90.0] {" + DefaultGeographicCRS.WGS84.toWKT() + "}",
                text);
    }

    @Test
    public void testStringToReferencedEnvelope() throws Exception {
        Envelope envelope =
                factory.createConverter(String.class, ReferencedEnvelope.class, null)
                        .convert(
                                "[-180.0 : 180.0, -90.0 : 90.0] {"
                                        + DefaultGeographicCRS.WGS84.toWKT()
                                        + "}",
                                ReferencedEnvelope.class);

        Assert.assertEquals(
                new ReferencedEnvelope(-180, 180, -90, 90, DefaultGeographicCRS.WGS84), envelope);
    }

    @Test
    public void testReferencedEnvelopeConverterFactory() {

        // make sure the class is registered and assigned
        Set<ConverterFactory> set =
                Converters.getConverterFactories(String.class, ReferencedEnvelope.class);
        assertNotNull(set);
        assertFalse(set.isEmpty());
        assertEquals(set.size(), 1);
        assertSame(set.iterator().next().getClass(), ReferencedEnvelopeConverterFactory.class);

        // make sure the class is registered also for the inverse process
        Set<ConverterFactory> set1 =
                Converters.getConverterFactories(ReferencedEnvelope.class, String.class);
        assertNotNull(set1);
        assertFalse(set1.isEmpty());
        assertEquals(set1.size(), 1);
        assertSame(set1.iterator().next().getClass(), ReferencedEnvelopeConverterFactory.class);

        //
        assertNull(new CRSConverterFactory().createConverter(null, null, null));
        assertNull(new CRSConverterFactory().createConverter(String.class, null, null));
        assertNull(new CRSConverterFactory().createConverter(String.class, Double.class, null));
    }
}
