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

import java.awt.Color;
import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;
import junit.framework.TestCase;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.gce.imagemosaic.jdbc.Import.ImportTyp;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.junit.Assert;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public abstract class AbstractTest extends TestCase {

    protected static final Logger LOGGER = Logging.getLogger(AbstractTest.class);

    protected static String CRSNAME = "EPSG:4326";

    protected static Connection Connection = null;

    protected static double DELTA = 1;

    protected static GeneralEnvelope ENV_1 =
            new GeneralEnvelope(new double[] {14, 47}, new double[] {16, 48});

    protected static GeneralEnvelope ENV_VIENNA =
            new GeneralEnvelope(new double[] {16.2533, 48.1371}, new double[] {16.4909, 48.2798});

    protected static GeneralEnvelope ENV_VIENNA2 =
            new GeneralEnvelope(new double[] {15.7533, 47.6371}, new double[] {15.9909, 47.7298});

    protected static String OUTPUTDIR_BASE = "target";

    protected static String OUTPUTDIR_RESOURCES =
            OUTPUTDIR_BASE + File.separator + "resources" + File.separator;

    protected static String RESOURCE_ZIP = "src/test/resources/resources.zip";

    protected Properties fixture;

    public AbstractTest(String test) {
        super(test);
    }

    protected boolean checkPreConditions() {
        try {
            initOutputDir();
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);

            LOGGER.severe("Cannot init " + OUTPUTDIR_RESOURCES + ", skipping test");

            return false;
        }

        String driverClassName = getDriverClassName();

        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException ex) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.log(Level.WARNING, driverClassName + " not found, skipping test");

            return false;
        }

        File file = getFixtureFile();

        if ((file != null) && (file.exists() == false)) {
            if (LOGGER.isLoggable(Level.CONFIG))
                LOGGER.log(Level.CONFIG, file.getAbsolutePath() + " not found, skipping test");

            return false;
        }

        return true;
    }

    public abstract String getConfigUrl();

    protected String getSrsId() {
        return null;
    }

    protected String getOutPutDir() {
        return OUTPUTDIR_BASE + File.separator + getSubDir();
    }

    protected String[] getTileTableNames() {
        return new String[] {"TILES1", "TILES2", "TILES3"};
    }

    protected String[] getSpatialTableNames() {
        return new String[] {"SPATIAL1", "SPATIAL2", "SPATIAL3"};
    }

    protected void createTargetResourceDir(File targetResourcedir) throws Exception {
        targetResourcedir.mkdir();

        ZipFile zipFile = new ZipFile(RESOURCE_ZIP);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();

            if (entry.isDirectory()) {
                File dir = new File(OUTPUTDIR_RESOURCES + entry.getName());
                dir.mkdir();
            } else {
                InputStream in = zipFile.getInputStream(entry);
                FileOutputStream out = new FileOutputStream(OUTPUTDIR_RESOURCES + entry.getName());
                byte[] buff = new byte[4096];
                int count;

                while ((count = in.read(buff)) > 0) out.write(buff, 0, count);

                in.close();
                out.close();
            }
        }
    }

    protected void initOutputDir() throws Exception {
        File targetResourcedir = new File(OUTPUTDIR_RESOURCES);

        if (targetResourcedir.exists() == false) {
            createTargetResourceDir(targetResourcedir);
        }

        // delete previous results
        File dir = new File(getOutPutDir());

        if (dir.exists()) {
            File[] files = dir.listFiles();

            if (files != null) {
                for (File f : files) f.delete();
            }
        } else {
            dir.mkdir();
        }
    }

    public void testDrop() {
        try {
            Connection.commit();
        } catch (SQLException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }

        try {
            Connection.prepareStatement(
                            getDBDialect()
                                    .getDropTableStatement(
                                            getDBDialect().getConfig().getMasterTable()))
                    .execute();
            Connection.commit();
        } catch (SQLException e) {
            try {
                Connection.rollback();
            } catch (SQLException ex) {
            }

            ;
        }

        ;

        for (String tn : getTileTableNames()) {
            try {
                Connection.prepareStatement((getDBDialect().getDropTableStatement(tn))).execute();
                Connection.commit();
            } catch (SQLException e) {
                try {
                    Connection.rollback();
                } catch (SQLException ex) {
                }

                ;
            }
        }

        for (String tn : getSpatialTableNames()) {
            try {
                Connection.prepareStatement((getDBDialect().getDropIndexStatment(tn))).execute();

                try {
                    Connection.rollback();
                } catch (SQLException ex) {
                }

                ;
            } catch (SQLException e) {
                try {
                    Connection.rollback();
                } catch (SQLException ex) {
                }

                ;
            }

            ;
        }

        for (String tn : getSpatialTableNames()) {
            try {

                String stmt = getDBDialect().getUnregisterSpatialStatement((tn));

                if (stmt != null) {
                    executeUnRegister(stmt);
                }

                Connection.prepareStatement((getDBDialect().getDropTableStatement(tn))).execute();
                Connection.commit();
            } catch (SQLException e) {
                try {
                    Connection.rollback();
                } catch (SQLException ex) {
                }

                ;
            }

            ;
        }

        try {
            Connection.commit();
        } catch (SQLException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
    }

    public void testScripts() {
        DDLGenerator gen =
                new DDLGenerator(
                        getDBDialect().getConfig(),
                        "SPAT",
                        "TILE",
                        2,
                        ";",
                        getSrsId(),
                        OUTPUTDIR_BASE + File.separator + getSubDir());
        try {
            gen.generate();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
    }

    public void testCreate() {
        try {
            // createXMLConnectFragment();

            Connection.prepareStatement(getDBDialect().getCreateMasterStatement()).execute();

            for (String tn : getTileTableNames()) {
                Connection.prepareStatement(getDBDialect().getCreateTileTableStatement(tn))
                        .execute();
            }

            for (String tn : getSpatialTableNames()) {
                Connection.prepareStatement(getDBDialect().getCreateSpatialTableStatement(tn))
                        .execute();

                String stmt = getDBDialect().getRegisterSpatialStatement(tn, getSrsId());

                if (stmt != null) {
                    executeRegister(stmt);
                }
            }

            Connection.commit();

            for (int i = 0; i < getTileTableNames().length; i++) {

                Import imp = null;
                if (i == 0) {
                    URL dirFileUrl = new URL("file:" + OUTPUTDIR_RESOURCES);
                    imp =
                            new Import(
                                    getDBDialect().getConfig(),
                                    new ImportParam(
                                            getSpatialTableNames()[i],
                                            getTileTableNames()[i],
                                            dirFileUrl,
                                            "tif",
                                            Import.ImportTyp.DIR),
                                    2,
                                    Connection);
                }
                if (i == 1) {
                    URL shapeFileUrl = new URL("file:" + OUTPUTDIR_RESOURCES + i + "/index.shp");
                    imp =
                            new Import(
                                    getDBDialect().getConfig(),
                                    new ImportParam(
                                            getSpatialTableNames()[i],
                                            getTileTableNames()[i],
                                            shapeFileUrl,
                                            "LOCATION",
                                            Import.ImportTyp.SHAPE),
                                    2,
                                    Connection);
                }
                if (i == 2) {
                    URL csvFileUrl = new URL("file:" + OUTPUTDIR_RESOURCES + i + "/index.csv");
                    imp =
                            new Import(
                                    getDBDialect().getConfig(),
                                    new ImportParam(
                                            getSpatialTableNames()[i],
                                            getTileTableNames()[i],
                                            csvFileUrl,
                                            ";",
                                            Import.ImportTyp.CSV),
                                    2,
                                    Connection);
                }
                imp.fillSpatialTable();
            }

            for (String tn : getSpatialTableNames()) {
                Connection.prepareStatement(getDBDialect().getCreateIndexStatement(tn)).execute();
            }

            Connection.commit();
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail(e.getMessage());
        }
    }

    protected JDBCAccess getJDBCAccess() {
        return JDBCAccessFactory.JDBCAccessMap.get(getConfigUrl().toString());
    }

    public void testImportParamList() throws Exception {

        URL shapeFileUrl = null, csvFileUrl = null, dirFileUrl = null;

        try {
            shapeFileUrl = new URL("file:" + OUTPUTDIR_RESOURCES + "index.shp");

            csvFileUrl = new URL("file:" + OUTPUTDIR_RESOURCES + "index.csv");

            dirFileUrl = new URL("file:" + OUTPUTDIR_RESOURCES);
        } catch (MalformedURLException ex) {
            // should not happen
        }

        List<ImportParam> importParamList = null;

        importParamList = new ArrayList<ImportParam>();
        Import.fillImportParamList(
                "SPAT", "TILE", dirFileUrl, "tif", ImportTyp.DIR, importParamList);
        assertTrue(importParamList.size() == 3);

        assertTrue(importParamList.get(0).getTileTableName().equals("TILE_0"));
        assertTrue(importParamList.get(1).getTileTableName().equals("TILE_1"));
        assertTrue(importParamList.get(2).getTileTableName().equals("TILE_2"));
        assertTrue(importParamList.get(0).getSpatialTableName().equals("SPAT_0"));
        assertTrue(importParamList.get(1).getSpatialTableName().equals("SPAT_1"));
        assertTrue(importParamList.get(2).getSpatialTableName().equals("SPAT_2"));

        assertTrue(isSameFile(importParamList.get(0).getSourceURL(), OUTPUTDIR_RESOURCES));
        assertTrue(isSameFile(importParamList.get(1).getSourceURL(), OUTPUTDIR_RESOURCES + 1));
        assertTrue(isSameFile(importParamList.get(2).getSourceURL(), OUTPUTDIR_RESOURCES + 2));

        importParamList = new ArrayList<ImportParam>();
        Import.fillImportParamList("SPAT", "TILE", csvFileUrl, ";", ImportTyp.CSV, importParamList);
        assertTrue(importParamList.size() == 3);

        assertTrue(
                isSameFile(
                        importParamList.get(0).getSourceURL(), OUTPUTDIR_RESOURCES + "index.csv"));
        assertTrue(
                isSameFile(
                        importParamList.get(1).getSourceURL(),
                        OUTPUTDIR_RESOURCES + "1/index.csv"));
        assertTrue(
                isSameFile(
                        importParamList.get(2).getSourceURL(),
                        OUTPUTDIR_RESOURCES + "2/index.csv"));

        importParamList = new ArrayList<ImportParam>();
        Import.fillImportParamList(
                "SPAT", "TILE", shapeFileUrl, "LOCATION", ImportTyp.SHAPE, importParamList);
        assertTrue(importParamList.size() == 3);

        assertTrue(
                isSameFile(
                        importParamList.get(0).getSourceURL(), OUTPUTDIR_RESOURCES + "index.shp"));
        assertTrue(
                isSameFile(
                        importParamList.get(1).getSourceURL(),
                        OUTPUTDIR_RESOURCES + "1/index.shp"));
        assertTrue(
                isSameFile(
                        importParamList.get(2).getSourceURL(),
                        OUTPUTDIR_RESOURCES + "2/index.shp"));
    }

    /**
     * Test if files are the same by comparing their canonical name.
     *
     * @return true if files have the same canonical name
     */
    public static boolean isSameFile(String file1, String file2) throws Exception {
        return new File(file1).getCanonicalPath().equals(new File(file2).getCanonicalPath());
    }

    /**
     * Test if files are the same by comparing their canonical name.
     *
     * @return true if files have the same canonical name
     */
    public static boolean isSameFile(URL file1, String file2) throws Exception {
        return URLs.urlToFile(file1).getCanonicalPath().equals(new File(file2).getCanonicalPath());
    }

    /** Unit test for {{@link #isSameFile(String, String)}. */
    public void testIsSameFile() throws Exception {
        assertTrue(isSameFile("foo", "./foo"));
        assertTrue(isSameFile("foo", "bar/../foo"));
        assertFalse(isSameFile("foo", "bar"));
    }

    public void testCreateJoined() {
        JDBCAccess access = getJDBCAccess();

        for (int i = 0; i <= access.getNumOverviews(); i++) {
            ImageLevelInfo li = access.getLevelInfo(i);
            li.setTileTableName(li.getSpatialTableName());
        }

        try {
            Connection.prepareStatement(getDBDialect().getCreateMasterStatement()).execute();

            for (String tn : getSpatialTableNames()) {
                Connection.prepareStatement(getDBDialect().getCreateSpatialTableStatementJoined(tn))
                        .execute();

                String stmt = getDBDialect().getRegisterSpatialStatement(tn, getSrsId());

                if (stmt != null) {
                    executeRegister(stmt);
                }
            }

            Connection.commit();

            for (int i = 0; i < getTileTableNames().length; i++) {
                Import imp = null;
                if (i == 0) {
                    URL dirFileUrl = new URL("file:" + OUTPUTDIR_RESOURCES);

                    imp =
                            new Import(
                                    getDBDialect().getConfig(),
                                    new ImportParam(
                                            getSpatialTableNames()[i],
                                            getSpatialTableNames()[i],
                                            dirFileUrl,
                                            "tif",
                                            Import.ImportTyp.DIR),
                                    2,
                                    Connection);
                }
                if (i == 1) {
                    URL shapeFileUrl = new URL("file:" + OUTPUTDIR_RESOURCES + i + "/index.shp");
                    imp =
                            new Import(
                                    getDBDialect().getConfig(),
                                    new ImportParam(
                                            getSpatialTableNames()[i],
                                            getSpatialTableNames()[i],
                                            shapeFileUrl,
                                            "LOCATION",
                                            Import.ImportTyp.SHAPE),
                                    2,
                                    Connection);
                }
                if (i == 2) {
                    URL csvFileUrl = new URL("file:" + OUTPUTDIR_RESOURCES + i + "/index.csv");
                    imp =
                            new Import(
                                    getDBDialect().getConfig(),
                                    new ImportParam(
                                            getSpatialTableNames()[i],
                                            getSpatialTableNames()[i],
                                            csvFileUrl,
                                            ";",
                                            Import.ImportTyp.CSV),
                                    2,
                                    Connection);
                }
                imp.fillSpatialTable();
            }

            for (String tn : getSpatialTableNames()) {
                Connection.prepareStatement(getDBDialect().getCreateIndexStatement(tn)).execute();
            }

            Connection.commit();
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail(e.getMessage());
        }
    }

    protected void imageMosaic(
            String name,
            String configUrl,
            GeneralEnvelope envelope,
            int width,
            int heigth,
            boolean expectsData)
            throws IOException {
        imageMosaic(name, configUrl, envelope, width, heigth, null, null, null, expectsData);
    }

    protected void imageMosaic(
            String name,
            String configUrl,
            GeneralEnvelope envelope,
            int width,
            int heigth,
            Color bColor,
            Color transparentColor,
            CoordinateReferenceSystem crs,
            boolean expectsData)
            throws IOException {
        // Hints hints = new
        // Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM,CRS.parseWKT(M34_PRJ));
        Hints hints = null;

        if (crs != null) {
            hints = new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, crs);
        }

        AbstractGridFormat format = (AbstractGridFormat) GridFormatFinder.findFormat(configUrl);
        ImageMosaicJDBCReader reader = (ImageMosaicJDBCReader) format.getReader(configUrl, hints);

        ParameterValue<GridGeometry2D> gg = AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();

        if (envelope == null) {
            envelope = reader.getOriginalEnvelope();
        } else if (envelope.getCoordinateReferenceSystem() == null) {
            envelope.setCoordinateReferenceSystem(
                    reader.getOriginalEnvelope().getCoordinateReferenceSystem());
        }

        gg.setValue(
                new GridGeometry2D(
                        new GridEnvelope2D(new Rectangle(0, 0, width, heigth)), envelope));

        final ParameterValue<Color> outTransp =
                ImageMosaicJDBCFormat.OUTPUT_TRANSPARENT_COLOR.createValue();
        outTransp.setValue(
                (transparentColor == null)
                        ? ImageMosaicJDBCFormat.OUTPUT_TRANSPARENT_COLOR.getDefaultValue()
                        : transparentColor);
        final ParameterValue<Color> bgColor = ImageMosaicJDBCFormat.BACKGROUND_COLOR.createValue();
        bgColor.setValue(
                (bColor == null)
                        ? ImageMosaicJDBCFormat.BACKGROUND_COLOR.getDefaultValue()
                        : bColor);

        GridCoverage2D coverage =
                (GridCoverage2D) reader.read(new GeneralParameterValue[] {gg, outTransp, bgColor});

        if (expectsData) assertNotNull(coverage);
        else assertNull(coverage);

        if (coverage != null) {
            ImageIO.write(
                    coverage.getRenderedImage(),
                    "tif",
                    new File(getOutPutDir() + File.separator + name + ".tif"));
        }
    }

    protected abstract String getSubDir();

    protected abstract DBDialect getDBDialect();

    public void testImage1() {
        doTestImage1("image1");
    }

    public void testImage1Joined() {
        doTestImage1("image1_joined");
    }

    public void testFullExtent() {
        doFullExtent("fullExtent");
    }

    public void testFullExtentJoined() {
        doFullExtent("fullExtentJoined");
    }

    public void testNoData() {
        doNoData("nodData");
    }

    public void testNoDataJoined() {
        doNoData("noDataJoined");
    }

    public void testPartial() {
        doPartial("partial");
    }

    public void testPartialJoined() {
        doPartial("partialJoined");
    }

    public void testVienna() {
        doVienna("vienna");
    }

    public void testViennaJoined() {
        doVienna("viennaJoined");
    }

    public void testViennaEnv() {
        doViennaEnv("viennaEnv");
    }

    public void testViennaEnvJoined() {
        doViennaEnv("viennaEnvJoined");
    }

    private void doVienna(String name) {
        try {
            ENV_VIENNA.setCoordinateReferenceSystem(CRS.decode(CRSNAME));
            imageMosaic(name, getConfigUrl(), ENV_VIENNA, 500, 500, true);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    private void doViennaEnv(String name) {
        try {
            ENV_VIENNA2.setCoordinateReferenceSystem(CRS.decode(CRSNAME));
            imageMosaic(name, getConfigUrl(), ENV_VIENNA2, 500, 500, true);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    private void doTestImage1(String name) {
        try {
            ENV_1.setCoordinateReferenceSystem(CRS.decode(CRSNAME));
            imageMosaic(name, getConfigUrl(), ENV_1, 500, 250, true);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    private void doFullExtent(String name) {
        JDBCAccess access = getJDBCAccess();
        ImageLevelInfo li = access.getLevelInfo(access.getNumOverviews());

        double scale = li.getEnvelope().getWidth() / 400;
        GeneralEnvelope env =
                new GeneralEnvelope(
                        new double[] {li.getExtentMinX(), li.getExtentMinY()},
                        new double[] {li.getExtentMaxX(), li.getExtentMaxY()});

        try {
            env.setCoordinateReferenceSystem(CRS.decode(CRSNAME));
            imageMosaic(
                    name,
                    getConfigUrl(),
                    env,
                    400,
                    (int) (li.getEnvelope().getHeight() / scale),
                    true);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    private void doNoData(String name) {
        JDBCAccess access = getJDBCAccess();
        ImageLevelInfo li = access.getLevelInfo(access.getNumOverviews());

        GeneralEnvelope env =
                new GeneralEnvelope(
                        new double[] {li.getExtentMaxX() + DELTA, li.getExtentMaxY() + DELTA},
                        new double[] {
                            li.getExtentMaxX() + (DELTA * 2), li.getExtentMaxY() + (DELTA * 2)
                        });

        try {
            env.setCoordinateReferenceSystem(CRS.decode(CRSNAME));
            imageMosaic(name, getConfigUrl(), env, 400, 400, false);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    private void doPartial(String name) {
        JDBCAccess access = getJDBCAccess();
        ImageLevelInfo li = access.getLevelInfo(access.getNumOverviews());

        GeneralEnvelope env =
                new GeneralEnvelope(
                        new double[] {li.getExtentMaxX() - DELTA, li.getExtentMaxY() - DELTA},
                        new double[] {li.getExtentMaxX() + DELTA, li.getExtentMaxY() + DELTA});

        try {
            env.setCoordinateReferenceSystem(CRS.decode(CRSNAME));
            imageMosaic(name, getConfigUrl(), env, 400, 400, Color.green, null, null, true);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    public void setUp() throws Exception {
        super.setUp();

        if (fixture == null) {
            initFixture();
            createXMLConnectFragment();
        }

        if (fixture == null) {
            String propertiesName = getSubDir() + ".properties";
            if (LOGGER.isLoggable(Level.CONFIG)) {
                LOGGER.log(
                        Level.CONFIG,
                        "Fixture not found - make sure "
                                + propertiesName
                                + " copied from build tree resources directory");
                LOGGER.log(
                        Level.CONFIG,
                        "to Documents and Settings/userid/.geotools/imagemosaic  (Windows) ");
                LOGGER.log(Level.CONFIG, "or   ~/.geotools/imagemosiac (Unix) ");
            }
            throw new IOException("Fixture not found");
        }
    }

    protected String getFixtureId() {
        return "imagemosaic." + getSubDir();
    }

    protected File getFixtureFile() {
        File base = new File(System.getProperty("user.home") + File.separator + ".geotools");
        String fixtureId = getFixtureId();

        if (fixtureId == null) {
            return null;
        }

        File fixtureFile =
                new File(base, fixtureId.replace('.', File.separatorChar).concat(".properties"));

        return fixtureFile;
    }

    protected void initFixture() throws Exception {
        File fixtureFile = getFixtureFile();

        if ((fixtureFile != null) && fixtureFile.exists()) {
            InputStream input = new BufferedInputStream(new FileInputStream(fixtureFile));

            try {
                fixture = new Properties();
                fixture.load(input);
            } finally {
                input.close();
            }
        }
    }

    void createXMLConnectFragment() throws Exception {
        if (fixture == null) {
            return;
        }

        String host = fixture.getProperty("host");

        if (host != null) {
            host = host.trim();
        }

        String portString = fixture.getProperty("portnum");
        Integer port = null;

        if (portString != null) {
            port = Integer.valueOf(portString);
        }

        String dbName = fixture.getProperty("dbname");

        if (dbName != null) {
            dbName = dbName.trim();
        }

        String user = fixture.getProperty("user");

        if (user != null) {
            user = user.trim();
        }

        String password = fixture.getProperty("password");

        if (password != null) {
            password = password.trim();
        }

        PrintWriter w =
                new PrintWriter(
                        new FileOutputStream(OUTPUTDIR_RESOURCES + getXMLConnectFragmentName()));
        w.println("<connect>");
        w.println("     <dstype value=\"DBCP\"/>");
        w.println("     <username value=\"" + user + "\"/>");
        w.println("     <password value=\"" + password + "\"/>");
        w.println("     <jdbcUrl value=\"" + getJDBCUrl(host, port, dbName) + "\"/>");
        w.println("     <driverClassName value=\"" + getDriverClassName() + "\"/>");
        w.println("     <maxActive value=\"10\"/>");
        w.println("     <maxIdle value=\"0\"/>");
        w.println("</connect>");
        w.close();
    }

    public void testGetConnection() {
        Connection = null;

        try {
            Connection = getDBDialect().getConnection();
        } catch (Exception e) {
            Assert.fail("Error getting connection");
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
    }

    public void testCloseConnection() {
        if (Connection != null) {
            try {
                Connection.close();
            } catch (SQLException e) {
                Assert.fail("Error closing connection");
                java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            } finally {
                Connection = null;
            }
        }
    }

    void executeRegister(String stmt) throws SQLException {}

    void executeUnRegister(String stmt) throws SQLException {}

    protected abstract String getXMLConnectFragmentName();

    /** @return der full qulified java class name for the jdbc driver */
    protected abstract String getDriverClassName();

    /**
     * @param host the host name/ip address
     * @param port the tcpip port where the db is listening
     * @param dbName the name of the database
     * @return the db specific connect url
     */
    protected abstract String getJDBCUrl(String host, Integer port, String dbName);

    public void testOutputTransparentColor() {
        JDBCAccess access = getJDBCAccess();
        ImageLevelInfo li = access.getLevelInfo(access.getNumOverviews());

        GeneralEnvelope env =
                new GeneralEnvelope(
                        new double[] {li.getExtentMaxX() - DELTA, li.getExtentMaxY() - DELTA},
                        new double[] {li.getExtentMaxX() + DELTA, li.getExtentMaxY() + DELTA});

        try {
            env.setCoordinateReferenceSystem(CRS.decode(CRSNAME));
            imageMosaic(
                    "transparent", getConfigUrl(), env, 400, 400, null, Color.black, null, true);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    public void testOutputTransparentColor2() {
        JDBCAccess access = getJDBCAccess();
        ImageLevelInfo li = access.getLevelInfo(access.getNumOverviews());

        GeneralEnvelope env =
                new GeneralEnvelope(
                        new double[] {li.getExtentMinX() - DELTA, li.getExtentMinY() - DELTA},
                        new double[] {li.getExtentMinX() + DELTA, li.getExtentMinY() + DELTA});

        try {
            env.setCoordinateReferenceSystem(CRS.decode(CRSNAME));
            imageMosaic(
                    "transparent2",
                    getConfigUrl(),
                    env,
                    400,
                    400,
                    Color.GREEN,
                    Color.GREEN,
                    null,
                    true);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}
