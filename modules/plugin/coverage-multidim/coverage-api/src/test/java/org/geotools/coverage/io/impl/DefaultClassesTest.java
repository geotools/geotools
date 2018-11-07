/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.io.CoverageAccess.AccessType;
import org.geotools.coverage.io.Driver.DriverCapabilities;
import org.geotools.coverage.io.driver.TestDriver;
import org.geotools.data.Parameter;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.DateRange;
import org.geotools.util.NumberRange;
import org.geotools.util.factory.Hints;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.type.Name;

/** @author Nicola Lagomarsini Geosolutions */
public class DefaultClassesTest {

    private static Map<String, Parameter<?>> emptyMap;

    private static Map<String, Serializable> connectionParams;

    private static URL url;

    private static Hints hints;

    private static Name name;

    @BeforeClass
    public static void setup() throws MalformedURLException {
        emptyMap = Collections.emptyMap();
        connectionParams = new HashMap<String, Serializable>();
        url = new URL(TestDriver.TEST_URL);
        connectionParams.put(DefaultFileDriver.URL.key, url);
        hints = new Hints();
        name = new NameImpl("test");
    }

    @Test
    public void testDefaultCoverageAccess() throws IOException {
        DefaultCoverageAccess access =
                new DefaultCoverageAccess(
                        new TestDriver(),
                        EnumSet.of(AccessType.READ_WRITE),
                        emptyMap,
                        connectionParams);
        // Checks on the creation and removal of a store
        assertFalse(access.canCreate(name, connectionParams, hints, null));
        assertFalse(access.canDelete(name, connectionParams, hints));
        assertFalse(access.delete(name, connectionParams, hints));

        // Test exceptions
        // Ensure that UnsupportedOperationException is thrown
        boolean notThrown = false;
        try {
            access.access(name, connectionParams, AccessType.READ_ONLY, hints, null);
            notThrown = true;
        } catch (UnsupportedOperationException e) {
        } catch (IOException e) {
            notThrown = true;
        }
        assertFalse(notThrown);
        // Reset the boolean
        notThrown = false;

        try {
            access.create(name, connectionParams, hints, null);
            notThrown = true;
        } catch (UnsupportedOperationException e) {
        } catch (IOException e) {
            notThrown = true;
        }
        assertFalse(notThrown);
        // Reset the boolean
        notThrown = false;
        try {
            access.getInfo(null);
            notThrown = true;
        } catch (UnsupportedOperationException e) {
        }
        assertFalse(notThrown);
        // Reset the boolean
        notThrown = false;
    }

