/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.util;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import javax.measure.Unit;
import javax.media.jai.PlanarImage;
import javax.media.jai.operator.ConstantDescriptor;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.data.DataSourceException;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.crs.DefaultProjectedCRS;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.referencing.operation.LinearTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.util.factory.GeoTools;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.geometry.Envelope;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.datum.PrimeMeridian;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import si.uom.SI;

/** Class testing the {@link Utilities} methods. */
public class UtilitiesTest extends Assert {

    private static final double DELTA = 1E-6;

    @Test
    public void testIdentifiers() {
        ReferenceIdentifier[] identifiers = Utilities.getIdentifiers("WGS84");
        assertNotNull(identifiers);
        assertEquals(7, identifiers.length);
        NamedIdentifier first = (NamedIdentifier) identifiers[0];
        assertEquals(first.getCode(), "WGS84");
        assertEquals(first.getAuthority(), Citations.OGC);
    }

    @Test
    public void testParsing() throws DataSourceException {

        // Test basic units parsing
        Unit unit = Utilities.parseUnit("m");
        assertEquals(unit, SI.METRE);

        unit = Utilities.parseUnit("s");
        assertEquals(unit, SI.SECOND);

        unit = Utilities.parseUnit("temp_deg_c");
        assertEquals(unit, SI.CELSIUS);

        // Testinc Datum and Ellipsoid related parsing
        final double inverseFlattening = 298.257223563;
        final double equatorialRadius = 6378137;
        final DefaultGeodeticDatum datum =
                Utilities.getDefaultGeodeticDatum(
                        "WGS84", equatorialRadius, inverseFlattening, SI.METRE);
        final PrimeMeridian primeMeridian = datum.getPrimeMeridian();
        assertEquals(0, primeMeridian.getGreenwichLongitude(), DELTA);
        final Ellipsoid ellipsoid = datum.getEllipsoid();
        assertEquals(equatorialRadius, ellipsoid.getSemiMajorAxis(), DELTA);
        assertEquals(inverseFlattening, ellipsoid.getInverseFlattening(), DELTA);

        // Testing coordinateReferenceSystem setup
        final GeographicCRS geoCrs = Utilities.getBaseCRS(equatorialRadius, inverseFlattening);
        assertEquals(datum, geoCrs.getDatum());

        CoordinateReferenceSystem mercatorCRS =
                Utilities.getMercator2SPProjectedCRS(10, 0, 0, geoCrs, null);
        assertTrue(mercatorCRS instanceof DefaultProjectedCRS);
        DefaultProjectedCRS mercator = (DefaultProjectedCRS) mercatorCRS;
        assertEquals(datum, mercator.getDatum());
        assertEquals(geoCrs, mercator.getBaseCRS());
    }

    @Test
    public void testCustomFile() {

        // Testing a custom file with suffix for coverageName
        final File file = new File("/sampleFile.ext:variable1");
        final File customFile = Utilities.getFileFromCustomInput(file);
        assertEquals("sampleFile.ext", customFile.getName());
    }

    @Test
    public void testEnvelopes()
            throws NoSuchAuthorityCodeException, FactoryException, TransformException {
        // Setting up an UTM and WGS84 CRSs

        // Setup a 3D envelope and return it as 2D, making sure there is no 3rd dimension anymore
        final GeneralEnvelope envelope3D = new GeneralEnvelope(DefaultGeographicCRS.WGS84_3D);
        envelope3D.setEnvelope(0, 0, 0, 10, 10, 10);
        final Envelope requestedEnvelope = Utilities.getRequestedEnvelope2D(envelope3D);
        assertEquals(0, requestedEnvelope.getMinimum(0), DELTA);
        assertEquals(0, requestedEnvelope.getMinimum(1), DELTA);
        assertEquals(10, requestedEnvelope.getMaximum(0), DELTA);
        assertEquals(10, requestedEnvelope.getMaximum(1), DELTA);

        // 3D envelope has 3rd dimension whilst 2D one hasn't
        assertEquals(10, envelope3D.getMaximum(2), DELTA);
        boolean is3D = true;
        try {
            requestedEnvelope.getMaximum(2);
        } catch (IndexOutOfBoundsException e) {
            is3D = false;
        }
        assertFalse(is3D);
    }

    @Test
    public void testGetEnvelope() throws FactoryException, TransformException {

        // Setup an envelope in WGS84
        GeneralEnvelope envelope = new GeneralEnvelope(DefaultGeographicCRS.WGS84);
        envelope.setEnvelope(0, 0, 10, 10);

        GeneralEnvelope wgs84 = new GeneralEnvelope(Utilities.getEnvelopeAsWGS84(envelope, true));
        GeneralEnvelope wgs84_2 =
                new GeneralEnvelope(Utilities.getEnvelopeAsWGS84(envelope, false));

        // Ensure the 2 envelope contain the initial one
        assertFalse(wgs84.isEmpty());
        assertTrue(wgs84.contains(envelope, true));
        assertFalse(wgs84_2.isEmpty());
        assertTrue(wgs84_2.contains(envelope, true));

        // Setup an envelope in EPSG:3857
        envelope = new GeneralEnvelope(CRS.decode("EPSG:3857"));
        envelope.setEnvelope(0, 0, 10, 10);

        wgs84 = new GeneralEnvelope(Utilities.getEnvelopeAsWGS84(envelope, true));
        wgs84_2 = new GeneralEnvelope(Utilities.getEnvelopeAsWGS84(envelope, false));
        // Ensure the new envelopes are not empty
        assertFalse(wgs84.isEmpty());
        assertFalse(wgs84_2.isEmpty());
    }

