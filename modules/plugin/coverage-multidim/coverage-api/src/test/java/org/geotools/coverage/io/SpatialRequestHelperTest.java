/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.ConstantDescriptor;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.io.SpatialRequestHelper.CoverageProperties;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.data.DataSourceException;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.util.factory.GeoTools;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.geometry.Envelope;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.operation.TransformException;

/** @author Nicola Lagomarsini Geosolutions */
public class SpatialRequestHelperTest {

    private static final double TOLERANCE = 0.01d;

    private static GridCoverage2D coverage;

    private static CoverageProperties coverageProperties;

    private static RenderedOp image;

    @BeforeClass
    public static void setup() {
        image = ConstantDescriptor.create(512f, 512f, new Byte[] {1}, GeoTools.getDefaultHints());
        Envelope envelope = new ReferencedEnvelope(-180, 180, -85, 85, DefaultGeographicCRS.WGS84);
        // Creation of a dummy GridCoverage 2D
        coverage =
                new GridCoverageFactory(GeoTools.getDefaultHints())
                        .create("testCoverage", image, envelope);
        // Properties
        coverageProperties = new CoverageProperties();
        coverageProperties.setBbox(new ReferencedEnvelope(coverage.getEnvelope2D()));
        coverageProperties.setCrs2D(coverage.getCoordinateReferenceSystem2D());
        coverageProperties.setFullResolution(
                CoverageUtilities.getResolution(
                        (AffineTransform)
                                coverage.getGridGeometry()
                                        .getGridToCRS2D(PixelOrientation.UPPER_LEFT)));
        coverageProperties.setRasterArea(coverage.getGridGeometry().getGridRange2D());
        coverageProperties.setGeographicBBox(new ReferencedEnvelope(coverage.getEnvelope2D()));
        coverageProperties.setGeographicCRS2D(coverage.getCoordinateReferenceSystem2D());
        coverageProperties.setGridToWorld2D(
                coverage.getGridGeometry().getGridToCRS2D(PixelOrientation.UPPER_LEFT));
    }

    @Test
    public void testHelperSimple() throws DataSourceException {
        // Initialization of the helper
        SpatialRequestHelper helper = new SpatialRequestHelper();
        // Final GridGeometry
        GridEnvelope2D gridRange = new GridEnvelope2D(0, 0, 1024, 1024);
        GridGeometry2D gridGeometry = new GridGeometry2D(gridRange, coverageProperties.getBbox());
        // Setting the requested gridGeometry to have
        helper.setRequestedGridGeometry(gridGeometry);
        helper.setCoverageProperties(coverageProperties);

        // Calculation of the final properties
        helper.prepare();

        // Calculate the expected results
        AffineTransform requestedGridToWorld = helper.getRequestedGridToWorld();
        double[] calculatedResolution =
                new double[] {
                    XAffineTransform.getScaleX0(requestedGridToWorld),
                    XAffineTransform.getScaleY0(requestedGridToWorld)
                };
        Rectangle calculatedRasterArea = new Rectangle(1024, 1024);

        // Ensure the same Coverage properties
        assertEquals(coverageProperties, helper.getCoverageProperties());
        // Ensure is not empty
        assertTrue(!helper.isEmpty());
        // Check resolution
        assertArrayEquals(helper.getRequestedResolution(), calculatedResolution, TOLERANCE);
        // Check the same boundingBox
        assertTrue(
                helper.getCropBBox()
                        .contains(new ReferencedEnvelope(coverageProperties.getBbox())));
        // Check the same destination Area
        assertTrue(helper.getDestinationRasterArea().contains(calculatedRasterArea));
    }

