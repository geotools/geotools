/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.properties.string;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.Arrays;
import java.util.Set;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.gce.imagemosaic.properties.PropertiesCollector;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorFinder;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorSPI;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class StringFileNameExtractorTest {

    private SimpleFeature feature;

    @Before
    public void setup() throws SchemaException {
        SimpleFeatureType ft = DataUtilities.createType("test", "id:int,seq:String");
        feature = DataUtilities.createFeature(ft, "1|null");
    }

    @Test
    public void testNoGroupExtraction() {
        PropertiesCollectorSPI spi = getStringFileNameSpi();
        PropertiesCollector collector = spi.create("regex=[0-9]{8}T[0-9]{6}", Arrays.asList("seq"));
        File file = new File("polyphemus_20130301T000000_.nc");
        collector.collect(file);
        collector.setProperties(feature);
        String seq = (String) feature.getAttribute("seq");
        assertNotNull(seq);
        assertEquals("20130301T000000", seq);
    }

    @Test
    public void testGroupExtraction() {
        PropertiesCollectorSPI spi = getStringFileNameSpi();
        PropertiesCollector collector =
                spi.create("regex=_([0-9]{8}T[0-9]{6})_", Arrays.asList("seq"));
        File file = new File("polyphemus_20130301T000000_.nc");
        collector.collect(file);
        collector.setProperties(feature);
        String seq = (String) feature.getAttribute("seq");
        assertNotNull(seq);
        assertEquals("20130301T000000", seq);
    }

    @Test
    public void testMultipleGroupExtraction() {
        PropertiesCollectorSPI spi = getStringFileNameSpi();
        PropertiesCollector collector =
                spi.create("regex=_([0-9]{8})T([0-9]{6})_", Arrays.asList("seq"));
        File file = new File("polyphemus_20130301T000000_.nc");
        collector.collect(file);
        collector.setProperties(feature);
        String seq = (String) feature.getAttribute("seq");
        assertNotNull(seq);
        assertEquals("20130301000000", seq);
    }

    private PropertiesCollectorSPI getStringFileNameSpi() {
        Set<PropertiesCollectorSPI> spis = PropertiesCollectorFinder.getPropertiesCollectorSPI();
        for (PropertiesCollectorSPI spi : spis) {
            if (spi.getName().equals(StringFileNameExtractorSPI.class.getSimpleName())) {
                return spi;
            }
        }

        return null;
    }
}
