/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature;

import java.util.List;

import org.opengis.feature.simple.SimpleFeature;

import org.opengis.feature.simple.SimpleFeatureType;

/**
 * An extension of {@link SimpleFeatureFactoryImpl} which creates 
 * {@link DefaultFeature} instances.
 *  
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 * 
 * @deprecated use {@link SimpleFeatureFactory},  this class is only provided to 
 * maintain backwards compatability for transition to geoapi feature model and 
 * will be removed in subsequent versions.
 * 
 * @since 2.5
 *
 *
 * @source $URL$
 */
public class DefaultFeatureFactory extends AbstractFeatureFactoryImpl {

	public SimpleFeature createSimpleFeature(List properties, SimpleFeatureType type, String id) {
		return new DefaultFeature( properties, (DefaultFeatureType) type, id );
	}

}
