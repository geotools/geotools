/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.DelegatingConnection;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.datasource.ManageableDataSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureWriter;
import org.geotools.data.store.ReprojectingFeatureCollection;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.Geometries;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geopkg.geom.GeoPkgGeomReader;
import org.geotools.geopkg.geom.GeoPkgGeomWriter;
import org.geotools.geopkg.geom.GeometryBooleanFunction;
import org.geotools.geopkg.geom.GeometryDoubleFunction;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCFeatureStore;
import org.geotools.jdbc.PrimaryKey;
import org.geotools.jdbc.util.SqlUtil;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.identity.Identifier;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.sqlite.Function;
import org.sqlite.SQLiteConfig;

/**
 * Provides access to a GeoPackage database.
 *
 * @author Justin Deoliveira, OpenGeo
 * @author Niels Charlier
 */
public class GeoPackage implements Closeable {

    static final Logger LOGGER = Logging.getLogger(GeoPackage.class);

    public static final String GEOPACKAGE_CONTENTS = "gpkg_contents";

    public static final String GEOMETRY_COLUMNS = "gpkg_geometry_columns";

    public static final String SPATIAL_REF_SYS = "gpkg_spatial_ref_sys";

    public static final String DATA_COLUMNS = "gpkg_data_columns";

    public static final String TILE_MATRIX_METADATA = "gpkg_tile_matrix";

    public static final String METADATA = "gpkg_metadata";

    public static final String METADATA_REFERENCE = "gpkg_metadata_reference";

    public static final String TILE_MATRIX_SET = "gpkg_tile_matrix_set";

    public static final String DATA_COLUMN_CONSTRAINTS = "gpkg_data_column_constraints";

    public static final String EXTENSIONS = "gpkg_extensions";

    public static final String SPATIAL_INDEX = "gpkg_spatial_index";

    public static final String SCHEMA = "gpkg_schema";

    /**
     * Adding this key into a {@link FeatureType#getUserData()} with a value of true will allow
     * creating tables without registering them as feature entries in the GeoPackage. Used by
     * extensions to create extra feature tables that should be visible only by clients aware of the
     * specific extension intent and usage.
     */
    public static final String SKIP_REGISTRATION = "skip_registration";

    /**
     * Add this among a AttributeType user data, in order to force a particular {@link DataColumn}
     * description for it. It can be required to add more metadata, to force a mime type, or have
     * fine grained control over its constraints
     */
    public static final String DATA_COLUMN = "gpgk_constraint";

    // requirement 11, two generic SRID are to be considered
    protected static final int GENERIC_GEOGRAPHIC_SRID = 0;
    protected static final int GENERIC_PROJECTED_SRID = -1;
    /** The application id for GeoPackage 1.2 onwards (GPKG) */
    static final int GPKG_120_APPID = 0x47504B47;
    /** The application id for GeoPackage 1.0 (GP10) */
    static final int GPKG_100_APPID = 0x47503130;

    public static enum DataType {
        Feature("features"),
        Raster("rasters"),
        Tile("tiles"),
        FeatureWithRaster("featuresWithRasters");

        String value;

