/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagepyramid;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.media.jai.PlanarImage;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.ImageMosaicFormat;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * Testing {@link ImagePyramidReader}.
 * 
 * @author Simone Giannecchini
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Test coverage for pyramids stored in JARs and referenced by URLs
 * @since 2.3
 * 
 *
 * @source $URL$
 */
public class ImagePyramidReaderTest extends Assert {

	/**
	 * File to be used for testing purposes.
	 */
	private final static String TEST_FILE = "pyramid.properties";
	private final static String TEST_JAR_FILE = "pyramid.jar";
	
	  
    /**
     * Tests automatic building of all the mosaic and pyramid files
     * 
     * @throws IOException
     */
    @Test
    public void testAutomaticBuild() throws IOException {
        final URL testFile = TestData.getResource(this, TEST_FILE);
        File sourceDir = DataUtilities.urlToFile(testFile).getParentFile();
        File targetDir = File.createTempFile("pyramid", "tst", new File("./target"));
        targetDir.delete();
        targetDir.mkdir();
        try {
            prepareEmptyMosaic(sourceDir, targetDir);

            // now make sure we can actually rebuild the mosaic
            final AbstractGridFormat format = new ImagePyramidFormat();
            assertTrue(format.accepts(targetDir));
            final ImagePyramidReader reader = (ImagePyramidReader) format.getReader(targetDir);
            assertNotNull(reader);
            reader.dispose();
        } finally {
            // cleanup
            try{
            	FileUtils.deleteDirectory(targetDir);
            }
            catch (Throwable e) {
			}
        }
    }

    /**
     * Tests automatic building of all the mosaic and pyramid files from a gdal_retile like
     * directory structure
     * 
     * @throws IOException
     */
    @Test
    public void testAutomaticBuildGdalRetile() throws IOException {
        final URL testFile = TestData.getResource(this, TEST_FILE);
        File sourceDir = DataUtilities.urlToFile(testFile).getParentFile();
        File targetDir = File.createTempFile("pyramid", "tst", new File("./target"));
        targetDir.delete();
        targetDir.mkdir();
        try {
            prepareEmptyMosaic(sourceDir, targetDir);

            // move the files so that it looks like a gdal_retile created directory
            File zeroDir = new File(targetDir, "0");
            assertTrue(zeroDir.isDirectory());
            FileUtils.copyDirectory(zeroDir, targetDir);
            FileUtils.deleteDirectory(zeroDir);

            // now make sure we can actually rebuild the mosaic
            final AbstractGridFormat format = new ImagePyramidFormat();
            assertTrue(format.accepts(targetDir));
            final ImagePyramidReader reader = (ImagePyramidReader) format.getReader(targetDir);
            assertNotNull(reader);
        } finally {
            // cleanup
            try{
            	FileUtils.deleteDirectory(targetDir);
            }
            catch (Throwable e) {
			}
        }
    }

   /**
    * Copies the mosaic from the source dir to the target dir and removes all
    * metadata files from it
    * @param sourceDir
    * @param targetDir
    * @throws IOException
    */
    void prepareEmptyMosaic(File sourceDir, File targetDir) throws IOException {
        FileUtils.copyDirectory(sourceDir, targetDir);
        
        // remove the files we want to recreate
        File[] dirs = new File[] {targetDir, new File(targetDir, "0"),
                new File(targetDir, "2"), new File(targetDir, "4"), new File(targetDir, "8")};
        FileFilter metadataFilter = FileFilterUtils.prefixFileFilter("pyramid.");
        for (File dir : dirs) {
            for (File file : dir.listFiles(metadataFilter)) {
                file.delete();
            }
        }
    }
}
