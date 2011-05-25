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
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.zip.Deflater;

import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import javax.media.jai.iterator.RectIter;

import org.geotools.gce.grassraster.DummyProgressListener;
import org.geotools.gce.grassraster.JGrassRegion;
import org.geotools.util.SimpleInternationalString;
import org.opengis.util.ProgressListener;

/**
 * <p>
 * Write compressed JGrass rasters to disk
 * </p>
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 * @since 1.1.0
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/plugin/grassraster/src/main/java/org/geotools/gce/grassraster/core/CompressesRasterWriter.java $
 */
public class CompressesRasterWriter {

    private int outputToDiskType = 0;

    private double novalue = -9999.0;

    private boolean jump = false;

    private double[] range = new double[]{Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};

    private long pointerInFilePosition = 0;

    private long[] rowaddresses = null;

    private JGrassRegion dataWindow = null;

    private ProgressListener monitor = new DummyProgressListener();

    private final String mapName;

    /**
     * Preparing the environment for compressing and writing the map to disk
     * 
     * @param _outputToDiskType
     * @param _novalue
     * @param _jump
     * @param _range
     * @param _pointerInFilePosition
     * @param _rowaddresses
     * @param _dataWindow
     * @param monitor
     */
    public CompressesRasterWriter( int _outputToDiskType, double _novalue, boolean _jump,
            double[] _range, long _pointerInFilePosition, long[] _rowaddresses,
            JGrassRegion _dataWindow, ProgressListener monitor, String mapName ) {
        outputToDiskType = _outputToDiskType;
        novalue = _novalue;
        jump = _jump;
        range = _range;
        pointerInFilePosition = _pointerInFilePosition;
        rowaddresses = _rowaddresses;
        dataWindow = _dataWindow;
        this.mapName = mapName;
        if (monitor != null)
            this.monitor = monitor;
    }

