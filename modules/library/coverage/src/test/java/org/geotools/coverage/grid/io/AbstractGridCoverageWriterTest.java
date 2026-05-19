/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.stream.FileCacheImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import org.eclipse.imagen.operator.ConstantDescriptor;
import org.geotools.api.coverage.grid.Format;
import org.geotools.api.coverage.grid.GridCoverage;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.image.io.ImageIOExt;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Verifies that {@link AbstractGridCoverageWriter#getImageOutputStream} picks the right stream type based on image size
 * and destination type. Uses {@link ConstantDescriptor} images which are lazy (no pixel RAM allocated) so large
 * dimensions don't cause OOM.
 */
public class AbstractGridCoverageWriterTest {

    private Long savedThreshold;

    @Before
    public void saveThreshold() {
        savedThreshold = ImageIOExt.getFilesystemThreshold();
    }

    @After
    public void restoreThreshold() {
        ImageIOExt.setFilesystemThreshold(savedThreshold);
    }

    static class TestWriter extends AbstractGridCoverageWriter {
        TestWriter(Object destination) {
            this.destination = destination;
        }

        @Override
        public Format getFormat() {
            return null;
        }

        @Override
        public void write(GridCoverage coverage, GeneralParameterValue... params) {}

        public ImageOutputStream openStream(RenderedImage image) throws IOException {
            return getImageOutputStream(image);
        }
    }

    private static RenderedImage constantImage(int width, int height) {
        return ConstantDescriptor.create((float) width, (float) height, new Byte[] {0}, null);
    }

    @Test
    public void testOutputStreamSmallImageUsesMemoryCache() throws IOException {
        // 100x100x1 byte = 10000 bytes, threshold = 50000 → memory cache
        ImageIOExt.setFilesystemThreshold(50_000L);
        TestWriter writer = new TestWriter(new ByteArrayOutputStream());
        try {
            assertTrue(writer.openStream(constantImage(100, 100)) instanceof MemoryCacheImageOutputStream);
        } finally {
            writer.dispose();
        }
    }

    @Test
    public void testOutputStreamLargeImageUsesFileCache() throws IOException {
        // 100000x100000x1 byte = 10^10 bytes >> threshold=1 → file cache (no OOM: ConstantDescriptor is lazy)
        ImageIOExt.setFilesystemThreshold(1L);
        TestWriter writer = new TestWriter(new ByteArrayOutputStream());
        try {
            assertTrue(writer.openStream(constantImage(100_000, 100_000)) instanceof FileCacheImageOutputStream);
        } finally {
            writer.dispose();
        }
    }

    @Test
    public void testFileDestinationNotMemoryCached() throws IOException {
        // File destination should never use an in-memory buffer, regardless of image size.
        // imageio-ext registers its own file stream SPI, so check behaviour not exact type.
        File tmp = File.createTempFile("writer-test", ".tif");
        tmp.deleteOnExit();
        TestWriter writer = new TestWriter(tmp);
        try {
            assertFalse(writer.openStream(constantImage(100, 100)) instanceof MemoryCacheImageOutputStream);
        } finally {
            writer.dispose();
        }
    }
}
