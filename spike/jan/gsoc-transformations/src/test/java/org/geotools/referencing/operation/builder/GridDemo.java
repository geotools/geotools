/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.builder;

import java.awt.Color;
import java.awt.image.RenderedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.media.jai.RenderedOp;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.processing.AbstractProcessor;
import org.geotools.coverage.processing.DefaultProcessor;
import org.geotools.coverage.processing.Operations;
import org.geotools.factory.Hints;
import org.geotools.gce.image.WorldImageReader;
import org.geotools.gce.image.WorldImageWriter;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.crs.DefaultDerivedCRS;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.cs.DefaultCartesianCS;
import org.geotools.referencing.operation.DefaultOperationMethod;
import org.geotools.referencing.operation.builder.algorithm.AbstractInterpolation;
import org.geotools.referencing.operation.builder.algorithm.TPSInterpolation;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;


public class GridDemo {
    private static List /*<MappedPositions>*/ generateMappedPositions(Envelope env, int number,
        double deltas, CoordinateReferenceSystem crs) {
    	crs = DefaultEngineeringCRS.CARTESIAN_2D;
        List /*<MappedPositions>*/ vectors = new ArrayList();
        double minx = env.getLowerCorner().getCoordinates()[0];
        double miny = env.getLowerCorner().getCoordinates()[1];

        double maxx = env.getUpperCorner().getCoordinates()[0];
        double maxy = env.getUpperCorner().getCoordinates()[1];

        final Random random = new Random(8578348921369L);

        for (int i = 0; i < number; i++) {
            double x = minx + (random.nextDouble() * (maxx - minx));
            double y = miny + (random.nextDouble() * (maxy - miny));
            vectors.add(new MappedPosition(new DirectPosition2D(crs, x, y),
                    new DirectPosition2D(crs,
                        (x + (random.nextDouble() * deltas)) - (random.nextDouble() * deltas),
                        (y + (random.nextDouble() * deltas)) - (random.nextDouble() * deltas))));
        }

        return vectors;
    }

    private static HashMap /*<MappedPositions>*/ generatePositionsWithValues(Envelope env,
        int number, double approxValue) {
        HashMap positions = new HashMap();
        double minx = env.getLowerCorner().getCoordinates()[0];
        double miny = env.getLowerCorner().getCoordinates()[1];

        double maxx = env.getUpperCorner().getCoordinates()[0];
        double maxy = env.getUpperCorner().getCoordinates()[1];

        final Random random = new Random(8578348921369L);

        for (int i = 0; i < number; i++) {
            double x = minx + (random.nextDouble() * (maxx - minx));
            double y = miny + (random.nextDouble() * (maxy - miny));
            positions.put(new DirectPosition2D(env.getCoordinateReferenceSystem(), x, y),
                random.nextDouble() * approxValue);
        }

        return positions;
    }

    static public GridCoverage2D generateCoverage2D(int row, int cells, Envelope env) {
        float[][] raster = new float[row][cells];

        for (int j = 0; j < row; j++) {
            for (int i = 0; i < cells; i++) {
                raster[j][i] = 0;
            }
        }

        for (int j = 1; j < (row - 1); j = j + 20) {
            for (int i = 1; i < (cells - 1); i++) {
                raster[j][i] = 100;
                raster[j + 1][i] = 60;
                raster[j - 1][i] = 60;
            }
        }

        for (int j = 1; j < (row - 1); j++) {
            for (int i = 1; i < (cells - 1); i = i + 20) {
                raster[j][i] = 100;
                raster[j][i + 1] = 60;
                raster[j][i - 1] = 60;
            }
        }

        GridCoverage2D cov = (new GridCoverageFactory()).create("name", raster, env);

        return cov;
    }

