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

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.MismatchedSizeException;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.CRS;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.MismatchedReferenceSystemException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchIdentifierException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;


public class RSGridBuilder extends WarpGridBuilder {
  

	private RubberSheetBuilder rsBuilder;
	
	private List<DirectPosition> quad;

    /**
     * Builds controlling Grid using RubberSheet Transformation
     * @param vectors
     * @param dx
     * @param dy
     * @param envelope
     * @param realToGrid
     * @throws MismatchedSizeException
     * @throws MismatchedDimensionException
     * @throws MismatchedReferenceSystemException
     * @throws TransformException
     */
    public RSGridBuilder(List<MappedPosition> vectors, double dx, double dy, Envelope envelope,
        MathTransform realToGrid)
        throws MismatchedSizeException, MismatchedDimensionException, NoSuchIdentifierException,
            MismatchedReferenceSystemException, TransformException, TriangulationException {
        super(vectors, dx, dy, envelope, realToGrid);

        Envelope gridEnvelope = CRS.transform(worldToGrid, envelope);
             
        double enlarge = gridEnvelope.getLength(0)*0.01;
        DirectPosition p0 = new DirectPosition2D(
        		gridEnvelope.getLowerCorner().getOrdinate(0)-enlarge,
        		gridEnvelope.getLowerCorner().getOrdinate(1)-enlarge);
        
        DirectPosition p2 = new DirectPosition2D(
        		gridEnvelope.getUpperCorner().getOrdinate(0)+enlarge,
        		gridEnvelope.getUpperCorner().getOrdinate(1)+enlarge);              

        DirectPosition p1 = new DirectPosition2D(
                p0.getOrdinate(0), p2.getOrdinate(1));
        DirectPosition p3 = new DirectPosition2D(
                p2.getOrdinate(0), p0.getOrdinate(1));
        
        List<MappedPosition> gridMP = super.getGridMappedPositions();
        CoordinateReferenceSystem crs = ((MappedPosition)gridMP.get(0)).getSource().getCoordinateReferenceSystem();
        p0 = new DirectPosition2D(crs, p0.getOrdinate(0), p0.getOrdinate(1));
        p1 = new DirectPosition2D(crs, p1.getOrdinate(0), p1.getOrdinate(1));
        p2 = new DirectPosition2D(crs, p2.getOrdinate(0), p2.getOrdinate(1));
        p3 = new DirectPosition2D(crs, p3.getOrdinate(0), p3.getOrdinate(1));
        
        
        quad = new ArrayList<DirectPosition>(); 
        quad.add(p0);//new Quadrilateral(p0, p1, p2, p3); 
        quad.add(p1);
        quad.add(p2);
        quad.add(p3);
        rsBuilder = new RubberSheetBuilder(super.getGridMappedPositions(), quad);
    }

    /**
     * Generates grid of source points.
     * @param values general values of grid
     * @return generated grid
     */
    private float[] generateSourcePoints(GridParameters gridParams) {
        float[] sourcePoints = ((float[]) gridParams.getWarpPositions());//values.parameter("warpPositions").getValue());

        for (int i = 0; i <= gridParams.getYNumber(); i++) {
            for (int j = 0; j <= gridParams.getXNumber(); j++) {
                float x = new Double((j * gridParams.getXStep())
                    + gridParams.getXStart()).floatValue();
                float y = new Double((i * gridParams.getYStep())
                    + gridParams.getYStart()).floatValue();

                sourcePoints[(i * ((1 + gridParams.getXNumber()) * 2)) + (2 * j)] = (float) x;

                sourcePoints[(i * ((1 + gridParams.getXNumber()) * 2)) + (2 * j)
                + 1] = (float) y;
            }
        }

        return sourcePoints;
    }

    /**
     * Computes target grid.
     * @return computed target grid.
     */
    protected float[] computeWarpGrid(GridParameters gridParams) throws FactoryException {
        float[] source = generateSourcePoints(gridParams);

      
      try {
		rsBuilder.getMathTransform().transform(source, 0, source, 0, (source.length + 1) / 2);
	} catch (TransformException e) {
		  throw new FactoryException(Errors.format(ErrorKeys.CANT_TRANSFORM_VALID_POINTS), e);			       			
	} 
       

        return source;
    }   
       

	
	public void setMappedPositions(List<MappedPosition> positions)
			throws MismatchedSizeException, MismatchedDimensionException,
			MismatchedReferenceSystemException {
    	
		super.setMappedPositions(positions);
		try {
			rsBuilder = new RubberSheetBuilder(super.getGridMappedPositions(), quad);
		} catch (TriangulationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
