/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.grassraster.core;

import java.awt.Rectangle;
import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.ComponentSampleModelJAI;
import javax.media.jai.RasterFactory;
import javax.media.jai.iterator.RectIter;
import org.geotools.gce.grassraster.DummyProgressListener;
import org.geotools.gce.grassraster.GrassBinaryImageReader;
import org.geotools.gce.grassraster.JGrassConstants;
import org.geotools.gce.grassraster.JGrassMapEnvironment;
import org.geotools.gce.grassraster.JGrassRegion;
import org.geotools.gce.grassraster.core.color.AttributeTable;
import org.geotools.gce.grassraster.core.color.AttributeTable.CellAttribute;
import org.geotools.gce.grassraster.core.color.JGrassColorTable;
import org.geotools.gce.grassraster.core.color.JlsTokenizer;
import org.geotools.gce.grassraster.metadata.GrassBinaryImageMetadata;
import org.geotools.referencing.CRS;
import org.geotools.util.SimpleInternationalString;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.ProgressListener;

/**
 * Grass binary data input/ouput handler.
 *
 * <p>Reading and writing is supported.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 * @since 3.0
 * @see GrassBinaryImageReader
 * @see GrassBinaryImageReadParam
 * @see GrassBinaryImageMetadata
 */
public class GrassBinaryRasterReadHandler implements Closeable {

    /** The flag that defines whether to abort or not. */
    private boolean abortRequired = false;

    /** {@linkplain ImageInputStream} used to read the input data. */
    private ImageInputStream imageIS = null;

    /** {@linkplain ImageInputStream} used to read the input data null bitmap. */
    private ImageInputStream imageNullFileIS = null;

    /** the value used to represent non existing data for in the raster. */
    private Double noData = Double.NaN;

    /** The {@linkplain JGrassMapEnvironment environment} needed for raster reading. */
    private JGrassMapEnvironment readerGrassEnv = null;

    /** the vector representing the reclass table. */
    private Vector<Object> reclassTable = null;

    /** the region of the native grass raster. */
    private JGrassRegion nativeRasterRegion = null;

    /**
     * the variable defining the type of the map that is processed.
     *
     * <p>
     *
     * <ul>
     *   <li>0 for 1-byte integer
     *   <li>1 for 2-byte integer and so on
     *   <li>-1 for float
     *   <li>-2 for double
     * </ul>
     */
    private int readerMapType = -9999;

    /** the number of bytes that define a map value. */
    private int numberOfBytesPerValue = -9999;

    /** the flag that defines compression of the data. */
    private boolean compressed = false;

    /** the flag that, if true, describes that the current map is in an old integer format. */
    private boolean isOldIntegerMap = false;

    /**
     * the long array that keeps the addresses of the starting point (bytes in the file) of each
     * raster row.
     */
    private long[] addressesOfRows;

    private int rowCacheRow = -1;
    private int firstDataRow = -1;

    private int rasterMapWidth;

    private int rasterMapHeight;

    private boolean useSubSamplingAsRequestedRowcols = false;
    private boolean castDoubleToFloating = false;

    private SampleModel sampleModel = null;

    private ProgressListener monitor = new DummyProgressListener();

    private JGrassRegion activeReadRegion;

    /**
     * the constructor to build a {@link GrassBinaryRasterReadHandler} usable for reading grass
     * rasters.
     *
     * @param cellFile the file of the raw raster data.
     */
    public GrassBinaryRasterReadHandler(File cellFile) {
        readerGrassEnv = new JGrassMapEnvironment(cellFile);
        abortRequired = false;
    }

    /**
     * Reads a grass raster, adding the possibility to override subsampling.
     *
     * @param param the {@linkplain ImageReadParam read parameters}.
     * @param useSubSamplingAsRequestedRowcols a flag that gives the possibility to bypass the
     *     imageio subsampling mechanism. With GRASS maps this is often more performing in some
     *     boundary situations. In the case this flag is set to true, the subsampling values will be
     *     handled as the requested columns and rows.
     * @param castDoubleToFloating a flag that gives the possibility to force the reading of a map
     *     as a floating point map. This is necessary right now because of a imageio bug:
     *     https://jai-imageio-core.dev.java.net /issues/show_bug.cgi?id=180
     * @return the read {@link WritableRaster raster}
     */
    public WritableRaster readRaster(
            ImageReadParam param,
            boolean useSubSamplingAsRequestedRowcols,
            boolean castDoubleToFloating,
            ProgressListener monitor)
            throws IOException, DataFormatException {
        this.useSubSamplingAsRequestedRowcols = useSubSamplingAsRequestedRowcols;
        this.castDoubleToFloating = castDoubleToFloating;
        this.monitor = monitor;
        return readRaster(param);
    }

