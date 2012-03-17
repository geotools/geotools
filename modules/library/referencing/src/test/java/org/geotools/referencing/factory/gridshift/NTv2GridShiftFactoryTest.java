/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.gridshift;

import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.opengis.referencing.FactoryException;

import au.com.objectix.jgridshift.GridShiftFile;


/**
 * Unit tests for {@link NTv2GridShiftFactory} public methods
 * 
 * @author Oscar Fonts
 */
public class NTv2GridShiftFactoryTest {

    private static final URL TEST_GRID = NTv2GridShiftFactoryTest.class.getResource("BALR2009.gsb");
    private static final URL INEXISTENT_GRID = NTv2GridShiftFactoryTest.class.getResource("this_NTv2_grid_does_not_exist");
    private static final URL MALFORMED_GRID = NTv2GridShiftFactoryTest.class.getResource("malformedNTv2grid.gsb");

    private NTv2GridShiftFactory factory;

    /**
     * Instantiates the test {@link NTv2GridShiftFactory}
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        factory = new NTv2GridShiftFactory();
    }
    
    
    /**
     * Test method for {@link org.geotools.referencing.factory.gridshift.NTv2GridShiftFactory#isNTv2Grid(java.lang.String)}.
     */
    @Test
    public void testIsNTv2GridAvailable() {
        assertFalse(factory.isNTv2Grid(null));
        assertFalse(factory.isNTv2Grid(INEXISTENT_GRID)); // Will log a FINEST message
        assertFalse(factory.isNTv2Grid(MALFORMED_GRID));  // Will log a WARNING message
        assertTrue(factory.isNTv2Grid(TEST_GRID));
    }
    
    /**
     * Test method for {@link org.geotools.referencing.factory.gridshift.NTv2GridShiftFactory#createNTv2Grid(java.lang.String)}.
     */
    @Test
    public void testCreateNTv2Grid() {
        
        // Try to create a null grid
        boolean factoryExceptionThrown = false;
        try {
            factory.createNTv2Grid(null);
        } catch (FactoryException e) {
            factoryExceptionThrown = true;
        }
        assertTrue(factoryExceptionThrown);

        // Try to create a grid from an inexistent resource
        factoryExceptionThrown = false;
        try {
            factory.createNTv2Grid(INEXISTENT_GRID); // Will log a FINEST message
        } catch (FactoryException e) {
            factoryExceptionThrown = true;
        }
        assertTrue(factoryExceptionThrown);
        
        // Try to create a grid from a corrupt NTv2 grid file
        factoryExceptionThrown = false;
        try {
            factory.createNTv2Grid(MALFORMED_GRID);  // Will log a SEVERE message
        } catch (FactoryException e) {
            factoryExceptionThrown = true;
        }
        assertTrue(factoryExceptionThrown);
    
        // Create a grid from the test file
        factoryExceptionThrown = false;
        GridShiftFile grid = null;
        try {
            grid = factory.createNTv2Grid(TEST_GRID);
        } catch (FactoryException e) {
            factoryExceptionThrown = true;
        }
        assertFalse(factoryExceptionThrown);
        assertNotNull(grid);
        assertTrue(grid.isLoaded());
    }

}
