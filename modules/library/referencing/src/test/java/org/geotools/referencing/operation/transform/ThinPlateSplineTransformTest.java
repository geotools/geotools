package org.geotools.referencing.operation.transform;

import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.operation.Matrix;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.Position2D;
import org.geotools.referencing.operation.builder.MappedPosition;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ThinPlateSplineTransformTest {
    @Test
    public void transformSinglePointReturnsCorrectCoordinates() throws TransformException {
        List<MappedPosition> positions = new ArrayList<>();
        positions.add(new MappedPosition(new Position2D(0, 0), new Position2D(10, 10)));
        positions.add(new MappedPosition(new Position2D(1, 0), new Position2D(11, 10)));
        positions.add(new MappedPosition(new Position2D(0, 1), new Position2D(10, 11)));

        ThinPlateSplineTransform transform = new ThinPlateSplineTransform(positions);
        Position result = transform.transform(new Position2D(0.5, 0.5), null);

        assertEquals(10.5, result.getOrdinate(0), 0.1);
        assertEquals(10.5, result.getOrdinate(1), 0.1);
    }

    @Test
    public void transformArrayOfPointsReturnsCorrectCoordinatesDoubleOverload() throws  TransformException {
        List<MappedPosition> positions = new ArrayList<>();
        positions.add(new MappedPosition(new Position2D(0, 0), new Position2D(10, 10)));
        positions.add(new MappedPosition(new Position2D(1, 0), new Position2D(11, 10)));
        positions.add(new MappedPosition(new Position2D(0, 1), new Position2D(10, 11)));

        ThinPlateSplineTransform transform = new ThinPlateSplineTransform(positions);
        double[] srcPts = {0.5, 0.5, 0.25, 0.25};
        double[] dstPts = new double[4];

        transform.transform(srcPts, 0, dstPts, 0, 2);

        assertEquals(10.5, dstPts[0], 0.1);
        assertEquals(10.5, dstPts[1], 0.1);
        assertEquals(10.25, dstPts[2], 0.1);
        assertEquals(10.25, dstPts[3], 0.1);
    }

    @Test
    public void transformArrayOfPointsReturnsCorrectCoordinatesFloatOverload() throws  TransformException {
        List<MappedPosition> positions = new ArrayList<>();
        positions.add(new MappedPosition(new Position2D(0, 0), new Position2D(10, 10)));
        positions.add(new MappedPosition(new Position2D(1, 0), new Position2D(11, 10)));
        positions.add(new MappedPosition(new Position2D(0, 1), new Position2D(10, 11)));

        ThinPlateSplineTransform transform = new ThinPlateSplineTransform(positions);
        float[] srcPts = {0.5f, 0.5f, 0.25f, 0.25f};
        float[] dstPts = new float[4];

        transform.transform(srcPts, 0, dstPts, 0, 2);

        assertEquals(10.5, dstPts[0], 0.1);
        assertEquals(10.5, dstPts[1], 0.1);
        assertEquals(10.25, dstPts[2], 0.1);
        assertEquals(10.25, dstPts[3], 0.1);
    }

    @Test
    public void transformArrayOfPointsReturnsCorrectCoordinatesSrcFloatDstDoubleOverload() throws  TransformException {
        List<MappedPosition> positions = new ArrayList<>();
        positions.add(new MappedPosition(new Position2D(0, 0), new Position2D(10, 10)));
        positions.add(new MappedPosition(new Position2D(1, 0), new Position2D(11, 10)));
        positions.add(new MappedPosition(new Position2D(0, 1), new Position2D(10, 11)));

        ThinPlateSplineTransform transform = new ThinPlateSplineTransform(positions);
        float[] srcPts = {0.5f, 0.5f, 0.25f, 0.25f};
        double[] dstPts = new double[4];

        transform.transform(srcPts, 0, dstPts, 0, 2);

        assertEquals(10.5, dstPts[0], 0.1);
        assertEquals(10.5, dstPts[1], 0.1);
        assertEquals(10.25, dstPts[2], 0.1);
        assertEquals(10.25, dstPts[3], 0.1);
    }

    @Test
    public void transformArrayOfPointsReturnsCorrectCoordinatesSrcDoubleDstFloatDoubleOverload() throws  TransformException {
        List<MappedPosition> positions = new ArrayList<>();
        positions.add(new MappedPosition(new Position2D(0, 0), new Position2D(10, 10)));
        positions.add(new MappedPosition(new Position2D(1, 0), new Position2D(11, 10)));
        positions.add(new MappedPosition(new Position2D(0, 1), new Position2D(10, 11)));

        ThinPlateSplineTransform transform = new ThinPlateSplineTransform(positions);
        double[] srcPts = {0.5f, 0.5f, 0.25f, 0.25f};
        float[] dstPts = new float[4];

        transform.transform(srcPts, 0, dstPts, 0, 2);

        assertEquals(10.5, dstPts[0], 0.1);
        assertEquals(10.5, dstPts[1], 0.1);
        assertEquals(10.25, dstPts[2], 0.1);
        assertEquals(10.25, dstPts[3], 0.1);
    }

    @Test
    public void transformArrayOfPointsReturnsCorrectCoordinates() throws TransformException {
        List<MappedPosition> positions = new ArrayList<>();
        positions.add(new MappedPosition(new Position2D(0, 0), new Position2D(10, 10)));
        positions.add(new MappedPosition(new Position2D(1, 0), new Position2D(11, 10)));
        positions.add(new MappedPosition(new Position2D(0, 1), new Position2D(10, 11)));

        ThinPlateSplineTransform transform = new ThinPlateSplineTransform(positions);
        double[] srcPts = {0.5, 0.5, 0.25, 0.25};
        double[] dstPts = new double[4];

        transform.transform(srcPts, 0, dstPts, 0, 2);

        assertEquals(10.5, dstPts[0], 0.1);
        assertEquals(10.5, dstPts[1], 0.1);
        assertEquals(10.25, dstPts[2], 0.1);
        assertEquals(10.25, dstPts[3], 0.1);
    }

    @Test
    public void emptyPositionsListThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ThinPlateSplineTransform(new ArrayList<>())
        );
        assertEquals("Positions list must not be null or empty.", exception.getMessage());
    }

    @Test
    public void nullPositionsListThrowsException() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ThinPlateSplineTransform(null)
        );
        assertEquals("Positions list must not be null or empty.", exception.getMessage());
    }


    @Test
    public void testThrowsExceptionListSizesDiffer() {
        List<Coordinate> sourcePoints = List.of(
                new Coordinate(0, 0),
                new Coordinate(1, 1)
        );

        List<Coordinate> targetPoints = List.of(
                new Coordinate(0, 0)
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ThinPlateSplineTransform(sourcePoints, targetPoints)
        );

        assertEquals("Source and target point lists must be the same size.", exception.getMessage());
    }

    @Test
    public void inverseTransformReversesTheTransformation() throws TransformException {
        List<MappedPosition> positions = new ArrayList<>();
        positions.add(new MappedPosition(new Position2D(0, 0), new Position2D(10, 10)));
        positions.add(new MappedPosition(new Position2D(1, 0), new Position2D(11, 10)));
        positions.add(new MappedPosition(new Position2D(0, 1), new Position2D(10, 11)));

        ThinPlateSplineTransform transform = new ThinPlateSplineTransform(positions);
        Position2D original = new Position2D(0.5, 0.5);
        Position transformed = transform.transform(original, null);
        Position result = transform.inverse().transform(transformed, null);

        assertEquals(original.getOrdinate(0), result.getOrdinate(0), 0.1);
        assertEquals(original.getOrdinate(1), result.getOrdinate(1), 0.1);
    }

    @Test
    public void testForwardTransformation() throws Exception {
        List<Coordinate> source = Arrays.asList(
                new Coordinate(0, 0), new Coordinate(0, 100), new Coordinate(100, 0), new Coordinate(100, 100));

        List<Coordinate> target = Arrays.asList(
                new Coordinate(10, 10), new Coordinate(10, 110), new Coordinate(110, 10), new Coordinate(110, 110));

        ThinPlateSplineTransform transform = new ThinPlateSplineTransform(source, target);

        Position2D input = new Position2D(50, 50);
        Position2D output = (Position2D) transform.transform(input, null);

        assertEquals(60.0, output.getX(), 2.0); // interpolated near center
        assertEquals(60.0, output.getY(), 2.0);
    }

    @Test
    public void testInverseTransformation() throws Exception {
        List<Coordinate> source = Arrays.asList(
                new Coordinate(0, 0), new Coordinate(0, 100), new Coordinate(100, 0), new Coordinate(100, 100));

        List<Coordinate> target = Arrays.asList(
                new Coordinate(10, 10), new Coordinate(10, 110), new Coordinate(110, 10), new Coordinate(110, 110));

        ThinPlateSplineTransform transform = new ThinPlateSplineTransform(source, target);
        ThinPlateSplineTransform inverse = (ThinPlateSplineTransform) transform.inverse();

        Position2D input = new Position2D(50, 50);
        Position2D mapped = (Position2D) transform.transform(input, null);
        Position2D unmapped = (Position2D) inverse.transform(mapped, null);

        assertEquals(input.getX(), unmapped.getX(), 1.5); // allow interpolation error
        assertEquals(input.getY(), unmapped.getY(), 1.5);
    }

    @Test
    public void testToWKTStructure() {
        List<Coordinate> source = Arrays.asList(new Coordinate(1.0, 2.0), new Coordinate(3.0, 4.0));
        List<Coordinate> target = Arrays.asList(new Coordinate(10.0, 20.0), new Coordinate(30.0, 40.0));

        ThinPlateSplineTransform transform = new ThinPlateSplineTransform(source, target);
        String wkt = transform.toWKT();

        assertTrue(wkt.contains("PARAM_MT[\"ThinPlateSpline\""));
        assertTrue(wkt.contains("PARAMETER[\"source_0\", [1.0, 2.0]]"));
        assertTrue(wkt.contains("PARAMETER[\"target_1\", [30.0, 40.0]]"));
        assertTrue(wkt.contains("PARAMETER[\"num_points\", 2]"));
    }

    @Test
    public void testIsIdentityAlwaysFalse() {
        List<Coordinate> source = Arrays.asList(
                new Coordinate(0, 0), new Coordinate(0, 100), new Coordinate(100, 0), new Coordinate(100, 100));

        List<Coordinate> target = Arrays.asList(
                new Coordinate(0, 0), new Coordinate(0, 100), new Coordinate(100, 0), new Coordinate(100, 100));

        ThinPlateSplineTransform transform = new ThinPlateSplineTransform(source, target);
        //TPS warping always warps why would you bother passing in the same source and target?
        assertFalse(transform.isIdentity());
    }

    @Test
    public void derivativeReturnsValidJacobianMatrix() throws TransformException {
        List<MappedPosition> positions = new ArrayList<>();
        positions.add(new MappedPosition(new Position2D(0, 0), new Position2D(10, 10)));
        positions.add(new MappedPosition(new Position2D(1, 0), new Position2D(11, 10)));
        positions.add(new MappedPosition(new Position2D(0, 1), null));

        ThinPlateSplineTransform transform = new ThinPlateSplineTransform(positions);
        Matrix jacobian = transform.derivative(new Position2D(0.5, 0.5));

        assertEquals(2, jacobian.getNumRow());
        assertEquals(2, jacobian.getNumCol());
    }

    @Test
    public void testGetSourceDimensions() {
        List<Coordinate> source = Arrays.asList(
                new Coordinate(0, 0), new Coordinate(0, 100), new Coordinate(100, 0), new Coordinate(100, 100));

        List<Coordinate> target = Arrays.asList(
                new Coordinate(0, 0), new Coordinate(0, 100), new Coordinate(100, 0), new Coordinate(100, 100));

        ThinPlateSplineTransform transform = new ThinPlateSplineTransform(source, target);
        // this ThinPlateSplineTransform is striclty 2D
        assertEquals(2, transform.getSourceDimensions());
    }

    @Test
    public void testGetTargetDimensions() {
        List<Coordinate> source = Arrays.asList(
                new Coordinate(0, 0), new Coordinate(0, 100), new Coordinate(100, 0), new Coordinate(100, 100));

        List<Coordinate> target = Arrays.asList(
                new Coordinate(0, 0), new Coordinate(0, 100), new Coordinate(100, 0), new Coordinate(100, 100));

        ThinPlateSplineTransform transform = new ThinPlateSplineTransform(source, target);
        // this ThinPlateSplineTransform is striclty 2D
        assertEquals(2, transform.getTargetDimensions());
    }

}
