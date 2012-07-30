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
package org.geotools.data.wfs.internal.v1_1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.ResultTypeType;

import org.geotools.data.Query;
import org.geotools.data.wfs.impl.WFSTestData;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.GetFeatureRequest.ResultType;
import org.geotools.data.wfs.internal.RequestComponents;
import org.geotools.data.wfs.internal.v1_x.CubeWerxStrategy;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.Capabilities;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.Intersects;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * Unit test suite for {@link CubeWerxStrategy}
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @version $Id$
 * @since 2.6.x
 * 
 * 
 * 
 * @source $URL$
 */
public class CubeWerxStrategyTest extends WFSTestData {

    private static CubeWerxStrategy strategy;

    @Before
    public void setUpBeforeClass() throws Exception {
        strategy = createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES, new CubeWerxStrategy());
        strategy = spy(strategy);
    }

    @Test
    public void testCreateGetFeatureRequest() throws IOException {
        GetFeatureRequest query = new GetFeatureQueryAdapter(
                new Query(CUBEWERX_GOVUNITCE.FEATURETYPENAME), "GML2", "EPSG:4326",
                ResultType.RESULTS);

        RequestComponents getFeatureRequest = strategy.buildGetFeatureRequest(query);

        GetFeatureType serverRequest = getFeatureRequest.getServerRequest();
        ResultTypeType resultType = serverRequest.getResultType();
        assertNull(resultType);
        Map<String, String> kvpParameters = getFeatureRequest.getKvpParameters();
        assertNull(kvpParameters.get("RESULTTYPE"));
    }

    /**
     * Ensures a single spatial filter is picked up as supported if anded spatial filters are
     * supplied
     */
    @Test
    public void testSplitFiltersAnd() {

        Capabilities caps = new Capabilities();
        caps.addAll(Capabilities.LOGICAL);
        caps.addAll(Capabilities.SIMPLE_COMPARISONS);
        caps.addName(Intersects.NAME);
        caps.addName(BBOX.NAME);
        caps.addName(Crosses.NAME);
        when(strategy.getFilterCapabilities()).thenReturn(caps.getContents());

        final Geometry geom = new GeometryFactory().createPoint(new Coordinate(0, 0));
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Filter intersects = ff.intersects(ff.property("geom"), ff.literal(geom));
        Filter bbox = ff.bbox(ff.property("geom"), 0, 0, 1, 1, "EPSG:4326");
        Filter crosses = ff.crosses(ff.property("geom"), ff.literal(geom));
        Filter disjoint = ff.disjoint(ff.property("geom"), ff.literal(geom));
        Filter nonSpatial = ff.equals(ff.property("name"), ff.literal("test"));

        Filter filter = ff.and(Arrays.asList(new Filter[] { intersects, bbox, crosses, disjoint,
                nonSpatial }));
        Filter[] splitted = strategy.splitFilters(filter);
        assertNotNull(splitted);
        assertEquals(2, splitted.length);

        Filter supported = splitted[0];
        Filter unsupported = splitted[1];

        assertTrue(supported instanceof And);
        assertTrue(unsupported instanceof And);

        // only the first anded spatial filter was added as supported?
        And supportedAnd = ff.and(Arrays.asList(new Filter[] { intersects, nonSpatial }));
        And unsupportedAnd = ff.and(Arrays.asList(new Filter[] { bbox, crosses, disjoint }));

        assertEquals(supportedAnd, supported);
        assertEquals(unsupportedAnd, unsupported);
    }

    @Test
    public void testSplitFiltersOr() {

        Capabilities caps = new Capabilities();
        caps.addAll(Capabilities.LOGICAL);
        caps.addAll(Capabilities.SIMPLE_COMPARISONS);
        caps.addName(Intersects.NAME);
        caps.addName(BBOX.NAME);
        caps.addName(Crosses.NAME);
        when(strategy.getFilterCapabilities()).thenReturn(caps.getContents());

        final Geometry geom = new GeometryFactory().createPoint(new Coordinate(0, 0));
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Filter intersects = ff.intersects(ff.property("geom"), ff.literal(geom));
        Filter bbox = ff.bbox(ff.property("geom"), 0, 0, 1, 1, "EPSG:4326");

        // if the filter is an Or we can't know
        Filter filter = ff.or(intersects, bbox);
        Filter[] splitted = strategy.splitFilters(filter);
        Filter supported = splitted[0];
        Filter unsupported = splitted[1];
        assertEquals(Filter.INCLUDE, supported);
        assertEquals(filter, unsupported);
    }
}