    @Test
    public void testHelperEmpty() throws DataSourceException {
        // Initialization of the helper
        SpatialRequestHelper helper = new SpatialRequestHelper();
        ReferencedEnvelope envelope =
                new ReferencedEnvelope(-180, 0, -90, 90, coverageProperties.getGeographicCRS2D());
        // Creation of a dummy GridCoverage 2D
        GridCoverage2D coverage2 =
                new GridCoverageFactory(GeoTools.getDefaultHints())
                        .create("testCoverage", image, envelope);
        CoverageProperties coverageProperties2 = new CoverageProperties();
        coverageProperties2.setBbox(new ReferencedEnvelope(coverage2.getEnvelope2D()));
        coverageProperties2.setCrs2D(coverage2.getCoordinateReferenceSystem2D());
        coverageProperties2.setFullResolution(
                CoverageUtilities.getResolution(
                        (AffineTransform)
                                coverage2
                                        .getGridGeometry()
                                        .getGridToCRS2D(PixelOrientation.UPPER_LEFT)));
        coverageProperties2.setRasterArea(coverage2.getGridGeometry().getGridRange2D());
        coverageProperties2.setGeographicBBox(new ReferencedEnvelope(coverage2.getEnvelope2D()));
        coverageProperties2.setGeographicCRS2D(coverage2.getCoordinateReferenceSystem2D());
        coverageProperties2.setGridToWorld2D(
                coverage2.getGridGeometry().getGridToCRS2D(PixelOrientation.UPPER_LEFT));
        // Final GridGeometry
        GridEnvelope2D gridRange = new GridEnvelope2D(0, 0, 1024, 1024);
        ReferencedEnvelope envelope2 =
                new ReferencedEnvelope(1, 180, -90, 90, coverageProperties.getGeographicCRS2D());
        GridGeometry2D gridGeometry = new GridGeometry2D(gridRange, envelope2);
        // Setting the requested gridGeometry to have
        helper.setRequestedGridGeometry(gridGeometry);
        helper.setCoverageProperties(coverageProperties2);

        // Calculation of the final properties
        helper.prepare();

        // Ensure the same Coverage properties
        assertEquals(coverageProperties2, helper.getCoverageProperties());
        // Ensure is not empty
        assertTrue(helper.isEmpty());
    }

    @Test
    public void testHelperWithReprojection()
            throws DataSourceException, NoSuchAuthorityCodeException, TransformException,
                    FactoryException {
        // Initialization of the helper
        SpatialRequestHelper helper = new SpatialRequestHelper();
        // Final GridGeometry
        GridEnvelope2D gridRange = new GridEnvelope2D(0, 0, 1024, 1024);
        GeneralEnvelope envelope = CRS.transform(coverage.getEnvelope(), CRS.decode("EPSG:3857"));
        GridGeometry2D gridGeometry = new GridGeometry2D(gridRange, envelope);
        // Setting the requested gridGeometry to have
        helper.setRequestedGridGeometry(gridGeometry);
        helper.setCoverageProperties(coverageProperties);

        // Calculation of the final properties
        helper.prepare();

        // Calculate the expected results
        final GeneralEnvelope temp =
                new GeneralEnvelope(
                        CRS.transform(gridGeometry.getEnvelope(), coverageProperties.crs2D));
        temp.setCoordinateReferenceSystem(coverageProperties.crs2D);
        temp.intersect(coverageProperties.getBbox());
        final GridToEnvelopeMapper geMapper =
                new GridToEnvelopeMapper(new GridEnvelope2D(gridRange), temp);
        final AffineTransform tempTransform = geMapper.createAffineTransform();
        double[] calculatedResolution =
                new double[] {
                    XAffineTransform.getScaleX0(tempTransform),
                    XAffineTransform.getScaleY0(tempTransform)
                };

        // Ensure is not empty
        assertTrue(!helper.isEmpty());
        // Check resolution
        assertArrayEquals(helper.getRequestedResolution(), calculatedResolution, TOLERANCE);
        // Check the same boundingBox
        assertTrue(helper.getCropBBox().contains(new ReferencedEnvelope(temp)));
        // Check the same destination Area
        assertTrue(helper.getDestinationRasterArea().contains(gridRange));
    }

    @Test
    public void testHelperWithNoGridGeometry()
            throws DataSourceException, NoSuchAuthorityCodeException, TransformException,
                    FactoryException {
        // Initialization of the helper
        SpatialRequestHelper helper = new SpatialRequestHelper();
        // Setting the properties
        helper.setCoverageProperties(coverageProperties);

        // Calculation of the final properties
        helper.prepare();

        // Ensure is not empty
        assertTrue(!helper.isEmpty());
        // Check resolution
        assertArrayEquals(
                helper.getRequestedResolution(), coverageProperties.getFullResolution(), TOLERANCE);
        // Check the same boundingBox
        assertTrue(helper.getCropBBox().contains(coverageProperties.getGeographicBBox()));
        // Check the same destination Area
        assertTrue(helper.getDestinationRasterArea().contains(coverageProperties.getRasterArea()));
    }

    @AfterClass
    public static void after() {
        PlanarImage.wrapRenderedImage(image).dispose();
    }
}
