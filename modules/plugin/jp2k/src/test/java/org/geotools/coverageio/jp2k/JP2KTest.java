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
 *
 */
package org.geotools.coverageio.jp2k;

import it.geosolutions.util.KakaduUtilities;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.geotools.util.factory.Hints;
import org.junit.AssumptionViolatedException;
import org.junit.Test;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 *     <p>Testing {@link org.geotools.coverageio.jp2k.JP2KReader}
 */
public final class JP2KTest extends BaseJP2K {

    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(JP2KTest.class);

    /** Creates a new instance of JP2KTest */
    public JP2KTest() {}

    @Test
    public void testTiledImageReadMT() throws Exception {
        if (!testingEnabled()) {
            return;
        }

        File file = getInputFile("sample.jp2");

        // //
        //
        // Setting several parameters
        //
        // //
        final Hints hints = new Hints(Hints.OVERVIEW_POLICY, OverviewPolicy.getDefaultPolicy());
        final JP2KReader reader = new JP2KReader(file, hints);
        final ParameterValue<GridGeometry2D> gg = JP2KFormat.READ_GRIDGEOMETRY2D.createValue();
        final ParameterValue<Boolean> useMT = JP2KFormat.USE_MULTITHREADING.createValue();
        final ParameterValue<Boolean> useJAI = JP2KFormat.USE_JAI_IMAGEREAD.createValue();
        final ParameterValue<String> tileSize = JP2KFormat.SUGGESTED_TILE_SIZE.createValue();
        final ParameterValue<Color> transparentColor =
                JP2KFormat.INPUT_TRANSPARENT_COLOR.createValue();
        transparentColor.setValue(new Color(0, 0, 0));
        tileSize.setValue("128,128");

        useMT.setValue(false);
        useJAI.setValue(true);
        final GeneralEnvelope oldEnvelope = reader.getOriginalEnvelope();
        gg.setValue(new GridGeometry2D(reader.getOriginalGridRange(), oldEnvelope));

        // //
        //
        // Reading
        //
        // //
        final GridCoverage2D gc =
                (GridCoverage2D)
                        reader.read(
                                new GeneralParameterValue[] {
                                    gg, useJAI, useMT, tileSize, transparentColor
                                });
        assertNotNull(gc);
        forceDataLoading(gc);

        final MathTransform g2w = reader.getRaster2Model();
        final AffineTransform at = (AffineTransform) g2w;
        assertEquals(at.getScaleX(), 0.9, DELTA);
        assertEquals(at.getScaleY(), -0.9, DELTA);
        assertEquals(at.getTranslateX(), -179.55, DELTA);
        assertEquals(at.getTranslateY(), 89.55, DELTA);
        assertEquals(gc.getRenderedImage().getWidth(), 400);
        assertEquals(gc.getRenderedImage().getHeight(), 200);
    }

    @Test
    public void test() throws Exception {
        if (!testingEnabled()) {
            return;
        }

        File file = getInputFile("bogota.jp2");

        // //
        //
        // Testing Direct read
        //
        // //

        final AbstractGridCoverage2DReader reader = new JP2KReader(file);
        final int nCov = reader.getGridCoverageCount();
        assertEquals(nCov, 1);

        final ParameterValue<GridGeometry2D> gg = JP2KFormat.READ_GRIDGEOMETRY2D.createValue();
        final ParameterValue<Boolean> useJAI = JP2KFormat.USE_JAI_IMAGEREAD.createValue();
        useJAI.setValue(false);
        final GeneralEnvelope oldEnvelope = reader.getOriginalEnvelope();
        checkReader(reader);
        gg.setValue(new GridGeometry2D(reader.getOriginalGridRange(), oldEnvelope));
        GridCoverage2D gc = (GridCoverage2D) reader.read(new GeneralParameterValue[] {gg, useJAI});
        assertNotNull(gc);
        forceDataLoading(gc);

        // //
        //
        // Testing simple imageRead
        //
        // //

        useJAI.setValue(true);
        final Envelope wgs84Envelope = CRS.transform(oldEnvelope, DefaultGeographicCRS.WGS84);
        gg.setValue(new GridGeometry2D(reader.getOriginalGridRange(), wgs84Envelope));
        gc = (GridCoverage2D) reader.read(new GeneralParameterValue[] {gg, useJAI});
        assertNotNull(gc);
        forceDataLoading(gc);
    }

    @Test
    public void testGMLJP2Projected() throws Exception {
        // GMLJP2 parsing is available only when Kakadu is enabled
        if (!testingEnabled() || !KakaduUtilities.isKakaduAvailable()) {
            throw new AssumptionViolatedException("Testing not enabled or Kakadu not available");
        }

        File file = getInputFile("bogota_gml.jp2");

        // check georeferencing has been loaded properly
        final AbstractGridCoverage2DReader reader = new JP2KReader(file);
        final int nCov = reader.getGridCoverageCount();
        assertEquals(nCov, 1);
        CoordinateReferenceSystem expected = CRS.decode("EPSG:21892");
        assertTrue(CRS.equalsIgnoreMetadata(expected, reader.getCoordinateReferenceSystem()));
        GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final double EPS = 1e-6;
        assertEquals(440720, envelope.getMinimum(0), EPS);
        assertEquals(471440, envelope.getMaximum(0), EPS);
        assertEquals(69280, envelope.getMinimum(1), EPS);
        assertEquals(100000, envelope.getMaximum(1), EPS);
    }

    @Test
    public void testGMLJP2Geographic() throws Exception {
        // GMLJP2 parsing is available only when Kakadu is enabled
        if (!testingEnabled() || !KakaduUtilities.isKakaduAvailable()) {
            throw new AssumptionViolatedException("Testing not enabled or Kakadu not available");
        }

        File file = getInputFile("sample_gml.jp2");

        // check georeferencing has been loaded properly
        final AbstractGridCoverage2DReader reader = new JP2KReader(file);
        final int nCov = reader.getGridCoverageCount();
        assertEquals(nCov, 1);
        CoordinateReferenceSystem expected = CRS.decode("EPSG:4326", true);
        assertTrue(CRS.equalsIgnoreMetadata(expected, reader.getCoordinateReferenceSystem()));
        GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final double EPS = 1e-6;
        assertEquals(-180, envelope.getMinimum(0), EPS);
        assertEquals(180, envelope.getMaximum(0), EPS);
        assertEquals(-90, envelope.getMinimum(1), EPS);
        assertEquals(90, envelope.getMaximum(1), EPS);
    }

    private File getInputFile(String path) throws IOException {
        File file = null;
        try {
            file = TestData.file(this, path);
        } catch (FileNotFoundException fnfe) {
            throw new AssumptionViolatedException(
                    "test-data not found: " + file + "\nTests are skipped");
        }
        final JP2KFormat format = factorySpi.createFormat();
        assertTrue(format.accepts(file));
        return file;
    }
}
