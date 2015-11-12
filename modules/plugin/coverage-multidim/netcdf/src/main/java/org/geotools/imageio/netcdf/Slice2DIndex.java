/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.netcdf;

import it.geosolutions.imageio.stream.eraf.EnhancedRandomAccessFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.util.List;


/**
 * A bean that represents a row in the index used for mapping 2d grids to 2d slices in NetCDF files.
 * 
 * <p>
 * The elements are:
 * <ol>
 *      <li><b>imageIndex</b> the index of the image to work with</li>
 *      <li><b>tIndex</b> the index of the time dimension for this 2d slice</li>
 *      <li><b>zIndex</b> the index of the elevation dimension for this 2d slice</li>
 *      <li><b>variableName</b> the name of this variable, e.g. temperature</li>
 * </ol>
 * 
 * @author Andrea Antonello
 * @author Simone Giannecchini, GeoSolutions
 *
 */
public class Slice2DIndex {

    /** DEFAULT_INDEX */
    public static final int DEFAULT_INDEX = -1;
    
    private int tIndex = DEFAULT_INDEX;
    
    private int zIndex = DEFAULT_INDEX;
    
    private final String variableName;
    
    public Slice2DIndex(String variableName) {
        this(DEFAULT_INDEX, DEFAULT_INDEX, variableName);
    }

    public Slice2DIndex(int tIndex, int zIndex, String variableName) {
        org.geotools.util.Utilities.ensureNonNull("variableName", variableName);
        this.tIndex = tIndex;
        this.zIndex = zIndex;
        this.variableName = variableName;
    }

    public int getZIndex() {
        return zIndex;
    }

    public int getTIndex() {
        return tIndex;
    }

    public String getVariableName() {
        return variableName;
    }

    @Override
    public String toString() {
        return "UnidataVariableIndex [tIndex=" + tIndex + ", zIndex="
                + zIndex + ", variableName=" + variableName + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + tIndex;
        result = prime * result + ((variableName == null) ? 0 : variableName.hashCode());
        result = prime * result + zIndex;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Slice2DIndex other = (Slice2DIndex) obj;
        if (tIndex != other.tIndex)
            return false;
        if (variableName == null) {
            if (other.variableName != null)
                return false;
        } else if (!variableName.equals(other.variableName))
            return false;
        if (zIndex != other.zIndex)
            return false;
        return true;
    }
      
    /**
     * An wrapper for variable index files.
     * 
     * @author Andrea Antonello
     * @author Simone Giannecchini, GeoSolutions
     *
     */
    public static class Slice2DIndexManager {

        private static final long ADDRESS_SIZE = 8l;

        private static long ADDRESS_POSITION = 4l;

        private EnhancedRandomAccessFile raf;

        private File file;

        private int numberOfRecords;

        public Slice2DIndexManager( File file ) {
            this.file = file;
        }

        public void open() throws IOException {
            raf = new EnhancedRandomAccessFile(file, "r");
            raf.setByteOrder(ByteOrder.BIG_ENDIAN);
            numberOfRecords = raf.readInt();
        }

        /**
         * Read a {@link Slice2DIndex} from file given the imageIndex.
         * 
         * @param imageIndex the imageIndex to look for.
         * @return the {@link Slice2DIndex} for the picked image.
         * @throws IOException
         */
        public synchronized Slice2DIndex getSlice2DIndex( int imageIndex ) throws IOException {
            // Synchronized these access due to the RAF usage.
            // concurrent seeks and reads on the same RAF may
            // may result into unexpected results
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

            return  new Slice2DIndex(tIndex, zIndex, varName);
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
         * @param indexList the list of {@link Slice2DIndex} to dump to file.
         * @throws IOException
         */
        public static void writeIndexFile( File file, List<Slice2DIndex> indexList ) throws IOException {
            writeIndexFile(file, indexList, 2);
        }
        
        /**
         * Utility method to write an index file.
         * 
         * @param file the file to write to.
         * @param indexList the list of {@link Slice2DIndex} to dump to file.
         * @throws IOException
         */
        public static void writeIndexFile( File file, List<Slice2DIndex> indexList, int dimensions ) throws IOException {
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
                    Slice2DIndex slice2DIndex = indexList.get(i);
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
}
