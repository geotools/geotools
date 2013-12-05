package org.geotools.mbtiles;

import static java.lang.String.format;
import static org.geotools.sql.SqlUtil.prepare;

import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.sql.SqlUtil;

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

import org.geotools.data.jdbc.datasource.ManageableDataSource;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.util.logging.Logging;

public class MBTilesFile {

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

    /**
     * Logger
     */
    protected static final Logger LOGGER = Logging.getLogger("org.geotools.mbtiles");

    /**
     * database file
     */
    protected File file;

    /**
     * connection pool
     */
    protected final DataSource connPool;

    /**
     * datastore for vector access, lazily created
     */
    protected volatile JDBCDataStore dataStore;

    /**
     * Creates a new empty MbTilesFile, generating a new file.
     */
    public MBTilesFile() throws IOException {
        this(File.createTempFile("mbtiles", "db"));
    }

    /**
     * Creates a MbTilesFile from an existing file.
     * <p>
     * This constructor assumes no credentials are required to connect to the database.
     * </p>
     */
    public MBTilesFile(File file) throws IOException {
        this(file, null, null);
    }

    /**
     * Creates a MbTilesFile from an existing file specifying database credentials.
     */
    public MBTilesFile(File file, String user, String passwd) throws IOException {
        this.file = file;

        Map params = new HashMap();
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

    MBTilesFile(DataSource dataSource) {
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
                PreparedStatement ps;

                if (entry.getData() != null) {
                    ps = prepare(cx,
                            format("INSERT OR REPLACE INTO %s VALUES (?,?,?,?)", TABLE_TILES))
                            .set(entry.getZoomLevel()).set(entry.getTileColumn())
                            .set(entry.getTileRow()).set(entry.getData()).log(Level.FINE)
                            .statement();
                } else {
                    ps = prepare(
                            cx,
                            format("DELETE FROM %s WHERE zoom_level=? AND tile_column=? AND tile_row=?",
                                    TABLE_TILES)).set(entry.getZoomLevel())
                            .set(entry.getTileColumn()).set(entry.getTileRow()).log(Level.FINE)
                            .statement();
                }
                ps.execute();
                ps.close();
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
                    ps = prepare(cx,
                            format("INSERT OR REPLACE INTO %s VALUES (?,?,?,?)", TABLE_GRIDS))
                            .set(entry.getZoomLevel()).set(entry.getTileColumn())
                            .set(entry.getTileRow()).set(entry.getGrid()).log(Level.FINE)
                            .statement();
                } else {
                    ps = prepare(
                            cx,
                            format("DELETE FROM %s WHERE zoom_level=? AND tile_column=? AND tile_row=?",
                                    TABLE_GRIDS)).set(entry.getZoomLevel())
                            .set(entry.getTileColumn()).set(entry.getTileRow()).log(Level.FINE)
                            .statement();
                }
                ps.execute();
                ps.close();

                for (Map.Entry<String, String> gridDataEntry : entry.getGridData().entrySet()) {
                    if (gridDataEntry.getValue() != null) {
                        ps = prepare(cx,
                                format("INSERT OR REPLACE INTO %s VALUES (?,?,?,?,?)", TABLE_GRID_DATA))
                                .set(entry.getZoomLevel()).set(entry.getTileColumn())
                                .set(entry.getTileRow()).set(gridDataEntry.getKey())
                                .set(gridDataEntry.getValue()).log(Level.FINE).statement();
                    } else {
                        ps = prepare(
                                cx,
                                format("DELETE FROM %s WHERE zoom_level=? AND tile_column=? AND tile_row=? AND key_name=?",
                                        TABLE_GRID_DATA)).set(entry.getZoomLevel())
                                .set(entry.getTileColumn()).set(entry.getTileRow())
                                .set(gridDataEntry.getKey()).log(Level.FINE).statement();
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

                ps = prepare(
                        cx,
                        format("SELECT tile_data FROM %s WHERE zoom_level=? AND tile_column=? AND tile_row=?",
                                TABLE_TILES)).set(entry.getZoomLevel()).set(entry.getTileColumn())
                        .set(entry.getTileRow()).log(Level.FINE).statement();
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

                ps = prepare(
                        cx,
                        format("SELECT grid FROM %s WHERE zoom_level=? AND tile_column=? AND tile_row=?",
                                TABLE_GRIDS)).set(entry.getZoomLevel()).set(entry.getTileColumn())
                        .set(entry.getTileRow()).log(Level.FINE).statement();
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    entry.setGrid(rs.getBytes(1));
                } else {
                    entry.setGrid(null);
                }
                rs.close();
                ps.close();

                ps = prepare(
                        cx,
                        format("SELECT key_name, key_json FROM %s WHERE zoom_level=? AND tile_column=? AND tile_row=?",
                                TABLE_GRID_DATA)).set(entry.getZoomLevel()).set(entry.getTileColumn())
                        .set(entry.getTileRow()).log(Level.FINE).statement();
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
        return new TileIterator(st.executeQuery("SELECT * FROM tiles;"));
    }

    public int numberOfTiles() throws SQLException {
        int size;
        Connection cx = connPool.getConnection();
        try {
            Statement st = cx.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM tiles;");
            rs.next();
            size = rs.getInt(1);
            rs.close();
            st.close();
        } finally {
            cx.close();
        }
        return size;
    }

    /**
     * Closes the mbtiles database connection.
     * <p>
     * The application should always call this method when done with a mbtiles to prevent connection leakage.
     * </p>
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
     * <p>
     * Note: this value may be <code>null</code> depending on how the geopackage was initialized.
     * </p>
     */
    public File getFile() {
        return file;
    }

    protected void saveMetaDataEntry(String name, String value, Connection cx) throws SQLException {
        PreparedStatement ps;

        if (value != null) {
            ps = prepare(cx, format("INSERT OR REPLACE INTO %s VALUES (?,?)", TABLE_METADATA))
                    .set(name).set(value).log(Level.FINE).statement();
        } else {
            ps = prepare(cx, format("DELETE FROM %s WHERE NAME = ?", TABLE_METADATA)).set(name)
                    .log(Level.FINE).statement();
        }

        ps.execute();
        ps.close();
    }

    protected String loadMetaDataEntry(String name, Connection cx) throws SQLException {
        PreparedStatement ps;

        ps = prepare(cx, format("SELECT VALUE FROM %s WHERE NAME = ?", TABLE_METADATA)).set(name)
                .log(Level.FINE).statement();

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
     * <p>
     * This method creates all the necessary tables.
     * </p>
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
     * <p>
     * This method creates all the necessary tables.
     * </p>
     */
    protected void init(Connection cx) throws SQLException {
        runScript("mbtiles.sql", cx);
    }

    // sql utility methods

    protected void runScript(String filename, Connection cx) throws SQLException {
        SqlUtil.runScript(getClass().getResourceAsStream(filename), cx);
    }

}
