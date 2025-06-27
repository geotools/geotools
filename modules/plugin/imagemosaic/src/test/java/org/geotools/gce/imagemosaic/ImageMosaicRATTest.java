/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.gce.imagemosaic.TestUtils.getReader;
import static org.geotools.util.URLs.fileToUrl;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import it.geosolutions.imageio.pam.PAMDataset;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand.FieldDefn;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand.GDALRasterAttributeTable;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand.Row;
import it.geosolutions.imageio.pam.PAMParser;
import it.geosolutions.jaiext.range.NoDataContainer;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.geotools.api.data.ResourceInfo;
import org.geotools.api.referencing.FactoryException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.PAMResourceInfo;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.image.ImageWorker;
import org.geotools.test.TestData;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Test;

public class ImageMosaicRATTest {

    @Test
    public void testRATFromGeotiff() throws IOException, FactoryException {
        // setup data
        File mosaicSource = TestData.file(this, "bluetopo");
        String mosaicName = "bluetopo";
        File mosaicDirectory = new File("target", mosaicName);
        FileUtils.deleteQuietly(mosaicDirectory);
        FileUtils.copyDirectory(mosaicSource, mosaicDirectory);

        // load the source PAM files
        PAMParser parser = new PAMParser();
        PAMDataset pam1 = parser.parsePAM(new File(mosaicDirectory, "BlueTopo_BH4JS577_20230918.tiff.aux.xml"));
        PAMDataset pam2 = parser.parsePAM(new File(mosaicDirectory, "BlueTopo_BH4JS578_20230918.tiff.aux.xml"));
        PAMDataset.PAMRasterBand band_1_2 = pam1.getPAMRasterBand().get(2);
        PAMDataset.PAMRasterBand band_2_2 = pam2.getPAMRasterBand().get(2);

        // create mosaic and grab the pam databset from resource info
        ImageMosaicReader reader = getReader(fileToUrl(mosaicDirectory));
        ResourceInfo info = reader.getInfo(mosaicName);
        assertThat(info, CoreMatchers.instanceOf(PAMResourceInfo.class));
        PAMResourceInfo pamInfo = (PAMResourceInfo) info;
        PAMDataset pam = pamInfo.getPAMDataset();

        // check bands
        assertNotNull(pam);
        List<PAMDataset.PAMRasterBand> bands = pam.getPAMRasterBand();
        assertEquals(3, bands.size());
        assertNull(bands.get(0).getGdalRasterAttributeTable());
        assertNull(bands.get(1).getGdalRasterAttributeTable());
        PAMDataset.PAMRasterBand b2 = bands.get(2);
        assertNotNull(b2);

        // look at band 02 RAT, check the fields first
        GDALRasterAttributeTable rat = b2.getGdalRasterAttributeTable();
        List<FieldDefn> fields = rat.getFieldDefn();
        List<FieldDefn> originalFields = band_1_2.getGdalRasterAttributeTable().getFieldDefn();
        assertEquals(originalFields, fields);

        // now check the rows are a union of the two, there are some overlaps
        // so the union has less overall rows
        List<Row> rows = rat.getRow();
        List<Row> rows1 = band_1_2.getGdalRasterAttributeTable().getRow();
        List<Row> rows2 = band_2_2.getGdalRasterAttributeTable().getRow();
        assertTrue(rows.size() < rows1.size() + rows2.size());

        // pick a row that's present in both files, index=0
        List<String> row0 = rows.get(0).getF();
        List<String> row1_0 = rows1.get(0).getF();
        List<String> row2_0 = rows2.get(0).getF();
        assertEquals(row0.size(), row1_0.size());
        for (int i = 0; i < row0.size(); i++) {
            if (i == 1) continue; // count field, it's going to be the sum
            assertEquals(row0.get(i), row1_0.get(i));
            assertEquals(row0.get(i), row2_0.get(i));
        }
        // handle the count, must have been accumulated
        assertEquals(String.valueOf(1715857 + 2177566), row0.get(1));

        // now grab a row that's only in the first table
        String rowValueOnlyFirst = "1014908";
        List<String> rowOnlyFirst = lookupRow(rows, rowValueOnlyFirst);
        List<String> rowOnlyFirstOriginal = lookupRow(rows1, rowValueOnlyFirst);
        assertThrows(IndexOutOfBoundsException.class, () -> lookupRow(rows2, rowValueOnlyFirst));
        assertEquals(rowOnlyFirstOriginal, rowOnlyFirst);

        // and one that's only in the second table
        String rowValueOnlySecond = "35367";
        List<String> rowOnlySecond = lookupRow(rows, rowValueOnlySecond);
        assertThrows(IndexOutOfBoundsException.class, () -> lookupRow(rows1, rowValueOnlySecond));
        List<String> rowOnlySecondOriginal = lookupRow(rows2, rowValueOnlySecond);
        assertEquals(rowOnlySecondOriginal, rowOnlySecond);

        // check nodata
        GridCoverage2D coverage = reader.read();
        NoDataContainer cvNoData = CoverageUtilities.getNoDataProperty(coverage);
        assertEquals(Double.NaN, cvNoData.getAsSingleValue(), 0d);
        RenderedImage ri = coverage.getRenderedImage();
        assertEquals(Double.NaN, new ImageWorker(ri).getNoData().getMin().doubleValue(), 0d);

        coverage.dispose(true);
        reader.dispose();
    }

