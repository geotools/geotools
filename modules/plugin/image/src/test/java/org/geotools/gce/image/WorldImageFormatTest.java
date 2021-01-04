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
package org.geotools.gce.image;

import org.junit.Assert;
import org.junit.Test;

/** @author rgoulds */
public class WorldImageFormatTest {

    @Test
    public void testGetWorldExtension() {
        Assert.assertTrue(WorldImageFormat.getWorldExtension("png").contains(".pgw"));
        Assert.assertTrue(WorldImageFormat.getWorldExtension("gif").contains(".gfw"));
        Assert.assertTrue(WorldImageFormat.getWorldExtension("jpg").contains(".jgw"));
        Assert.assertTrue(WorldImageFormat.getWorldExtension("jpeg").contains(".jgw"));
        Assert.assertTrue(WorldImageFormat.getWorldExtension("tif").contains(".tfw"));
        Assert.assertTrue(WorldImageFormat.getWorldExtension("tiff").contains(".tfw"));
    }
}
