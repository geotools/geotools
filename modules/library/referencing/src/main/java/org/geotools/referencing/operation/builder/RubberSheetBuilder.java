/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.vecmath.MismatchedSizeException;

import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.MismatchedReferenceSystemException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;


/**
 * Builds a RubberSheet transformation from a set of control points, defined as 
 * a List of 
 * {@linkplain org.geotools.referencing.operation.builder.MappedPosition MappedPosition}
 * objects, and a quadrilateral delimiting the outer area of interest, defined 
 * as a List of four 
 * {@linkplain org.opengis.geometry.DirectPosition DirectPosition} objects.
 * 
 * An explanation of the RubberSheet transformation algorithm can be seen 
 * <a href ="http://planner.t.u-tokyo.ac.jp/member/fuse/rubber_sheeting.pdf">here</a>.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Jan Jezek
 * @author Adrian Custer
 */
public class RubberSheetBuilder extends MathTransformBuilder {
    
    /* Map of the original and destination triangles. */
    private HashMap trianglesMap;
    
    /* Map of a original triangles and associated AffineTransformation.*/
    private HashMap trianglesToKeysMap;
    
/**
     * Creates the Builder from a List of control points and a List of four 
     * DirectPositions defining the vertexes of the area for interpolation. 
     * 
     * @param vectors A List of {@linkplain org.geotools.referencing.operation.builder.MappedPosition MappedPosition}
     * @param vertices A List with four points defining the quadrilateral in the region of interest.
     * 
     * @throws MismatchedSizeException
     * @throws MismatchedDimensionException
     * @throws MismatchedReferenceSystemException
     * @throws TriangulationException
     */
    public RubberSheetBuilder(List <MappedPosition> vectors, List <DirectPosition> vertices)
        throws MismatchedSizeException, MismatchedDimensionException, MismatchedReferenceSystemException, TriangulationException {
        
        //Validates the vectors parameter while setting it
        super.setMappedPositions(vectors);
        
        //Validate the vertices parameter
        if ( vertices.size() != 4){
            throw new IllegalArgumentException("The region of interest must have four vertices.");
        }
        
    	//Get the DirectPositions (In Java 1.4 we fail hard on this cast.) 
    	DirectPosition [] ddpp = new DirectPosition[4];
    	for (int i = 0; i< vertices.size(); i++){
    		ddpp[i] = (DirectPosition) vertices.get(i);
    	}
    	
	  	//Check they have a common crs;
	    CoordinateReferenceSystem crs;
	    try {
	        crs = getSourceCRS();
	    } catch (FactoryException e) {
	        // Can't fetch the CRS. Use the one from the first region of interest point instead.
	        crs = ddpp[0].getCoordinateReferenceSystem();
	    }
	    if ( !( CRS.equalsIgnoreMetadata( crs, ddpp[0].getCoordinateReferenceSystem() )
    		 || CRS.equalsIgnoreMetadata( crs, ddpp[1].getCoordinateReferenceSystem() )
    		 || CRS.equalsIgnoreMetadata( crs, ddpp[2].getCoordinateReferenceSystem() )
    		 || CRS.equalsIgnoreMetadata( crs, ddpp[3].getCoordinateReferenceSystem() )
    		 )
        ) {
          throw new MismatchedReferenceSystemException(
          		"Region of interest defined by mismatched DirectPositions.");
	    }
	    
	    //Check the vectors are inside the vertices. 
	    //  This is a quick check by envelope, can be more rigorous when we move 
	    //  to n dimensional operations.
	    DirectPosition[] dpa = this.getSourcePoints();
	    GeneralEnvelope srcextnt = new GeneralEnvelope(2);
	    for (int i = 0; i<dpa.length; i++){
	    	srcextnt.add(dpa[i]);
	    }
	    GeneralEnvelope vtxextnt = new GeneralEnvelope(2);
	    vtxextnt.add(ddpp[0]);
	    vtxextnt.add(ddpp[1]);
	    vtxextnt.add(ddpp[2]);
	    vtxextnt.add(ddpp[3]);
	    if (! vtxextnt.contains(srcextnt,true))
	    	throw new IllegalArgumentException("The region of interest must contain the control points");
        
        Quadrilateral quad = new Quadrilateral(ddpp[0],ddpp[1],ddpp[2],ddpp[3]);
        
        MapTriangulationFactory trianglemap = new MapTriangulationFactory(quad,vectors);
        
        this.trianglesMap = (HashMap) trianglemap.getTriangleMap();
        this.trianglesToKeysMap = mapTrianglesToKey();
    }
    
    /**
     * Returns the minimum number of points required by this builder.
     *
     * @return 1
     */
    public int getMinimumPointCount() {
        return 1;
    }

    /**
     * Returns the map of source and destination triangles.
     *
     * @return The Map of source and destination triangles.
     */
    public HashMap getMapTriangulation() {
        return trianglesMap;
    }
    
    /**
     * Returns MathTransform transformation setup as RubberSheet.
     *
     * @return calculated MathTransform
     *
     * @throws FactoryException when the size of source and destination point
     *         is not the same.
     */
    protected MathTransform computeMathTransform() throws FactoryException {
        return new RubberSheetTransform(trianglesToKeysMap);
    }
    
    

    
    /**
     * Calculates affine transformation parameters from the pair of triangles.
     *
     * @return The HashMap where the keys are the original triangles and values
     *         are AffineTransformation Objects.
     */
    private HashMap mapTrianglesToKey() {
        AffineTransformBuilder calculator;
        
        HashMap trianglesToKeysMap = (HashMap) trianglesMap.clone();

        Iterator it = trianglesToKeysMap.entrySet().iterator();

        while (it.hasNext()) {
            
            Map.Entry a = (Map.Entry) it.next();
            List pts = new ArrayList();

            for (int i = 1; i <= 3; i++) {
                pts.add(new MappedPosition(
                        ((TINTriangle) a.getKey()).getPoints()[i],
                        ((TINTriangle) a.getValue()).getPoints()[i]));
                
            }

            try {
                calculator = new AffineTransformBuilder(pts);
                a.setValue(calculator.getMathTransform());
            } catch (Exception e) {
                // should never reach here because AffineTransformBuilder(pts)
            	// should not throw any Exception.
            	e.printStackTrace();
            }
        }

        return trianglesToKeysMap;
    }
    
}
