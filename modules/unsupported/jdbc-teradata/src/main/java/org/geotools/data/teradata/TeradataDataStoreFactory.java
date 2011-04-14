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
 */
package org.geotools.data.teradata;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.jdbc.CompositePrimaryKeyFinder;
import org.geotools.jdbc.HeuristicPrimaryKeyFinder;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.MetadataTablePrimaryKeyFinder;
import org.geotools.jdbc.PrimaryKeyFinder;
import org.geotools.jdbc.SQLDialect;
import org.geotools.util.logging.Logging;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.util.InternationalString;

public class TeradataDataStoreFactory extends JDBCDataStoreFactory {

    private static final Logger LOGGER = Logging.getLogger(TeradataDataStoreFactory.class);

    /**
     * parameter for database type
     */
    public static final Param DBTYPE = new Param("dbtype", String.class, "Type", true, "teradata");

    /**
     * enables using && in bbox queries
     */
    public static final Param LOOSEBBOX = new Param("Loose bbox", Boolean.class,
            "Perform only primary filter on bbox", false, Boolean.TRUE);

    /**
     * parameter for database port
     */
    public static final Param PORT = new Param("port", Integer.class, "Port", true, 1025);

    public static final Param TMODE = new Param("tmode", String.class, "tmode", false, "ANSI");

    public static final Param CHARSET = new Param("charset", String.class, "charset", false, "UTF8");

    /**
     * Wheter a prepared statements based dialect should be used, or not
     */
    public static final Param PREPARED_STATEMENTS = new Param("preparedStatements", Boolean.class,
            "Use prepared statements", false, Boolean.FALSE);

    public static final Param U_XMIN_PARAM = new Param("tessellate_index_u_xmin", String.class,
            "tessellate_index_u_xmin", false, "-180");
    public static final Param U_YMIN_PARAM = new Param("tessellate_index_u_ymin", String.class,
            "tessellate_index_u_ymin", false, "-90");
    public static final Param U_XMAX_PARAM = new Param("tessellate_index_u_xmax", String.class,
            "tessellate_index_u_xmax", false, "180");
    public static final Param U_YMAX_PARAM = new Param("tessellate_index_u_ymax", String.class,
            "tessellate_index_u_ymax", false, "90");
    public static final Param G_NX_PARAM = new Param("tessellate_index_g_nx", String.class,
            "tessellate_index_g_nx", false, "1000");
    public static final Param G_NY_PARAM = new Param("tessellate_index_g_ny", String.class,
            "tessellate_index_g_ny", false, "1000");
    public static final Param LEVELS_PARAM = new Param("tessellate_index_levels", String.class,
            "tessellate_index_levels", false, "1");
    public static final Param SCALE_PARAM = new Param("tessellate_index_scale", String.class,
            "tessellate_index_scale", false, "0.01");
    public static final Param SHIFT_PARAM = new Param("tessellate_index_shift", String.class,
            "tessellate_index_shift", false, "0");

    public static final String U_XMIN = "org.geotools.data.teradata.tessellate_index_u_xmin";
    public static final String U_YMIN = "org.geotools.data.teradata.tessellate_index_u_ymin";
    public static final String U_XMAX = "org.geotools.data.teradata.tessellate_index_u_xmax";
    public static final String U_YMAX = "org.geotools.data.teradata.tessellate_index_u_ymax";
    public static final String G_NX = "org.geotools.data.teradata.tessellate_index_g_nx";
    public static final String G_NY = "org.geotools.data.teradata.tessellate_index_g_ny";
    public static final String LEVELS = "org.geotools.data.teradata.tessellate_index_levels";
    public static final String SCALE = "org.geotools.data.teradata.tessellate_index_scale";
    public static final String SHIFT = "org.geotools.data.teradata.tessellate_index_shift";

