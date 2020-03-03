package org.geotools.arcsde.data;

import com.esri.sde.sdk.client.SeColumnDefinition;
import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeCoordinateReference;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeExtent;
import com.esri.sde.sdk.client.SeInsert;
import com.esri.sde.sdk.client.SeLayer;
import com.esri.sde.sdk.client.SeRegistration;
import com.esri.sde.sdk.client.SeRow;
import com.esri.sde.sdk.client.SeShape;
import com.esri.sde.sdk.client.SeTable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import org.geotools.arcsde.session.Command;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.session.ISessionPool;
import org.geotools.arcsde.session.ISessionPoolFactory;
import org.geotools.arcsde.session.SessionPoolFactory;
import org.geotools.arcsde.session.UnavailableConnectionException;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;

public class ClobTestData {
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(TestData.class);

    public static SeColumnDefinition[] TEST_TABLE_COLS;

    /*
     * The first column definition must be an SDE managed row id.
     */
    @SuppressWarnings("deprecation")
    public static SeColumnDefinition[] getTestTableCols() throws SeException {
        if (TEST_TABLE_COLS == null) {
            TEST_TABLE_COLS =
                    new SeColumnDefinition[] {
                        new SeColumnDefinition(
                                "ROW_ID", SeColumnDefinition.TYPE_INTEGER, 10, 0, false),
                        new SeColumnDefinition(
                                "CLOB_COL", SeColumnDefinition.TYPE_CLOB, 1000, 0, true),
                    };
        }
        return TEST_TABLE_COLS;
    }

    private SeColumnDefinition[] tempTableColumns;

    /** the set of test parameters loaded from {@code test-data/testparams.properties} */
    private Map<String, Serializable> conProps = null;

    /** the name of a table that can be manipulated without risk of loosing important data */
    private String temp_table;

    /** the configuration keyword to use when creating layers and tables */
    private String configKeyword;

    private ISessionPool _pool;

    /** Creates a new TestData object. */
    public ClobTestData() {
        // intentionally blank
    }

    /**
     * Must be called from inside the test's setUp() method. Loads the test fixture from <code>
     * testparams.properties</code>, besides that, does not creates any connection nor any other
     * costly resource.
     *
     * @throws IOException if the test fixture can't be loaded
     * @throws IllegalArgumentException if some required parameter is not found on the test fixture
     */
    public void setUp() throws IOException {
        if (ArcSDEDataStoreFactory.getSdeClientVersion()
                == ArcSDEDataStoreFactory.JSDE_VERSION_DUMMY) {
            throw new RuntimeException(
                    "Don't run the test-suite with the dummy jar.  "
                            + "Make sure the real ArcSDE jars are on your classpath.");
        }

        Properties props = new Properties();

        String propsFile = "testparams.properties";
        InputStream in = org.geotools.test.TestData.openStream(null, propsFile);

        // The line above should never returns null. It should thow a
        // FileNotFoundException instead if the resource is not available.

        props.load(in);
        in.close();

        this.temp_table = props.getProperty("temp_table");
        this.configKeyword = props.getProperty("configKeyword");
        if (this.configKeyword == null) {
            this.configKeyword = "DEFAULTS";
        }

        if (this.temp_table == null) {
            throw new IllegalArgumentException("temp_table not defined in " + propsFile);
        }
        this.conProps = new HashMap<String, Serializable>();
        for (Map.Entry<Object, Object> e : props.entrySet()) {
            this.conProps.put(String.valueOf(e.getKey()), (Serializable) e.getValue());
        }
    }

    /** Must be called from inside the test's tearDown() method. */
    public void tearDown(boolean cleanTestTable, boolean cleanPool) {
        if (cleanTestTable) {
            deleteTempTable();
        }
    }

    public SeTable getTempTable(ISession session)
            throws IOException, UnavailableConnectionException {
        final String tempTableName = getTempTableName();
        return session.getTable(tempTableName);
    }

    public SeLayer getTempLayer(ISession session)
            throws IOException, UnavailableConnectionException {
        final String tempTableName = getTempTableName();
        return session.getLayer(tempTableName);
    }

