/*
 *    GeoTools - OpenSource mapping toolkit
 *    http://geotools.org
 *
 *   (C) 2000-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    This package contains formulas from the PROJ package of USGS.
 *    USGS's work is fully acknowledged here. This derived work has
 *    been relicensed under LGPL with Frank Warmerdam's permission.
 */
package org.geotools.referencing.operation.projection;

import java.awt.geom.Point2D;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.operation.MathTransform;
/**
 * Cassini-Soldner Projection (EPSG code 9806).
 * The Cassini-Soldner Projection is the ellipsoidal version of the Cassini 
 * projection for the sphere. It is not conformal but as it is relatively simple
 * to construct it was extensively used in the last century and is still useful
 * for mapping areas with limited longitudinal extent. It has now largely
 * been replaced by the conformal Transverse Mercator which it resembles. Like this,
 * it has a straight central meridian along which the scale is true, all other
 * meridians and parallels are curved, and the scale distortion increases
 * rapidly with increasing distance from the central meridian. 
 */
public class CassiniSoldner extends MapProjection {

	/**
     * Maximum number of iterations for iterative computations.
     */
    private static final int MAXIMUM_ITERATIONS = 15;
	
	/**
     * Meridian distance at the {@code latitudeOfOrigin}.
     * Used for calculations for the ellipsoid.
     */
    private final double ml0;
	
    
    /**
     * Contants used for the forward and inverse transform for the eliptical
     * case of the Cassini-Soldner.
     */
    private static final double  C1= 0.16666666666666666666,
    							 C2= 0.08333333333333333333,
    							 C3= 0.41666666666666666666,
    							 C4= 0.33333333333333333333,
    							 C5= 0.66666666666666666666;
    
    
	protected CassiniSoldner(ParameterValueGroup values)
			throws ParameterNotFoundException {
		super(values);
        ml0 = mlfn(latitudeOfOrigin, Math.sin(latitudeOfOrigin), Math.cos(latitudeOfOrigin));
	}
	
	/**
     * {@inheritDoc}
     */
	public ParameterDescriptorGroup getParameterDescriptors() {
		return Provider.PARAMETERS;
	}
	/**
     * Transforms the specified (<var>x</var>,<var>y</var>) coordinate
     * and stores the result in {@code ptDst}.
     */
	protected Point2D inverseTransformNormalized(double x, double y,
			Point2D ptDst) throws ProjectionException {
		double ph1=inv_mlfn(ml0+y);
		double tn=Math.tan(ph1);
		double t=tn*tn;
		double n=Math.sin(ph1);
		double r=1.0/(1.0-excentricitySquared*n*n);
		n=Math.sqrt(r);
		r*=(1.0-excentricitySquared)*n;
		double dd=x/n;
		double d2=dd*dd;
		double phi=ph1-(n*tn/r)*d2*(0.5-(1.0+3.0*t)*d2*C3);
		double lam=dd*(1.0+t*d2*(-C4+(1.0+3.0*t)*d2*C5))/Math.cos(ph1);
		if (ptDst != null) {
            ptDst.setLocation(lam,phi);
            return ptDst;
        }
        return new Point2D.Double(lam,phi);
	}

    /**
     * Transforms the specified (<var>x</var>,<var>y</var>) coordinate (units in radians)
     * and stores the result in {@code ptDst} (linear distance on a unit sphere).
     */
	protected Point2D transformNormalized(double lam, double phi, Point2D ptDst)
			throws ProjectionException {
		double sinphi = Math.sin(phi); 
        double cosphi = Math.cos(phi); 
        
        double n=1.0/(Math.sqrt(1.0-excentricitySquared*sinphi*sinphi));
        double tn=Math.tan(phi);
        double t=tn*tn;
        double a1=lam*cosphi;
        double c=cosphi*cosphi*excentricitySquared/(1-excentricitySquared);
        double a2=a1*a1;
                
        double x = n*a1*(1.0-a2*t*(C1-(8.0-t+8.0*c)*a2*C2));
        double y = (mlfn(phi, sinphi, cosphi))-ml0+n*tn*a2*(0.5+(5.0-t+6.0*c)*a2*C3);
        
        if (ptDst != null) {
            ptDst.setLocation(x,y);
            return ptDst;
        }
        return new Point2D.Double(x,y);
	}
	
	/**
     * Provides the transform equations for the spherical case of the
     * CassiniSoldner projection.
     * 
     */
	private static final class Spherical extends CassiniSoldner {

		protected Spherical(ParameterValueGroup values)
				throws ParameterNotFoundException {
			 super(values);
	         assert isSpherical;			
		}
		
		/**
         * {@inheritDoc}
         */
        protected Point2D transformNormalized(double x, double y, Point2D ptDst)
                throws ProjectionException 
        {
            double x1=Math.asin(Math.cos(y)*Math.sin(x));
            double y1=Math.atan2(Math.tan(y),Math.cos(x))-latitudeOfOrigin;
            if (ptDst != null) {
                ptDst.setLocation(x1,y1);
                return ptDst;
            }
            return new Point2D.Double(x1,y1);
        }        
        
        /**
         * {@inheritDoc}
         */
        protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst) 
                throws ProjectionException 
        {
        	double dd=y+latitudeOfOrigin;
            double phi=Math.asin(Math.sin(dd)*Math.cos(x));
            double lam=Math.atan2(Math.tan(x),Math.cos(dd));
            if (ptDst != null) {
                ptDst.setLocation(lam,phi);
                return ptDst;
            }
            return new Point2D.Double(lam,phi);
        }
		
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                          ////////
    ////////                                 PROVIDER                                 ////////
    ////////                                                                          ////////
    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The {@link org.geotools.referencing.operation.MathTransformProvider}
     * for a {@link CassiniSoldner} projection.
     *
     */
    public static class Provider extends AbstractProvider {
        /**
         * Returns a descriptor group for the specified parameters.
         */
    	
        static ParameterDescriptorGroup createDescriptorGroup(final ReferenceIdentifier[] identifiers) {
            return createDescriptorGroup(identifiers, new ParameterDescriptor[] {
                SEMI_MAJOR,       SEMI_MINOR,
                CENTRAL_MERIDIAN, LATITUDE_OF_ORIGIN,
                SCALE_FACTOR,     FALSE_EASTING,
                FALSE_NORTHING
            });
        }

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.OGC,      "Cassini_Soldner"),
                new NamedIdentifier(Citations.EPSG,     "Cassini-Soldner"),
                new NamedIdentifier(Citations.EPSG,     "9806"),
                new NamedIdentifier(Citations.GEOTIFF,  "CT_CassiniSoldner"),
                new NamedIdentifier(Citations.ESRI,     "Cassini_Soldner"),                
                new NamedIdentifier(Citations.GEOTOOLS, Vocabulary.formatInternational(
                                    VocabularyKeys.CASSINI_SOLDNER_PROJECTION))
            });

        /**
         * Constructs a new provider.
         */
        public Provider() {
            super(PARAMETERS);
        }

        /**
         * Constructs a new provider with the specified parameters.
         */
        Provider(final ParameterDescriptorGroup descriptor) {
            super(descriptor);
        }

        

        /**
         * Creates a transform from the specified group of parameter values.
         *
         * @param  parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        public MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            if (isSpherical(parameters)) {
                return new Spherical(parameters);
            } else {
                return new CassiniSoldner(parameters);
            }
        }
    }

}
