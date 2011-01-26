/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.resources.coverage;

import java.awt.Rectangle;

import javax.imageio.ImageReadParam;

import junit.framework.Assert;

import org.junit.Test;


/**
 * Tests {@link CoverageUtilities}.
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.6.x/modules/library/coverage/src/test/java/org/geotools/resources/coverage/CoverageUtilitiesTest.java $
 */
public final class CoverageUtilitiesTest {
    /**
     * Tests the checkEmptySourceRegion method.
     */
    @Test
    public void testCheckEmptySourceRegion() {
        final ImageReadParam params = new ImageReadParam();
        Rectangle sourceRegion = new Rectangle(300, 300, 700, 700);
        params.setSourceRegion(sourceRegion);
        Assert.assertEquals(sourceRegion.x, 300);
        Assert.assertEquals(sourceRegion.y, 300);
        Assert.assertEquals(sourceRegion.height, 700);
        Assert.assertEquals(sourceRegion.width, 700);
        
        final Rectangle intersecting = new Rectangle(400, 400, 900, 900);
        boolean isEmpty = CoverageUtilities.checkEmptySourceRegion(params, intersecting);
        
        Assert.assertFalse(isEmpty);
        final Rectangle intersection = params.getSourceRegion();
        Assert.assertEquals(intersection.x, 400);
        Assert.assertEquals(intersection.y, 400);
        Assert.assertEquals(intersection.height, 600);
        Assert.assertEquals(intersection.width, 600);
       
        final Rectangle intersecting2 = new Rectangle(0, 0, 300, 300);
        isEmpty = CoverageUtilities.checkEmptySourceRegion(params, intersecting2);
        Assert.assertTrue(isEmpty);
    }

}
