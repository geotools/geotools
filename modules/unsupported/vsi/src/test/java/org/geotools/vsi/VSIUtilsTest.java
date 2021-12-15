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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNoException;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.junit.Test;
import org.mockito.MockedStatic;

/**
 * Tests for VSIUtils class
 *
 * @author Matthew Northcott <matthewnorthcott@catalyst.net.nz>
 */
public final class VSIUtilsTest {

    @Test
    public void testIsVSILocationString() {
        assertTrue(VSIUtils.isVSILocation("/vsiswift/path/to/file.tif"));
        assertTrue(VSIUtils.isVSILocation("/vsiswift/path/without/extension"));
        assertTrue(VSIUtils.isVSILocation("/vsizip/path/to/zipfile.zip"));
        assertTrue(VSIUtils.isVSILocation("/vsizip/path/to/zipfile.zip/location/in/file.tif"));
        assertTrue(VSIUtils.isVSILocation("/vsizip//vsiswift/remote/path/to/archive.zip"));
        assertTrue(
                VSIUtils.isVSILocation(
                        "/vsizip//vsiswift/remote/path/to/archive.zip/location/in/file.tif"));

        assertFalse(VSIUtils.isVSILocation("/vsi/invalid/prefix.tif"));
        assertFalse(VSIUtils.isVSILocation("/vsinoexist/invalid/prefix.tif"));
        assertFalse(VSIUtils.isVSILocation("/no/prefix.tif"));
        assertFalse(VSIUtils.isVSILocation("/"));
        assertFalse(VSIUtils.isVSILocation(""));
    }

    @Test
    public void testIsVSILocationNull() {
        assertFalse(VSIUtils.isVSILocation(null));
    }

    @Test
    public void testGetFileName() {
        assertEquals(VSIUtils.getFileName("/vsiswift/path/to/file.tif"), "file.tif");
        assertEquals(VSIUtils.getFileName("/vsiswift/path/without/extension"), "extension");
        assertEquals(VSIUtils.getFileName("/vsizip/path/to/zipfile.zip"), "zipfile.zip");
        assertEquals(
                VSIUtils.getFileName("/vsizip/path/to/zipfile.zip/location/in/file.tif"),
                "file.tif");
        assertEquals(
                VSIUtils.getFileName("/vsizip//vsiswift/remote/path/to/archive.zip"),
                "archive.zip");
        assertEquals(
                VSIUtils.getFileName(
                        "/vsizip//vsiswift/remote/path/to/archive.zip/location/in/file.tif"),
                "file.tif");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFileNameNull() {
        VSIUtils.getFileName(null);
    }

    @Test
    public void testOpenDataset() throws IOException {
        final String location = "/vsiswift/path/to/file.tif";
        final Dataset dataset = mock(Dataset.class);

        try (MockedStatic<gdal> gd = mockStatic(gdal.class)) {
            gd.when(() -> gdal.Open(anyString(), anyInt())).thenReturn(dataset);

            assertEquals(VSIUtils.openDataset(location), dataset);
            gd.verify(() -> gdal.Open(anyString(), anyInt()), times(1));
        } catch (NoClassDefFoundError | UnsatisfiedLinkError ex) {
            assumeNoException(ex);
        }
    }

    @Test(expected = IOException.class)
    public void testOpenDatasetInvalid() throws IOException {
        try {
            VSIUtils.openDataset("/not/a/valid/location");
        } catch (NoClassDefFoundError | UnsatisfiedLinkError ex) {
            assumeNoException(ex);
        }
    }

    @Test
    public void testAddAlphaToVRT() {
        final Dataset dataset = mock(Dataset.class);
        final Band band = mock(Band.class);
        final Map<String, Object> metadata = new Hashtable<>();

        metadata.put("source_0", (Object) "<SourceBand>0");
        doReturn(band).when(dataset).GetRasterBand(anyInt());
        doReturn(metadata).when(band).GetMetadata_Dict(eq("vrt_sources"));

        try {
            VSIUtils.addAlphaToVRT(dataset);
        } catch (NoClassDefFoundError | UnsatisfiedLinkError ex) {
            assumeNoException(ex);
        }

        verify(dataset, times(2)).GetRasterBand(anyInt());
        verify(band, times(1)).SetColorInterpretation(anyInt());
        verify(band, times(1)).GetMetadata_Dict(eq("vrt_sources"));
        verify(band, times(1))
                .SetMetadataItem(eq("source_0"), eq("<SourceBand>mask"), eq("new_vrt_sources"));
        verify(dataset, times(1)).FlushCache();
    }

    @Test
    public void testDatasetToVRT() throws IOException {
        final String path = "/tmp/mydataset.vrt";
        final Driver driver = mock(Driver.class);
        final Dataset source = mock(Dataset.class);
        final Dataset destination = mock(Dataset.class);
        final Band band = mock(Band.class);
        final File file = mock(File.class);

        final Map<String, Object> metadata = new Hashtable<>();
        metadata.put("source_0", (Object) "<SourceBand>0");

        doReturn(path).when(file).getAbsolutePath();
        doReturn(destination).when(driver).CreateCopy(eq(path), eq(source));
        doReturn(band).when(destination).GetRasterBand(anyInt());
        doReturn(metadata).when(band).GetMetadata_Dict(eq("vrt_sources"));

        try (MockedStatic<gdal> gd = mockStatic(gdal.class)) {
            gd.when(() -> gdal.GetDriverByName(eq("VRT"))).thenReturn(driver);

            assertEquals(VSIUtils.datasetToVRT(source, file), destination);
            gd.verify(() -> gdal.GetDriverByName(eq("VRT")), times(1));
        } catch (NoClassDefFoundError | UnsatisfiedLinkError ex) {
            assumeNoException(ex);
        }

        verify(file, times(1)).getAbsolutePath();
        verify(driver, times(1)).CreateCopy(eq(path), eq(source));

        // verify addAlphaToVRT was called
        verify(destination, times(2)).GetRasterBand(anyInt());
        verify(band, times(1)).SetColorInterpretation(anyInt());
        verify(band, times(1)).GetMetadata_Dict(eq("vrt_sources"));
        verify(band, times(1))
                .SetMetadataItem(eq("source_0"), eq("<SourceBand>mask"), eq("new_vrt_sources"));
    }

    @Test(expected = IOException.class)
    public void testDatasetToVRTInvalid() throws IOException {
        final String path = "/tmp/mydataset.vrt";
        final Driver driver = mock(Driver.class);
        final Dataset source = mock(Dataset.class);
        final File file = mock(File.class);

        doReturn(path).when(file).getAbsolutePath();
        doReturn(null).when(driver).CreateCopy(eq(path), eq(source));

        try (MockedStatic<gdal> gd = mockStatic(gdal.class)) {
            gd.when(() -> gdal.GetDriverByName(eq("VRT"))).thenReturn(driver);

            VSIUtils.datasetToVRT(source, file);
        }
    }
}
