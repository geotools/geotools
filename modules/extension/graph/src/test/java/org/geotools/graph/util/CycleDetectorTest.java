/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.graph.util;

import org.geotools.graph.GraphTestUtil;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.basic.BasicGraphBuilder;
import org.geotools.graph.util.graph.CycleDetector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CycleDetectorTest {
    private GraphBuilder m_builder;

    @Before
    public void setUp() throws Exception {

        m_builder = createBuilder();
    }

    /**
     * Create a graph without a cycle. <br>
     * <br>
     * Expected: 1. containsCycle() returns false
     */
    @Test
    public void test_0() {
        GraphTestUtil.buildNoBifurcations(builder(), 100);

        CycleDetector detector = new CycleDetector(builder().getGraph());
        Assert.assertFalse(detector.containsCycle());
    }

    /**
     * Create a graph that contains a cycle. <br>
     * <br>
     * Expected: 1. containsCycle returns true
     */
    @Test
    public void test_1() {
        GraphTestUtil.buildCircular(builder(), 100);

        CycleDetector detector = new CycleDetector(builder().getGraph());
        Assert.assertTrue(detector.containsCycle());
    }

    protected GraphBuilder createBuilder() {
        return new BasicGraphBuilder();
    }

    protected GraphBuilder builder() {
        return m_builder;
    }
}
