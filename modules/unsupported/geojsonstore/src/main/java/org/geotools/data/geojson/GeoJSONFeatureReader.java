package org.geotools.data.geojson;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentState;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class GeoJSONFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

	private ContentState state;
	protected GeoJSONReader reader;
	private FeatureIterator<SimpleFeature> iterator;

	public GeoJSONFeatureReader(ContentState contentState, Query query) throws IOException {
		this.state = contentState;
		GeoJSONDataStore ds = (GeoJSONDataStore) state.getEntry().getDataStore();
		reader = ds.read();
		/*if (!reader.isConnected()) {
			throw new IOException("Unable to connect to GeoJSONDataStore");
		}*/
		
	}

	@Override
	public SimpleFeatureType getFeatureType() {
		GeoJSONDataStore ds = (GeoJSONDataStore) state.getEntry().getDataStore();
		try {
			SimpleFeatureType schema = (SimpleFeatureType) ds.getSchema(state.getEntry().getName());
			if(schema==null) {
				schema = (SimpleFeatureType) ds.getSchema("features");
			}
			return schema;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public SimpleFeature next() throws IOException, IllegalArgumentException, NoSuchElementException {
		if(iterator == null) {
			iterator = reader.getIterator();
		}
		return iterator.next();
	}

	@Override
	public boolean hasNext() throws IOException {
		if(iterator == null) {
			iterator = reader.getIterator();
		}
		return iterator.hasNext();
	}

	@Override
	public void close() throws IOException {
		if(iterator != null) {
			iterator.close();
		}
	}

}
