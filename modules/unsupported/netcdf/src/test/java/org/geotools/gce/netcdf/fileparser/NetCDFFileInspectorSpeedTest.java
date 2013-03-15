/* Copyright (c) 2001 - 2013 OpenPlans - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.gce.netcdf.fileparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.geotools.gce.netcdf.GrdDataEncapsulator;
import org.geotools.gce.netcdf.NetCdfUtil;
import org.geotools.gce.netcdf.ParamInformation;
import org.geotools.gce.netcdf.log.LogUtil;
import org.geotools.gce.netcdf.test.TestDataForSpeedTests;
import org.geotools.gce.netcdf.test.TestUtil;
import org.geotools.util.logging.Logging;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

/**
 * Unit tests for named class to help analyze operation and speed. These are commented out as they
 * may rely on uncommitted data files and do not need to run on an ongoing basis. I did want to
 * commit these for our future use.
 *
 * See the documentation in TestDataForSpeedTests.java for configuration information.
 */
public class NetCDFFileInspectorSpeedTest {

    NetCDFFileInspector classUnderTest;
    private static final Logger LOG = Logging
            .getLogger(NetCDFFileInspectorSpeedTest.class);
    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() throws Exception {
        // get rid of log4j warnings
        org.apache.log4j.BasicConfigurator.configure();
        org.apache.log4j.Logger logger = org.apache.log4j.Logger
                .getRootLogger();
        logger.setLevel(org.apache.log4j.Level.OFF);
    }

    @After
    public void tearDown() throws Exception {
        classUnderTest = null;
    }

    @Ignore
    @Test
    public void testWarmUp() {
        Date methodBeginDate = new Date();
        int i = 0;
        i++;
        System.out.print(i);
        LogUtil.logElapsedTime(LOG, methodBeginDate, "warm up");
    }

    // NCOM coastal series of tests
    @Ignore
    @Test
    public void testParseFiles_Coastal_Tau000_Nc() {
        testParseFiles_Coastal(TestDataForSpeedTests.TEST_DATA_SPEED_COASTAL_TAU_000_NC);
    }

    @Ignore
    @Test
    public void testParseFiles_Coastal_Tau048_Nc() {
        testParseFiles_Coastal(TestDataForSpeedTests.TEST_DATA_SPEED_COASTAL_TAU_048_NC);
    }

    @Ignore
    @Test
    public void testParseFiles_Coastal_Single_File_No_Agg_Ncml() {
        testParseFiles_Coastal(TestDataForSpeedTests.TEST_DATA_SPEED_COASTAL_SINGLE_FILE_NO_AGG_NCML);
    }

    @Ignore
    @Test
    public void testParseFiles_Coastal_Single_File_Agg_Ncml() {
        testParseFiles_Coastal(TestDataForSpeedTests.TEST_DATA_SPEED_COASTAL_SINGLE_FILE_AGG_NCML);
    }

    @Ignore
    @Test
    public void testParseFiles_Coastal_Many_File_Agg_Ncml() {
        testParseFiles_Coastal(TestDataForSpeedTests.TEST_DATA_SPEED_COASTAL_MANY_FILE_AGG_NCML);
    }

    @Ignore
    @Test
    public void testParseFiles_Coastal_Tau000_Nc_2() {
        testParseFiles_Coastal(TestDataForSpeedTests.TEST_DATA_SPEED_COASTAL_TAU_000_NC);
    }

    @Ignore
    @Test
    public void testParseFiles_Coastal_Tau048_Nc_2() {
        testParseFiles_Coastal(TestDataForSpeedTests.TEST_DATA_SPEED_COASTAL_TAU_048_NC);
    }

    @Ignore
    @Test
    public void testParseFiles_Coastal_Single_File_No_Agg_Ncml_2() {
        testParseFiles_Coastal(TestDataForSpeedTests.TEST_DATA_SPEED_COASTAL_SINGLE_FILE_NO_AGG_NCML);
    }

    private void testParseFiles_Coastal(String fileName) {
        testParseFiles(fileName, TestDataForSpeedTests.TEST_BOUNDS_COASTAL,
                TestDataForSpeedTests.TEST_SHAPE_COASTAL_WATER_TEMP);
    }

