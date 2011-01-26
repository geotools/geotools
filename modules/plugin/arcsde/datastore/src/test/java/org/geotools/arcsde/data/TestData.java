/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.arcsde.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.geotools.arcsde.ArcSDEDataStoreFactory;
import org.geotools.arcsde.ArcSdeException;
import org.geotools.arcsde.session.Command;
import org.geotools.arcsde.session.Commands;
import org.geotools.arcsde.session.Commands.GetVersionCommand;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.session.ISessionPool;
import org.geotools.arcsde.session.ISessionPoolFactory;
import org.geotools.arcsde.session.SessionPoolFactory;
import org.geotools.arcsde.session.UnavailableConnectionException;
import org.geotools.data.DataSourceException;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.esri.sde.sdk.client.SeColumnDefinition;
import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeCoordinateReference;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeExtent;
import com.esri.sde.sdk.client.SeInsert;
import com.esri.sde.sdk.client.SeLayer;
import com.esri.sde.sdk.client.SeObjectId;
import com.esri.sde.sdk.client.SeRegistration;
import com.esri.sde.sdk.client.SeRow;
import com.esri.sde.sdk.client.SeShape;
import com.esri.sde.sdk.client.SeState;
import com.esri.sde.sdk.client.SeTable;
import com.esri.sde.sdk.client.SeVersion;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.operation.valid.IsValidOp;
import com.vividsolutions.jts.operation.valid.TopologyValidationError;

/**
 * Provides access to the ArcSDEDataStore test data configuration.
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/test/java
 *         /org/geotools/arcsde/data/TestData.java $
 * @version $Id$
 */
@SuppressWarnings({ "nls", "unchecked" })
public class TestData {

    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(TestData.class
            .getPackage().getName());

    public static final String[] TEST_TABLE_COLS = { "INT32_COL", "INT16_COL", "FLOAT32_COL",
            "FLOAT64_COL", "STRING_COL", "NSTRING_COL", "DATE_COL", "SHAPE" };

    private SeColumnDefinition[] tempTableColumns;

    // private SeLayer tempTableLayer;

    // private SeTable tempTable;

    /**
     * the set of test parameters loaded from {@code test-data/testparams.properties}
     */
    private Map<String, Serializable> conProps = null;

    /**
     * the name of a table that can be manipulated without risk of loosing important data
     */
    private String temp_table;

    /** the configuration keyword to use when creating layers and tables */
    private String configKeyword;

    private ISessionPool _pool;

    /**
     * Creates a new TestData object.
     */
    public TestData() {
        // intentionally blank
    }

    /**
     * Must be called from inside the test's setUp() method. Loads the test fixture from
     * <code>testparams.properties</code>, besides that, does not creates any connection nor any
     * other costly resource.
     * 
     * @throws IOException
     *             if the test fixture can't be loaded
     * @throws IllegalArgumentException
     *             if some required parameter is not found on the test fixture
     */
    public void setUp() throws IOException {
        if (ArcSDEDataStoreFactory.getSdeClientVersion() == ArcSDEDataStoreFactory.JSDE_VERSION_DUMMY) {
            throw new RuntimeException("Don't run the test-suite with the dummy jar.  "
                    + "Make sure the real ArcSDE jars are on your classpath.");
        }

        Properties conProps = new Properties();

        String propsFile = "testparams.properties";
        InputStream in = org.geotools.test.TestData.openStream(null, propsFile);

        // The line above should never returns null. It should thow a
        // FileNotFoundException instead if the resource is not available.

        conProps.load(in);
        in.close();

        this.temp_table = conProps.getProperty("temp_table");
        this.configKeyword = conProps.getProperty("configKeyword");
        if (this.configKeyword == null) {
            this.configKeyword = "DEFAULTS";
        }

        if (this.temp_table == null) {
            throw new IllegalArgumentException("temp_table not defined in " + propsFile);
        }
        this.conProps = new HashMap<String, Serializable>();
        for (Map.Entry<Object, Object> e : conProps.entrySet()) {
            this.conProps.put(String.valueOf(e.getKey()), (Serializable) e.getValue());
        }
    }

    /**
     * Must be called from inside the test's tearDown() method.
     */
    public void tearDown(boolean cleanTestTable, boolean cleanPool) {
        if (cleanTestTable) {
            deleteTempTable();
        }
        if (cleanPool && _pool != null) {
            _pool.close();
            _pool = null;
        }
    }

    public SeTable getTempTable(ISession session) throws IOException {
        final String tempTableName = getTempTableName();
        return session.getTable(tempTableName);
    }

