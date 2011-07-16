package org.geotools.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.EmptyFeatureCollection;
import org.geotools.data.store.FilteringIterator;
import org.geotools.data.store.ReTypingIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.collection.AbstractFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.index.strtree.STRtree;

/**
 * A caching feature source for fast data access.
 * <p>
 * This feature source is used as a wrapper offering a spatial index, for a quick
 * user interface experience at the cost of memory, all data returned by the last query 
 * will be loaded in a in memory spatial index.
 * <p>This class is meant as an example, it is not tested enough to be considered production
 * worthy
 *
 * @author Andrea Aime - GeoSolutions
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/swing/src/main/java/org/geotools/data/CachingFeatureSource.java $
 */
public class CachingFeatureSource implements SimpleFeatureSource {
    private SimpleFeatureSource wrapped;

    private STRtree index;

    private boolean dirty;

    private Query cachedQuery;

    private Envelope cachedBounds;

    private SimpleFeatureType cachedSchema;

    private Envelope originalBounds;

    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    private static final Set<Class> supportedFilterTypes = new HashSet<Class>(Arrays.asList(
            BBOX.class, Contains.class, Crosses.class, DWithin.class, Equals.class,
            Intersects.class, Overlaps.class, Touches.class, Within.class));

    public CachingFeatureSource( FeatureSource original) throws IOException {
        this( DataUtilities.simple( original ));
    }
    
    public CachingFeatureSource(SimpleFeatureSource original) throws IOException {
        this.wrapped = original;
        this.originalBounds = original.getBounds();
        if (originalBounds == null)
            originalBounds = new Envelope(-Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE,
                    Double.MAX_VALUE);
    }

    private void fillCache(Query query) throws IOException {
        Query cloned = new DefaultQuery(query);
        cloned.getHints().remove(Hints.GEOMETRY_DISTANCE);
        
        FeatureCollection features = wrapped.getFeatures(cloned);
        FeatureIterator fi = features.features();
        index = null;
        STRtree newIndex = new STRtree();
        while (fi.hasNext()) {
            // consider turning all geometries into packed ones, to save space
            Feature f = fi.next();
            newIndex.insert(ReferencedEnvelope.reference(f.getBounds()), f);
        }
        fi.close();
        index = newIndex;
        cachedQuery = query;
        cachedSchema = (SimpleFeatureType) features.getSchema();
        cachedBounds = getEnvelope(query.getFilter());
        dirty = false;
    }

    public void addFeatureListener(FeatureListener listener) {
        wrapped.addFeatureListener(listener);
    }

    public void removeFeatureListener(FeatureListener listener) {
        wrapped.removeFeatureListener(listener);
    }

    public DataStore getDataStore() {
        return (DataStore) wrapped.getDataStore();
    }

    public ReferencedEnvelope getBounds() throws IOException {
        return wrapped.getBounds();
    }

    public ReferencedEnvelope getBounds(Query query) throws IOException {
        return wrapped.getBounds(query);
    }

    public int getCount(Query query) throws IOException {
        return wrapped.getCount(query);
    }

    public SimpleFeatureType getSchema() {
        return wrapped.getSchema();
    }

    public SimpleFeatureCollection getFeatures() throws IOException {
        return getFeatures(Filter.INCLUDE);
    }

    public SimpleFeatureCollection getFeatures(Filter filter) throws IOException {
        return getFeatures(new DefaultQuery(wrapped.getSchema().getName().getLocalPart(), filter));
    }

    public SimpleFeatureCollection getFeatures(Query query) throws IOException {
        String schemaName = wrapped.getSchema().getName().getLocalPart();
        if (query.getTypeName() != null && !schemaName.equals(query.getTypeName())) {
            throw new DataSourceException("Typename mismatch, query asks for '"
                    + query.getTypeName() + " but this feature source provides '" + schemaName
                    + "'");
        }

        return getFeatureCollection(query, getEnvelope(query.getFilter()));
    }

