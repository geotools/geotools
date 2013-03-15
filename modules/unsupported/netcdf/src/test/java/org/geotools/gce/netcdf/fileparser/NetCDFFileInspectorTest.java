/* Copyright (c) 2001 - 2013 OpenPlans - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.gce.netcdf.fileparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Rectangle;
import java.io.File;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

import org.geotools.gce.netcdf.GrdDataEncapsulator;
import org.geotools.gce.netcdf.NetCdfUtil;
import org.geotools.gce.netcdf.ParamInformation;
import org.geotools.gce.netcdf.test.TestData;
import org.geotools.gce.netcdf.test.TestUtil;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import ucar.nc2.NetcdfFile;
import ucar.nc2.dataset.NetcdfDataset;

/**
 * unit tests for named class.
 */
public class NetCDFFileInspectorTest {

    NetCDFFileInspector classUnderTest;
    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        classUnderTest = null;
    }

    @Test
    public void testGetBounds_PositiveLongitude() {
        File testFile = new File(TestData.TEST_DATA_POSITIVE_LONGITUDE);
        testGetBounds(testFile, TestData.TEST_BOUNDS_POSITIVE_LONGITUDE);
    }

    // big input file, simulate with smaller file.
    @Ignore
    @Test
    public void testGetBounds_NegativeLongitudeNcom() {
        File testFile = new File(TestData.TEST_DATA_NCOM_REG01);
        testGetBounds(testFile, null);
    }

    @Test
    public void testGetBounds_NegativeLongitudeSimulateNcomReg01() {
        File testFile = new File(
                TestData.TEST_DATA_NEGATIVE_LONGITUDE_SIMULATE_NCOMREG01);
        testGetBounds(testFile,
                TestData.TEST_BOUNDS_NEGATIVE_LONGITUDE_SIMULATE_NCOMREG01);
    }

    // big input file, and Reg01 test is enough for this story/defect.
    @Ignore
    @Test
    public void testGetBounds_NegativeLongitudeNcomReg07() {
        File testFile = new File(TestData.TEST_DATA_NCOM_REG07);
        testGetBounds(testFile, null);
    }

    // big input file, simulate with smaller file.
    @Ignore
    @Test
    public void testGetBounds_NegativeLongitudeSwan() {
        File testFile = new File(TestData.TEST_DATA_SWAN);
        testGetBounds(testFile, null);
    }

    @Test
    public void testGetBounds_NegativeLongitudeSimulateSwan() {
        File testFile = new File(
                TestData.TEST_DATA_NEGATIVE_LONGITUDE_SIMULATE_SWAN);
        testGetBounds(testFile,
                TestData.TEST_BOUNDS_NEGATIVE_LONGITUDE_SIMULATE_SWAN);
    }

    @Test
    public void testGetBounds_DatasetFromNcmlSingleRunPerFile() {
        File testFile = new File(TestData.TEST_DATA_NCML_SINGLE_RUN_PER_FILE);
        testGetBounds(testFile, TestData.TEST_BOUNDS_NCML_FILES);
    }

    @Test
    public void testGetBounds_DatasetFromNcmlSingleTauPerFile() {
        File testFile = new File(TestData.TEST_DATA_NCML_SINGLE_TAU_PER_FILE);
        testGetBounds(testFile, TestData.TEST_BOUNDS_NCML_FILES);
    }

    // TODO testGetAnalysisTimeString
    // add new test cases now that we check for runtime
    // var and for global time_origin attribute.
    /**
     * tau variable and its time_origin found, attribute is convertible to date.
     */
    @Test
    public void testGetAnalysisTimeString_HasTauWithValidTimeOrigin() {
        File testFile = new File(
                TestData.TEST_DATA_ANALYSIS_TIME_HAS_TAU_WITH_VALID_TIME_ORIGIN);
        if (null == testFile || !testFile.exists()) {
            return;
        }
        classUnderTest = new NetCDFFileInspector(testFile);
        String result = classUnderTest.getAnalysisTimeString();
        assertEquals(
                "Unexpected analysis time.",
                convertNetCdfDateStringToOutputDateString(TestData.TEST_TIME_ORIGIN_HAS_TAU_WITH_VALID_TIME_ORIGIN),
                result);
    }

    /**
     * tau variable and its time_origin found, attribute is not convertible to date. will read date
     * from global attribute.
     */
    @Test
    public void testGetAnalysisTimeString_HasTauWithInvalidTimeOrigin_HasValidGlobalTimeOrigin() {
        File testFile = new File(
                TestData.TEST_DATA_ANALYSIS_TIME_HAS_TAU_WITH_INVALID_TIME_ORIGIN);
        if (null == testFile || !testFile.exists()) {
            return;
        }
        classUnderTest = new NetCDFFileInspector(testFile);
        String result = classUnderTest.getAnalysisTimeString();

        assertEquals(
                "Unexpected analysis time.",
                convertNetCdfDateStringToOutputDateString(TestData.TEST_GLOBAL_TIME_ORIGIN),
                result);
    }

    /**
     * tau variable found, no time_origin attribute found.
     */
    @Test
    public void testGetAnalysisTimeString_HasTauWithNoTimeOrigin() {
        File testFile = new File(
                TestData.TEST_DATA_ANALYSIS_TIME_HAS_TAU_WITH_NO_TIME_ORIGIN);
        if (null == testFile || !testFile.exists()) {
            return;
        }
        classUnderTest = new NetCDFFileInspector(testFile);
        String result = classUnderTest.getAnalysisTimeString();
        assertEquals(
                "Unexpected analysis time.",
                convertNetCdfDateStringToOutputDateString(TestData.TEST_GLOBAL_TIME_ORIGIN),
                result);
    }

    /**
     * tau variable not found.
     */
    @Test
    public void testGetAnalysisTimeString_HasNoTau_HasValidGlobalTimeOrigin() {
        File testFile = new File(TestData.TEST_DATA_ANALYSIS_TIME_HAS_NO_TAU);
        if (null == testFile || !testFile.exists()) {
            return;
        }
        classUnderTest = new NetCDFFileInspector(testFile);
        String result = classUnderTest.getAnalysisTimeString();
        assertEquals(
                "Unexpected analysis time.",
                convertNetCdfDateStringToOutputDateString(TestData.TEST_GLOBAL_TIME_ORIGIN),
                result);
    }

    // TODO rework all these now that have re-arranged methods in the class
    // under test.
    // // TODO testFileHasMatchingAnalysisTime
    // // add new test cases now that we check for runtime
    // // var and for global time_origin attribute.
    //
    // /**
    // * tau variable and its time_origin found, attribute is convertible to
    // date.
    // * attribute value matches the target analysis time.
    // */
    // @Test
    // public void
    // testFileHasMatchingAnalysisTime_HasTauWithValidTimeOrigin_MatchesTarget()
    // {
    // NetcdfFile testNcFile = TestUtil
    // .getTestNetCdfFile(TestData.TEST_DATA_ANALYSIS_TIME_HAS_TAU_WITH_VALID_TIME_ORIGIN);
    // Date testDate = TestUtil
    // .getTestDate(TestData.TEST_TIME_ORIGIN_HAS_TAU_WITH_VALID_TIME_ORIGIN);
    //
    // classUnderTest = new NetCDFFileInspector();
    // boolean result = classUnderTest.fileHasMatchingAnalysisTime(testNcFile,
    // testDate, NetCdfUtil.NOT_FOUND);
    // assertTrue("Expected analysis time match.", result);
    // }
    //
    // /**
    // * tau variable and its time_origin found, attribute is convertible to
    // date.
    // * attribute value does not match the target analysis time.
    // */
    // @Test
    // public void
    // testFileHasMatchingAnalysisTime_HasTauWithValidTimeOrigin_DoesNotMatchTarget()
    // {
    // NetcdfFile testNcFile = TestUtil
    // .getTestNetCdfFile(TestData.TEST_DATA_ANALYSIS_TIME_HAS_TAU_WITH_VALID_TIME_ORIGIN);
    // Date testDate = new Date();
    //
    // classUnderTest = new NetCDFFileInspector();
    // boolean result = classUnderTest.fileHasMatchingAnalysisTime(testNcFile,
    // testDate, NetCdfUtil.NOT_FOUND);
    // assertFalse("Unexpected analysis time match.", result);
    // }
    //
    // /**
    // * tau variable and its time_origin found, attribute is not convertible to
    // * date. attribute value does not match the target analysis time.
    // */
    // @Test
    // public void
    // testFileHasMatchingAnalysisTime_HasTauWithInvalidTimeOrigin_CannotMatchTarget()
    // {
    // NetcdfFile testNcFile = TestUtil
    // .getTestNetCdfFile(TestData.TEST_DATA_ANALYSIS_TIME_HAS_TAU_WITH_INVALID_TIME_ORIGIN);
    // Date testDate = new Date();
    //
    // classUnderTest = new NetCDFFileInspector();
    // boolean result = classUnderTest.fileHasMatchingAnalysisTime(testNcFile,
    // testDate, NetCdfUtil.NOT_FOUND);
    // assertFalse("Unexpected analysis time match.", result);
    // }
    //
    // /**
    // * tau variable found, no time_origin attribute found.
    // */
    // @Test
    // public void
    // testFileHasMatchingAnalysisTime_HasTauWithNoTimeOrigin_CannotMatchTarget()
    // {
    // NetcdfFile testNcFile = TestUtil
    // .getTestNetCdfFile(TestData.TEST_DATA_ANALYSIS_TIME_HAS_TAU_WITH_NO_TIME_ORIGIN);
    // Date testDate = new Date();
    //
    // classUnderTest = new NetCDFFileInspector();
    // boolean result = classUnderTest.fileHasMatchingAnalysisTime(testNcFile,
    // testDate, NetCdfUtil.NOT_FOUND);
    // assertFalse("Unexpected analysis time match.", result);
    // }
    //
    // /**
    // * tau variable not found.
    // */
    // @Test
    // public void testFileHasMatchingAnalysisTime_HasNoTau_CannotMatchTarget()
    // {
    // NetcdfFile testNcFile = TestUtil
    // .getTestNetCdfFile(TestData.TEST_DATA_ANALYSIS_TIME_HAS_NO_TAU);
    // Date testDate = new Date();
    //
    // classUnderTest = new NetCDFFileInspector();
    // boolean result = classUnderTest.fileHasMatchingAnalysisTime(testNcFile,
    // testDate, NetCdfUtil.NOT_FOUND);
    // assertFalse("Unexpected analysis time match.", result);
    // }
    /**
     * test that we can open and access NetcdfFile object created from both nc and ncml files with
     * the same file opening code.
     *
     * TODO: evaluate whether we want to change our code to create NetcdfDataset objects instead of
     * NetcdfFile objects throughout our code.
     */
    @Test
    public void testOpenAndAccessNetcdfFileObjectFromNcFileAndNcmlFile() {
        NetcdfFile testDataset = null;

        File testFile = new File(TestData.TEST_DATA_AGG_MODEL_RUN_1);
        if (null == testFile || !testFile.exists()) {
            return;
        }

        // nc file.
        testDataset = TestUtil
                .getTestNetCdfFileFromDataset(TestData.TEST_DATA_AGG_MODEL_RUN_1);

        classUnderTest = new NetCDFFileInspector();
        int ncResult = classUnderTest.getElevationIndexInNCFile(testDataset, 50.0);
        assertEquals("Unexpected analysis time match.", 1, ncResult);

        // ncml file.
        testFile = new File(TestData.TEST_DATA_NCML_SINGLE_RUN_PER_FILE);
        if (null == testFile || !testFile.exists()) {
            return;
        }

        testDataset = TestUtil
                .getTestNetCdfFileFromDataset(TestData.TEST_DATA_NCML_SINGLE_RUN_PER_FILE);

        classUnderTest = new NetCDFFileInspector();
        int ncmlResult = classUnderTest.getElevationIndexInNCFile(testDataset,
                50.0);
        assertEquals("Unexpected analysis time match.", 1, ncmlResult);
    }

    /**
     * test that we can open and access NetcdfDataset object created from both nc and ncml files
     * with the same dataset opening code.
     */
    @Test
    public void testOpenAndAccessNetcdfDatasetObjectsFromNcFileAndNcmlFile() {
        NetcdfDataset testDataset = null;

        // nc file.
        File testFile = new File(TestData.TEST_DATA_AGG_MODEL_RUN_1);
        if (null == testFile || !testFile.exists()) {
            return;
        }
        testDataset = TestUtil
                .getTestNetcdfDatasetFromDataset(TestData.TEST_DATA_AGG_MODEL_RUN_1);

        classUnderTest = new NetCDFFileInspector();
        int ncResult = classUnderTest.getElevationIndexInNCFile(testDataset,
                50.0);
        assertEquals("Unexpected analysis time match.", 1, ncResult);

        // ncml file.
        testFile = new File(TestData.TEST_DATA_NCML_SINGLE_RUN_PER_FILE);
        if (null == testFile || !testFile.exists()) {
            return;
        }
        testDataset = TestUtil
                .getTestNetcdfDatasetFromDataset(TestData.TEST_DATA_NCML_SINGLE_RUN_PER_FILE);

        classUnderTest = new NetCDFFileInspector();
        int ncmlResult = classUnderTest.getElevationIndexInNCFile(testDataset,
                50.0);
        assertEquals("Unexpected analysis time match.", 1, ncmlResult);
    }

    /**
     * test for ncml file doing aggregation of nc files with single run and multiple taus per file.
     */
    @Test
    public void testGetAnalysisTimeString_ForDatasetFromNcmlSingleRunPerFile() {
        File testFile = new File(TestData.TEST_DATA_NCML_SINGLE_RUN_PER_FILE);
        if (null == testFile || !testFile.exists()) {
            return;
        }
        classUnderTest = new NetCDFFileInspector(testFile);
        String result = classUnderTest.getAnalysisTimeString();
        String expectedResult = TestData.TEST_RUNTIME1_AGGREGATION + ","
                + TestData.TEST_RUNTIME2_AGGREGATION;
        assertEquals("Unexpected analysis time string.", expectedResult, result);
    }

    /**
     * test for ncml file doing aggregation of nc files with single tau per file.
     */
    @Test
    public void testGetAnalysisTimeString_ForDatasetFromNcmlSingleTauPerFile() {
        File testFile = new File(TestData.TEST_DATA_NCML_SINGLE_TAU_PER_FILE);
        if (null == testFile || !testFile.exists()) {
            return;
        }
        classUnderTest = new NetCDFFileInspector(testFile);
        String result = classUnderTest.getAnalysisTimeString();
        String expectedResult = TestData.TEST_RUNTIME1_AGGREGATION + ","
                + TestData.TEST_RUNTIME2_AGGREGATION;
        assertEquals("Unexpected analysis time string.", expectedResult, result);
    }

    /**
     * test for ncml file doing aggregation of nc files with single run and multiple taus per file.
     */
    @Test
    public void testGetElevation_ForDatasetFromNcmlSingleRunPerFile() {
        File testFile = new File(TestData.TEST_DATA_NCML_SINGLE_RUN_PER_FILE);
        if (null == testFile || !testFile.exists()) {
            return;
        }
        classUnderTest = new NetCDFFileInspector(testFile);
        String result = classUnderTest.getElevationString();
        String expectedResult = "0.0,50.0,100.0,250.0,500.0";
        assertEquals("Unexpected elevation string.", expectedResult, result);
    }

    /**
     * test for ncml file doing aggregation of nc files with single run and single tau per file.
     */
    @Test
    public void testGetElevation_ForDatasetFromNcmlSingleTauPerFile() {
        File testFile = new File(TestData.TEST_DATA_NCML_SINGLE_TAU_PER_FILE);
        if (null == testFile || !testFile.exists()) {
            return;
        }
        classUnderTest = new NetCDFFileInspector(testFile);
        String result = classUnderTest.getElevationString();
        String expectedResult = "0.0,50.0,100.0,250.0,500.0";
        assertEquals("Unexpected elevation string.", expectedResult, result);
    }

    @Test
    public void testParseFiles_DatasetFromNetcdfFile_NoRuntime() throws ParseException {
        File testFile = new File(TestData.TEST_DATA_POSITIVE_LONGITUDE);
        if (null == testFile || !testFile.exists()) {
            return;
        }
        classUnderTest = new NetCDFFileInspector(testFile);
        float[] testBounds = TestData.TEST_BOUNDS_NCML_FILES;
        ParamInformation pi = createTestParamInformation(testBounds, null,
                ISODateTimeFormat.dateTime().parseDateTime("2000-01-04T00:00:00.000Z").toDate(), 0.0, "temp");
        GrdDataEncapsulator data = classUnderTest.parseFiles(pi);

        // 0.0 minimum value in test file.
        // 50.0 maximum value in test file.
        testImageData(data.getImageArray(), 0.0f, 50.0f);
    }

    @Test
    public void testParseFiles_DatasetFromNcmlSingleRunPerFile_FirstRuntime_FirstTime() {
        File testFile = new File(TestData.TEST_DATA_NCML_SINGLE_RUN_PER_FILE);
        if (null == testFile || !testFile.exists()) {
            return;
        }
        classUnderTest = new NetCDFFileInspector(testFile);
        float[] testBounds = TestData.TEST_BOUNDS_NCML_FILES;
        Date testDate = TestUtil
                .getTestDateOutput(TestData.TEST_RUNTIME1_AGGREGATION);
        ParamInformation pi = createTestParamInformation(testBounds, testDate,
                new Date(0), 0.0, "temp");
        GrdDataEncapsulator data = classUnderTest.parseFiles(pi);

        // 110.0 minimum value in test file for first run and first time
        // 119.9 maximum value in test file for first run and first time.
        testImageData(data.getImageArray(), 110.0f, 119.9f);
    }

    @Test
    public void testParseFiles_DatasetFromNcmlSingleRunPerFile_FirstRuntime_LastTime() {
        File testFile = new File(TestData.TEST_DATA_NCML_SINGLE_RUN_PER_FILE);
        if (null == testFile || !testFile.exists()) {
            return;
        }
        classUnderTest = new NetCDFFileInspector(testFile);
        float[] testBounds = TestData.TEST_BOUNDS_NCML_FILES;
        Date testDate = TestUtil
                .getTestDateOutput(TestData.TEST_RUNTIME1_AGGREGATION);
        ParamInformation pi = createTestParamInformation(testBounds, testDate,
                new Date(), 0.0, "temp");
        GrdDataEncapsulator data = classUnderTest.parseFiles(pi);

        // 150.0 minimum value in test file for first run and last time.
        // 159.9 maximum value in test file for first run and last time.
        testImageData(data.getImageArray(), 150.0f, 159.9f);
    }

    @Test
    public void testParseFiles_DatasetFromNcmlSingleRunPerFile_SecondRuntime_FirstTime() {
        File testFile = new File(TestData.TEST_DATA_NCML_SINGLE_RUN_PER_FILE);
        if (null == testFile || !testFile.exists()) {
            return;
        }
        classUnderTest = new NetCDFFileInspector(testFile);
        float[] testBounds = TestData.TEST_BOUNDS_NCML_FILES;
        Date testDate = TestUtil
                .getTestDateOutput(TestData.TEST_RUNTIME2_AGGREGATION);
        ParamInformation pi = createTestParamInformation(testBounds, testDate,
                new Date(0), 0.0, "temp");
        GrdDataEncapsulator data = classUnderTest.parseFiles(pi);

        // 210.0 minimum value in test file for second run and first time.
        // 219.9 maximum value in test file for second run and first time.
        testImageData(data.getImageArray(), 210.0f, 219.9f);
    }

    @Test
    public void testParseFiles_DatasetFromNcmlSingleRunPerFile_SecondRuntime_LastTime() {
        File testFile = new File(TestData.TEST_DATA_NCML_SINGLE_RUN_PER_FILE);
        if (null == testFile || !testFile.exists()) {
            return;
        }
        classUnderTest = new NetCDFFileInspector(testFile);
        float[] testBounds = TestData.TEST_BOUNDS_NCML_FILES;
        Date testDate = TestUtil
                .getTestDateOutput(TestData.TEST_RUNTIME2_AGGREGATION);
        ParamInformation pi = createTestParamInformation(testBounds, testDate,
                new Date(), 0.0, "temp");
        GrdDataEncapsulator data = classUnderTest.parseFiles(pi);

        // 250.0 minimum value in test file for second run and last time.
        // 259.9 maximum value in test file for second run and last time.
        testImageData(data.getImageArray(), 250.0f, 259.9f);
    }

    /**
     * test that if we have two model runs, and we do not specify which run we want data from, and
     * the requested time is in both, we will get the results from the later run.
     */
    @Test
    public void testParseFiles_DatasetFromNcmlSingleRunPerFile_RuntimeNotSpecified_TimeInBoth() {
        File testFile = new File(TestData.TEST_DATA_NCML_SINGLE_RUN_PER_FILE);
        if (null == testFile || !testFile.exists()) {
            return;
        }
        classUnderTest = new NetCDFFileInspector(testFile);
        float[] testBounds = TestData.TEST_BOUNDS_NCML_FILES;
        Date testDate = TestUtil
                .getTestDateOutput(TestData.TEST_RUNTIME2_AGGREGATION);
        ParamInformation pi = createTestParamInformation(testBounds, null,
                testDate, 0.0, "temp");
        GrdDataEncapsulator data = classUnderTest.parseFiles(pi);

        // 210.0 minimum value in test file for second run and specified time.
        // 219.9 maximum value in test file for second run and specified time.
        testImageData(data.getImageArray(), 210.0f, 219.9f);
    }

    @Test
    public void testParseFiles_DatasetFromNcmlSingleTauPerFile_FirstRuntime() {
        File testFile = new File(TestData.TEST_DATA_NCML_SINGLE_TAU_PER_FILE);
        if (null == testFile || !testFile.exists()) {
            return;
        }
        classUnderTest = new NetCDFFileInspector(testFile);
        float[] testBounds = TestData.TEST_BOUNDS_NCML_FILES;
        Date testDate = TestUtil
                .getTestDateOutput(TestData.TEST_RUNTIME1_AGGREGATION);
        ParamInformation pi = createTestParamInformation(testBounds, testDate,
                new Date(), 0.0, "temp");
        GrdDataEncapsulator data = classUnderTest.parseFiles(pi);

        // 150.0 minimum value in test file for first run and last time.
        // 159.9 maximum value in test file for first run and last time.
        testImageData(data.getImageArray(), 150.0f, 159.9f);
    }

    @Test
    public void testParseFiles_DatasetFromNcmlSingleTauPerFile_SecondRuntime() {
        File testFile = new File(TestData.TEST_DATA_NCML_SINGLE_TAU_PER_FILE);
        if (null == testFile || !testFile.exists()) {
            return;
        }
        classUnderTest = new NetCDFFileInspector(testFile);
        float[] testBounds = TestData.TEST_BOUNDS_NCML_FILES;
        Date testDate = TestUtil
                .getTestDateOutput(TestData.TEST_RUNTIME2_AGGREGATION);
        ParamInformation pi = createTestParamInformation(testBounds, testDate,
                new Date(), 0.0, "temp");
        GrdDataEncapsulator data = classUnderTest.parseFiles(pi);

        // 250.0 minimum value in test file for second run and last time.
        // 259.9 maximum value in test file for second run and last time.
        testImageData(data.getImageArray(), 250.0f, 259.9f);
    }

    /**
     * Created this test when revised our read strategy from point to range.
     *
     * This test passed for the previously existing 1D range read strategy (one read per lat, i.e.
     * get all lon values for a single lat in a single read). This read style worked fine whether
     * lat or lon is outer.
     *
     * When I created the first implementation of the new 2D range read strategy (one read for all
     * lats and lons of interest) this test failed, as expected, if the input file had variable with
     * lon dim outer of lat. Test passed once added separate branch for that case.
     */
    @Test
    public void testParseFiles_LatOuterLonSameAsLonOuterLat() {
        float[] testBounds = TestData.TEST_BOUNDS_FOR_LAT_OUTER_LON_AND_LON_OUTER_LAT;
        Date testDate = TestUtil.getTestDate(TestData.TEST_GLOBAL_TIME_ORIGIN);
        ParamInformation pi = createTestParamInformation(testBounds, testDate,
                new Date(), 0.0, "temp");

        File testFile = new File(TestData.TEST_DATA_LAT_OUTER_LON);
        if (null == testFile || !testFile.exists()) {
            return;
        }
        // use the latOuterLon data file to get the image data.
        classUnderTest = new NetCDFFileInspector(testFile);
        GrdDataEncapsulator dataForLatOuterLon = classUnderTest.parseFiles(pi);

        File testFile2 = new File(TestData.TEST_DATA_LON_OUTER_LAT);
        if (null == testFile2 || !testFile2.exists()) {
            return;
        }
        // use the lonOuterLat data file to get the image data.
        classUnderTest = new NetCDFFileInspector(testFile2);
        GrdDataEncapsulator dataForLonOuterLat = classUnderTest.parseFiles(pi);

        // test basic validity of each image data.
        // 3111.0 is the value for last time, first lat, first lon, first depth.
        // 3541.0 is the value for last time, last lat, last lon, first depth.
        testImageData(dataForLatOuterLon.getImageArray(), 3111.0f, 3541.0f);
        testImageData(dataForLonOuterLat.getImageArray(), 3111.0f, 3541.0f);
        // test that we get same result for both types of input data.
        assertTrue("These two image arrays should match and they do not.",
                Arrays.deepEquals(dataForLatOuterLon.getImageArray(),
                dataForLonOuterLat.getImageArray()));
    }

    private ParamInformation createTestParamInformation(float[] bounds,
            Date analysisTime, Date time, double depth, String variableName) {
        ParamInformation pi = new ParamInformation();

        int width = (int) Math
                .abs(bounds[NetCdfUtil.BOUNDS_INDEX_MAX_LONGITUDE]
                - bounds[NetCdfUtil.BOUNDS_INDEX_MIN_LONGITUDE]);
        int height = (int) Math
                .abs(bounds[NetCdfUtil.BOUNDS_INDEX_MAX_LATITUDE]
                - bounds[NetCdfUtil.BOUNDS_INDEX_MIN_LATITUDE]);
        pi.setDim(new GridEnvelope2D(new Rectangle(0, 0, width, height)));

        GeneralEnvelope ge = new GeneralEnvelope(new double[]{0, 0},
                new double[]{0, 0});
        ge.setRange(0, bounds[NetCdfUtil.BOUNDS_INDEX_MIN_LONGITUDE],
                bounds[NetCdfUtil.BOUNDS_INDEX_MAX_LONGITUDE]);
        ge.setRange(1, bounds[NetCdfUtil.BOUNDS_INDEX_MIN_LATITUDE],
                bounds[NetCdfUtil.BOUNDS_INDEX_MAX_LATITUDE]);
        ge.setCoordinateReferenceSystem(TestUtil.getTestCrs());
        pi.setRequestedEnvelope(ge);

        pi.setAnalysisTime(analysisTime);
        pi.setTime(time);
        pi.setElevation(depth);
        pi.setParameter(variableName);

        return pi;
    }

    private void testImageData(Float[][] imageArray, Float lowerLimit,
            Float upperLimit) {
        int countNaN = 0;
        int countValue = 0;
        for (int i = 0; i < imageArray.length; i++) {
            for (int j = 0; j < imageArray[i].length; j++) {
                if (Float.isNaN(imageArray[i][j])) {
                    countNaN++;
                } else {
                    countValue++;
                    if (lowerLimit != null && upperLimit != null) {
                        float imageValue = imageArray[i][j];
                        // System.out.println("value: " + imageArray[i][j]);
                        assertTrue(
                                "Image data value outside of expected range.",
                                (imageValue >= lowerLimit)
                                && (imageValue <= upperLimit));
                    }
                }
            }
        }
        System.out.println("countNaN is " + countNaN);
        System.out.println("countValue is " + countValue);

        int expectedCountValue = imageArray.length * imageArray[1].length;
        assertEquals("Unexpected number of image data values ",
                expectedCountValue, countValue);
    }

    private String convertNetCdfDateStringToOutputDateString(
            String netCdfDateString) {
        Date date = TestUtil.getTestDate(netCdfDateString);
        return NetCdfUtil.OUTPUT_DATE_FORMAT.format(date);
    }

    private void testGetBounds(File testFile, float[] expectedResult) {
        if (null == testFile || !testFile.exists()) {
            return;
        }
        classUnderTest = new NetCDFFileInspector(testFile);
        float[] bounds = classUnderTest.getBounds();
        printBounds(bounds);

        // basic range test.
        TestUtil.testBoundsMinVsMax(bounds);

        // optional specific value test.
        if (expectedResult != null) {
            TestUtil.testBoundsSpecificValues(expectedResult, bounds);
        }
    }

    private void printBounds(float[] bounds) {
        TestUtil.printFloatArray(this.getClass().getSimpleName(),
                this.testName.getMethodName(), "bounds", bounds);
    }
}
