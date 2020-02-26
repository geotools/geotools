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
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKBWriter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Implementation of JDBCAccess for MySql database
 *
 * @author mcr
 */
class JDBCAccessMySql extends JDBCAccessBase {
    private String extentSelect = null;

    private String allSelect = null;

    private String allSelectJoined = null;

    private String gridSelect = null;

    private String gridSelectJoined = null;

    JDBCAccessMySql(Config config) throws IOException {
        super(config);
        initStatementStrings(config);
    }

    /** Initialze needed sql statement strings */
    private void initStatementStrings(Config config) {
        String geomAttr = config.getGeomAttributeNameInSpatialTable();
        extentSelect = "select asbinary(envelope(" + geomAttr + ")) from {0}";

        String spatialSelectClause =
                "select s."
                        + config.getKeyAttributeNameInSpatialTable()
                        + ","
                        + "asbinary(envelope(s."
                        + geomAttr
                        + "))";

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

        String whereClause = "mbrIntersects(" + geomAttr + "," + "GeomFromWKB(?)) = 1";

        gridSelect = allSelect + " WHERE " + whereClause;
        gridSelectJoined = allSelectJoined + " AND " + whereClause;
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

        // s.setInt(2, li.getSrsId()); not supported
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
     * @see org.geotools.gce.imagemosaic.jdbc.JDBCAccessBase#getExtent(org.geotools.gce.imagemosaic.jdbc.ImageLevelInfo,
     *      java.sql.Connection)
     */
    @Override
    protected Envelope getExtent(ImageLevelInfo li, Connection con)
            throws SQLException, IOException {
        String extentSelect = getExtentSelectStatment(li);

        String statementString =
                MessageFormat.format(extentSelect, new Object[] {li.getSpatialTableName()});
        Envelope extent = null;
        try (PreparedStatement s = con.prepareStatement(statementString);
                ResultSet r = s.executeQuery()) {

            WKBReader reader = new WKBReader();

            while (r.next()) {
                byte[] bytes = r.getBytes(1);
                Geometry g;

                try {
                    g = reader.read(bytes);
                } catch (ParseException e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                    throw new IOException(e);
                }

                if (extent == null) {
                    extent = g.getEnvelopeInternal();
                } else {
                    extent.expandToInclude(g.getEnvelopeInternal());
                }
            }
        }

        return extent;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.gce.imagemosaic.jdbc.JDBCAccessBase#getEnvelopeFromResultSet(java.sql.ResultSet)
     */
    @Override
    protected Envelope getEnvelopeFromResultSet(ResultSet r) throws SQLException {
        byte[] bytes = r.getBytes(2);
        WKBReader reader = new WKBReader();
        Geometry bbox = null;

        try {
            bbox = reader.read(bytes);
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new SQLException(e.getMessage());
        }

        return bbox.getEnvelopeInternal();
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
        return null;
    }
}
