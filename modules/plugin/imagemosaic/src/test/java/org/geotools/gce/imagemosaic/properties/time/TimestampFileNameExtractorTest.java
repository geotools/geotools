/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2016, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.gce.imagemosaic.properties.time;

import static org.junit.Assert.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.gce.imagemosaic.properties.PropertiesCollector;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorFinder;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorSPI;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class TimestampFileNameExtractorTest {

    private SimpleDateFormat df;
    private SimpleFeature feature;

    @Before
    public void setup() throws SchemaException {
        df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleFeatureType ft = DataUtilities.createType("test", "id:int,time:java.util.Date");
        feature = DataUtilities.createFeature(ft, "1|null");
    }

    @Test
    public void testParseIsoTimestamp() {
        PropertiesCollectorSPI spi = getTimestampSpi();
        PropertiesCollector collector =
                spi.create("regex=[0-9]{8}T[0-9]{6}", Arrays.asList("time"));
        File file = new File("polyphemus_20130301T000000.nc");
        collector.collect(file);
        collector.setProperties(feature);
        Date time = (Date) feature.getAttribute("time");
        assertNotNull(time);
        assertEquals("2013-03-01T00:00:00.000Z", df.format(time));
    }

    @Test
    public void testUnableToParse() {
        PropertiesCollectorSPI spi = getTimestampSpi();
        PropertiesCollector collector =
                spi.create("regex=[0-9]{8}T[0-9]{6}", Arrays.asList("time"));

        // Note that the number of 0 after the T char isn't enough
        // will throw an illegalArgumentException while parsing
        File file = new File("polyphemus_20130301T00000.nc");

        boolean parsed = true;
        try {
            collector.collect(file);
            collector.setProperties(feature);
        } catch (IllegalArgumentException ie) {
            parsed = false;
        }
        assertFalse(parsed);
    }

    @Test
    public void testParseCustomTimestamp() {
        PropertiesCollectorSPI spi = getTimestampSpi();
        PropertiesCollector collector =
                spi.create("regex=[0-9]{14},format=yyyyMMddHHmmss", Arrays.asList("time"));
        File file = new File("polyphemus_20130301000000.nc");
        collector.collect(file);
        collector.setProperties(feature);
        Date time = (Date) feature.getAttribute("time");
        assertNotNull(time);
        assertEquals("2013-03-01T00:00:00.000Z", df.format(time));
    }

    @Test
    public void testParseFullPathTimestamp() {
        PropertiesCollectorSPI spi = getTimestampSpi();
        String regex = "(?:\\\\)(\\d{8})(?:\\\\)(?:file.)(T\\d{6})(?:.txt)";
        PropertiesCollector collector =
                spi.create("regex=" + regex + ",fullPath=true", Arrays.asList("time"));
        File file = new File("c:\\data\\20120602\\file.T120000.txt");
        collector.collect(file);
        collector.setProperties(feature);
        Date time = (Date) feature.getAttribute("time");
        assertNotNull(time);
        assertEquals("2012-06-02T12:00:00.000Z", df.format(time));
    }

    @Test
    public void testParseFullPathTimestampWithCustomFormat() {
        PropertiesCollectorSPI spi = getTimestampSpi();
        String regex = "(?:\\\\)(\\d{8})(?:\\\\)(?:file.)(t\\d{2}z)(?:.txt)";
        PropertiesCollector collector =
                spi.create(
                        "regex=" + regex + ",format=yyyyMMdd't'HH'z',fullPath=true",
                        Arrays.asList("time"));
        File file = new File("c:\\data\\20120602\\file.t12z.txt");
        collector.collect(file);
        collector.setProperties(feature);
        Date time = (Date) feature.getAttribute("time");
        assertNotNull(time);
        assertEquals("2012-06-02T12:00:00.000Z", df.format(time));
    }

    private PropertiesCollectorSPI getTimestampSpi() {
        Set<PropertiesCollectorSPI> spis = PropertiesCollectorFinder.getPropertiesCollectorSPI();
        for (PropertiesCollectorSPI spi : spis) {
            if (spi.getName().equals(TimestampFileNameExtractorSPI.class.getSimpleName())) {
                return spi;
            }
        }

        return null;
    }
}
