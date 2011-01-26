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
package org.geotools.coverage.io.hdf4;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.media.jai.PlanarImage;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.CoverageAccess.AccessType;
import org.geotools.coverage.io.CoverageSource;
import org.geotools.coverage.io.domain.RasterDatasetDomainManager.HorizontalDomain;
import org.geotools.coverage.io.domain.RasterDatasetDomainManager.TemporalDomain;
import org.geotools.coverage.io.domain.RasterDatasetDomainManager.VerticalDomain;
import org.geotools.coverage.io.driver.BaseFileDriver;
import org.geotools.coverage.io.driver.Driver.DriverOperation;
import org.geotools.coverage.io.impl.CoverageReadRequest;
import org.geotools.coverage.io.impl.CoverageResponse;
import org.geotools.coverage.io.impl.CoverageResponse.Status;
import org.geotools.coverage.io.impl.range.DefaultRangeType;
import org.geotools.coverage.io.range.FieldType;
import org.geotools.coverage.io.range.RangeType;
import org.geotools.test.TestData;
import org.geotools.util.NumberRange;
import org.opengis.coverage.Coverage;
import org.opengis.feature.type.Name;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.temporal.TemporalGeometricPrimitive;

public final class HDF4Test extends TestCase {

    private final static Logger LOGGER = Logger.getLogger(HDF4Test.class.toString());

	/**
     * Creates a new instance of HDF4Test
     * 
     * @param name
     */
    public HDF4Test(String name) {
        super(name);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();

        suite.addTest(new HDF4Test("testReader"));

        return suite;
    }

