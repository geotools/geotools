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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.geotools.geometry.DirectPosition2D;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

public class WarpGridTester {

	private WarpGridBuilder builder;
	
	private double error;
	
	public WarpGridTester(WarpGridBuilder builder){
		this.builder = builder;
	}
	
	public void calculateError() throws FactoryException, TransformException{
		List<MappedPosition> mps = new LinkedList<MappedPosition>(builder.getMappedPositions());
	   List<MappedPosition> allMps = new LinkedList<MappedPosition>(builder.getMappedPositions());
		double m = 0;
		 builder.setMappedPositions(mps);
				 
		//for(int i = 0; i< allMps.size(); i++){
		 for(Iterator<MappedPosition> i = allMps.iterator(); i.hasNext();){		
			//MappedPosition mp = mps.get(i);
			MappedPosition mapPos = i.next();
			mps.remove(mapPos);
			//List<MappedPosition> gcps = new LinkedList<MappedPosition>(mps);
			
			builder.setMappedPositions(mps);
			
		//	MathTransform trans = (new TPSGridBuilder(mps,0.01, 0.01, builder.getEnvelope(), builder.getWorldToGrid().inverse())).getMathTransform();
			MathTransform trans = builder.getMathTransform();
			//System.out.println(builder.getMappedPositions().size());
			DirectPosition dst = new DirectPosition2D();
			m=m+(mapPos.getError(trans, null));
			System.out.println((mapPos.getError(trans, null)));//*Math.PI/180 * 6480000);
			//System.out.println(trans.transform(mapPos.getSource(), null));
			//System.out.println("transformed - "+dst);
			//System.out.println("Should be - "+map.getTarget());
			
			
			mps.add(mapPos);
		}
		 
		 System.out.println("pruuuuuumer : "+(m/mps.size()));
		
	}
	
	/*try feature source with limited neigbors*/
	
}
