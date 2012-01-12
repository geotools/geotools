/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geotools.data.Query;
import org.geotools.data.store.ReprojectingIterator;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.OperationNotFoundException;

/**
 * An extension to {@linkplain org.geotools.data.complex.DataAccessMappingFeatureIterator} where
 * filter is present. Since join query between 2 or more tables isn't supported, the only way we can
 * query nested features is by applying the filter per simple feature (database row). This is done
 * in hasNext().
 *
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 *
 *
 *
 * @source $URL$
 */
public class FilteringMappingFeatureIterator extends DataAccessMappingFeatureIterator {

    private Filter filter;

    private List<Feature> sources;
    
    private String currentFeatureId;
    
    private CoordinateReferenceSystem targetCrs;
    
    public FilteringMappingFeatureIterator(AppSchemaDataAccess store, FeatureTypeMapping mapping,
            Query query, Query unrolledQuery, Filter filter) throws IOException {
        super(store, mapping, query, unrolledQuery);
        this.filter = filter;
        // don't reproject if there's no geometry, therefore no source CRS (will cause exception)
        this.targetCrs = (sourceFeatures.getSchema().getGeometryDescriptor() != null) ? super.reprojection
                : null;
    }
    
    public FilteringMappingFeatureIterator(AppSchemaDataAccess store, FeatureTypeMapping mapping,
            Query query, Query unrolledQuery, Filter filter, CoordinateReferenceSystem crs)
            throws IOException {
        super(store, mapping, query, unrolledQuery);
        this.filter = filter;
        this.targetCrs = crs;
    }

    @Override
    protected void initialiseSourceFeatures(FeatureTypeMapping mapping, Query query,
            CoordinateReferenceSystem crs) throws IOException {
        super.initialiseSourceFeatures(mapping, query, crs);
    }

    @Override
    protected void closeSourceFeatures() {
        super.closeSourceFeatures();
        this.sources = null;
    }    

    @Override
    public boolean hasNext() {
        if (sources != null) {
            // this is called in the beginning of next()
            // we don't want to actually check the source iterator again
            return true;
        }
        List<Feature> groupedFeatures;
        // check that the feature exists
        boolean matches = false;
        while (!matches && super.hasNext()) {
            sources = null;
            // get all rows with same id from denormalised views
            // and evaluate each row
            currentFeatureId = extractIdForFeature(curSrcFeature);
            try {
                groupedFeatures = super.getSources(currentFeatureId);
                Iterator<Feature> srcFeatures = groupedFeatures.iterator();
                while (!matches && srcFeatures.hasNext()) {
                    // apply filter
                    if (filter == null || filter.evaluate(srcFeatures.next())) {
                        sources = reprojectFeatures(groupedFeatures);
                        matches = true;
                    }
                }
            } catch (IOException e) {
                close();
                throw new RuntimeException(e);
            }
            // reset so next time we call hasNext, it will get the next row
            setHasNextCalled(false);            
        }
        return matches;
    }
    
    /**
     * Reproject source features if reprojection is set in the query. This has to be done after filtering, so 
     * it's consistent with JDBCFeatureSource way of filtering and reprojection.
     * @param srcFeatures
     * @return
     * @throws OperationNotFoundException
     * @throws FactoryRegistryException
     * @throws FactoryException
     */
    @SuppressWarnings("unchecked")
    private List<Feature> reprojectFeatures(List<Feature> srcFeatures) {
        if (targetCrs != null) {
            List<Feature> features = new ArrayList<Feature>();
            GeometryCoordinateSequenceTransformer transformer = new GeometryCoordinateSequenceTransformer();
            Iterator<Feature> reprojectedFeatures;
            try {
                reprojectedFeatures = new ReprojectingIterator(srcFeatures.iterator(), mappedSource
                        .getSchema().getCoordinateReferenceSystem(), targetCrs,
                        (SimpleFeatureType) this.mappedSource.getSchema(), transformer);
                while (reprojectedFeatures.hasNext()) {
                    features.add(reprojectedFeatures.next());
                }
            } catch (Exception e) {
                close();
                throw new RuntimeException ("Failed to reproject features in app-schema!", e);
            }
            return features;
        }
        return srcFeatures;
    }
    
    @Override
    protected String getNextFeatureId() {
        return currentFeatureId;
    }
    
    @Override
    protected List<Feature> getSources(String id) throws IOException {
        // return grouped source features
        List<Feature> features;
        if (sources != null) {
            features = sources;
            // reset for the next hasNext call
            sources = null;
        } else {
            features = super.getSources(id);
        } 
        return features;
    }    
}
