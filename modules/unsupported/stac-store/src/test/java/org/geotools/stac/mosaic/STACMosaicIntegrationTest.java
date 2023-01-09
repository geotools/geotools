/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.stac.mosaic;

import static org.junit.Assert.assertNotNull;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DefaultRepository;
import org.geotools.gce.imagemosaic.ImageMosaicFormat;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.image.test.ImageAssert;
import org.geotools.referencing.CRS;
import org.geotools.stac.STACOfflineTest;
import org.geotools.stac.store.AbstractSTACStoreTest;
import org.geotools.util.factory.Hints;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.parameter.ParameterValue;

/** Tests integration between STAC store and mosaic store */
public class STACMosaicIntegrationTest extends AbstractSTACStoreTest {

    protected static String WSF_TYPE =
            BASE_URL + "/search?f=application%2Fgeo%2Bjson&collections=WSF_2019&limit=100";
    protected static String WSF_2 =
            BASE_URL + "/search?f=application%2Fgeo%2Bjson&collections=WSF_2019&limit=2";

    protected static String WSF_BOUNDS =
            BASE_URL
                    + "/search?f=application%2Fgeo%2Bjson&collections=WSF_2019&limit=1000&fields=geometry,type,id,-bbox,-properties,-assets,-links";

    protected static String WSF_CRS_BOUNDS =
            BASE_URL
                    + "/search?f=application%2Fgeo%2Bjson&collections=WSF_2019&limit=1000&fields=geometry,assets.wsf2019.href,properties.proj:geometry,-assets,-links&FILTER=\"proj:epsg\" %3D 4326 AND BBOX(geometry, -76.01004945048919,39.98994217703509,-71.98999872252591,42.01007358796307)&FILTER-LANG=cql2-text";

    protected static String WSF_ITEMS =
            BASE_URL
                    + "/search?f=application%2Fgeo%2Bjson&collections=WSF_2019&limit=1000&bbox=-76.01004945048919,39.98994217703509,-71.98999872252591,42.01007358796307&fields=geometry,assets.wsf2019.href,properties.proj:epsg,type,id,-bbox,-properties,-assets,-links";

    protected static String WSF_CRS =
            BASE_URL
                    + "/search?f=application%2Fgeo%2Bjson&collections=WSF_2019&limit=1&FILTER=%22proj%3Aepsg%22%20%3D%20%27EPSG%3A4326%27&FILTER-LANG=cql2-text&FIELDS=properties.proj:epsg,type,id,-links";

    @AfterClass
    public static void cleanupCRS() {
        System.clearProperty("org.geotools.referencing.forceXY");
        CRS.reset("all");
    }

    @BeforeClass
    public static void initCRS() {
        // make sure CRS ordering is correct
        CRS.reset("all");
        System.setProperty("org.geotools.referencing.forceXY", "true");
    }

    @Override
    public void setup() throws IOException {
        super.setup();

        Class<?> cls = STACOfflineTest.class;
        httpClient.expectGet(new URL(WSF_TYPE), geojsonResponse("wsf.json", cls));
        httpClient.expectGet(new URL(WSF_2), geojsonResponse("wsf.json", cls));
        httpClient.expectGet(new URL(WSF_BOUNDS), geojsonResponse("wsf.json", cls));
        httpClient.expectGet(new URL(WSF_ITEMS), geojsonResponse("wsf.json", cls));
        httpClient.expectGet(new URL(WSF_CRS), geojsonResponse("wsf.json", cls));
        httpClient.expectGet(new URL(WSF_CRS_BOUNDS), geojsonResponse("wsf.json", cls));
    }

    @Test
    public void testWSFMosaic() throws Exception {
        File source = new File("./src/test/resources/org/geotools/stac/mosaic");
        File testDataDir = new File("./target/mosaic");
        if (testDataDir.exists()) {
            FileUtils.deleteDirectory(testDataDir);
        }
        FileUtils.copyDirectory(source, testDataDir);

        DefaultRepository repository = new DefaultRepository();
        repository.register("stac", store);
        Hints hints = new Hints(Hints.REPOSITORY, repository);
        ImageMosaicReader reader = new ImageMosaicReader(testDataDir, hints);
        assertNotNull(reader);

        ParameterValue<Color> intx = ImageMosaicFormat.INPUT_TRANSPARENT_COLOR.createValue();
        intx.setValue(Color.BLACK);
        GridCoverage2D coverage = reader.read(null);
        File expected = new File("src/test/resources/org/geotools/stac/wsf.png");
        ImageAssert.assertEquals(expected, coverage.getRenderedImage(), 0);
    }
}
