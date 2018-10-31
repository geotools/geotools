/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import static org.geotools.util.URLs.fileToUrl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.geotools.TestData;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.HarvestedSource;
import org.geotools.data.Query;
import org.geotools.gce.imagemosaic.properties.PropertiesCollector;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorFinder;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorSPI;
import org.geotools.gce.imagemosaic.properties.string.StringFileNameExtractorSPI;
import org.geotools.gce.imagemosaic.properties.time.TimestampFileNameExtractorSPI;
import org.geotools.util.URLs;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;

/** @author Simone Giannecchini, GeoSolutions SAS */
public class PropertiesCollectorTest {

    @BeforeClass
    public static void init() {
        System.setProperty("org.geotools.referencing.forceXY", "true");
        System.setProperty("user.timezone", "GMT");
        System.setProperty("org.geotools.shapefile.datetime", "true");
    }

    @AfterClass
    public static void cleanup() {
        System.clearProperty("org.geotools.referencing.forceXY");
        System.clearProperty("user.timezone");
        System.clearProperty("org.geotools.shapefile.datetime");
    }

    @Test
    public void test() {

        // get the spi
        final Set<PropertiesCollectorSPI> spis =
                PropertiesCollectorFinder.getPropertiesCollectorSPI();
        assertNotNull(spis);
        assertTrue(!spis.isEmpty());
        assertEquals(15, spis.size());
    }

    @Test
    public void testString() throws IOException {

        // get the spi
        final Set<PropertiesCollectorSPI> spis =
                PropertiesCollectorFinder.getPropertiesCollectorSPI();
        assertNotNull(spis);
        assertTrue(!spis.isEmpty());
        URL testUrl = TestData.url(this, "time_geotiff/stringregex.properties");
        // test a regex
        PropertiesCollectorSPI spi;
        final Iterator<PropertiesCollectorSPI> iterator = spis.iterator();
        while (iterator.hasNext()) {
            spi = iterator.next();
            if (spi instanceof StringFileNameExtractorSPI) {
                final PropertiesCollector pc = spi.create(testUrl, Arrays.asList("string_attr"));
                pc.collect(TestData.file(this, "time_geotiff/world.200402.3x5400x2700.tiff"));
                return;
            }
        }

        assertTrue(false);
    }

    @Test
    public void testTime() throws IOException {

        // get the spi
        final Set<PropertiesCollectorSPI> spis =
                PropertiesCollectorFinder.getPropertiesCollectorSPI();
        assertNotNull(spis);
        assertTrue(!spis.isEmpty());
        URL testUrl = TestData.url(this, "time_geotiff/timeregex.properties");
        // test a regex
        PropertiesCollectorSPI spi;
        final Iterator<PropertiesCollectorSPI> iterator = spis.iterator();
        while (iterator.hasNext()) {
            spi = iterator.next();
            if (spi instanceof TimestampFileNameExtractorSPI) {
                final PropertiesCollector pc = spi.create(testUrl, Arrays.asList("time_attr"));
                pc.collect(TestData.file(this, "time_geotiff/world.200403.3x5400x2700.tiff"));
                return;
            }
        }
        assertTrue(false);
    }

    @Test
    public void testHarvestCurrentTime() throws Exception {
        File source = URLs.urlToFile(org.geotools.test.TestData.url(this, "rgba/"));
        File config = URLs.urlToFile(org.geotools.test.TestData.url(this, "currentdate/"));
        File testDataDir = org.geotools.test.TestData.file(this, ".");
        File directory1 = new File(testDataDir, "currTimeHarvest1");
        File directory2 = new File(testDataDir, "currTimeHarvest2");
        if (directory1.exists()) {
            FileUtils.deleteDirectory(directory1);
        }
        FileUtils.copyDirectory(source, directory1);
        FileUtils.copyDirectory(config, directory1);
        if (directory2.exists()) {
            FileUtils.deleteDirectory(directory2);
        }
        FileUtils.copyDirectory(source, directory2);
        // remove all files but one from the source dir
        for (File file :
                directory1.listFiles(
                        f ->
                                !f.getName().startsWith("passA2006128193711.")
                                        && !f.getName().endsWith(".properties"))) {
            assertTrue(file.delete());
        }

        // ok, let's create a mosaic with a single granule and check its times
        URL harvestSingleURL = fileToUrl(directory1);
        final AbstractGridFormat format = TestUtils.getFormat(harvestSingleURL);
        ImageMosaicReader reader = TestUtils.getReader(harvestSingleURL, format);
        try {
            // wait a bit
            Thread.sleep(100);

            // harvest a file
            File harvestSourceOne = new File(directory2, "passA2006128194218.png");
            List<HarvestedSource> summary = reader.harvest(null, harvestSourceOne, null);
            assertEquals(1, summary.size());
            HarvestedSource hf = summary.get(0);
            assertTrue(hf.success());

            // wait a bit again

            Thread.sleep(100);

            // harvest another
            File harvestSourceTwo = new File(directory2, "passA2006128211927.png");
            summary = reader.harvest(null, harvestSourceTwo, null);
            assertEquals(1, summary.size());
            hf = summary.get(0);
            assertTrue(hf.success());

            // now collect the granules and make sure the times are in sequential order (not all
            // same)
            Map<String, Date> mappedDates = new HashMap<>();
            reader.getGranules(null, true)
                    .getGranules(Query.ALL)
                    .accepts(
                            new FeatureVisitor() {
                                @Override
                                public void visit(Feature feature) {
                                    String location =
                                            (String) feature.getProperty("location").getValue();
                                    int idxSlash = location.lastIndexOf('\\');
                                    int idxBackslash = location.lastIndexOf('/');
                                    String name = location;
                                    if (idxSlash > -1 || idxBackslash > -1) {
                                        name =
                                                location.substring(
                                                        Math.max(idxSlash, idxBackslash) + 1);
                                    }
                                    Date ingest =
                                            (Date) feature.getProperty("ingestion").getValue();
                                    mappedDates.put(name, ingest);
                                }
                            },
                            null);

            // check they are not all the same (the collector was re-created every time)
            assertThat(
                    mappedDates.get("passA2006128193711.png"),
                    Matchers.lessThan(mappedDates.get("passA2006128194218.png")));
            assertThat(
                    mappedDates.get("passA2006128194218.png"),
                    Matchers.lessThan(mappedDates.get("passA2006128211927.png")));

            // check they are reasonable (say within an hour from the current time, if not the
            // test execution went really wrong)
            long now = System.currentTimeMillis();
            long oneHour = 1000 * 60 * 60;
            for (Date d : mappedDates.values()) {
                assertThat(
                        d + " vs " + new Date(),
                        now - d.getTime(),
                        Matchers.lessThanOrEqualTo(oneHour));
            }
        } finally {
            reader.dispose();
        }
    }
}
