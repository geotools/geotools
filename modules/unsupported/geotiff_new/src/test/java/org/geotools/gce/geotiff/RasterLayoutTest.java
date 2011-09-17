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
 *
 */
package org.geotools.gce.geotiff;

import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BandedSampleModel;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.util.HashMap;
import java.util.Map;

import javax.media.jai.TiledImage;

import org.geotools.coverage.grid.RasterLayout;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author Daniele Romagnoli, GeoSolutions SAS
 *
 */
public class RasterLayoutTest extends Assert{

    @Test
    public void testRasterLayout(){
        RasterLayout rasterLayout = new RasterLayout(10, 10, 100, 100);
        assertEquals(rasterLayout.getHeight(), 100);
        assertEquals(rasterLayout.getWidth(), 100);
        assertEquals(rasterLayout.getMinX(), 10);
        assertEquals(rasterLayout.getMinY(), 10);
        assertEquals(rasterLayout.getTileGridXOffset(), 0);
        assertEquals(rasterLayout.getTileGridYOffset(), 0);
        assertEquals(rasterLayout.getTileWidth(), 0);
        assertEquals(rasterLayout.getTileHeight(), 0);
        
        SampleModel sm = new BandedSampleModel(DataBuffer.TYPE_BYTE, 50, 50, 3);
        ColorModel cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        TiledImage ri = new TiledImage(10, 10, 50, 50, 5, 5, sm, cm);
        
        RasterLayout rasterLayoutToBeCloned = new RasterLayout(ri);
        RasterLayout rasterLayout3 = new RasterLayout(ri);
        RasterLayout rasterLayout2 = (RasterLayout) rasterLayoutToBeCloned.clone();
        
        assertTrue(rasterLayout2.equals(rasterLayout3));
        
        assertEquals(rasterLayout2.getHeight(), 50);
        assertEquals(rasterLayout2.getWidth(), 50);
        assertEquals(rasterLayout2.getMinX(), 10);
        assertEquals(rasterLayout2.getMinY(), 10);
        assertEquals(rasterLayout2.getTileGridXOffset(), 5);
        assertEquals(rasterLayout2.getTileGridYOffset(), 5);
        assertEquals(rasterLayout2.getTileWidth(), 50);
        assertEquals(rasterLayout2.getTileHeight(), 50);
        
        rasterLayout2.toString();
        
        Map <RenderedImage, RasterLayout> map = new HashMap<RenderedImage, RasterLayout>();
        map.put(ri, rasterLayout2);
        assertTrue(map.containsKey(ri));
        
        
        
        
    }
    
}
