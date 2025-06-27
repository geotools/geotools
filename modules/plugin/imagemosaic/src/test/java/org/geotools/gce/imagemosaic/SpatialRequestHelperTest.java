/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.datum.PixelInCell;
import org.geotools.api.referencing.operation.MathTransform2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.gce.imagemosaic.SpatialRequestHelper.CoverageProperties;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.junit.Assert;
import org.junit.Test;

/** @author Simone Giannecchini, GeoSolutions */
public class SpatialRequestHelperTest extends Assert {

    /**
     * Requesting the same area at a much smaller resolution.
     *
     * <p>The computed resolution should be much coarser and that is it.
     */
    @Test
    public void testNoRequest() throws Exception {

        // using CoverageProperties to build the SpatialRequestHelper
        CoverageProperties coverageProperties = new CoverageProperties();

        // source area in project crs
        CoordinateReferenceSystem sourceCRS = DefaultGeographicCRS.WGS84;
        ReferencedEnvelope sourceBBox = new ReferencedEnvelope(-180, 180, -90, 90, sourceCRS);
        coverageProperties.setBBox(sourceBBox);
        coverageProperties.setCrs2D(sourceCRS);

        // raster area
        Rectangle sourceRasterArea = new Rectangle(0, 0, 1000, 1000);
        coverageProperties.setRasterArea(sourceRasterArea);

        // creating the g2d
        final GridToEnvelopeMapper geMapper =
                new GridToEnvelopeMapper(new GridEnvelope2D(sourceRasterArea), sourceBBox);
        AffineTransform sourceGridToWorldTransform = geMapper.createAffineTransform();
        coverageProperties.setGridToWorld2D((MathTransform2D) sourceGridToWorldTransform);
        coverageProperties.setFullResolution(new double[] {
            XAffineTransform.getScaleX0(sourceGridToWorldTransform),
            XAffineTransform.getScaleY0(sourceGridToWorldTransform),
        });
        coverageProperties.setGeographicBBox(sourceBBox);
        coverageProperties.setGeographicCRS2D(DefaultGeographicCRS.WGS84);
        SpatialRequestHelper spatialRequestHelper = new SpatialRequestHelper(coverageProperties);

        //
        // no target request
        //

        ///// TEST
        spatialRequestHelper.compute();
        assertFalse(spatialRequestHelper.isEmpty());
        assertFalse(spatialRequestHelper.isNeedsReprojection()); // no reprojection

        // computed raster area
        Rectangle computedRasterArea = spatialRequestHelper.getComputedRasterArea();
        assertFalse(computedRasterArea.isEmpty());
        assertEquals(computedRasterArea, coverageProperties.rasterArea);

        // computed bbox
        BoundingBox computedBBox = spatialRequestHelper.getComputedBBox();
        assertFalse(computedBBox.isEmpty());
        assertEquals(computedBBox, sourceBBox);

        // transform
        AffineTransform computedG2W = spatialRequestHelper.getComputedGridToWorld();
        assertNotNull(computedG2W);
        double[] computedResolution = spatialRequestHelper.getComputedResolution();
        double scale = 1; // we scaled down the original image
        assertNotNull(computedResolution);
        assertEquals(scale * XAffineTransform.getScaleX0(sourceGridToWorldTransform), computedResolution[0], 1E-6);
        assertEquals(scale * XAffineTransform.getScaleY0(sourceGridToWorldTransform), computedResolution[1], 1E-6);

        // all this intersecting and so on MUST not impact the requested resolutions
        GridToEnvelopeMapper gridToEnvelopeMapper =
                new GridToEnvelopeMapper(new GridEnvelope2D(coverageProperties.rasterArea), sourceBBox);
        gridToEnvelopeMapper.setPixelAnchor(PixelInCell.CELL_CORNER);
        double[] expectedResolution = {
            XAffineTransform.getScaleX0(gridToEnvelopeMapper.createAffineTransform()),
            XAffineTransform.getScaleY0(gridToEnvelopeMapper.createAffineTransform())
        };
        assertNotNull(computedResolution);
        assertEquals(expectedResolution[0], computedResolution[0], 1E-6);
        assertEquals(expectedResolution[1], computedResolution[1], 1E-6);

        // for code coverage
        assertNotNull(spatialRequestHelper.toString());

        // accurate resolution
        // should not change anything since it is used only when there is a reprojection involved
        spatialRequestHelper.setAccurateResolution(true);
        spatialRequestHelper.compute();
        double[] computedResolution2 = spatialRequestHelper.getComputedResolution();
        assertEquals(computedResolution[0], computedResolution2[0], 1E-6);
        assertEquals(computedResolution[1], computedResolution2[1], 1E-6);
    }

