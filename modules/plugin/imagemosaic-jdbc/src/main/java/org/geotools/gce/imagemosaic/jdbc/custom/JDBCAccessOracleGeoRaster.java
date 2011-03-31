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
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import javax.media.jai.PlanarImage;

import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.gce.imagemosaic.jdbc.Config;
import org.geotools.gce.imagemosaic.jdbc.ImageLevelInfo;
import org.geotools.gce.imagemosaic.jdbc.TileQueueElement;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKBReader;

/**
 * This class is used for JDBC Access to the Oracle GeoRaster feature
 * 
 * @author Christian Mueller based on the code of Steve Way and Pablo Najarro
 * 
 * 
 **/

public class JDBCAccessOracleGeoRaster extends JDBCAccessCustom {
    
    private final static Logger LOGGER = Logging.getLogger(JDBCAccessOracleGeoRaster.class
            .getPackage().getName());


    private static String StmtTemplatePixel = "SELECT "
            + " sdo_geor.getCellCoordinate(%s, ?, sdo_geometry(2001,%s,sdo_point_type(?,?,null), null,null)), "
            + " sdo_geor.getCellCoordinate(%s, ?, sdo_geometry(2001,%s,sdo_point_type(?,?,null), null,null)) "
            + " from %s where %s = ?";

//    static String StmtTemplateExport = "declare " + "gr sdo_georaster; " + "lb blob; " + "begin "
//            + "dbms_lob.createtemporary(lb,true); "
//            + "select a.%s into gr from %s a where a.%s = ?; "
//            + "sdo_geor.exportTo(gr, 'pLevel=%d cropArea=(%d,%d,%d,%d)', '%s', lb); " + "?:=lb; "
//            + "end; ";
    
    private static String StmtTemplateExport = "declare " + "gr sdo_georaster; " + "lb blob; " + "begin "
    + "dbms_lob.createtemporary(lb,true); "
    + "select a.%s into gr from %s a where a.%s = ?; "
    + "sdo_geor.exportTo(gr, ?, '%s', lb); " + "?:=lb; "
    + "end; ";
    
    
//    static String StmtTemplateRasterSubset = "declare " + "gr sdo_georaster; " + "lb blob; " + "begin "
//        + "dbms_lob.createtemporary(lb,true); "
//        + "select a.%s into gr from %s a where a.%s = ?; "
//        + "sdo_geor.getRasterSubset(gr, ?, sdo_number_array(?,?,?,?), null, lb,%s);"
//        + "?:=lb; "
//        + "end; ";

//   static String StmtTemplateRasterSubset = "declare " + "gr sdo_georaster; " + "lb blob; " + "begin "
//      + "dbms_lob.createtemporary(lb,true); "
//      + "select a.%s into gr from %s a where a.%s = ?; "
//      + "sdo_geor.getRasterSubset(gr, ?, sdo_geometry(2003,?,null,sdo_elem_info_array(1, 1003, 1),sdo_ordinate_array(?,?,?,?)), null, lb,%s);"
//      + "?:=lb; "
//      + "end; ";
    
      

    private String stmtPixel;
    private String stmtExport;



