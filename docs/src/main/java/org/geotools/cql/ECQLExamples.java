package org.geotools.cql;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.expression.Expression;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

/**
 * This class gathers up the ECQL examples shown in the sphinx documentation.
 * 
 * WORK IN PROGESS 
 * 
 * @author Mauricio Pazos
 * 
 */
public class ECQLExamples {

    private final static Logger LOGGER = Logger.getLogger(ECQLExamples.class
            .getName());

    public static void main(String[] args) {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    System.in));
            for (;;) {

                System.out.println("Select the ECQL Example:");
                System.out.println("1 - CQL Compatibility");
                System.out.println("2 - Comparison: 1000 <= POPULTATION");
                System.out.println("3 - like pattern using String 'aabbcc' LIKE '%bb%'");
                System.out.println("4 - Comparison: (under18YearsOld * 19541453 / 100 ) < (over65YearsOld * 19541453 / 100 )");
                System.out.println("5 - population Between 10000 and 20000");
                System.out.println("6 - 2006-11-30T01:00:00Z AFTER 2006-11-30T01:30:00Z");
                System.out.println("7 - 2006-11-30T01:00:00Z BEFORE 2006-11-30T01:30:00Z");
                
                
                System.out.println("0 - quite");
                System.out.print(">");

                String line = reader.readLine();
                int option = Integer.parseInt(line);
                switch (option) {
                case 0: 
                    System.out.println("bye!");
                    return;
                case 1:
                    comparisonPredicateCQLCompatibility();
                    break;
                case 2:
                    expressionLessThanOrEqualToProperty();
                    break;
                case 3:
                    likePredicateInString();
                    break;
                case 4:
                    comparisonUsingExpressions();
                    break;
                case 5:
                    betweenPredicate();
                    break;
                case 6:
                    afterPredicate();
                    break;
                case 7:
                    beforePredicate();
                    break;
                    
                default:
                    System.out.println("invalid option");
                }
                System.out.println();
            }

        } catch (Exception e) {

            LOGGER.severe(e.getMessage());
        }
    }


    private static void afterPredicate() throws Exception{
        Filter filter = ECQL.toFilter("2006-11-30T01:00:00Z AFTER 2006-11-30T01:30:00Z");
		prittyPrintFilter(filter);
		
        Boolean result = filter.evaluate(null);
        System.out.println("Result of filter evaluation: " + result);
		
	}
    private static void beforePredicate() throws Exception{
        Filter filter = ECQL.toFilter("2006-11-30T01:00:00Z BEFORE 2006-11-30T01:30:00Z");
		prittyPrintFilter(filter);
		
        Boolean result = filter.evaluate(null);
        System.out.println("Result of filter evaluation: " + result);
		
	}


	/**
     * Retrieves the features from a data store using the filter
     * 
     * @param filter
     * @throws Exception
     */
    static private void retrieveFeatures(Filter filter) throws Exception {
        SimpleFeatureSource featureSource = null; // TODO open a feature store
        
        SimpleFeatureCollection features = featureSource.getFeatures(filter);
        SimpleFeatureIterator iter = features.features();
        while(iter.hasNext() ) {
            SimpleFeature feature = iter.next();
            System.out.println(feature.toString());
        }
        iter.close();
    }

    /**
     * This example shows that ECQL syntax is compatible with CQL.
     * 
     * @throws Exception
     */
    static private void comparisonPredicateCQLCompatibility() throws Exception {

        // comparisonCQLCompatibility start
        Filter filter = ECQL.toFilter("POPULTATION >= 1000");
        // comparisonCQLCompatibility end

        // the same syntax, now using CQL parser will produce the same filter
        Filter filterFromCQL = CQL.toFilter("POPULTATION >= 1000");

        if (!filter.equals(filterFromCQL)) {
            throw new Exception("uncompatible ECQL Syntax");
        } else {
            System.out.println("CQL and ECQL have produced the same filter for the predicate \"POPULTATION >= 1000\"");
            prittyPrintFilter( filter );
        }
    }

    private static void prittyPrintFilter(Filter filter) {
        System.out.println("The filter result is:\n" + filter.toString());         
    }

    /**
     * ECQL allows expressions in the left hand of comparison predicate.
     * 
     * @throws Exception
     */
    static private void expressionLessThanOrEqualToProperty() throws Exception {

        
        // expressionLessThanOrEqualToProperty start
        Filter filter = ECQL.toFilter("1000 <= POPULTATION");
        // expressionLessThanOrEqualToProperty end
        
        retrieveFeatures(filter);
    }

    /**
     * Comparison between persons under 18 years old and over 65 years old
     * @throws Exception
     */
    static private void comparisonUsingExpressions() throws Exception {
        
        // comparisonUsingExpressions start
        Filter filter = ECQL.toFilter("(under18YearsOld * 19541453 / 100 ) < (over65YearsOld * 19541453 / 100 )");
        // comparisonUsingExpressions end
        SimpleFeature city = createCity();
        
        prittyPrintFilter(filter);

        PropertyIsLessThan lessThan =(PropertyIsLessThan) filter; 
        Expression leftExpr = lessThan.getExpression1();
        Expression rightExpr = lessThan.getExpression2();
        System.out.println("left expression value: " + leftExpr.evaluate(city));
        System.out.println("right expression value: " + rightExpr.evaluate(city));
        
        Boolean result = lessThan.evaluate(city);
        System.out.println("Result of filter evaluation: " + result);
    }
    
    private static void betweenPredicate() throws Exception {
        
        // betweenPredicate start
        Filter filter = ECQL.toFilter("population BETWEEN 10000000 and 20000000");
        // betweenPredicate end
        prittyPrintFilter(filter);

        SimpleFeature city = createCity();

        System.out.println("City population: " + city.getProperty("population").getValue());
        
        Boolean result = filter.evaluate(city);
        System.out.println("Result of filter evaluation: " + result);
    }

    private static void betweenUsingExpression() throws Exception {
        
        // betweenUsingExpression start
        Filter filter = ECQL.toFilter("area( shape ) BETWEEN 10000 AND 30000");
        // betweenUsingExpression end
        prittyPrintFilter(filter);

        SimpleFeature country = createCountry();

        Boolean result = filter.evaluate(country);
        System.out.println("Result of filter evaluation: " + result);
    }
    
    private static SimpleFeature createCountry() throws Exception {

        final SimpleFeatureType type = DataUtilities.createType("Location",
                "geometry:Polygon:srid=4326," + 
                "countryName:String," + 
                "population:Integer" 
        );
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);

        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
        WKTReader reader = new WKTReader();
        Polygon geometry = (Polygon) reader.read(""); // FIXME implement me

        featureBuilder.add(geometry);
        featureBuilder.add("USA");
        featureBuilder.add(307006550);
        
        SimpleFeature feature = featureBuilder.buildFeature(null);

        return feature;
    }


    /**
     * Creates a feature that represent New York city
     * @return a Feature
     * @throws Exception
     */
    static private SimpleFeature createCity() throws Exception {

        final SimpleFeatureType type = DataUtilities.createType("Location",
                "geometry:Point:srid=4326," + 
                "cityName:String," + 
                "over65YearsOld:Double,"+ 
                "under18YearsOld:Double," +
                "population:Integer" +
                "earthQuake: Date"
        );
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);

        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
        
        Point point = geometryFactory.createPoint(new Coordinate(-17.2053, 11.9517));

        featureBuilder.add(point);
        featureBuilder.add("New York");
        featureBuilder.add(22.6);
        featureBuilder.add(13.4);
        featureBuilder.add(19541453);
        
        SimpleFeature feature = featureBuilder.buildFeature(null);

        return feature;
}
    
    /**
     *  Matching a property with a text pattern (case sensitive)
     * 
     * @throws Exception
     */
    static private void likePredicate() throws Exception {
        // ecql likePredicate start
        Filter filter = ECQL.toFilter("CITY_NAME LIKE 'Ar%'");
        // ecql likePredicate end
        retrieveFeatures(filter);
    }

    /**
     *  Matching a property with a text pattern (case insensitive)
     * 
     * @throws Exception
     */
    static private void ilikePredicate() throws Exception {

        // ecql ilikePredicate start
        Filter filter = ECQL.toFilter("CITY_NAME ILIKE 'Ar%'");
        // ecql ilikePredicate end
        retrieveFeatures(filter);
    }

    /**
     * Matching a literal string
     * 
     * @throws Exception
     */
    static private void likePredicateInString() throws Exception {
        // ecql likePredicateInString start
        Filter filter = ECQL.toFilter("'aabbcc' LIKE '%bb%'");
        // ecql likePredicateInString end
        
        prittyPrintFilter(filter);
    }

    static void inPredicate() throws Exception {
        // ecql IN predicate start
        Filter filter = ECQL.toFilter("length IN (4100001,4100002, 4100003 )");
        // ecql IN predicate end

        retrieveFeatures(filter);
    }
    
    
}
