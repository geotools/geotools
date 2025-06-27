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

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.FeatureListener;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Query;
import org.geotools.api.data.QueryCapabilities;
import org.geotools.api.data.Repository;
import org.geotools.api.data.ResourceInfo;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.data.DefaultResourceInfo;
import org.geotools.data.FeatureListenerManager;
import org.geotools.data.gen.info.Generalization;
import org.geotools.data.gen.info.GeneralizationInfo;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;

/**
 * @author Christian Mueller
 *     <p>Feature source for a feature type with pregeneralized geometries
 *     <p>This featue store does business as usual with the exception described here {@link PreGeneralizedDataStore}
 */
public class PreGeneralizedFeatureSource implements SimpleFeatureSource {

    protected FeatureListenerManager listenerManager = new FeatureListenerManager();

    protected Repository repository;

    protected GeneralizationInfo info;

    protected PreGeneralizedDataStore dataStore;

    protected Logger log = Logging.getLogger(this.getClass());

    private Set<Key> supportedHints;

    private QueryCapabilities queryCapabilities;

    private SimpleFeatureTypeImpl featureTyp;

    private Map<Double, int[]> indexMapping;

    private DefaultResourceInfo ri = null;

    public PreGeneralizedFeatureSource(
            GeneralizationInfo info, Repository repository, PreGeneralizedDataStore dataStore) {
        this.info = info;
        this.repository = repository;
        this.dataStore = dataStore;
        reset();
    }

    private void dsNotFoundException(String wsName, String dsName) throws IOException {
        String msg = "Data store named " + dsName;
        if (wsName != null) msg += " in workspace " + wsName;
        msg += " not found";
        throw new IOException(msg);
    }

    public void reset() {
        indexMapping = new HashMap<>();
        supportedHints = null;
        queryCapabilities = null;
        featureTyp = null;
    }

    private SimpleFeatureSource getBaseFeatureSource() throws IOException {
        DataStore ds = repository.dataStore(new NameImpl(info.getDataSourceNameSpace(), info.getDataSourceName()));
        if (ds == null) dsNotFoundException(info.getDataSourceNameSpace(), info.getDataSourceName());
        return ds.getFeatureSource(info.getBaseFeatureName());
    }

    private int[] calculateIndexMapping(
            SimpleFeatureType backendType, String geomProperyName, String backendGeomPropertyName) throws IOException {
        int[] mapping = new int[getSchema().getAttributeCount()];
        outer:
        for (int i = 0; i < mapping.length; i++) {
            String attrName = getSchema().getAttributeDescriptors().get(i).getLocalName();
            if (attrName.equals(geomProperyName)) attrName = backendGeomPropertyName;
            for (int j = 0; j < backendType.getAttributeDescriptors().size(); j++) {
                if (backendType.getAttributeDescriptors().get(j).getLocalName().equals(attrName)) {
                    mapping[i] = j;
                    continue outer;
                }
            }
            throw new IOException("No attribute " + attrName + " found in " + backendType.getTypeName());
        }
        return mapping;
    }

    @Override
    public void addFeatureListener(FeatureListener listener) {
        listenerManager.addFeatureListener(this, listener);
    }

    @Override
    public ReferencedEnvelope getBounds() throws IOException {
        return getBounds(Query.ALL);
    }

    @Override
    public ReferencedEnvelope getBounds(Query query) throws IOException {
        Generalization di = info.getGeneralizationForDistance(getRequestedDistance(query));
        SimpleFeatureSource fs = getBaseFeatureSource();
        Query newQuery = getProxyObject(query, fs, di);

        return getBaseFeatureSource().getBounds(newQuery);
    }

    @Override
    public int getCount(Query query) throws IOException {
        Generalization di = info.getGeneralizationForDistance(getRequestedDistance(query));
        SimpleFeatureSource fs = getBaseFeatureSource();
        Query newQuery = getProxyObject(query, fs, di);

        return getBaseFeatureSource().getCount(newQuery);
    }

    @Override
    public DataAccess<SimpleFeatureType, SimpleFeature> getDataStore() {
        return dataStore;
    }

