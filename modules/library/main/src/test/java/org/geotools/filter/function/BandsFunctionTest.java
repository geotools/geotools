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
package org.geotools.filter.function;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import org.easymock.EasyMock;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.filter.expression.Function;

public class BandsFunctionTest {

    @Test
    public void testRenderedImage() throws Exception {
        Function bands = CommonFactoryFinder.getFilterFactory2().function("bands");
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
        assertEquals(4, (int) bands.evaluate(image, Integer.class));
    }

    @Test
    public void testCoverage() throws Exception {
        Function bands = CommonFactoryFinder.getFilterFactory2().function("bands");
        GridCoverage coverage = EasyMock.createNiceMock(GridCoverage.class);
        EasyMock.expect(coverage.getNumSampleDimensions()).andReturn(5);
        EasyMock.replay(coverage);
        assertEquals(5, (int) bands.evaluate(coverage, Integer.class));
    }
}
