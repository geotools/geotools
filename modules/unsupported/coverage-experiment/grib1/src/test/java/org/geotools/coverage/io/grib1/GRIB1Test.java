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
package org.geotools.coverage.io.grib1;


import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.jai.PlanarImage;

import junit.framework.Assert;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.CoverageAccess.AccessType;
import org.geotools.coverage.io.CoverageReadRequest;
import org.geotools.coverage.io.CoverageResponse;
import org.geotools.coverage.io.CoverageResponse.Status;
import org.geotools.coverage.io.CoverageSource;
import org.geotools.coverage.io.CoverageSource.HorizontalDomain;
import org.geotools.coverage.io.CoverageSource.TemporalDomain;
import org.geotools.coverage.io.CoverageSource.VerticalDomain;
import org.geotools.coverage.io.Driver.DriverOperation;
import org.geotools.coverage.io.impl.DefaultFileDriver;
import org.geotools.coverage.io.range.FieldType;
import org.geotools.coverage.io.range.RangeType;
import org.geotools.coverage.io.range.impl.DefaultRangeType;
import org.geotools.test.TestData;
import org.geotools.util.NumberRange;
import org.junit.Ignore;
import org.opengis.coverage.Coverage;
import org.opengis.feature.type.Name;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.temporal.TemporalGeometricPrimitive;

/**
 * 
 *
 * @source $URL$
 */
public final class GRIB1Test extends Assert {
	
	private final static Logger LOGGER=Logger.getLogger(GRIB1Test.class.toString());

    private final boolean isExtensiveTest = TestData.isExtensiveTest();

   
    @org.junit.Test
    @Ignore
    public void testReader() throws IllegalArgumentException, IOException,
            NoSuchAuthorityCodeException, InterruptedException {
        final DefaultFileDriver driver = new GRIB1Driver();
        
        File dir = TestData.file(this, "");
        File[] files = dir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                final String path = pathname.getAbsolutePath();
                if (path.endsWith(".grib") || path.endsWith(".grb"))
                    return true;
                return false;
            }
        });
        for (File inputFile : files) {
        	if(LOGGER.isLoggable(Level.INFO))
        		LOGGER.info("Testing file "+ inputFile.getAbsolutePath());
	
	        final URL source = inputFile.toURI().toURL();
	        if (driver.canProcess(DriverOperation.CONNECT,source,null)) {
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
	                final TemporalDomain temporalDomain = gridSource.getTemporalDomain();
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
	                final VerticalDomain verticalDomain= gridSource.getVerticalDomain();
	                if(verticalDomain==null)
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
	                final HorizontalDomain horizontalDomain= gridSource.getHorizontalDomain();
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
	                RangeType range = gridSource.getRangeType(null);
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
	                    if (!isExtensiveTest)
	                        break;
	                }
	                RangeType rangeSubset = new DefaultRangeType(range.getName(),range.getDescription(), fieldSet);
	                readRequest.setRangeSubset(rangeSubset);
	                CoverageResponse response = gridSource.read(readRequest, null);
	                if (response == null || response.getStatus() != Status.SUCCESS|| !response.getExceptions().isEmpty())
	                    throw new IOException("Unable to read");
	
	                final Collection<? extends Coverage> results = response.getResults(null);
	                for (Coverage c : results) {
	                    GridCoverage2D coverage = (GridCoverage2D) c;
	                    // Crs and envelope
	                    if (TestData.isInteractiveTest()) {
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