    /**
     * Requesting an included area at a much smaller resolution.
     *
     * <p>1) The computed resolution should be much coarser. 2) The computed BBOX and the Computed raster area should be
     * equal to the requested ones
     */
    @Test
    public void testBasic2() throws Exception {

        // using CoverageProperties to build the SpatialRequestHelper
        CoverageProperties coverageProperties = new CoverageProperties();

        // source area in project crs
        CoordinateReferenceSystem sourceCRS = DefaultGeographicCRS.WGS84;
        ReferencedEnvelope sourceBBox = new ReferencedEnvelope(-180, 180, -90, 90, sourceCRS);
        coverageProperties.setBBox(sourceBBox);
        coverageProperties.setCrs2D(sourceCRS);

        // raster area
        Rectangle sourceRasterArea = new Rectangle(0, 0, 1000, 1000);
        coverageProperties.setRasterArea(sourceRasterArea);

        // creating the g2d
        final GridToEnvelopeMapper geMapper =
                new GridToEnvelopeMapper(new GridEnvelope2D(sourceRasterArea), sourceBBox);
        AffineTransform sourceGridToWorldTransform = geMapper.createAffineTransform();
        coverageProperties.setGridToWorld2D((MathTransform2D) sourceGridToWorldTransform);
        coverageProperties.setFullResolution(new double[] {
            XAffineTransform.getScaleX0(sourceGridToWorldTransform),
            XAffineTransform.getScaleY0(sourceGridToWorldTransform)
        });
        coverageProperties.setGeographicBBox(sourceBBox);
        coverageProperties.setGeographicCRS2D(DefaultGeographicCRS.WGS84);
        SpatialRequestHelper spatialRequestHelper = new SpatialRequestHelper(coverageProperties);

        //
        // now the target request
        //
        spatialRequestHelper.setAccurateResolution(false);
        ReferencedEnvelope requestedBBox = new ReferencedEnvelope(0, 180, 0, 90, sourceCRS);
        GridEnvelope2D requestedRasterArea = new GridEnvelope2D(0, 0, 250, 250);
        spatialRequestHelper.setRequestedGridGeometry(
                new GridGeometry2D(requestedRasterArea, new GeneralBounds(requestedBBox)));

        ///// TEST
        spatialRequestHelper.compute();
        assertFalse(spatialRequestHelper.isEmpty());
        assertFalse(spatialRequestHelper.isNeedsReprojection()); // no reprojection

        // computed raster area is equal to requested raster area
        Rectangle computedRasterArea = spatialRequestHelper.getComputedRasterArea();
        assertFalse(computedRasterArea.isEmpty());
        assertEquals(computedRasterArea, requestedRasterArea);

        // computed bbox is equal to requestede bbox
        BoundingBox computedBBox = spatialRequestHelper.getComputedBBox();
        assertFalse(computedBBox.isEmpty());
        assertEquals(computedBBox, requestedBBox);

        // transform
        AffineTransform computedG2W = spatialRequestHelper.getComputedGridToWorld();
        assertNotNull(computedG2W);
        double[] computedResolution = spatialRequestHelper.getComputedResolution();
        double scale = 1000.0 / 2 / 250; // we scaled down the original image but we also use a portion of it
        assertNotNull(computedResolution);
        assertEquals(scale * XAffineTransform.getScaleX0(sourceGridToWorldTransform), computedResolution[0], 1E-6);
        assertEquals(scale * XAffineTransform.getScaleY0(sourceGridToWorldTransform), computedResolution[1], 1E-6);

        // all this intersecting and so on MUST not impact the requested resolutions
        GridToEnvelopeMapper gridToEnvelopeMapper =
                new GridToEnvelopeMapper(new GridEnvelope2D(requestedRasterArea), requestedBBox);
        gridToEnvelopeMapper.setPixelAnchor(PixelInCell.CELL_CORNER);
        double[] expectedResolution = {
            XAffineTransform.getScaleX0(gridToEnvelopeMapper.createAffineTransform()),
            XAffineTransform.getScaleY0(gridToEnvelopeMapper.createAffineTransform())
        };
        assertNotNull(computedResolution);
        assertEquals(expectedResolution[0], computedResolution[0], 1E-6);
        assertEquals(expectedResolution[1], computedResolution[1], 1E-6);
    }

