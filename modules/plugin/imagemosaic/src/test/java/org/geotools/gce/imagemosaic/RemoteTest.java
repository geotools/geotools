/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.media.jai.PlanarImage;
import org.geotools.TestData;
import org.geotools.api.coverage.grid.GridEnvelope;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.datum.PixelInCell;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.junit.Assert;
import org.junit.Test;

public class RemoteTest {

    @Test
    public void testRemoteMosaic() throws IOException, TransformException {
        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(TestData.file(this, "remote_test"));

        GridEnvelope originalRange = reader.getOriginalGridRange();
        GeneralEnvelope envelope = reader.getOriginalEnvelope();
        CoordinateReferenceSystem nativeCRS = envelope.getCoordinateReferenceSystem();

        final int minX = originalRange.getLow(0);
        final int minY = originalRange.getLow(1);
        final int width = originalRange.getSpan(0);
        final int height = originalRange.getSpan(1);
        final int maxX = minX + (width <= 50 ? width : 50);
        final int maxY = minY + (height <= 50 ? height : 50);
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
        final GridCoverage2D gc = reader.read(new ParameterValue<?>[] {pam});
        Assert.assertNotNull(gc);
        assertNotBlank(
                "remote image mosaic",
                ((PlanarImage) gc.getRenderedImage()).getAsBufferedImage(),
                Color.BLACK);
        reader.dispose();
    }

    /**
     * Asserts that the image is not blank, in the sense that there must be pixels different from
     * the passed background color.
     *
     * @param testName the name of the test to throw meaningfull messages if something goes wrong
     * @param image the imgage to check it is not "blank"
     * @param bgColor the background color for which differing pixels are looked for
     */
    protected void assertNotBlank(String testName, BufferedImage image, Color bgColor) {
        int pixelsDiffer = 0;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (image.getRGB(x, y) != bgColor.getRGB()) {
                    ++pixelsDiffer;
                }
            }
        }
        assertTrue(testName + " image is completely blank", 0 < pixelsDiffer);
    }
}
