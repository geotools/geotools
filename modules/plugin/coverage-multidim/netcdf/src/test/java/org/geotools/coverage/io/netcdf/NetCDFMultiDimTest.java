/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import org.apache.commons.io.FileUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.Query;
import org.geotools.gce.imagemosaic.ImageMosaicFormat;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.imageio.netcdf.NetCDFImageReader;
import org.geotools.imageio.netcdf.NetCDFImageReaderSpi;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.test.TestData;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;

/**
 * @author Niels Charlier
 *     <p>the samples used in this test class (.nc and .ncml files located in test-data/unidata) are
 *     taken from
 *     http://www.unidata.ucar.edu/software/thredds/current/netcdf-java/ncml/Aggregation.html (see
 *     THREDDS license) except the reversed sample files which are manipulations of the originals
 *     from the website above.
 */
@Ignore
public class NetCDFMultiDimTest {

    private static String D = "D";

    private static SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Rule public TemporaryFolder tempFolder = new TemporaryFolder();

    static {
        DF.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void test() throws Exception {
        NetCDFFormat format = new NetCDFFormat();
        File file = TestData.file(this, "fivedim.nc");

        NetCDFReader reader = (NetCDFReader) format.getReader(file);

        // eight slices !
        assertEquals(8, reader.getGranules(D, true).getCount(new Query(D)));

        // check time, elevation and runtime in metadata
        assertEquals("true", reader.getMetadataValue(D, "HAS_ELEVATION_DOMAIN"));
        assertEquals("true", reader.getMetadataValue(D, "HAS_TIME_DOMAIN"));
        assertEquals("true", reader.getMetadataValue(D, "HAS_RUNTIME_DOMAIN"));
        assertEquals("java.util.Date", reader.getMetadataValue(D, "RUNTIME_DOMAIN_DATATYPE"));
        assertEquals("0.0/0.0,1.0/1.0", reader.getMetadataValue(D, "ELEVATION_DOMAIN"));
        assertEquals(
                "2012-04-01T00:00:00.000Z/2012-04-01T00:00:00.000Z,2012-04-01T01:00:00.000Z/2012-04-01T01:00:00.000Z",
                reader.getMetadataValue(D, "TIME_DOMAIN"));
        assertEquals(
                "2012-04-01T02:00:00.000Z,2012-04-01T03:00:00.000Z",
                reader.getMetadataValue(D, "RUNTIME_DOMAIN"));

        // parameter descriptor for runtime
        Set<ParameterDescriptor<List>> pd = reader.getDynamicParameters(D);
        assertEquals(1, pd.size());

        final ParameterDescriptor<List> runtime = pd.iterator().next();
        assertTrue("runtime".equalsIgnoreCase(runtime.getName().getCode()));

        // check requesting data
        ParameterValue<List> timeValue = ImageMosaicFormat.TIME.createValue();
        ParameterValue<List> zValue = ImageMosaicFormat.ELEVATION.createValue();
        ParameterValue<List> runtimeValue = runtime.createValue();

        // z = 0, time = 0, runtime = 2;
        zValue.setValue(Collections.singletonList(0.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 00:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));

        checkFoursome(
                reader, new GeneralParameterValue[] {zValue, timeValue, runtimeValue}, 0, 1, 2, 3);

        // z = 1, time = 0, runtime = 2;
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 00:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));

        checkFoursome(
                reader, new GeneralParameterValue[] {zValue, timeValue, runtimeValue}, 4, 5, 6, 7);

        // z = 0, time = 1, runtime = 2;
        zValue.setValue(Collections.singletonList(0.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 01:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));

        checkFoursome(
                reader,
                new GeneralParameterValue[] {zValue, timeValue, runtimeValue},
                8,
                9,
                10,
                11);