    /**
     * Requesting an intersecting area at a much smaller resolution.
     *
     * <p>1) The computed resolution should be much coarser. 2) The computed BBOX and the Computed raster area should be
     * equal to the requested ones
     */
    @Test
    public void testBasic3() throws Exception {

        // using CoverageProperties to build the SpatialRequestHelper
        CoverageProperties coverageProperties = new CoverageProperties();

        // source area in project crs
        CoordinateReferenceSystem sourceCRS = DefaultGeographicCRS.WGS84;
        ReferencedEnvelope sourceBBox = new ReferencedEnvelope(-180, 100, -20, 90, sourceCRS);
        coverageProperties.setBBox(sourceBBox);
        coverageProperties.setCrs2D(sourceCRS);

        // raster area
        Rectangle sourceRasterArea = new Rectangle(0, 0, 1000, 1000);
        coverageProperties.setRasterArea(sourceRasterArea);

        // creating the g2d
        final GridToEnvelopeMapper geMapper =
                new GridToEnvelopeMapper(new GridEnvelope2D(sourceRasterArea), sourceBBox);
        AffineTransform sourceGridToWorldTransform = geMapper.createAffineTransform();
        coverageProperties.setGridToWorld2D((MathTransform2D) sourceGridToWorldTransform);
        coverageProperties.setFullResolution(new double[] {
            XAffineTransform.getScaleX0(sourceGridToWorldTransform),
            XAffineTransform.getScaleY0(sourceGridToWorldTransform)
        });
        coverageProperties.setGeographicBBox(sourceBBox);
        coverageProperties.setGeographicCRS2D(DefaultGeographicCRS.WGS84);
        SpatialRequestHelper spatialRequestHelper = new SpatialRequestHelper(coverageProperties);

        //
        // now the target request
        //
        spatialRequestHelper.setAccurateResolution(false);
        ReferencedEnvelope requestedBBox = new ReferencedEnvelope(0, 180, 0, 90, sourceCRS);
        GridEnvelope2D requestedRasterArea = new GridEnvelope2D(0, 0, 250, 250);
        spatialRequestHelper.setRequestedGridGeometry(
                new GridGeometry2D(requestedRasterArea, new GeneralBounds(requestedBBox)));

        ///// TEST
        spatialRequestHelper.compute();
        assertFalse(spatialRequestHelper.isEmpty());
        assertFalse(spatialRequestHelper.isNeedsReprojection()); // no reprojection

        // computed bbox is equal to requestede bbox
        BoundingBox computedBBox = spatialRequestHelper.getComputedBBox();
        assertFalse(computedBBox.isEmpty());
        ReferencedEnvelope finalReferencedEnvelope = new ReferencedEnvelope(0, 100, 0, 90, sourceCRS);
        assertEquals(computedBBox, finalReferencedEnvelope);

        // computed raster area is equal to requested raster area
        Rectangle computedRasterArea = spatialRequestHelper.getComputedRasterArea();
        assertFalse(computedRasterArea.isEmpty());
        GridToEnvelopeMapper gridToEnvelopeMapper =
                new GridToEnvelopeMapper(new GridEnvelope2D(requestedRasterArea), requestedBBox);
        gridToEnvelopeMapper.setPixelAnchor(PixelInCell.CELL_CORNER);
        assertEquals(
                computedRasterArea,
                CRS.transform(gridToEnvelopeMapper.createTransform().inverse(), computedBBox)
                        .toRectangle2D()
                        .getBounds());

        // g2w transform
        // all this intersecting and so on MUST not impact the requested resolutions
        AffineTransform computedG2W = spatialRequestHelper.getComputedGridToWorld();
        assertNotNull(computedG2W);
        double[] computedResolution = spatialRequestHelper.getComputedResolution();
        double[] expectedResolution = {
            XAffineTransform.getScaleX0(gridToEnvelopeMapper.createAffineTransform()),
            XAffineTransform.getScaleY0(gridToEnvelopeMapper.createAffineTransform())
        };
        assertNotNull(computedResolution);
        assertEquals(expectedResolution[0], computedResolution[0], 1E-6);
        assertEquals(expectedResolution[1], computedResolution[1], 1E-6);
    }

