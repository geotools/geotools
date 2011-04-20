package org.geotools.cql;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.logging.Logger;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.ecql.ECQL;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;

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
                System.out.println("2 - Comparison: 1000 >= POPULTATION");
                System.out.println("3 - like pattern using String 'aabbcc' LIKE '%bb%'");
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
                    comparisonPredicateECQL();
                    break;
                case 3:
                    likePredicateInString();
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
     * ECQL allow property names in the right hand of comparison predicate.
     * 
     * @throws Exception
     */
    static private void comparisonPredicateECQL() throws Exception {

        
        // comparisonPredicateECQL start
        Filter filter = ECQL.toFilter("1000 >= POPULTATION");
        // comparisonPredicateECQL end

        retrieveFeatures(filter);
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