    // NCOM global reg01 series of tests
    @Ignore
    @Test
    public void testParseFiles_Global_Reg01() {
        testParseFiles_Global_Reg01(TestDataForSpeedTests.TEST_DATA_SPEED_GLOBAL_REG01_NC);
    }

    @Ignore
    @Test
    public void testParseFiles_Global_Reg01_2() {
        testParseFiles_Global_Reg01(TestDataForSpeedTests.TEST_DATA_SPEED_GLOBAL_REG01_NC);
    }

    private void testParseFiles_Global_Reg01(String fileName) {
        testParseFiles(fileName,
                TestDataForSpeedTests.TEST_BOUNDS_GLOBAL_REG01,
                TestDataForSpeedTests.TEST_SHAPE_GLOBAL_REG01_WATER_TEMP);
    }

    // NCOM global reg07 series of tests
    @Ignore
    @Test
    public void testParseFiles_Global_Reg07() {
        testParseFiles_Global_Reg07(TestDataForSpeedTests.TEST_DATA_SPEED_GLOBAL_REG07_NC);
    }

    @Ignore
    @Test
    public void testParseFiles_Global_Reg07_2() {
        testParseFiles_Global_Reg07(TestDataForSpeedTests.TEST_DATA_SPEED_GLOBAL_REG07_NC);
    }

    private void testParseFiles_Global_Reg07(String fileName) {
        testParseFiles(fileName,
                TestDataForSpeedTests.TEST_BOUNDS_GLOBAL_REG07,
                TestDataForSpeedTests.TEST_SHAPE_GLOBAL_REG07_WATER_TEMP);
    }

    // NCOM regional hawaii series of tests
    @Ignore
    @Test
    public void testParseFiles_Regional_Hawaii() {
        testParseFiles_Regional_Hawaii(TestDataForSpeedTests.TEST_DATA_SPEED_REGIONAL_HAWAII_TAU_000_NC);
    }

    @Ignore
    @Test
    public void testParseFiles_Regional_Hawaii_2() {
        testParseFiles_Regional_Hawaii(TestDataForSpeedTests.TEST_DATA_SPEED_REGIONAL_HAWAII_TAU_096_NC);
    }

    private void testParseFiles_Regional_Hawaii(String fileName) {
        testParseFiles(fileName,
                TestDataForSpeedTests.TEST_BOUNDS_REGIONAL_HAWAII,
                TestDataForSpeedTests.TEST_SHAPE_REGIONAL_HAWAII_WATER_TEMP);
    }

    private void testParseFiles(String fileName, float[] bounds, int[] shape) {
        Date methodBeginDate = new Date();

        classUnderTest = new NetCDFFileInspector(new File(fileName));
        ParamInformation pi = TestUtil.createTestParamInformation(bounds,
                shape, null, new Date(), 0.0, "water_temp");
        GrdDataEncapsulator data = classUnderTest.parseFiles(pi);

        testImageData(data.getImageArray());
        // TODO what better qualitative test can we do here?

        LogUtil.logElapsedTime(LOG, methodBeginDate,
                classUnderTest.getFileName());
    }

    /**
     * used this to test optimize building the read input parameter. abstracted the original switch
     * statement using String.format() into a method, created other test candidate methods using
     * StringBuffer, tested each using this test method, and based on the results, replaced the
     * original with the speediest.
     */
    @Ignore
    @Test
    public void testBuildReadParameterMethods() {
        testBuildReadParameter("buildReadParameter");
        // testBuildReadParameter("buildReadParameter1");
        // testBuildReadParameter("buildReadParameter2");
        // testBuildReadParameter("buildReadParameterOrig");
        // testBuildReadParameter("buildReadParameter2");
        // testBuildReadParameter("buildReadParameterOrig");
        testBuildReadParameter("buildReadParameter");
        // testBuildReadParameter("buildReadParameter1");
    }

