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
 */
package org.geotools.gce.imagemosaic;

import static org.junit.Assert.assertTrue;

import com.google.common.io.Files;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import org.geotools.TestData;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

public class MultiResolutionTest {

    @Test
    public void testCatalogBuilderSample() throws IOException, TransformException {
        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(TestData.file(this, "multiresolution"));
        // the following code is copied from GeoServer's CatalogBuilder
        // which is the code our case failed on
        // the idea is to create a 5x5 test sample image
        final GridCoverage2D gc;
        GridEnvelope originalRange = reader.getOriginalGridRange();
        GeneralEnvelope envelope = reader.getOriginalEnvelope();
        CoordinateReferenceSystem nativeCRS = envelope.getCoordinateReferenceSystem();
        // final ParameterValueGroup readParams = format.getReadParameters();
        // final Map parameters = CoverageUtils.getParametersKVP(readParams);
        final int minX = originalRange.getLow(0);
        final int minY = originalRange.getLow(1);
        final int width = originalRange.getSpan(0);
        final int height = originalRange.getSpan(1);
        final int maxX = minX + (width <= 5 ? width : 5);
        final int maxY = minY + (height <= 5 ? height : 5);
        // we have to be sure that we are working against a valid grid range.
        final GridEnvelope2D testRange = new GridEnvelope2D(minX, minY, maxX, maxY);
        // build the corresponding envelope
        final MathTransform gridToWorldCorner =
                reader.getOriginalGridToWorld(PixelInCell.CELL_CORNER);
        final GeneralEnvelope testEnvelope =
                CRS.transform(gridToWorldCorner, new GeneralEnvelope(testRange.getBounds()));
        testEnvelope.setCoordinateReferenceSystem(nativeCRS);
        ParameterValue<GridGeometry2D> pam = AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        pam.setValue(new GridGeometry2D(testRange, testEnvelope));
        gc = reader.read(new ParameterValue<?>[] {pam});
        // gc would be null before bug fix
        Assert.assertNotNull(gc);
        reader.dispose();
    }

    @Test
    public void testVirtualNativeResolution() throws IOException, TransformException {
        TemporaryFolder folder = new TemporaryFolder();
        folder.create();
        File file = folder.newFile("sample.tif");
        Files.copy(TestData.file(this, "multiresolution/sample.tif"), file);

        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(folder.getRoot());
        GridEnvelope originalRange = reader.getOriginalGridRange();
        GeneralEnvelope envelope = reader.getOriginalEnvelope();
        MathTransform g2w = reader.getOriginalGridToWorld(PixelInCell.CELL_CORNER);
        AffineTransform2D at = (AffineTransform2D) g2w;

        ParameterValue<double[]> virtualNativeRes =
                ImageMosaicFormat.VIRTUAL_NATIVE_RESOLUTION.createValue();

        // Specifying a lower virtual native resolution
        virtualNativeRes.setValue(new double[] {at.getScaleX() * 16, -at.getScaleY() * 16});

        ParameterValue<GridGeometry2D> pam = AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        pam.setValue(new GridGeometry2D(originalRange, envelope));

        GridCoverage2D gc = reader.read(new ParameterValue<?>[] {virtualNativeRes, pam});
        RenderedImage ri = gc.getRenderedImage();

        // The Virtual Native Resolution resulted into getting back an image with
        // few pixels (Very low resolution)
        assertTrue(ri.getWidth() < 20);
        assertTrue(ri.getHeight() < 20);
        reader.dispose();
    }

    @Test
    public void testVirtualNativeResolutionNoOverviews() throws IOException, TransformException {
        TemporaryFolder folder = new TemporaryFolder();
        folder.create();
        File srtm = folder.newFile("sample_noov.tif");
        Files.copy(TestData.file(this, "multiresolution/sample_noov.tif"), srtm);

        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(folder.getRoot());
        GridEnvelope originalRange = reader.getOriginalGridRange();
        GeneralEnvelope envelope = reader.getOriginalEnvelope();
        MathTransform g2w = reader.getOriginalGridToWorld(PixelInCell.CELL_CORNER);
        AffineTransform2D at = (AffineTransform2D) g2w;

        ParameterValue<double[]> virtualNativeRes =
                ImageMosaicFormat.VIRTUAL_NATIVE_RESOLUTION.createValue();

        // Specifying a lower virtual native resolution
        virtualNativeRes.setValue(new double[] {at.getScaleX() * 16, -at.getScaleY() * 16});

        ParameterValue<GridGeometry2D> pam = AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        pam.setValue(new GridGeometry2D(originalRange, envelope));

        GridCoverage2D gc = reader.read(new ParameterValue<?>[] {virtualNativeRes, pam});
        RenderedImage ri = gc.getRenderedImage();

        // The Virtual Native Resolution resulted into getting back an image with
        // few pixels (Very low resolution)
        assertTrue(ri.getWidth() < 20);
        assertTrue(ri.getHeight() < 20);
        reader.dispose();
    }

    @Test
    public void testPickHighestResolution() throws IOException, TransformException {
        TemporaryFolder folder = new TemporaryFolder();
        folder.create();
        File srtm = folder.newFile("srtm.tiff");
        File sfdem = folder.newFile("sfdem.tiff");
        Files.copy(TestData.file(this, "multiresolution/srtm.tiff"), srtm);
        Files.copy(TestData.file(this, "multiresolution/sfdem.tiff"), sfdem);

        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(folder.getRoot());

        GeoTiffFormat tiffFormat = new GeoTiffFormat();
        GeoTiffReader sfdemReader = tiffFormat.getReader(sfdem);

        assertEquals(sfdemReader.getResolutionLevels()[0], reader.getResolutionLevels()[0], 0.001);
        sfdemReader.dispose();
    }

    private void assertEquals(double[] expected, double[] actuals, double epsilon) {
        Assert.assertEquals(expected.length, actuals.length);
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], actuals[i], epsilon);
        }
    }
}
