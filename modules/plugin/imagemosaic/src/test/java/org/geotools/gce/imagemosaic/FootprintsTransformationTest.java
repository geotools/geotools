/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

import static java.util.Arrays.asList;
import static org.geotools.gce.imagemosaic.TestUtils.getReader;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.CoverageReadingTransformation.ReaderAndParams;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.geotools.util.NumberRange;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.locationtech.jts.geom.MultiPolygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;

public class FootprintsTransformationTest {

    static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();
    static final FootprintsTransformation TX = new FootprintsTransformation();

    @Test
    public void lookupTest() {
        Function footprints = FF.function("footprints");
        assertNotNull(footprints);
        assertThat(footprints, CoreMatchers.instanceOf(FootprintsTransformation.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgument() throws Exception {
        FootprintsTransformation tx = new FootprintsTransformation();
        tx.evaluate(new Object());
    }

    @Test
    public void testBoundingBox() throws Exception {
        URL rgbURL = TestData.url(this, "rgb");
        final AbstractGridFormat format = TestUtils.getFormat(rgbURL);
        final ImageMosaicReader reader = getReader(rgbURL, format);
        try {

            // a bounding box that is matching one tile only
            ReferencedEnvelope re =
                    new ReferencedEnvelope(9.25, 12, 42.56, 44.57, DefaultGeographicCRS.WGS84);
            final ParameterValue<GridGeometry2D> gg =
                    AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
            gg.setValue(new GridGeometry2D(new GridEnvelope2D(0, 0, 50, 50), re));

            ReaderAndParams ctx = new ReaderAndParams(reader, new GeneralParameterValue[] {gg});
            SimpleFeatureCollection fc = TX.evaluate(ctx);

            // no dimensions, just footprint and location
            SimpleFeatureType schema = fc.getSchema();
            assertEquals(2, schema.getAttributeCount());
            assertEquals(MultiPolygon.class, getBinding(schema, "the_geom"));
            assertEquals(String.class, getBinding(schema, "location"));

            // one feature
            assertEquals(1, fc.size());
            SimpleFeature first = DataUtilities.first(fc);

            // the right feature
            assertEquals("global_mosaic_16.png", first.getAttribute("location"));
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testSortMaxTiles() throws Exception {
        URL rgbURL = TestData.url(this, "rgb");
        final AbstractGridFormat format = TestUtils.getFormat(rgbURL);
        final ImageMosaicReader reader = getReader(rgbURL, format);
        try {
            // mind, this is lexicographic sorting
            assertOneGranuleSort(reader, "location A", "global_mosaic_0.png");
            assertOneGranuleSort(reader, "location D", "global_mosaic_9.png");
        } finally {
            reader.dispose();
        }
    }

    private void assertOneGranuleSort(
            ImageMosaicReader reader, String sortValue, String expectedLocation) {
        ParameterValue<String> sort = ImageMosaicFormat.SORT_BY.createValue();
        sort.setValue(sortValue);

        ParameterValue<Integer> maxTiles = ImageMosaicFormat.MAX_ALLOWED_TILES.createValue();
        maxTiles.setValue(1);

        ReaderAndParams ctx =
                new ReaderAndParams(reader, new GeneralParameterValue[] {sort, maxTiles});
        SimpleFeatureCollection fc = TX.evaluate(ctx);

        // one feature
        assertEquals(1, fc.size());
        SimpleFeature first = DataUtilities.first(fc);
        assertEquals(expectedLocation, first.getAttribute("location"));
    }

    /** Using CQL filter to access dimension values */
    @Test
    public void testCQLFilter() throws Exception {
        final URL timeElevURL = setupWaterTemp("watertemp-cql");
        final AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        ImageMosaicReader reader = getReader(timeElevURL, format);
        try {

            ParameterValue<Filter> filter = ImageMosaicFormat.FILTER.createValue();
            filter.setValue(
                    CQL.toFilter("elevation = 0 and ingestion TEQUALS 2008-11-01T00:00:00Z"));

            ReaderAndParams ctx = new ReaderAndParams(reader, new GeneralParameterValue[] {filter});
            SimpleFeatureCollection fc = TX.evaluate(ctx);

            assertEquals(1, fc.size());
            SimpleFeature first = DataUtilities.first(fc);

            assertEquals(
                    "NCOM_wattemp_000_20081101T0000000_12.tiff", first.getAttribute("location"));
        } finally {
            reader.dispose();
        }
    }

    /**
     * Same as above, but using dimensions instead of a CQL filter
     *
     * @throws Exception
     */
    @Test
    public void testTimeElevation() throws Exception {
        final URL timeElevURL = setupWaterTemp("watertemp-time-elev");
        final AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        ImageMosaicReader reader = getReader(timeElevURL, format);

        try {
            ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
            time.setValue(asList(parseISO("2008-11-01T00:00:00Z")));

            ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
            elevation.setValue(asList(0));

            ReaderAndParams ctx =
                    new ReaderAndParams(reader, new GeneralParameterValue[] {time, elevation});
            SimpleFeatureCollection fc = TX.evaluate(ctx);

            assertEquals(1, fc.size());
            SimpleFeature first = DataUtilities.first(fc);

            assertEquals(
                    "NCOM_wattemp_000_20081101T0000000_12.tiff", first.getAttribute("location"));
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testTimeRange() throws Exception {
        URL ranges_customdims = TestData.url(this, "time_domainsRanges");
        final AbstractGridFormat format = TestUtils.getFormat(ranges_customdims);
        ImageMosaicReader reader = getReader(ranges_customdims, format);
        try {

            ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
            ReaderAndParams ctx = new ReaderAndParams(reader, new GeneralParameterValue[] {time});

            // range above
            time.setValue(
                    asList(
                            parseISO("2008-11-20T00:00:00.000Z"),
                            parseISO("2008-11-25T12:00:00.000Z")));

            SimpleFeatureCollection fc = TX.evaluate(ctx);
            assertTrue(fc.isEmpty());

            // range in hole
            time.setValue(
                    asList(
                            parseISO("2008-11-04T12:00:00.000Z"),
                            parseISO("2008-11-04T18:00:00.000Z")));
            fc = TX.evaluate(ctx);
            assertTrue(fc.isEmpty());

            // range that catches the first date range
            time.setValue(
                    asList(
                            parseISO("2008-10-28T00:00:00.000Z"),
                            parseISO("2008-10-31T18:00:00.000Z")));
            fc = TX.evaluate(ctx);
            assertEquals(4, fc.size());
            Set<String> locations = collectLocations(fc);
            assertThat(
                    locations,
                    hasItems(
                            "temp_020_099_20081031T000000_20081103T000000_12_24.tiff",
                            "temp_020_099_20081031T000000_20081103T000000_25_80.tiff",
                            "temp_100_150_20081031T000000_20081103T000000_12_24.tiff",
                            "temp_100_150_20081031T000000_20081103T000000_25_80.tiff"));

            // add an elevation filter, single value that matches inside a range
            ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
            elevation.setValue(Arrays.asList(34)); // single value in the 20-99 range
            ctx = new ReaderAndParams(reader, new GeneralParameterValue[] {time, elevation});
            fc = TX.evaluate(ctx);
            assertEquals(2, fc.size());
            locations = collectLocations(fc);
            assertThat(
                    locations,
                    hasItems(
                            "temp_020_099_20081031T000000_20081103T000000_12_24.tiff",
                            "temp_020_099_20081031T000000_20081103T000000_25_80.tiff"));

            // add a custom dimension filter
            ParameterDescriptor<List> wavelengthDescriptor =
                    reader.getDynamicParameters().stream()
                            .filter(p -> p.getName().getCode().equalsIgnoreCase("wavelength"))
                            .findFirst()
                            .get();
            ParameterValue<List> wavelength = wavelengthDescriptor.createValue();
            // ... range between 12 and 14
            wavelength.setValue(asList(new NumberRange<>(Double.class, 14d, 20d)));
            ctx =
                    new ReaderAndParams(
                            reader, new GeneralParameterValue[] {time, elevation, wavelength});
            fc = TX.evaluate(ctx);
            assertEquals(1, fc.size());
            locations = collectLocations(fc);
            assertThat(
                    locations, hasItems("temp_020_099_20081031T000000_20081103T000000_12_24.tiff"));
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testNoReadParameters() throws Exception {
        URL rgbURL = TestData.url(this, "rgb");
        final AbstractGridFormat format = TestUtils.getFormat(rgbURL);
        final ImageMosaicReader reader = getReader(rgbURL, format);
        try {
            /* Check it can work without read parameters */
            ReaderAndParams ctx = new ReaderAndParams(reader, null);
            SimpleFeatureCollection fc = TX.evaluate(ctx);

            // all files have been loaded
            assertEquals(24, fc.size());
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testNullReadParameterValues() throws Exception {
        URL rgbURL = TestData.url(this, "rgb");
        final AbstractGridFormat format = TestUtils.getFormat(rgbURL);
        final ImageMosaicReader reader = getReader(rgbURL, format);
        try {
            /* setup params with null values (happens from the GeoServer GUI for example) */
            ParameterValue<String> sort = ImageMosaicFormat.SORT_BY.createValue();
            sort.setValue(null);

            ParameterValue<Integer> maxTiles = ImageMosaicFormat.MAX_ALLOWED_TILES.createValue();
            maxTiles.setValue(null);

            /* These are not statically set, adding them for extra measure */
            ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
            time.setValue(null);

            ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
            elevation.setValue(null);

            ParameterValue<Filter> filter = ImageMosaicFormat.FILTER.createValue();
            filter.setValue(null);

            ParameterValue<GridGeometry2D> gg =
                    AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
            gg.setValue(null);

            /* Check it can work without read parameters */
            ReaderAndParams ctx =
                    new ReaderAndParams(
                            reader,
                            new GeneralParameterValue[] {
                                sort, maxTiles, time, elevation, filter, gg
                            });
            SimpleFeatureCollection fc = TX.evaluate(ctx);

            // all files have been loaded
            assertEquals(24, fc.size());
        } finally {
            reader.dispose();
        }
    }

    private Set<String> collectLocations(SimpleFeatureCollection fc) {
        Set<String> locations =
                DataUtilities.list(fc).stream()
                        .map(f -> (String) f.getAttribute("location"))
                        .collect(Collectors.toSet());
        return locations;
    }

    private Date parseISO(String s) {
        return Date.from(Instant.parse(s));
    }

    private URL setupWaterTemp(String targetDirectory) throws IOException {
        final File workDir = new File(TestData.file(this, "."), targetDirectory);
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        FileUtils.copyFile(
                TestData.file(this, "watertemp.zip"), new File(workDir, "watertemp.zip"));
        TestData.unzipFile(this, targetDirectory + "/watertemp.zip");

        final URL timeElevURL = TestData.url(this, targetDirectory);
        return timeElevURL;
    }

    private Class<?> getBinding(SimpleFeatureType schema, String the_geom) {
        return schema.getDescriptor(the_geom).getType().getBinding();
    }
}
