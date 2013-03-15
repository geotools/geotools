/* Copyright (c) 2001 - 2013 OpenPlans - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.gce.netcdf.test;

import java.io.File;

/**
 * class to hold test data path and file name information for netCdf speed
 * tests.
 * 
 * This is different than the TestData class. We did not commit the data for
 * these tests. The user should expect to have to configure the paths in this
 * file to get the netCdf speed tests to run. At a minimum you will need to set
 * the TEST_DATA_SPEED_PATH_ROOT to your GeoServer model data dir and the rest
 * of the paths should follow. If you do not have a GeoServer model data dir,
 * you can arrange the sample data to match the arrangement below or you can
 * revise the arrangement below to match your data.
 */
public class TestDataForSpeedTests {
    // set this variable to match your GeoServer model data dir.
    private static final String TEST_DATA_SPEED_PATH_ROOT = "C://GeoServerModelData"
	    + File.separator;

    // NCOM Coastal files - Mayport.
    // lon min 278.3 max 281.1485 shape 634
    // lat min 29.8 max 31.824999 shape 451
    private static final String TEST_DATA_SPEED_PATH_COASTAL = TEST_DATA_SPEED_PATH_ROOT
	    + "ncom" + File.separator + "coastal" + File.separator;
    public static final String TEST_DATA_SPEED_COASTAL_TAU_000_NC = TEST_DATA_SPEED_PATH_COASTAL
	    + "ncom-relo-mayport_u_miw-t000.nc";
    public static final String TEST_DATA_SPEED_COASTAL_TAU_048_NC = TEST_DATA_SPEED_PATH_COASTAL
	    + "ncom-relo-mayport_u_miw-t048.nc";
    public static final String TEST_DATA_SPEED_COASTAL_SINGLE_FILE_NO_AGG_NCML = TEST_DATA_SPEED_PATH_COASTAL
	    + "ncomCoastalTauPerFileSingleFileNoAgg.ncml";
    public static final String TEST_DATA_SPEED_COASTAL_SINGLE_FILE_AGG_NCML = TEST_DATA_SPEED_PATH_COASTAL
	    + "ncomCoastalTauPerFileSingleFileAgg.ncml";
    public static final String TEST_DATA_SPEED_COASTAL_MANY_FILE_AGG_NCML = TEST_DATA_SPEED_PATH_COASTAL
	    + "ncomCoastalTauPerFileManyFileAgg.ncml";
    // min lon, max lon, min lat, max lat
    public static final float[] TEST_BOUNDS_COASTAL = new float[] { 278.3f,
	    281.1485f, 29.8f, 31.824999f };
    // water temp shape - time, depth, lat, lon
    public static final int[] TEST_SHAPE_COASTAL_WATER_TEMP = new int[] { 1,
	    33, 451, 634 };

    // NCOM global
    private static final String TEST_DATA_SPEED_PATH_GLOBAL = TEST_DATA_SPEED_PATH_ROOT
	    + "ncom" + File.separator + "global" + File.separator;

    // NCOM global reg01
    public static final String TEST_DATA_SPEED_GLOBAL_REG01_NC = TEST_DATA_SPEED_PATH_GLOBAL
	    + "ncom-glb-reg01.nc";
    // min lon, max lon, min lat, max lat
    public static final float[] TEST_BOUNDS_GLOBAL_REG01 = new float[] {
	    -100.0f, -50.0f, 0.0f, 70.0f };
    // water temp shape - time, depth, lat, lon
    public static final int[] TEST_SHAPE_GLOBAL_REG01_WATER_TEMP = new int[] {
	    25, 40, 561, 401 };

    // NCOM global reg07
    public static final String TEST_DATA_SPEED_GLOBAL_REG07_NC = TEST_DATA_SPEED_PATH_GLOBAL
	    + "ncom-glb-reg07.nc";
    // min lon, max lon, min lat, max lat
    public static final float[] TEST_BOUNDS_GLOBAL_REG07 = new float[] {
	    -150.0f, -100.0f, 10.0f, 65.0f };
    // water temp shape - time, depth, lat, lon
    public static final int[] TEST_SHAPE_GLOBAL_REG07_WATER_TEMP = new int[] {
	    25, 40, 441, 401 };

    // NCOM regional
    private static final String TEST_DATA_SPEED_PATH_REGIONAL_HAWAII = TEST_DATA_SPEED_PATH_ROOT
	    + "ncom"
	    + File.separator
	    + "regional"
	    + File.separator
	    + "hawaii"
	    + File.separator;
    public static final String TEST_DATA_SPEED_REGIONAL_HAWAII_TAU_000_NC = TEST_DATA_SPEED_PATH_REGIONAL_HAWAII
	    + "ncom-relo-hawaii_u-t000.nc";
    public static final String TEST_DATA_SPEED_REGIONAL_HAWAII_TAU_096_NC = TEST_DATA_SPEED_PATH_REGIONAL_HAWAII
	    + "ncom-relo-hawaii_u-t096.nc";
    // min lon, max lon, min lat, max lat
    public static final float[] TEST_BOUNDS_REGIONAL_HAWAII = new float[] {
	    194.0f, 208.07264709472656f, 15.0f, 29.09401512145996f };
    // water temp shape - time, depth, lat, lon
    public static final int[] TEST_SHAPE_REGIONAL_HAWAII_WATER_TEMP = new int[] {
	    1, 40, 523, 484 };

}
