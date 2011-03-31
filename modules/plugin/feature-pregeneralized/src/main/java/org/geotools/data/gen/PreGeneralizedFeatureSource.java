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
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.geotools.data.DataAccess;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultResourceInfo;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureListenerManager;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Repository;
import org.geotools.data.ResourceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.gen.info.Generalization;
import org.geotools.data.gen.info.GeneralizationInfo;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.Hints;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureTypeImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

/**
 * @author Christian Mueller
 * 
 * Feature source for a feature type with pregeneralized geometries
 * 
 * This featue store does business as usual with the exception described here
 * {@link PreGeneralizedDataStore}
 * 
 * 
 *
 * @source $URL$
 */
public class PreGeneralizedFeatureSource implements SimpleFeatureSource {

    protected FeatureListenerManager listenerManager = new FeatureListenerManager();

    protected Repository repository;

    protected GeneralizationInfo info;

    protected PreGeneralizedDataStore dataStore;

    protected Logger log = Logging.getLogger(this.getClass());

    private SimpleFeatureSource baseFeatureSource;

    private Set<Key> supportedHints;

    private Map<Generalization, SimpleFeatureSource> featureSourceCache;

    private QueryCapabilities queryCapabilities;

    private SimpleFeatureTypeImpl featureTyp;

    private Map<Double, int[]> indexMapping;

    private DefaultResourceInfo ri = null;

    public PreGeneralizedFeatureSource(GeneralizationInfo info, Repository repository,
            PreGeneralizedDataStore dataStore) {
        this.info = info;
        this.repository = repository;
        this.dataStore = dataStore;
        reset();
    }

    private void dsNotFoundException(String wsName, String dsName) throws IOException {
        String msg = "Data store named " + dsName;
        if (wsName != null)
            msg += " in workspace " + wsName;
        msg += " not found";
        throw new IOException(msg);
    }

    public void reset() {
        baseFeatureSource = null;
        featureSourceCache = new HashMap<Generalization, SimpleFeatureSource>();
        indexMapping = new HashMap<Double, int[]>();
        supportedHints = null;
        queryCapabilities = null;
        featureTyp = null;

    }

    private SimpleFeatureSource getBaseFeatureSource()
            throws IOException {
        if (baseFeatureSource != null)
            return baseFeatureSource;
        DataStore ds = repository.dataStore(new NameImpl(info.getDataSourceNameSpace(), info
                .getDataSourceName()));
        if (ds == null)
            dsNotFoundException(info.getDataSourceNameSpace(), info.getDataSourceName());
        baseFeatureSource = ds.getFeatureSource(info.getBaseFeatureName());

        // calculate indexMapping
        int[] mapping = calculateIndexMapping(baseFeatureSource.getSchema(), info
                .getGeomPropertyName(), info.getGeomPropertyName());
        indexMapping.put(0.0, mapping);

        return baseFeatureSource;

    }

    private int[] calculateIndexMapping(SimpleFeatureType backendType, String geomProperyName,
            String backendGeomPropertyName) throws IOException {
        int[] mapping = new int[getSchema().getAttributeCount()];
        outer: for (int i = 0; i < mapping.length; i++) {
            String attrName = getSchema().getAttributeDescriptors().get(i).getLocalName();
            if (attrName.equals(geomProperyName))
                attrName = backendGeomPropertyName;
            for (int j = 0; j < backendType.getAttributeDescriptors().size(); j++) {
                if (backendType.getAttributeDescriptors().get(j).getLocalName().equals(attrName)) {
                    mapping[i] = j;
                    continue outer;
                }
            }
            throw new IOException("No attribute " + attrName + " found in "
                    + backendType.getTypeName());
        }
        return mapping;
    }

    public void addFeatureListener(FeatureListener listener) {
        listenerManager.addFeatureListener(this, listener);

    }

    public ReferencedEnvelope getBounds() throws IOException {
        return getBounds(Query.ALL);

    }

    public ReferencedEnvelope getBounds(Query query) throws IOException {

        // SimpleFeatureSource fs = getFeatureSourceFor(query);
        // Query newQuery=getProxyObject(query, fs);
        // return fs.getBounds(newQuery);

        Query newQuery = getProxyObject(query, getBaseFeatureSource());
        return getBaseFeatureSource().getBounds(newQuery);
    }

    public int getCount(Query query) throws IOException {

        // SimpleFeatureSource fs = getFeatureSourceFor(query);
        // Query newQuery=getProxyObject(query, fs);
        // return fs.getCount(newQuery);

        Query newQuery = getProxyObject(query, getBaseFeatureSource());
        return getBaseFeatureSource().getCount(newQuery);
    }

    public DataAccess<SimpleFeatureType, SimpleFeature> getDataStore() {
        return dataStore;
    }

    public SimpleFeatureCollection getFeatures() throws IOException {
        return new PreGeneralizedFeatureCollection(getBaseFeatureSource().getFeatures(),
                getSchema(), indexMapping.get(0.0), info.getGeomPropertyName(), info
                        .getGeomPropertyName());

    }

