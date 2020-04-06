/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf;

import it.geosolutions.jaiext.range.NoDataContainer;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import javax.media.jai.PlanarImage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.DimensionDescriptor;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.StructuredGridCoverage2DReader;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.coverage.util.FeatureUtilities;
import org.geotools.data.CloseableIterator;
import org.geotools.data.FileGroupProvider.FileGroup;
import org.geotools.data.FileResourceInfo;
import org.geotools.data.ResourceInfo;
import org.geotools.gce.imagemosaic.ImageMosaicFormat;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.metadata.iso.extent.GeographicBoundingBoxImpl;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.geotools.util.DateRange;
import org.geotools.util.NumberRange;
import org.geotools.util.factory.Hints;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.coverage.processing.Operation;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.metadata.extent.Extent;
import org.opengis.metadata.extent.GeographicExtent;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.InternationalString;
import si.uom.SI;
import ucar.nc2.dataset.NetcdfDataset;

public class NetCDFReaderTest extends Assert {

    @Before
    public void setup() {
        System.setProperty("netcdf.coordinates.enablePlugins", "true");
    }

    private static final double DELTA = 1E-6;

    /**
     * Test using this netcdf image: data: LAI= 20,20,20,30,30, 40,40,40,50,50, 60,60,60,70,70,
     * 80,80,80,90,90; lon= 10,15,20,25,30; lat= 70,60,50,40;
     */
    @Test
    public void testHDF5Image() throws IOException, FactoryException {
        final File testURL = TestData.file(this, "2DLatLonCoverage.nc");
        // Get format
        // final AbstractGridFormat format = (AbstractGridFormat)
        GridFormatFinder.findFormat(testURL.toURI().toURL(), null);
        final NetCDFReader reader = new NetCDFReader(testURL, null);
        // assertNotNull(format);
        assertNotNull(reader);
        try {
            String[] names = reader.getGridCoverageNames();
            assertNotNull(names);
            assertEquals(2, names.length);
            assertEquals("ROOT/LEVEL1/V2", names[1]);
            GridCoverage2D grid = reader.read("ROOT/LAI", null);
            assertNotNull(grid);

            // Checking the SampleDimension Description Fallback improvement
            GridSampleDimension sampleDimension = grid.getSampleDimension(0);
            InternationalString description = sampleDimension.getDescription();
            assertEquals("LAI", description.toString());

            byte[] byteValue =
                    grid.evaluate(
                            new DirectPosition2D(DefaultGeographicCRS.WGS84, 12, 70), new byte[1]);
            assertEquals(20, byteValue[0]);

            byteValue =
                    grid.evaluate(
                            new DirectPosition2D(DefaultGeographicCRS.WGS84, 23, 40), new byte[1]);
            assertEquals(90, byteValue[0]);

        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    @Test
    public void testFullReadOnCoverageWithIncreasingLat() throws IOException, FactoryException {
        final File testURL = TestData.file(this, "O3-NO2.nc");
        // Get format
        // final AbstractGridFormat format = (AbstractGridFormat)
        GridFormatFinder.findFormat(testURL.toURI().toURL(), null);
        final NetCDFReader reader = new NetCDFReader(testURL, null);
        // assertNotNull(format);
        assertNotNull(reader);
        try {
            String[] names = reader.getGridCoverageNames();
            assertNotNull(names);
            assertEquals(2, names.length);

            GridCoverage2D grid = reader.read("O3", null);
            assertFalse(grid.getSampleDimension(0).getDescription().toString().endsWith(":sd"));
            assertNotNull(grid);
            float[] value =
                    grid.evaluate(
                            (DirectPosition)
                                    new DirectPosition2D(DefaultGeographicCRS.WGS84, 5, 45),
                            new float[1]);
            assertEquals(47.63341f, value[0], 0.00001);

            value =
                    grid.evaluate(
                            (DirectPosition)
                                    new DirectPosition2D(DefaultGeographicCRS.WGS84, 5, 45.125),
                            new float[1]);
            assertEquals(52.7991f, value[0], 0.000001);

        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    @Test
    public void testScaleAndOffset() throws IOException, FactoryException, ParseException {
        // Capture the current enhance mode, so we can change it now and set it back later
        Set<NetcdfDataset.Enhance> currentEnhanceMode = NetcdfDataset.getDefaultEnhanceMode();
        Set<NetcdfDataset.Enhance> newEnhanceMode =
                EnumSet.of(NetcdfDataset.Enhance.CoordSystems, NetcdfDataset.Enhance.ScaleMissing);
        Boolean currentEnhanceSysProp = Boolean.getBoolean(NetCDFUtilities.ENHANCE_SCALE_MISSING);
        if (!currentEnhanceSysProp) {
            System.setProperty(NetCDFUtilities.ENHANCE_SCALE_MISSING, "true");
        }
        NetcdfDataset.setDefaultEnhanceMode(newEnhanceMode);
        File file = TestData.file(this, "o3_no2_so.nc");
        NetCDFReader reader = new NetCDFReader(file, null);
        String coverageName = "NO2";
        GeneralParameterValue[] values = new GeneralParameterValue[] {};
        GridCoverage2D coverage = reader.read(coverageName, values);

        float[] result =
                coverage.evaluate(
                        (DirectPosition)
                                new DirectPosition2D(DefaultGeographicCRS.WGS84, 5.0, 45.0),
                        new float[1]);

        assertEquals(1.615991, result[0], 1e-6f);
        reader.dispose();

        // let's open a file containing a time variable with _FillValue attribute
        file = TestData.file(this, "o3_no2_so_fv_time.nc");
        reader = new NetCDFReader(file, null);
        assertEquals(
                "2012-04-01T00:00:00.000Z",
                reader.getMetadataValue(coverageName, "TIME_DOMAIN_MINIMUM"));
        NetcdfDataset.setDefaultEnhanceMode(currentEnhanceMode);
        System.setProperty(NetCDFUtilities.ENHANCE_SCALE_MISSING, currentEnhanceMode.toString());
        reader.dispose();
    }

    @Test
    public void testRanges() throws IOException {
        File file = TestData.file(this, "20130101.METOPA.GOME2.NO2.DUMMY_3.nc");
        final NetCDFReader reader = new NetCDFReader(file, null);
        String coverageName = "z";
        GeneralParameterValue[] values = new GeneralParameterValue[] {};
        GridCoverage2D coverage = reader.read(coverageName, values);
        GridSampleDimension sampleDimension = coverage.getSampleDimension(0);
        double min = sampleDimension.getMinimumValue();
        double max = sampleDimension.getMaximumValue();
        double nodata = sampleDimension.getNoDataValues()[0];
        assertEquals(0.133045, min, 1e-3f);
        assertEquals(35.827045, max, 1e-3f);
        assertEquals(-999, nodata, 1e-3f);
        reader.dispose();
    }

    @Test
    public void NetCDFTestOn4Dcoverages()
            throws NoSuchAuthorityCodeException, FactoryException, IOException, ParseException {
        File mosaic = new File(TestData.file(this, "."), "NetCDFTestOn4Dcoverages");
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());

        File file = TestData.file(this, "O3-NO2.nc");
        FileUtils.copyFileToDirectory(file, mosaic);
        file = new File(mosaic, "O3-NO2.nc");
        final Hints hints =
                new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));
        // Get format
        final AbstractGridFormat format =
                (AbstractGridFormat) GridFormatFinder.findFormat(file.toURI().toURL(), hints);
        final NetCDFReader reader = (NetCDFReader) format.getReader(file.toURI().toURL(), hints);

        assertNotNull(format);
        try {
            String[] names = reader.getGridCoverageNames();
            names = new String[] {names[1]};

            for (String coverageName : names) {

                final String[] metadataNames = reader.getMetadataNames(coverageName);
                assertNotNull(metadataNames);
                assertEquals(metadataNames.length, 12);

                // Parsing metadata values
                assertEquals("true", reader.getMetadataValue(coverageName, "HAS_TIME_DOMAIN"));
                final String timeMetadata = reader.getMetadataValue(coverageName, "TIME_DOMAIN");
                assertEquals(
                        "2012-04-01T00:00:00.000Z/2012-04-01T00:00:00.000Z,2012-04-01T01:00:00.000Z/2012-04-01T01:00:00.000Z",
                        timeMetadata);
                assertNotNull(timeMetadata);
                assertEquals(
                        "2012-04-01T00:00:00.000Z",
                        reader.getMetadataValue(coverageName, "TIME_DOMAIN_MINIMUM"));
                assertEquals(
                        "2012-04-01T01:00:00.000Z",
                        reader.getMetadataValue(coverageName, "TIME_DOMAIN_MAXIMUM"));

                assertEquals("true", reader.getMetadataValue(coverageName, "HAS_ELEVATION_DOMAIN"));
                final String elevationMetadata =
                        reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN");
                assertNotNull(elevationMetadata);
                assertEquals("10.0/10.0,450.0/450.0", elevationMetadata);
                assertEquals(2, elevationMetadata.split(",").length);
                assertEquals(
                        "10.0", reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN_MINIMUM"));
                assertEquals(
                        "450.0", reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN_MAXIMUM"));

                List<DimensionDescriptor> descriptors =
                        ((StructuredGridCoverage2DReader) reader)
                                .getDimensionDescriptors(coverageName);
                assertNotNull(descriptors);
                assertEquals(2, descriptors.size());

                DimensionDescriptor descriptor = descriptors.get(0);
                assertEquals("TIME", descriptor.getName());
                assertEquals("time", descriptor.getStartAttribute());
                assertNull(descriptor.getEndAttribute());
                assertEquals(CoverageUtilities.UCUM.TIME_UNITS.getName(), descriptor.getUnits());
                assertEquals(
                        CoverageUtilities.UCUM.TIME_UNITS.getSymbol(), descriptor.getUnitSymbol());

                descriptor = descriptors.get(1);
                assertEquals("ELEVATION", descriptor.getName());
                assertEquals("z", descriptor.getStartAttribute());
                assertNull(descriptor.getEndAttribute());
                assertEquals("meters", descriptor.getUnits());
                assertEquals(
                        CoverageUtilities.UCUM.ELEVATION_UNITS.getSymbol(),
                        descriptor.getUnitSymbol());

                // subsetting the envelope
                final ParameterValue<GridGeometry2D> gg =
                        AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
                final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope(coverageName);
                final GeneralEnvelope reducedEnvelope =
                        new GeneralEnvelope(
                                new double[] {
                                    originalEnvelope.getLowerCorner().getOrdinate(0),
                                    originalEnvelope.getLowerCorner().getOrdinate(1)
                                },
                                new double[] {
                                    originalEnvelope.getMedian().getOrdinate(0),
                                    originalEnvelope.getMedian().getOrdinate(1)
                                });
                reducedEnvelope.setCoordinateReferenceSystem(
                        reader.getCoordinateReferenceSystem(coverageName));

                // Selecting bigger gridRange for a zoomed result
                final Dimension dim = new Dimension();
                GridEnvelope gridRange = reader.getOriginalGridRange(coverageName);
                dim.setSize(gridRange.getSpan(0) * 4.0, gridRange.getSpan(1) * 2.0);
                final Rectangle rasterArea = ((GridEnvelope2D) gridRange);
                rasterArea.setSize(dim);
                final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
                gg.setValue(new GridGeometry2D(range, reducedEnvelope));

                final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
                final SimpleDateFormat formatD =
                        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                formatD.setTimeZone(TimeZone.getTimeZone("GMT"));
                final Date timeD = formatD.parse("2012-04-01T00:00:00.000Z");
                time.setValue(
                        new ArrayList() {
                            {
                                add(timeD);
                            }
                        });

                final ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
                elevation.setValue(
                        new ArrayList() {
                            {
                                add(450d); // Elevation
                            }
                        });

                GeneralParameterValue[] values = new GeneralParameterValue[] {gg, time, elevation};
                GridCoverage2D coverage = reader.read(coverageName, values);
                assertNotNull(coverage);
                if (TestData.isInteractiveTest()) {
                    coverage.show();
                } else {
                    PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getTiles();
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    @Test
    public void NetCDFTestOnFilter()
            throws NoSuchAuthorityCodeException, FactoryException, IOException, ParseException {
        File mosaic = new File(TestData.file(this, "."), "NetCDFTestOnFilter");
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());

        File file = TestData.file(this, "O3-NO2.nc");
        FileUtils.copyFileToDirectory(file, mosaic);
        file = new File(mosaic, "O3-NO2.nc");

        final Hints hints =
                new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));
        // Get format
        final AbstractGridFormat format =
                (AbstractGridFormat) GridFormatFinder.findFormat(file.toURI().toURL(), hints);
        final NetCDFReader reader = (NetCDFReader) format.getReader(file.toURI().toURL(), hints);

        assertNotNull(format);
        try {
            String[] names = reader.getGridCoverageNames();
            names = new String[] {names[1]};

            for (String coverageName : names) {

                final String[] metadataNames = reader.getMetadataNames(coverageName);
                assertNotNull(metadataNames);
                assertEquals(12, metadataNames.length);

                // Parsing metadata values
                assertEquals("true", reader.getMetadataValue(coverageName, "HAS_TIME_DOMAIN"));
                final String timeMetadata = reader.getMetadataValue(coverageName, "TIME_DOMAIN");
                assertEquals(
                        "2012-04-01T00:00:00.000Z/2012-04-01T00:00:00.000Z,2012-04-01T01:00:00.000Z/2012-04-01T01:00:00.000Z",
                        timeMetadata);
                assertNotNull(timeMetadata);
                assertEquals(
                        "2012-04-01T00:00:00.000Z",
                        reader.getMetadataValue(coverageName, "TIME_DOMAIN_MINIMUM"));
                assertEquals(
                        "2012-04-01T01:00:00.000Z",
                        reader.getMetadataValue(coverageName, "TIME_DOMAIN_MAXIMUM"));

                assertEquals("true", reader.getMetadataValue(coverageName, "HAS_ELEVATION_DOMAIN"));
                final String elevationMetadata =
                        reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN");
                assertNotNull(elevationMetadata);
                assertEquals("10.0/10.0,450.0/450.0", elevationMetadata);
                assertEquals(2, elevationMetadata.split(",").length);
                assertEquals(
                        "10.0", reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN_MINIMUM"));
                assertEquals(
                        "450.0", reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN_MAXIMUM"));

                // subsetting the envelope
                final ParameterValue<GridGeometry2D> gg =
                        AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
                final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope(coverageName);
                final GeneralEnvelope reducedEnvelope =
                        new GeneralEnvelope(
                                new double[] {
                                    originalEnvelope.getLowerCorner().getOrdinate(0),
                                    originalEnvelope.getLowerCorner().getOrdinate(1)
                                },
                                new double[] {
                                    originalEnvelope.getMedian().getOrdinate(0),
                                    originalEnvelope.getMedian().getOrdinate(1)
                                });
                reducedEnvelope.setCoordinateReferenceSystem(
                        reader.getCoordinateReferenceSystem(coverageName));

                // Selecting bigger gridRange for a zoomed result
                final Dimension dim = new Dimension();
                GridEnvelope gridRange = reader.getOriginalGridRange(coverageName);
                dim.setSize(gridRange.getSpan(0) * 4.0, gridRange.getSpan(1) * 2.0);
                final Rectangle rasterArea = ((GridEnvelope2D) gridRange);
                rasterArea.setSize(dim);
                final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
                gg.setValue(new GridGeometry2D(range, reducedEnvelope));

                final ParameterValue<Filter> filterParam = NetCDFFormat.FILTER.createValue();
                FilterFactory2 FF = FeatureUtilities.DEFAULT_FILTER_FACTORY;
                Filter filter = FF.equals(FF.property("z"), FF.literal(450.0));
                filterParam.setValue(filter);

                GeneralParameterValue[] values = new GeneralParameterValue[] {filterParam};
                GridCoverage2D coverage = reader.read(coverageName, values);
                assertNotNull(coverage);
                if (TestData.isInteractiveTest()) {
                    coverage.show();
                } else {
                    PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getTiles();
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    @Test
    public void NetCDFTestOn4DcoveragesWithDifferentSchemas()
            throws NoSuchAuthorityCodeException, FactoryException, IOException, ParseException {
        File mosaic =
                new File(TestData.file(this, "."), "NetCDFTestOn4DcoveragesWithDifferentSchemas");
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());

        File file = TestData.file(this, "O3-NO2-noZ.nc");
        FileUtils.copyFileToDirectory(file, mosaic);
        file = new File(mosaic, "O3-NO2-noZ.nc");

        final Hints hints =
                new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));
        // Get format
        final AbstractGridFormat format =
                (AbstractGridFormat) GridFormatFinder.findFormat(file.toURI().toURL(), hints);
        final NetCDFReader reader = (NetCDFReader) format.getReader(file.toURI().toURL(), hints);

        assertNotNull(format);
        try {
            String[] names = reader.getGridCoverageNames();
            for (String coverageName : names) {

                final String[] metadataNames = reader.getMetadataNames(coverageName);
                assertNotNull(metadataNames);
                assertEquals(metadataNames.length, 12);

                // Parsing metadata values
                assertEquals("true", reader.getMetadataValue(coverageName, "HAS_TIME_DOMAIN"));
                final String timeMetadata = reader.getMetadataValue(coverageName, "TIME_DOMAIN");
                assertEquals(
                        "2012-04-01T00:00:00.000Z/2012-04-01T00:00:00.000Z,2012-04-01T01:00:00.000Z/2012-04-01T01:00:00.000Z",
                        timeMetadata);
                assertNotNull(timeMetadata);
                assertEquals(
                        "2012-04-01T00:00:00.000Z",
                        reader.getMetadataValue(coverageName, "TIME_DOMAIN_MINIMUM"));
                assertEquals(
                        "2012-04-01T01:00:00.000Z",
                        reader.getMetadataValue(coverageName, "TIME_DOMAIN_MAXIMUM"));
                assertEquals(
                        "java.util.Date",
                        reader.getMetadataValue(coverageName, "TIME_DOMAIN_DATATYPE"));

                if (coverageName.equalsIgnoreCase("O3")) {
                    assertEquals(
                            "true", reader.getMetadataValue(coverageName, "HAS_ELEVATION_DOMAIN"));
                    final String elevationMetadata =
                            reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN");
                    assertNotNull(elevationMetadata);
                    assertEquals("10.0/10.0,450.0/450.0", elevationMetadata);
                    assertEquals(2, elevationMetadata.split(",").length);
                    assertEquals(
                            "10.0",
                            reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN_MINIMUM"));
                    assertEquals(
                            "450.0",
                            reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN_MAXIMUM"));
                    assertEquals(
                            "java.lang.Double",
                            reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN_DATATYPE"));
                } else {
                    // Note that This sample doesn't have elevation for NO2
                    assertEquals(
                            "false", reader.getMetadataValue(coverageName, "HAS_ELEVATION_DOMAIN"));
                    final String elevationMetadata =
                            reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN");
                    assertNull(elevationMetadata);
                }

                // subsetting the envelope
                final ParameterValue<GridGeometry2D> gg =
                        AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
                final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope(coverageName);
                final GeneralEnvelope reducedEnvelope =
                        new GeneralEnvelope(
                                new double[] {
                                    originalEnvelope.getLowerCorner().getOrdinate(0),
                                    originalEnvelope.getLowerCorner().getOrdinate(1)
                                },
                                new double[] {
                                    originalEnvelope.getMedian().getOrdinate(0),
                                    originalEnvelope.getMedian().getOrdinate(1)
                                });
                reducedEnvelope.setCoordinateReferenceSystem(
                        reader.getCoordinateReferenceSystem(coverageName));

                // Selecting bigger gridRange for a zoomed result
                final Dimension dim = new Dimension();
                GridEnvelope gridRange = reader.getOriginalGridRange(coverageName);
                dim.setSize(gridRange.getSpan(0) * 4.0, gridRange.getSpan(1) * 2.0);
                final Rectangle rasterArea = ((GridEnvelope2D) gridRange);
                rasterArea.setSize(dim);
                final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
                gg.setValue(new GridGeometry2D(range, reducedEnvelope));

                final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
                final SimpleDateFormat formatD =
                        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                formatD.setTimeZone(TimeZone.getTimeZone("GMT"));
                final Date timeD = formatD.parse("2012-04-01T00:00:00.000Z");
                time.setValue(
                        new ArrayList() {
                            {
                                add(timeD);
                            }
                        });

                final ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
                elevation.setValue(
                        new ArrayList() {
                            {
                                add(450d); // Elevation
                            }
                        });

                GeneralParameterValue[] values =
                        coverageName.equalsIgnoreCase("O3")
                                ? new GeneralParameterValue[] {gg, time, elevation}
                                : new GeneralParameterValue[] {gg, time};

                GridCoverage2D coverage = reader.read(coverageName, values);
                assertNotNull(coverage);
                if (TestData.isInteractiveTest()) {
                    coverage.show();
                } else {
                    PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getTiles();
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    @Test
    public void NetCDFTestOn4DcoveragesWithImposedSchemas()
            throws NoSuchAuthorityCodeException, FactoryException, IOException, ParseException {
        File mosaic =
                new File(TestData.file(this, "."), "NetCDFTestOn4DcoveragesWithImposedSchemas");
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());

        File file = TestData.file(this, "O3NO2-noZ.nc");
        File auxFile = TestData.file(this, "O3NO2-noZ.xml");
        FileUtils.copyFileToDirectory(file, mosaic);
        FileUtils.copyFileToDirectory(auxFile, mosaic);
        file = new File(mosaic, "O3NO2-noZ.nc");

        final Hints hints =
                new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));
        hints.put(
                Utils.AUXILIARY_FILES_PATH,
                new File(mosaic, "O3NO2-noZ.xml").getAbsolutePath()); // impose def

        // Get format
        final AbstractGridFormat format =
                (AbstractGridFormat) GridFormatFinder.findFormat(file.toURI().toURL(), hints);
        final NetCDFReader reader = (NetCDFReader) format.getReader(file.toURI().toURL(), hints);

        assertNotNull(format);
        try {
            String[] names = reader.getGridCoverageNames();
            for (String coverageName : names) {

                final String[] metadataNames = reader.getMetadataNames(coverageName);
                assertNotNull(metadataNames);
                assertEquals(metadataNames.length, 12);

                // Parsing metadata values
                assertEquals("true", reader.getMetadataValue(coverageName, "HAS_TIME_DOMAIN"));
                final String timeMetadata = reader.getMetadataValue(coverageName, "TIME_DOMAIN");
                assertEquals(
                        "2012-04-01T00:00:00.000Z/2012-04-01T00:00:00.000Z,2012-04-01T01:00:00.000Z/2012-04-01T01:00:00.000Z",
                        timeMetadata);
                assertNotNull(timeMetadata);
                assertEquals(
                        "2012-04-01T00:00:00.000Z",
                        reader.getMetadataValue(coverageName, "TIME_DOMAIN_MINIMUM"));
                assertEquals(
                        "2012-04-01T01:00:00.000Z",
                        reader.getMetadataValue(coverageName, "TIME_DOMAIN_MAXIMUM"));

                if (coverageName.equalsIgnoreCase("O3")) {
                    assertEquals(
                            "true", reader.getMetadataValue(coverageName, "HAS_ELEVATION_DOMAIN"));
                    final String elevationMetadata =
                            reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN");
                    assertNotNull(elevationMetadata);
                    assertEquals("10.0/10.0,450.0/450.0", elevationMetadata);
                    assertEquals(2, elevationMetadata.split(",").length);
                    assertEquals(
                            "10.0",
                            reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN_MINIMUM"));
                    assertEquals(
                            "450.0",
                            reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN_MAXIMUM"));
                } else {
                    // Note that This sample doesn't have elevation for NO2
                    assertEquals(
                            "false", reader.getMetadataValue(coverageName, "HAS_ELEVATION_DOMAIN"));
                    final String elevationMetadata =
                            reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN");
                    assertNull(elevationMetadata);
                }

                // subsetting the envelope
                final ParameterValue<GridGeometry2D> gg =
                        AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
                final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope(coverageName);
                final GeneralEnvelope reducedEnvelope =
                        new GeneralEnvelope(
                                new double[] {
                                    originalEnvelope.getLowerCorner().getOrdinate(0),
                                    originalEnvelope.getLowerCorner().getOrdinate(1)
                                },
                                new double[] {
                                    originalEnvelope.getMedian().getOrdinate(0),
                                    originalEnvelope.getMedian().getOrdinate(1)
                                });
                reducedEnvelope.setCoordinateReferenceSystem(
                        reader.getCoordinateReferenceSystem(coverageName));

                // Selecting bigger gridRange for a zoomed result
                final Dimension dim = new Dimension();
                GridEnvelope gridRange = reader.getOriginalGridRange(coverageName);
                dim.setSize(gridRange.getSpan(0) * 4.0, gridRange.getSpan(1) * 2.0);
                final Rectangle rasterArea = ((GridEnvelope2D) gridRange);
                rasterArea.setSize(dim);
                final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
                gg.setValue(new GridGeometry2D(range, reducedEnvelope));

                final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
                final SimpleDateFormat formatD =
                        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                formatD.setTimeZone(TimeZone.getTimeZone("GMT"));
                final Date timeD = formatD.parse("2012-04-01T00:00:00.000Z");
                time.setValue(
                        new ArrayList() {
                            {
                                add(timeD);
                            }
                        });

                final ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
                elevation.setValue(
                        new ArrayList() {
                            {
                                add(450d); // Elevation
                            }
                        });

                GeneralParameterValue[] values =
                        coverageName.equalsIgnoreCase("O3")
                                ? new GeneralParameterValue[] {gg, time, elevation}
                                : new GeneralParameterValue[] {gg, time};

                GridCoverage2D coverage = reader.read(coverageName, values);
                assertNotNull(coverage);
                if (TestData.isInteractiveTest()) {
                    coverage.show();
                } else {
                    PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getTiles();
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    @Test
    public void NetCDFTestAscatL1()
            throws NoSuchAuthorityCodeException, FactoryException, IOException, ParseException {
        File mosaic = new File(TestData.file(this, "."), "NetCDFTestAscatL1");
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());
        File file = TestData.file(this, "ascatl1.nc");
        FileUtils.copyFileToDirectory(file, mosaic);
        file = new File(mosaic, "ascatl1.nc");

        final Hints hints =
                new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));
        hints.add(new Hints(Utils.EXCLUDE_MOSAIC, true));

        // Get format
        final AbstractGridFormat format =
                (AbstractGridFormat) GridFormatFinder.findFormat(file.toURI().toURL(), hints);
        final NetCDFReader reader = (NetCDFReader) format.getReader(file.toURI().toURL(), hints);

        assertNotNull(format);
        try {
            String[] names = reader.getGridCoverageNames();
            names = new String[] {names[1]};

            for (String coverageName : names) {

                final String[] metadataNames = reader.getMetadataNames(coverageName);
                assertNotNull(metadataNames);
                assertEquals(17, metadataNames.length);

                // Parsing metadata values
                assertEquals("false", reader.getMetadataValue(coverageName, "HAS_TIME_DOMAIN"));

                assertEquals(
                        "false", reader.getMetadataValue(coverageName, "HAS_ELEVATION_DOMAIN"));

                assertEquals("true", reader.getMetadataValue(coverageName, "HAS_NUMSIGMA_DOMAIN"));
                final String sigmaMetadata =
                        reader.getMetadataValue(coverageName, "NUMSIGMA_DOMAIN");
                assertNotNull(sigmaMetadata);
                assertEquals("0,1,2", sigmaMetadata);
                assertEquals(3, sigmaMetadata.split(",").length);

                // subsetting the envelope
                final ParameterValue<GridGeometry2D> gg =
                        AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
                final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope(coverageName);
                final GeneralEnvelope reducedEnvelope =
                        new GeneralEnvelope(
                                new double[] {
                                    originalEnvelope.getLowerCorner().getOrdinate(0),
                                    originalEnvelope.getLowerCorner().getOrdinate(1)
                                },
                                new double[] {
                                    originalEnvelope.getMedian().getOrdinate(0),
                                    originalEnvelope.getMedian().getOrdinate(1)
                                });
                reducedEnvelope.setCoordinateReferenceSystem(
                        reader.getCoordinateReferenceSystem(coverageName));

                // Selecting bigger gridRange for a zoomed result
                final Dimension dim = new Dimension();
                GridEnvelope gridRange = reader.getOriginalGridRange(coverageName);
                dim.setSize(gridRange.getSpan(0) * 4.0, gridRange.getSpan(1) * 2.0);
                final Rectangle rasterArea = ((GridEnvelope2D) gridRange);
                rasterArea.setSize(dim);
                final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
                gg.setValue(new GridGeometry2D(range, reducedEnvelope));

                ParameterValue<List<String>> sigmaValue = null;
                final String selectedSigma = "1";
                Set<ParameterDescriptor<List>> params = reader.getDynamicParameters(coverageName);
                for (ParameterDescriptor param : params) {
                    if (param.getName().getCode().equalsIgnoreCase("NUMSIGMA")) {
                        sigmaValue = param.createValue();
                        sigmaValue.setValue(
                                new ArrayList<String>() {
                                    {
                                        add(selectedSigma);
                                    }
                                });
                    }
                }

                GeneralParameterValue[] values = new GeneralParameterValue[] {gg, sigmaValue};
                GridCoverage2D coverage = reader.read(coverageName, values);
                assertNotNull(coverage);
                if (TestData.isInteractiveTest()) {
                    coverage.show();
                } else {
                    PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getTiles();
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    @Test
    public void NetCDFGOME2()
            throws NoSuchAuthorityCodeException, FactoryException, IOException, ParseException {
        File mosaic = new File(TestData.file(this, "."), "NetCDFGOME2");
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());
        File file = TestData.file(this, "DUMMY.GOME2.NO2.PGL.nc");
        FileUtils.copyFileToDirectory(file, mosaic);
        file = new File(mosaic, "DUMMY.GOME2.NO2.PGL.nc");

        final Hints hints =
                new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));
        // Get format
        final AbstractGridFormat format =
                (AbstractGridFormat) GridFormatFinder.findFormat(file.toURI().toURL(), hints);
        final NetCDFReader reader = (NetCDFReader) format.getReader(file.toURI().toURL(), hints);

        assertNotNull(format);
        try {
            String[] names = reader.getGridCoverageNames();
            names = new String[] {names[0]};

            for (String coverageName : names) {

                final String[] metadataNames = reader.getMetadataNames(coverageName);
                assertNotNull(metadataNames);
                assertEquals(metadataNames.length, 12);

                // Parsing metadata values
                assertEquals("false", reader.getMetadataValue(coverageName, "HAS_TIME_DOMAIN"));
                final String timeMetadata = reader.getMetadataValue(coverageName, "TIME_DOMAIN");
                assertNull(timeMetadata);

                assertEquals(
                        "false", reader.getMetadataValue(coverageName, "HAS_ELEVATION_DOMAIN"));
                final String elevationMetadata =
                        reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN");
                assertNull(elevationMetadata);
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    @Test
    public void testFileInfo()
            throws NoSuchAuthorityCodeException, FactoryException, IOException, ParseException {
        File nc2 = new File(TestData.file(this, "."), "nc2");
        if (nc2.exists()) {
            FileUtils.deleteDirectory(nc2);
        }
        assertTrue(nc2.mkdirs());

        File file = TestData.file(this, "O3-NO2.nc");
        FileUtils.copyFileToDirectory(file, nc2);
        file = new File(nc2, "O3-NO2.nc");
        final Hints hints =
                new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));
        // Get format
        final AbstractGridFormat format =
                (AbstractGridFormat) GridFormatFinder.findFormat(file.toURI().toURL(), hints);
        final NetCDFReader reader = (NetCDFReader) format.getReader(file.toURI().toURL(), hints);

        assertNotNull(format);
        CloseableIterator<FileGroup> files = null;
        try {
            String[] names = reader.getGridCoverageNames();
            names = new String[] {names[1]};

            for (String coverageName : names) {

                final String[] metadataNames = reader.getMetadataNames(coverageName);
                assertNotNull(metadataNames);
                assertEquals(metadataNames.length, 12);

                ResourceInfo info = reader.getInfo(coverageName);
                assertTrue(info instanceof FileResourceInfo);
                FileResourceInfo fileInfo = (FileResourceInfo) info;
                files = fileInfo.getFiles(null);

                int fileGroups = 0;
                FileGroup fg = null;
                while (files.hasNext()) {
                    fg = files.next();
                    fileGroups++;
                }
                assertEquals(1, fileGroups);
                File mainFile = fg.getMainFile();
                assertEquals("O3-NO2", FilenameUtils.getBaseName(mainFile.getAbsolutePath()));
                Map<String, Object> metadata = fg.getMetadata();
                assertNotNull(metadata);
                assertFalse(metadata.isEmpty());
                Set<String> keys = metadata.keySet();

                // envelope, time, elevation = 3 elements
                assertEquals(3, keys.size());

                // Check time
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
                Date start = sdf.parse("2012-04-01T00:00:00.000Z");
                Date end = sdf.parse("2012-04-01T01:00:00.000Z");
                DateRange timeRange = new DateRange(start, end);
                assertEquals(timeRange, metadata.get(Utils.TIME_DOMAIN));

                // Check elevation
                NumberRange<Double> elevationRange = NumberRange.create(10.0, 450.0);
                assertEquals(elevationRange, metadata.get(Utils.ELEVATION_DOMAIN));
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (files != null) {
                files.close();
            }
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    @Test
    public void NetCDFProjectedEnvelopeTest()
            throws NoSuchAuthorityCodeException, FactoryException, IOException, ParseException {
        File mosaic = new File(TestData.file(this, "."), "NetCDFProjection");
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());
        File file = TestData.file(this, "wind.nc");
        FileUtils.copyFileToDirectory(file, mosaic);
        file = new File(mosaic, "wind.nc");
        // Get format

        final NetCDFReader reader = new NetCDFReader(file, null);
        try {
            String[] names = reader.getGridCoverageNames();
            String coverageName = names[0];

            // subsetting the envelope
            final ParameterValue<GridGeometry2D> gg =
                    AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
            final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope(coverageName);
            final CoordinateReferenceSystem epsg3857 = CRS.decode("EPSG:3857", true);
            final GeneralEnvelope projectedEnvelope = CRS.transform(originalEnvelope, epsg3857);

            gg.setValue(
                    new GridGeometry2D(
                            new GridEnvelope2D(new Rectangle(0, 0, 30, 30)), projectedEnvelope));

            GeneralParameterValue[] values = new GeneralParameterValue[] {gg};
            GridCoverage2D coverage = reader.read(coverageName, values);

            // reader doesn't perform reprojection. It simply transforms reprojected envelope
            // to native envelope so BBOX and CRS should be wgs84
            CoordinateReferenceSystem coverageCRS = coverage.getCoordinateReferenceSystem();
            final int code = CRS.lookupEpsgCode(coverageCRS, false);
            assertEquals(4326, code);
            Extent extent = coverageCRS.getDomainOfValidity();
            Collection<? extends GeographicExtent> geoElements = extent.getGeographicElements();
            GeographicExtent geographicExtent = geoElements.iterator().next();
            GeographicBoundingBoxImpl impl = (GeographicBoundingBoxImpl) geographicExtent;

            // Getting the coverage Envelope for coordinates check
            Envelope coverageEnvelope = coverage.getEnvelope();
            assertTrue(impl.getEastBoundLongitude() >= coverageEnvelope.getMaximum(0));
            assertTrue(impl.getWestBoundLongitude() <= coverageEnvelope.getMinimum(0));
            assertTrue(impl.getNorthBoundLatitude() >= coverageEnvelope.getMaximum(1));
            assertTrue(impl.getSouthBoundLatitude() <= coverageEnvelope.getMinimum(1));

        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    @Test
    public void NetCDFNoData()
            throws NoSuchAuthorityCodeException, FactoryException, IOException, ParseException {
        File mosaic = new File(TestData.file(this, "."), "NetCDFGOME2");
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());
        File file = TestData.file(this, "DUMMY.GOME2.NO2.PGL.nc");
        FileUtils.copyFileToDirectory(file, mosaic);
        file = new File(mosaic, "DUMMY.GOME2.NO2.PGL.nc");

        final Hints hints =
                new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));
        // Get format
        final AbstractGridFormat format =
                (AbstractGridFormat) GridFormatFinder.findFormat(file.toURI().toURL(), hints);
        final NetCDFReader reader = (NetCDFReader) format.getReader(file.toURI().toURL(), hints);

        assertNotNull(format);
        try {
            String[] names = reader.getGridCoverageNames();
            names = new String[] {names[0]};
            GridCoverage2D gc = reader.read(null);
            Object noData = CoverageUtilities.getNoDataProperty(gc);
            assertNotNull(noData);
            assertTrue(noData instanceof NoDataContainer);
            Double d = ((NoDataContainer) noData).getAsSingleValue();
            assertEquals(d, -999d, DELTA);

        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    @Test
    public void NetCDFTestOnClimatologicalTime()
            throws NoSuchAuthorityCodeException, FactoryException, IOException, ParseException {
        final File workDir = new File(TestData.file(this, "."), "climatological");
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }

        FileUtils.copyFile(
                TestData.file(this, "climatological.zip"), new File(workDir, "climatological.zip"));
        TestData.unzipFile(this, "climatological/climatological.zip");

        File file = new File(workDir, "climatological.nc");

        // Get format
        final AbstractGridFormat format =
                (AbstractGridFormat) GridFormatFinder.findFormat(file.toURI().toURL(), null);
        final NetCDFReader reader = (NetCDFReader) format.getReader(file.toURI().toURL(), null);

        assertNotNull(format);
        try {
            String[] names = reader.getGridCoverageNames();
            names = new String[] {names[1]};

            for (String coverageName : names) {

                final String[] metadataNames = reader.getMetadataNames(coverageName);
                assertNotNull(metadataNames);
                assertEquals(metadataNames.length, 12);

                // Parsing metadata values
                assertEquals("true", reader.getMetadataValue(coverageName, "HAS_TIME_DOMAIN"));
                final String timeMetadata = reader.getMetadataValue(coverageName, "TIME_DOMAIN");
                assertNotNull(timeMetadata);
                assertEquals(
                        "0001-01-16T00:00:00.000Z/0001-01-16T00:00:00.000Z,0001-02-16T00:00:00.000Z/0001-02-16T00:00:00.000Z,0001-03-16T00:00:00.000Z/0001-03-16T00:00:00.000Z,0001-04-16T00:00:00.000Z/0001-04-16T00:00:00.000Z,0001-05-16T00:00:00.000Z/0001-05-16T00:00:00.000Z,0001-06-16T00:00:00.000Z/0001-06-16T00:00:00.000Z,0001-07-16T00:00:00.000Z/0001-07-16T00:00:00.000Z,0001-08-16T00:00:00.000Z/0001-08-16T00:00:00.000Z,0001-09-16T00:00:00.000Z/0001-09-16T00:00:00.000Z,0001-10-16T00:00:00.000Z/0001-10-16T00:00:00.000Z,0001-11-16T00:00:00.000Z/0001-11-16T00:00:00.000Z,0001-12-16T00:00:00.000Z/0001-12-16T00:00:00.000Z",
                        timeMetadata);
                assertEquals(12, timeMetadata.split(",").length);
                assertEquals(
                        "0001-01-16T00:00:00.000Z",
                        reader.getMetadataValue(coverageName, "TIME_DOMAIN_MINIMUM"));
                assertEquals(
                        "0001-12-16T00:00:00.000Z",
                        reader.getMetadataValue(coverageName, "TIME_DOMAIN_MAXIMUM"));

                assertEquals("true", reader.getMetadataValue(coverageName, "HAS_ELEVATION_DOMAIN"));
                final String elevationMetadata =
                        reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN");
                assertNotNull(elevationMetadata);
                assertEquals(
                        "0.0/0.0,10.0/10.0,20.0/20.0,30.0/30.0,50.0/50.0,75.0/75.0",
                        elevationMetadata);
                assertEquals(6, elevationMetadata.split(",").length);
                assertEquals(
                        "0.0", reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN_MINIMUM"));
                assertEquals(
                        "75.0", reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN_MAXIMUM"));

                List<DimensionDescriptor> descriptors =
                        ((StructuredGridCoverage2DReader) reader)
                                .getDimensionDescriptors(coverageName);
                assertNotNull(descriptors);
                assertEquals(2, descriptors.size());

                DimensionDescriptor descriptor = descriptors.get(0);
                assertEquals("TIME", descriptor.getName());
                assertEquals("time", descriptor.getStartAttribute());

                descriptor = descriptors.get(1);
                assertEquals("ELEVATION", descriptor.getName());
                assertEquals("depth", descriptor.getStartAttribute());

                final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
                Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
                calendar.set(0, 0, 16, 0, 0, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                time.setValue(
                        new ArrayList() {
                            {
                                add(calendar.getTime());
                            }
                        });

                final ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
                elevation.setValue(
                        new ArrayList() {
                            {
                                add(50d); // Elevation
                            }
                        });

                GeneralParameterValue[] values = new GeneralParameterValue[] {time, elevation};
                GridCoverage2D coverage = reader.read(coverageName, values);
                assertNotNull(coverage);
                if (TestData.isInteractiveTest()) {
                    coverage.show();
                } else {
                    PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getTiles();
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    @Test
    public void NetCDFNoDataOperation()
            throws NoSuchAuthorityCodeException, FactoryException, IOException, ParseException {
        File mosaic = new File(TestData.file(this, "."), "NetCDFGOME2");
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());
        File file = TestData.file(this, "DUMMY.GOME2.NO2.PGL.nc");
        FileUtils.copyFileToDirectory(file, mosaic);
        file = new File(mosaic, "DUMMY.GOME2.NO2.PGL.nc");

        final Hints hints =
                new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));
        // Get format
        final AbstractGridFormat format =
                (AbstractGridFormat) GridFormatFinder.findFormat(file.toURI().toURL(), hints);
        final NetCDFReader reader = (NetCDFReader) format.getReader(file.toURI().toURL(), hints);

        assertNotNull(format);
        GridCoverage2D gc = null;
        try {
            String[] names = reader.getGridCoverageNames();
            names = new String[] {names[0]};
            gc = reader.read(null);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
        // Checking NoData
        Object noData = CoverageUtilities.getNoDataProperty(gc);
        assertNotNull(noData);
        assertTrue(noData instanceof NoDataContainer);
        Double d = ((NoDataContainer) noData).getAsSingleValue();
        assertEquals(d, -999d, DELTA);
        // Try to execute an operation on the NoData and check if the result contains also NoData
        CoverageProcessor instance = CoverageProcessor.getInstance();
        Operation scale = instance.getOperation("Scale");
        ParameterValueGroup params = scale.getParameters();
        params.parameter("Source0").setValue(gc);
        params.parameter("backgroundValues").setValue(new double[] {0});
        GridCoverage2D result = (GridCoverage2D) instance.doOperation(params);
        noData = CoverageUtilities.getNoDataProperty(result);
        assertNotNull(noData);
        assertTrue(noData instanceof NoDataContainer);
        d = ((NoDataContainer) noData).getAsSingleValue();
        assertEquals(d, 0d, DELTA);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    @Ignore
    public void IASI() throws Exception {

        final URL testURL = TestData.url(this, "IASI_C_EUMP_20121120062959_31590_eps_o_l2.nc");
        final Hints hints =
                new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));
        // Get format
        final AbstractGridFormat format =
                (AbstractGridFormat) GridFormatFinder.findFormat(testURL, hints);
        final NetCDFReader reader = (NetCDFReader) format.getReader(testURL, hints);
        assertNotNull(format);
        assertNotNull(reader);
        try {
            String[] names = reader.getGridCoverageNames();
            assertNotNull(names);
            assertEquals(names.length, 20);

            // surface_emissivity
            final String coverageName = "surface_emissivity";
            final String[] metadataNames = reader.getMetadataNames(coverageName);
            assertNotNull(metadataNames);
            assertEquals(14, metadataNames.length);

            // Parsing metadata values
            assertEquals("false", reader.getMetadataValue(coverageName, "HAS_TIME_DOMAIN"));
            assertEquals("false", reader.getMetadataValue(coverageName, "HAS_ELEVATION_DOMAIN"));
            assertEquals("true", reader.getMetadataValue(coverageName, "HAS_NEW_DOMAIN"));

            // additional domains
            final String newDomain = reader.getMetadataValue(coverageName, "NEW_DOMAIN");
            assertNotNull(metadataNames);
            final String[] newDomainValues = newDomain.split(",");
            assertNotNull(newDomainValues);
            assertEquals(12, newDomainValues.length);
            assertEquals(13.063399669990758, Double.valueOf(newDomainValues[11]), 1E-6);
            assertEquals(3.6231999084702693, Double.valueOf(newDomainValues[0]), 1E-6);

            // subsetting the envelope
            final ParameterValue<GridGeometry2D> gg =
                    AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
            final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope(coverageName);
            final GeneralEnvelope reducedEnvelope =
                    new GeneralEnvelope(
                            new double[] {
                                originalEnvelope.getLowerCorner().getOrdinate(0),
                                originalEnvelope.getLowerCorner().getOrdinate(1)
                            },
                            new double[] {
                                originalEnvelope.getMedian().getOrdinate(0),
                                originalEnvelope.getMedian().getOrdinate(1)
                            });
            reducedEnvelope.setCoordinateReferenceSystem(
                    reader.getCoordinateReferenceSystem(coverageName));

            // Selecting bigger gridRange for a zoomed result
            final Dimension dim = new Dimension();
            GridEnvelope gridRange = reader.getOriginalGridRange(coverageName);
            dim.setSize(gridRange.getSpan(0) * 4.0, gridRange.getSpan(1) * 2.0);
            final Rectangle rasterArea = ((GridEnvelope2D) gridRange);
            rasterArea.setSize(dim);
            final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
            gg.setValue(new GridGeometry2D(range, reducedEnvelope));

            // specify additional Dimensions
            Set<ParameterDescriptor<List>> params = reader.getDynamicParameters(coverageName);
            ParameterValue<List> new_ = null;
            for (ParameterDescriptor param : params) {
                if (param.getName().getCode().equalsIgnoreCase("NEW")) {
                    new_ = param.createValue();
                    new_.setValue(
                            new ArrayList() {
                                {
                                    add(Double.valueOf(newDomainValues[11]));
                                }
                            });
                }
            }

            GeneralParameterValue[] values = new GeneralParameterValue[] {gg, new_};
            GridCoverage2D coverage = reader.read(coverageName, values);
            assertNotNull(coverage);
            if (TestData.isInteractiveTest()) {
                coverage.show();
            } else {
                PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getTiles();
            }
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    /**
     * Test that {@link NetCDFReader#getOriginalEnvelope()}, a method for which no coverage name is
     * specified, works for a NetCDF source containing multiple coverages. The method is expected to
     * succeed using {@link NetCDFReader#defaultName}.
     */
    @Test
    public void testGetOriginalEnvelopeDefaultName() throws Exception {
        NetCDFReader reader = new NetCDFReader(TestData.file(this, "O3-NO2.nc"), null);
        GeneralEnvelope envelope = reader.getOriginalEnvelope();
        assertNotNull(envelope);
        assertFalse(envelope.isEmpty());
        reader.dispose();
    }

    /**
     * Test that {@link GridCoverage2DReader#SOURCE_URL_PROPERTY} is correctly set on a coverage
     * read from a NetCDF file.
     */
    @Test
    public void testSourceUrl() throws Exception {
        NetCDFReader reader = new NetCDFReader(TestData.file(this, "O3-NO2.nc"), null);
        GridCoverage2D coverage = reader.read("O3", new GeneralParameterValue[] {});
        URL sourceUrl = (URL) coverage.getProperty(GridCoverage2DReader.SOURCE_URL_PROPERTY);
        assertNotNull(sourceUrl);
        assertEquals("file", sourceUrl.getProtocol());
        assertTrue(sourceUrl.getPath().endsWith("O3-NO2.nc"));
        reader.dispose();
    }

    @Test
    public void testPreserveUnit() throws Exception {
        NetCDFReader reader = new NetCDFReader(TestData.file(this, "sst.nc"), null);
        GridCoverage2D coverage = reader.read(reader.getGridCoverageNames()[0], null);
        assertEquals(SI.CELSIUS, coverage.getSampleDimension(0).getUnits());
        coverage.dispose(true);
        reader.dispose();
    }

    @Test
    public void testPurgeMetadataOnly() throws Exception {
        String directoryName = "purgeMetadataOnly";
        PurgeSetupHelper purgeSetupHelper = new PurgeSetupHelper(directoryName).invoke();

        // now remove only the metadata
        NetCDFReader reader = purgeSetupHelper.reader;
        reader.delete(false);
        reader.dispose();

        // check we indeed removed the metadata only
        assertFalse(
                "Metadata dir should have been removed",
                purgeSetupHelper.metadataDirectory.get().exists());
        assertTrue("Data file should still be there", purgeSetupHelper.dataFile.get().exists());
    }

    @Test
    public void testPurgeAll() throws Exception {
        String directoryName = "purgeAll";
        PurgeSetupHelper purgeSetupHelper = new PurgeSetupHelper(directoryName).invoke();

        // now remove only the metadata
        NetCDFReader reader = purgeSetupHelper.reader;
        reader.delete(true);
        reader.dispose();

        assertFalse(
                "Metadata dir should have been removed",
                purgeSetupHelper.metadataDirectory.get().exists());
        assertFalse(
                "Data file should also have been removed",
                purgeSetupHelper.dataFile.get().exists());
    }

    private class PurgeSetupHelper {
        private String directoryName;
        NetCDFReader reader;
        Optional<File> metadataDirectory;
        Optional<File> dataFile;

        public PurgeSetupHelper(String directoryName) {
            this.directoryName = directoryName;
        }

        public PurgeSetupHelper invoke() throws IOException {
            File directory = new File(TestData.file(NetCDFReaderTest.this, "."), directoryName);
            if (directory.exists()) {
                FileUtils.deleteDirectory(directory);
            }
            assertTrue(directory.mkdirs());

            FileUtils.copyFileToDirectory(
                    TestData.file(NetCDFReaderTest.this, "O3-NO2.nc"), directory);
            File file = new File(directory, "O3-NO2.nc");

            // create a reader and read coverage, should create everything
            reader = new NetCDFReader(file, null);
            GridCoverage2D read = reader.read(reader.getGridCoverageNames()[0], null);
            read.dispose(true);

            // check we have file and the metadata directory
            File[] files = directory.listFiles();
            assertEquals(2, files.length);
            metadataDirectory =
                    Arrays.stream(files)
                            .filter(f -> f.getName().startsWith(".O3-NO2") && f.isDirectory())
                            .findFirst();
            assertTrue(metadataDirectory.isPresent());
            dataFile =
                    Arrays.stream(files).filter(f -> f.getName().equals("O3-NO2.nc")).findFirst();
            assertTrue(dataFile.isPresent());
            return this;
        }
    }
}