    /**
     * reads a grass raster, given the {@linkplain ImageReadParam read parameters}.
     *
     * <p>The data are read into a single banded, floating point {@link WritableRaster}. A {@link
     * RectIter} can be used to access the data afterwards.
     *
     * @param param the read parameters.
     * @return the read raster
     */
    public WritableRaster readRaster(ImageReadParam param) throws IOException, DataFormatException {

        if (param != null) {
            // extract the region to read from file as a Rectangle
            Rectangle srcRegion = param.getSourceRegion();
            // the dimension of the Rectangle
            int intRealRowsToRead = srcRegion.height;
            int intRealColsToRead = srcRegion.width;
            // the steep to read the pixel (usually 1)
            int sourceXSubsampling = param.getSourceXSubsampling();
            int sourceYSubsampling = param.getSourceYSubsampling();
            // calculate the window to be read
            double xRes = nativeRasterRegion.getWEResolution();
            double yRes = nativeRasterRegion.getNSResolution();
            double newWest = nativeRasterRegion.getWest() + srcRegion.getMinX() * xRes;
            double newEast = nativeRasterRegion.getWest() + srcRegion.getMaxX() * xRes;

            /*
             * remember that the rectangle is handled in world mode,
             * not image mode, therefore ymin represents the south.
             */
            double minY = srcRegion.getMinY();
            double newNorth = nativeRasterRegion.getNorth() - minY * yRes;
            double newSouth = newNorth - srcRegion.height * yRes;

            activeReadRegion =
                    new JGrassRegion(
                            newWest,
                            newEast,
                            newSouth,
                            newNorth,
                            intRealRowsToRead,
                            intRealColsToRead);
            int colsAtMaxRes = activeReadRegion.getCols();
            int rowsAtMaxRes = activeReadRegion.getRows();

            if (!useSubSamplingAsRequestedRowcols) {
                rasterMapWidth = colsAtMaxRes / sourceXSubsampling;
                rasterMapHeight = rowsAtMaxRes / sourceYSubsampling;
            } else {
                rasterMapWidth = sourceXSubsampling;
                rasterMapHeight = sourceYSubsampling;
            }
            activeReadRegion.setCols(rasterMapWidth);
            activeReadRegion.setRows(rasterMapHeight);

        } else {
            // the whole image is read
            activeReadRegion = new JGrassRegion(nativeRasterRegion);
            rasterMapWidth = nativeRasterRegion.getCols();
            rasterMapHeight = nativeRasterRegion.getRows();
        }

        /*
         * create a single band double raster
         */
        final WritableRaster raster;
        if (numberOfBytesPerValue == 8) {
            if (!castDoubleToFloating) {
                raster =
                        RasterFactory.createBandedRaster(
                                DataBuffer.TYPE_DOUBLE, rasterMapWidth, rasterMapHeight, 1, null);
            } else {
                raster =
                        RasterFactory.createBandedRaster(
                                DataBuffer.TYPE_FLOAT, rasterMapWidth, rasterMapHeight, 1, null);
            }
        } else if (numberOfBytesPerValue == 4 && readerMapType < 0) {
            raster =
                    RasterFactory.createBandedRaster(
                            DataBuffer.TYPE_FLOAT, rasterMapWidth, rasterMapHeight, 1, null);
        } else if (readerMapType > -1) {
            raster =
                    RasterFactory.createBandedRaster(
                            DataBuffer.TYPE_INT, rasterMapWidth, rasterMapHeight, 1, null);
        } else {
            throw new IOException("Raster type not supported."); // $NON-NLS-1$
        }

        /* Allocate the space for the map data. */
        int activeRows = activeReadRegion.getRows();
        int activeCols = activeReadRegion.getCols();
        int bufferSize = activeRows * activeCols * numberOfBytesPerValue;
        ByteBuffer rasterByteBuffer = ByteBuffer.allocate(bufferSize);

        /* Byte array that will hold a complete null row */
        byte[] nullRow = null;
        /* The rowDataArray holds the unpacked row data */
        byte[] rowDataCache = new byte[activeCols * numberOfBytesPerValue];
        rowCacheRow = -1;
        firstDataRow = -1;
        int rowindex = -1;
        /* Get a local reference to speed things up */
        int filerows = nativeRasterRegion.getRows();
        double filenorth = nativeRasterRegion.getNorth();
        double filensres = nativeRasterRegion.getNSResolution();
        double datanorth = activeReadRegion.getNorth();
        double datansres = activeReadRegion.getNSResolution();
        monitor.started();
        monitor.setTask(
                new SimpleInternationalString("Read raster map: " + readerGrassEnv.getMapName()));
        float progress = 0f;
        for (double row = 0; row < activeRows; row++) {

            /*
             * Calculate the map file row for the current data window row.
             */
            double filerow = (filenorth - (datanorth - (row * datansres))) / filensres;
            filerow = Math.floor(filerow);
            if (filerow < 0 || filerow >= filerows) {
                /*
                 * If no data has been read yet, then increment first data row
                 * counter
                 */
                if (firstDataRow == -1) rowindex++;
                /*
                 * Write a null row to the raster buffer. To speed things up the
                 * first time this is called it instantiates the buffer and
                 * fills it with null values that are reused the other times.
                 */
                if (nullRow == null) nullRow = initNullRow(activeCols);
                rasterByteBuffer.put(nullRow);
            } else {
                if (firstDataRow == -1) firstDataRow = rowindex + 1;
                /* Read row and put in raster buffer */
                if (filerow == rowCacheRow) {
                    rasterByteBuffer.put(rowDataCache);
                } else {
                    readRasterRow((int) filerow, rowDataCache, activeReadRegion);
                    rowCacheRow = (int) filerow;
                    rasterByteBuffer.put(rowDataCache);
                }
            }
            progress = (float) (progress + 100f * row / activeRows);
            monitor.progress(progress);
        }
        monitor.complete();

        // prepare for reading
        rasterByteBuffer.rewind();

        rowCacheRow = -1;
        nullRow = null;

        /*
         * create the raster object from the read data.
         */
        if (numberOfBytesPerValue == 8) {
            if (!castDoubleToFloating) {
                for (int y = 0; y < activeRows; y++) {
                    for (int x = 0; x < activeCols; x++) {
                        double value = rasterByteBuffer.getDouble();
                        // if (!Double.isNaN(value)) {
                        // System.out.println(y + "/" + x + "/" + value);
                        // }
                        raster.setSample(x, y, 0, value);
                    }
                    // System.out.println();
                }
            } else {
                for (int y = 0; y < activeRows; y++) {
                    for (int x = 0; x < activeCols; x++) {
                        float value = (float) rasterByteBuffer.getDouble();
                        raster.setSample(x, y, 0, value);
                    }
                }
            }
        } else if (numberOfBytesPerValue == 4 && readerMapType < 0) {
            for (int y = 0; y < activeRows; y++) {
                for (int x = 0; x < activeCols; x++) {
                    float value = rasterByteBuffer.getFloat();
                    raster.setSample(x, y, 0, value);
                }
            }
        } else if (readerMapType > -1) {
            for (int y = 0; y < activeRows; y++) {
                for (int x = 0; x < activeCols; x++) {
                    int value = rasterByteBuffer.getInt();
                    if (value == Integer.MAX_VALUE) {
                        value = noData.intValue();
                    }
                    raster.setSample(x, y, 0, value);
                }
            }
        }

        return raster;
    }

