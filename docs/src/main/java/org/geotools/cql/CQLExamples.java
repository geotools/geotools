package org.geotools.cql;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.text.cql2.CQL;
import org.opengis.filter.Filter;

public class CQLExamples {

    void comparisonPredicate() throws Exception {
        SimpleFeatureSource featureSource = null;
        // cql comparison start
        Filter filter = CQL.toFilter("attName >= 5");
        SimpleFeatureCollection features = featureSource.getFeatures(filter);
        // cql comparison end
    }	
	
}
