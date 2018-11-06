/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.sun.media.jai.operator.ImageReadDescriptor;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import org.geotools.TestData;
import org.geotools.coverage.processing.MosaicTest;
import org.geotools.image.util.ImageUtilities;
import org.junit.Test;

public class DisposeStopperTest {

    @Test
    public void testDispose() throws FileNotFoundException, IOException {
        final File tiff = TestData.file(MosaicTest.class, "sample0.tif");

        final TIFFImageReader reader =
                (it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader)
                        new TIFFImageReaderSpi().createReaderInstance();
        final ImageInputStream stream = ImageIO.createImageInputStream(tiff);
        reader.setInput(stream);
        RenderedImage image =
                ImageReadDescriptor.create(
                        stream, 0, false, false, false, null, null, null, reader, null);

        DisposeStopper stopper = new DisposeStopper(image);
        boolean readSuccess = true;
        try {
            // Try to dispose. It shouldn't since we are using the wrapper.
            ImageUtilities.disposeImage(stopper);
            assertNotNull(image);

            // I still can get data since using the stopper the image isn't disposed
            assertEquals(187, image.getData().getSample(1, 5, 0));

            ImageUtilities.disposeImage(image);
            image.getData().getSample(1, 5, 0);
        } catch (RuntimeException ioe) {
            // The dispose on the image (without using the disposeStopper wrapper)
            // should have successfully disposed so the read can't success
            readSuccess = false;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Throwable t) {

                }
            }
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {

                }
            }
        }
        assertFalse(readSuccess);
    }
}
