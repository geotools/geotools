/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.complex.AppSchemaDataAccess;
import org.geotools.data.complex.AppSchemaDataAccessRegistry;
import org.geotools.data.complex.FeatureTypeMapping;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

/**
 * This is a test for AppSchemaDataAccessConfigurator on file-based datastores, including shapefiles
 * and *.properties files.
 * 
 * @author Tara Athan
 * @author Ben Caradoc-Davies, CSIRO Earth Science and Resource Engineering
 *
 * @source $URL$
 */
public class AppSchemaFileDataTest extends TestCase {

    /**
     * The resource path containing the source data for testing.
     */
    private static final String testData = "/test-data/";

    /**
     * The temporary directory where tests are run.
     */
    private static final File testDir = new File("target/test/"
            + AppSchemaFileDataTest.class.getSimpleName());

    /**
     * Create the test directory.
     * 
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws IOException {
        FileUtils.deleteDirectory(testDir);
        // copy all the test data into the test directory
        copyTestData("ArchSite.xsd", testDir);
        copyTestData("ArchSiteNillable.xml", testDir);
        copyTestData("AppSchemaFileDataTest.xml", testDir);
        copyTestData("PointFeatureGeomPropertyfile.properties", new File(testDir, "directory"));
        // this allows type names to be reused between tests
        AppSchemaDataAccessRegistry.unregisterAll();
    }

    /**
     * Remove the test directory.
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(testDir);
        AppSchemaDataAccessRegistry.unregisterAll();
    }

    /**
     * Copy a file from the test-data directory to another directory.
     * 
     * @param baseFileName
     *            base filename (without any path) of the file to be copied
     * @param destDir
     *            destination filename
     * @throws IOException
     */
    private static void copyTestData(String baseFileName, File destDir) throws IOException {
        destDir.mkdirs();
        FileUtils.copyFileToDirectory(
                DataUtilities.urlToFile(AppSchemaFileDataTest.class.getResource(testData
                        + baseFileName)), destDir);
    }

    /**
     * Return the path to a file in the test directory.
     * 
     * @param baseFileName
     *            file name without any path
     * @return
     */
    private static String getTestDirPath(String baseFileName) {
        return (new File(testDir, baseFileName)).getPath();
    }

    /**
     * Test the AppSchemaDataAccessConfigurator.buildMappings method with shapefiles, using a
     * relative paths to the data
     */
    public void testShapeMappings() throws Exception {
        String mappingFileNameRelativeShape = "ArchSiteNillable.xml";
        AppSchemaDataAccess dSRelative = null;
        try {
            // create the DataAccess based on shapefile configured with a relative path
            dSRelative = buildDataAccess(mappingFileNameRelativeShape);

            // there should be a single target feature in this data access
            assertEquals(1, dSRelative.getNames().size());

            // there should be 25 features in this data access
            assertEquals(25, countFeatures(dSRelative));
        } finally {
            if (dSRelative != null) {
                dSRelative.dispose();
            }
        }
    }

    /**
     * Test the AppSchemaDataAccessConfigurator.buildMappings method with shapefiles, using an
     * absolute path to the data.
     */
    public void testShapeMappingsAbsolute() throws Exception {
        String mappingFileNameRelativeShape = "ArchSiteNillable.xml";
        String configFilePathRelativeShape = getTestDirPath(mappingFileNameRelativeShape);
        String mappingFileNameAbsoluteShape = "ArchSiteAbsolute.xml";
        String configFilePathAbsoluteShape = getTestDirPath(mappingFileNameAbsoluteShape);
        AppSchemaDataAccess dSAbsolute = null;
        try {
            // now lets test a mapping file with an absolute path to the shapefile
            // because we don't know the absolute path in advance, we must create the mapping file
            copyRelativeToAbsolute(configFilePathRelativeShape, configFilePathAbsoluteShape);

            // create the DataAccess based on shapefile configured with a absolute path
            dSAbsolute = buildDataAccess(mappingFileNameAbsoluteShape);

            // there should be a single target feature in this data access
            assertEquals(1, dSAbsolute.getNames().size());

            // there should be 25 features in this data access
            assertEquals(25, countFeatures(dSAbsolute));
        } finally {
            if (dSAbsolute != null) {
                dSAbsolute.dispose();
            }
        }
    }

    /**
     * Test the AppSchemaDataAccessConfigurator.buildMappings method with *.properties files using a
     * relative and paths to the data just to be sure we didn't break these while we were playing
     * around with shapefiles.
     */
    public void testPropertiesMappings() throws Exception {
        String mappingFileNameRelativeProperties = "AppSchemaFileDataTest.xml";
        AppSchemaDataAccess dSRelative = null;
        try {
            // create the DataAccess based on properties file configured with a relative path
            dSRelative = buildDataAccess(mappingFileNameRelativeProperties);

            // there should be a single target feature in this data access
            assertEquals(1, dSRelative.getNames().size());

            // there should be 2 features in this data access
            assertEquals(2, countFeatures(dSRelative));
        } finally {
            if (dSRelative != null) {
                dSRelative.dispose();
            }
        }
    }

