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
package org.geotools.coverage.io.netcdf;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gce.imagemosaic.properties.PropertiesCollector;
import org.geotools.imageio.netcdf.utilities.RuntimeExtractorSPI;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/** @author Daniele Romagnoli, GeoSolutions SAS */
public class RuntimeExtractorTest extends Assert {

    @Test
    public void runtimeTest() throws Exception {

        // acquire dataset
        final File file = TestData.file(this, "O3-NO2.nc");
        long lastModified = file.lastModified();

        final RuntimeExtractorSPI spi = new RuntimeExtractorSPI();
        final PropertiesCollector collector =
                spi.create("regex=MODIFY_TIME", Arrays.asList("updated"));
        final SimpleFeatureTypeBuilder featureTypeBuilder = new SimpleFeatureTypeBuilder();
        featureTypeBuilder.setName("runtimeT");
        featureTypeBuilder.add("updated", Date.class);
        SimpleFeatureType featureType = featureTypeBuilder.buildFeatureType();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
        SimpleFeature feature = featureBuilder.buildFeature("0");
        collector.collect(file);
        collector.setProperties(feature);
        Date date = (Date) feature.getAttribute("updated");
        assertEquals(lastModified, date.getTime());
    }
}
