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

package org.geotools.gce.imagemosaic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.geotools.factory.Hints;
import org.geotools.test.TestData;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Testing that granule collectors correctly get configured and initialized
 */
public class GranuleCollectorSPITest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Rule
    public TemporaryFolder crsMosaicFolder = new TemporaryFolder();

    @Test
    public void testCustomizedGranuleAcceptor() throws IOException, URISyntaxException {
        URL testDataURL = TestData.url(this, "diffprojections");
        File testDataFolder = new File(testDataURL.toURI());
        File testDirectory = testFolder.newFolder("diffprojectionstest");
        FileUtils.copyDirectory(testDataFolder, testDirectory);
        Hints creationHints = new Hints();
        ImageMosaicReader imReader = new ImageMosaicReader(testDirectory, creationHints);
        assertNotNull(imReader);
        assertEquals(imReader.getGranules(
                imReader.getGridCoverageNames()[0], true).getCount(null),
                2);
    }

}
