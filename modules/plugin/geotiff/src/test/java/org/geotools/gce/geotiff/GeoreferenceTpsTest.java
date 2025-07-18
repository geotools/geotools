package org.geotools.gce.geotiff;

import static org.junit.Assert.assertEquals;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.Interpolation;

import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.processing.Operations;
import org.geotools.geometry.GeneralBounds;
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

    public static final GeneralParameterValue[] NO_PARAMS = null;
    private BufferedImage imageToGeoreference;
    private CoordinateReferenceSystem BNG;

    @Before
    public void setUp() throws Exception {
        imageToGeoreference = javax.imageio.ImageIO.read(
                getClass().getResourceAsStream("/org/geotools/gce/geotiff/test-data/ed-castle-1750.jpg"));
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
            {303.29563686980083, 95.6235249776584},
            {349.9759179062436, 94.91448251904228},
            {304.9982611021331, 162.98255854619326},
            {351.8204274912702, 163.1243670379165},
            {443.65649502152096, 287.18708751549517},
            {533.4835092296479, 240.33065582290846},
            {511.70511070490827, 240.0955086853721},
            {624.532509348446, 157.3279515558059},
            {622.3627516822241, 228.16830612958308},
            {606.4511954632637, 206.84391368135422}
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
        assertThinPlateSplineTransformationGeoTiff(new ThinPlateSplineTransform(positions));
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

        writer.write(resampled, NO_PARAMS);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ImageInputStream iis = ImageIO.createImageInputStream(byteArrayInputStream);
        GeoTiffReader reader = new GeoTiffReader(iis);
        GeneralBounds envelope = reader.getOriginalEnvelope();
        assertEquals("Wrong envelope minX", 324807.6741454695, envelope.getMinimum(0), 0.001);
        assertEquals("Wrong envelope minY", 673318.757108962, envelope.getMinimum(1), 0.001);
        assertEquals("Wrong envelope maxX", 325501.4622986133, envelope.getMaximum(0), 0.001);
        assertEquals("Wrong envelope maxY", 673631.2096537778, envelope.getMaximum(1), 0.001);
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

        writer.write(resampled, NO_PARAMS);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ImageInputStream iis = ImageIO.createImageInputStream(byteArrayInputStream);
        GeoTiffReader reader = new GeoTiffReader(iis);
        GeneralBounds envelope = reader.getOriginalEnvelope();
        assertEquals("Wrong envelope minX", 324804.45693588257, envelope.getMinimum(0), 0.001);
        assertEquals("Wrong envelope minY", 673288.3893161934, envelope.getMinimum(1), 0.001);
        assertEquals("Wrong envelope maxX", 325508.2505963644, envelope.getMaximum(0), 0.001);
        assertEquals("Wrong envelope maxY", 673667.7420926894, envelope.getMaximum(1), 0.001);
    }

    private void assertThinPlateSplineTransformationGeoTiff(MathTransform mathTransform) throws IOException {
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

        writer.write(resampled, NO_PARAMS);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ImageInputStream iis = ImageIO.createImageInputStream(byteArrayInputStream);
        GeoTiffReader reader = new GeoTiffReader(iis);
        GeneralBounds envelope = reader.getOriginalEnvelope();
        assertEquals("Wrong envelope minX", 324804.22162886866, envelope.getMinimum(0), 0.001);
        assertEquals("Wrong envelope minY", 673310.540005115, envelope.getMinimum(1), 0.001);
        assertEquals("Wrong envelope maxX", 325498.8018434337, envelope.getMaximum(0), 0.001);
        assertEquals("Wrong envelope maxY", 673616.8228763198, envelope.getMaximum(1), 0.001);
    }
}