    @Test
    public void testReprojectQuick() throws Exception {

        // using CoverageProperties to build the SpatialRequestHelper
        CoverageProperties coverageProperties = new CoverageProperties();

        // source area in project crs
        CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:27700");
        ReferencedEnvelope sourceBBox = new ReferencedEnvelope(414000, 420000, 237000, 301000, sourceCRS);
        coverageProperties.setBBox(sourceBBox);
        coverageProperties.setCrs2D(sourceCRS);

        // raster area
        Rectangle sourceRasterArea = new Rectangle(0, 0, 1000, 1000);
        coverageProperties.setRasterArea(sourceRasterArea);

        // creating the g2d
        final GridToEnvelopeMapper geMapper =
                new GridToEnvelopeMapper(new GridEnvelope2D(sourceRasterArea), sourceBBox);

        AffineTransform sourceGridToWorldTransform = geMapper.createAffineTransform();
        coverageProperties.setGridToWorld2D((MathTransform2D) sourceGridToWorldTransform);
        coverageProperties.setFullResolution(new double[] {
            XAffineTransform.getScaleX0(sourceGridToWorldTransform),
            XAffineTransform.getScaleY0(sourceGridToWorldTransform)
        });
        coverageProperties.setGeographicBBox(new ReferencedEnvelope(
                -1.7973440460762267,
                -1.7061039437509753,
                52.03105268214016,
                52.60660481087451,
                DefaultGeographicCRS.WGS84));
        coverageProperties.setGeographicCRS2D(DefaultGeographicCRS.WGS84);
        SpatialRequestHelper spatialRequestHelper = new SpatialRequestHelper(coverageProperties);

        // now the target request
        spatialRequestHelper.setAccurateResolution(false);
        spatialRequestHelper.setRequestedGridGeometry(new GridGeometry2D(
                new GridEnvelope2D(0, 0, 256, 256),
                new GeneralBounds(new ReferencedEnvelope(
                        -1.9868610903408341,
                        -1.1430930819885086,
                        51.938491047471814,
                        52.705668101075581,
                        DefaultGeographicCRS.WGS84))));

        ///// TEST
        spatialRequestHelper.compute();
        assertFalse(spatialRequestHelper.isEmpty());
        assertTrue(spatialRequestHelper.isNeedsReprojection());
        Rectangle destinationRasterArea = spatialRequestHelper.getComputedRasterArea();
        assertFalse(destinationRasterArea.isEmpty());
        BoundingBox computedBBox = spatialRequestHelper.getComputedBBox();
        assertFalse(computedBBox.isEmpty());
        AffineTransform computedG2W = spatialRequestHelper.getComputedGridToWorld();
        assertNotNull(computedG2W);
        double[] computedResolution = spatialRequestHelper.getComputedResolution();
        assertNotNull(computedResolution);
    }

