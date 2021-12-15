/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vsi;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.gdal.gdal.Dataset;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

/**
 * Tests for VRTDataset class
 *
 * @author Matthew Northcott <matthewnorthcott@catalyst.net.nz>
 */
public final class VSIDatasetTest {

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
    private Dataset datasetIn;
    private Dataset datasetOut;

    @Before
    public void setUp() {
        datasetIn = mock(Dataset.class);
        datasetOut = mock(Dataset.class);
    }

    @Test
    public void testStringInput() {
        final String fileName = "layer.tif";
        final String location = "/vsiswift/container/" + fileName;
        final File file = Paths.get(TEMP_DIR, "layer.tif.vrt").toFile();

        try (MockedStatic<VSIUtils> utils = mockStatic(VSIUtils.class)) {
            utils.when(() -> VSIUtils.isVSILocation(eq(location))).thenReturn(true);
            utils.when(() -> VSIUtils.getFileName(eq(location))).thenReturn(fileName);
            utils.when(() -> VSIUtils.openDataset(eq(location))).thenReturn(datasetIn);
            utils.when(() -> VSIUtils.datasetToVRT(eq(datasetIn), any())).thenReturn(datasetOut);

            final VSIDataset dataset = spy(new VSIDataset(location));

            utils.verify(() -> VSIUtils.openDataset(eq(location)), times(1));
            utils.verify(() -> VSIUtils.datasetToVRT(eq(datasetIn), any()), times(1));

            assertEquals(file.getAbsolutePath(), dataset.getFile().getAbsolutePath());
        }
    }

    @Test
    public void testWhenFileExists() throws IOException {
        final String fileName = "layer.tif";
        final String location = "/vsiswift/container/" + fileName;
        final File file = Paths.get(TEMP_DIR, "layer.tif.vrt").toFile();

        file.createNewFile();

        try (MockedStatic<VSIUtils> utils = mockStatic(VSIUtils.class)) {
            utils.when(() -> VSIUtils.isVSILocation(eq(location))).thenReturn(true);
            utils.when(() -> VSIUtils.getFileName(eq(location))).thenReturn(fileName);

            final VSIDataset dataset = spy(new VSIDataset(location));

            utils.verify(() -> VSIUtils.openDataset(eq(location)), times(0));
            utils.verify(() -> VSIUtils.datasetToVRT(eq(datasetIn), any()), times(0));

            assertEquals(file.getAbsolutePath(), dataset.getFile().getAbsolutePath());
        }

        file.delete();
    }

    @Test
    public void testFromObjectString() {
        final String fileName = "layer.tif";
        final String location = "/vsiswift/container/" + fileName;
        final File file = Paths.get(TEMP_DIR, "layer.tif.vrt").toFile();

        try (MockedStatic<VSIUtils> utils = mockStatic(VSIUtils.class)) {
            utils.when(() -> VSIUtils.isVSILocation(eq(location))).thenReturn(true);
            utils.when(() -> VSIUtils.getFileName(eq(location))).thenReturn(fileName);
            utils.when(() -> VSIUtils.openDataset(eq(location))).thenReturn(datasetIn);
            utils.when(() -> VSIUtils.datasetToVRT(eq(datasetIn), any())).thenReturn(datasetOut);

            final VSIDataset dataset = spy(VSIDataset.fromObject((Object) location));

            utils.verify(() -> VSIUtils.openDataset(eq(location)), times(1));
            utils.verify(() -> VSIUtils.datasetToVRT(eq(datasetIn), any()), times(1));

            assertEquals(file.getAbsolutePath(), dataset.getFile().getAbsolutePath());
        }
    }

    @Test(expected = RuntimeException.class)
    public void testFromObjectUnsupported() {
        VSIDataset.fromObject(mock(Object.class));
    }
}
