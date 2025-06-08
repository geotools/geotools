/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2010, HydroloGIS S.r.l.
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
package org.geotools.gce.grassraster;

import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.media.jai.Interpolation;
import javax.media.jai.RasterFactory;
import javax.media.jai.iterator.RandomIterFactory;
import javax.media.jai.iterator.RectIter;
import javax.media.jai.iterator.RectIterFactory;
import javax.media.jai.iterator.WritableRandomIter;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.InvalidGridGeometryException;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.ImageWorker;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.GeoTools;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;

/**
 * A facade of often used methods by the JGrass engine
 *
 * @author Andrea Antonello - www.hydrologis.com
 * @since 1.1.0
 */
public class JGrassUtilities {
    public static final String NORTH = "NORTH"; // $NON-NLS-1$
    public static final String SOUTH = "SOUTH"; // $NON-NLS-1$
    public static final String WEST = "WEST"; // $NON-NLS-1$
    public static final String EAST = "EAST"; // $NON-NLS-1$
    public static final String XRES = "XRES"; // $NON-NLS-1$
    public static final String YRES = "YRES"; // $NON-NLS-1$
    public static final String ROWS = "ROWS"; // $NON-NLS-1$
    public static final String COLS = "COLS"; // $NON-NLS-1$

    public static Interpolation interpolation = Interpolation.getInstance(Interpolation.INTERP_NEAREST);

    /**
     * Returns the list of files involved in the raster map issues. If for example a map has to be deleted, then all
     * these files have to.
     *
     * @param mapsetPath - the path of the mapset
     * @param mapname -the name of the map
     * @return the array of strings containing the full path to the involved files
     */
    public static boolean checkRasterMapConsistence(String mapsetPath, String mapname) {
        File file = new File(mapsetPath + File.separator + JGrassConstants.FCELL + File.separator + mapname);
        File file2 = new File(mapsetPath + File.separator + JGrassConstants.CELL + File.separator + mapname);
        // the map is in one of the two
        if (!file.exists() && !file2.exists()) return false;

        /*
         * helper files
         */
        file = new File(mapsetPath + File.separator + JGrassConstants.CELLHD + File.separator + mapname);
        if (!file.exists()) return false;
        // it is important that the folder cell_misc/mapname comes before the
        // files in it
        file = new File(mapsetPath + File.separator + JGrassConstants.CELL_MISC + File.separator + mapname);
        if (!file.exists()) return false;

        return true;
    }

    /** create a buffered image from a set of color triplets */
    public static BufferedImage ByteBufferImage(byte[] data, int width, int height) {
        int[] bandoffsets = {0, 1, 2, 3};
        DataBufferByte dbb = new DataBufferByte(data, data.length);
        WritableRaster wr = Raster.createInterleavedRaster(dbb, width, height, width * 4, 4, bandoffsets, null);
        int[] bitfield = {8, 8, 8, 8};

        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel cm =
                new ComponentColorModel(cs, bitfield, true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);

        return new BufferedImage(cm, wr, false, null);
    }

    public static Envelope reprojectEnvelopeByEpsg(int srcEpsg, int destEpsg, Envelope srcEnvelope)
            throws FactoryException, TransformException {

        CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:" + srcEpsg); // $NON-NLS-1$
        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:" + destEpsg); // $NON-NLS-1$
        MathTransform tr = CRS.findMathTransform(sourceCRS, targetCRS);

        // From that point, I'm not sure which kind of object is returned by
        // getLatLonBoundingBox(). But there is some convenience methods if CRS
        // like:

        return JTS.transform(srcEnvelope, tr);
    }

