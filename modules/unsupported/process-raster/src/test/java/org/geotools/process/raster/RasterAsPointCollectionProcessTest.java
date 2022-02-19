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

import static org.junit.Assert.assertNotEquals;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationBicubic;
import javax.media.jai.InterpolationBilinear;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import org.geotools.TestData;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.util.FeatureUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.map.RasterLayer;
import org.geotools.process.ProcessException;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.util.factory.GeoTools;
import org.geotools.xml.styling.SLDParser;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

public class RasterAsPointCollectionProcessTest {

    static class GridCoverageReaderLayer extends RasterLayer {

        GridCoverage2DReader reader;
        GeneralParameterValue[] params;

        public GridCoverageReaderLayer(
                GridCoverage2DReader reader, Style style, GeneralParameterValue[] params) {
            super(style);
            this.params = params;
            this.reader = reader;
        }

        @Override
        public SimpleFeatureCollection toFeatureCollection() {
            SimpleFeatureCollection collection;
            try {
                collection = FeatureUtilities.wrapGridCoverageReader(reader, params);
                return collection;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public ReferencedEnvelope getBounds() {
            return new ReferencedEnvelope(reader.getOriginalEnvelope());
        }
    }

    private static final String NORTH = "N";

    private static final String SOUTH = "S";

    private static CoverageProcessor processor;

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
        processor = CoverageProcessor.getInstance(GeoTools.getDefaultHints());
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

    @Test
    public void testCoverageInNorthEastCoordinatesNorthern() throws Exception {
        // Load the test coverage cropped to the northern hemisphere and
        // reprojected to LatLon coordinates
        String wkt =
                "GEOGCS[\"WGS 84\","
                        + "DATUM[\"World Geodetic System 1984\","
                        + "SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]],"
                        + "AUTHORITY[\"EPSG\",\"6326\"]],"
                        + "PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]],"
                        + "UNIT[\"degree\", 0.017453292519943295],"
                        + "AXIS[\"Geodetic latitude\", NORTH],"
                        + "AXIS[\"Geodetic longitude\", EAST],"
                        + "AUTHORITY[\"EPSG\",\"4326\"]]";
        CoordinateReferenceSystem crs = CRS.parseWKT(wkt);
        GridCoverage2D coverage = readCropAndResampleCoverage(-180, 180, 0, 90, crs);
        // Execution of the RasterAsPointCollectionProcess setting hemisphere
        SimpleFeatureCollection collection = process.execute(coverage, null, null, null, true);
        // Check if each point is in the northern hemisphere
        assertPointsInHemisphere(coverage, collection, NORTH);
    }

    @Test
    public void testCoverageInNorthEastCoordinatesSouthern() throws Exception {
        // Load the test coverage cropped to the southern hemisphere and
        // reprojected to LatLon coordinates
        String wkt =
                "GEOGCS[\"WGS 84\","
                        + "DATUM[\"World Geodetic System 1984\","
                        + "SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]],"
                        + "AUTHORITY[\"EPSG\",\"6326\"]],"
                        + "PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]],"
                        + "UNIT[\"degree\", 0.017453292519943295],"
                        + "AXIS[\"Geodetic latitude\", NORTH],"
                        + "AXIS[\"Geodetic longitude\", EAST],"
                        + "AUTHORITY[\"EPSG\",\"4326\"]]";
        CoordinateReferenceSystem crs = CRS.parseWKT(wkt);
        GridCoverage2D coverage = readCropAndResampleCoverage(-180, 180, -90, -1, crs);
        // Execution of the RasterAsPointCollectionProcess setting hemisphere
        SimpleFeatureCollection collection = process.execute(coverage, null, null, null, true);
        // Check if each point is in the southern hemisphere
        assertPointsInHemisphere(coverage, collection, SOUTH);
    }

    @Test
    public void testCoverageInPolarStereographicProjectionsNorthern() throws Exception {
        // Load the test coverage cropped to the northern hemisphere polar stereographic
        // projection area of validity and reprojected to the projection
        CoordinateReferenceSystem crs = CRS.decode("EPSG:5041");
        GridCoverage2D coverage = readCropAndResampleCoverage(-180, 180, 60, 90, crs);
        // Execution of the RasterAsPointCollectionProcess setting hemisphere
        SimpleFeatureCollection collection = process.execute(coverage, null, null, null, true);
        // Check if each point is in the northern hemisphere
        assertPointsInHemisphere(coverage, collection, NORTH);
    }

    @Test
    public void testCoverageInPolarStereographicProjectionsSouthern() throws Exception {
        // Load the test coverage cropped to the southern hemisphere polar stereographic
        // projection area of validity and reprojected to the projection
        CoordinateReferenceSystem crs = CRS.decode("EPSG:5042");
        GridCoverage2D coverage = readCropAndResampleCoverage(-180, 180, -90, -60, crs);
        // Execution of the RasterAsPointCollectionProcess setting hemisphere
        SimpleFeatureCollection collection = process.execute(coverage, null, null, null, true);
        // Check if each point is in the southern hemisphere
        assertPointsInHemisphere(coverage, collection, SOUTH);
    }

    private GridCoverage2D readCropAndResampleCoverage(
            double x1, double x2, double y1, double y2, CoordinateReferenceSystem outCRS)
            throws IOException {
        // Read the global coverage in LonLat coordinates
        GeoTiffReader reader = new GeoTiffReader(TestData.file(this, "current.tif"));
        GridCoverage2D coverage = reader.read(null);
        reader.dispose();
        // Crop the global coverage to the specified envelope
        CoordinateReferenceSystem inCRS = coverage.getCoordinateReferenceSystem();
        ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();
        param.parameter("Source").setValue(coverage);
        param.parameter("Envelope").setValue(new ReferencedEnvelope(x1, x2, y1, y2, inCRS));
        // Resample the coverage to to the specified coordinate system
        coverage = (GridCoverage2D) processor.doOperation(param);
        param = processor.getOperation("Resample").getParameters();
        param.parameter("Source").setValue(coverage);
        param.parameter("CoordinateReferenceSystem").setValue(outCRS);
        return (GridCoverage2D) processor.doOperation(param);
    }

    private static void assertPointsInHemisphere(
            GridCoverage2D coverage, SimpleFeatureCollection collection, String hemisphere) {
        // Check if the points are exactly as the number of pixel number
        int pixelNumber =
                coverage.getRenderedImage().getHeight() * coverage.getRenderedImage().getWidth();
        Assert.assertEquals(pixelNumber, collection.size());
        // Check if each point is in the correct hemisphere
        try (SimpleFeatureIterator it = collection.features()) {
            while (it.hasNext()) {
                Assert.assertEquals(hemisphere, it.next().getAttribute("emisphere"));
            }
        }
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
        // Iterator on the input image
        // Boolean indicating that the TargetCRS is not null
        // Cycle on the Collection
        try (SimpleFeatureIterator it = collection.features()) {
            RandomIter imageIterator = RandomIterFactory.create(coverage.getRenderedImage(), null);
            boolean crsExists = targetCRS != null;
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
                    Assert.assertNotEquals(0, angle);
                }
            }
        }
    }