    // SET QUERY_BAND = 'ApplicationName=TZA-InsuranceService;
    // Version=01.00.00.00;' FOR Session;
    public static final Param QUERY_BANDING_SQL = new Param(
            "queryBandingSQL",
            String.class,
            "SQL to use Query Banding (example: \"SET QUERY_BAND = 'ApplicationName=TZA-InsuranceService; Version=01.00.00.00;' FOR Session;\")",
            false, "");

    private static final PrimaryKeyFinder KEY_FINDER = new CompositePrimaryKeyFinder(
            new MetadataTablePrimaryKeyFinder(), new TeradataPrimaryKeyFinder(),
            new HeuristicPrimaryKeyFinder());

    /**
     * parameter for database schema
     */
    // public static final Param SCHEMA = new Param("schema", String.class,
    // "Schema", false, "public");

    // TODO rest of parameters for connection (ACCOUNT, Charset, etc...)
    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new TeradataGISDialect(dataStore);
    }

    @Override
    public String getDatabaseID() {
        return (String) DBTYPE.sample;
    }

    @Override
    public String getDisplayName() {
        return "Teradata";
    }

    public String getDescription() {
        return "Teradata Database";
    }

    @Override
    protected String getDriverClassName() {
        return "com.teradata.jdbc.TeraDriver";
    }

    @Override
    protected boolean checkDBType(Map params) {
        return checkDBType(params, "teradata");
    }

    @Override
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, final Map params)
            throws IOException {

        // setup loose bbox
        TeradataGISDialect dialect = (TeradataGISDialect) dataStore.getSQLDialect();

        Boolean loose = (Boolean) LOOSEBBOX.lookUp(params);
        dialect.setLooseBBOXEnabled(loose == null || Boolean.TRUE.equals(loose));

        if (!params.containsKey(PK_METADATA_TABLE.key)) {
            dataStore.setPrimaryKeyFinder(KEY_FINDER);
        }

        if (params.containsKey(U_XMIN_PARAM.key)) {
            dialect.setU_xmin((Double) U_XMIN_PARAM.lookUp(params));
        }
        if (params.containsKey(U_YMIN_PARAM.key)) {
            dialect.setU_ymin((Double) U_YMIN_PARAM.lookUp(params));
        }
        if (params.containsKey(U_XMAX_PARAM.key)) {
            dialect.setU_xmax((Double) U_XMAX_PARAM.lookUp(params));
        }
        if (params.containsKey(U_YMAX_PARAM.key)) {
            dialect.setU_ymax((Double) U_YMAX_PARAM.lookUp(params));
        }
        if (params.containsKey(G_NX_PARAM.key)) {
            dialect.setG_nx((Integer) G_NX_PARAM.lookUp(params));
        }
        if (params.containsKey(G_NY_PARAM.key)) {
            dialect.setG_ny((Integer) G_NY_PARAM.lookUp(params));
        }
        if (params.containsKey(LEVELS_PARAM.key)) {
            dialect.setLevels((Integer) LEVELS_PARAM.lookUp(params));
        }
        if (params.containsKey(SCALE_PARAM.key)) {
            dialect.setScale((Double) SCALE_PARAM.lookUp(params));
        }
        if (params.containsKey(SHIFT_PARAM.key)) {
            dialect.setShift((Integer) SHIFT_PARAM.lookUp(params));
        }

        if (params.containsKey(QUERY_BANDING_SQL.key)) {
            dialect.setQueryBandingSql((String) QUERY_BANDING_SQL.lookUp(params));
        }

        // setup the ps dialect if need be
        Boolean usePs = (Boolean) PREPARED_STATEMENTS.lookUp(params);
        if (Boolean.TRUE.equals(usePs)) {
            dataStore.setSQLDialect(new TeradataPSDialect(dataStore, dialect));
        }


        dataStore.setFeatureTypeFactory(new FeatureTypeFactoryImpl() {
            private int getInteger(Param param, int defaultValue) {
                try {
                    return params.containsKey(param.key) ? (Integer) param.lookUp(params) : defaultValue;
                }
                catch (IOException e) {
                    e.printStackTrace();
                    return defaultValue;
                }
            }
            private double getDouble(Param param, double defaultValue) {
                try {
                    return params.containsKey(param.key) ? (Double) param.lookUp(params) : defaultValue;
                }
                catch (IOException e) {
                    e.printStackTrace();
                    return defaultValue;
                }
            }
            @Override
            public FeatureType createFeatureType(Name name, Collection schema,
                    GeometryDescriptor defaultGeometry, boolean isAbstract, List restrictions,
                    AttributeType superType, InternationalString description) {
                FeatureType type = super.createFeatureType(name, schema, defaultGeometry, isAbstract, restrictions, superType,
                        description);

                type.getUserData().put(U_XMIN, getDouble(U_XMIN_PARAM, -180));
                type.getUserData().put(U_YMIN, getDouble(U_YMIN_PARAM, -90));
                type.getUserData().put(U_XMAX, getDouble(U_XMAX_PARAM, 180));
                type.getUserData().put(U_YMAX, getDouble(U_YMAX_PARAM, 90));
                type.getUserData().put(G_NX, getInteger(G_NX_PARAM, 1000));
                type.getUserData().put(G_NY, getInteger(G_NY_PARAM, 1000));
                type.getUserData().put(LEVELS, getInteger(LEVELS_PARAM, 1));
                type.getUserData().put(SCALE, getDouble(SCALE_PARAM, 0.01));
                type.getUserData().put(SHIFT, getInteger(SHIFT_PARAM, 0));
                
                return type;
            }
        });
        
        return dataStore;
    }

    @Override
    protected void setupParameters(Map parameters) {
        // NOTE: when adding parameters here remember to add them to
        // TeradataJNDIDataStoreFactory

        super.setupParameters(parameters);
        parameters.put(DBTYPE.key, DBTYPE);
        // parameters.put(SCHEMA.key, SCHEMA);
        parameters.put(LOOSEBBOX.key, LOOSEBBOX);
        parameters.put(PORT.key, PORT);
        parameters.put(PREPARED_STATEMENTS.key, PREPARED_STATEMENTS);
        parameters.put(MAX_OPEN_PREPARED_STATEMENTS.key, MAX_OPEN_PREPARED_STATEMENTS);

        parameters.put(U_XMIN_PARAM.key, U_XMIN_PARAM);
        parameters.put(U_YMIN_PARAM.key, U_YMIN_PARAM);
        parameters.put(U_XMAX_PARAM.key, U_XMAX_PARAM);
        parameters.put(U_YMAX_PARAM.key, U_YMAX_PARAM);
        parameters.put(G_NX_PARAM.key, G_NX_PARAM);
        parameters.put(G_NY_PARAM.key, G_NY_PARAM);
        parameters.put(LEVELS_PARAM.key, LEVELS_PARAM);
        parameters.put(SCALE_PARAM.key, SCALE_PARAM);
        parameters.put(SHIFT_PARAM.key, SHIFT_PARAM);

        parameters.put(QUERY_BANDING_SQL.key, QUERY_BANDING_SQL);
    }

    @Override
    protected String getValidationQuery() {
        return "select now()";
    }

    @Override
    protected String getJDBCUrl(Map params) throws IOException {
        String host = (String) HOST.lookUp(params);
        String db = (String) DATABASE.lookUp(params);
        int port = (Integer) PORT.lookUp(params);
        String mode = (String) TMODE.lookUp(params);
        if (mode == null)
            mode = TMODE.sample.toString();
        String charset = (String) CHARSET.lookUp(params);
        if (charset == null)
            charset = CHARSET.sample.toString();
        return "jdbc:teradata://" + host + "/DATABASE=" + db + ",PORT=" + port + ",TMODE=" + mode
                + ",CHARSET=" + charset;
    }
    
}
