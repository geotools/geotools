/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.coverageio.gdal.rpftoc;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.File;
import java.util.Iterator;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverageio.gdal.BaseGDALGridCoverage2DReader;
import org.geotools.coverageio.gdal.GDALTestCase;
import org.geotools.geometry.GeneralBounds;
import org.geotools.test.TestData;
import org.geotools.util.factory.Hints;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Daniele Romagnoli, GeoSolutions SAS
 * @author Simone Giannecchini, GeoSolutions SAS
 *     <p>Testing {@link RPFTOCReader}
 */
public final class RPFTOCTest extends GDALTestCase {
    /** file name of a valid RPFTOC sample data to be used for tests. */
    private static final String fileName = "A.TOC";

    /** Creates a new instance of {@code RPFTOCTest} */
    public RPFTOCTest() {
        super("RPFTOC", new RPFTOCFormatFactory());
    }

    @Test
    public void test() throws Exception {
        // get a reader
        File file = TestData.file(this, fileName);

        // Preparing an useful layout in case the image is striped.
        final ImageLayout l = new ImageLayout();
        l.setTileGridXOffset(0).setTileGridYOffset(0).setTileHeight(512).setTileWidth(512);

        Hints hints = new Hints();
        hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, l));

        final BaseGDALGridCoverage2DReader reader = new RPFTOCReader(file, hints);

        // /////////////////////////////////////////////////////////////////////
        //
        // read once
        //
        // /////////////////////////////////////////////////////////////////////
        GridCoverage2D gc = reader.read();
        forceDataLoading(gc);

        // /////////////////////////////////////////////////////////////////////
        //
        // read again with subsampling and crop
        //
        // /////////////////////////////////////////////////////////////////////
        final double cropFactor = 2.0;
        final Rectangle range = (GridEnvelope2D) reader.getOriginalGridRange();
        final GeneralBounds oldEnvelope = reader.getOriginalEnvelope();
        final GeneralBounds cropEnvelope = new GeneralBounds(
                new double[] {
                    oldEnvelope.getLowerCorner().getOrdinate(0) + oldEnvelope.getSpan(0) / cropFactor,
                    oldEnvelope.getLowerCorner().getOrdinate(1) + oldEnvelope.getSpan(1) / cropFactor
                },
                new double[] {
                    oldEnvelope.getUpperCorner().getOrdinate(0),
                    oldEnvelope.getUpperCorner().getOrdinate(1)
                });
        cropEnvelope.setCoordinateReferenceSystem(reader.getCoordinateReferenceSystem());

        final ParameterValue gg = ((AbstractGridFormat) reader.getFormat()).READ_GRIDGEOMETRY2D.createValue();
        gg.setValue(new GridGeometry2D(
                new GridEnvelope2D(new Rectangle(
                        0, 0, (int) (range.width / 2.0 / cropFactor), (int) (range.height / 2.0 / cropFactor))),
                cropEnvelope));
        gc = reader.read(new GeneralParameterValue[] {gg});
        forceDataLoading(gc);
    }

    @Test
    public void testIsAvailable() throws NoSuchAuthorityCodeException, FactoryException {
        GridFormatFinder.scanForPlugins();

        Iterator list = GridFormatFinder.getAvailableFormats().iterator();
        boolean found = false;
        GridFormatFactorySpi fac = null;

        while (list.hasNext()) {
            fac = (GridFormatFactorySpi) list.next();

            if (fac instanceof RPFTOCFormatFactory) {
                found = true;

                break;
            }
        }

        Assert.assertTrue("RPFTOCFormatFactory not registered", found);
        Assert.assertTrue("RPFTOCFormatFactory not available", fac.isAvailable());
        Assert.assertNotNull(new RPFTOCFormatFactory().createFormat());
    }
}