    @Test
    public void testRasterToTransformVectorWrapping() throws Exception {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        java.net.URL surl = TestData.getResource(this, "arrows.sld");
        SLDParser stylereader = new SLDParser(factory, surl);
        Style style = stylereader.readXML()[0];

        GeoTiffReader reader = new GeoTiffReader(TestData.file(this, "current.tif"));

        MapContent mc = new MapContent();
        ParameterValue<String> suggestedTileSize = GeoTiffFormat.SUGGESTED_TILE_SIZE.createValue();
        suggestedTileSize.setValue("512,512");

        mc.addLayer(
                new GridCoverageReaderLayer(
                        reader, style, new GeneralParameterValue[] {suggestedTileSize}));

        StreamingRenderer renderer = new StreamingRenderer();
        Map<Object, Object> rendererParams = new HashMap<>();
        rendererParams.put(StreamingRenderer.ADVANCED_PROJECTION_HANDLING_KEY, true);
        rendererParams.put(StreamingRenderer.CONTINUOUS_MAP_WRAPPING, true);
        renderer.setRendererHints(rendererParams);
        renderer.setMapContent(mc);

        // Request a region a couple of times away from the classic 180° dateline, spanning the
        // world several times
        ReferencedEnvelope reWgs84 =
                new ReferencedEnvelope(800, 1880, -35, 35, CRS.decode("EPSG:4326", true));
        ReferencedEnvelope re = reWgs84.transform(CRS.decode("EPSG:3857"), true);

        final int w = 1080;
        final int h = 70;
        final BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        Rectangle paintArea = new Rectangle(0, 0, w, h);

        renderer.paint(
                (Graphics2D) g,
                paintArea,
                re,
                RendererUtilities.worldToScreenTransform(re, paintArea));
        final int reducedWidth = 360;
        final int reducedHeight = h;
        final int minX = w - reducedWidth;

        Raster raster = image.getData(new Rectangle(minX, 0, reducedWidth, reducedHeight));

        int blackSamples = 0;
        int graySamples = 0;
        for (int i = 0; i < reducedWidth; i++) {
            for (int j = 0; j < reducedHeight; j++) {
                blackSamples += raster.getSample(minX + i, j, 0) == 0 ? 1 : 0;
                graySamples += raster.getSample(minX + i, j, 0) == 128 ? 1 : 0;
            }
        }
        // Check that we aren't getting a whole white image on a big part of the rightern
        // side of the image. this was happening before the fix on wrapping on rendering
        // transformation since it was only rendering a smaller area (NO wrapping at all)
        assertNotEquals(0, blackSamples);
        // Confirm that the NODATA values (-32767.0 in the test image) were preserved
        // by checking for samples in the image that are not black (non-NODATA values)
        // or white (background).
        assertNotEquals(0, graySamples);
    }
}
