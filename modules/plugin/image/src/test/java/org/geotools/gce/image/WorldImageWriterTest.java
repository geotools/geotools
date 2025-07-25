/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.image;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.logging.Logger;
import org.geotools.api.coverage.grid.Format;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.test.TestData;
import org.geotools.util.logging.Logging;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for WorldImageWriter. This test tries to read, writer and re-read successive images checking for errors.
 *
 * @author Simone Giannecchini
 * @author rgould
 */
public class WorldImageWriterTest extends WorldImageBaseTestCase {
    private static final String[] supportedFormat = {"tiff", "gif", "png", "bmp", "jpeg"};

    private Logger logger = Logging.getLogger(WorldImageWriterTest.class);

    @Before
    public void setUp() throws Exception {
        File testData = TestData.file(this, ".");
        new File(testData, "write").mkdir();
    }
    /**
     * This method simply read all the respecting a predefined pattern inside the testData directory and then it tries
     * to read, write and re-read them back. All the possible errors are caught.
     */
    @Test
    public void testWrite()
            throws MalformedURLException, IOException, IllegalArgumentException, FactoryException, TransformException,
                    ParseException {

        // checking test data directory for all kind of inputs
        final File test_data_dir = TestData.file(this, null);
        File output;
        final String[] fileList = test_data_dir.list(new MyFileFilter());
        for (String format : supportedFormat) {
            final StringBuffer buff =
                    new StringBuffer("Format is ").append(format).append("\n");
            for (String filePath : fileList) {
                buff.append(" Testing ability to write ").append(filePath);
                // url
                final URL url = TestData.getResource(this, filePath);
                assertNotNull(url);
                output = this.write(url, format);
                buff.append(" as url ").append(filePath).append(output.getName());

                // getting file
                final File file = TestData.file(this, filePath);
                assertNotNull(file);
                // starting write test
                output = this.write(file, format);
                buff.append(" and file ").append(filePath).append(output.getName() + "\n");
            }
            logger.info(buff.toString());
        }
    }

    /**
     * This method is responsible for loading the provided source object as a cverage then for writing it on the temp
     * directoy and finally for rereading the coverage back into memory in order to display it.
     *
     * @param source Object The object on disk representing the coverage to test.
     */
    private File write(Object source, String format)
            throws IOException, IllegalArgumentException, FactoryException, TransformException, ParseException {
        // instantiating a reader
        WorldImageReader wiReader = new WorldImageReader(source);

        // reading the original coverage
        GridCoverage2D coverage = wiReader.read();

        assertNotNull(coverage);
        assertNotNull(coverage.getRenderedImage());
        assertNotNull(coverage.getEnvelope());

        // remember to provide a valid name, it wil be mde unique by the helper
        // function temp
        final StringBuffer buff = new StringBuffer("./write/temp").append(".").append(format);
        final File tempFile = TestData.temp(this, buff.toString());

        // getting a writer
        final WorldImageWriter wiWriter = new WorldImageWriter(tempFile);

        // writing parameters for png
        final Format writerFormat = wiWriter.getFormat();

        // setting write parameters
        final ParameterValueGroup params = writerFormat.getWriteParameters();
        params.parameter(WorldImageFormat.FORMAT.getName().toString()).setValue(format);
        final GeneralParameterValue[] gpv = {
            params.parameter(WorldImageFormat.FORMAT.getName().toString())
        };
        // writing
        wiWriter.write(coverage, gpv);
        wiWriter.dispose();

        // reading again
        assertTrue(tempFile.exists());
        wiReader = new WorldImageReader(tempFile);
        coverage = wiReader.read();

        // displaying the coverage
        if (TestData.isInteractiveTest()) coverage.show();
        else coverage.getRenderedImage().getData();
        wiReader.dispose();
        coverage.dispose(true);

        return tempFile;
    }
}
