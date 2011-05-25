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

import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.geotools.gce.grassraster.DummyProgressListener;
import org.geotools.gce.grassraster.GrassBinaryImageReader;
import org.geotools.gce.grassraster.JGrassConstants;
import org.geotools.gce.grassraster.JGrassMapEnvironment;
import org.geotools.gce.grassraster.JGrassRegion;
import org.geotools.gce.grassraster.metadata.GrassBinaryImageMetadata;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.ProgressListener;

/**
 * Grass binary data input/ouput handler.
 * <p>
 * Reading and writing is supported.
 * </p>
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 * @since 3.0
 * @see GrassBinaryImageReader
 * @see GrassBinaryImageReadParam
 * @see GrassBinaryImageMetadata
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/plugin/grassraster/src/main/java/org/geotools/gce/grassraster/core/GrassBinaryRasterWriteHandler.java $
 */
public class GrassBinaryRasterWriteHandler {

    /**
     * {@linkplain ImageOutputStream} used to write the data to file.
     */
    private ImageOutputStream imageOS = null;

    /**
     * {@linkplain ImageOutputStream} used to write the data's null bitmap to file.
     */
    private ImageOutputStream imageNullFileOS = null;

    /**
     * the value used to represent non existing data for in the raster.
     */
    private Double noData = Double.NaN;

    /**
     * The {@linkplain JGrassMapEnvironment environment} needed for raster writing.
     */
    private JGrassMapEnvironment writerGrassEnv = null;

    /**
     * the long array that keeps the addresses of the starting point (bytes in the file) of each
     * raster row.
     */
    private long[] addressesOfRows;

    /**
     * the current position of the pointer in the file.
     */
    private long pointerInFilePosition = 0l;

    /**
     * the range of the raster map as an array of minimum value and maximum value.
     */
    private final double[] range = new double[]{Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};

    /**
     * the data type for the output map.
     * <p>
     * supported are 1 for float and 2 for double (jgrass only supports those two), the default is
     * double.
     * </p>
     */
    private int outputToDiskType = 2;

    private boolean jump = false;

    private boolean abortRequired;

    private JGrassRegion writeRegion = null;

    private ProgressListener monitor = new DummyProgressListener();

    /**
     * A constructor to build a {@link GrassBinaryRasterWriteHandler} usable for writing grass
     * rasters.
     * 
     * @param destMapset the mapset file into which the map has to be written.
     * @param newMapName the name for the written map.
     * @param monitor
     */
    public GrassBinaryRasterWriteHandler( File destMapset, String newMapName,
            ProgressListener monitor ) {
        if (monitor != null)
            this.monitor = monitor;
        writerGrassEnv = new JGrassMapEnvironment(destMapset, newMapName);
        abortRequired = false;
    }

    /**
     * Writes the raster, given an raster iterator and region metadata.
     * 
     * @param renderedImage the {@link RenderedImage} to write.
     * @param columns the columns of the raster to write.
     * @param rows the rows of the raster to write.
     * @param west the western bound of the raster to write.
     * @param south the southern bound of the raster to write.
     * @param xRes the east-west resolution of the raster to write.
     * @param yRes the north-south resolution of the raster to write.
     * @param noDataValue the value representing noData.
     * @throws IOException
     */
    public void writeRaster( RenderedImage renderedImage, int columns, int rows, double west,
            double south, double xRes, double yRes, double noDataValue ) throws IOException {
        boolean hasListeners = false;
        if (!checkStructure())
            throw new IOException("Inconsistent output structure for grass map. Check your paths.");

        /*
         * open the streams: the file for the map to create but also the needed null-file inside of
         * the cell_misc folder
         */
        imageOS = ImageIO.createImageOutputStream(writerGrassEnv.getFCELL());
        imageNullFileOS = ImageIO.createImageOutputStream(writerGrassEnv.getCELLMISC_NULL());

        double east = west + ((double) columns) * xRes;
        double north = south + ((double) rows) * yRes;

        JGrassRegion dataWindow = new JGrassRegion(west, east, south, north, rows, columns);

        createEmptyHeader(rows);

        if (hasListeners && abortRequired) {
            return;
        }

        /*
         * finally writing to disk
         */
        CompressesRasterWriter crwriter = new CompressesRasterWriter(outputToDiskType, noDataValue,
                jump, range, pointerInFilePosition, addressesOfRows, dataWindow, monitor,
                writerGrassEnv.getMapName());
        crwriter.compressAndWrite(imageOS, imageNullFileOS, renderedImage);
        createUtilityFiles(dataWindow);

    }

    /**
     * Calculates the region that is going to be written.
     * 
     * @return the region that will be written by this Writer.
     * @throws IOException
     */
    public JGrassRegion getWriteRegion() throws IOException {
        if (writeRegion == null) {
            File wind = writerGrassEnv.getWIND();
            writeRegion = new JGrassRegion(wind.getAbsolutePath());
        }
        return writeRegion;
    }

