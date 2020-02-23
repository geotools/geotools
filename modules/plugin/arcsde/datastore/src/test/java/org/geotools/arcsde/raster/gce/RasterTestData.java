/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.arcsde.raster.gce;

import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_16BIT_S;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_16BIT_U;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_1BIT;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_32BIT_REAL;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_32BIT_S;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_32BIT_U;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_4BIT;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_64BIT_REAL;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_8BIT_S;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_8BIT_U;

import com.esri.sde.sdk.client.SeColumnDefinition;
import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeCoordinateReference;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeExtent;
import com.esri.sde.sdk.client.SeInsert;
import com.esri.sde.sdk.client.SeQuery;
import com.esri.sde.sdk.client.SeRaster;
import com.esri.sde.sdk.client.SeRasterAttr;
import com.esri.sde.sdk.client.SeRasterBand;
import com.esri.sde.sdk.client.SeRasterColumn;
import com.esri.sde.sdk.client.SeRasterConstraint;
import com.esri.sde.sdk.client.SeRasterConsumer;
import com.esri.sde.sdk.client.SeRasterProducer;
import com.esri.sde.sdk.client.SeRasterRenderedImage;
import com.esri.sde.sdk.client.SeRegistration;
import com.esri.sde.sdk.client.SeRow;
import com.esri.sde.sdk.client.SeSqlConstruct;
import com.esri.sde.sdk.client.SeTable;
import com.esri.sde.sdk.pe.PeFactory;
import com.esri.sde.sdk.pe.PePCSDefs;
import com.esri.sde.sdk.pe.PeProjectedCS;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.geotools.arcsde.ArcSdeException;
import org.geotools.arcsde.data.TestData;
import org.geotools.arcsde.gce.producer.ArcSDERasterFloatProducerImpl;
import org.geotools.arcsde.gce.producer.ArcSDERasterOneBitPerBandProducerImpl;
import org.geotools.arcsde.gce.producer.ArcSDERasterOneBytePerBandProducerImpl;
import org.geotools.arcsde.gce.producer.ArcSDERasterProducer;
import org.geotools.arcsde.raster.info.RasterCellType;
import org.geotools.arcsde.session.ArcSDEConnectionConfig;
import org.geotools.arcsde.session.Command;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.session.ISessionPool;
import org.geotools.util.logging.Logging;

@SuppressWarnings({"nls", "deprecation"})
public class RasterTestData {

    private TestData testData;

    private static final Logger LOGGER = Logging.getLogger(RasterTestData.class);

    public void setUp() throws IOException {
        // load a raster dataset into SDE
        testData = new TestData();
        testData.setUp();
    }

    public void tearDown() throws Exception {
        // destroy all sample tables;
        // for (RasterTableName table : RasterTableName.values()) {
        // String tableName = getRasterTableName(table);
        // testData.deleteTable(tableName);
        // }
        testData.tearDown(false, true);
    }

    public void deleteTable(final String tableName) throws Exception {
        testData.deleteTable(tableName, false);
    }

    public ISessionPool getConnectionPool() throws IOException {
        return testData.getConnectionPool();
    }

    public String getRasterTableName(
            final RasterCellType cellType, final int numBands, final boolean colorMapped)
            throws IOException {
        String testTableName = getRasterTableName(cellType, numBands);
        if (colorMapped) {
            testTableName += "_CM";
        }
        return testTableName;
    }

    public String getRasterTableName(final RasterCellType cellType, final int numBands)
            throws IOException {
        String typeString = cellType.toString();
        typeString = typeString.substring("TYPE_".length());
        String testTableName =
                testData.getTempTableName() + "_" + cellType + "_" + numBands + "_BAND";
        return testTableName;
    }

    public String createCoverageUrl(final RasterCellType cellType, final int numBands)
            throws IOException {
        final String rasterTableName = getRasterTableName(cellType, numBands);
        return createCoverageUrl(rasterTableName);
    }

    public String createCoverageUrl(
            final RasterCellType cellType, final int numBands, final boolean colorMapped)
            throws IOException {
        final String rasterTableName = getRasterTableName(cellType, numBands, colorMapped);
        return createCoverageUrl(rasterTableName);
    }

    public String createCoverageUrl(final String rasterTableName) throws IOException {
        final ArcSDEConnectionConfig config = getConnectionPool().getConfig();
        String url =
                "sde://"
                        + config.getUserName()
                        + ":"
                        + config.getPassword()
                        + "@"
                        + config.getServerName()
                        + ":"
                        + config.getPortNumber()
                        + "/"
                        + config.getDatabaseName()
                        + "#"
                        + rasterTableName;
        return url;
    }

    public String getRasterTestDataProperty(String propName) {
        Serializable val = testData.getConProps().get(propName);
        return val == null ? null : String.valueOf(val);
    }

    // public void load1bitRaster() throws Exception {
    // final String tableName = getRasterTableName(RasterTableName.ONEBIT);
    // final int numberOfBands = 1;
    // final int pixelType = SeRaster.SE_PIXEL_TYPE_1BIT;
    // final boolean pyramiding = true;
    // final boolean skipLevelOne = false;
    // final int interpolationType = SeRaster.SE_INTERPOLATION_NEAREST;
    // final IndexColorModel colorModel = null;
    // loadTestRaster(tableName, numberOfBands, pixelType, colorModel, pyramiding, skipLevelOne,
    // interpolationType);
    // }

    /**
     * Loads the 1bit raster test data into the table given in {@link
     * RasterTestData#get1bitRasterTableName()}
     */
    public String load1bitRaster() throws Exception {
        ISession session = getConnectionPool().getSession();
        final String tableName = getRasterTableName(TYPE_1BIT, 1);

        // clean out the table if it's currently in-place
        testData.deleteTable(tableName);
        // build the base business table. We'll add the raster data to it in a bit
        createRasterBusinessTempTable(tableName, session);
        session.dispose();

        SeExtent imgExtent = new SeExtent(231000, 898000, 231000 + 500, 898000 + 500);
        SeCoordinateReference crs =
                getSeCRSFromPeProjectedCSId(PePCSDefs.PE_PCS_NAD_1983_HARN_MA_M);
        String rasterFilename = (String) testData.getConProps().get("sampledata.onebitraster");
        ArcSDERasterProducer producer = new ArcSDERasterOneBitPerBandProducerImpl();

        importRasterImage(
                tableName, crs, rasterFilename, SeRaster.SE_PIXEL_TYPE_1BIT, imgExtent, producer);

        return tableName;
    }

    /**
     * Loads the 1bit raster test data into the table given in {@link
     * RasterTestData#get1bitRasterTableName()}
     */
    public String loadRGBRaster() throws Exception {
        final String tableName = getRasterTableName(TYPE_8BIT_U, 3);

        // clean out the table if it's currently in-place
        testData.deleteTable(tableName);
        // build the base business table. We'll add the raster data to it in a bit
        ISession session = getConnectionPool().getSession();
        try {
            createRasterBusinessTempTable(tableName, session);
        } finally {
            session.dispose();
        }

        SeExtent imgExtent = new SeExtent(231000, 898000, 231000 + 501, 898000 + 501);
        SeCoordinateReference crs =
                getSeCRSFromPeProjectedCSId(PePCSDefs.PE_PCS_NAD_1983_HARN_MA_M);
        String rasterFilename = (String) testData.getConProps().get("sampledata.rgbraster");
        ArcSDERasterProducer prod = new ArcSDERasterOneBytePerBandProducerImpl();

        importRasterImage(
                tableName, crs, rasterFilename, SeRaster.SE_PIXEL_TYPE_8BIT_U, imgExtent, prod);

        return tableName;
    }