    /**
     * Test the AppSchemaDataAccessConfigurator.buildMappings method with *.properties files using
     * an absolute path to the data just to be sure we didn't break these while we were playing
     * around with shapefiles.
     */
    public void testPropertiesMappingsAbsolute() throws Exception {
        String mappingFileNameRelativeProperties = "AppSchemaFileDataTest.xml";
        String configFilePathRelativeProperties = getTestDirPath(mappingFileNameRelativeProperties);
        String mappingFileNameAbsoluteProperties = "AppSchemaFileDataTestAbsolute.xml";
        String configFilePathAbsoluteProperties = getTestDirPath(mappingFileNameAbsoluteProperties);
        AppSchemaDataAccess dSAbsolute = null;
        try {
            // now let's test a mapping file with an absolute path to the properties file
            // because we don't know the absolute path in advance, we must create the mapping file
            copyRelativeToAbsolute(configFilePathRelativeProperties,
                    configFilePathAbsoluteProperties);

            // create the DataAccess based on properties file configured with a absolute path
            dSAbsolute = buildDataAccess(mappingFileNameAbsoluteProperties);

            // there should be a single target feature in this data access
            assertEquals(1, dSAbsolute.getNames().size());

            // there should be 2 features in this data access
            assertEquals(2, countFeatures(dSAbsolute));
        } finally {
            if (dSAbsolute != null) {
                dSAbsolute.dispose();
            }
        }
    }

    private int countFeatures(AppSchemaDataAccess dS) throws Exception {
        // we need the feature type in order to query for the number of features
        Name[] dSNameArray = dS.getTypeNames();
        String testType = dSNameArray[0].toString();
        FeatureSource<FeatureType, Feature> featureSource = dS
                .getFeatureSourceByName(dSNameArray[0]);
        int numFeatures = featureSource.getCount(new Query(testType));

        return numFeatures;
    }

    private AppSchemaDataAccess buildDataAccess(String mappingsFileName) throws Exception {
        // generate the path to a mappings-file in the resources directory
        String configFilePath = extendFilename(testDir.getPath(), mappingsFileName);
        URL configFileUrl = DataUtilities.fileToURL(new File(configFilePath));

        // parse the mappings-file
        XMLConfigDigester configReader = new XMLConfigDigester();
        AppSchemaDataAccessDTO config = configReader.parse(configFileUrl);

        // generate the set of mappings needed to build the application-schema datastore
        Set<FeatureTypeMapping> mappings;
        mappings = AppSchemaDataAccessConfigurator.buildMappings(config);
        AppSchemaDataAccess datastore = new AppSchemaDataAccess(mappings);

        return datastore;
    }

    /**
     * copies a mapping file and changes some parameters to test absolute paths to data files
     * 
     * @param filePathIn
     *            the original mapping file
     * @param filePathOut
     *            the new mapping file
     * @return
     * @throws Exception
     */
    private String copyRelativeToAbsolute(String filePathIn, String filePathOut) throws Exception {

        BufferedReader reader = null;
        PrintWriter writer = null;
        String relativePath;
        String absolutePath = null;

        try {
            reader = new BufferedReader(new FileReader(filePathIn));
            writer = new PrintWriter(new FileWriter(filePathOut));
            String line;

            while ((line = reader.readLine()) != null) {
                // change the file path from relative to absolute
                if (line.trim().startsWith("<value>file:")) {
                    relativePath = line.split("<value>file:|</value>")[1];
                    String resolvedPath = extendFilename(testDir.getPath(), relativePath);
                    absolutePath = (new File(resolvedPath)).getAbsolutePath();
                    line = line.replace(relativePath, absolutePath);
                }
                // in shapefile test, chosen target feature doesn't allow null entry, so we'll take
                // the conditional out
                if (line.trim().startsWith("<OCQL>")) {
                    line = line.replace("if_then_else(equalTo(CAT_DESC, 'No Name'), "
                            + "Expression.Nil , CAT_DESC)", "CAT_DESC");
                }
                writer.println(line);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
        return absolutePath;
    }

    /**
     * extends a base filename with a path relative to the base filename
     * 
     * @param baseFilename
     *            may be absolute, or relative to the current directory
     * @param relativePath
     *            must be relative to the base file name
     * @return an absolute, normalized filename
     * @throws RuntimeException
     *             if the base filename and relative path are incompatible
     */
    private String extendFilename(String baseFilename, String relativePath) throws RuntimeException {
        baseFilename = new File(baseFilename).getAbsolutePath();
        String extendedFilename = FilenameUtils.concat(baseFilename, relativePath);

        if (extendedFilename == null) {
            throw new RuntimeException("Relative path to datastore is incompatible with the "
                    + "base path - check double dot steps.");
        }
        return extendedFilename;
    }

}
