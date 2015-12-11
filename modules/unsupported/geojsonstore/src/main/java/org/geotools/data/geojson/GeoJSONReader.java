package org.geotools.data.geojson;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.FeatureType;

/**
 * Utility class to provide a reader for GeoJSON streams
 * @author ian
 *
 */
public class GeoJSONReader {
	private static final Logger LOGGER = Logging.getLogger(GeoJSONReader.class.getName());
	private FeatureJSON reader = new FeatureJSON();
	private InputStream inputStream;
	private URL url;
	
	public GeoJSONReader(URL url) {
		this.url = url;
	}
	
	/*public GeoJSONReader( InputStream inputStream) {
		this.inputStream = inputStream;
		
	}*/

	public boolean isConnected() {
		try {
			inputStream = url.openStream();
		}catch (IOException e) {
			//whoops
			return false;
		}
		try {
			LOGGER.finest("inputstream is "+inputStream);
			return (inputStream!=null)&&(inputStream.available()>0);
		} catch (IOException e) {
			// something went wrong
			LOGGER.throwing(this.getClass().getName(),"isConnected",e);
			return false;
		}
	}

	
	public FeatureCollection getFeatures() throws IOException {
		if(!isConnected()) {
			throw new IOException("not connected to "+url.toExternalForm());
		}
		return reader.readFeatureCollection(inputStream);
	}
	
	public FeatureIterator<SimpleFeature> getIterator() throws IOException {
		if(!isConnected()) {
			return new DefaultFeatureCollection(null, null).features();
		}
			return reader.streamFeatureCollection(inputStream);
	}
	
	public FeatureType getSchema() throws IOException {
		if(!isConnected()) {
			throw new IOException("not connected to "+url.toExternalForm());
		}
		FeatureType schema = reader.readFeatureCollection(inputStream).getSchema();
		
		return schema;
	}
}
