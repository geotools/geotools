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
package org.geotools.renderer.lite.gridcoverage2d;

import java.awt.Color;
import javax.media.jai.PlanarImage;
import org.geotools.coverage.Category;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.geometry.GeneralBounds;
import org.geotools.util.SimpleInternationalString;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BaseCoverageProcessingNodeTest {

    private BaseCoverageProcessingNode testedObject;

    private BaseCoverageProcessingNode testedObject2;

    @Before
    public void setUp() throws Exception {
        this.testedObject =
                new BaseCoverageProcessingNode(
                        1, SimpleInternationalString.wrap("fake node"), SimpleInternationalString.wrap("fake node")) {
                    @Override
                    protected GridCoverage2D execute() {
                        return CoverageFactoryFinder.getGridCoverageFactory(null)
                                .create(
                                        "name",
                                        PlanarImage.wrapRenderedImage(RasterSymbolizerTest.getSynthetic(Double.NaN)),
                                        new GeneralBounds(new double[] {-90, -180}, new double[] {90, 180}),
                                        new GridSampleDimension[] {
                                            new GridSampleDimension(
                                                    "sd", new Category[] {new Category("", Color.BLACK, 0)}, null)
                                        },
                                        null,
                                        null);
                    }
                };
        this.testedObject2 =
                new BaseCoverageProcessingNode(
                        1, SimpleInternationalString.wrap("fake node"), SimpleInternationalString.wrap("fake node")) {

                    @Override
                    protected GridCoverage2D execute() {
                        return CoverageFactoryFinder.getGridCoverageFactory(null)
                                .create(
                                        "name",
                                        PlanarImage.wrapRenderedImage(RasterSymbolizerTest.getSynthetic(Double.NaN)),
                                        new GeneralBounds(new double[] {-90, -180}, new double[] {90, 180}),
                                        new GridSampleDimension[] {
                                            new GridSampleDimension(
                                                    "sd", new Category[] {new Category("", Color.BLACK, 0)}, null)
                                        },
                                        null,
                                        null);
                    }
                };
    }

    @Test
    public final void execute() {
        // execute
        Assert.assertNotNull(testedObject2.getOutput());
        // do nothing
        Assert.assertNotNull(testedObject2.getOutput());

        // add source clean output
        testedObject2.addSource(testedObject);
        testedObject2.addSink(testedObject);
        // recompute
        Assert.assertNotNull(testedObject2.getOutput());

        // dispose
        testedObject2.dispose(true);
    }

    @Test
    public final void dispose() {

        Assert.assertNotNull(testedObject.getOutput());
        // dispose
        testedObject.dispose(true);
        // do nothing
        testedObject.dispose(true);
        try {
            // trying to get the output from a disposed coverage should throw an
            // error
            testedObject.getOutput();
            Assert.fail();
        } catch (Exception e) {

        }
    }

    @Test
    public final void addSource() {
        // execute
        Assert.assertNotNull(testedObject2.getOutput());
        // do nothing since we have already executed
        Assert.assertNotNull(testedObject2.getOutput());

        // add source clean output but we also create a cycle which kills our
        // small framework
        testedObject2.addSource(testedObject);
        testedObject.addSink(testedObject2);
        try {
            testedObject2.addSink(testedObject);
            Assert.fail();
        } catch (IllegalStateException e) {
            // TODO: handle exception
        }
        // recompute
        Assert.assertNotNull(testedObject2.getOutput());

        // dispose
        testedObject2.dispose(true);
    }
}
