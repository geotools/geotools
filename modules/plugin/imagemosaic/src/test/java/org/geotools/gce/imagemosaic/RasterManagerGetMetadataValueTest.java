/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.RenderingHints;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Logger;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.factory.Hints;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.gce.imagemosaic.catalog.GTDataStoreGranuleCatalog;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.referencing.CRS;
import org.geotools.test.TestData;
import org.geotools.util.URLs;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.opengis.feature.FeatureVisitor;

/** @author Ivan Martynovskyi */
@RunWith(Parameterized.class)
public class RasterManagerGetMetadataValueTest {

    private static final Logger LOGGER = Logger.getLogger(ImageMosaicReaderTest.class.toString());
    private static boolean INTERACTIVE;

    private URL timeURL;
    private ImageMosaicReader reader = null;
    private RasterManager manager = null;
    private String attribute = null;
    private boolean isHintContainFeatureCalc = false;
    private ArrayList<FeatureCalc> listOfVisitors = new ArrayList<>();
    private String typeDomain;
    private boolean expectedContainsKey;
    private boolean expectedContainsFeatureCalc;
    private int expectedCntOfVisitors;
    ArrayList<Class<? extends FeatureVisitor>> expectedListOfVisitors;

    /**
     * Cleaning up the generated files (shape and properties so that we recreate them.
     *
     * @throws FileNotFoundException
     * @throws Exception
     */
    private void cleanUp() throws Exception {
        if (INTERACTIVE) return;
        File dir = TestData.file(this, "overview/");
        File[] files =
                dir.listFiles(
                        (FilenameFilter)
                                FileFilterUtils.notFileFilter(
                                        FileFilterUtils.or(
                                                FileFilterUtils.or(
                                                        FileFilterUtils.suffixFileFilter("tif"),
                                                        FileFilterUtils.suffixFileFilter("aux")),
                                                FileFilterUtils.nameFileFilter(
                                                        "datastore.properties"))));
        for (File file : files) {
            file.delete();
        }

        dir = TestData.file(this, "rgba/");
        files =
                dir.listFiles(
                        (FilenameFilter)
                                FileFilterUtils.notFileFilter(
                                        FileFilterUtils.or(
                                                FileFilterUtils.notFileFilter(
                                                        FileFilterUtils.suffixFileFilter("png")),
                                                FileFilterUtils.notFileFilter(
                                                        FileFilterUtils.suffixFileFilter("wld")))));
        for (File file : files) {
            file.delete();
        }

        dir = TestData.file(this, "time_domainsRanges");
        files =
                dir.listFiles(
                        (FilenameFilter)
                                FileFilterUtils.or(
                                        FileFilterUtils.suffixFileFilter("shp"),
                                        FileFilterUtils.suffixFileFilter("dbf"),
                                        FileFilterUtils.suffixFileFilter("qix"),
                                        FileFilterUtils.suffixFileFilter("shx"),
                                        FileFilterUtils.suffixFileFilter("prj")));
        for (File file : files) {
            file.delete();
        }
    }

    @BeforeClass
    public static void init() {
        // make sure CRS ordering is correct
        CRS.reset("all");
        System.setProperty("org.geotools.referencing.forceXY", "true");
        System.setProperty("user.timezone", "GMT");
        System.setProperty("org.geotools.shapefile.datetime", "true");
        INTERACTIVE = TestData.isInteractiveTest();
    }

    @Parameterized.Parameters
    public static Collection getListOfCases() {
        ArrayList<Class<? extends FeatureVisitor>> emptyList = new ArrayList<>();
        ArrayList<Class<? extends FeatureVisitor>> listUniqueVisitor = new ArrayList<>();
        listUniqueVisitor.add(UniqueVisitor.class);
        ArrayList<Class<? extends FeatureVisitor>> listMinVisitor = new ArrayList<>();
        listMinVisitor.add(MinVisitor.class);
        ArrayList<Class<? extends FeatureVisitor>> listMaxVisitor = new ArrayList<>();
        listMaxVisitor.add(MaxVisitor.class);
        ArrayList<Class<? extends FeatureVisitor>> listMinMaxVisitor = new ArrayList<>();
        listMinMaxVisitor.add(MinVisitor.class);
        listMinMaxVisitor.add(MaxVisitor.class);

        /**
         * Creates cases for testing Col1: flag shows rasterManager hints contain
         * DIMENSIONS_PRESENTATIONS_INFO entry Col2: flag shows the presentation of the dimension:
         * true - LIST; false - INTERVAL Col3: string contains the value of the domain type uses to
         * obtain metadata Col4: flag shows expected containing FeatureCalc objects in the hints of
         * the query Col5: expected list FeatureCalc objects in the hints of the query
         */
        return Arrays.asList(
                new Object[][] {
                    {false, false, GridCoverage2DReader.TIME_DOMAIN, false, emptyList},
                    {false, false, GridCoverage2DReader.TIME_DOMAIN_MINIMUM, false, emptyList},
                    {true, true, GridCoverage2DReader.TIME_DOMAIN, true, listUniqueVisitor},
                    {true, true, GridCoverage2DReader.TIME_DOMAIN_MINIMUM, true, listMinVisitor},
                    {true, true, GridCoverage2DReader.TIME_DOMAIN_MAXIMUM, true, listMaxVisitor},
                    {true, false, GridCoverage2DReader.TIME_DOMAIN, true, listMinMaxVisitor},
                    {true, false, GridCoverage2DReader.TIME_DOMAIN_MINIMUM, true, listMinVisitor},
                    {true, false, GridCoverage2DReader.TIME_DOMAIN_MAXIMUM, true, listMaxVisitor}
                });
    }

