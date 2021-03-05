/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.gtopo30;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.zip.ZipOutputStream;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.test.TestData;
import org.junit.Test;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;

/**
 * Purpose of this method is testing the ability of this plugin to write the complete set of files
 * for the GTOPO30 format in a single zip package.
 *
 * @author Simone Giannecchini
 */
public class GT30ZipWriterTest extends GT30TestBase {

    /** Testing zipped-package writing capabilites. */
    @Override
    @Test
    public void test() throws Exception {
        final URL statURL = TestData.getResource(this, this.fileName + ".DEM");
        final AbstractGridFormat format = new GTopo30FormatFactory().createFormat();

        final GTopo30WriteParams wp = new GTopo30WriteParams();
        wp.setCompressionMode(GTopo30WriteParams.MODE_EXPLICIT);
        wp.setCompressionType("ZIP");
        ParameterValueGroup params = format.getWriteParameters();
        params.parameter(AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString())
                .setValue(wp);

        if (format.accepts(statURL)) {
            // get a reader
            final GridCoverageReader reader = format.getReader(statURL);

            // get a grid coverage
            final GridCoverage2D gc = ((GridCoverage2D) reader.read(null));

            // preparing to write it down
            File testDir = TestData.file(this, "");
            newDir = new File(testDir.getAbsolutePath() + "/newDir");
            newDir.mkdir();

            final GridCoverageWriter writer = format.getWriter(newDir);
            writer.write(gc, params.values().toArray(new GeneralParameterValue[1]));

            gc.dispose(false);
        }
    }

    /** Testing zipped-package writing capabilites. */
    @Test
    public void testExternalZIP() throws Exception {
        final URL sourceURL = TestData.getResource(this, this.fileName + ".DEM");
        final AbstractGridFormat format = new GTopo30FormatFactory().createFormat();

        assertTrue(
                "Unable to parse source data for this GTOPO30 write test",
                format.accepts(sourceURL));

        // get a reader
        final GridCoverageReader reader = format.getReader(sourceURL);

        // get a grid coverage
        final GridCoverage2D gc = ((GridCoverage2D) reader.read(null));

        // preparing to write it down
        File testDir = TestData.file(this, ".");
        File outputFile = new File(testDir.getAbsolutePath(), "test.zip");

        try (OutputStream outputStream = new ZipOutputStream(new FileOutputStream(outputFile))) {
            GridCoverageWriter writer = null;
            try {
                writer = format.getWriter(outputStream);
                // ATTENTION we do not require to specify that we want compression as we are doing
                // that
                // on our own
                writer.write(gc, null);
            } finally {
                if (writer != null) {
                    writer.dispose();
                }
            }
        } catch (Exception e) {
            fail(e.getLocalizedMessage());
        } finally {
            // close source GC
            gc.dispose(false);

            // delete test file, or at least try to
            outputFile.delete();
        }
    }
}