    private void testBuildReadParameter(String methodName) {
        Date methodBeginDate = new Date();

        int desiredRepetitions = 100000;
        int preventOptimization = 0;
        classUnderTest = new NetCDFFileInspector();

        try {
            Class<?>[] methodParameters = new Class[]{int.class,
                String[].class};
            Method methodUnderTest = classUnderTest.getClass()
                    .getDeclaredMethod(methodName, methodParameters);

            for (int i = 0; i < desiredRepetitions; i++) {
                //
                String[] range = new String[]{
                    String.valueOf((int) Math.random()), "1", "2", "3", "4"};
                int dimensionSize = range.length;
                String result = (String) methodUnderTest.invoke(classUnderTest,
                        new Object[]{dimensionSize, range});
                preventOptimization += result.length();
            }

        } catch (SecurityException e) {
            // e.printStackTrace();
            Assert.fail("Test setup problem.");
        } catch (NoSuchMethodException e) {
            // e.printStackTrace();
            Assert.fail("Test setup problem.");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Assert.fail("Test setup problem.");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Assert.fail("Test setup problem.");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Assert.fail("Test setup problem.");
        }

        System.out.println("testBuildReadParameter result: "
                + preventOptimization);
        LogUtil.logElapsedTime(LOG, methodBeginDate, methodName);
    }

    /**
     * used this to test optimize getting the results of the read. three test sub-cases: a.
     * true,true - gets the read output using reduce. b. true,false - gets the read output w/o using
     * reduce. c. false,true - a 'control', does not get the output, just does the read. b. was
     * clearly faster than a. so made the change in the code.
     */
    @Ignore
    @Test
    public void testGetReadResult() {
        testGetReadResult(true, false);
        testGetReadResult(true, true);
        testGetReadResult(false, true);
        testGetReadResult(true, true);
        testGetReadResult(true, false);
        testGetReadResult(false, true);
    }

    private void testGetReadResult(boolean getValue, boolean useReduce) {
        Date methodBeginDate = new Date();
        classUnderTest = new NetCDFFileInspector();

        try {
            NetcdfFile ncFile = NetcdfDataset.openFile(
                    TestDataForSpeedTests.TEST_DATA_SPEED_COASTAL_TAU_000_NC,
                    null);
            Variable waterTemp = NetCdfUtil.getFileVariableByName(ncFile,
                    NetCdfUtil.WATER_TEMPERATURE_VARIABLE_NAMES);

            for (int dim0 = 0; dim0 < TestDataForSpeedTests.TEST_SHAPE_COASTAL_WATER_TEMP[0]; dim0++) {
                for (int dim1 = 0; dim1 < TestDataForSpeedTests.TEST_SHAPE_COASTAL_WATER_TEMP[1]; dim1++) {
                    for (int dim2 = 0; dim2 < TestDataForSpeedTests.TEST_SHAPE_COASTAL_WATER_TEMP[2]; dim2++) {
                        for (int dim3 = 0; dim3 < TestDataForSpeedTests.TEST_SHAPE_COASTAL_WATER_TEMP[3]; dim3++) {
                            String sectionSelector = String.valueOf(dim0) + ","
                                    + String.valueOf(dim1) + ","
                                    + String.valueOf(dim2) + ","
                                    + String.valueOf(dim3);
                            Array array = waterTemp.read(sectionSelector);

                            if (getValue) {
                                if (useReduce) {
                                    array.reduce().getFloat(0);
                                } else {
                                    array.getFloat(0);
                                }
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Test setup problem.");
        } catch (InvalidRangeException e) {
            e.printStackTrace();
            Assert.fail("Test setup problem.");
        }

        LogUtil.logElapsedTime(LOG, methodBeginDate, String.valueOf(getValue)
                + "," + String.valueOf(useReduce));
    }

    private void testImageData(Float[][] imageArray) {
        int countNaN = 0;
        int countValue = 0;
        for (int i = 0; i < imageArray.length; i++) {
            for (int j = 0; j < imageArray[i].length; j++) {
                if (Float.isNaN(imageArray[i][j])) {
                    countNaN++;
                } else {
                    countValue++;
                }
            }
        }

        int expectedNumberOfImageValues = imageArray.length
                * imageArray[1].length;

        System.out.println("expectedNumberOfImageValues "
                + expectedNumberOfImageValues);
        System.out.println("countNaN is " + countNaN);
        System.out.println("countValue is " + countValue);
        // System.out.println("imageArray.length " + imageArray.length);
        // System.out.println("imageArray[0].length " + imageArray[0].length);

        assertEquals("Unexpected number of image data values ",
                expectedNumberOfImageValues, countNaN + countValue);
    }
}
