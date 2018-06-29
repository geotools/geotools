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

package org.geotools.gce.imagemosaic.jdbc.custom;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.gce.imagemosaic.jdbc.Config;
import org.geotools.gce.imagemosaic.jdbc.ImageDecoderThread;
import org.geotools.gce.imagemosaic.jdbc.ImageLevelInfo;
import org.geotools.gce.imagemosaic.jdbc.TileQueueElement;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKBWriter;

/**
 * This class is used for JDBC Access to the Postgis raster feature
 *
 * @author Christian Mueller
 * @source $URL$
 *     http://svn.osgeo.org/geotools/trunk/modules/plugin/imagemosaic-jdbc/src/main/java/org
 *     /geotools/gce/imagemosaic/jdbc/custom/JDBCAccessPGRaster.java $
 */
public class JDBCAccessPGRaster extends JDBCAccessCustom {

    private static final Logger LOGGER =
            Logging.getLogger(JDBCAccessPGRaster.class.getPackage().getName());

    /** Different sql statements needed for in-db and out-db raster data */
    protected Map<ImageLevelInfo, String> statementMap;

    /** @param config Config from XML file passed to this class */

    // Map<ImageLevelInfo,Boolean> isOutDBMap;

    public JDBCAccessPGRaster(Config config) throws IOException {
        super(config);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.gce.imagemosaic.jdbc.JDBCAccess#initialize()
     */
    public void initialize() throws IOException {

        Connection con = null;

        try {
            con = getConnection();

            if (con.getAutoCommit()) {
                con.setAutoCommit(false);
            }

            listGDALFormats(con);
            initFromDB(getConfig().getCoverageName(), con);
            calculateExtentsFromDB(getConfig().getCoverageName(), con);
            calculateResolutionsFromDB(getConfig().getCoverageName(), con);
            /*
            populate statementsMap independently of calculateResolutionsFromDB()
            in case resolutions have been pre-set and don't need to be recalculated.
            */
            populateStatementsMap(getConfig().getCoverageName(), con);
            /*
            TODO nat changes - GEOT-4525. I am  not sure if this is the best place for the next statement, as
            if configurations have been already defined and were not recalculated, we will be just overwriting
            existing configuration, albeit with the same values. But for simplicity sake, it is probably better
             to leave it here...
             */
            con.commit();
            con.close();

            for (ImageLevelInfo levelInfo : getLevelInfos()) {
                if (LOGGER.isLoggable(Level.INFO)) LOGGER.info(levelInfo.infoString());
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);

            try {
                // con.rollback();
                con.close();
            } catch (SQLException e1) {
            }

            LOGGER.severe(e.getMessage());
            throw new IOException(e);
        }

        if (getLevelInfos().isEmpty()) {
            String msg = "No level available for " + getConfig().getCoverageName();
            LOGGER.severe(msg);
            throw new IOException(msg);
        }

        // sort levelinfos
        SortedSet<ImageLevelInfo> sortColl = new TreeSet<ImageLevelInfo>();
        sortColl.addAll(getLevelInfos());
        getLevelInfos().clear();
        getLevelInfos().addAll(sortColl);
    }

    /**
     * startTileDecoders
     *
     * @param pixelDimension Not Used (passed as per interface requirement)
     * @param requestEnvelope Geographic Envelope of request
     * @param info Pyramid Level
     * @param tileQueue Queue to place retrieved tile into
     * @param coverageFactory not used (passed as per interface requirement)
     */

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.gce.imagemosaic.jdbc.JDBCAccess#startTileDecoders(java.awt.Rectangle,
     * org.geotools.geometry.GeneralEnvelope, org.geotools.gce.imagemosaic.jdbc.ImageLevelInfo,
     * java.util.concurrent.LinkedBlockingQueue)
     */
    public void startTileDecoders(
            Rectangle pixelDimension,
            GeneralEnvelope requestEnvelope,
            ImageLevelInfo levelInfo,
            LinkedBlockingQueue<TileQueueElement> tileQueue,
            GridCoverageFactory coverageFactory)
            throws IOException {
        Date start = new Date();
        Connection con = null;
        List<ImageDecoderThread> threads = new ArrayList<ImageDecoderThread>();
        ExecutorService pool = getExecutorServivicePool();

        String gridStatement = statementMap.get(levelInfo);

        try {
            con = getConnection();

            PreparedStatement s = con.prepareStatement(gridStatement);
            WKBWriter w = new WKBWriter();
            byte[] bytes = w.write(polyFromEnvelope(requestEnvelope));
            s.setBytes(1, bytes);
            s.setInt(2, levelInfo.getSrsId());

            ResultSet r = s.executeQuery();

            while (r.next()) {
                // byte[] tileBytes = getTileBytes(r,2);
                byte[] tileBytes = r.getBytes(2);
                byte[] envBytes = r.getBytes(1);
                WKBReader reader = new WKBReader();
                Geometry g;
                try {
                    g = reader.read(envBytes);
                } catch (ParseException e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                    throw new IOException(e);
                }
                Envelope env = g.getEnvelopeInternal();
                Rectangle2D tmp =
                        new Rectangle2D.Double(
                                env.getMinX(), env.getMinY(), env.getWidth(), env.getHeight());
                GeneralEnvelope tileGeneralEnvelope = new GeneralEnvelope(tmp);
                tileGeneralEnvelope.setCoordinateReferenceSystem(
                        requestEnvelope.getCoordinateReferenceSystem());

                ImageDecoderThread thread =
                        new ImageDecoderThread(
                                tileBytes,
                                "",
                                tileGeneralEnvelope,
                                pixelDimension,
                                requestEnvelope,
                                levelInfo,
                                tileQueue,
                                getConfig());
                // thread.start();
                threads.add(thread);
                pool.execute(thread);
            }

            r.close();
            s.close();

            con.close();
        } catch (SQLException e) {
            try {
                con.close();
            } catch (SQLException e1) {
            }

            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new IOException(e);
        }

        if (LOGGER.isLoggable(Level.INFO))
            LOGGER.info(
                    "Getting "
                            + threads.size()
                            + " Tiles needs "
                            + ((new Date()).getTime() - start.getTime())
                            + " millisecs");

        // wait for all threads dto finish and write end marker
        pool.shutdown();
        try {
            pool.awaitTermination(3600, TimeUnit.SECONDS); // wait for one hour
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }

        tileQueue.add(TileQueueElement.ENDELEMENT);

        if (LOGGER.isLoggable(Level.INFO))
            LOGGER.info(
                    "Getting and decoding  "
                            + threads.size()
                            + " Tiles needs "
                            + ((new Date()).getTime() - start.getTime())
                            + " millisecs");
    }

