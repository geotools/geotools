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

package org.geotools.data.gen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.util.ProgressListener;

/**
 * @author Christian Mueller
 *     <p>Implementation of {@link FeatureCollection} for {@link PreGeneralizedSimpleFeature}
 *     <p>This collection is read only, modifying methods result in {@link
 *     UnsupportedOperationException}
 */
public class PreGeneralizedFeatureCollection implements SimpleFeatureCollection {

    protected SimpleFeatureCollection backendCollection;

    protected SimpleFeatureType featureType;

    protected SimpleFeatureType returnedFeatureType;

    protected String geomPropertyName, backendGeomPropertyName;

    protected int[] indexMapping;

    public PreGeneralizedFeatureCollection(
            SimpleFeatureCollection backendCollection,
            SimpleFeatureType featureType,
            SimpleFeatureType returnedFeatureType,
            int[] indexMapping,
            String geomPropertyName,
            String backendGeomPropertyName) {
        super();
        this.backendCollection = backendCollection;
        this.featureType = featureType;
        this.returnedFeatureType = returnedFeatureType;
        this.geomPropertyName = geomPropertyName;
        this.backendGeomPropertyName = backendGeomPropertyName;
        this.indexMapping = indexMapping;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.feature.FeatureCollection#accepts(org.opengis.feature.FeatureVisitor,
     *      org.opengis.util.ProgressListener) Logic copied from DefaultFeatureCollection class
     */
    @Override
    public void accepts(FeatureVisitor visitor, ProgressListener progress) throws IOException {
        DataUtilities.visit(this, visitor, progress);
    }

    @Override
    public boolean contains(Object feature) {
        if (feature instanceof PreGeneralizedSimpleFeature)
            return backendCollection.contains(((PreGeneralizedSimpleFeature) feature).feature);
        else return backendCollection.contains(feature);
    }

    @Override
    public boolean containsAll(Collection coll) {
        List<Object> searchColl = new ArrayList<>();
        Iterator it = coll.iterator();
        while (it.hasNext()) {
            Object feature = it.next();
            if (feature instanceof PreGeneralizedSimpleFeature)
                searchColl.add(((PreGeneralizedSimpleFeature) feature).feature);
            else searchColl.add(feature);
        }
        return backendCollection.containsAll(searchColl);
    }

    @Override
    public SimpleFeatureIterator features() {
        return new PreGeneralizedFeatureIterator(
                backendCollection.features(),
                featureType,
                returnedFeatureType,
                indexMapping,
                geomPropertyName,
                backendGeomPropertyName);
    }

    @Override
    public ReferencedEnvelope getBounds() {
        return backendCollection.getBounds();
    }

    @Override
    public String getID() {
        return "pregeneralizd featurecollection";
    }

    @Override
    public SimpleFeatureType getSchema() {
        return returnedFeatureType;
    }

    @Override
    public boolean isEmpty() {
        return backendCollection.isEmpty();
    }

    @Override
    public int size() {
        return backendCollection.size();
    }

    @Override
    public SimpleFeatureCollection sort(SortBy sortBy) {
        SimpleFeatureCollection fColl = backendCollection.sort(sortBy);
        if (fColl == null) return null;
        return new PreGeneralizedFeatureCollection(
                fColl,
                featureType,
                returnedFeatureType,
                indexMapping,
                geomPropertyName,
                backendGeomPropertyName);
    }

    @Override
    public SimpleFeatureCollection subCollection(Filter filter) {
        SimpleFeatureCollection fColl = backendCollection.subCollection(filter);
        if (fColl == null) return null;
        return new PreGeneralizedFeatureCollection(
                fColl,
                featureType,
                returnedFeatureType,
                indexMapping,
                geomPropertyName,
                backendGeomPropertyName);
    }

    @Override
    public Object[] toArray() {
        Object[] res = backendCollection.toArray();
        for (int i = 0; i < res.length; i++) {
            res[i] =
                    new PreGeneralizedSimpleFeature(
                            getSchema(),
                            getSchema(),
                            indexMapping,
                            (SimpleFeature) res[i],
                            geomPropertyName,
                            backendGeomPropertyName);
        }
        return res;
    }

    @Override
    public <O> O[] toArray(O[] a) {
        O[] res = backendCollection.toArray(a);
        for (int i = 0; i < res.length; i++) {
            @SuppressWarnings("unchecked")
            O cast =
                    (O)
                            new PreGeneralizedSimpleFeature(
                                    getSchema(),
                                    getSchema(),
                                    indexMapping,
                                    (SimpleFeature) res[i],
                                    geomPropertyName,
                                    backendGeomPropertyName);
            res[i] = cast;
        }
        return res;
    }
}