    @Test
    public void testDefaultFileDriver() {
        DefaultFileDriver driver =
                new DefaultFileDriver(
                        TestDriver.TEST_DRIVER,
                        TestDriver.TEST_DRIVER,
                        TestDriver.TEST_DRIVER,
                        new Hints(),
                        Collections.singletonList(".EXT"),
                        EnumSet.of(
                                DriverCapabilities.CONNECT,
                                DriverCapabilities.CREATE,
                                DriverCapabilities.DELETE));

        // Various checks on the DefaultFileDriver class
        assertFalse(driver.canProcess(DriverCapabilities.CONNECT, url, connectionParams));
        assertFalse(driver.canProcess(DriverCapabilities.CREATE, url, connectionParams));
        assertFalse(driver.canProcess(DriverCapabilities.DELETE, url, connectionParams));

        assertFalse(driver.canConnect(connectionParams));
        assertFalse(driver.canCreate(connectionParams));
        assertFalse(driver.canDelete(connectionParams));

        // Ensure that UnsupportedOperationException is thrown
        boolean notThrown = false;
        try {
            driver.connect(connectionParams, hints, null);
            notThrown = true;
        } catch (UnsupportedOperationException e) {
        } catch (IOException e) {
            notThrown = true;
        }
        assertFalse(notThrown);
        // Reset the boolean
        notThrown = false;

        try {
            driver.create(connectionParams, hints, null);
            notThrown = true;
        } catch (UnsupportedOperationException e) {
        } catch (IOException e) {
            notThrown = true;
        }
        assertFalse(notThrown);
        // Reset the boolean
        notThrown = false;

        try {
            driver.delete(connectionParams, hints, null);
            notThrown = true;
        } catch (UnsupportedOperationException e) {
        } catch (IOException e) {
            notThrown = true;
        }
        assertFalse(notThrown);

        try {
            driver.process(DriverCapabilities.CONNECT, url, connectionParams, hints, null);
            notThrown = true;
        } catch (UnsupportedOperationException e) {
        } catch (IOException e) {
            notThrown = true;
        }
        assertFalse(notThrown);
        // Reset the boolean
        notThrown = false;

        try {
            driver.process(DriverCapabilities.CREATE, url, connectionParams, hints, null);
            notThrown = true;
        } catch (UnsupportedOperationException e) {
        } catch (IOException e) {
            notThrown = true;
        }
        assertFalse(notThrown);
        // Reset the boolean
        notThrown = false;

        try {
            driver.process(DriverCapabilities.DELETE, url, connectionParams, hints, null);
            notThrown = true;
        } catch (UnsupportedOperationException e) {
        } catch (IOException e) {
            notThrown = true;
        }
        assertFalse(notThrown);

        // ParameterInfo
        Map<String, Parameter<?>> connectParameterInfo = driver.getConnectParameterInfo();
        assertTrue(connectParameterInfo != null);
        assertTrue(connectParameterInfo.containsKey(DefaultFileDriver.URL.key));
        Map<String, Parameter<?>> createParameterInfo = driver.getCreateParameterInfo();
        assertTrue(createParameterInfo != null);
        assertTrue(createParameterInfo.containsKey(DefaultFileDriver.URL.key));
        Map<String, Parameter<?>> deleteParameterInfo = driver.getDeleteParameterInfo();
        assertTrue(deleteParameterInfo != null);
        assertTrue(deleteParameterInfo.containsKey(DefaultFileDriver.URL.key));
    }

    @Test
    public void testDefaultGridCoverageResponse() {
        // creation of a dummy coverage and response
        GridCoverage2D cov =
                new GridCoverageFactory()
                        .create(
                                "test",
                                new float[][] {{1.0f, 1.0f}},
                                new ReferencedEnvelope(0.0d, 1.0d, 0.0d, 1.0d, null));
        DateRange temporalExtent = new DateRange(new Date(10000), new Date(20000));
        NumberRange<Double> verticalExtent = new NumberRange<Double>(Double.class, 0.0d, 100.0d);
        DefaultGridCoverageResponse response =
                new DefaultGridCoverageResponse(cov, temporalExtent, verticalExtent);

        // Check if the response results are equals to that of the input GridCoverage
        assertEquals(response.getNumSampleDimensions(), cov.getNumSampleDimensions());
        assertEquals(response.getNumOverviews(), cov.getNumOverviews());
        assertTrue(
                CRS.equalsIgnoreMetadata(
                        response.getCoordinateReferenceSystem(),
                        cov.getCoordinateReferenceSystem()));
        assertTrue(new ReferencedEnvelope(response.getEnvelope()).contains(cov.getEnvelope2D()));
        assertTrue(response.isDataEditable() == cov.isDataEditable());
        assertSame(response.getGridGeometry(), cov.getGridGeometry());
        assertSame(response.getGridCoverage2D(), cov);
        assertSame(response.getRenderedImage(), cov.getRenderedImage());
        assertSame(response.getTemporalExtent(), temporalExtent);
        assertSame(response.getVerticalExtent(), verticalExtent);
        assertSame(response.getSampleDimension(0), cov.getSampleDimension(0));
        assertSame(response.getSources(), cov.getSources());
        assertArrayEquals(response.getOptimalDataBlockSizes(), cov.getOptimalDataBlockSizes());

        // Evaluation of the same position
        DirectPosition2D pos =
                new DirectPosition2D(
                        cov.getEnvelope2D().getCenterX(), cov.getEnvelope2D().getCenterY());

        assertArrayEquals(response.evaluate(pos, new byte[1]), cov.evaluate(pos, new byte[1]));
    }
}