    public static void main2(String[] args) {
        try {
            // Prepare Coordinate System and Evelope
            CoordinateReferenceSystem crs = DefaultEngineeringCRS.CARTESIAN_2D;

            DirectPosition minDp = new DirectPosition2D(crs, 10.0, 10.0);
            DirectPosition maxDp = new DirectPosition2D(crs, 1000.0, 1000.0);

            Envelope env = new GeneralEnvelope(new GeneralDirectPosition(minDp),
                    new GeneralDirectPosition(maxDp));

            // Lets Generate some known points that will define interpolation
            HashMap /*<DirectPosition2D, Float>*/ pointsAndValues = new HashMap();

            pointsAndValues.put(new DirectPosition2D(crs, 130, 805), 6.5);
            pointsAndValues.put(new DirectPosition2D(crs, 14, 105), 1.5);
            pointsAndValues.put(new DirectPosition2D(crs, 45, 78), -9.5);
            pointsAndValues.put(new DirectPosition2D(crs, 905, 28), 7.5);
            pointsAndValues.put(new DirectPosition2D(crs, 123, 185), 16.5);
            pointsAndValues.put(new DirectPosition2D(crs, 104, 215), -21.5);
            pointsAndValues.put(new DirectPosition2D(crs, 45, 708), -9.5);
            pointsAndValues.put(new DirectPosition2D(crs, 905, 350), 17.5);
            pointsAndValues.put(new DirectPosition2D(crs, 905, 850), -45.5);

            //now we can construct the Interpolation Object  
            TPSInterpolation interpolation = new TPSInterpolation(pointsAndValues, 2, 2, env);

            // we can get and show coverage 
            (new GridCoverageFactory()).create("Intepolated Coverage", interpolation.get2DGrid(),
                env).show();

            // or we can interpolate value in any DirectPosition
            System.out.print(interpolation.getValue(new DirectPosition2D(12.34, 15.123)));

            /* AbstractInterpolation interpoaltion = new TPSInterpolation(
               generatePositionsWithValues(env,15, 5),
               env.getLength(0)/500,
               env.getLength(1)/500,
               env);*/
            (new GridCoverageFactory()).create("Intepoalted Coverage", interpolation.get2DGrid(),
                env).show();

            AbstractInterpolation interp = new TPSInterpolation(generatePositionsWithValues(env,
                        15, 5));
            
            Color[] colors = new Color[] {Color.BLUE, Color.CYAN, Color.WHITE, Color.YELLOW, Color.RED};          
            GridCoverage2D c= (new GridCoverageFactory()).create("Intepolated Coverage",  interpolation.getRaster (), interpolation.getEnv(),
                                                    null, null, null, new Color[][] {colors}, null);
                    
           
            
            
            WorldImageWriter writer = new WorldImageWriter((Object) (new File(
            		"/home/jezekjan/WDokumenty/geodata/rasters/p1010099.jpg")));// "/home/jezekjan/gsoc/geodata/p.tif")));
          
            writer.write( c,null);
           
            //(new GridCoverageFactory()).create("Intepoalted Coverage", interpolation.get2DGrid(),
        //env), null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CoordinateReferenceSystem realCRS = null;

        try {
            //  MathTransform2D realToGrid = gridShift.getMathTransform();
            realCRS = DefaultGeographicCRS.WGS84;

            URL url = null;

            url = new File("/home/jezekjan/tmp/testgeodata/rasters/p1010099.tif").toURI().toURL();

            // url = new File("/media/sda5/Dokumenty/geodata/rasters/Mane_3_1_4.tif").toURI().toURL();

            /* Open the file with Image */
            WorldImageReader reader = new WorldImageReader(url);
            Operations operations = new Operations(null);
            GridCoverage2D coverage = (GridCoverage2D) reader.read(null);
            Envelope env = coverage.getEnvelope();

            //coverage = GridCoverageExamples.getExample(0);

            //   List vectors = generateMappedPositions(env,15, 0.58, DefaultEngineeringCRS.CARTESIAN_2D);
            List vectors = generateMappedPositions(env, 15, 0.158,
                    env.getCoordinateReferenceSystem());

            //  System.out.println(env.getCoordinateReferenceSystem().getCoordinateSystem().getClass());
            //  WarpGridBuilder gridBuilder = new TPSGridBuilder(vectors, 0.01,0.01, env, coverage.getGridGeometry().getGridToCRS().inverse());

            // System.out.println(DefaultEngineeringCRS.CARTESIAN_2D.getCoordinateSystem().getClass().isAssignableFrom(DefaultCartesianCS.class));            

            //    MathTransformBuilder gridBuilder = new AffineTransformBuilder(vectors);//, env);

            /*
             * Construct WarpGrod Builder - assuming we are having some known vectors (MappedPositions)
             * that should deffine the source and target points
             * We also have to set the column size of controlling grid that is going to be generated
             * Within this grid there will be just approximative billiner interpalation used.
             */
            WarpGridBuilder gridBuilder = new TPSGridBuilder(vectors, 2, 2, env,
                    coverage.getGridGeometry().getGridToCRS().inverse());

            SimilarTransformBuilder builder =  new SimilarTransformBuilder(vectors);
            
            /* Get new transformation from builder */
            //  (new GridCoverageFactory()).create("DX", gridBuilder.getDxGrid(), coverage.getEnvelope())
            // .show();
           // (new GridCoverageFactory()).create("DY", gridBuilder.getDyGrid(), coverage.getEnvelope())
            // .show();

            /* Get new transformation from builder */
            MathTransform trans =  gridBuilder.getMathTransform();//gridBuilder.getMathTransform();

            System.out.println(trans.getSourceDimensions());
            System.out.println(trans.getTargetDimensions());

            /* Make New reference System */
            CoordinateReferenceSystem gridCRS = new DefaultDerivedCRS("gridCRS",
                    new DefaultOperationMethod(trans), coverage.getCoordinateReferenceSystem(),
                    trans, DefaultCartesianCS.GENERIC_2D);

            //////******************Show Source***************************///////

            // coverage.show();
            /* Reproject the image */
            AbstractProcessor processor = AbstractProcessor.getInstance();
            coverage = coverage.geophysics(false);

            final ParameterValueGroup param = processor.getOperation("Resample").getParameters();

            param.parameter("Source").setValue(coverage);
            param.parameter("CoordinateReferenceSystem").setValue(gridCRS);
            param.parameter("InterpolationType").setValue("bilinear");

            GridCoverage2D projected = (GridCoverage2D) processor.doOperation(param);
            final RenderedImage image = projected.getRenderedImage();
            projected = projected.geophysics(false);
                        
            
            WorldImageWriter writer = new WorldImageWriter((Object) (new File(
                                "/home/jezekjan/tmp/pp.png")));
                    writer.write(projected, null);
            
                    url = new File("/home/jezekjan/tmp/pp.png").toURI().toURL();

                    // url = new File("/media/sda5/Dokumenty/geodata/rasters/Mane_3_1_4.tif").toURI().toURL();

                    /* Open the file with Image */
                    WorldImageReader preader = new WorldImageReader(url);
                    GridCoverage2D pcoverage = (GridCoverage2D) preader.read(null);
            
          

           /* 
               Envelope envelope = CRS.transform(coverage.getGridGeometry().getGridToCRS().inverse(),
                       coverage.getEnvelope());
               GridCoverage2D target1 = projectTo((GridCoverage2D) coverage, gridCRS,
                       (GridGeometry2D) coverage.getGridGeometry(), null, false);
               target1.show();*/
             //  WorldImageWriter writer = new WorldImageWriter((Object) (new File(
               //            "/home/jezekjan/gsoc/geodata/p.tif")));
              // writer.write(projected, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static GridCoverage2D projectTo(final GridCoverage2D coverage,
        final CoordinateReferenceSystem targetCRS, final GridGeometry2D geometry,
        final Hints hints, final boolean useGeophysics) {
        final AbstractProcessor processor = (hints != null) ? new DefaultProcessor(hints)
                                                            : AbstractProcessor.getInstance();
        final String arg1;
        final Object value1;
        final String arg2;
        final Object value2;

        if (targetCRS != null) {
            arg1 = "CoordinateReferenceSystem";
            value1 = targetCRS;

            if (geometry != null) {
                arg2 = "GridGeometry";
                value2 = geometry;
            } else {
                arg2 = "InterpolationType";
                value2 = "bilinear";
            }
        } else {
            arg1 = "GridGeometry";
            value1 = geometry;
            arg2 = "InterpolationType";
            value2 = "bilinear";
        }

        GridCoverage2D projected = coverage.geophysics(useGeophysics);
        final ParameterValueGroup param = processor.getOperation("Resample").getParameters();
        param.parameter("Source").setValue(projected);
        param.parameter(arg1).setValue(value1);
        param.parameter(arg2).setValue(value2);

        projected = (GridCoverage2D) processor.doOperation(param);

        final RenderedImage image = projected.getRenderedImage();
        projected = projected.geophysics(false);

        String operation = null;

        if (image instanceof RenderedOp) {
            operation = ((RenderedOp) image).getOperationName();
            AbstractProcessor.LOGGER.fine("Applied \"" + operation + "\" JAI operation.");
        }

        // Viewer.show(projected, operation);
        return projected;

        // return operation;
    }
}
