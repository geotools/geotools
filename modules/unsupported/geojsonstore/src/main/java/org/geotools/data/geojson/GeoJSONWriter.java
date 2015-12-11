package org.geotools.data.geojson;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Wrapper to handle writing GeoJSON FeatureCollections
 * 
 * @author ian
 *
 */
public class GeoJSONWriter {
	private FeatureJSON writer = new FeatureJSON();
	private OutputStream out;
	private DefaultFeatureCollection collection = null;
	public GeoJSONWriter(OutputStream outputStream) {
		if (outputStream instanceof BufferedOutputStream) {
			this.out = outputStream;
		} else {
			this.out = new BufferedOutputStream(outputStream);
		}
		collection = new DefaultFeatureCollection();
	}

	public void setSchema(SimpleFeatureType schema) throws IOException {
		writer.setEncodeNullValues(true);
		writer.setFeatureType(schema);
		if (schema.getCoordinateReferenceSystem() != null) {
			writer.writeCRS(schema.getCoordinateReferenceSystem(), out);
		}
	}

	public void write(SimpleFeature currentFeature) throws IOException {
			collection.add(currentFeature);
	}

	public void close() throws IOException {
		writer.writeFeatureCollection(collection, out);
		out.close();
		writer = null;
		
	}

}