    /**
     * Determines the metadata of the raster map.
     *
     * <p>Reads the map type given a file and its mapset, the information from the header file in
     * the cellhd directory and determines the geographic limits, format of the data, etc from the
     * file. <b>NOTE:</b> for further informations about cell header files, read the package
     * description.
     *
     * <p><b>INFO:</b> this is a reader method.
     */
    @SuppressWarnings({"nls", "PMD.CloseResource"})
    public void parseHeaderAndAccessoryFiles() throws IOException {
        try {
            LinkedHashMap<String, String> readerFileHeaderMap = new LinkedHashMap<String, String>();
            /* Read contents of 'cellhd/name' file from the current mapset */
            String line;
            BufferedReader cellhead;
            String reclassedFile = null;
            String reclassedMapset = null;

            reclassTable = null;
            File cellhd = readerGrassEnv.getCELLHD();
            cellhead = new BufferedReader(new FileReader(cellhd));
            cellhead.mark(128);
            /*
             * Read first line to determine if file is a reclasses file. If it
             * is then open the data file and continue as per usual.
             */
            if ((line = cellhead.readLine()) == null) {
                throw new IOException(
                        "The cellhead file seems to be corrupted: " + cellhd.getAbsolutePath());
            }
            if (line.trim().equalsIgnoreCase("reclass")) { // $NON-NLS-1$
                /* The next two lines hold the orginal map file amd mapset */
                for (int i = 0; i < 2; i++) {
                    if ((line = cellhead.readLine()) == null) {
                        throw new IOException(
                                "The cellhead file seems to be corrupted: "
                                        + cellhd.getAbsolutePath());
                    }
                    String[] lineSplit = line.split(":");
                    if (lineSplit.length == 2) {
                        if (lineSplit[0].trim().equalsIgnoreCase("name"))
                            reclassedFile = lineSplit[1].trim();
                        else if (lineSplit[0].trim().equalsIgnoreCase("mapset"))
                            reclassedMapset = lineSplit[1].trim();
                    }
                }
                /* Instantiate the reclass table */
                reclassTable = new Vector<Object>();
                /* The next line holds the start value for categories */
                if ((line = cellhead.readLine()) == null) {
                    throw new IOException(
                            "The cellhead file seems to be corrupted: " + cellhd.getAbsolutePath());
                }
                if (line.charAt(0) == '#') {
                    int reclassFirstCategory = Integer.parseInt(line.trim().substring(1));
                    /* Pad reclass table until the first reclass category */
                    for (int i = 0; i < reclassFirstCategory; i++) {
                        reclassTable.addElement("");
                    }
                } else {
                    /* Add an empty element for the 0th category */
                    reclassTable.addElement("");
                }
                /* Now read the reclass table */
                while ((line = cellhead.readLine()) != null) {
                    reclassTable.addElement(Integer.valueOf(line));
                }
                // set new reclass environment and check for new reclass header
                readerGrassEnv.setReclassed(reclassedMapset, reclassedFile);
                if (!cellhd.exists()) {
                    throw new IOException(
                            "The reclassed cellhead file doesn't seems to exist: "
                                    + cellhd.getAbsolutePath());
                }
                cellhead = new BufferedReader(new FileReader(cellhd));
            } else {
                /* Push first line back onto buffered reader stack */
                cellhead.reset();
            }
            while ((line = cellhead.readLine()) != null) {
                String[] lineSplit = line.split(":");
                if (lineSplit.length == 2) {
                    String key = lineSplit[0].trim();
                    String value = lineSplit[1].trim();
                    /* If key is 'ew resol' or 'ns resol' then store 'xx res' */
                    if (key.indexOf("resol") != -1)
                        readerFileHeaderMap.put(key.replaceAll("resol", "res"), value);
                    else readerFileHeaderMap.put(key, value);
                } else {
                    // probably that means lat/long, i.e. something like
                    // north: 44:12:12N
                    // south: 44:09:51N
                    // east: 11:23:36E
                    // west: 11:19E
                    // e-w resol: 0:00:00.077246
                    // n-s resol: 0:00:00.055381
                    String key = lineSplit[0].trim();

                    double value = 0.0;

                    String degrees = lineSplit[1];
                    value = Double.parseDouble(degrees);

                    String minutes = lineSplit[2];
                    if (minutes.lastIndexOf('N') != -1
                            || minutes.lastIndexOf('S') != -1
                            || minutes.lastIndexOf('E') != -1
                            || minutes.lastIndexOf('W') != -1) {
                        if (minutes.lastIndexOf('S') != -1 || minutes.lastIndexOf('W') != -1) {
                            value = value * -1;
                        }
                        // last number
                        minutes = minutes.substring(0, minutes.length() - 1);
                    }
                    value = value + Double.parseDouble(minutes) / 60.0;

                    // seconds available?
                    if (lineSplit.length == 4) {
                        String seconds = lineSplit[3];
                        if (seconds.lastIndexOf('N') != -1
                                || seconds.lastIndexOf('S') != -1
                                || seconds.lastIndexOf('E') != -1
                                || seconds.lastIndexOf('W') != -1) {
                            if (seconds.lastIndexOf('S') != -1 || seconds.lastIndexOf('W') != -1) {
                                value = value * -1;
                            }
                            // last number
                            seconds = seconds.substring(0, seconds.length() - 1);
                        }
                        value = value + Double.parseDouble(seconds) / 60.0 / 60.0;
                    }

                    if (key.indexOf("resol") != -1)
                        readerFileHeaderMap.put(
                                key.replaceAll("resol", "res"), String.valueOf(value));
                    else readerFileHeaderMap.put(key, String.valueOf(value));
                }
            }

            /*
             * Setup file window object that holds the geographic limits of the
             * file data.
             */
            if (readerFileHeaderMap.containsKey("n-s res")) {
                nativeRasterRegion =
                        new JGrassRegion(
                                Double.parseDouble(readerFileHeaderMap.get("west")),
                                Double.parseDouble(readerFileHeaderMap.get("east")),
                                Double.parseDouble(readerFileHeaderMap.get("south")),
                                Double.parseDouble(readerFileHeaderMap.get("north")),
                                Double.parseDouble(readerFileHeaderMap.get("e-w res")),
                                Double.parseDouble(readerFileHeaderMap.get("n-s res")));
            } else if (readerFileHeaderMap.containsKey("cols")) {
                nativeRasterRegion =
                        new JGrassRegion(
                                Double.parseDouble(readerFileHeaderMap.get("west")),
                                Double.parseDouble(readerFileHeaderMap.get("east")),
                                Double.parseDouble(readerFileHeaderMap.get("south")),
                                Double.parseDouble(readerFileHeaderMap.get("north")),
                                Integer.parseInt(readerFileHeaderMap.get("rows")),
                                Integer.parseInt(readerFileHeaderMap.get("cols")));
            } else {
                throw new IOException(
                        "The map window file seems to be corrupted. Unable to read file region: "
                                + cellhd.getAbsolutePath());
            }

            if (!readerFileHeaderMap.get("format").equals("")) {
                readerMapType = Integer.valueOf(readerFileHeaderMap.get("format")).intValue();
                if (readerMapType > -1) {
                    readerMapType++;
                    /*
                     * In Grass integers can be from 1 to 4 bytes. JGrass will
                     * convert them all directly into an intger (4-bytes) at
                     * reding and decompressing time. Therefore the
                     * numberofbytespervalue is always 4.
                     */
                    numberOfBytesPerValue = 4;
                    /* Instantiate cell file object. */
                    imageIS = ImageIO.createImageInputStream(readerGrassEnv.getCELL());
                    /* Check if null file exists. */
                    imageNullFileIS = null;
                    if (readerGrassEnv.getCELLMISC_NULL().exists()) {
                        imageNullFileIS =
                                ImageIO.createImageInputStream(readerGrassEnv.getCELLMISC_NULL());
                        if (imageNullFileIS == null) {
                            isOldIntegerMap = false;
                        } else {
                            isOldIntegerMap = true;
                        }
                    }
                } else if (readerMapType < 0) {
                    /*
                     * Read contents of 'cell_misc/name/f_format' file from the
                     * current mapset
                     */
                    if (readerGrassEnv.getCELLMISC_FORMAT().exists()) {
                        /*
                         * if the file f_format exists, then we are talking
                         * about floating maps
                         */
                        BufferedReader cellmiscformat =
                                new BufferedReader(
                                        new FileReader(readerGrassEnv.getCELLMISC_FORMAT()));
                        while ((line = cellmiscformat.readLine()) != null) {
                            StringTokenizer tokk = new StringTokenizer(line, ":");
                            if (tokk.countTokens() == 2) {
                                String key = tokk.nextToken().trim();
                                String value = tokk.nextToken().trim();
                                readerFileHeaderMap.put(key, value);
                            }
                        }
                        // assign the values
                        if (!readerFileHeaderMap.get("type").equals("")) {
                            if ((readerFileHeaderMap.get("type")).equalsIgnoreCase("double")) {
                                readerMapType = -2;
                                numberOfBytesPerValue = 8;
                            } else if ((readerFileHeaderMap.get("type"))
                                    .equalsIgnoreCase("float")) {
                                readerMapType = -1;
                                numberOfBytesPerValue = 4;
                            } else {
                                throw new IOException(
                                        "Wrong number type in format file: "
                                                + readerGrassEnv
                                                        .getCELLMISC_FORMAT()
                                                        .getAbsolutePath());
                            }
                        } else {
                            throw new IOException(
                                    "Wrong number type in format file: "
                                            + readerGrassEnv
                                                    .getCELLMISC_FORMAT()
                                                    .getAbsolutePath());
                        }
                        cellmiscformat.close();
                    } else {
                        throw new IOException(
                                "Missing required format file: "
                                        + readerGrassEnv.getCELLMISC_FORMAT().getAbsolutePath());
                    }
                    isOldIntegerMap = false;
                    /* Instantiate cell file and null file objects */
                    imageIS = ImageIO.createImageInputStream(readerGrassEnv.getFCELL());
                    imageNullFileIS = null;
                    if (readerGrassEnv.getCELLMISC_NULL().exists()) {
                        imageNullFileIS =
                                ImageIO.createImageInputStream(readerGrassEnv.getCELLMISC_NULL());
                    }
                }
            } else {
                throw new IOException(
                        "The cellhead file seems to be corrupted: " + cellhd.getAbsolutePath());
            }

            if (!readerFileHeaderMap.get("compressed").equals("")) {
                // 1 == compressed, 0 == non compressed
                int cmpr = Integer.parseInt(readerFileHeaderMap.get("compressed"));
                compressed = cmpr == 1 ? true : false;
            } else {
                throw new IOException(
                        "The cellhead file seems to be corrupted: " + cellhd.getAbsolutePath());
            }

            cellhead.close();

            /*
             * parse the header
             */
            parseHeader();
        } catch (Exception e) {
            throw new IOException(e.getLocalizedMessage());
        }
    }

