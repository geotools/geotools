/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog;

import java.io.IOException;
import java.util.Collection;

import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Transaction;
import org.geotools.feature.SchemaException;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.gce.imagemosaic.GranuleDescriptor;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.BoundingBox;
/**
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
public interface GranuleCatalog {
	
	/**
	 * Finds the granules that intersects the provided {@link BoundingBox}:
	 * 
	 * @param envelope
	 *            The {@link BoundingBox} to test for intersection.
	 * @return Collection of {@link Feature} that intersect the provided
	 *         {@link BoundingBox}.
	 * @throws IOException 
	 */
	public Collection<GranuleDescriptor> getGranules(final BoundingBox envelope)throws IOException;
	
	public Collection<GranuleDescriptor> getGranules(final Query q) throws IOException;

	public Collection<GranuleDescriptor> getGranules()throws IOException;
	
	/**
	 * Finds the granules that intersects the provided {@link BoundingBox}:
	 * 
	 * @param envelope
	 *            The {@link BoundingBox} to test for intersection.
	 * @return List of {@link Feature} that intersect the provided
	 *         {@link BoundingBox}.
	 * @throws IOException 
	 */
	public void getGranules(final BoundingBox envelope,final  GranuleCatalogVisitor visitor) throws IOException;
	
	public void getGranules( final Query q, final GranuleCatalogVisitor visitor) throws IOException;	

	public void dispose();
		
	public void addGranule(final SimpleFeature granule, final Transaction transaction) throws IOException;
	
	public void addGranules(final Collection<SimpleFeature> granules, final Transaction transaction) throws IOException;
	
	public void createType(String namespace, String typeName, String typeSpec) throws IOException, SchemaException;
	
	public void createType(SimpleFeatureType featureType) throws IOException;
	
	public void createType(String identification, String typeSpec) throws SchemaException, IOException;
	
	public SimpleFeatureType getType() throws IOException;
	
	public int removeGranules(final Query query);

	public BoundingBox getBounds();
	
	public void computeAggregateFunction(final Query q,final FeatureCalc function) throws IOException;
	
	public QueryCapabilities getQueryCapabilities();
}