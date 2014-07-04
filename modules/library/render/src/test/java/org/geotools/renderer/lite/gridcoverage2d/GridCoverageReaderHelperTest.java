package org.geotools.renderer.lite.gridcoverage2d;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.media.jai.Interpolation;

import org.geotools.TestData;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.projection.MapProjection;
import org.geotools.renderer.crs.ProjectionHandler;
import org.geotools.renderer.crs.ProjectionHandlerFinder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.coverage.grid.Format;
import org.opengis.geometry.BoundingBox;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class GridCoverageReaderHelperTest {

    static final double EPS = 1e-9;

    private GeoTiffReader reader;

    @Before
    public void getData() throws IOException {
        MapProjection.SKIP_SANITY_CHECKS = true;
        File coverageFile = TestData.copy(this, "geotiff/world.tiff");
        assertTrue(coverageFile.exists());
        reader = new GeoTiffReader(coverageFile);
    }

    @After
    public void close() {
        MapProjection.SKIP_SANITY_CHECKS = false;
        reader.dispose();
    }

    @Test
    public void testGeographicLarge() throws Exception {
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(-360, 360, -90, 90,
                DefaultGeographicCRS.WGS84);
        GridCoverageReaderHelper helper = new GridCoverageReaderHelper(reader, new Rectangle(720,
                180), mapExtent, Interpolation.getInstance(Interpolation.INTERP_NEAREST));

        // read single coverage with no projection handling
        GridCoverage2D coverage = helper.readCoverage(null);
        Envelope2D envelope = coverage.getEnvelope2D();
        assertEquals(-180, envelope.getMinX(), EPS);
        assertEquals(180, envelope.getMaxX(), EPS);
        assertEquals(-90, envelope.getMinY(), EPS);
        assertEquals(90, envelope.getMaxY(), EPS);

        // try multiple coverage with projection handling, should not make a difference
        // since we are already reading everything in a single shot, just in need of coverage
        // replication
        // (which has to be performed after the eventual reprojection, so not here in the reader)
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(mapExtent,
                reader.getCoordinateReferenceSystem(), true);
        List<GridCoverage2D> coverages = helper.readCoverages(null, handler);
        // System.out.println(coverages);
        assertEquals(1, coverages.size());
        assertEquals(envelope, coverages.get(0).getEnvelope2D());
    }

    @Test
    public void testGeographicDatelineCross() throws Exception {
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(170, 190, 70, 80,
                DefaultGeographicCRS.WGS84);
        GridCoverageReaderHelper helper = new GridCoverageReaderHelper(reader, new Rectangle(100,
                100), mapExtent, Interpolation.getInstance(Interpolation.INTERP_NEAREST));

        // read single coverage with no projection handling, the geotiff reader gives us all
        GridCoverage2D coverage = helper.readCoverage(null);
        Envelope2D envelope = coverage.getEnvelope2D();
        assertEquals(-180, envelope.getMinX(), EPS);
        assertEquals(180, envelope.getMaxX(), EPS);
        assertEquals(-90, envelope.getMinY(), EPS);
        assertEquals(90, envelope.getMaxY(), EPS);

        // now read with projection handling instead, we must get two at the
        // two ends of the dateline
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(mapExtent,
                reader.getCoordinateReferenceSystem(), true);
        List<GridCoverage2D> coverages = helper.readCoverages(null, handler);
        // System.out.println(coverages);
        assertEquals(2, coverages.size());
        Envelope2D firstEnvelope = coverages.get(0).getEnvelope2D();
        assertEquals(169.2, firstEnvelope.getMinX(), EPS);
        assertEquals(180, firstEnvelope.getMaxX(), EPS);
        assertEquals(69.3, firstEnvelope.getMinY(), EPS);
        assertEquals(80.1, firstEnvelope.getMaxY(), EPS);
        Envelope2D secondEnvelope = coverages.get(1).getEnvelope2D();
        assertEquals(-180, secondEnvelope.getMinX(), EPS);
        assertEquals(-169.2, secondEnvelope.getMaxX(), EPS);
        assertEquals(69.3, secondEnvelope.getMinY(), EPS);
        assertEquals(80.1, secondEnvelope.getMaxY(), EPS);
    }

    @Test
    public void testUTM() throws Exception {
        // setup a request large enough to cause severe reprojection deformation
        CoordinateReferenceSystem crs = CRS.decode("EPSG:32632", true);
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(-1.5e7, 1.5e7, 0, 1e6, crs);
        // System.out.println(mapExtent.transform(DefaultGeographicCRS.WGS84, true));
        GridCoverageReaderHelper helper = new GridCoverageReaderHelper(reader, new Rectangle(400,
                200), mapExtent, Interpolation.getInstance(Interpolation.INTERP_NEAREST));

        // read single coverage with no projection handling, we should get the full requested area
        GridCoverage2D coverage = helper.readCoverage(null);
        Envelope2D envelope = coverage.getEnvelope2D();
        // System.out.println(envelope);
        assertTrue(envelope.getMinX() < -100);
        assertTrue(envelope.getMaxX() > 100);

        // now read via the projection handlers
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(mapExtent,
                reader.getCoordinateReferenceSystem(), true);
        List<GridCoverage2D> coverages = helper.readCoverages(null, handler);
        // System.out.println(coverages);
        assertEquals(1, coverages.size());
        envelope = coverages.get(0).getEnvelope2D();
        // west/east limited to 45 degrees from the central meridian, plus reading gutter
        assertEquals(-36, envelope.getMinX(), EPS);
        assertEquals(54, envelope.getMaxX(), EPS);
    }

    @Test
    public void testConic() throws Exception {
        // setup a request large enough to cause severe reprojection deformation
        CoordinateReferenceSystem crs = CRS.decode("EPSG:32632", true);
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(-1.5e7, 1.5e7, 0, 1e6, crs);
        // System.out.println(mapExtent.transform(DefaultGeographicCRS.WGS84, true));
        GridCoverageReaderHelper helper = new GridCoverageReaderHelper(reader, new Rectangle(400,
                200), mapExtent, Interpolation.getInstance(Interpolation.INTERP_NEAREST));

        // read single coverage with no projection handling, we should get the full requested area
        GridCoverage2D coverage = helper.readCoverage(null);
        Envelope2D envelope = coverage.getEnvelope2D();
        // System.out.println(envelope);
        assertTrue(envelope.getMinX() < -100);
        assertTrue(envelope.getMaxX() > 100);

        // now read via the projection handlers
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(mapExtent,
                reader.getCoordinateReferenceSystem(), true);
        List<GridCoverage2D> coverages = helper.readCoverages(null, handler);
        // System.out.println(coverages);
        assertEquals(1, coverages.size());
        envelope = coverages.get(0).getEnvelope2D();
        // west/east limited to 45 degrees from the central meridian
        assertEquals(-36, envelope.getMinX(), EPS);
        assertEquals(54, envelope.getMaxX(), EPS);
    }

    @Test
    public void testOutsideDefinitionArea() throws Exception {
        // setup a request that is outside of the coverage
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3031", true);
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(-1250000, 0, -13750000, -12500000,
                crs);
        // System.out.println(mapExtent.transform(DefaultGeographicCRS.WGS84, true));
        GridCoverageReaderHelper helper = new GridCoverageReaderHelper(reader, new Rectangle(400,
                200), mapExtent, Interpolation.getInstance(Interpolation.INTERP_NEAREST));

        // read, nothing should come out
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(mapExtent,
                reader.getCoordinateReferenceSystem(), true);
        List<GridCoverage2D> coverages = helper.readCoverages(null, handler);
        assertTrue(coverages.isEmpty());
    }

    @Test
    public void testFullResolutionNull() throws Exception {
        // this one has null native resolutions
        final GridCoverage2D coverage = new GridCoverageFactory().create("test",
                new float[200][100], new ReferencedEnvelope(-180, 180, -90, 90,
                        DefaultGeographicCRS.WGS84));
        GridCoverage2DReader reader = new AbstractGridCoverage2DReader() {
            
            {
                this.crs = DefaultGeographicCRS.WGS84;
                this.originalEnvelope = new GeneralEnvelope((BoundingBox) coverage.getEnvelope2D());
                this.originalGridRange = coverage.getGridGeometry().getGridRange();
            }

            @Override
            public Format getFormat() {
                return null;
            }
            
            @Override
            public GridCoverage2D read(GeneralParameterValue[] parameters) throws IllegalArgumentException,
                    IOException {
                // return fake coveage
                return coverage;
            }
        };
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3031", true);
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(-20000000, 20000000, -20000000,
                20000000, crs);

        GridCoverageReaderHelper helper = new GridCoverageReaderHelper(reader, new Rectangle(400,
                200), mapExtent, Interpolation.getInstance(Interpolation.INTERP_NEAREST));

        // read, we should get back a coverage, not a exception
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(mapExtent,
                reader.getCoordinateReferenceSystem(), true);
        List<GridCoverage2D> coverages = helper.readCoverages(null, handler);
        assertEquals(1, coverages.size());
        
        
    }
}