    public SimpleFeatureCollection getFeatures(Filter filter)
            throws IOException {
        return new PreGeneralizedFeatureCollection(getBaseFeatureSource().getFeatures(filter),
                getSchema(), indexMapping.get(0.0), info.getGeomPropertyName(), info
                        .getGeomPropertyName());
    }

    public SimpleFeatureCollection getFeatures(Query query)
            throws IOException {

        SimpleFeatureSource fs = getFeatureSourceFor(query);
        Query newQuery = getProxyObject(query, fs);
        Generalization di = info.getGeneralizationForDistance(getRequestedDistance(query));
        if (di != null)
            logDistanceInfo(di);
        return new PreGeneralizedFeatureCollection(fs.getFeatures(newQuery), getSchema(),
                indexMapping.get(di == null ? 0.0 : di.getDistance()), info.getGeomPropertyName(),
                getBackendGeometryName(fs));
    }

    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(Query query,
            Transaction transaction) throws IOException {

        SimpleFeatureSource fs = getFeatureSourceFor(query);
        Query newQuery = getProxyObject(query, fs);
        DataAccess<SimpleFeatureType, SimpleFeature> access = fs.getDataStore();
        if (access instanceof DataStore) {
            FeatureReader<SimpleFeatureType, SimpleFeature> backendReader = ((DataStore) access)
                    .getFeatureReader(newQuery, transaction);
            String backendGeometryPropertyName = getBackendGeometryName(fs);

            Generalization di = info.getGeneralizationForDistance(getRequestedDistance(query));
            if (di != null)
                logDistanceInfo(di);

            return new PreGeneralizedFeatureReader(getSchema(), indexMapping.get(di == null ? 0.0
                    : di.getDistance()), backendReader, info.getGeomPropertyName(),
                    backendGeometryPropertyName);
        }
        return null;
    }

    public ResourceInfo getInfo() {
        if (ri != null)
            return ri;
        try {
            ri = new DefaultResourceInfo(); // copy from basefeature
            ri.setBounds(getBaseFeatureSource().getBounds());
            if (getBaseFeatureSource().getSchema().getGeometryDescriptor() != null)
                ri.setCRS(getBaseFeatureSource().getSchema().getGeometryDescriptor()
                        .getCoordinateReferenceSystem());
            ri.setDescription(getBaseFeatureSource().getInfo().getDescription());

            // TODO, causes URI Exception
            // ri.setSchema(getBaseFeatureSource().getInfo().getSchema());

            ri.setTitle(getBaseFeatureSource().getInfo().getTitle());

            ri.setName(getName().getLocalPart());
            Set<String> keyWords = new TreeSet<String>();
            keyWords.addAll(getBaseFeatureSource().getInfo().getKeywords());
            keyWords.add("pregeneralized)");
            ri.setKeywords(keyWords);
        } catch (IOException ex) {
            ri = null;
            throw new RuntimeException(ex);
        }
        return ri;
    }

