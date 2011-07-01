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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import junit.framework.Assert;
import junit.textui.TestRunner;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.IIOMetadataDumper;
import org.geotools.data.DataSourceException;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.test.TestData;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * Testing {@link GeoTiffReader} as well as {@link IIOMetadataDumper}.
 * 
 * @author Simone Giannecchini
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/geotiff_new/src/test/java/org/geotools/gce/geotiff/GeoTiffReaderTest.java $
 */
public class GeoTiffReaderTest extends Assert {
    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(GeoTiffReaderTest.class.toString());

    /**
     * testReader
     * 
     * @throws IllegalArgumentException
     * @throws IOException
     * @throws NoSuchAuthorityCodeException
     */
    @Test
    public void testReader() throws IllegalArgumentException, IOException,
            NoSuchAuthorityCodeException {

        final File file = TestData.file(this, ".");
        final File files[] = file.listFiles();
        final int numFiles = files.length;
        final AbstractGridFormat format = new GeoTiffFormat();
        for (int i = 0; i < numFiles; i++) {
            StringBuffer buffer = new StringBuffer();
            final String path = files[i].getAbsolutePath().toLowerCase();
            if (!path.endsWith("tif") && !path.endsWith("tiff"))
                continue;
            if (path.contains("ovr.tif"))
                continue;
            buffer.append(files[i].getAbsolutePath()).append("\n");
            Object o;
            if (i % 2 == 0)
                // testing file
                o = files[i];
            else
                // testing url
                o = files[i].toURI().toURL();
            if (format.accepts(o)) {
                buffer.append("ACCEPTED").append("\n");

                // getting a reader
                GeoTiffReader reader = new GeoTiffReader(o, new Hints(
                        Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));

                if (reader != null) {

                    // reading the coverage
                    GridCoverage2D coverage = (GridCoverage2D) reader.read(null);

                    // Crs and envelope
                    if (TestData.isInteractiveTest()) {
                        buffer.append("CRS: ")
                                .append(coverage.getCoordinateReferenceSystem2D().toWKT())
                                .append("\n");
                        buffer.append("GG: ").append(coverage.getGridGeometry().toString())
                                .append("\n");
                    }

                    // showing it
                    if (TestData.isInteractiveTest())
                        coverage.show();
                    else
                        coverage.getRenderedImage().getData();

                }

            } else
                buffer.append("NOT ACCEPTED").append("\n");
            if (TestData.isInteractiveTest())
                LOGGER.info(buffer.toString());

        }

    }

    @Test
    @Ignore
    public void testExternalOverviews() throws Exception {
        final File file = TestData.file(GeoTiffReaderTest.class, "ovr.tif");
        assertNotNull(file);
        assertEquals(true, file.exists());
        GeoTiffReaderTester reader = new GeoTiffReaderTester(file);
        final int nOvrs = reader.getNumOverviews();
        LOGGER.info("Number of external overviews: " + nOvrs);
        assertEquals(4, nOvrs);
        double[][] overviewResolutions = reader.getOverviewResolutions();
        assertEquals(overviewResolutions.length, 4);

        final ParameterValue<GridGeometry2D> gg = AbstractGridFormat.READ_GRIDGEOMETRY2D
                .createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();
        dim.setSize(reader.getOriginalGridRange().getSpan(0) / 4.0, reader.getOriginalGridRange()
                .getSpan(1) / 4.0);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));

        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] { gg });
        RenderedImage image = coverage.getRenderedImage();
        assertEquals(image.getWidth(), 32);
        assertEquals(image.getHeight(), 32);

        final double delta = 0.00001;
        assertEquals(overviewResolutions[0][0], 10, delta);
        assertEquals(overviewResolutions[0][1], 10, delta);

        assertEquals(overviewResolutions[1][0], 20, delta);
        assertEquals(overviewResolutions[1][1], 20, delta);

        assertEquals(overviewResolutions[2][0], 40, delta);
        assertEquals(overviewResolutions[2][1], 40, delta);

        assertEquals(overviewResolutions[3][0], 80, delta);
        assertEquals(overviewResolutions[3][1], 80, delta);

    }

    class GeoTiffReaderTester extends GeoTiffReader {
        public GeoTiffReaderTester(Object input) throws DataSourceException {
            super(input);
        }

        int getNumOverviews() {
            return numOverviews;
        }

        double[][] getOverviewResolutions() {
            return overViewResolutions;
        }
    }
}
