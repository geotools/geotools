///*
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

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.media.jai.PlanarImage;

import junit.framework.Assert;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.WorldFileReader;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.operation.MathTransform;

/**
 * @author DamianoG
 * 
 */
public class ZonalStatsProcessTest extends Assert {

    private RasterZonalStatistics process;

    private DataStore ds;

    private static Map<String, String> results = null;
    static {
        results = new HashMap<String, String>();
        results.put(
                "testpolygon.11",
                "SimpleFeatureImpl:testpolygon=[SimpleFeatureImpl.Attribute: z_the_geom<z_the_geom id=testpolygon.1>=MULTIPOLYGON (((11.89513017802011 46.20793481460109, 11.895809089115916 46.20792743071521, 11.895112009452415 46.2070509839355, 11.89513017802011 46.20793481460109))), SimpleFeatureImpl.Attribute: z_name<z_name id=testpolygon.1>=, SimpleFeatureImpl.Attribute: classification<classification id=testpolygon.1>=1, SimpleFeatureImpl.Attribute: count<count id=testpolygon.1>=661, SimpleFeatureImpl.Attribute: min<min id=testpolygon.1>=1251.0, SimpleFeatureImpl.Attribute: max<max id=testpolygon.1>=1630.0, SimpleFeatureImpl.Attribute: sum<sum id=testpolygon.1>=894829.0, SimpleFeatureImpl.Attribute: avg<avg id=testpolygon.1>=1353.7503782148278, SimpleFeatureImpl.Attribute: stddev<stddev id=testpolygon.1>=69.14742498772046]");
        results.put(
                "testpolygon.12",
                "SimpleFeatureImpl:testpolygon=[SimpleFeatureImpl.Attribute: z_the_geom<z_the_geom id=testpolygon.1>=MULTIPOLYGON (((11.89513017802011 46.20793481460109, 11.895809089115916 46.20792743071521, 11.895112009452415 46.2070509839355, 11.89513017802011 46.20793481460109))), SimpleFeatureImpl.Attribute: z_name<z_name id=testpolygon.1>=, SimpleFeatureImpl.Attribute: classification<classification id=testpolygon.1>=2, SimpleFeatureImpl.Attribute: count<count id=testpolygon.1>=696, SimpleFeatureImpl.Attribute: min<min id=testpolygon.1>=1251.0, SimpleFeatureImpl.Attribute: max<max id=testpolygon.1>=1589.0, SimpleFeatureImpl.Attribute: sum<sum id=testpolygon.1>=944959.0, SimpleFeatureImpl.Attribute: avg<avg id=testpolygon.1>=1357.6997126436784, SimpleFeatureImpl.Attribute: stddev<stddev id=testpolygon.1>=70.2289804693121]");
        results.put(
                "testpolygon.21",
                "SimpleFeatureImpl:testpolygon=[SimpleFeatureImpl.Attribute: z_the_geom<z_the_geom id=testpolygon.2>=MULTIPOLYGON (((11.896630238926786 46.207395686643665, 11.897000925691524 46.207985638846736, 11.897753464657697 46.20749492858355, 11.896630238926786 46.207395686643665))), SimpleFeatureImpl.Attribute: z_name<z_name id=testpolygon.2>=, SimpleFeatureImpl.Attribute: classification<classification id=testpolygon.2>=1, SimpleFeatureImpl.Attribute: count<count id=testpolygon.2>=722, SimpleFeatureImpl.Attribute: min<min id=testpolygon.2>=1191.0, SimpleFeatureImpl.Attribute: max<max id=testpolygon.2>=1411.0, SimpleFeatureImpl.Attribute: sum<sum id=testpolygon.2>=904757.0, SimpleFeatureImpl.Attribute: avg<avg id=testpolygon.2>=1253.1260387811649, SimpleFeatureImpl.Attribute: stddev<stddev id=testpolygon.2>=42.393728281454365]");
        results.put(
                "testpolygon.22",
                "SimpleFeatureImpl:testpolygon=[SimpleFeatureImpl.Attribute: z_the_geom<z_the_geom id=testpolygon.2>=MULTIPOLYGON (((11.896630238926786 46.207395686643665, 11.897000925691524 46.207985638846736, 11.897753464657697 46.20749492858355, 11.896630238926786 46.207395686643665))), SimpleFeatureImpl.Attribute: z_name<z_name id=testpolygon.2>=, SimpleFeatureImpl.Attribute: classification<classification id=testpolygon.2>=2, SimpleFeatureImpl.Attribute: count<count id=testpolygon.2>=704, SimpleFeatureImpl.Attribute: min<min id=testpolygon.2>=1192.0, SimpleFeatureImpl.Attribute: max<max id=testpolygon.2>=1430.0, SimpleFeatureImpl.Attribute: sum<sum id=testpolygon.2>=883082.0, SimpleFeatureImpl.Attribute: avg<avg id=testpolygon.2>=1254.3778409090917, SimpleFeatureImpl.Attribute: stddev<stddev id=testpolygon.2>=43.45595854784222]");
        results.put(
                "testpolygon.31",
                "SimpleFeatureImpl:testpolygon=[SimpleFeatureImpl.Attribute: z_the_geom<z_the_geom id=testpolygon.3>=MULTIPOLYGON (((11.897871315022112 46.20812076814061, 11.898581076580497 46.20816171238038, 11.898470449632523 46.20767324666812, 11.897871315022112 46.20812076814061))), SimpleFeatureImpl.Attribute: z_name<z_name id=testpolygon.3>=, SimpleFeatureImpl.Attribute: classification<classification id=testpolygon.3>=1, SimpleFeatureImpl.Attribute: count<count id=testpolygon.3>=398, SimpleFeatureImpl.Attribute: min<min id=testpolygon.3>=1178.0, SimpleFeatureImpl.Attribute: max<max id=testpolygon.3>=1351.0, SimpleFeatureImpl.Attribute: sum<sum id=testpolygon.3>=509230.0, SimpleFeatureImpl.Attribute: avg<avg id=testpolygon.3>=1279.472361809045, SimpleFeatureImpl.Attribute: stddev<stddev id=testpolygon.3>=35.005332302692366]");
        results.put(
                "testpolygon.32",
                "SimpleFeatureImpl:testpolygon=[SimpleFeatureImpl.Attribute: z_the_geom<z_the_geom id=testpolygon.3>=MULTIPOLYGON (((11.897871315022112 46.20812076814061, 11.898581076580497 46.20816171238038, 11.898470449632523 46.20767324666812, 11.897871315022112 46.20812076814061))), SimpleFeatureImpl.Attribute: z_name<z_name id=testpolygon.3>=, SimpleFeatureImpl.Attribute: classification<classification id=testpolygon.3>=2, SimpleFeatureImpl.Attribute: count<count id=testpolygon.3>=383, SimpleFeatureImpl.Attribute: min<min id=testpolygon.3>=1189.0, SimpleFeatureImpl.Attribute: max<max id=testpolygon.3>=1348.0, SimpleFeatureImpl.Attribute: sum<sum id=testpolygon.3>=490572.0, SimpleFeatureImpl.Attribute: avg<avg id=testpolygon.3>=1280.8668407310697, SimpleFeatureImpl.Attribute: stddev<stddev id=testpolygon.3>=32.66128762163009]");
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
        SimpleFeatureIterator iterator = null;
        try {
            // build the feature collection
            final File fileshp = TestData.file(this, "testpolygon.shp");
            store = FileDataStoreFinder.getDataStore(fileshp.toURI().toURL());
            assertNotNull(store);
            assertTrue(store instanceof ShapefileDataStore);
            FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = store
                    .getFeatureSource(store.getNames().get(0));
            SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) featureSource
                    .getFeatures();

            // build the DataFile
            final File tiff = TestData.file(this, "test.tif");
            final File tfw = TestData.file(this, "test.tfw");
            reader = (it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader) new TIFFImageReaderSpi()
                    .createReaderInstance();
            assertNotNull(reader);
            reader.setInput(ImageIO.createImageInputStream(tiff));
            final BufferedImage image = reader.read(0);

            final MathTransform transform = new WorldFileReader(tfw).getTransform();
            coverage2D = CoverageFactoryFinder.getGridCoverageFactory(null).create(
                    "coverage",
                    image,
                    new GridGeometry2D(new GridEnvelope2D(PlanarImage.wrapRenderedImage(image)
                            .getBounds()), transform, DefaultGeographicCRS.WGS84),
                    new GridSampleDimension[] { new GridSampleDimension("coverage") }, null, null);
            assertNotNull(coverage2D);

            // build the classificator
            // generate the classificator image
            final BufferedImage imageClassificator = new BufferedImage(120, 80,
                    BufferedImage.TYPE_BYTE_INDEXED);
            final WritableRaster raster = imageClassificator.getRaster();
            for (int i = raster.getWidth(); --i >= 0;) {
                for (int j = raster.getHeight(); --j >= 0;) {
                    // create a simple raster used for classification
                    int sampleValue = (i % 2 == 0) ? 1 : 2;
                    raster.setSample(i, j, 0, sampleValue);
                }
            }
            // create the coverage for the classification layer
            covClassificator = CoverageFactoryFinder.getGridCoverageFactory(null).create(
                    "coverageClassificator",
                    imageClassificator,
                    new GridGeometry2D(new GridEnvelope2D(PlanarImage.wrapRenderedImage(
                            imageClassificator).getBounds()), coverage2D.getEnvelope()),
                    new GridSampleDimension[] { new GridSampleDimension("coverage") }, null, null);
            assertNotNull(coverage2D);

            // invoke the process
            SimpleFeatureCollection sfc = process.execute(coverage2D, null, featureCollection,
                    covClassificator);

            iterator = sfc.features();
            assertNotNull(iterator);

            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                assertTrue((feature.toString()).equals(results.get(feature.getID()
                        + feature.getAttribute("classification"))));
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
            try {
                if (iterator != null) {
                    iterator.close();
                }
            } catch (Exception e) {
            }

        }
    }
}
