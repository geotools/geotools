/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Logger;
import javax.media.jai.PlanarImage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.geotools.api.coverage.Coverage;
import org.geotools.api.feature.type.Name;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.CoverageAccess.AccessType;
import org.geotools.coverage.io.CoverageReadRequest;
import org.geotools.coverage.io.CoverageResponse;
import org.geotools.coverage.io.CoverageResponse.Status;
import org.geotools.coverage.io.CoverageSource;
import org.geotools.coverage.io.CoverageSource.SpatialDomain;
import org.geotools.coverage.io.CoverageSource.TemporalDomain;
import org.geotools.coverage.io.CoverageSource.VerticalDomain;
import org.geotools.coverage.io.Driver.DriverCapabilities;
import org.geotools.coverage.io.GridCoverageResponse;
import org.geotools.coverage.io.impl.DefaultFileDriver;
import org.geotools.coverage.io.util.DateRangeTreeSet;
import org.geotools.test.TestData;
import org.geotools.util.DateRange;
import org.geotools.util.NumberRange;
import org.geotools.util.logging.Logging;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public final class NetCDFPolyphemusTest extends NetCDFBaseTest {

    private static final Logger LOGGER = Logging.getLogger(NetCDFPolyphemusTest.class);
    private File testDirectory;

    @Test
    public void geoToolsReader() throws IllegalArgumentException, IOException, NoSuchAuthorityCodeException {
        boolean isInteractiveTest = TestData.isInteractiveTest();

        // create a base driver
        final DefaultFileDriver driver = new NetCDFDriver();
        final File[] files = TestData.file(this, ".")
                .listFiles(pathname ->
                        FilenameUtils.getName(pathname.getAbsolutePath()).equalsIgnoreCase("O3-NO2.nc"));

        for (File f : files) {

            // move to test directory
            final File file = new File(testDirectory, "O3-NO2.nc");
            FileUtils.copyFile(f, file);

            // get the file
            final URL source = file.toURI().toURL();
            assertTrue(driver.canProcess(DriverCapabilities.CONNECT, source, null));

            LOGGER.info("ACCEPTED: " + source.toString());

            // getting access to the file
            CoverageAccess access = null;

            try {
                access = driver.process(DriverCapabilities.CONNECT, source, null, null, null);
                if (access == null) {
                    throw new IOException("Unable to connect");
                }
                // get the names
                final List<Name> names = access.getNames(null);
                for (Name name : names) {
                    // get a source
                    final CoverageSource gridSource = access.access(name, null, AccessType.READ_ONLY, null, null);
                    if (gridSource == null) {
                        throw new IOException("Unable to access");
                    }
                    LOGGER.info("Connected to coverage: " + name.toString());

                    // TEMPORAL DOMAIN
                    final TemporalDomain temporalDomain = gridSource.getTemporalDomain();
                    if (temporalDomain == null) {
                        LOGGER.info("Temporal domain is null");
                    } else {
                        // temporal crs
                        LOGGER.info("TemporalCRS: " + temporalDomain.getCoordinateReferenceSystem());

                        // print the temporal domain elements
                        for (DateRange tg : temporalDomain.getTemporalElements(true, null)) {
                            LOGGER.info("Global Temporal Domain: " + tg.toString());
                        }

                        // print the temporal domain elements with overall = true
                        StringBuilder overallTemporal =
                                new StringBuilder("Temporal domain element (overall = true):\n");
                        for (DateRange tg : temporalDomain.getTemporalElements(false, null)) {
                            overallTemporal.append(tg.toString()).append("\n");
                        }
                        LOGGER.info(overallTemporal.toString());
                    }

                    // VERTICAL DOMAIN
                    final VerticalDomain verticalDomain = gridSource.getVerticalDomain();
                    if (verticalDomain == null) {
                        LOGGER.info("Vertical domain is null");
                    } else {
                        // vertical crs
                        LOGGER.info("VerticalCRS: " + verticalDomain.getCoordinateReferenceSystem());

                        // print the Vertical domain elements
                        for (NumberRange<Double> vg : verticalDomain.getVerticalElements(true, null)) {
                            LOGGER.info("Vertical domain element: " + vg.toString());
                        }

                        // print the Vertical domain elements with overall = true
                        StringBuilder overallVertical =
                                new StringBuilder("Vertical domain element (overall = true):\n");
                        for (NumberRange<Double> vg : verticalDomain.getVerticalElements(false, null)) {
                            overallVertical.append(vg.toString()).append("\n");
                        }
                        LOGGER.info(overallVertical.toString());
                    }

                    // HORIZONTAL DOMAIN
                    final SpatialDomain spatialDomain = gridSource.getSpatialDomain();
                    if (spatialDomain == null) {
                        LOGGER.info("Horizontal domain is null");
                    } else {
                        // print the horizontal domain elements
                        final CoordinateReferenceSystem crs2D = spatialDomain.getCoordinateReferenceSystem2D();
                        assert crs2D != null;
                        final MathTransform2D g2w = spatialDomain.getGridToWorldTransform(null);
                        assert g2w != null;
                        final Set<? extends BoundingBox> spatialElements = spatialDomain.getSpatialElements(true, null);
                        assert spatialElements != null && !spatialElements.isEmpty();

                        final StringBuilder buf = new StringBuilder();
                        buf.append("Horizontal domain is as follows:\n");
                        buf.append("G2W:").append("\t").append(g2w).append("\n");
                        buf.append("CRS2D:").append("\t").append(crs2D).append("\n");
                        for (BoundingBox bbox : spatialElements) {
                            buf.append("BBOX:").append("\t").append(bbox).append("\n");
                        }
                        LOGGER.info(buf.toString());
                    }

                    CoverageReadRequest readRequest = new CoverageReadRequest();
                    // //
                    //
                    // Setting up a limited range for the request.
                    //
                    // //

                    LinkedHashSet<NumberRange<Double>> requestedVerticalSubset = new LinkedHashSet<>();
                    SortedSet<? extends NumberRange<Double>> verticalElements =
                            verticalDomain.getVerticalElements(false, null);
                    final int numLevels = verticalElements.size();
                    final Iterator<? extends NumberRange<Double>> iterator = verticalElements.iterator();
                    int step = numLevels / 5 > 0 ? numLevels / 5 : 1;
                    for (int i = 0; i < numLevels; i++) {
                        NumberRange<Double> level = iterator.next();
                        if (i % step == 0) {
                            requestedVerticalSubset.add(level);
                        }
                    }
                    readRequest.setVerticalSubset(requestedVerticalSubset);

                    SortedSet<DateRange> requestedTemporalSubset = new DateRangeTreeSet();
                    SortedSet<? extends DateRange> temporalElements = temporalDomain.getTemporalElements(false, null);
                    final int numTimes = temporalElements.size();
                    Iterator<? extends DateRange> iteratorT = temporalElements.iterator();
                    step = numTimes / 5 > 0 ? numTimes / 5 : 1;
                    for (int i = 0; i < numTimes; i++) {
                        DateRange time = iteratorT.next();
                        if (i % step == 0) {
                            requestedTemporalSubset.add(time);
                        }
                    }
                    readRequest.setTemporalSubset(requestedTemporalSubset);

                    CoverageResponse response = gridSource.read(readRequest, null);
                    if (response == null
                            || response.getStatus() != Status.SUCCESS
                            || !response.getExceptions().isEmpty()) {
                        throw new IOException("Unable to read");
                    }

                    final Collection<? extends Coverage> results = response.getResults(null);
                    int index = 0;
                    for (Coverage c : results) {
                        GridCoverageResponse resp = (GridCoverageResponse) c;
                        GridCoverage2D coverage = resp.getGridCoverage2D();
                        String title =
                                coverage.getSampleDimension(0).getDescription().toString();
                        // Crs and envelope
                        if (isInteractiveTest) {
                            // ImageIOUtilities.visualize(coverage.getRenderedImage(), "tt",true);
                            coverage.show(title + " " + index++);
                        } else {
                            PlanarImage.wrapRenderedImage(coverage.getRenderedImage())
                                    .getTiles();
                        }

                        final StringBuilder buffer = new StringBuilder();
                        buffer.append("GridCoverage CRS: ")
                                .append(coverage.getCoordinateReferenceSystem2D()
                                        .toWKT())
                                .append("\n");
                        buffer.append("GridCoverage GG: ")
                                .append(coverage.getGridGeometry().toString())
                                .append("\n");
                        LOGGER.info(buffer.toString());
                    }
                    gridSource.dispose();
                }
            } finally {
                if (access != null) {
                    try {
                        access.dispose();
                    } catch (Throwable t) {
                        // Does nothing
                    }
                }
            }
        }
    }

    @After
    public void tearDown() throws Exception {
        if (TestData.isInteractiveTest()) {
            return;
        }
        if (testDirectory.exists()) {
            FileUtils.deleteDirectory(testDirectory);
            testDirectory.delete();
        }
    }

    @Before
    public void before() throws Exception {
        File testDir = TestData.file(this, null);
        testDirectory = new File(testDir, Long.toString(System.nanoTime()));
        if (testDirectory.exists()) {
            FileUtils.deleteDirectory(testDirectory);
            testDirectory.delete();
        }
        testDirectory.mkdir();
    }
}
