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

package org.geotools.tpk;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * A zoom level consists of 1 or more bundles A bundle is physically always 128 columns by 128 rows
 * but may be "logically" smaller (ie it doesn't necessarily have data for each row/column)
 */
public class TPKBundle {

    // when we need to access the TPKFile/specific files within we use these
    // provider functions
    private Supplier<ZipFile> TPKFile;
    private Supplier<Map<String, ZipEntry>> zipEntryMap;

    public SequentialInputFile bundleData; // the bundle data file
    public SequentialInputFile bundleIndx; // the bundle index file
    public long baseRow; // "first" row represented by bundle
    public long baseColumn; // "first" column represented by bundle

    // these four values are populated by the initial scan of the bundle's index and
    // they represent rows and columns for which the bundle has data!
    public long minRow;
    public long maxRow;
    public long minColumn;
    public long maxColumn;

    // only constructor
    public TPKBundle(
            String bundleName,
            String indexName,
            long baseColumn,
            long baseRow,
            Supplier<ZipFile> TPKFile,
            Supplier<Map<String, ZipEntry>> zipEntryMap) {

        // a couple of injected supplier functions
        this.TPKFile = TPKFile;
        this.zipEntryMap = zipEntryMap;

        // bundle data file MUST always be provided
        bundleData = new SequentialInputFile(bundleName);

        // V2 caches don't have bundle index files so this is optional
        if (indexName != null) {
            bundleIndx = new SequentialInputFile(indexName);
        }

        // the base column and row for the bundle
        this.baseColumn = baseColumn;
        this.baseRow = baseRow;

        // actual calculated coverage will be stored in these 4 values
        minColumn = Long.MAX_VALUE;
        maxColumn = Long.MIN_VALUE;
        minRow = Long.MAX_VALUE;
        maxRow = Long.MIN_VALUE;
    }

    public void releaseResources() {
        bundleData.close(); // close the bundle date SequentialInputFile object
        if (bundleIndx != null) { // and the bundle inde if we have one!!
            bundleIndx.close();
        }
    }

    /**
     * Predicate function to determine if the column,row coordinates are contained in the "coverage"
     * of this bundle
     *
     * @param column -- column number
     * @param row -- row number
     * @return -- true if tile at column,row is in this bundle otherwise false
     */
    public boolean inBundle(long column, long row) {
        return column >= minColumn && column <= maxColumn && row >= minRow && row <= maxRow;
    }

    /**
     * This object is used to manage the bundle data file and the bundle index file. We are using
     * the InputStream object returned by ZipFile.getInputStream(ZipEntry) to read data from these
     * files. The idea is to read in a forward direction (skipping bytes appropriately) whenever
     * possible. If the request is for an offset that is "behind" our current position the object
     * will close the current InputStream, get a "new" one and satisfy the read request.
     */
    public class SequentialInputFile {
        private String name; // path/name from the zipEntryMap
        private InputStream stream; // stream obtained from ZipFile object
        private long currentReadPosition; // our current "read" position in the stream

        /**
         * Constructor -- note that we only "open" the stream when needed (read request)
         *
         * @param name -- name of file
         */
        private SequentialInputFile(String name) {
            this.name = name;
            stream = null;
            currentReadPosition = 0;
        }

        /**
         * At a given offset read a given number of bytes and returned them as a new byte array
         *
         * @param offset -- where in the stream to start reading
         * @param length -- how many bytes to read
         * @return -- a byte array of "length" bytes
         */
        public byte[] read(long offset, int length) {
            byte[] result = new byte[length];
            try {

                // if the stream isn't open or if the stream is "beyond" the offset
                if (currentReadPosition > offset || stream == null) {
                    // close any existing stream
                    if (stream != null) {
                        stream.close();
                        currentReadPosition = 0;
                    }

                    // get the ZipEntry for this file and get a new InputStream for it
                    ZipEntry zipEntry = zipEntryMap.get().get(name);
                    stream = TPKFile.get().getInputStream(zipEntry);
                }

                // determine if we need to skip over any bytes in the stream
                long toSkip = offset - currentReadPosition;
                while (toSkip > 0) {
                    long skipped = stream.skip(toSkip);
                    toSkip -= skipped;
                    currentReadPosition += skipped; // update position!
                }

                // we need a loop here as the stream limits us to reading 8k bytes at a time
                int readOffset = 0;
                while (length > 0) {
                    int bytesRead = stream.read(result, readOffset, length);

                    if (bytesRead == -1) {
                        Logger.getLogger(this.getClass().getName())
                                .warning("Didn't read correctly");
                        length = 0;
                        result = null;
                    } else {
                        readOffset += bytesRead;
                        currentReadPosition += bytesRead;
                        length -= bytesRead;
                    }
                }
            } catch (IOException ex) {
                throw new RuntimeException("Caught error reading sequential input ZipEntry stream");
            }
            return result;
        }

        /** Clean up on aisle 5 -- release resources and re-initialize members */
        private void close() {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) { // don't care
                }
                stream = null; // re-init
            }
            currentReadPosition = 0; // re-init
        }
    }
}
