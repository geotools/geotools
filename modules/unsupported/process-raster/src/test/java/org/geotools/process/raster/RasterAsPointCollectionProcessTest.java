/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2014, Open Source Geospatial Foundation (OSGeo)
 * (C) 2001-2014 TOPP - www.openplans.org.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.process.raster;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationBicubic;
import javax.media.jai.InterpolationBilinear;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import org.geotools.TestData;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geometry.jts.JTS;
import org.geotools.process.ProcessException;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.factory.GeoTools;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

public class RasterAsPointCollectionProcessTest {

    private static final String NORTH = "N";

    private static GridCoverage2D coverage;

    private static GridCoverage2D inputCoverage;

    private static int pixelNumber;

    private static RasterAsPointCollectionProcess process;

    private static String bandName;

    @BeforeClass
    public static void setup() throws FileNotFoundException, IOException {
        // Selection of the File to use
        File tiff = TestData.file(RasterAsPointCollectionProcessTest.class, "sample.tif");
        // Reading of the file with the GeoTiff reader
        AbstractGridFormat format = GridFormatFinder.findFormat(tiff);
        // Get a reader for the selected format
        GridCoverageReader reader = format.getReader(tiff);
        // Read the input Coverage
        inputCoverage = (GridCoverage2D) reader.read(null);
        // Reproject to the default WGS84 CRS
        final CoverageProcessor processor =
                CoverageProcessor.getInstance(GeoTools.getDefaultHints());
        final ParameterValueGroup param = processor.getOperation("Resample").getParameters();
        param.parameter("Source").setValue(inputCoverage);
        param.parameter("CoordinateReferenceSystem").setValue(DefaultGeographicCRS.WGS84);
        coverage = (GridCoverage2D) processor.doOperation(param);
        // Reader disposal
        reader.dispose();
        // Definition of the Image Size
        pixelNumber =
                coverage.getRenderedImage().getHeight() * coverage.getRenderedImage().getWidth();
        // Definition of the process
        process = new RasterAsPointCollectionProcess();
        // Definition of the Name of the unique GridSampleDimension
        bandName = coverage.getSampleDimension(0).getDescription().toString();
    }

    @Test(expected = ProcessException.class)
    public void testNullCoverage() {
        process.execute(null, null, null, null, false);
    }

    @Test
    public void testCoverageWithoutParam() throws MismatchedDimensionException, TransformException {
        // Execution of the RasterAsPointCollectionProcess without setting any additional parameter
        SimpleFeatureCollection collection = process.execute(coverage, null, null, null, false);
        // Check if the points are exactly as the number of pixel number
        Assert.assertEquals(pixelNumber, collection.size());
        // Check if each Point Attribute contains the same values of the Input coverage
        checkCollectionPoints(collection, false, null, null);
    }

    @Test
    public void testCoverageWithHemisphere()
            throws MismatchedDimensionException, TransformException {
        // Execution of the RasterAsPointCollectionProcess setting hemisphere to true
        boolean hemisphere = true;
        SimpleFeatureCollection collection =
                process.execute(coverage, null, null, null, hemisphere);
        // Check if the points are exactly as the number of pixel number
        Assert.assertEquals(pixelNumber, collection.size());
        // Check if each Point Attribute contains the same values of the Input coverage
        checkCollectionPoints(collection, hemisphere, null, null);
    }

    @Test
    public void testCoverageWithScaleFactor()
            throws MismatchedDimensionException, TransformException {
        // Execution of the RasterAsPointCollectionProcess setting hemisphere and scaleFactor
        boolean hemisphere = true;
        float scaleFactor = 2.0f;
        SimpleFeatureCollection collection =
                process.execute(coverage, null, scaleFactor, null, hemisphere);
        // Check if the points are exactly as the number of pixel number
        Assert.assertEquals((int) (pixelNumber * scaleFactor * scaleFactor), collection.size());
        // Check if each Point Attribute contains the same values of the Input coverage
        checkCollectionPoints(collection, hemisphere, scaleFactor, null);
    }

