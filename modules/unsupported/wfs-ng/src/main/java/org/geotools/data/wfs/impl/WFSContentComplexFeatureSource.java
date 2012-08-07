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
package org.geotools.data.wfs.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.namespace.QName;

import org.geotools.data.Query;
import org.geotools.data.store.ContentComplexFeatureSource;
import org.geotools.data.store.WFSContentComplexFeatureCollection;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.data.wfs.internal.parsers.XmlComplexFeatureParser;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Combines the WFSClient and DataAccess objects and exposes methods to access
 * the features by using the XmlComplexFeatureParser. 
 *
 */
public class WFSContentComplexFeatureSource extends ContentComplexFeatureSource {
	/**
	 * The name of the feature type of the source.
	 */
	private Name typeName;

	/**
	 * The wfs client object.
	 */
	private WFSClient client;

	/**
	 * The data access object.
	 */
	private WFSContentDataAccess dataAccess;

	/**
	 * Initialises a new instance of the class WFSContentComplexFeatureSource.
	 * 
	 * @param typeName
	 * 		The name of the feature you want to retrieve.
	 * @param client
	 * 		The WFSClient responsible for making the WFS requests.
	 * @param dataAccess
	 * 		The data access object.
	 */
	public WFSContentComplexFeatureSource(Name typeName, WFSClient client,
			WFSContentDataAccess dataAccess) {
		super(null);
		this.typeName = typeName;
		this.client = client;
		this.dataAccess = dataAccess;
	}

	/**
	 * Get features based on the specified filter.
	 */
	@Override
	public FeatureCollection<FeatureType, Feature> getFeatures(Filter filter)
			throws IOException {
		return getFeatures( new Query(this.typeName.toString(), filter ) );
	}

	/**
	 * Get features using the default Query.ALL.
	 */
	@Override
	public FeatureCollection<FeatureType, Feature> getFeatures()
			throws IOException {
		return getFeatures(joinQuery(Query.ALL));
	}

	/**
	 * Get features based on the query specified.
	 */
	@Override
	public FeatureCollection<FeatureType, Feature> getFeatures(Query query)
			throws IOException {
		this.query = joinQuery(query);

		GetFeatureRequest request = client.createGetFeatureRequest();
		FeatureType schema = dataAccess.getSchema(typeName);
		QName name = dataAccess.getRemoteTypeName(typeName);
		request.setTypeName(new QName(query.getTypeName()));

		request.setFullType(schema);
		request.setFilter(query.getFilter());
		request.setPropertyNames(query.getPropertyNames());
		request.setSortBy(query.getSortBy());

		String srsName = null;
		CoordinateReferenceSystem crs = query.getCoordinateSystem();
		if (null != crs) {
			System.err.println("Warning: don't forget to set the query CRS");
		}

		request.setSrsName(srsName);
		InputStream stream = request.getFinalURL().openStream();
		XmlComplexFeatureParser parser = new XmlComplexFeatureParser(stream,
				schema, name);

		Queue<Feature> features = new LinkedList<Feature>();
		
		Feature feature;
		while ((feature = parser.parse()) != null) {
			features.add(feature);
		}

		return new WFSContentComplexFeatureCollection(features);
	}
}