    public void testReader() throws IllegalArgumentException, IOException,
            NoSuchAuthorityCodeException {
//        boolean isInteractiveTest = TestData.isInteractiveTest();
    	boolean isInteractiveTest = false;
        
        // create a base driver
        final BaseFileDriver driver = new HDF4Driver();
        File testDir = TestData.file(this,"");
        final String[] files = testDir.list(new FilenameFilter(){

			public boolean accept(File dir, String name) {
				return name.endsWith("hdf") || name.endsWith("hdf4") ;
			}});
        if (files==null||files.length==0){
            LOGGER.warning("Test files not found:\n Tests are skipped");
                     return;
        }
        
        for(String filePath:files){
        	final File file= new File(testDir,filePath);
	        final URL source = file.toURI().toURL();
	        if (driver.canProcess(DriverOperation.CONNECT, source,null)) {
	        	LOGGER.info("ACCEPTED: "+source.toString());
	
	            // getting access to the file
	            final CoverageAccess access = driver.process(DriverOperation.CONNECT,source, null, null,null);
	            if (access == null)
	                throw new IOException("Unable to connect");
	
	            // get the names
	            final List<Name> names = access.getNames(null);
	            for (Name name : names) {
	                // get a source
	                final CoverageSource gridSource = access.access(name, null,AccessType.READ_ONLY, null, null);
	                if (gridSource == null)
	                    throw new IOException("Unable to access");	                
	                LOGGER.info("Connected to coverage: "+name.toString());
	
	                // TEMPORAL DOMAIN
	                final TemporalDomain temporalDomain = gridSource.getDomainManager(null).getTemporalDomain();
	                if(temporalDomain==null)
	                	LOGGER.info("Temporal domain is null");
	                else{
	                	// temporal crs
	                	LOGGER.info("TemporalCRS: "+temporalDomain.getCoordinateReferenceSystem());
	                	
	                	// print the temporal domain elements
	                	for(TemporalGeometricPrimitive tg:temporalDomain.getTemporalElements(null)){
	                		LOGGER.info("TemporalGeometricPrimitive: "+tg.toString());
	                	}
	                }
	                
	                // VERTICAL DOMAIN
	                final VerticalDomain verticalDomain= gridSource.getDomainManager(null).getVerticalDomain();
	                if(verticalDomain==null || verticalDomain.getVerticalElements(true, null).isEmpty())
	                	LOGGER.info("Vertical domain is null");
	                else{
	                	// vertical  crs
	                	LOGGER.info("VerticalCRS: "+verticalDomain.getCoordinateReferenceSystem());
	                	
	                	// print the temporal domain elements
	                	for(NumberRange<Double> vg:verticalDomain.getVerticalElements(true, null)){
	                		LOGGER.info("Vertical domain element: "+vg.toString());
	                	}
	                }
	                
	                
	                // HORIZONTAL DOMAIN
	                final HorizontalDomain horizontalDomain= gridSource.getDomainManager(null).getHorizontalDomain();
	                if(horizontalDomain==null)
	                	LOGGER.info("Horizontal domain is null");
	                else{
	                	// print the horizontal domain elements
	                	final CoordinateReferenceSystem crs2D=horizontalDomain.getCoordinateReferenceSystem2D();
	                	assert crs2D!=null;
	                	final MathTransform2D g2w=horizontalDomain.getGridToWorldTransform(null);
	                	assert g2w !=null;
	                	final Set<? extends BoundingBox> spatialElements = horizontalDomain.getSpatialElements(true,null);
	                	assert spatialElements!=null&& !spatialElements.isEmpty();
	                	
	                	final StringBuilder buf= new StringBuilder();
	                	buf.append("Horizontal domain is as follows:\n");
	                	buf.append("G2W:").append("\t").append(g2w).append("\n");
	                	buf.append("CRS2D:").append("\t").append(crs2D).append("\n");
	                	for(BoundingBox bbox:spatialElements)
	                		buf.append("BBOX:").append("\t").append(bbox).append("\n");
	                	LOGGER.info(buf.toString());
	                }
	                
	                
	                // RANGE TYPE
	                RangeType range = gridSource.getRangeType(null);
	//
	                CoverageReadRequest readRequest = new CoverageReadRequest();
	                // //
	                //
	                // Setting up a limited range for the request.
	                //
	                // //
	                Iterator<FieldType> ftIterator = range.getFieldTypes().iterator();
	                HashSet<FieldType> fieldSet = new HashSet<FieldType>();
	                FieldType ft = null;
	                while (ftIterator.hasNext()) {
	                    ft = ftIterator.next();
	                    if (ft != null) {
	                        fieldSet.add(ft);
	                    }
	                    break;
	                }
	                RangeType rangeSubset = new DefaultRangeType(range.getName(), range.getDescription(), fieldSet);
	                readRequest.setRangeSubset(rangeSubset);
	//                LinkedHashSet<Envelope> requestedVerticalSubset = new LinkedHashSet<Envelope>();
	//                final int numLevels = verticalDomain.size();
	//                final Iterator<Envelope> iterator = verticalDomain.iterator();
	//                if (!isExtensiveTest) {
	//                    for (int i = 0; i < numLevels; i++) {
	//                        Envelope level = iterator.next();
	//                        if (i % (numLevels / 5) == 1)
	//                            requestedVerticalSubset.add(level);
	//                    }
	//                    readRequest.setVerticalSubset(requestedVerticalSubset);
	//                }
	                CoverageResponse response = gridSource.read(readRequest, null);
	                if (response == null || response.getStatus() != Status.SUCCESS || !response.getExceptions().isEmpty())
	                    throw new IOException("Unable to read");
	
	                final Collection<? extends Coverage> results = response.getResults(null);
	                for (Coverage c : results) {
	                    GridCoverage2D coverage = (GridCoverage2D) c;
	                    
	                    // Crs and envelope
	                    if (isInteractiveTest) {
	                    	coverage.show();
	                    }
	                    else
	                    	PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getTiles();
	                    
                    	final StringBuilder buffer= new StringBuilder();
                        buffer.append("GridCoverage CRS: ").append(coverage.getCoordinateReferenceSystem2D().toWKT()).append("\n");
                        buffer.append("GridCoverage GG: ").append(coverage.getGridGeometry().toString()).append( "\n");
                        LOGGER.info(buffer.toString());
	                }
	            }
	        } else
	        	LOGGER.info("NOT ACCEPTED");
        }
    }

}
