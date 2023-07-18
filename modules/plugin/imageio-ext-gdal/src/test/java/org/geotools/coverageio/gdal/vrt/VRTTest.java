/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverageio.gdal.vrt;

import static org.junit.Assert.assertEquals;

import it.geosolutions.jaiext.range.NoDataContainer;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.DataBuffer;
import java.io.File;
import java.util.Iterator;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverageio.gdal.BaseGDALGridCoverage2DReader;
import org.geotools.coverageio.gdal.GDALTestCase;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.test.TestData;
import org.geotools.util.NumberRange;
import org.geotools.util.factory.Hints;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 * @author Ugo Moschini, GeoSolutions
 *     <p>Testing {@link VRTReader}
 */
public final class VRTTest extends GDALTestCase {

    /** file name of a valid VRT file to be used for tests. */
    private static final String fileName = "n43.dt0.vrt";

    public VRTTest() {
        super("VRT", new VRTFormatFactory());
    }

    @Test
    public void testService() throws NoSuchAuthorityCodeException, FactoryException {
        if (!testingEnabled()) {
            return;
        }

        GridFormatFinder.scanForPlugins();

        Iterator<GridFormatFactorySpi> list = GridFormatFinder.getAvailableFormats().iterator();
        boolean found = false;
        GridFormatFactorySpi fac = null;

        while (list.hasNext()) {
            fac = list.next();

            if (fac instanceof VRTFormatFactory) {
                found = true;

                break;
            }
        }

        Assert.assertTrue("VRTFormatFactory not registered", found);
        Assert.assertTrue("VRTFormatFactory not available", fac.isAvailable());
        Assert.assertNotNull(new VRTFormatFactory().createFormat());
    }

    @Test
    public void test() throws Exception {
        if (!testingEnabled()) {
            return;
        }

        // Preparing an useful layout in case the image is striped.
        final ImageLayout l = new ImageLayout();
        l.setTileGridXOffset(0).setTileGridYOffset(0).setTileHeight(512).setTileWidth(512);

        Hints hints = new Hints();
        hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, l));

        // get a reader
        final File file = TestData.file(this, fileName);
        final BaseGDALGridCoverage2DReader reader = new VRTReader(file, hints);

        // /////////////////////////////////////////////////////////////////////
        //
        // read once
        //
        // /////////////////////////////////////////////////////////////////////
        GridCoverage2D gc = reader.read(null);
        forceDataLoading(gc);

        // check the nodata has been read
        Object property = gc.getProperty(NoDataContainer.GC_NODATA);
        final double NO_DATA_VALUE = -3.27670000000000E+04;
        assertEquals(NO_DATA_VALUE, (Double) property, 1);
        GridSampleDimension sd = gc.getSampleDimension(0);
        assertEquals(1, sd.getCategories().size());
        Category noDataCategory = sd.getCategories().get(0);
        assertEquals("NO_DATA", noDataCategory.getName().toString());
        assertEquals(
                new NumberRange<>(Double.class, NO_DATA_VALUE, NO_DATA_VALUE),
                noDataCategory.getRange());

        // /////////////////////////////////////////////////////////////////////
        //
        // read again with subsampling and crop
        //
        // /////////////////////////////////////////////////////////////////////
        final double cropFactor = 2.0;
        final int oldW = gc.getRenderedImage().getWidth();
        final int oldH = gc.getRenderedImage().getHeight();

        assertEquals(121, oldW);
        assertEquals(121, oldH);
        // check for expected data type
        assertEquals(DataBuffer.TYPE_SHORT, gc.getRenderedImage().getSampleModel().getDataType());

        final Rectangle range = ((GridEnvelope2D) reader.getOriginalGridRange());
        final GeneralEnvelope oldEnvelope = reader.getOriginalEnvelope();

        final GeneralEnvelope cropEnvelope =
                new GeneralEnvelope(
                        new double[] {
                            oldEnvelope.getLowerCorner().getOrdinate(0)
                                    + (oldEnvelope.getSpan(0) / cropFactor),
                            oldEnvelope.getLowerCorner().getOrdinate(1)
                                    + (oldEnvelope.getSpan(1) / cropFactor)
                        },
                        new double[] {
                            oldEnvelope.getUpperCorner().getOrdinate(0),
                            oldEnvelope.getUpperCorner().getOrdinate(1)
                        });
        cropEnvelope.setCoordinateReferenceSystem(reader.getCoordinateReferenceSystem());

        final ParameterValue gg =
                ((AbstractGridFormat) reader.getFormat()).READ_GRIDGEOMETRY2D.createValue();
        gg.setValue(
                new GridGeometry2D(
                        new GridEnvelope2D(
                                new Rectangle(
                                        0,
                                        0,
                                        (int) (range.width / 2.0 / cropFactor),
                                        (int) (range.height / 2.0 / cropFactor))),
                        cropEnvelope));
        gc = reader.read(new GeneralParameterValue[] {gg});
        forceDataLoading(gc);
    }
}
