/// *
// *    GeoTools - The Open Source Java GIS Toolkit
// *    http://geotools.org
// *
// *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
// *
// *    This library is free software; you can redistribute it and/or
// *    modify it under the terms of the GNU Lesser General Public
// *    License as published by the Free Software Foundation;
// *    version 2.1 of the License.
// *
// *    This library is distributed in the hope that it will be useful,
// *    but WITHOUT ANY WARRANTY; without even the implied warranty of
// *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// *    Lesser General Public License for more details.
// */
package org.geotools.process.raster;

import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.media.jai.PlanarImage;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.data.DataUtilities;
import org.geotools.data.WorldFileReader;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.test.TestData;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Polygon;

/** @author DamianoG */
public class ZonalStatsProcessTest extends Assert {

    private RasterZonalStatistics process;

    private DataStore ds;

    private static Map<String, String> results = null;

    static {
        // TODO: in the future we should use a more robust way to generate the expected results, this changes
        // due to numerical or ROI issues (e.g. a small change in how the geometry is turned into a binary mask)
        results = new HashMap<>();
        results.put(
                "testpolygon.11",
                "SimpleFeatureImpl:testpolygon=[SimpleFeatureImpl.Attribute: z_the_geom<z_the_geom id=testpolygon.1>=MULTIPOLYGON (((11.89513017802011 46.20793481460109, 11.895809089115916 46.20792743071521, 11.895112009452415 46.2070509839355, 11.89513017802011 46.20793481460109))), SimpleFeatureImpl.Attribute: z_name<z_name id=testpolygon.1>=, SimpleFeatureImpl.Attribute: classification<classification id=testpolygon.1>=1, SimpleFeatureImpl.Attribute: count<count id=testpolygon.1>=660, SimpleFeatureImpl.Attribute: min<min id=testpolygon.1>=1251.0, SimpleFeatureImpl.Attribute: max<max id=testpolygon.1>=1630.0, SimpleFeatureImpl.Attribute: sum<sum id=testpolygon.1>=893513.0, SimpleFeatureImpl.Attribute: avg<avg id=testpolygon.1>=1353.8075757575757, SimpleFeatureImpl.Attribute: stddev<stddev id=testpolygon.1>=69.18421849089562]");
        results.put(
                "testpolygon.12",
                "SimpleFeatureImpl:testpolygon=[SimpleFeatureImpl.Attribute: z_the_geom<z_the_geom id=testpolygon.1>=MULTIPOLYGON (((11.89513017802011 46.20793481460109, 11.895809089115916 46.20792743071521, 11.895112009452415 46.2070509839355, 11.89513017802011 46.20793481460109))), SimpleFeatureImpl.Attribute: z_name<z_name id=testpolygon.1>=, SimpleFeatureImpl.Attribute: classification<classification id=testpolygon.1>=2, SimpleFeatureImpl.Attribute: count<count id=testpolygon.1>=695, SimpleFeatureImpl.Attribute: min<min id=testpolygon.1>=1251.0, SimpleFeatureImpl.Attribute: max<max id=testpolygon.1>=1589.0, SimpleFeatureImpl.Attribute: sum<sum id=testpolygon.1>=943670.0, SimpleFeatureImpl.Attribute: avg<avg id=testpolygon.1>=1357.7985611510792, SimpleFeatureImpl.Attribute: stddev<stddev id=testpolygon.1>=70.23109033611718]");
        results.put(
                "testpolygon.21",
                "SimpleFeatureImpl:testpolygon=[SimpleFeatureImpl.Attribute: z_the_geom<z_the_geom id=testpolygon.2>=MULTIPOLYGON (((11.896630238926786 46.207395686643665, 11.897000925691524 46.207985638846736, 11.897753464657697 46.20749492858355, 11.896630238926786 46.207395686643665))), SimpleFeatureImpl.Attribute: z_name<z_name id=testpolygon.2>=, SimpleFeatureImpl.Attribute: classification<classification id=testpolygon.2>=1, SimpleFeatureImpl.Attribute: count<count id=testpolygon.2>=721, SimpleFeatureImpl.Attribute: min<min id=testpolygon.2>=1191.0, SimpleFeatureImpl.Attribute: max<max id=testpolygon.2>=1411.0, SimpleFeatureImpl.Attribute: sum<sum id=testpolygon.2>=903554.0, SimpleFeatureImpl.Attribute: avg<avg id=testpolygon.2>=1253.1955617198337, SimpleFeatureImpl.Attribute: stddev<stddev id=testpolygon.2>=42.38195085236204]");
        results.put(
                "testpolygon.22",
                "SimpleFeatureImpl:testpolygon=[SimpleFeatureImpl.Attribute: z_the_geom<z_the_geom id=testpolygon.2>=MULTIPOLYGON (((11.896630238926786 46.207395686643665, 11.897000925691524 46.207985638846736, 11.897753464657697 46.20749492858355, 11.896630238926786 46.207395686643665))), SimpleFeatureImpl.Attribute: z_name<z_name id=testpolygon.2>=, SimpleFeatureImpl.Attribute: classification<classification id=testpolygon.2>=2, SimpleFeatureImpl.Attribute: count<count id=testpolygon.2>=703, SimpleFeatureImpl.Attribute: min<min id=testpolygon.2>=1192.0, SimpleFeatureImpl.Attribute: max<max id=testpolygon.2>=1430.0, SimpleFeatureImpl.Attribute: sum<sum id=testpolygon.2>=881884.0, SimpleFeatureImpl.Attribute: avg<avg id=testpolygon.2>=1254.4580369843527, SimpleFeatureImpl.Attribute: stddev<stddev id=testpolygon.2>=43.43473527086064]");
        results.put(
                "testpolygon.31",
                "SimpleFeatureImpl:testpolygon=[SimpleFeatureImpl.Attribute: z_the_geom<z_the_geom id=testpolygon.3>=MULTIPOLYGON (((11.897871315022112 46.20812076814061, 11.898581076580497 46.20816171238038, 11.898470449632523 46.20767324666812, 11.897871315022112 46.20812076814061))), SimpleFeatureImpl.Attribute: z_name<z_name id=testpolygon.3>=, SimpleFeatureImpl.Attribute: classification<classification id=testpolygon.3>=1, SimpleFeatureImpl.Attribute: count<count id=testpolygon.3>=397, SimpleFeatureImpl.Attribute: min<min id=testpolygon.3>=1178.0, SimpleFeatureImpl.Attribute: max<max id=testpolygon.3>=1351.0, SimpleFeatureImpl.Attribute: sum<sum id=testpolygon.3>=508039.0, SimpleFeatureImpl.Attribute: avg<avg id=testpolygon.3>=1279.6952141057934, SimpleFeatureImpl.Attribute: stddev<stddev id=testpolygon.3>=34.76567014271108]");
        results.put(
                "testpolygon.32",
                "SimpleFeatureImpl:testpolygon=[SimpleFeatureImpl.Attribute: z_the_geom<z_the_geom id=testpolygon.3>=MULTIPOLYGON (((11.897871315022112 46.20812076814061, 11.898581076580497 46.20816171238038, 11.898470449632523 46.20767324666812, 11.897871315022112 46.20812076814061))), SimpleFeatureImpl.Attribute: z_name<z_name id=testpolygon.3>=, SimpleFeatureImpl.Attribute: classification<classification id=testpolygon.3>=2, SimpleFeatureImpl.Attribute: count<count id=testpolygon.3>=382, SimpleFeatureImpl.Attribute: min<min id=testpolygon.3>=1189.0, SimpleFeatureImpl.Attribute: max<max id=testpolygon.3>=1348.0, SimpleFeatureImpl.Attribute: sum<sum id=testpolygon.3>=489371.0, SimpleFeatureImpl.Attribute: avg<avg id=testpolygon.3>=1281.0759162303666, SimpleFeatureImpl.Attribute: stddev<stddev id=testpolygon.3>=32.4464751422778]");
    }

