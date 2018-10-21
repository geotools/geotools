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
 */
package org.geotools.coverage.grid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * Tests the {@link GridCoverage2D} implementation.
 *
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class GridCoverageFactoryTest {

    @Test
    public void testCreateWithoutBands() throws NoSuchAuthorityCodeException, FactoryException {
        String name = "Test Grid Coverage";
        // Create a raster with some data in it
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = image.getRaster();
        int[] pixel = {0};
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                pixel[0] = x * y;
                raster.setPixel(x, y, pixel);
            }
        }

        // Firstly, test the create method using an Envelope
        GeneralEnvelope env = new GeneralEnvelope(new Rectangle2D.Double(10, 10, 10, 10));
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
        env.setCoordinateReferenceSystem(crs);

        GridCoverage2D coverage = null;
        try {
            coverage = new GridCoverageFactory().create(name, raster, env, null);
        } catch (NullPointerException e) {
            fail(
                    "NullPointerException was thrown despite the javadocs indicating that 'bands' is nullable");
        }

        // And check that the GridCoverage actually contains data that matches what we put into it.
        int[] dest = new int[1];
        coverage.evaluate(new Point2D.Double(15, 14), dest);

        assertEquals(20, dest[0]);

        // Secondly, test the create method using a MathTransform
        AffineTransform tx = AffineTransform.getTranslateInstance(10, 10);
        MathTransform mathTx = ProjectiveTransform.create(new GeneralMatrix(tx));

        try {
            coverage = new GridCoverageFactory().create(name, raster, crs, mathTx, null);
        } catch (NullPointerException e) {
            fail(
                    "NullPointerException was thrown despite the javadocs indicating that 'bands' is nullable");
        }

        coverage.evaluate(new Point2D.Double(15, 14), dest);

        assertEquals(20, dest[0]);
    }
}
