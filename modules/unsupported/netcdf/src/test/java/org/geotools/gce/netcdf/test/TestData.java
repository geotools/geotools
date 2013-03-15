/* Copyright (c) 2001 - 2013 OpenPlans - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.gce.netcdf.test;

import java.io.File;

/**
 * class to hold test data path and file name information.
 *
 * set the bounds of the 'simulation' test files and test bounds based on running
 * NetCDFFileInspectorTest testGetBounds methods on the original files being simulated (currently
 * commented out).
 */
public class TestData {

    private static final String TEST_DATA_PATH_ROOT = "src" + File.separator
            + "test" + File.separator + "resources" + File.separator + "data"
            + File.separator;
    private static final String TEST_DATA_PATH_MISC = TEST_DATA_PATH_ROOT
            + "models" + File.separator + "misc" + File.separator;
    private static final String TEST_DATA_PATH_MISC_ANALYSIS_TIME = TEST_DATA_PATH_MISC
            + "analysisTime" + File.separator;
    private static final String TEST_DATA_PATH_MISC_NCMLAGG = TEST_DATA_PATH_MISC
            + "ncmlAgg" + File.separator;
    public static final String TEST_DEFAULT_CRS_CODE = "EPSG:4326";
    /**
     * test file for simple case.
     */
    public static final String TEST_DATA_POSITIVE_LONGITUDE = TEST_DATA_PATH_MISC
            + "lon2.0To4.5.nc";
    /**
     * test bounds of TEST_DATA_POSITIVE_LONGITUDE file.
     */
    public static final float[] TEST_BOUNDS_POSITIVE_LONGITUDE = new float[]{
        2.0f, 4.5f, 0.0f, 73.0f};
    /**
     * Test file has temp variable with lat dimension outside lon dimension.
     *
     * The data values for each combination are the same between this file and its sister file
     * modelLonOuterLat. This pair of files was created to confirm we get the correct results from
     * our range read independent of the input NetCDF data's lat-lon dim order.
     */
    public static final String TEST_DATA_LAT_OUTER_LON = TEST_DATA_PATH_MISC
            + "modelLatOuterLon.nc";
    /**
     * Test file with temp variable with lon dimension outside lat dimension.
     *
     * The data values for each combination are the same between this file and its sister file
     * modelLatOuterLon. This pair of files was created to confirm we get the correct results from
     * our range read independent of the input NetCDF data's lat-lon dim order.
     */
    public static final String TEST_DATA_LON_OUTER_LAT = TEST_DATA_PATH_MISC
            + "modelLonOuterLat.nc";
    /**
     * test bounds same as modelLatOuterLon.nc and modelLonOuterLat.nc files.
     */
    public static final float[] TEST_BOUNDS_FOR_LAT_OUTER_LON_AND_LON_OUTER_LAT = new float[]{
        -100.0f, -40.0f, 0.0f, 40.0f};
    /**
     * big file. for test, simulate with file with same bounds and less data. story/defect
     * #artf120060
     */
    public static final String TEST_DATA_NCOM_REG01 = TEST_DATA_PATH_ROOT
            + "ncom" + File.separator + "global" + File.separator + "ncom_glb_regp01_2012101200.nc";
    /**
     * big file. its data bounding box -150.0, -100.0, 10.0, 65.0. will not write separate unit
     * tests, test to simulate for reg01 will cover. story/defect #artf120060
     *
     */
    public static final String TEST_DATA_NCOM_REG07 = TEST_DATA_PATH_ROOT
            + "ncom" + File.separator + "global" + File.separator + "ncom-glb-reg07.nc";
    /**
     * big file. for test, simulate with file with same bounds and less data. story/defect
     * #artf119555
     */
    public static final String TEST_DATA_SWAN = TEST_DATA_PATH_ROOT
            + "swan" + File.separator + "SWAN-hawaii_nest1-2006102900.nc";
    /**
     * test file with same bounding box as ncom_glb_regp01_2012101200.nc.
     */
    public static final String TEST_DATA_NEGATIVE_LONGITUDE_SIMULATE_NCOMREG01 = TEST_DATA_PATH_MISC
            + "lon-100.0To-50.0.nc";
    /**
     * test bounds same as ncom_glb_regp01_2012101200.nc file.
     */
    public static final float[] TEST_BOUNDS_NEGATIVE_LONGITUDE_SIMULATE_NCOMREG01 = new float[]{
        -100.0f, -50.0f, 0.0f, 70.0f};
    /**
     * test file with same bounding box as SWAN-hawaii_nest1-2006102900.nc file.
     */
    public static final String TEST_DATA_NEGATIVE_LONGITUDE_SIMULATE_SWAN = TEST_DATA_PATH_MISC
            + "lon-162.0To-154.0.nc";
    /**
     * test bounds same as SWAN-hawaii_nest1-2006102900.nc file.
     */
    public static final float[] TEST_BOUNDS_NEGATIVE_LONGITUDE_SIMULATE_SWAN = new float[]{
        -162.0f, -154.0f, 18.0f, 22.999998f};
    /**
     * test file with tau with time_origin attribute that can be converted to a date.
     */
    public static final String TEST_DATA_ANALYSIS_TIME_HAS_TAU_WITH_VALID_TIME_ORIGIN = TEST_DATA_PATH_MISC_ANALYSIS_TIME
            + "hasTauWithValidTimeOrigin.nc";
    /**
     * value of the tau variable's time_origin attribute of
     * TEST_DATA_ANALYSIS_TIME_HAS_TAU_WITH_VALID_TIME_ORIGIN file.
     */
    public static final String TEST_TIME_ORIGIN_HAS_TAU_WITH_VALID_TIME_ORIGIN = "2000-01-03 00:00:00";
    /**
     * test file with tau with time_origin attribute that can not be converted to a date.
     */
    public static final String TEST_DATA_ANALYSIS_TIME_HAS_TAU_WITH_INVALID_TIME_ORIGIN = TEST_DATA_PATH_MISC_ANALYSIS_TIME
            + "hasTauWithInvalidTimeOrigin.nc";
    /**
     * test file with tau with no time_origin attribute.
     */
    public static final String TEST_DATA_ANALYSIS_TIME_HAS_TAU_WITH_NO_TIME_ORIGIN = TEST_DATA_PATH_MISC_ANALYSIS_TIME
            + "hasTauWithNoTimeOrigin.nc";
    /**
     * test file with no tau.
     */
    public static final String TEST_DATA_ANALYSIS_TIME_HAS_NO_TAU = TEST_DATA_PATH_MISC_ANALYSIS_TIME
            + "hasNoTau.nc";
    /**
     * value of the global time_origin attribute of TEST_DATA_ANALYSIS_TIME... files with a global
     * time origin attribute. TODO change this date and redo the files to match so that this date
     * different than the date in the tau var.
     */
    public static final String TEST_GLOBAL_TIME_ORIGIN = "2000-01-03 00:00:00";
    public static final String TEST_DATA_AGG_MODEL_RUN_1 = TEST_DATA_PATH_MISC_NCMLAGG
            + "modelRun1agg.nc";
    public static final String TEST_DATA_AGG_MODEL_RUN_2 = TEST_DATA_PATH_MISC_NCMLAGG
            + "modelRun2agg.nc";
    public static final String TEST_DATA_NCML_SINGLE_RUN_PER_FILE = TEST_DATA_PATH_MISC_NCMLAGG
            + "modelRunsSingleRunPerFile.ncml";
    public static final String TEST_DATA_NCML_SINGLE_TAU_PER_FILE = TEST_DATA_PATH_MISC_NCMLAGG
            + "modelRunsSingleTauPerFile.ncml";
    /**
     * test bounds of TEST_DATA_NCML_SINGLE_RUN_PER_FILE and TEST_DATA_NCML_SINGLE_TAU_PER_FILE
     * files.
     */
    public static final float[] TEST_BOUNDS_NCML_FILES = new float[]{2.0f,
        4.5f, 0.0f, 70.0f};
    /**
     * runtime value for run1 in ncml aggregation test files.
     */
    public static final String TEST_RUNTIME1_AGGREGATION = "2000-01-03T00:00:00.000Z";
    /**
     * runtime value for run2 in ncml aggregation test files.
     */
    public static final String TEST_RUNTIME2_AGGREGATION = "2000-01-04T00:00:00.000Z";

