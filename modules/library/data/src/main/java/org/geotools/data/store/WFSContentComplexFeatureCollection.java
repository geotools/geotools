package org.geotools.data.store;

import java.io.IOException;
import java.util.Collection;
import java.util.Queue;

import org.geotools.feature.FeatureCollection;
import org.geotools.feature.collection.ComplexFeatureIteratorImpl;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.util.ProgressListener;

public class WFSContentComplexFeatureCollection implements FeatureCollection<FeatureType, Feature> {
	private Queue<Feature> features;
		
	public WFSContentComplexFeatureCollection(Queue<Feature> features) {
		this.features = features;
	}

	@Override
	public FeatureIterator<Feature> features() {
		return new ComplexFeatureIteratorImpl(this.features);
	}

	@Override
	public FeatureType getSchema() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getID() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void accepts(FeatureVisitor visitor, ProgressListener progress)
			throws IOException {
		throw new UnsupportedOperationException();		
	}

	@Override
	public FeatureCollection<FeatureType, Feature> subCollection(Filter filter) {
		throw new UnsupportedOperationException();
	}

	@Override
	public FeatureCollection<FeatureType, Feature> sort(SortBy order) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ReferencedEnvelope getBounds() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <O> O[] toArray(O[] a) {
		throw new UnsupportedOperationException();
	}
}


























