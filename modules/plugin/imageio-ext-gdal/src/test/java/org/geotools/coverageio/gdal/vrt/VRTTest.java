/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.coverageio.gdal.vrt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import it.geosolutions.imageio.pam.PAMDataset;
import it.geosolutions.imageio.pam.PAMParser;
import it.geosolutions.jaiext.range.NoDataContainer;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import org.apache.commons.io.FileUtils;
import org.geotools.api.data.ResourceInfo;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.PAMResourceInfo;
import org.geotools.coverageio.gdal.BaseGDALGridCoverage2DReader;
import org.geotools.coverageio.gdal.GDALTestCase;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.geometry.GeneralBounds;
import org.geotools.image.util.ImageUtilities;
import org.geotools.test.TestData;
import org.geotools.util.NumberRange;
import org.geotools.util.factory.Hints;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 * @author Ugo Moschini, GeoSolutions
 *     <p>Testing {@link VRTReader}
 */
public final class VRTTest extends GDALTestCase {

    /** file name of a valid VRT file to be used for tests. */
    private static final String fileName = "n43.dt0.vrt";

    public VRTTest() {
        super("VRT", new VRTFormatFactory());
    }

    @Test
    public void testService() throws NoSuchAuthorityCodeException, FactoryException {
        GridFormatFinder.scanForPlugins();

        Iterator<GridFormatFactorySpi> list =
                GridFormatFinder.getAvailableFormats().iterator();
        boolean found = false;
        GridFormatFactorySpi fac = null;

        while (list.hasNext()) {
            fac = list.next();

            if (fac instanceof VRTFormatFactory) {
                found = true;

                break;
            }
        }

        Assert.assertTrue("VRTFormatFactory not registered", found);
        Assert.assertTrue("VRTFormatFactory not available", fac.isAvailable());
        Assert.assertNotNull(new VRTFormatFactory().createFormat());
    }

