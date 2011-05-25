/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.geotiff;


import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.media.jai.JAI;
import javax.media.jai.TileCache;

import junit.framework.TestCase;
import junit.textui.TestRunner;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.CoverageResponse;
import org.geotools.coverage.io.CoverageSource;
import org.geotools.coverage.io.CoverageStore;
import org.geotools.coverage.io.CoverageAccess.AccessType;
import org.geotools.coverage.io.CoverageResponse.Status;
import org.geotools.coverage.io.driver.Driver.DriverOperation;
import org.geotools.coverage.io.geotiff.GeoTiffAccess;
import org.geotools.coverage.io.geotiff.GeoTiffDriver;
import org.geotools.coverage.io.impl.DefaultCoverageReadRequest;
import org.geotools.coverage.io.impl.DefaultCoverageUpdateRequest;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.test.TestData;
import org.opengis.coverage.Coverage;
import org.opengis.feature.type.Name;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * @author Simone Giannecchini
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/geotiff/src/test/java/org/geotools/gce/geotiff/GeoTiffWriterTest.java $
 */
public class GeoTiffWriterTest extends TestCase {
	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(GeoTiffWriterTest.class.toString());
	private static GeoTiffDriver factory;

	/**
	 * 
	 */
	public GeoTiffWriterTest() {
		super("Writer Test!");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestRunner.run(GeoTiffWriterTest.class);

	}
    
    	/*
         * @see TestCase#setUp()
         */
        protected void setUp() throws Exception {
            super.setUp();
            final JAI jaiDef = JAI.getDefaultInstance();
    
            // using a big tile cache
            final TileCache cache = jaiDef.getTileCache();
            cache.setMemoryCapacity(64 * 1024 * 1024);
            cache.setMemoryThreshold(0.75f);
    
            factory = new GeoTiffDriver();
        }
    
        private static MathTransform getConversionFromBase(
                CoordinateReferenceSystem crs) {
            return (crs instanceof ProjectedCRS) ? ((ProjectedCRS) crs)
                    .getConversionFromBase().getMathTransform() : null;
        }
    
        /**
         * Checks two envelopes for equality ignoring their CRSs.
         * 
         * @param sourceEnv
         *                first {@link GeneralEnvelope} to check.
         * @param targetEnv
         *                second {@link GeneralEnvelope} to check.
         * @param gc
         *                the source {@link GridCoverage2D}.
         * @return false if they are reasonably equal, false otherwise.
         */
        private boolean checkEnvelopes(GeneralEnvelope sourceEnv,
                GeneralEnvelope targetEnv, GridCoverage2D gc) {
            final int dimension = sourceEnv.getDimension();
            if (sourceEnv.getDimension() != targetEnv.getDimension()) {
                return false;
            }
            AffineTransform mathTransformation = (AffineTransform) ((GridGeometry2D) gc
                    .getGridGeometry()).getGridToCRS2D();
            double epsilon;
            for (int i = 0; i < dimension; i++) {
                epsilon = i == 0 ? XAffineTransform.getScaleX0(mathTransformation)
                        : XAffineTransform.getScaleY0(mathTransformation);
                // Comparison below uses '!' in order to catch NaN values.
                if (!(Math.abs(sourceEnv.getMinimum(i) - targetEnv.getMinimum(i)) <= epsilon && Math
                        .abs(sourceEnv.getMaximum(i) - targetEnv.getMaximum(i)) <= epsilon)) {
                    return false;
                }
            }
            return true;
        }

