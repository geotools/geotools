/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.processing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;

import javax.imageio.ImageIO;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;

import org.geotools.TestData;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.ViewType;
import org.geotools.coverage.processing.operation.Extrema;
import org.geotools.factory.Hints;
import org.geotools.geometry.Envelope2D;
import org.geotools.image.test.ImageAssert;
import org.geotools.metadata.iso.spatial.PixelTranslation;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultProjectedCRS;
import org.geotools.referencing.cs.DefaultCartesianCS;
import org.geotools.referencing.operation.DefaultMathTransformFactory;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.NoSuchIdentifierException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;


/**
 * Visual test of the "Resample" operation. A remote sensing image is projected from a fitted
 * coordinate system to a geographic one.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Rémi Eve (IRD)
 * @author Martin Desruisseaux (IRD)
 * @author Simone Giannecchini, GeoSolutions
 */
public final class ResampleTest extends GridProcessingTestBase {
    
    private final static String GOOGLE_MERCATOR_WKT="PROJCS[\"WGS 84 / Pseudo-Mercator\","+
    "GEOGCS[\"Popular Visualisation CRS\","+
        "DATUM[\"Popular_Visualisation_Datum\","+
            "SPHEROID[\"Popular Visualisation Sphere\",6378137,0,"+
                "AUTHORITY[\"EPSG\",\"7059\"]],"+
            "TOWGS84[0,0,0,0,0,0,0],"+
            "AUTHORITY[\"EPSG\",\"6055\"]],"+
        "PRIMEM[\"Greenwich\",0,"+
            "AUTHORITY[\"EPSG\",\"8901\"]],"+
        "UNIT[\"degree\",0.01745329251994328,"+
            "AUTHORITY[\"EPSG\",\"9122\"]],"+
        "AUTHORITY[\"EPSG\",\"4055\"]],"+
    "UNIT[\"metre\",1,"+
        "AUTHORITY[\"EPSG\",\"9001\"]],"+
    "PROJECTION[\"Mercator_1SP\"],"+
    "PARAMETER[\"central_meridian\",0],"+
    "PARAMETER[\"scale_factor\",1],"+
    "PARAMETER[\"false_easting\",0],"+
    "PARAMETER[\"false_northing\",0],"+
    "AUTHORITY[\"EPSG\",\"3785\"],"+
    "AXIS[\"X\",EAST],"+
    "AXIS[\"Y\",NORTH]]";
    /**
     * The source grid coverage, to be initialized by {@link #setUp}.
     * Contains 8-bits indexed color model for a PNG image, with categories.
     */
    private GridCoverage2D coverage;

    /**
     * An other source coverage initialized by {@link #setUp}.
     * Contains indexed color model for a GIF image, without categories.
     */
    private GridCoverage2D indexedCoverage;

    /**
     * An other source coverage initialized by {@link #setUp}.
     * Contains indexed color model for a GIF image, without categories.
     */
    private GridCoverage2D indexedCoverageWithTransparency;

    /**
     * An other source coverage initialized by {@link #setUp}.
     * Contains float values.
     */
    private GridCoverage2D floatCoverage;
    
    /**
     * An other source coverage initialized by {@link #setUp}.
     * Contains ushort values.
     */
    private GridCoverage2D ushortCoverage;

    /**
     * Set up common objects used for all tests.
     */
    @Before
    public void setUp() {
        coverage                        = EXAMPLES.get(0);
        indexedCoverage                 = EXAMPLES.get(2);
        indexedCoverageWithTransparency = EXAMPLES.get(3);
        floatCoverage                   = EXAMPLES.get(4);
        ushortCoverage                  = EXAMPLES.get(5);
        Hints.putSystemDefault(Hints.RESAMPLE_TOLERANCE, 0.333);
    }

