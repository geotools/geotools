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
package org.geotools.graph.build.feature;

import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.GraphGenerator;
import org.geotools.graph.build.basic.BasicGraphGenerator;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Graphable;
import org.opengis.feature.simple.SimpleFeature;

/**
 * Builds a graph from {@link org.geotools.feature.Feature} objects.
 * <p>
 * This graph generator decorates another graph generator which 
 * builds a graph from geometries. 
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 *
 *
 * @source $URL$
 */
public class FeatureGraphGenerator extends BasicGraphGenerator {

	/**
	 * The underling "geometry" building graph generator
	 */
	GraphGenerator decorated;
	
	public FeatureGraphGenerator( GraphGenerator decorated ) {
		this.decorated = decorated;
	}
	
	public Graph getGraph() {
		return decorated.getGraph();
	}
	
	public GraphBuilder getGraphBuilder() {
		return decorated.getGraphBuilder();
	}
	
	public Graphable add( Object obj ) {
		SimpleFeature feature = (SimpleFeature) obj;
		Graphable g = decorated.add( feature.getDefaultGeometry() );
		g.setObject( feature );
	
		return g;
	}
	
	public Graphable remove( Object obj ) {
		SimpleFeature feature = (SimpleFeature) obj;
		return decorated.remove( feature.getDefaultGeometry() );
	}
	
	public Graphable get(Object obj) {
		SimpleFeature feature = (SimpleFeature) obj;
		return decorated.get( feature.getDefaultGeometry() );
	}
}