    public SeLayer getTempLayer(ISession session) throws IOException {
        final String tempTableName = getTempTableName();
        return session.getLayer(tempTableName);
    }

    /**
     * creates an ArcSDEDataStore using {@code test-data/testparams.properties} as holder of
     * datastore parameters
     * 
     */
    public ArcSDEDataStore getDataStore() throws IOException {
        ISessionPool pool = newSessionPool();
        ArcSDEDataStore dataStore = new ArcSDEDataStore(pool);

        return dataStore;
    }

    public ISessionPool getConnectionPool() throws IOException {
        if (this._pool == null) {
            this._pool = newSessionPool();
        }
        return this._pool;
    }

    public ISessionPool newSessionPool() throws IOException {
        ISessionPoolFactory pfac = SessionPoolFactory.getInstance();
        ArcSDEDataStoreConfig config = new ArcSDEDataStoreConfig(this.conProps);
        return pfac.createPool(config.getSessionConfig());
    }

    /**
     * 
     * 
     * @return Returns the conProps.
     */
    @SuppressWarnings("rawtypes")
    public Map<String, Serializable> getConProps() {
        return new HashMap<String, Serializable>((Map) this.conProps);
    }

    public String getTempTableName() throws IOException {
        ISession session;
        try {
            session = getConnectionPool().getSession();
        } catch (UnavailableConnectionException e) {
            throw new RuntimeException(e);
        }
        String tempTableName;
        try {
            tempTableName = getTempTableName(session);
        } finally {
            session.dispose();
        }
        return tempTableName;
    }

    /**
     * @return Returns the temp_table.
     * @throws SeException
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

    /**
     * Gracefully deletes the temp table hiding any exception (no problem if it does not exist)
     */
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

    public void deleteTable(final String typeName) throws IOException,
            UnavailableConnectionException {
        deleteTable(typeName, true);
    }

    public void deleteTable(final String typeName, final boolean ignoreFailure) throws IOException,
            UnavailableConnectionException {
        ISessionPool connectionPool = getConnectionPool();
        deleteTable(connectionPool, typeName, ignoreFailure);
    }

    /**
     * Gracefully deletes the temp table hiding any exception (no problem if it does not exist)
     * 
     * @param connPool
     *            to get the connection to use in deleting {@link #getTempTableName()}
     * @throws UnavailableConnectionException
     */
    public void deleteTempTable(ISessionPool connPool) throws IOException,
            UnavailableConnectionException {
        deleteTable(connPool, getTempTableName(), true);
    }

    private static void deleteTable(final ISessionPool connPool, final String tableName,
            final boolean ignoreFailure) throws IOException, UnavailableConnectionException {

        final Command<Void> deleteCmd = new Command<Void>() {

            @Override
            public Void execute(ISession session, SeConnection connection) throws SeException,
                    IOException {
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
                    // table did not already exist? or was locked...
                    if (!ignoreFailure) {
                        throw new ArcSdeException(ignorable);
                    }
                }
                return null;
            }
        };