    @Test
    public void testCoverageWithSmallScaleFactor()
            throws MismatchedDimensionException, TransformException {
        // Execution of the RasterAsPointCollectionProcess setting hemisphere and scaleFactor
        boolean hemisphere = true;
        float scaleFactor = 1f / 18;
        SimpleFeatureCollection collection =
                process.execute(coverage, null, scaleFactor, null, hemisphere);
        // Check if the points are exactly as the number of pixel number
        Assert.assertEquals(1, collection.size());
        // Check if each Point Attribute contains the same values of the Input coverage
        checkCollectionPoints(collection, hemisphere, scaleFactor, null);
    }

    @Test
    public void testCoverageWithNearestInterp()
            throws MismatchedDimensionException, TransformException {
        // Execution of the RasterAsPointCollectionProcess setting hemisphere, scaleFactor and
        // nearest interpolation
        boolean hemisphere = true;
        float scaleFactor = 2.0f;
        Interpolation interp = new InterpolationNearest();
        SimpleFeatureCollection collection =
                process.execute(coverage, null, scaleFactor, interp, hemisphere);
        // Check if the points are exactly as the number of pixel number
        Assert.assertEquals((int) (pixelNumber * scaleFactor * scaleFactor), collection.size());
        // Check if each Point Attribute contains the same values of the Input coverage
        checkCollectionPoints(collection, hemisphere, scaleFactor, null);
    }

    @Test
    public void testCoverageWithBilinearInterp()
            throws MismatchedDimensionException, TransformException {
        // Execution of the RasterAsPointCollectionProcess setting hemisphere, scaleFactor and
        // bilinear interpolation
        boolean hemisphere = true;
        float scaleFactor = 2.0f;
        Interpolation interp = new InterpolationBilinear();
        SimpleFeatureCollection collection =
                process.execute(coverage, null, scaleFactor, interp, hemisphere);
        // Check if the points are exactly as the number of pixel number
        Assert.assertEquals((int) (pixelNumber * scaleFactor * scaleFactor), collection.size());
        // Check if each Point Attribute contains the same values of the Input coverage
        checkCollectionPoints(collection, hemisphere, scaleFactor, null);
    }

    @Test
    public void testCoverageWithBicubicInterp()
            throws MismatchedDimensionException, TransformException {
        // Execution of the RasterAsPointCollectionProcess setting hemisphere, scaleFactor and
        // bicubic interpolation
        boolean hemisphere = true;
        float scaleFactor = 2.0f;
        Interpolation interp = new InterpolationBicubic(8);
        SimpleFeatureCollection collection =
                process.execute(coverage, null, scaleFactor, interp, hemisphere);
        // Check if the points are exactly as the number of pixel number
        Assert.assertEquals((int) (pixelNumber * scaleFactor * scaleFactor), collection.size());
        // Check if each Point Attribute contains the same values of the Input coverage
        checkCollectionPoints(collection, hemisphere, scaleFactor, null);
    }

    @Test
    public void testCoverageWithTargetCRS()
            throws MismatchedDimensionException, TransformException, FactoryException {
        // Execution of the RasterAsPointCollectionProcess setting hemisphere, scaleFactor, nearest
        // interpolation and targetCRS
        boolean hemisphere = true;
        float scaleFactor = 2.0f;
        Interpolation interp = new InterpolationNearest();
        // Selection of the Lambert Conformal Conic CRS
        String wkt =
                "PROJCS[\"Lambert_Conformal_Conic\","
                        + "GEOGCS[\"GCS_unknown\",DATUM[\"D_unknown\","
                        + "SPHEROID[\"Sphere\",6367470,0]],PRIMEM[\"Greenwich\",0],"
                        + "UNIT[\"Degree\",0.017453292519943295]],"
                        + "PROJECTION[\"Lambert_Conformal_Conic_1SP\"],"
                        + "PARAMETER[\"latitude_of_origin\",38.5],"
                        + "PARAMETER[\"central_meridian\",-97.5],"
                        + "PARAMETER[\"scale_factor\",1],"
                        + "PARAMETER[\"false_easting\",0],"
                        + "PARAMETER[\"false_northing\",0],UNIT[\"m\",1.0]]";
        CoordinateReferenceSystem targetCRS = CRS.parseWKT(wkt);
        SimpleFeatureCollection collection =
                process.execute(coverage, targetCRS, scaleFactor, interp, hemisphere);
        // Check if the points are exactly as the number of pixel number
        Assert.assertEquals((int) (pixelNumber * scaleFactor * scaleFactor), collection.size());
        // Check if each Point Attribute contains the same values of the Input coverage
        checkCollectionPoints(collection, hemisphere, scaleFactor, targetCRS);
    }

