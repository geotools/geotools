/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains formulas from the PROJ package of USGS.
 *    USGS's work is fully acknowledged here. This derived work has
 *    been relicensed under LGPL with Frank Warmerdam's permission.
 */
package org.geotools.referencing.operation.projection;

import java.awt.geom.Point2D;
import static java.lang.Math.toRadians;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.MathTransform;

/**
 * Homolosine projection
 *
 * @see <A HREF="https://doi.org/10.2307%2F2560812">Goode, J.P. (1925). "The Homolosine projection -
 *     a new device for portraying the Earth's surface entire". Annals of the Association of
 *     American Geographers. 15 (3): 119–125</A>
 * @see <A HREF="https://en.wikipedia.org/wiki/Goode_homolosine_projection">The Homolosine
 *     projection on Wikipedia</A>
 * @since 22.x
 * @author Luís M. de Sousa
 */
public class Homolosine extends MapProjection {
    /** For cross-version compatibility. */
	private static final long serialVersionUID = 4740760391570944118L;

    private static double LAT_THRESH = toRadians(40 + 44 / 60. + 11.8 / 3600.);

    private static final double[] INTERRUP_NORTH = {toRadians(-180), toRadians(-40), toRadians(180)};
    private static final double[] INTERRUP_SOUTH = {toRadians(-180), toRadians(-100), toRadians(-20), 
    											    toRadians(80), toRadians(180)};
    
    private static final double[] CENTRAL_MERID_NORTH = {toRadians(-100), toRadians(30)};
    private static final double[] CENTRAL_MERID_SOUTH = {toRadians(-160), toRadians(-60), 
    													 toRadians(20), toRadians(140)};

    ParameterDescriptorGroup descriptors;
    ParameterValueGroup parameters; // stored locally to skip computations in parent

    Mollweide moll;
    Sinusoidal sinu;

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    protected Homolosine(
            final ParameterDescriptorGroup descriptors, final ParameterValueGroup parameters)
            throws ParameterNotFoundException {

        super(parameters, descriptors.descriptors());
        this.descriptors = descriptors;
        this.parameters = parameters;
        this.sinu = new Sinusoidal(this.parameters);
        this.moll = new Mollweide(
                    	Mollweide.ProjectionMode.Mollweide, this.descriptors, this.parameters);
    }

    /** {@inheritDoc} */
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
    }
    
    /**
     * Computes the Northing difference between Sinusoidal and Mollweide at the threshold
     * latitude. Result depends on the datum and scaling parameters.  
     * @return Northing offset between Sinusoidal and Mollweide at threshold latitude.
     * @throws ProjectionException 
     */
    protected double computeOffset() throws ProjectionException
    {
    	Point2D sinu_tresh = sinu.transformNormalized(0, LAT_THRESH,  null);
        Point2D moll_tresh = moll.transformNormalized(0, LAT_THRESH,  null);
        return moll_tresh.getY() - sinu_tresh.getY();
    }
    
    /**
     * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates (units in
     * radians) and stores the result in {@code ptDst} (linear distance on a unit sphere).
     */
    protected Point2D transformNormalized(double lam, double phi, Point2D ptDst)
            throws ProjectionException {

        double[] interruptions;
        double[] central_merids;
        double offset = computeOffset();
        int i = 0;
        double central_merid = 0;
        double lam_shift = 0;
        Point2D p;
        Point2D shift;
        
        if (phi >= 0) {
        	interruptions = INTERRUP_NORTH;
        	central_merids = CENTRAL_MERID_NORTH;
        }
        else {
        	interruptions = INTERRUP_SOUTH;
        	central_merids = CENTRAL_MERID_SOUTH;
        	offset = - offset;
        }

        while (lam > interruptions[++i]); 
    	central_merid = central_merids[i - 1];
        lam_shift = lam - central_merid;
        
        if (phi > LAT_THRESH || phi < -LAT_THRESH) { // Mollweide
            p = moll.transformNormalized(lam_shift, phi, ptDst);
            p.setLocation(p.getX(), p.getY() - offset);
        } else { // Sinusoidal
            p = sinu.transformNormalized(lam_shift, phi, ptDst);
        }
        
        shift = sinu.transformNormalized(central_merid, 0., null);
        p.setLocation(p.getX() + shift.getX(), p.getY());
        
        if (ptDst != null) {
            ptDst.setLocation(p.getX(), p.getY());
            return ptDst;
        } else {
            return p;
        }
    }

    /**
     * Transforms the specified (<var>x</var>,<var>y</var>) coordinates and stores the result in
     * {@code ptDst}.
     */
    protected Point2D inverseTransformNormalized(double x, double y, final Point2D ptDst)
            throws ProjectionException {
    	
    	double[] interruptions;
        double[] central_merids;
        double offset = computeOffset();
        int i = 0;
        double central_merid = 0;
        Point2D p;
        Point2D shift;
        double thresh_map = sinu.transformNormalized(0, LAT_THRESH, null).getY();
        
        if (y >= 0) {
        	central_merids = CENTRAL_MERID_NORTH;
        	interruptions = new double[INTERRUP_NORTH.length];
        	for (int j=0; j < INTERRUP_NORTH.length; j++)
        		interruptions[j] = sinu.transformNormalized(INTERRUP_NORTH[j], 0, null).getX();
        }
        else {
        	central_merids = CENTRAL_MERID_SOUTH;
        	offset = - offset;
        	interruptions = new double[INTERRUP_SOUTH.length];
        	for (int j=0; j < INTERRUP_SOUTH.length; j++)
        		interruptions[j] = sinu.transformNormalized(INTERRUP_SOUTH[j], 0, null).getX();
        }

        while (x > interruptions[++i]); 
    	central_merid = central_merids[i - 1];
    	shift = sinu.transformNormalized(central_merid, 0, null);
    	       
        if (y > thresh_map || y < -thresh_map) { // Mollweide
        	p = moll.inverseTransformNormalized(x - shift.getX(), y + offset, ptDst);
        } else { // Sinusoidal
            p = sinu.inverseTransformNormalized(x - shift.getX(), y, ptDst);
        }
                       
        p.setLocation(p.getX() + central_merid, p.getY());
        
        if (ptDst != null) {
            ptDst.setLocation(p.getX(), p.getY());
            return ptDst;
        } else {
            return p;
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                          ////////
    ////////                                 PROVIDERS                                ////////
    ////////                                                                          ////////
    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform
     * provider} for the Homolosine projection (not part of the EPSG database).
     *
     * @since 22.x
     * @author Luís M. de Sousa
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class Provider extends AbstractProvider {

		/** For cross-version compatibility. */
    	private static final long serialVersionUID = -7345885830045627291L;

        /** The parameters group. */
        static final ParameterDescriptorGroup PARAMETERS =
                createDescriptorGroup(
                        new NamedIdentifier[] {
                            new NamedIdentifier(Citations.GEOTOOLS, "Homolosine"),
                            new NamedIdentifier(Citations.ESRI, "Homolosine")
                        },
                        new ParameterDescriptor[] {
                            SEMI_MAJOR, SEMI_MINOR, CENTRAL_MERIDIAN, FALSE_EASTING, FALSE_NORTHING
                        });

        /** Constructs a new provider. */
        public Provider() {
            super(PARAMETERS);
        }

        /**
         * Creates a transform from the specified group of parameter values.
         *
         * @param parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        protected MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException {
            return new Homolosine(PARAMETERS, parameters);
        }
    }
}
