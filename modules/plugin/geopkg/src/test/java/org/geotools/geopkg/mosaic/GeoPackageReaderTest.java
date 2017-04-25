/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg.mosaic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.IOException;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.data.DataUtilities;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geopkg.GeoPackageTest;
import org.geotools.geopkg.mosaic.GeoPackageReader;
import org.geotools.image.test.ImageAssert;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


public class GeoPackageReaderTest {
    
    private final CoordinateReferenceSystem WGS_84;

    public GeoPackageReaderTest() {
        try {
            WGS_84 = CRS.decode("EPSG:4326", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testZoomlevel0() throws IOException {
        GeoPackageReader reader = new GeoPackageReader(GeoPackageTest.class.getResource("Blue_Marble.gpkg"), null);

        GeneralParameterValue[] parameters = new GeneralParameterValue[1];
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(new Rectangle(1000,500)), new ReferencedEnvelope(-160,160.0,-80.0,80,WGS_84));
        parameters[0] = new Parameter<GridGeometry2D>(AbstractGridFormat.READ_GRIDGEOMETRY2D, gg);
        GridCoverage2D gc = reader.read("bluemarble_tif_tiles", parameters);
        RenderedImage img = gc.getRenderedImage();
        assertEquals(-180, gc.getEnvelope().getMinimum(0),0.01);
        assertEquals(-90, gc.getEnvelope().getMinimum(1),0.01);
        assertEquals(180, gc.getEnvelope().getMaximum(0),0.01);
        assertEquals(90, gc.getEnvelope().getMaximum(1),0.01);
        assertEquals(1536, img.getWidth());
        assertEquals(768, img.getHeight());

        //test CRS is consistent now
        assertTrue(CRS.equalsIgnoreMetadata(gc.getCoordinateReferenceSystem(), gc.getEnvelope().getCoordinateReferenceSystem()));

        //ImageIO.write(img, "png", DataUtilities.urlToFile(getClass().getResource("world_lakes.png")));
        ImageAssert.assertEquals(DataUtilities.urlToFile(GeoPackageTest.class.getResource("bluemarble.jpeg")), img, 250);
    }

    @Test
    public void testZoomlevel1() throws IOException {
        GeoPackageReader reader = new GeoPackageReader(GeoPackageTest.class.getResource("Blue_Marble.gpkg"), null);

        GeneralParameterValue[] parameters = new GeneralParameterValue[1];
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(new Rectangle(1000,500)), new ReferencedEnvelope(0,160,0,80,WGS_84));
        parameters[0] = new Parameter<GridGeometry2D>(AbstractGridFormat.READ_GRIDGEOMETRY2D, gg);
        GridCoverage2D gc = reader.read("bluemarble_tif_tiles", parameters);
        RenderedImage img = gc.getRenderedImage();
        assertEquals(0, gc.getEnvelope().getMinimum(0),0.01);
        assertEquals(0, gc.getEnvelope().getMinimum(1),0.01);
        assertEquals(180, gc.getEnvelope().getMaximum(0),0.01);
        assertEquals(90, gc.getEnvelope().getMaximum(1),0.01);
        assertEquals(1536, img.getWidth());
        assertEquals(768, img.getHeight());
    }

    @Test
    public void testZoomlevel2() throws IOException {
        GeoPackageReader reader = new GeoPackageReader(GeoPackageTest.class.getResource("Blue_Marble.gpkg"), null);
        
        GeneralParameterValue[] parameters = new GeneralParameterValue[1];
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(new Rectangle(1000,500)), new ReferencedEnvelope(0,80,0,40,WGS_84));
        parameters[0] = new Parameter<GridGeometry2D>(AbstractGridFormat.READ_GRIDGEOMETRY2D, gg);
        GridCoverage2D gc = reader.read("bluemarble_tif_tiles", parameters);
        RenderedImage img = gc.getRenderedImage();
        assertEquals(0, gc.getEnvelope().getMinimum(0),0.01);
        assertEquals(0, gc.getEnvelope().getMinimum(1),0.01);
        assertEquals(90, gc.getEnvelope().getMaximum(0),0.01);
        assertEquals(45, gc.getEnvelope().getMaximum(1),0.01);
        assertEquals(1536, img.getWidth());
        assertEquals(768, img.getHeight());
    }
    
    @Test
    public void testZoomlevel3() throws IOException {
        GeoPackageReader reader = new GeoPackageReader(GeoPackageTest.class.getResource("Blue_Marble.gpkg"), null);
        
        GeneralParameterValue[] parameters = new GeneralParameterValue[1];
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(new Rectangle(1000,500)), new ReferencedEnvelope(0,40,0,20,WGS_84));
        parameters[0] = new Parameter<GridGeometry2D>(AbstractGridFormat.READ_GRIDGEOMETRY2D, gg);
        GridCoverage2D gc = reader.read("bluemarble_tif_tiles", parameters);
        RenderedImage img = gc.getRenderedImage();
        assertEquals(0, gc.getEnvelope().getMinimum(0),0.01);
        assertEquals(0, gc.getEnvelope().getMinimum(1),0.01);
        assertEquals(45, gc.getEnvelope().getMaximum(0),0.01);
        assertEquals(22.5, gc.getEnvelope().getMaximum(1),0.01);
        assertEquals(1536, img.getWidth());
        assertEquals(768, img.getHeight());
    }
    
    @Test
    public void testZoomlevel4() throws IOException {
        GeoPackageReader reader = new GeoPackageReader(GeoPackageTest.class.getResource("Blue_Marble.gpkg"), null);
        
        GeneralParameterValue[] parameters = new GeneralParameterValue[1];
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(new Rectangle(1000,500)), new ReferencedEnvelope(0,20,0,10,WGS_84));
        parameters[0] = new Parameter<GridGeometry2D>(AbstractGridFormat.READ_GRIDGEOMETRY2D, gg);
        GridCoverage2D gc = reader.read("bluemarble_tif_tiles", parameters);
        RenderedImage img = gc.getRenderedImage();
        assertEquals(0, gc.getEnvelope().getMinimum(0),0.01);
        assertEquals(0, gc.getEnvelope().getMinimum(1),0.01);
        assertEquals(22.5, gc.getEnvelope().getMaximum(0),0.01);
        assertEquals(11.25, gc.getEnvelope().getMaximum(1),0.01);
        assertEquals(1536, img.getWidth());
        assertEquals(768, img.getHeight());
    }

}