    /**
     * Returns a projected CRS for test purpose.
     */
    private static CoordinateReferenceSystem getProjectedCRS(final GridCoverage2D coverage) {
        try {
            final GeographicCRS  base = (GeographicCRS) coverage.getCoordinateReferenceSystem();
            final Ellipsoid ellipsoid = base.getDatum().getEllipsoid();
            final DefaultMathTransformFactory factory = new DefaultMathTransformFactory();
            final ParameterValueGroup parameters = factory.getDefaultParameters("Oblique_Stereographic");
            parameters.parameter("semi_major").setValue(ellipsoid.getSemiMajorAxis());
            parameters.parameter("semi_minor").setValue(ellipsoid.getSemiMinorAxis());
            parameters.parameter("central_meridian").setValue(5);
            parameters.parameter("latitude_of_origin").setValue(-5);
            final MathTransform mt;
            try {
                mt = factory.createParameterizedTransform(parameters);
            } catch (FactoryException exception) {
                fail(exception.getLocalizedMessage());
                return null;
            }
            return new DefaultProjectedCRS("Stereographic", base, mt, DefaultCartesianCS.PROJECTED);
        } catch (NoSuchIdentifierException exception) {
            fail(exception.getLocalizedMessage());
            return null;
        }
    }

    /**
     * Projects the specified coverage to the same CRS without hints.
     * The result will be displayed in a window if {@link #SHOW} is set to {@code true}.
     *
     * @param coverage  The coverage to project.
     * @return The operation name which was applied on the image, or {@code null} if none.
     */
    private static String showProjected(final GridCoverage2D coverage) {
        return showProjected(coverage, coverage.getCoordinateReferenceSystem(), null, null, true);
    }

    /**
     * Tests the "Resample" operation with an identity transform.
     *
     * @todo Investigate why we get a Lookup operation on the first coverage.
     */
    @Test
    public void testIdentity() {
        assertEquals("Lookup", showProjected(coverage));
        assertNull(showProjected(indexedCoverage));
        assertNull(showProjected(indexedCoverageWithTransparency));
        assertNull(showProjected(floatCoverage));
    }

    /**
     * Tests the "Resample" operation with a "Crop" transform.
     */
    @Test
    public void testCrop() {
        final GridGeometry2D g1,g2;
        final MathTransform gridToCRS = null;
        g1 = new GridGeometry2D(new GeneralGridEnvelope(new Rectangle(50,50,100,100), 2), gridToCRS, null);
        g2 = new GridGeometry2D(new GeneralGridEnvelope(new Rectangle(50,50,200,200), 2), gridToCRS, null);
        assertEquals("Crop",   showProjected(coverage,        null, g2, null, false));
        assertEquals("Lookup", showProjected(coverage,        null, g2, null, true ));
        assertEquals("Crop",   showProjected(indexedCoverage, null, g1, null, false));
        assertEquals("Crop",   showProjected(indexedCoverageWithTransparency, null, g1, null, false));
        assertEquals("Crop",   showProjected(floatCoverage, null, g1,
                new Hints(Hints.COVERAGE_PROCESSING_VIEW, ViewType.PHOTOGRAPHIC), true));
    }

    /**
     * Tests the "Resample" operation with a stereographic coordinate system.
     */
    @Test
    public void testStereographic() {
        assertEquals("Warp", showProjected(coverage,getProjectedCRS(coverage), null, null, true));
    }
    
    
    /**
     * Tests the "Resample" operation with a stereographic coordinate system.
     * @throws FactoryException 
     * @throws NoSuchAuthorityCodeException 
     */
    @Test
    public void testReproject() throws NoSuchAuthorityCodeException, FactoryException {
        
        // do it again, make sure the image does not turn black since 
        GridCoverage2D coverage_ = project(ushortCoverage,CRS.parseWKT(GOOGLE_MERCATOR_WKT), null,"nearest", null, true);
        
        // reproject the ushort and check that things did not go bad, that is it turned black
        coverage_=(GridCoverage2D) Operations.DEFAULT.extrema(coverage_);
        Object minimums = coverage_.getProperty(Extrema.GT_SYNTHETIC_PROPERTY_MINIMUM);
        Assert.assertTrue(minimums instanceof double[]) ;
        final double[] mins=(double[]) minimums;
        Object maximums = coverage_.getProperty(Extrema.GT_SYNTHETIC_PROPERTY_MAXIMUM);
        Assert.assertTrue(maximums instanceof double[]) ;
        final double[] max=(double[]) maximums;        
        boolean fail=true;
        for(int i=0;i<mins.length;i++)
            if(mins[i]!=max[i]&&max[i]>0)
                fail=false;
        Assert.assertFalse("Reprojection failed", fail);
        
        //exception in case the target crs does not comply with the target gg crs
        try{
            // we supplied both crs and target gg in different crs, we get an exception backS
            assertEquals("Warp", showProjected(coverage,CRS.parseWKT(GOOGLE_MERCATOR_WKT), coverage.getGridGeometry(), null, true));
            Assert.assertTrue("We should not be allowed to set different crs for target crs and target gg", false);
        }catch (Exception e) {
            // ok!
        }
        
        
    }

