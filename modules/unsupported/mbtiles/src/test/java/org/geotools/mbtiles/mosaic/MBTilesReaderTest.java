package org.geotools.mbtiles.mosaic;

import static org.junit.Assert.assertEquals;

import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.IOException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.parameter.Parameter;
import org.geotools.util.URLs;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;

public class MBTilesReaderTest {

    @Test
    public void testZoomlevel2() throws IOException {
        MBTilesReader reader =
                new MBTilesReader(getClass().getResource("world_lakes.mbtiles"), null);

        GeneralParameterValue[] parameters = new GeneralParameterValue[1];
        GridGeometry2D gg =
                new GridGeometry2D(
                        new GridEnvelope2D(new Rectangle(500, 500)),
                        new ReferencedEnvelope(0, 180.0, -85.0, 0, MBTilesReader.WGS_84));
        parameters[0] = new Parameter<GridGeometry2D>(AbstractGridFormat.READ_GRIDGEOMETRY2D, gg);
        GridCoverage2D gc = reader.read(parameters);
        RenderedImage img = gc.getRenderedImage();
        assertEquals(0, gc.getEnvelope().getMinimum(0), 0.01);
        assertEquals(-20037508.34, gc.getEnvelope().getMinimum(1), 0.01);
        assertEquals(20037508.34, gc.getEnvelope().getMaximum(0), 0.01);
        assertEquals(0, gc.getEnvelope().getMaximum(1), 0.01);
        assertEquals(512, img.getWidth());
        assertEquals(512, img.getHeight());
        // ImageIO.write(img, "png", URLs.urlToFile(getClass().getResource("world_lakes.png")));
        ImageAssert.assertEquals(
                URLs.urlToFile(getClass().getResource("world_lakes.png")), img, 250);
    }

    @Test
    public void testZoomlevel3() throws IOException {
        MBTilesReader reader =
                new MBTilesReader(getClass().getResource("world_lakes.mbtiles"), null);

        GeneralParameterValue[] parameters = new GeneralParameterValue[1];
        GridGeometry2D gg =
                new GridGeometry2D(
                        new GridEnvelope2D(new Rectangle(500, 500)),
                        new ReferencedEnvelope(0, 90.0, -85.0, 0, MBTilesReader.WGS_84));
        parameters[0] = new Parameter<GridGeometry2D>(AbstractGridFormat.READ_GRIDGEOMETRY2D, gg);
        GridCoverage2D gc = reader.read(parameters);
        RenderedImage img = gc.getRenderedImage();
        assertEquals(0, gc.getEnvelope().getMinimum(0), 0.01);
        assertEquals(-20037508.34, gc.getEnvelope().getMinimum(1), 0.01);
        assertEquals(15028131.25, gc.getEnvelope().getMaximum(0), 0.01);
        assertEquals(0, gc.getEnvelope().getMaximum(1), 0.01);
        assertEquals(768, img.getWidth());
        assertEquals(1024, img.getHeight());
    }

    @Test
    public void testZoomlevel4() throws IOException {
        MBTilesReader reader =
                new MBTilesReader(getClass().getResource("world_lakes.mbtiles"), null);

        GeneralParameterValue[] parameters = new GeneralParameterValue[1];
        GridGeometry2D gg =
                new GridGeometry2D(
                        new GridEnvelope2D(new Rectangle(500, 500)),
                        new ReferencedEnvelope(0, 45.0, -85.0, 0, MBTilesReader.WGS_84));
        parameters[0] = new Parameter<GridGeometry2D>(AbstractGridFormat.READ_GRIDGEOMETRY2D, gg);
        GridCoverage2D gc = reader.read(parameters);
        RenderedImage img = gc.getRenderedImage();
        assertEquals(0, gc.getEnvelope().getMinimum(0), 0.01);
        assertEquals(-20037508.34, gc.getEnvelope().getMinimum(1), 0.01);
        assertEquals(7514065.62, gc.getEnvelope().getMaximum(0), 0.01);
        assertEquals(0, gc.getEnvelope().getMaximum(1), 0.01);
        assertEquals(768, img.getWidth());
        assertEquals(2048, img.getHeight());
    }
}
