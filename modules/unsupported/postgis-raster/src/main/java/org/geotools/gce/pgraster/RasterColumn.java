/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.pgraster;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.gce.pgraster.PGRasterConfig.TimeConfig;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A raster column of a table in a postgis raster database, obtained from the obtained from the
 * <code>raster_columns</code> view.
 */
class RasterColumn {

    static final Logger LOG = Logging.getLogger(RasterColumn.class);

    static RasterColumn lookup(PGRasterConfig config) throws SQLException {

        try (Connection cx = config.dataSource.getConnection()) {

            // load the column
            SQL sql =
                    new SQL()
                            .append(
                                    "SELECT r_table_schema, r_table_name, r_raster_column, srid, num_bands, ")
                            .append("ST_AsText(extent) as extent, scale_x, scale_y")
                            .append(" FROM raster_columns")
                            .append(" WHERE r_table_name = ?");

            if (config.schema != null) {
                sql.append(" AND r_table_schema = ?");
            }
            if (config.column != null) {
                sql.append(" AND r_raster_column = ?");
            }

            RasterColumn col = null;
            try (PreparedStatement st = cx.prepareStatement(sql.logAndGet(LOG))) {
                st.setString(1, config.table);
                if (config.schema != null) {
                    st.setString(2, config.schema);
                }
                if (config.column != null) {
                    st.setString(2 + (config.schema == null ? 0 : 1), config.column);
                }

                try (ResultSet rc = st.executeQuery()) {
                    while (rc.next()) {
                        if (col != null)
                            throw new IllegalArgumentException(
                                    "Multiple raster columns found for table '"
                                            + col.tableKey()
                                            + "', please specify one in config");

                        col = new RasterColumn(rc);

                        // srid
                        int srid = rc.getInt("srid");
                        if (srid == 0) {
                            // grab it from the actual table
                            srid = fetchSrid(col, cx);
                        }
                        if (srid != 0) {
                            col.srid = srid;
                            try {
                                col.crs = CRS.decode("EPSG:" + srid);
                            } catch (Exception e) {
                                LOG.log(
                                        Level.WARNING,
                                        "Error looking up SRS for srid = " + srid,
                                        e);
                            }
                        }
                        if (col.crs == null) {
                            // TODO: look up wkt
                            LOG.warning("Unable to determine crs for raster column: " + col.key());
                        }

                        // extent
                        String extent = rc.getString("extent");
                        if (extent == null) {
                            // grab it from the table
                            extent = fetchExtent(col, cx);
                        }
                        if (extent != null) {
                            col.extent = parseExtent(extent);
                        }

                        if (col.extent == null) {
                            LOG.warning(
                                    "Unable to determine extent for raster column: " + col.key());
                        }

                        // scale
                        Point2D.Double scale =
                                new Point2D.Double(
                                        rc.getDouble("scale_x"), rc.getDouble("scale_y"));

                        if (scale.x == 0d || scale.y == 0d) {
                            scale = fetchScale(col, cx);
                        }

                        if (scale != null && scale.x > 0d) {
                            col.scale = scale;
                            col.scale.y = Math.abs(col.scale.y);
                        }

                        if (col.scale == null) {
                            LOG.warning(
                                    "Unable to determine scale for raster column: " + col.key());
                        }

                        if (col.extent != null && col.scale != null) {
                            col.size =
                                    new Dimension(
                                            (int) (col.extent.getWidth() / col.scale.x),
                                            (int) (col.extent.getHeight() / col.scale.y));
                        }

                        // num bands
                        int numBands = rc.getInt("num_bands");
                        if (numBands == 0) {
                            numBands = fetchNumBands(col, cx);
                        }
                        if (numBands > 0) {
                            col.numBands = numBands;
                        }

                        // time?
                        if (config.time.enabled) {
                            TimeColumn time = fetchTime(col, config.time, cx);
                            if (time != null) {
                                col.time = time;
                            }
                        }
                    }
                }

                if (col == null) {
                    String dump =
                            "schema = "
                                    + config.schema
                                    + ", table = "
                                    + config.table
                                    + ", column = "
                                    + config.column;
                    throw new IllegalArgumentException(
                            "No raster column found for config: " + dump);
                }
            }

            // load it's overviews
            sql =
                    new SQL()
                            .append("SELECT * FROM raster_overviews")
                            .append(" WHERE r_table_name = ?")
                            .append(" AND r_table_schema = ?")
                            .append(" AND r_raster_column = ?")
                            .append(" ORDER BY overview_factor ASC");

            try (PreparedStatement ps = cx.prepareCall(sql.logAndGet(LOG))) {
                ps.setString(1, col.table);
                ps.setString(2, col.schema);
                ps.setString(3, col.name);

                try (ResultSet ov = ps.executeQuery()) {
                    while (ov.next()) {
                        RasterOverview overview = new RasterOverview(ov);
                        overview.extent = fetchAndParseExtent(overview, cx);
                        col.overview(overview);
                    }
                }
            }

            return col;
        }
    }

