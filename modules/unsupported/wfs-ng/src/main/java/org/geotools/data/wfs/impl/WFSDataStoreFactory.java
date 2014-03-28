/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.type.FeatureTypeFactoryImpl;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequenceFactory;

/**
 * A {@link DataStoreFactorySpi} to connect to a Web Feature Service.
 * <p>
 * Produces a {@link WFSDataStore} if the correct set of connection parameters
 * are provided. For instance, the only mandatory one is {@link #URL}.
 * </p>
 * <p>
 * As with all the DataStoreFactorySpi implementations, this one is not intended
 * to be used directly but through the {@link DataStoreFinder} mechanism, hence
 * client applications should not have strong dependencies over this module.
 * </p>
 * <p>
 * Upon a valid URL to a WFS GetCapabilities document, this factory will perform
 * version negotiation between the server supported protocol versions and this
 * plugin supported ones, and will return a {@link DataStore} capable of
 * communicating with the server using the agreed WFS protocol version.
 * </p>
 * <p>
 * In the case the provided GetCapabilities URL explicitly contains a VERSION
 * parameter and both the server and client support that version, that version
 * will be used.
 * </p>
 * 
 * @see WFSContentDataStore
 * @see WFSClient
 */
@SuppressWarnings({ "unchecked", "nls" })
public class WFSDataStoreFactory extends WFSDataAccessFactory implements
		DataStoreFactorySpi {

	private static int GMLComplianceLevel = 0;

	/**
	 * Requests the WFS Capabilities document from the
	 * {@link WFSDataStoreFactory#URL url} parameter in {@code params} and
	 * returns a {@link WFSDataStore} according to the version of the
	 * GetCapabilities document returned.
	 * <p>
	 * Note the {@code URL} provided as parameter must refer to the actual
	 * {@code GetCapabilities} request. If you need to specify a preferred
	 * version or want the GetCapabilities request to be generated from a base
	 * URL build the URL with the {@link #createGetCapabilitiesRequest} first.
	 * </p>
	 * 
	 * @see org.geotools.data.DataStoreFactorySpi#createDataStore(java.util.Map)
	 */
	@Override
	public WFSContentDataStore createDataStore(
			final Map<String, Serializable> params) throws IOException {

		WFSContentDataStore dataStore = new WFSContentDataStore(
				getWFSClient(params));

		// factories
		dataStore.setFilterFactory(CommonFactoryFinder.getFilterFactory(null));
		dataStore.setGeometryFactory(new GeometryFactory(
				PackedCoordinateSequenceFactory.DOUBLE_FACTORY));
		dataStore.setFeatureTypeFactory(new FeatureTypeFactoryImpl());
		dataStore
				.setFeatureFactory(CommonFactoryFinder.getFeatureFactory(null));
		dataStore.setDataStoreFactory(this);
		return dataStore;
	}

	/**
	 * Unsupported operation, can't create a WFS service.
	 * 
	 * @throws UnsupportedOperationException
	 *             always, as this operation is not applicable to WFS.
	 */
	@Override
	public DataStore createNewDataStore(final Map<String, Serializable> params)
			throws IOException {
		throw new UnsupportedOperationException(
				"Operation not applicable to a WFS service");
	}

	@Override
	public String getDisplayName() {
		return "Web Feature Server (NG)";
	}

	@Override
	public String getDescription() {
		return super.getDescription();
	}

	@Override
	public boolean canProcess(@SuppressWarnings("rawtypes") final Map params) {
		boolean canProcess = super.canProcess(params);

		if (!canProcess) {
			return false;
		}

		// Check compliance level
		if (params.containsKey(GML_COMPLIANCE_LEVEL.key)) {
			if ((Integer) params.get(GML_COMPLIANCE_LEVEL.key) > GMLComplianceLevel) {
				return false;
			}
		}

		return true;
	}

}