    /**
     * return the rectangle of the cell of the active region, that surrounds the given coordinates
     *
     * @param x the given easting coordinate
     * @param y given northing coordinate
     * @return the rectangle localizing the cell inside which the x and y stay
     */
    public static JGrassRegion getRectangleAroundPoint(JGrassRegion activeRegion, double x, double y) {

        double minx = activeRegion.getRectangle().getBounds2D().getMinX();
        double ewres = activeRegion.getWEResolution();
        double snapx = minx + Math.round((x - minx) / ewres) * ewres;
        double miny = activeRegion.getRectangle().getBounds2D().getMinY();
        double nsres = activeRegion.getNSResolution();
        double snapy = miny + Math.round((y - miny) / nsres) * nsres;
        double xmin = 0.0;
        double xmax = 0.0;
        double ymin = 0.0;
        double ymax = 0.0;

        if (x >= snapx) {
            xmin = snapx;
            xmax = xmin + ewres;
        } else {
            xmax = snapx;
            xmin = xmax - ewres;
        }

        if (y <= snapy) {
            ymax = snapy;
            ymin = ymax - nsres;
        } else {
            ymin = snapy;
            ymax = ymin + nsres;
        }

        // why do I have to put ymin, Rectangle requires the upper left
        // corner?????!!!! Is it a BUG
        // in the Rectangle2D class? or docu?
        // Rectangle2D rect = new Rectangle2D.Double(xmin, ymin, ewres, nsres);
        return new JGrassRegion(xmin, xmax, ymin, ymax, 1, 1);
    }

    // /**
    // * This method gives the possibility to supply a point through coordinates
    // and gain the value
    // of
    // * a certain supplied map in that point (a kind of d.what.rast)
    // *
    // * @param mapName - the map of which we want to know the value ("@mapset")
    // * @param rasterFormat - the raster format from where to read from
    // * @param coordinates - the point in which we want to know the value
    // (x=easting, y=northing)
    // */
    // public static String getWhatValueFromMap( String mapName, Point2D
    // coordinates ) {
    //
    // Rectangle2D pixelBound =
    // dataMask.snapRectangleToClickedCell(coordinates.getX(),
    // coordinates.getY());
    //
    // Window active = new Window(pixelBound.getMinX(), pixelBound.getMaxX(),
    // pixelBound.getMinY(), pixelBound.getMaxY(), 1, 1);
    //
    // // define the map
    // MapReader reader = MapIOFactory.CreateRasterMapReader(rasterFormat);
    // RasterMap theMap = (RasterMap) copt.g.getLocation().getMaps(reader,
    // mapName).elementAt(0); //
    // get
    // // the
    // // rastermap
    // theMap.setReader(rasterFormat); // the type of reader has to be set
    // theMap.setNovalue(new java.lang.Double(FluidConstants.fluidnovalue)); //
    // the
    // // novalue
    // // has to be
    // // set
    // theMap.setDataWindow(active); // the calculation region is set
    // theMap.setOutputMatrixType(0); // set the matrix type
    // theMap.setOutputDataObject(new double[0][0]); // data type
    //
    // // read elevation
    // double[][] elevation = null;
    // if (!theMap.openMap()) {
    // JOptionPane.showMessageDialog(null, "Error! Problems opening map...");
    // return "NaN";
    // }
    // while( theMap.hasMoreData() ) {
    // elevation = (double[][]) theMap.getNextData();
    // }
    //
    // if (elevation[0][0] == FluidConstants.fluidnovalue)
    // return "null";
    // return String.valueOf(elevation[0][0]);
    // }
    //
    // /**
    // * When we click on the map the chosen point will be somewhere inside the
    // hit cell. It is a
    // * matter of rounding the northing and easting of the point to get the
    // right row and column
    // that
    // * contains the hit. Therefore this method returns the row and col in
    // which the point falls.
    // *
    // * @param loc - the location
    // * @param coordinates - the coordinates of the hit point
    // * @return the reviewed point as row, col
    // */
    // public static Point putClickToCenterOfCell( Window active, Point2D
    // coordinates ) {
    // double eastingClick = coordinates.getX();
    // double northingClick = coordinates.getY();
    //
    // double startnorth = active.getNorth();
    // double startwest = active.getWest();
    // double northdelta = active.getNSResolution();
    // double westdelta = active.getWEResolution();
    // int clickrow = 0;
    // int clickcol = 0;
    //
    // for( int i = 0; i < active.getRows(); i++ ) {
    // startnorth = startnorth - northdelta;
    // if (northingClick > startnorth) {
    // clickrow = i;
    // break;
    // }
    // }
    // for( int i = 0; i < active.getCols(); i++ ) {
    // startwest = startwest + westdelta;
    // if (eastingClick < startwest) {
    // clickcol = i;
    // break;
    // }
    // }
    //
    // return new Point(clickrow, clickcol);
    // }
    //
    /**
     * Transforms row and column index of the active region into the regarding northing and easting coordinates. The
     * center of the cell is taken.
     *
     * <p>NOTE: basically the inverse of {@link JGrassUtilities#coordinateToNearestRowCol(JGrassRegion, Coordinate)}
     *
     * @param active - the active region (can be null)
     * @param row - row number of the point to transform
     * @param col - column number of the point to transform
     * @return the point in N/E coordinates of the supplied row and column
     */
    public static Coordinate rowColToCenterCoordinates(JGrassRegion active, int row, int col) {

        double north = active.getNorth();
        double west = active.getWest();
        double nsres = active.getNSResolution();
        double ewres = active.getWEResolution();

        double northing = north - row * nsres - nsres / 2.0;
        double easting = west + col * ewres + ewres / 2.0;

        return new Coordinate(easting, northing);
    }

