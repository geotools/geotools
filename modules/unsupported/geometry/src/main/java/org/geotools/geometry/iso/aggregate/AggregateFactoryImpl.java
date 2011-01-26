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
package org.geotools.geometry.iso.aggregate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.geotools.factory.Factory;
import org.geotools.factory.Hints;
import org.geotools.geometry.iso.PrecisionModel;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.aggregate.AggregateFactory;
import org.opengis.geometry.aggregate.MultiCurve;
import org.opengis.geometry.aggregate.MultiPoint;
import org.opengis.geometry.aggregate.MultiPrimitive;
import org.opengis.geometry.aggregate.MultiSurface;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.OrientableSurface;
import org.opengis.geometry.primitive.Point;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * 
 *
 * @source $URL$
 */
public class AggregateFactoryImpl implements Factory, AggregateFactory {

	//private FeatGeomFactoryImpl geometryFactory;
	private CoordinateReferenceSystem crs;
	
	private Map hintsWeCareAbout = new HashMap();
	
	/**
	 * This is just here so FactorySPI can find us.
	 * We have set it up with a default (undocumented) configuration
	 * for testing!
	 */
	public AggregateFactoryImpl() {
	    this( (Hints)null );
    }
	
	/**
	 * This is the constructor used by GeometryFactoryFinder when a user
	 * requests a new instance.
	 * The provided hints *must* be provided:
	 * <ul>
	 * <li>Hints.CRS
	 * </ul>
	 * There is no default for these values - you must describe your data source
	 * for us if we are to make useful Geometry object for you.
	 * 
	 * @param hints Hints (must include CRS)
	 */
	public AggregateFactoryImpl(Hints hints) {
		if (hints == null) {
		    this.crs =  DefaultGeographicCRS.WGS84;
		}
		else {
			this.crs = (CoordinateReferenceSystem) hints.get(Hints.CRS);
		}
        
        hintsWeCareAbout.put( Hints.CRS, crs );
    }

	/**
	 * @param crs
	 */
	public AggregateFactoryImpl(CoordinateReferenceSystem crs) {
		this.crs = crs;
		hintsWeCareAbout.put( Hints.CRS, crs );
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
	
	/**
	 * Creates a MultiPrimitive by a set of Primitives.
	 * @param points Set of Points which shall be contained by the MultiPoint
	 * @return
	 */
	public MultiPrimitive createMultiPrimitive(Set<Primitive> primitives) {
		return new MultiPrimitiveImpl(crs, primitives);
	}

	/**
	 * Creates a MultiPoint by a set of Points.
	 * @param points Set of Points which shall be contained by the MultiPoint
	 * @return
	 */
	public MultiPoint createMultiPoint(Set<Point> points) {
		return new MultiPointImpl(crs, points);
	}

	
	/**
	 * Creates a MultiCurve by a set of Curves.
	 * @param points Set of Points which shall be contained by the MultiCurve
	 * @return
	 */
	public MultiCurve createMultiCurve(Set<OrientableCurve> curves) {
		return new MultiCurveImpl(crs, curves);
	}

	/**
	 * Creates a MultiSurface by a set of Surfaces.
	 * @param points Set of Points which shall be contained by the MultiSurface
	 * @return
	 */
	public MultiSurface createMultiSurface(Set<OrientableSurface> surfaces) {
		return new MultiSurfaceImpl(crs, surfaces);
	}

	
}
