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

import java.io.IOException;
import java.util.List;

import javax.vecmath.MismatchedSizeException;

import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.operation.builder.algorithm.IDWInterpolation;
import org.geotools.referencing.operation.builder.algorithm.TPSInterpolation;
import org.geotools.referencing.operation.transform.NADCONTransform;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.MismatchedReferenceSystemException;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchIdentifierException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;


public class NADCONBuilder extends WarpGridBuilder {
	
    /**
     * Construts NADCONTransform using TPSInterpolation.
     * @param vectors ground control points
     * @param xStep grid cells width
     * @param yStep grid cells height
     * @param env Envelope of generated grid
     * @throws MismatchedSizeException
     * @throws MismatchedDimensionException
     * @throws MismatchedReferenceSystemException
     * @throws TransformException
     * @throws NoSuchIdentifierException
     */
	public NADCONBuilder(List<MappedPosition> vectors, double xStep, double yStep, GeneralEnvelope env)
        throws MismatchedSizeException, MismatchedDimensionException,
            MismatchedReferenceSystemException, TransformException, NoSuchIdentifierException {
		
        super(vectors,	GridParameters.createGridParameters(env, xStep, yStep, null, false), env);        
    }
   
    @Override
    protected float[] computeWarpGrid(GridParameters gridParams)
        throws TransformException, FactoryException {
      
    	TPSInterpolation dxInterpolation = new TPSInterpolation(buildPositionsMap(0, 3600));
    	TPSInterpolation dyInterpolation = new TPSInterpolation(buildPositionsMap(1, 3600));
    	
        return interpolateWarpGrid(gridParams, dxInterpolation, dyInterpolation); 
    }

	@Override
	protected MathTransform computeMathTransform() throws FactoryException {
	
		try {
			String tmpdir = System.getProperty("java.io.tmpdir");
			NADCONTransform trans = new NADCONTransform(this.writeDeltaFile(1, tmpdir+"/nadcon.laa").getAbsolutePath(),
					                                    this.writeDeltaFile(0, tmpdir+"/nadcon.loa").getAbsolutePath());
			return trans;
		} catch (ParameterNotFoundException e) {
			  throw new FactoryException(e);
		} catch (IOException e) {
		       throw new FactoryException(e);
		}
	}     	
}