    @Override
    public SimpleFeatureCollection getFeatures() throws IOException {
        return new PreGeneralizedFeatureCollection(
                getBaseFeatureSource().getFeatures(),
                getSchema(),
                getSchema(),
                indexMapping.get(0.0),
                info.getGeomPropertyName(),
                info.getGeomPropertyName());
    }

    @Override
    public SimpleFeatureCollection getFeatures(Filter filter) throws IOException {
        return new PreGeneralizedFeatureCollection(
                getBaseFeatureSource().getFeatures(filter),
                getSchema(),
                getSchema(),
                indexMapping.get(0.0),
                info.getGeomPropertyName(),
                info.getGeomPropertyName());
    }

    @Override
    public SimpleFeatureCollection getFeatures(Query query) throws IOException {

        SimpleFeatureSource fs = getFeatureSourceFor(query);
        Generalization di = info.getGeneralizationForDistance(getRequestedDistance(query));
        Query newQuery = getProxyObject(query, fs, di);
        if (di != null) logDistanceInfo(di);
        return new PreGeneralizedFeatureCollection(
                fs.getFeatures(newQuery),
                getSchema(),
                getReturnedSchema(getSchema(), query),
                query.getPropertyNames() == Query.ALL_NAMES
                        ? indexMapping.get(di == null ? 0.0 : di.getDistance())
                        : null,
                info.getGeomPropertyName(),
                getBackendGeometryName(di));
    }

    private SimpleFeatureType getReturnedSchema(SimpleFeatureType schema, Query query) {
        if (query.getPropertyNames() == Query.ALL_NAMES) {
            return schema;
        }

        return SimpleFeatureTypeBuilder.retype(schema, query.getPropertyNames());
    }

    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(Query query, Transaction transaction)
            throws IOException {

        SimpleFeatureSource fs = getFeatureSourceFor(query);
        DataAccess<SimpleFeatureType, SimpleFeature> access = fs.getDataStore();
        if (access instanceof DataStore) {
            Generalization di = info.getGeneralizationForDistance(getRequestedDistance(query));
            if (di != null) logDistanceInfo(di);

            Query newQuery = getProxyObject(query, fs, di);

            String backendGeometryPropertyName = getBackendGeometryName(di);

            return new PreGeneralizedFeatureReader(
                    getSchema(),
                    getReturnedSchema(getSchema(), query),
                    indexMapping.get(di == null ? 0.0 : di.getDistance()),
                    ((DataStore) access).getFeatureReader(newQuery, transaction),
                    info.getGeomPropertyName(),
                    backendGeometryPropertyName);
        }
        return null;
    }

    @Override
    public ResourceInfo getInfo() {
        if (ri != null) return ri;
        try {
            ri = new DefaultResourceInfo(); // copy from basefeature
            ri.setBounds(getBaseFeatureSource().getBounds());
            if (getBaseFeatureSource().getSchema().getGeometryDescriptor() != null)
                ri.setCRS(getBaseFeatureSource()
                        .getSchema()
                        .getGeometryDescriptor()
                        .getCoordinateReferenceSystem());
            ri.setDescription(getBaseFeatureSource().getInfo().getDescription());

            // TODO, causes URI Exception
            // ri.setSchema(getBaseFeatureSource().getInfo().getSchema());

            ri.setTitle(getBaseFeatureSource().getInfo().getTitle());

            ri.setName(getName().getLocalPart());
            Set<String> keyWords = new TreeSet<>();
            keyWords.addAll(getBaseFeatureSource().getInfo().getKeywords());
            keyWords.add("pregeneralized)");
            ri.setKeywords(keyWords);
        } catch (IOException ex) {
            ri = null;
            throw new RuntimeException(ex);
        }
        return ri;
    }

