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

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.PreparedStatement;

import javax.imageio.ImageIO;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.geotools.gce.imagemosaic.jdbc.AbstractTest;
import org.geotools.gce.imagemosaic.jdbc.Config;
import org.geotools.gce.imagemosaic.jdbc.DBDialect;
import org.geotools.gce.imagemosaic.jdbc.UniversalDialect;

/**
 * @author mcr
 * 
 *
 * @source $URL$
 */
public class H2CustomTest extends AbstractTest {

    

	static DBDialect dialect = null;

	{
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public H2CustomTest(String test) {
		super(test);
	}

	@Override
	public String getConfigUrl() {
		return "file:target/resources/oek.h2custom.xml";
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();

		H2CustomTest test = new H2CustomTest("");

		if (test.checkPreConditions() == false) {
			return suite;
		}

		suite.addTest(new H2CustomTest("testGetConnection"));
		suite.addTest(new H2CustomTest("testDrop"));
		suite.addTest(new H2CustomTest("testCreate"));
		suite.addTest(new H2CustomTest("testImage1"));
		suite.addTest(new H2CustomTest("testFullExtent"));
		suite.addTest(new H2CustomTest("testNoData"));
		suite.addTest(new H2CustomTest("testPartial"));
		suite.addTest(new H2CustomTest("testVienna"));
		suite.addTest(new H2CustomTest("testViennaEnv"));
		suite.addTest(new H2CustomTest("testOutputTransparentColor"));
		suite.addTest(new H2CustomTest("testOutputTransparentColor2"));
		suite.addTest(new H2CustomTest("testDrop"));
		suite.addTest(new H2CustomTest("testCloseConnection"));

		return suite;
	}

	@Override
	protected String getSubDir() {
		return "h2custom";
	}

	@Override
	protected DBDialect getDBDialect() {
		if (dialect != null) {
			return dialect;
		}

		Config config = null;

		try {
			config = Config.readFrom(new URL(getConfigUrl()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		dialect = new UniversalDialect(config);

		return dialect;
	}



	public void setUp() throws Exception {
		// No fixture check needed
	}

	protected String getFixtureId() {
		return null;
	}

	protected String getXMLConnectFragmentName() {
		return "connect.h2.xml.inc";
	}

	protected String getDriverClassName() {
		return "org.h2.Driver";
	}

	protected String getJDBCUrl(String host, Integer port, String dbName) {
		return "jdbc:h2:target/h2/testdata";
	}

    @Override
    public void testCreate() {
        String createStmt =  
        "CREATE TABLE OEK ( level INT NOT NULL," +
            " RESX DOUBLE  ,  RESY DOUBLE,  ULX DOUBLE ,  ULY DOUBLE," + 
            " Data BLOB,CONSTRAINT OEK_PK PRIMARY KEY(level))";
        
        try {
      
            // read world file
            InputStream worldIn = new URL("file:target/resources/baseimage/map.tfw").openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(worldIn));
            double xRes = new Double(reader.readLine());
            reader.readLine();
            reader.readLine();
            double yRes = new Double(reader.readLine());
            double ulx = new Double(reader.readLine());
            double uly = new Double(reader.readLine());
            reader.close();
            
            URL baseImageUrl = new URL("file:target/resources/baseimage/map.tif"); 
            java.sql.Connection con = dialect.getConnection();
            con.prepareStatement(createStmt).execute();
            InputStream imageIn = baseImageUrl.openStream();
            ByteArrayOutputStream imageOut = new ByteArrayOutputStream();
            int in; 
            while ((in = imageIn.read())!=-1) {
                imageOut.write(in);
            }
                                   
            PreparedStatement ps = con.prepareStatement("INSERT INTO oek values(?,?,?,?,?,?)");
            ps.setInt(1, 0);
            ps.setDouble(2, xRes);
            ps.setDouble(3, yRes);
            ps.setDouble(4, ulx);
            ps.setDouble(5, uly);
            ps.setBytes(6, imageOut.toByteArray());            
            ps.execute();
            
            imageIn = new URL("file:target/resources/baseimage/map.tif").openStream();
            BufferedImage baseImage = ImageIO.read(baseImageUrl);
            
            BufferedImage pyramid1 = getNextPyramid(baseImage);
            imageOut = new ByteArrayOutputStream();
            ImageIO.write(pyramid1, "TIF", imageOut);
            
            ps.setInt(1, 1);
            ps.setDouble(2, xRes*2);
            ps.setDouble(3, yRes*2);
            ps.setDouble(4, ulx);
            ps.setDouble(5, uly);
            ps.setBytes(6, imageOut.toByteArray());            
            ps.execute();

            BufferedImage pyramid2 = getNextPyramid(pyramid1);
            imageOut = new ByteArrayOutputStream();
            ImageIO.write(pyramid2, "TIF", imageOut);

            ps.setInt(1, 2);
            ps.setDouble(2, xRes*4);
            ps.setDouble(3, yRes*4);
            ps.setDouble(4, ulx);
            ps.setDouble(5, uly);
            ps.setBytes(6, imageOut.toByteArray());            
            ps.execute();

            
            ps.close();
            con.commit();
            con.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
            
        
    }

    private BufferedImage getNextPyramid(BufferedImage base) {
        BufferedImage scaledImage = new BufferedImage(
                base.getWidth()/2 , base.getHeight() /2, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D graphics2D = scaledImage.createGraphics();
        AffineTransform xform = AffineTransform.getScaleInstance(0.5,0.5);
        graphics2D.drawImage(base, xform, null);
        graphics2D.dispose();
        return scaledImage;        
    }
    
    @Override
    public void testDrop() {
        java.sql.Connection con = null;
        try {
            con = dialect.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());            
        }        
        try {
            con.prepareStatement("DROP TABLE OEK").execute();
            con.commit();
            con.close();
        } catch (Exception e) {}
        
    }

}
