/* Copyright (c) 2001 - 2013 OpenPlans - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.gce.netcdf;
//
//import java.awt.Rectangle;
//
//import org.geocent.test.TestData;
//import org.geocent.test.TestUtil;
//import org.geotools.geometry.GeneralEnvelope;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.opengis.coverage.grid.GridEnvelope;
//
///**
// * unit tests for named class.
// */
//public class NetCDFReaderTest {
//    NetCDFReader classUnderTest;
//
//    @Before
//    public void setUp() throws Exception {
//	classUnderTest = new NetCDFReader();
//    }
//
//    @After
//    public void tearDown() throws Exception {
//	classUnderTest = null;
//    }
//
//    @Test
//    public void testCalculateBoundsAsRectanglePositiveLongitude() {
//	float[] bounds = TestData.TEST_BOUNDS_POSITIVE_LONGITUDE;
//	Rectangle boundsAsRectangle = classUnderTest
//		.calculateBoundsAsRectangle(bounds);
//
//	TestUtil.testRectangle(boundsAsRectangle, 2, 0, 2, 73);
//    }
//
//    /**
//     * test failed before changes for story/defect #artf119555, #artf120060.
//     */
//    @Test
//    public void testCalculateBoundsAsRectangleNegativeLongitudeSimulateNcomReg01() {
//	float[] bounds = TestData.TEST_BOUNDS_NEGATIVE_LONGITUDE_SIMULATE_NCOMREG01;
//	Rectangle boundsAsRectangle = classUnderTest
//		.calculateBoundsAsRectangle(bounds);
//
//	TestUtil.testRectangle(boundsAsRectangle, -100, 0, 50, 70);
//    }
//
//    /**
//     * test failed before changes for story/defect #artf119555, #artf120060.
//     */
//    @Test
//    public void testCalculateBoundsAsRectangleNegativeLongitudeSimulateSwan() {
//	float[] bounds = TestData.TEST_BOUNDS_NEGATIVE_LONGITUDE_SIMULATE_SWAN;
//	Rectangle boundsAsRectangle = classUnderTest
//		.calculateBoundsAsRectangle(bounds);
//
//	TestUtil.testRectangle(boundsAsRectangle, -162, 18, 8, 5);
//    }
//
//    @Test
//    public void testCalculateOriginalEnvelopePositiveLongitude() {
//	float[] bounds = TestData.TEST_BOUNDS_POSITIVE_LONGITUDE;
//	GeneralEnvelope generalEnvelope = classUnderTest
//		.calculateOriginalEnvelope(bounds, TestUtil.getTestCrs());
//
//	TestUtil.testGeneralEnvelope(generalEnvelope, bounds);
//
//    }
//
//    @Test
//    public void testCalculateOriginalEnvelopeNegativeLongitudeSimulateNcomReg01() {
//	float[] bounds = TestData.TEST_BOUNDS_NEGATIVE_LONGITUDE_SIMULATE_NCOMREG01;
//	GeneralEnvelope generalEnvelope = classUnderTest
//		.calculateOriginalEnvelope(bounds, TestUtil.getTestCrs());
//
//	TestUtil.testGeneralEnvelope(generalEnvelope, bounds);
//
//    }
//
//    @Test
//    public void testCalculateOriginalEnvelopeNegativeLongitudeSimulateSwan() {
//	float[] bounds = TestData.TEST_BOUNDS_NEGATIVE_LONGITUDE_SIMULATE_SWAN;
//	GeneralEnvelope generalEnvelope = classUnderTest
//		.calculateOriginalEnvelope(bounds, TestUtil.getTestCrs());
//
//	TestUtil.testGeneralEnvelope(generalEnvelope, bounds);
//
//    }
//
//    @Test
//    public void testCalculateBoundsAsRectangleAndCalculateOriginalGridRangePositiveLongitude() {
//	float[] bounds = TestData.TEST_BOUNDS_POSITIVE_LONGITUDE;
//
//	Rectangle boundsAsRectangle = classUnderTest
//		.calculateBoundsAsRectangle(bounds);
//	TestUtil.testRectangle(boundsAsRectangle, 2, 0, 2, 73);
//
//	GridEnvelope gridEnvelope = classUnderTest
//		.calculateOriginalGridRange(boundsAsRectangle);
//	TestUtil.testGridEnvelope(gridEnvelope, 2, 3, 0, 72);
//    }
//
//    /**
//     * test failed before changes for story/defect #artf119555, #artf120060.
//     */
//    @Test
//    public void testCalculateBoundsAsRectangleAndCalculateOriginalGridRangeNegativeLongitudeSimulateNcomReg01() {
//	float[] bounds = TestData.TEST_BOUNDS_NEGATIVE_LONGITUDE_SIMULATE_NCOMREG01;
//
//	Rectangle boundsAsRectangle = classUnderTest
//		.calculateBoundsAsRectangle(bounds);
//	TestUtil.testRectangle(boundsAsRectangle, -100, 0, 50, 70);
//
//	GridEnvelope gridEnvelope = classUnderTest
//		.calculateOriginalGridRange(boundsAsRectangle);
//	TestUtil.testGridEnvelope(gridEnvelope, -100, -51, 0, 69);
//    }
//
//    /**
//     * test failed before changes for story/defect #artf119555, #artf120060.
//     */
//    @Test
//    public void testCalculateBoundsAsRectangleAndCalculateOriginalGridRangeNegativeLongitudeSimulateSwan() {
//	float[] bounds = TestData.TEST_BOUNDS_NEGATIVE_LONGITUDE_SIMULATE_SWAN;
//
//	Rectangle boundsAsRectangle = classUnderTest
//		.calculateBoundsAsRectangle(bounds);
//	TestUtil.testRectangle(boundsAsRectangle, -162, 18, 8, 5);
//
//	GridEnvelope gridEnvelope = classUnderTest
//		.calculateOriginalGridRange(boundsAsRectangle);
//	TestUtil.testGridEnvelope(gridEnvelope, -162, -155, 18, 22);
//    }
//
//}
