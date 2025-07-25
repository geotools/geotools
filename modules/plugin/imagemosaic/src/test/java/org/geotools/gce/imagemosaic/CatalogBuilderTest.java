/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Properties;
import javax.media.jai.PlanarImage;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.gce.imagemosaic.ImageMosaicEventHandlers.ExceptionEvent;
import org.geotools.gce.imagemosaic.ImageMosaicEventHandlers.ProcessingEvent;
import org.geotools.gce.imagemosaic.ImageMosaicEventHandlers.ProcessingEventListener;
import org.geotools.gce.imagemosaic.Utils.Prop;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilderConfiguration;
import org.geotools.geometry.GeneralBounds;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Testing {@link CatalogBuilder} and its related subclasses.
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 */
public class CatalogBuilderTest extends Assert {

    /** Used to avoid errors if building on a system where hostname is not defined */
    private boolean hostnameDefined;

    @Before
    public void setup() {
        try {
            InetAddress.getLocalHost();
            hostnameDefined = true;
        } catch (Exception ex) {
            hostnameDefined = false;
        }
    }

    private static final class CatalogBuilderListener extends ProcessingEventListener {

        @Override
        public void exceptionOccurred(ExceptionEvent event) {
            throw new RuntimeException(event.getException());
        }

        @Override
        public void getNotification(ProcessingEvent event) {}
    }

    @Test
    public void catalogBuilderConfiguration() throws Exception {
        // create a stub configuration
        final CatalogBuilderConfiguration c1 = new CatalogBuilderConfiguration();
        c1.setParameter(Prop.INDEX_NAME, "index");
        c1.setParameter(Prop.LOCATION_ATTRIBUTE, "location");
        c1.setParameter(Prop.ABSOLUTE_PATH, "true");
        c1.setParameter(Prop.ROOT_MOSAIC_DIR, TestData.file(this, "/rgb").toString());
        c1.setParameter(Prop.INDEXING_DIRECTORIES, TestData.file(this, "/rgb").toString());
        //        c1.setIndexName("index");
        // c1.setLocationAttribute("location");
        //		c1.setAbsolute(true);
        //		c1.setRootMosaicDirectory(TestData.file(this,"/rgb").toString());
        //		c1.setIndexingDirectories(Arrays.asList(TestData.file(this,"/rgb").toString()));
        assertNotNull(c1.toString());

        // create a second stub configuration
        final CatalogBuilderConfiguration c2 = new CatalogBuilderConfiguration();
        c2.setParameter(Prop.INDEX_NAME, "index");
        c2.setParameter(Prop.LOCATION_ATTRIBUTE, "location");
        c2.setParameter(Prop.ABSOLUTE_PATH, "true");
        c2.setParameter(Prop.ROOT_MOSAIC_DIR, TestData.file(this, "/rgb").toString());
        c2.setParameter(Prop.INDEXING_DIRECTORIES, TestData.file(this, "/rgb").toString());
        //		c2.setIndexName("index");
        //		c2.setLocationAttribute("location");
        //		c2.setAbsolute(true);
        //		c2.setRootMosaicDirectory(TestData.file(this,"/rgb").toString());
        //		c2.setIndexingDirectories(Arrays.asList(TestData.file(this,"/rgb").toString()));
        //
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());

        CatalogBuilderConfiguration c3 = c2.clone();
        assertEquals(c3, c2);
        assertEquals(c3.hashCode(), c2.hashCode());

