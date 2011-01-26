/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, David Zwiers
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
package org.geotools.data.wfs.v1_1_0;

import static org.geotools.data.wfs.v1_1_0.DataTestSupport.CUBEWERX_GOVUNITCE;

import com.vividsolutions.jts.geom.Polygon;

public class CubeWerxOnlineTest extends AbstractWfsDataStoreOnlineTest {

    public static final String SERVER_URL = "http://frameworkwfs.usgs.gov/framework/wfs/wfs.cgi?request=GetCapabilities&version=1.1.0"; //$NON-NLS-1$

    /**
     * A GetFeature request to the CubeWerx server does not return the
     * {@code numberOfFeatures} attribute in the FeatureCollection root element,
     * even if the request includes {@code restultType=hits}, so the WFS
     * FeatureSource is expected to return -1 as the number of features is too
     * expensive to calculate if we have to do a full scan
     */
    private static final int EXPECTED_FEATURE_COUNT = -1;

    public CubeWerxOnlineTest() {
        super(SERVER_URL, CUBEWERX_GOVUNITCE, "geometry", Polygon.class, EXPECTED_FEATURE_COUNT, null);
    }
}
