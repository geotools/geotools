/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer3d.utils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * BoundingRectangleImpl Tester.
 *
 * @author Hans Häggström
 */
public class TestBoundingRectangleImpl
        extends TestCase
{

    //======================================================================
    // Private Fields

    private BoundingRectangleImpl myBoundingRectangle;

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // Constructors

    public TestBoundingRectangleImpl( String name )
    {
        super( name );
    }

    //----------------------------------------------------------------------
    // Static Methods

    public static Test suite()
    {
        return new TestSuite( TestBoundingRectangleImpl.class );
    }

    //----------------------------------------------------------------------
    // Test Methods

    public void testGetOppositeSubquadrant() throws Exception
    {
        assertOpposingSubquadrantCorrect( 0, 3 );
        assertOpposingSubquadrantCorrect( 1, 2 );
        assertOpposingSubquadrantCorrect( 2, 1 );
        assertOpposingSubquadrantCorrect( 3, 0 );
    }

    //----------------------------------------------------------------------
    // Other Public Methods

    public void setUp() throws Exception
    {
        super.setUp();

        myBoundingRectangle = new BoundingRectangleImpl( 10, 20, 30, 40 );
    }


    public void tearDown() throws Exception
    {
        super.tearDown();

        myBoundingRectangle = null;
    }

    //======================================================================
    // Private Methods

    private void assertOpposingSubquadrantCorrect( final int subquadrant, final int expected )
    {
        assertEquals( "Opposing subquadrant should be correct for input '" + subquadrant + "' ",
                      expected, myBoundingRectangle.getOppositeSubquadrant( subquadrant ) );
    }

}
