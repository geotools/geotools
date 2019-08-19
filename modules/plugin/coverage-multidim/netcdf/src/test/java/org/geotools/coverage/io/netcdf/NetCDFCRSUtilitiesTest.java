/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.util.List;
import org.geotools.imageio.netcdf.utilities.NetCDFCRSUtilities;
import org.geotools.referencing.crs.DefaultTemporalCRS;
import org.geotools.referencing.crs.DefaultVerticalCRS;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.referencing.crs.TemporalCRS;
import org.opengis.referencing.crs.VerticalCRS;
import ucar.nc2.constants.AxisType;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.NetcdfDataset;

/** Test UnidataTimeUtilities */
public final class NetCDFCRSUtilitiesTest extends Assert {
    @Test
    public void testBuildVerticalCRS() throws IOException {
        final String url = TestData.url(this, "O3-NO2.nc").toExternalForm();

        NetcdfDataset dataset = null;
        try {
            dataset = NetcdfDataset.openDataset(url);
            assertNotNull(dataset);
            final List<CoordinateAxis> cvs = dataset.getCoordinateAxes();
            assertNotNull(cvs);
            assertSame(4, cvs.size());

            CoordinateAxis timeAxis = cvs.get(0);
            assertNotNull(timeAxis);
            assertEquals(AxisType.Time, timeAxis.getAxisType());

            final TemporalCRS temporalCrs = NetCDFCRSUtilities.buildTemporalCrs(timeAxis);
            assertNotNull(temporalCrs);
            assertTrue(temporalCrs instanceof DefaultTemporalCRS);

            CoordinateAxis verticalAxis = cvs.get(1);
            assertNotNull(verticalAxis);
            assertEquals(AxisType.Height, verticalAxis.getAxisType());

            final VerticalCRS verticalCrs = NetCDFCRSUtilities.buildVerticalCrs(verticalAxis);
            assertNotNull(verticalCrs);
            assertTrue(verticalCrs instanceof DefaultVerticalCRS);
        } finally {
            if (dataset != null) {
                dataset.close();
            }
        }
    }
}
