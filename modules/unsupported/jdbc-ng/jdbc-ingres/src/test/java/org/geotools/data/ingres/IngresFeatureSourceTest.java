package org.geotools.data.ingres;

import java.util.Iterator;

import org.geotools.data.DefaultQuery;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCFeatureSourceTest;
import org.geotools.jdbc.JDBCFeatureStore;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.And;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.filter.spatial.BBOX;

public class IngresFeatureSourceTest extends JDBCFeatureSourceTest {
//    ContentFeatureSource featureSource;

	@Override
    protected JDBCTestSetup createTestSetup() {
        return new IngresTestSetup();
    }
}
