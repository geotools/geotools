/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.gtopo30;

import java.awt.Rectangle;
import java.net.URL;
import javax.media.jai.PlanarImage;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.test.TestData;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;

/**
 * Purpose of this class is testing the ability of this plug in to read and write back the in
 * gtopo30 format.
 *
 * @author Simone Giannecchini
 */
public class GT30DecimationTest extends GT30TestBase {
    /** Constructor for GT30ReaderTest. */
    public GT30DecimationTest(String arg0) {
        super(arg0);
    }

    /**
     * Testing reader and writer for gtopo. This test first of all read an existing gtopo tessel
     * into a coverage object, therefore it writes it back onto the disk. Once the coverage is
     * written back\ it loads it again building a new coverage which is finally visualized.
     */
    public void test() throws Exception {

        URL statURL =
                TestData.url(this, (new StringBuffer(this.fileName).append(".DEM").toString()));
        AbstractGridFormat format = (AbstractGridFormat) new GTopo30FormatFactory().createFormat();

        if (format.accepts(statURL)) {

            /** STEP 1 Reading the coverage into memory in order to write it down again */
            // get a reader
            AbstractGridCoverage2DReader reader =
                    (AbstractGridCoverage2DReader) format.getReader(statURL);

            // get a grid coverage
            final ParameterValueGroup params = reader.getFormat().getReadParameters();
            params.parameter(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString())
                    .setValue(
                            new GridGeometry2D(
                                    new GridEnvelope2D(new Rectangle(0, 0, 640, 480)),
                                    reader.getOriginalEnvelope()));
            gc =
                    ((GridCoverage2D)
                            reader.read(
                                    (GeneralParameterValue[])
                                            params.values().toArray(new GeneralParameterValue[1])));
            if (TestData.isInteractiveTest()) {
                //				 logging some info
                logger.info(gc.getCoordinateReferenceSystem2D().toWKT());
                logger.info(gc.toString());
                gc.show();
            } else {
                PlanarImage.wrapRenderedImage(gc.getRenderedImage()).getTiles();
            }
        }
    }

    public static final void main(String[] args) throws Exception {
        junit.textui.TestRunner.run(GT30DecimationTest.class);
    }
}