    @Test
    public void test() throws Exception {
        // Preparing an useful layout in case the image is striped.
        final ImageLayout l = new ImageLayout();
        l.setTileGridXOffset(0).setTileGridYOffset(0).setTileHeight(512).setTileWidth(512);

        Hints hints = new Hints();
        hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, l));

        // get a reader
        final File file = TestData.file(this, fileName);
        final BaseGDALGridCoverage2DReader reader = new VRTReader(file, hints);

        // /////////////////////////////////////////////////////////////////////
        //
        // read once
        //
        // /////////////////////////////////////////////////////////////////////
        GridCoverage2D gc = reader.read();
        forceDataLoading(gc);

        // check the nodata has been read
        double noData = getNoData(gc);

        final double NO_DATA_VALUE = -3.27670000000000E+04;
        assertEquals(NO_DATA_VALUE, noData, 1);
        GridSampleDimension sd = gc.getSampleDimension(0);
        assertEquals(1, sd.getCategories().size());
        Category noDataCategory = sd.getCategories().get(0);
        assertEquals("No data", noDataCategory.getName().toString());
        assertEquals(new NumberRange<>(Double.class, NO_DATA_VALUE, NO_DATA_VALUE), noDataCategory.getRange());

        // /////////////////////////////////////////////////////////////////////
        //
        // read again with subsampling and crop
        //
        // /////////////////////////////////////////////////////////////////////
        final double cropFactor = 2.0;
        final int oldW = gc.getRenderedImage().getWidth();
        final int oldH = gc.getRenderedImage().getHeight();

        assertEquals(121, oldW);
        assertEquals(121, oldH);
        // check for expected data type
        assertEquals(
                DataBuffer.TYPE_SHORT, gc.getRenderedImage().getSampleModel().getDataType());

        final Rectangle range = (GridEnvelope2D) reader.getOriginalGridRange();
        final GeneralBounds oldEnvelope = reader.getOriginalEnvelope();

        final GeneralBounds cropEnvelope = new GeneralBounds(
                new double[] {
                    oldEnvelope.getLowerCorner().getOrdinate(0) + oldEnvelope.getSpan(0) / cropFactor,
                    oldEnvelope.getLowerCorner().getOrdinate(1) + oldEnvelope.getSpan(1) / cropFactor
                },
                new double[] {
                    oldEnvelope.getUpperCorner().getOrdinate(0),
                    oldEnvelope.getUpperCorner().getOrdinate(1)
                });
        cropEnvelope.setCoordinateReferenceSystem(reader.getCoordinateReferenceSystem());

        final ParameterValue gg = ((AbstractGridFormat) reader.getFormat()).READ_GRIDGEOMETRY2D.createValue();
        gg.setValue(new GridGeometry2D(
                new GridEnvelope2D(new Rectangle(
                        0, 0, (int) (range.width / 2.0 / cropFactor), (int) (range.height / 2.0 / cropFactor))),
                cropEnvelope));
        gc.dispose(true);
        gc = reader.read(new GeneralParameterValue[] {gg});
        forceDataLoading(gc);

        ResourceInfo info = reader.getInfo("n43.dt0");
        assertThat(info, CoreMatchers.instanceOf(PAMResourceInfo.class));
        assertPamDataset((PAMResourceInfo) info);

        gc.dispose(true);
        reader.dispose();
    }

    @Test
    public void testNaN() throws Exception {
        // Preparing an useful layout in case the image is striped.
        final ImageLayout l = new ImageLayout();
        l.setTileGridXOffset(0).setTileGridYOffset(0).setTileHeight(512).setTileWidth(512);

        Hints hints = new Hints();
        hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, l));

        // get a reader
        final File file = TestData.file(this, "n43.dt0.nan.vrt");
        final BaseGDALGridCoverage2DReader reader = new VRTReader(file, hints);

        GridCoverage2D gc = reader.read();

        // check the nodata has been read
        double noData = getNoData(gc);
        assertEquals(Double.NaN, noData, 0);

        gc.dispose(true);
        reader.dispose();
    }

    private void assertPamDataset(PAMResourceInfo pamResourceInfo) {
        PAMDataset pam = pamResourceInfo.getPAMDataset();

        assertNotNull(pam);
        List<PAMDataset.PAMRasterBand> bands = pam.getPAMRasterBand();
        assertEquals(1, bands.size());
        PAMDataset.PAMRasterBand band = bands.get(0);
        PAMDataset.PAMRasterBand.GDALRasterAttributeTable rat = band.getGdalRasterAttributeTable();
        assertNotNull(rat);

        // Check each field
        List<PAMDataset.PAMRasterBand.FieldDefn> fields = rat.getFieldDefn();
        assertEquals(3, fields.size());
        assertField(
                fields.get(0),
                "con_min",
                PAMDataset.PAMRasterBand.FieldType.Real,
                PAMDataset.PAMRasterBand.FieldUsage.Min);
        assertField(
                fields.get(1),
                "con_max",
                PAMDataset.PAMRasterBand.FieldType.Real,
                PAMDataset.PAMRasterBand.FieldUsage.Max);
        assertField(
                fields.get(2),
                "test",
                PAMDataset.PAMRasterBand.FieldType.String,
                PAMDataset.PAMRasterBand.FieldUsage.Generic);

        // Check rows
        List<PAMDataset.PAMRasterBand.Row> rows = rat.getRow();
        assertEquals(8, rows.size());

        // one sample row
        PAMDataset.PAMRasterBand.Row row = rows.get(1);
        List<String> fieldValues = row.getF();
        assertEquals("1.4", fieldValues.get(0));
        assertEquals("1.6", fieldValues.get(1));
        assertEquals("white", fieldValues.get(2));
    }

    private void assertField(
            PAMDataset.PAMRasterBand.FieldDefn fieldDefn,
            String name,
            PAMDataset.PAMRasterBand.FieldType type,
            PAMDataset.PAMRasterBand.FieldUsage usage) {
        assertEquals(name, fieldDefn.getName());
        assertEquals(type, fieldDefn.getType());
        assertEquals(usage, fieldDefn.getUsage());
    }

    private double getNoData(GridCoverage2D gc) {
        Object property = gc.getProperty(NoDataContainer.GC_NODATA);
        if (property != null) {
            if (property instanceof NoDataContainer) {
                return ((NoDataContainer) property).getAsRange().getMin().doubleValue();
            } else if (property instanceof Double) {
                return (Double) property;
            }
        }
        fail("No data not found");
        return Double.NaN; // just to make compiler happy
    }

    @Test
    public void testRATFromVRTMosaic() throws IOException, FactoryException {
        // setup data so that we the pam files only inside the VRTS
        String mosaicName = "bluetopo_vrt";
        File mosaicDirectory = new File("target", mosaicName);
        FileUtils.deleteQuietly(mosaicDirectory);
        FileUtils.copyDirectory(TestData.file(this, "mosaic"), mosaicDirectory);

        // load the source PAM files
        PAMParser parser = new PAMParser();
        PAMDataset pam1 = parser.parsePAM(TestData.file(this, "BlueTopo_BH4JS577_20230918.tiff.aux.xml"));
        PAMDataset pam2 = parser.parsePAM(TestData.file(this, "BlueTopo_BH4JS578_20230918.tiff.aux.xml"));
        PAMDataset.PAMRasterBand band_1_2 = pam1.getPAMRasterBand().get(2);
        PAMDataset.PAMRasterBand band_2_2 = pam2.getPAMRasterBand().get(2);

        ImageMosaicReader reader = new ImageMosaicReader(mosaicDirectory);

        // create mosaic and grab the pam databset from resource info
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
        PAMDataset.PAMRasterBand.GDALRasterAttributeTable rat = b2.getGdalRasterAttributeTable();
        List<PAMDataset.PAMRasterBand.FieldDefn> fields = rat.getFieldDefn();
        List<PAMDataset.PAMRasterBand.FieldDefn> originalFields =
                band_1_2.getGdalRasterAttributeTable().getFieldDefn();
        assertEquals(originalFields, fields);

        // now check the rows are a union of the two, there are some overlaps
        // so the union has less overall rows
        List<PAMDataset.PAMRasterBand.Row> rows = rat.getRow();
        List<PAMDataset.PAMRasterBand.Row> rows1 =
                band_1_2.getGdalRasterAttributeTable().getRow();
        List<PAMDataset.PAMRasterBand.Row> rows2 =
                band_2_2.getGdalRasterAttributeTable().getRow();
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

        reader.dispose();
    }

    @Test
    public void testGDALBandSelection() throws IOException, FactoryException {
        // setup data so that we the pam files only inside the VRTS
        String mosaicName = "bluetopo_vrt";
        File mosaicDirectory = new File("target", mosaicName);
        FileUtils.deleteQuietly(mosaicDirectory);
        FileUtils.copyDirectory(TestData.file(this, "mosaic"), mosaicDirectory);

        ImageMosaicReader reader = new ImageMosaicReader(mosaicDirectory);
        ParameterValue<int[]> bandsParam = AbstractGridFormat.BANDS.createValue();
        bandsParam.setValue(new int[] {2}); // select band 2 only
        GridCoverage2D readCoverage = reader.read(bandsParam);
        RenderedImage image = readCoverage.getRenderedImage();
        SampleModel sm = image.getSampleModel();
        ColorModel cm = image.getColorModel();
        ColorSpace colorSpace = cm.getColorSpace();
        // Original image is 37*42 pixels, 3 bands
        // Let's verify we only get one band image
        Assert.assertEquals(1, sm.getNumBands());
        Assert.assertEquals(1, cm.getNumComponents());
        Assert.assertEquals(ColorSpace.TYPE_GRAY, colorSpace.getType());
        ImageUtilities.disposeImage(image);
        reader.dispose();
    }

    private List<String> lookupRow(List<PAMDataset.PAMRasterBand.Row> rows, String reference) {
        return rows.stream()
                .filter(row -> row.getF().get(0).equals(reference))
                .map(row -> row.getF())
                .findFirst()
                .orElseThrow(() -> new IndexOutOfBoundsException("Row not found"));
    }
}