    public String loadRGBColorMappedRaster() throws Exception {
        final String tableName = getRasterTableName(TYPE_8BIT_U, 1, true);

        // clean out the table if it's currently in-place
        testData.deleteTable(tableName);

        // build the base business table. We'll add the raster data to it in a bit
        // Note that this DOESN'T LOAD THE COLORMAP RIGHT NOW.
        ISession session = getConnectionPool().getSession();
        try {
            createRasterBusinessTempTable(tableName, session);
        } finally {
            session.dispose();
        }

        SeExtent imgExtent = new SeExtent(231000, 898000, 231000 + 500, 898000 + 500);
        SeCoordinateReference crs =
                getSeCRSFromPeProjectedCSId(PePCSDefs.PE_PCS_NAD_1983_HARN_MA_M);
        String rasterFilename =
                (String) testData.getConProps().get("sampledata.rgbraster-colormapped");
        ArcSDERasterProducer prod = new ArcSDERasterOneBytePerBandProducerImpl();

        importRasterImage(
                tableName, crs, rasterFilename, SeRaster.SE_PIXEL_TYPE_8BIT_U, imgExtent, prod);
        return tableName;
    }

    public String loadOneByteGrayScaleRaster() throws Exception {
        final String tableName = getRasterTableName(TYPE_8BIT_U, 1);

        // clean out the table if it's currently in-place
        testData.deleteTable(tableName);
        // build the base business table. We'll add the raster data to it in a bit
        // Note that this DOESN'T LOAD THE COLORMAP RIGHT NOW.
        ISession session = getConnectionPool().getSession();
        try {
            createRasterBusinessTempTable(tableName, session);
        } finally {
            session.dispose();
        }

        SeExtent imgExtent = new SeExtent(231000, 898000, 231000 + 500, 898000 + 500);
        SeCoordinateReference crs =
                getSeCRSFromPeProjectedCSId(PePCSDefs.PE_PCS_NAD_1983_HARN_MA_M);
        String rasterFilename =
                (String) testData.getConProps().get("sampledata.onebyteonebandraster");
        ArcSDERasterProducer prod = new ArcSDERasterOneBytePerBandProducerImpl();

        importRasterImage(
                tableName, crs, rasterFilename, SeRaster.SE_PIXEL_TYPE_8BIT_U, imgExtent, prod);

        return tableName;
    }

    public String loadFloatRaster() throws Exception {
        final String tableName = getRasterTableName(TYPE_32BIT_REAL, 1);

        // clean out the table if it's currently in-place
        testData.deleteTable(tableName);
        // build the base business table. We'll add the raster data to it in a bit
        // Note that this DOESN'T LOAD THE COLORMAP RIGHT NOW.
        ISession session = getConnectionPool().getSession();
        try {
            createRasterBusinessTempTable(tableName, session);
        } finally {
            session.dispose();
        }

        SeExtent imgExtent = new SeExtent(245900, 899600, 246300, 900000);
        SeCoordinateReference crs =
                getSeCRSFromPeProjectedCSId(PePCSDefs.PE_PCS_NAD_1983_HARN_MA_M);
        String rasterFilename = (String) testData.getConProps().get("sampledata.floatraster");
        ArcSDERasterProducer prod = new ArcSDERasterFloatProducerImpl();

        importRasterImage(
                tableName, crs, rasterFilename, SeRaster.SE_PIXEL_TYPE_32BIT_REAL, imgExtent, prod);

        return tableName;
    }