    /**
     * creates an ArcSDEDataStore using {@code test-data/testparams.properties} as holder of
     * datastore parameters
     */
    public ArcSDEDataStore getDataStore() throws IOException {
        ISessionPool pool = getConnectionPool();
        ArcSDEDataStore dataStore = new ArcSDEDataStore(pool);

        return dataStore;
    }

    public ISessionPool getConnectionPool() throws IOException {
        if (this._pool == null) {
            ISessionPoolFactory pfac = SessionPoolFactory.getInstance();
            ArcSDEDataStoreConfig config = new ArcSDEDataStoreConfig(this.conProps);
            this._pool = pfac.createPool(config.getSessionConfig());
        }
        return this._pool;
    }

    public String getTempTableName() throws IOException, UnavailableConnectionException {
        ISession session = getConnectionPool().getSession();
        String tempTableName;
        try {
            tempTableName = getTempTableName(session);
        } finally {
            session.dispose();
        }
        return tempTableName;
    }

    /**
     * *Stolen as is from TestData*
     *
     * @return Returns the temp_table.
     */
    public String getTempTableName(ISession session) throws IOException {
        String dbName = session.getDatabaseName();
        String user = session.getUser();
        StringBuffer sb = new StringBuffer();
        if (dbName != null && dbName.length() > 0) {
            sb.append(dbName).append(".");
        }
        if (user != null && user.length() > 0) {
            sb.append(user).append(".");
        }
        sb.append(this.temp_table);
        return sb.toString().toUpperCase();
    }

    public String getConfigKeyword() {
        return this.configKeyword;
    }

    /** Gracefully deletes the temp table hiding any exception (no problem if it does not exist) */
    public void deleteTempTable() {
        // only if the datastore was used
        if (this._pool != null) {
            try {
                _pool = getConnectionPool();
                deleteTempTable(_pool);
            } catch (Exception e) {
                LOGGER.fine(e.getMessage());
            }
        }
    }

    public void deleteTable(final String typeName)
            throws IOException, UnavailableConnectionException {
        ISessionPool connectionPool = getConnectionPool();
        deleteTable(connectionPool, typeName);
    }

