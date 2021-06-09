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
package org.geotools.data.wfs.online;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import javax.xml.namespace.QName;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.data.wfs.WFSTestData.TestDataType;
import org.junit.Ignore;
import org.junit.Test;
import org.locationtech.jts.geom.Polygon;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;

/** @author Roar Brænden */
public class KartverketStedsnavnDataStoreOnlineTest extends AbstractWfsDataStoreOnlineTest {

    static final String SERVER_URL =
            "https://wfs.geonorge.no/skwms1/wfs.stedsnavn?request=GetCapabilities&service=WFS";

    static final TestDataType KARTVERKET_STEDSNAVN =
            new TestDataType(
                    "KartverketNo",
                    new QName(
                            "http://skjema.geonorge.no/SOSI/produktspesifikasjon/StedsnavnForVanligBruk/20181115",
                            "sted"),
                    "app_Sted",
                    "urn:ogc:def:crs:EPSG::4258");

    static final String defaultGeometryName = "område";

    static final Class<?> geometryType = Polygon.class;

    static final Id fidFilter = ff.id(Collections.singleton(ff.featureId("404676")));

    static final Filter spatialFilter =
            ff.bbox(defaultGeometryName, 68.0, 17.0, 69.0, 18.0, "urn:ogc:def:crs:EPSG::4258");

    public KartverketStedsnavnDataStoreOnlineTest() {
        super(
                SERVER_URL,
                KARTVERKET_STEDSNAVN,
                defaultGeometryName,
                geometryType,
                -1,
                fidFilter,
                spatialFilter,
                WFSDataStoreFactory.AXIS_ORDER_COMPLIANT);
    }

    @Override
    protected void setUpParameters(final Map<String, Serializable> params) {
        super.setUpParameters(params);
        params.put(WFSDataStoreFactory.USE_HTTP_CONNECTION_POOLING.key, "False");
    }

    @Override
    @Test
    @Ignore
    public void testDataStoreHandlesAxisFlipping() {
        // disabled, not implemented for 2.0.0
    }
}