    /*
     * @return the path to the WAM globala NetCDF
     */
    public static String getWam() {
        StringBuilder filename = getTestDataDirBuilder();

        filename.append("wam").append(File.separator);
        filename.append("WAM-globala.nc");

        return filename.toString();
    }

    /*
     * @return the path to the SWAN Santamon NetCDF
     */
    public static String getSwanSantamon() {
        StringBuilder filename = getTestDataDirBuilder();

        filename.append("swan").append(File.separator);
        filename.append("SWAN-santamon.nc");

        return filename.toString();
    }

    /*
     * @return the path to the SWAN Socal NetCDF
     */
    public static String getSwanSocal() {
        StringBuilder filename = getTestDataDirBuilder();

        filename.append("swan").append(File.separator);
        filename.append("SWAN-socal.nc");

        return filename.toString();
    }

    /*
     * @return the path to the PCTIDES Ches_basin NetCDF
     */
    public static String getPctidesChesBasin() {
        StringBuilder filename = getTestDataDirBuilder();

        filename.append("pctides").append(File.separator);
        filename.append("pctides-Ches_basin.nc");

        return filename.toString();
    }

    /*
     * @return the path to the PCTIDES SAN_DIEGO NetCDF
     */
    public static String getPctidesSanDiego() {
        StringBuilder filename = getTestDataDirBuilder();

        filename.append("pctides").append(File.separator);
        filename.append("pctides-SAN_DIEGO.nc");

        return filename.toString();
    }

    /*
     * Get the path for the test NetCDFs. Follows the Maven convention. Uses the
     * system file separator for better OS independence. Might be more
     * performant in a static block rather than a static method, but it's a
     * test.
     * 
     * <root>/src/test/resources/data/models/
     * 
     * @return StringBuilder
     */
    public static StringBuilder getTestDataDirBuilder() {
        StringBuilder filename = new StringBuilder();

        filename.append("src").append(File.separator);
        filename.append("test").append(File.separator);
        filename.append("resources").append(File.separator);
        filename.append("data").append(File.separator);
        filename.append("models").append(File.separator);

        return filename;
    }

    /*
     * Get a String version of the path for the test NetCDFs.
     */
    public static String getTestDataDir() {
        return getTestDataDirBuilder().toString();
    }
}
