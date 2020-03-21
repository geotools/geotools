/*
 *    GeoTools - The Open Source Java GIS Toolkit http://geotools.org
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or modify it under
 *    the terms of the GNU Lesser General Public License as published by the Free
 *    Software Foundation; version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful, but WITHOUT
 *    ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *    FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 */
package org.geotools.renderer.lite.gridcoverage2d;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;
import javax.media.jai.Interpolation;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.gce.image.WorldImageReader;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.renderer.RenderListener;
import org.geotools.renderer.lite.GridCoverageRendererTest;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.RasterSymbolizerImpl;
import org.geotools.styling.StyleBuilder;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class GridCoverageRenderingOutOfViewTest {

    private final String rasterBase = "test_raster_NZTM";

    @Test
    public void test()
            throws IOException, URISyntaxException, MismatchedDimensionException,
                    NoSuchAuthorityCodeException, FactoryException {
        StreamingRenderer renderer = new StreamingRenderer();

        MapContent map = new MapContent();
        URL raster = getClass().getResource(rasterBase + ".png");
        GridCoverage2D gc = readGeoReferencedImageFile(new File(raster.toURI()));

        map.addLayer(loadGeoReferencedImageFile(gc, "test"));

        renderer.setMapContent(map);
        BufferedImage image = new BufferedImage(400, 300, BufferedImage.TYPE_INT_ARGB);
        Envelope env = new Envelope(1880352, 5825436, 1884352, 5828436);
        ReferencedEnvelope refenv = new ReferencedEnvelope(env, gc.getCoordinateReferenceSystem());

        AtomicReference<Exception> error = new AtomicReference<>();
        renderer.addRenderListener(
                new RenderListener() {

                    @Override
                    public void featureRenderer(SimpleFeature feature) {}

                    @Override
                    public void errorOccurred(Exception e) {
                        error.set(e);
                    }
                });
        renderer.paint(image.createGraphics(), new Rectangle(400, 300), refenv);
        map.dispose();

        assertNull(error.get());
    }

    @Test
    public void testOversamplingOnLayoutHelperArithmetic() throws Exception {

        URL coverageFile =
                org.geotools.test.TestData.url(GridCoverageRendererTest.class, "arithmetic.tif");
        GeoTiffReader reader = new GeoTiffReader(coverageFile);
        GridCoverage2D coverage = reader.read(null);
        CoordinateReferenceSystem crs = reader.getCoordinateReferenceSystem();

        // Setup params for a tiny bbox and oversampling
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(
                        244897.2157599071,
                        244897.2203125144,
                        202588.4763366661,
                        202588.4808892734,
                        crs);
        Rectangle screenSize = new Rectangle(0, 0, 200, 200);
        AffineTransform w2s =
                new AffineTransform(
                        43930.870040629205,
                        0.0,
                        0.0,
                        -43930.870040629205,
                        -1.0758547758860409E10,
                        8.899888225675163E9);
        GridCoverageRenderer renderer = new GridCoverageRenderer(crs, mapExtent, screenSize, w2s);

        RasterSymbolizer rasterSymbolizer = new RasterSymbolizerImpl();

        RenderedImage rendered =
                renderer.renderImage(
                        coverage,
                        rasterSymbolizer,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        Color.RED,
                        200,
                        200);
        // No java.lang.ArithmeticException: / by zero occurred
        assertTrue(rendered.getWidth() > 0);
        assertTrue(rendered.getHeight() > 0);

        coverage.dispose(true);
    }

    public Layer loadGeoReferencedImageFile(GridCoverage2D gc, String title)
            throws IOException, URISyntaxException {

        StyleBuilder sb = new StyleBuilder();
        RasterSymbolizer rs = sb.createRasterSymbolizer();
        return new GridCoverageLayer(gc, sb.createStyle(rs), "");
    }

    public GridCoverage2D readGeoReferencedImageFile(File f) throws IOException {
        WorldImageReader reader = null;
        try {
            reader = new WorldImageReader(f);
            return reader.read(null);
        } finally {
            if (reader != null) reader.dispose();
        }
    }
}
