package org.geotools.data.geojson;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.geotools.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

public class GeoJSONDataStore extends org.geotools.data.store.ContentDataStore {
	URL url;
	SimpleFeatureType schema;
	protected Name typeName;

	public GeoJSONDataStore(URL url) throws IOException {
		this.url = url;
	}

	@Override
	protected List<Name> createTypeNames() throws IOException {

		String name = FilenameUtils.getBaseName(url.toExternalForm());
		// could hard code features in here?
		typeName = new NameImpl(name);

		return Collections.singletonList(typeName);
	}

	GeoJSONReader read() {
		GeoJSONReader reader = null;

		reader = new GeoJSONReader(url);

		return reader;
	}

	@Override
	protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
		// We can only really write to local files
		String scheme = url.getProtocol();
		String host = url.getHost();
		if ("file".equalsIgnoreCase(scheme) && (host == null || host.isEmpty())) {
			GeoJSONFeatureStore geoJSONFeatureStore = new GeoJSONFeatureStore(entry, Query.ALL);

			return geoJSONFeatureStore;
		} else {
			GeoJSONFeatureSource geoJSONFeatureSource = new GeoJSONFeatureSource(entry, Query.ALL);

			return geoJSONFeatureSource;
		}
	}

	@Override
	public void createSchema(SimpleFeatureType featureType) throws IOException {
		this.schema = featureType;
	}

}
