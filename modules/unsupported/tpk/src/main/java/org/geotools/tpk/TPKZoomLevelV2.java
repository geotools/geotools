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
 * TPKZoomLevel interface implementation for ESRI Compact Cache V2 files
 *
 * <p>TPK is a ZIP archive
 *
 * <p>In V2 the bundle index is a incorporated into the same file as the bundle data. The index entries are each 8-bytes
 * long and have both the tile data length and tile data offset.
 *
 * <p>Also note that the index is stored in row-major order INSTEAD of column-major order!
 */
public class TPKZoomLevelV2 implements TPKZoomLevel {

    // pattern isolates the "base" row and column number for each bundle (in hexidecimal)
    static Pattern PARSE_BUNDLE_NAME = Pattern.compile("^.*/R([0-9a-f]+)C([0-9a-f]+)\\.bundle$");

    static int BUNDLE_DIMENSION = 128; // bundle is 128 columns by 128 rows
    static int INDEX_ENTRY_LENGTH = 8; // each index entry is 8 bytes long in a V2 cache
    static int DATA_HEADER_LENGTH = 64; // first 64 bytes of V2 bundle data file is header
    static int HEXADECIMAL = 16; // row and column numbers in bundle name are base-16 encoded

    // injected via constructor or via "setTPKandEntryMap"
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

    // constructor
    public TPKZoomLevelV2(
            ZipFile theTPK, // a TPK file is a ZIP file!
            Map<String, ZipEntry> zipEntryMap, // maps file path/name to ZipEntry
            List<String> bundleNames, // path/names for all bundles in this zoom level
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
        init(bundleNames);
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
     * used is optimized for this version of the content cache (ie we try to always read forward as much as possible!!)
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
     * Given the list of bundle data files for this zoom level, parse them to create Bundle objects
     *
     * @param bundleNames -- names of each bundle for zoom level
     */
    private void init(List<String> bundleNames) {
        for (String bundleName : bundleNames) {
            Matcher m = PARSE_BUNDLE_NAME.matcher(bundleName);
            if (m.matches()) {
                String row_start = m.group(1);
                String col_start = m.group(2);
                parseBundle(bundleName, row_start, col_start);
            } else {
                throw new RuntimeException("Unable to parse bundle name??");
            }
        }
    }

    /**
     * Do an initial scan of a bundle index to determine the row and column coverage contained in the bundle and create
     * a bundle object for subsequent access
     *
     * @param bundleName -- path/name of the bundle file
     * @param rowStart -- hex string yields the "base" row number for this bundle
     * @param colStart -- hex string yields the "base" column number for this bundle
     */
    private void parseBundle(String bundleName, String rowStart, String colStart) {

        // get the starting row and column number for this bundle/index pair
        long baseRow = Long.parseLong(rowStart, HEXADECIMAL);
        long baseColumn = Long.parseLong(colStart, HEXADECIMAL);

        // as long as row and column are in allowable range ...
        if (baseRow <= max_row_column && baseColumn <= max_row_column) {

            TPKBundle bundle =
                    new TPKBundle(bundleName, null, baseColumn, baseRow, this::TPKSupplier, this::zipEntryMapSupplier);

            long indexReadOffset = DATA_HEADER_LENGTH; // skip first 64 bytes of index
            for (int row = 0; row < BUNDLE_DIMENSION; row++) { // 128 columns of 128 rows
                for (int col = 0; col < BUNDLE_DIMENSION; col++) {

                    // calculate row and column this index entry is for
                    long thisRow = baseRow + row;
                    long thisColumn = baseColumn + col;

                    // read the index entry and convert it to a data file offset
                    TPKTile.TileInfo tileInfo = getTileInfo(bundle, indexReadOffset);

                    // make sure the row and column are "in bounds" for this zoom level
                    if (thisColumn <= max_row_column && thisRow <= max_row_column) {

                        thisRow = max_row_column - thisRow;

                        // if the tile is zero then the tile does not exist
                        if (tileInfo.tileLength > 0) {
                            // update min/max values for this bundle
                            bundle.minRow = Math.min(bundle.minRow, thisRow);
                            bundle.maxRow = Math.max(bundle.maxRow, thisRow);
                            bundle.minColumn = Math.min(bundle.minColumn, thisColumn);
                            bundle.maxColumn = Math.max(bundle.maxColumn, thisColumn);
                        }
                    }

                    // quit early??
                    if (thisRow == max_row_column && thisColumn > max_row_column) {
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
     * @param right -- rightmost column of coverage
     * @param format -- format tile data is in (JPEG, PNG)
     * @return -- list of TPKTile objects
     */
    private List<TPKTile> makeTileSet(long top, long bottom, long left, long right, String format) {
        TPKBundle bundle = bundles.get(0);
        int bundleIndex = 0;
        List<TPKTile> tiles = new ArrayList<>();
        for (long row = top; row >= bottom; row--) {
            for (long col = left; col <= right; col++) {

                // find bundle for tile
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

                // calculate position of the tile index
                long bundleRow = max_row_column - row - bundle.baseRow;
                long indexReadOffset = DATA_HEADER_LENGTH
                        + (bundleRow * BUNDLE_DIMENSION + col - bundle.baseColumn) * INDEX_ENTRY_LENGTH;

                // read the tile index and get the offset to the tile data
                TPKTile.TileInfo ti = getTileInfo(bundle, indexReadOffset);

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

        // read tile data if present
        byte[] tileData = null;
        TPKTile.TileInfo ti = tile.tileInfo;
        if (ti.tileLength > 0) {
            tileData = bundle.bundleData.read(ti.tileDataOffset, ti.tileLength);
        }

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
    private TPKTile.TileInfo getTileInfo(TPKBundle bundle, long indexReadOffset) {
        byte[] tileIndex = bundle.bundleData.read(indexReadOffset, INDEX_ENTRY_LENGTH);

        // convert 5-byte little-endian value into a long
        long dataOffset = (long) (tileIndex[0] & 0xff)
                + ((long) (tileIndex[1] & 0xff) << 8)
                + ((long) (tileIndex[2] & 0xff) << 16)
                + ((long) (tileIndex[3] & 0xff) << 24)
                + ((long) (tileIndex[4] & 0xff) << 32);

        // convert 3-byte little-endian value into an int
        int dataLength = (tileIndex[5] & 0xff) + ((tileIndex[6] & 0xff) << 8) + ((tileIndex[7] & 0xff) << 16);

        return new TPKTile.TileInfo(dataLength, dataOffset);
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
