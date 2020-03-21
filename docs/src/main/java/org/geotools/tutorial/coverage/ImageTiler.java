/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
// docs start prelim
package org.geotools.tutorial.coverage;

import java.io.File;
import java.io.IOException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.processing.Operations;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.Arguments;
import org.geotools.util.factory.Hints;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Simple tiling of a coverage based simply on the number vertical/horizontal tiles desired and
 * subdividing the geographic envelope. Uses coverage processing operations.
 */
public class ImageTiler {

    private final int NUM_HORIZONTAL_TILES = 16;
    private final int NUM_VERTICAL_TILES = 8;

    private Integer numberOfHorizontalTiles = NUM_HORIZONTAL_TILES;
    private Integer numberOfVerticalTiles = NUM_VERTICAL_TILES;
    private Double tileScale;
    private File inputFile;
    private File outputDirectory;

    private String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

    public Integer getNumberOfHorizontalTiles() {
        return numberOfHorizontalTiles;
    }

    public void setNumberOfHorizontalTiles(Integer numberOfHorizontalTiles) {
        this.numberOfHorizontalTiles = numberOfHorizontalTiles;
    }

    public Integer getNumberOfVerticalTiles() {
        return numberOfVerticalTiles;
    }

    public void setNumberOfVerticalTiles(Integer numberOfVerticalTiles) {
        this.numberOfVerticalTiles = numberOfVerticalTiles;
    }

