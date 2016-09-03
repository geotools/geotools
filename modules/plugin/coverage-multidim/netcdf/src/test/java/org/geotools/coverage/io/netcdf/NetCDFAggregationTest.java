/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.geotools.imageio.netcdf.NetCDFImageReader;
import org.geotools.imageio.netcdf.NetCDFImageReaderSpi;
import org.geotools.test.TestData;
import org.junit.Test;
import org.opengis.feature.type.Name;

/**
 * 
 * @author Niels Charlier
 * 
 * the samples used in this test class are taken from 
 * http://www.unidata.ucar.edu/software/thredds/current/netcdf-java/ncml/Aggregation.html
 * see THREDDS license
 *
 */
public class NetCDFAggregationTest {
        
    @Test
    public void testUnion() throws IOException {
        NetCDFImageReaderSpi readerSpi = new NetCDFImageReaderSpi();
        File file = TestData.file(this, "unidata/aggUnionSimple.ncml");
        assertTrue(readerSpi.canDecodeInput(file));
        
        NetCDFImageReader reader = (NetCDFImageReader) readerSpi.createReaderInstance();
        reader.setInput(file);
        
        Set<String> coverageNames = new HashSet<String>();
        for (Name name : reader.getCoveragesNames()) {
            coverageNames.add(name.getLocalPart());
        }
            
        assertEquals(2, coverageNames.size());
        assertTrue(coverageNames.contains("lflx"));
        assertTrue(coverageNames.contains("cldc"));
    }
    
    @Test    
    public void testJoinExisting() throws IOException {
        NetCDFImageReaderSpi readerSpi = new NetCDFImageReaderSpi();
        File file = TestData.file(this, "unidata/aggExisting.ncml");
        assertTrue(readerSpi.canDecodeInput(file));
        
        NetCDFImageReader reader = (NetCDFImageReader) readerSpi.createReaderInstance();
        reader.setInput(file);
            
        assertEquals(59, reader.getVariableByName("P").getDimension(0).getLength());
        assertEquals(59, reader.getVariableByName("T").getDimension(0).getLength());
        assertEquals(59, reader.getVariableByName("time").getDimension(0).getLength());
    }
    
    @Test    
    public void testJoinNew() throws IOException {
        NetCDFImageReaderSpi readerSpi = new NetCDFImageReaderSpi();
        File file = TestData.file(this, "unidata/aggNewCoord.ncml");
        assertTrue(readerSpi.canDecodeInput(file));
        
        NetCDFImageReader reader = (NetCDFImageReader) readerSpi.createReaderInstance();
        reader.setInput(file);
            
        assertEquals("time", reader.getVariableByName("T").getDimension(0).getFullName());
        assertEquals(3, reader.getVariableByName("T").getDimension(0).getLength());
        assertEquals(3, reader.getVariableByName("time").getDimension(0).getLength());
    }
    
    @Test    
    public void testRunTime() throws IOException {
        NetCDFImageReaderSpi readerSpi = new NetCDFImageReaderSpi();
        File file = TestData.file(this, "unidata/aggRunTime.ncml");
        assertTrue(readerSpi.canDecodeInput(file));
        
        NetCDFImageReader reader = (NetCDFImageReader) readerSpi.createReaderInstance();
        reader.setInput(file);
        
        assertEquals(3, reader.getVariableByName("runtime").getDimension(0).getLength());
        assertEquals("runtime", reader.getVariableByName("T").getDimension(0).getFullName());        
        
    }

}
