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

import com.google.common.base.Throwables;
import com.google.common.collect.AbstractIterator;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.imageio.ImageReadParam;
import javax.media.jai.Interpolation;
import org.geotools.api.coverage.grid.Format;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.CRS;
import org.geotools.util.Converters;
import org.geotools.util.DateRange;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKBWriter;

/** PostGIS raster reader. */
public class PGRasterReader extends AbstractGridCoverage2DReader {

    static final Logger LOG = Logging.getLogger(PGRasterReader.class);
    static final GeometryFactory GEOMS = new GeometryFactory();

    final PGRasterConfig config;
    final PGRasterFormat format;
    final RasterColumn raster;

    ExecutorService exec;

    public PGRasterReader(PGRasterConfig config, PGRasterFormat format, Hints hints) throws IOException {
        super(config, hints);

        this.config = config;
        this.format = format;

        try {
            raster = RasterColumn.lookup(config);
        } catch (SQLException e) {
            throw new IOException("Error looking up raster column", e);
        }

        // the (possibly mapped) coverage name
        coverageName = config.name != null ? config.name : raster.table;

        // crs and bounds
        crs = raster.crs;
        originalEnvelope = GeneralBounds.toGeneralEnvelope(raster.bounds());
        if (raster.scale != null) {
            originalGridRange =
                    new GridEnvelope2D(new Rectangle((int) (originalEnvelope.getSpan(0) / raster.scale.x), (int)
                            (originalEnvelope.getSpan(1) / raster.scale.y)));
        }

        if (raster.scale != null) {
            highestRes = new double[] {raster.scale.x, raster.scale.y};
        }

        // overviews
        numOverviews = raster.overviews.size();
        if (numOverviews > 0) {
            if (raster.scale != null) {
                overViewResolutions = new double[numOverviews][2];
                for (int i = 0; i < numOverviews; i++) {
                    RasterOverview ov = raster.overviews.get(i);
                    overViewResolutions[i] = new double[] {raster.scale.x * ov.factor, raster.scale.y * ov.factor};
                }
            } else {
                numOverviews = 0;
            }
        }

        // process hints
        if (hints != null) {
            exec = (ExecutorService) hints.get(Hints.EXECUTOR_SERVICE);
        }
    }

    public String name() {
        return coverageName;
    }

    @Override
    public Format getFormat() {
        return format;
    }

    @Override
    public String[] getMetadataNames() {
        if (raster.time != null) {
            return new String[] {HAS_TIME_DOMAIN, TIME_DOMAIN, TIME_DOMAIN_MINIMUM, TIME_DOMAIN_MAXIMUM};
        }
        return null;
    }

    @Override
    public String getMetadataValue(String coverageName, String name) {
        if (raster.time != null) {
            TimeColumn t = raster.time;
            SimpleDateFormat in = dbDataFormat();
            SimpleDateFormat out = utcDateFormat();

            try {
                switch (name) {
                    case HAS_TIME_DOMAIN:
                        return String.valueOf(true);
                    case TIME_DOMAIN: {
                        SQL sql = new SQL().append("SELECT DISTINCT to_char(").name(t.name);

                        if (t.isTimestamp()) {
                            sql.append(" AT TIME ZONE 'UTC'");
                        }

                        sql.append(", 'YYYY-MM-DD\"T\"HH24:MI:SS.MS+00'), ").append(t.name);
                        sql.append(" FROM ")
                                .table(raster)
                                .append(" ORDER BY ")
                                .name(t.name)
                                .append(" DESC");

                        try (Connection cx = newConnection()) {
                            try (PreparedStatement ps = cx.prepareStatement(sql.logAndGet(LOG))) {
                                try (ResultSet rs = ps.executeQuery()) {
                                    List<Date> times = new ArrayList<>();
                                    while (rs.next()) {
                                        String s = rs.getString(1);
                                        if (s != null) {
                                            times.add(in.parse(s));
                                        }
                                    }
                                    return times.stream().map(out::format).collect(Collectors.joining(","));
                                }
                            }
                        }
                    }
                    case TIME_DOMAIN_MINIMUM:
                    case TIME_DOMAIN_MAXIMUM:
                        String fn = TIME_DOMAIN_MINIMUM.equals(name) ? "min" : "max";
                        Date d = queryDateExtreme(fn, raster);
                        if (d != null) {
                            return out.format(d);
                        }
                }
            } catch (SQLException | ParseException e) {
                throw new RuntimeException("Error fetching metadata value: " + name, e);
            }
        }

        return null;
    }

