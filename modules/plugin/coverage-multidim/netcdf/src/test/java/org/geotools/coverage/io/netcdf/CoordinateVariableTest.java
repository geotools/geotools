/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import org.apache.commons.io.FileUtils;
import org.geotools.imageio.netcdf.cv.ClimatologicalTimeCoordinateVariable;
import org.geotools.imageio.netcdf.cv.ClimatologicalTimeHandlerSpi.ClimatologicalTimeHandler;
import org.geotools.imageio.netcdf.cv.CoordinateHandlerFinder;
import org.geotools.imageio.netcdf.cv.CoordinateHandlerSpi.CoordinateHandler;
import org.geotools.imageio.netcdf.cv.CoordinateVariable;
import org.geotools.imageio.netcdf.utilities.NetCDFTimeUtilities;
import org.geotools.test.TestData;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.TemporalCRS;
import org.opengis.referencing.crs.VerticalCRS;
import ucar.nc2.Dimension;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.CoordinateAxis1D;
import ucar.nc2.dataset.NetcdfDataset;

/** @author Simone Giannecchini, GeoSolutions SAS */
public class CoordinateVariableTest extends Assert {

    @BeforeClass
    public static void init() {
        System.setProperty("user.timezone", "GMT");
        System.setProperty("netcdf.coordinates.enablePlugins", "true");
    }

    @AfterClass
    public static void close() {
        System.clearProperty("user.timezone");
    }

    /** Simple CoordinateAxis1D wrapper to override Units */
    class CoordinateAxis1DUnitWrapper extends CoordinateAxis1D {

        String units;

        @Override
        public String getUnitsString() {
            return units;
        }

        CoordinateAxis1DUnitWrapper(NetcdfDataset ncd, CoordinateAxis1D axis, String units) {
            super(ncd, axis);
            this.units = units;
        }
    }

    @Test
    public void timeUnitsTest() throws Exception {

        final NetcdfDataset dataset =
                NetcdfDataset.openDataset(TestData.url(this, "O3-NO2.nc").toExternalForm());
        Dimension dim = dataset.findDimension("time");

        // check type
        CoordinateAxis coordinateAxis = dataset.findCoordinateAxis(dim.getShortName());

        final int year = 2012;
        final int month = 4;
        final int day = 1;
        final int hour = 0;
        final int minute = 0;
        final int second = 0;
        final String refTime =
                year
                        + "-"
                        + (month < 10 ? ("0" + month) : month)
                        + "-"
                        + (day < 10 ? ("0" + day) : day)
                        + " "
                        + (hour < 10 ? ("0" + hour) : hour)
                        + ":"
                        + (minute < 10 ? ("0" + minute) : minute)
                        + ":"
                        + (second < 10 ? ("0" + second) : second);
        // //
        // Plural form units check
        // //
        checkTimes(dataset, coordinateAxis, refTime, year, month, day, hour, minute, second, false);

        // //
        // Singular form units check
        // //
        checkTimes(dataset, coordinateAxis, refTime, year, month, day, hour, minute, second, true);

        dataset.close();
    }

    private void checkTimes(
            NetcdfDataset dataset,
            CoordinateAxis coordinateAxis,
            String refTime,
            int year,
            int month,
            int day,
            int hour,
            int minute,
            int second,
            boolean singularForm)
            throws IOException {

        String units = "";
        CoordinateVariable<?> cv = null;
        GregorianCalendar cal = null;

        String unitsOriginSuffix =
                (singularForm ? "" : "s") // day vs days, hour vs hours, and so on
                        + " since "
                        + refTime;

        // MONTHS
        units = "month" + unitsOriginSuffix;
        cv = getCoordinateVariable(dataset, coordinateAxis, units);
        cal = getCalendar(year, month, day, hour, minute, second, GregorianCalendar.MONTH);
        assertEquals(cal.getTime(), cv.getMaximum());

        // DAYS
        units = "day" + unitsOriginSuffix;
        cv = getCoordinateVariable(dataset, coordinateAxis, units);
        cal = getCalendar(year, month, day, hour, minute, second, GregorianCalendar.DAY_OF_MONTH);
        assertEquals(cal.getTime(), cv.getMaximum());

        // HOURS
        units = "hour" + unitsOriginSuffix;
        cv = getCoordinateVariable(dataset, coordinateAxis, units);
        cal = getCalendar(year, month, day, hour, minute, second, GregorianCalendar.HOUR_OF_DAY);
        assertEquals(cal.getTime(), cv.getMaximum());

        // MINUTES
        units = "minute" + unitsOriginSuffix;
        cv = getCoordinateVariable(dataset, coordinateAxis, units);
        cal = getCalendar(year, month, day, hour, minute, second, GregorianCalendar.MINUTE);
        assertEquals(cal.getTime(), cv.getMaximum());

        // SECONDS
        units = "second" + unitsOriginSuffix;
        cv = getCoordinateVariable(dataset, coordinateAxis, units);
        cal = getCalendar(year, month, day, hour, minute, second, GregorianCalendar.SECOND);
        assertEquals(cal.getTime(), cv.getMaximum());
    }

