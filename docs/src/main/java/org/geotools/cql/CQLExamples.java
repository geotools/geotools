package org.geotools.cql;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.text.cql2.CQL;
import org.opengis.filter.Filter;

public class CQLExamples {

void cqlExample() throws Exception {
    SimpleFeatureSource featureSource = null;
    // cqlExample start
    Filter filter = CQL.toFilter("attName >= 5");
    SimpleFeatureCollection features = featureSource.getFeatures(filter);
    // cqlExample end
}
}