    static int fetchSrid(RasterColumn col, Connection cx) throws SQLException {
        SQL sql =
                new SQL()
                        .append("SELECT srid FROM (")
                        .append("SELECT DISTINCT st_srid(")
                        .name(col.name)
                        .append(") as srid")
                        .append(" FROM ")
                        .table(col)
                        .append(") AS a WHERE srid IS NOT NULL");

        try (Statement st = cx.createStatement()) {
            try (ResultSet rs = st.executeQuery(sql.logAndGet(LOG))) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    static String fetchExtent(RasterColumn col, Connection cx) throws SQLException {

        SQL sql =
                new SQL()
                        .append("SELECT ST_AsText(ST_Extent(ST_Envelope(")
                        .name(col.name)
                        .append("::geometry))) AS extent")
                        .append(" FROM ")
                        .table(col);

        try (Statement st = cx.createStatement()) {
            try (ResultSet rs = st.executeQuery(sql.logAndGet(LOG))) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }

        return null;
    }

    static Envelope parseExtent(String extent) {
        try {
            return new WKTReader().read(extent).getEnvelopeInternal();
        } catch (ParseException e) {
            LOG.log(Level.WARNING, "Error parsing extent", e);
        }
        return null;
    }

    static Envelope fetchAndParseExtent(RasterColumn col, Connection cx) throws SQLException {
        String e = fetchExtent(col, cx);
        if (e != null) {
            return parseExtent(e);
        }
        return null;
    }

    static TimeColumn fetchTime(RasterColumn col, TimeConfig time, Connection cx)
            throws SQLException {
        SQL sql =
                new SQL()
                        .append("SELECT column_name, data_type")
                        .append(" FROM ")
                        .append("information_schema.columns")
                        .append(" WHERE table_schema = ? AND table_name = ?")
                        .append(" AND (")
                        .append(" data_type = 'date' OR data_type LIKE 'timestamp %'")
                        .append(" )");

        if (time.column != null) {
            sql.append(" AND column_name = ?");
        }

        try (PreparedStatement ps = cx.prepareStatement(sql.logAndGet(LOG))) {
            ps.setString(1, col.schema);
            ps.setString(2, col.table);
            if (time.column != null) {
                ps.setString(3, time.column);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TimeColumn t = new TimeColumn(rs);
                    if (rs.next()) {
                        LOG.warning("Multiple time columns found for table " + col.tableKey());
                        return null;
                    }

                    return t;
                } else {
                    return null;
                }
            }
        }
    }

    static Point2D.Double fetchScale(RasterColumn col, Connection cx) throws SQLException {
        SQL sql =
                new SQL()
                        .append("SELECT ST_ScaleX(")
                        .name(col.name)
                        .append(") as scale_x,")
                        .append(" ST_ScaleY(")
                        .name(col.name)
                        .append(") as scale_y")
                        .append(" FROM ")
                        .table(col)
                        .append(" LIMIT 1");

        try (Statement st = cx.createStatement()) {
            try (ResultSet rs = st.executeQuery(sql.logAndGet(LOG))) {
                if (rs.next()) {
                    return new Point2D.Double(rs.getDouble(1), rs.getDouble(2));
                }
            }
        }

        return null;
    }

    static int fetchNumBands(RasterColumn col, Connection cx) throws SQLException {
        SQL sql =
                new SQL()
                        .append("SELECT max(ST_NumBands(")
                        .name(col.name)
                        .append(")) as num_bands")
                        .append(" FROM ")
                        .table(col)
                        .append(" LIMIT 1");

        try (Statement st = cx.createStatement()) {
            try (ResultSet rs = st.executeQuery(sql.logAndGet(LOG))) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return 0;
    }

    final String name;
    final String table;
    final String schema;
    final List<RasterOverview> overviews = new ArrayList<>();

    Integer srid;
    CoordinateReferenceSystem crs;
    Envelope extent;
    Point2D.Double scale;
    Dimension size;
    Integer numBands;

    TimeColumn time;

    RasterColumn(String name, String table, String schema) {
        this.name = name;
        this.table = table;
        this.schema = schema;
    }

    RasterColumn(ResultSet rs) throws SQLException {
        this(
                rs.getString("r_raster_column"),
                rs.getString("r_table_name"),
                rs.getString("r_table_schema"));
    }

    String key() {
        return schema + "." + table + "." + name;
    }

    String tableKey() {
        return schema + "." + table;
    }

    ReferencedEnvelope bounds() {
        return extent != null ? new ReferencedEnvelope(extent, crs) : null;
    }

    void overview(RasterOverview ov) {
        ov.srid = srid;
        ov.time = time;

        if (ov.extent == null) {
            ov.extent = extent;
        }

        if (scale != null) {
            ov.scale = new Point2D.Double(scale.x * ov.factor, scale.y * ov.factor);
            if (size != null) {
                ov.size = new Dimension(size.width / ov.factor, size.height / ov.factor);
            }
        }

        ov.numBands = numBands;
        overviews.add(ov);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RasterColumn that = (RasterColumn) o;
        return Objects.equals(name, that.name)
                && Objects.equals(table, that.table)
                && Objects.equals(schema, that.schema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, table, schema);
    }
}
