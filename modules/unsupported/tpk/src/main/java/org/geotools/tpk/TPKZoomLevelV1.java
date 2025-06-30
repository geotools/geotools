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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * TPKZoomLevel interface implementation for ESRI Compact Cache V1 files
 *
 * <p>TPK is a ZIP archive
 *
 * <p>In V1 the bundle index is a separate file from the bundle data and index entries are each 5-bytes long. The index
 * is stored in column-major order.
 */
public class TPKZoomLevelV1 implements TPKZoomLevel {

    // pattern isolates the "base" row and column number for each bundle (in hexidecimal)
    static Pattern PARSE_BUNDLE_NAME = Pattern.compile("^.*/R([0-9a-f]+)C([0-9a-f]+)\\.bundle$");

    static int BUNDLE_DIMENSION = 128; // bundle is 128 columns by 128 rows
    static int INDEX_ENTRY_LENGTH = 5; // each index entry is 5 bytes long
    static int INDEX_HEADER_LENGTH = 16; // first 16 bytes of index file is header data
    static int DATA_HEADER_LENGTH = 60; // first 60 bytes of bundle data file is header
    static int DATA_LENGTH_LENGTH = 4; // tile data length stored in 4 bytes
    static int HEXADECIMAL = 16; // row and column numbers in bundle name are base-16 encoded

    // if tile data offset is less than this value then tile does not exist!
    static int MINIMUM_DATA_OFFSET =
            DATA_HEADER_LENGTH + BUNDLE_DIMENSION * BUNDLE_DIMENSION * DATA_LENGTH_LENGTH; // (65596)

    // injected via constructor
    private ZipFile theTPK;
    private Map<String, ZipEntry> zipEntryMap;

    // there are getters for these 5 members
    private final long zoomLevel; // zoom level number (0 to a max of 24)
    private long minRow; // minimum row number found in TPK for this zoom level
    private long maxRow; // maximum row number found in TPK for this zoom level
    private long minColumn; // minimum col number found in TPK for this zoom level
    private long maxColumn; // maximum col number found in TPK for this zoom level

    private long max_row_column; // max value for row and col (2** zoomLevel) -1
    private List<TPKBundle> bundles; // list of bundles for this zoom level

    public TPKZoomLevelV1(
            ZipFile theTPK, // TPK file is a ZIP file
            Map<String, ZipEntry> zipEntryMap, // map path/names to ZipEntry
            List<String> bundleNames, // names of bundle data files for this zoom level
            List<String> indexNames, // names of bundle index files for this zoom level
            long zoomLevel) {

        this.theTPK = theTPK;
        this.zipEntryMap = zipEntryMap;
        this.zoomLevel = zoomLevel;

        minColumn = Long.MAX_VALUE;
        maxColumn = Long.MIN_VALUE;
        minRow = Long.MAX_VALUE;
        maxRow = Long.MIN_VALUE;

        // maximum value for row and column at this zoom level
        max_row_column = (long) (Math.pow(2, zoomLevel) - 1);

        bundles = new ArrayList<>();
        init(bundleNames, indexNames);
    }

    // ***********************************************************************
    // **                                                                   **
    // **   Public Methods                                                  **
    // **                                                                   **
    // ***********************************************************************

    /**
     * On subsequent uses of a TPK file we must use this method to set current references!!
     *
     * @param theTPK -- reference to the TPK ZipFile object
     * @param zipEntryMap -- reference to the ZipEntry mapping object
     */
    @Override
    public void setTPKandEntryMap(ZipFile theTPK, Map<String, ZipEntry> zipEntryMap) {
        this.theTPK = theTPK;
        this.zipEntryMap = zipEntryMap;
    }

    /**
     * Read the tile data for the given coverage and return a list of tile objects to the caller. Note the read order we
     * used is optimized for this version of the content cache (both for the index and the data files)
     *
     * @param top -- topmost row of coverage
     * @param bottom -- bottommost row of coverage
     * @param left -- leftmost column of coverage
     * @param right -- rightmost column of coverage
     * @param format -- format of tile data (PNG, JPEG)
     * @return -- list of TPKTile objects
     */
    @Override
    public List<TPKTile> getTiles(long top, long bottom, long left, long right, String format) {

        // first find the tile coverage in the bundle indexes
        List<TPKTile> tiles = makeTileSet(top, bottom, left, right, format);

        // then sort the tiles into the optimal read order and go read them!!
        tiles.stream().sorted(new TPKTile.TPKTileSorter()).forEach(this::readTileData);

        return tiles;
    }

    /**
     * When done with TPK file call this method for each Zoom Level to free up held resources and remove object
     * references that would prevent garbage collection
     */
    @Override
    public void releaseResources() {
        bundles.forEach(TPKBundle::releaseResources); // close open input streams
        zipEntryMap = null; // don't keep references alive!!
        theTPK = null;
    }