    /**
     * Requesting the same area at a much smaller resolution in a different project.
     *
     * <p>The computed resolution should be much coarser and that is it.
     */
    @Test
    public void testBasicReproject() throws Exception {

        // using CoverageProperties to build the SpatialRequestHelper
        CoverageProperties coverageProperties = new CoverageProperties();

        // source area in project crs
        CoordinateReferenceSystem sourceCRS = DefaultGeographicCRS.WGS84;
        ReferencedEnvelope sourceBBox = new ReferencedEnvelope( // Italy?
                8, 11, 42, 44, sourceCRS);
        coverageProperties.setBBox(sourceBBox);
        coverageProperties.setCrs2D(sourceCRS);

        // raster area
        Rectangle sourceRasterArea = new Rectangle(0, 0, 1000, 1000);
        coverageProperties.setRasterArea(sourceRasterArea);

        // creating the g2d
        final GridToEnvelopeMapper geMapper =
                new GridToEnvelopeMapper(new GridEnvelope2D(sourceRasterArea), sourceBBox);
        AffineTransform sourceGridToWorldTransform = geMapper.createAffineTransform();
        coverageProperties.setGridToWorld2D((MathTransform2D) sourceGridToWorldTransform);
        coverageProperties.setFullResolution(new double[] {
            XAffineTransform.getScaleX0(sourceGridToWorldTransform),
            XAffineTransform.getScaleY0(sourceGridToWorldTransform)
        });
        coverageProperties.setGeographicBBox(sourceBBox);
        coverageProperties.setGeographicCRS2D(DefaultGeographicCRS.WGS84);
        SpatialRequestHelper spatialRequestHelper = new SpatialRequestHelper(coverageProperties);

        //
        // now the request
        //
        spatialRequestHelper.setAccurateResolution(false);
        GridEnvelope2D requestedRasterArea = new GridEnvelope2D(0, 0, 256, 256);
        CoordinateReferenceSystem requestCRS = CRS.decode("EPSG:3857");
        GeneralBounds requestedBBox = CRS.transform(sourceBBox, requestCRS);
        spatialRequestHelper.setRequestedGridGeometry(new GridGeometry2D(requestedRasterArea, requestedBBox));

        ///// TEST
        spatialRequestHelper.compute();
        assertFalse(spatialRequestHelper.isEmpty());
        assertTrue(spatialRequestHelper.isNeedsReprojection()); // no reprojection

        // computed raster area
        Rectangle computedRasterArea = spatialRequestHelper.getComputedRasterArea();
        assertFalse(computedRasterArea.isEmpty());
        assertEquals(computedRasterArea, requestedRasterArea);

        // computed bbox is equal to the requested one
        BoundingBox computedBBox = spatialRequestHelper.getComputedBBox();
        assertFalse(computedBBox.isEmpty());
        // the source bbox and the computed one are the same
        // there might be minor differences due to multiple back and forth roundings, but we need
        // to make sure they are negligible
        assertTrue(new GeneralBounds(computedBBox).equals(sourceBBox, 1E-4, true));

        // transform
        AffineTransform computedG2W = spatialRequestHelper.getComputedGridToWorld();
        assertNotNull(computedG2W);
        double[] computedResolution = spatialRequestHelper.getComputedResolution();
        double scale = 1000.0 / 256; // we scaled down the original image
        assertNotNull(computedResolution);
        assertEquals(scale * XAffineTransform.getScaleX0(sourceGridToWorldTransform), computedResolution[0], 1E-6);
        assertEquals(scale * XAffineTransform.getScaleY0(sourceGridToWorldTransform), computedResolution[1], 1E-6);

        // all this intersecting and so on MUST not impact the requested resolutions
        GridToEnvelopeMapper gridToEnvelopeMapper =
                new GridToEnvelopeMapper(new GridEnvelope2D(requestedRasterArea), sourceBBox);
        gridToEnvelopeMapper.setPixelAnchor(PixelInCell.CELL_CORNER);
        double[] expectedResolution = {
            XAffineTransform.getScaleX0(gridToEnvelopeMapper.createAffineTransform()),
            XAffineTransform.getScaleY0(gridToEnvelopeMapper.createAffineTransform())
        };
        assertNotNull(computedResolution);
        assertEquals(expectedResolution[0], computedResolution[0], 1E-6);
        assertEquals(expectedResolution[1], computedResolution[1], 1E-6);
    }