    /**
     * Tests the "Resample" operation with a stereographic coordinate system.
     *
     * @throws FactoryException If the CRS can't not be created.
     */
    @Test
    public void testsNad83() throws FactoryException {
        Hints.putSystemDefault(Hints.RESAMPLE_TOLERANCE, 0.0);
        final Hints photo = new Hints(Hints.COVERAGE_PROCESSING_VIEW, ViewType.PHOTOGRAPHIC);
        final CoordinateReferenceSystem crs = CRS.parseWKT(
                "GEOGCS[\"NAD83\"," +
                  "DATUM[\"North_American_Datum_1983\"," +
                    "SPHEROID[\"GRS 1980\",6378137,298.257222101,AUTHORITY[\"EPSG\",\"7019\"]]," +
                    "TOWGS84[0,0,0,0,0,0,0],AUTHORITY[\"EPSG\",\"6269\"]]," +
                  "PRIMEM[\"Greenwich\",0, AUTHORITY[\"EPSG\",\"8901\"]]," +
                  "UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9108\"]]," +
                  "AXIS[\"Lat\",NORTH]," +
                  "AXIS[\"Long\",EAST]," +
                  "AUTHORITY[\"EPSG\",\"4269\"]]");
        assertEquals("Warp", showProjected(indexedCoverage, crs, null, null, false));
        assertEquals("Warp", showProjected(indexedCoverageWithTransparency, crs, null, null, false));
        assertEquals("Warp", showProjected(floatCoverage, crs, null, photo, true));
    }
    
    @Test
    public void testGoogleWorld() throws Exception {
        File world = TestData.copy(this, "geotiff/world.tiff");
        RenderedImage image = ImageIO.read(world);
        
        final CoordinateReferenceSystem wgs84 = CRS.decode("EPSG:4326", true);
        Envelope2D envelope = new Envelope2D(wgs84, -180, -90, 360, 180);
        GridCoverage2D gcFullWorld = new GridCoverageFactory().create("world", image, envelope);

        // crop, we cannot reproject it fully to the google projection
        final Envelope2D cropEnvelope = new Envelope2D(wgs84, -180, -80, 360, 160);
        GridCoverage2D gcCropWorld = (GridCoverage2D) Operations.DEFAULT.crop(gcFullWorld, cropEnvelope);
        
        // resample
        Hints.putSystemDefault(Hints.RESAMPLE_TOLERANCE, 0d);
        GridCoverage2D gcResampled = (GridCoverage2D) Operations.DEFAULT.resample(gcCropWorld, CRS.decode("EPSG:3857"), 
                null, Interpolation.getInstance(Interpolation.INTERP_BILINEAR));
        
        File expected = new File("src/test/resources/org/geotools/image/test-data/google-reproject.png");
        // allow one row of difference
        ImageAssert.assertEquals(expected, gcResampled.getRenderedImage(), 600);
    }
    
    @Test
    public void testWarpCompareGoogleWorld() throws Exception {
        File world = TestData.copy(this, "geotiff/world.tiff");
        RenderedImage image = ImageIO.read(world);
        
        final CoordinateReferenceSystem wgs84 = CRS.decode("EPSG:4326", true);
        Envelope2D envelope = new Envelope2D(wgs84, -180, -90, 360, 180);
        GridCoverage2D gcFullWorld = new GridCoverageFactory().create("world", image, envelope);

        // crop, we cannot reproject it fully to the google projection
        final Envelope2D cropEnvelope = new Envelope2D(wgs84, -180, -80, 360, 160);
        GridCoverage2D gcCropWorld = (GridCoverage2D) Operations.DEFAULT.crop(gcFullWorld, cropEnvelope);
        
        // resample with approximation
        Hints.putSystemDefault(Hints.RESAMPLE_TOLERANCE, 0.333d);
        GridCoverage2D gcResampledApprox = (GridCoverage2D) Operations.DEFAULT.resample(gcCropWorld, CRS.decode("EPSG:3857"), 
                null, Interpolation.getInstance(Interpolation.INTERP_BILINEAR));
        
        Hints.putSystemDefault(Hints.RESAMPLE_TOLERANCE, 0d);
        GridCoverage2D gcResampledAccurate = (GridCoverage2D) Operations.DEFAULT.resample(gcCropWorld, CRS.decode("EPSG:3857"), 
                null, Interpolation.getInstance(Interpolation.INTERP_BILINEAR));
        
        // allow one row of difference
        ImageAssert.assertEquals(gcResampledAccurate.getRenderedImage(), gcResampledApprox.getRenderedImage(), 600);
    }