    @Override
    public long getZoomLevel() {
        return zoomLevel;
    }

    @Override
    public long getMinRow() {
        return minRow;
    }

    @Override
    public long getMaxRow() {
        return maxRow;
    }

    @Override
    public long getMinColumn() {
        return minColumn;
    }

    @Override
    public long getMaxColumn() {
        return maxColumn;
    }

    // ***********************************************************************
    // **                                                                   **
    // **   Private Methods                                                 **
    // **                                                                   **
    // ***********************************************************************

    /**
     * Given the list of bundle data files and bundles indexes for this zoom level, isolate each "pair" and parse them
     * to create Bundle objects
     *
     * @param bundleNames -- names of each bundle for zoom level
     * @param indexNames -- name for each bundle index for zoom level
     */
    private void init(List<String> bundleNames, List<String> indexNames) {
        for (String bundleName : bundleNames) {
            Matcher m = PARSE_BUNDLE_NAME.matcher(bundleName);
            if (m.matches()) {
                String row_start = m.group(1);
                String col_start = m.group(2);
                String indexName = bundleName.replace("bundle", "bundlx");
                // do we have a matching bundle index file?
                if (indexNames.contains(indexName)) {
                    parseBundle(bundleName, indexName, row_start, col_start);
                }
            } else {
                throw new RuntimeException("Unable to parse bundle name??");
            }
        }
    }

    /**
     * Do an initial scan of a bundle/index file pair to determine the row and column coverage contained in the bundle
     * and create a bundle object for subsequent access
     *
     * @param bundleName -- path/name of the bundle file
     * @param indexName -- path/name of the bundle index file
     * @param row_start -- hex string yields the "base" row number for this bundle
     * @param col_start -- hex string yields the "base" column number for this bundle
     */
    private void parseBundle(String bundleName, String indexName, String row_start, String col_start) {

        // get the starting row and column number for this bundle/index pair
        long baseRow = Long.parseLong(row_start, HEXADECIMAL);
        long baseColumn = Long.parseLong(col_start, HEXADECIMAL);

        // as long as row and column are in allowable range ...
        if (baseRow <= max_row_column && baseColumn <= max_row_column) {

            TPKBundle bundle = new TPKBundle(
                    bundleName, indexName, baseColumn, baseRow, this::TPKSupplier, this::zipEntryMapSupplier);

            long indexReadOffset = INDEX_HEADER_LENGTH; // skip first 16 bytes of index
            for (int col = 0; col < BUNDLE_DIMENSION; col++) { // 128 columns of 128 rows
                for (int row = 0; row < BUNDLE_DIMENSION; row++) {

                    // calculate row and column this index entry is for
                    long thisRow = baseRow + row;
                    long thisColumn = baseColumn + col;

                    // read the index entry and convert it to a data file offset
                    long tileDataOffset = getTileDataOffset(bundle, indexReadOffset);

                    // make sure the row and column are "in bounds" for this zoom level
                    if (thisColumn <= max_row_column && thisRow <= max_row_column) {

                        thisRow = max_row_column - thisRow;

                        // if the tile data offset is less than the minimum data offset
                        // then the tile does not exist
                        if (tileDataOffset >= MINIMUM_DATA_OFFSET) {
                            // update min/max values for this bundle
                            bundle.minRow = Math.min(bundle.minRow, thisRow);
                            bundle.maxRow = Math.max(bundle.maxRow, thisRow);
                            bundle.minColumn = Math.min(bundle.minColumn, thisColumn);
                            bundle.maxColumn = Math.max(bundle.maxColumn, thisColumn);
                        }
                    }

                    // quit early??
                    if (thisColumn == max_row_column && thisRow > max_row_column) {
                        col = BUNDLE_DIMENSION;
                        row = BUNDLE_DIMENSION;
                    }

                    indexReadOffset += INDEX_ENTRY_LENGTH; // next slot in bundle index
                }
            }

            // update min/max values for the zoom level
            this.minRow = Math.min(this.minRow, bundle.minRow);
            this.maxRow = Math.max(this.maxRow, bundle.maxRow);
            this.minColumn = Math.min(this.minColumn, bundle.minColumn);
            this.maxColumn = Math.max(this.maxColumn, bundle.maxColumn);

            bundles.add(bundle);
        }
    }

