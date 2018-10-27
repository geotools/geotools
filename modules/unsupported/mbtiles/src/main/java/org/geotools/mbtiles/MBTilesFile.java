package org.geotools.mbtiles;

import static java.lang.String.format;
import static org.geotools.jdbc.util.SqlUtil.prepare;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
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
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.util.SqlUtil;
import org.geotools.util.logging.Logging;

public class MBTilesFile implements AutoCloseable {

    public static final String PRAGMA_JOURNAL_MODE_OFF = "PRAGMA journal_mode=OFF";

    public class TileIterator implements Iterator<MBTilesTile>, Closeable {

        ResultSet rs;

        Boolean next = null;

        TileIterator(ResultSet rs) {
            this.rs = rs;
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
                Statement st = rs.getStatement();
                Connection conn = st.getConnection();
                rs.close();
                st.close();
                conn.close();
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

    /** Logger */
    protected static final Logger LOGGER = Logging.getLogger(MBTilesFile.class);

    /** database file */
    protected File file;

    /** connection pool */
    protected final DataSource connPool;

    /** datastore for vector access, lazily created */
    protected volatile JDBCDataStore dataStore;

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
        Map<String, Object> params = new HashMap<String, Object>();
        if (user != null) {
            params.put(MBTilesDataStoreFactory.USER.key, user);
        }
        if (passwd != null) {
            params.put(MBTilesDataStoreFactory.PASSWD.key, passwd);
        }

        params.put(MBTilesDataStoreFactory.DATABASE.key, file.getPath());
        params.put(MBTilesDataStoreFactory.DBTYPE.key, MBTilesDataStoreFactory.DBTYPE.sample);

        this.connPool = new MBTilesDataStoreFactory().createDataSource(params);
    }

    /**
     * Create an MBTilesFile from an SQL DataSource connected to an MBTiles file. Behaviour is
     * undefined if the DataSource is any other form of database.
     *
     * @param dataSource
     */
    public MBTilesFile(DataSource dataSource) {
        this.connPool = dataSource;
    }

    MBTilesFile(JDBCDataStore dataStore) {
        this.dataStore = dataStore;
        this.connPool = dataStore.getDataSource();
    }

    /**
     * Store MetaData in file
     *
     * @param metaData
     * @throws IOException
     */
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
     * @throws IOException
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