    public void setWriteRegion( JGrassRegion writeRegion ) {
        this.writeRegion = writeRegion;
    }

    /**
     * Closes the I/O streams.
     */
    public void close() throws IOException {
        if (imageOS != null) {
            imageOS.close();
            imageNullFileOS.close();
        }
    }

    /**
     * checks if the needed GRASS structure folders are available.
     * <p>
     * <b>NOTE:</b> they could be missing if the mapset has just been created and this is the first
     * file that gets into it.
     * </p>
     * <p>
     * <b>INFO:</b> this is a writer method.
     * </p>
     * 
     * @return true is the structure is ok.
     */
    private boolean checkStructure() {
        File ds;

        File mapset = writerGrassEnv.getMAPSET();
        String name = writerGrassEnv.getCELL().getName();
        String mapsetPath = mapset.getAbsolutePath();
        ds = new File(mapsetPath + File.separator + JGrassConstants.CATS + File.separator);
        if (!ds.exists())
            if (!ds.mkdir())
                return false;
        ds = new File(mapsetPath + File.separator + JGrassConstants.CELL + File.separator);
        if (!ds.exists())
            if (!ds.mkdir())
                return false;
        ds = new File(mapsetPath + File.separator + JGrassConstants.CELL_MISC + File.separator);
        if (!ds.exists())
            if (!ds.mkdir())
                return false;
        ds = new File(mapsetPath + File.separator + JGrassConstants.CELL_MISC + File.separator
                + name);
        if (!ds.exists())
            if (!ds.mkdir())
                return false;
        ds = new File(mapsetPath + File.separator + JGrassConstants.FCELL + File.separator);
        if (!ds.exists())
            if (!ds.mkdir())
                return false;
        ds = new File(mapsetPath + File.separator + JGrassConstants.CELLHD + File.separator);
        if (!ds.exists())
            if (!ds.mkdir())
                return false;
        ds = new File(mapsetPath + File.separator + JGrassConstants.COLR + File.separator);
        if (!ds.exists())
            if (!ds.mkdir())
                return false;
        ds = new File(mapsetPath + File.separator + JGrassConstants.HIST + File.separator);
        if (!ds.exists())
            if (!ds.mkdir())
                return false;
        return true;
    }

    /**
     * Creates the binary header for the raster file.
     * <p>
     * The space for the header of the rasterfile is created, filling the spaces with zeros. After
     * the compression the values will be rewritten
     * </p>
     * 
     * @param rows number of rows that will be written.
     * @throws IOException if an error occurs while trying to write the header.
     */
    private void createEmptyHeader( int rows ) throws IOException {
        addressesOfRows = new long[rows + 1];
        // the size of a long in C?
        imageOS.write(4);
        // write the addresses of the row begins. Since we don't know how
        // much
        // they will be compressed, they will be filled after the
        // compression
        for( int i = 0; i < rows + 1; i++ ) {
            imageOS.writeInt(0);
        }
        pointerInFilePosition = imageOS.getStreamPosition();
        addressesOfRows[0] = pointerInFilePosition;
    }

