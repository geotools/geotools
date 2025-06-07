/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.properties.numeric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.util.Arrays;
import java.util.Set;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.gce.imagemosaic.properties.PropertiesCollector;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorFinder;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorSPI;
import org.junit.Before;
import org.junit.Test;

public class NumericFileNameExtractorTest {

    private SimpleFeature feature;

    @Before
    public void setup() throws SchemaException {
        SimpleFeatureType ft = DataUtilities.createType("test", "id:int,value:Double");
        feature = DataUtilities.createFeature(ft, "1|null");
    }

    @Test
    public void testNoGroupExtraction() {
        PropertiesCollectorSPI spi = getDoubleFileNameSpi();
        PropertiesCollector collector = spi.create("regex=[0-9]+\\.[0-9]", Arrays.asList("value"));
        File file = new File("polyphemus_20.3_.nc");
        collector.collect(file);
        collector.setProperties(feature);
        Double value = (Double) feature.getAttribute("value");
        assertNotNull(value);
        assertEquals(20.3, value, 0d);
    }

    @Test
    public void testFailedExtraction() {
        PropertiesCollectorSPI spi = getDoubleFileNameSpi();
        PropertiesCollector collector = spi.create("regex=[0-9]+\\.[0-9]", Arrays.asList("value"));
        File file = new File("polyphemus_20130301_.nc");
        collector.collect(file);
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> collector.setProperties(feature));
        assertEquals(
                "No matches found for: NumericFileNameExtractor{targetClass=class java.lang.Double, fullPath=false, pattern=[0-9]+\\.[0-9]}",
                ex.getMessage());
    }

    private PropertiesCollectorSPI getDoubleFileNameSpi() {
        Set<PropertiesCollectorSPI> spis = PropertiesCollectorFinder.getPropertiesCollectorSPI();
        for (PropertiesCollectorSPI spi : spis) {
            if (spi.getName().equals(DoubleFileNameExtractorSPI.class.getSimpleName())) {
                return spi;
            }
        }

        return null;
    }
}