    private GregorianCalendar getCalendar(
            int year, int month, int day, int hour, int minute, int second, int unit) {
        GregorianCalendar cal = new GregorianCalendar(NetCDFTimeUtilities.UTC_TIMEZONE);

        // Months are zero based
        if (unit == GregorianCalendar.YEAR) {
            year++;
        } else if (unit == GregorianCalendar.MONTH) {
            month++;
        } else if (unit == GregorianCalendar.DAY_OF_MONTH) {
            day++;
        } else if (unit == GregorianCalendar.HOUR_OF_DAY) {
            hour++;
        } else if (unit == GregorianCalendar.MINUTE) {
            minute++;
        } else if (unit == GregorianCalendar.SECOND) {
            second++;
        }
        month--;

        cal.set(year, month, day, hour, minute, second);
        cal.set(GregorianCalendar.MILLISECOND, 0);
        return cal;
    }

    private CoordinateVariable<?> getCoordinateVariable(
            NetcdfDataset dataset, CoordinateAxis coordinateAxis, String units) {
        CoordinateAxis1DUnitWrapper wrapper =
                new CoordinateAxis1DUnitWrapper(dataset, (CoordinateAxis1D) coordinateAxis, units);
        return CoordinateVariable.create((CoordinateAxis1D) wrapper);
    }

    @Test
    public void polyphemus() throws Exception {

        // acquire dataset
        final NetcdfDataset dataset =
                NetcdfDataset.openDataset(TestData.url(this, "O3-NO2.nc").toExternalForm());
        assertNotNull(dataset);
        final List<CoordinateAxis> cvs = dataset.getCoordinateAxes();
        assertNotNull(cvs);
        assertSame(4, cvs.size());

        //
        // cloud_formations is short
        //
        Dimension dim = dataset.findDimension("time");
        assertNotNull(dim);
        assertEquals("time", dim.getShortName());

        // check type
        CoordinateAxis coordinateAxis = dataset.findCoordinateAxis(dim.getShortName());
        assertNotNull(coordinateAxis);
        assertTrue(coordinateAxis instanceof CoordinateAxis1D);
        Class<?> binding = CoordinateVariable.suggestBinding((CoordinateAxis1D) coordinateAxis);
        assertNotNull(binding);
        assertSame(Date.class, binding);
        CoordinateVariable<?> cv = CoordinateVariable.create((CoordinateAxis1D) coordinateAxis);
        assertSame(Date.class, cv.getType());

        List<?> list = cv.read();
        assertNotNull(list);
        assertEquals(2, list.size());

        final GregorianCalendar cal = new GregorianCalendar(NetCDFTimeUtilities.UTC_TIMEZONE);
        cal.set(2012, 3, 1, 0, 0, 0);
        cal.set(GregorianCalendar.MILLISECOND, 0);
        assertEquals(cal.getTime(), cv.getMinimum());
        assertEquals(list.get(0), cv.getMinimum());

        cal.set(2012, 3, 1, 1, 0, 0);
        assertEquals(cal.getTime(), cv.getMaximum());
        assertEquals(list.get(1), cv.getMaximum());
        assertEquals(2, cv.getSize());
        assertEquals("hours since 2012-04-01 0:00:00", cv.getUnit());
        CoordinateReferenceSystem crs = cv.getCoordinateReferenceSystem();
        assertNotNull(crs);
        assertTrue(crs instanceof TemporalCRS);
        //
        // lat is float
        //
        dim = dataset.findDimension("z");
        assertNotNull(dim);
        assertEquals("z", dim.getShortName());

        // check type
        coordinateAxis = dataset.findCoordinateAxis(dim.getShortName());
        assertNotNull(coordinateAxis);
        assertTrue(coordinateAxis instanceof CoordinateAxis1D);
        binding = CoordinateVariable.suggestBinding((CoordinateAxis1D) coordinateAxis);
        assertNotNull(binding);
        assertSame(Float.class, binding);

        cv = CoordinateVariable.create((CoordinateAxis1D) coordinateAxis);
        list = cv.read();
        assertNotNull(list);
        assertEquals(2, list.size());

        assertSame(Float.class, cv.getType());
        assertEquals(10f, cv.getMinimum());
        assertEquals(450f, cv.getMaximum());
        assertEquals(list.get(0), cv.getMinimum());
        assertEquals(list.get(1), cv.getMaximum());
        assertEquals(2, cv.getSize());
        assertEquals("meters", cv.getUnit());

        crs = cv.getCoordinateReferenceSystem();
        assertNotNull(crs);
        assertTrue(crs instanceof VerticalCRS);

        dataset.close();
    }

