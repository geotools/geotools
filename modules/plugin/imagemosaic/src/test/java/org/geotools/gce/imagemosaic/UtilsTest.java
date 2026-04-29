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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import it.geosolutions.imageio.pam.PAMDataset;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand.Metadata;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand.Metadata.MDI;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import org.eclipse.imagen.ComponentSampleModelImageN;
import org.eclipse.imagen.Histogram;
import org.hamcrest.CoreMatchers;
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

    @Test
    public void testMigrateSampleImage() throws Exception {
        // path to a sample image still containing ImageN references, to test migration (package rebasing in the loader)
        File oldSampleImage = new File("src/test/resources/org/geotools/gce/imagemosaic/test-data/old_sample_image");
        RenderedImage renderedImage = Utils.loadSampleImage(oldSampleImage);
        assertThat(renderedImage.getSampleModel(), CoreMatchers.instanceOf(ComponentSampleModelImageN.class));
    }

    @Test
    public void testMigrateSampleImage2() throws Exception {
        // path to a sample image still containing ImageN references, to test migration (package rebasing in the loader)
        File oldSampleImage = new File("src/test/resources/org/geotools/gce/imagemosaic/test-data/old_sample_image2");
        RenderedImage renderedImage = Utils.loadSampleImage(oldSampleImage);
        assertThat(renderedImage.getSampleModel(), CoreMatchers.instanceOf(ComponentSampleModelImageN.class));
    }

    @Test
    public void testMergePamDatasetsSkipsNullAndEmptyDatasets() {
        PAMDataset empty = new PAMDataset();
        PAMDataset first = pamDataset(pamRasterBand(10, 100));
        PAMDataset second = pamDataset(pamRasterBand(5, 120));

        PAMDataset merged = Utils.mergePamDatasets(new PAMDataset[] {null, empty, first, second});

        assertNotNull(merged);
        assertEquals(1, merged.getPAMRasterBand().size());
        List<MDI> metadata = merged.getPAMRasterBand().get(0).getMetadata().getMDI();
        assertEquals("5.0", metadataValue(metadata, "STATISTICS_MINIMUM"));
        assertEquals("120.0", metadataValue(metadata, "STATISTICS_MAXIMUM"));
    }

    @Test
    public void testMergePamDatasetsReturnsSingleValidDataset() {
        PAMDataset valid = pamDataset(pamRasterBand(10, 100));

        assertSame(valid, Utils.mergePamDatasets(new PAMDataset[] {null, new PAMDataset(), valid}));
    }

    @Test(expected = IllegalStateException.class)
    public void testMergePamDatasetsRejectsInconsistentBandCounts() {
        Utils.mergePamDatasets(new PAMDataset[] {
            pamDataset(pamRasterBand(10, 100)), pamDataset(pamRasterBand(5, 120), pamRasterBand(20, 200))
        });
    }

    private static PAMDataset pamDataset(PAMRasterBand... bands) {
        PAMDataset dataset = new PAMDataset();
        dataset.getPAMRasterBand().addAll(Arrays.asList(bands));
        return dataset;
    }

    private static PAMRasterBand pamRasterBand(double minimum, double maximum) {
        PAMRasterBand band = new PAMRasterBand();
        Metadata metadata = new Metadata();
        metadata.getMDI().add(mdi("STATISTICS_MINIMUM", minimum));
        metadata.getMDI().add(mdi("STATISTICS_MAXIMUM", maximum));
        band.setMetadata(metadata);
        return band;
    }

    private static MDI mdi(String key, double value) {
        MDI mdi = new MDI();
        mdi.setKey(key);
        mdi.setValue(Double.toString(value));
        return mdi;
    }

    private static String metadataValue(List<MDI> metadata, String key) {
        return metadata.stream()
                .filter(mdi -> key.equals(mdi.getKey()))
                .findFirst()
                .map(MDI::getValue)
                .orElse(null);
    }
}