    public Name getName() {
        return new NameImpl(dataStore.getNamespace() == null ? null : dataStore.getNamespace()
                .toString(), info.getFeatureName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geotools.data.FeatureSource#getQueryCapabilities() A query capabilitiy is supported
     *      only if ALL backend feature sources support it
     */
    public QueryCapabilities getQueryCapabilities() {
        if (queryCapabilities != null)
            return queryCapabilities;
        queryCapabilities = new QueryCapabilities() {

            @Override
            public boolean isOffsetSupported() {
                try {
                    if (!getBaseFeatureSource().getQueryCapabilities().isOffsetSupported())
                        return false;
                    for (Generalization di : info.getGeneralizations()) {
                        SimpleFeatureSource fs = getFeatureSourceFor(di);
                        if (!fs.getQueryCapabilities().isOffsetSupported())
                            return false;
                    }
                    return true;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public boolean isReliableFIDSupported() {
                try {
                    if (!getBaseFeatureSource().getQueryCapabilities().isReliableFIDSupported())
                        return false;
                    for (Generalization di : info.getGeneralizations()) {
                        SimpleFeatureSource fs = getFeatureSourceFor(di);
                        if (!fs.getQueryCapabilities().isReliableFIDSupported())
                            return false;
                    }
                    return true;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public boolean supportsSorting(SortBy[] arg0) {
                try {
                    if (!getBaseFeatureSource().getQueryCapabilities().supportsSorting(arg0))
                        return false;
                    for (Generalization di : info.getGeneralizations()) {
                        SimpleFeatureSource fs = getFeatureSourceFor(di);
                        if (!fs.getQueryCapabilities().supportsSorting(arg0))
                            return false;
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
     * @see org.geotools.data.FeatureSource#getSchema() Schema derived from base feature schema 1)
     *      all generalized geom attributes removed 2) the default gemoetry propery is taken from
     *      the config
     */
    public SimpleFeatureType getSchema() {
        if (featureTyp != null)
            return featureTyp;
        try {
            SimpleFeatureType baseType = getBaseFeatureSource().getSchema();
            List<AttributeDescriptor> attrDescrs = new ArrayList<AttributeDescriptor>();
            outer: for (AttributeDescriptor descr : baseType.getAttributeDescriptors()) {
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
            GeometryDescriptor geomDescr = (GeometryDescriptor) baseType.getDescriptor(info
                    .getGeomPropertyName());

            featureTyp = new SimpleFeatureTypeImpl(new NameImpl(
                    dataStore.getNamespace() == null ? null : dataStore.getNamespace().toString(),
                    info.getFeatureName()), attrDescrs, geomDescr, false, null, null, baseType
                    .getDescription());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return featureTyp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geotools.data.FeatureSource#getSupportedHints() Calculates the supported hints as
     *      intersection of the the generalized features and adds Hints.GEOMETRY_DISTANCE
     */
    public Set<Key> getSupportedHints() {
        if (supportedHints != null)
            return supportedHints;
        Set<Key> hints = new HashSet<Key>();

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
        ;

        hints.add(Hints.GEOMETRY_DISTANCE); // always supported
        supportedHints = Collections.unmodifiableSet(hints);
        return supportedHints;

    }

    public void removeFeatureListener(FeatureListener listener) {
        listenerManager.removeFeatureListener(this, listener);

    }

    /**
     * @param requestedDistance
     * @return the proper feature source for the given distance
     */
    private SimpleFeatureSource getFeatureSourceFor(
            Double requestedDistance) throws IOException {

        if (requestedDistance == null || requestedDistance == 0)
            return getBaseFeatureSource();
        Generalization di = info.getGeneralizationForDistance(requestedDistance);
        return getFeatureSourceFor(di);
    }

    private SimpleFeatureSource getFeatureSourceFor(Generalization di)
            throws IOException {
        if (di == null)
            return getBaseFeatureSource();
        SimpleFeatureSource fs = featureSourceCache.get(di);
        if (fs != null)
            return fs;

        DataStore ds = repository.dataStore(new NameImpl(di.getDataSourceNameSpace(), di
                .getDataSourceName()));
        if (ds == null)
            dsNotFoundException(di.getDataSourceNameSpace(), di.getDataSourceName());
        fs = ds.getFeatureSource(di.getFeatureName());
        featureSourceCache.put(di, fs);

        // calculate indexMapping
        int[] mapping = calculateIndexMapping(fs.getSchema(), info.getGeomPropertyName(), di
                .getGeomPropertyName());
        indexMapping.put(di.getDistance(), mapping);

        return fs;
    }

    private Double getRequestedDistance(Query query) {
        Double result =  (Double) query.getHints().get(Hints.GEOMETRY_DISTANCE);
        if (result == null) {
            log.warning("No hint for geometry distance in query, fallback to base feature" );
        }
        else {
            log.info("Hint geometry distance: "+result);
        }
        return result;
    }

    private SimpleFeatureSource getFeatureSourceFor(Query query)
            throws IOException {
        Double distance = getRequestedDistance(query);

        String geomPropertyName = info.getGeomPropertyName(); // the geometry for which we have
        // generalizations
        String[] queryProperyNames = query.getPropertyNames();

        if (queryProperyNames != null) {
            for (String prop : queryProperyNames) { // check if geom property name was specified in
                // the query
                if (prop.equals(geomPropertyName))
                    return getFeatureSourceFor(distance);
            }
        } else { // we have Query.ALL
            return getFeatureSourceFor(distance);
        }
        // no geometry in the query for which generalizations are present.
        return getBaseFeatureSource();
    }

    /**
     * @param query
     *            the query object
     * @param fs
     *            the backend feature surce
     * @return Proxy modified for backend feature source
     * 
     * create a proxy for the origianl query object 1) typeName has to be changed to backend type
     * name 2) geometry property name has tob be changed to backend geometry property name
     * 
     */

    private String getBackendGeometryName(SimpleFeatureSource fs) {
        // look for the backend geom property name
        for (Entry<Generalization, SimpleFeatureSource> entry : featureSourceCache
                .entrySet()) {
            if (entry.getValue() == fs) {
                return entry.getKey().getGeomPropertyName();
            }
        }
        return info.getGeomPropertyName(); // use prop name from base feature source
    }

    protected Query getProxyObject(Query query, SimpleFeatureSource fs) {

        String baseGeomPropertyName = info.getGeomPropertyName(); // generalized geom property
        String backendGeomPropertyName = getBackendGeometryName(fs);

        String[] originalPropNames = query.getPropertyNames();
        String[] newPropNames;
        if (originalPropNames == Query.ALL_NAMES) {
            newPropNames = new String[getSchema().getAttributeCount()];
            for (int i = 0; i < newPropNames.length; i++) {
                AttributeDescriptor attrDescr = getSchema().getAttributeDescriptors().get(i);
                newPropNames[i] = attrDescr.getLocalName().equals(baseGeomPropertyName) ? backendGeomPropertyName
                        : attrDescr.getLocalName();
            }
        } else {
            newPropNames = new String[originalPropNames.length];
            for (int i = 0; i < newPropNames.length; i++) {
                newPropNames[i] = originalPropNames[i].equals(baseGeomPropertyName) ? backendGeomPropertyName
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