    @Test
    @Ignore
    public void testIASI() throws Exception {
        //
        // IASI does not have time or runtime, it only contains double variables besides lat e long
        //

        // acquire dataset
        final NetcdfDataset dataset =
                NetcdfDataset.openDataset(
                        TestData.url(this, "IASI_C_EUMP_20121120062959_31590_eps_o_l2.nc")
                                .toExternalForm());
        assertNotNull(dataset);
        final List<CoordinateAxis> cvs = dataset.getCoordinateAxes();
        assertNotNull(cvs);
        assertSame(8, cvs.size());

        //
        // cloud_formations is short
        //
        Dimension dim = dataset.findDimension("cloud_formations");
        assertNotNull(dim);
        assertEquals("cloud_formations", dim.getShortName());

        // check type
        CoordinateAxis coordinateAxis = dataset.findCoordinateAxis(dim.getShortName());
        assertNotNull(coordinateAxis);
        assertTrue(coordinateAxis instanceof CoordinateAxis1D);
        Class<?> binding = CoordinateVariable.suggestBinding((CoordinateAxis1D) coordinateAxis);
        assertNotNull(binding);
        assertSame(Short.class, binding);
        CoordinateVariable<?> cv = CoordinateVariable.create((CoordinateAxis1D) coordinateAxis);
        assertSame(Short.class, cv.getType());
        assertEquals((short) 0, cv.getMinimum());
        assertEquals((short) 2, cv.getMaximum());
        assertEquals(3, cv.getSize());
        assertEquals("level", cv.getUnit());

        //
        // lat is float
        //
        dim = dataset.findDimension("lat");
        assertNotNull(dim);
        assertEquals("lat", dim.getShortName());

        // check type
        coordinateAxis = dataset.findCoordinateAxis(dim.getShortName());
        assertNotNull(coordinateAxis);
        assertTrue(coordinateAxis instanceof CoordinateAxis1D);
        binding = CoordinateVariable.suggestBinding((CoordinateAxis1D) coordinateAxis);
        assertNotNull(binding);
        assertSame(Float.class, binding);
        cv = CoordinateVariable.create((CoordinateAxis1D) coordinateAxis);
        assertNotNull(cv);
        assertSame(Float.class, cv.getType());
        assertEquals(-77.327934f, cv.getMinimum());
        assertEquals(89.781555f, cv.getMaximum());
        assertEquals(766, cv.getSize());
        assertEquals("degrees_north", cv.getUnit());
        assertTrue(cv.isRegular());
        assertEquals(cv.getMinimum(), cv.getStart());
        assertEquals(0.2184437770469516, cv.getIncrement());

        //
        // lon is float
        //
        dim = dataset.findDimension("lon");
        assertNotNull(dim);
        assertEquals("lon", dim.getShortName());

        // check type
        coordinateAxis = dataset.findCoordinateAxis(dim.getShortName());
        assertNotNull(coordinateAxis);
        assertTrue(coordinateAxis instanceof CoordinateAxis1D);
        binding = CoordinateVariable.suggestBinding((CoordinateAxis1D) coordinateAxis);
        assertNotNull(binding);
        assertSame(Float.class, binding);
        cv = CoordinateVariable.create((CoordinateAxis1D) coordinateAxis);
        assertNotNull(cv);
        assertEquals("degrees_east", cv.getUnit());

        //
        // pressure_levels_ozone is Double
        //
        dim = dataset.findDimension("nlo");
        assertNotNull(dim);
        assertEquals("nlo", dim.getShortName());

        // check type
        coordinateAxis = dataset.findCoordinateAxis(dim.getShortName());
        assertNotNull(coordinateAxis);
        assertTrue(coordinateAxis instanceof CoordinateAxis1D);
        binding = CoordinateVariable.suggestBinding((CoordinateAxis1D) coordinateAxis);
        assertNotNull(binding);
        assertSame(Double.class, binding);
        cv = CoordinateVariable.create((CoordinateAxis1D) coordinateAxis);
        assertNotNull(cv);
        assertEquals("Pa", cv.getUnit());

        //
        // pressure_levels_ozone is Double
        //
        dim = dataset.findDimension("nlt");
        assertNotNull(dim);
        assertEquals("nlt", dim.getShortName());

        // check type
        coordinateAxis = dataset.findCoordinateAxis(dim.getShortName());
        assertNotNull(coordinateAxis);
        assertTrue(coordinateAxis instanceof CoordinateAxis1D);
        binding = CoordinateVariable.suggestBinding((CoordinateAxis1D) coordinateAxis);
        assertNotNull(binding);
        assertSame(Double.class, binding);
        cv = CoordinateVariable.create((CoordinateAxis1D) coordinateAxis);
        assertNotNull(cv);
        assertEquals("Pa", cv.getUnit());

        dataset.close();
    }