    /**
     * Step 1 of the bootstrapping process. Read meta table.
     *
     * @param coverageName the coverage name stored in the sql meta table
     * @param con jdbc connection
     * @throws SQLException
     * @throws IOException
     */
    protected void initFromDB(String coverageName, Connection con)
            throws SQLException, IOException {
        PreparedStatement s = null;
        ResultSet res = null;

        try {
            String stmt = getConfig().getSqlSelectCoverageStatement();
            s = con.prepareStatement(stmt);
            s.setString(1, coverageName);
            res = s.executeQuery();

            while (res.next()) {
                ImageLevelInfo imageLevelInfo = new ImageLevelInfo();
                imageLevelInfo.setCoverageName(coverageName);
                imageLevelInfo.setTileTableName(
                        (res.getString(getConfig().getTileTableNameAtribute())));

                imageLevelInfo.setExtentMaxX(
                        new Double(res.getDouble(getConfig().getMaxXAttribute())));

                if (res.wasNull()) {
                    imageLevelInfo.setExtentMaxX(null);
                }

                imageLevelInfo.setExtentMaxY(
                        new Double(res.getDouble(getConfig().getMaxYAttribute())));

                if (res.wasNull()) {
                    imageLevelInfo.setExtentMaxY(null);
                }

                imageLevelInfo.setExtentMinX(
                        new Double(res.getDouble(getConfig().getMinXAttribute())));

                if (res.wasNull()) {
                    imageLevelInfo.setExtentMinX(null);
                }

                imageLevelInfo.setExtentMinY(
                        new Double(res.getDouble(getConfig().getMinYAttribute())));

                if (res.wasNull()) {
                    imageLevelInfo.setExtentMinY(null);
                }

                imageLevelInfo.setResX(new Double(res.getDouble(getConfig().getResXAttribute())));

                if (res.wasNull()) {
                    imageLevelInfo.setResX(null);
                }

                imageLevelInfo.setResY(new Double(res.getDouble(getConfig().getResYAttribute())));

                if (res.wasNull()) {
                    imageLevelInfo.setResY(null);
                }

                /*
                Set noDataValue on imageLevelInfo based on what
                is stored in raster band metadata.
                Please note: alternatively this value could be loaded from mosaic config file,
                we could add an optional element/attribute to specify this value.
                */
                Number noDataValue =
                        getNoDataValue(
                                imageLevelInfo.getTileTableName(),
                                getConfig().getBlobAttributeNameInTileTable(),
                                con);
                imageLevelInfo.setNoDataValue(noDataValue);

                getLevelInfos().add(imageLevelInfo);

                imageLevelInfo.setCrs(getCRS());
                /*
                Set SrsId based on what has been specified in mosaic
                xml configuration file. It can get overwritten by value retrieved from database in
                method calculateResolutionsFromDB(). The reason I added this is: if user has specified
                resolutions in the mosaic table, then calculateResolutionsFromDB() will skip setting srsID
                which will eventually result in an exception further down the track.
                 */
                imageLevelInfo.setSrsId(getSrsId());
            }
        } catch (SQLException e) {
            throw (e);
        } finally {
            if (res != null) {
                res.close();
            }

            if (s != null) {
                s.close();
            }
        }
    }