    /**
     * 
     * @param config
     *            Config from XML file passed to this class
     * 
     **/
    public JDBCAccessOracleGeoRaster(Config config) throws IOException {
        super(config);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geotools.gce.imagemosaic.jdbc.JDBCAccess#initialize()
     * 
     * Gathers the initial meta data needed
     */
    public void initialize() {

        LOGGER.fine("Starting GeoRaster Image Mosaic");

        Connection con = null;
        try {

            con = getConnection();
            int srid = getSRID(con);

            stmtPixel = String.format(StmtTemplatePixel, getConfig().getGeoRasterAttribute(), Integer
                    .toString(srid), getConfig().getGeoRasterAttribute(), Integer.toString(srid), getConfig()
                    .getMasterTable(), getConfig().getCoverageNameAttribute());
            
            stmtExport = String.format(StmtTemplateExport, getConfig().getGeoRasterAttribute(), getConfig()
                    .getMasterTable(), getConfig().getCoverageNameAttribute(), 
                     "TIFF");

            CoordinateReferenceSystem crs = getCRS();
            Envelope extent = getExtent(con);
            double[] spatialResolutions = getSpatialResolutions(con);
            int numberOfPyramidLevels = getPyramidLevels(con);

            LOGGER.fine("Base Spatial Resolution X: " + spatialResolutions[0] + ", Y: "
                    + spatialResolutions[1]);
            LOGGER.fine("Number of Pyramids" + numberOfPyramidLevels);

            LOGGER.fine("minX " + extent.getMinX());
            LOGGER.fine("maxX " + extent.getMaxX());
            LOGGER.fine("maxY " + extent.getMaxY());
            LOGGER.fine("minY " + extent.getMinY());

            for (int i = 0; i < numberOfPyramidLevels; i++) {

                ImageLevelInfo imageLevel = new ImageLevelInfo();

                imageLevel.setCoverageName(getConfig().getCoverageName());
                imageLevel.setSpatialTableName(new String(i + ""));
                imageLevel.setTileTableName(new String(i + ""));

                imageLevel.setResX(spatialResolutions[0] * (Math.pow(2, i)));
                imageLevel.setResY(spatialResolutions[1] * (Math.pow(2, i)));

                imageLevel.setExtentMinX(extent.getMinX());
                imageLevel.setExtentMinY(extent.getMinY());
                imageLevel.setExtentMaxX(extent.getMaxX());
                imageLevel.setExtentMaxY(extent.getMaxY());

                imageLevel.setSrsId(srid);
                imageLevel.setCrs(crs);
                getLevelInfos().add(imageLevel);

                LOGGER.fine("New Level Info for Coverage: " + getConfig().getCoverageName()
                        + " Pyramid Level: " + imageLevel.getSpatialTableName());
                LOGGER.fine("Resolution X: " + imageLevel.getResX());
                LOGGER.fine("Resolution Y: " + imageLevel.getResY());
                LOGGER.fine("SRID: " + imageLevel.getSrsId());
                LOGGER.fine("CRS: " + imageLevel.getCrs());
            }

            LOGGER.fine("Image Level List Size: " + getLevelInfos().size());

        } finally {
            closeConnection(con);

        }

    }


    /**
     * getExtent of pyramid level 0
     * 
     * 
     * @return Envelope
     **/

    private Envelope getExtent(Connection con) {

        LOGGER.fine("Get Extent Method");

        String extentSelectLBX = "select sdo_geometry.get_wkb(sdo_geor.generateSpatialExtent("
                + getConfig().getGeoRasterAttribute() + ")) from " + getConfig().getMasterTable() + " where "
                + getConfig().getCoverageNameAttribute() + "=?";

        PreparedStatement s = null;
        ResultSet r = null;
        Envelope extent = null;

        try {

            s = con.prepareStatement(extentSelectLBX);
            s.setString(1, getConfig().getCoverageName());
            r = s.executeQuery();
            r.next();
            byte[] wkb = r.getBytes(1);
            Geometry geom = new WKBReader().read(wkb);
            extent = geom.getEnvelopeInternal();
            LOGGER.fine("creating Extent");

        } catch (Exception e) {
            throw new RuntimeException(e);

        } finally {

            closeResultSet(r);
            closePreparedStmt(s);

        }

        LOGGER.fine("returning Extent");
        return extent;
    }

    /**
     * getSRID
     * 
     * 
     * @return int
     **/

    private int getSRID(Connection con) {

        LOGGER.fine("getSRId Method");

        String SRSSelect = "select sdo_geor.getModelSRID(" + getConfig().getGeoRasterAttribute()
                + ") from " + getConfig().getMasterTable() + " where "
                + getConfig().getCoverageNameAttribute() + "=?";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        int srid = 0;

        try {
            stmt = con.prepareStatement(SRSSelect);

            stmt.setString(1, getConfig().getCoverageName());
            rs = stmt.executeQuery();

            if (rs.next()) {
                srid = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {

            closeResultSet(rs);
            closePreparedStmt(stmt);

        }

        return srid;

    }

    /**
     * getSpatialResolutions based on x/y cell order
     * 
     * 
     * @return double[]
     **/

    private double[] getSpatialResolutions(Connection con) {

        LOGGER.fine("getSpatialResolution Method");

        String sqlSpatialResolution = "select sdo_geor.getspatialresolutions("
                + getConfig().getGeoRasterAttribute() + ") from " + getConfig().getMasterTable() + " where "
                + getConfig().getCoverageNameAttribute() + "=?";

        LOGGER.fine("Sptial Reso SQL:" + sqlSpatialResolution);

        double[] spatialResolution = new double[2];
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement(sqlSpatialResolution);
            stmt.setString(1, getConfig().getCoverageName());
            rs = stmt.executeQuery();
            rs.next();
            Array array = rs.getArray(1);
            BigDecimal[] javaArray = (BigDecimal[]) array.getArray();
            spatialResolution[1] = javaArray[0].doubleValue();
            spatialResolution[0] = javaArray[1].doubleValue();
            LOGGER.fine("Assigned X Value: " + spatialResolution[0]);
            LOGGER.fine("Assigned Y Value: " + spatialResolution[1]);
        } catch (Exception ex) {
            LOGGER.severe("Failure getting spatial resolution");
        } finally {
            closeResultSet(rs);
            closePreparedStmt(stmt);

        }

        LOGGER.fine("getSpatialResolution Finished");
        return spatialResolution;
    }

    /**
     * getPyramidLevels
     * 
     * 
     * @return Returns Number of Pyramids Available for Coverage
     **/

    private int getPyramidLevels(Connection con) {

        LOGGER.fine("getPyrmidLevels Method");

        String sqlPyramidLevels = "select sdo_geor.getPyramidMaxLevel("
                + getConfig().getGeoRasterAttribute() + ") from " + getConfig().getMasterTable() + " where "
                + getConfig().getCoverageNameAttribute() + " = ?";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        int numberOfPyramidLevels = 0;

        try {

            LOGGER.fine("get pyramid level sql: " + sqlPyramidLevels);
            stmt = con.prepareStatement(sqlPyramidLevels);
            stmt.setString(1, getConfig().getCoverageName());
            rs = stmt.executeQuery();

            if (rs.next()) {
                LOGGER.fine("Assiging number of levels");
                numberOfPyramidLevels = rs.getInt(1) + 1;
                LOGGER.fine("Assigned number of levels");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResultSet(rs);
            closePreparedStmt(stmt);
        }

        LOGGER.fine("Returning Pyramid Levels");
        return numberOfPyramidLevels;

    }

    /**
     * startTileDecoders
     * 
     * @param pixelDimension
     *            Not Used (passed as per interface requirement)
     * 
     * @param requestEnvelope
     *            Geographic Envelope of request
     * 
     * @param info
     *            Pyramid Level
     * 
     * @param tileQueue
     *            Queue to place retrieved tile into
     * 
     * @param coverageFactory
     *            not used (passed as per interface requirement)
     * 
     **/

    public void startTileDecoders(Rectangle pixelDimension, GeneralEnvelope requestEnvelope,
            ImageLevelInfo info, LinkedBlockingQueue<TileQueueElement> tileQueue,
            GridCoverageFactory coverageFactory) throws IOException {

        long start = System.currentTimeMillis();
        LOGGER.fine("Starting GeoRaster Tile Decoder");

        Connection con = null;

        con = getConnection();
        TileQueueElement tqe = getSingleTQElement(requestEnvelope, info, con);
        tileQueue.add(tqe);
        closeConnection(con);
        tileQueue.add(TileQueueElement.ENDELEMENT);

        LOGGER.fine("Finished GeoRaster Tile Decoder");

        LOGGER.info("GeoRaster Generation time: " + (System.currentTimeMillis() - start));

    }



    /**
     * 
     * @param envelopeOrig
     *            Envelope in world coords
     * @param info
     *            ImageLevelInfo
     * @param conn
     *            Database Connection
     * @return TileQueueElement containing the georeferenced cropped image
     */
     private TileQueueElement getSingleTQElement(GeneralEnvelope envelopeOrig, ImageLevelInfo info,
            Connection conn) {

        int level = Integer.parseInt(info.getTileTableName());
        BufferedImage bimg = null;

        // check a against the extent of the pyramid level
        GeneralEnvelope envelope = new GeneralEnvelope(envelopeOrig);
        GeneralEnvelope intersectEnvelope = new GeneralEnvelope(new double[] {
                info.getExtentMinX(), info.getExtentMinY() }, new double[] { info.getExtentMaxX(),
                info.getExtentMaxY() });
        intersectEnvelope.setCoordinateReferenceSystem(envelope.getCoordinateReferenceSystem());
        envelope.intersect(intersectEnvelope);

        try {
            LOGGER.fine("Starting to Retrieve GeoRaster Image");

            byte[] bytes = getImageBytesUsingSDOExport(envelope, level, conn);

            // start creating a java Buffered image from the blob
            SeekableStream stream = new ByteArraySeekableStream(bytes);

            // find an ImageDecorder
            String decoderName = null;
            for (String dn : ImageCodec.getDecoderNames(stream)) {
                decoderName = dn;
                break;
            }
            // decode Image
            ImageDecoder decoder = ImageCodec.createImageDecoder(decoderName, stream, null);
            RenderedImage rimage = decoder.decodeAsRenderedImage();

            // Check for the color model, if there is none, create one
            ColorModel cm = rimage.getColorModel();
            if (cm == null)
                cm = PlanarImage.createColorModel(rimage.getSampleModel());

            // Convert to BufferedImage
            PlanarImage pimage = PlanarImage.wrapRenderedImage(rimage);
            bimg = pimage.getAsBufferedImage(null, cm);

            LOGGER.fine("Creating BufferedImage from GeoRaster Object");

            // LOGGER.fine("Writing Retrieved Image to disk (Should be for Debugging only!)");
            // ImageIO.write(bimg,"png", new File("/tmp/pics/test.png"));

            return new TileQueueElement(getConfig().getCoverageName(), bimg, envelope);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Gets the blob using Oracle Georaster export facility
     * 
     * @param envelope  The window in world coords
     * @param level     Pyramid level
     * @param conn      Connection
     * @return          The BLOB as byte array
     */
    private byte[] getImageBytesUsingSDOExport(GeneralEnvelope envelope, int level, Connection conn) {
        PreparedStatement ps = null;
        CallableStatement cs = null;
        ResultSet r = null;

        try {
            ps = conn.prepareStatement(stmtPixel);

            ps.setInt(1, level);
            ps.setDouble(2, envelope.getMinimum(0));
            ps.setDouble(3, envelope.getMaximum(1));
            ps.setInt(4, level);
            ps.setDouble(5, envelope.getMaximum(0));
            ps.setDouble(6, envelope.getMinimum(1));
            ps.setString(7, getConfig().getCoverageName());

            r = ps.executeQuery();
            BigDecimal[] pixelCoords1 = null;
            BigDecimal[] pixelCoords2 = null;

            if (r.next()) {
                pixelCoords1 = (BigDecimal[]) r.getArray(1).getArray();
                pixelCoords2 = (BigDecimal[]) r.getArray(2).getArray();
            } else {
                throw new RuntimeException("No cell/pixel coordinates for world Envelope "
                        + envelope);
            }

            r.close();
            ps.close();

            // Export the georaster object, cropped by cell coordinates as a TIFF image into a BLOB

            
            cs = conn.prepareCall(stmtExport);
            cs.setString(1, getConfig().getCoverageName());
            String params = String.format("pLevel=%d cropArea=(%d,%d,%d,%d)",level, 
                    pixelCoords1[0].intValue(), pixelCoords1[1].intValue(), pixelCoords2[0].intValue(),pixelCoords2[1].intValue());

            cs.setString(2, params);
            cs.registerOutParameter(3, Types.BLOB);
            cs.execute();
            Blob blob = cs.getBlob(3);
            byte[] bytes = blob.getBytes(1, (int) blob.length());
            cs.close();
            return bytes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeResultSet(r);
            closeStmt(ps);
            closeStmt(cs);
        }

    }
}