    /**
     * Compress and write data from a {@link RectIter map iterator}.
     * <p>
     * This method converts every single row of the buffer of values to bytes, as needed by the
     * deflater. Then the byterows are compressed and then written to file. Every rows first byte
     * carries the information about compression (0 = not compressed, 1 = compressed). At the begin
     * the place for the header is written to file, in the end the header is re-written with the
     * right rowaddresses (at the begin we do not know how much compression will influence).
     * </p>
     * 
     * @param theCreatedFile - handler for the main map file
     * @param theCreatedNullFile - handler for the file of the null map (in cell_misc)
     * @param renderedImage
     * @return successfull or not
     * @throws IOException
     */
    public void compressAndWrite( ImageOutputStream theCreatedFile,
            ImageOutputStream theCreatedNullFile, RenderedImage renderedImage ) throws IOException {
        // set the number of bytes needed for the values to write to disk
        int numberofbytes = outputToDiskType * 4;
        // set the the novalue to identify the nulls

        int dataWindowCols = dataWindow.getCols();
        int dataWindowRows = dataWindow.getRows();
        /*
         * the underlying byte array is needed as input to the deflater create it with the size of
         * the column numberofbytes (8 for double, 4 for float), which is to define how we write to
         * disk
         */
        byte[] rowAsBytes = new byte[dataWindowCols * numberofbytes];
        ByteBuffer rowAsByteBuffer = ByteBuffer.wrap(rowAsBytes);

        int numberOfValuesPerRow = dataWindowCols;
        int rest = numberOfValuesPerRow % 8;
        int paddings = 0;
        if (rest != 0) {
            paddings = 8 - rest;
        }
        BitSet nullbits = new BitSet(numberOfValuesPerRow + paddings);

        // iterate over the number of rows to compress every row and
        // write the result to disk

        int height = renderedImage.getHeight();
        int width = renderedImage.getWidth();
        // FIXME here I want to exploit tiling, not have the whole image loaded,
        // but when I do, I get an
        RandomIter iterator = RandomIterFactory.create(renderedImage,
                new Rectangle(0, 0, width, height));
        // NewWritableFileRandomIter iterator =
        // NewFileImageRandomIterFactory.createWritableFileRandomIter((FileImage) renderedImage,
        // new Rectangle(0, 0, width, height));
        int k = 0;
        monitor.started();
        monitor.setTask(new SimpleInternationalString("Writing map to disk: " + mapName));
        float progress = 0;
        for( int i = 0; i < dataWindowRows; i++ ) {
            for( int j = 0; j < dataWindowCols; j++ ) {
                double value = iterator.getSampleDouble(j,i,0);

                if (Double.isNaN(value) || value == novalue) {
                    // put in the map the placeholder = 0.0 ...
                    if (numberofbytes == 8) {
                        rowAsByteBuffer.putDouble(0.0);
                    } else {
                        rowAsByteBuffer.putFloat(0f);
                    }
                    // ...and create the bitarray for the nullmap
                    nullbits.set(k);
                    k++;
                } else {
                    // since we have to reread all the values, let's get the
                    // range
                    if (value < range[0])
                        range[0] = value;
                    if (value > range[1])
                        range[1] = value;

                    /*
                     * convert the double row in a sequence of byte as needed by the deflater
                     */
                    if (numberofbytes == 8) {
                        rowAsByteBuffer.putDouble(value);
                    } else {
                        rowAsByteBuffer.putFloat((float) value);
                    }
                    /*
                     * ...and create the bitarray for the nullmap (in this case 0 is ok, so we just
                     * increment the counter k
                     */
                    k++;

        

                }
            }
//iterator.done();
            /*
             * now the bitset is complete... just need to write it to disk to create in one time the
             * row (in cell_misc)
             */
            int l = 0;
            byte[] bytearray = new byte[(numberOfValuesPerRow + paddings) / 8];
            for( int e = 0; e < (numberOfValuesPerRow + paddings) / 8; e++ ) {
                bytearray[e] = 0;
                for( int f = 0; f < 8; f++ ) {
                    if (nullbits.get(l)) {
                        bytearray[e] += (byte) Math.pow(2.0, (double) (7 - f));
                    }
                    l++;
                }
            }
            theCreatedNullFile.write(bytearray);
            nullbits.clear();
            k = 0;

            /*
             * now the row is converted to an array of bytes (ByteBuffer's method wrap applies
             * changes on the ByteBuffer to the bytearray and vice versa. We can start with the
             * deflater.
             */
            byte[] output = new byte[rowAsBytes.length * 2];
            /*
             * lenght2 since not always compressing gives the expected/needed result :)
             */
            Deflater compresser = new Deflater();
            compresser.setInput(rowAsBytes);
            compresser.finish();
            int compressedDataLength = compresser.deflate(output);

            /*
             * now write to file the compressed row and set the right rowaddress.
             */
            theCreatedFile.seek(pointerInFilePosition);
            /*
             * jgrass always uses compression, so the first byte of the row will always be 49, i.e.
             * 1 which means that the row is compressed
             */
            theCreatedFile.write(49);
            theCreatedFile.write(output, 0, compressedDataLength);

            rowaddresses[i + 1] = pointerInFilePosition = theCreatedFile.getStreamPosition();
//            System.out.println("row: " + (i+1) + " position: " + pointerInFilePosition);
            
            rowAsByteBuffer.clear();
            
            progress = (float) (progress + 100f * i / dataWindowRows);
            monitor.progress(progress);
        }
        monitor.complete();
        /*
         * now that all the compressed rows are written to file, we have to write their addresses in
         * the header
         */
        theCreatedFile.seek(1);
        for( int i = 0; i < rowaddresses.length; i++ ) {
            theCreatedFile.writeInt((int) rowaddresses[i]);
        }
    }
    public JGrassRegion getDataWindow() {
        return dataWindow;
    }

    public boolean isJump() {
        return jump;
    }

    public double getNovalue() {
        return novalue;
    }

    public int getOutputToDiskType() {
        return outputToDiskType;
    }

    public long getPointerInFilePosition() {
        return pointerInFilePosition;
    }

    public double[] getRange() {
        return range;
    }

    public long[] getRowaddresses() {
        return rowaddresses;
    }
}