    @Test
    public void testCropRegion() throws TransformException {
        // Setup an envelope in WGS84
        GeneralEnvelope envelope = new GeneralEnvelope(DefaultGeographicCRS.WGS84);
        envelope.setEnvelope(0, 0, 10, 10);

        // Setup a rectangle
        Rectangle rect = new Rectangle(0, 0, 20, 20);
        GridEnvelope2D env = new GridEnvelope2D(rect);

        // GridGeometry
        GridGeometry2D gg2D = new GridGeometry2D(env, envelope);

        // Getting the crop region
        Rectangle cropRegion =
                Utilities.getCropRegion(envelope, gg2D.getGridToCRS2D(PixelOrientation.UPPER_LEFT));

        // Ensure the crop region is contains the input one
        assertTrue(cropRegion.contains(rect));

        // Check that an exception is thrown when one of the two parameters is null
        boolean notThrown = false;
        try {
            Utilities.getCropRegion(envelope, null);
            notThrown = true;
        } catch (IllegalArgumentException e) {
        }
        assertFalse(notThrown);
        // Reset the boolean
        notThrown = false;

        try {
            Utilities.getCropRegion(null, gg2D.getGridToCRS2D(PixelOrientation.UPPER_LEFT));
            notThrown = true;
        } catch (IllegalArgumentException e) {
        }
        assertFalse(notThrown);
    }

    @Test
    public void testGetTransform() {
        // Setup an envelope in WGS84
        GeneralEnvelope envelope = new GeneralEnvelope(DefaultGeographicCRS.WGS84);
        envelope.setEnvelope(0, 0, 10, 10);

        // Setup a rectangle
        Rectangle rect = new Rectangle(0, 0, 20, 20);
        GridEnvelope2D env = new GridEnvelope2D(rect);

        // GridGeometry
        GridGeometry2D gg2D = new GridGeometry2D(env, envelope);

        // Transform
        MathTransform tr = gg2D.getGridToCRS2D(PixelOrientation.UPPER_LEFT);

        // Check if with CELL_CENTER, the transformation returned is the same
        assertSame(tr, Utilities.getOriginalGridToWorld(tr, PixelInCell.CELL_CENTER));

        // Check with the CELL_CORNER and the following transformation, the result is a
        // Linear Transform
        assertTrue(
                LinearTransform.class.isAssignableFrom(
                        Utilities.getOriginalGridToWorld(tr, PixelInCell.CELL_CORNER).getClass()));

        // Check that a Linear Transform is returned also with an Identity transform
        rect = new Rectangle(0, 0, 10, 10);
        env = new GridEnvelope2D(rect);

        // GridGeometry
        gg2D = new GridGeometry2D(env, envelope);

        // Transform
        tr = gg2D.getGridToCRS2D(PixelOrientation.UPPER_LEFT);

        assertTrue(
                LinearTransform.class.isAssignableFrom(
                        Utilities.getOriginalGridToWorld(tr, PixelInCell.CELL_CORNER).getClass()));
    }

    @Test
    public void testCreateCoverage() throws IOException {
        // Definition of the parameters
        GridCoverageFactory coverageFactory = new GridCoverageFactory(GeoTools.getDefaultHints());
        String coverageName = "test";
        int imageIndex = 0;
        PlanarImage image =
                ConstantDescriptor.create(10f, 10f, new Byte[] {1}, GeoTools.getDefaultHints());
        MathTransform raster2Model = new AffineTransform2D(AffineTransform.getScaleInstance(2, 2));
        CoordinateReferenceSystem spatialReferenceSystem2D = DefaultGeographicCRS.WGS84;

        // Calculate a gridGeometry from the image and the MathTransform
        Rectangle bounds = image.getBounds();
        GridGeometry2D gg2D =
                new GridGeometry2D(
                        new GridEnvelope2D(bounds), raster2Model, spatialReferenceSystem2D);

        GeneralEnvelope coverageEnvelope2D = new GeneralEnvelope(gg2D.getEnvelope());
        GridSampleDimension[] sampleDimensions =
                new GridSampleDimension[] {new GridSampleDimension("testDim")};
        // Creation of the Coverage
        GridCoverage2D coverage1 =
                (GridCoverage2D)
                        Utilities.createCoverageFromImage(
                                coverageFactory,
                                coverageName,
                                imageIndex,
                                image,
                                raster2Model,
                                spatialReferenceSystem2D,
                                null,
                                sampleDimensions);

        GridCoverage2D coverage2 =
                (GridCoverage2D)
                        Utilities.createCoverageFromImage(
                                coverageFactory,
                                coverageName,
                                imageIndex,
                                image,
                                null,
                                null,
                                coverageEnvelope2D,
                                sampleDimensions);

        // Ensure the two coverages have the same envelope and raster dimensions
        assertTrue(coverage1.getEnvelope2D().boundsEquals(coverage2.getEnvelope2D(), 0, 1, 0.01d));
        assertSame(coverage1.getRenderedImage(), coverage2.getRenderedImage());
    }
}