    public SeCoordinateReference getSeCRSFromPeProjectedCSId(int PeProjectedCSId) {
        SeCoordinateReference crs;
        try {
            PeProjectedCS pcs = (PeProjectedCS) PeFactory.factory(PeProjectedCSId);
            crs = new SeCoordinateReference();
            crs.setCoordSysByDescription(pcs.toString());
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return crs;
    }

    public void createRasterBusinessTempTable(final String tableName, final ISession conn)
            throws Exception {
        conn.issue(
                new Command<Void>() {

                    @Override
                    public Void execute(ISession session, SeConnection connection)
                            throws SeException, IOException {
                        SeColumnDefinition[] colDefs = new SeColumnDefinition[1];
                        SeTable table = new SeTable(connection, tableName);

                        // first column to be SDE managed feature id
                        colDefs[0] =
                                new SeColumnDefinition(
                                        "ROW_ID", SeColumnDefinition.TYPE_INTEGER, 10, 0, false);
                        table.create(colDefs, testData.getConfigKeyword());

                        /*
                         * Register the column to be used as feature id and managed by sde
                         */
                        SeRegistration reg = new SeRegistration(connection, table.getName());
                        LOGGER.fine(
                                "setting rowIdColumnName to ROW_ID in table " + reg.getTableName());
                        reg.setRowIdColumnName("ROW_ID");
                        final int rowIdColumnType =
                                SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_SDE;
                        reg.setRowIdColumnType(rowIdColumnType);
                        reg.alter();
                        return null;
                    }
                });
    }

    public void importRasterImage(
            final String tableName,
            SeCoordinateReference crs,
            final String rasterFilename,
            final int sePixelType,
            SeExtent extent,
            ArcSDERasterProducer prod)
            throws Exception {
        importRasterImage(tableName, crs, rasterFilename, sePixelType, extent, prod, null);
    }

    public void importRasterImage(
            final String tableName,
            final SeCoordinateReference crs,
            final String rasterFilename,
            final int sePixelType,
            final SeExtent extent,
            final ArcSDERasterProducer prod,
            final IndexColorModel colorModel)
            throws Exception {

        Command<Void> importRasterCmd =
                new Command<Void>() {
                    @Override
                    public Void execute(ISession session, SeConnection connection)
                            throws SeException, IOException {
                        createRasterColumn(tableName, crs, session);

                        IndexColorModel indexCM = colorModel;

                        // now start loading the actual raster data
                        BufferedImage sampleImage =
                                ImageIO.read(
                                        org.geotools.test.TestData.getResource(
                                                null, rasterFilename));
                        {
                            ColorModel imgColorModel = sampleImage.getColorModel();
                            if (imgColorModel instanceof IndexColorModel) {
                                indexCM = (IndexColorModel) imgColorModel;
                            }
                        }
                        int imageWidth = sampleImage.getWidth(),
                                imageHeight = sampleImage.getHeight();

                        SeRasterAttr attr = new SeRasterAttr(true);
                        attr.setImageSize(
                                imageWidth,
                                imageHeight,
                                sampleImage.getSampleModel().getNumBands());
                        int tileWidth = imageWidth >> 3; // 128;
                        int tileHeight = imageHeight >> 3; // 128;
                        attr.setTileSize(tileWidth, tileHeight);
                        attr.setPixelType(sePixelType);
                        attr.setCompressionType(SeRaster.SE_COMPRESSION_NONE);

                        int maxLevels = (imageWidth / (2 * tileWidth)) - 1;
                        attr.setPyramidInfo(maxLevels, false, SeRaster.SE_INTERPOLATION_BILINEAR);
                        attr.setMaskMode(false);
                        attr.setImportMode(false);

                        attr.setExtent(extent);

                        // attr.setImageOrigin();

                        prod.setSeRasterAttr(attr);
                        prod.setSourceImage(sampleImage);
                        attr.setRasterProducer(prod);

                        try {
                            SeInsert insert = new SeInsert(connection);
                            insert.intoTable(tableName, new String[] {"RASTER"});
                            // no buffered writes on raster loads
                            insert.setWriteMode(false);
                            SeRow row = insert.getRowToSet();
                            row.setRaster(0, attr);

                            insert.execute();
                            insert.close();
                        } catch (SeException se) {
                            java.util.logging.Logger.getGlobal()
                                    .log(java.util.logging.Level.INFO, "", se);
                            throw se;
                        }

                        // if there's a colormap to insert, let's add that too
                        if (indexCM != null) {
                            attr =
                                    getRasterAttributes(
                                            connection,
                                            tableName,
                                            new Rectangle(0, 0, 0, 0),
                                            0,
                                            new int[] {1});
                            final int numEntries = indexCM.getMapSize();
                            // number of colors, including alpha, if any
                            final int numBanks = indexCM.getNumComponents();
                            final int colorMapType =
                                    numBanks == 3
                                            ? SeRaster.SE_COLORMAP_RGB
                                            : SeRaster.SE_COLORMAP_RGBA;
                            DataBufferByte dataBuffer = new DataBufferByte(numEntries, numBanks);
                            for (int elem = 0; elem < numEntries; elem++) {
                                dataBuffer.setElem(0, elem, indexCM.getRed(elem));
                                dataBuffer.setElem(1, elem, indexCM.getGreen(elem));
                                dataBuffer.setElem(2, elem, indexCM.getBlue(elem));
                                if (numBanks == 4) {
                                    dataBuffer.setElem(3, elem, indexCM.getAlpha(elem));
                                }
                            }
                            SeRaster raster;
                            try {
                                raster = attr.getRasterInfo();
                            } catch (CloneNotSupportedException e) {
                                throw new RuntimeException(e);
                            }
                            SeRasterBand[] bands = raster.getBands();
                            SeRasterBand band = bands[0];
                            band.setColorMap(colorMapType, dataBuffer);
                            band.alter();
                        }
                        return null;
                    }
                };
        final ISession session = getConnectionPool().getSession();
        try {
            session.issue(importRasterCmd);
        } finally {
            session.dispose();
        }
    }

    public void loadUshortRaster() throws Exception {
        final RasterCellType pixelType = TYPE_16BIT_U;
        final int numberOfBands = 1;
        final String tableName = getRasterTableName(pixelType, numberOfBands);
        final boolean pyramiding = true;
        final boolean skipLevelOne = false;
        final int interpolationType = SeRaster.SE_INTERPOLATION_NEAREST;
        final IndexColorModel colorModel = null;
        final int imageWidth = 256;
        final int imageHeight = 256;

        try {
            loadTestRaster(
                    tableName,
                    numberOfBands,
                    imageWidth,
                    imageHeight,
                    pixelType,
                    colorModel,
                    pyramiding,
                    skipLevelOne,
                    interpolationType,
                    null);
        } catch (SeException e) {
            throw new ArcSdeException(e);
        }
    }

    public String loadShortRaster() throws Exception {
        final int numberOfBands = 1;
        final RasterCellType pixelType = TYPE_16BIT_S;
        final String tableName = getRasterTableName(pixelType, numberOfBands);
        final boolean pyramiding = true;
        final boolean skipLevelOne = false;
        final int interpolationType = SeRaster.SE_INTERPOLATION_NEAREST;
        final IndexColorModel colorModel = null;
        final int imageWidth = 256;
        final int imageHeight = 256;

        try {
            loadTestRaster(
                    tableName,
                    numberOfBands,
                    imageWidth,
                    imageHeight,
                    pixelType,
                    colorModel,
                    pyramiding,
                    skipLevelOne,
                    interpolationType,
                    null);
        } catch (SeException e) {
            throw new ArcSdeException(e);
        }
        return tableName;
    }

    /**
     * Creates a 4 band raster, with 8 bit unsigned pixel type and no colormap, and pyramid
     *
     * @see {@link #loadTestRaster(String, int, int, IndexColorModel, boolean, boolean, int)
     *
     */
    public String loadRGBARaster() throws Exception {
        final int numberOfBands = 4;
        final RasterCellType pixelType = TYPE_8BIT_U;
        final String tableName = getRasterTableName(pixelType, numberOfBands);
        final boolean pyramiding = true;
        final boolean skipLevelOne = false;
        final int interpolationType = SeRaster.SE_INTERPOLATION_NEAREST;
        final IndexColorModel colorModel = null;
        final int imageWidth = 256;
        final int imageHeight = 256;
        loadTestRaster(
                tableName,
                numberOfBands,
                imageWidth,
                imageHeight,
                pixelType,
                colorModel,
                pyramiding,
                skipLevelOne,
                interpolationType,
                null);
        return tableName;
    }

    public String load8bitUnsignedColorMappedRaster() throws Exception {
        final int numberOfBands = 1;
        final RasterCellType pixelType = TYPE_8BIT_U;
        final String tableName = getRasterTableName(pixelType, numberOfBands);
        final boolean pyramiding = true;
        final boolean skipLevelOne = false;
        final int interpolationType = SeRaster.SE_INTERPOLATION_NEAREST;

        int cmBits = 8;
        int cmSize = 1;
        byte[] cmR = {0x00};
        byte[] cmG = {0x00};
        byte[] cmB = {(byte) 0xFF};
        final IndexColorModel colorModel = new IndexColorModel(cmBits, cmSize, cmR, cmG, cmB);
        final int imageWidth = 256;
        final int imageHeight = 256;

        loadTestRaster(
                tableName,
                numberOfBands,
                imageWidth,
                imageHeight,
                pixelType,
                colorModel,
                pyramiding,
                skipLevelOne,
                interpolationType,
                null);

        return tableName;
    }

    /**
     * Creates a a raster in the database with NO pyramiding, some default settings and the provided
     * parameters.
     *
     * <p>Default settings
     *
     * <ul>
     *   <li>CRS: PePCSDefs.PE_PCS_NAD_1983_HARN_MA_M
     *   <li>Extent: minx=0, miny=0, maxx=512, maxy=512
     *   <li>Width: 256
     *   <li>Height: 256
     *   <li>Tile size: 64x64 (being less than the recommended minimum of 128, but ok for our
     *       testing purposes)
     *   <li>Compression: none
     *   <li>Pyramid: none
     * </ul>
     *
     * @param tableName the name of the table to create
     * @param numberOfBands the number of bands of the raster
     * @param pixelType the pixel (cell) depth of the raster bands (one of the {@code
     *     SeRaster#SE_PIXEL_TYPE_*} constants)
     * @param colorModel the color model to apply to the raster, may be {@code null}. A non null
     *     value adds as precondition that {@code numberOfBands == 1}
     */
    public void loadTestRaster(
            final String tableName,
            final int numberOfBands,
            final RasterCellType pixelType,
            final IndexColorModel colorModel)
            throws Exception {
        final boolean pyramiding = true;
        final boolean skipLevelOne = false;
        final int interpolationType = SeRaster.SE_INTERPOLATION_BILINEAR;
        final int imageWidth = 256;
        final int imageHeight = 256;

        loadTestRaster(
                tableName,
                numberOfBands,
                imageWidth,
                imageHeight,
                pixelType,
                colorModel,
                pyramiding,
                skipLevelOne,
                interpolationType,
                null);
    }

    /**
     * Creates a simple raster catalog, extent and grid range coincide for easier assertions.
     *
     * <p>The catalog rasters are not inserted in left-right, top-bottom direction though, as it may
     * happen in real life.
     *
     * <p>The grid extent layout is as follows (invert Y for pixel layout):
     *
     * <pre>
     *   0,512       256,512       512,512
     *       _____________________
     *      |          |          |
     *      |          |          |
     *      |     3    |    1     |
     *      |          |          |
     *      |__________|__________| 512,256
     *  0,256          |256,256   |
     *                 |          |
     *                 |    2     |
     *                 |          |
     *                 |__________|
     *    0,0       256,0          512,0
     * </pre>
     */
    public String loadRasterCatalog() throws Exception {
        final int numberOfBands = 3;
        final RasterCellType pixelType = TYPE_8BIT_U;
        final String tableName = getRasterTableName(pixelType, numberOfBands) + "_CAT";

        {
            // clean out the table if it's currently in-place
            testData.deleteTable(tableName);
            // build the base business table. We'll add the raster data to it in a bit
            // Note that this DOESN'T LOAD THE COLORMAP RIGHT NOW.
            ISession session = getConnectionPool().getSession();
            try {
                createRasterBusinessTempTable(tableName, session);
            } finally {
                session.dispose();
            }
        }

        final SeCoordinateReference crs =
                getSeCRSFromPeProjectedCSId(PePCSDefs.PE_PCS_NAD_1983_HARN_MA_M);

        final ISession session = getConnectionPool().getSession();
        try {
            createRasterColumn(tableName, crs, session);

            int imageWidth;
            int imageHeight;
            SeExtent extent;
            int maxLevels;

            maxLevels = 2;
            extent = new SeExtent(256, 0, 512, 256);
            imageWidth = 228;
            imageHeight = 228;
            addRasterToCatalog(
                    session,
                    tableName,
                    crs,
                    numberOfBands,
                    imageWidth,
                    imageHeight,
                    pixelType,
                    maxLevels,
                    extent);

            maxLevels = 3;
            extent = new SeExtent(0, 256, 256, 512);
            imageWidth = 456;
            imageHeight = 456;
            addRasterToCatalog(
                    session,
                    tableName,
                    crs,
                    numberOfBands,
                    imageWidth,
                    imageHeight,
                    pixelType,
                    maxLevels,
                    extent);

            maxLevels = 3;
            extent = new SeExtent(256, 256, 512, 512);
            imageWidth = 532;
            imageHeight = 532;
            addRasterToCatalog(
                    session,
                    tableName,
                    crs,
                    numberOfBands,
                    imageWidth,
                    imageHeight,
                    pixelType,
                    maxLevels,
                    extent);
        } finally {
            session.dispose();
        }

        return tableName;
    }

    private void addRasterToCatalog(
            ISession session,
            String tableName,
            SeCoordinateReference crs,
            int numberOfBands,
            int imageWidth,
            int imageHeight,
            RasterCellType pixelType,
            int maxLevels,
            SeExtent extent)
            throws IOException {

        IndexColorModel colorModel = null;
        boolean skipLevelOne = false;
        int interpolationType = SeRaster.SE_INTERPOLATION_BICUBIC;

        addRasterAttribute(
                tableName,
                numberOfBands,
                imageWidth,
                imageHeight,
                pixelType,
                colorModel,
                maxLevels,
                skipLevelOne,
                interpolationType,
                extent,
                session);
    }

    /**
     * Creates a a raster in the database with some default settings and the provided parameters.
     *
     * <p>Default settings
     *
     * <ul>
     *   <li>CRS: PePCSDefs.PE_PCS_NAD_1983_HARN_MA_M
     *   <li>Extent: minx=0, miny=0, maxx=512, maxy=512
     *   <li>Width: 256
     *   <li>Height: 256
     *   <li>Tile size: 64x64 (being less than the recommended minimum of 128, but ok for our
     *       testing purposes)
     *   <li>Compression: none
     * </ul>
     *
     * @param tableName the name of the table to create
     * @param numberOfBands the number of bands of the raster
     * @param pixelType the pixel (cell) depth of the raster bands (one of the {@code
     *     SeRaster#SE_PIXEL_TYPE_*} constants)
     * @param colorModel the color model to apply to the raster, may be {@code null}. A non null
     *     value adds as precondition that {@code numberOfBands == 1}
     * @param pyramiding whether to create tiles or not for the raster. If {@code true} and {@code
     *     skipLevelOne == true} a pyramid with three levels will be created, avoiding to create the
     *     pyramid tiles for level 0 (same dimension as the source raster). If {@code skipLevelOne
     *     == false}, even for level 0 the pyramid tiles will be created, that is, four levels.
     * @param skipLevelOne only relevant if {@code pyramiding == true}, {@code true} indicates not
     *     to create pyramid tiles for the first level, since its equal in dimension than the source
     *     raster
     * @param interpolationType only relevant if {@code pyramiding == true}, indicates which
     *     interpolation method to use in building the pyramid tiles. Shall be one of {@link
     *     SeRaster#SE_INTERPOLATION_BICUBIC}, {@link SeRaster#SE_INTERPOLATION_BILINEAR}, {@link
     *     SeRaster#SE_INTERPOLATION_NEAREST}. Otherwise {@link SeRaster#SE_INTERPOLATION_NONE}
     */
    public void loadTestRaster(
            final String tableName,
            final int numberOfBands,
            final int imageWidth,
            final int imageHeight,
            final RasterCellType pixelType,
            final IndexColorModel colorModel,
            final boolean pyramiding,
            final boolean skipLevelOne,
            final int interpolationType,
            final Integer forceNumLevels)
            throws Exception {

        if (colorModel != null && numberOfBands > 1) {
            throw new IllegalArgumentException(
                    "Indexed rasters shall contain a single band. numberOfBands = "
                            + numberOfBands);
        }
        {
            // clean out the table if it's currently in-place
            testData.deleteTable(tableName);
            // build the base business table. We'll add the raster data to it in a bit
            // Note that this DOESN'T LOAD THE COLORMAP RIGHT NOW.
            ISession session = getConnectionPool().getSession();
            try {
                createRasterBusinessTempTable(tableName, session);
            } finally {
                session.dispose();
            }
        }

        final SeExtent extent = new SeExtent(0, 0, 2 * imageWidth, 2 * imageHeight);
        final SeCoordinateReference crs =
                getSeCRSFromPeProjectedCSId(PePCSDefs.PE_PCS_NAD_1983_HARN_MA_M);

        final ISession session = getConnectionPool().getSession();
        try {
            createRasterColumn(tableName, crs, session);

            int maxLevels;
            if (pyramiding) {
                int tileWidth = imageWidth >> 4;
                if (forceNumLevels == null) {
                    maxLevels = (imageWidth / (4 * tileWidth)) - 1;
                } else {
                    maxLevels = forceNumLevels;
                }
            } else {
                maxLevels = 0;
            }
            addRasterAttribute(
                    tableName,
                    numberOfBands,
                    imageWidth,
                    imageHeight,
                    pixelType,
                    colorModel,
                    maxLevels,
                    skipLevelOne,
                    interpolationType,
                    extent,
                    session);

        } finally {
            session.dispose();
        }
    }

    private void addRasterAttribute(
            final String tableName,
            final int numberOfBands,
            final int imageWidth,
            final int imageHeight,
            final RasterCellType pixelType,
            final IndexColorModel colorModel,
            final int maxPyramidLevel,
            final boolean skipLevelOne,
            final int interpolationType,
            final SeExtent extent,
            final ISession session)
            throws IOException {

        // attr.setImageOrigin();

        final SeRasterProducer prod =
                new SeRasterProducer() {
                    public void addConsumer(SeRasterConsumer consumer) {}

                    public boolean isConsumer(SeRasterConsumer consumer) {
                        return false;
                    }

                    public void removeConsumer(SeRasterConsumer consumer) {}

                    /**
                     * Note that due to some synchronization problems inherent in the SDE api code,
                     * the startProduction() method MUST return before consumer.setScanLines() or
                     * consumer.setRasterTiles() is called. Hence the thread implementation.
                     */
                    public void startProduction(final SeRasterConsumer consumer) {
                        if (!(consumer instanceof SeRasterRenderedImage)) {
                            throw new IllegalArgumentException(
                                    "You must set SeRasterAttr.setImportMode(false) to "
                                            + "load data using this SeProducer implementation.");
                        }

                        Thread runme =
                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            PixelSampler sampler =
                                                    PixelSampler.getSampler(pixelType);
                                            // for each band...
                                            for (int bandN = 0; bandN < numberOfBands; bandN++) {
                                                final byte[] imgBandData;
                                                imgBandData =
                                                        sampler.getImgBandData(
                                                                imageWidth,
                                                                imageHeight,
                                                                bandN,
                                                                numberOfBands);
                                                consumer.setScanLines(
                                                        imageHeight, imgBandData, null);
                                                consumer.rasterComplete(
                                                        SeRasterConsumer.SINGLEFRAMEDONE);
                                            }
                                            consumer.rasterComplete(
                                                    SeRasterConsumer.STATICIMAGEDONE);
                                        } catch (Exception se) {
                                            java.util.logging.Logger.getGlobal()
                                                    .log(java.util.logging.Level.INFO, "", se);
                                            consumer.rasterComplete(SeRasterConsumer.IMAGEERROR);
                                        }
                                    }
                                };
                        runme.start();
                    }
                };

        session.issue(
                new Command<Void>() {
                    @Override
                    public Void execute(ISession session, SeConnection connection)
                            throws SeException, IOException {
                        {
                            SeRasterAttr attr = new SeRasterAttr(true);
                            attr.setImageSize(imageWidth, imageHeight, numberOfBands);
                            int tileWidth = imageWidth >> 4;
                            int tileHeight = imageHeight >> 4;
                            attr.setTileSize(
                                    tileWidth, tileHeight); // this is lower than the recommended
                            // minimum
                            // of 128,128 but
                            // it's ok for our testing purposes
                            attr.setPixelType(pixelType.getSeRasterPixelType());
                            attr.setCompressionType(SeRaster.SE_COMPRESSION_NONE);

                            attr.setPyramidInfo(maxPyramidLevel, skipLevelOne, interpolationType);

                            attr.setInterleave(true, SeRaster.SE_RASTER_INTERLEAVE_BIP);
                            attr.setMaskMode(false);
                            attr.setImportMode(false);

                            attr.setExtent(extent);
                            attr.setRasterProducer(prod);

                            SeInsert insert = new SeInsert(connection);
                            insert.intoTable(tableName, new String[] {"RASTER"});
                            // no buffered writes on raster loads
                            insert.setWriteMode(false);
                            SeRow row = insert.getRowToSet();
                            row.setRaster(0, attr);
                            // import the data
                            insert.execute();
                            insert.close();
                        }
                        // if there's a colormap to insert, let's add that too
                        if (colorModel != null) {
                            try {
                                SeQuery query =
                                        new SeQuery(
                                                connection,
                                                new String[] {"RASTER"},
                                                new SeSqlConstruct(tableName));
                                query.prepareQuery();
                                query.execute();
                                SeRow row = query.fetch();
                                SeRasterAttr attr = row.getRaster(0);
                                SeRaster raster;
                                try {
                                    raster = attr.getRasterInfo();
                                } catch (CloneNotSupportedException e) {
                                    throw new RuntimeException(e);
                                }
                                SeRasterBand band1 = raster.getBands()[0];
                                final int numEntries = colorModel.getMapSize();
                                // number of colors, including alpha, if any
                                final int numBanks = colorModel.getNumComponents();
                                final int colorMapType =
                                        numBanks == 3
                                                ? SeRaster.SE_COLORMAP_RGB
                                                : SeRaster.SE_COLORMAP_RGBA;
                                final int dataType = pixelType.getDataBufferType();
                                DataBuffer dataBuffer;
                                if (DataBuffer.TYPE_BYTE == dataType) {
                                    dataBuffer = new DataBufferByte(numEntries, numBanks);
                                } else if (DataBuffer.TYPE_USHORT == dataType) {
                                    /*
                                     * beware we're using DataBufferShort instead of DataBufferUShort as
                                     * that's what the esri api expects
                                     */
                                    dataBuffer = new DataBufferShort(numEntries, numBanks);
                                } else {
                                    throw new IllegalArgumentException("data type: " + pixelType);
                                }
                                for (int elem = 0; elem < numEntries; elem++) {
                                    dataBuffer.setElem(0, colorModel.getRed(elem));
                                    dataBuffer.setElem(1, colorModel.getGreen(elem));
                                    dataBuffer.setElem(2, colorModel.getBlue(elem));
                                    if (numBanks == 4) {
                                        dataBuffer.setElem(3, colorModel.getAlpha(elem));
                                    }
                                }
                                band1.setColorMap(colorMapType, dataBuffer);
                                band1.alter();
                            } catch (SeException e) {
                                java.util.logging.Logger.getGlobal()
                                        .log(java.util.logging.Level.INFO, "", e);
                                throw new ArcSdeException(e);
                            }
                        }
                        return null;
                    }
                });
    }

    private void createRasterColumn(
            final String tableName, final SeCoordinateReference crs, final ISession session)
            throws IOException {
        // much of this code is from
        // http://edndoc.esri.com/arcsde/9.2/concepts/rasters/dataloading/dataloading.htm
        session.issue(
                new Command<Void>() {

                    @Override
                    public Void execute(ISession session, SeConnection connection)
                            throws SeException, IOException {
                        SeRasterColumn rasCol = new SeRasterColumn(connection);
                        rasCol.setTableName(tableName);
                        rasCol.setDescription("Sample geotools ArcSDE raster test-suite data.");
                        rasCol.setRasterColumnName("RASTER");
                        rasCol.setCoordRef(crs);
                        rasCol.setConfigurationKeyword(testData.getConfigKeyword());

                        rasCol.create();
                        return null;
                    }
                });
    }

    /** convenience method to test if two images are identical in their RGB pixel values */
    public static boolean imageEquals(RenderedImage image, String fileName) throws IOException {
        InputStream in = org.geotools.test.TestData.url(null, fileName).openStream();
        BufferedImage expected = ImageIO.read(in);

        return imageEquals(image, expected);
    }

    public static boolean imageEquals(RenderedImage image1, RenderedImage image2) {
        return imageEquals(image1, image2, true);
    }

    /** convenience method to test if two images are identical in their RGB pixel values */
    public static boolean imageEquals(
            RenderedImage image1, RenderedImage image2, boolean ignoreAlpha) {

        int skipBand = -1;
        if (ignoreAlpha) {
            skipBand = 3;
        }

        for (int b = 0; b < image1.getData().getNumBands(); b++) {
            if (b == skipBand) continue;
            int[] img1data =
                    image1.getData()
                            .getSamples(
                                    0,
                                    0,
                                    image1.getWidth(),
                                    image1.getHeight(),
                                    b,
                                    new int[image1.getHeight() * image1.getWidth()]);
            int[] img2data =
                    image2.getData()
                            .getSamples(
                                    0,
                                    0,
                                    image1.getWidth(),
                                    image1.getHeight(),
                                    b,
                                    new int[image1.getHeight() * image1.getWidth()]);

            if (!Arrays.equals(img1data, img2data)) {
                // try to figure out which pixel (exactly) was different
                for (int i = 0; i < img1data.length; i++) {
                    if (img1data[i] != img2data[i]) {
                        final int x = i % image1.getWidth();
                        final int y = i / image1.getHeight();
                        LOGGER.warning(
                                "pixel "
                                        + i
                                        + " (possibly "
                                        + x
                                        + ","
                                        + y
                                        + ") differs: "
                                        + img1data[i]
                                        + " != "
                                        + img2data[i]);
                        return false;
                    }
                }
            }

            /*
             * for (int xpos = 0; xpos < image1.getWidth(); xpos++) { // System.out.println("checking
             * column " + xpos); int[] img1data = image1.getData().getSamples(xpos, 0, 1,
             * image1.getHeight(), b, new int[image1.getHeight()]); int[] img2data =
             * image2.getData().getSamples(xpos, 0, 1, image1.getHeight(), b, new
             * int[image1.getHeight()]); if (!Arrays.equals(img1data, img2data)) {
             * // System.out.println("pixels in column " + xpos + " are different"); return false; } }
             */

        }
        return true;
    }

    public SeRasterAttr getRasterAttributes(
            SeConnection conn,
            final String rasterName,
            final Rectangle tiles,
            final int level,
            final int[] bands)
            throws IOException {

        try {
            final SeQuery query =
                    new SeQuery(conn, new String[] {"RASTER"}, new SeSqlConstruct(rasterName));
            query.prepareQuery();
            query.execute();
            final SeRow r = query.fetch();

            // Now build a SeRasterConstraint object which queries the db for
            // the right tiles/bands/pyramid level
            SeRasterConstraint rConstraint = new SeRasterConstraint();
            rConstraint.setEnvelope(
                    (int) tiles.getMinX(),
                    (int) tiles.getMinY(),
                    (int) tiles.getMaxX(),
                    (int) tiles.getMaxY());
            rConstraint.setLevel(level);
            rConstraint.setBands(bands);

            // Finally, execute the raster query aganist the already-opened
            // SeQuery object which already has an SeRow fetched against it.

            query.queryRasterTile(rConstraint);
            final SeRasterAttr rattr = r.getRaster(0);

            query.close();

            return rattr;
        } catch (SeException e) {
            throw new ArcSdeException(e);
        }
    }

    public abstract static class PixelSampler {

        private static Map<RasterCellType, PixelSampler> byPixelTypeSamplers =
                new HashMap<RasterCellType, PixelSampler>();

        static {
            byPixelTypeSamplers.put(TYPE_1BIT, new SamplerType1Bit());
            byPixelTypeSamplers.put(TYPE_4BIT, new SamplerType4Bit());
            byPixelTypeSamplers.put(TYPE_8BIT_U, new SamplerType8BitUnsigned());
            byPixelTypeSamplers.put(TYPE_8BIT_S, new SamplerType8BitSigned());
            byPixelTypeSamplers.put(TYPE_16BIT_U, new SamplerType16BitUnsigned());
            byPixelTypeSamplers.put(TYPE_16BIT_S, new SamplerType16BitSigned());
            byPixelTypeSamplers.put(TYPE_32BIT_U, new SamplerType32BitUnsigned());
            byPixelTypeSamplers.put(TYPE_32BIT_S, new SamplerType32BitSigned());
            byPixelTypeSamplers.put(TYPE_32BIT_REAL, new SamplerType32BitReal());
            byPixelTypeSamplers.put(TYPE_64BIT_REAL, new SamplerType64BitReal());
        }

        public static PixelSampler getSampler(final RasterCellType pixelType) {
            PixelSampler sampler = byPixelTypeSamplers.get(pixelType);
            if (sampler == null) {
                throw new NoSuchElementException(
                        "no pixel sampler exists for pixel type " + pixelType);
            }
            return sampler;
        }

        public void setImageData(WritableRaster raster) {
            final int width = raster.getWidth();
            final int height = raster.getHeight();
            final int numBands = raster.getNumBands();
            byte[][] bandData = new byte[numBands][];

            for (int bandN = 0; bandN < numBands; bandN++) {
                bandData[bandN] = getImgBandData(width, height, bandN, numBands);
            }

            for (int bandN = 0; bandN < numBands; bandN++) {
                byte[] bandContens = bandData[bandN];
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        setSample(x, y, bandN, bandContens, raster);
                    }
                }
            }
        }

        private void setSample(int x, int y, int bandN, byte[] bandContens, WritableRaster raster) {
            final int width = raster.getWidth();
            final int pixArrayOffset = (y * width) + x;
            byte pixelData = bandContens[pixArrayOffset];
            int sampleValue = pixelData & 0x000000ff;

            raster.setSample(x, y, bandN, sampleValue);
        }

        /**
         * Returns the programatically generated test data for a band of {@code bandWidth x
         * bandHeight} for the band {@code bandN} of {@code numBand} total bands.
         *
         * @param bandN zero-based index of the band to generate the sample data for
         * @param numBands total number of bands the sample data is being generated for
         */
        public abstract byte[] getImgBandData(
                final int bandWidth, final int bandHeight, final int bandN, final int numBands);

        /**
         * Pixel sampler for creating test data with pixel type {@link SeRaster#SE_PIXEL_TYPE_1BIT}
         *
         * @see PixelSampler#getImgBandData(int, int)
         */
        private static class SamplerType1Bit extends PixelSampler {
            @Override
            public byte[] getImgBandData(
                    int imgWidth, int imgHeight, final int bandN, final int numBands) {
                final byte[] imgBandData = new byte[(imgWidth * imgHeight) / 8];
                for (int bytePos = 0; bytePos < imgBandData.length; bytePos++) {
                    if (bytePos % 2 == 0) {
                        imgBandData[bytePos] = (byte) 0xFF;
                    } else {
                        imgBandData[bytePos] = (byte) 0x00;
                    }
                }
                return imgBandData;
            }
        }

        /**
         * Pixel sampler for creating test data with pixel type {@link SeRaster#SE_PIXEL_TYPE_4BIT}
         *
         * @see PixelSampler#getImgBandData(int, int)
         */
        private static class SamplerType4Bit extends PixelSampler {
            @Override
            public byte[] getImgBandData(
                    int imgWidth, int imgHeight, final int bandN, final int numBands) {
                final int numBytes = (int) Math.ceil(imgWidth * imgHeight / 2D);
                final byte[] imgBandData = new byte[numBytes];
                LinkedList<Integer> values = new LinkedList<Integer>();
                for (int val = 0; val < 16; val++) {
                    values.add(val);
                }
                for (int coupleN = 0; coupleN < numBytes; coupleN++) {
                    Integer val = values.poll();
                    values.add(val);
                    imgBandData[coupleN] = (byte) (val.intValue() & 15);
                }
                return imgBandData;
            }
        }

        /**
         * Pixel sampler for creating test data with pixel type {@link
         * SeRaster#SE_PIXEL_TYPE_8BIT_U}
         *
         * @see PixelSampler#getImgBandData(int, int)
         */
        private static class SamplerType8BitUnsigned extends PixelSampler {
            @Override
            public byte[] getImgBandData(
                    int imgWidth, int imgHeight, final int bandN, final int numBands) {
                // luckily the byte-packed data format in MultiPixelPackedSampleModel is identical
                // to the one-bit-per-pixel format expected by ArcSDE.
                final byte[] imgBandData = new byte[imgWidth * imgHeight];
                for (int w = 0; w < imgWidth; w++) {
                    for (int h = 0; h < imgHeight; h++) {
                        imgBandData[(w * imgHeight) + h] = (byte) h;
                    }
                }
                return imgBandData;
            }
        }

        /**
         * Pixel sampler for creating test data with pixel type {@link
         * SeRaster#SE_PIXEL_TYPE_8BIT_S}
         *
         * @see PixelSampler#getImgBandData(int, int)
         */
        private static class SamplerType8BitSigned extends PixelSampler {
            @Override
            public byte[] getImgBandData(
                    int imgWidth, int imgHeight, final int bandN, final int numBands) {
                // luckily the byte-packed data format in MultiPixelPackedSampleModel is identical
                // to the one-bit-per-pixel format expected by ArcSDE.
                final byte[] imgBandData = new byte[imgWidth * imgHeight];
                final byte min = Byte.MIN_VALUE;
                final byte step = 1;
                byte val = min;
                for (int w = 0; w < imgWidth; w++) {
                    for (int h = 0; h < imgHeight; h++) {
                        imgBandData[(w * imgHeight) + h] = val;
                    }
                    val += step;
                }
                return imgBandData;
            }
        }

        /**
         * Pixel sampler for creating test data with pixel type {@link
         * SeRaster#SE_PIXEL_TYPE_16BIT_U}
         *
         * @see PixelSampler#getImgBandData(int, int)
         */
        private static class SamplerType16BitUnsigned extends PixelSampler {
            @Override
            public byte[] getImgBandData(
                    int imgWidth, int imgHeight, final int bandN, final int numBands) {
                final int DATA_TYPE_DEPTH = 2;
                // luckily the byte-packed data format in MultiPixelPackedSampleModel is identical
                // to the one-bit-per-pixel format expected by ArcSDE.
                final int dataSize = DATA_TYPE_DEPTH * imgWidth * imgHeight;
                final ByteArrayOutputStream out = new ByteArrayOutputStream(dataSize);
                final DataOutputStream writer = new DataOutputStream(out);

                int pixelValue;
                final int MIN = 0;
                final int MAX = 65535;
                final float step = ((float) MAX - (float) MIN) / (float) imgWidth;
                pixelValue = MIN;
                try {
                    for (int x = 0; x < imgWidth; x++) {
                        for (int y = 0; y < imgHeight; y++) {
                            writer.writeShort(pixelValue);
                        }
                        pixelValue += step;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                final byte[] imgBandData = out.toByteArray();
                return imgBandData;

                // final byte[] imgBandData = new byte[DATA_TYPE_DEPTH * imgWidth * imgHeight];
                // int pixelIndex;
                // int pixelValue;
                // byte ushortByte1;
                // byte ushortByte2;
                // final int MIN = 0;
                // final int MAX = 65535;
                // final float step = ((float) MAX - (float) MIN) / (float) imgWidth;
                // pixelValue = MIN;
                // for (int x = 0; x < imgWidth; x++) {
                // ushortByte1 = (byte) ((pixelValue >>> 8) & 0xFF);
                // ushortByte2 = (byte) ((pixelValue >>> 0) & 0xFF);
                // for (int y = 0; y < imgHeight; y++) {
                // pixelIndex = (x * imgHeight) + y;
                // imgBandData[DATA_TYPE_DEPTH * pixelIndex] = ushortByte1;
                // imgBandData[DATA_TYPE_DEPTH * pixelIndex + 1] = ushortByte2;
                // {
                // int decodedPixelValue = 0;
                // decodedPixelValue |= ushortByte1 & 0xFF;
                // decodedPixelValue <<= 8;
                // decodedPixelValue |= ushortByte2 & 0xFF;
                // if (pixelValue != decodedPixelValue) {
                // throw new IllegalStateException(pixelValue + " != "
                // + decodedPixelValue);
                // }
                // }
                // }
                // pixelValue += step;
                // }
                // return imgBandData;
            }
        }

        /**
         * Pixel sampler for creating test data with pixel type {@link
         * SeRaster#SE_PIXEL_TYPE_16BIT_S}
         *
         * @see PixelSampler#getImgBandData(int, int)
         */
        private static class SamplerType16BitSigned extends PixelSampler {
            @Override
            public byte[] getImgBandData(
                    int imgWidth, int imgHeight, final int bandN, final int numBands) {
                final int DATA_TYPE_DEPTH = 2;
                // luckily the byte-packed data format in MultiPixelPackedSampleModel is identical
                // to the one-bit-per-pixel format expected by ArcSDE.
                final int dataSize = DATA_TYPE_DEPTH * imgWidth * imgHeight;
                final ByteArrayOutputStream out = new ByteArrayOutputStream(dataSize);
                final DataOutputStream writer = new DataOutputStream(out);

                final int MIN = Short.MIN_VALUE;
                final int MAX = Short.MAX_VALUE;
                final float step = ((float) MAX - (float) MIN) / (float) imgWidth;
                int pixelValue = MIN;
                try {
                    for (int x = 0; x < imgWidth; x++) {
                        for (int y = 0; y < imgHeight; y++) {
                            writer.writeShort(pixelValue);
                        }
                        pixelValue += step;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                final byte[] imgBandData = out.toByteArray();
                return imgBandData;
            }
        }

        /**
         * Pixel sampler for creating test data with pixel type {@link
         * SeRaster#SE_PIXEL_TYPE_32BIT_U}
         *
         * @see PixelSampler#getImgBandData(int, int)
         */
        private static class SamplerType32BitUnsigned extends PixelSampler {
            @Override
            public byte[] getImgBandData(
                    int imgWidth, int imgHeight, final int bandN, final int numBands) {
                final int DATA_TYPE_DEPTH = 4;
                final int dataSize = DATA_TYPE_DEPTH * imgWidth * imgHeight;
                final ByteArrayOutputStream out = new ByteArrayOutputStream(dataSize);
                final DataOutputStream writer = new DataOutputStream(out);

                final long MIN = (long) TYPE_32BIT_U.getSampleValueRange().getMinimum();
                final long MAX = (long) TYPE_32BIT_U.getSampleValueRange().getMaximum();
                final float step = ((float) MAX - (float) MIN) / (float) imgWidth;
                long pixelValue = MIN;
                try {
                    for (int x = 0; x < imgWidth; x++) {
                        for (int y = 0; y < imgHeight; y++) {
                            writer.writeInt((int) pixelValue);
                        }
                        pixelValue += step;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                final byte[] imgBandData = out.toByteArray();
                return imgBandData;
            }
        }

        /**
         * Pixel sampler for creating test data with pixel type {@link
         * SeRaster#SE_PIXEL_TYPE_32BIT_S}
         *
         * @see PixelSampler#getImgBandData(int, int)
         */
        private static class SamplerType32BitSigned extends PixelSampler {
            @Override
            public byte[] getImgBandData(
                    int imgWidth, int imgHeight, final int bandN, final int numBands) {
                final int DATA_TYPE_DEPTH = 4;
                final int dataSize = DATA_TYPE_DEPTH * imgWidth * imgHeight;
                final ByteArrayOutputStream out = new ByteArrayOutputStream(dataSize);
                final DataOutputStream writer = new DataOutputStream(out);

                final int MIN = (int) TYPE_32BIT_S.getSampleValueRange().getMinimum();
                final int MAX = (int) TYPE_32BIT_S.getSampleValueRange().getMaximum();
                final float step = ((float) MAX - (float) MIN) / (float) imgWidth;
                int pixelValue = MIN;
                try {
                    for (int x = 0; x < imgWidth; x++) {
                        for (int y = 0; y < imgHeight; y++) {
                            writer.writeInt(pixelValue);
                        }
                        pixelValue += step;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                final byte[] imgBandData = out.toByteArray();
                return imgBandData;
            }
        }

        /**
         * Pixel sampler for creating test data with pixel type {@link
         * SeRaster#SE_PIXEL_TYPE_32BIT_REAL}
         *
         * @see PixelSampler#getImgBandData(int, int)
         */
        private static class SamplerType32BitReal extends PixelSampler {
            @Override
            public byte[] getImgBandData(
                    int imgWidth, int imgHeight, final int bandN, final int numBands) {
                final int DATA_TYPE_DEPTH = 4;
                final int dataSize = DATA_TYPE_DEPTH * imgWidth * imgHeight;
                final ByteArrayOutputStream out = new ByteArrayOutputStream(dataSize);
                final DataOutputStream writer = new DataOutputStream(out);

                final float MIN = Float.MIN_VALUE;
                final float MAX = Float.MAX_VALUE;
                final float step = ((float) MAX - (float) MIN) / (float) imgWidth;
                float pixelValue = MIN;
                try {
                    for (int x = 0; x < imgWidth; x++) {
                        for (int y = 0; y < imgHeight; y++) {
                            writer.writeFloat(pixelValue);
                        }
                        pixelValue += step;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                final byte[] imgBandData = out.toByteArray();
                return imgBandData;
            }
        }

        /**
         * Pixel sampler for creating test data with pixel type {@link
         * SeRaster#SE_PIXEL_TYPE_64BIT_REAL}
         *
         * @see PixelSampler#getImgBandData(int, int)
         */
        private static class SamplerType64BitReal extends PixelSampler {
            @Override
            public byte[] getImgBandData(
                    int imgWidth, int imgHeight, final int bandN, final int numBands) {
                final int DATA_TYPE_DEPTH = 8;
                final int dataSize = DATA_TYPE_DEPTH * imgWidth * imgHeight;
                final ByteArrayOutputStream out = new ByteArrayOutputStream(dataSize);
                final DataOutputStream writer = new DataOutputStream(out);

                final double MIN = Double.MIN_VALUE;
                final double MAX = Double.MAX_VALUE;
                final double step = (MAX - MIN) / imgWidth;
                double pixelValue = MIN;
                try {
                    for (int x = 0; x < imgWidth; x++) {
                        for (int y = 0; y < imgHeight; y++) {
                            writer.writeDouble(pixelValue);
                        }
                        pixelValue += step;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                final byte[] imgBandData = out.toByteArray();
                return imgBandData;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void describeRasters() throws Exception {
        ISession session = getConnectionPool().getSession();
        try {
            session.issue(
                    new Command<Void>() {
                        @Override
                        public Void execute(ISession session, SeConnection connection)
                                throws SeException, IOException {
                            List<SeRasterColumn> rasterCols = connection.getRasterColumns();
                            for (SeRasterColumn rasterCol : rasterCols) {
                                final String colName = rasterCol.getName();
                                final String tableName = rasterCol.getTableName();
                                final String tableQName = rasterCol.getQualifiedTableName();

                                SeQuery query =
                                        new SeQuery(
                                                connection,
                                                new String[] {colName},
                                                new SeSqlConstruct(tableQName));
                                query.prepareQuery();
                                query.execute();
                                SeRow row = query.fetch();
                                if (row == null) {
                                    continue;
                                }
                                SeRasterAttr attr = row.getRaster(0);
                                int pixelType = attr.getPixelType();
                                final RasterCellType cellType = RasterCellType.valueOf(pixelType);
                                final int numBands = attr.getNumBands();
                                final int imageWidth = attr.getImageWidth();
                                final int imageHeight = attr.getImageHeight();
                                final int compressionType = attr.getCompressionType();
                                final int tileHeight = attr.getTileHeight();
                                final int tileWidth = attr.getTileWidth();
                                final int interpolation = attr.getInterpolation();

                                // System.out.println(
                                //                                        tableName
                                //                                                + ":\n\t
                                // pixelType: "
                                //                                                + cellType
                                //                                                + "\n\tnumBands: "
                                //                                                + numBands
                                //                                                + "\n\twidth: "
                                //                                                + imageWidth
                                //                                                + ", height: "
                                //                                                + imageHeight
                                //                                                + ", tile width: "
                                //                                                + tileWidth
                                //                                                + ", tile height:
                                // "
                                //                                                + tileHeight
                                //                                                + "\n\t
                                // compression type: "
                                //                                                +
                                // CompressionType.valueOf(compressionType)
                                //                                                + ",
                                // interpolation: "
                                //                                                +
                                // InterpolationType.valueOf(interpolation));
                                SeRasterBand[] bands = attr.getBands();
                                for (SeRasterBand band : bands) {
                                    int bandNumber = band.getBandNumber();
                                    boolean hasColorMap = band.hasColorMap();
                                    //                                    // System.out.println(
                                    //                                            "\t Band nº "
                                    //                                                    +
                                    // bandNumber
                                    //                                                    + ": has
                                    // color map: "
                                    //                                                    +
                                    // hasColorMap);
                                    // if (hasColorMap) {
                                    // SeRasterBandColorMap colorMap = band.getColorMap();
                                    // System.out.println("\tColor map: " + colorMap);
                                    // }
                                }
                                // query.close();
                            }
                            return null;
                        }
                    });
        } finally {
            session.dispose();
        }
    }

    public static RenderedImage createExpectedImage(
            final int width, final int height, final RasterCellType pixelType, final int numBands) {
        final BufferedImage fromSdeImage;

        final int pixelStride = 1;
        final int scanLineStride = width;
        final int[] bandOffsets = new int[] {0};

        SampleModel sm =
                new ComponentSampleModel(
                        DataBuffer.TYPE_USHORT,
                        width,
                        height,
                        pixelStride,
                        scanLineStride,
                        bandOffsets);
        DataBuffer db = new DataBufferUShort(width * height);
        WritableRaster wr = Raster.createWritableRaster(sm, db, null);
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorModel cm =
                new ComponentColorModel(
                        cs, false, true, Transparency.OPAQUE, DataBuffer.TYPE_USHORT);
        fromSdeImage = new BufferedImage(cm, wr, false, null);

        return fromSdeImage;
    }

    public static void main(String argv[]) {
        RasterTestData testData = new RasterTestData();
        try {
            testData.setUp();
            testData.describeRasters();
            testData.tearDown();
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
    }
}