    private SimpleFeatureCollection getFeatureCollection(Query query, Envelope bounds) throws IOException {
        try {
            SimpleFeatureType base = wrapped.getSchema();
            SimpleFeatureType alternate = base;
            if (query.getPropertyNames() != Query.ALL_NAMES) {
                alternate = SimpleFeatureTypeBuilder.retype(base, query.getPropertyNames());
                if (alternate.equals(base))
                    alternate = base;
            }

            return new CachingFeatureCollection(bounds, base, alternate, query);
        } catch (Exception e) {
            throw new DataSourceException(
                    "Error occurred extracting features from the spatial index", e);
        }
    }

    /**
     * Same as DataUtilities.reType, but without the cloning that uselessly wastes CPU cycles...
     * 
     * @param featureType
     * @param feature
     * @return
     * @throws IllegalAttributeException
     */
    public static SimpleFeature reType(SimpleFeatureType featureType, SimpleFeature feature)
            throws IllegalAttributeException {
        FeatureType origional = feature.getFeatureType();

        if (featureType.equals(origional)) {
            return SimpleFeatureBuilder.copy(feature);
        }

        String id = feature.getID();
        int numAtts = featureType.getAttributeCount();
        Object[] attributes = new Object[numAtts];
        String xpath;

        for (int i = 0; i < numAtts; i++) {
            AttributeDescriptor curAttType = featureType.getDescriptor(i);
            attributes[i] = feature.getAttribute(curAttType.getLocalName());
        }

        return SimpleFeatureBuilder.build(featureType, attributes, id);
    }

    boolean isSubQuery(Query query) {
        // no cached data?
        if (cachedQuery == null)
            return false;

        // do we miss some properties?
        String[] cachedPropNames = cachedQuery.getPropertyNames();
        String[] propNames = query.getPropertyNames();
        if (cachedPropNames != Query.ALL_NAMES
                && (propNames == Query.ALL_NAMES || !Arrays.asList(cachedPropNames).containsAll(
                        Arrays.asList(propNames))))
            return false;

        Filter[] filters = splitFilters(query);
        Filter[] cachedFilters = splitFilters(cachedQuery);
        if (!filters[0].equals(cachedFilters[0]))
            return false;

        Envelope envelope = getEnvelope(filters[1]);
        return cachedBounds.contains(envelope);
    }

    Envelope getEnvelope(Filter filter) {
        Envelope result = originalBounds;
        if (filter instanceof And) {
            Envelope bounds = new Envelope();
            for (Iterator iter = ((And) filter).getChildren().iterator(); iter.hasNext();) {
                Filter f = (Filter) iter.next();
                Envelope e = getEnvelope(f);
                if (e == null)
                    return null;
                else
                    bounds.expandToInclude(e);
            }
            result = bounds;
        } else if (filter instanceof BinarySpatialOperator) {
            BinarySpatialOperator gf = (BinarySpatialOperator) filter;
            if (supportedFilterTypes.contains(gf.getClass())) {
                Expression lg = gf.getExpression1();
                Expression rg = gf.getExpression2();
                if (lg instanceof Literal) {
                    Geometry g = (Geometry) ((Literal) lg).getValue();
                    if (rg instanceof PropertyName)
                        result = g.getEnvelopeInternal();
                } else if (rg instanceof Literal) {
                    Geometry g = (Geometry) ((Literal) rg).getValue();
                    if (lg instanceof PropertyName)
                        result = g.getEnvelopeInternal();
                }
            }
        }
        return result.intersection(originalBounds);
    }

    /**
     * Splits a query into two parts, a spatial component that can be turned into a bbox filter (by
     * including some more feature in the result) and a residual component that we cannot address
     * with the spatial index
     * 
     * @param query
     */
    Filter[] splitFilters(Query query) {
        Filter filter = query.getFilter();
        if (filter == null || filter.equals(Filter.EXCLUDE)) {
            return new Filter[] { Filter.EXCLUDE, bboxFilter(originalBounds) };
        }

        if (!(filter instanceof And)) {
            Envelope envelope = getEnvelope(filter);
            if (envelope == null)
                return new Filter[] { Filter.EXCLUDE, bboxFilter(originalBounds) };
            else
                return new Filter[] { Filter.EXCLUDE, bboxFilter(envelope) };
        }

        And and = (And) filter;
        List residuals = new ArrayList();
        List bboxBacked = new ArrayList();
        for (Iterator it = and.getChildren().iterator(); it.hasNext();) {
            Filter child = (Filter) it.next();
            if (getEnvelope(child) != null) {
                bboxBacked.add(child);
            } else {
                residuals.add(child);
            }
        }

        return new Filter[] { (Filter) ff.and(residuals), (Filter) ff.and(bboxBacked) };
    }

