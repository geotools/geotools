package org.geotools.data.collection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureListener;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ResourceInfo;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.EmptyFeatureCollection;
import org.geotools.data.store.ReTypingFeatureCollection;
import org.geotools.data.store.ReprojectingFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.collection.MaxSimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortBy;
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

/**
 * A FeatureSource using a spatial index to hold on to features and serve them up for fast display.
 * <p>
 * This is a port of Andrea's CachingFeatureSource (which is slightly more compliced and rebuilds
 * the cache as an origional feature source changes). Our implementation here knows up front that
 * the features are in memory and does its best to take advantage of the fact. A caching feature
 * source for fast data access.
 * <p>
 * Please note that this FeatureSource is strictly "read-only" and thus does not support feature
 * events.
 * </p>
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/main/src/main/java/org/geotools/data/collection/SpatialIndexFeatureSource.java $
 */
public class SpatialIndexFeatureSource implements SimpleFeatureSource {
    SpatialIndexFeatureCollection contents;

    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    private static final Set<Class> supportedFilterTypes = new HashSet<Class>(Arrays.asList(
            BBOX.class, Contains.class, Crosses.class, DWithin.class, Equals.class,
            Intersects.class, Overlaps.class, Touches.class, Within.class));

    public SpatialIndexFeatureSource(SpatialIndexFeatureCollection original) {
        this.contents = original;
    }

    public void addFeatureListener(FeatureListener listener) {
    }

    public void removeFeatureListener(FeatureListener listener) {
    }

    public DataStore getDataStore() {
        return null; // not applicable
    }

    public ReferencedEnvelope getBounds() throws IOException {
        return contents.getBounds();
    }

    public ReferencedEnvelope getBounds(Query query) throws IOException {
        return getFeatures(query).getBounds();
    }

    public int getCount(Query query) throws IOException {
        return getFeatures(query).size();
    }

    public SimpleFeatureType getSchema() {
        return contents.getSchema();
    }

    public SimpleFeatureCollection getFeatures() throws IOException {
        return contents;
    }

    public SimpleFeatureCollection getFeatures(Filter filter) throws IOException {
        Query query = new Query(getSchema().getName().getLocalPart(), filter);
        return getFeatures(query);
    }

    public SimpleFeatureCollection getFeatures(Query query) throws IOException {
        Envelope bounds = getEnvelope(query.getFilter());
        return getFeatureCollection(query, bounds);
    }

    private SimpleFeatureCollection getFeatureCollection(Query query, Envelope bounds)
            throws IOException {
        query = DataUtilities.resolvePropertyNames(query, getSchema());
        final int offset = query.getStartIndex() != null ? query.getStartIndex() : 0;
        if (offset > 0 & query.getSortBy() == null) {
            if (!getQueryCapabilities().supportsSorting(query.getSortBy())) {
                throw new IllegalStateException("Feature source does not support this sorting "
                        + "so there is no way a stable paging (offset/limit) can be performed");
            }
            Query copy = new Query(query);
            copy.setSortBy(new SortBy[] { SortBy.NATURAL_ORDER });
            query = copy;
        }
        SimpleFeatureCollection collection;
        // step one filter
        if (query.getFilter() != null && query.getFilter().equals(Filter.EXCLUDE)) {
            return new EmptyFeatureCollection(getSchema());
        }
        if (query.getFilter() != null && query.getFilter().equals(Filter.INCLUDE)) {
            collection = contents;
        }
        if (query.getFilter() != null && query.getFilter().equals(Filter.INCLUDE)) {
            collection = contents;
        } else {
            collection = contents.subCollection(query.getFilter());
        }
        // step two: reproject
        if (query.getCoordinateSystemReproject() != null) {
            collection = new ReprojectingFeatureCollection(collection, query
                    .getCoordinateSystemReproject());
        }
        // step two sort! (note this makes a sorted copy)
        if (query.getSortBy() != null && query.getSortBy().length != 0) {
            SimpleFeature array[] = collection.toArray(new SimpleFeature[collection.size()]);
            // Arrays sort is stable (not resorting equal elements)
            for (SortBy sortBy : query.getSortBy()) {
                Comparator<SimpleFeature> comparator = DataUtilities.sortComparator(sortBy);
                Arrays.sort(array, comparator);
            }
            ArrayList<SimpleFeature> list = new ArrayList<SimpleFeature>(Arrays.asList(array));
            collection = new ListFeatureCollection(getSchema(), list);
        }

        // step three skip to start and return max number of fetaures
        if (offset > 0 || !query.isMaxFeaturesUnlimited()) {
            long max = Long.MAX_VALUE;
            if (!query.isMaxFeaturesUnlimited()) {
                max = query.getMaxFeatures();
            }
            collection = new MaxSimpleFeatureCollection(collection, offset, max);
        }
        // step four - retyping
        // (It would be nice to do this earlier so as to not have all the baggage
        // of unneeded attributes)
        if (query.getPropertyNames() != Query.ALL_NAMES) {
            // rebuild the type and wrap the reader
            SimpleFeatureType schema = collection.getSchema();
            SimpleFeatureType target = SimpleFeatureTypeBuilder.retype(schema, query
                    .getPropertyNames());
            if (!target.equals(schema)) {
                collection = new ReTypingFeatureCollection(collection, target);
            }
        }
        return collection;
    }

    Envelope getEnvelope(Filter filter) {
        Envelope result = new Envelope();
        if (filter instanceof And) {
            Envelope bounds = new Envelope();
            for (Iterator iter = ((And) filter).getChildren().iterator(); iter.hasNext();) {
                Filter f = (Filter) iter.next();
                Envelope e = getEnvelope(f);
                if (e == null) {
                    return null;
                } else {
                    bounds.expandToInclude(e);
                }
            }
            result = bounds;
        } else if (filter instanceof BinarySpatialOperator) {
            BinarySpatialOperator gf = (BinarySpatialOperator) filter;
            if (supportedFilterTypes.contains(gf.getClass())) {
                Expression lg = gf.getExpression1();
                Expression rg = gf.getExpression2();
                if (lg instanceof Literal) {
                    Geometry g = (Geometry) ((Literal) lg).getValue();
                    if (rg instanceof PropertyName) {
                        result = g.getEnvelopeInternal();
                    }
                } else if (rg instanceof Literal) {
                    Geometry g = (Geometry) ((Literal) rg).getValue();
                    if (lg instanceof PropertyName) {
                        result = g.getEnvelopeInternal();
                    }
                }
            }
        }
        return result;
    }

    private BBOX bboxFilter(Envelope bbox) {
        return ff.bbox(contents.getSchema().getGeometryDescriptor().getLocalName(), bbox.getMinX(),
                bbox.getMinY(), bbox.getMaxX(), bbox.getMaxY(), null);
    }

    public ResourceInfo getInfo() {
        return null;
    }

    public Name getName() {
        return contents.getSchema().getName();
    }

    public QueryCapabilities getQueryCapabilities() {
        return new QueryCapabilities() {
            @Override
            public boolean isOffsetSupported() {
                return true;
            }
        };
    }

    public Set getSupportedHints() {
        HashSet hints = new HashSet();
        return hints;
    }

}
