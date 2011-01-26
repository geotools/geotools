/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.impl.range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

import org.geotools.coverage.io.range.Axis;
import org.geotools.feature.NameImpl;
import org.geotools.util.SimpleInternationalString;
import org.opengis.feature.type.Name;
import org.opengis.referencing.crs.SingleCRS;
import org.opengis.util.InternationalString;

/**
 * Implementation of a simple {@link Axis} which can be used
 * when defining your own field type.
 */
public class DefaultAxis<V,Q extends Quantity> implements Axis<V,Q> {
	private SingleCRS crs;
	private InternationalString description;
	private List<Measure<V,Q>> keys;
	private Name name;
	private Unit<Q> unit;
	
	public DefaultAxis( String name, Measure<V,Q> key, Unit<Q> unit){
		this( new NameImpl( name ), new SimpleInternationalString( name ), Collections.singletonList(key), unit, null );
	}
	
	public DefaultAxis( String name, List<Measure<V,Q>> keys, Unit<Q> unit){
		this( new NameImpl( name ), new SimpleInternationalString( name ), keys, unit, null );
	}
	
	public DefaultAxis( Name name, InternationalString description, List<Measure<V,Q>> keys, Unit<Q> unit){
		this( name, description, keys, unit, null );
	}
	
	public DefaultAxis( Name name, InternationalString description, List<Measure<V,Q>> keys, Unit<Q> unit, SingleCRS crs ){
		this.name = name;
		this.unit = unit;
		this.description = description;
		this.keys = new ArrayList<Measure<V,Q>>( keys );
		this.crs = crs;
	}
	
	public SingleCRS getCoordinateReferenceSystem() {
		return crs;
	}

	public InternationalString getDescription() {
		return description;
	}

	public Measure<V, Q> getKey(int keyIndex) {
		return keys.get( keyIndex );
	}

	public List<? extends Measure<V, Q>> getKeys() {
		return Collections.unmodifiableList(keys);
	}

	public Name getName() {
		return name;
	}

	public int getNumKeys() {
		return keys.size();
	}

	public Unit<Q> getUnitOfMeasure() {
		return unit;
	}
	
}
