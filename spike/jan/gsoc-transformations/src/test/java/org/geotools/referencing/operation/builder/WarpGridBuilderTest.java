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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.gce.image.WorldImageWriter;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.DefaultMathTransformFactory;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.referencing.operation.transform.WarpGridTransform2D;
import org.geotools.referencing.operation.transform.WarpGridTransform2D.ProviderFile;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;


/**
 * @author jezekjan
 *
 */
public class WarpGridBuilderTest extends TestCase {
    private double tolerance = 0.03; //cm   
    DefaultEngineeringCRS l;
    private CoordinateReferenceSystem crs = DefaultEngineeringCRS.GENERIC_2D;
    private boolean show = false;
    private boolean write = true;
 
    private String path="/home/jezekjan/tmp/";
    /**
     * Run the suite from the command line.
     *
     * @param args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Returns the test suite.
     *
     * @return
     */
    public static Test suite() {
        return new TestSuite(WarpGridBuilderTest.class);
    }

    /**
     * Generates Mapped positions inside specified envelope.
     * @param env Envelope
     * @param number Number of points to be generated
     * @param deltas approximately the deltas between source and target point.
     * @return
     */
    private List<MappedPosition> generateMappedPositions(Envelope env, int number,
        double deltas, CoordinateReferenceSystem crs) {
        List<MappedPosition> vectors = new ArrayList<MappedPosition>();
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

    /**
     * Test of TPDWarpGridBuilder
     *
     */
    public void testIDWWarpGridBuilder() {
    	  try {
              // Envelope 20*20 km 00
              Envelope env = new Envelope2D(crs, -50, -50, 400000, 200000);
              
              // Generates 15 MappedPositions of approximately 2 m differences
              List<MappedPosition> mp = generateMappedPositions(env, 6, 2, crs);

              WarpGridBuilder builder = new IDWGridBuilder(mp, 5000, 5000, env);

              //gridTest(mp, builder.getMathTransform());
              GridCoverage2D dx  =  (new GridCoverageFactory()).create("idw - dx", builder.getDxGrid(), env);
              GridCoverage2D dy =  (new GridCoverageFactory()).create("idw - dy", builder.getDyGrid(), env);
                        
              if (show == true) {
              	dx.show();
              	dy.show();
              	 }
              
              if (write == true) {
              WorldImageWriter writerx = new WorldImageWriter((Object) (new File(
              		path+"idwdx.png")));
    
               writerx.write(dx, null);
               WorldImageWriter writery = new WorldImageWriter((Object) (new File(
               path+"idwdy.png")));
     
                writery.write(dy, null);
              }

              assertBuilder(builder);
              assertInverse(builder);
         
          } catch (Exception e) {
              e.printStackTrace();
          }
    }

    /**
     * Test of IDWDWarpGridBuilder
     *
     */
    public void testTPSWarpGridBuilder() {
        try {
           
        	
            Envelope env = new Envelope2D(crs, 0, 0, 2000, 3000 );

            // Generates 15 MappedPositions of approximately 2 m differences
            List<MappedPosition> mp = generateMappedPositions(env, 10, 1, crs);

            GeneralMatrix M = new GeneralMatrix(3, 3);
            double[] m0 = { 1, 0, 0 };
            double[] m1 = { 0, 1, 0 };
            double[] m2 = { 0, 0, 1 };
            M.setRow(0, m0);
            M.setRow(1, m1);
            M.setRow(2, m2);

            WarpGridBuilder builder = new TPSGridBuilder(mp, 10, 10, env,ProjectiveTransform.create(M));
            
            //builder.getDeltaFile(0, "/home/jezekjan/gridfile.txt");

            GridCoverage2D dx  =  (new GridCoverageFactory()).create("tps - dx", builder.getDxGrid(), env);
            GridCoverage2D dy =  (new GridCoverageFactory()).create("tps - dy", builder.getDyGrid(), env);
            
            if (show == true) {
            	dx.show();
            	dy.show();
            	 }
            
            if (write == true) {
            WorldImageWriter writerx = new WorldImageWriter((Object) (new File(
            path+"tpsdx.png")));
  
             writerx.write(dx, null);
             WorldImageWriter writery = new WorldImageWriter((Object) (new File(
             path+"tpsdy.png")));
   
              writery.write(dy, null);
            }
                      
           assertBuilder(builder);
           assertInverse(builder);
            
          
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void testRSGridBuilder() {
        try {
           
            Envelope env = new Envelope2D(crs, 0, 0, 1000, 1000);

            // Generates 15 MappedPositions of approximately 2 m differences
            List<MappedPosition> mp = generateMappedPositions(env, 15, 1, crs);

            GeneralMatrix M = new GeneralMatrix(3, 3);
            double[] m0 = { 1, 0, 0 };
            double[] m1 = { 0, 1, 0 };
            double[] m2 = { 0, 0, 1 };
            M.setRow(0, m0);
            M.setRow(1, m1);
            M.setRow(2, m2);

            WarpGridBuilder builder = new RSGridBuilder(mp, 3, 3, env,
                    ProjectiveTransform.create(M));

                      
            GridCoverage2D rubberdx  =  (new GridCoverageFactory()).create("RubberSheet - dx", builder.getDxGrid(), env);
            GridCoverage2D rubberdy =  (new GridCoverageFactory()).create("RubberSheet - dy", builder.getDyGrid(), env);
                      
            if (show == true) {
            	rubberdx.show();
            	rubberdy.show();
            	 }
            
            if (write == true) {
            WorldImageWriter writerx = new WorldImageWriter((Object) (new File(
            path+"/rubberdx.png")));
  
             writerx.write(rubberdx, null);
             WorldImageWriter writery = new WorldImageWriter((Object) (new File(
             path+"/rubberdy.png")));
   
              writery.write(rubberdy, null);
            }
            assertBuilder(builder);  
          
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void testFileProvider(){
    	   try {
               // Envelope 20*20 km 
               Envelope env = new Envelope2D(crs, 10, 10, 500, 500);
               GeneralMatrix M = new GeneralMatrix(3, 3);
                       

             //  WarpGridBuilder builder = new RSGridBuilder(mp, 3, 3, env,
             //          ProjectiveTransform.create(M));

               // Generates 15 MappedPositions of approximately 2 m differences
               List<MappedPosition> mp = generateMappedPositions(env, 15, 0.01, crs);

               WarpGridBuilder builder = new IDWGridBuilder(mp, 20, 20, env, ProjectiveTransform.create(M));

               final DefaultMathTransformFactory factory = new DefaultMathTransformFactory();
               ParameterValueGroup gridParams = factory.getDefaultParameters("Warp Grid (from file)");
               String pathx = path+"dx";
               String pathy = path+"dy";
               builder.writeDeltaFile(0, pathx);
               builder.writeDeltaFile(1, pathy);
               gridParams.parameter("X_difference_file").setValue(pathx);
               gridParams.parameter("Y_difference_file").setValue(pathy);
               MathTransform mt = (new ProviderFile()).createMathTransform(gridParams);
               MathTransform mtOriginal = builder.getMathTransform();
               
              
            	  
            	float[] wp = ( float[] )(((WarpGridTransform2D)mt).getParameterValues().parameter("warpPositions").getValue());
            	
            	float[] wpOrig = ( float[] )(((WarpGridTransform2D)mtOriginal).getParameterValues().parameter("warpPositions").getValue());
                
            	 for (int i = 0; i < wp.length; i++){
                  Assert.assertEquals(wp[i], wpOrig[i], 0.0001);
               }
         
           } catch (Exception e) {
               e.printStackTrace();
           }
    }

    /**
     * Tests that transformed source point fits to target point (considering tolerance).
     * @param builder
     */
    private void assertBuilder(MathTransformBuilder builder) {
        List<MappedPosition> mp = builder.getMappedPositions();

        try {
            for (int i = 0; i < mp.size(); i++) {
            	
                Assert.assertEquals(0,
                    ((MappedPosition) mp.get(i)).getError(builder.getMathTransform(), null),
                     tolerance);                          
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void assertInverse(MathTransformBuilder builder) {
        try {
            List<MappedPosition> mp = builder.getMappedPositions();

            for (int i = 0; i < mp.size(); i++) {
                MappedPosition p = (MappedPosition) mp.get(i);

                MappedPosition inversMp = new MappedPosition(p.getTarget(), p.getSource());
                //inversMp.add(new MappedPosition(p.getTarget(),p.getSource()));
             //   System.out.println(inversMp.getError(builder.getMathTransform().inverse(), null));
                Assert.assertEquals(0, inversMp.getError(builder.getMathTransform().inverse(), null),
                    tolerance);
            }
        } catch (NoninvertibleTransformException e) {
           
            e.printStackTrace();
        } catch (TransformException e) {
          
            e.printStackTrace();
        } catch (FactoryException e) {
          
            e.printStackTrace();
        }

        //builder.getMathTransform().inverse();
    }
    
    public static void gridTest(List<MappedPosition> mps, MathTransform trans) throws TransformException{
		double sum = 0;
		int j = 0;
		for(Iterator<MappedPosition> i = mps.iterator(); i.hasNext();){
			MappedPosition mp = i.next();
			DirectPosition2D test = new DirectPosition2D(0,0);
			trans.transform(mp.getSource(),test);
			
			Assert.assertEquals(mp.getTarget().getOrdinate(0), test.getOrdinate(0), 0.04);
			Assert.assertEquals(mp.getTarget().getOrdinate(1), test.getOrdinate(1), 0.04);
			 double dx =  Math.pow((mp.getTarget().getOrdinate(0)-test.getOrdinate(0)),2);
			 double dy =  Math.pow((mp.getTarget().getOrdinate(1)-test.getOrdinate(1)),2);
				
			 sum = sum + Math.pow((dx+dy),0.5);
				
			
					j++;
		}
	
	}
}
