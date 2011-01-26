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
package org.geotools.geometry.iso.complex;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.factory.Factory;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeometryFactoryFinder;
import org.geotools.geometry.iso.primitive.PointImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.complex.ComplexFactory;
import org.opengis.geometry.complex.CompositeCurve;
import org.opengis.geometry.complex.CompositePoint;
import org.opengis.geometry.complex.CompositeSurface;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.OrientableSurface;
import org.opengis.geometry.primitive.Point;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class ComplexFactoryImpl implements Factory, ComplexFactory {

	//private FeatGeomFactoryImpl geometryFactory;
	private CoordinateReferenceSystem crs;
	private Map hintsWeCareAbout = new HashMap();

	/** FactorySPI entry point */
	public ComplexFactoryImpl() {
		this((Hints)null );
	}	
	
	/** Just the defaults, use GeometryFactoryFinder for the rest */
	public ComplexFactoryImpl( Hints hints ) {
		if (hints == null) {
			this.crs = DefaultGeographicCRS.WGS84;
			hints = GeoTools.getDefaultHints();
	        hints.put(Hints.CRS, crs );
		}
		else {
			this.crs = (CoordinateReferenceSystem) hints.get( Hints.CRS );
			if( crs == null ){
				throw new NullPointerException("A CRS Hint is required in order to use ComplexFactoryImpl");
			}
		}
		
		hintsWeCareAbout.put(Hints.CRS, crs );
	}	
	
	/**
	 */
	public ComplexFactoryImpl(CoordinateReferenceSystem crs) {
		this.crs = crs;
		hintsWeCareAbout.put(Hints.CRS, crs );
	}

	/**
	 * Report back to FactoryRegistry about our configuration.
	 * <p>
	 * FactoryRegistry will check to make sure that there are no duplicates
	 * created (so there will be only a "single" PositionFactory created
	 * with this configuration).
	 * </p>
	 */
    public Map getImplementationHints() {
        return Collections.unmodifiableMap( hintsWeCareAbout );
    }
	
	public CompositePoint createCompositePoint(Point generator) {
		return new CompositePointImpl(crs, (PointImpl) generator);
	}
	
	public CompositeCurve createCompositeCurve(List<OrientableCurve> generator) {
		return new CompositeCurveImpl(generator);
	}

	public CompositeSurface createCompositeSurface(List<OrientableSurface> generator) {
		return new CompositeSurfaceImpl(generator);
	}

	
}