	/**
         * Testing {@link GeoTiffWriter} capabilities to write a cropped
         * coverage.
         * 
         * @throws IllegalArgumentException
         * @throws IOException
         * @throws UnsupportedOperationException
         * @throws ParseException
         * @throws FactoryException
         * @throws TransformException
         */
    public void testWrite() throws IllegalArgumentException, IOException,
            UnsupportedOperationException, ParseException, FactoryException,
            TransformException {

        final File dir = TestData.file(GeoTiffReaderTest.class, "");
        final File writedir = new File(new StringBuffer(dir.getAbsolutePath())
                .append("/testWriter/").toString());
        writedir.mkdir();
        final File files[] = dir.listFiles();
        for (File file : files) {
            StringBuffer buffer = new StringBuffer();
            final String path = file.getAbsolutePath().toLowerCase();
            if (!path.endsWith("tif") && !path.endsWith("tiff"))
                continue;

            buffer.append(file.getAbsolutePath()).append("\n");
            final URL source = file.toURI().toURL();
            if (factory.canProcess(DriverOperation.CONNECT, source, null)) {
                buffer.append("ACCEPTED").append("\n");

                // getting access to the file
                CoverageAccess access = factory.process(DriverOperation.CONNECT,source, null, null,null);
                if (access == null)
                    throw new IOException("");

                // get the names
                List<Name> names = access.getNames(null);
                for (Name name : names) {
                    // get a source
                    CoverageSource gridSource = access.access(name, null,
                            AccessType.READ_ONLY, null, null);
                    if (gridSource == null)
                        throw new IOException("");
                    // create a request
                    // reading the coverage
                    CoverageResponse response = gridSource.read(
                            new DefaultCoverageReadRequest(), null);
                    if (response == null
                            || response.getStatus() != Status.SUCCESS
                            || !response.getExceptions().isEmpty())
                        throw new IOException("");

                    Collection<? extends Coverage> results = response
                            .getResults(null);
                    for (Coverage c : results) {
                        GridCoverage2D coverage = (GridCoverage2D) c;
                        // Crs and envelope
                        if (TestData.isInteractiveTest()) {
                            buffer.append("CRS: ").append(
                                    coverage.getCoordinateReferenceSystem2D()
                                            .toWKT()).append("\n");
                            buffer.append("GG: ").append(
                                    coverage.getGridGeometry().toString())
                                    .append("\n");
                        }

                        // create an update request
                        final DefaultCoverageUpdateRequest request = new DefaultCoverageUpdateRequest();
                        request.setData(Collections.singletonList(coverage));

                        // create access

                        final File writeFile = new File(writedir, coverage
                                .getName().toString()
                                + ".tiff");
                        final GeoTiffAccess storeAccess = 
                        	(GeoTiffAccess) factory.process(
                        			DriverOperation.CREATE,
                        			writeFile.toURI().toURL(), 
                        			null, 
                        			null,
                        			null);
                        final CoverageStore gridStore = (CoverageStore) storeAccess
                                .create(name, null, null, null);

                        // write it down
                        gridStore.update(request, null);

                        // getting access to the file
                        access = factory.process(DriverOperation.CONNECT,writeFile.toURI().toURL(),null, null, null);
                        if (access == null)
                            throw new IOException("");

                        //get the names
                        names = access.getNames(null);
                        gridSource = access.access(names.iterator().next(),
                                null, AccessType.READ_ONLY, null, null);
                        if (gridSource == null)
                            throw new IOException("");
                        //create a request
                        // reading the coverage
                        response = gridSource.read(
                                new DefaultCoverageReadRequest(), null);
                        if (response == null
                                || response.getStatus() != Status.SUCCESS
                                || !response.getExceptions().isEmpty())
                            throw new IOException("");

                        results = response.getResults(null);
                        for (Coverage cov : results) {
                            GridCoverage2D coverage1 = (GridCoverage2D) cov;
                            // Crs and envelope
                            if (TestData.isInteractiveTest()) {
                                buffer
                                        .append("CRS: ")
                                        .append(
                                                coverage1
                                                        .getCoordinateReferenceSystem2D()
                                                        .toWKT()).append("\n");
                                buffer.append("GG: ").append(
                                        coverage1.getGridGeometry().toString())
                                        .append("\n");
                                coverage1.show();
                            }
                        }
                    }
                }
            } else
                buffer.append("NOT ACCEPTED").append("\n");
            if (TestData.isInteractiveTest())
                LOGGER.info(buffer.toString());
        }
    }
}
