/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.logging.Level;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.locationtech.jts.io.WKBWriter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * JDBCAccess Implentation for DB2 Spatial Extended
 *
 * @author mcr
 */
class JDBCAccessDB2 extends JDBCAccessBase {
    static String SRSSelect =
            "select srs_id,srs_name from db2gse.st_geometry_columns where table_schema=? and table_name=? and column_name=? ";

    static String SRSSelectCurrentSchema =
            "select srs_id,srs_name from db2gse.st_geometry_columns where table_schema=(select current schema from sysibm.sysdummy1) and table_name=? and column_name=? ";

    static String CRSSelect =
            "select definition from db2gse.st_spatial_reference_systems where srs_id=?";

    private String extentSelect = null;

    private String allSelect = null;

    private String allSelectJoined = null;

    private String gridSelect = null;

    private String gridSelectJoined = null;

    JDBCAccessDB2(Config config) throws IOException {
        super(config);
        initStatementStrings(config);
    }

    /** initialize the sql statement strings */
    private void initStatementStrings(Config config) {
        String geomAttr = config.getGeomAttributeNameInSpatialTable();
        extentSelect =
                "select "
                        + "min(db2gse.st_minx("
                        + geomAttr
                        + ")), "
                        + "min(db2gse.st_miny("
                        + geomAttr
                        + ")), "
                        + "max(db2gse.st_maxx("
                        + geomAttr
                        + ")), "
                        + "max(db2gse.st_maxy("
                        + geomAttr
                        + ")) "
                        + " from {0}";

        String spatialSelectClause =
                "select s."
                        + config.getKeyAttributeNameInSpatialTable()
                        + ","
                        + "db2gse.st_minx(s."
                        + geomAttr
                        + "), "
                        + "db2gse.st_miny(s."
                        + geomAttr
                        + "), "
                        + "db2gse.st_maxx(s."
                        + geomAttr
                        + "), "
                        + "db2gse.st_maxy(s."
                        + geomAttr
                        + ") ";

        allSelect =
                spatialSelectClause
                        + ",s."
                        + config.getBlobAttributeNameInTileTable()
                        + " from {0} s";
        allSelectJoined =
                spatialSelectClause
                        + ",t."
                        + config.getBlobAttributeNameInTileTable()
                        + " from {0} s, {1} t  WHERE ";
        allSelectJoined +=
                (" s."
                        + config.getKeyAttributeNameInSpatialTable()
                        + " = t."
                        + config.getKeyAttributeNameInTileTable());

        String whereClause =
                "db2gse.st_mbrIntersects(s."
                        + geomAttr
                        + ","
                        + "db2gse.ST_GEOMETRY(CAST(? AS BLOB(128)),CAST(? AS INT))) = 1 SELECTIVITY 0.0000001";

        gridSelect = allSelect + " WHERE " + whereClause;
        gridSelectJoined = allSelectJoined + " AND " + whereClause;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.gce.imagemosaic.jdbc.JDBCAccessBase#getRandomTileStatement(org.geotools.gce.imagemosaic.jdbc.ImageLevelInfo)
     */
    @Override
    protected String getRandomTileStatement(ImageLevelInfo li) {
        if (li.isImplementedAsTableSplit()) {
            return MessageFormat.format(
                    allSelectJoined,
                    new Object[] {li.getSpatialTableName(), li.getTileTableName()});
        } else {
            return MessageFormat.format(allSelect, new Object[] {li.getSpatialTableName()});
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.gce.imagemosaic.jdbc.JDBCAccessBase#getGridSelectStatement(org.geotools.gce.imagemosaic.jdbc.ImageLevelInfo)
     */
    @Override
    protected String getGridSelectStatement(ImageLevelInfo li) {
        if (li.isImplementedAsTableSplit()) {
            return MessageFormat.format(
                    gridSelectJoined,
                    new Object[] {li.getSpatialTableName(), li.getTileTableName()});
        } else {
            return MessageFormat.format(gridSelect, new Object[] {li.getSpatialTableName()});
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.gce.imagemosaic.jdbc.JDBCAccessBase#getExtentSelectStatment(org.geotools.gce.imagemosaic.jdbc.ImageLevelInfo)
     */
    @Override
    protected String getExtentSelectStatment(ImageLevelInfo li) {
        return MessageFormat.format(extentSelect, new Object[] {li.getSpatialTableName()});
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.gce.imagemosaic.jdbc.JDBCAccessBase#getSRSID(org.geotools.gce.imagemosaic.jdbc.ImageLevelInfo,
     *      java.sql.Connection)
     */
    @Override
    protected Integer getSRSID(ImageLevelInfo li, Connection con) throws IOException {
        Integer result = null;
        String schema = null;

        try {
            schema = getSchemaFromSpatialTable(li.getSpatialTableName());

            PreparedStatement s = null;
            try {
                if (schema == null) {
                    s = con.prepareStatement(SRSSelectCurrentSchema);
                    s.setString(1, li.getSpatialTableName());
                    s.setString(2, config.getGeomAttributeNameInSpatialTable());
                } else {
                    s = con.prepareStatement(SRSSelect);
                    s.setString(1, schema);
                    s.setString(2, li.getSpatialTableName());
                    s.setString(3, config.getGeomAttributeNameInSpatialTable());
                }

                try (ResultSet r = s.executeQuery()) {
                    if (r.next()) {
                        result = (Integer) r.getObject(1);
                    }
                }
            } finally {
                if (s != null) s.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new IOException(e);
        }

        if (result == null) {
            String msg =
                    MessageFormat.format(
                            "No entry in db2gse.st_geometry_columns for {0},{1},{2}",
                            new Object[] {
                                (schema != null) ? schema : "currrent schema",
                                li.getSpatialTableName(),
                                config.getGeomAttributeNameInSpatialTable()
                            });
            LOGGER.log(Level.SEVERE, msg);
            throw new IOException(msg);
        }

        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.gce.imagemosaic.jdbc.JDBCAccessBase#getCRS(org.geotools.gce.imagemosaic.jdbc.ImageLevelInfo,
     *      java.sql.Connection)
     */
    @Override
    protected CoordinateReferenceSystem getCRS(ImageLevelInfo li, Connection con)
            throws IOException {
        CoordinateReferenceSystem result = null;

        try (PreparedStatement s = con.prepareStatement(CRSSelect)) {
            s.setInt(1, li.getSrsId());

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    String definition = r.getString(1);
                    result = CRS.parseWKT(definition);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new IOException(e);
        }

        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.gce.imagemosaic.jdbc.JDBCAccessBase#setGridSelectParams(java.sql.PreparedStatement,
     *      org.geotools.geometry.GeneralEnvelope,
     *      org.geotools.gce.imagemosaic.jdbc.ImageLevelInfo)
     */
    @Override
    protected void setGridSelectParams(
            PreparedStatement s, GeneralEnvelope envelope, ImageLevelInfo li) throws SQLException {
        WKBWriter w = new WKBWriter();
        byte[] bytes = w.write(polyFromEnvelope(envelope));
        s.setBytes(1, bytes);
        s.setInt(2, li.getSrsId());
    }
}
