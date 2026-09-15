/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.gce.imagemosaic.TestUtils.getReader;
import static org.geotools.util.URLs.fileToUrl;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import it.geosolutions.imageio.pam.PAMDataset;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand.FieldDefn;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand.FieldType;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand.FieldUsage;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand.GDALRasterAttributeTable;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand.Row;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand.TableType;
import it.geosolutions.imageio.pam.PAMParser;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.eclipse.imagen.media.range.NoDataContainer;
import org.geotools.api.data.ResourceInfo;
import org.geotools.api.referencing.FactoryException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.PAMResourceInfo;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.image.ImageWorker;
import org.geotools.test.TestData;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class ImageMosaicRATTest {

    /** Index of the footprint field in the GDAL 3.12 sample table. */
    private static final int FOOTPRINT = 7;

    /** The survey whose footprint the sample clips to each of the three granules. */
    private static final String CLIPPED_SURVEY = "54602";

    /** The three clipped footprints of {@link #CLIPPED_SURVEY} joined back together. */
    private static final String MERGED_FOOTPRINT = "POLYGON ((0 0,3 0,3 1,0 1,0 0))";

    @Test
    public void testRATFromGeotiff() throws IOException, FactoryException {
        // setup data
        File mosaicSource = TestData.file(this, "bluetopo");
        String mosaicName = "bluetopo";
        File mosaicDirectory = new File("target", mosaicName);
        FileUtils.deleteQuietly(mosaicDirectory);
        FileUtils.copyDirectory(mosaicSource, mosaicDirectory);

        // load the source PAM files
        PAMParser parser = new PAMParser();
        PAMDataset pam1 = parser.parsePAM(new File(mosaicDirectory, "BlueTopo_BH4JS577_20230918.tiff.aux.xml"));
        PAMDataset pam2 = parser.parsePAM(new File(mosaicDirectory, "BlueTopo_BH4JS578_20230918.tiff.aux.xml"));
        PAMDataset.PAMRasterBand band_1_2 = pam1.getPAMRasterBand().get(2);
        PAMDataset.PAMRasterBand band_2_2 = pam2.getPAMRasterBand().get(2);

        // create mosaic and grab the pam databset from resource info
        ImageMosaicReader reader = getReader(fileToUrl(mosaicDirectory));
        ResourceInfo info = reader.getInfo(mosaicName);
        assertThat(info, CoreMatchers.instanceOf(PAMResourceInfo.class));
        PAMResourceInfo pamInfo = (PAMResourceInfo) info;
        PAMDataset pam = pamInfo.getPAMDataset();

        // check bands
        assertNotNull(pam);
        List<PAMDataset.PAMRasterBand> bands = pam.getPAMRasterBand();
        assertEquals(3, bands.size());
        assertNull(bands.get(0).getGdalRasterAttributeTable());
        assertNull(bands.get(1).getGdalRasterAttributeTable());
        PAMDataset.PAMRasterBand b2 = bands.get(2);
        assertNotNull(b2);

        // look at band 02 RAT, check the fields first
        GDALRasterAttributeTable rat = b2.getGdalRasterAttributeTable();
        List<FieldDefn> fields = rat.getFieldDefn();
        List<FieldDefn> originalFields = band_1_2.getGdalRasterAttributeTable().getFieldDefn();
        assertEquals(originalFields, fields);

        // now check the rows are a union of the two, there are some overlaps
        // so the union has less overall rows
        List<Row> rows = rat.getRow();
        List<Row> rows1 = band_1_2.getGdalRasterAttributeTable().getRow();
        List<Row> rows2 = band_2_2.getGdalRasterAttributeTable().getRow();
        assertTrue(rows.size() < rows1.size() + rows2.size());

        // pick a row that's present in both files, index=0
        List<String> row0 = rows.get(0).getF();
        List<String> row1_0 = rows1.get(0).getF();
        List<String> row2_0 = rows2.get(0).getF();
        assertEquals(row0.size(), row1_0.size());
        for (int i = 0; i < row0.size(); i++) {
            if (i == 1) continue; // count field, it's going to be the sum
            assertEquals(row0.get(i), row1_0.get(i));
            assertEquals(row0.get(i), row2_0.get(i));
        }
        // handle the count, must have been accumulated
        assertEquals(String.valueOf(1715857 + 2177566), row0.get(1));

        // now grab a row that's only in the first table
        String rowValueOnlyFirst = "1014908";
        List<String> rowOnlyFirst = lookupRow(rows, rowValueOnlyFirst);
        List<String> rowOnlyFirstOriginal = lookupRow(rows1, rowValueOnlyFirst);
        assertThrows(IndexOutOfBoundsException.class, () -> lookupRow(rows2, rowValueOnlyFirst));
        assertEquals(rowOnlyFirstOriginal, rowOnlyFirst);

        // and one that's only in the second table
        String rowValueOnlySecond = "35367";
        List<String> rowOnlySecond = lookupRow(rows, rowValueOnlySecond);
        assertThrows(IndexOutOfBoundsException.class, () -> lookupRow(rows1, rowValueOnlySecond));
        List<String> rowOnlySecondOriginal = lookupRow(rows2, rowValueOnlySecond);
        assertEquals(rowOnlySecondOriginal, rowOnlySecond);

        // check nodata
        GridCoverage2D coverage = reader.read();
        NoDataContainer cvNoData = CoverageUtilities.getNoDataProperty(coverage);
        assertEquals(Double.NaN, cvNoData.getAsSingleValue(), 0d);
        RenderedImage ri = coverage.getRenderedImage();
        assertEquals(Double.NaN, new ImageWorker(ri).getNoData().getMin().doubleValue(), 0d);

        coverage.dispose(true);
        reader.dispose();
    }

    @Test
    public void testReloadRAT() throws IOException, FactoryException {
        // setup data
        File mosaicSource = TestData.file(this, "bluetopo");
        String mosaicName = "bluetopoReload";
        File mosaicDirectory = new File("target", mosaicName);
        FileUtils.deleteQuietly(mosaicDirectory);
        FileUtils.copyDirectory(mosaicSource, mosaicDirectory);

        // the aux files
        String pamFileName = "BlueTopo_BH4JS577_20230918.tiff.aux.xml";
        File small = new File(mosaicDirectory, "BlueTopo_BH4JS577_20230918.tiff.small.aux.xml");
        File full = new File(mosaicDirectory, pamFileName);

        // rename p1 so that it won't be found
        File hideMe = new File(full.getParent(), "hideme.xml");
        assertTrue(full.renameTo(hideMe));
        assertTrue(small.renameTo(full));

        // create mosaic and grab the pam databset from resource info
        ImageMosaicReader reader = getReader(fileToUrl(mosaicDirectory));
        ResourceInfo info = reader.getInfo(mosaicName);
        assertThat(info, CoreMatchers.instanceOf(PAMResourceInfo.class));
        PAMResourceInfo pamInfo = (PAMResourceInfo) info;
        PAMDataset pam = pamInfo.getPAMDataset();

        // grab the raster attribute table
        assertNotNull(pam);
        List<PAMDataset.PAMRasterBand> bands = pam.getPAMRasterBand();
        PAMDataset.PAMRasterBand b2 = bands.get(2);
        GDALRasterAttributeTable rat = b2.getGdalRasterAttributeTable();

        List<Row> originalRows = rat.getRow();

        // now add back the PAM file and force reload
        assertTrue(full.delete());
        assertTrue(hideMe.renameTo(full));
        assertTrue(pamInfo.reloadPAMDataset());

        // grab the PAM dataset again
        rat = pamInfo.getPAMDataset().getPAMRasterBand().get(2).getGdalRasterAttributeTable();
        List<Row> reloadedRows = rat.getRow();

        // check all the rows in the original are already available in the reloaded, but reloaded
        // has more
        assertThat(reloadedRows.size(), Matchers.greaterThan(originalRows.size()));
        for (Row row : originalRows) {
            String originalValue = row.getF().get(0);
            assertTrue(
                    "Could not find " + originalValue,
                    reloadedRows.stream()
                            .filter(r -> r.getF().get(0).equals(originalValue))
                            .findFirst()
                            .isPresent());
        }

        reader.dispose();
    }

    @Test
    public void testGdal312TypesMosaic() throws Exception {
        File mosaicSource = TestData.file(this, "s102rat");
        String mosaicName = "s102rat";
        File mosaicDirectory = new File("target", mosaicName);
        FileUtils.deleteQuietly(mosaicDirectory);
        FileUtils.copyDirectory(mosaicSource, mosaicDirectory);

        ImageMosaicReader reader = getReader(fileToUrl(mosaicDirectory));
        try {
            ResourceInfo info = reader.getInfo(mosaicName);
            assertThat(info, CoreMatchers.instanceOf(PAMResourceInfo.class));
            GDALRasterAttributeTable rat = firstBandRAT(((PAMResourceInfo) info).getPAMDataset());

            assertEquals(TableType.Thematic, rat.getTableType());
            assertGdal312Fields(rat);

            assertMergedRows(rat, mosaicDirectory);

            // the mosaic wide sidecar is written by marshalling, which is where a field type
            // with no XML mapping would be dropped
            GDALRasterAttributeTable written =
                    firstBandRAT(new PAMParser().parsePAM(new File(mosaicDirectory, mosaicName + ".aux.xml")));
            assertEquals(TableType.Thematic, written.getTableType());
            assertGdal312Fields(written);
            assertMergedRows(written, mosaicDirectory);
        } finally {
            reader.dispose();
        }
    }

    /** Without AttributeTableGeometries the mosaic still builds, with the disagreeing footprint left empty. */
    @Test
    public void testGdal312TypesMosaicGeometriesOff() throws Exception {
        String mosaicName = "s102ratOff";
        File mosaicDirectory = new File("target", mosaicName);
        FileUtils.deleteQuietly(mosaicDirectory);
        FileUtils.copyDirectory(TestData.file(this, "s102rat"), mosaicDirectory);
        Files.writeString(new File(mosaicDirectory, "indexer.properties").toPath(), "CollectAttributeTables=true\n");

        ImageMosaicReader reader = getReader(fileToUrl(mosaicDirectory));
        try {
            GDALRasterAttributeTable rat = firstBandRAT(((PAMResourceInfo) reader.getInfo(mosaicName)).getPAMDataset());
            assertEquals(5, rat.getRow().size());
            assertEquals("", lookupRow(rat.getRow(), CLIPPED_SURVEY).get(FOOTPRINT));
            // the survey every granule carries the same way keeps its footprint
            assertEquals(
                    "POLYGON ((1 0,2 0,2 1,1 1,1 0))",
                    lookupRow(rat.getRow(), "54603").get(FOOTPRINT));
        } finally {
            reader.dispose();
        }
    }

    /** The reload rebuilds the table with the geometry handling the mosaic was built with, not with the default. */
    @Test
    public void testReloadKeepsMergedFootprint() throws Exception {
        String mosaicName = "s102ratReload";
        File mosaicDirectory = new File("target", mosaicName);
        FileUtils.deleteQuietly(mosaicDirectory);
        FileUtils.copyDirectory(TestData.file(this, "s102rat"), mosaicDirectory);

        // build the mosaic, then take the indexer away: the mosaic properties have to carry the flag on their own
        ImageMosaicReader reader = getReader(fileToUrl(mosaicDirectory));
        reader.dispose();
        assertTrue(new File(mosaicDirectory, "indexer.properties").delete());

        reader = getReader(fileToUrl(mosaicDirectory));
        try {
            PAMResourceInfo info = (PAMResourceInfo) reader.getInfo(mosaicName);
            assertGeometryEquals(
                    "built footprint",
                    MERGED_FOOTPRINT,
                    lookupRow(firstBandRAT(info.getPAMDataset()).getRow(), CLIPPED_SURVEY)
                            .get(FOOTPRINT));

            assertTrue(info.reloadPAMDataset());

            assertGeometryEquals(
                    "reloaded footprint",
                    MERGED_FOOTPRINT,
                    lookupRow(firstBandRAT(info.getPAMDataset()).getRow(), CLIPPED_SURVEY)
                            .get(FOOTPRINT));
        } finally {
            reader.dispose();
        }
    }

    /**
     * Asserts the merged table holds every granule row exactly as its granule sidecar carries it, the shared survey ids
     * appearing once and the clipped footprint joined back together.
     */
    private void assertMergedRows(GDALRasterAttributeTable rat, File mosaicDirectory) throws Exception {
        PAMParser parser = new PAMParser();
        Map<String, List<String>> expected = new LinkedHashMap<>();
        for (String granule : List.of("qbc_tile1.tif", "qbc_tile2.tif", "qbc_tile3.tif")) {
            firstBandRAT(parser.parsePAM(new File(mosaicDirectory, granule + ".aux.xml")))
                    .getRow()
                    .forEach(row -> expected.putIfAbsent(row.getF().get(0), row.getF()));
        }
        // two survey ids are shared by all three granules, one is unique to each
        assertEquals(5, expected.size());
        assertEquals(List.copyOf(expected.keySet()), surveyIds(rat));
        // and a merged row matches the granule row it came from on every field type
        for (Row row : rat.getRow()) {
            String id = row.getF().get(0);
            List<String> expectedRow = expected.get(id);
            assertEquals(
                    "row " + id, expectedRow.subList(0, FOOTPRINT), row.getF().subList(0, FOOTPRINT));
            String expectedFootprint = CLIPPED_SURVEY.equals(id) ? MERGED_FOOTPRINT : expectedRow.get(FOOTPRINT);
            assertGeometryEquals("row " + id, expectedFootprint, row.getF().get(FOOTPRINT));
        }
    }

    private void assertGeometryEquals(String message, String expected, String actual) throws ParseException {
        if (expected.equals(actual)) return;
        WKTReader reader = new WKTReader();
        assertTrue(
                message + ", expected " + expected + " but got " + actual,
                reader.read(expected).equalsTopo(reader.read(actual)));
    }

    /** A footprint the merge cannot read leaves the cell empty instead of failing the whole mosaic. */
    @Test
    public void testUnmergeableFootprint() {
        assertEquals(List.of(CLIPPED_SURVEY, ""), mergeFootprints("POLYGON ((0 0,1 0,1 1,0 1,0 0))", "not a geometry"));
    }

    /** A footprint that parses but is not a valid ring leaves the cell empty, instead of failing the mosaic. */
    @Test
    public void testInvalidRingFootprint() {
        assertEquals(
                List.of(CLIPPED_SURVEY, ""),
                mergeFootprints("POLYGON ((0 0,1 0,1 1,0 1,0 0))", "POLYGON ((0 0,1 0,1 1))"));
    }

    /** The merged footprint is written back once, after the last granule, not on every merge. */
    @Test
    public void testMergedFootprint() throws ParseException {
        List<String> merged = mergeFootprints(
                "POLYGON ((0 0,1 0,1 1,0 1,0 0))",
                "POLYGON ((1 0,2 0,2 1,1 1,1 0))",
                "POLYGON ((2 0,3 0,3 1,2 1,2 0))");
        assertEquals(CLIPPED_SURVEY, merged.get(0));
        assertGeometryEquals("merged footprint", MERGED_FOOTPRINT, merged.get(1));
    }

    /** Each geometry column of a table is merged on its own. */
    @Test
    public void testSeveralGeometryColumns() throws ParseException {
        RATCollector collector = new RATCollector(
                0, footprintTable("POLYGON ((0 0,1 0,1 1,0 1,0 0))", "POINT (0 0)"), RATGeometries.MERGE);
        collector.collect(footprintTable("POLYGON ((1 0,2 0,2 1,1 1,1 0))", "POINT (5 5)"));

        List<String> merged = mergedRow(collector, 2);
        assertGeometryEquals("footprint1", "POLYGON ((0 0,2 0,2 1,0 1,0 0))", merged.get(1));
        assertGeometryEquals("footprint2", "MULTIPOINT ((0 0),(5 5))", merged.get(2));
    }

    /** Off, the default, empties a footprint the granules disagree on, without parsing any geometry. */
    @Test
    public void testGeometriesOff() {
        RATCollector collector = new RATCollector(0, footprintTable("POLYGON ((0 0,1 0,1 1,0 1,0 0))"));
        collector.collect(footprintTable("POLYGON ((1 0,2 0,2 1,1 1,1 0))"));

        assertEquals(List.of(CLIPPED_SURVEY, ""), mergedRow(collector, 1));
    }

    /** Collecting treats a footprint like any other column, so granules that disagree stop the collection. */
    @Test
    public void testGeometriesCollect() {
        RATCollector collector =
                new RATCollector(0, footprintTable("POLYGON ((0 0,1 0,1 1,0 1,0 0))"), RATGeometries.COLLECT);

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> collector.collect(footprintTable("POLYGON ((1 0,2 0,2 1,1 1,1 0))")));
        assertThat(e.getMessage(), CoreMatchers.containsString("Different values for band 0 in row " + CLIPPED_SURVEY));
    }

    /** Collects one row per given footprint, all under the same survey id, and returns the merged row. */
    private List<String> mergeFootprints(String first, String... others) {
        RATCollector collector = new RATCollector(0, footprintTable(first), RATGeometries.MERGE);
        for (String other : others) {
            collector.collect(footprintTable(other));
        }
        return mergedRow(collector, 1);
    }

    /** Writes the collected rows into a fresh table with the given number of footprint columns, and returns the row. */
    private List<String> mergedRow(RATCollector collector, int footprints) {
        String[] empty = new String[footprints];
        Arrays.fill(empty, "");
        PAMDataset dataset = new PAMDataset();
        PAMDataset.PAMRasterBand band = new PAMDataset.PAMRasterBand();
        band.setGdalRasterAttributeTable(footprintTable(empty));
        dataset.getPAMRasterBand().add(band);
        collector.replaceRows(dataset, 0);

        List<Row> rows = band.getGdalRasterAttributeTable().getRow();
        assertEquals(1, rows.size());
        return rows.get(0).getF();
    }

    /** A one row table holding the survey id and one column per given footprint. */
    private GDALRasterAttributeTable footprintTable(String... footprints) {
        GDALRasterAttributeTable rat = new GDALRasterAttributeTable();
        rat.getFieldDefn().add(fieldDefn("id", FieldType.Integer, FieldUsage.MinMax));
        for (int i = 0; i < footprints.length; i++) {
            rat.getFieldDefn().add(fieldDefn("footprint" + (i + 1), FieldType.WKBGeometry, FieldUsage.Generic));
        }
        Row row = new Row();
        row.getF().add(CLIPPED_SURVEY);
        for (String footprint : footprints) {
            row.getF().add(footprint);
        }
        rat.getRow().add(row);
        return rat;
    }

    private FieldDefn fieldDefn(String name, FieldType type, FieldUsage usage) {
        FieldDefn field = new FieldDefn();
        field.setName(name);
        field.setType(type);
        field.setUsage(usage);
        return field;
    }

    private GDALRasterAttributeTable firstBandRAT(PAMDataset pam) {
        assertNotNull(pam);
        List<PAMDataset.PAMRasterBand> bands = pam.getPAMRasterBand();
        assertEquals(1, bands.size());
        GDALRasterAttributeTable rat = bands.get(0).getGdalRasterAttributeTable();
        assertNotNull(rat);
        return rat;
    }

    /** Asserts the eight field definitions of the GDAL 3.12 sample, types included. */
    private void assertGdal312Fields(GDALRasterAttributeTable rat) {
        List<FieldDefn> fields = rat.getFieldDefn();
        assertEquals(8, fields.size());
        assertField(fields.get(0), "id", FieldType.Integer, FieldUsage.MinMax);
        assertField(fields.get(1), "dataAssessment", FieldType.Integer, FieldUsage.Generic);
        assertField(fields.get(2), "fullSeafloorCoverageAchieved", FieldType.Boolean, FieldUsage.Generic);
        assertField(fields.get(3), "surveyDateRange.dateStart", FieldType.DateTime, FieldUsage.Generic);
        assertField(fields.get(4), "surveyDateRange.dateEnd", FieldType.DateTime, FieldUsage.Generic);
        assertField(fields.get(5), "surveyAuthority", FieldType.String, FieldUsage.Generic);
        assertField(fields.get(6), "depthRange.minimumDepth", FieldType.Real, FieldUsage.Generic);
        assertField(fields.get(7), "footprint", FieldType.WKBGeometry, FieldUsage.Generic);
    }

    private void assertField(FieldDefn field, String name, FieldType type, FieldUsage usage) {
        assertEquals(name, field.getName());
        assertEquals(type, field.getType());
        assertEquals(usage, field.getUsage());
    }

    private List<String> surveyIds(GDALRasterAttributeTable rat) {
        return rat.getRow().stream().map(row -> row.getF().get(0)).toList();
    }

    private List<String> lookupRow(List<Row> rows, String reference) {
        return rows.stream()
                .filter(row -> row.getF().get(0).equals(reference))
                .map(row -> row.getF())
                .findFirst()
                .orElseThrow(() -> new IndexOutOfBoundsException("Row not found"));
    }
}