    /**
     * Requesting an included area at a much smaller resolution.
     *
     * <p>1) The computed resolution should be much coarser. 2) The computed BBOX and the Computed raster area should be
     * equal to the requested ones
     */
    @Test
    public void testBasic2Reproject() throws Exception {

        // using CoverageProperties to build the SpatialRequestHelper
        CoverageProperties coverageProperties = new CoverageProperties();

        // source area in project crs
        CoordinateReferenceSystem sourceCRS = DefaultGeographicCRS.WGS84;
        ReferencedEnvelope sourceBBox = new ReferencedEnvelope(-180, 180, -70, 70, sourceCRS);
        coverageProperties.setBBox(sourceBBox);
        coverageProperties.setCrs2D(sourceCRS);

        // raster area
        Rectangle sourceRasterArea = new Rectangle(0, 0, 1000, 1000);
        coverageProperties.setRasterArea(sourceRasterArea);

        // creating the g2d
        final GridToEnvelopeMapper geMapper =
                new GridToEnvelopeMapper(new GridEnvelope2D(sourceRasterArea), sourceBBox);
        AffineTransform sourceGridToWorldTransform = geMapper.createAffineTransform();
        coverageProperties.setGridToWorld2D((MathTransform2D) sourceGridToWorldTransform);
        coverageProperties.setFullResolution(new double[] {
            XAffineTransform.getScaleX0(sourceGridToWorldTransform),
            XAffineTransform.getScaleY0(sourceGridToWorldTransform)
        });
        coverageProperties.setGeographicBBox(sourceBBox);
        coverageProperties.setGeographicCRS2D(DefaultGeographicCRS.WGS84);
        SpatialRequestHelper spatialRequestHelper = new SpatialRequestHelper(coverageProperties);

        //
        // now the target request
        //
        spatialRequestHelper.setAccurateResolution(false);
        GridEnvelope2D requestedRasterArea = new GridEnvelope2D(0, 0, 256, 256);
        CoordinateReferenceSystem requestCRS = CRS.decode("EPSG:3857");
        ReferencedEnvelope requestedBBox_ = new ReferencedEnvelope(0, 180, 0, 70, sourceCRS);
        GeneralBounds requestedBBox = CRS.transform(requestedBBox_, requestCRS);
        spatialRequestHelper.setRequestedGridGeometry(new GridGeometry2D(requestedRasterArea, requestedBBox));

        ///// TEST
        spatialRequestHelper.compute();
        assertFalse(spatialRequestHelper.isEmpty());
        assertTrue(spatialRequestHelper.isNeedsReprojection()); // no reprojection

        // computed raster area is equal to requested raster area
        Rectangle computedRasterArea = spatialRequestHelper.getComputedRasterArea();
        assertFalse(computedRasterArea.isEmpty());
        assertEquals(computedRasterArea, requestedRasterArea);

        // computed bbox is equal to requestede bbox
        BoundingBox computedBBox = spatialRequestHelper.getComputedBBox();
        assertFalse(computedBBox.isEmpty());
        // the source bbox and the computed one are the same
        // there might be minor differences due to multiple back and forth roundings, but we need
        // to make sure they are negligible
        assertTrue(new GeneralBounds(computedBBox).equals(requestedBBox_, 1E-4, true));

        // transform
        AffineTransform computedG2W = spatialRequestHelper.getComputedGridToWorld();
        assertNotNull(computedG2W);
        double[] computedResolution = spatialRequestHelper.getComputedResolution();
        double scaleX = 1000.0 / 2 / 256; // we scaled down the original image but we also use a portion of it
        double scaleY = 1000.0 / 2 / 256; // we scaled down the original image but we also use a portion of it
        assertNotNull(computedResolution);
        assertEquals(scaleX * XAffineTransform.getScaleX0(sourceGridToWorldTransform), computedResolution[0], 1E-6);
        assertEquals(scaleY * XAffineTransform.getScaleY0(sourceGridToWorldTransform), computedResolution[1], 1E-6);

        // all this intersecting and so on MUST not impact the requested resolutions
        GridToEnvelopeMapper gridToEnvelopeMapper =
                new GridToEnvelopeMapper(new GridEnvelope2D(requestedRasterArea), requestedBBox_);
        gridToEnvelopeMapper.setPixelAnchor(PixelInCell.CELL_CORNER);
        double[] expectedResolution = {
            XAffineTransform.getScaleX0(gridToEnvelopeMapper.createAffineTransform()),
            XAffineTransform.getScaleY0(gridToEnvelopeMapper.createAffineTransform())
        };
        assertNotNull(computedResolution);
        assertEquals(expectedResolution[0], computedResolution[0], 1E-6);
        assertEquals(expectedResolution[1], computedResolution[1], 1E-6);

        // accurate resolution
        // should not change anything since it is used only when there is a reprojection involved
        spatialRequestHelper.setAccurateResolution(true);
        spatialRequestHelper.compute();
        double[] computedResolution2 = spatialRequestHelper.getComputedResolution();
        assertNotEquals(computedResolution[0], computedResolution2[0], 1E-6);
        assertNotEquals(
                computedResolution[1], computedResolution2[1], 1E-6); // high deformations on latitude for large areas
    }