    /**
     * Extract the row addresses from the header information of the file.
     *
     * <p><b>INFO:</b> this is a reader method.
     */
    private void parseHeader() throws IOException {

        /*
         * the first byte defines the number of bytes are used to describe the
         * row addresses in the header (once it was sizeof(long) in grass but
         * then it was turned to an offset (that brought to reading problems in
         * JGrass whenever the offset was != 4).
         */
        int first = imageIS.read();

        int capacity = 1 + first * nativeRasterRegion.getRows() + first;
        ByteBuffer fileHeader = ByteBuffer.allocate(capacity);

        imageIS.seek(0L);
        /* Read header */
        byte[] array = fileHeader.array();
        imageIS.read(array);

        /*
         * Jump over the no more needed first byte (used to define the header
         * size)
         */
        byte firstbyte = fileHeader.get();
        addressesOfRows = new long[nativeRasterRegion.getRows() + 1];
        if (firstbyte == 4) {
            for (int i = 0; i <= nativeRasterRegion.getRows(); i++) {
                addressesOfRows[i] = fileHeader.getInt();
            }
        } else if (firstbyte == 8) {
            for (int i = 0; i <= nativeRasterRegion.getRows(); i++) {
                addressesOfRows[i] = fileHeader.getLong();
            }
        } else {
            throw new IOException(
                    "The first byte of the GRASS file header is not 4 and not 8. Unknown case for file: "
                            + readerGrassEnv.getCELL().getAbsolutePath());
        }
    }

