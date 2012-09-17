/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.versioning.decorator;

import java.util.NoSuchElementException;

import org.geogit.api.Ref;
import org.geogit.api.RevTree;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.collection.DecoratingFeatureCollection;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

/**
 * FeatureCollectionDecorator that assigns as {@link ResourceId} as each Feature
 * {@link Feature#getIdentifier() identifier} from the {@link ObjectId} of the
 * current state of the Feature.
 * 
 * @author groldan
 */
class ResourceIdAssigningFeatureCollection<T extends FeatureType, F extends Feature>
        extends DecoratingFeatureCollection<T, F> implements
        FeatureCollection<T, F> {

    protected final FeatureSourceDecorator<T, F> store;

    protected final RevTree currentTypeTree;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected ResourceIdAssigningFeatureCollection(
            final FeatureCollection delegate,
            final FeatureSourceDecorator<T, F> store,
            final RevTree currentTypeTree) {

        super(delegate);

        this.store = store;
        this.currentTypeTree = currentTypeTree;
    }

    protected static class ResourceIdAssigningFeatureIterator<G extends Feature>
            implements FeatureIterator<G> {

        protected FeatureIterator<G> features;

        //@SuppressWarnings("rawtypes")
		//private final FeatureSourceDecorator source;

        private final RevTree typeTree;

        @SuppressWarnings("rawtypes")
        public ResourceIdAssigningFeatureIterator(FeatureIterator<G> features,
                FeatureSourceDecorator source, RevTree currentTypeTree) {
            this.features = features;
            // this.source = source;
            this.typeTree = currentTypeTree;
        }

        @Override
        public boolean hasNext() {
            return features.hasNext();
        }

        @Override
        public G next() throws NoSuchElementException {
            G next = features.next();
            String featureId = next.getIdentifier().getID();
            Ref ref = typeTree.get(featureId);
            String versionId = ref == null ? null : ref.getObjectId()
                    .toString();
            return VersionedFeatureWrapper.wrap(next, versionId);
        }

        @Override
        public void close() {
            features.close();
        }
    }

    @Override
    public FeatureIterator<F> features() {
        return new ResourceIdAssigningFeatureIterator<F>(delegate.features(),
                store, currentTypeTree);
    }

}
