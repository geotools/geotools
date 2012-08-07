package org.geotools.data.store;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

import javax.xml.namespace.QName;

import org.geotools.data.FeatureReader;
import org.geotools.data.store.ContentFeatureCollection.WrappingIterator;
import org.geotools.feature.CollectionListener;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.collection.ComplexFeatureIteratorImpl;
import org.geotools.feature.collection.FeatureIteratorImpl;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
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
	public void close(FeatureIterator<Feature> close) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void close(Iterator<Feature> close) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void addListener(CollectionListener listener)
			throws NullPointerException {
		throw new RuntimeException("Not implemented. Deprecated anyway, don't use.");
	}

	@Override
	public void removeListener(CollectionListener listener)
			throws NullPointerException {
		throw new RuntimeException("Not implemented. Deprecated anyway, don't use.");
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
	public Iterator<Feature> iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void purge() {
		throw new UnsupportedOperationException();		
	}

	@Override
	public boolean add(Feature obj) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends Feature> collection) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(
			FeatureCollection<? extends FeatureType, ? extends Feature> resource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
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
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
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


