    /**
     * reads a row of data from the file into a byte array.
     *
     * <p><b>INFO:</b> this is a reader method.
     *
     * @param currentfilerow the current row to be extracted from the file
     * @param rowDataCache the byte array to store the unpacked row data
     * @param activeReadRegion the region defining the portion of raster to be read
     * @return boolean TRUE for success, FALSE for failure.
     */
    private boolean readRasterRow(
            int currentfilerow, byte[] rowDataCache, JGrassRegion activeReadRegion)
            throws IOException, DataFormatException {
        ByteBuffer rowBuffer = ByteBuffer.wrap(rowDataCache);
        /*
         * Read the correct approximated row from the file. The row contents as
         * saved in a cache for along with the row number. If the row requested
         * is the row in the cache then we do not need to read from the file.
         */
        /* Data window geographic boundaries */
        double activeewres = activeReadRegion.getWEResolution();
        // double activeewres2 = activeewres / 2;
        double activewest = activeReadRegion.getWest();

        /* Map file geographic limits */
        double filewest = nativeRasterRegion.getWest();
        double fileewres = nativeRasterRegion.getWEResolution();

        // System.out.println("currentfilerow="+currentfilerow+",
        // fileWindow.getRows()="+fileWindow.getRows());

        /* Reset row cache and read new row data */
        ByteBuffer rowCache =
                ByteBuffer.allocate(nativeRasterRegion.getCols() * ((readerMapType == -2) ? 8 : 4));
        // rowCache.rewind();
        getMapRow(currentfilerow, rowCache);
        // rowCacheRow = currentfilerow;

        // if the northing is inside the file boundaries, calculate the values
        // for (double col = activewest; col < activeeast; col += activeewres)
        for (double col = 0; col < activeReadRegion.getCols(); col++) {
            /*
             * Calculate the column value of the data to be extracted from the
             * row
             */
            double x = (((activewest + (col * activeewres)) - filewest) / fileewres);
            x = Math.round(x);
            if (x < 0 || x >= nativeRasterRegion.getCols()) {
                /*
                 * Depending on the map type we store a different 'NO VALUE'
                 * value.
                 */
                if (readerMapType > 0) {
                    rowBuffer.putInt(Integer.MAX_VALUE);
                } else if (readerMapType == -1) {
                    rowBuffer.putFloat(Float.NaN);
                } else if (readerMapType == -2) {
                    rowBuffer.putDouble(Double.NaN);
                }
            } else if (readNullValueAtRowCol(currentfilerow, (int) x)) {
                /*
                 * Depending on the map type we store a different 'NO VALUE'
                 * value.
                 */
                if (readerMapType > 0) {
                    rowBuffer.putInt(Integer.MAX_VALUE);
                } else if (readerMapType == -1) {
                    rowBuffer.putFloat(Float.NaN);
                } else if (readerMapType == -2) {
                    rowBuffer.putDouble(Double.NaN);
                }
            } else {
                rowCache.position((int) x * numberOfBytesPerValue);
                if (readerMapType > 0) {
                    /* Integers */
                    int cell = rowCache.getInt();
                    /* File is an integer map file with 0 = novalue */
                    if (cell == 0 && isOldIntegerMap) {
                        rowBuffer.putInt(Integer.MAX_VALUE);
                    } else {
                        /* If map is a reclass then get the reclassed value */
                        if (reclassTable != null) {
                            cell = ((Integer) reclassTable.elementAt(cell)).intValue();
                        }
                        rowBuffer.putInt(cell);
                    }
                } else if (readerMapType == -1) {
                    /* Floating point map with float values. */
                    float cell = rowCache.getFloat();
                    if (reclassTable != null) {
                        cell = ((Integer) reclassTable.elementAt((int) cell)).floatValue();
                    }
                    rowBuffer.putFloat(cell);
                } else if (readerMapType == -2) {
                    /* Floating point map with double values. */
                    double cell = rowCache.getDouble();
                    if (reclassTable != null) {
                        cell = ((Integer) reclassTable.elementAt((int) cell)).doubleValue();
                    }
                    rowBuffer.putDouble(cell);
                }
            }
        }

        return true;
    }

