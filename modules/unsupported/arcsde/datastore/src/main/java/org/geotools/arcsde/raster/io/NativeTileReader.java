/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.arcsde.raster.io;

import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeQuery;
import com.esri.sde.sdk.client.SeRaster;
import com.esri.sde.sdk.client.SeRasterConstraint;
import com.esri.sde.sdk.client.SeRasterTile;
import com.esri.sde.sdk.client.SeRow;
import com.esri.sde.sdk.client.SeSqlConstruct;
import java.awt.Dimension;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.arcsde.ArcSdeException;
import org.geotools.arcsde.raster.info.RasterCellType;
import org.geotools.arcsde.raster.info.RasterDatasetInfo;
import org.geotools.arcsde.session.Command;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.session.ISessionPool;
import org.geotools.arcsde.session.UnavailableConnectionException;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.grid.GridEnvelope;

/**
 * Offers an iterator like interface to fetch ArcSDE raster tiles.
 *
 * @author Gabriel Roldan (OpenGeo)
 * @since 2.5.4
 */
@SuppressWarnings({"nls"})
final class NativeTileReader implements TileReader {

    private static final Logger LOGGER = Logging.getLogger(NativeTileReader.class);

    private final RasterDatasetInfo rasterInfo;

    private final long rasterId;

    private final int pyramidLevel;

    private final GridEnvelope requestedTiles;

    private final ISessionPool sessionPool;

    private ISession session;

    private boolean started;

    private final int pixelsPerTile;

    private final int tileDataLength;

    private int bitsPerSample;

    private final RasterCellType nativeCellType;

    private QueryObjects queryObjects;

    private final TileDataFetcher dataFetcher;

    /** @see DefaultTiledRasterReader#nextRaster() */
    private static class QueryRasterCommand extends Command<Void> {

        private SeQuery preparedQuery;

        private SeRow row;

        private final SeRasterConstraint rasterConstraint;

        private final String rasterColumn;

        private final String rasterTable;

        private final long rasterId;

        /**
         * @param rConstraint indicates which bands, pyramid level and grid envelope to query
         * @param rasterTable indicates which raster table to query
         * @param rasterColumn indicates what raster column in the raster table to query
         * @param rasterId indicates which raster in the raster catalog to query
         */
        public QueryRasterCommand(
                final SeRasterConstraint rConstraint,
                final String rasterTable,
                final String rasterColumn,
                final long rasterId) {
            this.rasterConstraint = rConstraint;
            this.rasterTable = rasterTable;
            this.rasterColumn = rasterColumn;
            this.rasterId = rasterId;
        }

        @Override
        public Void execute(ISession session, SeConnection connection)
                throws SeException, IOException {

            final SeSqlConstruct sqlConstruct = new SeSqlConstruct(rasterTable);
            /*
             * Filter by the given raster id
             */
            final String rasterIdFilter = rasterColumn + " = " + rasterId;
            sqlConstruct.setWhere(rasterIdFilter);

            final String[] rasterColumns = {rasterColumn};
            preparedQuery = new SeQuery(connection, rasterColumns, sqlConstruct);
            preparedQuery.prepareQuery();
            preparedQuery.execute();

            this.row = preparedQuery.fetch();
            if (row == null) {
                return null;
            }

            preparedQuery.queryRasterTile(rasterConstraint);

            return null;
        }

        public SeQuery getPreparedQuery() {
            return preparedQuery;
        }

        public SeRow getSeRow() {
            return row;
        }
    }