    /*
    extract noDataValues for each overview from overview raster tables
    */
    private Number getNoDataValue(
            String coverageTableName, String blobAttributeName, Connection con)
            throws SQLException {
        PreparedStatement s = null;
        ResultSet res = null;

        try {
            String stmt =
                    "select ST_BandNoDataValue("
                            + blobAttributeName
                            + ") from "
                            + coverageTableName
                            + " limit 1";
            s = con.prepareStatement(stmt);
            res = s.executeQuery();

            if (res.next()) {
                ResultSetMetaData resultMetadata = res.getMetaData();
                String colType = resultMetadata.getColumnTypeName(1);
                if (colType != null && colType.toLowerCase().startsWith("float")) {
                    return res.getFloat(1);
                } else {
                    return res.getInt(1);
                }
            }
            return null;
        } finally {
            if (res != null) {
                res.close();
            }
            if (s != null) {
                s.close();
            }
        }
    }

    /**
     * Step 2 of the bootstrapping process.
     *
     * <p>Calculating the the extent for each image level (original + pyramids). This calculation is
     * only done if the extent info in the master table is SQL NULL. After calculation the meta
     * table is updated with the result to avoid this operation in the future.
     *
     * @param coverageName The coverage name in the sql meta table
     * @param con JDBC connection
     * @throws SQLException
     * @throws IOException
     */
    void calculateExtentsFromDB(String coverageName, Connection con)
            throws SQLException, IOException {

        PreparedStatement stmt = con.prepareStatement(getConfig().getSqlUpdateMosaicStatement());

        List<ImageLevelInfo> toBeRemoved = new ArrayList<ImageLevelInfo>();

        for (ImageLevelInfo li : getLevelInfos()) {
            if (li.getCoverageName().equals(coverageName) == false) {
                continue;
            }

            if (li.calculateExtentsNeeded() == false) {
                continue;
            }

            Date start = new Date();
            if (LOGGER.isLoggable(Level.INFO)) LOGGER.info("Calculate extent for " + li.toString());

            final String rasterAttr = getConfig().getBlobAttributeNameInTileTable();
            String envSelect =
                    "with envelopes as ( select st_envelope("
                            + rasterAttr
                            + " ) as env from "
                            + li.getTileTableName()
                            + " ) select st_asbinary(st_extent(env)) from envelopes";

            Envelope envelope = null;
            PreparedStatement s = con.prepareStatement(envSelect);
            ResultSet r = s.executeQuery();
            WKBReader reader = new WKBReader();

            if (r.next()) {
                byte[] bytes = r.getBytes(1);
                Geometry g;

                try {
                    g = reader.read(bytes);
                } catch (ParseException e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                    throw new IOException(e);
                }

                envelope = g.getEnvelopeInternal();
            }

            r.close();
            s.close();

            if (envelope == null) {
                if (LOGGER.isLoggable(Level.WARNING))
                    LOGGER.log(Level.WARNING, "No extent, removing this level");
                toBeRemoved.add(li);

                continue;
            }

            li.setExtentMaxX(new Double(envelope.getMaxX()));
            li.setExtentMaxY(new Double(envelope.getMaxY()));
            li.setExtentMinX(new Double(envelope.getMinX()));
            li.setExtentMinY(new Double(envelope.getMinY()));

            stmt.setDouble(1, li.getExtentMaxX().doubleValue());
            stmt.setDouble(2, li.getExtentMaxY().doubleValue());
            stmt.setDouble(3, li.getExtentMinX().doubleValue());
            stmt.setDouble(4, li.getExtentMinY().doubleValue());
            stmt.setString(5, li.getCoverageName());
            stmt.setString(6, li.getTileTableName());
            /*
            TODO nat - changes for GEOT-4525
             */
            if (li.getSpatialTableName() != null) {
                stmt.setString(7, li.getSpatialTableName());
            }
            stmt.execute();

            long msecs = (new Date()).getTime() - start.getTime();

            if (LOGGER.isLoggable(Level.INFO))
                LOGGER.info(
                        "Calculate extent for " + li.toString() + " finished in " + msecs + " ms ");
        }

        getLevelInfos().removeAll(toBeRemoved);

        if (stmt != null) {
            stmt.close();
        }
    }

