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
package org.geotools.gce.imagemosaic;

import static org.geotools.util.URLs.fileToUrl;

import it.geosolutions.imageio.utilities.ImageIOUtilities;
import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.media.jai.PlanarImage;
import javax.swing.JFrame;
import org.apache.commons.io.FileUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.UnknownFormat;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.CRS;
import org.geotools.test.TestData;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.junit.Assert;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/** @author Simone Giannecchini, GeoSolutions SAS */
final class TestUtils extends Assert {

    private TestUtils() {
        // TODO Auto-generated constructor stub
    }

    @SuppressWarnings("unchecked")
    static GridCoverage2D testCoverage(
            final ImageMosaicReader reader,
            GeneralParameterValue[] values,
            String title,
            final GridCoverage2D coverage,
            final Rectangle rect)
            throws FactoryException {
        final RenderedImage image = coverage.getRenderedImage();
        if (ImageMosaicReaderTest.INTERACTIVE) show(image, title);
        else PlanarImage.wrapRenderedImage(image).getTiles();

        if (values != null)
            for (GeneralParameterValue pv : values) {
                if (pv.getDescriptor()
                        .getName()
                        .equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName())) {

                    Parameter<GridGeometry2D> param = (Parameter<GridGeometry2D>) pv;
                    // check envelope if it has been requested

                    CoordinateReferenceSystem envCRS =
                            param.getValue().getEnvelope().getCoordinateReferenceSystem();
                    CoordinateReferenceSystem coverageCRS = coverage.getCoordinateReferenceSystem();
                    boolean equalsIgnoreMetadata = CRS.equalsIgnoreMetadata(envCRS, coverageCRS);
                    if (!equalsIgnoreMetadata) {
                        MathTransform destinationToSourceTransform =
                                CRS.findMathTransform(envCRS, coverageCRS, true);
                        equalsIgnoreMetadata =
                                destinationToSourceTransform != null
                                        && destinationToSourceTransform.isIdentity();
                    }
                    assertTrue(equalsIgnoreMetadata);
                }
            }
        if (rect != null) {
            assertEquals(image.getWidth(), rect.width);
            assertEquals(image.getHeight(), rect.height);
        }

        if (!ImageMosaicReaderTest.INTERACTIVE) {
            // dispose stuff
            coverage.dispose(true);
            reader.dispose();
        }
        return coverage;
    }

    /**
     * Tests the creation of a {@link GridCoverage2D} using the provided {@link ImageMosaicReader}
     * as well as the provided {@link ParameterValue}.
     *
     * @param reader to use for creating a {@link GridCoverage2D}.
     * @param value that control the actions to take for creating a {@link GridCoverage2D}.
     * @param title to print out as the head of the frame in case we visualize it.
     */
    static GridCoverage2D checkCoverage(
            final ImageMosaicReader reader, GeneralParameterValue[] values, String title)
            throws IOException {
        return checkCoverage(reader, values, title, null);
    }

    static GridCoverage2D checkCoverage(
            final ImageMosaicReader reader,
            GeneralParameterValue[] values,
            String title,
            Rectangle rect)
            throws IOException {
        // Test the coverage
        final GridCoverage2D coverage = getCoverage(reader, values, true);
        try {
            return testCoverage(reader, values, title, coverage, rect);
        } catch (FactoryException e) {
            throw new IOException(e);
        }
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
        return getFormat(testURL, hints);
    }

    /**
     * Tries to get an {@link AbstractGridFormat} for the provided URL.
     *
     * @param testURL points to a shapefile that is the index of a certain mosaic.
     * @param hints hints to be used while looking for a format.
     * @return a suitable {@link AbstractGridFormat}.
     */
    static AbstractGridFormat getFormat(URL testURL, Hints hints)
            throws NoSuchAuthorityCodeException, FactoryException {
        // Get format
        final AbstractGridFormat format = GridFormatFinder.findFormat(testURL, hints);
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

        //		final Hints hints= new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM,
        // CRS.decode("EPSG:4326", true));
        return getReader(testURL, format, null, true);
    }

    static ImageMosaicReader getReader(URL testURL, final AbstractGridFormat format, Hints hints) {
        //		Get a reader
        return getReader(testURL, format, hints, true);
    }

    static ImageMosaicReader getReader(
            URL testURL, final AbstractGridFormat format, Hints hints, final boolean checkForNull) {
        // Get a reader
        final ImageMosaicReader reader = (ImageMosaicReader) format.getReader(testURL, hints);
        if (checkForNull) Assert.assertNotNull(reader);
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

    public static ImageMosaicReader getReader(File directory) throws FactoryException {
        URL directoryURL = fileToUrl(directory);
        return getReader(directoryURL);
    }

    public static ImageMosaicReader getReader(URL directoryURL) throws FactoryException {
        final AbstractGridFormat format = TestUtils.getFormat(directoryURL);
        ImageMosaicReader reader = TestUtils.getReader(directoryURL, format);
        assertNotNull(reader);
        return reader;
    }

    public static File setupTestDirectory(Object caller, URL url, String testDirectoryName)
            throws IOException {
        File source = URLs.urlToFile(url);
        File directory = new File(TestData.file(caller, "."), testDirectoryName);
        if (directory.exists()) {
            FileUtils.deleteDirectory(directory);
        }
        FileUtils.copyDirectory(source, directory);
        return directory;
    }
}
