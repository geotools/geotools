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
import java.sql.SQLException;
import java.text.MessageFormat;
import org.geotools.geometry.GeneralEnvelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * JDBC Access implementation for a sql db without a spatial extension
 *
 * @author mcr
 */
class JDBCAccessUniversal extends JDBCAccessBase {
    private String extentSelect = null;

    private String allSelect = null;

    private String allSelectJoined = null;

    private String gridSelect = null;

    private String gridSelectJoined = null;

    JDBCAccessUniversal(Config config) throws IOException {
        super(config);
        initStatementStrings(config);
    }

    /** Initalize needed sql statement strings */
    private void initStatementStrings(Config config) {
        extentSelect = "SELECT ";
        extentSelect += ("min(" + config.getTileMinXAttribute() + "),");
        extentSelect += ("min(" + config.getTileMinYAttribute() + "),");
        extentSelect += ("max(" + config.getTileMaxXAttribute() + "),");
        extentSelect += ("max(" + config.getTileMaxYAttribute() + ")");
        extentSelect += " FROM {0}";

        String spatialSelectClause = "SELECT ";
        spatialSelectClause += ("s." + config.getKeyAttributeNameInSpatialTable() + ",");
        spatialSelectClause += ("s." + config.getTileMinXAttribute() + ",");
        spatialSelectClause += ("s." + config.getTileMinYAttribute() + ",");
        spatialSelectClause += ("s." + config.getTileMaxXAttribute() + ",");
        spatialSelectClause += ("s." + config.getTileMaxYAttribute());

        allSelect =
                spatialSelectClause
                        + ",s."
                        + config.getBlobAttributeNameInTileTable()
                        + " FROM {0} s";
        allSelectJoined =
                spatialSelectClause
                        + ",t."
                        + config.getBlobAttributeNameInTileTable()
                        + " FROM {0} s, {1} t WHERE ";
        allSelectJoined +=
                (" s."
                        + config.getKeyAttributeNameInSpatialTable()
                        + " = t."
                        + config.getKeyAttributeNameInTileTable());

        // ////////////
        // return (xmax > xmin0 &&
        // ymax > ymin0 &&
        // xmin < xmax0 &&
        // ymin < ymax0 );
        String whereClause = "";
        whereClause += (" ? > s." + config.getTileMinXAttribute() + " AND "); // param
        // xmax

        whereClause += (" ? > s." + config.getTileMinYAttribute() + " AND "); // param
        // ymax

        whereClause += (" ? < s." + config.getTileMaxXAttribute() + " AND "); // param
        // xmin

        whereClause += (" ? < s." + config.getTileMaxYAttribute()); // param
        // ymin

        gridSelect = allSelect + " WHERE " + whereClause;
        gridSelectJoined = allSelectJoined + " AND " + whereClause;
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
     * @see org.geotools.gce.imagemosaic.jdbc.JDBCAccessBase#setGridSelectParams(java.sql.PreparedStatement,
     *      org.geotools.geometry.GeneralEnvelope,
     *      org.geotools.gce.imagemosaic.jdbc.ImageLevelInfo)
     */
    @Override
    protected void setGridSelectParams(
            PreparedStatement s, GeneralEnvelope envelope, ImageLevelInfo li) throws SQLException {
        s.setDouble(1, envelope.getMaximum(0));
        s.setDouble(2, envelope.getMaximum(1));
        s.setDouble(3, envelope.getMinimum(0));
        s.setDouble(4, envelope.getMinimum(1));
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