    /**
     * Tests the "Resample" operation with an "Affine" transform.
     */
    @Test
    public void testAffine() {
        final Hints photo = new Hints(Hints.COVERAGE_PROCESSING_VIEW, ViewType.PHOTOGRAPHIC);
        showTranslated(coverage,                        null, true,  "Lookup", "Affine");
        showTranslated(indexedCoverage,                 null, true,  "Lookup", "Affine");
        showTranslated(indexedCoverageWithTransparency, null, false, "Lookup", "Affine");
        showTranslated(floatCoverage,
                photo, false, "org.geotools.SampleTranscode", "org.geotools.SampleTranscode");
    }

    /**
     * Tests <var>X</var>,<var>Y</var> translation in the {@link GridGeometry} after
     * a "Resample" operation.
     *
     * @throws NoninvertibleTransformException If a "grid to CRS" transform is not invertible.
     */
    @Test
    public void testTranslation() throws NoninvertibleTransformException {
        doTranslation(coverage);
        doTranslation(indexedCoverage);
        doTranslation(indexedCoverageWithTransparency);
    }

    /**
     * Performs a translation using the "Resample" operation.
     *
     * @param grid the {@link GridCoverage2D} to apply the translation on.
     * @throws NoninvertibleTransformException If a "grid to CRS" transform is not invertible.
     */
    private void doTranslation(GridCoverage2D grid) throws NoninvertibleTransformException {
        final int    transX =  -253;
        final int    transY =  -456;
        final double scaleX =  0.04;
        final double scaleY = -0.04;
        final ParameterBlock block = new ParameterBlock().
                addSource(grid.getRenderedImage()).
                add((float) transX).
                add((float) transY);
        RenderedImage image = JAI.create("Translate", block);
        assertEquals("Incorrect X translation", transX, image.getMinX());
        assertEquals("Incorrect Y translation", transY, image.getMinY());
        
        /*
         * Create a grid coverage from the translated image but with the same envelope.
         * Consequently, the 'gridToCoordinateSystem' should be translated by the same
         * amount, with the opposite sign.
         */
        AffineTransform expected = getAffineTransform(grid);
        assertNotNull(expected);
        expected = new AffineTransform(expected); // Get a mutable instance.
        final GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(null);
        grid = factory.create("Translated",image, grid.getEnvelope(), grid.getSampleDimensions(),new GridCoverage2D[]{grid}, grid.getProperties());
        expected.translate(-transX, -transY);
        assertTransformEquals(expected, getAffineTransform(grid));
        
        /*
         * Apply the "Resample" operation with a specific 'gridToCoordinateSystem' transform.
         * The envelope is left unchanged. The "Resample" operation should compute automatically
         * new image bounds.
         */
        final AffineTransform at = AffineTransform.getScaleInstance(scaleX, scaleY);
        final MathTransform   tr = ProjectiveTransform.create(at);
        //account for the half pixel correction between the two spaces since we are talking raster here but the resample will talk model!
        final MathTransform correctedTransform = PixelTranslation.translate(tr, PixelInCell.CELL_CORNER,PixelInCell.CELL_CENTER);
        final GridGeometry2D geometry = new GridGeometry2D(null, correctedTransform, null);     
        final GridCoverage2D newGrid = (GridCoverage2D) Operations.DEFAULT.resample(grid,grid.getCoordinateReferenceSystem(), geometry, null);
        assertEquals(correctedTransform, getAffineTransform(newGrid));
        image = newGrid.getRenderedImage();
        expected.preConcatenate(at.createInverse());
        final Point point = new Point(transX, transY);
        assertSame(point, expected.transform(point, point)); // Round toward neareast integer
    }
}
