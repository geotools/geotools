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

package org.geotools.dem.attributes;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.dem.DEMCatalogManager;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.CatalogManagerImpl;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

/**
 * Testing whether the CRS extractor actually sets the CRS on a DEM
 */
public class CRSExtractorTest {

    @Rule public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void testDifferentProjections() throws Exception {

        URL testDataURL = this.getClass().getResource("/diffprojections");
        File testDataFolder = new File(testDataURL.toURI());
        File testDirectory = testFolder.newFolder("diffprojectionstest");
        FileUtils.copyDirectory(testDataFolder, testDirectory);
        CatalogManagerImpl catalogManager = new DEMCatalogManager();
        Hints creationHints = new Hints();
        creationHints.put(Utils.ALLOW_HETEROGENEOUS_CRS, true);
        ImageMosaicReader imReader = new ImageMosaicReader(testDirectory, creationHints, catalogManager);
        assertNotNull(imReader);

        GranuleCatalog gc = imReader.getRasterManager("diffprojectionstest").getGranuleCatalog();
        assertNotNull(gc);

        SimpleFeatureType type = gc.getType(gc.getTypeNames()[0]);
        type.getAttributeDescriptors().forEach(
                attributeDescriptor -> System.out.println(attributeDescriptor.getLocalName()));

        Optional<AttributeDescriptor> crsFound = type.getAttributeDescriptors().stream()
                .filter(attributeDescriptor -> attributeDescriptor.getLocalName().equals("crs"))
                .findAny();

        assertTrue(crsFound.isPresent());

        Query q = new Query(gc.getTypeNames()[0]);
        SimpleFeatureIterator features = gc.getGranules(q).features();
        while (features.hasNext()) {
            SimpleFeature feature = features.next();
            String crs = (String) feature.getAttribute("crs");
            assertNotNull(crs);
            System.out.println(crs);
        }


        FileUtils.forceDelete(testDirectory);
    }
}