        // z = 1, time = 1, runtime = 2;
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 01:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));

        checkFoursome(
                reader,
                new GeneralParameterValue[] {zValue, timeValue, runtimeValue},
                12,
                13,
                14,
                15);

        // z = 0, time = 0, runtime = 3;
        zValue.setValue(Collections.singletonList(0.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 00:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));

        checkFoursome(
                reader,
                new GeneralParameterValue[] {zValue, timeValue, runtimeValue},
                16,
                17,
                18,
                19);

        // z = 1, time = 0, runtime = 3;
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 00:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));

        checkFoursome(
                reader,
                new GeneralParameterValue[] {zValue, timeValue, runtimeValue},
                20,
                21,
                22,
                23);

        // z = 0, time = 1, runtime = 3;
        zValue.setValue(Collections.singletonList(0.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 01:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));

        checkFoursome(
                reader,
                new GeneralParameterValue[] {zValue, timeValue, runtimeValue},
                24,
                25,
                26,
                27);

        // z = 1, time = 1, runtime = 3;
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 01:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));

        checkFoursome(
                reader,
                new GeneralParameterValue[] {zValue, timeValue, runtimeValue},
                28,
                29,
                30,
                31);
        reader.dispose();
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void test2DTime() throws Exception {
        NetCDFFormat format = new NetCDFFormat();
        File file = TestData.file(this, "twodimtime/twodimtime.ncml");

        NetCDFReader reader = (NetCDFReader) format.getReader(file);

        // eight slices !
        assertEquals(8, reader.getGranules(D, true).getCount(new Query(D)));

        // check time, elevation and runtime in metadata
        assertEquals("true", reader.getMetadataValue(D, "HAS_ELEVATION_DOMAIN"));
        assertEquals("true", reader.getMetadataValue(D, "HAS_TIME_DOMAIN"));
        assertEquals("true", reader.getMetadataValue(D, "HAS_RUNTIME_DOMAIN"));
        assertEquals("java.util.Date", reader.getMetadataValue(D, "RUNTIME_DOMAIN_DATATYPE"));
        assertEquals("0.0/0.0,1.0/1.0", reader.getMetadataValue(D, "ELEVATION_DOMAIN"));
        assertEquals(
                "2012-04-01T00:00:00.000Z/2012-04-01T00:00:00.000Z,"
                        + "2012-04-01T01:00:00.000Z/2012-04-01T01:00:00.000Z,"
                        + "2012-04-01T02:00:00.000Z/2012-04-01T02:00:00.000Z",
                reader.getMetadataValue(D, "TIME_DOMAIN"));
        assertEquals(
                "2012-04-01T02:00:00.000Z,2012-04-01T03:00:00.000Z",
                reader.getMetadataValue(D, "RUNTIME_DOMAIN"));

        // parameter descriptor for runtime
        Set<ParameterDescriptor<List>> pd = reader.getDynamicParameters(D);
        assertEquals(1, pd.size());

        final ParameterDescriptor<List> runtime = pd.iterator().next();
        assertTrue("runtime".equalsIgnoreCase(runtime.getName().getCode()));

        // check requesting data
        ParameterValue<List> timeValue = ImageMosaicFormat.TIME.createValue();
        ParameterValue<List> zValue = ImageMosaicFormat.ELEVATION.createValue();
        ParameterValue<List> runtimeValue = runtime.createValue();

        // z = 0, time = 0, runtime = 2;
        zValue.setValue(Collections.singletonList(0.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 00:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));

        checkFoursome(
                reader, new GeneralParameterValue[] {zValue, timeValue, runtimeValue}, 0, 1, 2, 3);

        // z = 1, time = 0, runtime = 2;
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 00:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));

        checkFoursome(
                reader, new GeneralParameterValue[] {zValue, timeValue, runtimeValue}, 4, 5, 6, 7);

        // z = 0, time = 1, runtime = 2;
        zValue.setValue(Collections.singletonList(0.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 01:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));

        checkFoursome(
                reader,
                new GeneralParameterValue[] {zValue, timeValue, runtimeValue},
                8,
                9,
                10,
                11);

        // z = 1, time = 1, runtime = 2;
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 01:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));

        checkFoursome(
                reader,
                new GeneralParameterValue[] {zValue, timeValue, runtimeValue},
                12,
                13,
                14,
                15);

        // z = 0, time = 2, runtime = 2;
        zValue.setValue(Collections.singletonList(0.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));

        checkNull(reader, new GeneralParameterValue[] {zValue, timeValue, runtimeValue});

        // z = 1, time = 2, runtime = 2;
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));

        checkNull(reader, new GeneralParameterValue[] {zValue, timeValue, runtimeValue});

        // z = 0, time = 0, runtime = 3;
        zValue.setValue(Collections.singletonList(0.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 00:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));

        checkNull(reader, new GeneralParameterValue[] {zValue, timeValue, runtimeValue});

        // z = 1, time = 0, runtime = 3;
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 00:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));

        checkNull(reader, new GeneralParameterValue[] {zValue, timeValue, runtimeValue});

        // z = 0, time = 1, runtime = 3;
        zValue.setValue(Collections.singletonList(0.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 01:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));

        checkFoursome(
                reader,
                new GeneralParameterValue[] {zValue, timeValue, runtimeValue},
                16,
                17,
                18,
                19);

        // z = 1, time = 1, runtime = 3;
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 01:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));

        checkFoursome(
                reader,
                new GeneralParameterValue[] {zValue, timeValue, runtimeValue},
                20,
                21,
                22,
                23);

        // z = 0, time = 2, runtime = 3;
        zValue.setValue(Collections.singletonList(0.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));

        checkFoursome(
                reader,
                new GeneralParameterValue[] {zValue, timeValue, runtimeValue},
                24,
                25,
                26,
                27);

        // z = 1, time = 2, runtime = 3;
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));

        checkFoursome(
                reader,
                new GeneralParameterValue[] {zValue, timeValue, runtimeValue},
                28,
                29,
                30,
                31);

        // test max properties
        System.setProperty(NetCDFUtilities.PARAMS_MAX_KEY, "runtime, time");
        System.setProperty(NetCDFUtilities.PARAMS_MIN_KEY, "");
        NetCDFUtilities.refreshParameterBehaviors();

        // z = 1, time = 0, runtime = MAX (2);
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 00:00:00")));

        checkFoursome(reader, new GeneralParameterValue[] {zValue, timeValue}, 4, 5, 6, 7);

        // z = 1, time = 1, runtime = MAX (3);
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 01:00:00")));

        checkFoursome(reader, new GeneralParameterValue[] {zValue, timeValue}, 20, 21, 22, 23);

        // z = 1, time = MAX (2), runtime = 3;
        zValue.setValue(Collections.singletonList(1.0));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));

        checkFoursome(reader, new GeneralParameterValue[] {zValue, runtimeValue}, 28, 29, 30, 31);

        // test min properties
        System.setProperty(NetCDFUtilities.PARAMS_MAX_KEY, "");
        System.setProperty(NetCDFUtilities.PARAMS_MIN_KEY, "runtime, time");
        NetCDFUtilities.refreshParameterBehaviors();

        // z = 1, time = 1, runtime = MIN (2);
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 01:00:00")));

        checkFoursome(reader, new GeneralParameterValue[] {zValue, timeValue}, 12, 13, 14, 15);

        // z = 1, time = MIN(1), runtime = 3;
        zValue.setValue(Collections.singletonList(1.0));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));

        checkFoursome(reader, new GeneralParameterValue[] {zValue, runtimeValue}, 20, 21, 22, 23);

        System.setProperty(NetCDFUtilities.PARAMS_MAX_KEY, "");
        System.setProperty(NetCDFUtilities.PARAMS_MIN_KEY, "");
        reader.dispose();
    }

    private void checkFoursome(
            NetCDFReader reader,
            GeneralParameterValue[] pams,
            double a,
            double b,
            double c,
            double d)
            throws IllegalArgumentException, IOException {
        GridCoverage2D cov = reader.read(D, pams);

        assertEquals(a, ((double[]) cov.evaluate(new DirectPosition2D(-109, 41)))[0], 0.0);
        assertEquals(b, ((double[]) cov.evaluate(new DirectPosition2D(-107, 41)))[0], 0.0);
        assertEquals(c, ((double[]) cov.evaluate(new DirectPosition2D(-109, 40)))[0], 0.0);
        assertEquals(d, ((double[]) cov.evaluate(new DirectPosition2D(-107, 40)))[0], 0.0);
    }

    private void checkNull(NetCDFReader reader, GeneralParameterValue[] pams)
            throws IllegalArgumentException, IOException {
        GridCoverage2D cov = reader.read(D, pams);
        assertNull(cov);
    }

    /** @throws Exception */
    public void test2DTime_DiffSize(File file) throws Exception {

        NetCDFImageReaderSpi readerSpi = new NetCDFImageReaderSpi();
        assertTrue(readerSpi.canDecodeInput(file));

        NetCDFImageReader imageReader = (NetCDFImageReader) readerSpi.createReaderInstance();
        imageReader.setInput(file);

        // check max. dimension is used
        assertEquals(2, imageReader.getVariableByName("time").getDimensions().size());
        assertEquals(2, imageReader.getVariableByName("time").getDimension(0).getLength());
        assertEquals(3, imageReader.getVariableByName("time").getDimension(1).getLength());

        NetCDFFormat format = new NetCDFFormat();
        NetCDFReader reader = (NetCDFReader) format.getReader(file);

        // ten slices !
        assertEquals(10, reader.getGranules(D, true).getCount(new Query(D)));

        // check time, elevation and runtime in metadata
        assertEquals("true", reader.getMetadataValue(D, "HAS_ELEVATION_DOMAIN"));
        assertEquals("true", reader.getMetadataValue(D, "HAS_TIME_DOMAIN"));
        assertEquals("true", reader.getMetadataValue(D, "HAS_RUN_DOMAIN"));
        assertEquals("java.util.Date", reader.getMetadataValue(D, "RUN_DOMAIN_DATATYPE"));
        assertEquals("0.0/0.0,1.0/1.0", reader.getMetadataValue(D, "ELEVATION_DOMAIN"));
        assertEquals(
                "2012-04-01T00:00:00.000Z/2012-04-01T00:00:00.000Z,"
                        + "2012-04-01T01:00:00.000Z/2012-04-01T01:00:00.000Z,"
                        + "2012-04-01T02:00:00.000Z/2012-04-01T02:00:00.000Z,"
                        + "2012-04-01T03:00:00.000Z/2012-04-01T03:00:00.000Z",
                reader.getMetadataValue(D, "TIME_DOMAIN"));
        assertEquals(
                "2012-04-01T02:00:00.000Z,2012-04-01T03:00:00.000Z",
                reader.getMetadataValue(D, "RUN_DOMAIN"));

        // parameter descriptor for runtime
        Set<ParameterDescriptor<List>> pd = reader.getDynamicParameters(D);
        assertEquals(1, pd.size());

        final ParameterDescriptor<List> runtime = pd.iterator().next();
        assertTrue("run".equalsIgnoreCase(runtime.getName().getCode()));

        // check requesting data
        ParameterValue<List> timeValue = ImageMosaicFormat.TIME.createValue();
        ParameterValue<List> zValue = ImageMosaicFormat.ELEVATION.createValue();
        ParameterValue<List> runtimeValue = runtime.createValue();

        // z = 0, time = 0, runtime = 2;
        zValue.setValue(Collections.singletonList(0.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 00:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));

        checkFoursome(
                reader, new GeneralParameterValue[] {zValue, timeValue, runtimeValue}, 0, 1, 2, 3);

        // z = 1, time = 0, runtime = 2;
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 00:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));

        checkFoursome(
                reader, new GeneralParameterValue[] {zValue, timeValue, runtimeValue}, 4, 5, 6, 7);

        // z = 0, time = 1, runtime = 2;
        zValue.setValue(Collections.singletonList(0.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 01:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));

        checkFoursome(
                reader,
                new GeneralParameterValue[] {zValue, timeValue, runtimeValue},
                8,
                9,
                10,
                11);

        // z = 1, time = 1, runtime = 2;
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 01:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));

        checkFoursome(
                reader,
                new GeneralParameterValue[] {zValue, timeValue, runtimeValue},
                12,
                13,
                14,
                15);

        // z = 0, time = 2, runtime = 2;
        zValue.setValue(Collections.singletonList(0.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));

        checkNull(reader, new GeneralParameterValue[] {zValue, timeValue, runtimeValue});

        // z = 1, time = 2, runtime = 2;
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));

        // z = 0, time = 3, runtime = 2;
        zValue.setValue(Collections.singletonList(0.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));

        checkNull(reader, new GeneralParameterValue[] {zValue, timeValue, runtimeValue});

        // z = 1, time = 3, runtime = 2;
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));

        checkNull(reader, new GeneralParameterValue[] {zValue, timeValue, runtimeValue});

        // z = 0, time = 0, runtime = 3;
        zValue.setValue(Collections.singletonList(0.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 00:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));

        checkNull(reader, new GeneralParameterValue[] {zValue, timeValue, runtimeValue});

        // z = 1, time = 0, runtime = 3;
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 00:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));

        checkNull(reader, new GeneralParameterValue[] {zValue, timeValue, runtimeValue});

        // z = 0, time = 1, runtime = 3;
        zValue.setValue(Collections.singletonList(0.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 01:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));

        checkFoursome(
                reader,
                new GeneralParameterValue[] {zValue, timeValue, runtimeValue},
                16,
                17,
                18,
                19);

        // z = 1, time = 1, runtime = 3;
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 01:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));

        checkFoursome(
                reader,
                new GeneralParameterValue[] {zValue, timeValue, runtimeValue},
                20,
                21,
                22,
                23);

        // z = 0, time = 2, runtime = 3;
        zValue.setValue(Collections.singletonList(0.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));

        checkFoursome(
                reader,
                new GeneralParameterValue[] {zValue, timeValue, runtimeValue},
                24,
                25,
                26,
                27);

        // z = 1, time = 2, runtime = 3;
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 02:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));

        checkFoursome(
                reader,
                new GeneralParameterValue[] {zValue, timeValue, runtimeValue},
                28,
                29,
                30,
                31);

        // z = 0, time = 3, runtime = 3;
        zValue.setValue(Collections.singletonList(0.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));

        checkFoursome(
                reader,
                new GeneralParameterValue[] {zValue, timeValue, runtimeValue},
                32,
                33,
                34,
                35);

        // z = 1, time = 3, runtime = 3;
        zValue.setValue(Collections.singletonList(1.0));
        timeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));
        runtimeValue.setValue(Collections.singletonList(DF.parse("2012-04-01 03:00:00")));

        checkFoursome(
                reader,
                new GeneralParameterValue[] {zValue, timeValue, runtimeValue},
                36,
                37,
                38,
                39);
        reader.dispose();
    }

    /** @throws Exception */
    @Test
    public void test2DTime_FeatureCollection_DiffSize() throws Exception {
        File dir = TestData.file(this, "twodimtime");
        String content =
                FileUtils.readFileToString(new File(dir, "twodimtime_diffsize.fc"), "UTF-8");
        content = content.replaceAll("\\$\\{DIRECTORY\\}", dir.toString());
        File tempFile = tempFolder.newFile("twodimtime_diffsize.fc");
        FileUtils.writeStringToFile(tempFile, content, "UTF-8");
        test2DTime_DiffSize(tempFile);
    }
    /** @throws Exception */
    @Test
    public void test2DTime_Aggregation_DiffSize() throws Exception {
        File dir = TestData.file(this, "twodimtime");
        String content =
                FileUtils.readFileToString(new File(dir, "twodimtime_diffsize.ncml"), "UTF-8");
        content = content.replaceAll("\\$\\{DIRECTORY\\}", dir.toString());
        File tempFile = tempFolder.newFile("twodimtime_diffsize.ncml");
        FileUtils.writeStringToFile(tempFile, content, "UTF-8");
        test2DTime_DiffSize(tempFile);
    }
}
