/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.store.DataFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.identity.FeatureId;


/**
 * Wraps multiple feature collections into a single.
 * <p>
 * This feature collection is used for wfs feature collections which can be made
 * up of features from different schemas.
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 *
 * @source $URL$
 */
public class CompositeFeatureCollection extends DataFeatureCollection {
    /**
     * wrapped collecitons
     */
    List<FeatureCollection> collections;

    public CompositeFeatureCollection(List<FeatureCollection> collections) {
        this.collections = collections;
    }

    protected Iterator<SimpleFeature> openIterator() throws IOException {
        return new CompositeIterator();
    }

    public SimpleFeatureType getSchema() {
        return null;
    }

    public ReferencedEnvelope getBounds() {
        return DataUtilities.bounds(this);
    }

    public int getCount() throws IOException {
        int count = 0;
        Iterator i = iterator();

        try {
            while (i.hasNext()) {
                i.next();
                count++;
            }
        } finally {
            close(i);
        }

        return count;
    }

    class CompositeIterator implements Iterator<SimpleFeature> {
        int index;
        FeatureIterator iterator;

        public CompositeIterator() {
            index = 0;
        }

        public void remove() {
        }

        public boolean hasNext() {
            //is there a current iterator that has another element
            if ((iterator != null) && iterator.hasNext()) {
                return true;
            }

            //get the next iterator
            while (index < collections.size()) {
                //close current before we move to next
                if (iterator != null) {
                    iterator.close();
                }

                //grap next
                iterator = ((FeatureCollection) collections.get(index++)).features();

                if (iterator.hasNext()) {
                    return true;
                }
            }

            //no more
            if (iterator != null) {
                //close the last iterator
                iterator.close();
            }
            return false;
        }

        public SimpleFeature next() {
            return (SimpleFeature) iterator.next();
        }
    }

    public boolean addAll(Collection arg0) {
        throw new RuntimeException("Can't add to a composite featurecollection; you need to add to one of the constituent collections direclty.");
    }
    public <T> T[] toArray(T[] arg0) {
        List<T> list = new ArrayList<T>();
        Iterator it = collections.iterator();
        while(it.hasNext()){
            FeatureCollection col = (FeatureCollection)it.next();
            FeatureIterator it2 = col.features();
            try {
                while (it2.hasNext()){
                    list.add((T)it.next());
                }
            }
            finally {
                it2.close();
            }
        }
        
        return list.toArray(arg0);
    }

    public FeatureId getIdentifier() {
        throw new RuntimeException("Can't get the id for a composite featurecollection; you need to identify the consituent collections directly.");
    }
}
