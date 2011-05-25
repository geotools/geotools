/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009, Open Source Geospatial Foundation (OSGeo)
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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;

import javax.imageio.ImageIO;

import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.gce.imagemosaic.jdbc.Config;
import org.geotools.gce.imagemosaic.jdbc.ImageLevelInfo;
import org.geotools.gce.imagemosaic.jdbc.TileQueueElement;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;

/**
 * @author mcr
 *
 *
 * @source $URL$
 */
public class JDBCAccessH2Custom extends JDBCAccessCustom {

    public JDBCAccessH2Custom(Config config) throws IOException {
        super(config);
        
    }

    /* (non-Javadoc)
     * @see org.geotools.gce.imagemosaic.jdbc.custom.JDBCAccessCustom#initialize()
     */
    @Override
    public void initialize() throws SQLException, IOException {
        Connection con = getConnection();
        
        Envelope extent = getExtent(con);
        CoordinateReferenceSystem crs = getCRS();
        
        String stmt = "select RESX,RESY from oek order by level";
        PreparedStatement ps = con.prepareStatement(stmt);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ImageLevelInfo li = new ImageLevelInfo();
            getLevelInfos().add(li);
            li.setResX(rs.getDouble(1));
            li.setResY(rs.getDouble(2)*-1);
            li.setExtentMinX(extent.getMinX());
            li.setExtentMaxX(extent.getMaxX());
            li.setExtentMinY(extent.getMinY());
            li.setExtentMaxY(extent.getMaxY());    
            li.setCrs(crs);
        }
        rs.close();
        ps.close();
        
    }

    Envelope getExtent(Connection con) throws SQLException, IOException{

        String stmt = "select RESX,RESY,ULX,ULY,DATA from OEK where level = 0";
        PreparedStatement ps = con.prepareStatement(stmt);
        ResultSet rs = ps.executeQuery();
        rs.next();
        double resX = rs.getDouble(1);
        double resY = rs.getDouble(2);
        double ulx = rs.getDouble(3);
        double uly = rs.getDouble(4);
        byte[] bytes = rs.getBytes(5);
        
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
        
        double minx = ulx;
        double maxx = ulx + img.getWidth()*resX;
        double miny = uly + img.getHeight() * resY;
        double maxy = uly;
        Envelope result = new Envelope(minx,maxx,miny,maxy);
           
        rs.close();
        ps.close();
        return result;
    }
    
    
    /* (non-Javadoc)
     * @see org.geotools.gce.imagemosaic.jdbc.custom.JDBCAccessCustom#startTileDecoders(java.awt.Rectangle, org.geotools.geometry.GeneralEnvelope, org.geotools.gce.imagemosaic.jdbc.ImageLevelInfo, java.util.concurrent.LinkedBlockingQueue, org.geotools.coverage.grid.GridCoverageFactory)
     */
    @Override
    public void startTileDecoders(Rectangle pixelDimension, GeneralEnvelope requestEnvelope,
            ImageLevelInfo info, LinkedBlockingQueue<TileQueueElement> tileQueue,
            GridCoverageFactory coverageFactory) throws IOException {
        try {
            Connection con = getConnection();
            int level = getLevelInfos().indexOf(info);
            BufferedImage img = getBufferedImage(level, con);
            GeneralEnvelope genv = new GeneralEnvelope(info.getCrs());
            genv.setRange(0, info.getExtentMinX(), info.getExtentMaxX());
            genv.setRange(1, info.getExtentMinY(), info.getExtentMaxY());
            TileQueueElement tqElem = new TileQueueElement("oek",img,genv);;
            tileQueue.add(tqElem);
            con.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        tileQueue.add(TileQueueElement.ENDELEMENT);
    }

    
    BufferedImage getBufferedImage(int level,Connection con) throws SQLException, IOException{
        String stmt = "select DATA from OEK where level = "+level;
        PreparedStatement ps = con.prepareStatement(stmt);
        ResultSet rs = ps.executeQuery();
        rs.next();
        byte[] bytes = rs.getBytes(1);
        
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
        
           
        rs.close();
        ps.close();
        return img;
        
    }

}
