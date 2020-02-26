/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagepyramid;

import it.geosolutions.imageio.utilities.ImageIOUtilities;
import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.net.URL;
import javax.media.jai.PlanarImage;
import javax.swing.JFrame;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.UnknownFormat;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.Hints;
import org.junit.Assert;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/** @author Simone Giannecchini, GeoSolutions SAS */
final class TestUtils extends Assert {

    private TestUtils() {}

    @SuppressWarnings("unchecked")
    static void testCoverage(
            final ImageMosaicReader reader,
            GeneralParameterValue[] values,
            String title,
            final GridCoverage2D coverage,
            final Rectangle rect) {
        final RenderedImage image = coverage.getRenderedImage();
        PlanarImage.wrapRenderedImage(image).getTiles();

        if (values != null)
            for (GeneralParameterValue pv : values) {
                if (pv.getDescriptor()
                        .getName()
                        .equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName())) {

                    Parameter<GridGeometry2D> param = (Parameter<GridGeometry2D>) pv;
                    // check envelope if it has been requested
                    assertTrue(
                            CRS.equalsIgnoreMetadata(
                                    param.getValue().getEnvelope().getCoordinateReferenceSystem(),
                                    coverage.getCoordinateReferenceSystem()));
                }
            }
        if (rect != null) {
            assertEquals(image.getWidth(), rect.width);
            assertEquals(image.getHeight(), rect.height);
        }

        // dispose stuff
        coverage.dispose(true);
        reader.dispose();
    }

    /**
     * Tests the creation of a {@link GridCoverage2D} using the provided {@link ImageMosaicReader}
     * as well as the provided {@link ParameterValue}.
     *
     * @param reader to use for creating a {@link GridCoverage2D}.
     * @param value that control the actions to take for creating a {@link GridCoverage2D}.
     * @param title to print out as the head of the frame in case we visualize it.
     */
    static void checkCoverage(
            final ImageMosaicReader reader, GeneralParameterValue[] values, String title)
            throws IOException {
        checkCoverage(reader, values, title, null);
    }

    static void checkCoverage(
            final ImageMosaicReader reader,
            GeneralParameterValue[] values,
            String title,
            Rectangle rect)
            throws IOException {
        // Test the coverage
        final GridCoverage2D coverage = getCoverage(reader, values, true);
        testCoverage(reader, values, title, coverage, rect);
    }

    static GridCoverage2D getCoverage(
            final ImageMosaicReader reader,
            GeneralParameterValue[] values,
            final boolean checkForNull)
            throws IOException {
        final GridCoverage2D coverage = (GridCoverage2D) reader.read(values);
        if (checkForNull) {
            Assert.assertNotNull(coverage);
        }
        return coverage;
    }

    /**
     * Tries to get an {@link AbstractGridFormat} for the provided URL.
     *
     * @param testURL points to a shapefile that is the index of a certain mosaic.
     * @return a suitable {@link AbstractGridFormat}.
     */
    static AbstractGridFormat getFormat(URL testURL)
            throws NoSuchAuthorityCodeException, FactoryException {

        final Hints hints =
                new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));
        // Get format
        final AbstractGridFormat format =
                (AbstractGridFormat) GridFormatFinder.findFormat(testURL, hints);
        Assert.assertNotNull(format);
        Assert.assertFalse("UknownFormat", format instanceof UnknownFormat);
        return format;
    }

    /**
     * returns an {@link AbstractGridCoverage2DReader} for the provided {@link URL} and for the
     * providede {@link AbstractGridFormat}.
     *
     * @param testURL points to a valid object to create an {@link AbstractGridCoverage2DReader}
     *     for.
     * @param format to use for instantiating such a reader.
     * @return a suitable {@link ImageMosaicReader}.
     */
    static ImageMosaicReader getReader(URL testURL, final AbstractGridFormat format)
            throws NoSuchAuthorityCodeException, FactoryException {

        // final Hints hints= new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM,
        // CRS.decode("EPSG:4326", true));
        return getReader(testURL, format, null);
    }

    static ImageMosaicReader getReader(URL testURL, final AbstractGridFormat format, Hints hints) {
        // Get a reader
        final ImageMosaicReader reader = (ImageMosaicReader) format.getReader(testURL, hints);
        Assert.assertNotNull(reader);
        return reader;
    }

    /**
     * Shows the provided {@link RenderedImage} ina {@link JFrame} using the provided <code>title
     * </code> as the frame's title.
     *
     * @param image to show.
     * @param title to use.
     */
    static void show(RenderedImage image, String title) {
        ImageIOUtilities.visualize(image, title);
    }
}