    @Test
    public void testReloadRAT() throws IOException, FactoryException {
        // setup data
        File mosaicSource = TestData.file(this, "bluetopo");
        String mosaicName = "bluetopoReload";
        File mosaicDirectory = new File("target", mosaicName);
        FileUtils.deleteQuietly(mosaicDirectory);
        FileUtils.copyDirectory(mosaicSource, mosaicDirectory);

        // the aux files
        String pamFileName = "BlueTopo_BH4JS577_20230918.tiff.aux.xml";
        File small = new File(mosaicDirectory, "BlueTopo_BH4JS577_20230918.tiff.small.aux.xml");
        File full = new File(mosaicDirectory, pamFileName);

        // rename p1 so that it won't be found
        File hideMe = new File(full.getParent(), "hideme.xml");
        assertTrue(full.renameTo(hideMe));
        assertTrue(small.renameTo(full));

        // create mosaic and grab the pam databset from resource info
        ImageMosaicReader reader = getReader(fileToUrl(mosaicDirectory));
        ResourceInfo info = reader.getInfo(mosaicName);
        assertThat(info, CoreMatchers.instanceOf(PAMResourceInfo.class));
        PAMResourceInfo pamInfo = (PAMResourceInfo) info;
        PAMDataset pam = pamInfo.getPAMDataset();

        // grab the raster attribute table
        assertNotNull(pam);
        List<PAMDataset.PAMRasterBand> bands = pam.getPAMRasterBand();
        PAMDataset.PAMRasterBand b2 = bands.get(2);
        GDALRasterAttributeTable rat = b2.getGdalRasterAttributeTable();

        List<Row> originalRows = rat.getRow();

        // now add back the PAM file and force reload
        assertTrue(full.delete());
        assertTrue(hideMe.renameTo(full));
        assertTrue(pamInfo.reloadPAMDataset());

        // grab the PAM dataset again
        rat = pamInfo.getPAMDataset().getPAMRasterBand().get(2).getGdalRasterAttributeTable();
        List<Row> reloadedRows = rat.getRow();

        // check all the rows in the original are already available in the reloaded, but reloaded
        // has more
        assertThat(reloadedRows.size(), Matchers.greaterThan(originalRows.size()));
        for (Row row : originalRows) {
            String originalValue = row.getF().get(0);
            assertTrue(
                    "Could not find " + originalValue,
                    reloadedRows.stream()
                            .filter(r -> r.getF().get(0).equals(originalValue))
                            .findFirst()
                            .isPresent());
        }

        reader.dispose();
    }

    private List<String> lookupRow(List<Row> rows, String reference) {
        return rows.stream()
                .filter(row -> row.getF().get(0).equals(reference))
                .map(row -> row.getF())
                .findFirst()
                .orElseThrow(() -> new IndexOutOfBoundsException("Row not found"));
    }
}
