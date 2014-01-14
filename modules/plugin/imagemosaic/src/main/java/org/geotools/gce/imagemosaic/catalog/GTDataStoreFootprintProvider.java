package org.geotools.gce.imagemosaic.catalog;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.expression.PropertyName;

import com.vividsolutions.jts.geom.Geometry;

/**
 * A {@link FootprintGeometryProvider} matching the current feature with the geometry of one feature in a GT
 * data store. The filter must use property names like "granule/attname" to refer to the current
 * granule attributes, e.g. "granule/location"
 * 
 * @author Andrea Aime - GeoSolutions
 */
class GTDataStoreFootprintProvider implements FootprintGeometryProvider {

    static final Logger LOGGER = Logging.getLogger(GTDataStoreFootprintProvider.class);

    private SimpleFeatureSource featureSource;

    private Filter filter;

    private DataStore store;

    public GTDataStoreFootprintProvider(Map<String, Serializable> params, String typeName,
            Filter filter) throws IOException {
        store = DataStoreFinder.getDataStore(params);
        if (store == null) {
            throw new IOException("Coould not create footprint data store from params: " + params);
        }
        if (typeName != null) {
            this.featureSource = store.getFeatureSource(typeName);
        } else {
            this.featureSource = store.getFeatureSource("footprints");
        }
        this.filter = filter;
    }

    @Override
    public Geometry getFootprint(SimpleFeature feature) throws IOException {
        // replace granule/att with values
        Filter localFilter = (Filter) filter.accept(new GranuleFilterVisitor(feature), null);
        SimpleFeatureCollection fc = featureSource.getFeatures(localFilter);
        SimpleFeatureIterator fi = null;
        try {
            fi = fc.features();
            // no match? a bit weird, but possible, people might want to add footprints only
            // to the files that do have a significant one (e.g., not the bbox of the granule)
            Geometry result = null;
            if (fi.hasNext()) {
                SimpleFeature sf = fi.next();
                result = (Geometry) sf.getDefaultGeometry();
                if (fi.hasNext()) {
                    throw new IOException("The filter " + localFilter
                            + " matched more than one footprint record, in particular, it matched "
                            + fc.size() + ", the first match is: " + sf);
                }
            }

            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Returning footprint " + result + " for granule " + feature);
            }

            return result;
        } finally {
            if (fi != null) {
                fi.close();
            }
        }
    }

    /**
     * Replaces all references to granule/attribute with the value of said attribute in the feature
     * provided as a parameter
     * 
     * @author Andrea Aime - GeoSolutions
     */
    public class GranuleFilterVisitor extends DuplicatingFilterVisitor implements FilterVisitor {

        private static final String GRANULE_PREFIX = "granule/";

        private SimpleFeature feature;

        public GranuleFilterVisitor(SimpleFeature feature) {
            this.feature = feature;
        }

        @Override
        public Object visit(PropertyName expression, Object extraData) {
            String pname = expression.getPropertyName();
            // if it's a reference to the granule, do a replacement of the reference properties
            // with their actual value
            if (pname != null && pname.startsWith(GRANULE_PREFIX)) {
                String attName = pname.substring(GRANULE_PREFIX.length());
                return ff.literal(feature.getAttribute(attName));
            }

            return super.visit(expression, extraData);
        }
    }

    @Override
    public void dispose() {
        store.dispose();
    }
}