    /**
     * Requesting an intersecting area at a much smaller resolution.
     *
     * <p>1) The computed resolution should be much coarser. 2) The computed BBOX and the Computed raster area should be
     * equal to the requested ones
     */
    @Test
    public void testBasic3Reproject() throws Exception {

        // using CoverageProperties to build the SpatialRequestHelper
        CoverageProperties coverageProperties = new CoverageProperties();

        // source area in project crs
        CoordinateReferenceSystem sourceCRS = DefaultGeographicCRS.WGS84;
        ReferencedEnvelope sourceBBox = new ReferencedEnvelope(-180, 100, -20, 90, sourceCRS);
        coverageProperties.setBBox(sourceBBox);
        coverageProperties.setCrs2D(sourceCRS);

        // raster area
        Rectangle sourceRasterArea = new Rectangle(0, 0, 1000, 1000);
        coverageProperties.setRasterArea(sourceRasterArea);

        // creating the g2d
        final GridToEnvelopeMapper geMapper =
                new GridToEnvelopeMapper(new GridEnvelope2D(sourceRasterArea), sourceBBox);
        AffineTransform sourceGridToWorldTransform = geMapper.createAffineTransform();
        coverageProperties.setGridToWorld2D((MathTransform2D) sourceGridToWorldTransform);
        coverageProperties.setFullResolution(new double[] {
            XAffineTransform.getScaleX0(sourceGridToWorldTransform),
            XAffineTransform.getScaleY0(sourceGridToWorldTransform)
        });
        coverageProperties.setGeographicBBox(sourceBBox);
        coverageProperties.setGeographicCRS2D(DefaultGeographicCRS.WGS84);
        SpatialRequestHelper spatialRequestHelper = new SpatialRequestHelper(coverageProperties);

        //
        // now the request
        //
        spatialRequestHelper.setAccurateResolution(false);
        GridEnvelope2D requestedRasterArea = new GridEnvelope2D(0, 0, 256, 256);
        CoordinateReferenceSystem requestCRS = CRS.decode("EPSG:3857");
        ReferencedEnvelope requestedBBox_ = new ReferencedEnvelope(0, 180, 0, 70, sourceCRS);
        GeneralBounds requestedBBox = CRS.transform(requestedBBox_, requestCRS);
        spatialRequestHelper.setRequestedGridGeometry(new GridGeometry2D(requestedRasterArea, requestedBBox));

        ///// TEST
        spatialRequestHelper.compute();
        assertFalse(spatialRequestHelper.isEmpty());
        assertTrue(spatialRequestHelper.isNeedsReprojection()); // no reprojection

        // computed bbox is equal to requestede bbox
        BoundingBox computedBBox = spatialRequestHelper.getComputedBBox();
        assertFalse(computedBBox.isEmpty());
        ReferencedEnvelope finalReferencedEnvelope = new ReferencedEnvelope(0, 100, 0, 70, sourceCRS);
        // the source bbox and the computed one are the same
        // there might be minor differences due to multiple back and forth roundings, but we need
        // to make sure they are negligible
        assertTrue(new GeneralBounds(computedBBox).equals(finalReferencedEnvelope, 1E-4, true));

        // computed raster area is equal to requested raster area
        Rectangle computedRasterArea = spatialRequestHelper.getComputedRasterArea();
        assertFalse(computedRasterArea.isEmpty());
        GridToEnvelopeMapper gridToEnvelopeMapper =
                new GridToEnvelopeMapper(new GridEnvelope2D(requestedRasterArea), requestedBBox);
        gridToEnvelopeMapper.setPixelAnchor(PixelInCell.CELL_CORNER);
        assertEquals(computedRasterArea, new Rectangle(0, 0, 142, 256));

        // g2w transform
        // all this intersecting and so on MUST not impact the requested resolutions
        // transform
        AffineTransform computedG2W = spatialRequestHelper.getComputedGridToWorld();
        assertNotNull(computedG2W);
        double[] computedResolution = spatialRequestHelper.getComputedResolution();

        // all this intersecting and so on MUST not impact the requested resolutions
        gridToEnvelopeMapper =
                new GridToEnvelopeMapper(new GridEnvelope2D(computedRasterArea), finalReferencedEnvelope);
        gridToEnvelopeMapper.setPixelAnchor(PixelInCell.CELL_CORNER);
        double[] expectedResolution = {
            XAffineTransform.getScaleX0(gridToEnvelopeMapper.createAffineTransform()),
            XAffineTransform.getScaleY0(gridToEnvelopeMapper.createAffineTransform())
        };
        assertNotNull(computedResolution);
        assertEquals(expectedResolution[0], computedResolution[0], 1E-6);
        assertEquals(expectedResolution[1], computedResolution[1], 1E-6);
    }