    /**
     * creates a null row.
     *
     * <p><b>INFO:</b> this is a reader method.
     *
     * @param activeCols the numer of columns for the row.
     * @return the null row in its byte representation.
     */
    private byte[] initNullRow(int activeCols) {
        int len = activeCols * numberOfBytesPerValue;
        byte[] nrow = new byte[len];

        if (readerMapType > 0) {
            ByteBuffer src = ByteBuffer.allocate(4);
            src.putInt(Integer.MAX_VALUE);
            byte[] arr = src.array();
            for (int i = 0; i < len; i += 4) System.arraycopy(arr, 0, nrow, i, 4);
        } else if (readerMapType == -1) {
            ByteBuffer src = ByteBuffer.allocate(4);
            src.putFloat(Float.NaN);
            byte[] arr = src.array();
            for (int i = 0; i < len; i += 4) System.arraycopy(arr, 0, nrow, i, 4);
        } else if (readerMapType == -2) {
            ByteBuffer src = ByteBuffer.allocate(8);
            src.putDouble(Double.NaN);
            byte[] arr = src.array();
            for (int i = 0; i < len; i += 8) System.arraycopy(arr, 0, nrow, i, 8);
        }

        return nrow;
    }

    /**
     * read a row of the map from the active region.
     *
     * <p><b>INFO:</b> this is a reader method.
     *
     * @param currentrow the index of the row to read.
     * @param rowdata the buffer to hold the read row.
     */
    private void getMapRow(int currentrow, ByteBuffer rowdata)
            throws IOException, DataFormatException {
        if (compressed) {
            /* Compressed maps */
            if (readerMapType == -2) {
                /* Compressed double map */
                readCompressedFPRowByNumber(rowdata, currentrow);
            } else if (readerMapType == -1) {
                /* Compressed floating point map */
                readCompressedFPRowByNumber(rowdata, currentrow);
            } else if (readerMapType > 0) {
                /* Compressed integer map */
                readCompressedIntegerRowByNumber(rowdata, currentrow);
            }
        } else {
            if (readerMapType < 0) {
                /* Uncompressed floating point map */
                readUncompressedFPRowByNumber(rowdata, currentrow);
            } else if (readerMapType > 0) {
                /* Uncompressed integer map */
                readUncompressedIntegerRowByNumber(rowdata, currentrow);
            }
        }
    }

    /**
     * read a row of data from a compressed floating point map.
     *
     * <p><b>INFO:</b> this is a reader method.
     *
     * @param rowdata the buffer to hold the read row.
     * @param currentrow the index of the row to read.
     */
    private void readCompressedFPRowByNumber(ByteBuffer rowdata, int currentrow)
            throws DataFormatException, IOException {
        // System.out.println("row: " + (currentrow+1) + " position: " + addressesOfRows[currentrow
        // + 1]);

        int offset = (int) (addressesOfRows[currentrow + 1] - addressesOfRows[currentrow]);
        /*
         * The fact that the file is compressed does not mean that the row is
         * compressed. If the first byte is 0 (49), then the row is compressed,
         * otherwise (first byte = 48) the row has to be read in simple XDR
         * uncompressed format.
         */
        byte[] tmp = new byte[offset - 1];
        imageIS.seek(addressesOfRows[currentrow]);
        int firstbyte = (imageIS.read() & 0xff);
        if (firstbyte == 49) {
            /* The row is compressed. */
            // thefile.seek((long) adrows[rn] + 1);
            imageIS.read(tmp, 0, offset - 1);
            Inflater decompresser = new Inflater();
            decompresser.setInput(tmp, 0, tmp.length);
            decompresser.inflate(rowdata.array());
            decompresser.end();
        } else if (firstbyte == 48) {
            imageIS.read(rowdata.array(), 0, offset - 1);
        }
    }

    /**
     * read a row of data from an uncompressed floating point map.
     *
     * <p><b>INFO:</b> this is a reader method.
     *
     * @param rowdata the buffer to hold the read row.
     * @param currentrow the index of the row to read.
     */
    private void readUncompressedFPRowByNumber(ByteBuffer rowdata, int currentrow)
            throws IOException, DataFormatException {
        int datanumber = nativeRasterRegion.getCols() * numberOfBytesPerValue;
        imageIS.seek((currentrow * (long) datanumber));
        imageIS.read(rowdata.array());
    }

    /**
     * read a row of data from a compressed integer point map.
     *
     * <p><b>INFO:</b> this is a reader method.
     *
     * @param rowdata the buffer to hold the read row.
     * @param currentrow the index of the row to read.
     */
    private void readCompressedIntegerRowByNumber(ByteBuffer rowdata, int currentrow)
            throws IOException, DataFormatException {
        int offset = (int) (addressesOfRows[currentrow + 1] - addressesOfRows[currentrow]);

        imageIS.seek(addressesOfRows[currentrow]);
        /*
         * Read how many bytes the values are ex 1 => if encoded: 1 byte for the
         * value and one byte for the count = 2 2 => if encoded: 2 bytes for the
         * value and one byte for the count = 3 etc... etc
         */
        int bytespervalue = (imageIS.read() & 0xff);
        ByteBuffer cell = ByteBuffer.allocate(bytespervalue);
        int cellValue = 0;

        /* Create the buffer in which read the compressed row */
        byte[] tmp = new byte[offset - 1];
        imageIS.read(tmp);
        ByteBuffer tmpBuffer = ByteBuffer.wrap(tmp);
        tmpBuffer.order(ByteOrder.nativeOrder());

        /*
         * Create the buffer in which read the decompressed row. The final
         * decompressed row will always contain 4-byte integer values
         */
        if ((offset - 1) == (bytespervalue * nativeRasterRegion.getCols())) {
            /* There is no compression in this row */
            for (int i = 0; i < offset - 1; i = i + bytespervalue) {
                /* Read the value */
                tmpBuffer.get(cell.array());

                /*
                 * Integers can be of 1, 2, or 4 bytes. As rasterBuffer expects
                 * 4 byte integers we need to pad them with 0's. The order of
                 * the padding is determined by the ByteOrder of the buffer.
                 */
                if (bytespervalue == 1) {
                    cellValue = (cell.get(0) & 0xff);
                } else if (bytespervalue == 2) {
                    cellValue = cell.getShort(0);
                } else if (bytespervalue == 4) {
                    cellValue = cell.getInt(0);
                }
                // if (logger.isDebugEnabled()) logger.debug("tmpint=" + tmpint
                // );
                rowdata.putInt(cellValue);
            }
        } else {
            /*
             * If the row is compressed, then the values appear in pairs (like
             * couples a party). The couple is composed of the count and the
             * value value (WARNING: this can be more than one byte). Therefore,
             * knowing the length of the compressed row we can calculate the
             * number of couples.
             */
            int couples = (offset - 1) / (1 + bytespervalue);

            for (int i = 0; i < couples; i++) {
                /* Read the count of values */
                int count = (tmpBuffer.get() & 0xff);

                /* Read the value */
                tmpBuffer.get(cell.array());

                /*
                 * Integers can be of 1, 2, or 4 bytes. As rasterBuffer expects
                 * 4 byte integers we need to pad them with 0's. The order of
                 * the padding is determined by the ByteOrder of the buffer.
                 */
                if (bytespervalue == 1) {
                    cellValue = (cell.get(0) & 0xff);
                } else if (bytespervalue == 2) {
                    cellValue = cell.getShort(0);
                } else if (bytespervalue == 4) {
                    cellValue = cell.getInt(0);
                }
                /*
                 * Now write the cell value the required number of times to the
                 * raster row data buffer.
                 */
                for (int j = 0; j < count; j++) {
                    // // if (logger.isDebugEnabled()) logger.debug(" " +
                    // tmpint);
                    rowdata.putInt(cellValue);
                }
            }
        }
    }

