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
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKBWriter;

/**
 * JDBCAccess implentation for PostGis
 * 
 * @author mcr
 * 
 */
class JDBCAccessPostGis extends JDBCAccessBase {
    static String SRSSelect = "select srid from geometry_columns where f_table_schema=? and f_table_name=? and f_geometry_column=? ";

    static String CRSSelect = "select srtext from spatial_ref_sys where srid=?";

    private String extentSelect = null;

    private String allSelect = null;

    private String allSelectJoined = null;

    private String gridSelect = null;

    private String gridSelectJoined = null;

    // Since PostGis 1.2.2
    private String functionPrefix = "st_";

    JDBCAccessPostGis(Config config) throws IOException {
        super(config);
        initForPostGisVersion();
        initStatementStrings(config);
    }

    /**
     * Check if we have an instance of PostGis that uses the "st_" prefix for 
     * functions or not.
     * Eg: the extent function is replaced PostGis by st_extent.
     * The prefix is added since version 1.2.2
     */
    private void initForPostGisVersion() {
        String extentFunctionName="st_extent";
        Connection con=null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("select proname  from pg_proc where proname='"+extentFunctionName+"'");
            rs = ps.executeQuery();
        
            if (rs.next()==false) { 
                functionPrefix="";
                LOGGER.info("Using depricated postgis functions eg 'extent' instead of 'st_extent'");
            }               
        } catch (SQLException ex) {
            LOGGER.log(Level.WARNING, "could not verify existence of postgis function 'st_extent', falling back to depricated 'extent'");
            functionPrefix="";
        }
        finally {
            try { if (rs!=null) rs.close(); } catch (SQLException ex) {};
            try { if (ps!=null) ps.close(); } catch (SQLException ex) {};
            try { if (con!=null) ps.close(); } catch (SQLException ex) {};
                
        }
        
    }
    
    /**
     * Initialize needed sql statement strings
     * 
     * @param config
     */
    private void initStatementStrings(Config config) {
        final String geomAttr = config.getGeomAttributeNameInSpatialTable();        
        extentSelect = "select "+ functionPrefix +"extent(" + geomAttr + ") from {0}";

        // ////////////
        final String spatialSelectClause = "select s." + config.getKeyAttributeNameInSpatialTable()
                + "," + "asbinary(" + functionPrefix + "envelope(s." + geomAttr + "))";

        allSelect = spatialSelectClause + ",s." + config.getBlobAttributeNameInTileTable()
                + " from {0} s";
        allSelectJoined = spatialSelectClause + ",t." + config.getBlobAttributeNameInTileTable()
                + " from {0} s, {1} t  WHERE ";
        allSelectJoined += (" s." + config.getKeyAttributeNameInSpatialTable() + " = t." + config
                .getKeyAttributeNameInTileTable());

        String whereClause = functionPrefix + "intersects(" + geomAttr + "," + "GeomFromWKB(?,?))";

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
        return MessageFormat.format(extentSelect, new Object[] { li.getSpatialTableName() });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geotools.gce.imagemosaic.jdbc.JDBCAccessBase#getExtent(org.geotools.gce.imagemosaic.jdbc.ImageLevelInfo,
     *      java.sql.Connection)
     */
    @Override
    protected Envelope getExtent(ImageLevelInfo li, Connection con) throws SQLException,
            IOException {
        String extentSelect = getExtentSelectStatment(li);

        String statementString = MessageFormat.format(extentSelect, new Object[] { li
                .getSpatialTableName() });
        Envelope extent = null;
        PreparedStatement s = con.prepareStatement(statementString);
        ResultSet r = s.executeQuery();

        if (r.next()) {
            Object o = r.getObject(1);
            String pgString = o.toString();

            int start = pgString.indexOf("(") + 1;
            int end = pgString.indexOf(")");
            pgString = pgString.substring(start, end);

            String[] coords = pgString.split(",");
            String[] lower = coords[0].split(" ");
            String[] upper = coords[1].split(" ");
            extent = new Envelope(new Coordinate(new Double(lower[0]), new Double(lower[1])),
                    new Coordinate(new Double(upper[0]), new Double(upper[1])));
        }

        r.close();
        s.close();

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
     * @see org.geotools.gce.imagemosaic.jdbc.JDBCAccessBase#getRandomTileStatement(org.geotools.gce.imagemosaic.jdbc.ImageLevelInfo)
     */
    @Override
    protected String getRandomTileStatement(ImageLevelInfo li) {
        if (li.isImplementedAsTableSplit()) {
            return MessageFormat.format(allSelectJoined, new Object[] { li.getSpatialTableName(),
                    li.getTileTableName() });
        } else {
            return MessageFormat.format(allSelect, new Object[] { li.getSpatialTableName() });
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
            return MessageFormat.format(gridSelectJoined, new Object[] { li.getSpatialTableName(),
                    li.getTileTableName() });
        } else {
            return MessageFormat.format(gridSelect, new Object[] { li.getSpatialTableName() });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geotools.gce.imagemosaic.jdbc.JDBCAccessBase#setGridSelectParams(java.sql.PreparedStatement,
     *      org.geotools.geometry.GeneralEnvelope, org.geotools.gce.imagemosaic.jdbc.ImageLevelInfo)
     */
    @Override
    protected void setGridSelectParams(PreparedStatement s, GeneralEnvelope envelope,
            ImageLevelInfo li) throws SQLException {
        WKBWriter w = new WKBWriter();
        byte[] bytes = w.write(polyFromEnvelope(envelope));
        s.setBytes(1, bytes);
        s.setInt(2, li.getSrsId());
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

            if (schema == null) {
                schema = "public";
            }

            s = con.prepareStatement(SRSSelect);
            s.setString(1, schema);
            s.setString(2, li.getSpatialTableName());
            s.setString(3, config.getGeomAttributeNameInSpatialTable());

            ResultSet r = s.executeQuery();

            if (r.next()) {
                result = (Integer) r.getObject(1);
            }

            r.close();
            s.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new IOException(e.getMessage());
        }

        if (result == null) {
            String msg = MessageFormat.format("No entry in geometry_columns for {0},{1},{2}",
                    new Object[] { schema, li.getSpatialTableName(),
                            config.getGeomAttributeNameInSpatialTable() });
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

        try {
            PreparedStatement s = con.prepareStatement(CRSSelect);
            s.setInt(1, li.getSrsId());

            ResultSet r = s.executeQuery();

            if (r.next()) {
                String definition = r.getString(1);
                result = CRS.parseWKT(definition);
            }

            r.close();
            s.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new IOException(e.getMessage());
        }

        return result;
    }
}