    /**
     * Requesting the same area at a much smaller resolution.
     *
     * <p>The computed resolution should be much coarser and that is it.
     */
    @Test
    public void testBasic() throws Exception {

        // using CoverageProperties to build the SpatialRequestHelper
        CoverageProperties coverageProperties = new CoverageProperties();

        // source area in project crs
        CoordinateReferenceSystem sourceCRS = DefaultGeographicCRS.WGS84;
        ReferencedEnvelope sourceBBox = new ReferencedEnvelope(-180, 180, -90, 90, sourceCRS);
        coverageProperties.setBBox(sourceBBox);
        coverageProperties.setCrs2D(sourceCRS);

        // raster area
        Rectangle sourceRasterArea = new Rectangle(0, 0, 1000, 1000);
        coverageProperties.setRasterArea(sourceRasterArea);

        // creating the g2d
        final GridToEnvelopeMapper geMapper =
                new GridToEnvelopeMapper(new GridEnvelope2D(sourceRasterArea), sourceBBox);
        AffineTransform sourceGridToWorldTransform = geMapper.createAffineTransform();
        coverageProperties.setGridToWorld2D((MathTransform2D) sourceGridToWorldTransform);
        coverageProperties.setFullResolution(new double[] {
            XAffineTransform.getScaleX0(sourceGridToWorldTransform),
            XAffineTransform.getScaleY0(sourceGridToWorldTransform)
        });
        coverageProperties.setGeographicBBox(sourceBBox);
        coverageProperties.setGeographicCRS2D(DefaultGeographicCRS.WGS84);
        SpatialRequestHelper spatialRequestHelper = new SpatialRequestHelper(coverageProperties);

        //
        // now the target request
        //
        spatialRequestHelper.setAccurateResolution(false);
        GridEnvelope2D requestedRasterArea = new GridEnvelope2D(0, 0, 256, 256);
        spatialRequestHelper.setRequestedGridGeometry(
                new GridGeometry2D(requestedRasterArea, new GeneralBounds(sourceBBox)));

        ///// TEST
        spatialRequestHelper.compute();
        assertFalse(spatialRequestHelper.isEmpty());
        assertFalse(spatialRequestHelper.isNeedsReprojection()); // no reprojection

        // computed raster area
        Rectangle computedRasterArea = spatialRequestHelper.getComputedRasterArea();
        assertFalse(computedRasterArea.isEmpty());
        assertEquals(computedRasterArea, requestedRasterArea);

        // computed bbox
        BoundingBox computedBBox = spatialRequestHelper.getComputedBBox();
        assertFalse(computedBBox.isEmpty());
        assertEquals(computedBBox, sourceBBox);

        // transform
        AffineTransform computedG2W = spatialRequestHelper.getComputedGridToWorld();
        assertNotNull(computedG2W);
        double[] computedResolution = spatialRequestHelper.getComputedResolution();
        double scale = 1000.0 / 256; // we scaled down the original image
        assertNotNull(computedResolution);
        assertEquals(scale * XAffineTransform.getScaleX0(sourceGridToWorldTransform), computedResolution[0], 1E-6);
        assertEquals(scale * XAffineTransform.getScaleY0(sourceGridToWorldTransform), computedResolution[1], 1E-6);

        // all this intersecting and so on MUST not impact the requested resolutions
        GridToEnvelopeMapper gridToEnvelopeMapper =
                new GridToEnvelopeMapper(new GridEnvelope2D(requestedRasterArea), sourceBBox);
        gridToEnvelopeMapper.setPixelAnchor(PixelInCell.CELL_CORNER);
        double[] expectedResolution = {
            XAffineTransform.getScaleX0(gridToEnvelopeMapper.createAffineTransform()),
            XAffineTransform.getScaleY0(gridToEnvelopeMapper.createAffineTransform())
        };
        assertNotNull(computedResolution);
        assertEquals(expectedResolution[0], computedResolution[0], 1E-6);
        assertEquals(expectedResolution[1], computedResolution[1], 1E-6);

        // for code coverage
        assertNotNull(spatialRequestHelper.toString());

        // accurate resolution
        // should not change anything since it is used only when there is a reprojection involved
        spatialRequestHelper.setAccurateResolution(true);
        spatialRequestHelper.compute();
        double[] computedResolution2 = spatialRequestHelper.getComputedResolution();
        assertEquals(computedResolution[0], computedResolution2[0], 1E-6);
        assertEquals(computedResolution[1], computedResolution2[1], 1E-6);
    }
}
