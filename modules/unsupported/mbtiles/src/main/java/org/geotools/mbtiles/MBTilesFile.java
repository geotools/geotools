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

package org.geotools.mbtiles;

import static java.lang.String.format;
import static org.geotools.jdbc.util.SqlUtil.prepare;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.data.jdbc.datasource.ManageableDataSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.util.SqlUtil;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class MBTilesFile implements AutoCloseable {

    public static final String PRAGMA_JOURNAL_MODE_OFF = "PRAGMA journal_mode=OFF";

    public class TileIterator implements Iterator<MBTilesTile>, Closeable {

        ResultSet rs;

        Statement st;

        Connection cx;

        Boolean next = null;

        TileIterator(ResultSet rs, Statement st, Connection cx) {
            this.rs = rs;
            this.st = st;
            this.cx = cx;
        }

        @Override
        public boolean hasNext() {
            if (next == null) {
                try {
                    next = rs.next();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            return next;
        }

        @Override
        public MBTilesTile next() {
            try {
                MBTilesTile entry = new MBTilesTile(rs.getLong(1), rs.getLong(2), rs.getLong(3));
                entry.setData(rs.getBytes(4));
                return entry;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                next = null;
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void close() throws IOException {
            try {
                try {
                    rs.close();
                } finally {
                    try {
                        st.close();
                    } finally {
                        cx.close();
                    }
                }
            } catch (SQLException e) {
                throw new IOException(e);
            }
        }
    }

    // constant strings
    protected final String TABLE_METADATA = "metadata";

    protected final String TABLE_TILES = "tiles";

    protected final String TABLE_GRIDS = "grids";

    protected final String TABLE_GRID_DATA = "grid_data";

    protected final String MD_NAME = "name";

    protected final String MD_TYPE = "type";

    protected final String MD_VERSION = "version";

    protected final String MD_DESCRIPTION = "description";

    protected final String MD_FORMAT = "format";

    protected final String MD_BOUNDS = "bounds";

    protected final String MD_ATTRIBUTION = "attribution";

    protected final String MD_MINZOOM = "minzoom";

    protected final String MD_MAXZOOM = "maxzoom";

    protected final String MD_JSON = "json";

    /** Logger */
    protected static final Logger LOGGER = Logging.getLogger(MBTilesFile.class);

    public static final CoordinateReferenceSystem SPHERICAL_MERCATOR;

    static {
        try {
            SPHERICAL_MERCATOR = CRS.decode("EPSG:3857", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static final ReferencedEnvelope WORLD_ENVELOPE =
            new ReferencedEnvelope(
                    -20037508.34, 20037508.34, -20037508.34, 20037508.34, SPHERICAL_MERCATOR);

    /** database file */
    protected File file;

    /** connection pool */
    protected final DataSource connPool;

    /** Boolean indicating if journal must be disabled or not */
    protected boolean disableJournal;

    /** Creates a new empty MbTilesFile, generating a new file. */
    public MBTilesFile() throws IOException {
        this(File.createTempFile("temp", ".mbtiles"));
    }

    /**
     * Creates a new empty MbTilesFile, generating a new file, also deciding if journal must be
     * disabled or not.
     *
     * <p>This constructor assumes no credentials are required to connect to the database.
     */
    public MBTilesFile(boolean disableJournal) throws IOException {
        this(File.createTempFile("temp", ".mbtiles"), disableJournal);
    }

    /**
     * Creates a MbTilesFile from an existing file.
     *
     * <p>This constructor assumes no credentials are required to connect to the database.
     */
    public MBTilesFile(File file) throws IOException {
        this(file, null, null, false);
    }

    /**
     * Creates a MbTilesFile from an existing file, also deciding if journal must be disabled or
     * not.
     *
     * <p>This constructor assumes no credentials are required to connect to the database.
     */
    public MBTilesFile(File file, boolean disableJournal) throws IOException {
        this(file, null, null, disableJournal);
    }

    /** Creates a MbTilesFile from an existing file specifying database credentials. */
    public MBTilesFile(File file, String user, String passwd, boolean disableJournal)
            throws IOException {
        this.file = file;
        this.disableJournal = disableJournal;
        Map<String, Serializable> params = new HashMap<>();
        params.put(MBTilesDataStoreFactory.DATABASE.key, file.getPath());
        params.put(
                MBTilesDataStoreFactory.DBTYPE.key, (String) MBTilesDataStoreFactory.DBTYPE.sample);

        this.connPool = new MBTilesDataStoreFactory().createDataSource(params, false);
    }

    /**
     * Create an MBTilesFile from an SQL DataSource connected to an MBTiles file. Behaviour is
     * undefined if the DataSource is any other form of database.
     */
    public MBTilesFile(DataSource dataSource) {
        this.connPool = dataSource;
    }

    /** Store MetaData in file */
    public void saveMetaData(MBTilesMetadata metaData) throws IOException {
        try {
            Connection cx = connPool.getConnection();
            try {
                saveMetaDataEntry(MD_NAME, metaData.getName(), cx);
                saveMetaDataEntry(MD_VERSION, metaData.getVersion(), cx);
                saveMetaDataEntry(MD_DESCRIPTION, metaData.getDescription(), cx);
                saveMetaDataEntry(MD_ATTRIBUTION, metaData.getAttribution(), cx);
                saveMetaDataEntry(MD_TYPE, metaData.getTypeStr(), cx);
                saveMetaDataEntry(MD_FORMAT, metaData.getFormatStr(), cx);
                saveMetaDataEntry(MD_BOUNDS, metaData.getBoundsStr(), cx);
                saveMetaDataEntry(MD_MINZOOM, String.valueOf(metaData.getMinZoom()), cx);
                saveMetaDataEntry(MD_MAXZOOM, String.valueOf(metaData.getMaxZoom()), cx);
                saveMetaDataEntry(MD_JSON, String.valueOf(metaData.getJson()), cx);
            } finally {
                cx.close();
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Save the minimum and maximum zoom level as metadata items. GDAL and QGIS expect these items.
     *
     * @param min The minimum zoom level
     * @param max The maximum zoom level
     */
    public void saveMinMaxZoomMetadata(int min, int max) throws IOException {
        try {
            Connection cx = connPool.getConnection();
            try {
                saveMetaDataEntry(MD_MINZOOM, String.valueOf(min), cx);
                saveMetaDataEntry(MD_MAXZOOM, String.valueOf(max), cx);
            } finally {
                cx.close();
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /** Store a tile */
    public void saveTile(MBTilesTile entry) throws IOException {
        try {
            Connection cx = connPool.getConnection();
            try {

                if (disableJournal) {
                    disableJournal(cx);
                }

                if (entry.getData() != null) {
                    try (PreparedStatement ps =
                            prepare(
                                            cx,
                                            format(
                                                    "INSERT OR REPLACE INTO %s VALUES (?,?,?,?)",
                                                    TABLE_TILES))
                                    .set(entry.getZoomLevel())
                                    .set(entry.getTileColumn())
                                    .set(entry.getTileRow())
                                    .set(entry.getData())
                                    .log(Level.FINE)
                                    .statement()) {
                        ps.execute();
                    }
                } else {
                    try (PreparedStatement ps =
                            prepare(
                                            cx,
                                            format(
                                                    "DELETE FROM %s WHERE zoom_level=? AND tile_column=? AND tile_row=?",
                                                    TABLE_TILES))
                                    .set(entry.getZoomLevel())
                                    .set(entry.getTileColumn())
                                    .set(entry.getTileRow())
                                    .log(Level.FINE)
                                    .statement()) {
                        ps.execute();
                    }
                }

                saveMinMaxZoomMetadata(
                        (int) Math.min(entry.getZoomLevel(), this.minZoom()),
                        (int) Math.max(entry.getZoomLevel(), this.maxZoom()));

            } finally {
                cx.close();
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /** Store a grid */
    public void saveGrid(MBTilesGrid entry) throws IOException {
        try (Connection cx = connPool.getConnection()) {
            if (entry.getGrid() != null) {
                try (PreparedStatement ps =
                        prepare(
                                        cx,
                                        format(
                                                "INSERT OR REPLACE INTO %s VALUES (?,?,?,?)",
                                                TABLE_GRIDS))
                                .set(entry.getZoomLevel())
                                .set(entry.getTileColumn())
                                .set(entry.getTileRow())
                                .set(entry.getGrid())
                                .log(Level.FINE)
                                .statement()) {
                    ps.execute();
                }
            } else {
                try (PreparedStatement ps =
                        prepare(
                                        cx,
                                        format(
                                                "DELETE FROM %s WHERE zoom_level=? AND tile_column=? AND tile_row=?",
                                                TABLE_GRIDS))
                                .set(entry.getZoomLevel())
                                .set(entry.getTileColumn())
                                .set(entry.getTileRow())
                                .log(Level.FINE)
                                .statement()) {
                    ps.execute();
                }
            }

            for (Map.Entry<String, String> gridDataEntry : entry.getGridData().entrySet()) {
                if (gridDataEntry.getValue() != null) {
                    try (PreparedStatement ps =
                            prepare(
                                            cx,
                                            format(
                                                    "INSERT OR REPLACE INTO %s VALUES (?,?,?,?,?)",
                                                    TABLE_GRID_DATA))
                                    .set(entry.getZoomLevel())
                                    .set(entry.getTileColumn())
                                    .set(entry.getTileRow())
                                    .set(gridDataEntry.getKey())
                                    .set(gridDataEntry.getValue())
                                    .log(Level.FINE)
                                    .statement()) {
                        ps.execute();
                    }
                } else {
                    try (PreparedStatement ps =
                            prepare(
                                            cx,
                                            format(
                                                    "DELETE FROM %s WHERE zoom_level=? AND tile_column=? AND tile_row=? AND key_name=?",
                                                    TABLE_GRID_DATA))
                                    .set(entry.getZoomLevel())
                                    .set(entry.getTileColumn())
                                    .set(entry.getTileRow())
                                    .set(gridDataEntry.getKey())
                                    .log(Level.FINE)
                                    .statement()) {
                        ps.execute();
                    }
                }
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    public MBTilesMetadata loadMetaData() throws IOException {
        return loadMetaData(new MBTilesMetadata());
    }

    public MBTilesMetadata loadMetaData(MBTilesMetadata metaData) throws IOException {
        try {
            Connection cx = connPool.getConnection();
            try {
                metaData.setName(loadMetaDataEntry(MD_NAME, cx));
                metaData.setVersion(loadMetaDataEntry(MD_VERSION, cx));
                metaData.setDescription(loadMetaDataEntry(MD_DESCRIPTION, cx));
                metaData.setAttribution(loadMetaDataEntry(MD_ATTRIBUTION, cx));
                metaData.setTypeStr(loadMetaDataEntry(MD_TYPE, cx));
                metaData.setFormatStr(loadMetaDataEntry(MD_FORMAT, cx));
                metaData.setBoundsStr(loadMetaDataEntry(MD_BOUNDS, cx));
                metaData.setMinZoomStr(loadMetaDataEntry(MD_MINZOOM, cx));
                metaData.setMaxZoomStr(loadMetaDataEntry(MD_MAXZOOM, cx));
                metaData.setJson(loadMetaDataEntry(MD_JSON, cx));
            } finally {
                cx.close();
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }
        return metaData;
    }

    public MBTilesTile loadTile(long zoomLevel, long column, long row) throws IOException {
        return loadTile(new MBTilesTile(zoomLevel, column, row));
    }

    public MBTilesTile loadTile(MBTilesTile entry) throws IOException {
        try {
            try (Connection cx = connPool.getConnection();
                    PreparedStatement ps =
                            prepare(
                                            cx,
                                            format(
                                                    "SELECT tile_data FROM %s WHERE zoom_level=? AND tile_column=? AND tile_row=?",
                                                    TABLE_TILES))
                                    .set(entry.getZoomLevel())
                                    .set(entry.getTileColumn())
                                    .set(entry.getTileRow())
                                    .log(Level.FINE)
                                    .statement();
                    ResultSet rs = ps.executeQuery(); ) {
                if (rs.next()) {
                    entry.setData(rs.getBytes(1));
                } else {
                    entry.setData(null);
                }
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }

        return entry;
    }

    public MBTilesGrid loadGrid(long zoomLevel, long column, long row) throws IOException {
        return loadGrid(new MBTilesGrid(zoomLevel, column, row));
    }

    public MBTilesGrid loadGrid(MBTilesGrid entry) throws IOException {
        try {

            try (Connection cx = connPool.getConnection()) {
                try (PreparedStatement ps =
                                prepare(
                                                cx,
                                                format(
                                                        "SELECT grid FROM %s WHERE zoom_level=? AND tile_column=? AND tile_row=?",
                                                        TABLE_GRIDS))
                                        .set(entry.getZoomLevel())
                                        .set(entry.getTileColumn())
                                        .set(entry.getTileRow())
                                        .log(Level.FINE)
                                        .statement();
                        ResultSet rs = ps.executeQuery(); ) {

                    if (rs.next()) {
                        entry.setGrid(rs.getBytes(1));
                    } else {
                        entry.setGrid(null);
                    }
                }

                try (PreparedStatement ps =
                                prepare(
                                                cx,
                                                format(
                                                        "SELECT key_name, key_json FROM %s WHERE zoom_level=? AND tile_column=? AND tile_row=?",
                                                        TABLE_GRID_DATA))
                                        .set(entry.getZoomLevel())
                                        .set(entry.getTileColumn())
                                        .set(entry.getTileRow())
                                        .log(Level.FINE)
                                        .statement();
                        ResultSet rs = ps.executeQuery(); ) {

                    while (rs.next()) {
                        entry.setGridDataKey(rs.getString(1), rs.getString(2));
                    }
                }
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }

        return entry;
    }

    @SuppressWarnings("PMD.CloseResource")
    public TileIterator tiles() throws SQLException {
        Connection cx = null;
        Statement st = null;
        try {
            cx = connPool.getConnection();
            st = cx.createStatement();
            return new TileIterator(st.executeQuery("SELECT * FROM " + TABLE_TILES + ";"), st, cx);
        } catch (SQLException e) {
            close(st);
            close(cx);
            throw e;
        }
    }

    private void close(Statement st) {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Failed to close statement", e);
        }
    }

    private void close(Connection cx) {
        try {
            if (cx != null) {
                cx.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Failed to close connection", e);
        }
    }

    @SuppressWarnings("PMD.CloseResource")
    public TileIterator tiles(long zoomLevel) throws SQLException {
        Connection cx = null;
        PreparedStatement ps = null;
        try {
            cx = connPool.getConnection();
            ps =
                    prepare(cx, format("SELECT * FROM %s WHERE zoom_level=?", TABLE_TILES))
                            .set(zoomLevel)
                            .statement();
            return new TileIterator(ps.executeQuery(), ps, cx);
        } catch (Exception e) {
            close(ps);
            close(cx);
            throw e;
        }
    }

    @SuppressWarnings("PMD.CloseResource")
    public TileIterator tiles(
            long zoomLevel, long leftTile, long bottomTile, long rightTile, long topTile)
            throws SQLException {
        Connection cx = null;
        PreparedStatement ps = null;

        try {
            cx = connPool.getConnection();
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(
                        "Reading tiles at zoom level "
                                + zoomLevel
                                + ", col range "
                                + leftTile
                                + "/"
                                + rightTile
                                + ", row range "
                                + bottomTile
                                + "/"
                                + topTile);
            }

            ps =
                    prepare(
                                    cx,
                                    format(
                                            "SELECT * FROM %s WHERE zoom_level=? AND tile_column >= ? AND tile_row >= ? AND tile_column <= ? AND tile_row <= ?",
                                            TABLE_TILES))
                            .set(zoomLevel)
                            .set(leftTile)
                            .set(bottomTile)
                            .set(rightTile)
                            .set(topTile)
                            .statement();
            return new TileIterator(ps.executeQuery(), ps, cx);
        } catch (Exception e) {
            close(cx);
            close(ps);

            throw e;
        }
    }

    public int numberOfTiles() throws SQLException {
        int size;

        try (Connection cx = connPool.getConnection();
                Statement st = cx.createStatement();
                ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + TABLE_TILES + ";")) {
            if (!rs.next()) {
                throw new SQLException("Tiles count did not return any row");
            }
            size = rs.getInt(1);
        }
        return size;
    }

    public int numberOfTiles(long zoomLevel) throws SQLException {
        int size;
        try (Connection cx = connPool.getConnection();
                PreparedStatement ps =
                        prepare(
                                        cx,
                                        format(
                                                "SELECT COUNT(*) FROM %s WHERE zoom_level=?",
                                                TABLE_TILES))
                                .set(zoomLevel)
                                .statement();
                ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) {
                throw new SQLException("Zoom level count did not return any row");
            }
            size = rs.getInt(1);
        }
        return size;
    }

    public long closestZoom(long zoomLevel) throws SQLException {
        long zoom = 0;
        try (Connection cx = connPool.getConnection();
                PreparedStatement ps =
                        prepare(
                                        cx,
                                        format(
                                                "SELECT zoom_level FROM %s ORDER BY abs(zoom_level - ?)",
                                                TABLE_TILES))
                                .set(zoomLevel)
                                .statement();
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                zoom = rs.getLong(1);
            }
        }
        return zoom;
    }

    public long minZoom() throws SQLException {
        long zoom = 0;

        try (Connection cx = connPool.getConnection();
                Statement st = cx.createStatement();
                ResultSet rs = st.executeQuery("SELECT MIN(zoom_level) FROM " + TABLE_TILES)) {
            if (rs.next()) {
                zoom = rs.getLong(1);
            }
        }
        return zoom;
    }

    public long maxZoom() throws SQLException {
        long zoom = 0;
        try (Connection cx = connPool.getConnection();
                Statement st = cx.createStatement();
                ResultSet rs = st.executeQuery("SELECT MAX(zoom_level) FROM " + TABLE_TILES)) {
            if (rs.next()) {
                zoom = rs.getLong(1);
            }
        }
        return zoom;
    }

    public long minColumn(long zoomLevel) throws SQLException {
        long size = 0;
        try (Connection cx = connPool.getConnection();
                PreparedStatement ps =
                        prepare(
                                        cx,
                                        format(
                                                "SELECT MIN(tile_column) FROM %s WHERE zoom_level=?",
                                                TABLE_TILES))
                                .set(zoomLevel)
                                .statement();
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                size = rs.getLong(1);
            }
        }
        return size;
    }

    public long maxColumn(long zoomLevel) throws SQLException {
        long size = Long.MAX_VALUE;

        try (Connection cx = connPool.getConnection();
                PreparedStatement ps =
                        prepare(
                                        cx,
                                        format(
                                                "SELECT MAX(tile_column) FROM %s WHERE zoom_level=?",
                                                TABLE_TILES))
                                .set(zoomLevel)
                                .statement();
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                size = rs.getLong(1);
            }
        }
        return size;
    }

    public long minRow(long zoomLevel) throws SQLException {
        long size = 0;

        try (Connection cx = connPool.getConnection();
                PreparedStatement ps =
                        prepare(
                                        cx,
                                        format(
                                                "SELECT MIN(tile_row) FROM %s WHERE zoom_level=?",
                                                TABLE_TILES))
                                .set(zoomLevel)
                                .statement();
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                size = rs.getLong(1);
            }
        }
        return size;
    }

    public long maxRow(long zoomLevel) throws SQLException {
        long size = Long.MAX_VALUE;
        try (Connection cx = connPool.getConnection();
                PreparedStatement ps =
                        prepare(
                                        cx,
                                        format(
                                                "SELECT MAX(tile_row) FROM %s WHERE zoom_level=?",
                                                TABLE_TILES))
                                .set(zoomLevel)
                                .statement();
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                size = rs.getLong(1);
            }
        }
        return size;
    }

    /**
     * Closes the mbtiles database connection.
     *
     * <p>The application should always call this method when done with a mbtiles to prevent
     * connection leakage.
     */
    public void close() {
        try {
            if (connPool instanceof BasicDataSource) {
                ((BasicDataSource) connPool).close();
            } else if (connPool instanceof ManageableDataSource) {
                ((ManageableDataSource) connPool).close();
            }

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error closing database connection", e);
        }
    }

    /**
     * The underlying database file.
     *
     * <p>Note: this value may be <code>null</code> depending on how the geopackage was initialized.
     */
    public File getFile() {
        return file;
    }

    protected void saveMetaDataEntry(String name, String value, Connection cx) throws SQLException {
        if (disableJournal) {
            disableJournal(cx);
        }

        if (value != null) {
            try (PreparedStatement ps =
                    prepare(cx, format("INSERT OR REPLACE INTO %s VALUES (?,?)", TABLE_METADATA))
                            .set(name)
                            .set(value)
                            .log(Level.FINE)
                            .statement()) {
                ps.execute();
            }
        } else {
            try (PreparedStatement ps =
                    prepare(cx, format("DELETE FROM %s WHERE NAME = ?", TABLE_METADATA))
                            .set(name)
                            .log(Level.FINE)
                            .statement()) {
                ps.execute();
            }
        }
    }

    protected String loadMetaDataEntry(String name, Connection cx) throws SQLException {
        try (PreparedStatement ps =
                        prepare(cx, format("SELECT VALUE FROM %s WHERE NAME = ?", TABLE_METADATA))
                                .set(name)
                                .log(Level.FINE)
                                .statement();
                ResultSet rs = ps.executeQuery()) {

            String result = null;
            if (rs.next()) {
                result = rs.getString(1);
            }

            return result;
        }
    }

    /**
     * Initializes the mbtiles database.
     *
     * <p>This method creates all the necessary tables.
     */
    public void init() throws IOException {
        try {
            Connection cx = connPool.getConnection();
            try {
                init(cx);
            } finally {
                cx.close();
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Initializes a mbtiles connection.
     *
     * <p>This method creates all the necessary tables.
     */
    protected void init(Connection cx) throws SQLException {
        runScript("mbtiles.sql", cx);
    }

    // sql utility methods

    protected void runScript(String filename, Connection cx) throws SQLException {
        SqlUtil.runScript(getClass().getResourceAsStream(filename), cx);
    }

    private void disableJournal(Connection cx) throws SQLException {
        PreparedStatement prepared = prepare(cx, PRAGMA_JOURNAL_MODE_OFF).statement();
        try {
            prepared.execute();
        } catch (Exception e) {
            throw new SQLException(e);
        } finally {
            if (prepared != null) {
                prepared.close();
            }
        }
    }

    /**
     * Converts the envelope into a tiles rectangle containing it, at the requested zoom level. X
     * tiles start from west and increase towards east, Y tiles start from north and increase
     * towards south
     */
    protected RectangleLong toTilesRectangle(Envelope envelope, long zoomLevel)
            throws SQLException {
        // From the specification:
        // ---------------------------------------------------------------------------------
        // The tiles table contains tiles and the values used to locate them. The zoom_level,
        // tile_column, and tile_row columns MUST encode the location of the tile, following the
        // Tile Map Service Specification, with the restriction that the global-mercator (aka
        // Spherical Mercator) profile MUST be used.
        // ---------------------------------------------------------------------------------
        // Hence, tile wise the Y axis starts at the bottom and grows north-ward

        long numberOfTiles =
                tilesForZoom(zoomLevel); // number of tile columns/rows for chosen zoom level
        double resX = WORLD_ENVELOPE.getSpan(0) / numberOfTiles; // points per tile
        double resY = WORLD_ENVELOPE.getSpan(1) / numberOfTiles; // points per tile
        double offsetX = WORLD_ENVELOPE.getMinimum(0);
        double offsetY = WORLD_ENVELOPE.getMinimum(1);

        long minTileX = Math.round(Math.floor((envelope.getMinX() - offsetX) / resX));
        long maxTileX = Math.round(Math.floor((envelope.getMaxX() - offsetX) / resX));
        long minTileY = Math.round(Math.floor((envelope.getMinY() - offsetY) / resY));
        long maxTileY = Math.round(Math.floor((envelope.getMaxY() - offsetY) / resY));

        return new RectangleLong(minTileX, maxTileX, minTileY, maxTileY);
    }

    protected static long tilesForZoom(long zoomLevel) {
        return Math.round(Math.pow(2, zoomLevel));
    }

    /** Returns the actual tile bounds for the given zoom level, */
    protected RectangleLong getTileBounds(long zoomLevel, boolean exact) throws SQLException {
        if (exact) {
            long minRow = minRow(zoomLevel);
            long maxRow = maxRow(zoomLevel);
            long minCol = minColumn(zoomLevel);
            long maxCol = maxColumn(zoomLevel);
            return new RectangleLong(minCol, maxCol, minRow, maxRow);
        } else {
            long tiles = tilesForZoom(zoomLevel);
            return new RectangleLong(0, tiles - 1, 0, tiles - 1);
        }
    }

    /** Returns the zoom level for the given simplification distance */
    protected long getZoomLevel(double distance) throws SQLException {
        final long maxZoom = maxZoom();
        final long numberOfTiles = tilesForZoom(maxZoom);
        final double span = WORLD_ENVELOPE.getSpan(0) / numberOfTiles;
        double pxSize = span / 256; // assuming a visualization of 256px per tile
        for (long z = maxZoom; z > 0; z--, pxSize *= 2) {
            if (pxSize > distance) {
                // pick the lowest, vector tiles are made to be overzoomed a bit
                // and we need a large gutter to avoid rendering artifacts
                return z;
            }
        }

        return 0;
    }

    /**
     * Converts the tile locations into a real world one
     *
     * @param rect The tile rectangle, in tile space
     * @param zoom The zoom level
     */
    protected ReferencedEnvelope toEnvelope(RectangleLong rect, long zoom) {
        final long numberOfTiles = tilesForZoom(zoom);
        final double spanX = WORLD_ENVELOPE.getSpan(0) / numberOfTiles;
        final double spanY = WORLD_ENVELOPE.getSpan(1) / numberOfTiles;
        final double minX = rect.getMinX() * spanX + WORLD_ENVELOPE.getMinX();
        final double maxX = rect.getMaxX() * spanX + WORLD_ENVELOPE.getMinX();
        final double minY = rect.getMinX() * spanY + WORLD_ENVELOPE.getMinY();
        final double maxY = rect.getMaxX() * spanY + WORLD_ENVELOPE.getMinY();
        return new ReferencedEnvelope(minX, maxX, minY, maxY, SPHERICAL_MERCATOR);
    }

    protected static ReferencedEnvelope toEnvelope(MBTilesTileLocation tile) {
        final long numberOfTiles = tilesForZoom(tile.getZoomLevel());
        final double spanX = WORLD_ENVELOPE.getSpan(0) / numberOfTiles;
        final double spanY = WORLD_ENVELOPE.getSpan(1) / numberOfTiles;
        final double minX = tile.getTileColumn() * spanX + WORLD_ENVELOPE.getMinX();
        final double maxX = minX + spanX;
        final double minY = tile.getTileRow() * spanY + WORLD_ENVELOPE.getMinY();
        final double maxY = minY + spanY;
        return new ReferencedEnvelope(minX, maxX, minY, maxY, SPHERICAL_MERCATOR);
    }
}