    /**
     * Creates a {@link TileReader} that reads tiles out of ArcSDE for the given {@code
     * preparedQuery} and {@code SeRow} using the given {@code session}, in the native raster
     * format.
     *
     * <p>As for any object that receives a {@link ISession session}, the same rule applies: this
     * class is not responsible of {@link ISession#dispose() disposing} the session, but the calling
     * code is.
     */
    NativeTileReader(
            final ISessionPool sessionPool,
            final RasterDatasetInfo rasterInfo,
            final long rasterId,
            final int pyramidLevel,
            final GridEnvelope tileRange) {
        this.sessionPool = sessionPool;
        this.rasterInfo = rasterInfo;
        this.rasterId = rasterId;
        this.pyramidLevel = pyramidLevel;
        this.requestedTiles = tileRange;

        final Dimension tileSize = rasterInfo.getTileDimension(rasterId);

        this.pixelsPerTile = tileSize.width * tileSize.height;

        this.nativeCellType = rasterInfo.getNativeCellType();
        this.bitsPerSample = nativeCellType.getBitsPerSample();
        this.tileDataLength =
                (int) Math.ceil(((double) pixelsPerTile * (double) bitsPerSample) / 8D);

        final RasterCellType targetCellType = rasterInfo.getTargetCellType(rasterId);
        this.dataFetcher = TileDataFetcher.getTileDataFetcher(this.nativeCellType, targetCellType);

        int rasterIndex = rasterInfo.getRasterIndex(rasterId);
        int maxTileX = rasterInfo.getNumTilesWide(rasterIndex, pyramidLevel) - 1;
        int maxTileY = rasterInfo.getNumTilesHigh(rasterIndex, pyramidLevel) - 1;

        if (tileRange.getLow(0) < 0
                || tileRange.getLow(1) < 0
                || tileRange.getHigh(0) > maxTileX
                || tileRange.getHigh(1) > maxTileY) {
            throw new IllegalArgumentException(
                    "Invalid tile range for raster #"
                            + rasterId
                            + "/"
                            + pyramidLevel
                            + ": "
                            + tileRange
                            + ". Valid range is 0.."
                            + maxTileX
                            + " 0.."
                            + maxTileY);
        }
    }

    /** @see org.geotools.arcsde.raster.io.TileReader#getBitsPerSample() */
    public int getBitsPerSample() {
        return bitsPerSample;
    }

    /** @see org.geotools.arcsde.raster.io.TileReader#getPixelsPerTile() */
    public int getPixelsPerTile() {
        return pixelsPerTile;
    }

    /** @see org.geotools.arcsde.raster.io.TileReader#getNumberOfBands() */
    public int getNumberOfBands() {
        return rasterInfo.getNumBands();
    }

    /** @see org.geotools.arcsde.raster.io.TileReader#getTileWidth() */
    public int getTileWidth() {
        return rasterInfo.getTileWidth(rasterId);
    }

    /** @see org.geotools.arcsde.raster.io.TileReader#getTileHeight() */
    public int getTileHeight() {
        return rasterInfo.getTileHeight(rasterId);
    }

    /** @see org.geotools.arcsde.raster.io.TileReader#getTilesWide() */
    public int getTilesWide() {
        return requestedTiles.getSpan(0);
    }

    /** @see org.geotools.arcsde.raster.io.TileReader#getTilesHigh() */
    public int getTilesHigh() {
        return requestedTiles.getSpan(1);
    }

    /** @see org.geotools.arcsde.raster.io.TileReader#getBytesPerTile() */
    public int getBytesPerTile() {
        return tileDataLength;
    }

    /**
     * Creates and executes the {@link SeQuery} that's used to fetch the required tiles from the
     * specified raster, and stores (as member variables) the {@link SeRow} to fetch the tiles from
     * and the {@link SeQuery} to be closed at the TileReader's disposal
     */
    private void execute() throws IOException {
        final GridEnvelope requestedTiles = this.requestedTiles;
        this.queryObjects = execute(requestedTiles);
        this.started = true;
        this.lastTileX = this.lastTileY = -1;
    }

    private QueryObjects execute(final GridEnvelope requestedTiles) throws IOException {

        final int rasterIndex = rasterInfo.getRasterIndex(rasterId);

        /*
         * Create the raster constraint to query the needed tiles out of the specified raster at the
         * given pyramid level
         */
        final SeRasterConstraint rConstraint;
        try {
            final int numberOfBands;
            numberOfBands = rasterInfo.getNumBands();

            int[] bandsToQuery = new int[numberOfBands];
            for (int bandN = 1; bandN <= numberOfBands; bandN++) {
                bandsToQuery[bandN - 1] = bandN;
            }

            int minTileX = requestedTiles.getLow(0);
            int minTileY = requestedTiles.getLow(1);
            int maxTileX = requestedTiles.getHigh(0);
            int maxTileY = requestedTiles.getHigh(1);
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(
                        "Requesting tiles [x="
                                + minTileX
                                + "-"
                                + maxTileX
                                + ", y="
                                + minTileY
                                + "-"
                                + maxTileY
                                + "] from tile range [x=0-"
                                + (rasterInfo.getNumTilesWide(rasterIndex, pyramidLevel) - 1)
                                + ", y=0-"
                                + (rasterInfo.getNumTilesHigh(rasterIndex, pyramidLevel) - 1)
                                + "]");
            }
            // SDEPoint tileOrigin = rAttr.getTileOrigin();

            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Tiled image size: " + requestedTiles);
            }

