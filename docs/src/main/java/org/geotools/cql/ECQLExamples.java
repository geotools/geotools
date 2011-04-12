package org.geotools.cql;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.text.ecql.ECQL;
import org.opengis.filter.Filter;

public class ECQLExamples {

    void likePredicate() throws Exception {
        SimpleFeatureSource featureSource = null;
        // ecql comparison start
        Filter filter = ECQL.toFilter("CITY_NAME LIKE 'Ar%'");
        SimpleFeatureCollection features = featureSource.getFeatures(filter);
        // ecql comparison end
    }

    void ilikePredicate() throws Exception {

        SimpleFeatureSource featureSource = null;
        // ecql ILIKE start
        Filter filter = ECQL.toFilter("CITY_NAME ILIKE 'Ar%'");
        SimpleFeatureCollection features = featureSource.getFeatures(filter);
        // ecql ILIKE end
    }

    void inPredicate() throws Exception {
        SimpleFeatureSource featureSource = null;
        // ecql IN predicate start
        Filter filter = ECQL.toFilter("length IN (4100001,4100002, 4100003 )");
        SimpleFeatureCollection features = featureSource.getFeatures(filter);
        // ecql IN predicate end
    }
}
