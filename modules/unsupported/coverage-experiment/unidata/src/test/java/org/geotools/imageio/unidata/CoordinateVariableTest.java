/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.unidata;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.Assert;

import org.geotools.imageio.unidata.cv.CoordinateVariable;
import org.geotools.imageio.unidata.utilities.UnidataTimeUtilities;
import org.geotools.test.TestData;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.TemporalCRS;
import org.opengis.referencing.crs.VerticalCRS;

import ucar.nc2.Dimension;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.CoordinateAxis1D;
import ucar.nc2.dataset.NetcdfDataset;

/**
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
public class CoordinateVariableTest extends Assert{
    
    @Test
    public void polyphemus() throws Exception{
        
        // acquire dataset
        final NetcdfDataset dataset = NetcdfDataset.openDataset(TestData.url(this, "O3-NO2.nc").toExternalForm());
        assertNotNull(dataset);
        final List<CoordinateAxis> cvs = dataset.getCoordinateAxes();
        assertNotNull(cvs);
        assertSame(4,cvs.size());
        
        //
        // cloud_formations is short
        //
        Dimension dim = dataset.findDimension("time");
        assertNotNull(dim);
        assertEquals("time", dim.getShortName());
        
        // check type
        CoordinateAxis coordinateAxis = dataset.findCoordinateAxis(dim.getShortName());
        assertNotNull(coordinateAxis);
        assertTrue(coordinateAxis instanceof CoordinateAxis1D);
        Class<?> binding = CoordinateVariable.suggestBinding((CoordinateAxis1D) coordinateAxis);
        assertNotNull(binding);
        assertSame(Date.class, binding);
        CoordinateVariable<?> cv= CoordinateVariable.create((CoordinateAxis1D) coordinateAxis);
        assertSame(Date.class, cv.getType());
        

        List<?> list = cv.read();
        assertNotNull(list);
        assertEquals(2, list.size());
        
        final GregorianCalendar cal= new GregorianCalendar(UnidataTimeUtilities.UTC_TIMEZONE);
        cal.set(2012, 3, 1, 0, 0,0);
        cal.set(GregorianCalendar.MILLISECOND, 0);
        assertEquals(cal.getTime(), cv.getMinimum());
        assertEquals(list.get(0), cv.getMinimum());
        
        cal.set(2012, 3, 1, 1, 0,0);
        assertEquals(cal.getTime(), cv.getMaximum());
        assertEquals(list.get(1), cv.getMaximum());
        assertEquals(2, cv.getSize());
        assertEquals("hours since 2012-04-01 0:00:00", cv.getUnit());
        CoordinateReferenceSystem crs = cv.getCoordinateReferenceSystem();
        assertNotNull(crs);
        assertTrue(crs instanceof TemporalCRS);
        //
        // lat is float
        //
        dim = dataset.findDimension("z");
        assertNotNull(dim);
        assertEquals("z", dim.getShortName());
        
        // check type
        coordinateAxis = dataset.findCoordinateAxis(dim.getShortName());
        assertNotNull(coordinateAxis);
        assertTrue(coordinateAxis instanceof CoordinateAxis1D);
        binding = CoordinateVariable.suggestBinding((CoordinateAxis1D) coordinateAxis);
        assertNotNull(binding);
        assertSame(Float.class, binding);        
        
        cv= CoordinateVariable.create((CoordinateAxis1D) coordinateAxis);
        list = cv.read();
        assertNotNull(list);
        assertEquals(2, list.size());
        
        assertSame(Float.class, cv.getType());
        assertEquals(10f, cv.getMinimum());
        assertEquals(450f, cv.getMaximum());
        assertEquals(list.get(0), cv.getMinimum());
        assertEquals(list.get(1), cv.getMaximum());
        assertEquals(2, cv.getSize());
        assertEquals("meters", cv.getUnit());
       
        crs = cv.getCoordinateReferenceSystem();
        assertNotNull(crs);
        assertTrue(crs instanceof VerticalCRS);
        
        dataset.close();
        
    }

    @Test
    @Ignore
    public void testIASI() throws Exception{
        //
        // IASI does not have time or runtime, it only contains double variables besides lat e long
        //
        
        
        // acquire dataset
        final NetcdfDataset dataset = NetcdfDataset.openDataset(TestData.url(this, "IASI_C_EUMP_20121120062959_31590_eps_o_l2.nc").toExternalForm());
        assertNotNull(dataset);
        final List<CoordinateAxis> cvs = dataset.getCoordinateAxes();
        assertNotNull(cvs);
        assertSame(8,cvs.size());
        
        //
        // cloud_formations is short
        //
        Dimension dim = dataset.findDimension("cloud_formations");
        assertNotNull(dim);
        assertEquals("cloud_formations", dim.getShortName());
        
        // check type
        CoordinateAxis coordinateAxis = dataset.findCoordinateAxis(dim.getShortName());
        assertNotNull(coordinateAxis);
        assertTrue(coordinateAxis instanceof CoordinateAxis1D);
        Class<?> binding = CoordinateVariable.suggestBinding((CoordinateAxis1D) coordinateAxis);
        assertNotNull(binding);
        assertSame(Short.class, binding);
        CoordinateVariable<?> cv= CoordinateVariable.create((CoordinateAxis1D) coordinateAxis);
        assertSame(Short.class, cv.getType());
        assertEquals((short)0, cv.getMinimum());
        assertEquals((short)2, cv.getMaximum());
        assertEquals(3, cv.getSize());
        assertEquals("level", cv.getUnit());
        
        //
        // lat is float
        //
        dim = dataset.findDimension("lat");
        assertNotNull(dim);
        assertEquals("lat", dim.getShortName());
        
        // check type
        coordinateAxis = dataset.findCoordinateAxis(dim.getShortName());
        assertNotNull(coordinateAxis);
        assertTrue(coordinateAxis instanceof CoordinateAxis1D);
        binding = CoordinateVariable.suggestBinding((CoordinateAxis1D) coordinateAxis);
        assertNotNull(binding);
        assertSame(Float.class, binding);
        cv= CoordinateVariable.create((CoordinateAxis1D) coordinateAxis);
        assertNotNull(cv);
        assertSame(Float.class, cv.getType());
        assertEquals(-77.327934f, cv.getMinimum());
        assertEquals(89.781555f, cv.getMaximum());
        assertEquals(766, cv.getSize());   
        assertEquals("degrees_north", cv.getUnit());     
        assertTrue(cv.isRegular());
        assertEquals(cv.getMinimum(), cv.getStart());
        assertEquals(0.2184437770469516, cv.getIncrement());
        
        //
        // lon is float
        //
        dim = dataset.findDimension("lon");
        assertNotNull(dim);
        assertEquals("lon", dim.getShortName());
        
        // check type
        coordinateAxis = dataset.findCoordinateAxis(dim.getShortName());
        assertNotNull(coordinateAxis);
        assertTrue(coordinateAxis instanceof CoordinateAxis1D);
        binding = CoordinateVariable.suggestBinding((CoordinateAxis1D) coordinateAxis);
        assertNotNull(binding);
        assertSame(Float.class, binding);        
        cv= CoordinateVariable.create((CoordinateAxis1D) coordinateAxis);
        assertNotNull(cv);
        assertEquals("degrees_east", cv.getUnit());  
        
        
        //
        // pressure_levels_ozone is Double
        //
        dim = dataset.findDimension("nlo");
        assertNotNull(dim);
        assertEquals("nlo", dim.getShortName());
        
        // check type
        coordinateAxis = dataset.findCoordinateAxis(dim.getShortName());
        assertNotNull(coordinateAxis);
        assertTrue(coordinateAxis instanceof CoordinateAxis1D);
        binding = CoordinateVariable.suggestBinding((CoordinateAxis1D) coordinateAxis);
        assertNotNull(binding);
        assertSame(Double.class, binding);    
        cv= CoordinateVariable.create((CoordinateAxis1D) coordinateAxis);
        assertNotNull(cv);
        assertEquals("Pa", cv.getUnit());  
        
        //
        // pressure_levels_ozone is Double
        //
        dim = dataset.findDimension("nlt");
        assertNotNull(dim);
        assertEquals("nlt", dim.getShortName());
        
        // check type
        coordinateAxis = dataset.findCoordinateAxis(dim.getShortName());
        assertNotNull(coordinateAxis);
        assertTrue(coordinateAxis instanceof CoordinateAxis1D);
        binding = CoordinateVariable.suggestBinding((CoordinateAxis1D) coordinateAxis);
        assertNotNull(binding);
        assertSame(Double.class, binding);    
        cv= CoordinateVariable.create((CoordinateAxis1D) coordinateAxis);
        assertNotNull(cv);
        assertEquals("Pa", cv.getUnit());  
        
        dataset.close();
        
    }

}
