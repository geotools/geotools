package org.geotools.gce.geotiff;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.media.jai.Interpolation;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.processing.Operations;
import org.geotools.geometry.Position2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultDerivedCRS;
import org.geotools.referencing.cs.DefaultCartesianCS;
import org.geotools.referencing.operation.builder.AffineTransformBuilder;
import org.geotools.referencing.operation.builder.MappedPosition;
import org.geotools.referencing.operation.transform.ThinPlateSplineTransform;
import org.geotools.referencing.operation.transform.WarpTransform2D;
import org.junit.Before;
import org.junit.Test;

public final class GeoreferenceTpsTest {

    private BufferedImage imageToGeoreference;
    private CoordinateReferenceSystem BNG;

    @Before
    public void setUp() throws Exception {
        imageToGeoreference = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/edinburgh-castle-1750.png"));
        BNG = CRS.decode("EPSG:27700");
    }

    @Test
    public void testGeoTransform() throws FactoryException, IOException {

        double[][] worldCoords = {
            {325032.15, 673547.7},
            {325067.25, 673551.7},
            {325037.3, 673498.15},
            {325072.6, 673503.2},
            {325145.18, 673418.1},
            {325207.9, 673456.55},
            {325190.1, 673455.05},
            {325267.75, 673507.95},
            {325271.2, 673464.5},
            {325259.5, 673484.25}
        };

        double[][] imageCoords = {
            {606.5912737396017, 191.2470499553168},
            {699.9518358124872, 189.82896503808456},
            {609.9965222042662, 325.9651170923865},
            {703.6408549825404, 326.248734075833},
            {887.3129900430419, 574.3741750309903},
            {1066.9670184592958, 480.6613116458169},
            {1023.4102214098165, 480.1910173707442},
            {1249.065018696892, 314.6559031116118},
            {1244.7255033644483, 456.33661225916615},
            {1212.9023909265275, 413.68782736270845}
        };
        List<MappedPosition> positions = new ArrayList<>();
        for (int i = 0; i < worldCoords.length; i++) {
            positions.add(new MappedPosition(
                    new Position2D(worldCoords[i][0], worldCoords[i][1]),
                    new Position2D(
                            imageCoords[i][0], imageToGeoreference.getHeight() - imageCoords[i][1]) // reverse y axis
                    ));
        }
        AffineTransformBuilder affineBuilder = new AffineTransformBuilder(positions);

        // test the different transformations
        outputAffineTransformationGeoTiff(affineBuilder.getMathTransform());

        // first order polynomial transformation
        Point2D[] worldPoints = new Point2D[worldCoords.length];
        Point2D[] imagePoints = new Point2D[imageCoords.length];
        for (int i = 0; i < worldCoords.length; i++) {
            worldPoints[i] = new Point2D.Double(worldCoords[i][0], worldCoords[i][1]);
            imagePoints[i] = new Point2D.Double(imageCoords[i][0], imageCoords[i][1]);
        }
        MathTransform warpTransform = new WarpTransform2D(worldPoints, imagePoints, 1);
        outputFirstOrderPolynomialTransformationGeoTiff(warpTransform);

        // the new thin plate spline transformation
        outputThinPlateSplineTransformationGeoTiff(new ThinPlateSplineTransform(positions));
    }

    private void outputAffineTransformationGeoTiff(MathTransform mathTransform) throws IOException {
        DefaultDerivedCRS derivedCRS =
                new DefaultDerivedCRS("imageCRS", BNG, mathTransform, DefaultCartesianCS.GENERIC_2D);
        GridCoverageFactory factory = new GridCoverageFactory();
        ReferencedEnvelope ref = new ReferencedEnvelope(
                0, imageToGeoreference.getWidth(), 0, imageToGeoreference.getHeight(), derivedCRS);
        GridCoverage2D coverage = factory.create("GridCoverage", imageToGeoreference, ref);
        // resample
        GridCoverage2D resampled = (GridCoverage2D) Operations.DEFAULT.resample(
                coverage, BNG, null, Interpolation.getInstance(Interpolation.INTERP_NEAREST));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GeoTiffWriter writer = new GeoTiffWriter(outputStream);

        writer.write(resampled, null);
        outputStream.writeTo(new FileOutputStream("edinburgh-castle-affine.tif"));
    }

    private void outputFirstOrderPolynomialTransformationGeoTiff(MathTransform mathTransform) throws IOException {
        DefaultDerivedCRS derivedCRS =
                new DefaultDerivedCRS("imageCRS", BNG, mathTransform, DefaultCartesianCS.GENERIC_2D);
        GridCoverageFactory factory = new GridCoverageFactory();
        ReferencedEnvelope ref = new ReferencedEnvelope(
                0, imageToGeoreference.getWidth(), 0, imageToGeoreference.getHeight(), derivedCRS);
        GridCoverage2D coverage = factory.create("GridCoverage", imageToGeoreference, ref);
        // resample
        GridCoverage2D resampled = (GridCoverage2D) Operations.DEFAULT.resample(
                coverage, BNG, null, Interpolation.getInstance(Interpolation.INTERP_NEAREST));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GeoTiffWriter writer = new GeoTiffWriter(outputStream);

        writer.write(resampled, null);
        outputStream.writeTo(new FileOutputStream("edinburgh-castle-poly1.tif"));
    }

    private void outputThinPlateSplineTransformationGeoTiff(MathTransform mathTransform) throws IOException {
        DefaultDerivedCRS derivedCRS =
                new DefaultDerivedCRS("imageCRS", BNG, mathTransform, DefaultCartesianCS.GENERIC_2D);
        GridCoverageFactory factory = new GridCoverageFactory();
        ReferencedEnvelope ref = new ReferencedEnvelope(
                0, imageToGeoreference.getWidth(), 0, imageToGeoreference.getHeight(), derivedCRS);
        GridCoverage2D coverage = factory.create("GridCoverage", imageToGeoreference, ref);
        // resample
        GridCoverage2D resampled = (GridCoverage2D) Operations.DEFAULT.resample(
                coverage, BNG, null, Interpolation.getInstance(Interpolation.INTERP_NEAREST));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GeoTiffWriter writer = new GeoTiffWriter(outputStream);

        writer.write(resampled, null);
        outputStream.writeTo(new FileOutputStream("edinburgh-castle-tps.tif"));
    }
}