    /**
     * Store a tile
     *
     * @throws IOException
     */
    public void saveTile(MBTilesTile entry) throws IOException {
        try {
            Connection cx = connPool.getConnection();
            try {

                if (disableJournal) {
                    disableJournal(cx);
                }

                PreparedStatement ps;

                if (entry.getData() != null) {
                    ps =
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
                                    .statement();
                } else {
                    ps =
                            prepare(
                                            cx,
                                            format(
                                                    "DELETE FROM %s WHERE zoom_level=? AND tile_column=? AND tile_row=?",
                                                    TABLE_TILES))
                                    .set(entry.getZoomLevel())
                                    .set(entry.getTileColumn())
                                    .set(entry.getTileRow())
                                    .log(Level.FINE)
                                    .statement();
                }
                ps.execute();
                ps.close();

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

    /**
     * Store a grid
     *
     * @throws IOException
     */
    public void saveGrid(MBTilesGrid entry) throws IOException {
        try {
            Connection cx = connPool.getConnection();
            try {
                PreparedStatement ps;

                if (entry.getGrid() != null) {
                    ps =
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
                                    .statement();
                } else {
                    ps =
                            prepare(
                                            cx,
                                            format(
                                                    "DELETE FROM %s WHERE zoom_level=? AND tile_column=? AND tile_row=?",
                                                    TABLE_GRIDS))
                                    .set(entry.getZoomLevel())
                                    .set(entry.getTileColumn())
                                    .set(entry.getTileRow())
                                    .log(Level.FINE)
                                    .statement();
                }
                ps.execute();
                ps.close();

                for (Map.Entry<String, String> gridDataEntry : entry.getGridData().entrySet()) {
                    if (gridDataEntry.getValue() != null) {
                        ps =
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
                                        .statement();
                    } else {
                        ps =
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
                                        .statement();
                    }
                    ps.execute();
                    ps.close();
                }
            } finally {
                cx.close();
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
            Connection cx = connPool.getConnection();
            try {
                PreparedStatement ps;

                ps =
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
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    entry.setData(rs.getBytes(1));
                } else {
                    entry.setData(null);
                }
                rs.close();
                ps.close();
            } finally {
                cx.close();
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
            Connection cx = connPool.getConnection();
            try {
                PreparedStatement ps;

                ps =
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
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    entry.setGrid(rs.getBytes(1));
                } else {
                    entry.setGrid(null);
                }
                rs.close();
                ps.close();

                ps =
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
                rs = ps.executeQuery();

                while (rs.next()) {
                    entry.setGridDataKey(rs.getString(1), rs.getString(2));
                }
                rs.close();
                ps.close();
            } finally {
                cx.close();
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }

        return entry;
    }

    public TileIterator tiles() throws SQLException {
        Connection cx = connPool.getConnection();
        Statement st = cx.createStatement();
        return new TileIterator(st.executeQuery("SELECT * FROM " + TABLE_TILES + ";"));
    }

    public TileIterator tiles(long zoomLevel) throws SQLException {
        Connection cx = connPool.getConnection();
        PreparedStatement ps =
                prepare(cx, format("SELECT * FROM %s WHERE zoom_level=?", TABLE_TILES))
                        .set(zoomLevel)
                        .statement();
        return new TileIterator(ps.executeQuery());
    }

    public TileIterator tiles(
            long zoomLevel, long leftTile, long bottomTile, long rightTile, long topTile)
            throws SQLException {
        Connection cx = connPool.getConnection();
        PreparedStatement ps =
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
        return new TileIterator(ps.executeQuery());
    }

    public int numberOfTiles() throws SQLException {
        int size;
        Connection cx = connPool.getConnection();
        try {
            Statement st = cx.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + TABLE_TILES + ";");
            rs.next();
            size = rs.getInt(1);
            rs.close();
            st.close();
        } finally {
            cx.close();
        }
        return size;
    }

    public int numberOfTiles(long zoomLevel) throws SQLException {
        int size;
        Connection cx = connPool.getConnection();
        try {
            PreparedStatement ps =
                    prepare(cx, format("SELECT COUNT(*) FROM %s WHERE zoom_level=?", TABLE_TILES))
                            .set(zoomLevel)
                            .statement();
            ResultSet rs = ps.executeQuery();
            rs.next();
            size = rs.getInt(1);
            rs.close();
            ps.close();
        } finally {
            cx.close();
        }
        return size;
    }

    public long closestZoom(long zoomLevel) throws SQLException {
        long zoom = 0;
        Connection cx = connPool.getConnection();
        try {
            PreparedStatement ps =
                    prepare(
                                    cx,
                                    format(
                                            "SELECT zoom_level FROM %s ORDER BY abs(zoom_level - ?)",
                                            TABLE_TILES))
                            .set(zoomLevel)
                            .statement();
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                zoom = rs.getLong(1);
            }
            rs.close();
            ps.close();
        } finally {
            cx.close();
        }
        return zoom;
    }

    public long minZoom() throws SQLException {
        long zoom = 0;
        Connection cx = connPool.getConnection();
        try {
            Statement st = cx.createStatement();
            ResultSet rs = st.executeQuery("SELECT MIN(zoom_level) FROM " + TABLE_TILES);
            if (rs.next()) {
                zoom = rs.getLong(1);
            }
            rs.close();
            st.close();
        } finally {
            cx.close();
        }
        return zoom;
    }

    public long maxZoom() throws SQLException {
        long zoom = 0;
        Connection cx = connPool.getConnection();
        try {
            Statement st = cx.createStatement();
            ResultSet rs = st.executeQuery("SELECT MAX(zoom_level) FROM " + TABLE_TILES);
            if (rs.next()) {
                zoom = rs.getLong(1);
            }
            rs.close();
            st.close();
        } finally {
            cx.close();
        }
        return zoom;
    }

    public long minColumn(long zoomLevel) throws SQLException {
        long size = 0;
        Connection cx = connPool.getConnection();
        try {
            PreparedStatement ps =
                    prepare(
                                    cx,
                                    format(
                                            "SELECT MIN(tile_column) FROM %s WHERE zoom_level=?",
                                            TABLE_TILES))
                            .set(zoomLevel)
                            .statement();
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                size = rs.getLong(1);
            }
            rs.close();
            ps.close();
        } finally {
            cx.close();
        }
        return size;
    }

    public long maxColumn(long zoomLevel) throws SQLException {
        long size = Long.MAX_VALUE;
        Connection cx = connPool.getConnection();
        try {
            PreparedStatement ps =
                    prepare(
                                    cx,
                                    format(
                                            "SELECT MAX(tile_column) FROM %s WHERE zoom_level=?",
                                            TABLE_TILES))
                            .set(zoomLevel)
                            .statement();
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                size = rs.getLong(1);
            }
            rs.close();
            ps.close();
        } finally {
            cx.close();
        }
        return size;
    }

    public long minRow(long zoomLevel) throws SQLException {
        long size = 0;
        Connection cx = connPool.getConnection();
        try {
            PreparedStatement ps =
                    prepare(
                                    cx,
                                    format(
                                            "SELECT MIN(tile_row) FROM %s WHERE zoom_level=?",
                                            TABLE_TILES))
                            .set(zoomLevel)
                            .statement();
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (rs.next()) {
                size = rs.getLong(1);
            }
            rs.close();
            ps.close();
        } finally {
            cx.close();
        }
        return size;
    }

    public long maxRow(long zoomLevel) throws SQLException {
        long size = Long.MAX_VALUE;
        Connection cx = connPool.getConnection();
        try {
            PreparedStatement ps =
                    prepare(
                                    cx,
                                    format(
                                            "SELECT MAX(tile_row) FROM %s WHERE zoom_level=?",
                                            TABLE_TILES))
                            .set(zoomLevel)
                            .statement();
            ResultSet rs = ps.executeQuery();
            rs.next();
            size = rs.getLong(1);
            rs.close();
            ps.close();
        } finally {
            cx.close();
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
        if (dataStore != null) {
            dataStore.dispose();
        }

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
        PreparedStatement ps;

        if (disableJournal) {
            disableJournal(cx);
        }

        if (value != null) {
            ps =
                    prepare(cx, format("INSERT OR REPLACE INTO %s VALUES (?,?)", TABLE_METADATA))
                            .set(name)
                            .set(value)
                            .log(Level.FINE)
                            .statement();
        } else {
            ps =
                    prepare(cx, format("DELETE FROM %s WHERE NAME = ?", TABLE_METADATA))
                            .set(name)
                            .log(Level.FINE)
                            .statement();
        }

        ps.execute();
        ps.close();
    }

    protected String loadMetaDataEntry(String name, Connection cx) throws SQLException {
        PreparedStatement ps;

        ps =
                prepare(cx, format("SELECT VALUE FROM %s WHERE NAME = ?", TABLE_METADATA))
                        .set(name)
                        .log(Level.FINE)
                        .statement();

        ResultSet rs = ps.executeQuery();

        String result = null;
        if (rs.next()) {
            result = rs.getString(1);
        }

        rs.close();
        ps.close();

        return result;
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
}