    /**
     * read a row of data from an uncompressed integer map.
     *
     * <p><b>INFO:</b> this is a reader method.
     *
     * @param rowdata the buffer to hold the read row.
     * @param currentrow the index of the row to read.
     */
    private void readUncompressedIntegerRowByNumber(ByteBuffer rowdata, int currentrow)
            throws IOException, DataFormatException {
        int cellValue = 0;
        ByteBuffer cell = ByteBuffer.allocate(readerMapType);

        /* The number of bytes that are inside a row in the file. */
        int filerowsize = nativeRasterRegion.getCols() * readerMapType;

        /* Position the file pointer to read the row */
        imageIS.seek((currentrow * (long) filerowsize));

        /* Read the row of data from the file */
        ByteBuffer tmpBuffer = ByteBuffer.allocate(filerowsize);
        imageIS.read(tmpBuffer.array());

        /*
         * Transform the readerMapType-size-values to a standard 4 bytes integer
         * value
         */
        while (tmpBuffer.hasRemaining()) {
            // read the value
            tmpBuffer.get(cell.array());

            /*
             * Integers can be of 1, 2, or 4 bytes. As rasterBuffer expects 4
             * byte integers we need to pad them with 0's. The order of the
             * padding is determined by the ByteOrder of the buffer.
             */
            if (readerMapType == 1) {
                cellValue = (cell.get(0) & 0xff);
            } else if (readerMapType == 2) {
                cellValue = cell.getShort(0);
            } else if (readerMapType == 4) {
                cellValue = cell.getInt(0);
            }
            rowdata.putInt(cellValue);
        }
    }

    /**
     * read the null value from the null file (if it exists).
     *
     * <p>Return the information about the particular cell (true if it is novalue, false if it is
     * not a novalue.
     *
     * <p><b>INFO:</b> this is a reader method.
     *
     * @param currentfilerow index of the row.
     * @param currentfilecol index of the column.
     * @return true if it is a novalue.
     */
    private boolean readNullValueAtRowCol(int currentfilerow, int currentfilecol)
            throws IOException {
        /*
         * If the null file doesn't exist and the map is an integer, than it is
         * an old integer-map format, where the novalues are the cells that
         * contain the values 0
         */
        if (imageNullFileIS != null) {
            long byteperrow = (long) Math.ceil(nativeRasterRegion.getCols() / 8.0); // in the
            // null
            // map of
            // cell_misc
            long currentByte = (long) Math.ceil((currentfilecol + 1) / 8.0); // in
            // the
            // null
            // map

            // currentfilerow starts from 0, so it is the row before the one we
            // need
            long byteToRead = (byteperrow * currentfilerow) + currentByte;

            imageNullFileIS.seek(byteToRead - 1);

            int bitposition = (currentfilecol) % 8;

            byte[] thetmp = new byte[1];
            thetmp[0] = imageNullFileIS.readByte();
            BitSet tmp = fromByteArray(thetmp);

            boolean theBit = tmp.get(7 - bitposition);
            return theBit;
        }
        return false;
    }

    /**
     * Getter for nativeRasterRegion.
     *
     * <p><b>INFO:</b> this is a reader method.
     *
     * @return the nativeRasterRegion.
     */
    public JGrassRegion getNativeRasterRegion() {
        return nativeRasterRegion;
    }

    /**
     * Getter for the colorrules.
     *
     * <p><b>INFO:</b> this is a reader method.
     *
     * @return the list of single colorrules.
     */
    public List<String> getColorRules(double[] range) throws IOException {
        JGrassColorTable colorTable = new JGrassColorTable(readerGrassEnv, range);
        return colorTable.getColorRules();
    }