    /**
     * Step 3 of the bootstrapping process.
     *
     * <p>Calculating the the resolution for each image level (original + pyramids). This
     * calculation is only done if the resoltion info in the master table is SQL NULL. After
     * calculation the meta table is updated with the result to avoid this operation in the future.
     *
     * @param coverageName The coverage name in the sql meta table
     * @param con JDBC Connection
     * @throws SQLException
     * @throws IOException
     */
    void calculateResolutionsFromDB(String coverageName, Connection con)
            throws SQLException, IOException {
        PreparedStatement stmt = null;

        // isOutDBMap = new HashMap<ImageLevelInfo, Boolean>();
        stmt = con.prepareStatement(getConfig().getSqlUpdateResStatement());

        List<ImageLevelInfo> toBeRemoved = new ArrayList<ImageLevelInfo>();

        for (ImageLevelInfo li : getLevelInfos()) {
            if (li.getCoverageName().equals(coverageName) == false) {
                continue;
            }

            if (li.calculateResolutionNeeded() == false) {
                continue;
            }
            Date start = new Date();
            if (LOGGER.isLoggable(Level.INFO))
                LOGGER.info("Calculate resolutions for " + li.toString());

            String select =
                    "select "
                            + "st_scalex("
                            + getConfig().getBlobAttributeNameInTileTable()
                            + "),"
                            + "st_scaley("
                            + getConfig().getBlobAttributeNameInTileTable()
                            + "),"
                            + "st_srid("
                            + getConfig().getBlobAttributeNameInTileTable()
                            + ") "
                            + " from "
                            + li.getTileTableName()
                            + " LIMIT 1";

            double resolutions[] = null;
            PreparedStatement ps = con.prepareStatement(select);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                resolutions = new double[] {rs.getDouble(1), rs.getDouble(2)};
                li.setSrsId(rs.getInt(3));
            }
            rs.close();
            ps.close();

            if (resolutions == null) {
                if (LOGGER.isLoggable(Level.WARNING))
                    LOGGER.log(Level.WARNING, "No image found, removing " + li.toString());
                toBeRemoved.add(li);
                continue;
            }

            if (resolutions[0] < 0) resolutions[0] *= -1;
            if (resolutions[1] < 0) resolutions[1] *= -1;
            li.setResX(resolutions[0]);
            li.setResY(resolutions[1]);
            if (LOGGER.isLoggable(Level.INFO))
                LOGGER.info("ResX: " + li.getResX() + " ResY: " + li.getResY());

            /*
            moved code from here into method 'populateStatementsMap' below
            which is called at initialisation. Otherwise this code was skipped in line #496
            */
            stmt.setDouble(1, li.getResX().doubleValue());
            stmt.setDouble(2, li.getResY().doubleValue());
            stmt.setString(3, li.getCoverageName());
            stmt.setString(4, li.getTileTableName());
            stmt.execute();

            long msecs = (new Date()).getTime() - start.getTime();

            if (LOGGER.isLoggable(Level.INFO))
                LOGGER.info(
                        "Calculate resolutions for "
                                + li.toString()
                                + " finished in "
                                + msecs
                                + " ms ");
        }