    @Test
    public void testClimatologicalTimeVariable() throws MalformedURLException, IOException {
        // Selection of the input file
        final File workDir = new File(TestData.file(this, "."), "climatologicalaxis");
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }

        FileUtils.copyFile(
                TestData.file(this, "climatological.zip"), new File(workDir, "climatological.zip"));
        TestData.unzipFile(this, "climatologicalaxis/climatological.zip");

        final NetcdfDataset dataset =
                NetcdfDataset.openDataset(
                        TestData.url(this, "climatologicalaxis/climatological.nc")
                                .toExternalForm());
        Dimension dim = dataset.findDimension("time");

        CoordinateAxis1D coordinateAxis =
                (CoordinateAxis1D) dataset.findCoordinateAxis(dim.getShortName());
        try {

            CoordinateHandler handler = CoordinateHandlerFinder.findHandler(coordinateAxis);
            assertNotNull(handler);
            assertTrue(handler instanceof ClimatologicalTimeHandler);
            ClimatologicalTimeHandler timeHandler = (ClimatologicalTimeHandler) handler;

            CoordinateVariable<Date> coordinateVariable =
                    timeHandler.createCoordinateVariable(coordinateAxis);
            assertNotNull(coordinateVariable);
            assertTrue(coordinateVariable instanceof ClimatologicalTimeCoordinateVariable);

            ClimatologicalTimeCoordinateVariable timeVariable =
                    (ClimatologicalTimeCoordinateVariable) coordinateVariable;
            CoordinateReferenceSystem crs = timeVariable.getCoordinateReferenceSystem();
            assertTrue(crs instanceof TemporalCRS);

            assertEquals(12, timeVariable.getSize());
            Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
            calendar.set(0, 0, 16, 0, 0, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            assertEquals(calendar.getTimeInMillis(), timeVariable.getMinimum().getTime());
            calendar.set(Calendar.MONTH, 11);
            assertEquals(calendar.getTimeInMillis(), timeVariable.getMaximum().getTime());
            calendar.set(Calendar.MONTH, 2);
            assertEquals(
                    calendar.getTimeInMillis(),
                    timeVariable.read(Collections.singletonMap("time", 2)).getTime());

        } finally {
            dataset.close();
            FileUtils.deleteDirectory(TestData.file(this, "climatologicalaxis"));
        }
    }
}
