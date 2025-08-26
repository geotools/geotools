/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import it.geosolutions.imageio.utilities.ImageIOUtilities;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import org.apache.commons.io.FileUtils;
import org.geotools.api.data.Query;
import org.geotools.test.TestData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ImageMosaicSkipOverviewsTest {

    private boolean prev;

    @Before
    public void setSkipTrue() throws Exception {
        Field f = ImageIOUtilities.class.getDeclaredField("SKIP_EXTERNAL_FILES_LOOKUP");
        f.setAccessible(true);
        prev = f.getBoolean(null);
        f.setBoolean(null, true);
    }

    @After
    public void restoreSkip() throws Exception {
        Field f = ImageIOUtilities.class.getDeclaredField("SKIP_EXTERNAL_FILES_LOOKUP");
        f.setAccessible(true);
        f.setBoolean(null, prev);
    }

    @Test
    public void testWithSkipTrue() throws Exception {
        testSkipOverviews(null, true);
    }

    @Test
    public void testWithSkipFalse() throws Exception {
        testSkipOverviews(false, false);
    }

    private void testSkipOverviews(Boolean mosaicFlag, boolean expected) throws IOException {
        // copy over the RGB mosaic
        String workDirName = "skipOverviewsSys";
        File workDir = new File(TestData.file(this, "."), workDirName);
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            if (!workDir.mkdir()) {
                fail("Unable to create workdir:" + workDir);
            }
        }
        final File source = TestData.file(this, "rgba");
        FileUtils.copyDirectory(source, workDir);
        // clean up config files created by other tests
        for (File file : workDir.listFiles(f -> f.getName().startsWith("rgba."))) {
            file.delete();
        }

        // add an indexer telling the mosaic to skip overviews
        if (mosaicFlag != null) {
            try (FileWriter out = new FileWriter(new File(workDir, "indexer.properties"), true)) {
                out.write(Utils.Prop.SKIP_EXTERNAL_OVERVIEWS + "=" + mosaicFlag);
                out.flush();
            }
        }
        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(workDir);
        try {
            // check the catalog is actually skipping overviews
            RasterManager manager = reader.rasterManagers.get(workDirName);
            manager.granuleCatalog.getGranuleDescriptors(
                    new Query(workDirName),
                    (granule, feature) -> assertEquals(
                            expected, granule.getMaskOverviewProvider().isSkipExternalLookup()));
        } finally {
            if (reader != null) reader.dispose();
        }
    }
}