    /**
     * Creates all support files needed in the grass filesystem for a raster map.
     * 
     * @param dataRegion data region to be written, used for writing of the cellheader.
     */
    private void createUtilityFiles( JGrassRegion dataRegion ) throws IOException {
        // create the right files in the right places
        // cats/<name>
        OutputStreamWriter catsWriter = new OutputStreamWriter(new FileOutputStream(writerGrassEnv
                .getCATS()));
        catsWriter.write("# xyz categories\n#\n#\n 0.00 0.00 0.00 0.00"); //$NON-NLS-1$
        catsWriter.close();

        // cell/<name>
        OutputStreamWriter cellWriter = new OutputStreamWriter(new FileOutputStream(writerGrassEnv
                .getCELL()));
        cellWriter.write(""); //$NON-NLS-1$
        cellWriter.close();

        // cell_misc/<name>/<files>
        // the directory <name> in cell_misc has already been created in
        // writeMapInActiveRegion (or extended) of the Class Mapset (or
        // extended)

        // f_format
        OutputStreamWriter cell_miscFormatWriter = new OutputStreamWriter(new FileOutputStream(
                writerGrassEnv.getCELLMISC_FORMAT()));
        if (outputToDiskType * 4 == 8) {
            cell_miscFormatWriter.write("type: double\nbyte_order: xdr\nlzw_compression_bits: -1"); //$NON-NLS-1$
        } else {
            cell_miscFormatWriter.write("type: float\nbyte_order: xdr\nlzw_compression_bits: -1"); //$NON-NLS-1$
        }

        cell_miscFormatWriter.close();

        // f_quant
        OutputStreamWriter cell_miscQantWriter = new OutputStreamWriter(new FileOutputStream(
                writerGrassEnv.getCELLMISC_QUANT()));
        cell_miscQantWriter.write("round"); //$NON-NLS-1$
        cell_miscQantWriter.close();

        // f_range
        OutputStream cell_miscRangeStream = new FileOutputStream(writerGrassEnv.getCELLMISC_RANGE());
        cell_miscRangeStream.write(double2bytearray(range[0]));
        cell_miscRangeStream.write(double2bytearray(range[1]));
        cell_miscRangeStream.close();

        /*
         * need to reread the wind file to get the proj and zone (GRASS will not work if the cellhd
         * is not equal to the WIND proj)
         */

        JGrassRegion tmp = getWriteRegion();
        createCellhd(tmp.getProj(), tmp.getZone(), dataRegion.getNorth(), dataRegion.getSouth(),
                dataRegion.getEast(), dataRegion.getWest(), dataRegion.getCols(), dataRegion
                        .getRows(), dataRegion.getNSResolution(), dataRegion.getWEResolution(), -1,
                1);

        // hist/<name>
        OutputStreamWriter windFile = new OutputStreamWriter(new FileOutputStream(writerGrassEnv
                .getHIST()));
        Date date = new Date();
        windFile.write(date + "\n"); //$NON-NLS-1$
        windFile.write(writerGrassEnv.getCELL().getName() + "\n"); //$NON-NLS-1$
        windFile.write(writerGrassEnv.getMAPSET().getAbsolutePath() + "\n"); //$NON-NLS-1$
        windFile.write(System.getProperty("user.name") + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
        windFile.write("DCELL\n"); //$NON-NLS-1$
        windFile.write("\n\nCreated by imageio-ext enabled JGrass\n"); //$NON-NLS-1$
        windFile.close();
        // now all the files have been created
    }

    /**
     * Changes the cellhd file inserting the new values passed.
     * 
     * @param chproj the proj value.
     * @param chzone the zone value.
     * @param chn northern boundary.
     * @param chs southern boundary.
     * @param che eastern boundary.
     * @param chw western boundary.
     * @param chcols number of columns.
     * @param chrows number of rows.
     * @param chnsres the north-south resolution.
     * @param chewres the east-west resolution.
     * @param chformat the map type.
     * @param chcompressed the compression type.
     * @throws IOException
     */
    @SuppressWarnings("nls")
    private void createCellhd( int chproj, int chzone, double chn, double chs, double che,
            double chw, int chcols, int chrows, double chnsres, double chewres, int chformat,
            int chcompressed ) throws IOException {
        StringBuffer data = new StringBuffer(512);
        data.append("proj:   " + chproj + "\n").append("zone:   " + chzone + "\n").append(
                "north:   " + chn + "\n").append("south:   " + chs + "\n").append(
                "east:   " + che + "\n").append("west:   " + chw + "\n").append(
                "cols:   " + chcols + "\n").append("rows:   " + chrows + "\n").append(
                "n-s resol:   " + chnsres + "\n").append("e-w resol:   " + chewres + "\n").append(
                "format:   " + chformat + "\n").append("compressed:   " + chcompressed);
        OutputStreamWriter windFile = new OutputStreamWriter(new FileOutputStream(writerGrassEnv
                .getCELLHD()));
        windFile.write(data.toString());
        windFile.close();
    }

    /**
     * Converts a double value to its byte representation.
     * 
     * @param value the value to convert.
     * @return the byte array for the double.
     */
    private byte[] double2bytearray( double value ) {
        long l = Double.doubleToLongBits(value);
        byte[] b = new byte[8];
        int shift = 64 - 8;
        for( int k = 0; k < 8; k++, shift -= 8 ) {
            b[k] = (byte) (l >>> shift);
        }
        return b;
    }

    /**
     * Setter for the noData value.
     * 
     * @param noData the nodata value to set.
     */
    public void setNoData( double noData ) {
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
     * <p>
     * The definition for grass maps is held in the location. Grass projection definitions are
     * usually in a non parsable internal format. In JGrass we ask the user to choose the CRS. If
     * the user doesn't do so, the CRS will result to be undefined.
     * </p>
     * 
     * @return the {@link CoordinateReferenceSystem} for the map. Null if it is not defined.
     * @throws IOException if there were problems in parsing the CRS file.
     */
    public CoordinateReferenceSystem getCrs() throws IOException {
        String locationPath = writerGrassEnv.getLOCATION().getAbsolutePath();
        CoordinateReferenceSystem readCrs = null;
        String projWtkFilePath;
        projWtkFilePath = locationPath + File.separator + JGrassConstants.PERMANENT_MAPSET
                + File.separator + JGrassConstants.PROJ_WKT;
        File projWtkFile = new File(projWtkFilePath);
        if (projWtkFile.exists()) {

            BufferedReader crsReader = new BufferedReader(new FileReader(projWtkFile));
            StringBuffer wtkString = new StringBuffer();
            try {
                String line = null;
                while( (line = crsReader.readLine()) != null ) {
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
     * sets the abortrequired flag to true.
     * <p>
     * As soon as possible that should abort the reader.
     * </p>
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

}
