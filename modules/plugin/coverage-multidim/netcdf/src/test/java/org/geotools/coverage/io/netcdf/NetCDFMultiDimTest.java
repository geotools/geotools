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
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.Query;
import org.geotools.gce.imagemosaic.ImageMosaicFormat;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.test.TestData;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;

/**
 * 
 * @author Niels Charlier
 * 
 * the samples used in this test class (.nc and .ncml files located in test-data/unidata) 
 * are taken from 
 * http://www.unidata.ucar.edu/software/thredds/current/netcdf-java/ncml/Aggregation.html
 * (see THREDDS license)
 * except the reversed sample files which are manipulations of the originals from the website above.
 *
 */
public class NetCDFMultiDimTest {
    
    private static String D = "D";
    
    private static SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    
    static {
        DF.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
            
    @SuppressWarnings("rawtypes")
    @Test    
    public void test() throws Exception {
        NetCDFFormat format = new NetCDFFormat();
        File file = TestData.file(this, "fivedim.nc");
                
        NetCDFReader reader = (NetCDFReader) format.getReader(file);
        
        //eight slices !
        assertEquals(8, reader.getGranules(D, true).getCount(new Query(D)));
        
        //check time, elevation and runtime in metadata        
        assertEquals("true", reader.getMetadataValue(D, "HAS_ELEVATION_DOMAIN"));
        assertEquals("true", reader.getMetadataValue(D, "HAS_TIME_DOMAIN"));
        assertEquals("true", reader.getMetadataValue(D, "HAS_RUNTIME_DOMAIN"));
        assertEquals("java.util.Date", reader.getMetadataValue(D, "RUNTIME_DOMAIN_DATATYPE"));
        assertEquals("0.0/0.0,1.0/1.0", reader.getMetadataValue(D, "ELEVATION_DOMAIN"));
        assertEquals("2012-04-01T00:00:00.000Z/2012-04-01T00:00:00.000Z,2012-04-01T01:00:00.000Z/2012-04-01T01:00:00.000Z", 
                reader.getMetadataValue(D, "TIME_DOMAIN"));
        assertEquals("2012-04-01T02:00:00.000Z,2012-04-01T03:00:00.000Z", reader.getMetadataValue(D, "RUNTIME_DOMAIN"));

        
        //parameter descriptor for runtime
        Set<ParameterDescriptor<List>> pd = reader.getDynamicParameters(D);
        assertEquals(1, pd.size());
        
        final ParameterDescriptor<List> runtime = pd.iterator().next();
        assertTrue("runtime".equalsIgnoreCase(runtime.getName().getCode()));
        
        //check requesting data
        ParameterValue<List> timeValue = ImageMosaicFormat.TIME.createValue();
        ParameterValue<List> zValue = ImageMosaicFormat.ELEVATION.createValue();
        ParameterValue<List> runtimeValue = runtime.createValue();
        
        //z = 0, time = 0, runtime = 2;
        zValue.setValue(Collections.singletonList(0.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 00:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));
        
        checkFoursome(reader, new GeneralParameterValue[] { zValue, timeValue, runtimeValue }, 0, 1, 2, 3);
        
        //z = 1, time = 0, runtime = 2;
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 00:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));
        
        checkFoursome(reader, new GeneralParameterValue[] { zValue, timeValue, runtimeValue }, 4, 5, 6, 7);
        
        //z = 0, time = 1, runtime = 2;
        zValue.setValue(Collections.singletonList(0.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 01:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));
        
        checkFoursome(reader, new GeneralParameterValue[] { zValue, timeValue, runtimeValue }, 8, 9, 10, 11);

        //z = 1, time = 1, runtime = 2;
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 01:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));
        
        checkFoursome(reader, new GeneralParameterValue[] { zValue, timeValue, runtimeValue }, 12, 13, 14, 15);
        
        //z = 0, time = 0, runtime = 3;
        zValue.setValue(Collections.singletonList(0.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 00:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));
        
        checkFoursome(reader, new GeneralParameterValue[] { zValue, timeValue, runtimeValue }, 16, 17, 18, 19);   
        
        //z = 1, time = 0, runtime = 3;
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 00:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));

        checkFoursome(reader, new GeneralParameterValue[] { zValue, timeValue, runtimeValue }, 20, 21, 22, 23);
        
        //z = 0, time = 1, runtime = 3;
        zValue.setValue(Collections.singletonList(0.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 01:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));

        checkFoursome(reader, new GeneralParameterValue[] { zValue, timeValue, runtimeValue }, 24, 25, 26, 27);

        //z = 1, time = 1, runtime = 3;
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 01:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));

        checkFoursome(reader, new GeneralParameterValue[] { zValue, timeValue, runtimeValue }, 28, 29, 30, 31);
        
    }
    
    private void checkFoursome(NetCDFReader reader, GeneralParameterValue[] pams, double a, double b, double c, double d) 
            throws IllegalArgumentException, IOException {
        GridCoverage2D cov = reader.read(D, pams) ;
        
        assertEquals(a, ((double[]) cov.evaluate(new DirectPosition2D(-109, 41)))[0], 0.0);
        assertEquals(b, ((double[]) cov.evaluate(new DirectPosition2D(-107, 41)))[0], 0.0);
        assertEquals(c, ((double[]) cov.evaluate(new DirectPosition2D(-109, 40)))[0], 0.0);
        assertEquals(d, ((double[]) cov.evaluate(new DirectPosition2D(-107, 40)))[0], 0.0);
        
    }
    
}