    @Override
    public Name getName() {
        return new NameImpl(
                dataStore.getNamespace() == null
                        ? null
                        : dataStore.getNamespace().toString(),
                info.getFeatureName());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.data.FeatureSource#getQueryCapabilities() A query capabilitiy is supported
     *      only if ALL backend feature sources support it
     */
    @Override
    public QueryCapabilities getQueryCapabilities() {
        if (queryCapabilities != null) return queryCapabilities;
        queryCapabilities = new QueryCapabilities() {

            @Override
            public boolean isOffsetSupported() {
                try {
                    if (!getBaseFeatureSource().getQueryCapabilities().isOffsetSupported()) return false;
                    for (Generalization di : info.getGeneralizations()) {
                        SimpleFeatureSource fs = getFeatureSourceFor(di);
                        if (!fs.getQueryCapabilities().isOffsetSupported()) return false;
                    }
                    return true;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public boolean isReliableFIDSupported() {
                try {
                    if (!getBaseFeatureSource().getQueryCapabilities().isReliableFIDSupported()) return false;
                    for (Generalization di : info.getGeneralizations()) {
                        SimpleFeatureSource fs = getFeatureSourceFor(di);
                        if (!fs.getQueryCapabilities().isReliableFIDSupported()) return false;
                    }
                    return true;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public boolean supportsSorting(SortBy... arg0) {
                try {
                    if (!getBaseFeatureSource().getQueryCapabilities().supportsSorting(arg0)) return false;
                    for (Generalization di : info.getGeneralizations()) {
                        SimpleFeatureSource fs = getFeatureSourceFor(di);
                        if (!fs.getQueryCapabilities().supportsSorting(arg0)) return false;
                    }
                    return true;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        return queryCapabilities;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.data.FeatureSource#getSchema() Schema derived from base feature schema 1)
     *      all generalized geom attributes removed 2) the default gemoetry propery is taken from
     *      the config
     */
    @Override
    public SimpleFeatureType getSchema() {
        if (featureTyp != null) return featureTyp;
        try {
            SimpleFeatureType baseType = getBaseFeatureSource().getSchema();
            List<AttributeDescriptor> attrDescrs = new ArrayList<>();
            outer:
            for (AttributeDescriptor descr : baseType.getAttributeDescriptors()) {
                for (Generalization di : info.getGeneralizations()) {
                    if (di.getDataSourceName().equals(info.getDataSourceName())) { // same
                        // datasource
                        if (di.getFeatureName().equals(baseType.getName().getLocalPart())) { // same
                            // feature
                            if (di.getGeomPropertyName().equals(descr.getName().getLocalPart())) // is
                                // gneralized
                                // geom
                                continue outer;
                        }
                    }
                }
                attrDescrs.add(descr);
            }
            GeometryDescriptor geomDescr = (GeometryDescriptor) baseType.getDescriptor(info.getGeomPropertyName());

            featureTyp = new SimpleFeatureTypeImpl(
                    new NameImpl(
                            dataStore.getNamespace() == null
                                    ? null
                                    : dataStore.getNamespace().toString(),
                            info.getFeatureName()),
                    attrDescrs,
                    geomDescr,
                    false,
                    null,
                    null,
                    baseType.getDescription());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return featureTyp;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.data.FeatureSource#getSupportedHints() Calculates the supported hints as
     *      intersection of the the generalized features and adds Hints.GEOMETRY_DISTANCE
     */
    @Override
    public Set<Key> getSupportedHints() {
        if (supportedHints != null) return supportedHints;
        Set<Key> hints = new HashSet<>();

        // calculate the supported hints, which is the intersection of supported Hints for all
        // feature sources
        try {
            hints.addAll(getBaseFeatureSource().getSupportedHints()); // start with base feature
            // source
            for (Generalization di : info.getGeneralizations()) {
                SimpleFeatureSource fs = getFeatureSourceFor(di);
                hints.retainAll(fs.getSupportedHints());
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        hints.add(Hints.GEOMETRY_DISTANCE); // always supported
        supportedHints = Collections.unmodifiableSet(hints);
        return supportedHints;
    }

    @Override
    public void removeFeatureListener(FeatureListener listener) {
        listenerManager.removeFeatureListener(this, listener);
    }

    /** @return the proper feature source for the given distance */
    private SimpleFeatureSource getFeatureSourceFor(Double requestedDistance) throws IOException {

        if (requestedDistance == null || requestedDistance == 0) return getBaseFeatureSource();
        Generalization di = info.getGeneralizationForDistance(requestedDistance);
        return getFeatureSourceFor(di);
    }

    private SimpleFeatureSource getFeatureSourceFor(Generalization di) throws IOException {
        if (di == null) return getBaseFeatureSource();

        DataStore ds = repository.dataStore(new NameImpl(di.getDataSourceNameSpace(), di.getDataSourceName()));
        if (ds == null) dsNotFoundException(di.getDataSourceNameSpace(), di.getDataSourceName());
        SimpleFeatureSource fs = ds.getFeatureSource(di.getFeatureName());

        // calculate indexMapping
        int[] mapping = calculateIndexMapping(fs.getSchema(), info.getGeomPropertyName(), di.getGeomPropertyName());
        indexMapping.put(di.getDistance(), mapping);

        return fs;
    }

    private Double getRequestedDistance(Query query) {
        Double result = (Double) query.getHints().get(Hints.GEOMETRY_DISTANCE);
        if (result == null) {
            /*
             * Check if hints GEOMETRY_SIMPLIFICATION is active, when both are supported (because the
             * wrapper store supports simplification) only simplification is sent down, but we can use it to pick
             * a starting geometry to simplify further.
             */
            result = (Double) query.getHints().get(Hints.GEOMETRY_SIMPLIFICATION);
            log.fine("Hint for geometry simplification in query, fallback to base feature");
        }
        if (result == null) {
            log.fine("No hint for geometry distance in query, fallback to base feature");
        } else {
            if (log.isLoggable(Level.FINE)) {
                log.fine("Hint geometry distance: " + result);
            }
        }
        return result;
    }

    private SimpleFeatureSource getFeatureSourceFor(Query query) throws IOException {
        Double distance = getRequestedDistance(query);

        String geomPropertyName = info.getGeomPropertyName(); // the geometry for which we have
        // generalizations
        String[] queryProperyNames = query.getPropertyNames();

        if (queryProperyNames != null) {
            for (String prop : queryProperyNames) { // check if geom property name was specified in
                // the query
                if (prop.equals(geomPropertyName)) return getFeatureSourceFor(distance);
            }
        } else { // we have Query.ALL
            return getFeatureSourceFor(distance);
        }
        // no geometry in the query for which generalizations are present.
        return getBaseFeatureSource();
    }

    /**
     * @param di the backend dimension
     * @return Proxy modified for backend feature source
     *     <p>create a proxy for the origianl query object 1) typeName has to be changed to backend type name 2)
     *     geometry property name has tob be changed to backend geometry property name
     */
    private String getBackendGeometryName(Generalization di) {
        // look for the backend geom property name
        if (di != null) {
            return di.getGeomPropertyName();
        }
        return info.getGeomPropertyName(); // use prop name from base feature source
    }

    protected Query getProxyObject(Query query, SimpleFeatureSource fs, Generalization di) {

        String baseGeomPropertyName = info.getGeomPropertyName(); // generalized geom property
        String backendGeomPropertyName = getBackendGeometryName(di);

        String[] originalPropNames = query.getPropertyNames();
        String[] newPropNames;
        if (originalPropNames == Query.ALL_NAMES) {
            newPropNames = new String[getSchema().getAttributeCount()];
            for (int i = 0; i < newPropNames.length; i++) {
                AttributeDescriptor attrDescr =
                        getSchema().getAttributeDescriptors().get(i);
                newPropNames[i] = attrDescr.getLocalName().equals(baseGeomPropertyName)
                        ? backendGeomPropertyName
                        : attrDescr.getLocalName();
            }
        } else {
            newPropNames = new String[originalPropNames.length];
            for (int i = 0; i < newPropNames.length; i++) {
                newPropNames[i] = originalPropNames[i].equals(baseGeomPropertyName)
                        ? backendGeomPropertyName
                        : originalPropNames[i];
            }
        }

        Query newQuery = new Query(query);
        newQuery.setTypeName(fs.getName().getLocalPart());
        newQuery.setPropertyNames(newPropNames);

        return newQuery;
    }

    protected void logDistanceInfo(Generalization di) {
        StringBuffer buff = new StringBuffer("Using generalizsation: ");
        buff.append(di.getDataSourceName()).append(" ");
        buff.append(di.getFeatureName()).append(" ");
        buff.append(di.getGeomPropertyName()).append(" ");
        buff.append(di.getDistance());
        log.info(buff.toString());
    }
}
