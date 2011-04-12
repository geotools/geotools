package org.geotools.cql;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.text.cql2.CQL;
import org.opengis.filter.Filter;

public class ECQLExamples {

	
	void likePredicate() throws Exception {
		
		SimpleFeatureSource featureSource = null;
		// cql comparison  start
		Filter filter = CQL.toFilter("CITY_NAME LIKE 'Ar%'");
		SimpleFeatureCollection features = featureSource.getFeatures(filter);
		// cql comparison  end
	}
	
	void ilikePredicate() throws Exception {
		
		SimpleFeatureSource featureSource = null;
		// cql comparison  start
		Filter filter = CQL.toFilter("CITY_NAME ILIKE 'Ar%'");
		SimpleFeatureCollection features = featureSource.getFeatures(filter);
		// cql comparison  end
	}
	

	void inPredicate() throws Exception {
		
		SimpleFeatureSource featureSource = null;
		// cql comparison  start
		Filter filter = CQL.toFilter("length IN (4100001,4100002, 4100003 )");
		SimpleFeatureCollection features = featureSource.getFeatures(filter);
		// cql comparison  end
	}
	
	
}