        DataType(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    static final String DATE_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    /** database file */
    File file;

    /** connection pool */
    final DataSource connPool;

    /** datastore for vector access, lazily created */
    volatile JDBCDataStore dataStore;

    private boolean initialised = false;

    protected GeoPkgGeomWriter.Configuration writerConfig = new GeoPkgGeomWriter.Configuration();

    public GeoPkgGeomWriter.Configuration getWriterConfiguration() {
        return writerConfig;
    }

    /** Creates a new empty GeoPackage, generating a new file. */
    public GeoPackage() throws IOException {
        this(File.createTempFile("geopkg", ".db"));
    }

    /**
     * Creates a GeoPackage from an existing file.
     *
     * <p>This constructor assumes no credentials are required to connect to the database.
     */
    public GeoPackage(File file) throws IOException {
        this(file, null, (String) null);
    }

    /** Creates a GeoPackage from an existing file specifying database credentials. */
    public GeoPackage(File file, String user, String passwd) throws IOException {
        this(file, user, passwd, false);
    }

    /** Creates a GeoPackage from an existing file specifying database credentials. */
    public GeoPackage(File file, String user, String passwd, boolean readOnly) throws IOException {
        this.file = file;

        Map<String, Object> params = new HashMap<>();
        if (user != null) {
            params.put(GeoPkgDataStoreFactory.USER.key, user);
        }
        if (passwd != null) {
            params.put(GeoPkgDataStoreFactory.PASSWD.key, passwd);
        }
        if (readOnly) {
            params.put(GeoPkgDataStoreFactory.READ_ONLY.key, readOnly);
        }

        params.put(GeoPkgDataStoreFactory.DATABASE.key, file.getPath());
        params.put(GeoPkgDataStoreFactory.DBTYPE.key, GeoPkgDataStoreFactory.DBTYPE.sample);
        params.put(JDBCDataStoreFactory.BATCH_INSERT_SIZE.key, 1000);

        this.connPool = new GeoPkgDataStoreFactory(writerConfig).createDataSource(params);
    }

    GeoPackage(DataSource dataSource) {
        this.connPool = dataSource;
    }

    /**
     * Builds a GeoPackage from the given store (that has supposedly been created by the {@link
     * GeoPkgDataStoreFactory)}. Used to get access to lower level methods and internals of the
     * GeoPackage.
     *
     * @param dataStore
     */
    public GeoPackage(JDBCDataStore dataStore) {
        if (!(dataStore.getSQLDialect() instanceof GeoPkgDialect)) {
            throw new IllegalArgumentException(
                    "Invalid data store, should be associated to a GeoPkgDialect");
        }
        this.dataStore = dataStore;
        this.connPool = dataStore.getDataSource();
    }

    public GeoPackage(File file, SQLiteConfig config, Map<String, Object> storeParams)
            throws IOException {
        this.file = file;

        // enrich params with the basics
        Map<String, Object> params =
                new HashMap<>(storeParams != null ? storeParams : Collections.emptyMap());
        params.put(GeoPkgDataStoreFactory.DATABASE.key, file.getPath());
        params.put(GeoPkgDataStoreFactory.DBTYPE.key, GeoPkgDataStoreFactory.DBTYPE.sample);

        // setup pool and store honoring the params
        GeoPkgDataStoreFactory factory = new GeoPkgDataStoreFactory(writerConfig);
        this.connPool = factory.createDataSource(params);
        params.put(GeoPkgDataStoreFactory.DATASOURCE.key, this.connPool);
        this.dataStore = factory.createDataStore(params);

        // add connection properties to respect the sqlite config
        for (Map.Entry e : config.toProperties().entrySet()) {
            ((BasicDataSource) connPool)
                    .addConnectionProperty((String) e.getKey(), (String) e.getValue());
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

    /** The database data source. */
    public DataSource getDataSource() {
        return connPool;
    }

    /**
     * Initializes the geopackage database.
     *
     * <p>This method creates all the necessary metadata tables.
     */
    public void init() throws IOException {
        try {
            try (Connection cx = connPool.getConnection()) {
                init(cx);
                initialised = true;
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Initializes a geopackage connection.
     *
     * <p>This method creates all the necessary metadata tables.
     */
    static void init(Connection cx) throws SQLException {
        createFunctions(cx);
        // see if we have to create the table structure
        boolean initialized = false;
        try (Statement st = cx.createStatement();
                ResultSet rs = st.executeQuery("PRAGMA application_id")) {
            if (rs.next()) {
                int applicationId = rs.getInt(1);
                // support legacy application id (before 1.2) as well as newer one (from 1.2)
                initialized = (GPKG_100_APPID == applicationId || GPKG_120_APPID == applicationId);
            }
        }
        if (!initialized) {
            runScript(EXTENSIONS + ".sql", cx);
            runScript(SPATIAL_REF_SYS + ".sql", cx);
            runScript(GEOMETRY_COLUMNS + ".sql", cx);
            runScript(GEOPACKAGE_CONTENTS + ".sql", cx);
            runScript(TILE_MATRIX_SET + ".sql", cx);
            runScript(TILE_MATRIX_METADATA + ".sql", cx);
            runScript(DATA_COLUMNS + ".sql", cx);
            runScript(METADATA + ".sql", cx);
            runScript(METADATA_REFERENCE + ".sql", cx);
            runScript(DATA_COLUMN_CONSTRAINTS + ".sql", cx);
            addDefaultSpatialReferences(cx);
            runSQL("PRAGMA application_id = " + GPKG_120_APPID + ";", cx);
        }
    }

    static void createFunctions(Connection cx) throws SQLException {
        while (cx instanceof DelegatingConnection) {
            cx = ((DelegatingConnection) cx).getDelegate();
        }

        // minx
        Function.create(
                cx,
                "ST_MinX",
                new GeometryDoubleFunction() {

                    @Override
                    public double execute(GeoPkgGeomReader reader) throws IOException {
                        return reader.getEnvelope().getMinX();
                    }
                },
                1,
                Function.FLAG_DETERMINISTIC);

        // maxx
        Function.create(
                cx,
                "ST_MaxX",
                new GeometryDoubleFunction() {
                    @Override
                    public double execute(GeoPkgGeomReader reader) throws IOException {
                        return reader.getEnvelope().getMaxX();
                    }
                },
                1,
                Function.FLAG_DETERMINISTIC);

        // miny
        Function.create(
                cx,
                "ST_MinY",
                new GeometryDoubleFunction() {

                    @Override
                    public double execute(GeoPkgGeomReader reader)
                            throws IOException, SQLException {
                        return reader.getEnvelope().getMinY();
                    }
                },
                1,
                Function.FLAG_DETERMINISTIC);
        // maxy
        Function.create(
                cx,
                "ST_MaxY",
                new GeometryDoubleFunction() {

                    @Override
                    public double execute(GeoPkgGeomReader reader)
                            throws IOException, SQLException {
                        return reader.getEnvelope().getMaxY();
                    }
                },
                1,
                Function.FLAG_DETERMINISTIC);

        // empty
        Function.create(
                cx,
                "ST_IsEmpty",
                new GeometryBooleanFunction() {
                    @Override
                    public boolean execute(GeoPkgGeomReader reader) throws IOException {
                        return reader.getHeader().getFlags().isEmpty();
                    }
                },
                1,
                Function.FLAG_DETERMINISTIC);
    }

    /**
     * Closes the geopackage database connection.
     *
     * <p>The application should always call this method when done with a geopackage to prevent
     * connection leakage.
     */
    @Override
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
     * Adds an epsg crs to the geopackage, registering it in the spatial_ref_sys table.
     *
     * <p>This method will look up the <tt>srid</tt> in the local epsg database. Use {@link
     * #addCRS(CoordinateReferenceSystem, int)} to specify an explicit CRS, authority, code entry.
     */
    public void addCRS(int srid) throws IOException {
        try {
            addCRS(CRS.decode("EPSG:" + srid, true), "epsg", srid);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    protected static void addDefaultSpatialReferences(Connection cx) throws SQLException {
        try {
            addCRS(
                    cx,
                    -1,
                    "Undefined cartesian SRS",
                    "NONE",
                    -1,
                    "undefined",
                    "undefined cartesian coordinate reference system");
            addCRS(
                    cx,
                    0,
                    "Undefined geographic SRS",
                    "NONE",
                    0,
                    "undefined",
                    "undefined geographic coordinate reference system");
            addCRS(
                    cx,
                    4326,
                    "WGS 84 geodetic",
                    "EPSG",
                    4326,
                    "GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\","
                            + "6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],"
                            + "PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,"
                            + "AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]]",
                    "longitude/latitude coordinates in decimal degrees on the WGS 84 spheroid");
        } catch (IOException ex) {
            throw new SQLException("Unable to add default spatial references.", ex);
        }
    }

    public static void addCRS(
            Connection cx,
            int srid,
            String srsName,
            String organization,
            int organizationCoordSysId,
            String definition,
            String description)
            throws IOException {
        try {
            try (PreparedStatement ps =
                            cx.prepareStatement(
                                    String.format(
                                            "SELECT srs_id FROM %s WHERE srs_id = ?",
                                            SPATIAL_REF_SYS));
                    ResultSet rs =
                            prepare(ps).set(srid).log(Level.FINE).statement().executeQuery()) {
                if (rs.next()) {
                    return;
                }
            }

            try (PreparedStatement ps =
                    cx.prepareStatement(
                            String.format(
                                    "INSERT INTO %s (srs_id, srs_name, organization, organization_coordsys_id, definition, description) "
                                            + "VALUES (?,?,?,?,?,?)",
                                    SPATIAL_REF_SYS))) {
                prepare(ps)
                        .set(srid)
                        .set(srsName)
                        .set(organization)
                        .set(organizationCoordSysId)
                        .set(definition)
                        .set(description)
                        .log(Level.FINE)
                        .statement()
                        .execute();
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Adds a crs to the geopackage, registering it in the spatial_ref_sys table.
     *
     * @param crs The crs to add.
     * @param auth The authority code, example: epsg
     * @param srid The spatial reference system id.
     */
    public void addCRS(CoordinateReferenceSystem crs, String auth, int srid) throws IOException {
        try (Connection cx = connPool.getConnection()) {
            addCRS(crs, auth, srid, cx);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Same as {@link #addCRS(CoordinateReferenceSystem, String, int)}, but for internal usage, when
     * a connection is already available
     */
    void addCRS(CoordinateReferenceSystem crs, String auth, int srid, Connection cx)
            throws IOException {
        GeoPackage.addCRS(cx, srid, auth + ":" + srid, auth, srid, crs.toWKT(), auth + ":" + srid);
    }

    private CoordinateReferenceSystem getCRS(int srid) {
        try {

            try (Connection cx = connPool.getConnection()) {

                try (PreparedStatement ps =
                        cx.prepareStatement(
                                String.format(
                                        "SELECT definition FROM %s WHERE srs_id = ?",
                                        SPATIAL_REF_SYS))) {

                    try (ResultSet rs =
                            prepare(ps).set(srid).log(Level.FINE).statement().executeQuery()) {
                        if (rs.next()) {
                            try {
                                return CRS.parseWKT(rs.getString("definition"));
                            } catch (FactoryException e) {
                                LOGGER.log(Level.FINE, "Error parsing CRS definitions!", e);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /** Returns list of contents of the geopackage. */
    public List<Entry> contents() {
        List<Entry> contents = new ArrayList<>();
        try {
            try (Connection cx = connPool.getConnection()) {

                try (Statement st = cx.createStatement()) {

                    try (ResultSet rs = st.executeQuery("SELECT * FROM " + GEOPACKAGE_CONTENTS)) {
                        while (rs.next()) {
                            String dt = rs.getString("data_type");
                            org.geotools.geopkg.Entry.DataType type = Entry.DataType.valueOf(dt);
                            Entry e = null;
                            switch (type) {
                                case Feature:
                                    e = createFeatureEntry(rs);
                                    break;
                                case Tile:
                                    e = createTileEntry(rs, cx);
                                    break;
                                default:
                                    throw new IllegalStateException(
                                            "unexpected type in GeoPackage");
                            }
                            if (e != null) {
                                contents.add(e);
                            }
                        }
                    } catch (IOException e) {
                        LOGGER.log(Level.FINER, e.getMessage(), e);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return contents;
    }
    //
    //    //
    //    // feature methods
    //    //
    //
    /** Lists all the feature entries in the geopackage. */
    public List<FeatureEntry> features() throws IOException {
        try {

            try (Connection cx = connPool.getConnection()) {
                List<FeatureEntry> entries = new ArrayList<>();
                String sql =
                        format(
                                "SELECT a.*, b.column_name, b.geometry_type_name, b.z, b.m, c.organization_coordsys_id, c.definition"
                                        + " FROM %s a, %s b, %s c"
                                        + " WHERE a.table_name = b.table_name"
                                        + " AND a.srs_id = c.srs_id"
                                        + " AND a.data_type = ?",
                                GEOPACKAGE_CONTENTS, GEOMETRY_COLUMNS, SPATIAL_REF_SYS);

                try (PreparedStatement ps = cx.prepareStatement(sql)) {
                    ps.setString(1, DataType.Feature.value());

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            entries.add(createFeatureEntry(rs));
                        }
                    }
                }

                return entries;
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Looks up a feature entry by name.
     *
     * @param name THe name of the feature entry.
     * @return The entry, or <code>null</code> if no such entry exists.
     */
    public FeatureEntry feature(String name) throws IOException {
        try {

            try (Connection cx = connPool.getConnection()) {
                return feature(name, cx);
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    protected FeatureEntry feature(String name, Connection cx) throws SQLException, IOException {
        String sql =
                format(
                        "SELECT a.*, b.column_name, b.geometry_type_name, b.m, b.z, c.organization_coordsys_id, c.definition"
                                + " FROM %s a, %s b, %s c"
                                + " WHERE a.table_name = b.table_name "
                                + " AND a.srs_id = c.srs_id "
                                + " AND a.table_name = ?"
                                + " AND a.data_type = ?",
                        GEOPACKAGE_CONTENTS, GEOMETRY_COLUMNS, SPATIAL_REF_SYS);

        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, DataType.Feature.value());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return createFeatureEntry(rs);
                }
            }
        }

        return null;
    }

    /**
     * Returns the extension by name, or null if the extension is not supported by this
     * implementation
     */
    public GeoPkgExtension getExtension(String name) {
        Iterator<GeoPkgExtensionFactory> factories =
                GeoPkgExtensionFactoryFinder.getExtensionFactories();
        while (factories.hasNext()) {
            GeoPkgExtensionFactory factory = factories.next();
            GeoPkgExtension extension = factory.getExtension(name, this);
            if (extension != null) {
                return extension;
            }
        }

        return null;
    }

    /**
     * Returns the extension by class, or null if the extension is not supported by this
     * implementation
     */
    public <T extends GeoPkgExtension> T getExtension(Class<T> extensionClass) {
        Iterator<GeoPkgExtensionFactory> factories =
                GeoPkgExtensionFactoryFinder.getExtensionFactories();
        while (factories.hasNext()) {
            GeoPkgExtensionFactory factory = factories.next();
            GeoPkgExtension extension = factory.getExtension(extensionClass, this);
            if (extension != null) {
                return extensionClass.cast(extension);
            }
        }

        return null;
    }

    /**
     * Creates a new feature entry in the geopackage.
     *
     * <p>The resulting feature dataset will be empty. The {@link #writer(FeatureEntry, boolean,
     * Transaction)} method returns a writer object that can be used to populate the dataset.
     *
     * @param entry Contains metadata about the feature entry.
     * @param schema The schema of the feature dataset.
     * @throws IOException Any errors occurring while creating the new feature entry.
     */
    public void create(FeatureEntry entry, SimpleFeatureType schema) throws IOException {
        // clone entry so we can work on it
        FeatureEntry e = new FeatureEntry();
        e.init(entry);
        e.setTableName(schema.getTypeName());

        if (e.getGeometryColumn() != null) {
            // check it
            if (schema.getDescriptor(e.getGeometryColumn()) == null) {
                throw new IllegalArgumentException(
                        format(
                                "Geometry column %s does not exist in schema",
                                e.getGeometryColumn()));
            }
        } else {
            e.setGeometryColumn(findGeometryColumn(schema));
        }

        if (e.getIdentifier() == null) {
            e.setIdentifier(schema.getTypeName());
        }
        if (e.getDescription() == null) {
            e.setDescription(e.getIdentifier());
        }

        // check for bounds
        if (e.getBounds() == null) {
            throw new IllegalArgumentException("Entry must have bounds");
        }

        // check for srid
        if (e.getSrid() == null) {
            try {
                e.setSrid(findSRID(schema));
            } catch (Exception ex) {
                throw new IllegalArgumentException(ex);
            }
        }
        if (e.getSrid() == null) {
            throw new IllegalArgumentException("Entry must have srid");
        }

        if (e.getGeometryType() == null) {
            e.setGeometryType(findGeometryType(schema));
        }
        // mark changed
        e.setLastChange(new Date());

        // pass in the feature entry to the datsatore as user data
        schema.getUserData().put(FeatureEntry.class, e);

        JDBCDataStore dataStore = dataStore();

        // create the feature table
        dataStore.createSchema(schema);

        // update the metadata tables
        // addGeoPackageContentsEntry(e);

        // update the entry
        entry.init(e);
    }

    /**
     * According to GeoPKG spec, the coordinates MUST be in XY order. If this FC is in YX format, we
     * reproject to the equivalent XY project.
     *
     * <p>If already XY, return the input FC.
     *
     * @param fc underlying feature collection
     * @return feature collection which is has axis order in XY (NORTH_EAST)
     */
    static SimpleFeatureCollection forceXY(SimpleFeatureCollection fc) {
        CoordinateReferenceSystem sourceCRS = fc.getSchema().getCoordinateReferenceSystem();
        if ((CRS.getAxisOrder(sourceCRS) == CRS.AxisOrder.EAST_NORTH)
                || (CRS.getAxisOrder(sourceCRS) == CRS.AxisOrder.INAPPLICABLE)) {
            return fc;
        }

        for (ReferenceIdentifier identifier : sourceCRS.getIdentifiers()) {
            try {
                String _identifier = identifier.toString();
                CoordinateReferenceSystem flippedCRS = CRS.decode(_identifier, true);
                if (CRS.getAxisOrder(flippedCRS) == CRS.AxisOrder.EAST_NORTH) {
                    ReprojectingFeatureCollection result =
                            new ReprojectingFeatureCollection(fc, flippedCRS);
                    return result;
                }
            } catch (Exception e) {
                // couldn't flip - try again
            }
        }
        return fc;
    }

    /**
     * Adds a new feature dataset to the geopackage.
     *
     * @param entry Contains metadata about the feature entry.
     * @param collection The simple feature collection to add to the geopackage.
     * @throws IOException Any errors occurring while adding the new feature dataset.
     */
    @SuppressWarnings("PMD.UseTryWithResources") // Transaction needs to be rolled back ìn catch
    public void add(FeatureEntry entry, SimpleFeatureCollection collection) throws IOException {
        FeatureEntry e = new FeatureEntry();
        e.init(entry);

        collection = forceXY(collection);

        if (e.getBounds() == null) {
            e.setBounds(collection.getBounds());
        }

        create(e, collection.getSchema());

        Transaction tx = new DefaultTransaction();
        try {
            try (SimpleFeatureWriter w = writer(e, true, null, tx);
                    SimpleFeatureIterator it = collection.features()) {
                while (it.hasNext()) {
                    SimpleFeature f = it.next();
                    SimpleFeature g = w.next();
                    g.setAttributes(f.getAttributes());
                    for (PropertyDescriptor pd : collection.getSchema().getDescriptors()) {
                        /* geopkg spec requires booleans to be stored as SQLite integers this fixes
                         * bug reported by GEOT-5904 */
                        String name = pd.getName().getLocalPart();
                        if (pd.getType().getBinding() == Boolean.class) {
                            int bool = 0;
                            if (f.getAttribute(name) != null) {
                                bool = (Boolean) (f.getAttribute(name)) ? 1 : 0;
                            }
                            g.setAttribute(name, bool);
                        }
                    }

                    w.write();
                }
            }
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
            throw new IOException(ex);
        } finally {
            tx.close();
        }
        entry.init(e);
    }

    /**
     * Adds a new feature dataset to the geopackage.
     *
     * @param entry Contains metadata about the feature entry.
     * @param source The dataset to add to the geopackage.
     * @param filter Filter specifying what subset of feature dataset to include, may be <code>null
     *     </code> to specify no filter.
     * @throws IOException Any errors occurring while adding the new feature dataset.
     */
    public void add(FeatureEntry entry, SimpleFeatureSource source, Filter filter)
            throws IOException {

        // copy over features
        // TODO: make this more robust, won't handle case issues going between datasources, etc...
        // TODO: for big datasets we need to break up the transaction
        if (filter == null) {
            filter = Filter.INCLUDE;
        }

        add(entry, source.getFeatures(filter));
    }

    /**
     * Returns a writer used to modify or add to the contents of a feature dataset.
     *
     * @param entry The feature entry.
     * @param append Flag controlling whether to modify existing contents, or append to the dataset.
     * @param filter Filter determining what subset of dataset to modify, only relevant when
     *     <tt>append</tt> set to false. May be <code>null</code> to specify no filter.
     * @param tx Transaction object, may be <code>null</code> to specify auto commit transaction.
     */
    public SimpleFeatureWriter writer(
            FeatureEntry entry, boolean append, Filter filter, Transaction tx) throws IOException {

        DataStore dataStore = dataStore();
        @SuppressWarnings("PMD.CloseResource") // wrapped and returned
        FeatureWriter w =
                append
                        ? dataStore.getFeatureWriterAppend(entry.getTableName(), tx)
                        : dataStore.getFeatureWriter(entry.getTableName(), filter, tx);

        return Features.simple(w);
    }

    /**
     * Returns a reader for the contents of a feature dataset.
     *
     * @param entry The feature entry.
     * @param filter Filter Filter determining what subset of dataset to return. May be <code>null
     *     </code> to specify no filter.
     * @param tx Transaction object, may be <code>null</code> to specify auto commit transaction.
     */
    public SimpleFeatureReader reader(FeatureEntry entry, Filter filter, Transaction tx)
            throws IOException {
        Query q = new Query(entry.getTableName());
        q.setFilter(filter != null ? filter : Filter.INCLUDE);

        return Features.simple(dataStore().getFeatureReader(q, tx));
    }

    static Integer findSRID(SimpleFeatureType schema) throws Exception {
        CoordinateReferenceSystem crs = schema.getCoordinateReferenceSystem();
        if (crs == null) {
            GeometryDescriptor gd = findGeometryDescriptor(schema);
            crs = gd.getCoordinateReferenceSystem();
        }

        return crs != null ? CRS.lookupEpsgCode(crs, true) : null;
    }

    static String findGeometryColumn(SimpleFeatureType schema) {
        GeometryDescriptor gd = findGeometryDescriptor(schema);
        return gd != null ? gd.getLocalName() : null;
    }

    static Geometries findGeometryType(SimpleFeatureType schema) {
        GeometryDescriptor gd = findGeometryDescriptor(schema);
        if (gd != null) {
            @SuppressWarnings("unchecked")
            Class<? extends Geometry> binding =
                    (Class<? extends Geometry>) gd.getType().getBinding();
            return Geometries.getForBinding(binding);
        }
        return null;
    }

    static GeometryDescriptor findGeometryDescriptor(SimpleFeatureType schema) {
        GeometryDescriptor gd = schema.getGeometryDescriptor();
        if (gd == null) {
            for (PropertyDescriptor pd : schema.getDescriptors()) {
                if (pd instanceof GeometryDescriptor) {
                    return (GeometryDescriptor) pd;
                }
            }
        }
        return gd;
    }

    FeatureEntry createFeatureEntry(ResultSet rs) throws SQLException, IOException {
        FeatureEntry e = new FeatureEntry();
        initEntry(e, rs);

        e.setGeometryColumn(rs.getString("column_name"));
        e.setGeometryType(Geometries.getForName(rs.getString("geometry_type_name")));
        e.setZ(rs.getBoolean("z"));
        e.setM(rs.getBoolean("m"));

        return e;
    }

    void addGeoPackageContentsEntry(Entry e, Connection cx) throws IOException {
        try {
            final SimpleDateFormat dateFormat = getDateFormat();
            if (!initialised) {
                init(cx);
            }
            Integer srid = e.getSrid();
            if (srid != null && srid != GENERIC_PROJECTED_SRID && srid != GENERIC_GEOGRAPHIC_SRID) {
                addCRS(CRS.decode("EPSG:" + srid, true), "epsg", srid, cx);
            }

            StringBuilder sb = new StringBuilder();
            StringBuilder vals = new StringBuilder();

            sb.append(
                    format(
                            "INSERT INTO %s (table_name, data_type, identifier",
                            GEOPACKAGE_CONTENTS));
            vals.append("VALUES (?,?,?");

            if (e.getDescription() != null) {
                sb.append(", description");
                vals.append(",?");
            }

            if (e.getLastChange() != null) {
                sb.append(", last_change");
                vals.append(",?");
            }

            sb.append(", min_x, min_y, max_x, max_y");
            vals.append(",?,?,?,?");

            if (srid != null) {
                sb.append(", srs_id");
                vals.append(",?");
            }
            sb.append(") ").append(vals.append(")").toString());

            SqlUtil.PreparedStatementBuilder psb =
                    prepare(cx, sb.toString())
                            .set(e.getTableName())
                            .set(e.getDataType().value())
                            .set(e.getIdentifier());

            if (e.getDescription() != null) {
                psb.set(e.getDescription());
            }

            if (e.getLastChange() != null) {
                psb.set(dateFormat.format(e.getLastChange()));
            }
            if (e.getBounds() != null) {
                psb.set(e.getBounds().getMinX())
                        .set(e.getBounds().getMinY())
                        .set(e.getBounds().getMaxX())
                        .set(e.getBounds().getMaxY());
            } else {
                double minx = 0;
                double miny = 0;
                double maxx = 0;
                double maxy = 0;
                if (srid != null) {
                    CoordinateReferenceSystem crs = getCRS(srid);
                    if (crs != null) {
                        org.opengis.geometry.Envelope env = CRS.getEnvelope(crs);
                        if (env != null) {
                            minx = env.getMinimum(0);
                            miny = env.getMinimum(1);
                            maxx = env.getMaximum(0);
                            maxy = env.getMaximum(1);
                        }
                    }
                }
                psb.set(minx).set(miny).set(maxx).set(maxy);
            }
            if (srid != null) {
                psb.set(srid);
            }

            try (PreparedStatement ps = psb.log(Level.FINE).statement()) {
                ps.execute();
            }

        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }

    /** Returns a new instance of SimpleDateFormat with the default GeoPackage ISO formatting */
    public static SimpleDateFormat getDateFormat() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_STRING);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat;
    }

    void deleteGeoPackageContentsEntry(Entry e) throws IOException {
        String sql = format("DELETE FROM %s WHERE table_name = ?", GEOPACKAGE_CONTENTS);
        try (Connection cx = connPool.getConnection();
                PreparedStatement ps =
                        prepare(cx, sql).set(e.getTableName()).log(Level.FINE).statement()) {
            ps.execute();
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    void addGeometryColumnsEntry(FeatureEntry e, Connection cx) throws IOException {
        // geometryless tables should not be inserted into this table.
        if (e.getGeometryColumn() == null || e.getGeometryColumn().isEmpty()) {
            return;
        }
        String sql = format("INSERT INTO %s VALUES (?, ?, ?, ?, ?, ?);", GEOMETRY_COLUMNS);

        try (PreparedStatement ps =
                prepare(cx, sql)
                        .set(e.getTableName())
                        .set(e.getGeometryColumn())
                        .set(e.getGeometryType() != null ? e.getGeometryType().getName() : null)
                        .set(e.getSrid())
                        .set(e.isZ())
                        .set(e.isM())
                        .log(Level.FINE)
                        .statement()) {
            ps.execute();
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    void deleteGeometryColumnsEntry(FeatureEntry e) throws IOException {
        String sql = format("DELETE FROM %s WHERE table_name = ?", GEOMETRY_COLUMNS);
        try (Connection cx = connPool.getConnection();
                PreparedStatement ps =
                        prepare(cx, sql).set(e.getTableName()).log(Level.FINE).statement()) {
            ps.execute();
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * Create a spatial index
     *
     * @param e feature entry to create spatial index for
     */
    public void createSpatialIndex(FeatureEntry e) throws IOException {
        Map<String, String> properties = new HashMap<>();

        PrimaryKey pk =
                ((JDBCFeatureStore) (dataStore.getFeatureSource(e.getTableName()))).getPrimaryKey();
        if (pk.getColumns().size() != 1) {
            throw new IOException("Spatial index only supported for primary key of single column.");
        }

        properties.put("t", e.getTableName());
        properties.put("c", e.getGeometryColumn());
        properties.put("i", pk.getColumns().get(0).getName());

        try (Connection cx = connPool.getConnection()) {
            runScript(SPATIAL_INDEX + ".sql", cx, properties);
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    static Integer findSRID(GridCoverage2D raster) throws Exception {
        return CRS.lookupEpsgCode(raster.getCoordinateReferenceSystem(), true);
    }

    static ReferencedEnvelope findBounds(GridCoverage2D raster) {
        org.opengis.geometry.Envelope e = raster.getEnvelope();
        return new ReferencedEnvelope(
                e.getMinimum(0),
                e.getMaximum(0),
                e.getMinimum(1),
                e.getMaximum(1),
                raster.getCoordinateReferenceSystem());
    }

    static GeneralEnvelope toGeneralEnvelope(ReferencedEnvelope e) {
        GeneralEnvelope ge =
                new GeneralEnvelope(
                        new double[] {e.getMinX(), e.getMinY()},
                        new double[] {e.getMaxX(), e.getMaxY()});
        ge.setCoordinateReferenceSystem(e.getCoordinateReferenceSystem());
        return ge;
    }

    //
    // tile methods
    //

    /** Lists all the tile entries in the geopackage. */
    public List<TileEntry> tiles() throws IOException {
        List<TileEntry> entries = new ArrayList<>();
        String sql =
                format(
                        "SELECT a.*, c.organization_coordsys_id, c.definition"
                                + " FROM %s a, %s c"
                                + " WHERE a.srs_id = c.srs_id"
                                + " AND a.data_type = ?",
                        GEOPACKAGE_CONTENTS, SPATIAL_REF_SYS);
        LOGGER.fine(sql);
        try (Connection cx = connPool.getConnection();
                PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setString(1, DataType.Tile.value());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    entries.add(createTileEntry(rs, cx));
                }
            }

            return entries;

        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Looks up a tile entry by name.
     *
     * @param name THe name of the tile entry.
     * @return The entry, or <code>null</code> if no such entry exists.
     */
    public TileEntry tile(String name) throws IOException {
        String sql =
                format(
                        "SELECT a.*, c.organization_coordsys_id, c.definition"
                                + " FROM %s a, %s c"
                                + " WHERE a.srs_id = c.srs_id"
                                + " AND a.table_name = ?"
                                + " AND a.data_type = ?",
                        GEOPACKAGE_CONTENTS, SPATIAL_REF_SYS);
        LOGGER.fine(sql);
        try (Connection cx = connPool.getConnection();
                PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, DataType.Tile.value());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return createTileEntry(rs, cx);
                }
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }
        return null;
    }

    /**
     * Creates a new tile entry in the geopackage.
     *
     * @param entry The tile entry.
     */
    public void create(TileEntry entry) throws IOException {
        if (entry.getBounds() == null) {
            throw new IllegalArgumentException("Tile entry must specify bounds");
        }

        TileEntry e = new TileEntry();
        e.init(entry);

        if (e.getTableName() == null) {
            e.setTableName("tiles");
        }
        if (e.getIdentifier() == null) {
            e.setIdentifier(e.getTableName());
        }
        if (e.getDescription() == null) {
            e.setDescription(e.getIdentifier());
        }

        if (e.getSrid() == null) {
            try {
                e.setSrid(findSRID(entry.getBounds()));
            } catch (Exception ex) {
                throw new IOException(ex);
            }
        }

        e.setLastChange(new Date());

        try (Connection cx = connPool.getConnection()) {
            // TODO: do all of this in a transaction
            // add entry to tile matrix set table
            Envelope bounds = e.getTileMatrixSetBounds();
            if (bounds == null) {
                bounds = e.getBounds();
            }
            try (PreparedStatement st =
                    prepare(cx, format("INSERT INTO %s VALUES (?,?,?,?,?,?)", TILE_MATRIX_SET))
                            .set(e.getTableName())
                            .set(e.getSrid())
                            .set(bounds.getMinX())
                            .set(bounds.getMinY())
                            .set(bounds.getMaxX())
                            .set(bounds.getMaxY())
                            .statement(); ) {
                st.execute();
            }

            // create the tile_matrix_metadata entries
            try (PreparedStatement st =
                    prepare(
                                    cx,
                                    format(
                                            "INSERT INTO %s VALUES (?,?,?,?,?,?,?,?)",
                                            TILE_MATRIX_METADATA))
                            .statement()) {
                for (TileMatrix m : e.getTileMatricies()) {
                    prepare(st)
                            .set(e.getTableName())
                            .set(m.getZoomLevel())
                            .set(m.getMatrixWidth())
                            .set(m.getMatrixHeight())
                            .set(m.getTileWidth())
                            .set(m.getTileHeight())
                            .set(m.getXPixelSize())
                            .set(m.getYPixelSize())
                            .statement()
                            .execute();
                }
            }

            // create the tile table itself
            try (PreparedStatement st =
                    cx.prepareStatement(
                            format(
                                    "CREATE TABLE %s ("
                                            + "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                                            + "zoom_level INTEGER NOT NULL,"
                                            + "tile_column INTEGER NOT NULL,"
                                            + "tile_row INTEGER NOT NULL,"
                                            + "tile_data BLOB NOT NULL)",
                                    e.getTableName()))) {
                st.execute();
            }

            // create an index on the tile
            try (PreparedStatement st =
                    cx.prepareStatement(
                            format(
                                    "create index %s_zyx_idx on %s(zoom_level, tile_column, tile_row);",
                                    e.getTableName(), e.getTableName()))) {
                st.execute();
            }

            // update the metadata tables
            addGeoPackageContentsEntry(e, cx);
        } catch (SQLException ex) {
            throw new IOException(ex);
        }

        entry.init(e);
    }

    /**
     * Adds a tile to the geopackage.
     *
     * @param entry The tile metadata entry.
     * @param tile The tile.
     */
    public void add(TileEntry entry, Tile tile) throws IOException {
        try (Connection cx = connPool.getConnection();
                PreparedStatement ps =
                        prepare(
                                        cx,
                                        format(
                                                "INSERT INTO %s (zoom_level, tile_column,"
                                                        + " tile_row, tile_data) VALUES (?,?,?,?)",
                                                entry.getTableName()))
                                .set(tile.getZoom())
                                .set(tile.getColumn())
                                .set(tile.getRow())
                                .set(tile.getData())
                                .log(Level.FINE)
                                .statement()) {
            ps.execute();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Retrieve tiles within certain zooms and column/row boundaries.
     *
     * @param entry the tile entry
     * @param lowZoom low zoom boundary
     * @param highZoom high zoom boundary
     * @param lowCol low column boundary
     * @param highCol high column boundary
     * @param lowRow low row boundary
     * @param highRow high row boundary
     */
    @SuppressWarnings("PMD.CloseResource") // cx and st get into the TileReader
    public TileReader reader(
            TileEntry entry,
            Integer lowZoom,
            Integer highZoom,
            Integer lowCol,
            Integer highCol,
            Integer lowRow,
            Integer highRow)
            throws IOException {

        List<String> q = new ArrayList<>();
        addRange("zoom_level", lowZoom, highZoom, q);
        addRange("tile_column", lowCol, highCol, q);
        addRange("tile_row", lowRow, highRow, q);

        StringBuffer sql =
                new StringBuffer("SELECT * FROM \"").append(entry.getTableName()).append("\"");
        if (!q.isEmpty()) {
            sql.append(" WHERE ");
            for (String s : q) {
                sql.append(s).append(" AND ");
            }
            sql.setLength(sql.length() - 5);
        }
        try {
            Connection cx = connPool.getConnection();
            Statement st = cx.createStatement();
            ResultSet rs = st.executeQuery(sql.toString());
            return new TileReader(rs, st, cx);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    public void addRange(String attribute, Integer low, Integer high, List<String> q) {
        if (low != null && high != null && low.equals(high)) {
            q.add(attribute + " = " + low);
        } else {
            if (low != null) {
                q.add(attribute + " >= " + low);
            }
            if (high != null) {
                q.add(attribute + " <= " + high);
            }
        }
    }

    protected String getSpatialIndexName(FeatureEntry entry) {

        String spatial_index = "rtree_" + entry.getTableName() + "_" + entry.getGeometryColumn();
        return spatial_index;
    }
    /**
     * Verifies if a spatial index is present
     *
     * @param entry The feature entry.
     * @return whether this feature entry has a spatial index available.
     */
    public boolean hasSpatialIndex(FeatureEntry entry) throws IOException {
        String tableName = getSpatialIndexName(entry);
        try (Connection cx = connPool.getConnection();
                PreparedStatement ps =
                        prepare(cx, "SELECT name FROM sqlite_master WHERE type='table' AND name=? ")
                                .set(tableName)
                                .log(Level.FINE)
                                .statement();
                ResultSet rs = ps.executeQuery()) {

            return rs.next();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Searches a spatial index.
     *
     * @param entry The feature entry.
     * @param minX Optional minimum x boundary.
     * @param minY Optional minimum y boundary.
     * @param maxX Optional maximum x boundary.
     * @param maxY Optional maximum y boundary.
     */
    public Set<Identifier> searchSpatialIndex(
            FeatureEntry entry, Double minX, Double minY, Double maxX, Double maxY)
            throws IOException {
        List<String> q = new ArrayList<>();

        if (minX != null) {
            q.add("minx >= " + minX);
        }
        if (minY != null) {
            q.add("miny >= " + minY);
        }
        if (maxX != null) {
            q.add("maxx <= " + maxX);
        }
        if (maxY != null) {
            q.add("maxy <= " + maxY);
        }
        // Make Sure the table name is escaped - GEOT-5852
        StringBuffer sql =
                new StringBuffer("SELECT id FROM ")
                        .append("\"" + getSpatialIndexName(entry) + "\"");
        if (!q.isEmpty()) {
            sql.append(" WHERE ");
            for (String s : q) {
                sql.append(s).append(" AND ");
            }
            sql.setLength(sql.length() - 5);
        }

        try (Connection cx = connPool.getConnection();
                Statement st = cx.createStatement();
                ResultSet rs = st.executeQuery(sql.toString())) {
            HashSet<Identifier> ids = new HashSet<>();
            while (rs.next()) {
                ids.add(new FeatureIdImpl(rs.getString(1)));
            }
            return ids;
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Retrieve tile boundaries (min row, max row, min column and max column) for a particular zoom
     * level, available in the actual data
     *
     * @param entry The tile entry
     * @param zoom the zoom level
     * @param isMax true for max boundary, false for min boundary
     * @param isRow true for rows, false for columns
     * @return the min/max column/row of the zoom level available in the data
     */
    public int getTileBound(TileEntry entry, int zoom, boolean isMax, boolean isRow)
            throws IOException {
        try {

            int tileBounds = -1;

            StringBuffer sql =
                    new StringBuffer(
                            "SELECT "
                                    + (isMax ? "MAX" : "MIN")
                                    + "( "
                                    + (isRow ? "tile_row" : "tile_column")
                                    + ") FROM ");
            sql.append(entry.getTableName());
            sql.append(" WHERE zoom_level == ");
            sql.append(zoom);

            try (Connection cx = connPool.getConnection();
                    Statement st = cx.createStatement();
                    ResultSet rs = st.executeQuery(sql.toString())) {
                if (!rs.next()) {
                    throw new SQLException(
                            "Could not compute tile bounds, query did not return any record");
                }
                tileBounds = rs.getInt(1);
            }

            return tileBounds;

        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    static TileEntry createTileEntry(ResultSet rs, Connection cx) throws SQLException, IOException {
        TileEntry e = new TileEntry();
        initEntry(e, rs);

        // load all the tile matrix entries (and join with the data table to see if a certain level
        // has tiles available, given the indexes in the data table, it should be real quick)
        try (PreparedStatement psm =
                cx.prepareStatement(
                        format(
                                "SELECT *, exists(SELECT 1 FROM \"%s\" data where data.zoom_level = tileMatrix.zoom_level) as has_tiles"
                                        + " FROM %s as tileMatrix"
                                        + " WHERE table_name = ?"
                                        + " ORDER BY zoom_level ASC",
                                e.getTableName(), TILE_MATRIX_METADATA))) {
            psm.setString(1, e.getTableName());

            try (ResultSet rsm = psm.executeQuery()) {
                while (rsm.next()) {
                    TileMatrix m = new TileMatrix();
                    m.setZoomLevel(rsm.getInt("zoom_level"));
                    m.setMatrixWidth(rsm.getInt("matrix_width"));
                    m.setMatrixHeight(rsm.getInt("matrix_height"));
                    m.setTileWidth(rsm.getInt("tile_width"));
                    m.setTileHeight(rsm.getInt("tile_height"));
                    m.setXPixelSize(rsm.getDouble("pixel_x_size"));
                    m.setYPixelSize(rsm.getDouble("pixel_y_size"));
                    m.setTiles(rsm.getBoolean("has_tiles"));

                    e.getTileMatricies().add(m);
                }
            }
        }
        // use the tile matrix set bounds rather that gpkg_contents bounds
        // per spec, the tile matrix set bounds should be exact and used to calculate tile
        // coordinates
        // and in contrast the gpkg_contents is "informational" only
        try (PreparedStatement psm =
                cx.prepareStatement(
                        format(
                                "SELECT * FROM %s a, %s b "
                                        + "WHERE a.table_name = ? "
                                        + "AND a.srs_id = b.srs_id "
                                        + "LIMIT 1",
                                TILE_MATRIX_SET, SPATIAL_REF_SYS))) {
            psm.setString(1, e.getTableName());

            try (ResultSet rsm = psm.executeQuery()) {
                if (rsm.next()) {

                    int srid = rsm.getInt("organization_coordsys_id");
                    e.setSrid(srid);

                    CoordinateReferenceSystem crs;
                    try {
                        crs = CRS.decode("EPSG:" + srid, true);
                    } catch (Exception ex) {
                        // not a major concern, by spec the tile matrix set srs should match the
                        // gpkg_contents srs_id
                        // which can found in the tile entry bounds
                        crs = e.getBounds().getCoordinateReferenceSystem();
                    }

                    e.setTileMatrixSetBounds(
                            new ReferencedEnvelope(
                                    rsm.getDouble("min_x"),
                                    rsm.getDouble("max_x"),
                                    rsm.getDouble("min_y"),
                                    rsm.getDouble("max_y"),
                                    crs));
                }
            }
        }
        return e;
    }

    static Integer findSRID(ReferencedEnvelope e) throws Exception {
        return CRS.lookupEpsgCode(e.getCoordinateReferenceSystem(), true);
    }

    //
    // sql utility methods
    //

    static void initEntry(Entry e, ResultSet rs) throws SQLException, IOException {
        e.setIdentifier(rs.getString("identifier"));
        e.setDescription(rs.getString("description"));
        e.setTableName(rs.getString("table_name"));
        try {
            final SimpleDateFormat dateFormat = getDateFormat();
            e.setLastChange(dateFormat.parse(rs.getString("last_change")));
        } catch (ParseException ex) {
            throw new IOException(ex);
        }

        int srid = rs.getInt("organization_coordsys_id");
        e.setSrid(srid);

        CoordinateReferenceSystem crs = decodeCRSFromResultset(rs, srid);

        e.setBounds(
                new ReferencedEnvelope(
                        rs.getDouble("min_x"),
                        rs.getDouble("max_x"),
                        rs.getDouble("min_y"),
                        rs.getDouble("max_y"),
                        crs));
    }

    static CoordinateReferenceSystem decodeSRID(int srid) throws FactoryException {
        if (srid == GENERIC_GEOGRAPHIC_SRID || srid == GENERIC_PROJECTED_SRID) {
            return DefaultEngineeringCRS.GENERIC_2D;
        }
        return CRS.decode("EPSG:" + srid, true);
    }

    private static CoordinateReferenceSystem decodeCRSFromResultset(ResultSet rs, int srid)
            throws IOException {
        try {
            return decodeSRID(srid);
        } catch (Exception ex) {
            // try parsing srtext directly
            try {
                return CRS.parseWKT(rs.getString("srtext"));
            } catch (Exception e2) {
                throw new IOException(ex);
            }
        }
    }

    static void runSQL(String sql, Connection cx) throws SQLException {
        try (Statement st = cx.createStatement()) {
            st.execute(sql);
        }
    }

    static void runScript(String filename, Connection cx) throws SQLException {
        SqlUtil.runScript(GeoPackage.class.getResourceAsStream(filename), cx);
    }

    void runScript(String filename, Connection cx, Map<String, String> properties)
            throws SQLException {
        SqlUtil.runScript(getClass().getResourceAsStream(filename), cx, properties);
    }

    JDBCDataStore dataStore() throws IOException {
        if (dataStore == null) {
            synchronized (this) {
                if (dataStore == null) {
                    dataStore = createDataStore();
                }
            }
        }
        return dataStore;
    }

    JDBCDataStore createDataStore() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put(GeoPkgDataStoreFactory.DATASOURCE.key, connPool);
        return new GeoPkgDataStoreFactory(writerConfig).createDataStore(params);
    }
}