    /**
     * Gracefully deletes the temp table hiding any exception (no problem if it does not exist)
     * *Stolen as is from TestData*
     *
     * @param connPool to get the connection to use in deleting {@link #getTempTableName()}
     */
    public void deleteTempTable(ISessionPool connPool) {
        try {
            deleteTable(connPool, getTempTableName());
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
    }

    private static void deleteTable(final ISessionPool connPool, final String tableName)
            throws IOException, UnavailableConnectionException {

        final ISession session = connPool.getSession();

        // final SeTable layer = session.createSeTable(tableName);

        final Command<Void> deleteCmd =
                new Command<Void>() {

                    @Override
                    public Void execute(ISession session, SeConnection connection)
                            throws SeException, IOException {
                        // try {
                        // layer.delete();
                        // } catch (NoSuchElementException e) {
                        // // nothing to do
                        // } catch (SeException e) {
                        // // LOGGER.log(Level.WARNING, "while deleteing layer " +
                        // tableName + " got '" +
                        // // e.getSeError().getErrDesc() + "'");
                        // }
                        SeTable table = new SeTable(connection, tableName);
                        try {
                            table.delete();
                        } catch (SeException ignorable) {
                            // table did not already exist
                        }
                        return null;
                    }
                };

        session.issue(deleteCmd);
        session.dispose();
    }

    /**
     * Creates an ArcSDE feature type names as <code>getTemp_table()</code> on the underlying
     * database and if <code>insertTestData == true</code> also inserts some sample values. *Stolen
     * as is from TestData*
     *
     * @param insertTestData wether to insert some sample rows or not
     * @throws Exception for any error
     */
    public void createTempTable(final boolean insertTestData) throws Exception {
        ISessionPool connPool = getConnectionPool();

        deleteTempTable(connPool);

        ISession session = connPool.getSession();

        try {
            /*
             * Create a qualified table name with current user's name and the name of the table to
             * be created, "EXAMPLE".
             */
            final String tableName = getTempTableName(session);

            final SeTable tempTable = session.createSeTable(tableName);
            final SeLayer tempTableLayer =
                    session.issue(
                            new Command<SeLayer>() {
                                @Override
                                public SeLayer execute(ISession session, SeConnection connection)
                                        throws SeException, IOException {
                                    SeLayer tempTableLayer = new SeLayer(connection);
                                    tempTableLayer.setTableName(tableName);
                                    return tempTableLayer;
                                }
                            });

            tempTableColumns = createBaseTable(session, tempTable, tempTableLayer, configKeyword);

            if (insertTestData) {
                insertData(tempTableLayer, session, tempTableColumns);
            }
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            throw e;
        } finally {
            session.dispose();
        }
    }

    /**
     * Truncates the temp layer and populates it with fresh data. This method cannot be called if
     * {@link #createTempTable(boolean)} has not been called first, no matter if the table already
     * exists, it needs instance state initialized by createTempTable *Stolen as is from TestData*
     */
    public void insertTestData() throws Exception {
        truncateTempTable();
        ISessionPool connPool = getConnectionPool();
        ISession session = connPool.getSession();
        try {
            SeLayer tempTableLayer = getTempLayer(session);
            insertData(tempTableLayer, session, tempTableColumns);
        } finally {
            session.dispose();
        }
    }

    public void truncateTempTable() throws IOException, UnavailableConnectionException {
        final ISessionPool connPool = getConnectionPool();
        final ISession session = connPool.getSession();
        final String tempTableName = getTempTableName(session);

        try {
            session.issue(
                    new Command<Void>() {
                        @Override
                        public Void execute(ISession session, SeConnection connection)
                                throws SeException, IOException {
                            SeTable table;
                            try {
                                table = session.getTable(tempTableName);
                            } catch (IOException e) {
                                // table does not exist, its ok.
                                return null;
                            }
                            table.truncate();
                            return null;
                        }
                    });
        } finally {
            session.dispose();
        }
    }

    /** */
    private static SeColumnDefinition[] createBaseTable(
            final ISession session,
            final SeTable table,
            final SeLayer layer,
            final String configKeyword)
            throws IOException {

        Command<SeColumnDefinition[]> createTableCmd =
                new Command<SeColumnDefinition[]>() {

                    @Override
                    public SeColumnDefinition[] execute(ISession session, SeConnection connection)
                            throws SeException, IOException {

                        SeColumnDefinition[] colDefs = getTestTableCols();

                        try {
                            table.delete();
                        } catch (Exception e) {
                            // ignore
                        }
                        /*
                         * Create the table using the DBMS default configuration keyword. Valid keywords are
                         * defined in the dbtune table.
                         */
                        table.create(colDefs, configKeyword);

                        /*
                         * Register the column to be used as feature id and managed by sde
                         */
                        SeRegistration reg = new SeRegistration(connection, table.getName());
                        LOGGER.fine(
                                "setting rowIdColumnName to ROW_ID in table " + reg.getTableName());
                        reg.setRowIdColumnName("ROW_ID");
                        final int rowIdColumnType =
                                SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_SDE;
                        reg.setRowIdColumnType(rowIdColumnType);
                        reg.alter();

                        /*
                         * Define the attributes of the spatial column
                         */
                        layer.setSpatialColumnName("SHAPE");

                        /*
                         * Set the type of shapes that can be inserted into the layer. Shape type can be
                         * just one or many. NOTE: Layers that contain more than one shape type can only be
                         * accessed through the C and Java APIs and Arc Explorer Java 3.x. They cannot be
                         * seen from ArcGIS desktop applications.
                         */
                        layer.setShapeTypes(
                                SeLayer.SE_NIL_TYPE_MASK
                                        | SeLayer.SE_POINT_TYPE_MASK
                                        | SeLayer.SE_LINE_TYPE_MASK
                                        | SeLayer.SE_SIMPLE_LINE_TYPE_MASK
                                        | SeLayer.SE_AREA_TYPE_MASK
                                        | SeLayer.SE_MULTIPART_TYPE_MASK);
                        layer.setGridSizes(1100.0, 0.0, 0.0);
                        layer.setDescription("Layer Example");

                        /*
                         * Define the layer's Coordinate Reference
                         */
                        SeCoordinateReference coordref = getGenericCoordRef();

                        // SeExtent ext = new SeExtent(-1000000.0, -1000000.0,
                        // 1000000.0,
                        // 1000000.0);
                        SeExtent ext = coordref.getXYEnvelope();
                        layer.setExtent(ext);
                        layer.setCoordRef(coordref);

                        layer.setCreationKeyword(configKeyword);

                        /*
                         * Spatially enable the new table...
                         */
                        layer.create(3, 4);

                        return colDefs;
                    }
                };
        SeColumnDefinition[] colDefs = session.issue(createTableCmd);
        return colDefs;
    }

    /** Inserts two data rows, creating weak geometries and short clobs. */
    private void insertData(
            final SeLayer layer, final ISession session, final SeColumnDefinition[] colDefs)
            throws Exception {
        WKTReader reader = new WKTReader();
        Geometry[] geoms = new Geometry[2];
        geoms[0] = reader.read("POINT(0 0)");
        geoms[1] = reader.read("POINT(0 0)");

        final byte[][] strings = new byte[2][];
        strings[0] = new byte[] {0x00, 0x48, 0x00, 0x65, 0x00, 0x6C, 0x00, 0x6C, 0x00, 0x6F};
        strings[1] = new byte[] {0x00, 0x57, 0x00, 0x6F, 0x00, 0x72, 0x00, 0x6C, 0x00, 0x64};

        final SeCoordinateReference coordref = layer.getCoordRef();
        final SeShape shapes[] = new SeShape[2];
        for (int i = 0; i < shapes.length; i++) {
            Geometry geom = geoms[i];
            SeShape shape;
            if (geom == null) {
                shape = null;
            } else {
                ArcSDEGeometryBuilder builder = ArcSDEGeometryBuilder.builderFor(geom.getClass());
                shape = builder.constructShape(geom, coordref);
            }
            shapes[i] = shape;
        }
        /*
         * Define the names of the columns that data is to be inserted into.
         */
        final String[] columns = new String[colDefs.length];

        // Column one will be the row_id
        for (int j = 1; j < colDefs.length; j++) {
            columns[j - 1] = colDefs[j].getName(); // INT32 column
        }
        columns[colDefs.length - 1] = "SHAPE"; // Shape column

        Command<Void> insertDataCmd =
                new Command<Void>() {
                    @Override
                    public Void execute(ISession session, SeConnection connection)
                            throws SeException, IOException {

                        SeInsert insert = new SeInsert(connection);
                        insert.intoTable(layer.getName(), columns);
                        insert.setWriteMode(true);

                        try {
                            for (int i = 0; i < shapes.length; i++) {
                                SeRow row = insert.getRowToSet();
                                row.setClob(0, new ByteArrayInputStream(strings[i]));

                                SeShape seShape = shapes[i];
                                row.setShape(tempTableColumns.length - 1, seShape);

                                insert.execute();
                            }
                        } finally {
                            insert.close();
                        }
                        return null;
                    }
                };

        session.issue(insertDataCmd);
    } // End method insertData

    /**
     * Creates and returns a <code>SeCoordinateReference</code> CRS, though based on WGS84, is
     * inclusive enough (in terms of valid coordinate range and presicion) to deal with most
     * coordintates.
     *
     * <p>Actually tested to deal with coordinates with 0.0002 units of separation as well as with
     * large coordinates such as UTM (values greater than 500,000.00)
     */
    public static SeCoordinateReference getGenericCoordRef() throws SeException {

        SeCoordinateReference seCRS = new SeCoordinateReference();
        final String wgs84WKT = DefaultGeographicCRS.WGS84.toWKT();
        seCRS.setCoordSysByDescription(wgs84WKT);
        // seCRS.setPrecision(1000);
        seCRS.setXYByEnvelope(new SeExtent(-180, -90, 180, 90));
        return seCRS;
    }
}
