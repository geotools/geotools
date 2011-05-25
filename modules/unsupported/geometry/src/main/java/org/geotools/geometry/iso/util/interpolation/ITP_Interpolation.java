/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso.util.interpolation;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.geotools.geometry.iso.util.algorithm2D.AlgoPoint2D;

/**
 * @author roehrig
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 *
 *
 * @source $URL$
 */
public class ITP_Interpolation {
	
	public enum TYPE {
		HARDYMQ,
		CARLSONHARDY,
		STEADHARDY,
		SHEPARD
	}
	
	// TODO JR
	/**
	 * @author roehrig
	 *
	 */
	public static class Point3d {
		public double x,y,z;

		public Point3d(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		/**
		 * @param p
		 * @return
		 */
		public double distance(Point3d p) {
			return Math.sqrt(x*x+y*y+z*z);
		}
	}
	
	public static boolean shepardModified(ArrayList<Point3d> pIn,
			ArrayList<Point3d> pOut, double q, int nnbr) {
		// pIn collection of GM_DirectPosition25OK
		// pOutn collection of GM_DirectPosition25OK
		// number of neighbours of pIn to be used in the interpolation: nnbr
		if (nnbr == 0) nnbr = pIn.size() - 1;
		// get node with neighbours aqnd gradients
		ArrayList<PointNeighboursGradients> png = ITP_Interpolation.akimaGradient(pIn, nnbr);
		for (int i=0; i< pOut.size();++i) {
			ITP_Interpolation.shepardModified(png, q, pOut.get(i));
		}
		return true;
	}

	private static ArrayList<PointNeighboursGradients> akimaGradient(ArrayList<Point3d> pIn, int nnbr) {
		// transform each node into a node, neighbours and gradients
		ArrayList<PointNeighboursGradients> result = new ArrayList<PointNeighboursGradients>(pIn.size());
		//final ITP_Interpolation  dummy = new ITP_Interpolation();
		for (int i=0; i< pIn.size();++i) {
			Point3d p = pIn.get(i);
			// creates an object for node, neighbours and gradients
			PointNeighboursGradients png = new PointNeighboursGradients(p);
			// set the neighbours
			png.setClosestPoints(pIn, nnbr);
			// set the gradients
			png.setAkimaGradient();
			result.add(png);
		}
		return result;
	}

	private static void shepardModified(ArrayList<PointNeighboursGradients> pngColl, double q, Point3d p_eval) {
		PointNeighboursGradients png0;
		double[] dist = new double[pngColl.size()];
		double distMax = 0.0;
		for (int i = 0; i < pngColl.size(); ++i) {
			png0 = (PointNeighboursGradients)pngColl.get(i);
			if (q == 2) {
				dist[i] = (p_eval.x-png0.mP.x)*(p_eval.x-png0.mP.x)+
						  (p_eval.y-png0.mP.y)*(p_eval.y-png0.mP.y);
			} else {
				dist[i] = Math.pow(p_eval.distance(png0.mP), q);
			}
			distMax = Math.max(dist[i], distMax);
		}

		double sum0 = 0.0;
		double sum1 = 0.0;
		Point2D p0 = new Point2D.Double();
		Point2D p1 = new Point2D.Double();
		for (int i = 0; i < pngColl.size(); ++i) {
			png0 = (PointNeighboursGradients)pngColl.get(i);
			double prod = 1.0;
			for (int j = 0; j < pngColl.size(); ++j) {
				if (i != j) {
					prod *= dist[j] / distMax;
					if (prod == 0.0) {
						PointNeighboursGradients png1 = (PointNeighboursGradients)pngColl.get(j);
						p_eval.z = png1.mP.z;
						return;
					}
				}
			}
			p0.setLocation(p_eval.x,p_eval.y);
			p1.setLocation(png0.mP.x,png0.mP.y);
			sum0 += (png0.mP.z + AlgoPoint2D.scalar(AlgoPoint2D.subtract(p0,p1),png0.mGradXY))* prod;
			sum1 += prod;
		}
		p_eval.z = (sum0 / sum1);

	}
	
}
