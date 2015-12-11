package org.geotools.data.geojson;

import java.io.IOException;

import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;

public class GeoJSONFeatureSource extends ContentFeatureSource {

	private FeatureCollection collection = null;

	public GeoJSONFeatureSource(ContentEntry entry, Query query) {
		super(entry, query);
		if (schema == null) {
			// first see if our datastore knows what the schema is
			schema = getDataStore().schema;

		}
		if (schema == null) {
			try {
				// failing that we'll attempt to construct it from the features
				schema = buildFeatureType();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public GeoJSONDataStore getDataStore() {
		return (GeoJSONDataStore) super.getDataStore();
	}

	@Override
	protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
		collection = fetchFeatures();
		ReferencedEnvelope bounds = collection.getBounds();
		if (bounds.getCoordinateReferenceSystem() == null) {
			bounds = new ReferencedEnvelope(bounds, DefaultGeographicCRS.WGS84);
		}
		return bounds;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	private FeatureCollection fetchFeatures() throws IOException {
		if (collection == null) {
			GeoJSONReader reader = getDataStore().read();
			collection = reader.getFeatures();
		}
		return collection;
	}

	@Override
	protected int getCountInternal(Query query) throws IOException {
		collection = fetchFeatures();
		if (query.getFilter() == Filter.INCLUDE) {

			return collection.size();

		} else {

			FeatureCollection sub = collection.subCollection(query.getFilter());
			return sub.size();

		}

	}

	@Override
	protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query) throws IOException {

		return new GeoJSONFeatureReader(getState(), query);
	}

	@Override
	protected SimpleFeatureType buildFeatureType() throws IOException {
		if (schema == null) {
			collection = fetchFeatures();
			SimpleFeatureType sch = (SimpleFeatureType) collection.getSchema();
			SimpleFeatureTypeBuilder sb = new SimpleFeatureTypeBuilder();
			sb.setName(getState().getEntry().getTypeName());
			for (AttributeDescriptor att : sch.getAttributeDescriptors()) {
				sb.add(att);
			}
			if (sch.getCoordinateReferenceSystem() != null) {
				sb.setCRS(sch.getCoordinateReferenceSystem());
			} else {
				sb.setCRS(DefaultGeographicCRS.WGS84);
			}
			sb.setDescription(sch.getDescription());
			schema = sb.buildFeatureType();
		}
		return schema;
	}

	/**
	 * Make handleVisitor package visible allowing CSVFeatureStore to delegate to
	 * this implementation.
	 */
	@Override
	protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
		return super.handleVisitor(query, visitor);
	}
}
