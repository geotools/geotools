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
package org.geotools.referencing;

import java.util.Properties;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/** Tests if the CRS utility class is functioning correctly when using HSQL datastore. */
public class HSQLCRSTest extends AbstractCRSTest {

    @Override
    protected String getFixtureId() {
        return "hsql";
    }

    @Override
    protected Properties createOfflineFixture() {
        return new Properties();
    }

    @Override
    protected boolean supportsED50QuickScan() {
        return true;
    }

    @Test
    public void testMercatorProjectionTolerance()
            throws NoSuchAuthorityCodeException, FactoryException, MismatchedDimensionException,
                    TransformException {
        CoordinateReferenceSystem targetCrs = CRS.decode("EPSG:25831");
        // System.out.println(targetCrs.getDomainOfValidity());
        MathTransform mathTransform =
                CRS.findMathTransform(DefaultGeographicCRS.WGS84, targetCrs, true);
        DirectPosition2D position2D = new DirectPosition2D(DefaultGeographicCRS.WGS84, 0.1, 39);
        DirectPosition2D position2Dres = new DirectPosition2D();
        mathTransform.transform(position2D, position2Dres);
    }
}