    /**
     * Return the row and column of the active region matrix for a give coordinate *
     *
     * <p>NOTE: basically the inverse of {@link JGrassUtilities#rowColToCenterCoordinates(JGrassRegion, int, int)}
     *
     * @param active the active region
     * @return and int array containing row and col
     */
    public static int[] coordinateToNearestRowCol(JGrassRegion active, Coordinate coord) {

        double easting = coord.x;
        double northing = coord.y;
        int[] rowcol = new int[2];
        if (easting > active.getEast()
                || easting < active.getWest()
                || northing > active.getNorth()
                || northing < active.getSouth()) {
            return null;
        }

        double minx = active.getWest();
        double ewres = active.getWEResolution();
        for (int i = 0; i < active.getCols(); i++) {
            minx = minx + ewres;
            if (easting < minx) {
                rowcol[1] = i;
                break;
            }
        }

        double maxy = active.getNorth();
        double nsres = active.getNSResolution();
        for (int i = 0; i < active.getRows(); i++) {
            maxy = maxy - nsres;
            if (northing > maxy) {
                rowcol[0] = i;
                break;
            }
        }

        return rowcol;
    }

    // /**
    // * Given the mapsetpath and the mapname, the map is removed with all its accessor files
    // *
    // */
    // public static boolean removeGrassRasterMap( String mapsetPath, String mapName ) {
    //
    // // list of files to remove
    // String mappaths[] = filesOfRasterMap(mapsetPath, mapName);
    //
    // // first delete the list above, which are just files
    // for( int j = 0; j < mappaths.length; j++ ) {
    // File filetoremove = new File(mappaths[j]);
    // if (filetoremove.exists()) {
    // if (!FileUtilities.deleteFileOrDir(filetoremove)) {
    // return false;
    // }
    // }
    // }
    // return true;
    // }

    /**
     * Returns the list of files involved in the raster map issues. If for example a map has to be deleted, then all
     * these files have to.
     *
     * @param mapsetPath - the path of the mapset
     * @param mapname -the name of the map
     * @return the array of strings containing the full path to the involved files
     */
    public static String[] filesOfRasterMap(String mapsetPath, String mapname) {
        String[] filesOfRaster = {
            mapsetPath + File.separator + JGrassConstants.FCELL + File.separator + mapname,
            mapsetPath + File.separator + JGrassConstants.CELL + File.separator + mapname,
            mapsetPath + File.separator + JGrassConstants.CATS + File.separator + mapname,
            mapsetPath + File.separator + JGrassConstants.HIST + File.separator + mapname,
            mapsetPath + File.separator + JGrassConstants.CELLHD + File.separator + mapname,
            mapsetPath + File.separator + JGrassConstants.COLR + File.separator + mapname,
            // it is very important that the folder cell_misc/mapname comes
            // before the files in it
            mapsetPath + File.separator + JGrassConstants.CELL_MISC + File.separator + mapname,
            mapsetPath
                    + File.separator
                    + JGrassConstants.CELL_MISC
                    + File.separator
                    + mapname
                    + File.separator
                    + JGrassConstants.CELLMISC_FORMAT,
            mapsetPath
                    + File.separator
                    + JGrassConstants.CELL_MISC
                    + File.separator
                    + mapname
                    + File.separator
                    + JGrassConstants.CELLMISC_QUANT,
            mapsetPath
                    + File.separator
                    + JGrassConstants.CELL_MISC
                    + File.separator
                    + mapname
                    + File.separator
                    + JGrassConstants.CELLMISC_RANGE,
            mapsetPath
                    + File.separator
                    + JGrassConstants.CELL_MISC
                    + File.separator
                    + mapname
                    + File.separator
                    + JGrassConstants.CELLMISC_NULL
        };
        return filesOfRaster;
    }

