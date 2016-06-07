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

package org.geotools.dem;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.CatalogManagerImpl;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.gce.imagemosaic.MosaicConfigurationBean;
import org.geotools.gce.imagemosaic.Utils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Test whether all the found CRSs are captured when generating the mosaic properties
 */
public class FoundCRSsTest {

    @Rule public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void testFoundCRSGeneration() throws IOException, URISyntaxException {
        System.setProperty("org.geotools.referencing.forceXY", "true");
        URL testDataURL = this.getClass().getResource("/diffprojections");
        File testDataFolder = new File(testDataURL.toURI());
        File testDirectory = testFolder.newFolder("diffprojectionstest");
        FileUtils.copyDirectory(testDataFolder, testDirectory);
        CatalogManagerImpl catalogManager = new CatalogManagerImpl();
        Hints creationHints = new Hints();
        creationHints.put(Utils.ALLOW_HETEROGENEOUS_CRS, true);
        ImageMosaicReader imReader = new ImageMosaicReader(testDirectory, creationHints, catalogManager);
        assertNotNull(imReader);

        MosaicConfigurationBean mosaicConfig = imReader
                .getRasterManager("diffprojectionstest").getConfiguration();

        List<CoordinateReferenceSystem> foundCRSs = mosaicConfig.getFoundCRSs();
        assertEquals(2, foundCRSs.size());

        FileUtils.forceDelete(testDirectory);
    }
}
