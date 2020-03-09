package org.geotools.referencing.operation.projection;

import java.awt.geom.Point2D;

import org.opengis.parameter.ParameterValueGroup;

public class Testing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Sinusoidal sinu;
		Point2D p;
		//ParameterValueGroup parameters;
		//parameters = new ParameterValueGroup();
		
		sinu = new Sinusoidal(null);
		try {
			p = sinu.transformNormalized(-110, 0, null);
			System.out.println("Result: " + p.toString());
		} catch (ProjectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
