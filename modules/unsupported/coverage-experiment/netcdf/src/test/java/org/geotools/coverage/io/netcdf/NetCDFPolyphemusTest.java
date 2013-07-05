/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002, Geotools Project Managment Committee (PMC)
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
package org.geotools.coverage.io.netcdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.jai.PlanarImage;

import junit.framework.Assert;

import org.apache.commons.io.filefilter.FileFilterUtils;
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
import org.geotools.imageio.netcdf.NetCDFImageReader;
import org.geotools.imageio.netcdf.NetCDFImageReaderSpi;
import org.geotools.imageio.unidata.UnidataSlice2DIndex;
import org.geotools.test.TestData;
import org.geotools.util.DateRange;
import org.geotools.util.NumberRange;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.coverage.Coverage;
import org.opengis.feature.type.Name;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform2D;

/**
 * 
 *
 * @source $URL$
 */
public final class NetCDFPolyphemusTest extends Assert {

    private final static Logger LOGGER = Logger.getLogger(NetCDFPolyphemusTest.class.toString());

    
    @Test
    @Ignore
    public void testImageReader() throws IllegalArgumentException, IOException, NoSuchAuthorityCodeException {
        final String[] files = TestData.file(this, ".").list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.equalsIgnoreCase("O3-NO2.nc");
            }
        });

        for (String filePath : files) {

            final File file = new File(TestData.file(this, "."), filePath);

            final NetCDFImageReaderSpi netCDFImageReaderSpi = new NetCDFImageReaderSpi();
            assertTrue(netCDFImageReaderSpi.canDecodeInput(file));
            NetCDFImageReader reader = null;
            try {
                reader = (NetCDFImageReader) netCDFImageReaderSpi.createReaderInstance();
                reader.setInput(file);
                int numImages = reader.getNumImages(true);
                for (int i = 0; i < numImages; i++) {
                    UnidataSlice2DIndex sliceIndex = reader.getSlice2DIndex(i);
                    String variableName = sliceIndex.getVariableName();
                    StringBuilder sb = new StringBuilder();
                    sb.append("\n").append("\n").append("\n");
                    sb.append("IMAGE: ").append(i).append("\n");
                    sb.append(" Variable Name = ").append(variableName);
                    sb.append(" ( Z = ");
                    sb.append(sliceIndex.getZIndex());
                    sb.append("; T = ");
                    sb.append(sliceIndex.getTIndex());
                    sb.append(")");
                    LOGGER.info(sb.toString());
                }
            } catch (Throwable t) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, t.getLocalizedMessage(), t);
                }
            } finally {
                if (reader != null) {
                    try {
                        reader.dispose();
                    } catch (Throwable t) {
                        // Does nothing
                    }
                }
            }
        }
    }

    @Test
    @Ignore
    public void geoToolsReader() throws IllegalArgumentException, IOException, NoSuchAuthorityCodeException {
        boolean isInteractiveTest = TestData.isInteractiveTest();

        // create a base driver
        final DefaultFileDriver driver = new NetCDFDriver();
        final String[] files = TestData.file(this, ".").list(new FilenameFilter(){

            @Override
            public boolean accept( File dir, String name ) {
                return name.equalsIgnoreCase("O3-NO2.nc");
            }
        });

        for( String filePath : files ) {

            final File file = new File(TestData.file(this, "."), filePath);
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
                        StringBuilder overallTemporal = new StringBuilder("Temporal domain element (overall = true):\n");
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
                        StringBuilder overallVertical = new StringBuilder("Vertical domain element (overall = true):\n");
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

                    LinkedHashSet<NumberRange<Double>> requestedVerticalSubset = new LinkedHashSet<NumberRange<Double>>();
                    SortedSet<? extends NumberRange<Double>> verticalElements = verticalDomain
                            .getVerticalElements(false, null);
                    final int numLevels = verticalElements.size();
                    final Iterator<? extends NumberRange<Double>> iterator = verticalElements.iterator();
                    for (int i = 0; i < numLevels; i++) {
                        NumberRange<Double> level = iterator.next();
                        if (i % (numLevels / 5) == 1) {
                            requestedVerticalSubset.add(level);
                        }
                    }
                    readRequest.setVerticalSubset(requestedVerticalSubset);

                    SortedSet<DateRange> requestedTemporalSubset = new DateRangeTreeSet();
                    SortedSet<? extends DateRange> temporalElements = temporalDomain.getTemporalElements(false, null);
                    final int numTimes = temporalElements.size();
                    Iterator<? extends DateRange> iteratorT = temporalElements.iterator();
                    for (int i = 0; i < numTimes; i++) {
                        DateRange time = iteratorT.next();
                        if (i % (numTimes / 5) == 1) {
                            requestedTemporalSubset.add(time);
                        }
                    }
                    readRequest.setTemporalSubset(requestedTemporalSubset);

                    CoverageResponse response = gridSource.read(readRequest, null);
                    if (response == null || response.getStatus() != Status.SUCCESS || !response.getExceptions().isEmpty()) {
                        throw new IOException("Unable to read");
                    }

                    final Collection<? extends Coverage> results = response.getResults(null);
                    int index = 0;
                    for (Coverage c : results) {
                        GridCoverageResponse resp = (GridCoverageResponse) c;
                        GridCoverage2D coverage = resp.getGridCoverage2D();
                        String title = coverage.getSampleDimension(0).getDescription().toString();
                        // Crs and envelope
                        if (isInteractiveTest) {
                            // ImageIOUtilities.visualize(coverage.getRenderedImage(), "tt",true);
                            coverage.show(title + " " + index++);
                        } else {
                            PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getTiles();
                        }

                        final StringBuilder buffer = new StringBuilder();
                        buffer.append("GridCoverage CRS: ")
                                .append(coverage.getCoordinateReferenceSystem2D().toWKT())
                                .append("\n");
                        buffer.append("GridCoverage GG: ")
                                .append(coverage.getGridGeometry().toString()).append("\n");
                        LOGGER.info(buffer.toString());
                    }
                    gridSource.dispose();
                }
            } catch (Throwable t) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, t.getLocalizedMessage(), t);
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
    
    private void cleanUp() throws FileNotFoundException, IOException {
        if (TestData.isInteractiveTest()) {
            return;
        }
        final File dir = TestData.file(this, ".");
        File[] files = dir.listFiles((FilenameFilter) FileFilterUtils.notFileFilter(FileFilterUtils
                .or(FileFilterUtils.or(FileFilterUtils.suffixFileFilter(".nc")))));
        for (File file : files) {
            file.delete();
        }
    }

    @After
    public void tearDown() throws FileNotFoundException, IOException {
        cleanUp();
    }
}