    /**
     * Transforms row and column index of the active region into an array of the coordinates of the edgaes, i.e. n, s,
     * e, w
     *
     * @param active - the active region (can be null)
     * @param row - row number of the point to transform
     * @param col - column number of the point to transform
     * @return the array of north, south, east, west
     */
    public static double[] rowColToNodeboundCoordinates(JGrassRegion active, int row, int col) {

        double anorth = active.getNorth();
        double awest = active.getWest();
        double nsres = active.getNSResolution();
        double ewres = active.getWEResolution();

        double[] nsew = new double[4];
        nsew[0] = anorth - row * nsres;
        nsew[1] = anorth - row * nsres - nsres;
        nsew[2] = awest + col * ewres + ewres;
        nsew[3] = awest + col * ewres;

        return nsew;
    }

    public static int factorial(int n) {
        int fact = 1;
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }

    public static void makeColorRulesPersistent(File colrFile, List<String> rules, double[] minMax, int alpha)
            throws IOException {
        if (!colrFile.getParentFile().exists()) {
            colrFile.getParentFile().mkdir();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(colrFile))) {
            if (rules.isEmpty()) {
                throw new IllegalArgumentException("The list of colorrules can't be empty.");
            }
            String header = "% " + minMax[0] + "   " + minMax[1] + "   " + alpha;
            bw.write(header + "\n");
            for (String r : rules) {
                bw.write(r + "\n");
            }
        }
    }

    /**
     * Calculates optimal tile size for the actual free memory.
     *
     * @param rows the rows of the complete image the tiles are calculated for.
     * @param cols the cols of the complete image the tiles are calculated for.
     */
    public static int[] getTilesBasedOnFreeMemory(int rows, int cols) {
        long freeMemory = Runtime.getRuntime().freeMemory();
        int tileSizeY = 256;
        int tileSizeX = 256;
        if (freeMemory > 8L * cols) {
            tileSizeX = cols;
            tileSizeY = (int) (freeMemory / 8) / cols;
            if (tileSizeY > rows) {
                tileSizeY = rows;
            }
        }
        return new int[] {tileSizeX, tileSizeY};
    }

    public static JGrassRegion getJGrassRegionFromGridCoverage(GridCoverage2D gridCoverage2D)
            throws InvalidGridGeometryException, TransformException {
        ReferencedEnvelope env = gridCoverage2D.getEnvelope2D();
        GridEnvelope2D worldToGrid = gridCoverage2D.getGridGeometry().worldToGrid(env);

        double xRes = env.getWidth() / worldToGrid.getWidth();
        double yRes = env.getHeight() / worldToGrid.getHeight();

        JGrassRegion region = new JGrassRegion(env.getMinX(), env.getMaxX(), env.getMinY(), env.getMaxY(), xRes, yRes);

        return region;
    }

    public static RenderedImage scaleJAIImage(
            int requestedCols, int requestedRows, RenderedImage translatedImage, Interpolation interpolation) {
        if (interpolation == null) {
            interpolation = JGrassUtilities.interpolation;
        }
        ImageWorker worker = new ImageWorker(translatedImage);
        worker.scale(
                requestedCols / (float) translatedImage.getWidth(),
                requestedRows / (float) translatedImage.getHeight(),
                0F,
                0F,
                interpolation);
        return worker.getRenderedOperation();
    }

    /**
     * Creates a {@link GridCoverage2D coverage} from a double[][] matrix and the necessary geographic Information.
     *
     * @param name the name of the coverage.
     * @param dataMatrix the matrix containing the data.
     * @param crs the {@link CoordinateReferenceSystem}.
     * @param matrixIsRowCol a flag to tell if the matrix has rowCol or colRow order.
     * @return the {@link GridCoverage2D coverage}.
     */
    public static GridCoverage2D buildCoverage(
            String name,
            double[][] dataMatrix,
            double n,
            double s,
            double w,
            double e,
            CoordinateReferenceSystem crs,
            boolean matrixIsRowCol) {
        WritableRaster writableRaster = createWritableRasterFromMatrix(dataMatrix, matrixIsRowCol);

        ReferencedEnvelope writeEnvelope = ReferencedEnvelope.rect(w, s, e - w, n - s, crs);
        GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(GeoTools.getDefaultHints());

        GridCoverage2D coverage2D = factory.create(name, writableRaster, writeEnvelope);
        return coverage2D;
    }
    /**
     * Create a {@link WritableRaster} from a double matrix.
     *
     * @param matrix the matrix to take the data from.
     * @param matrixIsRowCol a flag to tell if the matrix has rowCol or colRow order.
     * @return the produced raster.
     */
    public static WritableRaster createWritableRasterFromMatrix(double[][] matrix, boolean matrixIsRowCol) {
        int height = matrix.length;
        int width = matrix[0].length;
        if (!matrixIsRowCol) {
            int tmp = height;
            height = width;
            width = tmp;
        }
        WritableRaster writableRaster = createDoubleWritableRaster(width, height, null, null, null);

        WritableRandomIter disckRandomIter = RandomIterFactory.createWritable(writableRaster, null);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (matrixIsRowCol) {
                    disckRandomIter.setSample(x, y, 0, matrix[y][x]);
                } else {
                    disckRandomIter.setSample(x, y, 0, matrix[x][y]);
                }
            }
        }
        disckRandomIter.done();

        return writableRaster;
    }

    /**
     * Creates a {@link WritableRaster writable raster}.
     *
     * @param width width of the raster to create.
     * @param height height of the raster to create.
     * @param dataClass data type for the raster. If <code>null</code>, defaults to double.
     * @param sampleModel the samplemodel to use. If <code>null</code>, defaults to <code>
     *     new ComponentSampleModel(dataType, width, height, 1, width, new int[]{0});</code>.
     * @param value value to which to set the raster to. If null, the default of the raster creation is used, which is
     *     0.
     * @return a {@link WritableRaster writable raster}.
     */
    public static WritableRaster createDoubleWritableRaster(
            int width, int height, Class<?> dataClass, SampleModel sampleModel, Double value) {
        int dataType = DataBuffer.TYPE_DOUBLE;
        if (dataClass != null) {
            if (dataClass.isAssignableFrom(Integer.class)) {
                dataType = DataBuffer.TYPE_INT;
            } else if (dataClass.isAssignableFrom(Float.class)) {
                dataType = DataBuffer.TYPE_FLOAT;
            } else if (dataClass.isAssignableFrom(Byte.class)) {
                dataType = DataBuffer.TYPE_BYTE;
            }
        }

        if (sampleModel == null) {
            sampleModel = new ComponentSampleModel(dataType, width, height, 1, width, new int[] {0});
        }

        WritableRaster raster = RasterFactory.createWritableRaster(sampleModel, null);
        if (value != null) {
            // autobox only once
            double v = value;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    raster.setSample(x, y, 0, v);
                }
            }
        }
        return raster;
    }

    /**
     * Checks if the given path is a GRASS raster file.
     *
     * <p>Note that there is no check on the existence of the file.
     *
     * @param path the path to check.
     * @return true if the file is a grass raster.
     */
    public static boolean isGrass(String path) {
        File file = new File(path);
        File cellFolderFile = file.getParentFile();
        File mapsetFile = cellFolderFile.getParentFile();
        File windFile = new File(mapsetFile, "WIND");
        return cellFolderFile.getName().toLowerCase().equals("cell") && windFile.exists();
    }

    public static void printImage(GridCoverage2D coverage2D) {
        RenderedImage renderedImage = coverage2D.getRenderedImage();
        printImage(renderedImage);
    }

    @SuppressWarnings("PMD.SystemPrintln")
    public static void printImage(RenderedImage renderedImage) {
        RectIter rectIter = RectIterFactory.create(renderedImage, null);
        do {
            do {
                double value = rectIter.getSampleDouble();
                System.out.print(value + " ");
            } while (!rectIter.nextPixelDone());
            rectIter.startPixels();
        } while (!rectIter.nextLineDone());
    }
}
