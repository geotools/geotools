package org.geotools.cql;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.ecql.ECQL;
import org.opengis.filter.Filter;

/**
 * This class gathers up the ECQL examples shown in the sphinx documentation.
 * 
 * WORKING IN PROGESS 
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

                System.out.println("Select the Example:");
                System.out.println("1 - CQL Compatibility");
                System.out.println("2 - ECQL Comparison");
                System.out.println("...");
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
        SimpleFeatureSource featureSource = null;
        SimpleFeatureCollection features = featureSource.getFeatures(filter);
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
            System.out.println("the filter for");
        }
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

    static private void likePredicate() throws Exception {
        // ecql comparison start
        Filter filter = ECQL.toFilter("CITY_NAME LIKE 'Ar%'");
        // ecql comparison end
        retrieveFeatures(filter);
    }

    static private void ilikePredicate() throws Exception {

        // ecql ILIKE start
        Filter filter = ECQL.toFilter("CITY_NAME ILIKE 'Ar%'");
        // ecql ILIKE end
        retrieveFeatures(filter);
    }

    static void inPredicate() throws Exception {
        // ecql IN predicate start
        Filter filter = ECQL.toFilter("length IN (4100001,4100002, 4100003 )");
        // ecql IN predicate end

        retrieveFeatures(filter);
    }
}