            final int interleaveType = SeRaster.SE_RASTER_INTERLEAVE_BIP;

            rConstraint = new SeRasterConstraint();
            rConstraint.setBands(bandsToQuery);
            rConstraint.setLevel(pyramidLevel);
            rConstraint.setEnvelope(minTileX, minTileY, maxTileX, maxTileY);
            rConstraint.setInterleave(interleaveType);
        } catch (SeException se) {
            throw new ArcSdeException(se);
        }

        /*
         * Obtain the ISession this tile reader will work with until exhausted
         */
        try {
            if (this.session == null) {
                // lets share connections as we're going to do read only operations
                final boolean transactional = false;
                this.session = sessionPool.getSession(transactional);

                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer(
                            "Using "
                                    + session
                                    + " to read raster #"
                                    + rasterId
                                    + " on Thread "
                                    + Thread.currentThread().getName()
                                    + ". Tile set: "
                                    + requestedTiles);
                }
            }
        } catch (UnavailableConnectionException e) {
            // really bad luck..
            throw new RuntimeException(e);
        }

        final String rasterTable = rasterInfo.getRasterTable();
        final String rasterColumn = rasterInfo.getRasterColumns()[0];
        final QueryRasterCommand queryCommand =
                new QueryRasterCommand(rConstraint, rasterTable, rasterColumn, rasterId);

        session.issue(queryCommand);

        final SeRow row = queryCommand.getSeRow();

        final SeQuery preparedQuery = queryCommand.getPreparedQuery();

        return new QueryObjects(preparedQuery, row);
    }

    private static class QueryObjects {
        SeQuery preparedQuery;

        SeRow row;

        public QueryObjects(SeQuery preparedQuery, SeRow row) {
            this.preparedQuery = preparedQuery;
            this.row = row;
        }
    }

    /** @see org.geotools.arcsde.raster.io.TileReader#getTile(int, int, byte[][]) */
    public void getTile(final int tileX, final int tileY, byte[][] data) throws IOException {
        assert data == null || data.length == getNumberOfBands();

        final int numberOfBands = getNumberOfBands();

        TileInfo[] tileInfo = getTileInfo();
        TileInfo t;
        for (int b = 0; b < numberOfBands; b++) {
            t = tileInfo[b]; // new TileInfo(getPixelsPerTile());
            t.setTileData(data[b]);
            tileInfo[b] = t;
        }

        getTile(tileX, tileY, tileInfo);
    }

    TileInfo[] tileInfo;

    /** @see org.geotools.arcsde.raster.io.TileReader#getTile(int, int, short[][]) */
    public void getTile(int tileX, int tileY, short[][] data) throws IOException {
        assert data == null || data.length == getNumberOfBands();

        final int numberOfBands = getNumberOfBands();

        TileInfo[] tileInfo = getTileInfo();
        TileInfo t;
        for (int b = 0; b < numberOfBands; b++) {
            t = tileInfo[b]; // new TileInfo(getPixelsPerTile());
            t.setTileData(data[b]);
            tileInfo[b] = t;
        }

        getTile(tileX, tileY, tileInfo);
    }

    private TileInfo[] getTileInfo() {
        if (tileInfo == null) {
            final int numberOfBands = getNumberOfBands();
            tileInfo = new TileInfo[numberOfBands];
            for (int b = 0; b < numberOfBands; b++) {
                TileInfo t = new TileInfo(getPixelsPerTile());
                tileInfo[b] = t;
            }
        }
        return tileInfo;
    }

    /** @see org.geotools.arcsde.raster.io.TileReader#getTile(int, int, int[][]) */
    public void getTile(int tileX, int tileY, int[][] data) throws IOException {
        assert data == null || data.length == getNumberOfBands();
        final int numberOfBands = getNumberOfBands();

        TileInfo[] tileInfo = getTileInfo();
        TileInfo t;
        for (int b = 0; b < numberOfBands; b++) {
            t = tileInfo[b]; // new TileInfo(getPixelsPerTile());
            t.setTileData(data[b]);
            tileInfo[b] = t;
        }

        getTile(tileX, tileY, tileInfo);
    }

    /** @see org.geotools.arcsde.raster.io.TileReader#getTile(int, int, float[][]) */
    public void getTile(int tileX, int tileY, float[][] data) throws IOException {
        assert data == null || data.length == getNumberOfBands();
        final int numberOfBands = getNumberOfBands();

        TileInfo[] tileInfo = getTileInfo();
        TileInfo t;
        for (int b = 0; b < numberOfBands; b++) {
            t = tileInfo[b]; // new TileInfo(getPixelsPerTile());
            t.setTileData(data[b]);
            tileInfo[b] = t;
        }

        getTile(tileX, tileY, tileInfo);
    }

    /** @see org.geotools.arcsde.raster.io.TileReader#getTile(int, int, double[][]) */
    public void getTile(int tileX, int tileY, double[][] data) throws IOException {
        assert data == null || data.length == getNumberOfBands();
        final int numberOfBands = getNumberOfBands();

        TileInfo[] tileInfo = getTileInfo();
        TileInfo t;
        for (int b = 0; b < numberOfBands; b++) {
            t = tileInfo[b]; // new TileInfo(getPixelsPerTile());
            t.setTileData(data[b]);
            tileInfo[b] = t;
        }

        getTile(tileX, tileY, tileInfo);
    }

    private void getTile(final int tileX, final int tileY, TileInfo[] target) throws IOException {
        // System.out.printf("fetchTile %d, %d\n", tileX, tileY);
        try {
            fetchTile(tileX, tileY, target);
        } catch (IOException e) {
            dispose();
            throw e;
        } catch (RuntimeException e) {
            dispose();
            throw (RuntimeException)
                    new RuntimeException(
                                    "Error geting tile "
                                            + tileX
                                            + ","
                                            + tileY
                                            + " on raster "
                                            + rasterInfo.getRasterTable()
                                            + "#"
                                            + this.rasterId)
                            .initCause(e);
        }
    }

    private int lastTileX = -1;

    private int lastTileY = -1;

    /**
     * How many random tiles requested (ie, not consecutive) before re executing the original
     * request as a rewind
     */
    private static final int RANDOM_THRESHOLD = Integer.MAX_VALUE;

    private int nonConsecutiveCallCount;

    private void fetchTile(final int tileX, final int tileY, TileInfo[] target) throws IOException {
        SeRasterTile[] seTile = null;

        if (isConsecutive(tileX, tileY)) {
            while (lastTileX != tileX || lastTileY != tileY) {
                seTile = nextTile();
            }
        } else {
            if (nonConsecutiveCallCount == RANDOM_THRESHOLD) {
                nonConsecutiveCallCount = 0;
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.info(
                            "Number of random (non consecutive) tile request exceeded"
                                    + " predefined threshold. Rewind by executing original request again");
                }
                dispose();
                fetchTile(tileX, tileY, target);
                return;
            } else {
                nonConsecutiveCallCount++;
                seTile = fetchSingleTile(tileX, tileY);
            }
        }

        if (lastTileX == getMaxTileX() && lastTileY == getMaxTileY()) {
            dispose();
        }

        extractTile(seTile, target);
    }

    /** @see org.geotools.arcsde.raster.io.TileReader#getMaxTileX() */
    public int getMaxTileX() {
        return requestedTiles.getHigh(0);
    }

    /** @see org.geotools.arcsde.raster.io.TileReader#getMaxTileY() */
    public int getMaxTileY() {
        return requestedTiles.getHigh(1);
    }

    /** Executes a separate request to fetch this single tile */
    private SeRasterTile[] fetchSingleTile(final int tileX, final int tileY) throws IOException {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info(
                    "fetchSingleTile raster #"
                            + this.rasterId
                            + ", plevel "
                            + pyramidLevel
                            + ", tile "
                            + tileX
                            + ", "
                            + tileY);
        }
        final int width = 1;
        final int height = 1;
        final GridEnvelope requestTiles = new GridEnvelope2D(tileX, tileY, width, height);

        final QueryObjects singleTileQueryObjects = execute(requestTiles);
        final SeQuery query = singleTileQueryObjects.preparedQuery;
        final SeRow row = singleTileQueryObjects.row;
        final TileFetchCommand command = new TileFetchCommand(row, nativeCellType);
        SeRasterTile[] tileData;

        try {
            tileData = readTile(command);
        } finally {
            session.close(query);
        }

        return tileData;
    }

    /**
     * Determines whether the tile defined by {@code tileX, tileY} is consecutive to the original
     * request, whether it is exactly the next in the stream or any other one that follows the last
     * tile fetched from the original request.
     */
    private boolean isConsecutive(final int tileX, final int tileY) {
        if (tileX > lastTileX && tileY >= lastTileY) {
            return true;
        }
        if (tileX <= lastTileX && tileY > lastTileY) {
            return true;
        }
        return false;
    }

    private TileFetchCommand sequentialFetchCommand;

    private SeRasterTile[] nextTile() throws IOException {
        if (!started) {
            execute();
            SeRow row = queryObjects.row;
            RasterCellType nativeType = nativeCellType;
            sequentialFetchCommand = new TileFetchCommand(row, nativeType);
        }

        SeRasterTile[] tileData = readTile(sequentialFetchCommand);

        if (lastTileX == -1 && lastTileY == -1) {
            lastTileX = getMinTileX();
            lastTileY = getMinTileY();
        } else {
            lastTileX++;
            if (lastTileX > getMaxTileX()) {
                lastTileX = getMinTileX();
                lastTileY++;
            }
        }

        return tileData;
    }

    private SeRasterTile[] readTile(final TileFetchCommand fetchCommand) throws IOException {

        final int numBands = getNumberOfBands();

        SeRasterTile[] tile = new SeRasterTile[numBands];

        SeRasterTile bandTile;
        for (int i = 0; i < numBands; i++) {
            bandTile = session.issue(fetchCommand);
            if (bandTile == null) {
                throw new IllegalStateException("There are no more tiles to fetch");
            }
            tile[i] = bandTile;
        }
        return tile;
    }

    /** @see org.geotools.arcsde.raster.io.TileReader#dispose() */
    public void dispose() {
        if (session != null) {
            // System.err.println("TileReader disposing " + session + " on Thread "
            // + Thread.currentThread().getName());
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer(
                        "TileReader disposing "
                                + session
                                + " on Thread "
                                + Thread.currentThread().getName());
            }
            if (queryObjects != null) {
                try {
                    SeQuery preparedQuery = queryObjects.preparedQuery;
                    session.close(preparedQuery);
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Closing tile reader's prepared Query", e);
                }
            }
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer(
                        "Disposing " + session + " on thread " + Thread.currentThread().getName());
            }
            session.dispose();
            session = null;

            // get ready for more invocations
            queryObjects = null;
            started = false;
            lastTileX = lastTileY = -1;
        }
    }

    /**
     * Disposes as to make sure the {@link ISession session} is returned to the pool even if a
     * failing or non careful client left this object hanging around
     *
     * @see #dispose()
     * @see java.lang.Object#finalize()
     */
    @Override
    @SuppressWarnings("deprecation") // finalize is deprecated in Java 9
    protected void finalize() {
        dispose();
    }

    public int getMinTileX() {
        return requestedTiles.getLow(0);
    }

    public int getMinTileY() {
        return requestedTiles.getLow(1);
    }

    public String getServerName() {
        return sessionPool.getConfig().getServerName();
    }

    public String getRasterTableName() {
        return rasterInfo.getRasterTable();
    }

    public int getPyramidLevel() {
        return pyramidLevel;
    }

    public long getRasterId() {
        return rasterId;
    }

    private void extractTile(final SeRasterTile[] seTile, TileInfo[] target) {
        final int numberOfBands = getNumberOfBands();
        assert numberOfBands == seTile.length;
        assert numberOfBands == target.length;

        SeRasterTile tile;
        TileInfo bandData;
        for (int bandN = 0; bandN < numberOfBands; bandN++) {
            tile = seTile[bandN];

            final byte[] bitMaskData = tile.getBitMaskData();
            final int numPixelsRead = tile.getNumPixels();
            final long bandId = tile.getBandId().longValue();
            final int colIndex = tile.getColumnIndex();
            final int rowIndex = tile.getRowIndex();
            final Number noData = rasterInfo.getNoDataValue(rasterId, bandN);
            bandData = target[bandN];
            bandData.setBandId(bandId);
            bandData.setColumnIndex(colIndex);
            bandData.setRowIndex(rowIndex);
            bandData.setNumPixelsRead(numPixelsRead);
            bandData.setBitmaskData(bitMaskData);
            bandData.setNoDataValue(noData);

            dataFetcher.setTileData(tile, bandData);
        }
    }
}