    @Before
    public void setup() throws Exception {
        process = new RasterZonalStatistics();
        File file = TestData.file(this, null);
        ds = new PropertyDataStore(file);
        TestData.unzipFile(this, "test.zip");
    }

    @After
    public void tearDown() {
        ds.dispose();
    }

    @Test
    public void simpleZonalStatsProcess() throws Exception {
        DataStore store = null;
        TIFFImageReader reader = null;
        GridCoverage2D coverage2D = null;
        GridCoverage2D covClassificator = null;
        try {
            // build the feature collection
            final File fileshp = TestData.file(this, "testpolygon.shp");
            store = FileDataStoreFinder.getDataStore(fileshp.toURI().toURL());
            assertNotNull(store);
            assertTrue(store instanceof ShapefileDataStore);
            FeatureSource<SimpleFeatureType, SimpleFeature> featureSource =
                    store.getFeatureSource(store.getNames().get(0));
            SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) featureSource.getFeatures();

            // build the DataFile
            final File tiff = TestData.file(this, "test.tif");
            final File tfw = TestData.file(this, "test.tfw");
            reader = (it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader)
                    new TIFFImageReaderSpi().createReaderInstance();
            assertNotNull(reader);
            reader.setInput(ImageIO.createImageInputStream(tiff));
            final BufferedImage image = reader.read(0);

            final MathTransform transform = new WorldFileReader(tfw).getTransform();
            coverage2D = CoverageFactoryFinder.getGridCoverageFactory(null)
                    .create(
                            "coverage",
                            image,
                            new GridGeometry2D(
                                    new GridEnvelope2D(
                                            PlanarImage.wrapRenderedImage(image).getBounds()),
                                    transform,
                                    DefaultGeographicCRS.WGS84),
                            new GridSampleDimension[] {new GridSampleDimension("coverage")},
                            null,
                            null);
            assertNotNull(coverage2D);

            // build the classificator
            // generate the classificator image
            final BufferedImage imageClassificator = new BufferedImage(120, 80, BufferedImage.TYPE_BYTE_INDEXED);
            final WritableRaster raster = imageClassificator.getRaster();
            for (int i = raster.getWidth(); --i >= 0; ) {
                for (int j = raster.getHeight(); --j >= 0; ) {
                    // create a simple raster used for classification
                    int sampleValue = i % 2 == 0 ? 1 : 2;
                    raster.setSample(i, j, 0, sampleValue);
                }
            }
            // create the coverage for the classification layer
            covClassificator = CoverageFactoryFinder.getGridCoverageFactory(null)
                    .create(
                            "coverageClassificator",
                            imageClassificator,
                            new GridGeometry2D(
                                    new GridEnvelope2D(PlanarImage.wrapRenderedImage(imageClassificator)
                                            .getBounds()),
                                    coverage2D.getEnvelope()),
                            new GridSampleDimension[] {new GridSampleDimension("coverage")},
                            null,
                            null);
            assertNotNull(coverage2D);

            // invoke the process
            SimpleFeatureCollection sfc = process.execute(coverage2D, null, featureCollection, covClassificator);

            try (SimpleFeatureIterator iterator = sfc.features()) {
                assertNotNull(iterator);

                while (iterator.hasNext()) {
                    SimpleFeature feature = iterator.next();
                    String sampleId = feature.getID() + feature.getAttribute("classification");
                    assertEquals(sampleId + " is not equals", feature.toString(), results.get(sampleId));
                }
            }

        } finally {
            try {
                if (store != null) {
                    store.dispose();
                }
            } catch (Exception e) {
            }
            try {
                if (reader != null) {
                    reader.dispose();
                }
            } catch (Exception e) {
            }
            try {
                if (coverage2D != null) {
                    coverage2D.dispose(true);
                }
            } catch (Exception e) {
            }
            try {
                if (covClassificator != null) {
                    covClassificator.dispose(true);
                }
            } catch (Exception e) {
            }
        }
    }

    @Test
    public void testStatisticsOnZeroes() {
        CoordinateReferenceSystem crs = DefaultEngineeringCRS.GENERIC_2D;

        // prepare a BW image in 3857
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 50, 100);
        graphics.dispose();
        final MathTransform transform = new AffineTransform2D(AffineTransform.getScaleInstance(1, 1));
        GridCoverage2D coverage2D = CoverageFactoryFinder.getGridCoverageFactory(null)
                .create(
                        "coverage",
                        image,
                        new GridGeometry2D(
                                new GridEnvelope2D(
                                        PlanarImage.wrapRenderedImage(image).getBounds()),
                                transform,
                                crs),
                        new GridSampleDimension[] {new GridSampleDimension("coverage")},
                        null,
                        null);
        assertNotNull(coverage2D);

        // prepare one rectangle feature that covers (and more) the top half of the raster
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.add("geom", Polygon.class, crs);
        tb.add("cat", Integer.class);
        tb.setName("zones");
        SimpleFeatureType schema = tb.buildFeatureType();
        Polygon poly = JTS.toGeometry(new Envelope(-10, 110, -10, 50));
        SimpleFeature feature = SimpleFeatureBuilder.build(schema, new Object[] {poly, Integer.valueOf(1)}, "fid123");
        SimpleFeatureCollection features = DataUtilities.collection(feature);

        RasterZonalStatistics stats = new RasterZonalStatistics();
        SimpleFeatureCollection results = stats.execute(coverage2D, 0, features, coverage2D);
        List<SimpleFeature> resultList = DataUtilities.list(results);

        assertEquals(2, resultList.size());
        Map<Integer, Integer> countsSummary = new LinkedHashMap<>();
        for (SimpleFeature sf : resultList) {
            int cloud = ((Number) sf.getAttribute("max")).intValue();
            int count = ((Number) sf.getAttribute("count")).intValue();
            countsSummary.compute(cloud, (k, v) -> v == null ? count : v + count);
        }
        // evenly split
        assertEquals(2499, countsSummary.get(0), 0d);
        assertEquals(2499, countsSummary.get(1), 0d);
    }

    @Test
    public void testStatisticsNoClassifier() {
        CoordinateReferenceSystem crs = DefaultEngineeringCRS.GENERIC_2D;

        // prepare a BW image in 3857
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 50, 100);
        graphics.dispose();
        final MathTransform transform = new AffineTransform2D(AffineTransform.getScaleInstance(1, 1));
        GridCoverage2D coverage2D = CoverageFactoryFinder.getGridCoverageFactory(null)
                .create(
                        "coverage",
                        image,
                        new GridGeometry2D(
                                new GridEnvelope2D(
                                        PlanarImage.wrapRenderedImage(image).getBounds()),
                                transform,
                                crs),
                        new GridSampleDimension[] {new GridSampleDimension("coverage")},
                        null,
                        null);
        assertNotNull(coverage2D);

        // prepare one rectangle feature that covers (and more) the top half of the raster
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.add("geom", Polygon.class, crs);
        tb.add("cat", Integer.class);
        tb.setName("zones");
        SimpleFeatureType schema = tb.buildFeatureType();
        Polygon poly = JTS.toGeometry(new Envelope(-10, 110, -10, 50));
        SimpleFeature feature = SimpleFeatureBuilder.build(schema, new Object[] {poly, Integer.valueOf(1)}, "fid123");
        SimpleFeatureCollection features = DataUtilities.collection(feature);

        RasterZonalStatistics stats = new RasterZonalStatistics();
        SimpleFeatureCollection results = stats.execute(coverage2D, 0, features, null);
        List<SimpleFeature> resultList = DataUtilities.list(results);

        assertEquals(1, resultList.size());
        SimpleFeature result = resultList.get(0);
        assertEquals(1d, result.getAttribute("max"));
        assertEquals(5000l, result.getAttribute("count"));
    }
}
