package org.geotools.geopkg;

import static java.lang.String.format;
import static org.geotools.geopkg.GeoPackage.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.geotools.geometry.jts.Geometries;
import org.geotools.geopkg.Entry;
import org.geotools.geopkg.FeatureEntry;
import org.geotools.geopkg.Entry.DataType;
import org.geotools.geopkg.GeoPackage;
import org.geotools.geopkg.geom.GeoPkgGeomReader;
import org.geotools.geopkg.geom.GeoPkgGeomWriter;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.PreparedStatementSQLDialect;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class GeoPkgDialect extends PreparedStatementSQLDialect {

    //GeoPackage geopkg;

    public GeoPkgDialect(JDBCDataStore dataStore) {
        super(dataStore);
        //this.geopkg = new GeoPackage(dataStore);
    }

    @Override
    public void initializeConnection(Connection cx) throws SQLException {
        new GeoPackage(dataStore.getDataSource()).init(cx);
    }

    @Override
    public boolean includeTable(String schemaName, String tableName, Connection cx) throws SQLException {
        Statement st = cx.createStatement();
        
        //PreparedStatement ps = cx.prepareStatement("SELECT * FROM geopackage_contents WHERE" +
        //    " table_name = ? AND data_type = ?");
        try {
            ResultSet rs = st.executeQuery(String.format("SELECT * FROM geopackage_contents WHERE" +
                " table_name = '%s' AND data_type = '%s'", tableName, DataType.Feature.value()));
            //ps.setString(1, tableName);
            //ps.setString(2, DataType.Feature.value());

            //ResultSet rs = ps.executeQuery();
            try {
                return rs.next();
            }
            finally {
                rs.close();
            }
        }
        finally {
            //dataStore.closeSafe(ps);
            dataStore.closeSafe(st);
        }
    }

    @Override
    public void encodeGeometryEnvelope(String tableName, String geometryColumn, StringBuffer sql) {
        encodeColumnName(null, geometryColumn, sql);
    }
    
    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column, Connection cx)
        throws SQLException, IOException {
        Geometry g = geometry(rs.getBytes(column));
        return g != null ? g.getEnvelopeInternal() : null;
    }

    @Override
    public Geometry decodeGeometryValue(GeometryDescriptor descriptor, ResultSet rs, String column, 
        GeometryFactory factory, Connection cx) throws IOException, SQLException {
        return geometry(rs.getBytes(column));
    }

    @Override
    public void setGeometryValue(Geometry g, int srid, Class binding,
            PreparedStatement ps, int column) throws SQLException {
        if (g == null) {
            ps.setNull(1, Types.BLOB);
        }
        else {
            try {
                ps.setBytes(column, new GeoPkgGeomWriter().write(g));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    Geometry geometry(byte[] b) throws IOException {
        return b != null ? new GeoPkgGeomReader().read(b) : null;
    }

    @Override
    public String getGeometryTypeName(Integer type) {
        return "BLOB";
    }

    @Override
    public void registerSqlTypeNameToClassMappings( Map<String, Class<?>> mappings) {
        super.registerSqlTypeNameToClassMappings(mappings);
        mappings.put("DOUBLE", Double.class);
    }

    @Override
    public void registerClassToSqlMappings(Map<Class<?>, Integer> mappings) {
        super.registerClassToSqlMappings(mappings);

        for (Geometries g : Geometries.values()) {
            mappings.put(g.getBinding(), Types.BLOB);
        }

        //override some internal defaults
        mappings.put(Long.class, Types.INTEGER);
        mappings.put(Double.class, Types.REAL);
    }

    @Override
    public Class<?> getMapping(ResultSet columns, Connection cx) throws SQLException {
        int type = columns.getInt("DATA_TYPE");

        //sqlite seems to map blobs to varchar 
        if (type == Types.VARCHAR) {
            String tbl = columns.getString("TABLE_NAME");
            String col = columns.getString("COLUMN_NAME"); 

            String sql = format(
                "SELECT b.geometry_type" +
                 " FROM %s a, %s b" + 
                " WHERE a.table_name = b.f_table_name" +
                  " AND b.f_table_name = ?" + 
                  " AND b.f_geometry_column = ?", GEOPACKAGE_CONTENTS, GEOMETRY_COLUMNS);

            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(String.format("%s; 1=%s, 2=%s", sql, tbl, col));
            }

            PreparedStatement ps = cx.prepareStatement(sql);
            try {
                ps.setString(1, tbl);
                ps.setString(2, col);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String t = rs.getString(1);
                    Geometries g = Geometries.getForName(t);
                    if (g != null) {
                        return g.getBinding();
                    }
                }
                
                rs.close();
            }
            finally {
                dataStore.closeSafe(ps);
            }
        }
        
        return null;
    }

    @Override
    public void postCreateTable(String schemaName, SimpleFeatureType featureType, Connection cx) 
        throws SQLException, IOException {
     
        FeatureEntry fe = (FeatureEntry) featureType.getUserData().get(FeatureEntry.class);
        if (fe == null) {
            fe = new FeatureEntry();
            fe.setIdentifier(featureType.getTypeName());
            fe.setDescription(featureType.getTypeName());
            fe.setTableName(featureType.getTypeName());
            fe.setLastChange(new Date());
        }
        
        GeometryDescriptor gd = featureType.getGeometryDescriptor(); 
        if (gd != null) {
            fe.setGeometryColumn(gd.getLocalName());
            fe.setGeometryType(Geometries.getForBinding((Class) gd.getType().getBinding()));
        }

        fe.setCoordDimension(2);
        CoordinateReferenceSystem crs = featureType.getCoordinateReferenceSystem(); 
        if (crs != null) {
            Integer epsgCode = null;
            try {
                epsgCode = CRS.lookupEpsgCode(crs, true);
            } catch (FactoryException e) {
                LOGGER.log(Level.WARNING, "Error looking up epsg code for " + crs, e);
            }
            if (epsgCode != null) {
                fe.setSrid(epsgCode);
            }
        }

        GeoPackage geopkg = geopkg();
        try {
            geopkg.addGeoPackageContentsEntry(fe);
            geopkg.addGeometryColumnsEntry(fe);
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    public Integer getGeometrySRID(String schemaName, String tableName, String columnName, Connection cx) throws SQLException {
        try {
            FeatureEntry fe = geopkg().feature(tableName);
            return fe != null ? fe.getSrid() : null;
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    public CoordinateReferenceSystem createCRS(int srid, Connection cx) throws SQLException {
        try {
            return CRS.decode("EPSG:" + srid);
        }
        catch (Exception e) {
            LOGGER.log(Level.FINE, "Unable to create CRS from epsg code " + srid, e);
            
            //try looking up in spatial ref sys
            String sql = 
                String.format("SELECT srtext FROM %s WHERE auth_srid = %d", SPATIAL_REF_SYS, srid);
            LOGGER.fine(sql);

            Statement st = cx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            try {
                if (rs.next()){
                    String wkt = rs.getString(1);
                    try {
                        return CRS.parseWKT(wkt);
                    } catch (Exception e2) {
                        LOGGER.log(Level.FINE, "Unable to create CRS from wkt: " + wkt, e2);
                    }
                }
            }
            finally {
                dataStore.closeSafe(rs);
                dataStore.closeSafe(st);
            }
        }

        return super.createCRS(srid, cx);
    }

    GeoPackage geopkg() {
        return new GeoPackage(dataStore);
    }
}