        // check errors
        final CatalogBuilderConfiguration c4 = new CatalogBuilderConfiguration();
        assertNotNull(c4.toString());
    }

    @Test
    @Ignore
    public void buildCatalog() throws FileNotFoundException, IOException {
        if (!hostnameDefined) {
            return;
        }

        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJai.setValue(false);

        final ParameterValue<String> tileSize = AbstractGridFormat.SUGGESTED_TILE_SIZE.createValue();
        tileSize.setValue("128,128");

        // build a relative index and then make it run
        CatalogBuilderConfiguration c1 = new CatalogBuilderConfiguration();
        c1.setParameter(Prop.INDEX_NAME, "shpindex");
        c1.setParameter(Prop.LOCATION_ATTRIBUTE, "location");
        c1.setParameter(Prop.ABSOLUTE_PATH, "false");
        c1.setParameter(Prop.ROOT_MOSAIC_DIR, TestData.file(this, "/overview/0").toString());
        c1.setParameter(
                Prop.INDEXING_DIRECTORIES, TestData.file(this, "/overview/0").toString());
        //		c1.setIndexName("shpindex");
        //		c1.setLocationAttribute("location");
        //		c1.setAbsolute(false);
        //		c1.setRootMosaicDirectory(TestData.file(this,"/overview").toString());
        //
        //	c1.setIndexingDirectories(Arrays.asList(TestData.file(this,"/overview/0").toString()));
        assertNotNull(c1.toString());
        ImageMosaicEventHandlers eventHandler = new ImageMosaicEventHandlers();
        final ImageMosaicConfigHandler catalogHandler = new ImageMosaicConfigHandler(c1, eventHandler);
        // TODO
        // build the index
        ImageMosaicDirectoryWalker builder = new ImageMosaicDirectoryWalker(catalogHandler, eventHandler);
        eventHandler.addProcessingEventListener(new CatalogBuilderListener());
        builder.run();
        final File relativeMosaic = TestData.file(this, "/overview/" + c1.getParameter(Prop.INDEX_NAME) + ".shp");
        assertTrue(relativeMosaic.exists());

        assertTrue(new ImageMosaicFormat().accepts(relativeMosaic));
        ImageMosaicReader reader = new ImageMosaicReader(relativeMosaic);

        // limit yourself to reading just a bit of it
        ParameterValue<GridGeometry2D> gg = AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        GeneralBounds envelope = reader.getOriginalEnvelope();
        Dimension dim = new Dimension();
        dim.setSize(
                reader.getOriginalGridRange().getSpan(0) / 2.0,
                reader.getOriginalGridRange().getSpan(1) / 2.0);
        Rectangle rasterArea = (GridEnvelope2D) reader.getOriginalGridRange();
        rasterArea.setSize(dim);
        GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));

        // use imageio with defined tiles

        // Test the output coverage
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {gg, useJai, tileSize});
        Assert.assertNotNull(coverage);
        PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getTiles();

        // caching should be false by default
        Properties props = new Properties();
        String c1IndexerProps = "/overview/" + c1.getParameter(Prop.INDEX_NAME) + ".properties";
        try (InputStream in = TestData.openStream(this, c1IndexerProps)) {
            assertNotNull("unable to find mosaic properties file", in);
            props.load(in);

            assertTrue(props.containsKey("Caching"));
            assertTrue(props.getProperty("Caching").equalsIgnoreCase("false"));
        }

        // dispose
        coverage.dispose(true);
        reader.dispose();

        // build an absolute index and then make it run
        CatalogBuilderConfiguration c2 = new CatalogBuilderConfiguration();
        c2.setParameter(Prop.INDEX_NAME, "shpindex_absolute");
        c2.setParameter(Prop.LOCATION_ATTRIBUTE, "location");
        c2.setParameter(Prop.ABSOLUTE_PATH, "true");
        c2.setParameter(Prop.CACHING, "true");
        c2.setParameter(Prop.ROOT_MOSAIC_DIR, TestData.file(this, "/overview").toString());
        c2.setParameter(
                Prop.INDEXING_DIRECTORIES, TestData.file(this, "/overview").toString());
        //		c2.setIndexName("shpindex_absolute");
        //		c2.setLocationAttribute("location");
        //		c2.setAbsolute(true);
        //		c2.setCaching(true);
        //		c2.setRootMosaicDirectory(TestData.file(this,"/overview").toString());
        //
        //	c2.setIndexingDirectories(Arrays.asList(TestData.file(this,"/overview/0").toString()));
        assertNotNull(c2.toString());
        ImageMosaicEventHandlers eventHandler2 = new ImageMosaicEventHandlers();
        final ImageMosaicConfigHandler catalogHandler2 = new ImageMosaicConfigHandler(c2, eventHandler);
        // build the index
        builder = new ImageMosaicDirectoryWalker(catalogHandler2, eventHandler);
        eventHandler2.addProcessingEventListener(new CatalogBuilderListener());
        builder.run();
        final File absoluteMosaic = TestData.file(this, "/overview/" + c2.getParameter(Prop.INDEX_NAME) + ".shp");
        assertTrue(absoluteMosaic.exists());

        // caching should be false by default
        props = new Properties();
        try (InputStream in =
                TestData.openStream(this, "/overview/" + c2.getParameter(Prop.INDEX_NAME) + ".properties")) {
            assertNotNull("unable to find mosaic properties file", in);
            props.load(in);

            assertTrue(props.containsKey("Caching"));
            assertTrue(props.getProperty("Caching").equalsIgnoreCase("true"));
        }

        assertTrue(new ImageMosaicFormat().accepts(absoluteMosaic));
        reader = new ImageMosaicReader(absoluteMosaic);

        // limit yourself to reading just a bit of it
        gg = AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        envelope = reader.getOriginalEnvelope();
        dim = new Dimension();
        dim.setSize(
                reader.getOriginalGridRange().getSpan(0) / 2.0,
                reader.getOriginalGridRange().getSpan(1) / 2.0);
        rasterArea = (GridEnvelope2D) reader.getOriginalGridRange();
        rasterArea.setSize(dim);
        range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));

        // use imageio with defined tiles

        // Test the output coverage
        coverage = reader.read(new GeneralParameterValue[] {gg, useJai, tileSize});
        Assert.assertNotNull(coverage);
        PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getTiles();

        // dispose
        coverage.dispose(true);
        reader.dispose();
    }

    @Test
    @Ignore
    public void buildCachingIndex() throws FileNotFoundException, IOException {
        if (!hostnameDefined) return;

        ImageMosaicDirectoryWalker builder = null;
        ImageMosaicReader reader = null;
        CatalogBuilderConfiguration c1 = new CatalogBuilderConfiguration();
        //		c1.setIndexName("shpindex");
        //		c1.setLocationAttribute("location");
        //		c1.setAbsolute(false);
        //		c1.setRootMosaicDirectory(TestData.file(this, "/caching").toString());
        //		c1.setIndexingDirectories(Arrays.asList(TestData.file(this,"/caching").toString()));
        c1.setParameter(Prop.INDEX_NAME, "shpindex");
        c1.setParameter(Prop.LOCATION_ATTRIBUTE, "location");
        c1.setParameter(Prop.ABSOLUTE_PATH, "false");
        c1.setParameter(Prop.ROOT_MOSAIC_DIR, TestData.file(this, "/caching").toString());
        c1.setParameter(
                Prop.INDEXING_DIRECTORIES, TestData.file(this, "/caching").toString());

        Properties prop = new Properties();

        try {

            //			c1.setCaching(false);
            c1.setParameter(Prop.CACHING, "false");

            ImageMosaicEventHandlers eventHandler = new ImageMosaicEventHandlers();
            final ImageMosaicConfigHandler catalogHandler = new ImageMosaicConfigHandler(c1, eventHandler);
            // TODO
            // build the index
            builder = new ImageMosaicDirectoryWalker(catalogHandler, eventHandler);
            eventHandler.addProcessingEventListener(new CatalogBuilderListener());
            builder.run();
            final File relativeMosaic = TestData.file(this, "/caching/" + c1.getParameter(Prop.INDEX_NAME) + ".shp");
            final File propertiesFile =
                    TestData.file(this, "/caching/" + c1.getParameter(Prop.INDEX_NAME) + ".properties");
            assertTrue(relativeMosaic.exists());
            try (InputStream inStream = new FileInputStream(propertiesFile)) {
                prop.load(inStream);
            }
            String value = prop.getProperty("Caching");
            assertNotNull(value);
            assertEquals("false", value.toLowerCase());

            assertTrue(new ImageMosaicFormat().accepts(relativeMosaic));
            reader = new ImageMosaicReader(relativeMosaic);

            GranuleCatalog catalog = reader.getRasterManager(reader.defaultName).granuleCatalog;
            assertTrue(catalog.getClass().toString().endsWith("GTDataStoreGranuleCatalog"));
        } finally {
            try {
                if (reader != null) {
                    reader.dispose();
                }
            } catch (Throwable t) {
                // Eat exception
            }
        }

        try {

            //			c1.setCaching(true);
            c1.setParameter(Prop.CACHING, "true");

            ImageMosaicEventHandlers eventHandler = new ImageMosaicEventHandlers();
            final ImageMosaicConfigHandler catalogHandler = new ImageMosaicConfigHandler(c1, eventHandler);
            // TODO
            // build the index
            builder = new ImageMosaicDirectoryWalker(catalogHandler, eventHandler);
            eventHandler.addProcessingEventListener(new CatalogBuilderListener());
            builder.run();
            final File relativeMosaic = TestData.file(this, "/caching/" + c1.getParameter(Prop.INDEX_NAME) + ".shp");
            final File propertiesFile =
                    TestData.file(this, "/caching/" + c1.getParameter(Prop.INDEX_NAME) + ".properties");
            try (InputStream inStream = new FileInputStream(propertiesFile)) {
                prop.load(inStream);
            }

            String value = prop.getProperty("Caching");
            assertNotNull(value);
            assertEquals("true", value.toLowerCase());

            assertTrue(relativeMosaic.exists());

            assertTrue(new ImageMosaicFormat().accepts(relativeMosaic));
            reader = new ImageMosaicReader(relativeMosaic);

            GranuleCatalog catalog = reader.getRasterManager(reader.defaultName).granuleCatalog;
            assertTrue(catalog.getClass().toString().endsWith("STRTreeGranuleCatalog"));

        } finally {
            try {
                if (reader != null) {
                    reader.dispose();
                }
            } catch (Throwable t) {
                // Eat exception
            }
        }
    }
}