    public RasterManagerGetMetadataValueTest(
            boolean containsDimPresentInfoHint,
            boolean isListDimension,
            String typeDomain,
            boolean expectedContainsFeatureCalc,
            ArrayList<Class<? extends FeatureVisitor>> expectedListOfVisitors)
            throws Exception {
        // remove generated file
        cleanUp();
        timeURL = TestData.url(this, "time_geotiff");
        isHintContainFeatureCalc = false;
        listOfVisitors.clear();
        final AbstractGridFormat format = TestUtils.getFormat(timeURL);
        reader = TestUtils.getReader(timeURL, format);
        String coverageName = reader.getGridCoverageNames()[0];
        manager = reader.getRasterManager(coverageName);
        attribute = manager.getConfiguration().getTimeAttribute();

        GranuleCatalog modifiedGranuleCatalog =
                new GTDataStoreGranuleCatalog(
                        initParamsForTest(timeURL, coverageName),
                        false,
                        Utils.SHAPE_SPI,
                        reader.getHints()) {
                    @Override
                    public void computeAggregateFunction(Query q, FeatureCalc function)
                            throws IOException {
                        Hints queryHints = q.getHints();
                        isHintContainFeatureCalc = queryHints.containsKey(Hints.FEATURE_CALC);
                        if (isHintContainFeatureCalc) {
                            listOfVisitors.add((FeatureCalc) queryHints.get(Hints.FEATURE_CALC));
                        }
                        super.computeAggregateFunction(q, function);
                    }
                };

        manager.granuleCatalog = modifiedGranuleCatalog;

        if (containsDimPresentInfoHint) {
            Map<String, Boolean> dimPresentationData = new HashMap<>();
            dimPresentationData.put(attribute, isListDimension);
            manager.getHints()
                    .add(
                            new RenderingHints(
                                    Hints.DIMENSIONS_PRESENTATIONS_INFO, dimPresentationData));
        }
        this.typeDomain = typeDomain;
        this.expectedContainsKey = containsDimPresentInfoHint;
        this.expectedContainsFeatureCalc = expectedContainsFeatureCalc;
        this.expectedCntOfVisitors = expectedListOfVisitors.size();
        this.expectedListOfVisitors = expectedListOfVisitors;
    }

    @Test
    public void testGetMetadataValue() throws Exception {
        boolean containsHint =
                manager.getHints().containsKey(Hints.DIMENSIONS_PRESENTATIONS_INFO)
                        == expectedContainsKey;
        assertTrue(containsHint);
        reader.getMetadataValue(typeDomain);
        assertTrue(isHintContainFeatureCalc == expectedContainsFeatureCalc);
        assertEquals(expectedCntOfVisitors, listOfVisitors.size());
        for (int i = 0; i < listOfVisitors.size(); i++) {
            assertTrue(listOfVisitors.get(i).getClass() == expectedListOfVisitors.get(i));
        }
    }

    private Properties initParamsForTest(final URL sourceURL, final String typeName) {
        // STANDARD PARAMS
        final Properties params = new Properties();

        params.put(Utils.Prop.PATH_TYPE, PathType.RELATIVE);
        params.put(Utils.Prop.LOCATION_ATTRIBUTE, "location");
        params.put(
                Utils.Prop.SUGGESTED_SPI,
                "it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi");
        params.put(Utils.Prop.HETEROGENEOUS, false);
        params.put(Utils.Prop.WRAP_STORE, false);
        if (sourceURL != null) {
            File parentDirectory = URLs.urlToFile(sourceURL);
            if (parentDirectory.isFile()) parentDirectory = parentDirectory.getParentFile();
            params.put(Utils.Prop.PARENT_LOCATION, URLs.fileToUrl(parentDirectory).toString());
        } else params.put(Utils.Prop.PARENT_LOCATION, null);
        params.put(Utils.Prop.TYPENAME, typeName);
        params.put(ShapefileDataStoreFactory.URLP.key, sourceURL);
        params.put(ShapefileDataStoreFactory.CREATE_SPATIAL_INDEX.key, Boolean.TRUE);
        params.put(ShapefileDataStoreFactory.ENABLE_SPATIAL_INDEX.key, Boolean.TRUE);
        params.put(ShapefileDataStoreFactory.MEMORY_MAPPED.key, Boolean.FALSE);
        params.put(ShapefileDataStoreFactory.CACHE_MEMORY_MAPS.key, Boolean.FALSE);
        params.put(ShapefileDataStoreFactory.DBFTIMEZONE.key, TimeZone.getTimeZone("UTC"));

        return params;
    }
}