    public File getInputFile() {
        return inputFile;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public Double getTileScale() {
        return tileScale;
    }

    public void setTileScale(Double tileScale) {
        this.tileScale = tileScale;
    }

    // docs end prelim
    /**
     * Argument parsing and initial setup.
     *
     * @param args Program arguments
     */
    // docs start main
    public static void main(String[] args) throws Exception {

        // GeoTools provides utility classes to parse command line arguments
        Arguments processedArgs = new Arguments(args);
        ImageTiler tiler = new ImageTiler();

        try {
            tiler.setInputFile(new File(processedArgs.getRequiredString("-f")));
            tiler.setOutputDirectory(new File(processedArgs.getRequiredString("-o")));
            tiler.setNumberOfHorizontalTiles(processedArgs.getOptionalInteger("-htc"));
            tiler.setNumberOfVerticalTiles(processedArgs.getOptionalInteger("-vtc"));
            tiler.setTileScale(processedArgs.getOptionalDouble("-scale"));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            printUsage();
            System.exit(1);
        }

        tiler.tile();
    }

    private static void printUsage() {
        System.out.println(
                "Usage: -f inputFile -o outputDirectory [-tw tileWidth<default:256> "
                        + "-th tileHeight<default:256> ");
        System.out.println(
                "-htc horizontalTileCount<default:16> -vtc verticalTileCount<default:8>");
    }
    // docs end main

    /**
     * Crop the coverage to the given envelope
     *
     * @param gridCoverage coverage to crp
     * @param envelope envelope to crop it to
     * @return the cropped coverage
     */
    // docs start cropping
    private GridCoverage2D cropCoverage(GridCoverage2D gridCoverage, Envelope envelope) {
        CoverageProcessor processor = CoverageProcessor.getInstance();

        // An example of manually creating the operation and parameters we want
        final ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();
        param.parameter("Source").setValue(gridCoverage);
        param.parameter("Envelope").setValue(envelope);

        return (GridCoverage2D) processor.doOperation(param);
    }
    // docs end cropping

    /**
     * Create the target tile envelope.
     *
     * @param coverageMinX minimum x of our coverage
     * @param coverageMinY minimum y of our coverage
     * @param geographicTileWidth our target tile envelope width
     * @param geographicTileHeight our target tile envelope height
     * @param targetCRS the target tile CRS
     * @param horizontalIndex horizontal index of the tile envelope
     * @param verticalIndex vertical index of the tile envelope
     * @return tile envelope
     */
    // docs start make envelope
    private Envelope getTileEnvelope(
            double coverageMinX,
            double coverageMinY,
            double geographicTileWidth,
            double geographicTileHeight,
            CoordinateReferenceSystem targetCRS,
            int horizontalIndex,
            int verticalIndex) {

        double envelopeStartX = (horizontalIndex * geographicTileWidth) + coverageMinX;
        double envelopeEndX = envelopeStartX + geographicTileWidth;
        double envelopeStartY = (verticalIndex * geographicTileHeight) + coverageMinY;
        double envelopeEndY = envelopeStartY + geographicTileHeight;

        return new ReferencedEnvelope(
                envelopeStartX, envelopeEndX, envelopeStartY, envelopeEndY, targetCRS);
    }
    // docs end make envelope

    // docs start load coverage
    private void tile() throws IOException {
        AbstractGridFormat format = GridFormatFinder.findFormat(this.getInputFile());
        String fileExtension = this.getFileExtension(this.getInputFile());

        // working around a bug/quirk in geotiff loading via format.getReader which doesn't set this
        // correctly
        Hints hints = null;
        if (format instanceof GeoTiffFormat) {
            hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
        }

        GridCoverage2DReader gridReader = format.getReader(this.getInputFile(), hints);
        GridCoverage2D gridCoverage = gridReader.read(null);
        // docs end load coverage

        // docs start envelope
        Envelope2D coverageEnvelope = gridCoverage.getEnvelope2D();
        double coverageMinX = coverageEnvelope.getBounds().getMinX();
        double coverageMaxX = coverageEnvelope.getBounds().getMaxX();
        double coverageMinY = coverageEnvelope.getBounds().getMinY();
        double coverageMaxY = coverageEnvelope.getBounds().getMaxY();

        int htc =
                this.getNumberOfHorizontalTiles() != null
                        ? this.getNumberOfHorizontalTiles()
                        : NUM_HORIZONTAL_TILES;
        int vtc =
                this.getNumberOfVerticalTiles() != null
                        ? this.getNumberOfVerticalTiles()
                        : NUM_VERTICAL_TILES;

        double geographicTileWidth = (coverageMaxX - coverageMinX) / (double) htc;
        double geographicTileHeight = (coverageMaxY - coverageMinY) / (double) vtc;

        CoordinateReferenceSystem targetCRS = gridCoverage.getCoordinateReferenceSystem();

        // make sure to create our output directory if it doesn't already exist
        File tileDirectory = this.getOutputDirectory();
        if (!tileDirectory.exists()) {
            tileDirectory.mkdirs();
        }

        // iterate over our tile counts
        for (int i = 0; i < htc; i++) {
            for (int j = 0; j < vtc; j++) {

                System.out.println("Processing tile at indices i: " + i + " and j: " + j);
                // create the envelope of the tile
                Envelope envelope =
                        getTileEnvelope(
                                coverageMinX,
                                coverageMinY,
                                geographicTileWidth,
                                geographicTileHeight,
                                targetCRS,
                                i,
                                j);

                GridCoverage2D finalCoverage = cropCoverage(gridCoverage, envelope);

                if (this.getTileScale() != null) {
                    finalCoverage = scaleCoverage(finalCoverage);
                }

                // use the AbstractGridFormat's writer to write out the tile
                File tileFile = new File(tileDirectory, i + "_" + j + "." + fileExtension);
                format.getWriter(tileFile).write(finalCoverage, null);
            }
        }
    }
    // docs end envelope

    // docs start scale
    /**
     * Scale the coverage based on the set tileScale
     *
     * <p>As an alternative to using parameters to do the operations, we can use the Operations
     * class to do them in a slightly more type safe way.
     *
     * @param coverage the coverage to scale
     * @return the scaled coverage
     */
    private GridCoverage2D scaleCoverage(GridCoverage2D coverage) {
        Operations ops = new Operations(null);
        coverage =
                (GridCoverage2D)
                        ops.scale(coverage, this.getTileScale(), this.getTileScale(), 0, 0);
        return coverage;
    }
}
// docs end scale
