/*
 *    ImageI/O-Ext - OpenSource Java Image translation Library
 *    http://www.geo-solutions.it/
 *    http://java.net/projects/imageio-ext/
 *    (C) 2007 - 2009, GeoSolutions
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    either version 3 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.imageio.unidata;

import it.geosolutions.imageio.stream.eraf.EnhancedRandomAccessFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.util.List;


/**
 * An wrapper for variable index files.
 * 
 * @author Andrea Antonello
 * @author Simone Giannecchini, GeoSolutions
 *
 */
class UnidataSlice2DIndexManager {

    private static final long ADDRESS_SIZE = 8l;    
    
    private static long ADDRESS_POSITION = 4l;
    
    private EnhancedRandomAccessFile raf;
    
    private File file;
    
    private int numberOfRecords;

    public UnidataSlice2DIndexManager( File file ) {
        this.file = file;
    }

    public void open() throws IOException {
        raf = new EnhancedRandomAccessFile(file, "r");
        raf.setByteOrder(ByteOrder.BIG_ENDIAN);
        numberOfRecords = raf.readInt();
    }

    /**
     * Read a {@link UnidataSlice2DIndex} from file given the imageIndex.
     * 
     * @param imageIndex the imageIndex to look for.
     * @return the {@link UnidataSlice2DIndex} for the picked image.
     * @throws IOException
     */
    public UnidataSlice2DIndex getSlice2DIndex( int imageIndex ) throws IOException {
        long addressPosition = ADDRESS_POSITION + imageIndex * ADDRESS_SIZE;

        raf.seek(addressPosition);
        long dataPosition = raf.readLong();
        long endDataPosition = raf.readLong();

        raf.seek(dataPosition);
        int tIndex = raf.readInt();
        int zIndex = raf.readInt();
        int stringSize = (int) (endDataPosition - raf.getFilePointer());
        byte[] stringBytes = new byte[stringSize];
        raf.read(stringBytes);
        String varName = new String(stringBytes);

        return  new UnidataSlice2DIndex(tIndex, zIndex, varName);
    }
    public void dispose() throws IOException {
        if (raf != null) {
            raf.close();
        }
    }
    /**
     * Utility method to write an index file.
     * 
     * @param file the file to write to.
     * @param indexList the list of {@link UnidataSlice2DIndex} to dump to file.
     * @throws IOException
     */
    public static void writeIndexFile( File file, List<UnidataSlice2DIndex> indexList ) throws IOException {
        writeIndexFile(file, indexList, 2);
    }
    
    /**
     * Utility method to write an index file.
     * 
     * @param file the file to write to.
     * @param indexList the list of {@link UnidataSlice2DIndex} to dump to file.
     * @throws IOException
     */
    public static void writeIndexFile( File file, List<UnidataSlice2DIndex> indexList, int dimensions ) throws IOException {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            int size = indexList.size();
            // write number of records
            raf.writeInt(size);
            long dataPosition = ADDRESS_POSITION + (size + 1) * ADDRESS_SIZE; // the +1 is to have the end address

            long[] pointer = new long[size];
            raf.seek(dataPosition);

            for( int i = 0; i < size; i++ ) {
                UnidataSlice2DIndex slice2DIndex = indexList.get(i);
                long pos = raf.getFilePointer();
                pointer[i] = pos;
                raf.writeInt(slice2DIndex.getTIndex());
                raf.writeInt(slice2DIndex.getZIndex());
                raf.write(slice2DIndex.getVariableName().getBytes());
            }
            long dataEnd = raf.getFilePointer();

            raf.seek(ADDRESS_POSITION);
            for( long address : pointer ) {
                raf.writeLong(address);
            }
            // add also the data end position
            raf.writeLong(dataEnd);

        } finally {
            if (raf != null) {
                raf.close();
            }
        }
    }

    public int getNumberOfRecords() throws IOException {
        return numberOfRecords;
    }
}
