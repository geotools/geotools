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
package org.geotools.geometry.iso;

import java.awt.RenderingHints;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.factory.Factory;
import org.geotools.factory.Hints;
import org.geotools.geometry.iso.coordinate.DirectPositionImpl;
import org.geotools.geometry.iso.coordinate.DoublePointArray;
import org.geotools.geometry.iso.coordinate.PointArrayImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.Precision;
import org.opengis.geometry.PrecisionType;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.coordinate.Position;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Default implementation of PositionFactory that stores contents using double.
 * <p>
 * You should be aware of the following:
 * <ul>
 * <li>createPositionList() is backed by an ArrayList
 * <li>createPositionList( double, int, int) is a custom efficient
 * implementation that does not support add
 * <li>createPositionList( float, int, int ) will copy the array contents into
 * individual DirectPositions.
 * </ul>
 * 
 * @author Jody Garnett
 *
 *
 * @source $URL$
 */
public class PositionFactoryImpl implements Serializable, Factory, PositionFactory {
	private static final long serialVersionUID = 1L;
	final private Precision precision;
    final CoordinateReferenceSystem crs;
    
	private Map<RenderingHints.Key, Object> hintsWeCareAbout = new HashMap<RenderingHints.Key, Object>();


	/**
	 * This is just here so FactorySPI can find us.
	 * We have set it up with a default (undocumented) configuration
	 * for testing!
	 */
	public PositionFactoryImpl() {
	    this( (Hints)null );
    }
	/**
	 * This is the constructor used by GeometryFactoryFinder when a user
	 * requests a new instance.
	 * The provided hints *must* be provided:
	 * <ul>
	 * <li>Hints.CRS
	 * <li>Hints.PRECISION
	 * </ul>
	 * There is no default for these values - you must describe your data source
	 * for us if we are to make useful Geometry object for you.
	 * 
	 * @param hints Hints (must include CRS and PRECISION)
	 */
	public PositionFactoryImpl(Hints hints) {
		if (hints == null) {
		    this.crs =  DefaultGeographicCRS.WGS84;
	        this.precision = new PrecisionModel();
		}
		else {
			this.crs = (CoordinateReferenceSystem) hints.get(Hints.CRS);
			this.precision = new PrecisionModel();
		}
        
        hintsWeCareAbout.put( Hints.CRS, crs );
        hintsWeCareAbout.put( Hints.PRECISION, precision );
    }
	
	public PositionFactoryImpl(CoordinateReferenceSystem crs) {
		this(crs, new PrecisionModel(PrecisionType.DOUBLE));
	}
	
	public PositionFactoryImpl(CoordinateReferenceSystem crs,
			Precision precision) {
		assert( precision.getType() == PrecisionType.DOUBLE );
		this.crs = crs;
		this.precision = precision;
		
		hintsWeCareAbout.put( Hints.CRS, crs );
        hintsWeCareAbout.put( Hints.PRECISION, precision );
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

	public DirectPosition createDirectPosition(double[] coords)
			throws MismatchedDimensionException {
		if (coords != null) return new DirectPositionImpl(crs, coords);
		return new DirectPositionImpl(crs);
	}

	public Position createPosition(Position position) {
		DirectPosition directPosition = position.getPosition();
		return new DirectPositionImpl(directPosition);
	}

	public PointArrayImpl createPointArray() {
		return new PointArrayImpl(crs);
	}

	public DoublePointArray createPointArray(final double[] array,
			final int start, final int end) {
		return new DoublePointArray(crs, array, start, end);
	}

	public PointArray createPointArray(float[] array, int start, int end) {
		PointArray pointArray = (PointArray) createPointArray();
		int D = crs.getCoordinateSystem().getDimension();
		if (D == 2) {
			for (int i = start; i < end; i += D) {
				double[] ordinates = new double[] { array[i], array[i + 1] };
				pointArray.add(new DirectPositionImpl(crs, ordinates));
			}
		} else if (D == 3) {
			for (int i = start; i < end; i += D) {
				double[] ordinates = new double[] { array[i], array[i + 1],
						array[i + 2] };
				pointArray.add(new DirectPositionImpl(crs, ordinates));
			}
		} else {
			for (int i = start; i < end; i += D) {
				double[] ordinates = new double[D];
				for (int o = 0; i < D; i++) {
					ordinates[o] = array[i + o];
				}
				pointArray.add(new DirectPositionImpl(crs, ordinates));
			}
		}
		return pointArray;
	}

	public CoordinateReferenceSystem getCoordinateReferenceSystem() {
		return crs;
	}

	public Precision getPrecision() {
		return precision;
	}

}
