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
import junit.framework.Test;
import junit.framework.TestSuite;
import org.geotools.gce.imagemosaic.jdbc.AbstractTest;
import org.geotools.gce.imagemosaic.jdbc.Config;
import org.geotools.gce.imagemosaic.jdbc.DBDialect;
import org.geotools.gce.imagemosaic.jdbc.UniversalDialect;
import org.junit.Assert;

/** @author mcr */
public class H2CustomTest extends AbstractTest {

    static DBDialect dialect = null;

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

    @Override
    public void setUp() throws Exception {
        // No fixture check needed
    }

    @Override
    protected String getFixtureId() {
        return null;
    }

    @Override
    protected String getXMLConnectFragmentName() {
        return "connect.h2.xml.inc";
    }

    @Override
    protected String getDriverClassName() {
        return "org.h2.Driver";
    }

    @Override
    protected String getJDBCUrl(String host, Integer port, String dbName) {
        return "jdbc:h2:target/h2/testdata";
    }

    @Override
    @org.junit.Test
    public void testCreate() {
        String createStmt =
                "CREATE TABLE OEK ( level INT NOT NULL,"
                        + " RESX DOUBLE  ,  RESY DOUBLE,  ULX DOUBLE ,  ULY DOUBLE,"
                        + " Data BLOB,CONSTRAINT OEK_PK PRIMARY KEY(level))";

        try (InputStream worldIn = new URL("file:target/resources/baseimage/map.tfw").openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(worldIn))) {

            // read world file

            double xRes = Double.valueOf(reader.readLine());
            reader.readLine();
            reader.readLine();
            double yRes = Double.valueOf(reader.readLine());
            double ulx = Double.valueOf(reader.readLine());
            double uly = Double.valueOf(reader.readLine());

            try (java.sql.Connection con = dialect.getConnection()) {
                con.prepareStatement(createStmt).execute();
                ByteArrayOutputStream baseOut = new ByteArrayOutputStream();
                int in;
                URL baseImageUrl = new URL("file:target/resources/baseimage/map.tif");
                try (InputStream imageIn = baseImageUrl.openStream()) {
                    while ((in = imageIn.read()) != -1) {
                        baseOut.write(in);
                    }
                }

                try (PreparedStatement ps =
                        con.prepareStatement("INSERT INTO oek values(?,?,?,?,?,?)")) {
                    ps.setInt(1, 0);
                    ps.setDouble(2, xRes);
                    ps.setDouble(3, yRes);
                    ps.setDouble(4, ulx);
                    ps.setDouble(5, uly);
                    ps.setBytes(6, baseOut.toByteArray());
                    ps.execute();

                    BufferedImage baseImage = ImageIO.read(baseImageUrl);

                    BufferedImage pyramid1 = getNextPyramid(baseImage);
                    ByteArrayOutputStream p1Out = new ByteArrayOutputStream();
                    ImageIO.write(pyramid1, "TIF", p1Out);

                    ps.setInt(1, 1);
                    ps.setDouble(2, xRes * 2);
                    ps.setDouble(3, yRes * 2);
                    ps.setDouble(4, ulx);
                    ps.setDouble(5, uly);
                    ps.setBytes(6, p1Out.toByteArray());
                    ps.execute();

                    BufferedImage pyramid2 = getNextPyramid(pyramid1);
                    ByteArrayOutputStream p2Out = new ByteArrayOutputStream();
                    ImageIO.write(pyramid2, "TIF", p2Out);

                    ps.setInt(1, 2);
                    ps.setDouble(2, xRes * 4);
                    ps.setDouble(3, yRes * 4);
                    ps.setDouble(4, ulx);
                    ps.setDouble(5, uly);
                    ps.setBytes(6, p2Out.toByteArray());
                    ps.execute();
                }
                con.commit();
            }

        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail(e.getMessage());
        }
    }

    private BufferedImage getNextPyramid(BufferedImage base) {
        BufferedImage scaledImage =
                new BufferedImage(
                        base.getWidth() / 2, base.getHeight() / 2, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = scaledImage.createGraphics();
        AffineTransform xform = AffineTransform.getScaleInstance(0.5, 0.5);
        graphics2D.drawImage(base, xform, null);
        graphics2D.dispose();
        return scaledImage;
    }

    @Override
    @org.junit.Test
    public void testDrop() throws Exception {
        try (java.sql.Connection con = dialect.getConnection()) {
            con.prepareStatement("DROP TABLE IF EXISTS OEK").execute();
            con.commit();
        }
    }
}