    /**
     * Getter for the categories.
     *
     * <p><b>INFO:</b> this is a reader method.
     *
     * @return the attribute table as read in the categories file
     */
    public List<String> getCategories() throws IOException {

        /*
         * File is a standard file where the categories values are stored in
         * the cats directory.
         */
        BufferedReader rdr = new BufferedReader(new FileReader(readerGrassEnv.getCATS()));
        try {

            /* Instantiate attribute table */
            AttributeTable attTable = new AttributeTable();
            /* Ignore first 4 lines. */
            rdr.readLine();
            rdr.readLine();
            rdr.readLine();
            rdr.readLine();
            /* Read next n lines */
            String line;
            while ((line = rdr.readLine()) != null) {
                /* All lines other than '0:no data' are processed */
                if (line.indexOf("0:no data") == -1) { // $NON-NLS-1$
                    JlsTokenizer tk = new JlsTokenizer(line, ":"); // $NON-NLS-1$
                    if (tk.countTokens() == 2) {
                        float f = Float.parseFloat(tk.nextToken());
                        String att = tk.nextToken().trim();
                        attTable.addAttribute(f, att);
                    } else if (tk.countTokens() == 3) {
                        float f0 = Float.parseFloat(tk.nextToken());
                        float f1 = Float.parseFloat(tk.nextToken());
                        String att = tk.nextToken().trim();
                        attTable.addAttribute(f0, f1, att);
                    }
                }
            }

            List<String> attrs = new ArrayList<String>();
            Enumeration<CellAttribute> categories = attTable.getCategories();
            while (categories.hasMoreElements()) {
                AttributeTable.CellAttribute object = categories.nextElement();
                attrs.add(object.toString());
            }

            return attrs;

        } finally {
            rdr.close();
        }
    }

    /** closes the I/O streams. */
    public void close() throws IOException {
        if (imageIS != null) {
            imageIS.close();
            imageNullFileIS.close();
        }
    }

    /**
     * Creates a {@link BitSet} from an array of bytes.
     *
     * <p><b>INFO:</b> this is a reader method.
     *
     * @param bytes the array of bytes to convert.
     * @return the bitset
     */
    private BitSet fromByteArray(byte[] bytes) {
        BitSet bits = new BitSet();
        for (int i = 0; i < bytes.length * 8; i++) {
            if ((bytes[bytes.length - i / 8 - 1] & (1 << (i % 8))) > 0) {
                bits.set(i);
            }
        }
        return bits;
    }

    /**
     * sets the abortrequired flag to true.
     *
     * <p>As soon as possible that should abort the reader.
     */
    public void abort() {
        abortRequired = true;
    }

    /**
     * Getter for the abortrequired flag.
     *
     * @return the abortRequired flag.
     */
    public boolean isAborting() {
        return abortRequired;
    }

    /**
     * Setter for the noData value.
     *
     * @param noData the nodata value to set.
     */
    public void setNoData(double noData) {
        this.noData = noData;
    }

    /**
     * Getter for the noData value.
     *
     * @return the nodata value.
     */
    public double getNoData() {
        return noData;
    }

    /**
     * Reads the crs definition for the map.
     *
     * <p>The definition for grass maps is held in the location. Grass projection definitions are
     * usually in a non parsable internal format. In JGrass we ask the user to choose the CRS. If
     * the user doesn't do so, the CRS will result to be undefined.
     *
     * @return the {@link CoordinateReferenceSystem} for the map. Null if it is not defined.
     * @throws IOException if there were problems in parsing the CRS file.
     */
    public CoordinateReferenceSystem getCrs() throws IOException {
        String locationPath = readerGrassEnv.getLOCATION().getAbsolutePath();
        CoordinateReferenceSystem readCrs = null;
        String projWtkFilePath;
        projWtkFilePath =
                locationPath
                        + File.separator
                        + JGrassConstants.PERMANENT_MAPSET
                        + File.separator
                        + JGrassConstants.PROJ_WKT;
        File projWtkFile = new File(projWtkFilePath);
        if (projWtkFile.exists()) {

            BufferedReader crsReader = new BufferedReader(new FileReader(projWtkFile));
            StringBuffer wtkString = new StringBuffer();
            try {
                String line = null;
                while ((line = crsReader.readLine()) != null) {
                    wtkString.append(line.trim());
                }
            } finally {
                crsReader.close();
            }
            try {
                readCrs = CRS.parseWKT(wtkString.toString());
            } catch (FactoryException e) {
                throw new IOException(e.getLocalizedMessage());
            }
            return readCrs;
        } else {
            return null;
        }
    }

    /**
     * returns the {@link SampleModel} compatible with the {@link WritableRaster}.
     *
     * @return the sample model compatible with the created raster
     */
    public SampleModel getSampleModel() {
        int[] bands = {0};
        int[] bandOffsets = {0};
        rasterMapWidth = activeReadRegion.getCols();
        rasterMapHeight = activeReadRegion.getRows();
        if (sampleModel == null) {
            if (numberOfBytesPerValue == 8) {
                if (!castDoubleToFloating) {
                    sampleModel =
                            new ComponentSampleModelJAI(
                                    DataBuffer.TYPE_DOUBLE,
                                    rasterMapWidth,
                                    rasterMapHeight,
                                    1,
                                    rasterMapWidth,
                                    bands,
                                    bandOffsets);
                } else {
                    sampleModel =
                            new ComponentSampleModelJAI(
                                    DataBuffer.TYPE_FLOAT,
                                    rasterMapWidth,
                                    rasterMapHeight,
                                    1,
                                    rasterMapWidth,
                                    bands,
                                    bandOffsets);
                }
            } else if (numberOfBytesPerValue == 4 && readerMapType < 0) {
                sampleModel =
                        new ComponentSampleModelJAI(
                                DataBuffer.TYPE_FLOAT,
                                rasterMapWidth,
                                rasterMapHeight,
                                1,
                                rasterMapWidth,
                                bands,
                                bandOffsets);
            } else if (readerMapType > -1) {
                sampleModel =
                        new ComponentSampleModelJAI(
                                DataBuffer.TYPE_INT,
                                rasterMapWidth,
                                rasterMapHeight,
                                1,
                                rasterMapWidth,
                                bands,
                                bandOffsets);
            }
        }
        return sampleModel;
    }

    public int getRasterMapWidth() {
        return rasterMapWidth;
    }

    public int getRasterMapHeight() {
        return rasterMapHeight;
    }

    public double[] getRange() {
        // TODO Auto-generated method stub
        return null;
    }
}