        final ISession session = connPool.getSession();
        try {
            session.issue(deleteCmd);
        } finally {
            session.dispose();
        }
    }

    /**
     * Creates an ArcSDE feature type names as <code>getTemp_table()</code> on the underlying
     * database and if <code>insertTestData == true</code> also inserts some sample values.
     * 
     * @param insertTestData
     *            wether to insert some sample rows or not
     * @throws Exception
     *             for any error
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
            final SeLayer tempTableLayer = session.issue(new Command<SeLayer>() {
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
            e.printStackTrace();
            throw e;
        } finally {
            session.dispose();
        }
    }

    /**
     * Truncates the temp layer and populates it with fresh data. This method cannot be called if
     * {@link #createTempTable(boolean)} has not been called first, no matter if the table already
     * exists, it needs instance state initialized by createTempTable
     * 
     * @throws Exception
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
        final String tempTableName = getTempTableName();

        truncateTestTable(tempTableName);
    }

    public void truncateTestTable(final String tempTableName) throws IOException,
            DataSourceException, UnavailableConnectionException {
        final ISessionPool connPool = getConnectionPool();
        final ISession session = connPool.getSession();

        try {
            session.issue(new Command<Void>() {
                @Override
                public Void execute(ISession session, SeConnection connection) throws SeException,
                        IOException {
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

    /**
     * 
     * 
     */
    private static SeColumnDefinition[] createBaseTable(final ISession session,
            final SeTable table, final SeLayer layer, final String configKeyword)
            throws IOException {

        Command<SeColumnDefinition[]> createTableCmd = new Command<SeColumnDefinition[]>() {

            @Override
            public SeColumnDefinition[] execute(ISession session, SeConnection connection)
                    throws SeException, IOException {

                SeColumnDefinition[] colDefs = new SeColumnDefinition[9];

                /*
                 * Define the columns and their attributes for the table to be created. NOTE: The
                 * valid range/values of size and scale parameters vary from one database to
                 * another.
                 */
                boolean isNullable = true;

                // first column to be SDE managed feature id
                colDefs[0] = new SeColumnDefinition("ROW_ID", SeColumnDefinition.TYPE_INT32, 10, 0,
                        false);

                colDefs[1] = new SeColumnDefinition(TEST_TABLE_COLS[0],
                        SeColumnDefinition.TYPE_INT32, 10, 0, false);
                colDefs[2] = new SeColumnDefinition(TEST_TABLE_COLS[1],
                        SeColumnDefinition.TYPE_INT16, 4, 0, isNullable);
                colDefs[3] = new SeColumnDefinition(TEST_TABLE_COLS[2],
                        SeColumnDefinition.TYPE_FLOAT32, 5, 2, isNullable);
                colDefs[4] = new SeColumnDefinition(TEST_TABLE_COLS[3],
                        SeColumnDefinition.TYPE_FLOAT64, 25, 4, isNullable);
                colDefs[5] = new SeColumnDefinition(TEST_TABLE_COLS[4],
                        SeColumnDefinition.TYPE_STRING, 25, 0, isNullable);

                colDefs[6] = new SeColumnDefinition(TEST_TABLE_COLS[5],
                        SeColumnDefinition.TYPE_NSTRING, 25, 0, isNullable);

                colDefs[7] = new SeColumnDefinition(TEST_TABLE_COLS[6],
                        SeColumnDefinition.TYPE_DATE, 1, 0, isNullable);
                // this is a blob one and should be ignored to all effects
                colDefs[8] = new SeColumnDefinition("SE_ANNO_CAD_DATA",
                        SeColumnDefinition.TYPE_BLOB, 1000, 0, isNullable);

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
                LOGGER.fine("setting rowIdColumnName to ROW_ID in table " + reg.getTableName());
                reg.setRowIdColumnName("ROW_ID");
                final int rowIdColumnType = SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_SDE;
                reg.setRowIdColumnType(rowIdColumnType);
                reg.alter();

                /*
                 * Define the attributes of the spatial column
                 */
                layer.setSpatialColumnName(TEST_TABLE_COLS[TEST_TABLE_COLS.length - 1]);

                /*
                 * Set the type of shapes that can be inserted into the layer. Shape type can be
                 * just one or many. NOTE: Layers that contain more than one shape type can only be
                 * accessed through the C and Java APIs and Arc Explorer Java 3.x. They cannot be
                 * seen from ArcGIS desktop applications.
                 */
                layer.setShapeTypes(SeLayer.SE_NIL_TYPE_MASK | SeLayer.SE_POINT_TYPE_MASK
                        | SeLayer.SE_LINE_TYPE_MASK | SeLayer.SE_SIMPLE_LINE_TYPE_MASK
                        | SeLayer.SE_AREA_TYPE_MASK | SeLayer.SE_MULTIPART_TYPE_MASK);
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

    /**
     * Inserts 8 rows of data into the layer Columns Inserted:
     * <ul>
     * <li>1. Integer - values: 1 -> 8
     * <li>2. Short - values: 1 -> 8
     * <li>3. Float - values: 0.1 -> 0.8
     * <li>4. Double - values: 0.1 -> 0.8
     * <li>5. String - values: <code>"FEATURE_" + ["1" -> "8"]</code>
     * <li>6. Date - values: July 1 2004 -> July 8 2004
     * <li>7. Shape - values:
     * <ul>
     * <li> <code>POINT(0 0)</code>
     * <li> <code>MULTIPOINT(0 0, 180 0)</code>
     * <li> <code>LINESTRING(0 0, 180 90)</code>
     * <li> <code>MULTILINESTRING((-180 -90, 180 90), (-180 90, 180 -90))</code>
     * <li> <code>POLYGON((-10 -10, -10 10, 10 10, 10 -10, -10 -10))</code>
     * <li>
     * <code>MULTIPOLYGON( ((-1 -1, -1 1, 1 1, 1 -1, -1 -1)), ((-180 -90, -180 -80, -170 -80, -170 -90, -180 -90)) )</code>
     * <li> <code>GEOMETRYCOLLECTION(POINT(1 1), LINESTRING(0 0, 180 90))</code>
     * <li> <code>null</code>
     * </ul>
     * </li>
     * 
     * @throws ParseException
     */
    private void insertData(final SeLayer layer, final ISession session,
            final SeColumnDefinition[] colDefs) throws Exception {
        WKTReader reader = new WKTReader();
        Geometry[] geoms = new Geometry[8];
        geoms[0] = reader.read("POINT(0 0)");
        geoms[1] = reader.read("MULTIPOINT(0 0, 170 0)");
        geoms[2] = reader.read("LINESTRING(0 0, 170 80)");
        geoms[3] = reader.read("MULTILINESTRING((-170 -80, 170 80), (-170 80, 170 -80))");
        geoms[4] = reader.read("POLYGON((-10 -10, -10 10, 10 10, 10 -10, -10 -10))");
        geoms[5] = reader
                .read("MULTIPOLYGON( ((-1 -1, -1 1, 1 1, 1 -1, -1 -1)), ((-170 -80, -170 -70, -160 -70, -160 -80, -170 -80)) )");
        geoms[6] = reader.read("POINT EMPTY");
        geoms[7] = null;

        insertData(geoms, layer, session);
    }

    public void insertData(final Geometry[] g, final SeLayer layer, final ISession session)
            throws Exception {

        SeColumnDefinition[] colDefs = tempTableColumns;
        final Geometry[] geoms;
        if (g.length < 8) {
            Geometry[] tmp = new Geometry[8];
            System.arraycopy(g, 0, tmp, 0, g.length);
            geoms = tmp;
        } else {
            geoms = g;
        }

        final SeCoordinateReference coordref = layer.getCoordRef();
        final SeShape shapes[] = new SeShape[8];
        for (int i = 0; i < shapes.length; i++) {
            Geometry geom = geoms[i];
            SeShape shape;
            if (geom == null) {
                shape = null;
            } else {
                IsValidOp validationOp = new IsValidOp(geom);
                TopologyValidationError validationError = validationOp.getValidationError();
                if (validationError != null) {
                    throw new IllegalArgumentException("Provided geometry is invalid: "
                            + validationError.getMessage());
                }

                ArcSDEGeometryBuilder builder = ArcSDEGeometryBuilder.builderFor(geom.getClass());
                shape = builder.constructShape(geom, coordref);
            }
            shapes[i] = shape;
        }
        /*
         * Define the names of the columns that data is to be inserted into.
         */
        final String[] columns = new String[8];

        columns[0] = colDefs[1].getName(); // INT32 column
        columns[1] = colDefs[2].getName(); // INT16 column
        columns[2] = colDefs[3].getName(); // FLOAT32 column
        columns[3] = colDefs[4].getName(); // FLOAT64 column
        columns[4] = colDefs[5].getName(); // String column
        columns[5] = colDefs[6].getName(); // NString column
        columns[6] = colDefs[7].getName(); // Date column
        columns[7] = "SHAPE"; // Shape column

        Command<Void> insertDataCmd = new Command<Void>() {
            @Override
            public Void execute(ISession session, SeConnection connection) throws SeException,
                    IOException {

                SeInsert insert = new SeInsert(connection);
                insert.intoTable(layer.getName(), columns);
                insert.setWriteMode(true);

                Calendar cal = Calendar.getInstance();
                // Year, month, date, hour, minute, second.
                cal.set(2004, 06, 1, 0, 0, 0);

                try {
                    for (int i = 1; i <= shapes.length; i++) {
                        SeRow row = insert.getRowToSet();
                        // col #0 is the sde managed row id
                        row.setInteger(0, Integer.valueOf(i));
                        row.setShort(1, Short.valueOf((short) i));
                        row.setFloat(2, new Float(i / 10.0F));
                        row.setDouble(3, new Double(i / 10D));
                        row.setString(4, "FEATURE_" + i);
                        row.setNString(5, "NSTRING_" + i);
                        cal.set(Calendar.DAY_OF_MONTH, i);
                        row.setTime(6, cal);
                        SeShape seShape = shapes[i - 1];
                        row.setShape(7, seShape);

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
     * Creates a SimpleFeatureCollection with features whose schema adheres to the one created in
     * <code>createTestData()</code> and returns it.
     * <p>
     * This schema is something like:
     * 
     * <pre>
     * 
     * colDefs[0] &quot;INT32_COL&quot;, SeColumnDefinition.TYPE_INTEGER, 10, 0, true colDefs[1] =
     * &quot;INT16_COL&quot;, SeColumnDefinition.TYPE_SMALLINT, 4, 0, true colDefs[2] =
     * &quot;FLOAT32_COL&quot;, SeColumnDefinition.TYPE_FLOAT, 5, 2, true colDefs[3] =
     * &quot;FLOAT64_COL&quot;, SeColumnDefinition.TYPE_DOUBLE, 15, 4, true colDefs[4] =
     * &quot;STRING_COL&quot;, SeColumnDefinition.TYPE_STRING, 25, 0, true colDefs[5] =
     * &quot;NSTRING_COL&quot;, SeColumnDefinition.TYPE_NSTRING, 25, 0, true colDefs[6] =
     * &quot;DATE_COL&quot;, SeColumnDefinition.TYPE_DATE, 1, 0, true colDefs[7] =
     * &quot;SHAPE&quot;, Geometry, 1, 0, true
     * 
     * </pre>
     * 
     * </p>
     * 
     * @param jtsGeomType
     *            class of JTS geometry to create
     * @param numFeatures
     *            number of features to create.
     * @throws IOException
     *             if the schema for te test table cannot be fetched from the database.
     * @throws SeException
     */
    public SimpleFeatureCollection createTestFeatures(Class<? extends Geometry> jtsGeomType,
            int numFeatures) throws IOException, SeException {
        SimpleFeatureCollection col = FeatureCollections.newCollection();
        SimpleFeatureType type = getDataStore().getSchema(getTempTableName());
        Object[] values = new Object[type.getAttributeCount()];

        for (int i = 0; i < numFeatures; i++) {
            values[0] = Integer.valueOf(i);

            // put some nulls
            values[1] = ((i % 2) == 0) ? null : Integer.valueOf(2 * i);
            values[2] = new Float(0.1 * i);
            values[3] = new Double(1000 * i);
            values[4] = "String value #" + i;
            values[5] = "NString value #" + i;

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, i);
            values[6] = cal.getTime();
            values[7] = createTestGeometry(jtsGeomType, i);

            SimpleFeature f = SimpleFeatureBuilder.build(type, values, null);
            col.add(f);
        }

        return col;
    }

    private static Geometry createTestGeometry(Class<? extends Geometry> geomType, int index) {
        Geometry geom = null;
        GeometryFactory gf = new GeometryFactory();

        if (geomType == Geometry.class) {
            geom = createTestGenericGeometry(gf, index);
        } else if (geomType == Point.class) {
            geom = createTestPoint(gf, index);
        } else if (geomType == MultiPoint.class) {
            geom = createTestMultiPoint(gf, index);
        } else if (geomType == LineString.class) {
            geom = createTestLineString(gf, index);
        } else if (geomType == MultiLineString.class) {
            geom = createTestMultiLineString(gf, index);
        } else if (geomType == Polygon.class) {
            geom = createTestPolygon(gf, index);
        } else if (geomType == MultiPolygon.class) {
            geom = createTestMultiPolygon(gf, index);
        } else {
            throw new UnsupportedOperationException("finish implementing this!");
        }

        return geom;
    }

    private static Geometry createTestGenericGeometry(GeometryFactory gf, int index) {
        if ((index % 6) == 0) {
            return createTestPoint(gf, index);
        } else if ((index % 5) == 0) {
            return createTestMultiPoint(gf, index);
        } else if ((index % 4) == 0) {
            return createTestLineString(gf, index);
        } else if ((index % 3) == 0) {
            return createTestMultiLineString(gf, index);
        } else if ((index % 2) == 0) {
            return createTestPolygon(gf, index);
        } else {
            return createTestMultiPolygon(gf, index);
        }
    }

    private static Point createTestPoint(GeometryFactory gf, int index) {
        return gf.createPoint(new Coordinate(index, index));
    }

    private static MultiPoint createTestMultiPoint(GeometryFactory gf, int index) {
        Coordinate[] coords = { new Coordinate(index, index), new Coordinate(-index, -index) };

        return gf.createMultiPoint(coords);
    }

    private static LineString createTestLineString(final GeometryFactory gf, final int index) {
        Coordinate[] coords = { new Coordinate(0, 0), new Coordinate(1 + index, -index) };

        return gf.createLineString(coords);
    }

    private static MultiLineString createTestMultiLineString(final GeometryFactory gf,
            final int index) {
        Coordinate[] coords1 = { new Coordinate(0, 0), new Coordinate(1 + index, 1 + index) };
        Coordinate[] coords2 = { new Coordinate(0, index), new Coordinate(index + 1, 0) };
        LineString[] lines = { gf.createLineString(coords1), gf.createLineString(coords2) };

        return gf.createMultiLineString(lines);
    }

    private static Polygon createTestPolygon(GeometryFactory gf, int index) {
        Coordinate[] coords = { new Coordinate(index, index), new Coordinate(index, index + 1),
                new Coordinate(index + 1, index + 1), new Coordinate(index + 1, index),
                new Coordinate(index, index) };
        LinearRing shell = gf.createLinearRing(coords);

        return gf.createPolygon(shell, null);
    }

    private static MultiPolygon createTestMultiPolygon(GeometryFactory gf, int index) {
        Polygon[] polys = { createTestPolygon(gf, index), createTestPolygon(gf, 1 + index) };

        MultiPolygon mp = gf.createMultiPolygon(polys);
        // System.out.println(mp);

        return mp;
    }

    /**
     * Creates and returns a <code>SeCoordinateReference</code> CRS, though based on WGS84, is
     * inclusive enough (in terms of valid coordinate range and presicion) to deal with most
     * coordintates.
     * <p>
     * Actually tested to deal with coordinates with 0.0002 units of separation as well as with
     * large coordinates such as UTM (values greater than 500,000.00)
     * </p>
     */
    public static SeCoordinateReference getGenericCoordRef() throws SeException {

        SeCoordinateReference seCRS = new SeCoordinateReference();
        final String wgs84WKT = DefaultGeographicCRS.WGS84.toWKT();
        seCRS.setCoordSysByDescription(wgs84WKT);
        // seCRS.setPrecision(1000);
        seCRS.setXYByEnvelope(new SeExtent(-180, -90, 180, 90));
        return seCRS;
    }

    public void createSimpleTestTables() throws IOException, UnavailableConnectionException {
        final ISessionPool connectionPool = getConnectionPool();
        final ISession session = connectionPool.getSession();

        String tableName;
        String rowIdColName;
        int rowIdColumnType;
        int shapeTypeMask;
        try {
            rowIdColName = "ROW_ID";
            shapeTypeMask = SeLayer.SE_POINT_TYPE_MASK;

            tableName = "GT_TEST_POINT_ROWID_USER";
            rowIdColumnType = SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_USER;
            createTestTable(session, tableName, rowIdColName, rowIdColumnType, true, shapeTypeMask);

            tableName = "GT_TEST_POINT_ROWID_SDE";
            rowIdColumnType = SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_SDE;
            createTestTable(session, tableName, rowIdColName, rowIdColumnType, true, shapeTypeMask);

            tableName = "GT_TEST_POINT_ROWID_NONE";
            rowIdColumnType = SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_NONE;
            createTestTable(session, tableName, rowIdColName, rowIdColumnType, true, shapeTypeMask);

            shapeTypeMask = SeLayer.SE_LINE_TYPE_MASK;

            tableName = "GT_TEST_LINE_ROWID_USER";
            rowIdColumnType = SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_USER;
            createTestTable(session, tableName, rowIdColName, rowIdColumnType, true, shapeTypeMask);

            tableName = "GT_TEST_LINE_ROWID_SDE";
            rowIdColumnType = SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_SDE;
            createTestTable(session, tableName, rowIdColName, rowIdColumnType, true, shapeTypeMask);

            tableName = "GT_TEST_LINE_ROWID_NONE";
            rowIdColumnType = SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_NONE;
            createTestTable(session, tableName, rowIdColName, rowIdColumnType, true, shapeTypeMask);

            shapeTypeMask = SeLayer.SE_AREA_TYPE_MASK;

            tableName = "GT_TEST_POLYGON_ROWID_USER";
            rowIdColumnType = SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_USER;
            createTestTable(session, tableName, rowIdColName, rowIdColumnType, true, shapeTypeMask);

            tableName = "GT_TEST_POLYGON_ROWID_SDE";
            rowIdColumnType = SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_SDE;
            createTestTable(session, tableName, rowIdColName, rowIdColumnType, true, shapeTypeMask);

            tableName = "GT_TEST_POLYGON_ROWID_NONE";
            rowIdColumnType = SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_NONE;
            createTestTable(session, tableName, rowIdColName, rowIdColumnType, true, shapeTypeMask);
        } finally {
            session.dispose();
        }
    }

    /**
     * Creates and registers a table, optionally creating a layer for it
     */
    public void createTestTable(final ISession session, final String tableName,
            final String rowIdColName, final int rowIdColumnType, final boolean createLayer,
            final int shapeTypeMask) throws IOException {

        LOGGER.fine("Creating layer " + tableName);

        final Command<Void> createCmd = new Command<Void>() {

            @Override
            public Void execute(ISession session, SeConnection connection) throws SeException,
                    IOException {
                final SeTable table = new SeTable(connection, tableName);

                try {
                    table.delete();
                } catch (SeException e) {
                    LOGGER.fine("table " + tableName + " does not already exist");
                }

                final boolean isNullable = true;

                // ROW_ID, INT_COL, DATE_COL, STRING_COL, SE_ANNO_CAD_DATA
                final int numCols = 5;
                final SeColumnDefinition[] colDefs = new SeColumnDefinition[numCols];

                // first column to be SDE managed feature id
                colDefs[0] = new SeColumnDefinition("ROW_ID", SeColumnDefinition.TYPE_INT32, 10, 0,
                        false);
                colDefs[1] = new SeColumnDefinition("INT_COL", SeColumnDefinition.TYPE_INT32, 10,
                        0, isNullable);
                colDefs[2] = new SeColumnDefinition("DATE_COL", SeColumnDefinition.TYPE_DATE, 1, 0,
                        isNullable);
                colDefs[3] = new SeColumnDefinition("STRING_COL", SeColumnDefinition.TYPE_STRING,
                        25, 0, isNullable);
                // use a blob type just to make sure they're correctly ignored
                colDefs[4] = new SeColumnDefinition("SE_ANNO_CAD_DATA",
                        SeColumnDefinition.TYPE_BLOB, 4000, 0, isNullable);

                /*
                 * Create the table using the DBMS default configuration keyword. Valid keywords are
                 * defined in the dbtune table.
                 */
                table.create(colDefs, configKeyword);

                /*
                 * Register the column to be used as feature id and managed by sde
                 */
                if (SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_NONE != rowIdColumnType) {
                    SeRegistration reg = new SeRegistration(connection, table.getName());
                    LOGGER.fine("setting rowIdColumnName to ROW_ID in table " + reg.getTableName());
                    reg.setRowIdColumnName("ROW_ID");
                    reg.setRowIdColumnType(rowIdColumnType);
                    reg.alter();
                }

                // Only tables with an sde maintained rowid column can be
                // versioned
                if (SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_SDE == rowIdColumnType) {
                    makeVersioned(session, tableName);
                }

                if (createLayer) {
                    final SeLayer layer = new SeLayer(connection);
                    layer.setTableName(tableName);
                    /*
                     * Define the attributes of the spatial column
                     */
                    layer.setSpatialColumnName("GEOM");

                    /*
                     * Set the type of shapes that can be inserted into the layer.
                     */
                    layer.setShapeTypes(SeLayer.SE_NIL_TYPE_MASK | shapeTypeMask);
                    layer.setGridSizes(1100.0, 0.0, 0.0);
                    layer.setDescription("GeoTools test table");

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
                }
                return null;
            }
        };

        session.issue(createCmd);
    }

    public void makeVersioned(final ISession session, final String tableName) throws IOException {

        Command<Void> cmd = new Command<Void>() {

            @Override
            public Void execute(ISession session, SeConnection connection) throws SeException,
                    IOException {
                // make the table multiversioned
                LOGGER.fine("Making " + tableName + " versioned...");
                SeRegistration reg = new SeRegistration(connection, tableName);
                reg.getInfo();
                reg.setMultiVersion(true);
                reg.alter();
                System.err.println(tableName + " successfully made versioned");
                return null;
            }
        };

        session.issue(cmd);
    }

    /**
     * Creates an ArcSDE version named {@code versionName} if it doesn't already exist
     * 
     * @param session
     * @param versionName
     * @param parentVersion
     * @throws IOException
     */
    public void createVersion(final ISession session, final String versionName,
            final String parentVersionName) throws IOException {

        session.issue(new Command<Void>() {

            @Override
            public Void execute(ISession session, SeConnection connection) throws SeException,
                    IOException {

                final SeVersion parentVersion = session.issue(new GetVersionCommand(
                        parentVersionName));
                SeVersion version = null;
                try {
                    version = session.issue(new GetVersionCommand(versionName));
                } catch (ArcSdeException e) {
                    // ignore
                }
                if (version != null) {
                    // already exists, no need to create it
                    return null;
                }

                SeVersion newVersion = new SeVersion(connection, parentVersionName);
                // newVersion.getInfo();
                newVersion.setName(versionName);
                newVersion.setOwner(session.getUser());
                newVersion.setParentName(parentVersionName);
                newVersion.setDescription(parentVersion.getName()
                        + " child for GeoTools ArcSDE unit tests");
                // do not require ArcSDE to create a unique name if the
                // required
                // version already exists
                boolean uniqueName = false;
                try {
                    newVersion.create(uniqueName, newVersion);
                    // newVersion.alter();
                    newVersion.getInfo();
                } catch (SeException e) {
                    throw new ArcSdeException(e);
                }
                return null;
            }
        });

    }

    public void deleteVersion(final ISession s, final String versionName) throws IOException {

        s.issue(new Command<Void>() {

            @Override
            public Void execute(ISession session, SeConnection connection) throws SeException,
                    IOException {

                final SeVersion version;
                try {
                    version = session.issue(new Commands.GetVersionCommand(versionName));
                } catch (IOException e) {
                    // version does not exist, we're ok...
                    return null;
                }

                LOGGER.fine("Deleting version " + versionName);
                version.delete();
                LOGGER.fine("Version " + versionName + " deleted!");

                return null;
            }
        });
    }

    /**
     * Creates a versioned table with a name column and a point SHAPE column
     * 
     * @return the versioned table created
     * @throws Exception
     *             any exception thrown by sde
     */
    public SeTable createVersionedTable(final ISession session) throws Exception {

        final Command<SeTable> createCmd = new Command<SeTable>() {

            @Override
            public SeTable execute(ISession session, SeConnection connection) throws SeException,
                    IOException {
                // SeConnection conn = session.unWrap();
                SeLayer layer = new SeLayer(connection);
                SeTable table;

                /*
                 * Create a qualified table name with current user's name and the name of the table
                 * to be created, "EXAMPLE".
                 */
                String dbname = connection.getDatabaseName();
                String user = connection.getUser();
                StringBuffer sb = new StringBuffer();
                if (dbname != null && dbname.length() > 0) {
                    sb.append(dbname).append(".");
                }
                if (user != null && user.length() > 0) {
                    sb.append(user).append(".");
                }
                String tableName = sb.append("VERSIONED_EXAMPLE").toString().toUpperCase();
                table = new SeTable(connection, tableName);
                layer.setTableName("VERSIONED_EXAMPLE");

                try {
                    table.delete();
                } catch (Exception e) {
                    // ignore, the table didn't exist already
                }

                SeColumnDefinition[] colDefs = new SeColumnDefinition[2];
                boolean isNullable = true;
                // first column to be SDE managed feature id
                colDefs[0] = new SeColumnDefinition("ROW_ID", SeColumnDefinition.TYPE_INT32, 10, 0,
                        false);
                colDefs[1] = new SeColumnDefinition("NAME", SeColumnDefinition.TYPE_STRING, 25, 0,
                        isNullable);

                table.create(colDefs, getConfigKeyword());
                layer.setSpatialColumnName("SHAPE");

                /*
                 * Register the column to be used as feature id and managed by sde
                 */
                SeRegistration reg = new SeRegistration(connection, table.getName());
                LOGGER.fine("setting rowIdColumnName to ROW_ID in table " + reg.getTableName());
                reg.setRowIdColumnName("ROW_ID");
                reg.setRowIdColumnType(SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_SDE);
                reg.alter();

                layer.setShapeTypes(SeLayer.SE_NIL_TYPE_MASK | SeLayer.SE_POINT_TYPE_MASK);
                layer.setGridSizes(1100.0, 0.0, 0.0);
                layer.setDescription("Layer Example");

                SeExtent ext = new SeExtent(0.0, 0.0, 10000.0, 10000.0);
                layer.setExtent(ext);

                /*
                 * Define the layer's Coordinate Reference
                 */
                SeCoordinateReference coordref = getGenericCoordRef();
                layer.setCoordRef(coordref);

                /*
                 * Spatially enable the new table...
                 */
                layer.setCreationKeyword(getConfigKeyword());
                layer.create(3, 4);

                // register the table as versioned
                SeRegistration registration = new SeRegistration(connection, tableName);
                registration.setMultiVersion(true);
                registration.alter();

                return table;
            }
        };

        return session.issue(createCmd);
    }

    public void insertIntoVersionedTable(final ISession session, final SeState state,
            final String tableName, final String nameField) throws IOException {

        session.issue(new Command<Void>() {

            @Override
            public Void execute(ISession session, SeConnection connection) throws SeException,
                    IOException {
                final SeInsert insert = new SeInsert(connection);
                SeObjectId differencesId = new SeObjectId(SeState.SE_NULL_STATE_ID);
                insert.setState(state.getId(), differencesId, SeState.SE_STATE_DIFF_NOCHECK);

                insert.intoTable(tableName, new String[] { "NAME" });
                SeRow row = insert.getRowToSet();
                row.setString(0, "NAME 1");
                insert.execute();
                insert.close();
                return null;
            }
        });
    }

}