    @Test
    public void testCoverageDifferentCRS()
            throws MismatchedDimensionException, TransformException, FactoryException {
        // Execution of the RasterAsPointCollectionProcess setting hemisphere, scaleFactor, nearest
        // interpolation and targetCRS
        boolean hemisphere = true;
        float scaleFactor = 2.0f;
        Interpolation interp = new InterpolationNearest();
        // Selection of the Lambert Conformal Conic CRS
        String wkt =
                "PROJCS[\"Lambert_Conformal_Conic\","
                        + "GEOGCS[\"GCS_unknown\",DATUM[\"D_unknown\","
                        + "SPHEROID[\"Sphere\",6367470,0]],PRIMEM[\"Greenwich\",0],"
                        + "UNIT[\"Degree\",0.017453292519943295]],"
                        + "PROJECTION[\"Lambert_Conformal_Conic_1SP\"],"
                        + "PARAMETER[\"latitude_of_origin\",38.5],"
                        + "PARAMETER[\"central_meridian\",-97.5],"
                        + "PARAMETER[\"scale_factor\",1],"
                        + "PARAMETER[\"false_easting\",0],"
                        + "PARAMETER[\"false_northing\",0],UNIT[\"m\",1.0]]";
        CoordinateReferenceSystem targetCRS = CRS.parseWKT(wkt);
        // The input coverage is not in the Default WGS84 CRS
        SimpleFeatureCollection collection =
                process.execute(inputCoverage, targetCRS, scaleFactor, interp, hemisphere);
        // Check if the points are exactly as the number of pixel number
        Assert.assertEquals((int) (pixelNumber * scaleFactor * scaleFactor), collection.size());
        // Check if each Point Attribute contains the same values of the Input coverage
        checkCollectionPoints(collection, hemisphere, scaleFactor, targetCRS);
    }

    private void checkCollectionPoints(
            SimpleFeatureCollection collection,
            boolean hemisphere,
            Float scaleFactor,
            CoordinateReferenceSystem targetCRS)
            throws MismatchedDimensionException, TransformException {
        // World2Grid transform associated to the coverage
        MathTransform2D w2g =
                coverage.getGridGeometry().getCRSToGrid2D(PixelOrientation.UPPER_LEFT);
        // Iterator on the FeatureCollection
        SimpleFeatureIterator it = collection.features();
        // Iterator on the input image
        RandomIter imageIterator = RandomIterFactory.create(coverage.getRenderedImage(), null);
        // Boolean indicating that the TargetCRS is not null
        boolean crsExists = targetCRS != null;
        // Cycle on the Collection
        try {
            while (it.hasNext()) {
                // Selection of the feature
                SimpleFeature ft = it.next();
                // If the scale factor is more than 1 then no comparison between the values is done
                // due
                // to possible differences on the interpolation
                if (scaleFactor == null) {
                    // Selection of the associated point
                    Point point = (Point) ft.getDefaultGeometry();
                    Point rasterPoint = (Point) JTS.transform(point, w2g);
                    int x = (int) (rasterPoint.getX());
                    int y = (int) (rasterPoint.getY());
                    // Selection of the value for the single band for the selected position
                    int sampleIMG = imageIterator.getSample(x, y, 0);
                    int sampleColl = (Short) ft.getAttribute(bandName);
                    // Ensure the values are equal
                    Assert.assertEquals(sampleIMG, sampleColl);
                }
                // Check the hemisphere
                if (hemisphere) {
                    Assert.assertEquals(NORTH, ft.getAttribute("emisphere"));
                }
                // Check the GridConvergenceAngle
                if (crsExists) {
                    double angle = (double) ft.getAttribute("gridConvergenceAngleCorrection");
                    Assert.assertTrue(angle != 0);
                }
            }
        } finally {
            if (it != null) {
                it.close();
            }
        }
    }
}
