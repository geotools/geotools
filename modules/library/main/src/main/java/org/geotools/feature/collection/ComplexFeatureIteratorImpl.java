/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.feature.collection;

import java.util.NoSuchElementException;
import java.util.Queue;

import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;

/**
 * Defines the complex feature iterator implementation class.
 * It's responsible for exposing an iterator-style interface for complex features. 
 *
 */
public class ComplexFeatureIteratorImpl implements FeatureIterator<Feature> {

	/**
	 * The features are pre-loaded into this queue.
	 * The reason this is done is to allow parsing of local xlink:hrefs.
	 */
	private Queue<Feature> features;

	/**
	 * Initialises a new instance of ComplexFeatureIteratorImpl. 
	 * @param features
	 * 		The queue of features to use.
	 */
	public ComplexFeatureIteratorImpl(Queue<Feature> features) {
		this.features = features;
	}

	@Override
	public boolean hasNext() {
		return features.size() > 0;
	}

	@Override
	public Feature next() throws NoSuchElementException {
		return this.features.remove();
	}

	@Override
	public void close() {
	}
}