    /**
     * Read the bundle index for the given set of tiles and return a corresponding list of TPKTile objects. Note that we
     * read the index in the optimal order for this cache format
     *
     * @param top -- topmost row of coverage
     * @param bottom -- bottommost row of coverage
     * @param left -- leftmost column of coverage
     * @param right -- rightmost column of coverahe
     * @param format -- format tile data is in (JPEG, PNG)
     * @return -- list of TPKTile objects
     */
    private List<TPKTile> makeTileSet(long top, long bottom, long left, long right, String format) {
        // get the first bundle for this zoom level
        TPKBundle bundle = bundles.get(0);
        int bundleIndex = 0;

        List<TPKTile> tiles = new ArrayList<>();
        for (long col = left; col <= right; col++) {
            for (long row = top; row >= bottom; row--) {

                // find bundle for tile if we don't already have it!
                if (!bundle.inBundle(col, row)) {
                    final long c = col;
                    final long r = row;
                    TPKBundle saveBundle = bundle;
                    bundle = bundles.stream()
                            .filter(b -> b.inBundle(c, r))
                            .findFirst()
                            .orElse(null);
                    if (bundle == null) {
                        bundle = saveBundle;
                        continue;
                    }
                    bundleIndex = bundles.indexOf(bundle);
                }

                // calculate the index position we need to read
                long bundleRow = max_row_column - row - bundle.baseRow;
                long indexReadOffset = INDEX_HEADER_LENGTH
                        + ((col - bundle.baseColumn) * BUNDLE_DIMENSION + bundleRow) * INDEX_ENTRY_LENGTH;

                // read the tile index and get the offset to the tile data
                long tileDataOffset = getTileDataOffset(bundle, indexReadOffset);
                TPKTile.TileInfo ti = new TPKTile.TileInfo(0, tileDataOffset);

                // build a TPKTile object for this tile
                TPKTile tile = new TPKTile(zoomLevel, col, row, format, ti, bundleIndex);
                tiles.add(tile);
            }
        }
        return tiles;
    }

    /**
     * For a given TPKTile object read the actual tile data
     *
     * @param tile -- input (and output!) TPKTile instance
     */
    private void readTileData(TPKTile tile) {
        // get the tile's bundle
        TPKBundle bundle = bundles.get(tile.bundleNum);
        // read the tile's data via the bndle
        byte[] tileData = getTileData(bundle, tile.tileInfo.tileDataOffset);
        // and set the reference in our TPKTile object
        tile.setTileData(tileData);
    }

    /**
     * Given a bundle and an bundle index offset, read the 5-byte data pointer in the index and convert that to a
     * byte-offset in the bundle data file
     *
     * @param bundle -- bundle in question
     * @param indexReadOffset -- offset within the bundle tile index
     * @return -- the converted data offset "pointer"
     */
    @SuppressWarnings("PMD.UnnecessaryCast")
    private long getTileDataOffset(TPKBundle bundle, long indexReadOffset) {
        byte[] tileIndex = bundle.bundleIndx.read(indexReadOffset, INDEX_ENTRY_LENGTH);

        // convert 5-byte value into a byte-offset
        return (long) tileIndex[0] & 0xff
                | (long) (tileIndex[1] & 0xff) << 8
                | (long) (tileIndex[2] & 0xff) << 16
                | (long) (tileIndex[3] & 0xff) << 24
                | (long) (tileIndex[4] & 0xff) << 32;
    }

    /**
     * Given the offset of the tile data within the bundle, find and retrieve the tile data
     *
     * @param bundle - the bundle object that contains the tile data in question
     * @param tileDataOffset -- offset of tile data within bundle
     * @return -- null or a byte-array of tile data
     */
    private byte[] getTileData(TPKBundle bundle, long tileDataOffset) {

        int dataLength = getTileDataLength(bundle, tileDataOffset);

        // if data length is greater than zero then read that many bytes and return them to
        // caller, otherwise return null
        if (dataLength > 0) {
            return bundle.bundleData.read(tileDataOffset + DATA_LENGTH_LENGTH, dataLength);
        } else {
            return null;
        }
    }

    /**
     * Given the offset of the tile data within the bundle read the first four bytes to determine the length of the tile
     * data
     *
     * @param bundle -- the bundle object that contains the tile we want
     * @param tileDataOffset -- offset of tile data within the bundle
     * @return -- the integer value found at the given offset
     */
    private int getTileDataLength(TPKBundle bundle, long tileDataOffset) {

        // the first 4 bytes of the tile data is the length!!
        byte[] dataLen = bundle.bundleData.read(tileDataOffset, DATA_LENGTH_LENGTH);

        // convert to an integer value
        return dataLen[0] & 0xff | (dataLen[1] & 0xff) << 8 | (dataLen[2] & 0xff) << 16 | (dataLen[3] & 0xff) << 24;
    }

    /**
     * Supplier function injected into TPKBundle objects at construction
     *
     * @return -- ref to the current ZipFile object
     */
    private ZipFile TPKSupplier() {
        return theTPK;
    }

    /**
     * Supplier function injected into TPKBundle objects at construction
     *
     * @return -- ref to the current zipEntryMap
     */
    private Map<String, ZipEntry> zipEntryMapSupplier() {
        return zipEntryMap;
    }
}