    @Override
    public GridCoverage2D read(GeneralParameterValue... params) throws IllegalArgumentException, IOException {

        ReadRequest req = new ReadRequest(this);

        if (params != null) {
            for (GeneralParameterValue p : params) {
                @SuppressWarnings("unchecked")
                Parameter<Object> param = (Parameter<Object>) p;

                String code = param.getDescriptor().getName().getCode();
                if (code.equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString())) {
                    final GridGeometry2D gg = (GridGeometry2D) param.getValue();
                    req.bounds = GeneralBounds.toGeneralEnvelope(gg.getEnvelope());
                    req.region = gg.toCanonical().getGridRange2D().getBounds();
                } else if (code.equals(AbstractGridFormat.TIME.getName().toString())) {
                    req.times = (List<?>) param.getValue();
                } else if (code.equals(
                        AbstractGridFormat.OVERVIEW_POLICY.getName().toString())) {
                    req.overviewPolicy = (OverviewPolicy) param.getValue();
                } else if (code.equals(
                        AbstractGridFormat.INTERPOLATION.getName().toString())) {
                    req.interpolation = (Interpolation) param.getValue();
                } else if (code.equals(
                        AbstractGridFormat.BACKGROUND_COLOR.getName().toString())) {
                    req.backgroundColor = (Color) param.getValue();
                }
            }
        }

        if (req.bounds == null) {
            // default to entire bounds
            req.bounds = GeneralBounds.toGeneralEnvelope(raster.bounds());
        }
        if (req.region == null && raster.size != null) {
            req.region = new Rectangle(raster.size);
        }

        // figure out bounds in raster crs
        ReferencedEnvelope nativeBounds;
        if (req.bounds != null) {
            nativeBounds = new ReferencedEnvelope(req.bounds);
        } else {
            nativeBounds = raster.bounds();
        }

        if (nativeBounds == null) {
            throw new IllegalStateException(
                    "No bounds specified in read request and unable to determine raster bounds");
        }

        // transform it if need be
        if (!CRS.equalsIgnoreMetadata(nativeBounds.getCoordinateReferenceSystem(), raster.crs)) {
            if (raster.crs == null) {
                throw new IllegalStateException("Raster crs is unknown, unable to transform from request crs");
            }

            try {
                nativeBounds = nativeBounds.transform(raster.crs, true);
            } catch (Exception e) {
                throw new IOException("Error transforming requested bounds", e);
            }
        }
        req.nativeBounds = nativeBounds;

        if (req.region != null) {
            req.resolution = new Point2D.Double(
                    nativeBounds.getWidth() / req.region.width, nativeBounds.getHeight() / req.region.height);
        }

        // figure out which overview to load
        ImageReadParam readParam = new ImageReadParam();
        Integer level = null;
        try {
            level = setReadParams(req.overviewPolicy(), readParam, req.bounds, req.region);
        } catch (TransformException e) {
            throw new IOException("Error setting read params", e);
        }
        if (level == null) level = 0;

        RasterColumn col = this.raster;
        if (level > 0) {
            col = this.raster.overviews.get(level - 1);
        }
        req.raster = col;

        // load the tiles
        SQL sql = new SQL()
                .append("SELECT ST_AsBinary(ST_Envelope(")
                .name(col.name)
                .append(")) AS extent")
                .append(", ST_AsTiff(")
                .name(col.name)
                .append(") AS tile")
                .append(" FROM ")
                .table(col)
                .append(" WHERE ST_Intersects(")
                .name(col.name)
                .append(",")
                .append(" ST_GeomFromWKB(?,?))");

        // add the time constraint
        if (col.time != null) {
            List<Date> times = new ArrayList<>();
            List<DateRange> ranges = new ArrayList<>();

            if (req.times != null) {
                for (Object t : req.times) {
                    if (t instanceof Date) {
                        times.add((Date) t);
                    } else if (t instanceof DateRange) {
                        ranges.add((DateRange) t);
                    } else {
                        Date d = Converters.convert(t, Date.class);
                        if (d != null) {
                            times.add(d);
                        } else {
                            LOG.warning("Unable to convert value to date:" + t);
                        }
                    }
                }
            } else {
                // no time specified, default to the latest
                try {
                    Date latest = queryDateExtreme("max", col);
                    if (latest != null) {
                        times.add(latest);
                    }
                } catch (SQLException | ParseException e) {
                    throw new IOException("Error querying for date max", e);
                }
            }

            if (!times.isEmpty()) {
                SimpleDateFormat df = dbDataFormat();

                sql.append(" AND ").name(col.time.name).append(" IN (");

                times.forEach(d -> sql.append("'").append(df.format(d)).append("',"));
                sql.trim(1);

                sql.append(")");
            } else if (!ranges.isEmpty()) {
                SimpleDateFormat df = dbDataFormat();
                sql.append(" AND (");

                for (DateRange range : ranges) {
                    sql.append("(")
                            .name(col.time.name)
                            .append(range.isMinIncluded() ? ">=" : ">")
                            .append("'")
                            .append(df.format(range.getMinValue()))
                            .append("' AND ");

                    sql.name(col.time.name)
                            .append(range.isMaxIncluded() ? "<=" : "<")
                            .append("'")
                            .append(df.format(range.getMaxValue()))
                            .append("'");

                    sql.append(") OR ");
                }
                sql.trim(4);
                sql.append(")");
            }
        }