    private BBOX bboxFilter(Envelope bbox) {
        // GeometryFilterImpl gf = (GeometryFilterImpl) ff
        // .createGeometryFilter(GeometryFilter.GEOMETRY_BBOX);
        // gf.setExpression1(ff.createAttributeExpression(wrapped.getSchema().getDefaultGeometry()));
        // gf.setExpression2(ff.createBBoxExpression(bbox));
        // return gf;
        return ff.bbox(wrapped.getSchema().getGeometryDescriptor().getLocalName(), bbox.getMinX(),
                bbox.getMinY(), bbox.getMaxX(), bbox.getMaxY(), null);
    }

    public ResourceInfo getInfo() {
        return wrapped.getInfo();
    }

    public Name getName() {
        return wrapped.getName();
    }

    public QueryCapabilities getQueryCapabilities() {
        return wrapped.getQueryCapabilities();
    }

    public Set getSupportedHints() {
        HashSet hints = new HashSet(wrapped.getSupportedHints());
        hints.remove(Hints.FEATURE_DETACHED);
        return hints;
    }
    
    /**
     * A custom feature collection directly peeking on the feature source spatial index
     * 
     * @author Andrea Aime - Geosolutions
     */
    final class CachingFeatureCollection extends AbstractFeatureCollection {

        private SimpleFeatureType sourceSchema;
        private SimpleFeatureType targetSchema;
        private Query query;
        private ReferencedEnvelope queryBounds = null;
        
        protected CachingFeatureCollection(Envelope queryBounds, SimpleFeatureType sourceSchema, 
                SimpleFeatureType targetSchema, Query query) {
            super(targetSchema);
            this.sourceSchema = sourceSchema;
            this.targetSchema = targetSchema;
            this.query = query;
        }
        
        @Override
        public int size() {
            try {
                return getCount(query);
            } catch(Exception e) {
                throw new RuntimeException("Failed to count features", e);
            }
        }
        @Override
        public synchronized ReferencedEnvelope getBounds() {
            try {
                return CachingFeatureSource.this.getBounds(query);
            } catch(Exception e) {
                throw new RuntimeException("Failed to count features", e);
            }
        }
        
        
        @Override
        protected Iterator openIterator() {
            List features;
            synchronized (CachingFeatureSource.this) {
                try {
                    if (index == null || dirty || !isSubQuery(query)) {
                        fillCache(query);
                    }
                    if(queryBounds != null) {
                        features = index.query(queryBounds);
                    } else {
                        features = index.query((Envelope) index.getRoot().getBounds());
                    }
                } catch(Exception e) {
                    throw new RuntimeException("Failed to get data", e);
                }
            }
            Iterator it = features.iterator();
            if(query.getFilter() != null && Filter.INCLUDE.equals(query.getFilter())) {
                it = new FilteringIterator<Feature>(it, query.getFilter());
            }
            if(targetSchema != sourceSchema) {
                it = new ReTypingIterator(it, sourceSchema, targetSchema);
            }
            return it;
        }
        
        @Override
        protected void closeIterator(Iterator close) {
            // nothing to do there
        }
        
        @Override
        public SimpleFeatureCollection subCollection(Filter filter) {
            // get the new target envelope
            Envelope filterEnvelope = getEnvelope(filter);
            Envelope subEnvelope = queryBounds;
            if(filterEnvelope != null) {
                subEnvelope = subEnvelope.intersection(queryBounds);
            }
            if(subEnvelope.isNull()) {
                return new EmptyFeatureCollection(targetSchema);
            }
            
            // mix filters
            Query subQuery = new Query(query);
            Filter baseFilter = query.getFilter();
            if(baseFilter != null && !Filter.INCLUDE.equals(baseFilter)) {
                Filter mixed = ff.and(baseFilter, filter);
                subQuery.setFilter(mixed); 
            }
            
            return new CachingFeatureCollection(subEnvelope, sourceSchema, targetSchema, subQuery);
        }
        
    }

}
