/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.tile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.locationtech.jts.geom.Envelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public abstract class ServiceTest {

    public static CoordinateReferenceSystem MERCATOR_CRS;

    private static ReferencedEnvelope DE_EXTENT;

    private static ReferencedEnvelope BR_EXTENT;

    private static ReferencedEnvelope HAWAII_EXTENT;

    private static ReferencedEnvelope NZ_EXTENT;

    private static ReferencedEnvelope TZ_EXTENT;

    protected static final String DE_EXTENT_NAME = "DE_EXTENT_NAME";

    protected static final String BR_EXTENT_NAME = "BR_EXTENT_NAME";

    protected static final String HAWAII_EXTENT_NAME = "HAWAII_EXTENT_NAME_NAME";

    protected static final String TZ_EXTENT_NAME = "TZ_HAWAII_EXTENT_NAME";

    protected static final String NZ_EXTENT_NAME = "NZ_EXTENT_NAME_NAME";

    protected static Map<String, ReferencedEnvelope> extentNameToExtent;

    @BeforeClass
    public static void beforeClass() {
        try {
            MERCATOR_CRS = CRS.decode("EPSG:3857");

        } catch (FactoryException e) {
            e.printStackTrace();
            Assert.fail(e.getLocalizedMessage());
        }

        extentNameToExtent = new HashMap<String, ReferencedEnvelope>();

        DE_EXTENT = new ReferencedEnvelope(new Envelope(6, 15, 47, 55), DefaultGeographicCRS.WGS84);

        extentNameToExtent.put(DE_EXTENT_NAME, DE_EXTENT);

        BR_EXTENT =
                new ReferencedEnvelope(
                        new Envelope(-43.72, -42.93, -23.10, -22.63), DefaultGeographicCRS.WGS84);

        extentNameToExtent.put(BR_EXTENT_NAME, BR_EXTENT);

        HAWAII_EXTENT =
                new ReferencedEnvelope(
                        new Envelope(-160.635967, -154.483623, 18.651309, 22.598660),
                        DefaultGeographicCRS.WGS84);
        extentNameToExtent.put(HAWAII_EXTENT_NAME, HAWAII_EXTENT);

        // hmmm failing near date line
        NZ_EXTENT =
                new ReferencedEnvelope(
                        new Envelope(164.798799, 179.029327, -47.732492, -33.697613),
                        DefaultGeographicCRS.WGS84);
        extentNameToExtent.put(NZ_EXTENT_NAME, NZ_EXTENT);

        TZ_EXTENT =
                new ReferencedEnvelope(
                        new Envelope(143.880831, 149.505830, -43.700251, -40.338803),
                        DefaultGeographicCRS.WGS84);
        extentNameToExtent.put(TZ_EXTENT_NAME, TZ_EXTENT);
    }

    public static final ReferencedEnvelope getExtent(String extentName) {
        return extentNameToExtent.get(extentName);
    }

    public abstract List<String> getUrlList(String extentName);
}
