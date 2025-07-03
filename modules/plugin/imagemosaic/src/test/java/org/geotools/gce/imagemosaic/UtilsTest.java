/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.PriorityQueue;
import javax.media.jai.Histogram;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class UtilsTest {

    @ClassRule
    public static TemporaryFolder folder = new TemporaryFolder();

    @Before
    @After
    public void reset() {
        System.clearProperty(Utils.SAMPLE_IMAGE_ALLOWLIST_KEY);
        Utils.resetSampleImageAllowlist();
    }

    @Test
    public void testGetHistogramValid() throws Exception {
        String file = folder.newFile("valid.histogram").getAbsolutePath();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(new Histogram(1, 0.0, 1.0, 1));
        }
        assertNotNull(Utils.getHistogram(file));
    }

    @Test
    public void testGetHistogramInvalid() throws Exception {
        String file = folder.newFile("invalid.histogram").getAbsolutePath();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(new PriorityQueue<>());
        }
        assertNull(Utils.getHistogram(file));
    }

    @Test
    public void testLoadSampleImageValid() throws Exception {
        File file = folder.newFile("valid.sample.image").getAbsoluteFile();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            BufferedImage image = new BufferedImage(1, 1, 1);
            out.writeObject(new SampleImage(image.getSampleModel(), image.getColorModel()));
        }
        assertNotNull(Utils.loadSampleImage(file));
    }

    @Test
    public void testLoadSampleImageInvalid() throws Exception {
        File file = folder.newFile("invalid.sample.image").getAbsoluteFile();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(new PriorityQueue<>());
        }
        assertNull(Utils.loadSampleImage(file));
    }
}