        try {
            try (Connection cx = newConnection()) {
                try (PreparedStatement ps = cx.prepareStatement(sql.logAndGet(LOG))) {
                    ps.setBytes(1, new WKBWriter().write(toPolygon(nativeBounds)));
                    ps.setInt(2, col.srid != null ? col.srid : -1);

                    try (ResultSet rs = ps.executeQuery()) {
                        Iterator<TileData> tiles = new AbstractIterator<>() {
                            WKBReader wkb = new WKBReader(GEOMS);

                            @Override
                            protected TileData computeNext() {
                                try {
                                    if (rs.next()) {
                                        return new TileData(
                                                rs.getBytes(2),
                                                wkb.read(rs.getBytes(1)).getEnvelopeInternal());
                                    }
                                } catch (Exception e) {
                                    Throwables.throwIfInstanceOf(e, RuntimeException.class);
                                    throw new RuntimeException(e);
                                }

                                try {
                                    rs.close();
                                } catch (SQLException e) {
                                    LOG.log(Level.FINE, "Error closing result set", e);
                                }
                                try {
                                    ps.close();
                                } catch (SQLException e) {
                                    LOG.log(Level.FINE, "Error closing statement", e);
                                }

                                return endOfData();
                            }
                        };

                        if (exec == null) {
                            return compose(tiles, req);
                        } else {
                            return compose(tiles, req, exec);
                        }
                    }
                }
            }

        } catch (SQLException e) {
            throw new IOException("Error reading raster", e);
        }
    }

    GridCoverage2D compose(Iterator<TileData> tiles, ReadRequest read) throws IOException {
        Mosaic mosaic = new Mosaic(read, coverageFactory);

        while (tiles.hasNext()) {
            TileData data = tiles.next();
            try {
                Tile tile = new TileDecoder(data, read).call();
                mosaic.accept(tile);
            } catch (Exception e) {
                Throwables.throwIfInstanceOf(e, IOException.class);
                throw new IOException("Error decoding tile", e);
            }
        }

        return mosaic.coverage();
    }

    GridCoverage2D compose(Iterator<TileData> tiles, ReadRequest read, ExecutorService exec) throws IOException {
        CompletionService<Tile> work = new ExecutorCompletionService<>(exec);

        int count = 0;
        while (tiles.hasNext()) {
            count++;
            work.submit(new TileDecoder(tiles.next(), read));
        }

        Mosaic mosaic = new Mosaic(read, coverageFactory);

        while (count > 0) {
            try {
                mosaic.accept(work.take().get());
                count--;
            } catch (Exception e) {
                Throwables.throwIfInstanceOf(e, IOException.class);
                throw new IOException(e);
            }
        }

        return mosaic.coverage();
    }

    Polygon toPolygon(Envelope e) {
        return GEOMS.createPolygon(new Coordinate[] {
            new Coordinate(e.getMinX(), e.getMinY()),
            new Coordinate(e.getMinX(), e.getMaxY()),
            new Coordinate(e.getMaxX(), e.getMaxY()),
            new Coordinate(e.getMaxX(), e.getMinY()),
            new Coordinate(e.getMinX(), e.getMinY())
        });
    }

    Date queryDateExtreme(String minOrMax, RasterColumn raster) throws SQLException, ParseException {
        SQL sql = new SQL("SELECT ");
        raster.time.select(minOrMax, sql).append(" FROM ").table(raster);

        try (Connection cx = newConnection()) {
            try (PreparedStatement ps = cx.prepareStatement(sql.logAndGet(LOG))) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return dbDataFormat().parse(rs.getString(1));
                    } else {
                        LOG.warning("Empty table, unable to calculate " + minOrMax);
                    }
                }
            }
        }

        return null;
    }

    @Override
    public void dispose() {
        config.close();
        super.dispose();
    }

    Connection newConnection() throws SQLException {
        Connection cx = config.dataSource.getConnection();
        if (config.enableDrivers != null) {
            try (Statement st = cx.createStatement()) {
                st.execute(String.format("SET postgis.gdal_enabled_drivers = '%s'", config.enableDrivers));
            }
        }

        return cx;
    }

    /** Date format used to parse dates from the database. */
    static SimpleDateFormat dbDataFormat() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        return df;
    }

    /** Date format used to encoded dates to return to client code. */
    static SimpleDateFormat utcDateFormat() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df;
    }
}