        getLevelInfos().removeAll(toBeRemoved);

        if (stmt != null) {
            stmt.close();
        }
    }

    private void populateStatementsMap(String coverageName, Connection con) throws SQLException {
        statementMap = new HashMap<ImageLevelInfo, String>();

        for (ImageLevelInfo li : getLevelInfos()) {
            if (li.getCoverageName().equals(coverageName) == false) {
                continue;
            }

            String select =
                    "select (ST_BandMetaData("
                            + getConfig().getBlobAttributeNameInTileTable()
                            + ")).isoutdb "
                            + " from "
                            + li.getTileTableName()
                            + " LIMIT 1";
            PreparedStatement ps = con.prepareStatement(select);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Boolean isOut = (Boolean) rs.getObject("isoutdb");
                String gridStatement =
                        isOut
                                ? "SELECT st_asbinary(st_envelope ("
                                        + getConfig().getBlobAttributeNameInTileTable()
                                        + ")),"
                                        + getConfig().getBlobAttributeNameInTileTable()
                                        + " from "
                                        + li.getTileTableName()
                                        + " where st_intersects("
                                        + getConfig().getBlobAttributeNameInTileTable()
                                        + " ,ST_GeomFromWKB(?,?))"
                                : "SELECT st_asbinary(st_envelope ("
                                        + getConfig().getBlobAttributeNameInTileTable()
                                        + ")),st_astiff("
                                        + getConfig().getBlobAttributeNameInTileTable()
                                        + ") "
                                        + " from "
                                        + li.getTileTableName()
                                        + " where st_intersects("
                                        + getConfig().getBlobAttributeNameInTileTable()
                                        + " ,ST_GeomFromWKB(?,?))";

                statementMap.put(li, gridStatement);
            }
            rs.close();
            ps.close();
        }
    }

    /**
     * @param env GeneralEnvelope
     * @return Polygon object with the same boundary as env
     */
    protected Polygon polyFromEnvelope(GeneralEnvelope env) {
        GeometryFactory factory = new GeometryFactory();

        Coordinate[] coords =
                new Coordinate[] {
                    new Coordinate(env.getMinimum(0), env.getMinimum(1)),
                    new Coordinate(env.getMinimum(0), env.getMaximum(1)),
                    new Coordinate(env.getMaximum(0), env.getMaximum(1)),
                    new Coordinate(env.getMaximum(0), env.getMinimum(1)),
                    new Coordinate(env.getMinimum(0), env.getMinimum(1))
                };

        return factory.createPolygon(factory.createLinearRing(coords), new LinearRing[0]);
    }

    /**
     * creates a thread pool
     *
     * @return
     */
    public ExecutorService getExecutorServivicePool() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        LOGGER.info("Using " + availableProcessors + " CPU(s)");
        return Executors.newFixedThreadPool(availableProcessors);
    }

    /**
     * List the formats supported by the used gdal library
     *
     * <p>Check from the command line with <code>gdalinfo --formats</code>
     *
     * @param con
     * @throws SQLException
     */
    public void listGDALFormats(Connection con) throws SQLException {
        if (LOGGER.isLoggable(Level.INFO) == false) return;

        String statement = "SELECT short_name, long_name FROM st_gdaldrivers() ORDER BY short_name";
        PreparedStatement ps = con.prepareStatement(statement);
        ResultSet rs = ps.executeQuery();
        StringBuffer buff = new StringBuffer("\n\n");
        buff.append("Supported GDAL formats for postgis raster\n");
        buff.append("Short Name\t\t\tLong Name\n");
        buff.append("-----------------------------------------------\n");
        while (rs.next()) {
            buff.append(rs.getString(1));
            buff.append("\t\t\t");
            buff.append(rs.getString(2));
            buff.append("\n");
        }
        LOGGER.info(buff.toString());
        rs.close();
        ps.close();
    }

    /*
    Extract srs Id from xml configuration file
     */
    protected Integer getSrsId() {
        String crsStr = getConfig().getCoordsys();
        String[] crsComponents = crsStr.split(":");
        if (crsComponents.length == 2) {
            try {
                return new Integer(crsComponents[1]);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
