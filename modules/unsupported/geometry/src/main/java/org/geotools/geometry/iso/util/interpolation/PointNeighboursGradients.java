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
import org.geotools.geometry.iso.util.interpolation.ITP_Interpolation.Point3d;

/**
 * @author roehrig
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 *
 *
 * @source $URL$
 */
public class PointNeighboursGradients {
	
	Point3d mP;
	
	ITP_Interpolation.Point3d[] mNbrs;
	
	public Point2D mGradXY;
	
	public PointNeighboursGradients(Point3d p) {
		this.mP = p;
	}
	
	public PointNeighboursGradients(Point3d p, Point3d[] nbrs) {
		this.mP = p;
		System.arraycopy(nbrs, 0, this.mNbrs, 0, nbrs.length);
		this.mGradXY = null;
	}
	

    public  void setAkimaGradient() {
        int nnbr = this.mNbrs.length; 
        double a = 0.0; 
        double b = 0.0; 
        double c = 0.0; 
        for ( int i = 0 ; i < nnbr; ++i) {
        	Point2D dp0 = new Point2D.Double(this.mNbrs[i].x-this.mP.x, this.mNbrs[i].y-this.mP.y); 
            double dz0 = this.mNbrs[i].z - this.mP.z; 
            for ( int j = i + 1 ; j <= this.mNbrs.length - 1; ++j) {
            	Point2D dp1 = new Point2D.Double(this.mNbrs[j].x-this.mP.x, this.mNbrs[j].y-this.mP.y); 
                double delta_c = AlgoPoint2D.cross(dp0,dp1); 
                if ( delta_c != 0.0 ) {
                    double dz1 = this.mNbrs[j].z - this.mP.z; 
                    double delta_a = dp0.getY() * dz1 - dp1.getY() * dz0; 
                    double delta_b = dp1.getX() * dz0 - dp0.getX() * dz1; 
                    if ( delta_c < 0.0 ) {
                        delta_a = -delta_a;
                        delta_b = -delta_b;
                        delta_c = -delta_c;
                    }
                    a += delta_a;
                    b += delta_b;
                    c += delta_c;
                }
            }
        }
        this.mGradXY = new Point2D.Double(-a / c, -b / c);
    }

    public void setClosestPoints(ArrayList<Point3d> pIn, int nnbr) {
    	ArrayList<Point3d> nbrs = new ArrayList<Point3d>(); 
        double[] nbrDist = new double[nnbr]; 
        for ( int i = 0 ; i < nnbr; ++i) {
            nbrDist[i] = Double.MAX_VALUE;
        }

        for ( int i = 0, j = 0 ; i < pIn.size(); ++i) {
        	Point3d p1 = pIn.get(i);
            if ( this.mP != p1 ) {
                double dist = (this.mP.x-p1.x)*(this.mP.x-p1.x) + (this.mP.y-p1.y)*(this.mP.y-p1.y); 
                if ( j < nnbr ) {
                    nbrDist[j] = dist;
                    nbrs.add(p1);
                } else {
                    for ( int k = 0 ; k < nnbr; ++k) {
                        if ( dist < nbrDist[k] ) {
                            nbrDist[k] = dist;
                            nbrs.add(p1);
                            break;
                        }
                    }
                }
                j += 1;
            }
        }
        this.mNbrs = new Point3d[nbrs.size()];
        for (int i=0; i<nbrs.size(); ++i) this.mNbrs[i] = nbrs.get(i);
    }
}
