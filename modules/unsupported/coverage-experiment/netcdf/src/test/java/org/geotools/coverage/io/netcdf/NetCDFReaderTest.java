/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002, Geotools Project Managment Committee (PMC)
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
package org.geotools.coverage.io.netcdf;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.media.jai.PlanarImage;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.DimensionDescriptor;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.StructuredGridCoverage2DReader;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.ImageMosaicFormat;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.resources.coverage.FeatureUtilities;
import org.geotools.test.TestData;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

public class NetCDFReaderTest extends Assert {

    @Test
    public void NetCDFTestOn4Dcoverages() throws NoSuchAuthorityCodeException, FactoryException, IOException, ParseException {
        File mosaic = new File(TestData.file(this,"."),"NetCDFTestOn4Dcoverages");
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());
        
        File file = TestData.file(this, "O3-NO2.nc");
        FileUtils.copyFileToDirectory(file, mosaic);
        file = new File(mosaic, "O3-NO2.nc");
        final Hints hints= new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));
        // Get format
        final AbstractGridFormat format = (AbstractGridFormat) GridFormatFinder.findFormat(file.toURI().toURL(),hints);
        final NetCDFReader reader = (NetCDFReader) format.getReader(file.toURI().toURL(), hints);
        
        assertNotNull(format);
        try {
            String[] names = reader.getGridCoverageNames();
            names = new String[] { names[1] };

            for (String coverageName : names) {

                final String[] metadataNames = reader.getMetadataNames(coverageName);
                assertNotNull(metadataNames);
                assertEquals(metadataNames.length, 12);

                // Parsing metadata values
                assertEquals("true", reader.getMetadataValue(coverageName, "HAS_TIME_DOMAIN"));
                final String timeMetadata = reader.getMetadataValue(coverageName, "TIME_DOMAIN");
                assertEquals( "2012-04-01T00:00:00.000Z/2012-04-01T00:00:00.000Z,2012-04-01T01:00:00.000Z/2012-04-01T01:00:00.000Z",
                        timeMetadata);
                assertNotNull(timeMetadata);
                assertEquals("2012-04-01T00:00:00.000Z", reader.getMetadataValue(coverageName, "TIME_DOMAIN_MINIMUM"));
                assertEquals("2012-04-01T01:00:00.000Z", reader.getMetadataValue(coverageName, "TIME_DOMAIN_MAXIMUM"));

                assertEquals("true", reader.getMetadataValue(coverageName, "HAS_ELEVATION_DOMAIN"));
                final String elevationMetadata = reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN");
                assertNotNull(elevationMetadata);
                assertEquals("10.0/10.0,450.0/450.0", elevationMetadata);
                assertEquals(2, elevationMetadata.split(",").length);
                assertEquals("10.0", reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN_MINIMUM"));
                assertEquals("450.0", reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN_MAXIMUM"));

                List<DimensionDescriptor> descriptors = ((StructuredGridCoverage2DReader)reader).getDimensionDescriptors(coverageName);
                assertNotNull(descriptors);
                assertEquals(2, descriptors.size());

                DimensionDescriptor descriptor = descriptors.get(0);
                assertEquals("TIME", descriptor.getName());
                assertEquals("time", descriptor.getStartAttribute());
                assertNull(descriptor.getEndAttribute());
                assertEquals(CoverageUtilities.UCUM.TIME_UNITS.getName(), descriptor.getUnits());
                assertEquals(CoverageUtilities.UCUM.TIME_UNITS.getSymbol(), descriptor.getUnitSymbol());

                descriptor = descriptors.get(1);
                assertEquals("ELEVATION", descriptor.getName());
                assertEquals("z", descriptor.getStartAttribute());
                assertNull(descriptor.getEndAttribute());
                assertEquals("meters", descriptor.getUnits());
                assertEquals(CoverageUtilities.UCUM.ELEVATION_UNITS.getSymbol(), descriptor.getUnitSymbol());

                // subsetting the envelope
                final ParameterValue<GridGeometry2D> gg = AbstractGridFormat.READ_GRIDGEOMETRY2D
                        .createValue();
                final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope(coverageName);
                final GeneralEnvelope reducedEnvelope = new GeneralEnvelope(new double[] {
                        originalEnvelope.getLowerCorner().getOrdinate(0),
                        originalEnvelope.getLowerCorner().getOrdinate(1) }, new double[] {
                        originalEnvelope.getMedian().getOrdinate(0),
                        originalEnvelope.getMedian().getOrdinate(1) });
                reducedEnvelope.setCoordinateReferenceSystem(reader
                        .getCoordinateReferenceSystem(coverageName));

                // Selecting bigger gridRange for a zoomed result
                final Dimension dim = new Dimension();
                GridEnvelope gridRange = reader.getOriginalGridRange(coverageName);
                dim.setSize(gridRange.getSpan(0) * 4.0, gridRange.getSpan(1) * 2.0);
                final Rectangle rasterArea = ((GridEnvelope2D) gridRange);
                rasterArea.setSize(dim);
                final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
                gg.setValue(new GridGeometry2D(range, reducedEnvelope));

                final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
                final SimpleDateFormat formatD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                formatD.setTimeZone(TimeZone.getTimeZone("GMT"));
                final Date timeD = formatD.parse("2012-04-01T00:00:00.000Z");
                time.setValue(new ArrayList() {
                    {
                        add(timeD);
                    }
                });

                final ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
                elevation.setValue(new ArrayList() {
                    {
                        add(450d); // Elevation
                    }
                });

                GeneralParameterValue[] values = new GeneralParameterValue[] { gg, time, elevation };
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
    public void NetCDFTestOnFilter() throws NoSuchAuthorityCodeException, FactoryException, IOException, ParseException {
        File mosaic = new File(TestData.file(this,"."),"NetCDFTestOnFilter");
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());             
        
        File file = TestData.file(this, "O3-NO2.nc");
        FileUtils.copyFileToDirectory(file, mosaic);
        file = new File(mosaic, "O3-NO2.nc");
        
        final Hints hints= new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));
        // Get format
        final AbstractGridFormat format = (AbstractGridFormat) GridFormatFinder.findFormat(file.toURI().toURL(),hints);
        final NetCDFReader reader = (NetCDFReader) format.getReader(file.toURI().toURL(), hints);
        
        assertNotNull(format);
        try {
            String[] names = reader.getGridCoverageNames();
            names = new String[] { names[1] };

            for (String coverageName : names) {

                final String[] metadataNames = reader.getMetadataNames(coverageName);
                assertNotNull(metadataNames);
                assertEquals(12, metadataNames.length);

                // Parsing metadata values
                assertEquals("true", reader.getMetadataValue(coverageName, "HAS_TIME_DOMAIN"));
                final String timeMetadata = reader.getMetadataValue(coverageName, "TIME_DOMAIN");
                assertEquals( "2012-04-01T00:00:00.000Z/2012-04-01T00:00:00.000Z,2012-04-01T01:00:00.000Z/2012-04-01T01:00:00.000Z",
                        timeMetadata);
                assertNotNull(timeMetadata);
                assertEquals("2012-04-01T00:00:00.000Z", reader.getMetadataValue(coverageName, "TIME_DOMAIN_MINIMUM"));
                assertEquals("2012-04-01T01:00:00.000Z", reader.getMetadataValue(coverageName, "TIME_DOMAIN_MAXIMUM"));

                assertEquals("true", reader.getMetadataValue(coverageName, "HAS_ELEVATION_DOMAIN"));
                final String elevationMetadata = reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN");
                assertNotNull(elevationMetadata);
                assertEquals("10.0/10.0,450.0/450.0", elevationMetadata);
                assertEquals(2, elevationMetadata.split(",").length);
                assertEquals("10.0", reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN_MINIMUM"));
                assertEquals("450.0", reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN_MAXIMUM"));

                // subsetting the envelope
                final ParameterValue<GridGeometry2D> gg = AbstractGridFormat.READ_GRIDGEOMETRY2D
                        .createValue();
                final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope(coverageName);
                final GeneralEnvelope reducedEnvelope = new GeneralEnvelope(new double[] {
                        originalEnvelope.getLowerCorner().getOrdinate(0),
                        originalEnvelope.getLowerCorner().getOrdinate(1) }, new double[] {
                        originalEnvelope.getMedian().getOrdinate(0),
                        originalEnvelope.getMedian().getOrdinate(1) });
                reducedEnvelope.setCoordinateReferenceSystem(reader
                        .getCoordinateReferenceSystem(coverageName));

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

                GeneralParameterValue[] values = new GeneralParameterValue[] { filterParam };
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
    public void NetCDFTestOn4DcoveragesWithDifferentSchemas() throws NoSuchAuthorityCodeException, FactoryException, IOException, ParseException {
        File mosaic = new File(TestData.file(this,"."),"NetCDFTestOn4DcoveragesWithDifferentSchemas");
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());        
        
        File file = TestData.file(this, "O3-NO2-noZ.nc");
        FileUtils.copyFileToDirectory(file, mosaic);
        file = new File(mosaic, "O3-NO2-noZ.nc");
        
        final Hints hints= new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));
        // Get format
        final AbstractGridFormat format = (AbstractGridFormat) GridFormatFinder.findFormat(file.toURI().toURL(),hints);
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
                assertEquals( "2012-04-01T00:00:00.000Z/2012-04-01T00:00:00.000Z,2012-04-01T01:00:00.000Z/2012-04-01T01:00:00.000Z",
                        timeMetadata);
                assertNotNull(timeMetadata);
                assertEquals("2012-04-01T00:00:00.000Z", reader.getMetadataValue(coverageName, "TIME_DOMAIN_MINIMUM"));
                assertEquals("2012-04-01T01:00:00.000Z", reader.getMetadataValue(coverageName, "TIME_DOMAIN_MAXIMUM"));

                if (coverageName.equalsIgnoreCase("O3")) {
                    assertEquals("true", reader.getMetadataValue(coverageName, "HAS_ELEVATION_DOMAIN"));
                    final String elevationMetadata = reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN");
                    assertNotNull(elevationMetadata);
                    assertEquals("10.0/10.0,450.0/450.0", elevationMetadata);
                    assertEquals(2, elevationMetadata.split(",").length);
                    assertEquals("10.0", reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN_MINIMUM"));
                    assertEquals("450.0", reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN_MAXIMUM"));
                } else {
                    // Note that This sample doesn't have elevation for NO2
                    assertEquals("false", reader.getMetadataValue(coverageName, "HAS_ELEVATION_DOMAIN"));
                    final String elevationMetadata = reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN");
                    assertNull(elevationMetadata);
                    
                }

                // subsetting the envelope
                final ParameterValue<GridGeometry2D> gg = AbstractGridFormat.READ_GRIDGEOMETRY2D
                        .createValue();
                final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope(coverageName);
                final GeneralEnvelope reducedEnvelope = new GeneralEnvelope(new double[] {
                        originalEnvelope.getLowerCorner().getOrdinate(0),
                        originalEnvelope.getLowerCorner().getOrdinate(1) }, new double[] {
                        originalEnvelope.getMedian().getOrdinate(0),
                        originalEnvelope.getMedian().getOrdinate(1) });
                reducedEnvelope.setCoordinateReferenceSystem(reader
                        .getCoordinateReferenceSystem(coverageName));

                // Selecting bigger gridRange for a zoomed result
                final Dimension dim = new Dimension();
                GridEnvelope gridRange = reader.getOriginalGridRange(coverageName);
                dim.setSize(gridRange.getSpan(0) * 4.0, gridRange.getSpan(1) * 2.0);
                final Rectangle rasterArea = ((GridEnvelope2D) gridRange);
                rasterArea.setSize(dim);
                final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
                gg.setValue(new GridGeometry2D(range, reducedEnvelope));

                final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
                final SimpleDateFormat formatD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                formatD.setTimeZone(TimeZone.getTimeZone("GMT"));
                final Date timeD = formatD.parse("2012-04-01T00:00:00.000Z");
                time.setValue(new ArrayList() {
                    {
                        add(timeD);
                    }
                });

                final ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
                elevation.setValue(new ArrayList() {
                    {
                        add(450d); // Elevation
                    }
                });

                GeneralParameterValue[] values = coverageName.equalsIgnoreCase("O3") ? new GeneralParameterValue[] { gg, time, elevation } : new GeneralParameterValue[] { gg, time};
                
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
    public void NetCDFTestOn4DcoveragesWithImposedSchemas() throws NoSuchAuthorityCodeException, FactoryException, IOException, ParseException {
        File mosaic = new File(TestData.file(this,"."),"NetCDFTestOn4DcoveragesWithImposedSchemas");
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());          
        
        File file = TestData.file(this, "O3NO2-noZ.nc");
        File auxFile = TestData.file(this, "O3NO2-noZ.xml");
        FileUtils.copyFileToDirectory(file, mosaic);
        FileUtils.copyFileToDirectory(auxFile, mosaic);
        file = new File(mosaic, "O3NO2-noZ.nc");
        
        final Hints hints= new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));      
        hints.put(Utils.AUXILIARY_FILES_PATH, new File(mosaic,"O3NO2-noZ.xml").getAbsolutePath()); // impose def

        // Get format
        final AbstractGridFormat format = (AbstractGridFormat) GridFormatFinder.findFormat(file.toURI().toURL(),hints);
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
                assertEquals( "2012-04-01T00:00:00.000Z/2012-04-01T00:00:00.000Z,2012-04-01T01:00:00.000Z/2012-04-01T01:00:00.000Z",
                        timeMetadata);
                assertNotNull(timeMetadata);
                assertEquals("2012-04-01T00:00:00.000Z", reader.getMetadataValue(coverageName, "TIME_DOMAIN_MINIMUM"));
                assertEquals("2012-04-01T01:00:00.000Z", reader.getMetadataValue(coverageName, "TIME_DOMAIN_MAXIMUM"));

                if (coverageName.equalsIgnoreCase("O3")) {
                    assertEquals("true", reader.getMetadataValue(coverageName, "HAS_ELEVATION_DOMAIN"));
                    final String elevationMetadata = reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN");
                    assertNotNull(elevationMetadata);
                    assertEquals("10.0/10.0,450.0/450.0", elevationMetadata);
                    assertEquals(2, elevationMetadata.split(",").length);
                    assertEquals("10.0", reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN_MINIMUM"));
                    assertEquals("450.0", reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN_MAXIMUM"));
                } else {
                    // Note that This sample doesn't have elevation for NO2
                    assertEquals("false", reader.getMetadataValue(coverageName, "HAS_ELEVATION_DOMAIN"));
                    final String elevationMetadata = reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN");
                    assertNull(elevationMetadata);
                    
                }

                // subsetting the envelope
                final ParameterValue<GridGeometry2D> gg = AbstractGridFormat.READ_GRIDGEOMETRY2D
                        .createValue();
                final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope(coverageName);
                final GeneralEnvelope reducedEnvelope = new GeneralEnvelope(new double[] {
                        originalEnvelope.getLowerCorner().getOrdinate(0),
                        originalEnvelope.getLowerCorner().getOrdinate(1) }, new double[] {
                        originalEnvelope.getMedian().getOrdinate(0),
                        originalEnvelope.getMedian().getOrdinate(1) });
                reducedEnvelope.setCoordinateReferenceSystem(reader
                        .getCoordinateReferenceSystem(coverageName));

                // Selecting bigger gridRange for a zoomed result
                final Dimension dim = new Dimension();
                GridEnvelope gridRange = reader.getOriginalGridRange(coverageName);
                dim.setSize(gridRange.getSpan(0) * 4.0, gridRange.getSpan(1) * 2.0);
                final Rectangle rasterArea = ((GridEnvelope2D) gridRange);
                rasterArea.setSize(dim);
                final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
                gg.setValue(new GridGeometry2D(range, reducedEnvelope));

                final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
                final SimpleDateFormat formatD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                formatD.setTimeZone(TimeZone.getTimeZone("GMT"));
                final Date timeD = formatD.parse("2012-04-01T00:00:00.000Z");
                time.setValue(new ArrayList() {
                    {
                        add(timeD);
                    }
                });

                final ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
                elevation.setValue(new ArrayList() {
                    {
                        add(450d); // Elevation
                    }
                });

                GeneralParameterValue[] values = coverageName.equalsIgnoreCase("O3") ? new GeneralParameterValue[] { gg, time, elevation } : new GeneralParameterValue[] { gg, time};
                
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
  public void NetCDFTestAscatL1() throws NoSuchAuthorityCodeException, FactoryException, IOException, ParseException {
        File mosaic = new File(TestData.file(this,"."),"NetCDFTestAscatL1");
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());
        File file = TestData.file(this, "ascatl1.nc");
        FileUtils.copyFileToDirectory(file, mosaic);
        file = new File(mosaic, "ascatl1.nc");
        
      final Hints hints= new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));
      hints.add(new Hints(Utils.EXCLUDE_MOSAIC, true));

      // Get format
      final AbstractGridFormat format = (AbstractGridFormat) GridFormatFinder.findFormat(file.toURI().toURL(),hints);
      final NetCDFReader reader = (NetCDFReader) format.getReader(file.toURI().toURL(), hints);
      
      assertNotNull(format);
      try {
          String[] names = reader.getGridCoverageNames();
          names = new String[] { names[1] };

          for (String coverageName : names) {

              final String[] metadataNames = reader.getMetadataNames(coverageName);
              assertNotNull(metadataNames);
              assertEquals(17, metadataNames.length);

              // Parsing metadata values
              assertEquals("false", reader.getMetadataValue(coverageName, "HAS_TIME_DOMAIN"));

              assertEquals("false", reader.getMetadataValue(coverageName, "HAS_ELEVATION_DOMAIN"));
              
              assertEquals("true", reader.getMetadataValue(coverageName, "HAS_NUMSIGMA_DOMAIN"));
              final String sigmaMetadata = reader.getMetadataValue(coverageName, "NUMSIGMA_DOMAIN");
              assertNotNull(sigmaMetadata);
              assertEquals("0,1,2", sigmaMetadata);
              assertEquals(3, sigmaMetadata.split(",").length);

              // subsetting the envelope
              final ParameterValue<GridGeometry2D> gg = AbstractGridFormat.READ_GRIDGEOMETRY2D
                      .createValue();
              final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope(coverageName);
              final GeneralEnvelope reducedEnvelope = new GeneralEnvelope(new double[] {
                      originalEnvelope.getLowerCorner().getOrdinate(0),
                      originalEnvelope.getLowerCorner().getOrdinate(1) }, new double[] {
                      originalEnvelope.getMedian().getOrdinate(0),
                      originalEnvelope.getMedian().getOrdinate(1) });
              reducedEnvelope.setCoordinateReferenceSystem(reader
                      .getCoordinateReferenceSystem(coverageName));

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
                      sigmaValue.setValue(new ArrayList<String>() {
                          {
                              add(selectedSigma);
                          }
                      });
                  }
              }
              
              GeneralParameterValue[] values = new GeneralParameterValue[] { gg, sigmaValue};
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
    public void NetCDFGOME2() throws NoSuchAuthorityCodeException, FactoryException, IOException, ParseException {
        File mosaic = new File(TestData.file(this,"."),"NetCDFGOME2");
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());
        File file = TestData.file(this, "DUMMY.GOME2.NO2.PGL.nc");
        FileUtils.copyFileToDirectory(file, mosaic);
        file = new File(mosaic, "DUMMY.GOME2.NO2.PGL.nc");
        
        final Hints hints= new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));
        // Get format
        final AbstractGridFormat format = (AbstractGridFormat) GridFormatFinder.findFormat(file.toURI().toURL(),hints);
        final NetCDFReader reader = (NetCDFReader) format.getReader(file.toURI().toURL(), hints);
        
        assertNotNull(format);
        try {
            String[] names = reader.getGridCoverageNames();
            names = new String[] { names[0] };

            for (String coverageName : names) {

                final String[] metadataNames = reader.getMetadataNames(coverageName);
                assertNotNull(metadataNames);
                assertEquals(metadataNames.length, 12);

                // Parsing metadata values
                assertEquals("false", reader.getMetadataValue(coverageName, "HAS_TIME_DOMAIN"));
                final String timeMetadata = reader.getMetadataValue(coverageName, "TIME_DOMAIN");
                assertNull(timeMetadata);

                assertEquals("false", reader.getMetadataValue(coverageName, "HAS_ELEVATION_DOMAIN"));
                final String elevationMetadata = reader.getMetadataValue(coverageName, "ELEVATION_DOMAIN");
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

    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    @Ignore
    public void IASI() throws Exception {
        
        final URL testURL = TestData.url(this, "IASI_C_EUMP_20121120062959_31590_eps_o_l2.nc");
        final Hints hints= new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));
        // Get format
        final AbstractGridFormat format = (AbstractGridFormat) GridFormatFinder.findFormat(testURL,hints);
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
            assertEquals(14,metadataNames.length);

            // Parsing metadata values
            assertEquals("false", reader.getMetadataValue(coverageName, "HAS_TIME_DOMAIN"));
            assertEquals("false", reader.getMetadataValue(coverageName, "HAS_ELEVATION_DOMAIN"));
            assertEquals("true", reader.getMetadataValue(coverageName, "HAS_NEW_DOMAIN"));
            
            // additional domains
            final String newDomain=reader.getMetadataValue(coverageName, "NEW_DOMAIN");
            assertNotNull(metadataNames);
            final String[] newDomainValues = newDomain.split(",");
            assertNotNull(newDomainValues);
            assertEquals(12,newDomainValues.length);
            assertEquals(13.063399669990758,Double.valueOf(newDomainValues[11]),1E-6);
            assertEquals(3.6231999084702693,Double.valueOf(newDomainValues[0]),1E-6);
            
            
            // subsetting the envelope
            final ParameterValue<GridGeometry2D> gg = AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
            final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope(coverageName);
            final GeneralEnvelope reducedEnvelope = new GeneralEnvelope(new double[] {
                    originalEnvelope.getLowerCorner().getOrdinate(0),
                    originalEnvelope.getLowerCorner().getOrdinate(1) }, new double[] {
                    originalEnvelope.getMedian().getOrdinate(0),
                    originalEnvelope.getMedian().getOrdinate(1) });
            reducedEnvelope.setCoordinateReferenceSystem(reader
                    .getCoordinateReferenceSystem(coverageName));

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
                    new_.setValue(new ArrayList() {
                        {
                            add(Double.valueOf(newDomainValues[11]));
                        }
                    });
                } 
            }

            GeneralParameterValue[] values = new GeneralParameterValue[] { gg,new_ };
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
}
